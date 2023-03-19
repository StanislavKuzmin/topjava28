package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.BeanValidationUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanValidationUtil.validate(user);
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            batchInsertRole(user);
        } else {
            if (namedParameterJdbcTemplate.update("""
                       UPDATE users SET name=:name, email=:email, password=:password,
                       registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                    """, parameterSource) == 0) {
                return null;
            }
            jdbcTemplate.update("DELETE FROM user_role WHERE user_id=?", user.getId());
            batchInsertRole(user);
        }
        return user;
    }

    private int[] batchInsertRole(User user) {
        Set<Role> roles = user.getRoles();
        if (!CollectionUtils.isEmpty(roles)) {
            List<Role> listRole = new ArrayList<>(roles);
            return jdbcTemplate.batchUpdate(
                    "insert into user_role (user_id, role) values (?,?)",
                    new BatchPreparedStatementSetter() {

                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setInt(1, user.getId());
                            ps.setString(2, String.valueOf(listRole.get(i)));
                        }

                        @Override
                        public int getBatchSize() {
                            return listRole.size();
                        }
                    });
        }
        return new int[0];
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * " +
                        "FROM users u" +
                        " LEFT JOIN user_role ur on u.id = ur.user_id WHERE u.id=?",
                JdbcUserRepository::extractUser, id);
        return DataAccessUtils.singleResult(users);
    }

    private static List<User> extractUser(ResultSet rs) throws SQLException {
        Map<Integer, User> map = new LinkedHashMap<>();
        while (rs.next()) {
            int id = rs.getInt("id");
            User user = map.get(id);
            if (user == null) {
                user = new User(id, rs.getString("name"), rs.getString("email"),
                        rs.getString("password"), rs.getInt("calories_per_day"),
                        rs.getBoolean("enabled"), rs.getDate("registered"), new ArrayList<>());
                map.put(id, user);
            }
            String role = rs.getString("role");
            if (role == null) {
                user.setRoles(Collections.emptyList());
            } else {
                user.addRole(Role.valueOf(role));
            }
        }
        return CollectionUtils.isEmpty(map) ? Collections.emptyList() : new ArrayList<>(map.values());
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * " +
                        "FROM users u" +
                        " LEFT JOIN user_role ur on u.id = ur.user_id WHERE u.email=?",
                JdbcUserRepository::extractUser, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * " +
                "FROM users u" +
                " LEFT JOIN user_role ur on u.id = ur.user_id" +
                " ORDER BY u.name, u.email", JdbcUserRepository::extractUser);
    }
}

[![Codacy Badge](https://app.codacy.com/project/badge/Grade/e8c1353b5585419cbde2bb49e6dba059)](https://www.codacy.com/gh/StanislavKuzmin/topjava28/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=StanislavKuzmin/topjava28&amp;utm_campaign=Badge_Grade)

Calories Management 
===============================
Java Enterprise проект с регистрацией/авторизацией и правами доступа на основе ролей (USER, ADMIN).Администратор может создавать/редактировать/удалять пользователей, а пользователи - управлять своим профилем и данными (едой) через UI (по AJAX) и по REST интерфейсу с базовой авторизацией. Возможна фильтрация еды по датам и времени. Цвет записи таблицы еды зависит от того, превышает ли сумма калорий за день норму (редактируемый параметр в профиле пользователя). Весь REST интерфейс покрывается JUnit тестами, используя Spring MVC Test и Spring Security Test.

Stack: Maven, Spring MVC, Security, JPA(Hibernate), REST(Jackson), Bootstrap (css,js), DataTables, jQuery + plugins, Java 8 Stream and Time API, Postgresql, HSQLDB.

## Демо

[Менеджмент калорий](http://stasonhd2.fvds.ru/topjava/)

## Системные требования

* Браузер
* Linux or macOs or Windows
* [JDK 17+](http://jdk.java.net/17/)
* [Maven](https://maven.apache.org/)
* [Bash for Windows](https://git-scm.com/downloads)
* [Tomcat 9](https://tomcat.apache.org/download-90.cgi#9.0.86)

## Сборка

```
# Клонировать репозиторий
$ git clone https://github.com/StanislavKuzmin/topjava28

# Зайти в корневую папку
$ cd topjava28

# Сборка
$ mvn clean package

# Запустить Tomcat локально на компьютере
# Скопировать target/topjava.war в папку tomcat/webapps
# Ввести ссылку в браузере: http://localhost:8080/topjava 
```

## Использование
[REST API documentation](http://stasonhd2.fvds.ru/topjava/swagger-ui.html)  

## Тесты

Запуск: `mvn clean test` in root directory.

## Благодарности
[Grigory Kislin](https://javaops.ru/#contacts)

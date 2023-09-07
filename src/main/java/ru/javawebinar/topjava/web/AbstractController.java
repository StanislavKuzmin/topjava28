package ru.javawebinar.topjava.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.javawebinar.topjava.to.BaseTo;

import javax.validation.Valid;
import java.util.stream.Collectors;

public interface AbstractController<S extends BaseTo> {

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    default ResponseEntity<String> createOrUpdateWithMessage(@Valid S s, BindingResult result) {
        if (result.hasErrors()) {
            String errorFieldsMsg = result.getFieldErrors().stream()
                    .map(fe -> String.format("[%s] %s", fe.getField(), fe.getDefaultMessage()))
                    .collect(Collectors.joining("<br>"));
            return ResponseEntity.unprocessableEntity().body(errorFieldsMsg);
        }
        createOrUpdate(s);
        return ResponseEntity.ok().build();
    }
    void createOrUpdate(S s);
}

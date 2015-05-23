package org.taskstodo.controller;

import org.springframework.web.bind.annotation.ExceptionHandler;

public class AbstractController {

    @ExceptionHandler(Exception.class)
    private Exception handleException(Exception exception) {
        return exception;
    }
}

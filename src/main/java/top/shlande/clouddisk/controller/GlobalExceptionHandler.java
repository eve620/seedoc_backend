package top.shlande.clouddisk.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import top.shlande.clouddisk.user.BadRequestException;
import top.shlande.clouddisk.user.DenyException;
import top.shlande.clouddisk.user.NotFoundException;
import top.shlande.clouddisk.vfs.NilDirException;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    public static class Message {
        String message;

        public Message(String message) {
            this.message = message;
        }
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "access deny")
    @ExceptionHandler(DenyException.class)
    public String deny(DenyException exception) {
        return exception.toString();
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "bad request")
    @ExceptionHandler({NotFoundException.class, NilDirException.class, BadRequestException.class})
    public String badRequest(Exception exception) {
        return exception.toString();
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "internal server error")
    @ExceptionHandler(Exception.class)
    public String other(Exception exception) {
        return exception.toString();
    }
}

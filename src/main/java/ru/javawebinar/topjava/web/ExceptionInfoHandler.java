package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.util.exception.ErrorInfo;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@ControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
public class ExceptionInfoHandler {
    private static Logger LOG = LoggerFactory.getLogger(ExceptionInfoHandler.class);

    //  http://stackoverflow.com/a/22358422/548473
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    public ErrorInfo handleError(HttpServletRequest req, NotFoundException e) {
        return logAndGetErrorInfo(req, e, false);
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)  // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    public ErrorInfo conflict(HttpServletRequest req, DataIntegrityViolationException e) {
        return logAndGetErrorInfo(req, e, true);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST) //400
    @ExceptionHandler(BindException.class)
    @ResponseBody
    @Order(Ordered.HIGHEST_PRECEDENCE + 2)
    public ErrorInfo bindValidationError(HttpServletRequest request, BindingResult result) {
        return logAndGetValidationErrorInfo(request, result);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST) //400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @Order(Ordered.HIGHEST_PRECEDENCE + 2)
    public ErrorInfo restValidationError(HttpServletRequest request, MethodArgumentNotValidException e) {
        return logAndGetValidationErrorInfo(request, e.getBindingResult());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ErrorInfo handleError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, true);
    }

    public ErrorInfo logAndGetValidationErrorInfo(HttpServletRequest request, BindingResult result) {
        String[] details = result.getFieldErrors().stream()
                .map(fe -> fe.getField() + ' ' + fe.getDefaultMessage()).toArray(String[]::new);

        LOG.warn("Validation exception at reques " + request.getRequestURI() + ":" + Arrays.toString(details));
        return new ErrorInfo(request.getRequestURL(), "ValidationException", details);
    }

    private static ErrorInfo logAndGetErrorInfo(HttpServletRequest req, Exception e, boolean logException) {
        Throwable rootCause = ValidationUtil.getRootCause(e);
        if (logException) {
            LOG.error("Exception at request " + req.getRequestURL(), rootCause);
        } else {
            LOG.warn("Exception at request " + req.getRequestURL() + ": " + rootCause.toString());
        }
        return new ErrorInfo(req.getRequestURL(), rootCause);
    }
}
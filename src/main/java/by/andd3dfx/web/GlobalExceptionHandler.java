package by.andd3dfx.web;

import by.andd3dfx.dto.ApiErrorResponse;
import by.andd3dfx.dto.FieldViolation;
import by.andd3dfx.exceptions.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiErrorResponse> handleBindException(BindException ex, HttpServletRequest request) {
        return ResponseEntity.badRequest().body(ApiErrorResponse.validation(
            request.getRequestURI(),
            collectBindingViolations(ex.getBindingResult().getFieldErrors(), ex.getBindingResult().getGlobalErrors())
        ));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(
        ConstraintViolationException ex,
        HttpServletRequest request
    ) {
        List<FieldViolation> violations = ex.getConstraintViolations().stream()
            .map(v -> new FieldViolation(v.getPropertyPath().toString(), v.getMessage()))
            .toList();
        return ResponseEntity.badRequest().body(ApiErrorResponse.validation(request.getRequestURI(), violations));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleNotReadable(
        HttpMessageNotReadableException ex,
        HttpServletRequest request
    ) {
        log.debug("Malformed request body: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(ApiErrorResponse.of(
            HttpStatus.BAD_REQUEST.value(),
            "Bad Request",
            "Malformed JSON request",
            request.getRequestURI()
        ));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleTypeMismatch(
        MethodArgumentTypeMismatchException ex,
        HttpServletRequest request
    ) {
        String msg = "Invalid value for parameter '%s'".formatted(ex.getName());
        return ResponseEntity.badRequest().body(ApiErrorResponse.of(
            HttpStatus.BAD_REQUEST.value(),
            "Bad Request",
            msg,
            request.getRequestURI()
        ));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(NotFoundException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiErrorResponse.of(
            HttpStatus.NOT_FOUND.value(),
            "Not Found",
            ex.getMessage(),
            request.getRequestURI()
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnhandled(Exception ex, HttpServletRequest request) {
        log.error("Unhandled error on {}", request.getRequestURI(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiErrorResponse.of(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error",
            "An unexpected error occurred",
            request.getRequestURI()
        ));
    }

    private static List<FieldViolation> collectBindingViolations(
        List<FieldError> fieldErrors,
        List<ObjectError> globalErrors
    ) {
        List<FieldViolation> violations = new ArrayList<>();
        for (FieldError fe : fieldErrors) {
            violations.add(new FieldViolation(fe.getField(), fe.getDefaultMessage()));
        }
        for (ObjectError ge : globalErrors) {
            violations.add(new FieldViolation("body", ge.getDefaultMessage()));
        }
        return violations;
    }
}

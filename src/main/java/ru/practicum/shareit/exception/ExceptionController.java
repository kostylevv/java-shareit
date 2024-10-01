package ru.practicum.shareit.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Set<ErrorResponse> handleValidationExceptions(
            final MethodArgumentNotValidException ex) {
        Set<ErrorResponse> responseSet = new HashSet<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            ErrorResponse errorResponse = new ErrorResponse(fieldName, errorMessage);
            responseSet.add(errorResponse);
        });
        log.error("Method args validation exception {}", ex.getMessage());
        return responseSet;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleConstraintViolationExceptions(ConstraintViolationException ex) {
        log.error("Constraint violation exception {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public ErrorResponse handleBadRequest(
            final Exception ex) {
        log.error("Bad request exception {}", ex.getMessage());
        return new ErrorResponse("Bad request exception", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public ErrorResponse handleNFExceptions(
            final NotFoundException ex) {
        log.error("Not found exception {}", ex.getMessage());
        return new ErrorResponse("Объект не найден", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler
    public ErrorResponse handleDuplicatedData(
            final DuplicatedDataException ex) {
        log.error("Duplicate data exception {}", ex.getMessage());
        return new ErrorResponse("Duplicated Data", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResponse handleGeneralException(
            final Exception ex) {
        log.error("Internal server error {}", ex.getMessage());
        return new ErrorResponse("Произошло исключение", ex.getMessage());
    }
}

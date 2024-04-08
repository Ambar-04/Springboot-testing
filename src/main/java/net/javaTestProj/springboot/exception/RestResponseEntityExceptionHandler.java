package net.javaTestProj.springboot.exception;


import net.javaTestProj.springboot.model.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

//extends ResponseEntityExceptionHandler
@ControllerAdvice
@ResponseStatus
public class RestResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<Object> employeeNotFoundException(ResourceNotFoundException exception, WebRequest request){ //We are not using request here,just for knowledge purpose
        ErrorMessage message = new ErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());

        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        //return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    @ExceptionHandler(value = {DuplicateResourceException.class})
    public ResponseEntity<Object> duplicateEmployeeFoundException(ResourceNotFoundException exception, WebRequest request){ //We are not using request here,just for knowledge purpose
        ErrorMessage message = new ErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage());

        return  new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        //return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessage> handleException(Exception exception){ //We are not using request here,just for knowledge purpose
        ErrorMessage  message = new ErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());

        return  new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        //return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

}

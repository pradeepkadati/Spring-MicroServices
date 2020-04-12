package com.appsdeveloperblog.app.ws.exception;

import com.appsdeveloperblog.app.ws.ui.model.response.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> handleAnyException(Exception ex, WebRequest request){
        String emsg = ex.getLocalizedMessage();

        if (emsg == null) {
            emsg = ex.toString();
        }

        ErrorMessage message = new ErrorMessage(emsg,new Date());

        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(value = {NullPointerException.class,UserServiceException.class})
    public ResponseEntity<Object> handleSpecifcException(Exception ex, WebRequest request){
        String emsg = ex.getLocalizedMessage();

        if (emsg == null) {
            emsg = ex.toString();
        }

        ErrorMessage message = new ErrorMessage(emsg + "null handler",new Date());

        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    
}

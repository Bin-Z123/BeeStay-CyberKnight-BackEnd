package com.poly.beestaycyberknightbackend.service.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.poly.beestaycyberknightbackend.domain.RestResponse;

@RestControllerAdvice
 public class GlobalException {
    //  @ExceptionHandler(value = {
    //          UsernameNotFoundException.class,
    //          BadCredentialsException.class
    //  })
    //  public ResponseEntity<RestResponse<Object>> handleIdException(Exception ex) {
    //      RestResponse<Object> res = new RestResponse<Object>();
    //      res.setStatusCode(HttpStatus.BAD_REQUEST.value());
    //      res.setError(ex.getMessage());
    //      res.setMessage("Exception occurs...");
    //      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    //  }
 
    //  @ExceptionHandler(MethodArgumentNotValidException.class)
    //  public ResponseEntity<RestResponse<Object>> validationError(MethodArgumentNotValidException ex) {
    //      BindingResult result = ex.getBindingResult();
    //      final List<FieldError> fieldErrors = result.getFieldErrors();
 
    //      RestResponse<Object> res = new RestResponse<Object>();
    //      res.setStatusCode(HttpStatus.BAD_REQUEST.value());
    //      res.setError(ex.getBody().getDetail());
 
    //      List<String> errors = fieldErrors.stream().map(f -> f.getDefaultMessage()).collect(Collectors.toList());
    //      res.setMessage(errors.size() > 1 ? errors : errors.get(0));
 
    //      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    //  }
    @ExceptionHandler(value = IdInvalidException.class)
    public ResponseEntity<RestResponse<Object>> handleIdException(IdInvalidException idException) {
        RestResponse<Object> res = new RestResponse<Object>();
         res.setStatusCode(HttpStatus.BAD_REQUEST.value());
         res.setError(idException.getMessage());
         res.setMessage("Exception occurs...");
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }
}
    
    
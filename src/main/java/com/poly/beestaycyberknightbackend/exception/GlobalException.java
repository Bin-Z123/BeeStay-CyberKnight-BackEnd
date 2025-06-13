package com.poly.beestaycyberknightbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.poly.beestaycyberknightbackend.dto.response.ApiResponse;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(value = {      
            UsernameNotFoundException.class,
            BadCredentialsException.class
    })
    public ResponseEntity<ApiResponse> handleIdException(Exception ex) {
        ApiResponse res = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), null);
        // res.setCode(HttpStatus.BAD_REQUEST.value());
        // res.setError(ex.getMessage());
        // res.setMessage("Exception occurs...");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    // @ExceptionHandler(ResourceNotFoundException.class)
    // public ResponseEntity<RestResponse<Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
    //     RestResponse<Object> res = new RestResponse<>();
    //     res.setStatusCode(HttpStatus.NOT_FOUND.value());
    //     res.setError(ex.getMessage());
    //     res.setMessage("Tài nguyên không được tìm thấy");
    //     return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
    // }

    // @ExceptionHandler(MethodArgumentNotValidException.class)
    // public ResponseEntity<RestResponse<Object>> validationError(MethodArgumentNotValidException ex) {
    //     BindingResult result = ex.getBindingResult();
    //     final List<FieldError> fieldErrors = result.getFieldErrors();

    //     RestResponse<Object> res = new RestResponse<Object>();
    //     res.setStatusCode(HttpStatus.BAD_REQUEST.value());
    //     res.setError(ex.getBody().getDetail());

    //     List<String> errors = fieldErrors.stream().map(f -> f.getDefaultMessage()).collect(Collectors.toList());
    //     res.setMessage(errors.size() > 1 ? errors : errors.get(0));

    //     return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    // }

    //Bắt lỗi trong dự tính
    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ApiResponse> handlingAppException(AppException e){
        ErrorCode errorCode = e.getErrorCode();

        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }
}
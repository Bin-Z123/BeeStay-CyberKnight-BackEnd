package com.poly.beestaycyberknightbackend.util;

import com.poly.beestaycyberknightbackend.domain.dto.response.RestResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice
public class FormatRestResponse implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response) {
        HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
        int status = servletResponse.getStatus();

        // Không format nếu body là String hoặc status là 204 (No Content)
        if (body instanceof String || status == HttpStatus.NO_CONTENT.value()) {
            return body;
        }

        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(status);

        // Xử lý lỗi (status >= 400)
        if (status >= 400) {
            return body;
        }

        // Xử lý thành công
        res.setData(body);
        if (status == HttpStatus.CREATED.value()) {
            // Đặt message tùy theo loại tài nguyên
            if (body instanceof com.poly.beestaycyberknightbackend.domain.User) {
                res.setMessage("Tạo user thành công");
            } else if (body instanceof com.poly.beestaycyberknightbackend.domain.RoomType) {
                res.setMessage("Tạo roomtype thành công");
            } 
        } else {
            res.setMessage("CALL API SUCCESS");
        }

        return res;
    }
}
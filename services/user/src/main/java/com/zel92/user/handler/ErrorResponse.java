package com.zel92.user.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private Integer code;
    private HttpStatus status;
    private String reason;
    private Map<String, String> data;

    public ErrorResponse(Integer code, HttpStatus status, String reason){
        this.code = code;
        this.status = status;
        this.reason = reason;
    }

    public ErrorResponse(Map<String, String> data) {
        this.data = data;
    }
}

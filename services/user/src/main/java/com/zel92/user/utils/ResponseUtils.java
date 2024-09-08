package com.zel92.user.utils;

import com.zel92.user.domain.Response;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

import static java.time.LocalDateTime.*;

public class ResponseUtils {

    public static Response getResponse(HttpStatus status, HttpServletRequest request, String message, Map<String, String> map){
        return new Response(now().toString(), status.value(),
                request.getRequestURI(), HttpStatus.valueOf(status.value()),
                message, "", map);
    }
}

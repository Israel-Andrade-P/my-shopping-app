package com.zel92.user.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zel92.user.domain.Response;
import com.zel92.user.exception.GenericException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static java.time.LocalDateTime.now;
import static java.util.Collections.emptyMap;
import static org.apache.commons.lang.exception.ExceptionUtils.getRootCauseMessage;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class RequestUtils {

    private static final BiConsumer<HttpServletResponse, Response> writeResponse = (httpServletResponse, response) -> {
        try{
            var outputStream = httpServletResponse.getOutputStream();
            new ObjectMapper().writeValue(outputStream, response);
            outputStream.flush();
        }catch (Exception exception){
            throw new GenericException(exception.getMessage());
        }
    };

    private static final BiFunction<Exception, HttpStatus, String> errorReason = (exception, status) -> {
        //if (status.isSameCodeAs(FORBIDDEN)) {return "You do not have enough permission";}
        if (status.isSameCodeAs(UNAUTHORIZED)) {return "Email and/or Password incorrect";}
        if (exception instanceof DisabledException || exception instanceof LockedException || exception instanceof BadCredentialsException || exception instanceof GenericException){
            return exception.getMessage();
        }
        if (status.is5xxServerError()){
            return "An internal server error has occurred";
        }else {return "An error has occurred. Please try again";}
    };

    public static Response getResponse(HttpServletRequest request, Map<?,?> data, String message, HttpStatus status){
        return new Response(now().toString(), status.value(), request.getRequestURI(), HttpStatus.valueOf(status.value()), message, "", data);
    }

    public static void handleErrorResponse(HttpServletRequest request, HttpServletResponse response, Exception exception){
            Response apiResponse = getErrorResponse(request, response, exception);
            writeResponse.accept(response, apiResponse);
    }

//    private static HttpStatus getHttpStatus(Exception exception){
//        if (exception instanceof BadCredentialsException) return UNAUTHORIZED;
//        return FORBIDDEN;
//    }

    private static Response getErrorResponse(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(FORBIDDEN.value());
        return new Response(now().toString(), FORBIDDEN.value(), request.getRequestURI(), HttpStatus.valueOf(FORBIDDEN.name()), errorReason.apply(exception, FORBIDDEN), getRootCauseMessage(exception), emptyMap());
    }
}

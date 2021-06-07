package com.itsm.dranswer.security;

/*
 * @package : com.itsm.dranswer.security
 * @name : EntryPointUnauthorizedHandler.java
 * @date : 2021-06-07 오전 11:01
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class EntryPointUnauthorizedHandler implements AuthenticationEntryPoint {

    static String _401 = "{\"success\":false,\"response\":null,\"error\":{\"message\":\"Unauthorized\",\"status\":401}}";

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        String contentType = request.getHeader("Content-Type");

        if (contentType == null) {

        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setHeader("content-type", "application/json");
            response.getWriter().write(_401);
            response.getWriter().flush();
            response.getWriter().close();
        }
    }

}
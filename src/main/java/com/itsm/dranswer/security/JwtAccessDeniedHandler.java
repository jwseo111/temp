package com.itsm.dranswer.security;

/*
 * @package : com.itsm.dranswer.security
 * @name : JwtAccessDeniedHandler.java
 * @date : 2021-06-07 오전 11:00
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    static String _403 = "{\"success\":false,\"response\":null,\"error\":{\"message\":\"Forbidden\",\"status\":403}}";

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {

        String contentType = request.getHeader("Content-Type");

        if (contentType == null) {
            response.sendRedirect("/");
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setHeader("content-type", "application/json");
            response.getWriter().write(_403);
            response.getWriter().flush();
            response.getWriter().close();
        }

    }

}
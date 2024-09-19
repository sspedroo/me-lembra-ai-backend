package org.remind.melembraai.infra.security.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponse;

import java.io.IOException;

@Component
public class SecurityAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException
    {
        final ErrorResponse errorResponse = ErrorResponse
                .builder(accessDeniedException, HttpStatus.FORBIDDEN, accessDeniedException.getMessage())
                .header("errorType", accessDeniedException.getClass().getCanonicalName())
                .build();

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.getWriter().write(mapper.writeValueAsString(errorResponse));
    }
}

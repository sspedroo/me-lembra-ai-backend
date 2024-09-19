package org.remind.melembraai.infra.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.remind.melembraai.domain.user.service.UserService;
import org.remind.melembraai.infra.security.SecurityConfig;
import org.remind.melembraai.infra.security.exceptions.InvalidJwtRefreshTokenException;
import org.remind.melembraai.infra.security.service.JwtService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import org.springframework.web.ErrorResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserService userService;
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
        return SecurityConfig.isWhiteList(request) ||
                SecurityConfig.isPublic(request) ||
                SecurityConfig.isAuth(request);
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException
    {
        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.isEmpty(authorization) || !StringUtils.startsWith(authorization, SecurityConfig.BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        try{
            final String tokenJwt = authorization.substring(SecurityConfig.BEARER_PREFIX.length());
            final String username = jwtService.extractUsername(tokenJwt);

            if (StringUtils.isNotEmpty(username)) {
                final UserDetails userDetails = userService.loadUserByUsername(username);

                if (jwtService.isRefreshToken(tokenJwt)) {
                    throw new InvalidJwtRefreshTokenException();
                }

                if (jwtService.isTokenValid(tokenJwt, userDetails)) {
                    final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null,
                            userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        } catch (RuntimeException e) {
            final ErrorResponse errorResponse = ErrorResponse
                    .builder(e, HttpStatus.UNAUTHORIZED, e.getMessage())
                    .header("errorType", e.getClass().getCanonicalName())
                    .build();

            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(mapper.writeValueAsString(errorResponse));
            return;
        }

        filterChain.doFilter(request, response);
    }
}

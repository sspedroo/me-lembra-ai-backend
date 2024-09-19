package org.remind.melembraai.infra.security.service;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.remind.melembraai.domain.user.model.User;
import org.remind.melembraai.domain.user.service.UserService;
import org.remind.melembraai.infra.security.SecurityConfig;
import org.remind.melembraai.infra.security.domain.JwtAuthResponse;
import org.remind.melembraai.infra.security.exceptions.JwtRefreshTokenShouldBeInformedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;



    public JwtAuthResponse login(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        return createJwtAuthResponse(email);
    }

    public JwtAuthResponse refreshToken(String authorization) {
        final String jwt = authorization.substring(SecurityConfig.BEARER_PREFIX.length());
        final String email = jwtService.extractUsername(jwt);

        if (StringUtils.isNotEmpty(email)) {
            final UserDetails userDetails = userService.loadUserByUsername(email);

            if (!jwtService.isRefreshToken(jwt)) {
                throw new JwtRefreshTokenShouldBeInformedException();
            }

            if (jwtService.isTokenValid(jwt, userDetails)) {
                final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null,
                        userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        return createJwtAuthResponse(email);
    }

    private JwtAuthResponse createJwtAuthResponse(String email) {
        final UserDetails userDetails = userService.loadUserByUsername(email);

        final String token = jwtService.generateToken(userDetails);
        final String refreshToken = jwtService.generateRefreshToken(userDetails);
        final User user = (User) userDetails;


        return JwtAuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .email(user.getEmail())
                .username(user.getName())
                .isVerified(user.getIsVerified())
                .build();
    }
}

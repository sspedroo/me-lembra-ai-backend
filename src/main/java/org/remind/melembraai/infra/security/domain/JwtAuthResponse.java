package org.remind.melembraai.infra.security.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtAuthResponse {
    private final String token;
    private final String refreshToken;
    private final String username;
    private final String email;
    private final Boolean isVerified;
}

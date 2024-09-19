package org.remind.melembraai.infra.security.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class JwtRefreshTokenShouldBeInformedException extends RuntimeException {

    public JwtRefreshTokenShouldBeInformedException() {
        super();
    }

    @Override
    public String getMessage() {
        return "Invalid JWT = refresh-token must be provided";
    }
}

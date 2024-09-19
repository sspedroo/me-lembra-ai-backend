package org.remind.melembraai.domain.validationToken.service;

import lombok.RequiredArgsConstructor;
import org.remind.melembraai.domain.user.model.User;
import org.remind.melembraai.domain.validationToken.model.VerifiedToken;
import org.remind.melembraai.domain.validationToken.repository.VerifiedTokenRepository;
import org.remind.melembraai.utils.BRDatesUtils;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerifiedTokenService {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int TOKEN_LENGTH = 6;
    private final SecureRandom secureRandom = new SecureRandom();

    private final VerifiedTokenRepository verifiedTokenRepository;

    public VerifiedToken createToken(User user) {
        VerifiedToken newToken = VerifiedToken.builder()
                .token(generateToken())
                .user(user)
                .isUsed(false)
                .createdAt(LocalDateTime.now(BRDatesUtils.getBrazilZoneId()))
                .expiresAt(LocalDateTime.now(BRDatesUtils.getBrazilZoneId()).plusHours(24))
                .build();

        verifiedTokenRepository.save(newToken);
        return newToken;
    }

    public Boolean verifyToken(UUID userID, String token) {
        VerifiedToken verifiedToken = verifiedTokenRepository.findByUserIdAndTokenAndIsUsed(userID, token, false);

        if (verifiedToken == null) {
            return false;
        } else if (LocalDateTime.now(BRDatesUtils.getBrazilZoneId()).isAfter(verifiedToken.getExpiresAt())) {
            return false;
        }

        verifiedToken.markAsUsed();
        verifiedTokenRepository.save(verifiedToken);

        return true;
    }


    private String generateToken() {
        StringBuilder token = new StringBuilder(TOKEN_LENGTH);

        for (int i = 0; i < TOKEN_LENGTH; i++) {
            int randomIndex = secureRandom.nextInt(CHARACTERS.length());
            token.append(CHARACTERS.charAt(randomIndex));
        }

        return token.toString();
    }
}

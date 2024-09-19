package org.remind.melembraai.domain.validationToken.repository;

import org.remind.melembraai.domain.validationToken.model.VerifiedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VerifiedTokenRepository extends JpaRepository<VerifiedToken, Long> {
    VerifiedToken findByUserIdAndTokenAndIsUsed(UUID userID, String token, boolean b);
}


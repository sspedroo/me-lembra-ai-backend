package org.remind.melembraai.domain.email.repository;

import org.remind.melembraai.domain.email.model.RememberEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface EmailRepository extends JpaRepository<RememberEmail, UUID> {
   @Query("SELECT e FROM tb_emails e WHERE (e.sendDate = :today OR e.nextSentAt = :today) AND e.isFinished = false")
    List<RememberEmail> findAllBySendDate(@Param("today") LocalDate today);
}

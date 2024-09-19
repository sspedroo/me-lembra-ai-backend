package org.remind.melembraai.domain.validationToken.model;

import jakarta.persistence.*;
import lombok.*;
import org.remind.melembraai.domain.user.model.User;

import java.time.LocalDateTime;

@Entity(name = "verified_tokens")
@Table(name = "verified_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerifiedToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String token;
    private Boolean isUsed;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;

    public void markAsUsed() {
        isUsed = true;
    }
}

package org.remind.melembraai.domain.email.model;

import jakarta.persistence.*;
import lombok.*;
import org.remind.melembraai.domain.user.model.User;
import org.remind.melembraai.utils.BRDatesUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "tb_emails")
@Table(name = "tb_emails")
@EqualsAndHashCode
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RememberEmail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private String email;
    private String title;
    @Lob
    private String content;
    private Boolean isFinished;
    private Boolean hasRepetition;
    @Column(nullable = false)
    private LocalDate sendDate;
    private Integer repetitionIntervalDays;
    private Integer remainingRepetitions;
    private LocalDate nextSentAt;
    private LocalDateTime lastSentAt;
    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now(BRDatesUtils.getBrazilZoneId());
        updatedAt = createdAt;
        isFinished = false;
    }
    public void markAsFinished() {
        this.isFinished = true;
    }

    public void decrementRemainingRepetitions() {
        remainingRepetitions--;
    }
}

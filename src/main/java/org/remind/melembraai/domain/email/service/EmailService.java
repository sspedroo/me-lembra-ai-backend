package org.remind.melembraai.domain.email.service;

import lombok.RequiredArgsConstructor;
import org.remind.melembraai.domain.email.dto.CreateEmailReminderDTO;
import org.remind.melembraai.domain.email.exceptions.EmailReminderNotFoundException;
import org.remind.melembraai.domain.email.model.RememberEmail;
import org.remind.melembraai.domain.email.repository.EmailRepository;
import org.remind.melembraai.domain.sender.SendEmailService;
import org.remind.melembraai.domain.user.exceptions.UserNotVerifiedException;
import org.remind.melembraai.domain.user.model.User;
import org.remind.melembraai.utils.BRDatesUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final EmailRepository emailRepository;
    private final SendEmailService sendEmailService;

    @Transactional
    public void createEmailReminder(CreateEmailReminderDTO createEmailReminderDTO) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!user.getIsVerified()) {
            throw new UserNotVerifiedException();
        }

        RememberEmail rememberEmail = RememberEmail.builder()
                .user(user)
                .email(user.getEmail())
                .title(createEmailReminderDTO.getTitle())
                .content(createEmailReminderDTO.getContent())
                .hasRepetition(createEmailReminderDTO.getHasRepetition())
                .repetitionIntervalDays(createEmailReminderDTO.getRepetitionIntervalDays())
                .remainingRepetitions(createEmailReminderDTO.getRemainingRepetitions())
                .sendDate(BRDatesUtils.transformBrazilDateFormatStringToLocalDate(createEmailReminderDTO.getSendDate()))
                .build();

        emailRepository.save(rememberEmail);
    }

    @Transactional
    @Scheduled(cron = "0 */8 * * * *", zone = "America/Sao_Paulo")
    public void sendTodayEmailReminder() {
        LocalDate today = LocalDate.now(BRDatesUtils.getBrazilZoneId());

        List<RememberEmail> todayRememberEmails = emailRepository.findAllBySendDate(today);

        todayRememberEmails.forEach(rm -> {
            sendEmailService.sendEmail(rm);
            validateEmailReminder(rm.getId());
        });
    }

    @Transactional
    public void cancelEmailReminder(UUID id) {
        emailRepository.findById(id)
                .ifPresentOrElse((email) -> {
                    email.markAsFinished();
                    email.setHasRepetition(false);
                    email.setRemainingRepetitions(0);
                    email.setNextSentAt(null);
                    emailRepository.save(email);
                }, () -> {
                    throw new EmailReminderNotFoundException();
                });
    }

    @Transactional
    public void validateEmailReminder(UUID id) {
        RememberEmail rememberEmail = emailRepository.findById(id)
                .orElseThrow(EmailReminderNotFoundException::new);

        if (rememberEmail.getRemainingRepetitions() < 0) {
            throw new IllegalStateException("Remaining repetitions cannot be negative.");
        }

        if (!rememberEmail.getHasRepetition()) {
            rememberEmail.markAsFinished();
            rememberEmail.setLastSentAt(LocalDateTime.now(BRDatesUtils.getBrazilZoneId()));
        } else if (rememberEmail.getRemainingRepetitions() > 1) {
            rememberEmail.decrementRemainingRepetitions();
            rememberEmail.setNextSentAt(LocalDate.now().plusDays(rememberEmail.getRepetitionIntervalDays()));
            rememberEmail.setLastSentAt(LocalDateTime.now(BRDatesUtils.getBrazilZoneId()));
        } else if (rememberEmail.getRemainingRepetitions() == 1) {
            rememberEmail.decrementRemainingRepetitions();
            rememberEmail.markAsFinished();
            rememberEmail.setHasRepetition(false);
            rememberEmail.setLastSentAt(LocalDateTime.now(BRDatesUtils.getBrazilZoneId()));
            rememberEmail.setNextSentAt(null);
        }

        emailRepository.save(rememberEmail);
    }
}

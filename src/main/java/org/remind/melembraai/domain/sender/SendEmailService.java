package org.remind.melembraai.domain.sender;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.remind.melembraai.domain.email.model.RememberEmail;
import org.remind.melembraai.domain.sender.exceptions.SendEmailException;
import org.remind.melembraai.domain.sender.templates.EmailsTemplates;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class SendEmailService {

    private final JavaMailSender mailSender;

    public void sendEmail(RememberEmail emailData) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        try {
            helper.setFrom("pedro.paulo@oai.com.br", "Seu lembrete chegou! - Me lembra ai");
            helper.setTo(emailData.getEmail());
            helper.setSubject(emailData.getTitle());
            helper.setText(EmailsTemplates.emailTemplate(emailData), true);

            mailSender.send(mimeMessage);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new SendEmailException();
        }
    }

    public void sendWelcomeEmail(String username, String userEmail, String activationCode) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        try {
            helper.setFrom("pedro.paulo@oai.com.br", "Me Lembra aí!");
            helper.setTo(userEmail);
            helper.setSubject("Bem-vindo ao Melembraai!");
            helper.setText(EmailsTemplates.WelcomeEmail(username, activationCode), true);

            mailSender.send(mimeMessage);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new SendEmailException();
        }
    }

    public void sendNewVerifiedToken(String username, String userEmail, String activationCode) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        try {
            helper.setFrom("pedro.paulo@oai.com.br", "Me Lembra aí!");
            helper.setTo(userEmail);
            helper.setSubject("Bem-vindo ao Melembraai!");
            helper.setText(EmailsTemplates.ResendVerificationEmail(username, activationCode));

            mailSender.send(mimeMessage);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new SendEmailException();
        }
    }

}

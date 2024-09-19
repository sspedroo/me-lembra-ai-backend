package org.remind.melembraai.domain.errorHandler;

import jakarta.mail.MessagingException;
import org.remind.melembraai.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(AppException.class)
    public ProblemDetail handleAppException(AppException e) {
        return e.toProblemDetail();
    }

    @ExceptionHandler(MessagingException.class)
    public ProblemDetail handleMessagingException(MessagingException e) {

        ProblemDetail pb = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        pb.setTitle("Messaging error");
        pb.setDetail(e.getMessage());
        pb.setType(URI.create("https://tools.ietf.org/html/rfc7231#section-6.5.1"));

       return pb;
    }
}

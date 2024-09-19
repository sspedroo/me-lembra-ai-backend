package org.remind.melembraai.domain.sender.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.remind.melembraai.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

@Getter
@Setter
@NoArgsConstructor
public class SendEmailException extends AppException {

    @Override
    public ProblemDetail toProblemDetail() {
        ProblemDetail pb = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        pb.setTitle("Failed to send email");
        pb.setDetail("An error occurred while trying to send the email.");

        return pb;
    }
}

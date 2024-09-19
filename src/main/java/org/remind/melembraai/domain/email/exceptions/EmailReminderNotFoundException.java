package org.remind.melembraai.domain.email.exceptions;

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
public class EmailReminderNotFoundException extends AppException {
    @Override
    public ProblemDetail toProblemDetail() {
        ProblemDetail pb = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);

        pb.setTitle("Email reminder not found");
        pb.setDetail("The email reminder with the given ID was not found.");

        return pb;
    }
}

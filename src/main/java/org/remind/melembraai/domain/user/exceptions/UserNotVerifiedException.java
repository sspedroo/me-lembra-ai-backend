package org.remind.melembraai.domain.user.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.remind.melembraai.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

@Getter
@Setter
@NoArgsConstructor
public class UserNotVerifiedException extends AppException {
    @Override
    public ProblemDetail toProblemDetail() {
        ProblemDetail pb = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);

        pb.setTitle("Usuário não verificado");
        pb.setDetail("Você precisa verificar seu email para utilizar esse recurso.");

        return pb;
    }
}

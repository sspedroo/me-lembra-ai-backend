package org.remind.melembraai.domain.user.exceptions;

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
public class UserNotFoundException extends AppException {

    @Override
    public ProblemDetail toProblemDetail() {
        ProblemDetail pb = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);

        pb.setTitle("Usuário não encontrado");
        pb.setDetail("Não foi possível encontrar o usuário com o ID fornecido.");

        return pb;
    }
}

package org.remind.melembraai.exception;

import org.springframework.http.ProblemDetail;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class AppException extends RuntimeException  {

    public ProblemDetail toProblemDetail() {
        ProblemDetail pb = ProblemDetail.forStatus(INTERNAL_SERVER_ERROR);
        pb.setTitle("Me Lembra Ai Internal Server Error");
        return pb;
    }
}

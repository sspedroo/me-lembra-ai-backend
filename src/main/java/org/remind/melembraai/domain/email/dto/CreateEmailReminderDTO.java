package org.remind.melembraai.domain.email.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.stereotype.Service;

@Data
public class CreateEmailReminderDTO {
    private String title;
    private String content;
    private Boolean hasRepetition;
    private Integer repetitionIntervalDays;
    @Min(0)@Max(10)
    private Integer remainingRepetitions;
    private String sendDate;
}

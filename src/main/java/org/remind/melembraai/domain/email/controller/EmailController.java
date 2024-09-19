package org.remind.melembraai.domain.email.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.remind.melembraai.domain.email.dto.CreateEmailReminderDTO;
import org.remind.melembraai.domain.email.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Criador de Email", description = "API para criação de email para lembretes.")
public class EmailController {
    private final EmailService service;

    @PostMapping()
    public ResponseEntity<Void> createEmailReminder(@RequestBody @Valid CreateEmailReminderDTO createEmailReminderDTO) {
        service.createEmailReminder(createEmailReminderDTO);
        return ResponseEntity.ok().build();
    }
}

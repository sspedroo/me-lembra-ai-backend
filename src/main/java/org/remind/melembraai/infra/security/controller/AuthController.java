package org.remind.melembraai.infra.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.remind.melembraai.infra.security.domain.JwtAuthResponse;
import org.remind.melembraai.infra.security.domain.LoginDTO;
import org.remind.melembraai.infra.security.service.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "API para autenticação de usuários")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Autenticar um usuário")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody @Valid LoginDTO loginDTO) {
        return ResponseEntity.ok(authService.login(loginDTO.getEmail(), loginDTO.getPassword()));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Atualizar token de autenticação")
    public ResponseEntity<JwtAuthResponse> refresh(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        return ResponseEntity.ok(authService.refreshToken(authorization));
    }
}

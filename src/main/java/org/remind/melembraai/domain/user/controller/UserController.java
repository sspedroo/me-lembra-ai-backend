package org.remind.melembraai.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.remind.melembraai.domain.user.dto.CreateUserDTO;
import org.remind.melembraai.domain.user.dto.ResponseUserDTO;
import org.remind.melembraai.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Controle de usuários", description = "API para gerenciamento de usuários")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    @Operation(summary = "Cadastrar um novo usuário")
    public ResponseEntity<ResponseUserDTO> createUser(@RequestBody CreateUserDTO createUserDTO) {
        ResponseUserDTO user = userService.createUser(createUserDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(uri).body(user);
    }

    @PostMapping("/{id}/activate")
    @Operation(summary = "Ativar um novo usuário com um token de verificação")
    public ResponseEntity<String> activateUser(@PathVariable("id") UUID id, @RequestParam String token) {
        String response = userService.activateUser(id, token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("{id}/send-new-verified-token")
    @Operation(summary = "Enviar um novo token de verificação para um usuário")
    public ResponseEntity<String> sendNewVerifiedToken(@PathVariable("id") UUID id) {
        String response = userService.sendNewVerifiedToken(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desativar um usuário")
    public ResponseEntity<Void> deactivateUser(@PathVariable("id") UUID id) {
        userService.deactivateUser(id);
        return ResponseEntity.noContent().build();
    }
}

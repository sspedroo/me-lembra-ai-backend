package org.remind.melembraai.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.remind.melembraai.domain.sender.SendEmailService;
import org.remind.melembraai.domain.user.dto.CreateUserDTO;
import org.remind.melembraai.domain.user.dto.ResponseUserDTO;
import org.remind.melembraai.domain.user.exceptions.UserNotFoundException;
import org.remind.melembraai.domain.user.model.User;
import org.remind.melembraai.domain.user.repository.UserRepository;

import org.remind.melembraai.domain.validationToken.model.VerifiedToken;
import org.remind.melembraai.domain.validationToken.service.VerifiedTokenService;
import org.remind.melembraai.utils.BRDatesUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final VerifiedTokenService verifiedTokenService;
    private final SendEmailService sendEmailService;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public ResponseUserDTO createUser(CreateUserDTO createUserDTO) {

        if (userRepository.existsByEmail(createUserDTO.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado.");
        }

        User createdUser = User.builder()
                .email(createUserDTO.getEmail())
                .name(createUserDTO.getName())
                .password(passwordEncoder.encode(createUserDTO.getPassword()))
                .verifiedTokens(new HashSet<>())
                .role("USER")
                .build();

        userRepository.save(createdUser);
        VerifiedToken token = verifiedTokenService.createToken(createdUser);
        createdUser.getVerifiedTokens().add(token);
        userRepository.save(createdUser);

        sendEmailService.sendWelcomeEmail(createdUser.getName(), createdUser.getEmail(), token.getToken());

        return new ResponseUserDTO(createdUser);
    }

    public String sendNewVerifiedToken(UUID id) {
        User user = userRepository.findById(id)
               .orElseThrow(() -> new UserNotFoundException());

        VerifiedToken token = verifiedTokenService.createToken(user);
        user.getVerifiedTokens().add(token);
        userRepository.save(user);

        sendEmailService.sendNewVerifiedToken(user.getName(), user.getEmail(), token.getToken());
        return "Código enviado com sucesso.";
    }


    @Transactional
    public void updateUser(UUID userID, CreateUserDTO updateUserDTO) {
         // TODO implementar a atualização do usuário
    }

    @Transactional
    public void deactivateUser(UUID userID) {
        User user = userRepository.findById(userID)
                .orElseThrow(() -> new UserNotFoundException());
        user.deactivate();
        userRepository.save(user);
    }

    @Transactional
    public String activateUser(UUID userID, String token) {
        User user = userRepository.findById(userID)
                .orElseThrow(UserNotFoundException::new);

        Boolean b = verifiedTokenService.verifyToken(user.getId(), token);

        if (b) {
            user.setVerified();
            user.setLastActivityDate(LocalDateTime.now(BRDatesUtils.getBrazilZoneId()));
            userRepository.save(user);
            return "Usuário ativado com sucesso!";
        }

        return "Não foi possível ativar o usuário! Confira o token digitado ou solicite um novo.";
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email);
    }
}

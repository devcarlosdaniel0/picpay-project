package com.projeto.picpay.security.controller;

import com.projeto.picpay.exception.UsernameAlreadyExistsException;
import com.projeto.picpay.security.domain.User;
import com.projeto.picpay.security.dto.AuthDTO;
import com.projeto.picpay.security.dto.LoginResponseDTO;
import com.projeto.picpay.security.dto.SignupDTO;
import com.projeto.picpay.security.repository.UserRepository;
import com.projeto.picpay.security.service.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthDTO authDTO) {
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(authDTO.username(), authDTO.password());
        Authentication auth = authenticationManager.authenticate(usernamePassword);

        String token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody @Valid SignupDTO signupDTO) {
        if (userRepository.findByUsername(signupDTO.username()) != null) throw new UsernameAlreadyExistsException();

        String encryptedPassword = new BCryptPasswordEncoder().encode(signupDTO.password());

        User newUser = User.builder()
                .username(signupDTO.username())
                .password(encryptedPassword)
                .role(signupDTO.role()).build();

        this.userRepository.save(newUser);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}

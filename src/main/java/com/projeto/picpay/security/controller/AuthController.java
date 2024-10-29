package com.projeto.picpay.security.controller;

import com.projeto.picpay.security.domain.User;
import com.projeto.picpay.security.dto.AuthDTO;
import com.projeto.picpay.security.dto.SignupDTO;
import com.projeto.picpay.security.repository.UserRepository;
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
    
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody @Valid AuthDTO authDTO) {
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(authDTO.username(), authDTO.password());
        Authentication auth = authenticationManager.authenticate(usernamePassword);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody @Valid SignupDTO signupDTO) {
        //TODO CRIAR UMA EXCEÇÃO PERSONALIZADA PARA USUÁRIO JÁ CADASTRADO
        if (userRepository.findByUsername(signupDTO.username()) != null) throw new RuntimeException("User already exists!");

        String encryptedPassword = new BCryptPasswordEncoder().encode(signupDTO.password());

        User newUser = User.builder()
                .username(signupDTO.username())
                .password(encryptedPassword)
                .role(signupDTO.role()).build();

        this.userRepository.save(newUser);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}

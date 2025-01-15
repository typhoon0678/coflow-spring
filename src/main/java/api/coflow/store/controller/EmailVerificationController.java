package api.coflow.store.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.coflow.store.dto.emailVerification.EmailVerificationRequestDTO;
import api.coflow.store.dto.emailVerification.SignupRequestDTO;
import api.coflow.store.service.EmailVerificationService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/verify")
@RequiredArgsConstructor
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    @PostMapping("")
    public ResponseEntity<?> sendMail(@RequestBody EmailVerificationRequestDTO emailVerificationRequestDTO) throws MessagingException {
        emailVerificationService.sendMail(emailVerificationRequestDTO.getEmail());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/code")
    public ResponseEntity<?> checkCode(@RequestBody EmailVerificationRequestDTO emailVerificationRequestDTO) {
        emailVerificationService.checkCode(emailVerificationRequestDTO);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/signup")
    public ResponseEntity<?> verifyAndSignup(@RequestBody SignupRequestDTO signupRequestDTO) {
        emailVerificationService.verifyAndSignup(signupRequestDTO);

        return ResponseEntity.ok().build();
    }
}

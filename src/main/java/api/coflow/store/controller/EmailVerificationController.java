package api.coflow.store.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.coflow.store.dto.emailVerification.EmailVerificationRequestDTO;
import api.coflow.store.dto.member.SignupRequestDTO;
import api.coflow.store.service.EmailVerificationService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/verify")
@RequiredArgsConstructor
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    @PostMapping("")
    public ResponseEntity<?> createCode(@RequestBody EmailVerificationRequestDTO emailVerificationRequestDTO) {
        emailVerificationService.createCode(emailVerificationRequestDTO.getEmail());

        return ResponseEntity.ok().build();
    }

    @GetMapping("")
    public ResponseEntity<?> checkCode(@RequestBody EmailVerificationRequestDTO emailVerificationRequestDTO) {
        emailVerificationService.checkCode(emailVerificationRequestDTO);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/signup")
    public ResponseEntity<?> verifyAndSignup(@RequestBody SignupRequestDTO signupRequestDTO) {
        emailVerificationService.verifyAndSignup(signupRequestDTO);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/test")
    public String getMethodName() {
        return "test";
    }

}

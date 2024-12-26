package api.coflow.store.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import api.coflow.store.common.enums.Role;
import api.coflow.store.common.exception.CustomException;
import api.coflow.store.dto.emailVerification.EmailVerificationRequestDTO;
import api.coflow.store.dto.member.SignupRequestDTO;
import api.coflow.store.entity.EmailVerification;
import api.coflow.store.entity.Member;
import api.coflow.store.repository.EmailVerificationRepository;
import api.coflow.store.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final MemberRepository memberRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void createCode(String email) {
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new CustomException("SIGNUP_MEMBER_EXISTS");
        }

        emailVerificationRepository.deleteAllByEmail(email);

        String code = UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        EmailVerification emailVerification = EmailVerification.builder()
                .email(email)
                .code(code)
                .build();

        emailVerificationRepository.save(emailVerification);
    }

    public void checkCode(EmailVerificationRequestDTO emailVerificationRequestDTO) {
        String email = emailVerificationRequestDTO.getEmail();
        String code = emailVerificationRequestDTO.getCode();

        EmailVerification emailVerification = emailVerificationRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("SIGNUP_CODE_NOT_EXISTS"));

        if (!emailVerification.getCode().equals(code)) {
            throw new CustomException("SIGNUP_CODE_NOT_MATCHED");
        }

        if (emailVerification.getCreatedAt().plusMinutes(10).isBefore(LocalDateTime.now())) {
            throw new CustomException("SIGNUP_CODE_EXPIRED");
        }
    }

    @Transactional
    public void verifyAndSignup(SignupRequestDTO signupRequestDTO) {
        String email = signupRequestDTO.getEmail();
        String code = signupRequestDTO.getCode();

        EmailVerification emailVerification = emailVerificationRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("SIGNUP_CODE_NOT_EXISTS"));

        if (!emailVerification.getCode().equals(code)) {
            throw new CustomException("SIGNUP_CODE_NOT_MATCHED");
        }

        emailVerificationRepository.deleteAllByEmail(email);

        Member member = Member.builder()
                .email(email)
                .username(email.split("@")[0])
                .password(passwordEncoder.encode(signupRequestDTO.getPassword()))
                .roles(new HashSet<>(Set.of(Role.ROLE_USER)))
                .build();

        memberRepository.save(member);
    }

}

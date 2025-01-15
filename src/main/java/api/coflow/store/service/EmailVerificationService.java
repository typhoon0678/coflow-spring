package api.coflow.store.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import api.coflow.store.common.enums.Role;
import api.coflow.store.common.exception.CustomException;
import api.coflow.store.dto.emailVerification.EmailVerificationRequestDTO;
import api.coflow.store.dto.emailVerification.SignupRequestDTO;
import api.coflow.store.entity.EmailVerification;
import api.coflow.store.entity.Member;
import api.coflow.store.repository.EmailVerificationRepository;
import api.coflow.store.repository.MemberRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    @Value("${spring.mail.username}")
    private String senderEmail;

    private final JavaMailSender javaMailSender;
    private final MemberRepository memberRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void sendMail(String email) {
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new CustomException("SIGNUP_MEMBER_EXISTS");
        }

        emailVerificationRepository.deleteAllByEmail(email);

        Random random = new Random();
        String code = String.format("%06d", random.nextInt(1000000));

        EmailVerification emailVerification = EmailVerification.builder()
                .email(email)
                .code(code)
                .build();

        createAndSendMail(email, code);

        emailVerificationRepository.save(emailVerification);
    }

    private void createAndSendMail(String email, String code) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("[COFLOW] 회원가입 인증 메일");
            String body = "";
            body += "<h3>" + "진행중인 회원가입 페이지에서 아래의 6자리 코드를 입력해주세요." + "</h3>";
            body += "<h1>" + code + "</h1>";
            body += "<h3>" + "감사합니다." + "</h3>";
            message.setText(body, "UTF-8", "html");

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new CustomException("SIGNUP_MAIL_SEND_FAILED");
        }
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

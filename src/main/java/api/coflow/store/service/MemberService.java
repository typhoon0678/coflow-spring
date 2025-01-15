package api.coflow.store.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import api.coflow.store.common.exception.CustomException;
import api.coflow.store.dto.member.LoginRequestDTO;
import api.coflow.store.entity.Member;
import api.coflow.store.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    public void login(LoginRequestDTO loginRequestDTO) {
        Member member = memberRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new CustomException("LOGIN_EMAIL_NOT_EXISTS"));

        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), member.getPassword())) {
            throw new CustomException("LOGIN_PASSWORD_NOT_MATCHED");
        }
    }
}

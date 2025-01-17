package api.coflow.store.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import api.coflow.store.common.exception.CustomException;
import api.coflow.store.dto.member.UsernameRequestDTO;
import api.coflow.store.dto.refresh.LoginRequestDTO;
import api.coflow.store.entity.Member;
import api.coflow.store.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    public Member login(LoginRequestDTO loginRequestDTO) {
        Member member = memberRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new CustomException("LOGIN_FAILED"));

        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), member.getPassword())) {
            throw new CustomException("LOGIN_FAILED");
        }

        return member;
    }

    public void username(UsernameRequestDTO usernameRequestDTO) {
        memberRepository.updateUsername(usernameRequestDTO.getUsername(), usernameRequestDTO.getEmail());
    }
}

package api.coflow.store.service;

import org.springframework.stereotype.Service;

import api.coflow.store.common.enums.Role;
import api.coflow.store.dto.member.SignupRequestDTO;
import api.coflow.store.entity.Member;
import api.coflow.store.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

}

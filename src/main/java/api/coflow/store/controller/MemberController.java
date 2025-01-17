package api.coflow.store.controller;

import org.springframework.web.bind.annotation.RestController;

import api.coflow.store.common.util.SecurityUtil;
import api.coflow.store.dto.member.MemberInfoDTO;
import api.coflow.store.dto.member.UsernameRequestDTO;
import api.coflow.store.service.MemberService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/username")
    public ResponseEntity<?> postMethodName(@RequestBody UsernameRequestDTO usernameRequestDTO) {
        usernameRequestDTO.setEmail(SecurityUtil.getAuthenticationMemberInfo().getEmail());
        memberService.username(usernameRequestDTO);
        
        return ResponseEntity.ok().build();
    }

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        MemberInfoDTO memberInfoDTO = SecurityUtil.getAuthenticationMemberInfo();

        return ResponseEntity.ok().body(memberInfoDTO);
    }

}

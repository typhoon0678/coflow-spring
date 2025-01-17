package api.coflow.store.controller;

import org.springframework.web.bind.annotation.RestController;

import api.coflow.store.common.auth.JWTUtil;
import api.coflow.store.common.enums.Role;
import api.coflow.store.dto.member.LoginRequestDTO;
import api.coflow.store.dto.member.MemberInfoResponseDTO;
import api.coflow.store.entity.Member;
import api.coflow.store.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JWTUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO,
            HttpServletResponse response) {
        Member member = memberService.login(loginRequestDTO);

        String accessToken = jwtUtil.generateAccessToken(member);
        response.addHeader("Authorization", "Bearer " + accessToken);

        Cookie refreshTokenCookie = jwtUtil.generateRefreshServletCookie(member);
        response.addCookie(refreshTokenCookie);

        MemberInfoResponseDTO memberInfoResponseDTO = MemberInfoResponseDTO.builder()
                .status(200)
                .email(member.getEmail())
                .roles(member.getRoles().stream().map(Role::getRole).collect(Collectors.toSet()))
                .build();

        return ResponseEntity.ok().body(memberInfoResponseDTO);
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        
        return ResponseEntity.ok().build();
    }
    

    @GetMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue(value = "refreshToken", defaultValue = "") String refreshToken,
            HttpServletResponse response) {
        if (!StringUtils.hasText(refreshToken) || !jwtUtil.validateToken(refreshToken)) {
            return ResponseEntity.ok().body(MemberInfoResponseDTO.builder().status(401).build());
        }

        Member member = jwtUtil.getMember(refreshToken);

        String newAccessToken = jwtUtil.generateAccessToken(member);
        response.addHeader("Authorization", "Bearer " + newAccessToken);

        Authentication authentication = jwtUtil.getAuthentication(newAccessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        if (true) {
            Cookie newRefreshTokenCookie = jwtUtil.generateRefreshServletCookie(member);
            response.addCookie(newRefreshTokenCookie);
        }

        MemberInfoResponseDTO memberInfoResponseDTO = MemberInfoResponseDTO.builder()
                .status(200)
                .email(authentication.getName())
                .roles(member.getRoles().stream().map(Role::getRole).collect(Collectors.toSet()))
                .build();

        return ResponseEntity.ok().body(memberInfoResponseDTO);
    }

    @GetMapping("/test")
    public ResponseEntity<?> test() {

        return ResponseEntity.ok().build();
    }

}

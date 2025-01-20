package api.coflow.store.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.coflow.store.common.auth.JWTUtil;
import api.coflow.store.common.enums.Role;
import api.coflow.store.dto.refresh.LoginRequestDTO;
import api.coflow.store.dto.refresh.LogoutRequestDTO;
import api.coflow.store.dto.refresh.MemberInfoResponseDTO;
import api.coflow.store.dto.refresh.TokenDTO;
import api.coflow.store.entity.Member;
import api.coflow.store.service.MemberService;
import api.coflow.store.service.RefreshService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class RefreshController {

    private final RefreshService refreshService;
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
        refreshService.save(member.getEmail(), refreshTokenCookie.getValue());

        MemberInfoResponseDTO memberInfoResponseDTO = MemberInfoResponseDTO.builder()
                .status(200)
                .email(member.getEmail())
                .roles(member.getRoles().stream().map(Role::getRole).collect(Collectors.toSet()))
                .build();

        return ResponseEntity.ok().body(memberInfoResponseDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody LogoutRequestDTO logoutRequestDTO, HttpServletResponse response) {
        response.addCookie(jwtUtil.generateLogoutCookie());
        refreshService.delete(logoutRequestDTO.getEmail());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue(value = "refreshToken", defaultValue = "") String refreshToken,
            HttpServletResponse response) {
        MemberInfoResponseDTO memberInfoResponseDTO;

        if (!StringUtils.hasText(refreshToken) || !refreshService.isValid(refreshToken)) {
            memberInfoResponseDTO = MemberInfoResponseDTO.builder()
                    .status(401)
                    .build();
            return ResponseEntity.ok().body(memberInfoResponseDTO);
        }

        TokenDTO tokenDTO = refreshService.getNewToken(refreshToken);

        response.addHeader("Authorization", "Bearer " + tokenDTO.getAccessToken());

        if (tokenDTO.getRefreshTokenCookie() != null) {
            response.addCookie(tokenDTO.getRefreshTokenCookie());
        }

        memberInfoResponseDTO = MemberInfoResponseDTO.builder()
                .status(200)
                .email(tokenDTO.getEmail())
                .roles(tokenDTO.getRoles())
                .build();

        return ResponseEntity.ok().body(memberInfoResponseDTO);
    }

}

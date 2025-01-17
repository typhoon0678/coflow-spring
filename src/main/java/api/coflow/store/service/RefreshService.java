package api.coflow.store.service;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import api.coflow.store.common.auth.JWTUtil;
import api.coflow.store.common.enums.Role;
import api.coflow.store.dto.refresh.TokenDTO;
import api.coflow.store.entity.Member;
import api.coflow.store.entity.Refresh;
import api.coflow.store.repository.RefreshRepository;
import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshService {

    private final RefreshRepository refreshRepository;
    private final JWTUtil jwtUtil;

    @Transactional
    public void save(String email, String refreshToken) {
        refreshRepository.deleteAllByEmail(email);

        Refresh refresh = Refresh.builder()
                .email(email)
                .jwt(refreshToken)
                .build();

        refreshRepository.save(refresh);
    }

    public void delete(String email) {
        refreshRepository.deleteAllByEmail(email);
    }

    public boolean isExists(String refreshToken) {
        return refreshRepository.isExistsRefreshToken(refreshToken) > 0;
    }

    public boolean isValid(String refreshToken) {
        return !StringUtils.hasText(refreshToken) || !jwtUtil.validateToken(refreshToken) || isExists(refreshToken);
    }

    public TokenDTO getNewToken(String refreshToken) {
        Member member = jwtUtil.getMember(refreshToken);

        String newAccessToken = jwtUtil.generateAccessToken(member);

        TokenDTO tokenDTO = TokenDTO.builder()
                .email(member.getEmail())
                .roles(member.getRoles().stream().map(Role::getRole).collect(Collectors.toSet()))
                .accessToken(newAccessToken)
                .build();

        if (jwtUtil.shouldRenewRefresh(refreshToken)) {
            Cookie newRefreshTokenCookie = jwtUtil.generateRefreshServletCookie(member);
            save(member.getEmail(), newRefreshTokenCookie.getValue());

            tokenDTO.setRefreshTokenCookie(newRefreshTokenCookie);
        }

        return tokenDTO;
    }
}

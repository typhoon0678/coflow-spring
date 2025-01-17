package api.coflow.store.common.auth;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import api.coflow.store.common.enums.Role;
import api.coflow.store.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.Cookie;

@Component
public class JWTUtil implements InitializingBean {

    @Value("${jwt.key}")
    private String key;

    @Value("${jwt.accessSeconds}")
    private int accessSeconds;

    @Value("${jwt.refreshSeconds}")
    private int refreshSeconds;

    @Value("${jwt.renewSeconds}")
    private int renewSeconds;

    @Value("${jwt.domain}")
    private String domain;

    private final String AUTHORITIES_KEY = "auth";

    private SecretKey secretKey;

    @Override
    public void afterPropertiesSet() {
        this.secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS512.key().build().getAlgorithm());
    }

    // jwt 생성
    public String generateAccessToken(Member member) {
        return generateToken(member, "accessToken");
    }

    private String generateRefreshToken(Member member) {
        return generateToken(member, "refreshToken");
    }

    private String generateToken(Member member, String type) {
        String authorities = member.getRoles().stream()
                .map(Role::getRole)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity;
        if (type.equals("accessToken")) {
            validity = new Date(now + this.accessSeconds * 1000);
        } else if (type.equals("refreshToken")) {
            validity = new Date(now + this.refreshSeconds * 1000);
        } else {
            validity = new Date(now);
        }

        return Jwts.builder()
                .subject(member.getEmail())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(secretKey)
                .expiration(validity)
                .compact();
    }

    public ResponseCookie generateRefreshCookie(Member member) {
        String jwt = generateRefreshToken(member);

        return generateCookie(jwt, "refreshToken", refreshSeconds);
    }

    private ResponseCookie generateCookie(String jwt, String type, int maxAge) {

        return ResponseCookie.from(type)
                .value(jwt)
                .domain(domain)
                .path("/")
                .httpOnly(true)
                // .secure(true)
                .maxAge(maxAge)
                .build();
    }

    public Cookie generateRefreshServletCookie(Member member) {
        return toServletCookie(generateRefreshCookie(member));
    }

    private Cookie toServletCookie(ResponseCookie responseCookie) {

        Cookie cookie = new Cookie(responseCookie.getName(), responseCookie.getValue());
        cookie.setDomain(domain);
        cookie.setMaxAge((int) responseCookie.getMaxAge().getSeconds());
        cookie.setPath("/");
        cookie.setHttpOnly(responseCookie.isHttpOnly());
        cookie.setSecure(responseCookie.isSecure());
        return cookie;
    }

    public Cookie generateLogoutCookie() {
        return toServletCookie(generateCookie("", "refreshToken", 0));
    }

    // 토큰 검증
    public boolean validateToken(String jwt) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(jwt);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException | ExpiredJwtException e) {
        }

        return false;
    }

    // 남은 유효기간이 renewSeconds 이내인지 확인
    public boolean shouldRenewRefresh(String jwt) {
        Date expirationDate = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(jwt)
                .getPayload()
                .getExpiration();

        long remainingTime = expirationDate.getTime() - System.currentTimeMillis();
        return remainingTime < renewSeconds * 1000;
    }

    // jwt -> Authentication
    public Authentication getAuthentication(String jwt) {
        Claims claims = getClaims(jwt);

        Collection<? extends GrantedAuthority> authorities = Arrays
                .stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, jwt, authorities);
    }

    // jwt -> Member
    public Member getMember(String jwt) {
        Claims claims = getClaims(jwt);

        Set<Role> roles = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(Role::valueOf)
                .collect(Collectors.toSet());

        return Member.builder()
                .email(claims.getSubject())
                .roles(roles)
                .build();
    }

    private Claims getClaims(String jwt) {
        return Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }
}

package api.coflow.store.common.util;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import api.coflow.store.dto.member.MemberInfoDTO;

public class SecurityUtil {

    static public MemberInfoDTO getAuthenticationMemberInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();
        Set<String> roles = authentication.getAuthorities().stream()
                .map((role) -> role.getAuthority())
                .collect(Collectors.toSet());

        MemberInfoDTO memberInfoDTO = MemberInfoDTO.builder()
                .email(email)
                .roles(roles)
                .build();

        return memberInfoDTO;
    }

}

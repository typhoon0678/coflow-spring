package api.coflow.store.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_PAID_MEMBER("ROLE_PAID_MEMBER"),
    ROLE_USER("ROLE_USER");

    private final String role;
}
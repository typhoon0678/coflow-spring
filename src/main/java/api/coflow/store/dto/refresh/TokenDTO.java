package api.coflow.store.dto.refresh;

import java.util.Set;

import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDTO {
    
    private String email;
    private String username;
    private Set<String> roles;
    private String accessToken;
    private Cookie refreshTokenCookie;
}

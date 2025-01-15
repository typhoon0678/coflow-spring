package api.coflow.store.dto.member;

import lombok.Data;

@Data
public class LoginRequestDTO {
    
    private String email;
    private String password;
}

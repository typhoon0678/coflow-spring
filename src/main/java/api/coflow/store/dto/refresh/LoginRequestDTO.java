package api.coflow.store.dto.refresh;

import lombok.Data;

@Data
public class LoginRequestDTO {
    
    private String email;
    private String password;
}

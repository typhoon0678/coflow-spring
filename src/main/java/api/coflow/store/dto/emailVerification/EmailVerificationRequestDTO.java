package api.coflow.store.dto.emailVerification;

import lombok.Data;

@Data
public class EmailVerificationRequestDTO {
    
    private String email;
    private String code;
}

package api.coflow.store.dto.refresh;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberInfoResponseDTO {
    
    private int status;
    private String email;
    private String username;
    private Set<String> roles;
}

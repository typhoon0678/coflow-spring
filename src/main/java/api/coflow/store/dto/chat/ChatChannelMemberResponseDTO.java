package api.coflow.store.dto.chat;

import java.util.UUID;

import api.coflow.store.entity.ChatChannelMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatChannelMemberResponseDTO {

    private UUID id;
    private String email;
    private String username;

    public ChatChannelMemberResponseDTO(ChatChannelMember chatChannelMember) {
        this.id = chatChannelMember.getId();
        this.email = chatChannelMember.getMember().getEmail();
        this.username = chatChannelMember.getMember().getEmail();
    }
}

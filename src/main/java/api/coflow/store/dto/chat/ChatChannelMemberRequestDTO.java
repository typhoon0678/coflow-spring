package api.coflow.store.dto.chat;

import java.util.UUID;

import lombok.Data;

@Data
public class ChatChannelMemberRequestDTO {
    
    private UUID channelId;
    private String email;
}

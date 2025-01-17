package api.coflow.store.dto.chat;

import java.util.UUID;

import lombok.Data;

@Data
public class ChatRoomMemberRequestDTO {
    
    private final UUID chatRoomId;
    private final String email;
}

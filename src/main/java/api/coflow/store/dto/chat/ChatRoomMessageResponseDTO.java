package api.coflow.store.dto.chat;

import java.util.UUID;

import org.springframework.data.domain.Page;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatRoomMessageResponseDTO {
    
    private UUID chatRoomId;
    private String roomName;
    private Page<ChatMessageDTO> messages;
}

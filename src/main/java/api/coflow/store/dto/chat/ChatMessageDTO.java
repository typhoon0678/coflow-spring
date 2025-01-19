package api.coflow.store.dto.chat;

import java.time.LocalDateTime;
import java.util.UUID;

import api.coflow.store.entity.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDTO {
    
    private UUID id;
    private UUID chatChannelId;
    private UUID chatRoomId;
    private String email;
    private String username;
    private String message;
    private LocalDateTime createdAt;

    public ChatMessageDTO(ChatMessage chatMessage) {
        this.id = chatMessage.getId();
        this.chatChannelId = chatMessage.getChatRoom().getChatChannel().getId();
        this.chatRoomId = chatMessage.getChatRoom().getId();
        this.email = chatMessage.getEmail();
        this.username = chatMessage.getUsername();
        this.message = chatMessage.getMessage();
        this.createdAt = chatMessage.getCreatedAt();
    }
}

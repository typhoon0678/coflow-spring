package api.coflow.store.dto.chat;

import java.util.UUID;

import api.coflow.store.entity.ChatMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomResponseDTO {

    private UUID id;
    private String roomName;

    public ChatRoomResponseDTO(ChatMember chatMember) {
        this.id = chatMember.getChatRoom().getId();
        this.roomName = chatMember.getChatRoom().getRoomName();
    }
}

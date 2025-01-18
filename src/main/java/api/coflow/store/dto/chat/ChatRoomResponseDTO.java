package api.coflow.store.dto.chat;

import java.util.UUID;

import api.coflow.store.entity.ChatRoomMember;
import api.coflow.store.entity.ChatRoomMember;
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

    public ChatRoomResponseDTO(ChatRoomMember chatRoomMember) {
        this.id = chatRoomMember.getChatRoom().getId();
        this.roomName = chatRoomMember.getChatRoom().getRoomName();
    }
}

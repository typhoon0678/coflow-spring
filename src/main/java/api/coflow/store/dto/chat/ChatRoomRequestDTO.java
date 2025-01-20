package api.coflow.store.dto.chat;

import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class ChatRoomRequestDTO {
    
    private UUID channelId;
    private String roomName;
    private List<String> emailList;
}

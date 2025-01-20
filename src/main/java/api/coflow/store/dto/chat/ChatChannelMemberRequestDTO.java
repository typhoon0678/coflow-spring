package api.coflow.store.dto.chat;

import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class ChatChannelMemberRequestDTO {
    
    private UUID channelId;
    private List<String> emailList;
}

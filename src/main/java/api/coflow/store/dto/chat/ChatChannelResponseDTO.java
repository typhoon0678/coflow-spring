package api.coflow.store.dto.chat;

import java.util.UUID;

import api.coflow.store.entity.ChatChannel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatChannelResponseDTO {
    
    private UUID id;
    private String channelName;

    public ChatChannelResponseDTO(ChatChannel chatChannel) {
        this.id = chatChannel.getId();
        this.channelName = chatChannel.getChannelName();
    }
}

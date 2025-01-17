package api.coflow.store.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import api.coflow.store.dto.chat.ChatMessageDTO;
import api.coflow.store.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    
    private final ChatMessageRepository chatMessageRepository;

    public List<ChatMessageDTO> getChatMessages(UUID chatRoomId) {

        return List.of();
    }

    public void save(ChatMessageDTO chatMessageDTO) {
        
    }
}

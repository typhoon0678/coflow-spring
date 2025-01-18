package api.coflow.store.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import api.coflow.store.dto.chat.ChatMessageDTO;
import api.coflow.store.entity.ChatMessage;
import api.coflow.store.entity.ChatRoom;
import api.coflow.store.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    public List<ChatMessageDTO> getChatMessages(UUID chatRoomId) {
        List<ChatMessage> chatMessageList = chatMessageRepository.findAllByChatRoomId(chatRoomId);
        return chatMessageList.stream()
                .map((msg) -> new ChatMessageDTO(msg))
                .toList();
    }

    public void save(ChatMessageDTO chatMessageDTO) {
        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(ChatRoom.builder().id(chatMessageDTO.getChatRoomId()).build())
                .email(chatMessageDTO.getEmail())
                .username(chatMessageDTO.getUsername())
                .message(chatMessageDTO.getMessage())
                .createdAt(chatMessageDTO.getCreatedAt())
                .updatedAt(chatMessageDTO.getCreatedAt())
                .build();

        chatMessageRepository.save(chatMessage);
    }
}

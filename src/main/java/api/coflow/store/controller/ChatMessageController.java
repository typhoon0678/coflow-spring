package api.coflow.store.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import api.coflow.store.dto.chat.ChatMessageDTO;
import api.coflow.store.dto.chat.ChatRoomMessageResponseDTO;
import api.coflow.store.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/chat/message")
@RequiredArgsConstructor
public class ChatMessageController {

    private final SimpMessageSendingOperations template;
    private final ChatMessageService chatMessageService;

    @GetMapping("/channel/{chatChannelId}")
    public ResponseEntity<?> getChannelMessages(
            @PathVariable UUID chatChannelId,
            @RequestParam int size) {
        Pageable pageable = PageRequest.of(0, size, Sort.by("createdAt", "id").descending());
        List<ChatRoomMessageResponseDTO> messages = chatMessageService.getChannelMessages(chatChannelId, pageable);

        return ResponseEntity.ok().body(messages);
    }

    @GetMapping("/room/{chatRoomId}")
    public ResponseEntity<?> getRoomMessages(
            @PathVariable UUID chatRoomId,
            @RequestParam String isoString,
            @RequestParam int page,
            @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt", "id").descending());
        Page<ChatMessageDTO> messages = chatMessageService.getRoomMessages(chatRoomId, isoString, pageable);

        return ResponseEntity.ok().body(messages);
    }

    // /pub/message로 요청
    @MessageMapping("/message")
    public ResponseEntity<?> receiveMessage(@RequestBody ChatMessageDTO chat) {
        chatMessageService.save(chat);

        template.convertAndSend("/sub/channel/" + chat.getChatChannelId(), chat);
        return ResponseEntity.ok().build();
    }

}

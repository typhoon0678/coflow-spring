package api.coflow.store.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.coflow.store.dto.chat.ChatMessageDTO;
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


    @GetMapping("/{chatRoomId}")
    public ResponseEntity<?> getChatMessages(@PathVariable UUID chatRoomId) {
        List<ChatMessageDTO> messages = chatMessageService.getChatMessages(chatRoomId);

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

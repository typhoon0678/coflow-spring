package api.coflow.store.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.coflow.store.dto.chat.ChatMessageDTO;
import api.coflow.store.dto.chat.ChatRoomMemberRequestDTO;
import api.coflow.store.dto.chat.ChatRoomRequestDTO;
import api.coflow.store.dto.chat.ChatRoomResponseDTO;
import api.coflow.store.service.ChatMemberService;
import api.coflow.store.service.ChatMessageService;
import api.coflow.store.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;



@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessageSendingOperations template;
    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;
    private final ChatMemberService chatMemberService;

    @PostMapping("/room")
    public ResponseEntity<?> createChatRoom(@RequestBody ChatRoomRequestDTO chatRoomRequestDTO) {
        chatRoomService.createChatRoom(chatRoomRequestDTO);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/room/member")
    public ResponseEntity<?> inviteMember(@RequestBody ChatRoomMemberRequestDTO ChatRoomMemberRequestDTO) {
        chatMemberService.inviteMember(ChatRoomMemberRequestDTO);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/room")
    public ResponseEntity<?> getChatRoomList() {
        List<ChatRoomResponseDTO> chatRoomResponseDTOList = chatRoomService.getChatRoomList();

        return ResponseEntity.ok().body(chatRoomResponseDTOList);
    }
    

    @GetMapping("/room/{id}")
    public ResponseEntity<?> getChatMessages(@PathVariable UUID id) {
        List<ChatMessageDTO> messages = chatMessageService.getChatMessages(id);

        return ResponseEntity.ok().body(messages);
    }

    // /pub/message로 요청
    @MessageMapping("/message")
    public ResponseEntity<Void> receiveMessage(@RequestBody ChatMessageDTO chat) {
        chatMessageService.save(chat);

        // 메시지를 해당 채팅방 구독자들에게 전송
        template.convertAndSend("/sub/chatroom/" + chat.getChatRoomId(), chat);
        return ResponseEntity.ok().build();
    }

}

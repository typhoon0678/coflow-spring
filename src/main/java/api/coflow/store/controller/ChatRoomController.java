package api.coflow.store.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.coflow.store.dto.chat.ChatRoomMemberRequestDTO;
import api.coflow.store.dto.chat.ChatRoomRequestDTO;
import api.coflow.store.dto.chat.ChatRoomResponseDTO;
import api.coflow.store.service.ChatRoomMemberService;
import api.coflow.store.service.ChatRoomService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/chat/room")
@RequiredArgsConstructor
public class ChatRoomController {
    
    private final ChatRoomService chatRoomService;
    private final ChatRoomMemberService chatRoomMemberService;

    @PostMapping
    public ResponseEntity<?> createChatRoom(@RequestBody ChatRoomRequestDTO chatRoomRequestDTO) {
        chatRoomService.createChatRoom(chatRoomRequestDTO);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/member")
    public ResponseEntity<?> inviteMember(@RequestBody ChatRoomMemberRequestDTO ChatRoomMemberRequestDTO) {
        chatRoomMemberService.inviteMember(ChatRoomMemberRequestDTO);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{channelId}")
    public ResponseEntity<?> getChatRoomList(@PathVariable UUID channelId) {
        List<ChatRoomResponseDTO> chatRoomResponseDTOList = chatRoomService.getChatRoomList(channelId);

        return ResponseEntity.ok().body(chatRoomResponseDTOList);
    }
}

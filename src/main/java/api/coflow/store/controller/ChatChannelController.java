package api.coflow.store.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.coflow.store.dto.chat.ChatChannelMemberRequestDTO;
import api.coflow.store.dto.chat.ChatChannelRequestDTO;
import api.coflow.store.dto.chat.ChatChannelResponseDTO;
import api.coflow.store.service.ChatChannelMemberService;
import api.coflow.store.service.ChatChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/chat/channel")
@RequiredArgsConstructor
public class ChatChannelController {

    private final ChatChannelService chatChannelService;
    private final ChatChannelMemberService chatChannelMemberService;

    @GetMapping
    public ResponseEntity<?> getChannelList() {
        List<ChatChannelResponseDTO> chatChannelResponseDTOList = chatChannelService.getChannelList();

        return ResponseEntity.ok().body(chatChannelResponseDTOList);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ChatChannelRequestDTO chatChannelRequestDTO) {
        chatChannelService.create(chatChannelRequestDTO);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/member")
    public ResponseEntity<?> invite(@RequestBody ChatChannelMemberRequestDTO chatChannelMemberRequestDTO) {
        chatChannelMemberService.invite(chatChannelMemberRequestDTO);

        return ResponseEntity.ok().build();
    }
}

package api.coflow.store.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import api.coflow.store.common.exception.CustomException;
import api.coflow.store.common.util.SecurityUtil;
import api.coflow.store.dto.chat.ChatMessageDTO;
import api.coflow.store.dto.chat.ChatRoomMessageResponseDTO;
import api.coflow.store.entity.ChatChannel;
import api.coflow.store.entity.ChatMessage;
import api.coflow.store.entity.ChatRoom;
import api.coflow.store.entity.Member;
import api.coflow.store.repository.ChatMessageRepository;
import api.coflow.store.repository.ChatRoomRepository;
import api.coflow.store.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;

    public List<ChatRoomMessageResponseDTO> getChannelMessages(UUID channelId, Pageable pageable) {
        String email = SecurityUtil.getAuthenticationMemberInfo().getEmail();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("MEMBER_EMAIL_NOT_FOUND"));

        ChatChannel chatChannel = ChatChannel.builder().id(channelId).build();

        List<ChatRoom> publicChatRoomList = chatRoomRepository.findAllByChatChannelAndIsPublic(chatChannel, true);
        List<ChatRoom> privateChatRoomList = chatRoomRepository.getChatRoomByChatChannelAndMember(chatChannel, member);
        List<ChatRoom> chatRoomList = Stream.concat(publicChatRoomList.stream(), privateChatRoomList.stream()).toList();

        return chatRoomList.stream()
                .map((chatRoom) -> ChatRoomMessageResponseDTO.builder()
                        .chatRoomId(chatRoom.getId())
                        .roomName(chatRoom.getRoomName())
                        .messages(chatMessageRepository.findAllByChatRoomIdAndCreatedAtLessThan(chatRoom.getId(), LocalDateTime.now(), pageable)
                                .map(ChatMessageDTO::new))
                        .build())
                .toList();
    }

    public ChatRoomMessageResponseDTO getRoomMessages(UUID chatRoomId, String isoString, Pageable pageable) {
        LocalDateTime createdAt = ZonedDateTime.parse(isoString)
                .withZoneSameInstant(ZoneId.of("Asia/Seoul"))
                .toLocalDateTime();
        Page<ChatMessage> chatMessages = chatMessageRepository.findAllByChatRoomIdAndCreatedAtLessThan(chatRoomId, createdAt, pageable);

        return ChatRoomMessageResponseDTO.builder()
                .chatRoomId(chatRoomId)
                .messages(chatMessages.map(ChatMessageDTO::new))
                .build();
    }

    public void save(ChatMessageDTO chatMessageDTO) {
        Member member = memberRepository.findByEmail(chatMessageDTO.getEmail())
                .orElseThrow(() -> new CustomException("MEMBER_EMAIL_NOT_FOUND"));

        ChatMessage chatMessage = ChatMessage.builder()
                .id(chatMessageDTO.getId())
                .chatRoom(ChatRoom.builder().id(chatMessageDTO.getChatRoomId()).build())
                .member(member)
                .message(chatMessageDTO.getMessage())
                .createdAt(chatMessageDTO.getCreatedAt())
                .updatedAt(chatMessageDTO.getCreatedAt())
                .build();

        chatMessageRepository.save(chatMessage);
    }
}

package api.coflow.store.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import api.coflow.store.common.exception.CustomException;
import api.coflow.store.common.util.SecurityUtil;
import api.coflow.store.dto.chat.ChatChannelRequestDTO;
import api.coflow.store.dto.chat.ChatChannelResponseDTO;
import api.coflow.store.entity.ChatChannel;
import api.coflow.store.entity.ChatChannelMember;
import api.coflow.store.entity.ChatRoom;
import api.coflow.store.entity.Member;
import api.coflow.store.repository.ChatChannelMemberRepository;
import api.coflow.store.repository.ChatChannelRepository;
import api.coflow.store.repository.ChatRoomRepository;
import api.coflow.store.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatChannelService {

    private final ChatChannelRepository chatChannelRepository;
    private final ChatChannelMemberRepository chatChannelMemberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;

    public List<ChatChannelResponseDTO> getChannelList() {
        String email = SecurityUtil.getAuthenticationMemberInfo().getEmail();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(""));

        List<ChatChannelMember> chatChannelMemberList = chatChannelMemberRepository.findAllByMember(member);
        return chatChannelMemberList.stream()
                .map((chatChannelMember) -> new ChatChannelResponseDTO(chatChannelMember.getChatChannel()))
                .toList();
    }

    public ChatChannelResponseDTO getChannelInfo(UUID channelId) {
        ChatChannel chatChannel = chatChannelRepository.findById(channelId)
                .orElseThrow(() -> new CustomException("CHAT_CHANNEL_NOT_FOUND"));

        return new ChatChannelResponseDTO(chatChannel);
    }

    @Transactional
    public void create(ChatChannelRequestDTO chatChannelRequestDTO) {
        String email = SecurityUtil.getAuthenticationMemberInfo().getEmail();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("MEMBER_EMAIL_NOT_FOUND"));

        ChatChannel chatChannel = ChatChannel.builder()
                .channelName(chatChannelRequestDTO.getChannelName())
                .build();
        ChatChannel savedChatChannel = chatChannelRepository.save(chatChannel);

        ChatChannelMember chatChannelMember = ChatChannelMember.builder()
                .chatChannel(savedChatChannel)
                .member(member)
                .build();
        chatChannelMemberRepository.save(chatChannelMember);

        ChatRoom defaultChatRoom = ChatRoom.builder()
                .chatChannel(savedChatChannel)
                .roomName("기본 채팅방")
                .isPublic(true)
                .build();
        chatRoomRepository.save(defaultChatRoom);
    }
}

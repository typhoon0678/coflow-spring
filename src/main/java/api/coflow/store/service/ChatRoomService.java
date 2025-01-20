package api.coflow.store.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import api.coflow.store.common.exception.CustomException;
import api.coflow.store.common.util.SecurityUtil;
import api.coflow.store.dto.chat.ChatRoomRequestDTO;
import api.coflow.store.dto.chat.ChatRoomResponseDTO;
import api.coflow.store.entity.ChatChannel;
import api.coflow.store.entity.ChatRoom;
import api.coflow.store.entity.ChatRoomMember;
import api.coflow.store.entity.Member;
import api.coflow.store.repository.ChatChannelMemberRepository;
import api.coflow.store.repository.ChatRoomMemberRepository;
import api.coflow.store.repository.ChatRoomRepository;
import api.coflow.store.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatChannelMemberRepository chatChannelMemberRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ChatRoomResponseDTO createChatRoom(ChatRoomRequestDTO chatRoomRequestDTO) {
        Member member = checkAuthority();

        ChatChannel chatChannel = ChatChannel.builder().id(chatRoomRequestDTO.getChannelId()).build();

        boolean isPublic = chatRoomRequestDTO.getEmailList().size() == 0;

        ChatRoom chatRoom = ChatRoom.builder()
                .chatChannel(chatChannel)
                .roomName(chatRoomRequestDTO.getRoomName())
                .isPublic(isPublic)
                .build();

        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        if (!isPublic) {
            ChatRoomMember chatRoomMember = ChatRoomMember.builder()
                    .chatRoom(chatRoom)
                    .member(member)
                    .build();
            chatRoomMemberRepository.save(chatRoomMember);

            chatRoomRequestDTO.getEmailList().stream()
                    .forEach((email) -> {
                        Member inviteMember = memberRepository.findByEmail(email)
                                .orElseThrow(() -> new CustomException("MEMBER_EMAIL_NOT_FOUND"));
                        ChatRoomMember chatRoomInviteMember = ChatRoomMember.builder()
                                .member(inviteMember)
                                .build();
                        chatRoomMemberRepository.save(chatRoomInviteMember);
                    });
        }

        return new ChatRoomResponseDTO(savedChatRoom);
    }

    public List<ChatRoomResponseDTO> getChatRoomList(UUID channelId) {
        Member member = checkAuthority();

        List<ChatRoomMember> chatMemberList = chatRoomMemberRepository.findAllByMember(member);
        List<ChatRoomMember> filteredChatMemberList = chatMemberList.stream()
                .filter((chatMember) -> chatMember.getChatRoom().getChatChannel().getId().equals(channelId))
                .toList();
        List<ChatRoomResponseDTO> chatRoomResponseDTOList = filteredChatMemberList.stream()
                .map((chatRoom) -> new ChatRoomResponseDTO(chatRoom))
                .toList();

        return chatRoomResponseDTOList;
    }

    private Member checkAuthority() {
        String email = SecurityUtil.getAuthenticationMemberInfo().getEmail();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("MEMBER_EMAIL_NOT_FOUND"));
        if (chatChannelMemberRepository.isExistsByMember(member) == 0) {
            throw new CustomException("CHAT_CHANNEL_NO_AUTHORITY");
        }

        return member;
    }
}

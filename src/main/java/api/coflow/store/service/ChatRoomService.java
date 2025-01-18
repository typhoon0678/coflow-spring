package api.coflow.store.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import api.coflow.store.common.exception.CustomException;
import api.coflow.store.common.util.SecurityUtil;
import api.coflow.store.dto.chat.ChatRoomRequestDTO;
import api.coflow.store.dto.chat.ChatRoomResponseDTO;
import api.coflow.store.dto.member.MemberInfoDTO;
import api.coflow.store.entity.ChatRoom;
import api.coflow.store.entity.ChatRoomMember;
import api.coflow.store.entity.Member;
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
    private final MemberRepository memberRepository;

    @Transactional
    public void createChatRoom(ChatRoomRequestDTO chatRoomRequestDTO) {
        ChatRoom chatRoom = ChatRoom.builder()
                .roomName(chatRoomRequestDTO.getRoomName())
                .build();

        chatRoomRepository.save(chatRoom);

        String email = SecurityUtil.getAuthenticationMemberInfo().getEmail();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(""));
        ChatRoomMember chatRoomMember = ChatRoomMember.builder()
                .chatRoom(chatRoom)
                .member(member)
                .build();

        chatRoomMemberRepository.save(chatRoomMember);
    }

    public List<ChatRoomResponseDTO> getChatRoomList(UUID channelId) {
        MemberInfoDTO memberInfoDTO = SecurityUtil.getAuthenticationMemberInfo();

        Member member = memberRepository.findByEmail(memberInfoDTO.getEmail())
                .orElseThrow(() -> new CustomException("MEMBER_EMAIL_NOT_FOUND"));

        List<ChatRoomMember> chatMemberList = chatRoomMemberRepository.findAllByMember(member);
        List<ChatRoomMember> filteredChatMemberList = chatMemberList.stream()
                .filter((chatMember) -> chatMember.getChatRoom().getChatChannel().getId().equals(channelId))
                .toList();
        List<ChatRoomResponseDTO> chatRoomResponseDTOList = filteredChatMemberList.stream()
                .map((chatRoom) -> new ChatRoomResponseDTO(chatRoom))
                .toList();

        return chatRoomResponseDTOList;
    }
}

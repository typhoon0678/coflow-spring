package api.coflow.store.service;

import org.springframework.stereotype.Service;

import api.coflow.store.common.exception.CustomException;
import api.coflow.store.common.util.SecurityUtil;
import api.coflow.store.dto.chat.ChatRoomMemberRequestDTO;
import api.coflow.store.dto.member.MemberInfoDTO;
import api.coflow.store.entity.ChatMember;
import api.coflow.store.entity.ChatRoom;
import api.coflow.store.entity.Member;
import api.coflow.store.repository.ChatMemberRepository;
import api.coflow.store.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatMemberService {

    private final ChatMemberRepository chatMemberRepository;
    private final MemberRepository memberRepository;

    public void inviteMember(ChatRoomMemberRequestDTO chatRoomMemberRequestDTO) {
        MemberInfoDTO memberInfoDTO = SecurityUtil.getAuthenticationMemberInfo();

        Member member = memberRepository.findByEmail(memberInfoDTO.getEmail())
                .orElseThrow(() -> new CustomException("MEMBER_EMAIL_NOT_FOUND"));

        if (chatMemberRepository.isExistsByMember(member) == 0) {
            throw new CustomException("CHAT_ROOM_NO_AUTHORITY");
        }

        Member inviteMember = memberRepository.findByEmail(chatRoomMemberRequestDTO.getEmail())
                .orElseThrow(() -> new CustomException("CHAT_EMAIL_NOT_FOUND"));

        ChatRoom chatRoom = ChatRoom.builder()
                .id(chatRoomMemberRequestDTO.getChatRoomId())
                .build();

        ChatMember chatMember = ChatMember.builder()
                .chatRoom(chatRoom)
                .member(inviteMember)
                .build();

        chatMemberRepository.save(chatMember);
    }
}

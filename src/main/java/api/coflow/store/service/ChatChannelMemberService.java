package api.coflow.store.service;

import org.springframework.stereotype.Service;

import api.coflow.store.common.exception.CustomException;
import api.coflow.store.common.util.SecurityUtil;
import api.coflow.store.dto.chat.ChatChannelMemberRequestDTO;
import api.coflow.store.entity.ChatChannel;
import api.coflow.store.entity.ChatChannelMember;
import api.coflow.store.entity.Member;
import api.coflow.store.repository.ChatChannelMemberRepository;
import api.coflow.store.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatChannelMemberService {

    private final ChatChannelMemberRepository chatChannelMemberRepository;
    private final MemberRepository memberRepository;

    public void invite(ChatChannelMemberRequestDTO chatChannelMemberRequestDTO) {
        String email = SecurityUtil.getAuthenticationMemberInfo().getEmail();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("MEMBER_EMAIL_NOT_FOUND"));
        if (chatChannelMemberRepository.isExistsByMember(member) == 0) {
            throw new CustomException("CHAT_CHANNEL_NO_AUTHORITY");
        }

        Member inviteMember = memberRepository.findByEmail(chatChannelMemberRequestDTO.getEmail())
                .orElseThrow(() -> new CustomException("CHAT_EMAIL_NOT_FOUND"));
        ChatChannelMember chatChannelMember = ChatChannelMember.builder()
                .chatChannel(ChatChannel.builder().id(chatChannelMemberRequestDTO.getChannelId()).build())
                .member(inviteMember)
                .build();
        chatChannelMemberRepository.save(chatChannelMember);
    }
}

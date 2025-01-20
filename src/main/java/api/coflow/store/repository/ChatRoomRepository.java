package api.coflow.store.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import api.coflow.store.entity.ChatChannel;
import api.coflow.store.entity.ChatRoom;
import api.coflow.store.entity.Member;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, UUID> {

    @Query("SELECT cr FROM ChatRoom cr WHERE cr.chatChannel = :chatChannel AND cr.id IN (SELECT crm.chatRoom.id FROM ChatRoomMember crm WHERE crm.member = :member)")
    List<ChatRoom> getChatRoomByChatChannelAndMember(ChatChannel chatChannel, Member member);

    List<ChatRoom> findAllByChatChannelAndIsPublic(ChatChannel chatChannel, boolean isPublic);
}

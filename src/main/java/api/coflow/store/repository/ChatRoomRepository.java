package api.coflow.store.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import api.coflow.store.entity.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, UUID> {

    @Query("SELECT cr FROM ChatRoom cr WHERE cr.chatChannel.id = :chatChannelId AND cr.id IN (SELECT crm.chatRoom.id FROM ChatRoomMember crm WHERE crm.member.id = :memberId)")
    List<ChatRoom> getChatRoomByChatChannelId(UUID chatChannelId, UUID memberId);
}

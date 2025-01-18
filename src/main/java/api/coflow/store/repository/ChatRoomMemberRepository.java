package api.coflow.store.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import api.coflow.store.entity.ChatRoomMember;
import api.coflow.store.entity.Member;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, UUID> {

    @Query("SELECT COUNT(crm) FROM ChatRoomMember crm WHERE crm.member = :member")
    int isExistsByMember(Member member);

    List<ChatRoomMember> findAllByMember(Member member);
}

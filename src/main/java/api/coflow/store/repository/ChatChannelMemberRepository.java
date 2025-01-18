package api.coflow.store.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import api.coflow.store.entity.ChatChannelMember;
import api.coflow.store.entity.Member;

public interface ChatChannelMemberRepository extends JpaRepository<ChatChannelMember, UUID> {

    @Query("SELECT COUNT(ccm) FROM ChatChannelMember ccm WHERE ccm.member = :member")
    int isExistsByMember(Member member);

    List<ChatChannelMember> findAllByMember(Member member);
}

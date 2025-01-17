package api.coflow.store.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import api.coflow.store.entity.ChatMember;
import api.coflow.store.entity.Member;

public interface ChatMemberRepository extends JpaRepository<ChatMember, UUID> {

    @Query("SELECT COUNT(cm) FROM ChatMember cm WHERE cm.member = :member")
    int isExistsByMember(Member member);

    List<ChatMember> findAllByMember(Member member);
}

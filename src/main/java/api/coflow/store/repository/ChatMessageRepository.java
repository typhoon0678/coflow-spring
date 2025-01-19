package api.coflow.store.repository;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import api.coflow.store.entity.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {
    
    Page<ChatMessage> findAllByChatRoomIdAndCreatedAtLessThan(UUID chatRoomId, LocalDateTime createdAt, Pageable pageable);
}

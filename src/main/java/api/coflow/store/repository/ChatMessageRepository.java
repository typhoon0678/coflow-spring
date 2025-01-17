package api.coflow.store.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import api.coflow.store.entity.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {
    
}

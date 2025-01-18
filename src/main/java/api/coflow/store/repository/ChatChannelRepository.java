package api.coflow.store.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import api.coflow.store.entity.ChatChannel;

public interface ChatChannelRepository extends JpaRepository<ChatChannel, UUID> {
    
}

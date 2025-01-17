package api.coflow.store.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import api.coflow.store.entity.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, UUID> {

}

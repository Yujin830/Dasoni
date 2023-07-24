package signiel.heartsigniel.model.room;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepo extends JpaRepository<Room, Long> {
    Optional<Room> findByRoomId(Long roomId);
}

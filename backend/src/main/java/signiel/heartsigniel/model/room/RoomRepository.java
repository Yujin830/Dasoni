package signiel.heartsigniel.model.room;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Page<Room> findAllByRoomType(String type, Pageable pageable);
    Page<Room> findRoomByTitleContaining(String title, Pageable pageable);



}

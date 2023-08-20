package signiel.heartsigniel.model.room;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Page<Room> findAllByRoomTypeAndStartTimeIsNull(String type, Pageable pageable);
    Page<Room> findRoomByTitleContainingAndStartTimeIsNullAndRoomType(String title, String roomType, Pageable pageable);


    @Query("SELECT r FROM room r " +
            "LEFT JOIN r.roomMembers rm ON rm.member.gender = :gender " +
            "WHERE r.roomType = :roomType " +
            "GROUP BY r.id " +
            "HAVING COALESCE(COUNT(rm), 0) <= :count")
    Page<Room> findRoomsByGenderAndCountLessThanEqualAndRoomType(@Param("gender") String gender, @Param("count") Long count, @Param("roomType") String roomType, Pageable pageable);

}

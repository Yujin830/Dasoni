package signiel.heartsigniel.model.room;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Page<Room> findAllByRoomTypeAndStartTimeIsNull(String type, Pageable pageable);
    Page<Room> findRoomByTitleContainingAndStartTimeIsNull(String title, Pageable pageable);

    @Query("SELECT r FROM room r WHERE r.roomType = :roomType AND "
            + "(SELECT COUNT(rm) FROM r.roomMembers rm WHERE rm.member.gender = 'male') <= :maleCount")
    Page<Room> findAllByRoomTypeAndMaleMemberCountLessThanEqual(String roomType, int maleCount, Pageable pageable);

    @Query("SELECT r FROM room r WHERE r.roomType = :roomType AND "
            + "(SELECT COUNT(rm) FROM r.roomMembers rm WHERE rm.member.gender = 'female') <= :femaleCount")
    Page<Room> findAllByRoomTypeAndFemaleMemberCountLessThanEqual(String roomType, int femaleCount, Pageable pageable);

}

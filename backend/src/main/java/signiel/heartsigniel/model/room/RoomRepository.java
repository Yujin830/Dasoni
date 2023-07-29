package signiel.heartsigniel.model.room;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Page<Room> findAllByRoomType(String type, Pageable pageable);
    Page<Room> findRoomByTitleContaining(String title, Pageable pageable);

    @Query("SELECT r FROM room r WHERE r.roomType = :roomType AND SIZE(r.maleParty.members) <= :malePartySize")
    Page<Room> findAllByRoomTypeAndMalePartyMemberCountLessThanEqual(String roomType, Long malePartySize, Pageable pageable);

    @Query("SELECT r FROM room r WHERE r.roomType = :roomType AND SIZE(r.femaleParty.members) <= :femalePartySize")
    Page<Room> findAllByRoomTypeAndFemalePartyMemberCountLessThanEqual(String roomType, Long femalePartySize, Pageable pageable);

}

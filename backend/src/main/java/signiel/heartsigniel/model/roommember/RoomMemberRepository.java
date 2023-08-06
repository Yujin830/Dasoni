package signiel.heartsigniel.model.roommember;

import org.springframework.data.jpa.repository.JpaRepository;
import signiel.heartsigniel.model.member.Member;

import java.util.List;
import java.util.Optional;

public interface RoomMemberRepository extends JpaRepository<RoomMember, Long> {
    Optional<RoomMember> findRoomMemberByMember(Member member);
    List<RoomMember> findRoomMembersByRoom_IdAndMember_Gender(Long roomId, String gender);
    Optional<RoomMember> findRoomMemberByRoom_IdAndMember_MemberId(Long roomId, Long memberId);
}
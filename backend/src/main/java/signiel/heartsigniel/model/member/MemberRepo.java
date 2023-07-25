package signiel.heartsigniel.model.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Transactional
public interface MemberRepo extends JpaRepository<Member, Long> {
    Optional<Member> findByLoginId(String loginId);

    @Modifying
    @Query(value = "delete from Authority a where a.name in :name and a.member.memberId in :member")
    void deleteUserRole(@Param("name")String name, @Param("member") Long member);


}



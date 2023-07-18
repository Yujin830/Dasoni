package signiel.heartsigniel.jpa;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * 모든 Entity의 상위 클래스에서
 * createDate, updateDate를 자동으로 관리해주는 역할.
 */

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {

    //엔티티가 생성된 시간
    @CreatedDate
    private LocalDateTime createDate;

    // 엔티티가 수정된 시간
    @LastModifiedDate
    private LocalDateTime modifiedDate;
}

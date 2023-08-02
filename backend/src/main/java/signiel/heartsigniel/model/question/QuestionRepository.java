package signiel.heartsigniel.model.question;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;


@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query(value = "SELECT DISTINCT * FROM question ORDER BY RAND() LIMIT 3", nativeQuery = true)
    List<Question> randomQuestion();
}

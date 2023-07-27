package signiel.heartsigniel.model.question;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepo extends JpaRepository<Question, Long> {
    @Query(value = "SELECT DISTINCT * FROM question ORDER BY RAND() LIMIT 3", nativeQuery = true)
    List<Question> randomQuestion();
}

package signiel.heartsigniel.model.member;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepo extends JpaRepository<Image, Long> {
    Image findImageByStoredName(String filename);
    Image findImageByAccessUrl(String url);
}

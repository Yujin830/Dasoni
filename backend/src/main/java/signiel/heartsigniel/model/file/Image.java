package signiel.heartsigniel.model.file;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originName; // 이미지 파일 본 이름
    private String storedName; // 이미지 파일이 S3에 저장될 때 이름
    private String accessUrl; // S3 내부 이미지에 접근할 수 있는 URL

    public Image(String originName){
        this.originName = originName;
        this.storedName = getFileName(originName);
        this.accessUrl = "";
    }

    public void setAccessUrl(String accessUrl){
        this.accessUrl = accessUrl;
    }

    // 이미지 파일 확장자 추출 메소드
    public String extractExtension(String originName){
        int index = originName.lastIndexOf('.');

        return originName.substring(index, originName.length());
    }
    
    // 이미지 파일 이름 저장 위한 이름 변환 메소드
    public String getFileName(String originName){
        return UUID.randomUUID()+"."+extractExtension(originName);
    }
}

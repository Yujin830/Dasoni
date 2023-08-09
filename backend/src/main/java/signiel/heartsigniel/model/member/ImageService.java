package signiel.heartsigniel.model.member;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import signiel.heartsigniel.model.member.dto.ImageSaveDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {
    private static String bucketName = "signiel-bucket";

    private final AmazonS3Client amazonS3Client;
    private final ImageRepo imageRepo;

    // 혹시 모르니 여러 파일을 업로드 할 수 있게 했음, 하나만 업로드도 가능 
    @Transactional
    public List<String> saveImages(ImageSaveDto saveDto) {
        List<String> resultList = new ArrayList<>();

        for(MultipartFile multipartFile : saveDto.getImages()) {
            String value = saveImage(multipartFile);
            resultList.add(value);
        }
        return resultList;
    }
    @Transactional
    public String saveImage(MultipartFile multipartFile) {
        String originalName = multipartFile.getOriginalFilename();
        Image image = new Image(originalName);
        String filename = image.getStoredName();

        try {
            /*
            * ObjectMetadata는 MultipartFile에서 제공하는 getInputStream() 메소드같은 것이 없어서
            * 객체를 생성하여 매개변수로 전달해주어야함
            * ObjectMetadata는 InputStream에 저장도니 파일의 정보, 즉 MultipartFile의 정보이기 때문에
            * 파일의 형식이 어떤지, 길이가 어느정도 되는지 정보 입력해야함
            * */
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(multipartFile.getContentType());
            objectMetadata.setContentLength(multipartFile.getInputStream().available());

            amazonS3Client.putObject(bucketName, filename, multipartFile.getInputStream(), objectMetadata);

            // 이미지 접근 URL의 경우, amazonS3Client.getUrl() 메소드를 통해 이미지 접근 URL 얻음
            String accessUrl = amazonS3Client.getUrl(bucketName, filename).toString();
            image.setAccessUrl(accessUrl);
        } catch(IOException e) {
            return e.getMessage();
        }

        imageRepo.save(image);

        return image.getAccessUrl();
    }

    // 삭제는 S3에 저장된 이름을 통해 삭제되도록 함
    @Transactional
    public void deleteImage(String filename){
        amazonS3Client.deleteObject(bucketName, filename);
    }
}

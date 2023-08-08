package signiel.heartsigniel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import signiel.heartsigniel.model.file.ImageSaveDto;
import signiel.heartsigniel.model.file.ImageService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ImageController {
    private final ImageService imageService;

    @PostMapping("/image")
    @ResponseStatus(HttpStatus.OK)
    public List<String> saveImage(@ModelAttribute ImageSaveDto imageSaveDto){
        return imageService.saveImages(imageSaveDto);
    }

    // S3에 저장된 이미지 삭제 로직, 이미지 파일의 확장자까지 정확하게 입력해야 삭제 가능
    // S3에 저장되지 않은 이미지 파일의 이름으로 요청하여도 오류 발생 X
    // test용으로 requestParam으로 함 ( 나중엔 member 테이블에서 받아오기 )
    @DeleteMapping("/image")
    @ResponseStatus(HttpStatus.OK)
    public void deleteImage(@RequestParam("fileName") String fileName){
        imageService.deleteImage(fileName);
    }
}

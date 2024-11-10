package image.module.data.controller;

import image.module.data.dto.ImageResponse;
import image.module.data.service.ImageService;
import image.module.data.dto.ImageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping
public class ImageController {

    private final ImageService imageService;

    //원본 이미지 업로드
    @PostMapping("/image/originalImageUpload")
    public ResponseEntity<String> uploadImage(@RequestParam String uploadImageName,
                                              @RequestParam  String storedImageName){
        imageService.saveImage(uploadImageName,storedImageName);
        return ResponseEntity.ok("저장 완료 되었습니다");
    }

    //리사이징 업로드
    @PostMapping("/image/resizingImageUpload")
    public ResponseEntity<String> uploadResizingImage(@RequestParam String uploadImageName,
                                                      @RequestParam  String storedImageName,
                                                      @RequestParam String imageType){

        imageService.saveResizingImage(uploadImageName,storedImageName,imageType);

        return  ResponseEntity.ok("저장 완료 되었습니다.");

    }




}

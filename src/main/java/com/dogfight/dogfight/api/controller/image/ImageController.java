package com.dogfight.dogfight.api.controller.image;

import com.dogfight.dogfight.api.ApiResponse;
import com.dogfight.dogfight.api.service.comment.CommentService;
import com.dogfight.dogfight.api.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
@RequestMapping("/api/v0/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;
    @GetMapping(value="/{folderName}/{imageName}", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public ResponseEntity<byte[]> getImage(@PathVariable("folderName") String folderName,
                                           @PathVariable("imageName") String imageName) throws IOException {
        return new ResponseEntity<byte[]>(imageService.getImage(folderName,imageName), HttpStatus.OK);
    }
}

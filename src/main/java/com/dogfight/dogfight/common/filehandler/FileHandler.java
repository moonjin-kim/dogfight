package com.dogfight.dogfight.common.filehandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.IllegalBlockSizeException;
import java.awt.*;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class FileHandler {
    @Value("${spring.file.upload-dir}")
    private String uploadDir;

    /**
     *
     * @param multipartFile 이미지 파일
     * @param dateTime 파일 생성 시간
     * @return  이미지 주소
     * @throws Exception
     */
    public String saveImage(MultipartFile multipartFile, LocalDateTime dateTime)
            throws Exception{

        // 파일명을 업로드 한 날짜로 변환하여 저장
        DateTimeFormatter dateTimeFormatter =
                DateTimeFormatter.ofPattern("yyyyMMdd");
        String current_date = dateTime.format(dateTimeFormatter);

        // 프로젝트 디렉터리 내의 저장을 위한 절대 경로 설정
        // 경로 구분자 File.separator 사용
        String absolutePath = new File(uploadDir).getAbsolutePath() + File.separator;

        log.info(absolutePath);

        String path = "images" + File.separator + current_date;
        File file = new File(absolutePath + path);

        if(!file.exists()) {
            boolean wasSuccessful = file.mkdirs();

            // 디렉터리 생성에 실패했을 경우
            if(!wasSuccessful)
                System.out.println("file: was not successful");
        }

        // 파일의 확장자 추출
        String originalFileExtension;
        String contentType = multipartFile.getContentType();

        // 확장자명이 존재하지 않을 경우 처리 x
        if(ObjectUtils.isEmpty(contentType)) {
            throw new IllegalBlockSizeException("확장자 명이 존재하지 않음");
        }
        else {  // 확장자가 jpeg, png인 파일들만 받아서 처리
            if(contentType.contains("image/jpeg"))
                originalFileExtension = ".jpg";
            else if(contentType.contains("image/png"))
                originalFileExtension = ".png";
            else if (contentType.contains("image/*"))
                originalFileExtension = ".png";
            else
                throw new IllegalBlockSizeException("옳지 않은 확장자명");
        }

        // 파일명 중복 피하고자 나노초까지 얻어와 지정
        String new_file_name = absolutePath + path + File.separator + System.nanoTime() + originalFileExtension;
        String save_name = path + File.separator + System.nanoTime() + originalFileExtension;

        file = new File(new_file_name);
        multipartFile.transferTo(file);

        // 파일 권한 설정(쓰기, 읽기)
        file.setWritable(true);
        file.setReadable(true);

        return save_name;
    }
}
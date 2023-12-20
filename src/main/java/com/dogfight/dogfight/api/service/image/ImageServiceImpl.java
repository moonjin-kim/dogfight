package com.dogfight.dogfight.api.service.image;

import com.dogfight.dogfight.common.filehandler.FileHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Transactional
@RequiredArgsConstructor
@Service
@Slf4j
public class ImageServiceImpl implements ImageService{

    private final FileHandler fileHandler;
    @Override
    public byte[] getImage(String folderPath, String fileName) throws IOException {
        String path = "/" + folderPath + "/" + fileName;

        return fileHandler.loadImage(path);
    }
}

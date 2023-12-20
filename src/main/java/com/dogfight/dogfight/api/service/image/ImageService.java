package com.dogfight.dogfight.api.service.image;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface ImageService {
    public byte[] getImage(String folderPath, String fileName) throws IOException;
}

package com.lcwd.electronic.store.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface FileService {

    //path = where we want to save file in our system
    String uploadFile(MultipartFile file, String path) throws IOException;

    InputStream getResource(String path, String fileName) throws FileNotFoundException;
}

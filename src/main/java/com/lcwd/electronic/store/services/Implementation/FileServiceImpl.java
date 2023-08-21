package com.lcwd.electronic.store.services.Implementation;

import com.lcwd.electronic.store.exceptions.BadAPIRequestException;
import com.lcwd.electronic.store.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    //log purpose
    Logger log = LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public String uploadFile(MultipartFile file, String path) throws IOException {

        //fetch fileName from uploaded File
        String originalFilename = file.getOriginalFilename();
        log.info("originalFileName:" + originalFilename);

        //generate unique FileName dynamically,
        //because uploaded FileNames can be same
        String fileName = UUID.randomUUID().toString();

        //get extension of uploaded File
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        log.info("file extension: " + extension);

        //append extension in unique FileName generated
        String fileNameWithExtension = fileName + extension;
        log.info("fileNameWithExtension: " + fileNameWithExtension);

        //generate full path to upload file
        String fullPathWithExtension = path + fileNameWithExtension;
        log.info("fullPathWithExtension: " + fullPathWithExtension);

        //check validation on extension
        if(extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg")) {
             //create an object of File Class
            File folder = new File(path);

            //check if folder exists or not
            if(!folder.exists()) {
                //create folders (all levels in path)
                log.info("New Folder created");
                folder.mkdirs();
            }

            //upload File in required path
            Files.copy(file.getInputStream(), Paths.get(fullPathWithExtension));
        }
        else {
            throw new BadAPIRequestException("File With Extension " + extension + " not allowed");
        }

        return fileNameWithExtension;
    }

    @Override
    public InputStream getResource(String path, String fileName) throws FileNotFoundException {

        String fullPath = path + fileName;
        InputStream inputStream = new FileInputStream(fullPath);

        return inputStream;
    }
}

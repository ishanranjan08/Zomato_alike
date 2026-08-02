package com.first.foodo.first_foodo.Service.impl;

import com.first.foodo.first_foodo.Dto.FileUpload;
import com.first.foodo.first_foodo.Exception.ImageErrorException;
import com.first.foodo.first_foodo.Service.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


@Service
public class FileUploadService implements ImageService {



    private Logger log= LoggerFactory.getLogger(this.getClass());

    @Override
    public FileUpload upload(MultipartFile image, String path) throws IOException {

        if(path.isBlank()){
            throw new ImageErrorException("Image is incorrect");

        }

        Path filePath = Paths.get(path.substring(0, path.lastIndexOf("/") + 1));

        log.info(filePath.toString());

        if(!Files.exists(filePath)){
            Files.createDirectories(filePath);
        }
        Path temp = Paths.get(path);
        Files.copy(image.getInputStream(), temp, StandardCopyOption.REPLACE_EXISTING);

        String fileName=path.substring(path.lastIndexOf("/")+1);

        FileUpload fileUpload=new FileUpload(path,fileName);

        return fileUpload;

    }
}

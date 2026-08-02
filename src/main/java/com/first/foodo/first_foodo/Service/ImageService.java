package com.first.foodo.first_foodo.Service;

import com.first.foodo.first_foodo.Dto.FileUpload;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService
{
    FileUpload upload(MultipartFile image,String Path) throws IOException;
}

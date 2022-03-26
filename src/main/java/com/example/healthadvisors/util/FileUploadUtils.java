package com.example.healthadvisors.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Component
public class FileUploadUtils {


    public String uploadImage(MultipartFile[] uploadedFiles, String path) throws IOException {
        if (uploadedFiles.length != 0) {
            for (MultipartFile uploadedFile : uploadedFiles) {
                if (Objects.equals(uploadedFile.getOriginalFilename(), "")) {
                    break;
                }
                String fileName = System.currentTimeMillis() + "_" + uploadedFile.getOriginalFilename();
                File newFile = new File(path+ fileName);
                uploadedFile.transferTo(newFile);
                return newFile.getName();
            }
        }
        return null;
    }

}



package com.example.healthadvisors.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

@Component
public class FileUploadDownLoadUtils {


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

    public byte[] getImage(String path, String picName) throws IOException {
        InputStream inputStream = Files.newInputStream(Paths.get(path + picName));
        return IOUtils.readAllBytes(inputStream);
    }

}



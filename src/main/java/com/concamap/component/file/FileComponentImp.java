package com.concamap.component.file;

import com.concamap.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

@Component
public class FileComponentImp implements FileComponent {
    private FileCopyUtils fileCopyUtils;

    @Value("${upload.path}")
    private String folderUploadPath;

    @Override
    public File copyFile(MultipartFile multipartFile, Users users) throws IOException {
        String fileName = System.currentTimeMillis() + "-" + multipartFile.getOriginalFilename();
        File folderUpload = new File(folderUploadPath, users.getUsername());
        if (!folderUpload.exists()) {
            if (!folderUpload.mkdirs()) {
                throw new IOException();
            }
        }
        File fileDestination = new File(folderUpload, fileName);
        FileCopyUtils.copy(multipartFile.getBytes(), fileDestination);
        return fileDestination;
    }

    @Override
    public String getImageLink(File file) throws FileNotFoundException {
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        String fileName = file.getName();
        String folderName = file.getParentFile().getName();
        return "/" + folderName + "/" + fileName;
    }
}

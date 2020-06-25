package com.concamap.component.file;

import com.concamap.model.Users;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface FileComponent {
    File copyFile(MultipartFile multipartFile, Users users) throws IOException;

    String getImageLink(File file) throws FileNotFoundException;
}

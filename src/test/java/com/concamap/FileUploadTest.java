package com.concamap;

import java.io.File;

public class FileUploadTest {
    public static void main(String[] args) {
        String rootPath = System.getProperty("user.dir");

        File file = new File(rootPath + "\\upload-file-test\\");
        if (!file.exists()) {
            if (file.mkdir()) {
                System.out.println("Success");
            } else {
                System.out.println("err");
            }
        }
    }
}

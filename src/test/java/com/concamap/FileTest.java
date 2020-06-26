package com.concamap;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileTest {
    public static void main(String[] args) throws IOException {
        File file = new File("D:\\Exercise\\CASE STUDY\\casestudy-module4-SharkSquad\\upload-file\\vanduc2514\\1593072265059-Ready Player One 2018 Movie Wallpaper HD.jpg");
        String fileName = file.getName();
        String folderName = file.getParentFile().getName();
        System.out.println("/" + folderName + "/" + fileName);
    }

}

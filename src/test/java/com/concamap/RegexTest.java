package com.concamap;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {
    public static void main(String[] args) {
        String demo = "Cộng hoà xã hội chủ nghĩa việt nam";
        Pattern pattern = Pattern.compile("\\s");
        Matcher matcher = pattern.matcher(demo);
        for (int index = 0; index < 4; index++) {
            if (!matcher.find()) {
                return;
            }
        }
        try {
            System.out.println(demo.substring(0, matcher.end()));
        } catch (IllegalStateException exception) {
            exception.printStackTrace();
            System.out.println(exception.getMessage());
        }
    }
}

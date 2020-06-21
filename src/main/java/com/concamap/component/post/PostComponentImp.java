package com.concamap.component.post;

import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PostComponentImp implements PostComponent {
    @Override
    public String summary(String content, int numberOfWords, String extendString) {
        String contentPlainText = toPlainText(content);
        final Pattern WB_PATTERN = Pattern.compile("(?<=\\w)\\b");
        if (numberOfWords <= 0 || contentPlainText == null) {
            return "";
        }
        Matcher m = WB_PATTERN.matcher(contentPlainText);
        for (int i = 0; i < numberOfWords && m.find(); i++) {
            if (m.hitEnd()) {
                return contentPlainText.concat(extendString);
            }
        }
        return contentPlainText.substring(0, m.end()).concat(extendString);
    }

    @Override
    public String toPlainText(String html) {
        return Jsoup.parse(html).text();
    }
}

package com.concamap.component.post;

import com.concamap.services.post.PostService;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PostComponentImp implements PostComponent {
    private final PostService postService;

    @Autowired
    public PostComponentImp(PostService postService) {
        this.postService = postService;
    }

    @Override
    public String summary(String content, int numberOfWords, String extendString) {
        String contentPlainText = toPlainText(content);
        final Pattern WB_PATTERN = Pattern.compile("\\s");
        if (numberOfWords <= 0 || contentPlainText == null) {
            return "";
        }
        Matcher m = WB_PATTERN.matcher(contentPlainText);
        for (int i = 0; i < numberOfWords; i++) {
            if (!m.find()) {
                return contentPlainText.concat(extendString);
            }
        }
        return contentPlainText.substring(0, m.end()).concat(extendString);
    }

    @Override
    public String toPlainText(String html) {
        return Jsoup.parse(html).text();
    }

    @Override
    public String toAnchorName(String title) {
        return removeAccent(title) + "-" + (postService.count() + 1);
    }

    private String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String result = pattern.matcher(temp).replaceAll("").replaceAll("[^a-zA-Z0-9 ]", "").replace('đ', 'd').replace('Đ', 'D').replace(' ', '-');
        char firstChar = result.charAt(0);
        if(Character.toString(firstChar).equals("-")){
            result = result.substring(1);
        }
        return result;
    }
}

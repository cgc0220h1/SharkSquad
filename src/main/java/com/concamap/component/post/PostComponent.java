package com.concamap.component.post;

public interface PostComponent {
    String summary(String content, int numberOfWords, String extendString);

    String toPlainText(String html);
}

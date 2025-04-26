package org.spring.ch6.facoryBean;

import lombok.Getter;

@Getter
public class Message {
    private final String text;

    private Message(String text) {
        this.text = text;
    }

    public static Message newMessage(String text) {     // 정적 팩토리 메서드
        return new Message(text);
    }
}

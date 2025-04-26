package org.spring.ch6.facoryBean;

import org.springframework.beans.factory.FactoryBean;

public class MessageFactoryBean implements FactoryBean<Message> {
    private String text;

    public void setText(String text) {
        this.text = text;               // Message 객체에 주입될 text 정보를 대신 DI 받는다. 이는 Message 객체 생성 시 사용된다.
    }

    @Override
    public Message getObject() throws Exception {
        return Message.newMessage(this.text);
    }

    @Override
    public Class<? extends Message> getObjectType() {
        return Message.class;
    }

    @Override
    public boolean isSingleton() {
        return false;       // 현재에는 getObject() 호출 시마다 객체를 생성하므로 프로토타입이다.
    }
}

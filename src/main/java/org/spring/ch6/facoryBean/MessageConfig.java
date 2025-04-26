package org.spring.ch6.facoryBean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageConfig {

    // message 빈 설정의 class 속성은 MessageFactoryBean 이지만, getBean()이 리턴한 오브젝트는 Message 타입이다.
    @Bean
    public MessageFactoryBean message() {
        MessageFactoryBean factoryBean = new MessageFactoryBean();
        factoryBean.setText("Factory Bean");
        return factoryBean;
    }
}

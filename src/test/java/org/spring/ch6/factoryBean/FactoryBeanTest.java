package org.spring.ch6.factoryBean;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.spring.ch6.facoryBean.Message;
import org.spring.ch6.facoryBean.MessageConfig;
import org.spring.ch6.facoryBean.MessageFactoryBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = MessageConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)          // 각 테스트 메서드간 테스트 인스턴스 공유
public class FactoryBeanTest {
    @Autowired
    Object message;             // applicationContext.getBean("message") 의 반환값은 Message 객체이다.

    @Autowired
    FactoryBean<Message> factoryBean;       // applicationContext.getBean("&message") 의 반환값은 FactoryBean 객체이다.

    @Test
    public void getMessageFromFactoryBean() {
        assertThat(message).isInstanceOf(Message.class);
        assertThat(((Message) message).getText()).isEqualTo("Factory Bean");
    }

    @Test
    public void getFactoryBean() {
        assertThat(factoryBean).isInstanceOf(MessageFactoryBean.class);
    }
}

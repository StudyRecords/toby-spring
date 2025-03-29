package org.spring.ch5.transaction.mail;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

@Getter
public class MailProperties {
    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.port}")
    private int port;
    @Value("${spring.mail.username}")
    private String username;
    @Value("${spring.mail.password}")
    private String password;
    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String auth;
    @Value("${spring.mail.properties.mail.smtp.timeout}")
    private String timeout;
    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private String startTls;
}

package org.spring.ch6.transaction.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@RequiredArgsConstructor
public class MailSenderFactory {
    private final org.spring.ch6.transaction.mail.MailProperties MailProperties;

    public MailSender createMyMailSender() {
        JavaMailSenderImpl myMailSender = new JavaMailSenderImpl();

        // 필수: SMTP 서버 설정
        myMailSender.setHost(MailProperties.getHost());  // 예: Gmail SMTP
        myMailSender.setPort(MailProperties.getPort());
        myMailSender.setUsername(MailProperties.getUsername()); // 발신자 이메일
        myMailSender.setPassword(MailProperties.getPassword());    // 앱 비밀번호 (Gmail 경우)

        // 추가: TLS/SSL 설정
        Properties props = myMailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", MailProperties.getAuth());
        props.put("mail.smtp.starttls.enable", MailProperties.getStartTls()); // TLS 사용

        return myMailSender;
    }
}

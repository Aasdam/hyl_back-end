package com.hyl.mailserver.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    //************************************************** LOGGER
    Logger logger = LoggerFactory.getLogger(MailConfig.class);


    //************************************************** PARAMS
    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private Integer port;

    @Value("${spring.mail.protocol}")
    private String protocol;

    @Value("${spring.mail.properties.smtp.auth}")
    private String smtpAuth;

    @Value("${spring.mail.properties.smtp.starttls.enable}")
    private String smtpTls;

    @Value("${spring.mail.properties.debug}")
    private String debugLog;


    //************************************************** BEAN
    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);

        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", protocol);
        props.put("mail.smtp.auth", smtpAuth);
        props.put("mail.smtp.starttls.enable", smtpTls);
        props.put("mail.debug", debugLog);

        return mailSender;
    }
}

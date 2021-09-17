package com.hyl.mailserver.service;

import com.hyl.mailserver.exception.CustomInternalServerErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Hashtable;
import java.util.List;

@Service
@Transactional
public class MailService {

    //************************************************** LOGGER
    Logger logger = LoggerFactory.getLogger(MailService.class);


    //************************************************** PARAMS
    @Value("${hyl.url.localstorage}")
    private String localUrl;


    //************************************************** BEANS
    private final JavaMailSender emailSender;


    //************************************************** SETTERS

   /* public void setLocalUrl(String localUrl) {
        MailService.localUrl = localUrl;
    }*/


    //************************************************** CONSTRUCTOR
    // MailConfig.getJavaMailSender()
    @Autowired
    public MailService(@Qualifier("getJavaMailSender") JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }



    //************************************************** METHODES
    public void sendMail(String destinary, String subject, String content,
                         String encoding, boolean html, List<Hashtable<String, String>> urlImages) {
        MimeMessage message = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, encoding);
            helper.setTo(destinary);
            helper.setSubject(subject);
            helper.setText(content, html);

            if(urlImages != null && !urlImages.isEmpty()) {
                for(Hashtable<String, String> table : urlImages) {
                    File file = new File(localUrl + table.get("url"));
                    if(file.exists()) {
                        helper.addAttachment(table.get("name"), new FileDataSource(file));
                    }
                }
            }

            this.emailSender.send(message);
        } catch (MessagingException e) {
            throw new CustomInternalServerErrorException(e.getMessage());
        }
    }
}

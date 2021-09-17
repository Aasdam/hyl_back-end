package com.hyl.mailserver.controller;

import com.hyl.mailserver.exception.CustomBadRequestException;
import com.hyl.mailserver.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.time.DateTimeException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

@RestController
@RequestMapping(path = "mail")
public class MailController {

    //************************************************** LOGGER
    Logger logger = LoggerFactory.getLogger(MailController.class);


    //************************************************** BEAN
    private final MailService mailService;


    //************************************************** CONSTRUCTOR
    @Autowired
    public MailController(MailService mailService) {
        this.mailService = mailService;
    }


    //************************************************** METHODES
    @PostMapping(value = "/send-mail")
    public void sendMail(@RequestBody(required = false) String body) {
        JSONObject data;
        try {
            data = new JSONObject(body);
        } catch (JSONException | NullPointerException e) {
            throw new CustomBadRequestException("Le format JSON du corps de la requête est incorrect.");
        }

        String destinary = data.optString("destinary");
        String subject = data.optString("subject");
        String content = data.optString("content");
        String encoding = data.optString("encoding");
        boolean html = data.optBoolean("html", true);
        JSONArray urlImagesArray = data.optJSONArray("urlImages");

        if (destinary.isBlank())
            throw new CustomBadRequestException("L'adresse email est vide");
        else if (!destinary.matches("^[a-z0-9._-]{3,99}@[a-z0-9._-]{3,99}.[a-z]{2,}$"))
            throw new CustomBadRequestException("Le format de l'adresse email est incorrect");
        if (subject.isBlank())
            subject = "Hyl - Courriel";
        if (content.isBlank())
            content = "Ce courriel vous a été envoyé par HYL.";
        if (encoding.isBlank())
            encoding = "utf-8";

        List<Hashtable<String,String>> urlImages = null;
        if (urlImagesArray != null) {
            urlImages = new ArrayList<>();
            int index = urlImagesArray.length();
            for (int j = 0; j < index; j++) {
                if (!urlImagesArray.isNull(j)) {
                    JSONObject dataTable = urlImagesArray.optJSONObject(j);
                    String name = dataTable.optString("name");
                    String url = dataTable.optString("url");
                    if (!name.isBlank() && !url.isBlank()) {
                        Hashtable<String, String> table = new Hashtable<>();
                        table.put("name", name);
                        table.put("url", url);
                        if (!urlImages.contains(table)) {
                            urlImages.add(table);
                        }
                    }
                }
            }
        }
        mailService.sendMail(destinary, subject, content, encoding, html, urlImages);
    }
}

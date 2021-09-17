package com.hyl.batch.service;

import com.hyl.batch.model.Loan;
import com.hyl.batch.model.Picture;
import com.hyl.batch.model.SubItem;
import com.hyl.batch.model.User;
import com.hyl.batch.proxy.MailProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Transactional
@Service
public class MailService {

    //****************************************** LOGGER
    private final Logger logger = LoggerFactory.getLogger(MailService.class);

    //****************************************** BEANS
    private final MailProxy mailProxy;

    //****************************************** CONSTRUCTOR
    @Autowired
    public MailService(MailProxy mailProxy) {
        this.mailProxy = mailProxy;
    }

    //****************************************** METHODES
    public void sendCallBack (Loan loan,  HashMap<String,String> signInAdminMap) {

        User user = loan.getUser();
        SubItem subItem = loan.getSubItem();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy à HH:mm:ss");
        int nbrDay = (int) ((new Date().getTime() -  loan.getStartDate().getTime()) / (1000*60*60*24));

        String subject = "[HYL] Rappel - Prêt en cours";
        String content = "<p>Bonjour " + defineCivility(user.getCivility()) + " "
                + user.getSurname() + " " + user.getName() + ",</p>"
                + "<p>Suite à votre demande,"
                + "<br />Nous vous informons que le "
                + "<strong>" + sdf.format(loan.getStartDate()) + "</strong> vous avez enregistré un prêt "
                + "avec demande de rappel concernant l'objet <strong>" + subItem.getItem().getName()
                + "</strong> ayant pour référence <strong>" + subItem.getReference() + "</strong> à "
                + "l'intention de <strong>" + loan.getBeneficiary() + "</strong>."
                + "<br />Vous avez donc prêté votre objet depuis <strong>" + nbrDay + "</strong> jours.</p>"
                + "<p>Merci de nous faire confiance et d'utiliser <strong>HYL</strong>.</p>";

        HashMap<String, Object> body = new HashMap<>();
        body.put("destinary", user.getEmail());
        body.put("subject", subject);
        body.put("content", content);
        body.put("urlImages", formatUrlImages(subItem.getUrlImages()));
        body.put("encoding", "utf-8");
        body.put("html", "true");

        HashMap<String, String> header = new HashMap<>();
        header.put(HttpHeaders.AUTHORIZATION, "Bearer "+this.extractJWT(signInAdminMap));

        mailProxy.sendMail(header, body);
    }

    private String extractJWT (HashMap<String,String> signInAdminMap) {
        String token = signInAdminMap.getOrDefault("token", null);
        if (token == null || token.isBlank()) {
            throw new RuntimeException("Aucun token n'est spécifié.");
        } else {
            return token;
        }
    }

    private List<Hashtable<String,String>> formatUrlImages (List<Picture> pictures) {
        List<Hashtable<String,String>> urlImages = null;
        if(pictures != null && !pictures.isEmpty()) {
            urlImages = new ArrayList<>();
            for (Picture picture : pictures) {
                String name = picture.getName();
                String url = picture.getUrl();
                if (name != null && !name.isBlank() && url != null && !url.isEmpty()) {
                    Hashtable<String, String> table = new Hashtable<>();
                    table.put("name", name);
                    table.put("url", url);
                    if (!urlImages.contains(table))
                        urlImages.add(table);
                }
            }
        }
        return urlImages;
    }

    private String defineCivility(String civility) {
        if (civility != null && civility.equals("M")) return "M.";
        else if (civility != null && civility.equals("W")) return "Mme";
        else return  "";
    }
}

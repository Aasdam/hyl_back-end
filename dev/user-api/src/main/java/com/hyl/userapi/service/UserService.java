package com.hyl.userapi.service;

import com.hyl.userapi.dao.UserDao;
import com.hyl.userapi.encoder.PBKDF2Encoder;
import com.hyl.userapi.exception.CustomBadRequestException;
import com.hyl.userapi.exception.CustomNotFoundException;
import com.hyl.userapi.exception.CustomUnauthorizedException;
import com.hyl.userapi.model.User;
import com.hyl.userapi.proxy.MailProxy;
import com.sun.net.httpserver.HttpsParameters;
import io.micrometer.shaded.org.pcollections.HashPMap;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
public class UserService {

    // ********************************************************* Logger
    private final Logger logger = LoggerFactory.getLogger(UserService.class);


    // ********************************************************* Bean
    private final UserDao userDao;
    private final PBKDF2Encoder passwordEncoder;
    private final HttpSession httpSession;
    private final MailProxy mailProxy;

    // ********************************************************* Constructor
    @Autowired
    public UserService(UserDao userDao, PBKDF2Encoder passwordEncoder, HttpSession httpSession, MailProxy mailProxy) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.httpSession = httpSession;
        this.mailProxy = mailProxy;
    }


    // ********************************************************* Méthodes
    public String authenticateUser (String email, String password) {
        User user = this.getUserByEmail(email);
        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomUnauthorizedException("Couple email/password incorrect");
        } else {
            httpSession.setAttribute("user"+user.getId(), user);
            return String.valueOf(user.getId());
        }

    }

    public void registerUser(User user) {
        toDoBeforeSavingUser(user, true);
        userDao.save(user);
    }

    public void disconnectUser(long idUser) {
        httpSession.removeAttribute("user"+idUser);
    }

    public void forgotPasswordUser(String email, String token) {
        User user = this.getUserByEmail(email);
        String password = generatePassword();
        user.setPassword(passwordEncoder.encode(password));
        userDao.save(user);
        this.sendNewPasswordWithMailProxy(user, password, token);
    }

    public Boolean checkAtomicEmail(String email) {
        return userDao.findByEmail(email).isEmpty();
    }

    public Boolean checkAtomicCellphone(String cellphone) {
        if (cellphone.startsWith("0")) {
            cellphone = "+33" + cellphone.substring(1);
        }
        return userDao.findByCellphone(cellphone).isEmpty();
    }

    public User getUserById(long idUser) {
        Optional<User> optUser = userDao.findById(idUser);
        if (optUser.isPresent()) return optUser.get();
        else throw new CustomNotFoundException("Utilisateur introuvable");
    }

    public User getUserByEmail(String email) {
        Optional<User> optUser = userDao.findByEmail(email);
        if (optUser.isPresent()) return optUser.get();
        throw new CustomNotFoundException("Aucun utilisateur n'est associé à "+email);
    }

    public void updateUser(long idUser, User user) {
        User user0 = this.getUserById(idUser);
        if (user0.getCellphone() != null && !user0.getCellphone().isBlank()) {
            user0.setCellphone("0" + user0.getCellphone().substring(3));
        }
        boolean checkAtomicCellphone = false;
        boolean encodePass = false;

        if (user.getName() != null && !user.getName().equals(user0.getName())) {
            user0.setName(user.getName());
        }
        if (user.getSurname() != null && !user.getSurname().equals(user0.getSurname())) {
            user0.setSurname(user.getSurname());
        }
        if (user.getPseudo() != null && !user.getPseudo().equals(user0.getPseudo())) {
            user0.setPseudo(user.getPseudo());
        }
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            encodePass = true;
            user0.setPassword(user.getPassword());
        }
        if (user.getCivility() != null && !user.getCivility().equals(user0.getCivility())) {
            user0.setCivility(user.getCivility());
        }
        if (user.getCellphone() != null && !user.getCellphone().equals(user0.getCellphone())) {
            user0.setCellphone(user.getCellphone());
            checkAtomicCellphone = true;
        }

        this.toDoBeforeSavingUser(user0, encodePass);
        this.checkUserIntegrity(user0, checkAtomicCellphone);
        this.userDao.save(user0);
    }

    private void checkUserIntegrity(@Validated(User.UpdateValidation.class) User user, boolean checkAtomicCellphone) {
        if (checkAtomicCellphone && this.checkAtomicCellphone(user.getCellphone())) {
            throw new CustomBadRequestException("Le numéro de téléphone est déjà utilisé.");
        }
    }

    private void toDoBeforeSavingUser(User user, boolean encodePass) {

        if (user.getCellphone() != null && user.getCellphone().isEmpty()) {
            user.setCellphone(null);
        } else if (user.getCellphone() != null && !user.getCellphone().isEmpty()) {
            user.setCellphone( "+33" + user.getCellphone().substring(1) );
        }
        if (user.getCivility() != null && user.getCivility().isEmpty()) {
            user.setCivility(null);
        }
        if (encodePass) user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    private String generatePassword() {
        String upperCaseLetters = RandomStringUtils.random(2, 65, 90, true, true);
        String lowerCaseLetters = RandomStringUtils.random(2, 97, 122, true, true);
        String numbers = RandomStringUtils.randomNumeric(2);
        String specialChar = RandomStringUtils.random(2, 33, 47, false, false);
        String totalChars = RandomStringUtils.randomAlphanumeric(2);
        String combinedChars = upperCaseLetters.concat(lowerCaseLetters).concat(numbers).concat(specialChar).concat(totalChars);
        List<Character> pwdChars = combinedChars.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
        Collections.shuffle(pwdChars);
        return  pwdChars.stream().collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
    }

    private void sendNewPasswordWithMailProxy(User user, String password, String token) {
        String subject = "[HYL] Réintialisation du mot de passe";
        String content = "<p>Bonjour " + defineCivility(user.getCivility()) + " "
                + user.getSurname() + " " + user.getName() + ",</p>"
                + "<p>Suite à votre demande, nous avons réintialisé votre mot de passe."
                + "<br />À présent pour vous connecter, vous devez utiliser le mot de passe suivant : "
                + "<strong>" + password + "</strong></p>"
                + "<p>Merci de nous faire confiance et d'utiliser <strong>HYL</strong>.</p>";

        HashMap<String, String> body = new HashMap<>();
        body.put("destinary", user.getEmail());
        body.put("subject", subject);
        body.put("content", content);
        body.put("encoding", "utf-8");
        body.put("html", "true");

        HashMap<String, String> header = new HashMap<>();
        header.put(HttpHeaders.AUTHORIZATION, token);

        mailProxy.sendMail(header, body);
    }

    private String defineCivility(String civility) {
        if (civility != null && civility.equals("M")) return "M.";
        else if (civility != null && civility.equals("W")) return "Mme";
        else return  "";
    }
}

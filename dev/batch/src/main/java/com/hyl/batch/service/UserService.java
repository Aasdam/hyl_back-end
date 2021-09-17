package com.hyl.batch.service;

import com.hyl.batch.dao.UserDao;
import com.hyl.batch.model.User;
import com.hyl.batch.proxy.GatewayProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    //****************************************** LOGGER
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    //****************************************** PARAMS
    @Value("${hyl.admin.username}")
    private String username;

    @Value("${hyl.admin.password}")
    private String password;

    //****************************************** BEANS
    private final UserDao userDao;
    private final GatewayProxy gatewayProxy;

    //****************************************** CONSTRUCTOR
    @Autowired
    public UserService(UserDao userDao, GatewayProxy gatewayProxy) {
        this.userDao = userDao;
        this.gatewayProxy = gatewayProxy;
    }

    //****************************************** METHODES
    public User getUserById(long idUser) {
        Optional<User> optUser = userDao.findById(idUser);
        if (optUser.isPresent()) return optUser.get();
        else throw new RuntimeException("Utilisateur introuvable");
    }

    public HashMap<String, String> signInAdmin() {
        HashMap<String,String> body = new HashMap<>();
        body.put("email", username);
        body.put("password", password);

        return gatewayProxy.signIn(body);
    }
}

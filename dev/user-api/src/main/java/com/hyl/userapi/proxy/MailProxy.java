package com.hyl.userapi.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.HashMap;

@Component
@FeignClient(name = "mail-server", url = "${hyl.gateway.url}"+"/mail")
public interface MailProxy {

    @PostMapping(value = "/send-mail")
    void sendMail(@RequestHeader HashMap<String, String> header,
                    @RequestBody HashMap<String, String> body);
}

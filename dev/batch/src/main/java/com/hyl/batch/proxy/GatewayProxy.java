package com.hyl.batch.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;

@Component
@FeignClient(name = "gateway-server", url = "${hyl.gateway.url}")
public interface GatewayProxy {

    @PostMapping(path = "/signin")
    HashMap<String, String> signIn(@RequestBody HashMap<String, String> body);
}

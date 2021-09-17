package com.hyl.itemapi.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Component
@FeignClient(name = "loan-api", url = "${hyl.gateway.url}"+"/loan")
public interface LoanProxy {

    @PatchMapping(value = "/update-reference")
    void updateReference(@RequestHeader HashMap<String, String> headerMap,
                     @RequestBody HashMap<String, String> hashMap);

    @DeleteMapping(value = "/delete-loans-by-reference")
    void deleteLoanByReference(@RequestHeader HashMap<String, String> headerMap,
                         @RequestParam String reference);

}

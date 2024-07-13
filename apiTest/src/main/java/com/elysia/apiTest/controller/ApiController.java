package com.elysia.apiTest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cxb
 * @ClassName ApiController
 * @date 17/5/2023 上午11:02
 */
@RestController
@RequestMapping("/api/test")
public class ApiController {
    @GetMapping("/Oauth2/baidu/redirect")
    public String getRedirectInfo(String code, String state) {
        return ("code=" + code + ";state=" + state);
    }
}

package com.elysia.demoApp.controller;

import com.elysia.demoApp.model.result.Result;
import com.elysia.demoApp.model.vo.LoginVo;
import com.elysia.demoApp.service.AccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 用户账号相关
 * @author cxb
 * @ClassName AccountController
 * @date 21/5/2023 下午3:36
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/user")
@Api(value = "用户登录相关的接口")
@CrossOrigin
public class AccountController {
    @Resource
    private AccountService accountService;
    @ApiOperation("用户手机登录(未注册用户将自动注册)")
    @PostMapping("/login")
    public Result UserLogin(@RequestBody LoginVo loginVo) {
        Map<String, Object> result = accountService.userLogin(loginVo);
        return Result.ok(result);
    }
}

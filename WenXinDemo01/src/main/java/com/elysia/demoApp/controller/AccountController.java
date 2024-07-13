package com.elysia.demoApp.controller;

import com.elysia.demoApp.model.entity.user.MessageStatus;
import com.elysia.demoApp.model.result.utils.Result;
import com.elysia.demoApp.model.vo.LoginVo;
import com.elysia.demoApp.service.AccountService;
import com.elysia.demoApp.utils.StringUtils;
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
@RequestMapping(value = "/user/auth")
@Api(value = "用户登录相关的接口")
@CrossOrigin
public class AccountController {
    @Resource
    private AccountService accountService;
    @ApiOperation("请求验证码(用于登录、注册）")
    @GetMapping("/sendMessage/{phone}")
    public Result getMessage(@PathVariable("phone") String phone) {
        MessageStatus checked = accountService.checkIntervalTime(phone);
        // 判断是否处于可发送状态
        if (checked.getStatus()){
            boolean isOk = accountService.sendMessage(phone);
            if (isOk){
                return Result.ok(checked);
            } else {
                return Result.fail(checked);
            }
        } else {
            return Result.fail(checked);
        }
    }
    @ApiOperation("用户手机验证码登录(未注册用户将自动注册)")
    @PostMapping("/login")
    public Result userLogin(@RequestBody LoginVo loginVo) {
        Map<String, Object> result = accountService.userLoginByWeiXin(loginVo);
        return Result.ok(result);
    }

    @ApiOperation("获取百度（第三方）授权登录url")
    @GetMapping("/Oauth2/baidu/authorization")
    public Result getBaiduAuthorization() {
       String authUrl = accountService.getBaiduAuthUrl();
       if (StringUtils.isEmpty(authUrl)) {
           return Result.fail();
       }
       return Result.ok(authUrl);
    }

    @ApiOperation("百度（第三方）授权登录回调接口")
    @GetMapping("/Oauth2/baidu/redirect")
    public Result getRedirectInfo(String code, String state) {
        Map<String, Object> authResult = accountService.userLoginByBaidu(code, state);
        return Result.ok(authResult);
    }
}

package com.elysia.demoApp.service;

import com.elysia.demoApp.model.vo.LoginVo;

import java.util.Map;

/**
 * @author cxb
 * @ClassName AccountService
 * @date 21/5/2023 下午7:12
 */
public interface AccountService {
    Map<String, Object> userLogin(LoginVo loginVo);
}

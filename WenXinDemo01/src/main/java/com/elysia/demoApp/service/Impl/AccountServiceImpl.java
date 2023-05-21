package com.elysia.demoApp.service.Impl;

import com.elysia.demoApp.model.vo.LoginVo;
import com.elysia.demoApp.service.AccountService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author cxb
 * @ClassName AccountServiceImpl
 * @date 21/5/2023 下午7:13
 */
@Service
public class AccountServiceImpl implements AccountService {
    @Override
    public Map<String, Object> userLogin(LoginVo loginVo) {
        return null;
    }
}

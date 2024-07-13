package com.elysia.demoApp.interceptor;

import com.elysia.demoApp.exception.WenXinException;
import com.elysia.demoApp.model.result.utils.ResultCodeEnum;
import com.elysia.demoApp.utils.AccessTokenUtils;
import com.elysia.demoApp.utils.RedisUtils;
import com.elysia.demoApp.utils.StringUtils;
import com.elysia.demoApp.utils.helper.JwtHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 暂时使用简单的jwt拦截器拦截判断，后期会更换成更加完善的spring-security
 * @author cxb
 * @ClassName JwtInterceptor
 * @date 23/5/2023 下午3:34
 */
@Component
public class JwtInterceptor implements HandlerInterceptor{

    @Resource
    private AccessTokenUtils accessTokenUtils;

    @Resource
    private RedisUtils redisUtils;
    private Boolean isBan = false;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从请求中取出对应的token
        String token = request.getHeader("Authorization");
        if(StringUtils.isEmpty(token)) {
            throw new WenXinException(ResultCodeEnum.LOGIN_AUTH);
        }

        try {
            // 验证并取出对应uid
            String uid = accessTokenUtils.getUid(token);
            if (uid == null) {
                throw new WenXinException(ResultCodeEnum.LOGIN_ERROR);
            }

            // 判断access_token是否处于黑名单
             isBan = redisUtils.existValue("Access_Token" + uid, token);
            if(isBan) {
                throw new WenXinException(ResultCodeEnum.LOGIN_AUTH);
            }

            // access_token验证通过并且uid不为空则验证通过
            return true;
        } catch (Exception e) {
            if (isBan) {
                throw new WenXinException(ResultCodeEnum.LOGIN_AUTH);
            }
            // TODO 现在完成了颁发部分，验证部分只修改了拦截器！
            // access_token验证失败，验证refresh_token
            String refreshToken = request.getHeader("RefreshToken");
            if (StringUtils.isEmpty(refreshToken)) {
                throw new WenXinException(ResultCodeEnum.LOGIN_AUTH);
            }
            // 验证并取出对应的uid和userName
            String uid = accessTokenUtils.getUidByRe(refreshToken);
            String userName = accessTokenUtils.getUserNameByRe(refreshToken);
            if (StringUtils.isEmpty(uid) || StringUtils.isEmpty(userName)) {
                throw new WenXinException(ResultCodeEnum.LOGIN_ERROR);
            }

            // 在刷新的token前先要判断该refresh_token是否处于黑名单
            boolean existValue = redisUtils.existValue("Refresh_Token_" + uid, refreshToken);
            if (existValue) {
                throw new WenXinException(ResultCodeEnum.LOGIN_ERROR);
            }

            // 确认refresh_token有效，生成新的access_token和refresh_token
            Map<String, Object> tokenNew = JwtHelper.createToken(uid, userName);
            // 刷新access_token和refresh_token后，将原来的refresh_token计入黑名单
            redisUtils.setList("Refresh_Token_" + uid, refreshToken);
            String accessTokenNew = "Bearer " + tokenNew.get("access_token");
            String refreshTokenNew = (String) tokenNew.get("refresh_token");
            throw new WenXinException("access_token:" + accessTokenNew + ";" + "refresh_token:" + refreshTokenNew, 403);
        }
    }
}

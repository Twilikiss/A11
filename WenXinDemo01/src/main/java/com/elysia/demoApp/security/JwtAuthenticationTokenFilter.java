package com.elysia.demoApp.security;

import com.elysia.demoApp.utils.helper.JwtHelper;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * TODO 之后完善spring-security权限校验
 * @author cxb
 * @ClassName JwtAuthenticationTokenFilter
 * @date 23/5/2023 上午10:48
 */
//public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
//    @Resource
//    private JwtHelper jwtHelper;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
////        String token = request.getHeader("");
//    }
//}

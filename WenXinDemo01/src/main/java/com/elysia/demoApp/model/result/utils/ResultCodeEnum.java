package com.elysia.demoApp.model.result.utils;

import lombok.Getter;

/**
 * code枚举类
 * @author cxb
 */
@Getter
public enum ResultCodeEnum {
    SUCCESS(200,"成功"),
    SUCCESS_GET(200,"获取成功"),
    FAIL(201, "失败"),
    PARAM_ERROR( 202, "参数不正确"),
    DELETE_ERROR(203, "数据删除失败，请重试"),
    DATA_ERROR(204, "数据异常，无法找到指定数据"),
    DATA_UPDATE_ERROR(205, "数据版本异常"),
    LOGIN_ERROR(208, "token失效，请重新登陆"),
    PERMISSION(209, "没有权限"),
    CODE_ERROR(210, "验证码错误"),
    //    LOGIN_MOBLE_ERROR(211, "账号不正确"),
    LOGIN_DISABLED_ERROR(212, "用户已被禁用"),
    REGISTER_MOBLE_ERROR(213, "手机号已被使用"),
    LOGIN_AUTH(214, "需要登录"),
    LOGIN_ACL(215, "没有权限"),
    URL_ENCODE_ERROR( 216, "URL编码失败"),
    ILLEGAL_CALLBACK_REQUEST_ERROR( 217, "非法的授权回调请求，请重试"),
    FETCH_ACCESSTOKEN_FAILD( 218, "获取accessToken失败"),
    FETCH_USERINFO_ERROR( 219, "获取用户信息失败"),
    //LOGIN_ERROR( 23005, "登录失败"),
    PUBLIC_KEY_ERROR(500, "公钥获取错误，请联系工程师"),
    FILE_LOAD_ERROR(500, "文件上传错误"),
    SIGN_ERROR(300, "签名错误或者过期，请重新登录"),
    ACCESSTOKEN_ERROR(401, "token非法，授权中止"),
    ;

    private Integer code;
    private String message;

    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}


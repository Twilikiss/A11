package com.elysia.demoApp.service;

/**
 *
 * @ClassName ApiService
 * @author   cxb
 * @date  17/5/2023 下午9:04
 */
public interface ApiService {
    /**
     * 获取百度智能云的授权token
     * @return
     */
    String getAccessToken();

    String getText2Image(String accessToken, String resolution, String style, Integer num, String text);

    String checkText2Image(String accessToken, String taskId);
}

package com.elysia.demoApp.service;

import com.elysia.demoApp.model.vo.FeedbackAiPaintingAdvancedVo;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName ModelService
 * @author   cxb
 * @date  8/7/2023 下午8:36
 */
public interface ModelService {
    void feedbackAiPaintingAdvanced(FeedbackAiPaintingAdvancedVo feedbackAiPaintingAdvancedVo, HttpServletRequest request);
}

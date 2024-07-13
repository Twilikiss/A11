package com.elysia.demoApp.service;

import com.elysia.demoApp.model.vo.Text2ImageAdvancedSaveVo;
import com.elysia.demoApp.model.vo.Text2ImageAdvancedVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 *
 * @ClassName ApiService
 * @author   cxb
 * @date  17/5/2023 下午9:04
 */
public interface AiPlatformApiService {
    /**
     * AI作画基础版接口
     * @return 作画结果json
     */
    Map<String, Object> getText2ImageBase(String resolution, String style, Integer num, String text, HttpServletRequest request);

    /**
     * 通过taskId查询作画详情
     * @param taskId 必要参数
     * @return 查询结果json
     */
    String checkText2Image(String taskId);

    Map<String, Object> getText2ImageAdvanced(Text2ImageAdvancedVo text2ImageAdvancedVo, HttpServletRequest request);

    String updateReferenceImage(MultipartFile multipartFile, HttpServletRequest request);

    String checkText2ImageAdvanced(String taskId);

    Map<String, Object> saveDataByUrl(Text2ImageAdvancedSaveVo text2ImageAdvancedSaveVo, String url, HttpServletRequest request);
}

package com.elysia.demoApp.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 用户关于文件的相关操作
 * @ClassName OcrService
 * @author   cxb
 * @date  24/5/2023 下午10:04
 */
public interface OcrService {

    Map<String, Object> image2text(MultipartFile multipartFile, HttpServletRequest request);

    Map<String, Object> image2textHandWriting(MultipartFile multipartFile, HttpServletRequest request);

    Map<String, Object> docConvert(MultipartFile multipartFile, String type, HttpServletRequest request);

    String checkDocConvert(String taskId);

    Map<String, Object> RemoveHandwriting(MultipartFile multipartFile, String type, HttpServletRequest request);
}

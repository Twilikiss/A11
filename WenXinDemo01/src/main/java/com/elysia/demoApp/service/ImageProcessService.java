package com.elysia.demoApp.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @ClassName ImageProcessService
 * @author   cxb
 * @date  12/7/2023 下午10:37
 */
public interface ImageProcessService {

    Map<String, Object> selfieAnime(MultipartFile multipartFile, String type, Integer maskId, HttpServletRequest request);

    Map<String, Object> styleTrans(MultipartFile multipartFile, String option, HttpServletRequest request);

    Map<String, Object> colourize(MultipartFile multipartFile, HttpServletRequest request);

    Map<String, Object> imageDefinitionEnhance(MultipartFile multipartFile, HttpServletRequest request);

    Map<String, Object> imageQualityEnhance(MultipartFile multipartFile, HttpServletRequest request);
}

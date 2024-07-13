package com.elysia.demoApp.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 关于图像识别相关的service
 * @author cxb
 */
public interface ImageClassifyService {
    Map<String, Object> imageClassifyUniversal(MultipartFile multipartFile, Integer baikeNum, HttpServletRequest request);

    Map<String, Object> imageClassifyDishes(MultipartFile multipartFile, Integer topNum, Integer baikeNum, HttpServletRequest request);

    Map<String, Object> imageClassifyAnimal(MultipartFile multipartFile, Integer topNum, Integer baikeNum, HttpServletRequest request);

    Map<String, Object> imageClassifyPlant(MultipartFile multipartFile, Integer baikeNum, HttpServletRequest request);

    Map<String, Object> imageClassifyIngredient(MultipartFile multipartFile, Integer topNum, HttpServletRequest request);

    Map<String, Object> imageClassifyLandmark(MultipartFile multipartFile, HttpServletRequest request);

    Map<String, Object> imageClassifyCurrency(MultipartFile multipartFile, HttpServletRequest request);

    Map<String, Object> imageClassifyCar(MultipartFile multipartFile, Integer topNum, Integer baikeNum, HttpServletRequest request);

    Map<String, Object> imageClassifyLogo(MultipartFile multipartFile, HttpServletRequest request);

    String getGraphicMatching(String prompt, String path);
}

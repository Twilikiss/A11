package com.elysia.demoApp.service;

import com.elysia.demoApp.model.vo.GalleryVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author cxb
 * @ClassName GalleryService
 * @date 17/7/2023 上午10:42
 */
public interface GalleryService {
    List<GalleryVo> getAllGallery();
    Boolean shareAiPainting(String id, HttpServletRequest request);
    List<GalleryVo> getSharedAiPaintingHistory(HttpServletRequest request);

    Boolean userDeleteSharedAiPaintingById(String id, HttpServletRequest request);
}

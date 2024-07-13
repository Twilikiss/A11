package com.elysia.demoApp.controller;

import com.elysia.demoApp.model.result.utils.Result;
import com.elysia.demoApp.model.vo.GalleryVo;
import com.elysia.demoApp.service.GalleryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

/**
 * @author cxb
 * @ClassName GalleryController
 * @date 17/7/2023 上午10:39
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/gallery")
@Api(value = "ai画廊（用户分享）相关的接口")
@CrossOrigin
public class GalleryController {
    @Resource
    private GalleryService galleryService;

    @ApiOperation("获取当前画廊")
    @GetMapping("/get/all")
    public Result getAllGallery() {
        List<GalleryVo> galleryVoList = galleryService.getAllGallery();
        return Result.ok(galleryVoList);
    }
}

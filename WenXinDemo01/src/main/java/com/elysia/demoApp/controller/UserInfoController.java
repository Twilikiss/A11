package com.elysia.demoApp.controller;

import com.elysia.demoApp.model.result.ImageClassifyResult;
import com.elysia.demoApp.model.result.ImageProcessResult;
import com.elysia.demoApp.model.result.OcrResult;
import com.elysia.demoApp.model.result.utils.Result;
import com.elysia.demoApp.model.result.Text2ImageResult;
import com.elysia.demoApp.model.vo.GalleryVo;
import com.elysia.demoApp.model.vo.UserCompleteInfoVo;
import com.elysia.demoApp.model.vo.UserInfoVo;
import com.elysia.demoApp.service.AccountService;
import com.elysia.demoApp.service.GalleryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author cxb
 * @ClassName UserInfoConroller
 * @date 2/6/2023 下午5:15
 */
@Slf4j
@RestController
@RequestMapping(value = "/user/info")
@Api(value = "用户相关信息的接口")
@CrossOrigin
public class UserInfoController {
    @Resource
    private AccountService accountService;
    @Resource
    private GalleryService galleryService;
    @ApiOperation("用户补充基本信息")
    @PostMapping("/completeInfo")
    public Result completeInfo(@RequestBody UserCompleteInfoVo userCompleteInfoVo,
                               HttpServletRequest request) {
        boolean isOk = accountService.completeInfo(userCompleteInfoVo, request);
        if (isOk) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }
    @ApiOperation("上传或修改头像")
    @PostMapping("/uploadIcon")
    public Result uploadIcon(@RequestParam MultipartFile multipartFile,
                             HttpServletRequest request) {
        String icon = accountService.uploadIcon(multipartFile, request);
        if (icon != null) {
            return Result.ok(icon);
        } else {
            return Result.fail();
        }
    }

    @ApiOperation("获取用户信息")
    @GetMapping("/getUserInfo")
    public Result getUserInfo(HttpServletRequest request) {
        UserInfoVo user = accountService.getUserInfo(request);
        if (user == null) {
            return Result.fail();
        } else {
            return Result.ok(user);
        }
    }

    @ApiOperation("退出用户账号")
    @GetMapping("/logout")
    public Result userLogout(HttpServletRequest request) {
        Boolean isOk = accountService.userLogout(request);
        if (isOk){
            return Result.ok("操作成功！");
        } else {
            return Result.fail();
        }
    }

    @ApiOperation("永久删除账户")
    @GetMapping("/deleteInfo")
    public Result deleteInfo(HttpServletRequest request) {
        Boolean isOk = accountService.deleteUserInfo(request);
        if (isOk) {
            return Result.ok("操作成功！");
        } else {
            return Result.fail();
        }
    }

    //*************下面是查询历史的相关的接口****************//

    @ApiOperation("查询基础版AI作画的相关历史")
    @GetMapping("/history/ai_painting/text2image/base")
    public Result getText2ImageBase(HttpServletRequest request) {
        List<Text2ImageResult> result = accountService.getHistoryWithText2ImageBase(request);
        return Result.ok(result);
    }

    @ApiOperation("查询高级版AI作画的相关历史")
    @GetMapping("/history/ai_painting/text2image/advanced")
    public Result getText2ImageAdvanced(HttpServletRequest request) {
        List<Text2ImageResult> result = accountService.getHistoryWithText2ImageAdvanced(request);
        return Result.ok(result);
    }

    @ApiOperation("查询所有的AI作画的相关历史")
    @GetMapping("/history/ai_painting/text2image/all")
    public Result getText2ImageAll(HttpServletRequest request) {
        List<Text2ImageResult> result = accountService.getHistoryWithText2ImageAll(request);
        return Result.ok(result);
    }

    @ApiOperation("查询图像识别接口")
    @GetMapping("/history/image_classify/all")
    public Result getImageClassifyAll(HttpServletRequest request) {
        List<ImageClassifyResult> result = accountService.getHistoryWithImageClassifyAll(request);
        return Result.ok(result);
    }

    @ApiOperation("查询OCR识别接口")
    @GetMapping("/history/ocr/all")
    public Result getOcrAll(HttpServletRequest request) {
        List<OcrResult> result = accountService.getHistoryWithOcrAll(request);
        return Result.ok(result);
    }

    @ApiOperation("查询图像特效与增强的相关历史")
    @GetMapping("/history/image_process/all")
    public Result getImageProcessAll(HttpServletRequest request) {
        List<ImageProcessResult> result = accountService.getHistoryWithImageProcessAll(request);
        return Result.ok(result);
    }

    //*************************以下是删除相关历史的接口***********************//

    @ApiOperation("通过id删除AI作画的历史（基础版和高级版通用）")
    @DeleteMapping("/delete/ai_painting/text2image/{id}")
    public Result deleteText2ImageById(@PathVariable("id")String id,
                                           HttpServletRequest request) {
        accountService.deleteText2ImageById(id, request);
        return Result.ok();
    }

    @ApiOperation("通过id删除图像识别的历史")
    @DeleteMapping("/delete/image_classify/{id}")
    public Result deleteImageClassifyById(@PathVariable("id")String id,
                                          HttpServletRequest request) {
        accountService.deleteImageClassifyById(id, request);
        return Result.ok();
    }

    @ApiOperation("通过id删除OCR识别的历史")
    @DeleteMapping("/delete/ocr/{id}")
    public Result deleteOcrById(@PathVariable("id")String id,
                                HttpServletRequest request) {
        accountService.deleteOcrById(id, request);
        return Result.ok();
    }

    @ApiOperation("通过id删除图像特效与增强的历史")
    @DeleteMapping("/delete/image_process/{id}")
    public Result deleteImageProcessById(@PathVariable("id")String id,
                                         HttpServletRequest request) {
        accountService.deleteImageProcessById(id, request);
        return Result.ok();
    }
    //**************************下面是用户分享的相关接口***************************//

    @ApiOperation("查询用户的所有分享的画作历史")
    @GetMapping("/share/history/ai_painting")
    public Result getSharedAiPaintingHistory(HttpServletRequest request) {
        List<GalleryVo> galleryVoList = galleryService.getSharedAiPaintingHistory(request);
        return Result.ok(galleryVoList);
    }

    @ApiOperation("用户分享ai画作")
    @PostMapping("/share/ai_painting")
    public Result shareAiPainting(@ApiParam("此处的id是通过查询作画历史记录的接口获取的id") @RequestParam String id,
                                  HttpServletRequest request) {
        Boolean isOk = galleryService.shareAiPainting(id, request);
        if (isOk) {
            return Result.ok();
        } else {
            return Result.fail("数据库错误，分享失败！");
        }
    }

    @ApiOperation("用户通过id删除已分享画作")
    @DeleteMapping("/delete/share/ai_painting/{id}")
    public Result deleteSharedAiPainting(@ApiParam("注意这里的id是通过查询用户所有分享的画作的历史时获取的id") @PathVariable("id")String id,
                                         HttpServletRequest request) {
        Boolean isOk = galleryService.userDeleteSharedAiPaintingById(id, request);
        if (isOk) {
            return Result.ok();
        } else {
            return Result.fail("删除失败，请刷新后重试");
        }
    }

}

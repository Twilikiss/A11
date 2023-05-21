package com.elysia.demoApp.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.elysia.demoApp.model.result.Result;
import com.elysia.demoApp.service.ApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author cxb
 * @ClassName ApiController
 * @date 17/5/2023 下午8:44
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/img")
@Api(value = "api测试")
@CrossOrigin
public class ApiController {
    @Resource
    private ApiService apiService;

    @ApiOperation("获取百度智能云的access_token")
    @GetMapping("/getAccessToken")
    public Result getAccessToken() {
        String accessToken = apiService.getAccessToken();
        return Result.ok(accessToken);
    }

    @ApiOperation("基于文心ERNIE-ViLG，通过关键词作画，返回任务id")
    @PostMapping("/toText2Image")
    public Result getText2Image(@ApiParam("鉴权token") @RequestParam String accessToken,
                             @ApiParam("图片分辨率，在”1024*1024、1024*1536、1536*1024“中选择") @RequestParam String resolution,
                             @ApiParam("风格，目前支持风格有：探索无限、古风、二次元、写实风格、浮世绘、low poly 、未来主义、像素风格、概念艺术、赛博朋克、洛丽塔风格、巴洛克风格、超现实主义、水彩画、蒸汽波艺术、油画、卡通画") @RequestParam String style,
                             @ApiParam("生成图片的数量") @RequestParam(required = false, defaultValue = "1") Integer num,
                             @ApiParam("关键词（200字以内）") @RequestParam String text) {
        String imageResult = apiService.getText2Image(accessToken, resolution, style, num, text);
        JSONObject jsonObject = JSON.parseObject(imageResult);
        if (jsonObject.getString("error_code") != null) {
            return Result.fail(jsonObject);
        }
        return Result.ok(jsonObject);
    }

    @ApiOperation("通过任务id查询任务详情，获取画作内容")
    @PostMapping("/getText2Image")
    public Result checkText2Image(@ApiParam("鉴权token") @RequestParam String accessToken,
                             @ApiParam("task_id") @RequestParam String taskId) {
        String checkResult = apiService.checkText2Image(accessToken, taskId);
        JSONObject jsonObject = JSON.parseObject(checkResult);
        return Result.ok(jsonObject);
    }
}

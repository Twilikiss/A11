package com.elysia.demoApp.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.elysia.demoApp.exception.WenXinException;
import com.elysia.demoApp.model.result.utils.Result;
import com.elysia.demoApp.model.result.utils.ResultCodeEnum;
import com.elysia.demoApp.model.vo.PromptGenerationVo;
import com.elysia.demoApp.service.NlpService;
import com.elysia.demoApp.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author cxb
 * @ClassName TextApiController
 * @date 13/6/2023 下午4:02
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/text")
@Api(value = "智能文本相关的api接口")
@CrossOrigin
public class TextApiController {
    @Resource
    private NlpService nlpService;

    @ApiOperation("文本纠错高级版")
    @PostMapping("/nlp/v2/text_correction")
    public Result textCorrectionV2(@ApiParam("需要纠正的文本内容") @RequestParam String text) {
        String textResult = nlpService.getTextErrorCorrection(text);
        JSONObject jsonObject = JSON.parseObject(textResult);
        if (jsonObject.getString("error_code") != null) {
            return Result.fail(jsonObject);
        }
        return Result.ok(jsonObject);
    }

    @ApiOperation("关键词提取接口")
    @PostMapping("/nlp/keyword")
    public Result getKeyWord(@ApiParam("输入文本") @RequestParam String text,
                             @ApiParam("关键词数量，非必需（默认为5）") @RequestParam(required = false, defaultValue = "5") int num,
                             HttpServletRequest request) {
        String result = nlpService.textKeyWord(text, num);
        JSONObject jsonObject = JSON.parseObject(result);
        if (jsonObject.getString("error_code") != null) {
            return Result.fail(jsonObject);
        }
        return Result.ok(jsonObject);
    }

    @ApiOperation("通过关键词智能写诗")
    @PostMapping("/nlp/poem")
    public Result makePoemByKeyword(@ApiParam("关键词（作为诗的主体，限制5字符数以内）") @RequestParam String text,
                                    @ApiParam("代表第几首诗，0代表第一首，每换一次加1即可，一定数量后会返回之前作诗的结果") @RequestParam(required = false, defaultValue = "0") Integer index,
                                    HttpServletRequest request) {
        if (StringUtils.isEmpty(text)) {
            throw new WenXinException(ResultCodeEnum.PARAM_ERROR);
        }
        String result = nlpService.createPoemByKeyword(text, index, request);
        JSONObject jsonObject = JSON.parseObject(result);
        if (jsonObject.getString("error_code") != null) {
            return Result.fail(jsonObject);
        }
        return Result.ok(jsonObject);
    }

    @ApiOperation("（AI作画高级版）通过关键词”智能生成prompt“")
    @PostMapping("/nlp/prompt_generation")
    public Result promptGeneration(@RequestBody PromptGenerationVo promptGenerationVo) {
        String result = nlpService.getPromptKeyword(promptGenerationVo);
        JSONObject jsonObject = JSON.parseObject(result);
        if (jsonObject.getString("error_code") != null) {
            return Result.fail(jsonObject);
        }
        return Result.ok(jsonObject);
    }
}

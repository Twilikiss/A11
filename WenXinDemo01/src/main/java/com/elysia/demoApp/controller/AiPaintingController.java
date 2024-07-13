package com.elysia.demoApp.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.elysia.demoApp.exception.WenXinException;
import com.elysia.demoApp.model.result.utils.Result;
import com.elysia.demoApp.model.result.utils.ResultCodeEnum;
import com.elysia.demoApp.model.vo.Text2ImageAdvancedSaveVo;
import com.elysia.demoApp.model.vo.Text2ImageAdvancedVo;
import com.elysia.demoApp.model.vo.Text2ImageBaseVo;
import com.elysia.demoApp.service.AiPlatformApiService;
import com.elysia.demoApp.service.ImageClassifyService;
import com.elysia.demoApp.service.NlpService;
import com.elysia.demoApp.service.OcrService;
import com.elysia.demoApp.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author cxb
 * @ClassName AiPaintingController
 * @date 1/7/2023 上午11:26
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/ai_painting")
@Api(value = "ai作画api接口")
@CrossOrigin
public class AiPaintingController {
    @Resource
    private AiPlatformApiService aiPlatformApiService;
    @Resource
    private OcrService ocrService;
    @Resource
    private NlpService nlpService;
    @Resource
    private ImageClassifyService imageClassifyService;

    @ApiOperation("【引导型】输入关键词、场景、主体风格和其他风格元素，基于文心ERNIE-ViLG作图(基础版)作画")
    @PostMapping("/base/text2image")
    public Result text2imageBase(@RequestBody Text2ImageBaseVo text2ImageBaseVo,
                                 HttpServletRequest request) {
        // 运行前先要判断所需参数的情况
        boolean isOk = StringUtils.checkText2ImageBaseVo(text2ImageBaseVo);
        if (!isOk) {
            throw new WenXinException(ResultCodeEnum.PARAM_ERROR);
        }
        // 取出对应的值
        String keyword = text2ImageBaseVo.getKeyword();
        List<String> otherStyle = text2ImageBaseVo.getOtherStyle();
        String style = text2ImageBaseVo.getStyle();
        String resolution = text2ImageBaseVo.getResolution();

        // 通过keyword, otherStyle生成对应prompt
        String prompt = StringUtils.getText2ImageBasePrompt(keyword, otherStyle);

        // 这里的num有默认值为1，默认只生成1张
        int num = text2ImageBaseVo.getNum() == null ? 5 : text2ImageBaseVo.getNum();
        Map<String, Object> imageResult = aiPlatformApiService.getText2ImageBase(resolution, style, num, prompt, request);

        JSONObject jsonObject = (JSONObject) imageResult.get("result_data");
        if (jsonObject.getString("error_code") != null) {
            return Result.fail(jsonObject);
        }
        return Result.ok(imageResult);
    }
//    @ApiOperation("【通用接口】关键词提取，并基于文心ERNIE-ViLG作图(基础版)(该接口为通用接口，作画效果较差)")
//    @PostMapping("/ai/text2image")
//    public Result text2image(@ApiParam("输入文本") @RequestParam String text,
//                             @ApiParam("图片分辨率，在”1024x1024、1024x1536、1536x1024“中选择(”x“请用”*“)") @RequestParam String resolution,
//                             @ApiParam("风格，支持：探索无限、古风、二次元、写实风格、浮世绘、low poly 、未来主义、像素风格、概念艺术、赛博朋克、洛丽塔风格、巴洛克风格、超现实主义、水彩画、蒸汽波艺术、油画、卡通画") @RequestParam String style,
//                             @ApiParam("生成图片的数量") @RequestParam(required = false, defaultValue = "1") Integer num,
//                             HttpServletRequest request) {
//        String nlpResult = nlpService.textKeyWord(text, request, 5);
//        JSONObject jsonObject01 = JSON.parseObject(nlpResult);
//        JSONArray results = jsonObject01.getJSONArray("results");
//        if (results == null) {
//            return Result.fail(nlpResult);
//        }
//        String words = "";
//        for (int i = 0; i < results.size(); i++) {
//            words += results.getJSONObject(i).getString("word") + "，";
//        }
//        words += "8k，大师级，高画质，壁纸，高精度渲染，画面精致";
//        String imageResult = aiPlatformApiService.getText2Image(resolution, style, num, words, request);
//        JSONObject jsonObject02 = JSON.parseObject(imageResult);
//        if (jsonObject02.getString("error_code") != null) {
//            return Result.fail(jsonObject02);
//        }
//        return Result.ok(jsonObject02);
//    }

    @ApiOperation("高级版上传参考图像接口(在绘画前要先上传获取到对于参考图的url)")
    @PostMapping("/advanced/text2image/update/reference_image")
    public Result updateReferenceImage(@RequestBody MultipartFile multipartFile,
                                       HttpServletRequest request) {
        String urlResult = aiPlatformApiService.updateReferenceImage(multipartFile, request);
        return Result.ok(urlResult);
    }

    @ApiOperation("【高级版】基于文心大模型，根据用户输入的文本，创作画作")
    @PostMapping("/advanced/text2image")
    public Result text2imageAdvanced(@RequestBody Text2ImageAdvancedVo text2ImageAdvancedVo,
                                     HttpServletRequest request) {
        Map<String, Object> result = aiPlatformApiService.getText2ImageAdvanced(text2ImageAdvancedVo, request);
        JSONObject jsonObject = (JSONObject) result.get("results");
        if (jsonObject.getString("error_code") != null) {
            return Result.fail(jsonObject);
        }
        return Result.ok(jsonObject);
    }

    @ApiOperation("（基础版接口）通过任务id查询任务详情，获取画作内容")
    @PostMapping("/getText2Image")
    public Result checkText2Image(@ApiParam("task_id") @RequestParam String taskId) {
        String checkResult = aiPlatformApiService.checkText2Image(taskId);
        JSONObject jsonObject = JSON.parseObject(checkResult);
        if (jsonObject.getString("error_code") != null) {
            return Result.fail(jsonObject);
        }
        return Result.ok(jsonObject);
    }

    @ApiOperation("（高级版接口）通过任务id查询任务详情，获取画作内容")
    @PostMapping("/getText2ImageAdvanced")
    public Result checkText2ImageAdvanced(@ApiParam("task_id") @RequestParam String taskId) {
        String checkResult = aiPlatformApiService.checkText2ImageAdvanced(taskId);
        JSONObject jsonObject = JSON.parseObject(checkResult);
        if (jsonObject.getString("error_code") != null) {
            return Result.fail(jsonObject);
        }
        return Result.ok(jsonObject);
    }

    @ApiOperation("（高级版）保存百度url的相关资源，并保存通过作画返回的相关数据")
    @PostMapping("/saveText2ImageAdvanced")
    public Result saveText2ImageAdvanced(@RequestBody Text2ImageAdvancedSaveVo text2ImageAdvancedSaveVo,
                                         HttpServletRequest request) {
        String url = text2ImageAdvancedSaveVo.getBaiduUrl();
        Map<String, Object> result = aiPlatformApiService.saveDataByUrl(text2ImageAdvancedSaveVo, url, request);
        String serverUrl = (String) result.get("serverUrl");
        if (serverUrl != null) {
            return Result.ok(result);
        } else {
            return Result.fail("保存失败！");
        }
    }


//    @ApiOperation("基于文心ERNIE-ViLG，通过关键词作画，返回任务id（后台测试使用！）")
//    @PostMapping("/toText2Image")
//    public Result getText2Image(@ApiParam("图片分辨率，在”1024*1024、1024*1536、1536*1024“中选择") @RequestParam String resolution,
//                                @ApiParam("风格，目前支持风格有：探索无限、古风、二次元、写实风格、浮世绘、low poly 、未来主义、像素风格、概念艺术、赛博朋克、洛丽塔风格、巴洛克风格、超现实主义、水彩画、蒸汽波艺术、油画、卡通画") @RequestParam String style,
//                                @ApiParam("生成图片的数量") @RequestParam(required = false, defaultValue = "1") Integer num,
//                                @ApiParam("关键词（200字以内）") @RequestParam String text,
//                                HttpServletRequest request) {
//        String imageResult = aiPlatformApiService.getText2ImageBase(resolution, style, num, text, request);
//        JSONObject jsonObject = JSON.parseObject(imageResult);
//        if (jsonObject.getString("error_code") != null) {
//            return Result.fail(jsonObject);
//        }
//        return Result.ok(jsonObject);
//    }
}

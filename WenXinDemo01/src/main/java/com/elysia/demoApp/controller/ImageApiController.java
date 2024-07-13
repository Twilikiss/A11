package com.elysia.demoApp.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.elysia.demoApp.exception.WenXinException;
import com.elysia.demoApp.model.result.utils.Result;
import com.elysia.demoApp.model.result.utils.ResultCodeEnum;
import com.elysia.demoApp.model.vo.Text2ImageAdvancedVo;
import com.elysia.demoApp.model.vo.Text2ImageBaseVo;
import com.elysia.demoApp.service.*;
import com.elysia.demoApp.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cxb
 * @ClassName ImageApiController
 * @date 17/5/2023 下午8:44
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/img")
@Api(value = "图像api接口")
@CrossOrigin
public class ImageApiController {
    @Resource
    private AiPlatformApiService aiPlatformApiService;
    @Resource
    private OcrService ocrService;
    @Resource
    private NlpService nlpService;
    @Resource
    private ImageClassifyService imageClassifyService;
    @Resource
    private ImageProcessService imageProcessService;

    //*********************OCR相关的接口**********************//

    @ApiOperation("文字通用识别高精度OCR接口（自动纠错版）")
    @PostMapping("/ocr/accurate_basic/auto_error_correction")
    public Result getAccurateBasicOcrAuto(@RequestParam MultipartFile multipartFile,
                                      HttpServletRequest request) {
        // 调用通用文字识别ocr接口获取文字识别结果（包含图片的url和识别出的文字）
        Map<String, Object> ocrResult = ocrService.image2text(multipartFile, request);
        JSONObject jsonObject = JSON.parseObject((String) ocrResult.get("result"));
        if (jsonObject.getString("error_code") != null) {
            return Result.fail(jsonObject);
        }
        JSONArray wordsResult = jsonObject.getJSONArray("words_result");
        if (wordsResult == null) {
            return Result.fail(ocrResult);
        }
        String words = "";
        for (int i = 0; i < wordsResult.size(); i++) {
            words += wordsResult.getJSONObject(i).getString("words");
        }
        // 调用文本纠错接口
        String textCorrectionResult = nlpService.getTextErrorCorrection(words);
        JSONObject finalJsonObject = JSON.parseObject(textCorrectionResult);
        Map<String, Object> result = new HashMap<>();
        result.put("url", ocrResult.get("url"));
        result.put("results", finalJsonObject);
        return Result.ok(result);
    }

    @ApiOperation("手写文字识别OCR接口（自动纠错版）")
    @PostMapping("/ocr/handwriting/auto_error_correction")
    public Result getHandWritingTextAuto(@RequestParam MultipartFile multipartFile,
                                     HttpServletRequest request) {
        // 调用手写ocr接口获取初步的文本，使用map的原因是希望能够将url也传递回前端
        Map<String, Object> ocrResult = ocrService.image2textHandWriting(multipartFile, request);
        JSONObject jsonObject = JSON.parseObject((String) ocrResult.get("result"));
        if (jsonObject.getString("error_code") != null) {
            return Result.fail(jsonObject);
        }
        JSONArray wordsResult = jsonObject.getJSONArray("words_result");
        if (wordsResult == null) {
            return Result.fail(ocrResult);
        }
        String words = "";
        for (int i = 0; i < wordsResult.size(); i++) {
            words += wordsResult.getJSONObject(i).getString("words");
        }
        // 将文本导入到文本纠错接口得到结果
        String textCorrectionResult = nlpService.getTextErrorCorrection(words);
        JSONObject finalJsonObject = JSON.parseObject(textCorrectionResult);
        Map<String, Object> result = new HashMap<>();
        result.put("url", ocrResult.get("url"));
        result.put("results", finalJsonObject);
        return Result.ok(result);
    }

    @ApiOperation("图文转换器，将pdf/图片转换为word/excel")
    @PostMapping("/ocr/doc_convert/update")
    public Result docConvert(@RequestParam MultipartFile multipartFile,
                             @ApiParam("上传文件的类型（image/pdf_file）") @RequestParam String type,
                             HttpServletRequest request) {
        Map<String, Object> docResult = ocrService.docConvert(multipartFile, type, request);
        JSONObject jsonObject = JSON.parseObject((String) docResult.get("result"));
        if (jsonObject.getString("error_code") != null) {
            return Result.fail(jsonObject);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("url", docResult.get("url"));
        result.put("results", jsonObject);
        return Result.ok(result);
    }

    @ApiOperation("图文转换结果查询")
    @PostMapping ("/ocr/doc_convert/get")
    public Result getDocConvert(@ApiParam("task_id") @RequestParam String taskId) {
        String result = ocrService.checkDocConvert(taskId);
        JSONObject jsonObject = JSON.parseObject(result);
        if (jsonObject.getString("error_code") != null) {
            return Result.fail(jsonObject);
        }
        return Result.ok(jsonObject);
    }

    @ApiOperation("文档去除手写（可上传图片和pdf文件，输出为base64格式）")
    @PostMapping ("/ocr/remove_handwriting")
    public Result removeHandwriting(@RequestParam MultipartFile multipartFile,
                                    @ApiParam("上传文件的类型（image/pdf_file）") @RequestParam String type,
                                    HttpServletRequest request) {
        Map<String, Object> removeResult = ocrService.RemoveHandwriting(multipartFile, type, request);
        JSONObject jsonObject = JSON.parseObject((String) removeResult.get("result"));
        if (jsonObject.getString("error_code") != null) {
            return Result.fail(jsonObject);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("url", removeResult.get("url"));
        result.put("results", jsonObject);
        return Result.ok(result);
    }

    @ApiOperation("通用文字识别（无纠错功能）")
    @PostMapping ("/ocr/accurate_basic")
    public Result getAccurateBasicOcr(@RequestParam MultipartFile multipartFile,
                                    @ApiParam("上传文件的类型（image/pdf_file）") @RequestParam String type,
                                    HttpServletRequest request) {
        // 调用通用文字识别ocr接口获取文字识别结果（包含图片的url和识别出的文字）
        Map<String, Object> ocrResult = ocrService.image2text(multipartFile, request);
        JSONObject jsonObject = JSON.parseObject((String) ocrResult.get("result"));
        if (jsonObject.getString("error_code") != null) {
            return Result.fail(jsonObject);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("url", ocrResult.get("url"));
        result.put("results", jsonObject);
        return Result.ok(result);
    }

    @ApiOperation("手写文字识别OCR接口（无纠错功能）")
    @PostMapping("/ocr/handwriting")
    public Result getHandWritingText(@RequestParam MultipartFile multipartFile,
                                         HttpServletRequest request) {
        // 调用手写ocr接口获取初步的文本，使用map的原因是希望能够将url也传递回前端
        Map<String, Object> ocrResult = ocrService.image2textHandWriting(multipartFile, request);
        JSONObject jsonObject = JSON.parseObject((String) ocrResult.get("result"));
        if (jsonObject.getString("error_code") != null) {
            return Result.fail(jsonObject);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("url", ocrResult.get("url"));
        result.put("results", jsonObject);
        return Result.ok(result);
    }

    //*************************下面是关于图像识别相关的接口******************//

    @ApiOperation("通用物品和场景识别")
    @PostMapping("/image_classify/universal")
    public Result ImageClassifyUniversal(@RequestParam MultipartFile multipartFile,
                                         @ApiParam("用于控制返回结果是否带有百科信息，输入正整数x代表输出x个,0代表不输出") @RequestParam(required = false, defaultValue = "0") Integer baikeNum,
                                         HttpServletRequest request) {
        Map<String, Object> classifyResult = imageClassifyService.imageClassifyUniversal(multipartFile, baikeNum, request);
        JSONObject jsonObject = JSON.parseObject((String) classifyResult.get("result"));
        if (jsonObject.getString("error_code") != null) {
            return Result.fail(jsonObject);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("url", classifyResult.get("url"));
        result.put("results", jsonObject);
        return Result.ok(result);
    }

    @ApiOperation("菜品识别接口（食说模块）")
    @PostMapping("/image_classify/dishes")
    public Result ImageClassifyDishes(@RequestParam MultipartFile multipartFile,
                                      @ApiParam("返回结果top n,默认5") @RequestParam(required = false, defaultValue = "5") Integer topNum,
                                      @ApiParam("用于控制返回结果是否带有百科信息，输入正整数x代表输出x个,默认不输出") @RequestParam(required = false, defaultValue = "0") Integer baikeNum,
                                      HttpServletRequest request) {
        Map<String, Object> classifyResult = imageClassifyService.imageClassifyDishes(multipartFile,topNum,baikeNum, request);
        JSONObject jsonObject = JSON.parseObject((String) classifyResult.get("result"));
        if (jsonObject.getString("error_code") != null) {
            return Result.fail(jsonObject);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("url", classifyResult.get("url"));
        result.put("results", jsonObject);
        return Result.ok(result);
    }

    @ApiOperation("动物识别")
    @PostMapping("/image_classify/animal")
    public Result ImageClassifyAnimal(@RequestParam MultipartFile multipartFile,
                                      @ApiParam("返回结果top n,默认5") @RequestParam(required = false, defaultValue = "5") Integer topNum,
                                      @ApiParam("用于控制返回结果是否带有百科信息，输入正整数x代表输出x个,默认不输出") @RequestParam(required = false, defaultValue = "0") Integer baikeNum,
                                      HttpServletRequest request) {
        Map<String, Object> classifyResult = imageClassifyService.imageClassifyAnimal(multipartFile,topNum,baikeNum, request);
        JSONObject jsonObject = JSON.parseObject((String) classifyResult.get("result"));
        if (jsonObject.getString("error_code") != null) {
            return Result.fail(jsonObject);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("url", classifyResult.get("url"));
        result.put("results", jsonObject);
        return Result.ok(result);
    }

    @ApiOperation("植物识别")
    @PostMapping("/image_classify/plant")
    public Result ImageClassifyPlant(@RequestParam MultipartFile multipartFile,
                                      @ApiParam("用于控制返回结果是否带有百科信息，输入正整数x代表输出x个,默认不输出") @RequestParam(required = false, defaultValue = "0") Integer baikeNum,
                                      HttpServletRequest request) {
        Map<String, Object> classifyResult = imageClassifyService.imageClassifyPlant(multipartFile,baikeNum, request);
        JSONObject jsonObject = JSON.parseObject((String) classifyResult.get("result"));
        if (jsonObject.getString("error_code") != null) {
            return Result.fail(jsonObject);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("url", classifyResult.get("url"));
        result.put("results", jsonObject);
        return Result.ok(result);
    }

    @ApiOperation("果蔬识别")
    @PostMapping("/image_classify/ingredient")
    public Result ImageClassifyIngredient(@RequestParam MultipartFile multipartFile,
                                          @ApiParam("返回结果top n,默认5") @RequestParam(required = false, defaultValue = "5") Integer topNum,
                                     HttpServletRequest request) {
        Map<String, Object> classifyResult = imageClassifyService.imageClassifyIngredient(multipartFile,topNum, request);
        JSONObject jsonObject = JSON.parseObject((String) classifyResult.get("result"));
        if (jsonObject.getString("error_code") != null) {
            return Result.fail(jsonObject);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("url", classifyResult.get("url"));
        result.put("results", jsonObject);
        return Result.ok(result);
    }

    @ApiOperation("地标识别")
    @PostMapping("/image_classify/landmark")
    public Result ImageClassifyLandmark(@RequestParam MultipartFile multipartFile,
                                          HttpServletRequest request) {
        Map<String, Object> classifyResult = imageClassifyService.imageClassifyLandmark(multipartFile, request);
        JSONObject jsonObject = JSON.parseObject((String) classifyResult.get("result"));
        if (jsonObject.getString("error_code") != null) {
            return Result.fail(jsonObject);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("url", classifyResult.get("url"));
        result.put("results", jsonObject);
        return Result.ok(result);
    }

    @ApiOperation("货币识别")
    @PostMapping("/image_classify/currency")
    public Result ImageClassifyCurrency(@RequestParam MultipartFile multipartFile,
                                        HttpServletRequest request) {
        Map<String, Object> classifyResult = imageClassifyService.imageClassifyCurrency(multipartFile, request);
        JSONObject jsonObject = JSON.parseObject((String) classifyResult.get("result"));
        if (jsonObject.getString("error_code") != null) {
            return Result.fail(jsonObject);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("url", classifyResult.get("url"));
        result.put("results", jsonObject);
        return Result.ok(result);
    }

    @ApiOperation("车辆识别")
    @PostMapping("/image_classify/car")
    public Result ImageClassifyCar(@RequestParam MultipartFile multipartFile,
                                      @ApiParam("返回结果top n,默认5") @RequestParam(required = false, defaultValue = "5") Integer topNum,
                                      @ApiParam("用于控制返回结果是否带有百科信息，输入正整数x代表输出x个,默认不输出") @RequestParam(required = false, defaultValue = "0") Integer baikeNum,
                                      HttpServletRequest request) {
        Map<String, Object> classifyResult = imageClassifyService.imageClassifyCar(multipartFile,topNum,baikeNum, request);
        JSONObject jsonObject = JSON.parseObject((String) classifyResult.get("result"));
        if (jsonObject.getString("error_code") != null) {
            return Result.fail(jsonObject);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("url", classifyResult.get("url"));
        result.put("results", jsonObject);
        return Result.ok(result);
    }

    @ApiOperation("logo识别")
    @PostMapping("/image_classify/logo")
    public Result ImageClassifyLogo(@RequestParam MultipartFile multipartFile,
                                        HttpServletRequest request) {
        Map<String, Object> classifyResult = imageClassifyService.imageClassifyLogo(multipartFile, request);
        JSONObject jsonObject = JSON.parseObject((String) classifyResult.get("result"));
        if (jsonObject.getString("error_code") != null) {
            return Result.fail(jsonObject);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("url", classifyResult.get("url"));
        result.put("results", jsonObject);
        return Result.ok(result);
    }

    //************************图像处理的相关接口********************//
    @ApiOperation("人像动漫化")
    @PostMapping("/image_process/selfie_anime")
    public Result selfieAnime(@RequestParam MultipartFile multipartFile,
                              @ApiParam("是否戴口罩，anime或者anime_mask") @RequestParam String type,
                              @ApiParam("口罩种类（1到8），为0时表示不戴口罩") @RequestParam(required = false,defaultValue = "0") Integer maskId,
                              HttpServletRequest request) {
        Map<String ,Object> result = imageProcessService.selfieAnime(multipartFile, type, maskId, request);
        if (result == null) {
            return Result.fail();
        } else {
            return Result.ok(result);
        }
    }

    @ApiOperation("图像风格转换")
    @PostMapping("/image_process/style_trans")
    public Result styleTrans(@RequestParam MultipartFile multipartFile,
                             @ApiParam("风格选择，详见说明文档") @RequestParam String option,
                             HttpServletRequest request) {
        Map<String, Object> result = imageProcessService.styleTrans(multipartFile, option ,request);
        if (result == null) {
            return Result.fail();
        } else {
            return Result.ok(result);
        }
    }

    @ApiOperation("黑白照片上色")
    @PostMapping("/image_process/colourize")
    public Result colourize(@RequestParam MultipartFile multipartFile,
                            HttpServletRequest request) {
        Map<String, Object> result = imageProcessService.colourize(multipartFile, request);
        if (result == null) {
            return Result.fail();
        } else {
            return Result.ok(result);
        }
    }

    @ApiOperation("图像清晰度增强")
    @PostMapping("/image_process/image_definition_enhance")
    public Result imageDefinitionEnhance(@RequestParam MultipartFile multipartFile,
                                         HttpServletRequest request) {
        Map<String, Object> result = imageProcessService.imageDefinitionEnhance(multipartFile, request);
        if (result == null) {
            return Result.fail();
        } else {
            return Result.ok(result);
        }
    }

    @ApiOperation("图像无损放大")
    @PostMapping("/image_process/image_quality_enhance")
    public Result imageQualityEnhance(@RequestParam MultipartFile multipartFile,
                                      HttpServletRequest request) {
        Map<String, Object> result = imageProcessService.imageQualityEnhance(multipartFile, request);
        if (result == null) {
            return Result.fail();
        } else {
            return Result.ok(result);
        }
    }
}

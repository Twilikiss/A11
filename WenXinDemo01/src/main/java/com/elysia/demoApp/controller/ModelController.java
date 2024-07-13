package com.elysia.demoApp.controller;

import com.elysia.demoApp.model.result.utils.Result;
import com.elysia.demoApp.model.vo.FeedbackAiPaintingAdvancedVo;
import com.elysia.demoApp.service.ModelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author cxb
 * @ClassName ModelController
 * @date 3/7/2023 下午10:51
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/model")
@Api(value = "模型评估和调试接口")
@CrossOrigin
public class ModelController {
    @Resource
    private ModelService modelService;

    //TODO 模型反馈的相关接口
    @ApiOperation("用户对AI作画生成的效果的反馈")
    @PostMapping("/feedback/ai_painting/advanced")
    public Result feedbackAiPaintingAdvanced(@RequestBody FeedbackAiPaintingAdvancedVo feedbackAiPaintingAdvancedVo,
                                             HttpServletRequest request) {
        modelService.feedbackAiPaintingAdvanced(feedbackAiPaintingAdvancedVo, request);
        return Result.ok();
    }

}

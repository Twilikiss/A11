package com.elysia.demoApp.controller;

import com.alibaba.fastjson.JSONObject;
import com.elysia.demoApp.model.result.utils.Result;
import com.elysia.demoApp.model.vo.WenXinChatVo;
import com.elysia.demoApp.model.vo.WenXinHistoryVo;
import com.elysia.demoApp.service.WenXinService;
import com.elysia.demoApp.utils.AccessTokenUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 文心一言的相关接口
 * @author cxb
 * @ClassName WenXinController
 * @date 21/7/2023 上午10:59
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/wenxin")
@Api(value = "文心大模型相关的接口")
@CrossOrigin
public class WenXinController {
    @Resource
    private WenXinService wenXinService;
    @Resource
    private AccessTokenUtils accessTokenUtils;
// TODO 基于文心大模型的提示词（prompt）推荐 待完成
    @ApiOperation("获取历史数据")
    @GetMapping("/history/get_all")
    public Result getAllHistory(HttpServletRequest request) {
        List<WenXinHistoryVo> wenXinHistoryVoList = wenXinService.getAllHistory(request);
        return Result.ok(wenXinHistoryVoList);
    }

    @ApiOperation("通过id获取对话数据（有传id则为获取历史数据，不传id为创建对话）")
    @PostMapping("/history/get_chat")
    public Result getChatHistory(@ApiParam("输入的对话talkId") @RequestParam(required = false, defaultValue = "-1") String talkId,
                                 HttpServletRequest request) {
        Map<String, Object> result = wenXinService.getChatHistory(talkId, request);
        return Result.ok(result);
    }
    @ApiOperation("基于文心大模型进行智能推荐对话")
    @PostMapping("/prompt/chat/completions")
    public Result PromptChatCompletions(@RequestBody WenXinChatVo wenXinChatVo,
                                        HttpServletRequest request) {
        Map<String, Object> result = wenXinService.getPromptWithChatByWenXin(wenXinChatVo, request);
        if (result == null) {
            return Result.fail("内部错误，请联系工程师");
        }
        return Result.ok(result);
    }
    @ApiOperation("根据智能对话的内容输出prompt")
    @PostMapping("/prompt/chat/get_prompt")
    public Result ChatGetPrompt(@ApiParam("对话id") @RequestParam String talkId,
                                HttpServletRequest request) {
       String result = wenXinService.chatGetPrompt(talkId, request);
       if (result == null) {
           return Result.fail("获取prompt失败！请联系工程师");
       } else {
           return Result.ok(result);
       }
    }
}

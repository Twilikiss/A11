package com.elysia.demoApp.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author cxb
 * @ClassName WenXinChatVo
 * @date 21/7/2023 下午11:06
 */
@Data
@ApiModel(description="基于文心大模型的智能对话数据的Vo类")
public class WenXinChatVo {
    @ApiModelProperty(value = "会话id")
    private String talkId;
    @ApiModelProperty(value = "用户输入的文本内容")
    private String content;
}

package com.elysia.demoApp.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

/**
 * 高级版AI作画模型反馈的Vo类
 * @author cxb
 * @ClassName FeedbackAiPaintingAdvancedVo
 * @date 8/7/2023 下午9:02
 */
@Data
@ApiModel(description="高级版AI作画模型反馈")
public class FeedbackAiPaintingAdvancedVo {
    @ApiModelProperty(value = "评价（0：赞，1：踩）")
    private String evaluation;
    @ApiModelProperty(value = "prompt的主体描述，type为wenxin时可不传")
    private String subjectDescription;
    @ApiModelProperty(value = "prompt")
    private String prompt;
    @ApiModelProperty(value = "用户反馈的内容")
    private String review;
    @ApiModelProperty(value = "是否使用”智能生成prompt,0：没有，1：有")
    private String isUsePromptGen;
    @ApiModelProperty(value = "生成的画作（百度url）")
    private String imageUrlBd;
    @ApiModelProperty("反馈的类型（默认advanced， 可以不传，如果是通过文心一言引导获取的prompt，必须传”wenxin“）")
    private String type;
}

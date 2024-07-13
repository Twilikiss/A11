package com.elysia.demoApp.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author cxb
 * @ClassName PromptGenerationVo
 * @date 11/7/2023 上午11:18
 */
@Data
@ApiModel(description="prompt生成的Vo类")
public class PromptGenerationVo {
    @ApiModelProperty(value = "输入的文本")
    private String text;
    @ApiModelProperty(value = "生成文本的字符长度（默认是64）,范围是0-128")
    private Integer maxGenLen;
}

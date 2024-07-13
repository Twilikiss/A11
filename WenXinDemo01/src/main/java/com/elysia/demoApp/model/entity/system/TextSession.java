package com.elysia.demoApp.model.entity.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 对话模板类，用于文心千帆
 * @author cxb
 * @ClassName TextSession
 * @date 21/7/2023 上午10:11
 */
@Data
@ApiModel(description = "对话模板")
public class TextSession {
    @ApiModelProperty("角色")
    private String role;
    @ApiModelProperty("内容")
    private String content;
}

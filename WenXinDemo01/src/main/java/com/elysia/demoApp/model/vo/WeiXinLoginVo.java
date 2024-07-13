package com.elysia.demoApp.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author cxb
 * @ClassName WeiXinLoginVo
 * @date 30/5/2023 下午9:44
 */
@Data
@ApiModel(description="微信用户信息对象，用于模拟微信登录")
public class WeiXinLoginVo {
    @ApiModelProperty(value = "昵称")
    private String nickName;
}

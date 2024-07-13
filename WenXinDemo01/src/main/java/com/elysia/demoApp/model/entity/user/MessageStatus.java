package com.elysia.demoApp.model.entity.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 关于用户验证码的状态
 * @author cxb
 * @ClassName MessageStatus
 * @date 21/4/2023 下午8:34
 */
@Data
@ApiModel(description = "短信详情")
public class MessageStatus {
    @ApiModelProperty(value = "用户手机号码")
    private String phone;
    @ApiModelProperty(value = "短信发送状态，true为可发送;false为不可发送")
    private Boolean status;
    @ApiModelProperty(value = "距离下一次可发送验证码的时间,-1为当前手机可发送")
    private Long waitingTime;
}

package com.elysia.demoApp.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author cxb
 * @ClassName UserInfoVo
 * @date 29/5/2023 下午6:27
 */
@Data
@ApiModel(description="用户信息对象，用于补全和修改用户信息")
public class UserInfoVo {
    @ApiModelProperty(value = "头像（url）")
    private String userIcon;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "性别：0为男，1为女")
    private Integer gender;

    @ApiModelProperty(value = "出生日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birth;
}

package com.elysia.demoApp.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.elysia.demoApp.model.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author cxb
 * @ClassName User
 * @date 12/4/2023 上午11:06
 */
@Data
@ApiModel(description = "User类")
@TableName("user")
public class User extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "昵称")
    @TableField("nick_name")
    private String nickName;

    @ApiModelProperty(value = "姓名")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "手机号")
    @TableField("phone")
    private String phone;

    @ApiModelProperty(value = "密码")
    @TableField("password")
    private String password;

    @ApiModelProperty(value = "状态：0为正常，1为锁定")
    @TableField("status")
    private Integer status;
}

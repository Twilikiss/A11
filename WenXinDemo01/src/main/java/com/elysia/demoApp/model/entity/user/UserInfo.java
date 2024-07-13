package com.elysia.demoApp.model.entity.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.elysia.demoApp.model.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author cxb
 * @ClassName UserInfo
 * @date 12/4/2023 上午11:06
 */
@Data
@ApiModel(description = "User类")
@TableName("user_info")
public class UserInfo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识码")
    @TableField("UID")
    private String uid;

    @ApiModelProperty(value = "头像（地址）")
    @TableField("user_icon")
    private String userIcon;
    @ApiModelProperty(value = "昵称")
    @TableField("nick_name")
    private String nickName;

    @ApiModelProperty(value = "手机号")
    @TableField("phone")
    private String phone;

    @ApiModelProperty(value = "性别：0为男，1为女")
    @TableField("gender")
    private Integer gender;

    @ApiModelProperty(value = "出生日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField("birth")
    private Date birth;

    @ApiModelProperty(value = "用户类型：weixin或者baidu二选一，默认weixin")
    @TableField("user_type")
    private String userType;

    @ApiModelProperty(value = "状态：0为正常，1为锁定")
    @TableField("status")
    private Integer status;
}

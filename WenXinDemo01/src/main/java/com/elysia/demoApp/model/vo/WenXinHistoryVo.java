package com.elysia.demoApp.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author cxb
 * @ClassName WenXinHistoryVo
 * @date 22/7/2023 下午5:05
 */
@Data
@ApiModel(description = "用户全部对话数据")
public class WenXinHistoryVo {
    @ApiModelProperty(value = "会话id")
    private String talkId;
    @ApiModelProperty(value = "最后一句话")
    private String lastWord;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}

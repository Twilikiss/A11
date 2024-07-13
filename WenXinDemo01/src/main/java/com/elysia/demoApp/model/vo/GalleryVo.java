package com.elysia.demoApp.model.vo;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Date;

/**
 * @author cxb
 * @ClassName GalleryVo
 * @date 17/7/2023 上午11:08
 */
@Data
@ApiModel(description="Gallery的Vo类")
public class GalleryVo {
    @ApiModelProperty("画作作者")
    private String author;
    @ApiModelProperty("画作url")
    private String imageUrl;
    @ApiModelProperty("画作对应的输入参数")
    private JSONObject jsonObject;
    @ApiModelProperty("分享时间")
    private Date shareTime;
    @ApiModelProperty("画作的类型（基础版：base|高级版：advanced）")
    private String type;
}

package com.elysia.demoApp.model.entity;

import com.elysia.demoApp.model.base.BaseMongoEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 关于从前端接收相关参数的实体类
 * @author cxb
 * @ClassName Text2ImageVO
 * @date 17/5/2023 下午8:49
 */
@Data
@ApiModel(description = "文字生成图片的相关参数的实体类")
@Document("Text2ImageTest")
public class Text2ImageVO extends BaseMongoEntity {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "access_token（用户数据在的唯一标识）")
    @Indexed(unique = true) //唯一索引
    private String accessToken;
    @ApiModelProperty(value = "分辨率（限制1024*1024、1024*1536、1536*1024）")
    private String resolution;
    @ApiModelProperty(value = "风格（探索无限、古风、二次元、写实风格、浮世绘、low poly 、未来主义、像素风格、概念艺术、赛博朋克、洛丽塔风格、巴洛克风格、超现实主义、水彩画、蒸汽波艺术、油画、卡通画）")
    private String style;
    @ApiModelProperty("生成数量")
    private Integer num;
    @ApiModelProperty("关键词（限制200字）")
    private String text;
}

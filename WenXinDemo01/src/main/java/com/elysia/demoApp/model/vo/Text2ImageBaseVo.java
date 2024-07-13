package com.elysia.demoApp.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

/**
 * 关于从前端接收相关参数的Vo类(基础版；关键词)
 *
 * @author cxb
 * @ClassName Text2ImageBaseVo
 * @date 17/5/2023 下午8:49
 */
@Data
@ApiModel(description = "文字生成图片的相关参数的Vo类（基础版）")
public class Text2ImageBaseVo {
    @ApiModelProperty(value = "关键词")
    private String keyword;
    @ApiModelProperty(value = "作画主体风格（探索无限、古风、二次元、写实风格、浮世绘、low poly 、未来主义、像素风格、概念艺术、赛博朋克、洛丽塔风格、巴洛克风格、超现实主义、水彩画、蒸汽波艺术、油画、卡通画）")
    private String style;
    @ApiModelProperty(value = "其他风格元素")
    private List<String> otherStyle;
    @ApiModelProperty(value = "分辨率")
    private String resolution;
    @ApiModelProperty("生成数量")
    private Integer num;
}

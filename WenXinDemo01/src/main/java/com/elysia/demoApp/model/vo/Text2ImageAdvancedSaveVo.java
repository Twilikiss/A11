package com.elysia.demoApp.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 保存高级作画的相关数据
 * @author cxb
 * @ClassName Text2ImageAdvancedSaveVo
 * @date 11/7/2023 下午5:40
 */
@Data
@ApiModel(description = "保存文字生成图片的相关参数的Vo类（高级版）")
public class Text2ImageAdvancedSaveVo {
    @ApiModelProperty(value = "生图的文本描述，仅支持中文、日常标点符号。不支持英文，特殊符号，限制 200 字")
    private String prompt;
    @ApiModelProperty(value = "prompt的主体描述")
    private String subjectDescription;
    @ApiModelProperty(value = "模型版本")
    private String version;
    @ApiModelProperty(value = "图片宽度，参数请参考技术文档")
    private Integer width;
    @ApiModelProperty(value = "图片高度，参数请参考技术文档")
    private Integer height;
    @ApiModelProperty(value = "生成的图片数量")
    private Integer imageNum;
    @ApiModelProperty(value = "参考图")
    private String imageUrl;
    @ApiModelProperty(value = "参考图影响因子，支持1~10，数值越大参考图影响越大（仅当上传参考图时需要传递）")
    private Integer changeDegree;
    @ApiModelProperty(value = "百度返回的AI作画的结果（url）")
    private String baiduUrl;

}

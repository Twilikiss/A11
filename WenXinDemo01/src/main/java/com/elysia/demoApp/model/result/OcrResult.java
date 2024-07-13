package com.elysia.demoApp.model.result;

import com.alibaba.fastjson.JSONObject;
import com.elysia.demoApp.model.base.BaseMongoEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 通用的Ocr返回结果的实体类
 * @author cxb
 * @ClassName OcrResult
 * @date 29/5/2023 下午10:46
 */
@Data
@ApiModel(description = "ocr详情")
@Document("OcrResult")
public class OcrResult extends BaseMongoEntity {
    @ApiModelProperty("用户唯一标识码")
    @Indexed(unique = true) //唯一索引
    private String uid;
    @ApiModelProperty("输入图像（url）")
    private String input;
    @ApiModelProperty("输出结果")
    private JSONObject output;

    @ApiModelProperty("ocr类型")
    private String type;
}

package com.elysia.demoApp.model.result;

import com.alibaba.fastjson.JSONObject;
import com.elysia.demoApp.model.base.BaseMongoEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author cxb
 * @ClassName ImageClassifyResult
 * @date 31/5/2023 下午8:03
 */
@Data
@ApiModel(description = "图像识别详情")
@Document("ImageClassifyResult")
public class ImageClassifyResult extends BaseMongoEntity {
    @ApiModelProperty("用户唯一标识码")
    @Indexed(unique = true) //唯一索引
    private String uid;
    @ApiModelProperty("输入图像（url）")
    private String input;
    @ApiModelProperty("输出结果")
    private JSONObject output;

    @ApiModelProperty("图像识别类型")
    private String type;
}

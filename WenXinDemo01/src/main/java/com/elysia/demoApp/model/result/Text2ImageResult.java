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
 * @ClassName Text2ImageResult
 * @date 18/5/2023 下午3:54
 */
@Data
@ApiModel(description = "文字生成图片的相关结果的实体类")
@Document("Text2ImageTestResult")
public class Text2ImageResult extends BaseMongoEntity {
    @ApiModelProperty("用户唯一标识码")
    @Indexed(unique = true) //唯一索引
    private String uid;
    @ApiModelProperty("输入关键词")
    private JSONObject input;
    @ApiModelProperty("输出结果")
    private JSONObject output;
    @ApiModelProperty("接口类型：基础版、高级版")
    private String type;
}

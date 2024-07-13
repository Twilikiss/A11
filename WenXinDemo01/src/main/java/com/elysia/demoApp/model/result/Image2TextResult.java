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
 * @ClassName Image2TextResult
 * @date 3/7/2023 下午9:41
 */
@Data
@ApiModel(description = "由图生成对应的文本")
@Document("Image2TextResult")
public class Image2TextResult extends BaseMongoEntity {
    @ApiModelProperty("用户唯一标识码")
    @Indexed(unique = true) //唯一索引
    private String uid;
    @ApiModelProperty("输入图像（url）")
    private String input;
    @ApiModelProperty("输出结果")
    private JSONObject output;
    @ApiModelProperty("生成文本类型")
    private String type;
}

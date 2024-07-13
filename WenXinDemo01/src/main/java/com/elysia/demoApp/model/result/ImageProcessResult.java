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
 * @ClassName ImageProcessResult
 * @date 13/7/2023 上午12:22
 */
@Data
@ApiModel(description = "图像特效与增强的数据")
@Document("ImageProcessResult")
public class ImageProcessResult extends BaseMongoEntity {
    @ApiModelProperty("用户唯一标识码")
    @Indexed(unique = true) //唯一索引
    private String uid;
    @ApiModelProperty("输入相关参数")
    private JSONObject input;
    @ApiModelProperty("输出的图像结果")
    private String base64;
    @ApiModelProperty("图像操作的类型，不同的操作类型他们的input是不一样的，但是output是一样的")
    private String processType;
}

package com.elysia.demoApp.model.entity;

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
    @ApiModelProperty("请求唯一标识码")
    @Indexed(unique = true) //唯一索引
    private String logId;
    @ApiModelProperty("返回结果对象，task_id,获取图片的依据")
    private String[] data;
}

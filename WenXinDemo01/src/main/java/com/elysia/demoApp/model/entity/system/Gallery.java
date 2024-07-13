package com.elysia.demoApp.model.entity.system;

import com.alibaba.fastjson.JSONObject;
import com.elysia.demoApp.model.base.BaseEntity;
import com.elysia.demoApp.model.base.BaseMongoEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 画廊
 * @author cxb
 * @ClassName Gallery
 * @date 14/7/2023 上午11:44
 */
@Data
@ApiModel(description = "“画廊”的实体类")
@Document("Gallery")
public class Gallery extends BaseMongoEntity {
    @ApiModelProperty("用户唯一标识码")
    @Indexed(unique = true) //唯一索引
    private String uid;
    @ApiModelProperty("作者")
    private String author;
    @ApiModelProperty("画作url")
    private String imageUrl;
    @ApiModelProperty("画作对应的输入参数")
    private JSONObject jsonObject;
    @ApiModelProperty("画作的类型（基础版：base|高级版：advanced）")
    private String type;
}

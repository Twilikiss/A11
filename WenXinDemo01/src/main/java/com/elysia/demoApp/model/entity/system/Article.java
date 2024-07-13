package com.elysia.demoApp.model.entity.system;

import com.elysia.demoApp.model.base.BaseMongoEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author cxb
 * @ClassName Article
 * @date 12/7/2023 上午10:06
 */
@Data
@ApiModel(description = "文章（趣闻）")
@Document("Article")
public class Article extends BaseMongoEntity {
    @ApiModelProperty(value = "标题")
    private String title;
    @ApiModelProperty(value = "文章内容")
    private String content;
    @ApiModelProperty(value = "点赞数量")
    private Long thumbUp;
    @ApiModelProperty(value = "访客数量")
    private Long visits;
    @ApiModelProperty("文章类型")
    private String type;
}

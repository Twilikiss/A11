package com.elysia.demoApp.model.entity.system;

import com.alibaba.fastjson.JSONObject;
import com.elysia.demoApp.model.base.BaseMongoEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author cxb
 * @ClassName WenXinHistory
 * @date 21/7/2023 下午5:02
 */
@Data
@ApiModel(description = "文心一言对话历史保存")
@Document("WenXinHistory")
public class WenXinHistory extends BaseMongoEntity {
    @ApiModelProperty("用户唯一标识码")
    @Indexed(unique = true) //唯一索引
    private String uid;
    @ApiModelProperty("对话数据（json）")
    private JSONObject sessionJson;
}

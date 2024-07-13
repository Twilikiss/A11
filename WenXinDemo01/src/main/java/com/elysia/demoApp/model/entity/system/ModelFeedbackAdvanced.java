package com.elysia.demoApp.model.entity.system;

import com.elysia.demoApp.model.base.BaseMongoEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

/**
 * @author cxb
 * @ClassName ModelFeedbackAdvanced
 * @date 9/7/2023 下午3:49
 */
@Data
@ApiModel(description = "高级版模型反馈的实体类")
@Document("ModelFeedbackAdvanced")
public class ModelFeedbackAdvanced extends BaseMongoEntity {
    @ApiModelProperty("用户唯一标识码")
    @Indexed(unique = true) //唯一索引
    private String uid;
    @ApiModelProperty("用户评价（0：赞，1：踩）")
    private String evaluation;
    @ApiModelProperty(value = "主体描述描述关键词")
    private List<String> subjectKeyword;
    @ApiModelProperty(value = "prompt的主体描述")
    private String subjectDescription;
    @ApiModelProperty(value = "prompt")
    private String prompt;
    @ApiModelProperty(value = "用户反馈的内容")
    private String review;
    @ApiModelProperty(value = "反馈情感倾向（0：消极、1：中性、2：积极）")
    private String sentiment;
    @ApiModelProperty(value = "评论观点tag")
    private List<String> commentTag;
    @ApiModelProperty(value = "是否使用”智能生成prompt,0：没有，1：有")
    private String isUsePromptGen;
    @ApiModelProperty(value = "图文匹配度")
    private String matchingDegree;
    @ApiModelProperty("生成的画作（服务器Url）")
    private String serverUrl;
    @ApiModelProperty("反馈的类型（默认advanced， 可以不传，如果是通过文心一言引导获取的prompt，必须传”wenxin“）")
    private String type;
}

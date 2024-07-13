package com.elysia.demoApp.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.elysia.demoApp.Repository.ModelRepository;
import com.elysia.demoApp.exception.WenXinException;
import com.elysia.demoApp.model.entity.system.ModelFeedbackAdvanced;
import com.elysia.demoApp.model.vo.FeedbackAiPaintingAdvancedVo;
import com.elysia.demoApp.service.ImageClassifyService;
import com.elysia.demoApp.service.ModelService;
import com.elysia.demoApp.service.NlpService;
import com.elysia.demoApp.utils.AccessTokenUtils;
import com.elysia.demoApp.utils.FileUtils;
import com.elysia.demoApp.utils.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * model相关的service类
 * @author cxb
 * @ClassName ModelServiceImpl
 * @date 8/7/2023 下午8:36
 */
@Service
public class ModelServiceImpl implements ModelService {
    @Resource
    private AccessTokenUtils accessTokenUtils;
    @Resource
    private NlpService nlpService;
    @Resource
    private ImageClassifyService imageClassifyService;
    @Resource
    private ModelRepository modelRepository;
    @Override
    public void feedbackAiPaintingAdvanced(FeedbackAiPaintingAdvancedVo feedbackAiPaintingAdvancedVo, HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        String uid = accessTokenUtils.getUid(token);

        ModelFeedbackAdvanced modelFeedbackAdvanced = new ModelFeedbackAdvanced();

        String evaluation = feedbackAiPaintingAdvancedVo.getEvaluation();
        String subjectDescription = feedbackAiPaintingAdvancedVo.getSubjectDescription();
        String type = feedbackAiPaintingAdvancedVo.getType();
        if (!"wenxin".equals(type)) {
            // 提取关键词
            String result01 = nlpService.textKeyWord(subjectDescription, 2);
            JSONObject jsonObject01 = JSON.parseObject(result01);
            JSONArray results01 = jsonObject01.getJSONArray("results");
            if (results01 == null) {
                throw new WenXinException("keyword_error", 500);
            }
            List<String> tempList01 = new ArrayList<>();
            for (int i = 0; i < results01.size(); i++) {
                tempList01.add(results01.getJSONObject(i).getString("word"));
            }
            modelFeedbackAdvanced.setSubjectKeyword(tempList01);
        }

        String prompt = feedbackAiPaintingAdvancedVo.getPrompt();
        String review = feedbackAiPaintingAdvancedVo.getReview();
        String isUsePromptGen = feedbackAiPaintingAdvancedVo.getIsUsePromptGen();
        if (!StringUtils.isEmpty(review)){
            // 提取感情倾向
            String result02 = nlpService.sentimentClassify(review);
            JSONObject jsonObject02 = JSON.parseObject(result02);
            String sentiment = jsonObject02.getJSONArray("items").getJSONObject(0).getString("sentiment");
            if (sentiment == null) {
                throw new WenXinException("sentiment_error", 500);
            }
            // 获取评论tag
            String result03 = nlpService.getCommentTag(review);
            JSONObject jsonObject03 = JSON.parseObject(result03);
            JSONArray results02 = jsonObject03.getJSONArray("items");
            if (results02 == null) {
                throw new WenXinException("commentType_error", 500);
            }
            List<String> tempList02 = new ArrayList<>();
            for (int i = 0; i < results02.size(); i++) {
                JSONObject jsonObject = results02.getJSONObject(i);
                tempList02.add(jsonObject.getString("prop") + jsonObject.getString("adj"));
            }
            modelFeedbackAdvanced.setSentiment(sentiment);
            modelFeedbackAdvanced.setCommentTag(tempList02);
        }
        String urlBd = feedbackAiPaintingAdvancedVo.getImageUrlBd();
        String serverUrl = FileUtils.downloadImageByUrl(urlBd,"/data/wenxin/Model");
        String filename = serverUrl.substring(39);
        String path = "/data/wenxin/Model/" + filename;
        String matchResult = imageClassifyService.getGraphicMatching(prompt,path);
        JSONObject jsonObject03 = JSON.parseObject(matchResult);
        String score = jsonObject03.getString("score");
        if (score == null) {
            throw new WenXinException("matchType_error", 500);
        }
        modelFeedbackAdvanced.setServerUrl(serverUrl);
        modelFeedbackAdvanced.setCreateTime(new Date());
        modelFeedbackAdvanced.setUpdateTime(new Date());
        modelFeedbackAdvanced.setIsDeleted(0);
        modelFeedbackAdvanced.setUid(uid);
        modelFeedbackAdvanced.setEvaluation(evaluation);
        modelFeedbackAdvanced.setPrompt(prompt);
        modelFeedbackAdvanced.setSubjectDescription(subjectDescription);
        modelFeedbackAdvanced.setReview(review);
        modelFeedbackAdvanced.setIsUsePromptGen(isUsePromptGen);
        modelFeedbackAdvanced.setMatchingDegree(score);
        modelRepository.insert(modelFeedbackAdvanced);
    }
}

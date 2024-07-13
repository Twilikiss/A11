package com.elysia.demoApp.controller;

import com.elysia.demoApp.model.entity.system.Article;
import com.elysia.demoApp.model.result.utils.Result;
import com.elysia.demoApp.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author cxb
 * @ClassName ArticleController
 * @date 12/7/2023 上午10:17
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/article")
@Api(value = "获取趣闻（文章、画廊等等）的相关接口")
@CrossOrigin
public class ArticleController {
    @Resource
    private ArticleService articleService;
    @ApiOperation("获取首页趣闻（三篇）")
    @PostMapping("/homepage")
    public Result getHomepageArticle() {
        List<Article> articles = articleService.getHomepageArticle();
        if (articles == null) {
            return Result.fail();
        } else {
            return Result.ok(articles);
        }
    }
}

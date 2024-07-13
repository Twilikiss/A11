package com.elysia.demoApp.service;

import com.elysia.demoApp.model.entity.system.Article;

import java.util.List;

/**
 *
 * @ClassName ArticleService
 * @author   cxb
 * @date  12/7/2023 上午10:20
 */
public interface ArticleService {
    List<Article> getHomepageArticle();
}

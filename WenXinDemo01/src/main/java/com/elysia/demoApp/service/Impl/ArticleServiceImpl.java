package com.elysia.demoApp.service.Impl;

import com.elysia.demoApp.Repository.ArticleRepository;
import com.elysia.demoApp.model.entity.system.Article;
import com.elysia.demoApp.service.ArticleService;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author cxb
 * @ClassName ArticleServiceImpl
 * @date 12/7/2023 上午10:21
 */
@Service
public class ArticleServiceImpl implements ArticleService {
    @Resource
    private ArticleRepository articleRepository;
    @Override
    public List<Article> getHomepageArticle() {
        Article article = new Article();
        article.setType("homepage");
        Example<Article> example = Example.of(article);
        List<Article> articleList = articleRepository.findAll(example);
        if (articleList.size() != 0) {
            return articleList;
        }
        return null;
    }
}

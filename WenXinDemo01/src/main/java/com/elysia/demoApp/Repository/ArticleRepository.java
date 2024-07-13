package com.elysia.demoApp.Repository;

import com.elysia.demoApp.model.entity.system.Article;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @ClassName ArticleRepository
 * @author   cxb
 * @date  12/7/2023 上午11:42
 */
public interface ArticleRepository extends MongoRepository<Article, String> {
}

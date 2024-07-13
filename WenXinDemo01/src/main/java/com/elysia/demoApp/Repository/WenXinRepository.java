package com.elysia.demoApp.Repository;

import com.elysia.demoApp.model.entity.system.Article;
import com.elysia.demoApp.model.entity.system.WenXinHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @ClassName WenXinRepository
 * @author   cxb
 * @date  22/7/2023 上午11:25
 */
public interface WenXinRepository extends MongoRepository<WenXinHistory, String> {
}

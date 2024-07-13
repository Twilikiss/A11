package com.elysia.demoApp.Repository;

import com.elysia.demoApp.model.entity.system.ModelFeedbackAdvanced;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @ClassName ModelRepository
 * @author   cxb
 * @date  10/7/2023 下午5:42
 */
public interface ModelRepository extends MongoRepository<ModelFeedbackAdvanced, String> {
}

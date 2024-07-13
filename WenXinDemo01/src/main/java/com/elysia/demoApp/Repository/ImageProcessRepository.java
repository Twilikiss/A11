package com.elysia.demoApp.Repository;

import com.elysia.demoApp.model.result.ImageProcessResult;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @ClassName ImageProcessRepository
 * @author   cxb
 * @date  13/7/2023 上午11:13
 */
public interface ImageProcessRepository extends MongoRepository<ImageProcessResult, String> {
}

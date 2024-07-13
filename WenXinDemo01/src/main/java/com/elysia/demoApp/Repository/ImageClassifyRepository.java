package com.elysia.demoApp.Repository;

import com.elysia.demoApp.model.result.ImageClassifyResult;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author cxb
 */
@Repository
public interface ImageClassifyRepository extends MongoRepository<ImageClassifyResult, String> {
}

package com.elysia.demoApp.Repository;

import com.elysia.demoApp.model.result.Text2ImageResult;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author cxb
 */
@Repository
public interface Text2ImageRepository extends MongoRepository<Text2ImageResult, String> {
}

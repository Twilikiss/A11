package com.elysia.demoApp.Repository;

import com.elysia.demoApp.model.result.OcrResult;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author cxb
 * @ClassName OcrRepository
 * @date 29/5/2023 下午10:35
 */
@Repository
public interface OcrRepository extends MongoRepository<OcrResult, String> {
}

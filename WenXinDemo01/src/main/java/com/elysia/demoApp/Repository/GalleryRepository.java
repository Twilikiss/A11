package com.elysia.demoApp.Repository;

import com.elysia.demoApp.model.entity.system.Gallery;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @ClassName GalleryRepository
 * @author   cxb
 * @date  17/7/2023 上午10:54
 */
public interface GalleryRepository extends MongoRepository<Gallery, String> {
}

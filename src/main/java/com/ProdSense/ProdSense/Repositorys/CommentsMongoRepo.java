package com.ProdSense.ProdSense.Repositorys;

import com.ProdSense.ProdSense.Entitys.CommentsMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentsMongoRepo extends MongoRepository<CommentsMongo, String> {
}

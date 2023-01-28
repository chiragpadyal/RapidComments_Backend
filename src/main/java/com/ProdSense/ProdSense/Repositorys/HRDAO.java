package com.ProdSense.ProdSense.Repositorys;


import com.ProdSense.ProdSense.Entitys.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HRDAO extends JpaRepository<Comments, String> {

    @Query("select comments from Comments comments where comments.thread.threadId = ?1")
    List<Comments> findCommentsByThread(String threadId);

    @Query("select comment from Comments comment where comment.id = ?1")
    List<Comments> deleteCommentById(String commentId);

}
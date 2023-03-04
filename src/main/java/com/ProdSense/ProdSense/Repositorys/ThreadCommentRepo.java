package com.ProdSense.ProdSense.Repositorys;

import com.ProdSense.ProdSense.Entitys.ThreadComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ThreadCommentRepo extends JpaRepository<ThreadComment, String> {
    @Query("select comments from ThreadComment comments where comments.thread.threadId = ?1 ORDER BY comments.createdAt ASC")
    List<ThreadComment> findByThreadIdOrderByCreatedAtAsc(String threadId);
}
//SELECT *
//FROM thread_comment
//WHERE thread_id = ?
//ORDER BY created_at ASC
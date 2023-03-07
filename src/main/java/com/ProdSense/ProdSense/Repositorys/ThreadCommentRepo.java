package com.ProdSense.ProdSense.Repositorys;

import com.ProdSense.ProdSense.Entitys.ThreadComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ThreadCommentRepo extends JpaRepository<ThreadComment, Long> {
    @Query("select comments from ThreadComment comments where comments.thread.threadId = ?1 ORDER BY comments.createdAt DESC ")
    List<ThreadComment> findByThreadIdOrderByCreatedAtDsc(String threadId);

    Page<ThreadComment> findByParentIdIsNull(Pageable page);

    Page<ThreadComment> findByParentId(String parentId, Pageable pageable);
}
//SELECT *
//FROM thread_comment
//FROM thread_comment
//WHERE thread_id = ?
//ORDER BY created_at ASC
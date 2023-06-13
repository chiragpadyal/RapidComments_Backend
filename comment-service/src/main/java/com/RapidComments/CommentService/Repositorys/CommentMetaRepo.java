package com.RapidComments.CommentService.Repositorys;

import com.RapidComments.CommentService.Entitys.CommentMeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentMetaRepo extends JpaRepository<CommentMeta, Long> {
    @Query("select comments from CommentMeta comments where comments.user.id = ?1 AND comments.comment.id = ?2")
    List<CommentMeta> findIfExist(Long userId, Long commentId);
}

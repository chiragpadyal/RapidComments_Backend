package com.ProdSense.ProdSense.ServiceImpl;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ProdSense.ProdSense.Entitys.CommentMeta;
import com.ProdSense.ProdSense.Entitys.ThreadComment;
import com.ProdSense.ProdSense.Entitys.User;

public interface CommentService {
    // write commentServiceImpl
    public ThreadComment addComment(ThreadComment newComment, String threadId);

    public List<ThreadComment> getCommentsForThread(String threadId);

    public ResponseEntity<?> getCommentsForThreadByPage(String thread, int page, int size, User user);

    public ResponseEntity<?> getCommentsForRepliesByPage(String thread, ThreadComment threadComment, int page, int size,
            User user);

    public ThreadComment modifyCommentContent(String threadId, ThreadComment commentMod);

    public ResponseEntity<String> deleteCommentContent(String threadId, ThreadComment commentMod);

    public CommentMeta updateCommentMeta(String threadId, CommentMeta commentMeta);

}
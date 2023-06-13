package com.RapidComments.CommentService.Controllers;

import com.RapidComments.CommentService.Entitys.CommentMeta;
import com.RapidComments.CommentService.ServiceImpl.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/comment-service/threads/{threadId}/comments/meta")
public class CommentMetaController {
    private static final Logger LOGGER
            = LoggerFactory.getLogger(ThreadCommentController.class);
    @Autowired
    private CommentService commentService;

    @PostMapping("")
    public ResponseEntity<CommentMeta> updateCommentMeta(@PathVariable String threadId,
            @RequestBody CommentMeta commentMeta) {
        LOGGER.info("update comment meta");
        CommentMeta createdComment = commentService.updateCommentMeta(threadId, commentMeta);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }
}

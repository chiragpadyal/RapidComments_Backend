package com.ProdSense.ProdSense.Controllers;

import com.ProdSense.ProdSense.Entitys.ThreadComment;
import com.ProdSense.ProdSense.Services.CommentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/threads/{threadId}/comments")
public class ThreadCommentController {

    @Autowired
    private CommentServiceImpl commentService;

    @PostMapping("")
    public ResponseEntity<ThreadComment> addComment(@PathVariable String threadId,
                                                    @RequestBody ThreadComment newComment) {
        ThreadComment createdComment = commentService.addComment(newComment, threadId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    @GetMapping("")
    public List<ThreadComment> getCommentsForThread(@PathVariable String threadId) {
        return commentService.getCommentsForThread(threadId);
    }

    @GetMapping("{page}")
    public ResponseEntity<?> getCommentsForThreadByPage(@PathVariable String threadId, @PathVariable int page ) {
        int size = 5 ;
        return commentService.getCommentsForThreadByPage(threadId, page, size);
    }

    @GetMapping("reply/{page}")
    public ResponseEntity<?> getCommentsForRepliesByPage(@PathVariable String threadId,@RequestBody ThreadComment threadComment, @PathVariable int page ) {
        int size = 5 ;
        return commentService.getCommentsForRepliesByPage(threadId, threadComment, page, size);
    }

    @PutMapping("")
    public ThreadComment updateCommentsForThread(@PathVariable String threadId, @RequestBody ThreadComment newComment) {
        return commentService.modifyCommentContent(threadId, newComment);
    }

    @DeleteMapping("")
    public ResponseEntity<String> deleteCommentsForThread(@PathVariable String threadId, @RequestBody ThreadComment newComment) {
        return commentService.deleteCommentContent(threadId, newComment);
    }
}


package com.ProdSense.ProdSense.Controllers;

import com.ProdSense.ProdSense.Entitys.ThreadComment;
import com.ProdSense.ProdSense.Services.CommentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/threads/{threadId}/comments")
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

    @PutMapping("")
    public List<ThreadComment> updateCommentsForThread(@PathVariable String threadId, @RequestBody ThreadComment newComment) {
        return commentService.getCommentsForThread(threadId);
    }
}


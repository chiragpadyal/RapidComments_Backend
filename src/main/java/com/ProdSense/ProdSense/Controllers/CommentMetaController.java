package com.ProdSense.ProdSense.Controllers;

import com.ProdSense.ProdSense.Entitys.CommentMeta;
import com.ProdSense.ProdSense.ServiceImpl.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/threads/{threadId}/comments/meta")
public class CommentMetaController {

    @Autowired
    private CommentService commentService;

    @PostMapping("")
    public ResponseEntity<CommentMeta> addComment(@PathVariable String threadId,
            @RequestBody CommentMeta commentMeta) {
        CommentMeta createdComment = commentService.updateCommentMeta(threadId, commentMeta);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }
}

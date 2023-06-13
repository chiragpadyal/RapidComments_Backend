package com.RapidComments.CommentService.Controllers;

import com.RapidComments.CommentService.Entitys.ThreadComment;
import com.RapidComments.CommentService.Entitys.User;
import com.RapidComments.CommentService.ServiceImpl.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/comment-service/threads/{threadId}/comments")
public class ThreadCommentController {
    private static final Logger LOGGER
            = LoggerFactory.getLogger(ThreadCommentController.class);

    @Autowired
    private CommentService commentService;

    /**
     * Get all comments for a thread
     * 
     * @param threadId
     * @return a list of comments
     */
    @GetMapping("")
    public List<ThreadComment> getCommentsForThread(@PathVariable String threadId) {
        LOGGER.info("Get all comments for a thread ");
        return commentService.getCommentsForThread(threadId);
    }

    /**
     * Get comments page wise for a thread
     * Page size is 2
     * 
     * @param page - the page number
     * @return a page of comments
     */
    @PostMapping("{page}")
    public ResponseEntity<?> getCommentsForThreadByPage(@PathVariable String threadId, @PathVariable int page,
            @RequestBody User user) {
        int size = 2;
        LOGGER.info("Get comments page wise for a thread ");

        return commentService.getCommentsForThreadByPage(threadId, page, size, user);
    }

    /**
     * Get replies page wise for a comment
     * Page size is 2
     * 
     * @param threadId      - id of the thread
     * @param threadComment - the comment object whose replies are to be fetched
     * @return a page of replies
     */
    @PostMapping("reply/{page}")
    public ResponseEntity<?> getCommentsForRepliesByPage(@PathVariable String threadId,
            @RequestBody ThreadComment threadComment, @PathVariable int page) {
        LOGGER.info("Get replies page wise for a comment");
        int size = 2;
        User user = threadComment.getUser();
        return commentService.getCommentsForRepliesByPage(threadId, threadComment, page, size, user);
    }

    /**
     * Add a comment to a thread
     * 
     * @param threadId
     * @param newComment
     * @return the created comment
     */
    @PostMapping("")
    public ResponseEntity<ThreadComment> addComment(@PathVariable String threadId,
            @RequestBody ThreadComment newComment) {
        LOGGER.info("Add a comment to a thread");
        ThreadComment createdComment = commentService.addComment(newComment, threadId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    /**
     * Update a comment
     * 
     * @param threadId
     * @param newComment
     * @return the updated comment
     */
    @PutMapping("")
    public ThreadComment updateCommentsForThread(@PathVariable String threadId, @RequestBody ThreadComment newComment) {
        LOGGER.info("Update a comment");
        return commentService.modifyCommentContent(threadId, newComment);
    }

    /**
     * Delete a comment
     * 
     * @param threadId
     * @param newComment
     * @return the delete response status as string
     */
    @DeleteMapping("")
    public ResponseEntity<String> deleteCommentsForThread(@PathVariable String threadId,
            @RequestBody ThreadComment newComment) {
        LOGGER.info("Delete a comment");
        return commentService.deleteCommentContent(threadId, newComment);
    }
}

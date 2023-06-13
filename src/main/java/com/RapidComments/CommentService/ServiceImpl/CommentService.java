package com.RapidComments.ProdSense.ServiceImpl;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.RapidComments.ProdSense.Entitys.CommentMeta;
import com.RapidComments.ProdSense.Entitys.ThreadComment;
import com.RapidComments.ProdSense.Entitys.User;

public interface CommentService {
    // write commentServiceImpl
    /**
     * Add a comment to a thread
     * 
     * @param newComment
     * @param threadId
     * @return
     */
    public ThreadComment addComment(ThreadComment newComment, String threadId);

    /**
     * Get all comments for a thread
     * 
     * @param threadId
     * @return a list of comments
     */
    public List<ThreadComment> getCommentsForThread(String threadId);

    /**
     * Get comments page wise for a thread
     * Page size is 2
     * 
     * @param thread
     * @param page
     * @param size
     * @param user
     * @return
     */
    public ResponseEntity<?> getCommentsForThreadByPage(String thread, int page, int size, User user);

    /**
     * Get the replies for a comment by page
     * 
     * @param thread
     * @param threadComment
     * @param page
     * @param size
     * @param user
     * @return
     */
    public ResponseEntity<?> getCommentsForRepliesByPage(String thread, ThreadComment threadComment, int page, int size,
            User user);

    /**
     * Add a reply to a comment
     * 
     * @param threadId
     * @param threadComment
     * @return
     */
    public ThreadComment modifyCommentContent(String threadId, ThreadComment commentMod);

    /**
     * Delete a comment from a thread
     * 
     * @param threadId
     * @param commentMod
     * @return
     */
    public ResponseEntity<String> deleteCommentContent(String threadId, ThreadComment commentMod);

    /**
     * Update the comment meta data for a comment in a thread
     * 
     * @param threadId
     * @param commentMeta
     * @return
     */
    public CommentMeta updateCommentMeta(String threadId, CommentMeta commentMeta);

}
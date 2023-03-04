package com.ProdSense.ProdSense.Services;

import com.ProdSense.ProdSense.Entitys.ThreadComment;
import com.ProdSense.ProdSense.Entitys.ThreadEntity;
import com.ProdSense.ProdSense.Entitys.User;
import com.ProdSense.ProdSense.Repositorys.ThreadCommentRepo;
import com.ProdSense.ProdSense.Repositorys.ThreadRepo;
import com.ProdSense.ProdSense.Repositorys.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class CommentServiceImpl {
    // TODO: add Thread verification
    // TODO: add User verification
    // TODO: change comment id to uuid
    // TODO: delete and update endpoint
    // TODO: implementation
    // TODO: pagination
    // TODO: websocket notification
    // TODO: email notification
    // TODO: graphql theme
    // TODO: moderation feature: report, spam, spoiler etc

    @Autowired
    private ThreadCommentRepo commentRepository;

    @Autowired
    private ThreadRepo threadRepo;

    @Autowired
    private UserRepository userRepository;

    public ThreadComment addComment(ThreadComment newComment, String threadId) {
        User user = userRepository.findUserByuser_id(newComment.getUser().getUser_id());
        ThreadEntity  thread = threadRepo.getById(threadId);
        newComment.setThread(thread);
        newComment.setUser(user);
        if (newComment.getParentId() != null) {
            ThreadComment parentComment = commentRepository.findById(newComment.getParentId())
                    .orElseThrow(() -> new Error("Parent comment not found"));
            newComment.setParent(parentComment);
        }
        return commentRepository.save(newComment);
    }

    public List<ThreadComment> getCommentsForThread(String threadId) {
        // Retrieve all comments for the given thread ID
        List<ThreadComment> comments = commentRepository.findByThreadIdOrderByCreatedAtAsc(threadId);

        List<ThreadComment> topLevelComments = new ArrayList<>();
        Map<Long, ThreadComment> commentMap = new HashMap<>();

        for (ThreadComment comment : comments) {
            commentMap.put(comment.getId(), comment);

            if (comment.getParent() == null) {
                topLevelComments.add(comment);
            } else {
                ThreadComment parentComment = commentMap.get(comment.getParent().getId());
                if(Objects.equals(parentComment.getContent(), "")) parentComment.getChildren().add(comment);
            }
        }
        return topLevelComments;
    }

/*    @Transactional
    public ThreadComment modifyCommentContent(String threadId, ThreadComment commentMod) {
        Optional<ThreadComment> optionalComment = commentRepository.findById(String.valueOf(commentMod.getId()));
        if (optionalComment.isPresent()) {
            ThreadComment comment = optionalComment.get();
            comment.setContent(commentMod.getContent());
            comment.setLastModified(Instant.now());
            return commentRepository.save(comment);
        } else {
            throw new Error("Comment not found with ID: " + commentId);
        }
    }*/
}
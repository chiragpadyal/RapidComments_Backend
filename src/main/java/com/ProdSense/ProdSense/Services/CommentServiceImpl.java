package com.ProdSense.ProdSense.Services;

import com.ProdSense.ProdSense.Entitys.CommentMeta;
import com.ProdSense.ProdSense.Entitys.ThreadComment;
import com.ProdSense.ProdSense.Entitys.ThreadEntity;
import com.ProdSense.ProdSense.Entitys.User;
import com.ProdSense.ProdSense.Repositorys.CommentMetaRepo;
import com.ProdSense.ProdSense.Repositorys.ThreadCommentRepo;
import com.ProdSense.ProdSense.Repositorys.ThreadRepo;
import com.ProdSense.ProdSense.Repositorys.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;

@Slf4j
@Service
public class CommentServiceImpl {
    // TODO: delete and update endpoint [DONE]
    // TODO: pagination [DONE]
    // TODO: Inverse SOrt in reply !imp [DONE]
    // TODO: Pagination not working on reply [DONE]

    // TODO: add Thread verification
    // TODO: add User verification
    // TODO: change comment id to uuid
    // TODO: websocket notification
    // TODO: email notification
    // TODO: graphql theme
    // TODO: moderation feature: report, spam, spoiler etc
    // TODO: Likes Dislikes Limiter to 0,1 and null
    // TODO: calculate quality and update Like Dislike on vote change
    // TODO: Integrate pagination in frontend
    // TODO: children (replies) has total pages <hard>


    @Autowired
    private ThreadCommentRepo commentRepository;

    @Autowired
    private ThreadRepo threadRepo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentMetaRepo commentMetaRepo;

    @Transactional
    public ThreadComment addComment(ThreadComment newComment, String threadId) {
        User user = userRepository.findUserByuser_id(newComment.getUser().getUser_id());
        ThreadEntity  thread = threadRepo.getById(threadId);
        newComment.setThread(thread);
        newComment.setUser(user);
        if (newComment.getParentId() != null) {
            ThreadComment parentComment = commentRepository.findById(Long.valueOf(newComment.getParentId()))
                    .orElseThrow(() -> new Error("Parent comment not found"));
            newComment.setParent(parentComment);
        }
        return commentRepository.save(newComment);
    }

    @Transactional
    public List<ThreadComment> getCommentsForThread(String threadId) {
        // Retrieve all comments for the given thread ID
        List<ThreadComment> comments = commentRepository.findByThreadIdOrderByCreatedAtDsc(threadId);

        List<ThreadComment> topLevelComments = new ArrayList<>();
        Map<Long, ThreadComment> commentMap = new HashMap<>();

        for (ThreadComment comment : comments) {
            commentMap.put(comment.getId(), comment);

            if (comment.getParent() == null) {
                topLevelComments.add(comment);
            } else {
                ThreadComment parentComment = commentMap.get(comment.getParent().getId());
                if (parentComment != null && Objects.equals(parentComment.getContent(), "")) {
                    parentComment.getChildren().add(comment);
                }
            }
        }
        return topLevelComments;
    }

    public ResponseEntity<?> getCommentsForThreadByPage(String thread,int page,int size) {
        try {
            Pageable paging = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Page<ThreadComment> comments = commentRepository.findByParentIdIsNull(paging);

            List<ThreadComment> commentResponses = new ArrayList<>();
            for (ThreadComment comment : comments.getContent()) {
                commentResponses.add(getCommentResponse(comment, page, size));
            }

            Map<String, Object> response = new HashMap<>();
            response.put("comments", commentResponses);
            response.put("currentPage", comments.getNumber());
            response.put("totalItems", comments.getTotalElements());
            response.put("totalPages", comments.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

/*    private ThreadComment getCommentResponse(ThreadComment comment, Pageable childPaging) {
        ThreadComment commentResponse = new ThreadComment();
        commentResponse.setId(comment.getId());
        commentResponse.setUser(comment.getUser());
        commentResponse.setContent(comment.getContent());
        commentResponse.setCreatedAt(comment.getCreatedAt());
        commentResponse.setLastModified(comment.getLastModified());
        commentResponse.setLikes(comment.getLikes());
        commentResponse.setDislikes(comment.getDislikes());
        commentResponse.setQuality(comment.getQuality());

        Page<ThreadComment> children = commentRepository.findByParentId(String.valueOf(comment.getId()), childPaging);
        List<ThreadComment> childrenResponses = new ArrayList<>();
        for (ThreadComment child : children.getContent()) {
            childrenResponses.add(getCommentResponse(child, childPaging));
        }
        commentResponse.setChildren(childrenResponses);

        return commentResponse;
    }*/

    public ResponseEntity<?> getCommentsForRepliesByPage( String thread,ThreadComment threadComment, int page, int size){
        try {
            Pageable paging = PageRequest.of(page, size, Sort.by("createdAt").descending());

            Optional<ThreadComment> commentOpt = commentRepository.findById(threadComment.getId());

            if(commentOpt.isPresent()){
                ThreadComment comment = commentOpt.get();
                Map<String, Object> response = new HashMap<>();
                Page<ThreadComment> children = commentRepository.findByParentId(String.valueOf(comment.getId()), paging);
                List<ThreadComment> childrenResponses = new ArrayList<>();
                for (ThreadComment child : children.getContent()) {
                    ThreadComment childResponse = getCommentResponse(child, 0, size);
                    childrenResponses.add(childResponse);
                }
                comment.setChildren(childrenResponses);
                response.put("comments", comment);
                response.put("currentPage", children.getNumber());
                response.put("totalItems", children.getTotalElements());
                response.put("totalPages", children.getTotalPages());
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private ThreadComment getCommentResponse(ThreadComment comment, int page, int size) {
        ThreadComment commentResponse = new ThreadComment();
        commentResponse.setId(comment.getId());
        commentResponse.setUser(comment.getUser());
        commentResponse.setContent(comment.getContent());
        commentResponse.setCreatedAt(comment.getCreatedAt());
        commentResponse.setLastModified(comment.getLastModified());
        commentResponse.setLikes(comment.getLikes());
        commentResponse.setDislikes(comment.getDislikes());
        commentResponse.setQuality(comment.getQuality());

        List<ThreadComment> childrenResponses = new ArrayList<>();
        Pageable paging = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<ThreadComment> children = commentRepository.findByParentId(String.valueOf(comment.getId()), paging);
        for (ThreadComment child : children.getContent()) {
            ThreadComment childResponse = getCommentResponse(child, 0, size);
            childrenResponses.add(childResponse);
        }
        commentResponse.setChildren(childrenResponses);

        return commentResponse;
    }


/*    private CommentMetaResponse getCommentMetaResponse(CommentMeta commentMeta) {
        CommentMetaResponse commentMetaResponse = new CommentMetaResponse();
        commentMetaResponse.setId(commentMeta.getId());
        commentMetaResponse.setCommentId(commentMeta.getComment().getId());
        commentMetaResponse.setUser(commentMeta.getUser());
        commentMetaResponse.setMetaKey(commentMeta.getMetaKey());
        commentMetaResponse.setMetaValue(commentMeta.getMetaValue());
        return commentMetaResponse;
    }*/

    @Transactional
    public ThreadComment modifyCommentContent(String threadId, ThreadComment commentMod) {
        Optional<ThreadComment> optionalComment = commentRepository.findById(commentMod.getId());
        if (optionalComment.isPresent()) {
            ThreadComment comment = optionalComment.get();
            comment.setContent(commentMod.getContent());
            comment.setLastModified(Instant.now());
            return commentRepository.save(comment);
        } else {
            throw new Error("Comment not found with ID: " + commentMod.getId());
        }
    }


    @Transactional
    public ResponseEntity<String> deleteCommentContent(String threadId, ThreadComment commentMod) {
        Optional<ThreadComment> optionalComment = commentRepository.findById(commentMod.getId());
        if (optionalComment.isPresent()) {
            commentRepository.delete(optionalComment.get());
            return new ResponseEntity<>( "Deleted", HttpStatus.OK);
        } else {
            throw new Error("Comment not found with ID: " + commentMod.getId());
        }
    }


    @Transactional
    public CommentMeta updateCommentMeta(String threadId, CommentMeta commentMeta) {
        ThreadComment opt = commentMeta.getComment();
        Optional<ThreadComment> optionalComment = commentRepository.findById(opt.getId());
        User user = commentMeta.getUser();
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findUserByuser_id(user.getUser_id()));
        if (optionalComment.isPresent() && optionalUser.isPresent()) {
            if (commentMeta.getVote() == 0 || commentMeta.getVote() == 1 || commentMeta.getVote() == 2){
                if(commentMeta.getVote() == 2) commentMeta.setVote(null); ;
                commentMeta.setUser(optionalUser.get());
                commentMeta.setComment(optionalComment.get());
                //Check if commentMeta exist!
                Optional<CommentMeta> check =  commentMetaRepo.findIfExist(optionalUser.get().getId(),optionalComment.get().getId()).stream().findFirst();
                if(check.isPresent()) commentMeta.setId(check.get().getId());
                commentMetaRepo.save(commentMeta);
                return commentMeta;
            }
            throw new Error("Comment value out of bound");
        }
        throw new Error("Comment not found with ID: " + opt.getId());
    }
}
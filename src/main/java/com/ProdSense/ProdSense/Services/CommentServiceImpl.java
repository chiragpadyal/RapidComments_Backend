package com.ProdSense.ProdSense.Services;

import com.ProdSense.ProdSense.Entitys.CommentMeta;
import com.ProdSense.ProdSense.Entitys.ThreadComment;
import com.ProdSense.ProdSense.Entitys.ThreadEntity;
import com.ProdSense.ProdSense.Entitys.User;
import com.ProdSense.ProdSense.Repositorys.CommentMetaRepo;
import com.ProdSense.ProdSense.Repositorys.ThreadCommentRepo;
import com.ProdSense.ProdSense.Repositorys.ThreadRepo;
import com.ProdSense.ProdSense.Repositorys.UserRepository;
import com.ProdSense.ProdSense.ServiceImpl.CommentService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * TODO: delete and update endpoint [DONE]
 * TODO: pagination [DONE]
 * TODO: Inverse SOrt in reply !imp [DONE]
 * TODO: Pagination not working on reply [DONE]
 * TODO: load more comment on scroll [Done]
 * TODO: Integrate pagination in frontend [DONE]
 * TODO: add Thread verification
 * TODO: add User verification
 * TODO: change comment id to uuid
 * TODO: websocket notification
 * TODO: email notification
 * TODO: graphql theme
 * TODO: moderation feature: report, spam, spoiler etc
 * TODO: Likes Dislikes Limiter to 0,1 and null
 * TODO: calculate quality and update Like Dislike on vote change
 * TODO: children (replies) has total pages <hard>
 * TODO: add proper creation time and date
 * TODO: the replies pagination not working as indented
 */
@Slf4j
@Service
public class CommentServiceImpl implements CommentService {

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
        ThreadEntity thread = threadRepo.getReferenceById(threadId);
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

    @Transactional
    public ResponseEntity<?> getCommentsForThreadByPage(String thread, int page, int size, User user) {
        try {
            Pageable paging = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "likes", "quality", "createdAt"));
            Page<ThreadComment> comments = commentRepository.findByParentIdIsNull(paging);
            log.info("user data is: " + user.getUser_id().toString());
            Optional<User> optionalUser = Optional.ofNullable(userRepository.findUserByuser_id(user.getUser_id()));

            List<ThreadComment> commentResponses = new ArrayList<>();
            for (ThreadComment comment : comments.getContent()) {
                commentResponses.add(getCommentResponse(comment, page, size, optionalUser.get()));

            }

            Map<String, Object> response = new HashMap<>();
            response.put("comments", commentResponses);
            response.put("currentPage", comments.getNumber());
            response.put("totalComments", comments.getTotalElements());
            response.put("totalPages", comments.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

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
    @Transactional
    public ResponseEntity<?> getCommentsForRepliesByPage(String thread, ThreadComment threadComment, int page, int size,
            User user) {
        try {
            Pageable paging = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "likes", "quality", "createdAt"));

            Optional<ThreadComment> commentOpt = commentRepository.findById(threadComment.getId());
            log.info("user data is: " + user.getUser_id().toString());
            Optional<User> optionalUser = Optional.ofNullable(userRepository.findUserByuser_id(user.getUser_id()));
            if (commentOpt.isPresent()) {
                ThreadComment comment = commentOpt.get();

                Map<String, Object> response = new HashMap<>();
                Page<ThreadComment> children = commentRepository.findByParentId(String.valueOf(comment.getId()),
                        paging);
                List<ThreadComment> childrenResponses = new ArrayList<>();
                for (ThreadComment child : children.getContent()) {
                    ThreadComment childResponse = getCommentResponse(child, 0, size, optionalUser.get());
                    childrenResponses.add(childResponse);
                }
                comment.setChildren(childrenResponses);

                ThreadComment[] comment_array = new ThreadComment[1];
                comment_array[0] = comment;

                response.put("comments", comment_array);
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

    @Transactional
    private ThreadComment getCommentResponse(ThreadComment comment, int page, int size, User user) {
        ThreadComment commentResponse = new ThreadComment();
        commentResponse.setId(comment.getId());
        commentResponse.setUser(comment.getUser());
        commentResponse.setContent(comment.getContent());
        commentResponse.setCreatedAt(comment.getCreatedAt());
        commentResponse.setLastModified(comment.getLastModified());
        commentResponse.setLikes(comment.getLikes());
        commentResponse.setDislikes(comment.getDislikes());
        commentResponse.setQuality(comment.getQuality());
        commentResponse.setHasMore(false);

        List<ThreadComment> childrenResponses = new ArrayList<>();
        Pageable paging = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "likes", "quality", "createdAt"));
        Page<ThreadComment> children = commentRepository.findByParentId(String.valueOf(comment.getId()), paging);

        // Fetch only the vote of the current user as a list
        List<CommentMeta> commentMetaList = commentMetaRepo.findIfExist(user.getId(), comment.getId())
                .stream()
                .collect(Collectors.toList());

        if (!commentMetaList.isEmpty()) {
            /*
             * List<CommentMeta> checklist = new ArrayList<>();
             * CommentMeta commentMeta = new CommentMeta();
             * commentMeta.setVote(check.get().getVote());
             * checklist.add(commentMeta);
             */
            comment.setVote(commentMetaList);
            commentResponse.setVote(comment.getVote());
        }
        if (children.getTotalPages() > 1) {
            commentResponse.setHasMore(true);
        }

        for (ThreadComment child : children.getContent()) {
            ThreadComment childResponse = getCommentResponse(child, 0, size, user);
            childrenResponses.add(childResponse);

        }

        commentResponse.setChildren(childrenResponses);

        return commentResponse;
    }

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
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
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
            if (commentMeta.getVote() == 0 || commentMeta.getVote() == 1) {
                // Check if commentMeta exist!
                Optional<CommentMeta> check = commentMetaRepo
                        .findIfExist(optionalUser.get().getId(), optionalComment.get().getId()).stream().findFirst();

                commentMeta.setUser(optionalUser.get());
                if (check.isPresent()) {
                    commentMeta.setId(check.get().getId());
                    if (check.get().getVote() == commentMeta.getVote()) {
                        if (commentMeta.getVote() == 1) {
                            log.info("Vote: is same and have vote as 1");
                            commentMeta.setVote(2L);
                            optionalComment.get().setLikes(optionalComment.get().getLikes() - 1);
                        } else if (commentMeta.getVote() == 0) {
                            commentMeta.setVote(2L);
                            log.info("Vote: is same and have vote as 0");
                            optionalComment.get().setDislikes(optionalComment.get().getDislikes() - 1);
                        }
                    } else {
                        // Second vote with different value
                        if (commentMeta.getVote() == 1 && check.get().getVote() == 0) {
                            log.info("Vote: is different with prev value as 0 and have vote as 1");
                            optionalComment.get().setLikes(optionalComment.get().getLikes() + 1);
                            optionalComment.get().setDislikes(optionalComment.get().getDislikes() - 1);
                        } else if (commentMeta.getVote() == 0 && check.get().getVote() == 1) {
                            log.info("Vote: is different with prev value as 1 and have vote as 0");
                            optionalComment.get().setDislikes(optionalComment.get().getDislikes() + 1);
                            optionalComment.get().setLikes(optionalComment.get().getLikes() - 1);
                        } else {
                            if (commentMeta.getVote() == 1) {
                                log.info("Vote: is different with null and have vote as 1");
                                optionalComment.get().setLikes(optionalComment.get().getLikes() + 1);
                            } else if (commentMeta.getVote() == 0) {
                                log.info("Vote: is different with null and have vote as 0");
                                optionalComment.get().setDislikes(optionalComment.get().getDislikes() + 1);
                            }
                        }
                    }
                } else {
                    // First vote
                    if (commentMeta.getVote() == 1) {
                        log.info("Vote: new and vote as 1");
                        optionalComment.get().setLikes(optionalComment.get().getLikes() + 1);
                    } else if (commentMeta.getVote() == 0) {
                        log.info("Vote: new and vote as 0");
                        optionalComment.get().setDislikes(optionalComment.get().getDislikes() + 1);
                    }
                }

                optionalComment.get().setQuality(
                        getWilsonScore(optionalComment.get().getLikes(), optionalComment.get().getDislikes()));
                commentMeta.setComment(optionalComment.get());
                commentMetaRepo.save(commentMeta);
                commentRepository.save(optionalComment.get());
                return commentMeta;
            }
            throw new Error("Comment value out of bound");
        }
        throw new Error("Comment not found with ID: " + opt.getId());
    }

    /**
     * Calculate the Wilson score interval for a given upvote and downvote count.
     * 
     * @param upvotes   The number of upvotes
     * @param downvotes The number of downvotes
     * @return The Wilson score interval
     */
    private double getWilsonScore(Long upvotes, Long downvotes) {
        double n = upvotes + downvotes;
        if (n == 0) {
            return 0;
        }
        double z = 1.96; // 95% confidence interval
        double phat = (double) upvotes / n;
        double score = (phat + z * z / (2 * n) - z * Math.sqrt((phat * (1 - phat) + z * z / (4 * n)) / n))
                / (1 + z * z / n);
        return (score * 2) - 1; // Normalize the score between -1 and 1
    }

}

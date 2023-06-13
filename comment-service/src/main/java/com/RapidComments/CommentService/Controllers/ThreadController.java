package com.RapidComments.CommentService.Controllers;

import com.RapidComments.CommentService.Entitys.ThreadEntity;
import com.RapidComments.CommentService.Repositorys.ThreadRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/comment-service")
public class ThreadController {

    private static final Logger LOGGER
            = LoggerFactory.getLogger(ThreadCommentController.class);
    @Autowired
    ThreadRepo threadRepo;
    @GetMapping("/getAllThreads")
    public ResponseEntity<List<ThreadEntity>> getAllThreads() {
        LOGGER.info("get all threads");
         try {
             return ResponseEntity.ok(threadRepo.findAll());
         }
         catch (Exception e){
             return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
         }
    }
    @PostMapping("/createThread")
    public ResponseEntity<Object> createThread(@RequestBody ThreadEntity thread) {
        LOGGER.info("create a new thread");

        try {
            return ResponseEntity.ok(threadRepo.save(thread));
        }
        catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

}

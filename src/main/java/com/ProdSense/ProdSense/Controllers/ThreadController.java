package com.ProdSense.ProdSense.Controllers;

import com.ProdSense.ProdSense.Entitys.Comments;
import com.ProdSense.ProdSense.Entitys.ThreadEntity;
import com.ProdSense.ProdSense.Repositorys.ThreadRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class ThreadController {
    @Autowired
    ThreadRepo threadRepo;
    @GetMapping("/getAllThreads")
    public ResponseEntity<List<ThreadEntity>> getAllThreads() {
         try {
             return ResponseEntity.ok(threadRepo.findAll());
         }
         catch (Exception e){
             return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
         }
    }
    @PostMapping("/createThread")
    public ResponseEntity<Object> createThread(@RequestBody ThreadEntity thread) {
        try {
            return ResponseEntity.ok(threadRepo.save(thread));
        }
        catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

}

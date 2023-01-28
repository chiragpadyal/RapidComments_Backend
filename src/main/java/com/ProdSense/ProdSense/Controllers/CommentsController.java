package com.ProdSense.ProdSense.Controllers;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.ProdSense.ProdSense.Entitys.Comments;
import com.ProdSense.ProdSense.Entitys.CommentsMongo;
import com.ProdSense.ProdSense.Repositorys.HRDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@CrossOrigin
@RestController
public class CommentsController {
	@Autowired
	private HRDAO departmentRepository;

    @GetMapping("/getAllComments")
    public ResponseEntity<List<Comments>> listDepartments() {
		try{
        return ResponseEntity.ok(departmentRepository.findAll());
		} catch(Exception e){
			return new ResponseEntity<>(null , HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/getCommentsByThread/{threadId}")
	public ResponseEntity<List<Comments>> getCommentsByThread(@PathVariable("threadId") String threadId){
		try{
			return ResponseEntity.ok(departmentRepository.findCommentsByThread(threadId));
		} catch(Exception e){
			return new ResponseEntity<>(null , HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/createComment")
	public ResponseEntity<Comments> createDepartment(@RequestBody String trial) {
		try{
			ObjectMapper objmapper = new ObjectMapper();
			Comments deptInput = objmapper.convertValue(objmapper.readValue(trial, ObjectNode.class), Comments.class);
			Comments deptRec = null;

			/* check if parent department information was passed */
			if (deptInput.getParentComments() != null) {
				Optional<Comments> parentDepartment = departmentRepository.findById(deptInput.getParentComments().getId());
				deptInput.setParentComments(parentDepartment.isPresent()? parentDepartment.get(): null);
			}

			deptRec = departmentRepository.save(deptInput);
			return ResponseEntity.ok(deptRec);
		}
		catch(Exception e){
			return new ResponseEntity<>(null , HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/deleteComment/{commentId}")
	public ResponseEntity<Object> deleteComment(@PathVariable("commentId") Long commentId){
		try {
			Comments comment = departmentRepository.findById(commentId).get();
			departmentRepository.delete(comment);
			return ResponseEntity.ok("Success");
		}
		catch(Exception e){
			return new ResponseEntity<>(null , HttpStatus.NOT_FOUND);
		}
	};

	@PutMapping("/updateComment/{commentId}")
	public ResponseEntity<Comments> updateComment(@PathVariable("commentId") Long commentId, @RequestBody Comments commentBody ){
		try {

			Comments comment = departmentRepository.findById(commentId).get();
			if (Objects.nonNull(commentBody.getMessage()) && !"".equalsIgnoreCase(commentBody.getMessage())) {
				comment.setMessage(commentBody.getMessage());
			}
			return ResponseEntity.ok(departmentRepository.save(comment));
		}catch(Exception e){
			return new ResponseEntity<>(null , HttpStatus.NOT_FOUND);
		}
	};


/*	@Autowired
	CommentsMongo commentsMongo;

	@PutMapping("/updateComment/{commentId}/like")
	public ResponseEntity<Object> updateLike(@PathVariable("commentId") Long commentId) {
		try{
			Comments comment = departmentRepository.findById(commentId).get();
			commentsMongo.
			return ResponseEntity.ok(departmentRepository.findAll());
		} catch(Exception e){
			return new ResponseEntity<>(null , HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/updateComment/{commentId}/dsilike")
	public ResponseEntity<Object> updateDislike(@PathVariable("commentId") Long commentId) {
		try{
			Comments comment = departmentRepository.findById(commentId).get();
			return ResponseEntity.ok(departmentRepository.findAll());
		} catch(Exception e){
			return new ResponseEntity<>(null , HttpStatus.NOT_FOUND);
		}
	}*/
}
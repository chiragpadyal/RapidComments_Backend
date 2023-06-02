package com.ProdSense.ProdSense.Controllers;

import com.ProdSense.ProdSense.ServiceImpl.AdminService;
import com.ProdSense.ProdSense.ServiceImpl.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.ProdSense.ProdSense.Entitys.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@PreAuthorize("hasAuthority('admin')")
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private AdminService adminService;

    @GetMapping(value = "users")
    public ResponseEntity<List<User>> getUsers() {
        try {
            return ResponseEntity.ok(userService.getAllUsers());
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping(value = "roles")
    public ResponseEntity<String> getRoles() {
        String roles = adminService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    @GetMapping(value = "roles/new/{name}/{description}") // ex: /roles/new/role1/
    public ResponseEntity<String> createRole(@PathVariable String name, @PathVariable String description) {
        String roles = adminService.createRole(name, description);
        return ResponseEntity.ok(roles);
    }

    @GetMapping(value = "roles/{roleId}/assign/{userIds}")
    public ResponseEntity<String> assignRoleToUser(@PathVariable String userIds, @PathVariable String roleId) {
        ArrayList<String> userIdArray = new ArrayList<>();

        // Currently we're not supporting multiple users
        for (String userId : userIds.split("%20")) {
            userIdArray.addAll(Arrays.asList(userId.split(" ")));
        }
        String roles = adminService.setUserRole(roleId, userIdArray);
        return ResponseEntity.ok(roles);
    }

    @GetMapping(value = "users/role/{userId}")
    public ResponseEntity<String> getUserById(@PathVariable String userId) {
        String user = adminService.getUserRole(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping(value = "users/{userId}/role/{roleId}")
    public ResponseEntity<String> deleteUserRole(@PathVariable String userId, @PathVariable String roleId) {
        String user = adminService.deleteUserRole(userId, roleId);
        return ResponseEntity.ok(user);
    }

    /**
     * @return null
     * @apiNote This method is used to trigger a fetch, in backend to populate users
     *          in the database
     */
    @GetMapping(value = "users/populate")
    public ResponseEntity<String> fetchUsers() {
        try {
            adminService.getAllUsersFromAuth0();
            return ResponseEntity.ok().build();
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(500).build();
        }
    }

}

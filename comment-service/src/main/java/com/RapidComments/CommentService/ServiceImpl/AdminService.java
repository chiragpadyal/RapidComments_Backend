package com.RapidComments.CommentService.ServiceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.ArrayList;

public interface AdminService {
    void getToken();

    void getAllUsersFromAuth0() throws JsonProcessingException;

    String getAllRoles();

    String createRole(String role, String description);

    String getUserRole(String uid);

    String setUserRole(String roleId, ArrayList<String> uid);

    String deleteUserRole(String userId, String roleId);
    boolean getAuth0UserByID(String uid);
}
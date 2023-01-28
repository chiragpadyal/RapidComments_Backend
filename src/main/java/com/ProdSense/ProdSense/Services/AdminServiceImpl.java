package com.ProdSense.ProdSense.Services;

import com.ProdSense.ProdSense.Entitys.AuthUser;
import com.ProdSense.ProdSense.Entitys.User;
import com.ProdSense.ProdSense.ServiceImpl.AdminService;
import com.ProdSense.ProdSense.ServiceImpl.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

//import javax.transaction.Transactional;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Objects;

@Service
@Transactional
@Component
public class AdminServiceImpl implements AdminService {
    Logger log = LoggerFactory.getLogger(AdminServiceImpl.class);
    @Value("${auth.properties.domain}")
    private String domain; // "https://dev-offsa7x1.us.auth0.com";
    @Value("${auth.properties.client-Id}")
    private String clientId;
    @Value("${auth.properties.client-secret}")
    private String clientSecret;
    private String token;

    @Autowired
    private UserService userService;

    private HttpResponse<String> tokenRequester() {
        return Unirest.post(domain + "/oauth/token").header("content-type", "application/json").body(MessageFormat.format("{0}{1}{2}\"grant_type\":\"client_credentials\"'}'", String.format("{\"client_id\":\"%s\",", clientId), "\"client_secret\":\"" + clientSecret + "\",", String.format("\"audience\":\"%s/api/v2/\",", domain))).asString();
    }

    private void setToken() {
        HttpResponse<String> response = tokenRequester();
        if (response.getStatus() == 200) {
            token = response.getBody().split(":")[1].split(",")[0].replace("\"", "");
        }
    }

    public void getToken() {
        // TODO: Implement expiration check
        if (token != null) return;
        setToken();
    }

    @Scheduled(fixedRate = 1000 * 60 * 60 * 24)
    @EventListener(ApplicationReadyEvent.class)
    public void getAllUsersFromAuth0() throws JsonProcessingException {
        log.info("Fetching users from Auth0");
        getToken();
        HttpResponse<String> response = Unirest.get(domain + "/api/v2/users").header("authorization", "Bearer " + token).asString();
        log.info("Response status: " + response.getStatus());
        if (!response.isSuccess()) return;
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(Objects.requireNonNull(response.getBody()));
        int length = root.size();
        ArrayList<AuthUser> users = new ArrayList<>();
        log.info("Number of users found: " + length);
        ArrayList<User> dbUsers = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            JsonNode node = root.get(i);
            AuthUser current_user = new AuthUser(node.get("user_id").toString(),
                    node.get("email").toString(),
                    node.get("name").toString(),
                    node.get("email_verified").asBoolean(),
                    node.get("picture").toString());
            users.add(current_user);
            User user = new User();
            user.setUser_id(current_user.getUser_id().replace("\"", ""));
            user.setEmail(current_user.getEmail().replace("\"", ""));
            user.setUsername(current_user.getUsername().replace("\"", ""));
            user.setPicture(current_user.getPicture().replace("\"", ""));
            user.setStatus(true);

            // Check if user exists in db
            User dbUserbyEmail = userService.getUserByEmail(user.getEmail());
            if (dbUserbyEmail == null) {
                dbUsers.add(user);
            }
            userService.batchSave(dbUsers);
        }
    }

    public boolean getAuth0UserByID(String uid) {
        log.info("Getting user by id: " + uid);
        User dbUser = userService.getUserByUserId(uid);
        if (dbUser != null) {
            return true;
        }
        getToken();
        HttpResponse<String> response = Unirest.get(domain + "/api/v2/users/" + uid.replace("|", "%7C")).header("authorization", "Bearer " + token).asString();
        log.info("Response status: " + response.getStatus());
        if (!response.isSuccess()) return false;
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = null;
        try {
            root = mapper.readTree(Objects.requireNonNull(response.getBody()));
            AuthUser current_user = new AuthUser(root.get("user_id").toString(),
                    root.get("email").toString(),
                    root.get("name").toString(),
                    root.get("email_verified").asBoolean(),
                    root.get("picture").toString());
            User user = new User();
            user.setUser_id(current_user.getUser_id().replace("\"", ""));
            user.setEmail(current_user.getEmail().replace("\"", ""));
            user.setUsername(current_user.getUsername().replace("\"", ""));
            user.setPicture(current_user.getPicture().replace("\"", ""));
            user.setStatus(true);
            userService.save(user);
            log.info("Fetch Complete");
            return true;
        } catch (Exception e) {
            log.warn("admin serviceImpl error (getAuth0UserByID): " + e.getMessage());
        }
        return false;
    }

    public String getAllRoles() {
        getToken();
        HttpResponse<String> response = Unirest.get(domain + "/api/v2/roles").header("authorization", "Bearer " + token).asString();
        return response.getBody();
    }

    public String createRole(String role, String description) {
        getToken();
        String body = MessageFormat.format("{0}{1}", String.format("{\"name\":\"%s\",", role), "\"description\":\"" + description + "\"}");
        HttpResponse<String> response = Unirest.post(domain + "/api/v2/roles").header("authorization", "Bearer " + token).body(body).asString();
        return response.getBody();
    }

    public String getUserRole(String uid) {
        getToken();
        uid = uid.replace("|", "%7C");
        HttpResponse<String> response = Unirest.get(domain + "/api/v2/users/" + uid + "/roles").header("authorization", "Bearer " + token).asString();
        return response.getBody();
    }

    public String setUserRole(String roleId, ArrayList<String> uid) {
        getToken();
        // change %7C to |
        uid.replaceAll(s -> s.replace("%7C", "|"));
        String users = String.join(",", uid);
        String body = String.format("{\"users\": [\"%s\"]}", users);
        HttpResponse<String> response = Unirest
                .post(domain + "/api/v2/roles/" + roleId + "/users")
                .header("authorization", "Bearer " + token)
                .header("content-type", "application/json charset=utf-8")
                .body(body).asObject(String.class);
        return response.getBody();
    }

    public String deleteUserRole(String userId, String roleId) {
        getToken();
        String body = String.format("{\"roles\": [\"%s\"]}", roleId);
        HttpResponse<String> response = Unirest.delete(domain + "/api/v2/users/" + userId.replace("|", "%7C") + "/roles")
                .header("authorization", "Bearer " + token)
                .header("content-type", "application/json charset=utf-8")
                .body(body).asString();
        return response.getBody();
    }
}
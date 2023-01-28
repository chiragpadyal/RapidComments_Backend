package com.ProdSense.ProdSense.ServiceImpl;

import com.ProdSense.ProdSense.Entitys.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

public interface UserService {
    User save(User user);

    User getUserByUserId(String login);

    Page<User> findAll(Pageable paging);

    List<User> getAllUsers();

    User getUserByEmail(String email);

    int batchSave(ArrayList<User> dbUsers);
}

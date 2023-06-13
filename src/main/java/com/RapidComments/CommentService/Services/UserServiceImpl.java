package com.RapidComments.ProdSense.Services;

import com.RapidComments.ProdSense.Entitys.User;
import com.RapidComments.ProdSense.Repositorys.UserRepository;
import com.RapidComments.ProdSense.ServiceImpl.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserByUserId(String userid) {
        return userRepository.findUserByuser_id(userid);
    }

    @Override
    public Page<User> findAll(Pageable paging) {
        return userRepository.findAll(paging);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public int batchSave(ArrayList<User> dbUsers) {
        return userRepository.saveAll(dbUsers).size();
    }


}
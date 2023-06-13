package com.RapidComments.ProdSense.Repositorys;

import com.RapidComments.ProdSense.Entitys.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    @Query("select user from User user where user.id = ?1")
    User findUserById(int id);

    @Query("SELECT max(d.id) FROM User d ")
    Integer getMaxIdUser();

    @Query("select user from User user where user.user_id = ?1")
    User findUserByuser_id(String login);

    @Query("select user from User user where user.email = ?1")
    User findUserByEmail(String email);
}

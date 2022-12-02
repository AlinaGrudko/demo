package com.example.demo.repository;

import com.example.demo.entity.user.Role;
import com.example.demo.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUserName(String userName);

    List<User> findByActive(boolean b);


    List<User> findByRolesContainsAndActive(Role role, boolean b);

    @Query("select u " +
            "from User u " +
            "where u.email like %:search% " +
            "or concat(u.firstName, ' ', u.lastName) like %:search% " +
            "or u.userName like %:search%")
    List<User> findBySearch(@Param("search") String search);
}

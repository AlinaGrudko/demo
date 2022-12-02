package com.example.demo.service.user;

import com.example.demo.dto.password.PasswordDto;
import com.example.demo.entity.advertisement.Advertisement;
import com.example.demo.entity.user.User;
import com.example.demo.exception.IncorrectPasswordException;
import com.example.demo.exception.PasswordsNotMatchException;
import com.example.demo.exception.UserAlreadyExistsException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    User findUserByUserName(String userName);

    User saveUser(User user) throws UserAlreadyExistsException;

    User updateUser(User user);

    User getById(int id);

    void setNewPassword(PasswordDto passwordDto) throws PasswordsNotMatchException, IncorrectPasswordException;

    Page<User> findPaginatedAllUsers(Pageable pageable);

    Page<User> findPaginatedAllUsers(Pageable pageable, String search);

    Page<User> findPaginatedModerators(Pageable pageable);

    Page<User> findPaginatedModerators(Pageable pageable, String search);

    Page<User> findPaginatedBlockedUsers(Pageable pageable);

    Page<User> findPaginatedBlockedUsers(Pageable pageable, String search);

    void blockUser(int id);

    void unblockUser(int id);

    void changeRoleToUser(int id);

    void changeRoleToModerator(int id);

    void addFavoriteAdvertisement(int advertisementId);

    void deleteFavoriteAdvertisement(int advertisementId);

    Page<Advertisement> findPaginatedFavoriteAdvertisements(Pageable pageable);

}

package com.mylibrary.user.dao;

import com.mylibrary.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * JPA Repository for {@link User} entity
 *
 * @author Bharadwaj Adepu
 */
public interface UserDAO extends JpaRepository<User, String> {

    /**
     * Loads user by userId.
     *
     * @return {@link Optional <Book>}
     */
    Optional<User> findUserByUserId(int userId);

}

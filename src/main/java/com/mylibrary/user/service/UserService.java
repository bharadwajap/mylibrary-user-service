package com.mylibrary.user.service;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mylibrary.user.dao.UserDAO;
import com.mylibrary.user.domain.User;
import com.mylibrary.user.dto.UserResource;
import com.mylibrary.user.exceptions.ResourceNotFoundException;
import com.mylibrary.user.util.MapperUtils;

/**
 * Service class for User controller.
 *
 * @author Bharadwaj Adepu
 */
@Service
public class UserService {

    private UserDAO userDAO;

    /**
     * Instantiates a new {@link UserService}
     *
     * @param userDAO the {@link UserDAO}
     */
    @Autowired
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Allows to retrieve the user by ISBN
     *
     * @param userId User identifier
     * @return {@link com.mylibrary.user.dto.UserResource}
     */
    public UserResource getUserById(int userId) {
        User user = this.userDAO.findUserByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("User", userId));
        return MapperUtils.mapTo(user, UserResource.class);
    }

    /**
     * Gets a page with Users.
     *
     * @param pageable the {@link Pageable} pageable
     * @return page of Users
     */
    public Page<UserResource> getUsers(Pageable pageable) {
        return convert(this.userDAO.findAll(pageable), pageable);
    }

    /**
     * Creates a user.
     *
     * @param userResource the {@link UserResource}
     * @return the {@link UserResource}
     */
    public UserResource createUser(UserResource userResource) {
    	final User user = new User(userResource.getUserName(), userResource.getIdProof(),
    			userResource.getIdType(), userResource.getMobile());
    	
        return convert(this.userDAO.save(user));
    }


    private Page<UserResource> convert(Page<User> page, Pageable pageable) {
        return new PageImpl<>(page.getContent().stream()
                .map(this::convert).collect(Collectors.toList()), pageable, page.getTotalElements());
    }

    private UserResource convert(User user) {
        return MapperUtils.mapTo(user, UserResource.class);
    }
    
}
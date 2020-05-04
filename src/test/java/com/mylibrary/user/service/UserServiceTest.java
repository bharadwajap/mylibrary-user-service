package com.mylibrary.user.service;

import com.mylibrary.user.dao.UserDAO;
import com.mylibrary.user.domain.User;
import com.mylibrary.user.dto.UserResource;
import com.mylibrary.user.exceptions.ConstraintViolationException;
import com.mylibrary.user.exceptions.ResourceNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;

/**
 * @author Bharadwaj Adepu
 */
public class UserServiceTest {

    private UserService userService;

    private UserDAO userDAO;

    @Before
    public void setUp() throws Exception {
        this.userDAO = Mockito.mock(UserDAO.class);
        this.userService = new UserService(this.userDAO);
    }
    
    User user = new User("Rishi Kapoor","DLIND39384948393939","DL",9876543210L);
    UserResource userResource = new UserResource("Rishi Kapoor","DLIND39384948393939","DL",9876543210L);
    
    @Test
    public void getuserByISBN() throws Exception {
        when(this.userDAO.findUserByUserId(1)).thenReturn(Optional.of(user));

        UserResource userResource = this.userService.getUserById(1);
        Assert.assertEquals("Rishi Kapoor", userResource.getUserName());
        Assert.assertEquals(9876543210L, userResource.getMobile());

        verify(this.userDAO, times(1)).findUserByUserId(1);
        verifyNoMoreInteractions(this.userDAO);
    }

    @Test
    public void getuserByISBN_NO_RESOURCE_FOUND() throws Exception {
        when(this.userDAO.findUserByUserId(1)).thenReturn(Optional.empty());
        try {
            this.userService.getUserById(1);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ResourceNotFoundException);
        } finally {
            verify(this.userDAO, times(1)).findUserByUserId(1);
            verifyNoMoreInteractions(this.userDAO);
        }
    }

    @Test
    public void testGetusers() {
        Pageable pageable = new PageRequest(0, 10, null);
        when(this.userDAO.findAll(pageable)).thenReturn(new PageImpl<>(Collections.singletonList(user)));

        Page<UserResource> page = this.userService.getUsers(pageable);
        Assert.assertEquals(1, page.getTotalElements());
        Assert.assertEquals(1, page.getTotalPages());

        Assert.assertEquals("Rishi Kapoor", page.getContent().get(0).getUserName());
        Assert.assertEquals(9876543210L, page.getContent().get(0).getMobile());

        verify(this.userDAO, times(1)).findAll(pageable);
        verifyNoMoreInteractions(this.userDAO);
    }

    @Test
    public void testGetusers_EMPTY_RESULT_SET() {
        Pageable pageable = new PageRequest(0, 10, null);
        when(this.userDAO.findAll(pageable)).thenReturn(new PageImpl<>(new ArrayList<>()));

        Page<UserResource> page = this.userService.getUsers(pageable);
        Assert.assertEquals(0, page.getTotalElements());
        Assert.assertEquals(0, page.getTotalPages());

        verify(this.userDAO, times(1)).findAll(pageable);
        verifyNoMoreInteractions(this.userDAO);
    }

    @Test
    public void testCreateuser() {
        when(this.userDAO.findUserByUserId(1)).thenReturn(Optional.empty());

        this.userService.createUser(userResource);

        verify(this.userDAO, times(1)).findUserByUserId(1);
        verify(this.userDAO, times(1)).save(Mockito.any(User.class));
        verifyNoMoreInteractions(this.userDAO);
    }

    @Test
    public void testCreateuser_ALREADY_EXISTS() {
        when(this.userDAO.findUserByUserId(1)).thenReturn(Optional.of(user));

        try {
            this.userService.createUser(userResource);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ConstraintViolationException);
        } finally {
            verify(this.userDAO, times(1)).findUserByUserId(1);
            verifyNoMoreInteractions(this.userDAO);
        }
    }

}
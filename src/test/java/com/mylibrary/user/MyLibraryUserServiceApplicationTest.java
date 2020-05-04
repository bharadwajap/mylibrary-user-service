package com.mylibrary.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Important test class to check whether spring context can be loaded correctly.
 * Checks that application can be run.
 *
 * @author Vadym Lotar
 * @see SpringBootTest
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MyLibraryUserServiceApplicationTest {

    @Test
    public void contextLoads() {
    }

}
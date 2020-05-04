package com.mylibrary.user;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Main starting point of the application.
 *
 * @author Bharadwaj Adepu
 * @see SpringBootApplication
 * @see EnableSwagger2
 * <p>
 */
@SpringBootApplication
@EnableSwagger2
public class MyLibraryUserServiceApplication {

    /**
     * Main starting point of the microservice.
     *
     * @param args an array of arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(MyLibraryUserServiceApplication.class, args);
    }
}

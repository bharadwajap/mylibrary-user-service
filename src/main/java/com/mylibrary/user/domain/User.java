package com.mylibrary.user.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Domain class which represent User entity in data storage
 *
 * @author Bharadwaj Adepu
 */

@AllArgsConstructor
@Entity
@Table(name = "users")
@Data
public class User implements Serializable {

    private static final long serialVersionUID = -1727518477635717091L;

    @Id
    @Column(name = "id")
    private int userId;

    @Column(name = "username")
    private String userName;

    @Column(name = "id_proof")
    private String idProof;
    
    @Column(name = "id_type")
    private String idType;
    
    @Column
    private long mobile;
    
    /**
     * Must be present because JPA is using default constructor
     */
    @SuppressWarnings("unused")
    public User() {
    }

    public User(String userName, String idProof,
    		String idType, long mobile) {
        super();
        this.userName = userName;
        this.idProof = idProof;
        this.idType = idType;
        this.mobile = mobile;
    }
}

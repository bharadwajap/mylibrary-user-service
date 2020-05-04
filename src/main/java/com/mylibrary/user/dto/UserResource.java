package com.mylibrary.user.dto;

import javax.validation.constraints.NotNull;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Resource class for User entity.
 *
 * @author Bharadwaj Adepu
 */
@Relation(value = "user", collectionRelation = "users")
@Getter
@Setter
@ApiModel
public class UserResource extends ResourceSupport {

    @ApiModelProperty(value = "Id for the User", required = true, example = "12345")
    @NotNull
	private int userId;
	
    @ApiModelProperty(value = "Name of the User", required = true, example = "John Doe")
    @NotNull   
    private String userName;

    @ApiModelProperty(value = "Id Proof Number", required = true, example = "DLFAP0000944589")
    @NotNull
    private String idProof;

    @ApiModelProperty(value = "Type of ID Proof", required = true, example = "Driving License")
    @NotNull
    private String idType;

    @ApiModelProperty(value = "Mobile Number", required = true, example = "9876543210")
    @NotNull
    private long mobile;
    
    @JsonCreator
    public UserResource(@JsonProperty("userName") String userName, @JsonProperty("idProof") String idProof,
    		@JsonProperty("idType") String idType, @JsonProperty("mobile") long mobile) {
        super();
        this.userName = userName;
        this.idProof = idProof;
        this.idType = idType;
        this.mobile = mobile;
    }

    public UserResource() {
    	
    }
}

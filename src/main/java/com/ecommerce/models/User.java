package com.ecommerce.models;

import java.util.HashSet;
import java.util.Set;

import com.ecommerce.domain.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	@JsonProperty(access = Access.WRITE_ONLY)//it will help only to write if we fetch user data in fronted it dont fetch password 
	
	private String password;
   
	private String email;
   
	private String fullname;
    
    private String mobile;
    
    private UserRole role = UserRole.ROLE_CUSTOMER;
    @OneToMany
    private Set<Address> addresses= new HashSet<>();
    @ManyToMany
    @JsonIgnore //whenever we fetch user data in fronted it will not come 
    private Set<Coupon> usedCoupons = new HashSet<>();
}

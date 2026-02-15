package com.ecommerce.service.impli;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ecommerce.domain.UserRole;
import com.ecommerce.models.Seller;
import com.ecommerce.models.User;
import com.ecommerce.repository.SellerRepository;
import com.ecommerce.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomUserServiceImpli implements UserDetailsService {
      private final UserRepository userRepository;
      private final SellerRepository sellerRepository;
      private static final String SELLER_PREFIX ="seller_";
      
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
         
		if(username.startsWith(SELLER_PREFIX)) {
			 String actualUsername =username.substring(SELLER_PREFIX.length());
		     Seller seller = sellerRepository.findByEmail(actualUsername);
		     
		     if(seller!=null) {
		    	 return buildUserDetails(seller.getEmail(),
		    			 seller.getPassword(),
		    			 seller.getRole());
		     }
		}
		else {
			 
			
			User user = userRepository.findByEmail(username);
		    if(user!=null) {
		    	return buildUserDetails(user.getEmail(),user.getPassword(),user.getRole());
		    }
		}
		
		throw new UsernameNotFoundException("user or seller not found with this email"+ username);
		
	}

	private UserDetails buildUserDetails(String email, String password, UserRole role) {
		if (role == null) role = UserRole.ROLE_CUSTOMER;

		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(role.toString()));

		return new org.springframework.security.core.userdetails.User(email, password, authorities);
	}
	
	
          
}

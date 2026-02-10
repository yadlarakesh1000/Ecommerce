package com.ecommerce.service.impli;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.domain.UserRole;
import com.ecommerce.models.Cart;
import com.ecommerce.models.User;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.response.SignUpRequest;
import com.ecommerce.service.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{ 

	private final UserRepository userRepository; 
	private final PasswordEncoder passwordEncoder;
	private final CartRepository cartRepository;
	
	
	@Override
	public String createUser(SignUpRequest req) {
		    User user = userRepository.findbyEmail(req.getEmail());
		    if(user==null) {
		    	User CreatedUser = new User();
		    	CreatedUser.setEmail(req.getEmail());
		    	CreatedUser.setFullname(req.getFullname());
		    	CreatedUser.setRole(UserRole.ROLE_CUSTOMER);
		    	CreatedUser.setPassword(passwordEncoder.encode(req.getOtp()));
		    	CreatedUser.setMobile("912121111");
		    	user=userRepository.save(CreatedUser);
		    	 
		    	Cart cart = new Cart();
		    	cart.setUser(user);
		    	cartRepository.save(cart);
		    }
		    //now we need to generate the token for the user and return it to the client
		    List<GrantedAuthority> authorities = new ArrayList<>();
		    authorities.add(new SimpleGrantedAuthority(UserRole.ROLE_CUSTOMER.toString()));
		   //4:00:00
		    Authentication authentication = new UsernamePasswordAuthenticationToken(req.getEmail(),null,authorities);
		    return "";
	}
		 
	}

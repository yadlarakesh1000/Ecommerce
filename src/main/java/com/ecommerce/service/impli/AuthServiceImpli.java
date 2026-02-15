package com.ecommerce.service.impli;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.config.JwtProvider;
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
public class AuthServiceImpli  implements AuthService{
  private final UserRepository userrepository;
  private final CartRepository cartrepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtProvider jwtProvider;
	@Override
	public String createUser(SignUpRequest req) {
		User user = userrepository.findByEmail(req.getEmail());
		if(user==null) {
		  User createdUser = new User();
		  createdUser.setEmail(req.getEmail());
		  createdUser.setFullname(req.getFullname());
		  createdUser.setRole(UserRole.ROLE_CUSTOMER);
		  createdUser.setMobile("9010234516");
		  createdUser.setPassword(passwordEncoder.encode(req.getOtp()));
		  user = userrepository.save(createdUser);
		  
		  Cart cart = new Cart();
		  cart.setUser(user);
		  cartrepository.save(cart);
		  
		}
		 List<GrantedAuthority> authorities = new ArrayList<>();

	        authorities.add(new SimpleGrantedAuthority(
	                UserRole.ROLE_CUSTOMER.toString()));


	        Authentication authentication = new UsernamePasswordAuthenticationToken(
	                req.getEmail(), null, authorities);
	        SecurityContextHolder.getContext().setAuthentication(authentication);

	        return jwtProvider.generateToken(authentication);
	    }
}


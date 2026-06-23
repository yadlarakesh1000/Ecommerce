package com.ecommerce.service.impli;

import org.springframework.stereotype.Service;

import com.ecommerce.config.JwtProvider;
import com.ecommerce.models.User;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.service.UserService;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpli implements UserService {
	private final JwtProvider jwtprovider;
	private final UserRepository userRepository;

	@Override
	public User findUserByEmail(String email) throws Exception {
		User user = userRepository.findByEmail(email);
		if(user==null) {
			throw new Exception("User Not Found with the email:/t/t"+email);
		}
		return user;
	}

	@Override
	public User findUserByJwt(String jwt) throws Exception {
		  if(jwt.startsWith("Bearer ")) {
			  jwt= jwt.substring(7);   //we got doubt we already removed bearer substring in jwt validator then why do here? because we directly call from Jwt provider which will not remove 
		  }
		       Claims claims = jwtprovider.extractClaims(jwt);
		       String email= jwtprovider.getEmailFromJwtToken(claims);
		       User user = this.findUserByEmail(email); //or userRepository.findByEmail(email); better to use this because it will reuse the same logic written on above method
		return user;
	}

}

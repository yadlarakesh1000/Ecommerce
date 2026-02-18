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
import com.ecommerce.models.VerificationCode;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.repository.VerificationCodeRepository;
import com.ecommerce.response.SignUpRequest;
import com.ecommerce.service.AuthService;
import com.ecommerce.service.EmailService;
import com.ecommerce.service.UserService;
import com.ecommerce.utils.OtpUtil;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class AuthServiceImpli  implements AuthService{
  private final UserRepository userrepository;
  private final CartRepository cartrepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtProvider jwtProvider;
  private final VerificationCodeRepository verificationCodeRepository;
  
  private final  EmailService emailService;
	
  @Override
  public void sentLoginOtp(String email) throws Exception {
	  String SIGNING_PREFIX="signin_";
	  if(email.startsWith(SIGNING_PREFIX)) {
		  email=email.substring(SIGNING_PREFIX.length());
		  
		  User user = userrepository.findByEmail(email);
		  if(user == null) {
			  throw new Exception("User Not Exist with this Mail");
		  }
	  }
	  VerificationCode isExist=verificationCodeRepository.findByEmail(email);
	   if(isExist!=null) {
		   verificationCodeRepository.delete(isExist);
	   }
	   
	   String otp = OtpUtil.generateOtp();
	   VerificationCode verificationcode= new VerificationCode();
	   verificationcode.setOtp(otp);
	   verificationcode.setEmail(email);
	   verificationCodeRepository.save(verificationcode);
	   String subject= "FashPro login/signup otp";
	   String text = "Your Login OTP is  -";
	   emailService.sendVerificationOtpEmail(email, otp, subject, text);
  }
  
  
  @Override
	public String createUser(SignUpRequest req) throws Exception{
		String email = req.getEmail();
		String otp =req.getOtp();
		String fullName =req.getFullname();
		
		VerificationCode verificationCode = verificationCodeRepository.findByEmail(email);
		if(verificationCode==null || !verificationCode.getOtp().equals(otp)) {
			throw new Exception ("Wrong OTP...");
		}
		
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


package com.ecommerce.service.impli;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.config.JwtProvider;
import com.ecommerce.domain.UserRole;
import com.ecommerce.models.Cart;
import com.ecommerce.models.User;
import com.ecommerce.models.VerificationCode;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.repository.VerificationCodeRepository;
import com.ecommerce.request.SignUpRequest;
import com.ecommerce.request.LoginRequest;
import com.ecommerce.response.AuthResponse;
import com.ecommerce.service.AuthService;
import com.ecommerce.service.EmailService;

import com.ecommerce.utils.OtpUtil;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpli  implements AuthService{
  private final UserRepository userrepository;
  private final CartRepository cartrepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtProvider jwtProvider;
  private final VerificationCodeRepository verificationCodeRepository;
  private final CustomUserServiceImpli customUserDetails;
  
  private final  EmailService emailService;
 @Transactional
  @Override
  public void sentLoginOtp(String email) throws Exception {
	  String SIGNING_PREFIX="signin_";
	  if(email.startsWith(SIGNING_PREFIX)) {
		  email=email.substring(SIGNING_PREFIX.length());
		  
		  User user = userrepository.findByEmail(email);
		  if(user == null) {
			  throw new Exception("User Not Found");
		  }
	  }
	  // delete old otp if exists
	  verificationCodeRepository.deleteByEmail(email);
	   
	   String otp = OtpUtil.generateOtp();
	   VerificationCode verificationcode= new VerificationCode();
	   verificationcode.setOtp(otp);
	   verificationcode.setEmail(email);
	   
	   // OTP expires in 2 minutes
	   verificationcode.setExpiryTime(LocalDateTime.now().plusMinutes(2));
	   verificationcode.setAttemptCount(0);
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
		if(verificationCode==null ) {
			throw new Exception ("Wrong OTP...");
		}
		// check Expiration
		if(verificationCode.getExpiryTime().isBefore(LocalDateTime.now())) {
			verificationCodeRepository.deleteByEmail(email);
			throw new Exception("OTP Expired");
		}
		//checks attempts count
		if (verificationCode.getAttemptCount()>=5) {
			throw new Exception ("Too many OTP attempts");
		}
		// check OTP verification
		if(!verificationCode.getOtp().equals(otp)) {
			verificationCode.setAttemptCount(verificationCode.getAttemptCount()+1);
			verificationCodeRepository.save(verificationCode);
			throw new Exception("Invalid OTP");
		}
		//OTP verified successfully
		verificationCodeRepository.deleteByEmail(email);
		//after verification 2 cases either new user or Existing user
		User user = userrepository.findByEmail(req.getEmail());
		//for new user 
		if(user==null) {
		  User createdUser = new User();
		  createdUser.setEmail(req.getEmail());
		  createdUser.setFullname(fullName);
		  createdUser.setRole(UserRole.ROLE_CUSTOMER);
		  createdUser.setMobile("9010234516");
		  // here we using password_less authentication (OTP based) so we will set random password to store in db
		  // here if no use of password but we should maintain password field because spring is designed based on username+password security but we will store dummy password rather than real.
		  createdUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
		  user = userrepository.save(createdUser);
		  
		  Cart cart = new Cart();
		  cart.setUser(user);
		  cartrepository.save(cart);
		  
		}
		//Authorities represent user permissions.
		 List<GrantedAuthority> authorities = new ArrayList<>();

		   //Now user has role:ROLE_CUSTOMER
	        authorities.add(new SimpleGrantedAuthority(
	                UserRole.ROLE_CUSTOMER.toString()));

             //This creates Spring Security authentication object.
	        Authentication authentication = new UsernamePasswordAuthenticationToken(
	               //This represents a logged-in user.
	        		req.getEmail(), null, authorities);
	        //Set Security Context
	        SecurityContextHolder.getContext().setAuthentication(authentication);//Now system considers this user authenticated.
    // Now after reaching to this line JWT provider generates token
	        return jwtProvider.generateToken(authentication);
	    }


  @Override
  public AuthResponse signin(LoginRequest req) throws Exception {
	         String username= req.getEmail();
	         String otp = req.getOtp();
	         System.out.println(username+"-----"+otp);
	         Authentication authentication = authenticate(username,otp);
	         SecurityContextHolder.getContext().setAuthentication(authentication);
	         String token = jwtProvider.generateToken(authentication);
	         AuthResponse authResponse = new AuthResponse();
	         authResponse.setJwt(token);
	         authResponse.setMessage("login success");
	         Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
	         String roleName = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();

	         authResponse.setRole(UserRole.valueOf(roleName));
	return authResponse;
  }


  private Authentication authenticate(String username, String otp) throws Exception{
	  UserDetails userDetails = customUserDetails.loadUserByUsername(username);
		  if(userDetails==null) {
          System.out.println("sign in userDetails - null ");
          throw new BadCredentialsException("Invalid username");
      }
		  VerificationCode verificationCode = verificationCodeRepository.findByEmail(username);

	        if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {
	            throw new Exception("wrong otp...");
	        }
	return new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
  }
  }



package com.ecommerce.controller;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.Exception.SellerException;
import com.ecommerce.config.JwtProvider;
import com.ecommerce.domain.AccountStatus;
import com.ecommerce.domain.UserRole;
import com.ecommerce.models.Seller;
import com.ecommerce.models.VerificationCode;
import com.ecommerce.repository.SellerRepository;
import com.ecommerce.repository.VerificationCodeRepository;
import com.ecommerce.response.ApiResponse;
import com.ecommerce.response.AuthResponse;
import com.ecommerce.service.EmailService;
import com.ecommerce.service.SellerService;
import com.ecommerce.service.VerificationService;
import com.ecommerce.service.impli.CustomUserServiceImpli;
import com.ecommerce.utils.OtpUtil;

import io.jsonwebtoken.Claims;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seller")
public class SellerController {

    private final SellerRepository sellerRepository;
    private final SellerService sellerService;
    private final EmailService emailService;
    private final VerificationCodeRepository verificationCodeRepository;
    private final JwtProvider jwtProvider;
    private final CustomUserServiceImpli customUserServiceImpli;
	private final VerificationService verificationService;

 
	   @PostMapping("/sent/login-otp")
	    public ResponseEntity<ApiResponse> sentLoginOtp(@RequestBody VerificationCode req) throws MessagingException, SellerException {
	        Seller seller = sellerService.getSellerByEmail(req.getEmail());
	        String otp = OtpUtil.generateOtp();
	        VerificationCode verificationCode = verificationService.createVerificationCode(otp, req.getEmail());

	        String subject = "FashPro Login Otp";
	        String text = "your login otp is - ";
	        emailService.sendVerificationOtpEmail(req.getEmail(), verificationCode.getOtp(), subject, text);

	        ApiResponse res = new ApiResponse();
	        res.setMessage("otp sent");
	        return new ResponseEntity<>(res, HttpStatus.CREATED);
	    }
	   @PostMapping("/verify/login-otp")
	   public ResponseEntity<AuthResponse> verifyLoginotp(@RequestBody VerificationCode req)throws MessagingException, SellerException{
			String email = req.getEmail();
			String otp =req.getOtp();
			
			VerificationCode verificationCode = verificationCodeRepository.findByEmail(email);
			if(verificationCode==null ) {
				throw new SellerException ("Wrong OTP...");
			}
			// check Expiration
			if(verificationCode.getExpiryTime().isBefore(LocalDateTime.now())) {
				verificationCodeRepository.deleteByEmail(email);
				throw new SellerException("OTP Expired");
			}
			//checks attempts count
			if (verificationCode.getAttemptCount()>=5) {
				throw new SellerException ("Too many OTP attempts");
			}
			// check OTP verification
			if(!verificationCode.getOtp().equals(otp)) {
				verificationCode.setAttemptCount(verificationCode.getAttemptCount()+1);
				verificationCodeRepository.save(verificationCode);
				throw new SellerException("Invalid OTP");
			}
				 Authentication authentication = authenticate(req.getEmail());
			        SecurityContextHolder.getContext().setAuthentication(authentication);

			        String token = jwtProvider.generateToken(authentication);
			        AuthResponse authResponse = new AuthResponse();

			        authResponse.setMessage("Login Success");
			        authResponse.setJwt(token);
			        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();


			        String roleName = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();


			        authResponse.setRole(UserRole.valueOf(roleName));

			        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);
			    
	   }
	   private Authentication authenticate(String username) {
	        UserDetails userDetails = customUserServiceImpli.loadUserByUsername("seller_" + username);

	        System.out.println("sign in userDetails - " + userDetails);

	        if (userDetails == null) {
	            System.out.println("sign in userDetails - null " + userDetails);
	            throw new BadCredentialsException("Invalid username or password");
	        }

	        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	    }
	   @PatchMapping("/verify/{otp}")
	    public ResponseEntity<Seller> verifySellerEmail(@PathVariable String otp) throws Exception {


	        VerificationCode verificationCode = verificationCodeRepository.findByOtp(otp);

	        if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {
	            throw new Exception("wrong otp...");
	        }

	        Seller seller = sellerService.verifyEmail(verificationCode.getEmail(), otp);

	        return new ResponseEntity<>(seller, HttpStatus.OK);
	    }


	    @PostMapping
	    public ResponseEntity<Seller> createSeller(@RequestBody Seller seller) throws SellerException, MessagingException {
	        Seller savedSeller = sellerService.createSeller(seller);

	        String otp = OtpUtil.generateOtp();
	        VerificationCode verificationCode = verificationService.createVerificationCode(otp, seller.getEmail());

	        String subject = "Zosh Bazaar Email Verification Code";
	        String text = "Welcome to Zosh Bazaar, verify your account using this link ";
	        String frontend_url = "http://localhost:3000/verify-seller/";
	        emailService.sendVerificationOtpEmail(seller.getEmail(), verificationCode.getOtp(), subject, text + frontend_url);
	        return new ResponseEntity<>(savedSeller, HttpStatus.CREATED);
	    }

	    @GetMapping("/{id}")
	    public ResponseEntity<Seller> getSellerById(@PathVariable Long id) throws SellerException {
	        Seller seller = sellerService.getSellerById(id);
	        return new ResponseEntity<>(seller, HttpStatus.OK);
	    }

	    @GetMapping("/profile")
	    public ResponseEntity<Seller> getSellerByJwt(
	            @RequestHeader("Authorization") String jwt) throws Exception {
	        Seller seller = sellerService.getSellerProfile(jwt);
	        return new ResponseEntity<>(seller, HttpStatus.OK);
	    }

		/*
		 * @GetMapping("/report") public ResponseEntity<SellerReport> getSellerReport(
		 * 
		 * @RequestHeader("Authorization") String jwt) throws SellerException { String
		 * email = jwtProvider.getEmailFromJwtToken(jwt); Seller seller =
		 * sellerService.getSellerByEmail(email); SellerReport report =
		 * sellerReportService.getSellerReport(seller); return new
		 * ResponseEntity<>(report, HttpStatus.OK); }
		 */

	    @GetMapping
	    public ResponseEntity<List<Seller>> getAllSellers(
	            @RequestParam(required = false) AccountStatus status) {

	        if (status == null) {
	            return ResponseEntity.ok(sellerRepository.findAll());
	        }
	        List<Seller> sellers = sellerService.getAllSellers(status);
	        
	        return ResponseEntity.ok(sellers);
	    }

	    @PatchMapping()
	    public ResponseEntity<Seller> updateSeller(
	            @RequestHeader("Authorization") String jwt, @RequestBody Seller seller) throws SellerException {

	        Seller profile = sellerService.getSellerProfile(jwt);
	        Seller updatedSeller = sellerService.updateSeller(profile.getId(), seller);
	        return ResponseEntity.ok(updatedSeller);

	    }

	    @DeleteMapping("/{id}")
	    public ResponseEntity<Void> deleteSeller(@PathVariable Long id) throws SellerException {

	        sellerService.deleteSeller(id);
	        return ResponseEntity.noContent().build();

	    }
	}



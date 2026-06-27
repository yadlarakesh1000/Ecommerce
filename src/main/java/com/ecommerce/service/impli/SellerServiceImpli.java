package com.ecommerce.service.impli;

import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.Exception.SellerException;
import com.ecommerce.config.JwtProvider;
import com.ecommerce.domain.AccountStatus;
import com.ecommerce.domain.UserRole;
import com.ecommerce.models.Address;
import com.ecommerce.models.Seller;
import com.ecommerce.models.VerificationCode;
import com.ecommerce.repository.AddressRepository;
import com.ecommerce.repository.SellerRepository;
import com.ecommerce.repository.VerificationCodeRepository;
import com.ecommerce.service.SellerService;

import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SellerServiceImpli implements SellerService {
    private final VerificationCodeRepository verificationCodeRepository;
    private final PasswordEncoder passwordEncoder;

     private final JwtProvider jwtProvider;
     private final SellerRepository sellerRepository;
     private final AddressRepository addressRepository;


	@Override
	public Seller getSellerProfile(String jwt) throws SellerException {
		 if(jwt.startsWith("Bearer ")) {
			 jwt=jwt.substring(7);
		 }
		 Claims claims=jwtProvider.extractClaims(jwt);
		 String email=jwtProvider.getEmailFromJwtToken(claims);
		 Seller seller = this.getSellerByEmail(email);
		return seller;
	}

	@Override
	public Seller createSeller(Seller seller) throws SellerException {
		     Seller isExist= sellerRepository.findByEmail(seller.getEmail());
		     if(isExist!=null) {
		    	 throw new SellerException("Seller already Exist with this Email");
		     }
		     Address savedAddress = addressRepository.save(seller.getPickupAddress());
		     Seller newSeller = new Seller();
		     newSeller.setEmail(seller.getEmail());
		     newSeller.setPickupAddress(savedAddress);
		     newSeller.setBankDetails(seller.getBankDetails());
		     newSeller.setBusinessDetails(seller.getBusinessDetails());
		     newSeller.setGSTIN(seller.getGSTIN());
		     newSeller.setMobile(seller.getMobile());
		    newSeller.setSellerName(seller.getSellerName());
		    newSeller.setRole(UserRole.ROLE_SELLER);
		    newSeller.setPassword(passwordEncoder.encode(seller.getPassword()));
		    System.out.println(newSeller);
		    return sellerRepository.save(newSeller);
		     
	}

	@Override
	public Seller getSellerById(Long id) throws SellerException {
		
		return sellerRepository.findById(id).orElseThrow(()->new SellerException("Seller not found with the id"));
	}

	@Override
	public Seller getSellerByEmail(String email) throws SellerException {
		      Seller seller = sellerRepository.findByEmail(email);
		      if(seller==null) {
		    	  throw new SellerException("Seller not found with this Email"+email);
		      }
		return seller;
	}

	@Override
	public List<Seller> getAllSellers(AccountStatus status) {
		       
		return sellerRepository.findByAccountStatus(status);
	}

	@Override
	public Seller updateSeller(Long id, Seller seller) throws SellerException {
		     Seller isExistSeller = this.getSellerById(id);
		     if(seller.getSellerName()!=null) {
		    	 isExistSeller.setSellerName(seller.getSellerName());
		     }
		     if (seller.getBankDetails() != null
		                && seller.getBankDetails().getAccountHolderName() != null
		                && seller.getBankDetails().getIfscCode() != null
		                && seller.getBankDetails().getAccountNumber() != null
		        ) {

		            isExistSeller.getBankDetails().setAccountHolderName(
		                    seller.getBankDetails().getAccountHolderName()
		            );
		           isExistSeller.getBankDetails().setAccountNumber(
		                    seller.getBankDetails().getAccountNumber()
		            );
		            isExistSeller.getBankDetails().setIfscCode(
		                    seller.getBankDetails().getIfscCode()
		            );
		        }
		     if(seller.getBusinessDetails()!=null && seller.getBusinessDetails().getBusinessName()!=null) {
		    	 isExistSeller.getBusinessDetails().setBusinessName(seller.getBusinessDetails().getBusinessName());
		     }
		     if(seller.getMobile()!=null) {
		    	 isExistSeller.setMobile(seller.getMobile());
		     }
		     if(seller.getPickupAddress()!=null 
		    	 && seller.getPickupAddress().getAddress()!=null 
		    	 && seller.getPickupAddress().getCity()!=null
		    	 && seller.getPickupAddress().getState()!=null
		    	 && seller.getPickupAddress().getMobile()!=null
		    	 ) { 
		    	 isExistSeller.getPickupAddress().setAddress(seller.getPickupAddress().getAddress());
		    	 isExistSeller.getPickupAddress().setCity(seller.getPickupAddress().getCity());
		    	 isExistSeller.getPickupAddress().setState(seller.getPickupAddress().getState());
		    	 isExistSeller.getPickupAddress().setMobile(seller.getPickupAddress().getMobile());
		     }        
		return sellerRepository.save(isExistSeller);
	}

	@Override
	public void deleteSeller(Long id) throws SellerException {
		     if(sellerRepository.existsById(id)) {
		    	 sellerRepository.deleteById(id);
		     }
		     else {
		    	 throw new SellerException("seller not found with this id"+ id);
		     }
		     
		
	}

	@Override
	public Seller verifyEmail(
	String email,
	String otp
	)throws SellerException{
	Seller seller=
	sellerRepository.findByEmail(email);


	VerificationCode code=

	verificationCodeRepository.findByEmail(email);


	if(code==null){
	throw new SellerException(
	"OTP not found"
	);
	}


	if(!code.getOtp().equals(otp)){

	throw new SellerException(
	"Invalid OTP"
	);

	}


	seller.setEmailVerified(true);
	seller.setAccountStatus(AccountStatus.ACTIVE);


	verificationCodeRepository
	.deleteByEmail(email);


	return sellerRepository.save(seller);
	}

	@Override
	public Seller updateSellerAccountStatus(Long sellerId, AccountStatus status) throws SellerException {
		                          Seller seller = this.getSellerById(sellerId);
		                          seller.setAccountStatus(status);
		return sellerRepository.save(seller);
	}

}

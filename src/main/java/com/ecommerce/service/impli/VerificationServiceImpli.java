package com.ecommerce.service.impli;

import org.springframework.stereotype.Service;

import com.ecommerce.models.VerificationCode;
import com.ecommerce.repository.VerificationCodeRepository;
import com.ecommerce.service.VerificationService;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class VerificationServiceImpli implements VerificationService {

    private final VerificationCodeRepository verificationCodeRepository;


    @Override
    public VerificationCode createVerificationCode(String otp,String email) {
        VerificationCode isExist=verificationCodeRepository.findByEmail(email);

        if(isExist!=null) {
            verificationCodeRepository.delete(isExist);
        }

        VerificationCode verificationCode=new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(email);

        return verificationCodeRepository.save(verificationCode);

    }
}

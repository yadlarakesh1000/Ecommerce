package com.ecommerce.service.impli;

import org.springframework.stereotype.Service;

import com.ecommerce.models.Seller;
import com.ecommerce.models.SellerReport;
import com.ecommerce.repository.SellerReportRepository;
import com.ecommerce.service.SellerReportService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SellerReportImpli  implements SellerReportService{

	 private final SellerReportRepository sellerReportRepository;


	    @Override
	    public SellerReport getSellerReport(Seller seller) {
	        SellerReport report = sellerReportRepository.findBySellerId(seller.getId());
	        if(report == null){
	            SellerReport newReport = new SellerReport();
	            newReport.setSeller(seller);
	            return sellerReportRepository.save(newReport);
	        }
	        return report;
	    }


	    @Override
	    public SellerReport updateSellerReport(SellerReport sellerReport) {

	        return sellerReportRepository.save(sellerReport);
	    }

	}

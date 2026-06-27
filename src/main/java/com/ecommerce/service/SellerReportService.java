package com.ecommerce.service;

import com.ecommerce.models.Seller;
import com.ecommerce.models.SellerReport;

public interface SellerReportService {
    SellerReport getSellerReport(Seller seller);
    SellerReport updateSellerReport( SellerReport sellerReport);

}

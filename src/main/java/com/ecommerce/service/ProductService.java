package com.ecommerce.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.ecommerce.Exception.ProductException;
import com.ecommerce.models.Product;
import com.ecommerce.models.Seller;
import com.ecommerce.request.CreateProductRequest;


public interface ProductService {

	public Product createProduct(CreateProductRequest req,Seller seller) throws ProductException;
	public void deleteProduct(Long productId) throws ProductException;
	public Product updateProduct(Long productId,Product product) ;
	List<Product> searchProducts(String query);
	public Product findProductById(Long productId) throws ProductException;
	 public Page<Product> getAllProduct(String category,
             String brand,
             String colors,
             String sizes,
             Integer minPrice,
             Integer maxPrice,
             Integer minDiscount,
             String sort,
             String stock,
             Integer pageNumber);

public List<Product> recentlyAddedProduct();
List<Product> getProductBySellerId(Long sellerId);
public Product updateProductStock(Long productId)throws ProductException;


}


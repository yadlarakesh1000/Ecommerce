package com.ecommerce.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.response.ApiResponse;

@RestController
public class HomeController {
      @GetMapping
	public ApiResponse home () {
    	  ApiResponse apiresponse = new ApiResponse();
    	  apiresponse.setMessage("welcome to fashionpro");
		return apiresponse;
	}

}

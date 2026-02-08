package com.ecommerce.models;

import com.ecommerce.domain.HomeCategorySection;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HomeCategory {

	   @Id
	   @GeneratedValue
	   (strategy = GenerationType.IDENTITY)
	   private Long id;
	   
	   private String name;
	   private String image;
	   private String categoryId;
	   private HomeCategorySection section;
	   
	   
}

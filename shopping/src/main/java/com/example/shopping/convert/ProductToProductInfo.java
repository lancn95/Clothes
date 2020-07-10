package com.example.shopping.convert;

import java.util.ArrayList;
import java.util.List;

import com.example.shopping.entities.Product;
import com.example.shopping.model.ProductInfo;

public class ProductToProductInfo {
	@SuppressWarnings("null")
	public static List<ProductInfo> convertProductInfo(List<Product> products) {
		List<ProductInfo> productInfos = new ArrayList<>();
		
		if(products != null && products.size() > 0) {
			for(Product product : products) {
				ProductInfo productInfo = new ProductInfo(product);
				if(productInfo != null) {
					productInfos.add(productInfo);
				}
			}
			
		}
		
		return productInfos;
		
	}
}

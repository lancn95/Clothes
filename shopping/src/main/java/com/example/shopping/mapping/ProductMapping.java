package com.example.shopping.mapping;

import java.util.ArrayList;
import java.util.List;

import com.example.shopping.entities.Product;
import com.example.shopping.model.ProductInfo;

public class ProductMapping {
	public static ProductInfo ProductToInfo(Product product) {
		if (product != null) {
			ProductInfo proInfo = new ProductInfo();
			proInfo.setCode(product.getCode());
			proInfo.setName(product.getName());
			proInfo.setDescription(product.getDescription());
			proInfo.setImage(product.getImage());
			proInfo.setPrice(product.getPrice());
			proInfo.setCreateDate(product.getCreateDate());
			proInfo.setCategory_id(product.getCategory().getId());
			return proInfo;
		}

		return null;
	}

	public static List<ProductInfo> productsToInfos(List<Product> products) {
		List<ProductInfo> productInfos = new ArrayList<>();
		if (products.size() > 0 && products != null) {
			for (Product product : products) {
				ProductInfo productInfo = ProductToInfo(product);
				productInfos.add(productInfo);
			}
		}
		return productInfos;
	}
}

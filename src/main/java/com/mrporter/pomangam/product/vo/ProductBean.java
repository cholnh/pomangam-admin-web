package com.mrporter.pomangam.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ProductBean
 * 
 * @version 1.0 [2018. 8. 15.]
 * @author Choi
 */
@Data @AllArgsConstructor @NoArgsConstructor
public class ProductBean {
	Integer idx, idx_restaurant, type;
	String name;
	Integer price;
	String imgpath, description;
	Integer unit_amount, unit_time, cnt_sell;
}

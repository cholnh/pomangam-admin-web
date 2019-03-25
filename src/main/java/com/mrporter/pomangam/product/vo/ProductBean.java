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
	/*
	Integer idx, idx_restaurant, type;
	String name;
	Integer price;
	String imgpath, description;
	Integer unit_amount, unit_time, cnt_sell;
	*/
	Integer idx, idx_restaurant;
	String category, name;
	Integer price;
	String imgpath, description;
	Integer unit_amount, unit_time, cnt_sell;
	Integer isActive, type; // 1 : main  / 2 : sub
	Integer sequence;
	String sub_description;
	Integer c_commission_prc;
	Integer s_commission_prc;
	Integer discount_prc;
}

package com.mrporter.pomangam.restaurant.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * RestaurantBean
 * 
 * @version 1.0 [2018. 8. 15.]
 * @author Choi
 */
@Data @AllArgsConstructor @NoArgsConstructor
public class RestaurantBean {
	Integer idx, idx_target;
	String name, location, tel, description, imgpath;
	Integer cnt_star, cnt_comment;
}

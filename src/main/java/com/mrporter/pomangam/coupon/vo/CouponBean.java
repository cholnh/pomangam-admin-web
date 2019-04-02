package com.mrporter.pomangam.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CouponBean
 * 
 * @version 1.0 [2019. 3. 25.]
 * @author Choi
 */
@Data @AllArgsConstructor @NoArgsConstructor
public class CouponBean {
	Integer idx;
	String cpname;
	String cpno;
	String reg_username, use_username;
	String register_date, modify_date; 
	Integer state_active;
	String start_date, end_date;
	Integer availability;
	Integer discount_prc;
}

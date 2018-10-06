package com.mrporter.pomangam.target.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TargetBean
 * 
 * @version 1.0 [2018. 8. 10.]
 * @author Choi
 */
@Data @AllArgsConstructor @NoArgsConstructor
public class TargetBean {
	Integer idx;
	String name, location, imgpath, category, description;
	Integer cnt_order, cnt_subscription;
}
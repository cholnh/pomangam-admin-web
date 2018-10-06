package com.mrporter.pomangam.member.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AdminBean
 * 
 * @version 1.0 [2018. 3. 29.]
 * @author Choi
 */
@Data @AllArgsConstructor @NoArgsConstructor
public class AdminBean {
	Integer idx;
	String username, password, nickname;
	String moddate, role;
	Integer idx_restaurant;
}

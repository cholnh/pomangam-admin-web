package com.mrporter.pomangam.member.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AuthorityBean
 * 
 * @version 1.0 [2018. 3. 29.]
 * @author Choi
 */
@Data @AllArgsConstructor @NoArgsConstructor
public class AuthorityBean {
	Integer idx;
	String authname;
	Integer type;
	String authority;
}

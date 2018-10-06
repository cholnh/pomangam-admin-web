package com.mrporter.pomangam.member.vo;

import java.util.Date;

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
public class MemberBean {
	Integer idx;
	String username, password, name, nickname;
	Integer idx_target, idx_subscribe;
	String regdate, moddate, tel, session_key;
	Date session_limit;
	
}

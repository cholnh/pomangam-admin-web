package com.mrporter.pomangam.order.vo;

import lombok.Data;

/**
 * ApiResultVO
 * 
 * @version 1.0 [2019. 3. 7.]
 * @author Choi
 */
@Data
public class ApiResultBean {
	String code;
	Data data;
	String message;
	
	class Data {
		String msgid;
	}
}


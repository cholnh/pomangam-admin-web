package com.mrporter.pomangam.order.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PaymentIndexBean
 * 
 * @version 1.0 [2018. 8. 19.]
 * @author Choi
 */
@Data @AllArgsConstructor @NoArgsConstructor
public class PaymentIndexBean {
	Integer idx;
	String idxes_payment, password, timestamp;
	Integer idx_box, idx_target;
	String receive_date, receive_time;
	Integer status; // 0 : 결제 대기, 1 : 결제 완료, 2 : 결제 실패
	String username, location, phonenumber;
}

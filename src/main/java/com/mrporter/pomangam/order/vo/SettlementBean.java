package com.mrporter.pomangam.order.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class SettlementBean {
	
	String res_name;	// 업체 이름
	Integer amount;		// 총 판매 수량
	Integer sales;		// 총 매출 (원)
	Integer commission;	// 총 수수료 (원)
	Integer purchase;	// 총 매입 (원)
	
}

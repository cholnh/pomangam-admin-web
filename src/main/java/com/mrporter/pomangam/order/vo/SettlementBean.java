package com.mrporter.pomangam.order.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class SettlementBean {
	String res_name;
	Integer amount;
	String additional; 
	Integer price;
	Integer c_commission_prc;
	Integer s_commission_prc;
}

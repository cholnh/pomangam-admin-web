package com.mrporter.pomangam.order.dao;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.mrporter.pomangam.common.pattern.dao.Crud;
import com.mrporter.pomangam.order.vo.PaymentBean;

/**
 * PaymentCrudDAO
 * 
 * @version 1.0 [2018. 8. 15.]
 * @author Choi
 */
public class PaymentCrudDAO extends Crud<PaymentBean> {
	private static final String TABLENAME = "payment"; 
	
	/**
	 * @param tableName
	 */
	public PaymentCrudDAO() {
		super(TABLENAME);
	}
	
	public String test() throws Exception {
		List<Map<String, Object>> lom 
		= sqlQuery(
				"SELECT pi.idx, m.name, pi.phonenumber FROM payment_index pi, member m WHERE pi.username = m.username GROUP BY pi.phonenumber HAVING count(*) >= 1;");
		
		return new Gson().toJson(lom);
	}
}

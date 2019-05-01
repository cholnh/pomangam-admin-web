package com.mrporter.pomangam.order.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mrporter.pomangam.common.pattern.dao.Crud;
import com.mrporter.pomangam.common.util.BizmApi;
import com.mrporter.pomangam.common.util.Date;
import com.mrporter.pomangam.order.vo.ApiResultBean;
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
	
	public static void main(String...args) {
		try {
			//new PaymentCrudDAO().sendDeliveryArrive("17시", "기숙사 식당 (도착시간 +10분)");
			// new PaymentCrudDAO().sendDeliveryDelay(10, "똥싸느라", "19시", "학생회관 뒤");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendOrderMsg(int paymentIndex) throws Exception {
		PaymentIndexCrudDAO indexDAO = new PaymentIndexCrudDAO();
		
		List<Map<String, Object>> lom 
		= sqlQuery(
				"SELECT * FROM payment_index WHERE idx = ?;", paymentIndex);
		
		if(lom != null  && !lom.isEmpty()) {
			String tmplId = "pmg_admin_3";
			String idx_box = lom.get(0).get("idx_box")+"";
			String receive_date = lom.get(0).get("receive_date")+"";
			String receive_time = lom.get(0).get("receive_time")+"";
			String phonenumber = lom.get(0).get("phonenumber")+"";
			String where = lom.get(0).get("where")+"";
			switch(where) {
			case "학생회관 뒤":
				where = "ㅎ";
				break;
			case "기숙사 식당 (도착시간 +10분)":
				where = "ㄱ";
				break;
			case "기숙사 정문":
				where = "기";
				break;
			case "제2학생회관 (도착시간 +5분)":
				where = "학";
				break;
			case "아카데미홀 (도착시간 +10분)":
				where = "아";
				break;
			}
			//where = where.equals("") ? "ㄱ" : "ㅎ";
			int approvalTime = Integer.parseInt(receive_time.replace("시", "")) - 1;
			
			List<Map<String, Object>> lom2 
			= sqlQuery(
					"SELECT idx_restaurant FROM payment WHERE idx_payment_index = ? group by idx_restaurant;", paymentIndex);
			
			String text = 	"주문번호 : " + where + "-" + idx_box + " (no." + paymentIndex + ")" + System.lineSeparator() +
					"배달시간 : " + receive_date + " (" + receive_time + " 00분)" + System.lineSeparator() +
					"※ " + approvalTime + "시 40~45분에 방문 예정 ※" + System.lineSeparator() +
					"------------------------" + System.lineSeparator();
					
			
			for(Map<String, Object> map : lom2) {
				
				String idx_restaurant = map.get("idx_restaurant")+"";
				//System.out.println("idx_restaurant : " + idx_restaurant);
				List<Map<String, Object>> lom21
				= sqlQuery(
						"SELECT pm.idx as idx, pd.name as name, pm.amount as amount, pm.additional as additional, pm.idx_restaurant as idx_restaurant, pm.requirement as requirement FROM payment pm, product pd " + 
						"WHERE pm.idx_product = pd.idx and pm.idx_payment_index = ? AND pm.idx_restaurant = ?", paymentIndex, idx_restaurant);
				
				String menu = "";
				List<String> idxes = new ArrayList<>();
				for(Map<String, Object> map2 : lom21) {
					String idx = map2.get("idx")+"";
					idxes.add(idx);
					
					String name = map2.get("name")+"";
					int amount = Integer.parseInt(map2.get("amount")+"");
					String additional = map2.get("additional")+"";
					String requirement = map2.get("requirement")+"";
					
					String add = "";
					String[] adds = additional.split(",");
					for(int i=0; i<adds.length; i++) {
						String parts = adds[i];
						String[] p = parts.split("-");
						if(p.length >= 4) {
							add += "추가" + (parts.length()==1?"":(i+1)) + " : " + p[3] + " - " + (Integer.parseInt(p[1])*amount) + "개\n";
						}
					}
					
					menu += "품명 : [ " + name + " ] - " + amount + "개" + System.lineSeparator() + 
							(add.length()>0?add:"") + System.lineSeparator() + 
							(requirement.length()>0?"요청사항 : " + requirement + System.lineSeparator() : "") +
							"------------------------" + System.lineSeparator();
				}
				
				menu += "고객번호 : " + phonenumber;
				
				List<Map<String, Object>> lom4
				= sqlQuery(
						"SELECT phone_number FROM restaurant_phone where idx_restaurant = ? and isActive = 1;", idx_restaurant);
				if(lom4 != null  && !lom4.isEmpty()) {
					
					for(int i=0; i<lom4.size(); i++) {
						String phone_number = lom4.get(i).get("phone_number") + "";
						//System.out.println("phone_number : " + phone_number);
						//System.out.println("text : " + text + menu);
						
						Object obj = BizmApi.send(phone_number, text + menu, tmplId).getBody();
						Gson gson = new Gson();
						List<ApiResultBean> bean = gson.fromJson(obj+"", new TypeToken<List<ApiResultBean>>() {}.getType());
						if(!bean.get(0).getCode().equals("success")) {
							// fail
							indexDAO.setStatus(6, paymentIndex);
						} else {
							for(String idx : idxes) {
								indexDAO.setOrderStatus(1, Integer.parseInt(idx));
							}
						}
					}
				} else {
					indexDAO.setStatus(6, paymentIndex);
				}
			}
			
			
			
		}
	}
	
	public void sendDeliveryDelay(int delay_min, String delay_reason ,String receive_time, String where, String curTarget) throws Exception {
		String tmplId = "pmg_delivery_delay_1";
		List<Map<String, Object>> lom = null;
		
		if(where == null) {
			if(curTarget == null || curTarget.isEmpty() || curTarget.equals("0")) {
				lom = sqlQuery(
						"SELECT " +
								"    phonenumber, pi.where " +
								"FROM " +
								"    payment_index pi " +
								"WHERE " +
								"    receive_date = CURDATE() " +
								"    AND receive_time = ? ", receive_time);
			} else {
				lom = sqlQuery(
						"SELECT " +
								"    phonenumber, pi.where " +
								"FROM " +
								"    payment_index pi " +
								"WHERE " +
								"    receive_date = CURDATE() " +
								"    AND receive_time = ? AND idx_target = ? ", receive_time, curTarget);
			}
			
			
		} else {
			
			if(curTarget == null || curTarget.isEmpty() || curTarget.equals("0")) {
				lom = sqlQuery(
						"SELECT " +
								"    phonenumber, pi.where " +
								"FROM " +
								"    payment_index pi " +
								"WHERE " +
								"    receive_date = CURDATE() " +
								"    AND receive_time = ? " +
								"    AND pi.where = ? ", receive_time, where);
			} else {
				lom = sqlQuery(
						"SELECT " +
								"    phonenumber, pi.where " +
								"FROM " +
								"    payment_index pi " +
								"WHERE " +
								"    receive_date = CURDATE() " +
								"    AND receive_time = ? " +
								"    AND pi.where = ? AND idx_target = ? ", receive_time, where, curTarget);
			}
			
		}
		
		if(lom != null  && !lom.isEmpty()) {
			
			String info = 	"포만감의 소식은 플친에서 확인!" + System.lineSeparator() +
							"■ 문의 : 플친 → [채팅하기]" + System.lineSeparator() +
							"http://pf.kakao.com/_xlxbhlj\n";  			
			
			for(Map<String, Object> map : lom) {
				int delay_total = 0;
				
				if(where == null) {
					// 전체 전송
					String w = map.get("where") + "";
					if(w.equals("학생회관 뒤") || w.equals("기숙사 정문")) {
						delay_total += delay_min;
					} else if(w.equals("제2학생회관 (도착시간 +5분)")) {
						delay_total += delay_min + 5;
					} else {
						delay_total += delay_min + 10;
					}
				} else {
					// 특정 장소 만 전송
					if(where.equals("학생회관 뒤") || where.equals("기숙사 정문")) {
						delay_total += delay_min;
					} else if(where.equals("제2학생회관 (도착시간 +5분)")) {
						delay_total += delay_min + 5;
					} else {
						delay_total += delay_min + 10;
					}
				}
				String text = 	"[포만감 배달 지연 안내]" + System.lineSeparator() +
								"배달 도착시간이 약 " + delay_min + "분 간 지연되어 안내드립니다." + System.lineSeparator() +
									System.lineSeparator() +
								"▷ 사유 : " + delay_reason + System.lineSeparator() +
								"▷ 예상 도착 시간 : " + receive_time + " " + delay_total + "분" + System.lineSeparator() +
									System.lineSeparator() +
								"[안내문]" + System.lineSeparator() +
								"■ " + info;
				
				String phonenumber = map.get("phonenumber") + "";
				Object obj = BizmApi.send(phonenumber, text, tmplId).getBody();
				Gson gson = new Gson();
				List<ApiResultBean> bean = gson.fromJson(obj+"", new TypeToken<List<ApiResultBean>>() {}.getType());
				if(!bean.get(0).getCode().equals("success")) {
					// fail
				}
			}
		}
    }
	
	
	public void sendDeliveryArrive(String receive_time, String where, String curTarget) throws Exception {
		String tmplId = "pmg_delivery_arrive_1";
		List<Map<String, Object>> lom = null;
		
		if(where == null) {
			if(curTarget == null || curTarget.isEmpty() || curTarget.equals("0")) {
				lom = sqlQuery(
						"SELECT " +
								"    phonenumber, pi.where " +
								"FROM " +
								"    payment_index pi " +
								"WHERE " +
								"    receive_date = CURDATE() " +
								"    AND receive_time = ? ", receive_time);
			} else {
				lom = sqlQuery(
						"SELECT " +
								"    phonenumber, pi.where " +
								"FROM " +
								"    payment_index pi " +
								"WHERE " +
								"    receive_date = CURDATE() " +
								"    AND receive_time = ? AND idx_target = ? ", receive_time, curTarget);
			}
			
		} else {
			if(curTarget == null || curTarget.isEmpty() || curTarget.equals("0")) {
				lom = sqlQuery(
						"SELECT " +
								"    phonenumber, pi.where " +
								"FROM " +
								"    payment_index pi " +
								"WHERE " +
								"    receive_date = CURDATE() " +
								"    AND receive_time = ? " +
								"    AND pi.where = ? ", receive_time, where);
			} else {
				lom = sqlQuery(
						"SELECT " +
								"    phonenumber, pi.where " +
								"FROM " +
								"    payment_index pi " +
								"WHERE " +
								"    receive_date = CURDATE() " +
								"    AND receive_time = ? " +
								"    AND pi.where = ? AND idx_target = ? ", receive_time, where, curTarget);
			}
			
		}
		
		if(lom != null  && !lom.isEmpty()) {
			
			String info = 	"■ 포만감의 소식은 플친에서 확인!" + System.lineSeparator() +
							"■ 문의 : 플친 → [채팅하기]" + System.lineSeparator() +
							"http://pf.kakao.com/_xlxbhlj\n";  			
			
			for(Map<String, Object> map : lom) {
				String w = (where==null?(map.get("where")+""):where);
				if(w.contains("(")) {
					w = w.substring(0, w.indexOf("("));
				}
				
				String text = 	"[포만감 도착 안내]" + System.lineSeparator() +
		    			"주문하신 음식이 '곧 도착' 합니다." + System.lineSeparator() +
		    			"수령 장소 : " + w + System.lineSeparator() + System.lineSeparator();
				
				String phonenumber = map.get("phonenumber") + "";
				
				Object obj = BizmApi.send(phonenumber, text+info, tmplId).getBody();
				Gson gson = new Gson();
				List<ApiResultBean> bean = gson.fromJson(obj+"", new TypeToken<List<ApiResultBean>>() {}.getType());
				if(!bean.get(0).getCode().equals("success")) {
					// fail
				}
			}
		}
    }
	
	public void sendFailMsg(int paymentIndex, String orderStatus) throws Exception {
		PaymentIndexCrudDAO indexDAO = new PaymentIndexCrudDAO();
		
		List<Map<String, Object>> lom 
		= sqlQuery(
				"SELECT * FROM payment_index WHERE idx = ?;", paymentIndex);
		
		if(lom != null  && !lom.isEmpty()) {
			String idxes = lom.get(0).get("idxes_payment")+"";
			String idx_box = lom.get(0).get("idx_box")+"";
			
			for(String idx : idxes.split(",")) {
				List<Map<String, Object>> lom2
				= sqlQuery(
						"SELECT pd.name as name, pm.amount as amount, pm.additional as additional, pm.idx_restaurant as idx_restaurant FROM payment pm, product pd WHERE pm.idx = ? and pm.idx_product = pd.idx;", idx);
				String name = lom2.get(0).get("name")+"";
				String amount = lom2.get(0).get("amount")+"";
				String additional = lom2.get(0).get("additional")+"";
				String idx_restaurant = lom2.get(0).get("idx_restaurant")+"";
				int lenAdd = additional.length() == 0 
						? 0 
						: additional.split(",").length;
				String tmplId = "pmg_order_fail_1";
				String approvalDateTime = Date.getCurTime2();
				String detail = name + " - " + amount + "개" + (lenAdd>0 ? System.lineSeparator() + "그 외 " + lenAdd + "개" : "");

				String text = 	"[주문 " + orderStatus + " 안내]" + System.lineSeparator() +
								"주문번호 " + idx_box + " (no." + paymentIndex + ")" + System.lineSeparator() +
								orderStatus + " 승인시간 : " + approvalDateTime + System.lineSeparator() +
								System.lineSeparator() +
								"해당 품목이 [" + orderStatus + "] 되었습니다." + System.lineSeparator() +
								"------------------------" + System.lineSeparator() +
								"품명 : " + detail + System.lineSeparator() +
								"------------------------";
				List<Map<String, Object>> lom3
				= sqlQuery(
						"SELECT phone_number FROM restaurant_phone where idx_restaurant = ? and isActive = 1;", idx_restaurant);
				if(lom3 != null  && !lom3.isEmpty()) {
					for(int i=0; i<lom3.size(); i++) {
						String phone_number = lom3.get(i).get("phone_number") + "";
						//System.out.println("phone_number : " + phone_number);
						//System.out.println("text : " + text);
						
						Object obj = BizmApi.send(phone_number, text, tmplId).getBody();
						Gson gson = new Gson();
						List<ApiResultBean> bean = gson.fromJson(obj+"", new TypeToken<List<ApiResultBean>>() {}.getType());
						if(!bean.get(0).getCode().equals("success")) {
							// fail
							indexDAO.setStatus(6, paymentIndex);
						}
					}
				} else {
					indexDAO.setStatus(6, paymentIndex);
				}
			}
		}
	}
	
}

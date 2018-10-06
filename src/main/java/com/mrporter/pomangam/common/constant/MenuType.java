package com.mrporter.pomangam.common.constant;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * MenuType
 * 
 * Desc : Enum field(Database value, Visible value) 
 * 
 * @version 1.0 [2018. 3. 28.]
 * @author Choi
 */
@Getter @AllArgsConstructor @NoArgsConstructor
public enum MenuType {

	ORDER(0),
	PRODUCT(1),	
	PROFIT(2),	
	PORTER(3),	
	SETTLEMENT(4),
	MEMBER(5),
	OWNER(6),
	ADMIN(7),	
	AUTHORITY(8),
	TARGET(9),
	RESTAURANT(10); 
	

	private Integer dbValue;
	
	public static Map<Integer, String> getIdxMap() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		MenuType[] types = MenuType.values();
		for(MenuType type : types) {
			map.put(type.getDbValue(), type.name());
		}
		return map;
	}
	
	public static void main(String...args) {
		MenuType[] types = MenuType.values();
		for(MenuType type : types) {
			System.out.println(type);
		}
	}
}

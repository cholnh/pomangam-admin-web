package com.mrporter.pomangam.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * TableStatus
 * 
 * Desc : Enum field(Database value, Visible value) 
 * 
 * @version 1.0 [2018. 3. 28.]
 * @author Choi
 */
@Getter @AllArgsConstructor @NoArgsConstructor
public enum TableStatus {
	
	Y("1", "on"),	
	N("0", "off");
	
	private String dbValue;
	private String visibleValue;
	
	public static void main(String...args) {
		System.out.println(TableStatus.Y.getDbValue());
	}
}

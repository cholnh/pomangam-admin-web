package com.mrporter.pomangam.target.vo;

import java.sql.Time;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TargetDetailBean
 * 
 * @version 1.0 [2019. 9. 12.]
 * @author Choi
 */
@Data @AllArgsConstructor @NoArgsConstructor
public class TargetDetailBean {
	Integer idx, idx_target;
	String name;
	Time increasing_time;
	String target_name;
}
		
package com.mrporter.pomangam.order.vo;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;

/**
 * OrderTime
 * 
 * @version 1.0 [2019. 9. 11.]
 * @author Choi
 */

@AllArgsConstructor
public class OrderTimeBean {
	
	private LocalDate arrivalDate;
	private Time arrivalTime;
	private Time pickUpTime;
	private Time orderEndTime;
	
	/**
	 * @param arrivalTime
	 * @param pickUpTime
	 * @param orderEndTime
	 */
	
	/**
	 * @param arrivalDate
	 * @param arrivalTime
	 * @param pickUpTime
	 * @param orderEndTime
	 */
	public OrderTimeBean() {
		this.arrivalDate = LocalDate.now();
	}

	public LocalDate getArrivalDate() {
		return arrivalDate;
	}
	public LocalTime getArrivalTime() {
		return arrivalTime.toLocalTime();
	}
	public LocalTime getPickUpTime() {
		return pickUpTime.toLocalTime();
	}
	public LocalTime getOrderEndTime() {
		return orderEndTime.toLocalTime();
	}
	
	public long getArrivalDateInMillis() {
		LocalDateTime ldt = LocalDateTime.of(arrivalDate, this.getArrivalTime());
		ZonedDateTime zdt = ldt.atZone(ZoneId.systemDefault());
		return zdt.toInstant().toEpochMilli();
	}
	public void setArrivalDate(LocalDate arrivalDate) {
		this.arrivalDate = arrivalDate;
	}
	public OrderTimeBean nextDay() {
		this.arrivalDate = this.arrivalDate.plusDays(1);
		return this;
	}
	public OrderTimeBean adjustOrderEndTime(int seconds) {
		this.orderEndTime = Time.valueOf(this.getOrderEndTime().minusSeconds(seconds));
		return this;
	}
}

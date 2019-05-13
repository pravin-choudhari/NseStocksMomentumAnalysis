package com.analysis.momentum.model;

import java.time.LocalDate;
import java.util.Date;

public class PriceData implements Comparable<PriceData>{
	private LocalDate localDate;
	public LocalDate getLocalDate() {
		return localDate;
	}
	public void setLocalDate(LocalDate date) {
		this.localDate = date;
	}

	private double openPrice;
	private double highPrice;
	private double lowPrice;
	private double closePrice;
	private double adjClosePrice;
	private double volume;

public double getOpenPrice() {
		return openPrice;
	}
	public void setOpenPrice(double openPrice) {
		this.openPrice = openPrice;
	}
	public double getHighPrice() {
		return highPrice;
	}
	public void setHighPrice(double highPrice) {
		this.highPrice = highPrice;
	}
	public double getLowPrice() {
		return lowPrice;
	}
	public void setLowPrice(double lowPrice) {
		this.lowPrice = lowPrice;
	}
	public double getClosePrice() {
		return closePrice;
	}
	public void setClosePrice(double closePrice) {
		this.closePrice = closePrice;
	}
	public double getAdjClosePrice() {
		return adjClosePrice;
	}
	public void setAdjClosePrice(double adjClosePrice) {
		this.adjClosePrice = adjClosePrice;
	}
	public double getVolume() {
		return volume;
	}
	public void setVolume(double volume) {
		this.volume = volume;
	}

	public String toString() {
	return "openPrice: " + openPrice + " closePrice: " + closePrice;
	}
	
	@Override
	public int compareTo(PriceData o) {
		if (this.localDate.isAfter(o.getLocalDate())) {
			return 1;
		}
		if (this.localDate.isBefore(o.getLocalDate())) {
			return -1;
		}
		
		return 0;
	}
}

package com.analysis.momentum.model;

public class DataPoint {
private final double monthlyClosePrice;
private final int monthlyHighDays;

public DataPoint(double monthlyClosePrice, int monthlyHighDays) {
	this.monthlyClosePrice = monthlyClosePrice;
	this.monthlyHighDays = monthlyHighDays;
}

public double getMonthlyClosePrice() {
	return monthlyClosePrice;
}

public int getMonthlyHighDays() {
	return monthlyHighDays;
}
}
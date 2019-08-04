package com.analysis.momentum.analysis;

import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import com.analysis.momentum.model.DataPoint;
import com.analysis.momentum.model.PriceData;
import com.analysis.momentum.model.Stock;

public class Analysis {
	private final Map<YearMonth, LinkedList<PriceData>> monthToPriceData = new LinkedHashMap<>();
	private final Map<YearMonth,DataPoint> monthToDataPoint = new LinkedHashMap<>();
	PriceData previousDayPriceData = null;
	DataPoint prevDataPoint = null;
	int highDays;
	int highMonths;
	
public Analysis() {	
}

public Map<YearMonth, LinkedList<PriceData>> getMonthToPriceData() {
	return monthToPriceData;
}

public Map<YearMonth, DataPoint> getMonthToDataPoint() {
	return monthToDataPoint;
}

public void setMonthToPriceData(final Map<YearMonth, LinkedList<PriceData>> monthToPriceData) {
	this.monthToPriceData.clear();
	this.monthToPriceData.putAll(monthToPriceData);	
}
public void generateAnalysis(final Stock stock) {	
	monthToPriceData.forEach((month, priceDataForMonth) -> {
		final DataPoint dataPoint;
		
		priceDataForMonth.forEach(priceData-> {		
			if (previousDayPriceData != null) {
				if ( previousDayPriceData.getClosePrice() < priceData.getClosePrice()) {
					highDays++;
				}				
			}	

			previousDayPriceData = priceData;
		});
		
		dataPoint = new DataPoint(priceDataForMonth.getLast().getClosePrice(), highDays);
		monthToDataPoint.put(month, dataPoint);
		stock.setMonthToHighDays(month, highDays);
		stock.addHighDays(highDays);
		highDays=0;
	});
	
	monthToDataPoint.forEach((month,dataPoint)-> {
		if (prevDataPoint != null) {
		 if (prevDataPoint.getMonthlyClosePrice()  < dataPoint.getMonthlyClosePrice()) {
			 stock.incrementHighMonths();
		 }		 
		}		
		 prevDataPoint= dataPoint;
		
		//System.out.println( getMonthName(month) + "::" + dataPoint.getMonthlyClosePrice() + ":" + dataPoint.getMonthlyHighDays());
	});
	
	System.out.println("Stock: " + stock.getName() + " High Months: " + stock.getHighMonths() + " Total high days: " + stock.getTotalHighDays());
}

private String getMonthName(YearMonth month) {
	return month.getMonth().name() + "-" + month.getYear();
}
}

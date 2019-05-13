package com.analysis.momentum.model;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Stock {
	private String name;
	private final Map<YearMonth, LinkedList<PriceData>> monthToPriceData = new LinkedHashMap<>();
	private final List<YearMonth> months = new LinkedList<>();

	public String getName() {
		return name;
	}

	public Stock(String name) {
		this.name = name;
	}

	public void updateMonthlyBucketData(final PriceData priceData) {
		final LocalDate localDate = priceData.getLocalDate();
		final YearMonth yearMonth = YearMonth.of(localDate.getYear(), localDate.getMonth());
		LinkedList<PriceData> monthlyPriceData;

		if (!months.contains(yearMonth)) {
			months.add(yearMonth);
		}

		monthlyPriceData = monthToPriceData.computeIfAbsent(yearMonth, k -> new LinkedList<PriceData>());

		if (monthlyPriceData == null) {
			monthlyPriceData = monthToPriceData.get(yearMonth);
		}

		monthlyPriceData.add(priceData);
		Collections.sort(monthlyPriceData);
	}
	
	public List<YearMonth> getAllMonths() {
		return months;
	}
	
	public Map<YearMonth, LinkedList<PriceData>> getMonthToPriceData () {
		return monthToPriceData;
	}
}

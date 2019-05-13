package com.analysis.momentum.index.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.analysis.momentum.model.Stock;

public class IndexData {
private final String indexFileName;
//private final List<Stock> stocks = new ArrayList<>();
private final List<String> stocks = new ArrayList<>();
 
	public IndexData  (final String fileName) {
	this.indexFileName =fileName;
	}
	
	public List<String> getIndexData() {
		try {
			Stream<String> lines = Files.lines(Paths.get(indexFileName));
			
			lines.forEach(line -> {
				stocks.add(line.strip());
			});
			lines.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return stocks;	
	}
	
	public List<String> getYahooDataForStock(final String symbol) {
	return null;	
	}
	
}

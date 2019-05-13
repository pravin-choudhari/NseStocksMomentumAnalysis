/**
 * 
 */
package com.analysis.momentum.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.AsyncHttpClientConfig;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.Response;
import org.asynchttpclient.cookie.CookieStore;
import org.asynchttpclient.cookie.ThreadSafeCookieStore;

import com.analysis.momentum.index.data.IndexData;
import com.analysis.momentum.index.data.YahooFinance;
import com.analysis.momentum.model.PriceData;
import com.analysis.momentum.model.Stock;

/**
 * @author Sony
 *
 */
public class MainApp {
private static final String url3 = "https://query1.finance.yahoo.com/v7/finance/download/TCS.NS?period1=1525548751&period2=1557074807&interval=1d&events=history&crumb=";
private static final String url5 = "https://query1.finance.yahoo.com/v7/finance/download/RELIANCE.NS?period1=1525548751&period2=1557074807&interval=1d&events=history&crumb=";
private static final String url4 = "https://ca.finance.yahoo.com/quote/TCS.NS/history?p=TCS.NS";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final IndexData indexData = new IndexData("H://tmp/index1.txt");
			final YahooFinance yahooFinance = new YahooFinance();
			final Date endDate =  new Date();
			long year = 365L * 24L * 60L * 60L * 1000L;
			//final long startTime = (endDate.getTime() - year)/1000L;
			//final long endTime = endDate.getTime() / 1000L;
			
			indexData.getIndexData().forEach(symbol ->{
				//final Stock stock =yahooFinance.getPriceDataForStockFromYahoo(symbol, startTime, endTime);
				final Stock stock =yahooFinance.getPriceDataForStockFromFile(symbol);
				//final Map<LocalDate, PriceData> dateToPriceData = stock.getBhavDateToPriceData();
				
				List<YearMonth> months = stock.getAllMonths();
				Map<YearMonth, LinkedList<PriceData>> monthToPriceData = stock.getMonthToPriceData();
				
				months.forEach(m -> {
					System.out.println(m.getMonth().name() + "-" + m.getYear());
				});
				monthToPriceData.forEach((month, priceDataForMonth) -> {
					System.out.println("For " + month.getMonth().name() + "-" + month.getYear());
					System.out.println("Last day of month " + priceDataForMonth.getLast().getLocalDate());
				});
				/*dateToPriceData.forEach((localDate,priceData) -> {
					System.out.println(localDate);
					System.out.println(priceData.toString());
					Month month = localDate.getMonth();					
				});*/

				
				System.out.println("Completed for " + symbol);
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void getDataFromYahoo() throws IOException {
        URL url = new URL(url4);
        String pattern = "(.*)(\"CrumbStore\":\\{\"crumb\":\")(.*?)(\"\\})(.*)";
		final BufferedWriter bw = new BufferedWriter(new FileWriter("H://tmp/tcs.txt"));
		final BufferedWriter bw1 = new BufferedWriter(new FileWriter("H://tmp/reliance.txt"));
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);
        Pattern r = Pattern.compile(pattern);
	    

        
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("GET");
        conn.setReadTimeout(15*1000);
        conn.connect();
        
        BufferedReader  reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        StringBuilder sb = new StringBuilder();
        String line = null;
        
        while ((line = reader.readLine()) != null)
        {
          sb.append(line + "\n");
          
          
        }
                
        if (sb.toString().contains("crumb")) {
        	System.out.println("found");
        }
        
        Matcher m = r.matcher(sb.toString());
        
	      if (m.find( )) {
	          System.out.println("Found value of crumb: " + m.group(3) ); 
	       }

	      url = new URL(url3 + m.group(3) );	      
	      conn = (HttpURLConnection) url.openConnection();
	        conn.setDoOutput(true);
	        conn.setRequestMethod("GET");
	        conn.setReadTimeout(150*1000);
	        conn.connect();	        
	        reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        line = null;
	        int i =0;
	        while (i < 1000)
	        {
	        	line = reader.readLine();
	          //sb.append(line + "\n");
	        	if (line != null) {
	        		bw.write(line + "\n");	
	        	}
	        	
	        	i++;	        	
	        }
	        
	        bw.flush();
	        bw.close();


		      url = new URL(url5 + m.group(3) );	      
		      conn = (HttpURLConnection) url.openConnection();
		        conn.setDoOutput(true);
		        conn.setRequestMethod("GET");
		        conn.setReadTimeout(150*1000);
		        conn.connect();	        
		        reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		        line = null;
		        int j =0;
		        while (j < 1000)
		        {
		        	line = reader.readLine();
		          //sb.append(line + "\n");
		        	if (line != null) {
		        		bw1.write(line + "\n");	
		        	}
		        	
		        	j++;	        	
		        }
	        
		        bw1.flush();
		        bw1.close();
	}	
}

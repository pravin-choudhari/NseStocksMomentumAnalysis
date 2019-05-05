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
		System.out.println("Hello World");
		String sample = "dndnksndkndkn\"CrumbStore\":{\"crumb\":\"sMt9UQ80bWV\"}dldndldldnln\"}cccc";
		//String pattern = (.*)(\Q"CrumbStore":{"crumb":"\E)(.*)(\Q"}\E)(.*)
		String pattern = "(.*)(\"CrumbStore\":\\{\"crumb\":\")(.*?)(\"\\})(.*)";
		try {
		      /*Pattern r = Pattern.compile(pattern);
		      Matcher m = r.matcher(sample);

		      if (m.find( )) {
		          System.out.println("Found value: " + m.group(0) );
		          System.out.println("Found value: " + m.group(1) );
		          System.out.println("Found value: " + m.group(2) );
		          System.out.println("Found value: " + m.group(3) ); 
		          System.out.println("Found value: " + m.group(4) );
		          System.out.println("Found value: " + m.group(5) );
		          
		       }else {
		          System.out.println("NO MATCH");
		       }*/

			testNse1();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void testNse1() throws IOException {
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
	
	public static void testNse() throws IOException {
		CookieStore cookieStore  = new ThreadSafeCookieStore();
		String pattern = "(.*)(\"CrumbStore\":\\{\"crumb\":\")(.*?)(\"\\})(.*)";
		Pattern r = Pattern.compile(pattern);
		
		AsyncHttpClientConfig config = new DefaultAsyncHttpClientConfig.Builder().setCookieStore(cookieStore).build();
	
		AsyncHttpClient asyncHttpClient = new DefaultAsyncHttpClient(config);
		Future<Response> whenResponse = asyncHttpClient.prepareGet(url4).execute();
		
		try {
			Response res = whenResponse.get();
			
		
			if (res.getResponseBody().contains("crumb")) {
				System.out.println("crumb found");
				
			}
			Matcher m = r.matcher(res.getResponseBody());
		      if (m.find( )) {
		          System.out.println("Found value of crumb: " + m.group(3) ); 
		       }

			Future<Response> whenResponse2 = asyncHttpClient.prepareGet(url3 + m.group(3)).execute();
			
			
			
			//System.out.println(r.getResponseBody());
			
			Response r2 = whenResponse2.get();
			System.out.println(r2.getResponseBody());
			
		} catch (InterruptedException e) {
			System.out.println("Interrupted exception: ");
			 e.printStackTrace();
		} catch (ExecutionException e) {
			System.out.println("ExecutionException: ");
			e.printStackTrace();
		}
	}
}

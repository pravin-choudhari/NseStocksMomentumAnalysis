package com.analysis.momentum.index.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.analysis.momentum.model.PriceData;
import com.analysis.momentum.model.Stock;

public class YahooFinance {
	private static final String baseURL = "https://ca.finance.yahoo.com/quote/TCS.NS/history?p=TCS.NS";
	private static final String stockURL = "https://query1.finance.yahoo.com/v7/finance/download/%s?period1=%s&period2=%s&interval=1d&events=history&crumb=%s";
	private final String pattern = "(.*)(\"CrumbStore\":\\{\"crumb\":\")(.*?)(\"\\})(.*)";
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private final CookieManager cookieManager;
	private final boolean writeToFile = true;
	private String crumb;
	private URL url;

	public YahooFinance() {
		cookieManager = new CookieManager();
		cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
		CookieHandler.setDefault(cookieManager);
	}

	public void setupYahooFinance() {
		try {
			final Pattern r = Pattern.compile(pattern);
			final BufferedReader reader;
			StringBuilder sb = new StringBuilder();
			String line = null;

			url = new URL(baseURL);
			reader = getReader(url);

			if (reader == null) {
				return;
			}

			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}

			Matcher m = r.matcher(sb.toString());
			if (m.find()) {
				System.out.println("Found value of crumb: " + m.group(3));
				crumb = m.group(3);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Stock getPriceDataForStockFromFile(final String symbol) {
		final Stock stock = new Stock(symbol);

		try {
			final List<String> lines = Files.readAllLines(Paths.get("H://tmp/" + symbol + ".txt"));

			lines.forEach(line -> {
				updateStockData(stock, line);
			});
		} catch (IOException e) {
			e.printStackTrace();
		}

		return stock;
	}

	public Stock getPriceDataForStockFromYahoo(final String symbol, final long startTime, final long endTime) {
		final Stock stock = new Stock(symbol);

		if (crumb == null) {
			setupYahooFinance();
		}

		final String endPoint = String.format(stockURL, symbol, startTime, endTime, crumb);
		try {
			final URL url = new URL(endPoint);
			final BufferedReader reader = getReader(url);
			String line = null;
			BufferedWriter bw;

			if (writeToFile) {
				bw = new BufferedWriter(new FileWriter("H://tmp/" + symbol + ".txt"));
			}

			while ((line = reader.readLine()) != null) {
				if (writeToFile) {
					bw.write(line + "\n");
				}

				updateStockData(stock, line);
			}

			if (writeToFile) {
				bw.flush();
				bw.close();
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return stock;
	}

	private void updateStockData(final Stock stock, final String line) {
		if (!line.startsWith("Date,")) {
			String[] data = line.split(",");
			final LocalDate date = LocalDate.from(DateTimeFormatter.ISO_LOCAL_DATE.parse(data[0]));
			final PriceData priceData = new PriceData();

			if (date != null) {
				priceData.setLocalDate(date);
			}

			if (data[1] != null && !data[1].equals("null")) {
				priceData.setOpenPrice(Double.parseDouble(data[1]));
			}
			if (data[2] != null && !data[2].equals("null")) {
				priceData.setHighPrice(Double.parseDouble(data[2]));
			}
			if (data[3] != null && !data[3].equals("null")) {
				priceData.setLowPrice(Double.parseDouble(data[3]));
			}
			if (data[4] != null && !data[4].equals("null")) {
				priceData.setClosePrice(Double.parseDouble(data[4]));
			}
			if (data[5] != null && !data[5].equals("null")) {
				priceData.setAdjClosePrice(Double.parseDouble(data[5]));
			}
			if (data[6] != null && !data[6].equals("null")) {
				priceData.setVolume(Double.parseDouble(data[6]));
			}

			stock.updateMonthlyBucketData(priceData);
		}
	}

	BufferedReader getReader(final URL url) {
		final HttpURLConnection conn;
		BufferedReader reader = null;

		try {
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setReadTimeout(15 * 1000);
			conn.connect();
			reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

		} catch (IOException e) {
			System.out.println("Unable to query URL: " + url);
			e.printStackTrace();
		}

		return reader;
	}
}

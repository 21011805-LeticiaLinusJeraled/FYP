/**
 * 
 * I declare that this code was written by me, 21011805. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Leticia Linus Jeraled
 * Student ID: 21011805 
 * Class: E63D
 * Date created: 2023-Jul-10 6:56:46 pm 
 * 
 */
package FYP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author 21011805
 *
 */
public class CurrencyConverter {
	private static final String API_ACCESS_KEY = "d47d85ec781c27517f65c5b5acf4dc55";

	public static void convertCurrency(String fromCurrency, String toCurrency, double amount) {
		try {
			fromCurrency = "SGD"; // Set fromCurrency to "SGD" by default
			String apiUrl = "http://data.fixer.io/api/convert?access_key=" + API_ACCESS_KEY + "&from=" + fromCurrency + "&to=" + toCurrency + "&amount=" + amount;
			// This line constructs the URL for the fixer.io API to perform the currency conversion

			URL url = new URL(apiUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection(); // open an HTTP connection to the fixer.io API
			connection.setRequestMethod("GET"); // Connection use HTTP GET method to retrieve data from the API

			int responseCode = connection.getResponseCode(); // Retrieves the HTTP response code from the API call

			// HTTP_OK (indicating a successful response)
			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream())); 
				// BufferedReader to read the API response
				String response = reader.readLine();
				reader.close();

				// Use helper methods to extract relevant data from the JSON response
				boolean success = extractBooleanValue(response, "success");
				double result = extractDoubleValue(response, "result");

				// Prints the details of the currency conversion if it is successful
				if (success) {
					System.out.println("Currency Conversion Successful!");
					System.out.println("From: " + fromCurrency);
					System.out.println("To: " + toCurrency);
					System.out.println("Amount: " + amount);
					System.out.println("Result: " + result);
				} else {
					System.out.println("Currency Conversion Failed!");
				}
			} else {
				System.out.println("Error: " + responseCode);
			}

			connection.disconnect();
		} catch (IOException e) { // Catch any IOException that might occur during the API call and print the
									// stack trace for debugging
			e.printStackTrace();
		}
	}

	// Helper methods to extract values from the JSON response received from the
	// fixer.io API

	private static String extractStringValue(String response, String key) {
		int index = response.indexOf("\"" + key + "\":");
		int startIndex = response.indexOf(":", index) + 1;
		int endIndex = response.indexOf(",", index);
		String valueString = response.substring(startIndex, endIndex).trim();
		return valueString.substring(1, valueString.length() - 1); // Remove quotation marks
	}

	private static boolean extractBooleanValue(String response, String key) {
		int index = response.indexOf("\"" + key + "\":");
		int startIndex = response.indexOf(":", index) + 1;
		int endIndex = response.indexOf(",", index);
		String valueString = response.substring(startIndex, endIndex).trim();
		return Boolean.parseBoolean(valueString);
	}

	private static double extractDoubleValue(String response, String key) {
		int index = response.indexOf("\"" + key + "\":");
		int startIndex = response.indexOf(":", index) + 1;
		int endIndex = response.indexOf(",", index);
		String valueString = response.substring(startIndex, endIndex).trim();
		return Double.parseDouble(valueString);
	}

}
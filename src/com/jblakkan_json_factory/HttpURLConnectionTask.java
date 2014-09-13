package com.jblakkan_json_factory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

public class HttpURLConnectionTask extends AsyncTask<String, Integer, String> {
	String TAG = "FactoryInfo_HttpURLConn";
	private Context mContext;
	String mFactorySymbol;

	HttpURLConnectionTask(Context context) {
		mContext = context;
	}

	// Called on background thread
	protected String doInBackground(String... factorySymbol) {
		//Argument is an array of strings; we only use the first one
		Log.v(TAG, this.getClass().getSimpleName()
				+ "doInBackground: get the factory info for: " + factorySymbol);
		mFactorySymbol = factorySymbol[0];
		Log.d("The Factory Symbol:", mFactorySymbol);
		//return getAirportInfo(airportSymbol); ///###///###
		return processRESTCommand(mFactorySymbol); //####///#### (would be mAirportSymbol)
	}

	// / Called from the UI Thread. After doInBackground() completes, this receives a
	// value (of type GenericParameter3) returned by doInBackground()
	// This is what returns the results back to the initiating (UI) thread
	//  Result is a JSON string.   (which will be later parsed into a structure and displayed)
	protected void onPostExecute(String result) {
		displayFactoryInfo(result);
	}



	
	//
	// Process a request (i.e. buy a log, etc.)
	//
	// Makes a URL connection, sends the operation
	// Returns: a string
	//    null => couldn't make a connection
	//    TOAST:<some error text> => got a 404 error from server
	//	  <any other string> => String from http, to pass on for parsing
	//    
	
	private String processRESTCommand(String requestedOperation) {
		//operation = "buy:<species>" or "cut:<length>" or "turn:<league>" or
		// "finish:<model>" or "summary:json"
		String sb = null;
		boolean isConnection = isNetworkConnected(mContext);
		if (isConnection == true) {
			// Put together the url
			
			String fields[] = requestedOperation.split(":");
			

			//String queryString = "http://w3.blakkan.org/summary/json";
			//This is the magic IP address for localhost in the emulator
			String postString;
			if (Build.MODEL.contains("sdk")) {
				postString = "http://10.0.2.2:9292" + fields[1] ;
			} else {
				postString = "http://w3.blakkan.org" + fields[1] ;
			}
			Log.d(TAG, "In processRESTCommand url: " + postString );
			Log.d(TAG, Build.MODEL );
			try {
				// Open the connection and get the response from the url.
				// Response should be in json
				URL url = new URL(postString);
				//This one needs to be a post, not a get, for our server.
				HttpURLConnection urlConnection = (HttpURLConnection) url
						.openConnection();
	
				urlConnection.setRequestMethod(fields[0]);

				int response = urlConnection.getResponseCode();
				
				if (response != HttpURLConnection.HTTP_OK) {
					Log.e(TAG, this.getClass().getSimpleName()
							+ " HttpURLConnection not ok: " + response);
				} else {
					Log.d(TAG, this.getClass().getSimpleName()
							+ " HttpURLConnection is ok: " + response);
					sb = getInputString(urlConnection);
					Log.d(TAG, sb);
				}
			} catch (Exception e) {
				Log.e(TAG, this.getClass().getSimpleName() + e + " for url: "
						+ postString);
				e.printStackTrace();
			}
		} else {
			Log.e(TAG, this.getClass().getSimpleName()
					+ " Not connection to network");
		}
		return sb;
	}
	
	
	//
	// Helper routine to just pull over the data from an HTTP request
	//
	private String getInputString(HttpURLConnection urlConnection) {
		// read the input
		StringBuffer sb = null;
		BufferedReader in;
		try {
			in = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				sb.append(line + NL);
			}
			in.close();
		} catch (IOException e) {
			Log.d(TAG, "");
			e.printStackTrace();
		}
		return sb.toString();
	}

	//
	// This sends the results back to the UI thread
	//
//,	private void displayAirportInfo(String result) {
//,		FactoryInfo airportInfo = null;
//,		if (result != null) {
//,			ParseJsonInfo parseJsonInfo = new ParseJsonInfo(mAirportSymbol);
//,			airportInfo = parseJsonInfo.decodeMessage(result, mAirportSymbol);
//,		}
		// Get the calling activity from the context that was passed in
		// Call the display routine in the activity
//,		MainActivity app = (MainActivity) mContext;
//,		app.displayData(airportInfo);
//,	}
		private void displayFactoryInfo(String result) {
			FactoryInfo factoryInfo = null;
			if (result != null) {
				ParseJsonInfo parseJsonInfo = new ParseJsonInfo();
				factoryInfo = parseJsonInfo.decodeMessage(result);
			}
			// Get the calling activity from the context that was passed in
			// Call the display routine in the activity
			MainActivity app = (MainActivity) mContext;
			app.displayData(factoryInfo);
		}

	//
	// utility routine to check type and availablity of network
	// connection
	//
	private boolean isNetworkConnected(Context context) {
		boolean networkState = false;
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();
			if (networkInfo != null) {
				networkState = networkInfo.isConnected();
				if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
					@SuppressWarnings("unused")
					// This is not needed in this example but here for an
					// example
					boolean mIsWifiConnection = true;
				} else {
					Log.d(TAG,
							this.getClass().getName()
									+ ": connected - not wifi: "
									+ networkInfo.getType() + " Build model: "
									+ Build.MODEL + " Build product: "
									+ Build.PRODUCT);
				}
			} else {
				Log.d(TAG, this.getClass().getName()
						+ ": networkInfo of active network is null");
			}
		} else {
			Log.d(TAG, this.getClass().getName() + ": connectivity is null");
		}
		Log.d(TAG, this.getClass().getName() + ":isNetworkConnected: "
				+ networkState);
		return networkState;
	}
}

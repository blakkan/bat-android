package com.jblakkan_json_factory;

import org.json.JSONObject;

import android.util.Log;

//
// Class to parse.   This now barely warrents being a class...
//
public class ParseJsonInfo {
	FactoryInfo mFactoryInfo = null;   // The block for communicating from http thread to GUI thread
	String TAG = "FactoryInfo_HttpURLConn";

	// constructors
	public ParseJsonInfo() {
	}

	public FactoryInfo getFactoryInfo() {
		return mFactoryInfo;
	}

	// Take a JSON string, parse it into a structure.

	public FactoryInfo decodeMessage(String message) {
		try {
			Log.d(TAG, "Attempting to parse: " + message);
			
			// This is what we're parsing into
			mFactoryInfo = new FactoryInfo();

			// And here's the parser structure itself
			JSONObject jObject;
			jObject = new JSONObject(message);
			
			//suction these out and drop them in the data structure one at a 
			// time.   We needn't worry about errors, since they'll blow us
			// out into the catch block.

			mFactoryInfo.setCash(jObject.getString(FactoryInfo.TAG_CASH));	
			mFactoryInfo.setToastMessage(jObject.getString(FactoryInfo.TAG_TOAST_MESSAGE));
			mFactoryInfo.setLogs(jObject.getString(FactoryInfo.TAG_LOGS));
			mFactoryInfo.setBlanks(jObject.getString(FactoryInfo.TAG_BLANKS));
			mFactoryInfo.setTurnings(jObject.getString(FactoryInfo.TAG_TURNINGS));
			mFactoryInfo.setBats(jObject.getString(FactoryInfo.TAG_BATS));


		} catch (Exception e) {
			Log.e(TAG, "JSON parser experienced exception while parsing");
			e.printStackTrace();
			return null;
		}
		return mFactoryInfo;
	}

	@Override
	public String toString() {
		return super.toString() + mFactoryInfo.toString();
	}
}

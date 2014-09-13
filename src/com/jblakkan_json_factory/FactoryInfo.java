package com.jblakkan_json_factory;

import android.util.Log;

//
//  This is just a status block; used for
//  Sending results information back from the URL background thread
//  back to the GUI.
//  The methods here are the usual suspects- getters, setters, toString for utility
//  and debug use, along with the constants for the JSON return packets.
//
//  There are, by design, no JSON command packets sent by the app to the server;
//  all the command information is contained in the URL.
//
public class FactoryInfo {
	
	private String mToastMessage = "";
	private String mCash = "";
	private String mLogs = "";
	private String mBlanks = "";
	private String mTurnings = "";
	private String mBats = "";

	// constructors; nothing done here
	public FactoryInfo() {
	}
	
	public void setToastMessage(String message) {
		mToastMessage = message;
	}

	public String getToastMessage() {
		return (mToastMessage);
	}
	
	
	public void setCash(String cash) {
		mCash = cash;
	}

	public String getCash() {
		return (mCash);
	}
	
	public void setLogs(String logs) {
		mLogs = logs;
	}

	public String getLogs() {
		return (mLogs);
	}
	
	public void setBlanks(String blanks) {
		mBlanks = blanks;
	}

	public String getBlanks() {
		return (mBlanks);
	}
	
	
	public void setTurnings(String turnings) {
		mTurnings = turnings;
	}

	public String getTurnings() {
		return (mTurnings);
	}

	public void setBats(String bats) {
		mBats = bats;
	}

	public String getBats() {
		return (mBats);
	}

	@Override
	public String toString() {
		return super.toString() + "Cash: " + mCash + " Logs: "
				+ mLogs + "Blanks: " + mBlanks+
				" Turnings:" + mTurnings + " Bats:" + mBats + "\n";
	}


	public static final String TAG_CASH = "cash";
	public static final String TAG_LOGS = "logs";
	public static final String TAG_BLANKS = "blanks";
	public static final String TAG_TURNINGS = "turnings";
	public static final String TAG_BATS = "bats";
	public static final String TAG_TOAST_MESSAGE = "message";
}

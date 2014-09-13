package com.jblakkan_json_factory;



import com.jblakkan_json_factory.R;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;


//
// Our main activity, on the GUI thred
//

public class MainActivity extends Activity {
	
	String TAG = "Bat Factory";
	private HttpURLConnectionTask backgroundTask = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//
		// Want an initial setting of the screen variables.
		//  We'll inflate the view, but we'd like to get
		//  values from the server without waiting for
		//  a user refresh click...

	    startHttpURLTask("GET", "/summary/json");

		//
		// Hook listeners to the buttons; in this case a named (but could be a lambda)
		// listener which just start the URL async task.
		//
		// Note that we're sending commands based on these buttons, and the commands
	    // are declared right here in the URL.
	    //
	    // For production code, this could go into a strings file, or even perhaps
	    // as XML metadata on the the layout item itself
	    //
	    
		findViewById(R.id.buttonRefresh).setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				startHttpURLTask("GET", "/summary/json");
			}
		});
		
		findViewById(R.id.buttonBuy).setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				startHttpURLTask("POST", "/buy/Ash");
			}
		});
		
		findViewById(R.id.buttonCut).setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				startHttpURLTask("POST", "/cut/oldest/32");
			}
		});
		
		findViewById(R.id.buttonTurn).setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				startHttpURLTask("POST", "/turn/oldest/NL");
			}
		});
		findViewById(R.id.buttonFinish).setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				startHttpURLTask("POST", "/finish/oldest/Cobb");
			}
		});
		findViewById(R.id.buttonSell).setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				startHttpURLTask("POST", "/sell/oldest");
			}
		});
	}

	//
	// If we get dumped, then restart... Still want to refresh the screen without needing
	// a click from the user
	//
	@Override
	public void onRestart() {

		//
		// Want an initial setting of the screen variables.
		//
		super.onRestart();
		startHttpURLTask("GET", "/summary/json");
	}
	
	//
	//
	// Singleton pattern, for starting the HttpURL task (try for prudence sake to start it
	// on each button click (i.e. might be coming back here after a pause, or some other
	// android-ism)
	//
	//  We give it the HTTP verb and route as two separate strings; that's arbitrary,
	// could have just colon separated them in URI fashion, but this is what we chose.
	//
	public void startHttpURLTask(String requestedVerb, String requestedRoute) {
		if (backgroundTask != null) {
			AsyncTask.Status backgroundStatus = backgroundTask.getStatus();
			if (backgroundStatus != AsyncTask.Status.FINISHED) {
				return;
			}
		}
		backgroundTask = new HttpURLConnectionTask(this);
		// glom in the colon and fire it off...
		backgroundTask.execute(requestedVerb + ":" + requestedRoute);
	}

	//
	// This is where the main activity updates everything here in the safety of the UI thread
	// In addition to updating the ordinary fields, it thows a toast if the server has seen
	// fit to include a message.
	//
    public void displayData(FactoryInfo factoryInfo) {
		
		if ( factoryInfo == null ) {
			// As Billy Preston said:  "Nothin' from Nothin' leaves Nothin"
			// Ah, internet... We hardly knew ye...
			Toast.makeText(getApplicationContext(), "Problem downloading factory info. Unable to connect", 
						Toast.LENGTH_SHORT).show() ;
		} else {
			((TextView) findViewById(R.id.textViewCash)).setText("Cash: " + factoryInfo.getCash());
			((TextView) findViewById(R.id.textViewLogs)).setText("Logs: " + factoryInfo.getLogs());
			((TextView) findViewById(R.id.textViewBlanks)).setText("Blanks: " + factoryInfo.getBlanks());
			((TextView) findViewById(R.id.textViewTurnings)).setText("Turnings: " + factoryInfo.getTurnings());
			((TextView) findViewById(R.id.textViewBats)).setText("Bats: " + factoryInfo.getBats());

			// And, if we've been given a message from our server, here's where we render it up.
			if ( ! factoryInfo.getToastMessage().equals("") ) {
				Toast.makeText(getApplicationContext(), factoryInfo.getToastMessage(),
					Toast.LENGTH_SHORT).show() ;
			}
		}
	}
}


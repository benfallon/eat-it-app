package com.benjaminafallon.eatit;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;

public class CookieActivity extends Activity {
	
	private int cookies_left = 12;
	private int bites_left = 7;
	private float high_score;
	private long ms_remaining;
	private boolean started = false;
	private ImageButton cookieButton;
	private ImageButton newCookieButton;
	private ImageView cookieSheetView;
	private TextView timerView;
	private SharedPreferences EatItPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cookie);
		
		Log.i("cookies", "1");
		//This line gets all saved/persistent values (ints, strings, whatever) for this application
	    EatItPreferences = this.getSharedPreferences("com.benjaminafallon.eatit", Context.MODE_PRIVATE);
		Log.i("cookies", "2");

	    //get the value saved in preferences under the key "com.benjaminafallon.eatit.highScore";
	    //if nothing is saved under this key it will return the default 9999;
	    //The long name insures that this key will always be unique
	    high_score = EatItPreferences.getFloat("com.benjaminafallon.eatit.highScore", 9999.0F);
		Log.i("cookies", "3");
		
		newCookieButton = (ImageButton) findViewById(R.id.newCookieButton);
		newCookieButton.setEnabled(false);
		Log.i("cookies", "4");

		
		cookieSheetView = (ImageView) findViewById(R.id.cookieSheetView);
		Log.i("cookies", "5");

		timerView = (TextView) findViewById(R.id.timerView);
		Log.i("cookies", "6");
	
	}
	
	public void cookieClick(View v) {
		
		//the following if statement will only run once (when start is pressed)
		if (started == false) {
			started = true;
			
			//first parameter is amount to set on timer (in this case, 300 seconds; second parameter is how often to call onTick method
			new CountDownTimer(300000, 1000) {

			     public void onTick(long millisUntilFinished) {
			         ms_remaining = millisUntilFinished;
			     }

			     public void onFinish() {
			    	 
			     }
			     
			  }.start();
				Log.i("cookies", "7");

		}
		
		cookieButton = (ImageButton) v;
		
		bites_left--;
		
		//the following couple lines of code allow the cookie image to be set according to the number of bites left
		String cookieDrawableName = "cookie" + bites_left + "bites";
		int cookieResourceId = getResources().getIdentifier(cookieDrawableName, "drawable", getPackageName());
		Log.i("cookies", "8");
		cookieButton.setImageResource(cookieResourceId);
		
		if (bites_left == 0) {
			if (cookies_left == 0) {
				endGame();
			}
			else {
				Log.i("cookies", "9");
				timerView.setText("Time: " + millisecondsToSeconds() + " seconds.");
				cookieButton.setEnabled(false);
				newCookieButton.setEnabled(true);
			}
		}
			
	}
	
	public void newCookie(View v) {

		bites_left = 7;
		cookieButton.setImageResource(R.drawable.cookie7bites);
		newCookieButton.setEnabled(false);
		cookieButton.setEnabled(true);
		
		cookies_left--;
		String cookieSheetDrawableName = "cookiesheet" + cookies_left;
		int cookieSheetResourceId = getResources().getIdentifier(cookieSheetDrawableName, "drawable", getPackageName());
		
		cookieSheetView.setImageResource(cookieSheetResourceId);
		
		
	}
	
	public float millisecondsToSeconds() {
		float seconds = ( (300000.0f - ms_remaining) / 1000.0f);
		return seconds;
	}
	
	public void endGame() {
		
		AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(this);

		float score = millisecondsToSeconds();
		
		if (score < high_score) {
			high_score = score;
			alertDialog2.setTitle("NEW HIGH SCORE!!!!");
			EatItPreferences.edit().putFloat("com.benjaminafallon.eatit.highScore", high_score).apply();
		}

		//Setting Dialog Message
		alertDialog2.setMessage(" Your Score: " + score + " seconds \n High Score: " + high_score + " seconds");
		
		alertDialog2.setPositiveButton("Play Again",
		        new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		            	recreate();
		            }
		        });
		
		alertDialog2.setNegativeButton("Quit",
		        new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		                dialog.cancel();
		                finish();
		            }
		        });

		alertDialog2.show();
	}

}

package edu.stanford.holyc.tia;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;

public class TIA extends Activity
{
    /** Debug name
     */
    private static final String TAG = "TIA";
    

    @Override
	public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

	Log.d(TAG, "Starting TIA...");

	Intent serviceIntent = new Intent();
	serviceIntent.setAction("edu.stanford.holyc.LalDBService");
	startService(serviceIntent);
	stopService(serviceIntent);

    }

    @Override
	public void onDestroy()
    {
	super.onDestroy();
	Log.d(TAG, "TIA stopped");
    }
}
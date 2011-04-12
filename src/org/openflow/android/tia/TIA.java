package org.openflow.android.tia;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import org.openflow.android.tia.Database;

public class TIA extends Activity
{
    /** Debug name
     */
    private static final String TAG = "TIA";
    
    /** Reference to database
     */
    public Database db = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

	db = new Database(getApplicationContext());
	Log.d(TAG, "TIA started...");


    }


}

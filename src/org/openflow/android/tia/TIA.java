package org.openflow.android.tia;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteCursor;
import org.sqlite.helper.OpenFlow;
import org.openflow.protocol.OFFlowRemoved;

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

	//db = new Database(getApplicationContext());
	Log.d(TAG, "TIA started...");

	Intent serviceIntent = new Intent();
	serviceIntent.setAction("edu.stanford.holyc.LalDBService");
	startService(serviceIntent);
	stopService(serviceIntent);

	/////////////////////////////
	/*OFFlowRemoved ofr = new OFFlowRemoved();
	ContentValues cv = new ContentValues();
	cv.put("App","Testing");
	OpenFlow.addOFFlowRemoved2CV(cv, ofr);
	db.insert("TIA_FLOW", cv);

	SQLiteCursor c = (SQLiteCursor) db.db.query("TIA_FLOW",
						    null, null, null, 
						    null, null, null);
	c.moveToFirst();
	Log.d(TAG, c.getCount()+" rows");
	while (true)
	{
	    String[] names = c.getColumnNames();
	    for (int i = 0; i < names.length; i++)
		Log.d(TAG, names[i]);
	    
	    if (c.isLast())
		break;
	    c.moveToNext();
	}
	c.close();*/
    }

    @Override
	public void onDestroy()
    {
	super.onDestroy();
	Log.d(TAG, "TIA stopped");
    }
}
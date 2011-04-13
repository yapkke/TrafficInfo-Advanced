package org.openflow.android.tia;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.content.ContentValues;
import android.database.sqlite.SQLiteCursor;
import org.openflow.android.tia.Database;
import org.sqlite.helper.OpenFlow;
import org.openflow.protocol.OFMatch;

public class TIA extends Activity
{
    /** Debug name
     */
    private static final String TAG = "TIA";
    
    /** Reference to database
     */
    public Database db = null;

    @Override
	public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

	db = new Database(getApplicationContext());
	Log.d(TAG, "TIA started...");

	/////////////////////////////
	OFMatch ofm = new OFMatch();
	ContentValues cv = new ContentValues();
	cv.put("App","Testing");
	OpenFlow.addOFMatch2CV(cv, ofm);
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
	c.close();
    }

    @Override
	public void onDestroy()
    {
	db.close();
	super.onDestroy();
	Log.d(TAG, "TIA stopped");
    }
}
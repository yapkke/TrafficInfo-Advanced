package org.openflow.android.tia;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.content.ContentValues;
import android.database.sqlite.SQLiteCursor;
import org.openflow.android.tia.Database;

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
	ContentValues cv = new ContentValues();
	cv.put("App","Testing");
	cv.put("App_No",12);
	db.insert("TIA_FLOW", cv);
	

    }

    @Override
	public void onDestroy()
    {
	db.close();
	super.onDestroy();
	Log.d(TAG, "TIA stopped");
    }

	    /*SQLiteCursor query = (SQLiteCursor) db.query(false, 
	    "TIA_FLOW",
	    null,
	    null,
							 null,
							 null,
							 null,
							 null, 
							 null);
 	    Log.d(TAG, ""+query.getColumnCount());
	    */


}
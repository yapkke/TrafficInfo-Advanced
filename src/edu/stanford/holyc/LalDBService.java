package edu.stanford.holyc;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;
import android.util.Log;
import edu.stanford.holyc.Database;

import android.content.ContentValues;
import org.sqlite.helper.OpenFlow;
import org.openflow.protocol.OFFlowRemoved;


public class LalDBService extends Service
{
    /** Log name
     */
    private static final String TAG = "LalDBService";
    
    /** Reference to database
     */
    public Database db = null;

    @Override
	public IBinder onBind(Intent intent)
    {
	return null;
    }

    @Override
	public void onCreate()
    {
	super.onCreate();
	Toast.makeText(this, "Starting Lal Database service...",
		       Toast.LENGTH_LONG).show();
	Log.d(TAG, "Starting Lal Database service");

	db = new Database(getApplicationContext());

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
	db.close();
	super.onDestroy();
	Toast.makeText(this, "Stopping Lal Database service...",
		       Toast.LENGTH_LONG).show();
	Log.d(TAG, "Stopping Lal Database service");
    }
}
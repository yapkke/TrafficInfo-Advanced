package edu.stanford.holyc;

import android.app.Service;
import android.content.Intent;
import android.content.ContentValues;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Handler;
import android.widget.Toast;
import android.util.Log;

import edu.stanford.holyc.Database;
import org.sqlite.helper.OpenFlow;
import org.openflow.protocol.OFFlowRemoved;

/** Lal's Database Service
 *
 *
 * Developed as messenger service with guidance from
 * http://developer.android.com/guide/topics/fundamentals/bound-services.html
 *
 * @author ykk
 * @date Apr 2011
 */
public class LalDBService extends Service
{
    /** Log name
     */
    private static final String TAG = "LalDBService";
    
    /** Reference to database
     */
    public Database db = null;

    /** Message type for query
     */
    public static final int QUERY_TYPE = 1;
    
    /** Handler of incoming messages from clients.
     */
    class IncomingHandler extends Handler 
    {
        @Override
	    public void handleMessage(Message msg) 
	{
            switch (msg.what) 
	    {
	    case QUERY_TYPE:
		Log.d(TAG, "Query :"+((String) msg.obj));
		break;
	    default:
		super.handleMessage(msg);
            }
        }
    }

    /** Target for clients to send messages.
     */
    public final Messenger mMessenger = new Messenger(new IncomingHandler());
    
    @Override
	public IBinder onBind(Intent intent) 
    {
	return mMessenger.getBinder();
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
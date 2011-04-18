package edu.stanford.holyc.tia;

import android.app.Activity;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.content.Intent;
import android.content.Context;
import android.content.ComponentName;
import android.content.ServiceConnection;
import edu.stanford.holyc.LalDBService;

/** TrafficInfo Advanced class
 * 
 * @author ykk
 * @date Apr 2011
 */
public class TIA extends Activity
{
    /** Debug name
     */
    private static final String TAG = "TIA";
    
    /** Reference to messenger
     */
    Messenger mService = null;
    /** Indicate if messenger is bounded
     */ 
    boolean mBound;

    /** Class to establish connection
     */
    private ServiceConnection mConnection = new ServiceConnection() 
    {
	public void onServiceConnected(ComponentName className, IBinder service) 
	{
	    mService = new Messenger(service);
	    mBound = true;
	}

	public void onServiceDisconnected(ComponentName className) 
	{
	    mService = null;
	    mBound = false;
	}
    };

    @Override
	protected void onStart() 
    {
        super.onStart();
        bindService(new Intent(this, LalDBService.class), 
		    mConnection,
		    Context.BIND_AUTO_CREATE);
    }

    @Override
	protected void onStop() 
    {
	Message msg = Message.obtain(null, LalDBService.QUERY_TYPE, 0, 0,
				     new String("SELECT * FROM Lal_Flow_Removed;"));
        try {
            mService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        super.onStop();
        if (mBound)
	{
            unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
	public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

	Log.d(TAG, "Starting TIA...");
    }

    @Override
	public void onDestroy()
    {
	Log.d(TAG, "Stopping TIA...");
	Intent serviceIntent = new Intent();
	serviceIntent.setAction("edu.stanford.holyc.LalDBService");
	stopService(serviceIntent);

	super.onDestroy();
    }
}
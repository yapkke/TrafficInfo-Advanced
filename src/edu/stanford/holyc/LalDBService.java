package edu.stanford.holyc;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;
import android.util.Log;
import edu.stanford.holyc.Database;

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
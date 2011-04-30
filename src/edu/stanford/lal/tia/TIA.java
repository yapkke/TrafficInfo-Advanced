package edu.stanford.lal.tia;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Context;
import android.content.BroadcastReceiver;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import java.util.Vector;

import edu.stanford.lal.LalMessage;

import com.google.gson.Gson;

import org.sqlite.helper.CursorHelper;

class NameValue
{
    /** Name
     */
    public String name;
    /** Value
     */
    public String value;

    public NameValue(String n, String v)
    {
	name = n;
	value = v;
    }

    @Override
	public String toString()
    {
	return name+"\n   Value="+value;
    }
}

/** TrafficInfo Advanced class
 * 
 * @author ykk
 * @date Apr 2011
 */
public class TIA extends ListActivity
{
    /** Debug name
     */
    private static final String TAG = "TIA";
    /** Vector of information
     */
    Vector<NameValue> COUNTRIES = new Vector<NameValue>();
    /** Reference to GSON
     */
    Gson gson = new Gson();
    /** Reference to LalMessage
     */
    LalMessage lmsg = new LalMessage();
    /** ListView adapter
     */
    ArrayAdapter<NameValue> adapter;

    /** Broadcast receiver
     */
    BroadcastReceiver bReceiver = new BroadcastReceiver()
    {
	@Override 
	    public void onReceive(Context context, Intent intent) 
	{
	    if (intent.getAction().equals(LalMessage.Result.action))
	    {
		String r = intent.getStringExtra(LalMessage.Result.str_key);
		Log.d(TAG, r);
		LalMessage.LalResult result = gson.fromJson(r, LalMessage.LalResult.class);
							    
		for (int i = 0; i < result.results.size(); i++)
		{
		    Vector row = CursorHelper.decipherRow(result.results.get(i));
		    Log.d(TAG, "App "+((String) row.get(0)));
		    Log.d(TAG, "PC "+((Long) row.get(1)));
		    Log.d(TAG, "BC "+((Long) row.get(2)));
		    Log.d(TAG, "D "+((Double) row.get(3)));
		}
		

		COUNTRIES.add(new NameValue("Test1", "t"));
		COUNTRIES.add(new NameValue("Test2", "ts"));
		COUNTRIES.add(new NameValue("Test3", "te"));
		COUNTRIES.add(new NameValue("Test4", "te"));
		COUNTRIES.add(new NameValue("Test5", "te2"));
		adapter.notifyDataSetChanged();
	    }
	}

    };

    @Override
	protected void onStart() 
    {
        super.onStart();
    }

    @Override
	protected void onStop() 
    {
        super.onStop();
    }

    @Override
	public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
	Log.d(TAG, "Starting TIA...");

	//Register for result of query
	IntentFilter rIntentFilter = new IntentFilter();
	rIntentFilter.addAction(LalMessage.Result.action);
	registerReceiver(bReceiver, rIntentFilter);

	//Set list view
	adapter = new ArrayAdapter<NameValue>(this, R.layout.list_item, COUNTRIES);
	setListAdapter(adapter);
	ListView lv = getListView();
	lv.setTextFilterEnabled(true);
	lv.setOnItemClickListener(new OnClick(getApplicationContext()));

	//Send broadcast query
	LalMessage.LalQuery q = lmsg.new LalQuery();
	q.distinct = true;
	q.columns = new String[] {"App",
				  "SUM(Packet_Count)",
				  "SUM(Byte_Count)",
				  "SUM(Duration)"};
	q.groupBy = "App";
	q.orderBy = "SUM(Byte_Count)";
	Intent i = new Intent(LalMessage.Query.action);
	i.setPackage(getPackageName());
	i.putExtra(LalMessage.Query.str_key, gson.toJson(q, LalMessage.LalQuery.class));
	sendBroadcast(i);
    }

    @Override
	public void onDestroy()
    {
	unregisterReceiver(bReceiver);

	Log.d(TAG, "Stopping TIA...");
	super.onDestroy();
    }
}

class OnClick
    implements OnItemClickListener
{
    Context context;

    public OnClick(Context c)
    {
	context = c;
    }

    public void onItemClick(AdapterView<?> parent, View view,
			    int position, long id) {
	// When clicked, show a toast with the TextView text
	Toast.makeText(context, ((TextView) view).getText(),
		       Toast.LENGTH_SHORT).show();
    }
}

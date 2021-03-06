package edu.stanford.lal.tia;

import java.util.ArrayList;
import java.util.Vector;

import org.sqlite.helper.CursorHelper;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.gson.Gson;

import edu.stanford.chart.BudgetPieChart;
import edu.stanford.lal.LalMessage;

/**
 * TrafficInfo Advanced class
 * 
 * @author ykk
 * @date Apr 2011
 */
public class TIA extends ListActivity {
    /**
     * Debug name
     */
    private static final String TAG = "TIA";
    /**
     * Reference to GSON
     */
    Gson gson = new Gson();
    /**
     * Reference to LalMessage
     */
    LalMessage lmsg = new LalMessage();
    /**
     * ListView adapter
     */
    ListItemAdapter adapter=null;
    /**
     * New ListView adapter
     */
    ArrayList<ListItem> listItems = new ArrayList<ListItem>();
    /** Reference to list view
     */
    ListView lv;
    /**
     * Broadcast receiver
     */
    BroadcastReceiver bReceiver = new BroadcastReceiver()
	{
	    @Override
		public void onReceive(Context context, Intent intent){
		if (intent.getAction().equals(LalMessage.Result.action)) {
		    String r = intent.getStringExtra(LalMessage.Result.str_key);
		    Log.d(TAG, r);
		    LalMessage.LalResult result = 
			gson.fromJson(r, LalMessage.LalResult.class);

		    if (result.columns[1].compareTo("SUM(Packet_Count)") == 0)
		    {
			//Result for the main screen
			listItems.clear();
			for (int i = 0; i < result.results.size(); i++) 
			{
			    Vector row = CursorHelper
				.decipherRow(result.results.get(i));
			    listItems.add(new ListItem(row));
			}
			adapter.notifyDataSetChanged();
		    }
		    else if (result.columns[1].compareTo("SUM(Byte_Count)") == 0)
		    {
			//Result for pie chart
			String aname = "";
			String[] keys = new String[result.results.size()];
			double[] values = new double[result.results.size()];
			for (int i = 0; i < result.results.size(); i++) 
			{
			    Vector row = CursorHelper
				.decipherRow(result.results.get(i));
			    aname = (String) row.get(0);
			    Long k = (Long) row.get(2);
			    switch (k.intValue())
			    {
			    case 0:
				keys[i] = "Non-IP";
				break;
			    case 3:
				keys[i] = "ICMP";
				break;
			    case 53:
				keys[i] = "DNS";
				break;
			    case 67:
				keys[i] = "DHCP";
				break;
			    case 80:
				keys[i] = "HTTP";
				break;
			    case 443:
				keys[i] = "HTTPS";
				break;
			    default:
				keys[i] = k.toString();
			    }
			    values[i] = ((Long) row.get(1)).doubleValue();
			}

			Intent chartintent = new BudgetPieChart(aname, keys, values)
			    .execute(context);
			chartintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(chartintent);
		    }
		}
	    }	
	};
	    
    @Override
	protected void onStart() {
	super.onStart();
	Log.d(TAG, "Starting TIA...");

	// Set list view
	if (adapter == null)
	{
	    adapter = new ListItemAdapter(this, R.layout.listview, listItems);
	    View header = (View) getLayoutInflater().inflate(R.layout.headerview,
							     null);
	    lv = getListView();
	    lv.addHeaderView(header);
	    lv.setTextFilterEnabled(true);
	    lv.setOnItemClickListener(new OnClick(getApplicationContext()));
	    setListAdapter(adapter);
	}

	// Send broadcast query
	LalMessage.LalQuery q = lmsg.new LalQuery();
	q.distinct = true;
	q.columns = new String[] { "App", "SUM(Packet_Count)",
				   "SUM(Byte_Count)", "SUM(Duration)" };
	q.groupBy = "App";
	q.orderBy = "SUM(Byte_Count) DESC";
	Intent i = new Intent(LalMessage.Query.action);
	i.setPackage(getPackageName());
	i.putExtra(LalMessage.Query.str_key,
		   gson.toJson(q, LalMessage.LalQuery.class));
	sendBroadcast(i);

    }

    @Override
	protected void onStop() {
	super.onStop();
    }

    @Override
	public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	Log.d(TAG, "Creating TIA...");

	// Register for result of query
	IntentFilter rIntentFilter = new IntentFilter();
	rIntentFilter.addAction(LalMessage.Result.action);
	registerReceiver(bReceiver, rIntentFilter);
    }

    @Override
	public void onDestroy() {
	unregisterReceiver(bReceiver);

	Log.d(TAG, "Stopping TIA...");
	super.onDestroy();
    }

    class OnClick implements OnItemClickListener {
	Context context;
	
	public OnClick(Context c) {
	    context = c;
	}
	
	public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
	    ListView lv = (ListView) parent;
	    ListItem item = (ListItem) lv.getAdapter().getItem(position);
	    
	    LalMessage.LalQuery q = lmsg.new LalQuery();
	    q.columns = new String[] { "App", "SUM(Byte_Count)", "MIN(Tp_Source, Tp_Destination)"};
	    q.groupBy = "MIN(Tp_Source, Tp_Destination)";
	    q.orderBy = "SUM(Byte_Count)";
	    q.selection = "App=\""+item.appName+"\"";
	    Intent i = new Intent(LalMessage.Query.action);
	    i.setPackage(getPackageName());
	    i.putExtra(LalMessage.Query.str_key,
		       gson.toJson(q, LalMessage.LalQuery.class));
	    sendBroadcast(i);
	}
    }   
}


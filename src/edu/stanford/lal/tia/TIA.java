package edu.stanford.lal.tia;

import java.util.ArrayList;
import java.util.Vector;

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
	 * Vector of information to list
	 */
	//Vector<QResult> info = new Vector<QResult>();
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
	// QResultAdapter adapter;
	ListItemAdapter adapter;

	/**
	 * New ListView adapter
	 */

	ArrayList<ListItem> listItems = new ArrayList<ListItem>();

	/**
	 * Broadcast receiver
	 */
	BroadcastReceiver bReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(LalMessage.Result.action)) {
				String r = intent.getStringExtra(LalMessage.Result.str_key);
				Log.d(TAG, r);
				LalMessage.LalResult result = gson.fromJson(r,
						LalMessage.LalResult.class);

				/*for (int i = 0; i < result.results.size(); i++) {
					Vector row = CursorHelper
							.decipherRow(result.results.get(i));
					listItems.add(new ListItem(row));
				}*/

				/*
				 * Directly test  
				*/
				Vector row = new Vector();
				row.add("com.google.android.apps.maps"); row.add(12345678L);
				row.add(53845L); row.add(10.2131); ListItem item = new
				ListItem(row); listItems.add(item); Vector row1 = new
				Vector(); row1.add("com.android.browser");
				row1.add(12345678L); row1.add(53845L); row1.add(10.2131);
				ListItem item1 = new ListItem(row1); listItems.add(item1);
				adapter.notifyDataSetChanged();
				
			}
		}

	};

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "Starting TIA...");

		// Register for result of query
		IntentFilter rIntentFilter = new IntentFilter();
		rIntentFilter.addAction(LalMessage.Result.action);
		registerReceiver(bReceiver, rIntentFilter);

		// Set list view
		// adapter = new QResultAdapter(this, R.layout.list_item, info);
		adapter = new ListItemAdapter(this, R.layout.listview, listItems);
		// setListAdapter(adapter);
		View header = (View) getLayoutInflater().inflate(R.layout.headerview,
				null);
		ListView lv = getListView();
		lv.addHeaderView(header);
		lv.setTextFilterEnabled(true);
		lv.setOnItemClickListener(new OnClick(getApplicationContext()));
		setListAdapter(adapter);

		// Send broadcast query
		LalMessage.LalQuery q = lmsg.new LalQuery();
		q.distinct = true;
		q.columns = new String[] { "App", "SUM(Packet_Count)",
				"SUM(Byte_Count)", "SUM(Duration)" };
		q.groupBy = "App";
		q.orderBy = "SUM(Byte_Count)";
		Intent i = new Intent(LalMessage.Query.action);
		i.setPackage(getPackageName());
		i.putExtra(LalMessage.Query.str_key,
				gson.toJson(q, LalMessage.LalQuery.class));
		sendBroadcast(i);
	}

	@Override
	public void onDestroy() {
		unregisterReceiver(bReceiver);

		Log.d(TAG, "Stopping TIA...");
		super.onDestroy();
	}

}

class OnClick implements OnItemClickListener {
	Context context;

	public OnClick(Context c) {
		context = c;
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
        ListView l = (ListView)parent;
        ListItem item = (ListItem)l.getAdapter().getItem(position);
		//Toast.makeText(context, "More info of " + item.appName, Toast.LENGTH_SHORT).show();
        String[] keys = new String[] {"TCP", "UDP", "ICMP", "Other"};
        double[] values = new double[] {50, 30, 15, 5};
        Intent intent = new BudgetPieChart(item.appName, keys, values).execute(context);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
	}
}

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
	 * Vector of information to list
	 */
	// Vector<QResult> info = new Vector<QResult>();
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

				for (int i = 0; i < result.results.size(); i++) {
					Vector row = CursorHelper
							.decipherRow(result.results.get(i));
					listItems.add(new ListItem(row));
				}
				adapter.notifyDataSetChanged();
			}
		}

	};

	@Override
	protected void onStart() {
		super.onStart();
		Log.d(TAG, "Starting TIA...");

		// Set list view
		adapter = new ListItemAdapter(this, R.layout.listview, listItems);
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

}

class OnClick implements OnItemClickListener {
	Context context;

	public OnClick(Context c) {
		context = c;
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ListView l = (ListView) parent;
		ListItem item = (ListItem) l.getAdapter().getItem(position);
		// Toast.makeText(context, "More info of " + item.appName,
		// Toast.LENGTH_SHORT).show();
		String[] keys = new String[] { "TCP", "UDP", "ICMP", "Other" };
		double[] values = new double[] { 50, 30, 15, 5 };
		Intent intent = new BudgetPieChart(item.appName, keys, values)
				.execute(context);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
}

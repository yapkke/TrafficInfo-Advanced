package edu.stanford.lal.tia;

import java.util.ArrayList;
import java.util.Vector;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A ListAdatper Class
 * 
 * @author Yongqiang Liu (liuyq7809@gmail.com)  
 */

public class ListItemAdapter extends ArrayAdapter<ListItem> {

	static final String TAG = "ListItemAdapter";
	private ArrayList<ListItem> items;

	public ListItemAdapter(Context context, int textViewResourceId,
			ArrayList<ListItem> items) {
		super(context, textViewResourceId, items);
		this.items = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.listview, null);
		}
		ListItem item = items.get(position);
		if (item != null) {
			TextView name = (TextView) v.findViewById(R.id.appName);
			TextView bytes = (TextView) v.findViewById(R.id.bytes);
			TextView packets = (TextView) v.findViewById(R.id.packets);
			TextView duration = (TextView) v.findViewById(R.id.duration);
			ImageView image = (ImageView) v.findViewById(R.id.icon);
			name.setText(getLabelFromPKGName(item.appName));
			bytes.setText(item.nubmerToString(item.bytes));
			packets.setText(item.nubmerToString(item.packets));
			duration.setText(item.nubmerToString(item.duration));
			PackageManager pk = getContext().getPackageManager();
			try {
				image.setImageDrawable(pk.getApplicationIcon(item.appName));
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				image.setImageDrawable(pk.getDefaultActivityIcon());
				Log.d(TAG, "Error: " + e.getMessage());
			}
		}
		return v;
	}
	
	public String getLabelFromPKGName(String pkgname) {
		PackageManager pk = getContext().getPackageManager();
		String appLabel = null;
		try {
			ApplicationInfo ai = pk.getApplicationInfo(pkgname, 0);
			appLabel = ai.loadLabel(pk).toString();
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			appLabel = pkgname;
			Log.d(TAG, "Label of " + pkgname + "Not Found");
		}
		return appLabel;
	}

}

class ListItem {
	
	static final String TAG = "ListItem";
	
	/**
	 * Application name
	 */
	public String appName;
	/**
	 * Packet count
	 */
	public Long packets;
	/**
	 * Byte count
	 */
	public Long bytes;
	/**
	 * Duration
	 */
	public Double duration;

	public ListItem(Vector v) {
		appName = (String) v.get(0);
		packets = (Long) v.get(1);
		bytes = (Long) v.get(2);
		duration = (Double) v.get(3);
	}

	public String nubmerToString(double num) {
		final int K = 1024;
		final int M = 1024 * 1024;
		final java.text.DecimalFormat df = new java.text.DecimalFormat("#.0");
		if (num > M) {
			return df.format(num * 1.0 / M) + "M";
		} else if (num > K) {
			return df.format(num * 1.0 / K) + "K";
		}
		return "" + df.format(num);
	}

}

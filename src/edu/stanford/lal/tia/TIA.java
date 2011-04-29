package edu.stanford.lal.tia;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;
import android.content.Context;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import java.util.Vector;

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

	Vector<NameValue> COUNTRIES = new Vector<NameValue>();
	COUNTRIES.add(new NameValue("Test1", "t"));
	COUNTRIES.add(new NameValue("Test2", "ts"));
	COUNTRIES.add(new NameValue("Test3", "te"));
	COUNTRIES.add(new NameValue("Test4", "te"));
	COUNTRIES.add(new NameValue("Test5", "te2"));

	setListAdapter(new ArrayAdapter<NameValue>(this, R.layout.list_item, COUNTRIES));

	ListView lv = getListView();
	lv.setTextFilterEnabled(true);
	lv.setOnItemClickListener(new OnClick(getApplicationContext()));

	Log.d(TAG, "Starting TIA...");
    }

    @Override
	public void onDestroy()
    {
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

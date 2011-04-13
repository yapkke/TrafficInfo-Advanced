package org.openflow.android.tia;

import android.content.Context;
import android.util.Log;
import org.sqlite.helper.*;

/** Database class for TIA's SQLite database
 *
 *  @author ykk
 *  @date Apr 2011
 */
public class Database
    extends org.sqlite.helper.Database
{
    /** Default filename
     */
    public static final String filename = "TIA.sqlite";

    /** Debug name
     */
    private static final String TAG = "TIASqlDb";

    public Database(Context context)
    {
	super(context, filename);
    }

    public void createTables()
    {
	SQLiteTable tab = new SQLiteTable("TIA_FLOW");
	tab.addColumn("App", SQLiteTable.DataType.TEXT);
	OpenFlow.addOFMatch2Table(tab);
	addTable(tab);
    }
}


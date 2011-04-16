package edu.stanford.holyc;

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
    public static final String filename = "Lal.sqlite";

    /** Debug name
     */
    private static final String TAG = "LalDatabase";

    /** Name of table
     */
    public static final String TABLE_NAME = "Lal_Flow_Removed";

    public Database(Context context)
    {
	super(context, filename);
    }

    public void createTables()
    {
	SQLiteTable tab = new SQLiteTable(TABLE_NAME);
	tab.addColumn("App", SQLiteTable.DataType.TEXT);
	OpenFlow.addOFFlowRemoved2Table(tab);
	addTable(tab);
    }
}


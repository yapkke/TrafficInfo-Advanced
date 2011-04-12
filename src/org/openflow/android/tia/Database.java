package org.openflow.android.tia;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.database.sqlite.*;
import java.io.File;

/** Database Open Helper
 * 
 * As recommended in Android development guide
 *
 * @author ykk
 * @date Apr 2011
 */
class DBOpenHelper extends SQLiteOpenHelper 
{
    /** Database version
     */
    public static final int DATABASE_VERSION = 2;
    /** Debug name
     */
    private static final String TAG = "DBOpenHelper";


    /** Name of table
     */
    private static final String TABLE_NAME = "TIA_FLOW";
    private static final String TABLE_CREATE =
	"CREATE TABLE " + TABLE_NAME + " (" +
        "KEYWORD TEXT, " +
	"KEYDEF TEXT);";
    
    /** Constructor
     *
     *  @param context application context
     *  @param name filename
     */
    DBOpenHelper(Context context, String name) 
    {
	this(context, name, DATABASE_VERSION);
    }

    /** Constructor
     *
     *  @param context application context
     *  @param name filename
     *  @param version database version
     */
    DBOpenHelper(Context context, String name, int version) 
    {
	super(context, name, null, version);
    }

    @Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
	;
    }
   
    @Override
	public void onCreate(SQLiteDatabase db) 
    {
	Log.d(TAG, "Create table");
        db.execSQL(TABLE_CREATE);
    }
}

/** Database class for SQLite database on external storage device
 *
 *  @author ykk
 *  @date Apr 2011
 */
public class Database
{
    /** Default filename
     */
    public static final String filename = "DB.sqlite";

    /** Debug name
     */
    private static final String TAG = "SQLiteDB";

    /** State of database
     */
    public boolean state = false;

    /** Constructor
     *
     *  @param context application context
     */
    public Database(Context context)
    {
	this(context, filename);
    }

    /** Constructor
     *
     * @param context application context
     * @param filename filename of database
     */
    public Database(Context context, String filename)
    {
	if (checkExtStore())
	{
	    //External storage ok, start DB now
	    this.state = true;

	    File file = new File(context.getExternalFilesDir(null), filename);
	    DBOpenHelper doh = new DBOpenHelper(context,
						file.getAbsolutePath());
	    Log.d(TAG, "Started database at "+file.getAbsolutePath());	    
	}
	else
	{
	    //External storage failed
	    Log.e(TAG, "External storage not available!  Database failed...");
	    this.state = false;
	}
    }

    /** Check if external storage is okay
     *
     * @return true if external storage is writeable
     */
    public boolean checkExtStore()
    {
	String state = Environment.getExternalStorageState();
	return Environment.MEDIA_MOUNTED.equals(state);
    }

}


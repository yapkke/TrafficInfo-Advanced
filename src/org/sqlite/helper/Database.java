package org.sqlite.helper;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.database.sqlite.*;
import java.io.File;
import java.util.*;
import org.sqlite.helper.*;

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
    public static final int DATABASE_VERSION = 3;
    /** Debug name
     */
    private static final String TAG = "DBOpenHelper";

    /** List of tables
     */
    public Vector<SQLiteTable> tables = new Vector<SQLiteTable>();
    
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
	Iterator<SQLiteTable> i = tables.iterator();
	while (i.hasNext())
	{
	    String t = i.next().createStat();
	    db.execSQL(t);
	    Log.d(TAG, t);	    
	}
    }
}

/** Database class for SQLite database on external storage device
 *
 *  @author ykk
 *  @date Apr 2011
 */
public abstract class Database
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
    /** Reference to DB helper
     */
    protected DBOpenHelper doh;
    /** Reference to DB
     */
    public SQLiteDatabase db = null;

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
	    doh = new DBOpenHelper(context, file.getAbsolutePath());
	    Log.d(TAG, "Started database at "+file.getAbsolutePath());

	    //Create database
	    createTables();
	    db = doh.getWritableDatabase();

	}
	else
	{
	    //External storage failed
	    Log.e(TAG, "External storage not available!  Database failed...");
	    this.state = false;
	}
    }

    /** Close database
     */
    public void close()
    {
	if (db != null)
	{
	    db.close();
	    db = null;
	}
    }

    /** Add table to database
     *
     * @param tab reference to SQLiteTable
     */
    public void addTable(SQLiteTable tab)
    {
	doh.tables.add(tab);
    }

    /** Function to create tables and add them via addTable
     */
    public abstract void createTables();

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

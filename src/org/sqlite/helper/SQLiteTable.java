package org.sqlite.helper;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import java.util.Vector;

/** SQLite table
 *
 * @author ykk
 * @date Apr 2011
 */
public class SQLiteTable
{
    /** Datatype for SQLite ver 3
     */
    public static enum DataType 
    {
	NULL, INTEGER, REAL, TEXT, BLOB
    }

    public static String getDataTypeName(DataType type)
    {
	switch(type)
	{
	case NULL:
	    return "NULL";
	case INTEGER:
	    return "INTEGER";
	case REAL:
	    return "REAL";
	case TEXT:
	    return "TEXT";
	case BLOB:
	    return "BLOB";
	}

	return null;
    }

    /** Name of table
     */
    private String name;
    
    /** Name of columns
     */
    public Vector<String> columns = new Vector<String>();

    /** Type of columns
     */
    public Vector<DataType> colType = new Vector<DataType>();

    /** Constructor
     *	
     * @param name name of table
     */
    public SQLiteTable(String name)
    {
	this.name = name;
    }

    /** Generate create statement
     *
     * @return statement to create table
     */
    public String createStat()
    {
	String stat = "CREATE TABLE "+name+" (";
	for (int i = 0; i < columns.size(); i++)
	{
	    stat += columns.get(i)+" "+getDataTypeName(colType.get(i));
	    if (i < (columns.size()-1))
		stat += ", ";
	}
	stat += ");";
	return stat;
    }

    /** Add column
     */
    public void addColumn(String name, DataType type)
    {
	columns.add(name);
	colType.add(type);
    }

    /** Get name of table
     *
     * @return name of table
     */
    public String name()
    {
	return name;
    }

    /** Insert values
     *
     * @param db reference to database
     * @param values values to insert
     * @return row number or -1 if error occured
     */
    public long insert(SQLiteDatabase db, ContentValues values)
    {
	return db.insert(name, null, values);
    }
}
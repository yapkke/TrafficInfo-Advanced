package org.sqlite.helper;

import java.util.Vector;

import android.database.sqlite.SQLiteCursor;

import com.google.gson.Gson;



/** Class to help with SQLiteCursor
 * @author ykk
 */
public class CursorHelper
{
    public static Gson gson = new Gson();

    /** Return appropriate object for column
     *
     * @return String for string;
     *         Long for long/integer;
     *         Double for float/double;
     *         else null
     */
    public static Object getObject(SQLiteCursor c, int columnIndex)
    {
	if (c.isNull(columnIndex))
	    return null;
	if (c.isString(columnIndex))
	    return c.getString(columnIndex);
	if (c.isFloat(columnIndex))
	    return new Double(c.getDouble(columnIndex));
	if (c.isLong(columnIndex))
	    return new Long(c.getLong(columnIndex));
	
	return null;
    }

    /** Return appropriate string for object in column
     */
    public static String getString(SQLiteCursor c, int columnIndex)
    {
	if (c.isNull(columnIndex))
	    return "N";
	
	if (c.isString(columnIndex))
	    return "S"+gson.toJson(c.getString(columnIndex),
				   String.class);

	if (c.isFloat(columnIndex))
	    return "D"+gson.toJson(new Double(c.getDouble(columnIndex)),
				   Double.class);

	if (c.isLong(columnIndex))
	    return "L"+gson.toJson(new Long(c.getLong(columnIndex)),
				   Long.class);
	
	return "N";
    }
    
    /** Return vector with appropriate objects for a row
     */
    public static Vector<String> getRow(SQLiteCursor c)
    {
	Vector<String> v = new Vector<String>();
	int count = c.getColumnCount();
	for (int i = 0; i < count; i++)
	    v.add(CursorHelper.getString(c,i));
	
	return v;
    }
    
    /** Interpret row
     */
    public static Vector decipherRow(Vector<String> in)
    {
	Vector v = new Vector();
	for (int i = 0; i < in.size(); i++)
	{
	    String s = (String) in.get(i);
	    if (s.startsWith("N"))
		v.add(null);
	    else if (s.startsWith("S"))
		v.add(gson.fromJson(s.substring(1), String.class));
	    else if (s.startsWith("D"))
		v.add(gson.fromJson(s.substring(1), Double.class));
	    else if (s.startsWith("L"))
		v.add(gson.fromJson(s.substring(1), Long.class));
	}
	return v;
    }
}
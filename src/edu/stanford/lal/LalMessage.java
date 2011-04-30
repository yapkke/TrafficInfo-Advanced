package edu.stanford.lal;

import java.util.Vector;

/** Class to contain query for Lal
 *
 * @author ykk
 * @date Apr 2011
 */
public class LalMessage
{
    /** Intent to send query to Lal
     *
     * @see LalQuery
     */
    public class Query{
	public static final String action = "lal.intent.query";
	public static final String str_key = "LALQUERY";
    }
    
    /** Intent to send result of query from Lal
     *
     * @see LalResult
     */
    public class Result{
	public static final String action = "lal.intent.result";
	public static final String str_key = "LALRESULT";
    }


    public class LalResult
    {
	public String[] columns = null;

	public Vector<Vector<String>> results = new Vector<Vector<String>>();
    }

    public class LalQuery
    {
	/** Return distinct values
	 */
	public boolean distinct = false;
	/** Columns to return
	 */
	public String[] columns = null;
	/** The SQL WHERE clause
	 */
	public String selection = null;
	/** May include ?s in selection, 
	 * which will be replaced by the values from selectionArgs
	 */
	public String[] selectionArgs = null;
	/** Group results by
	 */
	public String groupBy = null;
	/** A filter declare which row groups to include in the cursor
	 */
	public String having = null;
	/** Order result with
	 */
	public String orderBy = null;
	/** Limit result to size
	 */
	public String limit = null;
    }
}

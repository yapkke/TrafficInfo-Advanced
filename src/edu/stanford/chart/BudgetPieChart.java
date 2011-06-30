package edu.stanford.chart;

import org.achartengine.ChartFactory;
import org.achartengine.renderer.DefaultRenderer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;

/**
 * Budget demo pie chart.
 */
public class BudgetPieChart extends AbstractDemoChart {
    
	String[] legends;
	double[] values;
	String title;
    final int[] colors = new int[] { Color.BLUE, Color.RED, Color.CYAN, Color.MAGENTA,
				     Color.YELLOW };

    /**
     * Debug name
     */
    private static final String TAG = "PieChart";
	
	/**
	 * Class Constructor
	 */
	public BudgetPieChart(String title, String[] legends, double[] values) {
		this.title = title;
		this.legends = legends;
		this.values = values;
	}
	
	/**
	 * Returns the chart name.
	 * 
	 * @return the chart name
	 */
	public String getName() {
		return "Budget chart";
	}

	/**
	 * Returns the chart description.
	 * 
	 * @return the chart description
	 */
	public String getDesc() {
		return "The budget per project for this year (pie chart)";
	}

	/**
	 * Executes the chart demo.
	 * 
	 * @param context
	 *            the context
	 * @return the built intent
	 */
	public Intent execute(Context context) {
	    int[] colors = new int[this.values.length];
	    for (int i = 0; i < this.values.length; i++) {
        	colors[i] = this.colors[i%this.colors.length];
	    }
	
	    DefaultRenderer renderer = buildCategoryRenderer(colors);
	    renderer.setLabelsTextSize(14);
	    renderer.setLegendTextSize(20);
	    return ChartFactory.getPieChartIntent(context,
						  buildCategoryDataset("Traffic", this.legends, this.values), renderer,
						  this.title);
	}

}

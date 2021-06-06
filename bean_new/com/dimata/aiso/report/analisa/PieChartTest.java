/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.report.analisa;

import java.io.PrintWriter;
import java.util.Vector;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.data.general.DefaultPieDataset;

/**
 *
 * @author dwi
 */
public class PieChartTest {
    
    public static String[][] chartLabel ={
	{"Salary","Electricity","Depreciation","Development","Research"},
	{""+2.5,""+0.5,""+15.0,""+1.5,""+25.5,""+8.5}
    };
    
    public static String getChart(JspWriter out, String sChartRoot, HttpSession session, Vector vDataSet){
	String fileName = null;
	DefaultPieDataset pd = new DefaultPieDataset();
	String sResult = "";
	try
	{

	    for(int cnt=0;cnt < chartLabel[0].length;cnt++){
	      pd.setValue(chartLabel[0][cnt],Double.parseDouble(chartLabel[1][cnt]));
	   }

	   JFreeChart chart=ChartFactory.createPieChart3D("Expenses Share",pd, false,false, false);
	    chart.setBorderVisible(false);
	    chart.setTitle("Expenses Share");

	//  Write the chart image to the temporary directory
	    ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());


	   fileName = ServletUtilities.saveChartAsJPEG(chart, 500, 300, info, session);
	   PrintWriter pw = new PrintWriter(out);

	  //  Write the image map to the PrintWriter
	   ChartUtilities.writeImageMap(pw, fileName, info, true);
	   pw.flush();
	}

	
	catch(Exception e)
	{
		e.printStackTrace();
	}
	sResult = sChartRoot + fileName;
	return sResult;
    }
}

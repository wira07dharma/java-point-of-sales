/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.report.analisa;


import com.dimata.aiso.entity.search.SrcPieChart;
import com.dimata.aiso.session.analysis.SessChartDataSet;
import java.io.PrintWriter;
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
public class ChartGenerator {
    
    public static final int PIE_CHART = 0;
    public static final int BAR_CHART = 1;
    
    public static final int PIE_CHART_REVENUE_COMPOSITION = 0;
    public static final int PIE_CHART_REVENUE_CONTRIBUTION = 1;
    public static final int PIE_CHART_EXPENSES = 3;
    public static final int PIE_CHART_NET_WORKING_CAPITAL = 4;
    
     public static String generatePieChart(SrcPieChart srcPieChart, int iAccountGroup, int iChartRevType){
	String fileName = null;
	DefaultPieDataset pd = new DefaultPieDataset();
	String sResult = "";
	try{
	    pd = SessChartDataSet.getDataSet(srcPieChart.getRecordToGet(), srcPieChart.getStartDate(), 
		    srcPieChart.getEndDate(), srcPieChart.getIsLoadNewData(), srcPieChart.getInAmount(), 
		    srcPieChart.getShortOrientation(), srcPieChart.getOtherLabel(),iAccountGroup,PIE_CHART,iChartRevType);
	   
	    JFreeChart chart=ChartFactory.createPieChart3D(srcPieChart.getChartTitle(),pd, true,true, false);
	    chart.setBorderVisible(false);
	    chart.setTitle(srcPieChart.getChartTitle());
	
	    ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());

	    fileName = ServletUtilities.saveChartAsJPEG(chart, 390, 250, info, srcPieChart.getHttpSession());
	    PrintWriter pw = new PrintWriter(srcPieChart.getJspWriter());
	 
	    ChartUtilities.writeImageMap(pw, fileName, info, true);
	    pw.flush();
	}catch(Exception e){
		e.printStackTrace();
	}
	
	sResult = srcPieChart.getChartRoot() + fileName;
	
	return sResult;
    }
}

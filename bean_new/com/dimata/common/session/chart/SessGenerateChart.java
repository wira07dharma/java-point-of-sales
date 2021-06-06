/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.common.session.chart;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author dimata005
 */
public class SessGenerateChart {
    
      //GENERATE BAR & COLUMN CHART
    public void generateLineChartMonth(String chartType, String dataChart, 
	    String titleChartName, String subtitleChartName, 
	    String categoriesTitleName, JSONObject chart,
	    ArrayList<String> listCategories, String toolTips){
	
	//SET CHART OPTIONS
	JSONArray categories = new JSONArray();
	
	JSONObject categoriesTitle = new JSONObject();
	
	JSONObject chartOptions = new JSONObject();
	JSONObject titleChart = new JSONObject();
	JSONObject subtitleChart = new JSONObject();
	JSONObject xAxisChart = new JSONObject();
	JSONObject yAxisChart = new JSONObject();
	JSONObject tooltipChart = new JSONObject();
	JSONObject plotOptions = new JSONObject();
	JSONObject rangeOptions = new JSONObject();
	JSONObject month = new JSONObject();
	JSONObject label = new JSONObject();
	
	
	
	try{
	    chartOptions.put("type", chartType);
	    chartOptions.put("renderTo", dataChart);
	    titleChart.put("text", titleChartName);

	    subtitleChart.put("text", "");
	    categoriesTitle.put("text", "");
	    xAxisChart.put("categories", listCategories);
	    
	    chart.put("chart", chartOptions);
	    chart.put("title", titleChart);
	    chart.put("subtitle", subtitleChart);
	    chart.put("xAxis",xAxisChart);
	    chart.put("plotOptions", plotOptions);
	}catch(Exception ex){

	}
    }
    
    //GENERATE BAR & COLUMN CHART
    public void generateBarChart(String chartType, String dataChart, 
	    String titleChartName, String subtitleChartName, 
	    String categoriesTitleName, JSONObject chart,
	    ArrayList<String> listCategories, String toolTips){
	
	//SET CHART OPTIONS
	JSONArray categories = new JSONArray();
	
	JSONObject categoriesTitle = new JSONObject();
	
	JSONObject chartOptions = new JSONObject();
	JSONObject titleChart = new JSONObject();
	JSONObject subtitleChart = new JSONObject();
	JSONObject xAxisChart = new JSONObject();
	JSONObject yAxisChart = new JSONObject();
	JSONObject tooltipChart = new JSONObject();
	JSONObject plotOptions = new JSONObject();
	
	
	
	try{
	    chartOptions.put("type", chartType);
	    chartOptions.put("renderTo", dataChart);
	    titleChart.put("text", titleChartName);

	    subtitleChart.put("text", "");
	    categoriesTitle.put("text", "");
	    xAxisChart.put("categories", listCategories);
	    xAxisChart.put("title", categoriesTitle);

	    yAxisChart.put("min", 0);
	    yAxisChart.put("title", categoriesTitle);

	    tooltipChart.put("valueSuffix", " "+toolTips);

	    chart.put("chart", chartOptions);
	    chart.put("title", titleChart);
	    chart.put("subtitle", subtitleChart);
	    chart.put("xAxis",xAxisChart);
	    chart.put("yAxis", yAxisChart);
	    chart.put("tooltip", tooltipChart);
	}catch(Exception ex){

	}
    }
    
    
    public void generateAreaChart(String chartType, String dataChart, 
	    String titleChartName, String subtitleChartName, 
	    String categoriesTitleName, JSONObject chart,
	    ArrayList<String> listCategories, String toolTips, long oidPajakType){
	
	//SET CHART OPTIONS
	JSONArray categories = new JSONArray();
	
	JSONObject categoriesTitle = new JSONObject();
	
	JSONObject chartOptions = new JSONObject();
	JSONObject titleChart = new JSONObject();
	JSONObject subtitleChart = new JSONObject();
	JSONObject xAxisChart = new JSONObject();
	JSONObject yAxisChart = new JSONObject();
	JSONObject tooltipChart = new JSONObject();
	JSONObject plotOptions = new JSONObject();
	JSONObject month = new JSONObject();
	
	
	
	try{
	    chartOptions.put("type", chartType);
	    chartOptions.put("renderTo", dataChart+""+oidPajakType);
	    titleChart.put("text", titleChartName);

	    subtitleChart.put("text", "");
	    categoriesTitle.put("text", "");
	    xAxisChart.put("type", "datetime");
	    month.put("month", "%b-%y");
	    xAxisChart.put("dateTimeLabelFormats", month);

	    chart.put("chart", chartOptions);
	    chart.put("title", titleChart);
	    chart.put("subtitle", subtitleChart);
	    chart.put("xAxis",xAxisChart);
	}catch(Exception ex){

	}
    }
}

/**
 *
 * JFreeChart version 0.9.20
 * Called by Pie3DDemo.jsp
 *
 */
package com.dimata.posbo.report.grafik;

import com.lowagie.text.Image;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.urls.StandardPieURLGenerator;
import org.jfree.data.general.DefaultPieDataset;
//import org.jfree.data.general.PieDataset;

public class Pie3DDemo {

    public Pie3DDemo() {
    }

    private DefaultPieDataset getDataset() {
	// categories...
	String[] section = new String[]{
	    "Jan", "Feb", "Mar", "Apr", "May", "Jun",
	    "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
	};

	    
	// data...
	double[] data = new double[section.length];
	for (int i = 0; i < data.length; i++) {
	    data[i] = 10 + (Math.random() * 10);
	}

	   
	// create the dataset...
	DefaultPieDataset dataset = new DefaultPieDataset();
	for (int i = 0; i < data.length; i++) {
	    dataset.setValue(section[i], data[i]);
	}
	return dataset;
    }
    

    public String getChartViewer(HttpServletRequest request, HttpServletResponse response) {
	DefaultPieDataset dataset = getDataset(); 
	System.out.println("masuk");
	// create the chart...
	

	JFreeChart chart = ChartFactory.createPieChart(	
		"Pie3D Chart Demo", // chart title
		dataset, // data
		true, // include legend
		true,
		false);

	System.out.println("masuk"); 
	// set the background color for the chart...
	chart.setBackgroundPaint(Color.cyan);
	PiePlot plot = (PiePlot) chart.getPlot();
	plot.setNoDataMessage("No data available");

	// set drilldown capability...
	plot.setURLGenerator(new StandardPieURLGenerator("Bar3DDemo.jsp", "section"));
	plot.setLabelGenerator(null);

	// OPTIONAL CUSTOMISATION COMPLETED.

	ChartRenderingInfo info = null;
	HttpSession session = request.getSession();
	try {

	    //Create RenderingInfo object
	    response.setContentType("text/html");
	    info = new ChartRenderingInfo(new StandardEntityCollection());
	    BufferedImage chartImage = chart.createBufferedImage(640, 400, info);

	    // putting chart as BufferedImage in session, 
	    // thus making it available for the image reading action Action.
	    session.setAttribute("chartImage", chartImage);

	    PrintWriter writer = new PrintWriter(response.getWriter());
	    ChartUtilities.writeImageMap(writer, "imageMap", info, true);


	    writer.flush();

	} catch (Exception e) {
	// handel your exception here
	}

	String pathInfo = "http://";
	pathInfo += request.getServerName();
	int port = request.getServerPort();
	pathInfo += ":" + String.valueOf(port);
	pathInfo += request.getContextPath();
	String chartViewer = pathInfo + "/servlet/ChartViewer";
	return chartViewer;
    }
}

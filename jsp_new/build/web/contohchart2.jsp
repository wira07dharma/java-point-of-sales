<%-- 
    Document   : contohchart2
    Created on : Jul 8, 2008, 10:04:22 AM
    Author     : gadnyana
--%>
<%@ page import = "org.jfree.chart.*, java.awt.* , org.jfree.ui.*, org.jfree.data.general.*, java.util.*, org.jfree.chart.servlet.*, org.jfree.chart.entity.*, java.io.*"  %>

<HTML>
<HEAD>
<TITLE>Teresa Charts</TITLE>
</HEAD>
<BODY background="./Images/bground.jpg">
 
<%
String fileName = null; //dont forget to add this
DefaultPieDataset pd = new DefaultPieDataset();
try
{
    Vector alValues = new Vector();
	alValues.add("200");
	alValues.add("100");
	alValues.add("200");

	alValues.add("50");
	alValues.add("80");
	alValues.add("90");
	
    for(int cnt=0;cnt < alValues.size();cnt++){
      int val = Integer.parseInt((alValues.get(cnt).toString().trim()));
      pd.setValue("nama "+cnt,val);
   }
 
   JFreeChart chart=ChartFactory.createPieChart3D("",pd, false,false, false);
   request.getSession().setAttribute("contentType", "image/jpg");
	chart.setBorderVisible(false);
	chart.setTitle("Grafik Pilkada");
	
    //  Write the chart image to the temporary directory
	ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
   //info.
   //System.out
   
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
String path = request.getContextPath() + "/servlet/DisplayChart?filename=" + fileName;
%>
 
<div align="center"><img src="<%=path%>" border=0></div>


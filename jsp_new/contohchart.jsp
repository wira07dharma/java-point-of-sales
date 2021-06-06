<html>
<head>
<title>Pie Chart Demo</title>
</head>

<jsp:useBean id="myChart" scope="session" class="com.dimata.posbo.report.grafik.Pie3DDemo" />

<body>
<h2>Pie Chart Demo</h2>

<%String chartViewer = myChart.getChartViewer(request, response);%>

<img src="<%=chartViewer%>" border=0 usemap="#imageMap">

</body>
</html> 
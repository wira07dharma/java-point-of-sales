<%-- 
    Document   : mainmenumobile
    Created on : Jan 6, 2015, 8:11:13 PM
    Author     : dimata005
--%>

<%@page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterial"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.posbo.entity.masterdata.TableRoom"%>
<%@page import="com.dimata.posbo.entity.masterdata.Room"%>
<%@page import="com.dimata.pos.entity.billing.BillMain"%>
<%@page import="com.dimata.pos.entity.billing.PstBillMain"%>
<%@page import="java.util.Vector"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>

<%@ include file = "../main/javainit.jsp" %>
<%
    
    int iCommand = FRMQueryString.requestCommand(request);
    //String whereOpenBill=" DOC_TYPE=0 AND TRANSACTION_TYPE=0 AND TRANSACTION_STATUS=1";
    //Vector listTransaction = PstBillMain.listOpenBill(0, 0,whereOpenBill, "");
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    Date now = new Date(); 
    
    Vector listData = new Vector(1, 1);
    String priceType = PstSystemProperty.getValueByName("PRICE_TYPE_SHOPPING_CHART");
    String standartRate = PstSystemProperty.getValueByName("ID_STANDART_RATE");
    String whereCatalog = "p.EDIT_MATERIAL='4'";
    String order = " " + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID];
    listData = PstMaterial.listShopingCart(0, 0, whereCatalog, order, standartRate, priceType);
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Taking Order - Queens Bali</title>
<%--
<link href="../styles/takingorder/css/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="../styles/takingorder/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<!-- jQuery -->
<script src="../styles/takingorder/js/jquery.min.js"></script>
<!-- Tablesorter: required -->
<link href="../styles/takingorder/css/theme.dropbox.css" rel="stylesheet">
<script src="../styles/takingorder/js/jquery.tablesorter.js"></script>
<script src="../styles/takingorder/js/jquery.tablesorter.widgets.js"></script>
<!-- Tablesorter: pager -->
<link rel="stylesheet" href="../styles/takingorder/js/jquery.tablesorter.pager.css">
<script src="../styles/takingorder/js/widgets/widget-pager.js"></script>
--%>
<link href="../styles/takingorder/css/bootstrap.css" rel="stylesheet" type="text/css">
<link href="../styles/takingorder/css/bootstrap_002.css" rel="stylesheet" type="text/css">
<!-- jQuery -->
<script src="../styles/takingorder/js/jquery.js"></script>
<script type="text/javascript" src="../styles/jquery.min.js"></script>
<!-- Tablesorter: required -->
<link href="../styles/takingorder/css/theme.css" rel="stylesheet">
<script src="../styles/takingorder/js/jquery_002.js"></script>
<script src="../styles/takingorder/js/jquery_003.js"></script>
<!-- Tablesorter: pager -->
<link rel="stylesheet" href="../styles/takingorder/js/jquery.css">
<script src="../styles/takingorder/js/widget-pager.js"></script>
<script type="text/javascript" src="../styles/takingorder/js/bootstrap.min.js"></script>

</head>

<body>
<div style="background:#f0f0f0; margin-bottom:10px; padding:5px;">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td style="text-align:left;">
	<i><strong><%=salesName%></strong> <span id="cartorder"></span></i>
    </td>
    <td style="text-align:right;">
	<i>Date : <%=dateFormat.format(now)%></i>
    </td>
  </tr>
</table>
</div>
<div class="container">
<center>
<a href="#"><img src="../styles/takingorder/img/queens-head.png" width="250" border="none" /></a>
</center>
<div style="margin-top:20px;">
<a href="<%=approot%>/logout.jsp">Log Out</a>
<hr style="margin-top:10px;" />
<style type="text/css">
.items{
	-webkit-border-radius: 5px;
	-moz-border-radius: 5px;
	border-radius: 5px;
	width:46%; 
	float:left; 
	background:#F0F0F0; 
	margin:5px 2%; 
	padding:5px; 
	height:145px; 
	text-align:center;
}
.items:hover{
	background:#F90;
}
</style>
<form method="post" style="margin-bottom:10px;">
    <div class="items">
        <strong>DINE IN</strong><br />
         <a href="tableorder.jsp"><img src="../styles/takingorder/img/dine_in.png" alt="Smiley face" width="110"></a><br> <br>
    </div>
    <div class="items">
        <strong>TAKE A WAY</strong><br />
         <a href="takeaway.jsp"><img src="../styles/takingorder/img/takeaway.png" alt="Smiley face" width="110"></a><br> <br>
    </div>
    <div class="items">
        <strong>DELIVERY</strong><br />
        <a href="delivery_order.jsp"><img src="../styles/takingorder/img/delivery.png" alt="Smiley face" width="110"></a><br> <br>
    </div>
    <div class="items">
        <strong>WAITING LIST</strong><br />
        <a href="waiting_list.jsp"><img src="../styles/takingorder/img/waitinglist.png" alt="Smiley face" width="110"></a><br> <br>
    </div>
    <div style="clear:left; margin-bottom:10px;"></div>
    
    <%--<button name="submit" class="btn btn-danger btn-md btn-block" type="button">NEWS & INFO</button>--%>
    <button class="btn btn-danger btn-md btn-block" type="button" data-toggle="modal" data-target="#myModal">NEWS & INFO</button>
    
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <h4 class="modal-title" id="myModalLabel">INFORMATION</h4>
          </div>
          <div class="modal-body">
              <%-- body--%>
              <div class="guest" style="margin:10px 0px;">
               <div class="form-group">
                   <div class="col-md-12" style="margin-bottom:10px;">
                    <b>List Menu Not Available</b><br>
                    <%
                    int count=0;
                    int multiLanguageName = Integer.parseInt((String) com.dimata.system.entity.PstSystemProperty.getValueIntByName("NAME_MATERIAL_MULTI_LANGUAGE"));
                                    
                    for (int i = 0; i < listData.size(); i++) {
                    Vector vt = (Vector) listData.get(i);
                    Material material = (Material) vt.get(0);
                    count=count+1;
                    String[] smartPhonesSplits = material.getName().split("\\;");
                    String nameMat = "";
                    if (multiLanguageName == 1) {
                        try {
                            nameMat = smartPhonesSplits[0];
                        } catch (Exception ex) {
                            //rowx.add("");
                        }
                    } else {
                        //rowx.add(material.getName());
                        nameMat = material.getName();
                    }
                    %>
                        <b><%=count%></b>) <%=nameMat%><br>
                    <%}
                    %>
                   </div>
               </div>
             </div> 
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
          </div>
        </div>
      </div>
    </div>   
    
</form>
</div>
<hr />
<div style="margin:20px 0px; text-align:center;">
</div>
</div>
</body></body>
</html>
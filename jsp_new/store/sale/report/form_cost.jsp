<%-- 
    Document   : form_cost
    Created on : Jul 29, 2016, 12:46:44 PM
    Author     : dimata005
--%>

<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.posbo.report.sale.SalesUpdateCost"%>
<%@page import="com.dimata.posbo.entity.admin.AppObjInfo"%>
<%@page import="com.dimata.pos.entity.billing.Billdetail"%>
<%@page import="com.dimata.posbo.report.sale.SaleReportItem"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.dimata.posbo.entity.search.SrcSaleReport"%>
<%@page import="com.dimata.posbo.report.sale.SaleReportDocument"%>
<%@page import="java.util.Vector"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_REPORT, AppObjInfo.OBJ_GROSS_MARGIN);%>
<%@ include file = "../../../main/checkuser.jsp" %>

<!DOCTYPE html>
<%    
    int iCommand = FRMQueryString.requestCommand(request);
    long oidLocation = FRMQueryString.requestLong(request, "location");
    String dateto = FRMQueryString.requestString(request, "dateto");
    String datefrom = FRMQueryString.requestString(request, "datefrom");
    long materialIdxx = FRMQueryString.requestLong(request, "materialIdxx");

    DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    Date startDate = df.parse(dateto);
    Date endDate = df.parse(datefrom);

    Vector list = new Vector();
    SaleReportDocument saleReportDocument = new SaleReportDocument();
    SrcSaleReport srcSaleReport = new SrcSaleReport();
    srcSaleReport.setLocationId(oidLocation);
    srcSaleReport.setDateTo(startDate);
    srcSaleReport.setDateFrom(endDate);
    srcSaleReport.setItemId(materialIdxx);
    
    if(materialIdxx!=0){
        if(iCommand==Command.UPDATE){
            list = saleReportDocument.getListBillDetailCost(srcSaleReport);
        }
    }
    SalesUpdateCost salesUpdateCost = new SalesUpdateCost();
    
    if(materialIdxx!=0){
        int act = salesUpdateCost.action(list);
    }
    
    list = saleReportDocument.getListBillDetailCost(srcSaleReport);
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <%if (menuUsed == MENU_ICON) {%>
        <link href="../../../stylesheets/general_home_style.css" type="text/css"
              rel="stylesheet" />
        <%}%>
        <link rel="stylesheet" href="../../../styles/main.css" type="text/css">
        <link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
        <script language="JavaScript">
            function cmdUpdate() {
                    document.frm_reportsale.command.value="<%=Command.UPDATE%>";
                    document.frm_reportsale.action="form_cost.jsp";
                    document.frm_reportsale.submit();
                }
        </script>
    </head>
    <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
            <tr> 
                <td valign="top" align="left">
                    <form name="frm_reportsale" method="post" action="">
                        <input type="hidden" name="command" value="">
                        <input type="hidden" name="dateto" value="<%=dateto%>">
                        <input type="hidden" name="datefrom" value="<%=datefrom%>">
                        <input type="hidden" name="location" value="<%=oidLocation%>">
                        <input type="hidden" name="materialIdxx" value="<%=materialIdxx%>">
                        <table width="100%" border="0" cellspacing="1" cellpadding="2" class="listgen">
                            <tr>
                                <td  width ="5%" align="center" nowrap class="listgentitle" >No</td>
                                <td  width ="10%" align="center" nowrap class="listgentitle" >Bill Number</td>
                                <td width  ="35%" align="center" nowrap class="listgentitle" >Nama</td>
                                <td width  ="10%" align="center" nowrap class="listgentitle" >Qty</td>
                                <td  width ="20%" align="center" nowrap class="listgentitle" >Cost Sale </td>
                                <td  width ="20%" align="center" nowrap class="listgentitle">HPP </td>
                            </tr>
                            <%
                                int no = 0;
                                if (list != null && list.size() > 0) {
                                    for (int i = 0; i < list.size(); i++) {
                                        Billdetail billlDetail = (Billdetail) list.get(i);
                                        no = no + 1;
                            %>
                            <tr>
                                <td width ="5%" align="left" nowrap class="listgensell" ><%=no%></td>
                                <td width ="10%" align="left" nowrap class="listgensell" ><%=billlDetail.getNoBill()%></td>
                                <td width ="35%" align="left" nowrap class="listgensell" ><%=billlDetail.getItemName()%></td>
                                <td width ="10%" align="center" nowrap class="listgensell" ><%=billlDetail.getQty() %></td>
                                <td width ="20%" align="right" nowrap class="listgensell" ><%=billlDetail.getCost()%></td>
                                <td width ="20%" align="right" nowrap class="listgensell"><%=billlDetail.getItemPriceStock()%></td>
                            </tr>
                            <%
                                    }
                                }
                            %>
                            <tr>
                                <td width="5%" valign="top"><a href="javascript:cmdUpdate()"><img src="<%=approot%>/images/BtnSave.jpg" width="31" height="27" border="0"></a></td>
                                <td width="45%" nowrap>&nbsp; <a href="javascript:cmdUpdate()" class="command" >Update</a>
                                </td>
                            </tr>
                        </table>
                    </form>
                </td>
            </tr>
            <tr> 
                <td colspan="2" height="20"> <!-- #BeginEditable "footer" --> 
                    <%if (menuUsed == MENU_ICON) {%>
                    <%@include file="../../../styletemplate/footer.jsp" %>

                    <%} else {%>
                    <%@ include file = "../../../main/footer.jsp" %>
                    <%}%>
                    <!-- #EndEditable --> </td>
            </tr>
        </table>
    </body>
</html>

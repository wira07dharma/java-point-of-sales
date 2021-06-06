<%-- 
    Document   : listtransaction
    Created on : May 13, 2014, 6:08:20 PM
    Author     : dimata005
--%>

<%-- 
    Document   : outletonline
    Created on : May 2, 2014, 4:31:21 PM
    Author     : dimata005
--%>
<%@page import="com.dimata.pos.session.billing.SessBilling"%>
<%@page import="com.dimata.posbo.entity.shoppingchart.ShopCart"%>
<%@page import="com.dimata.posbo.ajax.checkStockOutletOrder"%>
<%@ page language = "java"  %>
<%@ page import = " java.util.* ,
         com.dimata.posbo.printing.purchasing.InternalExternalPrinting,
         com.dimata.printman.RemotePrintMan,
         com.dimata.printman.DSJ_PrintObj,
         com.dimata.printman.PrnConfig,
         com.dimata.printman.PrinterHost,
         com.dimata.common.entity.contact.PstContactList,
         com.dimata.common.entity.contact.PstContactClass,
         com.dimata.common.entity.contact.ContactList,
         com.dimata.common.entity.location.PstLocation,
         com.dimata.common.entity.location.Location,
         com.dimata.posbo.entity.masterdata.*,
         com.dimata.qdep.form.FRMHandler,
         com.dimata.gui.jsp.ControlList,
         com.dimata.qdep.entity.I_PstDocType,
         com.dimata.qdep.form.FRMQueryString,
         com.dimata.util.Command,
         com.dimata.qdep.form.FRMMessage,
         com.dimata.gui.jsp.ControlCombo,
         com.dimata.gui.jsp.ControlDate,
         com.dimata.gui.jsp.ControlLine,
         com.dimata.common.entity.payment.PstCurrencyType,
         com.dimata.posbo.entity.warehouse.*,
         com.dimata.common.entity.payment.CurrencyType,
         com.dimata.pos.form.billing.*,
         com.dimata.pos.entity.billing.*,
         com.dimata.posbo.entity.masterdata.*,
         com.dimata.posbo.form.masterdata.*,
         com.dimata.pos.form.balance.*,
         com.dimata.pos.entity.balance.*,
         com.dimata.pos.entity.payment.CashPayments1,
         com.dimata.pos.form.payment.*,
         com.dimata.pos.entity.payment.*,
         com.dimata.pos.form.masterCashier.*,
         com.dimata.pos.entity.masterCashier.*,
         com.dimata.common.entity.payment.*,
         com.dimata.posbo.entity.admin.*"%>
<%@ include file = "../main/javainit.jsp" %>

<%!ShopCart shoppingCart = new ShopCart();

public String drawList(int language,Vector objectClass)
{
	ControlList ctrlist = new ControlList();
	ctrlist.setAreaWidth("100%");
	ctrlist.setListStyle("listgen");
	ctrlist.setTitleStyle("listgentitle");
	ctrlist.setCellStyle("listgensell");
	ctrlist.setHeaderStyle("listgentitle");
	ctrlist.addHeader("NO","4%");
	ctrlist.addHeader("NO NOTA","6%");
	ctrlist.addHeader("TANGGAL","20%");
	ctrlist.addHeader("NAMA TAMU","20%");
        ctrlist.addHeader("ROOM","10%");
        ctrlist.addHeader("NO MEJA","10%");

        ctrlist.setLinkRow(1);
	ctrlist.setLinkPrefix("javascript:cmdEdit('");
	ctrlist.setLinkSufix("')");	
	Vector lstData = ctrlist.getData();
	Vector lstLinkData = ctrlist.getLinkData();
	Vector rowx = new Vector(1,1);
	ctrlist.reset();
	int index = -1;
	int start = 0;
	for(int i = 0; i < objectClass.size(); i++) 
	{
                Vector dataBill = (Vector) objectClass.get(i);
            
		BillMain billMain = (BillMain)dataBill.get(0);
                Room room = (Room) dataBill.get(1);
                
		rowx = new Vector();
                
		start = start + 1;
              
                rowx.add(""+start);
                rowx.add("<a href=\"javascript:cmdEdit('"+String.valueOf(billMain.getOID())+"')\">"+billMain.getInvoiceNumber()+"</a>");
                rowx.add(""+Formater.formatDate(billMain.getBillDate(), "dd-mm-yyy"));
                rowx.add(billMain.getCustName());
                rowx.add("");
                rowx.add("");

		lstData.add(rowx);
	}
	
	return ctrlist.draw();
}
%>

<%
    
    int iCommand = FRMQueryString.requestCommand(request);
    String whereOpenBill=" DOC_TYPE=0 AND TRANSACTION_TYPE=0 AND TRANSACTION_STATUS=1 AND LOCATION_ID='"+locationSales+"'";
    Vector listTransaction = PstBillMain.listOpenBill(0, 0,whereOpenBill, "");
    
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Outlet | Dashboard</title>
        <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>
        <!-- bootstrap 3.0.2 -->
        <link href="../styles/bootstrap3.1/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
        <!-- font Awesome -->
        <link href="../styles/bootstrap3.1/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
        <!-- Ionicons -->
        <link href="../styles/bootstrap3.1/css/ionicons.min.css" rel="stylesheet" type="text/css" />
        <!-- Morris chart -->
        <link href="../styles/bootstrap3.1/css/morris/morris.css" rel="stylesheet" type="text/css" />
        <!-- jvectormap -->
        <link href="../styles/bootstrap3.1/css/jvectormap/jquery-jvectormap-1.2.2.css" rel="stylesheet" type="text/css" />
        <!-- fullCalendar -->
        <link href="../styles/bootstrap3.1/css/fullcalendar/fullcalendar.css" rel="stylesheet" type="text/css" />
        <!-- Daterange picker -->
        <link href="../styles/bootstrap3.1/css/daterangepicker/daterangepicker-bs3.css" rel="stylesheet" type="text/css" />
        <!-- bootstrap wysihtml5 - text editor -->
        <link href="../styles/bootstrap3.1/css/bootstrap-wysihtml5/bootstrap3-wysihtml5.min.css" rel="stylesheet" type="text/css" />
        <!-- Theme style -->
        <link href="../styles/bootstrap3.1/css/AdminLTE.css" rel="stylesheet" type="text/css" />

        <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
          <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
          <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
        <![endif]-->
        <script type="text/javascript" src="../styles/jquery.min.js"></script>
        <script type="text/javascript">
            function saveCheckOut(){
                document.frmsrcsalesorder.command.value="<%=Command.SAVEALL%>";
                document.frmsrcsalesorder.action="outletonline.jsp";
                document.frmsrcsalesorder.submit();
            }
            
             function cmdSave(){
                document.frmsrcsalesorder.command.value="<%=Command.SAVE%>";
                document.frmsrcsalesorder.action="outletonline.jsp";
                document.frmsrcsalesorder.submit();
            }
          function cmdBack(){
                document.frmsrcsalesorder.command.value="<%=Command.BACK%>";
                document.frmsrcsalesorder.action="mobileoutlet.jsp";
                document.frmsrcsalesorder.submit();
            }
        </script>
    </head>
    <body class="skin-blue">
        <div class="wrapper row-offcanvas row-offcanvas-left">
            <!-- Right side column. Contains the navbar and content of the page -->
            <aside class="right-side">
                 <section class="content">
                    <form name="frmsrcsalesorder" method ="post" action="" role="form">
                        <input type="hidden" name="command" value="<%=iCommand%>">
                        <div class="row">
                            <!-- Left col -->
                            <div class="col-xs-12 ">
                                <div class="box box-danger" id="loading-example">
                                     <div class="col-xs-11 ">
                                        <div class="box-content">
                                                <input type="hidden" name="command" value="<%=iCommand%>">
                                                <%=drawList(SESS_LANGUAGE,listTransaction)%>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                         <div class='row'>
                            <div class='col-xs-12'>
                                <div class="col-lg-12 col-xs-12">
                                    &nbsp;
                                </div>
                            </div>
                       </div>
                        <div class='row'>
                            <div class='col-xs-12'>
                                <div class="col-lg-12 col-xs-12">
                                    <button class="btn btn-primary" onclick="javascript:cmdBack()"type="button" >Back Main Menu</button>
                                </div>
                            </div>
                       </div>
                    </form>                   
                 </section>
            </aside>
        </div><!-- ./wrapper -->

        <!-- add new calendar event modal -->


        <!-- jQuery 2.0.2 -->
        <script src="../styles/bootstrap3.1/libs/jquery.min.js"></script>
        <!-- jQuery UI 1.10.3 -->
        <script src="../styles/bootstrap3.1/js/jquery-ui-1.10.3.min.js" type="text/javascript"></script>
        <!-- Bootstrap -->
        <script src="../styles/bootstrap3.1/js/bootstrap.min.js" type="text/javascript"></script>
        <!-- Morris.js charts -->
        <script src="../styles/bootstrap3.1/js/raphael-min.js"></script>
        <script src="../styles/bootstrap3.1/js/plugins/morris/morris.min.js" type="text/javascript"></script>
        <!-- Sparkline -->
        <script src="../styles/bootstrap3.1/js/plugins/sparkline/jquery.sparkline.min.js" type="text/javascript"></script>
        <!-- jvectormap -->
        <script src="../styles/bootstrap3.1/js/plugins/jvectormap/jquery-jvectormap-1.2.2.min.js" type="text/javascript"></script>
        <script src="../styles/bootstrap3.1/js/plugins/jvectormap/jquery-jvectormap-world-mill-en.js" type="text/javascript"></script>
        <!-- fullCalendar -->
        <script src="../styles/bootstrap3.1/js/plugins/fullcalendar/fullcalendar.min.js" type="text/javascript"></script>
        <!-- jQuery Knob Chart -->
        <script src="../styles/bootstrap3.1/js/plugins/jqueryKnob/jquery.knob.js" type="text/javascript"></script>
        <!-- daterangepicker -->
        <script src="../styles/bootstrap3.1/js/plugins/daterangepicker/daterangepicker.js" type="text/javascript"></script>
        <!-- Bootstrap WYSIHTML5 -->
        <script src="../styles/bootstrap3.1/js/plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.all.min.js" type="text/javascript"></script>
        <!-- iCheck -->
        <script src="../styles/bootstrap3.1/js/plugins/iCheck/icheck.min.js" type="text/javascript"></script>

        <!-- AdminLTE App -->
        <script src="../styles/bootstrap3.1/js/AdminLTE/app.js" type="text/javascript"></script>
        
        <!-- AdminLTE dashboard demo (This is only for demo purposes) -->
        <script src="../styles/bootstrap3.1/js/AdminLTE/dashboard.js" type="text/javascript"></script>        

    </body>
    
    
    
    
</html>


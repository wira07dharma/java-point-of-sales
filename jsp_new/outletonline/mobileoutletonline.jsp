<%-- 
    Document   : mobileoutletonline
    Created on : Nov 19, 2014, 1:32:37 PM
    Author     : dimata005
--%>
<%@page import="com.dimata.pos.form.billing.CtrlOpenBillMain"%>
<%@page import="com.dimata.pos.entity.billing.BillMain"%>
<%@page import="com.dimata.qdep.form.FRMMessage"%>
<%@page import="com.dimata.qdep.form.FRMMessage"%>
<%@page import="com.dimata.posbo.entity.masterdata.Room"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstRoom"%>
<%@page import="com.dimata.gui.jsp.ControlCombo"%>
<%@page import="com.dimata.pos.form.billing.CtrlBillMain"%>
<%@page import="com.dimata.gui.jsp.ControlLine"%>
<%@page import="com.dimata.pos.form.billing.FrmBillMain"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.util.Command"%>
<!DOCTYPE html>
<%@ include file = "../main/javainit.jsp" %>


<%
 int iCommand = FRMQueryString.requestCommand(request);
 long cashBillMainId = FRMQueryString.requestLong(request, FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CASH_BILL_MAIN_ID]);
 String billNumber = FRMQueryString.requestString(request, FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_BILL_NUMBER]);
 
 CtrlOpenBillMain ctrlBillMain = new CtrlOpenBillMain(request);
 ControlLine ctrLine = new ControlLine();
 FrmBillMain frmBillMain = ctrlBillMain.getForm();
 
 
 Vector val_locationid = new Vector(1,1);
 Vector key_locationid = new Vector(1,1);
 String whereRoom = PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+"='"+locationSales+"'";
 Vector vt_loc = PstRoom.list(0,0,whereRoom, PstLocation.fieldNames[PstLocation.FLD_CODE]);
 val_locationid.add("0");
 key_locationid.add("All Room");
 for(int d=0;d<vt_loc.size();d++){
        Room room = (Room)vt_loc.get(d);
        val_locationid.add(""+room.getOID()+"");
        key_locationid.add(room.getName());
 }

 int iErrCode = FRMMessage.NONE;
 if(cashBillMainId==0){
    iErrCode = ctrlBillMain.action(iCommand, cashBillMainId, 0);
 } 
 BillMain billMain = ctrlBillMain.getBillMain();
 if(billMain.getOID()!= 0){
        response.sendRedirect("mobileorder.jsp?"+frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CASH_BILL_MAIN_ID]+"="+billMain.getOID());
 }  
%>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Prochain | Dashboard</title>
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
          <script src="../styles/bootstrap3.1/libs/html5shiv.js"></script>
          <script src="../styles/bootstrap3.1/libs/respond.min.js"></script>
        <![endif]-->
        <script type="text/javascript" src="../styles/jquery.min.js"></script>
        <script language="JavaScript">
         function cmdChangeLocation(idRoom){
            //alert("hellp");
            $("#posts").html("");
            $.ajax({
                url : "<%=approot%>/servlet/com.dimata.posbo.ajax.CheckTableFromRoom?roomId="+idRoom+"",
                type : "POST",
                async : false,
                cache: false,
                success : function(data) {
                        content=data;
                        $(content).appendTo("#posts");
                }
            });
        }
            function cmdSave(){
                var mejano =  document.frmsrcsalesorder.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TABLE_ID]%>.value;
                //alert("asa"+mejano);
                if(mejano==0){
                    alert("Please Select Table Number First");
                }else{
                    document.frmsrcsalesorder.command.value="<%=Command.SAVE%>";
                    document.frmsrcsalesorder.action="mobileoutletonline.jsp";
                    document.frmsrcsalesorder.submit();
                }
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
                        <input type="hidden" name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SALES_CODE]%>" value="<%=salesCodeNew%>">
                        <input type="hidden" name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_LOCATION_ID]%>" value="<%=locationSales%>">
                        <input type="hidden" name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CASH_CASHIER_ID]%>" value="1">
                        <input type="hidden" name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TRANSACTION_STATUS]%>" value="1">
                        <input type="hidden" name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TRANS_TYPE]%>" value="0">
                        <input type="hidden" name="sales_name" value="<%=salesName%>">
                        <input type="hidden" name="warehouseSales" value="<%=warehouseSales%>">
                        <input type="hidden" name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CASH_BILL_MAIN_ID]%>" value="<%=cashBillMainId%>">
                        <input type="hidden" name="catId" value="" />
                        <input type="hidden" name="oId" value="" />
                        <input type="hidden" name="qty" value="" />
                       <div class='row'>
                            <div class='col-xs-12'>
                                <div class="col-lg-12 col-xs-12">
                                    <div class="form-group">
                                            <label class="col-sm-3 control-label">Customer Name</label>
                                            <div class="col-sm-3">
                                                <input type="text" class="form-control" name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_GUEST_NAME]%>"  value="" />
                                            </div>
                                    </div>
                                </div>
                             </div>
                       </div>
                       <div class='row'>
                            <div class='col-xs-12'>
                                <div class="col-lg-12 col-xs-12">
                                    <div class="form-group">
                                            <label class="col-sm-3 control-label">Room</label>
                                            <div class="col-sm-3">
                                                <%=ControlCombo.drawBoostrap(frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_ROOM_ID],"form-control",null,"", val_locationid, key_locationid, "onChange=\"javascript:cmdChangeLocation(this.value)\"")%>
                                            </div>
                                    </div>
                                </div>
                            </div>
                       </div>
                                                <%--asa --%>
                       <div class='row'>
                            <div class='col-xs-12'>
                               <div class="col-lg-12 col-xs-12">
                                    <div class="form-group">
                                            <label class="col-sm-3 control-label">Table</label>
                                            <div class="col-sm-5" id="posts">
                                            </div>
                                    </div>
                               </div>
                            </div>
                       </div>
                       <div class='row'>
                            <div class='col-xs-12'>
                                <div class="col-lg-12 col-xs-12">
                                    <button class="btn btn-primary" onclick="javascript:cmdSave()" type="button" >Add Order</button>
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


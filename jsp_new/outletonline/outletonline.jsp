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

<%!ShopCart shoppingCart = new ShopCart();%>


<%
    
    int iCommand = FRMQueryString.requestCommand(request);
    String salesCode = FRMQueryString.requestString(request, "FRM_FIELD_SALES_CODE");
    long cashBillMainId = FRMQueryString.requestLong(request, FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CASH_BILL_MAIN_ID]);
    String billNumber = FRMQueryString.requestString(request, FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_BILL_NUMBER]);
    long catId = FRMQueryString.requestLong(request, "catId");//request.getParameter("catId");
    String oId= (String) request.getParameter("oId");
    String qty= request.getParameter("qty");
    
    CtrlBillMain ctrlBillMain = new CtrlBillMain(request);
    ControlLine ctrLine = new ControlLine();
    FrmBillMain frmBillMain = ctrlBillMain.getForm();
    
    //cek session listchart
    if(!session.equals(null)){
    }else{
     shoppingCart = (ShopCart) session.getValue("cart");
    }
    if(iCommand!=Command.SAVEALL && oId!=null&&qty!=null && cashBillMainId!=0 && !oId.equals("")&& !qty.equals("")){
        shoppingCart.addToCart(oId, Integer.parseInt(qty));
    }
    session.putValue("cart", shoppingCart);
    
    //cek session bill main
    int iErrCode = FRMMessage.NONE;
    if(cashBillMainId==0){
        iErrCode = ctrlBillMain.action(iCommand, cashBillMainId, 0);
    }
        
    BillMain billMain = ctrlBillMain.getBillMain();
    long oidMember=0;
    Vector listBillMain = new Vector(1, 1);
    Vector val_locationid = new Vector(1,1);
    Vector key_locationid = new Vector(1,1);
    BillMain billMainPrev = new BillMain();
    Date now = new Date();
    if(cashBillMainId==0){
        cashBillMainId=billMain.getOID();
        billNumber=billMain.getInvoiceNumber();
        try{
            billMain = PstBillMain.fetchExc(cashBillMainId);
        }catch(Exception ex){
        }
    }else{
        try{
            billMain = PstBillMain.fetchExc(cashBillMainId);
        }catch(Exception ex){
        }
    }
    
    //disini proses untuk menyimpan
    Vector fetchData = new Vector(1,1);
    double priceQty=0.0;
    //shoppingCart = (ShopCart) session.getValue("cart");
    if(shoppingCart!=null){
        fetchData = shoppingCart.getCart();
        priceQty = shoppingCart.getTotal();
    }else{
        session.putValue("cart", shoppingCart);
    }
    
    int ContentNumber;
    ContentNumber = shoppingCart.getCartContentNumber();
    priceQty = shoppingCart.getTotal();
    
    //jika billMain!=null dan size billmain>0 maka masukan session billnya
    if(iCommand==Command.SAVEALL && cashBillMainId!=0){
        int sucsess = ctrlBillMain.actionSaveChart(0, fetchData, cashBillMainId);
        if(sucsess==0){
            iCommand=Command.NONE;
            cashBillMainId=0;
            priceQty=0.0;
            shoppingCart.destroyCart();
            fetchData=new Vector();
            session.putValue("cart", shoppingCart);
        }
    }
    
    if(iCommand==Command.NONE){
        //buatkan bill no
        billMainPrev.setLocationId(locationSales);
        billMainPrev.setTransactionStatus(1);
        billMainPrev.setTransctionType(0);
        billMainPrev.setBillDate(now);
        billMainPrev.setSalesCode(salesName);
        billMainPrev.setInvoiceCounter(SessBilling.getIntCode(billMainPrev, billMainPrev.getBillDate(), 0, 0, true));
        billNumber=SessBilling.getCodeOrderMaterial(billMainPrev);
        
        //combo box ruangan
        String whereRoom = PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+"='"+locationSales+"'";
        Vector vt_loc = PstRoom.list(0,0,whereRoom, PstLocation.fieldNames[PstLocation.FLD_CODE]);

        val_locationid.add("0");
        key_locationid.add("All Room");
        for(int d=0;d<vt_loc.size();d++){
                Room room = (Room)vt_loc.get(d);
                val_locationid.add(""+room.getOID()+"");
                key_locationid.add(room.getName());
        }
    }
    
    
    
    Vector fetchDataTemp = new Vector(1,1);
    if(shoppingCart!=null && iCommand==Command.UPDATE){
        shoppingCart = (ShopCart) session.getValue("cart");
            fetchDataTemp = shoppingCart.getCart();
              for (int i = 0; i < fetchDataTemp.size(); i++) {

              Vector vt = (Vector) fetchDataTemp.get(i);
              Material material = (Material) vt.get(0);
              PriceTypeMapping priceTypeMapping = (PriceTypeMapping) vt.get(1);
              int amount = (Integer) vt.get(2);

              String qtyTemp = (String) request.getParameter("qtyEditValue"+material.getMaterialId());
              if(qtyTemp!=null)
                       {
                  shoppingCart.setAmount(material.getMaterialId(), Integer.parseInt(qtyTemp));
                        } 
               }

            if(request.getParameter("delete")!=null)
            {
            String materialId= request.getParameter("oId");
            if(materialId!=null)
                   {

                shoppingCart = (ShopCart) session.getValue("cart");
                shoppingCart.removeFromCart(materialId);
                    }
            }
    }
        
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
                document.frmsrcsalesorder.action="mobileoutlet.jsp";
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
            function cmdAddChart(valCat,valMaterialId,valQty){
                document.frmsrcsalesorder.catId.value=valCat;
                document.frmsrcsalesorder.oId.value=valMaterialId;
                document.frmsrcsalesorder.qty.value=valQty;
                document.frmsrcsalesorder.action="outletonline.jsp";
                document.frmsrcsalesorder.submit();
            }
            
            function listTransaction(){
                document.frmsrcsalesorder.command.value="<%=Command.LIST%>";
                document.frmsrcsalesorder.action="listtransaction.jsp";
                document.frmsrcsalesorder.submit();
            }
            
            function cmdOk(){
                document.frmsrcsalesorder.command.value="<%=Command.UPDATE%>";
                document.frmsrcsalesorder.action="outletonline.jsp";
                document.frmsrcsalesorder.submit();
            }
            
            function cmdSearcCat(value){
                document.frmsrcsalesorder.command.value="<%=Command.LIST%>";
                document.frmsrcsalesorder.catId.value=value;
                document.frmsrcsalesorder.action="outletonline.jsp";
                document.frmsrcsalesorder.submit();
            }
            function addMore(){
                document.frmsrcsalesorder.command.value="<%=Command.BACK%>";
                document.frmsrcsalesorder.action="mobileorder.jsp";
                document.frmsrcsalesorder.submit();
            }
        </script>
    </head>
    <body class="skin-blue">
        <!-- header logo: style can be found in header.less -->
        <header class="header">
            <a href="#" class="logo">
                <!-- Add the class icon to your logo image or logo icon to add the margining -->
                Sales Outlet
            </a>
            <!-- Header Navbar: style can be found in header.less -->
            <nav class="navbar navbar-static-top" role="navigation">
                <!-- Sidebar toggle button-->
                <a href="#" class="navbar-btn sidebar-toggle" data-toggle="offcanvas" role="button">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </a>
                <div class="navbar-right">
                         <!-- User Account: style can be found in dropdown.less -->
                         <ul class="nav navbar-nav">
                            <li>
                                <a href="<%=approot%>/logout.jsp" class="dropdown-toggle" data-toggle="dropdown">
                                    <i class="glyphicon glyphicon-user"></i>
                                    <span>Log Out</span>
                                </a>
                            </li>
                       </ul>
                </div>
            </nav>
        </header>
        <form id="defaultForm" name="frmsrcsalesorder" method="" class="form-horizontal">
            <div class="wrapper row-offcanvas row-offcanvas-left">
                <aside class="right-side">
                    <section class="content-header">
                        <h1>
                            Transaction Form <%=billMain.getInvoiceNumber()%>
                        </h1>
                        <ol class="breadcrumb">
                            <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
                            <li class="active">Dashboard</li>
                        </ol>
                    </section>
                    <!-- Main content -->
                    <section class="content">
                        <!-- Main row -->
                        <div class="row">
                            <!-- Left col -->
                              <section class="col-lg-9 connectedSortable">
                                <div class="box box-danger" id="loading-example">
                                     <div class="col-xs-12 ">
                                                <input type="hidden" name="command" value="<%=iCommand%>">
                                                <input type="hidden" name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SALES_CODE]%>" value="<%=salesCode%>">
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
                                                <div class="top-right">
                                            <span class="glyphicon glyphicon-shopping-cart"></span> Current Order 
                                        </div>
                                        <div>
                                            <div class="row">
                                                <table class="table table-bordered">
                                                        <thead>
                                                            <tr>
                                                                <th>Nama</th>
                                                                <th>Harga</th>
                                                                <th>Qty</th>
                                                            </tr>
                                                        </thead>
                                                        <tbody>
                                                        <%
                                                           for (int i = 0; i < fetchData.size(); i++) {

                                                                  Vector vt = (Vector) fetchData.get(i);
                                                                  Material material = (Material) vt.get(0);
                                                                  PriceTypeMapping priceTypeMapping = (PriceTypeMapping) vt.get(1);
                                                                  int amount = (Integer) vt.get(2);
                                                              %>
                                                                <tr>
                                                                    <td><%=material.getName()%></td>
                                                                    <td><%=priceTypeMapping.getPrice() %></td>
                                                                    <td>
                                                                        <span id="qtyValue<%=material.getMaterialId()%>" style="display:inline"><%=amount%></span>
                                                                        <input type="number" name="qtyEditValue<%=material.getMaterialId()%>" id="qtyEditValue<%=material.getMaterialId()%>" min="1" max="999" value="<%=amount%>" style="display:none" maxlength="3" >
                                                                    </td>
                                                                </tr>
                                                                 <%
                                                           }
                                                          %>
                                                        </tbody>
                                                        <tfoot>
                                                            <tr>
                                                                <th>TOTAL HARGA</th>
                                                                <th><%=priceQty%></th>
                                                                <th> 
                                                                    <input type="button" id="change" value="change" onclick="
                                                                      <% for (int i = 0; i < fetchData.size(); i++) {
                                                                              Vector vt = (Vector) fetchData.get(i);
                                                                              Material material = (Material) vt.get(0);
                                                                                %>
                                                                              toggleVisibility('qtyEditValue<%=material.getMaterialId()%>');
                                                                              toggleVisibility('qtyValue<%=material.getMaterialId()%>');
                                                                                      <%
                                                                                      }
                                                                                      %>
                                                                              toggleVisibility('change');
                                                                              toggleVisibility('ok');" 
                                                                              style="display:inline"/>
                                                                    <input type="submit" name="ok" id="ok" value="OK"  style="display:none" onclick="javascript:cmdOk()" >
                                                                </th>
                                                            </tr>
                                                        </tfoot>
                                                </table>
                                            </div>
                                            <%if(cashBillMainId!=0){%>
                                                <div class="user-panel">
                                                    <div class="row">
                                                             <button class="btn btn-default btn-lg" onclick="javascript:saveCheckOut()">Check Out</button>
                                                             <button class="btn btn-default btn-lg" onclick="javascript:addMore()">Add More</button>
                                                    </div>
                                                </div>
                                            <%}%>
                                        </div>
                                        </div>
                                    </div>
                                </section>  
                            </div>
                      </section><!-- /.Left col -->
                </aside><!-- /.right-side -->
                <aside class="right-side">
                    
                </aside>
            </div><!-- ./wrapper -->
        </form>
        
        <!-- add new calendar event modal -->
        <%--
        <!-- jQuery UI 1.10.3 -->
        <script src="../styles/bootstrap3.1/js/jquery-ui-1.10.3.min.js" type="text/javascript"></script>
        <!-- Bootstrap -->
        <script src="../styles/bootstrap3.1/js/bootstrap.min.js" type="text/javascript"></script>
        <!-- Morris.js charts -->
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
        <script src="../styles/bootstrap3.1/js/plugins/iCheck/icheck.min.js" type="text/javascript"></script>--%>

        <!-- AdminLTE App -->
        <script src="../styles/bootstrap3.1/js/AdminLTE/app.js" type="text/javascript"></script>
        <!-- DATA TABES SCRIPT -->
        <script src="../styles/bootstrap3.1/js/plugins/datatables/jquery.dataTables.js" type="text/javascript"></script>
        <script src="../styles/bootstrap3.1/js/plugins/datatables/dataTables.bootstrap.js" type="text/javascript"></script>
        
        <!-- AdminLTE dashboard demo (This is only for demo purposes)
        <script src="../styles/bootstrap3.1/js/AdminLTE/dashboard.js" type="text/javascript"></script>      -->   
    </body>
</html>

<script type="text/javascript">
    function toggleVisibility(id) {
       var e = document.getElementById(id);
       if(e.style.display == 'inline')
          e.style.display = 'none';
       else
          e.style.display = 'inline';
    }
    function toggleVisibility2(id) {
       var e = document.getElementById(id);
       if(e.style.display == 'inline')
          e.style.display = 'none';
       else
          e.style.display = 'inline';
    }
     function buttonValueOk() {
       var e = document.getElementById(buttonValue);
       e.value=1
    }
    function buttonValueDelete() {
       var e = document.getElementById(buttonValue);
       e.value=2
    }
</script>
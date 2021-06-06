<%-- 
    Document   : listcatalog
    Created on : May 6, 2014, 9:58:22 AM
    Author     : dimata005
--%>

<%@page import="com.dimata.posbo.entity.shoppingchart.ShopCart"%>
<%-- 
    Document   : outletonline
    Created on : May 2, 2014, 4:31:21 PM
    Author     : dimata005
--%>
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

<%!    //final static int CMD_NONE =0;
    final static int CMD_APPROVAL = 1;

%>
<%
        int appObjCodeSalesOrder = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_SALES_ORDER, AppObjInfo.OBJ_SALES_ORDER);
        int appObjCodeSalesRetur = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_SALES_ORDER, AppObjInfo.OBJ_SALES_RETUR);
        int appObjCodeSalesInvoice = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_SALES_ORDER, AppObjInfo.OBJ_SALES_INVOICE);        

%>
<!--%@ include file = "../main/checkuser.jsp" %-->
<%
        boolean privApprovalSalesOrder = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeSalesOrder, AppObjInfo.COMMAND_VIEW));
        boolean privApprovalSalesRetur = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeSalesRetur, AppObjInfo.COMMAND_VIEW));
        boolean privApprovalSalesInvoice = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeSalesInvoice, AppObjInfo.COMMAND_VIEW));
%>

<%!ShopCart shoppingCart = new ShopCart();%>

<%
    
    int iCommand = FRMQueryString.requestCommand(request);
    int iCommandDetail = FRMQueryString.requestInt(request, "commandDetail");
    long catId = FRMQueryString.requestLong(request, "catId");//request.getParameter("catId");
    String oId= (String) request.getParameter("oId");
    String qty= request.getParameter("qty");
    String salesCode = FRMQueryString.requestString(request, "FRM_FIELD_SALES_CODE");
    
    if(!session.equals(null)){
        //salesCode=shoppingCart.getSalesCode();
    }else{
     shoppingCart = (ShopCart) session.getValue("cart");
    }
    if(oId!=null&&qty!=null){
        shoppingCart.addToCart(oId, Integer.parseInt(qty));
    }
    session.putValue("cart", shoppingCart);
    
    /* VARIABLE DECLARATION */
    int recordToGet =9;
    String order = " " + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID];
    Vector listData = new Vector(1,1);
    Vector listCat = new Vector(1,1);
    String priceType = PstSystemProperty.getValueByName("PRICE_TYPE_SHOPPING_CHART"); 
    String standartRate = PstSystemProperty.getValueByName("ID_STANDART_RATE");
    String whereCatalog="";
    if(catId!=0)
    {
    whereCatalog = "AND p.CATEGORY_ID = " + catId;
    }
    
    listData = PstMaterial.listShopingCart(0, recordToGet,whereCatalog, order,standartRate,priceType); 
    listCat = PstCategory.list(0, 10,"", PstCategory.fieldNames[PstCategory.FLD_NAME]);
    
    
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
    

%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>AdminLTE | Dashboard</title>
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
            function showData(value){ 
                $("#customer").html("");
                       $.ajax({
                             url : "<%=approot%>/servlet/com.dimata.posbo.ajax.CheckCustomer?"+value,
                            type : "POST",
                            async : false,
                            cache: false,
                            success: function(msg){
                                //alert(msg);
                                content=msg;
                                $(content).appendTo("#customer");
                            }
                        });
            }
            
            function cmdAddChart(valCat,valMaterialId,valQty){
                document.formchart.catId.value=valCat;
                document.formchart.oId.value=valMaterialId;
                document.formchart.qty.value=valQty;
                document.formchart.action="listcatalog.jsp";
                document.formchart.submit();
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
                        <li class="dropdown user user-menu">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                <i class="glyphicon glyphicon-user"></i>
                                <span>Jane Doe <i class="caret"></i></span>
                            </a>
                            <ul class="dropdown-menu">
                                <!-- User image -->
                                <li class="user-header bg-light-blue">
                                    <img src="img/avatar3.png" class="img-circle" alt="User Image" />
                                    <p>
                                        Jane Doe - Web Developer
                                        <small>Member since Nov. 2012</small>
                                    </p>
                                </li>
                                <!-- Menu Body -->
                                <li class="user-body">
                                    <div class="col-xs-4 text-center">
                                        <a href="#">Followers</a>
                                    </div>
                                    <div class="col-xs-4 text-center">
                                        <a href="#">Sales</a>
                                    </div>
                                    <div class="col-xs-4 text-center">
                                        <a href="#">Friends</a>
                                    </div>
                                </li>
                                <!-- Menu Footer-->
                                <li class="user-footer">
                                    <div class="pull-left">
                                        <a href="#" class="btn btn-default btn-flat">Profile</a>
                                    </div>
                                    <div class="pull-right">
                                        <a href="#" class="btn btn-default btn-flat">Sign out</a>
                                    </div>
                                </li>
                            </ul>
                        </li>
                </div>
            </nav>
        </header>
            <div class="wrapper row-offcanvas row-offcanvas-left">
                <!-- Left side column. contains the logo and sidebar -->
                <aside class="left-side sidebar-offcanvas">
                    <!-- sidebar: style can be found in sidebar.less -->
                    <section class="sidebar">
                        <!-- Sidebar user panel -->
                        <div class="user-panel">
                            <div class="pull-left image">
                                <img src="img/avatar3.png" class="img-circle" alt="User Image" />
                            </div>
                            <div class="pull-left info">
                                <p>Hello, <%=salesName%></p>
                                <a href="#"><i class="fa fa-circle text-success"></i> Online</a>
                            </div>
                        </div>
                        <!-- sidebar menu: : style can be found in sidebar.less -->
                        <ul class="sidebar-menu">
                            <li class="active">
                                <a href="<%=approot%>/outletonline/outletonline.jsp?FRM_FIELD_SALES_CODE=<%=salesCode%>">
                                    <i class="fa fa-dashboard"></i> <span>Transaction</span>
                                </a>
                            </li>
                            <li>
                                <a href="<%=approot%>/logout.jsp">
                                    <i class="fa fa-th"></i> <span>Log Out</span> 
                                </a>
                            </li>
                        </ul>
                        <div class="user-panel">
                            <div class="row">
                                <table id="example2" class="table table-bordered table-hover">
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
                                                    <td><span id="qtyValue<%=material.getMaterialId()%>" style="display:inline"><%=amount%></span><input type="number" name="qtyEditValue<%=material.getMaterialId()%>" id="qtyEditValue<%=material.getMaterialId()%>" min="1" max="999" value="<%=amount%>" style="display:none" maxlength="3" ></td>
                                                </tr>
                                                 <%
                                           }
                                          %>
                                        </tbody>
                                </table>
                            </div>
                        </div>
                    </section>
                    <!-- /.sidebar -->
                </aside>

                <!-- Right side column. Contains the navbar and content of the page -->
                <aside class="right-side">
                    <!-- Content Header (Page header) -->
                    <section class="content-header">
                        <h1>
                            List Catalog
                        </h1>
                        <ol class="breadcrumb">
                            <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
                            <li class="active">catalog</li>
                        </ol>
                    </section>
                    
                    <!-- Main content -->
                    <form name="formchart" action="">
                        <input type="hidden" name="catId" value="" />
                        <input type="hidden" name="oId" value="" />
                        <input type="hidden" name="qty" value="" />
                        <section class="content">
                            <div class="row">
                                
                            </div>
                            <%
                                   for (int i = 0; i < listCat.size(); i++) {
                                     Category cat = (Category) listCat.get(i);
                                %>    
                                <div class="col-lg-3 col-xs-8">
                                <!-- small box -->
                                    <div class="small-box bg-yellow">
                                        <a href="listcatalog.jsp?catId=<%=cat.getOID()%>&FRM_FIELD_SALES_CODE=<%=salesCode%>" class="small-box-footer"><%=cat.getName()%><i class="fa fa-arrow-circle-right"></i>
                                        </a>
                                    </div>
                                </div>     
                             <%
                               }
                              %>
                            <div class="row">
                                 <div class="col-lg-3 col-xs-12">&nbsp;</div>    
                            </div>
                                <%
                                    for (int i = 0; i < listData.size(); i++) {
                                    Vector vt = (Vector) listData.get(i);
                                    Material material = (Material) vt.get(0);
                                    PriceTypeMapping priceTypeMapping = (PriceTypeMapping) vt.get(1);
                                %>    
                                <div class="col-lg-3 col-xs-6">
                                <!-- small box -->
                                    <div class="small-box bg-aqua">
                                        <div class="inner">
                                            <h4>
                                                Rp.<%=priceTypeMapping.getPrice() %>
                                            </h4>
                                            <p>
                                                <%=material.getName()%>
                                            </p>
                                        </div>
                                        <div class="icon">
                                            <i class="ion ion-bag"></i>
                                        </div>
                                        <a href="javascript:cmdAddChart('<%=catId%>','<%=material.getMaterialId()%>','1')" class="small-box-footer">Add To Cart<i class="fa fa-arrow-circle-right"></i>
                                        </a>
                                    </div>
                                </div>     
                             <%
                               }
                              %>
                            </div>      
                            <!-- Main row -->
                          </section><!-- /.Left col -->
                   </form>
                </aside><!-- /.right-side -->
            </div><!-- ./wrapper -->
        
        <!-- add new calendar event modal -->

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
        
        <!-- AdminLTE dashboard demo (This is only for demo purposes)
        <script src="../styles/bootstrap3.1/js/AdminLTE/dashboard.js" type="text/javascript"></script>      -->   
    </body>
</html>
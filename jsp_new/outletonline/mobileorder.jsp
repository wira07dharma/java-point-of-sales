<%-- 
    Document   : mobileorder
    Created on : Nov 19, 2014, 2:28:33 PM
    Author     : dimata005
--%>

<%@page import="com.dimata.posbo.entity.masterdata.Category"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstCategory"%>
<%@page import="com.dimata.posbo.entity.shoppingchart.ShopCart"%>
<%@page import="com.dimata.pos.session.billing.SessBilling"%>
<%@page import="com.dimata.posbo.entity.masterdata.PriceTypeMapping"%>
<%@page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterial"%>
<%@page import="com.dimata.pos.entity.billing.PstBillMain"%>
<%-- 
    Document   : mobileoutletonline
    Created on : Nov 19, 2014, 1:32:37 PM
    Author     : dimata005
--%>
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
<%!ShopCart shoppingCart = new ShopCart();%>


<%!
  //table       
%>
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
    BillMain billMainPrev = new BillMain();
    Date now = new Date();
    
    try{
        billMain = PstBillMain.fetchExc(cashBillMainId);
    }catch(Exception ex){
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
                alert("asa"+mejano);
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
            
             function cmdAddItem(name,hrg){
                document.frmorder.name.value=name;
                document.frmorder.harga.value=hrg;
             }
             
              function cmdAddChart(valCat,valMaterialId,valQty){
                document.frmsrcsalesorder.catId.value=valCat;
                document.frmsrcsalesorder.oId.value=valMaterialId;
                document.frmsrcsalesorder.qty.value=valQty;
                document.frmsrcsalesorder.action="outletonline.jsp";
                document.frmsrcsalesorder.submit();
            }
        </script>
    </head>
    <body class="skin-blue">
        <%@ include file = "../header_mobile.jsp" %> 
        <div class="wrapper row-offcanvas row-offcanvas-left">

            <!-- Right side column. Contains the navbar and content of the page -->
            <aside class="right-side">
                 <section class="content">
                    <form name="frmsrcsalesorder" method ="post" action="" role="form">
                        <input type="hidden" name="command" value="<%=iCommand%>">
                        <input type="hidden" name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CASH_BILL_MAIN_ID]%>" value="<%=cashBillMainId%>">
                        <input type="hidden" name="catId" value="" />
                        <input type="hidden" name="oId" value="" />
                        <input type="hidden" name="qty" value="" />
                        <div class='row'>
                            <div class='col-xs-12'>
                                <div class="col-lg-12 col-xs-12">
                                    <%=billMain.getInvoiceNo()%>
                                     <div class="input-group">
                                        <input id="valsearch" class="valsearch form-control" type="text" placeholder="Search items"></input>
                                    </div>
                                </div>
                             </div>
                            <div class='col-xs-12'>
                                <div class="col-lg-12 col-xs-12">
                                    <div class="input-group">
                                        <input id="valsearch" class="valsearch form-control" type="text" placeholder="Search items"></input>
                                        <span class="input-group-btn">
                                            <button id="mnsearch" class="search btn btn-default" type="button">></button>
                                        </span>
                                    </div>
                                    <div class="row">&nbsp;
                                    </div>
                                    <%
                                           Vector listCat = new Vector(1,1);
                                           String whereCat = " STATUS_CATEGORY='1'";
                                           listCat = PstCategory.list(0, 10,whereCat, PstCategory.fieldNames[PstCategory.FLD_NAME]);
                                           String order = " " + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID];
                                           Vector listData = new Vector(1,1);
                                           String priceType = PstSystemProperty.getValueByName("PRICE_TYPE_SHOPPING_CHART"); 
                                           String standartRate = PstSystemProperty.getValueByName("ID_STANDART_RATE");
                                           String whereCatalog="";
                                           if(catId!=0)
                                           {
                                            whereCatalog = "AND p.CATEGORY_ID = " + catId;
                                           }

                                           listData = PstMaterial.listShopingCart(0, 0,whereCatalog, order,standartRate,priceType); 
                                           for (int i = 0; i < listCat.size(); i++) {
                                           Category cat = (Category) listCat.get(i);
                                        %>    
                                        <div class="col-lg-3 col-xs-8">
                                        <!-- small box -->
                                            <div class="small-box bg-yellow">
                                                <a href="javascript:cmdSearcCat('<%=cat.getOID()%>')" class="small-box-footer"><%=cat.getName()%><i class="fa fa-arrow-circle-right"></i>
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
                                        <div class="col-lg-12 col-xs-12">
                                        <!-- small box -->
                                            <div class="box-body">
                                            <a class="btn btn-block btn-social btn-twitter" href="javascript:cmdAddChart('<%=catId%>','<%=material.getMaterialId()%>','1')">
                                               <%=material.getName()%>
                                            </a>
                                            </div>
                                        </div>     
                                     <%
                                       }
                                      %>
                                </div>
                             </div>
                       </div>
                    </form>                   
                 </section>
            </aside>
        </div>
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



<%-- 
    Document   : order
    Created on : Dec 22, 2014, 11:03:02 AM
    Author     : dimata005
--%>

<%@page import="com.dimata.qdep.form.FRMHandler"%>
<%@page import="com.dimata.posbo.entity.masterdata.TableRoom"%>
<%@page import="com.dimata.posbo.entity.masterdata.Room"%>
<%@page import="com.dimata.posbo.entity.masterdata.Category"%>
<%@page import="java.text.Format"%>
<%@page import="com.dimata.pos.entity.billing.PstBillMain"%>
<%@page import="com.dimata.pos.entity.billing.BillMain"%>
<%@page import="com.dimata.qdep.form.FRMMessage"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.gui.jsp.ControlLine"%>
<%@page import="com.dimata.pos.form.billing.CtrlBillMain"%>
<%@page import="com.dimata.pos.form.billing.FrmBillMain"%>
<%@page import="com.dimata.posbo.entity.masterdata.PriceTypeMapping"%>
<%@page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstCategory"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterial"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.posbo.entity.shoppingchart.ShopCart"%>
<%@page import="com.dimata.posbo.entity.admin.AppObjInfo"%>
<%-- 
    Document   : salesmobile
    Created on : Dec 22, 2014, 10:18:34 AM
    Author     : dimata005
--%>

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
    String salesCode = FRMQueryString.requestString(request, "FRM_FIELD_SALES_CODE");
    long cashBillMainId = FRMQueryString.requestLong(request, FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CASH_BILL_MAIN_ID]);
    String billNumber = FRMQueryString.requestString(request, FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_BILL_NUMBER]);
    long catId = FRMQueryString.requestLong(request, "catId");//request.getParameter("catId");
    String oId = (String) request.getParameter("oId");
    String qty = request.getParameter("qty");
    String typeFood = FRMQueryString.requestString(request, "typeFood");

    CtrlBillMain ctrlBillMain = new CtrlBillMain(request);
    ControlLine ctrLine = new ControlLine();
    FrmBillMain frmBillMain = ctrlBillMain.getForm();

    //cek session listchart
    if (!session.equals(null)) {
    } else {
        shoppingCart = (ShopCart) session.getValue("cart");
    }
    if (iCommand != Command.SAVEALL && oId != null && qty != null && cashBillMainId != 0 && !oId.equals("") && !qty.equals("")) {
        shoppingCart.addToCart(oId, Integer.parseInt(qty));
    }
    session.putValue("cart", shoppingCart);

    //cek session bill main
    int iErrCode = FRMMessage.NONE;
    if (cashBillMainId == 0) {
        iErrCode = ctrlBillMain.action(iCommand, cashBillMainId, 0);
    }

    BillMain billMain = ctrlBillMain.getBillMain();
    long oidMember = 0;
    Vector listBillMain = new Vector(1, 1);
    BillMain billMainPrev = new BillMain();
    Date now = new Date();

    try {
        billMain = PstBillMain.fetchExc(cashBillMainId);
    } catch (Exception ex) {
    }

    //disini proses untuk menyimpan
    Vector fetchData = new Vector(1, 1);
    double priceQty = 0.0;
    //shoppingCart = (ShopCart) session.getValue("cart");
    if (shoppingCart != null) {
        fetchData = shoppingCart.getCart();
        priceQty = shoppingCart.getTotal();
    } else {
        session.putValue("cart", shoppingCart);
    }

    int ContentNumber;
    ContentNumber = shoppingCart.getCartContentNumber();
    priceQty = shoppingCart.getTotal();

    //jika billMain!=null dan size billmain>0 maka masukan session billnya
    if (iCommand == Command.SAVEALL && cashBillMainId != 0) {
        int sucsess = ctrlBillMain.actionSaveChart(0, fetchData, cashBillMainId);
        if (sucsess == 0) {
            iCommand = Command.NONE;
            cashBillMainId = 0;
            priceQty = 0.0;
            shoppingCart.destroyCart();
            fetchData = new Vector();
            session.putValue("cart", shoppingCart);
        }
    }

    Vector fetchDataTemp = new Vector(1, 1);
    if (shoppingCart != null && iCommand == Command.UPDATE) {
        shoppingCart = (ShopCart) session.getValue("cart");
        fetchDataTemp = shoppingCart.getCart();
        for (int i = 0; i < fetchDataTemp.size(); i++) {

            Vector vt = (Vector) fetchDataTemp.get(i);
            Material material = (Material) vt.get(0);
            PriceTypeMapping priceTypeMapping = (PriceTypeMapping) vt.get(1);
            int amount = (Integer) vt.get(2);

            String qtyTemp = (String) request.getParameter("qtyEditValue" + material.getMaterialId());
            if (qtyTemp != null) {
                shoppingCart.setAmount(material.getMaterialId(), Integer.parseInt(qtyTemp));
            }
        }

        if (request.getParameter("delete") != null) {
            String materialId = request.getParameter("oId");
            if (materialId != null) {

                shoppingCart = (ShopCart) session.getValue("cart");
                shoppingCart.removeFromCart(materialId);
            }
        }
    }


    //list catalog
    Vector listData = new Vector(1, 1);
    String priceType = PstSystemProperty.getValueByName("PRICE_TYPE_SHOPPING_CHART");
    String standartRate = PstSystemProperty.getValueByName("ID_STANDART_RATE");
    String oidSpesialRequestFood=PstSystemProperty.getValueByName("SPESIAL_REQUEST_FOOD");
    String oidSpesialRequestBeverage=PstSystemProperty.getValueByName("SPESIAL_REQUEST_BEVERAGE");
    String whereCatalog = "";

    if (typeFood.equals("0")) {
        //food 
        whereCatalog = "p.CATEGORY_ID='504404582510028756'";
    } else {
        //beverage
        whereCatalog = "p.CATEGORY_ID='504404582510052277'";
    }

    String order = " " + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID];
    listData = PstMaterial.listShopingCart(0, 0, whereCatalog, order, standartRate, priceType);


    //list tamu
    String whereOpenBill = " cbm.CASH_BILL_MAIN_ID='" + cashBillMainId + "'";
    Vector listTransaction = PstBillMain.listOpenBill(0, 0, whereOpenBill, "");
    Room room = new Room();
    TableRoom table = new TableRoom();
    for (int i = 0; i < listTransaction.size(); i++) {
        Vector dataBill = (Vector) listTransaction.get(0);
        billMain = (BillMain) dataBill.get(0);
        room = (Room) dataBill.get(1);
        table = (TableRoom) dataBill.get(2);
    }
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Taking Order - Queens Bali</title>
        <%--
        <link href="../styles/takingorder/css/bootstrap.css" rel="stylesheet" type="text/css">
        <link href="../styles/takingorder/css/bootstrap_002.css" rel="stylesheet" type="text/css">
        <!-- jQuery -->
        <script src="../styles/takingorder/js/jquery.js"></script>
        <!-- Tablesorter: required -->
        <link href="../styles/takingorder/css/theme.css" rel="stylesheet">
        <script src="../styles/takingorder/js/jquery_002.js"></script>
        <script src="../styles/takingorder/js/jquery_003.js"></script>
        <!-- Tablesorter: pager -->
        <link rel="stylesheet" href="../styles/takingorder/js/jquery.css">
        <script src="../styles/takingorder/js/widget-pager.js"></script>
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

        <script language="JavaScript">
            function cmdBack(){
                document.frmsrcsalesorder.command.value="<%=Command.BACK%>";
                document.frmsrcsalesorder.action="tableorder.jsp";
                document.frmsrcsalesorder.submit();
            }
     
            function cmdConfirm(){
                document.frmsrcsalesorder.qty_order_beverage.value="0";
                var valQty = document.frmsrcsalesorder.qty_order.value;
                var valCat = document.frmsrcsalesorder.catId.value;
                var valMaterialId = document.frmsrcsalesorder.oId.value
                var chooseVarian =  document.frmsrcsalesorder.category_varian.value

                document.frmsrcsalesorder.catId.value=valCat;
                document.frmsrcsalesorder.oId.value=valMaterialId;
                document.frmsrcsalesorder.qty.value=valQty;
                document.frmsrcsalesorder.typeFood.value="0";
                document.frmsrcsalesorder.action="listorder.jsp";
                //alert("valCat : "+valCat+" valMaterialId : "+valMaterialId+" valQty : "+valQty+" varian : "+chooseVarian);
                //document.frmsrcsalesorder.action="listorder.jsp";
                //document.frmsrcsalesorder.submit();
            }

            function cmdConfirmBeverage(){
                document.frmsrcsalesorder.qty_order.value="0";
                var valQty = document.frmsrcsalesorder.qty_order_beverage.value;
                var valCat = document.frmsrcsalesorder.catId.value;
                var valMaterialId = document.frmsrcsalesorder.oId.value
                var chooseVarian =  document.frmsrcsalesorder.category_varian_beverage.value

                document.frmsrcsalesorder.catId.value=valCat;
                document.frmsrcsalesorder.oId.value=valMaterialId;
                document.frmsrcsalesorder.qty.value=valQty;
                document.frmsrcsalesorder.typeFood.value="1";
                document.frmsrcsalesorder.action="listorder.jsp";
                //alert("3");
                //document.frmsrcsalesorder.action="listorder.jsp";
                //alert("4");
            }
            
            
            function cmdAddChart(valCat,valMaterialId,valQty,name){
                document.frmsrcsalesorder.catId.value=valCat;
                document.frmsrcsalesorder.oId.value=valMaterialId;
                document.frmsrcsalesorder.qty.value=valQty;
                document.frmsrcsalesorder.name_beverage.value="0";
                document.frmsrcsalesorder.name_food.value=name;
                $(document).ready(function(){
                    $('#myModal').modal('show');
                });
            }
          
            function cmdAddChartBeverage(valCat,valMaterialId,valQty,name){
                document.frmsrcsalesorder.catId.value=valCat;
                document.frmsrcsalesorder.oId.value=valMaterialId;
                document.frmsrcsalesorder.qty.value=valQty;
                document.frmsrcsalesorder.name_food.value="0";
                document.frmsrcsalesorder.name_beverage.value=name;
                $(document).ready(function(){
                    $('#myModalBeverage').modal('show');
                });
            }
            
            function cmdSpesialRequestFood(){
                //alert("Spesial Request Food");
                document.frmsrcsalesorder.catId.value="504404582510028756";
                document.frmsrcsalesorder.oId.value="<%=oidSpesialRequestFood%>";
                document.frmsrcsalesorder.qty.value="1";
                document.frmsrcsalesorder.name_food.value="Spesial Request Food";
                document.frmsrcsalesorder.name_beverage.value="0";
                $(document).ready(function(){
                    $('#myModal').modal('show');
                });
            }
            
            function cmdSpesialRequestBeverage(){
                //alert("Spesial Request Beverage");
                document.frmsrcsalesorder.catId.value="504404582510052277";
                document.frmsrcsalesorder.oId.value="<%=oidSpesialRequestBeverage%>";
                document.frmsrcsalesorder.qty.value="1";
                document.frmsrcsalesorder.name_beverage.value="Spesial Request Beverage";
                document.frmsrcsalesorder.name_food.value="0";
                $(document).ready(function(){
                    $('#myModalBeverage').modal('show');
                });
            }
        </script>
    </head>
    <body>
        <div style="background:#f0f0f0; margin-bottom:10px; padding:5px;">
            <table border="0" cellpadding="0" cellspacing="0" width="100%">
                <tbody><tr>
                        <td style="text-align:left;">
                            <i><strong> </strong> <span id="cartorder"></span></i>
                        </td>
                        <td style="text-align:right;">
                            <i></i>
                        </td>
                    </tr>
                </tbody></table>
        </div>
        <div class="container">
            <center>
                <a href="#"><img src="../styles/takingorder/img/queens-head.png" border="none" width="250"></a>
            </center>
            <div style="margin-top:20px;">
                <a href="<%=approot%>/outletonline/salesmobile.jsp">Back to Main</a>
                <hr style="margin-top:10px;">
                Name Guest :<%=billMain.getGuestName()%>
                <br>Rooom : <%=room.getName()%>
                <br>Meja :<%=table.getTableNumber()%>
                <br>

                <script id="js">
                    $(function() {
                        // hide child rows
                        $('.tablesorter-childRow td').hide();
                        var $table = $('.tablesorter-dropbox')
                        .tablesorter({
                            headerTemplate: '{content}{icon}', // dropbox theme doesn't like a space between the content & icon
                            sortList : [ [0,0] ],
                            showProcessing: true,
                            cssChildRow: "tablesorter-childRow",
                            widgets    : ["pager","zebra","filter"],
                            widgetOptions: {
                                filter_columnFilters: false,
                                filter_saveFilters : true,
                            }
                        })
                        $('.tablesorter-dropbox').delegate('.toggle', 'click' ,function(){
                            $(this).closest('tr').nextUntil('tr.tablesorter-hasChildRow').find('td').toggle();
                            return false;
                        });

                        $.tablesorter.filter.bindSearch( $table, $('.search') );
                        $('select').change(function(){
                            // modify the search input data-column value (swap "0" or "all in this demo)
                            $('.selectable').attr( 'data-column', $(this).val() );
                            // update external search inputs
                            $.tablesorter.filter.bindSearch( $table, $('.search'), false );
                        });
                    });
                </script>
                <%--<button class="btn btn-primary btn-md btn-block" type="button" onclick="">&nbsp;</button>--%>
                <form name="frmsrcsalesorder" method ="post" action="">
                    <input type="hidden" name="command" value="<%=iCommand%>">
                    <input type="hidden" name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CASH_BILL_MAIN_ID]%>" value="<%=cashBillMainId%>">
                    <input type="hidden" name="catId" value="" />
                    <input type="hidden" name="oId" value="" />
                    <input type="hidden" name="qty" value="" />
                    <input type="hidden" name="typeFood" value="<%=typeFood%>" />
                    <div class="form-group">
                        <div class="col-md-6"> 
                            <button name="submit" class="btn btn-danger btn-md btn-block" onclick='location.href="<%=approot%>/outletonline/order.jsp?FRM_FIELD_CASH_BILL_MAIN_ID=<%=cashBillMainId%>&typeFood=0"' type="button">Food</button>
                            <button name="submit" class="btn btn-danger btn-md btn-block" onclick='location.href="<%=approot%>/outletonline/order.jsp?FRM_FIELD_CASH_BILL_MAIN_ID=<%=cashBillMainId%>&typeFood=1"' type="button">Beverage</button>
                        </div>
                        <div class="col-md-1"> &nbsp;</div>
                        <div class="col-md-5"> 
                            <button name="submit" class="btn btn-success btn-md btn-block" onclick='javascript:cmdSpesialRequestFood()' type="button">Spesial Request Food</button>
                            <button name="submit" class="btn btn-success btn-md btn-block" onclick='javascript:cmdSpesialRequestBeverage()' type="button">Spesial Request Beverage</button>
                        </div>
                    </div>
                    <div style="clear:left; margin-bottom:10px;"></div>  
                    <div class="guest" style="margin:10px 0px;">
                        <input data-lastsearchtime="1416898435043" class="form-control search selectable" placeholder="Search keyword.." data-column="all" type="search">
                        <table role="grid" class="tablesorter-dropbox tablesorter hasFilters">
                            <thead>
                                <tr role="row" class="tablesorter-headerRow">
                                    <th aria-label="#: Ascending sort applied, activate to apply a descending sort" aria-sort="ascending" style="-moz-user-select: none;" unselectable="on" aria-disabled="false" role="columnheader" scope="col" tabindex="0" class="tablesorter-header tablesorter-headerAsc" data-column="0" width="15%"><div class="tablesorter-header-inner">Category<i class="tablesorter-icon"></i></div></th>
                            <th aria-label="Guest Name: No sort applied, activate to apply an ascending sort" aria-sort="none" style="-moz-user-select: none;" unselectable="on" aria-disabled="false" role="columnheader" scope="col" tabindex="0" class="tablesorter-header tablesorter-headerUnSorted" data-column="1" width="30%"><div class="tablesorter-header-inner">Nama<i class="tablesorter-icon"></i></div></th>
                            <th aria-label="Guest Name: No sort applied, activate to apply an ascending sort" aria-sort="none" style="-moz-user-select: none;" unselectable="on" aria-disabled="false" role="columnheader" scope="col" tabindex="0" class="tablesorter-header tablesorter-headerUnSorted" data-column="2" width="20%"><div class="tablesorter-header-inner">Harga<i class="tablesorter-icon"></i></div></th>
                            <th aria-label="&nbsp;: No sort applied, activate to apply an ascending sort" aria-sort="none" style="-moz-user-select: none;" unselectable="on" aria-disabled="false" role="columnheader" scope="col" tabindex="0" class="tablesorter-header tablesorter-headerUnSorted" data-column="3" width="10%"><div class="tablesorter-header-inner">&nbsp;<i class="tablesorter-icon"></i></div></th>
                            </tr>
                            </thead>
                            <tfoot>
                                <tr>
                                    <th class="tablesorter-headerAsc" data-column="0" width="15%">Category</th>
                                    <th data-column="1" width="35%">Name</th>
                                    <th data-column="2" width="20%">Harga</th>
                                    <th data-column="3" width="10%">&nbsp;</th>
                                </tr>
                            </tfoot>
                            <tbody aria-relevant="all" aria-live="polite">
                                <%
                                    int start = 0;
                                    int multiLanguageName = Integer.parseInt((String) com.dimata.system.entity.PstSystemProperty.getValueIntByName("NAME_MATERIAL_MULTI_LANGUAGE"));
                                    for (int i = 0; i < listData.size(); i++) {
                                        Vector vt = (Vector) listData.get(i);
                                        Material material = (Material) vt.get(0);
                                        PriceTypeMapping priceTypeMapping = (PriceTypeMapping) vt.get(1);
                                        Category cat = (Category) vt.get(2);
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
                                        start = start + 1;
                                %>
                                <tr style="" class="tablesorter-hasChildRow odd">
                                    <%if (material.getEditMaterial() == 4) {%>
                                    <td align="left"><a href="#" class="toggle"><%=cat.getName()%></a></td>
                                    <td align="left"><a href="#"><%=nameMat%> (Not Available)</a></td>
                                    <td align="left"><a href="#" class="toggle"><%=FRMHandler.userFormatStringDecimal(priceTypeMapping.getPrice())%></a></td>
                                    <td align="left"></td>
                                    <%} else {%>
                                    <td align="left"><a href="#" class="toggle"><%=cat.getName()%></a></td>
                                    <td align="left"><a href="javascript:cmdAddChart('<%=catId%>','<%=material.getMaterialId()%>','1','<%=nameMat%>')"><%=nameMat%></a></td>
                                    <td align="left"><a href="#" class="toggle"><%=FRMHandler.userFormatStringDecimal(priceTypeMapping.getPrice())%></a></td>
                                    <%if (typeFood.equals("0")) {%>
                                    <td align="left"><a href="javascript:cmdAddChart('<%=catId%>','<%=material.getMaterialId()%>','1','<%=nameMat%>')" style="color:#F90;"><em>ADD</em></a></td>
                                    <%} else {%>
                                    <td align="left"><a href="javascript:cmdAddChartBeverage('<%=catId%>','<%=material.getMaterialId()%>','1','<%=nameMat%>')" style="color:#F90;"><em>ADD</em></a></td>
                                    <%}%>
                                    <%}%>
                                </tr>
                                <%
                                    }
                                %>
                            </tbody>
                        </table>
                        <button class="btn btn-primary btn-md btn-block" type="button" onclick='location.href="<%=approot%>/outletonline/addorder.jsp"' >Add New Guest</button>
                        <%--<button class="btn btn-primary btn-md btn-block" type="button" onclick="">&nbsp;</button>--%>
                        <div class="pager">
                            <img src="../styles/takingorder/img/first.png" class="first" alt="First" />
                            <img src="../styles/takingorder/img/prev.png" class="prev" alt="Prev" />
                            <span class="pagedisplay"></span> <!-- this can be any element, including an input -->
                            <img src="../styles/takingorder/img/next.png" class="next" alt="Next" />
                            <img src="../styles/takingorder/img/last.png" class="last" alt="Last" />
                            <select class="pagesize" title="Select page size">
                                <option value="10">10</option>
                                <option value="20">20</option>
                                <option value="30">30</option>
                                <option value="40">40</option>
                                <option value="50">50</option>
                                <option value="60">60</option>
                                <option value="70">70</option>
                                <option value="80">80</option>
                                <option value="90">90</option>
                                <option value="100">100</option>
                            </select>
                            <select class="gotoPage" title="Select page number"></select>
                        </div>    
                    </div>
                    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                    <h4 class="modal-title" id="myModalLabel">ORDER</h4>
                                </div>
                                <div class="modal-body">
                                    <%-- body--%>
                                    <div class="guest" style="margin:10px 0px;">
                                        <div class="form-group">
                                            <div class="col-md-12" style="margin-bottom:10px;">
                                                <div class="form-group">
                                                    <div class=row>
                                                        <div class="col-md-12">
                                                            <input name="name_food" id="guestname" type="text" class="form-control" placeholder="Name" required>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <div class=row>
                                                        <div class="col-md-12">
                                                            <input name="qty_order" id="guestname" type="text" class="form-control" placeholder="qty" required>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <div class=row>
                                                        <div class="col-md-12">
                                                            <%--<input name="qty_order" id="guestname" type="text" class="form-control" placeholder="qty" required>--%>
                                                            <select id="varian" name="category_varian" class="form-control">
                                                                <option value="Medium">Medium</option>
                                                                <option value="Spicy">Spicy</option>
                                                                <option value="Crispy">Crispy</option>
                                                            </select>        
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <div class=row>
                                                        <div class="col-md-12">
                                                            <button class="btn btn-primary btn-md btn-block" onclick="javascript:cmdConfirm()" name="submit" >Confirm</button>
                                                        </div>
                                                    </div>
                                                </div>
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

                    <div class="modal fade" id="myModalBeverage" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                    <h4 class="modal-title" id="myModalLabel">ORDER</h4>
                                </div>
                                <div class="modal-body">
                                    <%-- body--%>
                                    <div class="guest" style="margin:10px 0px;">
                                        <div class="form-group">
                                            <div class="col-md-12" style="margin-bottom:10px;">
                                                <div class="form-group">
                                                    <div class=row>
                                                        <div class="col-md-12">
                                                            <input name="name_beverage" id="guestname" type="text" class="form-control" placeholder="Name" required>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <div class=row>
                                                        <div class="col-md-12">
                                                            <input name="qty_order_beverage" id="guestname" type="text" class="form-control" placeholder="qty" required>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <div class=row>
                                                        <div class="col-md-12">
                                                            <%--<input name="qty_order" id="guestname" type="text" class="form-control" placeholder="qty" required>--%>
                                                            <select id="varian" name="category_varian_beverage" class="form-control">
                                                                <option value="Ice">Ice</option>
                                                                <option value="No Ice">No Ice</option>
                                                                <option value="Tea">Tea</option>
                                                                <option value="Coffe">Coffe</option>
                                                            </select>        
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <div class=row>
                                                        <div class="col-md-12">
                                                            <button class="btn btn-primary btn-md btn-block" onclick="javascript:cmdConfirmBeverage()" name="submit">Confirm</button>
                                                        </div>
                                                    </div>
                                                </div>
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
            <hr>
            <div style="margin:20px 0px; text-align:center;">
            </div>
        </div>

    </body></html>
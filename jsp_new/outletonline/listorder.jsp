<%-- 
    Document   : listorder
    Created on : Dec 22, 2014, 12:09:56 PM
    Author     : dimata005
--%>
<%@page import="com.dimata.posbo.entity.admin.PstAppUser"%>
<%@page import="com.dimata.pos.entity.billing.Billdetail"%>
<%@page import="com.dimata.pos.entity.billing.PstBillDetail"%>
<%@page import="com.dimata.posbo.entity.masterdata.TableRoom"%>
<%@page import="com.dimata.posbo.entity.masterdata.Room"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterial"%>
<%@page import="com.dimata.posbo.entity.masterdata.PriceTypeMapping"%>
<%@page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@page import="com.dimata.pos.entity.billing.PstBillMain"%>
<%@page import="com.dimata.pos.entity.billing.BillMain"%>
<%@page import="com.dimata.qdep.form.FRMMessage"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.gui.jsp.ControlLine"%>
<%@page import="com.dimata.pos.form.billing.CtrlBillMain"%>
<%@page import="com.dimata.pos.form.billing.FrmBillMain"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.posbo.entity.shoppingchart.ShopCart"%>
<%@ include file = "../main/javainit.jsp" %>

<%!ShopCart shoppingCart = new ShopCart();%>

<%
    int iCommand = FRMQueryString.requestCommand(request);
    String salesCode = FRMQueryString.requestString(request, "FRM_FIELD_SALES_CODE");
    long cashBillMainId = FRMQueryString.requestLong(request, FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CASH_BILL_MAIN_ID]);
    String billNumber = FRMQueryString.requestString(request, FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_BILL_NUMBER]);
    long catId = FRMQueryString.requestLong(request, "catId");
    String oId = (String) request.getParameter("oId");
    String qty = request.getParameter("qty");
    long oidBillDetail = FRMQueryString.requestLong(request, "FRM_FIELD_CASH_BILL_DETAIL_ID");
    int update = FRMQueryString.requestInt(request, "UPDATE");
    int status = FRMQueryString.requestInt(request, "STATUS");
    int confirm = FRMQueryString.requestInt(request, "CONFIRM");
    String loginID = FRMQueryString.requestString(request, "username");
    String passwd = FRMQueryString.requestString(request, "password");
    String noteGlobal = FRMQueryString.requestString(request, "noteGlobal");
    String noteErr = FRMQueryString.requestString(request, "noteErr");
    String noteVoid = FRMQueryString.requestString(request, "noteVoid"); 
    String category_varian = FRMQueryString.requestString(request, "category_varian");
    String category_varian_beverage = FRMQueryString.requestString(request, "category_varian_beverage");
    String typeFood = FRMQueryString.requestString(request, "typeFood");

    String nameSpesialFood = FRMQueryString.requestString(request, "name_food");
    String nameSpesialBeverage = FRMQueryString.requestString(request, "name_beverage");
    int multiLanguageName = Integer.parseInt((String) com.dimata.system.entity.PstSystemProperty.getValueIntByName("NAME_MATERIAL_MULTI_LANGUAGE"));

    CtrlBillMain ctrlBillMain = new CtrlBillMain(request);
    ControlLine ctrLine = new ControlLine();
    FrmBillMain frmBillMain = ctrlBillMain.getForm();

    //disini update item sudah check out dari kitchent
    if (oidBillDetail != 0 & update != 0) {
        if (confirm == 1) {
            //cek login nya
            boolean booleanSucses = true;
            AppUser user = PstAppUser.getByLoginIDAndPassword(loginID, passwd);
            if (iCommand == Command.CANCEL && cashBillMainId != 0) {
                if (user != null) {
                    if (user.getUserGroupNew() == 1) {
                        int sucsess = ctrlBillMain.actionUpdateItem(update, oidBillDetail, status);
                    }
                }
            } else {
                if (user != null) {
                    if (user.getUserGroupNew() == 1) {
                        int sucsess = ctrlBillMain.actionUpdateItem(update, oidBillDetail, status,noteErr);
                    }
                }
            }
        } else {
            int sucsess = ctrlBillMain.actionUpdateItem(update, oidBillDetail, status, noteVoid);
        }
    }
    
    if (iCommand == Command.CANCEL && cashBillMainId != 0) {
        AppUser user = PstAppUser.getByLoginIDAndPassword(loginID, passwd);
        if (user != null) {
            if (user.getUserGroupNew() == 1) {
                int sucsess = ctrlBillMain.actionUpdateItem(iCommand, cashBillMainId, status, noteErr);
            }
        }
    }

    if (typeFood.equals("0")) {
        //food 
        category_varian = category_varian;
    } else {
        //beverage
        category_varian = category_varian_beverage;
    }
    if (nameSpesialFood.equals("0")) {
        nameSpesialFood = nameSpesialBeverage;
    } else {
        nameSpesialFood = nameSpesialFood;
    }
    //cek session listchart
    if (!session.equals(null)) {
    } else {
        shoppingCart = (ShopCart) session.getValue("cart");
    }
    if (iCommand != Command.SAVEALL && oId != null && qty != null && cashBillMainId != 0 && !oId.equals("") && !qty.equals("")) {
        shoppingCart.addToCart(oId, Integer.parseInt(qty), category_varian, nameSpesialFood,cashBillMainId);
    }
    session.putValue("cart", shoppingCart);

    //cek session bill main
    int iErrCode = FRMMessage.NONE;
    if (cashBillMainId == 0) {
        iErrCode = ctrlBillMain.action(iCommand, cashBillMainId, 0);
    }

    BillMain billMain = ctrlBillMain.getBillMain();
    try {
        billMain = PstBillMain.fetchExc(cashBillMainId);
    } catch (Exception ex) {
    }

    String whereOpenBill = " cbm.DOC_TYPE=0 AND cbm.TRANSACTION_TYPE=0 AND cbm.TRANSACTION_STATUS=1 AND cbm.TABLE_ID!=0 ";
    if (billMain.getTableId() != 0) {
        whereOpenBill = whereOpenBill + " AND cbm.TABLE_ID=" + billMain.getTableId();
    }
    if (billMain.getRoomID() != 0) {
        whereOpenBill = whereOpenBill + " AND cbm.ROOM_ID=" + billMain.getRoomID();
    }
    Vector listTransaction = PstBillMain.listOpenBill(0, 0, whereOpenBill, "");

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
        int sucsess = ctrlBillMain.actionSaveChart(0, fetchData, cashBillMainId,noteGlobal);
        if (sucsess == 0) {
            iCommand = Command.NONE;
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

    if (iCommand == Command.REFRESH) {
        iCommand = Command.NONE;
        //cashBillMainId=0;
        priceQty = 0.0;
        shoppingCart.destroyCart();
        fetchData = new Vector();
        session.putValue("cart", shoppingCart);
        response.sendRedirect("tableorder.jsp");
    }
%>

<html xmlns="http://www.w3.org/1999/xhtml">
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
                        <%--<script src="../styles/takingorder/js/jquery.js"></script>
                        <script type="text/javascript" src="../styles/jquery.min.js"></script>--%>
                        <!-- Tablesorter: required -->
                        <link href="../styles/takingorder/css/theme.css" rel="stylesheet">
                            <%--<script src="../styles/takingorder/js/jquery_002.js"></script>
                            <script src="../styles/takingorder/js/jquery_003.js"></script>
                            <!-- Tablesorter: pager -->
                            <link rel="stylesheet" href="../styles/takingorder/js/jquery.css">
                            <script src="../styles/takingorder/js/widget-pager.js"></script>--%>

                                <%--<script type="text/javascript" src="../styles/takingorder/js/bootstrap.min.js"></script>--%>
                                <script language="JavaScript">
                                    function cmdBack() {
                                        document.frmsrcsalesorder.command.value = "<%=Command.REFRESH%>";
                                        document.frmsrcsalesorder.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CASH_BILL_MAIN_ID]%>.value = 0;
                                        document.frmsrcsalesorder.action = "listorder.jsp";
                                        document.frmsrcsalesorder.submit();
                                    }
                                </script>
                                <style type="text/css">
                                    .page {
                                        width: 29.7cm;
                                        min-height: 21cm;
                                        padding:1cm;
                                        margin: 1cm auto;
                                        border: 1px #D3D3D3 solid;
                                        border-radius: 5px;
                                        background: white;
                                        box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
                                    }
                                    .subpage {
                                        min-height: 21cm;
                                    }

                                    .printable{
                                        display:none;
                                    }

                                    .modal-lg-print{
                                        max-width: 1366px;
                                        width: 96.5%;
                                    }

                                    @page{
                                        size: portrait;
                                        margin: 0;
                                    }

                                    @media print{
                                        .printable{
                                                display: block;
                                                visibility: visible;
                                        }
                                        .nonprint{
                                                display: none;
                                                visibility: hidden;
                                        }
                                        .page {
                                            margin:0;
                                            width: 7.5 cm;
                                            min-height: 10cm;
                                            background: white;
                                            border:none;
                                        }
                                        .box-default{
                                            border:1px solid #ccc;
                                            border-top:3px solid #ccc;
                                        }
                                        
                                        td,th{
                                            font-size: 8px;
                                        }
                                    }
                                </style>        
                                </head>
                                <body >
                                    <div style="background:#f0f0f0; margin-bottom:10px; padding:5px;" class="nonprint">
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
                                    <div class="container nonprint">
                                        <center>
                                            <a href="#"><img src="../styles/takingorder/img/queens-head.png" border="none" width="250"></a>
                                        </center>
                                        <div style="margin-top:20px;">
                                            <a href="javascript:cmdBack()">Back to Main</a>
                                            <script id="js">
                                                $(function() {
                                                    // hide child rows
                                                    $('.tablesorter-childRow td').hide();
                                                    var $table = $('.tablesorter-dropbox')
                                                            .tablesorter({
                                                        headerTemplate: '{content}{icon}', // dropbox theme doesn't like a space between the content & icon
                                                        sortList: [[0, 0]],
                                                        showProcessing: true,
                                                        cssChildRow: "tablesorter-childRow",
                                                        widgets: ["pager", "zebra", "filter"],
                                                        widgetOptions: {
                                                            filter_columnFilters: false,
                                                            filter_saveFilters: true,
                                                        }
                                                    })
                                                    $('.tablesorter-dropbox').delegate('.toggle', 'click', function() {
                                                        $(this).closest('tr').nextUntil('tr.tablesorter-hasChildRow').find('td').toggle();
                                                        return false;
                                                    });

                                                    $.tablesorter.filter.bindSearch($table, $('.search'));
                                                    $('select').change(function() {
                                                        // modify the search input data-column value (swap "0" or "all in this demo)
                                                        $('.selectable').attr('data-column', $(this).val());
                                                        // update external search inputs
                                                        $.tablesorter.filter.bindSearch($table, $('.search'), false);
                                                    });
                                                });

                                                function cmdOk() {
                                                    //alert("ok");
                                                    document.frmsrcsalesorder.command.value = "<%=Command.UPDATE%>";
                                                    document.frmsrcsalesorder.action = "listorder.jsp";
                                                    document.frmsrcsalesorder.submit();
                                                }

                                                function addMore(oid) {
                                                    // alert("asa");
                                                    document.frmsrcsalesorder.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CASH_BILL_MAIN_ID]%>.value=oid;
                                                    document.frmsrcsalesorder.command.value = "<%=Command.BACK%>";
                                                    document.frmsrcsalesorder.action = "order.jsp";
                                                    document.frmsrcsalesorder.submit();
                                                }

                                                /*function saveCheckOut(oid) {
                                                    document.frmsrcsalesorder.command.value = "<%=Command.SAVEALL%>";
                                                    document.frmsrcsalesorder.action = "mobileoutlet.jsp";
                                                    document.frmsrcsalesorder.submit();
                                                }*/

                                                function saveCheckOut(oid) {
                                                    document.frmsrcsalesorder.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CASH_BILL_MAIN_ID]%>.value=oid;
                                                    document.frmsrcsalesorder.command.value = "<%=Command.SAVEALL%>";
                                                    document.frmsrcsalesorder.action = "listorder.jsp";
                                                    $(document).ready(function() {
                                                        $('#myModalSubmit').modal('show');
                                                    });
                                                    //document.frmsrcsalesorder.submit();
                                                }

                                                function actionBillItem(cashbilldetail, status, action, cashbillmain) {
                                                    document.frmsrcsalesorder.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CASH_BILL_MAIN_ID]%>.value = cashbillmain;
                                                    document.frmsrcsalesorder.FRM_FIELD_CASH_BILL_DETAIL_ID.value = cashbilldetail;
                                                    document.frmsrcsalesorder.STATUS.value = status;
                                                    document.frmsrcsalesorder.UPDATE.value = action;

                                                    $(document).ready(function() {
                                                        $('#myModal').modal('show');
                                                    });
                                                }
                                                
                                                function actionBillItemVoid(cashbilldetail, status, action, cashbillmain) {
                                                    document.frmsrcsalesorder.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CASH_BILL_MAIN_ID]%>.value = cashbillmain;
                                                    document.frmsrcsalesorder.FRM_FIELD_CASH_BILL_DETAIL_ID.value = cashbilldetail;
                                                    document.frmsrcsalesorder.CONFIRM.value ="0";
                                                    document.frmsrcsalesorder.STATUS.value = status;
                                                    document.frmsrcsalesorder.UPDATE.value = action;
                                                    $(document).ready(function() {
                                                        $('#myModalVoid').modal('show');
                                                    });
                                                }
                                                
                                                function cmdActionVoid(){
                                                    document.frmsrcsalesorder.submit();
                                                }

                                                function saveCancel(oid) {
                                                    document.frmsrcsalesorder.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CASH_BILL_MAIN_ID]%>.value=oid;
                                                    document.frmsrcsalesorder.command.value = "<%=Command.CANCEL%>";
                                                    $(document).ready(function() {
                                                        $('#myModal').modal('show');
                                                    });
                                                }



                                                function cmdConfirm() {
                                                    var xx = document.frmsrcsalesorder.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CASH_BILL_MAIN_ID]%>.value;
                                                    var yy = document.frmsrcsalesorder.FRM_FIELD_CASH_BILL_DETAIL_ID.value;
                                                    var zz = document.frmsrcsalesorder.STATUS.value;
                                                    var tt = document.frmsrcsalesorder.UPDATE.value;

                                                    document.frmsrcsalesorder.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CASH_BILL_MAIN_ID]%>.value = xx;
                                                    document.frmsrcsalesorder.FRM_FIELD_CASH_BILL_DETAIL_ID.value = yy;
                                                    document.frmsrcsalesorder.STATUS.value = zz;
                                                    document.frmsrcsalesorder.UPDATE.value = tt;
                                                    document.frmsrcsalesorder.CONFIRM.value = 1;
                                                    document.frmsrcsalesorder.submit();
                                                }
                                                
                                                function cmdPrint(){
                                                    window.print();
                                                }
                                                
                                            </script>
                                            <form name="frmsrcsalesorder" method ="post" action="" role="form">
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
                                                                                    <input type="hidden" name="FRM_FIELD_CASH_BILL_DETAIL_ID" value="">
                                                                                        <input type="hidden" name="STATUS" value="">
                                                                                            <input type="hidden" name="UPDATE" value="">
                                                                                                <input type="hidden" name="CONFIRM" value="">
                                                                                                    <%
                                                                                                        for (int b = 0; b < listTransaction.size(); b++) {
                                                                                                            Vector dataBill = (Vector) listTransaction.get(b);
                                                                                                            billMain = (BillMain) dataBill.get(0);
                                                                                                            Room room = (Room) dataBill.get(1);
                                                                                                            TableRoom table = (TableRoom) dataBill.get(2);
                                                                                                                Vector listOrder = new Vector(1, 1);
                                                                                                                String whereOrder = "CD.CASH_BILL_MAIN_ID='" + billMain.getOID() + "'";
                                                                                                                listOrder = PstBillDetail.listMat(0, 0, whereOrder, "");
                                                                                                    %>
                                                                                                    <hr style="margin-top:10px;">
                                                                                                        Name Guest :<%=billMain.getGuestName()%>
                                                                                                        <br>Rooom : <%=room.getName()%>
                                                                                                            <br>Meja :<%=table.getTableNumber()%>
                                                                                                                <br>
                                                                                                                    <%--<button class="btn btn-primary btn-md btn-block" type="button" onclick="javascript:addMore()">Add More</button>--%>    
                                                                                                                    <div class="guest" style="margin:10px 0px;">
                                                                                                                        <input data-lastsearchtime="1416898435043" class="form-control search selectable" placeholder="Search keyword.." data-column="all" type="search">
                                                                                                                            <table role="grid" class="tablesorter-dropbox tablesorter hasFilters">
                                                                                                                                <thead>
                                                                                                                                    <tr role="row" class="tablesorter-headerRow">
                                                                                                                                        <th aria-label="#: Ascending sort applied, activate to apply a descending sort" aria-sort="ascending" style="-moz-user-select: none;" unselectable="on" aria-disabled="false" role="columnheader" scope="col" tabindex="0" class="tablesorter-header tablesorter-headerAsc" data-column="0" width="10%"><div class="tablesorter-header-inner">#<i class="tablesorter-icon"></i></div></th>
                                                                                                                                        <th aria-label="Guest Name: No sort applied, activate to apply an ascending sort" aria-sort="none" style="-moz-user-select: none;" unselectable="on" aria-disabled="false" role="columnheader" scope="col" tabindex="0" class="tablesorter-header tablesorter-headerUnSorted" data-column="1" width="50%"><div class="tablesorter-header-inner">Nama<i class="tablesorter-icon"></i></div></th>
                                                                                                                                        <th aria-label="Guest Name: No sort applied, activate to apply an ascending sort" aria-sort="none" style="-moz-user-select: none;" unselectable="on" aria-disabled="false" role="columnheader" scope="col" tabindex="0" class="tablesorter-header tablesorter-headerUnSorted" data-column="2" width="10%"><div class="tablesorter-header-inner">Qty<i class="tablesorter-icon"></i></div></th>
                                                                                                                                        <th aria-label="Guest Name: No sort applied, activate to apply an ascending sort" aria-sort="none" style="-moz-user-select: none;" unselectable="on" aria-disabled="false" role="columnheader" scope="col" tabindex="0" class="tablesorter-header tablesorter-headerUnSorted" data-column="3" width="10%"><div class="tablesorter-header-inner">Note<i class="tablesorter-icon"></i></div></th>
                                                                                                                                        <th aria-label="Guest Name: No sort applied, activate to apply an ascending sort" aria-sort="none" style="-moz-user-select: none;" unselectable="on" aria-disabled="false" role="columnheader" scope="col" tabindex="0" class="tablesorter-header tablesorter-headerUnSorted" data-column="4" width="10%"><div class="tablesorter-header-inner">Status<i class="tablesorter-icon"></i></div></th>
                                                                                                                                        <th aria-label="Guest Name: No sort applied, activate to apply an ascending sort" aria-sort="none" style="-moz-user-select: none;" unselectable="on" aria-disabled="false" role="columnheader" scope="col" tabindex="0" class="tablesorter-header tablesorter-headerUnSorted" data-column="5" width="10%"><div class="tablesorter-header-inner">#<i class="tablesorter-icon"></i></div></th>
                                                                                                                                    </tr>
                                                                                                                                </thead>
                                                                                                                                <tfoot>
                                                                                                                                    <tr>
                                                                                                                                        <th class="tablesorter-headerAsc" data-column="0" width="10%">#</th>
                                                                                                                                        <th data-column="1" width="50%">Name</th>
                                                                                                                                        <th data-column="2" width="10%">Qty</th>
                                                                                                                                            <%--<th data-column="3" width="15%">Price</th>--%>
                                                                                                                                            <%--<th data-column="4" width="20%">Total</th>--%>
                                                                                                                                        <th data-column="3" width="10%">Note</th>
                                                                                                                                        <th data-column="4" width="10%">Status</th>
                                                                                                                                        <th data-column="5" width="10%">#</th>
                                                                                                                                    </tr>
                                                                                                                                </tfoot>
                                                                                                                                <tbody aria-relevant="all" aria-live="polite">
                                                                                                                                    <%
                                                                                                                                        int startorder = 0;
                                                                                                                                        double amountTotal = 0;
                                                                                                                                        for (int i = 0; i < listOrder.size(); i++) {
                                                                                                                                            Vector vt = (Vector) listOrder.get(i);
                                                                                                                                            Billdetail billdetail = (Billdetail) vt.get(0);
                                                                                                                                            startorder = startorder + 1;
                                                                                                                                            amountTotal = amountTotal + billdetail.getTotalPrice();
                                                                                                                                            String[] smartPhonesSplits = billdetail.getItemName().split("\\;");
                                                                                                                                            String nameMat = "";
                                                                                                                                            if (multiLanguageName == 1) {
                                                                                                                                                try {
                                                                                                                                                    nameMat = smartPhonesSplits[0];
                                                                                                                                                } catch (Exception ex) {
                                                                                                                                                }
                                                                                                                                            } else {
                                                                                                                                                nameMat = billdetail.getItemName();
                                                                                                                                            }
                                                                                                                                            String statusItem = "";
                                                                                                                                            if (billdetail.getStatus() == 0) {
                                                                                                                                                statusItem = "Order";
                                                                                                                                            } else if (billdetail.getStatus() == 1) {
                                                                                                                                                statusItem = "Check Out";
                                                                                                                                            } else {
                                                                                                                                                statusItem = "Reserved";
                                                                                                                                            }
                                                                                                                                    %>
                                                                                                                                    <tr style="" class="tablesorter-hasChildRow odd">
                                                                                                                                        <td style="text-align:center;"><a href="#" class="toggle"><%=startorder%></a></td>
                                                                                                                                        <td align="left"><a href="#" class="toggle"><%=nameMat%></a></td>
                                                                                                                                        <td align="left"><a href="#" class="toggle"><%=billdetail.getQty()%></td>
                                                                                                                                        <%--<td align="left"><a href="#" class="toggle"><%=billdetail.getItemPrice()%></a></td>
                                                                                                                                        <td align="left"><a href="#" class="toggle"><%=billdetail.getTotalPrice()%></a></td>--%>
                                                                                                                                        <td align="left"><a href="#" class="toggle"><%=billdetail.getNote()%></a></td>
                                                                                                                                        <td align="left"><a href="#" class="toggle"><%=statusItem%></a></td>
                                                                                                                                        <td align="left">
                                                                                                                                            <a href="listorder.jsp?FRM_FIELD_CASH_BILL_DETAIL_ID=<%=billdetail.getOID()%>&STATUS=2&UPDATE=61&FRM_FIELD_CASH_BILL_MAIN_ID=<%=billMain.getOID()%>" style="color:#F90;"><em>SERVED</em></a>
                                                                                                                                            |
                                                                                                                                            <%
                                                                                                                                            if (billdetail.getStatus() == 0) {%>
                                                                                                                                                <%--<a href="listorder.jsp?FRM_FIELD_CASH_BILL_DETAIL_ID=<%=billdetail.getOID()%>&STATUS=2&UPDATE=6&FRM_FIELD_CASH_BILL_MAIN_ID=<%=billMain.getOID()%>" style="color:#F90;"><em>VOID</em></a>--%>
                                                                                                                                                <a href="javascript:actionBillItemVoid('<%=billdetail.getOID()%>','2','6','<%=cashBillMainId%>')" style="color:#F90;"><em>VOID</em></a>
                                                                                                                                            <%} else {%>
                                                                                                                                                <a href="javascript:actionBillItem('<%=billdetail.getOID()%>','2','44','<%=cashBillMainId%>')" style="color:#F90;"><em>ERR</em></a>
                                                                                                                                            <%}%>
                                                                                                                                        </td>
                                                                                                                                    </tr>
                                                                                                                                    <%
                                                                                                                                        }
                                                                                                                                    %>

                                                                                                                                    <%
                                                                                                                                        //int start=0;
                                                                                                                                        boolean selected=false;
                                                                                                                                        for (int i = 0; i < fetchData.size(); i++) {
                                                                                                                                            Vector vt = (Vector) fetchData.get(i);
                                                                                                                                            long oidCashBillMainIdSelected = (Long) vt.get(5);
                                                                                                                                            if(oidCashBillMainIdSelected==billMain.getOID()){
                                                                                                                                                selected=true;
                                                                                                                                                Material material = (Material) vt.get(0);
                                                                                                                                                String note = (String) vt.get(3);
                                                                                                                                                PriceTypeMapping priceTypeMapping = (PriceTypeMapping) vt.get(1);
                                                                                                                                                int amount = (Integer) vt.get(2);
                                                                                                                                                String nameMat = (String) vt.get(4);
                                                                                                                                                startorder = startorder + 1;
                                                                                                                                                amountTotal = amountTotal + (priceTypeMapping.getPrice() * amount);
                                                                                                                                            %>
                                                                                                                                                <tr style="" class="tablesorter-hasChildRow odd">
                                                                                                                                                    <td style="text-align:center;"><a href="#" class="toggle"><%=startorder%></a></td>
                                                                                                                                                    <td align="left"><a href="#" class="toggle"><%=nameMat%></a></td>
                                                                                                                                                    <td align="left"><a href="#" class="toggle">
                                                                                                                                                            <span id="qtyValue<%=material.getMaterialId()%>" style="display:inline"><%=amount%></span>
                                                                                                                                                            <input type="number" name="qtyEditValue<%=material.getMaterialId()%>" id="qtyEditValue<%=material.getMaterialId()%>" min="1" max="999" value="<%=amount%>" style="display:none" maxlength="3" >
                                                                                                                                                        </a>
                                                                                                                                                    </td>
                                                                                                                                                    <td align="left"><a href="#" class="toggle"><%=note%></a></td>
                                                                                                                                                    <td align="left"><a href="#" class="toggle"></a></td>
                                                                                                                                                    <td align="left"><a href="#" class="toggle"></a></td>
                                                                                                                                                </tr>
                                                                                                                                            <%
                                                                                                                                            }
                                                                                                                                        }
                                                                                                                                    %>
                                                                                                                                    <tr style="" class="tablesorter-hasChildRow odd">
                                                                                                                                        <td style="text-align:center;"><a href="#" class="toggle"></a></td>
                                                                                                                                        <td align="left"><a href="#" class="toggle"></a>TOTAL PRICE</td>
                                                                                                                                        <%if(selected){%>
                                                                                                                                            <td align="left"><a href="#" class="toggle"></a>
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
                                                                                                                                            </td>
                                                                                                                                        <%}else{%>
                                                                                                                                            <td style="text-align:left;"><a href="#" class="toggle"></a></td>
                                                                                                                                        <%}%>
                                                                                                                                        <td style="text-align:left;"><a href="#" class="toggle"></a><%=amountTotal%></td>
                                                                                                                                        <td style="text-align:left;"><a href="#" class="toggle"></a></td>
                                                                                                                                        <td style="text-align:left;"><a href="#" class="toggle"></a></td>
                                                                                                                                    </tr>
                                                                                                                                </tbody>
                                                                                                                            </table><br>
                                                                                                                             Global Note : <%=billMain.getNotes()%>           
                                                                                                                    </div>
                                                                                                                    <button class="btn btn-primary btn-md btn-block" type="button" onclick="javascript:addMore('<%=billMain.getOID()%>')">Add More</button>                    
                                                                                                                    <button class="btn btn-success btn-md btn-block" type="button" onclick="javascript:saveCheckOut('<%=billMain.getOID()%>')">Check Out</button>  
                                                                                                                    <button class="btn btn-danger btn-md btn-block" type="button" onclick="javascript:cmdView('<%=billMain.getOID()%>')">View Detail</button>
                                                                                                                    <button class="btn btn-danger btn-md btn-block" type="button" onclick="javascript:saveCancel('<%=billMain.getOID()%>')">Cancel All Bill</button>
                                                                                                                    <%
                                                                                                                        }
                                                                                                                    %>    


                                                                                                                    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                                                                                                                        <div class="modal-dialog">
                                                                                                                            <div class="modal-content">
                                                                                                                                <div class="modal-header">
                                                                                                                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                                                                                                                    <h4 class="modal-title" id="myModalLabel">CONFIRMATION</h4>
                                                                                                                                </div>
                                                                                                                                <div class="modal-body">
                                                                                                                                    <%-- body--%>
                                                                                                                                    <div class="guest" style="margin:10px 0px;">
                                                                                                                                        <div class="form-group">
                                                                                                                                            <div class="col-md-12" style="margin-bottom:10px;">
                                                                                                                                                <div class="form-group">
                                                                                                                                                    <div class=row>
                                                                                                                                                        <div class="col-md-12">
                                                                                                                                                            <input name="username" id="guestname" type="text" class="form-control" placeholder="Username" required>
                                                                                                                                                        </div>
                                                                                                                                                    </div>
                                                                                                                                                </div> 
                                                                                                                                                <div class="form-group">
                                                                                                                                                    <div class=row>
                                                                                                                                                        <div class="col-md-12">
                                                                                                                                                            <input name="password" id="mobilenumber" type="text" class="form-control" placeholder="Password" required>
                                                                                                                                                        </div>
                                                                                                                                                    </div>
                                                                                                                                                </div>  
                                                                                                                                                <div class="form-group">
                                                                                                                                                    <div class=row>
                                                                                                                                                        <div class="col-md-12">
                                                                                                                                                            Note : <textarea id="txtArea" rows="2" cols="70" name="noteErr" ></textarea> 
                                                                                                                                                        </div>
                                                                                                                                                    </div>
                                                                                                                                                </div> 
                                                                                                                                                <div class="form-group">
                                                                                                                                                    <div class=row>
                                                                                                                                                        <div class="col-md-12">
                                                                                                                                                            <button class="btn btn-primary btn-md btn-block" onclick="javascript:cmdConfirm()" type="button" >Confirm</button>
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
                                                                                                                    <div class="modal fade" id="myModalSubmit" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                                                                                                                        <div class="modal-dialog">
                                                                                                                            <div class="modal-content">
                                                                                                                                <div class="modal-header">
                                                                                                                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                                                                                                                    <h4 class="modal-title" id="myModalLabel">CONFIRMATION</h4>
                                                                                                                                </div>
                                                                                                                                <div class="modal-body">
                                                                                                                                    <div class="guest" style="margin:10px 0px;">
                                                                                                                                        <div class="form-group">
                                                                                                                                            <div class="col-md-12" style="margin-bottom:10px;">
                                                                                                                                                <div class="form-group">
                                                                                                                                                    <div class=row>
                                                                                                                                                        <div class="col-md-12">
                                                                                                                                                             Global Note : <textarea id="txtArea" rows="2" cols="70" name="noteGlobal" ></textarea> 
                                                                                                                                                        </div>
                                                                                                                                                    </div>
                                                                                                                                                </div>        
                                                                                                                                                <div class="form-group">
                                                                                                                                                    <div class=row>
                                                                                                                                                        <div class="col-md-12">
                                                                                                                                                            <button class="btn btn-primary btn-md btn-block" onclick="javascript:cmdConfirm()" type="button" >Confirm</button>
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
                                                                                                                                    
                                                                                                                    <div class="modal fade" id="myModalVoid" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                                                                                                                        <div class="modal-dialog">
                                                                                                                            <div class="modal-content">
                                                                                                                                <div class="modal-header">
                                                                                                                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                                                                                                                    <h4 class="modal-title" id="myModalLabel">CONFIRMATION VOID</h4>
                                                                                                                                </div>
                                                                                                                                <div class="modal-body">
                                                                                                                                    <div class="guest" style="margin:10px 0px;">
                                                                                                                                        <div class="form-group">
                                                                                                                                            <div class="col-md-12" style="margin-bottom:10px;">
                                                                                                                                                <div class="form-group">
                                                                                                                                                    <div class=row>
                                                                                                                                                        <div class="col-md-12">
                                                                                                                                                             Note : <textarea id="txtArea" rows="2" cols="70" name="noteVoid" ></textarea> 
                                                                                                                                                        </div>
                                                                                                                                                    </div>
                                                                                                                                                </div>        
                                                                                                                                                <div class="form-group">
                                                                                                                                                    <div class=row>
                                                                                                                                                        <div class="col-md-12">
                                                                                                                                                            <button class="btn btn-primary btn-md btn-block" onclick="javascript:cmdActionVoid()" type="button" >Confirm</button>
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
                                                                                                                    
                                                                                                                    <script type="text/javascript" src="../styles/jquery.min.js"></script>
                                                                                                                    <!-- jQuery UI 1.10.3 -->
                                                                                                                    <script src="../styles/bootstrap3.1/js/jquery-ui-1.10.3.min.js" type="text/javascript"></script>
                                                                                                                    <!-- Bootstrap -->
                                                                                                                    <script src="../styles/bootstrap3.1/js/bootstrap.min.js" type="text/javascript"></script>
                                                                                                                    <script language="JavaScript">
                                                                                                                        function cmdView(oid) {
                                                                                                                            // alert("aaaa");
                                                                                                                             ajaxScriptParentPage("ajaxDataSource.jsp","Search Bill",4,"#myModalView",oid, "#modal-title", "#modal-body");
                                                                                                                             //test();
                                                                                                                         }
                                                                                                                        
                                                                                                                        //$(document).ready(function(){
                                                                                                                            function ajaxScriptParentPage(pageTarget, titlePage, pageShow, modalTemplate, oid, titleId, bodyId){
                                                                                                                                //alert("hhh");
                                                                                                                                $(titleId).html(titlePage);
                                                                                                                                $(modalTemplate).modal("show");
                                                                                                                                $(bodyId).html("Harap tunggu");
                                                                                                                                //var idNo = $("#guestname").val();
                                                                                                                                $.ajax({
                                                                                                                                    type	: "POST",
                                                                                                                                    url	: pageTarget,
                                                                                                                                    data	: {"searchType":"parent", 
                                                                                                                                                "pageShow":pageShow,
                                                                                                                                                "studentName":oid,
                                                                                                                                                "oid":oid
                                                                                                                                            },
                                                                                                                                    cache	: false,
                                                                                                                                    success	: function(data){
                                                                                                                                        $(bodyId).html(data);
                                                                                                                                        $(".print-body").html(data).fadeIn("medium");

                                                                                                                                    },
                                                                                                                                    error : function(){
                                                                                                                                        $(bodyId).html("Data not found");
                                                                                                                                    }
                                                                                                                                });
                                                                                                                            }

                                                                                                                            //agar modal tidak close saat are di luar form di klik
                                                                                                                            $("#myModalView").modal({
                                                                                                                                            backdrop:"static",
                                                                                                                                            keyboard:false,
                                                                                                                                            show:false
                                                                                                                            });

                                                                                                                            //fungsi jika tombol search di klik, maka akan menampilakn data
                                                                                                                            /*$(".search").click(function(){
                                                                                                                                
                                                                                                                            });*/

                                                                                                                            //dokument yang 
                                                                                                                            function searchList(){
                                                                                                                                $("form#searchList").submit(function(){
                                                                                                                                    $("#resultSearch").html("Harap Tunggu").fadeIn("medium");
                                                                                                                                    $("#resultFirst").hide();
                                                                                                                                    $.ajax({
                                                                                                                                        type	: "POST",
                                                                                                                                        url		: "ajaxDataSource.jsp",
                                                                                                                                        data	: $(this).serialize(),
                                                                                                                                        cache	: false,
                                                                                                                                        success	: function(data){
                                                                                                                                            $("#resultSearch").html(data).fadeIn("medium");
                                                                                                                                            $(".print-body").html(data).fadeIn("medium");

                                                                                                                                        },
                                                                                                                                        error : function(){

                                                                                                                                            alert("error");
                                                                                                                                        }
                                                                                                                                    });
                                                                                                                                    return false;
                                                                                                                                });
                                                                                                                            }

                                                                                                                            //event modal, di tambahkan agar pada saat search, tetap berada di modal
                                                                                                                            $("#myModalView").on("shown.bs.modal",function (e){
                                                                                                                                ajaxFunctionChildPage(searchList());

                                                                                                                            });

                                                                                                                            function ajaxFunctionChildPage(ajaxFunction){
                                                                                                                                return ajaxFunction;
                                                                                                                            }

                                                                                                                            $("#buttonSave").click(function(){
                                                                                                                            });

                                                                                                                       //});
                                                                                                                    </script>                 
                                                                                                                    <div id="myModalView" class="modal fade" tabindex="-1">
                                                                                                                        <div class="modal-dialog modal-lg">
                                                                                                                            <div class="modal-content">
                                                                                                                                <div class="modal-header">
                                                                                                                                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                                                                                                                    <h4 class="modal-title" id="modal-title"></h4>
                                                                                                                                </div>

                                                                                                                                <div class="modal-body" id="modal-body">
                                                                                                                                </div>
                                                                                                                                <div class="modal-footer">
                                                                                                                                    <button type="button" data-dismiss="modal" class="btn btn-danger">Close</button>
                                                                                                                                    <button type="button" class="btn btn-primary" onclick="javascript:cmdPrint()">Print</button>
                                                                                                                                </div>
                                                                                                                            </div>
                                                                                                                        </div>
                                                                                                                    </div>
                                                                                                                    </div>
                                                                                                                    <hr>
                                                                                                                        <div style="margin:20px 0px; text-align:center;">
                                                                                                                        </div>
                                                                                                                        </div>
                                                                                                                                    
                                                                                                                        <div class="printable">
                                                                                                                            <span class="print-body"></span>
                                                                                                                        </div>
                                                                                                                        </body>
                                                                                                                        </html>
                                                                                                                        <script type="text/javascript">
                                                                                                                            function toggleVisibility(id) {
                                                                                                                                var e = document.getElementById(id);
                                                                                                                                if (e.style.display == 'inline')
                                                                                                                                    e.style.display = 'none';
                                                                                                                                else
                                                                                                                                    e.style.display = 'inline';
                                                                                                                            }
                                                                                                                            function toggleVisibility2(id) {
                                                                                                                                var e = document.getElementById(id);
                                                                                                                                if (e.style.display == 'inline')
                                                                                                                                    e.style.display = 'none';
                                                                                                                                else
                                                                                                                                    e.style.display = 'inline';
                                                                                                                            }
                                                                                                                            function buttonValueOk() {
                                                                                                                                var e = document.getElementById(buttonValue);
                                                                                                                                e.value = 1
                                                                                                                            }
                                                                                                                            function buttonValueDelete() {
                                                                                                                                var e = document.getElementById(buttonValue);
                                                                                                                                e.value = 2
                                                                                                                            }
                                                                                                                        </script>
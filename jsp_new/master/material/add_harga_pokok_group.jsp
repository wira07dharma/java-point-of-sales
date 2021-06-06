<%-- 
    Document   : add_harga_pokok_group
    Created on : Jan 19, 2018, 4:46:10 PM
    Author     : Ed
--%>

<%@page import="com.dimata.posbo.entity.masterdata.Costing"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstCosting"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatCostingItem"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterialStock"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatCosting"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatCosting"%>
<%@page import="com.dimata.posbo.session.masterdata.SessPosting"%>
<%@page import="com.dimata.posbo.entity.masterdata.MaterialStock"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstUnit"%>
<%@page import="com.dimata.posbo.entity.masterdata.Unit"%>
<%@page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterial"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatReceiveItem"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatDispatchItem"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatDispatchItem"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatReceiveItem"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatDispatchReceiveItem"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatDispatchReceiveItem"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatReceive"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatReceive"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatDispatch"%>
<%@page import="com.dimata.qdep.form.FRMMessage"%>
<%@page import="com.dimata.posbo.form.warehouse.CtrlMatDispatch"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatDispatch"%>
<%@page import="com.dimata.gui.jsp.ControlList"%>
<%@ page import= "com.dimata.common.entity.location.PstLocation,
         com.dimata.common.entity.location.Location,
         com.dimata.qdep.entity.I_PstDocType,
         com.dimata.qdep.form.FRMQueryString,
         com.dimata.gui.jsp.ControlLine,
         com.dimata.util.Command,
         com.dimata.gui.jsp.ControlCombo,
         com.dimata.gui.jsp.ControlDate,
         com.dimata.posbo.session.warehouse.SessMatDispatch,
         com.dimata.posbo.entity.search.SrcMatDispatch,
         com.dimata.posbo.form.warehouse.FrmMatDispatch,
         com.dimata.posbo.form.search.FrmSrcMatDispatch"%>
<%@ page import="com.dimata.posbo.entity.admin.PstAppUser"%>
<%@ page language = "java" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../../main/javainit.jsp" %>

<%    SrcMatDispatch srcMatDispatch = new SrcMatDispatch();
    FrmSrcMatDispatch frmSrcMatDispatch = new FrmSrcMatDispatch(request, srcMatDispatch);
    MatDispatch matDispatch = new MatDispatch();
    FrmMatDispatch frmMatDispatch = new FrmMatDispatch();
    PstMatDispatch pstMatDispatch = new PstMatDispatch();

    MatReceive matReceive = new MatReceive();
    Vector listMR = new Vector();

    double totalAllTarget = 0;
    double totalAllSource = 0;
    List<Double> totalTarget = new ArrayList<Double>();
    List<Double> totalSource = new ArrayList<Double>();
    double bobot = 0;

%>

<!--GET REQUEST-->
<%    
    String nomor = FRMQueryString.requestString(request, "FRM_FIELD_DISPATCH_CODE");
    long oidLocationFrom = FRMQueryString.requestLong(request, "FRM_FIELD_LOCATION_ID");
    long oidLocationTo = FRMQueryString.requestLong(request, "FRM_FIELD_DISPATCH_TO");
    //String date = FRMQueryString.requestString(request, "FRM_FIELD_DISPATCH_DATE");
    int status = FRMQueryString.requestInt(request, "FRM_FIELD_DF_STATUS");
    String remark = FRMQueryString.requestString(request, "FRM_FIELD_REMARK");

    int iCommand = FRMQueryString.requestCommand(request);

    boolean success = false;

    Vector listResult = new Vector();


%>
<%    int prevCommand = FRMQueryString.requestInt(request, "prev_command");
    int startItem = FRMQueryString.requestInt(request, "start_item");
    int cmdItem = FRMQueryString.requestInt(request, "command_item");
    int appCommand = FRMQueryString.requestInt(request, "approval_command");

    long oidDispatchMaterial = FRMQueryString.requestLong(request, "hidden_dispatch_id");
    long oidReceiveMaterial = FRMQueryString.requestLong(request, "hidden_receive_id");
    long oidMatDispatchReceiveItem = FRMQueryString.requestLong(request, "hidden_dispatch_receive_id");
    long timemls = System.currentTimeMillis();
    int typePrint = FRMQueryString.requestInt(request, "type_print_tranfer");

    String errMsg = "";
    int iErrCode = FRMMessage.ERR_NONE;

    ControlLine ctrLine = new ControlLine();
    CtrlMatDispatch ctrlMatDispatch = new CtrlMatDispatch(request);
    iErrCode = ctrlMatDispatch.actionHPP(iCommand, oidDispatchMaterial, userName, userId, request);
    FrmMatDispatch frmdf = ctrlMatDispatch.getForm();
    MatDispatch df = ctrlMatDispatch.getMatDispatch();

    oidDispatchMaterial = df.getOID();
    oidReceiveMaterial = PstMatReceive.getOidReceiveMaterial(oidDispatchMaterial);
    errMsg = ctrlMatDispatch.getMessage();

    if (df.getDispatchStatus() == I_DocStatus.DOCUMENT_STATUS_FINAL) {
        try {
            SessPosting sessPosting = new SessPosting();
            sessPosting.postedDispatchDoc(oidDispatchMaterial);
            sessPosting.postedReceiveDoc(oidReceiveMaterial, userName, userId);

            df.setDispatchStatus(I_DocStatus.DOCUMENT_STATUS_CLOSED);
            iCommand = Command.EDIT;

        } catch (Exception e) {
            iCommand = Command.EDIT;
        }
    }

    boolean privManageData = true;
    if (df.getDispatchStatus() != I_DocStatus.DOCUMENT_STATUS_DRAFT) {
        privManageData = false;
    }

    String limit = "LIMIT 0,10";
    //GET ALL DATA
    listResult = PstMatDispatchReceiveItem.list(0, 0, PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DISPATCH_MATERIAL_ID] + " = "
            + oidDispatchMaterial, null);
    MatDispatchReceiveItem mdri = new MatDispatchReceiveItem();

%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Tambah Dokumen Harga Pokok</title>

        <link rel="stylesheet" type="text/css" href="../../styles/bootstrap/css/bootstrap.min.css"/>
        <link rel="stylesheet" type="text/css" href="../../styles/bootstrap/css/bootstrap-theme.min.css"/>
        <link rel="stylesheet" type="text/css" href="../../styles/jquery-ui-1.12.1/jquery-ui.min.css"/>
        <!--link rel="stylesheet" type="text/css" href="../styles/jquery-ui-1.12.1/jquery-ui.theme.min.css"/-->
        <link rel="stylesheet" type="text/css" href="../../styles/dist/css/AdminLTE.css"/>
        <link rel="stylesheet" type="text/css" href="../../styles/dist/css/skins/_all-skins.css"/>
        <link rel="stylesheet" type="text/css" href="../../styles/font-awesome/font-awesome.css"/>
        <link rel="stylesheet" type="text/css" href="../../styles/iCheck/flat/blue.css"/>
        <link rel="stylesheet" type="text/css" href="../../styles/iCheck/all.css"/>
        <link rel="stylesheet" type="text/css" href="../../styles/select2/css/select2.min.css"/>
        <link rel="stylesheet" type="text/css" href="../../styles/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css">
        <link rel="stylesheet" type="text/css" href="../../styles/plugin/datatables/dataTables.bootstrap.css"/>
        <link rel="stylesheet" type="text/css" href="../../styles/bootstrap-notify.css"/>
        <link rel="stylesheet" type="text/css" href="../../styles/datepicker/datepicker3.css"/>
        <link rel="stylesheet" type="text/css" href="../../styles/timepicker/bootstrap-timepicker.css">

        <script type="text/javascript" src="../../styles/jquery-3.1.1.min.js"></script>
        <script type="text/javascript" src="../../styles/jquery-ui-1.12.1/jquery-ui.min.js"></script>
        <script type="text/javascript" src="../../styles/bootstrap/js/bootstrap.min.js"></script>  
        <script type="text/javascript" src="../../styles/dimata-app.js"></script>
        <script type="text/javascript" src="../../styles/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.min.js"></script>
        <script type="text/javascript" src="../../styles/JavaScript-autoComplete-master/auto-complete.min.js"></script>
        <script type="text/javascript" src="../../styles/iCheck/icheck.min.js"></script>
        <script type="text/javascript" src="../../styles/plugin/datatables/jquery.dataTables.js"></script>
        <script type="text/javascript" src="../../styles/plugin/datatables/dataTables.bootstrap.js"></script>
        <script type="text/javascript" src="../../styles/bootstrap-notify.js"></script>
        <script type="text/javascript" src="../../styles/select2/js/select2.min.js"></script>
        <script type="text/javascript" src="../../styles/datepicker/bootstrap-datepicker.js"/>></script>
    <script type="text/javascript" src="../../styles/timepicker/bootstrap-timepicker.js"></script>

    <style>

        body table {font-size: 14px}
        #tableSource {font-size: 12px}
        #tableSource th {color: white}
        #tabletarget th {color: white}
        #tableTarget {font-size: 12px}

        #tableResult td {border-color: white}
        #tableResult th {border-color: white}
        #tableResult {font-size: 12px}
        #tableResult th {color: white}

    </style>

</head>
<body bgcolor = "#ffffff">

    <section class="content-header">
        <h1>Dokumen HPP
            <small> Tambah</small></h1>
        <ol class="breadcrumb">
            <li>
                <a href="src_produksi.jsp"> Hpp
                    <i class="fa fa-suitcase">

                    </i>
                    <li class="active">Tambah</li>
                </a>
            </li>
        </ol>
    </section>
    <p></p>
    <div>

        <div class="row">
            <%                if (iCommand == Command.DELETE && iErrCode == 0) {
            %>
            <div class="row">
                <div class="col-sm-12">
                    <table class="table table-bordered table-striped table-info">
                        <thead>
                            <tr>
                                <td></td>
                                <td></td>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td style="background-color: lightcoral" id='nodata' colspan='2' class='text-center'> Dokumen produksi telah terhapus! </td>
                            </tr>
                        </tbody>
                    </table>
                    <section class="content">
                        <div class="col-sm-12">
                            &nbsp;<a href="add_harga_pokok_group.jsp" id="addProduksi" class="btn btn-success"><i class="fa fa-plus"></i>&nbsp;Tambah Dokumen Hpp</a>
                            &nbsp;<a href="src_harga_pokok_item.jsp" class="btn btn-primary"><i class="fa fa-search"></i>&nbsp;Cari Dokumen Hpp</a>
                        </div>
                    </section>
                </div>
            </div>
            <%
            } else {
            %>
            <div class="col-sm-12">
                <div class="box box-default">
                    <div>
                        <div class="box-body">
                            <form id="mainform" name="frm_matdispatchrecitem" class="form-horizontal">

                                <input type="hidden" name="command" value="<%= Command.SAVE%>">

                                <input type="hidden" name="prev_command" value="<%=prevCommand%>">

                                <input type="hidden" name="start_item" value="<%=startItem%>">

                                <input type="hidden" name="command_item" value="<%=cmdItem%>">

                                <input type="hidden" name="approval_command" value="<%=appCommand%>">

                                <input type="hidden" id ="indukId" name="hidden_dispatch_id" value="<%=oidDispatchMaterial%>">

                                <input type="hidden" name="hidden_receive_id"  value="<%=oidReceiveMaterial%>">

                                <input type="hidden" name="hidden_dispatch_item_id" value="">

                                <input type="hidden" name="hidden_receive_item_id" value="">

                                <input type ="hidden" name="hidden_df_rec_group_id" value="">

                                <input type ="hidden" name="type_doc" value="">

                                <input type="hidden" name="sess_language" value="<%=SESS_LANGUAGE%>">

                                <input type="hidden" name="<%=FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_CODE]%>" value="<%=df.getDispatchCode()%>">

                                <input type="hidden" name="<%=FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_LOCATION_TYPE]%>" value="4">

                                <!--Disabled Form if status is closed-->

                                <fieldset>   
                                    <div class="row">
                                        <div class="col-sm-12">
                                            <!--NOMOR-->
                                            <div class="col-sm-4">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-4">Nomor:</label>
                                                    <label style="text-align: left" id="nomor" class="control-label col-sm-8">
                                                        <%= df.getDispatchCode()%>
                                                    </label>
                                                </div>
                                            </div>

                                            <!--Tanggal-->
                                            <div class="col-sm-4">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-4">Tanggal:</label>
                                                    <div class="col-sm-8">
                                                        <input id="date" name="<%= frmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_DATE]%>" type="text" class="datetimepicker form-control" value="<%= Formater.formatDate(df.getDispatchDate(), "yyyy-MM-dd HH:mm:ss")%>">
                                                    </div>  
                                                </div>
                                            </div>

                                            <!--Status-->
                                            <div class="col-sm-4">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-4">Status:</label>
                                                    <div class="col-sm-8">
                                                        <%
                                                            Vector statusDocVal = new Vector();
                                                            Vector statusDocKey = new Vector();

                                                            statusDocVal.add(I_DocStatus.DOCUMENT_STATUS_DRAFT);
                                                            statusDocVal.add(I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED);

                                                            statusDocKey.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]);
                                                            statusDocKey.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED]);

                                                            if (!listResult.isEmpty()) {
                                                                statusDocVal.add(I_DocStatus.DOCUMENT_STATUS_FINAL);
                                                                statusDocKey.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);
                                                                
                                                                //hide
                                                                statusDocVal.add(I_DocStatus.DOCUMENT_STATUS_CLOSED);
                                                                statusDocKey.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_CLOSED]);
                                                            }
                                                        %>
                                                        <select class="form-control" id="selectStatus" name="<%=frmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_STATUS]%>" value="<%= matDispatch.getDispatchStatus()%>" >

                                                            <%
                                                                for (int i = 0; i < statusDocVal.size()-1; i++) {
                                                                    if (df.getDispatchStatus() == Integer.parseInt("" + statusDocVal.get(i))) {
                                                            %>
                                                            <option selected="" value="<%= statusDocVal.get(i)%>"><%= statusDocKey.get(i)%></option>
                                                            <%
                                                            } else {
                                                            %>
                                                            <option value="<%= statusDocVal.get(i)%>"><%= statusDocKey.get(i)%></option>

                                                            <%
                                                                    }
                                                                }
                                                            %>
                                                        </select>
                                                    </div>

                                                </div> <!--formgroup--> 
                                            </div>

                                        </div>
                                    </div>
                                    <%
                                        Vector listLocation = new Vector();
                                        listLocation = PstLocation.listHpp(0, 0, "pos_dispatch_material.LOCATION_TYPE = 3", null);
                                        Vector listLocation2 = new Vector();
                                        listLocation2 = PstLocation.listAll();
                                    %>


                                    <div class="row">
                                        <div class="col-sm-12">
                                            <!--Lokasi Asal-->

                                            <div class="col-sm-4">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-4">Lokasi&nbsp;Asal:</label>
                                                    <div class="col-sm-8">
                                                        <%
                                                            if (listLocation.size() > 0) {
                                                        %>
                                                        <select class="form-control" id="selectLocationAsal" name="<%=frmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_LOCATION_ID]%>">

                                                            <%

                                                                for (int i = 0; i < listLocation.size(); i++) {
                                                                    Location location = (Location) listLocation.get(i);
                                                                    if (df.getLocationId() == location.getOID()) {
                                                            %>
                                                            <option selected="" value="<%= location.getOID()%>"><%= location.getName()%></option>
                                                            <%
                                                            } else {
                                                            %>
                                                            <option value="<%= location.getOID()%>"><%= location.getName()%></option>

                                                            <%
                                                                        }
                                                                    }
                                                                }
                                                            %>
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>   

                                            <!--Lokasi Tujuan-->
                                            <div class="col-sm-4">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-4">Lokasi&nbsp;Tujuan:</label>
                                                    <div class="col-sm-8">
                                                        <%
                                                            if (listLocation2.size() > 0) {
                                                        %>
                                                        <select class="form-control" id="selectLocationTujuan" name="<%=frmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_TO]%>">

                                                            <%

                                                                for (int i = 0; i < listLocation2.size(); i++) {
                                                                    Location location = (Location) listLocation2.get(i);
                                                                    if (df.getDispatchTo() == location.getOID()) {
                                                            %>
                                                            <option selected="" value="<%= location.getOID()%>"><%= location.getName()%></option>
                                                            <%
                                                            } else {

                                                            %>
                                                            <option value="<%= location.getOID()%>"><%= location.getName()%></option>

                                                            <%
                                                                        }

                                                                    }
                                                                }
                                                            %>
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>

                                            <!--Keterangan-->
                                            <div class="col-sm-4">
                                                <div class="form-group">
                                                    <label class="control-label col-sm-4">Keterangan:</label>
                                                    <div class="col-sm-8">
                                                        <textarea id="remark" class="form-control" rows="3" name="<%= frmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_REMARK]%>"><%= df.getRemark()%></textarea>
                                                    </div>
                                                </div>
                                            </div>

                                        </div>
                                    </div>
                                </fieldset>
                                <div class="row">
                                    <div class="col-sm-12">
                                        <!--Button-->
                                        <div class="col-sm-12">
                                            <div class="form-group">
                                                <div class="col-sm-12">
                                                    <%
                                                        if (df.getDispatchStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED) {
                                                    %>
                                                    <button type="button" class="btn btn-primary" id="btnAdd" data-oid="0" data-for="searchProduksi"><i class="fa fa-save"></i>&nbsp;Simpan&nbsp;Dokumen&nbsp;Hpp</button>
                                                    &nbsp;
                                                    <%
                                                        Vector listInduk = PstMatDispatch.list(0, 0, PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID] + " = "
                                                                + oidDispatchMaterial, null);
                                                        if (!listInduk.isEmpty() && df.getDispatchStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) {
                                                    %>
                                                    <button type="button" class="btn btn-danger" id="btnDelete" data-oid="<%= oidDispatchMaterial%>"><i class="fa fa-trash-o"></i>&nbsp;Hapus&nbsp;Dokumen&nbsp;Hpp</button>
                                                    &nbsp;
                                                    <%
                                                        }
                                                    %>
                                                    <%
                                                        }
                                                    %>

                                                    <a href="src_harga_pokok_item.jsp" class="btn btn-primary" id="btnbtn" data-oid="0" data-for="addProduksi"><i class="fa fa-chevron-left"></i>&nbsp;Kembali&nbsp;ke&nbsp;Pencarian&nbsp;Hpp</a>
                                                    &nbsp;<a href="add_harga_pokok_group.jsp" class="btn btn-default" id="addInduk"><i class="fa fa-plus-circle">&nbsp;Tambah Dokumen Hpp</i></a> 
                                                    &nbsp;<button type="button" id="logHistory" class="btn btn-default"><i class="fa fa-archive">&nbsp;Lihat Log Dokumen</i></button>
                                                </div>
                                            </div>    
                                        </div>    
                                    </div>    
                                </div>

                            </form>  
                        </div>
                    </div><!--box-->
                </div><!--COLSM12-->
            </div><!--COLSM12-->
            <%
                }
            %>


            <!--TABLE LIST-->
            <div class="col-sm-12">
                <div class="box-body showList-body">

                    <div class='row'>
                        <div class='col-sm-12'>
                            <%
                                if (!listResult.isEmpty()) {
                            %>

                            <section>
                                <div class="row">
                                    <div class="col-sm-12">
                                        <div class="box box-primary">
                                            <div class="box-header with-border">
                                                <h3 class="box-title">List Items</h3>
                                            </div>
                                            <%
                                                if (df.getDispatchStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) {
                                            %>
                                            <div>
                                                <button type="button" id="addGroup" data-oidgroup="0" class="btn btn-success"><i class="fa fa-pencil"></i>&nbsp;Edit Item Group</button>
                                            </div>
                                            <br>
                                            <%
                                                }
                                            %>
                                            <table class="table table-bordered table-info">
                                                <thead class="thead-inverse" style="background-color: lightblue">
                                                    <tr>
                                                        <th>Group</th>
                                                        <th>Bahan Baku</th>
                                                        <th>Penjualan</th>

                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <%
                                                        String order = "DFRI." + PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DF_REC_GROUP_ID];
                                                        Vector listMatDispatchReceiveItem = PstMatDispatchReceiveItem.listGroupHpp(startItem, 10, oidDispatchMaterial, order);
                                                        Vector vTot = new Vector();
                                                        if (!listMatDispatchReceiveItem.isEmpty()){
                                                        int sizeList = listMatDispatchReceiveItem.size();
                                                        vTot = PstMatDispatchReceiveItem.listGroup(startItem, 0, oidDispatchMaterial, null);
                                                        int sizeTotal = vTot.size();
                                                        int sizeAwal = 0;
                                                        if (!listMatDispatchReceiveItem.isEmpty()) {
                                                            sizeAwal = 1;
                                                        } else {
                                                            sizeAwal = 0;
                                                        }
                                                        for (int i = 0; i < listMatDispatchReceiveItem.size(); i++) {
                                                            totalAllTarget = 0;
                                                            totalAllSource = 0;
                                                            MatDispatchReceiveItem dfRecItem1 = (MatDispatchReceiveItem) listMatDispatchReceiveItem.get(i);

                                                    %>
                                                    <tr>
                                                        <td class="text-center" style="width: 3%"><%=i + 1%>
                                                        </td>
                                                        <td class="text-center">

                                                            <table id="tableTarget" class="table table-bordered table-striped table-info table-responsive">
                                                                <thead class="thead-inverse" style="background-color: lightseagreen">
                                                                    <tr>
                                                                        <th>SKU</th>
                                                                        <th>Nama Barang</th>
                                                                        <th>Unit</th>
                                                                        <th>Qty</th>
                                                                        <th>Stok</th>
                                                                        <th>Total</th>
                                                                    </tr>
                                                                </thead>
                                                                <tbody>
                                                                    <%
                                                                        String order1 = "DFRI." + PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DF_REC_GROUP_ID];
                                                                        Vector listItem = PstMatDispatchReceiveItem.list(0, 0, dfRecItem1.getDfRecGroupId(), order1);
                                                                        for (int j = 0; j < listItem.size(); j++) {
                                                                            MatDispatchReceiveItem dfRecItemAll = (MatDispatchReceiveItem) listItem.get(j);
                                                                            if (dfRecItemAll.getSourceItem().getMaterialSource().getOID() != 0) {
                                                                    %>
                                                                    <tr>
                                                                        <td><%= dfRecItemAll.getSourceItem().getMaterialSource().getSku()%></td>
                                                                        <td><%= dfRecItemAll.getSourceItem().getMaterialSource().getName()%></td>
                                                                        <td><%= dfRecItemAll.getSourceItem().getUnitSource().getCode()%></td>
                                                                        <td><%= dfRecItemAll.getSourceItem().getQty()%></td>
                                                                        <td><%= dfRecItemAll.getSourceItem().getHpp()%></td>
                                                                        <td><%= dfRecItemAll.getSourceItem().getHppTotal()%></td>
                                                                    </tr>
                                                                    <%
                                                                            }
                                                                            totalAllSource += dfRecItemAll.getSourceItem().getHppTotal();
                                                                        }
                                                                        totalSource.add(totalAllSource);
                                                                    %>

                                                                </tbody>
                                                            </table>
                                                        </td>
                                                        <td class="text-center">

                                                            <table id="tableSource" class="table table-bordered table-striped table-info table-responsive">
                                                                <thead class="thead-inverse" style="background-color: lightseagreen">
                                                                    <tr>
                                                                        <th>SKU</th>
                                                                        <th>Nama Barang</th>
                                                                        <th>Unit</th>
                                                                        <th>Qty</th>
                                                                        <th>Stok</th>
                                                                        <th>Total</th>
                                                                    </tr>
                                                                </thead>
                                                                <tbody>
                                                                    <%
                                                                        String order2 = "DFRI." + PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DF_REC_GROUP_ID];
                                                                        Vector listItem1 = PstMatDispatchReceiveItem.list(0, 0, dfRecItem1.getDfRecGroupId(), order2);
                                                                        if (!listItem1.isEmpty()) {
                                                                            for (int k = 0; k < listItem1.size(); k++) {
                                                                                MatDispatchReceiveItem dfRecItemAll1 = (MatDispatchReceiveItem) listItem1.get(k);
                                                                                if (dfRecItemAll1.getTargetItem().getMaterialTarget().getOID() != 0) {
                                                                    %>
                                                                    <tr>
                                                                        <td><%= dfRecItemAll1.getTargetItem().getMaterialTarget().getSku()%></td>
                                                                        <td><%= dfRecItemAll1.getTargetItem().getMaterialTarget().getName()%></td>
                                                                        <td><%= dfRecItemAll1.getTargetItem().getUnitTarget().getCode()%></td>
                                                                        <td><%= dfRecItemAll1.getTargetItem().getQty()%></td>
                                                                        <td><%= dfRecItemAll1.getTargetItem().getCost()%></td>
                                                                        <td><%= dfRecItemAll1.getTargetItem().getTotal()%></td>
                                                                    </tr>
                                                                    <%
                                                                                }
                                                                                totalAllTarget += dfRecItemAll1.getTargetItem().getTotal();
                                                                            }
                                                                            totalTarget.add(totalAllTarget);
                                                                        }
                                                                    %>

                                                                </tbody>
                                                            </table>
                                                        </td>

                                                    </tr>
                                                    <%
                                                        }
                                                    %>
                                                </tbody>
                                            </table>
                                        </div>
                                        &nbsp;<p>List: <%= sizeAwal%>-<%= sizeList%>, Total: <%= sizeTotal%></p>
                                    </div>
                                    
                                </div>

                            </section>

                             <section class="content">
                    <!--LIST HPP-->
                    <div class="box box-primary">
                        <div class="box-header with-border">
                            <h3 class="box-title">List Harga Pokok</h3>
                        </div>
                        <table id="mainTable2" class="table table-bordered table-info">
                            <thead class="thead-inverse" style="background-color: lightblue">
                                <tr>
                                    <th>Group</th>
                                    <th>Harga Pokok</th>

                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    String order2 = "DFRI." + PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DF_REC_GROUP_ID];
                                    Vector listMatDispatchReceiveItem2 = PstMatDispatchReceiveItem.listGroupHpp(startItem, 10, oidDispatchMaterial, order2);
                                    for (int i = 0; i < listMatDispatchReceiveItem2.size(); i++) {
                                        MatDispatchReceiveItem dfRecItem1 = (MatDispatchReceiveItem) listMatDispatchReceiveItem2.get(i);

                                %>

                                <tr>
                                    <td class="text-center" style="width: 2%"><%= i + 1%></td>

                                    <!--LIST HPP-->
                                    <td style="width: 98%"> 
                                        <table id="tableResult" class="table table-bordered table-striped table-info table-responsive">
                                            <thead class="thead-inverse" style="background-color: lightseagreen">
                                                <tr>
                                                    <th>Item</th>
                                                    <th>Bobot</th>
                                                    <th>Total</th>
                                                    <th>HPP</th>
                                                    <th>Unit</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <%
                                                    String order3 = "DFRI." + PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DF_REC_GROUP_ID];
                                                    Vector listItem1 = PstMatDispatchReceiveItem.list(0, 0, dfRecItem1.getDfRecGroupId(), order3);
                                                    if (!listItem1.isEmpty()) {
                                                        for (int k = 0; k < listItem1.size(); k++) {
                                                            MatDispatchReceiveItem dfRecItemAll1 = (MatDispatchReceiveItem) listItem1.get(k);

                                                            bobot = dfRecItemAll1.getTargetItem().getTotal() / totalTarget.get(i);
                                                            if (dfRecItemAll1.getTargetItem().getMaterialTarget().getOID() != 0) {

                                                %>
                                                <tr>
                                                    <td><%= dfRecItemAll1.getTargetItem().getMaterialTarget().getName()%></td>
                                                    <td><%= String.format("%.2f", bobot * 100)%>%</td>
                                                    <td><%= String.format("%.2f", bobot * totalSource.get(i))%></td>
                                                    <td><%= String.format("%.2f", (bobot * totalSource.get(i)) / dfRecItemAll1.getTargetItem().getQty())%></td>
                                                    <td><%= dfRecItemAll1.getTargetItem().getUnitTarget().getCode()%></td>
                                                </tr>
                                                <%
                                                            }
                                                        }
                                                    }
                                                %>

                                            </tbody>
                                        </table>
                                    </td>

                                </tr>
                                <%
                                    }
                                %>
                            </tbody>
                        </table>
                    </div>



                    <!--PEMBIAYAAN-->
                    <div class="box box-danger">
                        <div class="box-header with-border">
                            <h3 class="box-title">List Pembiayaan</h3>
                        </div>
                        <table id="tableSource" class="table table-bordered table-striped table-info table-responsive">
                            <thead class="thead-inverse" style="background-color: #D60000">
                                <tr>
                                    <th>No.</th>
                                    <th>Nomor</th>
                                    <!--                                                    <th>Tanggal</th>
                                                                                            <th>Consume</th>
                                                                                            <th>Asal</th>
                                                                                            <th>Tujuan</th>-->
                                    <th>Tipe Biaya</th>
                                    <th>Status</th>
                                    <th>keterangan</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    Vector vCosting = new Vector();
                                    MatCosting matCost = new MatCosting();

                                    vCosting = PstMatCosting.list(0, 0, PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_TYPE]+" = 4 AND "
                                        +PstMatCosting.fieldNames[PstMatCosting.FLD_DOCUMENT_ID]+" = "+oidDispatchMaterial , null);

                                    for (int z = 0; z < vCosting.size(); z++) {
                                        matCost = (MatCosting) vCosting.get(z);

                                        //size item
                                        Vector vCostitemCheck = PstMatCostingItem.list(0, 0, PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_PARENT_ID] + " = 0 "
                                                + " AND " + PstMatCostingItem.fieldNames[PstMatCostingItem.FLD_COSTING_MATERIAL_ID] + " = " + matCost.getOID(), null);

                                        //Tipe Biaya
                                        Vector vType = PstCosting.list(0, 0, PstCosting.fieldNames[PstCosting.FLD_COSTING_ID] + " = "
                                                + matCost.getCostingId(), null);
                                        Costing costing = (Costing) vType.get(0);

                                        //status conversion
                                        int statusConv = 0;
                                        if (matCost.getCostingStatus() == 0) {
                                            statusConv = 0;
                                        } else if (matCost.getCostingStatus() == 1) {
                                            statusConv = 1;
                                        } else {
                                            statusConv = 3;
                                        }


                                %>
                                <%                                                        
                                    Vector statusDocVal3 = new Vector();
                                    Vector statusDocKey3 = new Vector();

                                    statusDocVal3.add(I_DocStatus.DOCUMENT_STATUS_DRAFT);
                                    statusDocVal3.add(I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED);
                                    statusDocVal3.add(I_DocStatus.DOCUMENT_STATUS_FINAL);
                                    statusDocVal3.add(I_DocStatus.DOCUMENT_STATUS_CLOSED);

                                    statusDocKey3.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]);
                                    statusDocKey3.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED]);
                                    statusDocKey3.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);
                                    statusDocKey3.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_CLOSED]);

                                %> 

                                <tr>

                                    <td><%= z + 1%></td>
                                    <td><%= matCost.getCostingCode()%></td>
                                    <td><%= costing.getName()%></td>
                                    <td><%= statusDocKey3.get(statusConv)%></td>
                                    <td><%= matCost.getRemark()%></td>

                                </tr>
                                <%
                                    }

                                %>


                            </tbody>
                        </table>
                    </div>

                </section>
                <%
                    }
                %>

                            <%
                            } else if (listResult.isEmpty() && iCommand != Command.DELETE) {
                            %>
                            <div class="row">
                                <div class="col-sm-12">
                                    <table class="table table-bordered table-striped table-info">
                                        <thead>
                                            <tr>
                                                <td></td>
                                                <td></td>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr>
                                                <td style="background-color: lightgray" id='nodata' colspan='2' class='text-center'> Data tidak tersedia! </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                            <%
                                }
                            %>

                        </div>
                    </div>
                </div>

                
            </div>




        </div> <!--ROW-->
    </div>

</div>
<script>
    $(document).ready(function () {
        //INITIAL
        var command = $("#command").val();
        var approot = "<%= approot%>";
        var oid = null;
        var dataFor = null;

        //FUNCTION VARIABLE
        var onDone = null;
        var onSuccess = null;
        var dataSend = null;


        //ENTER KEY
        function keyEnter(element1, element2) {
            $(element1).keypress(function (e) {
                if (e.which == 13) {
                    $(element2).focus();
                    return false;
                }

            });
        }
        keyEnter('#date', '#selectStatus');
        keyEnter('#selectStatus', '#selectLocationAsal');
        keyEnter('#selectLocationAsal', '#selectLocationTujuan');
        keyEnter('#selectLocationTujuan', '#remark');
        keyEnter('#remark', '#btnAdd');

        //MODAL
        var modalSetting = function (elementId, backdrop, keyboard, show) {
            $(elementId).modal({
                backdrop: backdrop,
                keyboard: keyboard,
                show: show
            });
        };
        modalSetting("#myModalDeleteInduk", "static", false, false);

        //Get DATAFUNCTION
        var getDataFunction = function (onDone, onSuccess, approot, command, dataSend, servletName, dataAppendTo, notification, dataType) {
            /*
             * getDataFor	: # FOR PROCCESS FILTER
             * onDone	: # ON DONE FUNCTION,
             * onSuccess	: # ON ON SUCCESS FUNCTION,
             * approot	: # APPLICATION ROOT,
             * dataSend	: # DATA TO SEND TO THE SERVLET,
             * servletName  : # SERVLET'S NAME,
             * dataType	: # Data Type (JSON, HTML, TEXT)
             */
            $(this).getData({
                onDone: function (data) {
                    onDone(data);
                },
                onSuccess: function (data) {
                    onSuccess(data);
                },
                approot: approot,
                dataSend: dataSend,
                servletName: servletName,
                dataAppendTo: dataAppendTo,
                notification: notification,
                ajaxDataType: dataType
            });
        }

        //CLICK BUTTON
        function checkStatus() {

            dataSend = {
                "FRM_FIELD_DATA_FOR": "checkStatus",
                "FRM_FIELD_OID": "<%= oidDispatchMaterial%>",
                "command": "<%= Command.NONE%>"
            };
            //alert(JSON.stringify(dataSend));
            onSuccess = function (data) {

            };
            onDone = function (data) {
                var result_status = data.FRM_FIELD_STATUS;
                if (result_status == 1) {
                    alert("Pembiayaan harus di closed terlebih dahulu.");
                    document.frm_matdispatchrecitem.command.value = "<%=Command.EDIT%>";
                    document.frm_matdispatchrecitem.action = "harga_pokok_item.jsp";
                    document.frm_matdispatchrecitem.submit();
                } else {
                    document.frm_matdispatchrecitem.submit();
                }
            };
            getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxBahanBaku", null, true, "json");
            return false;
        }


        //HIDE DELETE 
        $("#btnDelete").click(function () {
            var oid = $(this).data("oid");
            $("#indukId").val(oid);

            $("#myModalDeleteInduk").modal("show");
        });

        $("#btnDeleteIndukModal").click(function () {
            document.frm_matdispatchrecitem.command.value = "<%=Command.DELETE%>";
            document.frm_matdispatchrecitem.action = "add_harga_pokok_group.jsp";
            document.frm_matdispatchrecitem.submit();
        });

        //DATEPICKER
        $(".datepicker").datepicker({
            format: "yyyy-mm-dd",
            autoclose: true
        });
        //Timepicker
        $('.datetimepicker').datetimepicker({
            format: "yyyy-mm-dd hh:ii:ss",
            autoclose: true
        });

        function addItem() {

    <%

        if (df.getDispatchStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) {

    %>

            document.frm_matdispatchrecitem.command.value = "<%=Command.ADD%>";

            //document.frm_matdispatchrecitem.action="df_unit_wh_material_item_2_1.jsp";
            document.frm_matdispatchrecitem.action = "harga_pokok_item.jsp";

            if (compareDateForAdd() == true)
                document.frm_matdispatchrecitem.submit();

    <%

    } else {

    %>

            alert("Document has been posted !!!");

    <%        }

    %>

        }


    <%                if (df.getDispatchStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT && iCommand == Command.SAVE && iErrCode == 0) {
    %>
        addItem();
    <%
        }
    %>


        //Add new Group
        $("#addGroup").click(function () {
            var oidGroup = $(this).data("oidgroup");

            //alert("Silahkan mengisi form yang telah disediakan diatas untuk menambah item pada group baru.");
            $("#groupId").val(oidGroup);
            document.frm_matdispatchrecitem.command.value = "<%=Command.ADD%>";
            document.frm_matdispatchrecitem.action = "harga_pokok_item.jsp";
            document.frm_matdispatchrecitem.submit();
        });

    <%
        if (df.getDispatchStatus() == I_DocStatus.DOCUMENT_STATUS_CLOSED) {
    %>
        $("fieldset").attr("disabled", true);

    <%
        }
    %>


        //Save, alert for status closed
        $("#btnAdd").click(function () {

            var stts = $("#selectStatus").val();

            if (stts === "<%=I_DocStatus.DOCUMENT_STATUS_FINAL%>") {
                var agree = confirm("Apakah Anda yakin akan melakukan posting dokumen?");
                if (agree) {
                    checkStatus();
                }

            } else if (stts === "<%=I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED%>") {
                document.frm_matdispatchrecitem.action = "add_harga_pokok_group.jsp";
                document.frm_matdispatchrecitem.submit();

            } else {
                document.frm_matdispatchrecitem.submit();

            }


        });

        //LOG HISTORY
        $("#logHistory").click(function () {
            var strvalue = "../../main/historypo.jsp?command=<%=Command.FIRST%>" +
                    "&oidDocHistory=<%=oidDispatchMaterial%>";
            window.open(strvalue, "material", "height=600,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");

        });


    });
</script>

<!-- Modal dialog for delete group -->
<div class="modal fade" id="myModalDeleteInduk" role="dialog">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">Delete Document</h4>
            </div>
            <div class="modal-body">
                <p>Apakah Anda yakin ingin menghapus dokumen produksi ini?</p>
            </div>
            <div class="modal-footer">
                <button id="btnDeleteIndukModal" type="button" class="btn btn-primary" data-dismiss="modal">Yes</button>
                <button type="button" class="btn btn-danger" data-dismiss="modal">No</button>
            </div>
        </div>
    </div>
</div>

</body>
</html>


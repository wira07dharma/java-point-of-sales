<%-- 
    Document   : add_chain
    Created on : Jul 9, 2019, 9:28:50 PM
    Author     : IanRizky
--%>

<%@page import="com.dimata.posbo.entity.masterdata.ChainMainMaterial"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterial"%>
<%@page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstChainAddCost"%>
<%@page import="com.dimata.posbo.entity.masterdata.ChainAddCost"%>
<%@page import="com.dimata.posbo.form.masterdata.CtrlChainAddCost"%>
<%@page import="com.dimata.posbo.form.masterdata.CtrlChainMainMaterial"%>
<%@page import="com.dimata.posbo.form.masterdata.FrmChainAddCost"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstChainMainMaterial"%>
<%@page import="com.dimata.posbo.form.masterdata.FrmChainMainMaterial"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstChainPeriod"%>
<%@page import="com.dimata.posbo.entity.masterdata.ChainPeriod"%>
<%@page import="com.dimata.posbo.form.masterdata.CtrlChainPeriod"%>
<%@page import="com.dimata.posbo.form.masterdata.FrmChainPeriod"%>
<%@page import="com.dimata.posbo.entity.masterdata.ChainMain"%>
<%@page import="com.dimata.posbo.form.masterdata.CtrlChainMain"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.posbo.form.masterdata.FrmChainMain"%>
<%@page import="com.dimata.common.entity.location.Location"%>
<%@page import="com.dimata.common.entity.location.PstLocation"%>
<%@page import="java.util.Vector"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../../main/javainit.jsp" %>
<%//
    int iCommand = FRMQueryString.requestCommand(request);
    long oidChain = FRMQueryString.requestLong(request, "hidden_chain_id");
    //value for period
    int iCommandPeriod = FRMQueryString.requestInt(request, "command_period");
    long oidChainPeriod = FRMQueryString.requestLong(request, "hidden_period_id");
    //value for product
    int iCommandProduct = FRMQueryString.requestInt(request, "command_product");
    long oidItemProduct = FRMQueryString.requestLong(request, "hidden_item_product_id");
    //value for cost
    int iCommandCost = FRMQueryString.requestInt(request, "command_cost");
    long oidItemCost = FRMQueryString.requestLong(request, "hidden_item_cost_id");
    
    //==================== ACTION FOR MAIN ====================
    CtrlChainMain ctrlChainMain = new CtrlChainMain(request);
    int iErrorMain = ctrlChainMain.action(iCommand, oidChain);
    String messageMain = ctrlChainMain.getMessage();
    ChainMain chainMain = ctrlChainMain.getChainMain();
    
    //==================== ACTION FOR PERIOD ====================
    ChainPeriod chainPeriod = new ChainPeriod();
    int iErrorPeriod = 0;
    String messagePeriod = "";
    if (iCommandPeriod == Command.SAVE) {
        CtrlChainPeriod ctrlChainPeriod = new CtrlChainPeriod(request);
        iErrorPeriod = ctrlChainPeriod.action(iCommandPeriod, oidChainPeriod);
        messagePeriod = ctrlChainPeriod.getMessage();
        chainPeriod = ctrlChainPeriod.getChainPeriod();
    }

    //==================== ACTION FOR PRODUCT ====================
    int iErrorProduct = 0;
    String messageProduct = "";
    if (iCommandProduct == Command.SAVE || iCommandProduct == Command.DELETE) {
        CtrlChainMainMaterial ctrlChainMainMaterial = new CtrlChainMainMaterial(request);
        iErrorProduct = ctrlChainMainMaterial.action(iCommandProduct, oidItemProduct);
        messageProduct = ctrlChainMainMaterial.getMessage();
    }

    //==================== ACTION FOR COST ====================
    int iErrorCost = 0;
    String messageCost = "";
    if (iCommandCost == Command.SAVE || iCommandCost == Command.DELETE) {
        CtrlChainAddCost ctrlChainAddCost = new CtrlChainAddCost(request);
        iErrorCost = ctrlChainAddCost.action(iCommandCost, oidItemCost);
        ChainAddCost cost = ctrlChainAddCost.getChainAddCost();
        CtrlChainMainMaterial ctrlChainMainMaterial = new CtrlChainMainMaterial(request);
        ctrlChainMainMaterial.updatePerhitunganPeriod(cost.getChainPeriodId());
        messageCost = ctrlChainAddCost.getMessage();
    }

    String selected = "";

%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Production Template</title>
        <link rel="stylesheet" type="text/css" href="../../styles/bootstrap/css/bootstrap.min.css"/>
        <link rel="stylesheet" type="text/css" href="../../styles/dist/css/AdminLTE.css"/>
        <link rel="stylesheet" type="text/css" href="../../styles/font-awesome/font-awesome.css"/>
        <link rel="stylesheet" type="text/css" href="../../styles/select2/css/select2.min.css" />
        <link rel="stylesheet" media="screen" href="../../styles/datepicker/bootstrap-datetimepicker.min.css"/>
        <link rel="stylesheet" type="text/css" href="../../styles/plugin/datatables/dataTables.bootstrap.css"/>
        <style>

            body {background-color: #DDD}
            .table {font-size: 12px;}
            .table th {text-align: center;}
            .table-condensed {font-size: 14px}
            .tabel_product th {background-color: #f39c12; color: white;}
            .tabel_cost th {background-color: #00a65a; color: white;}
            .tabel_perhitungan_hpp th {background-color: #3c8dbc; color: white;}

            .modal-header {padding: 10px 20px; border-color: lightgray}
            .modal-footer {padding: 10px 20px; margin: 0px; border-color: lightgray}
            .box .box-header, .box .box-footer {border-color: lightgray}

            #tableItemElement th {padding: 8px; font-size: 12px; background-color: lightgray}
            #tableItemElement td {padding: 8px; font-size: 12px}
            
            .tabel_product, .tabel_cost {margin: 0px}
            
            .tabel_product tbody tr th, .tabel_product tbody tr td, .tabel_cost tbody tr th, .tabel_cost tbody tr td {padding: 5px}
            
            form {margin: 0px}
            
            .timeline:before {background: #CCC}
            
        </style>
    </head>
    <body>
        <div class="col-sm-12">
            <h3>Template Produksi</h3>
            <div class="box box-warning">
                <div class="box-header with-border">
                    <h3 class="box-title">Data Main</h3>
                </div>
                <div class="box-body">
                    <form id="formChain" class="form-horizontal">
                        <input type="hidden" id="command" name="command" value="<%=iCommand%>">
                        <input type="hidden" name="hidden_chain_id" value="<%=chainMain.getOID()%>">
                        <input type="hidden" name="<%=FrmChainMain.fieldNames[FrmChainMain.FRM_FIELD_CHAIN_DATE]%>" value="<%=Formater.formatDate(chainMain.getChainDate(), "yyyy-MM-dd")%>">
                        
                        <div class="form-group">
                            <label class="col-sm-1 control-label">Title</label>
                            <div class="col-sm-3">
                                <input type="text" placeholder="Chain Title" class="form-control input-sm" name="<%=FrmChainMain.fieldNames[FrmChainMain.FRM_FIELD_CHAIN_TITLE]%>" value="<%=chainMain.getChainTitle() %>">
                            </div>
                        
                            <label class="col-sm-1 control-label">Lokasi</label>
                            <div class="col-sm-3">
                                <select style="width: 100%" class="form-control input-sm" name="<%= FrmChainMain.fieldNames[FrmChainMain.FRM_FIELD_CHAIN_LOCATION]%>">
                                    <%
                                        Vector listLokasi = PstLocation.list(0, 0, "", "" + PstLocation.fieldNames[PstLocation.FLD_NAME]);
                                        for (int i = 0; i < listLokasi.size(); i++) {
                                            selected = "";
                                            Location l = (Location) listLokasi.get(i);
                                            if (l.getOID() == chainMain.getChainLocation()) {
                                                selected = "selected";
                                            }
                                    %>
                                    <option <%=selected%> value="<%=l.getOID()%>"><%=l.getName()%></option>
                                    <%
                                        }
                                    %>
                                </select>
                            </div>  
                        
                            <label class="col-sm-1 control-label">Keterangan</label>
                            <div class="col-sm-3">
                                <textarea class="form-control" name="<%=FrmChainMain.fieldNames[FrmChainMain.FRM_FIELD_CHAIN_NOTE]%>"><%=chainMain.getChainNote() %></textarea>
                            </div>
                        </div>

                    </form>
                </div>
                <div class="box-footer">
                    <a href="list_production_chain.jsp" id="btnBack" class="btn btn-sm btn-primary"><i class="fa fa-undo"></i>&nbsp; Kembali</a>
                    <button type="button" id="btnSaveMain" class="btn btn-sm btn-success"><i class="fa fa-save"></i>&nbsp; Simpan</button>
                    <%
                        if (chainMain.getOID() > 0) {
                    %><button type="button" id="btnAddPeriod" class="btn btn-sm btn-primary"><i class="fa fa-plus"></i>&nbsp; Tambah Periode</button><%
                        }
                    %>
                </div>
            </div>
        </div>

        <section class="content">
            <div class="row">
                <div class="col-md-12">
                    <%
                        Vector listChainPeriod = PstChainPeriod.list(0, 0,
                                PstChainPeriod.fieldNames[PstChainPeriod.FLD_CHAIN_MAIN_ID] + "=" + chainMain.getOID(),
                                PstChainPeriod.fieldNames[PstChainPeriod.FLD_INDEX]);
                        long prevPeriod = 0;
                        for (int i = 0; i < listChainPeriod.size(); i++) {
                            ChainPeriod period = (ChainPeriod) listChainPeriod.get(i);
                            long rangeMilliseconds = period.getDuration();
                            long days = 0;
                            long hours = 0;
                            long minutes = 0;
                            long seconds = 0;
                            
                            try {
                                days = rangeMilliseconds / (24 * 60 * 60 * 1000);
                                long modDays = rangeMilliseconds % (24 * 60 * 60 * 1000);
                                if (modDays > 0) {
                                    hours = modDays / (60 * 60 * 1000);
                                    long modHours = modDays % (60 * 60 * 1000);
                                    if (modHours > 0) {
                                        minutes = modHours / (60 * 1000);
                                        long modMinutes = modHours % (60 * 1000);
                                        if (modMinutes > 0) {
                                            seconds = modMinutes / (1000);
                                        }
                                    }
                                }
                            } catch (Exception e) {
                            }
                            
                            String bindDataPeriod = "data-days='" + days + "'"
                                    + " data-hours='" + hours + "'"
                                    + " data-minutes='" + minutes + "'"
                                    + " data-seconds='" + seconds + "'";
                    %>
                    <ul class="timeline">
                        <li class="time-label">
                            <span class="bg-red btn btnEditPeriod" <%=bindDataPeriod%> data-period_id="<%=period.getOID()%>" data-period_title="<%=period.getTitle()%>" data-period_index="<%=period.getIndex() %>"><%=period.getIndex() %> - <%=period.getTitle()%></span>
                        </li>
                        
                        <li>
                            <i class="fa fa-info-circle bg-aqua"></i>
                            <div class="timeline-item">
                                <h3 class="timeline-header text-bold">Cost</h3>
                                <div class="timeline-body">
                                    <form id="formCost_<%=period.getOID()%>">
                                        <input type="hidden" id="commandCost_<%=period.getOID()%>" name="command_cost" value="">
                                        <input type="hidden" id="command" name="command" value="<%=Command.EDIT%>">
                                        <input type="hidden" name="hidden_chain_id" value="<%=chainMain.getOID()%>">
                                        <table class="table table-bordered tabel_cost">
                                            <tr>
                                                <th style="width: 1%">No.</th>
                                                <th>SKU</th>
                                                <th>Material</th>
                                                <th>Type</th>
                                                <th>Stock Qty</th>
                                                <th>Stock Value</th>
                                                <th>Sub Total Cost</th>
                                            </tr>
                                            <%
                                                int noCost = 0;
                                                double grandTotalCost = 0;
                                                Vector listAddCost = PstChainAddCost.list(0, 0, PstChainAddCost.fieldNames[PstChainAddCost.FLD_CHAIN_PERIOD_ID] + "=" + period.getOID(), "");
                                                for (int xx = 0; xx < listAddCost.size(); xx++) {
                                                    noCost++;
                                                    ChainAddCost chainAddCost = (ChainAddCost) listAddCost.get(xx);
                                                    Material material = new Material();
                                                    try {
                                                        material = PstMaterial.fetchExc(chainAddCost.getMaterialId());
                                                    } catch (Exception e) {
                                                        System.out.println(e.getMessage());
                                                    }
                                                    double qtyStock = chainAddCost.getStockQty();
                                                    double stockValue = chainAddCost.getStockValue();
                                                    double subTotalCost = qtyStock * stockValue;
                                                    grandTotalCost += subTotalCost;
                                            %>
                                            <tr>
                                                <td><%=noCost%></td>
                                                <%if(chainAddCost.getCostType() == PstChainAddCost.COST_TYPE_REFERENCED) {%>
                                                <td><%=material.getSku()%></td>
                                                <%}else{%>
                                                <td><a class="btnAppendCost" style="cursor: pointer" data-period_id="<%=chainAddCost.getChainPeriodId() %>" data-cost_id="<%=chainAddCost.getOID() %>"><%=material.getSku()%></a></td>
                                                <%}%>
                                                <td><%=material.getName()%></td>
                                                <td><%=PstChainAddCost.COST_TYPE_TITLE[chainAddCost.getCostType()] %></td>
                                                <td class="text-right"><%=String.format("%,.0f", qtyStock)%></td>
                                                <td class="text-right"><%=String.format("%,.2f", stockValue)%></td>
                                                <td class="text-right"><%=String.format("%,.2f", subTotalCost)%></td>
                                            </tr>
                                            <%
                                                }
                                            %>
                                            
                                            <%
                                                if (listAddCost.isEmpty()) {
                                            %>
                                            <tr class="label-default"><td colspan="7" class="text-center">Tidak ada data</td></tr>
                                            <%
                                                } else {
                                            %>
                                            <tr style="background-color: #d1ffd1">
                                                <td class="text-bold" colspan="6">GRAND TOTAL</td>
                                                <td class="text-right text-bold"><%=String.format("%,.2f", grandTotalCost)%></td>
                                            </tr>
                                            <%
                                                }
                                            %>
                                            <tr id="cost_<%=period.getOID()%>"></tr>
                                        </table>
                                    </form>
                                </div>
                                <div class="timeline-footer" id="footerCost_<%=period.getOID()%>">
                                    <a class="btn btn-primary btn-sm btnAppendCost" data-period_id="<%=period.getOID()%>" data-prevoid="<%=prevPeriod%>">Tambah Cost</a>
                                    <a class="btn btn-warning btn-sm btnCancelCost hidden" data-period_id="<%=period.getOID()%>">Batal</a>
                                    <a class="btn btn-success btn-sm btnSaveCost hidden">Simpan Cost</a>
                                </div>
                            </div>
                        </li>
                        
                        <li>
                            <i class="fa fa-info-circle bg-aqua"></i>
                            <div class="timeline-item">
                                <h3 class="timeline-header text-bold">Product</h3>
                                <div class="timeline-body">
                                    <form id="formProduct_<%=period.getOID()%>">
                                        <input type="hidden" id="commandProduct_<%=period.getOID()%>" name="command_product" value="">
                                        <input type="hidden" id="command" name="command" value="<%=Command.EDIT%>">
                                        <input type="hidden" name="hidden_chain_id" value="<%=chainMain.getOID()%>">
                                        <table class="table table-bordered tabel_product">
                                            <tr>
                                                <th style="width: 1%">No.</th>
                                                <th>SKU</th>
                                                <th>Material</th>
                                                <th>Item Type</th>
                                                <th>Cost Type</th>
                                                <th>% Cost</th>
                                                <th>Distribution</th>
                                                <th>Stock Qty</th>
                                                <th>Cost Value</th>
                                                <th>Result</th>
                                                <th>Sub Total Cost</th>
                                                <th>Sales Value</th>
                                                <th>Sub Total Sales</th>
                                            </tr>
                                            <%
                                                int noProduct = 0;
                                                double totalSales = 0;
                                                double grandTotalCostPct = 0;
                                                double grandTotalResult = 0;
                                                double grandTotalCostProduct = 0;
                                                double grandTotalSales = 0;
                                                Vector listProduct = PstChainMainMaterial.list(0, 0, PstChainMainMaterial.fieldNames[PstChainMainMaterial.FLD_CHAIN_PERIOD_ID] + "=" + period.getOID(), "");
                                                //CARI NILAI REFERENCED COST JIKA ADA                                                
                                                double totalCostPeriod = PstChainAddCost.sumTotalCostPeriod(period.getOID());
                                                for (int xx = 0; xx < listProduct.size(); xx++) {
                                                    noProduct++;
                                                    ChainMainMaterial chainMainMaterial = (ChainMainMaterial) listProduct.get(xx);
                                                    Material material = new Material();
                                                    try {
                                                        material = PstMaterial.fetchExc(chainMainMaterial.getMaterialId());
                                                    } catch (Exception e) {
                                                        System.out.println(e.getMessage());
                                                    }
                                                    
                                                    double totalCostPct = chainMainMaterial.getCostPct();
                                                    double result = 0;
                                                    double subTotalCost = chainMainMaterial.getCostPct() / 100 * totalCostPeriod;
                                                    double subTotalSales = chainMainMaterial.getStockQty() * chainMainMaterial.getSalesValue();
                                                    
                                                    if (chainMainMaterial.getMaterialType() == PstChainMainMaterial.TYPE_RESULT) {
                                                        result = chainMainMaterial.getCostPct() / 100 * totalCostPeriod;
                                                    }
                                                    
                                                    grandTotalResult += result;
                                                    grandTotalCostProduct += subTotalCost;
                                                    grandTotalSales += subTotalSales;
                                                    grandTotalCostPct += totalCostPct;
                                            %>
                                            <tr>
                                                <td><%=noProduct%></td>
                                                <td><a class="btnAppendProduct" style="cursor: pointer" data-period_id="<%=chainMainMaterial.getChainPeriodId()%>" data-product_id="<%=chainMainMaterial.getOID()%>"><%=material.getSku()%></a></td>
                                                <td><%=material.getName()%></td>
                                                <td><%=PstChainMainMaterial.typeStr[chainMainMaterial.getMaterialType()]%></td>
                                                <td><%=PstChainMainMaterial.COST_TYPE_TITLE[chainMainMaterial.getCostType()]%></td>
                                                <td class="text-right" style="white-space: nowrap"><%=String.format("%,.2f", totalCostPct)%> %</td>
                                                <td class="text-center"><%=chainMainMaterial.getPeriodDistribution()%></td>
                                                <td class="text-right"><%=String.format("%,.0f", (double) chainMainMaterial.getStockQty())%></td>
                                                <td class="text-right"><%=String.format("%,.2f", chainMainMaterial.getCostValue())%></td>
                                                <td class="text-right"><%=String.format("%,.2f", result)%></td>
                                                <td class="text-right"><%=String.format("%,.2f", subTotalCost)%></td>
                                                <td class="text-right"><%=String.format("%,.2f", chainMainMaterial.getSalesValue())%></td>
                                                <td class="text-right"><%=String.format("%,.2f", subTotalSales)%></td>
                                                <input type="hidden" class="subTotalSalesProduct" value="<%=totalSales%>">
                                                <input type="hidden" class="totalCostPct_<%=chainMainMaterial.getChainPeriodId()%>" data-product_id="<%=chainMainMaterial.getOID()%>" value="<%=chainMainMaterial.getCostPct()%>">
                                            </tr>
                                            <%
                                                    totalSales = totalSales + (chainMainMaterial.getStockQty() * chainMainMaterial.getSalesValue());
                                                }
                                            %>
                                            
                                            <%
                                                if (listProduct.isEmpty()) {
                                            %>
                                            <tr class="label-default"><td colspan="13" class="text-center">Tidak ada data</td></tr>
                                            <%
                                                } else {
                                            %>
                                            <tr style="background-color: #f5eda3">
                                                <td class="text-bold" colspan="5">GRAND TOTAL</td>
                                                <td class="text-right text-bold <%=(grandTotalCostPct != 100 ? " text-red":"")%>" style="white-space: nowrap"><%=String.format("%,.2f", grandTotalCostPct)%> %</td>
                                                <td class="text-right text-bold" colspan="3"></td>
                                                <td class="text-right text-bold"><%=String.format("%,.2f", grandTotalResult)%></td>
                                                <td class="text-right text-bold"><%=String.format("%,.2f", grandTotalCostProduct)%></td>
                                                <td class="text-right text-bold"></td>
                                                <td class="text-right text-bold"><%=String.format("%,.2f", grandTotalSales)%></td>
                                            </tr>
                                            <%
                                                }
                                            %>
                                            <tr id="product_<%=period.getOID()%>"></tr>
                                        </table>
                                    </form>
                                </div>
                                <div class="timeline-footer" id="footerProduct_<%=period.getOID()%>">
                                    <a class="btn btn-primary btn-sm btnAppendProduct" data-period_id="<%=period.getOID()%>" data-prevoid="<%=prevPeriod%>">Tambah Product</a>
                                    <a class="btn btn-warning btn-sm btnCancelProduct hidden" data-period_id="<%=period.getOID()%>">Batal</a>
                                    <a class="btn btn-success btn-sm btnSaveProduct hidden">Simpan Product</a>
                                </div>
                            </div>
                        </li>
                    </ul>

                    <%
                            prevPeriod = period.getOID();
                        }
                    %>
                    <br>
                </div>
            </div>
        </section>

        <!-- Modal -->
        <div id="myModal" class="modal fade" role="dialog">
            <div class="modal-dialog">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Tambah Periode</h4>
                    </div>
                    <div class="modal-body">
                        <form id="frmPeriod" class="form-horizontal">
                            <input type="hidden" name="type" value="1">
                            <input type="hidden" id="commandPeriod" name="command_period" value="<%=Command.SAVE%>">
                            <input type="hidden" id="command" name="command" value="<%=Command.EDIT%>">
                            <input type="hidden" name="hidden_chain_id" value="<%=chainMain.getOID()%>">
                            <input type="hidden" id="periodId" name="hidden_period_id" value="<%=chainPeriod.getOID()%>">
                            
                            <div class="form-group">
                                <label class="col-sm-2 control-label">Title</label>
                                <div class="col-sm-8">
                                    <input type="hidden" name="<%=FrmChainPeriod.fieldNames[FrmChainPeriod.FRM_FIELD_CHAIN_MAIN_ID]%>" value="<%=chainMain.getOID()%>">
                                    <input type="text" id="periodTitle" placeholder="Period Title" class="form-control input-sm" name="<%=FrmChainPeriod.fieldNames[FrmChainPeriod.FRM_FIELD_TITLE]%>">
                                </div>
                            </div>
                                
                            <div class="form-group">
                                <label class="col-sm-2 control-label">Index</label>
                                <div class="col-sm-3">
                                    <%
                                        Vector<ChainPeriod> lastPeriod = PstChainPeriod.list(0, 1, PstChainPeriod.fieldNames[PstChainPeriod.FLD_CHAIN_MAIN_ID] + " = " + chainMain.getOID(), PstChainPeriod.fieldNames[PstChainPeriod.FLD_INDEX] + " DESC ");
                                        int nextIndex = 0;
                                        if (lastPeriod.size() > 0) {
                                            nextIndex = lastPeriod.get(0).getIndex();
                                        }
                                        nextIndex++;
                                    %>
                                    <input type="hidden" id="nextIndex"  value="<%=nextIndex%>">
                                    <input type="number" id="periodIndex" placeholder="Index" class="form-control input-sm" name="<%=FrmChainPeriod.fieldNames[FrmChainPeriod.FRM_FIELD_INDEX]%>">
                                </div>
                            </div>
                                
                            <div class="form-group">
                                <label class="col-sm-2 control-label">Durasi</label>
                                <input type="hidden" id="duration" name="<%=FrmChainPeriod.fieldNames[FrmChainPeriod.FRM_FIELD_DURATION]%>">
                                <div class="col-sm-4">
                                    <div class="input-group">
                                        <input type="number" id="periodDay" class="form-control input-sm countDuration">
                                        <span class="input-group-addon">Hari</span>
                                    </div>
                                </div>
                                <div class="col-sm-4">
                                    <div class="input-group">
                                        <input type="number" id="periodHour" class="form-control input-sm countDuration">
                                        <span class="input-group-addon">Jam</span>
                                    </div>
                                </div>
                            </div>
                                
                            <div class="form-group">
                                <label class="col-sm-2"></label>
                                <div class="col-sm-4">
                                    <div class="input-group">
                                        <input type="number" id="periodMinute" class="form-control input-sm countDuration">
                                        <span class="input-group-addon">Menit</span>
                                    </div>
                                </div>
                                <div class="col-sm-4">
                                    <div class="input-group">
                                        <input type="number" id="periodSecond" class="form-control input-sm countDuration">
                                        <span class="input-group-addon">Detik</span>
                                    </div>
                                </div>
                            </div>
                                
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-sm btn-success" id="btnSavePeriod" >Simpan</button>
                    </div>
                </div>

            </div>
        </div>

        <div id="modalItem" class="modal fade" role="dialog">
            <div class="modal-dialog modal-lg">

                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Tambah Item</h4>
                    </div>
                    <div class="modal-body">
                        <div id="itemElement">
                            <input type="hidden" id="sku">
                            <input type="hidden" id="materialName">
                            <input type="hidden" id="modalType">
                            <table class="table table-bordered table-striped">
                                <thead>
                                    <tr>
                                        <th style="width: 1%">No.</th>
                                        <th>SKU</th>
                                        <th>Nama Item</th>
                                        <th>Unit</th>
                                        <th>Nilai Stok</th>
                                        <th>Nilai Jual</th>
                                    </tr>
                                </thead>
                            </table>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </body>

    <script type="text/javascript" src="../../styles/bootstrap/js/jquery.min.js"></script>
    <script type="text/javascript" src="../../styles/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="../../styles/dimata-app.js"></script>
    <script type="text/javascript" src="../../styles/datepicker/bootstrap-datetimepicker.js" charset="UTF-8"></script>
    <script type="text/javascript" src="../../styles/select2/js/select2.min.js" charset="UTF-8"></script>
    <script type="text/javascript" src="../../styles/plugin/datatables/jquery.dataTables.js"></script>
    <script type="text/javascript" src="../../styles/plugin/datatables/dataTables.bootstrap.js"></script>
    <script>

        function dataTablesOptions(elementIdParent, elementId, servletName, dataFor, callBackDataTables) {
            $(elementIdParent).find('table').addClass('table-bordered table-striped table-hover').attr({'id': elementId});
            var sku = $("#sku").val();
            var materialName = $("#materialName").val();
            var modalType = $("#modalType").val();
            var locationId = "<%=chainMain.getChainLocation()%>";
            
            $("#" + elementId).dataTable({"bDestroy": true,
                "ordering": false,
                "iDisplayLength": 10,
                "bProcessing": true,
                "oLanguage": {
                    "sProcessing": "<div class='col-sm-12'><div class='progress progress-striped active'><div class='progress-bar progress-bar-primary' style='width: 100%'><b>Please Wait...</b></div></div></div>"
                },
                "bServerSide": true,
                "sAjaxSource": "<%= approot%>/" + servletName + "?command=<%= Command.LIST%>&FRM_FIELD_DATA_FOR=" + dataFor + "&FRM_FLD_APP_ROOT=<%=approot%>"
                        + "&hidden_chain_id=<%=chainMain.getOID()%>" + "&modal_item=" + modalType + "&where_sku=" + sku + "&where_name=" + materialName
                        + "&location_id=" + locationId,
                aoColumnDefs: [
                    {
                        bSortable: false,
                        aTargets: 1
                    }
                ],
                "initComplete": function (settings, json) {
                    if (callBackDataTables !== null) {
                        callBackDataTables();
                    }
                },
                "fnDrawCallback": function (oSettings) {
                    if (callBackDataTables !== null) {
                        callBackDataTables();
                    }
                },
                "fnPageChange": function (oSettings) {

                }
            });
            $(elementIdParent).find("#" + elementId + "_filter").find("input").addClass("form-control");
            $(elementIdParent).find("#" + elementId + "_length").find("select").addClass("form-control");

            $("#" + elementId).css("width", "100%");
        }

        function runDataTables() {
            dataTablesOptions("#itemElement", "tableItemElement", "AjaxProductionChain", "listItemCostSales", null);
        }

        $('#modalItem').on('show.bs.modal', function (e) {
            if (window.top.document.querySelector('iframe')) {
                $('#modalItem').css('top', window.top.scrollY); //set modal position
            }
        });
        
        $('#myModal').on('show.bs.modal', function (e) {
            if (window.top.document.querySelector('iframe')) {
                $('#myModal').css('top', window.top.scrollY); //set modal position
            }
        });

        $('.multiSelect').select2({"placeholder": "Semua lokasi"});

        $('#btnBack').click(function(){
            $(this).html("<i class='fa fa-spinner fa-pulse'></i> Tunggu...").attr({"disabled": true});
        });
        
        $('#btnSaveMain').click(function () {
            var error = 0;
            if (error === 0) {
                $(this).html("<i class='fa fa-spinner fa-pulse'></i> Tunggu...").attr({"disabled": true});
                $('#command').val("<%=Command.SAVE%>");
                $('#formChain').submit();
            }
        });

        $('#btnAddPeriod').click(function () {
            $("#periodId").val(0);
            $("#periodTitle").val("");
            $("#periodIndex").val($('#nextIndex').val());
            $("#periodDay").val(0);
            $("#periodHour").val(0);
            $("#periodMinute").val(0);
            $("#periodSecond").val(0);
            $("#myModal .modal-title").html('Tambah Periode');
            $("#myModal").modal('show');
        });
        
        $('.btnEditPeriod').click(function () {
            $("#periodId").val($(this).data('period_id'));
            $("#periodTitle").val($(this).data('period_title'));
            $("#periodIndex").val($(this).data('period_index'));
            $("#periodDay").val($(this).data('days'));
            $("#periodHour").val($(this).data('hours'));
            $("#periodMinute").val($(this).data('minutes'));
            $("#periodSecond").val($(this).data('seconds'));
            
            $("#myModal .modal-title").html('Ubah Periode');
            $("#myModal").modal('show');
        });
        
        $('#btnSavePeriod').click(function () {
            var error = 0;
            if (error === 0) {
                $(this).html("<i class='fa fa-spinner fa-pulse'></i> Tunggu...").attr({"disabled": true});
                $('#commandPeriod').val("<%=Command.SAVE%>");
                $('#frmPeriod').submit();
            }
        });

        var base = "<%= approot%>";

        $('.btnAppendProduct').click(function () {
            var oidPeriod = $(this).data('period_id');
            var prevOid = $(this).data('prevoid');
            var oidProduct = $(this).data('product_id');
            
            var url = "" + base + "/AjaxProductionChain";
            var data = "command=<%=Command.NONE%>&PERIOD_ID=" + oidPeriod + "&FRM_FIELD_DATA_FOR=productRow&PREV_PERIOD_ID=" + prevOid + "&PRODUCT_ID=" + oidProduct;
            
            $.ajax({
                url: "" + url + "",
                data: "" + data + "",
                type: "POST",
                dataType: "json",
                async: false,
                cache: false,
                success: function (data) {
                    
                },
                error: function (data) {
                    //alert('error');
                }
            }).done(function (data) {
                $("#product_" + oidPeriod).html(data.FRM_FIELD_HTML);
                $("#footerProduct_" + oidPeriod + " .btnCancelProduct").removeClass('hidden');
                $("#footerProduct_" + oidPeriod + " .btnSaveProduct").removeClass('hidden');
                
                $('.btn_src_product').unbind().click(function () {
                    $(this).html("<i class='fa fa-spinner fa-pulse'></i>").attr({"disabled": true});
                    var sku = $('#product_sku').val();
                    var name = $('#product_name').val();
                    $("#sku").val(sku);
                    $("#materialName").val(name);
                    $("#modalType").val(2);
                    $('#modalItem').modal('show');
                    $(this).html("<i class='fa fa-search'></i>").attr({"disabled": false});
                    dataTablesOptions("#itemElement", "tableItemElement", "AjaxProductionChain", "listItemCostSales", null);
                });
                
                $('.hitungCostValue').keyup(function () {
                    var cost = $("#cost").val();
                    var qty = $('#product_qty').val().replace(/,/g, "");
                    var salesValue = $('#product_sold_qty').val().replace(/,/g, "");
                    var subTotalSales = (salesValue * qty);
                    if (isNaN(subTotalSales)) {
                        subTotalSales = 0;
                    }
                    
                    var totalSales = 0;
                    $('.subTotalSalesProduct').each(function (i) {
                        totalSales = +totalSales + +$(this).val();
                    });
                    totalSales = +totalSales + +subTotalSales;
                    var pct = (+subTotalSales / +totalSales) * 100;
                    if (isNaN(pct)) {
                        pct = 0;
                    }
                    
                    //$("#product_pct").val(pct);
                    $('#product_subtotal_sales').val(subTotalSales.toLocaleString());

                });
                
                $('.btnSaveProduct').unbind().click(function () {
                    var error = 0;
                    var idItemProduct = $('#product_item').val();
                    if (idItemProduct === "" || idItemProduct === "0" || idItemProduct === 0) {
                        alert("Pilih item product terlebih dahulu !");
                        error += 1;
                        return false;
                    }
                    
                    if ($('#mat_type').val() === "<%=PstChainMainMaterial.TYPE_NEXT_COST%>") {
                        var distribusi = Number($('#product_distribution').val());
                        if (distribusi <= 0) {
                            alert("Jumlah periode untuk item distribusi tidak boleh " + distribusi + " !");
                            error += 1;
                            return false;
                        }
                    }

                    if (error === 0) {
                        $(this).html("<i class='fa fa-spinner fa-pulse'></i> Tunggu...").attr({"disabled": true});
                        $('#commandProduct_' + oidPeriod).val("<%=Command.SAVE%>");
                        $('#formProduct_' + oidPeriod).submit();
                    }
                });
                
                $('#cost_type').change(function(){
                    if ($(this).val() === "<%=PstChainMainMaterial.COST_TYPE_CUSTOM%>") {
                        $('#product_pct').attr({"readonly":false});
                        $('#product_pct').focus();
                        var totalPct = 0;
                        $('.totalCostPct_'+oidPeriod).each(function () {
                            if ($(this).data('product_id') !== oidProduct) {
                                totalPct += +$(this).val();
                            }
                        });
                        $('#product_pct').val(100 - +totalPct);
                    } else {
                        $('#product_pct').attr({"readonly":true});
                        $('#product_pct').val(0);
                    }
                });
                
                $('#mat_type').change(function(){
                    if ($(this).val() === "<%=PstChainMainMaterial.TYPE_NEXT_COST%>") {
                        $('#product_distribution').attr({"readonly":false});
                        $('#product_distribution').focus();
                    } else {
                        $('#product_distribution').attr({"readonly":true});
                        $('#product_distribution').val(0);
                    }
                });
                
                $('#product_pct').keyup(function() {
                    var totalPct = 0;
                    $('.totalCostPct_'+oidPeriod).each(function () {
                        if ($(this).data('product_id') !== oidProduct) {
                            totalPct += +$(this).val();
                        }
                    });
                    
                    var countPct = +totalPct + +$(this).val();
                    if (countPct > 100) {
                        alert("Total persentase (" + countPct + ") lebih dari 100% !");
                        $(this).val(0);
                    }
                });
                
                $('.btnDeleteProduct').unbind().click(function(){
                    if (confirm("Delete product ?")) {
                        var oidPeriod = $(this).data('period_id');
                        $('#commandProduct_' + oidPeriod).val("<%=Command.DELETE%>");
                        $('#formProduct_' + oidPeriod).submit();
                    }
                });
                
            });
        });
        
        $('.btnAppendCost').click(function () {
            var oidPeriod = $(this).data('period_id');
            var prevOid = $(this).data('prevoid');
            var oidCost = $(this).data('cost_id');
            
            var url = "" + base + "/AjaxProductionChain";
            var data = "command=<%=Command.NONE%>&PERIOD_ID=" + oidPeriod + "&FRM_FIELD_DATA_FOR=costRow&PREV_PERIOD_ID=" + prevOid + "&COST_ID=" + oidCost;

            $.ajax({
                url: "" + url + "",
                data: "" + data + "",
                type: "POST",
                dataType: "json",
                async: false,
                cache: false,
                success: function (data) {

                },
                error: function (data) {
                    //alert('error');
                }
            }).done(function (data) {
                $("#cost_" + oidPeriod).html(data.FRM_FIELD_HTML);
                $("#footerCost_" + oidPeriod + " .btnCancelCost").removeClass('hidden');
                $("#footerCost_" + oidPeriod + " .btnSaveCost").removeClass('hidden');
                
                $('.btn_src_cost').unbind().click(function () {
                    $(this).html("<i class='fa fa-spinner fa-pulse'></i>").attr({"disabled": true});
                    var sku = $('#cost_sku').val();
                    var name = $('#cost_name').val();
                    $("#sku").val(sku);
                    $("#materialName").val(name);
                    $("#modalType").val(1);
                    $('#modalItem').modal('show');
                    $(this).html("<i class='fa fa-search'></i>").attr({"disabled": false});
                    dataTablesOptions("#itemElement", "tableItemElement", "AjaxProductionChain", "listItemCostSales", null);
                });
                
                $('.hitungCostValueCost').keyup(function () {
                    var qty = $('#cost_qty').val().replace(/,/g, "");
                    var subTotal = $('#cost_value').val().replace(/,/g, "");
                    var harga = (subTotal * qty);
                    if (isNaN(harga)) {
                        harga = 0;
                    }
                    $('#cost_subtotal').val(harga.toLocaleString());
                });
                
                $('.btnSaveCost').unbind().click(function () {
                    var error = 0;
                    var idItemCost = $('#cost_item').val();

                    if (idItemCost === "" || idItemCost === "0" || idItemCost === 0) {
                        error += 1;
                        alert("Pilih item cost terlebih dahulu !");
                    }

                    if (error === 0) {
                        $(this).html("<i class='fa fa-spinner fa-pulse'></i> Tunggu...").attr({"disabled": true});
                        $('#commandCost_' + oidPeriod).val("<%=Command.SAVE%>");
                        $('#formCost_' + oidPeriod).submit();
                    }
                });
                
                $('.btnDeleteCost').unbind().click(function(){
                    if (confirm("Delete cost ?")) {
                        var oidPeriod = $(this).data('period_id');
                        $('#commandCost_' + oidPeriod).val("<%=Command.DELETE%>");
                        $('#formCost_' + oidPeriod).submit();
                    }
                });
                
            });

        });
        
        function selectItem(itemId, itemSku, itemName, unitId, stockValue, salesValue) {
            var type = $("#modalType").val();
            if (type === "2") {
                $('#product_item').val(itemId);
                $('#product_sku').val(itemSku);
                $('#product_name').val(itemName);
                $('#product_sold_qty').val(salesValue);
            }
            if (type === "1") {
                $('#cost_item').val(itemId);
                $('#cost_sku').val(itemSku);
                $('#cost_name').val(itemName);
                $('#cost_value').val(stockValue);
            }
            $('#modalItem').modal('hide');
        }
        
        function reloadPage() {
            window.location = "add_chain.jsp?command=<%=Command.EDIT%>&hidden_chain_id=<%=chainMain.getOID() %>";
        }
        
        $('.btnCancelCost').click(function(){
            var oidPeriod = $(this).data('period_id');
            $("#cost_" + oidPeriod).html("");
            $("#footerCost_" + oidPeriod + " .btnCancelCost").addClass('hidden');
            $("#footerCost_" + oidPeriod + " .btnSaveCost").addClass('hidden');
        });
        
        $('.btnCancelProduct').click(function(){
            var oidPeriod = $(this).data('period_id');
            $("#product_" + oidPeriod).html("");
            $("#footerProduct_" + oidPeriod + " .btnCancelProduct").addClass('hidden');
            $("#footerProduct_" + oidPeriod + " .btnSaveProduct").addClass('hidden');
        });
        
        function countDuration() {
            var days = parseInt($('#periodDay').val());
            var hours = parseInt($('#periodHour').val());
            var minutes = parseInt($('#periodMinute').val());
            var seconds = parseInt($('#periodSecond').val());
            
            days = (isNaN(days)) ? 0 : days;
            hours = (isNaN(hours)) ? 0 : hours;
            minutes = (isNaN(minutes)) ? 0 : minutes;
            seconds = (isNaN(seconds)) ? 0 : seconds;
            
            var dayToMilliseconds = days * 24 * 60 * 60 * 1000;
            var hourToMilliseconds = hours * 60 * 60 * 1000;
            var minuteToMilliseconds = minutes * 60 * 1000;
            var secondToMilliseconds = seconds * 1000;
            
            $('#duration').val(+dayToMilliseconds + +hourToMilliseconds + +minuteToMilliseconds + +secondToMilliseconds);
        }
        
        $('.countDuration').keyup(function() {
            countDuration();
        });
        
        $('.countDuration').change(function() {
            countDuration();
        });

    </script>
    <script>
        $(document).ready(function(){
            if("<%=iErrorMain%>" !== "0") {
                alert("<%=messageMain%>");
            }
            if("<%=iErrorPeriod%>" !== "0") {
                alert("<%=messagePeriod%>");
            }
            if("<%=iErrorProduct%>" !== "0") {
                alert("<%=messageProduct%>");
            }
            if("<%=iErrorCost%>" !== "0") {
                alert("<%=messageCost%>");
            }
        });
    </script>
</html>

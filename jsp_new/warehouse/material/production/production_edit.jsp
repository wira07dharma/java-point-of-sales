<%-- 
    Document   : production_edit
    Created on : Sep 11, 2019, 3:15:29 PM
    Author     : Dimata 007
--%>

<%@page import="com.dimata.posbo.entity.warehouse.*"%>
<%@page import="com.dimata.posbo.entity.masterdata.*"%>
<%@page import="com.dimata.posbo.form.warehouse.*"%>
<%@page import="com.dimata.posbo.session.warehouse.*"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../../../main/javainit.jsp" %>
<%int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_PENENTUAN_HPP);%>
<%@ include file = "../../../main/checkuser.jsp" %>

<%//
    int command = FRMQueryString.requestCommand(request);
    long oidProduction = FRMQueryString.requestLong(request, "production_id");

    CtrlProduction ctrlProduction = new CtrlProduction(request);
    int error = ctrlProduction.action(command, oidProduction);
    String message = ctrlProduction.getMessage();
    Production production = ctrlProduction.getProduction();

    String prodDate = "";
    if (command == Command.ADD) {
        prodDate = Formater.formatDate(new java.util.Date(), "yyyy-MM-dd HH:mm:ss");
        production.setProductionNumber("Otomatis");
    } else {
        prodDate = (production.getProductionDate() == null) ? "" : Formater.formatDate(production.getProductionDate(), "yyyy-MM-dd HH:mm:ss");
    }

%>

<%//
    Vector<Location> listLocation = PstLocation.list(0, 0, "", "" + PstLocation.fieldNames[PstLocation.FLD_NAME]);
    String optionLocationFrom = "";
    String optionLocationTo = "";
    for (Location l : listLocation) {
        String selected = (production.getLocationFromId() == l.getOID()) ? "selected" : "";
        optionLocationFrom += "<option " + selected + " value='" + l.getOID() + "'>" + l.getName() + "</option>";

        selected = (production.getLocationToId() == l.getOID()) ? "selected" : "";
        optionLocationTo += "<option " + selected + " value='" + l.getOID() + "'>" + l.getName() + "</option>";
    }

    String dateTimeNow = Formater.formatDate(new java.util.Date(), "yyyy-MM-dd HH:mm:ss");
    
    String optionMatType = "";
    for (int x=0; x < PstProductionProduct.TYPE_TITLE.length; x++){
        if (x == PstProductionProduct.TYPE_REFERENCED_COST) {
            continue;
        }
        optionMatType += "<option value='" + x + "'>" + PstProductionProduct.TYPE_TITLE[x] + "</option>";
    }
    
    String optionCostType = "";
    for (int x = 0; x < PstProductionProduct.COST_TYPE_TITLE.length; x++) {
        if (x == PstProductionProduct.COST_TYPE_REFERENCED) {
            continue;
        }
        optionCostType += "<option value='" + x + "'>" + PstProductionProduct.COST_TYPE_TITLE[x] + "</option>";
    }
    
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@ include file = "../../../styles/plugin_component.jsp" %>
        <style>
            .box .box-header, .box .box-footer {border-color: lightgray}
            .modal-header, .modal-footer {border-color: lightgray}
            form, .modal-footer {margin: 0px}
            .datetimepicker th {font-size: 14px}
            .datetimepicker td {font-size: 12px}

            body {
                background-color: #DDD;
            }
            .tabel_data tr th {
                font-size: 12px;
                text-align: center;
            }
            .tabel_data tr td {
                font-size: 12px;
            }
            
            .tabel_cost tbody tr th {
                text-align: center;
                font-size: 12px;
                background-color: #00a65a;
                color: white;
                padding: 5px;
            }
            
            .tabel_cost tbody tr td {
                font-size: 12px;
                padding: 5px;
            }

            .tabel_product tbody tr th {
                text-align: center;
                font-size: 12px;
                background-color: #f39c12;
                color: white;
                padding: 5px;
            }
            
            .tabel_product tbody tr td {
                font-size: 12px;
                padding: 5px;
            }

            .panel_data {
                background-color: lightblue;
                font-weight: bold;
                text-align: center;
                padding: 5px;
            }
            
            #tableItemElement th {padding: 8px; font-size: 12px; background-color: lightgray}
            #tableItemElement td {padding: 8px; font-size: 12px}

        </style>
        <title>Production</title>
    </head>
    <body>
        <div class="col-sm-12">
            <h3>Produksi</h3>
            <div class="box box-primary">
                <div class="box-header with-border">
                    <h3 class="box-title">Dokumen Produksi</h3>
                </div>

                <div class="box-body">
                    <form id="formProduction" class="form-horizontal">
                        <input type="hidden" id="command" name="command" value="<%=Command.SAVE%>">
                        <input type="hidden" name="production_id" value="<%=production.getOID() %>">
                        <div class="row">
                            <div class="col-sm-4">
                                <div class="form-group">
                                    <label class="col-sm-3 control-label">Nomor</label>
                                    <div class="col-sm-8">
                                        <label class="control-label"><%=production.getProductionNumber() %></label>
                                        <input type="hidden" readonly="" placeholder="Otomatis" class="form-control input-sm" name="<%=FrmProduction.fieldNames[FrmProduction.FRM_FIELD_PRODUCTION_NUMBER]%>" value="<%=production.getProductionNumber()%>">
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="col-sm-3 control-label">Tanggal</label>
                                    <div class="col-sm-8">
                                        <input type="text" placeholder="Tanggal produksi" autocomplete="off" id="dateProd" class="form-control input-sm datePicker" name="<%=FrmProduction.fieldNames[FrmProduction.FRM_FIELD_PRODUCTION_DATE]%>" value="<%=prodDate%>">
                                    </div>
                                </div>
                            </div>

                            <div class="col-sm-4">
                                <div class="form-group">
                                    <label class="col-sm-4 control-label">Lokasi Asal</label>
                                    <div class="col-sm-8">
                                        <select class="form-control input-sm" name="<%=FrmProduction.fieldNames[FrmProduction.FRM_FIELD_LOCATION_FROM_ID]%>">
                                            <%=optionLocationFrom%>
                                        </select>
                                    </div>
                                </div>
                                        
                                <div class="form-group">
                                    <label class="col-sm-4 control-label">Lokasi Tujuan</label>
                                    <div class="col-sm-8">
                                        <select class="form-control input-sm" name="<%=FrmProduction.fieldNames[FrmProduction.FRM_FIELD_LOCATION_TO_ID]%>">
                                            <%=optionLocationTo%>
                                        </select>
                                    </div>
                                </div>
                            </div>

                            <div class="col-sm-4">
                                <div class="form-group">
                                    <label class="col-sm-4 control-label">Status</label>
                                    <div class="col-sm-8">
                                        <select id="statusProduction" class="form-control input-sm" name="<%=FrmProduction.fieldNames[FrmProduction.FRM_FIELD_PRODUCTION_STATUS]%>">
                                            <%if (production.getProductionStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) {%>
                                            <option value="<%=I_DocStatus.DOCUMENT_STATUS_DRAFT%>"><%=I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]%></option>
                                            <option value="<%=I_DocStatus.DOCUMENT_STATUS_FINAL%>"><%=I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]%></option>
                                            <%} else {%>
                                            <option value="<%=production.getProductionStatus()%>"><%=I_DocStatus.fieldDocumentStatus[production.getProductionStatus()]%></option>
                                            <%}%>
                                        </select>
                                    </div>
                                </div>
                                        
                                <div class="form-group">
                                    <label class="col-sm-4 control-label">Keterangan</label>
                                    <div class="col-sm-8">
                                        <textarea class="form-control input-sm" name="<%=FrmProduction.fieldNames[FrmProduction.FRM_FIELD_REMARK]%>"><%=production.getRemark()%></textarea>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>

                <div class="box-footer">
                    <a id="btnBack" class="btn btn-sm btn-primary" href="production_src.jsp"><i class="fa fa-undo"></i>&nbsp; Kembali</a>
                    <%if (production.getProductionStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) {%>
                    <a id="btnSaveProduction" class="btn btn-sm btn-primary"><i class="fa fa-save"></i>&nbsp; Simpan</a>
                    <%}%>
                    <%if(production.getOID() > 0 && production.getProductionStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) {%>
                    <a id="btnAddGroup" class="btn btn-sm btn-primary"><i class="fa fa-plus"></i>&nbsp; Tambah Periode</a>
                    <%}%>
                    <%if(production.getOID() > 0 && production.getProductionStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) {%>
                    <a id="btnDeleteProduction" class="btn btn-sm btn-primary pull-right"><i class="fa fa-trash"></i>&nbsp; Hapus Produksi</a>
                    <%}%>
                </div>
            </div>

            <%if (production.getOID() > 0) {%>
            <%
                String order = PstProductionGroup.fieldNames[PstProductionGroup.FLD_BATCH_NUMBER] + " ASC "
                        + "," + PstProductionGroup.fieldNames[PstProductionGroup.FLD_INDEX] + " ASC ";
                Vector<ProductionGroup> listGroup = PstProductionGroup.list(0, 0, PstProductionGroup.fieldNames[PstProductionGroup.FLD_PRODUCTION_ID] + " = " + production.getOID(), order);
                for (ProductionGroup group : listGroup) {
                    ChainMain chainMain = new ChainMain();
                    ChainPeriod chainPeriod = new ChainPeriod();
                    try {
                        chainPeriod = PstChainPeriod.fetchExc(group.getChainPeriodId());
                        chainMain = PstChainMain.fetchExc(chainPeriod.getChainMainId());
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    
                    String start = group.getDateStart() == null ? "-" : Formater.formatDate(group.getDateStart(), "yyyy-MM-dd HH:mm:ss");
                    String end = group.getDateStart() == null ? "-" : Formater.formatDate(group.getDateEnd(), "yyyy-MM-dd HH:mm:ss");
            %>

            <div class="box box-danger">
                <div class="box-header with-border">
                    <span><a class="btn btn-danger text-bold editGroup" data-group_id="<%=group.getOID()%>" style="cursor: pointer; border-radius: 0">Batch : <%=group.getBatchNumber()%> &nbsp;<span class="label text-red" style="background-color: white"><%=group.getIndex() %></span></a></span>
                    <span>&nbsp;</span>
                    <span><b>Template : <a href="../../../master/material/add_chain.jsp?command=<%=Command.EDIT%>&hidden_chain_id=<%=chainMain.getOID()%>"><%=chainMain.getChainTitle()%></a></b></span>
                    <span>&nbsp;&nbsp;&nbsp;&nbsp;</span>
                    <span><b>Periode :</b> <%=chainPeriod.getTitle()%></span>
                    <span>&nbsp;&nbsp;&nbsp;&nbsp;</span>
                    <span><b>Tanggal :</b> <%=start%> <b>&nbsp;s/d&nbsp;</b> <%=end%></span>
                </div>

                <div class="box-body">
                    <label>Cost</label>
                    <table class="table table-bordered tabel_cost">
                        <tr>
                            <th style="width: 1%">No.</th>
                            <th>SKU</th>
                            <th>Material</th>
                            <th>Type</th>
                            <th>Qty</th>
                            <th>Cost</th>
                            <th>Total Cost</th>
                        </tr>

                        <%
                            Vector<ProductionCost> listCost = PstProductionCost.list(0, 0, PstProductionCost.fieldNames[PstProductionCost.FLD_PRODUCTION_GROUP_ID] + " = " + group.getOID(), "");
                            int costNo = 0;
                            double grandTotalCost = 0;
                            for (ProductionCost pc : listCost) {
                                costNo++;
                                grandTotalCost += pc.getStockQty() * pc.getStockValue();
                                
                                Material m = new Material();
                                try {
                                    m = PstMaterial.fetchExc(pc.getMaterialId());
                                } catch (Exception e) {

                                }
                                
                                String dataBind = "data-group_id='" + group.getOID() + "'"
                                        + " data-cost_id='" + pc.getOID() + "'"
                                        + " data-item_id='" + pc.getMaterialId() + "'"
                                        + " data-item_name='" + m.getName() + "'"
                                        + " data-qty='" + pc.getStockQty() + "'"
                                        + " data-value='" + String.format("%.2f", pc.getStockValue()) + "'"
                                        + "";
                        %>

                        <tr>
                            <td><%=costNo%>.</td>
                            <td>
                                <%if (production.getProductionStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) {%>
                                
                                <%if(pc.getCostType() == PstProductionCost.COST_TYPE_REFERENCED && pc.getProductDistributionId() > 0) {%>
                                <%=m.getSku()%>
                                <%}else{%>
                                <a class="editCost" style="cursor: pointer" <%=dataBind%> ><%=m.getSku()%></a>
                                <%}%>
                                
                                <%}else{%>
                                <%=m.getSku()%>
                                <%}%>
                            </td>
                            <td><%=m.getName()%></td>
                            <td><%=PstProductionCost.COST_TYPE_TITLE[pc.getCostType()] %></td>
                            <td class="text-right costQty"><%=String.format("%,.0f", pc.getStockQty())%></td>
                            <td class="text-right"><%=String.format("%,.2f", pc.getStockValue())%></td>
                            <td class="text-right"><%=String.format("%,.2f", pc.getStockQty() * pc.getStockValue())%></td>
                        </tr>

                        <%
                            }
                        %>

                        <%if (listCost.isEmpty()) {%>
                        <tr class="label-default"><td colspan="7" class="text-center">Tidak ada data</td></tr>
                        <%} else {%>
                        <tr style="background-color: #d1ffd1">
                            <td colspan="6" class="text-left text-bold">Grand Total</td>
                            <td class="text-right text-bold"><%=String.format("%,.2f", grandTotalCost)%></td>
                        </tr>
                        <%}%>
                    </table>
                    <p></p>
                    <%if (production.getProductionStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) {%>
                    <div>
                        <a class="btn btn-xs btn-default text-bold btnAddCost" data-group_id="<%=group.getOID()%>">Tambah Cost</a>
                    </div>
                    <%}%>

                    <hr style="border-color: lightgray">
                    
                    <label>Product</label>
                    <table class="table table-bordered tabel_product">
                        <tr>
                            <th style="width: 1%">No.</th>
                            <th>SKU</th>
                            <th>Material</th>
                            <th>Item Type</th>
                            <th>Cost Type</th>
                            <th>% Cost</th>
                            <th>Distribution</th>
                            <th>Qty</th>
                            <th>Cost</th>
                            <th>Result</th>
                            <th>Total Cost</th>
                            <th>Sales</th>
                            <th>Total Sales</th>
                        </tr>
                        
                        <%
                            Vector<ProductionProduct> listProduct = PstProductionProduct.list(0, 0, PstProductionProduct.fieldNames[PstProductionProduct.FLD_PRODUCTION_GROUP_ID] + " = " + group.getOID(), "");
                            int noProduct = 0;
                            double grandTotalCostPct = 0;
                            double grandTotalResult = 0;
                            double grandTotalCostProduct = 0;
                            double grandTotalSales = 0;
                            double totalCostGroup = SessProduction.sumTotalCostGroupPeriod(group.getOID());
                            for (ProductionProduct pp : listProduct) {
                                noProduct++;
                                
                                double result = 0;
                                double totalCostPct = pp.getCostPct();
                                double totalCost = pp.getCostPct() / 100 * totalCostGroup;
                                double totalSales = pp.getStockQty() * pp.getSalesValue();
                                
                                if (pp.getMaterialType() == PstProductionProduct.TYPE_RESULT) {
                                    result = pp.getCostPct() / 100 * totalCostGroup;
                                }
                                
                                grandTotalCostPct += totalCostPct;
                                grandTotalResult += result;
                                grandTotalCostProduct += totalCost;
                                grandTotalSales += totalSales;
                                
                                Material m = new Material();
                                try {
                                    m = PstMaterial.fetchExc(pp.getMaterialId());
                                } catch (Exception e) {

                                }
                                
                                String dataBind = "data-group_id='" + group.getOID() + "'"
                                        + " data-product_id='" + pp.getOID() + "'"
                                        + " data-item_id='" + pp.getMaterialId() + "'"
                                        + " data-item_name='" + m.getName() + "'"
                                        + " data-item_type='" + pp.getMaterialType() + "'"
                                        + " data-distribution='" + pp.getPeriodDistribution()+ "'"
                                        + " data-qty='" + pp.getStockQty()+ "'"
                                        + " data-cost_type='" + pp.getCostType()+ "'"
                                        + " data-cost_pct='" + pp.getCostPct()+ "'"
                                        + " data-cost_value='" + String.format("%.2f", pp.getCostValue())+ "'"
                                        + " data-sales_value='" + String.format("%.2f",pp.getSalesValue())+ "'"
                                        + "";
                        %>
                        
                        <tr>
                            <td><%=noProduct%>.</td>
                            <td>
                                <%if (production.getProductionStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) {%>
                                <a class="editProduct" style="cursor: pointer" <%=dataBind%> ><%=m.getSku() %></a>
                                <%}else{%>
                                <%=m.getSku() %>
                                <%}%>
                            </td>
                            <td><%=m.getName() %></td>
                            <td><%=PstProductionProduct.TYPE_TITLE[pp.getMaterialType()] %></td>
                            <td><%=PstProductionProduct.COST_TYPE_TITLE[pp.getCostType()] %></td>
                            <td class="text-right">
                                <%=String.format("%,.2f", totalCostPct) %> %
                                <input type="hidden" class="costPctProduct_<%=group.getOID()%>" data-product_id="<%=pp.getOID() %>" value="<%=totalCostPct%>">
                            </td>
                            <td class="text-center"><%=pp.getPeriodDistribution() %></td>
                            <td class="text-right productQty"><%=String.format("%,.0f", pp.getStockQty())%></td>
                            <td class="text-right"><%=String.format("%,.2f", pp.getCostValue()) %></td>
                            <td class="text-right"><%=String.format("%,.2f", result) %></td>
                            <td class="text-right"><%=String.format("%,.2f", totalCost) %></td>
                            <td class="text-right"><%=String.format("%,.2f", pp.getSalesValue()) %></td>
                            <td class="text-right"><%=String.format("%,.2f", totalSales) %></td>
                        </tr>
                        
                        <%
                            }
                        %>
                        
                        <%if (listProduct.isEmpty()) {%>
                        <tr><td colspan="13" class="label-default text-center">Tidak ada data</td></tr>
                        <%}else{%>
                        <tr style="background-color: #f5eda3">
                            <td colspan="5" class="text-left text-bold">Grand Total</td>
                            <td class="text-right text-bold <%=(grandTotalCostPct != 100 ? " text-red":"")%>"><%=String.format("%,.2f", grandTotalCostPct) %> %</td>
                            <td colspan="3"></td>
                            <td class="text-right text-bold"><%=String.format("%,.2f", grandTotalResult) %></td>
                            <td class="text-right text-bold"><%=String.format("%,.2f", grandTotalCostProduct) %></td>
                            <td></td>
                            <td class="text-right text-bold"><%=String.format("%,.2f", grandTotalSales) %></td>
                        </tr>
                        <%}%>
                    </table>
                    <p></p>
                    <%if (production.getProductionStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) {%>
                    <div>
                        <a id="" class="btn btn-xs btn-default text-bold btnAddProduct" data-group_id="<%=group.getOID()%>">Tambah Product</a>
                    </div>
                    <%}%>
                </div>
            </div>

            <%
                }
            %>
            <%}%>
            <br>
            <br>
            <br>
        </div>
        
        <div id="modalGroup" class="modal fade" role="dialog">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Periode</h4>
                    </div>
                    <div class="modal-body">
                        <form id="formGroup" class="form-horizontal">
                            <input type="hidden" name="FRM_FIELD_DATA_FOR" value="saveProductionGroup">
                            <input type="hidden" name="command" value="<%=Command.SAVE%>">
                            <input type="hidden" name="<%=FrmProductionGroup.fieldNames[FrmProductionGroup.FRM_FIELD_PRODUCTION_ID]%>" value="<%=production.getOID()%>">
                            <input type="hidden" id="groupId" name="<%=FrmProductionGroup.fieldNames[FrmProductionGroup.FRM_FIELD_PRODUCTION_GROUP_ID]%>" value="">

                            <div class="row">
                                <div class="col-sm-12">
                                    <div class="form-group">
                                        <label class="col-sm-4">Nomor Batch</label>
                                        <div class="col-sm-8">
                                            <select id="selectBatch" class="form-control input-sm selectSearch" style="width: 100%" name="<%=FrmProductionGroup.fieldNames[FrmProductionGroup.FRM_FIELD_BATCH_NUMBER]%>">
                                                
                                            </select>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label class="col-sm-4">Template Produksi</label>
                                        <div class="col-sm-8">
                                            <select id="selectTemplate" class="form-control input-sm" name="FRM_CHAIN_MAIN_ID">
                                                
                                            </select>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label class="col-sm-4">Periode</label>
                                        <div class="col-sm-4">
                                            <select id="selectPeriod" class="form-control input-sm" name="<%=FrmProductionGroup.fieldNames[FrmProductionGroup.FRM_FIELD_CHAIN_PERIOD_ID]%>">
                                                
                                            </select>
                                        </div>
                                        <div class="col-sm-4">
                                            <div class="input-group">
                                                <span class="input-group-addon">Index</span>
                                                <input type="number" readonly="" id="groupIndex" class="form-control input-sm" name="<%=FrmProductionGroup.fieldNames[FrmProductionGroup.FRM_FIELD_INDEX]%>" value="">
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label class="col-sm-4">Tanggal</label>
                                        <div class="col-sm-8">
                                            <div class="input-group">
                                                <input type="text" autocomplete="off" id="startDate" class="form-control input-sm datePicker" name="<%=FrmProductionGroup.fieldNames[FrmProductionGroup.FRM_FIELD_DATE_START]%>" value="<%=dateTimeNow%>">
                                                <span class="input-group-addon">s/d</span>
                                                <input type="text" autocomplete="off" id="endDate" class="form-control input-sm datePicker" name="<%=FrmProductionGroup.fieldNames[FrmProductionGroup.FRM_FIELD_DATE_END]%>" value="<%=dateTimeNow%>">
                                            </div>
                                        </div>
                                    </div>

                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <%if (production.getProductionStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) {%>
                        <button type="button" id="btnDeleteGroup" class="btn btn-sm btn-danger hidden pull-left">Hapus</button>
                        <button type="button" id="btnSaveGroup" class="btn btn-sm btn-success">Simpan</button>
                        <%}%>
                    </div>
                </div>
            </div>
        </div>

        <div id="modalCost" class="modal fade" role="dialog">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Cost</h4>
                    </div>
                    <div class="modal-body">
                        <form id="formCost" class="form-horizontal">
                            <input type="hidden" name="FRM_FIELD_DATA_FOR" value="saveProductionCost">
                            <input type="hidden" name="command" value="<%=Command.SAVE%>">
                            <input type="hidden" id="costId" name="<%=FrmProductionCost.fieldNames[FrmProductionCost.FRM_FIELD_PRODUCTION_COST_ID]%>" value="">
                            <input type="hidden" id="costGroupId" name="<%=FrmProductionCost.fieldNames[FrmProductionCost.FRM_FIELD_PRODUCTION_GROUP_ID]%>" value="">

                            <div class="row">
                                <div class="col-sm-12">
                                    <p></p>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label">Material</label>
                                        <div class="col-sm-10">
                                            <div class="input-group">
                                                <span class="input-group-addon btn btn-primary btnSearchCost"><i class="fa fa-search"></i></span>
                                                <input type="text" id="costItemName" class="form-control input-sm" value="">
                                                <input type="hidden" id="costItemId" class="form-control input-sm" name="<%=FrmProductionCost.fieldNames[FrmProductionCost.FRM_FIELD_MATERIAL_ID]%>" value="">
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label class="col-sm-2 control-label">Qty</label>
                                        <div class="col-sm-3">
                                            <input type="text" id="costQty" class="form-control input-sm" name="<%=FrmProductionCost.fieldNames[FrmProductionCost.FRM_FIELD_STOCK_QTY]%>" value="">
                                        </div>

                                        <label class="col-sm-2 control-label">Value</label>
                                        <div class="col-sm-5">
                                            <div class="input-group">
                                                <span class="input-group-addon">Rp</span>
                                                <input type="text" id="costValue" class="form-control input-sm" name="<%=FrmProductionCost.fieldNames[FrmProductionCost.FRM_FIELD_STOCK_VALUE]%>" value="">
                                            </div>
                                        </div>
                                    </div>

                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <%if (production.getProductionStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) {%>
                        <button type="button" id="btnDeleteCost" class="btn btn-sm btn-danger hidden pull-left">Hapus</button>
                        <button type="button" id="btnSaveCost" class="btn btn-sm btn-success">Simpan</button>
                        <%}%>
                    </div>
                </div>
            </div>
        </div>

        <div id="modalProduct" class="modal fade" role="dialog">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Product</h4>
                    </div>
                    <div class="modal-body">
                        <form id="formProduct" class="form-horizontal">
                            <input type="hidden" name="FRM_FIELD_DATA_FOR" value="saveProductionProduct">
                            <input type="hidden" name="command" value="<%=Command.SAVE%>">
                            <input type="hidden" id="productId" name="<%=FrmProductionProduct.fieldNames[FrmProductionProduct.FRM_FIELD_PRODUCTION_PRODUCT_ID]%>" value="">
                            <input type="hidden" id="productGroupId" name="<%=FrmProductionProduct.fieldNames[FrmProductionProduct.FRM_FIELD_PRODUCTION_GROUP_ID]%>" value="">
                            
                            <div class="row">
                                <div class="col-sm-12">
                                    <p></p>
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label">Material</label>
                                        <div class="col-sm-10">
                                            <div class="input-group">
                                                <span class="input-group-addon btn btn-primary btnSearchProduct"><i class="fa fa-search"></i></span>
                                                <input type="text" id="productItemName" class="form-control input-sm" value="">
                                                <input type="hidden" class="form-control input-sm" id="productItemId" name="<%=FrmProductionProduct.fieldNames[FrmProductionProduct.FRM_FIELD_MATERIAL_ID]%>" value="">
                                            </div>
                                        </div>
                                    </div>
                                            
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label">Type</label>
                                        <div class="col-sm-5">
                                            <select class="form-control input-sm" id="itemType" name="<%=FrmProductionProduct.fieldNames[FrmProductionProduct.FRM_FIELD_MATERIAL_TYPE]%>">
                                                <%=optionMatType%>
                                            </select>
                                        </div>
                                        <div class="col-sm-5">
                                            <div class="input-group">
                                                <span class="input-group-addon">Distribution</span>
                                                <input type="number" readonly="" id="distribution" class="form-control input-sm" name="<%=FrmProductionProduct.fieldNames[FrmProductionProduct.FRM_FIELD_PERIOD_DISTRIBUTION]%>" value="0">
                                            </div>
                                        </div>
                                    </div>
                                            
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label">Cost</label>
                                        <div class="col-sm-3">
                                            <select class="form-control input-sm" id="costType" name="<%=FrmProductionProduct.fieldNames[FrmProductionProduct.FRM_FIELD_COST_TYPE]%>">
                                                <%=optionCostType%>
                                            </select>
                                        </div>
                                        <div class="col-sm-3">
                                            <div class="input-group">
                                                <input type="text" readonly="" id="costPct" class="form-control input-sm" name="<%=FrmProductionProduct.fieldNames[FrmProductionProduct.FRM_FIELD_COST_PCT]%>" value="0">
                                                <span class="input-group-addon">%</span>
                                            </div>
                                        </div>
                                    </div>
                                            
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label">Qty</label>
                                        <div class="col-sm-3">
                                            <input type="text" id="productQty" class="form-control input-sm" name="<%=FrmProductionProduct.fieldNames[FrmProductionProduct.FRM_FIELD_STOCK_QTY]%>" value="0">
                                        </div>
                                    </div>
                                            
                                    <div class="form-group">
                                        <label class="col-sm-2 control-label">Sales</label>
                                        <div class="col-sm-4">
                                            <div class="input-group">
                                                <span class="input-group-addon">Rp</span>
                                                <input type="text" id="salesValue" class="form-control input-sm" name="<%=FrmProductionProduct.fieldNames[FrmProductionProduct.FRM_FIELD_SALES_VALUE]%>" value="0">
                                            </div>
                                        </div>
                                    </div>

                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <%if (production.getProductionStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) {%>
                        <button type="button" id="btnDeleteProduct" class="btn btn-sm btn-danger hidden pull-left">Hapus</button>
                        <button type="button" id="btnSaveProduct" class="btn btn-sm btn-success">Simpan</button>
                        <%}%>
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

        <div id="modalConfirm" class="modal fade" role="dialog">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Konfirmasi</h4>
                    </div>
                    <div class="modal-body">
                        <table class="table table-striped tabel_data">
                            <tr class="info">
                                <th>No.</th>
                                <th>SKU</th>
                                <th>Name</th>
                                <th>Receive Doc Not Closed</th>
                                <th>Production Doc Not Closed</th>
                                <th>Production Qty</th>
                                <th>Production Cost</th>
                                <th>Current Stock</th>
                                <th>Current HPP</th>
                                <th>New Stock</th>
                                <th>New HPP</th>
                            </tr>
                            <tbody id="appendListConfirm">
                                
                            </tbody>
                        </table>
                    </div>
                    <div class="modal-footer">
                        <%if (production.getProductionStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) {%>
                        <button type="button" id="btnConfirmPosting" class="btn btn-sm btn-success"><i class="fa fa-check"></i> Posting</button>
                        <%}%>
                    </div>
                </div>
            </div>
        </div>

    </body>

    <script>
        var getDataFunction = function (onDone, onSuccess, approot, command, dataSend, servletName, dataAppendTo, notification, dataType) {
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
        };

        function dataTablesOptions(elementIdParent, elementId, servletName, dataFor, callBackDataTables) {
            $(elementIdParent).find('table').addClass('table-bordered table-striped table-hover').attr({'id': elementId});
            var sku = $("#sku").val();
            var materialName = $("#materialName").val();
            var locationId = "<%=production.getLocationFromId()%>";

            $("#" + elementId).dataTable({"bDestroy": true,
                "ordering": false,
                "iDisplayLength": 10,
                "bProcessing": true,
                "oLanguage": {
                    "sProcessing": "<div class='col-sm-12'><div class='progress progress-striped active'><div class='progress-bar progress-bar-primary' style='width: 100%'><b>Please Wait...</b></div></div></div>"
                },
                "bServerSide": true,
                "sAjaxSource": "<%= approot%>/" + servletName + "?command=<%= Command.LIST%>&FRM_FIELD_DATA_FOR=" + dataFor + "&FRM_FLD_APP_ROOT=<%=approot%>"
                        + "&where_sku=" + sku + "&where_name=" + materialName + "&location_id=" + locationId,
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

        function modalShowOnMiddleIframe(modalId) {
            $(modalId).on('show.bs.modal', function (e) {
                if (window.top.document.querySelector('iframe')) {
                    $(modalId).css('top', window.top.scrollY); //set modal position
                }
            });
        }

        modalShowOnMiddleIframe('#modalGroup');
        modalShowOnMiddleIframe('#modalCost');
        modalShowOnMiddleIframe('#modalProduct');
        modalShowOnMiddleIframe('#modalItem');

        function reloadPage() {
            window.location = "production_edit.jsp?command=<%=Command.EDIT%>&production_id=<%=production.getOID()%>";
        }

        function loadButton(buttonId) {
            $(buttonId).html("<i class='fa fa-spinner fa-pulse'></i> Tunggu...").attr({"disabled": true});
        }

        function getTemplate() {
            var batchNumber = $('#selectBatch').val();
            
            var dataSend = {
                "command": "<%=Command.NONE%>",
                "FRM_FIELD_DATA_FOR": "getOptionTemplate",
                "batch_number": batchNumber
            };
    
            onDone = function (data) {
                $('#selectTemplate').html(data.FRM_FIELD_HTML);
                getPeriod();
            };
            onSuccess = function (data) {
            };
            getDataFunction(onDone, onSuccess, "<%=approot%>", "", dataSend, "AjaxProductionChain", "", false, "json");
        }

        function getPeriod() {
            var batchNumber = $('#selectBatch').val();
            var templateId = $('#selectTemplate').val();
            
            var dataSend = {
                "command": "<%=Command.NONE%>",
                "FRM_FIELD_DATA_FOR": "getOptionPeriod",
                "batch_number": batchNumber,
                "chain_main_id": templateId
            };
            onDone = function (data) {
                $('#selectPeriod').html(data.FRM_FIELD_HTML);
                $('#groupIndex').val(data.NEXT_INDEX);
            };
            onSuccess = function (data) {
            };
            getDataFunction(onDone, onSuccess, "<%=approot%>", "", dataSend, "AjaxProductionChain", "", false, "json");
        }

        function selectItem(itemId, itemSku, itemName, unitId, stockValue, salesValue) {
            var type = $("#modalType").val();
            if (type === "cost") {
                $('#modalCost #costItemId').val(itemId);
                $('#modalCost #costItemName').val(itemName);
                $('#modalCost #costValue').val(stockValue);
            } else if (type === "product") {
                $('#modalProduct #productItemId').val(itemId);
                $('#modalProduct #productItemName').val(itemName);
                $('#modalProduct #salesValue').val(salesValue);
            }
            $('#modalItem').modal('hide');
        }

        function searchItemByCost() {
            var name = $('#costItemName').val();
            $("#sku").val(name);
            $("#materialName").val(name);
            $('#modalItem #modalType').val('cost');
            $('#modalItem').modal('show');
            dataTablesOptions("#itemElement", "tableItemElement", "AjaxProductionChain", "listItemCostSales", null);
        }

        function searchItemByProduct() {
            var name = $('#productItemName').val();
            $("#sku").val(name);
            $("#materialName").val(name);
            $('#modalItem #modalType').val('product');
            $('#modalItem').modal('show');
            dataTablesOptions("#itemElement", "tableItemElement", "AjaxProductionChain", "listItemCostSales", null);
        }
        
        function setDataGroup(groupId) {
            var dataSend = {
                "command": "<%=Command.NONE%>",
                "FRM_FIELD_DATA_FOR": "getDataOptionGroup",
                "group_id": groupId
            };
            onDone = function (data) {
                $('#modalGroup #groupId').val(groupId);
                $('#modalGroup #selectBatch').html(data.OPTION_BATCH);
                $('#modalGroup #selectTemplate').html(data.OPTION_TEMPLATE);
                $('#modalGroup #selectPeriod').html(data.OPTION_PERIOD);
                $('#modalGroup #groupIndex').val(data.GROUP_INDEX);
                $('#modalGroup #startDate').val(data.FRM_FIELD_DATE_START);
                $('#modalGroup #endDate').val(data.FRM_FIELD_DATE_END);
                
                if (""+groupId !== "0" && ""+groupId !== "") {
                    $('#modalGroup #btnDeleteGroup').removeClass('hidden');
                } else {
                    $('#modalGroup #btnDeleteGroup').addClass('hidden');
                }
                $('#modalGroup').modal('show');
            };
            onSuccess = function (data) {
            };
            getDataFunction(onDone, onSuccess, "<%=approot%>", "", dataSend, "AjaxProductionChain", "", false, "json");
        }
        
        function getConfirmList() {
            var dataSend = {
                "command": "<%=Command.NONE%>",
                "FRM_FIELD_DATA_FOR": "getConfirmList",
                "FRM_FIELD_OID": "<%=production.getOID()%>"
            };
            onDone = function (data) {
                $('#appendListConfirm').html(data.FRM_FIELD_HTML);
                $('#modalConfirm').modal('show');
                $('#btnSaveProduction').html('<i class="fa fa-save"></i>&nbsp; Simpan').attr({"disabled":false});
            };
            onSuccess = function (data) {
            };
            getDataFunction(onDone, onSuccess, "<%=approot%>", "", dataSend, "AjaxProductionChain", "", false, "json");
        }
        
        $('.datePicker').datetimepicker({
            "format":"yyyy-mm-dd hh:ii:ss",
            "todayBtn":true,
            "autoclose":true
        });
        
        $('#selectBatch').select2({});

        $('#btnSaveProduction').click(function () {
            var date = $('#formProduction #dateProd').val();
            if (date === "") {
                alert("Tanggal produksi harus diisi !");
                $('#formProduction #dateProd').focus();
                return false;
            }
            loadButton(this);
            var status = $('#statusProduction').val();
            if (status === "<%=I_DocStatus.DOCUMENT_STATUS_FINAL%>") {
                getConfirmList();
            } else {
                $('#formProduction').submit();
            }
        });
        
        $('#btnConfirmPosting').click(function () {
            loadButton(this);
            $('#formProduction').submit();
        });
        
        $('#btnBack').click(function () {
            loadButton(this);
        });
        
        $('#btnDeleteProduction').click(function () {
            if (confirm("Hapus produksi ?")) {
                $('#formProduction #command').val("<%=Command.DELETE%>");
                $('#formProduction').submit();
            }
        });

        $('#btnAddGroup').click(function () {
            setDataGroup(0);
        });
        
        $('.editGroup').click(function () {
            if ("<%=production.getProductionStatus()%>" !== "<%=I_DocStatus.DOCUMENT_STATUS_DRAFT%>") {
                return false;
            }
            setDataGroup($(this).data('group_id'));
        });

        $('#selectBatch').change(function () {
            getTemplate();
        });

        $('#selectTemplate').change(function () {
            getPeriod();
        });
        
        $('#selectPeriod').change(function () {
            var index = $(this).find('option:selected').data('index');
            $('#groupIndex').val(index);
        });
        
        $('#startDate').change(function() {
            var dataSend = {
                "command": "<%=Command.NONE%>",
                "FRM_FIELD_DATA_FOR": "setEndDatePeriod",
                "FRM_FIELD_OID": $('#modalGroup #selectPeriod').val(),
                "FRM_FIELD_DATE_START": $('#modalGroup #startDate').val()
            };
            onDone = function (data) {
                $('#modalGroup #endDate').val(data.FRM_FIELD_DATE_END);
            };
            onSuccess = function (data) {
            };
            getDataFunction(onDone, onSuccess, "<%=approot%>", "", dataSend, "AjaxProductionChain", "", false, "json");
        });

        $('#btnSaveGroup').click(function () {
            var templateId = $('#selectTemplate').val();
            var periodId = $('#selectPeriod').val();
            if (templateId === "0") {
                alert("Pilih template !");
                $('#selectTemplate').focus();
                return false;
            }
            if (periodId === "0") {
                alert("Pilih periode !");
                $('#selectPeriod').focus();
                return false;
            }
            var button = $(this).html();
            loadButton(this);
            var dataSend = $('#formGroup').serialize();
            onDone = function (data) {
                $('#btnSaveGroup').html(button).attr({"disabled":false});
                if (data.ERROR_NUMBER_RETURN > 0) {
                    alert(data.MESSAGE_RETURN);
                } else {
                    $('#modalGroup').modal('hide');
                    reloadPage();
                }
            };
            onSuccess = function (data) {
            };
            getDataFunction(onDone, onSuccess, "<%=approot%>", "", dataSend, "AjaxProductionChain", "", false, "json");
        });

        $('#btnDeleteGroup').click(function () {
            if (confirm("Hapus periode ?")) {
                var button = $(this).html();
                loadButton(this);
                var groupId = $('#modalGroup #groupId').val();
                var dataSend = {
                    "command": "<%=Command.DELETE%>",
                    "FRM_FIELD_DATA_FOR": "deleteProductionGroup",
                    "FRM_FIELD_OID": groupId
                };
                onDone = function (data) {
                    $('#btnDeleteGroup').html(button).attr({"disabled":false});
                    if (data.ERROR_NUMBER_RETURN > 0) {
                        alert(data.MESSAGE_RETURN);
                    } else {
                        $('#modalGroup').modal('hide');
                        reloadPage();
                    }
                };
                onSuccess = function (data) {
                };
                getDataFunction(onDone, onSuccess, "<%=approot%>", "", dataSend, "AjaxProductionChain", "", false, "json");
            }
        });
        
        $('.btnAddCost').click(function () {
            $('#modalCost #costGroupId').val($(this).data('group_id'));
            $('#modalCost #costId').val(0);
            $('#modalCost #costItemId').val(0);
            $('#modalCost #costItemName').val("");
            $('#modalCost #costQty').val(0);
            $('#modalCost #costValue').val(0);
            $('#modalCost #btnDeleteCost').addClass('hidden');
            $('#modalCost').modal('show');
        });
        
        $('.editCost').click(function () {
            $('#modalCost #costGroupId').val($(this).data('group_id'));
            $('#modalCost #costId').val($(this).data('cost_id'));
            $('#modalCost #costItemId').val($(this).data('item_id'));
            $('#modalCost #costItemName').val($(this).data('item_name'));
            $('#modalCost #costQty').val($(this).data('qty'));
            $('#modalCost #costValue').val($(this).data('value'));
            
            if ($(this).data('cost_id') !== "0" && $(this).data('cost_id') !== "") {
                $('#modalCost #btnDeleteCost').removeClass('hidden');
            } else {
                $('#modalCost #btnDeleteCost').addClass('hidden');
            }
            $('#modalCost').modal('show');
        });

        $('.btnSearchCost').click(function () {
            searchItemByCost();
        });

        $('#costItemName').keyup(function (e) {
            if (e.keyCode === 13) {
                e.preventDefault();
                searchItemByCost();
            }
        });

        $('#btnSaveCost').click(function () {
            var itemId = $('#modalCost #costItemId').val();
            if (itemId === "" || itemId === "0") {
                alert("Pilih material !");
                $('#modalCost #costItemName').focus();
                return false;
            }
            var ok = true;
            var qty = parseFloat($('#modalCost #costQty').val());
            if (isNaN(qty) || qty === 0 || qty === "0" || qty === "") {
                ok = false;
                if (confirm("Nilai quantity 0. Lanjutkan simpan ?")) {
                    ok = true;
                }
            }
            
            if (ok) {
                var button = $(this).html();
                loadButton(this);
                var dataSend = $('#formCost').serialize();
                onDone = function (data) {
                    $('#btnSaveCost').html(button).attr({"disabled":false});
                    if (data.ERROR_NUMBER_RETURN > 0) {
                        alert(data.MESSAGE_RETURN);
                    } else {
                        $('#modalCost').modal('hide');
                        reloadPage();
                    }
                };
                onSuccess = function (data) {
                };
                getDataFunction(onDone, onSuccess, "<%=approot%>", "", dataSend, "AjaxProductionChain", "", false, "json");
            }
        });

        $('#btnDeleteCost').click(function () {
            if (confirm("Hapus data cost ?")) {
                var button = $(this).html();
                loadButton(this);
                var costId = $('#modalCost #costId').val();
                var dataSend = {
                    "command": "<%=Command.DELETE%>",
                    "FRM_FIELD_DATA_FOR": "deleteProductionCost",
                    "FRM_FIELD_OID": costId
                };
                onDone = function (data) {
                    $('#btnDeleteCost').html(button).attr({"disabled":false});
                    if (data.ERROR_NUMBER_RETURN > 0) {
                        alert(data.MESSAGE_RETURN);
                    } else {
                        $('#modalCost').modal('hide');
                        reloadPage();
                    }
                };
                onSuccess = function (data) {
                };
                getDataFunction(onDone, onSuccess, "<%=approot%>", "", dataSend, "AjaxProductionChain", "", false, "json");
            }
        });
        
        $('.btnAddProduct').click(function () {
            $('#modalProduct #productGroupId').val($(this).data('group_id'));
            $('#modalProduct #productId').val(0);
            $('#modalProduct #productItemName').val("");
            $('#modalProduct #productItemId').val("");
            $('#modalProduct #itemType').val(0);
            $('#modalProduct #distribution').val(0);
            $('#modalProduct #productQty').val(0);
            $('#modalProduct #costType').val(0);
            $('#modalProduct #costPct').val(0);
            $('#modalProduct #salesValue').val(0);
            
            if (""+$(this).data('item_type') === "<%=PstProductionProduct.TYPE_NEXT_COST%>") {
                $('#modalProduct #distribution').attr({"readonly":false});
            } else {
                $('#modalProduct #distribution').attr({"readonly":true});
            }
            
            if (""+$(this).data('cost_type') === "<%=PstProductionProduct.COST_TYPE_CUSTOM%>") {
                $('#modalProduct #costPct').attr({"readonly":false});
            } else {
                $('#modalProduct #costPct').attr({"readonly":true});
            }
            
            $('#modalProduct #btnDeleteProduct').addClass('hidden');
            $('#modalProduct').modal('show');
        });
        
        $('.editProduct').click(function () {
            $('#modalProduct #productGroupId').val($(this).data('group_id'));
            $('#modalProduct #productId').val($(this).data('product_id'));
            $('#modalProduct #productItemName').val($(this).data('item_name'));
            $('#modalProduct #productItemId').val($(this).data('item_id'));
            $('#modalProduct #itemType').val($(this).data('item_type'));
            $('#modalProduct #distribution').val($(this).data('distribution'));
            $('#modalProduct #productQty').val($(this).data('qty'));
            $('#modalProduct #costType').val($(this).data('cost_type'));
            $('#modalProduct #costPct').val($(this).data('cost_pct'));
            $('#modalProduct #salesValue').val($(this).data('sales_value'));
            
            if (""+$(this).data('item_type') === "<%=PstProductionProduct.TYPE_NEXT_COST%>") {
                $('#modalProduct #distribution').attr({"readonly":false});
            } else {
                $('#modalProduct #distribution').attr({"readonly":true});
            }
            
            if (""+$(this).data('cost_type') === "<%=PstProductionProduct.COST_TYPE_CUSTOM%>") {
                $('#modalProduct #costPct').attr({"readonly":false});
            } else {
                $('#modalProduct #costPct').attr({"readonly":true});
            }
            
            if ($(this).data('product_id') !== "0" && $(this).data('product_id') !=="") {
                $('#modalProduct #btnDeleteProduct').removeClass('hidden');
            } else {
                $('#modalProduct #btnDeleteProduct').addClass('hidden');
            }
            
            $('#modalProduct').modal('show');
        });

        $('.btnSearchProduct').click(function () {
            searchItemByProduct();
        });

        $('#productItemName').keyup(function (e) {
            if (e.keyCode === 13) {
                e.preventDefault();
                searchItemByProduct();
            }
        });
        
        $('#modalProduct #itemType').change(function() {
            if ($(this).val() === "<%=PstProductionProduct.TYPE_NEXT_COST%>") {
                $('#modalProduct #distribution').attr({"readonly":false});
            } else {
                $('#modalProduct #distribution').attr({"readonly":true});
                $('#modalProduct #distribution').val(0);
            }
        });
        
        $('#modalProduct #costType').change(function() {
            if ($(this).val() === "<%=PstProductionProduct.COST_TYPE_CUSTOM%>") {
                $('#modalProduct #costPct').attr({"readonly":false});
                $('#modalProduct #costPct').focus();
                
                var groupId = $('#modalProduct #productGroupId').val();
                var productId = $('#modalProduct #productId').val();
                var totalPct = 0;
                $('.costPctProduct_'+groupId).each(function () {
                    if ($(this).data('product_id') !== productId) {
                        totalPct += +$(this).val();
                    }
                });
                $('#modalProduct #costPct').val(100 - +totalPct);
            } else {
                $('#modalProduct #costPct').attr({"readonly":true});
                $('#modalProduct #costPct').val(0);
            }
        });
         
        $('#modalProduct #costPct').keyup(function() {
            var groupId = $('#modalProduct #productGroupId').val();
            var productId = $('#modalProduct #productId').val();
            var totalPct = 0;
            $('.costPctProduct_'+groupId).each(function () {
                if ($(this).data('product_id') !== productId) {
                    totalPct += +$(this).val();
                }
            });
            var countPct = +totalPct + +$(this).val();
            if (countPct > 100) {
                alert("Persentase cost (" + countPct + ") lebih dari 100% !");
                $('#modalProduct #costPct').val(0);
            }
        });
        
        $('#btnSaveProduct').click(function () {
            var itemId = $('#modalProduct #productItemId').val();
            if (itemId === "" || itemId === "0") {
                alert("Pilih material !");
                $('#modalProduct #productItemName').focus();
                return false;
            }
            var ok = true;
            var qty = parseFloat($('#modalProduct #productQty').val());
            if (isNaN(qty) || qty === 0 || qty === "0" || qty === "") {
                ok = false;
                if (confirm("Nilai quantity 0. Lanjutkan simpan ?")) {
                    ok = true;
                }
            }
            
            if (ok) {
                var button = $(this).html();
                loadButton(this);
                var dataSend = $('#formProduct').serialize();
                onDone = function (data) {
                    $('#btnSaveProduct').html(button).attr({"disabled":false});
                    if (data.ERROR_NUMBER_RETURN > 0) {
                        alert(data.MESSAGE_RETURN);
                    } else {
                        $('#modalProduct').modal('hide');
                        reloadPage();
                    }
                };
                onSuccess = function (data) {
                };
                getDataFunction(onDone, onSuccess, "<%=approot%>", "", dataSend, "AjaxProductionChain", "", false, "json");
            }
        });
        
        $('#btnDeleteProduct').click(function () {
            if (confirm("Hapus data product ?")) {
                var button = $(this).html();
                loadButton(this);
                var productId = $('#modalProduct #productId').val();
                var dataSend = {
                    "command": "<%=Command.DELETE%>",
                    "FRM_FIELD_DATA_FOR": "deleteProductionProduct",
                    "FRM_FIELD_OID": productId
                };
                onDone = function (data) {
                    $('#btnDeleteProduct').html(button).attr({"disabled":false});
                    if (data.ERROR_NUMBER_RETURN > 0) {
                        alert(data.MESSAGE_RETURN);
                    } else {
                        $('#modalProduct').modal('hide');
                        reloadPage();
                    }
                };
                onSuccess = function (data) {
                };
                getDataFunction(onDone, onSuccess, "<%=approot%>", "", dataSend, "AjaxProductionChain", "", false, "json");
            }
        });
        
        $('#statusProduction').unbind().change(function() {
            if ($(this).val() === "<%=I_DocStatus.DOCUMENT_STATUS_FINAL%>") {
                var ok = true;
                $('.tabel_cost .costQty').each(function() {
                    var qty = $(this).html().replace(/,/g , "");
                    if (qty === 0 || qty === "0" || qty === "") {
                        alert("Terdapat quantity cost dengan nilai 0 !");
                        ok = false;
                        return;
                    }
                });
                if (!ok) {
                    return;
                }
                $('.tabel_product .productQty').each(function() {
                    var qty = $(this).html().replace(/,/g , "");
                    if (qty === 0 || qty === "0" || qty === "") {
                        alert("Terdapat quantity product dengan nilai 0 !");
                        return;
                    }
                });
            }
        });
        
    </script>
    <script>
        $(document).ready(function(){
            if("<%=error%>" !== "0") {
                alert("<%=message%>");
            }
        });
    </script>
</html>

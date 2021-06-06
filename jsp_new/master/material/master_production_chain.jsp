<%-- 
    Document   : master_production_chain
    Created on : Jun 28, 2019, 4:40:24 PM
    Author     : IanRizky
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../../main/javainit.jsp" %>
<%int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER, AppObjInfo.OBJ_TRANSFER);%>
<%@ include file = "../../main/checkuser.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="../../styles/bootstrap/css/bootstrap.min.css"/>
        <link rel="stylesheet" type="text/css" href="../../styles/dist/css/AdminLTE.css"/>
        <link rel="stylesheet" type="text/css" href="../../styles/font-awesome/font-awesome.css"/>
        <link rel="stylesheet" type="text/css" href="../../styles/select2/css/select2.min.css" />
        <link rel="stylesheet" media="screen" href="../../styles/datepicker/bootstrap-datetimepicker.min.css"/>
        <link rel="stylesheet" type="text/css" href="../../styles/plugin/datatables/dataTables.bootstrap.css"/>
        <style>
            
            body {background-color: #eeeeee}
            .table {font-size: 12px;}
            .table th {
                text-align: center;
                background-color: lightgray;                
            }
            .table-condensed {font-size: 14px}
            .tabel_biaya th {background-color: #f39c12; color: white;}
            .tabel_penjualan th {background-color: #00a65a; color: white;}
            .tabel_perhitungan_hpp th {background-color: #3c8dbc; color: white;}
            
            .modal-header {padding: 10px 20px; border-color: lightgray}
            .modal-footer {padding: 10px 20px; margin: 0px; border-color: lightgray}
            .box .box-header, .box .box-footer {border-color: lightgray}
            
            #tableItemElement th {padding: 8px}
            
        </style>
    </head>
    <body>
        <div class="col-sm-12">
            <h4>Production Chain</h4>
            
            <div class="box box-default">
                
                <div class="box-header with-border">
                    <h3 class="box-title">Data Main</h3>
                </div>
                
                <div class="box-body">
                    <form id="formCogsMain" class="form-horizontal">
                        <input type="hidden" id="command" name="command" value="<%=iCommand%>">
                        <input type="hidden" id="" name="hidden_calc_cogs_main_id" value="<%=calcCogsMain.getOID() %>">
                        
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Periode Biaya</label>
                            <div class="col-sm-2">
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                    <input type="text" placeholder="Tanggal awal" class="form-control input-sm datepick" name="<%=FrmCalcCogsMain.fieldNames[FrmCalcCogsMain.FRM_FIELD_COST_DATE_START] %>" value="<%=tglCostAwal %>">
                                </div>
                            </div>
                            <div class="col-sm-2">
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                    <input type="text" placeholder="Tanggal akhir" class="form-control input-sm datepick" name="<%=FrmCalcCogsMain.fieldNames[FrmCalcCogsMain.FRM_FIELD_COST_DATE_END] %>" value="<%=tglCostAkhir %>">
                                </div>
                            </div>                    
                        </div>

                        <div class="form-group">
                            <label class="col-sm-2 control-label">Lokasi Biaya</label>
                            <div class="col-sm-10">
                                <select multiple="" class="form-control input-sm multiSelect" name="MULTIPLE_LOCATION_COST">
                                    <%
                                        Vector listLokasiCost = PstLocation.list(0, 0, "", "" + PstLocation.fieldNames[PstLocation.FLD_NAME]);
                                        for (int i = 0; i < listLokasiCost.size(); i++) {
                                            selected = "";
                                            Location l = (Location) listLokasiCost.get(i);
                                            for (int j = 0; j < listLocationCost.size(); j++) {
                                                if (l.getOID() == listLocationCost.get(j).getLocationId()) {
                                                    selected = "selected";
                                                    break;
                                                }
                                            }

                                    %>
                                    <option <%=selected%> value="<%=l.getOID()%>"><%=l.getName()%></option>
                                    <%
                                        }
                                    %>
                                </select>
                            </div>  
                        </div>

                        <div class="form-group">
                            <label class="col-sm-2 control-label">Periode Penjualan</label>
                            <div class="col-sm-2">
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                    <input type="text" placeholder="Tanggal awal" class="form-control input-sm datepick" name="<%=FrmCalcCogsMain.fieldNames[FrmCalcCogsMain.FRM_FIELD_SALES_DATE_START] %>" value="<%=tglSalesAwal %>">
                                </div>                                
                            </div>
                            <div class="col-sm-2">
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                    <input type="text" placeholder="Tanggal akhir" class="form-control input-sm datepick" name="<%=FrmCalcCogsMain.fieldNames[FrmCalcCogsMain.FRM_FIELD_SALES_DATE_END] %>" value="<%=tglSalesAkhir %>">
                                </div>
                            </div>                
                        </div>
                                
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Lokasi Penjualan</label>
                            <div class="col-sm-10">
                                <select multiple="" class="form-control input-sm multiSelect" name="MULTIPLE_LOCATION_SALES">
                                    <%
                                        Vector listLokasiSales = PstLocation.list(0, 0, "", "" + PstLocation.fieldNames[PstLocation.FLD_NAME]);
                                        for (int i = 0; i < listLokasiSales.size(); i++) {
                                            selected = "";
                                            Location l = (Location) listLokasiSales.get(i);
                                            for (int j = 0; j < listLocationSales.size(); j++) {
                                                if (l.getOID() == listLocationSales.get(j).getLocationId()) {
                                                    selected = "selected";
                                                    break;
                                                }
                                            }

                                    %>
                                    <option <%=selected%> value="<%=l.getOID() %>"><%=l.getName() %></option>
                                    <%
                                        }
                                    %>
                                </select>
                            </div>         
                        </div>
                            
                        <div class="form-group">                                
                            <label class="col-sm-2 control-label">Status</label>
                            <div class="col-sm-2">
                                <%
                                    Vector statusDocVal = new Vector();
                                    Vector statusDocKey = new Vector();

                                    statusDocVal.add(""+I_DocStatus.DOCUMENT_STATUS_DRAFT);
                                    statusDocVal.add(""+I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED);
                                    statusDocVal.add(""+I_DocStatus.DOCUMENT_STATUS_FINAL);

                                    statusDocKey.add(""+I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]);
                                    statusDocKey.add(""+I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED]);
                                    statusDocKey.add(""+I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);
                                %>
                                <%=ControlCombo.draw(FrmCalcCogsMain.fieldNames[FrmCalcCogsMain.FRM_FIELD_STATUS], null, ""+calcCogsMain.getStatus(), statusDocVal, statusDocKey, "", "form-control input-sm")%>
                            </div>
                            
                            <label class="col-sm-2 control-label">Keterangan</label>
                            <div class="col-sm-6">
                                <textarea class="form-control input-sm" name="<%=FrmCalcCogsMain.fieldNames[FrmCalcCogsMain.FRM_FIELD_NOTE] %>"><%=calcCogsMain.getNote() %></textarea>
                            </div>
                        </div>
                        
                    </form>
                            
                    <%if(mainMassage.length() > 0){%>                                                        
                    <div class="<%=messageStyle %>"><%=mainMassage %></div>                                                        
                    <%}%>
                    
                </div>

                <div class="box-footer">
                    <button type="button" id="btnSaveMain" class="btn btn-sm btn-primary"><i class="fa fa-save"></i>&nbsp; Simpan</button>
                    <a href="src_perhitungan_hpp.jsp" id="btnBack" class="btn btn-sm btn-primary"><i class="fa fa-undo"></i>&nbsp; Kembali ke pencarian</a>
                </div>
            </div>
            
            <%if(calcCogsMain.getOID() > 0) {%>
                            
            <div class="box box-warning">
                
                <div class="box-header with-border">
                    <h3 class="box-title">Data Biaya Periode <a><%=Formater.formatDate(calcCogsMain.getCostDateStart(), "dd MMM yyyy") %></a> s/d <a><%=Formater.formatDate(calcCogsMain.getCostDateEnd(), "dd MMM yyyy") %></a></h3>
                </div>

                <div class="box-body">
                    <form id="formCogsCost" class="form-horizontal">
                        <input type="hidden" id="command" name="command" value="<%=iCommand%>">
                        <input type="hidden" id="" name="hidden_calc_cogs_main_id" value="<%=calcCogsMain.getOID() %>">
                        
                        <input type="hidden" id="commandCost" name="command_cost" value="">
                        <input type="hidden" id="cost_id" name="hidden_calc_cogs_cost_id" value="">
                        <input type="hidden" name="<%=FrmCalcCogsCost.fieldNames[FrmCalcCogsCost.FRM_FIELD_CALC_COGS_MAIN_ID] %>" value="<%=calcCogsMain.getOID()%>">
                        <input type="hidden" id="cost_item" name="<%=FrmCalcCogsCost.fieldNames[FrmCalcCogsCost.FRM_FIELD_MATERIAL_ID] %>" value="">
                        <input type="hidden" id="cost_unitid" name="<%=FrmCalcCogsCost.fieldNames[FrmCalcCogsCost.FRM_FIELD_UNIT_ID] %>" value="">
                        
                        <table class="table table-bordered tabel_biaya">
                            <tr>
                                <th style="width: 1%">No.</th>
                                <th>SKU</th>
                                <th>Nama Item</th>
                                <th>Unit</th>
                                <th>Harga @ (Rp)</th>
                                <th>Qty</th>
                                <th>Sub Total (Rp)</th>
                            </tr>                            
                            <%
                                double grandSubTotalCost = 0;
                                for (int i = 0; i < listCogsCost.size(); i++) {
                                    CalcCogsCost cogsCost = (CalcCogsCost) listCogsCost.get(i);
                                    
                                    Material m = new Material();
                                    if (cogsCost.getMaterialId() > 0 && PstMaterial.checkOID(cogsCost.getMaterialId())) {
                                        m = PstMaterial.fetchExc(cogsCost.getMaterialId());
                                    }
                                    Unit u = new Unit();
                                    if (cogsCost.getUnitId() > 0 && PstUnit.checkOID(cogsCost.getUnitId())) {
                                        u = PstUnit.fetchExc(m.getDefaultStockUnitId());
                                    }
                                    grandSubTotalCost += cogsCost.getSubTotalCost();
                                    double hargaCost = cogsCost.getSubTotalCost() / cogsCost.getQtyItem();
                            %>
                            <tr>
                                <td class="text-center"><%=(i+1)%></td>
                                <td><%=m.getSku() %><a href="javascript:deleteCost('<%=cogsCost.getOID()%>','<%=m.getSku()%>')" title="Hapus data biaya" class="pull-right text-red"><i class="fa fa-remove"></i></a></td>
                                <td><%=m.getName()%></td>
                                <td class="text-center"><%=u.getCode() %></td>
                                <td class="text-right"><%=String.format("%,.0f", hargaCost) %></td>
                                <td class="text-right"><%=String.format("%,.0f", cogsCost.getQtyItem()) %></td>
                                <td class="text-right"><%=String.format("%,.0f", cogsCost.getSubTotalCost()) %></td>
                            </tr>
                            <%
                                }
                            %>
                            <tr>
                                <td class="text-right" colspan="6"><b>TOTAL :</b></td>
                                <td class="text-right"><b><%=String.format("%,.0f", grandSubTotalCost)%></b></td>
                            </tr>
                            <tr>                            
                                <td class="text-center"><%=(listCogsCost.size()+1) %></td>
                                <td>
                                    <div class="input-group">
                                        <input type="text" id="cost_sku" class="form-control input-sm" name="COST_SKU" value="<%=costSku%>">
                                        <span class="input-group-addon btn btn-primary btn_src_cost"><i class="fa fa-search"></i></span>
                                    </div>
                                </td>
                                <td>
                                    <div class="input-group">
                                        <input type="text" id="cost_name" class="form-control input-sm" name="COST_NAME" value="<%=costName%>">
                                        <span class="input-group-addon btn btn-primary btn_src_cost"><i class="fa fa-search"></i></span>
                                    </div>
                                </td>
                                <td class="text-center"><div id="cost_unit"></div></td>
                                <td><input type="text" readonly="" id="cost_harga" class="form-control input-sm text-right"></td>
                                <td><input type="text" id="cost_qty" class="form-control input-sm text-right hitungHargaBiaya" name="<%=FrmCalcCogsCost.fieldNames[FrmCalcCogsCost.FRM_FIELD_QTY_ITEM] %>" value=""></td>                                
                                <td><input type="text" id="cost_subtotal" class="form-control input-sm text-right hitungHargaBiaya" name="<%=FrmCalcCogsCost.fieldNames[FrmCalcCogsCost.FRM_FIELD_SUB_TOTAL_COST] %>" value=""></td>
                            </tr>
                        </table>
                    </form>
                    <%if(costMassage.length() > 0){%>                                                        
                    <div class="<%=costMessageStyle %>"><%=costMassage %></div>                                                        
                    <%}%>
                </div>
                            
                <div class="box-footer">
                    <button type="button" id="btnSaveCost" class="btn btn-sm btn-primary pull-right"><i class="fa fa-save"></i>&nbsp; Simpan Biaya</button>
                </div>
            </div>
                        
            <div class="box box-success">
                <div class="box-header with-border">
                    <h3 class="box-title">Data Penjualan Periode <a><%=Formater.formatDate(calcCogsMain.getSalesDateStart(), "dd MMM yyyy") %></a> s/d <a><%=Formater.formatDate(calcCogsMain.getSalesDateEnd(), "dd MMM yyyy") %></a></h3>
                </div>
                <div class="box-body">
                    <form id="formCogsSales" class="form-horizontal">
                        <input type="hidden" id="command" name="command" value="<%=iCommand%>">
                        <input type="hidden" id="" name="hidden_calc_cogs_main_id" value="<%=calcCogsMain.getOID() %>">
                        
                        <input type="hidden" id="commandSales" name="command_sales" value="">
                        <input type="hidden" id="sales_id" name="hidden_calc_cogs_sales_id" value="">
                        <input type="hidden" name="<%=FrmCalcCogsSales.fieldNames[FrmCalcCogsSales.FRM_FIELD_CALC_COGS_MAIN_ID] %>" value="<%=calcCogsMain.getOID()%>">
                        <input type="hidden" id="sales_item" name="<%=FrmCalcCogsSales.fieldNames[FrmCalcCogsSales.FRM_FIELD_MATERIAL_ID] %>" value="">
                        <input type="hidden" id="sales_unitid" name="<%=FrmCalcCogsSales.fieldNames[FrmCalcCogsSales.FRM_FIELD_UNIT_ID] %>" value="">
                        
                        <table class="table table-bordered tabel_penjualan">
                        <tr>
                            <th style="width: 1%">No.</th>
                            <th>SKU</th>
                            <th>Nama Item</th>
                            <th>Unit</th>
                            <th>Stok Awal</th>
                            <th>Harga @ (Rp)</th>
                            <th>Qty</th>                                                            
                            <th>Sub Total (Rp)</th>
                            <th>Sisa Stok</th>
                            <th>Jumlah Produksi (Rp)</th>
                        </tr>                        
                        <%
                            double grandSubTotalSales = 0;
                            double grandJumlahProduksi = 0;
                            for (int i = 0; i < listCogsSales.size(); i++) {
                                CalcCogsSales cogsSales = (CalcCogsSales) listCogsSales.get(i);
                                Material m = new Material();
                                if (cogsSales.getMaterialId() > 0 && PstMaterial.checkOID(cogsSales.getMaterialId())) {
                                    m = PstMaterial.fetchExc(cogsSales.getMaterialId());
                                }
                                Unit u = new Unit();
                                if (cogsSales.getUnitId() > 0 && PstUnit.checkOID(cogsSales.getUnitId())) {
                                    u = PstUnit.fetchExc(m.getDefaultStockUnitId());
                                }                                
                                
                                double hargaSales = cogsSales.getSubTotalSales() / cogsSales.getQtyItem();
                                double jumlahProduksi = (cogsSales.getStockLeft() + cogsSales.getQtyItem() - cogsSales.getStokAwal()) * hargaSales;
                                
                                grandSubTotalSales += cogsSales.getSubTotalSales();
                                grandJumlahProduksi += jumlahProduksi;
                        %>
                        <tr>
                            <td class="text-center"><%=(i+1)%></td>
                            <td><%=m.getSku() %><a href="javascript:deleteSales('<%=cogsSales.getOID()%>','<%=m.getSku()%>')" title="Hapus data penjualan" class="pull-right text-red"><i class="fa fa-remove"></i></a></td>
                            <td><%=m.getName() %></td>
                            <td class="text-center"><%=u.getCode() %></td>
                            <td class="text-right"><%=String.format("%,.0f", cogsSales.getStokAwal()) %></td>
                            <td class="text-right"><%=String.format("%,.0f", hargaSales) %></td>
                            <td class="text-right"><%=String.format("%,.0f", cogsSales.getQtyItem()) %></td>
                            <td class="text-right"><%=String.format("%,.0f", cogsSales.getSubTotalSales()) %></td>
                            <td class="text-right"><%=String.format("%,.0f", cogsSales.getStockLeft()) %></td>
                            <td class="text-right"><%=String.format("%,.0f", jumlahProduksi) %></td>
                        </tr>
                        <%
                            }
                        %>
                        <tr>
                            <td class="text-right" colspan="7"><b>TOTAL :</b></td>
                            <td class="text-right"><b><%=String.format("%,.0f", grandSubTotalSales) %></b></td>
                            <td></td>
                            <td class="text-right"><b><%=String.format("%,.0f", grandJumlahProduksi) %></b></td>
                        </tr>
                        <tr>                            
                            <td class="text-center"><%=(listCogsSales.size()+1) %></td>
                            <td>
                                <div class="input-group">
                                    <input type="text" id="sales_sku" class="form-control input-sm" name="SALES_SKU" value="<%=salesItemSku%>">
                                    <span class="input-group-addon btn btn-primary btn_src_sales"><i class="fa fa-search"></i></span>
                                </div>
                            </td>
                            <td>
                                <div class="input-group">
                                    <input type="text" id="sales_name" class="form-control input-sm" name="SALES_NAME" value="<%=salesItemName%>">
                                    <span class="input-group-addon btn btn-primary btn_src_sales"><i class="fa fa-search"></i></span>
                                </div>
                            </td>
                            <td class="text-center"><div id="sales_unit"></div></td>
                            <td><input type="text" id="sales_stokawal" class="form-control input-sm text-right" name="<%=FrmCalcCogsSales.fieldNames[FrmCalcCogsSales.FRM_FIELD_STOK_AWAL] %>" value=""></td>                                
                            <td><input type="text" readonly="" id="sales_harga" class="form-control input-sm text-right"></td>
                            <td><input type="text" id="sales_qty" class="form-control input-sm text-right hitungHargaJual" name="<%=FrmCalcCogsSales.fieldNames[FrmCalcCogsSales.FRM_FIELD_QTY_ITEM] %>" value=""></td>                                
                            <td><input type="text" id="sales_subtotal" class="form-control input-sm text-right hitungHargaJual" name="<%=FrmCalcCogsSales.fieldNames[FrmCalcCogsSales.FRM_FIELD_SUB_TOTAL_SALES] %>" value=""></td>
                            <td><input type="text" id="sales_sisastok" class="form-control input-sm text-right" name="<%=FrmCalcCogsSales.fieldNames[FrmCalcCogsSales.FRM_FIELD_STOCK_LEFT] %>" value=""></td>
                            <td><input type="text" readonly="" id="sales_totalvalue" class="form-control input-sm text-right"></td>
                        </tr>
                    </table>
                    </form>
                    <%if(salesMassage.length() > 0){%>                                                        
                    <div class="<%=salesMessageStyle %>"><%=salesMassage %></div>                                                        
                    <%}%>
                </div>
                <div class="box-footer">
                    <button type="button" id="btnSaveSales" class="btn btn-sm btn-primary pull-right"><i class="fa fa-save"></i>&nbsp; Simpan Penjualan</button>
                </div>
            </div>
            
            <%if(!listCogsCost.isEmpty() && !listCogsSales.isEmpty()) {%>
                
            <div class="box box-primary">
                <div class="box-header with-border">
                    <h3 class="box-title">Perhitungan HPP</h3>
                </div>
                <div class="box-body">
                    <table class="table table-bordered tabel_perhitungan_hpp">
                        <tr>
                            <th style="width: 1%">No.</th>
                            <th>SKU</th>
                            <th>Nama Item</th>
                            <th>Unit</th>
                            <th>Bobot</th>
                            <th>Total (Rp)</th>
                            <th>HPP (Rp)</th>
                            <th>Harga Saat Ini (Rp)</th>
                            <th style="width: 1%">Aksi</th>
                        </tr>
                        <%                            
                            for (int i = 0; i < listCogsSales.size(); i++) {
                                CalcCogsSales cogsSales = (CalcCogsSales) listCogsSales.get(i);
                                Material m = new Material();
                                if (cogsSales.getMaterialId() > 0 && PstMaterial.checkOID(cogsSales.getMaterialId())) {
                                    m = PstMaterial.fetchExc(cogsSales.getMaterialId());
                                }
                                Unit u = new Unit();
                                if (m.getDefaultStockUnitId() > 0 && PstUnit.checkOID(m.getDefaultStockUnitId())) {
                                    u = PstUnit.fetchExc(m.getDefaultStockUnitId());
                                }
                                double hargaSales = cogsSales.getSubTotalSales() / cogsSales.getQtyItem();
                                double jumlahProduksi = (cogsSales.getStockLeft() + cogsSales.getQtyItem() - cogsSales.getStokAwal()) * hargaSales;
                                double bobot = jumlahProduksi / grandJumlahProduksi * 100;
                                double total = grandSubTotalCost * bobot / 100;
                                double hpp = total / cogsSales.getQtyItem();
                                double hargaSebelum = m.getAveragePrice();
                        %>
                        <tr>
                            <td class="text-center"><%=(i+1)%></td>
                            <td><%=m.getSku() %></td>
                            <td><%=m.getName() %></td>
                            <td class="text-center"><%=u.getCode() %></td>
                            <td class="text-right"><%=String.format("%,.0f", bobot)%>%</td>
                            <td class="text-right"><%=String.format("%,.0f", total)%></td>
                            <td class="text-right"><%=String.format("%,.0f", hpp)%></td>
                            <td class="text-right"><%=String.format("%,.0f", hargaSebelum)%></td>
                            <td class="text-center">
                                <button type="button" class="btn btn-xs btn-success btn_updateharga" data-itemid="<%=m.getOID()%>" data-hargabaru="<%=hpp%>">Update Harga</button>
                                <!--input type="checkbox" name="MULTI_UPDATE_ITEM_ID" value="<%=m.getOID()%>"-->
                            </td>
                        </tr>
                        <%
                            }
                        %>
                        <!--tr>
                            <td colspan="9" class="text-right">                                
                                <button type="button" id="btnUpdateHarga" class="btn btn-sm btn-success"><i class="fa fa-check"></i>&nbsp; Update Semua Harga</button>
                            </td>
                        </tr-->                        
                    </table>
                    <br>
                    <p><%=updatePriceMessage%></p>
                </div>
            </div>
                
            <%}%>
            
            <%}%>
            
        </div>
            
            <%
                String modalTitle = "";
                String kolomTitle = "";
                if (showModalItem == 1) {
                    modalTitle = "Data Biaya Periode <a>" + Formater.formatDate(calcCogsMain.getCostDateStart(), "dd MMM yyyy") + "</a> s/d <a>" + Formater.formatDate(calcCogsMain.getCostDateEnd(), "dd MMM yyyy") + "</a>";
                    kolomTitle = "Total Biaya";                    
                } else if (showModalItem == 2) {
                    modalTitle = "Data Penjualan Periode <a>" + Formater.formatDate(calcCogsMain.getSalesDateStart(), "dd MMM yyyy") + "</a> s/d <a>" + Formater.formatDate(calcCogsMain.getSalesDateEnd(), "dd MMM yyyy" + "</a>");
                    kolomTitle = "Total Penjualan";
                }
            %>
        
        <div id="modalItem" class="modal fade" role="dialog">
            <div class="modal-dialog modal-lg">

                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title"><%=modalTitle %></h4>
                    </div>
                    <div class="modal-body">
                        <div id="itemElement">
                            <table class="table table-bordered table-striped">
                                <thead>
                                    <tr>
                                        <th style="width: 1%">No.</th>
                                        <th>SKU</th>
                                        <th>Nama Item</th>
                                        <th>Unit</th>
                                        <%if (showModalItem == 2) {%>
                                        <th>Stok Awal</th>
                                        <%}%>
                                        <th>Harga</th>
                                        <th>Qty</th>
                                        <%if (showModalItem == 2) {%>
                                        <th>Sisa Stok</th>
                                        <%}%>
                                        <th><%=kolomTitle %></th>
                                        <%if (showModalItem == 2) {%>
                                        <th>Nilai Produksi</th>
                                        <%}%>
                                    </tr>
                                </thead>
                            </table>
                        </div>
                        <%--
                        <table class="table table-bordered">
                            <tr>
                                <th style="width: 1%">No.</th>
                                <th>SKU</th>
                                <th>Nama Item</th>
                                <th>Unit</th>
                                <%if (showModalItem == 2) {%>
                                <th>Stok Awal</th>
                                <%}%>
                                <th>Harga</th>
                                <th>Qty</th>
                                <%if (showModalItem == 2) {%>
                                <th>Sisa Stok</th>
                                <%}%>
                                <th><%=kolomTitle %></th>
                                <%if (showModalItem == 2) {%>
                                <th>Nilai Produksi</th>
                                <%}%>
                            </tr>
                            <%                                
                                if (showModalItem == 0) {
                                    listItem = new Vector();
                                }
                                
                                for (int i = 0; i < listItem.size(); i++) {
                                    Material m = (Material) listItem.get(i);
                                    Unit u = new Unit();
                                    if (m.getDefaultStockUnitId() > 0 && PstUnit.checkOID(m.getDefaultStockUnitId())) {
                                        u = PstUnit.fetchExc(m.getDefaultStockUnitId());
                                    }
                                    
                                    double harga = 0;
                                    double stokAwal = 0;
                                    double sisaStok = 0;
                                    double jumlah = 0;
                                    double subTotal = 0;
                                    double nilaiProduksi = 0;
                                    
                                    if (showModalItem == 1) {
                                        try {
                                            //get data lokasi biaya
                                            String lokasiCosting = "";
                                            for (int loc = 0; loc < listLocationCost.size(); loc++) {
                                                long idLocation = listLocationCost.get(loc).getLocationId();
                                                if (loc > 0) {
                                                    lokasiCosting += ",";
                                                }
                                                lokasiCosting += "" + idLocation;
                                            }

                                            //get data qty item biaya dan total biaya
                                            Vector listDataBiaya = SessMatCostingStokFisik.getTotalCosting(m.getOID(), lokasiCosting, calcCogsMain.getCostDateStart(), calcCogsMain.getCostDateEnd());
                                            for (int j = 0; j < listDataBiaya.size(); j++) {
                                                ArrayList<Double> aData = (ArrayList) listDataBiaya.get(j);
                                                jumlah += aData.get(0);
                                                subTotal += aData.get(1);
                                            }
                                        } catch (Exception e) {
                                            System.out.println(e.getMessage());
                                        }
                                    } else if (showModalItem == 2) {
                                        try {
                                            //kurangi tgl start sales 1 detik untuk mencari data stok awal
                                            Date startSales = calcCogsMain.getSalesDateStart();
                                            Calendar c = Calendar.getInstance();
                                            c.setTime(startSales);
                                            c.add(Calendar.SECOND, -1);
                                            Date beforeSales = c.getTime();

                                            //tambah tgl end sales 1 detik untuk mencari data sisa stok
                                            Date endSales = calcCogsMain.getSalesDateEnd();
                                            c.setTime(endSales);
                                            c.add(Calendar.SECOND, 1);
                                            Date afterSales = c.getTime();

                                            //get data lokasi jual
                                            String lokasiSales = "";
                                            for (int loc = 0; loc < listLocationSales.size(); loc++) {
                                                long idLocation = listLocationSales.get(loc).getLocationId();
                                                if (loc > 0) {
                                                    lokasiSales += ",";
                                                }
                                                lokasiSales += "" + idLocation;
                                                /*
                                                Vector docStatus = new Vector();
                                                docStatus.add(""+I_DocStatus.DOCUMENT_STATUS_CLOSED);
                                                docStatus.add(""+I_DocStatus.DOCUMENT_STATUS_POSTED);
                                                SrcStockCard srcStockCard = new SrcStockCard();
                                                srcStockCard.setEndDate(beforeSales);
                                                srcStockCard.setLocationId(idLocation);
                                                srcStockCard.setMaterialId(m.getOID());
                                                srcStockCard.setDocStatus(docStatus);
                                                Vector records = SessStockCard.createHistoryStockCard(srcStockCard);
                                                StockCardReport stockCrp = (StockCardReport)records.get(0);
                                                stokAwal += stockCrp.getQty();
                                                */
                                                stokAwal += SessMatCostingStokFisik.qtyMaterialBasedOnStockCard(m.getOID(), idLocation, beforeSales, 0);
                                                sisaStok += SessMatCostingStokFisik.qtyMaterialBasedOnStockCard(m.getOID(), idLocation, afterSales, 0);

                                                //get data qty item jual dan total penjualan
                                                SrcSaleReport srcSaleReport = new SrcSaleReport();
                                                srcSaleReport.setDateFrom(calcCogsMain.getSalesDateStart());
                                                srcSaleReport.setDateTo(calcCogsMain.getSalesDateEnd());
                                                srcSaleReport.setItemId(m.getOID());
                                                srcSaleReport.setLocationId(idLocation);
                                                srcSaleReport.setTransType(SrcSaleReport.TYPE_CASH_CREDIT);
                                                //srcSaleReport.setViewReport(SrcSaleReport.VIEW_BY_DETAIL);
                                                Vector records = SaleReportDocument.getList(srcSaleReport,0,0,SaleReportDocument.LOG_MODE_CONSOLE);
                                                for (int item = 0; item < records.size(); item++) {
                                                    SaleReportItem saleItem = (SaleReportItem) records.get(item);
                                                    jumlah += saleItem.getTotalQty();
                                                    subTotal += saleItem.getTotalSold();
                                                }
                                            }

                                        } catch (Exception e) {
                                            System.out.println(e.getMessage());
                                        }
                                    }                                    
                                    harga = subTotal / jumlah;                                    
                                    if (subTotal == 0 && jumlah == 0) {
                                        harga = 0;
                                    }                                    
                                    nilaiProduksi =  (sisaStok + jumlah - stokAwal) * harga;
                                    String link = "";
                                    if (showModalItem == 1) {
                                        link = "'" + m.getOID() + "','" + m.getSku() + "','" + m.getName() + "','" + u.getOID() + "','" + u.getCode() + "','" + String.format("%,.0f", harga) + "','" + String.format("%,.0f", jumlah) + "','" + String.format("%,.0f", subTotal) + "'";
                                    } else if (showModalItem == 2) {
                                        link = "'" + m.getOID() + "','" + m.getSku() + "','" + m.getName() + "','" + u.getOID() + "','" + u.getCode() + "','" + String.format("%,.0f", harga) + "','" + String.format("%,.0f", jumlah) + "','" + String.format("%,.0f", subTotal) + "','" + String.format("%,.0f", stokAwal) + "','" + String.format("%,.0f", sisaStok) + "','" + String.format("%,.0f", nilaiProduksi) + "'";
                                    }                                            
                                    
                            %>
                            <tr>
                                <td class="text-center"><%=(i+1)%></td>
                                <td><a href="javascript:selectItem(<%=link %>)"><%=m.getSku() %></a></td>
                                <td><a href="javascript:selectItem(<%=link %>)"><%=m.getName() %></a></td>
                                <td class="text-center"><%=u.getCode() %></td>
                                <%if (showModalItem == 2) {%>
                                <td class="text-right"><%=String.format("%,.0f", stokAwal) %></td>
                                <%}%>
                                <td class="text-right"><%=String.format("%,.0f", harga) %></td>
                                <td class="text-right"><%=String.format("%,.0f", jumlah) %></td>
                                <%if (showModalItem == 2) {%>
                                <td class="text-right"><%=String.format("%,.0f", sisaStok) %></td>
                                <%}%>
                                <td class="text-right"><%=String.format("%,.0f", subTotal) %></td>
                                <%if (showModalItem == 2) {%>
                                <td class="text-right"><%=String.format("%,.0f", nilaiProduksi) %></td>
                                <%}%>
                            </tr>
                            <%
                                }
                            %>
                            <%if(listItem.isEmpty()){%>
                            <tr>
                                <%if (showModalItem == 1) {%>
                                <td colspan="7" class="text-center">Data tidak ditemukan</td>
                                <%} else if (showModalItem == 2) {%>
                                <td colspan="10" class="text-center">Data tidak ditemukan</td>
                                <%}%>
                            </tr>
                            <%}%>
                        </table>      
                        --%>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-sm btn-default" data-dismiss="modal">Close</button>
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
        
        //$(document).ready(function(){

            function dataTablesOptions(elementIdParent, elementId, servletName, dataFor, callBackDataTables) {                                
                $(elementIdParent).find('table').addClass('table-bordered table-striped table-hover').attr({'id': elementId});
                $("#" + elementId).dataTable({"bDestroy": true,                    
                    "ordering": false,                    
                    "iDisplayLength": 10,
                    "bProcessing": true,
                    "oLanguage": {
                        "sProcessing": "<div class='col-sm-12'><div class='progress progress-striped active'><div class='progress-bar progress-bar-primary' style='width: 100%'><b>Please Wait...</b></div></div></div>"
                    },
                    "bServerSide": true,
                    "sAjaxSource": "<%= approot%>/" + servletName + "?command=<%= Command.LIST%>&FRM_FIELD_DATA_FOR=" + dataFor + "&FRM_FLD_APP_ROOT=<%=approot%>" 
                            + "&hidden_calc_cogs_main_id=<%=calcCogsMain.getOID()%>" + "&modal_item=<%=showModalItem%>" + "&where_sku=<%=searchSku%>" + "&where_name=<%=searchName%>",
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
                dataTablesOptions("#itemElement", "tableItemElement", "AjaxNilaiTukarEmas", "listItemCostSales", null);
            }

            var modalSetting = function (elementId, backdrop, keyboard, show) {
                $(elementId).modal({
                    backdrop: backdrop,
                    keyboard: keyboard,
                    show: show
                });
            };

            modalSetting("#modalItem", "static", false, false);

            $('.datepick').datetimepicker({
                autoclose: true,
                todayBtn: true,
                format: 'yyyy-mm-dd',
                minView: 2
            });

            $('.multiSelect').select2({"placeholder": "Semua lokasi"});

            if ("<%=showModalItem%>" === "1" || "<%=showModalItem%>" === "2") {
                $('#modalItem').modal('show');
                //runDataTables();
                dataTablesOptions("#itemElement", "tableItemElement", "AjaxNilaiTukarEmas", "listItemCostSales", null);                
            }

        //});
        
            $('.btn_src_cost').click(function() {
                $(this).html("<i class='fa fa-spinner fa-pulse'></i>").attr({"disabled":true});
                var sku = $('#cost_sku').val();
                var name = $('#cost_name').val();
                window.location.href = "add_perhitungan_hpp.jsp?modal_item=1"
                        +"&command=<%=Command.EDIT %>&hidden_calc_cogs_main_id=<%=calcCogsMain.getOID() %>"
                        +"&COST_SKU="+sku+"&COST_NAME="+name;
            });

            $('.btn_src_sales').click(function() {
                $(this).html("<i class='fa fa-spinner fa-pulse'></i>").attr({"disabled":true});
                var sku = $('#sales_sku').val();
                var name = $('#sales_name').val();
                window.location.href = "add_perhitungan_hpp.jsp?modal_item=2"
                        +"&command=<%=Command.EDIT %>&hidden_calc_cogs_main_id=<%=calcCogsMain.getOID() %>"
                        +"&SALES_SKU="+sku+"&SALES_NAME="+name;
            });

            function selectItem(itemId, itemSku, itemName, unitId, itemUnit, itemHarga, itemQty, itemSubTotal, itemStokAwal, itemSisaStok, itemTotalValue) {
                if ("<%=showModalItem%>" === "1") {
                    $('#cost_item').val(itemId);
                    $('#cost_sku').val(itemSku);
                    $('#cost_name').val(itemName);
                    $('#cost_unitid').val(unitId);
                    $('#cost_unit').html(itemUnit);
                    $('#cost_harga').val(itemHarga);
                    $('#cost_qty').val(itemQty);
                    $('#cost_subtotal').val(itemSubTotal);
                }
                if ("<%=showModalItem%>" === "2") {
                    $('#sales_item').val(itemId);
                    $('#sales_sku').val(itemSku);
                    $('#sales_name').val(itemName);
                    $('#sales_unitid').val(unitId);
                    $('#sales_unit').html(itemUnit);
                    $('#sales_harga').val(itemHarga);
                    $('#sales_qty').val(itemQty);
                    $('#sales_subtotal').val(itemSubTotal);
                    $('#sales_stokawal').val(itemStokAwal);
                    $('#sales_sisastok').val(itemSisaStok);
                    $('#sales_totalvalue').val(itemTotalValue);
                }
                $('#modalItem').modal('hide');
            }

            function formatAngka(id) {
                $(id).focusout(function() {
                    var angka = $(id).val().replace(/,/g , "");
                    $(id).val(Number(angka).toLocaleString());
                });
            }

            formatAngka('#cost_qty');
            formatAngka('#cost_subtotal');
            formatAngka('#sales_qty');
            formatAngka('#sales_subtotal');
            formatAngka('#sales_stokawal');
            formatAngka('#sales_sisastok');

            $('.hitungHargaBiaya').keyup(function () {
                var qty = $('#cost_qty').val().replace(/,/g , "");
                var subTotal = $('#cost_subtotal').val().replace(/,/g , "");
                var harga = (subTotal / qty);
                if (isNaN(harga)) {harga = 0;};
                $('#cost_harga').val(harga.toLocaleString());
            });

            $('.hitungHargaJual').keyup(function () {
                var qty = $('#sales_qty').val().replace(/,/g , "");
                var subTotal = $('#sales_subtotal').val().replace(/,/g , "");
                var harga = (subTotal / qty);
                if (isNaN(harga)) {harga = 0;};
                $('#sales_harga').val(harga.toLocaleString());
            });

            $('#btnSaveMain').click(function() {
                var error = 0;
                
                $('.datepick').each(function() {
                    var tgl = $(this).val();
                    if (tgl === "") {
                        error +=1;
                        alert("Semua tanggal harus diisi !");                        
                    }
                });
                /*
                $('.multiSelect').each(function() {
                    var loc = $(this).val();
                    if (loc === null) {
                        error +=1;
                        alert("Semua lokasi harus ditentukan !");
                    }
                });
                */
                if (error === 0) {
                    $(this).html("<i class='fa fa-spinner fa-pulse'></i> Tunggu...").attr({"disabled":true});
                    $('#command').val("<%=Command.SAVE%>");
                    $('#formCogsMain').submit();
                }
            });

            $('#btnSaveCost').click(function() {
                var error = 0;
                var idItemCost = $('#cost_item').val();
                
                if (idItemCost === "" || idItemCost === 0) {
                    error += 1;
                    alert("Pilih data biaya terlebih dahulu !");
                }
                
                if (error === 0) {
                    $(this).html("<i class='fa fa-spinner fa-pulse'></i> Tunggu...").attr({"disabled":true});
                    $('#commandCost').val("<%=Command.SAVE%>");
                    $('#formCogsCost').submit();
                }
            });

            $('#btnSaveSales').click(function() {
                var error = 0;
                var idItemSales = $('#sales_item').val();
                
                if (idItemSales === "" || idItemSales === 0) {
                    error += 1;
                    alert("Pilih data penjualan terlebih dahulu !");
                }
                
                if (error === 0) {
                    $(this).html("<i class='fa fa-spinner fa-pulse'></i> Tunggu...").attr({"disabled":true});
                    $('#commandSales').val("<%=Command.SAVE%>");
                    $('#formCogsSales').submit();
                }
            });

            function deleteCost(itemId,itemSku) {
                if (confirm("Hapus data " + itemSku + " ?")) {
                    $('#cost_id').val(itemId);
                    $('#commandCost').val("<%=Command.DELETE%>");
                    $('#formCogsCost').submit();
                }
            }

            function deleteSales(itemId,itemSku) {
                if (confirm("Hapus data " + itemSku + " ?")) {
                    $('#sales_id').val(itemId);
                    $('#commandSales').val("<%=Command.DELETE%>");
                    $('#formCogsSales').submit();
                }
            }
            
            function updateHarga(itemId,hargaBaru) {                
                window.location.href = "add_perhitungan_hpp.jsp?"
                        +"command=<%=Command.EDIT %>&hidden_calc_cogs_main_id=<%=calcCogsMain.getOID() %>"
                        +"&update_harga=1" + "&item_update=" + itemId + "&harga_baru=" + hargaBaru;
            }
            
            $('.btn_updateharga').click(function() {
                $(this).html("<i class='fa fa-spinner fa-pulse'></i> Tunggu...").attr({"disabled":true});
                var item = $(this).data('itemid');
                var harga = $(this).data('hargabaru');
                updateHarga(item,harga);
            });
            
            $('#btnBack').click(function() {
                $(this).html("<i class='fa fa-spinner fa-pulse'></i> Tunggu...").attr({"disabled":true});
            });

    </script>
</html>

<%-- 
    Document   : src_perhitungan_hpp
    Created on : Jun 13, 2018, 10:13:37 AM
    Author     : Dimata 007
--%>

<%@page import="com.dimata.posbo.entity.masterdata.*"%>
<%@page import="com.dimata.posbo.form.masterdata.*"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.gui.jsp.ControlCombo"%>
<%@page import="com.dimata.util.Command"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../../main/javainit.jsp" %>
<%int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_PENENTUAN_HPP);%>
<%@ include file = "../../main/checkuser.jsp" %>

<%//
    int iCommand = FRMQueryString.requestCommand(request);
    String tglCostAwal = FRMQueryString.requestString(request, FrmCalcCogsMain.fieldNames[FrmCalcCogsMain.FRM_FIELD_COST_DATE_START]);
    String tglCostAkhir = FRMQueryString.requestString(request, FrmCalcCogsMain.fieldNames[FrmCalcCogsMain.FRM_FIELD_COST_DATE_END]);
    String multiLocCost[] = FRMQueryString.requestStringValues(request, "MULTIPLE_LOCATION_COST");
    String tglSalesAwal = FRMQueryString.requestString(request, FrmCalcCogsMain.fieldNames[FrmCalcCogsMain.FRM_FIELD_SALES_DATE_START]);
    String tglSalesAkhir = FRMQueryString.requestString(request, FrmCalcCogsMain.fieldNames[FrmCalcCogsMain.FRM_FIELD_SALES_DATE_END]);
    String multiLocSales[] = FRMQueryString.requestStringValues(request, "MULTIPLE_LOCATION_SALES");
    int status = FRMQueryString.requestInt(request, FrmCalcCogsMain.fieldNames[FrmCalcCogsMain.FRM_FIELD_STATUS]);
    
    if (iCommand == Command.NONE) {
        status = -1;
        //tglCostAwal = Formater.formatDate(new Date(), "yyyy-MM-dd");
        //tglCostAkhir = Formater.formatDate(new Date(), "yyyy-MM-dd");
        //tglSalesAwal = Formater.formatDate(new Date(), "yyyy-MM-dd");
        //tglSalesAkhir = Formater.formatDate(new Date(), "yyyy-MM-dd");
    }
    
    String whereHpp = "";
    if (status == -1) {
        whereHpp += "ccm." + PstCalcCogsMain.fieldNames[PstCalcCogsMain.FLD_STATUS] + " != " + status;
    } else {
        whereHpp += "ccm." + PstCalcCogsMain.fieldNames[PstCalcCogsMain.FLD_STATUS] + " = " + status;
    }
    if (tglCostAwal.length() > 0) {
        whereHpp += " AND ccm." + PstCalcCogsMain.fieldNames[PstCalcCogsMain.FLD_COST_DATE_START] + " >= '" + tglCostAwal + "'";
    }
    if (tglCostAkhir.length() > 0) {
        whereHpp += " AND ccm." + PstCalcCogsMain.fieldNames[PstCalcCogsMain.FLD_COST_DATE_END] + " <= '" + tglCostAkhir + "'";
    }
    if (tglSalesAwal.length() > 0) {
        whereHpp += " AND ccm." + PstCalcCogsMain.fieldNames[PstCalcCogsMain.FLD_SALES_DATE_START] + " >= '" + tglSalesAwal + "'";
    }
    if (tglSalesAkhir.length() > 0) {
        whereHpp += " AND ccm." + PstCalcCogsMain.fieldNames[PstCalcCogsMain.FLD_SALES_DATE_END] + " <= '" + tglSalesAkhir + "'";
    }
    if (multiLocCost != null || multiLocSales != null) {
        whereHpp += " AND (";
        
        if (multiLocCost != null) {
            String multiLocId = "";
            for (int c = 0; c < multiLocCost.length; c++) {
                multiLocId += c == 0 ? multiLocCost[c] : "," + multiLocCost[c];
            }
            whereHpp += "("
                    + " ccl." + PstCalcCogsLocation.fieldNames[PstCalcCogsLocation.FLD_LOCATION_CALC_COGS_TYPE] + " = " + PstCalcCogsLocation.CALC_COGS_LOC_TYPE_COST
                    + " AND ccl." + PstCalcCogsLocation.fieldNames[PstCalcCogsLocation.FLD_LOCATION_ID] + " IN (" + multiLocId + ")"
                    + ")";
        }
        if (multiLocSales != null) {
            if (multiLocCost != null) {
                whereHpp += " OR ";
            }
            String multiLocId = "";
            for (int s = 0; s < multiLocSales.length; s++) {
                multiLocId += s == 0 ? multiLocSales[s] : "," + multiLocSales[s];
            }
            whereHpp += "("
                    + " ccl." + PstCalcCogsLocation.fieldNames[PstCalcCogsLocation.FLD_LOCATION_CALC_COGS_TYPE] + " = " + PstCalcCogsLocation.CALC_COGS_LOC_TYPE_SALES
                    + " AND ccl." + PstCalcCogsLocation.fieldNames[PstCalcCogsLocation.FLD_LOCATION_ID] + " IN (" + multiLocId + ")"
                    + ")";
        }
        
        whereHpp += ")";
    }
    whereHpp += " GROUP BY ccm." + PstCalcCogsMain.fieldNames[PstCalcCogsMain.FLD_CALC_COGS_MAIN_ID];
    
%>

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
            .tabel_data {font-size: 12px;}
            .tabel_data th {
                text-align: center;
            }
            .table-condensed {font-size: 14px}
            
            .modal-header {padding: 10px 20px; border-color: lightgray}
            .modal-footer {padding: 10px 20px; margin: 0px; border-color: lightgray}
            .box .box-header, .box .box-footer {border-color: lightgray}
            
        </style>
    </head>
    <body>
        <div class="col-sm-12">
            
            <h4>Pencarian Data Penentuan HPP</h4>
            <div class="box box-primary">
                <div class="box-header with-border">
                    <h3 class="box-title">Filter Pencarian</h3>
                </div>
                
                <div class="box-body">
                    <form id="formSearchHpp" class="form-horizontal">
                        
                        <input type="hidden" id="command" name="command" value="<%=Command.LIST%>">
                        
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
                            <div class="col-sm-4">
                                <select multiple="" class="form-control input-sm multiSelect" name="MULTIPLE_LOCATION_COST">
                                    <%
                                        Vector listLokasiCost = PstLocation.list(0, 0, "", "" + PstLocation.fieldNames[PstLocation.FLD_NAME]);
                                        for (int i = 0; i < listLokasiCost.size(); i++) {
                                            Location l = (Location) listLokasiCost.get(i);
                                            String selectedLocCost = "";
                                            if (multiLocCost != null) {
                                                for (int c = 0; c < multiLocCost.length; c++) {
                                                    if (multiLocCost[c].equals(""+l.getOID())) {
                                                        selectedLocCost = "selected";
                                                        break;
                                                    }
                                                }
                                            }
                                    %>
                                    <option <%=selectedLocCost %> value="<%=l.getOID() %>"><%=l.getName() %></option>
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
                            <div class="col-sm-4">
                                <select multiple="" class="form-control input-sm multiSelect" name="MULTIPLE_LOCATION_SALES">
                                    <%
                                        Vector listLokasiSales = PstLocation.list(0, 0, "", "" + PstLocation.fieldNames[PstLocation.FLD_NAME]);
                                        for (int i = 0; i < listLokasiSales.size(); i++) {                                            
                                            Location l = (Location) listLokasiSales.get(i);                                            
                                            String selectedLocSales = "";
                                            if (multiLocSales != null) {
                                                for (int c = 0; c < multiLocSales.length; c++) {
                                                    if (multiLocSales[c].equals(""+l.getOID())) {
                                                        selectedLocSales = "selected";
                                                        break;
                                                    }
                                                }
                                            }
                                    %>
                                    <option <%=selectedLocSales %> value="<%=l.getOID() %>"><%=l.getName() %></option>
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

                                    statusDocVal.add("-1");
                                    statusDocVal.add(""+I_DocStatus.DOCUMENT_STATUS_DRAFT);
                                    statusDocVal.add(""+I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED);
                                    statusDocVal.add(""+I_DocStatus.DOCUMENT_STATUS_FINAL);

                                    statusDocKey.add("All");
                                    statusDocKey.add(""+I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]);
                                    statusDocKey.add(""+I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED]);
                                    statusDocKey.add(""+I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);
                                %>
                                <%=ControlCombo.draw(FrmCalcCogsMain.fieldNames[FrmCalcCogsMain.FRM_FIELD_STATUS], null, ""+status, statusDocVal, statusDocKey, "", "form-control input-sm")%>
                            </div>                            
                        </div>
                            
                    </form>
                </div>
                
                <div class="box-footer">
                    <button type="button" id="btnSearch" class="btn btn-sm btn-primary"><i class="fa fa-search"></i>&nbsp; Cari</button>
                    <a href="add_perhitungan_hpp.jsp" class="btn btn-sm btn-primary"><i class="fa fa-plus"></i>&nbsp; Tambah Perhitungan HPP</a>
                </div>
            </div>

            <%if(iCommand == Command.LIST) {%>
            
            <div class="box box-primary">
                <div class="box-body">
                    
                    <table class="table table-bordered tabel_data">
                        <tr class="label-default">
                            <th style="width: 1%">No.</th>
                            <th>Periode Biaya</th>
                            <th>Lokasi Biaya</th>
                            <th>Periode Penjualan</th>
                            <th>Lokasi Penjualan</th>
                            <th>Status</th>
                            <th>Keterangan</th>
                            <th><i class="fa fa-gear"></i></th>
                        </tr>
                        <%
                            Vector listPerhitunganHpp = PstCalcCogsMain.listJoinLocation(0, 0, whereHpp, "");
                            for (int i = 0; i < listPerhitunganHpp.size(); i++) {
                                CalcCogsMain ccm = (CalcCogsMain) listPerhitunganHpp.get(i);
                                String periodeBiaya = Formater.formatDate(ccm.getCostDateStart(), "yyyy-MM-dd") + " s/d " + Formater.formatDate(ccm.getCostDateEnd(), "yyyy-MM-dd");                                
                                String periodeJual = Formater.formatDate(ccm.getSalesDateStart(), "yyyy-MM-dd") + " s/d " + Formater.formatDate(ccm.getSalesDateEnd(), "yyyy-MM-dd");
                                
                                String whereBiaya = PstCalcCogsLocation.fieldNames[PstCalcCogsLocation.FLD_LOCATION_CALC_COGS_TYPE] + " = " + PstCalcCogsLocation.CALC_COGS_LOC_TYPE_COST
                                        + " AND " + PstCalcCogsLocation.fieldNames[PstCalcCogsLocation.FLD_CALC_COGS_MAIN_ID] + " = " + ccm.getOID();
                                Vector listLokBiaya = PstCalcCogsLocation.list(0, 0, whereBiaya, "");
                                String lokasiBiaya = "";
                                for (int b = 0; b < listLokBiaya.size(); b++) {
                                    CalcCogsLocation ccl = (CalcCogsLocation) listLokBiaya.get(b);
                                    Location l = PstLocation.fetchExc(ccl.getLocationId());
                                    lokasiBiaya += b == 0 ? l.getName() : ", " + l.getName();
                                }
                                
                                String whereJual = PstCalcCogsLocation.fieldNames[PstCalcCogsLocation.FLD_LOCATION_CALC_COGS_TYPE] + " = " + PstCalcCogsLocation.CALC_COGS_LOC_TYPE_SALES
                                        + " AND " + PstCalcCogsLocation.fieldNames[PstCalcCogsLocation.FLD_CALC_COGS_MAIN_ID] + " = " + ccm.getOID();
                                Vector listLokJual = PstCalcCogsLocation.list(0, 0, whereJual, "");
                                String lokasiJual = "";
                                for (int j = 0; j < listLokJual.size(); j++) {
                                    CalcCogsLocation ccl = (CalcCogsLocation) listLokJual.get(j);
                                    Location l = PstLocation.fetchExc(ccl.getLocationId());
                                    lokasiJual += j == 0 ? l.getName() : ", " + l.getName();
                                }
                                
                        %>
                        <tr>
                            <td class="text-center"><%=(i+1)%></td>
                            <td class="text-center"><%=periodeBiaya %></td>
                            <td><%=lokasiBiaya %></td>
                            <td class="text-center"><%=periodeJual %></td>
                            <td><%=lokasiJual %></td>
                            <td class="text-center"><%=I_DocStatus.fieldDocumentStatus[ccm.getStatus()] %></td>
                            <td><%=ccm.getNote() %></td>
                            <td class="text-center"><a href="add_perhitungan_hpp.jsp?command=3&hidden_calc_cogs_main_id=<%=ccm.getOID() %>"><i class="fa fa-pencil"></i></a></td>
                        </tr>
                        <%
                            }
                        %>
                        <%if(listPerhitunganHpp.isEmpty()) {%>
                        <tr><td colspan="8" class="text-center"><b>Data tidak ditemukan</b></td></tr>
                        <%}%>
                    </table>
                    
                </div>
            </div>
            
            <%}%>

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
        
        $('.datepick').datetimepicker({
            autoclose: true,
            todayBtn: true,
            format: 'yyyy-mm-dd',
            minView: 2
        });
        
        $('.multiSelect').select2({"placeholder": "Semua lokasi"});
        
        $('#btnSearch').click(function() {
            $(this).html("<i class='fa fa-spinner fa-pulse'></i> Tunggu...").attr({"disabled":true});
            $('#formSearchHpp').submit();
        });
        
        $('a').click(function() {
            $(this).html("<i class='fa fa-spinner fa-pulse'></i> Tunggu...").attr({"disabled":true});
        });
            
    </script>
    
</html>

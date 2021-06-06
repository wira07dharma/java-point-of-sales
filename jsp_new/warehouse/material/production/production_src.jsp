<%-- 
    Document   : production_src
    Created on : Sep 11, 2019, 2:52:24 PM
    Author     : Dimata 007
--%>

<%@page import="com.dimata.posbo.entity.masterdata.*"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.posbo.entity.warehouse.*"%>
<%@page import="com.dimata.posbo.session.warehouse.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../../../main/javainit.jsp" %>
<%int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_PENENTUAN_HPP);%>
<%@ include file = "../../../main/checkuser.jsp" %>

<%//
    String nomorDokumen = FRMQueryString.requestString(request, "NOMOR_DOKUMEN");
    String tglMulai = FRMQueryString.requestString(request, "TGL_MULAI");
    String tglAkhir = FRMQueryString.requestString(request, "TGL_AKHIR");
    String nomorBatch = FRMQueryString.requestString(request, "NOMOR_BATCH");
    String[] multiLokasi = FRMQueryString.requestStringValues(request, "LOKASI");
    String[] multiStatus = FRMQueryString.requestStringValues(request, "STATUS");
    String[] multiTemplate = FRMQueryString.requestStringValues(request, "TEMPLATE");
    int viewType = FRMQueryString.requestInt(request, "VIEW_TYPE");

    SessProduction sessProduction = new SessProduction();
    sessProduction.setProductionNumber(nomorDokumen);
    sessProduction.setDateFrom(tglMulai);
    sessProduction.setDateTo(tglAkhir);
    if (multiLokasi != null) {
        String a = Arrays.toString(multiLokasi);
        sessProduction.setMultiLocationId(a.substring(1, a.length() - 1));
    }
    if (multiStatus != null) {
        String a = Arrays.toString(multiStatus);
        sessProduction.setMultiStatus(a.substring(1, a.length() - 1));
    }
    sessProduction.setBatchNumber(nomorBatch);
    if (multiTemplate != null) {
        String a = Arrays.toString(multiTemplate);
        sessProduction.setMultiTemplateId(a.substring(1, a.length() - 1));
    }
    sessProduction.setViewType(viewType);
    ArrayList<HashMap<String, Object>> listProduction = SessProduction.listProduction(sessProduction);

%>

<%//
    Vector<Location> listLocation = PstLocation.list(0, 0, "", "" + PstLocation.fieldNames[PstLocation.FLD_NAME]);
    String optionLocation = "";
    for (Location l : listLocation) {
        String selected = Arrays.toString(multiLokasi).contains("" + l.getOID()) ? "selected" : "";
        optionLocation += "<option " + selected + " value='" + l.getOID() + "'>" + l.getName() + "</option>";
    }

    int[] arrayStatus = {
        I_DocStatus.DOCUMENT_STATUS_DRAFT,
        I_DocStatus.DOCUMENT_STATUS_FINAL,
        I_DocStatus.DOCUMENT_STATUS_CLOSED,
        I_DocStatus.DOCUMENT_STATUS_POSTED
    };
    String statusOption = "";
    for (int i = 0; i < arrayStatus.length; i++) {
        String selected = Arrays.toString(multiStatus).contains("" + arrayStatus[i]) ? "selected" : "";
        statusOption += "<option " + selected + " value='" + arrayStatus[i] + "'>" + I_DocStatus.fieldDocumentStatus[arrayStatus[i]] + "</option>";
    }

    Vector<ChainMain> listTemplate = PstChainMain.list(0, 0, "", "" + PstChainMain.fieldNames[PstChainMain.FLD_CHAIN_TITLE]);
    String optionTemplate = "";
    for (ChainMain cm : listTemplate) {
        String selected = Arrays.toString(multiTemplate).contains("" + cm.getOID()) ? "selected" : "";
        optionTemplate += "<option " + selected + " value='" + cm.getOID() + "'>" + cm.getChainTitle() + "</option>";
    }

    String optionViewType = "";
    for (int i = 0; i < SessProduction.viewTypeTitle.length; i++) {
        optionViewType += "<option " + (viewType == i ? "selected" : "") + " value='" + i + "'>" + SessProduction.viewTypeTitle[i] + "</option>";
    }
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@ include file = "../../../styles/plugin_component.jsp" %>
        <title>Production</title>
        <style>
            .datetimepicker th {font-size: 14px}
            .datetimepicker td {font-size: 12px}
            .select2-selection {border-radius: 0% !important; border-color: lightgray !important}

            body {background-color: #eeeeee;}
            .box .box-header, .box .box-footer {border-color: lightgray;}
            
            .tabel_data tbody tr th {
                font-size: 12px;
                padding: 5px;
            }
            .tabel_data tbody tr td {
                font-size: 12px;
                padding: 5px;
            }
        </style>
    </head>
    <body>
        <div class="col-sm-12">
            <h3>Produksi</h3>
            <div class="box box-primary">
                <div class="box-header with-border">
                    <h3 class="box-title">Pencarian</h3>
                </div>
                <div class="box-body">
                    <form id="formSearch" class="form-horizontal">
                        <input type="hidden" id="viewType" name="VIEW_TYPE" value="<%=viewType%>">
                        <div class="col-sm-6">
                            <div class="form-group">
                                <label class="col-sm-4">Nomor Dokumen</label>
                                <div class="col-sm-8">
                                    <input type="text" class="form-control input-sm" name="NOMOR_DOKUMEN" value="<%=nomorDokumen%>">
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-4">Tanggal</label>
                                <div class="col-sm-8">
                                    <div class="input-group">
                                        <input type="text" autocomplete="off" class="form-control input-sm datePicker" name="TGL_MULAI" value="<%=tglMulai%>">
                                        <span class="input-group-addon">s/d</span>
                                        <input type="text" autocomplete="off" class="form-control input-sm datePicker" name="TGL_AKHIR" value="<%=tglAkhir%>">
                                    </div>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-4">Lokasi</label>
                                <div class="col-sm-8">
                                    <select multiple="" class="form-control input-sm selectSearch" name="LOKASI">
                                        <%=optionLocation%>
                                    </select>
                                </div>
                            </div>
                        </div>

                        <div class="col-sm-6">
                            <div class="form-group">
                                <label class="col-sm-4">Nomor Batch</label>
                                <div class="col-sm-8">
                                    <input type="text" class="form-control input-sm" name="NOMOR_BATCH" value="<%=nomorBatch%>">
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-4">Template</label>
                                <div class="col-sm-8">
                                    <select multiple="" class="form-control input-sm selectSearch" name="TEMPLATE">
                                        <%=optionTemplate%>
                                    </select>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-4">Status</label>
                                <div class="col-sm-8">
                                    <select multiple="" class="form-control input-sm selectSearch" name="STATUS">
                                        <%=statusOption%>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="box-footer">
                    <div class="form-inline">
                        <button id="btnSearch" class="btn btn-sm btn-primary"><i class="fa fa-search"></i>&nbsp; Cari</button>
                        <select id="selectViewType" class="form-control input-sm">
                            <%=optionViewType%>
                        </select>
                        &nbsp;&nbsp;
                        <a class="btn btn-sm btn-primary" href="production_edit.jsp?command=<%=Command.ADD%>"><i class="fa fa-plus"></i>&nbsp; Tambah Produksi</a>
                    </div>
                </div>
            </div>

            <div class="box box-primary">
                <div class="box-header with-border">
                    <h3 class="box-title">Daftar Dokumen</h3>
                </div>
                <div class="box-body">
                    <%if (viewType == SessProduction.viewTypeDoc) {%>
                    
                    <table class="table table-bordered tabel_data">
                        <tr class="label-primary">
                            <th style="width: 1%">No.</th>
                            <th>Nomor Dokumen</th>
                            <th>Tanggal</th>
                            <th>Lokasi Asal</th>
                            <th>Lokasi Tujuan</th>
                            <th>Status</th>
                            <th>Keterangan</th>
                        </tr>

                        <%//
                            int no = 0;
                            for (HashMap<String, Object> map : listProduction) {
                                no++;
                        %>
                        <tr>
                            <td><%=no%>.</td>
                            <td><a href="production_edit.jsp?command=<%=Command.EDIT%>&production_id=<%=map.get("PRODUCTION_ID")%>"><%=map.get("NUMBER")%></a></td>
                            <td><%=map.get("DATE")%></td>
                            <td><%=map.get("LOCATION_FROM")%></td>
                            <td><%=map.get("LOCATION_TO")%></td>
                            <td><%=I_DocStatus.fieldDocumentStatus[Integer.valueOf("" + map.get("STATUS"))]%></td>
                            <td><%=map.get("REMARK")%></td>
                        </tr>
                        <%
                            }
                        %>

                        <%if (listProduction.isEmpty()) {%>
                        <tr>
                            <td colspan="7" class="text-center label-default">Tidak ada data</td>
                        </tr>
                        <%} else {%>

                        <%}%>
                    </table>
                    
                    <%}%>

                    <%if (viewType == SessProduction.viewTypeBatch) {%>
                    
                    <table class="table table-bordered tabel_data">
                        <tr class="label-primary">
                            <th style="width: 1%">No.</th>
                            <th>Nomor Dokumen</th>
                            <th>Nomor Batch</th>
                            <th>Periode</th>
                            <th>Tanggal Mulai</th>
                            <th>Tanggal Berakhir</th>
                        </tr>

                        <%//
                            int no = 0;
                            for (HashMap<String, Object> map : listProduction) {
                                no++;
                        %>
                        <tr>
                            <td><%=no%>.</td>
                            <td><a href="production_edit.jsp?command=<%=Command.EDIT%>&production_id=<%=map.get("PRODUCTION_ID")%>"><%=map.get("NUMBER")%></a></td>
                            <td><%=map.get("BATCH")%></td>
                            <td><%=map.get("PERIOD")%></td>
                            <td><%=map.get("START")%></td>
                            <td><%=map.get("END")%></td>
                        </tr>
                        <%
                            }
                        %>

                        <%if (listProduction.isEmpty()) {%>
                        <tr>
                            <td colspan="6" class="text-center label-default">Tidak ada data</td>
                        </tr>
                        <%} else {%>

                        <%}%>
                    </table>
                    
                    <%}%>
                </div>
            </div>
        </div>
    </body>

    <script>
        $('.datePicker').datetimepicker({
            format: "yyyy-mm-dd",
            todayBtn: true,
            autoclose: true,
            minView: 2
        });

        $('.selectSearch').select2({"width": "100%"});

        $('.btn').click(function () {
            $(this).html("<i class='fa fa-spinner fa-pulse'></i> Tunggu...").attr({"disabled": true});
        });

        $('#btnSearch').click(function () {
            $(this).html("<i class='fa fa-spinner fa-pulse'></i> Tunggu...").attr({"disabled": true});
            $('#formSearch').submit();
        });
        
        $('#selectViewType').change(function() {
            $('#viewType').val($(this).val());
        });

    </script>
</html>

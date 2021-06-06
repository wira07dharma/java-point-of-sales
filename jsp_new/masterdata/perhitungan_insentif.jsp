<%-- 
    Document   : perhitungan_insentif
    Created on : Jun 7, 2018, 9:25:07 AM
    Author     : Dimata 007
--%>

<%@page import="com.dimata.util.*"%>
<%@page import="com.dimata.gui.jsp.*"%>
<%@page import="com.dimata.qdep.form.*"%>
<%@page import="com.dimata.posbo.entity.masterdata.*"%>
<%@page import="com.dimata.posbo.form.masterdata.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_INCENTIVE_SETTING); %>
<%@ include file = "../main/checkuser.jsp" %>

<%//
    int iCommand = FRMQueryString.requestCommand(request);
    long oidInsentifMaster = FRMQueryString.requestLong(request, "hidden_insentif_master_id");
    
    CtrlInsentifMaster ctrlInsentifMaster = new CtrlInsentifMaster(request);
    int error = ctrlInsentifMaster.action(iCommand, oidInsentifMaster, userId, userName);
    String message = ctrlInsentifMaster.getMessage();
    InsentifMaster insentifMaster = ctrlInsentifMaster.getInsentifMaster();
    
    Vector listInsentifMaster = PstInsentifMaster.list(0, 0, "", "");
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="../styles/bootstrap/css/bootstrap.min.css"/>
        <link rel="stylesheet" type="text/css" href="../styles/dist/css/AdminLTE.css"/>
        <link rel="stylesheet" type="text/css" href="../styles/font-awesome/font-awesome.css"/>
        <link rel="stylesheet" type="text/css" href="../styles/select2/css/select2.min.css" />
        <link rel="stylesheet" media="screen" href="../styles/datepicker/bootstrap-datetimepicker.min.css"/>
        <style>            
            body {background-color: beige;}
            th {font-size: 14px}
            td {font-size: 14px}
            .tabel_data td {font-size: 12px;}
            .tabel_data th {text-align: center;}
            .box .box-header {border-color: lightgray;}
            .box .box-footer {border-color: lightgray;}
            hr {border-color: lightgray;}
        </style>
    </head>
    <body>
        <div class="col-sm-12">

            <br>
            <div class="box box-primary">
                <div class="box-header with-border">
                    <h3 class="box-title">Perhitungan Insentif</h3>
                </div>

                <div class="box-body">
                    <a href="perhitungan_insentif.jsp?command=<%=Command.EDIT%>&hidden_insentif_master_id=0" class="btn btn-sm btn-primary"><i class="fa fa-plus"></i>&nbsp; Tambah Perhitungan Insentif</a>
                    <p></p>
                    <table class="table table-bordered table-striped tabel_data">
                        <tr>
                            <th style="width: 1%">No.</th>
                            <th>Judul</th>
                            <th>Tanggal Mulai</th>
                            <th>Tanggal Berakhir</th>
                            <th>Status Dokumen</th>
                            <th style="width: 1%">Aksi</th>
                        </tr>
                        <%
                            for (int i = 0; i < listInsentifMaster.size(); i++) {
                                InsentifMaster im = (InsentifMaster) listInsentifMaster.get(i);
                        %>
                        <tr>
                            <td class="text-center"><%=(i+1)%></td>
                            <td><%=im.getTitle()%></td>
                            <td class="text-center"><%=im.getPeriodeForever() == 1 ? "-" : im.getPeriodeStart() == null ? "Tanggal belum diisi" : Formater.formatDate(im.getPeriodeStart(), "dd MMMM yyyy") %></td>
                            <td class="text-center"><%=im.getPeriodeForever() == 1 ? "-" : im.getPeriodeEnd() == null ? "Tanggal belum diisi" : Formater.formatDate(im.getPeriodeEnd(), "dd MMMM yyyy") %></td>
                            <td class="text-center"><%=I_DocStatus.fieldDocumentStatus[im.getStatus()]%></td>
                            <td class="text-center"><a href="perhitungan_insentif.jsp?command=<%=Command.EDIT%>&hidden_insentif_master_id=<%=im.getOID()%>">Detail</a></td>
                        </tr>
                        <%
                            }
                        %>
                    </table>                    
                    <%= message.length() > 0 ? error > 0 ? "<p><div class='label-danger text-center'>" + message + "</div>" : "<p><div class='label-success text-center'>" + message + "</div>" : "" %>
                </div>
            </div>

            <% if (iCommand == Command.ADD || iCommand == Command.EDIT || error > 0) {%>
                
            <div class="box box-primary">
                <div class="box-header with-border">
                    <h3 class="box-title">Form Perhitungan Insentif</h3>
                </div>

                <div class="box-body">
                    <form id="formInsentif" class="form-horizontal">
                        <input type="hidden" name="command" value="<%=Command.SAVE%>">
                        <input type="hidden" name="hidden_insentif_master_id" value="<%=insentifMaster.getOID()%>">
                                                
                        <div class="col-sm-12">
                            <p></p>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">Judul</label>
                                <div class="col-sm-6">
                                    <input type="text" class="form-control input-sm" name="<%=FrmInsentifMaster.fieldNames[FrmInsentifMaster.FRM_FIELD_TITLE] %>" value="<%=insentifMaster.getTitle()%>">
                                </div>
                            </div>
                            
                            <div class="form-group">
                                <label class="col-sm-2 control-label">Material Main</label>
                                <div class="col-sm-2">
                                    <select class="form-control input-sm" name="<%=FrmInsentifMaster.fieldNames[FrmInsentifMaster.FRM_FIELD_MATERIAL_MAIN] %>">
                                        <%
                                            for (int item = 0; item < Material.MATERIAL_TYPE_TITLE.length; item++) {
                                                if (item == Material.MATERIAL_TYPE_GENERAL || item == Material.MATERIAL_TYPE_EMAS_LANTAKAN) {continue;}
                                        %>
                                        <option <%=item == insentifMaster.getMaterialMain() ? "selected" : ""%> value="<%=item%>"><%=Material.MATERIAL_TYPE_TITLE[item]%></option>
                                        <%
                                            }
                                        %>
                                    </select>
                                </div>    
                                    
                                <label class="col-sm-2 control-label">Kategori</label>
                                <div class="col-sm-2">
                                    <select class="form-control input-sm" name="<%=FrmInsentifMaster.fieldNames[FrmInsentifMaster.FRM_FIELD_CATEGORY_ID] %>">
                                        <%
                                            Vector listKategory = PstCategory.list(0, 0, "", "");
                                            for (int i = 0; i < listKategory.size(); i++) {
                                                Category cat = (Category) listKategory.get(i);
                                        %>
                                        <option <%=cat.getOID() == insentifMaster.getCategoryId()? "selected" : ""%> value="<%=cat.getOID() %>"><%=cat.getName() %></option>
                                        <%
                                            }
                                        %>
                                    </select>
                                </div>                                
                            </div>
                            
                            <div class="form-group">
                                <label class="col-sm-2 control-label">Periode</label>
                                <div class="col-sm-3">
                                    <div class="checkbox">
                                        <input type="checkbox" value="1" <%= insentifMaster.getPeriodeForever() == 1 ? "checked" : ""%> name="<%=FrmInsentifMaster.fieldNames[FrmInsentifMaster.FRM_FIELD_PERIODE_FOREVER] %>">Berlaku selamanya
                                    </div>
                                </div>
                            </div>
                                    
                            <div class="form-group">
                                <label class="col-sm-2 control-label"></label>
                                <div class="col-sm-3">
                                    <div class="input-group">
                                        <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                        <input type="text" placeholder="Tanggal mulai" class="form-control input-sm datepick" name="<%=FrmInsentifMaster.fieldNames[FrmInsentifMaster.FRM_FIELD_PERIODE_START] %>" value="<%= insentifMaster.getPeriodeStart() == null ? "" : Formater.formatDate(insentifMaster.getPeriodeStart(), "yyyy-MM-dd") %>">
                                    </div>
                                </div>
                                <div class="col-sm-3">
                                    <div class="input-group">
                                        <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                        <input type="text" placeholder="Tanggal berakhir" class="form-control input-sm datepick" name="<%=FrmInsentifMaster.fieldNames[FrmInsentifMaster.FRM_FIELD_PERIODE_END] %>" value="<%= insentifMaster.getPeriodeEnd() == null ? "" : Formater.formatDate(insentifMaster.getPeriodeEnd(), "yyyy-MM-dd") %>">
                                    </div>
                                </div>
                            </div>
                            
                            <hr>
                        </div>
                                                
                        <div class="col-sm-6">
                            <p>Detail Penentuan Poin :</p>
                            
                            <div class="form-group">
                                <label class="col-sm-4 control-label">Laba Penjualan</label>
                                <div class="col-sm-8">
                                    <div class="checkbox">
                                        <input type="checkbox" value="1" <%= insentifMaster.getIncludeSalesProfit() == 1 ? "checked" : ""%> name="<%=FrmInsentifMaster.fieldNames[FrmInsentifMaster.FRM_FIELD_INCLUDE_SALES_PROFIT] %>">Ya (masuk rumus perhitungan)
                                    </div>                                   
                                </div>
                            </div>
                            
                            <div class="form-group">
                                <label class="col-sm-4 control-label">Ongkos Jual</label>
                                <div class="col-sm-8">
                                    <div class="checkbox">
                                        <input type="checkbox" value="1" <%= insentifMaster.getIncludeCostOfSales() == 1 ? "checked" : ""%> name="<%=FrmInsentifMaster.fieldNames[FrmInsentifMaster.FRM_FIELD_INCLUDE_COST_OF_SALES] %>">Ya (masuk rumus perhitungan)
                                    </div>
                                </div>
                            </div>
                            
                            <div class="form-group">
                                <label class="col-sm-4 control-label">Nilai Pembagi</label>
                                <div class="col-sm-6">
                                    <div class="input-group">
                                        <span class="input-group-addon">Rp</span>
                                        <input type="text" class="form-control input-sm" name="<%=FrmInsentifMaster.fieldNames[FrmInsentifMaster.FRM_FIELD_DIVISION_POINT] %>" value="<%= insentifMaster.getDivisionPoint()%>">
                                    </div>
                                </div>
                            </div>
                            
                        </div>
                                                                        
                        <div class="col-sm-6">
                            <p>Perhitungan Nominal Insentif :</p>
                            
                            <div class="form-group">
                                <label class="col-sm-4 control-label">Faktor Insentif</label>
                                <div class="col-sm-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control input-sm" name="<%=FrmInsentifMaster.fieldNames[FrmInsentifMaster.FRM_FIELD_FAKTOR_NOMINAL_INSENTIF] %>" value="<%= insentifMaster.getFaktorNominalInsentif()%>">
                                        <span class="input-group-addon">%</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                        
                        <div class="col-sm-6">
                            <p>Perhitungan Insentif Selain Sales Counter :</p>
                            
                            <div class="form-group">
                                <label class="col-sm-4 control-label">Sesuai Posisi</label>
                                <div class="col-sm-8">
                                    <div class="checkbox">
                                        <input type="checkbox" value="1" <%= insentifMaster.getDependOnPosition() == 1 ? "checked" : ""%> name="<%=FrmInsentifMaster.fieldNames[FrmInsentifMaster.FRM_FIELD_DEPEND_ON_POSITION] %>">Ya (persentase berbeda per posisi)
                                    </div>
                                </div>
                            </div>                            
                        </div>
                                    
                        <div class="col-sm-12">
                            <hr>
                            <div class="form-group">                                    
                                <label class="col-sm-2 control-label">Status Dokumen</label>
                                <div class="col-sm-2">
                                    <select class="form-control input-sm" name="<%=FrmInsentifMaster.fieldNames[FrmInsentifMaster.FRM_FIELD_STATUS] %>">
                                        <%
                                            for (int doc = 0; doc < 3; doc++) {
                                        %>
                                        <option <%= doc == insentifMaster.getStatus() ? "selected" : ""%> value="<%=doc%>"><%=I_DocStatus.fieldDocumentStatus[doc]%></option>
                                        <%
                                            }
                                        %>
                                    </select>
                                </div>
                            </div>
                        </div>                            

                    </form>
                </div>

                <div class="box-footer">
                    <button type="button" id="btnSave" class="btn btn-sm btn-primary"><i class="fa fa-save"></i>&nbsp; Simpan</button>
                    <a href="perhitungan_insentif.jsp?command=<%=Command.NONE%>" class="btn btn-sm btn-primary"><i class="fa fa-undo"></i>&nbsp; Batal</a>
                </div>
            </div>
                                    
            <% } %>

        </div>
    </body>
    
    <script type="text/javascript" src="../styles/bootstrap/js/jquery.min.js"></script>
    <script type="text/javascript" src="../styles/datepicker/bootstrap-datetimepicker.js" charset="UTF-8"></script>
    <script>        
        $(document).ready(function () {
            
            $('.datepick').datetimepicker({
                autoclose: true,
                todayBtn: true,
                format: 'yyyy-mm-dd',
                minView: 2
            });
            $('.datepick').attr("autocomplete", "off");
            
            $('#btnSave').click(function() {
                $(this).html("<i class='fa fa-spinner fa-pulse'></i> Tunggu...").attr({"disabled":true});
                $('#formInsentif').submit();
            });
            
            $('a').click(function() {
                $(this).html("<i class='fa fa-spinner fa-pulse'></i> Tunggu...").attr({"disabled":true});                
            });
            
        });        
    </script>
    
</html>

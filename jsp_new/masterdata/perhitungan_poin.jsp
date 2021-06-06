<%-- 
    Document   : perhitungan_poin
    Created on : Apr 24, 2018, 4:05:43 PM
    Author     : Dimata 007
--%>

<%@page import="com.dimata.util.*"%>
<%@page import="com.dimata.gui.jsp.*"%>
<%@page import="com.dimata.qdep.form.*"%>
<%@page import="com.dimata.posbo.entity.masterdata.*"%>
<%@page import="com.dimata.posbo.form.masterdata.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_MEMBER_POINT_SETTING); %>
<%@ include file = "../main/checkuser.jsp" %>

<%//
    int iCommand = FRMQueryString.requestCommand(request);
    long oidPerhitunganPoin = FRMQueryString.requestLong(request, ""+FrmPerhitunganPoin.fieldNames[FrmPerhitunganPoin.FRM_FIELD_PERHITUNGAN_POIN_ID]);
    
    CtrlPerhitunganPoin ctrlPerhitunganPoin = new CtrlPerhitunganPoin(request);
    int error = ctrlPerhitunganPoin.action(iCommand, oidPerhitunganPoin, userId, userName);
    String message = ctrlPerhitunganPoin.getMessage();
    
    String order = "" + PstPerhitunganPoin.fieldNames[PstPerhitunganPoin.FLD_STATUS_AKTIF] + " DESC "
            + ", " + PstPerhitunganPoin.fieldNames[PstPerhitunganPoin.FLD_UPDATE_DATE]  + " DESC ";
    Vector listPerhitunganPoin = PstPerhitunganPoin.list(0, 0, "", order);
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
            
            .tabel_data {font-size: 14px;}
            .tabel_data th {
                text-align: center; 
                background-color: lightgray !important;
            }
            .box .box-header {border-color: lightgray;}
            
        </style>
    </head>
    <body>
        <div class="col-sm-12">
            <br>
            <div class="box box-primary">
                <div class="box-header with-border">
                    <h3 class="box-title">Perhitungan Poin</h3>
                </div>
                
                <div class="box-body">
                    <button type="button" id="btnAdd" class="btn btn-sm btn-primary"><i class="fa fa-plus"></i>&nbsp; Tambah Perhitungan Poin</button>
                    <p></p>
                    <table class="table table-bordered table-striped tabel_data">
                        <tr>
                            <th style="width: 1%">No.</th>
                            <th>Tipe Item</th>
                            <th>Penjualan</th>
                            <th>Poin</th>
                            <th>Tanggal Update</th>
                            <th>Status</th>
                        </tr>
                        
                        <%
                            for (int i = 0; i < listPerhitunganPoin.size(); i++) {
                                PerhitunganPoin pp = (PerhitunganPoin) listPerhitunganPoin.get(i);
                                String statusAktif[] = {"Tidak aktif", "Aktif"};
                        %>
                        
                        <tr>
                            <td><%=(i+1)%></td>
                            <td class="text-center"><%=Material.MATERIAL_TYPE_TITLE[pp.getMaterialJenisType()] %></td>
                            <td class="text-right"><%=String.format("%,.0f", pp.getSellValue()) %>.00</td>
                            <td class="text-center"><%=pp.getPoinReward() %></td>
                            <td class="text-center"><%=Formater.formatDate(pp.getUpdateDate(), "yyyy-MM-dd hh:mm:ss") %></td>
                            <td class="text-center"><%=statusAktif[pp.getStatusAktif()] %></td>
                        </tr>
                        
                        <%}%>
                        
                    </table>
                        
                    <p></p>
                    <div class="row">
                        <div class="col-sm-12">
                            <% if (error > 0) {%>
                            <div class="label-danger text-center"><%=message%></div>
                            <% } else {%>
                            <div class="label-success text-center"><%=message%></div>
                            <% }%>
                        </div>
                    </div>

                </div>
            </div>

            <% if (iCommand == Command.ADD) {%>
                        
            <div class="box box-primary">
                
                <div class="box-header with-border">
                    <h3 class="box-title">Tambah Perhitungan Baru</h3>
                </div>
                
                <form id="formAdd" class="form-horizontal">
                    <div class="box-body">
                        <input type="hidden" name="command" value="<%=Command.SAVE %>">
                        <input type="hidden" name="<%=FrmPerhitunganPoin.fieldNames[FrmPerhitunganPoin.FRM_FIELD_STATUS_AKTIF]%>" value="1">
                        
                        <p></p>
                        <div class="form-group">
                            <label class="col-sm-2">Tipe Item</label>
                            <div class="col-sm-2">                                            
                                <%
                                    Vector valTipeItem = new Vector();
                                    Vector keyTipeItem = new Vector();

                                    valTipeItem.add(""+Material.MATERIAL_TYPE_EMAS);
                                    valTipeItem.add(""+Material.MATERIAL_TYPE_BERLIAN);

                                    keyTipeItem.add(""+Material.MATERIAL_TYPE_TITLE[Material.MATERIAL_TYPE_EMAS]);
                                    keyTipeItem.add(""+Material.MATERIAL_TYPE_TITLE[Material.MATERIAL_TYPE_BERLIAN]);                                                    
                                %>
                                <%=ControlCombo.draw(""+FrmPerhitunganPoin.fieldNames[FrmPerhitunganPoin.FRM_FIELD_MATERIAL_JENIS_TYPE], "form-control input-sm", null, "", valTipeItem, keyTipeItem, "required")%>
                            </div>
                        </div>
                            
                        <div class="form-group">
                            <label class="col-sm-2">Nilai Penjualan</label>
                            <div class="col-sm-2">
                                <input type="text" required="" id="nilaiJual" class="form-control input-sm" name="<%=FrmPerhitunganPoin.fieldNames[FrmPerhitunganPoin.FRM_FIELD_SELL_VALUE]%>">
                            </div>
                        </div>
                            
                        <div class="form-group">
                            <label class="col-sm-2">Poin</label>
                            <div class="col-sm-2">
                                <input type="text" required="" class="form-control input-sm" name="<%=FrmPerhitunganPoin.fieldNames[FrmPerhitunganPoin.FRM_FIELD_POIN_REWARD]%>">
                            </div>
                        </div>                    

                    </div>

                    <div class="box-footer">
                        <button type="submit" class="btn btn-sm btn-primary"><i class="fa fa-save"></i>&nbsp; Simpan</button>
                        <button type="button" id="btnCancel" class="btn btn-sm btn-primary"><i class="fa fa-undo"></i>&nbsp; Batal</button>
                    </div>
                            
                </form>
                            
            </div>

            <% } %>
                        
        </div>        
    </body>
    
    <script src="../styles/bootstrap/js/jquery.min.js" type="text/javascript"></script>
    <script>
        
        $('#btnAdd').click(function() {
            $(this).html("<i class='fa fa-spinner fa-pulse'></i> Tunggu...").attr({"disabled":true});
            window.location.href = "perhitungan_poin.jsp?command="+<%=Command.ADD %>;
        });
        
        $('#btnCancel').click(function() {
            $(this).html("<i class='fa fa-spinner fa-pulse'></i> Tunggu...").attr({"disabled":true});
            window.location.href = "perhitungan_poin.jsp?command="+<%=Command.NONE %>;
        });
        
        $('#nilaiJual').keyup(function() {            
            var nilai = $(this).val().replace(/,/g , "");
            $(this).val(Number(nilai).toLocaleString());
        });
        
    </script>
</html>

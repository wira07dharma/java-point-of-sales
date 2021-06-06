<%-- 
    Document   : list_production_chain
    Created on : Aug 10, 2019, 10:02:25 AM
    Author     : Dimata 007
--%>

<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.posbo.entity.masterdata.ChainMain"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstChainMain"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../../main/javainit.jsp" %>
<%int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_PENENTUAN_HPP);%>
<%@ include file = "../../main/checkuser.jsp" %>

<%//
    int iCommand = FRMQueryString.requestCommand(request);
    String title = FRMQueryString.requestString(request, "FRM_TITLE");
    String[] multiLocationId = FRMQueryString.requestStringValues(request, "FRM_LOCATION");
    String note = FRMQueryString.requestString(request, "FRM_NOTE");

    String where = "";
    if (!title.isEmpty()) {
        where += where.isEmpty() ? "" : " AND ";
        where += PstChainMain.fieldNames[PstChainMain.FLD_CHAIN_TITLE] + " LIKE '%" + title + "%'";
    }
    if (multiLocationId != null && multiLocationId.length > 0) {
        try {
            String locationId = Arrays.toString(multiLocationId);
            locationId = locationId.substring(1, locationId.length()-1);
            where += where.isEmpty() ? "" : " AND ";
            where += PstChainMain.fieldNames[PstChainMain.FLD_CHAIN_LOCATION] + " IN (" + locationId + ")";
        } catch (Exception e) {
            
        }
    }
    if (!note.isEmpty()) {
        where += where.isEmpty() ? "" : " AND ";
        where += PstChainMain.fieldNames[PstChainMain.FLD_CHAIN_NOTE] + " LIKE '%" + note + "%'";
    }
    Vector<ChainMain> listChainMain = PstChainMain.list(0, 0, where, PstChainMain.fieldNames[PstChainMain.FLD_CHAIN_DATE] + " DESC ");
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
            body {background-color: #eeeeee;}
            .box .box-header, .box .box-footer {border-color: lightgray;}
            .form-group label {padding-top: 5px}
            .select2-selection {border-radius: 0% !important; border-color: lightgray !important}
            
            .tabel_data tbody tr th {
                //background-color: #f39c12;
                //color: white;
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
            <h3>Template Produksi</h3>
            <div class="box box-default">
                <div class="box-header with-border">
                    <h3 class="box-title">Pencarian</h3>
                </div>
                <div class="box-body">
                    <form id="formSearch" class="form-horizontal" style="margin: 0px">
                        <div class="form-group">
                            <label class="col-sm-1 ">Title</label>
                            <div class="col-sm-3">
                                <input type="text" class="form-control input-sm" name="FRM_TITLE" value="<%=title%>">
                            </div>
                        
                            <label class="col-sm-1 ">Lokasi</label>
                            <div class="col-sm-3">
                                <select multiple="" class="form-control input-sm multiSelect" name="FRM_LOCATION">
                                    <%
                                        Vector listLokasi = PstLocation.list(0, 0, "", "" + PstLocation.fieldNames[PstLocation.FLD_NAME]);
                                        for (int i = 0; i < listLokasi.size(); i++) {
                                            String selected = "";
                                            Location l = (Location) listLokasi.get(i);
                                            if (multiLocationId != null && multiLocationId.length > 0) {
                                                for (String s : multiLocationId) {
                                                    if (l.getOID() == Long.valueOf(s)) {
                                                        selected = "selected";
                                                        break;
                                                    }
                                                }
                                            }
                                    %>
                                    <option <%=selected%> value="<%=l.getOID()%>"><%=l.getName()%></option>
                                    <%
                                        }
                                    %>
                                </select>
                            </div>
                        
                            <label class="col-sm-1 ">Keterangan</label>
                            <div class="col-sm-3">
                                <input type="text" class="form-control input-sm" name="FRM_NOTE" value="<%=note%>">
                            </div>
                        </div>
                    </form>
                </div>
                <div class="box-footer">
                    <button id="btnSearchChain" type="button" class="btn btn-sm btn-primary"><i class="fa fa-search"></i>&nbsp; Cari</button>
                    <a id="btnAddChain" class="btn btn-sm btn-primary" href="add_chain.jsp"><i class="fa fa-plus"></i>&nbsp; Tambah Template</a>
                </div>
            </div>
                       
            <div class="box box-default">
                <div class="box-header with-border">
                    <h3 class="box-title">Daftar Template</h3>
                </div>
                <div class="box-body">
                    <table class="table table-bordered tabel_data">
                        <tr class="label-default">
                            <th style="width: 1%" class="text-center">No.</th>
                            <th>Title</th>
                            <th>Lokasi</th>
                            <th>Keterangan</th>
                        </tr>
                        <%
                            if (listChainMain.isEmpty()) {
                                out.print("<tr><td colspan='4' class='text-center'>Tidak ada data</td></tr>");
                            } else {
                                int no = 0;
                                for (ChainMain cm : listChainMain) {
                                    no++;
                                    String lokasi = "-";
                                    if (cm.getChainLocation() != 0) {
                                        try {
                                            lokasi = PstLocation.fetchExc(cm.getChainLocation()).getName();
                                        } catch (Exception e) {
                                            System.out.println(e.getMessage());
                                        }
                                    }
                        %>

                        <tr>
                            <td><%=no%>.</td>
                            <td><a href="add_chain.jsp?command=<%=Command.EDIT%>&hidden_chain_id=<%=cm.getOID()%>"><%=cm.getChainTitle()%></a></td>
                            <td><%=lokasi%></td>
                            <td><%=cm.getChainNote()%></td>
                        </tr>

                        <%
                                }
                            }
                        %>
                    </table>
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
        $('.multiSelect').select2({"placeholder": "Semua lokasi","width":"100%"});

        $('#btnAddChain').click(function(){
            $(this).html("<i class='fa fa-spinner fa-pulse'></i> Tunggu...").attr({"disabled": true});
        });
        
        $('#btnSearchChain').click(function(){
            $(this).html("<i class='fa fa-spinner fa-pulse'></i> Tunggu...").attr({"disabled": true});
            $('#formSearch').submit();
        });
        
        $('.btnEdit').click(function(){
            $(this).html("<i class='fa fa-spinner fa-pulse'></i>").attr({"disabled": true});
        });

    </script>
    
</html>

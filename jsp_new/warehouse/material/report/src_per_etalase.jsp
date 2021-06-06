<%-- 
    Document   : src_per_etalase
    Created on : Mar 8, 2018, 2:59:35 PM
    Author     : Dimata 007
--%>

<%@page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterial"%>
<%@page import="com.dimata.posbo.session.warehouse.SessMatCostingStokFisik"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstKsg"%>
<%@page import="com.dimata.posbo.entity.masterdata.Ksg"%>
<%@page import="com.dimata.gui.jsp.ControlCombo"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK_REPORT, AppObjInfo.OBJ_STOCK_REPORT); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<%
    int iCommand = FRMQueryString.requestCommand(request);
    long idLocation = FRMQueryString.requestLong(request, "FRM_LOCATION");
    long idEtalase = FRMQueryString.requestLong(request, "FRM_ETALASE");

    //get location
    Vector val_location = new Vector(1, 1);
    Vector key_location = new Vector(1, 1);

    boolean checkDataAllLocReportView = PstDataCustom.checkDataAllLocReportView(userId, "user_view_sale_stock_report_location", "0");

    if (checkDataAllLocReportView) {
        val_location.add("0");
        key_location.add("Semua Lokasi");
    }

    String whereLocViewReport = PstDataCustom.whereLocReportViewStock(userId, "user_view_sale_stock_report_location");
    Vector vt_loc = PstLocation.list(0, 0, whereLocViewReport, PstLocation.fieldNames[PstLocation.FLD_NAME]);

    for (int d = 0; d < vt_loc.size(); d++) {
        Location loc = (Location) vt_loc.get(d);
        val_location.add("" + loc.getOID());
        key_location.add("" + loc.getName());
    }

    //get etalase
    String where = "";
    String order = "" + PstKsg.fieldNames[PstKsg.FLD_LOCATION_ID] + "," + PstKsg.fieldNames[PstKsg.FLD_CODE];
    if (idLocation != 0) {
        where = "" + PstKsg.fieldNames[PstKsg.FLD_LOCATION_ID] + "='" + idLocation + "'";
    }
    Vector listKsg = PstKsg.list(0, 0, where, order);

    Vector vectKsgVal = new Vector(1, 1);
    Vector vectKsgKey = new Vector(1, 1);

    vectKsgVal.add("");
    vectKsgKey.add("Semua Etalase");

    for (int i = 0; i < listKsg.size(); i++) {
        Ksg matKsg = (Ksg) listKsg.get(i);
        Location loc = new Location();
        if (matKsg.getLocationId() > 0) {
            loc = PstLocation.fetchExc(matKsg.getLocationId());
        }
        vectKsgKey.add(loc.getCode() + " - " + matKsg.getCode());
        vectKsgVal.add("" + matKsg.getOID());
    }
%>

<%
    Vector listEtalase = new Vector();
    if (iCommand == Command.LIST) {
        //get etalase per location
        String whereClause = "";
        String orderClause = " LOC." + PstLocation.fieldNames[PstLocation.FLD_CODE] + ", KSG." + PstKsg.fieldNames[PstKsg.FLD_CODE];
        if (idLocation != 0) {
            whereClause += " LOC." + PstKsg.fieldNames[PstKsg.FLD_LOCATION_ID] + " IN (" + idLocation + ")";
        }
        if (idEtalase != 0) {
            if (!whereClause.equals("")) {
                whereClause += " AND ";
            }
            whereClause += " KSG." + PstKsg.fieldNames[PstKsg.FLD_KSG_ID] + " IN (" + idEtalase + ")";
        }
        listEtalase = PstKsg.listJoinLocation(0, 0, whereClause, orderClause);
    }
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap/css/bootstrap.min.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/dist/css/AdminLTE.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/font-awesome/font-awesome.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/select2/css/select2.min.css" />
        <style>
            .tabel_data {
                width: 50%;
                font-size: 14px;
                //border-color: black !important
            }
            
            .tabel_data th {
                text-align: center;
                //border-color: black !important;                
                border-bottom-width: thin !important;
                padding: 4px 8px !important;
                background-color: lightgray !important
            }
            .tabel_data td {
                //border-color: black !important;
                padding: 4px 8px !important
            }
        </style>
        
        <script type="text/javascript" src="../../../styles/bootstrap/js/jquery.min.js"></script>
        <script type="text/javascript" src="../../../styles/bootstrap/js/bootstrap.min.js"></script>  
        <script type="text/javascript" src="../../../styles/select2/js/select2.min.js"></script>
        <script type="text/javascript">
            $(document).ready(function(){
                
                $('.loc').change(function(){
                    $('#command').val("<%=Command.NONE%>");
                    $('#formSearch').submit();
                });
                
                $('#btnSrc').click(function(){
                    //$(this).html("<i class='fa fa-spinner fa-pulse'></i>&nbsp; Tunggu...");
                    $('#command').val("<%=Command.LIST%>");
                    $('#formSearch').submit();
                });
                
                $('#btnPrint').click(function(){
                    window.open("report_per_etalase.jsp?FRM_LOCATION="+"<%=idLocation%>"+"&FRM_ETALASE="+"<%=idEtalase%>");
                });
                
            });
        </script>
    </head>
    <body style="background-color: #EEEEEE">
        <br>
        <div class="col-md-12">
            <div class="box box-primary">

                <div class="box-header with-border">
                    <h3 class="box-title">Form Pencarian</h3>
                </div>

                <form name="" method="POST" id="formSearch" class="form-horizontal" role="form">
                    <input type="hidden" name="command" id="command" value="<%=iCommand%>">

                    <div class="box-body">
                        <div class="form-group">                            
                            <label class="col-sm-1 control-label">Lokasi</label>
                            <div class="col-sm-3">                                
                                <%=(ControlCombo.draw("FRM_LOCATION", null, ""+idLocation, val_location, key_location, "", "form-control input-sm loc"))%>                                
                            </div>                            
                        </div>                        
                        <div class="form-group">                            
                            <label class="col-sm-1 control-label">Etalase</label>
                            <div class="col-sm-3">
                                <%=ControlCombo.draw("FRM_ETALASE", "form-control input-sm eta", null, ""+idEtalase, vectKsgVal, vectKsgKey, "onChange='' ")%>
                            </div>                            
                        </div>                        
                    </div>

                    <div class="box-footer">
                        <button type="button" id="btnSrc" class="btn btn-sm btn-primary"><i class="fa fa-search"></i>&nbsp; Cari</button>
                    </div>

                </form>
            </div>
                            
            <%if(iCommand == Command.LIST) {%>
            
            <div class="box box-primary">
                <div class="box-header with-border">
                    <h3 class="box-title">Laporan Quantity Per Etalase</h3>
                </div>
                
                <div class="box-body">
                    <%if (!listEtalase.isEmpty()) {%>
                    <div>
                        <button type="button" id="btnPrint" class="btn btn-sm btn-primary"><i class="fa fa-print"></i>&nbsp; Cetak</button>
                    </div>
                    <p></p>
                    <%}%>
                    <table class="table table-bordered table-striped tabel_data">
                        <tr>
                            <th style="width: 1%">No.</th>
                            <th>Lokasi</th>
                            <th>Etalase</th>
                            <th>Qty</th>
                        </tr>
                        <%
                            long idLoc = 0;
                            int number = 0;
                            double totalPerEtalase = 0;
                            for (int i = 0; i < listEtalase.size(); i++) {
                                Ksg etalase = (Ksg) listEtalase.get(i);
                                Location etaLoc = new Location();
                                double stokItem = 0;
                                try {
                                    if (etalase.getLocationId() > 0) {
                                        etaLoc = PstLocation.fetchExc(etalase.getLocationId());
                                        Vector listItem = PstMaterial.list(0, 0, "" + PstMaterial.fieldNames[PstMaterial.FLD_GONDOLA_CODE] + "='" + etalase.getOID() + "'", "");
                                        for (int j = 0; j < listItem.size(); j++) {
                                            Material material = (Material) listItem.get(j);
                                            stokItem += SessMatCostingStokFisik.qtyMaterialBasedOnStockCard(material.getOID(), etaLoc.getOID(), new Date(), 0);
                                        }
                                    }
                                } catch (Exception e) {
                                    System.out.println(e.getMessage());
                                }
                        %>
                                                
                        <%if (i != 0 && idLoc != etaLoc.getOID()) {%>
                        <tr>
                            <td></td>
                            <td></td>
                            <td class="text-right"><b>Total :</b></td>
                            <td class="text-center"><b><%=String.format("%,.0f", totalPerEtalase)%></b></td>
                        </tr>
                        <%}%>                
                        
                        <%
                                //set table view                                
                                String locCode = "";
                                String locNumber = "";                                
                                if (idLoc != etaLoc.getOID()) {
                                    totalPerEtalase = 0;
                                    totalPerEtalase += stokItem;
                                    number += 1;
                                    locNumber = "" + number;
                                    idLoc = etaLoc.getOID();
                                    locCode = etaLoc.getCode();
                                } else {
                                    totalPerEtalase += stokItem;
                                }
                        %>
                        
                        <tr>
                            <td><%=locNumber%></td>
                            <td><%=locCode%></td>
                            <td><%=etalase.getCode()%></td>
                            <td class="text-center"><%=String.format("%,.0f",stokItem)%></td>
                        </tr>
                        
                        <%
                            }
                        %>
                        
                        <tr>
                            <td></td>
                            <td></td>
                            <td class="text-right"><b>Total :</b></td>
                            <td class="text-center"><b><%=String.format("%,.0f", totalPerEtalase)%></b></td>
                        </tr>
                
                        <%
                            if (listEtalase.isEmpty()) {
                        %>
                        <tr><td colspan="4" class="text-center">Data tidak ditemukan</td></tr>
                        <%
                            }
                        %>
                    </table>
                </div>
            </div>
            
            <%}%>
            
        </div>
    </body>
</html>

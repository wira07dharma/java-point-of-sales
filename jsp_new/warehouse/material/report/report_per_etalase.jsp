<%-- 
    Document   : report_per_etalase
    Created on : Mar 8, 2018, 2:54:41 PM
    Author     : Dimata 007
--%>

<%@page import="com.dimata.posbo.session.warehouse.SessMatCostingStokFisik"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.posbo.entity.masterdata.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../../../main/javainit.jsp" %>
<%//
    int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER, AppObjInfo.OBJ_TRANSFER);
%>
<%@ include file = "../../../main/checkuser.jsp" %>

<%//
    Vector<Company> company = PstCompany.list(0, 0, "", "");
    String compName = "";
    if (!company.isEmpty()) {
        compName = company.get(0).getCompanyName().toUpperCase();
    }
%>

<%
    long idLocation = FRMQueryString.requestLong(request, "FRM_LOCATION");
    long idEtalase = FRMQueryString.requestLong(request, "FRM_ETALASE");
    
    Vector listEtalase = new Vector();
    //get etalase per location
    String whereClause = "";
    String orderClause = " LOC." + PstLocation.fieldNames[PstLocation.FLD_CODE] + ", KSG." + PstKsg.fieldNames[PstKsg.FLD_CODE];
    
    String locName = "";
    Location loc = new Location();
    if (idLocation != 0) {
        loc = PstLocation.fetchExc(idLocation);
        locName = loc.getName();
        whereClause += " LOC." + PstKsg.fieldNames[PstKsg.FLD_LOCATION_ID] + " IN (" + idLocation + ")";
    } else {
        locName = "Semua Lokasi";
    }
    
    String etaName = "";
    Ksg ksg = new Ksg();
    if (idEtalase != 0) {
        ksg = PstKsg.fetchExc(idEtalase);
        etaName = ksg.getCode();
        if (!whereClause.equals("")) {
            whereClause += " AND ";
        }
        whereClause += " KSG." + PstKsg.fieldNames[PstKsg.FLD_KSG_ID] + " IN (" + idEtalase + ")";
    } else {
        etaName = "Semua Etalase";
    }
    
    listEtalase = PstKsg.listJoinLocation(0, 0, whereClause, orderClause);
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap/css/bootstrap.min.css"/>
        <style>

            table {font-size: 14px}
            
            .tabel_header {width: 100%}
            .tabel_header th {text-align: center; background-color: lightgray !important}
            .tabel_header td {padding-bottom: 5px}
            
            .tabel_data {
                width: 50%;
                border-color: black !important
            }
            .tabel_data th {
                text-align: center;
                border-color: black !important;                
                border-bottom-width: thin !important;
                padding: 4px 8px !important;
            }
            .tabel_data td {
                border-color: black !important;
                padding: 4px 8px !important
            }

        </style>
    </head>
    <body>
        <div style="margin: 10px">
            
            <br>
            <div>
                <img style="width: 100px" src="../../../images/litama.jpeg">
                <span style="font-size: 24px; font-family: TimesNewRomans"><b><%=compName%></b> <small><i>Gallery</i></small></span>
            </div>
            
            <div>
                <h4><b>Laporan Quantity Stok Per Etalase</b></h4>
            </div>
            
            <table class="tabel_header">
                <tr>
                    <td style="width: 5%">Lokasi</td>
                    <td style="">: <%=locName%></td>
                </tr>
                <tr>                    
                    <td>Etalase</td>
                    <td style="">: <%=etaName%></td>
                </tr>
                <tr>                    
                    <td>Tanggal</td>
                    <td style="">: <%=Formater.formatDate(new Date(),"dd MMMM yyyy")%></td>
                </tr>
            </table>
                
            <hr style="border-width: medium; border-color: black; margin-top: 5px">

            <table class="table table-bordered tabel_data">
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
                        String rowSpan = "";
                        try {
                            if (etalase.getLocationId() > 0) {
                                //get data location
                                etaLoc = PstLocation.fetchExc(etalase.getLocationId());                                
                                //get total qty item per etalase
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
                    <td class="text-right"><b>Total :</b></td>
                    <td class="text-center"><b><%=String.format("%,.0f", totalPerEtalase)%></b></td>
                </tr>
                <%}%>
                
                <tr>
                    
                <%
                    //set table view                                
                    String locCode = "";
                    String locNumber = "";
                    if (idLoc != etaLoc.getOID()) {
                        totalPerEtalase = 0;
                        number += 1;
                        locNumber = "" + number;
                        idLoc = etaLoc.getOID();
                        locCode = etaLoc.getCode();
                        //get number of etalase per location for rowspan
                        rowSpan = "" + (1+PstKsg.getCount("" + PstKsg.fieldNames[PstKsg.FLD_LOCATION_ID] + "=" + etaLoc.getOID()));
                        totalPerEtalase += stokItem;
                %>
                
                    <td rowspan="<%=rowSpan%>"><%=locNumber%></td>
                    <td rowspan="<%=rowSpan%>"><%=locCode%></td>
                
                <%
                    } else {
                        totalPerEtalase += stokItem;
                    }
                %>                
                    
                    <td><%=etalase.getCode()%></td>
                    <td class="text-center"><%=String.format("%,.0f", stokItem)%></td>                    
                </tr>                                                
                
                <%
                    }
                %>
                
                <tr>
                    <td class="text-right"><b>Total :</b></td>
                    <td class="text-center"><b><%=String.format("%,.0f", totalPerEtalase)%></b></td>
                </tr>
                
                <%if (listEtalase.isEmpty()) {%>
                <tr><td colspan="4" class="text-center">Data tidak ditemukan</td></tr>
                <%}%>
            </table>
        </div>
    </body>
</html>

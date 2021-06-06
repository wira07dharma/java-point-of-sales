<%-- 
    Document   : print_out_transfer_item_lebur
    Created on : Feb 5, 2018, 1:30:51 PM
    Author     : Dimata 007
--%>

<%@page import="com.dimata.posbo.entity.warehouse.PstMatDispatchItem"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatDispatchItem"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstColor"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstCategory"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterial"%>
<%@page import="com.dimata.posbo.entity.masterdata.Color"%>
<%@page import="com.dimata.posbo.entity.masterdata.Category"%>
<%@page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatDispatch"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatDispatch"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatReceiveItem"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatReceiveItem"%>
<%@page import="com.dimata.posbo.entity.masterdata.MaterialDetail"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterialDetail"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatDispatchReceiveItem"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatDispatchReceiveItem"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstCompany"%>
<%@page import="com.dimata.posbo.entity.masterdata.Company"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../../../main/javainit.jsp" %>
<%//    
    int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER, AppObjInfo.OBJ_TRANSFER);
%>
<%@ include file = "../../../main/checkuser.jsp" %>
<%//
    long oidDispatchMaterial = FRMQueryString.requestLong(request, "hidden_dispatch_id");
    boolean hidePrice = FRMQueryString.requestBoolean(request, "hidePrice");
    
    MatDispatch dispatch = new MatDispatch();
    Location locAsal = new Location();
    Location locTujuan = new Location();
    try {
        dispatch = PstMatDispatch.fetchExc(oidDispatchMaterial);
        locAsal = PstLocation.fetchExc(dispatch.getLocationId());
        locTujuan = PstLocation.fetchExc(dispatch.getDispatchTo());
    } catch (Exception e) {

    }
    
    //String order = "DFRI." + PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DF_REC_GROUP_ID];
    String order = " RIGHT(MAT1." + PstMaterial.fieldNames[PstMaterial.FLD_SKU]+",7) ASC";
    Vector listMatDispatchReceiveItem = PstMatDispatchReceiveItem.listGroup(0, 0, oidDispatchMaterial, order);

    Vector<Company> company = PstCompany.list(0, 0, "", "");
    String compName = "";
    if (!company.isEmpty()) {
        compName = company.get(0).getCompanyName().toUpperCase();
    }
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap/css/bootstrap.min.css"/>
        <style>

            table {font-size: 12px}
            .tabelHeader td {padding-bottom: 5px}
            .tabelHeader td {vertical-align: text-top}

            .tabel_group th {text-align: center}
            .tabel_group {border-color: black}
            .tabel_group th {border-color: black !important}
            .tabel_group td {border-color: black !important}
            .tabel_group th {border-bottom-width: thin !important}

            .tabel_source {margin-bottom: 0px !important; border-color: black}
            .tabel_source th {border-bottom-width: thin !important}
            .tabel_source {font-size: 11px}

            .tabel_target {margin-bottom: 0px !important; border-color: black}
            .tabel_target th {border-bottom-width: thin !important}
            .tabel_target {font-size: 11px}

        </style>
    </head>
    <body>
        <div style="margin: 10px">
            <br>
            <div>
                <img style="width: 100px" src="../../../images/litama.jpeg">
                <span style="font-size: 24px; font-family: TimesNewRomans">
                    <b><%=compName%></b>
                    <small><i>Gallery</i></small>
                </span>
            </div>
            <div>
                <h4><b>Laporan Item Dilebur</b></h4>
            </div>
            <div>
                <table class="tabelHeader" style="width: 100%">
                    <tr>
                        <td style="width: 10%">Nomor</td>
                        <td style="width: 20%">: <%=dispatch.getDispatchCode()%></td>

                        <td style="width: 10%">Lokasi Awal</td>
                        <td style="width: 25%">: <%=locAsal.getName()%></td>

                        <td style="width: 12%">Keterangan</td>
                        <td style="width: ">: <%=dispatch.getRemark()%></td>
                    </tr>
                    <tr>
                        <td>Tanggal</td>
                        <td>: <%=Formater.formatDate(new Date(), "dd/MMM/yyyy")%></td>

                        <td>Lokasi Tujuan</td>
                        <td>: <%=locTujuan.getName()%></td>
                        <%--
                        <td>Tipe Item Dilebur</td>
                        <td>: <%=Material.MATERIAL_TYPE_TITLE[dispatch.getDispatchItemType()]%></td>
                        --%>
                    </tr>
                </table>
            </div>

            <hr style="border-width: medium; border-color: black; margin-top: 5px">

            <table class="table table-bordered tabel_group">
                <thead>
                    <tr>
                        <th>Group</th>
                        <th>Source</th>
                        <th>Target</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        for (int i = 0; i < listMatDispatchReceiveItem.size(); i++) {
                            MatDispatchReceiveItem dfRecItem = (MatDispatchReceiveItem) listMatDispatchReceiveItem.get(i);
                            //get item source & target
                            String orderItem = "DFRI." + PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DF_REC_GROUP_ID];
                            Vector listItem = PstMatDispatchReceiveItem.list(0, 0, dfRecItem.getDfRecGroupId(), orderItem);
                    %>
                    <tr>
                        <td style="width: 1%; text-align: center; vertical-align: middle"><%=(i + 1)%></td>
                        <!--source-->                        
                        <td>
                            <table class="table table-bordered tabel_source">
                                <thead>
                                    <tr>
                                        <th>SKU</th>
                                        <th>Nama</th>
                                        <th>Qty</th>
                                        <th>Berat</th>
                                        <% if (!hidePrice) {%>
                                        <th>Harga Emas</th>
                                        <th>Oks/Batu</th>
                                        <th>Total HP</th>
                                        <% } %>
                                    </tr>
                                </thead>
                                <tbody>       
                                    <%
                                        for (int j = 0; j < listItem.size(); j++) {
                                            MatDispatchReceiveItem dfRecItemSource = (MatDispatchReceiveItem) listItem.get(j);
                                            Vector<MaterialDetail> listMatDetail = PstMaterialDetail.list(0, 0, "" + PstMaterialDetail.fieldNames[PstMaterialDetail.FLD_MATERIAL_ID] + " = '" + dfRecItemSource.getSourceItem().getMaterialId() + "'", "");
                                            MaterialDetail md = new MaterialDetail();
                                            if (!listMatDetail.isEmpty()) {
                                                md = listMatDetail.get(0);
                                            }
                                            
                                            MatDispatchItem mdi = new MatDispatchItem();
                                            Material mat = new Material();
                                            Category category = new Category();
                                            Color color = new Color();
                                            try {
                                                mdi = PstMatDispatchItem.fetchExc(dfRecItemSource.getSourceItem().getOID());
                                                mat = PstMaterial.fetchExc(dfRecItemSource.getSourceItem().getMaterialId());
                                                category = PstCategory.fetchExc(mat.getCategoryId());
                                                color = PstColor.fetchExc(mat.getPosColor());
                                            } catch (Exception e) {
                                                System.out.println(e.getMessage());
                                            }
            
                                            String itemName = mat.getName();
                                            if (mat.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS) {
                                                itemName = "" + category.getName() + " " + color.getColorName() + " " + mat.getName();
                                            } else if (mat.getMaterialJenisType() == Material.MATERIAL_TYPE_BERLIAN) {
                                                itemName = "" + category.getName() + " " + color.getColorName() + " Berlian " + mat.getName();
                                            }
                                            
                                            if (dfRecItemSource.getSourceItem().getMaterialId() != 0) {
                                    %>
                                    <tr>
                                        <td><%=dfRecItemSource.getSourceItem().getMaterialSource().getSku()%></td>
                                        <td><%=itemName%></td>
                                        <td class="text-right"><%=String.format("%,.0f", dfRecItemSource.getSourceItem().getQty())%></td>
                                        <td class="text-right"><%=String.format("%,.3f", mdi.getBeratCurrent())%></td>
                                        <% if (!hidePrice) {%>
                                        <td class="text-right"><%=String.format("%,.0f", dfRecItemSource.getSourceItem().getHpp())%>.00</td>
                                        <td class="text-right"><%=String.format("%,.0f", md.getOngkos())%>.00</td>
                                        <td class="text-right"><%=String.format("%,.0f", dfRecItemSource.getSourceItem().getHppTotal())%>.00</td>
                                        <% } %>
                                    </tr>
                                    <%}%>
                                    <%}%>
                                </tbody>
                            </table>
                        </td>
                        <!--target-->
                        <td>
                            <table class="table table-bordered tabel_target">
                                <thead>
                                    <tr>
                                        <th>SKU</th>
                                        <th>Nama</th>
                                        <th>Qty</th>
                                        <th>Berat</th>
                                        <% if (!hidePrice) {%>
                                        <th>Harga Emas</th>
                                        <th>Oks/Batu</th>
                                        <th>Total HP</th>
                                        <% } %>
                                        <th>Keterangan</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                        for (int j = 0; j < listItem.size(); j++) {
                                            MatDispatchReceiveItem dfRecItemTarget = (MatDispatchReceiveItem) listItem.get(j);
                                            Vector<MaterialDetail> listMatDetail = PstMaterialDetail.list(0, 0, "" + PstMaterialDetail.fieldNames[PstMaterialDetail.FLD_MATERIAL_ID] + " = '" + dfRecItemTarget.getTargetItem().getMaterialId() + "'", "");

                                            MaterialDetail md = new MaterialDetail();
                                            if (!listMatDetail.isEmpty()) {
                                                md = listMatDetail.get(0);
                                            }

                                            MatReceiveItem mri = new MatReceiveItem();
                                            Material mat = new Material();
                                            Category category = new Category();
                                            Color color = new Color();
                                            try {
                                                mri = PstMatReceiveItem.fetchExc(dfRecItemTarget.getTargetItem().getOID());
                                                mat = PstMaterial.fetchExc(dfRecItemTarget.getTargetItem().getMaterialId());
                                                category = PstCategory.fetchExc(mat.getCategoryId());
                                                color = PstColor.fetchExc(mat.getPosColor());
                                            } catch (Exception e) {

                                            }

                                            String itemName = mat.getName();
                                            if (mat.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS) {
                                                itemName = "" + category.getName() + " " + color.getColorName() + " " + mat.getName();
                                            } else if (mat.getMaterialJenisType() == Material.MATERIAL_TYPE_BERLIAN) {
                                                itemName = "" + category.getName() + " " + color.getColorName() + " Berlian " + mat.getName();
                                            }
                                            
                                            if (dfRecItemTarget.getTargetItem().getMaterialId() != 0) {
                                    %>
                                    <tr>
                                        <td><%=dfRecItemTarget.getTargetItem().getMaterialTarget().getSku()%></td>
                                        <td><%=itemName%></td>
                                        <td class="text-right"><%=String.format("%,.0f", dfRecItemTarget.getTargetItem().getQty())%></td>
                                        <td class="text-right"><%=String.format("%,.3f", mri.getBerat())%></td>
                                        <% if (!hidePrice) {%>
                                        <td class="text-right"><%=String.format("%,.0f", dfRecItemTarget.getTargetItem().getCost())%>.00</td>
                                        <td class="text-right"><%=String.format("%,.0f", md.getOngkos())%>.00</td>
                                        <td class="text-right"><%=String.format("%,.0f", dfRecItemTarget.getTargetItem().getTotal())%>.00</td>
                                        <% } %>
                                        <td><%=mri.getRemark()%></td>
                                    </tr>
                                    <%}%>
                                    <%}%>
                                </tbody>
                            </table>
                        </td>
                    </tr>
                    <%}%>
                </tbody>
            </table>
                
                <!--//START TEMPLATE TANDA TANGAN//-->                
                <% int signType = 0; %>
                <%@ include file = "../../../templates/template_sign.jsp" %>
                <!--//END TEMPLATE TANDA TANGAN//-->
                
        </div>
    </body>
</html>

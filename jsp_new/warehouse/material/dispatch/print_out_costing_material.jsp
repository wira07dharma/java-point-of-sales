<%-- 
    Document   : print_out_costing_material
    Created on : Mar 22, 2019, 3:04:49 PM
    Author     : IanRizky
--%>
<%@page import="com.dimata.posbo.entity.masterdata.PstCosting"%>
<%@page import="com.dimata.posbo.entity.masterdata.Costing"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatCosting"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatCosting"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatCostingItem"%>
<%@page import="com.dimata.posbo.session.masterdata.SessMaterial"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterial"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstColor"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstCategory"%>
<%@page import="com.dimata.posbo.entity.masterdata.Color"%>
<%@page import="com.dimata.posbo.entity.masterdata.Category"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatDispatch"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatDispatch"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstKsg"%>
<%@page import="com.dimata.posbo.entity.masterdata.Ksg"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterialDetail"%>
<%@page import="com.dimata.posbo.entity.masterdata.MaterialDetail"%>
<%@page import="com.dimata.posbo.entity.masterdata.Unit"%>
<%@page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatDispatchItem"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatDispatchItem"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstCompany"%>
<%@page import="com.dimata.posbo.entity.masterdata.Company"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../../../main/javainit.jsp" %>
<%//
    int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER, AppObjInfo.OBJ_TRANSFER);    
%>
<%@ include file = "../../../main/checkuser.jsp" %>
<%//
    long oidCosting = FRMQueryString.requestLong(request, "hidden_costing_id");
	int typePrint = FRMQueryString.requestInt(request, "type_print_tranfer");
    Vector<Company> company = PstCompany.list(0, 0, "", "");
    String compName = "";
    if (!company.isEmpty()) {
        compName = company.get(0).getCompanyName().toUpperCase();
    }
    
    //String order = " DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ITEM_ID];
    String order = " RIGHT(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU]+",7) ASC";
    Vector listMatCostingItem = PstMatCostingItem.list(0, 0, oidCosting, order);
    MatCosting costing = new MatCosting();
    Location lAwal = new Location();
    Location lTujuan = new Location();
    try {
        costing = PstMatCosting.fetchExc(oidCosting);
        lAwal = PstLocation.fetchExc(costing.getLocationId());
        lTujuan = PstLocation.fetchExc(costing.getCostingTo());
    } catch (Exception e) {

    }
	
	Costing cos = new Costing();
	try {
		cos = PstCosting.fetchExc(costing.getCostingId());
	} catch (Exception exc){}
	
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap/css/bootstrap.min.css"/>
        <style>                        
            .tabelHeader td {padding-bottom: 10px; font-size: 12px}
            
            .tabel_data {
                font-size: 11px;
                border-top-color: black !important;
            }
            
            .tabel_data th {
                text-align: center;
                border-color: black !important;
                border-width: thin !important;
            }
            
            .tabel_data td {
                border-color: black !important;
            }
            
            .sum_total td {font-weight: bold}
            /*
            print-area { visibility: hidden; display: none; }
            print-area.preview { visibility: visible; display: block; }
            @media print
            {
                body .main-page * { visibility: hidden; display: none; } 
                body print-area * { visibility: visible; }
                body print-area   { visibility: visible; display: unset !important; position: static; top: 0; left: 0; }
            }
            */
        </style>
    </head>
    <body>
        <div class="main-page">
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
                    <h4><b>Laporan Perpindahan Lokasi Barang</b></h4>
                </div>
                <div>
                    <table class="tabelHeader" style="width: 100%">
                        <tr>
                            <td style="width: 10%">Nomor</td>
                            <td style="width: 20%">: <%=costing.getCostingCode()%></td>
                            
                            <td style="width: 10%">Lokasi Awal</td>
                            <td style="width: 25%">: <%=lAwal.getName()%></td>
                            
                            <td style="width: 15%">Keterangan</td>
                            <td style="width: ">: <%=costing.getRemark()%></td>
                        </tr>
                        <tr>
                            <td>Tanggal</td>
                            <td>: <%=Formater.formatDate(new Date(), "dd/MMM/yyyy")%></td>
                            
                            <td>Lokasi Tujuan</td>
                            <td>: <%=lTujuan.getName()%></td>
                            
                            <td>Tipe Biaya</td>
                            <td>: <%=cos.getName()%></td>
                        </tr>
                    </table>
                </div>

                <hr style="border-width: medium; border-color: black; margin-top: 5px">

                <table class="table table-bordered tabel_data">
                    <thead>
                        <tr>
                            <th>Kode</th>
                            <th>Nama Barang</th>
                            <th>Qty</th>
                            <th>Berat</th>
                            <th>HPP</th>
                            <th>Ongkos</th>
							<th>Total</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            double totalQty = 0;
                            double totalHpp = 0;
                            double totalBerat = 0;
                            double totalCost = 0;
                            double totalTotalHp = 0;
                            for (int i = 0; i < listMatCostingItem.size(); i++) {
                                Vector temp = (Vector) listMatCostingItem.get(i);
                                MatDispatchItem dispatchItemx = (MatDispatchItem) temp.get(0);
                                Material mat = (Material) temp.get(1);
                                Unit unit = (Unit) temp.get(2);
                                MaterialDetail detail = new MaterialDetail();
                                Vector<MaterialDetail> listMatDetail = PstMaterialDetail.list(0, 0, "" + PstMaterialDetail.fieldNames[PstMaterialDetail.FLD_MATERIAL_ID] + "='"+dispatchItemx.getMaterialId()+"'", "");
                                Ksg ksg = new Ksg();
                                Ksg ksgTo = new Ksg();
                                Category category = new Category();
                                Color color = new Color();
                                try {
                                    ksg = PstKsg.fetchExc(dispatchItemx.getGondolaId());
                                    ksgTo = PstKsg.fetchExc(dispatchItemx.getGondolaToId());
                                    category = PstCategory.fetchExc(mat.getCategoryId());
                                    color = PstColor.fetchExc(mat.getPosColor());
                                    if (!listMatDetail.isEmpty()) {
                                        detail = PstMaterialDetail.fetchExc(listMatDetail.get(0).getOID());
                                    }
                                } catch (Exception e) {

                                }
                                String itemName = SessMaterial.setItemNameForLitama(dispatchItemx.getMaterialId());

                                totalQty += dispatchItemx.getQty();
                                totalBeratAwal += dispatchItemx.getBeratLast();
                                totalBeratSekarang += dispatchItemx.getBeratCurrent();
                                totalBeratSelisih += dispatchItemx.getBeratSelisih();
                                totalHargaAwal += dispatchItemx.getHppAwal();
                                totalHargaAkhir += dispatchItemx.getHpp();
                                totalOngkosAwal += detail.getOngkos();
                                totalOngkosAkhir += dispatchItemx.getOngkos();
                                totalTotalHpAwal += dispatchItemx.getHppTotalAwal();
                                totalTotalHp += dispatchItemx.getHppTotal();
                        %>
                        <tr>
                            <td class="text-center"><%=mat.getSku()%></td>
                            <td><%=itemName%></td>
                            <td class="text-center"><%=String.format("%,.0f", dispatchItemx.getQty())%></td>
                            <td class="text-center"><%=ksg.getCode()%></td>
                            <td class="text-center"><%=ksgTo.getCode()%></td>
                            <td class="text-right"><%=String.format("%,.3f", dispatchItemx.getBeratLast())%></td>
                            <td class="text-right"><%=String.format("%,.3f", dispatchItemx.getBeratCurrent())%></td>
                            <td class="text-right"><%=String.format("%,.3f", dispatchItemx.getBeratSelisih())%></td>
							<% if (typePrint != 2){ %>
                            <td class="text-right"><%=String.format("%,.0f", dispatchItemx.getHppAwal())%>.00</td>
                            <td class="text-right"><%=String.format("%,.0f", dispatchItemx.getHpp())%>.00</td>
                            <td class="text-right"><%=String.format("%,.0f", detail.getOngkos())%>.00</td>
                            <td class="text-right"><%=String.format("%,.0f", dispatchItemx.getOngkos())%>.00</td>
                            <td class="text-right"><%=String.format("%,.0f", dispatchItemx.getHppTotalAwal())%>.00</td>
                            <td class="text-right"><%=String.format("%,.0f", dispatchItemx.getHppTotal())%>.00</td>
							<% } %>
                        </tr>
                        <%
                            }
                        %>
                        <tr class="sum_total">
                            <td colspan="2" class="text-right">Total :</td>
                            <td class="text-center"><%=String.format("%,.0f", totalQty)%></td>
                            <td colspan="2"></td>
                            <td class="text-right"><%=String.format("%,.3f", totalBeratAwal)%></td>
                            <td class="text-right"><%=String.format("%,.3f", totalBeratSekarang)%></td>
                            <td class="text-right"><%=String.format("%,.3f", totalBeratSelisih)%></td>
							<% if (typePrint != 2){ %>
                            <td class="text-right"><%=String.format("%,.0f", totalHargaAwal)%>.00</td>
                            <td class="text-right"><%=String.format("%,.0f", totalHargaAkhir)%>.00</td>
                            <td class="text-right"><%=String.format("%,.0f", totalOngkosAwal)%>.00</td>
                            <td class="text-right"><%=String.format("%,.0f", totalOngkosAkhir)%>.00</td>
                            <td class="text-right"><%=String.format("%,.0f", totalTotalHpAwal)%>.00</td>
                            <td class="text-right"><%=String.format("%,.0f", totalTotalHp)%>.00</td>
							<% } %>
                        </tr>
                    </tbody>                                                                                    
                </table>

                <!--//START TEMPLATE TANDA TANGAN//-->
                <% int signType = 0; %>
                <%@ include file = "../../../templates/template_sign.jsp" %>
                <!--//END TEMPLATE TANDA TANGAN//-->
                    
            </div>
        </div>
    </body>
</html>

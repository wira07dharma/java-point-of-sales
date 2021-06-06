<%-- 
    Document   : report_stock_global
    Created on : Mar 6, 2018, 11:36:32 AM
    Author     : Dimata 007
--%>

<%@page import="com.sun.xml.internal.ws.util.StringUtils"%>
<%@page import="com.dimata.posbo.session.warehouse.SessMatCostingStokFisik"%>
<%@page import="com.dimata.posbo.entity.masterdata.*"%>
<%@page import="com.dimata.common.entity.payment.PstPriceType"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.posbo.session.warehouse.SessReportStock"%>
<%@page import="com.dimata.posbo.entity.search.SrcReportStock"%>
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

<%//
    int viewzerostock = FRMQueryString.requestInt(request, "viewzerostock");
    int start = FRMQueryString.requestInt(request, "start");

    SrcReportStock srcReportStock = new SrcReportStock();
    SessReportStock sessReportStock = new SessReportStock();
    try {
        srcReportStock = (SrcReportStock) session.getValue(SessReportStock.SESS_SRC_REPORT_STOCK);
        if (srcReportStock == null) {
            srcReportStock = new SrcReportStock();
        }
    } catch (Exception e) {
        srcReportStock = new SrcReportStock();
    }

    Periode periode = new Periode();
    try {
        periode = PstPeriode.fetchExc(srcReportStock.getPeriodeId());
    } catch (Exception e) {
    }

    Location location = new Location();
    if (srcReportStock.getLocationId() != 0) {
        try {
            location = PstLocation.fetchExc(srcReportStock.getLocationId());
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    } else {
        location.setName("Semua Lokasi");
    }

    boolean calculateQtyNull = false;
    if (viewzerostock == 1) {
        calculateQtyNull = true;
    } else {
        calculateQtyNull = false;
    }

    Vector vectMember = new Vector(1, 1);
    String[] strMember = null;
    Vector listTypeHrga = new Vector();
    strMember = request.getParameterValues("FRM_FIELD_PRICE_TYPE_ID");
    String sStrMember = "";
    if (strMember != null && strMember.length > 0) {
        for (int i = 0; i < strMember.length; i++) {
            try {
                if (strMember[i] != null && strMember[i].length() > 0) {
                    vectMember.add(strMember[i]);
                    sStrMember = sStrMember + strMember[i] + ",";
                }
            } catch (Exception exc) {
                System.out.println("err");
            }
        }
        if (sStrMember != null && sStrMember.length() > 0) {
            sStrMember = sStrMember.substring(0, sStrMember.length() - 1);
            String whereClauses = PstPriceType.fieldNames[PstPriceType.FLD_PRICE_TYPE_ID] + " IN(" + sStrMember + ")";
            listTypeHrga = PstPriceType.list(0, 0, whereClauses, "");
        }
    }

    Vector listStockAll = sessReportStock.getReportStockAllSummary2(srcReportStock, calculateQtyNull, start, 0);
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap/css/bootstrap.min.css"/>
        <style>

            table {font-size: 14px}            

            .tabel_header td {padding-bottom: 5px}

            .tabel_data {border-color: black !important}
            
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
                <h4><b>Laporan Stok Global</b></h4>
            </div>
            
            <table class="tabel_header" style="width: 100%">
                <tr>
                    <td style="width: 10%">Periode</td>
                    <td style="">: <%=periode.getPeriodeName() %> ( <%= periode.getStartDate()%> - <%= periode.getEndDate()%> )</td>
                </tr>
                <tr>                    
                    <td>Lokasi</td>
                    <td style="">: <%=location.getName()%></td>
                </tr>
                <tr>                    
                    <td>Tanggal Dicetak</td>
                    <td style="">: <%=Formater.formatDate(new Date(),"dd MMMM yyyy")%></td>
                </tr>
            </table>
            
            <hr style="border-width: medium; border-color: black; margin-top: 5px">
            
            <table class="table table-bordered tabel_data">
                <thead>
                    <tr>
                        <th style="width: 1%">No.</th>
                        <th>Sku</th>
                        <th>Barcode</th>
                        <th>Nama Barang</th>
                        <th>Etalase</th>
                        <th>Qty</th>
                        <th>Berat</th>
                        <th>Nilai Stok</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        double totalNilaiStock = 0;
                        double totalNilaiJual = 0;
                        double totalQtyStock = 0;
                        double totalBeratStock = 0;
                        for (int i = 0; i < listStockAll.size(); i++) {
                            Vector vt = (Vector)listStockAll.get(i);
                            Material material = (Material)vt.get(0);
                            MaterialStock materialStock = (MaterialStock)vt.get(1);
                            Unit unit = (Unit)vt.get(2);
                            Date dateNow = new Date();
                            double stokSisa = SessMatCostingStokFisik.qtyMaterialBasedOnStockCard(material.getOID(), srcReportStock.getLocationId(), dateNow, 0);
                            materialStock.setQty(stokSisa);
                            
                            Material newmat = new Material();
                            Category category = new Category();
                            Color color = new Color();
                            try {
                                newmat = PstMaterial.fetchExc(materialStock.getMaterialUnitId());
                                category = PstCategory.fetchExc(newmat.getCategoryId());
                                color = PstColor.fetchExc(newmat.getPosColor());
                            } catch (Exception e) {

                            }
                            String itemName = "" + material.getName();
                            if(typeOfBusinessDetail == 2){
                                if (newmat.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS) {
                                    itemName = "" + category.getName() + " " + color.getColorName() + " " + newmat.getName();
                                } else if (newmat.getMaterialJenisType() == Material.MATERIAL_TYPE_BERLIAN) {
                                    itemName = "" + category.getName() + " " + color.getColorName() + " Berlian " + newmat.getName();
                                }
                            }
                            
                            totalQtyStock = totalQtyStock + materialStock.getQty(); 		
                            totalNilaiJual = totalNilaiJual + (material.getDefaultPrice() * materialStock.getQty());
                            totalNilaiStock += (material.getAveragePrice() * materialStock.getQty());
                            totalBeratStock = totalBeratStock + materialStock.getBerat();
                    %>
                    
                    <tr>
                        <td><%=(start+i+1)%></td>
                        <td><%=material.getSku()%></td>
                        <td><%=material.getBarcode()%></td>
                        <td><%=itemName%></td>
                        <td><%=material.getGondolaName()%></td>
                        <td class="text-center"><%=String.format("%,.0f", materialStock.getQty())%></td>
                        <td class="text-right"><%=String.format("%,.3f", materialStock.getBerat())%></td>
                        <td class="text-right"><%=String.format("%,.0f", material.getAveragePrice() * materialStock.getQty())%>.00</td>
                    </tr>
                    
                    <%
                        }
                    %>
                    
                    <tr>
                        <td class="text-right" colspan="5"><b>Total :</b></td>
                        <td class="text-center"><b><%=String.format("%,.0f", totalQtyStock)%></b></td>
                        <td class="text-right"><b><%=String.format("%,.3f", totalBeratStock)%></b></td>
                        <td class="text-right"><b><%=String.format("%,.0f", totalNilaiStock)%>.00</b></td>
                    </tr>
                    
                </tbody>
            </table>
                
                <!--//START TEMPLATE TANDA TANGAN//-->                
                <% int signType = 0; %>
                <%@ include file = "../../../templates/template_sign.jsp" %>
                <!--//END TEMPLATE TANDA TANGAN//-->
                         
        </div>
    </body>
</html>

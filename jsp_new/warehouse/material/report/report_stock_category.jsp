<%-- 
    Document   : report_stock_category
    Created on : Mar 6, 2018, 2:27:28 PM
    Author     : Dimata 007
--%>

<%@page import="com.dimata.posbo.session.warehouse.SessMatCostingStokFisik"%>
<%@page import="com.dimata.common.entity.payment.PstPriceType"%>
<%@page import="com.dimata.posbo.session.warehouse.SessReportStock"%>
<%@page import="com.dimata.posbo.entity.search.SrcReportStock"%>
<%@page import="com.dimata.posbo.entity.masterdata.*"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
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
    int start = FRMQueryString.requestInt(request, "start");
    int viewzerostock = FRMQueryString.requestInt(request, "viewzerostock");

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
    }
    catch(Exception exx) {
        System.out.println("Exc when get periode");
    }

    Location location = new Location();
    if (srcReportStock.getLocationId() != 0) {
        try {
            location = PstLocation.fetchExc(srcReportStock.getLocationId());
        } catch (Exception e) {
            System.out.println("Exc when get lokasi");
        }
    } else {
        location.setName("Semua Lokasi");
    }

    Category category = new Category();
    if (srcReportStock.getCategoryId()!= 0) {
        try {
            category = PstCategory.fetchExc(srcReportStock.getCategoryId());
        } catch (Exception exx) {
            System.out.println("Exc when get kategori");
        }
    } else {
        category.setName("Semua Kategori");
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

    Vector listStockPerKategori = sessReportStock.getReportStockPerKategori(srcReportStock, calculateQtyNull); // getReportStock
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
                <h4><b>Laporan Stok Per Kategori</b></h4>
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
                    <td>Kategori</td>
                    <td style="">: <%=category.getName()%></td>
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
                        double subQty = 0;
                        double subAverage = 0;
                        double subsaldo = 0;
                        double subBerat = 0;

                        double grandQty = 0;
                        double grandAverage = 0;
                        double grandsaldo = 0;
                        double grandHargaJual = 0;
                        double grandBerat = 0;
                        long oldCategory = 0;
                
                        for (int i = 0; i < listStockPerKategori.size(); i++) {
                            Vector vt = (Vector) listStockPerKategori.get(i);
                            Material material = (Material) vt.get(0);
                            MaterialStock materialStock = (MaterialStock) vt.get(1);
                            Unit unit = (Unit) vt.get(3);
                            Category cat = (Category) vt.get(2);
                            
                            //cek qty
                            Date dateNow = new Date();
                            double stokSisa = SessMatCostingStokFisik.qtyMaterialBasedOnStockCard(material.getOID(), srcReportStock.getLocationId(), dateNow, 0);
                            materialStock.setQty(stokSisa);
                            
                            grandQty = grandQty+materialStock.getQty();
                            grandAverage = grandAverage+material.getAveragePrice();
                            grandsaldo = grandsaldo+(material.getAveragePrice() * materialStock.getQty());  
                            grandBerat = grandBerat + materialStock.getBerat();
                            
                            Material newmat = new Material();
                            Category c = new Category();
                            Color color = new Color();
                            Ksg ksg = new Ksg();
                            try {
                                newmat = PstMaterial.fetchExc(material.getOID());
                                c = PstCategory.fetchExc(newmat.getCategoryId());
                                color = PstColor.fetchExc(newmat.getPosColor());
                                ksg = PstKsg.fetchExc(newmat.getGondolaCode());
                            } catch (Exception e) {

                            }
                            String itemName = "" + material.getName();
                            if (typeOfBusinessDetail == 2) {
                                if (newmat.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS) {
                                    itemName = "" + c.getName() + " " + color.getColorName() + " " + newmat.getName();
                                } else if (newmat.getMaterialJenisType() == Material.MATERIAL_TYPE_BERLIAN) {
                                    itemName = "" + c.getName() + " " + color.getColorName() + " Berlian " + newmat.getName();
                                }
                            }
                
                            if (srcReportStock.getCategoryId() == 0) {
                                if (oldCategory != cat.getOID()) {
                                    oldCategory = cat.getOID();
                                    
                    %>
                    
                    <%if (i != 0) {%>
                    <tr>
                        <td colspan="5" class="text-right"><b>Sub Total :</b></td>
                        <td class="text-center"><b><%=String.format("%,.0f", subQty)%></b></td>
                        <td class="text-right"><b><%=String.format("%,.3f", subBerat)%></b></td>
                        <td class="text-right"><b><%=String.format("%,.0f", subsaldo)%>.00</b></td>
                    </tr>
                    <%}%>
                    
                    <%
                        subQty = 0;
                        subAverage = 0;
                        subsaldo = 0;

                        subQty = subQty + materialStock.getQty();
                        subAverage = subAverage + material.getAveragePrice();
                        subsaldo = subsaldo + (material.getAveragePrice() * materialStock.getQty());
                        subBerat = subBerat + materialStock.getBerat();
                    %>
                    
                    <tr>
                        <td colspan="8" style="background-color: lightgray"><b><%=cat.getName()%></b></td>
                    </tr>
                    
                    <%
                                } else {
                                    subQty = subQty + materialStock.getQty();
                                    subAverage = subAverage + material.getAveragePrice();
                                    subsaldo = subsaldo + (material.getAveragePrice() * materialStock.getQty());   
                                    subBerat = subBerat + materialStock.getBerat();
                    %>                                        
                    
                    <%                                    
                                }
                                
                            } else {                                
                    %>                                        
                    
                    <%
                            }
                    %>
                    
                    <tr>
                        <td><%=(i+1)%></td>
                        <td><%=material.getSku()%></td>
                        <td><%=material.getBarCode()%></td>
                        <td><%=itemName%></td>
                        <td><%=ksg.getName()%></td>
                        <td class="text-center"><%=String.format("%,.0f", materialStock.getQty())%></td>
                        <td class="text-right"><%=String.format("%,.3f", materialStock.getBerat())%></td>
                        <td class="text-right"><%=String.format("%,.0f", (material.getAveragePrice() * materialStock.getQty()))%>.00</td>
                    </tr>
                    
                    <%
                        }
                    %>
                    
                    <%if (srcReportStock.getCategoryId() == 0) {%>
                    <tr>
                        <td colspan="5" class="text-right"><b>Sub Total :</b></td>                        
                        <td class="text-center"><b><%=String.format("%,.0f", subQty)%></b></td>
                        <td class="text-right"><b><%=String.format("%,.3f", subBerat)%></b></td>
                        <td class="text-right"><b><%=String.format("%,.0f", subsaldo)%>.00</b></td>
                    </tr>
                    <%}%>
                    
                    <tr><td colspan="8" style="background-color: lightgray">&nbsp;</td></tr>
                    <tr>
                        <td colspan="5" class="text-right"><b>Grand Total :</b></td>
                        <td class="text-center"><b><%=String.format("%,.0f", grandQty)%></b></td>
                        <td class="text-right"><b><%=String.format("%,.3f", grandBerat)%></b></td>
                        <td class="text-right"><b><%=String.format("%,.0f", grandsaldo)%>.00</b></td>
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

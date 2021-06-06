<%-- 
    Document   : print_out_return_without_receive.jsp
    Created on : Mar 1, 2018, 2:09:38 PM
    Author     : Dimata 007
--%>

<%@page import="com.dimata.posbo.entity.masterdata.PstColor"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstCategory"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterial"%>
<%@page import="com.dimata.posbo.entity.masterdata.Color"%>
<%@page import="com.dimata.posbo.entity.masterdata.Category"%>
<%@page import="com.dimata.posbo.entity.masterdata.Unit"%>
<%@page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatReturnItem"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatReturnItem"%>
<%@page import="com.dimata.common.entity.contact.PstContactList"%>
<%@page import="com.dimata.common.entity.contact.ContactList"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatReturn"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatReturn"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.posbo.entity.masterdata.Company"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstCompany"%>
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
    long oidReturnMaterial = FRMQueryString.requestLong(request, "hidden_return_id");
    MatReturn ret = new MatReturn();
    ContactList cl = new ContactList();
    Location loc = new Location();
    if (oidReturnMaterial > 0) {
        try {
            ret = PstMatReturn.fetchExc(oidReturnMaterial);
            cl = PstContactList.fetchExc(ret.getSupplierId());
            loc = PstLocation.fetchExc(ret.getLocationId());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    String whereClauseItem = PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID] + "=" + oidReturnMaterial;    
    Vector listMatReturnItem = PstMatReturnItem.list(0, 0, whereClauseItem);
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap/css/bootstrap.min.css"/>
        <style>

            table {font-size: 14px}

            .tabel_data th {text-align: center}

            .tabelHeader td {padding-bottom: 5px}

            .tabel_data {border-color: black !important}
            .tabel_data th {border-color: black !important}
            .tabel_data td {border-color: black !important}
            .tabel_data th {border-bottom-width: thin !important}

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
                <h4><b>Return Faktur</b></h4>
            </div>

            <table class="tabelHeader" style="width: 100%">
                <tr>
                    <td style="width: 10%">Nomor Return</td>
                    <td style="width: 20%">: <%=ret.getRetCode()%></td>

                    <td style="width: 10%">Supplier</td>
                    <td style="width: 25%">: <%=cl.getCompName()%></td>

                    <td style="width: 8%">Keterangan</td>
                    <td>: <%=ret.getRemark()%></td>

                </tr>
                <tr>                    
                    <td>Tanggal Return</td>
                    <td style="width: ">: <%=Formater.formatDate(ret.getReturnDate(), "dd MMMM yyyy")%></td>
                    
                    <td>Lokasi</td>
                    <td>: <%=loc.getName()%></td>
                </tr>
            </table>

            <hr style="border-width: medium; border-color: black; margin-top: 5px">

            <table class="table table-bordered tabel_data">
                <thead>
                    <tr>
                        <th style="width: 1%">No.</th>
                        <th>Sku</th>
                        <th>Nama Barang</th>
                        <th>Harga Beli / HE</th>
                        <th>Qty</th>
                        <th>Berat</th>
                        <th>Ongkos/Batu</th>
                        <th>Total</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        double totalHe = 0;
                        double totalQty = 0;
                        double berat = 0;
                        double ongoks = 0;
                        double total = 0;
                        for (int i = 0; i < listMatReturnItem.size(); i++) {
                            Vector temp = (Vector) listMatReturnItem.get(i);
                            MatReturnItem retItemx = (MatReturnItem) temp.get(0);
                            Material mat = (Material) temp.get(1);
                            Unit unit = (Unit) temp.get(2);    
                            
                            Material newmat = new Material();
                            Category category = new Category();
                            Color color = new Color();
                            try {
                                newmat = PstMaterial.fetchExc(retItemx.getMaterialId());
                                category = PstCategory.fetchExc(newmat.getCategoryId());
                                color = PstColor.fetchExc(newmat.getPosColor());
                            } catch (Exception e) {
                                System.out.print(e.getMessage());
                            }
                            
                            String itemName = "" + newmat.getName();
                            if (newmat.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS) {
                                itemName = "" + category.getName() + " " + color.getColorName() + " " + newmat.getName();
                            } else if (newmat.getMaterialJenisType() == Material.MATERIAL_TYPE_BERLIAN) {
                                itemName = "" + category.getName() + " " + color.getColorName() + " Berlian " + newmat.getName();
                            }
                            
                            totalHe += retItemx.getCost();
                            totalQty += retItemx.getQty();
                            berat += retItemx.getBerat();
                            ongoks += retItemx.getOngkos();
                            total += retItemx.getTotal();
                    %>
                    <tr>
                        <td><%=(i+1)%></td>
                        <td><%=mat.getSku()%></td>
                        <td><%=itemName%></td>
                        <td class="text-right"><%=String.format("%,.0f", retItemx.getCost())%>.00</td>
                        <td class="text-right"><%=String.format("%,.0f", retItemx.getQty())%></td>
                        <td class="text-right"><%=String.format("%,.3f", retItemx.getBerat())%></td>
                        <td class="text-right"><%=String.format("%,.0f", retItemx.getOngkos())%>.00</td>
                        <td class="text-right"><%=String.format("%,.0f", retItemx.getTotal())%>.00</td>
                    </tr>
                    <%
                        }
                    %>
                    <tr>
                        <td colspan="3" style="text-align: right"><strong>Total</strong></td>
                        <td class="text-right"><strong><%=String.format("%,.0f", totalHe)%>.00</strong></td>
                        <td class="text-right"><strong><%=String.format("%,.0f", totalQty)%></strong></td>
                        <td class="text-right"><strong><%=String.format("%,.3f", berat)%></strong></td>
                        <td class="text-right"><strong><%=String.format("%,.0f", ongoks)%>.00</strong></td>
                        <td class="text-right"><strong><%=String.format("%,.0f", total)%>.00</strong></td>
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

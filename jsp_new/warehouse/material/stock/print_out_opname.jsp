<%-- 
    Document   : print_out_opname
    Created on : Feb 26, 2018, 11:12:57 AM
    Author     : Dimata 007
--%>

<%@page import="com.dimata.posbo.entity.masterdata.PstKsg"%>
<%@page import="com.dimata.common.entity.contact.PstContactList"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatStockOpname"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatStockOpname"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstKadar"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstColor"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterial"%>
<%@page import="com.dimata.posbo.entity.masterdata.Kadar"%>
<%@page import="com.dimata.posbo.entity.masterdata.Color"%>
<%@page import="com.dimata.posbo.entity.masterdata.Ksg"%>
<%@page import="com.dimata.common.entity.contact.ContactList"%>
<%@page import="com.dimata.posbo.entity.masterdata.Merk"%>
<%@page import="com.dimata.posbo.entity.masterdata.SubCategory"%>
<%@page import="com.dimata.posbo.entity.masterdata.Category"%>
<%@page import="com.dimata.posbo.entity.masterdata.Unit"%>
<%@page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatStockOpnameItem"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstCategory"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatStockOpnameItem"%>
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

    long oidStockOpname = FRMQueryString.requestLong(request, "hidden_opname_id");
    MatStockOpname mso = new MatStockOpname();
    ContactList cl = new ContactList();
    Location l = new Location();
    Ksg k = new Ksg();
    Category c = new Category();
    
    try {
        mso = PstMatStockOpname.fetchExc(oidStockOpname);
    } catch (Exception e) {
        System.out.print(e.getMessage());
    }
    
    String supplier = "";
    try {
        cl = PstContactList.fetchExc(mso.getSupplierId());
        supplier = cl.getCompName();
    } catch (Exception e) {
        supplier = "Semua Supplier";
    }
    
    try {
        l = PstLocation.fetchExc(mso.getLocationId());
    } catch (Exception e) {
        System.out.print(e.getMessage());
    }
    
    try {
        k = PstKsg.fetchExc(mso.getEtalaseId());
    } catch (Exception e) {
        System.out.print(e.getMessage());
    }
    
    String sCat = "";
    try {
        c = PstCategory.fetchExc(mso.getCategoryId());
        sCat = c.getName();
    } catch (Exception e) {
        sCat = "Semua Kategori";
    }
    
    try {
        mso = PstMatStockOpname.fetchExc(oidStockOpname);
        cl = PstContactList.fetchExc(mso.getSupplierId());
        l = PstLocation.fetchExc(mso.getLocationId());
        k = PstKsg.fetchExc(mso.getEtalaseId());
        c = PstCategory.fetchExc(mso.getCategoryId());
    } catch (Exception e) {
        System.out.print(e.getMessage());
    }
    
    Vector listMatStockOpnameItem = PstMatStockOpnameItem.list(0, 0, oidStockOpname);
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
                <h4><b>Opname Barang</b></h4>
            </div>

            <table class="tabelHeader" style="width: 100%">
                <tr>
                    <td style="width: 10%">Nomor Opname</td>
                    <td style="width: 20%">: <%=mso.getStockOpnameNumber()%></td>

                    <td style="width: 10%">Supplier</td>
                    <td style="width: 25%">: <%=supplier%></td>

                    <td style="width: 10%">Tanggal Opname</td>
                    <td style="width: ">: <%=Formater.formatDate(mso.getStockOpnameDate(), "dd MMMM yyyy")%></td>
                </tr>
                <tr>
                    <td>Lokasi</td>
                    <td>: <%=l.getName()%></td>

                    <td>Etalase</td>
                    <td>: <%=k.getCode()%></td>

                    <td>Keterangan</td>
                    <td>: <%=mso.getRemark()%></td>
                </tr>
                <tr>
                    <td>Kategori</td>
                    <td>: <%=sCat%></td>
                </tr>
            </table>

            <hr style="border-width: medium; border-color: black; margin-top: 5px">

            <table class="table table-bordered tabel_data">
                <thead>
                    <tr>
                        <th style="width: 1%">No.</th>
                        <th>Sku</th>
                        <th>Nama Barang</th>
                        <th>Qty</th>
                        <th>Qty SO</th>
                        <th>Qty Selisih</th>
                        <th>Kadar</th>
                        <th>Kadar SO</th>
                        <th>Berat</th>
                        <th>Berat SO</th>
                        <th>Berat Selisih</th>
                        <th>Keterangan</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        double totalQty = 0;
                        double totalQtyOpname = 0;
                        double totalQtySelisih = 0;
                        double totalBerat = 0;
                        double totalBeratOpname = 0;
                        double totalBeratSelisih = 0;
                        for (int i = 0; i < listMatStockOpnameItem.size(); i++) {
                            Vector temp = (Vector)listMatStockOpnameItem.get(i);
                            MatStockOpnameItem soItem = (MatStockOpnameItem)temp.get(0);
                            Material mat = (Material)temp.get(1);
                            Unit unit = (Unit)temp.get(2);
                            Category cat = (Category)temp.get(3);
                            SubCategory scat = (SubCategory)temp.get(4);
                            
                            totalQty += soItem.getQtyItem();
                            totalQtyOpname += soItem.getQtyOpname();
                            totalQtySelisih += soItem.getQtySelisih();
                            totalBerat += soItem.getBerat();
                            totalBeratOpname += soItem.getBeratOpname();
                            totalBeratSelisih += soItem.getBeratSelisih();
                            
                            Material newmat = new Material();
                            Category category = new Category();
                            Color color = new Color();
                            Kadar kadar = new Kadar();
                            Kadar kadarOpname = new Kadar();
                            try {
                                newmat = PstMaterial.fetchExc(soItem.getMaterialId());
                                category = PstCategory.fetchExc(newmat.getCategoryId());
                                color = PstColor.fetchExc(newmat.getPosColor());
                                kadar = PstKadar.fetchExc(soItem.getKadarId());
                                kadarOpname = PstKadar.fetchExc(soItem.getKadarOpnameId());
                            } catch (Exception e) {
                                System.out.print(e.getMessage());
                            }
                            String itemName = "" + newmat.getName();
                            if (newmat.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS) {
                                itemName = "" + category.getName() + " " + color.getColorName() + " " + newmat.getName();
                            } else if (newmat.getMaterialJenisType() == Material.MATERIAL_TYPE_BERLIAN) {
                                itemName = "" + category.getName() + " " + color.getColorName() + " Berlian " + newmat.getName();
                            }
                    %>
                    <tr>
                        <td><%=(i+1)%></td>
                        <td><%=mat.getSku()%></td>
                        <td><%=itemName%></td>
                        <td class="text-right"><%=String.format("%,.0f", soItem.getQtyItem())%></td>
                        <td class="text-right"><%=String.format("%,.0f", soItem.getQtyOpname())%></td>
                        <td class="text-right"><%=String.format("%,.0f", soItem.getQtySelisih())%></td>
                        <td class="text-right"><%=String.format("%,.2f",kadar.getKadar())%></td>
                        <td class="text-right"><%=String.format("%,.2f",kadarOpname.getKadar())%></td>
                        <td class="text-right"><%=String.format("%,.3f",soItem.getBerat())%></td>
                        <td class="text-right"><%=String.format("%,.3f",soItem.getBeratOpname())%></td>
                        <td class="text-right"><%=String.format("%,.3f",soItem.getBeratSelisih())%></td>
                        <td><%=soItem.getRemark()%></td>
                    </tr>
                    <%                        
                        }
                    %>
                    
                    <tr>
                        <td class="text-right" colspan="3"><b>Total :</b></td>
                        <td class="text-right"><%=String.format("%,.0f", totalQty)%></td>
                        <td class="text-right"><%=String.format("%,.0f", totalQtyOpname)%></td>
                        <td class="text-right"><%=String.format("%,.0f", totalQtySelisih)%></td>
                        <td colspan="2"></td>
                        <td class="text-right"><%=String.format("%,.3f", totalBerat)%></td>
                        <td class="text-right"><%=String.format("%,.3f", totalBeratOpname)%></td>
                        <td class="text-right"><%=String.format("%,.3f", totalBeratSelisih)%></td>
                        <td></td>
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

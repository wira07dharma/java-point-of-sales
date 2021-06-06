<%-- 
    Document   : print_out_report_sales_return
    Created on : Apr 5, 2018, 4:51:23 PM
    Author     : Dimata 007
--%>

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.dimata.posbo.session.masterdata.SessMaterial"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstKadar"%>
<%@page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatReceiveItem"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatReceive"%>
<%@page import="com.dimata.pos.entity.billing.BillMain"%>
<%@page import="com.dimata.posbo.entity.masterdata.Company"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstCompany"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../../../main/javainit.jsp" %>
<%int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER, AppObjInfo.OBJ_TRANSFER);%>
<%@ include file = "../../../main/checkuser.jsp" %>

<%//
    Vector<Company> company = PstCompany.list(0, 0, "", "");
    String compName = "";
    if (!company.isEmpty()) {
        compName = company.get(0).getCompanyName().toUpperCase();
    }
    
    Vector listPenjualan = new Vector(1, 1);
    if(session.getValue("listPenjualan")!=null){
        listPenjualan = (Vector)session.getValue("listPenjualan"); 
    }
    
    String startDate = "";
    if (session.getValue("startDate")!=null){
        startDate = (String)session.getValue("startDate"); 
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
        startDate = new SimpleDateFormat("dd/MM/yyyy").format(date);
    }
    
    String endDate = "";
    if (session.getValue("endDate")!=null){
        endDate = (String)session.getValue("endDate"); 
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
        endDate = new SimpleDateFormat("dd/MM/yyyy").format(date);
        
    }
    
    String date = startDate;
    if (!startDate.equals(endDate)){
        date = startDate + " - "+ endDate;
    }
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap/css/bootstrap.min.css"/>
        <style>
            table {font-size: 14px}            

            .tabel_header {width: 100%}
            .tabel_header td {padding-bottom: 10px}

            .tabel_data {border-color: black !important}
            
            .tabel_data th {
                //text-align: center;
                border-color: black !important;                
                border-bottom-width: thin !important;
                padding: 4px 8px !important;
            }
            .tabel_data td {
                border-color: black !important;
                padding: 4px 8px !important
            }
            
            @media print{@page {size: landscape}}
        </style>
    </head>
    <body onload="window.print()">
        <div style="margin: 10px">
            <br>
            <div>
                <img style="width: 100px" src="../../../images/litama.jpeg">
                <span style="font-size: 24px; font-family: TimesNewRomans"><b><%=compName%></b> <small><i>Gallery</i></small></span>
            </div>
            
            <div>
                <h4><b>Laporan Retur Penjualan</b></h4>
            </div>
            
            <table class="tabel_header"  style='font-size: 11px;'>
                <tr>
                    <td style="width: 5%">Tanggal</td>
                    <td style="width: 1%">: </td>
                    <td><%=date%></td>
                </tr>
            </table>
            
            <hr style="border-width: medium; border-color: black; margin-top: 5px">
            
            <table style='font-size: 11px; border-spacing: 5px; border-collapse: collapse' width='100%'>
                <tr style="border-bottom: solid thin;">
                    <th style="padding: 2px 8px;">No. Penjualan</th>
                    <th style="padding: 2px 8px;">Tgl Penjualan</th>
                    <th style="padding: 2px 8px;">Tgl Retur</th>
                    <th style="padding: 2px 8px;">SKU</th>
                    <th style="padding: 2px 8px;">Nama Barang</th>
                    <th style="padding: 2px 8px;">No. Retur</th>
                    <th style="padding: 2px 8px;">Kadar</th>
                    <th style="padding: 2px 8px;">Berat Awal</th>
                    <th style="padding: 2px 8px;">Berat Baru</th>
                    <th style="padding: 2px 8px;">Berat Selisih</th>
                    <th style="padding: 2px 8px;">Harga / gr</th>
                    <th style="padding: 2px 8px;">Jumlah</th>
                    <th style="padding: 2px 8px;">Potongan</th>
                    <th style="padding: 2px 8px;">Total Nilai</th>
                    <th style="padding: 2px 8px;">Rate</th>
                    <th style="padding: 2px 8px;">Total (Rp)</th>
                    <th style="padding: 2px 8px;">Kode Baru</th>
                    <th style="padding: 2px 8px;">Keterangan</th>
                </tr>
                <%
                            double subBeratAwal = 0;
                            double subBeratBaru = 0;
                            double subHarga = 0;
                            double subJumlah = 0;
                            double subPotongan = 0;
                            double subTotalNilai = 0;
                            double subTotal = 0;
                            
                            double grandBeratAwal = 0;
                            double grandBeratBaru = 0;
                            double granndTotal = 0;
                            for (int i = 0; i < listPenjualan.size(); i++) {
                                Vector v = (Vector) listPenjualan.get(i);
                                BillMain bm = (BillMain) v.get(0);
                                MatReceive rc = (MatReceive) v.get(1);
                                MatReceiveItem it = (MatReceiveItem) v.get(2);
                                Material m = (Material) v.get(3);
                                Material nm = (Material) v.get(4);
                                
                                double kadarValue;
				try {
					kadarValue = PstKadar.fetchExc(m.getPosKadar()).getKadar();
				} catch (Exception ex) {
					kadarValue = 0;
				}
                                
                                double jumlah = 0;
                                if (m.getMaterialJenisType() == Material.MATERIAL_TYPE_BERLIAN){
                                    jumlah = rc.getHel();
                                } else {
                                    jumlah = rc.getHel() * it.getBerat();
                                }
                                
                                subBeratAwal += it.getBeratAwal();
                                subBeratBaru += it.getBerat();
                                subHarga += rc.getHel();
                                subJumlah += jumlah;
                                subPotongan += it.getDiscount();
                                subTotalNilai += jumlah-it.getDiscount();
                                subTotal += (jumlah-it.getDiscount())*rc.getTransRate();
                                
                                grandBeratAwal += subBeratAwal;
                                grandBeratBaru += subBeratBaru;
                                granndTotal += subTotal;
                        %>

                        <tr style="border-bottom: solid thin">
                            <td style="padding: 2px 8px;"><%=bm.getInvoiceNumber()%></td>
                            <td style="padding: 2px 8px;"><%=Formater.formatDate(bm.getBillDate(), "yyyy/MM/dd") %></td>
                            <td style="padding: 2px 8px;"><%=Formater.formatDate(rc.getReceiveDate(), "yyyy/MM/dd") %></td>
                            <td style="padding: 2px 8px;"><%=m.getSku()%></td>
                            <td style="padding: 2px 8px;"><%=SessMaterial.setItemNameForLitama(m.getOID())%></td>
                            <td style="padding: 2px 8px;"><%=rc.getRecCode()%></td>
                            <td style="padding: 2px 8px;"><%=String.format("%.2f",kadarValue)%></td>
                            <td style="padding: 2px 8px;"><%=String.format("%.3f",it.getBeratAwal())%></td>
                            <td style="padding: 2px 8px;"><%=String.format("%.3f",it.getBerat())%></td>
                            <td style="padding: 2px 8px;"><%=String.format("%.3f",(it.getBeratAwal()-it.getBerat()))%></td>
                            <td style="padding: 2px 8px;"><%=String.format("%,.2f", rc.getHel())%></td>
                            <td style="padding: 2px 8px;"><%=String.format("%,.2f", jumlah)%></td>
                            <td style="padding: 2px 8px;"><%=String.format("%,.2f", it.getDiscount())%></td>
                            <td style="padding: 2px 8px;"><%=String.format("%,.2f", (jumlah-it.getDiscount()))%></td>
                            <td style="padding: 2px 8px;"><%=String.format("%.2f",rc.getTransRate())%></td>
                            <td style="padding: 2px 8px;"><%=String.format("%,.2f", (jumlah-it.getDiscount())*rc.getTransRate())%></td>
                            <td style="padding: 2px 8px;"><%=nm.getBarCode()%></td>
                            <td style="padding: 2px 8px;"><%=rc.getRemark()%></td>
                        </tr>
                        
                        <%}%>
                        
                        <%if(listPenjualan.isEmpty()) {%>
                        <tr style="border-bottom: solid thin">
                            <td  style="padding: 2px 8px;" colspan="19" class="text-center"><b>Tidak ada data retur penjualan</b></td>
                        </tr>
                        <%}%>
                        
                        <tr style="border-bottom: solid thin">
                            <td style="padding: 2px 8px;"><b>Total :</b></td>
                            <td style="padding: 2px 8px;" colspan="6"></td>
                            <td style="padding: 2px 8px;"><strong><%=String.format("%.3f",subBeratAwal)%></strong></td>
                            <td style="padding: 2px 8px;"><strong><%=String.format("%.3f",subBeratBaru)%></strong></td>
                            <td style="padding: 2px 8px;"</td>
                            <td style="padding: 2px 8px;"><strong><%=String.format("%,.2f",subHarga)%></strong></td>
                            <td style="padding: 2px 8px;"><strong><%=String.format("%,.2f",subJumlah)%></strong></td>
                            <td style="padding: 2px 8px;"><strong><%=String.format("%,.2f",subPotongan)%></strong></td>
                            <td style="padding: 2px 8px;"><strong><%=String.format("%,.2f",subTotalNilai)%></strong></td>
                            <td style="padding: 2px 8px;"></td>
                            <td style="padding: 2px 8px;"><strong><%=String.format("%,.2f",subTotal)%></strong></td>
                            <td style="padding: 2px 8px;" colspan="2"></td>
                        </tr>
            </table>
                            <br>
                 
                <!--//START TEMPLATE TANDA TANGAN//-->                
                <% int signType = 1; %>
                <%@ include file = "../../../templates/template_sign.jsp" %>
                <!--//END TEMPLATE TANDA TANGAN//-->
                                
        </div>
    </body>
</html>

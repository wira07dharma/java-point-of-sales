<%-- 
    Document   : print_out_receive_wh_supp_material_item
    Created on : Dec 17, 2017, 2:08:12 PM
    Author     : Dimata 007
--%>

<%@page import="com.dimata.posbo.session.masterdata.SessMaterial"%>
<%@page import="com.dimata.posbo.entity.warehouse.*"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.posbo.entity.masterdata.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ include file = "../../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE);%>
<%@ include file = "../../../main/checkuser.jsp" %>

<%//    
    long oidReceiveMaterial = FRMQueryString.requestLong(request, "hidden_receive_id");
    int printType = FRMQueryString.requestInt(request, "type_print_tranfer");
    MatReceive receive = PstMatReceive.fetchExc(oidReceiveMaterial);
    Location location = PstLocation.fetchExc(receive.getLocationId());
    String whereClauseItem = PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + "=" + oidReceiveMaterial;
    //String orderClauseItem = " RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID];
    String orderClauseItem = " RIGHT(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU]+",7) ASC";
    Vector listMatReceiveItem = PstMatReceiveItem.listVectorRecItemComplete(0, 0, whereClauseItem, orderClauseItem);
    Vector<Company> company = PstCompany.list(0, 0, "", "");
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap/css/bootstrap.min.css"/>
        <style>
            .hide_border_top {border-top: none !important}            
            
            .tabel_header td {padding-bottom: 10px; font-size: 12px; vertical-align: text-top}
            
            .table_warna {border-color: black; font-size: 11px}
            .table_warna th {
                text-align: center;
                border-color: black !important;
                border-bottom-width: thin !important;
            }
            .table_warna td {
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
                        <b><%=company.get(0).getCompanyName().toUpperCase()%></b>
                        <small><i>Gallery</i></small>
                    </span>
                </div>
                <div>
                    <h4><b>Laporan Pembelian Detail</b></h4>
                </div>
                        
                <table style="width: 100%;" class="tabel_header">
                    <tr>
                        <td style="width: 12%">Nomor</td>
                        <td style="width: 1%">:</td>
                        <td style="width: 20%"><%=receive.getRecCode()%></td>
                        
                        <td style="width: 12%">Tipe Item</td>
                        <td style="width: 1%">:</td>
                        <td style="width: 20%"><%=Material.MATERIAL_TYPE_TITLE[receive.getReceiveItemType()]%></td>
                        
                        <td style="width: 8%">Keterangan</td>
                        <td style="width: 1%">:</td>
                        <td style="vertical-align: text-top" rowspan="2"><%=receive.getRemark()%></td>
                    </tr>
                    <tr>
                        <td>Tanggal</td><td>:</td><td><%=Formater.formatDate(receive.getReceiveDate(), "dd/MMM/yyyy")%></td>                        
                        <td>Tgl dicetak</td><td>:</td><td><%=Formater.formatDate(new Date(), "dd/MMM/yyyy")%></td>
                    </tr>
                </table>

                <hr style="border-width: medium; border-color: black; margin-top: 5px">

                <table class="table table-bordered table_warna">
                    <thead>
                        <tr>
                            <th>Kode</th>
                            <%if (receive.getReceiveItemType() == Material.MATERIAL_TYPE_EMAS_LANTAKAN) {%>
                            <th>Nama&nbsp;Kadar</th>
                            <%} else {%>
                            <th>Nama&nbsp;Barang</th>
                            <%}%>
                            <th>Kadar<br>(%)</th>
                            <th>Berat<br>(gr)</th>
                            <th>Qty</th>                                
                            <%if(printType != 3){%>                            
                            <% if (receive.getReceiveItemType() == Material.MATERIAL_TYPE_EMAS || receive.getReceiveItemType() == Material.MATERIAL_TYPE_EMAS_LANTAKAN) { %>
                            <th>Total HE<br>(Rp)</th>
                            <%} else if (receive.getReceiveItemType() == Material.MATERIAL_TYPE_BERLIAN) { %>
                            <th>Harga&nbsp;Beli&nbsp;(Rp)</th>
                            <%}%>                            
                            <th>Oks/Batu<br>(Rp)</th>
                            <th>Rate</th>                            
                            <% if (receive.getReceiveItemType() == Material.MATERIAL_TYPE_EMAS || receive.getReceiveItemType() == Material.MATERIAL_TYPE_EMAS_LANTAKAN) { %>
                            <th>Total HP<br>(Rp)</th>
                            <%} else if (receive.getReceiveItemType() == Material.MATERIAL_TYPE_BERLIAN) { %>
                            <th>Harga&nbsp;Pokok&nbsp;(Rp)</th>
                            <%}%>                            
                            <%}%>                            
                            <th>Lokasi</th>
                            <th>Keterangan</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            double totalBerat = 0;
                            double totalOngkos = 0;
                            double totalTotalHarga = 0;
                            double totalTotal = 0;
                            double totalQty = 0;
                            for (int i = 0; i < listMatReceiveItem.size(); i++) {
                                Vector temp = (Vector) listMatReceiveItem.get(i);
                                MatReceiveItem recItem = (MatReceiveItem) temp.get(0);
                                Material mat = (Material) temp.get(1);
                                Kadar kadar = new Kadar();
                                Ksg ksg = new Ksg();
                                try {
                                    kadar = PstKadar.fetchExc(mat.getPosKadar());
                                    ksg = PstKsg.fetchExc(mat.getGondolaCode());
                                } catch (Exception e) {
                                    System.out.println(e.getMessage());
                                }
                                double total = (recItem.getTotal() + (recItem.getForwarderCost() * recItem.getQty()));
                                String itemName = SessMaterial.setItemNameForLitama(recItem.getMaterialId());
                                
                                totalBerat += recItem.getBerat();
                                totalQty += recItem.getQty();
                                totalOngkos += recItem.getForwarderCost();
                                totalTotalHarga += recItem.getTotal();
                                totalTotal += total;
                        %>
                        <tr>
                            <td class="text-center"><%=mat.getSku()%></td>
                            <td><%=itemName%></td>
                            <td class="text-center"><%=String.format("%,.2f", kadar.getKadar())%></td>
                            <td class="text-right"><%=String.format("%,.3f", recItem.getBerat())%></td>
                            <td class="text-center"><%=String.format("%,.0f", recItem.getQty())%></td>                            
                            <%if(printType != 3){%>
                            <td class="text-right"><%=String.format("%,.0f", recItem.getTotal())%>.00</td>
                            <td class="text-right"><%=String.format("%,.0f", recItem.getForwarderCost())%>.00</td>
                            <td class="text-center">1.00</td>
                            <td class="text-right"><%=String.format("%,.0f", total)%>.00</td>
                            <%}%>
                            <td class="text-center"><%=location.getCode()%> - <%=ksg.getCode()%></td>
                            <td><%=recItem.getRemark()%></td>
                        </tr>
                        <%
                            }
                        %>
                        <tr class="sum_total">
                            <td class="text-right" colspan="2">Total :</td>
                            <td class="text-right"></td>
                            <td class="text-right"><%=String.format("%,.3f", totalBerat)%></td>
                            <td class="text-center"><%=String.format("%,.0f", totalQty)%></td>    
                            <%if(printType != 3){%>
                            <td class="text-right"><%=String.format("%,.0f", totalTotalHarga)%>.00</td>
                            <td class="text-right"><%=String.format("%,.0f", totalOngkos)%>.00</td>
                            <td class="text-right"></td>
                            <td class="text-right"><%=String.format("%,.0f", totalTotal)%>.00</td>
                            <%}%>
                            <td colspan="2"></td>
                        </tr>
                        <tr class="sum_total">
                            <td class="hide_border_top text-right" colspan="2">Total Nota :</td>
                            <td class="text-right"></td>
                            <td class="hide_border_top text-right"><%=String.format("%,.3f", receive.getBerat())%></td>
                            <td class="hide_border_top"></td>                            
                            <%if(printType != 3){%>
                            <td class="hide_border_top text-right"><%=String.format("%,.0f", receive.getTotalHe())%>.00</td>
                            <td class="hide_border_top text-right"><%=String.format("%,.0f", receive.getTotalOngkos())%>.00</td>
                            <td class="hide_border_top"></td>
                            <td class="hide_border_top text-right"><%=String.format("%,.0f", receive.getTotalNota())%>.00</td>
                            <%}%>
                            <td class="hide_border_top" colspan="2"></td>
                        </tr>
                        <tr class="sum_total">
                            <td class="hide_border_top text-right" colspan="2">Selisih :</td>
                            <td class="text-right"></td>
                            <td class="text-right"><%=String.format("%,.3f", (totalBerat - receive.getBerat()))%></td>
                            <td class="hide_border_top"></td>                            
                            <%if(printType != 3){%>
                            <td class="text-right"><%=String.format("%,.0f", (totalTotalHarga - receive.getTotalHe()))%>.00</td>
                            <td class="text-right"><%=String.format("%,.0f", (totalOngkos - receive.getTotalOngkos()))%>.00</td>
                            <td class="hide_border_top"></td>
                            <td class="text-right"><%=String.format("%,.0f", (totalTotal - receive.getTotalNota()))%>.00</td>
                            <%}%>
                            <td class="hide_border_top" colspan="2"></td>
                        </tr> 
                    </tbody>                                       
                </table>

                <!--hr style="border-bottom-width: 2px; border-bottom-style: solid"-->
                
                <!--//START TEMPLATE TANDA TANGAN//-->                
                <% int signType = 0; %>
                <%@ include file = "../../../templates/template_sign.jsp" %>
                <!--//END TEMPLATE TANDA TANGAN//-->
                
            </div>
        </div>
        <%--             
    <print-area>
        <div style="margin: 10px">
                <br>
                <div>
                    <img style="width: 100px" src="../../../images/litama.jpeg">
                    <span style="font-size: 24px; font-family: TimesNewRomans">
                        <b><%=company.get(0).getCompanyName().toUpperCase()%></b>
                        <small><i>Gallery</i></small>
                    </span>
                </div>
                <div>
                    <h4><b>Laporan Pembelian Detail</b></h4>
                </div>
                
                <table style="width: 100%;" class="tabel_header">
                    <tr>
                        <td style="width: 15%">Nomor</td>
                        <td style="width: 1%">:</td>
                        <td style="width: 20%"><%=receive.getRecCode()%></td>
                        
                        <td style="width: 15%">Tipe Item Penerimaan</td>
                        <td style="width: 1%">:</td>
                        <td style="width: 20%"><%=Material.MATERIAL_TYPE_TITLE[receive.getReceiveItemType()]%></td>
                        
                        <td style="width: 10%">Keterangan</td>
                        <td style="width: 1%">:</td>
                        <td style="vertical-align: text-top" rowspan="2"><%=receive.getRemark()%></td>
                    </tr>
                    <tr>
                        <td>Tanggal Penerimaan</td><td>:</td><td><%=Formater.formatDate(receive.getReceiveDate(), "dd MMMM yyyy")%></td>                        
                        <td>Tanggal dicetak</td><td>:</td><td><%=Formater.formatDate(new Date(), "dd/MMM/yyyy")%></td>
                    </tr>
                </table>

                <hr style="border-width: medium; border-color: black; margin-top: 5px">

                <table class="table table-bordered table_warna">
                    <thead>
                        <tr>
                            <th class="text-center">Kode</th>
                            <%if (receive.getReceiveItemType() == Material.MATERIAL_TYPE_EMAS_LANTAKAN) {%>
                            <th class="text-center">Nama&nbsp;Kadar</th>
                            <%} else {%>
                            <th class="text-center">Nama&nbsp;Barang</th>
                            <%}%>
                            <th class="text-center">Kadar</th>
                            <th class="text-center">Berat (gr)</th>
                            <th class="text-center">Qty</th>                                
                            <%if(printType != 3){%>                            
                            <% if (receive.getReceiveItemType() == Material.MATERIAL_TYPE_EMAS || receive.getReceiveItemType() == Material.MATERIAL_TYPE_EMAS_LANTAKAN) { %>
                            <th class="text-center">Total&nbsp;HE&nbsp;(Rp)</th>
                            <%} else if (receive.getReceiveItemType() == Material.MATERIAL_TYPE_BERLIAN) { %>
                            <th class="text-center">Harga&nbsp;Beli&nbsp;(Rp)</th>
                            <%}%>                            
                            <th class="text-center">Ongkos/Batu&nbsp;(Rp)</th>
                            <th class="text-center">Rate</th>                            
                            <% if (receive.getReceiveItemType() == Material.MATERIAL_TYPE_EMAS || receive.getReceiveItemType() == Material.MATERIAL_TYPE_EMAS_LANTAKAN) { %>
                            <th class="text-center">Total&nbsp;HP&nbsp;(Rp)</th>
                            <%} else if (receive.getReceiveItemType() == Material.MATERIAL_TYPE_BERLIAN) { %>
                            <th class="text-center">Harga&nbsp;Pokok&nbsp;(Rp)</th>
                            <%}%>                            
                            <%}%>                            
                            <th class="text-center">Lokasi</th>
                            <th class="text-center">Keterangan</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            totalBerat = 0;
                            totalOngkos = 0;
                            totalTotalHarga = 0;
                            totalTotal = 0;
                            totalQty = 0;
                            for (int i = 0; i < listMatReceiveItem.size(); i++) {
                                Vector temp = (Vector) listMatReceiveItem.get(i);
                                MatReceiveItem recItem = (MatReceiveItem) temp.get(0);
                                Material mat = (Material) temp.get(1);
                                Kadar kadar = new Kadar();
                                Ksg ksg = new Ksg();
                                Category category = new Category();
                                Color color = new Color();
                                try {
                                    kadar = PstKadar.fetchExc(mat.getPosKadar());
                                    ksg = PstKsg.fetchExc(mat.getGondolaCode());
                                    category = PstCategory.fetchExc(mat.getCategoryId());
                                    color = PstColor.fetchExc(mat.getPosColor());
                                } catch (Exception e) {

                                }
                                double total = (recItem.getTotal() + (recItem.getForwarderCost() * recItem.getQty()));
                                String itemName = "" + mat.getName();
                                if (mat.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS) {
                                    itemName = "" + category.getName() + " " + color.getColorName() + " " + mat.getName();
                                } else if (mat.getMaterialJenisType() == Material.MATERIAL_TYPE_BERLIAN) {
                                    itemName = "" + category.getName() + " " + color.getColorName() + " Berlian " + mat.getName();
                                }
                                totalBerat += recItem.getBerat();
                                totalQty += recItem.getQty();
                                totalOngkos += recItem.getForwarderCost();
                                totalTotalHarga += recItem.getTotal();
                                totalTotal += total;
                        %>
                        <tr>
                            <td><%=mat.getSku()%></td>
                            <td><%=itemName%></td>
                            <td class="text-right"><%=String.format("%,.2f", kadar.getKadar())%></td>
                            <td class="text-right"><%=String.format("%,.3f", recItem.getBerat())%></td>
                            <td class="text-right"><%=Formater.formatNumber(recItem.getQty(), "#,###.##")%></td>                            
                            <%if(printType != 3){%>
                            <td class="text-right"><%=String.format("%,.0f", recItem.getTotal())%>.00</td>
                            <td class="text-right"><%=String.format("%,.0f", recItem.getForwarderCost())%>.00</td>
                            <td class="text-right">1.00</td>
                            <td class="text-right"><%=String.format("%,.0f", total)%>.00</td>
                            <%}%>
                            <td><%=location.getCode()%> - <%=ksg.getCode()%></td>
                            <td><%=recItem.getRemark()%></td>
                        </tr>
                        <%
                            }
                        %>
                        <tr>
                            <th class="text-right" colspan="2">Total :</th>
                            <th class="text-right"></th>
                            <th class="text-right"><%=String.format("%,.3f", totalBerat)%></th>
                            <th class="text-right"><%=Formater.formatNumber(totalQty, "#,###.##")%></th>    
                            <%if(printType != 3){%>
                            <th class="text-right"><%=String.format("%,.0f", totalTotalHarga)%>.00</th>
                            <th class="text-right"><%=String.format("%,.0f", totalOngkos)%>.00</th>
                            <th class="text-right"></th>
                            <th class="text-right"><%=String.format("%,.0f", totalTotal)%>.00</th>
                            <%}%>
                            <th colspan="2"></th>
                        </tr>
                        <tr>
                            <th class="hide_border_top text-right" colspan="2">Total Nota :</th>
                            <th class="text-right"></th>
                            <th class="hide_border_top text-right"><%=String.format("%,.3f", receive.getBerat())%></th>
                            <th class="hide_border_top"></th>                            
                            <%if(printType != 3){%>
                            <th class="hide_border_top text-right"><%=String.format("%,.0f", receive.getTotalHe())%>.00</th>
                            <th class="hide_border_top text-right"><%=String.format("%,.0f", receive.getTotalOngkos())%>.00</th>
                            <th class="hide_border_top"></th>
                            <th class="hide_border_top text-right"><%=String.format("%,.0f", receive.getTotalNota())%>.00</th>
                            <%}%>
                            <th class="hide_border_top" colspan="2"></th>
                        </tr>
                        <tr>
                            <th class="hide_border_top text-right" colspan="2">Selisih :</th>
                            <th class="text-right"></th>
                            <th class="text-right"><%=String.format("%,.3f", (totalBerat - receive.getBerat()))%></th>
                            <th class="hide_border_top"></th>                            
                            <%if(printType != 3){%>
                            <th class="text-right"><%=String.format("%,.0f", (totalTotalHarga - receive.getTotalHe()))%>.00</th>
                            <th class="text-right"><%=String.format("%,.0f", (totalOngkos - receive.getTotalOngkos()))%>.00</th>
                            <th class="hide_border_top"></th>
                            <th class="text-right"><%=String.format("%,.0f", (totalTotal - receive.getTotalNota()))%>.00</th>
                            <%}%>
                            <th class="hide_border_top" colspan="2"></th>
                        </tr>
                    </tbody>              
                </table>

                <!--hr style="border-bottom-width: 2px; border-bottom-style: solid"-->

                <div class="">     
                    <p><b>Denpasar, <%=Formater.formatDate(new Date(), "dd MMMM yyyy")%></b></p>
                    <table style="width: 100%">
                        <tr>
                            <th><%=keterangan1%></th>
                            <th></th>
                            <th></th>
                            <th class="text-center"><%=keterangan2%><%=keterangan3%></th>
                            <th></th>
                            <th></th>
                            <th><%=keterangan4%></th>
                        </tr>
                        <tr>
                            <th style="width: 15%"><%=jabatan1%></th>
                            <th style="width: 10%"></th>
                            <th style="width: 15%"><%=jabatan2%></th>
                            <th style="width: 15%"></th>
                            <th style="width: 15%"><%=jabatan3%></th>
                            <th style="width: 10%"></th>
                            <th style="width: 15%"><%=jabatan4%></th>
                        </tr>
                        <tr>
                            <td><br><br><br><br></td>
                            <td></td>
                            <td><br><br><br><br></td>
                            <td></td>
                            <td><br><br><br><br></td>
                            <td></td>
                            <td><br><br><br><br></td>
                        </tr>
                        <tr>
                            <th><%=nama1%></th>
                            <th></th>
                            <th><%=nama2%></th>
                            <th></th>
                            <th><%=nama3%></th>
                            <th></th>
                            <th><%=nama4%></th>
                        </tr>
                        <tr>
                            <th><hr style="margin: 0px; border-bottom-width: 2px; border-style: solid"></th>
                            <th></th>
                            <th><hr style="margin: 0px; border-bottom-width: 2px; border-style: solid"></th>
                            <th></th>
                            <th><hr style="margin: 0px; border-bottom-width: 2px; border-style: solid"></th>
                            <th></th>
                            <th><hr style="margin: 0px; border-bottom-width: 2px; border-style: solid"></th>
                        </tr>
                        <tr>
                            <th>NIK : <%=nik1%></th>
                            <th></th>
                            <th>NIK : <%=nik2%></th>
                            <th></th>
                            <th>NIK : <%=nik3%></th>
                            <th></th>
                            <th></th>
                        </tr>
                    </table>
                </div>

            </div>
    </print-area>
    --%>
    </body>
</html>

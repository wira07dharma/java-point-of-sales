<%-- 
    Document   : print_out_receive_back_material
    Created on : Oct 10, 2019, 11:40:02 AM
    Author     : Dimata 007
--%>


<%@page import="com.dimata.pos.entity.billing.PstBillMain"%>
<%@page import="com.dimata.pos.entity.billing.BillMain"%>
<%@page import="com.dimata.posbo.entity.masterdata.Unit"%>
<%@page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatReceiveItem"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatReceiveItem"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatReceive"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatReceive"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE);%>
<%@ include file = "../../../main/checkuser.jsp" %>

<%!//variabel
    int KETERANGAN = 0;
    int JABATAN = 1;
    int NAMA = 2;
    int KODE = 3;

    String checkSignValue(String arrayValue[], int typeValue) {
        String value = "";
        try {
            value = arrayValue[typeValue];
        } catch (Exception e) {
            value = "";
        }
        return value;
    }
%>

<%//
    long oidReceiveMaterial = FRMQueryString.requestLong(request, "hidden_receive_id");
    int printType = FRMQueryString.requestInt(request, "type_print_tranfer");

    MatReceive receive = new MatReceive();
    Location location = new Location();
    if (oidReceiveMaterial != 0) {
        receive = PstMatReceive.fetchExc(oidReceiveMaterial);
        location = PstLocation.fetchExc(receive.getLocationId());
    }

    String whereClauseItem = PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + " = " + oidReceiveMaterial;
    String orderClauseItem = "";
    Vector listMatReceiveItem = PstMatReceiveItem.listVectorRecItemComplete(0, 0, whereClauseItem, orderClauseItem);

    String syspropSign1 = PstSystemProperty.getValueByName("SIGN_RECEIVE_1");
    String syspropSign2 = PstSystemProperty.getValueByName("SIGN_RECEIVE_2");
    String syspropSign3 = PstSystemProperty.getValueByName("SIGN_RECEIVE_3");

    String arraySign1[] = syspropSign1.split(",");
    String arraySign2[] = syspropSign2.split(",");
    String arraySign3[] = syspropSign3.split(",");
    

%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Receive Back</title>
        <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap/css/bootstrap.min.css"/>
        <style>
            .hide_border_top {border-top: none !important}            

            .tabel_header td {
                padding-bottom: 10px;
                font-size: 14px;
                vertical-align: text-top
            }

            .tabel_data {
                border-color: black;
                font-size: 12px
            }
            .tabel_data th {
                text-align: center;
                border-color: black !important;
                border-bottom-width: thin !important;
                white-space: nowrap;
            }
            .tabel_data td {
                border-color: black !important;
            }

            .tabel_sign {
                width: 100%;
            }

            .tabel_sign td {
                text-align: center;
                font-size: 14px;
            }

        </style>
    </head>
    <body>
        <div class="main-page">
            <div style="margin: 10px">
                <br>
                <div class="text-center">
                    <img style="width: 100px" alt="Gambar" src="">
                </div>
                <div>
                    <h4 class="text-center"><b>PEMASUKAN BARANG KEMBALI DARI RUANG PENYERAHAN</b></h4>
                </div>
                <br>
                <table style="width: 100%;" class="tabel_header">
                    <tr>
                        <td style="">Nomor</td>
                        <td style="">:</td>
                        <td style=""><%=receive.getRecCode()%></td>

                        <td style="">Jenis Dokumen</td>
                        <td style="">:</td>
                        <td style=""><%=receive.getJenisDokumen()%></td>
                    </tr>
                    <tr>
                        <td>Lokasi</td>
                        <td>:</td>
                        <td><%=location.getName()%></td>

                        <td>Nomor BC</td>
                        <td>:</td>
                        <td><%=receive.getNomorBc()%></td>
                    </tr>
                    <tr>
                        <td>Tanggal</td>
                        <td>:</td>
                        <td><%=Formater.formatDate(receive.getReceiveDate(), "dd MMMM yyyy")%></td>
                        <td>Tanggal BC</td>
                        <td>:</td>
                        <td><%=Formater.formatDate(receive.getTglBc(), "dd MMMM yyyy")%></td>
                    </tr>
                </table>

                <hr style="border-width: medium; border-color: black; margin-top: 5px">

                <table class="table table-bordered tabel_data">
                    <thead>
                        <tr>
                            <th style="width: 1%">No</th>
                            <th>Invoice</th>
                            <th>SKU</th>
                            <th>Barcode</th>
                            <th>Nama</th>
                            <th>Unit</th>
                            <th>Harga Beli</th>
                            <th>Qty</th>
                            <th>Total Beli</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%//
                            double totalCost = 0;
                            double totalQty = 0;
                            double totalAll = 0;
                            for (int i = 0; i < listMatReceiveItem.size(); i++) {
                                Vector temp = (Vector) listMatReceiveItem.get(i);
                                MatReceiveItem recItem = (MatReceiveItem) temp.get(0);
                                Material mat = (Material) temp.get(1);
                                Unit unit = (Unit) temp.get(2);
                                BillMain bm = new BillMain();

                                try {
                                    if (recItem.getOID() != 0) {
                                        recItem = PstMatReceiveItem.fetchExc(recItem.getOID());
                                    }

                                    if (recItem.getCashBillMainId() != 0) {
                                        bm = PstBillMain.fetchExc(recItem.getCashBillMainId());
                                    }
                                } catch (Exception e) {

                                }
                                totalCost += recItem.getCost();
                                totalQty += recItem.getQty();
                                totalAll += recItem.getTotal();
                        %>
                        <tr>
                            <td><%=(i + 1)%>.</td>
                            <td><%=bm.getInvoiceNumber()%></td>
                            <td><%=mat.getSku()%></td>
                            <td><%=mat.getBarCode()%></td>
                            <td><%=mat.getName()%></td>
                            <td class="text-center"><%=unit.getCode()%></td>
                            <td class="text-right"><%=String.format("%,.0f", recItem.getCost())%></td>
                            <td class="text-center"><%=String.format("%,.0f", recItem.getQty())%></td>
                            <td class="text-right"><%=String.format("%,.0f", recItem.getTotal())%></td>
                        </tr>
                        <%
                            }
                        %>

                        <% if (listMatReceiveItem.isEmpty()) {%>
                        <tr>
                            <td colspan="9" class="text-center">Tidak ada data</td>
                        </tr>
                        <% } else {%>
                        <tr style="font-weight: bold">
                            <td class="text-left text-bold" colspan="6">TOTAL</td>
                            <td class="text-right text-bold"><%=String.format("%,.0f", totalCost)%></td>
                            <td class="text-center text-bold"><%=String.format("%,.0f", totalQty)%></td>
                            <td class="text-right text-bold"><%=String.format("%,.0f", totalAll)%></td>
                        </tr>
                        <% }%>
                    </tbody>
                </table>

                <p><%=Formater.formatDate(new java.util.Date(), "dd MMMM yyyy")%></p>

                <br>

                <table class="tabel_sign">
                    <tr>
                        <td style="width: 20%"><%=checkSignValue(arraySign1, KETERANGAN)%></td>
                        <td style="width: 20%"></td>
                        <td style="width: 20%"><%=checkSignValue(arraySign2, KETERANGAN)%></td>
                        <td style="width: 20%"></td>
                        <td style="width: 20%"><%=checkSignValue(arraySign3, KETERANGAN)%></td>
                    </tr>
                    <tr>
                        <td><p></p></td>
                        <td><p></p></td>
                        <td><p></p></td>
                        <td><p></p></td>
                        <td><p></p></td>
                    </tr>
                    <tr>
                        <td><%=checkSignValue(arraySign1, JABATAN)%></td>
                        <td></td>
                        <td><%=checkSignValue(arraySign2, JABATAN)%></td>
                        <td></td>
                        <td><%=checkSignValue(arraySign3, JABATAN)%></td>
                    </tr>
                    <tr>
                        <td style="padding: 40px 0px"></td>
                    </tr>
                    <tr>
                        <td><%=checkSignValue(arraySign1, NAMA)%></td>
                        <td></td>
                        <td><%=checkSignValue(arraySign2, NAMA)%></td>
                        <td></td>
                        <td><%=checkSignValue(arraySign3, NAMA)%></td>
                    </tr>
                    <tr>
                        <td><hr style="margin: 5px; border-bottom-width: 2px; border-style: solid"></td>
                        <td></td>
                        <td><hr style="margin: 5px; border-bottom-width: 2px; border-style: solid"></td>
                        <td></td>
                        <td><hr style="margin: 5px; border-bottom-width: 2px; border-style: solid"></td>
                    </tr>
                    <tr>
                        <td><%=checkSignValue(arraySign1, KODE)%></td>
                        <td></td>
                        <td><%=checkSignValue(arraySign2, KODE)%></td>
                        <td></td>
                        <td><%=checkSignValue(arraySign3, KODE)%></td>
                    </tr>
                </table>
            </div>
        </div>
    </body>
</html>



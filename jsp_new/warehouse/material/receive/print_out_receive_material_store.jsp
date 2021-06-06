<%-- 
    Document   : print_out_receive_material_store
    Created on : Jul 12, 2018, 10:49:08 AM
    Author     : Dimata 007
--%>

<%@page import="com.dimata.posbo.session.masterdata.*"%>
<%@page import="com.dimata.util.*"%>
<%@page import="com.dimata.qdep.form.*"%>
<%@page import="com.dimata.qdep.entity.*"%>
<%@page import="com.dimata.common.entity.payment.*"%>
<%@page import="com.dimata.posbo.entity.masterdata.*"%>
<%@page import="com.dimata.gui.jsp.*"%>
<%@page import="com.dimata.posbo.form.warehouse.*"%>
<%@page import="com.dimata.posbo.entity.warehouse.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../../../main/javainit.jsp" %>
<%//
    int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_LOCATION_RECEIVE, AppObjInfo.OBJ_LOCATION_RECEIVE);
    int appObjCodeShowQtyAndPrice = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE_QTY_AND_PRICE);
%>
<%@ include file = "../../../main/checkuser.jsp" %>

<%!
    /* this constant used to list text of listHeader */
    public static final String textListOrderHeader[][]
            = {
                {"No", "Lokasi", "Tanggal", "Asal Pengiriman", "Status", "Keterangan", "Nomor Pengiriman"},
                {"No", "Location", "Date", "Supplier", "Status", "Remark", "Dispatch Number"}
            };


    /* this constant used to list text of listMaterialItem */
    public static final String textListOrderItem[][]
            = {
                {"No", "Sku", "Nama", "Kadaluarsa", "Unit", "Harga Beli", "Mata Uang", "Jumlah", "Sub Total"},
                {"No", "Code", "Name", "Expired Date", "Unit", "Cost", "Currency", "Quantity", "Sub Total"}
            };

    /**
     * this method used to maintain poMaterialList
     */
    public String drawListRetItem(int language, int iCommand, FrmMatReceiveItem frmObject, MatReceiveItem objEntity, Vector objectClass, long recItemId, int start) {
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.addHeader(textListOrderItem[language][0], "5%");
        ctrlist.addHeader(textListOrderItem[language][1], "10%");
        ctrlist.addHeader(textListOrderItem[language][2], "20%");
        ctrlist.addHeader(textListOrderItem[language][3], "15%");
        ctrlist.addHeader(textListOrderItem[language][4], "5%");
        ctrlist.addHeader(textListOrderItem[language][5], "10%");
        ctrlist.addHeader(textListOrderItem[language][6], "5%");
        ctrlist.addHeader(textListOrderItem[language][7], "5%");
        ctrlist.addHeader(textListOrderItem[language][8], "15%");

        Vector lstData = ctrlist.getData();
        Vector rowx = new Vector(1, 1);
        ctrlist.reset();
        ctrlist.setLinkRow(1);
        int index = -1;
        if (start < 0) {
            start = 0;
        }

        for (int i = 0; i < objectClass.size(); i++) {
            Vector temp = (Vector) objectClass.get(i);
            MatReceiveItem recItem = (MatReceiveItem) temp.get(0);
            Material mat = (Material) temp.get(1);
            Unit unit = (Unit) temp.get(2);
            CurrencyType currencyType = (CurrencyType) temp.get(3);
            rowx = new Vector();
            start = start + 1;

            rowx.add("<div align=\"center\">" + start + "</div>");
            rowx.add(mat.getSku());
            rowx.add(mat.getName());
            rowx.add("<div align=\"center\">" + Formater.formatDate(recItem.getExpiredDate(), "dd-MM-yyyy") + "</div>");
            rowx.add("<div align=\"center\">" + unit.getCode() + "</div>");
            rowx.add("<div align=\"right\">" + Formater.formatNumber(recItem.getCost(), "##,###.00") + "</div>");
            rowx.add("<div align=\"center\">" + currencyType.getCode() + "</div>");
            rowx.add("<div align=\"center\">" + String.valueOf(recItem.getQty()) + "</div>");
            rowx.add("<div align=\"right\">" + Formater.formatNumber(recItem.getTotal(), "##,###.00") + "</div>");
            lstData.add(rowx);
        }
        return ctrlist.draw();
    }

%>

<%    /**
     * get approval status for create document
     */
    I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();
    I_Approval i_approval = (I_Approval) Class.forName(approvalClassName).newInstance();
    I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();
    int docType = i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL, I_DocType.MAT_DOC_TYPE_LMRR);
%>

<%
    int iCommand = FRMQueryString.requestCommand(request);
    int startItem = FRMQueryString.requestInt(request, "start_item");
    int prevCommand = FRMQueryString.requestInt(request, "prev_command");
    int appCommand = FRMQueryString.requestInt(request, "approval_command");
    long oidReceiveMaterial = FRMQueryString.requestLong(request, "hidden_receive_id");
	int typePrint = FRMQueryString.requestInt(request, "type_print_receive");

    int iErrCode = FRMMessage.NONE;
    String msgString = "";

    CtrlMatReceive ctrlMatReceive = new CtrlMatReceive(request);
    iErrCode = ctrlMatReceive.action(Command.EDIT, oidReceiveMaterial);
    MatReceive rec = ctrlMatReceive.getMatReceive();

    String whereClauseItem = PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + " = " + oidReceiveMaterial;
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

            .tabel_header td {padding-bottom: 10px; font-size: 12px}

            .table_data {border-color: black; font-size: 11px}
            .table_data th {
                text-align: center;
                border-color: black !important;
                border-bottom-width: thin !important;
            }
            .table_data td {
                border-color: black !important;
            }

            .sum_total td {font-weight: bold}
        </style>
    </head>
    <body>
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
                <h4><b>Laporan Penerimaan Toko / Gudang</b></h4>
            </div>
                    
            <table style="width: 100%;" class="tabel_header">
                <tr>
                    <td style="width: 12%">Nomor</td>
                    <td style="width: 1%">:</td>
                    <td style="width: 20%"><%=rec.getRecCode()%></td>

                    <td style="width: 12%">Tipe Item Penerimaan</td>
                    <td style="width: 1%">:</td>
                    <td style="width: 20%"><%=Material.MATERIAL_TYPE_TITLE[rec.getReceiveItemType()]%></td>

                    <td style="width: 8%">Keterangan</td>
                    <td style="width: 1%">:</td>
                    <td style="vertical-align: text-top" rowspan="2"><%=rec.getRemark()%></td>
                </tr>
                <tr>
                    <td>Tanggal Penerimaan</td><td>:</td><td><%=Formater.formatDate(rec.getReceiveDate(), "dd MMMM yyyy")%></td>                        
                    <td>Tanggal dicetak</td><td>:</td><td><%=Formater.formatDate(new Date(), "dd MMMM yyyy")%></td>
                </tr>
            </table>
                
            <hr style="border-width: medium; border-color: black; margin-top: 5px">
            
            <table class="table table-bordered table_data">
                <tr>
                    <th style="width: 1%">No.</th>
                    <th>SKU</th>
                    <th>Nama Barang</th>
                    <th>Qty</th>
                    <th>Berat</th>
					<% if (typePrint != 2){ %>
                    <th>Ongkos/Batu</th>
                    <%if(rec.getReceiveItemType() == Material.MATERIAL_TYPE_EMAS || rec.getReceiveItemType() == Material.MATERIAL_TYPE_EMAS_LANTAKAN) {%>
                    <th>Harga Emas</th>
                    <%}else{%>
                    <th>Harga Beli</th>
                    <%}%>
                    <th>Total HP</th>
					<% } %>
                </tr>

                <%
                    double totalQty = 0;
                    double totalBerat = 0;
                    double totalOngkos = 0;
                    double totalHarga = 0;
                    double grandTotal = 0;
                    
                    for (int i = 0; i < listMatReceiveItem.size(); i++) {
                        Vector temp = (Vector)listMatReceiveItem.get(i);
                        MatReceiveItem receiveItem = (MatReceiveItem)temp.get(0);
                        Material mat = (Material)temp.get(1);
                        Unit unit = (Unit)temp.get(2);
                        CurrencyType currencyType = (CurrencyType)temp.get(3);
                        String itemName = SessMaterial.setItemNameForLitama(receiveItem.getMaterialId());
                        
                        totalQty += receiveItem.getQty();
                        totalBerat += receiveItem.getBerat();
                        totalOngkos += receiveItem.getForwarderCost();
                        totalHarga += receiveItem.getCost();
                        grandTotal += receiveItem.getTotal();
                %>
                
                <tr>
                    <td class="text-center"><%=(i+1)%></td>
                    <td><%=mat.getSku() %></td>
                    <td><%=itemName %></td>
                    <td class="text-center"><%=String.format("%,.0f", receiveItem.getQty()) %></td>
                    <td class="text-center"><%=String.format("%,.3f", receiveItem.getBerat()) %></td>
					<% if (typePrint != 2){ %>
                    <td class="text-right"><%=String.format("%,.0f", receiveItem.getForwarderCost()) %>.00</td>
                    <td class="text-right"><%=String.format("%,.0f", receiveItem.getCost()) %>.00</td>
                    <td class="text-right"><%=String.format("%,.0f", receiveItem.getTotal()) %>.00</td>
					<% } %>
                </tr>
                
                <%
                    }
                %>

                <tr class="sum_total">
                    <td class="text-right" colspan="3">Total :</td>
                    <td class="text-center"><%=String.format("%,.0f", totalQty)%></td>
                    <td class="text-center"><%=String.format("%,.3f", totalBerat)%></td>
					<% if (typePrint != 2){ %>
                    <td class="text-right"><%=String.format("%,.0f", totalOngkos)%>.00</td>
                    <td class="text-right"><%=String.format("%,.0f", totalHarga)%>.00</td>
                    <td class="text-right"><%=String.format("%,.0f", grandTotal)%>.00</td>
					<% } %>
                </tr>
                
            </table>
            
                <!--//START TEMPLATE TANDA TANGAN//-->                
                <% int signType = 0; %>
                <%@ include file = "../../../templates/template_sign.jsp" %>
                <!--//END TEMPLATE TANDA TANGAN//-->
                
        </div>
    </body>
</html>

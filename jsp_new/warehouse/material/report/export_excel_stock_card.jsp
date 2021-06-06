<%-- 
    Document   : export_excel_stock_card
    Created on : Mar 4, 2019, 10:31:03 AM
    Author     : Dimata 007
--%>

<%@page import="com.dimata.pos.entity.billing.BillMain"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatDispatch"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.dimata.posbo.session.masterdata.SessMaterial"%>
<%@ page language = "java" %>
<%@ page import="com.dimata.posbo.session.warehouse.SessStockCard,
         com.dimata.common.entity.location.PstLocation,
         com.dimata.common.entity.location.Location,
         com.dimata.posbo.entity.warehouse.StockCardReport,
         com.dimata.gui.jsp.ControlList,
         com.dimata.qdep.form.FRMQueryString,
         com.dimata.gui.jsp.ControlLine,
         com.dimata.util.Command,
         com.dimata.pos.entity.billing.PstBillMain,
         com.dimata.posbo.form.search.FrmSrcStockCard,
         com.dimata.posbo.entity.search.SrcStockCard,
         com.dimata.posbo.entity.masterdata.Material,
         com.dimata.posbo.entity.masterdata.PstMaterial,
         com.dimata.posbo.entity.masterdata.Unit,
         com.dimata.posbo.entity.masterdata.PstUnit"%>
<%@ page import="com.dimata.posbo.entity.warehouse.PstMaterialStockCode"%>
<%@ page import="com.dimata.posbo.entity.warehouse.MaterialStockCode"%>
<%@ page import="com.dimata.qdep.form.FRMHandler"%>

<%@ include file = "../../../main/javainit.jsp" %>
<%//
    int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK_REPORT, AppObjInfo.OBJ_STOCK_CARD);
    int appObjCodeRec = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE);
    boolean privApprovalFinal = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeRec, AppObjInfo.COMMAND_FINAL));
%>
<%@ include file = "../../../main/checkuser.jsp" %>

<%!//
    public static final String textListGlobal[][] = {
        {"Tidak ada data", "Kartu Stok", "Periode", "Lokasi", "Kode/Nama Barang", "Periode", " s/d ", "Cetak Kartu Stok", "Stok awal", "Satuan", "Status Dokumen","Riwayat Perpindahan"},
        {"No available data", "Stock Card", "Period", "Location", "Code/Goods Name", "Period", " to ", "Print Stock Card", "Beginning stock", "Unit", "Document Status","Transfer History"}
    };

    /* this constant used to list text of listHeader */
    public static final String textListMaterialHeader[][] = {
        {"Tanggal", "Nomor Dokumen", "Keterangan", "Mutasi", "Stok Masuk", "Stok Keluar", "Saldo"},
        {"Date", "Number", "Remark", "Mutation", "Stock In", "Stock Out", "Saldo"}
    };

    public String drawList2(int language, Vector listAll, int typeOfBusinessDetail, int showTransferHistory) {
        String result = "";
        if (listAll != null && listAll.size() > 0) {
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("100%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("listgentitle");

            ctrlist.addHeader(textListMaterialHeader[language][0], "");
            ctrlist.addHeader(textListMaterialHeader[language][2], "");
            ctrlist.addHeader(textListMaterialHeader[language][1], "");
            ctrlist.addHeader(textListMaterialHeader[language][4], "");
            ctrlist.addHeader(textListMaterialHeader[language][5], "");
            ctrlist.addHeader(textListMaterialHeader[language][6], "");

            ctrlist.setLinkRow(1);
            ctrlist.setLinkSufix("");
            Vector lstData = ctrlist.getData();

            double qtyawal = 0;
            StockCardReport stockCrp = (StockCardReport) listAll.get(0);
            Vector rowx = new Vector(1, 1);
            rowx.add("<div align=\"left\">" + Formater.formatDate(stockCrp.getDate(), "dd/MM/yyyy") + "</div>");
            rowx.add(textListGlobal[language][8]);
            rowx.add("");
            rowx.add("<div align=\"center\">&nbsp;</div>");
            rowx.add("<div align=\"center\">&nbsp;</div>");
            qtyawal = stockCrp.getQty();
            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(stockCrp.getQty()) + "</div>");
            lstData.add(rowx);

            Vector objectClass = (Vector) listAll.get(1);
            if (objectClass != null && objectClass.size() > 0) {
                for (int i = 0; i < objectClass.size(); i++) {
                    StockCardReport stockCardReport = (StockCardReport) objectClass.get(i);
                    rowx = new Vector();
                    rowx.add("<div align=\"left\">" + Formater.formatDate(stockCardReport.getDate(), "dd/MM/yyyy") + "</div>");
                    rowx.add(stockCardReport.getKeterangan());
                    
                    switch (stockCardReport.getDocType()) {
                        case I_DocType.MAT_DOC_TYPE_LMRR:
                            switch (stockCardReport.getTransaction_type()) {
                                case I_DocType.MAT_RECEIVE_SOURCE_PENERIMAAN_WITH_PO:
                                    rowx.add("<div href=\"javascript:cmdViewKartuStock('" + I_DocType.MAT_DOC_TYPE_LMRR + "','" + stockCardReport.getDocCode() + "','" + stockCardReport.getTransaction_type() + "')\">" + stockCardReport.getDocCode() + "</div>"); //penerimaan  
                                    break;
                                case I_DocType.MAT_RECEIVE_SOURCE_PENERIMAAN_TRANSFER_TOKO:
                                    if (showTransferHistory == 0) {
                                        continue;
                                    }
                                    rowx.add("<div href=\"javascript:cmdViewKartuStock('" + I_DocType.MAT_DOC_TYPE_LMRR + "','" + stockCardReport.getDocCode() + "','" + stockCardReport.getTransaction_type() + "')\">" + stockCardReport.getDocCode() + "</div>"); //penerimaan  
                                    break;
                                case I_DocType.MAT_RECEIVE_SOURCE_PENERIMAAN_TRANSFER_UNIT:
                                    rowx.add("<div href=\"javascript:cmdViewKartuStock('" + I_DocType.MAT_DOC_TYPE_LMRR + "','" + stockCardReport.getDocCode() + "','" + stockCardReport.getTransaction_type() + "')\">" + stockCardReport.getDocCode() + "</div>"); //penerimaan  
                                    break;
                                default:
                                    rowx.add("<div href=\"javascript:cmdViewKartuStock('" + I_DocType.MAT_DOC_TYPE_LMRR + "','" + stockCardReport.getDocCode() + "','" + stockCardReport.getTransaction_type() + "')\">" + stockCardReport.getDocCode() + "</div>"); //penerimaan  
                                    break;
                            }
                            if (typeOfBusinessDetail == 2) {
                                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(stockCardReport.getBerat()) + "</div>");
                                rowx.add("<div align=\"center\">&nbsp;</div>");
                                qtyawal = qtyawal + stockCardReport.getBerat();
                            } else {
                                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(stockCardReport.getQty()) + "</div>");
                                rowx.add("<div align=\"center\">&nbsp;</div>");
                                qtyawal = qtyawal + stockCardReport.getQty();
                            }
                            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(qtyawal) + "</div>");
                            break;
                        case I_DocType.MAT_DOC_TYPE_ROMR:
                            rowx.add("<div href=\"javascript:cmdViewKartuStock('" + I_DocType.MAT_DOC_TYPE_ROMR + "','" + stockCardReport.getDocCode() + "')\">" + stockCardReport.getDocCode() + "</div>"); //penerimaan
                            if (typeOfBusinessDetail == 2) {
                                rowx.add("<div align=\"center\">&nbsp;</div>");
                                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(stockCardReport.getBerat()) + "</div>");
                                qtyawal = qtyawal - stockCardReport.getBerat();
                            } else {
                                rowx.add("<div align=\"center\">&nbsp;</div>");
                                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(stockCardReport.getQty()) + "</div>");
                                qtyawal = qtyawal - stockCardReport.getQty();
                            }
                            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(qtyawal) + "</div>");
                            break;
                        case I_DocType.MAT_DOC_TYPE_DF:
                            switch (stockCardReport.getTransaction_type()) {
                                case PstMatDispatch.FLD_TYPE_DISPATCH_LOCATION_STORE:
                                case PstMatDispatch.FLD_TYPE_DISPATCH_LOCATION_WAREHOUSE:
                                    if (showTransferHistory == 0) {
                                        continue;
                                    }
                                    break;
                            }
                            rowx.add("<div href=\"javascript:cmdViewKartuStock('" + I_DocType.MAT_DOC_TYPE_DF + "','" + stockCardReport.getDocCode() + "')\">" + stockCardReport.getDocCode() + "</div>"); //penerimaan
                            if (typeOfBusinessDetail == 2) {
                                rowx.add("<div align=\"center\">&nbsp;</div>");
                                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(stockCardReport.getBerat()) + "</div>");
                                qtyawal = qtyawal - stockCardReport.getBerat();
                            } else {
                                rowx.add("<div align=\"center\">&nbsp;</div>");
                                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(stockCardReport.getQty()) + "</div>");
                                qtyawal = qtyawal - stockCardReport.getQty();
                            }
                            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(qtyawal) + "</div>");
                            break;
                        case I_DocType.MAT_DOC_TYPE_OPN:
                            if (typeOfBusinessDetail == 2) {
                                qtyawal = (stockCardReport.getBerat() - qtyawal);
                            } else {
                                qtyawal = (stockCardReport.getQty() - qtyawal);
                            }

                            if (qtyawal < 0) {
                                rowx.add("<div href=\"javascript:cmdViewKartuStock('" + I_DocType.MAT_DOC_TYPE_OPN + "','" + stockCardReport.getDocCode() + "')\">" + stockCardReport.getDocCode() + "</div>"); //penerimaan
                                rowx.add("<div align=\"center\">&nbsp;</div>");
                                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(qtyawal * -1) + "</div>");
                            } else {
                                if (qtyawal == 0) {
                                    rowx.add("<div href=\"javascript:cmdViewKartuStock('" + I_DocType.MAT_DOC_TYPE_OPN + "','" + stockCardReport.getDocCode() + "')\">" + stockCardReport.getDocCode() + "</div>"); //penerimaan

                                    rowx.add("<div align=\"center\">&nbsp;</div>");
                                } else {
                                    rowx.add("<div href=\"javascript:cmdViewKartuStock('" + I_DocType.MAT_DOC_TYPE_OPN + "','" + stockCardReport.getDocCode() + "')\">" + stockCardReport.getDocCode() + "</div>"); //penerimaan

                                    rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(qtyawal) + "</div>");
                                }
                                rowx.add("<div align=\"center\">&nbsp;</div>");
                            }
                            if (typeOfBusinessDetail == 2) {
                                qtyawal = stockCardReport.getBerat();
                                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(stockCardReport.getBerat()) + "</div>");
                            } else {
                                qtyawal = stockCardReport.getQty();
                                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(stockCardReport.getQty()) + "</div>");
                            }

                            break;
                        case I_DocType.MAT_DOC_TYPE_SALE:
                            
                            String parentInfo = "";
                            try {
                                Vector<BillMain> listBill = PstBillMain.list(0, 0, PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] + " = '" + stockCardReport.getDocCode() + "'", "");
                                if (!listBill.isEmpty()) {
                                    BillMain billMainSales = PstBillMain.fetchExc(listBill.get(0).getParentId());
                                    parentInfo = (stockCardReport.getTransaction_type() == PstBillMain.TYPE_INVOICE) ? " exchange from " : " return from " + billMainSales.getInvoiceNumber();
                                }
                            } catch (Exception e) {
                                parentInfo = "";
                            }
                            
                            switch (stockCardReport.getTransaction_type()) {
                                case PstBillMain.TYPE_INVOICE:
                                    rowx.add("" + stockCardReport.getDocCode() + parentInfo);

                                    if (typeOfBusinessDetail == 2) {
                                        rowx.add("<div align=\"center\">&nbsp;</div>");
                                        rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(stockCardReport.getBerat()) + "</div>");
                                        qtyawal = qtyawal - stockCardReport.getBerat();
                                    } else {
                                        rowx.add("<div align=\"center\">&nbsp;</div>");
                                        rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(stockCardReport.getQty()) + "</div>");
                                        qtyawal = qtyawal - stockCardReport.getQty();
                                    }
                                    break;
                                case PstBillMain.TYPE_RETUR:
                                    rowx.add("" + stockCardReport.getDocCode() + parentInfo);

                                    if (typeOfBusinessDetail == 2) {
                                        rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(stockCardReport.getBerat()) + "</div>");
                                        rowx.add("<div align=\"center\">&nbsp;</div>");
                                        qtyawal = qtyawal + stockCardReport.getBerat();
                                    } else {
                                        rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(stockCardReport.getQty()) + "</div>");
                                        rowx.add("<div align=\"center\">&nbsp;</div>");
                                        qtyawal = qtyawal + stockCardReport.getQty();
                                    }
                                    break;
                                case PstBillMain.TYPE_GIFT:

                                    break;
                                case PstBillMain.TYPE_COST:

                                    break;
                                case PstBillMain.TYPE_COMPLIMENT:

                                    break;
                            }
                            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(qtyawal) + "</div>");
                            break;

                        case I_DocType.MAT_DOC_TYPE_COS:
                            rowx.add("<div href=\"javascript:cmdViewKartuStock('" + I_DocType.MAT_DOC_TYPE_COS + "','" + stockCardReport.getDocCode() + "')\">" + stockCardReport.getDocCode() + "</div>"); //penerimaan
                            rowx.add("<div align=\"center\">&nbsp;</div>");
                            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(stockCardReport.getQty()) + "</div>");
                            if (typeOfBusinessDetail == 2) {
                                qtyawal = qtyawal - stockCardReport.getBerat();
                            } else {
                                qtyawal = qtyawal - stockCardReport.getQty();
                            }
                            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(qtyawal) + "</div>");
                            break;

                    }

                    lstData.add(rowx);
                }
            }
            result = ctrlist.draw();
        } else {
            result = "<div class=\"msginfo\">&nbsp;&nbsp;" + textListGlobal[language][0] + "</div>";
        }
        return result;
    }
%>

<%//
    int iCommand = FRMQueryString.requestCommand(request);
    long locationId = FRMQueryString.requestLong(request, "location_id");
    long materialId = FRMQueryString.requestLong(request, "material_id");
    String stardDate = FRMQueryString.requestString(request, "start_date");
    String endDate = FRMQueryString.requestString(request, "end_date");
    String strStatus[] = FRMQueryString.requestString(request, "status_doc").split(",");
    int includeWarehouse = FRMQueryString.requestInt(request, "INCLUDE_WAREHOUSE");
    int showTransferHistory = FRMQueryString.requestInt(request, "SHOW_TRANSFER_HISTORY");

    SrcStockCard srcStockCard = new SrcStockCard();
    srcStockCard.setLocationId(locationId);
    srcStockCard.setMaterialId(materialId);
    srcStockCard.setStardDate(Formater.formatDate(stardDate, "yyyy-MM-dd"));
    srcStockCard.setEndDate(Formater.formatDate(endDate, "yyyy-MM-dd"));
    Vector vectSt = new Vector(1, 1);
    String statusDoc = "";
    if (strStatus != null && strStatus.length > 0) {
        for (int i = 0; i < strStatus.length; i++) {
            statusDoc += (statusDoc.isEmpty()) ? strStatus[i] : "," + strStatus[i];
            try {
                if (strStatus[i].length() == 0 || strStatus[i].isEmpty()) {
                    continue;
                }
                vectSt.add(strStatus[i]);
            } catch (Exception exc) {
                System.out.println("err");
            }
        }
    }
    srcStockCard.setDocStatus(vectSt);

    /**
     * get vectSize, start and data to be display in this page
     */
    if (SESS_LANGUAGE == 0) {
        srcStockCard.setLanguage(0);
    } else {
        srcStockCard.setLanguage(1);
    }

    Location objLocation = new Location();
    if (srcStockCard.getLocationId() != 0) {
        try {
            objLocation = PstLocation.fetchExc(srcStockCard.getLocationId());
            if (objLocation.getParentLocationId() > 0 && includeWarehouse == 1) {
                srcStockCard.setWarehouseLocationId(objLocation.getParentLocationId());
                Location wareHouse = PstLocation.fetchExc(objLocation.getParentLocationId());
                objLocation.setName(objLocation.getName() + " , " + wareHouse.getName());
            }
        } catch (Exception e) {
        }
    } else {
        objLocation.setName("All location");
    }

    Vector records = SessStockCard.createHistoryStockCard(srcStockCard);

    Material objMaterial = new Material();
    if (srcStockCard.getMaterialId() != 0) {
        try {
            objMaterial = PstMaterial.fetchExc(srcStockCard.getMaterialId());
        } catch (Exception e) {
        }
    }

    String where = "";
    Vector list = new Vector();

    if (objMaterial.getRequiredSerialNumber() == 1 || privApprovalFinal == true) {
        where = PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_LOCATION_ID] + "=" + srcStockCard.getLocationId()
                + " AND " + PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_MATERIAL_ID] + "=" + srcStockCard.getMaterialId()
                + " AND " + PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_STOCK_STATUS] + "=" + PstMaterialStockCode.FLD_STOCK_STATUS_GOOD;
        list = PstMaterialStockCode.list(0, 0, where, PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_STOCK_DATE] + " ASC ");
    }

    Unit objUnit = new Unit();
    if (objMaterial.getBuyUnitId() != 0) {
        try {
            objUnit = PstUnit.fetchExc(objMaterial.getBuyUnitId());
        } catch (Exception e) {
        }
    }

    response.setContentType("application/x-msexcel"); 
    response.setHeader("Content-Disposition","attachment; filename=StockCard_" + objMaterial.getSku() + ".xls" );
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <!--link rel="stylesheet" href="../../../styles/main.css" type="text/css"-->
        <!--link rel="stylesheet" href="../../../styles/tab.css" type="text/css"-->
        <style>
            table {
                border-collapse: collapse;
                font-size: 12px;
                font-family: Geneva, Arial, Helvetica, sans-serif;
            }
            .listgen td {
                border-collapse: collapse;
                padding: 6px 8px;
                border: 1px;
                border-style: solid;
                border-width: thin
            }
            .listgensell {
                //color: #000000;
                //background-color: #FFFFFF;
                //background-position: center;
            }
        </style>
    </head>
    <body>
        <table width="100%" cellspacing="0" cellpadding="3">
            <tr align="left" valign="top"> 
                <td height="14" align="center" valign="middle">
                    <h4><strong><%=textListGlobal[SESS_LANGUAGE][1].toUpperCase()%></strong></h4>
                </td>
            </tr>
            <tr align="left" valign="top">
                <td height="14" valign="middle" colspan="3" class="command">
                    <table width="100%"  border="0" cellspacing="1" cellpadding="1">
                        <tr>
                            <td>
                                <b><%=textListGlobal[SESS_LANGUAGE][5]%> : </b>
                                <%=Formater.formatDate(srcStockCard.getStardDate(), "dd-MM-yyyy")%>
                                <%=textListGlobal[SESS_LANGUAGE][6]%>
                                <%=Formater.formatDate(srcStockCard.getEndDate(), "dd-MM-yyyy")%>
                            </td>
                        </tr>
                        <tr>
                            <td width="15%">
                                <b><%=textListGlobal[SESS_LANGUAGE][3]%> : </b>
                                <%=objLocation.getName().toUpperCase()%>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <b><%=textListGlobal[SESS_LANGUAGE][4]%> : </b>
                                <%=objMaterial.getSku()%> / <%=(typeOfBusinessDetail == 2 ? SessMaterial.setItemNameForLitama(objMaterial.getOID()) : objMaterial.getName())%>
                            </td>
                        </tr>
                        <tr>
                            <td><b><%=textListGlobal[SESS_LANGUAGE][10]%> : </b>
                                <%
                                    if (statusDoc.length() > 0 && statusDoc != null) {
                                        String statusDocLabel[] = statusDoc.split(",");
                                        for (int ds = 0; ds < statusDocLabel.length; ds++) {
                                            if (ds > 0) {
                                                out.print(" , ");
                                            }
                                            out.print(I_DocStatus.fieldDocumentStatus[Integer.valueOf(statusDocLabel[ds])]);
                                        }
                                    } else {
                                        out.print("ALL STATUS");
                                    }
                                %>
                            </td>
                        </tr>
                        <tr>
                            <td><b><%=textListGlobal[SESS_LANGUAGE][9]%> : </b>
                                <%=objUnit.getCode()%>
                            </td>
                        </tr>
                        <tr>
                            <td><b><%=textListGlobal[SESS_LANGUAGE][11]%> : </b>
                                <%= (showTransferHistory == 1 ? "Shown" : "Hidden")%>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td><br></td>
            </tr>
            <tr align="left" valign="top">
                <td height="22" valign="middle" colspan="3"><%/**
                     * Vector hasil =
                     * drawList(SESS_LANGUAGE,records,isCategory,isSubCategory,isDispatchTo,isSupplier);
                     * Vector report = new Vector(1,1);
                     * report.add(srcStockCard); report.add(hasil); try {
                     * session.putValue("SESS_MAT_REPORT_DISPATCH",report); }
                     * catch(Exception e){}
                     *
                     * for(int k=0;k<hasil.size();k++){
                     * out.println(hasil.get(k)); }
                     */
                    %>
                    <%=drawList2(SESS_LANGUAGE, records, typeOfBusinessDetail, showTransferHistory)%>
                </td>
            </tr>
            <%
                if (list != null && list.size() > 0) {
            %>
            <tr align="left" valign="top" id="hideserial">
                <td height="22" valign="middle" colspan="3">&nbsp;&nbsp;<table width="30%"  border="0" cellpadding="1" cellspacing="1" class="listgen">
                        <tr align="center" class="listgensell">
                            <td width="16%">No</td>
                            <td width="84%">Serial Number (in Stock)</td>
                            <%if (privApprovalFinal) {%>
                            <td width="84%">Value</td>
                            <%} else {%>
                            <td width="84%"></td>
                            <%}%>
                        </tr>
                        <%
                            for (int k = 0; k < list.size(); k++) {
                                MaterialStockCode materialStockCode = (MaterialStockCode) list.get(k);
                        %>

                        <tr class="listgensell">
                            <td>&nbsp;<%=(k + 1)%></td>
                            <td>&nbsp;<%=materialStockCode.getStockCode()%></td>
                            <%if (privApprovalFinal) {%>
                            <td>&nbsp;<%=Formater.formatNumber(materialStockCode.getStockValue(), "###.###")%></td>
                            <%} else {%>
                            <td>&nbsp;</td>
                            <%}%>
                        </tr>
                        <%
                            }
                        %>
                    </table>
                </td>
            </tr>
            <%
                }
            %>
        </table>
    </body>
</html>

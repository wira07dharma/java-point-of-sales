<%@page import="com.dimata.posbo.entity.warehouse.PstProduction"%>
<%@page import="com.dimata.pos.entity.billing.BillMain"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatDispatch"%>
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
<!-- Jsp Block -->
<%!//
    public static final int ADD_TYPE_SEARCH = 0;
    public static final int ADD_TYPE_LIST = 1;

    public static final String textListGlobal[][] = {
        {"Tidak ada data", "Kartu Stok", "Periode", "Lokasi", "Kode / Nama Barang", "Periode", " s/d ", "Cetak Kartu Stok", "Stok awal", "Satuan","Status Dokumen","Riwayat Perpindahan"},
        {"No available data", "Stock Card", "Period", "Location", "Code / Goods Name", "Period", " to ", "Print Stock Card", "Beginning stock", "Unit", "Document Status","Tranfer History"}
    };

    /* this constant used to list text of listHeader */
    public static final String textListMaterialHeader[][] = {
        {"Tanggal", "Nomor Dokumen", "Keterangan", "Mutasi", "Stok Masuk", "Stok Keluar", "Saldo"},
        {"Date", "Number", "Remark", "Mutation", "Stock In", "Stock Out", "Saldo"}
    };

    public Vector drawLineHorizontal() {
        Vector rowx = new Vector();
        //Add Under line
        rowx.add("-");
        rowx.add("--------------------------");
        rowx.add("<div align=\"center\">" + "-" + "</div>");
        rowx.add("----------------------------------------------");
        rowx.add("<div align=\"center\">" + "-" + "</div>");
        rowx.add("--------------------------------------------------------------------------");
        rowx.add("<div align=\"center\">" + "-" + "</div>");
        rowx.add("----------");
        rowx.add("<div align=\"center\">" + "-" + "</div>");
        rowx.add("----------");
        rowx.add("<div align=\"center\">" + "-" + "</div>");
        rowx.add("----------");
        rowx.add("<div align=\"center\">" + "-" + "</div>");

        return rowx;
    }

    public Vector drawHeader(int language) {
        Vector rowx = new Vector();
        //Add Header
        rowx.add("|");
        rowx.add("<div align=\"center\">" + textListMaterialHeader[language][0] + "</div>");
        rowx.add("<div align=\"center\">" + "|" + "</div>");
        rowx.add("<div align=\"center\">" + textListMaterialHeader[language][2] + "</div>");
        rowx.add("<div align=\"center\">" + "|" + "</div>");
        rowx.add("<div align=\"center\">" + textListMaterialHeader[language][1] + "</div>");
        rowx.add("<div align=\"center\">" + "|" + "</div>");
        rowx.add("<div align=\"center\">" + textListMaterialHeader[language][4] + "</div>");
        rowx.add("<div align=\"center\">" + "|" + "</div>");
        rowx.add("<div align=\"center\">" + textListMaterialHeader[language][5] + "</div>");
        rowx.add("<div align=\"center\">" + "|" + "</div>");
        rowx.add("<div align=\"center\">" + textListMaterialHeader[language][6] + "</div>");
        rowx.add("<div align=\"center\">" + "|" + "</div>");

        return rowx;
    }

    public Vector drawLineTotal() {
        Vector rowx = new Vector();
        rowx.add("");
        rowx.add("");
        rowx.add("<div align=\"center\">" + "-" + "</div>");
        rowx.add("-----------");
        rowx.add("<div align=\"center\">" + "-" + "</div>");
        rowx.add("-----------");
        rowx.add("<div align=\"center\">" + "-" + "</div>");
        rowx.add("-----------");
        rowx.add("<div align=\"center\">" + "-" + "</div>");
        rowx.add("-----------");
        rowx.add("<div align=\"center\">" + "-" + "</div>");
        rowx.add("-----------");
        rowx.add("<div align=\"center\">" + "-" + "</div>");
        return rowx;
    }

    public Vector drawLineSingleSpot() {
        Vector rowx = new Vector();
        rowx.add("-");
        rowx.add("");
        rowx.add("");
        rowx.add("");
        rowx.add("");
        rowx.add("");
        rowx.add("");
        rowx.add("");
        rowx.add("");
        rowx.add("");
        rowx.add("");
        rowx.add("");
        rowx.add("");

        return rowx;
    }

    public Vector drawLineTotalSide() {
        Vector rowx = new Vector();
        rowx.add("");
        rowx.add("");
        rowx.add("<div align=\"center\">" + "-" + "</div>");
        rowx.add("--------");
        rowx.add("<div align=\"center\">" + "-" + "</div>");
        rowx.add("--------");
        rowx.add("<div align=\"center\">" + "-" + "</div>");
        rowx.add("--------");
        rowx.add("<div align=\"center\">" + "-" + "</div>");
        rowx.add("--------");
        rowx.add("<div align=\"center\">" + "-" + "</div>");
        rowx.add("--------");
        rowx.add("<div align=\"center\">" + "-" + "</div>");

        return rowx;
    }

    public Vector drawList(int language, Vector listAll, boolean isCategory, boolean isSubCategory,
            boolean isDispatchTo, boolean isSupplier) {
        Vector result = new Vector();
        if (listAll != null && listAll.size() > 0) {
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("85%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("listgentitle");
            ctrlist.setCellSpacing("0");

            ctrlist.dataFormat("", "1%", "0", "0", "left", "top");
            ctrlist.dataFormat("", "10%", "0", "0", "center", "top");
            ctrlist.dataFormat("", "1%", "0", "0", "left", "top");
            ctrlist.dataFormat("", "15%", "0", "0", "center", "top");
            ctrlist.dataFormat("", "1%", "0", "0", "right", "bottom");
            ctrlist.dataFormat("", "25%", "0", "0", "right", "bottom");
            ctrlist.dataFormat("", "1%", "0", "0", "right", "bottom");
            ctrlist.dataFormat("", "4%", "0", "0", "right", "bottom");
            ctrlist.dataFormat("", "1%", "0", "0", "right", "bottom");
            ctrlist.dataFormat("", "4%", "0", "0", "right", "bottom");
            ctrlist.dataFormat("", "1%", "0", "0", "right", "bottom");
            ctrlist.dataFormat("", "4%", "0", "0", "right", "bottom");
            ctrlist.dataFormat("", "1%", "0", "0", "right", "bottom");

            ctrlist.setLinkRow(-1);
            ctrlist.setLinkSufix("");
            Vector lstData = ctrlist.getData();
            Vector lstLinkData = ctrlist.getLinkData();
            ctrlist.setLinkPrefix("javascript:cmdEdit('");
            ctrlist.setLinkSufix("')");
            ctrlist.reset();
            boolean firstRow = true;
            double totalPrice = 0.00;
            double totalQty = 0;
            int baris = 0;
            int maxlines = 72;
            int maxlinespgdst = 74;
            boolean boolmaxlines = true;
            Vector list_all_data = new Vector(1, 1);

            double qtyawal = 0;
            StockCardReport stockCrp = (StockCardReport) listAll.get(0);
            //------- create header
            lstData.add(drawLineHorizontal());
            baris += 1;
            lstData.add(drawHeader(language));
            baris += 1;
            lstData.add(drawLineHorizontal());
            baris += 1;
            // --------
            Vector rowx = new Vector(1, 1);
            rowx.add("|");
            rowx.add(Formater.formatDate(stockCrp.getDate(), "dd/MM/yyyy"));
            rowx.add("<div align=\"center\">|</div>");
            rowx.add(stockCrp.getKeterangan());
            rowx.add("<div align=\"center\">|</div>");
            rowx.add(stockCrp.getDocCode());
            rowx.add("<div align=\"center\">|</div>");

            rowx.add("<div align=\"center\">&nbsp;</div>");
            rowx.add("<div align=\"center\">|</div>");
            rowx.add("<div align=\"center\">&nbsp;</div>");
            rowx.add("<div align=\"center\">|</div>");
            System.out.println("stockCrp.getDate().getYear() : " + stockCrp.getDate().getYear());
        //if((stockCrp.getDate().getYear()+1900) == 2005)
            //    stockCrp.setQty(0);

            qtyawal = stockCrp.getQty();
            rowx.add("<div align=\"center\">" + FRMHandler.userFormatStringDecimal(stockCrp.getQty()) + "</div>");
            rowx.add("<div align=\"center\">|</div>");
            lstData.add(rowx);
            lstLinkData.add("");

            Vector objectClass = (Vector) listAll.get(1);
            for (int i = 0; i < objectClass.size(); i++) {
                StockCardReport stockCardReport = (StockCardReport) objectClass.get(i);

                if (firstRow) {
                    firstRow = false;
                }

                rowx = new Vector(1, 1);
                rowx.add("|");
                rowx.add(Formater.formatDate(stockCardReport.getDate(), "dd/MM/yyyy"));
                rowx.add("<div align=\"center\">|</div>");
                rowx.add(stockCardReport.getKeterangan());
                rowx.add("<div align=\"center\">|</div>");
                rowx.add(stockCardReport.getDocCode());
                rowx.add("<div align=\"center\">|</div>");
                switch (stockCardReport.getDocType()) {
                    case I_DocType.MAT_DOC_TYPE_LMRR:
                        rowx.add("<div align=\"center\">" + FRMHandler.userFormatStringDecimal(stockCardReport.getQty()) + "</div>");
                        rowx.add("<div align=\"center\">|</div>");
                        rowx.add("<div align=\"center\">&nbsp;</div>");
                        rowx.add("<div align=\"center\">|</div>");
                        qtyawal = qtyawal + stockCardReport.getQty();
                        rowx.add("<div align=\"center\">" + FRMHandler.userFormatStringDecimal(qtyawal) + "</div>");
                        rowx.add("<div align=\"center\">|</div>");
                        break;
                    case I_DocType.MAT_DOC_TYPE_ROMR:
                        rowx.add("<div align=\"center\">&nbsp;</div>");
                        rowx.add("<div align=\"center\">|</div>");
                        rowx.add("<div align=\"center\">" + FRMHandler.userFormatStringDecimal(stockCardReport.getQty()) + "</div>");
                        rowx.add("<div align=\"center\">|</div>");
                        qtyawal = qtyawal - stockCardReport.getQty();
                        rowx.add("<div align=\"center\">" + FRMHandler.userFormatStringDecimal(qtyawal) + "</div>");
                        rowx.add("<div align=\"center\">|</div>");
                        break;
                    case I_DocType.MAT_DOC_TYPE_DF:
                        rowx.add("<div align=\"center\">&nbsp;</div>");
                        rowx.add("<div align=\"center\">|</div>");
                        rowx.add("<div align=\"center\">" + FRMHandler.userFormatStringDecimal(stockCardReport.getQty()) + "</div>");
                        rowx.add("<div align=\"center\">|</div>");
                        qtyawal = qtyawal - stockCardReport.getQty();
                        rowx.add("<div align=\"center\">" + FRMHandler.userFormatStringDecimal(qtyawal) + "</div>");
                        rowx.add("<div align=\"center\">|</div>");
                        break;
                    case I_DocType.MAT_DOC_TYPE_OPN:
                        qtyawal = (stockCardReport.getQty() - qtyawal);
                        if (qtyawal < 0) {
                            rowx.add("<div align=\"center\">&nbsp;</div>");
                            rowx.add("<div align=\"center\">|</div>");
                            rowx.add("<div align=\"center\">" + FRMHandler.userFormatStringDecimal(qtyawal * -1) + "</div>");
                        } else {
                            if (qtyawal == 0) {
                                rowx.add("<div align=\"center\">&nbsp;</div>");
                            } else {
                                rowx.add("<div align=\"center\">" + FRMHandler.userFormatStringDecimal(qtyawal) + "</div>");
                            }

                            rowx.add("<div align=\"center\">|</div>");
                            rowx.add("<div align=\"center\">&nbsp;</div>");
                        }
                        //rowx.add("<div align=\"center\">"+qtyawal+"</div>");
                        rowx.add("<div align=\"center\">|</div>");
                        qtyawal = stockCardReport.getQty();
                        rowx.add("<div align=\"center\">" + FRMHandler.userFormatStringDecimal(stockCardReport.getQty()) + "</div>");
                        rowx.add("<div align=\"center\">|</div>");
                        break;
                    case I_DocType.MAT_DOC_TYPE_SALE:
                        System.out.println("stockCardReport.getTransaction_type() : " + stockCardReport.getTransaction_type());
                        switch (stockCardReport.getTransaction_type()) {
                            case PstBillMain.TYPE_INVOICE:
                                rowx.add("<div align=\"center\">&nbsp;</div>");
                                rowx.add("<div align=\"center\">|</div>");
                                rowx.add("<div align=\"center\">" + FRMHandler.userFormatStringDecimal(stockCardReport.getQty()) + "</div>");
                                rowx.add("<div align=\"center\">|</div>");
                                qtyawal = qtyawal - stockCardReport.getQty();
                                break;
                            case PstBillMain.TYPE_RETUR:
                                rowx.add("<div align=\"center\">" + FRMHandler.userFormatStringDecimal(stockCardReport.getQty()) + "</div>");
                                rowx.add("<div align=\"center\">|</div>");
                                rowx.add("<div align=\"center\">&nbsp;</div>");
                                rowx.add("<div align=\"center\">|</div>");

                                rowx.setElementAt("Terima Ret.Konsumen", 3);
                                qtyawal = qtyawal + stockCardReport.getQty();
                                break;
                            case PstBillMain.TYPE_GIFT:

                                break;
                            case PstBillMain.TYPE_COST:

                                break;
                            case PstBillMain.TYPE_COMPLIMENT:

                                break;
                        }
                        rowx.add("<div align=\"center\">" + FRMHandler.userFormatStringDecimal(qtyawal) + "</div>");
                        rowx.add("<div align=\"center\">|</div>");
                        break;

                    case I_DocType.MAT_DOC_TYPE_COS:
                        rowx.add("<div align=\"center\">&nbsp;</div>");
                        rowx.add("<div align=\"center\">|</div>");
                        rowx.add("<div align=\"center\">" + FRMHandler.userFormatStringDecimal(stockCardReport.getQty()) + "</div>");
                        rowx.add("<div align=\"center\">|</div>");
                        qtyawal = qtyawal - stockCardReport.getQty();
                        rowx.add("<div align=\"center\">" + FRMHandler.userFormatStringDecimal(qtyawal) + "</div>");
                        rowx.add("<div align=\"center\">|</div>");
                        break;

                }
                lstData.add(rowx);
                lstLinkData.add("");
            }
            lstData.add(drawLineHorizontal());
            result = ctrlist.drawMePartVector(0, lstData.size(), 13);
        } else {
            result.add("<div class=\"msginfo\">&nbsp;&nbsp;" + textListGlobal[language][0] + "</div>");
        }
        return result;
    }

    public String drawList2(int language, Vector listAll, int typeOfBusinessDetail, int showTransferHistory) {
        String result = "";
        if (listAll != null && listAll.size() > 0) {
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("100%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("listgentitle");

            ctrlist.addHeader(textListMaterialHeader[language][0], "10%");
            ctrlist.addHeader(textListMaterialHeader[language][2], "20%");
            ctrlist.addHeader(textListMaterialHeader[language][1], "25%");
            ctrlist.addHeader(textListMaterialHeader[language][4], "10%");
            ctrlist.addHeader(textListMaterialHeader[language][5], "10%");
            ctrlist.addHeader(textListMaterialHeader[language][6], "10%");

            ctrlist.setLinkRow(1);
            ctrlist.setLinkSufix("");
            Vector lstData = ctrlist.getData();

            double qtyawal = 0;
            StockCardReport stockCrp = (StockCardReport) listAll.get(0);
            Vector rowx = new Vector(1, 1);
            rowx.add("<div align=\"center\">" + Formater.formatDate(stockCrp.getDate(), "dd/MM/yyyy") + "</div>");
            rowx.add(textListGlobal[language][8]);
            rowx.add("");
            rowx.add("<div align=\"center\">&nbsp;</div>");
            rowx.add("<div align=\"center\">&nbsp;</div>");
            qtyawal = stockCrp.getQty();
            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(stockCrp.getQty()) + "</div>");
            lstData.add(rowx);

            HashMap<Long, Double> mapQtyLocation = (HashMap<Long, Double>) listAll.get(2);
            if (mapQtyLocation.isEmpty()){
                Vector listLocation = PstLocation.listAll();
                for (int i =0; i < listLocation.size();i++){
                    Location loc = (Location) listLocation.get(i);
                    mapQtyLocation.put(loc.getOID(), 0.0);
                }
            }
            Vector objectClass = (Vector) listAll.get(1);

            if (objectClass != null && objectClass.size() > 0) {
                for (int i = 0; i < objectClass.size(); i++) {
                    StockCardReport stockCardReport = (StockCardReport) objectClass.get(i);
                    rowx = new Vector();
                    rowx.add("<div align=\"center\">" + Formater.formatDate(stockCardReport.getDate(), "dd/MM/yyyy") + "</div>");
                    rowx.add(stockCardReport.getKeterangan());
                    
                    switch (stockCardReport.getDocType()) {
                        case I_DocType.MAT_DOC_TYPE_LMRR:
                            switch (stockCardReport.getTransaction_type()) {
                                case I_DocType.MAT_RECEIVE_SOURCE_PENERIMAAN_WITH_PO:
                                    rowx.add("<a href=\"javascript:cmdViewKartuStock('" + I_DocType.MAT_DOC_TYPE_LMRR + "','" + stockCardReport.getDocCode() + "','" + stockCardReport.getTransaction_type() + "')\">" + stockCardReport.getDocCode() + "</a>"); //penerimaan  
                                    break;
                                case I_DocType.MAT_RECEIVE_SOURCE_PENERIMAAN_TRANSFER_TOKO:
                                    if (showTransferHistory == 0) {continue;}
                                    rowx.add("<a href=\"javascript:cmdViewKartuStock('" + I_DocType.MAT_DOC_TYPE_LMRR + "','" + stockCardReport.getDocCode() + "','" + stockCardReport.getTransaction_type() + "')\">" + stockCardReport.getDocCode() + "</a>"); //penerimaan  
                                    break;
                                case I_DocType.MAT_RECEIVE_SOURCE_PENERIMAAN_TRANSFER_UNIT:
                                    rowx.add("<a href=\"javascript:cmdViewKartuStock('" + I_DocType.MAT_DOC_TYPE_LMRR + "','" + stockCardReport.getDocCode() + "','" + stockCardReport.getTransaction_type() + "')\">" + stockCardReport.getDocCode() + "</a>"); //penerimaan  
                                    break;
                                default:
                                    rowx.add("<a href=\"javascript:cmdViewKartuStock('" + I_DocType.MAT_DOC_TYPE_LMRR + "','" + stockCardReport.getDocCode() + "','" + stockCardReport.getTransaction_type() + "')\">" + stockCardReport.getDocCode() + "</a>"); //penerimaan  
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
                                mapQtyLocation.put(stockCardReport.getLocationId(), stockCardReport.getQty() + mapQtyLocation.get(stockCardReport.getLocationId()));
                            }
                            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(qtyawal) + "</div>");
                            break;
                        case I_DocType.MAT_DOC_TYPE_ROMR:
                            rowx.add("<a href=\"javascript:cmdViewKartuStock('" + I_DocType.MAT_DOC_TYPE_ROMR + "','" + stockCardReport.getDocCode() + "')\">" + stockCardReport.getDocCode() + "</a>"); //penerimaan
                            if (typeOfBusinessDetail == 2) {
                                rowx.add("<div align=\"center\">&nbsp;</div>");
                                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(stockCardReport.getBerat()) + "</div>");
                                qtyawal = qtyawal - stockCardReport.getBerat();
                            } else {
                                rowx.add("<div align=\"center\">&nbsp;</div>");
                                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(stockCardReport.getQty()) + "</div>");
                                qtyawal = qtyawal - stockCardReport.getQty();
                                mapQtyLocation.put(stockCardReport.getLocationId(), mapQtyLocation.get(stockCardReport.getLocationId()) - stockCardReport.getQty());
                            }
                            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(qtyawal) + "</div>");
                            break;
                        case I_DocType.MAT_DOC_TYPE_DF:
                            switch (stockCardReport.getTransaction_type()) {
                                case PstMatDispatch.FLD_TYPE_DISPATCH_LOCATION_STORE:
                                case PstMatDispatch.FLD_TYPE_DISPATCH_LOCATION_WAREHOUSE:
                                    if (showTransferHistory == 0) {continue;}
                                    break;
                            }
                            rowx.add("<a href=\"javascript:cmdViewKartuStock('" + I_DocType.MAT_DOC_TYPE_DF + "','" + stockCardReport.getDocCode() + "')\">" + stockCardReport.getDocCode() + "</a>"); //penerimaan
                            if (typeOfBusinessDetail == 2) {
                                rowx.add("<div align=\"center\">&nbsp;</div>");
                                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(stockCardReport.getBerat()) + "</div>");
                                qtyawal = qtyawal - stockCardReport.getBerat();
                            } else {
                                rowx.add("<div align=\"center\">&nbsp;</div>");
                                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(stockCardReport.getQty()) + "</div>");
                                qtyawal = qtyawal - stockCardReport.getQty();
                                mapQtyLocation.put(stockCardReport.getLocationId(), mapQtyLocation.get(stockCardReport.getLocationId()) - stockCardReport.getQty());
                            }
                            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(qtyawal) + "</div>");
                            break;
                        case I_DocType.MAT_DOC_TYPE_OPN:
//                            if (typeOfBusinessDetail == 2) {
//                                qtyawal = (stockCardReport.getBerat() - qtyawal);
//                            } else {
//                                qtyawal = (mapQtyLocation.get(stockCardReport.getLocationId()) + stockCardReport.getQty());
//                                mapQtyLocation.put(stockCardReport.getLocationId(), stockCardReport.getQty());
//                            }
                            double selisih = stockCardReport.getQty() - mapQtyLocation.get(stockCardReport.getLocationId());
                            if (selisih < 0) {
                                rowx.add("<a href=\"javascript:cmdViewKartuStock('" + I_DocType.MAT_DOC_TYPE_OPN + "','" + stockCardReport.getDocCode() + "')\">" + stockCardReport.getDocCode() + "</a>"); //penerimaan
                                rowx.add("<div align=\"center\">&nbsp;</div>");
                                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(selisih * -1) + "</div>");
                            } else {
                                if (selisih == 0) {
                                    rowx.add("<a href=\"javascript:cmdViewKartuStock('" + I_DocType.MAT_DOC_TYPE_OPN + "','" + stockCardReport.getDocCode() + "')\">" + stockCardReport.getDocCode() + "</a>"); //penerimaan

                                    rowx.add("<div align=\"center\">&nbsp;</div>");
                                } else {
                                    rowx.add("<a href=\"javascript:cmdViewKartuStock('" + I_DocType.MAT_DOC_TYPE_OPN + "','" + stockCardReport.getDocCode() + "')\">" + stockCardReport.getDocCode() + "</a>"); //penerimaan

                                    rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(selisih) + "</div>");
                                }
                                rowx.add("<div align=\"center\">&nbsp;</div>");
                            }
                            if (typeOfBusinessDetail == 2) {
                                qtyawal = stockCardReport.getBerat();
                                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(stockCardReport.getBerat()) + "</div>");
                            } else {
                                qtyawal = (qtyawal - mapQtyLocation.get(stockCardReport.getLocationId()) + stockCardReport.getQty());
                                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(qtyawal) + "</div>");
                                mapQtyLocation.put(stockCardReport.getLocationId(), stockCardReport.getQty());
                            }

                            break;
                        case I_DocType.MAT_DOC_TYPE_SALE:
                            
                            String parentInfo = "";
                            try {
                                Vector<BillMain> listBill = PstBillMain.list(0, 0, PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] + " = '" + stockCardReport.getDocCode() + "'", "");
                                if (!listBill.isEmpty()) {
                                    BillMain billMainSales = PstBillMain.fetchExc(listBill.get(0).getParentId());
                                    parentInfo = (stockCardReport.getTransaction_type() == PstBillMain.TYPE_INVOICE) ? " exchange from " : " return from ";
                                    parentInfo += " <a href=\"javascript:cmdViewKartuStock('" + I_DocType.MAT_DOC_TYPE_SALE + "','" + billMainSales.getInvoiceNumber()+ "')\">" + billMainSales.getInvoiceNumber() + "</a>";
                                }
                            } catch (Exception e) {
                                parentInfo = "";
                            }

                            switch (stockCardReport.getTransaction_type()) {
                                case PstBillMain.TYPE_INVOICE:
                                    rowx.add("<a href=\"javascript:cmdViewKartuStock('" + I_DocType.MAT_DOC_TYPE_SALE + "','" + stockCardReport.getDocCode() + "')\">" + stockCardReport.getDocCode() + "</a>" + parentInfo);

                                    if (typeOfBusinessDetail == 2) {
                                        rowx.add("<div align=\"center\">&nbsp;</div>");
                                        rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(stockCardReport.getBerat()) + "</div>");
                                        qtyawal = qtyawal - stockCardReport.getBerat();
                                    } else {
                                        rowx.add("<div align=\"center\">&nbsp;</div>");
                                        rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(stockCardReport.getQty()) + "</div>");
                                        qtyawal = qtyawal - stockCardReport.getQty();
                                        mapQtyLocation.put(stockCardReport.getLocationId(), mapQtyLocation.get(stockCardReport.getLocationId()) - stockCardReport.getQty());
                                    }
                                    break;
                                case PstBillMain.TYPE_RETUR:
                                    rowx.add("<a href=\"javascript:cmdViewKartuStock('" + I_DocType.MAT_DOC_TYPE_SALE + "','" + stockCardReport.getDocCode() + "')\">" + stockCardReport.getDocCode() + "</a>" + parentInfo);

                                    if (typeOfBusinessDetail == 2) {
                                        rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(stockCardReport.getBerat()) + "</div>");
                                        rowx.add("<div align=\"center\">&nbsp;</div>");
                                        qtyawal = qtyawal + stockCardReport.getBerat();
                                    } else {
                                        rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(stockCardReport.getQty()) + "</div>");
                                        rowx.add("<div align=\"center\">&nbsp;</div>");
                                        qtyawal = qtyawal + stockCardReport.getQty();
                                        mapQtyLocation.put(stockCardReport.getLocationId(), mapQtyLocation.get(stockCardReport.getLocationId()) + stockCardReport.getQty());
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
							if (typeOfBusinessDetail == 2) {
							rowx.add("<a href=\"javascript:cmdViewKartuStock('" + I_DocType.MAT_DOC_TYPE_COS + "','" + stockCardReport.getDocCode() + "')\">" + stockCardReport.getDocCode() + "</a>"); //penerimaan
                            rowx.add("<div align=\"center\">&nbsp;</div>");
                            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(stockCardReport.getBerat()) + "</div>");
							} else {
                            rowx.add("<a href=\"javascript:cmdViewKartuStock('" + I_DocType.MAT_DOC_TYPE_COS + "','" + stockCardReport.getDocCode() + "')\">" + stockCardReport.getDocCode() + "</a>"); //penerimaan
                            rowx.add("<div align=\"center\">&nbsp;</div>");
                            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(stockCardReport.getQty()) + "</div>");

							}
                            if (typeOfBusinessDetail == 2) {
                                qtyawal = qtyawal - stockCardReport.getBerat();
                            } else {
                                qtyawal = qtyawal - stockCardReport.getQty();
                            }
                            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(qtyawal) + "</div>");
                            break;
                            
                        case I_DocType.MAT_DOC_TYPE_PROD:
                            switch (stockCardReport.getTransaction_type()) {
                                case PstProduction.PRODUCTION_COST:
                                    rowx.add("<a href=\"javascript:cmdViewKartuStock('" + I_DocType.MAT_DOC_TYPE_PROD + "','" + stockCardReport.getDocCode() + "')\">" + stockCardReport.getDocCode() + "</a>"); //penerimaan
                                    rowx.add("<div align=\"center\">&nbsp;</div>");
                                    rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(stockCardReport.getQty()) + "</div>");
                                    qtyawal = qtyawal - stockCardReport.getQty();
                                    mapQtyLocation.put(stockCardReport.getLocationId(), mapQtyLocation.get(stockCardReport.getLocationId()) - stockCardReport.getQty());
                                    break;
                                case PstProduction.PRODUCTION_PRODUCT:
                                    rowx.add("<a href=\"javascript:cmdViewKartuStock('" + I_DocType.MAT_DOC_TYPE_PROD + "','" + stockCardReport.getDocCode() + "')\">" + stockCardReport.getDocCode() + "</a>"); //penerimaan
                                    rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(stockCardReport.getQty()) + "</div>");
                                    rowx.add("<div align=\"center\">&nbsp;</div>");
                                    qtyawal = qtyawal + stockCardReport.getQty();
                                    mapQtyLocation.put(stockCardReport.getLocationId(), mapQtyLocation.get(stockCardReport.getLocationId()) + stockCardReport.getQty());
                                    break;
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
    int start = FRMQueryString.requestInt(request, "start");
    int index = FRMQueryString.requestInt(request, "type");
    int includeWarehouse = FRMQueryString.requestInt(request, "INCLUDE_WAREHOUSE");
    int showTransferHistory = FRMQueryString.requestInt(request, "SHOW_TRANSFER_HISTORY");

    boolean isCategory = false;
    boolean isSubCategory = false;
    boolean isDispatchTo = false;
    boolean isSupplier = false;

    String statusDoc = "";

    ControlLine ctrLine = new ControlLine();
    SrcStockCard srcStockCard = new SrcStockCard();
    SessStockCard sessStockCard = new SessStockCard();
    FrmSrcStockCard frmSrcStockCard = new FrmSrcStockCard(request, srcStockCard);
    if (iCommand == Command.BACK || iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST) {
        try {
            srcStockCard = (SrcStockCard) session.getValue(SessStockCard.SESS_STOCK_CARD);
            if (srcStockCard == null) {
                srcStockCard = new SrcStockCard();
            }
        } catch (Exception e) {
            srcStockCard = new SrcStockCard();
        }
    } else {
        frmSrcStockCard.requestEntityObject(srcStockCard);
        Vector vectSt = new Vector(1, 1);
        String[] strStatus = request.getParameterValues(FrmSrcStockCard.fieldNames[FrmSrcStockCard.FRM_FIELD_DOC_STATUS]);
        if (strStatus != null && strStatus.length > 0) {
            for (int i = 0; i < strStatus.length; i++) {
                statusDoc += (statusDoc.isEmpty()) ? strStatus[i] : "," + strStatus[i];
                try {
                    vectSt.add(strStatus[i]);
                } catch (Exception exc) {
                    System.out.println("err");
                }
            }
        }
        srcStockCard.setDocStatus(vectSt);
        session.putValue(SessStockCard.SESS_STOCK_CARD, srcStockCard);
    }

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
%>
<!-- End of Jsp Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
    <head>
        <!-- #BeginEditable "doctitle" -->
        <title>Dimata - ProChain POS</title>
        <script language="JavaScript">
    function cmdBack() {
        document.frm_reportcosting.command.value = "<%=Command.BACK%>";
        document.frm_reportcosting.action = "src_stockcard.jsp";
        document.frm_reportcosting.submit();
    }

    function printForm() {
        //window.open("reportcosting_form_print.jsp","stock_card","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
        window.open("buff_pdf_stockcard.jsp", "stock_card");
    }

    function printExcel() {
        var params = "location_id=<%= srcStockCard.getLocationId()%>"
                + "&material_id=<%= srcStockCard.getMaterialId()%>"
                + "&start_date=<%= Formater.formatDate(srcStockCard.getStardDate(), "yyyy-MM-dd")%>"
                + "&end_date=<%= Formater.formatDate(srcStockCard.getEndDate(), "yyyy-MM-dd")%>"
                + "&status_doc=<%= statusDoc%>"
                + "&INCLUDE_WAREHOUSE=<%= includeWarehouse %>"
                + "&SHOW_TRANSFER_HISTORY=<%= showTransferHistory %>"
                + "";
        window.open("export_excel_stock_card.jsp?" + params, "export_excel", "");
    }

    function cmdViewKartuStock(type, codeDoc) {
        return cmdViewKartuStock(type, codeDoc, 0);
    }
    function cmdViewKartuStock(type, codeDoc, source) {
        checkAjax(type, codeDoc, source);
        var code = document.frm_reportcosting.oid_document_hidden.value;
        var strvalue = "";
        //alert("code"+code);
        //alert("type"+type);
        switch (type)
        {
            case '<%="" + I_DocType.MAT_DOC_TYPE_LMRR%>':
                switch (source)
                {
                    case '1'://with po
                        //alert("1");
                        strvalue = "../receive/receive_wh_supp_po_material_edit.jsp?command=3&hidden_receive_id=" + code;
                        break;
                    case '2'://transfe
                        //alert("2");
                        strvalue = "../receive/receive_store_wh_material_edit.jsp?command=3&hidden_receive_id=" + code;
                        break;
                    case '4'://transfer unit
                        //alert("4");
                        strvalue = "../dispatch/df_unit_wh_material_edit.jsp?command=3&hidden_dispatch_id=" + code;
                        break;
                    default:
                        strvalue = "../receive/receive_wh_supp_material_edit_old.jsp?command=3&hidden_receive_id=" + code;
                        break;
                }

                break;
            case '<%="" + I_DocType.MAT_DOC_TYPE_ROMR%>':
                strvalue = "../return/return_wh_supp_material_edit.jsp?command=3&hidden_return_id=" + code;
                break;
            case '<%="" + I_DocType.MAT_DOC_TYPE_DF%>':
                strvalue = "../dispatch/df_stock_wh_material_edit.jsp?command=3&hidden_dispatch_id=" + code;
                break;
            case '<%=I_DocType.MAT_DOC_TYPE_OPN%>':
                strvalue = "../stock/mat_opname_edit_new.jsp?command=3&hidden_opname_id=" + code;
                break;
            case '<%="" + I_DocType.MAT_DOC_TYPE_SALE%>':
                strvalue = "../../../store/sale/report/invoice_edit.jsp?command=3&hidden_billmain_id=" + code;
                break;
            case '<%="" + I_DocType.MAT_DOC_TYPE_COS%>':
                strvalue = "../dispatch/costing_material_edit.jsp?command=3&hidden_costing_id=" + code;
                break;
            case '<%="" + I_DocType.MAT_DOC_TYPE_PROD%>':
                strvalue = "../production/production_edit.jsp?command=3&production_id=" + code;
                break;
            default :
                break;
        }

//alert("heheh "+strvalue);
        winSrcMaterial = window.open(strvalue, "document", "height=600,width=800,status=yes,toolbar=no,menubar=yes,location=no,scrollbars=yes");
        if (window.focus) {
            winSrcMaterial.focus();
        }

    }


    function checkAjax(type, codeDoc, source) {
        $.ajax({
            url: "<%=approot%>/servlet/com.dimata.posbo.ajax.CheckOidDocumentStock?type=" + type + "&codeDoc=" + codeDoc + "&source=" + source,
            type: "POST",
            async: false,
            success: function (data) {
                document.frm_reportcosting.oid_document_hidden.value = data;
            }
        });
    }
//------------------------- START JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------

//------------------------- END JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------

    function MM_swapImgRestore() { //v3.0
        var i, x, a = document.MM_sr;
        for (i = 0; a && i < a.length && (x = a[i]) && x.oSrc; i++)
            x.src = x.oSrc;
    }

    function MM_preloadImages() { //v3.0
        var d = document;
        if (d.images) {
            if (!d.MM_p)
                d.MM_p = new Array();
            var i, j = d.MM_p.length, a = MM_preloadImages.arguments;
            for (i = 0; i < a.length; i++)
                if (a[i].indexOf("#") != 0) {
                    d.MM_p[j] = new Image;
                    d.MM_p[j++].src = a[i];
                }
        }
    }

    function MM_findObj(n, d) { //v4.0
        var p, i, x;
        if (!d)
            d = document;
        if ((p = n.indexOf("?")) > 0 && parent.frames.length) {
            d = parent.frames[n.substring(p + 1)].document;
            n = n.substring(0, p);
        }
        if (!(x = d[n]) && d.all)
            x = d.all[n];
        for (i = 0; !x && i < d.forms.length; i++)
            x = d.forms[i][n];
        for (i = 0; !x && d.layers && i < d.layers.length; i++)
            x = MM_findObj(n, d.layers[i].document);
        if (!x && document.getElementById)
            x = document.getElementById(n);
        return x;
    }

    function MM_swapImage() { //v3.0
        var i, j = 0, x, a = MM_swapImage.arguments;
        document.MM_sr = new Array;
        for (i = 0; i < (a.length - 2); i += 3)
            if ((x = MM_findObj(a[i])) != null) {
                document.MM_sr[j++] = x;
                if (!x.oSrc)
                    x.oSrc = x.src;
                x.src = a[i + 2];
            }
    }

//-->
        </script>
        <!-- #EndEditable -->
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
        <!-- #BeginEditable "styles" -->
        <%if (menuUsed == MENU_ICON) {%>
        <link href="../../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
        <%}%>
        <link rel="stylesheet" href="../../../styles/main.css" type="text/css">
        <!-- #EndEditable -->
        <!-- #BeginEditable "stylestab" -->
        <link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
        <!-- #EndEditable -->
        <script src="../../../styles/jquery.min.js"></script>
        <!-- #BeginEditable "headerscript" -->
        <SCRIPT language=JavaScript>




            function hideObjectForMarketing() {
            }

            function hideObjectForWarehouse() {
            }

            function hideObjectForProduction() {
            }

            function hideObjectForPurchasing() {
            }

            function hideObjectForAccounting() {
            }

            function hideObjectForHRD() {
            }

            function hideObjectForGallery() {
            }

            function hideObjectForMasterData() {
            }

        </SCRIPT>
        <!-- #EndEditable --> 
    </head> 

    <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >

            <%if (menuUsed == MENU_PER_TRANS) {%>
            <tr>
                <td height="25" ID="TOPTITLE"> <!-- #BeginEditable "header" -->
                    <%@ include file = "../../../main/header.jsp" %>
                    <!-- #EndEditable --></td>
            </tr>
            <tr>
                <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
                    <%@ include file = "../../../main/mnmain.jsp" %>
                    <!-- #EndEditable --> </td>
            </tr>
            <%} else {%>
            <tr bgcolor="#FFFFFF">
                <td height="10" ID="MAINMENU">
                    <%@include file="../../../styletemplate/template_header.jsp" %>
                </td>
            </tr>
            <%}%>
            <tr> 
                <td valign="top" align="left"> 
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">  
                        <tr> 
                            <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->&nbsp;<!-- #EndEditable --></td>
                        </tr>
                        <tr> 
                            <td><!-- #BeginEditable "content" -->
                                <form name="frm_reportcosting" method="post" action="">
                                    <input type="hidden" name="command" value="">
                                    <input type="hidden" name="add_type" value="">
                                    <input type="hidden" name="start" value="<%=start%>">
                                    <input type="hidden" name="approval_command">
                                    <input type="hidden" name="oid_document_hidden">
                                    <input type="hidden" name="type" value="<%=index%>">
                                    <input type="hidden" name="<%= FrmSrcStockCard.fieldNames[FrmSrcStockCard.FRM_FIELD_MATERIAL_ID]%>" value="<%=srcStockCard.getMaterialId()%>">
                                    <input type="hidden" name="INCLUDE_WAREHOUSE" value="<%= includeWarehouse %>">
                                    <input type="hidden" name="SHOW_TRANSFER_HISTORY" value="<%= showTransferHistory %>">
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
                                                        <td><strong><%=textListGlobal[SESS_LANGUAGE][5]%></strong></td>
                                                        <td><strong>:</strong></td>
                                                        <td>
                                                            <%=Formater.formatDate(srcStockCard.getStardDate(), "dd-MM-yyyy")%>
                                                            <%=textListGlobal[SESS_LANGUAGE][6]%>
                                                            <%=Formater.formatDate(srcStockCard.getEndDate(), "dd-MM-yyyy")%>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td width="15%"><b><%=textListGlobal[SESS_LANGUAGE][3]%></b></td>
                                                        <td width="1%"><b>:</b></td>
                                                        <td width="74%"><%=objLocation.getName().toUpperCase()%></td>
                                                    </tr>
                                                    <tr>
                                                        <td><b><%=textListGlobal[SESS_LANGUAGE][4]%></b></td>
                                                        <td><b>:</b></td>
                                                        <td><%=objMaterial.getSku()%> / <%=(typeOfBusinessDetail == 2 ? SessMaterial.setItemNameForLitama(objMaterial.getOID()) : objMaterial.getName())%></td>
                                                    </tr>
                                                    <tr>
                                                        <td><b><%=textListGlobal[SESS_LANGUAGE][10]%></b></td>
                                                        <td><b>:</b></td>
                                                        <td>
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
                                                        <td><b><%=textListGlobal[SESS_LANGUAGE][9]%></b></td>
                                                        <td><b>:</b></td>
                                                        <td><%=objUnit.getCode()%></td>
                                                    </tr>
                                                    <tr>
                                                        <td><b><%=textListGlobal[SESS_LANGUAGE][11]%></b></td>
                                                        <td><b>:</b></td>
                                                        <td><%= (showTransferHistory == 1 ? "Shown":"Hidden") %></td>
                                                    </tr>
                                                </table>
                                            </td>
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
                                            <td height="22" valign="middle" colspan="3">&nbsp;&nbsp;
                                                <table width="30%"  border="0" cellpadding="1" cellspacing="1" class="listgen">
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
                                        <tr align="left" valign="top">
                                            <td height="22" valign="middle" colspan="3">&nbsp; </td>
                                        </tr>
                                        <tr align="left" valign="top">
                                            <td height="18" valign="top" colspan="3"> 
                                                <table width="100%" border="0">
                                                    <tr>
                                                        <td width="70%">
                                                            <table width="52%" border="0" cellspacing="0" cellpadding="0">
                                                                <tr>
                                                                    <td nowrap width="7%"><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10', '', '<%=approot%>/images/BtnBackOn.jpg', 1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE, textListGlobal[SESS_LANGUAGE][1], ctrLine.CMD_BACK_SEARCH, true)%>"></a></td>
                                                                    <td nowrap width="3%">&nbsp;</td>
                                                                    <td class="command" nowrap width="90%"><a href="javascript:cmdBack()"><%=ctrLine.getCommand(SESS_LANGUAGE, textListGlobal[SESS_LANGUAGE][1], ctrLine.CMD_BACK_SEARCH, true)%></a></td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td width="15%">
                                                            <%if (true == true) {%>
                                                            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                                                <tr>
                                                                    <td width="5%" valign="top"><a href="javascript:printForm()"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0" alt="<%=textListGlobal[SESS_LANGUAGE][7]%>"></a></td>
                                                                    <td width="95%" nowrap>&nbsp; <a href="javascript:printForm()" class="command" ><%=textListGlobal[SESS_LANGUAGE][7]%></a></td>
                                                                </tr>
                                                            </table>
                                                            <%} else {%>
                                                            &nbsp;
                                                            <%}%>
                                                        </td>
                                                        <td width="15%">
                                                            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                                                <tr>
                                                                    <td width="5%" valign="top"><a href="javascript:printExcel()"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0" alt="<%=textListGlobal[SESS_LANGUAGE][7]%>"></a></td>
                                                                    <td width="95%" nowrap>&nbsp; <a href="javascript:printExcel()" class="command" >Cetak Excel</a></td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                    </table>
                                </form>
                                <!-- #EndEditable -->
                            </td> 
                        </tr> 
                    </table>
                </td>
            </tr>
            <tr> 
                <td colspan="2" height="20"> <!-- #BeginEditable "footer" -->
                    <%if (menuUsed == MENU_ICON) {%>
                    <%@include file="../../../styletemplate/footer.jsp" %>
                    <%} else {%>
                    <%@ include file = "../../../main/footer.jsp" %>
                    <%}%>
                    <!-- #EndEditable --> 
                </td>
            </tr>
        </table>
    </body>
    <!-- #EndTemplate -->
</html>

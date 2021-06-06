<%-- 
    Document   : receive_back_material
    Created on : Oct 4, 2019, 9:25:18 AM
    Author     : Dimata 007
--%>

<%@page import="com.dimata.pos.entity.billing.PstBillMain"%>
<%@page import="com.dimata.pos.entity.billing.BillMain"%>
<%@page import="com.dimata.hanoman.entity.masterdata.MasterType"%>
<%@page import="com.dimata.hanoman.entity.masterdata.PstMasterType"%>
<%@page import="com.dimata.hanoman.entity.masterdata.PstMasterGroup"%>
<%@page import="com.dimata.hanoman.entity.masterdata.MasterGroup"%>
<%@page import="com.dimata.posbo.session.masterdata.SessMaterial"%>
<%@page import="com.dimata.posbo.session.masterdata.SessMemberReg"%>
<%@page import="com.dimata.posbo.entity.search.SrcMemberReg"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*,
         com.dimata.posbo.entity.masterdata.*,
         com.dimata.posbo.form.warehouse.CtrlMatReceive,
         com.dimata.posbo.form.warehouse.FrmMatReceive,
         com.dimata.posbo.entity.warehouse.*,
         com.dimata.posbo.form.warehouse.*,
         com.dimata.posbo.session.warehouse.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.entity.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package common -->
<%@ page import = "com.dimata.common.entity.location.*" %>
<%@ page import = "com.dimata.common.entity.contact.*" %>
<%@ page import = "com.dimata.common.entity.payment.*" %>
<!--package material -->
<%@ page import = "com.dimata.harisma.entity.masterdata.*" %>
<%@ page import="com.dimata.posbo.session.masterdata.SessPosting"%>
<%@ include file = "../../../main/javainit.jsp" %>
<%    int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE);
    int appObjCodeShowQtyAndPrice = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE_QTY_AND_PRICE);
%>
<%@ include file = "../../../main/checkuser.jsp" %>
<%//boolean privApproval = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_APPROVE));
    boolean privApprovalApprove = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_APPROVE));
    boolean privApprovalFinal = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_FINAL));
    boolean privShowQtyPrice = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeShowQtyAndPrice, AppObjInfo.COMMAND_VIEW));
%>

<%!//
    static boolean bEnableExpiredDate = false;

    public static final String textListGlobal[][] = {
        {"Penerimaan", "Dari Pembelian", "Pencarian", "Daftar", "Edit", "Dengan PO", "Tanpa PO", "Tidak ada item penerimaan barang", "Cetak Penerimaan Barang", "Posting Stock", "Posting Harga Beli", "Dari Penerimaan Kembali"}, //10
        {"Receive", "From Purchase", "Search", "List", "Edit", "With PO", "Without PO", "There is no goods receive item", "Print Goods Receive", "Posting Stock", "Posting Cost Price", "From Receive Back"}
    };

    /* this constant used to list text of listHeader */
    public static final String textListOrderHeader[][] = {
        {"Kode", "Lokasi", "Tanggal", "Supplier", "Status", "Keterangan", "Nomor Invoice", "Ppn", "Waktu", "Mata Uang", "Sub Total", "Grand Total", "Include", "%", "Terms", "Days", "Rate", "Tipe Item Penerimaan", "Tipe Penerimaan", "HEL", "Nilai Tukar", "Berat Nota", "Total Nota", "Kepemilikan", "Berat 24k"},
        {"Code", "Location", "Date", "Supplier", "Status", "Remark", "Supplier Invoice", "VAT", "Time", "Currency", "Sub Total", "Grand Total", "Include", "%", "Terms", "Days", "Rate", "Receive Item Type", "Receive Type", "HEL", "Exchange Value", "Weight", "Total Nota", "Own", "Weight 24k"}
    };

    /* this constant used to list text of listMaterialItem */
//public static final String textListOrderItem[][] = {
    //{"No","Sku","Nama Barang","Kadaluarsa","Unit","Harga Beli","Ongkos Kirim","Mata Uang","Qty","Total Beli"},
    //{"No","Code","Name","Expired Date","Unit","Cost","Delivery Cost","Currency","Qty","Total Cost","last Discount %"}
//};

    /* this constant used to list text of listMaterialItem */
    public static final String textListOrderItem[][] = {
        {"No", "Sku", "Nama Barang", "Kadaluarsa", "Unit", "Harga Beli", "Ongkos Kirim", "Mata Uang", "Qty", "Total Beli", "Diskon Terakhir %",//10
            "Diskon1 %", "Diskon2 %", "Discount Nominal", "Hapus", "Barcode", "Berat (gr)", "Keterangan", "Sorting", "Warna", "Etalase"},//20
        {"No", "Code", "Name", "Expired Date", "Unit", "Cost", "Delivery Cost", "Currency", "Qty", "Total Cost", "last Discount %", "Discount1 %",
            "Discount2 %", "Disc. Nominal", "Delete", "Barcode", "Weight", "Remark", "Sorting", "Color", "Etalase"}
    };

    /**
     * this constan used to list text of forwarder information
     */
    public static final String textListForwarderInfo[][] = {
        {"Nomor", "Nama Perusahaan", "Tanggal", "Total Biaya", "Keterangan", "Informasi Forwarder", "Tidak Ada Informasi Forwarder!", "Sebelum status 'Final' pastikan informasi 'Forwarder' diisi jika ada !"},
        {"Number", "Company Name", "Date", "Total Cost", "Remark", "Forwarder Information", "No Forwarder Information!", "Prior to final status, make sure forwarder information is entered if required !"}
    };

    public static final String textListDetailPayment[][] = {
        {"Nomor", "Rincian Pembayaran", "Pembayaran"},
        {"Number", "Detail Of Payment", "Payment"}
    };

    public static final String textPosting[][] = {
        {"Anda yakin melakukan Posting Stok ?", "Anda yakin melakukan Posting Harga ?"},
        {"Are You Sure to Posting Stock ? ", "Are You Sure to Posting Cost Price?"}
    };

    public static final String textDelete[][] = {
        {"Apakah Anda Yakin Akan Menghapus Data ?"},
        {"Are You Sure to Delete This Data? "}
    };

    /**
     * this method used to list all po item
     */
    public Vector drawListRecItem(int language, Vector objectClass, int start, boolean privManageData,
            boolean privShowQtyPrice, int statusDoc, String approot, int select_status,
            String typeOfBusiness, String syspropDiscount1, String syspropDiscount2,
            String syspropDiscountNominal, String syspropOngkosKirim, String syspropBonus,
            String syspropHargaBeli, int typeOfBusinessDetail, int recItemType, MatReceive rec,
            String syspropColor, String syspropEtalase, String syspropTotalBeli, String syspropHargaJual) {
        Vector list = new Vector(1, 1);
        Vector listError = new Vector(1, 1);
        String result = "";
        if (objectClass != null && objectClass.size() > 0) {
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("100%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("listgentitle");

            ctrlist.addHeader(textListOrderItem[language][0], "1%");//No
            ctrlist.addHeader("Invoice", "10%");//invoice
            ctrlist.addHeader(textListOrderItem[language][1], "10%");//SKU
            ctrlist.addHeader("HS Code", "10%");//category
            if (typeOfBusiness.equals("0")) {
                ctrlist.addHeader(textListOrderItem[language][15], "10%");//Barcode
            }
            ctrlist.addHeader(textListOrderItem[language][2], "20%");//Nama Barang
            if (bEnableExpiredDate) {
                ctrlist.addHeader(textListOrderItem[language][3], "8%");//Kadaluarsa
            }
            if (typeOfBusinessDetail != 2) {
                ctrlist.addHeader(textListOrderItem[language][4], "5%");//Unit  
            }
            if (privShowQtyPrice) {
                ctrlist.addHeader(textListOrderItem[language][8], "5%");//Qty
                if (typeOfBusinessDetail == 2) {
                    ctrlist.addHeader(textListOrderItem[language][16], "5%");//Berat
                }
                if (syspropHargaBeli.equals("1")) {
                    if (typeOfBusinessDetail == 2 && (recItemType == Material.MATERIAL_TYPE_EMAS || recItemType == Material.MATERIAL_TYPE_EMAS_LANTAKAN)) {
                        ctrlist.addHeader("Harga Emas (Rp)", "8%");
                    } else {
                        ctrlist.addHeader(textListOrderItem[language][5], "8%");//Harga Beli
                    }
                }
                if (syspropDiscount1.equals("1")) {
                    ctrlist.addHeader(textListOrderItem[language][11], "5%");//Diskon1 %
                }
                if (syspropDiscount2.equals("1")) {
                    ctrlist.addHeader(textListOrderItem[language][12], "5%");//Diskon2 %
                }
                if (syspropDiscountNominal.equals("1")) {
                    ctrlist.addHeader(textListOrderItem[language][13], "8%");//Discount Nominal
                }
                if (syspropOngkosKirim.equals("1")) {
                    if (typeOfBusinessDetail == 2) {
                        ctrlist.addHeader("Ongkos / Batu (Rp)", "8%");//Ongkos / Batu
                    } else {
                        ctrlist.addHeader(textListOrderItem[language][6], "8%");//Ongkos Kirim
                    }
                }
                if (syspropTotalBeli.equals("1")) {
                    if (typeOfBusinessDetail == 2) {
                        if (recItemType == Material.MATERIAL_TYPE_BERLIAN) {
                            ctrlist.addHeader("Harga Pokok (Rp)", "10%");
                        } else if (recItemType == Material.MATERIAL_TYPE_EMAS || recItemType == Material.MATERIAL_TYPE_EMAS_LANTAKAN) {
                            ctrlist.addHeader("Total HP (Rp)", "10%");//Total HP
                        }
                    } else {
                        ctrlist.addHeader(textListOrderItem[language][9], "10%");//Total Beli
                    }
                }
                if (syspropHargaJual.equals("1")) {
                    ctrlist.addHeader("Harga Jual", "10%"); //Harga Jual
                }
            } else {
                ctrlist.addHeader(textListOrderItem[language][8], "9%");//Qty
            }
            if (syspropEtalase.equals("1")) {
                ctrlist.addHeader(textListOrderItem[language][20], "7%");//etalase
            }
            if (syspropColor.equals("1")) {
                ctrlist.addHeader(textListOrderItem[language][19], "7%");//warna
            }
            ctrlist.addHeader(textListOrderItem[language][17], "5%");//keterangan
            if (typeOfBusinessDetail == 2 && rec.getReceiveSource() == PstMatReceive.SOURCE_FROM_BUYBACK) {
                ctrlist.addHeader(textListOrderItem[language][18], "5%");//sorting
            }
            Vector listGroupMapping = PstMasterGroupMapping.list(0, 0, PstMasterGroupMapping.fieldNames[PstMasterGroupMapping.FLD_MODUL] + " = " + PstMasterGroupMapping.MODUL_RECEIVING, "");
            if (listGroupMapping.size() > 0) {
                for (int i = 0; i < listGroupMapping.size(); i++) {
                    MasterGroupMapping masterGroupMap = (MasterGroupMapping) listGroupMapping.get(i);
                    MasterGroup masterGroup = new MasterGroup();
                    try {
                        masterGroup = PstMasterGroup.fetchExc(masterGroupMap.getGroupId());
                    } catch (Exception exc) {
                    }
                    ctrlist.addHeader(masterGroup.getNamaGroup(), "7%");//
                }
            }
            ctrlist.addHeader(textListOrderItem[language][14], "1%");//hapus

            Vector lstData = ctrlist.getData();
            Vector lstLinkData = ctrlist.getLinkData();
            Vector rowx = new Vector(1, 1);
            ctrlist.reset();
            int index = -1;
            if (start < 0) {
                start = 0;
            }

            for (int i = 0; i < objectClass.size(); i++) {
                Vector temp = (Vector) objectClass.get(i);
                MatReceiveItem recItem = (MatReceiveItem) temp.get(0);
                Material mat = (Material) temp.get(1);
                Unit unit = (Unit) temp.get(2);
                //added by dewok 2018 for jewelry
                String itemName = SessMaterial.setItemNameForLitama(recItem.getMaterialId());

                Category cat = new Category();
                BillMain bm = new BillMain();
                try {
                    MatReceiveItem mri = PstMatReceiveItem.fetchExc(recItem.getOID());
                    if (mri.getCashBillMainId() != 0) {
                        bm = PstBillMain.fetchExc(mri.getCashBillMainId());
                    }
                    if (mat.getCategoryId() != 0) {
                        cat = PstCategory.fetchExc(mat.getCategoryId());
                    }
                } catch (Exception e) {
                }

                Ksg ksg = new Ksg();
                try {
                    ksg = PstKsg.fetchExc(recItem.getGondolaId());
                } catch (Exception exc) {

                }

                Color color = new Color();
                try {
                    color = PstColor.fetchExc(recItem.getColorId());
                } catch (Exception exc) {

                }

                rowx = new Vector();
                start = start + 1;
                double totalForwarderCost = recItem.getForwarderCost() * recItem.getQty();

                rowx.add("" + start + "");
                rowx.add("" + bm.getInvoiceNumber() + "");
                if ((privManageData || privShowQtyPrice) && statusDoc != I_DocStatus.DOCUMENT_STATUS_CLOSED) {
                    rowx.add("<a href=\"javascript:editItem('" + String.valueOf(recItem.getOID()) + "')\">" + mat.getSku() + "</a>");
                } else {
                    rowx.add(mat.getSku());
                }
                rowx.add("" + cat.getCode() + "");
                if (typeOfBusiness.equals("0")) {
                    rowx.add(mat.getBarCode());
                }
                if (typeOfBusinessDetail == 2) {
                    rowx.add(itemName);
                } else {
                    rowx.add(mat.getName());
                }
                if (bEnableExpiredDate) {
                    rowx.add("<div align=\"center\">" + Formater.formatDate(recItem.getExpiredDate(), "dd-MM-yyyy") + "</div>");
                }
                if (typeOfBusinessDetail != 2) {
                    rowx.add("<div align=\"center\">" + unit.getCode() + "</div>");
                }
                if (mat.getRequiredSerialNumber() == PstMaterial.REQUIRED) {
                    String where = PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_RECEIVE_MATERIAL_ITEM_ID] + "=" + recItem.getOID();
                    int cnt = PstReceiveStockCode.getCount(where);

                    double recQtyPerBuyUnit = recItem.getQty();
                    double qtyPerSellingUnit = PstUnit.getQtyPerBaseUnit(mat.getBuyUnitId(), mat.getDefaultStockUnitId());
                    double recQty = recQtyPerBuyUnit * qtyPerSellingUnit;
                    double max = recQty;
                    if (cnt < max) {
                        if (listError.size() == 0) {
                            listError.add("Silahkan cek :");
                        }
                        listError.add("" + listError.size() + ". Jumlah serial kode stok " + mat.getName() + " tidak sama dengan qty terima");
                    }

                    rowx.add("<div align=\"right\"><a href=\"javascript:gostock('" + String.valueOf(recItem.getOID()) + "')\">[ST.CD]</a> " + FRMHandler.userFormatStringDecimal(recItem.getQty()) + "</div>");

                } else {
                    if (typeOfBusinessDetail == 2) {
                        rowx.add("<div align=\"right\">" + String.format("%,.0f", recItem.getQty()) + "</div>");
                    } else {
                        rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(recItem.getQty()) + "</div>");
                    }
                }
                if (typeOfBusinessDetail == 2) {
                    rowx.add("<div align=\"right\">" + String.format("%,.3f", recItem.getBerat()) + "</div>");
                }
                if (privShowQtyPrice) {
                    if (syspropHargaBeli.equals("1")) {
                        if (typeOfBusinessDetail == 2) {
                            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(recItem.getCost()) + "</div>");
                        } else {
                            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(recItem.getCost()) + "</div>");
                        }
                    }
                    if (syspropDiscount1.equals("1")) {
                        rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(recItem.getDiscount()) + "</div>");
                    }
                    if (syspropDiscount2.equals("1")) {
                        rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(recItem.getDiscount2()) + "</div>");
                    }
                    if (syspropDiscountNominal.equals("1")) {
                        rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(recItem.getDiscNominal()) + "</div>");
                    }
                    if (syspropOngkosKirim.equals("1")) {
                        if (typeOfBusinessDetail == 2) {
                            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(recItem.getForwarderCost()) + "</div>");
                        } else {
                            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(recItem.getForwarderCost()) + "</div>");
                        }
                    }
                }
                if (syspropTotalBeli.equals("1")) {
                    if (typeOfBusinessDetail == 2) {
                        rowx.add("<div align=\"right\">" + String.format("%,.0f", recItem.getTotal() + totalForwarderCost) + "</div>");
                    } else {
                        rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(recItem.getTotal() + totalForwarderCost) + "</div>");
                    }
                }
                if (syspropHargaJual.equals("1")) {
                    double sellPrice = PstPriceTypeMapping.getSellPrice(recItem.getMaterialId(), PstPriceTypeMapping.getOidStandartRate(), PstPriceTypeMapping.getOidPriceType());
                    rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(sellPrice) + "</div>");
                }
                if (syspropEtalase.equals("1")) {
                    rowx.add("<div align=\"right\">" + ksg.getName() + "</div>");
                }
                if (syspropColor.equals("1")) {
                    rowx.add("<div align=\"right\">" + color.getColorName() + "</div>");
                }
                rowx.add("<div align=\"\">" + recItem.getRemark() + "</div>");
                if (typeOfBusinessDetail == 2 && rec.getReceiveSource() == PstMatReceive.SOURCE_FROM_BUYBACK) {
                    rowx.add("<div align=\"right\">" + PstMatReceiveItem.sortingKey[recItem.getSortStatus()] + "</div>");
                }
                if (listGroupMapping.size() > 0) {
                    for (int x = 0; x < listGroupMapping.size(); x++) {
                        MasterGroupMapping masterGroupMap = (MasterGroupMapping) listGroupMapping.get(x);
                        MasterGroup masterGroup = new MasterGroup();
                        try {
                            masterGroup = PstMasterGroup.fetchExc(masterGroupMap.getGroupId());
                        } catch (Exception exc) {
                        }
                        Vector vValue = new Vector(1, 1);
                        Vector vKey = new Vector(1, 1);

                        vValue.add("0");
                        vKey.add("-");

                        Vector listType = PstMasterType.list(0, 0,
                                PstMasterType.fieldNames[PstMasterType.FLD_TYPE_GROUP] + "=" + masterGroup.getTypeGroup(),
                                PstMasterType.fieldNames[PstMasterType.FLD_MASTER_NAME]);

                        long oidMapping = PstMaterialMappingType.getSelectedTypeId(masterGroup.getTypeGroup(), recItem.getOID());
                        MasterType masterType = new MasterType();
                        String typeName = "";
                        try {
                            masterType = PstMasterType.fetchExc(oidMapping);
                            typeName = masterType.getMasterName();
                        } catch (Exception exc) {
                            typeName = "-";
                        }
                        rowx.add("<div align=\"left\">" + typeName + "</div>");
                    }
                }
                if ((select_status != 5 && select_status != 2 && select_status != 7 && select_status != 1 && select_status != 2)) {
                    rowx.add(" <div align=\"center\"> <a href=\"javascript:cmdNewDelete('" + String.valueOf(recItem.getOID()) + "')\"><img src=" + approot + "/images/x3.png align=\"center\" ></a></div>");
                } else {
                    rowx.add("");
                }

                lstData.add(rowx);
            }
            //tambahan row untuk informasi total dan selisih
            if (typeOfBusinessDetail == 2) {
                double totalQty = 0;
                double totalBeratItem = 0;
                double totalHargaBeli = 0;
                double totalOngkir = 0;
                double totalHargaPokok = 0;
                String whereClauseItem = PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + " = " + rec.getOID();
                Vector<MatReceiveItem> listMatReceiveItem = PstMatReceiveItem.list(0, 0, whereClauseItem, null);
                for (MatReceiveItem mri : listMatReceiveItem) {
                    totalQty += mri.getQty();
                    totalBeratItem += mri.getBerat();
                    totalHargaBeli += mri.getCost();
                    totalOngkir += (mri.getForwarderCost() * mri.getQty());
                    totalHargaPokok += mri.getForwarderCost() + (mri.getTotal() * mri.getQty());
                }
                Vector rowAdditional = new Vector(1, 1);
                rowAdditional.add("");
                rowAdditional.add("<div align=\"right\"><b>Total :</b></div>"
                        + "<div align=\"right\"><b>Total Nota :</b></div>"
                        + "<div align=\"right\"><b>Selisih :</b></div>");
                rowAdditional.add("");
                rowAdditional.add("");
                rowAdditional.add("<div align=\"right\"><b>" + String.format("%,.0f", totalQty) + "</b></div>");
                rowAdditional.add("<div align=\"right\"><b>" + String.format("%,.3f", totalBeratItem) + "</b></div>"
                        + "<div align=\"right\"><b>" + String.format("%,.3f", rec.getBerat()) + "</b></div>"
                        + "<div align=\"right\"><b>" + String.format("%,.3f", (totalBeratItem - rec.getBerat())) + "</b></div>");
                if (syspropHargaBeli.equals("1")) {
                    rowAdditional.add("<div align=\"right\"><b>" + String.format("%,.0f", totalHargaBeli) + "</b></div>"
                            + "<div align=\"right\"><b>" + String.format("%,.0f", rec.getTotalHe()) + "</b></div>"
                            + "<div align=\"right\"><b>" + String.format("%,.0f", (totalHargaBeli - rec.getTotalHe())) + "</b></div>");
                }
                rowAdditional.add("<div align=\"right\"><b>" + String.format("%,.0f", totalOngkir) + "</b></div>"
                        + "<div align=\"right\"><b>" + String.format("%,.0f", rec.getTotalOngkos()) + "</b></div>"
                        + "<div align=\"right\"><b>" + String.format("%,.0f", (totalOngkir - rec.getTotalOngkos())) + "</b></div>");
                rowAdditional.add("<div align=\"right\"><b>" + String.format("%,.0f", totalHargaPokok) + "</b></div>"
                        + "<div align=\"right\"><b>" + String.format("%,.0f", rec.getTotalNota()) + "</b></div>"
                        + "<div align=\"right\"><b>" + String.format("%,.0f", totalHargaPokok - rec.getTotalNota()) + "</b></div>");
                rowAdditional.add("");
                if (typeOfBusinessDetail == 2 && rec.getReceiveSource() == PstMatReceive.SOURCE_FROM_BUYBACK) {
                    rowAdditional.add("");
                }
                rowAdditional.add("");
                lstData.add(rowAdditional);
            }
            result = ctrlist.draw();
        } else {
            result = "<div class=\"msginfo\">" + textListGlobal[language][7] + "</div>";
        }
        list.add(result);
        list.add(listError);
        return list;
    }

%>

<!-- Jsp Block -->
<%
    /**
     * get approval status for create document
     */
    I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();
    I_Approval i_approval = (I_Approval) Class.forName(approvalClassName).newInstance();
    I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();
    int docType = i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL, I_DocType.MAT_DOC_TYPE_LMRR);
%>

<%
    /**
     * get request data from current form
     */
    int iCommand = FRMQueryString.requestCommand(request);
    int prevCommand = FRMQueryString.requestInt(request, "prev_command");
    int startItem = FRMQueryString.requestInt(request, "start_item");
    int cmdItem = FRMQueryString.requestInt(request, "command_item");
    int appCommand = FRMQueryString.requestInt(request, "approval_command");
    long oidReceiveMaterial = FRMQueryString.requestLong(request, "hidden_receive_id");
    long oidForwarderInfo = FRMQueryString.requestLong(request, "hidden_forwarder_id");
    int iCommandPosting = FRMQueryString.requestInt(request, "iCommandPosting");
    int cekBeratStatus = FRMQueryString.requestInt(request, "cek_berat");

    String sEnableExpiredDate = PstSystemProperty.getValueByName("ENABLE_EXPIRED_DATE");
    bEnableExpiredDate = (sEnableExpiredDate != null && sEnableExpiredDate.equalsIgnoreCase("YES")) ? true : false;
    String syspropDiscount1 = PstSystemProperty.getValueByName("SHOW_DISCOUNT_1");
    String syspropDiscount2 = PstSystemProperty.getValueByName("SHOW_DISCOUNT_2");
    String syspropDiscountNominal = PstSystemProperty.getValueByName("SHOW_DISCOUNT_NOMINAL");
    String syspropOngkosKirim = PstSystemProperty.getValueByName("SHOW_ONGKOS_KIRIM");
    String syspropBonus = PstSystemProperty.getValueByName("SHOW_BONUS");
    String syspropHargaBeli = PstSystemProperty.getValueByName("SHOW_HARGA_BELI");
    String syspropRecTypeDefault = PstSystemProperty.getValueByName("DEFAULT_RECEIVE_TYPE");
    String syspropColor = PstSystemProperty.getValueByName("SHOW_COLOR");
    String syspropEtalase = PstSystemProperty.getValueByName("SHOW_ETALASE");
    String syspropTotalBeli = PstSystemProperty.getValueByName("SHOW_TOTAL_BELI");
    String syspropHargaJual = PstSystemProperty.getValueByName("SHOW_HARGA_JUAL");

    //include ppn
    int includePpn = FRMQueryString.requestInt(request, "include_ppn");
    //0= not include
    //1= include
    //int includePpn = Integer.parseInt(PstSystemProperty.getValueByName("INCLUDE_PPN_MASUKAN"));
    double defaultPpn = Double.parseDouble(PstSystemProperty.getValueByName("POS_PPN_DEFAULT"));

    //adding currency id
    long oidCurrencyRec = FRMQueryString.requestLong(request, "hidden_currency_id");

    /**
     * initialization of some identifier
     */
    String errMsg = "";
    int iErrCode = FRMMessage.ERR_NONE;

    /**
     * purchasing ret code and title
     */
    String recCode = "";//i_pstDocType.getDocCode(docType);
    String recTitle = textListGlobal[SESS_LANGUAGE][0];//"Terima Barang";//i_pstDocType.getDocTitle(docType);
    String recItemTitle = recTitle + " Item";

    /**
     * action process
     */
    ControlLine ctrLine = new ControlLine();
    CtrlMatReceive ctrlMatReceive = new CtrlMatReceive(request);
    iErrCode = ctrlMatReceive.action(iCommand, oidReceiveMaterial, userName, userId, request);
    FrmMatReceive frmrec = ctrlMatReceive.getForm();
    MatReceive rec = ctrlMatReceive.getMatReceive();
    errMsg = ctrlMatReceive.getMessage();

    //posting
    //proses posting stock
    int typeOfBussiness = Integer.parseInt(PstSystemProperty.getValueByName("TYPE_OF_BUSINESS"));
    //typer reail
    if (typeOfBussiness == I_ApplicationType.APPLICATION_DISTRIBUTIONS) {
        SessPosting sessPosting = new SessPosting();
        switch (iCommandPosting) {
            case Command.POSTING:
                boolean isOKP = sessPosting.postedQtyReceiveOnlyDoc(oidReceiveMaterial);
                if (isOKP) {
                    rec.setReceiveStatus(I_DocStatus.DOCUMENT_STATUS_APPROVED);
                }
                break;
            case Command.REPOSTING:
                boolean isOK = sessPosting.postedValueReceiveOnlyDoc(oidReceiveMaterial);
                if (isOK) {
                    rec.setReceiveStatus(I_DocStatus.DOCUMENT_STATUS_CLOSED);
                }
                break;

            default:
            // break;
        }
        //maka statusnya final = posting value
    } else {
        //type kecuali retail, klo final langsung posting
        if (iCommandPosting == Command.POSTING) {
            SessPosting sessPosting = new SessPosting();
            boolean isOK = sessPosting.postedReceiveDoc(oidReceiveMaterial, userName, userId);
            if (isOK) {
                rec.setReceiveStatus(I_DocStatus.DOCUMENT_STATUS_CLOSED);
            }
        }
    }

    /**
     * generate code of current currency
     */
    String priceCode = "Rp.";

    /**
     * list purchase order item
     */
    oidCurrencyRec = rec.getCurrencyId();
    oidReceiveMaterial = rec.getOID();
    int recordToGetItem = 0;
    String whereClauseItem = "" + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + "=" + oidReceiveMaterial
            + " AND " + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_BONUS] + "=0";

    String orderClauseItem = " RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID];
    if (typeOfBusinessDetail == 2) {
        orderClauseItem = " RIGHT(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + ",7) ASC";
    }
    int vectSizeItem = PstMatReceiveItem.getCount(whereClauseItem);

    Vector listMatReceiveItem = new Vector();

    if (rec != null && rec.getOID() != 0) {
        listMatReceiveItem = PstMatReceiveItem.listVectorRecItemComplete(startItem, recordToGetItem, whereClauseItem, orderClauseItem);
        vectSizeItem = listMatReceiveItem.size();
    }

    /**
     * get total biaya forwarder
     */
    double totalForwarderCost = SessForwarderInfo.getTotalCost(oidReceiveMaterial);

    //update opie-eyek 20131205
    /**
     * check if document may modified or not
     */
    String enableInput = "";
    boolean privManageData = true;
    if (rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_DRAFT) {
        privManageData = false;
        enableInput = "readonly";
    }

    Vector list = drawListRecItem(SESS_LANGUAGE, listMatReceiveItem, startItem, privManageData, privShowQtyPrice, rec.getReceiveStatus(), approot, rec.getReceiveStatus(), typeOfBusiness, syspropDiscount1, syspropDiscount2, syspropDiscountNominal, syspropOngkosKirim, syspropBonus, syspropHargaBeli, typeOfBusinessDetail, rec.getReceiveItemType(), rec, syspropColor, syspropEtalase, syspropTotalBeli, syspropHargaJual);
    Vector listError = (Vector) list.get(1);

%>

<%    if (iCommand == Command.DELETE && iErrCode == 0) {
%>
<jsp:forward page="src_receive_back_material.jsp">
    <jsp:param name="command" value="<%=Command.FIRST%>"/>
</jsp:forward>
<%
    }
%>
<!-- End of Jsp Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
    <head>
        <!-- #BeginEditable "doctitle" -->
        <title>Dimata - ProChain POS</title>
        <script language="JavaScript">
            var recStatus = "<%=rec.getReceiveStatus()%>";
            var posted = "<%=I_DocStatus.DOCUMENT_STATUS_POSTED%>";
            var closed = "<%=I_DocStatus.DOCUMENT_STATUS_CLOSED%>";

            function cmdEdit(oid) {
                document.frm_recmaterial.command.value = "<%=Command.EDIT%>";
                document.frm_recmaterial.prev_command.value = "<%=prevCommand%>";
                document.frm_recmaterial.action = "receive_back_material.jsp";
                document.frm_recmaterial.submit();
            }

            function compare() {
                var dt = document.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_DATE]%>_dy.value;
                var mn = document.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_DATE]%>_mn.value;
                var yy = document.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_DATE]%>_yr.value;
                var dt = new Date(yy, mn - 1, dt);
                var bool = new Boolean(compareDate(dt));
                return bool;
            }

            function cmdNewDelete(oid) {
                var msg;
                msg = "<%=textDelete[SESS_LANGUAGE][0]%>";
                var agree = confirm(msg);
                if (agree)
                    return cmdConfirmDeleteItem(oid);
                else
                    return cmdEdit(oid);
            }

            function cmdConfirmDeleteItem(oidReceiveMaterialItem) {
                document.frm_recmaterial.hidden_receive_item_id.value = oidReceiveMaterialItem;
                document.frm_recmaterial.command.value = "<%=Command.DELETE%>";
                document.frm_recmaterial.prev_command.value = "<%=prevCommand%>";
                document.frm_recmaterial.approval_command.value = "<%=Command.DELETE%>";
                document.frm_recmaterial.action = "receive_back_material_item.jsp";
                document.frm_recmaterial.submit();
            }

            function cmdSave() {
                if ((recStatus !== posted) && (recStatus !== closed)) {
                    var statusDoc = document.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_STATUS]%>.value;
                    if ("<%=typeOfBusiness%>" == 3) {
                        if (statusDoc == "<%=I_DocStatus.DOCUMENT_STATUS_APPROVED%>" && <%=privApprovalFinal%> == false) {
                            var conf = confirm("<%=textPosting[SESS_LANGUAGE][0]%>");
                            if (conf) {
                                document.frm_recmaterial.command.value = "<%=Command.SAVE%>";
                                document.frm_recmaterial.iCommandPosting.value = "<%=Command.POSTING%>";
                                document.frm_recmaterial.prev_command.value = "<%=prevCommand%>";
                                document.frm_recmaterial.action = "receive_back_material.jsp";
                                if (compare() == true)
                                    document.frm_recmaterial.submit();
                            }
                        } else if (statusDoc == "<%=I_DocStatus.DOCUMENT_STATUS_FINAL%>") {
                            var conf = confirm("<%=textPosting[SESS_LANGUAGE][1]%>");
                            if (conf) {
                                document.frm_recmaterial.command.value = "<%=Command.SAVE%>";
                                document.frm_recmaterial.iCommandPosting.value = "<%=Command.REPOSTING%>";
                                document.frm_recmaterial.prev_command.value = "<%=prevCommand%>";
                                document.frm_recmaterial.action = "receive_back_material.jsp";
                                if (compare() == true)
                                    document.frm_recmaterial.submit();
                            }
                        } else {
                            document.frm_recmaterial.command.value = "<%=Command.SAVE%>";
                            document.frm_recmaterial.prev_command.value = "<%=prevCommand%>";
                            document.frm_recmaterial.action = "receive_back_material.jsp";
                            if (compare() == true)
                                document.frm_recmaterial.submit();
                        }
                    } else {
                        if (statusDoc == "<%=I_DocStatus.DOCUMENT_STATUS_FINAL%>") {
                            var conf = confirm("<%=textPosting[SESS_LANGUAGE][0]%>");
                            if (conf) {
                                document.frm_recmaterial.command.value = "<%=Command.SAVE%>";
                                document.frm_recmaterial.prev_command.value = "<%=prevCommand%>";
                                document.frm_recmaterial.iCommandPosting.value = "<%=Command.POSTING%>";
                                document.frm_recmaterial.action = "receive_back_material.jsp";
                                if (compare() == true)
                                    document.frm_recmaterial.submit();
                            }
                        } else {
                            document.frm_recmaterial.command.value = "<%=Command.SAVE%>";
                            document.frm_recmaterial.prev_command.value = "<%=prevCommand%>";
                            document.frm_recmaterial.action = "receive_back_material.jsp";
                            if (compare() == true)
                                document.frm_recmaterial.submit();
                        }
                    }
                } else {
                    alert("Document has been <%=I_DocStatus.fieldDocumentStatus[rec.getReceiveStatus()]%> !!!");
                }
            }

            function cmdAsk(oid) {
                if ((recStatus !== posted) && (recStatus !== closed)) {
                    document.frm_recmaterial.command.value = "<%=Command.ASK%>";
                    document.frm_recmaterial.prev_command.value = "<%=prevCommand%>";
                    document.frm_recmaterial.action = "receive_back_material.jsp";
                    document.frm_recmaterial.submit();
                } else {
                    alert("Document has been <%=I_DocStatus.fieldDocumentStatus[rec.getReceiveStatus()]%> !!!");
                }
            }

            function gostock(oid) {
                document.frm_recmaterial.command.value = "<%=Command.EDIT%>";
                document.frm_recmaterial.rec_type.value = 2;
                document.frm_recmaterial.type_doc.value = 2;
                document.frm_recmaterial.hidden_receive_item_id.value = oid;
                document.frm_recmaterial.action = "rec_wh_stockcode.jsp";
                document.frm_recmaterial.submit();
            }

            function cmdCancel() {
                document.frm_recmaterial.command.value = "<%=Command.CANCEL%>";
                document.frm_recmaterial.prev_command.value = "<%=prevCommand%>";
                document.frm_recmaterial.action = "receive_back_material.jsp";
                document.frm_recmaterial.submit();
            }

            function cmdConfirmDelete(oid) {
                document.frm_recmaterial.command.value = "<%=Command.DELETE%>";
                document.frm_recmaterial.prev_command.value = "<%=prevCommand%>";
                document.frm_recmaterial.approval_command.value = "<%=Command.DELETE%>";
                document.frm_recmaterial.action = "receive_back_material.jsp";
                document.frm_recmaterial.submit();
            }

            function cmdBack() {
                document.frm_recmaterial.command.value = "<%=Command.FIRST%>";
                document.frm_recmaterial.prev_command.value = "<%=prevCommand%>";
                document.frm_recmaterial.action = "src_receive_back_material.jsp";
                document.frm_recmaterial.submit();
            }

            function printExcel() {
                var typePrint = document.frm_recmaterial.type_print_tranfer.value;
                window.open("receive_wh_supp_material_print_form.jsp?printExcel=1&hidden_receive_id=<%=oidReceiveMaterial%>&command=<%=Command.EDIT%>&type_print_tranfer=" + typePrint + "", "receivereport", "scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
            }

            function printForm() {
                var typePrint = document.frm_recmaterial.type_print_tranfer.value;
                window.open("print_out_receive_back_material.jsp?hidden_receive_id=<%=oidReceiveMaterial%>&command=<%=Command.LIST%>&type_print_tranfer=" + typePrint + "", "receivereport");
            }

            function PostingStock() {
                var conf = confirm("<%=textPosting[SESS_LANGUAGE][0]%>");
                if (conf) {
                    document.frm_recmaterial.command.value = "<%=Command.POSTING%>";
                    document.frm_recmaterial.prev_command.value = "<%=prevCommand%>";
                    document.frm_recmaterial.action = "receive_back_material.jsp";
                    document.frm_recmaterial.submit();
                }
            }

            function PostingCostPrice() {
                var conf = confirm("<%=textPosting[SESS_LANGUAGE][1]%>");
                if (conf) {
                    document.frm_recmaterial.command.value = "<%=Command.REPOSTING%>";
                    document.frm_recmaterial.prev_command.value = "<%=prevCommand%>";
                    document.frm_recmaterial.action = "receive_back_material.jsp";
                    document.frm_recmaterial.submit();
                }
            }

            function changeCurrency(value) {
                var oidCurrencyId = value;
                checkAjax(oidCurrencyId);
            }

            function checkAjax(oidCurrencyId) {
                $.ajax({
                    url: "<%=approot%>/servlet/com.dimata.posbo.ajax.CurrentDailyRateRunning?FRM_FIELD_RATE=" + oidCurrencyId + "&typeCheckCurrency=1",
                    type: "POST",
                    async: false,
                    success: function (data) {
                        document.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_TRANS_RATE]%>.value = data;
                        //document.frm_purchaseorder.exchangeRate.value=data;
                    }
                });
            }

            //------------------------- START JAVASCRIPT FUNCTION FOR PO ITEM -----------------------

            function addItem() {
                if ((recStatus !== posted) && (recStatus !== closed)) {
                    document.frm_recmaterial.command.value = "<%=Command.ADD%>";
                    document.frm_recmaterial.action = "receive_back_material_item.jsp";
                    if (compareDateForAdd() == true) {
                        document.frm_recmaterial.submit();
                    }
                } else {
                    alert("Document has been <%=I_DocStatus.fieldDocumentStatus[rec.getReceiveStatus()]%> !!!");
                }
            }

            function editItem(oid) {
                if ((recStatus !== posted) && (recStatus !== closed)) {
                    document.frm_recmaterial.command.value = "<%=Command.EDIT%>";
                    document.frm_recmaterial.hidden_receive_item_id.value = oid;
                    document.frm_recmaterial.action = "receive_back_material_item.jsp";
                    document.frm_recmaterial.submit();
                } else {
                    alert("Document has been <%=I_DocStatus.fieldDocumentStatus[rec.getReceiveStatus()]%> !!!");
                }
            }

            function itemList(comm) {
                document.frm_recmaterial.command.value = comm;
                document.frm_recmaterial.prev_command.value = comm;
                document.frm_recmaterial.action = "receive_back_material.jsp";
                document.frm_recmaterial.submit();
            }

            //------------------------- START JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------

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

            //------------------------- END JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------

            function MM_findObj(n, d) { //v4.01
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
                if (!x && d.getElementById)
                    x = d.getElementById(n);
                return x;
            }

            function viewHistoryTable() {
                var strvalue = "../../../main/historypo.jsp?command=<%=Command.FIRST%>" +
                        "&oidDocHistory=<%=oidReceiveMaterial%>";
                window.open(strvalue, "material", "height=600,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
            }

            function formApproval(recMatId) {
                var winApproval = window.open("receive_approval_form.jsp?rec_mat_id=" + recMatId, "material", "height=300,width=400,left=500,top=100,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes,resizing=false");
                if (window.focus) {
                    winApproval.focus();
                }
            }
        </script>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
        <%if (menuUsed == MENU_ICON) {%>
        <link href="../../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
        <%}%>
        <link rel="stylesheet" href="../../../styles/main.css" type="text/css">
        <script src="../../../styles/jquery.min.js"></script>
        <link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
        <script>
            $(document).ready(function () {

                $('#tipeItemPenerimaan').change(function () {
                    var val = $(this).val();
                    if (val === "<%=Material.MATERIAL_TYPE_EMAS%>" || val === "<%=Material.MATERIAL_TYPE_EMAS_LANTAKAN%>") {
                        $('.tipe_emas').removeAttr('hidden');
                        $('#totalNotaTitle').html("Total HE (Rp)");
                    } else {
                        $('.tipe_emas').attr({'hidden': true});
                        $('#totalNotaTitle').html("Total Beli (Rp)");
                    }
                });

                $('#radioBeli').click(function () {
                    var command = "<%=iCommand%>";
                    var status = "";
                    if (command === "<%=Command.ADD%>") {
                        status = $('#optionAdd').html();
                    } else if (command === "<%=Command.EDIT%>") {
                        status = $('#optionBeli').html();
                    }
                    $('.comboStatus').html(status);
                });

                $('#radioTitip').click(function () {
                    var command = "<%=iCommand%>";
                    var status = "";
                    if (command === "<%=Command.ADD%>") {
                        status = $('#optionAdd').html();
                    } else if (command === "<%=Command.EDIT%>") {
                        status = $('#optionTitip').html();
                    }
                    $('.comboStatus').html(status);
                });

                $('.countTotalHe').on('keyup', function (evt) {
                    var jenisItem = $('#tipeItemPenerimaan').val();
                    if (jenisItem === "<%=Material.MATERIAL_TYPE_EMAS%>" || jenisItem === "<%=Material.MATERIAL_TYPE_EMAS_LANTAKAN%>") {
                        //total berat 24k                
                        var beratNota = $('#recBeratNota').val().replace(/,/g, '');
                        var nilaiTukar = $('#recNilaiTukar').val().replace(/,/g, '');
                        var berat24k = (nilaiTukar / 100) * beratNota;
                        $('#recBerat24k').val(berat24k.toFixed(3));
                        //total he
                        var hel = $('#recHel').val().replace(/,/g, '') * 1;
                        var totalHe = (nilaiTukar / 100) * beratNota * hel;
                        if (evt.which != 190 && evt.which != 188) {//not a fullstop
                            $('#recHel').val(hel.toLocaleString(undefined, {minimumFractionDigits: 0, maximumFractionDigits: 2}));
                        }
                        $('#totalHe').val(totalHe.toLocaleString());
                        //total nota
                        var totalOngkos = $('#totalOngkos').val().replace(/,/g, '') * 1;
                        var totalNota = totalOngkos + totalHe;
                        $('#totalOngkos').val(totalOngkos.toLocaleString());
                        $('#totalNota').val(totalNota.toLocaleString());
                    } else {

                    }
                });

                $('#totalHe').keyup(function () {
                    var totalOngkos = $('#totalOngkos').val().replace(/,/g, '') * 1;
                    var totalHe = $('#totalHe').val().replace(/,/g, '') * 1;
                    var totalNota = totalOngkos + totalHe;
                    //$('#totalHe').val(totalHe.toLocaleString());
                    $('#totalOngkos').val(totalOngkos.toLocaleString());
                    $('#totalNota').val(totalNota.toLocaleString());
                });

                $('#totalOngkos').keyup(function () {
                    var totalOngkos = $('#totalOngkos').val().replace(/,/g, '');
                    var totalHe = $('#totalHe').val().replace(/,/g, '') * 1;
                    var totalNota = 1 * totalOngkos + totalHe;
                    var last = totalOngkos.slice(totalOngkos.length - 1);
                    var show = (last === ".") ? Number(totalOngkos).toLocaleString() + "." : Number(totalOngkos).toLocaleString();
                    $('#totalOngkos').val(show);
                    $('#totalNota').val(Number(totalNota).toLocaleString());
                });

            });
        </script>
    </head> 

    <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
        <table width="100%" border="0" cellspacing="0" cellpadding="0" >
            <%if (menuUsed == MENU_PER_TRANS) {%>
            <tr>
                <td height="25" ID="TOPTITLE">
                    <%@ include file = "../../../main/header.jsp" %>
                </td>
            </tr>
            <tr>
                <td height="20" ID="MAINMENU">
                    <%@ include file = "../../../main/mnmain.jsp" %>
                </td>
            </tr>
            <%} else {%>
            <tr bgcolor="#FFFFFF">
                <td height="10" ID="MAINMENU">
                    <%@include file="../../../styletemplate/template_header.jsp" %>
                </td>
            </tr>
            <%}%>
            <tr> 
                <td class="mainheader">
                    <%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][11]%> &gt; <%=textListGlobal[SESS_LANGUAGE][4]%>
                </td>
            </tr>
        </table>
                
        <form name="frm_recmaterial" method="post" action="">
            <input type="hidden" name="command" value="">
            <input type="hidden" name="prev_command" value="<%=prevCommand%>">
            <input type="hidden" name="start_item" value="<%=startItem%>">
            <input type="hidden" name="rec_type" value="">
            <input type="hidden" name="type_doc" value="">
            <input type="hidden" name="command_item" value="<%=cmdItem%>">
            <input type="hidden" name="approval_command" value="<%=appCommand%>">
            <input type="hidden" name="hidden_receive_id" value="<%=oidReceiveMaterial%>">
            <input type="hidden" name="hidden_forwarder_id" value="<%=oidForwarderInfo%>">
            <input type="hidden" name="hidden_receive_item_id" value="">
            <input type="hidden" name="hidden_currency_id" value="<%=rec.getCurrencyId()%>">
            <input type="hidden" name="hidden_currency_id" value="<%=oidCurrencyRec%>">
            <input type="hidden" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_REC_CODE]%>" value="<%=rec.getRecCode()%>">
            <input type="hidden" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_LOCATION_TYPE]%>" value="<%=PstLocation.TYPE_LOCATION_WAREHOUSE%>">
            <input type="hidden" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_SOURCE]%>" value="<%=PstMatReceive.SOURCE_FROM_RECEIVE_BACK%>">
            <input type="hidden" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_DISCOUNT]%>" value="<%=rec.getDiscount()%>">
            <input type="hidden" name="iCommandPosting" value="">
            
            <table width="100%" border="0" cellpadding="1">  
                <tr>
                    <td width="" align="left"><%=textListOrderHeader[SESS_LANGUAGE][0]%></td>
                    <%String kode = "";
                        if (rec.getRecCode() == "") {
                            kode = "Kode Otomatis";
                        } else {
                            kode = rec.getRecCode();
                        }%>
                    <td width="" align="left">: <b><%=kode%></b></td>
                    <td width="" align="left"><%=textListOrderHeader[SESS_LANGUAGE][1]%></td>
                    <td width="" align="left"> :
                        <%
                            Vector val_locationid = new Vector(1, 1);
                            Vector key_locationid = new Vector(1, 1);
                            //add opie-eyek
                            //algoritma : di check di sistem usernya dimana saja user tsb bisa melakukan create document
                            String whereClause = " (" + PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE
                                    + " OR " + PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE + ")";
                            whereClause += " AND " + PstDataCustom.whereLocReportView(userId, "user_create_document_location");
                            Vector vt_loc = PstLocation.listLocationCreateDocument(0, 0, whereClause, "");

                            for (int d = 0; d < vt_loc.size(); d++) {
                                Location loc = (Location) vt_loc.get(d);
                                val_locationid.add("" + loc.getOID() + "");
                                key_locationid.add(loc.getName());
                            }
                            String select_locationid = "" + rec.getLocationId(); //selected on combo box
                        %>
                        <%=ControlCombo.draw(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_LOCATION_ID], null, select_locationid, val_locationid, key_locationid, "tabindex=\"1\"", "formElemen")%>
                    </td>

                    <td><%=textListOrderHeader[SESS_LANGUAGE][4]%></td>
                    <td>:</td>
                    <td>
                        <select id="optionAdd" hidden="">
                            <option value="<%=I_DocStatus.DOCUMENT_STATUS_DRAFT%>"><%=I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]%></option>                          
                        </select>

                        <select id="optionBeli" hidden="">
                            <option value="<%=I_DocStatus.DOCUMENT_STATUS_DRAFT%>"><%=I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]%></option>
                            <option value="<%=I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED%>"><%=I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED]%></option>
                            <option value="<%=I_DocStatus.DOCUMENT_STATUS_APPROVED%>"><%=I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_APPROVED]%></option>
                            <option value="<%=I_DocStatus.DOCUMENT_STATUS_FINAL%>"><%=I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]%></option>
                        </select>

                        <select id="optionTitip" hidden="">
                            <option value="<%=I_DocStatus.DOCUMENT_STATUS_DRAFT%>"><%=I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]%></option>
                            <option value="<%=I_DocStatus.DOCUMENT_STATUS_FINAL%>"><%=I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]%></option>
                        </select>

                        <%
                            Vector val_status = new Vector(1, 1);
                            Vector key_status = new Vector(1, 1);

                            if (typeOfBusiness.equals("3")) {
                                if (rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_APPROVED) {
                                    val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_DRAFT));
                                    key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]);

                                    if (listMatReceiveItem.size() > 0 && rec.getReceiveType() == 0) {
                                        val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED));
                                        key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED]);
                                    }
                                }
                            } else {
                                val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_DRAFT));
                                key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]);
                                //appObjCode
                                if (listMatReceiveItem.size() > 0 && rec.getReceiveType() == 0) {
                                    val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED));
                                    key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED]);
                                }
                            }
                            //appObjCode
                            // update opie-eyek 19022013
                            // user bisa memfinalkan purchase request  jika  :
                            // 1. punya approve document pr = true
                            // 2. lokasi sumber (lokasi asal)  ada di lokasi-lokasi yg diassign ke user
                            boolean locationAssign = false;
                            locationAssign = PstDataCustom.checkDataCustom(userId, "user_location_map", rec.getLocationId());
                            if (listMatReceiveItem.size() > 0 && listError.size() == 0 && privApprovalApprove == true && rec.getReceiveType() == 0) {
                                val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_APPROVED));
                                key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_APPROVED]);
                            }
                            if (listMatReceiveItem.size() > 0 && listError.size() == 0 && privApprovalFinal == true && locationAssign == true) {
                                val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_FINAL));
                                key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);
                            }
                            String select_status = "" + rec.getReceiveStatus();
                            if (rec.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_CLOSED) {
                                out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_CLOSED]);
                            } else if (rec.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_POSTED) {
                                out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_POSTED]);
                            } else {
                                out.println(ControlCombo.draw(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_STATUS], null, select_status, val_status, key_status, "tabindex=\"4\"", "formElemen comboStatus"));
                            }
                        %>
                    </td>
                </tr>
                <tr>
                    <td width="" align="left"><%=textListOrderHeader[SESS_LANGUAGE][2]%></td>
                    <td width="" align="left">: <%=ControlDate.drawDateWithStyle(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_DATE], (rec.getReceiveDate() == null) ? new Date() : rec.getReceiveDate(), 1, -5, "formElemen", "")%></td>

                    <td>Jenis Dokumen</td>
                    <td>:
                        <%
                            String jenDoc = "";
                            if (iCommand == Command.ADD) {
                                jenDoc = "PPKTBB";
                            } else {
                                jenDoc = rec.getJenisDokumen();
                            }
                        %>
                        <input name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_JENIS_DOKUMEN]%>" type="text" class="formElemen" size="10" value="<%=jenDoc%>">
                    </td>

                    <td width="" align="left"><%=textListOrderHeader[SESS_LANGUAGE][5]%></td>
                    <td>:</td>
                    <td width=""><textarea name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_REMARK]%>" class="formElemen" wrap="VIRTUAL" rows="2" tabindex="6"><%=rec.getRemark()%></textarea></td>
                </tr>
                <tr>
                    <td width="" align="left"><%=textListOrderHeader[SESS_LANGUAGE][8]%></td>
                    <td width="" align="left">: <%=ControlDate.drawTimeSec(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_DATE], (rec.getReceiveDate() == null) ? new Date() : rec.getReceiveDate(), "formElemen")%></td>

                    <td><%=textListOrderHeader[SESS_LANGUAGE][9]%></td>
                    <td>:
                        <%
                            long oidCurrencyx = 0;
                            double resultKonversi = 0.0;

                            Vector listCurr = PstCurrencyType.list(0, 0, PstCurrencyType.fieldNames[PstCurrencyType.FLD_INCLUDE_IN_PROCESS] + "=" + PstCurrencyType.INCLUDE, "");
                            Vector vectCurrVal = new Vector(1, 1);
                            Vector vectCurrKey = new Vector(1, 1);
                            for (int i = 0; i < listCurr.size(); i++) {
                                CurrencyType currencyType = (CurrencyType) listCurr.get(i);
                                if (i == 0) {
                                    oidCurrencyx = currencyType.getOID();
                                }
                                vectCurrKey.add(currencyType.getCode());
                                vectCurrVal.add("" + currencyType.getOID());
                            }
                            if ((vectSizeItem == 0) || (rec.getCurrencyId() == 0)) {
                                resultKonversi = PstDailyRate.getCurrentDailyRateSales(oidCurrencyx);
                                out.println(ControlCombo.draw(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_CURRENCY_ID], "formElemen", null, "" + rec.getCurrencyId(), vectCurrVal, vectCurrKey, "onChange=\"javascript:changeCurrency(this.value)\""));
                            } else {
                                out.println(ControlCombo.draw(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_CURRENCY_ID] + "_sel", "formElemen", null, "" + rec.getCurrencyId(), vectCurrVal, vectCurrKey, "disabled"));
                                out.println("<input type=\"hidden\" name=\"" + FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_CURRENCY_ID] + "\" value=\"" + rec.getCurrencyId() + "\">");
                            }
                        %>                                                                             
                    </td>                     
                    <td><%=textListOrderHeader[SESS_LANGUAGE][16]%></td>
                    <td>:</td>
                    <td>
                        <input name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_TRANS_RATE]%>" type="text" class="formElemen" size="10" value="<%=rec.getTransRate() != 0 ? rec.getTransRate() : resultKonversi%>" <%=enableInput%>>
                    </td>
                </tr>
                <tr>
                    <td>Tanggal BC</td>
                    <td width="" align="left">: <%=ControlDate.drawDateWithStyle(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_TANGGAL_BC], (rec.getTglBc() == null) ? new Date() : rec.getTglBc(), 1, -5, "formElemen", "")%></td>
                    <td width="" align="left">Nomor BC</td>
                    <td width="" align="left">:
                        <input name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_NOMOR_BC]%>" type="text" class="formElemen" size="10" value="<%=rec.getNomorBc()%>">
                    </td>
                </tr>
            </table>
                                    
            <table width="100%" height="10%" border="0" cellspacing="0" cellpadding="0">
                <%if(iCommand == Command.SAVE && frmrec.errorSize() == 0) {%>
                <tr>
                    <td class="msginfo"><%=errMsg%></td>
                </tr>
                <%}%>
                <tr>
                    <td width="100%">
                        <%
                            ctrLine.setLocationImg(approot + "/images");

                            // set image alternative caption
                            ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE, recTitle, ctrLine.CMD_SAVE, true));
                            ctrLine.setBackImageAlt(SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE, recTitle, ctrLine.CMD_BACK, true) : ctrLine.getCommand(SESS_LANGUAGE, recTitle, ctrLine.CMD_BACK, true) + " List");
                            ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE, recTitle, ctrLine.CMD_ASK, true));
                            ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE, recTitle, ctrLine.CMD_CANCEL, false));

                            ctrLine.initDefault();
                            ctrLine.setTableWidth("100%");
                            String scomDel = "javascript:cmdAsk('" + oidReceiveMaterial + "')";
                            String sconDelCom = "javascript:cmdConfirmDelete('" + oidReceiveMaterial + "')";
                            String scancel = "javascript:cmdEdit('" + oidReceiveMaterial + "')";
                            ctrLine.setCommandStyle("command");
                            ctrLine.setColCommStyle("command");

                            // set command caption
                            ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE, recTitle, ctrLine.CMD_SAVE, true));
                            ctrLine.setBackCaption(SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE, recTitle, ctrLine.CMD_BACK, true) : ctrLine.getCommand(SESS_LANGUAGE, recTitle, ctrLine.CMD_BACK, true) + " List");
                            ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE, recTitle, ctrLine.CMD_ASK, true));
                            ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE, recTitle, ctrLine.CMD_DELETE, true));
                            ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE, recTitle, ctrLine.CMD_CANCEL, false));

                            if (privDelete && privManageData) {
                                ctrLine.setConfirmDelCommand(sconDelCom);
                                ctrLine.setDeleteCommand(scomDel);
                                ctrLine.setEditCommand(scancel);
                            } else {
                                ctrLine.setConfirmDelCaption("");
                                ctrLine.setDeleteCaption("");
                                ctrLine.setEditCaption("");
                            }

                            if (privAdd == false && privUpdate == false) {
                                ctrLine.setSaveCaption("");
                            }

                            if (typeOfBusiness.equals("3")) {
                                if (privApprovalFinal == true && (rec.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT || rec.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED)) {
                                    ctrLine.setSaveCaption("");
                                    ctrLine.setDeleteCaption("");
                                }
                                if (privApprovalApprove == true && rec.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_APPROVED && privApprovalFinal == false) {
                                    ctrLine.setSaveCaption("");
                                }
                            }

                            if (privAdd == false || rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_DRAFT) {
                                ctrLine.setAddCaption("");
                            }

                            if (rec.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_FINAL || rec.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_CLOSED) {
                                ctrLine.setAddCaption("");
                                ctrLine.setSaveCaption("");
                            }

                            if (iCommand == Command.SAVE && iErrCode == 0) {
                                iCommand = Command.EDIT;
                            }
                        %>
                        <%=ctrLine.drawImage(iCommand, frmrec.errorSize(), errMsg)%>
                    </td>
                </tr>
            </table>
                    
            <%if (oidReceiveMaterial != 0) {%>
            
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr align="left" valign="top">
                    <td height="22" valign="middle" colspan="3">
                        <%=list.get(0)%>
                    </td>
                </tr>
                <%if(listError.size() > 0){%>
                <tr align="left" valign="top">
                    <td height="22" colspan="3" valign="middle" class="errfont">
                        <%
                            for (int k = 0; k < listError.size(); k++) {
                                if (k == 0) {
                                    out.println(listError.get(k) + "<br>");
                                } else {
                                    out.println("&nbsp;&nbsp;&nbsp;" + listError.get(k) + "<br>");
                                }
                            }
                        %>                        
                    </td>
                </tr>
                <%}%>
                <%if (list.size() > 0) {%>
                <tr align="left" valign="top">
                    <td height="8" align="left" colspan="3" class="command">
                        <span class="command">
                            <%
                                if (cmdItem != Command.FIRST && cmdItem != Command.PREV && cmdItem != Command.NEXT && cmdItem != Command.LAST) {
                                    cmdItem = Command.FIRST;
                                }
                                ctrLine.setLocationImg(approot + "/images");
                                ctrLine.initDefault();
                                ctrLine.setImageListName(approot + "/images", "item");

                                ctrLine.setListFirstCommand("javascript:itemList('" + Command.FIRST + "')");
                                ctrLine.setListNextCommand("javascript:itemList('" + Command.NEXT + "')");
                                ctrLine.setListPrevCommand("javascript:itemList('" + Command.PREV + "')");
                                ctrLine.setListLastCommand("javascript:itemList('" + Command.LAST + "')");
                            %>
                            <%=ctrLine.drawImageListLimit(cmdItem, vectSizeItem, startItem, recordToGetItem)%>
                        </span>
                    </td>
                </tr>
                <%}%>
            </table>

            <% if (rec.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) {%>
            <table width="100%" height="5%" border="0" cellspacing="0" cellpadding="0">
                <tr align="left" valign="">
                    <td>
                        <a class="btn-primary btn-lg" href="javascript:addItem()"><i class="fa fa-plus-circle"></i> &nbsp;<%=ctrLine.getCommand(SESS_LANGUAGE, recCode + " Item", ctrLine.CMD_ADD, true)%></a>
                    </td>
                </tr>
            </table>
            <% } %>
            
            <%if (listMatReceiveItem != null && listMatReceiveItem.size() > 0) {%>
            <table width="100%" height="5%" border="0" cellspacing="0" cellpadding="0">
                <tr align="left">
                    <td>
                        <a class="btn-primary btn-lg" href="javascript:printForm('<%=oidReceiveMaterial%>')" class="command" ><i class="fa fa-print"></i>&nbsp; <%=textListGlobal[SESS_LANGUAGE][8]%></a>
                        <a class="btn-primary btn-lg" href="javascript:printExcel('<%=oidReceiveMaterial%>')" class="command" ><i class="fa fa-file-excel-o"></i>&nbsp; Import File Excel</a>
                        &nbsp;&nbsp;
                        <%if (privShowQtyPrice) {%>
                        <select name="type_print_tranfer">
                            <option value="0">Price Cost</option>
                            <option value="1">Sell Price</option>
                            <option value="2">Cost & Sell Price</option>
                            <%if (typeOfBusinessDetail == 2) {%>
                            <option value="3">Without Cost & Sell Price</option>
                            <%}%>
                        </select>
                        <%} else {%>
                        <input type="hidden" name="type_print_tranfer" value="0">
                        <%}%>
                    </td>
                </tr>
            </table>
            <%}%>
            
            <table width="100%" height="5%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td><a href="javascript:viewHistoryTable()">History</a></td>
                </tr>
            </table>
            
            <%}%>
            
        </form>
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr> 
                <td colspan="2" height="20">
                    <%if (menuUsed == MENU_ICON) {%>
                    <%@include file="../../../styletemplate/footer.jsp" %>
                    <%} else {%>
                    <%@ include file = "../../../main/footer.jsp" %>
                    <%}%>
                </td>
            </tr>
        </table>
    </body>
</html>
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
<%//
    int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE);
    int appObjCodeShowQtyAndPrice = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE_QTY_AND_PRICE);
    int  appObjCodeAdditionalPrice = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE_PAYMENT);
    int appObjCodePayment = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE_PAYMENT);
%>
<%@ include file = "../../../main/checkuser.jsp" %>

<%//boolean privApproval = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_APPROVE));
    boolean privApprovalApprove = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_APPROVE));
    boolean privApprovalFinal = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_FINAL));
    boolean privShowQtyPrice = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeShowQtyAndPrice, AppObjInfo.COMMAND_VIEW));
    boolean privAdditionalPrice = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeAdditionalPrice, AppObjInfo.COMMAND_VIEW));
    boolean privPayment = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodePayment, AppObjInfo.COMMAND_FINAL));
%>

<%!//
    static String sEnableExpiredDate = PstSystemProperty.getValueByName("ENABLE_EXPIRED_DATE");
    static boolean bEnableExpiredDate = (sEnableExpiredDate != null && sEnableExpiredDate.equalsIgnoreCase("YES")) ? true : false;

    public static final String textListGlobal[][] = {
        {"Penerimaan", "Dari Pembelian", "Pencarian", "Daftar", "Edit", "Dengan PO", "Tanpa PO", "Tidak ada item penerimaan barang", "Cetak Penerimaan Barang", "Posting Stock", "Posting Harga Beli", ""}, //10
        {"Receive", "From Purchase", "Search", "List", "Edit", "With PO", "Without PO", "There is no goods receive item", "Print Goods Receive", "Posting Stock", "Posting Cost Price", ""}
    };

    /* this constant used to list text of listHeader */
    public static final String textListOrderHeader[][] = {
    {"Kode","Lokasi","Tanggal","Supplier","Status","Keterangan","Nomor Invoice","Ppn","Waktu","Mata Uang","Sub Total","Grand Total","Include", "%","Terms","Days","Rate","Tipe Item Penerimaan","Tipe Penerimaan","HEL","Nilai Tukar","Berat Nota","Total Nota","Kepemilikan","Berat 24k", "Nomor BC", "Jenis Dokumen", "Lokasi Pabean"},
    {"Code","Location","Date","Supplier","Status","Remark","Supplier Invoice","VAT","Time","Currency","Sub Total","Grand Total","Include", "%","Terms","Days","Rate","Receive Item Type","Receive Type","HEL","Exchange Value","Weight","Total Nota","Own","Weight 24k", "Customs Number", "Document Type", "Pabean Location"}
    };

    /* this constant used to list text of listMaterialItem */
//public static final String textListOrderItem[][] = {
    //{"No","Sku","Nama Barang","Kadaluarsa","Unit","Harga Beli","Ongkos Kirim","Mata Uang","Qty","Total Beli"},
    //{"No","Code","Name","Expired Date","Unit","Cost","Delivery Cost","Currency","Qty","Total Cost","last Discount %"}
//};

/* this constant used to list text of listMaterialItem */
    public static final String textListOrderItem[][] = {
        {"No", "Sku", "Nama Barang", "Kadaluarsa", "Unit", "Harga Beli", "Ongkos Kirim", "Mata Uang", "Qty", "Total Beli", "Diskon Terakhir %",//10
            "Diskon1 %", "Diskon2 %", "Discount Nominal", "Hapus", "Barcode", "Berat (gr)", "Keterangan", "Sorting", "Warna", "Etalase",  "HS Code"},//20
        {"No", "Code", "Name", "Expired Date", "Unit", "Cost", "Delivery Cost", "Currency", "Qty", "Total Cost", "last Discount %", "Discount1 %",
            "Discount2 %", "Disc. Nominal", "Delete", "Barcode", "Weight", "Remark", "Sorting", "Color", "Etalase", "HS Code"}
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

public static int getStrDutyFree(){
	String strDutyFree = PstSystemProperty.getValueByName("ENABLE_DUTY_FREE");
	System.out.println("#Duty Free: " + strDutyFree);
	int dutyFree = Integer.parseInt(strDutyFree);
	return dutyFree;
}

    /**
     * this method used to list all po item
     */
    public Vector drawListRecItem(int language, Vector objectClass, int start, boolean privManageData,
            boolean privShowQtyPrice, int statusDoc, String approot, int select_status,
            String typeOfBusiness, String syspropDiscount1, String syspropDiscount2,
            String syspropDiscountNominal, String syspropOngkosKirim, String syspropBonus,
            String syspropHargaBeli, int typeOfBusinessDetail, int recItemType, MatReceive rec,
            String syspropColor, String syspropEtalase, String syspropTotalBeli, String syspropHargaJual, String syspropHSCode, String syspropKeterangan, String syspropPenerimaanHpp, String useForRaditya, int fromReturn) {
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
            ctrlist.addHeader(textListOrderItem[language][1], "10%");//SKU
            if (typeOfBusiness.equals("0")) {
                ctrlist.addHeader(textListOrderItem[language][15], "10%");//Barcode
            }
            if (syspropHSCode.equals("1") && getStrDutyFree() == 1) { 
                ctrlist.addHeader(textListOrderItem[language][21], "7%");//HS Code 
            }
            ctrlist.addHeader(textListOrderItem[language][2], "20%");//Nama Barang
            if (bEnableExpiredDate) {
                ctrlist.addHeader(textListOrderItem[language][3], "8%");//Kadaluarsa
            }
            if (typeOfBusinessDetail != 2) {
                ctrlist.addHeader(textListOrderItem[language][5], "5%");//Unit  
                ctrlist.addHeader(textListOrderItem[language][4], "5%");//Unit  
            }
            if (privShowQtyPrice) {
                ctrlist.addHeader(textListOrderItem[language][8], "5%");//Qty
                if (typeOfBusinessDetail == 2) {
                    ctrlist.addHeader(textListOrderItem[language][16], "5%");//Berat
                }
if(useForRaditya.equals("1")){}else{
                if (syspropHargaBeli.equals("1")) {
                    if (typeOfBusinessDetail == 2 && (recItemType == Material.MATERIAL_TYPE_EMAS || recItemType == Material.MATERIAL_TYPE_EMAS_LANTAKAN)) {
                        ctrlist.addHeader("Harga Emas (Rp)", "8%");
                    } else {
                        if (fromReturn == 1){
                            ctrlist.addHeader("Harga", "8%");//Harga Beli
                        } else {
                            ctrlist.addHeader(textListOrderItem[language][5], "8%");//Harga Beli
                        }
                        
                    }
                }
                    if(syspropPenerimaanHpp.equals("1")){
                     ctrlist.addHeader("Harga Pokok", "8%");//Harga Pokok
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
            if (syspropKeterangan.equals("1")) {
            ctrlist.addHeader(textListOrderItem[language][17], "5%");//keterangan
              }
//            if (typeOfBusinessDetail == 2 && rec.getReceiveSource() == PstMatReceive.SOURCE_FROM_BUYBACK) {
                ctrlist.addHeader(textListOrderItem[language][18], "5%");//sorting
//            }
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
                try {
                    cat = PstCategory.fetchExc(mat.getCategoryId());
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
                if ((privManageData || privShowQtyPrice) && statusDoc != I_DocStatus.DOCUMENT_STATUS_CLOSED) {
                    rowx.add("<a href=\"javascript:editItem('" + String.valueOf(recItem.getOID()) + "')\">" + mat.getSku() + "</a>");
                } else {
                    rowx.add(mat.getSku());
                }

                if (typeOfBusiness.equals("0")) {
                    rowx.add(mat.getBarCode());
                }
                if (syspropHSCode.equals("1") && getStrDutyFree() == 1) {
                    rowx.add("<div align=\"center\">" + cat.getCode()+ "</div>");
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
                    rowx.add("<div align=\"center\">" + FRMHandler.userFormatStringDecimal(recItem.getCost())+ "</div>");
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

            if(useForRaditya.equals("1")){}else{
                if (privShowQtyPrice) { 
                    if (syspropHargaBeli.equals("1")) {
                        if (typeOfBusinessDetail == 2) {
                            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(recItem.getCost()) + "</div>");
                        } else {
                            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(recItem.getCost()) + "</div>");
                        }
                    }
                    if(syspropPenerimaanHpp.equals("1")){
                        rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(mat.getAveragePrice()) + "</div>");
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
}
                if (syspropEtalase.equals("1")) {
                    rowx.add("<div align=\"right\">" + ksg.getName() + "</div>");
                }
                if (syspropColor.equals("1")) {
                    rowx.add("<div align=\"right\">" + color.getColorName() + "</div>");
                }
              if (syspropKeterangan.equals("1")) {
                rowx.add("<div align=\"\">" + recItem.getRemark() + "</div>");
                }
//                if (typeOfBusinessDetail == 2 && rec.getReceiveSource() == PstMatReceive.SOURCE_FROM_BUYBACK) {
                    rowx.add("<div align=\"right\">" + PstMatReceiveItem.sortingKey[recItem.getSortStatus()] + "</div>");
//                }
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

<%!//TABEL INI DI HIDE
    public Vector drawListRecBonusItem(int language, Vector objectClass, int start, boolean privManageData, boolean privShowQtyPrice, int statusDoc, String approot) {
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
            ctrlist.addHeader(textListOrderItem[language][0], "5%");//No
            ctrlist.addHeader(textListOrderItem[language][1], "10%");//SKU
            ctrlist.addHeader(textListOrderItem[language][2], "20%");//Nama Barang
            if (bEnableExpiredDate) {
                ctrlist.addHeader(textListOrderItem[language][3], "8%");//Kadaluarsa
            }
            ctrlist.addHeader(textListOrderItem[language][4], "5%");//Unit

            if (privShowQtyPrice) {
                ctrlist.addHeader(textListOrderItem[language][5], "8%");//Harga Beli
                ctrlist.addHeader(textListOrderItem[language][11], "5%");//Diskon1 %
                ctrlist.addHeader(textListOrderItem[language][12], "5%");//Diskon2 %
                ctrlist.addHeader(textListOrderItem[language][13], "8%");//Discount Nominal
                ctrlist.addHeader(textListOrderItem[language][6], "8%");//Ongkos Kirim
                ctrlist.addHeader(textListOrderItem[language][8], "9%");//Qty
                ctrlist.addHeader(textListOrderItem[language][9], "10%");//Total Beli
            } else {
                ctrlist.addHeader(textListOrderItem[language][8], "9%");//Qty
            }

            ctrlist.addHeader(textListOrderItem[language][14], "9%");

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
                rowx = new Vector();
                start = start + 1;
                double totalForwarderCost = recItem.getForwarderCost() * recItem.getQty();

                rowx.add("" + start + "");
                if ((privManageData || privShowQtyPrice) && statusDoc != I_DocStatus.DOCUMENT_STATUS_CLOSED) {
                    rowx.add("<a href=\"javascript:editItem('" + String.valueOf(recItem.getOID()) + "')\">" + mat.getSku() + "</a>");
                } else {
                    rowx.add(mat.getSku());
                }

                rowx.add(mat.getName());
                if (bEnableExpiredDate) {
                    rowx.add("<div align=\"center\">" + Formater.formatDate(recItem.getExpiredDate(), "dd-MM-yyyy") + "</div>");
                }

                rowx.add("<div align=\"center\">" + unit.getCode() + "</div>");

                if (privShowQtyPrice) {
                    rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(recItem.getCost()) + "</div>");
                    rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(recItem.getDiscount()) + "</div>");
                    rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(recItem.getDiscount2()) + "</div>");
                    rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(recItem.getDiscNominal()) + "</div>");
                    rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(recItem.getForwarderCost()) + "</div>");
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
                    rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(recItem.getQty()) + "</div>");
                }

                if (privShowQtyPrice) {
                    rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(recItem.getTotal() + totalForwarderCost) + "</div>");
                }

                rowx.add(" <div align=\"center\"> <a href=\"javascript:cmdNewDelete('" + String.valueOf(recItem.getOID()) + "')\"><img src=" + approot + "/images/x3.png align=\"center\" ></a></div>");

                lstData.add(rowx);
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
    int fromReturn = FRMQueryString.requestInt(request, "from_return");

    String styleDisplay = "";
    if (fromReturn == 1){
        styleDisplay = "style='display: none;'";
    }
    
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
    String syspropHSCode = PstSystemProperty.getValueByName("SHOW_HS_CODE");
    String syspropKeterangan = PstSystemProperty.getValueByName("SHOW_KETERANGAN");
    String syspropPenerimaanHpp = PstSystemProperty.getValueByName("SHOW_PENERIMAAN_HPP");

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
    }

    double totalBeratItem = 0;
    if (!listMatReceiveItem.isEmpty()) {
        for (int i = 0; i < listMatReceiveItem.size(); i++) {
            Vector temp = (Vector) listMatReceiveItem.get(i);
            MatReceiveItem recItem = (MatReceiveItem) temp.get(0);
            totalBeratItem += recItem.getBerat();
        }
    }

    /**
     * get forwarder info
     */
    Vector vctForwarderInfo = new Vector(1, 1);
    try {
        if (oidReceiveMaterial != 0) {
            vctForwarderInfo = SessForwarderInfo.getObjForwarderInfo(oidReceiveMaterial);
        }
    } catch (Exception e) {
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

    Vector list = drawListRecItem(SESS_LANGUAGE, listMatReceiveItem, startItem, privManageData, privShowQtyPrice, rec.getReceiveStatus(), approot, rec.getReceiveStatus(), typeOfBusiness, syspropDiscount1, syspropDiscount2, syspropDiscountNominal, syspropOngkosKirim, syspropBonus, syspropHargaBeli, typeOfBusinessDetail, rec.getReceiveItemType(), rec, syspropColor, syspropEtalase, syspropTotalBeli, syspropHargaJual, syspropHSCode, syspropKeterangan, syspropPenerimaanHpp, useForRaditya, fromReturn);
    Vector listError = (Vector) list.get(1);

//untuk bonus
    String whereClauseBonusItem = "" + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + "=" + oidReceiveMaterial
            + " AND " + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_BONUS] + "=1";

    Vector listMatReceiveBonusItem = new Vector();
    if (rec != null && rec.getOID() != 0) {
        listMatReceiveBonusItem = PstMatReceiveItem.listVectorRecItemComplete(0, 0, whereClauseBonusItem, orderClauseItem);
    }

    Vector listBonus = drawListRecItem(SESS_LANGUAGE, listMatReceiveBonusItem, startItem, privManageData, privShowQtyPrice, rec.getReceiveStatus(), approot, rec.getReceiveStatus(), typeOfBusiness, syspropDiscount1, syspropDiscount2, syspropDiscountNominal, syspropOngkosKirim, syspropBonus, syspropHargaBeli, typeOfBusinessDetail, rec.getReceiveItemType(), rec, syspropColor, syspropEtalase, syspropTotalBeli, syspropHargaJual, syspropHSCode, syspropKeterangan, syspropPenerimaanHpp, useForRaditya, fromReturn);
    Vector listBonusError = (Vector) listBonus.get(1);

%>

<%//
    if (iCommand == Command.DELETE && iErrCode == 0) {
%>
<jsp:forward page="search_receive_material.jsp">
    <jsp:param name="command" value="<%=Command.FIRST%>"/>
</jsp:forward>
<%
    }
%>

<html>
    <head>
        <title>Dimata - ProChain POS</title>
        <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap/css/bootstrap.min.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap/css/bootstrap-theme.min.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/jquery-ui-1.12.1/jquery-ui.min.css"/>
        <!--link rel="stylesheet" type="text/css" href="../styles/jquery-ui-1.12.1/jquery-ui.theme.min.css"/-->
        <link rel="stylesheet" type="text/css" href="../../../styles/dist/css/AdminLTE.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/dist/css/skins/_all-skins.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/font-awesome/font-awesome.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/iCheck/flat/blue.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/iCheck/all.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/select2/css/select2.min.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css">
        <link rel="stylesheet" type="text/css" href="../../../styles/plugin/datatables/dataTables.bootstrap.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap-notify.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/datepicker/datepicker3.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/prochain.css"/>

        <script language="JavaScript">
        //------------------------- START JAVASCRIPT FUNCTION FOR PO MAIN -----------------------

        var windowSupplier;
        function addSupplier(){
            windowSupplier=window.open("../../../master/contact/contact_company_edit.jsp?command=<%=Command.ADD%>&source_link=receive_wh_supp_material_edit_old.jsp","add_supplier", "height=600,width=800,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
            if (window.focus) { windowSupplier.focus();}
        }

        function  setSupplierOnLGR(textIn, supOID){
            var oOption = self.opener.document.createElement("OPTION");
            oOption.text=textIn;
            oOption.value="supOID";
            document.forms.frm_recmaterial.FRM_FIELD_SUPPLIER_ID.add(oOption);
            document.forms.frm_recmaterial.FRM_FIELD_SUPPLIER_ID.value = "504404432982825708";
            changeFocus(self.opener.document.forms.frm_recmaterial.FRM_FIELD_SUPPLIER_ID);
        }

        function cmdEdit(oid){
    
            document.frm_recmaterial.command.value="<%=Command.EDIT%>";
            document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
            document.frm_recmaterial.action="receive_wh_supp_material_edit_old.jsp";
            document.frm_recmaterial.submit();
        }

        function compare(){
            var dt = document.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_DATE]%>_dy.value;
            var mn = document.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_DATE]%>_mn.value;
            var yy = document.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_DATE]%>_yr.value;
            var dt = new Date(yy,mn-1,dt);
            var bool = new Boolean(compareDate(dt));
            return bool;
        }

        function cmdNewDelete(oid){
        var msg;
        msg= "<%=textDelete[SESS_LANGUAGE][0]%>" ;
        var agree=confirm(msg);
        if (agree)
        return cmdConfirmDeleteItem(oid) ;
        else
        return cmdEdit(oid);
        }

        function cmdConfirmDeleteItem(oidReceiveMaterialItem) {
            document.frm_recmaterial.hidden_receive_item_id.value=oidReceiveMaterialItem;
            document.frm_recmaterial.command.value="<%=Command.DELETE%>";
            document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
            document.frm_recmaterial.approval_command.value="<%=Command.DELETE%>";
            document.frm_recmaterial.action="receive_wh_supp_materialitem.jsp";
            document.frm_recmaterial.submit();
        }

        function cmdSave() {      
            <%
                if ((rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED)) {
            %>
                var invNo = document.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_INVOICE_SUPPLIER]%>.value;
               if(invNo!=''){
                    var statusDoc = document.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_STATUS]%>.value;
            <%if (typeOfBusiness.equals("3")) {%>
                         if(statusDoc=="<%=I_DocStatus.DOCUMENT_STATUS_APPROVED%>" && <%=privApprovalFinal%>==false){
                             var conf = confirm("<%=textPosting[SESS_LANGUAGE][0]%>");
                             if(conf){
                                document.frm_recmaterial.command.value="<%=Command.SAVE%>";
                                document.frm_recmaterial.iCommandPosting.value="<%=Command.POSTING%>";
                                document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
                                document.frm_recmaterial.action="receive_wh_supp_material_edit_old.jsp";
                                if(compare()==true)
                                            document.frm_recmaterial.submit();
                             }
                        }else if(statusDoc=="<%=I_DocStatus.DOCUMENT_STATUS_FINAL%>"){
                             var conf = confirm("<%=textPosting[SESS_LANGUAGE][1]%>");
                             if(conf){
                                document.frm_recmaterial.command.value="<%=Command.SAVE%>";
                                document.frm_recmaterial.iCommandPosting.value="<%=Command.REPOSTING%>";
                                document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
                                document.frm_recmaterial.action="receive_wh_supp_material_edit_old.jsp";
                                if(compare()==true)
                                            document.frm_recmaterial.submit();
                             }
                        }else{
                                document.frm_recmaterial.command.value="<%=Command.SAVE%>";
                                document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
                                document.frm_recmaterial.action="receive_wh_supp_material_edit_old.jsp";
                                if(compare()==true)
                                            document.frm_recmaterial.submit();
                        }   
            <% } else {%>
                        if(statusDoc=="<%=I_DocStatus.DOCUMENT_STATUS_FINAL%>"){
                             var conf = confirm("<%=textPosting[SESS_LANGUAGE][0]%>");
                             if(conf){
                                document.frm_recmaterial.command.value="<%=Command.SAVE%>";
                                document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
                                document.frm_recmaterial.iCommandPosting.value="<%=Command.POSTING%>";
                                document.frm_recmaterial.action="receive_wh_supp_material_edit_old.jsp";
                                if(compare()==true)
                                            document.frm_recmaterial.submit();
                             }
                        }else{
                            document.frm_recmaterial.command.value="<%=Command.SAVE%>";
                            document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
                            document.frm_recmaterial.action="receive_wh_supp_material_edit_old.jsp";
                            if(compare()==true)
                                        document.frm_recmaterial.submit();
                        }
            <%}%>
                }else{
                    alert("Nomor invoice supplier harus diisi");
                }
            <%
            } else {
            %>
                    alert("Document has been <%=I_DocStatus.fieldDocumentStatus[rec.getReceiveStatus()]%> !!!");
            <%
                }
            %>
        }

        function cmdAsk(oid) {
            <% 
                if ((rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED)) {
            %>
                document.frm_recmaterial.command.value="<%=Command.ASK%>";
                document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
                document.frm_recmaterial.action="receive_wh_supp_material_edit_old.jsp";
                document.frm_recmaterial.submit();
            <%
            } else {
            %>
                    alert("Document has been <%=I_DocStatus.fieldDocumentStatus[rec.getReceiveStatus()]%> !!!");
            <%
                }
            %>
        }

        function gostock(oid) {
            document.frm_recmaterial.command.value="<%=Command.EDIT%>";
            document.frm_recmaterial.rec_type.value = 2;
            document.frm_recmaterial.type_doc.value = 2;
            document.frm_recmaterial.hidden_receive_item_id.value=oid;
            document.frm_recmaterial.action="rec_wh_stockcode.jsp";
            document.frm_recmaterial.submit();
        }

        function cmdCancel() {
            document.frm_recmaterial.command.value="<%=Command.CANCEL%>";
            document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
            document.frm_recmaterial.action="receive_wh_supp_material_edit_old.jsp";
            document.frm_recmaterial.submit();
        }

        function cmdConfirmDelete(oid) {
            document.frm_recmaterial.command.value="<%=Command.DELETE%>";
            document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
            document.frm_recmaterial.approval_command.value="<%=Command.DELETE%>";
            document.frm_recmaterial.action="receive_wh_supp_material_edit_old.jsp";
            document.frm_recmaterial.submit();
        }

        function cmdBack() {
            document.frm_recmaterial.command.value="<%=Command.FIRST%>";
            document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
            document.frm_recmaterial.action="search_receive_material.jsp";
            document.frm_recmaterial.submit();
        }
        
        function cmdBackReturn() {
            document.frm_recmaterial.command.value="<%=Command.FIRST%>";
            document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
            document.frm_recmaterial.action="search_receive_return_material.jsp";
            document.frm_recmaterial.submit();
        }

        function printExcel() {
            var typePrint = document.frm_recmaterial.type_print_tranfer.value;
            window.open("receive_wh_supp_material_print_form.jsp?printExcel=1&hidden_receive_id=<%=oidReceiveMaterial%>&command=<%=Command.EDIT%>&type_print_tranfer="+typePrint+"","receivereport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
        }

        function printForm() {
            var typePrint = document.frm_recmaterial.type_print_tranfer.value;
            <%if (typeOfBusinessDetail == 2) {%>
                window.open("print_out_receive_wh_supp_material_item.jsp?hidden_receive_id=<%=oidReceiveMaterial%>&command=<%=Command.LIST%>&type_print_tranfer="+typePrint+"","receivereport");
            <%} else {%>
                window.open("receive_wh_supp_material_print_form.jsp?hidden_receive_id=<%=oidReceiveMaterial%>&command=<%=Command.EDIT%>&type_print_tranfer="+typePrint+"","receivereport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
            <%}%>    
        }

        function PostingStock() {
            var conf = confirm("<%=textPosting[SESS_LANGUAGE][0]%>");
            if(conf){
                document.frm_recmaterial.command.value="<%=Command.POSTING%>";
                document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
                document.frm_recmaterial.action="receive_wh_supp_material_edit_old.jsp";
                document.frm_recmaterial.submit();
                }
        }

        function PostingCostPrice() {
            var conf = confirm("<%=textPosting[SESS_LANGUAGE][1]%>");
            if(conf){
                document.frm_recmaterial.command.value="<%=Command.REPOSTING%>";
                document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
                document.frm_recmaterial.action="receive_wh_supp_material_edit_old.jsp";
                document.frm_recmaterial.submit();
                }
        }

        function addForwarderInfo() {
            <%
                if ((rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED)) {
            %>
                document.frm_recmaterial.command.value="<%=Command.EDIT%>";
                document.frm_recmaterial.action="forwarder_info.jsp";
                document.frm_recmaterial.submit();
            <%
            } else {
            %>
                    alert("Document has been <%=I_DocStatus.fieldDocumentStatus[rec.getReceiveStatus()]%> !!!");
            <%
                }
            %>
        }

        function editForwarderInfo(){
            document.frm_recmaterial.command.value="<%=Command.EDIT%>";
            document.frm_recmaterial.action="forwarder_info.jsp";
            document.frm_recmaterial.submit();
        }

        function addReceivePayable(oid) {
    
                document.frm_recmaterial.command.value="<%=Command.ADD%>";
                document.frm_recmaterial.oid_invoice_selected.value=oid;
                document.frm_recmaterial.action="../../../arap/payable/payable_view.jsp";
                document.frm_recmaterial.submit();
        }

        function printBarcode(){
            var con = confirm("Print Barcode ? ");
            if (con ==true)
            {
                window.open("<%=approot%>/servlet/com.dimata.posbo.printing.warehouse.PrintBarcode?hidden_receive_id=<%=oidReceiveMaterial%>&command=<%=Command.EDIT%>","corectionwh","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
            }
        }

        function changeCurrency(value){
            //alert("asa");
            var oidCurrencyId=value;
            checkAjax(oidCurrencyId);
        }

        function checkAjax(oidCurrencyId){
            $.ajax({
            url : "<%=approot%>/servlet/com.dimata.posbo.ajax.CurrentDailyRateRunning?FRM_FIELD_RATE="+oidCurrencyId+"&typeCheckCurrency=1",
            type : "POST",
            async : false,
            success : function(data) {
                 document.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_TRANS_RATE]%>.value=data;
                 //document.frm_purchaseorder.exchangeRate.value=data;
            }
        });
        }

        //------------------------- END JAVASCRIPT FUNCTION FOR PO MAIN -----------------------


        //------------------------- START JAVASCRIPT FUNCTION FOR PO ITEM -----------------------
        function addItem() {
            <%
                if ((rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED)) {
            %>
                document.frm_recmaterial.command.value="<%=Command.ADD%>";
                document.frm_recmaterial.action="receive_wh_supp_materialitem.jsp";
                if(compareDateForAdd()==true)
                    document.frm_recmaterial.submit();
            <%
            } else {
            %>
                    alert("Document has been <%=I_DocStatus.fieldDocumentStatus[rec.getReceiveStatus()]%> !!!");
            <%
                }
            %>
        }
		
		function importItem(oid){
			window.open("import_item_excel.jsp?receive_material_id="+oid,"receivereport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
		}

        function editItem(oid) {
            <%
                if ((rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED)) {
            %>
                document.frm_recmaterial.command.value="<%=Command.EDIT%>";
                document.frm_recmaterial.hidden_receive_item_id.value=oid;
                document.frm_recmaterial.action="receive_wh_supp_materialitem.jsp";
                document.frm_recmaterial.submit();
            <%
            } else {
            %>
                    alert("Document has been <%=I_DocStatus.fieldDocumentStatus[rec.getReceiveStatus()]%> !!!");
            <%
                }
            %>
        }

        function itemList(comm) {
            document.frm_recmaterial.command.value=comm;
            document.frm_recmaterial.prev_command.value=comm;
            document.frm_recmaterial.action="receive_wh_supp_materialitem.jsp";
            document.frm_recmaterial.submit();
        }

        function cekBerat(oid) {
            document.frm_recmaterial.command.value="<%=Command.EDIT%>";
            document.frm_recmaterial.hidden_receive_item_id.value=oid;
            document.frm_recmaterial.action="receive_wh_supp_material_edit_old.jsp?cek_berat=1";
            document.frm_recmaterial.submit();
        }
        //------------------------- END JAVASCRIPT FUNCTION FOR PO ITEM -----------------------


        //------------------------- START JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------
        function MM_swapImgRestore() { //v3.0
                var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
        }

        function MM_preloadImages() { //v3.0
                var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
                var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
                if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
        }

        function MM_swapImage() { //v3.0
                var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
                if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
        }
        //------------------------- END JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------

        function MM_findObj(n, d) { //v4.01
          var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
            d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
          if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
          for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
          if(!x && d.getElementById) x=d.getElementById(n); return x;
        }

        function viewHistoryTable() {
            var strvalue ="../../../main/historypo.jsp?command=<%=Command.FIRST%>"+
                             "&oidDocHistory=<%=oidReceiveMaterial%>";
            window.open(strvalue,"material", "height=600,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
        }

        function formApproval(recMatId) {    
            var winApproval = window.open("receive_approval_form.jsp?rec_mat_id="+recMatId, "material", "height=300,width=400,left=500,top=100,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes,resizing=false");
            if (window.focus) {
                winApproval.focus();
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
        <script src="../../../styles/jquery.min.js"></script>
        <!-- #BeginEditable "stylestab" -->
        <link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
        <!-- #EndEditable -->
        <!-- #BeginEditable "headerscript" -->
        <SCRIPT language=JavaScript>
        function hideObjectForMarketing(){
        }

        function hideObjectForWarehouse(){
        }

        function hideObjectForProduction(){
        }

        function hideObjectForPurchasing(){
        }

        function hideObjectForAccounting(){
        }

        function hideObjectForHRD(){
        }

        function hideObjectForGallery(){
        }

        function hideObjectForMasterData(){
        }

        </SCRIPT>
        <script>
            $(document).ready(function () {
        
                $('#tipeItemPenerimaan').change(function(){
                    var val = $(this).val();
                    if (val === "<%=Material.MATERIAL_TYPE_EMAS%>" || val === "<%=Material.MATERIAL_TYPE_EMAS_LANTAKAN%>") {                
                        $('.tipe_emas').removeAttr('hidden');
                        $('#totalNotaTitle').html("Total HE (Rp)");
                    } else {
                        $('.tipe_emas').attr({'hidden':true});
                        $('#totalNotaTitle').html("Total Beli (Rp)");
                    }
                });
        
                $('#radioBeli').click(function(){
                    var command = "<%=iCommand%>";
                    var status = "";
                    if (command === "<%=Command.ADD%>") {
                        status = $('#optionAdd').html();
                    } else if (command === "<%=Command.EDIT%>") {
                        status = $('#optionBeli').html();
                    }
                    $('.comboStatus').html(status);
                });
        
                $('#radioTitip').click(function(){
                    var command = "<%=iCommand%>";
                    var status = "";            
                    if (command === "<%=Command.ADD%>") {
                        status = $('#optionAdd').html();
                    } else if (command === "<%=Command.EDIT%>") {
                        status = $('#optionTitip').html();
                    }
                    $('.comboStatus').html(status);
                });
        
                $('.countTotalHe').on('keyup', function(evt){
                    var jenisItem = $('#tipeItemPenerimaan').val();
                    if (jenisItem === "<%=Material.MATERIAL_TYPE_EMAS%>" || jenisItem === "<%=Material.MATERIAL_TYPE_EMAS_LANTAKAN%>") {
                        //total berat 24k                
                        var beratNota = $('#recBeratNota').val().replace(/,/g,'');
                        var nilaiTukar = $('#recNilaiTukar').val().replace(/,/g,'');
                        var berat24k = (nilaiTukar/100) * beratNota;
                        $('#recBerat24k').val(berat24k.toFixed(3));
                        //total he
                        var hel = $('#recHel').val().replace(/,/g,'') * 1;
                        var totalHe = (nilaiTukar/100) * beratNota * hel;
                        if (evt.which != 190 && evt.which != 188 ){//not a fullstop
                            $('#recHel').val(hel.toLocaleString(undefined, {minimumFractionDigits: 0, maximumFractionDigits: 2}));
                        }
                        $('#totalHe').val(totalHe.toLocaleString());
                        //total nota
                        var totalOngkos = $('#totalOngkos').val().replace(/,/g,'') * 1;
                        var totalNota = totalOngkos + totalHe;
                        $('#totalOngkos').val(totalOngkos.toLocaleString());
                        $('#totalNota').val(totalNota.toLocaleString());
                    } else {
                
                    }            
                });
        
                $('#totalHe').keyup(function(){            
                    var totalOngkos = $('#totalOngkos').val().replace(/,/g,'') * 1;
                    var totalHe = $('#totalHe').val().replace(/,/g,'') * 1;
                    var totalNota = totalOngkos + totalHe;
                    //$('#totalHe').val(totalHe.toLocaleString());
                    $('#totalOngkos').val(totalOngkos.toLocaleString());
                    $('#totalNota').val(totalNota.toLocaleString());
                });
        
                $('#totalOngkos').keyup(function(){
                    var totalOngkos = $('#totalOngkos').val().replace(/,/g,'');
                    var totalHe = $('#totalHe').val().replace(/,/g,'') * 1;
                    var totalNota = 1*totalOngkos + totalHe;
                    var last = totalOngkos.slice(totalOngkos.length - 1);
                    var show = (last === ".") ? Number(totalOngkos).toLocaleString() + "." : Number(totalOngkos).toLocaleString();
                    $('#totalOngkos').val(show);
                    $('#totalNota').val(Number(totalNota).toLocaleString());
                });
        
            });
        </script>
        <style>
      .listgentitle {
        font-size: 12px !important;
        font-style: normal;
        font-weight: bold;
        color: #FFFFFF;
        background-color: #3e85c3 !important;
        text-align: center;
        height: 25px !important;
        border: 1px solid !important;
      }
table.listgen {
    margin: auto 1%;
    text-align: center;
    width: 96% !important;
}

      .listgensell {
        color: #000000;
        background-color: #ffffff !important;
        border: 1px solid #3e85c3;
      }


      .btn {
        padding: 5px 8px;
        font-size: 12px;
        line-height: 1.5;
        border-radius: 3px;
      }
      .form-control[disabled], .form-control[readonly], fieldset[disabled] .form-control {
        cursor: not-allowed;
        background-color: #fff !important;
        opacity: 1;
      }
select {
    height: 30px !important;
    border: 1px solid #ccc !important;
    padding: 0px !important;
    width: 100%;
}
textarea.formElemen {
    min-height: 55px;
    border-radius: 0px;
    width: 100%;
}
.row {
    margin-bottom: 10px;
}
label.control-label.col-sm-4.padd {
    padding: 10px 0px;
    margin: 0px;
}
label.control-label.col-sm-4.paddd {
    padding: 0px;
}
.col-sm-8 {
    margin-bottom: 10px;
}
label.control-label.col-sm-4 {
    padding-top: 10px;
}
select.tanggal {
    width: 27.9%;
}
input#selectNomor {
    width: 90%;
}

.form-control {
  font-size: 12px
}
select.waktu {
    width: 30.2%;
}
select#FRM_FIELD_LOCATION_ID {
    width: 90%;
}
li.fa.fa-trash {
    color: #ff0700;
    width: 25px;
    height: 25px;
    font-size: 18px;
}
select#FRM_FIELD_LOCATION_PABEAN {
    width: 90%;
}
.btn {
    padding: 6px 8px !important;
    font-size: 12px;
    line-height: 1.5;
    border-radius: 3px;
    height: 30px;
}
a.btn.btn-success {
    margin-left: 10px;
}
td.pull-right {
    margin-right: 5%;
}
a.btn.btn-warning {
    margin-top: 15px;
    height: 30px;
}
label.history-view {
    margin-right: 5%;
    margin-top: 1%;
}
input#suplierName {
    width: 80.8%;
    float: left;
}
input#noPO {
    float: left;
    width: 80.8%;
}
a#btnOpenSearchSuplier {
    padding-top: 9px !important;
    height: 30px;
    border-radius: 0px;
    margin: 5px 0px 5px 0px;
    width: 35.5px;
}
select#FRM_FIELD_SUPPLIER_ID {
    width: 80.2%;
    margin-right: 0px;
    float: left;
}
a.btn.btn-primary.check {
    margin: 0;
    font-size: 12px;
    padding: 9px 4px 4px 4px !important;
    border-radius: 0px;
    height: 34px;
}
.col-sm-6 {
    padding: 0px !important;
}

.input-group .input-group-addon {
    color: #fff;
    border-radius: 0px;
    border-color: #faae00 !important;
    background-color: #fbaf01 !important;
}
select#FRM_FIELD_RECEIVE_STATUS {
    width: 90%;
}
.row {
    font-size: 11px;
    margin-bottom: 10px;
}
.total {
    margin-top: 15px;
    font-size: 14px;
}
</style>    
    </head> 
    <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
      <section class="content-header">
        <h1><%=textListGlobal[SESS_LANGUAGE][0]%>
          <small><%=textListGlobal[SESS_LANGUAGE][1]%> </small></h1>
        <ol class="ol">
          <li>
            <a>
              <li class="active"><%=textListGlobal[SESS_LANGUAGE][6]%></li>
            </a>
          </li>
        </ol>
      </section>
      <p class="border"></p>
      <div class="container-pos">    
        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
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
                <td valign="top" align="left"> 
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">  
                        <tr>
                            <td>
                                <form name="frm_recmaterial" method="post" action="">
                                    <%
                                        try {
                                    %>
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
                                    <input type="hidden" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_SOURCE]%>" value="<%=(rec.getReceiveSource() == PstMatReceive.SOURCE_FROM_BUYBACK ? PstMatReceive.SOURCE_FROM_BUYBACK : (rec.getReceiveSource() == PstMatReceive.SOURCE_FROM_RETURN ? PstMatReceive.SOURCE_FROM_RETURN : PstMatReceive.SOURCE_FROM_SUPPLIER))%>">
                                    <input type="hidden" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_DISCOUNT]%>" value="<%=rec.getDiscount()%>">
                                    <input type="hidden" name="iCommandPosting" value="">

                                    <table width="100%" border="0">
                                        <tr>
                                            <td colspan="3">
                                                <table width="100%" border="0" cellpadding="1">  
                                                  <div class="row">
                                                      <div class="col-sm-12">
                                                        <!-- left side -->
                                                        <div class="col-sm-4">
                                                          
                                                          <div class="form-group">
                                                            <label class="control-label col-sm-4 padd" for="selectNomor"><%=textListOrderHeader[SESS_LANGUAGE][0]%></label>
                                                            <div class="col-sm-8" style="margin-bottom: 0px;">
                                                              <%String kode = "";
                                                      if (rec.getRecCode() == "") {
                                                        kode = "Kode Otomatis";
                                                      } else {
                                                        kode = rec.getRecCode();
                                                      }%>
                                                              <input id="selectNomor" class="form-control" type="text" name="" value="<%=kode%>" readonly>
                                                            </div>
                                                          </div>
                                                          <div class="form-group">
                                                            <label class="control-label col-sm-4 padd " for="lokasi"><%=textListOrderHeader[SESS_LANGUAGE][1]%></label>
                                                            <div class="col-sm-8" style="margin-bottom: 0px;">
                                                              <%
                                                                Vector obj_locationid = new Vector(1, 1);
                                                                Vector val_locationid = new Vector(1, 1);
                                                                Vector key_locationid = new Vector(1, 1);
                                                                /*String whereClause = PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE;
                                                                   whereClause += " OR " + PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE;
                                                                   Vector vt_loc = PstLocation.list(0, 0, whereClause, PstLocation.fieldNames[PstLocation.FLD_CODE]);*/
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
                                                              <%=ControlCombo.draw(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_LOCATION_ID], null, select_locationid, val_locationid, key_locationid, "tabindex=\"1\"", "formLocation")%>
                                                            </div>
                                                          </div>
                                                            <%if (getStrDutyFree() == 1) {%>
                                                          <div class="form-group">
                                                            <label class="control-label col-sm-4 padd" for="lokasi"><%=textListOrderHeader[SESS_LANGUAGE][27]%></label>
                                                            <div class="col-sm-8" style="margin-bottom: 0px;">
                                                              <%
                                                                Vector obj_locationid1 = new Vector(1, 1);
                                                                Vector val_locationid1 = new Vector(1, 1);
                                                                Vector key_locationid1 = new Vector(1, 1);
                                                                String locWhClause = "";//PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE;
                                                                String whereClausePabean = " (" + PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_PABEAN + ")";

                                                                Vector vt_loc1 = PstLocation.listLocationCreateDocument(0, 0, whereClausePabean, "");
                                                                String locOrderBy = String.valueOf(PstLocation.fieldNames[PstLocation.FLD_CODE]);
                                                                //Vector vt_loc1 = PstLocation.list(0,0,locWhClause,locOrderBy);
                                                                for (int d = 0; d < vt_loc1.size(); d++) {
                                                                  Location loc1 = (Location) vt_loc1.get(d);
                                                                  val_locationid1.add("" + loc1.getOID() + "");
                                                                  key_locationid1.add(loc1.getName());
                                                                }
                                                                String select_locationid1 = "" + rec.getLocationPabean(); //selected on combo box
              %>
                                                              <%=ControlCombo.draw(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_LOCATION_PABEAN], null, select_locationid1, val_locationid1, key_locationid1, " tabindex=\"1\"", "formLocation")%>
                                                            </div>
                                                          </div><%}%>
                                                          <div class="form-group">
                                                            <label class="control-label col-sm-4 padd" for="tanggal"><%=textListOrderHeader[SESS_LANGUAGE][2]%></label>
                                                            <div class="col-sm-8" style="margin-bottom: 0px;">
                                                              <%=ControlDate.drawDateWithStyle(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_DATE], (rec.getReceiveDate() == null) ? new Date() : rec.getReceiveDate(), 1, -5, "tanggal", "")%>
                                                            </div>
                                                          </div>
                                                            <%if(getStrDutyFree() == 1){%>
                                                          <div class="form-group">
                                                            <label class="control-label col-sm-4 padd" for="tanggalBC"><%=textListOrderHeader[SESS_LANGUAGE][2]%> BC</label>
                                                            <div class="col-sm-8" style="margin-bottom: 0px;">
                                                              <%=ControlDate.drawDateWithStyle(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_TANGGAL_BC], (rec.getTglBc()== null) ? new Date() : rec.getTglBc(), 1, -5, "tanggal", "")%>
                                                            </div>
                                                          </div>
                                                          <%}%>
                                                          <div class="form-group">
                                                            <label class="control-label col-sm-4 padd" for="status"><%=textListOrderHeader[SESS_LANGUAGE][4]%></label>
                                                            <div class="col-sm-8" style="margin-bottom: 0px;">
                                                                  <!--  Status Kondisi --> 
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
                                                                Vector obj_status = new Vector(1, 1);
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
                                                                  out.println(ControlCombo.draw(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_STATUS], null, select_status, val_status, key_status, "tabindex=\"4\"", "formStatus comboStatus"));
                                                                }
                                                              %>
                                                              <%if (privShowQtyPrice) {%>
                                                              <div>
                                                                <% if (rec.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) {%>
                                                                <%=textListForwarderInfo[SESS_LANGUAGE][7]%>
                                                                <%}%>
                                                              </div>
                                                              <%}%></div>
                                                          </div>
                                                        </div>
                                                        <!-- Center side -->             
                                                        <div class="col-sm-4">
                                                          <% if (getStrDutyFree() == 1) {%>
                                                          <div class="form-group">
                                                            <label class="control-label col-sm-4 padd" for="<%= FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_NOMOR_BC]%>"> <%= textListOrderHeader[SESS_LANGUAGE][25]%></label>
                                                            <div class="col-sm-8" style="margin-bottom: 0px;">
                                                              <input type="text" class="form-control"  id="<%= FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_NOMOR_BC]%>" name="<%= FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_NOMOR_BC]%>" value="<%= rec.getNomorBc()%>" required="">
                                                            </div>
                                                          </div>
                                                          <% } else { %><% }%>
                                                          <%if (privShowQtyPrice) {%>
                                                          <div class="form-group" <%=styleDisplay%>>
                                                            <label class="control-label col-sm-4 padd" for="terms"><%=textListOrderHeader[SESS_LANGUAGE][14]%></label>
                                                            <div class="col-sm-8" style="margin-bottom: 0px;">
                                                              <%
                                                                Vector val_terms = new Vector(1, 1);
                                                                Vector key_terms = new Vector(1, 1);
                                                                for (int d = 0; d < PstMatReceive.fieldsPaymentType.length; d++) {
                                                                  val_terms.add(String.valueOf(d));
                                                                  key_terms.add(PstMatReceive.fieldsPaymentType[d]);
                                                                }
                                                                String select_terms = "" + rec.getTermOfPayment();
                                                              %>
                                                              <%=ControlCombo.draw(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_TERM_OF_PAYMENT], null, select_terms, val_terms, key_terms, "", "formTerm")%>
                                                            </div>
                                                          </div>
                                                            <%}%>
                                                          <div class="form-group">
                                                            <label class="control-label col-sm-4 padd" for="waktu"><%=textListOrderHeader[SESS_LANGUAGE][8]%></label>
                                                            <div class="col-sm-8" style="margin-bottom: 0px;">
                                                              <%=ControlDate.drawTimeSec(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_DATE], (rec.getReceiveDate() == null) ? new Date() : rec.getReceiveDate(), "waktu")%>
                                                            </div>
                                                          </div>    
                                                          <div class="form-group">
                                                            <label class="control-label col-sm-4 padd" for="suplierName"><%=textListOrderHeader[SESS_LANGUAGE][3]%></label>
                                                            <div class="col-sm-8" style="margin-bottom: 0px;">
                                                              <%
                                                                Vector val_supplier = new Vector(1, 1);
                                                                Vector key_supplier = new Vector(1, 1);
                                                                String wh_supp = PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE]
                                                                        + " = " + PstContactClass.CONTACT_TYPE_SUPPLIER
                                                                        + " AND " + PstContactList.fieldNames[PstContactList.FLD_PROCESS_STATUS]
                                                                        + " != " + PstContactList.DELETE;
                                                                Vector vt_supp = PstContactList.listContactByClassType(0, 0, wh_supp, PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);

                                                                for (int d = 0; d < vt_supp.size(); d++) {
                                                                  ContactList cnt = (ContactList) vt_supp.get(d);
                                                                  String cntName = cnt.getContactCode() + " - " + cnt.getCompName();
                                                                  if (cntName.length() == 0) {
                                                                    cntName = cnt.getPersonName() + " " + cnt.getPersonLastname();
                                                                  }
                                                                  val_supplier.add(String.valueOf(cnt.getOID()));
                                                                  key_supplier.add(cntName);
                                                                }
                                                                String select_supplier = "" + rec.getSupplierId();
                                                              %>
                                                              <%=ControlCombo.draw(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_SUPPLIER_ID], null, select_supplier, val_supplier, key_supplier, "tabindex=\"2\"", "formSupplier")%> 
                                                              <a href="javascript:addSupplier();" class="btn-primary btn see" id="btnOpenSearchSuplier"><i class="fa fa-plus"> </i></a>
                                                            </div>
                                                          </div>
                                                          <div class="form-group">
                                                            <label class="control-label col-sm-4 padd" for="invoice"><%=textListOrderHeader[SESS_LANGUAGE][6]%></label>
                                                            <div class="col-sm-8">
                                                              <input type="text" class="form-control"  id="invoice" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_INVOICE_SUPPLIER]%>" value="<%=rec.getInvoiceSupplier()%>">
                                                            </div>
                                                          </div>
                                                            
                                                          <%if (typeOfBusinessDetail == 2) {%>
                                                          <div class="form-group" >
                                                            <label class="control-label col-sm-4 padd " for="tipeItemPenerimaan"><%=textListOrderHeader[SESS_LANGUAGE][17]%></label>
                                                            <div class="col-sm-8" style="margin-bottom: 0px;">
                                                              <select class="formItem" id="tipeItemPenerimaan" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_ITEM_TYPE]%>">   
                                                                <option value="">- Pilih -</option>
                                                                <%
                                                                  for (int main_material = 0; main_material < Material.MATERIAL_TYPE_TITLE.length; main_material++) {
                                                                    if (main_material == Material.MATERIAL_TYPE_GENERAL) {
                                                                      continue;
                                                                    }
                                                                %>                            
                                                                <option <%=rec.getReceiveItemType() == main_material ? "selected" : ""%> value="<%=main_material%>"><%=Material.MATERIAL_TYPE_TITLE[main_material]%></option>
                                                                <%
                                                                  }
                                                                %>                                
                                                              </select>
                                                            </div>
                                                          </div>
                                                          <div class="form-group">
                                                            <label class="control-label col-sm-4 padd" for="selectKepemilikan"><%=textListOrderHeader[SESS_LANGUAGE][23]%></label>
                                                            <div class="col-sm-8" style="margin-bottom: 0px;">
                                                              <%
                                                                SrcMemberReg objSrcMemberReg = new SrcMemberReg();
                                                                Vector vKepemilikan = SessMemberReg.searchKepemilikan(objSrcMemberReg, 0, 0);
                                                                Vector kepemilikan_Val = new Vector(1, 1);
                                                                Vector kepemilikan_Key = new Vector(1, 1);
                                                                kepemilikan_Val.add("0");
                                                                kepemilikan_Key.add("-");
                                                                for (int i = 0; i < vKepemilikan.size(); i++) {
                                                                  ContactList contactList = (ContactList) vKepemilikan.get(i);
                                                                  kepemilikan_Val.add("" + contactList.getOID());
                                                                  kepemilikan_Key.add("" + contactList.getContactCode() + " - " + contactList.getPersonName());
                                                                }
                                                              %>
                                                              <%=ControlCombo.draw(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_KEPEMILIKAN_ID], "formKepemilikan", null, "" + rec.getKepemilikanId(), kepemilikan_Val, kepemilikan_Key, null)%>

                                                            </div>
                                                          </div>
                                                          <%if (typeOfUseConsignment == 1) {%>
                                                          <div class="form-group" <%=styleDisplay%>>
                                                            <label class="control-label col-sm-4 padd" for="selectTipePenerimaan"><%=textListOrderHeader[SESS_LANGUAGE][18]%></label>
                                                            <div class="col-sm-8" style="margin-bottom: 0px;">
                                                              <%String checked = "";
                                                        if (rec.getOID() != 0 && rec.getReceiveType() == 0) {
                                                          checked = "checked";
                                                        }%>
                                                              <input <%=checked%> id="radioBeli" type="radio" value="0" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_TYPE]%>">Beli
                                                              <%checked = "";
                                                        if (rec.getOID() != 0 && rec.getReceiveType() == 1) {
                                                          checked = "checked";
                                                        }%>
                                                              <input <%=checked%> id="radioTitip" type="radio" value="1" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_TYPE]%>">Penitipan
                                                            </div>
                                                          </div>
                                                          <%}%>
                                                          <%} else {%><%if (typeOfUseConsignment == 1) {%>
                                                          <div class="form-group">
                                                            <label class="control-label col-sm-4 padd" for="selectPenerimaan"><%=textListOrderHeader[SESS_LANGUAGE][18]%></label>
                                                            <div class="col-sm-8" style="margin-bottom: 0px;">
                                                              <%
                                                                String checked1 = "";
                                                                String checked2 = "";
                                                                if (rec.getOID() != 0) {
                                                                  if (rec.getReceiveType() == 0) {
                                                                    checked1 = "checked";
                                                                  } else if (rec.getReceiveType() == 1) {
                                                                    checked2 = "checked";
                                                                  }
                                                                } else {
                                                                  if (syspropRecTypeDefault.equals("0")) {
                                                                    checked1 = "checked";
                                                                  } else if (syspropRecTypeDefault.equals("1")) {
                                                                    checked2 = "checked";
                                                                  }
                                                                }
                                                              %>
                                                              <input <%=checked1%> id="radioBeli" type="radio" value="0" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_TYPE]%>">Beli
                                                              <input <%=checked2%> id="radioTitip" type="radio" value="1" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_TYPE]%>">Penitipan
                                                            </div>
                                                          </div>
                                                          <%}%>
                                                            <%}%>
                                                        </div>
                                                        <!--Right Side-->
                                                        <div class="col-sm-4">
                                                          <% if (getStrDutyFree() == 1) {%>
                                                          <div class="form-group">
                                                            <label class="control-label col-sm-4 padd" for="<%= FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_JENIS_DOKUMEN]%>"><%= textListOrderHeader[SESS_LANGUAGE][26]%></label>
                                                            <div class="col-sm-8" style="margin-bottom: 5px;">
                                                              <input type="text" id="<%= FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_JENIS_DOKUMEN]%>" class="form-control" name="<%= FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_JENIS_DOKUMEN]%>" value="BC2.3" readonly>
                                                            </div>
                                                          </div>
                                                          <% } else { %><% } %>
                                                          <%if (privShowQtyPrice) {%>
                                                          <div class="form-group">
                                                            <label class="control-label col-sm-4 padd" for="days"><%=textListOrderHeader[SESS_LANGUAGE][15]%></label>
                                                            <div class="col-sm-8"  style="margin-bottom: 0px;">
                                                              <input type="text" class="form-control"  id="days" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_CREDIT_TIME]%>" value="<%=rec.getCreditTime()%>">
                                                              <%} else {%>
                                                              <input name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_CREDIT_TIME]%>" type="hidden" class="formElemen" style="text-align:right" size="5" value="<%=rec.getCreditTime()%>"></td>
                                                              <%}%>
                                                            </div>
                                                          </div>
                                                          <div class="form-group">
                                                            <label class="control-label col-sm-4 padd" for="mata"><%=textListOrderHeader[SESS_LANGUAGE][9]%></label>
                                                            <div class="col-sm-8" style="margin-bottom: 2px;">
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
                                                                  out.println(ControlCombo.draw(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_CURRENCY_ID], "formMata", null, "" + rec.getCurrencyId(), vectCurrVal, vectCurrKey, "onChange=\"javascript:changeCurrency(this.value)\""));
                                                                } else {
                                                                  out.println(ControlCombo.draw(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_CURRENCY_ID] + "_sel", "formMata", null, "" + rec.getCurrencyId(), vectCurrVal, vectCurrKey, "disabled"));
                                                                  out.println("<input type=\"hidden\" name=\"" + FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_CURRENCY_ID] + "\" value=\"" + rec.getCurrencyId() + "\">");
                                                                }
                                                              %>
                                                            </div>
                                                          </div>
                                                          <div class="form-group">
                                                            <label class="control-label col-sm-4 padd" for="rate"><%=textListOrderHeader[SESS_LANGUAGE][16]%></label>
                                                            <div class="col-sm-8" style="margin-bottom: 5px;">
                                                              <input  class="form-control"  id="rate" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_TRANS_RATE]%>" type="text"  value="<%=rec.getTransRate() != 0 ? rec.getTransRate() : resultKonversi%>" <%=enableInput%>>
                                                            </div>
                                                          </div>
                                                          <div class="form-group">
                                                            <label class="control-label col-sm-4 padd" for="textarea"><%=textListOrderHeader[SESS_LANGUAGE][5]%></label>
                                                            <div class="col-sm-8 ">
                                                              <textarea id="textarea" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_REMARK]%>" cols="25" rows="3" wrap="VIRTUAL" class="formElemen"><%=rec.getRemark()%></textarea>
                                                            </div>
                                                          </div>
                                                        </div> 
                                                      </div>
                                                    </div>
                                                </table>                  
                                            </td>
                                        </tr>
                                        <tr>
                                            <td colspan="3">
                                                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                    <tr align="left" valign="top">
                                                        <td height="22" valign="middle" colspan="3">
                                                            <%=list.get(0)%>
                                                        </td>
                                                    </tr>
                                                    <tr align="left" valign="top">
                                                        <td height="22" valign="middle" colspan="3">
                                                            <%-- if (!listMatReceiveItem.isEmpty()) {%>
                                                            <% if(typeOfBusinessDetail == 2) { %>
                                                            <br>
                                                            <p>
                                                                Total berat seluruh item : <b><%=Formater.formatNumber(totalBeratItem, "#,###.###")%> gram</b>
                                                                <a href="javascript:cekBerat('<%=oidReceiveMaterial%>')">Cek</a>                                
                                                            </p>
                                                            <% 
                                                                if (cekBeratStatus == 1) {
                                                                    double beratNota = rec.getBerat();
                                                                    double beratTotalItem = totalBeratItem;
                                                                    if (beratNota != beratTotalItem) {
                                                            %>
                                                            Berat total seluruh item tidak sesuai dengan berat di nota. <a href="javascript:formApproval('<%=oidReceiveMaterial%>')">Sesuaikan</a>                                                                
                                                            <%
                                                                    } else {
                                                            %>
                                                            Berat total seluruh item dan berat di nota sudah sesuai.
                                                            <%
                                                                    }
                                                                }
                                                            %>
                                                            <%}%>
                                                            <%}--%>
                                                        </td>
                                                    </tr>
                                                    <tr align="left" valign="top">
                                                        <td height="22" valign="middle" colspan="3"><%--Bonus Item--%></td>
                                                    </tr>
                                                    <tr align="left" valign="top">
                                                      <td height="22" valign="middle" colspan="3">
                                                          <%if(typeOfBusinessDetail != 2) {%>
                                                          <%=listBonus.get(0)%>
                                                          <%}%>
                                                      </td>
                                                    </tr>
                                                    <%if(oidReceiveMaterial!=0){%>
                                                    <tr align="left" valign="top">
                                                        <td height="8" align="left" colspan="3" class="command">
                                                            <span class="command">
                                                                <%
                                                                  if(cmdItem!=Command.FIRST && cmdItem!=Command.PREV && cmdItem!=Command.NEXT && cmdItem!=Command.LAST){
                                                                      cmdItem = Command.FIRST;
                                                                  }
                                                                  ctrLine.setLocationImg(approot+"/images");
                                                                  ctrLine.initDefault();
                                                                  ctrLine.setImageListName(approot+"/images","item");

                                                                  ctrLine.setListFirstCommand("javascript:itemList('"+Command.FIRST+"')");
                                                                  ctrLine.setListNextCommand("javascript:itemList('"+Command.NEXT+"')");
                                                                  ctrLine.setListPrevCommand("javascript:itemList('"+Command.PREV+"')");
                                                                  ctrLine.setListLastCommand("javascript:itemList('"+Command.LAST+"')");
                                                                %>
                                                                <%=ctrLine.drawImageListLimit(cmdItem,vectSizeItem,startItem,recordToGetItem)%>
                                                            </span>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                      <td width="3%"> <div align="left" class="total"><strong>TOTAL QTY :  </strong>
                                                        <%
                                                            double totQty = 0;
                                                            for (int i=0; i < listMatReceiveItem.size(); i++ ) {
                                                                Vector v = (Vector) listMatReceiveItem.get(i);
                                                                MatReceiveItem mri = (MatReceiveItem) v.get(0);
                                                                totQty += mri.getQty();
                                                            }
                                                        %> <strong><%= totQty %> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strong></div></td>
                                                    </tr>
                                                    <tr align="left" valign="top">
                                                        <td height="22" colspan="3" valign="middle" class="errfont">
                                                            <%
                                                                for(int k=0;k<listError.size();k++){
                                                                  if(k==0)
                                                                      out.println(listError.get(k)+"<br>");
                                                                  else
                                                                      out.println("&nbsp;&nbsp;&nbsp;"+listError.get(k)+"<br>");
                                                                }
                                                            %>
                                                        </td>
                                                    </tr>
                                                    <tr align="left" valign="top">
                                                        <td height="22" colspan="3" valign="middle" class="errfont">
                                                            <%
                                                              for(int k=0;k<listBonusError.size();k++){
                                                                if(k==0)
                                                                    out.println(listBonusError.get(k)+"<br>");
                                                                else
                                                                    out.println("&nbsp;&nbsp;&nbsp;"+listBonusError.get(k)+"<br>");
                                                              }
                                                            %>                        
                                                        </td>
                                                    </tr>
                                                    <tr align="left" valign="top">
                                                        <td height="22" valign="middle" colspan="3">
                                                            <table width="50%" border="0" cellspacing="2" cellpadding="0">
                                                                <tr>
                                                                    <% if(rec.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) { %>
                                                                    <td width="35%"><a class="btn-primary btn-lg" href="javascript:addItem()"><i class="fa fa-plus-circle"> &nbsp;<%=ctrLine.getCommand(SESS_LANGUAGE,recCode+" Item",ctrLine.CMD_ADD,true)%></i></a></td>
                                                                    <%if(useForGreenbowl.equals("1")){%>
                                                                   <td width="94%"><a class="btn-primary btn-lg" href="javascript:importItem('<%=rec.getOID()%>')"><i class="fa fa-file-excel-o"> &nbsp;Import Excel</i></a></td>
                                                                    <%}%>
                                                                    <% } %>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                    <%}%>
                                                </table>
                                            </td>
                                        </tr>
                                        <tr>
                                          <td valign="top" colspan="3">&nbsp;</td>
                                        </tr>
                                        <%if(privAdditionalPrice){%>
                                        <%-- update opie-eyek untuk privilage gudang tidak bisa bisa melihat nominal price--%>
                                        <%if(privShowQtyPrice){%>
                                        <% if(oidReceiveMaterial!=0 && listMatReceiveItem.size() > 0) { %>
                                        <tr>
                                            <td width="50%"  valign="top">
                                                <table width="98%" border="0">
                                                    <tr align="left" valign="top">
                                                        <td height="22" valign="middle" colspan="3" class="mainheader"><%=textListForwarderInfo[SESS_LANGUAGE][5]%> :</td>
                                                    </tr>
                                                    <%
                                                        if(vctForwarderInfo.size() > 0) {
                                                            Vector temp = (Vector)vctForwarderInfo.get(0);
                                                            ForwarderInfo forwarderInfo = (ForwarderInfo)temp.get(0);
                                                            ContactList contactList = (ContactList)temp.get(1);
                                                    %>
                                                    <tr align="left" valign="top">
                                                        <td width="1%" height="22" valign="middle" colspan="1">&nbsp;</td>
                                                        <td height="22" valign="middle" colspan="2">
                                                            <table width="85%" border="0" cellspacing="2" cellpadding="0">
                                                                <tr>
                                                                    <td height="22" width="22%"><%=textListForwarderInfo[SESS_LANGUAGE][0]%></td>
                                                                    <td height="22" width="3%">:</td>
                                                                    <td height="22" width="75%">
                                                                      <a href="javascript:editForwarderInfo()"><%=forwarderInfo.getDocNumber()%></a>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td height="22" width="22%"><%=textListForwarderInfo[SESS_LANGUAGE][1]%></td>
                                                                    <td height="22" width="3%">:</td>
                                                                    <td height="22" width="75%">
                                                                      <%=ControlCombo.draw(FrmForwarderInfo.fieldNames[FrmForwarderInfo.FRM_FIELD_CONTACT_ID],null,String.valueOf(forwarderInfo.getContactId()),val_supplier,key_supplier,"disabled=\"true\"","formElemen")%>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td height="22" width="22%"><%=textListForwarderInfo[SESS_LANGUAGE][2]%></td>
                                                                    <td height="22" width="3%">:</td>
                                                                    <td height="22" width="75%">
                                                                      <%=ControlDate.drawDateWithStyle(FrmForwarderInfo.fieldNames[FrmForwarderInfo.FRM_FIELD_DOC_DATE], (forwarderInfo.getDocDate()==null) ? new Date() : forwarderInfo.getDocDate(), 0, -1, "formElemen", "disabled=\"true\"")%>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td height="22" width="22%"><%=textListForwarderInfo[SESS_LANGUAGE][3]%></td>
                                                                    <td height="22" width="3%">:</td>
                                                                    <td height="22" width="75%">
                                                                      <strong><%=FRMHandler.userFormatStringDecimal(totalForwarderCost)%></strong>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td height="22" width="22%"><%=textListForwarderInfo[SESS_LANGUAGE][4]%></td>
                                                                    <td height="22" width="3%">:</td>
                                                                    <td height="22" width="75%">
                                                                      <textarea name="<%=FrmForwarderInfo.fieldNames[FrmForwarderInfo.FRM_FIELD_NOTES]%>" class="formElemen" wrap="VIRTUAL" rows="2" cols="27" disabled="true"><%=forwarderInfo.getNotes()%></textarea>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                    <% } else { %>
                                                    <tr align="left" valign="top">
                                                        <td height="22" valign="middle" colspan="3" class="comment">&nbsp;&nbsp;<%=textListForwarderInfo[SESS_LANGUAGE][6]%></td>
                                                    </tr>
                                                    <tr align="left" valign="top">
                                                        <td height="22" valign="middle" colspan="3">
                                                            <table width="80%" border="0" cellspacing="2" cellpadding="0">
                                                                <tr>
                                                                  <% if(rec.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) { %>
                                                                    <td width="94%"><a class="btn-primary btn-lg" href="javascript:addForwarderInfo()"><i class="fa fa-plus-circle"> &nbsp; <%=ctrLine.getCommand(SESS_LANGUAGE,recCode+textListForwarderInfo[SESS_LANGUAGE][5],ctrLine.CMD_ADD,true)%></i></a></td>
                                                                  <% } %>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                    <% } %>
                                                </table>                  	
                                            </td>
                                             <!-- include receive_payable_list -->
                                             <!-- by Mirahu -->
                                             <!-- 20111220 -->
                                            <% if (privPayment){
                                                if(rec.getTermOfPayment()== 0){ %>
                                            <td width="18%" valign="top">
                                                <table width="100%" border="0" <%=styleDisplay%>>
                                                    <tr>
                                                        <td width="56%" class="mainheader"><div align="left"><%=textListDetailPayment[SESS_LANGUAGE][1]%> :</div></td>
                                                    </tr>
                                                    <tr>
                                                        <td width="56%">
                                                          <%@ include file = "receive_payable_list.jsp" %>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                         <td width="6%" valign="top">
                                                             <a href="javascript:addReceivePayable('<%=oidReceiveMaterial%>')" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,recCode+textListDetailPayment[SESS_LANGUAGE][2],ctrLine.CMD_ADD,true)%>"></a>
                                                             &nbsp;&nbsp;<a href="javascript:addReceivePayable('<%=oidReceiveMaterial%>')"><%=ctrLine.getCommand(SESS_LANGUAGE,recCode+textListDetailPayment[SESS_LANGUAGE][2],ctrLine.CMD_ADD,true)%></a>
                                                         </td>
                                                    </tr>
                                                </table>
                                           </td>
                                          <% }} %>
                                            <td width="32%" valign="top" >
                                                <table width="100%" border="0">
                                                    <%
                                                        String whereItem = "" + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + "=" + oidReceiveMaterial;
                                                        double totalBeli = PstMatReceiveItem.getTotal(whereItem);
                                                        double ppn = rec.getTotalPpn();
                                                        if (ppn == 0) {
                                                            ppn = defaultPpn;
                                                        }
                                                        double totalBeliWithPPN = (totalBeli * (ppn / 100)) + totalBeli;
                                                        double valuePpn = 0.0;
                                                        if (rec.getIncludePpn() == 1) {
                                                            valuePpn = totalBeli - (totalBeli / 1.1);
                                                        } //else if(rec.getIncludePpn()== 0){
                                                        else {
                                                            valuePpn = totalBeli * (ppn / 100);
                                                        }
                                                    %>
                                                    <tr>
                                                        <td width="56%"><div align="right"><%=textListOrderHeader[SESS_LANGUAGE][10]%> <%=(fromReturn == 1 ? "" : textListOrderItem[SESS_LANGUAGE][5])%></div></td>
                                                        <td width="44%"><div align="right"><b><%=FRMHandler.userFormatStringDecimal(totalBeli)%></b></div></td>
                                                    </tr>
                                                    <tr>
                                                        <td width="56%">
                                                            <div align="right">
                                                                <input type="checkbox" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_INCLUDE_PPN]%>" value="1" <% if(rec.getIncludePpn()==1){%>checked<%}%>><%=textListOrderHeader[SESS_LANGUAGE][12]%>&nbsp;<%=textListOrderHeader[SESS_LANGUAGE][7]%>
                                                                <input type="text"  class="formElemen" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_TOTAL_PPN]%>" value="<%if(ppn != 0.0){%><%=FRMHandler.userFormatStringDecimal(ppn)%><%}else {%><%=FRMHandler.userFormatStringDecimal(defaultPpn)%><%}%>"  size="5" style="text-align:right">&nbsp;<%=textListOrderHeader[SESS_LANGUAGE][13]%>
                                                            </div>
                                                        </td>
                                                        <td width="44%">
                                                            <div align="right">
                                                                <b><%=FRMHandler.userFormatStringDecimal(valuePpn)%></b>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td width="56%"><div align="right"><%=textListOrderHeader[SESS_LANGUAGE][10]%></div></td>
                                                        <% if (rec.getIncludePpn()==1) { %>
                                                          <td width="44%"><div align="right"><b><%=FRMHandler.userFormatStringDecimal(totalBeli)%></b></div></td>
                                                         <%} else {%>
                                                        <td width="44%"><div align="right"><b><%=FRMHandler.userFormatStringDecimal(totalBeliWithPPN)%></b></div></td>
                                                         <%}%>
                                                    </tr>
                                                    <tr>
                                                        <td width="56%"><div align="right"><%=textListOrderHeader[SESS_LANGUAGE][10]%> <%=textListOrderItem[SESS_LANGUAGE][6]%></div></td>
                                                        <td width="44%"><div align="right"><b><%=FRMHandler.userFormatStringDecimal(totalForwarderCost)%></b></div></td>
                                                    </tr>
                                                    <tr> 
                                                        <td width="56%"><div align="right"><%=textListOrderHeader[SESS_LANGUAGE][11]%></div></td>
                                                        <% if (rec.getIncludePpn()==1) { %>
                                                        <td width="44%"><div align="right"><b><%=FRMHandler.userFormatStringDecimal(totalBeli+totalForwarderCost)%></b></div></td>
                                                        <%}else {%>
                                                          <td width="44%"><div align="right"><b><%=FRMHandler.userFormatStringDecimal(totalBeliWithPPN+totalForwarderCost)%></b></div></td>
                                                        <%}%>
                                                    </tr>
                                              </table>
                                            </td>
                                        </tr>
                                        <% } %>
                                        <% } }%>
                                        <tr>
                                          <td valign="top" colspan="3">&nbsp;</td>
                                        </tr>
                                        <tr>
                                            <td colspan="3">
                                                <table width="100%" border="0">
                                                    <%-- update opie-eyek untuk privilage gudang tidak bisa bisa melihat nominal price--%>
                                                    <% if(oidReceiveMaterial!=0 && listMatReceiveItem.size() > 0) { %>
                                                    <%if(privShowQtyPrice){%>
                                                    <tr>
                                                        <td>&nbsp;</td>
                                                        <td>
                                                            <select name="type_print_tranfer">
                                                                <option value="0">Price Cost</option>
                                                                <option value="1">Sell Price</option>
                                                                <option value="2">Cost & Sell Price</option>
                                                                <%if (typeOfBusinessDetail == 2) {%>
                                                                <option value="3">Without Cost & Sell Price</option>
                                                                <%}%>
                                                            </select>
                                                        </td>
                                                    </tr>
                                                    <%}else{%>
                                                        <input type="hidden" name="type_print_tranfer" value="0">
                                                    <%}%>
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
                                                                ctrLine.setTableWidth("80%");
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

                                                                if (rec.getReceiveSource() == PstMatReceive.SOURCE_FROM_RETURN){
                                                                    ctrLine.setBackCommand("javascript:cmdBackReturn()");
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

                                                                if (rec.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_FINAL || rec.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_CLOSED || rec.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_POSTED) {
                                                                    ctrLine.setAddCaption("");
                                                                    ctrLine.setSaveCaption("");
                                                                }

                                                                if (iCommand == Command.SAVE && frmrec.errorSize() == 0) {
                                                                    //iCommand=Command.EDIT;
                                                                }
                                                            %>
                                                            <%=ctrLine.drawImage(iCommand,iErrCode,errMsg)%>
                                                        </td>
                                                        <td width="30%">
                                                            <%if(listMatReceiveItem!=null && listMatReceiveItem.size()>0){%>

                                                          <% if(useForRaditya.equals("1")){
                                                          if(rec.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_CLOSED || rec.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_POSTED){ %>
                                                            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                                                <tr>
                                                                    <td width="45%" nowrap>&nbsp; <a class="btn-primary btn-lg" href="javascript:printForm('<%=oidReceiveMaterial%>')" class="command" > <i class="fa fa-print">&nbsp;<%=textListGlobal[SESS_LANGUAGE][8]%> </i></a></td>
                                                                    <td width="45%" nowrap>&nbsp; <a class="btn-primary btn-lg" href="javascript:printExcel('<%=oidReceiveMaterial%>')" class="command" > <i class="fa fa-file-excel-o">&nbsp;Export Excel </i></a></td>
                                                                </tr>
                                                            </table>
                                                          <%}else{}}else{%>
                                                            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                                                <tr>
                                                                    <td width="45%" nowrap>&nbsp; <a class="btn-primary btn-lg" href="javascript:printForm('<%=oidReceiveMaterial%>')" class="command" > <i class="fa fa-print">&nbsp;<%=textListGlobal[SESS_LANGUAGE][8]%> </i></a></td>
                                                                    <td width="45%" nowrap>&nbsp; <a class="btn-primary btn-lg" href="javascript:printExcel('<%=oidReceiveMaterial%>')" class="command" > <i class="fa fa-file-excel-o">&nbsp;Export Excel </i></a></td>
                                                                </tr>
                                                            </table>
                                                          <%}%>
                                                            <%}%>
                                                       </td>
                                                    </tr>
                                                    <tr>
                                                        <td> &nbsp;</td>
                                                    </tr>
                                                    <tr>
                                                        <td><a href="javascript:viewHistoryTable()">VIEW TABEL HISTORY</a></td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                    </table>
                                    <%
                                        } catch(Exception e) {
                                            System.out.println(e);
                                        }
                                    %>
                                </form>
                                <script language="JavaScript">
                                    document.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_LOCATION_ID]%>.focus();
                                </script>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>

            <tr>
                <td colspan="2" height="20">
                    <%if (menuUsed == MENU_ICON) {%>
                    <%@include file="../../../styletemplate/footer.jsp" %>
                    <%} else {%>
                    <%@ include file = "../../../main/footer.jsp" %>
                    <%}%>

<!--                    <script language="JavaScript">
                        <% if (rec.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT && iCommand == Command.SAVE && iErrCode == 0) { %>
                        addItem();
                        <% }%>
                    </script>-->
                </td>
            </tr>
        </table>
      </div>
    </body>
</html>

<%-- 
    Document   : receive_back_material_item
    Created on : Oct 5, 2019, 11:33:33 AM
    Author     : Dimata 007
--%>

<%@page import="com.dimata.pos.entity.billing.PstBillDetail"%>
<%@page import="com.dimata.pos.entity.billing.Billdetail"%>
<%@page import="com.dimata.pos.entity.billing.PstBillMain"%>
<%@page import="com.dimata.pos.entity.billing.BillMain"%>
<%@page import="com.dimata.posbo.db.DBHandler"%>
<%@page import="com.dimata.hanoman.entity.masterdata.PstMasterType"%>
<%@page import="com.dimata.hanoman.entity.masterdata.MasterType"%>
<%@page import="com.dimata.hanoman.entity.masterdata.PstMasterGroup"%>
<%@page import="com.dimata.hanoman.entity.masterdata.MasterGroup"%>
<%@page import="com.dimata.posbo.session.masterdata.SessMaterial"%>
<%@page import="com.dimata.posbo.session.masterdata.SessMemberReg"%>
<%@page import="com.dimata.posbo.entity.search.SrcMemberReg"%>
<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*,
         com.dimata.posbo.entity.warehouse.PstReceiveStockCode,
         com.dimata.gui.jsp.ControlList,
         com.dimata.util.Command,
         com.dimata.gui.jsp.ControlDate,
         com.dimata.qdep.form.FRMHandler,
         com.dimata.qdep.entity.I_PstDocType,
         com.dimata.qdep.form.FRMQueryString,
         com.dimata.qdep.form.FRMMessage,
         com.dimata.gui.jsp.ControlLine,
         com.dimata.common.entity.contact.PstContactClass,
         com.dimata.common.entity.contact.PstContactList,
         com.dimata.common.entity.contact.ContactList,
         com.dimata.gui.jsp.ControlCombo,
         com.dimata.common.entity.location.Location,
         com.dimata.common.entity.location.PstLocation,
         com.dimata.posbo.entity.warehouse.MatReceiveItem,
         com.dimata.posbo.entity.warehouse.PstMatReceive,
         com.dimata.posbo.form.warehouse.FrmMatReceiveItem,
         com.dimata.posbo.entity.masterdata.*,
         com.dimata.posbo.form.warehouse.CtrlMatReceive,
         com.dimata.posbo.form.warehouse.FrmMatReceive,
         com.dimata.posbo.entity.warehouse.MatReceive,
         com.dimata.posbo.form.warehouse.CtrlMatReceiveItem,
         com.dimata.posbo.entity.warehouse.PstMatReceiveItem,
         com.dimata.common.entity.payment.PstCurrencyType,
         com.dimata.common.entity.payment.CurrencyType" %>

<%@ include file = "../../../main/javainit.jsp" %>
<%//
    int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE);
    int appObjCodeShowQtyAndPrice = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE_QTY_AND_PRICE);
%>
<%@ include file = "../../../main/checkuser.jsp" %>
<%//
    boolean privShowQtyPrice = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeShowQtyAndPrice, AppObjInfo.COMMAND_VIEW));
    boolean privFinal = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_FINAL));
%>

<%!//
    static boolean bEnableExpiredDate = false;

    public static final String textListGlobal[][] = {
        {"Penerimaan", "Dari Pembelian", "Pencarian", "Daftar", "Edit", "Dengan PO", "Tanpa PO", "Tidak ada item penerimaan barang ...", "Dari Penerimaan Kembali"},
        {"Receive", "From Purchase", "Search", "List", "Edit", "With PO", "Without PO", "There is no goods receive item ...", "From Receive Back"}
    };

    /* this constant used to list text of listHeader */
    public static final String textListOrderHeader[][] = {
        {"Kode", "Lokasi", "Tanggal", "Supplier", "Status", "Keterangan", "Nota Supplier", "Total PPN", "Mata Uang", "Terms", "Days", "Rate", "Tipe Item Penerimaan", "Tipe Penerimaan", "HEL", "Nilai Tukar", "Berat Nota", "Total Nota", "Kepemilikan", "Berat 24k","Waktu"},
        {"Code", "Location", "Date", "Supplier", "Status", "Remark", "Supplier Invoice", "Total VAT", "Currency", "Terms", "Days", "Rate", "Receive Item Type", "Receive Type", "HEL", "Weight", "Exchange Value", "Total Nota", "Own", "Weight 24k","Time"}
    };

    /* this constant used to list text of listMaterialItem */
    public static final String textListOrderItem[][] = {
        {"No", "Sku", "Nama Barang", "Kadaluarsa", "Unit Stock", "Harga Beli", "Ongkos Kirim", "Mata Uang", "Qty Stock", "Total Beli", "Diskon Terakhir %",//10
            "Diskon1 %", "Diskon2 %", "Discount Nominal", "Qty Entri", "Unit Konversi", "Hapus", "Bonus", "Berat (gr)", "Keterangan", "Sorting",
            "Warna", "Etalase"},//19
        {"No", "Code", "Name", "Expired Date", "Unit", "Cost", "Delivery Cost", "Currency", "Qty", "Total Cost", "last Discount %",
            "Discount1 %", "Discount2 %", "Disc. Nominal", "Qty Entri", "Unit Konversi", "Delete", "Bonus", "Weight", "Remark", "Sorting",
            "Color", "Etalase"}
    };

    public static final String textDelete[][] = {
        {"Apakah Anda Yakin Akan Menghapus Data ?"},
        {"Are You Sure to Delete This Data? "}
    };
%>

<%!
    /**
     * this method used to maintain poMaterialList
     */
    public Vector drawListRetItem(int language, int iCommand, FrmMatReceiveItem frmObject,
            MatReceiveItem objEntity, Vector objectClass, long recItemId, int start,
            boolean privShowQtyPrice, String readOnlyQty, String approot, String syspropDiscount1,
            String syspropDiscount2, String syspropDiscountNominal, String syspropOngkosKirim,
            String syspropBonus, String syspropHargaBeli, int typeOfBusinessDetail, int recItemType,
            String currCode, int recSource, String syspropColor, String syspropEtalase,
            int recType, String syspropTotalBeli, long locationId, String syspropAutoSave) {

        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.addHeader(textListOrderItem[language][0], "1%");//no
        ctrlist.addHeader("Invoice", "1%");//invoice
        ctrlist.addHeader(textListOrderItem[language][1], "17%");//sku
        ctrlist.addHeader("HS Code", "10%");//kategori
        ctrlist.addHeader(textListOrderItem[language][2], "20%");//nama brg
        if (bEnableExpiredDate) {
            ctrlist.addHeader(textListOrderItem[language][3], "8%");
        }
        if (typeOfBusinessDetail != 2) {
            ctrlist.addHeader(textListOrderItem[language][14], "5%");//qty entry
            ctrlist.addHeader(textListOrderItem[language][15], "5%");//unit konv
        }
        if (privShowQtyPrice) {
            ctrlist.addHeader(textListOrderItem[language][8], "7%");//qty            
            ctrlist.addHeader(textListOrderItem[language][4], "5%");//unit                 
            if (typeOfBusinessDetail == 2) {
                ctrlist.addHeader(textListOrderItem[language][18], "5%");//berat
            }
            if (syspropHargaBeli.equals("1") || syspropHargaBeli.equals("2") || syspropHargaBeli.equals("3")) {
                if (syspropHargaBeli.equals("1") || syspropHargaBeli.equals(String.valueOf(recType))) {
                    if (typeOfBusinessDetail == 2) {
                        if (recItemType == Material.MATERIAL_TYPE_EMAS || recItemType == Material.MATERIAL_TYPE_EMAS_LANTAKAN) {
                            ctrlist.addHeader("Harga Emas (" + currCode + ")", "7%");
                        } else {
                            ctrlist.addHeader(textListOrderItem[language][5] + " (" + currCode + ")", "7%");
                        }
                    } else {
                        ctrlist.addHeader(textListOrderItem[language][5], "7%");
                    }
                }
            }
            if (syspropDiscount1.equals("1")) {
                ctrlist.addHeader(textListOrderItem[language][11], "5%");//discount 1
            }
            if (syspropDiscount2.equals("1")) {
                ctrlist.addHeader(textListOrderItem[language][12], "5%");//discount 2
            }
            if (syspropDiscountNominal.equals("1")) {
                ctrlist.addHeader(textListOrderItem[language][13], "7%");//discount nominal
            }
            if (syspropOngkosKirim.equals("1")) {
                if (typeOfBusinessDetail == 2) {
                    ctrlist.addHeader("Oks/Batu (" + currCode + ")", "7%");//ongkos kirim
                } else {
                    ctrlist.addHeader(textListOrderItem[language][6], "7%");//ongkos kirim
                }
            }
            if (syspropBonus.equals("1")) {
                ctrlist.addHeader(textListOrderItem[language][17], "7%");//bonus
            }
            if (syspropTotalBeli.equals("1") || syspropTotalBeli.equals("2") || syspropTotalBeli.equals("3")) {
                if (syspropTotalBeli.equals("1") || syspropTotalBeli.equals(String.valueOf(recType))) {
                    if (typeOfBusinessDetail == 2) {
                        if (recItemType == Material.MATERIAL_TYPE_BERLIAN) {
                            ctrlist.addHeader("Harga Pokok (Rp)", "7%");
                        } else if (recItemType == Material.MATERIAL_TYPE_EMAS || recItemType == Material.MATERIAL_TYPE_EMAS_LANTAKAN) {
                            ctrlist.addHeader("Total HP (Rp)", "7%");//Total HP
                        }
                    } else {
                        ctrlist.addHeader(textListOrderItem[language][9], "7%");
                    }
                }
            }
        } else {
            ctrlist.addHeader(textListOrderItem[language][8], "9%");
            if (syspropDiscount1.equals("1")) {
                ctrlist.addHeader(textListOrderItem[language][11], "9%");
            }
            if (syspropDiscount2.equals("1")) {
                ctrlist.addHeader(textListOrderItem[language][12], "9%");
            }
            if (syspropDiscountNominal.equals("1")) {
                ctrlist.addHeader(textListOrderItem[language][13], "9%");
            }
            if (syspropOngkosKirim.equals("1")) {
                ctrlist.addHeader(textListOrderItem[language][6], "9%");
            }
            ctrlist.addHeader(textListOrderItem[language][9], "9%");
            if (syspropBonus.equals("1")) {
                ctrlist.addHeader(textListOrderItem[language][17], "7%");//bonus
            }
        }
        if (syspropEtalase.equals("1")) {
            ctrlist.addHeader(textListOrderItem[language][22], "7%");//bonus
        }
        if (syspropColor.equals("1")) {
            ctrlist.addHeader(textListOrderItem[language][21], "7%");//bonus
        }
        ctrlist.addHeader(textListOrderItem[language][19], "7%");//keterangan
        if (typeOfBusinessDetail == 2 && recSource == PstMatReceive.SOURCE_FROM_BUYBACK) {
            ctrlist.addHeader(textListOrderItem[language][20], "5%");//sorting
        }
        Vector listGroupMapping = PstMasterGroupMapping.list(0, 0,
                PstMasterGroupMapping.fieldNames[PstMasterGroupMapping.FLD_MODUL] + " = " + PstMasterGroupMapping.MODUL_RECEIVING, "");
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
        ctrlist.addHeader(textListOrderItem[language][16], "1%");//hapus
        /**
         * add opie 28-06-2012 untuk konversi
         */
        String whereUnit = "";
        Vector listBuyUnit = PstUnit.list(0, 1000, whereUnit, "");
        Vector index_value = new Vector(1, 1);
        Vector index_key = new Vector(1, 1);
        index_key.add("-");
        index_value.add("0");
        for (int i = 0; i < listBuyUnit.size(); i++) {
            Unit mateUnit = (Unit) listBuyUnit.get(i);
            index_key.add(mateUnit.getCode());
            index_value.add("" + mateUnit.getOID());
        }

        Vector list = new Vector(1, 1);
        Vector listError = new Vector(1, 1);

        Vector lstData = ctrlist.getData();
        Vector rowx = new Vector(1, 1);
        ctrlist.reset();
        ctrlist.setLinkRow(1);
        int index = -1;
        if (start < 0) {
            start = 0;
        }
        Vector vWarna = PstColor.listAll();
        Vector warna_Val = new Vector(1, 1);
        Vector warna_Key = new Vector(1, 1);
        //warna_Val.add("0");
        //warna_Key.add("-");    
        for (int i = 0; i < vWarna.size(); i++) {
            Color color = (Color) vWarna.get(i);
            warna_Val.add("" + color.getOID());
            warna_Key.add("" + color.getColorCode() + " - " + color.getColorName());
        }

        Vector listKsg = PstKsg.list(0, 0, PstKsg.fieldNames[PstKsg.FLD_LOCATION_ID] + "=" + locationId, PstKsg.fieldNames[PstKsg.FLD_LOCATION_ID] + "," + PstKsg.fieldNames[PstKsg.FLD_CODE]);
        Vector vectKsgVal = new Vector(1, 1);
        Vector vectKsgKey = new Vector(1, 1);
        //vectKsgKey.add("Semua");
        //vectKsgVal.add("0");
        for (int i = 0; i < listKsg.size(); i++) {
            Ksg matKsg = (Ksg) listKsg.get(i);
            Location l = new Location();
            if (matKsg.getLocationId() != 0) {
                try {
                    l = PstLocation.fetchExc(matKsg.getLocationId());
                } catch (Exception exc) {
                }
            }
            vectKsgKey.add(matKsg.getCode() + " - " + l.getCode());
            vectKsgVal.add("" + matKsg.getOID());
        }

        int priceReadOnly = Integer.parseInt(PstSystemProperty.getValueByName("PRICE_RECEIVING_READONLY"));
        String read = "";
        if (priceReadOnly == 1) {
            read = "readonly";
        }
        for (int i = 0; i < objectClass.size(); i++) {
            Vector temp = (Vector) objectClass.get(i);
            MatReceiveItem recItem = (MatReceiveItem) temp.get(0);
            Material mat = (Material) temp.get(1);
            Unit unit = (Unit) temp.get(2);
            CurrencyType currencyType = (CurrencyType) temp.get(3);

            Category cat = new Category();
            BillMain bm = new BillMain();
            try {
                if (recItem.getCashBillMainId() != 0) {
                    bm = PstBillMain.fetchExc(recItem.getCashBillMainId());
                }
                if (mat.getCategoryId() != 0) {
                    cat = PstCategory.fetchExc(mat.getCategoryId());
                }
            } catch (Exception e) {}
            
            //added by dewok 2018 for jewelry
            String itemName = SessMaterial.setItemNameForLitama(recItem.getMaterialId());

            rowx = new Vector();
            start = start + 1;
            double totalForwarderCost = recItem.getForwarderCost() * recItem.getQty();
            Unit untiKonv = new Unit();
            try {
                untiKonv = PstUnit.fetchExc(recItem.getUnitKonversi());
            } catch (Exception ex) {

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

            if (recItemId == recItem.getOID()) {
                index = i;
            }
            if (index == i && (iCommand == Command.EDIT || iCommand == Command.ASK)) {
                rowx.add("" + start);
                rowx.add(""+bm.getInvoiceNumber());
                rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID] + "\" value=\"" + recItem.getMaterialId()
                        + "\"><input type=\"text\" size=\"15\" name=\"matCode\" value=\"" + mat.getSku() + "\" class=\"formElemen\" onKeyDown=\"javascript:keyDownCheck(event)\">"); // <a href=\"javascript:cmdCheck()\">CHK</a>
                //update opie-eyek 20130812
                rowx.add(""+cat.getCode());
                if (privShowQtyPrice) {
                    if (mat.getCurrBuyPrice() < recItem.getCost()) {
                        if (typeOfBusinessDetail == 2) {
                            rowx.add("<input type=\"text\" size=\"20\" name=\"matItem\" value=\"" + itemName + "\" class=\"hiddenText\" readOnly><img src='../../../images/DOTreddotANI.gif'><blink><a href=\"javascript:cmdHargaJual('" + String.valueOf(recItem.getMaterialId()) + "')\"><font color='#FF0000'>[Edit]</font></a></blink>");
                        } else {
                            rowx.add("<input type=\"text\" size=\"20\" name=\"matItem\" value=\"" + mat.getName() + "\" class=\"hiddenText\" readOnly><img src='../../../images/DOTreddotANI.gif'><blink><a href=\"javascript:cmdHargaJual('" + String.valueOf(recItem.getMaterialId()) + "')\"><font color='#FF0000'>[Edit]</font></a></blink>");
                        }
                    } else {
                        if (typeOfBusinessDetail == 2) {
                            rowx.add("<input type=\"text\" size=\"20\" name=\"matItem\" value=\"" + itemName + "\" class=\"hiddenText\" readOnly> <a href=\"javascript:cmdHargaJual('" + String.valueOf(recItem.getMaterialId()) + "')\">[Edit]</a>");
                        } else {
                            rowx.add("<input type=\"text\" size=\"20\" name=\"matItem\" value=\"" + mat.getName() + "\" class=\"hiddenText\" readOnly><a href=\"javascript:cmdHargaJual('" + String.valueOf(recItem.getMaterialId()) + "')\">&nbsp;&nbsp;[Edit]</a>");
                        }
                    }
                } else {
                    rowx.add("<input type=\"text\" size=\"20\" name=\"matItem\" value=\"" + mat.getName() + "\" class=\"hiddenText\" readOnly>");
                }

                if (bEnableExpiredDate) {
                    rowx.add(ControlDate.drawDateWithStyle(frmObject.fieldNames[frmObject.FRM_FIELD_EXPIRED_DATE], (recItem.getExpiredDate() == null) ? new Date() : recItem.getExpiredDate(), 5, -5, "formElemen", ""));
                }

                //add opie-eyek 20140108 untuk konversi satuan
                if (typeOfBusinessDetail != 2) {
                    if (readOnlyQty == "readonly") {
                        rowx.add("<div align=\"center\"><input type=\"hidden\" size=\"7\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_QTY_INPUT] + "\" value=\"" + recItem.getQtyEntry() + "" + "\" class=\"formElemen\" onkeyup=\"javascript:change(this.value)\" " + readOnlyQty + "></div>"
                                + "<input type=\"text\" size=\"7\" name=\"ccc\" value=\"" + recItem.getQtyEntry() + "" + "\" class=\"formElemen\" " + readOnlyQty + ">");
                    } else {
                        rowx.add("<div align=\"center\"><input type=\"text\" size=\"7\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_QTY_INPUT] + "\" value=\"" + recItem.getQtyEntry() + "" + "\" class=\"formElemen\" onkeyup=\"javascript:change(this.value)\"</div>");
                    }
                    rowx.add("<div align=\"center\">" + ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID_KONVERSI], null, "" + untiKonv.getOID(), index_value, index_key, "onChange=\"javascript:showData(this.value)\"", "formElemen") + " </div>");
                }

                if (privShowQtyPrice) {
                    if (readOnlyQty == "readonly") {
                        rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_QTY] + "\" value=\"" + FRMHandler.userFormatStringDecimal(recItem.getQty()) + "\" class=\"formElemen\" " + read + "  onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\" " + readOnlyQty + " ></div>");
                    } else {
                        rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_QTY] + "\" value=\"" + FRMHandler.userFormatStringDecimal(recItem.getQty()) + "\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                    }
                    rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID] + "\" value=\"" + recItem.getUnitId()
                            + "\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\"" + unit.getCode() + "\" class=\"hiddenText\" readOnly>");

                    if (typeOfBusinessDetail == 2) {
                        rowx.add("<div align=\"right\"><input type=\"text\" onKeyUp=\"javascript:cntTotal(this, event)\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_BERAT] + "\" value=\"" + recItem.getBerat() + "\" class=\"formElemen\" style=\"text-align:right\"></div>");
                    }
                    if (syspropHargaBeli.equals("1") || syspropHargaBeli.equals("2") || syspropHargaBeli.equals("3")) {
                        if (syspropHargaBeli.equals("1") || syspropHargaBeli.equals(String.valueOf(recType))) {
                            if (typeOfBusinessDetail == 2) {
                                if (recItemType == Material.MATERIAL_TYPE_EMAS || recItemType == Material.MATERIAL_TYPE_EMAS_LANTAKAN) {
                                    rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID] + "\" value=\"" + recItem.getCurrencyId() + "><div align=\"right\"><input type=\"text\" size=\"10\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_COST] + "\" value='" + String.format("%,.0f", recItem.getCost()) + "' class=\"formElemen hiddenText\" " + read + " onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                                } else if (recItemType == Material.MATERIAL_TYPE_BERLIAN) {
                                    rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID] + "\" value=\"" + recItem.getCurrencyId() + "><div align=\"right\"><input type=\"text\" size=\"10\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_COST] + "\" value='" + String.format("%,.0f", recItem.getCost()) + "' class=\"formElemen\" " + read + " onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                                }
                            } else {
                                rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID] + "\" value=\"" + recItem.getCurrencyId() + "><div align=\"right\"><input type=\"text\" size=\"10\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_COST] + "\" value=\"" + FRMHandler.userFormatStringDecimal(recItem.getCost()) + "\" class=\"formElemen\" " + read + " onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                            }
                        }
                    }
                    if (syspropDiscount1.equals("1")) {
                        rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT] + "\" value=\"" + FRMHandler.userFormatStringDecimal(recItem.getDiscount()) + "\" class=\"formElemen\" " + read + "  onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                    }
                    if (syspropDiscount2.equals("1")) {
                        rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT2] + "\" value=\"" + FRMHandler.userFormatStringDecimal(recItem.getDiscount2()) + "\" class=\"formElemen\" " + read + "  onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                    }
                    if (syspropDiscountNominal.equals("1")) {
                        rowx.add("<div align=\"right\"><input type=\"text\" size=\"10\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_DISC_NOMINAL] + "\" value=\"" + FRMHandler.userFormatStringDecimal(recItem.getDiscNominal()) + "\" class=\"formElemen\" " + read + "  onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                    }
                    if (syspropOngkosKirim.equals("1")) {
                        rowx.add("<div align=\"right\"><input type=\"text\" size=\"10\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_FORWARDER_COST] + "\" value=\"" + FRMHandler.userFormatStringDecimal(recItem.getForwarderCost()) + "\" class=\"formElemen\" " + read + "  onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                    }
                    if (syspropBonus.equals("1")) {
                        rowx.add("<div align=\"left\"><input type=\"hidden\"  name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_BONUS] + "\" value=\"0\"> <input type=\"checkbox\"  name=\"typeBonus\" value=\"1\" onClick=\"javascript:checkBonus()\" >Bonus</div>");
                    }
                    if (syspropTotalBeli.equals("1") || syspropTotalBeli.equals("2") || syspropTotalBeli.equals("3")) {
                        if (syspropTotalBeli.equals("1") || syspropTotalBeli.equals(String.valueOf(recType))) {
                            if (typeOfBusinessDetail == 2) {
                                rowx.add("<div align=\"right\"><input type=\"text\" size=\"10\" name=\"total_cost\" value=\"" + String.format("%,.0f", recItem.getTotal() + totalForwarderCost) + "\" class=\"hiddenText\" readOnly></div>"
                                        + "<input type=\"hidden\" size=\"15\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL] + "\" value=\"" + FRMHandler.userFormatStringDecimal(recItem.getTotal()) + "\">");
                            } else {
                                rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\"total_cost\" value=\"" + FRMHandler.userFormatStringDecimal(recItem.getTotal() + totalForwarderCost) + "\" class=\"hiddenText\" readOnly></div>"
                                        + "<input type=\"hidden\" size=\"15\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL] + "\" value=\"" + FRMHandler.userFormatStringDecimal(recItem.getTotal()) + "\">");
                            }
                        }
                    }
                } else {
                    rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_QTY] + "\" value=\"" + FRMHandler.userFormatStringDecimal(recItem.getQty()) + "\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                    if (typeOfBusinessDetail == 2) {
                        rowx.add("<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\"total_cost\" value=\"" + FRMHandler.userFormatStringDecimal(recItem.getTotal() + totalForwarderCost) + "\" class=\"hiddenText\" readOnly></div>");
                    } else {
                        rowx.add("<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\"total_cost\" value=\"" + FRMHandler.userFormatStringDecimal(recItem.getTotal() + totalForwarderCost) + "\" class=\"hiddenText\" readOnly></div>");
                    }
                    rowx.add("<input type=\"hidden\" size=\"15\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL] + "\" value=\"" + FRMHandler.userFormatStringDecimal(recItem.getTotal()) + "\">");
                    rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID] + "\" value=\"" + recItem.getCurrencyId() + "><div align=\"right\">");
                    rowx.add("<input type=\"hidden\" size=\"10\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_COST] + "\" value=\"" + FRMHandler.userFormatStringDecimal(recItem.getCost()) + "\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                    if (syspropDiscount1.equals("1")) {
                        rowx.add("<div align=\"right\"><input type=\"hidden\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT] + "\" value=\"" + FRMHandler.userFormatStringDecimal(recItem.getDiscount()) + "\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                    }
                    if (syspropDiscount2.equals("1")) {
                        rowx.add("<div align=\"right\"><input type=\"hidden\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT2] + "\" value=\"" + FRMHandler.userFormatStringDecimal(recItem.getDiscount2()) + "\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                    }
                    if (syspropDiscountNominal.equals("1")) {
                        rowx.add("<div align=\"right\"><input type=\"hidden\" size=\"10\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_DISC_NOMINAL] + "\" value=\"" + FRMHandler.userFormatStringDecimal(recItem.getDiscNominal()) + "\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                    }
                    if (syspropOngkosKirim.equals("1")) {
                        rowx.add("<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_FORWARDER_COST] + "\" value=\"" + FRMHandler.userFormatStringDecimal(recItem.getForwarderCost()) + "\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                    }
                    if (syspropBonus.equals("1")) {
                        rowx.add("<div align=\"left\"><input type=\"hidden\"  name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_BONUS] + "\" value=\"0\"> <input type=\"checkbox\"  name=\"typeBonus\" value=\"1\" onClick=\"javascript:checkBonus()\" >Bonus</div>");
                    }
                }
                if (syspropEtalase.equals("1")) {
                    rowx.add(ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_GONDOLA_ID], null, "" + recItem.getGondolaId(), vectKsgVal, vectKsgKey, ""));
                }
                if (syspropColor.equals("1")) {
                    rowx.add(ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_COLOR_ID], null, "" + recItem.getColorId(), warna_Val, warna_Key, ""));
                }
                rowx.add("<input type=\"text\" size=\"10\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_REMARK] + "\" value=\"" + recItem.getRemark() + "\" class=\"formElemen\">");
                if (typeOfBusinessDetail == 2 && recSource == PstMatReceive.SOURCE_FROM_BUYBACK) {
                    rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_PREV_MATERIAL_ID] + "\" value=\"" + recItem.getPrevMaterialId()
                            + "\"> " + ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_SORTING_STATUS], null, "" + recItem.getSortStatus(), PstMatReceiveItem.getSortingVal(), PstMatReceiveItem.getSortingKey(), ""));
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
                        if (listType.size() > 0) {
                            for (int n = 0; n < listType.size(); n++) {
                                MasterType masterType = (MasterType) listType.get(n);
                                vValue.add("" + masterType.getOID());
                                vKey.add(masterType.getMasterName());
                            }
                        }
                        rowx.add("<input type=\"hidden\" name=\"groupSize\" value=\"" + listGroupMapping.size()
                                + "\"> " + ControlCombo.draw("MASTER_GROUP" + masterGroup.getTypeGroup(), null, "" + oidMapping, vValue, vKey, ""));
                    }
                }
                rowx.add("<div align=\"right\"></div>");//hapus
            } else {
                rowx.add("" + start + "");
                rowx.add(""+bm.getInvoiceNumber());
                rowx.add("<a href=\"javascript:cmdEdit('" + String.valueOf(recItem.getOID()) + "')\">" + mat.getSku() + "</a>");
                rowx.add(cat.getCode());
                //update opie-eyek 20130812
                if (privShowQtyPrice) {
                    if (mat.getCurrBuyPrice() < recItem.getCost()) {
                        if (typeOfBusinessDetail == 2) {
                            rowx.add(itemName + " <a href=\"javascript:cmdHargaJual('" + String.valueOf(recItem.getMaterialId()) + "')\"><img src='../../../images/DOTreddotANI.gif'><font color='#FF0000'>[Edit]</font></a>");
                        } else {
                            rowx.add(mat.getName() + "<a href=\"javascript:cmdHargaJual('" + String.valueOf(recItem.getMaterialId()) + "')\">&nbsp;&nbsp;<img src='../../../images/DOTreddotANI.gif'><font color='#FF0000'>[Edit]</font></a>");
                        }
                    } else {
                        if (typeOfBusinessDetail == 2) {
                            rowx.add(itemName + " <a href=\"javascript:cmdHargaJual('" + String.valueOf(recItem.getMaterialId()) + "')\">[Edit]</a>");
                        } else {
                            rowx.add(mat.getName() + "<a href=\"javascript:cmdHargaJual('" + String.valueOf(recItem.getMaterialId()) + "')\">&nbsp;&nbsp;[Edit]</a>");
                        }
                    }
                } else {
                    rowx.add(mat.getName());
                }

                if (bEnableExpiredDate) {
                    rowx.add("<div align=\"center\">" + Formater.formatDate(recItem.getExpiredDate(), "dd-MM-yyyy") + "</div>");
                }
                if (typeOfBusinessDetail != 2) {
                    rowx.add("<div align=\"center\">" + recItem.getQtyEntry() + "</div>");
                    rowx.add("<div align=\"center\">" + untiKonv.getCode() + "</div>");
                }

                if (privShowQtyPrice) {
                    if (typeOfBusinessDetail == 2) {
                        rowx.add("<div align=\"right\">" + String.format("%,.0f", recItem.getQty()) + "</div>");
                    } else {
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
                            rowx.add("<div align=\"center\">" + FRMHandler.userFormatStringDecimal(recItem.getQty()) + "</div>");
                        }

                    }

                    rowx.add("<div align=\"center\">" + unit.getCode() + "</div>");

                    if (typeOfBusinessDetail == 2) {
                        rowx.add("<div align=\"right\">" + String.format("%,.3f", recItem.getBerat()) + "</div>");
                    }
                    if (syspropHargaBeli.equals("1") || syspropHargaBeli.equals("2") || syspropHargaBeli.equals("3")) {
                        if (syspropHargaBeli.equals("1") || syspropHargaBeli.equals(String.valueOf(recType))) {
                            if (typeOfBusinessDetail == 2) {
                                rowx.add("<div align=\"right\">" + String.format("%,.0f", recItem.getCost()) + "</div>");
                            } else {
                                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(recItem.getCost()) + "</div>");
                            }
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
                            rowx.add("<div align=\"right\">" + String.format("%,.0f", recItem.getForwarderCost()) + "</div>");
                        } else {
                            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(recItem.getForwarderCost()) + "</div>");
                        }
                    }
                }

                if (syspropBonus.equals("1")) {
                    rowx.add("<div align=\"right\"></div>");//bonus
                }
                if (privShowQtyPrice) {
                    if (syspropTotalBeli.equals("1") || syspropTotalBeli.equals("2") || syspropTotalBeli.equals("3")) {
                        if (syspropTotalBeli.equals("1") || syspropTotalBeli.equals(String.valueOf(recType))) {
                            if (typeOfBusinessDetail == 2) {
                                rowx.add("<div align=\"right\">" + String.format("%,.0f", recItem.getTotal() + totalForwarderCost) + "</div>");
                            } else {
                                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(recItem.getTotal() + totalForwarderCost) + "</div>");
                            }
                        }
                    }
                }
                if (syspropEtalase.equals("1")) {
                    rowx.add("<div align=\"right\">" + ksg.getName() + "</div>");
                }
                if (syspropColor.equals("1")) {
                    rowx.add("<div align=\"right\">" + color.getColorName() + "</div>");
                }
                rowx.add("<div align=\"\">" + recItem.getRemark() + "</div>");
                if (typeOfBusinessDetail == 2 && recSource == PstMatReceive.SOURCE_FROM_BUYBACK) {
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
                // add by fitra 17-05-2014
                rowx.add(" <div align=\"center\"> <a href=\"javascript:cmdNewDelete('" + String.valueOf(recItem.getOID()) + "')\"><img src=" + approot + "/images/x3.png align=\"center\" ></a></div>");

            }
            lstData.add(rowx);
        }

        rowx = new Vector();

        if (readOnlyQty == "readonly") {

        } else {
            if (iCommand == Command.ADD || (iCommand == Command.SAVE && frmObject.errorSize() > 0)) {
                rowx.add("" + (start + 1));
                rowx.add("");
                rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID] + "\" value=\"" + ""
                        + "\"><input type=\"text\" id='barcode' size=\"15\" name=\"matCode\" value=\"" + "" + "\" class=\"formElemen\" " + (!syspropAutoSave.equals("1") ? "onKeyDown=\"javascript:keyDownCheck(event)\"" : "") + "> <a href=\"javascript:cmdCheck()\">CHK</a>");
                rowx.add("");
                rowx.add("<input type=\"text\" size=\"20\" name=\"matItem\" value=\"" + "" + "\" class=\"formElemen\" onKeyDown=\"javascript:keyDownCheck(event)\" id=\"txt_materialname\"> <a href=\"javascript:cmdCheck()\">CHK</a>");
                if (bEnableExpiredDate) {
                    rowx.add(ControlDate.drawDateWithStyle(frmObject.fieldNames[frmObject.FRM_FIELD_EXPIRED_DATE], new Date(), 5, -5, "formElemen", ""));
                }
                //add opie-eyek 20140108 untuk konversi satuan
                if (typeOfBusinessDetail != 2) {
                    rowx.add("<div align=\"center\"><input type=\"text\" size=\"7\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_QTY_INPUT] + "\" value=\"" + "" + "\" class=\"formElemen\" onkeyup=\"javascript:change(this.value)\" </div>");
                    rowx.add("<div align=\"center\">" + ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID_KONVERSI], null, "" + 0, index_value, index_key, "onChange=\"javascript:showData(this.value)\"", "formElemen") + " </div>");
                }
                if (privShowQtyPrice) {
                    rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_QTY] + "\" value=\"" + "" + "\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");

                    rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID] + "\" value=\"" + ""
                            + "\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\"" + "" + "\" class=\"hiddenText\" readOnly>");

                    if (typeOfBusinessDetail == 2) {
                        rowx.add("<input type=\"text\" onKeyUp=\"javascript:cntTotal(this, event)\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_BERAT] + "\" value=\"" + "\" class=\"formElemen\" style=\"text-align:right\">");
                    }
                    if (syspropHargaBeli.equals("1") || syspropHargaBeli.equals("2") || syspropHargaBeli.equals("3")) {
                        if (syspropHargaBeli.equals("1") || syspropHargaBeli.equals(String.valueOf(recType))) {
                            if (typeOfBusinessDetail == 2) {
                                if (recItemType == Material.MATERIAL_TYPE_EMAS || recItemType == Material.MATERIAL_TYPE_EMAS_LANTAKAN) {
                                    rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID] + "\" value=\"\"><div align=\"right\"><input type=\"text\" size=\"10\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_COST] + "\" value=\"" + "" + "\" class=\"formElemen hiddenText\" " + read + " onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                                } else {
                                    rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID] + "\" value=\"\"><div align=\"right\"><input type=\"text\" size=\"10\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_COST] + "\" value=\"" + "" + "\" class=\"formElemen\" " + read + " onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                                }
                            } else {
                                rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID] + "\" value=\"\"><div align=\"right\"><input type=\"text\" size=\"10\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_COST] + "\" value=\"" + "" + "\" class=\"formElemen\" " + read + " onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                            }
                        }
                    }
                    if (syspropDiscount1.equals("1")) {
                        rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT] + "\" value=\"" + "" + "\" class=\"formElemen\" " + read + " onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                    }
                    if (syspropDiscount2.equals("1")) {
                        rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT2] + "\" value=\"" + "" + "\" class=\"formElemen\" " + read + " onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                    }
                    if (syspropDiscountNominal.equals("1")) {
                        rowx.add("<div align=\"right\"><input type=\"text\" size=\"10\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_DISC_NOMINAL] + "\" value=\"" + "" + "\" class=\"formElemen\" " + read + " onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                    }
                    if (syspropOngkosKirim.equals("1")) {
                        rowx.add("<div align=\"right\"><input type=\"text\" size=\"10\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_FORWARDER_COST] + "\" value=\"" + "" + "\" class=\"formElemen\" " + read + " onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                    }
                    if (syspropBonus.equals("1")) {
                        rowx.add("<div align=\"left\"><input type=\"hidden\"  name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_BONUS] + "\" value=\"0\"> <input type=\"checkbox\"  name=\"typeBonus\" value=\"1\" onClick=\"javascript:checkBonus()\" >Bonus</div>");
                    }
                    if (syspropTotalBeli.equals("1") || syspropTotalBeli.equals("2") || syspropTotalBeli.equals("3")) {
                        if (syspropTotalBeli.equals("1") || syspropTotalBeli.equals(String.valueOf(recType))) {
                            rowx.add("<div align=\"right\"><input type=\"text\" size=\"10\" name=\"total_cost\" value=\"" + "" + "\" " + read + " onKeyUp=\"javascript:cntTotal3(this, event)\">"
                                    + "<input type=\"hidden\" size=\"15\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL] + "\" value=\"" + "" + "\" class=\"hiddenText\" readOnly></div>");
                        }
                    }
                } else {
                    rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_QTY] + "\" value=\"" + "" + "\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>"
                            + "<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID] + "\" value=\"\">"
                            + "<div align=\"right\"><input type=\"hidden\" size=\"10\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_COST] + "\" value=\"" + "" + "\" class=\"formElemen\" " + read + " onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                    if (syspropDiscount1.equals("1")) {
                        rowx.add("<div align=\"right\"><input type=\"hidden\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT] + "\" value=\"" + "" + "\" class=\"formElemen\" " + read + " onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                    }
                    if (syspropDiscount2.equals("1")) {
                        rowx.add("<div align=\"right\"><input type=\"hidden\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT2] + "\" value=\"" + "" + "\" class=\"formElemen\" " + read + " onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                    }
                    if (syspropDiscountNominal.equals("1")) {
                        rowx.add("<div align=\"right\"><input type=\"hidden\" size=\"10\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_DISC_NOMINAL] + "\" value=\"" + "" + "\" class=\"formElemen\" " + read + " onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                    }
                    if (syspropOngkosKirim.equals("1")) {
                        rowx.add("<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_FORWARDER_COST] + "\" value=\"" + "" + "\" class=\"formElemen\" " + read + " onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                    }
                    rowx.add("<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\"total_cost\" value=\"" + "" + "\" " + read + " onKeyUp=\"javascript:cntTotal3(this, event)\">"
                            + "<input type=\"hidden\" size=\"15\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL] + "\" value=\"" + "" + "\" class=\"hiddenText\" readOnly></div>");
                    if (syspropBonus.equals("1")) {
                        rowx.add("<div align=\"left\"><input type=\"hidden\"  name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_BONUS] + "\" value=\"0\"> <input type=\"checkbox\"  name=\"typeBonus\" value=\"1\" onClick=\"javascript:checkBonus()\" >Bonus</div>");
                    }
                }
                if (syspropEtalase.equals("1")) {
                    rowx.add(ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_GONDOLA_ID], null, "", vectKsgVal, vectKsgKey, ""));
                }
                if (syspropColor.equals("1")) {
                    rowx.add(ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_COLOR_ID], null, "", warna_Val, warna_Key, ""));
                }
                rowx.add("<input type=\"text\" size=\"10\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_REMARK] + "\" value=\"" + "\" class=\"formElemen\">");
                if (typeOfBusinessDetail == 2 && recSource == PstMatReceive.SOURCE_FROM_BUYBACK) {
                    rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_PREV_MATERIAL_ID] + "\" value=\"0 \"> "
                            + ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_SORTING_STATUS], null, null, PstMatReceiveItem.getSortingVal(), PstMatReceiveItem.getSortingKey(), ""));
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

                        if (listType.size() > 0) {
                            for (int n = 0; n < listType.size(); n++) {
                                MasterType masterType = (MasterType) listType.get(n);
                                vValue.add("" + masterType.getOID());
                                vKey.add(masterType.getMasterName());
                            }
                        }
                        rowx.add("<input type=\"hidden\" name=\"groupSize\" value=\"" + listGroupMapping.size()
                                + "\"> " + ControlCombo.draw("MASTER_GROUP" + masterGroup.getTypeGroup(), null, null, vValue, vKey, ""));
                    }
                }
                rowx.add("");
                lstData.add(rowx);
            }
        }

        list.add(ctrlist.draw());
        list.add(listError);
        return list;
    }

%>

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
    int startItem = FRMQueryString.requestInt(request, "start_item");
    int prevCommand = FRMQueryString.requestInt(request, "prev_command");
    int appCommand = FRMQueryString.requestInt(request, "approval_command");
    long oidReceiveMaterial = FRMQueryString.requestLong(request, "hidden_receive_id");
    long oidReceiveMaterialItem = FRMQueryString.requestLong(request, "hidden_receive_item_id");
    long oidMaterial = FRMQueryString.requestLong(request, "hidden_material_id");
    int cekBeratStatus = FRMQueryString.requestInt(request, "cek_berat");
    int findBill = FRMQueryString.requestInt(request, "find_bill");
    Date startDate = FRMQueryString.requestDate(request, "START_DATE");
    Date endDate = FRMQueryString.requestDate(request, "END_DATE");
    
    if (findBill == 0) {
        startDate = new Date();
        endDate = new Date();
    }

    //sysprop
    String sEnableExpiredDate = PstSystemProperty.getValueByName("ENABLE_EXPIRED_DATE");
    bEnableExpiredDate = (sEnableExpiredDate != null && sEnableExpiredDate.equalsIgnoreCase("YES")) ? true : false;
    String syspropDiscount1 = PstSystemProperty.getValueByName("SHOW_DISCOUNT_1");
    String syspropDiscount2 = PstSystemProperty.getValueByName("SHOW_DISCOUNT_2");
    String syspropDiscountNominal = PstSystemProperty.getValueByName("SHOW_DISCOUNT_NOMINAL");
    String syspropOngkosKirim = PstSystemProperty.getValueByName("SHOW_ONGKOS_KIRIM");
    String syspropBonus = PstSystemProperty.getValueByName("SHOW_BONUS");
    String syspropHargaBeli = PstSystemProperty.getValueByName("SHOW_HARGA_BELI");
    String syspropColor = PstSystemProperty.getValueByName("SHOW_COLOR");
    String syspropEtalase = PstSystemProperty.getValueByName("SHOW_ETALASE");
    String syspropTotalBeli = PstSystemProperty.getValueByName("SHOW_TOTAL_BELI");
    String syspropAutoSave = PstSystemProperty.getValueByName("AUTO_SAVE_RECEIVING");

    /**
     * initialization of some identifier
     */
    int iErrCode = FRMMessage.NONE;
    String msgString = "";
    FRMHandler.deFormatStringDecimal(",");
    /**
     * purchasing pr code and title
     */
    String recCode = "";//i_pstDocType.getDocCode(docType);
    String retTitle = "Penerimaan Barang";//i_pstDocType.getDocTitle(docType);
    String recItemTitle = retTitle + " Item";

    /**
     * process on purchase order main
     */
    CtrlMatReceive ctrlMatReceive = new CtrlMatReceive(request);
    iErrCode = ctrlMatReceive.action(Command.EDIT, oidReceiveMaterial);
    FrmMatReceive frmMatReceive = ctrlMatReceive.getForm();
    MatReceive rec = ctrlMatReceive.getMatReceive();

    /**
     * check if document may modified or not
     */
    boolean privManageData = true;

    ControlLine ctrLine = new ControlLine();
    CtrlMatReceiveItem ctrlMatReceiveItem = new CtrlMatReceiveItem(request);
    ctrlMatReceiveItem.setLanguage(SESS_LANGUAGE);
    iErrCode = ctrlMatReceiveItem.action(iCommand, oidReceiveMaterialItem, oidReceiveMaterial, userName, userId);
    int billFound = 0;
    int billAlreadyReceived = 0;
    int billSaved = 0;
    if (findBill == 1) {
        try {
            String whereBill = PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " BETWEEN '" + Formater.formatDate(startDate, "yyyy-MM-dd") + " 00:00:00' AND '" + Formater.formatDate(endDate, "yyyy-MM-dd") + " 23:59:59'"
                    + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + " = " + PstBillMain.DELIVERY_STATUS_NOT_TAKEN;
            Vector<BillMain> listBillNotTaken = PstBillMain.list(0, 0, whereBill, "");
            billFound = listBillNotTaken.size();
            for (BillMain bm : listBillNotTaken) {
                Vector<Billdetail> listItem = PstBillDetail.list(0, 0, PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + " = " + bm.getOID(), "");
                for (Billdetail bd : listItem) {
                    //CEK APAKAH BILL SUDAH PERNAH DI RECEIVE
                    int count = PstMatReceiveItem.getCount(PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_CASH_BILL_MAIN_ID] + " = " + bm.getOID() + " AND " + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_CASH_BILL_DETAIL_ID] + " = " + bd.getOID());
                    if (count > 0) {
                        billAlreadyReceived++;
                        continue;
                    }
                    MatReceiveItem recItem = new MatReceiveItem();
                    recItem.setReceiveMaterialId(rec.getOID());
                    recItem.setCashBillMainId(bm.getOID());
                    recItem.setCashBillDetailId(bd.getOID());
                    recItem.setMaterialId(bd.getMaterialId());
                    recItem.setQty(bd.getQty());
                    recItem.setUnitId(bd.getUnitId());
                    recItem.setCost(bd.getItemPrice());
                    recItem.setDiscNominal(bd.getDisc());
                    recItem.setTotal(bd.getQty() * (bd.getItemPrice() - bd.getDisc()));
                    PstMatReceiveItem.insertExc(recItem);
                    billSaved++;
                }
            }
        } catch (Exception e) {
            
        }
    }
    FrmMatReceiveItem frmMatReceiveItem = ctrlMatReceiveItem.getForm();
    MatReceiveItem recItem = ctrlMatReceiveItem.getMatReceiveItem();
    msgString = ctrlMatReceiveItem.getMessage();
    String materialname = FRMQueryString.requestString(request, "txt_materialname");

    String whereClauseItem = PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + "=" + oidReceiveMaterial
            + " AND " + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_BONUS] + "=0";

    String orderClauseItem = " RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID];
    if (typeOfBusinessDetail == 2) {
        orderClauseItem = " RIGHT(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + ",7) ASC";
    }
    int vectSizeItem = 0;//PstMatReceiveItem.getCount(whereClauseItem);
    int recordToGetItem = 10;

    if (iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST) {
        startItem = ctrlMatReceiveItem.actionList(iCommand, startItem, vectSizeItem, recordToGetItem);
    }

    /**
     * kondisi ini untuk manampilakn form tambah item, setelah proses simpan
     * item
     */
    if (iCommand == Command.ADD || (iCommand == Command.SAVE && iErrCode == 0)) {
        iCommand = Command.ADD;
        /**
         * agar form add item tampil pada list paling akhir
         */
        startItem = ctrlMatReceiveItem.actionList(Command.LAST, startItem, vectSizeItem, recordToGetItem);
    }

    if (startItem < 0) {
        startItem = 0;
    }
    if (recordToGetItem < 0) {
        recordToGetItem = 10;
    }

    Vector listMatReceiveItem = new Vector(); //PstMatReceiveItem.listVectorRecItemComplete(startItem, recordToGetItem, whereClauseItem, orderClauseItem);//PstMatReceiveItem.list(startItem,recordToGetItem,whereClauseItem);
    if (listMatReceiveItem.size() < 1 && startItem > 0) {
        if (vectSizeItem - recordToGetItem > recordToGetItem) {
            startItem = startItem - recordToGetItem;
        } else {
            startItem = 0;
            iCommand = Command.FIRST;
            prevCommand = Command.FIRST;
        }
        //listMatReceiveItem = PstMatReceiveItem.list(startItem,recordToGetItem,whereClauseItem);
        if (startItem < 0) {
            startItem = 0;
        }
        if (recordToGetItem < 0) {
            recordToGetItem = 10;
        }
        listMatReceiveItem = new Vector(); //PstMatReceiveItem.listVectorRecItemComplete(startItem, recordToGetItem, whereClauseItem, orderClauseItem);
    }

    String readonlyQty = "";
    if (typeOfBusiness.equals("3") && privFinal == true) {
        readonlyQty = "readonly";
    }

    if (iCommand == Command.SAVE && iErrCode == 0) {
        iCommand = Command.ADD;
        oidReceiveMaterialItem = 0;
    }
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dimata - ProChain POS</title>
        <script language="JavaScript">
            var recStatus = "<%=rec.getReceiveStatus()%>";
            var posted = "<%=I_DocStatus.DOCUMENT_STATUS_POSTED%>";
            var closed = "<%=I_DocStatus.DOCUMENT_STATUS_CLOSED%>";
            
            function cmdFindBillItemNotTaken() {
                document.frm_recmaterial.command.value = "<%=Command.EDIT%>";
                document.frm_recmaterial.action = "receive_back_material_item.jsp";
                document.frm_recmaterial.submit();
            }

            function cmdEditHargaJual2(oid) {
                document.frm_recmaterial.hidden_material_id.value = oid;
                document.frm_recmaterial.start.value = 0;
                document.frm_recmaterial.approval_command.value = "<%=Command.APPROVE%>";
                document.frm_recmaterial.command.value = "<%=Command.EDIT%>";
                document.frm_recmaterial.action = "<%=approot%>/master/material/material_main.jsp";
                document.frm_recmaterial.submit();
            }

            function cmdEditHargaJual(oid) {
                document.frmvendorsearch.command.value = "<%=Command.EDIT%>";
                document.frmvendorsearch.action = "<%=approot%>/master/material/material_main.jsp";
                document.frmvendorsearch.submit();
            }

            function cmdAdd() {
                document.frm_recmaterial.hidden_receive_item_id.value = "0";
                document.frm_recmaterial.command.value = "<%=Command.ADD%>";
                document.frm_recmaterial.prev_command.value = "<%=prevCommand%>";
                document.frm_recmaterial.action = "receive_back_material_item.jsp";
                if (compareDateForAdd() == true)
                    document.frm_recmaterial.submit();
            }

            function cmdSelectItem(matOid, matCode, matItem, matUnit, matPrice, matUnitId, matCurrencyId, matCurrCode, stockQty) {
                document.forms.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_MATERIAL_ID]%>.value = matOid;
                document.forms.frm_recmaterial.matCode.value = matCode;
                document.forms.frm_recmaterial.matItem.value = matItem;
                document.forms.frm_recmaterial.matUnit.value = matUnit;
                document.forms.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>.value = matPrice;
                document.forms.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID]%>.value = matUnitId;
            <%if (privShowQtyPrice) {%>
                changeFocus(document.forms.frm_recmaterial.matCode);
            <%} else {%>
                changeFocus(document.forms.frm_recmaterial.matQty);
            <%}%>

            }

            function cmdEdit(oidReceiveMaterialItem) {
                if ((recStatus !== posted) && (recStatus !== closed)) {
                    document.frm_recmaterial.hidden_receive_item_id.value = oidReceiveMaterialItem;
                    document.frm_recmaterial.command.value = "<%=Command.EDIT%>";
                    document.frm_recmaterial.prev_command.value = "<%=prevCommand%>";
                    document.frm_recmaterial.action = "receive_back_material_item.jsp";
                    document.frm_recmaterial.submit();
                } else {
                    alert("Document has been <%=I_DocStatus.fieldDocumentStatus[rec.getReceiveStatus()]%> !!!");
                }
            }

            function cmdAsk(oidReceiveMaterialItem) {
                document.frm_recmaterial.hidden_receive_item_id.value = oidReceiveMaterialItem;
                document.frm_recmaterial.command.value = "<%=Command.ASK%>";
                document.frm_recmaterial.prev_command.value = "<%=prevCommand%>";
                document.frm_recmaterial.action = "receive_back_material_item.jsp";
                document.frm_recmaterial.submit();
            }

            function cmdSave() {
                document.frm_recmaterial.command.value = "<%=Command.SAVE%>";
                document.frm_recmaterial.prev_command.value = "<%=prevCommand%>";
                document.frm_recmaterial.action = "receive_back_material_item.jsp";
                document.frm_recmaterial.submit();
            }

            function keyDownCheck(e) {
                var trap = document.frm_recmaterial.trap.value;
                if (e.keyCode == 13 && trap == 0) {
                    document.frm_recmaterial.trap.value = "1";
                }
                // add By fitra
                if (e.keyCode == 13 && trap == "0" && document.frm_recmaterial.matItem.value == "") {
                    document.frm_recmaterial.trap.value = "0";
                    cmdCheck();
                }
                if (e.keyCode == 13 && trap == "1") {
                    document.frm_recmaterial.trap.value = "0";
                    cmdCheck();
                }
                if (e.keyCode == 27) {
                    document.frm_recmaterial.txt_materialname.value = "";
                }
            }

            function cmdConfirmDelete(oidReceiveMaterialItem) {
                document.frm_recmaterial.hidden_receive_item_id.value = oidReceiveMaterialItem;
                document.frm_recmaterial.command.value = "<%=Command.DELETE%>";
                document.frm_recmaterial.prev_command.value = "<%=prevCommand%>";
                document.frm_recmaterial.approval_command.value = "<%=Command.DELETE%>";
                document.frm_recmaterial.action = "receive_back_material_item.jsp";
                document.frm_recmaterial.submit();
            }
            // add by fitra 17-05-2014
            function cmdNewDelete(oid) {
                var msg;
                msg = "<%=textDelete[SESS_LANGUAGE][0]%>";
                var agree = confirm(msg);
                if (agree)
                    return cmdConfirmDelete(oid);
                else
                    return cmdEdit(oid);
            }

            function cmdCancel(oidReceiveMaterialItem) {
                document.frm_recmaterial.hidden_receive_item_id.value = oidReceiveMaterialItem;
                document.frm_recmaterial.command.value = "<%=Command.EDIT%>";
                document.frm_recmaterial.prev_command.value = "<%=prevCommand%>";
                document.frm_recmaterial.action = "receive_back_material_item.jsp";
                document.frm_recmaterial.submit();
            }

            function cmdBack() {
                document.frm_recmaterial.command.value = "<%=Command.EDIT%>";
                document.frm_recmaterial.start_item.value = 0;
                document.frm_recmaterial.action = "receive_back_material.jsp";
                document.frm_recmaterial.submit();
            }

            var winSrcMaterial;
            function cmdCheck() {
            <%if (typeOfBusinessDetail == 2) {%>
                var itemMaterialType = document.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_ITEM_TYPE]%>.value;
                var itemRecType = document.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_TYPE]%>.value;
                var kepemilikan = document.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_KEPEMILIKAN_ID]%>.value;
                var idLoc = document.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_LOCATION_ID]%>.value;
                if (itemMaterialType === "<%=Material.MATERIAL_TYPE_EMAS_LANTAKAN%>") {
                    var strvalue = "materialdosearch.jsp?command=<%=Command.FIRST%>" +
                            "&material_type=" + itemMaterialType +
                            "&location_id=<%=rec.getLocationId()%>" +
                            "&mat_code=" + document.frm_recmaterial.matCode.value +
                            "&txt_materialname=" + document.frm_recmaterial.matItem.value +
                            "&currency_id=<%=rec.getCurrencyId()%>" +
                            "&trans_rate=<%=rec.getTransRate()%>";
                    winSrcMaterial = window.open(strvalue, "material", "height=600,width=800,left=300,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
                    if (window.focus) {
                        winSrcMaterial.focus();
                    }
                } else {
                    var strvalue = "../../../master/material/material_main.jsp?command=<%=Command.ADD%>"
                            + "&direct_penerimaan=1"
                            + "&material_type=" + itemMaterialType
                            + "&kepemilikan_id_input=" + kepemilikan
                            + "&receive_type=" + itemRecType
                            + "&location_id=" + idLoc;
                    winSrcMaterial = window.open(strvalue, "material", "height=600,width=1200,left=100,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
                    if (window.focus) {
                        winSrcMaterial.focus();
                    }
                }
            <%} else {%>
                var strvalue = "materialdosearch.jsp?command=<%=Command.FIRST%>" +
                        "&location_id=<%=rec.getLocationId()%>" +
                        "&mat_code=" + document.frm_recmaterial.matCode.value +
                        "&txt_materialname=" + document.frm_recmaterial.matItem.value +
                        "&currency_id=<%=rec.getCurrencyId()%>" +
                        "&trans_rate=<%=rec.getTransRate()%>" +
                        "&rec_id=" + document.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_MATERIAL_ID]%>.value;
                winSrcMaterial = window.open(strvalue, "material", "height=600,width=800,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
                if (window.focus) {
                    winSrcMaterial.focus();
                }
            <%}%>
            }

            function cmdHargaJual(oidMaterial) {
                if ("<%=typeOfBusinessDetail%>" === "2") {
                    var idLoc = document.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_LOCATION_ID]%>.value;
                    var itemMaterialType = document.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_ITEM_TYPE]%>.value;
                    var strvalue = "<%=approot%>/master/material/material_main.jsp?direct_penerimaan=2&material_type=" + itemMaterialType + "&command=<%=Command.EDIT%>" +
                            "&hidden_material_id=" + oidMaterial +
                            "&location_id=" + idLoc +
                            "&mat_code=" + document.frm_recmaterial.matCode.value +
                            "&txt_materialname=" + document.frm_recmaterial.matItem.value;
                    winSrcMaterial = window.open(strvalue, "material", "height=600,width=1200,left=100,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
                    if (window.focus) {
                        winSrcMaterial.focus();
                    }
                } else {
                    var strvalue = "<%=approot%>/master/material/material_main.jsp?command=<%=Command.EDIT%>" + "&hidden_material_id=" + oidMaterial + "&mat_code=" + document.frm_recmaterial.matCode.value + "&txt_materialname=" + document.frm_recmaterial.matItem.value;
                                winSrcMaterial = window.open(strvalue, "material", "height=600,width=1200,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
                                if (window.focus) {
                                    winSrcMaterial.focus();
                                }
                            }
                        }


                        function keyDownCheck1(e) {
                            if (e.keyCode == 13) {
                                cmdCheck();
                            }
                        }

                        function changeFocus(element) {
                            if (element.name == "matCode") {
            <%if (typeOfBusinessDetail == 2) {%>
                                document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_BERAT]%>.focus();
            <%} else {%>
                                document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY_INPUT]%>.focus();
            <%}%>
                            }
                            else if (element.name == "<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>") {
                                document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISCOUNT]%>.focus();
                            }
                            else if (element.name == "<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISCOUNT]%>") {
                                document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISCOUNT2]%>.focus();
                            }
                            else if (element.name == "<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISCOUNT2]%>") {
                                document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISC_NOMINAL]%>.focus();
                            }
                            else if (element.name == "<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISC_NOMINAL]%>") {
                                document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_FORWARDER_COST]%>.focus();
                            }
                            else if (element.name == "<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_FORWARDER_COST]%>") {

            <%if (typeOfBusinessDetail == 2) {%>
                                var itemMaterialType = document.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_ITEM_TYPE]%>.value;
                                if (itemMaterialType == "<%=Material.MATERIAL_TYPE_EMAS%>" || itemMaterialType == "<%=Material.MATERIAL_TYPE_EMAS_LANTAKAN%>") {
                                    document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_FORWARDER_COST]%>.focus();
                                } else if (itemMaterialType == "<%=Material.MATERIAL_TYPE_BERLIAN%>") {
                                    document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>.focus();
                                }
            <%} else {%>
                                document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.focus();
            <%}%>
                            }
                            else if (element.name == "<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>") {
                                cmdSave();
                            } else if (element.name == "matQty") {
                                document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.focus();
                            } else {
                                cmdSave();
                            }
                        }

                        function cntTotal(element, evt) {
            <%if (typeOfBusinessDetail == 2) {%>
                            cntTotal4();
            <%} else {%>
                            var qty = cleanNumberInt(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.value, guiDigitGroup);
                            var price = cleanNumberInt(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>.value, guiDigitGroup);
                            var forwarder_cost = 0;
                            if (<%=syspropOngkosKirim%> == "1") {
                                forwarder_cost = cleanNumberInt(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_FORWARDER_COST]%>.value, guiDigitGroup);
                            }
                            var lastDisc = 0;
                            if (<%=syspropDiscount1%> == "1") {
                                lastDisc = cleanNumberFloat(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISCOUNT]%>.value, guiDigitGroup, guiDecimalSymbol);
                            }
                            var lastDisc2 = 0;
                            if (<%=syspropDiscount2%> == "1") {
                                lastDisc2 = cleanNumberFloat(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISCOUNT2]%>.value, guiDigitGroup, guiDecimalSymbol);
                            }
                            var lastDiscNom = 0;
                            if (<%=syspropDiscountNominal%> == "1") {
                                lastDiscNom = cleanNumberFloat(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISC_NOMINAL]%>.value, guiDigitGroup, guiDecimalSymbol);
                            }
                            if (qty < 0.0000) {

                                document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.value = 0;

                                return;

                            }


                            if (price == "") {
                                price = 0;
                            }
                            if (forwarder_cost == "") {
                                forwarder_cost = 0;
                            }

                            if (isNaN(lastDisc) || (lastDisc == ""))
                                lastDisc = 0.0;
                            if (isNaN(lastDisc2) || (lastDisc2 == ""))
                                lastDisc2 = 0.0;
                            if (isNaN(lastDiscNom) || (lastDiscNom == ""))
                                lastDiscNom = 0.0;

                            var totaldiscount = price * lastDisc / 100;
                            var totalMinus = price - totaldiscount;
                            var totaldiscount2 = totalMinus * lastDisc2 / 100;
                            var totalCost = (totalMinus - totaldiscount2) - lastDiscNom;
                            //var lastTotal = qty * totalCost;

                            if (!(isNaN(qty)) && (qty != '0')) {
                                //document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_TOTAL]%>.value = parseFloat(price) * qty;
                                //document.frm_recmaterial.total_cost.value = (parseFloat(price) + parseFloat(forwarder_cost)) * qty;
                                document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_TOTAL]%>.value = parseFloat(totalCost) * qty;
                                document.frm_recmaterial.total_cost.value = (parseFloat(totalCost) + parseFloat(forwarder_cost)) * qty;
                            }
                            else {
                                document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.focus();
                            }

                            if (evt.keyCode == 13) {
                                changeFocus(element);
                            }
            <%}%>
                        }

                        function cntTotal2() {
                            var qty = cleanNumberInt(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.value, guiDigitGroup);
                            var price = cleanNumberInt(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>.value, guiDigitGroup);
                            var forwarder_cost = cleanNumberInt(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_FORWARDER_COST]%>.value, guiDigitGroup);
                            var lastDisc = cleanNumberFloat(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISCOUNT]%>.value, guiDigitGroup, guiDecimalSymbol);
                            var lastDisc2 = cleanNumberFloat(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISCOUNT2]%>.value, guiDigitGroup, guiDecimalSymbol);
                            var lastDiscNom = cleanNumberFloat(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISC_NOMINAL]%>.value, guiDigitGroup, guiDecimalSymbol);

                            if (qty < 0.0000) {

                                document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.value = 0;

                                return;

                            }


                            if (price == "") {
                                price = 0;
                            }
                            if (forwarder_cost == "") {
                                forwarder_cost = 0;
                            }

                            if (isNaN(lastDisc) || (lastDisc == ""))
                                lastDisc = 0.0;
                            if (isNaN(lastDisc2) || (lastDisc2 == ""))
                                lastDisc2 = 0.0;
                            if (isNaN(lastDiscNom) || (lastDiscNom == ""))
                                lastDiscNom = 0.0;

                            var totaldiscount = price * lastDisc / 100;
                            var totalMinus = price - totaldiscount;
                            var totaldiscount2 = totalMinus * lastDisc2 / 100;
                            var totalCost = (totalMinus - totaldiscount2) - lastDiscNom;
                            //var lastTotal = qty * totalCost;

                            if (!(isNaN(qty)) && (qty != '0')) {
                                document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_TOTAL]%>.value = parseFloat(totalCost) * qty;
                                document.frm_recmaterial.total_cost.value = (parseFloat(totalCost) + parseFloat(forwarder_cost)) * qty;
                            }
                            else {
                                document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.focus();
                            }
                        }



                        function cntTotal3() {
                            var qty = cleanNumberInt(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.value, guiDigitGroup);
                            var price = cleanNumberInt(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>.value, guiDigitGroup);
                            var forwarder_cost = cleanNumberInt(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_FORWARDER_COST]%>.value, guiDigitGroup);
                            var lastDisc = cleanNumberFloat(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISCOUNT]%>.value, guiDigitGroup, guiDecimalSymbol);
                            var lastDisc2 = cleanNumberFloat(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISCOUNT2]%>.value, guiDigitGroup, guiDecimalSymbol);
                            var lastDiscNom = cleanNumberFloat(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISC_NOMINAL]%>.value, guiDigitGroup, guiDecimalSymbol);
                            var total_cost = cleanNumberFloat(document.frm_recmaterial.total_cost.value, guiDigitGroup, guiDecimalSymbol);

                            if (qty < 0.0000) {

                                document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.value = 0;

                                return;

                            }


                            if (price == "") {
                                price = 0;
                            }
                            if (forwarder_cost == "") {
                                forwarder_cost = 0;
                            }

                            if (isNaN(lastDisc) || (lastDisc == ""))
                                lastDisc = 0.0;
                            if (isNaN(lastDisc2) || (lastDisc2 == ""))
                                lastDisc2 = 0.0;
                            if (isNaN(lastDiscNom) || (lastDiscNom == ""))
                                lastDiscNom = 0.0;

                            var totaldiscount = price * lastDisc / 100;
                            var totalMinus = price - totaldiscount;
                            var totaldiscount2 = totalMinus * lastDisc2 / 100;
                            var totalCost = (totalMinus - totaldiscount2) - lastDiscNom;
                            //var lastTotal = qty * totalCost;

                            if (!(isNaN(qty)) && (qty != '0')) {
                                document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_TOTAL]%>.value = parseFloat(total_cost);
                                document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>.value = (parseFloat(total_cost) + parseFloat(forwarder_cost)) / qty;
                            }
                            else {
                                document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.focus();
                            }
                        }

                        function cntTotal4() {
                            var tipeItem = document.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_ITEM_TYPE]%>.value;
                            var ongkir = cleanNumberInt(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_FORWARDER_COST]%>.value, guiDigitGroup);
                            if (tipeItem === "<%=Material.MATERIAL_TYPE_EMAS%>" || tipeItem === "<%=Material.MATERIAL_TYPE_EMAS_LANTAKAN%>") {
                                var qtyItem = cleanNumberInt(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.value, guiDigitGroup);
                                var beratItem = cleanNumberInt(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_BERAT]%>.value, guiDigitGroup);
                                var hel = cleanNumberInt(document.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_HEL]%>.value, guiDigitGroup);
                                var nilaiTukar = cleanNumberInt(document.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_NILAI_TUKAR]%>.value, guiDigitGroup);
                                var totalHargaShow = (+qtyItem * +beratItem * +hel * (+nilaiTukar / 100)) + +ongkir;
                                var totalHarga = (+qtyItem * +beratItem * +hel * (+nilaiTukar / 100));
                                //alert("qty item = " + qtyItem + "\nberat item = " + beratItem +"\nhel = " + hel + "\nnilai tukar = " + nilaiTukar + "\ntotal harga = (" + qtyItem + " * " + beratItem + " * " + hel + " * " + nilaiTukar + "/100) + " + ongkir + " = " + totalHarga);
                                document.frm_recmaterial.total_cost.value = totalHargaShow.toLocaleString();
                                document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_FORWARDER_COST]%>.value = Number(ongkir).toLocaleString();
                                document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_TOTAL]%>.value = totalHarga;
                            } else if (tipeItem === "<%=Material.MATERIAL_TYPE_BERLIAN%>") {
                                var hargaBeli = cleanNumberInt(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>.value, guiDigitGroup);
                                var rate = document.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_TRANS_RATE]%>.value;
                                var hargaPokokShow = (+hargaBeli * +rate) + +ongkir;
                                var hargaPokok = (+hargaBeli * +rate);
                                //alert("harga beli = " + hargaBeli + "\nrate = " + rate + "\nongkir = " + ongkir + "\nharga pokok = (" + hargaBeli + " * " + rate + ") + " + ongkir + " = " + hargaPokok);
                                document.frm_recmaterial.total_cost.value = hargaPokokShow;
                                document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_TOTAL]%>.value = hargaPokok;
                            }
                        }

                        function change(value) {
                            document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.value = value;
                            document.frm_recmaterial.hidden_qty_input.value = value;
            <%if (typeOfBusinessDetail == 2) {%>
                            cntTotal4();
            <%} else {%>
                            cntTotal2();
            <%}%>

                        }

                        function cmdListFirst() {
                            document.frm_recmaterial.command.value = "<%=Command.FIRST%>";
                            document.frm_recmaterial.prev_command.value = "<%=Command.FIRST%>";
                            document.frm_recmaterial.action = "receive_back_material_item.jsp";
                            document.frm_recmaterial.submit();
                        }

                        function cmdListPrev() {
                            document.frm_recmaterial.command.value = "<%=Command.PREV%>";
                            document.frm_recmaterial.prev_command.value = "<%=Command.PREV%>";
                            document.frm_recmaterial.action = "receive_back_material_item.jsp";
                            document.frm_recmaterial.submit();
                        }

                        function cmdListNext() {
                            document.frm_recmaterial.command.value = "<%=Command.NEXT%>";
                            document.frm_recmaterial.prev_command.value = "<%=Command.NEXT%>";
                            document.frm_recmaterial.action = "receive_back_material_item.jsp";
                            document.frm_recmaterial.submit();
                        }

                        function cmdListLast() {
                            document.frm_recmaterial.command.value = "<%=Command.LAST%>";
                            document.frm_recmaterial.prev_command.value = "<%=Command.LAST%>";
                            document.frm_recmaterial.action = "receive_back_material_item.jsp";
                            document.frm_recmaterial.submit();
                        }

                        function cmdBackList() {
                            document.frm_recmaterial.command.value = "<%=Command.FIRST%>";
                            document.frm_recmaterial.action = "receive_wh_supp_material_list.jsp";
                            document.frm_recmaterial.submit();
                        }

                        function gostock(oid) {
                            document.frm_recmaterial.command.value = "<%=Command.EDIT%>";
                            document.frm_recmaterial.rec_type.value = 2;
                            document.frm_recmaterial.type_doc.value = 2;
                            document.frm_recmaterial.hidden_receive_item_id.value = oid;
                            //    document.frm_recmaterial.action="rec_df_stockcode.jsp";
                            document.frm_recmaterial.action = "rec_wh_stockcode.jsp";
                            document.frm_recmaterial.submit();
                        }

                        //------------------------- END JAVASCRIPT FUNCTION FOR PO ITEM -----------------------


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
        </script>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <%if (menuUsed == MENU_ICON) {%>
        <link href="../../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
        <%}%>
        <link rel="stylesheet" href="../../../styles/main.css" type="text/css">
        <link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
        <script src="../../../styles/jquery.min.js"></script>
        <script type="text/javascript">
                        function showData(value) {
                            var qtyInput = document.frm_recmaterial.hidden_qty_input.value;
                            var oidUnit = document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID]%>.value;
                            var oidKonversiUnit = value;
                            checkAjax(oidKonversiUnit, oidUnit, qtyInput);
                        }

                        function checkAjax(oidKonversiUnit, oidUnit, qtyInput) {
                            $.ajax({
                                url: "<%=approot%>/servlet/com.dimata.posbo.ajax.CheckKonversiUnit?<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID_KONVERSI]%>=" + oidKonversiUnit + "&<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID]%>=" + oidUnit + "&<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY_INPUT]%>=" + qtyInput + "",
                                type: "POST",
                                async: false,
                                success: function (data) {
                                    document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.value = data;
                                    var qty = document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.value;
                                    var value = cleanNumberInt(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>.value, guiDigitGroup);
                                    if (isNaN(value)) {
                                        value = 0;
                                    }
                                    var total = parseFloat(value) * qty;
                                    if (isNaN(total)) {
                                        total = 0;
                                    }
                                    document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_TOTAL]%>.value = parseFloat(total);
                                    document.frm_recmaterial.total_cost.value = parseFloat(total);
                                    document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>.focus();
                                }
                            });
                        }

                        function checkBonus() {
                            if (document.frm_recmaterial.typeBonus.checked) {
                                document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_BONUS]%>.value = "1";
                            }
                        }

                        function formApproval(recMatId) {
                            var winApproval = window.open("receive_approval_form.jsp?rec_mat_id=" + recMatId, "material", "height=300,width=400,left=500,top=100,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes,resizing=false");
                            if (window.focus) {
                                winApproval.focus();
                            }
                        }
        </script>
        <%--autocomplate add By fitra--%>
        <script type="text/javascript" src="../../../styles/jquery-1.4.2.min.js"></script>
        <script src="../../../styles/jquery.autocomplete.js"></script>
        <link rel="stylesheet" type="text/css" href="../../../styles/style.css" />
    </head>
    <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
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
                            <td height="20" class="mainheader">
                                <%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][8]%> &gt; <%=textListGlobal[SESS_LANGUAGE][4]%> Item
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <form name="frm_recmaterial" method ="post" action="">
                                    <input type="hidden" name="command" value="<%=iCommand%>">
                                    <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                                    <input type="hidden" name="start_item" value="<%=startItem%>">
                                    <input type="hidden" name="rec_type" value="">
                                    <input type="hidden" name="type_doc" value="">
                                    <input type="hidden" name="matQty" value="">
                                    <input type="hidden" name="hidden_receive_id" value="<%=oidReceiveMaterial%>">
                                    <input type="hidden" name="hidden_material_id" value="<%=oidMaterial%>">
                                    <input type="hidden" name="hidden_receive_item_id" value="<%=((iCommand == Command.ADD || iCommand == Command.SAVE) ? 0 : oidReceiveMaterialItem)%>">
                                    <input type="hidden" id="receMatOid" name="<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_RECEIVE_MATERIAL_ID]%>" value="<%=oidReceiveMaterial%>">
                                    <input type="hidden" name="approval_command" value="<%=appCommand%>">
                                    <input type="hidden" name="hidden_qty_unit" value="">
                                    <input type="hidden" name="hidden_qty_input" value="">
                                    <input type="hidden" name="trap" value="">

                                    <table width="100%" cellpadding="1" cellspacing="0">
                                        <tr>
                                            <td valign="top" colspan="3">&nbsp;</td>
                                        </tr>
                                        <tr align="left">
                                            <td colspan="3" class="">
                                                <table width="100%" border="0" cellpadding="1">
                                                    <tr>
                                                        <td width="10%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][0]%></td>
                                                        <%
                                                            String kode = "";
                                                            if (rec.getRecCode() == "") {
                                                                kode = "Kode Otomatis";
                                                            } else {
                                                                kode = rec.getRecCode();
                                                            }
                                                        %>
                                                        <td width="20%" align="left">: <b><%=kode%></b></td>
                                                        
                                                        <td width="10%" align="left" valign=""><%=textListOrderHeader[SESS_LANGUAGE][1]%></td>
                                                        <td width="20%" align="left" valign="">:
                                                            <%
                                                                try {
                                                                    Location location = PstLocation.fetchExc(rec.getLocationId());
                                                                    out.print(location.getName());
                                                                } catch (Exception e) {
                                                                }
                                                            %>
                                                        </td>
                                                        <td width="10%" align="left" valign="">Nomor BC</td>
                                                        <td width="20%" align="left" valign="">: <%=rec.getNomorBc() %></td>
                                                        <td></td>
                                                    </tr>
                                                    <tr>
                                                        <td width="" align="left"><%=textListOrderHeader[SESS_LANGUAGE][2]%></td>
                                                        <td width="" align="left">: <%=Formater.formatDate(rec.getReceiveDate(), "yyyy-MM-dd HH:mm")%></td>
                                                        
                                                        <td width="" align="left"><%=textListOrderHeader[SESS_LANGUAGE][4]%></td>
                                                        <td width="" align="left">: <%=I_DocStatus.fieldDocumentStatus[rec.getReceiveStatus()] %></td>
                                                        
                                                        <td width="" align="left" valign=""><%=textListOrderHeader[SESS_LANGUAGE][5]%></td>                                                        
                                                        <td width="" align="left" valign="">: <%=rec.getRemark()%></td>
                                                    </tr>
                                                    <tr>
                                                        <td width="" align="left">Jenis Dokumen</td>
                                                        <td width="" align="left">: <%=rec.getJenisDokumen() %></td>
                                                        
                                                        <td width="" align="left"><%=textListOrderHeader[SESS_LANGUAGE][8]%></td>
                                                        <td width="" align="left">:
                                                            <%
                                                                String currCode = "";
                                                                try {
                                                                    CurrencyType currencyType = PstCurrencyType.fetchExc(rec.getCurrencyId());
                                                                    out.print(currencyType.getCode());
                                                                    currCode = currencyType.getCode();
                                                                } catch (Exception e) {
                                                                }
                                                            %>
                                                        </td>
                                                        
                                                        <td width="" align="left"><%=textListOrderHeader[SESS_LANGUAGE][11]%></td>
                                                        <td width="" align="left">: <%=rec.getTransRate()%></td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                    </table>
                                    <table width="100%" cellpadding="1" cellspacing="0">
                                        <tr>
                                            <td valign="top">
                                                <table width="100%" cellpadding="1" cellspacing="1">
                                                    <tr>
                                                        <td colspan="3" >
                                                            <table width="100%" border="0" cellspacing="0" cellpadding="0" >
                                                                <tr align="left" valign="top">
                                                                    <td>
                                                                        <hr>
                                                                        <input type="hidden" name="find_bill" value="1">
                                                                        Tanggal Bill : &nbsp;
                                                                        <%=ControlDate.drawDateWithStyle("START_DATE", startDate, 1, -5, "formElemen")%>
                                                                        &nbsp;
                                                                        <b>s/d</b>
                                                                        &nbsp;
                                                                        <%=ControlDate.drawDateWithStyle("END_DATE", endDate, 1, -5, "formElemen")%>
                                                                        <a href="javascript:cmdFindBillItemNotTaken()" class="btn btn-primary"><i class="fa fa-search"></i> Cari Item</a>
                                                                    </td>
                                                                </tr>
                                                                <tr align="left" valign="top">
                                                                    <%
                                                                        Vector listError = new Vector(1, 1);
                                                                        int recType = 1;
                                                                        try {
                                                                    %>
                                                                    <td height="22" valign="middle" colspan="3" id="CONTENT_ITEM">
                                                                        <%
                                                                            if (findBill == 1) {
                                                                                if (billFound == 0) {
                                                                                    out.print("<br><div class='msgerror'>Tidak ada bill ditemukan</div><br>");
                                                                                }
                                                                                if (billAlreadyReceived > 0) {
                                                                                    out.print("<br><div class='msgerror'>Bill di tanggal tersebut sudah di terima kembali</div><br>");
                                                                                }
                                                                                if (billSaved > 0) {
                                                                        %>
                                                                                    <script>cmdBack();</script>
                                                                        <%
                                                                                }
                                                                            }
                                                                        %>
                                                                        
                                                                        <%
                                                                            if (rec.getReceiveType() == 0) {
                                                                                recType = 2;
                                                                            } else if (rec.getReceiveType() == 1) {
                                                                                recType = 3;
                                                                            }
                                                                            //Vector list = drawListRetItem(SESS_LANGUAGE, iCommand, frmMatReceiveItem, recItem, listMatReceiveItem, oidReceiveMaterialItem, startItem, privShowQtyPrice, readonlyQty, approot, syspropDiscount1, syspropDiscount2, syspropDiscountNominal, syspropOngkosKirim, syspropBonus, syspropHargaBeli, typeOfBusinessDetail, rec.getReceiveItemType(), currCode, rec.getReceiveSource(), syspropEtalase, syspropColor, recType, syspropTotalBeli, rec.getLocationId(), syspropAutoSave);
                                                                            //out.println("" + list.get(0));
                                                                            //listError = (Vector) list.get(1);
                                                                        %>
                                                                    </td>
                                                                    <%
                                                                        } catch (Exception e) {
                                                                            System.out.println(e);
                                                                        }
                                                                    %>
                                                                </tr>
                                                                <tr>
                                                                    <td height="22" valign="middle" colspan="3">
                                                                        <a href="javascript:cmdBack()" class="btn btn-primary"><i class="fa fa-arrow-left"></i> Kembali</a>
                                                                    </td>
                                                                </tr>
                                                                <tr align="left" valign="top">
                                                                    <td height="8" align="left" colspan="3" class="command">
                                                                        <span class="command">
                                                                            <%
                                                                                int cmd = 0;
                                                                                if (iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST) {
                                                                                    cmd = iCommand;
                                                                                } else {
                                                                                    if (iCommand == Command.NONE || prevCommand == Command.NONE) {
                                                                                        cmd = Command.FIRST;
                                                                                    } else {
                                                                                        cmd = prevCommand;
                                                                                    }
                                                                                }
                                                                                ctrLine.setLocationImg(approot + "/images");
                                                                                ctrLine.initDefault();
                                                                                out.println(ctrLine.drawImageListLimit(cmd, vectSizeItem, startItem, recordToGetItem));
                                                                            %>
                                                                        </span>
                                                                    </td>
                                                                </tr>
                                                                <%if(!listError.isEmpty()){%>
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
                                                                
                                                                <%--if (privShowQtyPrice) {%>
                                                                <tr align="left" valign="top">
                                                                    <td height="22" colspan="3" valign="middle">
                                                                        <%
                                                                            out.println("&nbsp;&nbsp;&nbsp;<img src='../../../images/DOTreddotANI.gif'><font color='#FF0000'><blink><b>[edit]</b></blink></font>&nbsp; : <b>Edit Harga Jual (Jika Harga Beli + PPN di Master Data Lebih Kecil dari Harga Beli di Dokument Penerimaan Ini)</b><br>");
                                                                        %>
                                                                    </td>
                                                                </tr>
                                                                <%}--%>
                                                                <tr align="left" valign="top">
                                                                    <td height="22" valign="middle" colspan="3">
                                                                        <%
                                                                            ctrLine.setLocationImg(approot + "/images");

                                                                            // set image alternative caption
                                                                            ctrLine.setAddNewImageAlt(ctrLine.getCommand(SESS_LANGUAGE, recCode + " Item", ctrLine.CMD_ADD, true));
                                                                            ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE, recCode + " Item", ctrLine.CMD_SAVE, true));
                                                                            ctrLine.setBackImageAlt(SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE, recCode + " Item", ctrLine.CMD_BACK, true) : ctrLine.getCommand(SESS_LANGUAGE, recCode + " Item", ctrLine.CMD_BACK, true) + " List");
                                                                            ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE, recCode + " Item", ctrLine.CMD_ASK, true));
                                                                            ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE, recCode + " Item", ctrLine.CMD_CANCEL, false));

                                                                            ctrLine.initDefault();
                                                                            ctrLine.setTableWidth("65%");
                                                                            String scomDel = "javascript:cmdAsk('" + oidReceiveMaterialItem + "')";
                                                                            String sconDelCom = "javascript:cmdConfirmDelete('" + oidReceiveMaterialItem + "')";
                                                                            String scancel = "javascript:cmdEdit('" + oidReceiveMaterialItem + "')";
                                                                            ctrLine.setCommandStyle("command");
                                                                            ctrLine.setColCommStyle("command");

                                                                            // set command caption
                                                                            ctrLine.setAddCaption(ctrLine.getCommand(SESS_LANGUAGE, recCode + " Item", ctrLine.CMD_ADD, true));
                                                                            ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE, recCode + " Item", ctrLine.CMD_SAVE, true));
                                                                            ctrLine.setBackCaption(SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE, recCode + " Item", ctrLine.CMD_BACK, true) : ctrLine.getCommand(SESS_LANGUAGE, recCode + " Item", ctrLine.CMD_BACK, true) + " List");
                                                                            ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE, recCode + " Item", ctrLine.CMD_ASK, true));
                                                                            ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE, recCode + " Item", ctrLine.CMD_DELETE, true));
                                                                            ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE, recCode + " Item", ctrLine.CMD_CANCEL, false));

                                                                            if (privDelete) {
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

                                                                            if (privAdd == false) {
                                                                                ctrLine.setAddCaption("");
                                                                            }
                                                                            
                                                                            String strDrawImage = ctrLine.drawImage(iCommand, iErrCode, msgString);%>

                                                                        <%
                                                                            if ((iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST) && strDrawImage.length() == 0) {
                                                                        %>
                                                                        <table width="50%" border="0" cellspacing="2" cellpadding="0">
                                                                            <tr>
                                                                                <%
                                                                                    if (rec.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) {
                                                                                %>
                                                                                <td width="6%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200', '', '<%=approot%>/images/BtnNewOn.jpg', 1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE, recCode + " Item", ctrLine.CMD_ADD, true)%>"></a></td>
                                                                                <td width="47%"><a href="javascript:cmdAdd()"><%=ctrLine.getCommand(SESS_LANGUAGE, recCode + " Item", ctrLine.CMD_ADD, true)%></a></td>
                                                                                    <%
                                                                                        }
                                                                                    %>
                                                                                <td width="6%"><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200', '', '<%=approot%>/images/BtnBackOn.jpg', 1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE, recCode + " Item", ctrLine.CMD_BACK, true)%>"></a></td>
                                                                                <td width="25%"><a href="javascript:cmdBack()"><%=ctrLine.getCommand(SESS_LANGUAGE, recCode + " Item", ctrLine.CMD_BACK, true)%></a></td>
                                                                            </tr>
                                                                        </table>
                                                                        <%
                                                                            } else {
                                                                                //out.println(strDrawImage);
                                                                            }
                                                                        %>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                    </table>
                                </form>
                                <script language="JavaScript">
                                    // add By Fitra
                                    var trap = document.frm_recmaterial.trap.value;
                                    document.frm_recmaterial.trap.value = "0";
                                    document.frm_recmaterial.matCode.focus();
                                </script>                                
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td colspan="2" height="20">

                    <%
                        if (menuUsed == MENU_ICON) {
                    %>

                    <%@include file="../../../styletemplate/footer.jsp" %>

                    <%
                    } else {
                    %>

                    <%@ include file = "../../../main/footer.jsp" %>

                    <%}%>
                </td>
            </tr>
        </table>
    </body>


    <script language="JavaScript">
        document.frmvendorsearch.txt_materialname.focus();
    </script>
    <%--autocomplate add by fitra--%>
    <script>
        jQuery(function () {
            $("#txt_materialname").autocomplete("list.jsp");
        });

        function addItemTransfer(barcode) {
            var command = "<%= Command.SAVE%>";
            var dataFor = "saveReceiveItem";
            var matDisOid = $("#receMatOid").val();
            var dataSend = "command=" + command + "&FRM_FIELD_DATA_FOR=" + dataFor + "&MATERIAL_RECEIVE_ID=" + matDisOid + "&BARCODE=" + barcode
                    + "&START=<%=startItem%>&RECORD=<%=recordToGetItem%>&SESS_LANGUAGE=<%=SESS_LANGUAGE%>"
                    + "&OID_REC_MAT_ITEM=<%=oidReceiveMaterialItem%>&PRIV_SHOW_QTY_PRICE=<%=privShowQtyPrice%>"
                    + "&TYPE_OFF_BUSINESS=<%=typeOfBusinessDetail%>&CURR_CODE=<%=currCode%>&REC_TYPE=<%=recType%>";
            $.ajax({
                type: "POST",
                url: "<%= approot%>/AjaxPenerimaan",
                data: dataSend,
                chace: false,
                dataType: "json",
                success: function (data) {
                    var error = data.FRM_FIELD_ERROR_NUMBER;
                    if (error != 0) {
                        alert(data.FRM_FIELD_RETURN_MESSAGE);
                    }
                    $("#CONTENT_ITEM").html(data.FRM_FIELD_HTML).fadeIn("medium");
                    $("#CONTENT_ITEM").append("<b>TOTAL QTY : " + data.FRM_FIELD_TOTAL_QTY_ENTRY + "</b>");
                    $('#barcode').focus();
                    $('#barcode').keydown(function (e) {
                        if (e.keyCode == 13) {
                            addItemTransfer($(this).val());
                        }
                    });
                }
            }).done(function (data) { //alert("done");
                alert(data.FRM_FIELD_HTML);

            })
        }

        $('#barcode').keydown(function (e) {
            if (e.keyCode == 13) {
                addItemTransfer($(this).val());
            }
        });

    </script>
    <%//
        if (iCommand == Command.DELETE && iErrCode == 0) {
    %>
    <script>cmdBack();</script>
    <%
        }
    %>
    <!-- #EndTemplate -->
</html>

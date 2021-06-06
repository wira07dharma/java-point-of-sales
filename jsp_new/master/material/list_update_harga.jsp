<%@page import="com.dimata.gui.jsp.ControlCheckBox"%>
<%@page import="com.dimata.gui.jsp.ControlCombo"%>
<%@page import="com.dimata.posbo.form.masterdata.CtrlPriceTypeMapping"%>
<%@page import="com.dimata.posbo.form.masterdata.FrmMaterial"%>
<%@page import="com.dimata.common.entity.payment.StandartRate"%>
<%@page import="com.dimata.common.entity.payment.PstStandartRate"%>
<%@page import="com.dimata.posbo.form.masterdata.FrmPriceTypeMapping"%>
<%@page import="com.dimata.common.form.payment.FrmPriceType"%>
<%@page import="com.dimata.common.entity.payment.PriceType"%>
<%@page import="com.dimata.common.entity.payment.PstPriceType"%>
<%@ page language = "java" %>
<%@ page import="
         com.dimata.common.entity.contact.PstContactList,
         com.dimata.common.entity.contact.ContactList,
         com.dimata.gui.jsp.ControlList,
         com.dimata.qdep.form.FRMHandler,
         com.dimata.qdep.form.FRMQueryString,
         com.dimata.qdep.form.FRMMessage,
         com.dimata.gui.jsp.ControlLine,
         com.dimata.util.Command,
         com.dimata.posbo.entity.admin.PstAppUser,
         com.dimata.posbo.entity.masterdata.*,
         com.dimata.posbo.session.masterdata.SessMaterial,
         com.dimata.posbo.form.masterdata.CtrlMaterial,
         com.dimata.posbo.entity.search.SrcMaterial,
         com.dimata.posbo.form.search.FrmSrcMaterial,
         com.dimata.posbo.session.masterdata.SessUploadCatalogPhoto,
         com.dimata.common.entity.location.*"%>
<%@ page import="com.dimata.common.entity.payment.CurrencyType"%>
<%@ page import="com.dimata.common.entity.payment.PstCurrencyType"%>

<%@ include file = "../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_CATALOG);%>
<%@ include file = "../../main/checkuser.jsp" %>
<%    boolean privShowPrice = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_VIEW_VALUE));
%>
<%!//public static final int SESS_LANGUAGE = com.dimata.util.lang.I_Language.LANGUAGE_FOREIGN;
    public static final String textListTitleHeader[][] = {
        {"Daftar Barang", "Barang", "Tidak ada data", "Masuk ke Exception"},
        {"Goods List", "Goods", "No data available", "Go Into Exception"}
    };
%>
<%
    if (userGroupNewStatus == PstAppUser.GROUP_SUPERVISOR) {
        privAdd = false;
        privUpdate = false;
        privDelete = false;
    }
%>

<!-- Jsp Block -->
<%!
    public static final int ADD_TYPE_SEARCH = 0;
    public static final int ADD_TYPE_LIST = 1;

    public static final String textListMaterialHeader[][] = {
        // 0    1       2                          3               4     5      6      7         8      9       10         11       12              13               14           15          16        17   18   19     20        21
        {"No", "Kode", "Barcode", "Kategori / Merk / Nama Barang", "Jenis", "Tipe", "HPP", "Umum", "Kategori", "Merk", "Kode Grup", "Foto", "Mata Uang HPP", "Mata Uang Jual", "Harga Beli", "Margin", "Harga Pokok", "Rp", "$", "MEMBER", "VIV", "HARGA BELI TERAKHIR"},
        {"No", "Code", "Barcode", "Category / Merk / Name", "Jenis", "Type", "Average Price", "Price", "Category", "Merk", "Code Range", "Picture", "Curr. Avg Price", "Curr. Sell", "Buy.PRice", "Margin", "Cost Of Goods Sold", "Rp", "MEMBER", "VIV", "LAST BUYING PRICE"}
    };

//untuk discount Qty mapping
    public static final String textListDiscountQtyHeader[][] = {
        {"No", "Mata Uang", "Tipe Member", "Lokasi", "Start Qty", "To Qty", "Diskon", "Tipe Diskon", "Harga Per Unit", "Harga Pokok"},
        {"No", "Currency", "Member Type", "Location", "Start Qty", "To Qty", "Discount", "Discount type", "Unit Price"}
    };

    /* this constant used to list text of listHeader */
    public static final String textListHeaderReposting[][] = {
        {"Lokasi", "Periode"},
        {"Location", "Period"}
    };

    public String drawList(int language, Vector objectClass, Vector currency, int start, int iCommand, int showImage, String approot, int showUpdateCatalog, SrcMaterial srcMaterial, int start2, int showDiscountQty, int showHpp, int showCurrency, boolean privShowPrice, Vector listTypeHrga, FrmMaterial frmMaterial, int multiLanguageName) {
        String result = "";

        String orderBy = PstMerk.fieldNames[PstMerk.FLD_NAME];
        Vector listMaterialMerk = PstMerk.list(0, 0, "", orderBy);
        Vector vectMerkVal = new Vector(1, 1);
        Vector vectMerkKey = new Vector(1, 1);
        if (listMaterialMerk != null && listMaterialMerk.size() > 0) {
            for (int i = 0; i < listMaterialMerk.size(); i++) {
                Merk matMerk = (Merk) listMaterialMerk.get(i);
                vectMerkVal.add(matMerk.getName());
                vectMerkKey.add("" + matMerk.getOID());
            }
        }
        Vector masterCatAcak = PstCategory.list(0, 0, "", PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID]);
        Vector materGroup = PstCategory.structureList(masterCatAcak);

        Vector listMaterialUnit = PstUnit.list(0, 0, "", orderBy);
        Vector vectUnitVal = new Vector(1, 1);
        Vector vectUnitKey = new Vector(1, 1);
        if (listMaterialUnit != null && listMaterialUnit.size() > 0) {
            for (int i = 0; i < listMaterialUnit.size(); i++) {
                Unit matUnit = (Unit) listMaterialUnit.get(i);
                vectUnitVal.add(matUnit.getName());
                vectUnitKey.add("" + matUnit.getOID());
            }
        }

        String orderSubBy = PstSubCategory.fieldNames[PstSubCategory.FLD_NAME];

        Vector vectSubCatVal = new Vector(1, 1);
        Vector vectSubCatKey = new Vector(1, 1);
        String useSubCategory = PstSystemProperty.getValueByName("USE_SUB_CATEGORY");
        Vector listMaterialSubcategory = new Vector();
        if (useSubCategory.equals("1")) {
            listMaterialSubcategory = PstSubCategory.list(0, 0, "", orderSubBy);
            vectSubCatVal.add(" ");
            vectSubCatKey.add("0");
            if (listMaterialSubcategory != null && listMaterialSubcategory.size() > 0) {
                for (int i = 0; i < listMaterialSubcategory.size(); i++) {
                    SubCategory SubCategory = (SubCategory) listMaterialSubcategory.get(i);
                    vectSubCatVal.add(SubCategory.getName());
                    vectSubCatKey.add("" + SubCategory.getOID());
                }
            }
        }

        Vector listKsg = PstKsg.list(0, 0, "", PstKsg.fieldNames[PstKsg.FLD_CODE]);
        Vector vectKsgVal = new Vector(1, 1);
        Vector vectKsgKey = new Vector(1, 1);
        vectKsgVal.add("-select-");
        vectKsgKey.add("0");
        for (int i = 0; i < listKsg.size(); i++) {
            Ksg matKsg = (Ksg) listKsg.get(i);
            vectKsgVal.add("" + matKsg.getCode() + " - " + matKsg.getName());
            vectKsgKey.add("" + matKsg.getOID());
        }

        if (objectClass != null && objectClass.size() > 0) {
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("100%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("listgentitle");

            /**
             * mendapatkan mata uang default
             */
            CurrencyType defaultCurrencyType = PstCurrencyType.getDefaultCurrencyType();

            ctrlist.addHeader(textListMaterialHeader[language][0], "3%");
            ctrlist.addHeader(textListMaterialHeader[language][1], "7%");
            ctrlist.addHeader(textListMaterialHeader[language][1], "7%");
            if (showImage == 1) {
                ctrlist.dataFormat(textListMaterialHeader[language][11], "3%", "2", "0", "center", "center");
            }
            ctrlist.dataFormat(textListMaterialHeader[language][2], "4%", "2", "0", "center", "midle");
            ctrlist.dataFormat("Print<br>Barcode", "1%", "1", "0", "center", "midle");
            ctrlist.dataFormat("Stock ", "2%", "2", "0", "center", "midle");

            String merkName = PstSystemProperty.getValueByName("NAME_OF_MERK");

            if (multiLanguageName == 1) {
                ctrlist.addHeader("Name English", "10%");
                ctrlist.addHeader("Name Indonesia", "10%");
                ctrlist.addHeader("Name Indian", "10%");
            } else {
                ctrlist.addHeader("Name", "15%");
            }

            ctrlist.addHeader("Category (<a href=\"javascript:addCategory()\">Add New</a>)", "10%");
            if (useSubCategory.equals("1")) {
                ctrlist.addHeader("Sub Category", "10%");
            }
            ctrlist.addHeader("Merk (<a href=\"javascript:addMerk()\">Add New</a>) ", "10%");
            ctrlist.addHeader("Rak", "10%");
            ctrlist.addHeader("Unit<br>Stock (<a href=\"javascript:addUnit()\">Add New</a>) ", "10%");
            ctrlist.addHeader("Buying Unit", "10%");
            ctrlist.addHeader("Contract", "10%");

            //update by fitra
            long oidCurrX = 0;
            try {
                oidCurrX = Long.parseLong((String) com.dimata.system.entity.PstSystemProperty.getValueByName("OID_CURR_FOR_PRICE_SALE"));
            } catch (Exception e) {
                oidCurrX = 0;
            }

            int design = 0;
            try {
                design = Integer.parseInt((String) com.dimata.system.entity.PstSystemProperty.getValueByName("DESIGN_MATERIAL_FOR"));
            } catch (Exception e) {
                design = 0;
            }

            long oidCurrSell = 0;
            oidCurrSell = oidCurrX;
            if (design == 0) {
                oidCurrX = 0;
            }
            Vector listCurrStandardX = PstStandartRate.listCurrStandard(0);
            if (privShowPrice) {
                if (showHpp == 1) {
                    ctrlist.addHeader(textListMaterialHeader[language][6] + " (" + defaultCurrencyType.getCode() + ")", "10%");
                    ctrlist.addHeader(textListMaterialHeader[language][14] + " (" + defaultCurrencyType.getCode() + ")", "10%");
                }

                ctrlist.addHeader(textListMaterialHeader[language][16], "5%");
                ctrlist.addHeader(textListMaterialHeader[language][21], "5%");

                //update by Fitra
                if (listTypeHrga != null && listTypeHrga.size() > 0) {
                    for (int i = 0; i < listTypeHrga.size(); i++) {
                        //DISNI DITAMBAHKAN UNTUK VIEW CURRENCY YANG DITAMPILKAN
                        for (int j = 0; j < listCurrStandardX.size(); j++) {
                            Vector temp = (Vector) listCurrStandardX.get(j);
                            CurrencyType curr = (CurrencyType) temp.get(0);
                            PriceType pricetype = (PriceType) listTypeHrga.get(i);
                            ctrlist.addHeader(pricetype.getName() + " " + curr.getCode(), "5%");
                        }
                    }
                }

            }

            ctrlist.setLinkRow(1);
            ctrlist.setLinkSufix("");
            Vector lstData = ctrlist.getData();
            Vector lstLinkData = ctrlist.getLinkData();
            ctrlist.setLinkPrefix("javascript:cmdEdit('");
            ctrlist.setLinkSufix("')");
            ctrlist.reset();
            int index = -1;
            if (start < 0) {
                start = 0;
            }
            Vector rowx = new Vector();
            Vector list = PstCurrencyType.list(0, 0, PstCurrencyType.fieldNames[PstCurrencyType.FLD_TAB_INDEX] + "=1", "");
            CurrencyType currSell = new CurrencyType();
            if (list != null && list.size() > 0) {
                currSell = (CurrencyType) list.get(0);
            }

            Vector qtyBarcode = new Vector();
            try {
                qtyBarcode = srcMaterial.getQtyPrintBarcode();
            } catch (Exception e) {
            }

            for (int i = 0; i < objectClass.size(); i++) {
                Vector temp = (Vector) objectClass.get(i);
                Material material = (Material) temp.get(0);
                Category category = (Category) temp.get(1);
                Merk merk = (Merk) temp.get(3);
                MaterialStock materialStock = (MaterialStock) temp.get(4);

                rowx = new Vector();

                start = start + 1;

                Material matUpdate = new Material();
                try {
                    matUpdate = PstMaterial.fetchExc(material.getOID());
                } catch (Exception e) {
                }
                rowx.add("" + start + "");
                rowx.add("<a title=\"\" href=\"javascript:cmdEdit('" + material.getOID() + "')\">" + material.getSku() + "</a>"
                        + "<input type=\"hidden\" name=\"" + frmMaterial.fieldNames[frmMaterial.FRM_FIELD_MATERIAL_ID] + "_" + i + "\" value=\"" + material.getOID() + "\">");

                //rowx.add(material.getBarCode());
                rowx.add("<input name =\"" + frmMaterial.fieldNames[frmMaterial.FRM_FIELD_SKU] + "_" + i + "\" type=\"text\" size\"4\"  value=\"" + material.getSku() + "\" class=\"formElemen\">");
                rowx.add("<input name =\"" + frmMaterial.fieldNames[frmMaterial.FRM_FIELD_BARCODE] + "_" + i + "\" type=\"text\" size\"4\"  value=\"" + material.getBarCode() + "\" class=\"formElemen\">");

                String xx = "";
                String loop = "";
                try {
                    xx = (String) qtyBarcode.get(i);
                    if (!xx.equals("")) {
                        //loop = Integer.parseInt(xx);
                    }
                } catch (Exception ex) {
                }

                rowx.add("<input name =\"printbarcode_" + i + "\" type=\"text\" value=\"" + loop + "\" class=\"formElemen\" width=\"40\"> <a href=\"javascript:cmdPrintBarcodeOne('" + material.getOID() + "')\">Print Barcode</a>");

                rowx.add("" + materialStock.getQty());

                String goodName = "";
                if (multiLanguageName == 1) {
                    String[] smartPhonesSplits = material.getName().split("\\;");
                    try {
                        rowx.add("<input name =\"" + frmMaterial.fieldNames[frmMaterial.FRM_FIELD_NAME] + "_" + i + "\" type=\"text\" size\"7\"  value=\"" + smartPhonesSplits[0] + "\" class=\"formElemen\">");
                    } catch (Exception ex) {
                        rowx.add("<input name =\"" + frmMaterial.fieldNames[frmMaterial.FRM_FIELD_NAME] + "_" + i + "\" type=\"text\" size\"7\"  value=\"-\" class=\"formElemen\">");
                    }
                    try {
                        rowx.add("<input name =\"" + frmMaterial.fieldNames[frmMaterial.FRM_FIELD_NAME_1] + "_" + i + "\" type=\"text\" size\"7\"  value=\"" + smartPhonesSplits[1] + "\" class=\"formElemen\">");
                    } catch (Exception ex) {
                        rowx.add("<input name =\"" + frmMaterial.fieldNames[frmMaterial.FRM_FIELD_NAME_1] + "_" + i + "\" type=\"text\" size\"7\"  value=\"-\" class=\"formElemen\">");
                    }
                    try {
                        rowx.add("<input name =\"" + frmMaterial.fieldNames[frmMaterial.FRM_FIELD_NAME_2] + "_" + i + "\" type=\"text\" size\"7\"  value=\"" + smartPhonesSplits[2] + "\" class=\"formElemen\">");
                    } catch (Exception ex) {
                        rowx.add("<input name =\"" + frmMaterial.fieldNames[frmMaterial.FRM_FIELD_NAME_2] + "_" + i + "\" type=\"text\" size\"7\"  value=\"-\" class=\"formElemen\">");
                    }
                } else {
                    rowx.add("<input name =\"" + frmMaterial.fieldNames[frmMaterial.FRM_FIELD_NAME] + "_" + i + "\" type=\"text\" size\"7\"  value=\"" + material.getName() + "\" class=\"formElemen\">");
                }

                rowx.add(ControlCombo.drawParentComboBox(frmMaterial.fieldNames[frmMaterial.FRM_FIELD_CATEGORY_ID] + "_" + i, "formElemen", null, "" + material.getCategoryId(), null, materGroup));
                if (useSubCategory.equals("1")) {
                    rowx.add(ControlCombo.draw(frmMaterial.fieldNames[frmMaterial.FRM_FIELD_SUB_CATEGORY_ID] + "_" + i, "formElemen", null, "" + material.getSubCategoryId(), vectSubCatKey, vectSubCatVal, null));
                }
                rowx.add(ControlCombo.draw(frmMaterial.fieldNames[frmMaterial.FRM_FIELD_MERK_ID] + "_" + i, "formElemen", null, "" + material.getMerkId(), vectMerkKey, vectMerkVal, null));
                rowx.add(ControlCombo.draw(frmMaterial.fieldNames[frmMaterial.FRM_FIELD_GONDOLA_CODE] + "_" + i, "formElemen", null, "" + material.getGondolaCode(), vectKsgKey, vectKsgVal, null));

                rowx.add(ControlCombo.draw(frmMaterial.fieldNames[frmMaterial.FRM_FIELD_DEFAULT_STOCK_UNIT_ID] + "_" + i, "formElemen", null, "" + material.getDefaultStockUnitId(), vectUnitKey, vectUnitVal, null));

                rowx.add("<a title=\"\" href=\"javascript:addUnitBuy('" + material.getOID() + "')\">Set Unit</a>");

                rowx.add("<a title=\"\" href=\"javascript:setPrice('" + material.getOID() + "')\">Set Contract</a>");

                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(material.getAveragePrice()) + "</div>");

                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(material.getDefaultCost()) + "</div>");

                if (privShowPrice) {

                    CurrencyType curr = new CurrencyType();
                    try {
                        curr = PstCurrencyType.fetchExc(matUpdate.getDefaultCostCurrencyId());
                    } catch (Exception e) {
                    }

                    //add opie-eyek 20130708
                    PriceTypeMapping priceTypeMapping = new PriceTypeMapping();

                    FrmPriceTypeMapping frmPriceTypeMapping = new FrmPriceTypeMapping();

                    Hashtable memberPrice = SessMaterial.getPriceSaleInTypePriceMember(srcMaterial, material, material.getOID());

                    try {

                    } catch (Exception e) {
                    }

                    if (listTypeHrga != null && listTypeHrga.size() > 0) {
                        for (int m = 0; m < listTypeHrga.size(); m++) {
                            PriceType pricetype = (PriceType) listTypeHrga.get(m);
                            //DISINI UNTUK LOOPING CURRENCY
                            for (int j = 0; j < listCurrStandardX.size(); j++) {
                                Vector tempStand = (Vector) listCurrStandardX.get(j);
                                CurrencyType currx = (CurrencyType) tempStand.get(0);
                                StandartRate standart = (StandartRate) tempStand.get(1);

                                PriceTypeMapping pTypeMapping = null;
                                if (memberPrice != null && !memberPrice.isEmpty()) {

                                    pTypeMapping = (PriceTypeMapping) memberPrice.get("" + pricetype.getOID() + "_" + standart.getOID());
                                }

                                if (pTypeMapping == null) {
                                    pTypeMapping = new PriceTypeMapping();
                                    pTypeMapping.setMaterialId(material.getOID());
                                    pTypeMapping.setPriceTypeId(pricetype.getOID());
                                    pTypeMapping.setStandartRateId(currx.getOID());
                                }
                                try {
                                    rowx.add("<input type=\"hidden\" name=\"inputan\" value=\"" + pTypeMapping.getMaterialId() + "_" + pTypeMapping.getPriceTypeId() + "_" + standart.getOID() + "_" + FRMHandler.userFormatStringDecimal(pTypeMapping.getPrice()) + "\">"
                                            + "<input name =\"" + pTypeMapping.getMaterialId() + "_" + pTypeMapping.getPriceTypeId() + "_" + standart.getOID() + "\" type=\"text\" size\"10\"  value=\"" + FRMHandler.userFormatStringDecimal(pTypeMapping.getPrice()) + "\" class=\"formElemen\">"
                                            + "<input type=\"hidden\" name =\"pricebarcode_" + i + "\" type=\"text\" size\"10\"  value=\"" + FRMHandler.userFormatStringDecimal(pTypeMapping.getPrice()) + "\" class=\"formElemen\">"
                                            + "");
                                } catch (Exception exc) {
                                    System.out.println(exc);
                                }
                            }

                        }
                    }

                }

                lstData.add(rowx);

            }
            result = ctrlist.draw();
        } else {
            result = "<div class=\"msginfo\">&nbsp;&nbsp;" + textListTitleHeader[language][2] + "</div>";
        }
        return result;
    }

%>

<%!//    
    //list jewelry
    public String drawListJewelry(int language, Vector objectClass, Vector currency, int start, int iCommand, 
            int showImage, String approot, int showUpdateCatalog, SrcMaterial srcMaterial, int start2, 
            int showDiscountQty, int showHpp, int showCurrency, boolean privShowPrice, Vector listTypeHrga, 
            FrmMaterial frmMaterial, int multiLanguageName, String tipeItem) {
        String result = "";

        if (objectClass != null && objectClass.size() > 0) {
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("100%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("listgentitle");

            ctrlist.addHeader(textListMaterialHeader[language][0], "1%");
            ctrlist.addHeader(textListMaterialHeader[language][2], "5%");
            ctrlist.addHeader("Nama Barang", "10%");
            ctrlist.addHeader("Berat", "3%");
            if (tipeItem.equals(""+Material.MATERIAL_TYPE_EMAS) || tipeItem.equals(""+Material.MATERIAL_TYPE_EMAS_LANTAKAN)) {
                ctrlist.addHeader("HE", "5%");
            } else if (tipeItem.equals(""+Material.MATERIAL_TYPE_BERLIAN)) {
                ctrlist.addHeader("Harga Beli", "5%");
            }
            ctrlist.addHeader("Oks/Batu", "5%");        
            if (tipeItem.equals(""+Material.MATERIAL_TYPE_EMAS) || tipeItem.equals(""+Material.MATERIAL_TYPE_EMAS_LANTAKAN)) {
                ctrlist.addHeader("Total HP", "5%");
            } else if (tipeItem.equals(""+Material.MATERIAL_TYPE_BERLIAN)) {
                ctrlist.addHeader("Harga Pokok", "5%");
            }            
            ctrlist.addHeader("Harga Jual", "5%");            
            ctrlist.addHeader("Rate", "5%");            
            ctrlist.addHeader("Faktor Jual", "5%");
            if (tipeItem.equals(""+Material.MATERIAL_TYPE_BERLIAN)) {
                ctrlist.addHeader("UP ACC", "5%");
                ctrlist.addHeader("UP HET", "5%");
            }
            if (tipeItem.equals(""+Material.MATERIAL_TYPE_BERLIAN)) {
                ctrlist.addHeader("Koreksi", "5%");
            }
            ctrlist.addHeader("Lokasi Jual", "10%");
            ctrlist.addHeader("Print Barcode", "5%");
            
            ctrlist.setLinkRow(1);
            ctrlist.setLinkSufix("");
            Vector lstData = ctrlist.getData();
            Vector lstLinkData = ctrlist.getLinkData();
            ctrlist.setLinkPrefix("javascript:cmdEdit('");
            ctrlist.setLinkSufix("')");
            ctrlist.reset();
            int index = -1;
            if (start < 0) {
                start = 0;
            }
            
            Vector listLokasiJual = PstLocation.list(0, 0, PstLocation.fieldNames[PstLocation.FLD_TYPE] + "=" + PstLocation.TYPE_LOCATION_STORE, null);                        
            
            Vector rowx = new Vector();
            
            for (int i = 0; i < objectClass.size(); i++) {
                Vector temp = (Vector) objectClass.get(i);
                Material material = (Material) temp.get(0);
                Category category = (Category) temp.get(1);
                Merk merk = (Merk) temp.get(3);
                MaterialStock materialStock = (MaterialStock) temp.get(4);
                Material m = new Material();
                try {
                    if (material.getOID() > 0) {
                        m = PstMaterial.fetchExc(material.getOID());
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                
                String itemName = SessMaterial.setItemNameForLitama(material.getOID());
                
                double rateCurreny = 0.0;
                rateCurreny = PstStandartRate.getStandardRate();
                MaterialDetail materialDetail = new MaterialDetail();
                long oidMaterialDetailId = PstMaterialDetail.checkOIDMaterialDetailId(m.getOID());
                try {
                    materialDetail = PstMaterialDetail.fetchExc(oidMaterialDetailId);
                    if (materialDetail.getRate() != 0) {
                        rateCurreny = materialDetail.getRate();
                    }
                } catch (Exception ex) {
                }
                
                double rateJual = 0;
                Vector<RateJualBerlian> listRateJual = PstRateJualBerlian.list(0, 0, "" + PstRateJualBerlian.fieldNames[PstRateJualBerlian.FLD_STATUS_AKTIF] + " = 0", "");
                if (!listRateJual.isEmpty()) {
                    rateJual = listRateJual.get(0).getRate();
                }
                
                double hh = 0;
                double upAcc = 0;
                double persenHet = 30;
                double upHet = 0;
                double koreksi = 0;
                if (materialDetail.getFaktorJual() > 0) {
                    hh = (materialDetail.getHargaBeli() * (materialDetail.getUphetPersentase()/100));
                    upAcc = (materialDetail.getHargaBeli() + hh);
                    upHet = (upAcc + (upAcc * (persenHet/100)));
                    koreksi = (materialDetail.getHargaJual() - upHet);
                }
                
                rowx = new Vector();
                start = start + 1;
                rowx.add("" + start + "");
                rowx.add("<a title=\"\" href=\"javascript:cmdEdit('" + material.getOID() + "')\">" + material.getSku() + "</a>"
                        + "<input type=\"hidden\" name=\"" + frmMaterial.fieldNames[frmMaterial.FRM_FIELD_MATERIAL_ID] + "_" + i + "\" value=\"" + material.getOID() + "\">");
                rowx.add("" + itemName);
                rowx.add("<div style='text-align:right'>" + String.format("%,.3f",materialDetail.getBerat()) + "</div>");
                rowx.add("<div style='text-align:right'>" + String.format("%,.0f",m.getAveragePrice()) + ".00</div>"
                        + "<input type='hidden' name='' class='harga_beli"+i+"' value='"+m.getAveragePrice()+"'>");
                rowx.add("<div style='text-align:right'>" + String.format("%,.0f",materialDetail.getOngkos()) + ".00</div>");                
                rowx.add("<div style='text-align:right'>" + String.format("%,.0f",(m.getAveragePrice()+materialDetail.getOngkos())) + ".00</div>");   
                rowx.add("<div style='text-align:center'><input type='text' size='10' name='harga_jual_"+i+"' class='count_faktor_jual harga_jual"+i+"' index='"+i+"' style='text-align:right' value='"+String.format("%,.0f",materialDetail.getHargaJual())+".00'></div>");
                rowx.add("<div style='text-align:right'>" + String.format("%,.0f", rateJual) + ".00</div>"
                        + "<input type='hidden' name='' class='rate"+i+"' value='"+rateJual+"'>");
                rowx.add("<div style='text-align:center'><input type='text' size='5' name='faktor_jual_"+i+"' class='faktor_jual"+i+"' style='text-align:right;background-color:#FFD7AE' readOnly value='"+String.format("%,.2f",materialDetail.getFaktorJual())+"'></div>");
                if (tipeItem.equals(""+Material.MATERIAL_TYPE_BERLIAN)) {
                    rowx.add("<div style='text-align:center'>"
                            + "<input type='text' size='10' placeholder='%' name='uphet_persentase_"+i+"' class='count_koreksi persen_up_acc"+i+"' index='"+i+"' style='text-align:left' value='"+String.format("%,.2f",materialDetail.getUphetPersentase())+"'>"
                            + "<input type='text' size='10' placeholder='Rp' name='uphet_value_"+i+"' class='rp_up_acc"+i+"' style='text-align:left' value='"+String.format("%,.0f",materialDetail.getUphetValue())+".00'>"
                            + "<input type='text' size='10' class='nilai_up_acc"+i+"' style='text-align:left' value='"+String.format("%,.0f",upAcc)+".00'>"
                            + "</div>");
                    rowx.add("<div style='text-align:center'>"
                            + "<input type='text' size='10' placeholder='%' name='uphet_persentase_total_"+i+"' style='text-align:left' value='30'>"
                            + "<input type='text' size='10' placeholder='Rp' name='uphet_value_total_"+i+"' class='rp_up_het"+i+"' style='text-align:left' value='"+String.format("%,.0f",materialDetail.getUphetValueTot())+".00'>"
                            + "<input type='text' size='10' class='nilai_up_het"+i+"' style='text-align:left' value='"+String.format("%,.0f",upHet)+".00'>"
                            + "</div>");
                }
                if (tipeItem.equals(""+Material.MATERIAL_TYPE_BERLIAN)) {
                    rowx.add("<div style='text-align:center'><input type='text' size='10' class='koreksi"+i+"' style='text-align:right' value='"+String.format("%,.2f",koreksi)+"'></div>");
                }
                String checkbox = "";
                //get selected location
                Vector<MatMappLocation> listSelectedLocation = PstMatMappLocation.list(0, 0, ""+PstMatMappLocation.fieldNames[PstMatMappLocation.FLD_MATERIAL_ID]+"="+material.getOID(), "");
                
                for (int j = 0; j < listLokasiJual.size(); j++) {
                    Location locJual = (Location) listLokasiJual.get(j);
                    String checked = "";
                    for (int k = 0; k < listSelectedLocation.size(); k++) {
                        if (locJual.getOID() == listSelectedLocation.get(k).getLocationId()) {
                            checked = "checked";
                            break;
                        }
                    }
                    checkbox += "<input type='checkbox' "+checked+" name='lokasi_jual_"+i+"' value='"+locJual.getOID()+"'><small>"+locJual.getName()+"</small><br>";
                }
                rowx.add(""+checkbox+"");
                rowx.add("print barcode");
                System.out.println(i);
                lstData.add(rowx);
            }
            result = ctrlist.draw();
        } else {
            result = "<div class=\"msginfo\">&nbsp;&nbsp;" + textListTitleHeader[language][2] + "</div>";
        }
        return result;
    }

%>

<%
    /**
     * get approval status for create document
     */
    int systemName = I_DocType.SYSTEM_MATERIAL;
    boolean privManageData = true;
%>

<%
    /**
     * get title for purchasing(pr) document
     */
    String dfTitle = textListTitleHeader[SESS_LANGUAGE][1];
    String dfItemTitle = dfTitle + " Item";

    /**
     * get request data from current form
     */
    Material material = new Material();
    String sourceLink = FRMQueryString.requestString(request, "source_link");
    String sourceLink2 = FRMQueryString.requestString(request, "source_link2");
    long oidMaterial = FRMQueryString.requestLong(request, "hidden_material_id");
    long oidCodeRange = FRMQueryString.requestLong(request, "hidden_range_code_id");
    int type = FRMQueryString.requestInt(request, "hidden_type");
    int prevCommand = FRMQueryString.requestInt(request, "prev_command");
    int countMaterial = FRMQueryString.requestInt(request, "countMaterial");
    String materialTitle = textListTitleHeader[SESS_LANGUAGE][1];
    
    //added by dewok 2018-01-17
    String kodeAwal = FRMQueryString.requestString(request, "FRM_KODE_START");
    String kodeAkhir = FRMQueryString.requestString(request, "FRM_KODE_END");
    String tipeItem = FRMQueryString.requestString(request, "tipe_item");
    String multiMatId = "";
    if (kodeAwal.length() > 0 && kodeAkhir.length() > 0) {
        try {
            long lKodeAwal = Long.valueOf(kodeAwal);
            long lKodeAkhir = Long.valueOf(kodeAkhir);
            while (lKodeAwal <= lKodeAkhir) {
                Vector<Material> listItem = PstMaterial.list(0, 0, "" + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + lKodeAwal + "'", "");
                for (Material m : listItem) {
                    multiMatId += multiMatId.length() > 0 ? "," + m.getOID() : "" + m.getOID();
                }
                lKodeAwal++;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    //add where clause
    String whereAdd = "";
    if(typeOfBusinessDetail == 2) {
        if (multiMatId.length() > 0) {
            whereAdd += " AND MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + " IN (" + multiMatId + ")";
        }
        if (!tipeItem.equals("")) {
            whereAdd += " AND MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_JENIS_TYPE] + " = '" + tipeItem + "'";
        }
    }
    
    /**
     * initialitation some variable
     */
    int iErrCode = FRMMessage.ERR_NONE;
    String msgStr = "";
    int iCommand = FRMQueryString.requestCommand(request);
    int start = FRMQueryString.requestInt(request, "start");
    int recordToGet = 0;
    int vectSize = 0;
    String whereClause = "";
//for discount qty
    int start2 = FRMQueryString.requestInt(request, "start2");
    int multiLanguageName = Integer.parseInt((String) com.dimata.system.entity.PstSystemProperty.getValueIntByName("NAME_MATERIAL_MULTI_LANGUAGE"));

    /**
     * instantiate some object used in this page
     */
    ControlLine ctrLine = new ControlLine();
    CtrlMaterial ctrlMaterial = new CtrlMaterial(request);
    FrmMaterial frmMaterial = ctrlMaterial.getForm();
    String errMsg = "";
    if (typeOfBusinessDetail == 2) {
        iErrCode = ctrlMaterial.actionSaveAllMaterialJewelry(iCommand, request, countMaterial, userId, userName);
        errMsg = ctrlMaterial.getMessage();
    } else {
        ctrlMaterial.actionSaveAllMaterial(iCommand, request, countMaterial, multiLanguageName);
    }

    CtrlPriceTypeMapping ctrlPriceTypeMapping = new CtrlPriceTypeMapping(request);
    ctrlPriceTypeMapping.action(iCommand, request);
    SrcMaterial srcMaterial = new SrcMaterial();
    SessMaterial sessMaterial = new SessMaterial();
    FrmSrcMaterial frmSrcMaterial = new FrmSrcMaterial(request, srcMaterial);
    Vector listTypeHrga = new Vector();//PstPriceType.list(0, 0, whereClause, order)
    Vector currency = new Vector();
    long oidPrice = FRMQueryString.requestLong(request, frmSrcMaterial.fieldNames[frmSrcMaterial.FRM_FIELD_PRICE_TYPE_ID]);

    long oidSubCategory = FRMQueryString.requestLong(request, frmSrcMaterial.fieldNames[frmSrcMaterial.FRM_FIELD_CATEGORYID]);
    long oidCategory = FRMQueryString.requestLong(request, frmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_MERK_ID]);

    /**
     * handle current search data session
     */
    String[] strMember = null;

    if (iCommand == Command.BACK || iCommand == Command.FIRST
            || iCommand == Command.PREV || iCommand == Command.NEXT
            || iCommand == Command.LAST) {
        try {
            srcMaterial = (SrcMaterial) session.getValue(SessMaterial.SESS_SRC_MATERIAL_EDIT_ALL);

            if (srcMaterial == null) {
                srcMaterial = new SrcMaterial();
            } else {
                listTypeHrga = srcMaterial.getListTypeHrga();
            }
        } catch (Exception e) {
            out.println(textListTitleHeader[SESS_LANGUAGE][3]);
            srcMaterial = new SrcMaterial();
        }
    } else {
        if (type == 0) {
            frmSrcMaterial.requestEntityObject(srcMaterial);
        //add opsi currency Type nd memberType

            if (iCommand == Command.SAVE || iCommand == Command.PRINT || iCommand == Command.REFRESH) {
                try {
                    srcMaterial = (SrcMaterial) session.getValue(SessMaterial.SESS_SRC_MATERIAL_EDIT_ALL);

                    if (srcMaterial == null) {
                        srcMaterial = new SrcMaterial();
                    }
                } catch (Exception e) {
                    out.println(textListTitleHeader[SESS_LANGUAGE][3]);
                    srcMaterial = new SrcMaterial();
                }
            }

            Vector vectCurr = new Vector(1, 1);
            String[] strCurrencyType = request.getParameterValues(FrmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_CURRENCY_TYPE_ID]);
            if (strCurrencyType != null && strCurrencyType.length > 0) {
                for (int i = 0; i < strCurrencyType.length; i++) {
                    try {
                        vectCurr.add(strCurrencyType[i]);
                    } catch (Exception exc) {
                        System.out.println("err");
                    }
                }
            }

            Vector vectMember = new Vector(1, 1);

            strMember = request.getParameterValues(frmSrcMaterial.fieldNames[frmSrcMaterial.FRM_FIELD_PRICE_TYPE_ID]);
            //oidMembers = request.getParameterValues(frmSrcMaterial.fieldNames[frmSrcMaterial.FRM_FIELD_PRICE_TYPE_ID]);
            String sStrMember = "";
            if (strMember != null && strMember.length > 0) {
                for (int i = 0; i < strMember.length; i++) {
                    try {
                        if (strMember[i] != null && strMember[i].length() > 0) {
                            vectMember.add(strMember[i]);
                            sStrMember = sStrMember + strMember[i] + ",";
                        }

                    } catch (Exception exc) {
                        System.out.println("err");
                    }
                }
                if (sStrMember != null && sStrMember.length() > 0) {
                    sStrMember = sStrMember.substring(0, sStrMember.length() - 1);
                    String whereClauses = PstPriceType.fieldNames[PstPriceType.FLD_PRICE_TYPE_ID] + " IN(" + sStrMember + ")";

                    listTypeHrga = PstPriceType.list(0, 0, whereClauses, "");

                }

            }

            srcMaterial.setPriceTypeId(vectMember);
            srcMaterial.setListTypeHrga(listTypeHrga);

            String[] selectedSellItem = request.getParameterValues(FrmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_SELL_LOCATION]);
            Vector vectSellItem = new Vector(1, 1);
            if (selectedSellItem != null && selectedSellItem.length > 0) {
                for (int i = 0; i < selectedSellItem.length; i++) {
                    try {
                        vectSellItem.add(selectedSellItem[i]);

                    } catch (Exception exc) {
                        System.out.println("err");
                    }
                }
            }

            srcMaterial.setSellLocation(vectSellItem);

            String designMat = String.valueOf(PstSystemProperty.getValueByName("DESIGN_MATERIAL_FOR"));
            int DESIGN_MATERIAL_FOR = Integer.parseInt(designMat);

            srcMaterial.setDesignMat(DESIGN_MATERIAL_FOR);

            srcMaterial.setOidCodeRange(oidCodeRange);

            if (iCommand == Command.PRINT) {
                Vector recordsBarcode = sessMaterial.searchPrice(srcMaterial, start, recordToGet, whereAdd);
                Vector vectQty = new Vector(1, 1);
                Vector vectPrice = new Vector(1, 1);
                for (int i = 0; i < recordsBarcode.size(); i++) {
                    int loop = FRMQueryString.requestInt(request, "printbarcode_" + i);
                    String price = FRMQueryString.requestString(request, "pricebarcode_" + i);
                    vectQty.add("" + loop);
                    vectPrice.add("" + price);
                }
                srcMaterial.setQtyPrintBarcode(vectQty);
                srcMaterial.setPriceBarcode(vectPrice);
            }

            session.putValue(SessMaterial.SESS_SRC_MATERIAL_EDIT_ALL, srcMaterial);
        } else {
            srcMaterial = (SrcMaterial) session.getValue(SessMaterial.SESS_SRC_MATERIAL_EDIT_ALL);
            srcMaterial.setOidCodeRange(oidCodeRange);
        }
    }
    Vector selected = new Vector(1, 1);
    Vector vectDisc = new Vector(1, 1);
    Vector vectData = new Vector();

    

//iErrCode = ctrlMaterial.action(iCommand,vectData,listTypeHrga, selected);   
    /**
     * get vectSize, start and data to be display in this page
     */
    vectSize = sessMaterial.getCountSearchPrice(srcMaterial, whereAdd);
//vectSize = sessMaterial.getCountSearchWithMultiSupp(srcMaterial);
    if (iCommand == Command.FIRST || iCommand == Command.NEXT || iCommand == Command.PREV
            || iCommand == Command.LAST || iCommand == Command.LIST) {
        start = ctrlMaterial.actionList(iCommand, start, vectSize, recordToGet);
    }

    Vector records = sessMaterial.searchPrice(srcMaterial, start, recordToGet, whereAdd);

    String vendorName = "";
    if (srcMaterial.getSupplierId() > 0) {
        try {
            ContactList contact = new ContactList();
            contact = PstContactList.fetchExc(srcMaterial.getSupplierId());
            vendorName = ", Supplier : " + contact.getCompName();
        } catch (Exception e) {
        }
    }

    /**
     * ini di gunakan untuk mencari range code Vector listPriceType =
     * PstPriceType.list(0,0,"",ordPrice);
     */
    long oidCurr = 0;
    try {
        oidCurr = Long.parseLong((String) com.dimata.system.entity.PstSystemProperty.getValueByName("OID_CURR_FOR_PRICE_SALE"));
    } catch (Exception e) {
        oidCurr = 0;
    }

    String ordPrice = PstPriceType.fieldNames[PstPriceType.FLD_INDEX];
    Vector listCurrStandard = PstStandartRate.listCurrStandard(oidCurr); //list currency type & standart rate

    Vector listPriceType = PstPriceType.list(0, 0, "", ordPrice);
    Vector recordsCodeRange = PstCodeRange.list(0, 0, "", PstCodeRange.fieldNames[PstCodeRange.FLD_FROM_RANGE_CODE]);

%>
<!-- End of Jsp Block -->
<%if (iCommand == Command.PRINT) {
        iCommand = Command.REFRESH;
%>
<script language="JavaScript">
        window.open("<%=approot%>/servlet/com.dimata.posbo.printing.warehouse.PrintBarcodeRetail?hidden_material_id=<%=oidMaterial%>&type=1", "corectionwh", "scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
</script>    
<%}%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
    <head>
        <!-- #BeginEditable "doctitle" --> 
        <title>Dimata - ProChain POS</title>
        <style>
            .tool-tip{
                color:#444444;
                width:250px;
                z-index:13000;
            }

            .tool-title{
                font-weight:normal;
                font-size:16px;
                font-family:Georgia, "Times New Roman", Times, serif;
                margin:0;
                color:#343434;
                padding:8px 8px 5px 8px;
                background:url(images/tips-trans.png) top left;
                text-align:left;
            }
            .tool-text{
                font-size:12px;
                padding:0 8px 8px 8px;
                background:url(images/tips-trans.png) bottom right;
                text-align:left;
            } 
        </style>
        
        <script language="JavaScript">
            function cmdAdd()
            {
                document.frmmaterial.start.value = 0;
                document.frmmaterial.approval_command.value = "<%=Command.SAVE%>";
                document.frmmaterial.command.value = "<%=Command.ADD%>";
                document.frmmaterial.add_type.value = "<%=ADD_TYPE_LIST%>";
                document.frmmaterial.action = "material_main.jsp";
                document.frmmaterial.submit();
            }



            function cmdEdit(oid)
            {
                var strvalue = "material_main.jsp?command=<%=Command.EDIT%>&hidden_material_id=" + oid + "&start=0&approval_command=44";
                winSrcMaterial = window.open(strvalue, "material", "height=600,width=1200,left=100,status=yes,toolbar=no,menubar=yes,location=no,scrollbars=yes");
                if (window.focus) {
                    winSrcMaterial.focus();
                }

            }

            function cmdSave()
            {
                document.frmmaterial.command.value = "<%=Command.SAVE%>";
                document.frmmaterial.prev_command.value = "<%=prevCommand%>";
                document.frmmaterial.action = "list_update_harga.jsp";
                document.frmmaterial.submit();
            }


            function cmdPrintBarcode() {
                document.frmmaterial.command.value = "<%=Command.PRINT%>";
                document.frmmaterial.prev_command.value = "<%=prevCommand%>";
                document.frmmaterial.action = "list_update_harga.jsp";
                document.frmmaterial.submit();
            }

            function cmdEditSearch(oid) {
                document.frmmaterial.hidden_type.value = "1";
                document.frmmaterial.hidden_range_code_id.value = oid;
                document.frmmaterial.approval_command.value = "<%=Command.FIRST%>";
                document.frmmaterial.command.value = "<%=Command.LIST%>";
                document.frmmaterial.action = "material_list.jsp";
                document.frmmaterial.submit();
            }


            function cmdListFirst()
            {
                document.frmmaterial.command.value = "<%=Command.FIRST%>";
                document.frmmaterial.action = "list_update_harga.jsp";
                document.frmmaterial.submit();
            }

            function cmdListPrev()
            {
                document.frmmaterial.command.value = "<%=Command.PREV%>";
                document.frmmaterial.action = "list_update_harga.jsp";
                document.frmmaterial.submit();
            }

            function cmdListNext()
            {
                document.frmmaterial.command.value = "<%=Command.NEXT%>";
                document.frmmaterial.action = "list_update_harga.jsp";
                document.frmmaterial.submit();
            }

            function cmdListLast()
            {
                document.frmmaterial.command.value = "<%=Command.LAST%>";
                document.frmmaterial.action = "list_update_harga.jsp";
                document.frmmaterial.submit();
            }

            function cmdBack()
            {
                document.frmmaterial.command.value = "<%=Command.BACK%>";
                document.frmmaterial.action = "src_update_harga.jsp";
                document.frmmaterial.submit();
            }

            function cmdPrintAll() {
                document.frmmaterial.command.value = "<%=Command.LIST%>";
                document.frmmaterial.target = "print_catalog";
                document.frmmaterial.action = "material_list_print.jsp";
                document.frmmaterial.submit();
            }

            function cmdPrintAllWithoutPrice() {
                document.frmmaterial.command.value = "<%=Command.LIST%>";
                document.frmmaterial.target = "print_catalog";
                document.frmmaterial.action = "material_list_print_without_price.jsp";
                document.frmmaterial.submit();
            }
            o
            function printForm() {
                window.open("<%=approot%>/servlet/com.dimata.posbo.report.masterdata.PrintAllListWoPricePdf?tms=<%=System.currentTimeMillis()%>&", "sale", "scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
                //window.open("reportsaleinvoice_form_print.jsp","stockreport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
            }

            function addUnitBuy(oidMaterial) {
                var strvalue = "material_unit_order.jsp?command=<%=Command.LIST%>&oidMaterial=" + oidMaterial;
                winSrcMaterial = window.open(strvalue, "material", "height=600,width=800,status=yes,toolbar=no,menubar=yes,location=no,scrollbars=yes");
                if (window.focus) {
                    winSrcMaterial.focus();
                }
            }


            function setPrice(oid) {
                var strvalue = "vdrmaterialprice.jsp?command=<%=Command.LIST%>&hidden_material_id=" + oid + "&hidden_vendor_id=0&prev_command=23&source_link=materialdosearch.jsp";
                winSrcMaterial = window.open(strvalue, "material", "height=600,width=800,status=yes,toolbar=no,menubar=yes,location=no,scrollbars=yes");
                if (window.focus) {
                    winSrcMaterial.focus();
                }
            }

            function cmdPrintAllPriceTag() {
                var strvalue = "<%=approot%>/master/material/search_tipe_harga.jsp?command=<%=Command.ADD%>";
                        winSrcMaterial = window.open(strvalue, "searchtipeharga", "height=600,width=800,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
                        if (window.focus) {
                            winSrcMaterial.focus();
                        }
                    }

                    function printFormAllPdf() {
                        window.open("<%=approot%>/servlet/com.dimata.posbo.report.masterdata.PrintAllListPdf?tms=<%=System.currentTimeMillis()%>&", "sale", "scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
                    }

                    function addUnit() {
                        var strvalue = "matunit.jsp?command=<%=Command.LIST%>&source=1";
                        winSrcMaterial = window.open(strvalue, "material", "height=600,width=800,status=yes,toolbar=no,menubar=yes,location=no,scrollbars=yes");
                        if (window.focus) {
                            winSrcMaterial.focus();
                        }
                    }

                    function addCategory() {
                        var strvalue = "matcategory.jsp?command=<%=Command.LIST%>&source=1";
                        winSrcMaterial = window.open(strvalue, "PopupWindow", "height=600,width=800,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes");
                        if (window.focus) {
                            winSrcMaterial.focus();
                        }
                    }

                    function addMerk() {
                        var strvalue = "matmerk.jsp?command=<%=Command.LIST%>&source=1";
                        winSrcMaterial = window.open(strvalue, "material", "height=600,width=800,status=yes,toolbar=no,menubar=yes,location=no,scrollbars=yes");
                        if (window.focus) {
                            winSrcMaterial.focus();
                        }
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
        //------------------------- END JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------
        </script>
        <!-- #EndEditable -->
        <script type="text/javascript" src="../../styles/jquery-1.4.2.min.js"></script>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
        <!-- #BeginEditable "styles" -->
        <%if (menuUsed == MENU_ICON) {%>
        <link href="../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
        <%}%>
        <link rel="stylesheet" href="../../styles/main.css" type="text/css">
        <!-- #EndEditable -->
        <!-- #BeginEditable "stylestab" --> 
        <link rel="stylesheet" href="../../styles/tab.css" type="text/css">
        <!-- #EndEditable -->
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
            <%if (menuUsed == MENU_PER_TRANS || MODUS_TRANSFER == MODUS_TRANSFER_KASIR) {%>
            <tr>
                <td height="25" ID="TOPTITLE"> <!-- #BeginEditable "header" -->
                    <%@ include file = "../../main/header.jsp" %>
                    <!-- #EndEditable --></td>
            </tr>
            <tr>
                <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
                    <%@ include file = "../../main/mnmain.jsp" %>
                    <!-- #EndEditable --> </td>
            </tr>
            <%} else {%>
            <tr bgcolor="#FFFFFF">
                <td height="10" ID="MAINMENU">
                    <%@include file="../../styletemplate/template_header.jsp" %>
                </td>
            </tr>
            <%}%>
            <tr> 
                <td valign="top" align="left"> 
                    <table width="100%" cellspacing="0" cellpadding="0">  
                        <tr> 
                            <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" --> 
                                &nbsp;Masterdata &gt; <%=textListTitleHeader[SESS_LANGUAGE][0]%><!-- #EndEditable -->
                                <%
                                    if(typeOfBusinessDetail == 2) {
                                        if (tipeItem.equals("")) {
                                            out.print(": Emas dan Berlian");
                                        } else {
                                            out.println(": "+Material.MATERIAL_TYPE_TITLE[Integer.valueOf(tipeItem)]);
                                        }
                                    }
                                %>
                            </td>
                        </tr>
                        <tr> 
                            <td><!-- #BeginEditable "content" --> 
                                <form name="frmmaterial" method="post" action=""> 
                                    <input type="hidden" name="command" value="">
                                    <input type="hidden" name="add_type" value="">			  			  			  			  
                                    <input type="hidden" name="start" value="<%=start%>">
                                    <input type="hidden" name="countMaterial" value="<%=records.size()%>">
                                    <input type="hidden" name="hidden_material_id" value="<%=oidMaterial%>">
                                    <input type="hidden" name="prev_command" value="<%=prevCommand%>">

                                    <% if (strMember != null && strMember.length > 0) {
                                            for (int n = 0; n < strMember.length; n++) {
                                                if (strMember[n] != null && strMember[n].length() > 0) {
                                    %>
                                    <input type="hidden" name= "<%=frmSrcMaterial.fieldNames[frmSrcMaterial.FRM_FIELD_PRICE_TYPE_ID]%>" value="<%=strMember[n]%>">  
                                    <%
                                                }
                                            }

                                        }
                                    %>
                                    <input type="hidden" name="<%=frmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_CATEGORYID]%>"  value="<%=oidSubCategory%>"> 
                                    <input type="hidden" name="<%=frmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_MERK_ID]%>"  value="<%=oidCategory%>">

                                    <input type="hidden" name="hidden_range_code_id" value="<%=oidMaterial%>">
                                    <input type="hidden" name="hidden_type" value="<%=type%>">
                                    <input type="hidden" name="approval_command">
                                    <!--tambahan untuk litama by dewok 2018-02-07-->
                                    <input type="hidden" name="FRM_KODE_START" value="<%=kodeAwal%>">
                                    <input type="hidden" name="FRM_KODE_END" value="<%=kodeAkhir%>">
                                    <input type="hidden" name="tipe_item" value="<%=tipeItem%>">
                                    <table width="100%" cellspacing="0" cellpadding="3">
                                        <tr align="left" valign="top">
                                            <%if(typeOfBusinessDetail == 2) {%>
                                            <td height="22" valign="middle" colspan="3">                                                
                                                <%=drawListJewelry(SESS_LANGUAGE, records, currency, start, iCommand, srcMaterial.getShowImage(), approot, srcMaterial.getShowUpdateCatalog(), srcMaterial, start2, srcMaterial.getShowDiscountQty(), srcMaterial.getShowHpp(), srcMaterial.getShowCurrency(), privShowPrice, listTypeHrga, frmMaterial, multiLanguageName, tipeItem)%>
                                            </td>
                                            <%} else {%>
                                            <td height="22" valign="middle" colspan="3"><%=drawList(SESS_LANGUAGE, records, currency, start, iCommand, srcMaterial.getShowImage(), approot, srcMaterial.getShowUpdateCatalog(), srcMaterial, start2, srcMaterial.getShowDiscountQty(), srcMaterial.getShowHpp(), srcMaterial.getShowCurrency(), privShowPrice, listTypeHrga, frmMaterial, multiLanguageName)%></td>
                                            <%}%>
                                        </tr>
                                        <tr align="left" valign="top">
                                            <td height="18" valign="top" colspan="3"> 
                                                
                                            </td>
                                        </tr>	                                        
                                    </table> 
                                    <table width="91%" border="0" cellspacing="0" cellpadding="0">
                                        <tr>
                                            <%if (privAdd && privManageData) {%>
                                            <!--td width="3%" nowrap><a href="javascript:cmdSave()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE, dfTitle, ctrLine.CMD_SAVE, true)%>"></a></td--> 
                                            <td width="11%" nowrap class="command"><a class="btn btn-primary" href="javascript:cmdSave()"><i class="fa fa-check"></i> <%=ctrLine.getCommand(SESS_LANGUAGE, dfTitle, ctrLine.CMD_SAVE, true)%></a></td> 
                                            <%}%>
                                            <%if (listTypeHrga.size() == 1) {%>
                                            <td width="3%" nowrap><a href="javascript:cmdPrintBarcode()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100', '', '<%=approot%>/images/BtnNewOn.jpg', 1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE, dfTitle, ctrLine.CMD_SAVE, true)%>"></a></td> 
                                            <td width="11%" nowrap class="command"><a class="btn btn-primary" href="javascript:cmdPrintBarcode()"><i class="fa fa-print"></i> Print Barcode</a></td> 
                                            <%}%>
                                            <!--td width="6%" nowrap><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnBackOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE, dfTitle, ctrLine.CMD_BACK_SEARCH, true)%>"></a></td-->
                                            <td width="32%" nowrap class="command"><a class="btn btn-primary" href="javascript:cmdBack()"><i class="fa fa-arrow-left"></i> <%=ctrLine.getCommand(SESS_LANGUAGE, dfTitle, ctrLine.CMD_BACK_SEARCH, true)%></a></td>
                                        </tr>                                        
                                    </table>
                                    <br>                                        
                                    <table width="100%">
                                        <tr align="left" valign="top">
                                            <%if(iCommand == Command.SAVE) {%>
                                            <td height="18" valign="top" colspan="" style="text-align: center; background-color: yellow">                                                 
                                                <b><%=errMsg%></b>                                                
                                            </td>
                                            <%}%>
                                        </tr>
                                    </table>
                                </form>   
                                <!-- #EndEditable --></td> 
                        </tr> 
                    </table>
                </td>
            </tr>
            <tr>
                <td colspan="2" height="20"> <!-- #BeginEditable "footer" --> 
                    <%if (menuUsed == MENU_ICON) {%>
                    <%@include file="../../styletemplate/footer.jsp" %>

                    <%} else {%>
                    <%@ include file = "../../main/footer.jsp" %>
                    <%}%>
                    <!-- #EndEditable --> </td>
            </tr>
        </table>
    </body>
<script>
    jQuery(function(){
        $('.count_faktor_jual').keyup(function() {            
            var index = $(this).attr('index');                        
            var hargaJual = cleanNumberFloat($(this).val(),guiDigitGroup,guiDecimalSymbol);
            var rate = $('.rate'+index).val();
            var faktorJual = +hargaJual / +rate;
            //alert(hargaJual +"/"+rate+"="+faktorJual);
            $('.faktor_jual'+index).val(faktorJual.toFixed(2));
            //
            var upAccPersen = $('.persen_up_acc'+index).val();
            if (upAccPersen !== "") {
                countKoreksi(index);
            }
        });
                
        $('.count_koreksi').keyup(function() {
            var index = $(this).attr('index');            
            countKoreksi(index);
        });
        
        function countKoreksi(index) {            
            var hargaBeli = cleanNumberFloat($('.harga_beli'+index).val(),guiDigitGroup,guiDecimalSymbol);
            var upAccPersen = cleanNumberFloat($('.persen_up_acc'+index).val(),guiDigitGroup,guiDecimalSymbol);
            var hh = +hargaBeli * (+upAccPersen/100);
            var upAccRp = (+hargaBeli + +hh) - +hargaBeli;            
            $('.rp_up_acc'+index).val(upAccRp.toLocaleString()+".00");
            var upAcc = +hargaBeli + +hh;
            $('.nilai_up_acc'+index).val(upAcc.toLocaleString()+".00");
            var upHetRp = +upAcc * (30/100);
            $('.rp_up_het'+index).val(upHetRp.toLocaleString()+".00");
            var upHet = +upAcc + +upHetRp;
            $('.nilai_up_het'+index).val(upHet.toLocaleString()+".00");
            var hargaJual = cleanNumberFloat($('.harga_jual'+index).val(),guiDigitGroup,guiDecimalSymbol);
            var koreksi = +hargaJual - +upHet;
            $('.koreksi'+index).val(koreksi.toFixed(2));
        }
    });
</script>
    <!-- #EndTemplate --></html>

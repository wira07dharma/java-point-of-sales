<%-- 
    Document   : material_list_excel
    Created on : Mar 7, 2016, 5:41:44 PM
    Author     : dimata005
--%>
<%@page import="com.dimata.posbo.session.masterdata.SessCategory"%>
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

    /* this constant used to list text of listHeader */
 /*public static final String textListMaterialHeader[][] =
{
        {"No","Sku","Nama","Kategori","Sub Kategori","Tipe Supplier","Supplier","Hpp","Hrg Jual"},
        {"No","Code","Name","Category","Sub Kategori","Supplier Type","Supplier","Average","Price"}
};*/
    public static final String textListMaterialHeader[][] = {
        {"No", "Kode", "Barcode", "Kategori / Merk / Nama Barang", "Jenis", "Tipe", "HPP", "Harga Jual", "Kategori", "Merk", "Kode Grup", "Foto", "Mata Uang HPP", "Mata Uang Jual", "Harga Beli", "Margin"},
        {"No", "Code", "Barcode", "Category / Merk / Name", "Jenis", "Type", "Average Price", "Price", "Category", "Merk", "Code Range", "Picture", "Curr. Avg Price", "Curr. Sell", "Buy.PRice", "Margin"}
    };

//{"No","Kode","Barcode","Nama Barang","Jenis","Tipe","Hpp","Hrg Jual","Kategori","Merk","Kode Grup"},
//{"No","Code","Barcode","Name","Jenis","Type","Average","Price","Category","Merk","Code Range"}
//untuk discount Qty mapping
    public static final String textListDiscountQtyHeader[][] = {
        {"No", "Mata Uang", "Tipe Member", "Lokasi", "Start Qty", "To Qty", "Diskon", "Tipe Diskon", "Harga Per Unit"},
        {"No", "Currency", "Member Type", "Location", "Start Qty", "To Qty", "Discount", "Discount type", "Unit Price"}
    };

    /* this constant used to list text of listHeader */
    public static final String textListHeaderReposting[][] = {
        {"Lokasi", "Periode"},
        {"Location", "Period"}
    };

    public String drawList(JspWriter outObj, int language, Vector objectClass, int start, int showImage, String approot, int showUpdateCatalog, SrcMaterial srcMaterial, int start2, int showDiscountQty, int showHpp, int showCurrency, boolean privShowPrice, long locationSelected) {
        String result = "";
        if (objectClass != null && objectClass.size() > 0) {
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("100%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("listgentitle");
            ctrlist.setBorder(1);
            /**
             * mendapatkan mata uang default
             */
            CurrencyType defaultCurrencyType = PstCurrencyType.getDefaultCurrencyType();
            int multiLanguageName = Integer.parseInt((String) com.dimata.system.entity.PstSystemProperty.getValueIntByName("NAME_MATERIAL_MULTI_LANGUAGE"));
            int useSubCategory = Integer.parseInt((String) com.dimata.system.entity.PstSystemProperty.getValueIntByName("USE_SUB_CATEGORY"));
            int useForRaditya = Integer.parseInt((String) com.dimata.system.entity.PstSystemProperty.getValueIntByName("USE_FOR_RADITYA"));

            ctrlist.addHeader(textListMaterialHeader[language][0], "3%");
            ctrlist.addHeader("Material Id", "5%");
            ctrlist.addHeader(textListMaterialHeader[language][1], "5%");
            if (showImage == 1) {
                ctrlist.addHeader(textListMaterialHeader[language][11], "10%");
            }

            ctrlist.addHeader(textListMaterialHeader[language][2], "5%");
            //ctrlist.addHeader(textListMaterialHeader[language][3],"35%");

            //String merkName = PstSystemProperty.getValueByName("NAME_OF_MERK");
            //String name = "Category / "+ merkName+" / Name";
            ctrlist.addHeader("Category", "10%");
            if (useSubCategory == 1) {
                ctrlist.addHeader("Sub Category", "10%");
            }
            if (multiLanguageName == 1) {
                ctrlist.addHeader("Name English", "10%");
                ctrlist.addHeader("Name Indonesia", "10%");
                ctrlist.addHeader("Name Indian", "10%");
            } else {
                ctrlist.addHeader("Name", "15%"); //textListMaterialHeader[language][3]
            }
            ctrlist.addHeader("Merk", "5%");
            ctrlist.addHeader("Gondola", "5%");
            ctrlist.addHeader("Supplier", "5%");
            ctrlist.addHeader("Unit Stock", "5%");
            ctrlist.addHeader("Cost price", "5%");

            if (privShowPrice) {
                if (showHpp == 1) {
                    ctrlist.addHeader(textListMaterialHeader[language][6] + " (" + defaultCurrencyType.getCode() + ")", "10%");
                    ctrlist.addHeader(textListMaterialHeader[language][14] + " (" + defaultCurrencyType.getCode() + ")", "10%");
                }
                ctrlist.addHeader(textListMaterialHeader[language][13], "5%");
                ctrlist.addHeader(textListMaterialHeader[language][7], "10%");
                if (showHpp == 1) {
                    ctrlist.addHeader(textListMaterialHeader[language][15] + " (%)", "10%");
                }
            }
            if (useForRaditya == 0) {
                ctrlist.addHeader("Safety stocks", "5%");
                ctrlist.addHeader("Min. stocks", "5%");
                ctrlist.addHeader("Real Stock", "5%");
            }
            ctrlist.setLinkRow(1);
            ctrlist.setLinkSufix("");
            Vector lstData = ctrlist.getData();
            Vector lstLinkData = ctrlist.getLinkData();
            ctrlist.setLinkPrefix("javascript:cmdEdit('");
            ctrlist.setLinkSufix("')");
            ctrlist.reset();
            if (start < 0) {
                start = 0;
            }
            Vector rowx = new Vector();
            Vector list = PstCurrencyType.list(0, 0, PstCurrencyType.fieldNames[PstCurrencyType.FLD_TAB_INDEX] + "=1", "");
            CurrencyType currSell = new CurrencyType();
            if (list != null && list.size() > 0) {
                currSell = (CurrencyType) list.get(0);
            }
            Periode maPeriode = PstPeriode.getPeriodeRunning();
            SessCategory sessCategory = new SessCategory();

            for (int i = 0; i < objectClass.size(); i++) {
                Vector temp = (Vector) objectClass.get(i);
                Material material = (Material) temp.get(0);
                Category category = (Category) temp.get(1);
                SubCategory subCategory = (SubCategory) temp.get(2);
                Merk merk = (Merk) temp.get(3);
                Unit unit = (Unit) temp.get(4);
                //ContactList cnt = (ContactList)temp.get(3);
                rowx = new Vector();
                Ksg ksg = (Ksg) temp.get(5);
                start = start + 1;

                Material matUpdate = new Material();

                try {
                    matUpdate = PstMaterial.fetchExc(material.getOID());
                } catch (Exception e) {
                }
                rowx.add("" + start + "");
                rowx.add("" + material.getOID() + "");
                rowx.add("" + material.getSku() + "");

                // untuk menampilkan gambar
                if (showImage == 1) {
                    SessUploadCatalogPhoto objSessUploadCatalogPhoto = new SessUploadCatalogPhoto();
                    String pictPath = objSessUploadCatalogPhoto.fetchImagePeserta(material.getSku());
                    if (pictPath.length() > 0) {
                        rowx.add("<div align=\"center\"><img heigth = \"110\" width = \"110\" src=\"" + approot + "/" + pictPath + "\"></div>");
                    } else {
                        rowx.add("&nbsp");
                    }
                }

                rowx.add("" + material.getBarCode() + "");

                //cek category
                String cat = sessCategory.cleanCategoryName(category.getOID());
                cat = sessCategory.getCategoryName(category.getOID());
                rowx.add(cat);
                if (useSubCategory == 1) {
                    rowx.add(subCategory.getName());
                }
                String[] smartPhonesSplits = material.getName().split("\\;");
                if (multiLanguageName == 1) {
                    try {
                        rowx.add(smartPhonesSplits[0]);
                    } catch (Exception ex) {
                        rowx.add("");
                    }
                    try {
                        rowx.add(smartPhonesSplits[1]);
                    } catch (Exception ex) {
                        rowx.add("");
                    }
                    try {
                        rowx.add(smartPhonesSplits[2]);
                    } catch (Exception ex) {
                        rowx.add("");
                    }
                } else {
                    rowx.add(material.getName());
                }

                rowx.add(merk.getName());

                rowx.add("" + ksg.getName());
                //supplier
                //String nameSupplier = PstMatVendorPrice.listJoin(limitStart, recordToGet, whereClause, order);
                String whereClauseSupp = "" + PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID] + "=" + material.getOID();
                Vector listMatVendorPrice = PstMatVendorPrice.list(0, 0, whereClauseSupp, "");
                String namaSupplier = "";
                if (listMatVendorPrice.size() > 0) {
                    for (int k = 0; k < listMatVendorPrice.size(); k++) {
                        MatVendorPrice matVendorPrice = (MatVendorPrice) listMatVendorPrice.get(k);
                        if (k == 0) {
                            namaSupplier = PstMatVendorPrice.getVendorName(matVendorPrice.getVendorId());
                        } else {
                            namaSupplier = namaSupplier + ", " + PstMatVendorPrice.getVendorName(matVendorPrice.getVendorId());
                        }
                    }
                }

                rowx.add("" + namaSupplier);
                rowx.add(unit.getCode());
                rowx.add("" + FRMHandler.userFormatStringDecimal(material.getAveragePrice()));

                if (privShowPrice) {
                    CurrencyType curr = new CurrencyType();
                    try {
                        curr = PstCurrencyType.fetchExc(matUpdate.getDefaultCostCurrencyId());
                    } catch (Exception e) {
                    }
                    //rowx.add("<div align=\"right\">"+curr.getCode()+"</div>");
                    if (showHpp == 1) {
                        rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(matUpdate.getAveragePrice()) + "</div>");
                        rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(matUpdate.getCurrBuyPrice()) + "</div>");
                    }
                    //add opie-eyek 20130708
                    double priceSales = SessMaterial.getPriceSaleInTypePrice(material, srcMaterial.getSelectPriceTypeId());
                    try {
                        //matUpdate.setDefaultPrice(priceSales);
                        //PstMaterial.updateExc(matUpdate);
                    } catch (Exception e) {
                    }
                    //rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(material.getDefaultPrice())+"</div>");
                    rowx.add("<div align=\"center\">" + currSell.getCode() + "</div>");

                    if (useForRaditya == 0) {
                        rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(priceSales) + "</div>");

                        if (showHpp == 1) {
                            if (matUpdate.getCurrBuyPrice() > 0) {
                                String cfont = "";
                                String planMargin = "";
                                if (((priceSales - matUpdate.getCurrBuyPrice()) * 100 / matUpdate.getCurrBuyPrice()) - 0.5 > matUpdate.getProfit()) {
                                    cfont = "<font class=\"msgquestion\">";
                                    planMargin = "";
                                } else {
                                    if (((priceSales - matUpdate.getCurrBuyPrice()) * 100 / matUpdate.getCurrBuyPrice()) + 0.5 < matUpdate.getProfit()) {
                                        cfont = "<font class=\"msgerror\">";
                                        planMargin = "";
                                    } else {
                                        cfont = "<font>";
                                    }
                                }

                                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(priceSales - matUpdate.getCurrBuyPrice()) + cfont + " (  "
                                        + (matUpdate.getCurrBuyPrice() > 0 ? Formater.formatNumber((priceSales - matUpdate.getCurrBuyPrice()) * 100 / matUpdate.getCurrBuyPrice(), "###.#") : "") + "%) <font> </div>");
                            } else {
                                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(priceSales - matUpdate.getCurrBuyPrice()) + " (  "
                                        + (matUpdate.getCurrBuyPrice() > 0 ? Formater.formatNumber((priceSales - matUpdate.getCurrBuyPrice()) * 100 / matUpdate.getCurrBuyPrice(), "###.#") : "") + "%) </div>");
                            }
                        }
                    } else {
                        double price = 0;
                        double itemPrice = 0;
                        double valuePriceTotal = 0;
                        String formulaCash = PstSystemProperty.getValueByName("CASH_FORMULA");
                        if (PstPriceTypeMapping.checkString(formulaCash, "HPP") > -1) {
                            formulaCash = formulaCash.replaceAll("HPP", "" + material.getAveragePrice());
                        }
//                        if (PstPriceTypeMapping.checkString(formulaCash, "INCREASE") > -1) {
//                            formulaCash = formulaCash.replaceAll("INCREASE", "" + category.getKenaikanHarga());
//                        }
                        price += PstPriceTypeMapping.getValue(formulaCash);
                        price = Math.round(price);
                        valuePriceTotal = price;
                        rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(valuePriceTotal) + "</div>");
                    }
                }

                if (useForRaditya == 0) {
                    //sefty & min. stock add opie-eyek 20140318
                    if (locationSelected != 0) {
                        String where = PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] + "=" + material.getOID()
                                + " AND " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID] + "=" + maPeriode.getOID();
                        Vector listLoc = PstMaterialStock.list(0, 0, where + " AND " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] + "=" + locationSelected, "");
                        MaterialStock materialStock = new MaterialStock();
                        if (listLoc != null && listLoc.size() > 0) {
                            materialStock = (MaterialStock) listLoc.get(0);
                        }
                        rowx.add("" + materialStock.getQtyMin());
                        rowx.add("" + materialStock.getQtyOptimum());
                        rowx.add("" + materialStock.getQty());
                    } else {
                        rowx.add("");
                        rowx.add("");
                        rowx.add("");
                    }
                }

                lstData.add(rowx);

                //table for list discount qty mapping 
                String whereClause = "";
                whereClause = " DISCQTY. " + PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_MATERIAL_ID] + "=" + material.getOID();
                String strfromDate = Formater.formatDate(srcMaterial.getDateFrom(), "yyyy-MM-dd 00:00:01");
                String strtoDate = Formater.formatDate(srcMaterial.getDateTo(), "yyyy-MM-dd 23:59:59");
                whereClause = whereClause + " AND DISCQTY." + PstMaterial.fieldNames[PstMaterial.FLD_UPDATE_DATE] + " BETWEEN '" + strfromDate + "' AND '" + strtoDate + "'";

                String orderClause = " LOC." + PstLocation.fieldNames[PstLocation.FLD_CODE];
                orderClause = orderClause + ", DISC." + com.dimata.common.entity.payment.PstDiscountType.fieldNames[PstDiscountType.FLD_CODE];
                orderClause = orderClause + ", CURR." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE];
                orderClause = orderClause + ", DISCQTY." + PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_DISCOUNT_TYPE];
                //Vector listDiscQty = PstDiscountQtyMapping.listDiscQtyAll(0, 0, whereClause, orderClause);
                Vector listDiscQty = SessMaterial.getDiscountQtyMapping(srcMaterial, material.getOID());
                if (showUpdateCatalog == 1 && showDiscountQty == 1) {
                    if (listDiscQty.size() != 0) {
                        rowx = new Vector();
                        rowx.add("");
                        rowx.add("");
                        if (showImage == 1) {
                            rowx.add("");
                        }
                        rowx.add("<div align=\"center\"class=\"comment\"><b>DISCOUNT QTY</b></div>");
                        rowx.add(drawDiscountQty(language, listDiscQty, start2, showCurrency));
                        //rowx.add("");
                        //rowx.add("");
                        //rowx.add("");
                        lstData.add(rowx);
                    }
                }

            }
            //ctrlist.drawexcel();
            ctrlist.draw(outObj);
        } else {
            result = "<div class=\"msginfo\">&nbsp;&nbsp;" + textListTitleHeader[language][2] + "</div>";
        }
        return result;
    }

    public String drawCodeRangeList(int language, Vector objectClass) {
        String result = "";
        if (objectClass != null && objectClass.size() > 0) {
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("20%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("listgentitle");
            ctrlist.addHeader(textListMaterialHeader[language][10], "20%");
            //ctrlist.addHeader(textListMaterialHeader[language][1],"10%");

            ctrlist.setLinkRow(-1);
            ctrlist.setLinkSufix("");
            Vector lstData = ctrlist.getData();
            Vector lstLinkData = ctrlist.getLinkData();
            ctrlist.setLinkPrefix("javascript:cmdEditSearch('");
            ctrlist.setLinkSufix("')");
            ctrlist.reset();
            Vector rowx = new Vector();
            for (int i = 0; i < objectClass.size(); i++) {
                CodeRange codeRange = (CodeRange) objectClass.get(i);
                rowx = new Vector();
                rowx.add("<a href=\"javascript:cmdEditSearch('" + codeRange.getOID() + "')\">" + codeRange.getFromRangeCode() + "</a>");
                lstData.add(rowx);
            }
            result = ctrlist.draw();
        }
        return result;
    }

//for list discount qty mapping
    public String drawDiscountQty(int language, Vector objectClass, int start, int showCurrency) {
        String result = "";
        if (objectClass != null && objectClass.size() > 0) {
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("100%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("listgentitle");
            ctrlist.addHeader(textListDiscountQtyHeader[language][0], "5%");
            if (showCurrency == 1) {
                ctrlist.addHeader(textListDiscountQtyHeader[language][1], "10%");
            }
            ctrlist.addHeader(textListDiscountQtyHeader[language][2], "10%");
            ctrlist.addHeader(textListDiscountQtyHeader[language][3], "10%");
            ctrlist.addHeader(textListDiscountQtyHeader[language][4], "5%");
            ctrlist.addHeader(textListDiscountQtyHeader[language][5], "5%");
            ctrlist.addHeader(textListDiscountQtyHeader[language][6], "10%");
            ctrlist.addHeader(textListDiscountQtyHeader[language][7], "10%");
            ctrlist.addHeader(textListDiscountQtyHeader[language][8], "10%");

            ctrlist.setLinkRow(-1);
            ctrlist.setLinkSufix("");
            Vector lstData = ctrlist.getData();
            Vector lstLinkData = ctrlist.getLinkData();
            ctrlist.setLinkPrefix("javascript:cmdEditSearch('");
            ctrlist.setLinkSufix("')");
            ctrlist.reset();
            Vector rowx = new Vector();
            for (int i = 0; i < objectClass.size(); i++) {
                //Vector temp = (Vector)objectClass.get(i);
                DiscountQtyMapping discountqtymapping = (DiscountQtyMapping) objectClass.get(i);
                //DiscountType discountType = (DiscountType)temp.get(1);
                //CurrencyType currencyType = (CurrencyType)temp.get(2);
                //Location location = (Location)temp.get(3);
                //Material material = (Material)temp.get(4);

                com.dimata.common.entity.payment.DiscountType discountType = new com.dimata.common.entity.payment.DiscountType();
                try {
                    discountType = com.dimata.common.entity.payment.PstDiscountType.fetchExc(discountqtymapping.getDiscountTypeId());
                } catch (Exception e) {
                    System.out.println("Exc when PstDiscountType.fetchExc() : " + e.toString());
                }
                CurrencyType currencyType = new CurrencyType();
                try {
                    currencyType = PstCurrencyType.fetchExc(discountqtymapping.getCurrencyTypeId());
                } catch (Exception e) {
                    System.out.println("Exc when PstCurrencyType.fetchExc() : " + e.toString());
                }

                Location location = new Location();
                try {
                    location = PstLocation.fetchExc(discountqtymapping.getLocationId());
                } catch (Exception e) {
                    System.out.println("Exc when PstLocation.fetchExc() : " + e.toString());
                }

                Material material = new Material();
                try {
                    material = PstMaterial.fetchExc(discountqtymapping.getMaterialId());
                } catch (Exception e) {
                    System.out.println("Exc when PstMaterial.fetchExc() : " + e.toString());
                }

                //start = start + 1;
                double diskon = 0;
                double diskonPersen = 0;
                double hargaUnit = 0;
                double priceSales = SessMaterial.getPriceSale(material);

                rowx = new Vector();
                rowx.add("" + (start + i + 1) + "");
                if (showCurrency == 1) {
                    rowx.add(currencyType.getCode());
                }
                rowx.add(discountType.getCode());
                rowx.add(location.getCode());
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(discountqtymapping.getStartQty()));
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(discountqtymapping.getToQty()));
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(discountqtymapping.getDiscountValue()));
                rowx.add(PstDiscountQtyMapping.typeDiscount[discountqtymapping.getDiscountType()]);

                if (discountqtymapping.getDiscountType() == 0) {
                    diskonPersen = discountqtymapping.getDiscountValue();
                    diskon = priceSales * diskonPersen / 100;
                } else if (discountqtymapping.getDiscountType() == 1) {
                    diskon = discountqtymapping.getDiscountValue();
                }

                hargaUnit = priceSales - diskon;
                if (discountqtymapping.getDiscountType() == 2) {
                    rowx.add("<div align=\"center\">-</div>");
                } else {
                    rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(hargaUnit));
                }

                lstData.add(rowx);
            }
            result = ctrlist.draw();
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
    long oidMaterial = FRMQueryString.requestLong(request, "hidden_material_id");
    long oidCodeRange = FRMQueryString.requestLong(request, "hidden_range_code_id");
    int type = FRMQueryString.requestInt(request, "hidden_type");

    /**
     * initialitation some variable
     */
    int iErrCode = FRMMessage.ERR_NONE;
    String msgStr = "";
    int iCommand = FRMQueryString.requestCommand(request);
    int start = FRMQueryString.requestInt(request, "start");

    int start_search = FRMQueryString.requestInt(request, "start_search");
    int start_search2 = FRMQueryString.requestInt(request, "start_search2");

    int recordToGet = 0;
    if (start_search == 1) {
        start = start_search2 - recordToGet;
        iCommand = 22;
    }

    int vectSize = 0;
    String whereClause = "";
//for discount qty
    int start2 = FRMQueryString.requestInt(request, "start2");

    /**
     * instantiate some object used in this page
     */
    ControlLine ctrLine = new ControlLine();
    CtrlMaterial ctrlMaterial = new CtrlMaterial(request);
    SrcMaterial srcMaterial = new SrcMaterial();
    SessMaterial sessMaterial = new SessMaterial();

    try {
        srcMaterial = (SrcMaterial) session.getValue(SessMaterial.SESS_SRC_MATERIAL);
        if (srcMaterial == null) {
            srcMaterial = new SrcMaterial();
        } else {
            session.putValue(SessMaterial.SESS_SRC_MATERIAL, srcMaterial);
        }
    } catch (Exception e) {
        out.println(textListTitleHeader[SESS_LANGUAGE][3]);
        srcMaterial = new SrcMaterial();
    }
    /**
     * get vectSize, start and data to be display in this page
     */
    vectSize = sessMaterial.getCountSearch(srcMaterial);
//vectSize = sessMaterial.getCountSearchWithMultiSupp(srcMaterial);
    if (iCommand == Command.FIRST || iCommand == Command.NEXT || iCommand == Command.PREV
            || iCommand == Command.LAST || iCommand == Command.LIST) {
        start = ctrlMaterial.actionList(iCommand, start, vectSize, recordToGet);
    }

    Vector records = sessMaterial.searchMaterial(srcMaterial, start, recordToGet);
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
     * ini di gunakan untuk mencari range code
     */
    Vector recordsCodeRange = PstCodeRange.list(0, 0, "", PstCodeRange.fieldNames[PstCodeRange.FLD_FROM_RANGE_CODE]);

    response.setContentType("application/x-msexcel");
    response.setHeader("Content-Disposition", "attachment; filename=" + "Masterdata_List.xls");
%>
<!-- End of Jsp Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
    <head>
        <!-- #BeginEditable "doctitle" --> 
        <title>Dimata - ProChain POS</title>
        <!-- #EndEditable --> 
    </head> 

    <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">   
        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
            <tr> 
                <td valign="top" align="left"> 
                    <table width="100%" border="1" cellspacing="0" cellpadding="0">  
                        <tr> 
                            <td><!-- #BeginEditable "content" --> 
                                <form name="frm_material" method="post" action=""> 
                                    <input type="hidden" name="command" value="">
                                    <input type="hidden" name="add_type" value="">			  			  			  			  
                                    <input type="hidden" name="start" value="<%=start%>">
                                    <input type="hidden" name="start2" value="<%=start2%>">
                                    <input type="hidden" name="start_search" value="0">
                                    <input type="hidden" name="start_search2" value="<%=start%>">
                                    <input type="hidden" name="hidden_material_id" value="<%=oidMaterial%>">
                                    <input type="hidden" name="hidden_range_code_id" value="<%=oidMaterial%>">
                                    <input type="hidden" name="hidden_type" value="<%=type%>">
                                    <input type="hidden" name="approval_command">
                                    <table width="100%" cellspacing="0" cellpadding="3">
                                        <tr align="left" valign="top">
                                            <td height="22" valign="middle" colspan="3">
                                                <%=drawList(out, SESS_LANGUAGE, records, start, srcMaterial.getShowImage(), approot, srcMaterial.getShowUpdateCatalog(), srcMaterial, start2, srcMaterial.getShowDiscountQty(), srcMaterial.getShowHpp(), srcMaterial.getShowCurrency(), privShowPrice, srcMaterial.getLocationId())%>
                                            </td>
                                        </tr>
                                        <%=drawCodeRangeList(SESS_LANGUAGE, recordsCodeRange)%>				  
                                    </table>
                                </form>  
                            </td> 
                        </tr> 
                    </table>
                </td>
            </tr>
        </table>
    </body>
    <!-- #EndTemplate --></html>







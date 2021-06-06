<%@page import="com.dimata.hanoman.entity.masterdata.MasterGroup"%>
<%@page import="com.dimata.hanoman.entity.masterdata.PstMasterGroup"%>
<%@page import="com.dimata.hanoman.entity.masterdata.PstMasterType"%>
<%@page import="com.dimata.hanoman.entity.masterdata.MasterType"%>
<%@page import="com.dimata.posbo.session.warehouse.SessMatCostingStokFisik"%>
<%@page import="com.dimata.common.entity.payment.PriceType"%>
<%@page import="com.dimata.common.entity.payment.PstPriceType"%>
<%@page import="com.dimata.gui.jsp.ControlCheckBox"%>
<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*,
                   com.dimata.posbo.entity.warehouse.PstSourceStockCode,
                   com.dimata.posbo.form.warehouse.FrmMatStockOpnameItem,
                   com.dimata.posbo.entity.warehouse.MatStockOpnameItem,
                   com.dimata.gui.jsp.ControlList,
                   com.dimata.posbo.entity.masterdata.*,
                   com.dimata.util.Command,
                   com.dimata.qdep.entity.I_PstDocType,
				   com.dimata.qdep.form.FRMHandler,
                   com.dimata.qdep.form.FRMMessage,
                   com.dimata.qdep.form.FRMQueryString,
                   com.dimata.posbo.form.warehouse.CtrlMatStockOpnameItem,
                   com.dimata.gui.jsp.ControlLine,
                   com.dimata.posbo.entity.warehouse.MatStockOpname,
                   com.dimata.posbo.form.warehouse.FrmMatStockOpname,
                   com.dimata.posbo.form.warehouse.CtrlMatStockOpname,
                   com.dimata.posbo.entity.warehouse.PstMatStockOpnameItem,
                   com.dimata.gui.jsp.ControlCombo,
                   com.dimata.gui.jsp.ControlLine,
                   com.dimata.gui.jsp.ControlDate,
                   com.dimata.common.entity.contact.ContactList,
                   com.dimata.common.entity.contact.PstContactList,
                   com.dimata.common.entity.contact.PstContactClass,
                   com.dimata.common.entity.location.Location,
                   com.dimata.common.entity.location.PstLocation,
                    com.dimata.posbo.entity.warehouse.*,
                   com.dimata.gui.jsp.ControlDate" %>
				   
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK, AppObjInfo.OBJ_STOCK_OPNAME);
//int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_COSTING, AppObjInfo.G2_COSTING, AppObjInfo.OBJ_COSTING);
int  appObjCodeShowQtyAndPrice = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE_QTY_AND_PRICE);

%>
<%@ include file = "../../../main/checkuser.jsp" %>

<%
boolean privShowQtyPrice = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeShowQtyAndPrice, AppObjInfo.COMMAND_VIEW));
%>



<!-- Jsp Block -->
<%!//
    public static final String textListGlobal[][] = {
        {"Stok", "Opname", "Pencarian", "Daftar", "Edit", "Tidak ada data opname", "Cetak Opname", "Verifikasi", "Verifikasi berhasil", "Tutup"},
        {"Stock", "Opname", "Search", "List", "Edit", "No opname data available", "Print Opname", "Verification", "Verifivation success", "Close"}
    };

    /* this constant used to list text of listHeader */
    public static final String textListOrderHeader[][] = {
        {"Nomor", "Lokasi", "Tanggal", "Time", "Status", "Supplier", "Kategori", "Sub Kategori", "Keterangan", "Semua"},
        {"Number", "Location", "Date", "Jam", "Status", "Supplier", "Category", "Sub Category", "Remark", "All"}
    };

    /* this constant used to list text of listMaterialItem */
    public static final String textListOrderItem[][]
            = {
                {"No", "Sku", "Nama Barang", "Unit", "Kategori", "Sub Kategori", "Qty Opname", "Hapus", "Barcode","Warna","Ukuran"},
                {"No", "Code", "Name", "Unit", "Category", "Sub Category", "Qty Opname", "Delete", "Barcode","Color","Size"}
            };

    public static final String textDelete[][] = {
        {"Apakah Anda Yakin Akan Menghapus Data ?"},
        {"Are You Sure to Delete This Data? "}
    };

    /**
     * this method used to maintain soList
     */
    public Vector drawListOpnameItem(int language, int iCommand, FrmMatStockOpnameItem frmObject, 
            MatStockOpnameItem objEntity, Vector objectClass, long soItemId, int start, String approot, 
            int typeOfBusinessDetail) {
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        
        String useForGreenbowl = PstSystemProperty.getValueByName("USE_FOR_GREENBOWL");
        
        ctrlist.addHeader(textListOrderItem[language][0], "1%");
        ctrlist.addHeader(textListOrderItem[language][1], "15%");
        if (useForGreenbowl.equals("1")) {
            ctrlist.addHeader(textListOrderItem[language][8], "15%");
        }
        ctrlist.addHeader(textListOrderItem[language][2], "20%");
        ctrlist.addHeader(textListOrderItem[language][3], "5%");
        if (useForGreenbowl.equals("1")) {
            ctrlist.addHeader(textListOrderItem[language][9], "5%");
            ctrlist.addHeader(textListOrderItem[language][10], "5%");
        }
        if (typeOfBusinessDetail == 2) {
            ctrlist.addHeader("Qty", "5%");
            ctrlist.addHeader("Qty SO", "5%");
            ctrlist.addHeader("Qty Selisih", "5%");
            ctrlist.addHeader("Kadar", "5%");
            ctrlist.addHeader("Kadar SO", "5%");
            ctrlist.addHeader("Berat", "5%");
            ctrlist.addHeader("Berat SO", "5%");
            ctrlist.addHeader("Berat Selisih", "5%");
            ctrlist.addHeader("Remark", "10%");
        } else {
            ctrlist.addHeader(textListOrderItem[language][6], "5%");
        }
        ctrlist.addHeader(textListOrderItem[language][7], "1%");
        Vector lstData = ctrlist.getData();
        Vector rowx = new Vector(1, 1);
        Vector list = new Vector(1, 1);
        Vector listError = new Vector(1, 1);

        ctrlist.reset();
        ctrlist.setLinkRow(1);
        int index = -1;
        if (start < 0) {
            start = 0;
        }

        int soItemCounter = 0;
        long soItemSameMaterialId = 0;

        //added by dewok 2018 for jewel
        Vector key_kadar = new Vector(1, 1);
        Vector val_kadar = new Vector(1, 1);
        Vector<Kadar> listKadar = PstKadar.list(0, 0, "", "");
        for (Kadar k : listKadar) {
            key_kadar.add("" + k.getKadar());
            val_kadar.add("" + k.getOID());
        }

        //added for litama
        double totalQty = 0;
        double totalQtyOpname = 0;
        double totalQtySelisih = 0;
        double totalBerat = 0;
        double totalBeratOpname = 0;
        double totalBeratSelisih = 0;

        for (int i = 0; i < objectClass.size(); i++) {
            Vector temp = (Vector) objectClass.get(i);
            MatStockOpnameItem soItem = (MatStockOpnameItem) temp.get(0);
            Material mat = (Material) temp.get(1);
            Unit unit = (Unit) temp.get(2);
            Category cat = (Category) temp.get(3);
            SubCategory scat = (SubCategory) temp.get(4);
            rowx = new Vector();
            start = start + 1;

            String name = "";
            name = mat.getName();
            //if (name.compareToIgnoreCase("\"") >= 0 || name.compareToIgnoreCase("'") >= 0) {
            name = name.replace('\"', '`');
            name = name.replace('\'', '`');
            //}

            //counter
            //
            if (i == 0) {
                soItemCounter = soItem.getStockOpnameCounter();
                soItemSameMaterialId = soItem.getMaterialId();
            }

            //added by dewok 2018 for litama
            Material newmat = new Material();
            Category category = new Category();
            Color color = new Color();
            Kadar kadar = new Kadar();
            Kadar kadarOpname = new Kadar();
            MasterType masterTypeSize = new MasterType();
            
            try {
                newmat = PstMaterial.fetchExc(soItem.getMaterialId());
                category = PstCategory.fetchExc(newmat.getCategoryId());
                if (PstColor.checkOID(newmat.getPosColor())) {
                    color = PstColor.fetchExc(newmat.getPosColor());
                }
                if (PstKadar.checkOID(soItem.getKadarId())) {
                    kadar = PstKadar.fetchExc(soItem.getKadarId());
                }
                if (PstKadar.checkOID(soItem.getKadarOpnameId())) {
                    kadarOpname = PstKadar.fetchExc(soItem.getKadarOpnameId());
                }
                long oidMappingSize = PstMaterialMappingType.getSelectedTypeId(1, newmat.getOID());
                if (PstMasterType.checkOID(oidMappingSize)) {
                    masterTypeSize = PstMasterType.fetchExc(oidMappingSize);
                }
            } catch (Exception e) {

            }

            String itemName = "" + newmat.getName();
            if (typeOfBusinessDetail == 2) {
                if (newmat.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS) {
                    itemName = "" + category.getName() + " " + color.getColorName() + " " + newmat.getName();
                } else if (newmat.getMaterialJenisType() == Material.MATERIAL_TYPE_BERLIAN) {
                    itemName = "" + category.getName() + " " + color.getColorName() + " Berlian " + newmat.getName();
                }
                name = itemName;
            }

            totalQty += soItem.getQtyItem();
            totalQtyOpname += soItem.getQtyOpname();
            totalQtySelisih += soItem.getQtySelisih();
            totalBerat += soItem.getBerat();
            totalBeratOpname += soItem.getBeratOpname();
            totalBeratSelisih += soItem.getBeratSelisih();

            if (soItemId == soItem.getOID()) {
                index = i;
            }
            if (index == i && (iCommand == Command.EDIT || iCommand == Command.ASK)) {
                rowx.add("" + start);
                rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID] + "\" value=\"" + soItem.getMaterialId()
                        + "\"><input type=\"text\" size=\"15\" onKeyDown=\"javascript:keyDownCheck(event)\" name=\"matCode\" value=\"" + mat.getSku() + "\" class=\"formElemen\" style=\"background-color:#E8E8E8\" readOnly>");
                if (useForGreenbowl.equals("1")) {
                    rowx.add("<input type=\"text\" size=\"20\" name=\"matItem\" value=\"" + newmat.getBarCode() + "\" class=\"formElemen\" style=\"background-color:#E8E8E8\" readOnly>");
                }
                rowx.add("<input type=\"text\" size=\"20\" name=\"matItem\" value=\"" + name + "\" class=\"formElemen\" style=\"background-color:#E8E8E8\" readOnly>");
                rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID] + "\" value=\"" + soItem.getUnitId()
                        + "\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\"" + unit.getCode() + "\" class=\"formElemen\" style=\"background-color:#E8E8E8\" readOnly>");
                //rowx.add("<input type=\"text\" size=\"15\" name=\"matCat\" value=\""+cat.getName()+"\" class=\"formElemen\" style=\"background-color:#E8E8E8\" readOnly>");
                //rowx.add("<input type=\"text\" size=\"15\" name=\"matSCat\" value=\""+scat.getName()+"\" class=\"formElemen\" style=\"background-color:#E8E8E8\" readOnly>");
                if (useForGreenbowl.equals("1")) {
                    rowx.add("<input type=\"text\" size=\"15\" name=\"\" value=\""+color.getColorName()+"\" class=\"formElemen\" style=\"background-color:#E8E8E8\" readOnly>");
                    rowx.add("<input type=\"text\" size=\"5\" name=\"\" value=\""+masterTypeSize.getMasterName()+"\" class=\"formElemen\" style=\"background-color:#E8E8E8\" readOnly>");
                }
                if (typeOfBusinessDetail == 2) {
                    rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_QTY_ITEM] + "\" value=\"" + FRMHandler.userFormatStringDecimal(soItem.getQtyItem()) + "\" class=\"formElemen qty_item\" style=\"text-align:right\"></div>");
                }
                rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_QTY_OPNAME] + "\" value=\"" + FRMHandler.userFormatStringDecimal(soItem.getQtyOpname()) + "\" class=\"formElemen qty_opname\" style=\"text-align:right\"></div>");
                if (typeOfBusinessDetail == 2) {
                    rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_QTY_SELISIH] + "\" value=\"" + FRMHandler.userFormatStringDecimal(soItem.getQtySelisih()) + "\" class=\"formElemen qty_selisih\" style=\"text-align:right\"></div>");
                }
                if (typeOfBusinessDetail == 2) {
                    rowx.add("" + ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_KADAR_ID], null, "" + soItem.getKadarId(), val_kadar, key_kadar, "", "formElemen"));
                    rowx.add("" + ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_KADAR_OPNAME_ID], null, "" + soItem.getKadarOpnameId(), val_kadar, key_kadar, "", "formElemen"));
                    rowx.add("<div align=\"center\"><input type=\"text\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_BERAT] + "\" value=\"" + soItem.getBerat() /*FRMHandler.userFormatStringDecimal(soItem.getBerat()) */+ "\" class=\"formElemen\" style=\"text-align:right;background-color:#E8E8E8\" readOnly></div>");
                    rowx.add("<div align=\"center\"><input type=\"text\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_BERAT_OPNAME] + "\" value=\"" + soItem.getBeratOpname() /*FRMHandler.userFormatStringDecimal(soItem.getBeratOpname())*/ + "\" class=\"formElemen\" onKeyUp=\"javascript:countSelisih(event)\" style=\"text-align:right\"></div>");
                    rowx.add("<div align=\"center\"><input type=\"text\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_BERAT_SELISIH] + "\" value=\"" + FRMHandler.userFormatStringDecimal(soItem.getBeratSelisih()) + "\" class=\"formElemen\" style=\"text-align:right;background-color:#E8E8E8\" readOnly></div>");
                    rowx.add("<div align=\"center\"><input type=\"text\" size=\"10\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_REMARK] + "\" value=\"" + soItem.getRemark() + "\" class=\"formElemen\"></div>");
                }
                rowx.add("");
            } else {
                rowx.add("" + start + "");
                rowx.add("<a href=\"javascript:cmdEdit('" + String.valueOf(soItem.getOID()) + "')\">" + mat.getSku() + "</a>");
                if (useForGreenbowl.equals("1")) {
                    rowx.add(newmat.getBarCode());
                }
                //update opie-eyek 31-12-2012
                if ((soItemCounter != 0 && soItemCounter == soItem.getStockOpnameCounter() && i != 0) || (i != 0 && soItemSameMaterialId == soItem.getMaterialId())) {
                    //rowx.add("<font color=\"##347C17\" align=\"left\">"+mat.getName()+"</font>");
                    rowx.add("<font color=\"#FF0080\" align=\"left\">" + name + "</font>");
                    soItemCounter = soItem.getStockOpnameCounter();
                } else {
                    rowx.add(name);
                    soItemCounter = soItem.getStockOpnameCounter();
                    soItemSameMaterialId = soItem.getMaterialId();
                }
                rowx.add(unit.getCode());
                if (useForGreenbowl.equals("1")) {
                    rowx.add(color.getColorName());
                    rowx.add(masterTypeSize.getMasterName());
                }
                if (mat.getRequiredSerialNumber() == PstMaterial.REQUIRED) {
                    String where = PstSourceStockCode.fieldNames[PstSourceStockCode.FLD_SOURCE_ID] + "=" + soItem.getOID();
                    int cnt = PstSourceStockCode.getCount(where);
                    if (cnt < soItem.getQtyOpname()) {
                        if (listError.size() == 0) {
                            listError.add("Pesan Kesalahan: ");
                        }
                        listError.add("" + listError.size() + ". Jumlah Serial kode barang '" + mat.getName() + "' tidak sama dengan jumlah qty opname");
                    }
                    rowx.add("<div align=\"right\"><a href=\"javascript:stockcode('" + String.valueOf(soItem.getOID()) + "')\">[ST.CD]</a> " + FRMHandler.userFormatStringDecimal(soItem.getQtyOpname()) + "</div>");
                } else {
                    if (typeOfBusinessDetail == 2) {
                        rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(soItem.getQtyItem()) + "</div>");
                    }
                    rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(soItem.getQtyOpname()) + "</div>");
                    if (typeOfBusinessDetail == 2) {
                        rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(soItem.getQtySelisih()) + "</div>");
                    }
                }
                if (typeOfBusinessDetail == 2) {
                    rowx.add("<div align=\"right\">" + String.format("%,.2f", kadar.getKadar()) + "</div>");
                    rowx.add("<div align=\"right\">" + String.format("%,.2f", kadarOpname.getKadar()) + "</div>");
                    rowx.add("<div align=\"right\">" + String.format("%,.3f", soItem.getBerat()) + "</div>");
                    rowx.add("<div align=\"right\">" + String.format("%,.3f", soItem.getBeratOpname()) + "</div>");
                    rowx.add("<div align=\"right\">" + String.format("%,.3f", soItem.getBeratSelisih()) + "</div>");
                    rowx.add("" + soItem.getRemark());
                }

                // add by fitra 17-05-2014            
                rowx.add("<div align=\"center\"> <a href=\"javascript:cmdNewDelete('" + String.valueOf(soItem.getOID()) + "')\"><img src=" + approot + "/images/x3.png align=\"center\" ></a></div>");
            }
            lstData.add(rowx);
        }

        rowx = new Vector();
        if (iCommand == Command.ADD || (iCommand == Command.SAVE && frmObject.errorSize() > 0 && soItemId == 0)) {
            rowx.add("" + (start + 1));
            rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID] + "\" value=\"" + ""
                    + "\"><input type=\"text\" size=\"13\" name=\"matCode\" value=\"" + "" + "\" class=\"formElemen\" onKeyDown=\"javascript:cekEnter(event)\"> <a href=\"javascript:cmdCheck()\">CHK</a>");
            //rowx.add("<input type=\"text\" size=\"40\" name=\"matItem\" value=\""+""+"\" class=\"formElemen\" style=\"background-color:#E8E8E8\" readOnly>");
            if (useForGreenbowl.equals("1")) {
                rowx.add("<input type='text' name='matBarcode' class='formElemen' readOnly style=\"background-color:#E8E8E8\">");
            }
            rowx.add("<input type=\"text\" size=\"20\" name=\"matItem\" value=\"" + "" + "\" class=\"formElemen\" onKeyDown=\"javascript:cekEnter(event)\" id=\"txt_materialname\"> <a href=\"javascript:cmdCheck()\">CHK</a>");
            rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID] + "\" value=\"" + ""
                    + "\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\"" + "" + "\" class=\"formElemen\" style=\"background-color:#E8E8E8\" readOnly>");
            //rowx.add("<input type=\"text\" size=\"15\" name=\"matCat\" value=\""+""+"\" class=\"formElemen\" style=\"background-color:#E8E8E8\" readOnly>");
            //rowx.add("<input type=\"text\" size=\"15\" name=\"matSCat\" value=\""+""+"\" class=\"formElemen\" style=\"background-color:#E8E8E8\" readOnly>");
            if (useForGreenbowl.equals("1")) {
                rowx.add("<input type='text' name='matColor' size='15' class='formElemen' readOnly style=\"background-color:#E8E8E8\">");
                rowx.add("<input type='text' name='matSize' size='5' class='formElemen' readOnly style=\"background-color:#E8E8E8\">");
            }
            if (typeOfBusinessDetail == 2) {
                rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_QTY_ITEM] + "\" value=\"\" class=\"formElemen\" onKeyUp=\"javascript:calculate(event)\" style=\"text-align:right\"></div>");
            }
            rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_QTY_OPNAME] + "\" value=\"\" class=\"formElemen\" onKeyUp=\"javascript:calculate(event)\" style=\"text-align:right\"></div>");
            if (typeOfBusinessDetail == 2) {
                rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_QTY_SELISIH] + "\" value=\"\" class=\"formElemen\" onKeyUp=\"javascript:calculate(event)\" style=\"text-align:right\"></div>");
            }
            if (typeOfBusinessDetail == 2) {
                rowx.add("" + ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_KADAR_ID], null, null, val_kadar, key_kadar, "", "formElemen"));
                rowx.add("" + ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_KADAR_OPNAME_ID], null, null, val_kadar, key_kadar, "", "formElemen"));
                rowx.add("<div align=\"center\"><input type=\"text\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_BERAT] + "\" value=\"" + "" + "\" class=\"formElemen\" style=\"text-align:right;background-color:#E8E8E8\" readOnly></div>");
                rowx.add("<div align=\"center\"><input type=\"text\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_BERAT_OPNAME] + "\" value=\"" + "" + "\" class=\"formElemen\" onKeyUp=\"javascript:countSelisih(event)\" style=\"text-align:right\"></div>");
                rowx.add("<div align=\"center\"><input type=\"text\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_BERAT_SELISIH] + "\" value=\"" + "" + "\" class=\"formElemen\" style=\"text-align:right;background-color:#E8E8E8\" readOnly></div>");
                rowx.add("<div align=\"center\"><input type=\"text\" size=\"10\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_REMARK] + "\" value=\"" + "" + "\" class=\"formElemen\"></div>");
            }
            rowx.add("");
            lstData.add(rowx);
        }

        //added by dewok 2018 for jewelry
        if (typeOfBusinessDetail == 2) {
            Vector rowAdd = new Vector(1, 1);
            rowAdd.add("");
            rowAdd.add("");
            rowAdd.add("");
            rowAdd.add("<div align=\"right\"><b>Total :</b></div>");
            rowAdd.add("<div align=\"right\"><b>" + String.format("%,.0f", totalQty) + "</b></div>");
            rowAdd.add("<div align=\"right\"><b>" + String.format("%,.0f", totalQtyOpname) + "</b></div>");
            rowAdd.add("<div align=\"right\"><b>" + String.format("%,.0f", totalQtySelisih) + "</b></div>");
            rowAdd.add("");
            rowAdd.add("");
            rowAdd.add("<div align=\"right\"><b>" + String.format("%,.3f", totalBerat) + "</b></div>");
            rowAdd.add("<div align=\"right\"><b>" + String.format("%,.3f", totalBeratOpname) + "</b></div>");
            rowAdd.add("<div align=\"right\"><b>" + String.format("%,.3f", totalBeratSelisih) + "</b></div>");
            rowAdd.add("");
            rowAdd.add("");
            lstData.add(rowAdd);
        }

        list.add(ctrlist.draw());
        list.add(listError);
        return list;
    }
%>

<%!//
    public Vector drawListOpnameAllItem(int language, int iCommand, FrmMatStockOpnameItem frmObject,
            MatStockOpnameItem objEntity, Vector objectClass, long soItemId, int start, String approot,
            Vector listOpname, int typeOfBusinessDetail, long oidLocation) {
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.addHeader(textListOrderItem[language][0], "1%");
        ctrlist.addHeader(textListOrderItem[language][1], "8%");
        if (typeOfBusinessDetail != 2) {
            ctrlist.addHeader(textListOrderItem[language][8], "8%");
        }
        ctrlist.addHeader(textListOrderItem[language][2], "15%");
        ctrlist.addHeader(textListOrderItem[language][3], "5%");
        if (typeOfBusinessDetail == 2) {
            ctrlist.addHeader("Qty", "5%");
            ctrlist.addHeader("Qty SO", "5%");
            ctrlist.addHeader("Qty Selisih", "5%");
            ctrlist.addHeader("Kadar", "5%");
            ctrlist.addHeader("Kadar SO", "5%");
            ctrlist.addHeader("Berat", "5%");
            ctrlist.addHeader("Berat SO", "5%");
            ctrlist.addHeader("Berat Selisih", "5%");
            ctrlist.addHeader("Remark", "10%");
        } else {
            ctrlist.addHeader(textListOrderItem[language][6], "5%");
        }
        ctrlist.addHeader(textListOrderItem[language][7], "1%");
        Vector lstData = ctrlist.getData();
        Vector rowx = new Vector(1, 1);
        Vector list = new Vector(1, 1);
        Vector listError = new Vector(1, 1);

        ctrlist.reset();
        ctrlist.setLinkRow(1);
        int index = -1;
        if (start < 0) {
            start = 0;
        }

        int soItemCounter = 0;
        long soItemSameMaterialId = 0;

        //added by dewok 2018 for jewel
        Vector key_kadar = new Vector(1, 1);
        Vector val_kadar = new Vector(1, 1);
        Vector<Kadar> listKadar = PstKadar.list(0, 0, "", "");
        for (Kadar k : listKadar) {
            key_kadar.add("" + k.getKadar());
            val_kadar.add("" + k.getOID());
        }
        System.out.println(objectClass.size());
        for (int i = 0; i < objectClass.size(); i++) {
            Vector temp = (Vector) objectClass.get(i);
            MatStockOpnameItem soItem = (MatStockOpnameItem) temp.get(0);
            Material mat = (Material) temp.get(1);
            Unit unit = (Unit) temp.get(2);
            Category cat = (Category) temp.get(3);
            SubCategory scat = (SubCategory) temp.get(4);
            rowx = new Vector();
            start = start + 1;

            String name = "";
            name = mat.getName();
            //if (name.compareToIgnoreCase("\"") >= 0 || name.compareToIgnoreCase("'") >= 0) {
            name = name.replace('\"', '`');
            name = name.replace('\'', '`');
            //}

            //counter
            //
            if (i == 0) {
                soItemCounter = soItem.getStockOpnameCounter();
                soItemSameMaterialId = soItem.getMaterialId();
            }

            //added by dewok 2018 for litama
            Material newmat = new Material();
            Category category = new Category();
            Color color = new Color();
            Kadar kadar = new Kadar();
            Kadar kadarOpname = new Kadar();
            try {
                newmat = PstMaterial.fetchExc(soItem.getMaterialId());
                category = PstCategory.fetchExc(newmat.getCategoryId());
                color = PstColor.fetchExc(newmat.getPosColor());
                kadar = PstKadar.fetchExc(soItem.getKadarId());
                kadarOpname = PstKadar.fetchExc(soItem.getKadarOpnameId());
            } catch (Exception e) {

            }
            String itemName = "" + newmat.getName();
            if (typeOfBusinessDetail == 2) {
                if (newmat.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS) {
                    itemName = "" + category.getName() + " " + color.getColorName() + " " + newmat.getName();
                } else if (newmat.getMaterialJenisType() == Material.MATERIAL_TYPE_BERLIAN) {
                    itemName = "" + category.getName() + " " + color.getColorName() + " Berlian " + newmat.getName();
                }
                name = itemName;
            }

            if (soItemId == soItem.getOID()) {
                index = i;
            }
            
            if (index == i && (iCommand == Command.EDIT || iCommand == Command.ASK)) {
                /*
                ***** ##### FORM EDIT INI GAK PERNAH DIPAKE (KAYAKNYA) ##### *****
                */
                rowx.add("" + start);
                rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID] + "\" value=\"" + soItem.getMaterialId()
                        + "\"><input type=\"text\" size=\"15\" onKeyDown=\"javascript:keyDownCheck(event)\" name=\"matCode\" value=\"" + mat.getSku() + "\" class=\"formElemen\" readOnly>");
                rowx.add("<input type=\"text\" size=\"40\" name=\"matItem\" value=\"" + name + "\" class=\"formElemen\" style=\"background-color:#E8E8E8\" readOnly>");
                rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID] + "\" value=\"" + soItem.getUnitId()
                        + "\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\"" + unit.getCode() + "\" class=\"formElemen\" style=\"background-color:#E8E8E8\" readOnly>");
                //rowx.add("<input type=\"text\" size=\"15\" name=\"matCat\" value=\""+cat.getName()+"\" class=\"formElemen\" style=\"background-color:#E8E8E8\" readOnly>");
                //rowx.add("<input type=\"text\" size=\"15\" name=\"matSCat\" value=\""+scat.getName()+"\" class=\"formElemen\" style=\"background-color:#E8E8E8\" readOnly>");                
                rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_QTY_OPNAME] + "\" value=\"" + FRMHandler.userFormatStringDecimal(soItem.getQtyOpname()) + "\" class=\"formElemen\" style=\"text-align:right\"></div>");
                rowx.add("");
                /*
                ***** ##### KALO EDIT LEWAT DRAWLIST YANG SATUNYA ##### *****
                */
            } else {
                rowx.add("" + start + "");
                rowx.add("<a href=\"javascript:cmdEdit('" + String.valueOf(soItem.getOID()) + "')\">" + mat.getSku() + "</a>");
                //update opie-eyek 31-12-2012
                if ((soItemCounter != 0 && soItemCounter == soItem.getStockOpnameCounter() && i != 0) || (i != 0 && soItemSameMaterialId == soItem.getMaterialId())) {
                    //rowx.add("<font color=\"##347C17\" align=\"left\">"+mat.getName()+"</font>");
                    if (typeOfBusinessDetail != 2) {
                        rowx.add("" + newmat.getBarCode());
                    }
                    rowx.add("<font color=\"#FF0080\" align=\"left\">" + name + "</font>");
                    soItemCounter = soItem.getStockOpnameCounter();
                } else {
                    if (typeOfBusinessDetail != 2) {
                        rowx.add("" + newmat.getBarCode());
                    }
                    rowx.add(name);
                    soItemCounter = soItem.getStockOpnameCounter();
                    soItemSameMaterialId = soItem.getMaterialId();
                }
                rowx.add(unit.getCode());

                if (mat.getRequiredSerialNumber() == PstMaterial.REQUIRED) {
                    String where = PstSourceStockCode.fieldNames[PstSourceStockCode.FLD_SOURCE_ID] + "=" + soItem.getOID();
                    int cnt = PstSourceStockCode.getCount(where);
                    if (cnt < soItem.getQtyOpname()) {
                        if (listError.size() == 0) {
                            listError.add("Pesan Kesalahan: ");
                        }
                        listError.add("" + listError.size() + ". Jumlah Serial kode barang '" + name + "' tidak sama dengan jumlah qty opname");
                    }
                    rowx.add("<div align=\"right\"><a href=\"javascript:stockcode('" + String.valueOf(soItem.getOID()) + "')\">[ST.CD]</a> " + FRMHandler.userFormatStringDecimal(soItem.getQtyOpname()) + "</div>");
                } else {
                    if (typeOfBusinessDetail == 2) {
                        rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(soItem.getQtyItem()) + "</div>");
                    }
                    rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(soItem.getQtyOpname()) + "</div>");
                    if (typeOfBusinessDetail == 2) {
                        rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(soItem.getQtySelisih()) + "</div>");
                    }
                }
                if (typeOfBusinessDetail == 2) {
                    rowx.add("<div align=\"right\">" + String.format("%,.2f", kadar.getKadar()) + "</div>");
                    rowx.add("<div align=\"right\">" + String.format("%,.2f", kadarOpname.getKadar()) + "</div>");
                    rowx.add("<div align=\"right\">" + String.format("%,.3f", soItem.getBerat()) + "</div>");
                    rowx.add("<div align=\"right\">" + String.format("%,.3f", soItem.getBeratOpname()) + "</div>");
                    rowx.add("<div align=\"right\">" + String.format("%,.3f", soItem.getBeratSelisih()) + "</div>");
                    rowx.add("" + soItem.getRemark());
                }
                // add by fitra 17-05-2014            
                rowx.add("<div align=\"center\"> <a href=\"javascript:cmdNewDelete('" + String.valueOf(soItem.getOID()) + "')\"><img src=" + approot + "/images/x3.png align=\"center\" ></a></div>");
            }
            lstData.add(rowx);
        }

        if (iCommand == Command.ADDALL || (iCommand == Command.SAVE && frmObject.errorSize() > 0 && soItemId == 0)) {
            int count = 0;
            if (listOpname.size() > 0) {
                for (int i = 0; i < listOpname.size(); i++) {
                    Material material = (Material) listOpname.get(i);
                    String unitName = "";
                    String unitCode = "";
                    long unitId = 0;
                    //count = count + 1;
                    try {
                        Unit unit = PstUnit.fetchExc(material.getDefaultStockUnitId());
                        unitName = unit.getName();
                        unitCode = unit.getCode();
                        unitId = unit.getOID();
                    } catch (Exception e) {
                    }

                    String name = "";
                    String kodeMat = "";
                    String barcodeMat = "";
                    kodeMat = material.getSku();
                    barcodeMat = material.getBarCode();
                    name = material.getName();
                    name = name.replace('\"', '`');
                    name = name.replace('\'', '`');

                    //added by dewok 2018 for litama
                    Material newmat = new Material();
                    Category category = new Category();
                    Color color = new Color();
                    Kadar kadar = new Kadar();
                    Kadar kadarOpname = new Kadar();
                    MaterialDetail md = new MaterialDetail();
                    try {
                        newmat = PstMaterial.fetchExc(material.getOID());
                        category = PstCategory.fetchExc(newmat.getCategoryId());
                        color = PstColor.fetchExc(newmat.getPosColor());
                        kadar = PstKadar.fetchExc(material.getPosKadar());
                        kadarOpname = PstKadar.fetchExc(material.getPosKadar());
                        md = PstMaterialDetail.fetchExc(PstMaterialDetail.checkOIDMaterialDetailId(material.getOID()));
                    } catch (Exception e) {

                    }
                    String itemName = "" + newmat.getName();
                    String qtyDefault = "";
                    if (typeOfBusinessDetail == 2) {
                        if (newmat.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS) {
                            itemName = "" + category.getName() + " " + color.getColorName() + " " + newmat.getName();
                        } else if (newmat.getMaterialJenisType() == Material.MATERIAL_TYPE_BERLIAN) {
                            itemName = "" + category.getName() + " " + color.getColorName() + " Berlian " + newmat.getName();
                        }
                        name = itemName;
                        qtyDefault = "1";
                    }
                    
                    double qtyStock = SessMatCostingStokFisik.qtyMaterialBasedOnStockCard(material.getOID(), oidLocation, new Date(), 0);
                    double beratStock = SessMatCostingStokFisik.beratMaterialBasedOnStockCard(material.getOID(), oidLocation, new Date(), 0);                    
                    if (qtyStock <= 0) {continue;}
                    count = count + 1;
                    rowx = new Vector();
                    rowx.add("" + (count));
                    rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID] + "_" + i + "\" value=\"" + material.getOID() + ""
                            + "\"><input type=\"text\" size=\"13\" name=\"matCode\" value=\"" + kodeMat + "" + "\" class=\"formElemen\" onKeyDown=\"javascript:cekEnter(event)\">");
                    if (typeOfBusinessDetail != 2) {
                        rowx.add("<input type=\"text\" size=\"13\" name=\"matBarcode\" value=\"" + barcodeMat + "" + "\" class=\"formElemen\" onKeyDown=\"javascript:cekEnter(event)\" id=\"txt_kode\">");
                    }
                    rowx.add("<input type=\"text\" size=\"20\" name=\"matItem\" value=\"" + name + "" + "\" class=\"formElemen\" onKeyDown=\"javascript:cekEnter(event)\" id=\"txt_materialname\">");
                    rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID] + "_" + i + "\" value=\"" + unitId + ""
                            + "\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\"" + unitCode + "" + "\" class=\"formElemen\" style=\"background-color:#E8E8E8\" readOnly>");
                    if (typeOfBusinessDetail == 2) {
                        rowx.add("<div align=\"center\"><input type=\"text\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_QTY_ITEM] + "_" + i + "\" value=\"" + qtyDefault + "\" class=\"formElemen\" onKeyUp=\"javascript:calculate(event)\" style=\"text-align:right\"></div>");
                    }
                    rowx.add("<div align=\"center\"><input type=\"text\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_QTY_OPNAME] + "_" + i + "\" value=\"" + qtyDefault + "\" class=\"formElemen\" onKeyUp=\"javascript:calculate(event)\" style=\"text-align:right\"></div>");
                    if (typeOfBusinessDetail == 2) {
                        rowx.add("<div align=\"center\"><input type=\"text\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_QTY_SELISIH] + "_" + i + "\" value=\"0\" class=\"formElemen\" onKeyUp=\"javascript:calculate(event)\" style=\"text-align:right\"></div>");
                    }
                    if (typeOfBusinessDetail == 2) {
                        rowx.add("" + ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_KADAR_ID] + "_" + i, null, ""+kadar.getOID(), val_kadar, key_kadar, "", "formElemen"));
                        rowx.add("" + ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_KADAR_OPNAME_ID] + "_" + i, null, ""+kadar.getOID(), val_kadar, key_kadar, "", "formElemen"));
                        rowx.add("<div align=\"center\"><input type=\"text\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_BERAT] + "_" + i + "\" value=\"" + beratStock + "\" class=\"formElemen berat_" + i + "\" style=\"text-align:right;background-color:#E8E8E8\" readOnly></div>");
                        rowx.add("<div align=\"center\"><input type=\"text\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_BERAT_OPNAME] + "_" + i + "\" value=\"" + beratStock + "\" class=\"formElemen berat_opname_" + i + "\" onKeyUp=\"javascript:countSelisih2(" + i + ")\" style=\"text-align:right\"></div>");
                        rowx.add("<div align=\"center\"><input type=\"text\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_BERAT_SELISIH] + "_" + i + "\" value=\"" + "0" + "\" class=\"formElemen berat_selisih_" + i + "\" style=\"text-align:right;background-color:#E8E8E8\" readOnly></div>");
                        rowx.add("<div align=\"center\"><input type=\"text\" size=\"10\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_REMARK] + "_" + i + "\" value=\"" + "" + "\" class=\"formElemen\"></div>");
                    }
                    rowx.add("");
                    lstData.add(rowx);
                }
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
int docType = i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL,I_DocType.MAT_DOC_TYPE_OPN);
//int ownIndex = i_approval.getUserApprovalIndex(I_DocType.SYSTEM_MATERIAL,docType,deptx.getOID(),sectx.getOID(),postx.getOID());
//long appMappingId = i_approval.getUserApprovalId(I_DocType.SYSTEM_MATERIAL,docType,deptx.getOID(),sectx.getOID(),postx.getOID(),ownIndex);
%>

<%
/**
* get request data from current form
*/
int iCommand = FRMQueryString.requestCommand(request);
int prevCommand = FRMQueryString.requestInt(request,"prev_command");
int startItem = FRMQueryString.requestInt(request,"start_item");
int cmdItem = FRMQueryString.requestInt(request,"command_item");
int appCommand = FRMQueryString.requestInt(request,"approval_command");
long oidStockOpname = FRMQueryString.requestLong(request, "hidden_opname_id");
long oidStockOpnameItem = FRMQueryString.requestLong(request,"hidden_opname_item_id");
long oidLocation = FRMQueryString.requestLong(request, "hidden_location_id");
int notOpname = FRMQueryString.requestInt(request,"notOpname");

//get nama item, kode, supplier, kategori
String materialcode = FRMQueryString.requestString(request,"mat_code");
long materialgroup = FRMQueryString.requestLong(request,"txt_materialgroup");
long materialsubcategory = FRMQueryString.requestLong(request,"txt_material_sub_category");
long materialsupplier = FRMQueryString.requestLong(request,"txt_material_supplier");
String materialname = FRMQueryString.requestString(request,"txt_materialname");
int counterOpname = FRMQueryString.requestInt(request,"counterOpname");
Material objMaterial = new Material();
objMaterial.setCategoryId(materialgroup);
objMaterial.setSubCategoryId(materialsubcategory);
objMaterial.setSku(materialcode);
objMaterial.setName(materialname);

//added by dewok 2018 for emas lantakan
int lantakan = FRMQueryString.requestInt(request, "emas_lantakan");

/**
* initialization of some identifier
*/
int iErrCode = FRMMessage.NONE;
String msgString = "";

/**
* purchasing pr code and title
*/
String soCode = i_pstDocType.getDocCode(docType);
String opnTitle = "Opname Barang"; //i_pstDocType.getDocTitle(docType);
String soItemTitle = opnTitle + " Item";
if (typeOfBusinessDetail == 2) {
    opnTitle = "Stok Opname";
}

/**
* purchasing pr code and title
*/
String prCode = i_pstDocType.getDocCode(i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL,I_DocType.MAT_DOC_TYPE_DF));


//set list Opname nol
//mirahu
//20111018
if (oidStockOpname !=0 && iCommand==Command.ADD && (notOpname == 1 || notOpname == 2)){

  try {
    String orderBy = "MAT."+PstMaterial.fieldNames[PstMaterial.FLD_SKU];
    Vector listItemNotOpname = new Vector();
    if(notOpname == 1){
          listItemNotOpname = PstMaterial.getListMaterialOpnameAll(materialsupplier, objMaterial, 0, 0, orderBy, oidLocation, oidStockOpname);
    } else if (notOpname == 2){
        listItemNotOpname = PstMaterial.getListMaterialOpnameAllNew(materialsupplier, objMaterial, 0, 0, orderBy, oidLocation, oidStockOpname);
    }
        

    if (listItemNotOpname !=null && listItemNotOpname.size()>0){

       for(int i=0; i<=listItemNotOpname.size(); i++){

        Material mat = (Material) listItemNotOpname.get(i);
        if(mat !=null){
            MatStockOpnameItem opnItem = new MatStockOpnameItem();
            opnItem.setStockOpnameId(oidStockOpname);
            opnItem.setMaterialId(mat.getOID());
            opnItem.setUnitId(mat.getDefaultStockUnitId());
            opnItem.setQtyOpname(0);
            opnItem.setQtySold(0);
            opnItem.setQtySystem(0);
            opnItem.setCost(mat.getAveragePrice());
            opnItem.setPrice(0);
            int counter = PstMatStockOpnameItem.getIntCounter(oidStockOpname);
            opnItem.setStockOpnameCounter(counter);

           try{
              PstMatStockOpnameItem.insertExc(opnItem);

            }catch (Exception e){}
          }
        }
    }

 }

  catch(Exception e){

  System.out.println(e);

 }%>

<%
}


/**
* process on so main
*/
CtrlMatStockOpname ctrlMatStockOpname = new CtrlMatStockOpname(request);
iErrCode = ctrlMatStockOpname.action(Command.EDIT,oidStockOpname,userName,userId);
FrmMatStockOpname frmMatStockOpname = ctrlMatStockOpname.getForm();
MatStockOpname so = ctrlMatStockOpname.getMatStockOpname();

/**
* check if document already closed or not
*/
boolean documentClosed = false;
if(so.getStockOpnameStatus()==I_DocStatus.DOCUMENT_STATUS_CLOSED)
{
	documentClosed = true;
}

/**
* check if document may modified or not
*/
boolean privManageData = false;

ControlLine ctrLine = new ControlLine();
CtrlMatStockOpnameItem ctrlMatStockOpnameItem = new CtrlMatStockOpnameItem(request);
ctrlMatStockOpnameItem.setLanguage(SESS_LANGUAGE);

Vector listOpnameAll= new Vector();
if(iCommand == Command.ADDALL){
    String orderBy=" TRIM(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME]+") ASC ";
    String whereAdd = "";
    if (typeOfBusinessDetail == 2) {
        orderBy = " RIGHT(MAT."+PstMaterial.fieldNames[PstMaterial.FLD_SKU]+",7) ASC";
        if (lantakan == 1) {
            whereAdd = " AND MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_JENIS_TYPE] + "='" + Material.MATERIAL_TYPE_EMAS_LANTAKAN + "'";
        } else {
            whereAdd = " AND MAT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_JENIS_TYPE] + "!='" + Material.MATERIAL_TYPE_EMAS_LANTAKAN + "'";
        }
        whereAdd += " AND MAT." + PstMaterial.fieldNames[PstMaterial.FLD_GONDOLA_CODE] + "='" + so.getEtalaseId() + "'";
    }
    objMaterial.setCategoryId(so.getCategoryId());
    //objMaterial.setSupplierId(so.getSupplierId());
    listOpnameAll = PstMaterial.getListMaterialOpname(so.getSupplierId(), objMaterial, 0, 0, orderBy, oidLocation, oidStockOpname, whereAdd);
    System.out.println(listOpnameAll.size());
}

if(iCommand == Command.SAVEALL){
    iErrCode = ctrlMatStockOpnameItem.actionSaveAll(iCommand,oidStockOpnameItem,oidStockOpname,userName,userId,request,counterOpname);
}else{
     iErrCode = ctrlMatStockOpnameItem.action(iCommand,oidStockOpnameItem,oidStockOpname,userName,userId);
}
FrmMatStockOpnameItem frmMatStockOpnameItem = ctrlMatStockOpnameItem.getForm();
MatStockOpnameItem soItem = ctrlMatStockOpnameItem.getMatStockOpnameItem();
msgString = ctrlMatStockOpnameItem.getMessage();

String whereClauseItem = PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID]+"="+oidStockOpname;
String orderClauseItem = "";
int vectSizeItem = PstMatStockOpnameItem.getCount(whereClauseItem);
int recordToGetItem = 20;

if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST)
{
	startItem = ctrlMatStockOpnameItem.actionList(iCommand,startItem,vectSizeItem,recordToGetItem);
}

Vector listMatStockOpnameItem = PstMatStockOpnameItem.list(startItem,recordToGetItem,oidStockOpname);
if(listMatStockOpnameItem.size()<1 && startItem>0)
{
	 if(vectSizeItem-recordToGetItem > recordToGetItem)
	 {
		startItem = startItem - recordToGetItem;
	 }
	 else
	 {
		startItem = 0 ;
		iCommand = Command.FIRST;
		prevCommand = Command.FIRST;
	 }
	 listMatStockOpnameItem = PstMatStockOpnameItem.list(startItem,recordToGetItem,oidStockOpname);
}
//Switch command to make auto add
if ((iCommand == Command.SAVE) && (frmMatStockOpnameItem.errorSize() == 0))
{
	oidStockOpnameItem = 0;
    iCommand = Command.ADD;
}

//Verification type (using finger or not)
String verificationType = PstSystemProperty.getValueByName("KASIR_LOGIN_TYPE");




%>



<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title
><script language="JavaScript">
//------------------------- START JAVASCRIPT FUNCTION FOR PO MAIN -----------------------
function main(oid,comm){
	document.frm_matopname.command.value=comm;
	document.frm_matopname.hidden_opname_id.value=oid;
	document.frm_matopname.action="mat_opname_edit_new.jsp";
	document.frm_matopname.submit();
}

function stockcode(oid){
    document.frm_matopname.hidden_opname_item_id.value=oid;
	document.frm_matopname.command.value="<%=Command.EDIT%>";
	document.frm_matopname.prev_command.value="<%=prevCommand%>";
	document.frm_matopname.action="op_stockcode.jsp";
	document.frm_matopname.submit();
}

function cmdAdd()
{
	document.frm_matopname.hidden_opname_item_id.value="0";
	document.frm_matopname.command.value="<%=Command.ADD%>";
	document.frm_matopname.prev_command.value="<%=prevCommand%>";
	document.frm_matopname.action="mat_opname_item.jsp";
	if(compareDateForAdd()==true)
		document.frm_matopname.submit();
}

function cmdEdit(oidStockOpnameItem)
{
	document.frm_matopname.hidden_opname_item_id.value=oidStockOpnameItem;
	document.frm_matopname.command.value="<%=Command.EDIT%>";
	document.frm_matopname.prev_command.value="<%=prevCommand%>";
	document.frm_matopname.action="mat_opname_item.jsp";
	document.frm_matopname.submit();
}

function cmdAsk(oidStockOpnameItem)
{
	document.frm_matopname.hidden_opname_item_id.value=oidStockOpnameItem;
	document.frm_matopname.command.value="<%=Command.ASK%>";
	document.frm_matopname.prev_command.value="<%=prevCommand%>";
	document.frm_matopname.action="mat_opname_item.jsp";
	document.frm_matopname.submit();
}

function cmdSave()
{
	document.frm_matopname.prev_command.value="<%=prevCommand%>";
        <%if(iCommand == Command.ADDALL){%>
            document.frm_matopname.command.value="<%=Command.SAVEALL %>";
        <%}else{%>
            document.frm_matopname.command.value="<%=Command.SAVE%>";
        <%}%>
	document.frm_matopname.action="mat_opname_item.jsp";
	document.frm_matopname.submit();
}

function cmdConfirmDelete(oidStockOpnameItem)
{
	document.frm_matopname.hidden_opname_item_id.value=oidStockOpnameItem;
	document.frm_matopname.command.value="<%=Command.DELETE%>";
	document.frm_matopname.prev_command.value="<%=prevCommand%>";
	document.frm_matopname.approval_command.value="<%=Command.DELETE%>";
	document.frm_matopname.action="mat_opname_item.jsp";
	document.frm_matopname.submit();
        //cmdAdd();
}
// add by fitra 17-05-2014
function cmdNewDelete(oid){
var msg;
msg= "<%=textDelete[SESS_LANGUAGE][0]%>" ;
var agree=confirm(msg);
if (agree)
return cmdConfirmDelete(oid) ;
else
return cmdEdit(oid);
}


function cmdCancel(oidStockOpnameItem)
{
	document.frm_matopname.hidden_opname_item_id.value=oidStockOpnameItem;
	document.frm_matopname.command.value="<%=Command.EDIT%>";
	document.frm_matopname.prev_command.value="<%=prevCommand%>";
	document.frm_matopname.action="mat_opname_item.jsp";
	document.frm_matopname.submit();
}

function cmdBack()
{
	document.frm_matopname.command.value="<%=Command.EDIT%>";
	document.frm_matopname.action="mat_opname_edit_new.jsp";
	document.frm_matopname.submit();
}

function sumPrice()
{
}




function cmdCheck()
{
    if ("<%=typeOfBusinessDetail%>" == 2) {
        var etalase = document.frm_matopname.<%=FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_ETALASE_ID]%>.value;
        //var itemType = document.frm_matopname.<%=FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_OPNAME_ITEM_TYPE]%>.value;        
    }
	var strvalue  = "materialdosearch.jsp?command=<%=Command.FIRST%>"+
                "&etalase_id="+etalase+
                //"&item_type="+itemType+
                "&mat_code="+document.frm_matopname.matCode.value+
                "&txt_materialname="+document.frm_matopname.matItem.value+
                "&txt_materialgroup=<%=so.getCategoryId()%>"+ // document.frm_matopname.<%=FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_CATEGORY_ID]%>.value+
                "&location_id=<%=oidLocation%>"+
                "&stock_opname_id=<%=oidStockOpname%>"+
                "&txt_material_sub_category=<%=so.getSubCategoryId()%>"+
                "&txt_material_master_type=<%=so.getMasterGroupId()%>"+// document.frm_matopname.<%=FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_SUB_CATEGORY_ID]%>.value+
                "&txt_material_supplier="+document.frm_matopname.<%=FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_SUPPLIER_ID]%>.value;
	window.open(strvalue,"material", "height=500,width=700,left=300,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
}


function printExcel()
{
        var priceTypeId=document.frm_matopname.FRM_FIELD_PRICE_TYPE_ID.value;     
	var strvalue  = "mat_opname_item_excel.jsp?command=<%=Command.FIRST%>"+
					"&mat_code="+document.frm_matopname.matCode.value+
                                        "&txt_materialname="+document.frm_matopname.matItem.value+
					"&txt_materialgroup=<%=so.getCategoryId()%>"+
                                        "&location_id=<%=oidLocation%>"+
                                        "&etalase_id=<%=so.getEtalaseId()%>"+
                                        "&stock_opname_id=<%=oidStockOpname%>"+
					"&txt_material_sub_category=<%=so.getSubCategoryId()%>"+
                                        "&FRM_FIELD_PRICE_TYPE_ID="+document.frm_matopname.FRM_FIELD_PRICE_TYPE_ID.value+
					"&txt_material_supplier="+document.frm_matopname.<%=FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_SUPPLIER_ID]%>.value;
        window.open(strvalue,"material");                        
}

function printHtml(){
        var strvalue  = "mat_opname_item_html.jsp?command=<%=Command.FIRST%>"+
					"&mat_code="+document.frm_matopname.matCode.value+
                                        "&txt_materialname="+document.frm_matopname.matItem.value+
					"&txt_materialgroup=<%=so.getCategoryId()%>"+
                                        "&location_id=<%=oidLocation%>"+
                                        "&etalase_id=<%=so.getEtalaseId()%>"+
                                        "&stock_opname_id=<%=oidStockOpname%>"+
					"&txt_material_sub_category=<%=so.getSubCategoryId()%>"+
                                        "&FRM_FIELD_PRICE_TYPE_ID="+document.frm_matopname.FRM_FIELD_PRICE_TYPE_ID.value+
					"&txt_material_supplier="+document.frm_matopname.<%=FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_SUPPLIER_ID]%>.value;
        window.open(strvalue,"material", "height=500,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");  
}

function keyDownCheck(e){
    
   var trap = document.frm_matopname.trap.value;
       
    
   if (e.keyCode == 13 && trap==0) {
    document.frm_matopname.trap.value="1";
  
   }
   
   // add By fitra
    if (e.keyCode == 13 && trap == "0" && document.frm_matopname.matItem.value == "" ){
        document.frm_matopname.trap.value="0";
        cmdCheck();
   }
   if (e.keyCode == 13 && trap==1) {
       document.frm_matopname.trap.value="0";
       cmdCheck();
}
   if (e.keyCode == 27) {
       //alert("sa");
       document.frm_matopname.txt_materialname.value="";
   } 
}


function cekEnter(e)
{
	var trap = document.frm_matopname.trap.value;
   
    
   if (e.keyCode == 13 && trap==0) {
    document.frm_matopname.trap.value="1";
  
   }
   
   // add By fitra
    if (e.keyCode == 13 && trap == "0" && document.frm_matopname.matItem.value == "" ){
        document.frm_matopname.trap.value="0";
		cmdCheck();
	}
   if (e.keyCode == 13 && trap==1) {
       document.frm_matopname.trap.value="0";
       cmdCheck();
}
   if (e.keyCode == 27) {
       //alert("sa");
       document.frm_matopname.txt_materialname.value="";
   } 
}

function cmdListFirst()
{
	document.frm_matopname.command.value="<%=Command.FIRST%>";
	document.frm_matopname.prev_command.value="<%=Command.FIRST%>";
	document.frm_matopname.action="mat_opname_item.jsp";
	document.frm_matopname.submit();
}

function calculate(e){
    
        
   if (e.keyCode == 13) {
       
        cmdSave();
   }
}

//added by dewok for jewelry
function countSelisih(e){
    var berat = document.frm_matopname.<%=FrmMatStockOpnameItem.fieldNames[FrmMatStockOpnameItem.FRM_FIELD_BERAT]%>.value;
    var beratOpname = document.frm_matopname.<%=FrmMatStockOpnameItem.fieldNames[FrmMatStockOpnameItem.FRM_FIELD_BERAT_OPNAME]%>.value;
    var beratSelisih = +beratOpname - +berat;
    document.frm_matopname.<%=FrmMatStockOpnameItem.fieldNames[FrmMatStockOpnameItem.FRM_FIELD_BERAT_SELISIH]%>.value = beratSelisih.toFixed(3);
}

function cmdListPrev()
{
	document.frm_matopname.command.value="<%=Command.PREV%>";
	document.frm_matopname.prev_command.value="<%=Command.PREV%>";
	document.frm_matopname.action="mat_opname_item.jsp";
	document.frm_matopname.submit();
}

function cmdListNext()
{
	document.frm_matopname.command.value="<%=Command.NEXT%>";
	document.frm_matopname.prev_command.value="<%=Command.NEXT%>";
	document.frm_matopname.action="mat_opname_item.jsp";
	document.frm_matopname.submit();
}

function cmdListLast()
{
	document.frm_matopname.command.value="<%=Command.LAST%>";
	document.frm_matopname.prev_command.value="<%=Command.LAST%>";
	document.frm_matopname.action="mat_opname_item.jsp";
	document.frm_matopname.submit();
}

function cmdBackList()
{
	document.frm_matopname.command.value="<%=Command.FIRST%>";
	document.frm_matopname.action="mat_opname_list.jsp";
	document.frm_matopname.submit();
}

function refreshItemOpname(){

       document.frm_matopname.command.value="<%=Command.REFRESH%>";
       document.frm_matopname.prev_command.value="<%=prevCommand%>";
       document.frm_matopname.action="mat_opname_item.jsp";
       document.frm_matopname.submit();
}

function printForm()
{       
    if ("<%=typeOfBusinessDetail%>" == 2) {
        window.open("print_out_opname.jsp?hidden_opname_id=<%=oidStockOpname%>","pireport","scrollbars=yes,height=600,width=1200,left=100,status=no,toolbar=no,menubar=yes,location=no");
    } else {
        var valueOrder = document.frm_matopname.groupByElement.value;
	window.open("mat_opname_print_form.jsp?hidden_opname_id=<%=oidStockOpname%>&command=<%=Command.EDIT%>&groupByElement="+valueOrder+"","pireport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
    }
}

function printFormExcel()
{        var valueOrder = document.frm_matopname.groupByElement.value;
	window.open("mat_opname_print_form_excel.jsp?hidden_opname_id=<%=oidStockOpname%>&command=<%=Command.EDIT%>&groupByElement="+valueOrder+"","pireport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
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

function MM_findObj(n, d) { //v4.0
	var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
	d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
	if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
	for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
	if(!x && document.getElementById) x=document.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
	var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
	if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}
//------------------------- END JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------
</script>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<!-- #BeginEditable "styles" -->
<%if(menuUsed == MENU_ICON){%>
    <link href="../../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
<%}%>
<link rel="stylesheet" href="../../../styles/main.css" type="text/css">
<!-- #EndEditable -->
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
<%--autocomplate addd by fitra--%>
<script type="text/javascript" src="../../../styles/jquery-1.4.2.min.js"></script>

<link rel="stylesheet" type="text/css" href="../../../styles/style.css" />
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
    <%if(menuUsed == MENU_PER_TRANS){%>
    <tr>
        <td height="25" ID="TOPTITLE"> <!-- #BeginEditable "header" -->
            <%@ include file = "../../../main/header.jsp" %>
            <!-- #EndEditable -->
        </td>
    </tr>
    <tr>
        <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
            <%@ include file = "../../../main/mnmain.jsp" %>
            <!-- #EndEditable --> 
        </td>
    </tr>
    <%}else{%>
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
                    <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->
		  	<%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][1]%> &gt; <%=textListGlobal[SESS_LANGUAGE][4]%>
                        <%
                            if(typeOfBusinessDetail == 2) {
                                if (lantakan == 1) {
                                    out.print(" : Emas Lantakan");
                                }else{
                                    out.print(" : Emas & Berlian");
                                }
                            }
                        %>
                        <!-- #EndEditable -->
                    </td>
                </tr>
                <tr> 
                    <td><!-- #BeginEditable "content" -->                        
                        <form name="frm_matopname" method ="post" action="">                            
                            <input type="hidden" name="command" value="<%=iCommand%>">
                            <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                            <input type="hidden" name="start_item" value="<%=startItem%>">
                            <input type="hidden" name="type_doc" value="1">
                            <input type="hidden" name="hidden_opname_id" value="<%=oidStockOpname%>">
                            <input type="hidden" name="hidden_opname_item_id" value="<%=oidStockOpnameItem%>">
                            <input type="hidden" name="hidden_location_id" value="<%=oidLocation%>">
                            <input type="hidden" name="<%=FrmMatStockOpnameItem.fieldNames[FrmMatStockOpnameItem.FRM_FIELD_STOCK_OPNAME_ID]%>" value="<%=oidStockOpname%>">
                            <input type="hidden" name="<%=FrmMatStockOpnameItem.fieldNames[FrmMatStockOpnameItem.FRM_FIELD_QTY_SOLD]%>" value="<%=soItem.getQtySold()%>">
                            <input type="hidden" name="<%=FrmMatStockOpnameItem.fieldNames[FrmMatStockOpnameItem.FRM_FIELD_QTY_SYSTEM]%>" value="<%=soItem.getQtySystem()%>">
                            <input type="hidden" name="<%=FrmMatStockOpnameItem.fieldNames[FrmMatStockOpnameItem.FRM_FIELD_COST]%>" value="<%=soItem.getCost()%>">
                            <input type="hidden" name="<%=FrmMatStockOpnameItem.fieldNames[FrmMatStockOpnameItem.FRM_FIELD_PRICE]%>" value="<%=soItem.getPrice()%>">
                            <input type="hidden" name="approval_command" value="<%=appCommand%>">
                            <input type="hidden" name="notOpname" value="<%=notOpname%>">
                            <input type="hidden" name="txt_materialgroup" value="<%=so.getCategoryId()%>">
                            <input type="hidden" name="counterOpname" value="<%=listOpnameAll.size()%>">
                            <input type="hidden" name="emas_lantakan" value="<%=lantakan%>">              
                            <input type="hidden" name="trap" value="">
                            
                            <table width="100%" cellpadding="1" cellspacing="0">
                                <tr> 
                                    <td colspan="3"> 
                                        <hr size="1" noshade>
                                    </td>
                                </tr>
                                <tr align="center">
                                    <td colspan="3" class="title">
                                        <table width="100%"  border="0" cellspacing="1" cellpadding="1">
                                            <tr>
                                                <td width="8%"><%=textListOrderHeader[SESS_LANGUAGE][0]%></td>
                                                <td width="27%">: 
                                                    <b>
                                                        <%                                                    
                                                        if((so.getStockOpnameNumber()).length() > 0 && iErrCode==0)
                                                        {                                                        
                                                            out.println(so.getStockOpnameNumber());
                                                        }
                                                        else
                                                        {
                                                            out.println("");
                                                        }
                                                        %>
                                                    </b>
                                                </td>
                                                <td width="8%"><%=textListOrderHeader[SESS_LANGUAGE][5]%> </td>
                                                <td width="25%">:
                                                    <%
                                                    Vector obj_supplier = new Vector(1,1);
                                                    Vector val_supplier = new Vector(1,1);
                                                    Vector key_supplier = new Vector(1,1);
                                                    String wh_supp = PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE]+
                                                        " = "+PstContactClass.CONTACT_TYPE_SUPPLIER+
                                                        " AND "+PstContactList.fieldNames[PstContactList.FLD_PROCESS_STATUS]+
                                                        " != "+PstContactList.DELETE;
                                                    String ordBySupp =  " CONT." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME];
                                                    Vector vt_supp = PstContactList.listContactByClassType(0,0,wh_supp,ordBySupp);

                                                    //Vector vt_supp = PstContactList.list(0,0,"",PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE]);
                                                    /*String whClauseSupp = " CLS." + PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE] + "=" + PstContactClass.FLD_CLASS_VENDOR;
                                                    String ordBySupp =  " CONT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE];
                                                    Vector vt_supp = PstContactList.listContactByClassType(0,0,whClauseSupp,ordBySupp);*/

                                                    key_supplier.add(textListOrderHeader[SESS_LANGUAGE][9]+" "+textListOrderHeader[SESS_LANGUAGE][5]);
                                                    val_supplier.add("0");
                                                    for(int d=0; d<vt_supp.size(); d++)
                                                    {
                                                            ContactList cnt = (ContactList)vt_supp.get(d);
                                                            String cntName = cnt.getCompName();
                                                            if(cntName.length()==0)
                                                            {
                                                                    cntName = cnt.getPersonName()+" "+cnt.getPersonLastname();
                                                            }
                                                            val_supplier.add(String.valueOf(cnt.getOID()));
                                                            key_supplier.add(""+cnt.getContactCode()+" - "+cntName);
                                                    }
                                                    String select_supplier = ""+so.getSupplierId();
                                                    %>
                                                    <%=ControlCombo.draw(FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_SUPPLIER_ID],null,select_supplier,val_supplier,key_supplier,"disabled","formElemen")%>
                                                </td>
                                                <td width="12%"><%=textListOrderHeader[SESS_LANGUAGE][3]%></td>
                                                <td width="17%">:
                                                    <input type="text"  class="formElemen" name="<%=FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_STOCK_OPNAME_TIME]%>2" value="<%=so.getStockOpnameTime()%>"  size="10" style="text-align:right">
                                                </td>
                                            </tr>
                                            <tr>
                                                <td><%=textListOrderHeader[SESS_LANGUAGE][3]%></td>
                                                <td>: 
                                                    <%=ControlDate.drawTimeSec(FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_STOCK_OPNAME_DATE], (so.getStockOpnameDate()==null) ? new Date(): so.getStockOpnameDate(),"formElemen")%>
                                                </td>
                                                <%--
                                                <td><%=textListOrderHeader[SESS_LANGUAGE][6]%></td>
                                                <td>:
                                                    <%
                                                    Vector materGroup = PstCategory.list(0,0,"",PstCategory.fieldNames[PstCategory.FLD_CODE]);
                                                    Vector vectGroupVal = new Vector(1,1);
                                                    Vector vectGroupKey = new Vector(1,1);
                                                    vectGroupVal.add(textListOrderHeader[SESS_LANGUAGE][9]+" "+textListOrderHeader[SESS_LANGUAGE][6]);
                                                    vectGroupKey.add("0");
                                                    if(materGroup!=null && materGroup.size()>0)
                                                    {
                                                            for(int i=0; i<materGroup.size(); i++)
                                                            {
                                                                    Category mGroup = (Category)materGroup.get(i);
                                                                    vectGroupVal.add(mGroup.getName());
                                                                    vectGroupKey.add(""+mGroup.getOID());
                                                            }
                                                    }
                                                    out.println(ControlCombo.draw(FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_CATEGORY_ID],"formElemen", null, ""+so.getCategoryId(), vectGroupKey, vectGroupVal, null));
                                                    %>
                                                </td>
                                                --%>
                                                <%--</td>--%>
                                                <td><%=textListOrderHeader[SESS_LANGUAGE][6]%></td>
                                                <td>:
                                                    <select disabled="" id="<%=FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_CATEGORY_ID]%>"  name="<%=FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_CATEGORY_ID]%>" class="formElemen">
                                                        <option value="-1">Semua Category</option>
                                                        <%
                                                        Vector masterCatAcak = PstCategory.list(0,0,"",PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID]);
                                                        //Category newCategory = new Category();
                                                        //add opie-eyek 20130821
                                                        Vector materGroup = PstCategory.structureList(masterCatAcak) ;
                                                        Vector vectGroupVal = new Vector(1,1);
                                                        Vector vectGroupKey = new Vector(1,1);
                                                        if(materGroup!=null && materGroup.size()>0) {
                                                            String parent="";
                                                            //Vector<Category> resultTotal= new Vector();
                                                            Vector<Long> levelParent = new Vector<Long>();
                                                            for(int i=0; i<materGroup.size(); i++) {
                                                                Category mGroup = (Category)materGroup.get(i);
                                                                String select="";
                                                                if(mGroup.getOID()==so.getCategoryId()){
                                                                    select="selected";
                                                                }
                                                                if(mGroup.getCatParentId()!=0){
                                                                    for(int lv=levelParent.size()-1; lv > -1; lv--){
                                                                        long oidLevel=levelParent.get(lv);
                                                                        if(oidLevel==mGroup.getCatParentId()){
                                                                            break;
                                                                        }else{
                                                                            levelParent.remove(lv);
                                                                        }
                                                                    }
                                                                    parent="";
                                                                    for(int lv=0; lv<levelParent.size(); lv++){
                                                                       parent=parent+"&nbsp;&nbsp;";
                                                                    }
                                                                    levelParent.add(mGroup.getOID());

                                                                }else{
                                                                    levelParent.removeAllElements();
                                                                    levelParent.add(mGroup.getOID());
                                                                    parent="";
                                                                }
                                                        %>
                                                        <option value="<%=mGroup.getOID()%>" <%=select%>><%=parent%><%=mGroup.getName()%></option>
                                                        <%
                                                            }
                                                        } else {
                                                            vectGroupVal.add("Tidak Ada Category");
                                                            vectGroupKey.add("-1");
                                                        }
                                                        %>
                                                    </select>
                                                <td><%=textListOrderHeader[SESS_LANGUAGE][8]%></td>
                                                <td rowspan="2" valign="top">:
                                                    <textarea disabled="" name="textarea" class="formElemen" wrap="VIRTUAL" rows="4"><%=so.getRemark()%></textarea>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td><%=textListOrderHeader[SESS_LANGUAGE][2]%></td>
                                                <td>: <%=ControlDate.drawDateWithStyle(FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_STOCK_OPNAME_DATE], (so.getStockOpnameDate()==null) ? new Date() : so.getStockOpnameDate(), 1, -5, "formElemen", "")%></td>
                                                 <!-- combo box status update opie-eyek 31-12-2012-->
                                                <%
                                                    String mapping = PstSystemProperty.getValueByName("GROUP_TYPE_USE_IN_OPNAME");
                                                    if (mapping.length()>0 && !mapping.equals("Not Initialized")){
                                                        Vector listMasterGroup = PstMasterGroup.list(0, 0, PstMasterGroup.fieldNames[PstMasterGroup.FLD_TYPE_GROUP]+"="+mapping, "");
                                                        if (listMasterGroup.size()>0){
                                                            MasterGroup masterGroup = (MasterGroup) listMasterGroup.get(0);
                                                            Vector listType = PstMasterType.list(0, 0, 
                                                                PstMasterType.fieldNames[PstMasterType.FLD_TYPE_GROUP]+"="+masterGroup.getTypeGroup(),
                                                                PstMasterType.fieldNames[PstMasterType.FLD_MASTER_NAME]);

                                                             Vector vValue = new Vector(1,1);
                                                            Vector vKey = new Vector(1,1);

                                                            vValue.add("0");
                                                            vKey.add("-");
                                                            if (listType.size() > 0){
                                                                for (int x=0; x < listType.size(); x++){
                                                                    MasterType masterType = (MasterType) listType.get(x);
                                                                    vValue.add(""+masterType.getOID());
                                                                    vKey.add(masterType.getMasterName());
                                                                }
                                                            }    
                                                            String select_master = ""+so.getMasterGroupId();
                                                            %>
                                                                <td><%=masterGroup.getNamaGroup()%></td>
                                                                <td>: <%=ControlCombo.draw(FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_MASTER_TYPE_ID],"formElemen", null, select_master, vValue, vKey, "disabled")%></td>
                                                            <%
                                                        }
                                                    }
                                                %>
                                                <!-- end of status -->
                                                <td>&nbsp;</td>
                                                <td>&nbsp;</td>
                                                <td>&nbsp;</td>
                                            </tr>
                                            <tr>
                                                <td><%=textListOrderHeader[SESS_LANGUAGE][1]%></td>
                                                <td>:
                                                    <%
                                                    Vector obj_locationid = new Vector(1,1);
                                                    Vector val_locationid = new Vector(1,1);
                                                    Vector key_locationid = new Vector(1,1);
                                                    String whereClause = "";//PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE;
                                                    Vector vt_loc = PstLocation.list(0,0,whereClause, PstLocation.fieldNames[PstLocation.FLD_CODE]);
                                                    for(int d=0;d<vt_loc.size();d++)
                                                    {
                                                        Location loc = (Location)vt_loc.get(d);
                                                        val_locationid.add(""+loc.getOID()+"");
                                                        key_locationid.add(loc.getName());
                                                    }
                                                    String select_locationid = ""+so.getLocationId(); //selected on combo box
                                                    %>
                                                    <%=ControlCombo.draw(FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_LOCATION_ID], null, select_locationid, val_locationid, key_locationid, "disabled", "formElemen")%> </td>

                                                <%if(typeOfBusinessDetail == 2){%>
                                                <td>Etalase</td>
                                                <td>:
                                                    <%
                                                    Vector val_etalase = new Vector(1,1);
                                                    Vector key_etalase = new Vector(1,1);
                                                    Vector listEtalase = PstKsg.list(0, 0, ""+PstKsg.fieldNames[PstKsg.FLD_LOCATION_ID]+"="+so.getLocationId(), ""+PstKsg.fieldNames[PstKsg.FLD_CODE]);
                                                    for (int i=0; i<listEtalase.size(); i++) {
                                                        Ksg ksg = (Ksg) listEtalase.get(i);
                                                        if (ksg.getOID() == so.getEtalaseId()) {
                                                            Location loc = PstLocation.fetchExc(ksg.getLocationId());
                                                            val_etalase.add(""+ksg.getOID());
                                                            key_etalase.add(""+loc.getCode()+" - "+ksg.getCode());
                                                        }
                                                    }
                                                    %>
                                                    <%=ControlCombo.draw(FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_ETALASE_ID], null, ""+so.getEtalaseId(), val_etalase, key_etalase, "disabled", "formElemen")%>
                                                </td>
                                                <%--
                                                <td>Tipe Item Opname</td>
                                                <td>:
                                                    <%
                                                    Vector val_itemTipe = new Vector(1,1);
                                                    Vector key_itemType = new Vector(1,1);                            
                                                    val_itemTipe.add(""+Material.MATERIAL_TYPE_EMAS);
                                                    key_itemType.add(""+Material.MATERIAL_TYPE_TITLE[Material.MATERIAL_TYPE_EMAS]);
                                                    val_itemTipe.add(""+Material.MATERIAL_TYPE_BERLIAN);
                                                    key_itemType.add(""+Material.MATERIAL_TYPE_TITLE[Material.MATERIAL_TYPE_BERLIAN]);
                                                    val_itemTipe.add(""+Material.MATERIAL_TYPE_EMAS_LANTAKAN);
                                                    key_itemType.add(""+Material.MATERIAL_TYPE_TITLE[Material.MATERIAL_TYPE_EMAS_LANTAKAN]); 
                                                    %>
                                                    <%=ControlCombo.draw(FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_OPNAME_ITEM_TYPE], null, ""+so.getOpnameItemType(), val_itemTipe, key_itemType, "", "formElemen")%>
                                                </td>
                                                --%>
                                                <%}%>
                                                <td><%=textListOrderHeader[SESS_LANGUAGE][4]%></td>
                                                <td>:
                                                    <%
                                                    Vector obj_status = new Vector(1,1);
                                                    Vector val_status = new Vector(1,1);
                                                    Vector key_status = new Vector(1,1);

                                                    val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_DRAFT));
                                                    key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]);

                                                    //add by fitra
                                                    val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED));
                                                    key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED]);
                                                    if(vectSizeItem!=0){
                                                        val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_FINAL));
                                                        key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);
                                                    }
                                                    String select_status = ""+so.getStockOpnameStatus();
                                                    if(so.getStockOpnameStatus()==I_DocStatus.DOCUMENT_STATUS_CLOSED){
                                                        out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_CLOSED]);
                                                    }else if(so.getStockOpnameStatus()==I_DocStatus.DOCUMENT_STATUS_POSTED){
                                                        out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_POSTED]);
                                                    }else{
                                                        out.println(ControlCombo.draw(FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_STOCK_OPNAME_STATUS],null,select_status,val_status,key_status,"tabindex=\"4\" disabled","formElemen"));
                                                    }
                                                    %>
                                                    &nbsp;
                                                </td>
                                                <td>&nbsp;</td>
                                                <td><%System.out.println("===>>>>>>>>> proses xxxxx ");%></td>
                                                <td>&nbsp;</td>
                                            </tr>
                                            <!-- end of status
                                            <tr>
                                                <td>&nbsp;</td>
                                                <td>&nbsp;</td>
                                                <td>&nbsp;</td>
                                                <td><%//=strComboStatus%></td>
                                                <td>&nbsp;</td>
                                            </tr> -->
                                        </table>
                                    </td>
                                </tr>
                                <tr>
                                    <td valign="top">
                                        <table width="100%" cellpadding="1" cellspacing="1">
                                            <tr>
                                                <td colspan="3" >
                                                    <table width="100%" border="0" cellspacing="0" cellpadding="0" >
                                                        <tr align="left" valign="top">
                                                            <%
                                                            Vector listError  = new Vector();
                                                            try
                                                            {
                                                            %>
                                                            <td height="22" valign="middle" colspan="3"> 
                                                                <%
                                                                Vector list =  new Vector()  ;
                                                                if(iCommand == Command.ADDALL){
                                                                    list = drawListOpnameAllItem(SESS_LANGUAGE,iCommand,frmMatStockOpnameItem, soItem,listMatStockOpnameItem,oidStockOpnameItem,startItem, approot,listOpnameAll, typeOfBusinessDetail, so.getLocationId());                                                                    
                                                                }else{
                                                                    list = drawListOpnameItem(SESS_LANGUAGE,iCommand,frmMatStockOpnameItem, soItem,listMatStockOpnameItem,oidStockOpnameItem,startItem, approot,typeOfBusinessDetail); 
                                                                }
                                                                out.println(""+list.get(0));
                                                                listError = (Vector)list.get(1);
                                                                %> 
                                                            </td>
                                                            <%
                                                            }
                                                            catch(Exception e)
                                                            {
                                                                    System.out.println(e);
                                                            }
                                                            %>                                                                                                                                                                                                            
                                                        </tr>
                                                        <tr><td style="background-color: yellow; text-align: center; color: red"><b><%if (iCommand == Command.ADDALL && listOpnameAll.isEmpty()) {out.print("Tidak ada item");}%></b></td></tr>
                                                        <tr align="left" valign="top">
                                                            <td height="8" align="left" colspan="3" class="command">
                                                                <span class="command">
                                                                <%
                                                                int cmd = 0;
                                                                if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand == Command.NEXT || iCommand==Command.LAST){
                                                                    cmd =iCommand;
                                                                }else{
                                                                    if(iCommand == Command.NONE || prevCommand == Command.NONE)
                                                                        cmd = Command.FIRST;
                                                                    else
                                                                        cmd =prevCommand;
                                                                }
                                                                ctrLine.setLocationImg(approot+"/images");
                                                                ctrLine.initDefault();
                                                                out.println(ctrLine.drawImageListLimit(cmd,vectSizeItem,startItem,recordToGetItem));
                                                                %>
                                                                </span> 
                                                            </td>
                                                        </tr>
                                                        <tr align="left" valign="top">
                                                            <td height="22" valign="middle" colspan="3">
                                                                <span class="errfont">
                                                                <%
                                                                for(int k=0;k<listError.size();k++){
                                                                    if(k==0)
                                                                        out.println(listError.get(k)+"<br>");
                                                                    else
                                                                        out.println("&nbsp;&nbsp;&nbsp;"+listError.get(k)+"<br>");
                                                                }
                                                                %>
                                                                </span>
                                                            </td>
                                                        </tr>
                                                        <tr align="left" valign="top">
                                                        <td height="22" valign="middle" colspan="3">
                                                            <%
                                                            ctrLine.setLocationImg(approot+"/images");

                                                            // set image alternative caption
                                                            ctrLine.setAddNewImageAlt(ctrLine.getCommand(SESS_LANGUAGE,soCode+" Item",ctrLine.CMD_ADD,true));
                                                            ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,soCode+" Item",ctrLine.CMD_SAVE,true));
                                                            ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,soCode+" Item",ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,soCode+" Item",ctrLine.CMD_BACK,true)+" List");
                                                            ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,soCode+" Item",ctrLine.CMD_ASK,true));
                                                            ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,soCode+" Item",ctrLine.CMD_CANCEL,false));

                                                            ctrLine.initDefault();
                                                            ctrLine.setTableWidth("65%");
                                                            String scomDel = "javascript:cmdAsk('"+oidStockOpnameItem+"')";
                                                            String sconDelCom = "javascript:cmdConfirmDelete('"+oidStockOpnameItem+"')";
                                                            String scancel = "javascript:cmdEdit('"+oidStockOpnameItem+"')";
                                                            ctrLine.setCommandStyle("command");
                                                            ctrLine.setColCommStyle("command");

                                                            // set command caption
                                                            ctrLine.setAddCaption(ctrLine.getCommand(SESS_LANGUAGE," Item",ctrLine.CMD_ADD,true));
                                                            ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE," Item",ctrLine.CMD_SAVE,true));
                                                            ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE," Item",ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE," Item",ctrLine.CMD_BACK,true)+" List");
                                                            ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE," Item",ctrLine.CMD_ASK,true));
                                                            ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE," Item",ctrLine.CMD_DELETE,true));
                                                            ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE," Item",ctrLine.CMD_CANCEL,false));


                                                            if (privDelete){
                                                                    ctrLine.setConfirmDelCommand(sconDelCom);
                                                                    ctrLine.setDeleteCommand(scomDel);
                                                                    ctrLine.setEditCommand(scancel);
                                                            }else{
                                                                    ctrLine.setConfirmDelCaption("");
                                                                    ctrLine.setDeleteCaption("");
                                                                    ctrLine.setEditCaption("");
                                                            }

                                                            if(privAdd == false  && privUpdate == false){
                                                                    ctrLine.setSaveCaption("");
                                                            }

                                                            if (privAdd == false){
                                                                    ctrLine.setAddCaption("");
                                                            }

                                                            String  strDrawImage = ctrLine.drawImage(iCommand,iErrCode,msgString);
                                                            if((iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST) && strDrawImage.length()==0){
                                                                %>

                                                                <table width="50%" border="0" cellspacing="2" cellpadding="0">
                                                                    <tr>
                                                                        <td width="6%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,soCode+" Item",ctrLine.CMD_ADD,true)%>"></a></td>
                                                                        <td width="94%"><a href="javascript:cmdAdd()"><%=ctrLine.getCommand(SESS_LANGUAGE,soCode+" Item",ctrLine.CMD_ADD,true)%></a></td>
                                                                    </tr>
                                                                </table>

                                                                <%
                                                                }else{       
                                                                    out.println(strDrawImage);
                                                                }
                                                                %>
                                                        </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td colspan="3">&nbsp;</td>
                                            </tr>
                                            <%if(listMatStockOpnameItem!=null && listMatStockOpnameItem.size()>0){%>
                                            <tr>
                                                <td colspan="2" valign="top">&nbsp; </td>
                                                <td width="27%" valign="top"></td>
                                            </tr>
                                            <tr>
                                                <td colspan="3">&nbsp;</td>
                                            </tr>
                                            <%}%>
                                            <tr>
                                                <td colspan="3">
                                                    <%
                                                    ctrLine.setLocationImg(approot+"/images");

                                                    // set image alternative caption
                                                    ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,opnTitle,ctrLine.CMD_SAVE,true));
                                                    ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,opnTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,opnTitle,ctrLine.CMD_BACK,true)+" List");
                                                    ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,opnTitle,ctrLine.CMD_ASK,true));
                                                    ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,opnTitle,ctrLine.CMD_CANCEL,false));

                                                    ctrLine.initDefault();
                                                    ctrLine.setTableWidth("80%");
                                                    String scomDelMat = "javascript:main('"+oidStockOpname+"','"+Command.ASK+"')";
                                                    String sconDelComMat = "javascript:main('"+oidStockOpname+"','"+Command.DELETE+"')";
                                                    String scancelMat = "javascript:cmdEdit('"+oidStockOpname+"','"+Command.EDIT+"')";
                                                    ctrLine.setSaveCommand("javascript:main('"+oidStockOpname+"','"+Command.SAVE+"')");
                                                    ctrLine.setBackCommand("javascript:cmdBackList()");
                                                    ctrLine.setCommandStyle("command");
                                                    ctrLine.setColCommStyle("command");

                                                    // set command caption
                                                    ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE,opnTitle,ctrLine.CMD_SAVE,true));
                                                    ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,opnTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,opnTitle,ctrLine.CMD_BACK,true)+" List");
                                                    ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE,opnTitle,ctrLine.CMD_ASK,true));
                                                    ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE,opnTitle,ctrLine.CMD_DELETE,true));
                                                    ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE,opnTitle,ctrLine.CMD_CANCEL,false));

                                                    ctrLine.setConfirmDelCaption("");
                                                    ctrLine.setDeleteCaption("");
                                                    ctrLine.setEditCaption("");
                                                    ctrLine.setSaveCaption("");
                                                    ctrLine.setAddCaption("");

                                                    if(iCommand==Command.SAVE && frmMatStockOpname.errorSize()==0){
                                                            iCommand=Command.EDIT;
                                                    }

                                                    if(documentClosed){
                                                            ctrLine.setSaveCaption("");
                                                            ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,opnTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,opnTitle,ctrLine.CMD_BACK,true)+" List");
                                                            ctrLine.setDeleteCaption("");
                                                            ctrLine.setConfirmDelCaption("");
                                                            ctrLine.setCancelCaption("");
                                                    }
                                                    %>
                                                    <%=ctrLine.drawImage(Command.EDIT,iErrCode,msgString)%> 
                                                </td>
                                            </tr>
                                            <tr>
                                                <td colspan="3" align="right">
                                                    <%if(listMatStockOpnameItem.size()==0){%>

                                                    <table width="40%" border="0" cellpadding="0" cellspacing="0" align="right">
                                                        <tr>
                                                            <td width="10%">View</td>
                                                            <td width="20%">
                                                            <%
                                                                //add opie-eyek 20130805
                                                                ControlCheckBox controlCheckBox = new ControlCheckBox();
                                                                String ordPrice = PstPriceType.fieldNames[PstPriceType.FLD_INDEX]; 
                                                                Vector listPriceType = PstPriceType.list(0,0,"",ordPrice);
                                                                Vector prTypeVal = new Vector(1,1);
                                                                Vector prTypeKey = new Vector(1,1);
                                                                Vector vSelecetd = new Vector(1,1);
                                                                for(int i=0;i<listPriceType.size();i++) {
                                                                        PriceType prType = (PriceType)listPriceType.get(i);
                                                                        prTypeVal.add(""+prType.getOID()+"");
                                                                        prTypeKey.add(""+prType.getCode()+"");

                                                                        if(listPriceType.size()==1){
                                                                            vSelecetd.add(""+prType.getOID());
                                                                        }
                                                                }
                                                                controlCheckBox.setWidth(5);
                                                            %> 
                                                            <%=controlCheckBox.draw("FRM_FIELD_PRICE_TYPE_ID", prTypeVal, prTypeKey, vSelecetd)%>
                                                            </td>
                                                            <td width="65%" valign="top">
                                                                <a href="javascript:printHtml()">Print Html Opname</a> | <a href="javascript:printExcel()">Print Excel Opname</a>
                                                            </td>
                                                            <td width="5%" nowrap>&nbsp;</td>
                                                            <td width="5%" nowrap>&nbsp;</td>
                                                            <td width="5%" nowrap>&nbsp;</td>
                                                        </tr>
                                                    </table>

                                                    <%}else{%>
                                                    <table width="40%" border="0" cellpadding="0" cellspacing="0" align="right">
                                                        <tr>
                                                            <td width="10%">Group By</td>
                                                            <td width="20%">
                                                            <%

                                                                Vector val_groupby = new Vector(1,1);
                                                                Vector key_groupby = new Vector(1,1);

                                                                val_groupby.add("1");
                                                                key_groupby.add("By Kategori");
                                                                val_groupby.add("2");
                                                                key_groupby.add("By Supplier");
                                                                val_groupby.add("3");
                                                                key_groupby.add("By Rak Gondola");
                                                            %>  
                                                            <%= ControlCombo.draw("groupByElement", null, "", val_groupby, key_groupby, "", "formElemen")%>
                                                            </td>
                                                            <td width="5%" valign="top">
                                                                <a href="javascript:printForm('<%=oidStockOpname%>')"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0" alt="<%=textListGlobal[SESS_LANGUAGE][6]%>"></a>
                                                            </td>
                                                            <td width="30%" nowrap>&nbsp; <a href="javascript:printForm('<%=oidStockOpname%>')" class="command"><%=textListGlobal[SESS_LANGUAGE][6]%></a></td>
                                                            <td width="5%" valign="top"><a href="javascript:printFormExcel('<%=oidStockOpname%>')"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0" alt="<%=textListGlobal[SESS_LANGUAGE][6]%>"></a></td>
                                                            <td width="30%" nowrap>&nbsp; <a href="javascript:printFormExcel('<%=oidStockOpname%>')" class="command">Export to Excel</a></td>
                                                        </tr>
                                                    </table>

                                                    <%} %>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>               
                        </form>
    <SCRIPT language=JavaScript>
    var asu = "<%=iCommand%>";
    var asu2 = "<%=Command.ADD%>";
    if (asu == asu2)
    {
            document.frm_matopname.matItem.focus();
    }
    </SCRIPT>
            <!-- #EndEditable -->
                    </td> 
                </tr>
        <script language="JavaScript">
                 var addNotOpname = "<%=iCommand%>";
                 var notOpn = "<%=notOpname%>";
         if(addNotOpname == "<%=Command.ADD%>" && notOpn == "1" || notOpn == "2"){
         // CREDITS:
         // Automatic Page Refresher by Peter Gehrig and Urs Dudli www.24fun.com
         // Permission given to use the script provided that this notice remains as is.
         // Additional scripts can be found at http://www.hypergurl.com
         // Configure refresh interval (in seconds)
         var refreshinterval=5
         // Shall the coundown be displayed inside your status bar? Say "yes"  or "no" below: var displaycountdown="yes"
         // Do not edit the code below

         var starttime;
         var nowtime;
         var reloadseconds=0;
         var secondssinceloaded=0;
         function starttime() {
             starttime=new Date() ;
             starttime=starttime.getTime() ;
             countdown() ;
         }

         function countdown() {
         nowtime= new Date() ;
         nowtime=nowtime.getTime() ;
         secondssinceloaded=(nowtime-starttime)/1000 ;
         reloadseconds=Math.round(refreshinterval-secondssinceloaded) ;
         if (refreshinterval>=secondssinceloaded) {
             var timer=setTimeout("countdown()",1000) ;
         if (displaycountdown=="yes") {
             window.status="Page refreshing in "+reloadseconds+ " seconds" ;
          }
     } else {
       clearTimeout(timer);
       refreshItemOpname();//window.location.reload(true) ;
      }
     }
     window.onload=starttime ;
 }
 </script>
            </table>
        </td>
    </tr>
    <tr> 
        <td colspan="2" height="20"> <!-- #BeginEditable "footer" -->
            <%if(menuUsed == MENU_ICON){%>
                <%@include file="../../../styletemplate/footer.jsp" %>
            <%}else{%>
                <%@ include file = "../../../main/footer.jsp" %>
            <%}%>
        <!-- #EndEditable --> 
        </td>
    </tr>
</table>
 
</body>


<script language="JavaScript">
            // add By Fitra
                var trap = document.frm_matopname.trap.value;       
                document.frm_matopname.trap.value="0";
         document.frmvendorsearch.txt_materialname.focus();
</script>
<%--autocomplate--%>

<link type="text/css" rel="stylesheet" href="../../../styles/bootstrap3.1/css/bootstrap.css">
<script type="text/javascript" src="../../../styles/jquery.min.js"></script>
<script type="text/javascript" src="../../../styles/bootstrap3.1/js/bootstrap.min.js"></script>
<script type="text/javascript">
    $(document).ready(function(){
        var interval = 0;
        var final = 2;
        var finalText = "Final";
        var curentStatus = $('#FRM_FIELD_STOCK_OPNAME_STATUS').val();
        var textStatus = $("#FRM_FIELD_STOCK_OPNAME_STATUS option:selected").text();
        var verificationType = <%=verificationType%>;
        
        function ajaxUser2(url,data,type,appendTo,another,optional,optional2){
            $.ajax({
                url : ""+url+"",
                data: ""+data+"",
                type : ""+type+"",
                async : false,
                cache: false,
                success : function(data) {
                    //alert(data);
                    $(''+appendTo+'').html(data);
                },
                error : function(data){
                    alert('error');
                }
            }).done(function(data){

                if (another=="checkStatusUser2"){
                    
                   if (data==1){
                       clearInterval(interval);
                       alert("<%= textListGlobal[SESS_LANGUAGE][8]%>");
                       //alert(data);
                       //set nilai combobox
                       $("#FRM_FIELD_STOCK_OPNAME_STATUS").val(final);
                       $('#modalVerifikasi').modal('hide');
                       
                   }
                }else if (another=="checkUser2"){
                    clickFinger2();
                }
            });
        }
        
        function checkUser2(){
            var url = "<%=approot%>/AjaxUser";
            var loginId = $('#searchVerification').val();
            var data="command=<%=Command.ASSIGN%>&login="+loginId+"&language=<%= SESS_LANGUAGE%>&base=<%= baseURL%>&func='1'";
           
            ajaxUser2(url,data,"POST","#dynamicPlace","checkUser2","","");
        }
        
        function checkStatusUser2(userId){
            var url = "<%=approot%>/AjaxUser";
            var data="command=<%=Command.SEARCH%>&loginId="+userId+"";
            
            ajaxUser2(url,data,"POST","","checkStatusUser2","","");
        }
        
        function clickFinger2(){
            $('.loginFinger2').click(function(){
                var loginId= $('#searchVerification').val();
                interval= setInterval(function() {
                    checkStatusUser2(loginId);
                },5000);
            });
        }
        
        function changeCombo(){
            $('#FRM_FIELD_STOCK_OPNAME_STATUS').change(function(){
                var status = $('#FRM_FIELD_STOCK_OPNAME_STATUS').val();
                if (status==final){
                    //alert('test');
                    $("#FRM_FIELD_STOCK_OPNAME_STATUS").val(curentStatus);
                    $('#searchVerification').val('');
                    checkUser2();
                    $('#modalVerifikasi').modal('show');
                }else{
                    curentStatus = status;
                    textStatus= $("#FRM_FIELD_STOCK_OPNAME_STATUS option:selected").text();
                }
            });
        }
        
        if (verificationType==1){
            changeCombo();
        }

        $('#searchVerification').keyup(function(){
            checkUser2();
        });                                
    });
    
    function countSelisih2(i){
        var berat = $('.berat_'+i).val();
        var beratOpname = $('.berat_opname_'+i).val();
        var beratSelisih = +beratOpname - +berat;
        $('.berat_selisih_'+i).val(beratSelisih.toFixed(3));
    }
    
    function countSelisihQty() {
        var qtyItem = $('.qty_item').val();
        var qtyOpname = $('.qty_opname').val();
        var tySelisih = +qtyOpname - +qtyItem;
        $('.qty_selisih').val(tySelisih);
    }
    
    $('.qty_item').keyup(function() {
        countSelisihQty();
    });
    
    $('.qty_opname').keyup(function() {
        countSelisihQty();
    });
    
</script>
<style>
    .finger{
        width:20%; 
        height:auto;
        padding : 2%;
        float:left;
     }
    .finger_spot{
        width:100%;
        height: 80px;
        background-color :#e5e5e5;
        border : thin solid #c5c5c5;
        font-size: 14px;
        font-family:calibri;
        text-align:center;
        color :#FFF;
        border-radius: 3px;
    }

    .green{
       background-color : #5CB85C;
       border : thin solid #4CAE4C;
    }
</style>
<!--MODAL BOOTSTRAP -->
<div id="modalVerifikasi" class="modal fade" tabindex="-1">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" data-dismiss="modal" class="close"  aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="modal-title"><%=textListGlobal[SESS_LANGUAGE][7]%></h4>
            </div>
            <div class="modal-body" id="modal-body">
                <div class="row">
                    <div class="col-md-12">
                        <input type="text" class="form-control" id="searchVerification" placeholder="Input user..."/>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <div id="dynamicPlace"></div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" data-dismiss="modal" class="btn btn-danger"><%=textListGlobal[SESS_LANGUAGE][9]%></button>
            </div>
        </div>
    </div>
</div>
 <script src="../../../styles/jquery.autocomplete.js"></script>
<script>
jQuery(function(){
    $("#txt_materialname").autocomplete("list.jsp");
});
</script>           
<!-- #EndTemplate -->

</html>


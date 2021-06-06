<%-- 
    Document   : df_unit_material_lebur_edit
    Created on : Jan 24, 2018, 1:57:34 PM
    Author     : Dimata 007
--%>

<%@page import="com.dimata.posbo.entity.masterdata.PstKsg"%>
<%@page import="com.dimata.posbo.entity.masterdata.Ksg"%>
<%@page import="com.dimata.common.entity.logger.PstLogSysHistory"%>
<%@page import="com.dimata.common.entity.logger.LogSysHistory"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstColor"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstCategory"%>
<%@page import="com.dimata.posbo.entity.masterdata.Color"%>
<%@page import="com.dimata.posbo.entity.masterdata.Category"%>
<%@page import="com.dimata.posbo.form.search.FrmSrcMatDispatch"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.dimata.posbo.session.masterdata.SessPosting"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@ page language = "java" %>

<%@ page import = "java.util.*,
         com.dimata.common.entity.location.PstLocation,
         com.dimata.common.entity.location.Location,
         com.dimata.gui.jsp.ControlList,
         com.dimata.qdep.form.FRMHandler,
         com.dimata.qdep.form.FRMQueryString,
         com.dimata.qdep.entity.I_PstDocType,
         com.dimata.gui.jsp.ControlLine,
         com.dimata.qdep.form.FRMMessage,
         com.dimata.util.Command,
         com.dimata.gui.jsp.ControlDate,
         com.dimata.gui.jsp.ControlCombo,
         com.dimata.posbo.entity.masterdata.Material,
         com.dimata.posbo.entity.masterdata.Unit,
         com.dimata.posbo.entity.masterdata.PstMaterial,
         com.dimata.posbo.form.warehouse.CtrlMatDispatch,
         com.dimata.posbo.form.warehouse.FrmMatDispatch,
         com.dimata.posbo.form.warehouse.CtrlMatDispatchReceiveItem,
         com.dimata.posbo.form.warehouse.FrmMatDispatchReceiveItem,
         com.dimata.posbo.entity.warehouse.*" %>

<%@ page import="com.dimata.posbo.entity.admin.PstAppUser"%>
<%@ include file = "../../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER, AppObjInfo.OBJ_TRANSFER);%>
<%@ include file = "../../../main/checkuser.jsp" %>
<% boolean privApproval = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_APPROVE));%>

<%!//
    public static final String textListGlobal[][] = {
        {"Produk Dilebur", "Edit", "Tidak ada item produk dilebur", "Cetak Transfer Unit", "Proses transfer tidak dapat dilakukan pada lokasi yang sama"},
        {"Dispatch Unit", "Edit", "There is no Dispatch item unit", "Print Dispatch Unit", "Transfer cant'be proceed in same location"}
    };

    /* this constant used to list text of listHeader */
    public static final String textListOrderHeader[][] = {
        {"Nomor", "Lokasi Asal", "Lokasi Tujuan", "Tanggal", "Status", "Keterangan", "Nota Supplier", "Waktu", "Source", "Target"},
        {"Number", "From Location", "Destination", "Date", "Status", "Remark", "Supplier Invoice", "Time", "Source", "Target"}
    };

    /* this constant used to list text of listMaterialItem */
    public static final String textListOrderItem[][] = {
        {"No", "Sku", "Nama Barang", "Unit", "Qty", "Group", "Source", "Target", "@Nilai Stok", "Total", "Hapus"},//10
        {"No", "Code", "Goods Name", "Unit", "Qty", "Group", "Source", "Target", "@Stock Value", "Total", "Delete"}
    };

    public static final String textPosting[][] = {
        {"Anda yakin melakukan Posting Stok ?", "Anda yakin melakukan Posting Harga ?"},
        {"Are You Sure to Posting Stock ? ", "Are You Sure to Posting Cost Price?"}
    };

    public static final String textDelete[][] = {
        {"Apakah Anda Yakin Akan Menghapus Data ?"},
        {"Are You Sure to Delete This Data? "}
    };

    public double getPriceCost(Vector list, long oid) {
        double cost = 0.00;
        if (list.size() > 0) {
            for (int k = 0; k < list.size(); k++) {
                MatReceiveItem matReceiveItem = (MatReceiveItem) list.get(k);
                if (matReceiveItem.getMaterialId() == oid) {
                    cost = matReceiveItem.getCost();
                    break;
                }
            }
        }
        return cost;
    }

	public static void insertHistoryMaterialWithDetailAction(long userID, String nameUser, int cmd, long oid, Material material, String detail, String action) {
        try {
            LogSysHistory logSysHistory = new LogSysHistory();
            logSysHistory.setLogUserId(userID);
            logSysHistory.setLogLoginName(nameUser);
            logSysHistory.setLogApplication("Prochain");
            logSysHistory.setLogOpenUrl("warehouse/material/dispatch/df_unit_material_lebur_edit.jsp");
            logSysHistory.setLogUpdateDate(new Date());
            logSysHistory.setLogDocumentType("Pos material");
            logSysHistory.setLogUserAction(action);
            logSysHistory.setLogDocumentNumber(material.getFullName());
            logSysHistory.setLogDocumentId(oid);
            logSysHistory.setLogDetail(detail);

            if (!logSysHistory.getLogDetail().equals("") || cmd == Command.DELETE) {
                long oidHistory = PstLogSysHistory.insertLog(logSysHistory);
            }
        } catch (Exception e) {

        }
    }

    /**
     *
     * this method used to list all po item
     *
     */
    public Vector drawListGroupItem(int language, Vector objectClass, int start, boolean privManageData, String approot, int itemType, int status) {
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
            ctrlist.addHeader(textListOrderItem[language][5], "1%");
            ctrlist.addHeader(textListOrderItem[language][6], "45%");
            ctrlist.addHeader(textListOrderItem[language][7], "45%");
            Vector lstData = ctrlist.getData();
            Vector lstLinkData = ctrlist.getLinkData();
            Vector rowx = new Vector(1, 1);
            ctrlist.reset();
            int index = -1;
            if (start < 0) {
                start = 0;
            }
            for (int i = 0; i < objectClass.size(); i++) {
                MatDispatchReceiveItem dfRecItem = (MatDispatchReceiveItem) objectClass.get(i);
                rowx = new Vector();
                if (status == I_DocStatus.DOCUMENT_STATUS_CLOSED || status == I_DocStatus.DOCUMENT_STATUS_POSTED) {
                    rowx.add("<div align=\"center\">"+(start + i + 1)+"</div>");
                } else {
                    rowx.add("<div align=\"center\">" + (start + i + 1) + "</div><a href=\"javascript:editItem('" + String.valueOf(dfRecItem.getDfRecGroupId()) + "')\"><div align=\"center\">Edit</div>");
                }
                //String order = "DFRI." + PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DF_REC_GROUP_ID];
                String order = " RIGHT(MAT1." + PstMaterial.fieldNames[PstMaterial.FLD_SKU]+",7) ASC";
                Vector listItem = PstMatDispatchReceiveItem.list(0, 0, dfRecItem.getDfRecGroupId(), order);
                Vector listSource = drawListSourceItem(language, listItem, start, privManageData, approot, itemType);
                rowx.add(listSource.get(0));
                listError.add(listSource.get(1));
                Vector ListTargetItem = drawListTargetItem(language, listItem, start, itemType);
                rowx.add(ListTargetItem.get(0));
                listError.add(ListTargetItem.get(1));
                lstData.add(rowx);
            }
            result = ctrlist.draw();
        } else {
            result = "<div class=\"msginfo\">&nbsp;&nbsp;" + textListGlobal[language][2] + "</div>";
        }
        list.add(result);
        list.add(listError);
        return list;
    }
    /*
     * Drawlist Source Item
     * By Mirahu
     * update by opie-eyek 20140411 untuk penginputan serial code dan eror jika serial code tdak dimasukkan
     */

    public Vector drawListSourceItem(int language, Vector objectClass, int start, boolean privManageData, String approot, int itemType) {
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
            ctrlist.addHeader(textListOrderItem[language][1], "10%");
            ctrlist.addHeader(textListOrderItem[language][2], "15%");
            ctrlist.addHeader(textListOrderItem[language][3], "5%");
            ctrlist.addHeader(textListOrderItem[language][4], "5%");
            ctrlist.addHeader("Berat", "5%");
            if (itemType == Material.MATERIAL_TYPE_EMAS || itemType == Material.MATERIAL_TYPE_EMAS_LANTAKAN) {
                ctrlist.addHeader("Harga Emas", "5%");
            } else if (itemType == Material.MATERIAL_TYPE_BERLIAN) {
                ctrlist.addHeader("Harga Beli", "5%");
            } else {
                ctrlist.addHeader("Harga Beli", "5%");
            }
            ctrlist.addHeader("Oks/Batu", "5%");
            ctrlist.addHeader("Total HP", "5%");
            Vector lstData = ctrlist.getData();
            Vector lstLinkData = ctrlist.getLinkData();
            Vector rowx = new Vector(1, 1);
            ctrlist.reset();
            int index = -1;
            if (start < 0) {
                start = 0;
            }                             
            
            for (int i = 0; i < objectClass.size(); i++) {
                MatDispatchReceiveItem dfRecItem = (MatDispatchReceiveItem) objectClass.get(i);
                rowx = new Vector();
                //get berat n ongkos   
                MatDispatchItem mdi = new MatDispatchItem();                
                Material mat = new Material();
                Category category = new Category();
                Color color = new Color();
                try {
                    mdi = PstMatDispatchItem.fetchExc(dfRecItem.getSourceItem().getOID());
                    mat = PstMaterial.fetchExc(dfRecItem.getSourceItem().getMaterialId());
                    category = PstCategory.fetchExc(mat.getCategoryId());
                    color = PstColor.fetchExc(mat.getPosColor());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                
                String itemName = "" + mat.getName();
                if (mat.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS) {
                    itemName = "" + category.getName() + " " + color.getColorName() + " " + mat.getName();
                } else if (mat.getMaterialJenisType() == Material.MATERIAL_TYPE_BERLIAN) {
                    itemName = "" + category.getName() + " " + color.getColorName() + " Berlian " + mat.getName();
                }
                
                if (dfRecItem.getSourceItem().getMaterialId() != 0) {
                    rowx.add(dfRecItem.getSourceItem().getMaterialSource().getSku());                    
                    rowx.add("" + itemName);
                    rowx.add(dfRecItem.getSourceItem().getUnitSource().getCode());

                    if (dfRecItem.getSourceItem().getMaterialSource().getRequiredSerialNumber() == PstMaterial.REQUIRED) {
                        String where = PstDispatchStockCode.fieldNames[PstDispatchStockCode.FLD_DISPATCH_MATERIAL_ITEM_ID] + "=" + dfRecItem.getSourceItem().getOID();
                        int cnt = PstDispatchStockCode.getCount(where);
                        if (cnt < dfRecItem.getSourceItem().getQty()) {
                            if (listError.size() == 0) {
                                listError.add("Silahkan cek Source Item :");
                            }
                            listError.add("" + listError.size() + ". Jumlah serial kode stok " + dfRecItem.getSourceItem().getMaterialSource().getName() + " tidak sama dengan qty pengiriman");
                        }
                        rowx.add("<div align=\"right\"><a href=\"javascript:gostock('" + String.valueOf(dfRecItem.getSourceItem().getOID()) + "','0')\">[ST.CD]</a> " + FRMHandler.userFormatStringDecimal(dfRecItem.getSourceItem().getQty()) + "</div>");
                    } else {
                        rowx.add("<div align=\"right\">" + String.format("%,.0f",dfRecItem.getSourceItem().getQty()));
                    }
                    rowx.add("<div align=\"right\">" + String.format("%,.3f",mdi.getBeratCurrent()));
                    rowx.add("<div align=\"right\">" + String.format("%,.0f",dfRecItem.getSourceItem().getHpp())+".00");
                    rowx.add("<div align=\"right\">" + String.format("%,.0f",mdi.getOngkos())+".00");
                    rowx.add("<div align=\"right\">" + String.format("%,.0f",dfRecItem.getSourceItem().getHppTotal())+".00");
                    lstData.add(rowx);
                }
            }
            result = ctrlist.draw();
        } else {
            result = "<div class=\"msginfo\">&nbsp;&nbsp;" + textListGlobal[language][2] + "</div>";
        }
        list.add(result);
        list.add(listError);
        return list;
    }

    /*
     * Drawlist Target Item
     * By Mirahu
     * update by opie-eyek 20140411 untuk serial code
     */
    public Vector drawListTargetItem(int language, Vector objectClass, int start, int itemType) {
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
            ctrlist.addHeader(textListOrderItem[language][1], "10%");
            ctrlist.addHeader(textListOrderItem[language][2], "15%");
            ctrlist.addHeader(textListOrderItem[language][3], "5%");
            ctrlist.addHeader(textListOrderItem[language][4], "5%");
            ctrlist.addHeader("Berat", "5%");
            if (itemType == Material.MATERIAL_TYPE_EMAS || itemType == Material.MATERIAL_TYPE_EMAS_LANTAKAN) {
                ctrlist.addHeader("Harga Emas", "5%");
            } else if (itemType == Material.MATERIAL_TYPE_BERLIAN) {
                ctrlist.addHeader("Harga Beli", "5%");
            } else {
                ctrlist.addHeader("Harga Beli", "5%");
            }
            ctrlist.addHeader("Oks/Batu", "5%");
            ctrlist.addHeader("Total HP", "5%");
            ctrlist.addHeader("Keterangan", "5%");
            Vector lstData = ctrlist.getData();
            Vector lstLinkData = ctrlist.getLinkData();
            Vector rowx = new Vector(1, 1);
            ctrlist.reset();
            int index = -1;
            if (start < 0) {
                start = 0;
            }
            /**
             *
             * get data receive for get price cost
             *
             */
            double sourceBerat = 0;
            double sourceHargaBeli = 0;
            double sourceOngkos = 0;
            double sourceTotal = 0;

            double targetBerat = 0;
            double targetHargaBeli = 0;
            double targetOngkos = 0;
            double targetTotal = 0;
                    
            for (int i = 0; i < objectClass.size(); i++) {
                MatDispatchReceiveItem dfRecItem = (MatDispatchReceiveItem) objectClass.get(i);
                rowx = new Vector();
                start = start + 1;
                //get berat n ongkos   
                MatReceiveItem mri = new MatReceiveItem();
                Material mat = new Material();
                Category category = new Category();
                Color color = new Color();
                try {
                    mri = PstMatReceiveItem.fetchExc(dfRecItem.getTargetItem().getOID());
                    mat = PstMaterial.fetchExc(dfRecItem.getTargetItem().getMaterialId());
                    category = PstCategory.fetchExc(mat.getCategoryId());
                    color = PstColor.fetchExc(mat.getPosColor());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                
                String itemName = "" + mat.getName();
                if (mat.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS) {
                    itemName = "" + category.getName() + " " + color.getColorName() + " " + mat.getName();
                } else if (mat.getMaterialJenisType() == Material.MATERIAL_TYPE_BERLIAN) {
                    itemName = "" + category.getName() + " " + color.getColorName() + " Berlian " + mat.getName();
                }
                
                if (dfRecItem.getTargetItem().getMaterialId() != 0) {
                    rowx.add(dfRecItem.getTargetItem().getMaterialTarget().getSku());
                    rowx.add("" + itemName);
                    rowx.add(dfRecItem.getTargetItem().getUnitTarget().getCode());
                    if (dfRecItem.getTargetItem().getMaterialTarget().getRequiredSerialNumber() == PstMaterial.REQUIRED) {
                        String where = PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_RECEIVE_MATERIAL_ITEM_ID] + "=" + dfRecItem.getTargetItem().getOID();
                        int cnt = PstReceiveStockCode.getCount(where);
                        if (cnt < dfRecItem.getTargetItem().getQty()) {
                            if (listError.size() == 0) {
                                listError.add("Silahkan cek Target Item :");
                            }
                            listError.add("" + listError.size() + ". Jumlah serial kode stok " + dfRecItem.getTargetItem().getMaterialTarget().getName() + " tidak sama dengan qty penerimaan");
                        }
                        rowx.add("<div align=\"right\"><a href=\"javascript:gostock('" + String.valueOf(dfRecItem.getTargetItem().getOID()) + "','1')\">[ST.CD]</a> " + FRMHandler.userFormatStringDecimal(dfRecItem.getTargetItem().getQty()) + "</div>");
                    } else {
                        rowx.add("<div align=\"right\">" + String.format("%,.0f",dfRecItem.getTargetItem().getQty()));
                    }
                    rowx.add("<div align=\"right\">" + String.format("%,.3f",mri.getBerat()));
                    rowx.add("<div align=\"right\">" + String.format("%,.0f",dfRecItem.getTargetItem().getCost())+".00");
                    rowx.add("<div align=\"right\">" + String.format("%,.0f",mri.getForwarderCost())+".00");
                    rowx.add("<div align=\"right\">" + String.format("%,.0f",dfRecItem.getTargetItem().getTotal())+".00");
                    rowx.add("<div align=\"\">" + mri.getRemark());
                    lstData.add(rowx);
                    
                    //cari selisih
                    Vector<MatDispatchItem> listItemSource = PstMatDispatchItem.list(0, 0, ""
                            + "" + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ID] + " IN ("
                            + "SELECT DISTINCT " + PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DISPATCH_MATERIAL_ID] 
                            + " FROM " + PstMatDispatchReceiveItem.TBL_MAT_DISPATCH_RECEIVE_ITEM
                            + " WHERE " + PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DF_REC_GROUP_ID] + " = '"+dfRecItem.getDfRecGroupId()+"')", "");

                    Vector<MatReceiveItem> listItemTarget = PstMatReceiveItem.list(0, 0, ""
                            + "" + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + " IN ("
                            + "SELECT DISTINCT " + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] 
                            + " FROM " + PstMatReceive.TBL_MAT_RECEIVE
                            + " WHERE " + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ID] + " IN ("
                            + "SELECT DISTINCT " + PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DISPATCH_MATERIAL_ID] 
                            + " FROM " + PstMatDispatchReceiveItem.TBL_MAT_DISPATCH_RECEIVE_ITEM
                            + " WHERE " + PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DF_REC_GROUP_ID] + " = '"+dfRecItem.getDfRecGroupId()+"'))", "");

                    sourceBerat = 0;
                    sourceHargaBeli = 0;
                    sourceOngkos = 0;
                    sourceTotal = 0;
                    for (int g = 0; g < listItemSource.size(); g++) {
                        sourceBerat += listItemSource.get(g).getBeratCurrent();
                        sourceHargaBeli += listItemSource.get(g).getHpp();
                        sourceOngkos += listItemSource.get(g).getOngkos();
                        sourceTotal += listItemSource.get(g).getHppTotal();
                    }

                    targetBerat = 0;
                    targetHargaBeli = 0;
                    targetOngkos = 0;
                    targetTotal = 0;
                    for (int g = 0; g < listItemTarget.size(); g++) {
                        targetBerat += listItemTarget.get(g).getBerat();
                        targetHargaBeli += listItemTarget.get(g).getCost();
                        targetOngkos += listItemTarget.get(g).getForwarderCost();
                        targetTotal += listItemTarget.get(g).getTotal();
                    }
                    
                }
            }
            Vector rowy = new Vector(1, 1);
            rowy.add("<div align='right'><b>Selisih :</b></div>");
            rowy.add("");
            rowy.add("");
            rowy.add("");
            rowy.add("<div align='right'><b>"+String.format("%,.3f",(sourceBerat-targetBerat))+"</b></div>");
            rowy.add("<div align='right'><b>"+String.format("%,.0f",(sourceHargaBeli-targetHargaBeli))+".00</b></div>");
            rowy.add("<div align='right'><b>"+String.format("%,.0f",(sourceOngkos-targetOngkos))+".00</b></div>");
            rowy.add("<div align='right'><b>"+String.format("%,.0f",(sourceTotal-targetTotal))+".00</b></div>");
            rowy.add("");
            lstData.add(rowy);
                    
            result = ctrlist.draw();
        } else {
            result = "<div class=\"msginfo\">&nbsp;&nbsp;" + textListGlobal[language][2] + "</div>";
        }
        list.add(result);
        list.add(listError);
        return list;
    }

    public Vector drawListOrderItem(int language, Vector objectClass, int start) {
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
            ctrlist.addHeader(textListOrderItem[language][0], "3%");
            ctrlist.addHeader(textListOrderItem[language][1], "10%");
            ctrlist.addHeader(textListOrderItem[language][2], "30%");
            ctrlist.addHeader(textListOrderItem[language][3], "5%");
            ctrlist.addHeader(textListOrderItem[language][4], "5%");
            ctrlist.addHeader(textListOrderItem[language][1], "10%");
            ctrlist.addHeader(textListOrderItem[language][2], "40%");
            ctrlist.addHeader(textListOrderItem[language][3], "5%");
            ctrlist.addHeader(textListOrderItem[language][4], "5%");
            Vector lstData = ctrlist.getData();
            Vector lstLinkData = ctrlist.getLinkData();
            Vector rowx = new Vector(1, 1);
            ctrlist.reset();
            int index = -1;
            if (start < 0) {
                start = 0;
            }

            /**
             *
             * get data receive for get price cost
             *
             */
            for (int i = 0; i < objectClass.size(); i++) {
                MatDispatchReceiveItem dfRecItem = (MatDispatchReceiveItem) objectClass.get(i);
                rowx = new Vector();
                start = start + 1;
                rowx.add("" + start + "");
                rowx.add("<a href=\"javascript:editItem('" + String.valueOf(dfRecItem.getDfRecGroupId()) + "')\">" + dfRecItem.getSourceItem().getMaterialSource().getSku() + "</a>");
                rowx.add(dfRecItem.getSourceItem().getMaterialSource().getName());
                rowx.add(dfRecItem.getSourceItem().getUnitSource().getCode());

                if (dfRecItem.getSourceItem().getMaterialSource().getRequiredSerialNumber() == PstMaterial.REQUIRED) {
                    String where = PstDispatchStockCode.fieldNames[PstDispatchStockCode.FLD_DISPATCH_MATERIAL_ITEM_ID] + "=" + dfRecItem.getSourceItem().getOID();
                    int cnt = PstDispatchStockCode.getCount(where);
                    if (cnt < dfRecItem.getSourceItem().getQty()) {
                        if (listError.size() == 0) {
                            listError.add("Silahkan cek :");
                        }
                        listError.add("" + listError.size() + ". Jumlah serial kode stok " + dfRecItem.getSourceItem().getMaterialSource().getName() + " tidak sama dengan qty pengiriman");
                    }
                    rowx.add("<div align=\"right\"><a href=\"javascript:gostock('" + String.valueOf(dfRecItem.getSourceItem().getOID()) + "')\">[ST.CD]</a> " + FRMHandler.userFormatStringDecimal(dfRecItem.getSourceItem().getQty()) + "</div>");
                } else {
                    rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(dfRecItem.getSourceItem().getQty()));
                }
                rowx.add(dfRecItem.getTargetItem().getMaterialTarget().getSku());
                rowx.add(dfRecItem.getTargetItem().getMaterialTarget().getName());
                rowx.add(dfRecItem.getTargetItem().getUnitTarget().getCode());
                if (dfRecItem.getTargetItem().getMaterialTarget().getRequiredSerialNumber() == PstMaterial.REQUIRED) {
                    String where = PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_RECEIVE_MATERIAL_ITEM_ID] + "=" + dfRecItem.getTargetItem().getOID();
                    int cnt = PstReceiveStockCode.getCount(where);
                    if (cnt < dfRecItem.getTargetItem().getQty()) {
                        if (listError.size() == 0) {
                            listError.add("Silahkan cek :");
                        }
                        listError.add("" + listError.size() + ". Jumlah serial kode stok " + dfRecItem.getTargetItem().getMaterialTarget().getName() + " tidak sama dengan qty penerimaan");
                    }
                    rowx.add("<div align=\"right\"><a href=\"javascript:gostock('" + String.valueOf(dfRecItem.getTargetItem().getOID()) + "')\">[ST.CD]</a> " + FRMHandler.userFormatStringDecimal(dfRecItem.getTargetItem().getQty()) + "</div>");
                } else {
                    rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(dfRecItem.getTargetItem().getQty()));
                }
                lstData.add(rowx);
            }
            result = ctrlist.draw();
        } else {
            result = "<div class=\"msginfo\">&nbsp;&nbsp;" + textListGlobal[language][2] + "</div>";
        }
        list.add(result);
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
    int docType = i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL, I_DocType.MAT_DOC_TYPE_DF);
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

    long oidDispatchMaterial = FRMQueryString.requestLong(request, "hidden_dispatch_id");
    long oidReceiveMaterial = FRMQueryString.requestLong(request, "hidden_receive_id");
    long oidMatDispatchReceiveItem = FRMQueryString.requestLong(request, "hidden_dispatch_receive_id");
    long timemls = System.currentTimeMillis();
    int typePrint = FRMQueryString.requestInt(request, "type_print_tranfer");
    int ignoreDate = FRMQueryString.requestInt(request, FrmSrcMatDispatch.fieldNames[FrmSrcMatDispatch.FRM_FIELD_IGNORE_DATE]);
    int lantakan = FRMQueryString.requestInt(request, "emas_lantakan");

    String errMsg = "";
    int iErrCode = FRMMessage.ERR_NONE;
    
    /**
     * dispatch code and title
     */
    String dfCode = "";//i_pstDocType.getDocCode(docType);
    String dfTitle = textListGlobal[SESS_LANGUAGE][0];//i_pstDocType.getDocTitle(docType);
    String dfItemTitle = dfTitle + " Item";

    /**
     * action process
     */
    ControlLine ctrLine = new ControlLine();
    CtrlMatDispatch ctrlMatDispatch = new CtrlMatDispatch(request);
    iErrCode = ctrlMatDispatch.action(iCommand, oidDispatchMaterial, userName, userId);
    FrmMatDispatch frmdf = ctrlMatDispatch.getForm();
    MatDispatch df = ctrlMatDispatch.getMatDispatch();
    errMsg = ctrlMatDispatch.getMessage();

    oidDispatchMaterial = df.getOID();
    int itemType = df.getDispatchItemType();
    /*
     * For get oid Receive Material
     */
    oidReceiveMaterial = PstMatReceive.getOidReceiveMaterial(oidDispatchMaterial);

    /**
     * add opie-eyek 20131205 untuk posting stock
     */
    if (df.getDispatchStatus() == I_DocStatus.DOCUMENT_STATUS_FINAL) {
        try {
            SessPosting sessPosting = new SessPosting();
            sessPosting.postedDispatchDoc(oidDispatchMaterial);
            sessPosting.postedReceiveDoc(oidReceiveMaterial, userName, userId);
            df.setDispatchStatus(I_DocStatus.DOCUMENT_STATUS_CLOSED);
            iCommand = Command.EDIT;
        } catch (Exception e) {
            iCommand = Command.EDIT;
        }
    }

    boolean privManageData = true;
    if (df.getDispatchStatus() != I_DocStatus.DOCUMENT_STATUS_DRAFT) {
        privManageData = false;
    }
	
	if (typeOfBusiness.equals("2") && df.getDispatchStatus() == I_DocStatus.DOCUMENT_STATUS_CLOSED){
		try {
			Location locFrom = PstLocation.fetchExc(df.getLocationId());
			Location locTo = PstLocation.fetchExc(df.getDispatchTo());
			String whereDispatch = PstMatDispatchItem.fieldNames[PstMatConDispatchItem.FLD_DISPATCH_MATERIAL_ID]+"="+df.getOID()
					+ " AND "+PstMatDispatchItem.fieldNames[PstMatConDispatchItem.FLD_MATERIAL_ID]+" > 0";
			Vector listItem = PstMatDispatchItem.list(whereDispatch, "");
			if (listItem.size()>0){
				for (int i=0; i < listItem.size(); i++){
					MatDispatchItem matDispatchItem = (MatDispatchItem) listItem.get(i);
					Ksg ksgFrom = PstKsg.fetchExc(matDispatchItem.getGondolaId());
					Ksg ksgTo = PstKsg.fetchExc(matDispatchItem.getGondolaToId());
					
					Material material = PstMaterial.fetchExc(matDispatchItem.getMaterialId());
					
					String detail = "Transfer dari : "+locFrom.getCode()+" - "+ksgFrom.getCode()+" ke "
							+ locTo.getCode()+" - "+ksgTo.getCode()+"; No Transfer : "+df.getDispatchCode();
					insertHistoryMaterialWithDetailAction(userId, userName, Command.UPDATE, material.getOID(), material, detail, "Transfer");
				}
			}
		} catch (Exception exc){}
	}

    int recordToGetItem = 10;
    int vectSizeItem = PstMatDispatchReceiveItem.getCountGroup(PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DISPATCH_MATERIAL_ID] + " = " + oidDispatchMaterial);
    String order = "DFRI." + PstMatDispatchReceiveItem.fieldNames[PstMatDispatchReceiveItem.FLD_DF_REC_GROUP_ID];
    Vector listMatDispatchReceiveItem = PstMatDispatchReceiveItem.listGroup(startItem, recordToGetItem, oidDispatchMaterial, order);

    if (iCommand == Command.DELETE && iErrCode == 0) {
%>
<jsp:forward page="df_unit_material_lebur.jsp">
    <jsp:param name="command" value="<%=Command.FIRST%>"/>
</jsp:forward>
<%
    }
    double total = PstMatDispatchItem.getTotalTransfer(oidDispatchMaterial);
%>

<!DOCTYPE html>
<html>
    <head>
        <title>Dimata - ProChain POS</title>
        <script language="JavaScript">

            //------------------------- START JAVASCRIPT FUNCTION FOR PO MAIN -----------------------
            function cmdEdit(oid) {
                document.frm_matdispatchrecitem.command.value = "<%=Command.EDIT%>";
                document.frm_matdispatchrecitem.prev_command.value = "<%=prevCommand%>";
                document.frm_matdispatchrecitem.action = "df_unit_material_lebur_edit.jsp";
                document.frm_matdispatchrecitem.submit();
            }

            function gostock(oid, type) {
                document.frm_matdispatchrecitem.command.value = "<%=Command.EDIT%>";
                document.frm_matdispatchrecitem.hidden_dispatch_item_id.value = oid;
                document.frm_matdispatchrecitem.hidden_receive_item_id.value = oid;
                if (type == 0) {
                    document.frm_matdispatchrecitem.type_doc.value = "1";
                    document.frm_matdispatchrecitem.action = "df_stockcode.jsp";
                } else {
                    document.frm_matdispatchrecitem.type_doc.value = "3";
                    document.frm_matdispatchrecitem.action = "../receive/rec_wh_stockcode.jsp";
                }
                document.frm_matdispatchrecitem.submit();
            }

            function compare() {
                var dt = document.frm_matdispatchrecitem.<%=FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_DATE]%>_dy.value;
                var mn = document.frm_matdispatchrecitem.<%=FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_DATE]%>_mn.value;
                var yy = document.frm_matdispatchrecitem.<%=FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_DATE]%>_yr.value;
                var dt = new Date(yy, mn - 1, dt);
                var bool = new Boolean(compareDate(dt));
                return bool;
            }

            function cmdSave() {
            <%
                if ((df.getDispatchStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (df.getDispatchStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED)) {
            %>
                var statusDoc = document.frm_matdispatchrecitem.<%=FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_STATUS]%>.value;
                if (statusDoc == "<%=I_DocStatus.DOCUMENT_STATUS_FINAL%>") {
                    var conf = confirm("<%=textPosting[SESS_LANGUAGE][0]%>");
                    if (conf) {
                        document.frm_matdispatchrecitem.command.value = "<%=Command.SAVE%>";
                        document.frm_matdispatchrecitem.prev_command.value = "<%=prevCommand%>";
                        document.frm_matdispatchrecitem.action = "df_unit_material_lebur_edit.jsp";
                        if (compare() == true)
                            document.frm_matdispatchrecitem.submit();
                    }
                } else {
                    document.frm_matdispatchrecitem.command.value = "<%=Command.SAVE%>";
                    document.frm_matdispatchrecitem.prev_command.value = "<%=prevCommand%>";
                    document.frm_matdispatchrecitem.action = "df_unit_material_lebur_edit.jsp";
                    if (compare() == true)
                        document.frm_matdispatchrecitem.submit();
                }
            <%
            } else {
            %>
                alert("Document has been posted !!!");
            <%
                }
            %>
            }

            function cmdAsk(oid) {
            <%
                if ((df.getDispatchStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (df.getDispatchStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED)) {
            %>
                document.frm_matdispatchrecitem.command.value = "<%=Command.ASK%>";
                document.frm_matdispatchrecitem.prev_command.value = "<%=prevCommand%>";
                document.frm_matdispatchrecitem.action = "df_unit_material_lebur_edit.jsp";
                document.frm_matdispatchrecitem.submit();
            <%
            } else {
            %>
                alert("Document has been posted !!!");
            <%
                }
            %>
            }

            function cmdCancel() {
                document.frm_matdispatchrecitem.command.value = "<%=Command.CANCEL%>";
                document.frm_matdispatchrecitem.prev_command.value = "<%=prevCommand%>";
                document.frm_matdispatchrecitem.action = "df_unit_material_lebur_edit.jsp";
                document.frm_matdispatchrecitem.submit();
            }

            function cmdDelete(oid) {
                document.frm_matdispatchrecitem.command.value = "<%=Command.DELETE%>";
                document.frm_matdispatchrecitem.prev_command.value = "<%=prevCommand%>";
                document.frm_matdispatchrecitem.approval_command.value = "<%=Command.DELETE%>";
                document.frm_matdispatchrecitem.action = "df_unit_material_lebur_edit.jsp";
                document.frm_matdispatchrecitem.submit();
            }

            function cmdConfirmDelete(oid) {
                document.frm_matdispatchrecitem.command.value = "<%=Command.DELETE%>";
                document.frm_matdispatchrecitem.hidden_df_rec_group_id.value = oid;
                document.frm_matdispatchrecitem.prev_command.value = "<%=prevCommand%>";
                document.frm_matdispatchrecitem.approval_command.value = "<%=Command.DELETE%>";
                document.frm_matdispatchrecitem.action = "df_unit_material_lebur_item.jsp";
                document.frm_matdispatchrecitem.submit();
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

            function cmdBack() {
                document.frm_matdispatchrecitem.command.value = "<%=Command.FIRST%>";
                document.frm_matdispatchrecitem.prev_command.value = "<%=prevCommand%>";
                document.frm_matdispatchrecitem.action = "df_unit_material_lebur.jsp";
                document.frm_matdispatchrecitem.submit();
            }

            function printForm() {
                var chk = document.getElementById("hidePrice").checked;
                window.open("print_out_transfer_item_lebur.jsp?hidden_dispatch_id=<%=oidDispatchMaterial%>&command=<%=Command.EDIT%>&timemls=<%=timemls%>&hidePrice="+chk, "print_out_lebur");
            }

            function findInvoice() {
                window.open("df_wh_material_receive.jsp", "invoice_supplier", "scrollbars=yes,height=500,width=700,status=no,toolbar=no,menubar=yes,location=no");
            }

            //------------------------- END JAVASCRIPT FUNCTION FOR PO MAIN -----------------------

            //------------------------- START JAVASCRIPT FUNCTION FOR PO ITEM -----------------------
            function addItem() {
            <%
                if (df.getDispatchStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) {
            %>
                document.frm_matdispatchrecitem.command.value = "<%=Command.ADD%>";
                document.frm_matdispatchrecitem.action = "df_unit_material_lebur_item.jsp";
                if (compareDateForAdd() == true)
                    document.frm_matdispatchrecitem.submit();
            <%
            } else {
            %>
                alert("Document has been posted !!!");
            <%
                }
            %>
            }

            function addGroupItem() {
            <%
                if (df.getDispatchStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) {
            %>
                document.frm_matdispatchrecitem.command.value = "<%=Command.ADD%>";
                document.frm_matdispatchrecitem.action = "df_unit_material_lebur_item.jsp";
                if (compareDateForAdd() == true)
                    document.frm_matdispatchrecitem.submit();
            <%
            } else {
            %>
                alert("Document has been posted !!!");
            <%
                }
            %>
            }

            function addItemReceive() {
            <%
                if (df.getDispatchStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) {
            %>
                document.frm_matdispatchrecitem.command.value = "<%=Command.ADD%>";
                document.frm_matdispatchrecitem.action = "df_stock_material_receive_search.jsp";
                if (compareDateForAdd() == true)
                    document.frm_matdispatchrecitem.submit();
            <%
            } else {
            %>
                alert("Document has been posted !!!");
            <%
                }
            %>
            }

            function editItem(oid) {
            <%
                if ((df.getDispatchStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (df.getDispatchStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED)) {
            %>
                document.frm_matdispatchrecitem.command.value = "<%=Command.EDIT%>";
                document.frm_matdispatchrecitem.hidden_df_rec_group_id.value = oid;
                document.frm_matdispatchrecitem.action = "df_unit_material_lebur_item.jsp";
                document.frm_matdispatchrecitem.submit();
            <%
            } else {
            %>
                alert("Document has been posted !!!");
            <%
                }
            %>
            }

            function itemList(comm) {
                document.frm_matdispatchrecitem.command.value = comm;
                document.frm_matdispatchrecitem.prev_command.value = comm;
                document.frm_matdispatchrecitem.action = "df_unit_material_lebur_item.jsp";
                document.frm_matdispatchrecitem.submit();
            }

            /**
             * by Dyas
             * penambahan function viewHistoryTable() untuk menampilkan halaman logHistory
             */
            function viewHistoryTable() {
                var strvalue = "../../../main/historypo.jsp?command=<%=Command.FIRST%>" +
                        "&oidDocHistory=<%=oidDispatchMaterial%>";
                window.open(strvalue, "material", "height=600,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
            }

            function MM_swapImgRestore() { //v3.0
                var i, x, a = document.MM_sr;
                for (i = 0; a && i < a.length && (x = a[i]) && x.oSrc
                        ; i++)
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

        </script>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%if (menuUsed == MENU_ICON) {%>
        <link href="../../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
        <%}%>
        <link rel="stylesheet" href="../../../styles/main.css" type="text/css">
        <link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
    </head>

    <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('<%=approot%>', '<%=approot%>')">

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
                <td width="88%" valign="top" align="left">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                            <td height="20" class="mainheader">&nbsp;<%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][1]%>
                                <%if(lantakan == 1) {out.print(" : Emas Lantakan");}else{out.print(" : Emas & Berlian");}%>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <form name="frm_matdispatchrecitem" method="post" action="">
                                    <%
                                        try {
                                    %>
                                    <input type="hidden" name="command" value="">
                                    <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                                    <input type="hidden" name="start_item" value="<%=startItem%>">
                                    <input type="hidden" name="command_item" value="<%=cmdItem%>">
                                    <input type="hidden" name="approval_command" value="<%=appCommand%>">
                                    <input type="hidden" name="hidden_dispatch_id" value="<%=oidDispatchMaterial%>">
                                    <input type="hidden" name="hidden_receive_id"  value="<%=oidReceiveMaterial%>">
                                    <input type="hidden" name="hidden_dispatch_item_id" value="">
                                    <input type="hidden" name="hidden_receive_item_id" value="">
                                    <input type ="hidden" name="hidden_df_rec_group_id" value="">
                                    <input type ="hidden" name="type_doc" value="">
                                    <input type="hidden" name="sess_language" value="<%=SESS_LANGUAGE%>">
                                    <input type="hidden" name="<%=FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_CODE]%>" value="<%=df.getDispatchCode()%>">
                                    <input type="hidden" name="<%=FrmSrcMatDispatch.fieldNames[FrmSrcMatDispatch.FRM_FIELD_IGNORE_DATE]%>" value="<%=ignoreDate%>">
                                    <input type="hidden" name="<%=FrmSrcMatDispatch.fieldNames[FrmSrcMatDispatch.FRM_FIELD_LOCATION_TYPE]%>" value="<%=PstMatDispatch.FLD_TYPE_TRANSFER_LEBUR%>">
                                    <input type="hidden" name="<%=FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_LOCATION_TYPE]%>" value="<%=PstMatDispatch.FLD_TYPE_TRANSFER_LEBUR%>">
                                    <input type="hidden" name="emas_lantakan" value="<%=lantakan%>">
                                    <%if (lantakan == 1) {%>
                                    <input type="hidden" name="<%=FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_ITEM_TYPE]%>" value="<%=Material.MATERIAL_TYPE_EMAS_LANTAKAN%>">
                                    <%}%>
                                    
                                    <table width="100%" border="0">
                                        <tr>
                                            <td colspan="3">
                                                <table width="100%"  border="0" cellspacing="1" cellpadding="1">
                                                    <tr>
                                                        <td width="9%"><%=textListOrderHeader[SESS_LANGUAGE][0]%></td>
                                                        <td width="25%">: 
                                                            <b>
                                                                <%
                                                                    if (iCommand == Command.ADD) {
                                                                        out.println("Nomor otomatis");
                                                                    } else {
                                                                        if (df.getDispatchCode() != "" && iErrCode == 0) {
                                                                            out.println(df.getDispatchCode());
                                                                        } else {
                                                                            out.println("");
                                                                        }
                                                                    }
                                                                %>
                                                            </b>
                                                        </td>
                                                        <td width="9%"><%=textListOrderHeader[SESS_LANGUAGE][1]%></td>
                                                        <td width="31%">:
                                                            <%
                                                                Vector obj_locationid = new Vector(1, 1);
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
                                                                String select_locationid = "" + df.getLocationId(); //selected on combo box
                                                            %>
                                                            <%=ControlCombo.draw(FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_LOCATION_ID], null, select_locationid, val_locationid, key_locationid, "", "formElemen")%> </td>
                                                        <td width="12%"><%=textListOrderHeader[SESS_LANGUAGE][5]%></td>
                                                        <td>:</td>
                                                        <td width="17%" rowspan="2" align="right" valign="top"><textarea name="<%=FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_REMARK]%>" class="formElemen" wrap="VIRTUAL" rows="3"><%=df.getRemark()%></textarea></td>
                                                    </tr>

                                                    <tr>
                                                        <td><%=textListOrderHeader[SESS_LANGUAGE][3]%></td>
                                                        <td>: <%=ControlDate.drawDateWithStyle(FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_DATE], (df.getDispatchDate() == null) ? new Date() : df.getDispatchDate(), 1, -5, "formElemen", "")%></td>
                                                        <td><%=textListOrderHeader[SESS_LANGUAGE][2]%></td>
                                                        <td>:
                                                            <%
                                                                Vector obj_locationid1 = new Vector(1, 1);
                                                                Vector val_locationid1 = new Vector(1, 1);
                                                                Vector key_locationid1 = new Vector(1, 1);
                                                                String locWhClause = ""; //PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE;
                                                                String locOrderBy = String.valueOf(PstLocation.fieldNames[PstLocation.FLD_CODE]);
                                                                Vector vt_loc1 = PstLocation.list(0, 0, locWhClause, locOrderBy);

                                                                for (int d = 0; d < vt_loc.size(); d++) {
                                                                    Location loc1 = (Location) vt_loc.get(d);
                                                                    val_locationid1.add("" + loc1.getOID() + "");
                                                                    key_locationid1.add(loc1.getName());
                                                                }
                                                                String select_locationid1 = "" + df.getDispatchTo(); //selected on combo box
                                                            %>
                                                            <%=ControlCombo.draw(FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_TO], null, select_locationid1, val_locationid1, key_locationid1, "", "formElemen")%> </td>
                                                        <td>&nbsp;</td>
                                                    </tr>

                                                    <tr>
                                                        <td><%=textListOrderHeader[SESS_LANGUAGE][7]%></td>
                                                        <td>: <%=ControlDate.drawTimeSec(FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_DATE], (df.getDispatchDate() == null) ? new Date() : df.getDispatchDate(), "formElemen")%></td>
                                                        <td><%=textListOrderHeader[SESS_LANGUAGE][4]%></td>
                                                        <td>:
                                                            <%
                                                                Vector obj_status = new Vector(1, 1);
                                                                Vector val_status = new Vector(1, 1);
                                                                Vector key_status = new Vector(1, 1);
                                                                // update opie-eyek 19022013
                                                                // user bisa memfinalkan DF jika  :
                                                                // 1. punya priv. transfer approve = true
                                                                // 2. lokasi sumber (lokasi asal)  ada di lokasi-lokasi yg diassign ke user
                                                                boolean locationAssign = false;
                                                                locationAssign = PstDataCustom.checkDataCustom(userId, "user_location_map", df.getLocationId());
                                                                val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_DRAFT));
                                                                key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]);
                                                                //add by fitra
                                                                val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED));
                                                                key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED]);
                                                                if (listMatDispatchReceiveItem.size() > 0 && privApproval == true && locationAssign == true) {
                                                                    val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_FINAL));
                                                                    key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);
                                                                }
                                                                String select_status = "" + df.getDispatchStatus();
                                                                if (df.getDispatchStatus() == I_DocStatus.DOCUMENT_STATUS_CLOSED) {
                                                                    out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_CLOSED]);
                                                                } else if (df.getDispatchStatus() == I_DocStatus.DOCUMENT_STATUS_POSTED) {
                                                                    out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_POSTED]);
                                                                } else if (df.getDispatchStatus() == I_DocStatus.DOCUMENT_STATUS_FINAL && (privApproval == false || locationAssign == false)) {
                                                                    out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);
                                                                } else {
                                                            %>
                                                            <%=ControlCombo.draw(FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_STATUS], null, select_status, val_status, key_status, "", "formElemen")%>
                                                            <%}%>
                                                        </td>
                                                        <%--
                                                        <td width="">Tipe Item Lebur</td>
                                                        <td>:</td>
                                                        <td width="">
                                                            <%
                                                                Vector val_itemType = new Vector(1, 1);
                                                                Vector key_itemType = new Vector(1, 1);
                                                                val_itemType.add("" + Material.MATERIAL_TYPE_EMAS);
                                                                key_itemType.add("" + Material.MATERIAL_TYPE_TITLE[Material.MATERIAL_TYPE_EMAS]);
                                                                val_itemType.add("" + Material.MATERIAL_TYPE_BERLIAN);
                                                                key_itemType.add("" + Material.MATERIAL_TYPE_TITLE[Material.MATERIAL_TYPE_BERLIAN]);
                                                            %>
                                                            <%=ControlCombo.draw(FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_ITEM_TYPE], null, "" + df.getDispatchItemType(), val_itemType, key_itemType, "", "formElemen")%>
                                                        </td>
                                                        --%>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>

                                        <tr>
                                            <td colspan="3">
                                                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                    <tr align="left" valign="top">
                                                        <td height="22" valign="middle" colspan="3">
                                                            <%
                                                                Vector list = drawListGroupItem(SESS_LANGUAGE, listMatDispatchReceiveItem, startItem, privManageData, approot, itemType, df.getDispatchStatus());
                                                                out.println("" + list.get(0));
                                                                Vector listError = (Vector) list.get(1);
                                                            %>
                                                        </td>
                                                    </tr>

                                                    <%if (oidDispatchMaterial != 0) {%>

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

                                                    <tr align="left" valign="top">
                                                        <td height="22" colspan="3" valign="middle" class="errfont">
                                                            <%
                                                                try {
                                                                    if (listError.size() > 0) {
                                                                        for (int k = 0; k < listError.size(); k++) {
                                                                            Vector detailList = (Vector) listError.get(k);
                                                                            if (detailList.size() > 0) {
                                                                                for (int l = 0; l < detailList.size(); l++) {
                                                                                    if (l == 0) {
                                                                                        out.println(listError.get(l) + "<br>");
                                                                                    } else {
                                                                                        out.println("&nbsp;&nbsp;&nbsp;" + listError.get(l) + "<br>");
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                } catch (Exception e) {
                                                                    System.out.println(e.getMessage());
                                                                }
                                                            %>
                                                        </td>
                                                    </tr>

                                                    <tr align="left" valign="top">
                                                        <td height="22" valign="middle" colspan="3">
                                                            <%if (privAdd == true) {%>
                                                            <table width="24%" border="0" cellspacing="2" cellpadding="0">
                                                                <tr>
                                                                    <% if (df.getDispatchStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) {%>
                                                                    <td width="90%"><a class="btn btn-primary" href="javascript:addGroupItem()"><%=ctrLine.getCommand(SESS_LANGUAGE, dfCode + " Group Item", ctrLine.CMD_ADD, true)%></a></td>
                                                                    <% } %>
                                                                </tr>
                                                            </table>
                                                            <%}%>
                                                        </td>
                                                    </tr>

                                                    <%}%>
                                                </table>

                                            </td>
                                        </tr>

                                        <tr>
                                            <td colspan="2" valign="top">&nbsp;</td>
                                            <td width="30%">&nbsp;</td>
                                        </tr>

                                        <tr>
                                            <td colspan="2" valign="top">&nbsp;</td>
                                            <td width="30%">&nbsp;</td>
                                        </tr>

                                        <tr>
                                            <td colspan="3">
                                                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                    <tr>
                                                        <td width="77%" rowspan="2">
                                                            <%
                                                                ctrLine.setLocationImg(approot + "/images");
                                                                // set image alternative caption
                                                                ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE, dfTitle, ctrLine.CMD_SAVE, true));
                                                                ctrLine.setBackImageAlt(SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE, dfTitle, ctrLine.CMD_BACK, true) : ctrLine.getCommand(SESS_LANGUAGE, dfTitle, ctrLine.CMD_BACK, true) + " List");
                                                                ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE, dfTitle, ctrLine.CMD_ASK, true));
                                                                ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE, dfTitle, ctrLine.CMD_CANCEL, false));
                                                                ctrLine.initDefault();
                                                                ctrLine.setTableWidth("80%");
                                                                String scomDel = "javascript:cmdAsk('" + oidDispatchMaterial + "')";
                                                                String sconDelCom = "javascript:cmdDelete('" + oidDispatchMaterial + "')";
                                                                String scancel = "javascript:cmdEdit('" + oidDispatchMaterial + "')";
                                                                ctrLine.setCommandStyle("command");
                                                                ctrLine.setColCommStyle("command");
                                                                // set command caption
                                                                ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE, dfTitle, ctrLine.CMD_SAVE, true));
                                                                ctrLine.setBackCaption(SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE, dfTitle, ctrLine.CMD_BACK, true) : ctrLine.getCommand(SESS_LANGUAGE, dfTitle, ctrLine.CMD_BACK, true) + " List");
                                                                ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE, dfTitle, ctrLine.CMD_ASK, true));
                                                                ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE, dfTitle, ctrLine.CMD_DELETE, true));
                                                                ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE, dfTitle, ctrLine.CMD_CANCEL, false));

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

                                                                if (privAdd == false) {
                                                                    ctrLine.setAddCaption("");
                                                                }

                                                                if (df.getDispatchStatus() != I_DocStatus.DOCUMENT_STATUS_DRAFT) {
                                                                    ctrLine.setDeleteCaption("");
                                                                    ctrLine.setAddCaption("");
                                                                }

                                                                if (iCommand == Command.SAVE && frmdf.errorSize() == 0) {

                                                                }

                                                            %>
                                                            <%=ctrLine.drawImage(iCommand, iErrCode, errMsg)%> </td>
                                                    </tr>

                                                    <tr>
                                                        <td width="23%">
                                                            <%if (listMatDispatchReceiveItem != null && listMatDispatchReceiveItem.size() > 0) {%>
                                                            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                                                <tr>
                                                                    <!--td width="5%" valign="top"><a href="javascript:printForm('<%=oidDispatchMaterial%>')"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0" alt="<%=textListGlobal[SESS_LANGUAGE][3]%>"></a></td-->
                                                                    <td width="95%" nowrap>
                                                                        <a href="javascript:printForm('<%=oidDispatchMaterial%>')" class="btn btn-primary" ><i class="fa fa-print"></i> <%=textListGlobal[SESS_LANGUAGE][3]%></a>
                                                                        <input type="checkbox" id="hidePrice" value="1">Without Cost & Sell Price
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                            <%}%>                        
                                                        </td>
                                                    </tr>

                                                    <tr>
                                                        <td>&nbsp;</td>
                                                    </tr>

                                                    <tr>
                                                        <td>&nbsp;</td>
                                                    </tr>

                                                    <tr>
                                                        <td> <a class="btn btn-primary" href="javascript:viewHistoryTable()">VIEW TABEL HISTORY</a></td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                    </table>

                                    <%
                                        } catch (Exception e) {
                                            System.out.println(e);
                                        }
                                    %>

                                </form>
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
                </td>          
            </tr>
            <script language="JavaScript">
                // add by fitra 10-5-2014
                <%if (df.getDispatchStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT && iCommand == Command.SAVE) {%>
                addItem();
                <%}%>
            </script>
        </table>

    </body>
</html>

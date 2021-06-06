<%@page import="com.dimata.common.entity.contact.PstContactList"%>
<%@page import="com.dimata.posbo.entity.search.SrcSaleReport"%>
<%@page import="com.dimata.common.entity.contact.ContactList"%>
<%@page import="com.dimata.posbo.form.search.FrmSrcStockCard"%>
<%@ page import="com.dimata.qdep.form.FRMHandler,
         com.dimata.posbo.entity.masterdata.*,
         com.dimata.gui.jsp.ControlList,
         com.dimata.qdep.form.FRMQueryString,
         com.dimata.gui.jsp.ControlLine,
         com.dimata.posbo.form.warehouse.CtrlMatStockOpname,
         com.dimata.posbo.session.warehouse.SessMatStockOpname,
         com.dimata.posbo.entity.search.SrcMatStockOpname,
         com.dimata.posbo.form.search.FrmSrcMatStockOpname,
         com.dimata.util.Command,
         com.dimata.common.entity.location.Location,
         com.dimata.common.entity.location.PstLocation,
         com.dimata.posbo.entity.warehouse.*"%>
<%@ page import="com.dimata.posbo.session.masterdata.SessMaterial"%>
<%@ page language = "java" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK_REPORT, AppObjInfo.OBJ_STOCK_CORRECTION);%>
<%@ include file = "../../../main/checkuser.jsp" %>
<!-- Jsp Block -->
<%!    public static final String textListGlobal[][] = {
        {"Tidak ada data", "LAPORAN SELISIH KOREKSI STOK", "LOKASI", "Semua", "Laporan Stok", "Cetak Laporan Selisih Koreksi Stok", "Tanggal", "s/d", "Supplier"},
        {"No available data", "LOST CORRECTION STOCK REPORT", "LOCATION", "All", "Stock Report", "Print Lost Correction Stock Report", "Date", "to", "Supplier"}
    };

    /* this constant used to list text of listMaterialItem */
    public static final String textListOrderItem[][]
            = {
                {"NO", "SKU", "NAMA BARANG", "UNIT", "KATEGORI", "SUB KATEGORI", "QTY OPNAME", "QTY TERJUAL", "QTY SYSTEM", "QTY SELISIH", "NILAI SELISIH", "COST", "TGL OPNAME", "( + )", "( - )", "SUPPLIER", "GROUP BY"},
                {"NO", "CODE", "Name", "UNIT", "CATEGORY", "SUB CATEGORY", "OPNAME QTY", "SOLD QTY", "SYSTEM QTY", "LOST QTY", "LOST VALUE", "COST", "OPNAME DATE", "( + )", "( - )", "SUPPLIER", "GROUP BY"}
            };

    public Vector drawListKategoriLokasi(JspWriter outObj, int language, Vector objectClass, int start, SrcMatStockOpname srcMatStockOpname) {
        Vector result = new Vector(1, 1);
        double totPlus = 0.0;
        double totMinus = 0.0;

        if (objectClass != null && objectClass.size() > 0) {
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("100%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("listgentitle");

            ctrlist.addHeader("");
            ctrlist.addHeader("");

            ctrlist.setLinkRow(1);
            ctrlist.setLinkSufix("");
            Vector lstData = ctrlist.getData();

            double totalQtyOpname = 0;
            double totalQtyTerjual = 0;
            double totalQtySystem = 0;
            double totalQtySelisih = 0;
            double NilaiSelisih = 0;
            for (int i = 0; i < objectClass.size(); i++) {
                Vector vt = (Vector) objectClass.get(i);
                MatStockOpname matStockOpname = (MatStockOpname) vt.get(0);
                Location location = (Location) vt.get(1);
                Category category = (Category) vt.get(2);
                Vector rowx = new Vector();
                rowx.add(String.valueOf(start + i + 1) + ".");
                //rowx.add("<div align=\"left\"><b>"+textListGlobal[language][2]+"&nbsp;&nbsp;"+":"+"&nbsp;&nbsp;"+location.getName()+"</b></div>");
                rowx.add("");
                lstData.add(rowx);
                long where = matStockOpname.getLocationId();
                rowx = new Vector();
                Vector list = SessMatStockOpname.getListLostCorrectionStokPercategory(srcMatStockOpname, 0, 0, where);
                rowx.add("<div align=\"left\"></div>");
                Vector vDrawListKategori = drawListKategori(outObj, language, list, start, srcMatStockOpname);
                rowx.add(vDrawListKategori.get(0));
                String totPlusSelisih = String.valueOf(vDrawListKategori.get(1));
                totPlus = Double.parseDouble(totPlusSelisih);
                String totMinusSelisih = String.valueOf(vDrawListKategori.get(2));
                totMinus = Double.parseDouble(totMinusSelisih);
                lstData.add(rowx);
            }
            ctrlist.draw(outObj);
            result.add("");
            result.add(totPlus);
            result.add(totMinus);
        } else {
            result.add("<div class=\"msginfo\">&nbsp;&nbsp;" + textListGlobal[language][0] + "</div>");
            result.add("");
            result.add(0);
            result.add(0);
        }
        return result;
    }

    public Vector drawListKategori(JspWriter outObj, int language, Vector objectClass, int start, SrcMatStockOpname srcMatStockOpname) {
        Vector vResult = new Vector(1, 1);
        String result = "";
        double totPlus = 0;
        double totMinus = 0;
        if (objectClass != null && objectClass.size() > 0) {
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("100%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("listgentitle");

            ctrlist.addHeader("");
            ctrlist.addHeader("");

            ctrlist.setLinkRow(1);
            ctrlist.setLinkSufix("");
            Vector lstData = ctrlist.getData();

            double totalQtyOpname = 0;
            double totalQtyTerjual = 0;
            double totalQtySystem = 0;
            double totalQtySelisih = 0;
            double NilaiSelisih = 0;
            for (int i = 0; i < objectClass.size(); i++) {
                Vector vt = (Vector) objectClass.get(i);
                MatStockOpname matStockOpname = (MatStockOpname) vt.get(0);

                Location location = (Location) vt.get(1);
                Category category = (Category) vt.get(2);
                Material material = (Material) vt.get(3);
                ContactList contact = (ContactList) vt.get(4);

                Vector rowx = new Vector();
                rowx.add(String.valueOf(start + i + 1) + ".");

                if (srcMatStockOpname.getGroupBy() == SrcSaleReport.GROUP_BY_CATEGORY) {
                    rowx.add("<div align=\"left\"><b>" + textListOrderItem[language][4] + "&nbsp;&nbsp;" + ":" + "&nbsp;&nbsp;" + category.getName() + "</b></div>");
                } else {
                    rowx.add("<div align=\"left\"><b>" + textListOrderItem[language][15] + "&nbsp;&nbsp;" + ":" + "&nbsp;&nbsp;" + contact.getCompName() + "</b></div>");
                }

                lstData.add(rowx);

            //rowx = new Vector();
                //rowx.add("");
                //rowx.add("<div align=\"left\"><b>"+textListOrderItem[language][4]+"&nbsp;&nbsp;"+":"+"&nbsp;&nbsp;"+category.getName()+"</b></div>");
            //lstData.add(rowx);
                //long where = location.getOID();
                //long where2 = category.getOID();
                long where = matStockOpname.getLocationId();
                //long where2 = matStockOpname.getCategoryId();
                long where2 = 0;
                if (srcMatStockOpname.getGroupBy() == SrcSaleReport.GROUP_BY_CATEGORY) {
                    where2 = material.getCategoryId();
                } else {
                    where2 = contact.getOID();
                }

                rowx = new Vector();
                Vector list = SessMatStockOpname.getListLostCorrectionStok(srcMatStockOpname, 0, 0, where, where2);
                rowx.add("<div align=\"left\"></div>");

                Vector resultDrawList = drawList(outObj, language, list, start, srcMatStockOpname);

                rowx.add(resultDrawList.get(0));

                String totPlusSelisih = String.valueOf(resultDrawList.get(1));
                totPlus = totPlus + Double.parseDouble(totPlusSelisih);

                String totMinusSelisih = String.valueOf(resultDrawList.get(2));
                totMinus = totMinus + Double.parseDouble(totMinusSelisih);
                lstData.add(rowx);

            }
            //ctrlist.draw();
        }

        vResult.add(result);
        vResult.add(totPlus);
        vResult.add(totMinus);
        return vResult;
    }

    public Vector drawList(JspWriter outObj, int language, Vector objectClass, int start, SrcMatStockOpname srcMatStockOpname) {
        Vector resultVect = new Vector(1, 1);
        String result = "";
        double selisihMinusTot = 0.0;
        double selisihPlusTot = 0.0;
        if (objectClass != null && objectClass.size() > 0) {
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("100%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("listgentitle");

            ctrlist.addHeader(textListOrderItem[language][0], "5%");
            ctrlist.addHeader(textListOrderItem[language][1], "10%");
            ctrlist.addHeader(textListOrderItem[language][2], "25%");
            ctrlist.addHeader(textListOrderItem[language][3], "5%");
            ctrlist.addHeader(textListOrderItem[language][12], "7%");
            ctrlist.addHeader(textListOrderItem[language][6], "7%");
            ctrlist.addHeader(textListOrderItem[language][8], "7%");
            ctrlist.addHeader(textListOrderItem[language][9], "7%");
            //cost
            ctrlist.addHeader(textListOrderItem[language][11], "7%");
            ctrlist.addHeader(textListOrderItem[language][10], "10%");
            ctrlist.addHeader(textListOrderItem[language][13], "10%");
            ctrlist.addHeader(textListOrderItem[language][14], "10%");

            ctrlist.setLinkRow(1);
            ctrlist.setLinkSufix("");
            Vector lstData = ctrlist.getData();

            double totalQtyOpname = 0;
            double totalQtyTerjual = 0;
            double totalQtySystem = 0;
            double totalQtySelisih = 0;
            double NilaiSelisih = 0;
            double totalCost = 0;
            double selisihMinus = 0.0;
            double selisihPlus = 0.0;
            int count = 0;
            String space = "";
            String style = "";
            for (int i = 0; i < objectClass.size(); i++) {
                Vector vt = (Vector) objectClass.get(i);
                MatStockOpname matStockOpname = (MatStockOpname) vt.get(0);
                MatStockOpnameItem matStockOpnameItem = (MatStockOpnameItem) vt.get(1);
                Material material = (Material) vt.get(2);
                Unit unit = (Unit) vt.get(3);
                Category category = (Category) vt.get(4);
                ContactList contactList = (ContactList) vt.get(5);

                count = count + 1;
                Vector rowx = new Vector();
                space = "";
                if (count == 1) {
                    space = "</b><br><br>";
                } else {
                    space = "";
                }
                rowx = new Vector();
                double qtyLost = (matStockOpnameItem.getQtyOpname() + matStockOpnameItem.getQtySold()) - matStockOpnameItem.getQtySystem();

                style = "";
                if (qtyLost < 0) {
                    style = "style=\"background-color:#f2a898;\"";
                }

                if (qtyLost > 0) {
                    style = "style=\"background-color:#e2f2c6;\"";
                }

                rowx.add(String.valueOf(start + i + 1) + ".");
                rowx.add("" + space + "<div align=\"left\" " + style + "><a href=\"javascript:cmdViewKartuStock('" + material.getOID() + "','" + material.getSku() + "','" + material.getName() + "')\">" + material.getSku() + "</a></div>");
                if (count == 1) {
                    if (srcMatStockOpname.getGroupBy() == SrcSaleReport.GROUP_BY_CATEGORY) {
                        rowx.add("CATEGORY : <b>" + category.getName() + "</b><br><br><div align=\"left\" " + style + " >" + material.getName() + "</div>");
                    } else {
                        rowx.add("SUPPLIER : <b>" + contactList.getCompName() + "</b><br><br><div align=\"left\" " + style + " >" + material.getName() + "</div>");
                    }
                } else {
                    rowx.add("<div align=\"left\" " + style + " >" + material.getName() + "</div>");
                }

                rowx.add("" + "" + space + "<div align=\"right\" " + style + " >" + unit.getCode() + "</div>");
                rowx.add("" + "" + space + "<div align=\"right\" " + style + " >" + Formater.formatDate(matStockOpname.getStockOpnameDate(), "dd/MM/yyyy HH:mm:ss") + "</div>");
                rowx.add("" + space + "<div align=\"right\" " + style + " >" + FRMHandler.userFormatStringDecimal(matStockOpnameItem.getQtyOpname()) + "</div>");
                rowx.add("" + space + "<div align=\"right\" " + style + " >" + FRMHandler.userFormatStringDecimal(matStockOpnameItem.getQtySystem()) + "</div>");
                rowx.add("" + space + "<div align=\"right\" " + style + " >" + FRMHandler.userFormatStringDecimal(qtyLost) + "</div>");
                rowx.add("" + space + "<div align=\"right\" " + style + " >" + FRMHandler.userFormatStringDecimal(matStockOpnameItem.getCost()) + "</div>");
                rowx.add("" + space + "<div align=\"right\" " + style + " >" + FRMHandler.userFormatStringDecimal(qtyLost * matStockOpnameItem.getCost()) + "</div>");

                totalQtyOpname = totalQtyOpname + matStockOpnameItem.getQtyOpname();
                totalQtyTerjual = totalQtyTerjual + matStockOpnameItem.getQtySold();
                totalQtySystem = totalQtySystem + matStockOpnameItem.getQtySystem();
                totalQtySelisih = totalQtySelisih + qtyLost;
                NilaiSelisih = NilaiSelisih + (qtyLost * matStockOpnameItem.getCost());
                totalCost = totalCost + matStockOpnameItem.getCost();

                double resultNilaiSelisih = qtyLost * matStockOpnameItem.getCost();
                if (resultNilaiSelisih > 0) {
                    selisihPlus = selisihPlus + resultNilaiSelisih;
                    selisihPlusTot = selisihPlusTot + resultNilaiSelisih;
                    rowx.add("" + space + "<div align=\"right\" " + style + " >" + FRMHandler.userFormatStringDecimal(resultNilaiSelisih) + "</div>");
                    rowx.add("" + space + "<div align=\"right\" " + style + " >" + 0 + "</div>");
                } else {
                    selisihMinus = selisihMinus + resultNilaiSelisih;
                    selisihMinusTot = selisihMinusTot + resultNilaiSelisih;
                    rowx.add("" + space + "<div align=\"right\" " + style + " >" + 0 + "</div>");
                    rowx.add("" + space + "<div align=\"right\" " + style + " >" + FRMHandler.userFormatStringDecimal(resultNilaiSelisih) + "</div>");
                }

                lstData.add(rowx);

            }

            Vector rowx = new Vector();
            rowx.add("");
            rowx.add("");
            rowx.add("");
            rowx.add("");

            rowx.add("<div align=\"right\"><b>TOTAL</b></div>");
            rowx.add("<div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(totalQtyOpname) + "</b></div>");
            rowx.add("<div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(totalQtySystem) + "</b></div>");
            rowx.add("<div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(totalQtySelisih) + "</b></div>");
            rowx.add("<div align=\"right\"><b>&nbsp;</b></div>");
            rowx.add("<div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(NilaiSelisih) + "</b></div>");
            rowx.add("<div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(selisihPlus) + "</b></div>");
            rowx.add("<div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(selisihMinus) + "</b></div>");

            lstData.add(rowx);
            ctrlist.draw(outObj);
        }
        resultVect.add(result);
        resultVect.add(selisihPlusTot);
        resultVect.add(selisihMinusTot);

        return resultVect;
    }

//Grand Total Nilai Stok
    public Vector drawListGrandTotal(int language, Vector objectClass, SrcMatStockOpname srcMatStockOpname, String strDrawPlusSelisih, String strDrawMinusSelisih) {
        Vector result = new Vector(1, 1);
        //String result = "";
        if (objectClass != null && objectClass.size() > 0) {
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("100%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("listgentitle");
            ctrlist.addHeader("", "56%");
            ctrlist.addHeader(textListOrderItem[language][6], "11%");
            ctrlist.addHeader(textListOrderItem[language][8], "11%");
            ctrlist.addHeader(textListOrderItem[language][9], "11%");
            ctrlist.addHeader(textListOrderItem[language][10], "11%");
            ctrlist.addHeader(textListOrderItem[language][13], "11%");
            ctrlist.addHeader(textListOrderItem[language][14], "11%");

            ctrlist.setLinkRow(1);
            ctrlist.setLinkSufix("");
            Vector lstData = ctrlist.getData();

            double totalQtyOpname = 0;
            double totalQtyTerjual = 0;
            double totalQtySystem = 0;
            double totalQtySelisih = 0;
            double NilaiSelisih = 0;
            double totalCost = 0;
            double totPlus = 0;
            double totMinus = 0;
            for (int i = 0; i < objectClass.size(); i++) {
                Vector temp = (Vector) objectClass.get(i);
                double sumQtyOpname = (Double) temp.get(0);
                double sumQtySold = (Double) temp.get(1);
                double sumQtySystem = (Double) temp.get(2);
                double sumCost = (Double) temp.get(3);
                double lostValue = (Double) temp.get(5);

                double sumQtyLost = (sumQtyOpname + sumQtySold) - sumQtySystem;
                Vector rowx = new Vector();
                if (strDrawPlusSelisih.equals("")) {
                    totPlus = 0;
                } else {
                    totPlus = Double.parseDouble(strDrawPlusSelisih);
                }
                if (strDrawPlusSelisih.equals("")) {
                    totMinus = 0;
                } else {
                    totMinus = Double.parseDouble(strDrawMinusSelisih);
                }
                rowx.add("<div align=\"center\"><b>GRAND TOTAL</b></div>");
                rowx.add("<div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(sumQtyOpname) + "</b></div>");
                rowx.add("<div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(sumQtySystem) + "</b></div>");
                rowx.add("<div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(sumQtyLost) + "</b></div>");
                rowx.add("<div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(lostValue) + "</b></div>");
                rowx.add("<div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(totPlus) + "</b></div>");
                rowx.add("<div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(totMinus) + "</b></div>");

                lstData.add(rowx);
            }

            result.add(ctrlist.draw());

        }
        return result;
    //return "";
    }

//end of Nilai Stock

%>


<%    int iCommand = FRMQueryString.requestCommand(request);
    int start = FRMQueryString.requestInt(request, "start");
    int daily = FRMQueryString.requestInt(request, "daily");
    CtrlMatStockOpname ctrlMatStockOpname = new CtrlMatStockOpname(request);
    ControlLine ctrLine = new ControlLine();
    SrcMatStockOpname srcMatStockOpname = new SrcMatStockOpname();
    FrmSrcMatStockOpname frmSrcMatStockOpname = new FrmSrcMatStockOpname(request, srcMatStockOpname);
    frmSrcMatStockOpname.requestEntityObject(srcMatStockOpname);
    SessMatStockOpname sessMatStockOpname = new SessMatStockOpname();

    if (iCommand == Command.BACK || iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST) {
        try {
            srcMatStockOpname = (SrcMatStockOpname) session.getValue(SessMatStockOpname.SESS_SRC_MATSTOCKCORRECTION);
            if (srcMatStockOpname == null) {
                srcMatStockOpname = new SrcMatStockOpname();
            }
        } catch (Exception e) {
            srcMatStockOpname = new SrcMatStockOpname();
        }
    } else {
        frmSrcMatStockOpname.requestEntityObject(srcMatStockOpname);

        //vector status
        Vector vectSt = new Vector(1, 1);
        String[] strStatus = request.getParameterValues(FrmSrcMatStockOpname.fieldNames[FrmSrcMatStockOpname.FRM_FIELD_STATUS]);
        if (strStatus != null && strStatus.length > 0) {
            for (int i = 0; i < strStatus.length; i++) {
                try {
                    vectSt.add(strStatus[i]);
                } catch (Exception exc) {
                    System.out.println("err");
                }
            }
        }
        srcMatStockOpname.setDocStatus(vectSt);
        //end of vector status
        session.putValue(SessMatStockOpname.SESS_SRC_MATSTOCKCORRECTION, srcMatStockOpname);
    }

    Location location = new Location();
    if (srcMatStockOpname.getLocationId() != 0) {
        try {
            location = PstLocation.fetchExc(srcMatStockOpname.getLocationId());
        } catch (Exception e) {
        }
    } else {
        location.setName(textListGlobal[SESS_LANGUAGE][3] + " " + textListGlobal[SESS_LANGUAGE][2]);
    }

    Category category = new Category();
    if (srcMatStockOpname.getCategoryId() != 0) {
        try {
            category = PstCategory.fetchExc(srcMatStockOpname.getCategoryId());
        } catch (Exception e) {
        }
    } else {
        category.setName(textListGlobal[SESS_LANGUAGE][3] + " " + textListOrderItem[SESS_LANGUAGE][4]);
    }

    ContactList contact = new ContactList();
    if (srcMatStockOpname.getSupplierId() != 0) {
        try {
            contact = PstContactList.fetchExc(srcMatStockOpname.getSupplierId());
            contact.setCompName(contact.getCompName());
        } catch (Exception e) {
        }
    } else {
        contact.setCompName(textListGlobal[SESS_LANGUAGE][3] + " " + textListOrderItem[SESS_LANGUAGE][8]);
    }

    String groupBy = "";
    if (srcMatStockOpname.getGroupBy() == SrcSaleReport.GROUP_BY_CATEGORY) {
        groupBy = "" + SrcSaleReport.groupMethod[SESS_LANGUAGE][srcMatStockOpname.getGroupBy()];
    } else {
        groupBy = "" + SrcSaleReport.groupMethod[SESS_LANGUAGE][srcMatStockOpname.getGroupBy()];
    }
//Periode periode = new Periode();
//try {
    //periode = PstPeriode.fetchExc(srcMatStockOpname..getPeriodeId());
//} catch(Exception e){
//}

    /**
     * use tis variable for display stock with zero qty
     */
    boolean calculateQtyNull = false;

    /**
     * get size stock list and stock value
     */
    int recordToGet = 500;
//Vector vctStockValue = sessMatStockOpname.getListLostCorrectionStok(srcMatStockOpname, start, recordToGet);
//untuk daftar barang
//Vector vctStockValue = sessMatStockOpname.searchMatStockOpnameLost(srcMatStockOpname, start, recordToGet);
//untuk list perkategori dan perLokasi
//Vector vctStockValue = sessMatStockOpname.searchMatStockOpnameLostList(srcMatStockOpname, start, recordToGet);
//int vectSize = Integer.parseInt((String)vctStockValue.get(0));
//double stockValue = Double.parseDouble((String)vctStockValue.get(1));
//int recordToGet = 500;

//if(iCommand==Command.FIRST || iCommand==Command.NEXT || iCommand==Command.PREV || iCommand==Command.LAST || iCommand==Command.LIST) {
    //start = ctrlMatStockOpname.actionList(iCommand,start,vectSize,recordToGet);
//}
//untuk list perkategori dan perlokasi
    Vector listStockAll = sessMatStockOpname.getListLostCorrectionStokPerlocationPercategory(srcMatStockOpname, start, recordToGet);
    Vector grandTotalCorrectionStok = sessMatStockOpname.grandTotalLostCorrectionStok(srcMatStockOpname, start, recordToGet, srcMatStockOpname.getLocationId(), srcMatStockOpname.getCategoryId());

//untuk daftar barang
//Vector listStockAll = sessMatStockOpname.getListLostCorrectionStok(srcMatStockOpname, start, recordToGet);
//Vector vctDrawList = drawList(SESS_LANGUAGE, listStockAll, start,srcMatStockOpname);
/*Vector vctDrawList = drawListKategoriLokasi(out,SESS_LANGUAGE, listStockAll, start,srcMatStockOpname);
     String strDrawList = (String)vctDrawList.get(0);
     String strDrawPlusSelisih = String.valueOf(vctDrawList.get(1));
     String strDrawMinusSelisih = String.valueOf(vctDrawList.get(2));
     Vector vctDrawGrandTotal = drawListGrandTotal(SESS_LANGUAGE, grandTotalCorrectionStok, srcMatStockOpname,strDrawPlusSelisih,strDrawMinusSelisih);
     String strDrawGrandTotal = (String)vctDrawGrandTotal.get(0);*/
//String strNilaiStock = (String)vctDrawList.get(1);

%>
<!-- End of Jsp Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
    <head>
        <!-- #BeginEditable "doctitle" --> 
        <title>Dimata - ProChain POS</title>
        <script language="JavaScript">
            <!--
        function cmdEdit(oid) {
            }

            function cmdListFirst() {
                document.frm_reportstock.command.value = "<%=Command.FIRST%>";
                document.frm_reportstock.action = "report_list_lost_correction_stock.jsp";
                document.frm_reportstock.submit();
            }

            function cmdListPrev() {
                document.frm_reportstock.command.value = "<%=Command.PREV%>";
                document.frm_reportstock.action = "report_list_lost_correction_stock.jsp";
                document.frm_reportstock.submit();
            }

            function cmdListNext() {
                document.frm_reportstock.command.value = "<%=Command.NEXT%>";
                document.frm_reportstock.action = "report_list_lost_correction_stock.jsp";
                document.frm_reportstock.submit();
            }

            function cmdListLast() {
                document.frm_reportstock.command.value = "<%=Command.LAST%>";
                document.frm_reportstock.action = "report_list_lost_correction_stock.jsp";
                document.frm_reportstock.submit();
            }

            function cmdBack() {
                document.frm_reportstock.command.value = "<%=Command.BACK%>";
            <%if (daily == 1) {%>
                document.frm_reportstock.action = "src_reportselisihkoreksistok_daily.jsp";
            <%} else {%>
                document.frm_reportstock.action = "src_reportselisihkoreksistok.jsp";
            <%}%>
                document.frm_reportstock.submit();
            }

            function printForm() {
                window.open("report_list_lost_correction_stock_html.jsp", "form_stock_report");
            }

            function printFormExcel() {
                window.open("report_list_lost_correction_stock_excel.jsp", "form_stock_report");
            }

            function cmdViewKartuStock(oidMat, txtkode, txtnama) {
                var strvalue = "src_stockcard.jsp?type=1&<%=FrmSrcStockCard.fieldNames[FrmSrcStockCard.FRM_FIELD_MATERIAL_ID]%>=" + oidMat + "&txtkode=" + txtkode + "&txtnama=" + txtnama + "&typeCheckKartu=1";
                winSrcMaterial = window.open(strvalue, "material", "height=600,width=800,status=yes,toolbar=no,menubar=yes,location=no,scrollbars=yes");
                if (window.focus) {
                    winSrcMaterial.focus();
                }
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
                                <form name="frm_reportstock" method="post" action="">
                                    <input type="hidden" name="command" value="">
                                    <input type="hidden" name="start" value="<%=start%>">
                                    <input type="hidden" name="daily" value="<%=daily%>">
                                    <table width="100%" cellspacing="0" cellpadding="3">
                                        <tr align="left" valign="top"> 
                                            <td height="14" colspan="3" align="center" valign="middle">
                                                <h4><strong><%=textListGlobal[SESS_LANGUAGE][1]%></strong></h4>
                                            </td>
                                        </tr>
                                        <tr align="left" valign="top">
                                            <td width="10%" height="14" valign="middle" class="command"><b><%=(textListGlobal[SESS_LANGUAGE][2]).toUpperCase()%></b></td>
                                            <td width="1%" height="14" valign="middle" class="command">:</td>
                                            <td width="89%" height="14" valign="middle" class="command"><b><%=location.getName().toUpperCase()%></b></td>

                                        </tr>
                                        <tr align="left" valign="top">
                                            <td width="10%" height="14" valign="middle" class="command"><b><%=(textListGlobal[SESS_LANGUAGE][6]).toUpperCase()%>
                                                    <td width="1%" height="14" valign="middle" class="command">:</td>
                                                    <td width="89%" height="14" valign="middle" class="command">
                                                    <%=Formater.formatDate(srcMatStockOpname.getFromDate(), "dd-MM-yyyy")%> <%=textListGlobal[SESS_LANGUAGE][7]%> <%=Formater.formatDate(srcMatStockOpname.getToDate(), "dd-MM-yyyy")%></b> </td>
                                        </tr>
                                        <tr align="left" valign="top">
                                            <td width="10%" height="14" valign="middle" class="command"><b><%=(textListOrderItem[SESS_LANGUAGE][4]).toUpperCase()%></b></td>
                                            <td width="1%" height="14" valign="middle" class="command">:</td>
                                            <td width="89%" height="14" valign="middle" class="command"><%=category.getName().toUpperCase()%></td>
                                        </tr>
                                        <tr align="left" valign="top">
                                            <td width="10%" height="14" valign="middle" class="command"><b><%=(textListOrderItem[SESS_LANGUAGE][15]).toUpperCase()%></b></td>
                                            <td width="1%" height="14" valign="middle" class="command">:</td>
                                            <td width="89%" height="14" valign="middle" class="command"><%=contact.getCompName().toUpperCase()%></td>
                                        </tr>
                                        <tr align="left" valign="top">
                                            <td width="10%" height="14" valign="middle" class="command"><b><%=(textListOrderItem[SESS_LANGUAGE][16]).toUpperCase()%></b></td>
                                            <td width="1%" height="14" valign="middle" class="command">:</td>
                                            <td width="89%" height="14" valign="middle" class="command"><%=groupBy.toUpperCase()%></td>
                                        </tr>

                                        <tr align="left" valign="top"> 
                                            <td height="22" align="center" valign="middle" colspan="3"><%//=strDrawList%>
                                                <%
                                                    Vector vctDrawList = drawListKategoriLokasi(out, SESS_LANGUAGE, listStockAll, start, srcMatStockOpname);
                                                    String strDrawList = (String) vctDrawList.get(0);
                                                    String strDrawPlusSelisih = String.valueOf(vctDrawList.get(1));
                                                    String strDrawMinusSelisih = String.valueOf(vctDrawList.get(2));
                                                    Vector vctDrawGrandTotal = drawListGrandTotal(SESS_LANGUAGE, grandTotalCorrectionStok, srcMatStockOpname, strDrawPlusSelisih, strDrawMinusSelisih);
                                                    String strDrawGrandTotal = (String) vctDrawGrandTotal.get(0);
                                                %>

                                            </td>

                                        </tr>
                                        <!-- <tr align="left" valign="top">
                                           <td height="8" align="left" colspan="3" class="command"> 
                                             <span class="command"> 
                                        <%
                                              //ctrLine.setLocationImg(approot+"/images");
                                            //ctrLine.initDefault();
                                            //out.println(ctrLine.drawImageListLimit(iCommand,vectSize,start,recordToGet));
                                        %>
                                      </span>
                                    </td>
                                  </tr>-->
                                        <% if (listStockAll.size() != 0) {%>
                                        <tr align="left" valign="top"> 
                                            <td height="22" align="center" valign="middle" colspan="3">
                                                <%=strDrawGrandTotal%>

                                            </td>
                                        <br>

                                        </tr>
                                        <% }%>
                                        <tr align="left" valign="top"> 
                                            <td height="18" valign="top" colspan="3"> <table width="100%" border="0">
                                                    <tr> 
                                                        <td width="100%"> 
                                                            <table width="50%" border="0" cellspacing="0" cellpadding="0">
                                                                <tr> 
                                                                  
                                                                </tr>
                                                            </table>
                                                            <table width="50%" border="0" cellspacing="0" cellpadding="0">
                                                                <tr> 
                                                                    <!--td nowrap width="6%"><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnBackOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE, textListGlobal[SESS_LANGUAGE][5], ctrLine.CMD_BACK_SEARCH, true)%>"></a></td-->
                                                                    <td nowrap width="2%">&nbsp;</td>
                                                                    <td class="command" nowrap><a class="btn btn-primary" href="javascript:cmdBack()"><i class="fa fa-arrow-left"></i> <%=ctrLine.getCommand(SESS_LANGUAGE, textListGlobal[SESS_LANGUAGE][5], ctrLine.CMD_BACK_SEARCH, true)%></a></td>
                                                                    <!--td width="5%" valign="top"><a href="javascript:printForm()"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0"></a></td-->
                                                                    <td width="20%" nowrap>&nbsp; <a class="btn btn-primary" href="javascript:printForm()" class="command" ><i class="fa fa-print"></i> Export Html</a>
                                                                    </td>
                                                                    <!--td width="5%" valign="top"><a href="javascript:printFormExcel()"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0"></a></td-->
                                                                    <td width="20%" nowrap>&nbsp; <a class="btn btn-primary" href="javascript:printFormExcel()" class="command" ><i class="fa fa-print"></i> Export Excel</a>
                                                                    </td>
                                                                </tr>
                                                            </table>  
                                                        </td>
                                                    </tr>
                                                </table></td>
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
                    <%@include file="../../../styletemplate/footer.jsp" %>
                    <%} else {%>
                    <%@ include file = "../../../main/footer.jsp" %>
                    <%}%>
                    <!-- #EndEditable --> </td>
            </tr>
        </table>
    </body>
    <!-- #EndTemplate --></html>

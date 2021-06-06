<%-- 
    Document   : reportsalerekaptanggal_list_excel
    Created on : May 3, 2016, 4:25:10 PM
    Author     : dimata005
--%>
<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.entity.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package common -->
<%@ page import = "com.dimata.common.entity.location.*" %>
<%@ page import = "com.dimata.common.entity.contact.*" %>
<!--package material -->
<%@ page import = "com.dimata.harisma.entity.masterdata.*" %>
<!--package material -->
<!--pos versi 1-->
<!--%@ page import = "com.dimata.cashier.entity.billing.*" %-->
<!--%@ page import = "com.dimata.cashier.entity.payment.*" %-->

<!--pos versi 2-->
<%@ page import = "com.dimata.pos.entity.billing.*" %>
<%@ page import = "com.dimata.pos.entity.payment.*" %>

<%@ page import = "com.dimata.posbo.entity.warehouse.*" %>
<%@ page import = "com.dimata.posbo.entity.masterdata.*" %>
<%@ page import = "com.dimata.posbo.entity.admin.*" %>
<%@ page import = "com.dimata.posbo.entity.search.*" %>
<%@ page import = "com.dimata.posbo.form.warehouse.*" %>
<%@ page import = "com.dimata.posbo.form.search.*" %>
<%@ page import = "com.dimata.posbo.session.warehouse.*" %>

<%@ include file = "../../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_REPORT, AppObjInfo.OBJ_DAILY_SUMMARY);%>
<%@ include file = "../../../main/checkuser.jsp" %>

<!-- Jsp Block -->
<%!    public static final int ADD_TYPE_SEARCH = 0;
    public static final int ADD_TYPE_LIST = 1;

    /* this constant used to list text of listHeader */
    public static final String textListMaterialHeader[][]
            = {
                {"No", "Tanggal", "Total HPP", "Total Disc", "Total Jual", "Total Profit", "Total Tax", "Total Service"},
                {"No", "Date", "Total Cost", "Total Disc", "Total Sale", "Total Profit", "Total Tax", "Total Service"}
            };

    public static final String textListHeader[][]
            = {
                {"LAPORAN REKAP HARIAN", "Laporan Rekap Harian"},
                {"SUMMARY DAILY REPORT", "Summary Daily Report"}
            };

    public static final String textButton[][]
            = {
                {"Rekap Harian"},
                {"Daily Report"}
            };

    public Vector drawLineHorizontal() {
        Vector rowx = new Vector();
        //Add Under line
        rowx.add("");
        rowx.add("");
        rowx.add("<div align=\"center\">"  + "</div>");
        rowx.add("");
        rowx.add("<div align=\"center\">"  + "</div>");
        rowx.add("");
        rowx.add("<div align=\"center\">"  + "</div>");
        rowx.add("");
        rowx.add("<div align=\"center\">"  + "</div>");
        rowx.add("");
        rowx.add("<div align=\"center\">"  + "</div>");

        rowx.add("");
        rowx.add("<div align=\"right\">"  + "</div>");
        rowx.add("");
        rowx.add("<div align=\"right\">"  + "</div>");
        return rowx;
    }

    public Vector drawHeader(int language) {
        Vector rowx = new Vector();
        //Add Header
        rowx.add("|");
        rowx.add(textListMaterialHeader[language][0]);
        rowx.add("<div align=\"center\">"  + "</div>");
        rowx.add("<div align=\"center\">" + textListMaterialHeader[language][1] + "</div>");
        rowx.add("<div align=\"center\">"  + "</div>");
        rowx.add("<div align=\"right\">" + textListMaterialHeader[language][4] + "</div>");
        rowx.add("<div align=\"center\">"  + "</div>");
        rowx.add("<div align=\"right\">" + textListMaterialHeader[language][2] + "</div>");
        rowx.add("<div align=\"center\">"  + "</div>");
        rowx.add("<div align=\"right\">" + textListMaterialHeader[language][5] + "</div>");
        rowx.add("<div align=\"center\">"  + "</div>");
        rowx.add("<div align=\"right\">" + textListMaterialHeader[language][6] + "</div>");
        rowx.add("<div align=\"right\">"  + "</div>");
        rowx.add("<div align=\"right\">" + textListMaterialHeader[language][7] + "</div>");
        rowx.add("<div align=\"right\">"  + "</div>");
        return rowx;
    }

    public Vector drawLineTotal() {
        Vector rowx = new Vector();
        rowx.add("");
        rowx.add("");
        rowx.add("");
        rowx.add("");
        rowx.add("<div align=\"center\">"  + "</div>");
        rowx.add("-");
        rowx.add("<div align=\"center\">"  + "</div>");
        rowx.add("-");
        rowx.add("<div align=\"center\">"  + "</div>");
        rowx.add("-");
        rowx.add("<div align=\"center\">"  + "</div>");

        // tax service
        rowx.add("-");
        rowx.add("<div align=\"center\">"  + "</div>");
        rowx.add("-");
        rowx.add("<div align=\"center\">"  + "</div>");

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
        rowx.add("");
        rowx.add("");
        return rowx;
    }

    public Vector drawLineTotalSide() {
        Vector rowx = new Vector();
        rowx.add("|");
        rowx.add("");
        rowx.add("");
        rowx.add("");
        rowx.add("<div align=\"center\">"  + "</div>");
        rowx.add("");
        rowx.add("<div align=\"center\">"  + "</div>");
        rowx.add("");
        rowx.add("<div align=\"center\">"  + "</div>");
        rowx.add("");
        rowx.add("<div align=\"center\">"  + "</div>");

        // tax service
        rowx.add("");
        rowx.add("<div align=\"right\">"  + "</div>");
        rowx.add("");
        rowx.add("<div align=\"right\">"  + "</div>");

        return rowx;
    }

    public Vector drawList(int language, Vector objectClass) {
        Vector result = new Vector();
        if (objectClass != null && objectClass.size() > 0) {
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("80%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("listgentitle");
            ctrlist.setCellSpacing("0");
            ctrlist.dataFormat("", "1%", "0", "0", "center", "middle");
            ctrlist.dataFormat("", "3%", "0", "0", "center", "middle");
            ctrlist.dataFormat("", "1%", "0", "0", "center", "middle");
            ctrlist.dataFormat("", "10%", "0", "0", "center", "middle");
            ctrlist.dataFormat("", "1%", "0", "0", "center", "middle");
            ctrlist.dataFormat("", "15%", "0", "0", "right", "bottom");
            ctrlist.dataFormat("", "1%", "0", "0", "center", "middle");
            ctrlist.dataFormat("", "15%", "0", "0", "right", "bottom");
            ctrlist.dataFormat("", "1%", "0", "0", "center", "middle");
            ctrlist.dataFormat("", "15%", "0", "0", "right", "bottom");
            ctrlist.dataFormat("", "1%", "0", "0", "center", "middle");

            // tax service
            ctrlist.dataFormat("", "15%", "0", "0", "right", "bottom");
            ctrlist.dataFormat("", "1%", "0", "0", "center", "middle");
            ctrlist.dataFormat("", "15%", "0", "0", "right", "bottom");
            ctrlist.dataFormat("", "1%", "0", "0", "center", "middle");
            
            ctrlist.setBorder(1);
            
            ctrlist.setLinkRow(-1);
            ctrlist.setLinkSufix("");
            Vector lstData = ctrlist.getData();
            Vector lstLinkData = ctrlist.getLinkData();
            ctrlist.setLinkPrefix("javascript:cmdEdit('");
            ctrlist.setLinkSufix("')");
            ctrlist.reset();
            double totalJual = 0.00;
            double totalCost = 0.00;
            double totalPotongan = 0.00;
            double totalProfit = 0.00;

            double totalTaxVal = 0.00;
            double totalServiceVal = 0.00;

            boolean firstRow = true;
            int baris = 2;
            for (int i = 0; i < objectClass.size(); i++) {
                Vector rowx = new Vector();
                Vector vt = (Vector) objectClass.get(i);
                String tanggal = (String) vt.get(0);
                Double totalRekap = (Double) vt.get(1);
                Double totalHPP = (Double) vt.get(2);
                Double totalDisc = (Double) vt.get(3);

                Double totalTax = (Double) vt.get(4);
                Double totalService = (Double) vt.get(5);

                if (firstRow == true) {
                    firstRow = false;
                    //Draw Header
                    //lstData.add(drawLineHorizontal());
                    //baris += 1;
                    lstData.add(drawHeader(language));
                    baris += 1;
                    //lstData.add(drawLineHorizontal());
                    //baris += 1;
                }
                rowx.add("|");
                rowx.add("" + (i + 1));
                rowx.add("<div align=\"center\">"  + "</div>");
                rowx.add("<div align=\"center\">" + tanggal + "</div>");
                rowx.add("<div align=\"center\">"  + "</div>");
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(totalRekap.doubleValue()) + "</div>"); // 
                rowx.add("<div align=\"center\">"  + "</div>");
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(totalHPP.doubleValue()) + "</div>");
                rowx.add("<div align=\"center\">"  + "</div>");
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(totalRekap.doubleValue() - totalHPP.doubleValue()) + "</div>");
                rowx.add("<div align=\"center\">"  + "</div>");

                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(totalTax.doubleValue()) + "</div>");
                rowx.add("<div align=\"right\">"  + "</div>");
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(totalService.doubleValue()) + "</div>");
                rowx.add("<div align=\"right\">"  + "</div>");

                lstData.add(rowx);

                totalJual += totalRekap.doubleValue();
                totalCost += totalHPP.doubleValue();
                totalPotongan += totalDisc.doubleValue();
                totalProfit += (totalRekap.doubleValue() - totalHPP.doubleValue());
                totalTaxVal += totalTax.doubleValue();
                totalServiceVal += totalService.doubleValue();

                baris += 1;

                if (baris == 78) {
                    //Add line
                    lstData.add(drawLineHorizontal());
                    //Add header for next page and restart counting baris
                    /*lstData.add(drawLineHorizontal());
                    baris = 1;
                    lstData.add(drawHeader(language));
                    baris += 1;
                    lstData.add(drawLineHorizontal());
                    baris += 1;*/
                }

                lstLinkData.add("");
            }
            //lstData.add(drawLineHorizontal());
            //Add TOTAL
            Vector rowx = new Vector();
            rowx.add("");
            rowx.add("");
            rowx.add("");
            rowx.add("<div align=\"right\">" + "TOTAL" + "</div>");
            rowx.add("<div align=\"center\">"  + "</div>");
            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(totalJual) + "</div>");
            rowx.add("<div align=\"center\">"  + "</div>");
            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(totalCost) + "</div>");
            rowx.add("<div align=\"center\">"  + "</div>");
            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(totalProfit) + "</div>");
            rowx.add("<div align=\"center\">"  + "</div>");

            // total tax service
            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(totalTaxVal) + "</div>");// 
            rowx.add("<div align=\"right\">"  + "</div>");
            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(totalServiceVal) + "</div>");// 
            rowx.add("<div align=\"right\">"  + "</div>");

            lstData.add(rowx);
            lstData.add(drawLineTotal());
            result = ctrlist.drawMePartVector(0, lstData.size(), rowx.size());
        } else {
            result.add("<div class=\"msginfo\">&nbsp;&nbsp;No data available ...</div>");
        }
        return result;
    }
%>

<%    int iErrCode = FRMMessage.ERR_NONE;
    String msgStr = "";
    int iCommand = FRMQueryString.requestCommand(request);
    int start = FRMQueryString.requestInt(request, "start");
    int recordToGet = 20;
    int vectSize = 0;
    String whereClause = "";

    /**
     * instantiate some object used in this page
     */
    ControlLine ctrLine = new ControlLine();
    SrcReportSale srcReportSale = new SrcReportSale();
    SessReportSale sessReportSale = new SessReportSale();
    FrmSrcReportSale frmSrcReportSale = new FrmSrcReportSale(request, srcReportSale);

    /**
     * handle current search data session
     */
    try {
        srcReportSale = (SrcReportSale) session.getValue(SessReportSale.SESS_SRC_REPORT_SALE_REKAP);
        if (srcReportSale == null) {
            srcReportSale = new SrcReportSale();
        }
    } catch (Exception e) {
        srcReportSale = new SrcReportSale();
    }
    Vector reports = new Vector(1,1);
    try {
        reports = (Vector) session.getValue("SESS_MAT_SALE_REPORT_REKAP");
        srcReportSale = (SrcReportSale) reports.get(0);
            
    }catch (Exception e) {
        srcReportSale = new SrcReportSale();
    }

    /**
     * get vectSize, start and data to be display in this page
     */
    Vector records = sessReportSale.getReportSaleRekapTanggal(srcReportSale);
    vectSize = records.size();
    if (iCommand == Command.FIRST || iCommand == Command.NEXT || iCommand == Command.PREV || iCommand == Command.LAST || iCommand == Command.LIST) {
        //start = ctrlReportSale.actionList(iCommand,start,vectSize,recordToGet);
    }
%>
<!-- End of Jsp Block -->
<%@ page contentType="application/x-msexcel" %>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
    <head>
        <!-- #BeginEditable "doctitle" --> 
        <title>Dimata - ProChain POS</title>
        <!-- #EndEditable -->
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
    </head> 

    <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
            <tr> 
                <td valign="top" align="left"> 
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">  
                        <tr> 
                            <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" --><%=textListHeader[SESS_LANGUAGE][1]%><!-- #EndEditable --></td>
                        </tr>
                        <tr> 
                            <td><!-- #BeginEditable "content" --> 
                                <form name="frm_reportsale" method="post" action="">
                                    <input type="hidden" name="command" value="">
                                    <input type="hidden" name="add_type" value="">
                                    <input type="hidden" name="start" value="<%=start%>">
                                    <input type="hidden" name="approval_command">
                                    <table width="100%" cellspacing="0" cellpadding="3">
                                        <tr align="left" valign="top"> 
                                            <td valign="middle" colspan="3"> <hr size="1"> </td>
                                        </tr>
                                        <tr align="left" valign="top"> 
                                            <td height="14" valign="middle" colspan="3" align="center"><b><%=textListHeader[SESS_LANGUAGE][0]%></b></td>
                                        </tr>
                                        <tr align="left" valign="top"> 
                                            <td height="14" valign="middle" colspan="3" align="center" ><b> 
                                                    <%
                                                        if (!Validator.isEqualsDate(srcReportSale.getDateFrom(), srcReportSale.getDateTo())) {
                                                            out.println(Formater.formatDate(srcReportSale.getDateFrom(), "dd-MM-yyyy") + " s/d " + Formater.formatDate(srcReportSale.getDateTo(), "dd-MM-yyyy"));
                                                        } else {
                                                            out.println(Formater.formatDate(srcReportSale.getDateFrom(), "dd-MM-yyyy"));
                                                        }
                                                    %>
                                                </b></td>
                                        </tr>
                                        <%
                                            String namaLokasi = "";
                                        if (srcReportSale.getLocationMultiple().length()>0){
                                            try{
                                                String[] parts = srcReportSale.getLocationMultiple().split(",");
                                                for (int i = 0; i<parts.length;i++){
                                                    Location entLocation = new Location();
                                                    long oidLocation = Long.parseLong(parts[i]);
                                                    entLocation = PstLocation.fetchExc(oidLocation);
                                                    
                                                    if (namaLokasi.length()>0){
                                                        namaLokasi += ","+entLocation.getName()+"";
                                                    }else{
                                                        namaLokasi = entLocation.getName();
                                                    }    
                                                }
                                            }catch(Exception ex){
                                                namaLokasi="";
                                            }
                                        }else if (srcReportSale.getLocationId() != 0){
                                            Location entLocation = new Location();
                                            try{
                                                entLocation = PstLocation.fetchExc(srcReportSale.getLocationId());
                                                namaLokasi = entLocation.getName();
                                            }catch(Exception ex){
                                                namaLokasi="";
                                            }
                                            
                                        }
                                        
                                        if (namaLokasi.length()>0){
                                            String html = "";
                                            
                                            html += ""
                                            + "<tr align='left' valign='top'>"
                                                + "<td height='14' colspan='3' class='listgensell' valign='middle'><b>&nbsp;Lokasi : "+namaLokasi+"</b></td>"
                                            + "</tr>";
                                            
                                            out.println(html);
                                        }
                                        %>
                                        
                                        <%
                                            if (srcReportSale.getSupplierId() != 0) {
                                                try {
                                                    ContactList cnt = PstContactList.fetchExc(srcReportSale.getSupplierId());
                                        %>
                                        <tr align="left" valign="top"> 
                                            <td height="14" valign="middle" colspan="3" class="command"> 
                                                <b> &nbsp; <%= "Supplier : " + cnt.getCompName()%> </b> </td>
                                        </tr>
                                        <%
                                                } catch (Exception exx) {
                                                }
                                            }
                                        %>
                                        <%
                                            if (srcReportSale.getCategoryId() != 0) {
                                                try {
                                                    Category kategori = PstCategory.fetchExc(srcReportSale.getCategoryId());
                                        %>
                                        <tr align="left" valign="top"> 
                                            <td height="14" valign="middle" colspan="3" class="command"> 
                                                <b> &nbsp; <%= "Category : " + kategori.getName()%> </b> </td>
                                        </tr>
                                        <%
                                                } catch (Exception exx) {
                                                }
                                            }
                                        %>
                                        <%
                                            if (srcReportSale.getShiftId() != 0) {
                                                try {
                                                    Shift shf = PstShift.fetchExc(srcReportSale.getShiftId());
                                        %>
                                        <tr align="left" valign="top"> 
                                            <td height="14" valign="middle" colspan="3" class="command"> 
                                                <b> &nbsp; <%= "Shift : " + shf.getName()%> </b> </td>
                                        </tr>
                                        <%
                                                } catch (Exception exx) {
                                                }
                                            }
                                        %>
                                        <%
                                            if (srcReportSale.getOperatorId() != 0) {
                                                try {
                                                    AppUser usr = PstAppUser.fetch(srcReportSale.getOperatorId());
                                        %>
                                        <tr align="left" valign="top"> 
                                            <td height="14" valign="middle" colspan="3" class="command"> 
                                                <b> &nbsp; <%= "Cashier : " + usr.getFullName()%> </b> </td>
                                        </tr>
                                        <%
                                                } catch (Exception exx) {
                                                }
                                            }
                                        %>
                                        <tr align="left" valign="top"> 
                                            <td height="22" valign="middle" colspan="3"> <%
                                                Vector hasil = drawList(SESS_LANGUAGE, records);
                                                Vector report = new Vector(1, 1);
                                                report.add(srcReportSale);
                                                report.add(hasil);

                                                for (int k = 0; k < hasil.size(); k++) {
                                                    out.println(hasil.get(k));
                                                }

                                                try {
                                                    session.putValue("SESS_MAT_SALE_REPORT_REKAP", report);
                                                } catch (Exception e) {
                                                }
                                                %> </td>
                                        </tr>
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

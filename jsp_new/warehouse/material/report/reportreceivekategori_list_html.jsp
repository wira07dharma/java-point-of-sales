<%-- 
    Document   : reportreceivekategori_list_html
    Created on : Aug 19, 2016, 7:08:24 PM
    Author     : dimata005
--%>
<%-- 
    Document   : reportreceivekategori_list_excel
    Created on : May 4, 2016, 9:33:36 AM
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
<%@ page import = "com.dimata.posbo.entity.warehouse.*" %>
<%@ page import = "com.dimata.posbo.entity.masterdata.*" %>
<%@ page import = "com.dimata.posbo.entity.admin.*" %>
<%@ page import = "com.dimata.posbo.entity.search.*" %>
<%@ page import = "com.dimata.posbo.form.warehouse.*" %>
<%@ page import = "com.dimata.posbo.form.search.*" %>
<%@ page import = "com.dimata.posbo.session.warehouse.*" %>

<%@ include file = "../../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE_REPORT, AppObjInfo.OBJ_PURCHASE_RECEIVE_REPORT_BY_CATEGORY);%>
<%@ include file = "../../../main/checkuser.jsp" %>
<!-- Jsp Block -->
<%!   
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

public static final String textListGlobal[][] = {
	{"Tidak ada data penerimaan barang", "LAPORAN PENERIMAAN BARANG PER KATEGORI", "Periode", "s/d", "Lokasi", "Kategori", "Suplier",
	 "Laporan Penerimaan","Cetak Laporan Penerimaan","Semua Lokasi","Semua Kategori"},
	{"Receiving goods item not found", "GOODS RECEIVE REPORT BY CATEGORY", "Period", "to", "Location", "Category", "Supplier",
	 "Receive Report","Print Receive Report","All Location","All Category"}
};

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] = {
	{"NO","SKU","NAMA BARANG","QTY", "SATUAN", "HRG BELI","TOTAL BELI","HARGA JUAL","TOTAL JUAL","BARCODE"},
	{"NO","SKU","NAME","QTY", "UNIT", "COST","TOTAL COST","PRICE","TOTAL PRICE","BARCODE"}
};

public Vector drawList2(int language, Vector objectClass,SrcReportReceive srcReportReceive) {
	Vector result = new Vector(1,1);
	if(objectClass!=null && objectClass.size()>0) {
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		ctrlist.setBorder(1);

		ctrlist.addHeader(textListMaterialHeader[language][0],"5%");
		ctrlist.addHeader(textListMaterialHeader[language][1],"7%");
                ctrlist.addHeader(textListMaterialHeader[language][9],"8%");//barcode
		ctrlist.addHeader(textListMaterialHeader[language][2],"31%");
		ctrlist.addHeader(textListMaterialHeader[language][3],"13%");
		ctrlist.addHeader(textListMaterialHeader[language][4],"10%");
		ctrlist.addHeader(textListMaterialHeader[language][5],"13%");
                ctrlist.addHeader(textListMaterialHeader[language][6],"13%");
	
		ctrlist.setLinkRow(1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		
		double totalHargaBeli = 0;
		double grdTotalHargaBeli = 0;
		long oldCategoryId=0;

                double subtotalqty=0;
                double subtotalhargabeli=0;
                double subtotalhargajual=0;
                
                double totalqty=0;
                double totalhargabeli=0;
                double totalhargajual=0;

		for(int i=0; i<objectClass.size(); i++) {
			Vector vt = (Vector)objectClass.get(i);
			MatReceiveItem matReceiveItem = (MatReceiveItem)vt.get(0);
			Material material = (Material)vt.get(1);
			Unit unit = (Unit)vt.get(2); 
			Category category = (Category)vt.get(3);
			SubCategory subCategory = (SubCategory)vt.get(4);
			MatReceive matReceive = (MatReceive)vt.get(5);
			
			totalHargaBeli = matReceiveItem.getCost() * matReceiveItem.getQty() * matReceive.getTransRate();
			grdTotalHargaBeli += totalHargaBeli;
                        
			totalqty=totalqty+matReceiveItem.getQty();
                        totalhargabeli=totalhargabeli+matReceiveItem.getCost() * matReceive.getTransRate();
                        totalhargajual=totalhargajual+totalHargaBeli;

			Vector rowx = new Vector();				
			Vector temp = new Vector(1,1);
                        if(srcReportReceive.getCategoryId()==0){
                            if(oldCategoryId!=category.getOID()){
                               oldCategoryId= category.getOID();
                                
                                if(i!=0){
                                    rowx = new Vector();
                                    rowx.add("");
                                    rowx.add("");
                                    rowx.add("");
                                    rowx.add("<b>SUB TOTAL</b>");
                                    rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(subtotalqty)+"</b></div>");
                                    rowx.add("<div align=\"center\"></div>");
                                    rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(subtotalhargabeli)+"</b></div>");
                                    rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(subtotalhargajual)+"</b></div>");
                                    lstData.add(rowx);
                                }
                                
                                rowx = new Vector();
                                rowx.add("");
                                rowx.add("");
                                rowx.add("");
                                rowx.add("<b>"+category.getName()+"</b>");
                                rowx.add("");
                                rowx.add("");
                                rowx.add("");
                                rowx.add("");
                                lstData.add(rowx);

                                subtotalqty=0;
                                subtotalqty=subtotalqty+matReceiveItem.getQty();
                                subtotalhargabeli=0;
                                subtotalhargabeli=subtotalhargabeli+matReceiveItem.getCost() * matReceive.getTransRate();
                                subtotalhargajual=0;
                                subtotalhargajual=subtotalhargajual+totalHargaBeli;
                                
                            }else{
                                subtotalqty=subtotalqty+matReceiveItem.getQty();
                                subtotalhargabeli=subtotalhargabeli+matReceiveItem.getCost() * matReceive.getTransRate();
                                subtotalhargajual=subtotalhargajual+totalHargaBeli;    
                            }
                        }
                        rowx = new Vector();
			rowx.add(String.valueOf(i+1));
			rowx.add("<div align=\"left\" style='mso-number-format:\"\\@\"' >"+material.getSku()+"</div>");
                        rowx.add("<div align=\"left\" style='mso-number-format:\"\\@\"' >"+material.getBarCode()+"</div>");
			rowx.add(material.getName());
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(matReceiveItem.getQty())+"</div>");
			rowx.add("<div align=\"center\">"+unit.getCode()+"</div>");
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(matReceiveItem.getCost() * matReceive.getTransRate())+"</div>");
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(totalHargaBeli)+"</div>");
	
			lstData.add(rowx);
		}
                
                Vector rowx = new Vector();
                if(srcReportReceive.getCategoryId()==0){
                    rowx = new Vector();
                    rowx.add("");
                    rowx.add("");
                    rowx.add("");
                    rowx.add("<b>SUB TOTAL</b>");
                    rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(subtotalqty)+"</b></div>");
                    rowx.add("<div align=\"center\"></div>");
                    rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(subtotalhargabeli)+"</b></div>");
                    rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(subtotalhargajual)+"</b></div>");
                    lstData.add(rowx);
                }

                rowx = new Vector();
                rowx.add("");
                rowx.add("");
                rowx.add("");
                rowx.add("<b>GRAND TOTAL</b>");
                rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(totalqty)+"</b></div>");
                rowx.add("<div align=\"center\"></div>");
                rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(totalhargabeli)+"</b></div>");
                rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(totalhargajual)+"</b></div>");
                lstData.add(rowx);

		result.add(ctrlist.draw());
		result.add(FRMHandler.userFormatStringDecimal(grdTotalHargaBeli));
	}
	else{
		result.add("<div class=\"msginfo\">&nbsp;&nbsp;"+textListGlobal[language][0]+"</div>");		
		result.add("");
	}
	return result;
}
%>

<%    
    int iCommand = FRMQueryString.requestCommand(request);
    int start = FRMQueryString.requestInt(request, "start");

    /**
     * instantiate some object used in this page
     */
    ControlLine ctrLine = new ControlLine();
    SrcReportReceive srcReportReceive = new SrcReportReceive();
    SessReportReceive sessReportReceive = new SessReportReceive();
    FrmSrcReportReceive frmSrcReportReceive = new FrmSrcReportReceive(request, srcReportReceive);

    /**
     * handle current search data session
     */
    if(srcReportReceive.getSupplierId()==0){
        srcReportReceive.setGroupBy(2);
    }
    try {
        srcReportReceive = (SrcReportReceive) session.getValue(SessReportReceive.SESS_SRC_REPORT_RECEIVE);
        if (srcReportReceive == null) {
            srcReportReceive = new SrcReportReceive();
        }
    } catch (Exception e) {
        srcReportReceive = new SrcReportReceive();
    }

    /**
     * get vectSize, start and data to be display in this page
     */
    Vector records = sessReportReceive.getReportReceiveTotal(srcReportReceive);
    int vectSize = records.size();

    Location location = new Location();
    if (srcReportReceive.getLocationId() != 0) {
        try {
            location = PstLocation.fetchExc(srcReportReceive.getLocationId());
        } catch (Exception e) {
            System.out.println("Exc when fetch Location : " + e.toString());
        }
    } else {
        location.setName(textListGlobal[SESS_LANGUAGE][9]);
    }

    Category category = new Category();
    if (srcReportReceive.getCategoryId() != 0) {
        try {
            category = PstCategory.fetchExc(srcReportReceive.getCategoryId());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    } else {
        category.setName(textListGlobal[SESS_LANGUAGE][10]);
    }

%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
    <head>
        <!-- #BeginEditable "doctitle" --> 
        <title>Dimata - ProChain POS</title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
    </head> 
    <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
            <tr> 
                <td valign="top" align="left"> 
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">  
                        <tr> 
                            <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->&nbsp;<!-- #EndEditable --></td>
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
                                            <td height="14" colspan="3" align="center" ><h4><%=textListGlobal[SESS_LANGUAGE][1]%></h4></td>
                                        </tr>
                                        <tr align="left" valign="top"> 
                                            <td width="100%"><strong><%=textListGlobal[SESS_LANGUAGE][2]%></strong><strong>:</strong>
                                                <%=Formater.formatDate(srcReportReceive.getDateFrom(), "dd-MM-yyyy")%>
                                                <%=textListGlobal[SESS_LANGUAGE][3]%>
                                                <%=Formater.formatDate(srcReportReceive.getDateTo(), "dd-MM-yyyy")%></td>
                                        </tr>
                                        <tr align="left" valign="top"> 
                                            <td><strong><%=textListGlobal[SESS_LANGUAGE][4]%></strong><strong>:</strong> <%=location.getName().toUpperCase()%>
                                            </td>
                                            <td colspan="2"></td>
                                        </tr>
                                        <tr align="left" valign="top"> 
                                            <td><strong><%=textListGlobal[SESS_LANGUAGE][5]%></strong><strong>:</strong> <%=category.getName().toUpperCase()%>
                                            </td>
                                            <td colspan="2"></td>
                                        </tr>
                                        <tr align="left" valign="top"> 
                                            <td valign="middle" colspan="3"> 
                                                <%
                                                    Vector vctDrawList = drawList2(SESS_LANGUAGE, records,srcReportReceive);
                                                    String strList = (String) vctDrawList.get(0);
                                                    String grandTotalHargaBeli = (String) vctDrawList.get(1);
                                                    out.println(strList);
                                                %>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td align="right" >&nbsp;</td>
                                        </tr>
                                        <tr>
                                            <td valign="top" colspan="3">
                                                <table width="100%" border="0" cellspacing="0" cellpadding="0" >
                                                <tr align="left" valign="top"> 
                                                  <td height="22" valign="middle" colspan="4"></td>
                                                </tr>
                                                <tr align="left" valign="top"> 
                                                  <td height="40" valign="middle" colspan="4"></td>
                                                </tr>
                                                <tr> 
                                                  <td width="25%" align="left" nowrap>EDP/INVENTORY</td>  
                                                  <td width="25%" align="left" nowrap></td>
                                                  <td align="25" valign="left" nowrap></td>
                                                  <td width="25%" align="left" nowrap>OWNER</td>
                                                </tr>
                                                <tr align="left" valign="top"> 
                                                  <td height="75" valign="middle" colspan="4"></td>
                                                </tr>
                                                <tr> 
                                                  <td width="25%" align="left" nowrap> (.................................) 
                                                  </td>  
                                                  <td width="25%" align="left" nowrap>
                                                  </td>
                                                  <td align="25%" valign="left" nowrap>
                                                  </td>
                                                  <td width="25%" align="left" nowrap> (.................................) 
                                                  </td>
                                                </tr>
                                                 <tr align="left" valign="top"> 
                                                  <td height="22" valign="middle" colspan="4"></td>
                                                </tr>
                                                </table>

                                            </td>
                                          </tr>
                                    </table>
                                </form>
                                <!-- #EndEditable --></td> 
                        </tr> 
                    </table>
                </td>
            </tr>
        </table>
    </body>
    <!-- #EndTemplate --></html>

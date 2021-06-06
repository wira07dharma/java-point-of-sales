<%-- 
    Document   : reportreceive_return_list_excel
    Created on : 24 Agu 20, 14:13:01
    Author     : gndiw
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
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE_REPORT, AppObjInfo.OBJ_PURCHASE_RECEIVE_REPORT);%>
<%@ include file = "../../../main/checkuser.jsp" %>


<!-- Jsp Block -->
<%!   
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

public static final String textListGlobal[][] = {
	{"Tidak ada data penerimaan barang", "LAPORAN PENERIMAAN BARANG DARI PENGEMBALIAN", "Periode", "s/d", "Lokasi",
	 "Laporan Penerimaan","Cetak Laporan Penerimaan","Semua Lokasi"},
	{"Receiving goods item not found", "GOODS RECEIVE REPORT", "Period", "to", "Location",
	 "Receive Report","Print Receive Report","All Location"}
};

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] = {
	{"NO","SKU","NAMA BARANG","QTY", "SATUAN", "HRG BELI","TOTAL BELI","HARGA JUAL","TOTAL JUAL","LOKASI","SUPPLIER","BARCODE","PO","RECEIVE"},//13
	{"NO","SKU","NAME","QTY", "UNIT", "COST","TOTAL COST","PRICE","TOTAL PRICE","LOKASI","SUPPLIER","BARCODE","PO","RECEIVE"}
};

public Vector drawList2(int language, Vector objectClass) {
	Vector result = new Vector(1,1);
	if(objectClass!=null && objectClass.size()>0) {
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		ctrlist.setBorder(1);

		ctrlist.addHeader(textListMaterialHeader[language][0],"3%");
                //adding location_id & supplier_id
                ctrlist.addHeader(textListMaterialHeader[language][9],"10%");//lokasi
		ctrlist.addHeader(textListMaterialHeader[language][10],"10%");//supplier

                //ctrlist.addHeader(textListMaterialHeader[language][12],"15%");//po
                //ctrlist.addHeader(textListMaterialHeader[language][13],"15%");//receive

		ctrlist.addHeader(textListMaterialHeader[language][1],"5%");

                ctrlist.addHeader(textListMaterialHeader[language][11],"7%");//barcode

		ctrlist.addHeader(textListMaterialHeader[language][2],"25%");
		ctrlist.addHeader(textListMaterialHeader[language][3],"10%");
		ctrlist.addHeader(textListMaterialHeader[language][4],"10%");
		ctrlist.addHeader(textListMaterialHeader[language][5],"10%");
                ctrlist.addHeader(textListMaterialHeader[language][6],"10%");
	
		ctrlist.setLinkRow(1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		
		double totalHargaBeli = 0;
		double grdTotalHargaBeli = 0;
		double subtotalqty=0;
                double subtotalcost=0;
                double subtotalHargaBeli=0;
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
			
			Vector rowx = new Vector();				
			Vector temp = new Vector(1,1);
			rowx.add(String.valueOf(i+1));
                        Location location = new Location();
                        try {
                        location = PstLocation.fetchExc(matReceive.getLocationId());
                        }
                        catch(Exception e) {
                        System.out.println("Exc when fetch Location : " + e.toString());
                        }
                        rowx.add(location.getName());

                        //rowx.add(location.getName());//po
                        //rowx.add(location.getName());//kode receive
                        

                        ContactList contactList = new ContactList();
                        try	{
                        contactList = PstContactList.fetchExc(matReceive.getSupplierId());
                        }
                        catch(Exception e) {
                            System.out.println(e.toString());
                        }
                        rowx.add(contactList.getCompName());
			rowx.add("<div align=\"left\" style='mso-number-format:\"\\@\"'>"+material.getSku()+"</div>");
                        
                        rowx.add("<div align=\"left\" style='mso-number-format:\"\\@\"'>"+material.getBarCode()+"</div>");
                        
			rowx.add(material.getName());
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(matReceiveItem.getQty())+"</div>");
			rowx.add("<div align=\"center\">"+unit.getCode()+"</div>");
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(matReceiveItem.getCost() * matReceive.getTransRate())+"</div>");
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(totalHargaBeli)+"</div>");
                        
                         subtotalqty=subtotalqty+matReceiveItem.getQty();
                         subtotalcost=subtotalcost+(matReceiveItem.getCost() * matReceive.getTransRate());
                         subtotalHargaBeli=subtotalHargaBeli+totalHargaBeli;
                        
			lstData.add(rowx);
		}
                

                Vector rowx = new Vector();
                rowx.add("");
                rowx.add("");
                //rowx.add("");//po
                //rowx.add("");//kode receive
                rowx.add("");
                rowx.add("");
                rowx.add("");
                rowx.add("GRAND TOTAL");
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(subtotalqty)+"</div>");
                rowx.add("<div align=\"center\"></div>");
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(subtotalcost)+"</div>");
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(subtotalHargaBeli)+"</div>");

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

<%    int iCommand = FRMQueryString.requestCommand(request);
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
    try {
        srcReportReceive = (SrcReportReceive) session.getValue(SessReportReceive.SESS_SRC_REPORT_RECEIVE_RETURN);
        if (srcReportReceive == null) {
            srcReportReceive = new SrcReportReceive();
        }
    } catch (Exception e) {
        srcReportReceive = new SrcReportReceive();
    }

    /**
     * get vectSize, start and data to be display in this page
     */
    Vector records = sessReportReceive.getReportReceiveReturnTotal(srcReportReceive);
    int vectSize = records.size();

    Location location = new Location();
    if (srcReportReceive.getLocationId() != 0) {
        try {
            location = PstLocation.fetchExc(srcReportReceive.getLocationId());
        } catch (Exception e) {
            System.out.println("Exc when fetch Location : " + e.toString());
        }
    } else {
        location.setName(textListGlobal[SESS_LANGUAGE][7]);
    }
    
response.setContentType("application/x-msexcel"); 
response.setHeader("Content-Disposition","attachment; filename=" + "reportreceive_all_list_excel.xls" ); 
%>

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
    <head>
        <!-- #BeginEditable "doctitle" --> 
        <title>Dimata - ProChain POS</title>
    </head> 

    <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
            <tr> 
                <td valign="top" align="left"> 
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">  
                        <tr> 
                            <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" --> 
                                &nbsp;<!-- #EndEditable --></td>
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
                                            <td width="10%"><strong><%=textListGlobal[SESS_LANGUAGE][2]%></strong>
                                             <%=Formater.formatDate(srcReportReceive.getDateFrom(), "dd-MM-yyyy")%>
                                                <%=textListGlobal[SESS_LANGUAGE][3]%>
                                                <%=Formater.formatDate(srcReportReceive.getDateTo(), "dd-MM-yyyy")%>
                                            </td>
                                            <td width="90%" colspan="2">
                                               
                                            </td>
                                        </tr>
                                        <tr align="left" valign="top"> 
                                            <td><strong><%=textListGlobal[SESS_LANGUAGE][4]%> : <%=location.getName().toUpperCase()%></strong>
                                            </td>
                                            <td colspan="2"></td>
                                        </tr>
                                        <tr align="left" valign="top"> 
                                            <td valign="middle" colspan="3"> 
                                                <%
                                                    Vector vctDrawList = drawList2(SESS_LANGUAGE, records);
                                                    String strList = (String) vctDrawList.get(0);
                                                    String grandTotalHargaBeli = (String) vctDrawList.get(1);
                                                    out.println(strList);
                                                %>
                                            </td>
                                        </tr>
                                        <% if (records.size() > 0) {%>
                                        <tr>
                                            <td align="right" colspan="3">&nbsp;</td>
                                        </tr>
                                        <tr>
                                            <td align="right"><b>GRAND <%=textListMaterialHeader[SESS_LANGUAGE][6]%>&nbsp;:&nbsp;&nbsp;&nbsp;<%=grandTotalHargaBeli%></b></td>
                                        </tr>
                                        <% }%>
                                        <tr>
                                            <td align="right" colspan="3">&nbsp;</td>
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

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
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE_REPORT, AppObjInfo.OBJ_PURCHASE_RECEIVE_REPORT); %>
<%@ include file = "../../../main/checkuser.jsp" %>


<!-- Jsp Block -->
<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

public static final String textListGlobal[][] = {
	{"Tidak ada data penerimaan barang", "LAPORAN PENERIMAAN BARANG", "Periode", "s/d", "Lokasi",
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
if(iCommand==Command.BACK || iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST) {
	 try { 
		srcReportReceive = (SrcReportReceive)session.getValue(SessReportReceive.SESS_SRC_REPORT_RECEIVE); 
		if (srcReportReceive == null) srcReportReceive = new SrcReportReceive();
	 }
	 catch(Exception e) { 
		srcReportReceive = new SrcReportReceive();
	 }
}
else {
	 frmSrcReportReceive.requestEntityObject(srcReportReceive);
	 session.putValue(SessReportReceive.SESS_SRC_REPORT_RECEIVE, srcReportReceive);
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
}
catch(Exception e) {
	System.out.println("Exc when fetch Location : " + e.toString());
}
}else {
    location.setName(textListGlobal[SESS_LANGUAGE][7]);
}
%>
<!-- End of Jsp Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
<!--
function cmdBack() {
	document.frm_reportsale.command.value="<%=Command.BACK%>";
	document.frm_reportsale.action="src_reportreceive_all.jsp";
	document.frm_reportsale.submit();
}

function printForm() {
	window.open("reportreceive_all_list_html.jsp","allreceivereportall","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
	//window.open("buff_pdf_report_receive.jsp","form_report_receive");
       // window.open("reportreceive_all_list_excel.jsp");
}

function printFormExcel(){
    window.open("reportreceive_all_list_excel.jsp");
}

//------------------------- START JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------

//------------------------- END JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------

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

//-->
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
<!-- #EndEditable --> 
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
  <%if(menuUsed == MENU_PER_TRANS){%>
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
                  <td width="10%"><strong><%=textListGlobal[SESS_LANGUAGE][2]%></strong></td>
				  <td width="90%" colspan="2"><strong>:</strong>
				  	<%=Formater.formatDate(srcReportReceive.getDateFrom(), "dd-MM-yyyy")%>
					<%=textListGlobal[SESS_LANGUAGE][3]%>
					<%=Formater.formatDate(srcReportReceive.getDateTo(), "dd-MM-yyyy")%>
				  </td>
                </tr>
                <tr align="left" valign="top"> 
                  <td><strong><%=textListGlobal[SESS_LANGUAGE][4]%></strong>
                  </td>
                  <td colspan="2"><strong>:</strong> <%=location.getName().toUpperCase()%></td>
                </tr>
		<tr align="left" valign="top"> 
                  <td valign="middle" colspan="3"> 
                    <%
				  	Vector vctDrawList = drawList2(SESS_LANGUAGE, records);
					String strList = (String)vctDrawList.get(0);
					String grandTotalHargaBeli = (String)vctDrawList.get(1);
					out.println(strList);
				  %>
                  </td>
                </tr>
                <% if(records.size()>0) { %>
                <tr>
                  <td align="right" colspan="3">&nbsp;</td>
                </tr>
                <tr>
                  <td align="right" colspan="3"><b>GRAND <%=textListMaterialHeader[SESS_LANGUAGE][6]%>&nbsp;:&nbsp;&nbsp;&nbsp;<%=grandTotalHargaBeli%></b></td>
                </tr>
                <% } %>
                <tr>
                  <td align="right" colspan="3">&nbsp;</td>
                </tr>
               
                <tr align="left" valign="top"> 
                  <td height="18" valign="top" colspan="3"> <table width="100%" border="0">
                      <tr> 
                        <td width="80%"> <table width="52%" border="0" cellspacing="0" cellpadding="0">
                            <tr> 
                              <td class="command" nowrap width="90%"><a class="btn-primary btn-lg" href="javascript:cmdBack()"><i class="fa fa-backward"> &nbsp;<%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][5],ctrLine.CMD_BACK_SEARCH,true)%></i></a></td>
                            </tr>
                          </table>
						</td>
                        <td width="20%">
						  <table width="100%" border="0" cellpadding="0" cellspacing="0">
                            <%
							if(records.size()>0){
							%>
							<tr> 
                              <td width="45%" nowrap>&nbsp; <a class="btn-primary btn-lg" href="javascript:printForm()" class="command"><i class="fa fa-print">&nbsp;<%=textListGlobal[SESS_LANGUAGE][6]%></i></a></td>
                              <td width="45%" nowrap>&nbsp; <a class="btn-primary btn-lg" href="javascript:printFormExcel()" class="command"><i class="fa fa-print">&nbsp;Export Excel</i></a></td>
                            </tr>
							<%}%>
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
        <%if(menuUsed == MENU_ICON){%>
            <%@include file="../../../styletemplate/footer.jsp" %>
        <%}else{%>
            <%@ include file = "../../../main/footer.jsp" %>
        <%}%>
      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>

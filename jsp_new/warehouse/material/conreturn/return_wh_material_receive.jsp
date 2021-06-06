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
<%@ page import = "com.dimata.posbo.entity.search.*" %>
<%@ page import = "com.dimata.posbo.form.warehouse.*" %>
<%@ page import = "com.dimata.posbo.form.search.*" %>
<%@ page import = "com.dimata.posbo.session.warehouse.*" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RETURN, AppObjInfo.G2_SUPPLIER_RETURN, AppObjInfo.OBJ_SUPPLIER_RETURN); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<%!
public static final String textListGlobal[][] = {
	{"Gudang","Retur Barang","Pencarian Invoice","Nomor Invoice","Cari Invoice"},
	{"Warehouse","Goods Return","Search Invoice","Invoice Number","Invoice Search"}
};

public static final String textListMaterialHeader[][] = 
{
	{"No","Nota","No Penerimaan","Tanggal Penerimaan"},
	{"No","invoice","Receive Number","Receive Date"}
};

public String drawList(int language,Vector objectClass, int start){
	String result = "";
	if(objectClass!=null && objectClass.size()>0){
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("80%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		ctrlist.addHeader(textListMaterialHeader[language][0],"5%");
		ctrlist.addHeader(textListMaterialHeader[language][1],"15%");
		ctrlist.addHeader(textListMaterialHeader[language][2],"15%");
		ctrlist.addHeader(textListMaterialHeader[language][3],"15%");
		
		ctrlist.setLinkRow(1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		ctrlist.setLinkPrefix("javascript:change('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();

		for(int i=0; i<objectClass.size(); i++) 
		{
			MatReceive rec = (MatReceive)objectClass.get(i);
			
			Vector rowx = new Vector();
			
			rowx.add(""+(start+i+1));
			rowx.add(rec.getInvoiceSupplier());
			rowx.add(rec.getRecCode());
			String str_dt_RecDate = ""; 
			try
			{
				Date dt_RecDate = rec.getReceiveDate();
				if(dt_RecDate==null)
				{
					dt_RecDate = new Date();
				}	
				str_dt_RecDate = Formater.formatDate(dt_RecDate, "dd-MM-yyyy");
			}
			catch(Exception e){ str_dt_RecDate = ""; }
			rowx.add(str_dt_RecDate);
			
			lstData.add(rowx);
			lstLinkData.add(rec.getInvoiceSupplier()+"', '"+rec.getOID()+"', '"+rec.getPurchaseOrderId()+"', '"+rec.getLocationId());
		}
		result = ctrlist.draw();
	}
	else
	{
		result = "<div class=\"msginfo\">&nbsp;&nbsp;Tidak ada invoice yang masuk ...</div>";		
	}
	return result;
}

%>
<!-- JSP Block -->
<%
	ControlLine ctrLine = new ControlLine();
	CtrlMatReceive ctrlMatReceive = new CtrlMatReceive(request);
	int iCommand = FRMQueryString.requestCommand(request);
	int start = FRMQueryString.requestInt(request, "start");
	long supplierId = FRMQueryString.requestLong(request, "supplier_id");
	String supplierNota = FRMQueryString.requestString(request, "supplier_nota");
	int recordToGet = 20;
	
	String whereClasue = "";
	String orderClause = PstMatReceive.fieldNames[PstMatReceive.FLD_INVOICE_SUPPLIER];
	whereClasue = PstMatReceive.fieldNames[PstMatReceive.FLD_TRANSFER_STATUS]+"="+I_DocStatus.DOCUMENT_STATUS_DRAFT+
		" AND ("+PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE]+"="+PstMatReceive.SOURCE_FROM_SUPPLIER+
		" OR "+PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE]+"="+PstMatReceive.SOURCE_FROM_SUPPLIER_PO+")"+
		" AND "+PstMatReceive.fieldNames[PstMatReceive.FLD_SUPPLIER_ID]+"="+supplierId+
		" AND "+PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_TYPE]+"="+PstLocation.TYPE_LOCATION_WAREHOUSE;
	
	if(!supplierNota.equals("")){
		whereClasue = whereClasue + " AND "+ PstMatReceive.fieldNames[PstMatReceive.FLD_INVOICE_SUPPLIER]+" LIKE '%"+supplierNota+"%'"; 
	}
	
	int vectSize = PstMatReceive.getCount(whereClasue);
	if(iCommand==Command.FIRST || iCommand==Command.NEXT || iCommand==Command.PREV || iCommand==Command.LAST || iCommand==Command.LIST) {
		start = ctrlMatReceive.actionList(iCommand,start,vectSize,recordToGet);
	}
	Vector records = PstMatReceive.list(start, recordToGet, whereClasue, orderClause);
%>
<!-- End of JSP Block -->  

<html><!-- #BeginTemplate "/Templates/maindosearch.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->  
<title>Dimata - ProChain POS</title>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<!-- #BeginEditable "styles" --> 
<link rel="stylesheet" href="../../../styles/main.css" type="text/css"> 
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" --> 
<link rel="stylesheet" href="../../../styles/tab.css" type="text/css"> 
<!-- #EndEditable -->
<!-- #BeginEditable "headerscript" -->
<SCRIPT language=JavaScript>
function change(str, idRcv, idPo, idLocation){
	self.opener.frm_retmaterial.<%=FrmMatReturn.fieldNames[FrmMatReturn.FRM_FIELD_INVOICE_SUPPLIER]%>.value = str;
	self.opener.frm_retmaterial.<%=FrmMatReturn.fieldNames[FrmMatReturn.FRM_FIELD_RECEIVE_MATERIAL_ID]%>.value = idRcv;
	self.opener.frm_retmaterial.<%=FrmMatReturn.fieldNames[FrmMatReturn.FRM_FIELD_PURCHASE_ORDER_ID]%>.value = idPo;
	self.opener.frm_retmaterial.<%=FrmMatReturn.fieldNames[FrmMatReturn.FRM_FIELD_LOCATION_ID]%>.value = idLocation;
	self.opener.frm_retmaterial.all.item("idcommand").style.display = "";
	self.opener.frm_retmaterial.all.item("idcommandback").style.display = "none";
	self.close();
}

function cmdSearch(){
    document.frmsearch.action = "return_wh_material_receive.jsp"
    document.frmsearch.submit();
}

function cmdListFirst() {
	document.form_mat_receive.command.value="<%=Command.FIRST%>";
	document.form_mat_receive.action="return_wh_material_receive.jsp";
	document.form_mat_receive.submit();
}

function cmdListPrev(){
	document.form_mat_receive.command.value="<%=Command.PREV%>";
	document.form_mat_receive.action="return_wh_material_receive.jsp";
	document.form_mat_receive.submit();
}

function cmdListNext(){
	document.form_mat_receive.command.value="<%=Command.NEXT%>";
	document.form_mat_receive.action="return_wh_material_receive.jsp";
	document.form_mat_receive.submit();
}

function cmdListLast(){
	document.form_mat_receive.command.value="<%=Command.LAST%>";
	document.form_mat_receive.action="return_wh_material_receive.jsp";
	document.form_mat_receive.submit();
}

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
</SCRIPT>
<!-- #EndEditable --> 
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<!-- #BeginEditable "editfocus" -->
<script language="JavaScript" type="text/JavaScript">
	window.focus();
</script>

<!-- #EndEditable -->    
<table width="100%" border="0" cellspacing="3" cellpadding="2" height="100%" bgcolor="#FCFDEC" >
  <tr> 
    <td width="88%" valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0"> 
        <tr> 
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->
            <%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][1]%> &gt; <%=textListGlobal[SESS_LANGUAGE][2]%><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" -->
			<form name="form_mat_receive" method="post" action="">
			  <input type="hidden" name="command" value="">
			  <input type="hidden" name="start" value="<%=start%>">
			  <input name="supplier_id" type="hidden" value="<%=supplierId%>">
			  <input name="supplier_nota" type="hidden" value="<%=supplierNota%>">
              <table width="100%" border="0" cellspacing="1" cellpadding="1">
                <!--tr> 
                  <td width="8%"><%=textListGlobal[SESS_LANGUAGE][3]%></td>
                  <td colspan="2">: <input name="txt_invoice" type="text" class="formElemen" value="<%//=invoice%>"></td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  <td width="2%"><a href="javascript:cmdSearch()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200','','<%=approot%>/images/BtnSearchOn.jpg',1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][4],ctrLine.CMD_SEARCH,true)%>"></a></td>
				  <td width="90%" align="left" >&nbsp;&nbsp;<a href="javascript:cmdSearch()"><%=textListGlobal[SESS_LANGUAGE][4]%></a></td>
                </tr-->
                <tr> 
                  <td colspan="3"><%=drawList(SESS_LANGUAGE, records, start)%></td>
                </tr>					  					
			    <tr align="left" valign="top"> 
					<td height="8" align="left" colspan="3" class="command"> 
					  <span class="command"> 
					  <%
						ctrLine.setLocationImg(approot+"/images");
						ctrLine.initDefault();
						out.println(ctrLine.drawImageListLimit(iCommand,vectSize,start,recordToGet));
					  %> 
					  </span>
					</td>
			    </tr>
                <tr> 
                  <td colspan="3">&nbsp;</td>
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
      <%@ include file = "../../../main/footer.jsp" %>
      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>

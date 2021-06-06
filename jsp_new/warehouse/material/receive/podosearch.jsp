<%@page contentType="text/html"%>
<!-- package java -->
<%@ page import = "java.util.*,
                   com.dimata.posbo.entity.purchasing.PurchaseOrder,
                   com.dimata.posbo.entity.purchasing.PstPurchaseOrder,
                   com.dimata.posbo.form.purchasing.CtrlPurchaseOrder,
                   com.dimata.posbo.form.warehouse.FrmMatReceive,
                   com.dimata.common.entity.payment.CurrencyType,
                   com.dimata.common.entity.payment.PstCurrencyType" %>
<%@ page import = "java.lang.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<!-- package qdep -->
<%@ page import = "com.dimata.qdep.entity.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE); %>
<%@ include file = "../../../main/checkuser.jsp" %>


<%!
/* this constant used to list text of listHeader */
public static final String textPurchaseOrderHeader[][] = {
	{"Bulan,Tahun Order"},
	{"PO Month"}
};

/* this constant used to list text of listHeader */
public static final String textListPurchaseOrderHeader[][] = { 
	{"No","Kode","Tanggal","Status","Keterangan","Mata Uang"},
	{"No","Code","Date","Status","Remark","Currency"}
};

public String drawListPurchaseOrder(int language,Vector objectClass,int start,I_DocStatus i_status,int docType) {
	String result = "";
	if(objectClass!=null && objectClass.size()>0) {
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%"); 
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		
		ctrlist.addHeader(textListPurchaseOrderHeader[language][0],"3%");	
		ctrlist.addHeader(textListPurchaseOrderHeader[language][1],"15%");	
		ctrlist.addHeader(textListPurchaseOrderHeader[language][2],"15%");
                ctrlist.addHeader(textListPurchaseOrderHeader[language][5],"10%");
		ctrlist.addHeader(textListPurchaseOrderHeader[language][3],"15%");
		ctrlist.addHeader(textListPurchaseOrderHeader[language][4],"20%");	
	
		ctrlist.setLinkRow(1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();
		int index = -1;
		
		if(start<0) start = 0;
			
		for(int i=0; i<objectClass.size(); i++) {
			//Vector vt = (Vector)objectClass.get(i);			
			PurchaseOrder po = (PurchaseOrder)objectClass.get(i);
			start = start + 1;			
	
			Vector rowx = new Vector();
			
			String str_dt_PurchDate = ""; 
			try	{
				Date dt_PurchDate = po.getPurchDate();
				if(dt_PurchDate==null) {
					dt_PurchDate = new Date();
				}	
				str_dt_PurchDate = Formater.formatDate(dt_PurchDate, "dd-MM-yyyy");
			}
			catch(Exception e) {
				str_dt_PurchDate = "";
			}
			
            CurrencyType currencyType = new CurrencyType();
            try {
                currencyType = PstCurrencyType.fetchExc(po.getCurrencyId());
            }
			catch(Exception e) {
			}
			
			rowx.add(""+start);
			rowx.add(po.getPoCode());
			rowx.add(str_dt_PurchDate);
            rowx.add(currencyType.getCode());
			rowx.add(i_status.getDocStatusName(docType,po.getPoStatus()));
			rowx.add(po.getRemark());
			
			lstData.add(rowx);
			lstLinkData.add(po.getOID()+"','"+po.getPoCode()+"','"+currencyType.getCode()+"','"+po.getCurrencyId()
							+"', '"+po.getPoStatus()+"', '"+po.getSupplierId()+"', '"+po.getPpn()+"', '"+po.getLocationId()+"', '"+po.getTermOfPayment()+"','"+po.getCreditTime()+"','"+po.getIncludePpn()+"','"+po.getExchangeRate());
                       // lstLinkData.add(po.getOID()+"','"+po.getPoCode()+"','"+currencyType.getCode()+"','"+po.getCurrencyId()
							//+"', '"+po.getPoStatus()+"', '"+po.getSupplierId()+"', '"+po.getPpn()+"', '"+po.getLocationId()+"', '"+po.getIncludePpn());
		}
		return ctrlist.draw();
	}
	else {
		result = "<div class=\"msginfo\">&nbsp;&nbsp;Tidak ada data order pembelian...</div>";				
	}	
	return result;
}
%>

<!-- JSP Block -->
<%
long oidVendor = FRMQueryString.requestLong(request,"oidVendor");
String poCode = FRMQueryString.requestString(request,"po_code");
int start = FRMQueryString.requestInt(request, "start");
int iCommand = FRMQueryString.requestCommand(request);
int month_combo = FRMQueryString.requestInt(request, "month_combo");
int year_combo = FRMQueryString.requestInt(request, "year_combo");
int recordToGet = 15;
int monthOfPO = month_combo;
int yearOfPO = year_combo;
I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();
I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();
int docType = i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL,I_DocType.MAT_DOC_TYPE_LMRR);
String pageTitle = "Pencarian Order Pembelian";

if (monthOfPO == 0) {
	Date asu = new Date();
	monthOfPO = asu.getMonth() + 1;
}
if (yearOfPO == 0) {
	Date asulagi = new Date();
	yearOfPO = asulagi.getYear() + 1900;	
}
String whereClause = "";
if(poCode == ""){
//String whereClause = PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_SUPPLIER_ID] + " = " + oidVendor +
  whereClause = PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_SUPPLIER_ID] + " = " + oidVendor +
		" AND Month(" + PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PURCH_DATE] + " ) = " + monthOfPO +
		" AND Year(" + PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PURCH_DATE] + " ) = " + yearOfPO ;

//Po yang ditampilkan hanya final dan closed saja
      /* whereClause += " AND (" + PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PO_STATUS] + " = 2 " +
                     " OR " + PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PO_STATUS] + " = 7 )";*/
   whereClause += " AND (" + PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PO_STATUS] + " = 2 )";
}
if(poCode != "") {
	//whereClause += " OR " + PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PO_CODE] + " like '%" + poCode + "%'";
        whereClause = PstPurchaseOrder.fieldNames[PstPurchaseOrder.FLD_PO_CODE] + " like '%" + poCode + "%'";
}

int vectSize = PstPurchaseOrder.getCount(whereClause);

CtrlPurchaseOrder ctrlPurchaseOrder = new CtrlPurchaseOrder(request);
if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST) {
	start = ctrlPurchaseOrder.actionList(iCommand, start, vectSize, recordToGet);
} 
Vector vect = PstPurchaseOrder.list(start, recordToGet, whereClause, "");

%>
<!-- End of JSP Block -->
<html>
<head>
<title>Dimata - ProChain POS</title
><script language="JavaScript">
function cmdEdit(poOid, poCode, currcode, currOid, poStatus, supplierId, ppn, locationId,termsPay,creditTime,includePpn,exchangeRate) {
	if(poStatus != <%=I_DocStatus.DOCUMENT_STATUS_POSTED%>) {
		self.opener.document.forms.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_PURCHASE_ORDER_ID]%>.value = poOid;
		self.opener.document.forms.frm_recmaterial.txt_ponumber.value = poCode;
		self.opener.document.forms.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_CURRENCY_ID]%>.value = currOid;
		/*self.opener.document.forms.frm_recmaterial.CURRENCY_CODE.value = currOid;*/
		self.opener.document.forms.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_SUPPLIER_ID]%>.value = supplierId;
		self.opener.document.forms.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_TOTAL_PPN]%>.value = ppn;
		self.opener.document.forms.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_LOCATION_ID]%>.value = locationId;
                self.opener.document.forms.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_TERM_OF_PAYMENT]%>.value = termsPay;
                self.opener.document.forms.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_CREDIT_TIME]%>.value = creditTime;
                self.opener.document.forms.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_TRANS_RATE]%>.value = exchangeRate;
                //self.opener.document.forms.frm_recmaterial.exchangeRate.value = exchangeRate;
                //self.opener.document.forms.frm_recmaterial.<%//=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_INCLUDE_PPN]%>.value = includePpn;
		self.close();
	}
	else {
		alert("Document has been posted!");
	}
}




function cmdListFirst() {
	document.frmvendorsearch.command.value="<%=Command.FIRST%>";
	document.frmvendorsearch.action="podosearch.jsp";
	document.frmvendorsearch.submit();
}

function cmdListPrev(){
	document.frmvendorsearch.command.value="<%=Command.PREV%>";
	document.frmvendorsearch.action="podosearch.jsp";
	document.frmvendorsearch.submit();
}

function cmdListNext(){
	document.frmvendorsearch.command.value="<%=Command.NEXT%>";
	document.frmvendorsearch.action="podosearch.jsp";
	document.frmvendorsearch.submit();
}

function cmdListLast(){
	document.frmvendorsearch.command.value="<%=Command.LAST%>";
	document.frmvendorsearch.action="podosearch.jsp";
	document.frmvendorsearch.submit();
}	

function cmdSearch(){
	document.frmvendorsearch.command.value="<%=Command.LIST%>";
	document.frmvendorsearch.action="podosearch.jsp";
	document.frmvendorsearch.submit();
}	

function clear(){
	document.frmvendorsearch.txt_materialcode.value="";
}	


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
</script>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="../../../styles/main.css" type="text/css">
<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
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
</head>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<script language="JavaScript">
	window.focus();
</script>
<table width="100%" border="0" cellspacing="3" cellpadding="2" height="100%" bgcolor="#FCFDEC" >
  <tr> <%@include file="../../../styletemplate/template_header_empty.jsp" %>
    <td width="88%" valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td height="20" class="mainheader" colspan="2"><%=pageTitle%> </td>
        </tr>
        <tr> 
          <td colspan="2"> 
            <hr size="1">
          </td>
        </tr>
        <tr> 
          <td> 
            <form name="frmvendorsearch" method="post" action="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="oidVendor" value="<%=oidVendor%>">
              <table width="100%" border="0" cellspacing="1" cellpadding="1">
                <tr> 
                  <td width="18%"><%=textPurchaseOrderHeader[SESS_LANGUAGE][0]%></td>
                  <td width="82%"> : 
                    <% 
					Vector obj_monthid = new Vector(1,1); 
					Vector val_monthid = new Vector(1,1); 
					Vector key_monthid = new Vector(1,1); 
					Date jani = new Date();
					int bulan_awal = jani.getMonth()+1;
					for(int d = 1;d < 13; d++)
					{
						val_monthid.add(""+d);
						key_monthid.add(""+d);
					}
					String select_monthid = "";
					if (month_combo != 0)
					{
						select_monthid = ""+month_combo; //selected on combo box
					}
					else
					{
						select_monthid = ""+bulan_awal; //selected on combo box
					}	
				  %>
				  <%=ControlCombo.draw("month_combo", null, select_monthid, val_monthid, key_monthid, "", "formElemen")%>
				  ,
				  <% 
					Vector obj_yearid = new Vector(1,1); 
					Vector val_yearid = new Vector(1,1); 
					Vector key_yearid = new Vector(1,1); 
					Date sekarang = new Date();
					int tahun_awal = sekarang.getYear() + 1900 -2;
					for(int d = 0;d < 5; d++)
					{
						val_yearid.add(""+(d+tahun_awal));
						key_yearid.add(""+(d+tahun_awal));
					}
					String select_yearid = "";
					if (year_combo != 0)
					{
						select_yearid = ""+year_combo; //selected on combo box
					}
					else
					{
						select_yearid = ""+(String)key_yearid.get(2); //selected on combo box
					}	
				  %>
				  <%=ControlCombo.draw("year_combo", null, select_yearid, val_yearid, key_yearid, "", "formElemen")%>
				</td>
			</tr>
                <tr> 
                  <td width="18%">&nbsp;</td>
                  <td width="82%"> 
                    <input type="button" name="Button" value="Search" onClick="javascript:cmdSearch()" class="formElemen">
                  </td>
                </tr>
                <tr> 
                  <td colspan="2"><%=drawListPurchaseOrder(SESS_LANGUAGE,vect,start,i_status,docType)%></td>
                </tr>
                <tr> 
                  <td colspan="2"><span class="command"> 
                    <span class="command"> 
					  <%
						ControlLine ctrLine = new ControlLine();
						ctrLine.setLocationImg(approot+"/images");
						ctrLine.initDefault();
						out.println(ctrLine.drawImageListLimit(iCommand,vectSize,start,recordToGet));
					  %> 
				    </span>
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
</html>

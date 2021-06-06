<%@page contentType="text/html"%>
<!-- package java -->
<%@ page import = "java.util.*,
                   com.dimata.posbo.entity.warehouse.MatDispatch,
                   com.dimata.posbo.entity.warehouse.PstMatDispatch,
                   com.dimata.posbo.form.warehouse.CtrlMatDispatch,
                   com.dimata.posbo.form.warehouse.FrmMatReceive" %>
<%@ page import = "java.lang.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<!-- package qdep -->
<%@ page import = "com.dimata.qdep.entity.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_LOCATION_RECEIVE, AppObjInfo.OBJ_LOCATION_RECEIVE); %>
<%@ include file = "../../../main/checkuser.jsp" %>


<%!
/* this constant used to list text of listHeader */
public static final String textListGlobal[][] = {
	{"Daftar Transfer Barang","Bulan Pengiriman","Tidak ada data..."},
	{"List of Goods Transfer","Dispatch Month","No available data"}
};

/* this constant used to list text of listHeader */
public static final String textListMatDispatchHeader[][] = { 
	{"No","Kode","Tanggal","Status","Keterangan"},
	{"No","Code","Date","Status","Remark"}
};

public String drawListMatDispatch(int language,Vector objectClass,int start,I_DocStatus i_status,int docType) {
	String result = "";
	if(objectClass!=null && objectClass.size()>0) {
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%"); 
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		
		ctrlist.addHeader(textListMatDispatchHeader[language][0],"3%");	
		ctrlist.addHeader(textListMatDispatchHeader[language][1],"15%");	
		ctrlist.addHeader(textListMatDispatchHeader[language][2],"15%");					
		ctrlist.addHeader(textListMatDispatchHeader[language][3],"15%");		
		ctrlist.addHeader(textListMatDispatchHeader[language][4],"20%");	
	
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
			MatDispatch df = (MatDispatch)objectClass.get(i);
			start = start + 1;			
	
            //if(oidloc==df.getDispatchTo()) {
                Vector rowx = new Vector();
                rowx.add(""+start);
                rowx.add(df.getDispatchCode());

                rowx.add(Formater.formatDate(df.getDispatchDate(), "dd-MM-yyyy"));
                rowx.add(i_status.getDocStatusName(docType,df.getDispatchStatus()));
                rowx.add(df.getRemark());

                lstData.add(rowx);
                lstLinkData.add(df.getOID()+"','"+df.getDispatchCode()+"', '"+df.getLocationId()+"', '"+df.getDispatchTo());
            //}
		}
		return ctrlist.draw();
	}
	else {
		result = "<div class=\"msginfo\">&nbsp;&nbsp;"+textListGlobal[language][2]+"</div>";
	}	
	return result;
}
%>

<!-- JSP Block -->
<%
long oidFrom = FRMQueryString.requestLong(request,"oidFrom");
long oidTo = FRMQueryString.requestLong(request,"oidTo");
int start = FRMQueryString.requestInt(request, "start");
int iCommand = FRMQueryString.requestCommand(request);
int month_combo = FRMQueryString.requestInt(request, "month_combo");
int year_combo = FRMQueryString.requestInt(request, "year_combo");
int recordToGet = 20;
int monthOfDF = month_combo;
int yearOfDF = year_combo;
I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();
I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();
int docType = i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL,I_DocType.MAT_DOC_TYPE_LMRR);
String txtDfNumber = FRMQueryString.requestString(request,"txt_dfnumber");

if (monthOfDF == 0) {
	Date asu = new Date();
	monthOfDF = asu.getMonth() + 1;
}
if (yearOfDF == 0) {
	Date asulagi = new Date();
	yearOfDF = asulagi.getYear() + 1900;	
}

//update opie-eyek 20131122
/**
menampilkan dokument transfer dari toko/gudang yang status transfer=draft dan status dokument = final
*/
String whereClause = " " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + " = " + oidFrom +
		" AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO] + " = " + oidTo +
		" AND Month(" + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] + " ) = " + monthOfDF +
		" AND Year(" + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] + " ) = " + yearOfDF +
		" AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_TRANSFER_STATUS] + " = " + i_status.DOCUMENT_STATUS_DRAFT+
                " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + " = " + i_status.DOCUMENT_STATUS_FINAL;

//update opie-eyek 20131122
/**
menampilkan dokument transfer dari toko/gudang yang status transfer=draft dan status dokument = final
*/
if(txtDfNumber != "" && txtDfNumber.length() > 0) {
	whereClause = PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE] + " like '%" + txtDfNumber + "%'"+
		//" AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_TRANSFER_STATUS] + " = " + i_status.DOCUMENT_STATUS_DRAFT;
                " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_TRANSFER_STATUS] + " = " + i_status.DOCUMENT_STATUS_DRAFT+
                " AND " + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + " = " + i_status.DOCUMENT_STATUS_FINAL;
}
//out.println(whereClause);		
Vector vect = PstMatDispatch.list(0,0,whereClause,"");
int vectSize = vect.size();

CtrlMatDispatch ctrlMatDispatch = new CtrlMatDispatch(request);
if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST) {
	start = ctrlMatDispatch.actionList(iCommand, start, vectSize, recordToGet);
} 
 
%>
<!-- End of JSP Block -->
<html>
<head>
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
function cmdEdit(dfOid, dfCode, locDf, locRcv) {
	self.opener.document.forms.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_DISPATCH_MATERIAL_ID]%>.value = dfOid;
	self.opener.document.forms.frm_recmaterial.txt_dfnumber.value = dfCode;
	self.opener.document.forms.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_FROM]%>.value = locDf;
	self.opener.document.forms.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_LOCATION_ID]%>.value = locRcv;
	self.close();		
}

function cmdListFirst() {
	document.frmvendorsearch.command.value="<%=Command.FIRST%>";
	document.frmvendorsearch.action="dfdosearch.jsp";
	document.frmvendorsearch.submit();
}

function cmdListPrev() {
	document.frmvendorsearch.command.value="<%=Command.PREV%>";
	document.frmvendorsearch.action="dfdosearch.jsp";
	document.frmvendorsearch.submit();
}

function cmdListNext() {
	document.frmvendorsearch.command.value="<%=Command.NEXT%>";
	document.frmvendorsearch.action="dfdosearch.jsp";
	document.frmvendorsearch.submit();
}

function cmdListLast() {
	document.frmvendorsearch.command.value="<%=Command.LAST%>";
	document.frmvendorsearch.action="dfdosearch.jsp";
	document.frmvendorsearch.submit();
}	

function cmdSearch() {
	document.frmvendorsearch.command.value="<%=Command.LIST%>";
	document.frmvendorsearch.action="dfdosearch.jsp";
	document.frmvendorsearch.submit();
}	

function clear() {
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
  <tr> 
    <td width="88%" valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td height="20" class="mainheader" colspan="2"><%=textListGlobal[SESS_LANGUAGE][0]%> </td>
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
              <input type="hidden" name="oidFrom" value="<%=oidFrom%>">
              <input type="hidden" name="oidTo" value="<%=oidTo%>">
              <table width="100%" border="0" cellspacing="1" cellpadding="1">
                <tr>
                  <td width="18%"><%=textListGlobal[SESS_LANGUAGE][1]%></td>
                  <td width="82%"> 
                    <% 
					Vector obj_monthid = new Vector(1,1); 
					Vector val_monthid = new Vector(1,1); 
					Vector key_monthid = new Vector(1,1); 
					Date jani = new Date();
					int bulan_awal = jani.getMonth()+1;
					for(int d = 1;d < 13; d++) {
						val_monthid.add(""+d);
						key_monthid.add(""+d);
					}
					String select_monthid = "";
					if (month_combo != 0) {
						select_monthid = ""+month_combo; //selected on combo box
					}
					else {
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
					for(int d = 0;d < 5; d++) {
						val_yearid.add(""+(d+tahun_awal));
						key_yearid.add(""+(d+tahun_awal));
					}
					String select_yearid = "";
					if (year_combo != 0) {
						select_yearid = ""+year_combo; //selected on combo box
					}
					else {
						select_yearid = ""+(String)key_yearid.get(2); //selected on combo box
					}	
				  %>
				  <%=ControlCombo.draw("year_combo", null, select_yearid, val_yearid, key_yearid, "", "formElemen")%> 
                    <input type="button" name="Button" value="Search" onClick="javascript:cmdSearch()" class="formElemen">
                  </td>
				</tr>
                <tr> 
                  <td width="18%">&nbsp;</td>
                  <td width="82%">&nbsp; </td>
                </tr>
                <tr> 
                  <td colspan="2"><%=drawListMatDispatch(SESS_LANGUAGE,vect,start,i_status,docType)%></td>
                </tr>
                <tr> 
                  <td colspan="2"><span class="command"></span></td>
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

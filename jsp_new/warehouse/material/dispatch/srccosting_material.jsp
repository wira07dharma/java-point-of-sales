<%@page import="com.dimata.posbo.entity.warehouse.MatDispatch"%>
<%@page import="com.dimata.posbo.session.warehouse.SessMatDispatch"%>
<%@page import="com.dimata.qdep.form.FRMMessage"%>
<%@page import="com.dimata.posbo.form.warehouse.CtrlMatCosting"%>
<%@page import="com.dimata.gui.jsp.ControlList"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatCosting"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@ page import="com.dimata.posbo.entity.search.SrcMatCosting,
                 com.dimata.posbo.form.search.FrmSrcMatCosting,
                 com.dimata.posbo.session.warehouse.SessMatCosting,
                 com.dimata.common.entity.location.PstLocation,
                 com.dimata.common.entity.location.Location,
                 com.dimata.qdep.entity.I_PstDocType,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.util.Command,
                 com.dimata.gui.jsp.ControlCombo,
                 com.dimata.gui.jsp.ControlDate"%>
<%@ page language = "java" %>

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_COSTING, AppObjInfo.G2_COSTING, AppObjInfo.OBJ_COSTING); %>
<%@ include file = "../../../main/checkuser.jsp" %>

 <%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

public static final String textListGlobal[][] = {
	{"Pembiayaan","Pencarian","Daftar","Edit","Tidak ada data pembiayaan"},
	{"Costing","Search","List","Edit","No costing data available"}
};

/* this constant used to list text of listHeader */
public static final String textListHeader[][] = 
{
	{"Kode","Lokasi Asal","Lokasi Tujuan","Tanggal","Status","Urut Berdasar","Semua Tanggal","Dari"," s/d "},
	{"Code","Dispatch From","Dispatch To","Date","Status","Sort By","All Date","From"," to "}	
};

public static final String textSortBy[][] = 
{
	{"Kode","Tanggal","Lokasi Asal","Lokasi Tujuan","Status"},
	{"Code","Date","Dispatch To","Dispatch From","Status"}	
};


/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] = 
{
    {"No","Kode","Tanggal","Lokasi Asal","Lokasi Tujuan","Status"},
    {"No","Code","Date","Dispatch From","Dispatch To","Status"}
};



public String drawList(int language,Vector objectClass,int start,int docType,I_DocStatus i_status)
{
	String result = "";
	if(objectClass!=null && objectClass.size()>0)
	{
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		ctrlist.addHeader(textListMaterialHeader[language][0],"3%");
		ctrlist.addHeader(textListMaterialHeader[language][1],"15%");
		ctrlist.addHeader(textListMaterialHeader[language][2],"10%");
		ctrlist.addHeader(textListMaterialHeader[language][3],"20%");
		ctrlist.addHeader(textListMaterialHeader[language][4],"20%");
		ctrlist.addHeader(textListMaterialHeader[language][5],"10%");
	
		ctrlist.setLinkRow(1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();
		if(start<0){
			start = 0;		
		}	
		for(int i=0; i<objectClass.size(); i++)
		{
			Vector rowx = new Vector();				
			Vector vt = (Vector)objectClass.get(i);
			MatCosting df = (MatCosting)vt.get(0);
			Location loc1 = (Location)vt.get(1);
			Location loc2 = new Location();
			try
			{
				loc2 = PstLocation.fetchExc(df.getCostingTo());
			}
			catch(Exception e)
			{
			}
			start += 1;
			
			rowx.add(""+start);
			rowx.add(df.getCostingCode());
			rowx.add(Formater.formatDate(df.getCostingDate(), "dd-MM-yyyy"));
			rowx.add(loc1.getName());			
			rowx.add(loc2.getName());			
			rowx.add(i_status.fieldDocumentStatus[df.getCostingStatus()]); //getDocStatusName(docType,df.getCostingStatus()));
	
			lstData.add(rowx);
			lstLinkData.add(String.valueOf(df.getOID()));
		}
		result = ctrlist.draw();
	}
	else
	{
		result = "<div class=\"msginfo\">&nbsp;&nbsp;"+textListGlobal[language][4]+"</div>";		
	}
	return result;
}



public String getJspTitle(int index, int language, String prefiks, boolean addBody)
{
	String result = "";
	if(addBody)
	{
		result = textListHeader[language][index];		
	}
	else
	{
		result = textListHeader[language][index];
	} 
	return result;
}

public String getJspTitle2(int index, int language, String prefiks, boolean addBody)
{
	String result = "";
	if(addBody)
	{
		result = textSortBy[language][index];		
	}
	else
	{
		result = textSortBy[language][index];
	} 
	return result;
}



public boolean getTruedFalse(Vector vect, int index)
{
	for(int i=0;i<vect.size();i++)
	{
		int iStatus = Integer.parseInt((String)vect.get(i));
		if(iStatus==index)
			return true;
	}
	return false;
}
%>


<!-- Jsp Block -->
<%
/**
* get approval status for create document 
*/
I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();
I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();
int systemName = I_DocType.SYSTEM_MATERIAL;
int docType = i_pstDocType.composeDocumentType(systemName,I_DocType.MAT_DOC_TYPE_COS);
boolean privManageData = true;%>


<%


String dfTitle = textListGlobal[SESS_LANGUAGE][0];//"Costing Barang"; //i_pstDocType.getDocTitle(docType);

/**
* get request data from current form
*/
long oidMatCosting = FRMQueryString.requestLong(request, "hidden_costing_id");

/**
* initialitation some variable
*/
int iErrCode = FRMMessage.ERR_NONE;
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
CtrlMatCosting ctrlMatCosting = new CtrlMatCosting(request);
SrcMatCosting srcMatCosting = new SrcMatCosting();
SessMatCosting sessMatCosting = new SessMatCosting();
FrmSrcMatCosting frmSrcMatCosting = new FrmSrcMatCosting(request, srcMatCosting);




/**
* get data from 'hidden form'
*/


/**
* declaration of some identifier
*/
String dfCode = i_pstDocType.getDocCode(docType);

String dfItemTitle = dfTitle + " Item";

/**
* ControlLine 
*/



//SrcMatCosting srcMatDispatch = new SrcMatCosting();


/*
try
{
	srcMatDispatch = (SrcMatCosting)session.getValue(SessMatCosting.SESS_SRC_MATDISPATCH);
}
catch(Exception e)
{
	srcMatDispatch = new SrcMatCosting();
}


if(srcMatDispatch==null)
{
	srcMatDispatch = new SrcMatCosting();
}

try
{
	session.removeValue(SessMatCosting.SESS_SRC_MATDISPATCH);
}
catch(Exception e)
{
}*/

boolean oidDate = FRMQueryString.requestBoolean(request, FrmSrcMatCosting.fieldNames[FrmSrcMatCosting.FRM_FIELD_IGNORE_DATE] ); 
long oidLoc = FRMQueryString.requestLong(request,frmSrcMatCosting.fieldNames[FrmSrcMatCosting.FRM_FIELD_COSTING_FROM]  );
long oidLocFrom = FRMQueryString.requestLong(request,frmSrcMatCosting.fieldNames[FrmSrcMatCosting.FRM_FIELD_COSTING_TO] );

String oidCode = FRMQueryString.requestString(request, frmSrcMatCosting.fieldNames[FrmSrcMatCosting.FRM_FIELD_COSTING_CODE] );
int oidSortBy = FRMQueryString.requestInt(request, frmSrcMatCosting.fieldNames[frmSrcMatCosting.FRM_FIELD_SORT_BY]);
int status = FRMQueryString.requestInt(request, frmSrcMatCosting.fieldNames[frmSrcMatCosting.FRM_FIELD_STATUS]);
             
if (oidDate == true || oidLoc != 0 || oidLocFrom !=0 || oidSortBy !=0 || oidCode !="" || status >=0 ){  
 
         frmSrcMatCosting.requestEntityObject(srcMatCosting);
	 session.putValue(SessMatCosting.SESS_SRC_MATDISPATCH, srcMatCosting);
}else{
    srcMatCosting.setIgnoreDate(false);
}

String whereClausex = PstDataCustom.whereLocReportView(userId, "user_create_document_location");

vectSize = sessMatCosting.getCountMatCosting(srcMatCosting,whereClausex);
if(iCommand==Command.FIRST || iCommand==Command.NEXT || iCommand==Command.PREV || iCommand==Command.LAST || iCommand==Command.LIST)
{
	start = ctrlMatCosting.actionList(iCommand,start,vectSize,recordToGet);
}
//System.out.println("start >>>>> : "+start);
Vector records = sessMatCosting.listMatCosting(srcMatCosting,start,recordToGet,whereClausex);

%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">


function cmdSearch(){
	document.frm_matdispatch.command.value="<%=Command.LIST%>";
	document.frm_matdispatch.action="srccosting_material.jsp";
	document.frm_matdispatch.submit();
}

function cmdAdd()
{
	document.frm_matdispatch.start.value=0;
	document.frm_matdispatch.approval_command.value="<%=Command.SAVE%>";			
	document.frm_matdispatch.command.value="<%=Command.ADD%>";
	document.frm_matdispatch.add_type.value="<%=ADD_TYPE_LIST%>";			
	document.frm_matdispatch.action="costing_material_edit.jsp";
	if(compareDateForAdd()==true)
	document.frm_matdispatch.submit();
}

function cmdEdit(oid)
{
	document.frm_matdispatch.hidden_costing_id.value=oid;
	document.frm_matdispatch.start.value=0;
	document.frm_matdispatch.approval_command.value="<%=Command.APPROVE%>";					
	document.frm_matdispatch.command.value="<%=Command.EDIT%>";
	document.frm_matdispatch.action="costing_material_edit.jsp";
	document.frm_matdispatch.submit();
}

function cmdListFirst()
{
	document.frm_matdispatch.command.value="<%=Command.FIRST%>";
	document.frm_matdispatch.action="srccosting_material.jsp";
	document.frm_matdispatch.submit();
}

function cmdListPrev()
{
	document.frm_matdispatch.command.value="<%=Command.PREV%>";
	document.frm_matdispatch.action="srccosting_material.jsp";
	document.frm_matdispatch.submit();
}

function cmdListNext()
{
	document.frm_matdispatch.command.value="<%=Command.NEXT%>";
	document.frm_matdispatch.action="srccosting_material.jsp";
	document.frm_matdispatch.submit();
}

function cmdListLast()
{
	document.frm_matdispatch.command.value="<%=Command.LAST%>";
	document.frm_matdispatch.action="srccosting_material.jsp";
	document.frm_matdispatch.submit();
}

function cmdBack()
{
	document.frm_matdispatch.command.value="<%=Command.BACK%>";
	document.frm_matdispatch.action="srccosting_material.jsp";
	document.frm_matdispatch.submit();
}

<!--
//-------------- script control line -------------------
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
            <%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][1]%><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frm_matdispatch" method="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="add_type" value="">			
              <input type="hidden" name="approval_command">			    			  			  			  			  			  
              <table width="100%" border="0">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="hidden_costing_id" value="<%=oidMatCosting%>">
          	
                <tr>
                  <td valign="top" colspan="2">
                    <hr size="1">
                  </td>
                </tr>
				<tr> 
                  <td colspan="2"> 
                    <table width="100%" border="0" cellspacing="1" cellpadding="1">
                      <tr> 
                        <td height="21" width="16%"><%=getJspTitle(0,SESS_LANGUAGE,dfCode,true)%></td>
                        <td height="21" width="1%">:</td>
                        <td height="21" width="83%">&nbsp; 
                          <input type="text" name="<%=frmSrcMatCosting.fieldNames[FrmSrcMatCosting.FRM_FIELD_COSTING_CODE] %>"  value="<%= srcMatCosting.getCostingCode()%>" class="formElemen" size="20">
                        </td>
                      </tr>
                      <tr> 
                        <td height="21" width="16%"><%=getJspTitle(1,SESS_LANGUAGE,dfCode,true)%></td>
                        <td height="21" width="1%">:</td>
                        <td height="21" width="83%">&nbsp; 
                          <% 
							Vector obj_locationid = new Vector(1,1); 
							Vector val_locationid = new Vector(1,1); 
							Vector key_locationid = new Vector(1,1); 
							//Vector vt_loc = PstLocation.list(0,0,PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE, PstLocation.fieldNames[PstLocation.FLD_CODE]);
							//Vector vt_loc = PstLocation.list(0,0,"", PstLocation.fieldNames[PstLocation.FLD_NAME]);
                                                        //add opie-eyek
                                                        //algoritma : di check di sistem usernya dimana saja user tsb bisa melakukan create document
                                                         whereClause = " ("+PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE +
                                                                               " OR "+PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE +")";

                                                        whereClause += " AND "+PstDataCustom.whereLocReportView(userId, "user_create_document_location");

                                                        Vector vt_loc = PstLocation.listLocationCreateDocument(0, 0, whereClause, "");

							val_locationid.add("0");
							key_locationid.add("All");
							for(int d=0;d<vt_loc.size();d++) {
								Location loc = (Location)vt_loc.get(d);
								val_locationid.add(""+loc.getOID()+"");
								key_locationid.add(loc.getName());
							}
						  %>
                          <%=ControlCombo.draw(frmSrcMatCosting.fieldNames[FrmSrcMatCosting.FRM_FIELD_COSTING_FROM], null, ""+oidLoc, val_locationid, key_locationid, "", "formElemen")%>
                        </td>
                      </tr>
                      <tr> 
                        <td height="21" width="16%"><%=getJspTitle(2,SESS_LANGUAGE,dfCode,true)%></td>
                        <td height="21" width="1%">:</td>
                        <td height="21" width="83%">&nbsp; 
                          <% 
							Vector obj_locationid1 = new Vector(1,1); 
							Vector val_locationid1 = new Vector(1,1); 
							Vector key_locationid1 = new Vector(1,1); 
							String locWhClause = "";//PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE;
							String locOrderBy = String.valueOf(PstLocation.fieldNames[PstLocation.FLD_CODE]);														
							//Vector vt_loc1 = PstLocation.list(0,0,locWhClause,locOrderBy);
							val_locationid1.add("0");
							key_locationid1.add("All");
							for(int d=0;d<vt_loc.size();d++) {
								Location loc1 = (Location)vt_loc.get(d);
								val_locationid1.add(""+loc1.getOID()+"");
								key_locationid1.add(loc1.getName());
							}
						  %>
                          <%=ControlCombo.draw(frmSrcMatCosting.fieldNames[FrmSrcMatCosting.FRM_FIELD_COSTING_TO], null, ""+oidLocFrom, val_locationid1, key_locationid1, "", "formElemen")%> 
                        </td>
                      </tr>
                      <tr> 
                        <td height="43" rowspan="2" valign="top" width="16%" align="left"><%=getJspTitle(3,SESS_LANGUAGE,dfCode,true)%></td>
                        <td height="43" rowspan="2" valign="top" width="1%" align="left">:</td>
                        <td height="21" width="83%" valign="top" align="left">&nbsp;
						  <input type="radio" name="<%=FrmSrcMatCosting.fieldNames[FrmSrcMatCosting.FRM_FIELD_IGNORE_DATE]%>" <%if(oidDate == false){%>checked<%}%> value="0">
                          <%=textListHeader[SESS_LANGUAGE][6]%>
						</td>
                      </tr>
                      <tr align="left"> 
                        <td height="21" width="83%" valign="top">&nbsp;  
						  <input type="radio" name="<%=FrmSrcMatCosting.fieldNames[FrmSrcMatCosting.FRM_FIELD_IGNORE_DATE]%>" <%if(oidDate){%>checked<%}%> value="1">
						  <%=textListHeader[SESS_LANGUAGE][7]%>
						  <%=ControlDate.drawDateWithStyle(FrmSrcMatCosting.fieldNames[FrmSrcMatCosting.FRM_FIELD_COSTING_DATE_FROM], (srcMatCosting.getCostingDateFrom()==null) ? new Date() : srcMatCosting.getCostingDateFrom(), 1, -5, "formElemen", "")%>
						  <%=textListHeader[SESS_LANGUAGE][8]%>
						  <%=ControlDate.drawDateWithStyle(FrmSrcMatCosting.fieldNames[FrmSrcMatCosting.FRM_FIELD_COSTING_DATE_TO], (srcMatCosting.getCostingDateTo()==null) ? new Date() : srcMatCosting.getCostingDateTo(), 1, -5, "formElemen", "")%>
                        </td>
                      </tr>
                      <tr> 
                        <td height="21" valign="top" width="16%" align="left"><%=getJspTitle(4,SESS_LANGUAGE,dfCode,true)%></td>
                        <td height="21" valign="top" width="1%" align="left">:</td>
                        <td height="21" width="83%" valign="top" align="left"> 
                          &nbsp; 
                          <%
                              Vector val_status = new Vector(1,1); //hidden values that will be deliver on request (oids) 
                              Vector key_status = new Vector(1,1); //texts that displayed on combo box
                              val_status.add("-1");
                              key_status.add("All");

                              Vector vectResult = i_status.getDocStatusFor(docType);
                              val_status.add(""+I_DocStatus.DOCUMENT_STATUS_DRAFT);
                              key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]);
                              
                              val_status.add(""+I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED);
                              key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED]);
                              
                              val_status.add(""+I_DocStatus.DOCUMENT_STATUS_FINAL);
                              key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);
                              
                              val_status.add(""+I_DocStatus.DOCUMENT_STATUS_POSTED);
                              key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_POSTED]);

                              val_status.add(""+I_DocStatus.DOCUMENT_STATUS_CLOSED);
                              key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_CLOSED]);

                              

                              String select_status = ""+srcMatCosting.getStatus(); //selected on combo box*/
						  %>
                          <%=ControlCombo.draw(FrmSrcMatCosting.fieldNames[FrmSrcMatCosting.FRM_FIELD_STATUS], null, select_status, val_status, key_status, "", "formElemen")%><%=frmSrcMatCosting.getErrorMsg(FrmSrcMatCosting.FRM_FIELD_STATUS)%>
                        </td>
                      </tr>
                      <tr> 
                        <td height="19" valign="top" width="16%" align="left"><%=getJspTitle(5,SESS_LANGUAGE,dfCode,false)%></td>
                        <td height="19" valign="top" width="1%" align="left">:</td>
                        <td height="19" width="83%" valign="top" align="left">&nbsp; 
                          <% 
							Vector key_sort = new Vector(1,1); 						  
							Vector val_sort = new Vector(1,1); 
							for(int i=0; i<textSortBy[0].length; i++)
							{ 
								key_sort.add(""+i);							
								val_sort.add(""+getJspTitle2(i,SESS_LANGUAGE,dfCode,true)); 
							}
							String select_sort = ""+srcMatCosting.getSortBy();      
							out.println(ControlCombo.draw(frmSrcMatCosting.fieldNames[frmSrcMatCosting.FRM_FIELD_SORT_BY], null, select_sort,key_sort,val_sort,"","formElemen"));
						  %>
                        </td>
                      </tr>
                      <tr> 
                        <td height="21" valign="top" width="16%" align="left">&nbsp;</td>
                        <td height="21" valign="top" width="1%" align="left">&nbsp;</td>
                        <td height="21" width="83%" valign="top" align="left">&nbsp;</td>
                      </tr>
                      <tr> 
                        <td height="21" valign="top" width="16%" align="left">&nbsp;</td>
                        <td height="21" valign="top" width="1%" align="left">&nbsp;</td>
                        <td height="21" width="83%" valign="top" align="left"> 
                          <table width="57%" border="0" cellspacing="0" cellpadding="0">
                            <tr> 
                              <td class="command" nowrap width="44%"><a class="btn-primary btn-lg" href="javascript:cmdSearch()"><i class="fa fa-search"> &nbsp;<%=ctrLine.getCommand(SESS_LANGUAGE,dfTitle,ctrLine.CMD_SEARCH,true)%></i></a></td>
                              <%if(privAdd){%>		
                                <td class="command" nowrap width="42%"><a class="btn-primary btn-lg" href="javascript:cmdAdd()"><i class="fa fa-plus-circle"> &nbsp; <%=ctrLine.getCommand(SESS_LANGUAGE,dfTitle,ctrLine.CMD_ADD,true)%></i></a></td>
                                <td nowrap width="0%">&nbsp;</td>
                              <%}%>
                            </tr>
                          </table>
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>
              </table>
            </form>
            <!-- #EndEditable --></td> 
        </tr> 
        
        
     <%if(iCommand==Command.LIST || iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand == Command.LAST){%>        
        <table width="100%" cellspacing="0" cellpadding="3">
			    <tr>
                  <td valign="top" colspan="3">
                    <hr size="1">
                  </td>
                </tr>
			  <tr align="left" valign="top"> 
				<td height="22" valign="middle" colspan="3"><%=drawList(SESS_LANGUAGE,records,start,docType,i_status)%></td>
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
			  <tr align="left" valign="top"> 
				<td height="18" valign="top" colspan="3"> 					                             
				    <table width="52%" border="0" cellspacing="0" cellpadding="0">
                                        <tr> 
                                            <td height="25" valign="top" width="9%" align="left">&nbsp;</td>
                                        </tr>
                                        <tr>                               
					  <%if(privAdd){%>	
					    <td class="command" nowrap width="42%"><a class="btn-primary btn-lg" href="javascript:cmdAdd()"><i class="fa fa-plus-circle"> &nbsp; <%=ctrLine.getCommand(SESS_LANGUAGE,dfTitle,ctrLine.CMD_ADD,true)%></i></a></td>
					    <td nowrap width="0%">&nbsp;</td>
					  <%}%>
					</tr>
				  </table>                        						  
				</td>
			  </tr>					  
			</table>
          <%}%>
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

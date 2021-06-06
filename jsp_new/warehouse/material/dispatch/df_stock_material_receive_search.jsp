<%-- 

    Document   : df_stock_material_receive_search

    Created on : Jun 8, 2010, 4:48:47 PM

    Author     : Dimata

--%>

<%@ page language = "java" %>

<!-- package java -->

<%@ page import = "java.util.*,

                   com.dimata.common.entity.location.PstLocation,

                   com.dimata.common.entity.location.Location,

                   com.dimata.posbo.entity.search.SrcMatReceive,

                   com.dimata.posbo.form.search.FrmSrcMatReceive,

                   com.dimata.posbo.session.warehouse.SessMatReceive,

                   com.dimata.posbo.entity.warehouse.PstMatReceive,

                   com.dimata.posbo.entity.warehouse.PstMatDispatch,

                   com.dimata.posbo.entity.warehouse.MatDispatch" %>

<!-- package dimata -->

<%@ page import = "com.dimata.util.*" %>

<!-- package qdep -->

<%@ page import = "com.dimata.gui.jsp.*" %>

<%@ page import = "com.dimata.qdep.entity.*" %>

<%@ page import = "com.dimata.qdep.form.*" %>

<!--package material -->

<%@ page import = "com.dimata.common.entity.location.*" %>

<%@ include file = "../../../main/javainit.jsp" %>

<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE); %>

<%@ include file = "../../../main/checkuser.jsp" %>



<%!

public static final int ADD_TYPE_SEARCH = 0;

public static final int ADD_TYPE_LIST = 1;



public static final String textListGlobal[][] = {

    {"Tambah Item","Dari Penerimaan","Ke Transfer","Pencarian","Penerimaan","Proses transfer tidak dapat dilakukan pada lokasi yang sama"},

    {"Add Item", "From Receive","For Transfer","Search","Receive","There Transfer cant'be proceed in same location"}

};

/*public static final String textListGlobal[][] = {

	{"Penerimaan","Dari Pembelian","Pencarian","Daftar","Edit","Dengan PO","Tanpa PO"},

	{"Receive","From Purchase","Search","List","Edit","With PO","Without PO"}

};*/



/* this constant used to list text of listHeader */

public static final String textListOrderHeader[][] =

{

    {"Nomor","Lokasi Asal","Lokasi Tujuan","Tanggal","Status","Keterangan","Nota Supplier","Waktu"},

    {"Number","From Location","Destination","Date","Status","Remark","Supplier Invoice","Time"}

};



/* this constant used to list text of listHeader */

public static final String textListHeader[][] = {

	{"Nomor","Supplier","Invoice Supplier","Lokasi Terima","Tanggal","Dari"," s/d ","Status","Urut Berdasar","Semua"},

	{"Number","Supplier","Supplier Invoice","Receive Location","Date","From"," to ","Status","Sort By","All"}

};



public String getJspTitle(int index, int language, String prefiks, boolean addBody) {

	String result = "";

	if(addBody) {

		if(language==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT){

			result = textListHeader[language][index] + " " + prefiks;

		}

		else {

			result = prefiks + " " + textListHeader[language][index];

		}

	}

	else {

		result = textListHeader[language][index];

	}

	return result;

}



public boolean getTruedFalse(Vector vect, int index) {

	for(int i=0;i<vect.size();i++) {

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

I_Approval i_approval = (I_Approval) Class.forName(approvalClassName).newInstance();

I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();

int systemName = I_DocType.SYSTEM_MATERIAL;

int docType = i_pstDocType.composeDocumentType(systemName,I_DocType.MAT_DOC_TYPE_LMRR);

boolean privManageData = true;

%>





<%

/**

* get data from 'hidden form'

*/

int iCommand = FRMQueryString.requestCommand(request);



/**

* declaration of some identifier

*/

String recCode = i_pstDocType.getDocCode(docType);

String recTitle = "Terima Barang"; // i_pstDocType.getDocTitle(docType);

String recItemTitle = recTitle + " Item";

long oidDispatchMaterial = FRMQueryString.requestLong(request,"hidden_dispatch_id");

MatDispatch df =new MatDispatch();

if(oidDispatchMaterial !=0){

try{

  df = PstMatDispatch.fetchExc(oidDispatchMaterial);

}

  catch(Exception e){

  System.out.println(e);

}

}

else{

System.out.println("Tidak ada data");

}





/**

* ControlLine

*/

ControlLine ctrLine = new ControlLine();



SrcMatReceive srcMatReceive = new SrcMatReceive();

FrmSrcMatReceive frmSrcMatReceive = new FrmSrcMatReceive();

try {

	srcMatReceive = (SrcMatReceive)session.getValue(SessMatReceive.SESS_SRC_MATRECEIVE);

}

catch(Exception e) {

	srcMatReceive = new SrcMatReceive();

}





if(srcMatReceive==null) {

	srcMatReceive = new SrcMatReceive();

}



try {

	session.removeValue(SessMatReceive.SESS_SRC_MATRECEIVE);

}

catch(Exception e){

	System.out.println("Exc. when remove session SESS_SRC_MATRECEIVE");

}



try {

	session.removeValue("BARCODE_RECEIVE");

}

catch(Exception e){

	System.out.println("Exc. when remove session BARCODE_RECEIVE");

}



%>

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->

<head>

<!-- #BeginEditable "doctitle" -->

<title>Dimata - ProChain POS</title>

<script language="JavaScript">

<!--

function cmdSearch(){

	document.frmsrcmatreceive.command.value="<%=Command.LIST%>";

	document.frmsrcmatreceive.action="df_stock_material_receive_transfer_list.jsp";

	document.frmsrcmatreceive.submit();

}



function fnTrapKD(){

   if (event.keyCode == 13) {

		document.all.aSearch.focus();

		cmdSearch();

   }

}



function cmdBack(){

	document.frmsrcmatreceive.command.value="<%=Command.EDIT%>";

	document.frmsrcmatreceive.action="df_stock_wh_material_edit.jsp";

	document.frmsrcmatreceive.submit();

}



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

<link rel="stylesheet" href="../../../styles/main.css" type="text/css">

<!-- #EndEditable -->

<!-- #BeginEditable "stylestab" -->

<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
<%if(menuUsed == MENU_ICON){%>
    <link href="../../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
<%}%>
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
<script language="JavaScript">

                window.focus();

        </script>
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

            &nbsp;<%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][1]%> &gt; <%=textListGlobal[SESS_LANGUAGE][2]%><!-- #EndEditable --></td>

        </tr>

        <tr>

          <td><!-- #BeginEditable "content" -->

            <form name="frmsrcmatreceive" method="post" action="">

              <input type="hidden" name="command" value="<%=iCommand%>">

              <input type="hidden" name="add_type" value="">

              <input type="hidden" name="approval_command">

              <input type="hidden" name="hidden_dispatch_id" value="<%=oidDispatchMaterial%>">

              <input type="hidden" name="<%=frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_LOCATION_TYPE]%>" value="<%=PstLocation.TYPE_LOCATION_WAREHOUSE%>">

              <input type="hidden" name="<%=frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_RECEIVE_SOURCE]%>" value="<%=PstMatReceive.SOURCE_FROM_SUPPLIER%>">

              <table width="100%" border="0">

		<tr>

                  <td colspan="3">

                    <table width="100%" border="0" cellspacing="1" cellpadding="1">

                      <tr>

                        <td width="8%"><%=textListOrderHeader[SESS_LANGUAGE][0]%></td>

                        <td width="25%">:

                           <%=df.getDispatchCode()%>

                        </td>

                        <td width="9%"><%=textListOrderHeader[SESS_LANGUAGE][1]%></td>

                        <td width="29%">:

                            <%

                             Location srcLoc = new Location();

                             try{

                                 srcLoc = PstLocation.fetchExc(df.getLocationId());





                                 }catch(Exception exc){

                                     srcLoc=new Location();

                                }

                            %>

                           <%=srcLoc.getName()%>



                        </td>

                        <td width="8%"><%=textListOrderHeader[SESS_LANGUAGE][5]%></td>

						  <td width="21%">:

                          <%=df.getRemark()%>

                      </tr>

                        <tr>

                           <td width ="8%"><%=textListOrderHeader[SESS_LANGUAGE][3]%></td>

                           <td width="25%">:

                             <%=(Formater.formatDate(df.getDispatchDate(), "dd-MM-yyyy"))%>

                          </td>

                       <td width="8%"><%=textListOrderHeader[SESS_LANGUAGE][2]%></td>

                           <td width="25%">:

                            <%

                            srcLoc = PstLocation.fetchExc(df.getDispatchTo());

                            %>

                            <%=srcLoc.getName()%>

                        </td>

                       <tr>

                           <td width ="8%"><%=textListOrderHeader[SESS_LANGUAGE][7]%></td>

                           <td width="25%">:

                               <%=(Formater.formatDate(df.getDispatchDate(), "HH:mm"))%>

                           <td width ="8%"><%=textListOrderHeader[SESS_LANGUAGE][4]%></td>

                           <td width="25%">:

                               <%if(df.getDispatchStatus()==I_DocStatus.DOCUMENT_STATUS_DRAFT){

                                    out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]);

                               } else if(df.getDispatchStatus()==I_DocStatus.DOCUMENT_STATUS_FINAL){

                                    out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);

                               }else if(df.getDispatchStatus()==I_DocStatus.DOCUMENT_STATUS_CLOSED){

                                    out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_CLOSED]);

                                }else if(df.getDispatchStatus()==I_DocStatus.DOCUMENT_STATUS_POSTED)

                                    out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_POSTED]);%>

                       </tr>

                       

                    </table>



                       <table width="100%" border="0" cellspacing="0" cellpadding="0">

                       <tr>

                           <hr size="1">

                           &nbsp;

                           <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->

                            <%=textListGlobal[SESS_LANGUAGE][3]%> &gt; <%=textListGlobal[SESS_LANGUAGE][4]%><!-- #EndEditable --></td>

                       </tr>

                       </table>

                       &nbsp;

                       &nbsp;

                        <table width="100%" border="0" cellspacing="0" cellpadding="0">

                      <tr>

                        <td height="21" width="12%"><%=getJspTitle(0,SESS_LANGUAGE,recCode,false)%></td>

                        <td height="21" width="1%">:</td>

                        <td height="21" width="87%">&nbsp;

                          <input tabindex="1" type="text" name="<%=frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_RECEIVENUMBER] %>"  value="<%= srcMatReceive.getReceivenumber() %>" class="formElemen" size="20" onKeyDown="javascript:fnTrapKD()">

                        </td>

                      </tr>

                      <tr>

                        <td height="21" width="12%"><%=getJspTitle(1,SESS_LANGUAGE,recCode,false)%></td>

                        <td height="21" width="1%">:</td>

                        <td height="21" width="87%">&nbsp;

                          <input type="text" name="<%=frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_VENDORNAME] %>"  value="<%= srcMatReceive.getVendorname() %>" class="formElemen" size="30" onKeyDown="javascript:fnTrapKD()">

                        </td>

                      </tr>

                      <tr>

                        <td height="21" width="12%"><%=getJspTitle(2,SESS_LANGUAGE,recCode,false)%></td>

                        <td height="21" width="1%">:</td>

                        <td height="21" width="87%">&nbsp;

                          <input type="text" name="<%=frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_INVOICE_SUPPLIER] %>"  value="<%= srcMatReceive.getInvoiceSupplier() %>" class="formElemen" size="20" onKeyDown="javascript:fnTrapKD()">

                        </td>

                      </tr>

                      <tr>

                        <td height="21" width="12%"><%=getJspTitle(3,SESS_LANGUAGE,recCode,false)%></td>

                        <td height="21" width="1%">:</td>

                        <td height="21" width="87%">&nbsp;

                          <%

							Vector obj_locationid = new Vector(1,1);

							Vector val_locationid = new Vector(1,1);

							Vector key_locationid = new Vector(1,1);

							String whereClause = PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE;

							whereClause += " OR " + PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE;

							Vector vt_loc = PstLocation.list(0, 0, whereClause, PstLocation.fieldNames[PstLocation.FLD_CODE]);

							val_locationid.add(String.valueOf(0));

							key_locationid.add(getJspTitle(9,SESS_LANGUAGE,recCode,false)+" "+getJspTitle(3,SESS_LANGUAGE,recCode,false));

							for(int d=0; d<vt_loc.size(); d++) {

								Location loc = (Location)vt_loc.get(d);

								val_locationid.add(""+loc.getOID()+"");

								key_locationid.add(loc.getName());

							}

						  %>

                          <%=ControlCombo.draw(frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_LOCATION_ID], null, "", val_locationid, key_locationid, " onKeyDown=\"javascript:fnTrapKD()\"", "formElemen")%>

                        </td>

                      </tr>

                      <tr>

                        <td height="43" rowspan="2" valign="top" width="12%" align="left"><%=getJspTitle(4,SESS_LANGUAGE,recCode,false)%></td>

                        <td height="43" rowspan="2" valign="top" width="1%" align="left">:</td>

                        <td height="21" width="87%" valign="top" align="left">

                          <input type="radio" name="<%=frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_RECEIVEDATESTATUS] %>" <%if(srcMatReceive.getReceivedatestatus()==0){%>checked<%}%> value="0" onKeyDown="javascript:fnTrapKD()">

                          <%=getJspTitle(9,SESS_LANGUAGE,recCode,false)%>&nbsp;<%=getJspTitle(4,SESS_LANGUAGE,recCode,false)%></td>

                      </tr>

                      <tr align="left">

                        <td height="21" width="87%" valign="top">

                          <input type="radio" name="<%=frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_RECEIVEDATESTATUS] %>" <%if(srcMatReceive.getReceivedatestatus()==1){%>checked<%}%> value="1" onKeyDown="javascript:fnTrapKD()">

                          <%=getJspTitle(5,SESS_LANGUAGE,recCode,false)%> <%=ControlDate.drawDate(frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_RECEIVEFROMDATE], srcMatReceive.getReceivefromdate(),"formElemen",1,-5)%> <%=getJspTitle(6,SESS_LANGUAGE,recCode,false)%> <%=ControlDate.drawDate(frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_RECEIVETODATE], srcMatReceive.getReceivetodate(),"formElemen",1,-5) %> </td>

                      </tr>

                      <tr>

                        <td height="21" valign="top" width="12%" align="left"><%=getJspTitle(7,SESS_LANGUAGE,recCode,false)%></td>

                        <td height="21" valign="top" width="1%" align="left">:</td>

                        <td height="21" width="87%" valign="top" align="left">

                          <%

						  Vector vectResult = i_status.getDocStatusFor(docType);

						  for(int i=0; i<vectResult.size(); i++)

						  {

						  	if ((i == I_DocStatus.DOCUMENT_STATUS_DRAFT) || (i == I_DocStatus.DOCUMENT_STATUS_POSTED) || (i == I_DocStatus.DOCUMENT_STATUS_CLOSED) || (i == I_DocStatus.DOCUMENT_STATUS_FINAL))

							{

								Vector vetTemp = (Vector)vectResult.get(i);

								int indexPrStatus = Integer.parseInt(String.valueOf(vetTemp.get(0)));

								String strPrStatus = String.valueOf(vetTemp.get(1));

							  %>

							  <input type="checkbox" class="formElemen" name="<%=frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_RECEIVESTATUS]%>" value="<%=(indexPrStatus)%>" <%if(getTruedFalse(srcMatReceive.getReceivestatus(),indexPrStatus)){%>checked<%}%> onKeyDown="javascript:fnTrapKD()">

							  <%=strPrStatus%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

							  <%

							}

						  }

						  %>

                        </td>

                      </tr>

                      <tr>

                        <td height="19" valign="top" width="12%" align="left"><%=getJspTitle(8,SESS_LANGUAGE,recCode,false)%></td>

                        <td height="19" valign="top" width="1%" align="left">:</td>

                        <td height="19" width="87%" valign="top" align="left">

                          <%

							Vector key_sort = new Vector(1,1);

							Vector val_sort = new Vector(1,1);

							int maxItem = textListHeader[0].length-1;

							for(int i=0; i<maxItem; i++){

								key_sort.add(""+i);

								val_sort.add(""+getJspTitle(i,SESS_LANGUAGE,recCode,false));

							}

							String select_sort = ""+srcMatReceive.getReceivesortby();

							out.println("&nbsp;"+ControlCombo.draw(frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_RECEIVESORTBY], null, select_sort,key_sort,val_sort," onKeyDown=\"javascript:fnTrapKD()\"","formElemen"));

						  %>

                        </td>

                      </tr>

                      <tr>

                        <td height="21" valign="top" width="12%" align="left">&nbsp;</td>

                        <td height="21" valign="top" width="1%" align="left">&nbsp;</td>

                        <td height="21" width="87%" valign="top" align="left">&nbsp;</td>

                      </tr>

                      <tr>

                        <td height="21" valign="top" width="12%" align="left">&nbsp;</td>

                        <td height="21" valign="top" width="1%" align="left">&nbsp;</td>

                        <td height="21" width="87%" valign="top" align="left">

                          <table width="80%" border="0" cellspacing="0" cellpadding="0">

                            <tr>

                              <td nowrap width="6%"><a href="javascript:cmdSearch()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnSearchOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][4],ctrLine.CMD_SEARCH,true)%>"></a></td>

                              <td class="command" nowrap width="20%"><a href="javascript:cmdSearch()"><%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][4],ctrLine.CMD_SEARCH,true)%></a></td>

                              <td nowrap width="6%"><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnBackOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][4],ctrLine.CMD_BACK,true)%>"></a></td>

                              <td class="command" nowrap width="92%"><a href="javascript:cmdBack()"><%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][4],ctrLine.CMD_BACK,true)%></a></td>

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


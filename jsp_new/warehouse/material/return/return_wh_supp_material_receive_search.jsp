<%@ page language = "java" %>

<!-- package java -->

<%@ page import = "java.util.*,
                   com.dimata.common.entity.location.PstLocation,
                   com.dimata.common.entity.location.Location,
                   com.dimata.posbo.entity.search.SrcMatReceive,
                   com.dimata.posbo.form.search.FrmSrcMatReceive,
                   com.dimata.posbo.session.warehouse.SessMatReceive,
                   com.dimata.posbo.entity.warehouse.PstMatReceive,
                   com.dimata.posbo.entity.warehouse.PstMatReturn,
                   com.dimata.posbo.entity.warehouse.MatReturn,
                   com.dimata.posbo.form.warehouse.FrmMatReturn,
                   com.dimata.common.entity.contact.*,
                   com.dimata.common.entity.payment.PstCurrencyType,
                   com.dimata.common.entity.payment.CurrencyType" %>

<!-- package dimata -->

<%@ page import = "com.dimata.util.*" %>

<!-- package qdep -->

<%@ page import = "com.dimata.gui.jsp.*" %>

<%@ page import = "com.dimata.qdep.entity.*" %>

<%@ page import = "com.dimata.qdep.form.*" %>

<!--package material -->

<%@ page import = "com.dimata.common.entity.location.*" %>

<%@ include file = "../../../main/javainit.jsp" %>

<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RETURN, AppObjInfo.G2_SUPPLIER_RETURN, AppObjInfo.OBJ_SUPPLIER_RETURN); %>

<%@ include file = "../../../main/checkuser.jsp" %>



<%!

public static final int ADD_TYPE_SEARCH = 0;

public static final int ADD_TYPE_LIST = 1;



public static final String textListGlobal[][] = {

    {"Tambah Item","Dari Penerimaan","Ke Retur","Pencarian","Penerimaan"},

    {"Add Item", "From Receive","For Return","Search","Receive"}

};

/*public static final String textListGlobal[][] = {

	{"Penerimaan","Dari Pembelian","Pencarian","Daftar","Edit","Dengan PO","Tanpa PO"},

	{"Receive","From Purchase","Search","List","Edit","With PO","Without PO"}

};*/




/* this constant used to list text of listHeader */
public static final String textListOrderHeader[][] = {
	{"Nomor","Lokasi","Tanggal","Supplier","Status","Keterangan","Alasan","Waktu","Mata Uang"},
	{"No","Location","Date","Supplier","Status","Remark","Reason","Time","Currency"}
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

long oidReturnMaterial = FRMQueryString.requestLong(request,"hidden_return_id");

MatReturn ret =new MatReturn();

if(oidReturnMaterial !=0){

try{

  ret = PstMatReturn.fetchExc(oidReturnMaterial);

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

	document.frmsrcmatreceive.action="return_wh_supp_material_receive_list.jsp";

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

	document.frmsrcmatreceive.action="return_wh_supp_material_edit.jsp";

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

  <tr>

    <td height="25" ID="TOPTITLE"> <!-- #BeginEditable "header" -->

<script language="JavaScript">

	window.focus();

</script>

      <%@ include file = "../../../main/header.jsp" %>

      <!-- #EndEditable --></td>

  </tr>

  <tr>

    <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->

      <%@ include file = "../../../main/mnmain.jsp" %>

      <!-- #EndEditable --> </td>

  </tr>

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

              <input type="hidden" name="hidden_return_id" value="<%=oidReturnMaterial%>">

              <input type="hidden" name="<%=frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_LOCATION_TYPE]%>" value="<%=PstLocation.TYPE_LOCATION_WAREHOUSE%>">

              <input type="hidden" name="<%=frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_RECEIVE_SOURCE]%>" value="<%=PstMatReceive.SOURCE_FROM_SUPPLIER%>">

              <table width="100%" border="0">

		<tr>

                  <td colspan="3">

                    <table width="100%" border="0" cellspacing="1" cellpadding="1">

                      <tr>

                        <td width="10%"><%=textListOrderHeader[SESS_LANGUAGE][0]%></td>

                        <td width="25%">:

                         <b> <%=ret.getRetCode()%></b>

                        </td>

                        <td width="10%"><%=textListOrderHeader[SESS_LANGUAGE][3]%></td>

                        <td width="28%">:

                            <%

                             ContactList contactList = new ContactList();

                             try{

                                 contactList = PstContactList.fetchExc(ret.getSupplierId());
                                 }catch(Exception exc){

                                     contactList=new ContactList();

                                }

                            %>

                           <%=contactList.getCompName()%>
                        </td>

                         <td width="10%"><%=textListOrderHeader[SESS_LANGUAGE][6]%></td>
                        <td width="15%">:
                        <%=PstMatReturn.strReturnReasonList[SESS_LANGUAGE][ret.getReturnReason()]%>
                        </td>

                       
                      <tr>
                        <td width="10%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][1]%></td>

                        <td width="25%" align="left">:

                            <%

                             Location srcLoc = new Location();

                             try{

                                 srcLoc = PstLocation.fetchExc(ret.getLocationId());





                                 }catch(Exception exc){

                                     srcLoc=new Location();

                                }

                            %>

                           <%=srcLoc.getName()%>



                        </td>
                         <td width ="10%"><%=textListOrderHeader[SESS_LANGUAGE][4]%></td>
                         <td width ="28%">:

                               <%if(ret.getReturnStatus()==I_DocStatus.DOCUMENT_STATUS_DRAFT){

                                    out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]);

                               } else if(ret.getReturnStatus()==I_DocStatus.DOCUMENT_STATUS_FINAL){

                                    out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);

                               }else if(ret.getReturnStatus()==I_DocStatus.DOCUMENT_STATUS_CLOSED){

                                    out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_CLOSED]);

                                }else if(ret.getReturnStatus()==I_DocStatus.DOCUMENT_STATUS_POSTED)

                                    out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_POSTED]);%>
                           </td>

                        <td width="10%"><%=textListOrderHeader[SESS_LANGUAGE][5]%></td>

			<td width="15%">:

                          <%=ret.getRemark()%>
                                                  </td>

                      </tr>

                        <tr>

                           <td width ="10%"><%=textListOrderHeader[SESS_LANGUAGE][2]%></td>

                           <td width="25%">:

                             <%=(Formater.formatDate(ret.getReturnDate(),"dd-MM-yyyy"))%>

                          </td>
                          <td width ="10%"><%=textListOrderHeader[SESS_LANGUAGE][8]%></td>
                          <td width="28%">:

                             <% CurrencyType currencyType = new CurrencyType();
                               try{

                                 currencyType = PstCurrencyType.fetchExc(ret.getCurrencyId());


                                 }catch(Exception exc){

                                     currencyType = new CurrencyType();

                                }
                            %>
                            <%=currencyType.getCode()%>

                           </td>

                       <tr>

                           <td width ="10%%"><%=textListOrderHeader[SESS_LANGUAGE][7]%></td>

                           <td width="25%">:

                               <%=(Formater.formatDate(ret.getReturnDate(), "HH:mm"))%></td>
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

                      <!--<tr>

                        <td height="21" width="12%"><%=getJspTitle(1,SESS_LANGUAGE,recCode,false)%></td>

                        <td height="21" width="1%">:</td>

                        <td height="21" width="87%">&nbsp;

                          <input type="text" name="<%=frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_VENDORNAME] %>"  value="<%= srcMatReceive.getVendorname() %>" class="formElemen" size="30" onKeyDown="javascript:fnTrapKD()">

                        </td>

                      </tr>-->

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

      <%@ include file = "../../../main/footer.jsp" %>

      <!-- #EndEditable --> </td>

  </tr>

</table>

</body>

<!-- #EndTemplate --></html>


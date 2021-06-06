<%@ page import="com.dimata.gui.jsp.ControlList,
                 com.dimata.common.entity.contact.ContactList,
                 com.dimata.qdep.entity.I_PstDocType,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.qdep.form.FRMMessage,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.util.Command,
                 com.dimata.common.entity.location.PstLocation,
                 com.dimata.common.entity.location.Location,
                 com.dimata.posbo.entity.warehouse.MatReceive,
                 com.dimata.posbo.form.warehouse.CtrlMatReceive,
                 com.dimata.posbo.form.warehouse.FrmMatReceive,
                 com.dimata.posbo.entity.warehouse.PstMatReceive,
                 com.dimata.common.entity.payment.CurrencyType,
                 com.dimata.common.entity.payment.PstCurrencyType,
		 com.dimata.posbo.entity.purchasing.PurchaseOrder,
                 com.dimata.posbo.entity.warehouse.PstMatReturn,
                 com.dimata.posbo.entity.warehouse.MatReturn,
                 com.dimata.posbo.entity.warehouse.PstMatReceive,
                 com.dimata.common.entity.contact.*"%>
<%@ page language = "java" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RETURN, AppObjInfo.G2_SUPPLIER_RETURN, AppObjInfo.OBJ_SUPPLIER_RETURN); %>
<%@ include file = "../../../main/checkuser.jsp" %>
<!-- Jsp Block -->
<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

/*public static final String textListGlobal[][] = {
	{"Penerimaan","Dari Pembelian","Daftar","Daftar","Edit","Dengan PO","Tanpa PO","Tidak ada data penerimaan barang"},
	{"Receive","From Purchase","List","List","Edit","With PO","Without PO","Receiving goods not found"}
};*/
public static final String textListGlobal[][] = {
    {"Tambah Item","Dari Penerimaan","Ke Transfer","Daftar","Penerimaan","Tidak ada data penerimaan barang","Retur"},
    {"Add Item", "From Receive","For Transfer","List","Receive","Receiving goods not found","Return"}
};

//public static final String textListOrderHeader[][] =
//{
   // {"Nomor","Lokasi Asal","Lokasi Tujuan","Tanggal","Status","Keterangan","Nota Supplier","Waktu"},
   // {"Number","From Location","Destination","Date","Status","Remark","Supplier Invoice","Time"}
//};

/* this constant used to list text of listHeader */
public static final String textListOrderHeader[][] = {
	{"Nomor","Lokasi","Tanggal","Supplier","Status","Keterangan","Alasan","Waktu","Mata Uang"},
	{"No","Location","Date","Supplier","Status","Remark","Reason","Time","Currency"}
};

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] = {
	{"No","Kode","Tanggal","Mata Uang","Nomor PO","Supplier","Status","Keterangan"},
	{"No","Code","Date","Currency","PO Number","Supplier","Status","Description"}
};

public String drawList(int language,Vector objectClass,int start,int docType,I_DocStatus i_status) {
	String result = "";
	if(objectClass!=null && objectClass.size()>0) {
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");

		ctrlist.addHeader(textListMaterialHeader[language][0],"3%");
		ctrlist.addHeader(textListMaterialHeader[language][1],"14%");
		ctrlist.addHeader(textListMaterialHeader[language][2],"8%");
		ctrlist.addHeader(textListMaterialHeader[language][3],"10%");
		ctrlist.addHeader(textListMaterialHeader[language][4],"14%");
		ctrlist.addHeader(textListMaterialHeader[language][5],"18%");
                ctrlist.addHeader(textListMaterialHeader[language][6],"8%");
                ctrlist.addHeader(textListMaterialHeader[language][7],"25%");

		ctrlist.setLinkRow(1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();
		if(start < 0)	{
			start = 0;
		}

		for(int i=0; i<objectClass.size(); i++) {
			Vector vt = (Vector)objectClass.get(i);
			MatReceive rec = (MatReceive)vt.get(0);
			ContactList contact = (ContactList)vt.get(1);
			CurrencyType currencyType = (CurrencyType)vt.get(2);
			PurchaseOrder purchaseOrder = (PurchaseOrder)vt.get(3);

			String cntName = contact.getCompName();
			if(cntName.length()==0) {
				cntName = String.valueOf(contact.getPersonName()+" "+contact.getPersonLastname());
			}

			String str_dt_RecDate = "";
			try {
				Date dt_RecDate = rec.getReceiveDate();
				if(dt_RecDate==null) {
					dt_RecDate = new Date();
				}
				str_dt_RecDate = Formater.formatDate(dt_RecDate, "dd-MM-yyyy");
			}
			catch(Exception e) {
                str_dt_RecDate = "";
            }

			Vector rowx = new Vector();
			rowx.add(""+(start + i + 1));
			rowx.add(rec.getRecCode());
                        rowx.add(str_dt_RecDate);
                        rowx.add("<div align=\"center\">"+currencyType.getCode()+"</div>");
			rowx.add(purchaseOrder.getPoCode());
                        rowx.add(cntName);
			rowx.add(i_status.getDocStatusName(docType,rec.getReceiveStatus()));
			rowx.add(rec.getRemark());

			lstData.add(rowx);
			lstLinkData.add(String.valueOf(rec.getOID())+"', '"+purchaseOrder.getOID());
		}
		result = ctrlist.draw();
	}
	else{
		result = "<div class=\"msginfo\">&nbsp;&nbsp;"+textListGlobal[language][5]+"</div>";
	}
	return result;
}
%>

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
 * get title for purchasing(pr) document
 */
String recCode = i_pstDocType.getDocCode(docType);
String recTitle = "Terima Barang"; // i_pstDocType.getDocTitle(docType);
String retItemTitle = recTitle + " Item";
//long oidReturnMaterial = FRMQueryString.requestLong(request,"hidden_return_id");
long idSupplier = FRMQueryString.requestLong(request, "hidden_supplier_id");
long oidReturnMaterial = FRMQueryString.requestLong(request,"hidden_return_id");

MatReturn ret =new MatReturn();

if(oidReturnMaterial !=0){

try{

  ret = PstMatReturn.fetchExc(oidReturnMaterial);
  idSupplier = ret.getSupplierId();

}

  catch(Exception e){

  System.out.println(e);

}

}
/**
* get request data from current form
*/
long oidMatReceive = FRMQueryString.requestLong(request, "hidden_receive_id");
/**
* initialitation some variable
*/
int iErrCode = FRMMessage.ERR_NONE;
String msgStr = "";
int iCommand = FRMQueryString.requestCommand(request);
int prevCommand = FRMQueryString.requestInt(request,"prev_command");
int appCommand = FRMQueryString.requestInt(request,"approval_command");
int start = FRMQueryString.requestInt(request, "start");

int recordToGet = 5;
int vectSize = 0;
String whereClause = "";
//select by supplier
String whereClauseItem = "REC."+PstMatReceive.fieldNames[PstMatReceive.FLD_SUPPLIER_ID]+"="+ idSupplier;
//String orderClauseItem = " RMI."+PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_MATERIAL_ID];


/**
* instantiate some object used in this page
*/

ControlLine ctrLine = new ControlLine();
CtrlMatReceive ctrlMatReceive = new CtrlMatReceive(request);
iErrCode = ctrlMatReceive.action(iCommand , oidMatReceive);
FrmMatReceive frmrec = ctrlMatReceive.getForm();
MatReceive rec = ctrlMatReceive.getMatReceive();
msgStr = ctrlMatReceive.getMessage();


 //vectSize = PstMatReceive.getCountListBySupplier(whereClauseItem);
 //Vector records = PstMatReceive.listBySupplier(start,recordToGet, whereClauseItem, orderClauseItem);
 vectSize = PstMatReceive.getCountListBySupplier(whereClause);
 Vector records = PstMatReceive.listBySupplier(start,recordToGet, whereClauseItem,"");


/**
* handle current search data session
*/

%>
<!-- End of Jsp Block -->
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
function cmdEdit(idRcv, idPo){
	document.frm_recmaterial.hidden_receive_id.value=idRcv;
	document.frm_recmaterial.start.value=0;
	document.frm_recmaterial.approval_command.value="<%=Command.APPROVE%>";
	document.frm_recmaterial.command.value="<%=Command.EDIT%>";
	if(parseInt(idPo) != 0) {
		document.frm_recmaterial.action="return_wh_supp_material_receive_item.jsp";
	}
	else {
		document.frm_recmaterial.action="return_wh_supp_material_receive_item.jsp";
	}
	document.frm_recmaterial.submit();
}

function cmdListFirst(oidMatReceive){
        document.frm_recmaterial.hidden_receive_id.value=oidMatReceive;
	document.frm_recmaterial.command.value="<%=Command.FIRST%>";
	document.frm_recmaterial.action="return_wh_supp_material_receive_list.jsp";
	document.frm_recmaterial.submit();
}

function cmdListPrev(oidMatReceive){
        document.frm_recmaterial.hidden_receive_id.value=oidMatReceive;
	document.frm_recmaterial.command.value="<%=Command.PREV%>";
	document.frm_recmaterial.action="return_wh_supp_material_receive_list.jsp";
	document.frm_recmaterial.submit();
}

function cmdListNext(oidMatReceive){
        document.frm_recmaterial.hidden_receive_id.value=oidMatReceive;
	document.frm_recmaterial.command.value="<%=Command.NEXT%>";
	document.frm_recmaterial.action="return_wh_supp_material_receive_list.jsp";
	document.frm_recmaterial.submit();
}

function cmdListLast(oidMatReceive){
        document.frm_recmaterial.hidden_receive_id.value=oidMatReceive;
	document.frm_recmaterial.command.value="<%=Command.LAST%>";
	document.frm_recmaterial.action="return_wh_supp_material_receive_list.jsp";
	document.frm_recmaterial.submit();
}

function cmdBack(){
        //document.frm_recmaterial.hidden_receive_id.value=oidMatReceive;
	document.frm_recmaterial.command.value="<%=Command.BACK%>";
	document.frm_recmaterial.action="return_wh_supp_material_edit.jsp";
	document.frm_recmaterial.submit();
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
            &nbsp;<%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][1]%> &gt; <%=textListGlobal[SESS_LANGUAGE][3]%><!-- #EndEditable --></td>
        </tr>
        <tr>
          <td><!-- #BeginEditable "content" -->
            <!--<form name="frm_recmaterial" method="retst" action="">-->
              <form name="frm_recmaterial" method="post" action="">
              <input type="hidden" name="command" value="">
              <input type="hidden" name="add_type" value="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="hidden_receive_id" value="<%=oidMatReceive%>">
              <input type="hidden" name="hidden_return_id" value="<%=oidReturnMaterial%>">
              <input type="hidden" name="hidden_supplier_id" value="<%=idSupplier%>">
              <input type="hidden" name="approval_command">
              <table width="100%" cellspacing="0" cellpadding="3">
                <tr>
                  <td valign="top" colspan="3">
                    <hr size="1">
                    <table width="100%" border="0" cellspacing="1" cellpadding="1">
                      <tr>
                        <td width="10%"><%=textListOrderHeader[SESS_LANGUAGE][0]%></td>
                        <td width="25%">:
                           <%=ret.getRetCode()%>
                        </td>
                         <td width="10%"><%=textListOrderHeader[SESS_LANGUAGE][3]%></td>

                        <td width="28%">:

                            <%

                             ContactList contactList = new ContactList();

                             try{

                                 contactList = PstContactList.fetchExc(idSupplier);
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
                        <td width="10%"><%=textListOrderHeader[SESS_LANGUAGE][1]%></td>
                        <td width="25%">:
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

                         <%=ret.getRemark()%></td>
                      </tr>
                        <tr>
                           <td width ="10%"><%=textListOrderHeader[SESS_LANGUAGE][3]%></td>
                           <td width="25%">:
                             <%=(Formater.formatDate(ret.getReturnDate(), "dd-MM-yyyy"))%>
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
                           <td width ="8%"><%=textListOrderHeader[SESS_LANGUAGE][7]%></td>
                           <td width="25%">:
                               <%=(Formater.formatDate(ret.getReturnDate(), "HH:mm"))%>
                       </tr>

                    </table>
                  </td>
                </tr>
		<tr align="left" valign="top">
                  <td height="22" valign="middle" colspan="3"><%=drawList(SESS_LANGUAGE,records,start,docType,i_status)%></td>
                </tr>
                <tr align="left" valign="top">
                  <td height="8" align="left" colspan="3" class="command"> <span class="command">
                    <%
					ctrLine.setLocationImg(approot+"/images");
					ctrLine.initDefault();
					out.println(ctrLine.drawImageListLimit(iCommand,vectSize,start,recordToGet));
				  %>
                    </span> </td>
                </tr>
                <tr align="left" valign="top">
                  <td height="18" valign="top" colspan="3">
                    <table width="80%" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td nowrap width="6%"><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnBack.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][6],ctrLine.CMD_BACK,true)%>"></a></td>
                        <td class="command" width="94%"><a href="javascript:cmdBack()"><%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][6],ctrLine.CMD_BACK,true)%></a></td>
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




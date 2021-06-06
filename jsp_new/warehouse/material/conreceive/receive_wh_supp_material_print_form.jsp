<%@ page import="com.dimata.common.entity.location.Location,
                 com.dimata.common.entity.location.PstLocation,
                 com.dimata.common.entity.contact.ContactList,
                 com.dimata.common.entity.contact.PstContactList,
                 com.dimata.gui.jsp.ControlList,
                 com.dimata.qdep.form.FRMHandler,
                 com.dimata.qdep.entity.I_PstDocType,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.qdep.form.FRMMessage,
                 com.dimata.util.Command,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.posbo.entity.warehouse.MatConReceiveItem,
                 com.dimata.posbo.form.warehouse.FrmMatConReceiveItem,
                 com.dimata.posbo.entity.masterdata.Unit,
                 com.dimata.posbo.entity.masterdata.Material,
                 com.dimata.posbo.form.warehouse.CtrlMatConReceive,
                 com.dimata.posbo.form.warehouse.FrmMatConReceive,
                 com.dimata.posbo.entity.warehouse.MatConReceive,
                 com.dimata.posbo.form.warehouse.CtrlMatConReceiveItem,
                 com.dimata.posbo.entity.warehouse.PstMatConReceiveItem,
                 com.dimata.posbo.entity.purchasing.PurchaseOrder,
                 com.dimata.posbo.entity.purchasing.PstPurchaseOrder,
                 com.dimata.common.entity.payment.CurrencyType"%>
<%@ page language = "java" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE); %>
<%@ include file = "../../../main/checkuser.jsp" %>
<!-- Jsp Block -->
<%!
/* this constant used to list text of listHeader */
public static final String textListOrderHeader[][] = 
{
	{"Nomor","Lokasi","Tanggal","Supplier","Status","Keterangan","Nota Supplier","Ppn (%)","Sub Total"},
	{"No","Location","Date","Supplier","Status","Remark","Supplier Invoice","Total VAT","Sub Total"}
};


/* this constant used to list text of listMaterialItem */
public static final String textListOrderItem[][] = 
{
	{"No","Sku","Nama Barang","Kadaluarsa","Unit","Harga Beli","Harga Jual","Mata Uang","Qty","Total Beli"},
	{"No","Code","Name","Expired Date","Unit","Cost","Price","Currency","Qty","Total Cost"}
};

/**
* this method used to maintain poMaterialList
*/
public String drawListRetItem(int language,int iCommand,FrmMatConReceiveItem frmObject,MatConReceiveItem objEntity,Vector objectClass,long recItemId,int start)
{
	ControlList ctrlist = new ControlList();
	ctrlist.setAreaWidth("100%");
	ctrlist.setListStyle("listgen");
	ctrlist.setTitleStyle("listgentitle");
	ctrlist.setCellStyle("listgensell");
	ctrlist.setHeaderStyle("listgentitle");
	ctrlist.addHeader(textListOrderItem[language][0],"4%");
	ctrlist.addHeader(textListOrderItem[language][1],"11%");
	ctrlist.addHeader(textListOrderItem[language][2],"21%");
	ctrlist.addHeader(textListOrderItem[language][3],"15%");
	ctrlist.addHeader(textListOrderItem[language][4],"5%");
	ctrlist.addHeader(textListOrderItem[language][5],"10%");
    //ctrlist.addHeader(textListOrderItem[language][6],"10%");
	ctrlist.addHeader(textListOrderItem[language][7],"5%");
	ctrlist.addHeader(textListOrderItem[language][8],"5%");
	ctrlist.addHeader(textListOrderItem[language][9],"10%");

	Vector lstData = ctrlist.getData();
	Vector rowx = new Vector(1,1);
	ctrlist.reset();
	ctrlist.setLinkRow(1);
	int index = -1;
	if(start<0)
	{
	   start=0;
	}	
	
	for(int i=0; i<objectClass.size(); i++)
	{
		Vector temp = (Vector)objectClass.get(i);
		MatConReceiveItem recItem = (MatConReceiveItem)temp.get(0);
		Material mat = (Material)temp.get(1);
		Unit unit = (Unit)temp.get(2);
		CurrencyType currencyType = (CurrencyType)temp.get(3);
		rowx = new Vector();
		start = start + 1;
		
		rowx.add("<div align=\"center\">"+start+"</div>");
		rowx.add(mat.getSku());
		rowx.add(mat.getName());
		rowx.add("<div align=\"center\">"+Formater.formatDate(recItem.getExpiredDate(), "dd-MM-yyyy")+"</div>");
		rowx.add("<div align=\"center\">"+unit.getCode()+"</div>");			 
		rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(recItem.getCost())+"</div>");
        //rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(mat.getDefaultPrice())+"</div>");
		rowx.add("<div align=\"center\">"+currencyType.getCode()+"</div>");
		rowx.add("<div align=\"center\">"+String.valueOf(recItem.getQty())+"</div>");			 			 
		rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(recItem.getTotal())+"</div>");
		lstData.add(rowx);
	}
	return ctrlist.draw();
}

%>
<%
/**
* get approval status for create document 
*/
I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();
I_Approval i_approval = (I_Approval) Class.forName(approvalClassName).newInstance();
I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();
int docType = i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL,I_DocType.MAT_DOC_TYPE_LMRR);
%>
<%
/**
* get request data from current form
*/
int iCommand = FRMQueryString.requestCommand(request);
int startItem = FRMQueryString.requestInt(request,"start_item");
int prevCommand = FRMQueryString.requestInt(request,"prev_command");
int appCommand = FRMQueryString.requestInt(request,"approval_command");
long oidReceiveMaterial = FRMQueryString.requestLong(request,"hidden_receive_id");
long oidReceiveMaterialItem = FRMQueryString.requestLong(request,"hidden_receive_item_id");

/**
* initialization of some identifier
*/
int iErrCode = FRMMessage.NONE;
String msgString = "";

/**
* purchasing pr code and title
*/
String recCode = i_pstDocType.getDocCode(docType);
String retTitle = "Penerimaan Barang"; //i_pstDocType.getDocTitle(docType);
String recItemTitle = retTitle + " Item";

/**
* process on purchase order main
*/
CtrlMatConReceive ctrlMatConReceive = new CtrlMatConReceive(request);
iErrCode = ctrlMatConReceive.action(Command.EDIT,oidReceiveMaterial);
FrmMatConReceive frmMatConReceive = ctrlMatConReceive.getForm();
MatConReceive rec = ctrlMatConReceive.getMatConReceive();
	
/**
* check if document may modified or not 
*/
boolean privManageData = true; 

ControlLine ctrLine = new ControlLine();
CtrlMatConReceiveItem ctrlMatConReceiveItem = new CtrlMatConReceiveItem(request);
ctrlMatConReceiveItem.setLanguage(SESS_LANGUAGE);
iErrCode = ctrlMatConReceiveItem.action(iCommand,oidReceiveMaterialItem,oidReceiveMaterial);
FrmMatConReceiveItem frmMatConReceiveItem = ctrlMatConReceiveItem.getForm();
MatConReceiveItem recItem = ctrlMatConReceiveItem.getMatConReceiveItem();
msgString = ctrlMatConReceiveItem.getMessage();

String whereClauseItem = PstMatConReceiveItem.fieldNames[PstMatConReceiveItem.FLD_RECEIVE_MATERIAL_ID]+"="+oidReceiveMaterial;
String orderClauseItem = "";
int vectSizeItem = PstMatConReceiveItem.getCount(whereClauseItem);
int recordToGetItem = 0;

if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST)
{
	startItem = ctrlMatConReceiveItem.actionList(iCommand,startItem,vectSizeItem,recordToGetItem);
} 

Vector listMatConReceiveItem = PstMatConReceiveItem.list(startItem,recordToGetItem,whereClauseItem);
if(listMatConReceiveItem.size()<1 && startItem>0)
{
	 if(vectSizeItem-recordToGetItem > recordToGetItem)
	 {
		startItem = startItem - recordToGetItem;   
	 }
	 else
	 {
		startItem = 0 ;
		iCommand = Command.FIRST;
		prevCommand = Command.FIRST; 
	 }
	 listMatConReceiveItem = PstMatConReceiveItem.list(startItem,recordToGetItem,whereClauseItem);
}

PurchaseOrder po = new PurchaseOrder();
try
{
	po = PstPurchaseOrder.fetchExc(rec.getPurchaseOrderId());
}
catch(Exception xxx)
{
}	
%>
<html>
<!-- #BeginTemplate "/Templates/print.dwt" --> 
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title
><script language="JavaScript">
<!--
//------------------------- START JAVASCRIPT FUNCTION FOR PO MAIN -----------------------
function main(oid,comm)
{
	document.frm_recmaterial.command.value=comm;
	document.frm_recmaterial.hidden_receive_id.value=oid;
	document.frm_recmaterial.action="receive_wh_supp_po_material_edit.jsp";
	document.frm_recmaterial.submit();
}
//------------------------- END JAVASCRIPT FUNCTION FOR PO MAIN -----------------------


//------------------------- START JAVASCRIPT FUNCTION FOR PO ITEM -----------------------
function cmdAdd()
{
	document.frm_recmaterial.hidden_receive_item_id.value="0";
	document.frm_recmaterial.command.value="<%=Command.ADD%>";
	document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_recmaterial.action="receive_wh_supp_po_materialitem.jsp";
	document.frm_recmaterial.submit();
}

function cmdEdit(oidReceiveMaterialItem)
{
	document.frm_recmaterial.hidden_receive_item_id.value=oidReceiveMaterialItem;
	document.frm_recmaterial.command.value="<%=Command.EDIT%>";
	document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_recmaterial.action="receive_wh_supp_po_materialitem.jsp";
	document.frm_recmaterial.submit();
}

function cmdAsk(oidReceiveMaterialItem){
	document.frm_recmaterial.hidden_receive_item_id.value=oidReceiveMaterialItem;
	document.frm_recmaterial.command.value="<%=Command.ASK%>";
	document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_recmaterial.action="receive_wh_supp_po_materialitem.jsp";
	document.frm_recmaterial.submit();
}

function cmdSave()
{
	document.frm_recmaterial.command.value="<%=Command.SAVE%>"; 
	document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_recmaterial.action="receive_wh_supp_po_materialitem.jsp";
	document.frm_recmaterial.submit();
}

function cmdConfirmDelete(oidReceiveMaterialItem){
	document.frm_recmaterial.hidden_receive_item_id.value=oidReceiveMaterialItem;
	document.frm_recmaterial.command.value="<%=Command.DELETE%>";
	document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_recmaterial.approval_command.value="<%=Command.DELETE%>";	
	document.frm_recmaterial.action="receive_wh_supp_po_materialitem.jsp";
	document.frm_recmaterial.submit();
}

function cmdCancel(oidReceiveMaterialItem){
	document.frm_recmaterial.hidden_receive_item_id.value=oidReceiveMaterialItem;
	document.frm_recmaterial.command.value="<%=Command.EDIT%>";
	document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_recmaterial.action="receive_wh_supp_po_materialitem.jsp";
	document.frm_recmaterial.submit();
}

function cmdBack(){
	document.frm_recmaterial.command.value="<%=Command.EDIT%>";
	document.frm_recmaterial.action="receive_wh_supp_po_material_edit.jsp";
	document.frm_recmaterial.submit();
}

function sumPrice()
{
}		

function cmdCheck()
{
	var strvalue  = "materialpodosearch.jsp?command=<%=Command.FIRST%>"+
					"&mat_code="+document.frm_recmaterial.matCode.value+
					"&oidPurchaseOrder=<%=rec.getPurchaseOrderId()%>";
	window.open(strvalue,"material", "height=500,width=750,status=yes,toolbar=no,menubar=yes,location=no,scrollbars=yes");
	//alert("Sorry, under construction!!!");
}

function cntTotal()
{
	var qty = cleanNumberInt(document.frm_recmaterial.<%=FrmMatConReceiveItem.fieldNames[FrmMatConReceiveItem.FRM_FIELD_QTY]%>.value,guiDigitGroup);
	var price = cleanNumberInt(document.frm_recmaterial.<%=FrmMatConReceiveItem.fieldNames[FrmMatConReceiveItem.FRM_FIELD_COST]%>.value,guiDigitGroup);
	if(!(isNaN(qty)) && (qty != '0'))
	{
		var amount = parseFloat(price) * qty;
		document.frm_recmaterial.<%=FrmMatConReceiveItem.fieldNames[FrmMatConReceiveItem.FRM_FIELD_TOTAL]%>.value = amount;					 
	}
	else
	{
		document.frm_recmaterial.<%=FrmMatConReceiveItem.fieldNames[FrmMatConReceiveItem.FRM_FIELD_QTY]%>.focus();
	}
}

function cmdListFirst(){
	document.frm_recmaterial.command.value="<%=Command.FIRST%>";
	document.frm_recmaterial.prev_command.value="<%=Command.FIRST%>";
	document.frm_recmaterial.action="receive_wh_supp_po_materialitem.jsp";
	document.frm_recmaterial.submit();
}

function cmdListPrev(){
	document.frm_recmaterial.command.value="<%=Command.PREV%>";
	document.frm_recmaterial.prev_command.value="<%=Command.PREV%>";
	document.frm_recmaterial.action="receive_wh_supp_po_materialitem.jsp";
	document.frm_recmaterial.submit();
}

function cmdListNext(){
	document.frm_recmaterial.command.value="<%=Command.NEXT%>";
	document.frm_recmaterial.prev_command.value="<%=Command.NEXT%>";
	document.frm_recmaterial.action="receive_wh_supp_po_materialitem.jsp";
	document.frm_recmaterial.submit();
}

function cmdListLast(){
	document.frm_recmaterial.command.value="<%=Command.LAST%>";
	document.frm_recmaterial.prev_command.value="<%=Command.LAST%>";
	document.frm_recmaterial.action="receive_wh_supp_po_materialitem.jsp";
	document.frm_recmaterial.submit();
}

function cmdBackList(){
	document.frm_recmaterial.command.value="<%=Command.FIRST%>";
	document.frm_recmaterial.action="receive_wh_supp_po_material_list.jsp";
	document.frm_recmaterial.submit();
}
//------------------------- END JAVASCRIPT FUNCTION FOR PO ITEM -----------------------


//------------------------- START JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------
function MM_swapImgRestore() { //v3.0
	var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
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
//------------------------- END JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------//-->
</script>
<!-- #EndEditable --> 
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<!-- #BeginEditable "stylestab" --> 
<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
<link rel="stylesheet" href="../../../styles/print.css" type="text/css">
<!-- #EndEditable --> 
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0" >
  <tr> 
    <td width="88%" valign="top" align="left" height="56"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frm_recmaterial" method ="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="start_item" value="<%=startItem%>">
              <input type="hidden" name="hidden_receive_id" value="<%=oidReceiveMaterial%>">
              <input type="hidden" name="hidden_receive_item_id" value="<%=oidReceiveMaterialItem%>">
              <input type="hidden" name="<%=FrmMatConReceiveItem.fieldNames[FrmMatConReceiveItem.FRM_FIELD_RECEIVE_MATERIAL_ID]%>" value="<%=oidReceiveMaterial%>">
              <input type="hidden" name="approval_command" value="<%=appCommand%>">
              <table width="100%" cellpadding="1" cellspacing="0">
                <tr align="center"> 
                  <td colspan="3" class="title" align="center"> 
                    <table width="100%" border="0" cellpadding="1">
                      <tr align="left" class="listgensell">
                        <td colspan="4">
							<table width="100%" border="0" cellpadding="1">
							  <tr>
								<td class="title" align="left" width="15%"><img src="../../../images/company.jpg"></td>
								<td class="title" align="center" width="70%"><b>&nbsp;<%=retTitle.toUpperCase()%></b></td>
								<td width="15%"></td>
							  </tr>
							</table>
						</td>
                      </tr>
                      <tr align="center" class="listgensell"> 
                        <td colspan="4" nowrap><b>&nbsp;</b></td>
                      </tr>
                      <tr class="listgensell"> 
                        <td width="10%" align="left" nowrap><%=textListOrderHeader[SESS_LANGUAGE][0]%></td>
                        <td width="30%" align="left"> : <%=rec.getRecCode()%> </td>
                        <td width="19%" align="left" valign="bottom">&nbsp; </td>
                        <td width="41%" align="right"> 
                      </tr>
                      <tr class="listgensell"> 
                        <td width="10%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][1]%></td>
                        <td width="30%" align="left">: 
                          <% 
							Location loc = new Location();
							try{
								loc = PstLocation.fetchExc(rec.getLocationId());
							}catch(Exception e){}
						  %>
                          <%=loc.getName()%></td>
                        <td width="19%" rowspan="2" align="left" valign="top"> 
                          <%//=strComboStatus%>
                        </td>
                        <td width="41%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][3]%> : 
                          <% 
								ContactList cnt = new ContactList();
								try{
									cnt = PstContactList.fetchExc(rec.getSupplierId());
								}catch(Exception e){}
								
								String cntName = cnt.getCompName();
								if(cntName.length()==0){
									cntName = cnt.getPersonName()+" "+cnt.getPersonLastname();
								}
						  %>
                          <%=cntName%> 
                      </tr>
                      <tr class="listgensell"> 
                        <td width="10%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][2]%></td>
                        <td width="30%" align="left">: <%=Formater.formatDate(rec.getReceiveDate(),"dd-MM-yyyy")%></td>
                        <td width="41%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][6]%> : <%=rec.getInvoiceSupplier()%> </td>
                      </tr>
                    </table>
                  </td>
                </tr>
                <tr> 
                  <td valign="top"> 
                    <table width="100%" cellpadding="1" cellspacing="1">
                      <tr> 
                        <td colspan="2" > 
                          <table width="100%" border="0" cellspacing="0" cellpadding="0" >
                            <tr align="left" valign="top"> 
                              <td height="22" valign="middle" colspan="3"> 
                                <table width="100%" border="1" cellspacing="0" cellpadding="0">
                                  <tr align="center"> 
                                    <td width="6%" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][0]%></td>
                                    <td width="12%" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][1]%></td>
                                    <td width="27%" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][2]%></td>
                                    <td width="12%" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][3]%></td>
                                    <td width="9%" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][4]%></td>
                                    <td width="9%" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][5]%></td>
                                    <!--td width="9%" class="listgentitle"><%//=textListOrderItem[SESS_LANGUAGE][6]%></td-->
                                    <!--<td width="6%" class="listgentitle"><%//=textListOrderItem[SESS_LANGUAGE][6]%></td>-->
                                    <td width="5%" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][8]%></td>
                                    <td width="10%" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][9]%></td>
                                  </tr>
                                  <%
								int start = 0;
								for(int i=0; i<listMatConReceiveItem.size(); i++)
								{
									Vector temp = (Vector)listMatConReceiveItem.get(i);
									MatConReceiveItem recItemx = (MatConReceiveItem)temp.get(0);
									Material mat = (Material)temp.get(1);
									Unit unit = (Unit)temp.get(2);
									CurrencyType currencyType = (CurrencyType)temp.get(3);									
									start = start + 1;
								%>
                                  <tr> 
                                    <td width="6%" align="center" class="listgensell">&nbsp;<%=start%></td>
                                    <td width="12%" class="listgensell">&nbsp;<%=mat.getSku()%></td>
                                    <td width="25%" class="listgensell">&nbsp;<%=mat.getName()%></td>
                                    <td width="12%" align="center" class="listgensell">&nbsp;<%=Formater.formatDate(recItemx.getExpiredDate(), "dd-MM-yyyy")%></td>
                                    <td width="9%" align="center" class="listgensell">&nbsp;<%=unit.getCode()%></td>
                                    <td width="10%" align="right" class="listgensell">&nbsp;<%=FRMHandler.userFormatStringDecimal(recItemx.getCost())%></td>
                                    <!--td width="10%" align="right" class="listgensell">&nbsp;<%//=FRMHandler.userFormatStringDecimal(mat.getDefaultPrice())%></td-->
                                    <!--<td width="6%" align="center" class="listgensell">&nbsp;<%//=matCurrency.getCode()%></td>-->
                                    <td width="5%" align="right" class="listgensell">&nbsp;<%=recItemx.getQty()%></td>
                                    <td width="10%" align="right" class="listgensell">&nbsp;<%=FRMHandler.userFormatStringDecimal(recItemx.getTotal())%></td>
                                  </tr>
                                  <%}%>
                                </table>
                              </td>
                            </tr>
                          </table>
                        </td>
                      </tr>
                      <tr> 
                        <td valign="top" rowspan="2"><%=textListOrderHeader[SESS_LANGUAGE][5]%> : <%=rec.getRemark()%> </td>
                        <td width="27%" valign="top"> 
                          <table width="100%" border="0">
                            <%
                                String whereItem = ""+PstMatConReceiveItem.fieldNames[PstMatConReceiveItem.FLD_RECEIVE_MATERIAL_ID]+"="+oidReceiveMaterial;
                                double total = PstMatConReceiveItem.getTotal(whereItem);
                            %>
                            <tr class="listgensell">
                              <td width="44%">
                                <div align="right"><%=textListOrderHeader[SESS_LANGUAGE][8]%></div>
                              </td>
                              <td width="15%">
                                <div align="right"></div>
                              </td>
                              <td width="41%">
                                <div align="right"> <%=FRMHandler.userFormatStringDecimal(total)%> </div>
                              </td>
                            </tr>
                            <tr class="listgensell">
                              <td width="44%"> 
                                <div align="right"><%=textListOrderHeader[SESS_LANGUAGE][7]%></div>
                              </td>
                              <td width="15%"> 
                                <div align="right"></div>
                              </td>
                              <td width="41%"> 
                                <div align="right"> <%=FRMHandler.userFormatStringDecimal(rec.getTotalPpn())%> </div>
                              </td>
                            </tr>
                          </table>
                        </td>
                      </tr>
                      <%if((listMatConReceiveItem!=null) && (listMatConReceiveItem.size()>0) && (rec.getTotalPpn()>0)){%>
                      <tr> 
                        <td width="27%" valign="top"> 
                          <table width="100%" border="0">
                            <tr class="listgensell"> 
                              <td width="44%"> 
                                <div align="right"><%="Total"%></div>
                              </td>
                              <td width="15%"> 
                                <div align="right"></div>
                              </td>
                              <td width="41%"> 
                                <div align="right"> 
                                  <%
                                    double ppn = total * rec.getTotalPpn() / 100;
                                    ppn = total + ppn;
                                    out.println(FRMHandler.userFormatStringDecimal(ppn));
        						  %>
                                </div>
                              </td>
                            </tr>
                          </table>
                        </td>
                      </tr>
                      <%}%>
                    </table>
                  </td>
                </tr>
              </table>			  
			  <table width="100%">			  
                      <tr align="left" valign="top"> 
                        <td height="40" valign="middle" colspan="3"></td>
                      </tr>
                      <tr>
                        
                  <td width="34%" align="center" nowrap>Diterima Oleh,</td>
                  <td align="center" valign="bottom" width="33%">Dikirim Oleh,</td>
                        
                  <td width="33%" align="center">Mengetahui,</td>
                      </tr>
                      <tr align="left" valign="top"> 
                        <td height="75" valign="middle" colspan="3"></td>
                      </tr>
                      <tr>
                        <td width="34%" align="center" nowrap>
							(.................................)
						</td>
                        <td align="center" valign="bottom" width="33%">
							(.................................)
						</td>
                        <td width="33%" align="center">
							(.................................)
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
<!-- #EndTemplate -->
</html>

<%@page import="com.dimata.posbo.entity.masterdata.PstMaterial"%>
<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*,
                   com.dimata.posbo.form.warehouse.FrmMatReceiveItem,
                   com.dimata.posbo.entity.masterdata.Material,
                   com.dimata.posbo.entity.masterdata.Unit,
                   com.dimata.posbo.form.warehouse.CtrlMatReceive,
                   com.dimata.posbo.form.warehouse.FrmMatReceive,
                   com.dimata.posbo.form.warehouse.CtrlMatReceiveItem,
                   com.dimata.posbo.entity.warehouse.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.entity.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package common -->
<%@ page import = "com.dimata.common.entity.location.*" %>
<%@ page import = "com.dimata.common.entity.contact.*" %>
<%@ page import = "com.dimata.common.entity.payment.*" %>
<!--package material -->
<%@ page import = "com.dimata.harisma.entity.masterdata.*" %>
<%@ include file = "../../../main/javainit.jsp" %>
<%
int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_LOCATION_RECEIVE, AppObjInfo.OBJ_LOCATION_RECEIVE);
int  appObjCodeShowQtyAndPrice = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE_QTY_AND_PRICE);
%>
<%@ include file = "../../../main/checkuser.jsp" %>
<%
boolean privShowQtyPrice = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeShowQtyAndPrice, AppObjInfo.COMMAND_VIEW));
String sEnableExpiredDate = PstSystemProperty.getValueByName("ENABLE_EXPIRED_DATE");
boolean bEnableExpiredDate = (sEnableExpiredDate!=null && sEnableExpiredDate.equalsIgnoreCase("YES")) ? true : false;
%>
<!-- Jsp Block -->
<%!
/* this constant used to list text of listHeader */
public static final String textListOrderHeader[][] = 
{
	{"No","Lokasi","Tanggal","Asal Pengiriman","Status","Keterangan","Nomor Pengiriman"},
	{"No","Location","Date","Supplier","Status","Remark","Dispatch Number"}	
};


/* this constant used to list text of listMaterialItem */
public static final String textListOrderItem[][] = 
{
	{"No","Sku","Nama","Kadaluarsa","Unit","Harga Beli","Mata Uang","Jumlah","Sub Total"},
	{"No","Code","Name","Expired Date","Unit","Cost","Currency","Quantity","Sub Total"}
};

/**
* this method used to maintain poMaterialList
*/
public String drawListRetItem(int language,int iCommand,FrmMatReceiveItem frmObject,MatReceiveItem objEntity,Vector objectClass,long recItemId,int start)
{
	ControlList ctrlist = new ControlList();
	ctrlist.setAreaWidth("100%");
	ctrlist.setListStyle("listgen");
	ctrlist.setTitleStyle("listgentitle");
	ctrlist.setCellStyle("listgensell");
	ctrlist.setHeaderStyle("listgentitle");
	ctrlist.addHeader(textListOrderItem[language][0],"5%");
	ctrlist.addHeader(textListOrderItem[language][1],"10%");
	ctrlist.addHeader(textListOrderItem[language][2],"20%");
	ctrlist.addHeader(textListOrderItem[language][3],"15%");
	ctrlist.addHeader(textListOrderItem[language][4],"5%");
	ctrlist.addHeader(textListOrderItem[language][5],"10%");
	ctrlist.addHeader(textListOrderItem[language][6],"5%");
	ctrlist.addHeader(textListOrderItem[language][7],"5%");
	ctrlist.addHeader(textListOrderItem[language][8],"15%");

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
		MatReceiveItem recItem = (MatReceiveItem)temp.get(0);
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
		rowx.add("<div align=\"right\">"+Formater.formatNumber(recItem.getCost(),"##,###.00")+"</div>");
		rowx.add("<div align=\"center\">"+currencyType.getCode()+"</div>");
		rowx.add("<div align=\"center\">"+String.valueOf(recItem.getQty())+"</div>");			 			 
		rowx.add("<div align=\"right\">"+Formater.formatNumber(recItem.getTotal(),"##,###.00")+"</div>");
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
String retTitle = i_pstDocType.getDocTitle(docType);
String recItemTitle = retTitle + " Item";

/**
* process on purchase order main
*/
CtrlMatReceive ctrlMatReceive = new CtrlMatReceive(request);
iErrCode = ctrlMatReceive.action(Command.EDIT,oidReceiveMaterial);
FrmMatReceive frmMatReceive = ctrlMatReceive.getForm();
MatReceive rec = ctrlMatReceive.getMatReceive();

/**
* check if document may modified or not 
*/
boolean privManageData = true; 

ControlLine ctrLine = new ControlLine();
CtrlMatReceiveItem ctrlMatReceiveItem = new CtrlMatReceiveItem(request);
ctrlMatReceiveItem.setLanguage(SESS_LANGUAGE);
iErrCode = ctrlMatReceiveItem.action(iCommand,oidReceiveMaterialItem,oidReceiveMaterial);
FrmMatReceiveItem frmMatReceiveItem = ctrlMatReceiveItem.getForm();
MatReceiveItem recItem = ctrlMatReceiveItem.getMatReceiveItem();
msgString = ctrlMatReceiveItem.getMessage();

String whereClauseItem = PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]+"="+oidReceiveMaterial;
String orderClauseItem = " RMI."+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID];
int vectSizeItem = PstMatReceiveItem.getCount(whereClauseItem);
int recordToGetItem = 25;

if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST)
{
	startItem = ctrlMatReceiveItem.actionList(iCommand,startItem,vectSizeItem,recordToGetItem);
} 

//Vector listMatReceiveItem = PstMatReceiveItem.list(startItem,recordToGetItem,whereClauseItem);
Vector listMatReceiveItem = PstMatReceiveItem.listVectorRecItemComplete(0,0,whereClauseItem,orderClauseItem);
if(listMatReceiveItem.size()<1 && startItem>0)
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
	 listMatReceiveItem = PstMatReceiveItem.list(startItem,recordToGetItem,whereClauseItem);
}

//Fetch Dispatch Material Info
MatDispatch df = new MatDispatch();
try
{
	df = PstMatDispatch.fetchExc(rec.getDispatchMaterialId());
}
catch(Exception xxx)
{
}	

%>
<html>
<!-- #BeginTemplate "/Templates/print.dwt" --> 
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
<!--
//------------------------- START JAVASCRIPT FUNCTION FOR PO MAIN -----------------------
function main(oid,comm)
{
	document.frm_recmaterial.command.value=comm;
	document.frm_recmaterial.hidden_receive_id.value=oid;
	document.frm_recmaterial.action="receive_store_wh_material_edit.jsp";
	document.frm_recmaterial.submit();
}
//------------------------- END JAVASCRIPT FUNCTION FOR PO MAIN -----------------------


//------------------------- START JAVASCRIPT FUNCTION FOR PO ITEM -----------------------
function cmdAdd()
{
	document.frm_recmaterial.hidden_receive_item_id.value="0";
	document.frm_recmaterial.command.value="<%=Command.ADD%>";
	document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_recmaterial.action="receive_store_wh_materialitem.jsp";
	document.frm_recmaterial.submit();
}

function cmdEdit(oidReceiveMaterialItem)
{
	document.frm_recmaterial.hidden_receive_item_id.value=oidReceiveMaterialItem;
	document.frm_recmaterial.command.value="<%=Command.EDIT%>";
	document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_recmaterial.action="receive_store_wh_materialitem.jsp";
	document.frm_recmaterial.submit();
}

function cmdAsk(oidReceiveMaterialItem){
	document.frm_recmaterial.hidden_receive_item_id.value=oidReceiveMaterialItem;
	document.frm_recmaterial.command.value="<%=Command.ASK%>";
	document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_recmaterial.action="receive_store_wh_materialitem.jsp";
	document.frm_recmaterial.submit();
}

function cmdSave()
{
	document.frm_recmaterial.command.value="<%=Command.SAVE%>"; 
	document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_recmaterial.action="receive_store_wh_materialitem.jsp";
	document.frm_recmaterial.submit();
}

function cmdConfirmDelete(oidReceiveMaterialItem){
	document.frm_recmaterial.hidden_receive_item_id.value=oidReceiveMaterialItem;
	document.frm_recmaterial.command.value="<%=Command.DELETE%>";
	document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_recmaterial.approval_command.value="<%=Command.DELETE%>";	
	document.frm_recmaterial.action="receive_store_wh_materialitem.jsp";
	document.frm_recmaterial.submit();
}

function cmdCancel(oidReceiveMaterialItem){
	document.frm_recmaterial.hidden_receive_item_id.value=oidReceiveMaterialItem;
	document.frm_recmaterial.command.value="<%=Command.EDIT%>";
	document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_recmaterial.action="receive_store_wh_materialitem.jsp";
	document.frm_recmaterial.submit();
}

function cmdBack(){
	document.frm_recmaterial.command.value="<%=Command.EDIT%>";
	document.frm_recmaterial.action="receive_store_wh_material_edit.jsp";
	document.frm_recmaterial.submit();
}

function sumPrice()
{
}		

function cmdCheck()
{
	var strvalue  = "materialdfdosearch.jsp?command=<%=Command.FIRST%>"+
					"&mat_code="+document.frm_recmaterial.matCode.value+
					"&oidDispatch="+document.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_DISPATCH_MATERIAL_ID]%>.value;
	window.open(strvalue,"material", "height=600,width=700,status=yes,toolbar=no,menubar=yes,location=no,scrollbars=yes");
	//alert("Sorry, under construction!!!");
}

function cntTotal()
{
	var qty = cleanNumberInt(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.value,guiDigitGroup);
	var price = cleanNumberInt(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>.value,guiDigitGroup);
	if(!(isNaN(qty)) && (qty != '0'))
	{
		var amount = parseFloat(price) * qty;
		document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_TOTAL]%>.value = amount;					 
	}
	else
	{
		document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.focus();
	}
}

function cmdListFirst(){
	document.frm_recmaterial.command.value="<%=Command.FIRST%>";
	document.frm_recmaterial.prev_command.value="<%=Command.FIRST%>";
	document.frm_recmaterial.action="receive_store_wh_materialitem.jsp";
	document.frm_recmaterial.submit();
}

function cmdListPrev(){
	document.frm_recmaterial.command.value="<%=Command.PREV%>";
	document.frm_recmaterial.prev_command.value="<%=Command.PREV%>";
	document.frm_recmaterial.action="receive_store_wh_materialitem.jsp";
	document.frm_recmaterial.submit();
}

function cmdListNext(){
	document.frm_recmaterial.command.value="<%=Command.NEXT%>";
	document.frm_recmaterial.prev_command.value="<%=Command.NEXT%>";
	document.frm_recmaterial.action="receive_store_wh_materialitem.jsp";
	document.frm_recmaterial.submit();
}

function cmdListLast(){
	document.frm_recmaterial.command.value="<%=Command.LAST%>";
	document.frm_recmaterial.prev_command.value="<%=Command.LAST%>";
	document.frm_recmaterial.action="receive_store_wh_materialitem.jsp";
	document.frm_recmaterial.submit();
}

function cmdBackList(){
	document.frm_recmaterial.command.value="<%=Command.FIRST%>";
	document.frm_recmaterial.action="receive_store_wh_material_list.jsp";
	document.frm_recmaterial.submit();
}
//------------------------- END JAVASCRIPT FUNCTION FOR PO ITEM -----------------------


//------------------------- START JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------

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
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%">
  <tr> 
    <td width="88%" valign="top" align="left" height="56"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frm_recmaterial" method ="post" action="">
              <%
try
{
%>
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="start_item" value="<%=startItem%>">
              <input type="hidden" name="hidden_receive_id" value="<%=oidReceiveMaterial%>">
              <input type="hidden" name="hidden_receive_item_id" value="<%=oidReceiveMaterialItem%>">
              <input type="hidden" name="<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_RECEIVE_MATERIAL_ID]%>" value="<%=oidReceiveMaterial%>">
              <input type="hidden" name="approval_command" value="<%=appCommand%>">
              <input type="hidden" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_DISPATCH_MATERIAL_ID]%>" value="<%=rec.getDispatchMaterialId()%>">
              <%
}
catch(Exception exc)
{
	System.out.println(exc);
}
%>
              <table width="100%" cellpadding="1" cellspacing="0">
                <tr align="center">
                  <td colspan="3" align="left" class="title"><img src="../../../images/company_pdf.jpg" width="120" height="50"></td>
                </tr>
                <tr align="center"> 
                  <td colspan="3" class="title"><b><%=retTitle.toUpperCase()%></b></td>
                </tr>
                <tr align="center"> 
                  <td colspan="3" class="title"> 
                    <table width="100%" border="0" cellpadding="1">
                      <tr>
                        <td width="11%" align="left" nowrap><%=recCode%> <%=textListOrderHeader[SESS_LANGUAGE][0]%></td>
                        <td width="29%" align="left"> : 
                          <%
						  if(rec.getRecCode()!="" && iErrCode==0)
						  {
							out.println(""+rec.getRecCode());
						  }
						  else
						  {
						  	out.println("");
						  }
						  %>
                        </td>
                        <td align="left" valign="bottom" width="35%"><%=textListOrderHeader[SESS_LANGUAGE][3]%> : 
                          <% 
							Location loc1 = new Location();
							try{
								loc1 = PstLocation.fetchExc(rec.getReceiveFrom());
							}catch(Exception e){}
						  %>
                          <%=loc1.getName()%></td>
                        <td width="25%" align="right"><%=textListOrderHeader[SESS_LANGUAGE][6]%>: <%= df.getDispatchCode() %></td>						 
                      </tr>
                      <tr>
                        <td width="11%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][1]%></td>
                        <td width="29%" align="left">: 
                          <% 
							Location loc = new Location();
							try{
								loc = PstLocation.fetchExc(rec.getLocationId());
							}catch(Exception e){}
						  %>
                          <%=loc.getName()%></td>
                        <td align="left" valign="top" rowspan="2" width="35%"> 
                          <%//=strComboStatus%> </td>
                        <td width="25%" align="right">
                      </tr>
                      <tr>
                        <td width="11%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][2]%></td>
                        <td width="29%" align="left">: <%=Formater.formatDate(rec.getReceiveDate(),"dd MMMM yyyy")%></td>
                        <td width="25%" align="right">&nbsp; </td>
                      </tr>
                    </table>
                  </td>
                </tr>
                <tr> 
                  <td valign="top"> 
                    <table width="100%" cellpadding="1" cellspacing="1">
                      <tr> 
                        <td colspan="3" > 
                          <table width="100%" border="0" cellspacing="0" cellpadding="0" >
                            <tr align="left" valign="top"> 
                              <%try
								{
								%>
                              <td height="22" valign="middle" colspan="3"> 

								<table width="100%" border="1" cellspacing="0" cellpadding="0" class="listgen">
                                  <tr align="center"> 
                                    <td class="listgentitle" width="5%"><%=textListOrderItem[SESS_LANGUAGE][0]%></td>
                                    <td class="listgentitle" width="10%"><%=textListOrderItem[SESS_LANGUAGE][1]%></td>
                                    <td class="listgentitle" width="20%"><%=textListOrderItem[SESS_LANGUAGE][2]%></td>
                                    <%if(bEnableExpiredDate){%>
                                        <td class="listgentitle" width="15%"><%=textListOrderItem[SESS_LANGUAGE][3]%></td>
                                    <%}%>
                                    <td class="listgentitle" width="5%"><%=textListOrderItem[SESS_LANGUAGE][4]%></td>
                                    <%if(privShowQtyPrice){%>
                                        <td class="listgentitle" width="10%"><%=textListOrderItem[SESS_LANGUAGE][5]%></td>
                                    <%}%>
                                    <td class="listgentitle" width="5%"><%=textListOrderItem[SESS_LANGUAGE][6]%></td>
                                    <td class="listgentitle" width="5%"><%=textListOrderItem[SESS_LANGUAGE][7]%></td>
                                    <%if(privShowQtyPrice){%>
                                        <td class="listgentitle" width="15%"><%=textListOrderItem[SESS_LANGUAGE][8]%></td>
                                    <%}%>
                                  </tr>
                                      <%
                                        int start = 0;
                                        for(int i=0; i<listMatReceiveItem.size(); i++)
                                        {
                                                Vector temp = (Vector)listMatReceiveItem.get(i);
                                                MatReceiveItem receiveItem = (MatReceiveItem)temp.get(0);
                                                Material mat = (Material)temp.get(1);
                                                Unit unit = (Unit)temp.get(2);
                                                CurrencyType currencyType = (CurrencyType)temp.get(3);
                                                String listStockCode= "";
                                                if(mat.getRequiredSerialNumber()==PstMaterial.REQUIRED){
                                                     String where = PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_RECEIVE_MATERIAL_ITEM_ID]+"="+receiveItem.getOID();
                                                     Vector cntStockCode = PstReceiveStockCode.list(0,0,where,"");
                                                     for (int s = 0; s < cntStockCode.size(); s++) {
                                                        ReceiveStockCode materialStockCode = (ReceiveStockCode) cntStockCode.get(s);
                                                        if(s==0){
                                                            listStockCode=listStockCode+"<br>&nbsp;SN : "+materialStockCode.getStockCode();
                                                        }else{
                                                             listStockCode=listStockCode+"<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+materialStockCode.getStockCode();
                                                        }
                                                     }
                                                }
                                                start = start + 1;
                                      %>
                                      <tr>
                                        <td class="listgensell" width="5%"><%=start%></td>
                                        <td class="listgensell" width="10%"><%=mat.getSku()%></td>
                                        <td class="listgensell" width="20%"><%=mat.getName()%><%=listStockCode%></td>
                                        <%if(bEnableExpiredDate){%>
                                            <td class="listgensell" width="15%"><%=Formater.formatDate(receiveItem.getExpiredDate(), "dd-MM-yyyy")%></td>
                                        <%}%>
                                        <td class="listgensell" width="5%"><%=unit.getCode()%></td>
                                        <%if(privShowQtyPrice){%>
                                            <td class="listgensell" width="10%"><%=Formater.formatNumber(receiveItem.getCost(),"##,###.00")%></td>
                                        <%}%>
                                        <td class="listgensell" width="5%"><%=currencyType.getCode()%></td>
                                        <td class="listgensell" width="5%"><%=String.valueOf(receiveItem.getQty())%></td>
                                        <%if(privShowQtyPrice){%>
                                            <td class="listgensell" width="15%" align="right"><%=Formater.formatNumber(receiveItem.getTotal(),"##,###.00")%></td>
                                        <%}%>
                                      </tr>
                                  <%}%>
                                </table>                              						                       							  
                                </td>
                              <%
                                }
                                catch(Exception e)
                                {
                                        System.out.println(e);
                                }
                                %>
                            </tr>
                          </table>
                        </td>
                      </tr>
                      <%if(listMatReceiveItem!=null && listMatReceiveItem.size()>0){%>
                          <%if(privShowQtyPrice){%>
                              <tr>
                                <td colspan="2" valign="top">&nbsp; </td>
                                <td width="27%" valign="top">
                                  <table width="100%" border="0">
                                    <tr>
                                      <td width="44%">
                                        <div align="right"><%="TOTAL : "+recCode%></div>
                                      </td>
                                      <td width="15%">
                                        <div align="right"></div>
                                      </td>
                                      <td width="41%">
                                        <div align="right">
                                          <%
                                                          String whereItem = ""+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]+"="+oidReceiveMaterial;
                                                          out.println(Formater.formatNumber(PstMatReceiveItem.getTotal(whereItem),"##,###.00"));
                                                          %>
                                        </div>
                                      </td>
                                    </tr>
                                  </table>
                                </td>
                              </tr>
                          <%}%>
                      <tr>
                        <td colspan="2" valign="top"><%=textListOrderHeader[SESS_LANGUAGE][5]%> : <%=rec.getRemark()%></td>
                        <td width="27%" valign="top">&nbsp;</td>
                      </tr>
                      <%if(listMatReceiveItem!=null && listMatReceiveItem.size()>0){%>
                      <%}%>
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
                        <td width="34%" align="center" nowrap>Pengirim,</td>
                        <td align="center" valign="bottom" width="33%">Mengetahui,</td>
                        <td width="33%" align="center">Penerima,</td>
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

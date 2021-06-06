<%-- 
    Document   : df_stockcode_delivery_order
    Created on : Dec 1, 2013, 8:07:33 PM
    Author     : dimata005
--%>

<%@page import="com.dimata.pos.entity.billing.Billdetail"%>
<%@page import="com.dimata.pos.entity.billing.PstBillDetail"%>
<%@page import="com.dimata.qdep.form.FRMHandler"%>
<%@ page import="com.dimata.gui.jsp.ControlList,
                 com.dimata.qdep.form.FRMMessage,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.util.Command,
                 com.dimata.posbo.entity.warehouse.*,
                 com.dimata.posbo.entity.masterdata.Material,
                 com.dimata.posbo.entity.masterdata.PstMaterial,
                 com.dimata.posbo.form.warehouse.*"%>
<%@ page language="java" %>

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_LOCATION_RECEIVE, AppObjInfo.OBJ_LOCATION_RECEIVE); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<!-- JSP Block -->
<%!
/* this constant used to list text of listHeader */
public static final String textListOrderHeader[][] =
{
	{"No","Lokasi","Tanggal","Time","Status","Supplier","Kategori","Sub Kategori","Keterangan"},
	{"No","Location","Date","Jam","Status","Supplier","Category","Sub Category","Remark"}
};

/* this constant used to list text of listMaterialItem */
public static final String textListOrderItem[][] =
{
	{"No","Kode Stok","Nilai Stok"},
	{"No","Stock Code","Value Stock"}
};

public DispatchStockCode isReady(Vector list, String strKode){
    DispatchStockCode dfStockCode = new DispatchStockCode();
    if(list!=null && list.size()>0){
        for(int k=0;k<list.size();k++){
            dfStockCode = (DispatchStockCode)list.get(k);
            System.out.println(dfStockCode+" = "+strKode);
            if(dfStockCode.getStockCode().equals(strKode)){
                return dfStockCode;
            }
        }
    }
    return dfStockCode;
}
/**
* this method used to maintain soList
*/
public String drawList(int language,int iCommand,FrmDispatchStockCode frmObject, DispatchStockCode objEntity, Vector classObj,
                                double max ,long soItemId,int start, Vector listObj, int sts, double valueHpp)
{
	ControlList ctrlist = new ControlList();
	ctrlist.setAreaWidth("30%");
	ctrlist.setListStyle("listgen");
	ctrlist.setTitleStyle("listgentitle");
	ctrlist.setCellStyle("listgensell");
	ctrlist.setHeaderStyle("listgentitle");
	ctrlist.addHeader(textListOrderItem[language][0],"5%");
	ctrlist.addHeader(textListOrderItem[language][1],"25%");
        ctrlist.addHeader(textListOrderItem[language][2],"25%");
	Vector lstData = ctrlist.getData();
	Vector rowx = new Vector(1,1);
	ctrlist.reset();
	ctrlist.setLinkRow(1);
	int index = -1;
	if(start<0){
	   start = 0;
	}

        for(int i=0; i< classObj.size(); i++){
            max=max-1;
            DispatchStockCode dispatchStockCode = (DispatchStockCode)classObj.get(i);
            rowx = new Vector();
            start = start + 1;
            rowx.add(""+start);
            if(sts==I_DocStatus.DOCUMENT_STATUS_POSTED){
                rowx.add(""+dispatchStockCode.getStockCode());
                rowx.add(""+dispatchStockCode.getStockValue());
            }else{
                rowx.add("<a href=\"javascript:deleteCode('"+dispatchStockCode.getOID()+"')\">"+dispatchStockCode.getStockCode()+"</a>");
                rowx.add(""+FRMHandler.userFormatStringDecimal(dispatchStockCode.getStockValue()));
            }
            lstData.add(rowx);
        }

        for(int i=0; i< max; i++){
            rowx = new Vector();
            String strCode = "";
            String strValue= "0.0";
            DispatchStockCode dispatchStockCode = new DispatchStockCode();
            strValue= FRMHandler.userFormatStringDecimal(valueHpp);
            //add opie-eyek untuk stock fifo
            start = start + 1;
            rowx.add(""+start);
            rowx.add("<div align=\"left\"><input name=\""+frmObject.fieldNames[FrmDispatchStockCode.FRM_FIELD_STOCK_CODE] +"_"+i+"_oid\" type=\"hidden\" value=\""+dispatchStockCode.getOID()+"\"><input type=\"text\" name=\""+frmObject.fieldNames[FrmDispatchStockCode.FRM_FIELD_STOCK_CODE] +"_"+i+"\" value=\""+strCode+"\" class=\"formElemen\"></div>");
            rowx.add("<div align=\"left\"><input name=\""+frmObject.fieldNames[FrmDispatchStockCode.FRM_FIELD_STOCK_VALUE] +"_"+i+"_oid\" type=\"hidden\" value=\""+dispatchStockCode.getOID()+"\"><input type=\"text\" name=\""+frmObject.fieldNames[FrmDispatchStockCode.FRM_FIELD_STOCK_VALUE] +"_"+i+"\" value=\""+strValue+"\" class=\"formElemen\"></div>");
            lstData.add(rowx);
	}
	return ctrlist.draw();
}

%>
<%
/**
* get request data from current form
*/
ControlLine ctrLine = new ControlLine();
int iCommand = FRMQueryString.requestCommand(request);
int prevCommand = FRMQueryString.requestInt(request,"prev_command");
int start = FRMQueryString.requestInt(request,"start");
int type = FRMQueryString.requestInt(request,"type_doc");
int cmdItem = FRMQueryString.requestInt(request,"command_item");
int appCommand = FRMQueryString.requestInt(request,"approval_command");
long oidSourceStockCodeId = FRMQueryString.requestLong(request, "hidden_source_stock_code_id");
long oidDispatchMaterialId = FRMQueryString.requestLong(request, "hidden_dispatch_item_id");
long oidDispatchId = FRMQueryString.requestLong(request, "hidden_dispatch_id");

long oidCashBillMain = FRMQueryString.requestLong(request, "hidden_bill_main_id");

int iErrCode = FRMMessage.NONE;
String msgString = "";

//DispatchStockCode stockCode = new DispatchStockCode();
MatDispatch matDispatch = new MatDispatch();
MatDispatchItem matDispatchItem = new MatDispatchItem();
Material material = new Material();
try{
	matDispatchItem = PstMatDispatchItem.fetchExc(oidDispatchMaterialId);
        matDispatch = PstMatDispatch.fetchExc(matDispatchItem.getDispatchMaterialId());
	material = PstMaterial.fetchExc(matDispatchItem.getMaterialId());
}catch(Exception e){}

double max = matDispatchItem.getQty();
double valueHpp = matDispatchItem.getHpp();

CtrDispatchStockCode ctrDispatchStockCode = new CtrDispatchStockCode(request);
iErrCode = ctrDispatchStockCode.action(Command.STOP,oidSourceStockCodeId);
FrmDispatchStockCode frmDispatchStockCode = ctrDispatchStockCode.getForm();
DispatchStockCode sourceStockCode = ctrDispatchStockCode.getDispatchStockCode();

//mencari id dari cashBillDetail
long cashBillDetailId= PstBillDetail.getCashBillDetailId(oidCashBillMain, matDispatchItem.getMaterialId());


//update opie-eyek 20130910
Vector vStock = PstDispatchStockCode.getSerialCodeDispatchBy(material.getOID(),matDispatch.getCashBillMainId());
    if (iCommand == Command.SAVE){
        ctrDispatchStockCode.requestBarcodeDeliveryOrder(matDispatchItem.getQty(),matDispatchItem.getOID(),request,cashBillDetailId);
        iCommand = Command.ADD;
    }else if(iCommand == Command.DELETE){
        ctrDispatchStockCode.action(iCommand,oidSourceStockCodeId,cashBillDetailId);
        iCommand = Command.ADD;
    }

int recordToGetItem = 20;
int vectSize = 0;
Vector listObject  = new Vector();
if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST){
	start = ctrDispatchStockCode.actionList(iCommand,start,vectSize,recordToGetItem);
}

Vector listDispatchStockCode= PstDispatchStockCode.list(0,0,PstDispatchStockCode.fieldNames[PstDispatchStockCode.FLD_DISPATCH_MATERIAL_ITEM_ID]+"="+matDispatchItem.getOID(),"");
if(listDispatchStockCode.size() < 1 && start >0){
	 if(vectSize-recordToGetItem > recordToGetItem){
	    start = start - recordToGetItem;
	 }else{
	    start = 0 ;
		iCommand = Command.FIRST;
	    prevCommand = Command.FIRST;
	}
	listDispatchStockCode = PstDispatchStockCode.list(0,0,PstDispatchStockCode.fieldNames[PstDispatchStockCode.FLD_DISPATCH_MATERIAL_ITEM_ID]+"="+matDispatchItem.getOID(),"");
}

String where = "";
where = PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_LOCATION_ID]+"="+matDispatch.getLocationId()+
        " AND "+PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_MATERIAL_ID]+"="+matDispatchItem.getMaterialId()+
        " AND "+PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_STOCK_STATUS]+"="+PstMaterialStockCode.FLD_STOCK_STATUS_GOOD;

Vector list = PstMaterialStockCode.list(0,0,where,PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_STOCK_CODE]);

%>

<!-- End of JSP Block -->
<html>
<!-- DW6 -->
<head>
<title>Dimata - ProChain POS</title>
<script language="JavaScript" type="text/JavaScript">

function cmdBack(){
    document.frm_stockcode.command.value="<%=Command.EDIT%>";
    document.frm_stockcode.prev_command.value="<%=prevCommand%>";
    document.frm_stockcode.hidden_dispatch_id.value="<%=oidDispatchId%>";
    document.frm_stockcode.action="delivery_order_material_edit.jsp";
    document.frm_stockcode.submit();
}

function deleteCode(oidItem){
    var isTrue = confirm("Anda yakin ingin menghapus ?")
    if (isTrue){
        document.frm_stockcode.hidden_source_stock_code_id.value=oidItem;
        document.frm_stockcode.command.value="<%=Command.DELETE%>";
        document.frm_stockcode.prev_command.value="<%=prevCommand%>";
        document.frm_stockcode.action="df_stockcode_delivery_order.jsp";
        document.frm_stockcode.submit();
    }
}

function cmdSave(){
	document.frm_stockcode.command.value="<%=Command.SAVE%>";
	document.frm_stockcode.prev_command.value="<%=prevCommand%>";
	document.frm_stockcode.action="df_stockcode_delivery_order.jsp";
	document.frm_stockcode.submit();
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
</script>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="../../../styles/main.css" type="text/css">
<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">




<!-- Copyright 2000, 2001, 2002, 2003 Macromedia, Inc. All rights reserved. -->
</head>

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
  <tr>
    <td width="88%" valign="top" align="left">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="3%">&nbsp;</td>
          <td width="97%">
            <form name="frm_stockcode" method="post" action="">
                    <input name="command" type="hidden" value="<%=iCommand%>">
                    <input name="prev_command" type="hidden" value="<%=prevCommand%>">
                    <input name="hidden_dispatch_item_id" type="hidden" value="<%=oidDispatchMaterialId%>">
                    <input name="hidden_source_stock_code_id" type="hidden" value="<%=oidSourceStockCodeId%>">
                    <input name="type_doc" type="hidden" value="<%=type%>">
                    <input name="start" type="hidden" value="<%=start%>">
                    <input name="hidden_dispatch_id" type="hidden" value="<%=oidDispatchId%>">
                    <input name="hidden_bill_main_id" type="hidden" value="<%=oidCashBillMain%>">

              <table width="100%"  border="0" cellspacing="1" cellpadding="1">
                <tr>
                  <td width="2%"><table width="100%"  border="0" cellspacing="1" cellpadding="1">
                    <tr>
                      <td colspan="2" class="mainheader">Serial Kode</td>
                    </tr>
                    <tr>
                      <td width="10%" class="formElemen">SKU</td>
                      <td width="90%" class="formElemen">: <%=material.getSku()%></td>
                    </tr>
                    <tr>
                      <td class="formElemen">Name</td>
                      <td class="formElemen">: <%=material.getName()%></td>
                    </tr>
                     <tr>
                      <td class="formElemen">Qty Receive </td>
                      <td class="formElemen">: <%=max%></td>
                    </tr>
                    <tr>
                      <td height="22">&nbsp;</td>
                      <td>&nbsp;</td>
                    </tr>
                  </table></td>
                </tr>
                <tr>
                  <td><%= drawList(SESS_LANGUAGE,iCommand,frmDispatchStockCode, sourceStockCode ,listDispatchStockCode,max,oidDispatchMaterialId,start,vStock,matDispatch.getDispatchStatus(),valueHpp)%></td>
                </tr>
                <tr>
                  <td><span class="command">
                    <%
								int cmd = 0;
								if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand == Command.NEXT || iCommand==Command.LAST){
									cmd =iCommand;
								}else{
								    if(iCommand == Command.NONE || prevCommand == Command.NONE)
										cmd = Command.FIRST;
								    else
										cmd =prevCommand;
							    }
                                ctrLine.setLocationImg(approot+"/images");
							   	ctrLine.initDefault();
								out.println(ctrLine.drawImageListLimit(cmd,vectSize,start,recordToGetItem));
								%>
                  </span></td>
                </tr>
                <tr>
                  <td><%=msgString%></td>
                </tr>
                <tr>
                  <td><%
                            ctrLine.setLocationImg(approot+"/images");
                            ctrLine.initDefault();
                            ctrLine.setTableWidth("80%");
                            if(matDispatch.getDispatchStatus() ==I_DocStatus.DOCUMENT_STATUS_POSTED){
                                ctrLine.setSaveCaption("");
                            }else{
                                ctrLine.setSaveCaption("Save Stock Code");
                                ctrLine.setAddCaption("Add Serial Kode");
                                ctrLine.setAddCommand("Add Serial Code");
                            }
                            ctrLine.setDeleteCaption("");
                            ctrLine.setBackCaption("Back to item");
							ctrLine.setCommandStyle("command");
							ctrLine.setColCommStyle("command");

							if(iCommand==Command.SAVE && frmDispatchStockCode.errorSize()==0){
								iCommand=Command.EDIT;
							}
							%>
                    <%=ctrLine.drawImage(iCommand,iErrCode,msgString)%> </td>
                </tr>
                <%if(list!=null && list.size()>0) {%>
                <tr align="left" valign="top" id="hideserial">
                  <td height="22" valign="middle" colspan="3">&nbsp;&nbsp;<table width="30%"  border="0" cellpadding="1" cellspacing="1" class="listgen">
					<tr align="center" class="listgensell">
                      <td width="16%">No</td>
                      <td width="84%">Serial Number (in Stock)</td>
                    </tr>
					<%
					for(int k=0;k<list.size();k++){
						MaterialStockCode materialStockCode = (MaterialStockCode)list.get(k);
                    %>

                    <tr class="listgensell">
                      <td>&nbsp;<%=(k+1)%></td>
                      <td>&nbsp;<%=materialStockCode.getStockCode()%></td>
                    </tr>
					<%
					}
					%>
                  </table></td>
                </tr>
                <%
                }
                %>
              </table>
		  </form></td>
        </tr>
      </table>
    </td>
  </tr>
</table>
</body>
</html>

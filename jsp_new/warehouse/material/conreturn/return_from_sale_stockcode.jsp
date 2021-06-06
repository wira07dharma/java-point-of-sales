<%-- 
    Document   : return_from_sale_stockcode
    Created on : Jan 14, 2014, 6:22:04 AM
    Author     : dimata005
--%>

<%@page import="com.dimata.pos.form.billing.FrmBillDetail"%>
<%@page import="com.dimata.pos.entity.billing.PstBillMain"%>
<%@page import="com.dimata.pos.entity.billing.PstBillDetail"%>
<%@page import="com.dimata.pos.entity.billing.Billdetail"%>
<%@page import="com.dimata.pos.entity.billing.BillMain"%>
<%@page import="com.dimata.pos.entity.billing.PstBillDetailCode"%>
<%@page import="com.dimata.pos.form.billing.FrmBillDetailCode"%>
<%@page import="com.dimata.pos.entity.billing.BillDetailCode"%>
<%@page import="com.dimata.pos.form.billing.CtrlBillDetailCode"%>
<%@page import="com.dimata.posbo.ajax.CheckStockCode"%>
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
	{"No","Kode Stok",""},
	{"No","Stock Code",""}
};

public BillDetailCode isReady(Vector list, String strKode){
    BillDetailCode billDetailCode = new BillDetailCode();
    if(list!=null && list.size()>0){
        for(int k=0;k<list.size();k++){
            billDetailCode = (BillDetailCode)list.get(k);
            System.out.println(billDetailCode+" = "+strKode);
            if(billDetailCode.getStockCode().equals(strKode)){
                return billDetailCode;
            }
        }
    }
    return billDetailCode;
}
/**
* this method used to maintain soList
*/
public String drawList(int language,int iCommand,FrmBillDetailCode frmObject,
                                 BillDetailCode objEntity, Vector classObj,
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
            BillDetailCode billDetailCode = (BillDetailCode)classObj.get(i);
            rowx = new Vector();
            start = start + 1;
            rowx.add(""+start);
            if(sts!=I_DocStatus.DOCUMENT_STATUS_DRAFT){
                rowx.add(""+billDetailCode.getStockCode());
                rowx.add("");
            }else{
                rowx.add("<a href=\"javascript:deleteCode('"+billDetailCode.getOID()+"')\">"+billDetailCode.getStockCode()+"</a>");
                rowx.add("");
            }
            lstData.add(rowx);
        }

        for(int i=0; i< max; i++){
            rowx = new Vector();
            String strCode = "";
            String strValue= "";
            ReceiveStockCode sourceStockCode = new ReceiveStockCode();
            strValue= FRMHandler.userFormatStringDecimal(valueHpp);
            //add opie-eyek untuk stock fifo
            start = start + 1;
            rowx.add(""+start);

            //dyas 20131130
            //tambah perintah onkeyup untuk memberikan perintah jika user menekan enter,
            //perintah untuk memanggil showData
            rowx.add("<div align=\"left\"><input name=\""+frmObject.fieldNames[FrmBillDetailCode.FRM_FIELD_STOCK_CODE] +"_"+i+"_oid\" type=\"hidden\" value=\""+sourceStockCode.getOID()+"\"><input type=\"text\" name=\""+frmObject.fieldNames[FrmBillDetailCode.FRM_FIELD_STOCK_CODE] +"_"+i+"\" value=\""+strCode+"\" onkeyup=\"if (event.keyCode == 13) { showData(this.value); return false; }\" class=\"formElemen\"></div>");
            rowx.add("<div align=\"left\"><input name=\""+frmObject.fieldNames[FrmBillDetailCode.FRM_FIELD_STOCK_VALUE] +"_"+i+"_oid\" type=\"hidden\" value=\""+sourceStockCode.getOID()+"\"><input type=\"text\" name=\""+frmObject.fieldNames[FrmBillDetailCode.FRM_FIELD_STOCK_VALUE] +"_"+i+"\" value=\""+strValue+"\" class=\"formElemen\"></div>");
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
long oidBillMainOid = FRMQueryString.requestLong(request, "hidden_billmain_id");
//long oidReceive = FRMQueryString.requestLong(request, "hidden_receive_id");
long oidSourceStockCodeId = FRMQueryString.requestLong(request, "hidden_source_stock_code_id");
long oidCashBillDetailId = FRMQueryString.requestLong(request,"hidden_cash_bill_detail_id");

int iErrCode = FRMMessage.NONE;
String msgString = "";

//DispatchStockCode stockCode = new DispatchStockCode();
BillMain billMain = new BillMain();
Billdetail billdetail = new Billdetail();
Material material = new Material();
BillMain billMainParent = new BillMain();
Billdetail billdetailParent = new Billdetail();
try{
	billdetail = PstBillDetail.fetchExc(oidCashBillDetailId);
        billMain = PstBillMain.fetchExc(billdetail.getBillMainId());
	material = PstMaterial.fetchExc(billdetail.getMaterialId());
        oidBillMainOid=billMain.getOID();

}catch(Exception e){}

double max = billdetail.getQty();
double valueHpp = billdetail.getCost();

CtrlBillDetailCode ctrlBillDetailCode = new CtrlBillDetailCode(request);
iErrCode = ctrlBillDetailCode.action(Command.STOP,oidSourceStockCodeId,oidSourceStockCodeId);
FrmBillDetailCode frmBillDetailCode = ctrlBillDetailCode.getForm();
BillDetailCode sourceStockCode = ctrlBillDetailCode.getBillDetailCode();

//update opie-eyek 20130910
Vector vStock = PstReceiveStockCode.getSerialCodeReceiveBy(material.getOID(),billdetail.getOID());
    if (iCommand == Command.SAVE){
        //dyas 20131130
        //tambah variabel oidReceive
        ctrlBillDetailCode.requestBarcode(billdetail.getQty(),billdetail.getOID(),request,oidBillMainOid);
        iCommand = Command.ADD;
    }else if(iCommand == Command.DELETE){
        ctrlBillDetailCode.action(iCommand,oidSourceStockCodeId,0);
        iCommand = Command.ADD;
    }

int recordToGetItem = 20;
int vectSize = 0;
Vector listObject  = new Vector();
if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST){
	start = ctrlBillDetailCode.actionList(iCommand,start,vectSize,recordToGetItem);
}

Vector listBillingStockCode = PstBillDetailCode.list(0,0,PstBillDetailCode.fieldNames[PstBillDetailCode.FLD_SALE_ITEM_ID]+"="+billdetail.getOID(),"");
if(listBillingStockCode.size() < 1 && start >0){
	 if(vectSize-recordToGetItem > recordToGetItem){
	    start = start - recordToGetItem;
	 }else{
	    start = 0 ;
		iCommand = Command.FIRST;
	    prevCommand = Command.FIRST;
	}
	listBillingStockCode = PstBillDetailCode.list(0,0,PstBillDetailCode.fieldNames[PstBillDetailCode.FLD_SALE_ITEM_ID]+"="+billdetail.getOID(),"");
}

//mencari serial number dari item retur yang di retur
Vector list = PstBillDetailCode.getSerialCodeReturSale(material.getOID(),billMain.getParentId());

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
        document.frm_stockcode.hidden_billmain_id="<%=oidBillMainOid%>";
        document.frm_stockcode.action="return_from_sale_material_edit.jsp";
	document.frm_stockcode.submit();
}

function deleteCode(oidItem){
    var isTrue = confirm("Anda yakin ingin menghapus ?")
    if (isTrue){
        document.frm_stockcode.hidden_source_stock_code_id.value=oidItem;
        document.frm_stockcode.command.value="<%=Command.DELETE%>";
        document.frm_stockcode.prev_command.value="<%=prevCommand%>";
        document.frm_stockcode.action="return_from_sale_stockcode.jsp";
        document.frm_stockcode.submit();
    }
}

function cmdSave(){
	document.frm_stockcode.command.value="<%=Command.SAVE%>";
	document.frm_stockcode.prev_command.value="<%=prevCommand%>";
	document.frm_stockcode.action="return_from_sale_stockcode.jsp";
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

<!--
    dyas 20131130
-tambah script untuk panggil jquery n javascript
-buat function showData n checkAjax untuk melakukan pengecekan pada stock code yang diinputkan oleh user
-->
<script src="../../../styles/jquery.min.js"></script>
<script type="text/javascript">
//fungsi ajax
function showData(value){
    //buatkan default value = 0
    var stateCheck = document.frm_stockcode.hidden_state.value;
    //alert(stateCheck);
    //jika stateCheck==0, cek fungsi search ajax nya
    if(stateCheck=="0"){
        checkAjax(value);
    }else{
        document.frm_stockcode.hidden_state.value="0";
        //cmdSave();
        //return false;
    }
}

function checkAjax(value){
    $.ajax({
    url : "<%=approot%>/servlet/com.dimata.posbo.ajax.CheckStockCode?<%=FrmReceiveStockCode.fieldNames[FrmReceiveStockCode.FRM_FIELD_STOCK_CODE]%>="+value+"&<%=FrmBillDetailCode.fieldNames[FrmBillDetailCode.FRM_FIELD_CASH_BILL_DETAIL_ID]%>=<%=oidCashBillDetailId%>&<%=CheckStockCode.CHECK_STOCK_CODE_NAME%>=<%=CheckStockCode.CHECK_STOCK_CODE_RETURN_SALE%>&<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_MATERIAL_ID]%>=<%=material.getOID()%>&<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_CASH_BILL_MAIN_ID]%>=<%=billMain.getParentId()%>",
    type : "POST",
    async : false,
    
    success : function(data) {
        //Do something with the data here untuk fungsi success
        // var result=data;
        if($.trim(data)!="false"){
           //jika data yang dikirim lain dari kata succses maka munculkan resultnya
           alert(data);
           //dan ubah default value hidden_state menjadi 1
           document.frm_stockcode.hidden_state.value="1";
        }else{
            // jika enggak succsess maka ubah default value hidden_state menjadi 0
            document.frm_stockcode.hidden_state.value="0";
            cmdSave();
        }
    }
   // alert(url);
    //ini kalau pengen respon ga pakai alert tapi di tampilkan di halamannya
    /*error: function(xhr, status, error) {
            //clear old error message, then display the new php error message
            $("#divErrorMessages").empty().append(status);
            $("#divErrorMessages").append(xhr.responseText);
        },
        success: function(results) {
            // clear the error message first
            $("#divErrorMessages").empty();

            // clear old results, then display the new results
            $("#divResults").empty().append(results);
        }*/
});

}

function setFocus()
{
document.getElementById("hidden_receive_id").focus();
}
</script>



<!-- Copyright 2000, 2001, 2002, 2003 Macromedia, Inc. All rights reserved. -->
</head>

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
  <tr>
    <td width="88%" valign="top" align="left">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td width="3%">&nbsp;</td>
          <td width="97%"><%@include file="../../../styletemplate/template_header_empty.jsp" %>
              <form name="frm_stockcode" method="post" action="">
		     <input name="hidden_cash_bill_detail_id" type="hidden" value="<%=oidCashBillDetailId%>">
                    <input name="command" type="hidden" value="<%=iCommand%>">
                    <input name="prev_command" type="hidden" value="<%=prevCommand%>">
                    <input name="hidden_source_stock_code_id" type="hidden" value="<%=oidSourceStockCodeId%>">
                    <input name="type_doc" type="hidden" value="<%=type%>">
                    <input name="start" type="hidden" value="<%=start%>">
                    <input name="hidden_billmain_id" type="hidden" value="<%=oidBillMainOid%>">
                    <!--dyas 20131130
                    tambah hidden_state-->
                    <input name="hidden_state" type="hidden" value="0">
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
                    <tr>
                      <td class="comment">1 qty = 1 Stock Code </td>
                      <div id="divResults"></div>
                      <div id="divErrorMessages"></div>
                    </tr>
                  </table></td>
                </tr>
                <tr>
                  <td><%= drawList(SESS_LANGUAGE,iCommand,frmBillDetailCode, sourceStockCode ,listBillingStockCode,max,oidCashBillDetailId,start,vStock,billMain.getBillStatus(),valueHpp)%></td>
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
                <%if(list!=null && list.size()>0) {%>
                <tr align="left" valign="top" id="hideserial">
                  <td height="22" valign="middle" colspan="3">&nbsp;&nbsp;<table width="30%"  border="0" cellpadding="1" cellspacing="1" class="listgen">
					<tr align="center" class="listgensell">
                      <td width="16%">No</td>
                      <td width="84%">Serial Number (in Stock)</td>
                    </tr>
					<%
					for(int k=0;k<list.size();k++){
						BillDetailCode billDetailCode = (BillDetailCode)list.get(k);
                    %>

                    <tr class="listgensell">
                      <td>&nbsp;<%=(k+1)%></td>
                      <td>&nbsp;<%=billDetailCode.getStockCode()%></td>
                    </tr>
					<%
					}
					%>
                  </table></td>
                </tr>
                <%
                }
                %>
                <tr>
                  <td><%
                            ctrLine.setLocationImg(approot+"/images");
                            ctrLine.initDefault();
                            ctrLine.setTableWidth("80%");
                            if(billMain.getBillStatus()!=I_DocStatus.DOCUMENT_STATUS_DRAFT){
                                ctrLine.setSaveCaption("");
                            }else{
                                ctrLine.setSaveCaption("");
                                ctrLine.setAddCaption("Add Serial Kode");
                                ctrLine.setAddCommand("Add Serial Code");
                            }
                            ctrLine.setDeleteCaption("");
                            ctrLine.setBackCaption("Back to item");
							ctrLine.setCommandStyle("command");
							ctrLine.setColCommStyle("command");

							if(iCommand==Command.SAVE && frmBillDetailCode.errorSize()==0){
								iCommand=Command.EDIT;
							}
							%>
                    <%=ctrLine.drawImage(iCommand,iErrCode,msgString)%> </td>
                </tr>
              </table>
            </form>
                <script type="text/javascript">
                        document.all.<%=FrmBillDetailCode.fieldNames[FrmBillDetailCode.FRM_FIELD_STOCK_CODE]%>_0.focus();
                </script>

          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
</body>
</html>



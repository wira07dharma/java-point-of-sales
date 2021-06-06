<%-- 
    Document   : return_wh_stockcode.jsp
    Created on : Nov 24, 2013, 11:30:00 PM
    Author     : dimata005
--%>

<%@page import="com.dimata.posbo.form.warehouse.FrmMatReturn"%>
<%@page import="com.dimata.posbo.ajax.CheckStockCode"%>
<%@page import="com.dimata.posbo.entity.warehouse.MaterialStockCode"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMaterialStockCode"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstReturnStockCode"%>
<%@page import="com.dimata.posbo.form.warehouse.CtrReturnStockCode"%>
<%@page import="com.dimata.posbo.entity.warehouse.ReceiveStockCode"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterial"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatReturn"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatReturnItem"%>
<%@page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatReturnItem"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatReturn"%>
<%@page import="com.dimata.qdep.form.FRMMessage"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.gui.jsp.ControlLine"%>
<%@page import="com.dimata.gui.jsp.ControlList"%>
<%@page import="com.dimata.posbo.entity.warehouse.ReturnStockCode"%>
<%@page import="com.dimata.posbo.form.warehouse.FrmReturnStockCode"%>
<%@page import="com.dimata.qdep.form.FRMHandler"%>
<%@ page language="java" %>

<%@ include file = "../../../main/javainit.jsp" %>
<%
int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_LOCATION_RECEIVE, AppObjInfo.OBJ_LOCATION_RECEIVE);
int  appObjCodeShowQtyAndPrice = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE_QTY_AND_PRICE);
%>
<%@ include file = "../../../main/checkuser.jsp" %>
<%
boolean privShowQtyPrice = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeShowQtyAndPrice, AppObjInfo.COMMAND_VIEW));
%>
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
	{"No","Kode Stok","Stock Value",""},
	{"No","Stock Code","Stock Value",""}
};

/**
* this method used to maintain soList
*/
public String drawList(int language,int iCommand,FrmReturnStockCode frmObject,
                                 ReturnStockCode objEntity, Vector classObj,
                                double max ,long soItemId,int start, Vector listObj, int sts, double valueHpp,boolean privShowQtyPrice)
{
	ControlList ctrlist = new ControlList();
	ctrlist.setAreaWidth("30%");
	ctrlist.setListStyle("listgen");
	ctrlist.setTitleStyle("listgentitle");
	ctrlist.setCellStyle("listgensell");
	ctrlist.setHeaderStyle("listgentitle");
	ctrlist.addHeader(textListOrderItem[language][0],"5%");
	ctrlist.addHeader(textListOrderItem[language][1],"25%");
        if(privShowQtyPrice){
            ctrlist.addHeader(textListOrderItem[language][2],"25%");
        }else{
            ctrlist.addHeader(textListOrderItem[language][3],"25%");
        }
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
            ReturnStockCode returnStockCode = (ReturnStockCode)classObj.get(i);
            rowx = new Vector();
            start = start + 1;
            rowx.add(""+start);
            if(sts!=I_DocStatus.DOCUMENT_STATUS_DRAFT){
                rowx.add(""+returnStockCode.getStockCode());
                if(privShowQtyPrice){
                     rowx.add(""+FRMHandler.userFormatStringDecimal(returnStockCode.getStockValue()));
                }else{
                    rowx.add("");
                }
            }else{
                rowx.add("<a href=\"javascript:deleteCode('"+returnStockCode.getOID()+"')\">"+returnStockCode.getStockCode()+"</a>");
                if(privShowQtyPrice){
                     rowx.add(""+FRMHandler.userFormatStringDecimal(returnStockCode.getStockValue()));
                }else{
                     rowx.add("");
                }
            }
            lstData.add(rowx);
        }

        for(int i=0; i< max; i++){
            rowx = new Vector();
            String strCode = "";
            String strValue= "";
            ReturnStockCode returnStockCode = new ReturnStockCode();
            //add opie-eyek untuk stock fifo
            start = start + 1;
            rowx.add(""+start);

            //dyas 20131202
            //tambah onkeyup untuk panggil function showData
            rowx.add("<div align=\"left\"><input name=\""+frmObject.fieldNames[FrmReturnStockCode.FRM_FIELD_STOCK_CODE] +"_"+i+"_oid\" type=\"hidden\" value=\""+returnStockCode.getOID()+"\"><input type=\"text\" name=\""+frmObject.fieldNames[FrmReturnStockCode.FRM_FIELD_STOCK_CODE] +"_"+i+"\" value=\""+strCode+"\" onkeyup=\"if (event.keyCode == 13) { showData(this.value); return false; }\" class=\"formElemen\"></div>");
            rowx.add("<div align=\"left\"><input name=\""+frmObject.fieldNames[FrmReturnStockCode.FRM_FIELD_STOCK_VALUE] +"_"+i+"_oid\" type=\"hidden\" value=\""+returnStockCode.getOID()+"\"><input type=\"text\" name=\""+frmObject.fieldNames[FrmReturnStockCode.FRM_FIELD_STOCK_VALUE] +"_"+i+"\" value=\""+strValue+"\" class=\"formElemen\"></div>");
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
long oidReturnId = FRMQueryString.requestLong(request, "hidden_return_id");
long oidMatReturnItem = FRMQueryString.requestLong(request,"hidden_return_item_id");

long oidSourceStockCodeId = FRMQueryString.requestLong(request, "hidden_source_stock_code_id");

int iErrCode = FRMMessage.NONE;
String msgString = "";

//DispatchStockCode stockCode = new DispatchStockCode();
MatReturn matReturn = new MatReturn();
MatReturnItem matReturnItem = new MatReturnItem();
Material material = new Material();
try{
	matReturnItem = PstMatReturnItem.fetchExc(oidMatReturnItem);
        matReturn = PstMatReturn.fetchExc(matReturnItem.getReturnMaterialId());
	material = PstMaterial.fetchExc(matReturnItem.getMaterialId());
}catch(Exception e){}

double max = matReturnItem.getQty();
double valueHpp = matReturnItem.getCost();

CtrReturnStockCode ctrReturnStockCode = new CtrReturnStockCode(request);
iErrCode = ctrReturnStockCode.action(Command.STOP,oidSourceStockCodeId);
FrmReturnStockCode frmDispatchStockCode = ctrReturnStockCode.getForm();
ReturnStockCode returnStockCode = ctrReturnStockCode.getReturnStockCode();

//update opie-eyek 20130910
Vector vStock = PstReturnStockCode.getSerialCodeReturnBy(material.getOID(),matReturn.getReceiveMaterialId());
    if (iCommand == Command.SAVE){
        //dyas 20131202
        //tambah variabel oidReturnId
        ctrReturnStockCode.requestBarcode(matReturnItem.getQty(),matReturnItem.getOID(),request,oidReturnId);
        iCommand = Command.ADD;
    }else if(iCommand == Command.DELETE){
        ctrReturnStockCode.action(iCommand,oidSourceStockCodeId);
        iCommand = Command.ADD;
    }

int recordToGetItem = 20;
int vectSize = 0;
Vector listObject  = new Vector();
if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST){
	start = ctrReturnStockCode.actionList(iCommand,start,vectSize,recordToGetItem);
}

Vector listReturnStockCode = PstReturnStockCode.list(0,0,PstReturnStockCode.fieldNames[PstReturnStockCode.FLD_RETURN_MATERIAL_ITEM_ID]+"="+matReturnItem.getOID(),"");
if(listReturnStockCode.size() < 1 && start >0){
	 if(vectSize-recordToGetItem > recordToGetItem){
	    start = start - recordToGetItem;
	 }else{
	    start = 0 ;
		iCommand = Command.FIRST;
	    prevCommand = Command.FIRST;
	}
	listReturnStockCode = PstReturnStockCode.list(0,0,PstReturnStockCode.fieldNames[PstReturnStockCode.FLD_RETURN_MATERIAL_ITEM_ID]+"="+matReturn.getOID(),"");
}

//list serial number yang bisa di return
String where = "";
where = PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_LOCATION_ID]+"="+matReturn.getLocationId()+
        " AND "+PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_MATERIAL_ID]+"="+matReturnItem.getMaterialId()+
        " AND "+PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_STOCK_STATUS]+"="+PstMaterialStockCode.FLD_STOCK_STATUS_GOOD;
Vector list = PstMaterialStockCode.list(0,0,where,PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_STOCK_DATE]);

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
        document.frm_stockcode.hidden_return_id.value="<%=oidReturnId%>";
        <%if(type==0){%>
	     document.frm_stockcode.action="return_wh_material_edit.jsp";
        <%}else if(type==1){%>
            document.frm_stockcode.action="return_wh_supp_material_edit.jsp";
        <%}%>
	document.frm_stockcode.submit();
}

function deleteCode(oidItem){
    var isTrue = confirm("Anda yakin ingin menghapus ?")
    if (isTrue){
        document.frm_stockcode.hidden_source_stock_code_id.value=oidItem;
        document.frm_stockcode.command.value="<%=Command.DELETE%>";
        document.frm_stockcode.prev_command.value="<%=prevCommand%>";
        document.frm_stockcode.action="return_wh_stockcode.jsp";
        document.frm_stockcode.submit();
    }
}

function cmdSave(){
	document.frm_stockcode.command.value="<%=Command.SAVE%>";
	document.frm_stockcode.prev_command.value="<%=prevCommand%>";
	document.frm_stockcode.action="return_wh_stockcode.jsp";
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
    dyas 20131202
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
    url : "<%=approot%>/servlet/com.dimata.posbo.ajax.CheckStockCode?<%=FrmReturnStockCode.fieldNames[FrmReturnStockCode.FRM_FIELD_STOCK_CODE]%>="+value+"&<%=FrmMatReturn.fieldNames[FrmMatReturn.FRM_FIELD_RETURN_MATERIAL_ID]%>=<%=oidReturnId%>&<%=CheckStockCode.CHECK_STOCK_CODE_NAME%>=<%=CheckStockCode.CHECK_STOCK_CODE_RETURN%>",
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
document.getElementById("hidden_return_id").focus();
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
            <%@include file="../../../styletemplate/template_header_empty.jsp" %>
          <td width="3%">&nbsp;</td>
          <td width="97%">
                <form name="frm_stockcode" method="post" action="">
                <input name="command" type="hidden" value="<%=iCommand%>">
                <input name="prev_command" type="hidden" value="<%=prevCommand%>">
                <input name="hidden_return_id" type="hidden" value="<%=oidReturnId%>">
                <input name="hidden_source_stock_code_id" type="hidden" value="<%=oidSourceStockCodeId%>">
                <input name="type_doc" type="hidden" value="<%=type%>">
                <input name="start" type="hidden" value="<%=start%>">
                <input name="hidden_return_item_id" type="hidden" value="<%=oidMatReturnItem%>">
<!--                    dyas 20131202
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
                    <%--language,int iCommand,FrmReturnStockCode frmObject,ReturnStockCode objEntity, Vector classObj,double max ,long soItemId,int start, Vector listObj, int sts, double valueHpp--%>
                  <td><%= drawList(SESS_LANGUAGE,iCommand,frmDispatchStockCode, returnStockCode ,listReturnStockCode,max,oidMatReturnItem,start,vStock,matReturn.getReturnStatus(),valueHpp,privShowQtyPrice)%></td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                </tr>
                <%if(list!=null && list.size()>0) {
                                    %>
                    <tr align="left" valign="top" id="hideserial">
                      <td height="22" valign="middle" colspan="3">&nbsp;&nbsp;<table width="30%"  border="0" cellpadding="1" cellspacing="1" class="listgen">
                       <tr align="center" class="listgentitle">
                          <td width="16%">No</td>
                          <td width="54%">Serial Number (in Stock)</td>
                          <% if(privShowQtyPrice){%>
                            <td width="30%">Value Stock</td>
                          <%}%>
                        </tr>
                                            <%
                                            for(int k=0;k<list.size();k++){
                                                    MaterialStockCode materialStockCode = (MaterialStockCode)list.get(k);
                        %>

                        <tr class="listgensell">
                          <td>&nbsp;<%=(k+1)%></td>
                          <td>&nbsp;<%=materialStockCode.getStockCode()%></td>
                           <% if(privShowQtyPrice){%>
                            <td>&nbsp;<%=FRMHandler.userFormatStringDecimal(materialStockCode.getStockValue()) %></td>
                          <%}%>
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
                            if(matReturn.getReturnStatus() ==I_DocStatus.DOCUMENT_STATUS_POSTED || matReturn.getReturnStatus() ==I_DocStatus.DOCUMENT_STATUS_CLOSED){
                                ctrLine.setSaveCaption("");
                            }else{
                               // ctrLine.setSaveCaption("Save Stock Code");
                                 ctrLine.setSaveCaption("");
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
              </table>
		  </form>
                <script type="text/javascript">
                        document.all.<%=FrmReturnStockCode.fieldNames[FrmReturnStockCode.FRM_FIELD_STOCK_CODE]%>_0.focus();
                </script>

          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
</body>
</html>


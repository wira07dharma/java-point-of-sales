<%@page import="com.dimata.posbo.ajax.CheckStockCode"%>
<%@page import="com.dimata.posbo.form.warehouse.FrmMatStockOpname"%>
<%@page import="com.dimata.qdep.form.FRMHandler"%>
<%@ page import="com.dimata.posbo.form.warehouse.FrmSourceStockCode,
                 com.dimata.posbo.entity.warehouse.SourceStockCode,
                 com.dimata.posbo.form.warehouse.CtrSourceStockCode,
                 com.dimata.posbo.entity.warehouse.PstSourceStockCode,
                 com.dimata.posbo.entity.warehouse.MatStockOpnameItem,
                 com.dimata.posbo.entity.warehouse.PstMatStockOpnameItem,
                 com.dimata.posbo.entity.masterdata.*,
                 com.dimata.posbo.entity.warehouse.MatStockOpname,
                 com.dimata.posbo.entity.warehouse.PstMatStockOpname,
                 com.dimata.posbo.entity.warehouse.PstMaterialStockCode,
                 com.dimata.gui.jsp.ControlList,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.qdep.form.FRMMessage,
                 com.dimata.util.Command"%>
<%@ page language="java" %>

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK, AppObjInfo.OBJ_STOCK_OPNAME); %>
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

/**
* this method used to maintain soList
*/
public String drawList(int language,int iCommand,FrmSourceStockCode frmObject,SourceStockCode objEntity, Vector classObj,double max ,long soItemId,int start, Vector listObj,int sts)
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
	   start=0;
	}

         for(int i=0; i< listObj.size(); i++){
            max=max-1;
             SourceStockCode sourceStockCode = (SourceStockCode)listObj.get(i);
            rowx = new Vector();
            start = start + 1;
            rowx.add(""+start);
            if(sts==I_DocStatus.DOCUMENT_STATUS_POSTED){
                rowx.add(""+sourceStockCode.getStockCode());
                rowx.add(""+sourceStockCode.getStockValue());
            }else{
                rowx.add("<a href=\"javascript:deleteCode('"+sourceStockCode.getOID()+"')\">"+sourceStockCode.getStockCode()+"</a>");
                rowx.add(""+FRMHandler.userFormatStringDecimal(sourceStockCode.getStockValue()));
            }
            lstData.add(rowx);
        }

         for(int i=0; i< max; i++){
            rowx = new Vector();
            String strCode = "";
            String strValue= "0.0";
            SourceStockCode sourceStockCode = new SourceStockCode();
            strValue= FRMHandler.userFormatStringDecimal(sourceStockCode.getStockValue());
            //add opie-eyek untuk stock fifo
            start = start + 1;
            rowx.add(""+start);
            rowx.add("<div align=\"left\"><input name=\""+frmObject.fieldNames[FrmSourceStockCode.FRM_FIELD_STOCK_CODE] +"_"+i+"_oid\" type=\"hidden\" value=\""+sourceStockCode.getOID()+"\"><input type=\"text\" name=\""+frmObject.fieldNames[FrmSourceStockCode.FRM_FIELD_STOCK_CODE] +"_"+i+"\" value=\""+strCode+"\" onkeyup=\"if (event.keyCode == 13) { showData(this.value); return false; }\" class=\"formElemen\"></div>");
            rowx.add("<div align=\"left\"><input name=\""+frmObject.fieldNames[FrmSourceStockCode.FRM_FIELD_STOCK_VALUE] +"_"+i+"_oid\" type=\"hidden\" value=\""+sourceStockCode.getOID()+"\"><input type=\"text\" name=\""+frmObject.fieldNames[FrmSourceStockCode.FRM_FIELD_STOCK_VALUE] +"_"+i+"\" value=\""+strValue+"\" class=\"formElemen\"></div>");
            lstData.add(rowx);
	}

	/*for(int i=0; i< max; i++){
		rowx = new Vector();
		String strCode = "";
                String strValue= "0.0";
                SourceStockCode sourceStockCode = new SourceStockCode();
                if(i<listObj.size()){
                    sourceStockCode = (SourceStockCode)listObj.get(i);
                    strCode = sourceStockCode.getStockCode();
                    strValue= FRMHandler.userFormatStringDecimal(sourceStockCode.getStockValue());
                }
                //add opie-eyek untuk stock fifo
                start = start + 1;
                rowx.add(""+start);
                rowx.add("<div align=\"left\"><input name=\""+frmObject.fieldNames[FrmSourceStockCode.FRM_FIELD_STOCK_CODE] +"_"+i+"_oid\" type=\"hidden\" value=\""+sourceStockCode.getOID()+"\"><input type=\"text\" name=\""+frmObject.fieldNames[FrmSourceStockCode.FRM_FIELD_STOCK_CODE] +"_"+i+"\" value=\""+strCode+"\" onkeyup=\"if (event.keyCode == 13) { showData(this.value); return false; }\" class=\"formElemen\"></div>");
                rowx.add("<div align=\"left\"><input name=\""+frmObject.fieldNames[FrmSourceStockCode.FRM_FIELD_STOCK_VALUE] +"_"+i+"_oid\" type=\"hidden\" value=\""+sourceStockCode.getOID()+"\"><input type=\"text\" name=\""+frmObject.fieldNames[FrmSourceStockCode.FRM_FIELD_STOCK_VALUE] +"_"+i+"\" value=\""+strValue+"\" class=\"formElemen\"></div>");
                lstData.add(rowx);
	}*/
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
int startItem = FRMQueryString.requestInt(request,"start_item");
int type = FRMQueryString.requestInt(request,"type_doc");
int cmdItem = FRMQueryString.requestInt(request,"command_item");
int appCommand = FRMQueryString.requestInt(request,"approval_command");
long oidOpname = FRMQueryString.requestLong(request, "hidden_opname_id");
long oidSourceStockCode = FRMQueryString.requestLong(request, "hidden_source_stock_code_id");
long oidStockOpnameItem = FRMQueryString.requestLong(request,"hidden_opname_item_id");

int iErrCode = FRMMessage.NONE;
String msgString = "";

SourceStockCode stockCode = new SourceStockCode();
MatStockOpname matStockOpname = new MatStockOpname();
MatStockOpnameItem stOpnameItem = new MatStockOpnameItem();
Material material = new Material();
try{
	stOpnameItem = PstMatStockOpnameItem.fetchExc(oidStockOpnameItem);
    matStockOpname = PstMatStockOpname.fetchExc(stOpnameItem.getStockOpnameId());
	material = PstMaterial.fetchExc(stOpnameItem.getMaterialId());
}catch(Exception e){}
    double recQtyPerBuyUnit = stOpnameItem.getQtyOpname();
    double qtyPerSellingUnit = PstUnit.getQtyPerBaseUnit(material.getBuyUnitId(), material.getDefaultStockUnitId());
    double recQty = recQtyPerBuyUnit * qtyPerSellingUnit;
    double max = recQty;

    String where = PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_MATERIAL_ID]+"="+material.getOID()+
            " AND "+PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_LOCATION_ID]+"="+matStockOpname.getLocationId()+
            " AND "+PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_STOCK_STATUS]+"="+PstMaterialStockCode.FLD_STOCK_STATUS_GOOD;
    Vector vStock = PstMaterialStockCode.list(0,0,where,"");

    if(vStock!=null && vStock.size()>0){
        if(vStock.size()>max){
           // max = vStock.size();
        }
    }

CtrSourceStockCode ctrSourceStockCode = new CtrSourceStockCode(request);
iErrCode = ctrSourceStockCode.action(Command.STOP,oidSourceStockCode);
FrmSourceStockCode frmSourceStockCode = ctrSourceStockCode.getForm();
SourceStockCode sourceStockCode = ctrSourceStockCode.getSourceStockCode();

    // Switch command to make auto add
    if (iCommand == Command.SAVE){
        //int x=0;
        ctrSourceStockCode.requestBarcode(stOpnameItem.getQtyOpname(),stOpnameItem.getOID(),request,oidOpname);
        iCommand = Command.ADD;
    }else if(iCommand == Command.DELETE){
        ctrSourceStockCode.action(iCommand,oidSourceStockCode);
        iCommand = Command.ADD;
    }

int recordToGetItem = 20;
int vectSize = 0;
Vector listObject  = new Vector();
if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST){
	startItem = ctrSourceStockCode.actionList(iCommand,startItem,vectSize,recordToGetItem);
}

Vector listMatStockOpnameItem = PstSourceStockCode.list(0,0,"","");
if(listMatStockOpnameItem.size()<1 && startItem>0){
	 if(vectSize-recordToGetItem > recordToGetItem){
	    startItem = startItem - recordToGetItem;
	 }else{
	    startItem = 0 ;
		iCommand = Command.FIRST;
	    prevCommand = Command.FIRST;
	}
	listMatStockOpnameItem = PstSourceStockCode.list(0,0,"","");
}

listObject = PstSourceStockCode.list(0,0,PstSourceStockCode.fieldNames[PstSourceStockCode.FLD_SOURCE_ID]+"="+stOpnameItem.getOID(),"");
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
    <%if(type==0){%>
	    document.frm_stockcode.action="mat_opname_store_edit.jsp";
    <%}else if(type==1){%>
        document.frm_stockcode.action="mat_opname_edit.jsp";
    <%}%>
	document.frm_stockcode.submit();
}

function cmdSave(){
	document.frm_stockcode.command.value="<%=Command.SAVE%>";
	document.frm_stockcode.prev_command.value="<%=prevCommand%>";
	document.frm_stockcode.action="op_stockcode.jsp";
	document.frm_stockcode.submit();
}

function deleteCode(oidItem){
    var isTrue = confirm("Anda yakin ingin menghapus ?")
    if (isTrue){
        document.frm_stockcode.hidden_source_stock_code_id.value=oidItem;
        document.frm_stockcode.command.value="<%=Command.DELETE%>";
        document.frm_stockcode.prev_command.value="<%=prevCommand%>";
        document.frm_stockcode.action="op_stockcode.jsp";
        document.frm_stockcode.submit();
    }
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
    url : "<%=approot%>/servlet/com.dimata.posbo.ajax.CheckStockCode?<%=FrmSourceStockCode.fieldNames[FrmSourceStockCode.FRM_FIELD_STOCK_CODE]%>="+value+"&<%=FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_STOCK_OPNAME_ID]%>=<%=oidOpname%>&<%=CheckStockCode.CHECK_STOCK_CODE_NAME%>=<%=CheckStockCode.CHECK_STOCK_CODE_OPNAME%>",
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
document.getElementById("hidden_opname_id").focus();
}
</script>
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
		    <input name="hidden_opname_item_id" type="hidden" value="<%=oidStockOpnameItem%>">
                    <input name="command" type="hidden" value="<%=iCommand%>">
                    <input name="prev_command" type="hidden" value="<%=prevCommand%>">
                    <input name="hidden_opname_id" type="hidden" value="<%=oidOpname%>">
                    <input name="type_doc" type="hidden" value="<%=type%>">
                    <input name="hidden_source_stock_code_id" type="hidden" value="0">
                    <input name="hidden_state" type="hidden" value="0">

              <table width="100%"  border="0" cellspacing="1" cellpadding="1">
                <tr>
                  <td width="2%"><table width="100%"  border="0" cellspacing="1" cellpadding="1">
                    <tr>
                      <td colspan="2" class="mainheader">Create Stock Code </td>
                    </tr>
                    <tr>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
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
                      <td class="formElemen">Qty Stock </td>
                      <td class="formElemen">: <%=stOpnameItem.getQtyOpname()%></td>
                    </tr>
                    <tr>
                      <td height="22">&nbsp;</td>
                      <td>&nbsp;</td>
                    </tr>
                  </table></td>
                </tr>
                <tr>
                  <td class="comment">1 qty = 1 Stock Code </td>
                  <div id="divResults"></div>
                  <div id="divErrorMessages"></div>
                </tr>
                <tr>
                  <td><%= drawList(SESS_LANGUAGE,iCommand,frmSourceStockCode, sourceStockCode ,vStock, max ,oidSourceStockCode,startItem,listObject,matStockOpname.getStockOpnameStatus())%></td>
                </tr>
                 <tr>
                  <td class="comment"><%=ctrSourceStockCode.getStrError()%></td>
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
								out.println(ctrLine.drawImageListLimit(cmd,vectSize,startItem,recordToGetItem));
								%>
                  </span></td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                </tr>
                <tr>
                  <td><%
							ctrLine.setLocationImg(approot+"/images");
							ctrLine.initDefault();
							ctrLine.setTableWidth("80%");
                            ctrLine.setSaveCaption("Save Stock Code");
                            ctrLine.setDeleteCaption("");
                            ctrLine.setBackCaption("Back to item");
							ctrLine.setCommandStyle("command");
							ctrLine.setColCommStyle("command");

							if(iCommand==Command.SAVE && frmSourceStockCode.errorSize()==0){
								iCommand=Command.EDIT;
							}

                            if(matStockOpname.getStockOpnameStatus()==I_DocStatus.DOCUMENT_STATUS_POSTED){
                                ctrLine.setSaveCaption("");
                            }
							%>
                    <%=ctrLine.drawImage(Command.EDIT,iErrCode,msgString)%> </td>
                </tr>
              </table>
		  </form>
                   <script type="text/javascript">
                        document.all.<%=FrmSourceStockCode.fieldNames[FrmSourceStockCode.FRM_FIELD_STOCK_CODE]%>_0.focus();
                  </script>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
</body>
</html>

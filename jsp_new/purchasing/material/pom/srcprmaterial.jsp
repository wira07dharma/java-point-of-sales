<%-- 
    Document   : srcprmaterial
    Created on : Feb 5, 2014, 10:20:51 PM
    Author     : dimata005
--%>

<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@page import="com.dimata.posbo.form.purchasing.CtrlPurchaseRequest"%>
<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.entity.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package material -->
<%@ page import = "com.dimata.posbo.entity.search.*" %>
<%@ page import = "com.dimata.posbo.form.search.*" %>
<%@ page import = "com.dimata.posbo.session.purchasing.*" %>
<%@ page import = "com.dimata.posbo.entity.purchasing.*" %>
<%@ page import = "com.dimata.common.entity.location.*" %>
<%//@ page import = "com.dimata.posbo.entity.admin.*" %>

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_PURCHASING, AppObjInfo.G2_PURCHASING, AppObjInfo.OBJ_PURCHASE_ORDER); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

/* this constant used to list text of listHeader */
public static final String textListHeader[][] =
{
    {"Nomor","Supplier","Tanggal","Status","Urut Berdasar","Request Pembelian","Gudang","Semua Tanggal","Dari","s/d"},
    {"Number","Supplier","Date","Status","Sort By","Purchase Request","Warehouse","All Date","From","To"}
};

public static final String textMainHeader[][] =
{
    {"Gudang","Request Barang"},
    {"Warehouse","Purchase Request"}
};

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] =
{
    {"No","Kode","Tanggal","Supplier","Status","Keterangan","Mata Uang"},
    {"No","Code","Date","Supplier","Status","Remark","Currency"}
};

public String drawList(int language,Vector objectClass,int start,int docType,I_DocStatus i_status)
{
    String result = "";
    if(objectClass!=null && objectClass.size()>0){
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.addHeader(textListMaterialHeader[language][0],"3%");
        ctrlist.addHeader(textListMaterialHeader[language][1],"14%");
        ctrlist.addHeader(textListMaterialHeader[language][2],"7%");
        //ctrlist.addHeader(textListMaterialHeader[language][6],"7%");
        //ctrlist.addHeader(textListMaterialHeader[language][3],"30%");
        ctrlist.addHeader(textListMaterialHeader[language][4],"7%");
        ctrlist.addHeader(textListMaterialHeader[language][5],"25%");

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

        for(int i=0; i<objectClass.size(); i++){
            Vector vt = (Vector)objectClass.get(i);
            PurchaseRequest po = (PurchaseRequest)vt.get(0);
            //ContactList contact = (ContactList)vt.get(1);
            start = start + 1;

            Vector rowx = new Vector();
            rowx.add(""+start);
            rowx.add(po.getPrCode());

            String str_dt_PurchDate = "";
            try{
                Date dt_PurchDate = po.getPurchRequestDate();
                if(dt_PurchDate==null){
                    dt_PurchDate = new Date();
                }
                str_dt_PurchDate = Formater.formatDate(dt_PurchDate, "dd-MM-yyyy");
            }catch(Exception e){ str_dt_PurchDate = ""; }

            rowx.add(str_dt_PurchDate);

            //rowx.add(cntName);
            rowx.add(i_status.getDocStatusName(docType,po.getPrStatus()));
            rowx.add(po.getRemark());

            lstData.add(rowx);
            lstLinkData.add(String.valueOf(po.getOID()));
        }
        result = ctrlist.draw();
    }else{
        result = "<div class=\"msginfo\">&nbsp;&nbsp;Tidak ada data order pembelian ...</div>";
    }
    return result;
}

public String getJspTitle(int index, int language, String prefiks, boolean addBody){
    String result = "";
    if(addBody){
        if(language==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT){
            result = textListHeader[language][index] + " " + prefiks;
        }else{
            result = prefiks + " " + textListHeader[language][index];
        }
    }else{
        result = textListHeader[language][index];
    }
    return result;
}

public boolean getTruedFalse(Vector vect, int index){
    for(int i=0;i<vect.size();i++){
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
int docType = i_pstDocType.composeDocumentType(systemName,I_DocType.MAT_DOC_TYPE_POR);
%>


<%
/**
* get data from 'hidden form'
*/

/**
* declaration of some identifier
*/
String poCode = "PO"; //i_pstDocType.getDocCode(docType);
String poTitle = textListHeader[SESS_LANGUAGE][5];
String poTitles = textMainHeader[SESS_LANGUAGE][1];
String poItemTitle = poTitle + " Item";
String poTitleBlank = "";


long oidPurchaseRequest = FRMQueryString.requestLong(request, "hidden_material_request_id");

/**
* ControlLine
*/

int iErrCode = FRMMessage.ERR_NONE;
String msgStr = "";
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int recordToGet = 20;
int vectSize = 0;
String whereClause = "";

ControlLine ctrLine = new ControlLine();
CtrlPurchaseRequest ctrlPurchaseRequest = new CtrlPurchaseRequest(request);
SrcPurchaseRequest srcPurchaseRequest = new SrcPurchaseRequest();
SessPurchaseRequest sessPurchaseRequest = new SessPurchaseRequest();
FrmSrcPurchaseRequest frmSrcPurchaseRequest = new FrmSrcPurchaseRequest(request, srcPurchaseRequest);
srcPurchaseRequest.setSupplierWarehouse(-1);

//iErrCode = ctrlPurchaseRequest.action(iCommand,oidPurchaseRequest, userName, userId);

/*
try
{
	srcPurchaseRequest = (SrcPurchaseRequest)session.getValue(SessPurchaseRequest.SESS_SRC_REQUESTMATERIAL);
}
catch(Exception e)
{
	srcPurchaseRequest = new SrcPurchaseRequest();
}


if(srcPurchaseRequest==null){
	srcPurchaseRequest = new SrcPurchaseRequest();
}

try
{
	session.removeValue(SessPurchaseRequest.SESS_SRC_REQUESTMATERIAL);
}catch(Exception e){
}
*/

String sOidNumber = FRMQueryString.requestString(request, frmSrcPurchaseRequest.fieldNames[FrmSrcPurchaseRequest.FRM_FIELD_PRMNUMBER]); 
int oidDate = FRMQueryString.requestInt(request, frmSrcPurchaseRequest.fieldNames[FrmSrcPurchaseRequest.FRM_FIELD_STATUSDATE]);
int oidSortBy = FRMQueryString.requestInt(request,frmSrcPurchaseRequest.fieldNames[frmSrcPurchaseRequest.FRM_FIELD_SORTBY]);

if (oidDate == 1 || sOidNumber!= ""  || oidSortBy != 0 ){  
    frmSrcPurchaseRequest.requestEntityObject(srcPurchaseRequest);
}
 
Vector vectSt = new Vector(1,1);
String[] strStatus = request.getParameterValues(FrmSrcPurchaseRequest.fieldNames[FrmSrcPurchaseRequest.FRM_FIELD_PRMSTATUS]);
if(strStatus!=null && strStatus.length>0){
     for(int i=0; i<strStatus.length; i++){
        try{
                vectSt.add(strStatus[i]);
        }catch(Exception exc){
                System.out.println("err");
        }
     }
}
srcPurchaseRequest.setPrmstatus(vectSt);

String whereClausex = PstDataCustom.whereLocReportView(userId, "user_create_document_location");

vectSize = sessPurchaseRequest.getCountPurchaseRequestMaterial(srcPurchaseRequest,docType,0,whereClausex);
if(iCommand==Command.FIRST || iCommand==Command.NEXT || iCommand==Command.PREV || iCommand==Command.LAST || iCommand==Command.LIST){
    start = ctrlPurchaseRequest.actionList(iCommand,start,vectSize,recordToGet);
}

Vector records = sessPurchaseRequest.searchPurchaseRequestMaterial(srcPurchaseRequest,docType,start,recordToGet,0,whereClausex);

%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
function cmdAdd(){
    
  
    document.frm_purchaseorder.command.value="<%=Command.ADD%>";
    document.frm_purchaseorder.approval_command.value="<%=Command.SAVE%>";
    document.frm_purchaseorder.add_type.value="<%=ADD_TYPE_SEARCH%>";
    document.frm_purchaseorder.action="prmaterial_edit.jsp";
    if(compareDateForAdd()==true)
        document.frm_purchaseorder.submit();
    
   
}

function cmdSearch(oidPurchaseRequest){
    document.frm_purchaseorder.hidden_material_request_id.value=oidPurchaseRequest;
    document.frm_purchaseorder.command.value="<%=Command.LIST%>";
    document.frm_purchaseorder.action="srcprmaterial.jsp";
    document.frm_purchaseorder.submit();
}

function cmdEdit(oid){
    //alert("xxx " +oid);
    document.frm_purchaseorder.hidden_material_request_id.value=oid;
    document.frm_purchaseorder.start.value=0;
    document.frm_purchaseorder.approval_command.value="<%=Command.APPROVE%>";
    document.frm_purchaseorder.command.value="<%=Command.EDIT%>";
    document.frm_purchaseorder.action="prmaterial_edit.jsp";
    document.frm_purchaseorder.submit();
}

function cmdListFirst(){
    document.frm_purchaseorder.command.value="<%=Command.FIRST%>";
    document.frm_purchaseorder.action="srcprmaterial.jsp";
    document.frm_purchaseorder.submit();
}

function cmdListPrev(){
    document.frm_purchaseorder.command.value="<%=Command.PREV%>";
    document.frm_purchaseorder.action="srcprmaterial.jsp";
    document.frm_purchaseorder.submit();
}

function cmdListNext(){
    document.frm_purchaseorder.command.value="<%=Command.NEXT%>";
    document.frm_purchaseorder.action="srcprmaterial.jsp";
    document.frm_purchaseorder.submit();
}

function cmdListLast(){
    document.frm_purchaseorder.command.value="<%=Command.LAST%>";
    document.frm_purchaseorder.action="srcprmaterial.jsp";
    document.frm_purchaseorder.submit();
}

function cmdBack(){
    document.frm_purchaseorder.command.value="<%=Command.BACK%>";
    document.frm_purchaseorder.action="srcprmaterial.jsp";
    document.frm_purchaseorder.submit();
}


function disableRightClick(e){
    var message = "maaf,klik kanan tidak diijin kan";
    if(!document.rightClickDisabled){
        if(document.layers){
            document.captureEvents(Event.MOUSEDOWN);
            document.onmousedown = disableRightClick;
        }
    else 
        document.oncontextmenu = disableRightClick;
    return document.rightClickDisabled = true;
    }
    if(document.layers || (document.getElementById && !document.all)){
        if (e.which==2||e.which==3){
            alert(message);
            return false;
        }
    }else{
        alert(message);
        return false;
    }
}

disableRightClick();

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
<script type="text/javascript" src="../../../stylescript/javascript_flyout/jquery.min.js"></script>
<script>
    $(document).ready(function(){
        //menampilkan loading gif when button clicked
        
        $('.btnLoad').click(function(){
            //alert('test');
            $('#loading_space').fadeIn();
        });
    });
</script>
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
            <%=textListHeader[SESS_LANGUAGE][6]%> &gt; <%=textListHeader[SESS_LANGUAGE][5]%><!-- #EndEditable --></td>
        </tr>
        <tr>
          <td><!-- #BeginEditable "content" -->
              <form name="frm_purchaseorder" method ="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="add_type" value="">
              <input type="hidden" name="approval_command">
              <input type="hidden" name="hidden_material_request_id" value="<%=oidPurchaseRequest%>">
              <input type="hidden" name="start" value="<%=start%>">
              
              
              <input type="hidden" name="<%=frmSrcPurchaseRequest.fieldNames[frmSrcPurchaseRequest.FRM_FIELD_LOCATION_TYPE]%>" value="<%=PstLocation.fieldNames[PstLocation.TYPE_LOCATION_WAREHOUSE]%>">
              <table width="100%" border="0">
                <tr>
                  <td valign="top" colspan="2">
                    <hr size="1">
                  </td>
		</tr>
                
		<tr>
                    
                </tr>
                  <td>
                    <table width="100%" border="0" cellspacing="1" cellpadding="1">
                      <tr>
                        <td height="21" width="15%"><%=getJspTitle(0,SESS_LANGUAGE,poCode,false)%></td>
                        <td height="21" width="1%">:</td>
                        <td height="21" width="90%">&nbsp;
                          <input type="text" name="<%=frmSrcPurchaseRequest.fieldNames[FrmSrcPurchaseRequest.FRM_FIELD_PRMNUMBER] %>"  value="<%= srcPurchaseRequest.getPrmnumber() %>" class="formElemen" size="20">                        </td>
                      </tr>
                      <tr>
                        <td height="43" rowspan="2" valign="top" width="9%" align="left"><%=getJspTitle(2,SESS_LANGUAGE,poCode,false)%></td>
                        <td height="43" rowspan="2" valign="top" width="1%" align="left">:</td>
                        <td height="21" width="90%" valign="top" align="left">
                          <input type="radio" name="<%=frmSrcPurchaseRequest.fieldNames[FrmSrcPurchaseRequest.FRM_FIELD_STATUSDATE] %>" <%if(srcPurchaseRequest.getStatusdate()==0){%>checked<%}%> value="0">
                          <%=textListHeader[SESS_LANGUAGE][7]%></td>
                      </tr>
                      <tr align="left">
                        <td height="21" width="100%" valign="top">
                          <input type="radio" name="<%=frmSrcPurchaseRequest.fieldNames[FrmSrcPurchaseRequest.FRM_FIELD_STATUSDATE] %>" <%if(srcPurchaseRequest.getStatusdate()==1){%>checked<%}%> value="1">
                          <%=textListHeader[SESS_LANGUAGE][8]%> <%=ControlDate.drawDate(frmSrcPurchaseRequest.fieldNames[FrmSrcPurchaseRequest.FRM_FIELD_PRMDATEFROM], srcPurchaseRequest.getPrmdatefrom(),"formElemen",1,-5)%> <%=textListHeader[SESS_LANGUAGE][9]%> <%=	ControlDate.drawDate(frmSrcPurchaseRequest.fieldNames[FrmSrcPurchaseRequest.FRM_FIELD_PRMDATETO], srcPurchaseRequest.getPrmdateto(),"formElemen",1,-5) %> </td>
                      </tr>
                      
                        <tr>
                        <td height="21" colspan="3" align="left" valign="top">
                          <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                              <td nowrap width="4%"></td><br>
                              <td class="command" nowrap width="50%">
                                    <a class="btn-primary btn-lg" href="javascript:cmdSearch()"><%=ctrLine.getCommand(SESS_LANGUAGE,poTitle,ctrLine.CMD_SEARCH,true)%></a>
                                    
                                    <%if(privAdd){%>
                                        <a class="btn-primary btn-lg" class="btnLoad" href="javascript:cmdAdd()"><%=ctrLine.getCommand(SESS_LANGUAGE,poTitle,ctrLine.CMD_ADD,true)%></a>
                                    <%}%>
                              </td>
                            </tr>
                        </table></td>
                      </tr>
                    </table>
                  </td>
                  <td>
                    <table width="100%" border="0" cellspacing="1" cellpadding="1">
                      <tr>
                        <td height="21" valign="top" width="20%" align="left"><%=getJspTitle(3,SESS_LANGUAGE,poCode,false)%></td>
                        <td height="21" valign="top" width="1%" align="left">:</td>
                        <td height="21" width="90%" valign="top" align="left">
                        <%--<%
                          Vector vectResult = i_status.getDocStatusFor(docType);
                          for(int i=0; i<vectResult.size(); i++){
                                if ((i == I_DocStatus.DOCUMENT_STATUS_DRAFT) || (i == I_DocStatus.DOCUMENT_STATUS_POSTED)|| (i == I_DocStatus.DOCUMENT_STATUS_CLOSED) || (i == I_DocStatus.DOCUMENT_STATUS_FINAL) || (i == I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED) || (i == I_DocStatus.DOCUMENT_STATUS_APPROVED) )
                                {
                                        Vector vetTemp = (Vector)vectResult.get(i);
                                        int indexPrStatus = Integer.parseInt(String.valueOf(vetTemp.get(0)));
                                        String strPrStatus = String.valueOf(vetTemp.get(1));
                                  %>
                                  <input type="checkbox" class="formElemen" name="<%=frmSrcPurchaseRequest.fieldNames[FrmSrcPurchaseRequest.FRM_FIELD_PRMSTATUS]%>" value="<%=(indexPrStatus)%>" <%if(getTruedFalse(srcPurchaseRequest.getPrmstatus(),indexPrStatus)){%>checked<%}%>>
                                  <%=strPrStatus%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                  <%
                                }
                          }
                          %> --%>
                          <%
                           int indexPrStatus = 0; 
                           String strPrStatus = "Draft";
                          %>
                          <input type="checkbox" class="formElemen" name="<%=frmSrcPurchaseRequest.fieldNames[FrmSrcPurchaseRequest.FRM_FIELD_PRMSTATUS]%>" value="<%=(indexPrStatus)%>" <%if(getTruedFalse(srcPurchaseRequest.getPrmstatus(),indexPrStatus)){%>checked<%}%>><%=strPrStatus%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  

                          <%
                           indexPrStatus = 1; 
                           strPrStatus = "To Be Confirm";
                          %>
                          <input type="checkbox" class="formElemen" name="<%=frmSrcPurchaseRequest.fieldNames[FrmSrcPurchaseRequest.FRM_FIELD_PRMSTATUS]%>" value="<%=(indexPrStatus)%>" <%if(getTruedFalse(srcPurchaseRequest.getPrmstatus(),indexPrStatus)){%>checked<%}%>><%=strPrStatus%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  

                          <%
                           indexPrStatus = 10; 
                           strPrStatus = "Approved";
                          %>
                          <input type="checkbox" class="formElemen" name="<%=frmSrcPurchaseRequest.fieldNames[FrmSrcPurchaseRequest.FRM_FIELD_PRMSTATUS]%>" value="<%=(indexPrStatus)%>" <%if(getTruedFalse(srcPurchaseRequest.getPrmstatus(),indexPrStatus)){%>checked<%}%>><%=strPrStatus%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  

                          <%
                           indexPrStatus = 2; 
                           strPrStatus = "Final";
                          %>
                          <input type="checkbox" class="formElemen" name="<%=frmSrcPurchaseRequest.fieldNames[FrmSrcPurchaseRequest.FRM_FIELD_PRMSTATUS]%>" value="<%=(indexPrStatus)%>" <%if(getTruedFalse(srcPurchaseRequest.getPrmstatus(),indexPrStatus)){%>checked<%}%>><%=strPrStatus%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  
                          <%
                           indexPrStatus = 5; 
                           strPrStatus = "Closed";
                          %>
                          <input type="checkbox" class="formElemen" name="<%=frmSrcPurchaseRequest.fieldNames[FrmSrcPurchaseRequest.FRM_FIELD_PRMSTATUS]%>" value="<%=(indexPrStatus)%>" <%if(getTruedFalse(srcPurchaseRequest.getPrmstatus(),indexPrStatus)){%>checked<%}%>><%=strPrStatus%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  
                          <%
                           indexPrStatus = 7; 
                           strPrStatus = "Posted";
                          %>
                          <input type="checkbox" class="formElemen" name="<%=frmSrcPurchaseRequest.fieldNames[FrmSrcPurchaseRequest.FRM_FIELD_PRMSTATUS]%>" value="<%=(indexPrStatus)%>" <%if(getTruedFalse(srcPurchaseRequest.getPrmstatus(),indexPrStatus)){%>checked<%}%>><%=strPrStatus%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  

                        </td>
                      </tr>
                      <tr>
                        <td height="19" valign="top" width="9%" align="left"><%=getJspTitle(4,SESS_LANGUAGE,poCode,false)%></td>
                        <td height="19" valign="top" width="1%" align="left">:</td>
                        <td height="19" width="90%" valign="top" align="left">
                          <%
                                Vector key_sort = new Vector(1,1);
                                Vector val_sort = new Vector(1,1);

                                key_sort.add("0");
                                val_sort.add("Kode");

                                key_sort.add("1");
                                val_sort.add("Tanggal");

                                key_sort.add("2");
                                val_sort.add("Status");

                                key_sort.add("3");
                                val_sort.add("Supplier");

                                String select_sort = ""+srcPurchaseRequest.getSortby();
                                out.println("&nbsp;"+ControlCombo.draw(frmSrcPurchaseRequest.fieldNames[frmSrcPurchaseRequest.FRM_FIELD_SORTBY], null, select_sort,key_sort,val_sort,"","formElemen"));
                          %>                        </td>
                      </tr>
                      <tr>
                        <td height="21" valign="top" width="9%" align="left">&nbsp;</td>
                        <td height="21" valign="top" width="1%" align="left">&nbsp;</td>
                        <td height="21" width="90%" valign="top" align="left">&nbsp;</td>
                      </tr>
                    
                      
                      <tr>
                        <td height="21" colspan="3" align="left" valign="top">
                          <table width="47%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                              <%--<%if(privAdd){%>
                              <td nowrap width="6%"></td>
                              <td class="command" nowrap width="72%"><a class="btn-primary btn-lg" class="btnLoad" href="javascript:cmdAdd()"><%=ctrLine.getCommand(SESS_LANGUAGE,poTitle,ctrLine.CMD_ADD,true)%></a></td>
                              <%}%>--%>
                            </tr>
                        </table></td>
                      </tr>
                      
                      
                    </table>
                            
                </td> 
              
                  
              </table>
            </form>
            <!-- #EndEditable --></td>
        </tr>
        
        <%if(iCommand==Command.LIST || iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand == Command.LAST ){%>	 
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
        <td height="18" valign="top" align="left" colspan="2">
            <table width="36%" border="0" cellspacing="0" cellpadding="0">
              <tr>
              <%if(privAdd){%><br>
                    <td class="command" nowrap width="72%"><a class="btn-primary btn-lg" href="javascript:cmdAdd()"><%=ctrLine.getCommand(SESS_LANGUAGE,poTitle,ctrLine.CMD_ADD,true)%></a></td>
                <%}%>

              </tr>
            </table>
       </td>
      </tr>
     
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
<style>
    #loading_space{
        position:absolute;
        top : 50%;
        left : 50%;
        display:none;
    }
</style>
<div id="loading_space"><img src="../../../imgstyle/loading.gif"></div>
</body>
<!-- #EndTemplate --></html>


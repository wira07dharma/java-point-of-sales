<%-- 
    Document   : srcstorerequest
    Created on : Aug 3, 2014, 4:49:01 PM
    Author     : dimata005
--%>

<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@page import="com.dimata.common.entity.contact.ContactList"%>
<%@page import="com.dimata.common.entity.payment.PstCurrencyType"%>
<%@page import="com.dimata.common.entity.payment.CurrencyType"%>
<%@page import="com.dimata.posbo.form.purchasing.CtrlPurchaseOrder"%>
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

<%
/** Check privilege except VIEW, view is already checked on checkuser.jsp as basic access */
boolean privView=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_VIEW));

boolean privSubmit=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_SUBMIT));
%>


<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

/* this constant used to list text of listHeader */




public static final String textListHeader[][] = 
{
	{"Nomor","Supplier","Tanggal","Status","Urut Berdasar","Order Pembelian","Gudang","Semua Tanggal","Dari","s/d"},
	{"Number","Supplier","Date","Status","Sort By","Purchase Order","Warehouse","All Date","From","To"}
};






public static final String textMainHeader[][] = 
{
	{"Gudang","Store Request"},
	{"Warehouse","Store Request"}
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
	if(objectClass!=null && objectClass.size()>0)
	{
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("100%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("listgentitle");
            ctrlist.addHeader(textListMaterialHeader[language][0],"3%");
            ctrlist.addHeader(textListMaterialHeader[language][1],"14%");
            ctrlist.addHeader(textListMaterialHeader[language][2],"7%");
            ctrlist.addHeader(textListMaterialHeader[language][6],"7%");
            ctrlist.addHeader(textListMaterialHeader[language][3],"30%");
            ctrlist.addHeader(textListMaterialHeader[language][4],"7%");
            ctrlist.addHeader(textListMaterialHeader[language][5],"25%");

            ctrlist.setLinkRow(1);
            ctrlist.setLinkSufix("");
            Vector lstData = ctrlist.getData();
            Vector lstLinkData = ctrlist.getLinkData();
            ctrlist.setLinkPrefix("javascript:cmdEdit('");
            ctrlist.setLinkSufix("')");
            ctrlist.reset();
            if(start<0)
            {
                    start = 0;		
            }	

            for(int i=0; i<objectClass.size(); i++){
                Vector vt = (Vector)objectClass.get(i);
                PurchaseOrder po = (PurchaseOrder)vt.get(0);
                ContactList contact = (ContactList)vt.get(1);		
                String cntName = contact.getCompName();					
                if(cntName.length()==0){
                        cntName = String.valueOf(contact.getPersonName()+" "+contact.getPersonLastname());					
                }			
                start = start + 1;

                Vector rowx = new Vector();				
                rowx.add(""+start);
                rowx.add(po.getPoCode());

                String str_dt_PurchDate = ""; 
                try{
                        Date dt_PurchDate = po.getPurchDate();
                        if(dt_PurchDate==null){
                                dt_PurchDate = new Date();
                        }	
                        str_dt_PurchDate = Formater.formatDate(dt_PurchDate, "dd-MM-yyyy");
                }
                catch(Exception e){ str_dt_PurchDate = ""; }

                rowx.add(str_dt_PurchDate);

                // currency
                CurrencyType currType = new CurrencyType();
                try{
                    currType = PstCurrencyType.fetchExc(po.getCurrencyId());
                }catch(Exception e){}
                rowx.add(currType.getCode());

                rowx.add(cntName);
                if(po.getPoStatus()==I_DocStatus.DOCUMENT_STATUS_APPROVED){
                    rowx.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_APPROVED]);
                }else{
                    rowx.add(i_status.getDocStatusName(docType,po.getPoStatus()));
                }  

                rowx.add(po.getRemark());

                lstData.add(rowx);
                lstLinkData.add(String.valueOf(po.getOID()));
            }
            result = ctrlist.drawBootstrap();
	}
	else
	{
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
int iCommand = FRMQueryString.requestCommand(request);

/**
* declaration of some identifier
*/
String poCode = "PO"; //i_pstDocType.getDocCode(docType);
String poTitle = textListHeader[SESS_LANGUAGE][5];
String poItemTitle = poTitle + " Item";
String poTitleBlank = "";

/**
* ControlLine 
*/
ControlLine ctrLine = new ControlLine();



long oidPurchaseOrder = FRMQueryString.requestLong(request, "hidden_material_order_id");

/**
* initialitation some variable
*/
int iErrCode = FRMMessage.ERR_NONE;
String msgStr = "";

int start = FRMQueryString.requestInt(request, "start");
int recordToGet = 20;
int vectSize = 0;
String whereClause = "";



CtrlPurchaseOrder ctrlPurchaseOrder = new CtrlPurchaseOrder(request);
SrcPurchaseOrder srcPurchaseOrder = new SrcPurchaseOrder();
SessPurchaseOrder sessPurchaseOrder = new SessPurchaseOrder();
FrmSrcPurchaseOrder frmSrcPurchaseOrder = new FrmSrcPurchaseOrder(request, srcPurchaseOrder);


String sOidNumber = FRMQueryString.requestString(request,frmSrcPurchaseOrder.fieldNames[FrmSrcPurchaseOrder.FRM_FIELD_PRMNUMBER]); 
int oidDate = FRMQueryString.requestInt(request, frmSrcPurchaseOrder.fieldNames[FrmSrcPurchaseOrder.FRM_FIELD_STATUSDATE]);
int oidSortBy = FRMQueryString.requestInt(request,frmSrcPurchaseOrder.fieldNames[frmSrcPurchaseOrder.FRM_FIELD_SORTBY]);



if (oidDate == 1 || sOidNumber!= ""  || oidSortBy != 0 ){ 
frmSrcPurchaseOrder.requestEntityObject(srcPurchaseOrder);
}

	 Vector vectSt = new Vector(1,1);
	 String[] strStatus = request.getParameterValues(FrmSrcPurchaseOrder.fieldNames[FrmSrcPurchaseOrder.FRM_FIELD_PRMSTATUS]);
	 if(strStatus!=null && strStatus.length>0){
		 for(int i=0; i<strStatus.length; i++){
			try{
				vectSt.add(strStatus[i]);
			}catch(Exception exc){
				System.out.println("err");
			}
		 }
	 }
	 srcPurchaseOrder.setPrmstatus(vectSt);



         
         
         
String whereClausex = PstDataCustom.whereLocReportView(userId, "user_create_document_location");

vectSize = sessPurchaseOrder.getCountPurchaseOrderMaterial(srcPurchaseOrder,docType,whereClausex);
if(iCommand==Command.FIRST || iCommand==Command.NEXT || iCommand==Command.PREV || iCommand==Command.LAST || iCommand==Command.LIST){
	start = ctrlPurchaseOrder.actionList(iCommand,start,vectSize,recordToGet);
}	
Vector records = sessPurchaseOrder.searchPurchaseOrderMaterial(srcPurchaseOrder,docType,start,recordToGet,whereClausex);

%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
function cmdAdd(){
        //alert("he");
	document.frmsrcordermaterial.command.value="<%=Command.ADD%>";
	document.frmsrcordermaterial.approval_command.value="<%=Command.SAVE%>";	
	document.frmsrcordermaterial.add_type.value="<%=ADD_TYPE_SEARCH%>";				
	document.frmsrcordermaterial.action="storerequest_edit.jsp";
	if(compareDateForAdd()==true)
		document.frmsrcordermaterial.submit();
}

function cmdSearch(){
	document.frmsrcordermaterial.command.value="<%=Command.LIST%>";
	document.frmsrcordermaterial.action="srcstorerequest.jsp";
	document.frmsrcordermaterial.submit();
}

function cmdEdit(oid){
	document.frmsrcordermaterial.hidden_material_order_id.value=oid;
	document.frmsrcordermaterial.start.value=0;
	document.frmsrcordermaterial.approval_command.value="<%=Command.APPROVE%>";					
	document.frmsrcordermaterial.command.value="<%=Command.EDIT%>";
	document.frmsrcordermaterial.action="storerequest_edit.jsp";
	document.frmsrcordermaterial.submit();
}

function cmdListFirst(){
	document.frmsrcordermaterial.command.value="<%=Command.FIRST%>";
	document.frmsrcordermaterial.action="srcstorerequest.jsp";
	document.frmsrcordermaterial.submit();
}

function cmdListPrev(){
	document.frmsrcordermaterial.command.value="<%=Command.PREV%>";
	document.frmsrcordermaterial.action="srcstorerequest.jsp";
	document.frmsrcordermaterial.submit();
}

function cmdListNext(){
	document.frmsrcordermaterial.command.value="<%=Command.NEXT%>";
	document.frmsrcordermaterial.action="srcstorerequest.jsp";
	document.frmsrcordermaterial.submit();
}

function cmdListLast(){
	document.frmsrcordermaterial.command.value="<%=Command.LAST%>";
	document.frmsrcordermaterial.action="srcstorerequest.jsp";
	document.frmsrcordermaterial.submit();
}

function cmdBack(){
	document.frmsrcordermaterial.command.value="<%=Command.BACK%>";
	document.frmsrcordermaterial.action="srcstorerequest.jsp";
	document.frmsrcordermaterial.submit();
}


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
        <meta charset="UTF-8">
        <title>AdminLTE | Dashboard</title>
        <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>
        <!-- bootstrap 3.0.2 -->
        <link href="../../../styles/bootstrap3.1/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
        <!-- font Awesome -->
        <link href="../../../styles/bootstrap3.1/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
        <!-- Ionicons -->
        <link href="../../../styles/bootstrap3.1/css/ionicons.min.css" rel="stylesheet" type="text/css" />
        <!-- Morris chart -->
        <link href="../../../styles/bootstrap3.1/css/morris/morris.css" rel="stylesheet" type="text/css" />
        <!-- jvectormap -->
        <link href="../../../styles/bootstrap3.1/css/jvectormap/jquery-jvectormap-1.2.2.css" rel="stylesheet" type="text/css" />
        <!-- fullCalendar -->
        <!--link href="../../../styles/bootstrap3.1/css/fullcalendar/fullcalendar.css" rel="stylesheet" type="text/css"-- />
        <!-- Daterange picker -->
        <!--link href="../../../styles/bootstrap3.1/css/daterangepicker/daterangepicker-bs3.css" rel="stylesheet" type="text/css" /-->
        <!-- bootstrap wysihtml5 - text editor -->
        <link href="../../../styles/bootstrap3.1/css/bootstrap-wysihtml5/bootstrap3-wysihtml5.min.css" rel="stylesheet" type="text/css" />
        <!-- Theme style -->
        <link href="../../../styles/bootstrap3.1/css/AdminLTE.css" rel="stylesheet" type="text/css" />

        <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
          <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
          <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
        <![endif]-->
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
<body class="skin-blue">
        <%@ include file = "../../../header_mobile.jsp" %> 
        <div class="wrapper row-offcanvas row-offcanvas-left">
            
            <!-- Left side column. contains the logo and sidebar -->
            <%@ include file = "../../../menu_left_mobile.jsp" %> 

            <!-- Right side column. Contains the navbar and content of the page -->
            <aside class="right-side">
                <!-- Content Header (Page header) -->
                <section class="content-header">
                    <h1>
                        Dashboard
                        <small>Control panel</small>
                    </h1>
                    <ol class="breadcrumb">
                        <li><a href="#"><i class="fa fa-dashboard"></i>Store Request</a></li>
                        <li class="active">Search Store Request</li>
                    </ol>
                </section>

                <!-- Main content -->
                <section class="content">
                    <div class="row">
                        <div class="col-md-12">
                            <div class="box box-primary">
                                 <div class="box-header">
                                    <h3 class="box-title">Store Request</h3>
                                </div>
                                <form role="form" name="frmsrcordermaterial" method="post" action="">
                                     <input type="hidden" name="command" value="<%=iCommand%>">
                                     <input type="hidden" name="add_type" value="">			
                                     <input type="hidden" name="approval_command">
                                     <input type="hidden" name="start" value="<%=start%>">
                                     <input type="hidden" name="hidden_material_order_id" value="<%=oidPurchaseOrder%>">
                                     <input type="hidden" name="<%=frmSrcPurchaseOrder.fieldNames[frmSrcPurchaseOrder.FRM_FIELD_LOCATION_TYPE]%>" value="<%=PstLocation.fieldNames[PstLocation.TYPE_LOCATION_WAREHOUSE]%>">
                                     <div class="box-body">
                                         <%--start request--%>
                                         <div class="row">
                                            <div class="col-md-5">
                                                 <div class="form-group">
                                                    <label for="exampleInputEmail1"><%=getJspTitle(0,SESS_LANGUAGE,poCode,false)%></label>
                                                    <input type="text" class="form-control"  placeholder="Store Request Number" name="<%=frmSrcPurchaseOrder.fieldNames[FrmSrcPurchaseOrder.FRM_FIELD_PRMNUMBER] %>"  value="<%= srcPurchaseOrder.getPrmnumber() %>">
                                                 </div>
                                                 <div class="form-group">
                                                    <label for="exampleInputEmail1"><%=getJspTitle(2,SESS_LANGUAGE,poCode,false)%></label><br>
                                                    <input type="radio" name="<%=frmSrcPurchaseOrder.fieldNames[FrmSrcPurchaseOrder.FRM_FIELD_STATUSDATE] %>" <%if(srcPurchaseOrder.getStatusdate()==0){%>checked<%}%> value="0">
                                                    <%=textListHeader[SESS_LANGUAGE][7]%><br>
                                                    <input type="radio" name="<%=frmSrcPurchaseOrder.fieldNames[FrmSrcPurchaseOrder.FRM_FIELD_STATUSDATE] %>" <%if(srcPurchaseOrder.getStatusdate()==1){%>checked<%}%> value="1">
                                                    <%=textListHeader[SESS_LANGUAGE][8]%> <%=ControlDate.drawDate(frmSrcPurchaseOrder.fieldNames[FrmSrcPurchaseOrder.FRM_FIELD_PRMDATEFROM], srcPurchaseOrder.getPrmdatefrom(),"formElemen",1,-5)%>  &nbsp;<%=textListHeader[SESS_LANGUAGE][9]%>&nbsp; <%=ControlDate.drawDate(frmSrcPurchaseOrder.fieldNames[FrmSrcPurchaseOrder.FRM_FIELD_PRMDATETO], srcPurchaseOrder.getPrmdateto(),"formElemen",1,-5) %> 
                                                 </div>
                                            </div>
                                            <div class="col-md-7">
                                                  <div class="form-group">
                                                    <label for="exampleInputEmail1"><%=getJspTitle(3,SESS_LANGUAGE,poCode,false)%></label><br>
                                                    <%
                                                       int indexPrStatus = 0; 
                                                       String strPrStatus = "Draft";
                                                      %>
                                                      <input type="checkbox" class="formElemen" name="<%=frmSrcPurchaseOrder.fieldNames[frmSrcPurchaseOrder.FRM_FIELD_PRMSTATUS]%>" value="<%=(indexPrStatus)%>" <%if(getTruedFalse(srcPurchaseOrder.getPrmstatus(),indexPrStatus)){%>checked<%}%>>&nbsp; <%=strPrStatus%>&nbsp;&nbsp;&nbsp;&nbsp; 

                                                      <%
                                                       indexPrStatus = 1; 
                                                       strPrStatus = "ToBe Confirm";
                                                      %>
                                                      <input type="checkbox" class="formElemen" name="<%=frmSrcPurchaseOrder.fieldNames[frmSrcPurchaseOrder.FRM_FIELD_PRMSTATUS]%>" value="<%=(indexPrStatus)%>" <%if(getTruedFalse(srcPurchaseOrder.getPrmstatus(),indexPrStatus)){%>checked<%}%>>&nbsp; <%=strPrStatus%>&nbsp;&nbsp;&nbsp;&nbsp;

                                                      <%
                                                       indexPrStatus = 10; 
                                                       strPrStatus = "Approved";
                                                      %>
                                                      <input type="checkbox" class="formElemen" name="<%=frmSrcPurchaseOrder.fieldNames[frmSrcPurchaseOrder.FRM_FIELD_PRMSTATUS]%>" value="<%=(indexPrStatus)%>" <%if(getTruedFalse(srcPurchaseOrder.getPrmstatus(),indexPrStatus)){%>checked<%}%>>&nbsp; <%=strPrStatus%>&nbsp;&nbsp;&nbsp;&nbsp; 

                                                      <%
                                                       indexPrStatus = 2; 
                                                       strPrStatus = "Final";
                                                      %>
                                                      <input type="checkbox" class="formElemen" name="<%=frmSrcPurchaseOrder.fieldNames[frmSrcPurchaseOrder.FRM_FIELD_PRMSTATUS]%>" value="<%=(indexPrStatus)%>" <%if(getTruedFalse(srcPurchaseOrder.getPrmstatus(),indexPrStatus)){%>checked<%}%>>&nbsp;<%=strPrStatus%>&nbsp;&nbsp;&nbsp;&nbsp;  
                                                      <%
                                                       indexPrStatus = 5; 
                                                       strPrStatus = "Closed";
                                                      %>
                                                      <input type="checkbox" class="formElemen" name="<%=frmSrcPurchaseOrder.fieldNames[frmSrcPurchaseOrder.FRM_FIELD_PRMSTATUS]%>" value="<%=(indexPrStatus)%>" <%if(getTruedFalse(srcPurchaseOrder.getPrmstatus(),indexPrStatus)){%>checked<%}%>>&nbsp;<%=strPrStatus%>&nbsp;&nbsp;&nbsp;&nbsp;  
                                                      <%
                                                       indexPrStatus = 7; 
                                                       strPrStatus = "Posted";
                                                      %>
                                                      <input type="checkbox" class="formElemen" name="<%=frmSrcPurchaseOrder.fieldNames[frmSrcPurchaseOrder.FRM_FIELD_PRMSTATUS]%>" value="<%=(indexPrStatus)%>" <%if(getTruedFalse(srcPurchaseOrder.getPrmstatus(),indexPrStatus)){%>checked<%}%>>&nbsp;<%=strPrStatus%>&nbsp;&nbsp;&nbsp;&nbsp;  
                                                 </div>  
                                                 <div class="form-group">
                                                    <label for="exampleInputEmail1"><%=getJspTitle(4,SESS_LANGUAGE,poCode,false)%></label>
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

                                                            String select_sort = ""+srcPurchaseOrder.getSortby(); 
                                                            out.println("&nbsp;"+ControlCombo.drawBootsratap(frmSrcPurchaseOrder.fieldNames[frmSrcPurchaseOrder.FRM_FIELD_SORTBY], null, select_sort,key_sort,val_sort,"","form-control-date"));
                                                      %>
                                                 </div>
                                            </div>
                                         </div>
                                     </div>
                                    <div class="box-footer">
                                        <button  onclick="javascript:cmdSearch()" type="submit" class="btn btn-primary">Search</button>
                                        <button type="submit" onclick="javascript:cmdAdd()" class="btn btn-primary pull-right" >Add New</button>
                                    </div>
                                    <%if(iCommand==Command.LIST || iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand == Command.LAST){%>	 
                                        <%=drawList(SESS_LANGUAGE,records,start,docType,i_status)%>
                                    <%}%>
                                    <div class="box-footer clearfix">
                                        <%--<ul class="pagination pagination-sm no-margin pull-right">
                                            <li><a href="#">&laquo;</a></li>
                                            <li><a href="#">1</a></li>
                                            <li><a href="#">2</a></li>
                                            <li><a href="#">3</a></li>
                                            <li><a href="#">&raquo;</a></li>
                                        </ul>--%>
                                         <% 
                                            ctrLine.setLocationImg(approot+"/images");
                                            ctrLine.initDefault();
                                            String strList = ctrLine.drawImageListLimit(iCommand,vectSize,start,recordToGet);
                                            if(strList.length()>0){
                                            %>
                                            <%=strList%>
                                        <%}%>
                                    </div>
                                    <div class="box-footer">
                                        <button  onclick="javascript:cmdAdd()" type="submit" class="btn btn-primary">Add New</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>  
                </section><!-- /.content -->
                
            </aside><!-- /.right-side -->
        </div><!-- ./wrapper -->

        <!-- add new calendar event modal -->


        <!-- jQuery 2.0.2 -->
        <script src="../../../styles/bootstrap3.1/js/jquery.min.js"></script>
        <!-- jQuery UI 1.10.3 -->
        <script src="../../../styles/bootstrap3.1/js/jquery-ui-1.10.3.min.js" type="text/javascript"></script>
        <!-- Bootstrap -->
        <script src="../../../styles/bootstrap3.1/js/bootstrap.min.js" type="text/javascript"></script>
        <!-- Morris.js charts -->
        <script src="../../../styles/bootstrap3.1/js/raphael-min.js"></script>
        <script src="../../../styles/bootstrap3.1/js/plugins/morris/morris.min.js" type="text/javascript"></script>
        <!-- Sparkline -->
        <script src="../../../styles/bootstrap3.1/js/plugins/sparkline/jquery.sparkline.min.js" type="text/javascript"></script>
        <!-- jvectormap -->
        <script src="../../../styles/bootstrap3.1/js/plugins/jvectormap/jquery-jvectormap-1.2.2.min.js" type="text/javascript"></script>
        <script src="../../../styles/bootstrap3.1/js/plugins/jvectormap/jquery-jvectormap-world-mill-en.js" type="text/javascript"></script>
        <!-- fullCalendar -->
        <script src="../../../styles/bootstrap3.1/js/plugins/fullcalendar/fullcalendar.min.js" type="text/javascript"></script>
        <!-- jQuery Knob Chart -->
        <script src="../../../styles/bootstrap3.1/js/plugins/jqueryKnob/jquery.knob.js" type="text/javascript"></script>
        <!-- daterangepicker -->
        <script src="../../../styles/bootstrap3.1/js/plugins/daterangepicker/daterangepicker.js" type="text/javascript"></script>
        <!-- Bootstrap WYSIHTML5 -->
        <script src="../../../styles/bootstrap3.1/js/plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.all.min.js" type="text/javascript"></script>
        <!-- iCheck -->
        <script src="../../../styles/bootstrap3.1/js/plugins/iCheck/icheck.min.js" type="text/javascript"></script>

        <!-- AdminLTE App -->
        <script src="../../../styles/bootstrap3.1/js/AdminLTE/app.js" type="text/javascript"></script>
        
        <!-- AdminLTE dashboard demo (This is only for demo purposes) -->
        <script src="../../../styles/bootstrap3.1/js/AdminLTE/dashboard.js" type="text/javascript"></script>        

    </body>
<!-- #EndTemplate --></html>

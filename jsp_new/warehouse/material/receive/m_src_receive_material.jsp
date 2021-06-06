<%@page import="com.dimata.posbo.form.warehouse.CtrlMatReceive"%>
<%@page import="com.dimata.posbo.entity.purchasing.PurchaseOrder"%>
<%@page import="com.dimata.common.entity.payment.CurrencyType"%>
<%@page import="com.dimata.common.entity.contact.ContactList"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatReceive"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<% 
/* 
 * Page Name  		:  src_receive_material.jsp
 * Created on 		:  Selasa, 2 Agustus 2007 10:40 AM 
 * 
 * @author  		:  gwawan
 * @version  		:  -
 */

/*******************************************************************
 * Page Description : page ini merupakan gabungan dari page :
 						- srcreceive_wh_supp_material.jsp 
						- srcreceive_wh_supp_po_material.jsp
 * Imput Parameters : [input parameter ...] 
 * Output 			: [output ...] 
 *******************************************************************/
%>
<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*,
                   com.dimata.posbo.entity.search.SrcMatReceive,
                   com.dimata.posbo.form.search.FrmSrcMatReceive,
                   com.dimata.posbo.session.warehouse.SessMatReceive,
                   com.dimata.posbo.entity.warehouse.PstMatReceive" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.entity.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package material -->
<%@ page import = "com.dimata.common.entity.location.*" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

public static final String textListGlobal[][] = {
	{"Penerimaan","Dari Pembelian","Pencarian","Daftar","Edit","Dengan PO","Tanpa PO","Tidak ada data penerimaan barang"},
	{"Receive","From Purchase","Search","List","Edit","With PO","Without PO","Receiving goods not found"}
};

/* this constant used to list text of listHeader */
public static final String textListHeader[][] = {
	{"Nomor","Supplier","Invoice Supplier","Lokasi Terima","Tanggal","Dari"," s/d ","Status","Urut Berdasar","Semua"},
	{"Number","Supplier","Supplier Invoice","Receive Location","Date","From"," to ","Status","Sort By","All"}
};





/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] = {
	{"No","Kode","Tanggal","Mata Uang","Nomor SR","Supplier","Status","Keterangan"},
	{"No","Code","Date","Currency","SR Number","Supplier","Status","Description"}
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
		result = ctrlist.drawBootstrap();
	}
	else{
		result = "<div class=\"msginfo\">&nbsp;&nbsp;"+textListGlobal[language][7]+"</div>";		
	}
	return result;
}


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
long oidMatReceive = FRMQueryString.requestLong(request, "hidden_receive_id");


int iErrCode = FRMMessage.ERR_NONE;
String msgStr = "";

int start = FRMQueryString.requestInt(request, "start");

int recordToGet = 20;
int vectSize = 0;

/**
* declaration of some identifier
*/
String recCode = i_pstDocType.getDocCode(docType);
String recTitle = "Terima Barang"; // i_pstDocType.getDocTitle(docType);
String recItemTitle = recTitle + " Item";

/**
* ControlLine 
*/
ControlLine ctrLine = new ControlLine();

CtrlMatReceive ctrlMatReceive = new CtrlMatReceive(request);
SrcMatReceive srcMatReceive = new SrcMatReceive();
SessMatReceive sessMatReceive = new SessMatReceive();
FrmSrcMatReceive frmSrcMatReceive = new FrmSrcMatReceive(request, srcMatReceive);


String sOidNumber = FRMQueryString.requestString(request,frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_RECEIVENUMBER]); 
int oidDate = FRMQueryString.requestInt(request, frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_RECEIVEDATESTATUS]);
int oidSortBy = FRMQueryString.requestInt(request,frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_RECEIVESORTBY]); 
String sOidVendor = FRMQueryString.requestString(request,frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_VENDORNAME]); 
String sInvoiceSupp = FRMQueryString.requestString(request,frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_INVOICE_SUPPLIER]); 
long oidLoc = FRMQueryString.requestLong(request,frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_LOCATION_ID]); 
int PurchaseID = FRMQueryString.requestInt(request, "PurchaseID");

if (oidDate == 1 || sOidNumber!= ""  || oidSortBy != 0 || sOidVendor !="" || sInvoiceSupp !="" || oidLoc !=0 && iCommand != Command.FIRST){ 
frmSrcMatReceive.requestEntityObject(srcMatReceive);
}

Vector vectSt = new Vector(1,1);
String[] strStatus = request.getParameterValues(FrmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_RECEIVESTATUS]);
if(strStatus!=null && strStatus.length>0) {
     for(int i=0; i<strStatus.length; i++) {        
            try {
                    vectSt.add(strStatus[i]);
            }
            catch(Exception exc) {
                    System.out.println("err");
            }
     }
}

srcMatReceive.setReceiveSource(-1);
srcMatReceive.setReceivestatus(vectSt);   

if(PurchaseID==-1){
    srcMatReceive.setPurchaseOrderId(PurchaseID);
}
if(PurchaseID==-2){
    srcMatReceive.setPurchaseOrderId(PurchaseID);
}
  
String whereClausex = PstDataCustom.whereLocReportView(userId, "user_create_document_location");

vectSize = sessMatReceive.getCountSearchSupplier(srcMatReceive, whereClausex);

if(iCommand==Command.FIRST || iCommand==Command.NEXT || iCommand==Command.PREV || iCommand==Command.LAST || iCommand==Command.LIST) {
	start = ctrlMatReceive.actionList(iCommand,start,vectSize,recordToGet);
}

Vector records = sessMatReceive.searchMatReceiveSupplier(srcMatReceive,start,recordToGet,whereClausex);
%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">


function cmdSearch(){
	document.frm_recmaterial.command.value="<%=Command.LIST%>";
	document.frm_recmaterial.action="m_src_receive_material.jsp";
	document.frm_recmaterial.submit();
}

function cmdAddPO(){
	document.frm_recmaterial.command.value="<%=Command.ADD%>";
	document.frm_recmaterial.approval_command.value="<%=Command.SAVE%>";	
	document.frm_recmaterial.add_type.value="<%=ADD_TYPE_SEARCH%>";				
	document.frm_recmaterial.action="m_receive_wh_supp_po_material_edit.jsp";
	document.frm_recmaterial.submit();
}

function cmdAdd(){
	document.frm_recmaterial.command.value="<%=Command.ADD%>";
	document.frm_recmaterial.approval_command.value="<%=Command.SAVE%>";	
	document.frm_recmaterial.add_type.value="<%=ADD_TYPE_SEARCH%>";				
	document.frm_recmaterial.action="receive_wh_supp_material_edit.jsp";
	if(compareDateForAdd()==true)
	document.frm_recmaterial.submit();
}

function cmdEdit(idRcv, idPo){
	document.frm_recmaterial.hidden_receive_id.value=idRcv;
	document.frm_recmaterial.start.value=0;
	document.frm_recmaterial.approval_command.value="<%=Command.APPROVE%>";					
	document.frm_recmaterial.command.value="<%=Command.EDIT%>";
	if(parseInt(idPo) != 0) {
		document.frm_recmaterial.action="m_receive_wh_supp_po_material_edit.jsp";
                //document.frm_recmaterial.action="m_receive_wh_supp_po_materialitem.jsp";
	} 
	else {
                //document.frm_recmaterial.action="m_receive_wh_supp_po_materialitem.jsp";
              document.frm_recmaterial.action="receive_wh_supp_material_edit.jsp";
	}
	document.frm_recmaterial.submit();
}

function cmdListFirst(){
	document.frm_recmaterial.command.value="<%=Command.FIRST%>";
	document.frm_recmaterial.action="m_src_receive_material.jsp";
	document.frm_recmaterial.submit();
}

function cmdListPrev(){
	document.frm_recmaterial.command.value="<%=Command.PREV%>";
	document.frm_recmaterial.action="m_src_receive_material.jsp";
	document.frm_recmaterial.submit();
}

function cmdListNext(){
	document.frm_recmaterial.command.value="<%=Command.NEXT%>";
	document.frm_recmaterial.action="m_src_receive_material.jsp";
	document.frm_recmaterial.submit();
}

function cmdListLast(){
	document.frm_recmaterial.command.value="<%=Command.LAST%>";
	document.frm_recmaterial.action="m_src_receive_material.jsp";
	document.frm_recmaterial.submit();
}

function cmdBack(){
	document.frm_recmaterial.command.value="<%=Command.BACK%>";
	document.frm_recmaterial.action="m_src_receive_material.jsp";
	document.frm_recmaterial.submit();
}


function fnTrapKD(){
   if (event.keyCode == 13) {
		document.all.aSearch.focus();
		cmdSearch();
   }
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
                        <small><%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][1]%> &gt; <%=textListGlobal[SESS_LANGUAGE][2]%> <!-- #EndEditable --></small>
                    </h1>
                    <ol class="breadcrumb">
                        <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
                        <li class="active">Dashboard</li>
                    </ol>
                </section>

                <!-- Main content -->
                <section class="content">
                        <form name="frm_recmaterial" method="post" action="">
                        <input type="hidden" name="command" value="<%=iCommand%>">
                        <input type="hidden" name="add_type" value="">			
                        <input type="hidden" name="approval_command">			    			  			  			  			  			  
                        <input type="hidden" name="<%=frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_LOCATION_TYPE]%>" value="<%=PstLocation.TYPE_LOCATION_WAREHOUSE%>">
                        <input type="hidden" name="<%=frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_RECEIVE_SOURCE]%>" value="<%=PstMatReceive.SOURCE_FROM_SUPPLIER%>">
                        <input type="hidden" name="start" value="<%=start%>">
                        <input type="hidden" name="hidden_receive_id" value="<%=oidMatReceive%>">
                        <input type="hidden" name="PurchaseID" value="<%=PurchaseID%>">
                        <!--form hidden -->
                        
                        <!--body-->
                        <div class="box-body">
                            <div class="box-body">
                                <div class="row">
                                        <div class="col-md-5">
                                            
                                            <div class="form-group">
                                                <label for="exampleInputEmail1"><%=getJspTitle(0,SESS_LANGUAGE,recCode,false)%></label><br />
                                                <input tabindex="1" type="text" name="<%=frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_RECEIVENUMBER] %>"  value="<%= srcMatReceive.getReceivenumber() %>" class="form-control" size="20" onKeyDown="javascript:fnTrapKD()">
                                                
                                            </div>
                                                
                                            <div class="form-group">
                                                <label for="exampleInputEmail1"><%=getJspTitle(1,SESS_LANGUAGE,recCode,false)%></label><br />
                                                <input type="text" name="<%=frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_VENDORNAME] %>"  value="<%= srcMatReceive.getVendorname() %>" class="form-control" size="30" onKeyDown="javascript:fnTrapKD()">

                                            </div>
                                                
                                            <div class="form-group">
                                                <label for="exampleInputEmail1"><%=getJspTitle(2,SESS_LANGUAGE,recCode,false)%></label><br />
                                                <input type="text" name="<%=frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_INVOICE_SUPPLIER] %>"  value="<%= srcMatReceive.getInvoiceSupplier() %>" class="form-control" size="20" onKeyDown="javascript:fnTrapKD()">

                                            </div>    
                                                
                                                
                                             
                                            <div class="form-group">
                                                <label for="exampleInputEmail1"><%=getJspTitle(3,SESS_LANGUAGE,recCode,false)%></label><br />
                                                <% 
                                                        Vector obj_locationid = new Vector(1,1); 
                                                        Vector val_locationid = new Vector(1,1); 
                                                        Vector key_locationid = new Vector(1,1); 
                                                        /*String whereClause = PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE;
                                                        whereClause += " OR " + PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE;
                                                        Vector vt_loc = PstLocation.list(0, 0, whereClause, PstLocation.fieldNames[PstLocation.FLD_CODE]);*/
                                                        //add opie-eyek
                                                        //algoritma : di check di sistem usernya dimana saja user tsb bisa melakukan create document
                                                        String whereClause = " ("+PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE + 
                                                                           " OR "+PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE +")";

                                                        whereClause += " AND "+PstDataCustom.whereLocReportView(userId, "user_create_document_location");

                                                        Vector vt_loc = PstLocation.listLocationCreateDocument(0, 0, whereClause, "");

                                                        val_locationid.add(String.valueOf(0));
                                                        key_locationid.add(getJspTitle(9,SESS_LANGUAGE,recCode,false)+" "+getJspTitle(3,SESS_LANGUAGE,recCode,false));
                                                        for(int d=0; d<vt_loc.size(); d++) {
                                                                Location loc = (Location)vt_loc.get(d);
                                                                val_locationid.add(""+loc.getOID()+"");
                                                                key_locationid.add(loc.getName());
                                                        }
                                                  %>                 
                                                  <%=ControlCombo.drawBootsratap(frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_LOCATION_ID], null, "" +oidLoc , val_locationid, key_locationid, " onKeyDown=\"javascript:fnTrapKD()\"", "form-control")%>

                                            </div>    
                                                
                                            
                                        </div>
                                    <!-- col-md-5 -->    
                                        <div class="col-md-7">
                                             <div class="form-group">
                                                <label for="exampleInputEmail1"><%=getJspTitle(4,SESS_LANGUAGE,recCode,false)%></label><br />
                                                <input type="radio" name="<%=frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_RECEIVEDATESTATUS] %>" <%if(srcMatReceive.getReceivedatestatus()==0){%>checked<%}%> value="0" onKeyDown="javascript:fnTrapKD()">
                                                 <%=getJspTitle(9,SESS_LANGUAGE,recCode,false)%>&nbsp;<%=getJspTitle(4,SESS_LANGUAGE,recCode,false)%>
                                             </div>
                                             <div class="form-group">
                                                 <input type="radio" name="<%=frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_RECEIVEDATESTATUS] %>" <%if(srcMatReceive.getReceivedatestatus()==1){%>checked<%}%> value="0" onKeyDown="javascript:fnTrapKD()">
                                                 <%=getJspTitle(5,SESS_LANGUAGE,recCode,false)%> <%=ControlDate.drawDate(frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_RECEIVEFROMDATE], srcMatReceive.getReceivefromdate(),"form-control-date",1,-5)%> <%=getJspTitle(6,SESS_LANGUAGE,recCode,false)%> <%=ControlDate.drawDate(frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_RECEIVETODATE], srcMatReceive.getReceivetodate(),"form-control-date",1,-5) %> 
                                             </div>
                                             <div class="form-group">
                                                     <label for="exampleInputEmail1"><%=getJspTitle(7,SESS_LANGUAGE,recCode,false)%></label><br />
                                                     
                                                           <%
                                                            int indexPrStatus = 0; 
                                                            String strPrStatus = "&nbsp; Draft";
                                                           %>
                                                           <input type="checkbox" class="form-control" name="<%=frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_RECEIVESTATUS]%>" value="<%=(indexPrStatus)%>" <%if(getTruedFalse(srcMatReceive.getReceivestatus(),indexPrStatus)){%>checked<%}%>><%=strPrStatus%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  

                                                           <%
                                                            indexPrStatus = 1; 
                                                            strPrStatus = "&nbsp; To Be Confirm";
                                                           %>
                                                           <input type="checkbox" class="form-control" name="<%=frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_RECEIVESTATUS]%>" value="<%=(indexPrStatus)%>" <%if(getTruedFalse(srcMatReceive.getReceivestatus(),indexPrStatus)){%>checked<%}%>><%=strPrStatus%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  

                                                           <%
                                                            indexPrStatus = 10; 
                                                            strPrStatus = "&nbsp; Approved";
                                                           %>
                                                           <input type="checkbox" class="form-control" name="<%=frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_RECEIVESTATUS]%>" value="<%=(indexPrStatus)%>" <%if(getTruedFalse(srcMatReceive.getReceivestatus(),indexPrStatus)){%>checked<%}%>><%=strPrStatus%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  

                                                           <%
                                                            indexPrStatus = 2; 
                                                            strPrStatus = "&nbsp;Final";
                                                           %>
                                                           <input type="checkbox" class="form-control" name="<%=frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_RECEIVESTATUS]%>" value="<%=(indexPrStatus)%>" <%if(getTruedFalse(srcMatReceive.getReceivestatus(),indexPrStatus)){%>checked<%}%>><%=strPrStatus%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  
                                                           <%
                                                            indexPrStatus = 5; 
                                                            strPrStatus = "&nbsp;Closed";
                                                           %>
                                                           <input type="checkbox" class="form-control" name="<%=frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_RECEIVESTATUS]%>" value="<%=(indexPrStatus)%>" <%if(getTruedFalse(srcMatReceive.getReceivestatus(),indexPrStatus)){%>checked<%}%>><%=strPrStatus%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  
                                                           <%
                                                            indexPrStatus = 7; 
                                                            strPrStatus = "&nbsp; Posted";
                                                           %>
                                                           <input type="checkbox" class="form-control" name="<%=frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_RECEIVESTATUS]%>" value="<%=(indexPrStatus)%>" <%if(getTruedFalse(srcMatReceive.getReceivestatus(),indexPrStatus)){%>checked<%}%>><%=strPrStatus%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  

                                                         
                                                     
                                                 </div>
                                                           
                                                <div class="form-group">
                                                    <label for="exampleInputEmail1"><%=getJspTitle(8,SESS_LANGUAGE,recCode,false)%></label><br />
                                                    <% 
                                                        Vector key_sort = new Vector(1,1); 						  
                                                        Vector val_sort = new Vector(1,1); 
                                                        int maxItem = textListHeader[0].length-1;
                                                        for(int i=0; i<maxItem; i++){ 
                                                                key_sort.add(""+i);							
                                                                val_sort.add(""+getJspTitle(i,SESS_LANGUAGE,recCode,false)); 
                                                        }
                                                        String select_sort = ""+srcMatReceive.getReceivesortby(); 
                                                        out.println("&nbsp;"+ControlCombo.drawBootsratap(frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_RECEIVESORTBY], null, select_sort,key_sort,val_sort," onKeyDown=\"javascript:fnTrapKD()\"","form-control"));
                                                  %>
                                                    

                                                </div>          

                                                           
                                             
                                             
                                        </div>
                                     <!-- col-md-7 -->  
                                </div>
                               <br />
                                <div class="box-footer">
                                        <button  onclick="javascript:cmdSearch()" type="submit" class="btn btn-primary">Search</button>
                                        
                                        <% if(privAdd){%>
                                         <button  onclick="javascript:cmdAddPO()" type="submit" class="btn btn-primary">Tambah Penerimaan Dengan PO</button>
                                         <button  onclick="javascript:cmdAdd()" type="submit" class="btn btn-primary">Tambah Penerimaan Tanpa PO</button>
                                        
                                        <%}%>
                                        
                                </div>
                               
                                  <br />      
                                        
                                 <%if(iCommand==Command.LIST || iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand == Command.LAST){%>	        
                                <div class="row">
                                    <div class="col-md-12">
                                         <%=drawList(SESS_LANGUAGE,records,start,docType,i_status)%>   
                                         <span class="command">
                                        <%
                                             ctrLine.setLocationImg(approot+"/images");
                                             ctrLine.initDefault();
                                             out.println(ctrLine.drawImageListLimit(iCommand,vectSize,start,recordToGet));
                                        %>
                                       </span>
                                    </div>
                                       
                                </div>
                                     <br />  
                                       
                                 <div class="row">     
                                    <div class="col-md-12">   
                                       
                                       
                                       <% if(privAdd){%>
                                         <button  onclick="javascript:cmdAddPO()" type="submit" class="btn btn-primary">Tambah Penerimaan Dengan PO</button>
                                         <button  onclick="javascript:cmdAdd()" type="submit" class="btn btn-primary">Tambah Penerimaan Tanpa PO</button>
                                        
                                        
                                       <%}%>
                                       
                                    </div>
                                    
                                </div>        
                              <%}%>        
                                        
                                        
                                        
                            </div>
                        </div>
                    </form>
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
</html>

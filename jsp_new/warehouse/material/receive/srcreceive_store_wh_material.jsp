<%@page import="com.dimata.posbo.entity.warehouse.MatReceive"%>
<%@page import="com.dimata.posbo.form.warehouse.CtrlMatReceive"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
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
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_LOCATION_RECEIVE, AppObjInfo.OBJ_LOCATION_RECEIVE); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

public static final String textListGlobal[][] = {
	{"Penerimaan","Dari Toko/Gudang","Pencarian","Daftar","Edit","Tidak ada data penerimaan"},
	{"Recive","From Store/Warehouse","Search","List","Edit","Receive","No receive data available"}
};

// this constant used to list text of listHeader 
public static final String textListHeader[][] = {
	{"Nomor","Penerimaan Dari","Lokasi Penerimaan","Tanggal","Status","Urut Berdasar","Semua Tanggal","Tanggal Awal","Tanggal Akhir","Semua Lokasi", "Pilih Semua", "Hapus Pilih Semua" },
	{"Number","Recive From","Receive Location","Date","Status","Sort By","All Date","From","to","All Location", "Select All", "Cancel Select All"}	
};

public static final String textListMaterialHeader[][] =  {
	{"No","Nomor","Tanggal","Penerimaan Dari","Lokasi Penerimaan","Status","Keterangan", "Nomor BC"},
	{"No","Number","Date","Receive From","Receive Location","Status","Remark", "Customs Number"}
};


public String drawList(int language,Vector objectClass,int start,int docType,I_DocStatus i_status, String dutyFree) {
	String result = "";
	if(objectClass!=null && objectClass.size()>0) {
		ControlList ctrlist = new ControlList();
		Vector temp = (Vector) objectClass.get(0);
		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		ctrlist.addHeader(textListMaterialHeader[language][0],"3%");
		ctrlist.addHeader(textListMaterialHeader[language][1],"15%");		
		ctrlist.addHeader(textListMaterialHeader[language][2],"10%");
if(dutyFree.equals("1")){
		ctrlist.addHeader(textListMaterialHeader[language][7],"10%");
}
		ctrlist.addHeader(textListMaterialHeader[language][3],"15%");
		ctrlist.addHeader(textListMaterialHeader[language][4],"15%");
		ctrlist.addHeader(textListMaterialHeader[language][5],"10%");
		ctrlist.addHeader(textListMaterialHeader[language][6],"27%");
	
		ctrlist.setLinkRow(1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();
		
		if(start < 0) {
			start = 0;		
		}	
		
		Hashtable hashListLocation = PstLocation.getHashListLocation();
		String lokasiAsal = "";
		String lokasiTerima = "";
		
		for(int i=0; i<objectClass.size(); i++) {
			Vector vt = (Vector)objectClass.get(i);
			MatReceive rec = (MatReceive)vt.get(0);
			Location loc = null;
			if(vt.size() > 3){
				loc = (Location)vt.get(3);
			}
			start = start + 1;
			lokasiAsal = (String)hashListLocation.get(String.valueOf(rec.getReceiveFrom()));
			lokasiTerima = (String)hashListLocation.get(String.valueOf(rec.getLocationId()));
			
			Vector rowx = new Vector();				
			rowx.add(""+start);//1
			rowx.add(rec.getRecCode());//2
			
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
	
			rowx.add(str_dt_RecDate);//3
			if(vt.size() > 3){
				//rowx.add("");	
			}
if(dutyFree.equals("1")){
			rowx.add((rec.getNomorBc() != null ? rec.getNomorBc() : "-"));//4
}
			rowx.add(lokasiAsal);//loc.getName()); 5
			rowx.add(lokasiTerima);		//	6
			rowx.add(i_status.getDocStatusName(docType,rec.getReceiveStatus()));// 7
			rowx.add(rec.getRemark());//8
	
			lstData.add(rowx);
			lstLinkData.add(String.valueOf(rec.getOID()));
		}
		result = ctrlist.draw();
	}
	else
	{
		result = "<div class=\"msginfo\">&nbsp;&nbsp;"+textListGlobal[language][5]+"</div>";		
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
// get approval status for create document 
I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();
I_Approval i_approval = (I_Approval) Class.forName(approvalClassName).newInstance();
I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();
int systemName = I_DocType.SYSTEM_MATERIAL;
int docType = i_pstDocType.composeDocumentType(systemName,I_DocType.MAT_DOC_TYPE_LMRR);
boolean privManageData = true;
%>

 
<%
// get data from 'hidden form'
int iCommand = FRMQueryString.requestCommand(request);

String dutyFree = PstSystemProperty.getValueByName("ENABLE_DUTY_FREE");
// declaration of some identifier
String recCode = "";//i_pstDocType.getDocCode(docType);
String recTitle = textListGlobal[SESS_LANGUAGE][0];//i_pstDocType.getDocTitle(docType);

int iErrCode = FRMMessage.ERR_NONE;
String msgStr = "";

ControlLine ctrLine = new ControlLine();
CtrlMatReceive ctrlMatReceive = new CtrlMatReceive(request);
SrcMatReceive srcMatReceive = new SrcMatReceive();
SessMatReceive sessMatReceive = new SessMatReceive();
FrmSrcMatReceive frmSrcMatReceive = new FrmSrcMatReceive(request, srcMatReceive);



int start = FRMQueryString.requestInt(request, "start");
int recordToGet = 20;
int vectSize = 0;
String whereClause = "";
String retItemTitle = recTitle + " Item";

// get request data from current form
long oidMatReceive = FRMQueryString.requestLong(request, "hidden_receive_id");

// ControlLine 

/*try {
	srcMatReceive = (SrcMatReceive)session.getValue(SessMatReceive.SESS_SRC_MATRECEIVE);
}
catch(Exception e) {
	srcMatReceive = new SrcMatReceive();
}

if(srcMatReceive==null) {
	srcMatReceive = new SrcMatReceive();
}

try {
	session.removeValue(SessMatReceive.SESS_SRC_MATRECEIVE);
}
catch(Exception e) {
}*/


/** gel list location */
Vector obj_locationid = new Vector(1,1); 
Vector val_locationid = new Vector(1,1); 
Vector key_locationid = new Vector(1,1);

val_locationid.add("0");
key_locationid.add(textListHeader[SESS_LANGUAGE][9]);
//Vector vctLocation = PstLocation.list(0,0,"",PstLocation.fieldNames[PstLocation.FLD_NAME]);
//add opie-eyek
//algoritma : di check di sistem usernya dimana saja user tsb bisa melakukan create document


whereClause = " ("+PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE +
                   " OR "+PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE +")";

whereClause += " AND "+PstDataCustom.whereLocReportView(userId, "user_create_document_location");

Vector vctLocation = PstLocation.listLocationCreateDocument(0, 0, whereClause, "");
for(int d=0; d<vctLocation.size();d++) {
	Location location = (Location)vctLocation.get(d);
	val_locationid.add(""+location.getOID());
	key_locationid.add(location.getName());
        
}        


String sOidNumber = FRMQueryString.requestString(request,frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_RECEIVENUMBER] ); 
int oidDate = FRMQueryString.requestInt(request, frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_RECEIVEDATESTATUS]);
int oidSortBy = FRMQueryString.requestInt(request,frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_RECEIVESORTBY]);
long oidLoc = FRMQueryString.requestLong(request,frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_LOCATION_ID]); 
long oidLocFrom = FRMQueryString.requestLong(request,frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_LOCATION_FROM]); 
  

if (oidDate == 1 || sOidNumber!= ""  || oidSortBy != 0 || oidLocFrom !=0 || oidLoc !=0 && iCommand != Command.FIRST){  
    frmSrcMatReceive.requestEntityObject(srcMatReceive);
}
    Vector vectSt = new Vector(1,1);
    String[] strStatus = request.getParameterValues(FrmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_RECEIVESTATUS]);
    if(strStatus!=null && strStatus.length>0) {
        for(int i=0; i<strStatus.length; i++) {
            try {
                vectSt.add(strStatus[i]);
            } catch(Exception exc) {
                System.out.println("err");
            }
        }
    }
    srcMatReceive.setReceivestatus(vectSt);

String whereClausex = PstDataCustom.whereLocReportView(userId, "user_create_document_location");

vectSize = sessMatReceive.getCountSearchNonSupplier(srcMatReceive,whereClausex);
if(iCommand==Command.FIRST || iCommand==Command.NEXT || iCommand==Command.PREV || iCommand==Command.LAST || iCommand==Command.LIST) {
    start = ctrlMatReceive.actionList(iCommand,start,vectSize,recordToGet);
}
Vector records = sessMatReceive.searchMatReceiveNonSupplier(srcMatReceive,start,recordToGet,whereClausex);    


          
 


%>
<html>
<head>
<title>Dimata - ProChain POS</title>
    <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap/css/bootstrap-theme.min.css"/>
    <link rel="stylesheet" type="text/css" href="../../../styles/jquery-ui-1.12.1/jquery-ui.min.css"/>
    <!--link rel="stylesheet" type="text/css" href="../styles/jquery-ui-1.12.1/jquery-ui.theme.min.css"/-->
    <link rel="stylesheet" type="text/css" href="../../../styles/dist/css/AdminLTE.css"/>
    <link rel="stylesheet" type="text/css" href="../../../styles/dist/css/skins/_all-skins.css"/>
    <link rel="stylesheet" type="text/css" href="../../../styles/font-awesome/font-awesome.css"/>
    <link rel="stylesheet" type="text/css" href="../../../styles/iCheck/flat/blue.css"/>
    <link rel="stylesheet" type="text/css" href="../../../styles/iCheck/all.css"/>
    <link rel="stylesheet" type="text/css" href="../../../styles/select2/css/select2.min.css"/>
    <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css">
    <link rel="stylesheet" type="text/css" href="../../../styles/plugin/datatables/dataTables.bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap-notify.css"/>
    <link rel="stylesheet" type="text/css" href="../../../styles/datepicker/datepicker3.css"/>
    <link rel="stylesheet" type="text/css" href="../../../styles/prochain.css"/>
<script language="JavaScript">
function cmdAdd() {
	document.frm_recmaterial.command.value="<%=Command.ADD%>";
	document.frm_recmaterial.approval_command.value="<%=Command.SAVE%>";	
	document.frm_recmaterial.add_type.value="<%=ADD_TYPE_SEARCH%>";				
	document.frm_recmaterial.action="receive_store_wh_material_edit.jsp";
	if(compareDateForAdd()==true)
	document.frm_recmaterial.submit();
}

function cmdSearch() {
	document.frm_recmaterial.command.value="<%=Command.LIST%>";
	document.frm_recmaterial.action="srcreceive_store_wh_material.jsp";
	document.frm_recmaterial.submit();
}

function cmdEdit(oid){
	document.frm_recmaterial.hidden_receive_id.value=oid;
	document.frm_recmaterial.start.value=0;
	document.frm_recmaterial.approval_command.value="<%=Command.APPROVE%>";					
	document.frm_recmaterial.command.value="<%=Command.EDIT%>";
	document.frm_recmaterial.action="receive_store_wh_material_edit.jsp";
	document.frm_recmaterial.submit();
}

function cmdListFirst(){
	document.frm_recmaterial.command.value="<%=Command.FIRST%>";
	document.frm_recmaterial.action="srcreceive_store_wh_material.jsp";
	document.frm_recmaterial.submit();
}

function cmdListPrev(){
	document.frm_recmaterial.command.value="<%=Command.PREV%>";
	document.frm_recmaterial.action="srcreceive_store_wh_material.jsp";
	document.frm_recmaterial.submit();
}

function cmdListNext(){
	document.frm_recmaterial.command.value="<%=Command.NEXT%>";
	document.frm_recmaterial.action="srcreceive_store_wh_material.jsp";
	document.frm_recmaterial.submit();
}

function cmdListLast(){
	document.frm_recmaterial.command.value="<%=Command.LAST%>";
	document.frm_recmaterial.action="srcreceive_store_wh_material.jsp";
	document.frm_recmaterial.submit();
}

function cmdBack(){
	document.frm_recmaterial.command.value="<%=Command.BACK%>";
	document.frm_recmaterial.action="srcreceive_store_wh_material.jsp";
	document.frm_recmaterial.submit();
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
<style>
    .listgentitle {
        font-size: 12px !important;
        font-style: normal;
        font-weight: bold;
        color: #FFFFFF;
        background-color: #3e85c3 !important;
        text-align: center;
        height: 25px !important;
        border: 1px solid !important;
    }
    table.listgen {
        text-align: center;
    }

    .listgensell {
        color: #000000;
        background-color: #ffffff !important;
        border: 1px solid #3e85c3;
    }

    #CheckStatusAllBtn, #UnCheckStatusAllBtn{
        cursor: pointer;
    }

    li {
        padding-right: 15px;
    }

    select.tanggal {
        height: 30px;
        border: 1px #ccc solid;
    }
    select.formElemen {
        height: 30px;
        border: 1px #ccc solid;
        width: 28%;
    }

    .tgl {
        padding: 0px !important;
        margin: 0px !important;
    }
    .col-sm-8.dated {
        margin-bottom: 5px !important;
        padding-bottom: 0px !important;
        height: 40px;
        padding-left: 0px;
        padding-right: 0px;
        padding-top: 0px;
    }
    .col-sm-8.da {
        margin-bottom: 5px !important;
        padding-bottom: 0px !important;
        height: 40px;
        padding-left: 17px;
        padding-right: 0px;
        padding-top: 0px;
    }

    .form-control[disabled], .form-control[readonly], fieldset[disabled] .form-control {
        cursor: not-allowed;
        background-color: #fff !important;
        opacity: 1;
    }
    select {
        height: 30px !important;
        border: 1px solid #ccc !important;
        width: 100%;
    }

    .row {
        margin-bottom: 10px;
    }
    .col-sm-8 {
        margin-bottom: 10px;
    }
    label.control-label.col-sm-4 {
        padding-top: 10px;
        text-align: right;
    }
    select.tanggal {
        width: 30.9%;
    }
    i.fa.fa-backward {
        padding: 3px;
    }

    .form-control {
        font-size: 12px
    }
    li {
        padding-right: 20px;
        display: inline-block;
    }
    .list-status {
        list-style: none;
        display: block;
        margin: 0 0px 5px 0px;
        padding: 0px;
        position: relative;
        float: left;
    }
    .col-sm-8.stt {
        padding-right: 0px;
    }
    label.control-label.col-sm-4.padd {
        padding: 10px 15px 0px 0px;
    }
    label.control-label.col-sm-4.paddd {
        padding: 10px 15px 0px 0px;
        text-align: left;
    }
    select.formtest {
        width: 30.2%;
        padding-left: 0px;
    }
    a.btn.btn-primary {
        height: 15px;
        margin-left: 30px;
        padding: 10px 10px 25px 10px;
        margin-bottom: 15px;
    }
    label.control-label.col-sm-4.padddd {
        padding: 10px 0px 0px 0px;
    }
    </style>
</head> 
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">  
    <section class="content-header">
      <h1><%=textListGlobal[SESS_LANGUAGE][0]%>
        <small> <%=textListGlobal[SESS_LANGUAGE][1]%></small></h1>
      <ol class="ol">
        <li>
          <a>
            <li class="active">Transfer</li>
          </a>
        </li>
      </ol>
    </section>
    <p class="border"></p>
    <div class="container-pos">    
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
  <%if(menuUsed == MENU_PER_TRANS){%>
  <tr><td height="25" ID="TOPTITLE"><%@ include file = "../../../main/header.jsp" %></td></tr>
  <tr> <td height="20" ID="MAINMENU"><%@ include file = "../../../main/mnmain.jsp" %></td> </tr>
  <%}else{%>
   <tr bgcolor="#FFFFFF"><td height="10" ID="MAINMENU"><%@include file="../../../styletemplate/template_header.jsp" %></td></tr>
  <%}%>
  <tr> 
    <td valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">  
        <tr> 
          <td>
            <form name="frm_recmaterial" method="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="add_type" value="">			
              <input type="hidden" name="approval_command">	
               <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="hidden_receive_id" value="<%=oidMatReceive%>">
              <input type="hidden" name="<%=frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_LOCATION_TYPE]%>" value="<%= PstLocation.TYPE_LOCATION_STORE %>">
              <input type="hidden" name="<%=frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_RECEIVE_SOURCE]%>" value="<%= PstMatReceive.SOURCE_FROM_DISPATCH %>">
              <table width="100%" border="0">
                  <td width ="100%"> 
                      <div class="row">
                                <div class="col-sm-12">
                                  <!-- left side -->
                                  <div class="col-sm-4" style="padding-right: 0px;">
                                    <div class="form-group">
                                      <label class="control-label col-sm-4 padddd" for="selectNomor"><%=getJspTitle(0,SESS_LANGUAGE,recCode,true)%></label>
                                      <div class="col-sm-8" style="margin-bottom: 0px">
                                        <input id="selectNomor" class="form-control" type="text" name="<%=frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_RECEIVENUMBER] %>"  value="<%= srcMatReceive.getReceivenumber() %>" >
                                      </div>
                                    </div>
                                    <div class="form-group">
                                      <label class="control-label col-sm-4 padddd" for="lokasi-asal"><%=getJspTitle(1,SESS_LANGUAGE,recCode,true)%></label>
                                      <div class="col-sm-8" style="margin-bottom: 0px">
                                        <%=ControlCombo.draw(frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_LOCATION_FROM], null, ""+oidLocFrom, val_locationid, key_locationid, "", "formLocation")%> 
                                      </div>
                                    </div>
                                    <div class="form-group">
                                      <label class="control-label col-sm-4 padddd" for="lokasi-tujuan"><%=getJspTitle(2,SESS_LANGUAGE,recCode,true)%></label>
                                      <div class="col-sm-8" style="margin-bottom: 0px">
                                        <%=ControlCombo.draw(frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_LOCATION_ID], null, ""+oidLoc, val_locationid, key_locationid, "", "formLocation")%> 
                                      </div>
                                    </div>
                                  </div>
                                  <!-- right side -->             
                                  <div class="col-sm-4">
                                    <div class="form-group">
                                      <label class="control-label col-sm-4 padd" for="selectUrut"><%=getJspTitle(5,SESS_LANGUAGE,recCode,false)%></label>
                                      <div class="col-sm-8">
                                        <% 
                                             Vector key_sort = new Vector(1,1); 						  
                                             Vector val_sort = new Vector(1,1); 

                                             key_sort.add("0");							
                                             val_sort.add(""+getJspTitle(0,SESS_LANGUAGE,recCode,true)); 

                                             key_sort.add("1");							
                                             val_sort.add(""+getJspTitle(2,SESS_LANGUAGE,recCode,true)); 

                                             key_sort.add("2");							
                                             val_sort.add(""+getJspTitle(3,SESS_LANGUAGE,recCode,true)); 

                                             key_sort.add("3");							
                                             val_sort.add(""+getJspTitle(4,SESS_LANGUAGE,recCode,true)); 

                                             String select_sort = ""+srcMatReceive.getReceivesortby(); 
                                             out.println("&nbsp;"+ControlCombo.draw(frmSrcMatReceive.fieldNames[frmSrcMatReceive.FRM_FIELD_RECEIVESORTBY], null, select_sort,key_sort,val_sort,"","formSortBy"));
                                              %>
                                      </div>
                                    </div>
                                    <div class="form-group">
                                      <label class="control-label col-sm-4"><%=getJspTitle(4,SESS_LANGUAGE,recCode,true)%></label>
                                      <div class="col-sm-8 stt">
                                        <ul class="list-status">
                                              <%
                                                Vector vectResult = i_status.getDocStatusFor(docType);						  
                                                for(int i=0; i<vectResult.size(); i++) {
                                                 if ((i == I_DocStatus.DOCUMENT_STATUS_DRAFT) || (i == I_DocStatus.DOCUMENT_STATUS_CLOSED) || (i == I_DocStatus.DOCUMENT_STATUS_FINAL) || (i == I_DocStatus.DOCUMENT_STATUS_POSTED)|| (i == I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED)) {
                                                Vector vetTemp = (Vector)vectResult.get(i);
                                                int indexPrStatus = Integer.parseInt(String.valueOf(vetTemp.get(0)));							
                                                String strPrStatus = String.valueOf(vetTemp.get(1));
                                                 %>
                                                 <li>
                                                 <input type="checkbox" class="formElemen checks" name="<%=frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_RECEIVESTATUS]%>" value="<%=(indexPrStatus)%>" <%if(getTruedFalse(srcMatReceive.getReceivestatus(),indexPrStatus)){%>checked<%}%>>
                                                 <%=strPrStatus%>
                                                </li>
                                                 <%
                                               }
                                                }
                                                %>
                                        </ul>
                                        <a id="CheckStatusAllBtn">
                                          <%= textListHeader[SESS_LANGUAGE][10]%>
                                        </a>
                                        |
                                        <a id="UnCheckStatusAllBtn">
                                          <%= textListHeader[SESS_LANGUAGE][11]%>
                                        </a>
                                      </div>
                                    </div> 
                                  </div>
                                  <!--Nomor BC-->
                                  <div class="col-sm-4">
                                    <!--Tanggal-->
                                    <div class="form-group">
                                      <label class="control-label col-sm-4 paddd" for="CustomeDate"><%=textListHeader[SESS_LANGUAGE][7]%> </label>
                                      <div class="col-sm-8 dated">
                                        <input type="radio" name="<%=frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_RECEIVEDATESTATUS] %>" <%if(srcMatReceive.getReceivedatestatus()==1){%>checked<%}%> value="1">
                                        <%=ControlDate.drawDate(frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_RECEIVEFROMDATE], (srcMatReceive.getReceivefromdate() == null) ? new Date() : srcMatReceive.getReceivefromdate(),"formElemen",1,-5)%>
                                      </div>  
                                    </div>
                                    <div class="form-group">
                                      <label class="control-label col-sm-4 paddd" style="padding-top: 10px"><%=textListHeader[SESS_LANGUAGE][8]%> </label>
                                      <div class="col-sm-8 da">
                                        <%=ControlDate.drawDate(frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_RECEIVETODATE], (srcMatReceive.getReceivetodate() == null) ? new Date() : srcMatReceive.getReceivetodate(),"formtest",1,-5) %>
                                      </div>  
                                    </div>
                                    <div class="form-group tgl">
                                      <label class="col-sm-4"></label>
                                      <div class="col-sm-8 dated">
                                        <input id="allDate" type="radio" name="<%=frmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_RECEIVEDATESTATUS] %>" <%if(srcMatReceive.getReceivedatestatus()==0){%>checked<%}%> value="0"> <label for="allDate" >&nbsp;<%=textListHeader[SESS_LANGUAGE][6]%></label>
                                      </div>
                                    </div>
                                  </div> 
                                </div>
                              </div>
                  </td> 
              </table>
            </form>
            <!-- #EndEditable --></td> 
        </tr> 
        <tr align="left" valign="top">
		<td height="18" valign="top" align="left" colspan="2">
		    <table width="36%" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td class="command" nowrap width=""><a class="btn-primary btn-lg" href="javascript:cmdSearch()"><i class="fa fa-search"> &nbsp;<%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][0],ctrLine.CMD_SEARCH,true)%></i></a></td>
                             
                        <%if(privAdd){%>
                        <td width="" nowrap class="command"><a class="btn-primary btn-lg" href="javascript:cmdAdd()"><i class="fa fa-plus-circle"> &nbsp;<%=ctrLine.getCommand(SESS_LANGUAGE,recTitle,ctrLine.CMD_ADD,true)%></i></a></td>
                        <%}%>
                        
                      </tr>
                    </table>
	       </td>
      </tr>
      
      
       <tr align="left" valign="top">
		<td height="18" valign="top" align="left" colspan="2">
		    <table width="36%" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td nowrap width="1%">&nbsp; </td>
                        <td class="command" nowrap width="26%">&nbsp; </td>
                        <%if(privAdd){%>
                        <td width="5%" nowrap>&nbsp; </td>
			<td width="44%" nowrap class="command">&nbsp; </td>
                        <%}%>
                      </tr>
                    </table>
	       </td>
      </tr>
        
        
         <%if(iCommand==Command.LIST || iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand == Command.LAST){%>	 
        <tr align="left" valign="top"> 
            <td height="22" valign="middle" colspan="3"><%=drawList(SESS_LANGUAGE,records,start,docType,i_status, dutyFree)%></td>
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
                        <td height="25" valign="top" width="9%" align="left">&nbsp;</td>
                     </tr>  
                      <tr>
                        <%if(privAdd){%>
                        <td width="44%" nowrap class="command"><a class="btn-primary btn-lg" href="javascript:cmdAdd()"><i class="fa fa-plus-circle"> &nbsp;<%=ctrLine.getCommand(SESS_LANGUAGE,recTitle,ctrLine.CMD_ADD,true)%></i></a></td>
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
    </div>
		<script src="../../../styles/AdminLTE-2.3.11/plugins/jQuery/jquery-2.2.3.min.js"></script>
		<script src="../../../styles/AdminLTE-2.3.11/plugins/jQueryUI/jquery-ui.min.js"></script>
		<script src="../../../styles/AdminLTE-2.3.11/bootstrap/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="../../../styles/plugin/datatables/jquery.dataTables.min.js"></script>
		<script type="text/javascript" src="../../../styles/plugin/datatables/dataTables.bootstrap.min.js"></script>
		<script src="../../../styles/AdminLTE-2.3.11/dist/js/app.min.js"></script>
  <script>
        $(document).ready(function () {

            $(".checks").click(function () {
                var checkboxVal = "";
                $(".checks").each(function (i) {
                    if ($(this).is(":checked")) {
                        if (checkboxVal.length == 0) {
                            checkboxVal += "" + $(this).val();
                        } else {
                            checkboxVal += "," + $(this).val();
                        }
                    }
                });

                if (checkboxVal.length > 0) {
                    $("#testCheck").val("1");
                } else {
                    $("#testCheck").val("0");
                }
            });
            
        $('#CheckStatusAllBtn').click(function(){
					console.log("KLIKZZ");
					$('.list-status li input:checkbox').prop('checked', true);
				});
				$('#UnCheckStatusAllBtn').click(function(){
					console.log("KLIKZZ");
					$('.list-status li input:checkbox').prop('checked', false);
				});
            
        });
    </script>
</body>
<!-- #EndTemplate -->
</html>

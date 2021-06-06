<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*,
                   com.dimata.posbo.entity.warehouse.PstSourceStockCode,
                   com.dimata.gui.jsp.ControlList,
                   com.dimata.posbo.entity.masterdata.*,
                   com.dimata.posbo.entity.warehouse.MatStockOpnameItem,
                   com.dimata.qdep.entity.I_PstDocType,
				   com.dimata.qdep.form.FRMHandler,
                   com.dimata.qdep.form.FRMQueryString,
                   com.dimata.posbo.entity.search.SrcMatStockOpname,
                   com.dimata.posbo.session.warehouse.SessMatStockOpname,
                   com.dimata.qdep.form.FRMMessage,
                   com.dimata.posbo.entity.warehouse.MatStockOpname,
                   com.dimata.posbo.form.warehouse.FrmMatStockOpname,
                   com.dimata.posbo.form.warehouse.CtrlMatStockOpname,
                   com.dimata.gui.jsp.ControlLine,
                   com.dimata.util.Command,
                   com.dimata.posbo.entity.warehouse.PstMatStockOpnameItem,
                   com.dimata.common.entity.contact.PstContactList,
                   com.dimata.common.entity.contact.ContactList,
                   com.dimata.common.entity.contact.PstContactClass,
                   com.dimata.gui.jsp.ControlCombo,
                   com.dimata.common.entity.location.PstLocation,
                   com.dimata.common.entity.location.Location,
                   com.dimata.gui.jsp.ControlDate" %>

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK, AppObjInfo.OBJ_STOCK_OPNAME); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<%!
public static final String textListGlobal[][] = {
	{"Stok","Opname","Pencarian","Daftar","Edit","Tidak ada data opname","Cetak Opname","Material dengan barcode yang sama atau mirip lebih dari 1"},
	{"Stock","Opname","Search","List","Edit","No opname data available","Print Opname","Material with same barcode more than 1"}
};

/* this constant used to list text of listHeader */
public static final String textListOrderHeader[][] = {
	{"Nomor","Lokasi","Tanggal","Waktu","Status","Supplier","Kategori","Sub Kategori","Keterangan","Semua"},
	{"Number","Location","Date","Jam","Status","Supplier","Category","Sub Category","Remark","All"}
};

/* this constant used to list text of listMaterialItem */
public static final String textListOrderItem[][] = {
	{"No","Sku","Nama Barang","Unit","Kategori","Sub Kategori","Qty Opname","Hapus"},//7
	{"No","Code","Name","Unit","Category","Sub Category","Qty Opname","Delete"}
};


public static final String textDelete[][] = {
    {"Apakah Anda Yakin Akan Menghapus Data ?"},
    {"Are You Sure to Delete This Data? "}
};


/**
* this method used to list all stock opname item
*/
public Vector drawListOpnameItem(int language,Vector objectClass,int start,boolean privManageData,String approot) {
    Vector list = new Vector(1,1);
	Vector listError = new Vector(1,1);
	String result = "";
	if(objectClass!=null && objectClass.size()>0) {
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		ctrlist.addHeader(textListOrderItem[language][0],"3%");
		ctrlist.addHeader(textListOrderItem[language][1],"8%");
		ctrlist.addHeader(textListOrderItem[language][2],"25%");
		ctrlist.addHeader(textListOrderItem[language][3],"5%");
		//ctrlist.addHeader(textListOrderItem[language][4],"15%");
		//ctrlist.addHeader(textListOrderItem[language][5],"15%");
		ctrlist.addHeader(textListOrderItem[language][6],"10%");
                ctrlist.addHeader(textListOrderItem[language][7],"10%");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		Vector rowx = new Vector(1,1);
		ctrlist.reset();
		int index = -1;
		if(start<0)
		{
			start=0;
		}

                int soItemCounter= 0;
                long soItemSameMaterialId=0;

		for(int i=0; i<objectClass.size(); i++)
		{
                         System.out.println("===>>>>>>>>> proses xxxxx "+i);
                         Vector temp = (Vector)objectClass.get(i);
			 MatStockOpnameItem soItem = (MatStockOpnameItem)temp.get(0);
			 Material mat = (Material)temp.get(1);
			 Unit unit = (Unit)temp.get(2);
			 //Category cat = (Category)temp.get(3);
			 //SubCategory scat = (SubCategory)temp.get(4);
                         //counter
                         //
                         if(i == 0){
                            soItemCounter = soItem.getStockOpnameCounter();
                            soItemSameMaterialId = soItem.getMaterialId();
                         }


			 rowx = new Vector();
			 start = start + 1;

			 rowx.add(""+start+"");
			 //rowx.add("<a href=\"javascript:editItem('"+String.valueOf(soItem.getOID())+"')\">"+mat.getSku()+"</a>");
                            if(privManageData){
                                rowx.add("<a href=\"javascript:editItem('"+String.valueOf(soItem.getOID())+"')\">"+mat.getSku()+"</a>");
                            }else{
                                rowx.add(""+mat.getSku());
                            }
			 //rowx.add(mat.getName());
                         //update opie-eyek 31-12-2012
                         if((soItemCounter!=0 && soItemCounter == soItem.getStockOpnameCounter() && i!=0) || (i!=0 && soItemSameMaterialId == soItem.getMaterialId())){
                         //rowx.add("<font color=\"##347C17\" align=\"left\">"+mat.getName()+"</font>");
                         rowx.add("<font color=\"#FF0080\" align=\"left\">"+mat.getName()+"</font>");

                         soItemCounter = soItem.getStockOpnameCounter();
                        }
                        
                         else {
                             rowx.add(mat.getName());
                             soItemCounter = soItem.getStockOpnameCounter();
                             soItemSameMaterialId = soItem.getMaterialId();
                         }
			 rowx.add(unit.getCode());
			 //rowx.add(cat.getName());
			 //rowx.add(scat.getName());
             System.out.println("===>>>>>>>>> proses xxxxx "+i);
             if(mat.getRequiredSerialNumber()==PstMaterial.REQUIRED){
                String where = PstSourceStockCode.fieldNames[PstSourceStockCode.FLD_SOURCE_ID]+"="+soItem.getOID();
                int cnt = PstSourceStockCode.getCount(where);
                if(cnt<soItem.getQtyOpname()){
                    if(listError.size()==0){
                        listError.add("Pesan Kesalahan: ");
                    }
                    listError.add(""+listError.size()+". Jumlah Serial kode barang '"+mat.getName()+"' tidak sama dengan jumlah qty opname");
                }
                rowx.add("<div align=\"right\"><a href=\"javascript:stockcode('"+String.valueOf(soItem.getOID())+"')\">[ST.CD]</a> "+String.valueOf(soItem.getQtyOpname())+"</div>");
             }else{
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(soItem.getQtyOpname())+"</div>");
             }
             // add by fitra 17-05-2014
             if(privManageData){
            rowx.add("<div align=\"center\"> <a href=\"javascript:cmdNewDelete('"+String.valueOf(soItem.getOID())+"')\"><img src="+approot+"/images/x3.png align=\"center\" ></a></div>");
            }else{
            rowx.add("");
            }
             
             
			lstData.add(rowx);
        }
        
		result = ctrlist.drawBootstrap();
	}else{
                if(language==0){
                    result = "<div class=\"msginfo\">&nbsp;&nbsp;Tidak ada item opname ...</div>";
                }else{
                    result = "<div class=\"msginfo\">&nbsp;&nbsp;There are no opname items ...</div>";
                }
	}

    list.add(result);
	list.add(listError);
	return list;
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
int docType = i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL,I_DocType.MAT_DOC_TYPE_OPN);
//int ownIndex = i_approval.getUserApprovalIndex(I_DocType.SYSTEM_MATERIAL,docType,deptx.getOID(),sectx.getOID(),postx.getOID());
//long appMappingId = i_approval.getUserApprovalId(I_DocType.SYSTEM_MATERIAL,docType,deptx.getOID(),sectx.getOID(),postx.getOID(),ownIndex);
%>


<%
/**
* get request data from current form
*/
int iCommand = FRMQueryString.requestCommand(request);
int prevCommand = FRMQueryString.requestInt(request,"prev_command");
int startItem = FRMQueryString.requestInt(request,"start_item");
int cmdItem = FRMQueryString.requestInt(request,"command_item");
int appCommand = FRMQueryString.requestInt(request,"approval_command");
long oidStockOpname = FRMQueryString.requestLong(request, "hidden_opname_id");
long oidLocation = FRMQueryString.requestLong(request, "hidden_location_id");

SrcMatStockOpname srcMatStockOpname = new SrcMatStockOpname();
if(session.getValue(SessMatStockOpname.SESS_SRC_MATSTOCKOPNAME)!=null){
    srcMatStockOpname = (SrcMatStockOpname)session.getValue(SessMatStockOpname.SESS_SRC_MATSTOCKOPNAME);
	session.putValue(SessMatStockOpname.SESS_SRC_MATSTOCKOPNAME, srcMatStockOpname);
}

/**
* initialization of some identifier
*/
String errMsg = "";
int iErrCode = FRMMessage.ERR_NONE;

/**
* dispatch code and title
*/
String soCode = i_pstDocType.getDocCode(docType);
String opnTitle = "Opname"; //i_pstDocType.getDocTitle(docType);
String soItemTitle = opnTitle + " Item";

/**
* action process
*/
ControlLine ctrLine = new ControlLine();
CtrlMatStockOpname ctrlMatStockOpname = new CtrlMatStockOpname(request);

//by dyas 20131127
//tambah userName dan userId
iErrCode = ctrlMatStockOpname.action(iCommand , oidStockOpname, userName, userId);
FrmMatStockOpname frmso = ctrlMatStockOpname.getForm();
MatStockOpname so = ctrlMatStockOpname.getMatStockOpname();
errMsg = ctrlMatStockOpname.getMessage();

/**
* if iCommand = Commmand.ADD ---> Set default rate which value taken from PstCurrencyRate
*/
//double curr = PstCurrencyRate.getLastCurrency();
String priceCode = "Rp.";
/**
* check if document already closed or not
*/
boolean documentClosed = false;
if(so.getStockOpnameStatus()==I_DocStatus.DOCUMENT_STATUS_POSTED)
{
	documentClosed = true;
}

    System.out.println("===>>>>>>>>> proses 1.0");
/**
* check if document may modified or not
*/
boolean privManageData = true;
if(so.getStockOpnameStatus()!= I_DocStatus.DOCUMENT_STATUS_DRAFT){
    privManageData=false;
}

/**
* list purchase order item
*/
oidStockOpname = so.getOID();
oidLocation = so.getLocationId();
int recordToGetItem = 20;
int vectSizeItem = PstMatStockOpnameItem.getCount(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID] + " = " + oidStockOpname);
Vector listMatStockOpnameItem = PstMatStockOpnameItem.list(startItem,recordToGetItem,oidStockOpname);
    System.out.println("===>>>>>>>>> proses 2");
int getCounter = SessMatStockOpname.getCountCounterList(oidStockOpname);
//Get sub category name
String subcategory_name = "";
try
{
    System.out.println("===>>>>>>>>> proses 3");
    SubCategory scat = new SubCategory(); // PstSubCategory.fetchExc(so.getSubCategoryId());
	subcategory_name = scat.getName();
    System.out.println("===>>>>>>>>> proses 4");
}
catch(Exception yyy)
{
	System.out.println(yyy);
	subcategory_name = "";
}


//list opie-eyek 20131216
Vector list = drawListOpnameItem(SESS_LANGUAGE,listMatStockOpnameItem,startItem,privManageData,approot); 
//out.println(""+list.get(0));
Vector listError = (Vector)list.get(1);

if(iCommand==Command.DELETE && iErrCode==0)
{
%>
	<jsp:forward page="mat_opname_list.jsp">
	<jsp:param name="command" value="<%=Command.FIRST%>"/>
	</jsp:forward>
<%
}
    System.out.println("===>>>>>>>>> proses 5");
%>

<!-- End of Jsp Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
//------------------------- START JAVASCRIPT FUNCTION FOR PO MAIN -----------------------
function cmdEdit(oid){
	document.frm_matopname.command.value="<%=Command.EDIT%>";
	document.frm_matopname.prev_command.value="<%=prevCommand%>";
	document.frm_matopname.action="m_mat_opname_edit.jsp";
	document.frm_matopname.submit();
}

function stockcode(oid){
    document.frm_matopname.hidden_opname_item_id.value=oid;
	document.frm_matopname.command.value="<%=Command.EDIT%>";
	document.frm_matopname.prev_command.value="<%=prevCommand%>";
	document.frm_matopname.action="op_stockcode.jsp";
	document.frm_matopname.submit();
}

function compare(){
	var dt = document.frm_matopname.<%=FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_STOCK_OPNAME_DATE]%>_dy.value;
	var mn = document.frm_matopname.<%=FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_STOCK_OPNAME_DATE]%>_mn.value;
	var yy = document.frm_matopname.<%=FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_STOCK_OPNAME_DATE]%>_yr.value;
	var dt = new Date(yy,mn-1,dt);
	var bool = new Boolean(compareDate(dt));
	return bool;
}

function cmdSave()
{
	<%	if (so.getStockOpnameStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED)
		{
                    if(so.getStockOpnameStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT && getCounter>1){%>
                       alert("<%=textListGlobal[SESS_LANGUAGE][7]%>");
                  <%  }
                    else {
	%>
		//var textSubKategori = document.frm_matopname.txt_subcategory.value;
		//if (textSubKategori == "")
		//{
			//document.frm_matopname.<%=FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_SUB_CATEGORY_ID]%>.value = "0";
		//}
		document.frm_matopname.command.value="<%=Command.SAVE%>";
		document.frm_matopname.prev_command.value="<%=prevCommand%>";
		document.frm_matopname.action="m_mat_opname_edit.jsp";
		if(compare()==true)
			document.frm_matopname.submit();
                <% } %>
	<%
		}
		else
		{
	%>
	alert("Document has been posted !!!");
	<%
		}
	%>



}

function cmdAsk(oid){
	document.frm_matopname.command.value="<%=Command.ASK%>";
	document.frm_matopname.prev_command.value="<%=prevCommand%>";
	document.frm_matopname.action="m_mat_opname_edit.jsp";
	document.frm_matopname.submit();
}

function cmdCancel(){
	document.frm_matopname.command.value="<%=Command.CANCEL%>";
	document.frm_matopname.prev_command.value="<%=prevCommand%>";
	document.frm_matopname.action="m_mat_opname_edit.jsp";
	document.frm_matopname.submit();
}
// addd by Fitra
function cmdDelete(oid){
	document.frm_matopname.command.value="<%=Command.DELETE%>";

	document.frm_matopname.prev_command.value="<%=prevCommand%>";
	document.frm_matopname.approval_command.value="<%=Command.DELETE%>";
	document.frm_matopname.action="m_mat_opname_edit.jsp";
	document.frm_matopname.submit();
}

function cmdConfirmDelete(oid){
	document.frm_matopname.command.value="<%=Command.DELETE%>";
        document.frm_matopname.hidden_opname_item_id.value=oid;
	document.frm_matopname.prev_command.value="<%=prevCommand%>";
	document.frm_matopname.approval_command.value="<%=Command.DELETE%>";
	document.frm_matopname.action="m_mat_opname_item.jsp";
	document.frm_matopname.submit();
}

// add by fitra 17-05-2014
function cmdNewDelete(oid){
var msg;
msg= "<%=textDelete[SESS_LANGUAGE][0]%>" ;
var agree=confirm(msg);
if (agree)
return cmdConfirmDelete(oid) ;
else
return cmdEdit(oid);
}
function cmdBack(){
	document.frm_matopname.command.value="<%=Command.BACK%>";
	document.frm_matopname.prev_command.value="<%=prevCommand%>";
	document.frm_matopname.action="m_mat_opname_list.jsp";
	document.frm_matopname.submit();
}

function cmdSelectSubCategory()
{
	window.open("subcategorydosearch.jsp?command=<%=Command.FIRST%>&txt_subcategory="+document.frm_matopname.txt_subcategory.value+
			"&oidCategory="+document.frm_matopname.<%=FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_CATEGORY_ID]%>.value,"material", "height=600,width=600,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes");
}

function printForm()
{
	window.open("mat_opname_print_form.jsp?hidden_opname_id=<%=oidStockOpname%>&command=<%=Command.EDIT%>","pireport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
}

//------------------------- END JAVASCRIPT FUNCTION FOR PO MAIN -----------------------


//------------------------- START JAVASCRIPT FUNCTION FOR PO ITEM -----------------------
function addItem()
{
	<%	if (so.getStockOpnameStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED)
		{
	%>
		document.frm_matopname.command.value="<%=Command.ADD%>";
		document.frm_matopname.action="m_mat_opname_item.jsp";
		if(compareDateForAdd()==true)
			document.frm_matopname.submit();
	<%
		}
		else
		{
	%>
	alert("Document has been posted !!!");
	<%
		}
	%>
}

function editItem(oid)
{
	<%	if (so.getStockOpnameStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED)
		{
	%>
		document.frm_matopname.command.value="<%=Command.EDIT%>";
		document.frm_matopname.hidden_opname_item_id.value=oid;
		document.frm_matopname.action="m_mat_opname_item.jsp";
		document.frm_matopname.submit();
	<%
		}
		else
		{
	%>
	alert("Document has been posted !!!");
	<%
		}
	%>
}

function itemList(comm){
	document.frm_matopname.command.value=comm;
	document.frm_matopname.prev_command.value=comm;
	document.frm_matopname.action="m_mat_opname_item.jsp";
	document.frm_matopname.submit();
}
//------------------------- END JAVASCRIPT FUNCTION FOR PO ITEM -----------------------



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

function viewHistoryTable() {
    var strvalue ="../../../main/historypo.jsp?command=<%=Command.FIRST%>"+
                     "&oidDocHistory=<%=oidStockOpname%>";
    window.open(strvalue,"material", "height=600,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
}
//------------------------- END JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------
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
<script language=JavaScript>
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
<link rel="stylesheet" type="text/css" href="../../../styles/style.css" />
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
                        <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
                        <li class="active">Dashboard</li>
                    </ol>
                </section>

                <!-- Main content -->
                <section class="content">
                    <form name="frm_matopname" method="post" action="">
                        <!--form hidden -->
                        <input type="hidden" name="command" value="">
                        <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                        <input type="hidden" name="start_item" value="<%=startItem%>">
                        <input type="hidden" name="command_item" value="<%=cmdItem%>">
                        <input type="hidden" name="approval_command" value="<%=appCommand%>">
                        <input type="hidden" name="hidden_opname_id" value="<%=oidStockOpname%>">
                        <input type="hidden" name="hidden_opname_item_id" value="">
                        <input type="hidden" name="hidden_location_id" value="<%=oidLocation %>">
                        <input type="hidden" name="type_doc" value="1">
                        <input type="hidden" name="<%=FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_STOCK_OPNAME_NUMBER]%>" value="<%=so.getStockOpnameNumber()%>">
                        <input type="hidden" name="<%=FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_SUB_CATEGORY_ID]%>" value="<%=so.getSubCategoryId()%>">
                        
                        
                        <!--body-->
                        <div class="box-body">
                            <div class="box-body">
                                <div class="row">
                                        <div class="col-md-4">
                                            <div class="form-group">
                                                <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][0]%></label> <br/>
                                                <%if((so.getStockOpnameNumber()).length() > 0 && iErrCode==0) {
							out.println(so.getStockOpnameNumber());
						  }
						  else {
						  	out.println("");
						  }
						  %>
                                                
                                            </div>
                                            <div class="form-group">
                                               <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][2]%></label> 
                                               <%=ControlDate.drawDateWithBootstrap(FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_STOCK_OPNAME_DATE], (so.getStockOpnameDate()==null) ? new Date() : so.getStockOpnameDate(), 1, -5, "form-control-date-medium", "")%>
                                                

                                            </div>  
                                            <div class="form-group">
                                                
                                                <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][3]%></label> <br />
                                                
                                                <%=ControlDate.drawTimeSec(FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_STOCK_OPNAME_DATE], (so.getStockOpnameDate()==null) ? new Date(): so.getStockOpnameDate(),"form-control-date-medium")%>

                                            </div>   
                                        </div>
                                    <!-- end of col-md-3  -->
                                    
                                    
                                        <div class="col-md-5"> 
                                            <div class="form-group">
                                                <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][6]%></label><br/>
                                                <% if(vectSizeItem!=0){
                                                try{
                                                    if(so.getCategoryId()!=0){
                                                        System.out.println("===>>>>>>>>> proses xxxxx ");
                                                        Category category = PstCategory.fetchExc(so.getCategoryId());
                                                        out.println(category.getName());
                                                    }else{
                                                        out.println(textListOrderHeader[SESS_LANGUAGE][9]+" "+textListOrderHeader[SESS_LANGUAGE][6]);
                                                    }
                                                }catch(Exception e){}
                                                %>
                                                <input type="hidden" name="<%=FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_CATEGORY_ID]%>" value="<%=so.getCategoryId()%>">
                                                <%}else{%>
                                                <% /*Vector materGroup = PstCategory.list(0,0,"",PstCategory.fieldNames[PstCategory.FLD_CODE]);
						  Vector vectGroupVal = new Vector(1,1);
						  Vector vectGroupKey = new Vector(1,1);
						  vectGroupVal.add(textListOrderHeader[SESS_LANGUAGE][9]+" "+textListOrderHeader[SESS_LANGUAGE][6]);
						  vectGroupKey.add("0");
						  if(materGroup!=null && materGroup.size()>0)
						  {
							  for(int i=0; i<materGroup.size(); i++)
							  {
								  Category mGroup = (Category)materGroup.get(i);
								  vectGroupVal.add(mGroup.getName());
								  vectGroupKey.add(""+mGroup.getOID());
							  }
						  }
                                                out.println(ControlCombo.draw(FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_CATEGORY_ID],"formElemen", "", ""+so.getCategoryId(), vectGroupKey, vectGroupVal, null));
                                               */ %>
                                                    <select id="<%=FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_CATEGORY_ID]%>"  name="<%=FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_CATEGORY_ID]%>" class="form-control">
                                                        <option value="-1">Semua Category</option>
                                                        <%
                                                         Vector masterCatAcak = PstCategory.list(0,0,"",PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID]);
                                                         //Category newCategory = new Category();
                                                         //add opie-eyek 20130821
                                                         Vector materGroup = PstCategory.structureList(masterCatAcak) ;
                                                         Vector vectGroupVal = new Vector(1,1);
                                                         Vector vectGroupKey = new Vector(1,1);
                                                         if(materGroup!=null && materGroup.size()>0) {
                                                             String parent="";
                                                             //Vector<Category> resultTotal= new Vector();
                                                             Vector<Long> levelParent = new Vector<Long>();
                                                             for(int i=0; i<materGroup.size(); i++) {
                                                                 Category mGroup = (Category)materGroup.get(i);
                                                                     String select="";
                                                                     if(mGroup.getOID()==so.getCategoryId()){
                                                                         select="selected";
                                                                     }
                                                                     if(mGroup.getCatParentId()!=0){
                                                                         for(int lv=levelParent.size()-1; lv > -1; lv--){
                                                                             long oidLevel=levelParent.get(lv);
                                                                             if(oidLevel==mGroup.getCatParentId()){
                                                                                 break;
                                                                             }else{
                                                                                 levelParent.remove(lv);
                                                                             }
                                                                         }
                                                                         parent="";
                                                                         for(int lv=0; lv<levelParent.size(); lv++){
                                                                            parent=parent+"&nbsp;&nbsp;";
                                                                         }
                                                                         levelParent.add(mGroup.getOID());

                                                                     }else{
                                                                         levelParent.removeAllElements();
                                                                         levelParent.add(mGroup.getOID());
                                                                         parent="";
                                                                     }
                                                                 %>
                                                                     <option value="<%=mGroup.getOID()%>" <%=select%>><%=parent%><%=mGroup.getName()%></option>
                                                                 <%
                                                             }
                                                         } else {
                                                             vectGroupVal.add("Tidak Ada Category");
                                                             vectGroupKey.add("-1");
                                                         }
                                                       %>
                                                       </select>
                                               <%}%>
                                                
                                            </div>        
                                            
                                               
                                           <div class="form-group">
                                                <label for="exampleInputEmail1"> <%=textListOrderHeader[SESS_LANGUAGE][4]%></label><br />
                                                <%
                                                    Vector obj_status = new Vector(1,1);
                                                    Vector val_status = new Vector(1,1);
                                                    Vector key_status = new Vector(1,1);

                                                    val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_DRAFT));
                                                    key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]);

                                                                            //add by fitra
                                                                            val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED));
                                                              key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED]);
                                                    if(vectSizeItem!=0 && listError.size()==0){
                                                        val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_FINAL));
                                                        key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);
                                                    }
                                                    String select_status = ""+so.getStockOpnameStatus();
                                                    if(so.getStockOpnameStatus()==I_DocStatus.DOCUMENT_STATUS_CLOSED){
                                                        out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_CLOSED]);
                                                    }else if(so.getStockOpnameStatus()==I_DocStatus.DOCUMENT_STATUS_POSTED){
                                                        out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_POSTED]);
                                                    }else{
                                                        out.println(ControlCombo.drawBootsratap(FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_STOCK_OPNAME_STATUS],null,select_status,val_status,key_status,"tabindex=\"4\"","form-control"));
                                                    }
                                                  %>
                                                
                                            </div> 
                                                  
                                            <div class="form-group">
                                                <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][1]%></label>    
                                                <%
                                                Vector val_locationid = new Vector(1,1);
                                                Vector key_locationid = new Vector(1,1);
                                                String whereClause = "";//PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE;
                                                Vector vt_loc = PstLocation.list(0,0,whereClause, PstLocation.fieldNames[PstLocation.FLD_NAME]);
                                                for(int d=0;d<vt_loc.size();d++)
                                                {
                                                        Location loc = (Location)vt_loc.get(d);
                                                        val_locationid.add(""+loc.getOID()+"");
                                                        key_locationid.add(loc.getName());
                                                }
                                                  System.out.println("===>>>>>>>>> proses xxxxx ");
                                                String select_locationid = ""+so.getLocationId(); //selected on combo box
                                                  System.out.println("===>>>>>>>>> proses xxxxx ");
                                              %>
                                              <%=ControlCombo.drawBoostrap(FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_LOCATION_ID], "form-control","","",val_locationid, key_locationid, select_locationid)%>
                                            </div>       
                                                    

                                        </div>
                                 <!-- end of col-md-5  -->
                                    
                                        <div class="col-md-3">
                                            
                                            <div class="form-group">
                                               <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][5]%></label><br />
                                                  <% if(vectSizeItem!=0) {
                                                            try {
                                                                if(so.getSupplierId()!=0) {
                                                                    ContactList contactList = PstContactList.fetchExc(so.getSupplierId());
                                                                    out.println(contactList.getCompName());
                                                                }
                                                                                                    else {
                                                                    out.println(textListOrderHeader[SESS_LANGUAGE][9]+" "+textListOrderHeader[SESS_LANGUAGE][5]);
                                                                }
                                                            }
                                                                                            catch(Exception e){
                                                                                            }
                                                      %>
                                                        <input type="hidden" name="<%=FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_SUPPLIER_ID]%>" value="<%=so.getSupplierId()%>">
                                                      <%}else{
                                                        Vector val_supplier = new Vector(1,1);
                                                        Vector key_supplier = new Vector(1,1);

                                                          String wh_supp = PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE]+
                                                                           " = "+PstContactClass.CONTACT_TYPE_SUPPLIER+
                                                                           " AND "+PstContactList.fieldNames[PstContactList.FLD_PROCESS_STATUS]+
                                                                           " != "+PstContactList.DELETE;
                                                          String ordBySupp =  " CONT." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME];
                                                          Vector vt_supp = PstContactList.listContactByClassType(0,0,wh_supp,ordBySupp);
                                                        key_supplier.add(textListOrderHeader[SESS_LANGUAGE][9]+" "+textListOrderHeader[SESS_LANGUAGE][5]);
                                                        val_supplier.add("0");
                                                        for(int d=0; d<vt_supp.size(); d++)
                                                        {
                                                                ContactList cnt = (ContactList)vt_supp.get(d);
                                                                String cntName = cnt.getCompName();
                                                                if(cntName.length()==0)
                                                                {
                                                                        cntName = cnt.getPersonName()+" "+cnt.getPersonLastname();
                                                                }
                                                                val_supplier.add(String.valueOf(cnt.getOID()));
                                                                key_supplier.add(cntName);
                                                        }
                                                        String select_supplier = ""+so.getSupplierId();
                                                  %>
                                                  <%=ControlCombo.drawBootsratap(FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_SUPPLIER_ID],null,select_supplier,val_supplier,key_supplier,"","form-control")%><%}%>
  
                                            </div> 
                                             <div class="form-group">
                                               <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][8]%></label>   
                                               <textarea name="<%=FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_REMARK]%>" class="form-control" wrap="VIRTUAL" rows="4"><%=so.getRemark()%></textarea>
                                                
                                            </div>
                                        </div>
                                </div>
                                
                               <div class="row">
                                   <div class="col-md-12">
                                       <%=list.get(0)%>
                                   </div> 
                               </div>
                                
                                
                                <div class="row">
                                    <div class="col-md-12">
                                     <%if(oidStockOpname!=0){%>
                                          <span class="command">
                                     <%
						  if(cmdItem!=Command.FIRST && cmdItem!=Command.PREV && cmdItem!=Command.NEXT && cmdItem!=Command.LAST){
								cmdItem = Command.FIRST;
						  }
						  ctrLine.setLocationImg(approot+"/images");
						  ctrLine.initDefault();
						  ctrLine.setImageListName(approot+"/images","item");

						  ctrLine.setListFirstCommand("javascript:itemList('"+Command.FIRST+"')");
						  ctrLine.setListNextCommand("javascript:itemList('"+Command.NEXT+"')");
						  ctrLine.setListPrevCommand("javascript:itemList('"+Command.PREV+"')");
						  ctrLine.setListLastCommand("javascript:itemList('"+Command.LAST+"')");

						  %>
                                                <%=ctrLine.drawImageListLimit(cmdItem,vectSizeItem,startItem,recordToGetItem)%> </span>

                                                <span class="errfont">
                                                          <%
                                                              for(int k=0;k<listError.size();k++){
                                                                      if(k==0)
                                                                              out.println(listError.get(k)+"<br>");
                                                                      else
                                                                              out.println("&nbsp;&nbsp;&nbsp;"+listError.get(k)+"<br>");
                                                              }
                                                          %>
                                                </span>
                                                 <br />
                                                <%if(so.getStockOpnameStatus()==I_DocStatus.DOCUMENT_STATUS_DRAFT ){%> 
                                                <div class="box-footer">
                                              
                                                     <button class="btn btn-success pull-left" onclick="javascript:addItem()" type="button" > Tambah Item </button>  
                                                </div>
                                                
                                                <%}%>
                                                
                                                
                                        <%}%>  
                                        <br/>
                                        
                                    </div> 
                           <!-- end col-md-12 -->  
                                 </div>
                              <!-- end of row -->  
                                
                                
                                <div class="row">
                                   <div class="col-md-12"> <%if(listMatStockOpnameItem!=null && listMatStockOpnameItem.size()>0){%>
                                   
                                    <%}%>
                                   </div>     
                                </div>
                                   
                                    <br/> 
                                <div class="row">
                                    <div class="col-md-12">
                                            <%
                                                ctrLine.setLocationImg(approot+"/images");

                                                // set image alternative caption
                                                ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,opnTitle,ctrLine.CMD_SAVE,true));
                                                ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,opnTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,opnTitle,ctrLine.CMD_BACK,true)+" List");
                                                ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,opnTitle,ctrLine.CMD_ASK,true));
                                                ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,opnTitle,ctrLine.CMD_CANCEL,false));

                                                ctrLine.initDefault();
                                                ctrLine.setTableWidth("80%");
                                                String scomDel = "javascript:cmdAsk('"+oidStockOpname+"')";
                                                String sconDelCom = "javascript:cmdDelete('"+oidStockOpname+"')";
                                                String scancel = "javascript:cmdEdit('"+oidStockOpname+"')";
                                                ctrLine.setCommandStyle("btn btn-success pull-left");
                                                ctrLine.setColCommStyle("command");

                                                // set command caption
                                                ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE,opnTitle,ctrLine.CMD_SAVE,true));
                                                ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,opnTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,opnTitle,ctrLine.CMD_BACK,true)+" List");
                                                ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE,opnTitle,ctrLine.CMD_ASK,true));
                                                ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE,opnTitle,ctrLine.CMD_DELETE,true));
                                                ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE,opnTitle,ctrLine.CMD_CANCEL,false));

                                                if(privDelete){
                                                        ctrLine.setConfirmDelCommand(sconDelCom);
                                                        ctrLine.setDeleteCommand(scomDel);
                                                        ctrLine.setEditCommand(scancel);
                                                }else{
                                                        ctrLine.setConfirmDelCaption("");
                                                        ctrLine.setDeleteCaption("");
                                                        ctrLine.setEditCaption("");
                                                }

                                                if(privAdd==false && privUpdate==false){
                                                        ctrLine.setSaveCaption("");
                                                }

                                                if(privAdd==false || so.getStockOpnameStatus()!=I_DocStatus.DOCUMENT_STATUS_DRAFT ){
                                                        ctrLine.setAddCaption("");
                                                }


                                                if(iCommand==Command.SAVE && frmso.errorSize()==0){
                                                        //siCommand=Command.EDIT;
                                                }

                                                if(documentClosed)
                                                {
                                                        ctrLine.setSaveCaption("");
                                                        ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,opnTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,opnTitle,ctrLine.CMD_BACK,true)+" List");
                                                        ctrLine.setDeleteCaption("");
                                                        ctrLine.setConfirmDelCaption("");
                                                        ctrLine.setCancelCaption("");
                                                }
                                          %>
                                          <%=ctrLine.drawImage(iCommand,iErrCode,errMsg)%>
                                        
                                    </div>

                                </div>  
                                          
                               <div class="row">
                                   <% if(listMatStockOpnameItem!=null && listMatStockOpnameItem.size()>0) { %>
                                     <div class="col-md-12">
                                       
                                         <div class="form-group">
                                            <button class="btn btn-success pull-right" onclick="javascript:printForm()" type="button" ><i class="fa fa-download"></i> <%=textListGlobal[SESS_LANGUAGE][6]%> </button>  
                                            
                                         </div>
                                     </div>
                                     
                                     <%}
                                      %>
                               </div>
                               
                               
                               <div class="row">
                                    <div class="col-md-12">
                                        <div class="form-group">
                                          <a href="javascript:viewHistoryTable()">VIEW TABEL HISTORY</a>      
                                                
                                        </div>
                                    </div>
                              </div>
                                        
                                               
                             <script language="JavaScript">
                                <% 
                                // add by fitra 10-5-2014   
                                if(so.getStockOpnameStatus()==I_DocStatus.DOCUMENT_STATUS_DRAFT && iCommand==Command.SAVE){%>  
                                addItem();
                                <% } %>
                            </script>                  
                                               
                                               
                                               
                                               
                                               
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


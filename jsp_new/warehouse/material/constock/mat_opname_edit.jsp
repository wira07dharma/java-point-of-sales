<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*,
                   com.dimata.posbo.entity.warehouse.PstSourceStockCode,
                   com.dimata.gui.jsp.ControlList,
                   com.dimata.posbo.entity.masterdata.*,
                   com.dimata.posbo.entity.warehouse.MatConStockOpnameItem,
                   com.dimata.qdep.entity.I_PstDocType,
				   com.dimata.qdep.form.FRMHandler,
                   com.dimata.qdep.form.FRMQueryString,
                   com.dimata.posbo.entity.search.SrcMatConStockOpname,
                   com.dimata.posbo.session.warehouse.SessMatConStockOpname,
                   com.dimata.qdep.form.FRMMessage,
                   com.dimata.posbo.entity.warehouse.MatConStockOpname,
                   com.dimata.posbo.form.warehouse.FrmMatConStockOpname,
                   com.dimata.posbo.form.warehouse.CtrlMatConStockOpname,
                   com.dimata.gui.jsp.ControlLine,
                   com.dimata.util.Command,
                   com.dimata.posbo.entity.warehouse.PstMatConStockOpnameItem,
                   com.dimata.common.entity.contact.PstContactList,
                   com.dimata.common.entity.contact.ContactList,
                   com.dimata.common.entity.contact.PstContactClass,
                   com.dimata.gui.jsp.ControlCombo,
                   com.dimata.common.entity.location.PstLocation,
                   com.dimata.common.entity.location.Location,
                   com.dimata.gui.jsp.ControlDate" %>

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = 1;// AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK, AppObjInfo.OBJ_STOCK_OPNAME); %>
<%@ include file = "../../../main/checkuser.jsp" %>
<%!
/* this constant used to list text of listHeader */
public static final String textListOrderHeader[][] =
{
	{"Nomor","Lokasi","Tanggal","Waktu","Status","Supplier","Kategori","Sub Kategori","Keterangan","Semua"},
	{"Number","Location","Date","Jam","Status","Supplier","Category","Sub Category","Remark","All"}
};

/* this constant used to list text of listMaterialItem */
public static final String textListOrderItem[][] =
{
	{"No","Sku","Nama Barang","Unit","Kategori","Sub Kategori","Qty Opname"},
	{"No","Code","Name","Unit","Category","Sub Category","Qty Opname"}
};

/**
* this method used to list all stock opname item
*/
public Vector drawListOpnameItem(int language,Vector objectClass,int start)
{
    Vector list = new Vector(1,1);
	Vector listError = new Vector(1,1);
	String result = "";
	if(objectClass!=null && objectClass.size()>0)
	{
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

		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		Vector rowx = new Vector(1,1);
		ctrlist.reset();
		int index = -1;
		if(start<0)
		{
			start=0;
		}

		for(int i=0; i<objectClass.size(); i++)
		{
            System.out.println("===>>>>>>>>> proses xxxxx "+i);
             Vector temp = (Vector)objectClass.get(i);
			 MatConStockOpnameItem soItem = (MatConStockOpnameItem)temp.get(0);
			 Material mat = (Material)temp.get(1);
			 Unit unit = (Unit)temp.get(2);
			 //Category cat = (Category)temp.get(3);
			 //SubCategory scat = (SubCategory)temp.get(4);
			 rowx = new Vector();
			 start = start + 1;

			 rowx.add(""+start+"");
			 rowx.add("<a href=\"javascript:editItem('"+String.valueOf(soItem.getOID())+"')\">"+mat.getSku()+"</a>");
			 rowx.add(mat.getName());
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
			lstData.add(rowx);
		}
		result = ctrlist.draw();
	}else{
		result = "<div class=\"msginfo\">&nbsp;&nbsp;Tidak ada item opname ...</div>";
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

SrcMatConStockOpname srcMatConStockOpname = new SrcMatConStockOpname();
if(session.getValue(SessMatConStockOpname.SESS_SRC_MATSTOCKOPNAME)!=null){
    srcMatConStockOpname = (SrcMatConStockOpname)session.getValue(SessMatConStockOpname.SESS_SRC_MATSTOCKOPNAME);
	session.putValue(SessMatConStockOpname.SESS_SRC_MATSTOCKOPNAME, srcMatConStockOpname);
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
String opnTitle = "Opname Barang"; //i_pstDocType.getDocTitle(docType);
String soItemTitle = opnTitle + " Item";

/**
* action process
*/
ControlLine ctrLine = new ControlLine();
CtrlMatConStockOpname ctrlMatConStockOpname = new CtrlMatConStockOpname(request);
iErrCode = ctrlMatConStockOpname.action(iCommand , oidStockOpname);
FrmMatConStockOpname frmso = ctrlMatConStockOpname.getForm();
MatConStockOpname so = ctrlMatConStockOpname.getMatConStockOpname();
errMsg = ctrlMatConStockOpname.getMessage();

/**
* if iCommand = Commmand.ADD ---> Set default rate which value taken from PstCurrencyRate
*/
//double curr = PstCurrencyRate.getLastCurrency();
String priceCode = "Rp.";
/**
* check if document already closed or not
*/
boolean documentClosed = false;
if(so.getStockOpnameStatus()==I_DocStatus.DOCUMENT_STATUS_CLOSED)
{
	documentClosed = true;
}

    System.out.println("===>>>>>>>>> proses 1.0");
/**
* check if document may modified or not
*/
boolean privManageData = true;

/**
* list purchase order item
*/
oidStockOpname = so.getOID();
int recordToGetItem = 50;
int vectSizeItem = PstMatConStockOpnameItem.getCount(PstMatConStockOpnameItem.fieldNames[PstMatConStockOpnameItem.FLD_STOCK_OPNAME_ID] + " = " + oidStockOpname);
Vector listMatConStockOpnameItem = PstMatConStockOpnameItem.list(startItem,recordToGetItem,oidStockOpname);
    System.out.println("===>>>>>>>>> proses 2");

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

<html>
<!-- #BeginTemplate "/Templates/main.dwt" -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
//------------------------- START JAVASCRIPT FUNCTION FOR PO MAIN -----------------------
function cmdEdit(oid){
	document.frm_matopname.command.value="<%=Command.EDIT%>";
	document.frm_matopname.prev_command.value="<%=prevCommand%>";
	document.frm_matopname.action="mat_opname_edit.jsp";
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
	var dt = document.frm_matopname.<%=FrmMatConStockOpname.fieldNames[FrmMatConStockOpname.FRM_FIELD_STOCK_OPNAME_DATE]%>_dy.value;
	var mn = document.frm_matopname.<%=FrmMatConStockOpname.fieldNames[FrmMatConStockOpname.FRM_FIELD_STOCK_OPNAME_DATE]%>_mn.value;
	var yy = document.frm_matopname.<%=FrmMatConStockOpname.fieldNames[FrmMatConStockOpname.FRM_FIELD_STOCK_OPNAME_DATE]%>_yr.value;
	var dt = new Date(yy,mn-1,dt);
	var bool = new Boolean(compareDate(dt));
	return bool;
}

function cmdSave()
{
	<%	if (so.getStockOpnameStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED)
		{
	%>
		//var textSubKategori = document.frm_matopname.txt_subcategory.value;
		//if (textSubKategori == "")
		//{
			//document.frm_matopname.<%=FrmMatConStockOpname.fieldNames[FrmMatConStockOpname.FRM_FIELD_SUB_CATEGORY_ID]%>.value = "0";
		//}
		document.frm_matopname.command.value="<%=Command.SAVE%>";
		document.frm_matopname.prev_command.value="<%=prevCommand%>";
		document.frm_matopname.action="mat_opname_edit.jsp";
		if(compare()==true)
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

function cmdAsk(oid){
	document.frm_matopname.command.value="<%=Command.ASK%>";
	document.frm_matopname.prev_command.value="<%=prevCommand%>";
	document.frm_matopname.action="mat_opname_edit.jsp";
	document.frm_matopname.submit();
}

function cmdCancel(){
	document.frm_matopname.command.value="<%=Command.CANCEL%>";
	document.frm_matopname.prev_command.value="<%=prevCommand%>";
	document.frm_matopname.action="mat_opname_edit.jsp";
	document.frm_matopname.submit();
}

function cmdConfirmDelete(oid){
	document.frm_matopname.command.value="<%=Command.DELETE%>";
	document.frm_matopname.prev_command.value="<%=prevCommand%>";
	document.frm_matopname.approval_command.value="<%=Command.DELETE%>";
	document.frm_matopname.action="mat_opname_edit.jsp";
	document.frm_matopname.submit();
}

function cmdBack(){
	document.frm_matopname.command.value="<%=Command.BACK%>";
	document.frm_matopname.prev_command.value="<%=prevCommand%>";
	document.frm_matopname.action="mat_opname_list.jsp";
	document.frm_matopname.submit();
}

function cmdSelectSubCategory()
{
	window.open("subcategorydosearch.jsp?command=<%=Command.FIRST%>&txt_subcategory="+document.frm_matopname.txt_subcategory.value+
			"&oidCategory="+document.frm_matopname.<%=FrmMatConStockOpname.fieldNames[FrmMatConStockOpname.FRM_FIELD_CATEGORY_ID]%>.value,"material", "height=600,width=600,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes");
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
		document.frm_matopname.action="mat_opname_item.jsp";
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
		document.frm_matopname.action="mat_opname_item.jsp";
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
	document.frm_matopname.action="mat_opname_item.jsp";
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
//------------------------- END JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------
</script>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<!-- #BeginEditable "styles" -->
<link rel="stylesheet" href="../../../styles/main.css" type="text/css">
<!-- #EndEditable --> <!-- #BeginEditable "stylestab" -->
<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
<!-- #EndEditable -->
</head>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('<%=approot%>/images/BtnSaveOn.jpg')">
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
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
  <tr>
    <td width="88%" valign="top" align="left">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->&nbsp;Konsinyasi > Opname<!-- #EndEditable --></td>
        </tr>
        <tr>
          <td><!-- #BeginEditable "content" -->
            <form name="frm_matopname" method="post" action="">
<%
try
{
%>
              <input type="hidden" name="command" value="">
			  <input type="hidden" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="start_item" value="<%=startItem%>">
              <input type="hidden" name="command_item" value="<%=cmdItem%>">
              <input type="hidden" name="approval_command" value="<%=appCommand%>">
              <input type="hidden" name="hidden_opname_id" value="<%=oidStockOpname%>">
              <input type="hidden" name="hidden_opname_item_id" value="">
              <input type="hidden" name="type_doc" value="1">
              <input type="hidden" name="<%=FrmMatConStockOpname.fieldNames[FrmMatConStockOpname.FRM_FIELD_STOCK_OPNAME_NUMBER]%>" value="<%=so.getStockOpnameNumber()%>">
              <input type="hidden" name="<%=FrmMatConStockOpname.fieldNames[FrmMatConStockOpname.FRM_FIELD_SUB_CATEGORY_ID]%>" value="<%=so.getSubCategoryId()%>">
              <table width="100%" border="0">
                <tr valign="top">
                  <td colspan="3">
                    <table width="100%"  border="0" cellspacing="1" cellpadding="1">
                      <tr>
                        <td width="8%"><%=textListOrderHeader[SESS_LANGUAGE][0]%></td>
                        <td width="27%">: <b>                           <%
						  if((so.getStockOpnameNumber()).length() > 0 && iErrCode==0)
						  {
							out.println(so.getStockOpnameNumber());
						  }
						  else
						  {
						  	out.println("");
						  }
						  %></b></td>
                        <td width="8%"><%=textListOrderHeader[SESS_LANGUAGE][5]%> </td>
                        <td width="30%">:
                          <% if(vectSizeItem!=0){
                                try{
                                    if(so.getSupplierId()!=0){
                                        ContactList contactList = PstContactList.fetchExc(so.getSupplierId());
                                        out.println(contactList.getCompName());
                                    }else{
                                        out.println(textListOrderHeader[SESS_LANGUAGE][9]+" "+textListOrderHeader[SESS_LANGUAGE][5]);
                                    }
                                }catch(Exception e){}
                          %>
                            <input type="hidden" name="<%=FrmMatConStockOpname.fieldNames[FrmMatConStockOpname.FRM_FIELD_SUPPLIER_ID]%>" value="<%=so.getSupplierId()%>">
                          <%}else{
							Vector val_supplier = new Vector(1,1);
							Vector key_supplier = new Vector(1,1);

                          String wh_supp = PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE]+
                                           " = "+PstContactClass.CONTACT_TYPE_SUPPLIER+
                                           " AND "+PstContactList.fieldNames[PstContactList.FLD_PROCESS_STATUS]+
                                           " != "+PstContactList.DELETE;
                          String ordBySupp =  " CONT." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME];
                          Vector vt_supp = PstContactList.listContactByClassType(0,0,wh_supp,ordBySupp);

                            //String whClauseSupp = " CLS." + PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE] + "=" + PstContactClass.FLD_CLASS_VENDOR;
                            //String ordBySupp =  " CONT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE];
                            //Vector vt_supp = PstContactList.listContactByClassType(0,0,whClauseSupp,ordBySupp);

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
                        <%=ControlCombo.draw(FrmMatConStockOpname.fieldNames[FrmMatConStockOpname.FRM_FIELD_SUPPLIER_ID],null,select_supplier,val_supplier,key_supplier,"","formElemen")%><%}%></td>
                        <td width="10%"><%=textListOrderHeader[SESS_LANGUAGE][8]%></td>
                        <td width="17%" rowspan="4" valign="top">                          <textarea name="<%=FrmMatConStockOpname.fieldNames[FrmMatConStockOpname.FRM_FIELD_REMARK]%>" class="formElemen" wrap="VIRTUAL" rows="4"><%=so.getRemark()%></textarea></td>
                      </tr>
                      <tr>
                        <td><%=textListOrderHeader[SESS_LANGUAGE][1]%></td>
                        <td>: 
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
                        <%=ControlCombo.draw(FrmMatConStockOpname.fieldNames[FrmMatConStockOpname.FRM_FIELD_LOCATION_ID], null, select_locationid, val_locationid, key_locationid, "", "formElemen")%> </td>
                        <td><%=textListOrderHeader[SESS_LANGUAGE][6]%></td>
                        <td>: 

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
                            <input type="hidden" name="<%=FrmMatConStockOpname.fieldNames[FrmMatConStockOpname.FRM_FIELD_CATEGORY_ID]%>" value="<%=so.getCategoryId()%>">
                          <%}else{
						  Vector materGroup = PstCategory.list(0,0,"",PstCategory.fieldNames[PstCategory.FLD_CODE]);
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
                          out.println(ControlCombo.draw(FrmMatConStockOpname.fieldNames[FrmMatConStockOpname.FRM_FIELD_CATEGORY_ID],"formElemen", "", ""+so.getCategoryId(), vectGroupKey, vectGroupVal, null));
						}
                        %>
						</td>
                        <td>&nbsp;</td>
                      </tr>
                      <tr>
                        <td><%=textListOrderHeader[SESS_LANGUAGE][2]%></td>
                        <td>: <%=ControlDate.drawDateWithStyle(FrmMatConStockOpname.fieldNames[FrmMatConStockOpname.FRM_FIELD_STOCK_OPNAME_DATE], (so.getStockOpnameDate()==null) ? new Date() : so.getStockOpnameDate(), 1, -5, "formElemen", "")%></td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                      </tr>
                      <tr>
                        <td><%=textListOrderHeader[SESS_LANGUAGE][3]%></td>
                        <td>: <%=ControlDate.drawTimeSec(FrmMatConStockOpname.fieldNames[FrmMatConStockOpname.FRM_FIELD_STOCK_OPNAME_DATE], (so.getStockOpnameDate()==null) ? new Date(): so.getStockOpnameDate(),"formElemen")%></td>
                        <td>&nbsp;</td>
                        <td><%System.out.println("===>>>>>>>>> proses xxxxx ");%></td>
                        <td>&nbsp;</td>
                      </tr>
                    </table>
                  </td>
                </tr>
                <tr>
                  <td colspan="3">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr align="left" valign="top">
                        <td height="22" valign="middle" colspan="3"><%
						Vector list = drawListOpnameItem(SESS_LANGUAGE,listMatConStockOpnameItem,startItem);
							out.println(""+list.get(0));
							Vector listError = (Vector)list.get(1);
						%></td>
                      </tr>
                      <%if(oidStockOpname!=0){%>
                      <tr align="left" valign="top">
                        <td height="8" align="left" colspan="3" class="command">
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
                          <%=ctrLine.drawImageListLimit(cmdItem,vectSizeItem,startItem,recordToGetItem)%> </span> </td>
                      </tr>
                      <tr align="left" valign="top">
                        <td height="22" valign="middle" colspan="3"><span class="errfont">
                          <%
				  	for(int k=0;k<listError.size();k++){
						if(k==0)
							out.println(listError.get(k)+"<br>");
						else
							out.println("&nbsp;&nbsp;&nbsp;"+listError.get(k)+"<br>");
					}
				  %>
                        </span></td>
                      </tr>
                      <tr align="left" valign="top">
                        <td height="22" valign="middle" colspan="3">
                          <table width="50%" border="0" cellspacing="2" cellpadding="0">
                            <tr>
                              <td width="6%"><a href="javascript:addItem()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE," Item",ctrLine.CMD_ADD,true)%>"></a></td>
                              <td width="94%"><a href="javascript:addItem()"><%=ctrLine.getCommand(SESS_LANGUAGE," Item",ctrLine.CMD_ADD,true)%></a></td>
                            </tr>
                          </table>
                        </td>
                      </tr>
                      <%}%>
                    </table>
                  </td>
                </tr>
                <tr>
                  <td colspan="2" valign="top">&nbsp;</td>
                  <td width="30%">&nbsp;</td>
                </tr>
                <%//if(oidStockOpname!=0){%>
			    <%if(listMatConStockOpnameItem!=null && listMatConStockOpnameItem.size()>0){%>
                <tr>
                  <td colspan="3">&nbsp;</td>
                </tr>
				<%}%>
                <tr>
                  <td colspan="3">
                    <table width="100%" border="0">
                      <tr>
                        <td width="70%">
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
					String sconDelCom = "javascript:cmdConfirmDelete('"+oidStockOpname+"')";
					String scancel = "javascript:cmdEdit('"+oidStockOpname+"')";
					ctrLine.setCommandStyle("command");
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

					if(privAdd==false){
						ctrLine.setAddCaption("");
					}

					if(iCommand==Command.SAVE && frmso.errorSize()==0){
						iCommand=Command.EDIT;
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
                          <%=ctrLine.drawImage(iCommand,iErrCode,errMsg)%> </td>
                        <td width="30%">
                          <%
						  if(listMatConStockOpnameItem!=null && listMatConStockOpnameItem.size()>0){
						  %>
						  <table width="100%" border="0" cellpadding="0" cellspacing="0">
                            <tr>
                              <td width="5%" valign="top"><a href="javascript:printForm('<%=oidStockOpname%>')"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0"></a></td>
                              <td width="95%" nowrap>&nbsp; <a href="javascript:printForm('<%=oidStockOpname%>')" class="command" >Print
                                <%=opnTitle%> Form</a></td>
                            </tr>
                          </table>
						  <%}%>
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>
              </table>
<%
}
catch(Exception e)
{
	System.out.println(e);
}
%>
            </form>
            <!-- #EndEditable --></td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td colspan="2" height="20"> <!-- #BeginEditable "footer" -->
      <%@ include file = "../../../main/footer.jsp" %>
      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate -->
</html>

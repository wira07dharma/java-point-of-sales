<%@page import="com.dimata.posbo.form.warehouse.CtrlPriceProtectionItem"%>
<%@page import="com.dimata.posbo.form.warehouse.FrmPriceProtectionItem"%>
<%@page import="com.dimata.posbo.form.warehouse.FrmPriceProtection"%>
<%@page import="com.dimata.posbo.form.warehouse.CtrlPriceProtection"%>
<%@page import="com.dimata.posbo.session.masterdata.SessPosting"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@ page language = "java" %>
<%@ page import="com.dimata.posbo.form.warehouse.CtrlMatCosting,
                 com.dimata.posbo.form.warehouse.FrmPriceProtection,
                 com.dimata.common.entity.location.PstLocation,
                 com.dimata.common.entity.location.Location,
                 com.dimata.posbo.entity.warehouse.*,
                 com.dimata.gui.jsp.ControlList,
                 com.dimata.qdep.form.FRMHandler,
                 com.dimata.qdep.entity.I_PstDocType,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.qdep.form.FRMMessage,
                 com.dimata.util.Command,
                 com.dimata.gui.jsp.ControlDate,
                 com.dimata.gui.jsp.ControlCombo,
                 com.dimata.posbo.entity.masterdata.Material,
                 com.dimata.posbo.entity.masterdata.Unit,
                 com.dimata.posbo.entity.masterdata.PstMaterial,
                 com.dimata.posbo.form.warehouse.FrmMatDispatch,
                 com.dimata.posbo.entity.masterdata.Costing,
                 com.dimata.posbo.entity.masterdata.PstCosting"%>
				 
<%@ include file = "../../../main/javainit.jsp" %>
<%
int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_COSTING, AppObjInfo.G2_COSTING, AppObjInfo.OBJ_COSTING);
int  appObjCodeShowQtyAndPrice = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE_QTY_AND_PRICE);
%>
<%@ include file = "../../../main/checkuser.jsp" %>
<%
boolean privApproval = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_APPROVE));
boolean privShowQtyPrice = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeShowQtyAndPrice, AppObjInfo.COMMAND_VIEW));
%>
<%!

public static final String textListGlobal[][] = {
	{"Pembiayaan","Gudang","Ke Toko","Tidak ada data pembiayaan","Posting Stock","Posting Harga Beli"},
	{"Costing","Warehouse","Store","No costing data available","Posting Stock","Posting Harga Beli"}
};

/* this constant used to list text of listHeader */
public static final String textListOrderHeader[][] =
{
	{"Nomor","Lokasi","Lokasi Tujuan","Tanggal","Status","Keterangan","Nota Supplier","Waktu", "Tipe Biaya","Entry Stock Fisik"},
	{"No","Location","Destination","Date","Status","Remark","Supplier Invoice","Time","Type of Costing","Entry Physical Stock"}
};

/* this constant used to list text of listMaterialItem */
public static final String textListOrderItem[][] =
{
	{"No","Kode","Nama","Stok","Amount","Total Amount","Total","Action","Stock balance","Stock Fisik", "Delete"},//10
	{"No","Code","Name","Stok On Hand","Amount","Total Amount","Action","Sell Price","Stock balance","Physical Stock","Delete"}
};

public static final String textPosting[][] = {
    {"Anda yakin memfinalkan dokument ?","Anda yakin melakukan Posting Harga ?"},
    {"Are You Sure to Posting ? ","Are You Sure to Posting Cost Price?"}
};


public static final String textDelete[][] = {
    {"Apakah Anda Yakin Akan Menghapus Data ?"},
    {"Are You Sure to Delete This Data? "}
};

public double getPriceCost(Vector list, long oid){
    double cost = 0.00;
    if(list.size()>0){
        for(int k=0;k<list.size();k++){
            MatReceiveItem matReceiveItem = (MatReceiveItem)list.get(k);
            if(matReceiveItem.getMaterialId()==oid){
                cost = matReceiveItem.getCost();
                break;
            }
        }
    }
    return cost;
}
/**
* this method used to list all po item
*/
public Vector drawListOrderItem(int language,int iCommand,FrmPriceProtectionItem frmObject,
                             PriceProtectionItem objEntity,Vector objectClass,
                             long dfItemId,int start, int tranUsedPriceHpp, int enableStockFisik,boolean privManageData, String approot)
{
    ControlList ctrlist = new ControlList();
	ctrlist.setAreaWidth("100%");
	ctrlist.setListStyle("listgen");
	ctrlist.setTitleStyle("listgentitle");
	ctrlist.setCellStyle("listgensell");
	ctrlist.setHeaderStyle("listgentitle");
	ctrlist.addHeader(textListOrderItem[language][0],"3%");
	ctrlist.addHeader(textListOrderItem[language][1],"10%");
	ctrlist.addHeader(textListOrderItem[language][2],"30%");
	ctrlist.addHeader(textListOrderItem[language][3],"17%");
        ctrlist.addHeader(textListOrderItem[language][4],"15%");
        ctrlist.addHeader(textListOrderItem[language][5],"20%");
        ctrlist.addHeader(textListOrderItem[language][6],"5%");
         
        Vector list = new Vector(1,1);
	Vector listError = new Vector(1,1);

	Vector lstData = ctrlist.getData();
	Vector rowx = new Vector(1,1);
	ctrlist.reset();
	ctrlist.setLinkRow(1);
	int index = -1;
	if(start<0)
	   start=0;

        double total = 0;
        double totalQty = 0;
        for(int i=0; i<objectClass.size(); i++)
	{
             Vector temp = (Vector)objectClass.get(i);
             PriceProtectionItem priceProtectionItem = (PriceProtectionItem)temp.get(0);
             Material mat = (Material)temp.get(1);
             
             rowx = new Vector();
             start = start + 1;
                 
                if (dfItemId == priceProtectionItem.getOID()) index = i;
                if(index==i && (iCommand==Command.EDIT || iCommand==Command.ASK))
                {
                        rowx.add(""+start);
                        rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FLD_MATERIAL_ID]+"\" value=\""+mat.getOID()+""+
			"\"><input type=\"text\" size=\"13\" onKeyDown=\"javascript:keyDownCheck(event)\" name=\"matCode\" value=\""+mat.getSku()+"\" class=\"formElemen\">&nbsp;<a href=\"javascript:cmdCheck()\">CHK</a>");
                        rowx.add("<input type=\"text\" size=\"40\" name=\"matItem\" onKeyDown=\"javascript:keyDownCheck(event)\" value=\""+mat.getName()+"\" class=\"formElemen\" id=\"txt_materialname\">&nbsp;<a href=\"javascript:cmdCheck()\">CHK</a>");
                        rowx.add("<div align=\"center\"> <input type=\"text\" size=\"20\" name=\""+frmObject.fieldNames[frmObject.FRM_FLD_QTY_ON_HAND]+"\" value=\""+priceProtectionItem.getStockOnHand()+"\" class=\"formElemen\" align=\"center\"></div>");
                        rowx.add("<div align=\"center\"> <input type=\"text\" size=\"20\" name=\""+frmObject.fieldNames[frmObject.FRM_FLD_AMOUNT]+"\" value=\""+priceProtectionItem.getAmount()+"\" class=\"formElemen\" align=\"center\"></div>");
                        rowx.add("<div align=\"center\"> <input type=\"text\" size=\"20\" name=\""+frmObject.fieldNames[frmObject.FRM_FLD_TOTAL_AMOUNT]+"\" value=\""+priceProtectionItem.getTotalAmount()+"\" class=\"formElemen\" align=\"center\"></div>");
                }else{
                    rowx.add(""+start+"");
                    if(!privManageData){
                        rowx.add(""+mat.getSku());
                    }else{
                        rowx.add("<a href=\"javascript:cmdEdit('"+String.valueOf(priceProtectionItem.getOID())+"')\">"+mat.getSku()+"</a>");
                    }
                    
                    rowx.add(mat.getName());
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(priceProtectionItem.getStockOnHand())+"</div>");
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(priceProtectionItem.getAmount())+"</div>");
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(priceProtectionItem.getTotalAmount())+"</div>");
                }
                
                if(!privManageData){
                    rowx.add("");
                }else{
                    rowx.add("<div align=\"center\"> <a href=\"javascript:cmdNewDelete('"+String.valueOf(priceProtectionItem.getOID())+"')\"><img src="+approot+"/images/x3.png align=\"center\" ></a></div>");
                }
                
                lstData.add(rowx);
        }

	rowx = new Vector();
	if((iCommand==Command.SAVE && frmObject.errorSize()>0)){
		rowx.add(""+(start+1));
		rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FLD_MATERIAL_ID]+"\" value=\""+""+
			"\"><input type=\"text\" size=\"13\" onKeyDown=\"javascript:keyDownCheck(event)\" name=\"matCode\" value=\""+""+"\" class=\"formElemen\">&nbsp;<a href=\"javascript:cmdCheck()\">CHK</a>");
                
                rowx.add("<input type=\"text\" size=\"40\" name=\"matItem\" onKeyDown=\"javascript:keyDownCheck(event)\" value=\""+""+"\" class=\"formElemen\" id=\"txt_materialname\">&nbsp;<a href=\"javascript:cmdCheck()\">CHK</a>");
		
                rowx.add("<div align=\"center\"> <input type=\"text\" size=\"20\" name=\""+frmObject.fieldNames[frmObject.FRM_FLD_QTY_ON_HAND]+"\" value=\""+""+"\" class=\"formElemen\" align=\"center\"></div>");
                rowx.add("<div align=\"center\"> <input type=\"text\" size=\"20\" name=\""+frmObject.fieldNames[frmObject.FRM_FLD_AMOUNT]+"\" value=\""+""+"\" class=\"formElemen\" align=\"center\"></div>");
                rowx.add("<div align=\"center\"> <input type=\"text\" size=\"20\" name=\""+frmObject.fieldNames[frmObject.FRM_FLD_TOTAL_AMOUNT]+"\" value=\""+""+"\" class=\"formElemen\" align=\"center\"></div>");
                
                rowx.add("<div align=\"center\"></div>");
                
		lstData.add(rowx);
	}
        
    list.add(ctrlist.draw());
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
int docType = i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL,I_DocType.MAT_DOC_TYPE_DF);
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
long oidPriceProtection = FRMQueryString.requestLong(request, "hidden_priceprotection_id");

/**
* initialization of some identifier
*/
String errMsg = "";
int iErrCode = FRMMessage.ERR_NONE;

/**
* dispatch code and title
*/
String ppCode = "";//i_pstDocType.getDocCode(docType);
String ppTitle = "Price Protection";//i_pstDocType.getDocTitle(docType);
String ppItemTitle = ppTitle + " Item";

/**
* action process
*/
ControlLine ctrLine = new ControlLine();
CtrlPriceProtection ctrlPriceProtection = new CtrlPriceProtection(request);

//by dyas - 20131126
//tambah variabel userName dan userId
iErrCode = ctrlPriceProtection.action(iCommand , oidPriceProtection, userName, userId);
FrmPriceProtection frmpp = ctrlPriceProtection.getForm();
PriceProtection pp = ctrlPriceProtection.getPriceProtection();
errMsg = ctrlPriceProtection.getMessage();


CtrlPriceProtectionItem ctrlPriceProtectionItem = new CtrlPriceProtectionItem(request);
FrmPriceProtectionItem frmPriceProtectionItem = ctrlPriceProtectionItem.getForm();
PriceProtectionItem priceProtectionItem = ctrlPriceProtectionItem.getPriceProtectionItem();
//
/*
if(pp.getCostingStatus()==I_DocStatus.DOCUMENT_STATUS_FINAL){
    try{
        System.out.println(">>> proses posting doc : "+oidPriceProtection);
        SessPosting sessPosting = new SessPosting();
        boolean isOK =  sessPosting.postedCostingDoc(oidPriceProtection);
         if(isOK){
            pp.setCostingStatus(I_DocStatus.DOCUMENT_STATUS_CLOSED);
        }
        iCommand = Command.EDIT;
    }catch(Exception e){
        iCommand = Command.EDIT;
    }
}*/
/**
* if iCommand = Commmand.ADD ---> Set default rate which value taken from PstCurrencyRate
*/
//double curr = PstCurrencyRate.getLastCurrency();
String priceCode = "Rp.";

/**
* list purchase order item
*/
oidPriceProtection = pp.getOID();
int recordToGetItem = 500;
String whereClouse=PstPriceProtectionItem.fieldNames[PstPriceProtectionItem.FLD_POS_PRICE_PROTECTION_ID] + " = " + oidPriceProtection;
String whereClousex="PPPI."+PstPriceProtectionItem.fieldNames[PstPriceProtectionItem.FLD_POS_PRICE_PROTECTION_ID] + " = " + oidPriceProtection;
int vectSizeItem = PstPriceProtectionItem.getCount(whereClouse);
Vector listMatCostingItem = PstPriceProtectionItem.listInnerJoinMaterial(startItem,recordToGetItem,whereClousex,PstPriceProtectionItem.fieldNames[PstPriceProtectionItem.FLD_POS_PRICE_PROTECTION_ITEM_ID]);

// check if document may modified or not
boolean privManageData = true;
if(pp.getStatus()!= I_DocStatus.DOCUMENT_STATUS_DRAFT){
    privManageData=false;
}

//add opie-eyek 20131206
//Vector list = drawListOrderItem(SESS_LANGUAGE,listMatCostingItem,startItem, pp.getNumberPP(),tranUsedPriceHpp,0,privShowQtyPrice,privManageData, approot);
Vector list = drawListOrderItem(SESS_LANGUAGE,iCommand,frmPriceProtectionItem,priceProtectionItem,listMatCostingItem,0,0,tranUsedPriceHpp,0,privManageData,approot);
Vector listError = (Vector)list.get(1);

String whereClausex=PstPriceProtectionItem.fieldNames[PstPriceProtectionItem.FLD_POS_PRICE_PROTECTION_ID]+"="+oidPriceProtection;
double sumTotal= PstPriceProtectionItem.getSum(whereClausex);

if(iCommand==Command.DELETE && iErrCode==0)
{
%>
	<jsp:forward page="src_price_protection.jsp">
	<jsp:param name="command" value="<%=Command.FIRST%>"/>
	</jsp:forward>
<%
}
%>
<!-- End of Jsp Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
//------------------------- START JAVASCRIPT FUNCTION FOR PO MAIN -----------------------
function cmdEdit(oid){
	document.frm_matdispatch.command.value="<%=Command.EDIT%>";
	document.frm_matdispatch.prev_command.value="<%=prevCommand%>";
        document.frm_matdispatch.hidden_priceprotection_item_id.value=oid;
	document.frm_matdispatch.action="price_protection_item.jsp";
	document.frm_matdispatch.submit();
}

function compare(){
	var dt = document.frm_matdispatch.<%=FrmPriceProtection.fieldNames[FrmPriceProtection.FRM_FLD_CREATE_DATE]%>_dy.value;
	var mn = document.frm_matdispatch.<%=FrmPriceProtection.fieldNames[FrmPriceProtection.FRM_FLD_CREATE_DATE]%>_mn.value;
	var yy = document.frm_matdispatch.<%=FrmPriceProtection.fieldNames[FrmPriceProtection.FRM_FLD_CREATE_DATE]%>_yr.value;
	var dt = new Date(yy,mn-1,dt);
	var bool = new Boolean(compareDate(dt));
	return bool;
}

function gostock(oid){
    document.frm_matdispatch.command.value="<%=Command.EDIT%>";
    document.frm_matdispatch.hidden_priceprotection_item_id.value=oid;
    document.frm_matdispatch.action="costing_stockcode.jsp";
    document.frm_matdispatch.submit();
}


function PostingStock() {
    var conf = confirm("<%=textPosting[SESS_LANGUAGE][0]%>");
    if(conf){
        document.frm_matdispatch.command.value="<%=Command.POSTING%>";
        document.frm_matdispatch.prev_command.value="<%=prevCommand%>";
        document.frm_matdispatch.action="price_protection_edit.jsp";
        document.frm_matdispatch.submit();
        }
}


function PostingCostPrice() {
    var conf = confirm("<%=textPosting[SESS_LANGUAGE][1]%>");
    if(conf){
        document.frm_matdispatch.command.value="<%=Command.REPOSTING%>";
        document.frm_matdispatch.prev_command.value="<%=prevCommand%>";
        document.frm_matdispatch.action="price_protection_edit.jsp";
        document.frm_matdispatch.submit();
        }
}

function cmdSave()
{
    <%	if ((pp.getStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (pp.getStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED))
            {
    %>

            var statusDoc = document.frm_matdispatch.<%=FrmPriceProtection.fieldNames[FrmPriceProtection.FRM_FLD_STATUS]%>.value;
            if(statusDoc=="<%=I_DocStatus.DOCUMENT_STATUS_FINAL%>"){
                 var conf = confirm("<%=textPosting[SESS_LANGUAGE][0]%>");
                 if(conf){
                    document.frm_matdispatch.command.value="<%=Command.POSTING%>";
                    document.frm_matdispatch.prev_command.value="<%=prevCommand%>";
                    document.frm_matdispatch.action="price_protection_edit.jsp";
                    if(compare()==true)
                            document.frm_matdispatch.submit();
                 }
            }else{
                document.frm_matdispatch.command.value="<%=Command.SAVE%>";
                document.frm_matdispatch.prev_command.value="<%=prevCommand%>";
                document.frm_matdispatch.action="price_protection_edit.jsp";
                if(compare()==true)
                        document.frm_matdispatch.submit();
            }

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
<%	if ((pp.getStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (pp.getStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED))
    {
	%>
            document.frm_matdispatch.command.value="<%=Command.ASK%>";
            document.frm_matdispatch.prev_command.value="<%=prevCommand%>";
            document.frm_matdispatch.action="price_protection_edit.jsp";
			if(compare()==true)
				document.frm_matdispatch.submit();
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

function cmdCancel(){
	document.frm_matdispatch.command.value="<%=Command.CANCEL%>";
	document.frm_matdispatch.prev_command.value="<%=prevCommand%>";
	document.frm_matdispatch.action="price_protection_edit.jsp";
	document.frm_matdispatch.submit();
}


function cmdDelete(oid){
	document.frm_matdispatch.command.value="<%=Command.DELETE%>";
       
	document.frm_matdispatch.prev_command.value="<%=prevCommand%>";
	document.frm_matdispatch.approval_command.value="<%=Command.DELETE%>";
	document.frm_matdispatch.action="price_protection_edit.jsp";
	document.frm_matdispatch.submit();
}

function cmdConfirmDelete(oid){
	document.frm_matdispatch.command.value="<%=Command.DELETE%>";
        document.frm_matdispatch.hidden_priceprotection_item_id.value=oid;
	document.frm_matdispatch.prev_command.value="<%=prevCommand%>";
	document.frm_matdispatch.approval_command.value="<%=Command.DELETE%>";
	document.frm_matdispatch.action="price_protection_item.jsp";
	document.frm_matdispatch.submit();
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
	document.frm_matdispatch.command.value="<%=Command.FIRST%>";
	document.frm_matdispatch.prev_command.value="<%=prevCommand%>";
	document.frm_matdispatch.action="src_price_protection.jsp";
	document.frm_matdispatch.submit();
}

function printForm()
{
	window.open("print_price_protection.jsp?hidden_priceprotection_id=<%=oidPriceProtection%>&command=<%=Command.EDIT%>","pireport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
}

function findInvoice()
{
	window.open("pp_wh_material_receive.jsp","invoice_supplier","scrollbars=yes,height=500,width=700,status=no,toolbar=no,menubar=yes,location=no");
}

//------------------------- END JAVASCRIPT FUNCTION FOR PO MAIN -----------------------


//------------------------- START JAVASCRIPT FUNCTION FOR PO ITEM -----------------------
function addItem()
{
	<%	if (pp.getStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED)
		{
	%>
		document.frm_matdispatch.command.value="<%=Command.ADD%>";
		document.frm_matdispatch.action="price_protection_item.jsp";
		if(compareDateForAdd()==true)
			document.frm_matdispatch.submit();
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
	<%	if (pp.getStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED)
		{
	%>
		document.frm_matdispatch.command.value="<%=Command.EDIT%>";
		document.frm_matdispatch.hidden_priceprotection_item_id.value=oid;
		document.frm_matdispatch.action="price_protection_item.jsp";
		document.frm_matdispatch.submit();
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
	document.frm_matdispatch.command.value=comm;
	document.frm_matdispatch.prev_command.value=comm;
	document.frm_matdispatch.action="price_protection_item.jsp";
	document.frm_matdispatch.submit();
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

//by dyas - 20131126
//tambah function viewHistoryTable
//untuk mengambil oidPriceProtection dan digunakan di historyPo
function viewHistoryTable() {
    var strvalue ="../../../main/historypo.jsp?command=<%=Command.FIRST%>"+
                     "&oidDocHistory=<%=oidPriceProtection%>";
    window.open(strvalue,"material", "height=600,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
}
//------------------------- END JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------
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
          <!--<td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->
            <!--&nbsp;Warehouse &gt; Costing &gt; Store <!-- #EndEditable --><!--</td>-->
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->
            &nbsp;<%=textListGlobal[SESS_LANGUAGE][1]%> &gt; <%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][2]%> <!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" -->
            <form name="frm_matdispatch" method="post" action="">
<%
try
{
%>
              <input type="hidden" name="command" value="">
	      <input type="hidden" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="start" value="<%=startItem%>">
              <input type="hidden" name="command_item" value="<%=cmdItem%>">
              <input type="hidden" name="approval_command" value="<%=appCommand%>">
              <input type="hidden" name="hidden_priceprotection_id" value="<%=oidPriceProtection%>">
              <input type="hidden" name="hidden_priceprotection_item_id" value="">
              <input type="hidden" name="<%=FrmPriceProtection.fieldNames[FrmPriceProtection.FRM_FLD_NOMOR_PRICE_PROTECTION]%>" value="<%=pp.getNumberPP()%>">
              <input type="hidden" name="<%=FrmPriceProtection.fieldNames[FrmPriceProtection.FRM_FLD_COUNTER]%>" value="<%=pp.getPpCounter()%>">
              <input type="hidden" name="<%=FrmPriceProtection.fieldNames[FrmPriceProtection.FRM_FLD_TOTAL_AMOUNT]%>" value="<%=sumTotal%>"></input>
              <table width="100%" border="0">
                <tr>
                  <td colspan="3">
                    <table width="100%" border="0" cellpadding="1">
                      <tr>
                        <td align="left">&nbsp;</td>
                        <td align="left">&nbsp;</td>
                        <td align="right">                      
                        <td width="13%" align="right" valign="top">                      
                      </tr>
                      <tr>
                        <td width="12%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][0]%></td>
                        <td width="26%" align="left">: <b><%=pp.getNumberPP() %></b></td>
                        <td width="8%" align="right"></td>
                        <td width="13%" align="left" valign="top"></td>  
                        <td width="19%" align="right">&nbsp;</td>                                             
                        <td width="22%" align="left" valign="top">&nbsp;</td>
                      </tr>
                      <tr>
                        <td width="12%" align="left" valign="bottom"><%=textListOrderHeader[SESS_LANGUAGE][3]%></td>
                        <td width="26%" align="left" valign="bottom">: <%=ControlDate.drawDateWithStyle(FrmPriceProtection.fieldNames[FrmPriceProtection.FRM_FLD_CREATE_DATE], (pp.getDateCreated()==null) ? new Date() : pp.getDateCreated(), 1, -5, "formElemen", "")%></td>
                        <td width="8%" align="right"><%=textListOrderHeader[SESS_LANGUAGE][1]%></td>
                        <td width="13%" align="left">
                            <%
                                Vector obj_locationid = new Vector(1,1);
                                Vector val_locationid = new Vector(1,1);
                                Vector key_locationid = new Vector(1,1);
                                //Vector vt_loc = PstLocation.list(0,0,PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE, PstLocation.fieldNames[PstLocation.FLD_CODE]);
                                //Vector vt_loc = PstLocation.list(0,0,"", PstLocation.fieldNames[PstLocation.FLD_NAME]);
                                //add opie-eyek
                                //algoritma : di check di sistem usernya dimana saja user tsb bisa melakukan create document
                              String whereClause = " ("+PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE +
                                                   " OR "+PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE +")";

                              whereClause += " AND "+PstDataCustom.whereLocReportView(userId, "user_create_document_location");

                              Vector vt_loc = PstLocation.listLocationCreateDocument(0, 0, whereClause, "");
                                for(int d=0;d<vt_loc.size();d++) {
                                        Location loc = (Location)vt_loc.get(d);
                                        val_locationid.add(""+loc.getOID()+"");
                                        key_locationid.add(loc.getName());
                                }
                                String select_locationid = ""+pp.getLocationId(); //selected on combo box
                          %>
                        <%=ControlCombo.draw(FrmPriceProtection.fieldNames[FrmPriceProtection.FRM_FLD_LOCATION_ID], null, select_locationid, val_locationid, key_locationid, "", "formElemen")%> </td>
                        </td>
                        <td width="19%" align="right"><%=textListOrderHeader[SESS_LANGUAGE][5]%> : </td>                                             
                        <td width="22%" rowspan="2" align="left" valign="top"><textarea name="<%=FrmPriceProtection.fieldNames[FrmPriceProtection.FRM_FLD_REMARK]%>" class="formElemen" wrap="VIRTUAL" rows="3"><%=pp.getRemark()%></textarea></td>
                      </tr>
                      <tr>
                        <td width="12%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][7]%></td>
                        <td width="26%" align="left">: <%=ControlDate.drawTimeSec(FrmPriceProtection.fieldNames[FrmPriceProtection.FRM_FLD_CREATE_DATE], (pp.getDateCreated()==null) ? new Date(): pp.getDateCreated(),"formElemen")%></td>
                        <td width="8%" align="right"><%=textListOrderHeader[SESS_LANGUAGE][4]%> </td>
                        <td width="13%" align="left">
                                <%
                                Vector obj_status = new Vector(1,1);
                                Vector val_status = new Vector(1,1);
                                Vector key_status = new Vector(1,1);

                                // update opie-eyek 19022013
                                // user bisa memfinalkan costing tidak bisa mengubah status dari final ke draft jika  jika  :
                                // 1. punya priv. costing approve = true
                                // 2. lokasi sumber (lokasi asal)  ada di lokasi-lokasi yg diassign ke user
                                boolean locationAssign=false;
                                locationAssign  = PstDataCustom.checkDataCustom(userId, "user_location_map",pp.getLocationId());
                                
                                val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_DRAFT));
                                key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]);
                                //add by fitra
                                if(listMatCostingItem.size()>0){
                                            val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED));
                                            key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED]);
                                }
                                if(listMatCostingItem.size()>0 && privApproval==true && locationAssign==true && listError.size()==0){
                                    val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_FINAL));
                                    key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);
                                }
                                String select_status = ""+pp.getStatus();
                                if(pp.getStatus()==I_DocStatus.DOCUMENT_STATUS_CLOSED){
                                    out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_CLOSED]);
                                }else if(pp.getStatus()==I_DocStatus.DOCUMENT_STATUS_POSTED){
                                    out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_POSTED]);
                                }else if(pp.getStatus()==I_DocStatus.DOCUMENT_STATUS_FINAL && (privApproval==false || locationAssign==false)){
                                    out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);
                                }else{
                                %>
                                <%=ControlCombo.draw(FrmPriceProtection.fieldNames[FrmPriceProtection.FRM_FLD_STATUS],null,select_status,val_status,key_status,"","formElemen")%>
                                <%}%>
                        </td>
                        <td width="19%" align="right">&nbsp;</td>                                            
                        <td width="22%" >&nbsp;</td>
                      </tr>
                    </table>
                  </td>
                </tr>
                <tr>
                  <td colspan="3">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr align="left" valign="top">
                        <td height="22" valign="middle" colspan="3"><%=list.get(0)%></td>
                      </tr>
                       <tr>
                          <td width="80%">&nbsp;</td>
                          <td width="20%">&nbsp;</td>
                      </tr>
                      <tr>
                          <td width="80%"></td>
                          <td width="20%"><b>TOTAL : <b> <%=FRMHandler.userFormatStringDecimal(sumTotal)%></td>
                      </tr>
                      
                      <%if(oidPriceProtection!=0){%>
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
                          <%=ctrLine.drawImageListLimit(cmdItem,vectSizeItem,startItem,recordToGetItem)%> 
                          </span>
                        </td>
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
                              <% if(pp.getStatus()==I_DocStatus.DOCUMENT_STATUS_DRAFT) { %>
                              <td width="6%"><a href="javascript:addItem()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,ppCode+" Item",ctrLine.CMD_ADD,true)%>"></a></td>
                              <td width="94%"><a href="javascript:addItem()"><%=ctrLine.getCommand(SESS_LANGUAGE,ppCode+" Item",ctrLine.CMD_ADD,true)%></a></td>
                              <% } %>
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
                <tr>
                  <td colspan="3">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td width="70%">
                        <%
                            ctrLine.setLocationImg(approot+"/images");

                            // set image alternative caption
                            ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,ppTitle,ctrLine.CMD_SAVE,true));
                            ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,ppTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,ppTitle,ctrLine.CMD_BACK,true)+" List");
                            ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,ppTitle,ctrLine.CMD_ASK,true));
                            ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,ppTitle,ctrLine.CMD_CANCEL,false));

                            ctrLine.initDefault();
                            ctrLine.setTableWidth("100%");
                            String scomDel = "javascript:cmdAsk('"+oidPriceProtection+"')";
                            String sconDelCom = "javascript:cmdDelete('"+oidPriceProtection+"')";
                            String scancel = "javascript:cmdEdit('"+oidPriceProtection+"')";
                            ctrLine.setCommandStyle("command");
                            ctrLine.setColCommStyle("command");

                            // set command caption
                            ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE,ppTitle,ctrLine.CMD_SAVE,true));
                            ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,ppTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,ppTitle,ctrLine.CMD_BACK,true)+" List");
                            ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE,ppTitle,ctrLine.CMD_ASK,true));
                            ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE,ppTitle,ctrLine.CMD_DELETE,true));
                            ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE,ppTitle,ctrLine.CMD_CANCEL,false));

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
                            
                             if(pp.getStatus() != I_DocStatus.DOCUMENT_STATUS_DRAFT){
                                    ctrLine.setSaveCaption("");
                                    ctrLine.setDeleteCaption("");
                            }

                            if(privAdd==false || pp.getStatus() != I_DocStatus.DOCUMENT_STATUS_DRAFT){
                                    ctrLine.setAddCaption("");
                            }

                            if(iCommand==Command.SAVE && frmpp.errorSize()==0){
                                    //iCommand=Command.EDIT;
                            }

                        %>
                        <%=ctrLine.drawImage(iCommand,iErrCode,errMsg)%> </td>
                        <td width="30%">
                          <%if(listMatCostingItem!=null && listMatCostingItem.size()>0){%>
                            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                             <tr>
                                  <td width="5%" valign="top"><a href="javascript:printForm('<%=oidPriceProtection%>')"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0"></a></td>
                                  <td width="95%" nowrap>&nbsp; <a href="javascript:printForm('<%=oidPriceProtection%>')" class="command" >Print <%=ppTitle%></a></td>
                             </tr>
                          </table>
			<%}%>
                        </td>
                      </tr>
    <!--            by dyas - 20131126
                    tambah baris untuk memanggil fungsi viewHistoryTable()-->
                    <tr>
                      <td colspan="2" valign="top">&nbsp;</td>
                      <td width="30%">&nbsp;</td>
                    </tr>
                    <tr>
                      <td colspan="2" valign="top">&nbsp;</td>
                      <td width="30%">&nbsp;</td>
                    </tr>
                    <tr>
                        <td> <centre> <a href="javascript:viewHistoryTable()">VIEW TABEL HISTORY</a></centre></td>
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
       <%if(menuUsed == MENU_ICON){%>
            <%@include file="../../../styletemplate/footer.jsp" %>
        <%}else{%>
            <%@ include file = "../../../main/footer.jsp" %>
        <%}%>
    </td>
        <script language="JavaScript">
            <% 
            // add by fitra 10-5-2014
            if(pp.getStatus()==I_DocStatus.DOCUMENT_STATUS_DRAFT && iCommand==Command.SAVE){%>  
               addItem();
            <% } %>
      </script>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>


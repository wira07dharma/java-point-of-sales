<%-- 
    Document   : distribution_price_protection
    Created on : Jan 5, 2015, 11:40:54 AM
    Author     : dimata005
--%>

<%@page import="com.dimata.common.entity.contact.PstContactList"%>
<%@page import="com.dimata.common.entity.contact.PstContactClass"%>
<%@page import="com.dimata.common.entity.contact.ContactList"%>
<%@page import="com.dimata.posbo.form.warehouse.FrmPriceProtectionDistribution"%>
<%@page import="com.dimata.posbo.form.warehouse.CtrlPriceProtectionDistribution"%>
<%@page import="com.dimata.posbo.form.warehouse.FrmPriceProtectionItem"%>
<%@page import="com.dimata.posbo.form.warehouse.CtrlPriceProtectionItem"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@page import="com.dimata.posbo.form.warehouse.FrmPriceProtection"%>
<%@page import="com.dimata.posbo.form.warehouse.CtrlPriceProtection"%>
<%@ page import="com.dimata.posbo.form.warehouse.FrmMatCostingItem,
                 com.dimata.posbo.form.warehouse.CtrlMatCosting,
                 com.dimata.posbo.form.warehouse.FrmMatCosting,
                 com.dimata.posbo.form.warehouse.CtrlMatCostingItem,
                 com.dimata.common.entity.location.PstLocation,
                 com.dimata.common.entity.location.Location,
                 com.dimata.gui.jsp.ControlList,
                 com.dimata.util.Command,
                 com.dimata.qdep.form.FRMHandler,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.qdep.entity.I_PstDocType,
                 com.dimata.qdep.form.FRMMessage,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.gui.jsp.ControlDate,
                 com.dimata.gui.jsp.ControlCombo,
                 com.dimata.posbo.entity.warehouse.*,
                 com.dimata.posbo.entity.masterdata.Unit,
                 com.dimata.posbo.entity.masterdata.Material,
                 com.dimata.posbo.entity.masterdata.PstMaterial,
                 com.dimata.posbo.entity.masterdata.Costing,
                 com.dimata.posbo.entity.masterdata.PstCosting"%>
<%@ page language = "java" %>

<%@ include file = "../../../main/javainit.jsp" %>
<%
int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_COSTING, AppObjInfo.G2_COSTING, AppObjInfo.OBJ_COSTING);
int  appObjCodeShowQtyAndPrice = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE_QTY_AND_PRICE);
%>
<%@ include file = "../../../main/checkuser.jsp" %>
<%
boolean privShowQtyPrice = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeShowQtyAndPrice, AppObjInfo.COMMAND_VIEW));
boolean privApproval = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_APPROVE));
%>
<!-- Jsp Block -->
<%!
public static final String textListGlobal[][] = {
	{"Pembiayaan","Gudang","Ke Toko","Tidak ada data pembiayaan"},
	{"Costing","Warehouse","Store","No priceProtection data available"}
};

/* this constant used to list text of listHeader */
public static final String textListOrderHeader[][] =
{
	{"Nomor","Lokasi Asal","Lokasi Tujuan","Tanggal","Status","Keterangan","Nota Supplier","Tipe Biaya","Entry Stock Fisik"},
	{"No","Location","Destination","Date","Status","Remark","Supplier Invoice", "Type Of Costing","Entry Physical Stock"}
};

/* this constant used to list text of listMaterialItem */
public static final String textListOrderItem[][] =
{
	{"No","Kode","Nama Supplier","Rate","Amount","Status","Action"},//10
	{"No","Code","Vendor Name","Rate","Amount","Status","Action"}
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
* this method used to maintain dfList
*/
public Vector drawListDfItem(int language,int iCommand,FrmPriceProtectionDistribution frmObject,PriceProtectionDistribution objEntity,Vector objectClass,long dfItemId,long oidPriceProtection, String approot, double balanceDistribusi)
{
	ControlList ctrlist = new ControlList();
	ctrlist.setAreaWidth("100%");
	ctrlist.setListStyle("listgen");
	ctrlist.setTitleStyle("listgentitle");
	ctrlist.setCellStyle("listgensell");
	ctrlist.setHeaderStyle("listgentitle");
	ctrlist.addHeader(textListOrderItem[language][0],"3%");//0
	ctrlist.addHeader(textListOrderItem[language][2],"30%");//1
	ctrlist.addHeader(textListOrderItem[language][3],"17%");//2
        ctrlist.addHeader(textListOrderItem[language][4],"15%");//3
        ctrlist.addHeader(textListOrderItem[language][5],"20%");//4
        ctrlist.addHeader(textListOrderItem[language][6],"5%");//5
         
        Vector list = new Vector(1,1);
	Vector listError = new Vector(1,1);

	Vector lstData = ctrlist.getData();
	Vector rowx = new Vector(1,1);
	ctrlist.reset();
	ctrlist.setLinkRow(1);
	int index = -1;
        int start=0;
	if(start<0)
	   start=0;

        double total = 0;
        double totalQty = 0;
        
        String wh_supp = PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE]+
        " = "+PstContactClass.CONTACT_TYPE_SUPPLIER+
        " AND "+PstContactList.fieldNames[PstContactList.FLD_PROCESS_STATUS]+
        " != "+PstContactList.DELETE;
        Vector vt_supp = PstContactList.listContactByClassType(0,0,wh_supp,PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);
        Vector val_supplier = new Vector(1,1);
        Vector key_supplier = new Vector(1,1);
          if(vt_supp!=null && vt_supp.size()>0){
              for(int d=0; d<vt_supp.size(); d++){
                  ContactList cnt = (ContactList)vt_supp.get(d);
                  String cntName = cnt.getCompName();
                  if(cntName.length()==0){
                      cntName = cnt.getPersonName()+" "+cnt.getPersonLastname();
                  }

                  if (cntName.compareToIgnoreCase("'") >= 0) {
                      cntName = cntName.replace('\'','`');
                  }

                  val_supplier.add(String.valueOf(cnt.getOID()));
                  key_supplier.add(cntName);
              }
          }
        
        for(int i=0; i<objectClass.size(); i++)
	{
            Vector temp = (Vector)objectClass.get(i);
             PriceProtectionDistribution priceProtectionDistribution = (PriceProtectionDistribution)temp.get(0);
             ContactList contactList = (ContactList)temp.get(1);
             
             rowx = new Vector();
             start = start + 1;
                 
                if (dfItemId == priceProtectionDistribution.getOID()) index = i;
             
                if(index==i && (iCommand==Command.EDIT || iCommand==Command.ASK))
                {
                        rowx.add(""+start);
                        rowx.add(""+ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FLD_SUPPLIER_ID],null,""+contactList.getOID(),val_supplier,key_supplier,"","formElemen"));
                        rowx.add("<div align=\"center\"> <input type=\"text\" size=\"20\" name=\""+frmObject.fieldNames[frmObject.FRM_FLD_EXCHANGE_RATE]+"\" value=\""+priceProtectionDistribution.getExchangeRate()+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" align=\"center\"></div>");
                        rowx.add("<div align=\"center\"> <input type=\"text\" size=\"20\" name=\""+frmObject.fieldNames[frmObject.FRM_FLD_AMOUNT_ISSUE]+"\" value=\""+priceProtectionDistribution.getAmountIssue()+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" align=\"center\"></div>");
                        rowx.add("<div align=\"center\"></div>");
                }else{
                    rowx.add(""+start+"");
                    rowx.add(contactList.getCompName());
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(priceProtectionDistribution.getExchangeRate())+"</div>");
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(priceProtectionDistribution.getAmountIssue())+"</div>");
                    
                    if(priceProtectionDistribution.getStatus()==0){
                        rowx.add("<div align=\"center\">Draft</div>");
                    }else{
                        rowx.add("<div align=\"center\">Closed</div>");
                    }
                    
                }
                
                rowx.add("<div align=\"center\"> <a href=\"javascript:cmdNewDelete('"+String.valueOf(priceProtectionDistribution.getOID())+"')\"><img src="+approot+"/images/x3.png align=\"center\" ></a></div>");
                lstData.add(rowx);
        }

        rowx = new Vector();
        
        
	if(iCommand==Command.ADD || (iCommand==Command.SAVE && frmObject.errorSize()>0)){
		rowx.add(""+(start+1));
		rowx.add(""+ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FLD_SUPPLIER_ID],null,"",val_supplier,key_supplier,"","formElemen"));
                rowx.add("<div align=\"center\"> <input type=\"text\" size=\"20\" name=\""+frmObject.fieldNames[frmObject.FRM_FLD_EXCHANGE_RATE]+"\" value=\"1"+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" align=\"center\"></div>");
                rowx.add("<div align=\"center\"> <input type=\"text\" size=\"20\" name=\""+frmObject.fieldNames[frmObject.FRM_FLD_AMOUNT_ISSUE]+"\" value=\""+balanceDistribusi+""+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\"  align=\"center\"></div>");
                
                rowx.add("<div align=\"center\"></div>");
                rowx.add("<div align=\"center\"></div>");
		lstData.add(rowx);
	}
        
    list.add(ctrlist.draw());
    list.add(listError);
    return list;
}

%>


<%
/**
* get approval status for create document
*/
I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();

%>

<%
/**
* get request data from current form
*/
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request,"start");
int prevCommand = FRMQueryString.requestInt(request,"prev_command");
int appCommand = FRMQueryString.requestInt(request,"approval_command");
long oidPriceProtection = FRMQueryString.requestLong(request,"hidden_priceprotection_id");
long oidPriceProtectionItem = FRMQueryString.requestLong(request,"hidden_priceprotection_item_id");

/**
* initialization of some identifier
*/
int iErrCode = FRMMessage.NONE;
String msgString = "";

/**
* purchasing pr code and title
*/
String costingCode = ""; //i_pstDocType.getDocCode(docType);
String costingTitle = "Price Protection Barang"; //i_pstDocType.getDocTitle(docType);
String costingItemTitle = costingTitle + " Item";

/**
* purchasing pr code and title
*/
String prCode = i_pstDocType.getDocCode(i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL,I_DocType.MAT_DOC_TYPE_DF));


/**
* process on df main
*/
CtrlPriceProtection ctrlPriceProtection = new CtrlPriceProtection(request);
iErrCode = ctrlPriceProtection.action(Command.EDIT,oidPriceProtection,userName,userId);
FrmPriceProtection frmPriceProtection = ctrlPriceProtection.getForm();
PriceProtection pp = ctrlPriceProtection.getPriceProtection();

/**
* check if document already closed or not
*/
boolean documentClosed = false;
if(pp.getStatus()==I_DocStatus.DOCUMENT_STATUS_CLOSED)
{
	documentClosed = true;
}

/**
* check if document may modified or not
*/
boolean privManageData = true;

ControlLine ctrLine = new ControlLine();
CtrlPriceProtectionDistribution ctrlPriceProtectionDistribution = new CtrlPriceProtectionDistribution(request);
ctrlPriceProtectionDistribution.setLanguage(SESS_LANGUAGE);


//by dyas - 20131126
//tambah variabel userName dan userId
iErrCode = ctrlPriceProtectionDistribution.action(iCommand,oidPriceProtectionItem,oidPriceProtection, userName, userId);
FrmPriceProtectionDistribution frmPriceProtectionDistribution = ctrlPriceProtectionDistribution.getForm();
PriceProtectionDistribution priceProtectionDistribution = ctrlPriceProtectionDistribution.getPriceProtectionItem();
msgString = ctrlPriceProtectionDistribution.getMessage();

String whereClouse=PstPriceProtectionDistribution.fieldNames[PstPriceProtectionDistribution.FLD_POS_PRICE_PROTECTION_ID] + " = " + oidPriceProtection;
String whereClousex="PPPI."+PstPriceProtectionDistribution.fieldNames[PstPriceProtectionDistribution.FLD_POS_PRICE_PROTECTION_ID] + " = " + oidPriceProtection;
int vectSizeItem = PstPriceProtectionDistribution.getCount(whereClouse);

int recordToGetItem = 500;
if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST)
{
	start = ctrlPriceProtectionDistribution.actionList(iCommand,start,vectSizeItem,recordToGetItem);
}

Vector listPriceProtectionDistribution = PstPriceProtectionDistribution.listInnerJoinSupplier(start,recordToGetItem,whereClousex,PstPriceProtectionDistribution.fieldNames[PstPriceProtectionDistribution.FLD_POS_PRICE_PROTECTION_DISTRIBUTION_ID]);

if(listPriceProtectionDistribution.size()<1 && start>0)
{
     if(vectSizeItem-recordToGetItem > recordToGetItem){
            start = start - recordToGetItem;
     }
     else{
            start = 0 ;
            iCommand = Command.FIRST;
            prevCommand = Command.FIRST;
     }
     listPriceProtectionDistribution = PstPriceProtectionDistribution.list(start,recordToGetItem,"","");
}

if(iCommand==Command.SAVE && iErrCode == 0) {
    iCommand = Command.ADD;
    oidPriceProtectionItem=0;
}

String whereClausex=PstPriceProtectionItem.fieldNames[PstPriceProtectionItem.FLD_POS_PRICE_PROTECTION_ID]+"="+oidPriceProtection;
double sumTotal= PstPriceProtectionItem.getSum(whereClausex);

//String whereClausex=PstPriceProtectionItem.fieldNames[PstPriceProtectionItem.FLD_POS_PRICE_PROTECTION_ID]+"="+oidPriceProtection;
double sumDistribusi= PstPriceProtectionDistribution.getSum(whereClausex);

double balanceDistribusi=sumTotal-sumDistribusi;

%>

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
function main(oid,comm){
	document.frm_matdispatch.command.value=comm;
	document.frm_matdispatch.hidden_priceprotection_id.value=oid;
	document.frm_matdispatch.action="src_issue_price_protection.jsp";
	document.frm_matdispatch.submit();
}
function cmdAdd()
{
	document.frm_matdispatch.hidden_priceprotection_item_id.value="0";
	document.frm_matdispatch.command.value="<%=Command.ADD%>";
	document.frm_matdispatch.prev_command.value="<%=prevCommand%>";
	document.frm_matdispatch.action="distribution_price_protection.jsp";
	if(compareDateForAdd()==true)
		document.frm_matdispatch.submit();
}

function gostock(oid){
    document.frm_matdispatch.command.value="<%=Command.EDIT%>";
    document.frm_matdispatch.hidden_priceprotection_item_id.value=oid;
    document.frm_matdispatch.action="costing_stockcode.jsp";
    document.frm_matdispatch.submit();
}

function cmdEdit(oidPriceProtectionItem)
{
	document.frm_matdispatch.hidden_priceprotection_item_id.value=oidPriceProtectionItem;
	document.frm_matdispatch.command.value="<%=Command.EDIT%>";
	document.frm_matdispatch.prev_command.value="<%=prevCommand%>";
	document.frm_matdispatch.action="distribution_price_protection.jsp";
	document.frm_matdispatch.submit();
}

function cmdAsk(oidPriceProtectionItem)
{
	document.frm_matdispatch.hidden_priceprotection_item_id.value=oidPriceProtectionItem;
	document.frm_matdispatch.command.value="<%=Command.ASK%>";
	document.frm_matdispatch.prev_command.value="<%=prevCommand%>";
	document.frm_matdispatch.action="distribution_price_protection.jsp";
	document.frm_matdispatch.submit();
}

function cmdSave(){
	document.frm_matdispatch.command.value="<%=Command.SAVE%>";
	document.frm_matdispatch.prev_command.value="<%=prevCommand%>";
	document.frm_matdispatch.action="distribution_price_protection.jsp";
	document.frm_matdispatch.submit();
}

function cmdConfirmDelete(oidPriceProtectionItem)
{
	document.frm_matdispatch.hidden_priceprotection_item_id.value=oidPriceProtectionItem;
	document.frm_matdispatch.command.value="<%=Command.DELETE%>";
	document.frm_matdispatch.prev_command.value="<%=prevCommand%>";
	document.frm_matdispatch.approval_command.value="<%=Command.DELETE%>";
	document.frm_matdispatch.action="distribution_price_protection.jsp";
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

function cmdCancel(oidPriceProtectionItem)
{
	document.frm_matdispatch.hidden_priceprotection_item_id.value=oidPriceProtectionItem;
	document.frm_matdispatch.command.value="<%=Command.EDIT%>";
	document.frm_matdispatch.prev_command.value="<%=prevCommand%>";
	document.frm_matdispatch.action="distribution_price_protection.jsp";
	document.frm_matdispatch.submit();
}

function cmdBack()
{
	document.frm_matdispatch.command.value="<%=Command.LIST%>";
	document.frm_matdispatch.action="src_issue_price_protection.jsp";
	document.frm_matdispatch.submit();
}


function sumPrice()
{
}

function cmdCheck()
{
	var strvalue  = "priceprotectiondosearch.jsp?command=<%=Command.FIRST%>"+
                    "&location_id=<%=pp.getLocationId()%>"+
					"&hidden_priceprotection_id=<%=oidPriceProtection%>"+
					"&mat_code="+document.frm_matdispatch.matCode.value+
                                        "&txt_materialname="+document.frm_matdispatch.matItem.value+
                                        "&enable_stock_fisik=";
	window.open(strvalue,"material", "height=600,width=700,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes");
}

function calculate(evenClick){
  var qty = cleanNumberFloat(document.frm_matdispatch.<%=FrmMatCostingItem.fieldNames[FrmMatCostingItem.FRM_FIELD_QTY]%>.value,guiDigitGroup,guiDecimalSymbol);
   if(qty<0.0000){

          document.frm_matdispatch.<%=FrmMatCostingItem.fieldNames[FrmMatCostingItem.FRM_FIELD_QTY]%>.value=0;
          return;
        }
        
        
        
        if (evenClick.keyCode == 13) {
        cmdSave();
}
}


function cntTotal(element, evt){

   var qty = cleanNumberFloat(document.frm_matdispatch.<%=frmPriceProtectionDistribution.fieldNames[frmPriceProtectionDistribution.FRM_FLD_AMOUNT_ISSUE]%>.value,guiDigitGroup,guiDecimalSymbol);
   //alert("asas "+qty);
   if(qty>0.0000){
      //alert("perhitunga");
      var amount = cleanNumberFloat(document.frm_matdispatch.<%=FrmPriceProtection.fieldNames[FrmPriceProtection.FRM_FLD_TOTAL_AMOUNT]%>.value,guiDigitGroup,guiDecimalSymbol);
      var total = amount-qty;
      if(total<0){
          alert("tidak bisa melebih jumlah PP");
          document.frm_matdispatch.<%=frmPriceProtectionDistribution.fieldNames[frmPriceProtectionDistribution.FRM_FLD_AMOUNT_ISSUE]%>_1.value=0;
      }else{
          document.frm_matdispatch.<%=FrmPriceProtection.fieldNames[FrmPriceProtection.FRM_FLD_TOTAL_AMOUNT]%>_1.value=total;
           document.frm_matdispatch.<%=FrmPriceProtection.fieldNames[FrmPriceProtection.FRM_FLD_TOTAL_AMOUNT]%>.value=total;
      }
      
    }else{
        //alert("tidak bisa 0");
        document.frm_matdispatch.<%=frmPriceProtectionDistribution.fieldNames[frmPriceProtectionDistribution.FRM_FLD_AMOUNT_ISSUE]%>.value=0;
        
    }
    //alert("dor");
   // if (evt.keyCode == 13) {
   //     changeFocus(element);
    //}
    
}


function changeFocus(element){
    //alert(element.name);
    if(element.name == "<%=frmPriceProtectionDistribution.fieldNames[frmPriceProtectionDistribution.FRM_FLD_EXCHANGE_RATE]%>") {

        document.frm_matdispatch.<%=frmPriceProtectionDistribution.fieldNames[frmPriceProtectionDistribution.FRM_FLD_AMOUNT_ISSUE]%>.focus();

    }

    else if(element.name == "<%=frmPriceProtectionDistribution.fieldNames[frmPriceProtectionDistribution.FRM_FLD_AMOUNT_ISSUE]%>") {

        cmdSave();

    }

    else {

        cmdSave();

    }

}

function calculateStok(e){
  var qtyBalance = cleanNumberFloat(document.frm_matdispatch.<%=FrmMatCostingItem.fieldNames[FrmMatCostingItem.FRM_FIELD_BALANCE_QTY]%>.value,guiDigitGroup,guiDecimalSymbol);
  var qtyFisik = cleanNumberFloat(document.frm_matdispatch.<%=FrmMatCostingItem.fieldNames[FrmMatCostingItem.FRM_FIELD_RESIDUE_QTY]%>.value,guiDigitGroup,guiDecimalSymbol);
   if(qtyFisik<0.0000){

          document.frm_matdispatch.<%=FrmMatCostingItem.fieldNames[FrmMatCostingItem.FRM_FIELD_QTY]%>.value=0;
          return;
        }
   
        var qty = qtyBalance - qtyFisik;
        if(isNaN(qty)){
            qty = "0";
        }
        document.frm_matdispatch.<%=FrmMatCostingItem.fieldNames[FrmMatCostingItem.FRM_FIELD_QTY]%>.value=(parseFloat(qty));
        
        if (e.keyCode == 13){
            
            cmdSave();
        }
        //document.frm_matdispatch.total_qty.value = (parseFloat(qty));
}

function cmdListFirst()
{
	document.frm_matdispatch.command.value="<%=Command.FIRST%>";
	document.frm_matdispatch.prev_command.value="<%=Command.FIRST%>";
	document.frm_matdispatch.action="distribution_price_protection.jsp";
	document.frm_matdispatch.submit();
}

function cmdListPrev()
{
	document.frm_matdispatch.command.value="<%=Command.PREV%>";
	document.frm_matdispatch.prev_command.value="<%=Command.PREV%>";
	document.frm_matdispatch.action="distribution_price_protection.jsp";
	document.frm_matdispatch.submit();
}

function cmdListNext()
{
	document.frm_matdispatch.command.value="<%=Command.NEXT%>";
	document.frm_matdispatch.prev_command.value="<%=Command.NEXT%>";
	document.frm_matdispatch.action="distribution_price_protection.jsp";
	document.frm_matdispatch.submit();
}

function cmdListLast()
{
	document.frm_matdispatch.command.value="<%=Command.LAST%>";
	document.frm_matdispatch.prev_command.value="<%=Command.LAST%>";
	document.frm_matdispatch.action="distribution_price_protection.jsp";
	document.frm_matdispatch.submit();
}



function keyDownCheck(e){
  var trap = document.frm_matdispatch.trap.value;
   
    
   if (e.keyCode == 13 && trap==0) {
    document.frm_matdispatch.trap.value="1";
  
   }
   
   // add By fitra
    if (e.keyCode == 13 && trap == "0" && document.frm_matdispatch.matItem.value == "" ){
        document.frm_matdispatch.trap.value="0";
        cmdCheck();
   }
   if (e.keyCode == 13 && trap==1) {
       document.frm_matdispatch.trap.value="0";
       cmdCheck();
}
   if (e.keyCode == 27) {
       //alert("sa");
       document.frm_matdispatch.txt_materialname.value="";
   } 
}


function cmdBackList()
{
	document.frm_matdispatch.command.value="<%=Command.FIRST%>";
	document.frm_matdispatch.action="costing_material_list.jsp";
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


<%--autocomplate addd by fitra--%>
<script type="text/javascript" src="../../../styles/jquery-1.4.2.min.js"></script>
<script src="../../../styles/jquery.autocomplete.js"></script>
<link rel="stylesheet" type="text/css" href="../../../styles/style.css" />
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
           <!--&nbsp;Gudang &gt; Costing Barang &gt; Ke Toko <!-- #EndEditable --><!--</td>-->
           <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->
           &nbsp;<%=textListGlobal[SESS_LANGUAGE][1]%> &gt; <%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][2]%> <!-- #EndEditable -->
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" -->
            <form name="frm_matdispatch" method ="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="start" value="<%=start%>">
	      <input type="hidden" name="hidden_priceprotection_id" value="<%=oidPriceProtection%>">
              <input type="hidden" name="hidden_priceprotection_item_id" value="<%=oidPriceProtectionItem%>">
              <input type="hidden" name="<%=FrmPriceProtectionItem.fieldNames[FrmPriceProtectionItem.FRM_FLD_POS_PRICE_PROTECTION_ID]%>" value="<%=oidPriceProtection%>">
              <input type="hidden" name="approval_command" value="<%=appCommand%>">
              
              
              <input type="hidden" name="trap" value="">
              <table width="100%" cellpadding="1" cellspacing="0">
                <tr align="center">
                  <td colspan="3" class="title">
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
                        <td width="19%" align="right">&nbsp;</td>                                            
                        <td width="22%" >&nbsp;</td>
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
                        
                        val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED));
                        key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED]);
                        
                        val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_FINAL));
                        key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);
                            
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
                      </tr>
                    </table>
                  </td>
                </tr>
                <tr>
                  <td valign="top">
                    <table width="100%" cellpadding="1" cellspacing="1">
                      <tr>
                        <td colspan="3" >
                          <table width="100%" border="0" cellspacing="0" cellpadding="0" >
                            <tr align="left" valign="top">
                              <%
							  Vector listError = new Vector();
							  try
								{
								%>
                              <td height="22" valign="middle" colspan="3"> <%

                                                                Vector list = drawListDfItem(SESS_LANGUAGE,iCommand,frmPriceProtectionDistribution,priceProtectionDistribution,listPriceProtectionDistribution,oidPriceProtectionItem,oidPriceProtection,approot, balanceDistribusi);
								out.println(""+list.get(0));
								listError = (Vector)list.get(1);
							  %> </td>
                              <%
								}
								catch(Exception e)
								{
									System.out.println(e);
								}
								%>
                            </tr>
                            <tr>
                              <td width="80%">&nbsp;</td>
                              <td width="20%">&nbsp;</td>
                            </tr>
                            <%
                               
                            %>
                            <tr>
                              <td width="80%"></td>
                              <td width="20%"><b>TOTAL : <b> <input type="hidden" name="<%=FrmPriceProtection.fieldNames[FrmPriceProtection.FRM_FLD_TOTAL_AMOUNT]%>" value="<%=balanceDistribusi%>"> <input type="text" name="<%=FrmPriceProtection.fieldNames[FrmPriceProtection.FRM_FLD_TOTAL_AMOUNT]%>_1" value="<%=FRMHandler.userFormatStringDecimal(balanceDistribusi)%>"></td>
                            </tr>
                            <tr align="left" valign="top">
                              <td height="8" align="left" colspan="3" class="command">
                                <span class="command">
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
                                out.println(ctrLine.drawImageListLimit(cmd,vectSizeItem,start,recordToGetItem));
                                %>
                                </span> </td>
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
                                <%
                                    ctrLine.setLocationImg(approot+"/images");

                                    // set image alternative caption
                                    ctrLine.setAddNewImageAlt(ctrLine.getCommand(SESS_LANGUAGE,costingCode+" Item",ctrLine.CMD_ADD,true));
                                    ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,costingCode+" Item",ctrLine.CMD_SAVE,true));
                                    ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,costingCode+" Item",ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,costingCode+" Item",ctrLine.CMD_BACK,true)+" List");
                                    ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,costingCode+" Item",ctrLine.CMD_ASK,true));
                                    ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,costingCode+" Item",ctrLine.CMD_CANCEL,false));

                                    ctrLine.initDefault();
                                    ctrLine.setTableWidth("65%");
                                    String scomDel = "javascript:cmdAsk('"+oidPriceProtectionItem+"')";
                                    String sconDelCom = "javascript:cmdConfirmDelete('"+oidPriceProtectionItem+"')";
                                    String scancel = "javascript:cmdEdit('"+oidPriceProtectionItem+"')";
                                    ctrLine.setCommandStyle("command");
                                    ctrLine.setColCommStyle("command");

                                    // set command caption
                                    ctrLine.setAddCaption(ctrLine.getCommand(SESS_LANGUAGE,costingCode+" Item",ctrLine.CMD_ADD,true));
                                    ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE,costingCode+" Item",ctrLine.CMD_SAVE,true));
                                    ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,costingCode+" Item",ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,costingCode+" Item",ctrLine.CMD_BACK,true)+" List");
                                    ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE,costingCode+" Item",ctrLine.CMD_ASK,true));
                                    ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE,costingCode+" Item",ctrLine.CMD_DELETE,true));
                                    ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE,costingCode+" Item",ctrLine.CMD_CANCEL,false));


                                    if (privDelete){
                                            ctrLine.setConfirmDelCommand(sconDelCom);
                                            ctrLine.setDeleteCommand(scomDel);
                                            ctrLine.setEditCommand(scancel);
                                    }else{
                                            ctrLine.setConfirmDelCaption("");
                                            ctrLine.setDeleteCaption("");
                                            ctrLine.setEditCaption("");
                                    }

                                    if(privAdd == false  && privUpdate == false && pp.getStatus()!=I_DocStatus.DOCUMENT_STATUS_DRAFT){
                                            ctrLine.setSaveCaption("");
                                    }

                                    if (privAdd == false){
                                            ctrLine.setAddCaption("");
                                    }

                                    String  strDrawImage = ctrLine.drawImage(iCommand,iErrCode,msgString);
                                    if((iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST) && strDrawImage.length()==0){
                                    %>
                                <table width="50%" border="0" cellspacing="2" cellpadding="0">
                                  <tr>
                                    <% if(pp.getStatus()==I_DocStatus.DOCUMENT_STATUS_DRAFT) { %>
					<td width="6%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,costingCode+" Item",ctrLine.CMD_ADD,true)%>"></a></td>
                                        <td width="47%"><a href="javascript:cmdAdd()"><%=ctrLine.getCommand(SESS_LANGUAGE,costingCode+" Item",ctrLine.CMD_ADD,true)%></a></td>
				    <% } %>
                                    <td width="6%"><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200','','<%=approot%>/images/BtnBackOn.jpg',1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,costingCode+" Item",ctrLine.CMD_BACK,true)%>"></a></td>
                                    <td width="47%"><a href="javascript:cmdBack()"><%=ctrLine.getCommand(SESS_LANGUAGE,costingCode+" Item",ctrLine.CMD_BACK,true)%></a></td>
                                  </tr>
                                </table>
                                <%
                                }else{
                                        out.println(strDrawImage);
                                }
                                %>
                              </td>
                            </tr>
                          </table>
                        </td>
                      </tr>
                      <tr>
                        <td colspan="3">
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>
              </table>
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

      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<script language="JavaScript">
    <% if(iCommand == Command.ADD) { %>
        document.frm_matdispatch.matItem.focus();
       
    <% } %>
</script>

<script language="JavaScript">
         // add By Fitra
         var trap = document.frm_matdispatch.trap.value;       
         document.frm_matdispatch.trap.value="0";
         document.frmvendorsearch.txt_materialname.focus();
</script>
<%--autocomplate--%>
<script>
	jQuery(function(){
		$("#txt_materialname").autocomplete("list.jsp");
	});
</script>

<!-- #EndTemplate --></html>



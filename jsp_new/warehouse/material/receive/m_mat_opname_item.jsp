<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*,
                   com.dimata.posbo.entity.warehouse.PstSourceStockCode,
                   com.dimata.posbo.form.warehouse.FrmMatStockOpnameItem,
                   com.dimata.posbo.entity.warehouse.MatStockOpnameItem,
                   com.dimata.gui.jsp.ControlList,
                   com.dimata.posbo.entity.masterdata.*,
                   com.dimata.util.Command,
                   com.dimata.qdep.entity.I_PstDocType,
				   com.dimata.qdep.form.FRMHandler,
                   com.dimata.qdep.form.FRMMessage,
                   com.dimata.qdep.form.FRMQueryString,
                   com.dimata.posbo.form.warehouse.CtrlMatStockOpnameItem,
                   com.dimata.gui.jsp.ControlLine,
                   com.dimata.posbo.entity.warehouse.MatStockOpname,
                   com.dimata.posbo.form.warehouse.FrmMatStockOpname,
                   com.dimata.posbo.form.warehouse.CtrlMatStockOpname,
                   com.dimata.posbo.entity.warehouse.PstMatStockOpnameItem,
                   com.dimata.gui.jsp.ControlCombo,
                   com.dimata.gui.jsp.ControlLine,
                   com.dimata.gui.jsp.ControlDate,
                   com.dimata.common.entity.contact.ContactList,
                   com.dimata.common.entity.contact.PstContactList,
                   com.dimata.common.entity.contact.PstContactClass,
                   com.dimata.common.entity.location.Location,
                   com.dimata.common.entity.location.PstLocation,
                    com.dimata.posbo.entity.warehouse.*,
                   com.dimata.gui.jsp.ControlDate" %>
				   
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK, AppObjInfo.OBJ_STOCK_OPNAME);
//int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_COSTING, AppObjInfo.G2_COSTING, AppObjInfo.OBJ_COSTING);
int  appObjCodeShowQtyAndPrice = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE_QTY_AND_PRICE);

%>
<%@ include file = "../../../main/checkuser.jsp" %>

<%
boolean privShowQtyPrice = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeShowQtyAndPrice, AppObjInfo.COMMAND_VIEW));
%>



<!-- Jsp Block -->
<%!
public static final String textListGlobal[][] = {
	{"Stok","Opname","Pencarian","Daftar","Edit","Tidak ada data opname","Cetak Opname"},
	{"Stock","Opname","Search","List","Edit","No opname data available","Print Opname"}
};

/* this constant used to list text of listHeader */
public static final String textListOrderHeader[][] = {
	{"Nomor","Lokasi","Tanggal","Time","Status","Supplier","Kategori","Sub Kategori","Keterangan","Semua"},
	{"Number","Location","Date","Jam","Status","Supplier","Category","Sub Category","Remark","All"}
};

/* this constant used to list text of listMaterialItem */
public static final String textListOrderItem[][] =
{
	{"No","Sku","Nama Barang","Unit","Kategori","Sub Kategori","Qty Opname","Hapus"},//7
	{"No","Code","Name","Unit","Category","Sub Category","Qty Opname","Delete"}
};


public static final String textDelete[][] = {
    {"Apakah Anda Yakin Akan Menghapus Data ?"},
    {"Are You Sure to Delete This Data? "}
};
/**
* this method used to maintain soList
*/
public Vector drawListOpnameItem(int language,int iCommand,FrmMatStockOpnameItem frmObject,MatStockOpnameItem objEntity,Vector objectClass,long soItemId,int start, String approot) 
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
        ctrlist.addHeader(textListOrderItem[language][7],"10%");
	Vector lstData = ctrlist.getData();
	Vector rowx = new Vector(1,1);
    Vector list = new Vector(1,1);
	Vector listError = new Vector(1,1);

	ctrlist.reset();
	ctrlist.setLinkRow(1);
	int index = -1;
	if(start<0)
	{
	   start=0;
	}

        int soItemCounter= 0;
        long soItemSameMaterialId=0;

	for(int i=0; i<objectClass.size(); i++)
	{
		 Vector temp = (Vector)objectClass.get(i);
		 MatStockOpnameItem soItem = (MatStockOpnameItem)temp.get(0);
		 Material mat = (Material)temp.get(1);
		 Unit unit = (Unit)temp.get(2);
		 Category cat = (Category)temp.get(3);
		 SubCategory scat = (SubCategory)temp.get(4);
		 rowx = new Vector();
		 start = start + 1;

            String name = "";
          name = mat.getName();
          //if (name.compareToIgnoreCase("\"") >= 0 || name.compareToIgnoreCase("'") >= 0) {
              name = name.replace('\"','`');
              name = name.replace('\'','`');
          //}

                //counter
                //
                if(i == 0){
                   soItemCounter = soItem.getStockOpnameCounter();
                   soItemSameMaterialId = soItem.getMaterialId();
                }

		if (soItemId == soItem.getOID()) index = i;
		if(index==i && (iCommand==Command.EDIT || iCommand==Command.ASK))
		{
			rowx.add(""+start);
			rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID]+"\" value=\""+soItem.getMaterialId()+
				"\"><input type=\"text\" size=\"15\" onKeyDown=\"javascript:keyDownCheck(event)\" name=\"matCode\" value=\""+mat.getSku()+"\" class=\"formElemen\" readOnly>");
			rowx.add("<input type=\"text\" size=\"40\" name=\"matItem\" value=\""+name+"\" class=\"formElemen\" style=\"background-color:#E8E8E8\" readOnly>");
			rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"\" value=\""+soItem.getUnitId()+
				"\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\""+unit.getCode()+"\" class=\"formElemen\" style=\"background-color:#E8E8E8\" readOnly>");
			//rowx.add("<input type=\"text\" size=\"15\" name=\"matCat\" value=\""+cat.getName()+"\" class=\"formElemen\" style=\"background-color:#E8E8E8\" readOnly>");
			//rowx.add("<input type=\"text\" size=\"15\" name=\"matSCat\" value=\""+scat.getName()+"\" class=\"formElemen\" style=\"background-color:#E8E8E8\" readOnly>");
			rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY_OPNAME] +"\" value=\""+FRMHandler.userFormatStringDecimal(soItem.getQtyOpname())+"\" class=\"formElemen\" style=\"text-align:right\"></div>");
                        rowx.add("");
                }
		else
		{
			 rowx.add(""+start+"");
			 rowx.add("<a href=\"javascript:cmdEdit('"+String.valueOf(soItem.getOID())+"')\">"+mat.getSku()+"</a>");
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

            if(mat.getRequiredSerialNumber()==PstMaterial.REQUIRED){
               String where = PstSourceStockCode.fieldNames[PstSourceStockCode.FLD_SOURCE_ID]+"="+soItem.getOID();
               int cnt = PstSourceStockCode.getCount(where);
               if(cnt<soItem.getQtyOpname()){
                   if(listError.size()==0){
                       listError.add("Pesan Kesalahan: ");
                   }
                   listError.add(""+listError.size()+". Jumlah Serial kode barang '"+mat.getName()+"' tidak sama dengan jumlah qty opname");
               }
               rowx.add("<div align=\"right\"><a href=\"javascript:stockcode('"+String.valueOf(soItem.getOID())+"')\">[ST.CD]</a> "+FRMHandler.userFormatStringDecimal(soItem.getQtyOpname())+"</div>");
            }else{
               rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(soItem.getQtyOpname())+"</div>");
            }
                         
          // add by fitra 17-05-2014            
            rowx.add("<div align=\"center\"> <a href=\"javascript:cmdNewDelete('"+String.valueOf(soItem.getOID())+"')\"><img src="+approot+"/images/x3.png align=\"center\" ></a></div>");
		}
		lstData.add(rowx);
	}

	rowx = new Vector();
	if(iCommand==Command.ADD || (iCommand==Command.SAVE && frmObject.errorSize() > 0 && soItemId == 0))
	{
		rowx.add(""+(start+1));
		rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID]+"\" value=\""+""+
			"\"><input type=\"text\" size=\"13\" name=\"matCode\" value=\""+""+"\" class=\"formElemen\" onKeyDown=\"javascript:cekEnter(event)\"><a href=\"javascript:cmdCheck()\">CHK</a>");
		//rowx.add("<input type=\"text\" size=\"40\" name=\"matItem\" value=\""+""+"\" class=\"formElemen\" style=\"background-color:#E8E8E8\" readOnly>");
                rowx.add("<input type=\"text\" size=\"40\" name=\"matItem\" value=\""+""+"\" class=\"formElemen\" onKeyDown=\"javascript:cekEnter(event)\" id=\"txt_materialname\"><a href=\"javascript:cmdCheck()\">CHK</a>");
		rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"\" value=\""+""+
			"\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\""+""+"\" class=\"formElemen\" style=\"background-color:#E8E8E8\" readOnly>");
		//rowx.add("<input type=\"text\" size=\"15\" name=\"matCat\" value=\""+""+"\" class=\"formElemen\" style=\"background-color:#E8E8E8\" readOnly>");
		//rowx.add("<input type=\"text\" size=\"15\" name=\"matSCat\" value=\""+""+"\" class=\"formElemen\" style=\"background-color:#E8E8E8\" readOnly>");
		rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY_OPNAME] +"\" value=\""+""+"\" class=\"formElemen\" onKeyUp=\"javascript:calculate(event)\" style=\"text-align:right\"></div>");
		rowx.add("");
                lstData.add(rowx);
	}

    list.add(ctrlist.drawBootstrap());
	list.add(listError);
	return list;
}


%>


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
long oidStockOpnameItem = FRMQueryString.requestLong(request,"hidden_opname_item_id");
long oidLocation = FRMQueryString.requestLong(request, "hidden_location_id");
int notOpname = FRMQueryString.requestInt(request,"notOpname");

//get nama item, kode, supplier, kategori
String materialcode = FRMQueryString.requestString(request,"mat_code");
long materialgroup = FRMQueryString.requestLong(request,"txt_materialgroup");
long materialsubcategory = FRMQueryString.requestLong(request,"txt_material_sub_category");
long materialsupplier = FRMQueryString.requestLong(request,"txt_material_supplier");
String materialname = FRMQueryString.requestString(request,"txt_materialname");

Material objMaterial = new Material();
objMaterial.setCategoryId(materialgroup);
objMaterial.setSubCategoryId(materialsubcategory);
objMaterial.setSku(materialcode);
objMaterial.setName(materialname);


/**
* initialization of some identifier
*/
int iErrCode = FRMMessage.NONE;
String msgString = "";

/**
* purchasing pr code and title
*/
String soCode = i_pstDocType.getDocCode(docType);
String opnTitle = "Opname Barang"; //i_pstDocType.getDocTitle(docType);
String soItemTitle = opnTitle + " Item";

/**
* purchasing pr code and title
*/
String prCode = i_pstDocType.getDocCode(i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL,I_DocType.MAT_DOC_TYPE_DF));


//set list Opname nol
//mirahu
//20111018
if (oidStockOpname !=0 && iCommand==Command.ADD && (notOpname == 1 || notOpname == 2)){

  try {
    String orderBy = "MAT."+PstMaterial.fieldNames[PstMaterial.FLD_SKU];
    Vector listItemNotOpname = new Vector();
   // Vector listItemNotOpname =  PstMaterial.getListMaterialOpnameAll(0, 0, orderBy, oidLocation);
    if(notOpname == 1){
        //listItemNotOpname =  PstMaterial.getListMaterialOpnameAll(0, 0, orderBy, oidLocation);
          listItemNotOpname = PstMaterial.getListMaterialOpnameAll(materialsupplier, objMaterial, 0, 0, orderBy, oidLocation, oidStockOpname);
    } else if (notOpname == 2){
        //listItemNotOpname = PstMaterial.getListMaterialOpnameAllNew(0, 0, orderBy, oidLocation);
        listItemNotOpname = PstMaterial.getListMaterialOpnameAllNew(materialsupplier, objMaterial, 0, 0, orderBy, oidLocation, oidStockOpname);
    }
        

    if (listItemNotOpname !=null && listItemNotOpname.size()>0){

       for(int i=0; i<=listItemNotOpname.size(); i++){

        Material mat = (Material) listItemNotOpname.get(i);
        if(mat !=null){
            MatStockOpnameItem opnItem = new MatStockOpnameItem();
            opnItem.setStockOpnameId(oidStockOpname);
            opnItem.setMaterialId(mat.getOID());
            opnItem.setUnitId(mat.getDefaultStockUnitId());
            opnItem.setQtyOpname(0);
            opnItem.setQtySold(0);
            opnItem.setQtySystem(0);
            opnItem.setCost(mat.getAveragePrice());
            opnItem.setPrice(0);
            int counter = PstMatStockOpnameItem.getIntCounter(oidStockOpname);
            opnItem.setStockOpnameCounter(counter);

           try{
              PstMatStockOpnameItem.insertExc(opnItem);

            }catch (Exception e){}
          }
        }
    }

 }

  catch(Exception e){

  System.out.println(e);

 }%>

<%
}


/**
* process on so main
*/
CtrlMatStockOpname ctrlMatStockOpname = new CtrlMatStockOpname(request);
iErrCode = ctrlMatStockOpname.action(Command.EDIT,oidStockOpname);
FrmMatStockOpname frmMatStockOpname = ctrlMatStockOpname.getForm();
MatStockOpname so = ctrlMatStockOpname.getMatStockOpname();

/**
* check if document already closed or not
*/
boolean documentClosed = false;
if(so.getStockOpnameStatus()==I_DocStatus.DOCUMENT_STATUS_CLOSED)
{
	documentClosed = true;
}

/**
* check if document may modified or not
*/
boolean privManageData = false;

ControlLine ctrLine = new ControlLine();
CtrlMatStockOpnameItem ctrlMatStockOpnameItem = new CtrlMatStockOpnameItem(request);
ctrlMatStockOpnameItem.setLanguage(SESS_LANGUAGE);
iErrCode = ctrlMatStockOpnameItem.action(iCommand,oidStockOpnameItem,oidStockOpname,userName,userId);

FrmMatStockOpnameItem frmMatStockOpnameItem = ctrlMatStockOpnameItem.getForm();
MatStockOpnameItem soItem = ctrlMatStockOpnameItem.getMatStockOpnameItem();
msgString = ctrlMatStockOpnameItem.getMessage();

String whereClauseItem = PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID]+"="+oidStockOpname;
String orderClauseItem = "";
int vectSizeItem = PstMatStockOpnameItem.getCount(whereClauseItem);
int recordToGetItem = 20;

if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST)
{
	startItem = ctrlMatStockOpnameItem.actionList(iCommand,startItem,vectSizeItem,recordToGetItem);
}

Vector listMatStockOpnameItem = PstMatStockOpnameItem.list(startItem,recordToGetItem,oidStockOpname);
if(listMatStockOpnameItem.size()<1 && startItem>0)
{
	 if(vectSizeItem-recordToGetItem > recordToGetItem)
	 {
		startItem = startItem - recordToGetItem;
	 }
	 else
	 {
		startItem = 0 ;
		iCommand = Command.FIRST;
		prevCommand = Command.FIRST;
	 }
	 listMatStockOpnameItem = PstMatStockOpnameItem.list(startItem,recordToGetItem,oidStockOpname);
}
//Switch command to make auto add
if ((iCommand == Command.SAVE) && (frmMatStockOpnameItem.errorSize() == 0))
{
	oidStockOpnameItem = 0;
    iCommand = Command.ADD;
}



%>



<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title
><script language="JavaScript">
//------------------------- START JAVASCRIPT FUNCTION FOR PO MAIN -----------------------
function main(oid,comm){
	document.frm_matopname.command.value=comm;
	document.frm_matopname.hidden_opname_id.value=oid;
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

function cmdAdd()
{
	document.frm_matopname.hidden_opname_item_id.value="0";
	document.frm_matopname.command.value="<%=Command.ADD%>";
	document.frm_matopname.prev_command.value="<%=prevCommand%>";
	document.frm_matopname.action="m_mat_opname_item.jsp";
	if(compareDateForAdd()==true)
		document.frm_matopname.submit();
}

function cmdEdit(oidStockOpnameItem)
{
	document.frm_matopname.hidden_opname_item_id.value=oidStockOpnameItem;
	document.frm_matopname.command.value="<%=Command.EDIT%>";
	document.frm_matopname.prev_command.value="<%=prevCommand%>";
	document.frm_matopname.action="m_mat_opname_item.jsp";
	document.frm_matopname.submit();
}

function cmdAsk(oidStockOpnameItem)
{
	document.frm_matopname.hidden_opname_item_id.value=oidStockOpnameItem;
	document.frm_matopname.command.value="<%=Command.ASK%>";
	document.frm_matopname.prev_command.value="<%=prevCommand%>";
	document.frm_matopname.action="m_mat_opname_item.jsp";
	document.frm_matopname.submit();
}

function cmdSave()
{
	document.frm_matopname.command.value="<%=Command.SAVE%>";
	document.frm_matopname.prev_command.value="<%=prevCommand%>";
	document.frm_matopname.action="m_mat_opname_item.jsp";
	document.frm_matopname.submit();
}

function cmdConfirmDelete(oidStockOpnameItem)
{
	document.frm_matopname.hidden_opname_item_id.value=oidStockOpnameItem;
	document.frm_matopname.command.value="<%=Command.DELETE%>";
	document.frm_matopname.prev_command.value="<%=prevCommand%>";
	document.frm_matopname.approval_command.value="<%=Command.DELETE%>";
	document.frm_matopname.action="m_mat_opname_item.jsp";
	document.frm_matopname.submit();
        //cmdAdd();
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


function cmdCancel(oidStockOpnameItem)
{
	document.frm_matopname.hidden_opname_item_id.value=oidStockOpnameItem;
	document.frm_matopname.command.value="<%=Command.EDIT%>";
	document.frm_matopname.prev_command.value="<%=prevCommand%>";
	document.frm_matopname.action="m_mat_opname_item.jsp";
	document.frm_matopname.submit();
}


function cmdBack()
{
	document.frm_matopname.command.value="<%=Command.EDIT%>";
	document.frm_matopname.action="m_mat_opname_edit.jsp";
	document.frm_matopname.submit();
}



function sumPrice()
{
}




function cmdCheck()
{
 
	var strvalue  = "materialdosearch.jsp?command=<%=Command.FIRST%>"+
					"&mat_code="+document.frm_matopname.matCode.value+
                                        "&txt_materialname="+document.frm_matopname.matItem.value+
					"&txt_materialgroup=<%=so.getCategoryId()%>"+ // document.frm_matopname.<%=FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_CATEGORY_ID]%>.value+
                                        "&location_id=<%=oidLocation%>"+
                                        "&stock_opname_id=<%=oidStockOpname%>"+
					"&txt_material_sub_category=<%=so.getSubCategoryId()%>"+ // document.frm_matopname.<%=FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_SUB_CATEGORY_ID]%>.value+
					"&txt_material_supplier="+document.frm_matopname.<%=FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_SUPPLIER_ID]%>.value;
	window.open(strvalue,"material", "height=500,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
}


function keyDownCheck(e){
    
   var trap = document.frm_matopname.trap.value;
       
    
   if (e.keyCode == 13 && trap==0) {
    document.frm_matopname.trap.value="1";
  
   }
   
   // add By fitra
    if (e.keyCode == 13 && trap == "0" && document.frm_matopname.matItem.value == "" ){
        document.frm_matopname.trap.value="0";
        cmdCheck();
   }
   if (e.keyCode == 13 && trap==1) {
       document.frm_matopname.trap.value="0";
       cmdCheck();
}
   if (e.keyCode == 27) {
       //alert("sa");
       document.frm_matopname.txt_materialname.value="";
   } 
}


function cekEnter(e)
{       
        
	var trap = document.frm_matopname.trap.value;
   
 
    if (e.keyCode == 13 && trap==0) {
     document.frm_matopname.trap.value="1";

    }
     
   // add By fitra
    if (e.keyCode == 13 && trap == "0" && document.frm_matopname.matItem.value == "" ){
        document.frm_matopname.trap.value="0";
		cmdCheck();
	}
         
   if (e.keyCode == 13 && trap==1) {
       document.frm_matopname.trap.value="0";
       cmdCheck();
    }
  
   if (e.keyCode == 27) {
       //alert("sa");
       document.frm_matopname.txt_materialname.value="";
   } 
      
}

function cmdListFirst()
{
	document.frm_matopname.command.value="<%=Command.FIRST%>";
	document.frm_matopname.prev_command.value="<%=Command.FIRST%>";
	document.frm_matopname.action="m_mat_opname_item.jsp";
	document.frm_matopname.submit();
}

function calculate(e){
    
        
   if (e.keyCode == 13) {
       cmdSave();
   }
}

function cmdListPrev()
{
	document.frm_matopname.command.value="<%=Command.PREV%>";
	document.frm_matopname.prev_command.value="<%=Command.PREV%>";
	document.frm_matopname.action="m_mat_opname_item.jsp";
	document.frm_matopname.submit();
}

function cmdListNext()
{
	document.frm_matopname.command.value="<%=Command.NEXT%>";
	document.frm_matopname.prev_command.value="<%=Command.NEXT%>";
	document.frm_matopname.action="m_mat_opname_item.jsp";
	document.frm_matopname.submit();
}

function cmdListLast()
{
	document.frm_matopname.command.value="<%=Command.LAST%>";
	document.frm_matopname.prev_command.value="<%=Command.LAST%>";
	document.frm_matopname.action="m_mat_opname_item.jsp";
	document.frm_matopname.submit();
}

function cmdBackList()
{
	document.frm_matopname.command.value="<%=Command.FIRST%>";
	document.frm_matopname.action="mat_opname_list.jsp";
	document.frm_matopname.submit();
}

function refreshItemOpname(){

       document.frm_matopname.command.value="<%=Command.REFRESH%>";
       document.frm_matopname.prev_command.value="<%=prevCommand%>";
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
<%--autocomplate addd by fitra--%>
<script type="text/javascript" src="../../../styles/jquery-1.4.2.min.js"></script>
<script src="../../../styles/jquery.autocomplete.js"></script> 


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
                    <form name="frm_matopname" method ="post" action="">
                    <input type="hidden" name="command" value="<%=iCommand%>">
                    <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                    <input type="hidden" name="start_item" value="<%=startItem%>">
                    <input type="hidden" name="type_doc" value="1">
                    <input type="hidden" name="hidden_opname_id" value="<%=oidStockOpname%>">
                    <input type="hidden" name="hidden_opname_item_id" value="<%=oidStockOpnameItem%>">
                    <input type="hidden" name="hidden_location_id" value="<%=oidLocation%>">
                    <input type="hidden" name="<%=FrmMatStockOpnameItem.fieldNames[FrmMatStockOpnameItem.FRM_FIELD_STOCK_OPNAME_ID]%>" value="<%=oidStockOpname%>">
                    <input type="hidden" name="<%=FrmMatStockOpnameItem.fieldNames[FrmMatStockOpnameItem.FRM_FIELD_QTY_SOLD]%>" value="<%=soItem.getQtySold()%>">
                    <input type="hidden" name="<%=FrmMatStockOpnameItem.fieldNames[FrmMatStockOpnameItem.FRM_FIELD_QTY_SYSTEM]%>" value="<%=soItem.getQtySystem()%>">
                    <input type="hidden" name="<%=FrmMatStockOpnameItem.fieldNames[FrmMatStockOpnameItem.FRM_FIELD_COST]%>" value="<%=soItem.getCost()%>">
                    <input type="hidden" name="<%=FrmMatStockOpnameItem.fieldNames[FrmMatStockOpnameItem.FRM_FIELD_PRICE]%>" value="<%=soItem.getPrice()%>">
                    <input type="hidden" name="approval_command" value="<%=appCommand%>">
                    <input type="hidden" name="notOpname" value="<%=notOpname%>">
                    <input type="hidden" name="trap" value="">
                        <!--form hidden -->
                        
                        <!--body-->
                        <div class="box-body">
                            <div class="box-body">
                                <div class="row">
                                        <div class="col-md-4">
                                           <div class="form-group">
                                                <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][0]%></label> <br/> 
                                                  <% if((so.getStockOpnameNumber()).length() > 0 && iErrCode==0)
						  {
							out.println(so.getStockOpnameNumber());
						  }
						  else
						  {
						  	out.println("");
						  }
						  %>
                                                
                                           </div>
                                                
                                           <div class="form-group">
                                                <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][1]%></label> <br/> 
                                                    <%
							Vector obj_locationid = new Vector(1,1);
							Vector val_locationid = new Vector(1,1);
							Vector key_locationid = new Vector(1,1);
							String whereClause = "";//PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE;
							Vector vt_loc = PstLocation.list(0,0,whereClause, PstLocation.fieldNames[PstLocation.FLD_CODE]);
							for(int d=0;d<vt_loc.size();d++)
							{
								Location loc = (Location)vt_loc.get(d);
								val_locationid.add(""+loc.getOID()+"");
								key_locationid.add(loc.getName());
							}
							String select_locationid = ""+so.getLocationId(); //selected on combo box
						  %>
                                                  <%=ControlCombo.drawBootsratap(FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_LOCATION_ID], null, select_locationid, val_locationid, key_locationid, "", "form-control")%> 
                                                
                                           </div>
                                                    
                                           <div class="form-group">
                                                <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][3]%></label> <br/> 
                                                <%=ControlDate.drawTimeSec(FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_STOCK_OPNAME_DATE], (so.getStockOpnameDate()==null) ? new Date(): so.getStockOpnameDate(),"form-control-date")%>
                                                
                                           </div>
                                                    
                                                
                                                
                                                
                                            

                                        </div>
                                   <!-- end of col-md-4 -->             
                                        <div class="col-md-5">
                                             <div class="form-group">
                                                 <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][5]%> </label>
                                                 <%
							Vector obj_supplier = new Vector(1,1);
							Vector val_supplier = new Vector(1,1);
							Vector key_supplier = new Vector(1,1);
                                                            String wh_supp = PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE]+
                                                                            " = "+PstContactClass.CONTACT_TYPE_SUPPLIER+
                                                                            " AND "+PstContactList.fieldNames[PstContactList.FLD_PROCESS_STATUS]+
                                                                            " != "+PstContactList.DELETE;
                                                            String ordBySupp =  " CONT." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME];
                                                            Vector vt_supp = PstContactList.listContactByClassType(0,0,wh_supp,ordBySupp);

                                                            //Vector vt_supp = PstContactList.list(0,0,"",PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE]);
                                                            /*String whClauseSupp = " CLS." + PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE] + "=" + PstContactClass.FLD_CLASS_VENDOR;
                                                            String ordBySupp =  " CONT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE];
                                                            Vector vt_supp = PstContactList.listContactByClassType(0,0,whClauseSupp,ordBySupp);*/

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
                                                          <%=ControlCombo.drawBootsratap(FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_SUPPLIER_ID],null,select_supplier,val_supplier,key_supplier,"","form-control")%>
                                                 
                                                 
                                             </div>
                                                          
                                             <div class="form-group">
                                                 <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][6]%> </label>
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
                                                 
                                                 
                                             </div>
                                             <div class="form-group">
                                                 <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][4]%> </label>
                                                 <%
                                                    Vector obj_status = new Vector(1,1);
                                                    Vector val_status = new Vector(1,1);
                                                    Vector key_status = new Vector(1,1);

                                                    val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_DRAFT));
                                                    key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]);

                                                                            //add by fitra
                                                                            val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED));
                                                              key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED]);
                                                    if(vectSizeItem!=0){
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
                                            

                                        </div>
                                   <!-- end of col-md-5 -->
                                   
                                        <div class="col-md-3">
                                            <div class="form-group">
                                                 <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][3]%> </label><br /> 
                                                 <input type="text"  class="formElemen" name="<%=FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_STOCK_OPNAME_TIME]%>" value="<%=so.getStockOpnameTime()%>"  size="10" style="text-align:right">
                                            
                                                 
                                            </div>
                                                 
                                           
                                             <div class="form-group">
                                                 <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][8]%> </label>
                                                 <textarea name="textarea" class="formElemen" wrap="VIRTUAL" rows="4"><%=so.getRemark()%></textarea>
                                                 
                                             </div>
                                                 
                                         </div>
                                    <!-- end of col-md-3 -->
                                </div>
                                                 
                                <div class="row">
                                    
                                    <div class="col-md-12">
                                        <%
                                        Vector listError  = new Vector();
                                        try
                                        {
                                        %>
                                        <%
                                        Vector list = drawListOpnameItem(SESS_LANGUAGE,iCommand,frmMatStockOpnameItem, soItem,listMatStockOpnameItem,oidStockOpnameItem,startItem, approot);
                                        out.println(""+list.get(0));
                                        listError = (Vector)list.get(1);
                                        %> 
                                        <%
                                        }
                                        catch(Exception e)
                                        {
                                                System.out.println(e);
                                        }
                                        %>
                                        
                                        
                                    </div>

                                </div>
                                                                
                                                                
                                <div class="row">
                                    <div class="col-md-12">
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
                                                out.println(ctrLine.drawImageListLimit(cmd,vectSizeItem,startItem,recordToGetItem));
					   %>
                                         </span>
                                        
                                    </div>
                                    
                                </div>
                                         
                                         
                                <div class="row">
                                    <div class="col-md-12">
                                        <span class="command">
                                           <%
                                                for(int k=0;k<listError.size();k++)
                                                {
                                                        if(k==0)
                                                                out.println(listError.get(k)+"<br>");
                                                        else
                                                                out.println("&nbsp;&nbsp;&nbsp;"+listError.get(k)+"<br>");
                                                }
					   %> 
                                        </span>
                                    </div>
                                </div>
                                        
                               <%String  strDrawImage = ctrLine.drawImage(iCommand,iErrCode,msgString);
                                if((iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST) && strDrawImage.length()==0){
                                %>         
                               <div class="row">
                                   <div class="col-md-12">
                                       <button class="btn btn-success pull-left" onclick="javascript:add()" type="button" > Tambah Item </button>  
                                   </div>
                               </div>
                                <%
                                }else{
                                        out.println(strDrawImage);
                                }
                                %>        
                                         
                                                 
                                                 
                                                 
                                
			   </div>
                        </div>
                    </form>
                </section><!-- /.content -->
                
            </aside><!-- /.right-side -->
        </div><!-- ./wrapper -->

        <!-- add new calendar event modal -->


        <!-- jQuery 2.0.2 -->
        <!-- add new calendar event modal -->
        
        
        <!-- jQuery 2.0.2 -->
        
        <!-- jQuery UI 1.10.3 -->
       
        <!-- Bootstrap -->
       
        <!-- Morris.js charts -->
        
        
        <!-- Sparkline -->
        
        <!-- jvectormap -->
        
       
        <!-- fullCalendar -->
        
        <!-- jQuery Knob Chart -->
        
        <!-- daterangepicker --> 
       
        <!-- Bootstrap WYSIHTML5 -->
        
        <!-- iCheck -->
        

        <!-- AdminLTE App -->
       
         <script src="../../../styles/bootstrap3.1/js/AdminLTE/app.js" type="text/javascript"></script>
        
        <!-- AdminLTE dashboard demo (This is only for demo purposes) -->
         
<SCRIPT language=JavaScript>
var asu = "<%=iCommand%>";
var asu2 = "<%=Command.ADD%>";
if (asu == asu2)
{
	document.frm_matopname.matItem.focus();
}
</SCRIPT>

<script language="JavaScript">
                var addNotOpname = "<%=iCommand%>";
                var notOpn = "<%=notOpname%>";
        if(addNotOpname == "<%=Command.ADD%>" && notOpn == "1" || notOpn == "2"){
        // CREDITS:
        // Automatic Page Refresher by Peter Gehrig and Urs Dudli www.24fun.com
        // Permission given to use the script provided that this notice remains as is.
        // Additional scripts can be found at http://www.hypergurl.com
        // Configure refresh interval (in seconds)
        var refreshinterval=5
        // Shall the coundown be displayed inside your status bar? Say "yes"  or "no" below: var displaycountdown="yes"
        // Do not edit the code below

        var starttime;
        var nowtime;
        var reloadseconds=0;
        var secondssinceloaded=0;
        function starttime() {
            starttime=new Date() ;
            starttime=starttime.getTime() ;
            countdown() ;
        }

        function countdown() {
        nowtime= new Date() ;
        nowtime=nowtime.getTime() ;
        secondssinceloaded=(nowtime-starttime)/1000 ;
        reloadseconds=Math.round(refreshinterval-secondssinceloaded) ;
        if (refreshinterval>=secondssinceloaded) {
            var timer=setTimeout("countdown()",1000) ;
        if (displaycountdown=="yes") {
            window.status="Page refreshing in "+reloadseconds+ " seconds" ;
         }
    } else {
      clearTimeout(timer);
      refreshItemOpname();//window.location.reload(true) ;
     }
    }
    window.onload=starttime ;
}

</script>

    </body>
    
    
    
<script language="JavaScript">
            // add By Fitra
        var trap = document.frm_matopname.trap.value;       
        document.frm_matopname.trap.value="0";
        document.frmvendorsearch.txt_materialname.focus();
</script>
<%--autocomplate--%>
<script>
	jQuery(function(){
		$("#txt_materialname").autocomplete("list.jsp");
	});
</script>    
</html>


               
          

         


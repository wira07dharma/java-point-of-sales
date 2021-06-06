<%@page import="com.dimata.hanoman.entity.masterdata.MasterGroup"%>
<%@page import="com.dimata.hanoman.entity.masterdata.PstMasterGroup"%>
<%@page import="com.dimata.hanoman.entity.masterdata.PstMasterType"%>
<%@page import="com.dimata.hanoman.entity.masterdata.MasterType"%>
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
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK, AppObjInfo.OBJ_STOCK_OPNAME); 
   int  appObjCodeCorrection = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK, AppObjInfo.OBJ_STOCK_CORRECTION); 
%>
<%@ include file = "../../../main/checkuser.jsp" %>
<%
boolean privApprovalFinal = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_FINAL));
boolean privViewCorrection= userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeCorrection, AppObjInfo.COMMAND_ADD));
%>
<%!
public static final String textListGlobal[][] = {
	{"Stok","Opname","Pencarian","Daftar","Edit","Tidak ada data opname","Cetak Opname","Material dengan barcode yang sama atau mirip lebih dari 1","Verifikasi","Verifikasi berhasil","Tutup"},
	{"Stock","Opname","Search","List","Edit","No opname data available","Print Opname","Material with same barcode more than 1","Verification", "Verifivation success","Close"}
};

/* this constant used to list text of listHeader */
public static final String textListOrderHeader[][] = {
	{"Nomor","Lokasi","Tanggal","Waktu","Status","Supplier","Kategori","Sub Kategori","Keterangan","Semua"},
	{"Number","Location","Date","Jam","Status","Supplier","Category","Sub Category","Remark","All"}
};

/* this constant used to list text of listMaterialItem */
public static final String textListOrderItem[][] = {
	{"No","Sku","Nama Barang","Unit","Kategori","Sub Kategori","Qty Opname","Hapus","Barcode","Warna","Ukuran"},
	{"No","Code","Name","Unit","Category","Sub Category","Qty Opname","Delete", "Barcode","Color","Size"}
};


public static final String textDelete[][] = {
    {"Apakah Anda Yakin Akan Menghapus Data ?"},
    {"Are You Sure to Delete This Data? "}
};


/**
* this method used to list all stock opname item
*/
public Vector drawListOpnameItem(int language,Vector objectClass,int start,boolean privManageData,String approot, int typeOfBusinessDetail) {
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
        
        String useForGreenbowl = PstSystemProperty.getValueByName("USE_FOR_GREENBOWL");
        
        ctrlist.addHeader(textListOrderItem[language][0],"1%");
        ctrlist.addHeader(textListOrderItem[language][1],"10%");
        if (useForGreenbowl.equals("1")) {
            ctrlist.addHeader(textListOrderItem[language][8], "10%");
        }
        ctrlist.addHeader(textListOrderItem[language][2],"20%");
        ctrlist.addHeader(textListOrderItem[language][3],"5%");
        if (useForGreenbowl.equals("1")) {
            ctrlist.addHeader(textListOrderItem[language][9], "15%");
            ctrlist.addHeader(textListOrderItem[language][10], "5%");
        }
        //added by dewok 2018
        if (typeOfBusinessDetail == 2) {
            ctrlist.addHeader("Qty", "5%");
            ctrlist.addHeader("Qty SO", "5%");
            ctrlist.addHeader("Qty Selisih", "5%");
            ctrlist.addHeader("Kadar", "5%");
            ctrlist.addHeader("Kadar SO", "5%");
            ctrlist.addHeader("Berat", "5%");
            ctrlist.addHeader("Berat SO", "5%");
            ctrlist.addHeader("Berat Selisih", "5%");
            ctrlist.addHeader("Remark", "10%");
        } else {
            ctrlist.addHeader(textListOrderItem[language][6],"5%");
        }        
        ctrlist.addHeader(textListOrderItem[language][7],"1%");
                
        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        Vector rowx = new Vector(1,1);
        ctrlist.reset();
        int index = -1;
        if(start<0) {
                start=0;
        }

        int soItemCounter= 0;
        long soItemSameMaterialId=0;
        
        //added for litama
        double totalQty = 0;
        double totalQtyOpname = 0;
        double totalQtySelisih = 0;
        double totalBerat = 0;
        double totalBeratOpname = 0;
        double totalBeratSelisih = 0;

        for(int i=0; i<objectClass.size(); i++) {
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
            //added by dewok 2018
            Material newmat = new Material();
            Category category = new Category();
            Color color = new Color();
            Kadar kadar = new Kadar();
            Kadar kadarOpname = new Kadar();
            MasterType masterTypeSize = new MasterType();
            try {
                newmat = PstMaterial.fetchExc(soItem.getMaterialId());
                category = PstCategory.fetchExc(newmat.getCategoryId());
                if (PstColor.checkOID(newmat.getPosColor())) {
                    color = PstColor.fetchExc(newmat.getPosColor());
                }
                if (PstKadar.checkOID(soItem.getKadarId())) {
                    kadar = PstKadar.fetchExc(soItem.getKadarId());
                }
                if (PstKadar.checkOID(soItem.getKadarOpnameId())) {
                    kadarOpname = PstKadar.fetchExc(soItem.getKadarOpnameId());
                }
                long oidMappingSize = PstMaterialMappingType.getSelectedTypeId(1, newmat.getOID());
                if (PstMasterType.checkOID(oidMappingSize)) {
                    masterTypeSize = PstMasterType.fetchExc(oidMappingSize);
                }
            } catch (Exception e) {
                
            }
            String itemName = "" + newmat.getName();
            if(typeOfBusinessDetail == 2){
                if (newmat.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS) {
                    itemName = "" + category.getName() + " " + color.getColorName() + " " + newmat.getName();
                } else if (newmat.getMaterialJenisType() == Material.MATERIAL_TYPE_BERLIAN) {
                    itemName = "" + category.getName() + " " + color.getColorName() + " Berlian " + newmat.getName();
                }
            }
            
            totalQty += soItem.getQtyItem();
            totalQtyOpname += soItem.getQtyOpname();
            totalQtySelisih += soItem.getQtySelisih();
            totalBerat += soItem.getBerat();
            totalBeratOpname += soItem.getBeratOpname();
            totalBeratSelisih += soItem.getBeratSelisih();
            
            rowx = new Vector();
            start = start + 1;
            
            rowx.add(""+start+"");
            //rowx.add("<a href=\"javascript:editItem('"+String.valueOf(soItem.getOID())+"')\">"+mat.getSku()+"</a>");
            if(privManageData){
                rowx.add("<a href=\"javascript:editItem('"+String.valueOf(soItem.getOID())+"')\">"+mat.getSku()+"</a>");
            }else{
                rowx.add(""+mat.getSku());
            }
            if (useForGreenbowl.equals("1")) {
                rowx.add(newmat.getBarCode());
            }
            //rowx.add(mat.getName());
            //update opie-eyek 31-12-2012
            if((soItemCounter!=0 && soItemCounter == soItem.getStockOpnameCounter() && i!=0) || (i!=0 && soItemSameMaterialId == soItem.getMaterialId())){
                //rowx.add("<font color=\"##347C17\" align=\"left\">"+mat.getName()+"</font>");
                rowx.add("<font color=\"#FF0080\" align=\"left\">"+itemName+"</font>");
                soItemCounter = soItem.getStockOpnameCounter();
            } else {
                rowx.add(itemName);
                soItemCounter = soItem.getStockOpnameCounter();
                soItemSameMaterialId = soItem.getMaterialId();
            }
            rowx.add(unit.getCode());
            if (useForGreenbowl.equals("1")) {
                rowx.add(color.getColorName());
                rowx.add(masterTypeSize.getMasterName());
            }
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
                if (typeOfBusinessDetail == 2) {
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(soItem.getQtyItem())+"</div>");
                }
               rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(soItem.getQtyOpname())+"</div>");
               if (typeOfBusinessDetail == 2) {
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(soItem.getQtySelisih())+"</div>");
                }
            }
            //added by dewok 2018
            if(typeOfBusinessDetail == 2){
                rowx.add("<div align=\"right\">"+String.format("%,.2f",kadar.getKadar())+"</div>");
                rowx.add("<div align=\"right\">"+String.format("%,.2f",kadarOpname.getKadar())+"</div>");
                rowx.add("<div align=\"right\">"+String.format("%,.3f", soItem.getBerat())+"</div>");
                rowx.add("<div align=\"right\">"+String.format("%,.3f", soItem.getBeratOpname())+"</div>");
                rowx.add("<div align=\"right\">"+String.format("%,.3f", soItem.getBeratSelisih())+"</div>");
                rowx.add(""+soItem.getRemark());
            }
            // add by fitra 17-05-2014
            if(privManageData){
                rowx.add("<div align=\"center\"> <a href=\"javascript:cmdNewDelete('"+String.valueOf(soItem.getOID())+"')\"><img src="+approot+"/images/x3.png align=\"center\" ></a></div>");
            }else{
                rowx.add("");
            }
            lstData.add(rowx);            
        }        
        
        //added by dewok 2018 for jewelry
        if (typeOfBusinessDetail == 2) {
            Vector rowAdd = new Vector(1, 1);
            rowAdd.add("");
            rowAdd.add("");
            rowAdd.add("");
            rowAdd.add("<div align=\"right\"><b>Total :</b></div>");
            rowAdd.add("<div align=\"right\"><b>" + String.format("%,.0f", totalQty) + "</b></div>");
            rowAdd.add("<div align=\"right\"><b>" + String.format("%,.0f", totalQtyOpname) + "</b></div>");
            rowAdd.add("<div align=\"right\"><b>" + String.format("%,.0f", totalQtySelisih) + "</b></div>");
            rowAdd.add("");
            rowAdd.add("");
            rowAdd.add("<div align=\"right\"><b>" + String.format("%,.3f", totalBerat) + "</b></div>");
            rowAdd.add("<div align=\"right\"><b>" + String.format("%,.3f", totalBeratOpname) + "</b></div>");
            rowAdd.add("<div align=\"right\"><b>" + String.format("%,.3f", totalBeratSelisih) + "</b></div>");
            rowAdd.add("");
            rowAdd.add("");
            lstData.add(rowAdd);
        }
            
        result = ctrlist.draw();
        
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
int typeharian = FRMQueryString.requestInt(request,"typeharian");

//added by dewok 2018 for emas lantakan
int lantakan = FRMQueryString.requestInt(request, "emas_lantakan");

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
iErrCode = ctrlMatStockOpname.action(iCommand , oidStockOpname, userName, userId,typeharian);
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
Vector list = drawListOpnameItem(SESS_LANGUAGE,listMatStockOpnameItem,startItem,privManageData,approot,typeOfBusinessDetail); 
//out.println(""+list.get(0));
Vector listError = (Vector)list.get(1);

//Verification type (using finger or not)
String verificationType = PstSystemProperty.getValueByName("KASIR_LOGIN_TYPE");

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
	document.frm_matopname.action="mat_opname_edit_new.jsp";
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
		document.frm_matopname.action="mat_opname_edit_new.jsp";
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
	document.frm_matopname.action="mat_opname_edit_new.jsp";
	document.frm_matopname.submit();
}

function cmdCancel(){
	document.frm_matopname.command.value="<%=Command.CANCEL%>";
	document.frm_matopname.prev_command.value="<%=prevCommand%>";
	document.frm_matopname.action="mat_opname_edit_new.jsp";
	document.frm_matopname.submit();
}
// addd by Fitra
function cmdDelete(oid){
	document.frm_matopname.command.value="<%=Command.DELETE%>";

	document.frm_matopname.prev_command.value="<%=prevCommand%>";
	document.frm_matopname.approval_command.value="<%=Command.DELETE%>";
	document.frm_matopname.action="mat_opname_edit_new.jsp";
	document.frm_matopname.submit();
}

function cmdConfirmDelete(oid){
	document.frm_matopname.command.value="<%=Command.DELETE%>";
        document.frm_matopname.hidden_opname_item_id.value=oid;
	document.frm_matopname.prev_command.value="<%=prevCommand%>";
	document.frm_matopname.approval_command.value="<%=Command.DELETE%>";
	document.frm_matopname.action="mat_opname_item.jsp";
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
	document.frm_matopname.action="mat_opname_list.jsp";
	document.frm_matopname.submit();
}

function cmdSelectSubCategory()
{
	window.open("subcategorydosearch.jsp?command=<%=Command.FIRST%>&txt_subcategory="+document.frm_matopname.txt_subcategory.value+
			"&oidCategory="+document.frm_matopname.<%=FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_CATEGORY_ID]%>.value,"material", "height=600,width=600,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes");
}

function printForm()
{   
    if ("<%=typeOfBusinessDetail%>" == 2) {
        window.open("print_out_opname.jsp?hidden_opname_id=<%=oidStockOpname%>","pireport","scrollbars=yes,height=600,width=1200,left=100,status=no,toolbar=no,menubar=yes,location=no");
    } else {
        var valueOrder = document.frm_matopname.groupByElement.value;
        //alert(valueOrder);
        window.open("mat_opname_print_form.jsp?hidden_opname_id=<%=oidStockOpname%>&command=<%=Command.EDIT%>&groupByElement="+valueOrder+"","pireport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
    }
}

function printFormExcel()
{        var valueOrder = document.frm_matopname.groupByElement.value;
        //alert(valueOrder);
	window.open("mat_opname_print_form_excel.jsp?hidden_opname_id=<%=oidStockOpname%>&command=<%=Command.EDIT%>&groupByElement="+valueOrder+"","pireport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
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

function addAllItem()
{
	<%	if (so.getStockOpnameStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED)
		{
	%>
		document.frm_matopname.command.value="<%=Command.ADDALL%>";
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

function banding(oid){
	document.frm_matopname.command.value="<%=Command.EDIT%>";
	document.frm_matopname.hidden_opname_id.value = oid;
	document.frm_matopname.approval_command.value="<%=Command.APPROVE%>";
	document.frm_matopname.action="mat_stock_correction_edit.jsp";
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
<link type="text/css" rel="stylesheet" href="../../../styles/bootstrap3.1/css/bootstrap.css">
<script type="text/javascript" src="../../../styles/jquery.min.js"></script>
<script type="text/javascript" src="../../../styles/bootstrap3.1/js/bootstrap.js"></script>
<script src="../../../styles/dimata-app.js" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function(){
                
        var interval = 0;
        var final = 2;
        var finalText = "Final";
        var curentStatus = $('#FRM_FIELD_STOCK_OPNAME_STATUS').val();
        var textStatus = $("#FRM_FIELD_STOCK_OPNAME_STATUS option:selected").text();
        var verificationType = <%=verificationType%>;
        //alert(verificationType);
        function ajaxUser2(url,data,type,appendTo,another,optional,optional2){
            $.ajax({
                url : ""+url+"",
                data: ""+data+"",
                type : ""+type+"",
                async : false,
                cache: false,
                success : function(data) {
                    //alert(data);
                    $(''+appendTo+'').html(data);
                },
                error : function(data){
                    alert('error');
                }
            }).done(function(data){

                if (another=="checkStatusUser2"){
                    
                   if (data==1){
                       clearInterval(interval);
                       alert("<%= textListGlobal[SESS_LANGUAGE][9]%>");
                       //alert(data);
                       //set nilai combobox
                       $("#FRM_FIELD_STOCK_OPNAME_STATUS").val(final);
                       $('#modalVerifikasi').modal('hide');
                       
                   }
                }else if (another=="checkUser2"){
                    clickFinger2();
                }
            });
        }
        
        function checkUser2(){
            var url = "<%=approot%>/AjaxUser";
            var loginId = $('#searchVerification').val();
            var data="command=<%=Command.ASSIGN%>&login="+loginId+"&language=<%= SESS_LANGUAGE%>&base=<%= baseURL%>&func='1'";
           
            ajaxUser2(url,data,"POST","#dynamicPlace","checkUser2","","");
        }
        
        function checkStatusUser2(userId){
            var url = "<%=approot%>/AjaxUser";
            var data="command=<%=Command.SEARCH%>&loginId="+userId+"";
            
            ajaxUser2(url,data,"POST","","checkStatusUser2","","");
        }
        
        function clickFinger2(){
            $('.loginFinger2').click(function(){
                var loginId= $('#searchVerification').val();
                interval= setInterval(function() {
                    checkStatusUser2(loginId);
                },5000);
            });
        }
        
        function changeCombo(){
            //alert('jalan');
            $('#FRM_FIELD_STOCK_OPNAME_STATUS').change(function(){
                var status = $('#FRM_FIELD_STOCK_OPNAME_STATUS').val();
                if (status==final){
                    //alert('test');
                    $("#FRM_FIELD_STOCK_OPNAME_STATUS").val(curentStatus);
                    $('#searchVerification').val('');
                    checkUser2();
                    $('#modalVerifikasi').modal('show');
                }else{
                    curentStatus = status;
                    textStatus= $("#FRM_FIELD_STOCK_OPNAME_STATUS option:selected").text();
                }
            });
        }
        
        if (verificationType==1){
            changeCombo();
        }

        $('#searchVerification').keyup(function(){
            checkUser2();
        });
        
        //added by dewok 2018
        var getDataFunction = function(onDone, onSuccess, approot, command, dataSend, servletName, dataAppendTo, notification, dataType){		
            $(this).getData({
               onDone	: function(data){
                   onDone(data);
               },
               onSuccess	: function(data){
                    onSuccess(data);
               },
               approot          : approot,
               dataSend         : dataSend,
               servletName	: servletName,
               dataAppendTo	: dataAppendTo,
               notification     : notification,
               ajaxDataType	: dataType
            });
        };
        
        $('.loc').change(function(){
            var idLocation = $(this).val();
            var dataFor = "getEtalaseByLocation";
            var command = "<%=Command.NONE%>";
            var approot = "<%=approot%>";
            var dataSend = {
                "FRM_FIELD_DATA_FOR": dataFor,
                "FRM_FIELD_LOCATION_ID": idLocation,
                "command": command
            };
            onDone = function (data) {
                
            };
            onSuccess = function (data) {

            };
            getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxPenerimaan", ".eta", false, "json");            
        });
        
    });
</script>
<style>
    .finger{
        width:20%; 
        height:auto;
        padding : 2%;
        float:left;
     }
    .finger_spot{
        width:100%;
        height: 80px;
        background-color :#e5e5e5;
        border : thin solid #c5c5c5;
        font-size: 14px;
        font-family:calibri;
        text-align:center;
        color :#FFF;
        border-radius: 3px;
    }

    .green{
       background-color : #5CB85C;
       border : thin solid #4CAE4C;
    }
</style>
<!--MODAL BOOTSTRAP -->
<div id="modalVerifikasi" class="modal fade" tabindex="-1">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" data-dismiss="modal" class="close"  aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="modal-title"><%=textListGlobal[SESS_LANGUAGE][8]%></h4>
            </div>
            <div class="modal-body" id="modal-body">
                <div class="row">
                    <div class="col-md-12">
                        <input type="text" class="form-control" id="searchVerification" placeholder="Input user..."/>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <div id="dynamicPlace"></div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" data-dismiss="modal" class="btn btn-danger"><%=textListGlobal[SESS_LANGUAGE][10]%></button>
            </div>
        </div>
    </div>
</div>
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
		  	<%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][1]%> &gt; <%=textListGlobal[SESS_LANGUAGE][4]%>
                        <%
                            if(typeOfBusinessDetail == 2) {
                                if (lantakan == 1) {
                                    out.print(" : Emas Lantakan");
                                }else{
                                    out.print(" : Emas & Berlian");
                                }
                            }
                        %>
		  <!-- #EndEditable --></td>
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
              <input type="hidden" name="hidden_location_id" value="<%=oidLocation %>">
              <input type="hidden" name="type_doc" value="1">
              <input type="hidden" name="typeharian" value="<%=typeharian%>">
              <input type="hidden" name="<%=FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_STOCK_OPNAME_NUMBER]%>" value="<%=so.getStockOpnameNumber()%>">
              <input type="hidden" name="<%=FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_SUB_CATEGORY_ID]%>" value="<%=so.getSubCategoryId()%>">
              <input type="hidden" name="emas_lantakan" value="<%=lantakan%>">
              <%
                if (typeOfBusinessDetail == 2) {
                    if (lantakan == 1) {
                    %>
                        <input type="hidden" name="<%=FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_OPNAME_ITEM_TYPE]%>" value="<%=Material.MATERIAL_TYPE_EMAS_LANTAKAN%>">
                    <%
                    }
                }
              %>
              <table width="100%" border="0">
                <tr> 
                  <td colspan="3"> 
                    <hr size="1" noshade>
                  </td>
                </tr>
                <tr valign="top">
                  <td colspan="3">
                    <table width="100%"  border="0" cellspacing="1" cellpadding="1">
                      <tr>
                        <td width="8%"><%=textListOrderHeader[SESS_LANGUAGE][0]%></td>
                        <td width="27%">: <b>                           <%
						  if((so.getStockOpnameNumber()).length() > 0 && iErrCode==0) {
							out.println(so.getStockOpnameNumber());
						  }
						  else {
						  	out.println("");
						  }
						  %></b></td>
                        <td width="8%"><%=textListOrderHeader[SESS_LANGUAGE][5]%> </td>
                        <td width="30%">:
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
								key_supplier.add(""+cnt.getContactCode()+" - "+cntName);
							}
							String select_supplier = ""+so.getSupplierId();
						  %>
                        <%=ControlCombo.draw(FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_SUPPLIER_ID],null,select_supplier,val_supplier,key_supplier,"","formElemen")%><%}%></td>
                        <td width="10%"><%=textListOrderHeader[SESS_LANGUAGE][8]%></td>
                        <td width="17%" rowspan="3" valign="top">                          <textarea name="<%=FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_REMARK]%>" class="formElemen" wrap="VIRTUAL" rows="4"><%=so.getRemark()%></textarea></td>
                      </tr>
                      <tr>
                        <td><%=textListOrderHeader[SESS_LANGUAGE][3]%></td>
                        <td>: <%=ControlDate.drawTimeSec(FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_STOCK_OPNAME_DATE], (so.getStockOpnameDate()==null) ? new Date(): so.getStockOpnameDate(),"formElemen")%></td>                        
                        <td><%=textListOrderHeader[SESS_LANGUAGE][6]%></td>
                        <td>: 

                         
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
                               <select id="<%=FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_CATEGORY_ID]%>"  name="<%=FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_CATEGORY_ID]%>" class="formElemen">
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
			</td>
                        <td>&nbsp;</td>
                      </tr>
                      <tr>
                        <td><%=textListOrderHeader[SESS_LANGUAGE][2]%></td>
                        <td>: <%=ControlDate.drawDateWithStyle(FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_STOCK_OPNAME_DATE], (so.getStockOpnameDate()==null) ? new Date() : so.getStockOpnameDate(), 1, -5, "formElemen", "")%></td>
                        <%
                            String mapping = PstSystemProperty.getValueByName("GROUP_TYPE_USE_IN_OPNAME");
                            if (mapping.length()>0 && !mapping.equals("Not Initialized")){
                                Vector listMasterGroup = PstMasterGroup.list(0, 0, PstMasterGroup.fieldNames[PstMasterGroup.FLD_TYPE_GROUP]+"="+mapping, "");
                                if (listMasterGroup.size()>0){
                                    MasterGroup masterGroup = (MasterGroup) listMasterGroup.get(0);
                                    Vector listType = PstMasterType.list(0, 0, 
                                        PstMasterType.fieldNames[PstMasterType.FLD_TYPE_GROUP]+"="+masterGroup.getTypeGroup(),
                                        PstMasterType.fieldNames[PstMasterType.FLD_MASTER_NAME]);
                                    
                                     Vector vValue = new Vector(1,1);
                                    Vector vKey = new Vector(1,1);

                                    vValue.add("0");
                                    vKey.add("-");
                                    if (listType.size() > 0){
                                        for (int x=0; x < listType.size(); x++){
                                            MasterType masterType = (MasterType) listType.get(x);
                                            vValue.add(""+masterType.getOID());
                                            vKey.add(masterType.getMasterName());
                                        }
                                    }    
                                    String select_master = ""+so.getMasterGroupId();
                                    %>
                                        <td><%=masterGroup.getNamaGroup()%></td>
                                        <td>: <%=ControlCombo.draw(FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_MASTER_TYPE_ID],"formElemen", null, select_master, vValue, vKey, null)%></td>
                                    <%
                                }
                            }
                        %>
                        <!-- combo box status -->
                        
                        <!-- end of status -->
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
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
                        <%=ControlCombo.draw(FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_LOCATION_ID], null, select_locationid, val_locationid, key_locationid, "", "formElemen loc")%> </td>
                        
                        <% if(typeOfBusinessDetail == 2){
                            Vector val_etalase = new Vector(1,1);
                            Vector key_etalase = new Vector(1,1);
                            long loc_eta = 0;
                            if (iCommand == Command.ADD && !vt_loc.isEmpty()) {
                                Location loc = (Location)vt_loc.get(0);
                                loc_eta = loc.getOID();
                            } else {
                                loc_eta = so.getLocationId();
                            }
                            Vector listEtalase = PstKsg.list(0, 0, ""+PstKsg.fieldNames[PstKsg.FLD_LOCATION_ID]+"='"+loc_eta+"'", ""+PstKsg.fieldNames[PstKsg.FLD_CODE]);
                            for (int i=0; i<listEtalase.size(); i++) {
                                Ksg ksg = (Ksg) listEtalase.get(i);
                                Location loc = PstLocation.fetchExc(ksg.getLocationId());
                                val_etalase.add(""+ksg.getOID());
                                key_etalase.add(""+loc.getCode()+" - "+ksg.getCode());
                            }
                        %>
                        <td>Etalase</td>
                        <td>:                            
                            <%=ControlCombo.draw(FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_ETALASE_ID], null, ""+so.getEtalaseId(), val_etalase, key_etalase, "", "formElemen eta")%>
                        </td>
                        <%--
                        <td>Tipe Item Opname</td>
                        <td>
                            <%
                            Vector val_itemTipe = new Vector(1,1);
                            Vector key_itemType = new Vector(1,1);                            
                            val_itemTipe.add(""+Material.MATERIAL_TYPE_EMAS);
                            key_itemType.add(""+Material.MATERIAL_TYPE_TITLE[Material.MATERIAL_TYPE_EMAS]);
                            val_itemTipe.add(""+Material.MATERIAL_TYPE_BERLIAN);
                            key_itemType.add(""+Material.MATERIAL_TYPE_TITLE[Material.MATERIAL_TYPE_BERLIAN]);
                            val_itemTipe.add(""+Material.MATERIAL_TYPE_EMAS_LANTAKAN);
                            key_itemType.add(""+Material.MATERIAL_TYPE_TITLE[Material.MATERIAL_TYPE_EMAS_LANTAKAN]);  
                            %>
                            <%=ControlCombo.draw(FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_OPNAME_ITEM_TYPE], null, ""+so.getOpnameItemType(), val_itemTipe, key_itemType, "", "formElemen")%>
                        </td>
                        --%>
                        <%}%>
                        <td><%=textListOrderHeader[SESS_LANGUAGE][4]%></td>
                        <td>:
                        <%
                          Vector obj_status = new Vector(1,1);
                          Vector val_status = new Vector(1,1);
                          Vector key_status = new Vector(1,1);

                          val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_DRAFT));
                          key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]);			  
                          //added by dewok 2018-12-22
                          val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_CANCELLED));
                          key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_CANCELLED]);			  
			  //add by fitra
                          val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED));
                          key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED]);
                          
                          if(vectSizeItem!=0 && listError.size()==0 && privApprovalFinal){
                              val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_FINAL));
                              key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);
                          }
                          
                          String select_status = ""+so.getStockOpnameStatus();
                          if(so.getStockOpnameStatus()==I_DocStatus.DOCUMENT_STATUS_CLOSED){
                              out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_CLOSED]);
                          }else if(so.getStockOpnameStatus()==I_DocStatus.DOCUMENT_STATUS_POSTED){
                              out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_POSTED]);
                          }else{
                              out.println(ControlCombo.draw(FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_STOCK_OPNAME_STATUS],null,select_status,val_status,key_status,"tabindex=\"4\"","formElemen"));
                          }
                        %>
                        &nbsp;
                        </td>
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
                        <td height="22" valign="middle" colspan="3"><%=list.get(0)%></td>
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
                          <%if(so.getStockOpnameStatus()==I_DocStatus.DOCUMENT_STATUS_DRAFT ){%>
                           <tr align="left" valign="top">
                            <td height="22" valign="middle" colspan="3">
                              <table width="50%" border="0" cellspacing="2" cellpadding="0">
                                <tr>
                                  <td width="5%"><a href="javascript:addItem()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE," Item",ctrLine.CMD_ADD,true)%>"></a></td>
                                  <td width="30%"><a href="javascript:addItem()"><%=ctrLine.getCommand(SESS_LANGUAGE," Item",ctrLine.CMD_ADD,true)%></a></td>
                                  
                                  <td width="5%"><a href="javascript:addAllItem()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE," Item",ctrLine.CMD_ADD,true)%>"></a></td>
                                  <td width="30%"><a href="javascript:addAllItem()">Tambah All Item</a></td>
                                  <%if(privViewCorrection){%>
                                    <td width="5%"><a href="javascript:banding('<%=oidStockOpname%>')" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE," Item",ctrLine.CMD_ADD,true)%>"></a></td>
                                    <td width="40%"><a href="javascript:banding('<%=oidStockOpname%>')">Bandingkan Stok Dng Sistem</a></td>
                                  <%}%>
                                </tr>
                              </table>
                            </td>
                          </tr>
                          <%}%>
                      <%}%>
                    </table>
                  </td>
                </tr>
                <tr>
                  <td colspan="2" valign="top">&nbsp;</td>
                  <td width="30%">&nbsp;</td>
                </tr>
                <%//if(oidStockOpname!=0){%>
			    <%if(listMatStockOpnameItem!=null && listMatStockOpnameItem.size()>0){%>
                <tr>
                  <td colspan="3">&nbsp;</td>
                </tr>
				<%}%>
                <tr>
                  <td colspan="3">
                    <table width="100%" border="0">
                      <tr>
                        <td width="60%">
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
		
							if(privAdd==false || so.getStockOpnameStatus()!=I_DocStatus.DOCUMENT_STATUS_DRAFT ){
								ctrLine.setAddCaption("");
							}
                                                        
		
							if(iCommand==Command.SAVE && frmso.errorSize()==0){
                                                                ctrLine.setAddCaption("");
                                                                ctrLine.setSaveCaption("Save");
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
                          <%=ctrLine.drawImage(iCommand,iErrCode,errMsg)%> </td>
                        <td width="40%" align="right">
                          <% if(listMatStockOpnameItem!=null && listMatStockOpnameItem.size()>0) { %>
				 <table  border="0" cellpadding="0" cellspacing="0">
                                        <tr>
                                            <td width="10%">Group By</td>
                                            <td width="20%">
                                            <%

                                                Vector val_groupby = new Vector(1,1);
                                                Vector key_groupby = new Vector(1,1);

                                                val_groupby.add("1");
                                                key_groupby.add("By Kategori");
                                                val_groupby.add("2");
                                                key_groupby.add("By Supplier");
                                                val_groupby.add("3");
                                                key_groupby.add("By Rak Gondola");
                                            %>  
                                            <%= ControlCombo.draw("groupByElement", null, "", val_groupby, key_groupby, "", "formElemen")%>
                                            </td>
                                          <td width="5%" valign="top">
                                              <a href="javascript:printForm('<%=oidStockOpname%>')"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0" alt="<%=textListGlobal[SESS_LANGUAGE][6]%>"></a>
                                          </td>
                                          <td width="30%" nowrap>&nbsp; <a href="javascript:printForm('<%=oidStockOpname%>')" class="command"><%=textListGlobal[SESS_LANGUAGE][6]%></a></td>
                                          <td width="5%" valign="top"><a href="javascript:printFormExcel('<%=oidStockOpname%>')"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0" alt="<%=textListGlobal[SESS_LANGUAGE][6]%>"></a></td>
                                          <td width="30%" nowrap>&nbsp; <a href="javascript:printFormExcel('<%=oidStockOpname%>')" class="command">Export to Excel</a></td>
                                        </tr>
                                    </table>
                            <% } %>
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>
                <tr>
                    <td><a class="btn btn-primary" href="javascript:viewHistoryTable()"><i class="fa fa-file"></i> VIEW TABEL HISTORY</a></td>
                </tr>
              </table>
<%
}
catch(Exception e) {
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
      <!-- #EndEditable --> </td>
    
    
    <script language="JavaScript">
            <% 
            // add by fitra 10-5-2014   


if(so.getStockOpnameStatus()==I_DocStatus.DOCUMENT_STATUS_DRAFT && iCommand==Command.SAVE){%>  
               //addItem();
            <% } %>
      </script>
  </tr>
</table>
</body>


<!-- #EndTemplate --></html>

<%-- 
    Document   : componen_composit_excel
    Created on : Mar 17, 2015, 3:41:24 PM
    Author     : dimata005
--%>

<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*,
                   com.dimata.posbo.entity.masterdata.*,
                   com.dimata.posbo.form.masterdata.*,
                   com.dimata.posbo.session.warehouse.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*,
                   com.dimata.util.Command " %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.entity.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package common -->
<%@ page import = "com.dimata.common.entity.location.*" %>
<%@ page import = "com.dimata.common.entity.contact.*" %>
<%@ page import = "com.dimata.common.entity.payment.*" %>
<!--package material -->
<%@ page import = "com.dimata.harisma.entity.masterdata.*" %>
<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_CATALOG); %>
<%@ include file = "../../main/checkuser.jsp" %>
<%!


public static final String textListTitleHeader[][] =
{
	{"Daftar Component ","SKU","Klik sku untuk kembali ke edit barang","Nama Composit",//3
	 "Tidak ada data component...","Nama Component","Kode","Unit","Qty","Barcode",//9
	  "Nilai Stok","Total Nilai Stok","Total Nilai Stock Composit",//12
  "Tambah Component","Kembali ke Daftar Barang","Harga Beli Terakhir","Total Harga Beli Terakhir","Item Componen","Unit Konversi","Qty Entri"},//19
	{"Component List","SKU","Click sku for back to goods edit","Composite Material Name",
	 "No data component ..","Component Name","Code","Unit","Qty","Barcode",
	 "Nilai Stok","Total Nilai Stok","Total Stock Value Composit",
	 "Add Component","Back to Search Result", "Last Buying Price","Total Last Buying Price","Item Component","Unit Entry","Qty Conversion"}

};




/**
* this method used to list all po item
*/
public Vector drawListCompositItem(int language,Vector objectClass,int start,boolean privManageData,int iCommand,FrmMaterialComposit frmObject, long oidMatComposit){
    Vector list = new Vector(1,1);
    Vector listError = new Vector(1,1);
    String result = "";
   if((objectClass!=null && objectClass.size()>0) || iCommand == Command.ADD){
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.setBorder(1);
        ctrlist.addHeader("No","3%","0","0"); //no
	ctrlist.addHeader(textListTitleHeader[language][6],"15%","0","0"); //kode
	ctrlist.addHeader(textListTitleHeader[language][5],"20%","0","0"); //nama
        ctrlist.addHeader(textListTitleHeader[language][19],"5%","0","0");
        ctrlist.addHeader(textListTitleHeader[language][18],"5%","0","0");//unit konversi
        ctrlist.addHeader(textListTitleHeader[language][7],"5%","0","0"); //unit
        ctrlist.addHeader(textListTitleHeader[language][8],"7%","0","0");
	ctrlist.addHeader(textListTitleHeader[language][10],"10%","0","0");
	ctrlist.addHeader(textListTitleHeader[language][11],"10%","0","0");
        ctrlist.addHeader(textListTitleHeader[language][15],"10%","0","0");
	ctrlist.addHeader(textListTitleHeader[language][16],"10%","0","0");
        
        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        Vector rowx = new Vector(1,1);
        ctrlist.reset();
        int index = -1;
        if(start<0){
            start=0;
        }

        /**
         * add opie 28-06-2012 untuk konversi
         */
        String whereUnit = "";
        Vector listBuyUnit = PstUnit.list(0,1000,whereUnit,"");
        Vector index_value = new Vector(1,1);
        Vector index_key = new Vector(1,1);
        index_key.add("-");
        index_value.add("0");
        for(int i=0;i<listBuyUnit.size();i++){
            Unit mateUnit = (Unit)listBuyUnit.get(i);
            index_key.add(mateUnit.getCode());
            index_value.add(""+mateUnit.getOID());
        }

        double totalStockValue = 0.0;
        double totalCost = 0.0;
        double totalStockAll = 0.0;
        double totalCostAll = 0.0;
        double totalqtyinput =0.0;
        for(int i=0; i<objectClass.size(); i++){
            Vector temp = (Vector)objectClass.get(i);
            MaterialComposit materialComposit = (MaterialComposit)temp.get(0);
            Unit unit = (Unit)temp.get(1);
            Material mat = (Material)temp.get(2);
            
            rowx = new Vector();
            start = start + 1;

        if (oidMatComposit == materialComposit.getOID()) index = i;

        if(index==i && (iCommand==Command.EDIT || iCommand==Command.ASK)) {
            rowx.add(""+start);
            rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_COMPOSIT_ID]+"\" value=\""+materialComposit.getOID()+
                    "\"><input type=\"text\" size=\"15\" name=\"matCode\" value=\""+mat.getSku()+"\" class=\"formElemen\" onKeyDown=\"javascript:keyDownCheck(event)\">"); // <a href=\"javascript:cmdCheck()\">CHK</a>
            rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_COMPOSER_ID]+"\" value=\""+materialComposit.getMaterialComposerId()+
                "\"><input type=\"text\" size=\"20\" name=\"matItem\" value=\""+mat.getName()+"\" class=\"hiddenText\" readOnly>");
            
            rowx.add("<div align=\"center\">"+ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID_KONVERSI], null, ""+0, index_value, index_key,"","formElemen")+"</div>");
            rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY] +"\" value=\""+materialComposit.getQty()+"\" class=\"formElemen\"  onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
            
            rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"\" value=\""+materialComposit.getUnitId()+
                    "\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\""+unit.getCode()+"\" class=\"hiddenText\" readOnly>");
            totalqtyinput= materialComposit.getQty();
            //add opie 28-06-2012
            rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY_INPUT] +"\" value=\""+FRMHandler.userFormatStringDecimal(totalqtyinput,1)+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(mat.getAveragePrice(),1)+"</div>");
            totalStockValue = materialComposit.getQty() * mat.getAveragePrice();
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(totalStockValue,1)+"</div>");
            totalStockAll = totalStockAll + totalStockValue;
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(mat.getCurrBuyPrice(),1)+"</div>");
            totalCost = materialComposit.getQty() * mat.getCurrBuyPrice();
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(totalCost,1)+"</div>");
            totalCostAll = totalCostAll + totalCost;
            
        } else {
            
            rowx.add(""+start+"");
            if(privManageData) {
                rowx.add("<a href=\"javascript:editItem('"+String.valueOf(materialComposit.getOID())+"')\">"+mat.getSku()+"</a>");
            }else{
                rowx.add(mat.getSku());
            }
            //rowx.add(mat.getBarCode());
            rowx.add(mat.getName());
            //add opie 28-06-2012
            rowx.add("<div align=\"center\">0"+"</div>");// qty entri
            rowx.add("<div align=\"center\">0"+"</div>");
            rowx.add("<div align=\"center\">"+unit.getCode()+"</div>");
            rowx.add("<div align=\"right\">"+materialComposit.getQty()+"</div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(mat.getAveragePrice(),1)+"</div>");
            totalStockValue = materialComposit.getQty() * mat.getAveragePrice();
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(totalStockValue,1)+"</div>");
            totalStockAll = totalStockAll + totalStockValue;
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(mat.getCurrBuyPrice(),1)+"</div>");
            totalCost = materialComposit.getQty() * mat.getCurrBuyPrice();
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(totalCost,1)+"</div>");
            totalCostAll = totalCostAll + totalCost;
           }
            lstData.add(rowx);
        }

            rowx = new Vector();
        if(iCommand==Command.ADD || (iCommand==Command.SAVE && frmObject.errorSize()>0)) {
            rowx.add(""+(start+1));
            rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_COMPOSER_ID]+"\" value=\""+""+
                "\"><input type=\"text\" size=\"15\" name=\"matCode\" value=\""+""+"\" class=\"formElemen\" onKeyDown=\"javascript:keyDownCheck(event)\"><a href=\"javascript:cmdCheck()\">CHK</a>");
            rowx.add("<input type=\"text\" size=\"20\" name=\"matItem\" value=\""+""+"\" class=\"formElemen\" onKeyDown=\"javascript:keyDownCheck(event)\"><a href=\"javascript:cmdCheck()\">CHK</a>");
            rowx.add("<div align=\"center\"><input type=\"text\" size=\"7\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY_INPUT] +"\" value=\""+""+"\" class=\"formElemen\" onkeyup=\"javascript:change(this.value)\"</div>");
            rowx.add("<div align=\"center\">"+ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID_KONVERSI], null, ""+0, index_value, index_key,"onChange=\"javascript:showData(this.value)\"","formElemen")+" </div>");
            
            rowx.add("<div align=\"center\"><input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"\" value=\""+""+
                "\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\""+""+"\" class=\"hiddenText\" readOnly> </div>");
             //add opie 28-06-2012
            rowx.add("<div align=\"right\"><input type=\"text\" size=\"7\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY] +"\" value=\""+""+"\" class=\"formElemen\" </div>");
            rowx.add("<div align=\"right\"><input type=\"text\" size=\"10\" name=\"nilaiStock\" value=\""+""+"\" class=\"formElemen\" disabled=\"true\" </div>");
            rowx.add("<div align=\"right\"><input type=\"text\" size=\"10\" name=\"totalNilaiStock\" value=\""+""+"\" class=\"formElemen\" disabled=\"true\" </div>");
            rowx.add("<div align=\"right\"><input type=\"text\" size=\"10\" name=\"matPrice\" value=\""+""+"\" class=\"formElemen\" disabled=\"true\" </div>");
            rowx.add("<div align=\"right\"><input type=\"text\" size=\"10\" name=\"totalCost\" value=\""+""+"\" class=\"formElemen\" disabled=\"true\" </div>");
        lstData.add(rowx);
       }

            Vector rowy = new Vector();
            rowy.add("");
            rowy.add("");
            rowy.add("");
            rowy.add("");
            
             //add opie 28-06-2012
            rowy.add("");
            rowy.add("");
            rowy.add("");
            rowy.add("<div align=\"right\"><b>TOTAL NILAI STOCK COMPOSIT</b></div>");
            rowy.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(totalStockAll,1)+"</b></div>");
            rowy.add("<div align=\"right\"><b>TOTAL HARGA BELI TERAKHIR COMPOSIT</b></div>");
            rowy.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(totalCostAll,1)+"</b></div>");

            lstData.add(rowy);

        result = ctrlist.draw();
      //}

   }else{
        result = "<div class=\"msginfo\">"+textListTitleHeader[language][4]+"</div>";
    }
    list.add(result);
    list.add(listError);
    return list;
}


%>


<!-- Jsp Block -->


<%
/**
* get request data from current form
*/
int iCommand = FRMQueryString.requestCommand(request);
int prevCommand = FRMQueryString.requestInt(request,"prev_command");
int startItem = FRMQueryString.requestInt(request,"start_item");
int cmdItem = FRMQueryString.requestInt(request,"command_item");
int appCommand = FRMQueryString.requestInt(request,"approval_command");
long oidMatComposit = FRMQueryString.requestLong(request, "hidden_material_composit_id");
long oidMaterial = FRMQueryString.requestLong(request, "hidden_material_id");



/**
* initialization of some identifier
*/
String errMsg = "";
int iErrCode = FRMMessage.ERR_NONE;

/**
* purchasing ret code and title
*/
String recCode = "";//i_pstDocType.getDocCode(docType);
String recTitle = textListTitleHeader[SESS_LANGUAGE][17];//"Terima Barang";//i_pstDocType.getDocTitle(docType);
String recItemTitle = recTitle + " Item";

/**
* action process
*/

Material mat = null;
try{
	mat = PstMaterial.fetchExc(oidMaterial);
}catch(Exception e){}

ControlLine ctrLine = new ControlLine();
CtrlMaterialComposit ctrlMaterialComposit = new CtrlMaterialComposit(request);
iErrCode = ctrlMaterialComposit.action(iCommand , oidMatComposit);
FrmMaterialComposit frmMaterialComposit = ctrlMaterialComposit.getForm();
MaterialComposit materialComposit = ctrlMaterialComposit.getMaterialComposit();
errMsg =  ctrlMaterialComposit.getMessage();



/**
* generate code of current currency
*/
String priceCode = "Rp.";

/**
* check if document may modified or not
*/
boolean privManageData = true;

/**
* list purchase order item
*/
oidMatComposit = materialComposit.getOID();
int recordToGetItem = 0;
String whereClauseItem = " MC."+PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_ID]+"="+oidMaterial;
String orderClauseItem = " MC."+PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_COMPOSIT_ID];
int vectSizeItem = PstMaterialComposit.getCount(whereClauseItem);
Vector listMatComposit = new Vector();

//if(materialComposit!=null && materialComposit.getOID()!=0){
//add opie 27-06-2012
if((iCommand == Command.FIRST || iCommand == Command.PREV )||
  (iCommand == Command.NEXT || iCommand == Command.LAST)){
		startItem = ctrlMaterialComposit.actionList(iCommand, startItem,vectSizeItem, recordToGetItem);
 }

listMatComposit = PstMaterialComposit.list(startItem, recordToGetItem, whereClauseItem, orderClauseItem);
//}
//update nilai stock dan harga beli terakhir
//oidMaterial = materialComposit.getMaterialId();
if(iCommand == Command.SAVE || iCommand == Command.DELETE){
Vector grandTotalComposit = PstMaterialComposit.grandTotalComposit(oidMaterial);

                        for(int i=0;i<grandTotalComposit.size();i++){
                            Vector temp = (Vector)grandTotalComposit.get(i);
                               double sumStockValue = (Double)temp.get(0);
                               double sumCostValue = (Double)temp.get(1);
                               boolean isOK = false;

                               double sumCostNoPpnValue = sumCostValue/1.1;
                               double lastVat = Double.parseDouble(PstSystemProperty.getValueByName("POS_PPN_DEFAULT"));


                              isOK = PstMaterialComposit.updateCostAndStockValueMaster(oidMaterial, sumStockValue, sumCostValue, sumCostNoPpnValue, lastVat);
                              System.out.println("=============== update stockValue and lastCost : "+sumStockValue);
                              System.out.println("=============== update lastCost : "+sumCostValue);
                     }
}

//add opie 26-06-2012
if (listMatComposit.size() < 1 && startItem > 0)
{
	 if (vectSizeItem - recordToGetItem > recordToGetItem)
			startItem = startItem - recordToGetItem;   //go to Command.PREV
	 else{
		 startItem = 0 ;
		 iCommand = Command.FIRST;
		 prevCommand = Command.FIRST; //go to Command.FIRST
	 }
	 listMatComposit = PstMaterialComposit.list(startItem, recordToGetItem, whereClauseItem, orderClauseItem);
}

long selectedUnitId =0;
if((iCommand==Command.ADD)){
           selectedUnitId = FRMQueryString.requestLong(request, FrmMaterialComposit.fieldNames[frmMaterialComposit.FRM_FIELD_UNIT_ID]);
}
%>
<!-- End of Jsp Block -->
<%@ page contentType="application/x-msexcel" %>
<html>
<head>
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
<!--
//------------------------- START JAVASCRIPT FUNCTION FOR PO MAIN -----------------------

var windowSupplier;
function addSupplier(){
    windowSupplier=window.open("../../../master/contact/contact_company_edit.jsp?command=<%=Command.ADD%>&source_link=receive_wh_supp_material_edit.jsp","add_supplier", "height=600,width=800,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
    if (window.focus) { windowSupplier.focus();}
}

function  setSupplierOnLGR(textIn, supOID){
    var oOption = self.opener.document.createElement("OPTION");
    oOption.text=textIn;
    oOption.value="supOID";
    document.forms.frm_compositmat.FRM_FIELD_SUPPLIER_ID.add(oOption);
    document.forms.frm_compositmat.FRM_FIELD_SUPPLIER_ID.value = "504404432982825708";
    changeFocus(self.opener.document.forms.frm_compositmat.FRM_FIELD_SUPPLIER_ID);
}

function cmdEdit(oid){
    document.frm_compositmat.command.value="<%=Command.EDIT%>";
    document.frm_compositmat.prev_command.value="<%=prevCommand%>";
    document.frm_compositmat.action="componen_composit_edit.jsp";
    document.frm_compositmat.submit();
}

function cmdSave() {      
    document.frm_compositmat.command.value="<%=Command.SAVE%>";
    document.frm_compositmat.prev_command.value="<%=prevCommand%>";
    document.frm_compositmat.action="componen_composit_edit.jsp";
    //if(compare()==true)
        document.frm_compositmat.submit();
}

function cmdAsk(oid) {
        document.frm_compositmat.command.value="<%=Command.ASK%>";
        document.frm_compositmat.prev_command.value="<%=prevCommand%>";
        document.frm_compositmat.action="componen_composit_edit.jsp";
        document.frm_compositmat.submit();
}

function gostock(oid) {
    document.frm_compositmat.command.value="<%=Command.EDIT%>";
    document.frm_compositmat.hidden_material_composit_id.value=oid;
    document.frm_compositmat.action="rec_wh_stockcode.jsp";
    document.frm_compositmat.submit();
}

function cmdCancel() {
    document.frm_compositmat.command.value="<%=Command.CANCEL%>";
    document.frm_compositmat.prev_command.value="<%=prevCommand%>";
    document.frm_compositmat.action="componen_composit_edit.jsp";
    document.frm_compositmat.submit();
}

function cmdConfirmDelete(oid) {
    document.frm_compositmat.command.value="<%=Command.DELETE%>";
    document.frm_compositmat.prev_command.value="<%=prevCommand%>";
    document.frm_compositmat.approval_command.value="<%=Command.DELETE%>";
    document.frm_compositmat.action="componen_composit_edit.jsp";
    document.frm_compositmat.submit();
}

function cmdBack(){
	document.frm_compositmat.command.value="<%=Command.BACK%>";
	document.frm_compositmat.action="componen_composit_edit.jsp";
	document.frm_compositmat.submit();
}

function cmdBackList(){
	document.frm_compositmat.command.value="<%=Command.BACK%>";
	document.frm_compositmat.action="material_list.jsp";
	document.frm_compositmat.submit();
}

function editbarang(oid){
    document.frm_compositmat.hidden_material_id.value = oid;
	document.frm_compositmat.command.value="<%=Command.EDIT%>";
	document.frm_compositmat.prev_command.value="<%=Command.FIRST%>";
	document.frm_compositmat.action="material_main.jsp";
	document.frm_compositmat.submit();
}


function cmdPrint(){
    //alert("hallo");
    window.open("componen_composit_excel.jsp?hidden_material_id=<%=oidMaterial%>&command=<%=Command.EDIT%>","pireport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
}
//------------------------- END JAVASCRIPT FUNCTION FOR PO MAIN -----------------------


//------------------------- START JAVASCRIPT FUNCTION FOR PO ITEM -----------------------
function addItem() {
        document.frm_compositmat.command.value="<%=Command.ADD%>";
        document.frm_compositmat.action="componen_composit_edit.jsp";
        if(compareDateForAdd()==true)
            document.frm_compositmat.submit();
}

function editItem(oid) {
        document.frm_compositmat.command.value="<%=Command.EDIT%>";
        document.frm_compositmat.hidden_material_composit_id.value=oid;
        document.frm_compositmat.action="componen_composit_edit.jsp";
        document.frm_compositmat.submit();

}

function itemList(comm) {
    document.frm_compositmat.command.value=comm;
    document.frm_compositmat.prev_command.value=comm;
    document.frm_compositmat.action="componen_composit_edit.jsp";
    document.frm_compositmat.submit();
}

var winSrcMaterial;
function cmdCheck() {
    var strvalue  = "materialcomponentdosearch.jsp?command=<%=Command.FIRST%>"+
                    "&mat_code="+document.frm_compositmat.matCode.value+
                    "&txt_materialname="+document.frm_compositmat.matItem.value;
    winSrcMaterial = window.open(strvalue,"material", "height=600,width=800,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
    if (window.focus) { winSrcMaterial.focus();}
}


function keyDownCheck(e){
   if (e.keyCode == 13) {
        //document.all.aSearch.focus();
        cmdCheck();
   }
}


function change(value){
     document.frm_compositmat.<%=FrmMaterialComposit.fieldNames[FrmMaterialComposit.FRM_FIELD_QTY]%>.value=value;
     document.frm_compositmat.hidden_qty_input.value=value;
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

function MM_swapImage() { //v3.0
	var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
	if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}
//------------------------- END JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}
//-->
</script>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<script type="text/javascript">
function showData(value){

   var qtyInput = document.frm_compositmat.hidden_qty_input.value;
   var oidUnit = document.frm_compositmat.hidden_qty_unit.value;
   var oidKonversiUnit=value;
    //alert("x "+oidUnit);
   checkAjax(oidKonversiUnit,oidUnit,qtyInput);
}

function checkAjax(oidKonversiUnit,oidUnit, qtyInput){
    $.ajax({
    url : "<%=approot%>/servlet/com.dimata.posbo.ajax.KonversiUnitComposite?<%=FrmMaterialComposit.fieldNames[FrmMaterialComposit.FRM_FIELD_UNIT_ID_KONVERSI]%>="+oidKonversiUnit+"&<%=FrmMaterialComposit.fieldNames[FrmMaterialComposit.FRM_FIELD_UNIT_ID]%>="+oidUnit+"&<%=FrmMaterialComposit.fieldNames[FrmMaterialComposit.FRM_FIELD_QTY_INPUT]%>="+qtyInput+"",
    type : "POST",
    async : false,
    success : function(data) {
        //alert("hello");
        document.frm_compositmat.<%=FrmMaterialComposit.fieldNames[FrmMaterialComposit.FRM_FIELD_QTY]%>.value=data;
    }
});

}
</script>
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
  <tr> 
    <td valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">  
        <tr> 
          <td><!-- #BeginEditable "content" -->
            <form name="frm_compositmat" method="post" action="">
            <%
            try {
            %>
              <input type="hidden" name="command" value="">
              <input type="hidden" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="start_item" value="<%=startItem%>">
              <input type="hidden" name="command_item" value="<%=cmdItem%>">
              <input type="hidden" name="approval_command" value="<%=appCommand%>">
              <input type="hidden" name="hidden_material_composit_id" value="<%=oidMatComposit%>">
              <input type="hidden" name="hidden_material_id" value="<%=oidMaterial%>">
              <input type="hidden" name="<%=FrmMaterialComposit.fieldNames[FrmMaterialComposit.FRM_FIELD_MATERIAL_ID]%>" value="<%=oidMaterial%>">
              <input type="hidden" name="hidden_qty_unit" value="">
              <input type="hidden" name="hidden_qty_input" value="">
              <table width="100%" border="0">
                <tr>
                  <td valign="top" colspan="3">&nbsp;
                  </td>
                </tr>
                <tr>
                  <td colspan="3">
                    <table width="100%" border="0" cellpadding="1">
                      <tr>
                        <td width="5%" align="left"><%=textListTitleHeader[SESS_LANGUAGE][1]%> : <b><%=mat.getSku()%></b></td>
                      </tr>
                      <tr>
                        <td width="5%" align="left"><%=textListTitleHeader[SESS_LANGUAGE][3]%>: <%=mat.getName()%></td>
                     </tr>
                     <tr>
                        <td width="5%" align="left">&nbsp;</td>
                     </tr>
                    </table>
                  </td>
                </tr>
                <tr>
                  <td colspan="3">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr align="left" valign="top">
                        <td height="22" valign="middle" colspan="3">
                        <%
                            Vector list = drawListCompositItem(SESS_LANGUAGE,listMatComposit,startItem,privManageData,iCommand,frmMaterialComposit, oidMatComposit);
                            out.println(""+list.get(0));
                            Vector listError = (Vector)list.get(1);
                        %>
                        </td>
                      </tr>
                      <tr align="left" valign="top">
                        <td height="22" colspan="3" valign="middle" class="errfont">
                          <%
                            for(int k=0;k<listError.size();k++){
                              if(k==0)
                                  out.println(listError.get(k)+"<br>");
                              else
                                  out.println("&nbsp;&nbsp;&nbsp;"+listError.get(k)+"<br>");
                            }
			  %>	
                        </td>
                      </tr>
                      <%//}%>
                    </table>
                  </td>
                </tr>
                <tr>
                  <td valign="top" colspan="3">&nbsp;</td>
                </tr>
                <tr>
                  <td valign="top" colspan="3">&nbsp;</td>
                </tr>
                <tr>
                  <td colspan="3">
                    <table width="100%" border="0">
                      <tr>
                        <td>&nbsp;</td>
                      </tr>
                    </table>
                  </td>
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
</table>
</body>
<!-- #EndTemplate --></html>


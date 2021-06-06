<%@ page import="com.dimata.posbo.entity.admin.PstAppUser,
                 com.dimata.posbo.form.masterdata.FrmMatVendorPrice,
                 com.dimata.gui.jsp.ControlList,
                 com.dimata.qdep.form.FRMHandler,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.qdep.form.FRMMessage,
                 com.dimata.common.entity.payment.*,
                 com.dimata.posbo.entity.masterdata.*,
                 com.dimata.posbo.form.masterdata.CtrlMatVendorPrice,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.util.Command,
                 com.dimata.gui.jsp.ControlCombo,
                 com.dimata.common.entity.payment.PstCurrencyType,
                 com.dimata.common.entity.payment.CurrencyType,
                 com.dimata.common.entity.payment.DiscountType,
                 com.dimata.posbo.form.warehouse.FrmMatReceiveItem,
                 com.dimata.posbo.entity.warehouse.PstMatReceive "%>
<%
/*
 * Page Name  		:  vdrmaterialprice.jsp
 * Created on 		:  [date] [time] AM/PM
 *
 * @author  		:  [authorName]
 * @version  		:  [version]
 */

/*******************************************************************
 * Page Description 	: [project description ... ]
 * Imput Parameters 	: [input parameter ...]
 * Output 			: [output ...]
 *******************************************************************/
%>
<%@ page language = "java" %>

<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_CATALOG); %>
<%@ include file = "../../main/checkuser.jsp" %>

<%
if(userGroupNewStatus == PstAppUser.GROUP_SUPERVISOR)
{
	privAdd=false;
	privUpdate=false;
	privDelete=false;
}
%>
<!-- Jsp Block -->
<%!

public static Vector parserVendorCode(String vCode){
    Vector vt = new Vector();
    StringTokenizer st0 = new StringTokenizer(vCode.toUpperCase(),";");
    while(st0.hasMoreTokens()){
        String strTemp1 = st0.nextToken();
        System.out.println("test : "+strTemp1);
        vt.add(strTemp1);
    }
    return vt;
}

public static final String textListTitleHeader[][] =
{
	{"Daftar Harga Supplier","SKU","Klik sku untuk kembali ke edit barang","Nama",
	 "Tidak ada data vendor price ..","Supplier","Kode","Unit","Harga Beli","Barcode",
	  "Discount Terakhir","Keterangan","PPN Terakhir","Harga Beli Terakhir",
	  "Tambah Harga Supplier","Harga Supplier","Kembali ke Daftar Barang","Mata Uang","Biaya Kirim","Diskon 1 Terakhir","Diskon 2 Terakhir","%"},
	{"Supplier Price List","SKU","Click sku for back to goods edit","Name",
	 "No data vendor price ..","Supplier","Code","Unit","Buying Price","Barcode",
	 "Last Discount","Description","Last Task","Last Buying Price",
	 "Add Supplier Price","Supplier Price","Back to Search Result","Currency","Cost Cargo", "Last Discount 1","Last Discount 2","%"}

};

	public String drawList(int iCommand,FrmMatVendorPrice frmObject, MatVendorPrice objEntity, Vector objectClass,  long materialId,  long vendorId, int start,int SESS_LANGUAGE)

	{
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");

		ctrlist.addHeader("No","3%","0","0");
		ctrlist.addHeader(textListTitleHeader[SESS_LANGUAGE][3],"20%","0","0");
		ctrlist.addHeader(textListTitleHeader[SESS_LANGUAGE][6],"10%","0","0");
		ctrlist.addHeader(textListTitleHeader[SESS_LANGUAGE][9],"10%","0","0");
                ctrlist.addHeader(textListTitleHeader[SESS_LANGUAGE][17],"10%","0","0");
                ctrlist.addHeader(textListTitleHeader[SESS_LANGUAGE][7],"5%","0","0");
		ctrlist.addHeader(textListTitleHeader[SESS_LANGUAGE][8],"10%","0","0");
                // add discount %
                ctrlist.addHeader(textListTitleHeader[SESS_LANGUAGE][19],"5%","0","0");
                ctrlist.addHeader(textListTitleHeader[SESS_LANGUAGE][20],"5%","0","0");
                //end of add discount %
		ctrlist.addHeader(textListTitleHeader[SESS_LANGUAGE][10],"5%","0","0");
                ctrlist.addHeader(textListTitleHeader[SESS_LANGUAGE][12],"5%","0","0");
		ctrlist.addHeader(textListTitleHeader[SESS_LANGUAGE][13],"10%","0","0");
		ctrlist.addHeader(textListTitleHeader[SESS_LANGUAGE][11],"20%","0","0");


		ctrlist.setLinkRow(0);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		Vector rowx = new Vector(1,1);
		ctrlist.reset();
		int index = -1;
		String whereCls = "";
		String orderCls = "";

		if(start < 0)
			start = 0;

		for (int i = 0; i < objectClass.size(); i++) {
			 MatVendorPrice matVendorPrice = (MatVendorPrice)objectClass.get(i);
			 rowx = new Vector();
			 start = start + 1;

            Unit unit = new Unit();
            try{
                unit = PstUnit.fetchExc(matVendorPrice.getBuyingUnitId());
            }catch(Exception e){}

            rowx.add(""+start+"");
            rowx.add("<a href=\"javascript:cmdEdit('"+String.valueOf(matVendorPrice.getOID())+"')\">"+PstMatVendorPrice.getVendorName(matVendorPrice.getVendorId())+"</a>");

            Vector vt = parserVendorCode(matVendorPrice.getVendorPriceCode());
            if(vt!=null&&vt.size()>1){
                Vector membergroupid_value = new Vector(1,1);
                Vector membergroupid_key = new Vector(1,1);
                for(int k=0;k<vt.size();k++){
                    membergroupid_key.add("0");
                    membergroupid_value.add(vt.get(k));
                }
                rowx.add(ControlCombo.draw("vdr_code",null, "0", membergroupid_key, membergroupid_value, "", "formElemen"));
            }else{
                rowx.add(matVendorPrice.getVendorPriceCode());
            }
            rowx.add(matVendorPrice.getVendorPriceBarcode());
            CurrencyType currencyType = new CurrencyType();
            try{
                currencyType = PstCurrencyType.fetchExc(matVendorPrice.getPriceCurrency());
            }catch(Exception e){}
            rowx.add(currencyType.getCode());
            rowx.add(unit.getCode());
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(matVendorPrice.getOrgBuyingPrice())+"</div>");
            // add discount %
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(matVendorPrice.getLastDiscount1())+"</div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(matVendorPrice.getLastDiscount2())+"</div>");
            //end of add discount %
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(matVendorPrice.getLastDiscount())+"</div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(matVendorPrice.getLastVat())+"</div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(matVendorPrice.getCurrBuyingPrice())+"</div>");
            rowx.add(matVendorPrice.getDescription());

			lstData.add(rowx);
		}
		return ctrlist.draw();
	}

%>
<%
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int prevCommand = FRMQueryString.requestInt(request, "prev_command");
int cmdMat = FRMQueryString.requestInt(request, "command_mat");
long materialId = FRMQueryString.requestLong(request, "hidden_material_id");
long vendorId = FRMQueryString.requestLong(request, "hidden_vendor_id");
String sourceLink = FRMQueryString.requestString(request, "source_link");
double transRate = FRMQueryString.requestDouble(request,"trans_rate");

/*variable declaration*/
int recordToGet = 5;
String msgString = "";
int iErrCode = FRMMessage.NONE;
String whereClause = "";
String orderClause = "";

Material material = null;
try{
	material = PstMaterial.fetchExc(materialId);
}catch(Exception e){}

CtrlMatVendorPrice ctrlMatVendorPrice = new CtrlMatVendorPrice(request);
ControlLine ctrLine = new ControlLine();
Vector listMatVendorPrice = new Vector(1,1);

    System.out.println("vendorId ; "+vendorId);
/*switch statement */
iErrCode = ctrlMatVendorPrice.action(iCommand , vendorId);
/* end switch*/
FrmMatVendorPrice frmMatVendorPrice = ctrlMatVendorPrice.getForm();
whereClause = ""+PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID]+"="+materialId;
/*count list All VdrMaterialPrice*/
int vectSize = PstMatVendorPrice.getCount(whereClause);
/*switch list VdrMaterialPrice*/
if((iCommand == Command.FIRST || iCommand == Command.PREV )||
  (iCommand == Command.NEXT || iCommand == Command.LAST)){
		start = ctrlMatVendorPrice.actionList(iCommand, start, vectSize, recordToGet);
 }
/* end switch list*/
MatVendorPrice matVendorPrice = ctrlMatVendorPrice.getMatVendorPrice();
msgString =  ctrlMatVendorPrice.getMessage();
/* get record to display */
listMatVendorPrice = PstMatVendorPrice.list(start,recordToGet, whereClause , orderClause);
/*handle condition if size of record to display = 0 and start > 0 	after delete*/
if (listMatVendorPrice.size() < 1 && start > 0)
{
	 if (vectSize - recordToGet > recordToGet)
			start = start - recordToGet;   //go to Command.PREV
	 else{
		 start = 0 ;
		 iCommand = Command.FIRST;
		 prevCommand = Command.FIRST; //go to Command.FIRST
	 }
	 listMatVendorPrice = PstMatVendorPrice.list(start,recordToGet, whereClause , orderClause);
}

%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
<!--
	//window.location="#go";

<%
  long mat_vendor = FRMQueryString.requestLong(request, "mat_vendor");
  long currency_id = FRMQueryString.requestLong(request, "currency_id");
  double trans_rate = FRMQueryString.requestDouble(request, "trans_rate");

  int includePpn = Integer.parseInt(PstSystemProperty.getValueByName("INCLUDE_PPN_MASUKAN"));


if(  sourceLink!=null && sourceLink.equals("materialdosearch.jsp")){ %>

function cmdCheck() {
var strvalue  = "<%=approot%>/warehouse/material/receive/materialdosearch.jsp?command=<%=Command.FIRST%>"+
                    "&mat_code="+
                     "&txt_materialname="+
                    "&mat_vendor="+document.frmmaterial.mat_vendor.value+
                    "&currency_id="+document.frmmaterial.currency_id.value+
                    "&trans_rate="+document.frmmaterial.trans_rate.value;
    window.location.href=strvalue;
}


function cmdSelectItemToReceive(matOid,matCode,matItem,matUnit,matPrice,matUnitId,matCurrencyId,matCurrCode){
    self.opener.document.forms.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_MATERIAL_ID]%>.value = matOid;
    self.opener.document.forms.frm_recmaterial.matCode.value = matCode;
    self.opener.document.forms.frm_recmaterial.matItem.value = matItem;
    self.opener.document.forms.frm_recmaterial.matUnit.value = matUnit;
    self.opener.document.forms.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>.value = matPrice;
    self.opener.document.forms.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID]%>.value = matUnitId;
    self.opener.document.forms.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_CURRENCY_ID]%>.value = matCurrencyId;
    //self.opener.document.forms.frm_recmaterial.matCurrency.value = matCurrCode;
    self.opener.changeFocus(self.opener.document.forms.frm_recmaterial.matCode); //document.forms.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>.focus();
    self.close();
}


<%}%>


function cmdAdd(){
	document.frmmaterial.hidden_vendor_id.value="0";
	document.frmmaterial.command.value="<%=Command.ADD%>";
	document.frmmaterial.prev_command.value="<%=prevCommand%>";
	document.frmmaterial.action="vdrmaterialprice.jsp";
	document.frmmaterial.submit();
}

function cmdAsk(materialId, vendorId){
	document.frmmaterial.hidden_material_id.value=materialId;
	document.frmmaterial.hidden_vendor_id.value=vendorId;
	document.frmmaterial.command.value="<%=Command.ASK%>";
	document.frmmaterial.prev_command.value="<%=prevCommand%>";
	document.frmmaterial.action="vdrmaterialprice.jsp";
	document.frmmaterial.submit();
}

function cmdConfirmDelete(materialId, vendorId){
	document.frmmaterial.hidden_material_id.value=materialId;
	document.frmmaterial.hidden_vendor_id.value=vendorId;
	document.frmmaterial.command.value="<%=Command.DELETE%>";
	document.frmmaterial.prev_command.value="<%=prevCommand%>";
	document.frmmaterial.action="vdrmaterialprice.jsp";
	document.frmmaterial.submit();
}

function cmdSave(){
	document.frmmaterial.command.value="<%=Command.SAVE%>";
	document.frmmaterial.prev_command.value="<%=prevCommand%>";
	document.frmmaterial.action="vdrmaterialprice.jsp";
	document.frmmaterial.submit();
}

function cmdEdit(vendorId){
	document.frmmaterial.hidden_vendor_id.value=vendorId;
	document.frmmaterial.command.value="<%=Command.EDIT%>";
	document.frmmaterial.prev_command.value="<%=prevCommand%>";
	document.frmmaterial.action="vdrmaterialprice.jsp";
	document.frmmaterial.submit();
}

function cmdCancel(materialId, vendorId){
	document.frmmaterial.hidden_material_id.value=materialId;
	document.frmmaterial.hidden_vendor_id.value=vendorId;
	document.frmmaterial.command.value="<%=Command.EDIT%>";
	document.frmmaterial.prev_command.value="<%=prevCommand%>";
	document.frmmaterial.action="vdrmaterialprice.jsp";
	document.frmmaterial.submit();
}

function cmdBack(){
	document.frmmaterial.command.value="<%=Command.BACK%>";
	document.frmmaterial.action="vdrmaterialprice.jsp";
	document.frmmaterial.submit();
}

function cmdBackList(){
	document.frmmaterial.command.value="<%=Command.BACK%>";
	document.frmmaterial.action="material_list.jsp";
	document.frmmaterial.submit();
}

function cmdListFirst(){
	document.frmmaterial.command.value="<%=Command.FIRST%>";
	document.frmmaterial.prev_command.value="<%=Command.FIRST%>";
	document.frmmaterial.action="vdrmaterialprice.jsp";
	document.frmmaterial.submit();
}

function editbarang(oid){
    document.frmmaterial.hidden_material_id.value = oid;
	document.frmmaterial.command.value="<%=Command.EDIT%>";
	document.frmmaterial.prev_command.value="<%=Command.FIRST%>";
	document.frmmaterial.action="material_main.jsp";
	document.frmmaterial.submit();
}

function cmdListPrev(){
	document.frmmaterial.command.value="<%=Command.PREV%>";
	document.frmmaterial.prev_command.value="<%=Command.PREV%>";
	document.frmmaterial.action="vdrmaterialprice.jsp";
	document.frmmaterial.submit();
}

function cmdListNext(){
	document.frmmaterial.command.value="<%=Command.NEXT%>";
	document.frmmaterial.prev_command.value="<%=Command.NEXT%>";
	document.frmmaterial.action="vdrmaterialprice.jsp";
	document.frmmaterial.submit();
}

function cmdListLast(){
	document.frmmaterial.command.value="<%=Command.LAST%>";
	document.frmmaterial.prev_command.value="<%=Command.LAST%>";
	document.frmmaterial.action="vdrmaterialprice.jsp";
	document.frmmaterial.submit();
}

function calculate(){
	var cost = cleanNumberFloat(document.frmmaterial.<%=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_ORG_BUYING_PRICE]%>.value,guiDigitGroup,guiDecimalSymbol);
	var lastDisc = cleanNumberFloat(document.frmmaterial.<%=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_LAST_DISCOUNT]%>.value,guiDigitGroup,guiDecimalSymbol);
	var lastVat = cleanNumberFloat(document.frmmaterial.<%=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_LAST_VAT]%>.value,guiDigitGroup,guiDecimalSymbol);
    var lastCostCargo = cleanNumberFloat(document.frmmaterial.<%=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_LAST_COST_CARGO]%>.value,".",",");


    if(isNaN(lastCostCargo) || lastCostCargo=='')
        lastCostCargo = 0.0;

    //alert(lastCostCargo);
    if(isNaN(cost) || cost=='')
		cost = 0.0;
	if(isNaN(lastDisc) || lastDisc=='')
		lastDisc = 0.0;
	if(isNaN(lastVat) || lastVat=='')
		lastVat = 0.0;

	var totaldiscount = parseFloat(cost) * parseFloat(lastDisc) / 100;
	var totalppn = (parseFloat(cost) - parseFloat(totaldiscount)) * parseFloat(lastVat) / 100;
	var totalCost = ((parseFloat(cost) - parseFloat(totaldiscount)) + parseFloat(totalppn)); // + parseFloat(lastCostCargo);

	//document.frmmaterial.<%=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_CURR_BUYING_PRICE]%>.value = totalCost;
	document.frmmaterial.<%=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_CURR_BUYING_PRICE]%>.value = formatFloat(totalCost, '', guiDigitGroup, guiDecimalSymbol, decPlace);
}

function calculate1(){
	var cost = cleanNumberFloat(document.frmmaterial.<%=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_ORG_BUYING_PRICE]%>.value,guiDigitGroup,guiDecimalSymbol);
	var lastDisc = cleanNumberFloat(document.frmmaterial.<%=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_LAST_DISCOUNT]%>.value,guiDigitGroup,guiDecimalSymbol);
	var lastVat = cleanNumberFloat(document.frmmaterial.<%=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_LAST_VAT]%>.value,guiDigitGroup,guiDecimalSymbol);
        var lastCostCargo = cleanNumberFloat(document.frmmaterial.<%=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_LAST_COST_CARGO]%>.value,".",",");
        var lastDisc1 = cleanNumberFloat(document.frmmaterial.<%=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_LAST_DISCOUNT_1]%>.value,guiDigitGroup,guiDecimalSymbol);
        var lastDisc2 = cleanNumberFloat(document.frmmaterial.<%=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_LAST_DISCOUNT_2]%>.value,guiDigitGroup,guiDecimalSymbol);

    if(isNaN(lastCostCargo) || lastCostCargo=='')
        lastCostCargo = 0.0;

   
    if(isNaN(cost) || cost=='')
		cost = 0.0;
	if(isNaN(lastDisc) || lastDisc=='')
		lastDisc = 0.0;
	if(isNaN(lastVat) || lastVat=='')
		lastVat = 0.0;
        if(isNaN(lastDisc1) || lastDisc1=='')
                lastDisc1 = 0.0;
        if(isNaN(lastDisc2) || lastDisc2=='')
                lastDisc2 = 0.0;

        var totalDisc1 = cost * lastDisc1/100;
        var totalMinus = cost - totalDisc1;
        var totalDisc2 = totalMinus * lastDisc2 /100;
        var totalDiscount = (totalMinus - totalDisc2) - lastDisc;
        var totalPpn = totalDiscount * lastVat/100;
        var totalCost = totalDiscount + totalPpn;
        var totalCostAll = totalCost + parseFloat(lastCostCargo);

	//document.frmmaterial.<%//=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_CURR_BUYING_PRICE]%>.value = totalCost;
	//document.frmmaterial.<%//=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_CURR_BUYING_PRICE]%>.value = formatFloat(totalDiscount, '', guiDigitGroup, guiDecimalSymbol, decPlace);
        //document.frmmaterial.<%//=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_CURR_BUYING_PRICE]%>.value = formatFloat(totalCost, '', guiDigitGroup, guiDecimalSymbol, decPlace);
         document.frmmaterial.<%=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_CURR_BUYING_PRICE]%>.value = formatFloat(totalCostAll, '', guiDigitGroup, guiDecimalSymbol, decPlace);
}

function calculateAll(){
	var cost = cleanNumberFloat(document.frmmaterial.<%=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_ORG_BUYING_PRICE]%>.value,guiDigitGroup,guiDecimalSymbol);
	var lastDisc = cleanNumberFloat(document.frmmaterial.<%=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_LAST_DISCOUNT]%>.value,guiDigitGroup,guiDecimalSymbol);
	var lastVat = cleanNumberFloat(document.frmmaterial.<%=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_LAST_VAT]%>.value,guiDigitGroup,guiDecimalSymbol);
        var lastCostCargo = cleanNumberFloat(document.frmmaterial.<%=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_LAST_COST_CARGO]%>.value,".",",");
        var lastDisc1 = cleanNumberFloat(document.frmmaterial.<%=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_LAST_DISCOUNT_1]%>.value,guiDigitGroup,guiDecimalSymbol);
        var lastDisc2 = cleanNumberFloat(document.frmmaterial.<%=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_LAST_DISCOUNT_2]%>.value,guiDigitGroup,guiDecimalSymbol);
         
    if(isNaN(lastCostCargo) || lastCostCargo=='')
        lastCostCargo = 0.0;

    //alert(lastCostCargo);
    if(isNaN(cost) || cost=='')
		cost = 0.0;
	if(isNaN(lastDisc) || lastDisc=='')
		lastDisc = 0.0;
	if(isNaN(lastVat) || lastVat=='')
		lastVat = 0.0;
        if(isNaN(lastDisc1) || lastDisc=='')
                lastDisc1 = 0.0;
        if(isNaN(lastDisc2) || lastDisc2=='')
                lastDisc2 = 0.0;
       
        //var totalDisc1 = parseFloat(cost) * parseFloat(lastDisc1)/100;
        var totalDisc1 = cost * lastDisc1/100;
        var totalMinus = cost - totalDisc1;
        var totalDisc2 = totalMinus * lastDisc2 /100;
        var totalDiscount = (totalMinus - totalDisc2) - lastDisc;
        //var totalCost = totalDiscount;
	//var totaldiscount = parseFloat(cost) * parseFloat(lastDisc) / 100;
	//var totalppn = (parseFloat(cost) - parseFloat(totaldiscount)) * parseFloat(lastVat) / 100;
	//var totalCost = ((parseFloat(cost) - parseFloat(totaldiscount)) + parseFloat(totalppn)); // + parseFloat(lastCostCargo);
        //var totalCost = (parseFloat(totalDiscount)+ parseFloat(lastCostCargo));
        //include Ppn
            var totalPpn = totalCost * lastPpn/100;
           
            var totalCostPpn = parseFloat(totalCost) + parseFloat(totalPpn);
            if(includePpn ==<%=PstMatReceive.INCLUDE_PPN%>){
                var totalCost = totalDiscount;
            }
            else if (includePpn ==<%=PstMatReceive.EXCLUDE_PPN%>){
                var totalCost = totalCostPpn;
            }

        //if(!(isNaN(lastCostCargo)) && (lastCostCargo != '0')) {
            //var totalCost = totalDiscount + lastCostCargo;
            var totalCostAll = parseFloat(totalCost)+ parseFloat(lastCostCargo);
            
            document.frmmaterial.<%=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_CURR_BUYING_PRICE]%>.value = formatFloat(totalCostAll, '', guiDigitGroup, guiDecimalSymbol, decPlace);
        //}
        //document.frmmaterial.<%//=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_CURR_BUYING_PRICE]%>.value = formatFloat(totalCost, '', guiDigitGroup, guiDecimalSymbol, decPlace);
       // document.frmmaterial.<%//=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_CURR_BUYING_PRICE]%>.value = totalCost;

	//document.frmmaterial.<%//=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_CURR_BUYING_PRICE]%>.value = totalCost;
	//document.frmmaterial.<%//=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_CURR_BUYING_PRICE]%>.value = formatFloat(totalCost, '', guiDigitGroup, guiDecimalSymbol, decPlace);
}

	function check(){
		//window.open("vendordosearch.jsp?command=<%=Command.FIRST%>&txt_vendorname="+document.frmmaterial.txt_vendorname.value+"&frm_id=0","materialvendor", "height=500,width=800,status=yes,top, scrollbars=yes,toolbar=no,menubar=no,location=no");
                window.open("vendordosearch.jsp?command=<%=Command.FIRST%>&txt_vendorname="+document.frmmaterial.txt_vendorname.value+"&frm_id=0","materialvendor", "height=500,width=800,status=yes,top, scrollbars=yes,toolbar=no,menubar=no,location=no");
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

function MM_swapImage() { //v3.0
		var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
		if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
	}
//-------------- script control line -------------------
//-->
</script>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<!-- #BeginEditable "styles" -->
<link rel="stylesheet" href="../../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" -->
<link rel="stylesheet" href="../../styles/tab.css" type="text/css">
<!-- #EndEditable -->
<%if(menuUsed == MENU_ICON){%>
    <link href="../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
<%}%>
<!-- #BeginEditable "headerscript" -->
<script language=JavaScript>

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

</script>

<!-- #EndEditable --> 
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
  <%if((sourceLink==null || !sourceLink.equals("materialdosearch.jsp"))){ %>
      <%if(menuUsed == MENU_PER_TRANS){%>
      <tr>
        <td height="25" ID="TOPTITLE"> <!-- #BeginEditable "header" -->
          <%@ include file = "../../main/header.jsp" %>
          <!-- #EndEditable --></td>
      </tr>
      <tr>
        <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
          <%@ include file = "../../main/mnmain.jsp" %>
          <!-- #EndEditable --> </td>
      </tr>
      <%}else{%>
       <tr bgcolor="#FFFFFF">
        <td height="10" ID="MAINMENU">
          <%@include file="../../styletemplate/template_header.jsp" %>
        </td>
      </tr>
      <%}%>
  <%}%>
  <tr> 
    <td valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">  
        <tr> 
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->Material
            &gt; <%=textListTitleHeader[SESS_LANGUAGE][0]%><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" -->
            <form name="frmmaterial" method ="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="command_mat" value="<%=cmdMat%>">
              <input type="hidden" name="vectSize" value="<%=vectSize%>">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="hidden_material_id" value="<%=materialId%>">
              <input type="hidden" name="hidden_vendor_id" value="<%=vendorId%>">
              <input type="hidden" name="<%=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_VENDOR_ID]%>" value="<%=matVendorPrice.getVendorId()%>">
              <input type="hidden" name="<%=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_MATERIAL_ID]%>" value="<%=materialId%>">
              <input type="hidden" name="source_link" value="<%=sourceLink%>">
              <input type="hidden" name="trans_rate" value="<%=transRate%>">
              <% if(  sourceLink!=null && sourceLink.equals("materialdosearch.jsp")){
                %>
                <input type="hidden" name="mat_vendor" value="<%=mat_vendor%>">
                <input type="hidden" name="currency_id" value="<%=currency_id%>">
              <% } %>


              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr align="left" valign="top">
                  <td height="14" valign="middle" colspan="3" class="listtitle"><hr size="1"></td>
                </tr>
                <%
							try{
							%>
                <tr align="left" valign="top">
                  <td height="22" valign="middle" colspan="3">
                    <table width="100%" border="0" cellspacing="1" cellpadding="1">
                      <tr>
                        <td width="6%"><%=textListTitleHeader[SESS_LANGUAGE][1]%></td>
                        <td width="94%">: <a href="javascript:editbarang('<%=materialId%>')"><%=material.getSku()%></a> (* <%=textListTitleHeader[SESS_LANGUAGE][2]%></td>
                      </tr>
                      <tr>
                        <td width="6%"><%=textListTitleHeader[SESS_LANGUAGE][3]%></td>
                        <td width="94%">: <%=material.getName()%></td>
                      </tr>
                    </table>
                  </td>
                </tr>
				<%
				if(listMatVendorPrice.size()==0){

				%>
                <tr align="left" valign="top">
                  <td colspan="3" valign="middle" class="msginfo">&nbsp;<%=textListTitleHeader[SESS_LANGUAGE][4]%></td>
                </tr>
				<%}else{%>
                <tr align="left" valign="top">
                  <td valign="middle" colspan="3"> <%= drawList(iCommand,frmMatVendorPrice, matVendorPrice,listMatVendorPrice,materialId,vendorId,start,SESS_LANGUAGE)%> </td>
                </tr>
                <%
						  }
						  }catch(Exception exc){
						  }%>
                <tr align="left" valign="top">
                  <td height="8" colspan="3" align="left" valign="top" class="command">
                    <span class="command">
                    <%
								   int cmd = 0;
									   if ((iCommand == Command.FIRST || iCommand == Command.PREV )||
										(iCommand == Command.NEXT || iCommand == Command.LAST))
											cmd =iCommand;
								   else{
									  if(iCommand == Command.NONE || prevCommand == Command.NONE)
										cmd = Command.FIRST;
									  else
									  	cmd = prevCommand;
								   }
							    %>
                    <% ctrLine.setLocationImg(approot+"/images");
							   	ctrLine.initDefault();
								 %>
                    <%=ctrLine.drawImageListLimit(cmd,vectSize,start,recordToGet)%> </span> </td>
                </tr>
                <%if(iCommand==Command.ADD || iCommand==Command.EDIT || iCommand==Command.ASK || ((iCommand==Command.SAVE || iCommand==Command.DELETE)&& iErrCode!=FRMMessage.NONE)){%>
                <tr align="left" valign="top">
                  <td height="22" colspan="3">
                    <table width="100%" border="0" cellspacing="1" cellpadding="1">
                      <tr>
                        <td width="9%">&nbsp;</td>
                        <td width="1%">&nbsp;</td>
                        <td width="37%">&nbsp;</td>
                        <td width="3%">&nbsp;</td>
                        <td width="12%">&nbsp;</td>
                        <td width="1%">&nbsp;</td>
                        <td width="37%">&nbsp;</td>
                      </tr>
                      <tr>
                        <td width="9%"> <div align="left"><%=textListTitleHeader[SESS_LANGUAGE][5]%></div></td>
                        <td width="1%">:</td>
                        <td width="37%"> <input type="text" size="30" readonly name="txt_vendorname" value="<%=PstMatVendorPrice.getVendorName(matVendorPrice.getVendorId())%>" class="formElemen">
                          <a href="javascript:check()">CHK</a></td>
                        <td width="3%">&nbsp;</td>
                        <td width="12%"> <div align="left"><%=textListTitleHeader[SESS_LANGUAGE][17]%></div></td>
                        <td width="1%">:</td>
                        <td width="37%"> <%
                            String whereCurr  = PstCurrencyType.fieldNames[PstCurrencyType.FLD_INCLUDE_IN_PROCESS]+"="+PstCurrencyType.INCLUDE;
                            Vector listCurr = PstCurrencyType.list(0,0,whereCurr,"");
                            Vector vectCurrVal = new Vector(1,1);
                            Vector vectCurrKey = new Vector(1,1);
                            for(int i=0; i<listCurr.size(); i++){
                                CurrencyType currencyType = (CurrencyType)listCurr.get(i);
                                vectCurrKey.add(currencyType.getCode());
                                vectCurrVal.add(""+currencyType.getOID());
                            }
                          %> <%=ControlCombo.draw(FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_PRICE_CURRENCY],"formElemen", null, ""+matVendorPrice.getPriceCurrency(), vectCurrVal, vectCurrKey, "")%> </td>
                      </tr>
                      <tr>
                        <td width="9%"><%=textListTitleHeader[SESS_LANGUAGE][6]%></td>
                        <td width="1%">:</td>
                        <td width="37%"> <input tabindex="1" type="text" size="20" name="<%=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_VENDOR_PRICE_CODE]%>" value="<%=matVendorPrice.getVendorPriceCode()%>" class="formElemen">                        </td>
                        <td width="3%">&nbsp;</td>
                        <td width="12%"><div align="left"><%=textListTitleHeader[SESS_LANGUAGE][7]%></div></td>
                        <td>:</td>
                        <td> <%
                            Unit firstUnit = new Unit();
                            try{
                                firstUnit = PstUnit.fetchExc(material.getDefaultStockUnitId());
                            }catch(Exception e){}

                            String whereBaseUnitOrder = "PMU."+PstMaterialUnitOrder.fieldNames[PstMaterialUnitOrder.FLD_MATERIAL_ID] + " ='"+materialId+"'";
                            Vector listUnit = PstMaterialUnitOrder.listJoin(0,0,whereBaseUnitOrder,"");
                            
                            Vector vectUnitVal = new Vector(1,1);
                            Vector vectUnitKey = new Vector(1,1);

                            vectUnitKey.add(firstUnit.getCode());
                            vectUnitVal.add(""+firstUnit.getOID());
                            
                            for(int i=0; i<listUnit.size(); i++)
                            {
                                MaterialUnitOrder materialUnitOrder = (MaterialUnitOrder)listUnit.get(i);
                                vectUnitKey.add(materialUnitOrder.getUnitKode());
                                vectUnitVal.add(""+materialUnitOrder.getUnitID());
                            }
                          %> <%=ControlCombo.draw(FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_BUYING_UNIT_ID],"formElemen", null, ""+matVendorPrice.getBuyingUnitId(), vectUnitVal, vectUnitKey, " tabindex=\"4\"")%></td>
                      </tr>
                      <tr>

                        <td width="9%"> <div align="left"><%=textListTitleHeader[SESS_LANGUAGE][9]%></div></td>
                        <td width="1%">:</td>
                        <td width="37%"> <input tabindex="2" type="text" size="20" name="<%=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_VENDOR_PRICE_BARCODE]%>" value="<%=matVendorPrice.getVendorPriceBarcode()%>" class="formElemen">                        </td>
                        <td width="3%">&nbsp;</td>
                        <td width="12%"> <%=textListTitleHeader[SESS_LANGUAGE][8]%> </td>
                        <td>:</td>
                        <td> <input tabindex="5" type="text" size="15"  name="<%=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_ORG_BUYING_PRICE]%>" value="<%=FRMHandler.userFormatStringDecimal(matVendorPrice.getOrgBuyingPrice())%>" class="formElemen" onKeyUp="javascript:calculate1()" style="text-align:right">                        </td>
                      </tr>
                      <tr>
                        <td>&nbsp;</td>
                        <td valign="top">&nbsp;</td>
                        <td width="37%" rowspan="5" valign="top"> <textarea tabindex="3" cols="30" name="<%=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_DESCRIPTION]%>" class="formElemen" rows="3"><%=matVendorPrice.getDescription()%></textarea>                        </td>
                        <td>&nbsp;</td>
                        <td><%=textListTitleHeader[SESS_LANGUAGE][18]%></td>
                        <td>:</td>
                        <td><input tabindex="6" type="text" size="15"  name="<%=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_LAST_COST_CARGO]%>" value="<%=FRMHandler.userFormatStringDecimal(matVendorPrice.getLastCostCargo())%>" class="formElemen" onKeyUp="javascript:calculate1()" style="text-align:right"></td>
                      </tr>
                      <tr>
                        <td width="9%"> <div align="left"><%=textListTitleHeader[SESS_LANGUAGE][11]%></div></td>
                        <td width="1%" valign="top">:</td>
                        <td width="3%">&nbsp;</td>
                        <td width="12%"> <div align="left"><%=textListTitleHeader[SESS_LANGUAGE][19]%></div></td>
                        <td>:</td>

                        <td> <input tabindex="6" type="text" size="7"  name="<%=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_LAST_DISCOUNT_1]%>" value="<%=FRMHandler.userFormatStringDecimal(matVendorPrice.getLastDiscount1())%>" class="formElemen" onKeyUp="javascript:calculate1()" style="text-align:right">
                          % </td>
                      </tr>
                      <tr>
                        <td width="9%"> <div align="left"></div></td>
                        <td width="1%" valign="top">&nbsp;</td>
                        <td width="3%">&nbsp;</td>
                        <td width="12%"><div align="left"><%=textListTitleHeader[SESS_LANGUAGE][20]%></div></td>
                        <td>:</td>
                        <td> <input tabindex="7" type="text" size="7"  name="<%=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_LAST_DISCOUNT_2]%>" value="<%=FRMHandler.userFormatStringDecimal(matVendorPrice.getLastDiscount2())%>" class="formElemen" onKeyUp="javascript:calculate1()" style="text-align:right">
                          % </td>
                      </tr>
                      <tr>
                        <td>&nbsp;</td>
                        <td valign="top">&nbsp;</td>
                        <td>&nbsp;</td>
                        <td><div align="left"><%=textListTitleHeader[SESS_LANGUAGE][10]%></div></td>
                        <td>:</td>
                        <td> <input tabindex="8" type="text" size="15"  name="<%=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_LAST_DISCOUNT]%>" value="<%=FRMHandler.userFormatStringDecimal(matVendorPrice.getLastDiscount())%>" class="formElemen" onKeyUp="javascript:calculate1()" style="text-align:right"></td>
                      </tr>
                      <tr>
                        <td>&nbsp;</td>
                        <td valign="top">&nbsp;</td>
                        <td>&nbsp;</td>
                        <td><div align="left"><%=textListTitleHeader[SESS_LANGUAGE][12]%></div></td>
                        <td>:</td>
                        <td><input tabindex="8" type="text" size="15"  name="<%=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_LAST_VAT]%>" value="<%=FRMHandler.userFormatStringDecimal(matVendorPrice.getLastVat())%>" class="formElemen" onKeyUp="javascript:calculate1()" style="text-align:right"></td>
                      </tr>
					  <tr>
                        <td>&nbsp;</td>
                        <td valign="top">&nbsp;</td>
                        <td>&nbsp;</td>
                        <td></td>
                        <td><%=textListTitleHeader[SESS_LANGUAGE][13]%></td>
                        <td>:</td>
						<td><input tabindex="8" type="text" size="15"  name="<%=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_CURR_BUYING_PRICE]%>" value="<%=FRMHandler.userFormatStringDecimal(matVendorPrice.getCurrBuyingPrice())%>" class="formElemen" style="text-align:right">
                                                (* automatic <a tabindex="9" href="javascript:calculate1()">calculate</a>
                          )</td>
                      </tr>
					  <tr>
                        <td>&nbsp;</td>
                        <td valign="top">&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                      </tr>
                    </table>
                  </td>
                </tr>
                <%}%>
                <tr align="left" valign="top"> 
                  <td height="22" valign="middle" colspan="3"> <%if((iCommand==Command.BACK || iCommand==Command.NONE ||
									iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LIST || iCommand==Command.LAST || iCommand==Command.SAVE || iCommand ==Command.DELETE) && frmMatVendorPrice.errorSize()==0){%> 
                    <table width="49%" border="0" cellspacing="2" cellpadding="0">
                      <tr> 
                        <%if(privAdd){%>
                        <td width="7%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="New Vendor Price"></a></td>
                        <td nowrap width="93%"><a href="javascript:cmdAdd()" class="command"><%=textListTitleHeader[SESS_LANGUAGE][14]%></a></td>
                        <%}%>
                      </tr>
                    </table>
                    <%}%> </td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="22" valign="middle" colspan="3"> <%
									ctrLine.setLocationImg(approot+"/images");
									
									// set image alternative caption
									ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,textListTitleHeader[SESS_LANGUAGE][15],ctrLine.CMD_SAVE,true));
									ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,textListTitleHeader[SESS_LANGUAGE][15],ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,textListTitleHeader[SESS_LANGUAGE][15],ctrLine.CMD_BACK,true)+" List");
									ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,textListTitleHeader[SESS_LANGUAGE][15],ctrLine.CMD_ASK,true));
									ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,textListTitleHeader[SESS_LANGUAGE][15],ctrLine.CMD_CANCEL,false));
									
									ctrLine.initDefault();
									ctrLine.setTableWidth("80%");
									String scomDel = "javascript:cmdAsk('"+materialId+"','"+vendorId+"')";
									String sconDelCom = "javascript:cmdConfirmDelete('"+materialId+"','"+vendorId+"')";
									String scancel = "javascript:cmdEdit('"+materialId+"','"+vendorId+"')";
									
									// set command caption
									ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE,textListTitleHeader[SESS_LANGUAGE][15],ctrLine.CMD_SAVE,true));
									ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,textListTitleHeader[SESS_LANGUAGE][15],ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,textListTitleHeader[SESS_LANGUAGE][15],ctrLine.CMD_BACK,true)+" List");
									ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE,textListTitleHeader[SESS_LANGUAGE][15],ctrLine.CMD_ASK,true));
									ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE,textListTitleHeader[SESS_LANGUAGE][15],ctrLine.CMD_DELETE,true));
									ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE,textListTitleHeader[SESS_LANGUAGE][15],ctrLine.CMD_CANCEL,false));
									
									//ctrLine.setSaveCaption("Simpan");
									//ctrLine.setDeleteCaption("Hapus");
									//ctrLine.setConfirmDelCaption("Ya, Hapus");
									//ctrLine.setBackCaption("Batal");

                                           String cmdSelectToLgr ="";
                                          if(  sourceLink!=null && sourceLink.equals("materialdosearch.jsp")){
                                             if(material==null){
                                                  material= new Material();
                                                 }
                                            //ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,materialTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,materialTitle,ctrLine.CMD_BACK,true)+" List");

                                            ctrLine.setBackCaption("Back To Search");
                                            ctrLine.setBackCommand("javascript:cmdCheck()");


                                            String name = "";
                                            name = material.getName();
                                            name = name.replace('\"','`');
                                            name = name.replace('\'','`');
                                            Unit unit = new Unit();
                                            try{unit = PstUnit.fetchExc(material.getDefaultStockUnitId());} catch(Exception e2){}

                                            Hashtable objActiveStandardRate = PstStandartRate.getActiveStandardRate();
                                                        double standardRate = 0;
                                            try{standardRate = Double.parseDouble((String)objActiveStandardRate.get(""+material.getDefaultCostCurrencyId()));}catch(Exception exc){;}
                                            double defaultCost = 0; //mendapatkan besaran default cost dalam mata uang transaksi
                                            try{defaultCost = (material.getDefaultCost() * standardRate) / transRate;}catch(Exception exc){;}

                                            MatCurrency matCurr = new MatCurrency();
                                            try{PstMatCurrency.fetchExc(material.getDefaultPriceCurrencyId());} catch(Exception e1){}

                                            cmdSelectToLgr ="javascript:cmdSelectItemToReceive('"+material.getOID()+"','"+material.getSku()+"','"+name+"','"+unit.getCode()+
                                            "','"+(defaultCost >0.00 ? ""+FRMHandler.userFormatStringDecimal(defaultCost) : "")+"','"+unit.getOID()+
                                            "','"+material.getDefaultCostCurrencyId()+"','"+matCurr.getCode()+"')";

                                          }

									ctrLine.setCommandStyle("command");
									ctrLine.setColCommStyle("command");

									if (privDelete){
										ctrLine.setConfirmDelCommand(sconDelCom);
										ctrLine.setDeleteCommand(scomDel);
										ctrLine.setEditCommand(scancel);
									}else{
										ctrLine.setConfirmDelCaption("");
										ctrLine.setDeleteCaption("");
										ctrLine.setEditCaption("");
									}

									if(privAdd == false  && privUpdate == false){
										ctrLine.setSaveCaption("");
									}

									if (privAdd == false){
										ctrLine.setAddCaption("");
									}
									%> <%if(iCommand==Command.ADD || iCommand==Command.EDIT || iCommand==Command.ASK || ((iCommand==Command.SAVE || iCommand==Command.DELETE)&& iErrCode!=FRMMessage.NONE)){%> 
									<%=ctrLine.drawImage(iCommand, iErrCode, msgString)%> <%}%>
                                          <%
                                            if(material.getOID()!=0 && cmdSelectToLgr!=null && cmdSelectToLgr.length()>0){
                                          %>
                                          <a href="<%=cmdSelectToLgr%>" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image1003','','<%=approot%>/images/BtnNewOn.jpg',1)"><img src="<%=approot%>/images/BtnNew.jpg" alt="Select to Receive%>" name="Image1003" width="24" height="24" border="0" id="Image1003">Select to Receive</a>
                                          <%} else {%>


                    <!--td valign="top" width="20%"-->
                    <table border="0" cellpadding="0" cellspacing="0"><tr>
                    <td valign="top"><a href="javascript:cmdBackList()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image33','','<%=approot%>/images/BtnBackOn.jpg',1)"><img name="Image33" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=textListTitleHeader[SESS_LANGUAGE][16]%> "></a></td>
                    <td valign="top" nowrap class="command">&nbsp; <a href="javascript:cmdBackList()" class="command"><%=textListTitleHeader[SESS_LANGUAGE][16]%> </a></td>
                  </tr></table>
                  <!--/td-->
                  <%}%>
                  </td>
                </tr>
                <tr>
                
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
            <%@include file="../../styletemplate/footer.jsp" %>

        <%}else{%>
            <%@ include file = "../../main/footer.jsp" %>
        <%}%>
      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>

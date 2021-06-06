<%@ page language = "java" %>

<!-- package java -->
<%@ page import = "java.util.*
                   ,
                   com.dimata.posbo.entity.admin.PstAppUser,
                   com.dimata.posbo.session.masterdata.SessMaterial,
                   com.dimata.posbo.entity.search.SrcMaterial,
                   com.dimata.posbo.form.masterdata.FrmMaterial,
                   com.dimata.posbo.form.masterdata.CtrlMaterial,
                   com.dimata.posbo.form.masterdata.CtrlMaterialComposit,
                   com.dimata.common.entity.payment.*,
                   com.dimata.posbo.form.masterdata.FrmMatVendorPrice,
                   com.dimata.posbo.entity.admin.AppObjInfo" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package material -->
<%@ page import = "com.dimata.common.entity.contact.*" %>
<%@ page import = "com.dimata.common.entity.location.*" %>
<%@ page import = "com.dimata.posbo.entity.masterdata.*" %>

<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_CATALOG); %>
<%@ include file = "../../main/checkuser.jsp" %>
<%
    // ini privilege pelengkap
    // ini privilege untuk harga selling price
    //appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTER_D, AppObjInfo.G2_MASTER_D_MATERIAL, AppObjInfo.OBJ_MASTER_D_VIEW_SELL_PRICE);
    boolean privEditPrice = true;//userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_VIEW));

    // ini privilege untuk harga supplier
    //appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTER_D, AppObjInfo.G2_MASTER_D_MATERIAL, AppObjInfo.OBJ_MASTER_D_VIEW_SUPP_PRICE);
    boolean privEditSuppPrice = true;//userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_VIEW));
%>

<!-- Jsp Block -->
<%!
public static String formatNumberList = "#,###.00";
public static String formatNumberEdit = "##.###";

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] =
{
	{"No","Kode","Barcode","Nama Barang","Merk","Kategori","Tipe","Unit Jual","Harga Jual",//8
	 "Mata Uang Jual","Harga Beli","Mata Uang Beli","Tipe Supplier", "Supplier", "Tipe Harga 1",//14
	 "Tipe Harga 2", "Tipe Harga 3","Group","Discount Terakhir","PPN Terakhir","Harga Beli Terakhir","Tanggal Kadaluarsa","Profit",//22
	 "Poin Minimum","Nomor Serial","HPP"},//23-25
	{"No","Code","Barcode","Name","Merk","Category","Type","Sell Unit","Default Price",
	 "Default Price Currency","Default Cost","Default Cost Currency","Default Supplier Type", "Supplier", "Price Type 1", "Price Type 2",
	 "Price Type 3","Group","Last Discount","Last Vat","Last Buying Price","Expired Date","Profit",
	 "Minimum Point","Serial Number","Average Price"}
};

public static final String textListTitleHeader[][] =
{
	{"Editor Barang","Barang","Pabrik","Set Harga Supplier","Panjang kode lebih dari 12 huruf!!","Tidak ada data group .."},
	{"Goods Editor","Goods","Factory","Supplier Price Setting","Code`s length more than 12 character!!","No group data available .."}
};

/* this constant used to list text of listHeader */
public static final String textListMaterialCompositHeader[][] =
{
	{"No","SKU","Nama","Unit","Qty"},
	{"No","Code","Name","Unit","Qty"}
};
%>

<%
/* get data from form request */
int iCommand = FRMQueryString.requestCommand(request);
int startMat = FRMQueryString.requestInt(request, "start_mat");
int prevCommand = FRMQueryString.requestInt(request, "prev_command");
long oidMaterial = FRMQueryString.requestLong(request, "hidden_material_id");
long oidVendor = FRMQueryString.requestLong(request, "hidden_vendor_id");
int catType = FRMQueryString.requestInt(request, "hidden_catType");
String materialTitle = textListTitleHeader[SESS_LANGUAGE][1];
/* variable declaration */
boolean editWithoutList = false;
int recordToGet = 20;
int iErrCode = FRMMessage.NONE;
String whereClause = "";
String orderClause = PstMaterial.fieldNames[PstMaterial.FLD_SKU];

/* for manupalate price type */
Vector listCurrStandard = PstStandartRate.listCurrStandard();
String ordPrice = PstPriceType.fieldNames[PstPriceType.FLD_CODE]+", "+PstPriceType.fieldNames[PstPriceType.FLD_NAME];
Vector listPriceType = PstPriceType.list(0,0,"",ordPrice);

Vector vectData = new Vector();
if(iCommand==Command.SAVE){
    if(listPriceType!=null&&listPriceType.size()>0){
        for(int i=0;i<listPriceType.size();i++){
            PriceType priceType = (PriceType)listPriceType.get(i);
            Vector vect = new Vector(1,1);
            for(int j=0;j<listCurrStandard.size();j++){
                Vector vect2 = new Vector(1,1);
                Vector temp = (Vector)listCurrStandard.get(j);
                StandartRate standart = (StandartRate)temp.get(1);
                String valPrice = request.getParameter("price_"+i+""+j);
                System.out.println("valPrice :"+valPrice);
                valPrice = FRMHandler.deFormatStringDecimal(valPrice);
                System.out.println("valPrice :"+valPrice);
                if(!valPrice.equals("0")){
                   vect2.add(""+priceType.getOID());
                   vect2.add(valPrice);
                   vect2.add(""+standart.getOID());
                   vect.add(vect2);
                }
            }
            vectData.add(vect);
        }
    }
}
/* ControlLine */
ControlLine ctrLine = new ControlLine();
CtrlMaterial ctrlMaterial = new CtrlMaterial(request);
FrmMaterial frmMaterial = ctrlMaterial.getForm();

    Unit unit = new Unit();
    Vector listUnit = PstUnit.list(0,0,"","");
    if(listUnit!=null && listUnit.size()>0){
        unit = (Unit)listUnit.get(0);
    }

    //Merk merk = new Merk();
    //Vector listMerk = PstMerk.list(0,0,"","");
    //if(listMerk!=null && listMerk.size()>0){
    //    merk = (Merk)listMerk.get(0);
    //}


Material material = new Material();
iErrCode = ctrlMaterial.action(iCommand,oidMaterial,vectData,new Vector(), new Vector());
frmMaterial = ctrlMaterial.getForm();
String msgString = ctrlMaterial.getMessage();
material = ctrlMaterial.getMaterial();
oidMaterial = material.getOID();
Vector listPriceMapping = ctrlMaterial.getListPriceMapping();

boolean isDelete = false; //untuk menampung, apakah proses sebelumnya adalah proses DELETE
//Jika DELETE, switch to NONE for listing object
if (iCommand == Command.DELETE) {
	if(frmMaterial.errorSize()==0){
        iCommand=Command.ADD;
		isDelete = true;
	}
}
    if(iCommand==Command.ADD){
        material.setBuyUnitId(unit.getOID());
        material.setDefaultStockUnitId(unit.getOID());
       // material.setMerkId(merk.getOID());
    }

    // get Vendor Price
    Vector listVdr = PstMatVendorPrice.list(0,0,PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID]+"="+oidMaterial,"");
        ContactList contactList = new ContactList();
        MatVendorPrice matVendorPrice = new MatVendorPrice();
    if(listVdr!=null && listVdr.size()>0){
        matVendorPrice = (MatVendorPrice)listVdr.get(0);
        try{
            contactList = PstContactList.fetchExc(matVendorPrice.getVendorId());
        }catch(Exception e){}

        // update vendor price
        if(iCommand==Command.SAVE){
            if(oidMaterial!=0){
                //matVendorPrice = new MatVendorPrice();
                FrmMatVendorPrice frmMatVendorPrice = new FrmMatVendorPrice(request,matVendorPrice);
                matVendorPrice = frmMatVendorPrice.getRequestForInsert(oidMaterial);
                matVendorPrice.setOID(matVendorPrice.getOID());
                long oidvdr = PstMatVendorPrice.updateExc(matVendorPrice);
            }
        }
    }else{
        if(iCommand==Command.SAVE){
            matVendorPrice = new MatVendorPrice();
            FrmMatVendorPrice frmMatVendorPrice = new FrmMatVendorPrice(request,matVendorPrice);
            matVendorPrice = frmMatVendorPrice.getRequestForInsert(oidMaterial);
            try{
                long oidvdr = PstMatVendorPrice.insertExc(matVendorPrice);
            }catch(Exception e){}
            listVdr = PstMatVendorPrice.list(0,0,PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID]+"="+oidMaterial,"");
            matVendorPrice = (MatVendorPrice)listVdr.get(0);
            try{
                contactList = PstContactList.fetchExc(matVendorPrice.getVendorId());
            }catch(Exception e){}
        }
    }
    material.setProses(Material.IS_PROCESS_INSERT_UPDATE);
    String goodName = material.getName();
    if(material.getMaterialSwitchType()==Material.WITH_USE_SWITCH_MERGE_AUTOMATIC){
      try{
        goodName = goodName.substring(goodName.lastIndexOf(Material.getSeparate())+1,goodName.length());
      }catch(Exception e){}
    }
	
	/** agar informasi catalog yang di-DELETE tidak tampil lagi pada user interface */
	if(isDelete) {
		material = new Material();
		goodName = "";
	}
%>
<html>
<head>
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
<!--
/*------------- start main function ----------------------*/
function cmdSelectVendor(){
	window.open("vendordosearch.jsp?command=<%=Command.FIRST%>&txt_vendorname="+document.frmmaterial.txt_vendorname.value+"&frm_id=0","material", "height=600,width=800,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes");
}

function cmdSelectSubCategory(){
	window.open("subcategorydosearch.jsp?command=<%=Command.FIRST%>&txt_subcategory="+document.frmmaterial.txt_subcategory.value+
			"&oidCategory="+document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_CATEGORY_ID]%>.value,"material", "height=600,width=800,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes");
}

function cmdKeyUpPriceType(objForm){
    var name = objForm.name;

	<%
    if(listPriceType!=null && listPriceType.size()>0){
		int maxPriceType = listPriceType.size();
		for(int i=0; i<maxPriceType; i++){
	%>

	if(objForm == document.frmmaterial.price_<%=i%>0){
		var price = cleanNumberFloat(objForm.value, guiDigitGroup, guiDecimalSymbol);

		<%
		if(listCurrStandard!=null && listCurrStandard.size()>0){
			int maxCurrStandard = listCurrStandard.size();
			for(int j=1; j<maxCurrStandard; j++){
				Vector temp = (Vector)listCurrStandard.get(j);
				StandartRate sRate = (StandartRate)temp.get(1);
		%>

		var price_rate = price / <%=sRate.getSellingRate()%>;
		if((isNaN(price_rate)) || (price_rate=="")){
			price_rate = "0";
		}
		document.frmmaterial.price_<%=i%><%=j%>.value = formatFloat(price_rate, '', guiDigitGroup, guiDecimalSymbol, decPlace);

		<%
			}
		}
		%>

	}

	<%
		}
	}
	%>
    cmdhitung(objForm);
}

function cmdAdd(){
	document.frmmaterial.hidden_material_id.value="0";
	document.frmmaterial.command.value="<%=Command.ADD%>";
	document.frmmaterial.prev_command.value="<%=prevCommand%>";
	document.frmmaterial.action="simple_mat_main.jsp";
	document.frmmaterial.submit();
}

function cmdAsk(oidMaterial){
	document.frmmaterial.hidden_material_id.value=oidMaterial;
	document.frmmaterial.command.value="<%=Command.ASK%>";
	document.frmmaterial.prev_command.value="<%=prevCommand%>";
	document.frmmaterial.action="simple_mat_main.jsp";
	document.frmmaterial.submit();
}

function cmdConfirmDelete(oidMaterial){
	document.frmmaterial.hidden_material_id.value=oidMaterial;
	document.frmmaterial.command.value="<%=Command.DELETE%>";
	document.frmmaterial.prev_command.value="<%=prevCommand%>";
	document.frmmaterial.action="simple_mat_main.jsp";
	document.frmmaterial.submit();
}

function cmdSave(){
	//Cek panjang SKU
	var my_sku = document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_SKU]%>.value;
	var panjang_sku = my_sku.length;
	if (panjang_sku <= 12){
		document.frmmaterial.command.value="<%=Command.SAVE%>";
		document.frmmaterial.prev_command.value="<%=prevCommand%>";
		document.frmmaterial.action="simple_mat_main.jsp";
		document.frmmaterial.submit();
    }else{
		alert('<%=textListTitleHeader[SESS_LANGUAGE][4]%>');
	}
}

function cmdhitung(objForm){
    var koe = document.frmmaterial.txtkoe.value;
    var retail = document.frmmaterial.price_00.value;
    var wholesale = document.frmmaterial.price_10.value;

    if(koe=='')
        koe = formatFloat(1, '', guiDigitGroup, guiDecimalSymbol, decPlace);

    if(wholesale=='')
        wholesale = formatFloat(1, '', guiDigitGroup, guiDecimalSymbol, decPlace);

    if(retail=='')
        retail = formatFloat(1, '', guiDigitGroup, guiDecimalSymbol, decPlace);

	koe = cleanNumberFloat(koe, guiDigitGroup, guiDecimalSymbol);
    wholesale = cleanNumberFloat(wholesale, guiDigitGroup, guiDecimalSymbol);
    retail = cleanNumberFloat(retail, guiDigitGroup, guiDecimalSymbol);

    if(objForm.name == 'price_00'){
        document.frmmaterial.txtkoe.value = formatFloat(retail / wholesale, '', guiDigitGroup, guiDecimalSymbol, decPlace);
    }
    if(objForm.name == 'price_10'){
        document.frmmaterial.txtkoe.value = formatFloat(retail / wholesale, '', guiDigitGroup, guiDecimalSymbol, decPlace);
    }

    document.frmmaterial.price_001.value = formatFloat(cleanNumberFloat(document.frmmaterial.price_00.value, guiDigitGroup, guiDecimalSymbol), '', guiDigitGroup, guiDecimalSymbol, decPlace);
	document.frmmaterial.price_101.value = formatFloat(cleanNumberFloat(document.frmmaterial.price_10.value, guiDigitGroup, guiDecimalSymbol), '', guiDigitGroup, guiDecimalSymbol, decPlace);
	document.frmmaterial.txtkoe_1.value = formatFloat(cleanNumberFloat(document.frmmaterial.txtkoe.value, guiDigitGroup, guiDecimalSymbol), '', guiDigitGroup, guiDecimalSymbol, decPlace);
}

function cmdhitung1(objForm){
    var koe = document.frmmaterial.txtkoe_1.value;
    var retail = document.frmmaterial.price_001.value;
    var wholesale = document.frmmaterial.price_101.value;

    if(koe=='')
        koe = formatFloat(1, '', guiDigitGroup, guiDecimalSymbol, decPlace);

    if(wholesale=='')
        wholesale = formatFloat(1, '', guiDigitGroup, guiDecimalSymbol, decPlace);

    if(retail=='')
        retail = formatFloat(1, '', guiDigitGroup, guiDecimalSymbol, decPlace);

    koe = cleanNumberFloat(koe, guiDigitGroup, guiDecimalSymbol);
    wholesale = cleanNumberFloat(wholesale, guiDigitGroup, guiDecimalSymbol);
    retail = cleanNumberFloat(retail, guiDigitGroup, guiDecimalSymbol);
    if(objForm.name == 'price_001'){
        document.frmmaterial.price_101.value = formatFloat(retail / koe, '', guiDigitGroup, guiDecimalSymbol, decPlace);
    }
    if(objForm.name == 'txtkoe_1'){
        document.frmmaterial.price_101.value = formatFloat(retail / koe, '', guiDigitGroup, guiDecimalSymbol, decPlace);
    }

    document.frmmaterial.price_00.value = formatFloat(cleanNumberFloat(document.frmmaterial.price_001.value, guiDigitGroup, guiDecimalSymbol), '', guiDigitGroup, guiDecimalSymbol, decPlace);
	document.frmmaterial.price_10.value = formatFloat(cleanNumberFloat(document.frmmaterial.price_101.value, guiDigitGroup, guiDecimalSymbol), '', guiDigitGroup, guiDecimalSymbol, decPlace);
	document.frmmaterial.txtkoe.value = formatFloat(cleanNumberFloat(document.frmmaterial.txtkoe_1.value, guiDigitGroup, guiDecimalSymbol), '', guiDigitGroup, guiDecimalSymbol, decPlace);
}

<%
    if((iCommand==Command.SAVE) || (iCommand==Command.ADD)){
%>
    parent.frames[1].cmdedit('<%=oidMaterial%>');
<%}%>
function cmdEdit(oidMaterial){
	document.frmmaterial.hidden_material_id.value=oidMaterial;
	//document.frmmaterial.hidden_vendor_id.value=0;
	document.frmmaterial.command.value="<%=Command.EDIT%>";
	document.frmmaterial.prev_command.value="<%=prevCommand%>";
	document.frmmaterial.action="simple_mat_main.jsp";
	document.frmmaterial.submit();
}

function cmdCancel(oidMaterial){
	document.frmmaterial.hidden_material_id.value=oidMaterial;
	document.frmmaterial.hidden_vendor_id.value=0;
	document.frmmaterial.command.value="<%=Command.EDIT%>";
	document.frmmaterial.prev_command.value="<%=prevCommand%>";
	document.frmmaterial.action="simple_mat_main.jsp";
	document.frmmaterial.submit();
}

function cmdBack(){
	document.frmmaterial.hidden_material_id.value="0";
    document.frmmaterial.hidden_vendor_id.value="0";
	document.frmmaterial.command.value="<%=Command.ADD%>";
	document.frmmaterial.prev_command.value="<%=prevCommand%>";
	document.frmmaterial.action="simple_mat_main.jsp";
	document.frmmaterial.submit();
}

function cmdselectvendor()
{
	window.open("simplevendordosearch.jsp?txt_vendorkode="+document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_SKU]%>.value,"simplematerial", "height=1,width=1,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes");
}

function cmdstockcard(oidlok){
	window.open("src_stockcard.jsp?txtkode=<%=material.getSku()%>&txtnama=<%=goodName%>&oid_material=<%=oidMaterial%>&location_oid="+oidlok+"","simplematerial", "height=500,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
}

function cmdenter(){
    if(event.keyCode == 13){
        cmdselectvendor();
        document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_NAME]%>.focus;
    }
}

function cmdblur(){
    cmdselectvendor();
    document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_NAME]%>.focus;
}

/*------------- end main function -----------------*/

function calculate(){
	var cost = cleanNumberFloat(document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_DEFAULT_COST]%>.value,guiDigitGroup,guiDecimalSymbol);
	var lastDisc = cleanNumberFloat(document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_LAST_DISCOUNT]%>.value,guiDigitGroup,guiDecimalSymbol);
	var lastVat = cleanNumberFloat(document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_LAST_VAT]%>.value,guiDigitGroup,guiDecimalSymbol);
    var profit = cleanNumberFloat(document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_PROFIT]%>.value,guiDigitGroup,guiDecimalSymbol);

	if((isNaN(cost)) || (cost=="")){
		cost = 0;
	}

	if((isNaN(lastDisc)) || (lastDisc=="")){
		lastDisc = 0;
	}

	if((isNaN(lastVat)) || (lastVat=="")){
		lastVat = 0;
	}

	var totaldiscount = (cost * lastDisc) / 100;
	var totalMinus = cost - totaldiscount;
    var totalppn = (totalMinus * lastVat) / 100;
	var totalCost = totalMinus + totalppn;

	document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_CURR_BUY_PRICE]%>.value = formatFloat(totalCost, '', guiDigitGroup, guiDecimalSymbol, decPlace);

    var currBuyProfit = (totalCost * profit) / 100;
    var lastCurrBuyProfit = totalCost + currBuyProfit;
	var lastCurrBuyWithProfit = lastCurrBuyProfit/<%=1%>;
    document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_DEFAULT_PRICE]%>.value = lastCurrBuyWithProfit;
    document.frmmaterial.<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_CURR_SELL_PRICE_RECOMENTATION]%>.value = lastCurrBuyWithProfit;
}
/*------------- end vendor price function -----------------*/
function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

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

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}
//-->
</script>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="../../styles/main.css" type="text/css">
<link rel="stylesheet" href="../../styles/tab.css" type="text/css">


</head>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
  <tr>
    <td width="88%" valign="top" align="left">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td>
		   <form name="frmmaterial" method ="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="hidden_material_id" value="<%=oidMaterial%>">
              <input type="hidden" name="hidden_vendor_id" value="<%=contactList.getOID()%>">
              <input type="hidden" name="hidden_catType" value="<%=catType%>">
              <input type="hidden" name="<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_BUY_UNIT_ID]%>" value="<%=material.getBuyUnitId()%>">
              <input type="hidden" name="<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_DEFAULT_STOCK_UNIT_ID]%>" value="<%=material.getDefaultStockUnitId()%>">
              <input type="hidden" name="<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_SUPPLIER_ID]%>" value="<%=material.getSupplierId()%>">
              <input type="hidden" name="<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_SUB_CATEGORY_ID]%>" value="<%=material.getSubCategoryId()%>">
              <input type="hidden" name="<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_DEFAULT_PRICE_CURRENCY_ID]%>" value="0">
              <input type="hidden" name="<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_DEFAULT_COST_CURRENCY_ID]%>" value="0">
              <input type="hidden" name="<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_CURR_SELL_PRICE_RECOMENTATION]%>" value="<%=FRMHandler.userFormatStringDecimal(material.getCurrSellPriceRecomentation())%>">
			  <input type="hidden" name="<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_MATERIAL_TYPE]%>" value="<%=PstMaterial.MAT_TYPE_REGULAR%>">
              <table width="100%" border="0">
                <tr>
                  <td width="7%" valign="top">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr align="left" valign="top">
                        <td height="18" valign="top">
                          <table width="100%" border="0" cellspacing="0" cellpadding="1">
                            <tr>
                              <td width="50%" valign="top"><table width="100%" border="0" cellspacing="1" cellpadding="1">
                                <tr>
                                  <td colspan="2"><strong>Product Editor</strong></td>
                                  </tr>
                                <tr>
                                  <td width="33%"><%=textListMaterialHeader[SESS_LANGUAGE][1]%></td>
                                  <td width="67%">
                                    :
                                    <input type="text"  class="formElemen" name="<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_SKU]%>" value="<%=material.getSku()%>"  onKeyDown="javascript:cmdenter()" onBlur="javascript:cmdblur()" size="20" maxlength="12" >
                                    * <%=frmMaterial.getErrorMsg(FrmMaterial.FRM_FIELD_SKU)%></td>
                                </tr>
                                <tr>
                                  <td><%=textListMaterialHeader[SESS_LANGUAGE][3]%></td>
                                  <td>
                                    :
                                    <input type="text"  class="formElemen" name="<%=FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_NAME]%>" value="<%=goodName%>"  size="30" >
                                  </td>
                                </tr>
                                <tr>
                                  <td><%=textListMaterialHeader[SESS_LANGUAGE][5]%></td>
                                  <td>:
                                    <%
                                        Vector materGroup = PstCategory.list(0,0,"",PstCategory.fieldNames[PstCategory.FLD_CODE]);
                                        Vector vectGroupVal = new Vector(1,1);
                                        Vector vectGroupKey = new Vector(1,1);
                                        if(materGroup!=null && materGroup.size()>0){
                                            for(int i=0; i<materGroup.size(); i++){
                                                Category mGroup = (Category)materGroup.get(i);
                                                vectGroupVal.add(mGroup.getName());
                                                vectGroupKey.add(""+mGroup.getOID());
                                            }
                                        }else{
                                            vectGroupVal.add(textListTitleHeader[SESS_LANGUAGE][5]);
                                            vectGroupKey.add("0");
                                        }
                                        out.println(ControlCombo.draw(FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_CATEGORY_ID],"formElemen", null, ""+material.getCategoryId(), vectGroupKey, vectGroupVal, null));
									  %>
</td>
                                </tr>
                                <tr>
                                  <td width="33%"><%=textListMaterialHeader[SESS_LANGUAGE][4]%></td>
                                  <td width="67%">
                                    :
                                      <%
                                        Vector materMerk = PstMerk.list(0,0,"",PstMerk.fieldNames[PstMerk.FLD_NAME]);
                                        Vector vectMerkVal = new Vector(1,1);
                                        Vector vectMerkKey = new Vector(1,1);
                                        if(materMerk!=null && materMerk.size()>0){
                                            for(int i=0; i<materMerk.size(); i++){
                                                Merk merk = (Merk)materMerk.get(i);
                                                vectMerkVal.add(merk.getName());
                                                vectMerkKey.add(""+merk.getOID());
                                            }
                                        }else{
                                            vectMerkVal.add(textListTitleHeader[SESS_LANGUAGE][5]);
                                            vectMerkKey.add("0");
                                        }
                                        out.println(ControlCombo.draw(FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_MERK_ID],"formElemen", null, ""+material.getMerkId(), vectMerkKey, vectMerkVal, null));
									  %>
                                  </td>
                                </tr>
                              </table></td>
                              <td width="50%" valign="top"><table width="100%"  border="0" cellspacing="1" cellpadding="1">
                                <%if(privEditSuppPrice){%><tr>
                                  <td colspan="2"><strong>Supplier</strong></td>
                                </tr>
                                <tr>
                                  <td width="22%" class="formElemen">Code</td>
                                  <td> :
                                      <input tabindex="1" type="text" size="20" readonly  name="txt_vendorcode" value="<%=contactList.getContactCode()%>" class="formElemen"></td>
                                </tr>
                                <tr>
                                  <td class="formElemen">Name</td>
                                  <td>:
                                      <input type="text" size="30" readonly name="txt_vendorname" value="<%=contactList.getCompName()%>" class="formElemen">
                                  </td>
                                </tr>
                                <tr>
                                  <td class="formElemen">Price</td>
                                  <td width="73%">: <span class="formElemen">
                                    <input tabindex="8" type="text" size="15"  name="<%=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_CURR_BUYING_PRICE]%>" value="<%=FRMHandler.userFormatStringDecimal(matVendorPrice.getCurrBuyingPrice())%>" class="formElemen" style="text-align:right">
      Valuta
      <%
                                        Vector vtCurrency = PstCurrencyType.list(0,0,PstCurrencyType.fieldNames[PstCurrencyType.FLD_INCLUDE_IN_PROCESS]+"="+PstCurrencyType.INCLUDE,PstCurrencyType.fieldNames[PstCurrencyType.FLD_TAB_INDEX]);
                                        Vector currencyVal = new Vector(1,1);
                                        Vector currencyKey = new Vector(1,1);
                                        if(vtCurrency!=null && vtCurrency.size()>0){
                                            for(int i=0; i<vtCurrency.size(); i++){
                                                CurrencyType currencyType = (CurrencyType)vtCurrency.get(i);
                                                currencyVal.add(currencyType.getCode());
                                                currencyKey.add(""+currencyType.getOID());
                                            }
                                        }else{
                                            currencyVal.add(textListTitleHeader[SESS_LANGUAGE][5]);
                                            currencyKey.add("0");
                                        }
                                        out.println(ControlCombo.draw(FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_DEFAULT_COST_CURRENCY_ID],"formElemen", null, ""+material.getDefaultCostCurrencyId(), currencyKey, currencyVal, null));
									  %>
                                  </span> </td>
                                </tr>
                                <%}else{%>
                                    <tr>
                                      <td class="formElemen">&nbsp;</td>
                                      <td width="73%">&nbsp;</td>
                                    </tr>
                                <%}%>
                              </table></td>
                            </tr>
                            <tr>
                              <td colspan="2" valign="top"><strong>Sales Price </strong></td>
                            </tr>
							<%if(privEditPrice){%>
                            <tr>
                              <td colspan="2" valign="top"><table width="70%"  border="0" cellspacing="1" cellpadding="0">
								<tr class="listgentitle">
									<td>Input here  </td>
									<td> or Input here  </td>
								</tr>
								<tr>
                                  <td valign="top"><table width="100%" border="0" cellpadding="2" cellspacing="1" class="listgensell">
                                    <tr>
                                      <td nowrap>COEFFICIENT</td>
                                      <td align="left" nowrap> :
                                          <input name="txtkoe_1" onKeyUp="javascript:cmdhitung1(this)" class="formElemen" style="text-align:right" type="text" size="5"></td>
                                    </tr>
                                    <%
								  if(listPriceType!=null&&listPriceType.size()>0){
								  	for(int i=0;i<listPriceType.size();i++){
										PriceType prType = (PriceType)listPriceType.get(i);
								  %>
                                    <tr>
                                      <td width="34%" nowrap><%=prType.getCode()%></td>
                                      <%

										for(int j=0;j<listCurrStandard.size();j++){
											Vector temp = (Vector)listCurrStandard.get(j);
											StandartRate sRate = (StandartRate)temp.get(1);
											double valuePrice = PstPriceTypeMapping.getPrice(listPriceMapping,oidMaterial,prType.getOID(),sRate.getOID());
                	                %>
                                      <td width="66%" align="left" nowrap> :
                                          <input type="text" name="price_<%=i%><%=j%>1" size="15" <% if(i==1){%>readonly <%}%> value="<%=FRMHandler.userFormatStringDecimal(valuePrice)%>" class="formElemen" style="text-align:right"  onKeyUp="javascript:cmdhitung1(this)" maxlength="12">
                                      </td>
                                      <%}%>
                                    </tr>
                                    <%}}%>
                                  </table></td>
                                  <td valign="top"><table width="100%" border="0" cellpadding="2" cellspacing="1" class="listgensell">
                                    <tr>
                                      <td nowrap>COEFFICIENT</td>
                                      <td align="left" nowrap> :
                                          <input name="txtkoe" type="text" class="formElemenR" style="text-align:right" onKeyUp="javascript:cmdhitung(this)" size="5" readonly></td>
                                    </tr>
                                    <%
								  if(listPriceType!=null&&listPriceType.size()>0){
								  	for(int i=0;i<listPriceType.size();i++){
										PriceType prType = (PriceType)listPriceType.get(i);
								  %>
                                    <tr>
                                      <td width="34%" nowrap><%=prType.getCode()%></td>
                                      <%

										for(int j=0;j<listCurrStandard.size();j++){
											Vector temp = (Vector)listCurrStandard.get(j);
											StandartRate sRate = (StandartRate)temp.get(1);
											double valuePrice = PstPriceTypeMapping.getPrice(listPriceMapping,oidMaterial,prType.getOID(),sRate.getOID());
                	                %>
                                      <td width="66%" align="left" nowrap> :
                                          <input type="text" name="price_<%=i%><%=j%>" size="15" value="<%=FRMHandler.userFormatStringDecimal(valuePrice)%>" class="formElemen" style="text-align:right"  onKeyUp="javascript:cmdhitung(this)" maxlength="12">
                                      </td>
                                      <%}%>
                                    </tr>
                                    <%}}%>
                                  </table></td>
								</tr>
                              </table></td>
                            </tr>
							<%}%>
                          </table>
                        </td>
                      </tr>
                      <tr align="left" valign="top">
                        <td height="8" align="right" valign="top"><%
							ctrLine.setLocationImg(approot+"/images");

							// set image alternative caption
							ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,materialTitle,ctrLine.CMD_SAVE,true));
							ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,materialTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,materialTitle,ctrLine.CMD_BACK,true)+" List");
							ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,materialTitle,ctrLine.CMD_ASK,true));
							ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,materialTitle,ctrLine.CMD_CANCEL,false));

							ctrLine.initDefault(SESS_LANGUAGE,materialTitle);
							ctrLine.setTableWidth("100%");
							String scomDel = "javascript:cmdAsk('"+oidMaterial+"')";
							String sconDelCom = "javascript:cmdConfirmDelete('"+oidMaterial+"')";
							String scancel = "javascript:cmdEdit('"+oidMaterial+"')";
							ctrLine.setCommandStyle("command");
							ctrLine.setColCommStyle("command");
                            ctrLine.setBackCaption("Add New");

							// set command caption
							//ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE,materialTitle,ctrLine.CMD_SAVE,true));
							//ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,materialTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,materialTitle,ctrLine.CMD_BACK,true)+" List");
							//ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE,materialTitle,ctrLine.CMD_ASK,true));
							//ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE,materialTitle,ctrLine.CMD_DELETE,true));
							//ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE,materialTitle,ctrLine.CMD_CANCEL,false));

							if (privDelete)
							{
								ctrLine.setConfirmDelCommand(sconDelCom);
								ctrLine.setDeleteCommand(scomDel);
								ctrLine.setEditCommand(scancel);
							}
							else
							{
								ctrLine.setConfirmDelCaption("");
								ctrLine.setDeleteCaption("");
								ctrLine.setEditCaption("");
							}

							if(privAdd == false  && privUpdate == false)
							{
								ctrLine.setSaveCaption("");
							}

							if (privAdd == false)
							{
								ctrLine.setAddCaption("");
                                ctrLine.setBackCaption("");
							}

							if(iCommand==Command.SAVE && frmMaterial.errorSize()==0){
								editWithoutList = true;
							}
                            if(iCommand==Command.NONE)
                                iCommand = Command.ADD;

                            if(iCommand==Command.ADD){
                                ctrLine.setBackCaption("");
                            }


							if(iCommand==Command.ADD || iCommand==Command.EDIT || iCommand==Command.ASK || iCommand==Command.SAVE || (iCommand==Command.DELETE && frmMaterial.errorSize()>0))
							{
								if(iCommand==Command.SAVE){
                                    out.println(ctrLine.drawImage(Command.EDIT, iErrCode, msgString));
                                }else{
                                    out.println(ctrLine.drawImage(iCommand, iErrCode, msgString));
                                }
							}
						  %></td>
                      </tr>
                      <tr align="left" valign="top">
                        <td height="8" align="right" valign="top">&nbsp;</td>
                      </tr>
					  <%
					  	if(oidMaterial!=0){
					  %>
                      <tr align="left" valign="top">
                        <td height="8" align="right" valign="top"><table width="100%"  border="0" cellpadding="0" cellspacing="1">
                            <%
                                Vector vLocation = PstLocation.list(0,0,"",PstLocation.fieldNames[PstLocation.FLD_NAME]);
                            %>
                          <tr>
                            <td colspan="<%=vLocation.size()+1%>"><strong>Current Stock</strong></td>
                            </tr>
                          <tr>
                            <td width="10%" class="listgentitle">Location</td>
                            <%
                                if(vLocation!=null && vLocation.size()>0){
                                    int wd = 90/vLocation.size();
                                    for(int k=0;k<vLocation.size();k++){
                                        Location location = (Location)vLocation.get(k);
                            %>
                            <td width="<%=wd%>%" class="listgentitle"><a href="javascript:cmdstockcard('<%=location.getOID()%>')"><%=location.getName()%></a></td>
                            <%}}%>
                          </tr>
                          <tr>
                            <td width="10%" class="listgentitle">Qty</td>
                            <%
                                if(vLocation!=null && vLocation.size()>0){
                                    Periode period = PstPeriode.getPeriodeRunning();
                                    int wd = 90/vLocation.size();
                                    for(int k=0;k<vLocation.size();k++){
                                        Location location = (Location)vLocation.get(k);
                                        String where = PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID]+"="+location.getOID()+
                                            " AND "+PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]+"="+oidMaterial+
                                                " AND "+PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID]+"="+period.getOID();

                                        Vector vStock = PstMaterialStock.list(0,0,where,"");
                                        MaterialStock matStock = new MaterialStock();
                                        if(vStock!=null && vStock.size()>0){
                                            matStock = (MaterialStock)vStock.get(0);
                                        }
                            %>
                            <td width="<%=wd%>%" align="center" class="listgensell"><%=matStock.getQty()%></td>
                            <%}}%>
                          </tr>
                        </table></td>
                      </tr>
					  <%}%>
                    </table>
                  </td>
                </tr>
              </table>
            </form></td>
        </tr>
      </table>
    </td>
  </tr>
</table>
</body>

<script language="JavaScript">
<%
if(iCommand==Command.SAVE)
{
%>
    parent.frames[2].cmdSearch();
<%
}
%>
</script>
</html>

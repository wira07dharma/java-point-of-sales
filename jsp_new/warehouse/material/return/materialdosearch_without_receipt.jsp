<%@page import="com.dimata.hanoman.entity.masterdata.PstMasterType"%>
<%@page import="com.dimata.hanoman.entity.masterdata.MasterType"%>
<%@page import="com.dimata.common.entity.system.AppValue"%>
<%@ page import="com.dimata.gui.jsp.ControlList,
                 com.dimata.qdep.form.FRMHandler,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.common.entity.contact.PstContactList,
                 com.dimata.common.entity.contact.ContactList,
				 com.dimata.common.entity.payment.PstStandartRate,
                 com.dimata.util.Command,
                 com.dimata.gui.jsp.ControlCombo,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.posbo.entity.masterdata.*,
                 com.dimata.posbo.form.masterdata.CtrlMaterial,
                 com.dimata.posbo.form.warehouse.FrmMatReturnItem,
		 com.dimata.posbo.entity.search.SrcMaterial,
                                 com.dimata.posbo.form.search.FrmSrcMaterial,
				 com.dimata.common.entity.payment.CurrencyType,
				 com.dimata.common.entity.payment.PstCurrencyType"%>
<%@page contentType="text/html"%>

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RETURN, AppObjInfo.G2_SUPPLIER_RETURN, AppObjInfo.OBJ_SUPPLIER_RETURN); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<%!
public static final String textListGlobal[][] = {
	{"Pencarian Barang","Tidak ada barang"},
	{"Goods Searching","No goods available"}
};

/* this constant used to list text of listHeader */
public static final String textMaterialHeader[][] = {
	{"Kategori","SKU","Nama Barang","Semua","Semua Barang"},
	{"Category","SKU","Material Name","All","All Goods"}
};

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] = { 
	{"No","Sku","Nama Barang","Unit","Qty","Harga Beli"},
	{"No","Code","Item","Unit","Qty","Cost"}
};

public String drawListMaterial2(long currency, double transRate, int language,Vector objectClass,int start, int typeOfBusinessDetail){
    String result = "";
    if(objectClass!=null && objectClass.size()>0){
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%"); 
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell"); 
        ctrlist.setHeaderStyle("listgentitle");

        ctrlist.addHeader(textListMaterialHeader[language][0],"1%");
        ctrlist.addHeader(textListMaterialHeader[language][1],"15%");					
        ctrlist.addHeader(textListMaterialHeader[language][2],"30%");	
        ctrlist.addHeader(textListMaterialHeader[language][3],"5%");
        if(typeOfBusinessDetail == 2){
            ctrlist.addHeader("Berat Stock","15%");
        }        					
        ctrlist.addHeader(textListMaterialHeader[language][4],"10%");		
        ctrlist.addHeader(textListMaterialHeader[language][5],"12%");	
				
        ctrlist.setLinkRow(2);
        ctrlist.setLinkSufix("");
        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        ctrlist.setLinkPrefix("javascript:cmdEdit('");
        ctrlist.setLinkSufix("')");
        ctrlist.reset();
        int index = -1;		
		
        if(start<0) start = 0;
		
        /** mendapatkan objek hashtable yang berisi list current standard rate */
        Hashtable objActiveStandardRate = PstStandartRate.getActiveStandardRate();

        for(int i=0; i<objectClass.size(); i++) {
            MaterialStock materialStock = (MaterialStock)objectClass.get(i);
			
            Material material = new Material();
            try {
                material = PstMaterial.fetchExc(materialStock.getMaterialUnitId());
            }
            catch(Exception e) {
                System.out.println("Exc when PstMaterial.fetchExc() : " + e.toString());
            }
			
            Unit unit = new Unit();
            try {
                unit = PstUnit.fetchExc(material.getDefaultStockUnitId());
            }
            catch(Exception e) {
                System.out.println("Exc when PstUnit.fetchExc() : " + e.toString());
            }
            
            //added by dewok 2018
            Material mat = new Material();
            Category category = new Category();
            Color color = new Color();
            MaterialDetail md = new MaterialDetail();                        
            try {
                mat = PstMaterial.fetchExc(material.getOID());
                category = PstCategory.fetchExc(mat.getCategoryId());
                color = PstColor.fetchExc(mat.getPosColor());
                long matDetailId = PstMaterialDetail.checkOIDMaterialDetailId(material.getOID());
                md = PstMaterialDetail.fetchExc(matDetailId);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            String itemName = "" + mat.getName();
            if(typeOfBusinessDetail == 2){
                if (mat.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS) {
                    itemName = "" + category.getName() + " " + color.getColorName() + " " + mat.getName();
                } else if (mat.getMaterialJenisType() == Material.MATERIAL_TYPE_BERLIAN) {
                    itemName = "" + category.getName() + " " + color.getColorName() + " Berlian " + mat.getName();
                }
            }
			
            Vector rowx = new Vector();	
			
            //if(materialStock.getQty() > 0){
            start = start + 1;
				
            double qtyPerbaseUnit = PstUnit.getQtyPerBaseUnit(material.getBuyUnitId(), material.getDefaultStockUnitId());
            //double sellPrice = PstPriceTypeMapping.getSellPrice(material.getOID(),PstPriceTypeMapping.getOidStandartRate(),PstPriceTypeMapping.getOidPriceType());
				
            /** ambil standard rate dari mata uang default material*/
            double standardRate = Double.parseDouble((String)objActiveStandardRate.get(""+material.getDefaultCostCurrencyId()));
				
            /** mendapatkan besaran default price dalam mata uang transaksi */
            double defaultPricePerTransCurr = (material.getDefaultCost() * standardRate) / transRate;
            
            
            rowx.add(""+start);		
            rowx.add("<div align=\"center\">"+material.getSku()+"</div>");		
            rowx.add(itemName);
            rowx.add("<div align=\"center\">"+unit.getCode()+"</div>");
            if(typeOfBusinessDetail == 2){
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(materialStock.getBerat())+"</div>");
            }            	
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(materialStock.getQty())+"</div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(defaultPricePerTransCurr)+"</div>");
				
            lstData.add(rowx);

            String materialName = "";
            materialName = itemName;
            materialName = materialName.replace('\"','`');
            materialName = materialName.replace('\'','`');
            
            boolean useForGreenbowl = 1 == Integer.valueOf(PstSystemProperty.getValueByName("USE_FOR_GREENBOWL"));
            if (useForGreenbowl) {
                double hargaJual = PstPriceTypeMapping.getSellPrice(material.getOID(), PstPriceTypeMapping.getOidStandartRate(), PstPriceTypeMapping.getOidPriceType());
                MasterType masterTypeSize = new MasterType();
                try {
                    long oidMappingSize = PstMaterialMappingType.getSelectedTypeId(1, material.getOID());
                    masterTypeSize = PstMasterType.fetchExc(oidMappingSize);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                lstLinkData.add(material.getOID()+"','"+material.getSku()+"','"+materialName+"','"+unit.getCode()+
                        "','"+FRMHandler.userFormatStringDecimal(hargaJual)+"','"+material.getDefaultStockUnitId()+
                        "','"+materialStock.getQty()+"','"+materialStock.getBerat()+"','"+md.getOngkos()+
                        "','"+material.getBarCode()+"','"+color.getColorName()+"','"+masterTypeSize.getMasterName());
            } else {
                lstLinkData.add(material.getOID()+"','"+material.getSku()+"','"+materialName+"','"+unit.getCode()+
                        "','"+FRMHandler.userFormatStringDecimal(defaultPricePerTransCurr)+"','"+material.getDefaultStockUnitId()+
                        "','"+materialStock.getQty()+"','"+materialStock.getBerat()+"','"+md.getOngkos());
            }
            //}
        }
        return ctrlist.draw();
    }else{
        result = "<div class=\"msginfo\">&nbsp;&nbsp;"+textListGlobal[language][1]+"</div>";
    }	
    return result;
}
%>

<!-- JSP Block -->
<%
long materialgroup = FRMQueryString.requestLong(request,"material_group");
String materialcode = FRMQueryString.requestString(request,"material_code");
String materialname = FRMQueryString.requestString(request,"material_name");
long locationId = FRMQueryString.requestLong(request,"location_id");
long currencyId = FRMQueryString.requestLong(request,"currency_id");
double transRate = FRMQueryString.requestDouble(request,"trans_rate");
int start = FRMQueryString.requestInt(request, "start");
int iCommand = FRMQueryString.requestCommand(request);
int recordToGet = 15;
int showStokNol = FRMQueryString.requestInt(request, "show_stok_Nol");
int showAllGoods = FRMQueryString.requestInt(request, "show_all_good");
long oidVendor = FRMQueryString.requestLong(request,"mat_vendor");
/**
* instantiate material object that handle searching parameter
*/
SrcMaterial objSrcMaterial = new SrcMaterial();
objSrcMaterial.setLocationId(locationId);
objSrcMaterial.setCategoryId(materialgroup);
objSrcMaterial.setMatcode(materialcode);
objSrcMaterial.setMatname(materialname);
objSrcMaterial.setShowStokNol(showStokNol);
objSrcMaterial.setSupplierId(oidVendor);

FrmSrcMaterial frmSrcMaterial = new FrmSrcMaterial();

//boolean useForGreenbowl = 1 == Integer.valueOf(PstSystemProperty.getValueByName("USE_FOR_GREENBOWL"));

/**
* get amount/count of material's list
*/
int vectSize = 0;//PstMaterialStock.countMaterialStockCurrPeriod(objSrcMaterial);
if(showAllGoods>0){
    objSrcMaterial.setSupplierId(0);
    vectSize = PstMaterialStock.countMaterialStockCurrPeriod(objSrcMaterial);
}else{
    vectSize = PstMaterialStock.countMaterialStockCurrPeriod(objSrcMaterial);
}
/**
* generate start/mailstone for displaying data
*/
CtrlMaterial ctrlMaterial = new CtrlMaterial(request);
if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST){
    start = ctrlMaterial.actionList(iCommand, start, vectSize, recordToGet);
} 
 
/**
* get material list will displayed in this page
*/ 
Vector vect = PstMaterialStock.listMaterialStockCurrPeriod(objSrcMaterial, start, recordToGet, "");
int Size = vect.size();

/** kondisi ini untuk menangani jika menemukan satu buah item saja, sehingga langsung di teruskan ke halaman item */

int automaticSelected = Integer.valueOf(AppValue.getValueByKey("AUTOMATIC_SELECTED"));

if(vectSize == 1 && automaticSelected==0) {
    
    MaterialStock materialStock = (MaterialStock)vect.get(0);

    Material material = new Material();
    try {
            material = PstMaterial.fetchExc(materialStock.getMaterialUnitId());
    }
    catch(Exception e) {
            System.out.println("Exc when PstMaterial.fetchExc() : " + e.toString());
    }

    Unit unit = new Unit();
    try {
            unit = PstUnit.fetchExc(material.getDefaultStockUnitId());
    }
    catch(Exception e) {
            System.out.println("Exc when PstUnit.fetchExc() : " + e.toString());
    }
//    MatCurrency matCurr = new MatCurrency();
//    try{
//        matCurr =PstMatCurrency.fetchExc(material.getDefaultCostCurrencyId());
//    }
//    catch(Exception e) {
//            System.out.println("Exc when PstMatCurrency.fetchExc() : " + e.toString());
//    }

    /** mendapatkan objek hashtable yang berisi list current daily rate */
    Hashtable objActiveStandardRate = PstStandartRate.getActiveStandardRate();

    double standardRate = 1.0d;
    try{
        standardRate =Double.parseDouble((String)objActiveStandardRate.get(""+material.getDefaultCostCurrencyId()));
        }catch(Exception exc){
             System.out.println("materialdosearch.jsp"+exc.toString());
            }
    double defaultCost = 0.0d;
    try{
       defaultCost = (material.getDefaultCost() * standardRate) / transRate; //mendapatkan besaran default cost dalam mata uang transaksi
    } catch(Exception exc){
         System.out.println("materialdosearch.jsp"+exc.toString());
    }
    
    //added by dewok 20190114 for greenbowl
    double hargaJual = 0;
    Color color = new Color();
    MasterType masterTypeSize = new MasterType();
    if (useForGreenbowl.equals("1")) {
        hargaJual = PstPriceTypeMapping.getSellPrice(material.getOID(), PstPriceTypeMapping.getOidStandartRate(), PstPriceTypeMapping.getOidPriceType());
        try {
            color = PstColor.fetchExc(material.getPosColor());
            long oidMappingSize = PstMaterialMappingType.getSelectedTypeId(1, material.getOID());
            masterTypeSize = PstMasterType.fetchExc(oidMappingSize);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    String name = "";
    name = material.getName();
    name = name.replace('\"','`');
    name = name.replace('\'','`');
    %>
    <script language="JavaScript">
        self.opener.document.forms.frm_retmaterial.<%=FrmMatReturnItem.fieldNames[FrmMatReturnItem.FRM_FIELD_MATERIAL_ID]%>.value = "<%=material.getOID()%>";
        self.opener.document.forms.frm_retmaterial.matCode.value = "<%=material.getSku()%>";
        self.opener.document.forms.frm_retmaterial.matItem.value = "<%=name%>";
        self.opener.document.forms.frm_retmaterial.matUnit.value = "<%=unit.getCode()%>";
        if(<%= useForGreenbowl %>) {
            self.opener.document.forms.frm_retmaterial.<%=FrmMatReturnItem.fieldNames[FrmMatReturnItem.FRM_FIELD_HARGA_JUAL]%>.value = "<%=FRMHandler.userFormatStringDecimal(hargaJual)%>";
            self.opener.document.forms.frm_retmaterial.matBarcode.value = "<%= material.getBarCode()%>";
            self.opener.document.forms.frm_retmaterial.matColor.value = "<%= color.getColorName()%>";
            self.opener.document.forms.frm_retmaterial.matSize.value = "<%= masterTypeSize.getMasterName() %>";
        } else {
            self.opener.document.forms.frm_retmaterial.<%=FrmMatReturnItem.fieldNames[FrmMatReturnItem.FRM_FIELD_COST]%>.value = "<%=FRMHandler.userFormatStringDecimal(defaultCost)%>";
        }
        self.opener.document.forms.frm_retmaterial.<%=FrmMatReturnItem.fieldNames[FrmMatReturnItem.FRM_FIELD_UNIT_ID]%>.value = "<%=unit.getOID()%>";

        self.opener.document.forms.frm_retmaterial.<%=FrmMatReturnItem.fieldNames[FrmMatReturnItem.FRM_FIELD_QTY]%>.focus();

        self.close();
    </script>
    <%
}

%>
<!-- End of JSP Block -->

<html>
<head>
<title>Dimata - ProChain POS</title>

<script language="JavaScript">
function cmdEdit(matOid, matCode, matName, matUnit, matPrice, matUnitId, residuQty, beratStock,ongkos,barcode,color,size) {
	self.opener.document.forms.frm_retmaterial.<%=FrmMatReturnItem.fieldNames[FrmMatReturnItem.FRM_FIELD_MATERIAL_ID]%>.value = matOid;
	self.opener.document.forms.frm_retmaterial.matCode.value = matCode;
	self.opener.document.forms.frm_retmaterial.matItem.value = matName;
	self.opener.document.forms.frm_retmaterial.matUnit.value = matUnit;
	self.opener.document.forms.frm_retmaterial.<%=FrmMatReturnItem.fieldNames[FrmMatReturnItem.FRM_FIELD_UNIT_ID]%>.value = matUnitId;
        if(<%= useForGreenbowl %>) {
            self.opener.document.forms.frm_retmaterial.<%=FrmMatReturnItem.fieldNames[FrmMatReturnItem.FRM_FIELD_HARGA_JUAL]%>.value = matPrice;
            self.opener.document.forms.frm_retmaterial.matBarcode.value = barcode;
            self.opener.document.forms.frm_retmaterial.matColor.value = color;
            self.opener.document.forms.frm_retmaterial.matSize.value = size;
        } else {
            self.opener.document.forms.frm_retmaterial.<%=FrmMatReturnItem.fieldNames[FrmMatReturnItem.FRM_FIELD_COST]%>.value = matPrice;
        }
	self.opener.document.forms.frm_retmaterial.residu_qty.value = residuQty;
	self.opener.document.forms.frm_retmaterial.<%=FrmMatReturnItem.fieldNames[FrmMatReturnItem.FRM_FIELD_QTY]%>.focus();
        if("<%=typeOfBusinessDetail%>" == 2) {
            self.opener.document.forms.frm_retmaterial.<%=FrmMatReturnItem.fieldNames[FrmMatReturnItem.FRM_FIELD_QTY]%>.value = 1;
            self.opener.document.forms.frm_retmaterial.<%=FrmMatReturnItem.fieldNames[FrmMatReturnItem.FRM_FIELD_BERAT]%>.value = beratStock;
            self.opener.document.forms.frm_retmaterial.<%=FrmMatReturnItem.fieldNames[FrmMatReturnItem.FRM_FIELD_ONGKOS]%>.value = ongkos;
            self.opener.document.forms.frm_retmaterial.<%=FrmMatReturnItem.fieldNames[FrmMatReturnItem.FRM_FIELD_TOTAL]%>.value = matPrice;
        }
	self.close();		
}

function cmdListFirst(){
	document.frmvendorsearch.command.value="<%=Command.FIRST%>";
	document.frmvendorsearch.action="materialdosearch_without_receipt.jsp";
	document.frmvendorsearch.submit();
}

function cmdListPrev(){
	document.frmvendorsearch.command.value="<%=Command.PREV%>";
	document.frmvendorsearch.action="materialdosearch_without_receipt.jsp";
	document.frmvendorsearch.submit();
}

function keyDownCheck(e){
    //activeTable();
   var trap = document.frmvendorsearch.trap.value;
   
   if (e.keyCode == 13 && trap==0) {
    document.frmvendorsearch.trap.value="1";
   }
   if (e.keyCode == 13 && trap==1) {
       document.frmvendorsearch.trap.value="0";
       cmdSearch();
       
   }
   if (e.keyCode == 27) {
       //alert("sa");
       document.frmvendorsearch.txt_materialname.value="";
   }
}


function cmdListNext(){
	document.frmvendorsearch.command.value="<%=Command.NEXT%>";
	document.frmvendorsearch.action="materialdosearch_without_receipt.jsp";
	document.frmvendorsearch.submit();
}

function cmdListLast(){
	document.frmvendorsearch.command.value="<%=Command.LAST%>";
	document.frmvendorsearch.action="materialdosearch_without_receipt.jsp";
	document.frmvendorsearch.submit();
}	

function cmdSearch(){
	document.frmvendorsearch.command.value="<%=Command.LIST%>";
        document.frmvendorsearch.start.value = '0';
	document.frmvendorsearch.action="materialdosearch_without_receipt.jsp";
	document.frmvendorsearch.submit();
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
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="../../../styles/main.css" type="text/css">
<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
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

<%--autocomplate--%>
<script type="text/javascript" src="../../../styles/jquery-1.4.2.min.js"></script>
<script src="../../../styles/jquery.autocomplete.js"></script>
<link rel="stylesheet" type="text/css" href="../../../styles/style.css" />




</head>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<script language="JavaScript">
	window.focus();
</script>
<table width="100%" border="0" cellspacing="3" cellpadding="2" height="100%" bgcolor="#FCFDEC" >
  <tr> <%@include file="../../../styletemplate/template_header_empty.jsp" %>
    <td width="88%" valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td height="20" class="mainheader" colspan="2">
		  	<%=textListGlobal[SESS_LANGUAGE][0]%>
		  </td>
        </tr>
        <tr>
          <td colspan="2">
            <hr size="1">
          </td>
        </tr>
        <tr>
          <td>
          <%
            try{
          %>
            <form name="frmvendorsearch" method="post" action="">
                <input type="hidden" name="start" value="<%=start%>">
                <input type="hidden" name="command" value="<%=iCommand%>">
		<input type="hidden" name="location_id" value="<%=locationId%>">
                <input type="hidden" name="currency_id" value="<%=currencyId%>">
                <input type="hidden" name="trans_rate" value="<%=transRate%>">
                <input type="hidden" name="mat_vendor" value="<%=oidVendor%>">
                <input type="hidden" name="trap" value="">
              <table width="100%" border="0" cellspacing="1" cellpadding="1">
                <tr>
                  <td width="13%"><%=textMaterialHeader[SESS_LANGUAGE][0]%></td>
                  <td width="87%"> 
                      <%--<%
				  Vector category = PstCategory.list(0,0,"",PstCategory.fieldNames[PstCategory.FLD_NAME]);
				  Vector vectGroupVal = new Vector(1,1);
				  Vector vectGroupKey = new Vector(1,1);
				  vectGroupVal.add(textMaterialHeader[SESS_LANGUAGE][3]+" "+textMaterialHeader[SESS_LANGUAGE][0]);
				  vectGroupKey.add("-1");																	  				  
				  if(category!=null && category.size()>0) {
					  for(int i=0; i<category.size(); i++) {
						  Category mGroup = (Category)category.get(i);
						  vectGroupVal.add(mGroup.getName());
						  vectGroupKey.add(""+mGroup.getOID());																	  	
					  }
				  }
				  else {							  
					  vectGroupVal.add("No Category Available");
					  vectGroupKey.add("0");									
				  }
				  out.println(ControlCombo.draw("material_group","formElemen", null, ""+materialgroup, vectGroupKey, vectGroupVal, null));
				  %>--%>
                      <select id="material_group"  name="material_group" class="formElemen">
                        <option value="0">Semua Category</option>
                        <%
                         Vector masterCatAcak = PstCategory.list(0,0,"",PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID]);
                         //Category newCategory = new Category();
                         //add opie-eyek 20130821
                         //long xx = Long.parseLong("201611010033");
                         Vector materGroup = PstCategory.structureList(masterCatAcak) ;
                         Vector vectGroupVal = new Vector(1,1);
                         Vector vectGroupKey = new Vector(1,1);
                         if(materGroup!=null && materGroup.size()>0) {
                             String parent="";
                            // Vector<Category> resultTotal= new Vector();
                             Vector<Long> levelParent = new Vector<Long>();
                             for(int i=0; i<materGroup.size(); i++) {
                                 Category mGroup = (Category)materGroup.get(i);
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
                                     <option value="<%=mGroup.getOID()%>"><%=parent%><%=mGroup.getName()%></option>
                                 <%
                             }
                         } else {
                             vectGroupVal.add("Tidak Ada Category");
                             vectGroupKey.add("0");
                         }
                       %>
                       </select>
                             <input type="checkbox" name="show_stok_Nol" value="1" <% if(showStokNol==1){%>checked<%}%> >With Qty Nol
                             <input type="checkbox" name="show_all_good" value="1" <% if(showAllGoods==1){%>checked<%}%> ><%=textMaterialHeader[SESS_LANGUAGE][4]%>
                  </td>

                  <!-- Opsi with stok 0 -->
              
                     
                </tr>
                <tr> 
                  <td width="13%"><%=textMaterialHeader[SESS_LANGUAGE][1]%></td>
                  <td width="87%"> 
                    <input type="text" name="material_code" size="15" value="<%=materialcode%>" class="formElemen">
                  </td>
                </tr>
                
                
                 <%--autoComplete--%>
                <tr> 
                  <td width="13%"><%=textMaterialHeader[SESS_LANGUAGE][2]%></td>
                  <td width="87%"> 
                    <input type="text" onKeyDown="javascript:keyDownCheck(event)" name="material_name" size="30" value="<%=materialname%>" class="formElemen" id="txt_materialname">
                  </td>
                </tr>
                <tr> 
                  <td width="13%">&nbsp;</td>
                  <td width="87%"> 
                    <input type="button" name="Button" value="Search" onClick="javascript:cmdSearch()" class="formElemen">
                  </td>
                </tr>
                <tr> 
                  <td colspan="2"><%=drawListMaterial2(currencyId, transRate, SESS_LANGUAGE, vect, start, typeOfBusinessDetail)%></td>
                </tr>
                <tr> 
                  <td colspan="2"><span class="command"> 
                    <% 
						ControlLine ctrlLine= new ControlLine();
						ctrlLine.setLocationImg(approot+"/images");
						ctrlLine.initDefault();
					%>
                    <%=ctrlLine.drawImageListLimit(iCommand ,vectSize,start,recordToGet)%> </span>
				  </td>
                </tr>
              </table>
            </form>
            <%
            }catch(Exception e){
                System.out.println("terr "+e.toString());
            }
            %>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
</body>

<script language="JavaScript">
	document.frmvendorsearch.material_name.focus();
</script>


<%-- add by Fitra --%>





<script>
	jQuery(function(){
		$("#txt_materialname").autocomplete("list.jsp");
	});
</script>


</html>

<%@ page import="com.dimata.gui.jsp.ControlList,com.dimata.qdep.form.FRMHandler,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.common.entity.contact.PstContactList,
                 com.dimata.common.entity.contact.ContactList,
		 com.dimata.common.entity.payment.PstStandartRate,
                 com.dimata.util.Command,
                 com.dimata.gui.jsp.ControlCombo,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.posbo.entity.masterdata.*,
                 com.dimata.posbo.form.masterdata.CtrlMaterial,
                 com.dimata.posbo.form.warehouse.FrmMatReceiveItem,
                 com.dimata.posbo.form.masterdata.*,
                 com.dimata.posbo.entity.search.SrcMaterial"%>
<%@page contentType="text/html"%>

<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_CATALOG); %>
<%@ include file = "../../main/checkuser.jsp" %>

<%!
public static final String textListGlobal[][] = {
    {"Pencarian Barang","Tidak ada barang"},
    {"Goods Searching","No goods available"}
};

/* this constant used to list text of listHeader */
public static final String textMaterialHeader[][] = {
    {"Kategori","Sku","Nama Barang","Semua"},
    {"Category","Material Code","Material Name","All"}
};

/* this constant used to list text of listHeader */
//public static final String textListMaterialHeader[][] = {
   //{"No","Grup","Sku","Nama Barang","Unit","Harga Beli"},
   //{"No","Category","Code","Item","Unit","Cost"}
//};

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] = {
   {"No","Grup","Sku","Nama Barang","Unit","Nilai Stok","Harga Beli","Qty"},
   {"No","Category","Code","Item","Unit","Stock Value","Cost","Qty"}
};

public String drawListMaterial(long currency, double transRate, int language,Vector objectClass,int start) {
    String result = "";
    if(objectClass!=null && objectClass.size()>0){
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        
        ctrlist.addHeader(textListMaterialHeader[language][0],"5%");
        //ctrlist.addHeader(textListMaterialHeader[language][1],"20%");
        ctrlist.addHeader(textListMaterialHeader[language][2],"15%");
        ctrlist.addHeader(textListMaterialHeader[language][3],"35%");
        ctrlist.addHeader(textListMaterialHeader[language][4],"10%");
        ctrlist.addHeader(textListMaterialHeader[language][5],"15%");
        ctrlist.addHeader(textListMaterialHeader[language][6],"15%");

        ctrlist.setLinkRow(2);
        ctrlist.setLinkSufix("");
        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        ctrlist.setLinkPrefix("javascript:cmdEdit('");
        ctrlist.setLinkSufix("')");
        ctrlist.reset();
        int index = -1;
        
        if(start<0) start = 0;
        
        /** mendapatkan objek hashtable yang berisi list current daily rate */
        Hashtable objActiveStandardRate = PstStandartRate.getActiveStandardRate();
        
        for(int i=0; i<objectClass.size(); i++) {
            Vector vt = (Vector)objectClass.get(i);
            Material material = (Material)vt.get(0);
            Category categ = (Category)vt.get(1);
            Unit unit = (Unit)vt.get(2);
            MatCurrency matCurr = (MatCurrency)vt.get(3);
            MatVendorPrice matVendorPrice = (MatVendorPrice)vt.get(4);
            

           /* MaterialStock materialStock = (MaterialStock)objectClass.get(i);

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
                        //MatCurrency matCurr = new MatCurrency();
                       // try{
                           // matCurr =PstMatCurrency.fetchExc(material.getDefaultCostCurrencyId());
                      //  }
                        //catch(Exception e) {
				//System.out.println("Exc when PstMatCurrency.fetchExc() : " + e.toString());
			//}*/
                        
            start = start + 1;
            
            double standardRate = 0;
            try{standardRate = Double.parseDouble((String)objActiveStandardRate.get(""+material.getDefaultCostCurrencyId()));}catch(Exception exc){;}            
            double defaultStock = 0; //mendapatkan besaran default cost dalam mata uang transaksi
            try{defaultStock = material.getAveragePrice() * standardRate;}catch(Exception exc){;}
            double defaultCost = 0; //mendapatkan besaran default cost dalam mata uang transaksi
            try{defaultCost = material.getDefaultCost() * standardRate;}catch(Exception exc){;}
            double currBuyPrice = 0; //mendapatkan besaran default cost dalam mata uang transaksi
            try{currBuyPrice = material.getCurrBuyPrice() * standardRate;}catch(Exception exc){;}
            
            Vector rowx = new Vector();
            
            rowx.add(""+start);
            // rowx.add(categ.getName());
            rowx.add(material.getSku());
            rowx.add(material.getName());
            rowx.add(unit.getCode());
            //rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(matVendorPrice.getCurrBuyingPrice())+"</div>");
            rowx.add("<div align=\"right\"><a href=\"javascript:cmdEditItem('"+material.getOID()+"')\">"+FRMHandler.userFormatStringDecimal(defaultStock)+"</a></div>");
            rowx.add("<div align=\"right\"><a href=\"javascript:cmdEditItem('"+material.getOID()+"')\">"+FRMHandler.userFormatStringDecimal(currBuyPrice)+"</a></div>");
            //rowx.add("<div align=\"right\"><a href=\"javascript:cmdEditItem('"+material.getOID()+"')\">"+FRMHandler.userFormatStringDecimal(defaultCost)+"</a></div>");
           // rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(materialStock.getQty())+"</div>");

            String name = "";
            name = material.getName();
            name = name.replace('\"','`');
            name = name.replace('\'','`');
            
            lstData.add(rowx);
            //lstLinkData.add(material.getOID()+"','"+material.getSku()+"','"+name+"','"+unit.getCode()+
                   // "','"+(defaultCost>0.00 ? (""+FRMHandler.userFormatStringDecimal(defaultCost)) : "")+"','"+unit.getOID());
            lstLinkData.add(material.getOID()+"','"+material.getSku()+"','"+name+"','"+unit.getCode()+
                    "','"+(defaultStock>0.00 ? (""+FRMHandler.userFormatStringDecimal(defaultStock)) : "")+"','"+unit.getOID()+"','"+(currBuyPrice>0.00 ? (""+FRMHandler.userFormatStringDecimal(currBuyPrice)) : ""));
        }
        return ctrlist.draw();
    } else {
        result = "<div class=\"msginfo\">&nbsp;&nbsp;"+textListGlobal[language][1]+"</div>";
    }
    return result;
}
%>

<!-- JSP Block -->
<%
String materialcode = FRMQueryString.requestString(request,"mat_code");
long materialgroup = FRMQueryString.requestLong(request,"txt_materialgroup");
String materialname = FRMQueryString.requestString(request,"txt_materialname");
long locationId = FRMQueryString.requestLong(request,"location_id");
long oidVendor = FRMQueryString.requestLong(request,"mat_vendor");
long currencyId = FRMQueryString.requestLong(request,"currency_id");
double transRate = FRMQueryString.requestDouble(request,"trans_rate");
int start = FRMQueryString.requestInt(request, "start");
int iCommand = FRMQueryString.requestCommand(request);
int recordToGet = 15;

String supplierName = "";
try{
    PstContactList pstContactList = new PstContactList();
    ContactList contactList = pstContactList.fetchExc(oidVendor);
    supplierName = contactList.getCompName();
}catch(Exception e){
    System.out.println("Err fetch ContactList");
}

/**
* instantiate material object that handle searching parameter
*/
String orderBy = "MAT."+PstMaterial.fieldNames[PstMaterial.FLD_SKU];
Material objMaterial = new Material();
objMaterial.setCategoryId(materialgroup);
objMaterial.setSku(materialcode);
objMaterial.setBarCode(materialcode);
objMaterial.setName(materialname);
objMaterial.setDefaultCostCurrencyId(currencyId);

//SrcMaterial objSrcMaterial = new SrcMaterial();
//objSrcMaterial.setLocationId(locationId);
//objSrcMaterial.setCategoryId(materialgroup);
//objSrcMaterial.setMatcode(materialcode);
//objSrcMaterial.setMatname(materialname);
//objSrcMaterial.setBarCode(materialcode);




/**
* get amount/count of material's list
*/
//int vectSize = PstMaterial.getCountMaterialItem(0,objMaterial);
int vectSize = PstMaterial.getCountMaterialItemComposit(0, objMaterial);
//int vectSize = PstMaterialStock.countMaterialStockCurrPeriodAll(objSrcMaterial);
//int vectSize = PstMaterialStock.countMaterialStockCurrPeriodAllBarcode(objSrcMaterial);


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
//Vector vect = PstMaterial.getListMaterialItem(0,objMaterial,start,recordToGet, orderBy);
  //Vector vect = PstMaterialStock.getlistMaterialStockCurrPeriodAllBarcode(objSrcMaterial, start, recordToGet, "");
Vector vect = PstMaterial.getListMaterialItemComposit(0, objMaterial, start, recordToGet, orderBy);


/** kondisi ini untuk menangani jika menemukan satu buah item saja, sehingga langsung di teruskan ke halaman item */
if(vectSize == 1) {

    Vector vt = (Vector)vect.get(0);
    Material material = (Material)vt.get(0);
    Category categ = (Category)vt.get(1);
    Unit unit = (Unit)vt.get(2);
    MatCurrency matCurr = (MatCurrency)vt.get(3);
    MatVendorPrice matVendorPrice = (MatVendorPrice)vt.get(4);
    
    /*MaterialStock materialStock = (MaterialStock)vect.get(0);

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
                        MatCurrency matCurr = new MatCurrency();
                        try{
                            matCurr =PstMatCurrency.fetchExc(material.getDefaultCostCurrencyId());
                        }
                        catch(Exception e) {
				System.out.println("Exc when PstMatCurrency.fetchExc() : " + e.toString());
			}*/

    /** mendapatkan objek hashtable yang berisi list current daily rate */
    Hashtable objActiveStandardRate = PstStandartRate.getActiveStandardRate();
    
    double standardRate = 1.0d;
    try{
        standardRate =Double.parseDouble((String)objActiveStandardRate.get(""+material.getDefaultCostCurrencyId()));
        }catch(Exception exc){
             System.out.println("materialcomponentdosearch.jsp"+exc.toString());
            }
    double defaultCost = 0.0d;
    try{
       defaultCost = material.getAveragePrice() * standardRate; //mendapatkan besaran default cost dalam mata uang transaksi
    } catch(Exception exc){
         System.out.println("materialcomponentdosearch.jsp"+exc.toString());
    }

     double defaultStock = 0.0d;
    try{
       defaultStock = material.getDefaultCost() * standardRate; //mendapatkan besaran default cost dalam mata uang transaksi
    } catch(Exception exc){
         System.out.println("materialcomponentdosearch.jsp"+exc.toString());
    }
    
    String name = "";
    name = material.getName();
    name = name.replace('\"','`');
    name = name.replace('\'','`');
    
    /*String link = "'"+material.getOID()+"','"+material.getSku()+"','"+name+"','"+unit.getCode()+"','"+
            FRMHandler.userFormatStringDecimal(defaultCost)+"','"+unit.getOID()+"','"+
            material.getDefaultCostCurrencyId()+"','"+matCurr.getCode()+"'";*/
     /* String link = "'"+material.getOID()+"','"+material.getSku()+"','"+name+"','"+unit.getCode()+"','"+
            FRMHandler.userFormatStringDecimal(defaultCost)+"','"+unit.getOID()+"','"+
            material.getDefaultCostCurrencyId()+"','"+matCurr.getCode()+"','"+materialStock.getQty()+"'";*/
    %>
    <script language="JavaScript">
       self.opener.document.forms.frm_compositmat.<%=FrmMaterialComposit.fieldNames[FrmMaterialComposit.FRM_FIELD_MATERIAL_COMPOSER_ID]%>.value = "<%=material.getOID()%>";
       self.opener.document.forms.frm_compositmat.matCode.value = "<%=material.getSku()%>";
       self.opener.document.forms.frm_compositmat.matItem.value = "<%=name%>";
       self.opener.document.forms.frm_compositmat.<%=FrmMaterialComposit.fieldNames[FrmMaterialComposit.FRM_FIELD_UNIT_ID]%>.value = "<%=unit.getOID()%>";
       self.opener.document.forms.frm_compositmat.hidden_qty_unit.value = "<%=unit.getOID()%>";
       self.opener.document.forms.frm_compositmat.matUnit.value = "<%=unit.getCode()%>";
       self.opener.document.forms.frm_compositmat.<%=FrmMaterialComposit.fieldNames[FrmMaterialComposit.FRM_FIELD_QTY_INPUT]%>.focus();
       self.opener.document.forms.frm_compositmat.nilaiStock.value = "<%=defaultStock%>";
       self.opener.document.forms.frm_compositmat.matPrice.value = "<%=defaultCost%>";
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

function addNewItem(){
    document.frmvendorsearch.command.value="<%=Command.ADD%>";
    document.frmvendorsearch.action="<%=approot%>/master/material/material_main.jsp";
    document.frmvendorsearch.submit();
    }

function cmdEdit(matOid,matCode,matItem,matUnit,matNilaiStock,matUnitId,matPrice){
    self.opener.document.forms.frm_compositmat.<%=FrmMaterialComposit.fieldNames[FrmMaterialComposit.FRM_FIELD_MATERIAL_COMPOSER_ID]%>.value = matOid;
    self.opener.document.forms.frm_compositmat.matCode.value = matCode;
    self.opener.document.forms.frm_compositmat.matItem.value = matItem;
    self.opener.document.forms.frm_compositmat.<%=FrmMaterialComposit.fieldNames[FrmMaterialComposit.FRM_FIELD_UNIT_ID]%>.value = matUnitId;
    self.opener.document.forms.frm_compositmat.hidden_qty_unit.value = matUnitId;
    self.opener.document.forms.frm_compositmat.matUnit.value = matUnit;
    self.opener.document.forms.frm_compositmat.<%=FrmMaterialComposit.fieldNames[FrmMaterialComposit.FRM_FIELD_QTY_INPUT]%>.focus();
    self.opener.document.forms.frm_compositmat.nilaiStock.value = matNilaiStock;
    self.opener.document.forms.frm_compositmat.matPrice.value = matPrice;
    //self.opener.document.forms.frm_compositmat.<%//=FrmMaterialComposit.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID]%>.value = matUnitId;
    //self.opener.changeFocus(self.opener.document.forms.frm_compositmat.matCode);
    self.close();
}


function cmdEditItem(oid)
{
    document.frmvendorsearch.hidden_material_id.value=oid;
    document.frmvendorsearch.start.value=0;
    document.frmvendorsearch.approval_command.value="<%=Command.APPROVE%>";
    document.frmvendorsearch.command.value="<%=Command.EDIT%>";
    document.frmvendorsearch.action="<%=approot%>/master/material/material_main.jsp";
    document.frmvendorsearch.submit();
}

function cmdListFirst(){
    document.frmvendorsearch.command.value="<%=Command.FIRST%>";
    document.frmvendorsearch.action="materialcomponentdosearch.jsp";
    document.frmvendorsearch.submit();
}

function cmdListPrev(){
    document.frmvendorsearch.command.value="<%=Command.PREV%>";
    document.frmvendorsearch.action="materialcomponentdosearch.jsp";
    document.frmvendorsearch.submit();
}

function cmdListNext(){
    document.frmvendorsearch.command.value="<%=Command.NEXT%>";
    document.frmvendorsearch.action="materialcomponentdosearch.jsp";
    document.frmvendorsearch.submit();
}

function cmdListLast(){
    document.frmvendorsearch.command.value="<%=Command.LAST%>";
    document.frmvendorsearch.action="materialcomponentdosearch.jsp";
    document.frmvendorsearch.submit();
}	

function cmdSearch(){
    document.frmvendorsearch.command.value="<%=Command.LIST%>";
    document.frmvendorsearch.start.value = '0';
    document.frmvendorsearch.action="materialcomponentdosearch.jsp";
    document.frmvendorsearch.submit();
}	

function clear(){
    document.frmvendorsearch.txt_materialcode.value="";
}

function fnTrapKD(e){
    if (e.keyCode == 13) {
        //document.all.aSearch.focus();
        cmdSearch();
    }
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
<%if(menuUsed == MENU_ICON){%>
    <link href="../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
<%}%>
<link rel="stylesheet" href="../../styles/main.css" type="text/css">
<link rel="stylesheet" href="../../styles/tab.css" type="text/css">

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
</head>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" border="0" cellspacing="3" cellpadding="2" height="100%" bgcolor="#FCFDEC" >
  <tr> 
    <td width="88%" valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> <%@include file="../../styletemplate/template_header_empty.jsp" %>
          <td height="20" class="mainheader" colspan="2"><%=textListGlobal[SESS_LANGUAGE][0]%></td>
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
              <input type="hidden" name="mat_vendor" value="<%=oidVendor%>">
              <input type="hidden" name="currency_id" value="<%=currencyId%>">
              <input type="hidden" name="trans_rate" value="<%=transRate%>">
              <input type="hidden" name="source_link" value="materialdosearch.jsp">
              <input type="hidden" name="hidden_material_id" value="0">
              <input type="hidden" name="approval_command" value="0">


              <table width="100%" border="0" cellspacing="1" cellpadding="1">
                <tr>
                  <td width="13%"><%=textMaterialHeader[SESS_LANGUAGE][0]%></td>
                  <td width="87%"> 
                  <%
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
                  } else {
                      vectGroupVal.add("No Category Available");
                      vectGroupKey.add("0");
                  }
                  //out.println(ControlCombo.draw("txt_materialgroup","formElemen", null, ""+materialgroup, vectGroupKey, vectGroupVal, null));
                  %>
                  <%=ControlCombo.draw("txt_materialgroup", null, ""+materialgroup, vectGroupKey, vectGroupVal, " onkeydown=\"javascript:fnTrapKD(event)\"", "formElemen")%>
                  </td>
                </tr>
                <tr> 
                  <td width="13%"><%=textMaterialHeader[SESS_LANGUAGE][1]%></td>
                  <td width="87%"> 
                    <input type="text" name="mat_code" size="15" value="<%=materialcode%>" class="formElemen" onKeyDown="javascript:fnTrapKD(event)">
                  </td>
                </tr>
                <tr> 
                  <td width="13%"><%=textMaterialHeader[SESS_LANGUAGE][2]%></td>
                  <td width="87%"> 
                    <input type="text" name="txt_materialname" size="30" value="<%=materialname%>" class="formElemen" onKeyDown="javascript:fnTrapKD(event)">
                  </td>
                </tr>
                <tr> 
                  <td width="13%">&nbsp;</td>
                  <td width="87%"> 
                    <input type="button" name="Button" value="Search" onClick="javascript:cmdSearch()" class="formElemen">
                    <input type="button" name="Button2" value="Add New Item" onClick="javascript:addNewItem()" class="formElemen">
                  </td>
                </tr>
                <tr> 
                  <td colspan="2"><%=drawListMaterial(currencyId, transRate, SESS_LANGUAGE, vect, start)%></td>
                </tr>
                <tr> 
                  <td colspan="2"><span class="command"> 
                    <%
                    ControlLine ctrlLine= new ControlLine();
                    %>
                    <%
                    ctrlLine.setLocationImg(approot+"/images");
                    ctrlLine.initDefault();
                    %>
                    <%=ctrlLine.drawImageListLimit(iCommand ,vectSize,start,recordToGet)%> </span></td>
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
	document.frmvendorsearch.txt_materialgroup.focus();
</script>
</html>

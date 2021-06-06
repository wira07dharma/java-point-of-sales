<%@ page language = "java" %>
<%@ page import="
                 com.dimata.common.entity.contact.PstContactList,
                 com.dimata.common.entity.contact.ContactList,
                 com.dimata.gui.jsp.ControlList,
                 com.dimata.qdep.form.FRMHandler,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.qdep.form.FRMMessage,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.util.Command,
                 com.dimata.posbo.entity.admin.PstAppUser,
                 com.dimata.posbo.entity.masterdata.*,
                 com.dimata.posbo.session.masterdata.SessMaterial,
                 com.dimata.posbo.form.masterdata.CtrlMaterial,
                 com.dimata.posbo.entity.search.SrcMaterial,
                 com.dimata.posbo.form.search.FrmSrcMaterial,
                 com.dimata.posbo.session.masterdata.SessUploadCatalogPhoto,
                 com.dimata.common.entity.location.*"%>
<%@ page import="com.dimata.common.entity.payment.CurrencyType"%>
<%@ page import="com.dimata.common.entity.payment.PstCurrencyType"%>

<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_CATALOG); %>
<%@ include file = "../../main/checkuser.jsp" %>



<%!
//public static final int SESS_LANGUAGE = com.dimata.util.lang.I_Language.LANGUAGE_FOREIGN;
public static final String textListTitleHeader[][] = {
    {"Daftar Barang","Barang","Tidak ada data","Masuk ke Exception"},
    {"Goods List","Goods","No data available","Go Into Exception"}
};
%>
<%
if(userGroupNewStatus == PstAppUser.GROUP_SUPERVISOR) {
    privAdd=false;
    privUpdate=false;
    privDelete=false;
}
%>

<!-- Jsp Block -->
<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

/* this constant used to list text of listHeader */
/*public static final String textListMaterialHeader[][] =
{
	{"No","Sku","Nama","Kategori","Sub Kategori","Tipe Supplier","Supplier","Hpp","Hrg Jual"},
	{"No","Code","Name","Category","Sub Kategori","Supplier Type","Supplier","Average","Price"}
};*/

public static final String textListMaterialHeader[][] = {
    {"No","Kode","Barcode","Kategori / Merk / Nama Barang","Jenis","Tipe","HPP","Harga Jual","Kategori","Merk","Kode Grup","Foto","Mata Uang HPP","Mata Uang Jual"},
    {"No","Code","Barcode","Category / Merk / Name","Jenis","Type","Average Price","Price","Category","Merk","Code Range","Picture","Curr. Avg Price","Curr. Sell"}
};

//{"No","Kode","Barcode","Nama Barang","Jenis","Tipe","Hpp","Hrg Jual","Kategori","Merk","Kode Grup"},
//{"No","Code","Barcode","Name","Jenis","Type","Average","Price","Category","Merk","Code Range"}


//header
//untuk discount Qty mapping
public static final String textListDiscountQtyHeader[][] = {
    {"No","Mata Uang","Tipe Member","Lokasi","Start Qty","To Qty","Diskon","Tipe Diskon","Harga Per Unit"},
    {"No","Currency","Member Type","Location","Start Qty","To Qty","Discount","Discount type","Unit Price"}
};


public String drawList(int language,Vector objectClass,int start, int showImage, String approot,int showUpdateCatalog, SrcMaterial srcMaterial, int start2, int showDiscountQty, int showHpp, int showCurrency) {
    String result = "";
    if(objectClass!=null && objectClass.size()>0) {
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("tableprint");
        ctrlist.setTitleStyle("cellprint");
        ctrlist.setCellStyle("cellprint");
        ctrlist.setHeaderStyle("cellprint");
        ctrlist.setBorder(2);
        ctrlist.setCellSpacing("1");
        
        /** mendapatkan mata uang default */
        CurrencyType defaultCurrencyType = PstCurrencyType.getDefaultCurrencyType();
        
        ctrlist.addHeader(textListMaterialHeader[language][0],"3%");
        ctrlist.addHeader(textListMaterialHeader[language][1],"10%");
        if(showImage==1)
            ctrlist.addHeader(textListMaterialHeader[language][11],"10%");
        
        ctrlist.addHeader(textListMaterialHeader[language][2],"12%");
        //ctrlist.addHeader(textListMaterialHeader[language][3],"35%");
	
        String merkName = PstSystemProperty.getValueByName("NAME_OF_MERK");
	String name = "Category / "+ merkName+" / Name";
	ctrlist.addHeader(name,"40%"); //textListMaterialHeader[language][3]
	
        //ctrlist.addHeader(textListMaterialHeader[language][9],"10%");
        //ctrlist.addHeader(textListMaterialHeader[language][3],"35%");
        //ctrlist.addHeader(textListMaterialHeader[language][12],"5%");
        if(showHpp == 1){
        ctrlist.addHeader(textListMaterialHeader[language][6]+" ("+defaultCurrencyType.getCode()+")","10%");
        }

        ctrlist.addHeader(textListMaterialHeader[language][13],"5%");
        ctrlist.addHeader(textListMaterialHeader[language][7],"10%");
        
        ctrlist.setLinkRow(1);
        ctrlist.setLinkSufix("");
        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        ctrlist.setLinkPrefix("javascript:cmdEdit('");
        ctrlist.setLinkSufix("')");
        ctrlist.reset();
        if(start<0) {
            start = 0;
        }
        Vector rowx = new Vector();
        Vector list = PstCurrencyType.list(0,0,PstCurrencyType.fieldNames[PstCurrencyType.FLD_TAB_INDEX]+"=1","");
        CurrencyType currSell = new CurrencyType();
        if(list!=null && list.size()>0){
            currSell = (CurrencyType)list.get(0);
        }
        
        for(int i=0; i<objectClass.size(); i++){
            Vector temp = (Vector)objectClass.get(i);
            Material material = (Material)temp.get(0);
            Category category = (Category)temp.get(1);
            //SubCategory subCategory = (SubCategory)temp.get(2);
            //ContactList cnt = (ContactList)temp.get(3);
            Merk merk = (Merk)temp.get(3);
            rowx = new Vector();
            
            start = start + 1;
            
            Material matUpdate = new Material();
            try{
                matUpdate = PstMaterial.fetchExc(material.getOID());
            }catch(Exception e){}
            rowx.add(""+start+"");
            rowx.add(""+material.getSku());
            
            // untuk menampilkan gambar
            if(showImage==1){
                SessUploadCatalogPhoto objSessUploadCatalogPhoto = new SessUploadCatalogPhoto();
                String pictPath = objSessUploadCatalogPhoto.fetchImagePeserta(material.getSku());
                if(pictPath.length()>0){
                    rowx.add("<div align=\"center\"><img heigth = \"110\" width = \"110\" src=\""+approot+"/"+pictPath+"\"></div>");
                }else{
                    rowx.add("&nbsp");
                }
            }
            
            rowx.add(material.getBarCode());
            
            String goodName = "";
	    
            /*if(material.getMaterialSwitchType()==Material.WITH_USE_SWITCH_MERGE_AUTOMATIC){
                material.setProses(Material.IS_PROCESS_INSERT_UPDATE);
                goodName = material.getName();
                try{
                    goodName = goodName.substring(goodName.lastIndexOf(Material.getSeparate())+1,goodName.length());
                }catch(Exception e){}
            }else{
                goodName = material.getName();
            }*/
	    
	    //material.setProses(Material.IS_PROCESS_SHORT_VIEW);
            rowx.add(material.getName());
	    
            //rowx.add(category.getName()+" / "+merk.getName()+" / "+goodName);
            //rowx.add(merk.getName());
            //rowx.add(goodName);
            //
            //rowx.add(subCategory.getCode());
            //rowx.add(PstMaterial.SpTypeSourceKey[material.getDefaultSupplierType()]);
            //rowx.add(cnt.getContactCode());
            CurrencyType curr = new CurrencyType();
            try{
                curr = PstCurrencyType.fetchExc(matUpdate.getDefaultCostCurrencyId());
            }catch(Exception e){}
            //rowx.add("<div align=\"right\">"+curr.getCode()+"</div>");
            if(showHpp==1){
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(matUpdate.getAveragePrice())+"</div>");
            }
            double priceSales = SessMaterial.getPriceSale(material);
            try{
                //matUpdate.setDefaultPrice(priceSales);
                //PstMaterial.updateExc(matUpdate);
            }catch(Exception e){}
            //rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(material.getDefaultPrice())+"</div>");
            rowx.add("<div align=\"center\">"+currSell.getCode()+"</div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(priceSales)+"</div>");
            lstData.add(rowx);


              //table for list discount qty mapping
            String whereClause = "";
             whereClause = " DISCQTY. "+PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_MATERIAL_ID]+"="+material.getOID();
              String strfromDate = Formater.formatDate(srcMaterial.getDateFrom(), "yyyy-MM-dd 00:00:01");
              String strtoDate = Formater.formatDate(srcMaterial.getDateTo(), "yyyy-MM-dd 23:59:59");
             whereClause = whereClause + " AND MAT." + PstMaterial.fieldNames[PstMaterial.FLD_UPDATE_DATE] + " BETWEEN '" + strfromDate + "' AND '" + strtoDate + "'";
             String orderClause = " LOC."+PstLocation.fieldNames[PstLocation.FLD_CODE];
              orderClause = orderClause + ", DISC." + com.dimata.common.entity.payment.PstDiscountType.fieldNames[PstDiscountType.FLD_CODE];
              orderClause = orderClause + ", CURR."+PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE];
            //Vector listDiscQty = PstDiscountQtyMapping.listDiscQtyAll(0,0, whereClause, orderClause);
            Vector listDiscQty = SessMaterial.getDiscountQtyMapping(srcMaterial,material.getOID());
            if(showUpdateCatalog==1 && showDiscountQty==1) {
              if(listDiscQty.size()!=0){
              rowx = new Vector();
              //rowx.add("");
              //rowx.add("");
              rowx.add("<div align=\"center\"colspan =\"3\" class=\"comment\"><b>DISCOUNT QTY</b></div>");
              //rowx.add("");
              //rowx.add("");
              rowx.add(drawDiscountQty(language,listDiscQty,start2,showCurrency));
              //rowx.add("");
              //rowx.add("");
              //rowx.add("");
              lstData.add(rowx);
             }
           }
        }
        result = ctrlist.draw();
    } else {
        result = "<div class=\"msginfo\">&nbsp;&nbsp;"+textListTitleHeader[language][2]+"</div>";
    }
    return result;
}

public String drawCodeRangeList(int language,Vector objectClass){
    String result = "";
    if(objectClass!=null && objectClass.size()>0){
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("20%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.addHeader(textListMaterialHeader[language][10],"20%");
        //ctrlist.addHeader(textListMaterialHeader[language][1],"10%");
        
        ctrlist.setLinkRow(-1);
        ctrlist.setLinkSufix("");
        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        ctrlist.setLinkPrefix("javascript:cmdEditSearch('");
        ctrlist.setLinkSufix("')");
        ctrlist.reset();
        Vector rowx = new Vector();
        for(int i=0; i<objectClass.size(); i++){
            CodeRange codeRange = (CodeRange)objectClass.get(i);
            rowx = new Vector();
            rowx.add("<a href=\"javascript:cmdEditSearch('"+codeRange.getOID()+"')\">"+codeRange.getFromRangeCode()+"</a>");
            lstData.add(rowx);
        }
        result = ctrlist.draw();
    }
    return result;
}

//for list discount qty mapping
public String drawDiscountQty(int language, Vector objectClass, int start, int showCurrency) {
    String result = "";
    if(objectClass!=null && objectClass.size()>0){
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("tableprint");
        ctrlist.setTitleStyle("cellprint");
        ctrlist.setCellStyle("cellprint");
        ctrlist.setHeaderStyle("cellprint");
        ctrlist.setBorder(2);
        ctrlist.setCellSpacing("1");
       
        ctrlist.addHeader(textListDiscountQtyHeader[language][0],"5%");
        if (showCurrency == 1){
        ctrlist.addHeader(textListDiscountQtyHeader[language][1],"10%");
        }
        ctrlist.addHeader(textListDiscountQtyHeader[language][2],"10%");
        ctrlist.addHeader(textListDiscountQtyHeader[language][3],"10%");
        ctrlist.addHeader(textListDiscountQtyHeader[language][4],"5%");
        ctrlist.addHeader(textListDiscountQtyHeader[language][5],"5%");
        ctrlist.addHeader(textListDiscountQtyHeader[language][6],"10%");
        ctrlist.addHeader(textListDiscountQtyHeader[language][7],"10%");
        ctrlist.addHeader(textListDiscountQtyHeader[language][8],"10%");


        ctrlist.setLinkRow(-1);
        ctrlist.setLinkSufix("");
        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        ctrlist.setLinkPrefix("javascript:cmdEditSearch('");
        ctrlist.setLinkSufix("')");
        ctrlist.reset();
        Vector rowx = new Vector();
        for(int i=0; i<objectClass.size(); i++){
            //Vector temp = (Vector)objectClass.get(i);
            DiscountQtyMapping discountqtymapping = (DiscountQtyMapping)objectClass.get(i);
            //DiscountType discountType = (DiscountType)temp.get(1);
            //CurrencyType currencyType = (CurrencyType)temp.get(2);
            //Location location = (Location)temp.get(3);
            //Material material = (Material)temp.get(4);

            com.dimata.common.entity.payment.DiscountType discountType = new com.dimata.common.entity.payment.DiscountType();
            try {
		  discountType = com.dimata.common.entity.payment.PstDiscountType.fetchExc(discountqtymapping.getDiscountTypeId());
	        }
	   catch(Exception e) {
                  System.out.println("Exc when PstDiscountType.fetchExc() : " + e.toString());
		}
            CurrencyType currencyType = new CurrencyType();
            try {
		  currencyType = PstCurrencyType.fetchExc(discountqtymapping.getCurrencyTypeId());
	        }
	   catch(Exception e) {
                  System.out.println("Exc when PstCurrencyType.fetchExc() : " + e.toString());
		}

            Location location = new Location();
            try {
		  location = PstLocation.fetchExc(discountqtymapping.getLocationId());
	        }
	   catch(Exception e) {
                  System.out.println("Exc when PstLocation.fetchExc() : " + e.toString());
		}

            Material material = new Material();
	    try {
		  material = PstMaterial.fetchExc(discountqtymapping.getMaterialId());
	        }
	   catch(Exception e) {
                  System.out.println("Exc when PstMaterial.fetchExc() : " + e.toString());
		}



            //start = start + 1;
            double diskon = 0;
            double diskonPersen =0;
            double hargaUnit = 0;
            double priceSales = SessMaterial.getPriceSale(material);

            rowx = new Vector();
            rowx.add(""+(start+i+1)+"");
            if (showCurrency == 1){
            rowx.add(currencyType.getCode());
            }
            rowx.add(discountType.getCode());
            rowx.add(location.getCode());
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(discountqtymapping.getStartQty()));
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(discountqtymapping.getToQty()));
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(discountqtymapping.getDiscountValue()));
            rowx.add(PstDiscountQtyMapping.typeDiscount[discountqtymapping.getDiscountType()]);
             if(discountqtymapping.getDiscountType()== 0){
               diskonPersen = discountqtymapping.getDiscountValue();
               diskon = priceSales *diskonPersen/100;
            }
            else if (discountqtymapping.getDiscountType()== 1){
               diskon = discountqtymapping.getDiscountValue();
            }

            hargaUnit = priceSales - diskon;
            if(discountqtymapping.getDiscountType()== 2){
              rowx.add("<div align=\"center\">-</div>");
            }

            else {
              rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(hargaUnit));
            }

            lstData.add(rowx);
        }
        result = ctrlist.draw();
    }


  return result;
}

%>

<%
/**
* get approval status for create document 
*/
int systemName = I_DocType.SYSTEM_MATERIAL;
boolean privManageData = true; 
%>

<%
/**
 * get title for purchasing(pr) document
 */
String dfTitle = textListTitleHeader[SESS_LANGUAGE][1];
String dfItemTitle = dfTitle + " Item";

/**
 * get request data from current form
 */
long oidMaterial = FRMQueryString.requestLong(request, "hidden_material_id");
long oidCodeRange = FRMQueryString.requestLong(request, "hidden_range_code_id");
int type = FRMQueryString.requestInt(request, "hidden_type");

/**
 * initialitation some variable
 */
int iErrCode = FRMMessage.ERR_NONE;
String msgStr = "";
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
//int recordToGet = 5000;
int recordToGet =0;
int vectSize = 0;
String whereClause = "";
//for discount qty
int start2 = FRMQueryString.requestInt(request, "start2");

/**
 * instantiate some object used in this page
 */
ControlLine ctrLine = new ControlLine();
CtrlMaterial ctrlMaterial = new CtrlMaterial(request);
SrcMaterial srcMaterial = new SrcMaterial();
SessMaterial sessMaterial = new SessMaterial();
FrmSrcMaterial frmSrcMaterial = new FrmSrcMaterial(request, srcMaterial);

    try{
        srcMaterial = (SrcMaterial)session.getValue(SessMaterial.SESS_SRC_MATERIAL);
        
        if (srcMaterial == null){
            srcMaterial = new SrcMaterial();
        }
    }catch(Exception e){
        out.println(textListTitleHeader[SESS_LANGUAGE][3]);
        srcMaterial = new SrcMaterial();
    }

/**
 * get vectSize, start and data to be display in this page
 */
vectSize = sessMaterial.getCountSearch(srcMaterial);
//vectSize = sessMaterial.getCountSearchWithMultiSupp(srcMaterial);
if(iCommand==Command.FIRST || iCommand==Command.NEXT || iCommand==Command.PREV ||
        iCommand==Command.LAST || iCommand==Command.LIST){
    start = ctrlMaterial.actionList(iCommand,start,vectSize,recordToGet);
}
Vector records = sessMaterial.searchMaterial(srcMaterial,start,recordToGet);
//Vector records = sessMaterial.searchMaterialWithMultiSupp(srcMaterial,start,recordToGet);

String vendorName = "";
if(srcMaterial.getSupplierId()>0){
    try{
        ContactList contact = new ContactList();
        contact = PstContactList.fetchExc(srcMaterial.getSupplierId());
        vendorName = ", Supplier : "+contact.getCompName();
    }catch(Exception e){}
}

/**
 * ini di gunakan untuk mencari range code
 */
Vector recordsCodeRange = PstCodeRange.list(0,0,"",PstCodeRange.fieldNames[PstCodeRange.FLD_FROM_RANGE_CODE]);

%>
<!-- End of Jsp Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
    
<script language="JavaScript">

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
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" --> 
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

<style type="text/css">
<!--
.cellprint {
	color: #000000;
	background-color: #FFFFFF;
        overflow: visible !important ;
}
table, .tableClass, #tableID, .tableprint {
    border: thin solid #FFFFFF;
    overflow: visible !important ;
}
.nav {
    float: none;
}
#logo img {
   position: relative;
}
-->
</style>
<!-- #EndEditable --> 
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" >
  <tr> 
    <td valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
       <%if(srcMaterial.getShowUpdateCatalog() != 1 ){ %>
        <tr>
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" --> 
            &nbsp;Masterdata &gt; <%=textListTitleHeader[SESS_LANGUAGE][0]%><!-- #EndEditable --></td>
         </tr>
         <%}%>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frm_material" method="post" action=""> 
              <input type="hidden" name="command" value="">
              <input type="hidden" name="add_type" value="">			  			  			  			  
              <input type="hidden" name="start" value= "<%=start%>">
              <input type="hidden" name="start2" value="<%=start2%>">
              <input type="hidden" name="hidden_material_id" value="<%=oidMaterial%>">
              <input type="hidden" name="hidden_range_code_id" value="<%=oidMaterial%>">
              <input type="hidden" name="hidden_type" value="<%=type%>">
              <input type="hidden" name="approval_command">
                <table width="100%" cellspacing="0" cellpadding="3">
                    <tr align="left" valign="top">
                  <%if(srcMaterial.getShowUpdateCatalog() == 1 ){ %>
                   <td height="14" valign="middle" colspan="3" class="command">
                    <b>DAFTAR CATALOG BARANG TERBARU</b> </td>
                </tr>
                  <td height="14" valign="middle" colspan="3" class="command">
                    <b>Tanggal : <%=Formater.formatDate(srcMaterial.getDateFrom(), "dd-MM-yyyy")%> s/d <%=Formater.formatDate(srcMaterial.getDateTo(), "dd-MM-yyyy")%></b> </td>
                <tr align="left" valign="top">
                  
                </tr>
                 <%}%>
                  <tr align="left" valign="top">
                    <td height="22" valign="middle" bgcolor="#333333"><%=drawList(SESS_LANGUAGE,records,start,srcMaterial.getShowImage(),approot, srcMaterial.getShowUpdateCatalog(), srcMaterial, start2, srcMaterial.getShowDiscountQty(),srcMaterial.getShowHpp(), srcMaterial.getShowCurrency())%></td>

                  </tr>
                  <tr align="left" valign="top">
                    <td  valign="top"> 
                       <table width="52%" border="0" cellspacing="0" cellpadding="0">
                         <tr>
                           <td width="30%" nowrap><a href="javascript:window.close()"> < CLOSE > </a></td>
                           <td width="45%" nowrap class="command">&nbsp;</td>
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
</table>
</body>
<!-- #EndTemplate --></html>

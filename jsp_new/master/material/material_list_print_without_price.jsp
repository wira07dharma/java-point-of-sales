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
                 com.dimata.posbo.session.masterdata.SessUploadCatalogPhoto"%>
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
    {"No","Kode","Barcode","Kategori / Merk / Nama Barang","Jenis","Tipe","HPP","Harga Jual","Kategori","Merk","Kode Grup","Foto","Mata Uang HPP","Mata Uang Jual", "Qty Stok Opname"},
    {"No","Code","Barcode","Category / Merk / Name","Jenis","Type","Average Price","Price","Category","Merk","Code Range","Picture","Curr. Avg Price","Curr. Sell", "Qty Stok Opname"}
};

//{"No","Kode","Barcode","Nama Barang","Jenis","Tipe","Hpp","Hrg Jual","Kategori","Merk","Kode Grup"},
//{"No","Code","Barcode","Name","Jenis","Type","Average","Price","Category","Merk","Code Range"}

public String drawList(int language,Vector objectClass,int start, int showImage, String approot) {
    String result = "";
    if(objectClass!=null && objectClass.size()>0) {
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("tableprint");
        ctrlist.setTitleStyle("cellprint");
        ctrlist.setCellStyle("cellprint");
        ctrlist.setHeaderStyle("cellprint");
        ctrlist.setBorder(1);
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
        //ctrlist.addHeader(textListMaterialHeader[language][6]+" ("+defaultCurrencyType.getCode()+")","10%");
        //ctrlist.addHeader(textListMaterialHeader[language][13],"5%");
        //ctrlist.addHeader(textListMaterialHeader[language][7],"10%");
        ctrlist.addHeader(textListMaterialHeader[language][14],"10%");
        
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
            //rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(matUpdate.getAveragePrice())+"</div>");
            
            double priceSales = SessMaterial.getPriceSale(material);
            try{
                //matUpdate.setDefaultPrice(priceSales);
                //PstMaterial.updateExc(matUpdate);
            }catch(Exception e){}
            //rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(material.getDefaultPrice())+"</div>");
            //rowx.add("<div align=\"center\">"+currSell.getCode()+"</div>");
            //rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(priceSales)+"</div>");
              rowx.add("<div align=\"right\"> </div>");
            lstData.add(rowx);
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
int recordToGet = 5000;
//int recordToGet = 40;
int vectSize = 0;
String whereClause = "";

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

function cmdListFirst()
{
    document.frm_material.command.value="<%=Command.FIRST%>";
    document.frm_material.action="material_list_print_without_price.jsp";
    document.frm_material.submit();
}

function cmdListPrev()
{
    document.frm_material.command.value="<%=Command.PREV%>";
    document.frm_material.action="material_list_print_without_price.jsp";
    document.frm_material.submit();
}

function cmdListNext()
{
    document.frm_material.command.value="<%=Command.NEXT%>";
    document.frm_material.action="material_list_print_without_price.jsp";
    document.frm_material.submit();
}

function cmdListLast()
{
    document.frm_material.command.value="<%=Command.LAST%>";
    document.frm_material.action="material_list_print_without_price.jsp";
    document.frm_material.submit();
}

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
        border-color: #000000;
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
        <tr> 
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->
            &nbsp;Masterdata &gt; <%=textListTitleHeader[SESS_LANGUAGE][0]%><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frm_material" method="post" action=""> 
              <input type="hidden" name="command" value="">
              <input type="hidden" name="add_type" value="">			  			  			  			  
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="hidden_material_id" value="<%=oidMaterial%>">
              <input type="hidden" name="hidden_range_code_id" value="<%=oidMaterial%>">
              <input type="hidden" name="hidden_type" value="<%=type%>">
              <input type="hidden" name="approval_command">
                <table width="100%"  border="0" cellspacing="0" cellpadding="3">
                  <tr align="left" valign="top">
                    <td height="22" valign="middle" bgcolor="#333333"><%=drawList(SESS_LANGUAGE,records,start,srcMaterial.getShowImage(),approot)%></td>
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

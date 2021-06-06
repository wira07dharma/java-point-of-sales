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
                 com.dimata.posbo.session.masterdata.SessMaterialReposting,
                 com.dimata.posbo.form.masterdata.CtrlMaterial,
                 com.dimata.posbo.entity.search.SrcMaterial,
                 com.dimata.posbo.form.search.FrmSrcMaterial,
                 com.dimata.posbo.session.masterdata.SessUploadCatalogPhoto,
                 com.dimata.gui.jsp.ControlCombo,
                 com.dimata.gui.jsp.ControlDate,
                 com.dimata.posbo.form.search.*,
                 com.dimata.posbo.entity.search.*,
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

public static final String textListMaterialHeader[][] = {
    {"No","Kode","Barcode","Kategori / Merk / Nama Barang","Jenis","Tipe","HPP","Harga Jual","Kategori","Merk","Kode Grup","Foto","Mata Uang HPP","Mata Uang Jual"},
    {"No","Code","Barcode","Category / Merk / Name","Jenis","Type","Average Price","Price","Category","Merk","Code Range","Picture","Curr. Avg Price","Curr. Sell"}
};


/* this constant used to list text of listHeader */
public static final String textListHeaderReposting[][] = {
	{"Lokasi","Periode"},
	{"Location","Period"}
};

public String drawList(int language,Vector objectClass,int start, int showImage, String approot, int showUpdateCatalog, SrcMaterialRepostingStock srcMaterialRepostingStock, int start2, int showDiscountQty, int showHpp, int showCurrency) {
    String result = "";
    if(objectClass!=null && objectClass.size()>0) {
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        
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
           // rowx.add("<a title=\"\" href=\"javascript:cmdEdit('"+material.getOID()+"')\">"+material.getSku()+"</a>");
            rowx.add(material.getSku());
            
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
            //double priceSales = SessMaterial.getPriceSale(material);
            double priceSales = SessMaterialReposting.getPriceSale(material);
            try{
                //matUpdate.setDefaultPrice(priceSales);
                //PstMaterial.updateExc(matUpdate);
            }catch(Exception e){}
            //rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(material.getDefaultPrice())+"</div>");
            rowx.add("<div align=\"center\">"+currSell.getCode()+"</div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(priceSales)+"</div>");
            lstData.add(rowx);

           
        }
        result = ctrlist.draw();
    } else {
        result = "<div class=\"msginfo\">&nbsp;&nbsp;"+textListTitleHeader[language][2]+"</div>";
    }
    return result;
}

 public String drawCalendarJS(String id){
        String str="";
        str+="<script language=\"javascript\">" +
                "var cal_obj2 = null;\n"+
                "var format = '%j-%m-%Y';\n"+

             "function show_calendar_"+id+"(el) {\n"+
                "if (cal_obj2) return;\n"+
                "var text_field = document.getElementById(\""+id+"\");\n"+
                "cal_obj2 = new RichCalendar();\n"+
                "cal_obj2.start_week_day = 0;\n"+
                "cal_obj2.show_time = false;\n"+
                "cal_obj2.language = 'en';\n"+
                "cal_obj2.user_onchange_handler = cal2_on_change_"+id+";\n"+
                "cal_obj2.user_onclose_handler = cal2_on_close_"+id+";\n"+
                "cal_obj2.user_onautoclose_handler = cal2_on_autoclose_"+id+";\n"+
                "cal_obj2.parse_date(text_field.value, format);\n"+
                "cal_obj2.show_at_element(text_field, \"adj_left-bottom\");\n"+
                //cal_obj2.change_skin('alt');
                "}\n"+

            // user defined onchange handler
            "function cal2_on_change_"+id+"(cal, object_code) {\n"+
                "if (object_code == 'day') {\n"+
                    "if(cal.get_formatted_date('%j').length <= 1){\n" +
                    "format='0%j-%m-%Y';\n" +
                    "}else{\n" +
                    "format='%j-%m-%Y';\n" +
                    "}\n" +
                    "document.getElementById(\""+id+"\").value = cal.get_formatted_date(format);\n"+
                    "cal.hide();\n"+
                    "cal_obj2 = null;\n"+
                "}\n"+
            "}\n"+

            // user defined onclose handler (used in pop-up mode - when auto_close is true)
            "function cal2_on_close_"+id+"(cal) {\n"+
                "if (window.confirm('Are you sure to close the calendar?')) {\n"+
                    "cal.hide();\n"+
                    "cal_obj2 = null;\n"+
                "}\n"+
            "}\n"+

            // user defined onautoclose handler
            "function cal2_on_autoclose_"+id+"(cal) {\n"+
                "cal_obj2 = null;\n"+
            "}\n" +
            "</script>\n";
        return str;
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
int recordToGet = 20;
int vectSize = 0;
String whereClause = "";
//for discount qty
int start2 = FRMQueryString.requestInt(request, "start2");
int index = FRMQueryString.requestInt(request,"type");

/**
 * instantiate some object used in this page
 */
ControlLine ctrLine = new ControlLine();
CtrlMaterial ctrlMaterial = new CtrlMaterial(request);
//SrcMaterial srcMaterial = new SrcMaterial();
SrcMaterialRepostingStock srcMaterialRepostingStock = new SrcMaterialRepostingStock();
//SessMaterial sessMaterial = new SessMaterial();
SessMaterialReposting sessMaterialReposting = new SessMaterialReposting();
//FrmSrcMaterial frmSrcMaterial = new FrmSrcMaterial(request, srcMaterial);
FrmSrcMaterialRepostingStock frmSrcMaterialRepostingStock = new FrmSrcMaterialRepostingStock(request,srcMaterialRepostingStock);


/**
 * handle current search data session
 */
if(iCommand==Command.BACK || iCommand==Command.FIRST ||
        iCommand==Command.PREV || iCommand==Command.NEXT ||
        iCommand==Command.LAST){
    try {
        //srcMaterialRepostingStock = (SrcMaterialRepostingStock)session.getValue(SessMaterial.SESS_SRC_MATERIAL);
          srcMaterialRepostingStock = (SrcMaterialRepostingStock)session.getValue(SessMaterialReposting.SESS_SRC_MATERIAL_REPOSTING);
        
        if (srcMaterialRepostingStock == null){
            srcMaterialRepostingStock = new SrcMaterialRepostingStock();
        }
    }catch(Exception e){
        out.println(textListTitleHeader[SESS_LANGUAGE][3]);
        srcMaterialRepostingStock = new SrcMaterialRepostingStock();
    }
}else{
    if(type==0){
        frmSrcMaterialRepostingStock.requestEntityObject(srcMaterialRepostingStock);
        //add opsi currency Type nd memberType
        Vector vectCurr = new Vector(1,1);
	 String[] strCurrencyType = request.getParameterValues(FrmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_CURRENCY_TYPE_ID]);
	 if(strCurrencyType!=null && strCurrencyType.length>0) {
		 for(int i=0; i<strCurrencyType.length; i++) {
			try {
				vectCurr.add(strCurrencyType[i]);
			}
			catch(Exception exc) {
				System.out.println("err");
			}
		 }
	 }

         Vector vectMember = new Vector(1,1);
	 String[] strMember = request.getParameterValues(FrmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_MEMBER_TYPE_ID]);
	 if(strMember!=null && strMember.length>0) {
		for(int i=0; i<strMember.length; i++) {
			try {
				vectMember.add(strMember[i]);
			}
			catch(Exception exc) {
				System.out.println("err");
			}
		}
	}
        srcMaterialRepostingStock.setMemberTypeId(vectMember);
         //end of add opsi currency Type nd memberType
        srcMaterialRepostingStock.setOidCodeRange(oidCodeRange);
        //session.putValue(SessMaterial.SESS_SRC_MATERIAL, srcMaterialRepostingStock);
        session.putValue(SessMaterialReposting.SESS_SRC_MATERIAL_REPOSTING, srcMaterialRepostingStock);
    }else{
        //srcMaterialRepostingStock = (SrcMaterialRepostingStock)session.getValue(SessMaterial.SESS_SRC_MATERIAL);
          srcMaterialRepostingStock = (SrcMaterialRepostingStock)session.getValue(SessMaterialReposting.SESS_SRC_MATERIAL_REPOSTING);
        srcMaterialRepostingStock.setOidCodeRange(oidCodeRange);
    }
}
//session.putValue(SessMaterial.SESS_SRC_MATERIAL, srcMaterialRepostingStock);
session.putValue(SessMaterialReposting.SESS_SRC_MATERIAL_REPOSTING, srcMaterialRepostingStock);




/**
 * get vectSize, start and data to be display in this page
 */
//vectSize = sessMaterial.getCountSearch(srcMaterialRepostingStock);
  vectSize = sessMaterialReposting.getCountSearchRepostingStok(srcMaterialRepostingStock);
//vectSize = sessMaterial.getCountSearchWithMultiSupp(srcMaterial);
if(iCommand==Command.FIRST || iCommand==Command.NEXT || iCommand==Command.PREV ||
        iCommand==Command.LAST || iCommand==Command.LIST){
    start = ctrlMaterial.actionList(iCommand,start,vectSize,recordToGet);
}
//Vector records = sessMaterial.searchMaterialRepostingStock(srcMaterialRepostingStock,start,recordToGet);
Vector records = sessMaterialReposting.searchMaterialRepostingStock(srcMaterialRepostingStock,start,recordToGet);
//Vector records = sessMaterial.searchMaterialWithMultiSupp(srcMaterial,start,recordToGet);


String vendorName = "";
if(srcMaterialRepostingStock.getSupplierId()>0){
    try{
        ContactList contact = new ContactList();
        contact = PstContactList.fetchExc(srcMaterialRepostingStock.getSupplierId());
        vendorName = ", Supplier : "+contact.getCompName();
    }catch(Exception e){}
}

/**
 * ini di gunakan untuk mencari range code
 */
Vector recordsCodeRange = PstCodeRange.list(0,0,"",PstCodeRange.fieldNames[PstCodeRange.FLD_FROM_RANGE_CODE]);

Vector val_locationid1 = new Vector(1,1);
Vector key_locationid1 = new Vector(1,1);
String locWhereClause = "";
if(index ==0 ){
	locWhereClause = PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE;
}else{
	locWhereClause = PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE;
}
String locOrderBy = PstLocation.fieldNames[PstLocation.FLD_NAME];
//Vector vt_loc1 = PstLocation.list(0,0,locWhereClause,locOrderBy);
Vector vt_loc1 = PstLocation.list(0,0,"",locOrderBy);
for(int d=0;d<vt_loc1.size();d++) {
	Location loc = (Location)vt_loc1.get(d);
	val_locationid1.add(""+loc.getOID()+"");
	key_locationid1.add(loc.getName());
}

%>
<!-- End of Jsp Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>

<style>
  .tool-tip{
    color:#444444;
    width:250px;
    z-index:13000;
    }
    
    .tool-title{
    font-weight:normal;
    font-size:16px;
    font-family:Georgia, "Times New Roman", Times, serif;
    margin:0;
    color:#343434;
    padding:8px 8px 5px 8px;
    background:url(images/tips-trans.png) top left;
    text-align:left;
    }
    .tool-text{
    font-size:12px;
    padding:0 8px 8px 8px;
    background:url(images/tips-trans.png) bottom right;
    text-align:left;
    } 
    </style>
    
<script language="JavaScript">
function cmdAdd()
{
    document.frm_material.start.value=0;
    document.frm_material.approval_command.value="<%=Command.SAVE%>";			
    document.frm_material.command.value="<%=Command.ADD%>";
    document.frm_material.add_type.value="<%=ADD_TYPE_LIST%>";			
    document.frm_material.action="material_main_reposting_stock.jsp";
    document.frm_material.submit();
}

function cmdEdit(oid)
{
    document.frm_material.hidden_material_id.value=oid;
    document.frm_material.start.value=0;
    document.frm_material.approval_command.value="<%=Command.APPROVE%>";					
    document.frm_material.command.value="<%=Command.EDIT%>";
    document.frm_material.action="material_main_reposting_stock.jsp";
    document.frm_material.submit();
}

function cmdEditSearch(oid){
document.frm_material.hidden_type.value = "1";
    document.frm_material.hidden_range_code_id.value=oid;
    document.frm_material.approval_command.value="<%=Command.FIRST%>";
    document.frm_material.command.value="<%=Command.LIST%>";
    document.frm_material.action="material_list_reposting_stock.jsp";
    document.frm_material.submit();
}


function cmdListFirst()
{
    document.frm_material.command.value="<%=Command.FIRST%>";
    document.frm_material.action="material_list_reposting_stock.jsp";
    document.frm_material.submit();
}

function cmdListPrev()
{
    document.frm_material.command.value="<%=Command.PREV%>";
    document.frm_material.action="material_list_reposting_stock.jsp";
    document.frm_material.submit();
}

function cmdListNext()
{
    document.frm_material.command.value="<%=Command.NEXT%>";
    document.frm_material.action="material_list_reposting_stock.jsp";
    document.frm_material.submit();
}

function cmdListLast()
{
    document.frm_material.command.value="<%=Command.LAST%>";
    document.frm_material.action="material_list_reposting_stock.jsp";
    document.frm_material.submit();
}

function cmdBack()
{
    document.frm_material.command.value="<%=Command.BACK%>";
    document.frm_material.action="srcmaterial_reposting_stock.jsp";
    document.frm_material.submit();
}

function cmdPrintAll(){
    document.frm_material.command.value="<%=Command.LIST%>";
    document.frm_material.target = "print_catalog";
    document.frm_material.action="material_list_print.jsp";
    document.frm_material.submit();
}

function cmdPrintAllWithoutPrice(){
    document.frm_material.command.value="<%=Command.LIST%>";
    document.frm_material.target = "print_catalog";
    document.frm_material.action="material_list_print_without_price.jsp";
    document.frm_material.submit();
}
o
function printForm() {
    window.open("<%=approot%>/servlet/com.dimata.posbo.report.masterdata.PrintAllListWoPricePdf?tms=<%=System.currentTimeMillis()%>&","sale","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
	//window.open("reportsaleinvoice_form_print.jsp","stockreport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
}


function cmdProsesReposting() {

      document.frm_material.command.value="<%=Command.LIST%>";
      document.frm_material.action="proses_reposting.jsp";
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
<%if(menuUsed == MENU_ICON){%>
    <link href="../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
<%}%>
<link rel="stylesheet" href="../../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" --> 
<link rel="stylesheet" href="../../styles/tab.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "headerscript" -->

 <link rel="stylesheet" type="text/css" href="../../resources/rich_calendar/rich_calendar.css">
	<script type="text/javascript" src="../../resources/rich_calendar/rich_calendar.js"></script>

	<script type="text/javascript" src="../../resources/rich_calendar/rc_lang_en.js"></script>
	<script type="text/javascript" src="../../resources/rich_calendar/rc_lang_ru.js"></script>
	<script type="text/javascript" src="../../resources/domready/domready.js"></script>

         <script type="text/javascript" src="../../resources/iMask/mootools.js"></script>
         <script type="text/javascript" src="../../resources/iMask/moodalbox.js"></script>
         <script type="text/javascript" src="../../resources/iMask/shCore.js"></script>
         <script type="text/javascript" src="../../resources/iMask/shBrushXml.js"></script>
         <script type="text/javascript" src="../../resources/iMask/shBrushJScript.js"></script>
         <script type="text/javascript" src="../../resources/iMask/imask-full.js"></script>
         
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

//::::::::::::::::::::::::::::::::::::::::function for calendar

            var format = '%dd-%m-%Y';
            var cal_obj2_start = null;
            var cal_obj2_end = null;

            // show calendar
            function show_cal_start(el) {

                if (cal_obj2_start) return;

                var text_field = document.getElementById("text_field");

                cal_obj2_start = new RichCalendar();
                cal_obj2_start.start_week_day = 0;
                cal_obj2_start.show_time = false;
                cal_obj2_start.language = 'en';
                cal_obj2_start.user_onchange_handler = cal2_on_change_start;
                cal_obj2_start.user_onclose_handler = cal2_on_close_start;
                cal_obj2_start.user_onautoclose_handler = cal2_on_autoclose_start;

                cal_obj2_start.parse_date(text_field.value, format);

                cal_obj2_start.show_at_element(text_field, "adj_left-bottom");
                //cal_obj2_start.change_skin('alt');

            }
            function show_cal_end(el) {

                if (cal_obj2_end) return;

                var text_field = document.getElementById("text_field1");

                cal_obj2_end = new RichCalendar();
                cal_obj2_end.start_week_day = 0;
                cal_obj2_end.show_time = false;
                cal_obj2_end.language = 'en';
                cal_obj2_end.user_onchange_handler = cal2_on_change_end;
                cal_obj2_end.user_onclose_handler = cal2_on_close_end;
                cal_obj2_end.user_onautoclose_handler = cal2_on_autoclose_end;

                cal_obj2_end.parse_date(text_field.value, format);

                cal_obj2_end.show_at_element(text_field, "adj_left-bottom");
                //cal_obj2_start.change_skin('alt');

            }

            // user defined onchange handler
            function cal2_on_change_start(cal, object_code) {
                if (object_code == 'day') {
                    document.getElementById("text_field").value = cal.get_formatted_date(format);
                    cal.hide();
                    cal_obj2_start = null;
                }
            }
            function cal2_on_change_end(cal, object_code) {
                if (object_code == 'day') {
                    document.getElementById("text_field1").value = cal.get_formatted_date(format);
                    cal.hide();
                    cal_obj2_end = null;
                }
            }

            // user defined onclose handler (used in pop-up mode - when auto_close is true)
            function cal2_on_close_start(cal) {
                if (window.confirm('Are you sure to close the calendar?')) {
                    cal.hide();
                    cal_obj2_start = null;
                }
            }
            function cal2_on_close_end(cal) {
                if (window.confirm('Are you sure to close the calendar?')) {
                    cal.hide();
                    cal_obj2_end = null;
                }
            }

            // user defined onautoclose handler
            function cal2_on_autoclose_start(cal) {
                cal_obj2_start = null;
            }
            function cal2_on_autoclose_end(cal) {
                cal_obj2_end = null;
            }

</SCRIPT>
<!-- #EndEditable --> 
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">   
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
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
              <input type="hidden" name="start2" value="<%=start2%>">
              <input type="hidden" name="hidden_material_id" value="<%=oidMaterial%>">
              <input type="hidden" name="hidden_range_code_id" value="<%=oidMaterial%>">
              <input type="hidden" name="hidden_type" value="<%=type%>">
              <input type="hidden" name="approval_command">
              
                <table width="100%" cellspacing="0" cellpadding="3">
                    
                     <tr>
                        <td height="21" width="5%"><b><%=textListHeaderReposting[SESS_LANGUAGE][0]%></b></td>
                        <td height="21" width="1%">:</td>
                        <td height="21" width="84%"><%=ControlCombo.draw(frmSrcMaterialRepostingStock.fieldNames[FrmSrcMaterialRepostingStock.FRM_FIELD_LOCATIONID], null, ""+srcMaterialRepostingStock.getLocationId(), val_locationid1, key_locationid1, "", "formElemen")%> </td>
                      </tr>
                      
                      <tr>
                          <td height="21" width="5%"><b>Periode</b> &nbsp;</td>
                        <td height="21" width="1%">:</td>
                                               <td>

                                                            

                                                            <%
                                                                String addedComp = "<a onclick=\"show_calendar_" + FrmSrcMaterialRepostingStock.fieldNames[FrmSrcMaterialRepostingStock.FRM_FIELD_DATE_FROM] + "(this);\"><img src=\"../../resources/images/calendar.png\" alt=\"Calendar\"></a>";
                                                                String addStyle="alt=\"{type:'fixed',mask:'99-99-9999',stripMask: false}\"";
                                                                
                                                            %>
                                                            <input id="<%=FrmSrcMaterialRepostingStock.fieldNames[FrmSrcMaterialRepostingStock.FRM_FIELD_DATE_FROM]%>" type="text" value="<%=Formater.formatDate(srcMaterialRepostingStock.getDateFrom(),"dd-MM-yyyy", "dd-mm-yyyy")%>" name="<%=FrmSrcMaterialRepostingStock.fieldNames[FrmSrcMaterialRepostingStock.FRM_FIELD_DATE_FROM]%>" size="10" maxlength="10" class="iMask" alt="<%=addStyle%>">
                                                            <%=addedComp%><%=drawCalendarJS(FrmSrcMaterialRepostingStock.fieldNames[FrmSrcMaterialRepostingStock.FRM_FIELD_DATE_FROM])%>

                                                            &nbsp; <b>s/d</b> &nbsp;
                                                            <% addedComp = "<a onclick=\"show_calendar_" + FrmSrcMaterialRepostingStock.fieldNames[FrmSrcMaterialRepostingStock.FRM_FIELD_DATE_TO] + "(this);\"><img src=\"../../resources/images/calendar.png\" alt=\"Calendar\"></a>"; %>

                                                            <input id="<%=FrmSrcMaterialRepostingStock.fieldNames[FrmSrcMaterialRepostingStock.FRM_FIELD_DATE_TO]%>" type="text" value="<%=Formater.formatDate(srcMaterialRepostingStock.getDateTo(),"dd-MM-yyyy", "dd-mm-yyyy")%>" name="<%=FrmSrcMaterialRepostingStock.fieldNames[FrmSrcMaterialRepostingStock.FRM_FIELD_DATE_TO]%>" size="10" maxlength="10" class="iMask" alt="<%=addStyle%>">
                                                            <%=addedComp%><%=drawCalendarJS(FrmSrcMaterialRepostingStock.fieldNames[FrmSrcMaterialRepostingStock.FRM_FIELD_DATE_TO])%>

                                                            </td>
                                                        </tr>
                
                  <tr align="left" valign="top">
                    <td height="22" valign="middle" colspan="3"><%=drawList(SESS_LANGUAGE,records,start,srcMaterialRepostingStock.getShowImage(),approot, srcMaterialRepostingStock.getShowUpdateCatalog(), srcMaterialRepostingStock, start2, srcMaterialRepostingStock.getShowDiscountQty(), srcMaterialRepostingStock.getShowHpp(), srcMaterialRepostingStock.getShowCurrency())%></td>
                  </tr>
                  <tr align="left" valign="top">
                    <td height="8" align="left" colspan="3" class="command"> 
                      <span class="command"> 
                      <%
                            ctrLine.setLocationImg(approot+"/images");
                            ctrLine.initDefault();
                            out.println(ctrLine.drawImageListLimit(iCommand,vectSize,start,recordToGet));
                      %>
                      </span>
                    </td>
                  </tr>
                 
                  <tr align="left" valign="top">
                    <td height="18" valign="top" colspan="3"> 
                       <table width="91%" border="0" cellspacing="0" cellpadding="0">
                         <tr>
                           
                           <!--td width="4%" nowrap><a href="javascript:cmdProsesReposting()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,dfTitle,ctrLine.CMD_ADD,true)%>"></a></td-->
                           <td width="11%" nowrap class="command"><a class="btn btn-primary" href="javascript:cmdProsesReposting()"><i class="fa fa-play"></i> Reposting stock</a></td>
                           <!--td width="5%" nowrap><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnBackOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,dfTitle,ctrLine.CMD_BACK_SEARCH,true)%>"></a></td-->
                           <td width="80%" nowrap class="command"><a class="btn btn-primary" href="javascript:cmdBack()"><i class="fa fa-arrow-left"></i> <%=ctrLine.getCommand(SESS_LANGUAGE,dfTitle,ctrLine.CMD_BACK_SEARCH,true)%></a></td>
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
            <%@include file="../../styletemplate/footer.jsp" %>
        <%}else{%>
            <%@ include file = "../../main/footer.jsp" %>
        <%}%>
      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>

<%-- 
    Document   : src_update_harga
    Created on : Feb 28, 2014, 10:06:32 AM
    Author     : Fitra
--%>

<%@ page language = "java" %>
<%@ page import="com.dimata.util.Command,
                 com.dimata.gui.jsp.ControlCombo,
                 com.dimata.common.entity.contact.PstContactClass,
                 com.dimata.common.entity.contact.PstContactList,
                 com.dimata.common.entity.contact.ContactList,
                 com.dimata.gui.jsp.ControlDate,
                 com.dimata.posbo.entity.admin.PstAppUser,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.posbo.entity.search.SrcMaterial,
                 com.dimata.posbo.form.search.FrmSrcMaterial,
                 com.dimata.posbo.session.masterdata.SessMaterial,
                 com.dimata.posbo.entity.masterdata.*,
                 com.dimata.gui.jsp.ControlList,
                 com.dimata.gui.jsp.ControlCheckBox,
                 com.dimata.common.entity.payment.*,
                 com.dimata.common.entity.payment.PstDiscountType,
                 java.util.Vector,com.dimata.common.entity.location.*"%>

<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_CATALOG); %>
<%@ include file = "../../main/checkuser.jsp" %>


<%!

public static final String textListMaterialHeader[][] = {
    {"No","Kode","Barcode","Nama Barang","Jenis","Tipe","Hpp","Hrg Jual","Kategori","Merk","Kode Grup"},
    {"No","Code","Barcode","Name","Jenis","Type","Average","Price","Category","Merk","Code Range"}
};

public String drawCodeRangeList(int language,Vector objectClass) {
    String result = "";
    if(objectClass!=null && objectClass.size()>0){
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("20%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.addHeader(textListMaterialHeader[language][10],"3%");
        //ctrlist.addHeader(textListMaterialHeader[language][1],"10%");
        
        ctrlist.setLinkRow(-1);
        ctrlist.setLinkSufix("");
        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        ctrlist.setLinkPrefix("javascript:cmdsearchcode('");
        ctrlist.setLinkSufix("')");
        ctrlist.reset();
        Vector rowx = new Vector();
        for(int i=0; i<objectClass.size(); i++){
            CodeRange codeRange = (CodeRange)objectClass.get(i);
            rowx = new Vector();
            rowx.add("<a href=\"javascript:cmdsearchcode('"+codeRange.getOID()+"')\">"+codeRange.getFromRangeCode()+"</a>");
            lstData.add(rowx);
        }
        result = ctrlist.draw();
    }
    return result;
}
 %>
<%!
public static final String textListTitleHeader[][] = {
   //            0         1      2     3              4       5        6                7
    {"Pencarian Barang","Kode","Nama","Supplier","Kategori","Tipe","Urut Berdasar","Cari Barang",
             //8                       9          10          11                12             13        14   
             "Tambah Barang Baru","Tipe Barang","Merk","Tampilkan Gambar","Refresh Catalog","Group","Semua Group","Locakasi Jual"},
    {"Goods Searching","Code","Name","Supplier","Group","Tipe","Sort By","Goods Search",
             "Add New Goods","Goods Type","Category","Show Picture","Refresh Catalog","Group","All Group","Sell Location"}
};

public static final String typeItemNames[][] = {
    {"Semua Tipe Barang","Hadiah"},
    {"All","Gift"}
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
<%
int iCommand = FRMQueryString.requestCommand(request);

long oidMaterial = FRMQueryString.requestLong(request, "hidden_material_id");

//updated by dewok 2018-02-07
String kodeAwal = FRMQueryString.requestString(request, "FRM_KODE_START");
String kodeAkhir = FRMQueryString.requestString(request, "FRM_KODE_END");
int tipeItem = FRMQueryString.requestInt(request, "tipe_item");

SrcMaterial srcMaterial = new SrcMaterial();
FrmSrcMaterial frmSrcMaterial = new FrmSrcMaterial();
try {
    srcMaterial = (SrcMaterial)session.getValue(SessMaterial.SESS_SRC_MATERIAL_EDIT_ALL);
} catch(Exception e) {
    srcMaterial = new SrcMaterial();
}
long oidPrice = FRMQueryString.requestLong(request, frmSrcMaterial.fieldNames[frmSrcMaterial.FRM_FIELD_PRICE_TYPE_ID]);  
// Vector vectSubCategory = PstSubCategory.listAll();   
   
if(srcMaterial==null)
    srcMaterial = new SrcMaterial();

try {
    session.removeValue(SessMaterial.SESS_SRC_MATERIAL_EDIT_ALL);
}catch(Exception e){}

Vector recordsCodeRange = PstCodeRange.list(0,0,"",PstCodeRange.fieldNames[PstCodeRange.FLD_FROM_RANGE_CODE]);
merkName = PstSystemProperty.getValueByName("NAME_OF_MERK");
String useSubCategory=PstSystemProperty.getValueByName("USE_SUB_CATEGORY");
%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">

function cmdAdd() {
    document.frmsrcmaterial.hidden_material_id.value="0";
    document.frmsrcmaterial.command.value="<%=Command.ADD%>";
    document.frmsrcmaterial.action="material_main.jsp";
    document.frmsrcmaterial.submit();
}

function cmdSearch() {
    
    //var textSubKategori = document.frmsrcmaterial.txt_subcategory.value;
    //if (textSubKategori == "")
    //{
    //	document.frmsrcmaterial.<%=FrmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_SUBCATEGORYID]%>.value = "0";
    //	}
    document.frmsrcmaterial.command.value="<%=Command.LIST%>";
    document.frmsrcmaterial.action="list_print_barcode.jsp";
    document.frmsrcmaterial.submit();
}

function cmdsearchcode(oidcode) {
    document.frmsrcmaterial.<%=FrmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_CODE_RANGE_ID]%>.value = oidcode;
    //alert(document.frmsrcmaterial.<%=FrmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_CODE_RANGE_ID]%>.value);
    document.frmsrcmaterial.command.value="<%=Command.LIST%>";
    document.frmsrcmaterial.action="list_print_barcode.jsp?hidden_range_code_id="+oidcode+"";
    document.frmsrcmaterial.submit();
}

function cmdSelectSubCategory() {
    window.open("subcategorydosearch.jsp?command=<%=Command.FIRST%>&txt_subcategory="+document.frmsrcmaterial.txt_subcategory.value+
                    "&caller=1"+
                    "&oidCategory="+document.frmsrcmaterial.<%=FrmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_CATEGORYID]%>.value,"material", "height=600,width=800,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes");
}

function fnTrapKD(evt){
   if (evt.keyCode == 13) {
        document.all.aSearch.focus();
        cmdSearch();
   }
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
<script type="text/javascript" src="../../styles/jquery-1.4.2.min.js"></script>
<script src="../../styles/jquery.autocomplete.js"></script>
<link rel="stylesheet" type="text/css" href="../../styles/style.css" />
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
  <%if(menuUsed == MENU_PER_TRANS || MODUS_TRANSFER==MODUS_TRANSFER_KASIR){%>
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
            <form name="frmsrcmaterial" method="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="hidden_material_id" value="<%=oidMaterial%>">
              <input type="hidden" name="<%=FrmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_SUBCATEGORYID]%>"  value="">
           
              
              <input type="hidden" name="<%=FrmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_CODE_RANGE_ID]%>"  value="-1" >
              <input type="hidden" name="<%=frmSrcMaterial.fieldNames[frmSrcMaterial.FRM_FIELD_PRICE_TYPE_ID]%>"  value="" >
              <table width="100%" border="0" cellspacing="2" cellpadding="2">
                <tr align="left">
                  <td height="21">&nbsp;</td>
                  <td height="21" valign="top">
                  <td height="21" valign="top">
                <%if(typeOfBusinessDetail == 2) {%>
                <tr>
                    <td>Rentang Kode</td>
                    <td>:</td>
                    <td>
                        <input tabindex="1" type="text" placeholder="Kode awal" name="FRM_KODE_START"  value="<%=kodeAwal%>" class="formElemen" size="20">
                        &nbsp; s.d. &nbsp;
                        <input tabindex="1" type="text" placeholder="Kode akhir" name="FRM_KODE_END"  value="<%=kodeAkhir%>" class="formElemen" size="20">
                    </td>
                </tr>
                <%}%>
                <tr align="left">
                  <td height="21" width="13%"> <%=textListTitleHeader[SESS_LANGUAGE][1]%></td>
                  <td height="21" width="2%" valign="">: 
                  <td height="21" width="40%" valign="">                                                                     
                    <input tabindex="1" type="text" name="<%=frmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_MATCODE] %>"  value="<%= srcMaterial.getMatcode() %>" class="formElemen" size="20" onKeyDown="javascript:fnTrapKD(event)">                      
                <tr align="left"> 
                  <td height="21" width="13%"> <%=textListTitleHeader[SESS_LANGUAGE][2]%></td>
                  <td height="21" width="2%" valign="">: </td>
                  <td height="21" width="40%" valign=""> 
                    <input tabindex="2" type="text" name="<%=frmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_MATNAME] %>"  value="<%= srcMaterial.getMatname() %>" class="formElemen" size="40" onKeyDown="javascript:fnTrapKD(event)" id="txt_materialname" >
                  </td>
                    <% ControlCheckBox controlCheckBox = new ControlCheckBox(); %>
                <%if(typeOfBusinessDetail == 2) {%>
                <tr>
                    <td>Tipe Item</td>
                    <td>:</td>
                    <td>
                        <select class="formElemen" name="tipe_item">
                            <%
                            for (int main_material = 0; main_material < Material.MATERIAL_TYPE_TITLE.length; main_material++) {                                    
                                if (main_material == Material.MATERIAL_TYPE_GENERAL) {continue;}
                            %>                            
                            <option <%=tipeItem == main_material ? "selected" : "" %> value="<%=main_material%>"><%=Material.MATERIAL_TYPE_TITLE[main_material]%></option>
                            <%
                                }
                            %>
                        </select>
                    </td>
                </tr>
                <%}%>
                <tr align="left"> 
                  <td height="21" width="13%"> <%=textListTitleHeader[SESS_LANGUAGE][4]%></td>
                  <td height="21" width="2%" valign="">:</td>
                  <td height="21" width="40%" valign=""> 
                  <select id="<%=frmSrcMaterial.fieldNames[frmSrcMaterial.FRM_FIELD_CATEGORYID]%>"  name="<%=frmSrcMaterial.fieldNames[frmSrcMaterial.FRM_FIELD_CATEGORYID]%>" class="formElemen">
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
                        vectGroupKey.add("-1");
                    }
                  %>
                  </select>
                <%if(useSubCategory.equals("1")){%>
                    <tr align="left"> 
                      <td height="21" width="13%">Sub Categori </td>
                      <td height="21" width="2%" valign="">:</td>
                      <td height="21" width="40%" valign=""> 
                      <%
                           String orderSubBy = PstSubCategory.fieldNames[PstSubCategory.FLD_NAME];
                            Vector listMaterialSubcategory = PstSubCategory.list(0,0,"",orderSubBy);
                            Vector vectSubCatVal = new Vector(1,1);
                            Vector vectSubCatKey = new Vector(1,1);
                            vectSubCatVal.add(" ");
                            vectSubCatKey.add("0");
                            if(listMaterialSubcategory!=null && listMaterialSubcategory.size()>0) {
                                for(int i=0; i<listMaterialSubcategory.size(); i++) {
                                    SubCategory SubCategory = (SubCategory)listMaterialSubcategory.get(i);
                                    vectSubCatVal.add(SubCategory.getName());
                                    vectSubCatKey.add(""+SubCategory.getOID());
                                }
                            }
                           out.println(ControlCombo.draw(frmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_SUBCATEGORYID],"formElemen", null, ""+srcMaterial.getSubCategoryId(), vectSubCatKey, vectSubCatVal, " tabindex=\"5\" onkeydown=\"javascript:fnTrapKD(event)\""));
                      %>
                 <%}%> 
                <tr align="left"> 
                  <td height="21" width="13%"> <%=merkName%><%//=textListTitleHeader[SESS_LANGUAGE][10]%></td>
                  <td height="21" width="2%" valign="">:</td>
                  <td height="21" width="40%" valign=""> 
                  <%
                  //Vector materGroup = PstCategory.list(0,0,"",PstCategory.fieldNames[PstCategory.FLD_CODE]);
                  materGroup = PstMerk.list(0,0,PstMerk.fieldNames[PstMerk.FLD_MERK_ID]+"!=0",PstMerk.fieldNames[PstMerk.FLD_NAME]);
                  vectGroupVal = new Vector(1,1);
                  vectGroupKey = new Vector(1,1);
                  vectGroupVal.add(" ");
                  vectGroupKey.add("-1");
                  if(materGroup!=null && materGroup.size()>0) {
                      int maxGrp = materGroup.size();
                      for(int i=0; i<maxGrp; i++) {
                          Merk mGroup = (Merk)materGroup.get(i);
                          vectGroupVal.add(mGroup.getCode()+" - "+mGroup.getName());
                          vectGroupKey.add(""+mGroup.getOID());
                      }
                  } else {
                      vectGroupVal.add("Tidak Ada Merk");
                      vectGroupKey.add("0");
                  }
                  out.println(ControlCombo.draw(frmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_MERK_ID],"formElemen", null, ""+srcMaterial.getMerkId(), vectGroupKey, vectGroupVal, " tabindex=\"5\" onkeydown=\"javascript:fnTrapKD(event)\""));
                  %>
                  <td>
                  </td>
                  <tr align="left">
                  <td height="21" valign="">Kode Gondola</td>
                  <td height="21" valign="">:</td>
                  <td height="21" valign="">
                        <%
                        String whereKsg = "";
                        Vector listKsg = PstKsg.list(0,0,whereKsg,PstKsg.fieldNames[PstKsg.FLD_CODE]);
                        Vector vectKsgVal = new Vector(1,1);
                        Vector vectKsgKey = new Vector(1,1);
                        vectKsgKey.add("Semua");
                        vectKsgVal.add("0");
                        for(int i=0; i<listKsg.size(); i++){
                            Ksg matKsg = (Ksg)listKsg.get(i);
                            vectKsgKey.add(""+matKsg.getCode()+" - "+matKsg.getName());
                            vectKsgVal.add(""+matKsg.getOID());
                        }
                        %> <%=ControlCombo.draw(frmSrcMaterial.fieldNames[frmSrcMaterial.FRM_FIELD_GONDOLA_CODE],"formElemen", null, ""+srcMaterial.getGondolaId(), vectKsgVal, vectKsgKey, "onChange=\"javascript:changeUnit()\"")%>                                    </td>

                 </td>
                </tr>
		<tr align="left">
                  <td height="21" valign=""><%=textListTitleHeader[SESS_LANGUAGE][13]%></td>
                  <td height="21" valign="">:</td>
                  <td height="21" valign="">
                      <%
                  Vector vectGroupMatVal = new Vector(1,1);
                  Vector vectGroupMatKey = new Vector(1,1);
                  
                  vectGroupMatVal.add(textListTitleHeader[SESS_LANGUAGE][14]);
                  vectGroupMatKey.add("-1");

                  vectGroupMatVal.add(PstMaterial.strMaterialType[SESS_LANGUAGE][PstMaterial.MAT_TYPE_REGULAR]);
                  vectGroupMatKey.add(""+PstMaterial.MAT_TYPE_REGULAR);
                  
                  vectGroupMatVal.add(PstMaterial.strMaterialType[SESS_LANGUAGE][PstMaterial.MAT_TYPE_SERVICE]);
                  vectGroupMatKey.add(""+PstMaterial.MAT_TYPE_SERVICE);
                  
                  vectGroupMatVal.add(PstMaterial.strMaterialType[SESS_LANGUAGE][PstMaterial.MAT_TYPE_COMPOSITE]);
                  vectGroupMatKey.add(""+PstMaterial.MAT_TYPE_COMPOSITE);
                  out.println(ControlCombo.draw(frmSrcMaterial.fieldNames[frmSrcMaterial.FRM_FIELD_GROUP_ITEM],"formElemen", null, ""+srcMaterial.getGroupItem(), vectGroupMatKey, vectGroupMatVal, " tabindex=\"7\" onkeydown=\"javascript:fnTrapKD(event)\""));
                  %>
                 
                  <tr align="left"> 
                  <td height="21" width="13%"> <%=textListTitleHeader[SESS_LANGUAGE][3]%></td>
                  <td height="21" width="2%" valign="">:</td>
                  <td height="21" width="40%" valign=""> 
                    <% 
                    Vector obj_supplier = new Vector(1,1);
                    Vector val_supplier = new Vector(1,1);
                    Vector key_supplier = new Vector(1,1);
                    val_supplier.add("-1");
                    key_supplier.add(" ");
                    //val_supplier.add("0");
                    //key_supplier.add("No Supplier");
                    //Vector vt_supp = PstContactList.list(0,0,"",PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE]);
                    String wh_supp = PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE]+
                            " = "+PstContactClass.CONTACT_TYPE_SUPPLIER+" AND "+PstContactList.fieldNames[PstContactList.FLD_PROCESS_STATUS]+
                            " != "+PstContactList.DELETE;
                    Vector vt_supp = PstContactList.listContactByClassType(0,0,wh_supp,PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);
                    if(vt_supp!=null && vt_supp.size()>0){
                        int maxSupp = vt_supp.size();
                        for(int d=0; d<maxSupp; d++){
                            ContactList cnt = (ContactList)vt_supp.get(d);
                            String cntName = cnt.getCompName();
                            val_supplier.add(String.valueOf(cnt.getOID()));
                            key_supplier.add(cntName);
                        }
                    }
                    String select_supplier = ""+srcMaterial.getSupplierId();
                    %>
                    <%=ControlCombo.draw(frmSrcMaterial.fieldNames[frmSrcMaterial.FRM_FIELD_SUPPLIERID],"formElemen",null,select_supplier,val_supplier,key_supplier," tabindex=\"3\" onkeydown=\"javascript:fnTrapKD(event)\"")%> 
                  </td>
                  <tr align="left">
                  <td height="21" valign="">Stock Location</td>
                  <td height="21" valign="">:</td>
                  <td>
                       <%
                                //add opie-eyek 20130805
                                //String ordPrice = PstPriceType.fieldNames[PstPriceType.FLD_INDEX]; 
                                Vector listLocation = PstLocation.list(0,0,"","");
                                Vector locationVal = new Vector(1,1);
                                Vector locationKey = new Vector(1,1);
                                for(int i=0;i<listLocation.size();i++) {
                                        Location location = (Location)listLocation.get(i);
					locationVal.add(""+location.getOID()+"");
					locationKey.add(""+location.getName()+"");
				}
                              controlCheckBox.setWidth(5);
                              out.println(ControlCombo.draw(frmSrcMaterial.fieldNames[frmSrcMaterial.FRM_FIELD_LOCATIONID],"formElemen", null, ""+srcMaterial.getLocationId(), locationVal, locationKey, " tabindex=\"9\" onkeydown=\"javascript:fnTrapKD(event)\""));
                          %>
                  </td>
                </tr>
                <tr align="left"> 
                  <td height="9" width="13%">Sort By</td>
                  <td height="9" width="2%" valign="">:</td>
                  <td height="9" width="40%" valign=""> 
                  <%
                  Vector vectSortVal = new Vector(1,1);
                  Vector vectSortKey = new Vector(1,1);
                  
                  vectSortVal.add(textListTitleHeader[SESS_LANGUAGE][1]);
                  vectSortKey.add("0");
                  
                  vectSortVal.add(textListTitleHeader[SESS_LANGUAGE][4]);
                  vectSortKey.add("1");
                  
                  vectSortVal.add(textListTitleHeader[SESS_LANGUAGE][2]);
                  vectSortKey.add("2");
                  
                  vectSortVal.add("Barcode");
                  vectSortKey.add("3");
                  
                  out.println(ControlCombo.draw(frmSrcMaterial.fieldNames[frmSrcMaterial.FRM_FIELD_SORTBY],"formElemen", null, ""+srcMaterial.getSortby(), vectSortKey, vectSortVal, " tabindex=\"6\" onkeydown=\"javascript:fnTrapKD(event)\""));
                  %>
                  </td>
                </tr>
                  <tr align="left">
                  <td height="21" valign="">Tipe Harga</td>
                  <td height="21" valign="">:</td>
                  <td height="21" valign="">
                      <%
                                //add opie-eyek 20130805
                                String ordPrice = PstPriceType.fieldNames[PstPriceType.FLD_INDEX]; 
                                Vector listPriceType = PstPriceType.list(0,0,"",ordPrice);
                                 Vector prTypeVal = new Vector(1,1);
                                 Vector prTypeKey = new Vector(1,1);
                                for(int i=0;i<listPriceType.size();i++) {
                                        PriceType prType = (PriceType)listPriceType.get(i);
					prTypeVal.add(""+prType.getOID()+"");
					prTypeKey.add(""+prType.getCode()+"");
				}
                              controlCheckBox.setWidth(5);
                              
                          %> 
                          <%=controlCheckBox.draw(frmSrcMaterial.fieldNames[frmSrcMaterial.FRM_FIELD_PRICE_TYPE_ID], prTypeVal, prTypeKey, new Vector())%>
                  </td>
                </tr>
                  </td>
                </tr>
                <% if(typeOfBusiness.equals("3")){%>
                    <tr align="left">
                      <td height="21" valign="">Show Use Serial Number</td>
                      <td height="21" valign="">:</td>
                      <td><input type="checkbox"  class="formElemen" name="<%=frmSrcMaterial.fieldNames[frmSrcMaterial.FRM_FIELD_REQUIRED_SERIAL_NUMBER]%>"  value="1">
                      </td>
                    </tr>
                <%} %>
                  
                <% 
                String designMat = String.valueOf(PstSystemProperty.getValueByName("DESIGN_MATERIAL_FOR"));
                int DESIGN_MATERIAL_FOR = Integer.parseInt(designMat);
                Vector selected = new Vector(1,1);
                if (DESIGN_MATERIAL_FOR == 1) { %>
                    <tr align="left"> 
                      <td height="21" valign="" width="13%"><%=textListTitleHeader[SESS_LANGUAGE][15]%></td>
                      <td height="21" width="2%" valign="">:</td>
                      <td height="21" width="40%" valign="">
                           <%
                                //add opie-eyek 20130805
                                 Vector sttRsv = PstLocation.list(0,0, PstLocation.fieldNames[PstLocation.FLD_TYPE]+"="+PstLocation.TYPE_LOCATION_STORE, null);
                                 Vector locationSellVal = new Vector(1,1);
                                 Vector locationSellKey = new Vector(1,1);
                                 for(int i=0;i<sttRsv.size();i++) {
                                        Location temp = (Location)sttRsv.get(i);
					locationSellVal.add(""+temp.getOID()+"");
					locationSellKey.add(""+temp.getName()+"");
				}
                              controlCheckBox.setWidth(5);
                              out.println(controlCheckBox.draw(frmSrcMaterial.fieldNames[frmSrcMaterial.FRM_FIELD_SELL_LOCATION],locationSellVal,locationSellKey,new Vector()));
                          %>
                      </td>
                    </tr>
                <% }%>
                
                <tr align="left"> 
                  <td height="21" valign="" width="13%">&nbsp;</td>
                  <td height="21" width="2%" valign="">&nbsp;</td>
                  <td height="21" width="40%" valign=""><%=drawCodeRangeList(SESS_LANGUAGE,recordsCodeRange)%></td>
                </tr>
                <tr align="left"> 
                  <td height="21" valign="" width="13%">&nbsp;</td>
                  <td height="21" width="2%" valign="">&nbsp;</td>
                  <td height="21" width="40%" valign=""> 
                    <table width="98%" border="0" cellspacing="0" cellpadding="0">
                      <tr> 
                        <!--td nowrap width="4%"><a href="javascript:cmdSearch()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnSearchOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24" alt="Cari Barang"></a></td-->
                        <td class="command" nowrap width="33%"><a class="btn btn-lg btn-primary" href="javascript:cmdSearch()"><i class="fa fa-search"></i> <%=textListTitleHeader[SESS_LANGUAGE][7]%></a></td>                        
                        <% if(privAdd){%>
                        <!--td nowrap width="4%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="Tambah Baru Barang"></a></td-->
                        <td class="command" nowrap width="59%"><a class="btn btn-lg btn-primary" href="javascript:cmdAdd()"><i class="fa fa-plus-circle"></i> <%=textListTitleHeader[SESS_LANGUAGE][8]%></a></td>
                        <%}%>
                      </tr>
                    </table>
                  </td>
                </tr>
              </table>
            <script language="javascript">
                    document.frmsrcmaterial.<%=frmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_MATCODE]%>.focus();
            </script>			  
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
<script>
	jQuery(function(){
		$("#txt_materialname").autocomplete("../../purchasing/material/pom/list.jsp");
	});
</script>
<!-- #EndTemplate --></html>

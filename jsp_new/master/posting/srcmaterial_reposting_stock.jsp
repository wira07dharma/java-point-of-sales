<%@page import="com.dimata.posbo.session.masterdata.SessMaterialReposting"%>
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
                 com.dimata.posbo.entity.search.*,
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
    {"Pencarian Barang","Kode","Nama","Supplier","Kategori","Tipe","Urut Berdasar","Cari Barang",
             "Tambah Barang Baru","Tipe Barang","Merk","Tampilkan Gambar","Refresh Catalog","Group","Semua Group"},
    {"Goods Searching","Code","Name","Supplier","Group","Tipe","Sort By","Goods Search",
             "Add New Goods","Goods Type","Category","Show Picture","Refresh Catalog","Group","All Group"}
};

public static final String typeItemNames[][] = {
    {"Semua Tipe Barang","Hadiah"},
    {"All","Gift"}
};
%>


<!-- Jsp Block -->
<%
int iCommand = FRMQueryString.requestCommand(request);

SrcMaterialRepostingStock srcMaterialRepostingStock = new SrcMaterialRepostingStock();
FrmSrcMaterial frmSrcMaterial = new FrmSrcMaterial();
try {
    //srcMaterialRepostingStock = (SrcMaterialRepostingStock)session.getValue(SessMaterial.SESS_SRC_MATERIAL);
      srcMaterialRepostingStock = (SrcMaterialRepostingStock)session.getValue(SessMaterialReposting.SESS_SRC_MATERIAL_REPOSTING);
} catch(Exception e) {
    srcMaterialRepostingStock = new SrcMaterialRepostingStock();
}

// Vector vectSubCategory = PstSubCategory.listAll();

if(srcMaterialRepostingStock==null)
    srcMaterialRepostingStock = new SrcMaterialRepostingStock();

try {
    //session.removeValue(SessMaterial.SESS_SRC_MATERIAL);
    session.removeValue(SessMaterialReposting.SESS_SRC_MATERIAL_REPOSTING);
}catch(Exception e){}

Vector recordsCodeRange = PstCodeRange.list(0,0,"",PstCodeRange.fieldNames[PstCodeRange.FLD_FROM_RANGE_CODE]);
//String merkName = PstSystemProperty.getValueByName("NAME_OF_MERK");
%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">

function cmdAdd() {
    document.frmsrcmatrepoststock.hidden_material_id.value="0";
    document.frmsrcmatrepoststock.command.value="<%=Command.ADD%>";
    document.frmsrcmatrepoststock.action="material_main.jsp";
    document.frmsrcmatrepoststock.submit();
}

function cmdSearch() {
    //var textSubKategori = document.frmsrcmaterial.txt_subcategory.value;
    //if (textSubKategori == "")
    //{
    //	document.frmsrcmaterial.<%=FrmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_SUBCATEGORYID]%>.value = "0";
    //	}
    document.frmsrcmatrepoststock.command.value="<%=Command.LIST%>";
    document.frmsrcmatrepoststock.action="material_list_reposting_stock.jsp";
    document.frmsrcmatrepoststock.submit();
}

function cmdsearchcode(oidcode) {
    document.frmsrcmatrepoststock.<%=FrmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_CODE_RANGE_ID]%>.value = oidcode;
    //alert(document.frmsrcmaterial.<%=FrmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_CODE_RANGE_ID]%>.value);
    document.frmsrcmatrepoststock.command.value="<%=Command.LIST%>";
    document.frmsrcmatrepoststock.action="material_list_reposting_stock.jsp?hidden_range_code_id="+oidcode+"";
    document.frmsrcmatrepoststock.submit();
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
                              <td>
                                   <%if(SESS_LANGUAGE==0){%>
                                        *) Notes : Reposting dilakukan untuk data dengan dokumen closed dan posted
                                   <%}else{%>
                                         *) Notes : Reposting only process  documents with status closed and posted
                                   <%}%>	  
                              </td>
                          </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frmsrcmatrepoststock" method="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="hidden_material_id" value="">
              <input type="hidden" name="<%=FrmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_SUBCATEGORYID]%>"  value="-1">
              <input type="hidden" name="<%=FrmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_CODE_RANGE_ID]%>"  value="-1" >
              <table width="100%" border="0" cellspacing="2" cellpadding="2">
                <tr align="left">
                  <td height="21">&nbsp;</td>
                  <td height="21" valign="top">
                  <td height="21" valign="top">
                <tr align="left">
                  <td height="21" width="12%"> <%=textListTitleHeader[SESS_LANGUAGE][1]%></td>
                  <td height="21" width="2%" valign="top">: 
                  <td height="21" width="86%" valign="top"> 
                    <input tabindex="1" type="text" name="<%=frmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_MATCODE] %>"  value="<%= srcMaterialRepostingStock.getMatcode() %>" class="formElemen" size="20" onKeyDown="javascript:fnTrapKD(event)">
                  </td>

   
                <tr align="left"> 
                  <td height="21" width="12%"> <%=textListTitleHeader[SESS_LANGUAGE][2]%></td>
                  <td height="21" width="2%" valign="top">: 
                  <td height="21" width="86%" valign="top"> 
                  <input tabindex="2" type="text" name="<%=frmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_MATNAME] %>"  value="<%= srcMaterialRepostingStock.getMatname() %>" class="formElemen" size="40" onKeyDown="javascript:fnTrapKD(event)"></td>
                    <!-- Option currency in Discount Qty -->
               
                <tr align="left">
                  <td height="21" width="12%"><%=textListTitleHeader[SESS_LANGUAGE][9]%></td>
                  <td height="21" width="2%" valign="top">:</td>
                  <td height="21" width="86%" valign="top"> 
                    <% 
                    Vector val_typeitem = new Vector(1,1);
                    Vector key_typeitem = new Vector(1,1);
                    //val_typeitem.add("-1");
                    //key_typeitem.add("Semua");

                    if(typeItemNames!=null && typeItemNames.length>0){
                        for(int d=0; d<typeItemNames.length; d++){
                            val_typeitem.add(""+d);
                            key_typeitem.add(typeItemNames[SESS_LANGUAGE][d]);
                        }
                    }
                    String select_typeitem = ""+srcMaterialRepostingStock.getTypeItem();
                    %>
                    <%=ControlCombo.draw(frmSrcMaterial.fieldNames[frmSrcMaterial.FRM_FIELD_TYPE_ITEM],"formElemen",null,select_typeitem,val_typeitem,key_typeitem," tabindex=\"3\"")%>
                  </td>
                  
                      
                <tr align="left"> 
                  <td height="21" width="12%"> <%=textListTitleHeader[SESS_LANGUAGE][3]%></td>
                  <td height="21" width="2%" valign="top">:</td>
                  <td height="21" width="86%" valign="top"> 
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
                    String select_supplier = ""+srcMaterialRepostingStock.getSupplierId();
                    %>
                    <%=ControlCombo.draw(frmSrcMaterial.fieldNames[frmSrcMaterial.FRM_FIELD_SUPPLIERID],"formElemen",null,select_supplier,val_supplier,key_supplier," tabindex=\"3\" onkeydown=\"javascript:fnTrapKD(event)\"")%> 
                  </td>
                 
                <tr align="left"> 
                  <td height="21" width="12%"> <%=textListTitleHeader[SESS_LANGUAGE][4]%></td>
                  <td height="21" width="2%" valign="top">:</td>
                  <td height="21" width="86%" valign="top"> 
                    <%
                    //Vector materGroup = PstCategory.list(0,0,"",PstCategory.fieldNames[PstCategory.FLD_CODE]);
                    Vector materGroup = PstCategory.list(0,0,PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID]+"!=0",PstCategory.fieldNames[PstCategory.FLD_NAME]);
                    Vector vectGroupVal = new Vector(1,1);
                    Vector vectGroupKey = new Vector(1,1);
                    vectGroupVal.add(" ");
                    vectGroupKey.add("-1");
                    //vectGroupVal.add("No Category");
                    //vectGroupKey.add("0");
                    if(materGroup!=null && materGroup.size()>0) {
                        int maxGrp = materGroup.size();
                        for(int i=0; i<maxGrp; i++) {
                            Category mGroup = (Category)materGroup.get(i);
                            vectGroupVal.add(mGroup.getName());
                            vectGroupKey.add(""+mGroup.getOID());
                        }
                    } else {
                        vectGroupVal.add("No Group Available");
                        vectGroupKey.add("-1");
                    }
                    out.println(ControlCombo.draw(frmSrcMaterial.fieldNames[frmSrcMaterial.FRM_FIELD_CATEGORYID],"formElemen", null, ""+srcMaterialRepostingStock.getCategoryId(), vectGroupKey, vectGroupVal, " tabindex=\"4\" onkeydown=\"javascript:fnTrapKD(event)\""));
                    %>
                <tr align="left"> 
                  <td height="21" width="12%"> <%=merkName%><%//=textListTitleHeader[SESS_LANGUAGE][10]%></td>
                  <td height="21" width="2%" valign="top">:</td>
                  <td height="21" width="86%" valign="top"> 
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
                  out.println(ControlCombo.draw(frmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_MERK_ID],"formElemen", null, ""+srcMaterialRepostingStock.getMerkId(), vectGroupKey, vectGroupVal, " tabindex=\"5\" onkeydown=\"javascript:fnTrapKD(event)\""));
                  %>
                <tr align="left"> 
                  <td height="9" width="12%"><%=textListTitleHeader[SESS_LANGUAGE][6]%></td>
                  <td height="9" width="2%" valign="top">:</td>
                  <td height="9" width="86%" valign="top"> 
                  <%
                  Vector vectSortVal = new Vector(1,1);
                  Vector vectSortKey = new Vector(1,1);
                  
                  vectSortVal.add(textListTitleHeader[SESS_LANGUAGE][1]);
                  vectSortKey.add("0");
                  
                  vectSortVal.add(textListTitleHeader[SESS_LANGUAGE][4]);
                  vectSortKey.add("1");
                  
                  vectSortVal.add(textListTitleHeader[SESS_LANGUAGE][2]);
                  vectSortKey.add("2");
                  out.println(ControlCombo.draw(frmSrcMaterial.fieldNames[frmSrcMaterial.FRM_FIELD_SORTBY],"formElemen", null, ""+srcMaterialRepostingStock.getSortby(), vectSortKey, vectSortVal, " tabindex=\"6\" onkeydown=\"javascript:fnTrapKD(event)\""));
                  %>
                  </td>
                <tr align="left">
                  <td height="21" valign="top"><%=textListTitleHeader[SESS_LANGUAGE][11]%></td>
                  <td height="21" valign="top">:</td>
                  <td height="21" valign="top">
                       <input type="checkbox" <%if(srcMaterialRepostingStock.getShowImage()==1){%>checked<%}%> name="<%=frmSrcMaterial.fieldNames[frmSrcMaterial.FRM_SHOW_IMAGE]%>" value="1">
                  </td>
                </tr>
		<tr align="left">
                  <td height="21" valign="top"><%=textListTitleHeader[SESS_LANGUAGE][13]%></td>
                  <td height="21" valign="top">:</td>
                  <td height="21" valign="top">
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
                  out.println(ControlCombo.draw(frmSrcMaterial.fieldNames[frmSrcMaterial.FRM_FIELD_GROUP_ITEM],"formElemen", null, ""+srcMaterialRepostingStock.getGroupItem(), vectGroupMatKey, vectGroupMatVal, " tabindex=\"7\" onkeydown=\"javascript:fnTrapKD(event)\""));
                  %>
                  </td>
                </tr>
                
                <tr align="left"> 
                  <td height="21" valign="top" width="12%">&nbsp;</td>
                  <td height="21" width="2%" valign="top">&nbsp;</td>
                  <td height="21" width="86%" valign="top"><%=drawCodeRangeList(SESS_LANGUAGE,recordsCodeRange)%></td>
                </tr>
                <tr align="left"> 
                  <td height="21" valign="top" width="12%">&nbsp;</td>
                  <td height="21" width="2%" valign="top">&nbsp;</td>
                  <td height="21" width="86%" valign="top"> 
                    <table width="98%" border="0" cellspacing="0" cellpadding="0">
                      <tr> 
                        <!--td nowrap width="5%"><a href="javascript:cmdSearch()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnSearchOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24" alt="Cari Barang"></a></td-->
                        <td class="command" nowrap width="95%"><a class="btn btn-primary" href="javascript:cmdSearch()"><i class="fa fa-search"></i> <%=textListTitleHeader[SESS_LANGUAGE][7]%></a></td>                        
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
<!-- #EndTemplate --></html>

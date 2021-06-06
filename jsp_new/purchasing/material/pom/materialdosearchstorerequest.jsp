<%-- 
    Document   : materialdosearchstorerequest
    Created on : Jan 22, 2015, 9:18:37 PM
    Author     : dimata005
--%>

<%@page import="com.dimata.common.entity.system.AppValue"%>
<%@page import="com.dimata.posbo.entity.purchasing.PstPurchaseOrder"%>
<%@page import="com.dimata.posbo.entity.purchasing.PurchaseOrder"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatReceiveItem"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatReceiveItem"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatReceive"%>
<%@ page import="com.dimata.gui.jsp.ControlList,
                 com.dimata.posbo.entity.masterdata.*,
                 com.dimata.qdep.form.FRMHandler,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.common.entity.contact.PstContactList,
                 com.dimata.common.entity.contact.ContactList,
                 com.dimata.util.Command,
                 com.dimata.posbo.form.masterdata.CtrlMaterial,
                 com.dimata.posbo.form.purchasing.FrmPurchaseOrderItem,
                 com.dimata.gui.jsp.ControlCombo,
                 com.dimata.gui.jsp.ControlLine"%>
<%@page contentType="text/html"%>

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_PURCHASING, AppObjInfo.G2_PURCHASING, AppObjInfo.OBJ_PURCHASE_ORDER); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<%!
/* this constant used to list text of listHeader */
public static final String textMaterialHeader[][] = {
    {"Kategori","Sku","Nama Barang","Semua Barang"},
    {"Category","Code","Material Name","All Goods"}
};

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] = {
    {"NO","SKU","NAMA BARANG","UNIT"},
    {"NO","CODE","NAME PRODUCT","UNIT"}
};

public String drawListMaterial(int currency,int language,Vector objectClass,int start, boolean sts, double curr, long locationID){
    String result = "";
    if(objectClass!=null && objectClass.size()>0){
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        
        ctrlist.addHeader(textListMaterialHeader[language][0],"3%");
        ctrlist.addHeader(textListMaterialHeader[language][1],"10%");
        ctrlist.addHeader(textListMaterialHeader[language][2],"40%");
        ctrlist.addHeader(textListMaterialHeader[language][3],"10%");
        
        ctrlist.setLinkRow(1);
        ctrlist.setLinkSufix("");
        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        ctrlist.setLinkPrefix("javascript:cmdEdit('");
        ctrlist.setLinkSufix("')");
        ctrlist.reset();
        int index = -1;
        
        if(start<0) start = 0;
        long standartId = Long.parseLong(com.dimata.system.entity.PstSystemProperty.getValueByName("ID_STANDART_RATE"));
        Vector customer = PstMemberReg.listFromLocationId(0, 0, PstMemberReg.fieldNames[PstMemberReg.FLD_LOCATION_ID]+"='"+locationID+"'", "");
        MemberReg member = new MemberReg();
        long priceTypeId=0;
        if(customer.size()>0){
            for (int i = 0; i < customer.size(); i++) {
                member = (MemberReg) customer.get(0);
                try{
                    MemberGroup memberGroup= PstMemberGroup.fetchExc(member.getMemberGroupId());
                    priceTypeId=memberGroup.getPriceTypeId();
                }catch(Exception ex){
                }
                
            }
      }
        
        
        for(int i=0; i<objectClass.size(); i++){
            Vector vt = (Vector)objectClass.get(i);
            Material material = (Material)vt.get(0);
            Category categ = (Category)vt.get(1);
            Unit unit = (Unit)vt.get(2);
            MatCurrency matCurr = (MatCurrency)vt.get(3);
            
            MatVendorPrice matVendorPrice = (MatVendorPrice)vt.get(4);
            double price = PstPriceTypeMapping.getPrice(material.getOID(), standartId, priceTypeId);
            start = start + 1;
            
            Vector rowx = new Vector();
            double exchangeRateLastReceive=1;
            String orderLastReceiveBy = PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE]+" DESC ";
            Vector listLastReceive = PstMatReceiveItem.listLastReceive(material.getOID(),0,1,"",orderLastReceiveBy);
             for(int r=0; r<listLastReceive.size(); r++){
                  MatReceiveItem matreceiveitem = (MatReceiveItem)listLastReceive.get(r);//new MatReceiveItem();
                  if(curr==1){
                      if(matreceiveitem.getExchangeRate()==0){
                           exchangeRateLastReceive=1;
                      }else{
                           exchangeRateLastReceive=matreceiveitem.getExchangeRate();
                      }   
                  }  
            }
            rowx.add(""+start);
            rowx.add(material.getSku());
            rowx.add(material.getName());
            rowx.add(unit.getCode());
            
            lstData.add(rowx);
            
            String name = "";
            name = material.getName();
            name = name.replace('\"','`');
            name = name.replace('\'','`');
            if(sts){
                lstLinkData.add(material.getOID()+"','"+material.getSku()+"','"+name+"','"+unit.getCode()+
                        "','"+FRMHandler.userFormatStringDecimal((matVendorPrice.getOrgBuyingPrice()*exchangeRateLastReceive))+"','"+unit.getOID()+
                        "','"+material.getDefaultCostCurrencyId()+"','0','"+FRMHandler.userFormatStringDecimal(matVendorPrice.getCurrBuyingPrice()*exchangeRateLastReceive));
            }else{
                lstLinkData.add(material.getOID()+"','"+material.getSku()+"','"+name+"','"+unit.getCode()+
                        "','"+FRMHandler.userFormatStringDecimal(price)+"','"+unit.getOID()+
                        "','"+material.getDefaultCostCurrencyId()+"','0','"+FRMHandler.userFormatStringDecimal(price));
            }
        }
        //return ctrlist.drawTableNavigation();
         return ctrlist.draw();
    }else{
        result = "<div class=\"msginfo\">&nbsp;&nbsp;Tidak ada data ...</div>";
    }
    return result;
}
%>

<!-- JSP Block -->
<%
String materialcode = FRMQueryString.requestString(request,"mat_code");
long materialgroup = FRMQueryString.requestLong(request,"txt_materialgroup");
String materialname = FRMQueryString.requestString(request,"txt_materialname");
long oidVendor = FRMQueryString.requestLong(request,"mat_vendor");
long locationID = FRMQueryString.requestLong(request, "locationID");
long oidCurrency = FRMQueryString.requestLong(request,"currency_id");
int currType = FRMQueryString.requestInt(request,"curr_type");
double curr = FRMQueryString.requestDouble(request,"rate");
int start = FRMQueryString.requestInt(request, "start");
//int rate = FRMQueryString.requestInt(request,"rate");
int iCommand = FRMQueryString.requestCommand(request);
int recordToGet = 20;
//show all good
int showAllGoods = FRMQueryString.requestInt(request, "show_all_good");

    System.out.println("currency_id : "+oidCurrency);
String vendorName = "";
 ContactList contactList = new ContactList();
try{
    PstContactList pstContactList = new PstContactList();
    contactList = pstContactList.fetchExc(oidVendor);
    vendorName = contactList.getCompName();
}catch(Exception e){
    System.out.println("Err when fetch Supplier : "+e.toString());
}
/**
* instantiate material object that handle searching parameter
*/
//String orderBy = "CAT."+PstCategory.fieldNames[PstCategory.FLD_CODE]; 
String orderBy = "CAT."+PstCategory.fieldNames[PstCategory.FLD_CODE]+
				 ", MAT."+PstMaterial.fieldNames[PstMaterial.FLD_SKU]; 

Material objMaterial = new Material(); 
objMaterial.setCategoryId(materialgroup);
objMaterial.setSku(materialcode);
objMaterial.setName(materialname);
objMaterial.setDefaultCostCurrencyId(oidCurrency);
objMaterial.setBarCode(materialcode);

String designMat = String.valueOf(PstSystemProperty.getValueByName("DESIGN_MATERIAL_FOR"));
int DESIGN_MATERIAL_FOR = Integer.parseInt(designMat);
if (DESIGN_MATERIAL_FOR == 1) { 
    objMaterial.setUseSellLocation(contactList.getLocationId());
}else{
    objMaterial.setUseSellLocation(0);
}
//show all good

/**
* get amount/count of material's list
*/
//int vectSize = PstMaterial.getCountMaterialItem(oidVendor,objMaterial);
//if(vectSize==0 || showAllGoods==1)
int vectSize=0;
if(DESIGN_MATERIAL_FOR==0){
    vectSize = PstMaterial.getCountMaterialItem(0,objMaterial);
}else{
    vectSize = PstMaterial.getCountMaterialItemWithSellLocation(0,objMaterial);
}


//System.out.println("kdjhgkjfdhgkdfgksdjfhkgjshdkfjghlksjdfhglkjsdl");
/**
* generate start/mailstone for displaying data
*/
CtrlMaterial ctrlMaterial = new CtrlMaterial(request);
if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST){
	start = ctrlMaterial.actionList(iCommand, start, vectSize, recordToGet);
}

String pageTitle ="";

if(showAllGoods>0){
pageTitle = "DAFTAR SEMUA BARANG ";
}
else {
//String pageTitle = "DAFTAR BARANG DARI SUPPLIER "+vendorName;
pageTitle = "DAFTAR BARANG DARI SUPPLIER "+vendorName;
}


/**
* get material list will displayed in this page
*/

boolean sts = true;
//Vector vect = PstMaterial.getListMaterialItem(oidVendor,objMaterial,start,recordToGet, orderBy);
//if(vect.size()==0 || showAllGoods==1){
//    vect = PstMaterial.getListMaterialItem(0,objMaterial,start,recordToGet, orderBy);
//    sts = false;
//}

Vector vect = new Vector();
if(DESIGN_MATERIAL_FOR==0){
    vect = PstMaterial.getListMaterialItem(0,objMaterial,start,recordToGet, orderBy);
}else{
    vect = PstMaterial.getListMaterialItemWithSellLocation(0,objMaterial,start,recordToGet, orderBy);
}


sts = false;

int automaticSelected = Integer.valueOf(AppValue.getValueByKey("AUTOMATIC_SELECTED"));
if(vectSize == 1 && automaticSelected==0) {
    
    
    //MaterialStock materialStock = (MaterialStock)vect.get(0);
    Vector vData =  (Vector)vect.get(0); 
    Material material=(Material)vData.get(0);
    Unit unit = (Unit) vData.get(2);
    try {
            material = PstMaterial.fetchExc(material.getOID());     
    }
    catch(Exception e) {
            System.out.println("Exc when PstMaterial.fetchExc() : " + e.toString());
    }

    /*Unit unit = new Unit();
    try {
            unit = PstUnit.fetchExc(material.getDefaultStockUnitId());
    }
    catch(Exception e) {
            System.out.println("Exc when PstUnit.fetchExc() : " + e.toString());
    }*/
    MatCurrency matCurr = new MatCurrency();
    try{
        matCurr =PstMatCurrency.fetchExc(material.getDefaultCostCurrencyId()); 
    }
    catch(Exception e) {
            System.out.println("Exc when PstMatCurrency.fetchExc() : " + e.toString());
    }
                        
                        
    String name = "";
    name = material.getName();
    name = name.replace('\"','`');
    name = name.replace('\'','`');
    
%>
    <script language="JavaScript">
    self.opener.document.forms.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_MATERIAL_ID]%>.value = "<%=material.getOID()%>";
    self.opener.document.forms.frm_purchaseorder.matCode.value = "<%=material.getSku()%>";
    self.opener.document.forms.frm_purchaseorder.matItem.value = "<%=name%>"; 
    self.opener.document.forms.frm_purchaseorder.matUnit.value = "<%=unit.getCode()%>";
    self.opener.document.forms.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_UNIT_ID]%>.value = "<%=unit.getOID()%>";
    self.opener.document.forms.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_QTY_INPUT]%>.focus();
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
function cmdEdit(matOid,matCode,matItem,matUnit,matPrice,matUnitId,matCurrencyId,disc,currPrice){	
    self.opener.document.forms.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_MATERIAL_ID]%>.value = matOid;
    self.opener.document.forms.frm_purchaseorder.matCode.value = matCode;
    self.opener.document.forms.frm_purchaseorder.matItem.value = matItem;
    self.opener.document.forms.frm_purchaseorder.matUnit.value = matUnit;
    self.opener.document.forms.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_UNIT_ID]%>.value = matUnitId;
    
    //self.opener.document.forms.frm_purchaseorder.<%//=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_CURRENCY_ID]%>.value = matCurrencyId;
    self.opener.document.forms.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_DISCOUNT]%>.value = disc;
    self.opener.document.forms.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_DISCOUNT1]%>.value = disc;
   
    self.opener.document.forms.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_PRICE]%>.value = matPrice;
    self.opener.document.forms.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_ORG_BUYING_PRICE]%>.value=matPrice;
    self.opener.document.forms.frm_purchaseorder.unitItem.value=matUnit;
    
    self.opener.document.forms.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_QTY_INPUT]%>.focus();
    
    self.close();		
}

function cmdListFirst(){
    document.frmvendorsearch.command.value="<%=Command.FIRST%>";
    document.frmvendorsearch.action="materialdosearchstorerequest.jsp";
    document.frmvendorsearch.submit();
}

function cmdListPrev(){
    document.frmvendorsearch.command.value="<%=Command.PREV%>";
    document.frmvendorsearch.action="materialdosearchstorerequest.jsp";
    document.frmvendorsearch.submit();
}

function cmdListNext(){
    document.frmvendorsearch.command.value="<%=Command.NEXT%>";
    document.frmvendorsearch.action="materialdosearchstorerequest.jsp";
    document.frmvendorsearch.submit();
}

function cmdListLast(){
    document.frmvendorsearch.command.value="<%=Command.LAST%>";
    document.frmvendorsearch.action="materialdosearchstorerequest.jsp";
    document.frmvendorsearch.submit();
}

function cmdSearch(){
    document.frmvendorsearch.start.value="0";
    document.frmvendorsearch.command.value="<%=Command.LIST%>";
    document.frmvendorsearch.action="materialdosearchstorerequest.jsp";
    document.frmvendorsearch.submit();
}	

function clear(){
    document.frmvendorsearch.txt_materialcode.value="";
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
<script type="text/javascript" src="../../../styles/jquery-1.4.2.min.js"></script>
<script src="../../../styles/jquery.autocomplete.js"></script>
<link rel="stylesheet" type="text/css" href="../../../styles/style.css" />

<%--<link rel="stylesheet" href="../../../styles/jquery.js" type="text/javascript">
<link rel="stylesheet" href="../../../styles/jquery.table_navigation.js" type="text/javascript">
<script type="text/javascript">
	jQuery.tableNavigation();
</script>
<style type="text/css">
	tr.selected {background-color: red; color: white;}
</style>--%>

</head>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<script language="JavaScript">
	window.focus();
</script>
<table width="100%" border="0" cellspacing="3" cellpadding="2" height="100%" bgcolor="#FCFDEC" >
  <tr> 
    <td width="88%" valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> <%@include file="../../../styletemplate/template_header_empty.jsp" %>
          <td height="20" class="mainheader" colspan="2"><%=pageTitle%> </td>
        </tr>
        <tr> 
          <td colspan="2"> 
            <hr size="1">
          </td>
        </tr>
        <tr> 
          <td> 
            <form name="frmvendorsearch" method="post" action="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="mat_vendor" value="<%=oidVendor%>">
              <input type="hidden" name="curr_type" value="<%=currType%>">
              <input type="hidden" name="currency_id" value="<%=oidCurrency%>">
              <input type="hidden" name="rate" value="<%=curr%>">
              <input type="hidden" name="locationID" value="<%=locationID%>">
              <input type="hidden" name="trap" value="">
              <input type="hidden" name="source_link2" value="materialdosearchstorerequest.jsp">
              <table width="100%" border="0" cellspacing="1" cellpadding="1">
                <tr> 
                  <td width="13%"><%=textMaterialHeader[SESS_LANGUAGE][0]%></td>
                  <td width="87%"> : 
                  <%--<%
                    Vector category = PstCategory.list(0,0,"","");
                    Vector vectGroupVal = new Vector(1,1);
                    Vector vectGroupKey = new Vector(1,1);
                    vectGroupVal.add("Semua Kategori");
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
                    out.println(ControlCombo.draw("txt_materialgroup","formElemen", null, ""+materialgroup, vectGroupKey, vectGroupVal, null));
                  %>--%>
                   <select  name="txt_materialgroup" class="formElemen">
                   <option value="-1">Semua Category</option>
                   <%
                    Vector masterCatAcak = PstCategory.list(0,0,"",PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID]);
                    //Category newCategory = new Category();
                    //add opie-eyek 20130821
                    String checked="selected";
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
                                <option value="<%=mGroup.getOID()%>" <%=mGroup.getOID()==objMaterial.getCategoryId()?checked:""%> ><%=parent%><%=mGroup.getName()%></option>
                            <%
                        }
                    } else {
                        vectGroupVal.add("Tidak Ada Category");
                        vectGroupKey.add("-1");
                    }
                  %>
                  </select>
                   <input type="checkbox" name="show_all_good" value="1" <% if( showAllGoods==1){%>checked<%}%> >
                  <%=textMaterialHeader[SESS_LANGUAGE][3]%>
                  </td>
                </tr>
                <tr> 
                  <td width="13%"><%=textMaterialHeader[SESS_LANGUAGE][1]%></td>
                  <td width="87%"> : 
                    <input type="text" name="mat_code" size="15" value="<%=materialcode%>" class="formElemen">
                  </td>
                </tr>
                <tr> 
                  <td width="13%"><%=textMaterialHeader[SESS_LANGUAGE][2]%></td>
                  <td width="87%"> : 
                    <input type="text" onKeyDown="javascript:keyDownCheck(event)" name="txt_materialname" size="30" value="<%=materialname%>" class="formElemen" id="txt_materialname">
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
                  <td colspan="2"><%=drawListMaterial(currType,SESS_LANGUAGE,vect,start,sts,curr,locationID)%></td>
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
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
</body>
<script language="JavaScript">
         document.frmvendorsearch.txt_materialname.focus();
</script>
<script>
	jQuery(function(){
		$("#txt_materialname").autocomplete("list.jsp");
	});
</script>
</html>

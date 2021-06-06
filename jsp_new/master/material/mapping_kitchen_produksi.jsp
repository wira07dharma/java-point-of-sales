<%-- 
    Document   : mapping_kitchen_produski
    Created on : Aug 7, 2016, 5:05:29 PM
    Author     : dimata005
--%>
<%@page import="com.dimata.posbo.entity.masterdata.SubCategory"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstSubCategory"%>
<%@page import="com.dimata.posbo.entity.masterdata.Merk"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMerk"%>
<%@page import="com.dimata.posbo.entity.masterdata.KitchenProduksiMapping"%>
<%@page import="com.dimata.posbo.entity.masterdata.FrmKitchenProduksiMapping"%>
<%@page import="com.dimata.posbo.entity.masterdata.CtrlKitchenProduksiMapping"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstKitchenProduksiMapping"%>

<%@ page language = "java" %>
<%@ page import = "java.util.*,
                   com.dimata.posbo.entity.masterdata.Category,
                   com.dimata.posbo.entity.masterdata.PstCategory,
                   com.dimata.posbo.form.masterdata.CtrlCategory,
                   com.dimata.posbo.form.masterdata.FrmCategory,
                   com.dimata.gui.jsp.ControlLine,
                   com.dimata.gui.jsp.ControlList,
                   com.dimata.posbo.jsp.JspInfo,
                   com.dimata.common.entity.payment.DiscountType,
                   com.dimata.common.entity.payment.CurrencyType,
                   com.dimata.posbo.entity.masterdata.PstDiscountMapping,
                   com.dimata.common.entity.payment.PstCurrencyType,
                   com.dimata.common.entity.payment.PstDiscountType,
                   com.dimata.common.entity.location.PstLocation,
                   com.dimata.common.entity.location.Location,
                    com.dimata.gui.jsp.ControlCombo,
                   com.dimata.posbo.session.masterdata.SessDiscountCategory" %>
<%@ page import = "com.dimata.util.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_CATEGORY); %>
<%@ include file = "../../main/checkuser.jsp" %>
<%
boolean privEditPrice = true;
%>

<!-- Jsp Block -->
<%!
/* this constant used to list text of listHeader */
public static final String textListHeader[][] =
{
	{"Nama","Lokasi Order","Lokasi Produksi","Lokasi Produksi","Parent Id","Status","Category"},
	{"Name","Order Location","Production Location","Production Location","Parent Id","Status","Category"}
};

/* this method used to list material department */
public String drawList(int language,Vector objectClass,long departmentId, int start, String typeOfBusiness)
{
	
        ControlList ctrlist = new ControlList();
	ctrlist.setAreaWidth("100%");
	ctrlist.setListStyle("listgen");
	ctrlist.setTitleStyle("listgentitle");
	ctrlist.setCellStyle("listgensell");
	ctrlist.setHeaderStyle("listgentitle");
	ctrlist.dataFormat("No","5%","center","left");
	ctrlist.dataFormat(textListHeader[language][0],"10%","center","left");
	ctrlist.dataFormat(textListHeader[language][1],"15%","center","left");
        ctrlist.dataFormat(textListHeader[language][2],"10%","center","left");

	ctrlist.setLinkRow(1);
	ctrlist.setLinkSufix("");
	Vector lstData = ctrlist.getData();
	Vector lstLinkData = ctrlist.getLinkData();
	ctrlist.setLinkPrefix("javascript:cmdEdit('");
	ctrlist.setLinkSufix("')");
	ctrlist.reset();
	int index = -1;
        if(objectClass!=null && objectClass.size()>0) {
            for(int i=0; i<objectClass.size(); i++) {
                //disini
                KitchenProduksiMapping kitchenProduksi=(KitchenProduksiMapping)objectClass.get(i);
                Vector rowx = new Vector();
                rowx.add(""+(i+start+1));
                rowx.add(kitchenProduksi.getSubCategory());
		rowx.add(kitchenProduksi.getLocationOrder());
                rowx.add(kitchenProduksi.getLocationProduksi());
                lstData.add(rowx);
                lstLinkData.add(String.valueOf(kitchenProduksi.getOID()));
            }
            
        }
    return ctrlist.drawMe(index);
}

%>

<%
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int prevCommand = FRMQueryString.requestInt(request, "prev_command");
long oidMatDepartment = FRMQueryString.requestLong(request, "hidden_department_id");
long oidmapping = FRMQueryString.requestLong(request, "oidmapping");
int source = FRMQueryString.requestInt(request, "source");
int mappingProduksi = Integer.parseInt(PstSystemProperty.getValueByName("MAPPING_PRINT_PRODUKSI"));
String departmentTitle = "Mapping Produksi";

int recordToGet = 15;
String msgString = "";
int iErrCode = FRMMessage.NONE;
String whereClause = "";
/**
* Special case if code is caracter of numeric (1,2,3,4,5, ...)
* dengan "+1" artinya String akan diubah secara implisit menjadi Numerik
*/
String orderClause = "("+PstCategory.fieldNames[PstCategory.FLD_CODE]+"+1)";

CtrlKitchenProduksiMapping ctrlKitchenProduksiMapping = new CtrlKitchenProduksiMapping(request);
ControlLine ctrLine = new ControlLine();
Vector listKitchenProduksi = new Vector(1,1);

iErrCode = ctrlKitchenProduksiMapping.action(iCommand,oidMatDepartment);
FrmKitchenProduksiMapping frmKitchenProduksiMapping = ctrlKitchenProduksiMapping.getForm();

// count list All MatDepartment
int vectSize = 0;//PstCategory.getCount(whereClause);

KitchenProduksiMapping kitchenProduksiMapping = ctrlKitchenProduksiMapping.getKitchenProduksiMapping();
msgString =  ctrlKitchenProduksiMapping.getMessage();

if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST)
{
	start = ctrlKitchenProduksiMapping.actionList(iCommand, start, vectSize, recordToGet);
}

// get record to display
//orderClause = PstCategory.fieldNames[PstCategory.FLD_NAME];
listKitchenProduksi = PstKitchenProduksiMapping.listJoin(0, 0, "", "",mappingProduksi);

if(listKitchenProduksi.size()<1 && start>0)
{
  if (vectSize - recordToGet > recordToGet)
  {
	 start = start - recordToGet;
  }
  else
  {
	 start = 0 ;
	 iCommand = Command.FIRST;
	 prevCommand = Command.FIRST;
  }
}

%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
function cmdAdd()
{
	document.frmcategory.hidden_department_id.value="0";
	document.frmcategory.command.value="<%=Command.ADD%>";
	document.frmcategory.prev_command.value="<%=prevCommand%>";
	document.frmcategory.action="mapping_kitchen_produksi.jsp";
	document.frmcategory.submit();
}

function cmdAsk(oidMatDepartment)
{
	document.frmcategory.hidden_department_id.value=oidMatDepartment;
	document.frmcategory.command.value="<%=Command.ASK%>";
	document.frmcategory.prev_command.value="<%=prevCommand%>";
	document.frmcategory.action="mapping_kitchen_produksi.jsp";
	document.frmcategory.submit();
}

function cmdConfirmDelete(oidMatDepartment)
{
	document.frmcategory.hidden_department_id.value=oidMatDepartment;
	document.frmcategory.command.value="<%=Command.DELETE%>";
	document.frmcategory.prev_command.value="<%=prevCommand%>";
	document.frmcategory.action="mapping_kitchen_produksi.jsp";
	document.frmcategory.submit();
}
function cmdSave()
{
	document.frmcategory.command.value="<%=Command.SAVE%>";
	document.frmcategory.prev_command.value="<%=prevCommand%>";
	document.frmcategory.action="mapping_kitchen_produksi.jsp";
	document.frmcategory.submit();
}

function cmdEdit(oidMatDepartment)
{
	document.frmcategory.hidden_department_id.value=oidMatDepartment;
	document.frmcategory.command.value="<%=Command.EDIT%>";
	document.frmcategory.prev_command.value="<%=prevCommand%>";
	document.frmcategory.action="mapping_kitchen_produksi.jsp";
	document.frmcategory.submit();
}

function cmdCancel(oidMatDepartment)
{
	document.frmcategory.hidden_department_id.value=oidMatDepartment;
	document.frmcategory.command.value="<%=Command.EDIT%>";
	document.frmcategory.prev_command.value="<%=prevCommand%>";
	document.frmcategory.action="mapping_kitchen_produksi.jsp";
	document.frmcategory.submit();
}

function cmdBack()
{
	document.frmcategory.command.value="<%=Command.BACK%>";
	document.frmcategory.action="mapping_kitchen_produksi.jsp";
	document.frmcategory.submit();
}

function cmdListFirst()
{
	document.frmcategory.command.value="<%=Command.FIRST%>";
	document.frmcategory.prev_command.value="<%=Command.FIRST%>";
	document.frmcategory.action="mapping_kitchen_produksi.jsp";
	document.frmcategory.submit();
}

function cmdListPrev()
{
	document.frmcategory.command.value="<%=Command.PREV%>";
	document.frmcategory.prev_command.value="<%=Command.PREV%>";
	document.frmcategory.action="mapping_kitchen_produksi.jsp";
	document.frmcategory.submit();
}

function cmdListNext()
{
	document.frmcategory.command.value="<%=Command.NEXT%>";
	document.frmcategory.prev_command.value="<%=Command.NEXT%>";
	document.frmcategory.action="mapping_kitchen_produksi.jsp";
	document.frmcategory.submit();
}

function cmdListLast()
{
	document.frmcategory.command.value="<%=Command.LAST%>";
	document.frmcategory.prev_command.value="<%=Command.LAST%>";
	document.frmcategory.action="mapping_kitchen_produksi.jsp";
	document.frmcategory.submit();
}

function cmdClose(){
     self.opener.document.forms.frmmaterial.submit();
     self.close();
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
<%if(menuUsed == MENU_ICON){%>
    <link href="../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
<%}%>
<!-- #BeginEditable "styles" -->
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
<style type="text/css">
.listheader { COLOR: #FFFFFF; background-color:<%=tableHeader%>; FONT-SIZE: 10px; FONT-WEIGHT: bold; TEXT-ALIGN: center}
.listgensell {  color: #000000; background-color:<%=tableCell%>}
.listgensell {  color: #000000; background-color:<%=tableCell%>}
.tabcontent {  background-color: <%=tableCell%>}
.table_cell {  background-color: <%=tableCell%>}
.listgentitle {  font-size: 11px; font-style: normal; font-weight: bold; color: #FFFFFF; background-color: <%=tableHeader%>; text-align: center}
</style>
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
       <%if(source==0){%>
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
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->
            Masterdata &gt; <%=departmentTitle%><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" -->
            <form name="frmcategory" method ="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="vectSize" value="<%=vectSize%>">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="hidden_department_id" value="<%=oidMatDepartment%>">
              <input type="hidden" name="source" value="<%=source%>">
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr align="left" valign="top">
                  <td height="8"  colspan="3">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr align="left" valign="top">
                        <td height="8" valign="middle" colspan="3">
                          <hr size="1">
                        </td>
                      </tr>
                      <tr align="left" valign="top">
                        <td height="8" align="left" colspan="3" class="command">
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
                                                cmd =prevCommand;
                                   }
                            %>
                            <% 
                                ctrLine.setLocationImg(approot+"/images");
                                ctrLine.initDefault();
                            %>
                            <%=ctrLine.drawImageListLimit(cmd,vectSize,start,recordToGet)%> </span> </td>
                      </tr>
                      <tr align="left" valign="top">
                        <td height="22" valign="middle" colspan="3">
                            <%if(iCommand!=Command.ADD && iCommand!=Command.EDIT && iCommand!=Command.ASK && iErrCode==FRMMessage.NONE){%>
                              <table width="17%" border="0" cellspacing="2" cellpadding="3">
                                <tr>
                                  <%if(privAdd){%>
                                      <td width="18%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,departmentTitle,ctrLine.CMD_ADD,true)%>"></a></td>
                                      <td nowrap width="82%"><a href="javascript:cmdAdd()" class="command"><%=ctrLine.getCommand(SESS_LANGUAGE,departmentTitle,ctrLine.CMD_ADD,true)%></a></td>
                                  <%}%>
                                </tr>
                                <tr>
                                  <%if(source==1){%>
                                      <td width="18%"><a href="javascript:cmdClose()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,departmentTitle,ctrLine.CMD_BACK_SEARCH,true)%>"></a></td>
                                      <td nowrap width="82%"><a href="javascript:cmdClose()" class="command"><%=ctrLine.getCommand(SESS_LANGUAGE,departmentTitle,ctrLine.CMD_BACK_SEARCH,true)%></a></td>
                                  <%}%>
                                </tr>
                              </table>
                          <%}%>			
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>
                
                <tr align="left" valign="top">
                  <td height="8" valign="middle" colspan="3">
                    <%
                      if((iCommand == Command.ADD)
                         || (iCommand == Command.EDIT)
                             || (iCommand == Command.ASK)
                             || ((iCommand==Command.SAVE || iCommand==Command.DELETE) && (iErrCode>0))
                            )
                      {
		    %>
                    <table width="100%" border="0" cellspacing="2" cellpadding="0">
                      <tr align="left">
                        <td colspan="10" class="comment" height="30"><u><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Editor "+departmentTitle : departmentTitle+" Editor"%></u></td>
                      <%
                        Vector vectCat = new Vector();
                         Vector vectCatKey = new Vector();
                          
                        if(mappingProduksi==0){//pos merk
                            //sql = sql + ",PM."+PstMerk.fieldNames[PstMerk.FLD_MERK_ID]+" AS NAME_CAT_ID ";
                            Vector vMerk = PstMerk.list(0, 0, "", "");
                            if(vMerk!=null && vMerk.size()>0) {
                                for(int i=0; i<vMerk.size(); i++) {
                                        Merk merk = (Merk) vMerk.get(i);
                                        vectCat.add(""+merk.getName());
                                        vectCatKey.add(""+merk.getOID());
                                }
                            }
                        }else if(mappingProduksi==1){ //pos_sub_category
                            //sql = sql + ",PM."+PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID]+" AS NAME_CAT_ID ";
                            Vector vMerk = PstSubCategory.list(0, 0, "", "");
                            if(vMerk!=null && vMerk.size()>0) {
                                for(int i=0; i<vMerk.size(); i++) {
                                        SubCategory  merk = (SubCategory) vMerk.get(i);
                                        vectCat.add(""+merk.getName());
                                        vectCatKey.add(""+merk.getOID());
                                }
                            }
                        }else{//pos_category
                            //sql = sql + ",PM."+PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID]+" AS NAME_CAT_ID ";
                            Vector vMerk = PstCategory.list(0, 0, "", "");
                            if(vMerk!=null && vMerk.size()>0) {
                                for(int i=0; i<vMerk.size(); i++) {
                                        Category  merk = (Category) vMerk.get(i);
                                        vectCat.add(""+merk.getName());
                                        vectCatKey.add(""+merk.getOID());
                                }
                            }
                        }
                        
                        if(oidmapping==0){
                            oidmapping = kitchenProduksiMapping.getSubCategoryId();
                        }
                      %>
                      
                      <tr align="left">
                        <td width="9%">&nbsp;<%=textListHeader[SESS_LANGUAGE][0]%></td>
                        <td width="1%">:</td>
                        <td colspan="8" width="90%">
                           <%if(mappingProduksi==2){%>
                                <select id="<%=frmKitchenProduksiMapping.fieldNames[frmKitchenProduksiMapping.FRM_FIELD_SUBCATEGORYID]%>"  name="<%=frmKitchenProduksiMapping.fieldNames[frmKitchenProduksiMapping.FRM_FIELD_SUBCATEGORYID]%>" class="formElemen">
                                <%
                                 Vector masterCatAcak = PstCategory.list(0,0,"",PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID]);
                                 Vector materGroup = PstCategory.structureList(masterCatAcak) ;
                                 Vector vectGroupVal = new Vector(1,1);
                                 Vector vectGroupKey = new Vector(1,1);
                                 if(materGroup!=null && materGroup.size()>0) {
                                     String parent="";
                                    // Vector<Category> resultTotal= new Vector();
                                     Vector<Long> levelParent = new Vector<Long>();
                                     for(int i=0; i<materGroup.size(); i++) {
                                         Category mGroup = (Category)materGroup.get(i);
                                             String select="";
                                                if(mGroup.getOID()==oidmapping){
                                                    select="selected";
                                                }
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
                                             <option value="<%=mGroup.getOID()%>"  <%=select%> ><%=parent%><%=mGroup.getName()%></option>
                                         <%
                                     }
                                 } else {
                                     vectGroupVal.add("Tidak Ada Category");
                                     vectGroupKey.add("-1");
                                 }
                               %>
                               </select>
                               
                           <%}else{%>
                                 <%=ControlCombo.draw(frmKitchenProduksiMapping.fieldNames[frmKitchenProduksiMapping.FRM_FIELD_SUBCATEGORYID],"formElemen", null, ""+oidmapping,vectCatKey ,vectCat , "onChange=\"javascript:changeUnit()\"")%> 
                           <%}%> 
                        </td>  
                      </tr>
                      <%
                          Vector vectKsgVal = new Vector();
                          Vector vectKsgKey = new Vector();
                          Vector vLocation = PstLocation.listAll();
                          if(vLocation!=null && vLocation.size()>0) {
                            for(int i=0; i<vLocation.size(); i++) {
                                Location loc = (Location) vLocation.get(i);
                                vectKsgVal.add(""+loc.getOID());
                                vectKsgKey.add(""+loc.getName());
                            }
                          }  
                      %>
                      <tr align="left">
                        <td width="9%">&nbsp;<%=textListHeader[SESS_LANGUAGE][1]%></td>
                        <td width="1%">:</td>
                        <td colspan="8" width="90%">
                          
                          <%=ControlCombo.draw(frmKitchenProduksiMapping.fieldNames[frmKitchenProduksiMapping.FRM_FIELD_LOCATIONORDERID],"formElemen", null, ""+kitchenProduksiMapping.getLocationOrderId(), vectKsgVal, vectKsgKey, "onChange=\"javascript:changeUnit()\"")%> 
                        </td>  
                      </tr>
                      <tr align="left">
                        <td width="9%">&nbsp;<%=textListHeader[SESS_LANGUAGE][2]%></td>
                        <td width="1%">:</td>
                        <td colspan="8" width="90%">
                          <%=ControlCombo.draw(frmKitchenProduksiMapping.fieldNames[frmKitchenProduksiMapping.FRM_FIELD_LOCATIONPRODUKSIID],"formElemen", null, ""+kitchenProduksiMapping.getLocationProduksiId(), vectKsgVal, vectKsgKey, "onChange=\"javascript:changeUnit()\"")%> 
                        </td>  
                      </tr>
                      <tr align="left" >
                        <td colspan="10" class="command" valign="top">                      
                      </tr>
                      <tr align="left" >
                        <td colspan="10" class="command" valign="top">                      
                      </tr>
                      <tr align="left" >
                        <td colspan="10" class="command" valign="top">
                          <%
                                ctrLine.setLocationImg(approot+"/images");

                                // set image alternative caption
                                ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,departmentTitle,ctrLine.CMD_SAVE,true));
                                ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,departmentTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,departmentTitle,ctrLine.CMD_BACK,true)+" List");
                                ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,departmentTitle,ctrLine.CMD_ASK,true));
                                ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,departmentTitle,ctrLine.CMD_CANCEL,false));

                                ctrLine.initDefault();
                                ctrLine.setTableWidth("80%");
                                String scomDel = "javascript:cmdAsk('"+oidMatDepartment+"')";
                                String sconDelCom = "javascript:cmdConfirmDelete('"+oidMatDepartment+"')";
                                String scancel = "javascript:cmdEdit('"+oidMatDepartment+"')";
                                ctrLine.setCommandStyle("command");
                                ctrLine.setColCommStyle("command");

                                // set command caption
                                ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE,departmentTitle,ctrLine.CMD_SAVE,true));
                                ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,departmentTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,departmentTitle,ctrLine.CMD_BACK,true)+" List");
                                ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE,departmentTitle,ctrLine.CMD_ASK,true));
                                ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE,departmentTitle,ctrLine.CMD_DELETE,true));
                                ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE,departmentTitle,ctrLine.CMD_CANCEL,false));


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
                                ctrLine.setAddCaption("");
                          %>
                          <%
                          if( (iCommand==Command.ADD)
                              || (iCommand==Command.EDIT)
                                  || (iCommand==Command.ASK)
                              || ((iCommand==Command.SAVE || iCommand==Command.DELETE) && iErrCode>0)
                                )
                          {
                          out.println(ctrLine.drawImage(iCommand, iErrCode, msgString));
                          }
                          %>
                      </tr>
                    </table>
                    <%}%>
                  </td>
                </tr>
                <tr align="left" >
                    <td colspan="10" class="command" valign="top">                      
                  </tr>
                  <tr align="left" >
                    <td colspan="10" class="command" valign="top">                      
                  </tr>
                 <tr align="left" >
                    <td colspan="10" class="command" valign="top">                      
                  </tr> 
                <tr align="left" valign="top">
                  <td height="14" valign="middle" colspan="3" class="comment">&nbsp;<u><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar "+departmentTitle : departmentTitle+" List"%></u></td>
                </tr>
                <tr align="left" valign="top">
                  <td height="22" valign="middle" colspan="3"> 
                      <%=drawList(SESS_LANGUAGE,listKitchenProduksi,oidMatDepartment,start,typeOfBusiness)%> 
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

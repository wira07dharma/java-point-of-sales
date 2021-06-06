<%-- 
    Document   : src_reportselisihkoreksistok_daily
    Created on : Aug 13, 2016, 2:21:11 PM
    Author     : dimata005
--%>
<%@ page import="com.dimata.qdep.entity.I_PstDocType,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.posbo.entity.search.SrcMatStockOpname,
                 com.dimata.posbo.form.search.FrmSrcMatStockOpname,
                 com.dimata.posbo.session.warehouse.SessMatStockOpname,
                 com.dimata.util.Command,
                 com.dimata.common.entity.location.PstLocation,
                 com.dimata.common.entity.location.Location,
                 com.dimata.gui.jsp.ControlCombo,
                 com.dimata.posbo.entity.masterdata.PstCategory,
                 com.dimata.posbo.entity.masterdata.Category,
                 com.dimata.gui.jsp.ControlDate"%>
<%@ page language = "java" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK, AppObjInfo.OBJ_STOCK_CORRECTION); %>
<%@ include file = "../../../main/checkuser.jsp" %>
<%!
public static final String textListGlobal[][] = {
	{"Stok","Laporan","Selisih Koreksi Stok","Pencarian","Daftar","Edit"},
	{"Stock","Report","Lost Stock Correction","Search","List","Edit"}
};

public static final String textListHeader[][] = {
	{"Lokasi","Supplier","Kategori","Sub Kategori","Periode","Semua tanggal"," s/d ",
		"Urut Berdasarkan","Status","Semua"},
	{"Location Name","Supplier","Kategori","Sub Kategori","Period","All Date"," to ",
		"Sort by","Status","All"}	
};

public boolean getTruedFalse(Vector vect, int index) {
    for(int i=0;i<vect.size();i++) {
        int iStatus = Integer.parseInt((String)vect.get(i));
        if(iStatus==index)
            return true;
    }
    return false;
}
%>

<!-- Jsp Block -->
<%
/**
* get approval status for create document 
*/
I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();
I_Approval i_approval = (I_Approval) Class.forName(approvalClassName).newInstance();
I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();
int systemName = I_DocType.SYSTEM_MATERIAL;
int docType = i_pstDocType.composeDocumentType(systemName,I_DocType.MAT_DOC_TYPE_OPN);
boolean privManageData = true;%>

<%
String opnameCode = ""; //i_pstDocType.getDocCode(docType);
String opnameTitle = "Koreksi Stok"; //i_pstDocType.getDocTitle(docType);

int iCommand = FRMQueryString.requestCommand(request);

ControlLine ctrLine = new ControlLine();
SrcMatStockOpname srcMatStockOpname = new SrcMatStockOpname();
FrmSrcMatStockOpname frmSrcMatStockOpname = new FrmSrcMatStockOpname();
try{
	srcMatStockOpname = (SrcMatStockOpname)session.getValue(SessMatStockOpname.SESS_SRC_MATSTOCKCORRECTION);
}
catch(Exception e){
	srcMatStockOpname = new SrcMatStockOpname();
	srcMatStockOpname.setStatus(-1);
}

if(srcMatStockOpname==null){
	srcMatStockOpname = new SrcMatStockOpname();
	srcMatStockOpname.setStatus(-1);
}

try{
	session.removeValue(SessMatStockOpname.SESS_SRC_MATSTOCKCORRECTION);
}
catch(Exception e){
}

/** gett list location */
Vector locationid_value = new Vector(1,1);
Vector locationid_key = new Vector(1,1);
String whereClause = "";//PstLocation.fieldNames[PstLocation.FLD_TYPE]+"="+PstLocation.TYPE_LOCATION_WAREHOUSE;
Vector vectLocation = PstLocation.list(0,0,whereClause,PstLocation.fieldNames[PstLocation.FLD_CODE]);
locationid_key.add(textListHeader[SESS_LANGUAGE][9]+" "+textListHeader[SESS_LANGUAGE][0]);
locationid_value.add("0");
if(vectLocation!=null && vectLocation.size()>0){
	for(int b=0; b < vectLocation.size(); b++){
		Location location = (Location)vectLocation.get(b);
		locationid_value.add(""+location.getOID());
		locationid_key.add(location.getName());
	}
}
String selectValue = ""+srcMatStockOpname.getLocationId();

/** get list category */
/**Vector materGroup = PstCategory.list(0,0,"",PstCategory.fieldNames[PstCategory.FLD_CODE]);
Vector vectGroupVal = new Vector(1,1);
Vector vectGroupKey = new Vector(1,1);
vectGroupVal.add(textListHeader[SESS_LANGUAGE][9]+" "+textListHeader[SESS_LANGUAGE][2]);
vectGroupKey.add("0");																	  	
if(materGroup!=null && materGroup.size()>0) {
	for(int i=0; i<materGroup.size(); i++) {
		Category mGroup = (Category)materGroup.get(i);
		vectGroupVal.add(mGroup.getName());
		vectGroupKey.add(""+mGroup.getOID());																	  	
	}
}*/

%>

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title
><script language="JavaScript">
<!--
function cmdSearch(){
	document.frmsrcmatstockcorrection.command.value="<%=Command.LIST%>";
	document.frmsrcmatstockcorrection.action="report_list_lost_correction_stock.jsp?daily=1";
	document.frmsrcmatstockcorrection.submit();
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
//-->
</script>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<!-- #BeginEditable "styles" -->
<%if(menuUsed == MENU_ICON){%>
    <link href="../../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
<%}%>
<link rel="stylesheet" href="../../../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" --> 
<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
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
      <%@ include file = "../../../main/header.jsp" %>
      <!-- #EndEditable --></td>
  </tr>
  <tr>
    <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
      <%@ include file = "../../../main/mnmain.jsp" %>
      <!-- #EndEditable --> </td>
  </tr>
  <%}else{%>
   <tr bgcolor="#FFFFFF">
    <td height="10" ID="MAINMENU">
      <%@include file="../../../styletemplate/template_header.jsp" %>
    </td>
  </tr>
  <%}%>
  <tr> 
    <td valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">  
        <tr> 
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->
            <%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][1]%> &gt; <%=textListGlobal[SESS_LANGUAGE][2]%>&gt; <%=textListGlobal[SESS_LANGUAGE][3]%><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frmsrcmatstockcorrection" method="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="approval_command" value="">
              <input type="hidden" name="<%=frmSrcMatStockOpname.fieldNames[FrmSrcMatStockOpname.FRM_FIELD_STATUS]%>" value="-1">
              <table width="100%" border="0" cellspacing="2" cellpadding="2">
                <tr> 
                  <td colspan="3" align="left" class="title"> <hr size="1"> </td>
                </tr>
                <tr> 
                  <td height="21" width="13%"> <strong><%=textListHeader[SESS_LANGUAGE][0]%> </strong></td>
                  <td height="21" width="1%" valign="top" align="left"><strong>:</strong></td>
                  <td height="21" width="86%" valign="top" align="left"><%= ControlCombo.draw(frmSrcMatStockOpname.fieldNames[FrmSrcMatStockOpname.FRM_FIELD_LOCATION_ID], null, selectValue, locationid_value, locationid_key, "", "formElemen") %> </td>
                </tr>
                <tr> 
                  <td height="21" width="13%"> <strong><%=textListHeader[SESS_LANGUAGE][2]%> </strong></td>
                  <td height="21" width="1%" valign="top" align="left"><strong>:</strong></td>
                  <td height="21" width="86%" valign="top" align="left">
                      <%--
                      <%=ControlCombo.draw(frmSrcMatStockOpname.fieldNames[frmSrcMatStockOpname.FRM_FIELD_CATEGORY_ID],"formElemen", null, ""+srcMatStockOpname.getLocationId(), vectGroupKey, vectGroupVal, null)%> --%>
                       <select  name="<%=frmSrcMatStockOpname.fieldNames[frmSrcMatStockOpname.FRM_FIELD_CATEGORY_ID]%>" class="formElemen">
                       <option value="0"><Semua Category</option>
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
                                    <option value="<%=mGroup.getOID()%>" <%=mGroup.getOID()==srcMatStockOpname.getCategoryId()?checked:""%> ><%=parent%><%=mGroup.getName()%></option>
                                <%
                            }
                        } else {
                            vectGroupVal.add("Tidak Ada Category");
                            vectGroupKey.add("0");
                        }
                      %>
                  </select>
                  </td>
                </tr>
                <tr>
                  <td height="21" width="13%"> <strong><%=textListHeader[SESS_LANGUAGE][4]%> </strong></td>
                  <td height="21" width="1%" valign="top" align="left"><strong>:</strong></td>
                  <td height="21" width="86%" valign="top" align="left">
                  <%=ControlDate.drawDateWithStyle(frmSrcMatStockOpname.fieldNames[FrmSrcMatStockOpname.FRM_FIELD_FROM_DATE],srcMatStockOpname.getFromDate(),1,-5,"formElemen","") %> <%=textListHeader[SESS_LANGUAGE][6]%> <%=ControlDate.drawDateWithStyle(frmSrcMatStockOpname.fieldNames[FrmSrcMatStockOpname.FRM_FIELD_TO_DATE],srcMatStockOpname.getToDate(),1,-5,"formElemen","")%> </td>
                </tr>
                <!-- Added document status -->
                <tr>
                        <td height="21" valign="top" width="15%" align="left"><strong><%=textListHeader[SESS_LANGUAGE][8]%></strong></td>
                        <td height="21" valign="top" width="1%" align="left"><strong>:</strong></td>
                        <td height="21" width="84%" valign="top" align="left">
                        <%
                          Vector vectResult = i_status.getDocStatusFor(docType);
                          for(int i=0; i<vectResult.size(); i++) {
                              if ((i == I_DocStatus.DOCUMENT_STATUS_DRAFT))	{
                                  Vector vetTemp = (Vector)vectResult.get(i);
                                  int indexPrStatus = Integer.parseInt(String.valueOf(vetTemp.get(0)));
                                  String strPrStatus = String.valueOf(vetTemp.get(1));
                                  %>
                                  <input type="checkbox" class="formElemen" name="<%=FrmSrcMatStockOpname.fieldNames[FrmSrcMatStockOpname.FRM_FIELD_STATUS]%>" value="<%=(indexPrStatus)%>" checked onKeyDown="">
                                  <%=strPrStatus%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                  <%
                              }
                          }
                        %>
                        </td>
                </tr>
                <tr> 
                  <td height="21" valign="top" width="13%" align="left">&nbsp;</td>
                  <td height="21" width="1%" valign="top" align="left">&nbsp;</td>
                  <td height="21" width="86%" valign="top" align="left">&nbsp;</td>
                </tr>
                <tr> 
                  <td height="21" valign="top" width="13%" align="left">&nbsp;</td>
                  <td height="21" width="1%" valign="top" align="left">&nbsp;</td>
                  <td height="21" width="86%" valign="top" align="left"> <table width="40%" border="0" cellspacing="0" cellpadding="0">
                      <tr> 
                        <!--td nowrap width="8%"><a href="javascript:cmdSearch()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image101','','<%=approot%>/images/BtnSearchOn.jpg',1)" id="aSearch"><img name="Image101" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][1],ctrLine.CMD_SEARCH,true)%>"></a></td-->
                        <td nowrap width="3%">&nbsp;</td>
                        <td class="command" nowrap width="89%"><a class="btn btn-primary" href="javascript:cmdSearch()"><i class="fa fa-search"></i> <%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][1],ctrLine.CMD_SEARCH,true)%></a></td>
                      </tr>
                    </table></td>
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
            <%@include file="../../../styletemplate/footer.jsp" %>
        <%}else{%>
            <%@ include file = "../../../main/footer.jsp" %>
        <%}%>
      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>

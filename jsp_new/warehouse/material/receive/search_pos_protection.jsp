<%-- 
    Document   : search_pos_protection
    Created on : Jun 2, 2014, 10:02:06 AM
    Author     : dimata005
--%>

<%@page import="com.dimata.posbo.form.warehouse.CtrlMaterialStockCode"%>
<%@page import="com.dimata.posbo.session.warehouse.SessPriceProtection"%>
<%@page import="com.dimata.common.entity.contact.ContactList"%>
<%@page import="com.dimata.posbo.entity.warehouse.MaterialStockCode"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMaterialStockCode"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstStockCardReport"%>
<%@page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@page import="com.dimata.harisma.entity.masterdata.PstMarital"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterial"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*,
                   com.dimata.posbo.entity.search.SrcMatReceive,
                   com.dimata.posbo.form.search.FrmSrcMatReceive,
                   com.dimata.posbo.session.warehouse.SessMatReceive,
                   com.dimata.posbo.entity.warehouse.PstMatReceive" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.entity.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package material -->
<%@ page import = "com.dimata.common.entity.location.*" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

public static final String textListGlobal[][] = {
	{"Price Protection","Price Protection","Pencarian","Daftar","Edit","Nama Barang","Kode"},
	{"Price Protection","Price Protection","Search","List","Edit","Item Name","Code"}
};

/* this constant used to list text of listHeader */
public static final String textListHeader[][] = {
	{"Nomor","Supplier","Invoice Supplier","Lokasi Terima","Tanggal","Dari"," s/d ","Status","Urut Berdasar","Semua"},
	{"Number","Supplier","Supplier Invoice","Receive Location","Date","From"," to ","Status","Sort By","All"}
};

public String drawList(int language,Vector objectClass, long locationId)
{
	String result = "";
	if(objectClass!=null && objectClass.size()>0)
	{
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		ctrlist.addHeader("No","3%");
		ctrlist.addHeader("Name","30%");
                ctrlist.addHeader("Price Protection","63%");

		ctrlist.setLinkRow(1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();
		
		int start = 0;

		for(int i=0; i<objectClass.size(); i++){
                    
			Material material = (Material) objectClass.get(i);
                        
			start = start + 1;

			Vector rowx = new Vector();
                        
                        //Vector list = new Vector();
                        Vector vList = new Vector();
                        
                        /*String where =  PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_LOCATION_ID]+"="+locationId+
                                        " AND "+PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_MATERIAL_ID]+"="+material.getOID()+
                                        " AND "+PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_STOCK_STATUS]+"="+PstMaterialStockCode.FLD_STOCK_STATUS_GOOD;
                        
                        list = PstMaterialStockCode.list(0,0,where,PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_MATERIAL_STOCK_CODE_ID]);*/
                        
                        vList =  PstMaterialStockCode.getPosProtectionVendor(material.getOID(), locationId);
                        
			rowx.add(""+start);
			
			rowx.add(material.getName());
                        try{
                            rowx.add(generateVendorPP(vList,locationId));
                        }catch(Exception ex){
                            rowx.add("");
                        }
                        
                        

			lstData.add(rowx);
			lstLinkData.add(String.valueOf(material.getOID()));
		}
		result = ctrlist.draw();
	}
	else
	{
		result = "<div class=\"msginfo\">&nbsp;&nbsp;</div>";
	}
	return result;
}


public String generateVendorPP(Vector vList, long locationId){
        String result = "";
	if(vList!=null && vList.size()>0)
	{
                Vector contactList = new Vector();
                Hashtable <String, Boolean> vectorHast = new Hashtable();
                Vector list = new  Vector();
                if(vList!=null && vList.size()>0){
                    contactList = (Vector) vList.get(0);
                    vectorHast = (Hashtable) vList.get(1);
                    list = (Vector)vList.get(2);
                }        
                
        
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		ctrlist.addHeader("No","3%");
		ctrlist.addHeader("Serial Number","17%");
                ctrlist.addHeader("Harga","10%");
                for(int s=0; s<contactList.size(); s++){
                     ContactList cl = (ContactList) contactList.get(s);
                     ctrlist.addHeader(""+cl.getPersonName(),"10%");
                }
               

		ctrlist.setLinkRow(1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();
		int start = 0;
                
		for(int i=0; i<list.size(); i++){
                        SessPriceProtection materialStockCode = (SessPriceProtection) list.get(i);         
			start = start + 1;
			Vector rowx = new Vector();
			rowx.add(""+start);
			rowx.add(materialStockCode.getStockCode());
                        rowx.add(""+materialStockCode.getValue());
                        for(int s=0; s<contactList.size(); s++){
                             ContactList cl = (ContactList) contactList.get(s);
                             try{
                                 boolean checkVendor = vectorHast.get(""+materialStockCode.getStockCode()+"_"+cl.getOID());
                                 if(checkVendor){
                                    rowx.add("<input type=\"hidden\" name=\"inputan\"  value=\""+materialStockCode.getStockCode()+"_"+cl.getOID()+"_"+locationId+"\">"
                                            + "<input type=\"text\" name=\""+materialStockCode.getStockCode()+"_"+cl.getOID()+"\"  value=\"\">");
                                 }else{
                                    rowx.add("");
                                 } 
                             }catch(Exception ex){
                                  rowx.add("");
                             }
                             
                        }
                        
			lstData.add(rowx);
                        
			lstLinkData.add(String.valueOf(materialStockCode.getStockCodeId()));
		}
		result = ctrlist.draw();
	}
	else
	{
		result = "<div class=\"msginfo\">&nbsp;&nbsp;</div>";
	}
   return result;
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
int docType = i_pstDocType.composeDocumentType(systemName,I_DocType.MAT_DOC_TYPE_LMRR);
boolean privManageData = true;
%>


<%
/**
* get data from 'hidden form'
*/
int iCommand = FRMQueryString.requestCommand(request);
String ItemName = FRMQueryString.requestString(request,"itemName");
String codeItem = FRMQueryString.requestString(request,"codeItem");
long locationId = FRMQueryString.requestLong(request, "locationId");
/**
* declaration of some identifier
*/
String recCode = i_pstDocType.getDocCode(docType);
String recTitle = "Terima Barang"; // i_pstDocType.getDocTitle(docType);
String recItemTitle = recTitle + " Item";

/**
* ControlLine 
*/

CtrlMaterialStockCode  ctrlMaterialStockCode = new CtrlMaterialStockCode(request);

ctrlMaterialStockCode.action(iCommand); 


ControlLine ctrLine = new ControlLine();
Vector records = new  Vector();
String where ="";
if(!ItemName.equals("")){
     where = PstMaterial.fieldNames[PstMaterial.FLD_NAME]+"='"+ItemName+"'";
}

if(!codeItem.equals("")){
    if(where.length()>0){
        where=where+" AND "+PstMaterial.fieldNames[PstMaterial.FLD_SKU]+"='"+codeItem+"'";
    }else{
        where = PstMaterial.fieldNames[PstMaterial.FLD_SKU]+"='"+codeItem+"'";
    }
}

records = PstMaterial.list(0, 0,where, "");

%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
<!--

function cmdSearch(){
	document.frmsrcmatreceive.command.value="<%=Command.LIST%>";
	document.frmsrcmatreceive.action="search_pos_protection.jsp";
	document.frmsrcmatreceive.submit();
}

function cmdSave(){
	document.frmsrcmatreceive.command.value="<%=Command.SAVE%>";
	document.frmsrcmatreceive.action="search_pos_protection.jsp";
	document.frmsrcmatreceive.submit();
}

function fnTrapKD(){
   if (event.keyCode == 13) {
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
<%--autocomplate addd by fitra--%>
<script type="text/javascript" src="../../../styles/jquery-1.4.2.min.js"></script>
<script src="../../../styles/jquery.autocomplete.js"></script>
<link rel="stylesheet" type="text/css" href="../../../styles/style.css" />

</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
  <%if(menuUsed == MENU_PER_TRANS){%>
  <tr>
    <td height="25" ID="TOPTITLE"> <!-- #BeginEditable "header" -->
         <script language="JavaScript">
                window.focus();
        </script>
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
        <script language="JavaScript">
                window.focus();
        </script>
      <%@include file="../../../styletemplate/template_header.jsp" %>
    </td>
  </tr>
  <%}%>
  <tr> 
    <td valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">  
        <tr> 
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" --> 
            &nbsp;<%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][1]%> &gt; <%=textListGlobal[SESS_LANGUAGE][2]%> <!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frmsrcmatreceive" method="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="add_type" value="">			
              <input type="hidden" name="approval_command">		
              <table width="100%" border="0">
                <tr>
                  <td valign="top" colspan="2">
                    <hr size="1">
                  </td>
                </tr>
		<tr> 
                  <td colspan="2"> 
                    <table width="100%" border="0" cellspacing="1" cellpadding="1">
                      <tr> 
                        <td height="21" width="12%">Location</td>
                        <td height="21" width="1%">:</td>
                        <td height="21" width="87%">&nbsp; 
                          <% 
                                Vector obj_locationid = new Vector(1,1); 
                                Vector val_locationid = new Vector(1,1); 
                                Vector key_locationid = new Vector(1,1); 

                                String whereClause = " ("+PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE +
                                                   " OR "+PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE +")";

                                whereClause += " AND "+PstDataCustom.whereLocReportView(userId, "user_create_document_location");

                                Vector vt_loc = PstLocation.listLocationCreateDocument(0, 0, whereClause, "");

                                for(int d=0; d<vt_loc.size(); d++) {
                                        Location loc = (Location)vt_loc.get(d);
                                        val_locationid.add(""+loc.getOID()+"");
                                        key_locationid.add(loc.getName());
                                }
                          %>
                          <%=ControlCombo.draw("locationId", null, "", val_locationid, key_locationid, " onKeyDown=\"javascript:fnTrapKD()\"", "formElemen")%>
                        </td>
                      </tr>  
                      <tr> 
                        <td height="21" width="12%"><%=textListGlobal[SESS_LANGUAGE][6]%></td>
                        <td height="21" width="1%">:</td>
                        <td height="21" width="87%">&nbsp; 
                          <input type="text" name="codeItem" value="<%=codeItem%>" class="formElemen" size="30">
                        </td>
                      </tr>
                      <tr> 
                        <td height="21" width="12%"><%=textListGlobal[SESS_LANGUAGE][5]%></td>
                        <td height="21" width="1%">:</td>
                        <td height="21" width="87%">&nbsp; 
                          <input type="text" name="itemName" value="<%=ItemName%>" class="formElemen" size="30" id="txt_materialname">
                        </td>
                      </tr>
                      <tr> 
                        <td height="21" valign="top" width="12%" align="left">&nbsp;</td>
                        <td height="21" valign="top" width="1%" align="left">&nbsp;</td>
                        <td height="21" width="87%" valign="top" align="left">&nbsp;</td>
                      </tr>
                      <tr> 
                        <td height="21" valign="top" width="12%" align="left">&nbsp;</td>
                        <td height="21" valign="top" width="1%" align="left">&nbsp;</td>
                        <td height="21" width="87%" valign="top" align="left"> 
                          <table width="80%" border="0" cellspacing="0" cellpadding="0">
                            <tr> 
                              <td nowrap width="4%"><a href="javascript:cmdSearch()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnSearchOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][0],ctrLine.CMD_SEARCH,true)%>"></a></td>
                              <td class="command" nowrap width="100%"><a href="javascript:cmdSearch()"><%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][0],ctrLine.CMD_SEARCH,true)%></a></td>
                            </tr>
                          </table>
                        </td>
                      </tr>
                      <tr> 
                        <td height="21" valign="top" width="12%" align="left">&nbsp;</td>
                        <td height="21" valign="top" width="1%" align="left">&nbsp;</td>
                        <td height="21" width="87%" valign="top" align="left">&nbsp;</td>
                      </tr>
                      <tr>
                        <td height="21" valign="top" width="12%" align="left">&nbsp;</td>
                        <td height="21" valign="top" width="1%" align="left">&nbsp;</td>
                        <td height="21" width="87%" valign="top" align="left">
                            <%
                              if(iCommand==Command.LIST){
                                if(records.size()>0){%>
                                    <%=drawList(SESS_LANGUAGE,records,locationId)%>
                                <%}
                               } 
                            %>    
                        </td>
                      </tr>
                      <tr> 
                        <td height="21" valign="top" width="12%" align="left">&nbsp;</td>
                        <td height="21" valign="top" width="1%" align="left">&nbsp;</td>
                        <td height="21" width="87%" valign="top" align="left">&nbsp;</td>
                      </tr>
                      <tr> 
                        <td height="21" valign="top" width="12%" align="left">&nbsp;</td>
                        <td height="21" valign="top" width="1%" align="left">&nbsp;</td>
                        <td height="21" width="87%" valign="top" align="left"> 
                          <table width="80%" border="0" cellspacing="0" cellpadding="0">
                            <tr> 
                              <td nowrap width="4%"><a href="javascript:cmdSave()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnSearchOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][0],ctrLine.CMD_SEARCH,true)%>"></a></td>
                              <td class="command" nowrap width="100%"><a href="javascript:cmdSave()"><%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][0],ctrLine.CMD_SAVE,true)%></a></td>
                            </tr>
                          </table>
                        </td>
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
            <%@include file="../../../styletemplate/footer.jsp" %>
        <%}else{%>
            <%@ include file = "../../../main/footer.jsp" %>
        <%}%>
      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate -->
<%--autocomplate--%>
<script>
	jQuery(function(){
		$("#txt_materialname").autocomplete("list.jsp");
	});
</script>

</html>


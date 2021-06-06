<%@ page import="com.dimata.gui.jsp.ControlList,
                 com.dimata.qdep.form.FRMHandler,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.common.entity.contact.PstContactList,
                 com.dimata.common.entity.contact.ContactList,
                 com.dimata.util.Command,
                 com.dimata.gui.jsp.ControlCombo,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.posbo.entity.masterdata.*,
                 com.dimata.posbo.form.masterdata.CtrlMaterial,
                 com.dimata.posbo.form.warehouse.FrmMatConReceiveItem"%>
<%@page contentType="text/html"%>

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<%!
/* this constant used to list text of listHeader */
public static final String textMaterialHeader[][] = 
{
	{"Kategori","Sku","Nama Barang"},
	{"Category","Material Code","Material Name"}
};

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] = 
{ 
	{"No","Grup","Sku","Nama Barang","Unit","Harga Beli"},
	{"No","Category","Code","Item","Unit","Cost"}
};

public String drawListMaterial(int currency,int language,Vector objectClass,int start){
	String result = "";
	if(objectClass!=null && objectClass.size()>0){
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%"); 
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		
		ctrlist.addHeader(textListMaterialHeader[language][0],"3%");	
		//ctrlist.addHeader(textListMaterialHeader[language][1],"20%");
		ctrlist.addHeader(textListMaterialHeader[language][2],"10%");
		ctrlist.addHeader(textListMaterialHeader[language][3],"35%");
		ctrlist.addHeader(textListMaterialHeader[language][4],"5%");		
		ctrlist.addHeader(textListMaterialHeader[language][5],"10%");	
	
		ctrlist.setLinkRow(2);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();
		int index = -1;
		
		if(start<0) start = 0;
			
		for(int i=0; i<objectClass.size(); i++)
		{
			Vector vt = (Vector)objectClass.get(i);			
			Material material = (Material)vt.get(0);
			Category categ = (Category)vt.get(1);
			Unit unit = (Unit)vt.get(2);
			MatCurrency matCurr = (MatCurrency)vt.get(3);
			MatVendorPrice matVendorPrice = (MatVendorPrice)vt.get(4);
            start = start + 1;
	
			Vector rowx = new Vector();
			
			rowx.add(""+start);		
			// rowx.add(categ.getName());
			rowx.add(material.getSku());		
			rowx.add(material.getName());		
			rowx.add(unit.getCode());
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(matVendorPrice.getCurrBuyingPrice())+"</div>");

            String name = "";
            name = material.getName();
            name = name.replace('\"','`');
            name = name.replace('\'','`');

			lstData.add(rowx);
			lstLinkData.add(material.getOID()+"','"+material.getSku()+"','"+name+"','"+unit.getCode()+
				"','"+FRMHandler.userFormatStringDecimal(matVendorPrice.getCurrBuyingPrice())+"','"+unit.getOID()+//material.getDefaultSellUnitId()+
				"','"+material.getDefaultCostCurrencyId()+"','"+matCurr.getCode());
		}
		return ctrlist.draw();
	}
	else
	{
		result = "<div class=\"msginfo\">&nbsp;&nbsp;No Material Available ...</div>";				
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
long currencyId = FRMQueryString.requestLong(request,"currency_id");
int currType = FRMQueryString.requestInt(request,"curr_type");
double curr = FRMQueryString.requestDouble(request,"rate");
int start = FRMQueryString.requestInt(request, "start");
int iCommand = FRMQueryString.requestCommand(request);
int recordToGet = 15;
String pageTitle = "Daftar Barang Supplier ";

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
objMaterial.setName(materialname);
objMaterial.setDefaultCostCurrencyId(currencyId);


/**
* get amount/count of material's list
*/
int vectSize = PstMaterial.getCountMaterialItem(0,objMaterial,PstMaterial.CATALOG_TYPE_CONSIGMENT);

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
Vector vect = PstMaterial.getListMaterialItem(0,objMaterial,start,recordToGet, orderBy, PstMaterial.CATALOG_TYPE_CONSIGMENT);
%>
<!-- End of JSP Block -->
<html>
<head>
<title>Dimata - ProChain POS</title
><script language="JavaScript">
function cmdEdit(matOid,matCode,matItem,matUnit,matPrice,matUnitId,matCurrencyId,matCurrCode)
{	
	self.opener.frm_recmaterial.<%=FrmMatConReceiveItem.fieldNames[FrmMatConReceiveItem.FRM_FIELD_MATERIAL_ID]%>.value = matOid;
	self.opener.frm_recmaterial.matCode.value = matCode;
	self.opener.frm_recmaterial.matItem.value = matItem;
	self.opener.frm_recmaterial.matUnit.value = matUnit;
	self.opener.frm_recmaterial.<%=FrmMatConReceiveItem.fieldNames[FrmMatConReceiveItem.FRM_FIELD_UNIT_ID]%>.value = matUnitId;	
	//self.opener.frm_recmaterial.matCurrency.value = matCurrCode;
	self.opener.frm_recmaterial.<%=FrmMatConReceiveItem.fieldNames[FrmMatConReceiveItem.FRM_FIELD_CURRENCY_ID]%>.value = matCurrencyId;
	self.opener.frm_recmaterial.<%=FrmMatConReceiveItem.fieldNames[FrmMatConReceiveItem.FRM_FIELD_COST]%>.value = matPrice;		
	self.opener.frm_recmaterial.<%=FrmMatConReceiveItem.fieldNames[FrmMatConReceiveItem.FRM_FIELD_QTY]%>.focus();			
	self.close();		
}

function cmdListFirst(){
	document.frmvendorsearch.command.value="<%=Command.FIRST%>";
	document.frmvendorsearch.action="materialdosearch.jsp";
	document.frmvendorsearch.submit();
}

function cmdListPrev(){
	document.frmvendorsearch.command.value="<%=Command.PREV%>";
	document.frmvendorsearch.action="materialdosearch.jsp";
	document.frmvendorsearch.submit();
}

function cmdListNext(){
	document.frmvendorsearch.command.value="<%=Command.NEXT%>";
	document.frmvendorsearch.action="materialdosearch.jsp";
	document.frmvendorsearch.submit();
}

function cmdListLast(){
	document.frmvendorsearch.command.value="<%=Command.LAST%>";
	document.frmvendorsearch.action="materialdosearch.jsp";
	document.frmvendorsearch.submit();
}	

function cmdSearch(){
        document.frmvendorsearch.start.value=0;
	document.frmvendorsearch.command.value="<%=Command.LIST%>";
	document.frmvendorsearch.action="materialdosearch.jsp";
	document.frmvendorsearch.submit();
}	

function clear(){
	document.frmvendorsearch.txt_materialcode.value="";
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
<script language="JavaScript">
	window.focus();
</script>
<table width="100%" border="0" cellspacing="3" cellpadding="2" height="100%" bgcolor="#FCFDEC" >
  <tr> 
    <td width="88%" valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td height="20" class="mainheader" colspan="2"><%=pageTitle.toUpperCase()+" "+supplierName%> </td>
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
              <input type="hidden" name="mat_vendor" value="<%=oidVendor%>">
              <input type="hidden" name="curr_type" value="<%=currType%>">
              <input type="hidden" name="rate" value="<%=curr%>">
              <input type="hidden" name="currency_id" value="<%=currencyId%>">
              <table width="100%" border="0" cellspacing="1" cellpadding="1">
                <tr>
                  <td width="13%"><%=textMaterialHeader[SESS_LANGUAGE][0]%></td>
                  <td width="87%"> 
                  <%
				  Vector category = PstCategory.list(0,0,"","");
				  Vector vectGroupVal = new Vector(1,1);
				  Vector vectGroupKey = new Vector(1,1);
				  vectGroupVal.add("All Material Group");
				  vectGroupKey.add("-1");																	  				  
				  if(category!=null && category.size()>0)
				  {
					  for(int i=0; i<category.size(); i++)
					  {
						  Category mGroup = (Category)category.get(i);
						  vectGroupVal.add(mGroup.getName());
						  vectGroupKey.add(""+mGroup.getOID());																	  	
					  }
				  }
				  else
				  {							  
					  vectGroupVal.add("No Category Available");
					  vectGroupKey.add("0");									
				  }
				  out.println(ControlCombo.draw("txt_materialgroup","formElemen", null, ""+materialgroup, vectGroupKey, vectGroupVal, null));
				  %>
                  </td>
                </tr>
                <tr> 
                  <td width="13%"><%=textMaterialHeader[SESS_LANGUAGE][1]%></td>
                  <td width="87%"> 
                    <input type="text" name="mat_code" size="15" value="<%=materialcode%>" class="formElemen">
                  </td>
                </tr>
                <tr> 
                  <td width="13%"><%=textMaterialHeader[SESS_LANGUAGE][2]%></td>
                  <td width="87%"> 
                    <input type="text" name="txt_materialname" size="30" value="<%=materialname%>" class="formElemen">
                  </td>
                </tr>
                <tr> 
                  <td width="13%">&nbsp;</td>
                  <td width="87%"> 
                    <input type="button" name="Button" value="Search" onClick="javascript:cmdSearch()" class="formElemen">
                  </td>
                </tr>
                <tr> 
                  <td colspan="2"><%=drawListMaterial(currType,SESS_LANGUAGE,vect,start)%></td>
                </tr>
                <tr> 
                  <td colspan="2"><span class="command"> 
                    <% 
						ControlLine ctrlLine= new ControlLine();
					%>
                    <% ctrlLine.setLocationImg(approot+"/images");
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
</html>

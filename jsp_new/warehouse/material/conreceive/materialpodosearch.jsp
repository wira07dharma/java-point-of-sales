
<%@page contentType="text/html"%>
<!-- package java -->
<%@ page import = "java.util.*,
                   com.dimata.posbo.entity.masterdata.Material,
                   com.dimata.posbo.entity.purchasing.PurchaseOrderItem,
                   com.dimata.posbo.entity.masterdata.Unit,
                   com.dimata.posbo.entity.masterdata.MatCurrency,
                   com.dimata.posbo.entity.masterdata.PstMaterial,
                   com.dimata.posbo.entity.purchasing.PstPurchaseOrderItem,
                   com.dimata.posbo.form.masterdata.CtrlMaterial,
                   com.dimata.posbo.form.warehouse.FrmMatConReceiveItem" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE); %>
<%@ include file = "../../../main/checkuser.jsp" %>


<%!
/* this constant used to list text of listHeader */
public static final String textMaterialHeader[][] = 
{
	{"Kategori","Sku","Nama Barang"},
	{"Category","Code","Name"}
};

/* this constant used to list text of listHeader */
public static final String textListOrderItem[][] = 
{ 
	{"No","Sku","Nama Barang","Unit","Hrg. Beli","Mata Uang","Qty","Total Beli"},
	{"No","Code","Name","Unit","Buy Price","Currency","Quantity","Sub Total"}
};

public String drawListMaterial(int language,Vector objectClass,int start){
	String result = "";
	if(objectClass!=null && objectClass.size()>0)
	{
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%"); 
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		
		ctrlist.addHeader(textListOrderItem[language][0],"3%");
		ctrlist.addHeader(textListOrderItem[language][1],"15%");
		ctrlist.addHeader(textListOrderItem[language][2],"20%");
		ctrlist.addHeader(textListOrderItem[language][3],"5%");
		ctrlist.addHeader(textListOrderItem[language][4],"10%");
		//ctrlist.addHeader(textListOrderItem[language][5],"5%");
		ctrlist.addHeader(textListOrderItem[language][6],"5%");
		ctrlist.addHeader(textListOrderItem[language][7],"15%");
	
		ctrlist.setLinkRow(1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();
		int index = -1;
		
		if(start<0) start = 0;
			
		for(int i=0; i<objectClass.size(); i++){
			Vector temp = (Vector)objectClass.get(i);
			PurchaseOrderItem poItem = (PurchaseOrderItem)temp.get(0);
			Material mat = (Material)temp.get(1);
			Unit unit = (Unit)temp.get(2);
			MatCurrency matCurrency = (MatCurrency)temp.get(3);
			Vector rowx = new Vector();
			start = start + 1;
			
			rowx.add(""+start+"");
			rowx.add(mat.getSku());
			rowx.add(mat.getName());
			rowx.add(unit.getCode());			 
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(poItem.getCurBuyingPrice())+"</div>");
			//rowx.add("<div align=\"center\">"+matCurrency.getCode()+"</div>");

			rowx.add("<div align=\"right\">"+String.valueOf(poItem.getQuantity() - poItem.getResiduQty())+"</div>");
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal((poItem.getQuantity() - poItem.getResiduQty()) * poItem.getCurBuyingPrice())+"</div>");
			
            String name = "";
            name = mat.getName();
            name = name.replace('\"','`');
            name = name.replace('\'','`');

            if((poItem.getQuantity() - poItem.getResiduQty())!=0){
                lstData.add(rowx);
                lstLinkData.add(poItem.getMaterialId()+"','"+mat.getSku()+"','"+name+"','"+unit.getCode()+
                "','"+FRMHandler.userFormatStringDecimal(poItem.getCurBuyingPrice())+"','"+poItem.getUnitId()+"','"+(poItem.getQuantity() - poItem.getResiduQty())+
                "','"+poItem.getCurrencyId()+"','"+matCurrency.getCode()+"','"+FRMHandler.userFormatStringDecimal(poItem.getCurBuyingPrice() * (poItem.getQuantity() - poItem.getResiduQty())));
            }else{
				start = start - 1;
			}
		}
		return ctrlist.draw();
	}else{
		result = "<div class=\"msginfo\">&nbsp;&nbsp;Tidak ada item order pembelian ...</div>";				
	}	
	return result;
}
%>

<!-- JSP Block -->
<%
long oidPurchaseOrder = FRMQueryString.requestLong(request,"oidPurchaseOrder");
String materialcode = FRMQueryString.requestString(request,"mat_code");
long materialgroup = FRMQueryString.requestLong(request,"txt_materialgroup");
String materialname = FRMQueryString.requestString(request,"txt_materialname");
long oidVendor = FRMQueryString.requestLong(request,"mat_vendor");
int start = FRMQueryString.requestInt(request, "start");
int iCommand = FRMQueryString.requestCommand(request);
int recordToGet = 20;
String pageTitle = "Terima Barang > Daftar barang PO";

/**
* instantiate material object that handle searching parameter
*/
String orderBy = "MAT."+PstMaterial.fieldNames[PstMaterial.FLD_SKU];
Material objMaterial = new Material(); 
objMaterial.setCategoryId(materialgroup);
objMaterial.setSku(materialcode);
objMaterial.setName(materialname);

String whereClause = " POI." + PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_PURCHASE_ORDER_ID] +
		" = " + oidPurchaseOrder+ " AND POI."+PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_RESIDU_QTY]+" < POI."+PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_QUANTITY];

if(materialcode != "") {
	whereClause += " AND MAT."+PstMaterial.fieldNames[PstMaterial.FLD_SKU]+" like '%"+materialcode+"%'";
}

Vector vectCount = PstPurchaseOrderItem.list(0, 0, whereClause);
int vectSize = vectCount.size();

CtrlMaterial ctrlMaterial = new CtrlMaterial(request);
if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST){
	start = ctrlMaterial.actionList(iCommand, start, vectSize, recordToGet);
} 
Vector vect = PstPurchaseOrderItem.list(start,recordToGet,whereClause); 
%>
<!-- End of JSP Block -->
<html>
<head>
<title>Dimata - ProChain POS</title
><script language="JavaScript">
function cmdEdit(matOid,matCode,matItem,matUnit,matPrice,matUnitId,qtyPO,matCurrencyId,matCurrCode,amount) {	
	self.opener.document.forms.frm_recmaterial.<%=FrmMatConReceiveItem.fieldNames[FrmMatConReceiveItem.FRM_FIELD_MATERIAL_ID]%>.value = matOid;
	self.opener.document.forms.frm_recmaterial.matCode.value = matCode;
	self.opener.document.forms.frm_recmaterial.matItem.value = matItem;
	self.opener.document.forms.frm_recmaterial.matUnit.value = matUnit;
	self.opener.document.forms.frm_recmaterial.<%=FrmMatConReceiveItem.fieldNames[FrmMatConReceiveItem.FRM_FIELD_UNIT_ID]%>.value = matUnitId;	
	//self.opener.document.forms.frm_recmaterial.matCurrency.value = matCurrCode;
	self.opener.document.forms.frm_recmaterial.<%=FrmMatConReceiveItem.fieldNames[FrmMatConReceiveItem.FRM_FIELD_CURRENCY_ID]%>.value = matCurrencyId;
	self.opener.document.forms.frm_recmaterial.<%=FrmMatConReceiveItem.fieldNames[FrmMatConReceiveItem.FRM_FIELD_COST]%>.value = matPrice;		
	self.opener.document.forms.frm_recmaterial.<%=FrmMatConReceiveItem.fieldNames[FrmMatConReceiveItem.FRM_FIELD_QTY]%>.value = qtyPO;
	self.opener.document.forms.frm_recmaterial.FRM_FIELD_RESIDUE_QTY.value = qtyPO;
	//var amount = parseFloat(matPrice) * qtyPO;
	self.opener.document.forms.frm_recmaterial.<%=FrmMatConReceiveItem.fieldNames[FrmMatConReceiveItem.FRM_FIELD_TOTAL]%>.value = amount;					 	
	self.close();		
}

function cmdListFirst(){
	document.frmvendorsearch.command.value="<%=Command.FIRST%>";
	document.frmvendorsearch.action="materialpodosearch.jsp";
	document.frmvendorsearch.submit();
}

function cmdListPrev(){
	document.frmvendorsearch.command.value="<%=Command.PREV%>";
	document.frmvendorsearch.action="materialpodosearch.jsp";
	document.frmvendorsearch.submit();
}

function cmdListNext(){
	document.frmvendorsearch.command.value="<%=Command.NEXT%>";
	document.frmvendorsearch.action="materialpodosearch.jsp";
	document.frmvendorsearch.submit();
}

function cmdListLast(){
	document.frmvendorsearch.command.value="<%=Command.LAST%>";
	document.frmvendorsearch.action="materialpodosearch.jsp";
	document.frmvendorsearch.submit();
}	

function cmdSearch(){
	document.frmvendorsearch.command.value="<%=Command.LIST%>";
	document.frmvendorsearch.action="materialpodosearch.jsp";
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
          <td height="20" class="mainheader" colspan="2"><%=pageTitle%> </td>
        </tr>
        <tr> 
          <td> 
            <form name="frmvendorsearch" method="post" action="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="mat_vendor" value="<%=oidVendor%>">
              <input type="hidden" name="oidPurchaseOrder" value="<%=oidPurchaseOrder%>">
              <table width="100%" border="0" cellspacing="1" cellpadding="1">
                <tr> 
                </tr>
                <tr> 
                </tr>
                <tr> 
                </tr>
                <tr> 
                  <td colspan="2"><%=drawListMaterial(SESS_LANGUAGE,vect,start)%></td>
                </tr>
                <tr> 
                  <td colspan="2">
				    <span class="command"> 
                    <% 
						ControlLine ctrlLine= new ControlLine();
						ctrlLine.setLocationImg(approot+"/images");
						ctrlLine.initDefault();
						out.println(ctrlLine.drawImageListLimit(iCommand,vectSize,start,recordToGet));
					 %>
				  </span></td>
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
</html>

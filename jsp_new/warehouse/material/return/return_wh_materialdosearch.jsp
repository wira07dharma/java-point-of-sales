<%@page contentType="text/html"%>
<%@ page import = "java.util.*" %>
<%@ page import = "com.dimata.util.*" %>
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<%@ page import = "com.dimata.posbo.entity.masterdata.*" %>
<%@ page import = "com.dimata.posbo.entity.warehouse.*" %>
<%@ page import = "com.dimata.posbo.form.warehouse.*" %>
<%@ page import = "com.dimata.posbo.form.masterdata.*" %>
<%@ page import = "com.dimata.common.entity.payment.CurrencyType" %>
<%@ page import = "com.dimata.common.entity.payment.PstCurrencyType" %>

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RETURN, AppObjInfo.G2_SUPPLIER_RETURN, AppObjInfo.OBJ_SUPPLIER_RETURN); %>
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
	{"No","Kategori","Sku","Nama Barang","Unit","Harga Beli","Qty"},
	{"No","Category","Code","Goods Name","Unit","Cost","Qty"}
};

public long getOidDfItem(Vector vectDf, long oidmaterial){
	long oidreturn = 0;
	if(vectDf!=null && vectDf.size()>0){
		for(int k=0;k<vectDf.size();k++){
			MatReturnItem matDf = (MatReturnItem)vectDf.get(k);
			if(matDf.getMaterialId()==oidmaterial){
				oidreturn = matDf.getOID();
				break;
			}
		}
	}
	return oidreturn;
}

public String drawListMaterial(int language,Vector objectClass, Vector vectDf, int start)
{
	String result = "";
	if(objectClass!=null && objectClass.size()>0){
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");

		ctrlist.addHeader(textListMaterialHeader[language][0],"5%");
		//ctrlist.addHeader(textListMaterialHeader[language][1],"20%");
		ctrlist.addHeader(textListMaterialHeader[language][2],"15%");
		ctrlist.addHeader(textListMaterialHeader[language][3],"20%");
		ctrlist.addHeader(textListMaterialHeader[language][6],"10%");
		ctrlist.addHeader(textListMaterialHeader[language][4],"10%");
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

		for(int i=0; i<objectClass.size(); i++)	{
			//MatReceiveItem matReceiveItem = (MatReceiveItem)objectClass.get(i);
			
			Vector vt = (Vector)objectClass.get(i);
			MatReceiveItem matReceiveItem = (MatReceiveItem)vt.get(0);
            Material material = (Material)vt.get(1);
            Unit unit = (Unit)vt.get(2);
            CurrencyType matCurr = (CurrencyType)vt.get(3);
			
			/*Material material = (Material)vt.get(0);
			Category categ = (Category)vt.get(1);
			Unit unit = (Unit)vt.get(2);
			MatCurrency matCurr = (MatCurrency)vt.get(3);*/

			/*Material material = new Material();
			try{
				material = PstMaterial.fetchExc(matReceiveItem.getMaterialId());
			}catch(Exception e){}*/

			//Category categ = new Category();
			//try{
				//categ = PstCategory.fetchExc(material.getCategoryId());
			//}catch(Exception e){}

			/*Unit unit = new Unit();
			try{
				unit = PstUnit.fetchExc(matReceiveItem.getUnitId());//PstUnit.fetchExc(material.getDefaultStockUnitId());
			}catch(Exception e){}

			CurrencyType matCurr = new CurrencyType();
			try{
				matCurr = PstCurrencyType.fetchExc(matReceiveItem.getCurrencyId());//PstCurrencyType.fetchExc(material.getDefaultPriceCurrencyId());
			}catch(Exception e){}*/

			Vector rowx = new Vector();
			if(matReceiveItem.getResidueQty()>0){
				start = start + 1;
				rowx.add(""+start);
				//rowx.add(categ.getName());
				rowx.add("<div align=\"center\">"+material.getSku()+"</div>");
				rowx.add(material.getName());
				rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(matReceiveItem.getResidueQty())+"</div>");
				rowx.add("<div align=\"center\">"+unit.getCode()+"</div>");
				rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(matReceiveItem.getCost())+"</div>");

				lstData.add(rowx);
				long oid = getOidDfItem(vectDf,material.getOID());

                String name = "";
                name = material.getName();
                name = name.replace('\"','`');
                name = name.replace('\'','`');

				lstLinkData.add(matReceiveItem.getMaterialId()+"','"+material.getSku()+"','"+name+"','"+unit.getCode()+
					"','"+FRMHandler.userFormatStringDecimal(matReceiveItem.getCost())+"','"+matReceiveItem.getUnitId()+
					"','"+matCurr.getOID()+"','"+matCurr.getCode()+"','"+matReceiveItem.getOID()+"','"+oid+"', '"+matReceiveItem.getResidueQty());

				/*lstLinkData.add(material.getOID()+"','"+material.getSku()+"','"+material.getName()+"','"+unit.getCode()+
					"','"+material.getDefaultCost()+"','"+material.getDefaultStockUnitId()+
					"','"+material.getDefaultCostCurrencyId()+"','"+matCurr.getCode()+"','"+matReceiveItem.getOID()+"','"+oid);*/
			}
		}
		return ctrlist.draw();
	}
	else
	{
		result = "<div class=\"msginfo\">&nbsp;&nbsp;Tidak ada data item ...</div>";
	}
	return result;
}
%>

<!-- JSP Block -->
<%
String invoice = FRMQueryString.requestString(request,"invoice_supplier");
String materialcode = FRMQueryString.requestString(request,"mat_code");
long materialgroup = FRMQueryString.requestLong(request,"txt_materialgroup");
long oidReturnMaterial = FRMQueryString.requestLong(request,"hidden_return_id");
String materialname = FRMQueryString.requestString(request,"txt_materialname");
int start = FRMQueryString.requestInt(request, "start");
int iCommand = FRMQueryString.requestCommand(request);
int recordToGet = 20;
String pageTitle = "Gudang > Pencarian Barang";

/**
* instantiate material object that handle searching parameter
*/
String whereClause = "";
String orderBy = "MAT."+PstMaterial.fieldNames[PstMaterial.FLD_SKU];
Material objMaterial = new Material();
objMaterial.setCategoryId(materialgroup);
objMaterial.setSku(materialcode);
objMaterial.setName(materialname);

/**
* get material list will displayed in this page
*/
whereClause = PstMatReceive.fieldNames[PstMatReceive.FLD_INVOICE_SUPPLIER]+"='"+invoice+"'";
//whereClause+= " AND "+PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE]+"="+PstMatReceive.SOURCE_FROM_SUPPLIER_PO;

Vector vt = PstMatReceive.list(0,0,whereClause,"");
MatReceive matreceive = new MatReceive();
if(vt!=null && vt.size()>0){
	matreceive = (MatReceive)vt.get(0);
}

whereClause = PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]+"="+matreceive.getOID();
if(materialcode != "") {
	whereClause += " AND MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " like '%" + materialcode + "%'";
}

Vector vectCount = PstMatReceiveItem.list(0, 0, whereClause);
int vectSize = vectCount.size();
/**
* generate start/mailstone for displaying data
*/
CtrlMaterial ctrlMaterial = new CtrlMaterial(request);
if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST){
	start = ctrlMaterial.actionList(iCommand, start, vectSize, recordToGet);
}
Vector vect = PstMatReceiveItem.list(start, recordToGet, whereClause); //PstMaterial.getListMaterialItem(0,objMaterial,start,recordToGet, orderBy);

whereClause = PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID]+"="+oidReturnMaterial;
Vector vectDf = PstMatReturnItem.list(0,0,whereClause,""); //PstMaterial.getListMaterialItem(0,objMaterial,start,recordToGet, orderBy);

%>
<!-- End of JSP Block -->
<html>
<head>
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
function cmdEdit(matOid,matCode,matItem,matUnit,matPrice,matUnitId,matCurrencyId,matCurrCode,returnId,oidDfItem,availableQtyToReturn) {
	if(oidDfItem==0){
		self.opener.frm_retmaterial.<%=FrmMatReturnItem.fieldNames[FrmMatReturnItem.FRM_FIELD_MATERIAL_ID]%>.value = matOid;
		self.opener.frm_retmaterial.matCode.value = matCode;
		self.opener.frm_retmaterial.matItem.value = matItem;
		self.opener.frm_retmaterial.matUnit.value = matUnit;
		self.opener.frm_retmaterial.<%=FrmMatReturnItem.fieldNames[FrmMatReturnItem.FRM_FIELD_UNIT_ID]%>.value = matUnitId;
		//self.opener.frm_retmaterial.matCurrency.value = matCurrCode;
		self.opener.frm_retmaterial.<%=FrmMatReturnItem.fieldNames[FrmMatReturnItem.FRM_FIELD_CURRENCY_ID]%>.value = matCurrencyId;
		self.opener.frm_retmaterial.<%=FrmMatReturnItem.fieldNames[FrmMatReturnItem.FRM_FIELD_COST]%>.value = matPrice;
		self.opener.frm_retmaterial.hidden_return_item_id.value = returnId;
		self.opener.frm_retmaterial.available_qty_to_return.value = availableQtyToReturn;
		self.opener.frm_retmaterial.<%=FrmMatReturnItem.fieldNames[FrmMatReturnItem.FRM_FIELD_QTY]%>.focus();
	} else {
		self.opener.frm_retmaterial.hidden_return_item_id.value = oidDfItem;
		self.opener.frm_retmaterial.command.value = "<%=Command.EDIT%>";
		self.opener.frm_retmaterial.submit();
	}
	self.close();
}

function cmdListFirst() {
	document.frmvendorsearch.command.value="<%=Command.FIRST%>";
	document.frmvendorsearch.action="return_wh_materialdosearch.jsp";
	document.frmvendorsearch.submit();
}

function cmdListPrev() {
	document.frmvendorsearch.command.value="<%=Command.PREV%>";
	document.frmvendorsearch.action="return_wh_materialdosearch.jsp";
	document.frmvendorsearch.submit();
}

function cmdListNext() {
	document.frmvendorsearch.command.value="<%=Command.NEXT%>";
	document.frmvendorsearch.action="return_wh_materialdosearch.jsp";
	document.frmvendorsearch.submit();
}

function cmdListLast() {
	document.frmvendorsearch.command.value="<%=Command.LAST%>";
	document.frmvendorsearch.action="return_wh_materialdosearch.jsp";
	document.frmvendorsearch.submit();
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
              <input type="hidden" name="invoice_supplier" value="<%=invoice%>">
			  <input type="hidden" name="hidden_dispatch_id" value="<%=oidReturnMaterial%>">
              <table width="100%" border="0" cellspacing="1" cellpadding="1">
                <tr>
                  <td colspan="2"><%=drawListMaterial(SESS_LANGUAGE, vect, vectDf, start)%></td>
                </tr>
                <tr>
                  <td colspan="2"><span class="command">
                    <%
						ControlLine ctrlLine= new ControlLine();
						ctrlLine.setLocationImg(approot+"/images");
						ctrlLine.initDefault();
					 %>
                    <%=ctrlLine.drawImageListLimit(iCommand ,vectSize, start, recordToGet)%> </span></td>
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

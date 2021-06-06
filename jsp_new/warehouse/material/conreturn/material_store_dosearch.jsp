
<%@page contentType="text/html"%>
<!-- package java -->
<%@ page import = "java.util.*" %> 
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<%@ page import = "com.dimata.posbo.entity.masterdata.*" %>
<%@ page import = "com.dimata.posbo.entity.warehouse.*" %>
<%@ page import = "com.dimata.posbo.form.warehouse.*" %>
<%@ page import = "com.dimata.posbo.form.masterdata.*" %>
<%@ page import = "com.dimata.common.entity.payment.*" %>

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RETURN, AppObjInfo.G2_SUPPLIER_RETURN, AppObjInfo.OBJ_SUPPLIER_RETURN); %>
<%@ include file = "../../../main/checkuser.jsp" %>


<%!
public static final String textListGlobal[][] = {
	{"Gudang","Retur Barang","Pencarian Barang"},
	{"Warehouse","Goods Return","Goods Searching"}
};

/* this constant used to list text of listHeader */
public static final String textMaterialHeader[][] = {
	{"Kategori","Sku","Nama Barang"},
	{"Category","Material Code","Material Name"}
};

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] = { 
	{"No","Sku","Nama Barang","Unit","HPP","Qty","Harga Jual"},
	{"No","Code","Item","Unit","Cost","Qty","Sell Price"}
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


public String drawListMaterial(int language,Vector objectClass, Vector vectDf, int start){
	String result = "";
	if(objectClass!=null && objectClass.size()>0){
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%"); 
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell"); 
		ctrlist.setHeaderStyle("listgentitle");
		
		ctrlist.addHeader(textListMaterialHeader[language][0],"5%");
		ctrlist.addHeader(textListMaterialHeader[language][1],"15%");					
		ctrlist.addHeader(textListMaterialHeader[language][2],"42%");	
		ctrlist.addHeader(textListMaterialHeader[language][3],"8%");					
		ctrlist.addHeader(textListMaterialHeader[language][5],"15%");		
		ctrlist.addHeader(textListMaterialHeader[language][4],"10%");	
		
		
		ctrlist.setLinkRow(2);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();
		int index = -1;
		
		if(start<0) start = 0;
			
		for(int i=0; i<objectClass.size(); i++){
			MaterialStock materialStock = (MaterialStock)objectClass.get(i);
			
			Material material = new Material();
			try{
				material = PstMaterial.fetchExc(materialStock.getMaterialUnitId());
			}catch(Exception e){
				System.out.println("Exc when PstMaterial.fetchExc() : " + e.toString());
			}
			
			Unit unit = new Unit();
			try{
				unit = PstUnit.fetchExc(material.getDefaultSellUnitId());
			}catch(Exception e){
				System.out.println("Exc when PstUnit.fetchExc() : " + e.toString());
			}
			
			CurrencyType currencyType = new CurrencyType();
			try{
				currencyType = PstCurrencyType.fetchExc(material.getDefaultCostCurrencyId());
			}catch(Exception e){
				System.out.println("Exc when PstMatCurrency.fetchExc() : " + e.toString());			
			}	
					
			Vector rowx = new Vector();	
		
			if(materialStock.getQty() > 0){
				start = start + 1;
				
				double qtyPerbaseUnit = PstUnit.getQtyPerBaseUnit(material.getBuyUnitId(), material.getDefaultSellUnitId());
                double sellPrice = PstPriceTypeMapping.getSellPrice(material.getOID(),PstPriceTypeMapping.getOidStandartRate(),PstPriceTypeMapping.getOidPriceType());
				double buyPricePerBaseUnit = material.getDefaultCost();

                material.getAveragePrice(); //qtyPerbaseUnit;
				
				rowx.add(""+start);		
				rowx.add("<div align=\"center\">"+material.getSku()+"</div>");		
				rowx.add(material.getName());
				rowx.add("<div align=\"center\">"+unit.getCode()+"</div>");		
				rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(materialStock.getQty())+"</div>");
				rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(buyPricePerBaseUnit)+"</div>");						
		
				lstData.add(rowx);
				long oid = getOidDfItem(vectDf, material.getOID());

                String name = "";
                name = material.getName();
                name = name.replace('\"','`');
                name = name.replace('\'','`');

				lstLinkData.add(material.getOID()+"','"+material.getSku()+"','"+name+"','"+unit.getCode()+
					"','"+FRMHandler.userFormatStringDecimal(buyPricePerBaseUnit)+"','"+material.getDefaultSellUnitId()+
					"','"+material.getDefaultCostCurrencyId()+"','"+currencyType.getCode()+"','"+materialStock.getOID()+"','"+oid);													
			}
		}
		return ctrlist.draw();
	}else{
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
long oidSupplier = FRMQueryString.requestLong(request,"mat_vendor");
long oidLocation = FRMQueryString.requestLong(request,"hidden_location_id");

String materialname = FRMQueryString.requestString(request,"txt_materialname");
int start = FRMQueryString.requestInt(request, "start");
int iCommand = FRMQueryString.requestCommand(request);
int recordToGet = 20;
String pageTitle = "Warehouse > Retur > Search";

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
* get amount/count of material's list
*/
int vectSize = PstMaterial.getCountMaterialItem(0,objMaterial);

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
whereClause = PstMatReceive.fieldNames[PstMatReceive.FLD_INVOICE_SUPPLIER]+"='"+invoice+"'"; 

Vector vt = PstMatReceive.list(0,0,whereClause,"");
MatReceive matreceive = new MatReceive();
if(vt!=null && vt.size()>0){
	matreceive = (MatReceive)vt.get(0);
}

Vector vectStock = PstMaterialStock.listMaterialStockCurrPeriod(oidSupplier,oidLocation);
whereClause = PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID]+"="+oidReturnMaterial; 
Vector vectDf = PstMatReturnItem.list(0,0,whereClause,"");
    if(vectStock.size()==0){
        vectStock = PstMaterialStock.listMaterialStockCurrPeriod(0,oidLocation);
    }

%>
<!-- End of JSP Block -->
<html>
<head>
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
function cmdEdit(matOid,matCode,matItem,matUnit,matPrice,matUnitId,matCurrencyId,matCurrCode,returnId,oidDfItem)
{	
	if(oidDfItem==0){
		self.opener.frm_retmaterial.<%=FrmMatReturnItem.fieldNames[FrmMatReturnItem.FRM_FIELD_MATERIAL_ID]%>.value = matOid;
		self.opener.frm_retmaterial.matCode.value = matCode;
		self.opener.frm_retmaterial.matItem.value = matItem;
		self.opener.frm_retmaterial.matUnit.value = matUnit;
		self.opener.frm_retmaterial.<%=FrmMatReturnItem.fieldNames[FrmMatReturnItem.FRM_FIELD_UNIT_ID]%>.value = matUnitId;	
		self.opener.frm_retmaterial.currencyType.value = matCurrCode;
		self.opener.frm_retmaterial.<%=FrmMatReturnItem.fieldNames[FrmMatReturnItem.FRM_FIELD_CURRENCY_ID]%>.value = matCurrencyId;	
		self.opener.frm_retmaterial.<%=FrmMatReturnItem.fieldNames[FrmMatReturnItem.FRM_FIELD_COST]%>.value = matPrice;

        self.opener.frm_retmaterial.<%=FrmMatReturnItem.fieldNames[FrmMatReturnItem.FRM_FIELD_QTY]%>.value = ""
		self.opener.frm_retmaterial.<%=FrmMatReturnItem.fieldNames[FrmMatReturnItem.FRM_FIELD_TOTAL]%>.value = 0;

		self.opener.frm_retmaterial.hidden_return_item_id.value = returnId;	
		self.opener.frm_retmaterial.<%=FrmMatReturnItem.fieldNames[FrmMatReturnItem.FRM_FIELD_QTY]%>.focus();	
	}else{
		self.opener.frm_retmaterial.hidden_return_item_id.value = oidDfItem;
		self.opener.frm_retmaterial.command.value = "<%=Command.EDIT%>";
		self.opener.frm_retmaterial.submit();
	}
	self.close();		
}
</script>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="../../../styles/main.css" type="text/css">
<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
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
          <td height="20" class="mainheader" colspan="2">
		  	&nbsp;<%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][1]%> &gt; <%=textListGlobal[SESS_LANGUAGE][2]%>
		  </td>
        </tr>
        <tr> 
          <td> 
            <form name="frmvendorsearch" method="post" action="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="command" value="<%=iCommand%>">
			  <input type="hidden" name="hidden_dispatch_id" value="<%=oidReturnMaterial%>">
              <input type="hidden" name="hidden_location_id" value="<%=oidLocation%>">
              <table width="100%" border="0" cellspacing="1" cellpadding="1">
                <tr>
                  <td colspan="2">
				  <%=drawListMaterial(SESS_LANGUAGE,vectStock,vectDf,start)%>
				  </td>
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

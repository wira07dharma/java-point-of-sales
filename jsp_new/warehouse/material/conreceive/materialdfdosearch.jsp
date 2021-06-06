
<%@page contentType="text/html"%>
<!-- package java -->
<%@ page import = "java.util.*,
                   com.dimata.posbo.entity.masterdata.*,
                   com.dimata.posbo.entity.warehouse.*,
                   com.dimata.posbo.form.masterdata.CtrlMaterial,
                   com.dimata.posbo.form.warehouse.FrmMatConReceiveItem,
				   com.dimata.common.entity.payment.CurrencyType,
				   com.dimata.common.entity.payment.PstCurrencyType" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_LOCATION_RECEIVE, AppObjInfo.OBJ_LOCATION_RECEIVE); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<%!
/* this constant used to list text of listHeader */
public static final String textMaterialHeader[][] = 
{
	{"Kategori","Sku","Nama Material"},
	{"Category","Material Code","Material Name"}
};

/* this constant used to list text of listHeader */
public static final String textListOrderItem[][] = 
{ 
	{"No","Sku","Nama Barang","Unit","Harga","Mata Uang","Jumlah","Total","Harga Jual"},
	{"No","Code","Name","Unit","Price","Currency","Quantity","Total","Selling Price"}
};

/*
public String drawListMaterial(int language,Vector objectClass,int start)
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
			
		for(int i=0; i<objectClass.size(); i++)
		{
			Vector temp = (Vector)objectClass.get(i);
			MatConDispatchItem dfItem = (MatConDispatchItem)temp.get(0);
			Material mat = (Material)temp.get(1);
			Unit unit = (Unit)temp.get(2);
			MatCurrency matCurrency = (MatCurrency)temp.get(3);
			Vector rowx = new Vector();
			start = start + 1;
			
			rowx.add(""+start+"");
			rowx.add(mat.getSku());
			rowx.add(mat.getName());
			rowx.add(unit.getCode());			 
			rowx.add("<div align=\"right\">"+Formater.formatNumber(mat.getDefaultCost(),"##,###.00")+"</div>");
			//rowx.add("<div align=\"center\">"+matCurrency.getCode()+"</div>");
			rowx.add("<div align=\"right\">"+String.valueOf(dfItem.getQty())+"</div>");			 			 
			rowx.add("<div align=\"right\">"+Formater.formatNumber(dfItem.getQty() * mat.getDefaultCost(),"##,###.00")+"</div>");
			
			lstData.add(rowx);
			lstLinkData.add(dfItem.getMaterialId()+"','"+mat.getSku()+"','"+mat.getName()+"','"+unit.getCode()+
			"','"+mat.getDefaultCost()+"','"+dfItem.getUnitId()+"','"+dfItem.getQty()+
			"','"+mat.getDefaultCostCurrencyId()+"','"+matCurrency.getCode()+"','"+(mat.getDefaultCost()*dfItem.getQty()));
		}
		return ctrlist.draw();
	}
	else
	{
		result = "<div class=\"msginfo\">&nbsp;&nbsp;No Material Available ...</div>";				
	}	
	return result;
}
*/

public long getOidRecItem(Vector vectRec, long oidmaterial){
	long oidreturn = 0;
	if(vectRec!=null && vectRec.size()>0){
		for(int k=0;k<vectRec.size();k++){
			MatConReceiveItem matRec = (MatConReceiveItem)vectRec.get(k);
			if(matRec.getMaterialId()==oidmaterial){
				oidreturn = matRec.getOID();
				break;
			}
		}
	}
	return oidreturn;
}

public String drawListMaterial(int language,Vector objectClass, Vector vectRec, int start, int tranUsedPriceHpp){
	String result = "";
	if(objectClass!=null && objectClass.size()>0){
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%"); 
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		System.out.println("\n>>> masuk drawListMaterial");
		/*
		ctrlist.addHeader(textListMaterialHeader[language][0],"3%");	
		ctrlist.addHeader(textListMaterialHeader[language][1],"20%");	
		ctrlist.addHeader(textListMaterialHeader[language][2],"12%");					
		ctrlist.addHeader(textListMaterialHeader[language][3],"30%");					
		ctrlist.addHeader(textListMaterialHeader[language][4],"5%");		
		ctrlist.addHeader(textListMaterialHeader[language][5],"10%");	
		*/
		
		ctrlist.addHeader(textListOrderItem[language][0],"3%");
		ctrlist.addHeader(textListOrderItem[language][1],"15%");
		ctrlist.addHeader(textListOrderItem[language][2],"20%");
		ctrlist.addHeader(textListOrderItem[language][3],"5%");
		if(tranUsedPriceHpp==0)
			ctrlist.addHeader(textListOrderItem[language][4],"10%");
		else	
			ctrlist.addHeader(textListOrderItem[language][8],"10%");
		
		ctrlist.addHeader(textListOrderItem[language][6],"5%");
		ctrlist.addHeader(textListOrderItem[language][7],"15%");		
	
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
			/*
			Vector vt = (Vector)objectClass.get(i);			
			Material material = (Material)vt.get(0); 
			Category categ = (Category)vt.get(1);
			Unit unit = (Unit)vt.get(2);
			MatCurrency matCurr = (MatCurrency)vt.get(3);
			*/
			
			MatConDispatchItem matDispatchItem = (MatConDispatchItem)objectClass.get(i);
			
			Material material = new Material();
			try{
				material = PstMaterial.fetchExc(matDispatchItem.getMaterialId());
			}catch(Exception e){}
			
			/*Category categ = new Category();
			try{
				categ = PstCategory.fetchExc(material.getCategoryId());
			}catch(Exception e){}*/
			
			Unit unit = new Unit();
			try{
				unit = PstUnit.fetchExc(matDispatchItem.getUnitId());//PstUnit.fetchExc(material.getDefaultSellUnitId());
			}catch(Exception e){}
			
			CurrencyType matCurr = new CurrencyType();
			try{
				matCurr = PstCurrencyType.fetchExc(material.getDefaultCostCurrencyId());
			}catch(Exception e){}			
						
			Vector rowx = new Vector();
			
			if(matDispatchItem.getResidueQty()>0){
				start = start + 1;			
				
				/*
				rowx.add(""+start);		
				rowx.add(categ.getName());
				rowx.add(material.getSku());		
				rowx.add(material.getName());		
				rowx.add(unit.getCode());
				rowx.add(""+material.getDefaultCost());
				long oid = getOidRecItem(vectRec,material.getOID());		
		
				lstData.add(rowx);
				lstLinkData.add(material.getOID()+"','"+material.getSku()+"','"+material.getName()+"','"+unit.getCode()+
					"','"+material.getDefaultCost()+"','"+material.getDefaultSellUnitId()+
					"','"+material.getDefaultCostCurrencyId()+"','"+matCurr.getCode()+"','"+oid);					
				*/
				
				rowx.add(""+start+"");
				rowx.add(material.getSku());
				rowx.add(material.getName());
				rowx.add(unit.getCode());			 
				rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(matDispatchItem.getHpp())+"</div>");
				rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(matDispatchItem.getResidueQty())+"</div>");			 			 
				rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(matDispatchItem.getHppTotal())+"</div>");
				long oid = getOidRecItem(vectRec,material.getOID());

                String name = "";
                name = material.getName();
                name = name.replace('\"','`');
                name = name.replace('\'','`');

				lstData.add(rowx);
				lstLinkData.add(matDispatchItem.getMaterialId()+"','"+material.getSku()+"','"+name+"','"+unit.getCode()+
				"','"+FRMHandler.userFormatStringDecimal(matDispatchItem.getHpp())+"','"+matDispatchItem.getUnitId()+"','"+matDispatchItem.getResidueQty()+
				"','"+material.getDefaultCostCurrencyId()+"','"+matCurr.getCode()+"','"+FRMHandler.userFormatStringDecimal(matDispatchItem.getHppTotal())+"','"+oid);
			}		
		}
		return ctrlist.draw();
	}
	else
	{
		result = "<div class=\"msginfo\">&nbsp;&nbsp;Tidak ada data material ...</div>";
	}	
	return result;
}
%>

<!-- JSP Block -->
<%
long matReceiveOid = FRMQueryString.requestLong(request,"mat_receive_oid");
long oidDispatch = FRMQueryString.requestLong(request,"oidDispatch");
String materialcode = FRMQueryString.requestString(request,"mat_code");
long materialgroup = FRMQueryString.requestLong(request,"txt_materialgroup");
String materialname = FRMQueryString.requestString(request,"txt_materialname");
int start = FRMQueryString.requestInt(request, "start");
int iCommand = FRMQueryString.requestCommand(request);
int recordToGet = 20;
String pageTitle = "Daftar Barang Yang Ditransfer Dari Gudang";

/**
* instantiate material object that handle searching parameter
*/
String orderClause = "MAT."+PstMaterial.fieldNames[PstMaterial.FLD_SKU]; 
Material objMaterial = new Material(); 
objMaterial.setCategoryId(materialgroup);
objMaterial.setSku(materialcode);
objMaterial.setName(materialname);

/*
String whereClause = " DFI." + PstMatConDispatchItem.fieldNames[PstMatConDispatchItem.FLD_DISPATCH_MATERIAL_ID] +
		" = " + oidDispatch;
Vector vect = PstMatConDispatchItem.list(whereClause,orderClause);
*/

String whereClause = PstMatConReceiveItem.fieldNames[PstMatConReceiveItem.FLD_RECEIVE_MATERIAL_ID]+"="+matReceiveOid;
Vector vectRec = PstMatConReceiveItem.list(0,0,whereClause,""); 

whereClause = PstMatConDispatch.fieldNames[PstMatConDispatch.FLD_DISPATCH_MATERIAL_ID] + " = " + oidDispatch;
Vector vect = com.dimata.posbo.entity.warehouse.PstMatConDispatchItem.list(0,0,whereClause,"");

int vectSize = vect.size();

CtrlMaterial ctrlMaterial = new CtrlMaterial(request);
if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST){
	start = ctrlMaterial.actionList(iCommand, start, vectSize, recordToGet);
} 
 
%>
<!-- End of JSP Block -->
<html>
<head>
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
function cmdEdit(matOid,matCode,matItem,matUnit,matPrice,matUnitId,qtyDF,matCurrencyId,matCurrCode,matTotal,oid){	
	if(oid==0){
		self.opener.frm_recmaterial.<%=FrmMatConReceiveItem.fieldNames[FrmMatConReceiveItem.FRM_FIELD_MATERIAL_ID]%>.value = matOid;
		self.opener.frm_recmaterial.matCode.value = matCode;
		self.opener.frm_recmaterial.matItem.value = matItem;
		self.opener.frm_recmaterial.matUnit.value = matUnit;
		self.opener.frm_recmaterial.<%=FrmMatConReceiveItem.fieldNames[FrmMatConReceiveItem.FRM_FIELD_UNIT_ID]%>.value = matUnitId;	
		//self.opener.frm_recmaterial.matCurrency.value = matCurrCode;
		self.opener.frm_recmaterial.<%=FrmMatConReceiveItem.fieldNames[FrmMatConReceiveItem.FRM_FIELD_CURRENCY_ID]%>.value = matCurrencyId;	
		self.opener.frm_recmaterial.<%=FrmMatConReceiveItem.fieldNames[FrmMatConReceiveItem.FRM_FIELD_COST]%>.value = matPrice;		
		self.opener.frm_recmaterial.<%=FrmMatConReceiveItem.fieldNames[FrmMatConReceiveItem.FRM_FIELD_QTY]%>.value = qtyDF;
		self.opener.frm_recmaterial.<%=FrmMatConReceiveItem.fieldNames[FrmMatConReceiveItem.FRM_FIELD_TOTAL]%>.value = matTotal;	
		self.opener.frm_recmaterial.<%=FrmMatConReceiveItem.fieldNames[FrmMatConReceiveItem.FRM_FIELD_QTY]%>.focus();		
	}else{
		self.opener.frm_recmaterial.hidden_receive_item_id.value = oid;
		self.opener.frm_recmaterial.command.value = "<%=Command.EDIT%>";
		self.opener.frm_recmaterial.submit();		
	}		
	self.close();		
}

function cmdListFirst(){
	document.frmvendorsearch.command.value="<%=Command.FIRST%>";
	document.frmvendorsearch.action="materialdfdosearch.jsp";
	document.frmvendorsearch.submit();
}

function cmdListPrev(){
	document.frmvendorsearch.command.value="<%=Command.PREV%>";
	document.frmvendorsearch.action="materialdfdosearch.jsp";
	document.frmvendorsearch.submit();
}

function cmdListNext(){
	document.frmvendorsearch.command.value="<%=Command.NEXT%>";
	document.frmvendorsearch.action="materialdfdosearch.jsp";
	document.frmvendorsearch.submit();
}

function cmdListLast(){
	document.frmvendorsearch.command.value="<%=Command.LAST%>";
	document.frmvendorsearch.action="materialdfdosearch.jsp";
	document.frmvendorsearch.submit();
}	

function cmdSearch(){
	document.frmvendorsearch.command.value="<%=Command.LIST%>";
	document.frmvendorsearch.action="materialdfdosearch.jsp";
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
          <td colspan="2"> 
            <hr size="1">
          </td>
        </tr>
        <tr> 
          <td> 
            <form name="frmvendorsearch" method="post" action="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="oidDispatch" value="<%=oidDispatch%>">
              <table width="100%" border="0" cellspacing="1" cellpadding="1">
                <tr> 
                </tr>
                <tr> 
                </tr>
                <tr> 
                </tr>
                <tr> 
                  <td colspan="2">
				  <%//=drawListMaterial(SESS_LANGUAGE,vect,start)%>
				  <%=drawListMaterial(SESS_LANGUAGE,vect,vectRec,start,tranUsedPriceHpp)%>
				  </td>
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
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
</body>
</html>

<%@ page import="com.dimata.posbo.entity.warehouse.MatCostingItem,
                 com.dimata.posbo.form.warehouse.FrmMatCostingItem,
                 com.dimata.gui.jsp.ControlList,
                 com.dimata.qdep.form.FRMHandler,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.util.Command,
                 com.dimata.gui.jsp.ControlCombo,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.posbo.entity.warehouse.MatConDispatchItem,
                 com.dimata.posbo.entity.masterdata.*,
                 com.dimata.posbo.form.masterdata.CtrlMaterial,
                 com.dimata.posbo.entity.warehouse.PstMatConDispatchItem,
                 com.dimata.posbo.form.warehouse.FrmMatConDispatchItem"%>
<%@page contentType="text/html"%>
<!-- package java -->

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER, AppObjInfo.OBJ_TRANSFER); %>
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
	{"No","Sku","Nama Barang","Unit","Qty Stok"},
	{"No","Code","Item Name","Unit","Stock Qty"}
};

public long getOidDfItem(Vector vectDf, long oidmaterial){
	long oidDispatch = 0;
	if(vectDf!=null && vectDf.size()>0){
		for(int k=0;k<vectDf.size();k++){
			MatConDispatchItem matDf = (MatConDispatchItem)vectDf.get(k);
			if(matDf.getMaterialId()==oidmaterial){
				oidDispatch = matDf.getOID();
				break;
			}
		}
	}
	return oidDispatch;
}

public String drawListMaterial(int language,Vector objectClass, Vector vectDf, int start, int tranUsedPriceHpp)
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
		ctrlist.addHeader(textListMaterialHeader[language][1],"15%");
		ctrlist.addHeader(textListMaterialHeader[language][2],"35%");
		ctrlist.addHeader(textListMaterialHeader[language][3],"5%");
		ctrlist.addHeader(textListMaterialHeader[language][4],"15%");

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
			Vector list = (Vector)objectClass.get(i);
			Material material = (Material)list.get(0);
            MaterialConStock materialStock = (MaterialConStock)list.get(1);
            Vector rowx = new Vector();
            Unit unit = new Unit();
            System.out.println("oid sell unit : "+material.getDefaultSellUnitId());
            try{
                unit = PstUnit.fetchExc(material.getDefaultSellUnitId()); //PstUnit.fetchExc(material.getBuyUnitId());
            }catch(Exception e){}

            start = start + 1;
            rowx.add("<div align=\"center\">"+start+"</div>");
            rowx.add("<div align=\"center\">"+material.getSku()+"</div>");
            rowx.add(material.getName());
            rowx.add("<div align=\"center\">"+unit.getCode()+"</div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(materialStock.getQty())+"</div>");

            lstData.add(rowx);
            long oid = getOidDfItem(vectDf,material.getOID());

            String name = "";
            name = material.getName();
            name = name.replace('\"','`');
            name = name.replace('\'','`');

            if(tranUsedPriceHpp==0){
                lstLinkData.add(material.getOID()+"','"+material.getSku()+"','"+name+"','"+unit.getCode()+
                    "','"+FRMHandler.userFormatStringDecimal(material.getAveragePrice())+"','"+material.getDefaultSellUnitId()+"','"+oid);
            }else{
                double sellPrice = PstPriceTypeMapping.getSellPrice(material.getOID(),PstPriceTypeMapping.getOidStandartRate(),PstPriceTypeMapping.getOidPriceType());
                lstLinkData.add(material.getOID()+"','"+material.getSku()+"','"+name+"','"+unit.getCode()+
                    "','"+FRMHandler.userFormatStringDecimal(sellPrice)+"','"+material.getDefaultSellUnitId()+"','"+oid);
            }
        }
		return ctrlist.draw();
	}
	else{
		result = "<div class=\"msginfo\">&nbsp;&nbsp;Tidak ada data barang ...</div>";				
	}	
	return result;
}
%>

<!-- JSP Block -->
<%
String materialcode = FRMQueryString.requestString(request,"mat_code");
long materialgroup = FRMQueryString.requestLong(request,"txt_materialgroup");
long locationId = FRMQueryString.requestLong(request,"location_id");
long periodeId = FRMQueryString.requestLong(request,"periode_id");
long oidDispatchMaterial = FRMQueryString.requestLong(request,"hidden_dispatch_id");
String materialname = FRMQueryString.requestString(request,"txt_materialname");
int start = FRMQueryString.requestInt(request, "start");
int iCommand = FRMQueryString.requestCommand(request);
int recordToGet = 15;
String pageTitle = "Konsinyasi > Pengiriman > Pencarian Barang";

String whereClause = "";
String orderBy = "MAT."+PstMaterialConStock.fieldNames[PstMaterialConStock.FLD_QTY];
Material objMaterial = new Material(); 
objMaterial.setCategoryId(materialgroup);
objMaterial.setSku(materialcode);
objMaterial.setName(materialname);

if(periodeId==0){
    Vector vt_mp = PstPeriode.list(0,0,""+PstPeriode.fieldNames[PstPeriode.FLD_STATUS] + " = " + PstPeriode.FLD_STATUS_RUNNING,"");
    if(vt_mp!=null && vt_mp.size()>0){
        Periode matPeriode = (Periode)vt_mp.get(0);
        periodeId = matPeriode.getOID();
    }
}

    //System.out.println("locationId : "+locationId);
   // System.out.println("periodeId : "+periodeId);

int vectSize = PstMaterialConStock.getCountDaftarBarangStock(locationId,periodeId,materialgroup,materialcode,materialname, PstMaterial.CATALOG_TYPE_CONSIGMENT);
CtrlMaterial ctrlMaterial = new CtrlMaterial(request);
if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST){
	start = ctrlMaterial.actionList(iCommand, start, vectSize, recordToGet);
}

Vector vect = PstMaterialConStock.getDaftarBarangStock(start,recordToGet,locationId,periodeId,materialgroup,materialcode,materialname, PstMaterial.CATALOG_TYPE_CONSIGMENT);
String whr = PstMatConDispatchItem.fieldNames[PstMatConDispatchItem.FLD_DISPATCH_MATERIAL_ID]+"="+oidDispatchMaterial;
Vector vectCos = PstMatConDispatchItem.list(0,0,whr,"");

%>
<!-- End of JSP Block -->
<html>
<head>
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
function cmdEdit(matOid,matCode,matItem,matUnit,matHpp,matUnitId,oidDfItem){
	if(oidDfItem==0){
		self.opener.frm_matdispatch.<%=FrmMatConDispatchItem.fieldNames[FrmMatConDispatchItem.FRM_FIELD_MATERIAL_ID]%>.value = matOid;
		self.opener.frm_matdispatch.matCode.value = matCode;
		self.opener.frm_matdispatch.matItem.value = matItem;
		self.opener.frm_matdispatch.matUnit.value = matUnit;
		self.opener.frm_matdispatch.<%=FrmMatConDispatchItem.fieldNames[FrmMatConDispatchItem.FRM_FIELD_UNIT_ID]%>.value = matUnitId;
        self.opener.frm_matdispatch.<%=FrmMatConDispatchItem.fieldNames[FrmMatConDispatchItem.FRM_FIELD_HPP]%>.value = matHpp;
		//self.opener.frm_matdispatch.hidden_receive_item_id.value = receiveId;
		self.opener.frm_matdispatch.<%=FrmMatConDispatchItem.fieldNames[FrmMatConDispatchItem.FRM_FIELD_QTY]%>.focus();
	}else{
		self.opener.frm_matdispatch.hidden_dispatch_item_id.value = oidDfItem;
		self.opener.frm_matdispatch.command.value = "<%=Command.EDIT%>";
		self.opener.frm_matdispatch.submit();
	}
	self.close();		
}

function cmdListFirst()
{
	document.frmvendorsearch.command.value="<%=Command.FIRST%>";
	document.frmvendorsearch.action="dfwhdosearch.jsp";
	document.frmvendorsearch.submit();
}

function cmdListPrev(){
	document.frmvendorsearch.command.value="<%=Command.PREV%>";
	document.frmvendorsearch.action="dfwhdosearch.jsp";
	document.frmvendorsearch.submit();
}

function cmdListNext(){
	document.frmvendorsearch.command.value="<%=Command.NEXT%>";
	document.frmvendorsearch.action="dfwhdosearch.jsp";
	document.frmvendorsearch.submit();
}

function cmdListLast(){
	document.frmvendorsearch.command.value="<%=Command.LAST%>";
	document.frmvendorsearch.action="dfwhdosearch.jsp";
	document.frmvendorsearch.submit();
}	

function cmdSearch(){
	document.frmvendorsearch.command.value="<%=Command.LIST%>";
	document.frmvendorsearch.action="dfwhdosearch.jsp";
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
          <td colspan="2"> 
            <hr size="1">
          </td>
        </tr>
        <tr> 
          <td> 
            <form name="frmvendorsearch" method="post" action="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="command" value="<%=iCommand%>">
			  <input type="hidden" name="hidden_dispatch_id" value="<%=oidDispatchMaterial%>">
              <input type="hidden" name="location_id" value="<%=locationId%>">
              <input type="hidden" name="periode_id" value="<%=periodeId%>">
              <table width="100%" border="0" cellspacing="1" cellpadding="1">
               <tr>
                  <td width="18%"><%=textMaterialHeader[SESS_LANGUAGE][0]%></td>
                  <td width="82%"> 
                  <%
				  Vector category = PstCategory.list(0,0,"",PstCategory.fieldNames[PstCategory.FLD_NAME]);
				  Vector vectGroupVal = new Vector(1,1);
				  Vector vectGroupKey = new Vector(1,1);
				  vectGroupVal.add("Select ...");
				  vectGroupKey.add("0");
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
                  <td width="18%"><%=textMaterialHeader[SESS_LANGUAGE][1]%></td>
                  <td width="82%"> 
                    <input type="text" name="mat_code" size="30" value="<%=materialcode%>" class="formElemen">
                  </td>
                </tr>
                <tr> 
                  <td width="18%"><%=textMaterialHeader[SESS_LANGUAGE][2]%></td>
                  <td width="82%"> 
                    <input type="text" name="txt_materialname" size="30" value="<%=materialname%>" class="formElemen">
                  </td>
                </tr>
                <tr>  
                  <td width="18%">&nbsp;</td>
                  <td width="82%"> 
                    <input type="button" name="Button" value="Search" onClick="javascript:cmdSearch()" class="formElemen">
                  </td>
                </tr>
                <tr> 
                  <td colspan="2"><%=drawListMaterial(SESS_LANGUAGE,vect,vectCos,start,tranUsedPriceHpp)%></td>
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

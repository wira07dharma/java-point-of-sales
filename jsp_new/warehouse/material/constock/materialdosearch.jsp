<%@ page import="com.dimata.gui.jsp.ControlList,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.posbo.form.masterdata.CtrlMaterial,
                 com.dimata.util.Command,
                 com.dimata.posbo.form.warehouse.FrmMatConStockOpnameItem,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.posbo.entity.masterdata.*"%>
<%@page contentType="text/html"%>

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = 1;//AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK, AppObjInfo.OBJ_STOCK_OPNAME); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<%!
/* this constant used to list text of listHeader */
public static final String textMaterialHeader[][] =
{
	{"Kategori","Sku","Nama Barang","Sub Kategori"},
	{"Category","Material Code","Material Name","Sub Kategory"}
};

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] =
{
	{"No","Grup","Sku","Nama Barang","Unit","Harga Beli"},
	{"No","Category","Code","Item","Unit","Cost"}
};

public String drawListMaterial(int language,Vector objectClass,int start)
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
		ctrlist.addHeader(textListMaterialHeader[language][2],"10%");
		ctrlist.addHeader(textListMaterialHeader[language][3],"50%");
		ctrlist.addHeader(textListMaterialHeader[language][4],"10%");
		//ctrlist.addHeader(textListMaterialHeader[language][5],"10%");

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
			Material material  = (Material)objectClass.get(i);
			
			String unitName = "";
			String unitCode = "";
            try{
                Unit unit = PstUnit.fetchExc(material.getDefaultSellUnitId());
				//Unit unit = PstUnit.fetchExc(material.getBuyUnitId());
                unitName = unit.getName();
				unitCode = unit.getCode();
            }catch(Exception e){}
			
            String name = "";
            name = material.getName();
            //if (name.compareToIgnoreCase("\"") >= 0) {
            name = name.replace('\"','`');
            name = name.replace('\'','`');
            //}
			
			start = start + 1;
			Vector rowx = new Vector();
			rowx.add(""+start);
			rowx.add("<div align=\"center\">"+material.getSku()+"</div>");
			rowx.add(material.getName());
			rowx.add("<div align=\"center\">"+unitCode+"</div>");
			
			lstData.add(rowx);
			lstLinkData.add(material.getOID()+"','"+material.getSku()+"','"+name+"','"+material.getDefaultSellUnitId()+"','"+unitName);
		}
		return ctrlist.draw();
	}
	else
	{
		result = "<div class=\"msginfo\">&nbsp;&nbsp;Tidak ada data barang...</div>";
	}
	return result;
}
%>

<!-- JSP Block -->
<%
String materialcode = FRMQueryString.requestString(request,"mat_code");
long materialgroup = FRMQueryString.requestLong(request,"txt_materialgroup");
long materialsubcategory = FRMQueryString.requestLong(request,"txt_material_sub_category");
long materialsupplier = FRMQueryString.requestLong(request,"txt_material_supplier");

/*
out.println("materialsupplier : "+materialsupplier);
out.println("materialgroup : "+materialgroup);
out.println("materialsubcategory : "+materialsubcategory);
*/

String materialname = FRMQueryString.requestString(request,"txt_materialname");
int start = FRMQueryString.requestInt(request, "start");
int iCommand = FRMQueryString.requestCommand(request);
int recordToGet = 15;
String pageTitle = "Konsinyasi > Opname > Pencarian Barang";

/**
* instantiate material object that handle searching parameter
*/
String orderBy = "MAT."+PstMaterial.fieldNames[PstMaterial.FLD_SKU];
Material objMaterial = new Material();
objMaterial.setCategoryId(materialgroup);
objMaterial.setSubCategoryId(materialsubcategory);
objMaterial.setSku(materialcode);
objMaterial.setName(materialname);

    //System.out.println("objMaterial.getCategoryId() "+objMaterial.getCategoryId()+"-"+materialgroup);
    //System.out.println("objMaterial.getSubCategoryId() "+objMaterial.getSubCategoryId());
    //System.out.println("objMaterial.getSku() "+objMaterial.getSku());
    //System.out.println("objMaterial.getName() "+objMaterial.getName());
    //System.out.println("materialsupplier "+materialsupplier);
/**
* get amount/count of material's list
*/
int vectSize = PstMaterial.getCountMaterial(materialsupplier,objMaterial);

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
Vector vect = PstMaterial.getListMaterial(materialsupplier,objMaterial,start,recordToGet, orderBy);
%>
<!-- End of JSP Block -->
<html>
<head>
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
function cmdEdit(matOid,matCode,matItem,unitId,unitName)
{
	self.opener.frm_matopname.<%=FrmMatConStockOpnameItem.fieldNames[FrmMatConStockOpnameItem.FRM_FIELD_MATERIAL_ID]%>.value = matOid;
	self.opener.frm_matopname.matCode.value = matCode;
	self.opener.frm_matopname.matItem.value = matItem;
	self.opener.frm_matopname.matUnit.value = unitName;
	self.opener.frm_matopname.<%=FrmMatConStockOpnameItem.fieldNames[FrmMatConStockOpnameItem.FRM_FIELD_UNIT_ID]%>.value = unitId;
	//self.opener.frm_matopname.<%=FrmMatConStockOpnameItem.fieldNames[FrmMatConStockOpnameItem.FRM_FIELD_COST]%>.value = matCost;
	//self.opener.frm_matopname.<%=FrmMatConStockOpnameItem.fieldNames[FrmMatConStockOpnameItem.FRM_FIELD_PRICE]%>.value = matPrice;
	//self.opener.frm_matopname.matCat.value = catName;
	//self.opener.frm_matopname.matSCat.value = scatName;
	self.opener.frm_matopname.<%=FrmMatConStockOpnameItem.fieldNames[FrmMatConStockOpnameItem.FRM_FIELD_QTY_OPNAME]%>.focus();
	self.close();
}

function cmdListFirst()
{
	document.frmmaterialsearch.command.value="<%=Command.FIRST%>";
	document.frmmaterialsearch.action="materialdosearch.jsp";
	document.frmmaterialsearch.submit();
}

function cmdListPrev(){
	document.frmmaterialsearch.command.value="<%=Command.PREV%>";
	document.frmmaterialsearch.action="materialdosearch.jsp";
	document.frmmaterialsearch.submit();
}

function cmdListNext(){
	document.frmmaterialsearch.command.value="<%=Command.NEXT%>";
	document.frmmaterialsearch.action="materialdosearch.jsp";
	document.frmmaterialsearch.submit();
}

function cmdListLast(){
	document.frmmaterialsearch.command.value="<%=Command.LAST%>";
	document.frmmaterialsearch.action="materialdosearch.jsp";
	document.frmmaterialsearch.submit();
}

function cmdSearch(){
        document.frmmaterialsearch.start.value=0;
	document.frmmaterialsearch.command.value="<%=Command.LIST%>";
	document.frmmaterialsearch.action="materialdosearch.jsp";
	document.frmmaterialsearch.submit();
}

function clear(){
	document.frmmaterialsearch.txt_materialcode.value="";
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
            <form name="frmmaterialsearch" method="post" action="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="txt_material_sub_category" value="<%=materialsubcategory%>">
              <input type="hidden" name="txt_material_supplier" value="<%=materialsupplier%>">
              <input type="hidden" name="txt_materialgroup" value="<%=materialgroup%>">
              <table width="100%" border="0" cellspacing="1" cellpadding="1">
                <!--tr>
                  <td width="17%"><%=textMaterialHeader[SESS_LANGUAGE][0]%></td>
                  <td width="83%"> :
                    <%/*
                        Vector materGroup = PstCategory.list(0,0,"",PstCategory.fieldNames[PstCategory.FLD_CODE]);
                        Vector vectGroupVal = new Vector(1,1);
                        Vector vectGroupKey = new Vector(1,1);
                        vectGroupVal.add("Semua Kategori");
                        vectGroupKey.add("0");
                        for(int i=0; i<materGroup.size(); i++)
                        {
                            Category mGroup = (Category)materGroup.get(i);
                            vectGroupVal.add(mGroup.getName());
                            vectGroupKey.add(""+mGroup.getOID());
                        }
                        out.println(ControlCombo.draw("txt_materialgroup","formElemen", null, ""+materialgroup, vectGroupKey, vectGroupVal, null));
					*/
				  /*Category mGroup = new Category();
				  try{
				  		mGroup = PstCategory.fetchExc(materialgroup);
				  }catch(Exception e){}
				  out.println(mGroup.getName());*/
				  %> </td>
                </tr-->
                <tr>
                  <td width="17%"><%=textMaterialHeader[SESS_LANGUAGE][1]%></td>
				  <td width="83%">:
                    <input type="text" name="mat_code" size="30" value="<%=materialcode%>" class="formElemen">
                  </td>
                </tr>
                <tr>
                  <td width="17%"><%=textMaterialHeader[SESS_LANGUAGE][2]%></td>
                  <td width="83%"> :
                    <input type="text" name="txt_materialname" size="30" value="<%=materialname%>" class="formElemen">
                  </td>
                </tr>
                <tr>
                  <td width="17%">&nbsp;</td>
                  <td width="83%">&nbsp;
                    <input type="button" name="Button" value=" Cari " onClick="javascript:cmdSearch()" class="formElemen">
                  </td>
                </tr>
                <tr>
                  <td colspan="2"><%=drawListMaterial(SESS_LANGUAGE,vect,start)%></td>
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

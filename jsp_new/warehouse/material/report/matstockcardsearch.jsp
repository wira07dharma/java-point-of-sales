<%@page import="com.dimata.posbo.session.masterdata.SessMaterial"%>
<%@ page import="com.dimata.posbo.form.masterdata.CtrlMaterial,
                 com.dimata.posbo.entity.masterdata.*,
                 com.dimata.posbo.form.search.FrmSrcStockCard,
                 com.dimata.gui.jsp.ControlList,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.util.Command,
                 com.dimata.gui.jsp.ControlCombo,
                 com.dimata.gui.jsp.ControlLine"%>
<%@page contentType="text/html"%>

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK_REPORT, AppObjInfo.OBJ_STOCK_CARD); %>
<%@ include file = "../../../main/checkuser.jsp" %>


<%!
/* this constant used to list text of listHeader */
public static final String textMaterialHeader[][] =
{
	{"Kategori","Sku","Nama Barang","Sub Kategori","Cari Barang","Barcode"},
	{"Category","Material Code","Material Name","Sub Kategory","Search Item","Barcode"}
};

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] =
{
	{"No","Grup","Sku","Nama Barang","Unit","Harga Beli","Barcode","Status Item"},
	{"No","Category","Code","Item","Unit","Cost","Barcode","Status Item"}
};

public String drawListMaterial(int language,Vector objectClass,int start, int typeOfBusinessDetail)
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
		ctrlist.addHeader(textListMaterialHeader[language][2],"10%");
                ctrlist.addHeader(textListMaterialHeader[language][6],"10%");
		ctrlist.addHeader(textListMaterialHeader[language][3],"30%");
                ctrlist.addHeader(textListMaterialHeader[language][7],"10%");

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
			start = start + 1;
			Vector rowx = new Vector();
			rowx.add(""+start);
                        
			rowx.add("<div align=\"center\">"+material.getSku()+"</div>");
                        rowx.add("<div align=\"center\">"+material.getBarCode()+"</div>");
			rowx.add((typeOfBusinessDetail == 2 ? SessMaterial.setItemNameForLitama(material.getOID()) : material.getName()));
                        
                        if(material.getEditMaterial()==4){
                            rowx.add("Non Aktive");
                        }else{
                            rowx.add("Aktive");
                        }

                        String name = "";
                        name = typeOfBusinessDetail == 2 ? SessMaterial.setItemNameForLitama(material.getOID()) : material.getName();
                        name = name.replace('\"','`');
                        name = name.replace('\'','`');

			lstData.add(rowx);
			lstLinkData.add(material.getOID()+"','"+material.getSku()+"','"+name);
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
String materialbarcode = FRMQueryString.requestString(request,"mat_barcode");
long materialgroup = FRMQueryString.requestLong(request,"txt_materialgroup");
long materialsubcategory = FRMQueryString.requestLong(request,"txt_material_sub_category");
long materialsupplier = FRMQueryString.requestLong(request,"txt_material_supplier");

String materialname = FRMQueryString.requestString(request,"txt_materialname");
int start = FRMQueryString.requestInt(request, "start");
int iCommand = FRMQueryString.requestCommand(request);
int recordToGet = 30;
String pageTitle = "Pencarian Barang";

/**
* instantiate material object that handle searching parameter
*/
String orderBy = "MAT."+PstMaterial.fieldNames[PstMaterial.FLD_SKU];
Material objMaterial = new Material();
objMaterial.setCategoryId(materialgroup);
objMaterial.setSubCategoryId(materialsubcategory);
objMaterial.setSku(materialcode);
objMaterial.setName(materialname);
objMaterial.setBarCode(materialbarcode);
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
function cmdEdit(matOid,matCode,matItem)
{
	self.opener.document.forms.frmsrcstockcard.<%=FrmSrcStockCard.fieldNames[FrmSrcStockCard.FRM_FIELD_MATERIAL_ID]%>.value = matOid;
	self.opener.document.forms.frmsrcstockcard.txtkode.value = matCode;
	self.opener.document.forms.frmsrcstockcard.txtnama.value = matItem;
	self.close();
}

function cmdListFirst()
{
	document.frmmaterialsearch.command.value="<%=Command.FIRST%>";
	document.frmmaterialsearch.action="matstockcardsearch.jsp";
	document.frmmaterialsearch.submit();
}

function cmdListPrev(){
	document.frmmaterialsearch.command.value="<%=Command.PREV%>";
	document.frmmaterialsearch.action="matstockcardsearch.jsp";
	document.frmmaterialsearch.submit();
}

function cmdListNext(){
	document.frmmaterialsearch.command.value="<%=Command.NEXT%>";
	document.frmmaterialsearch.action="matstockcardsearch.jsp";
	document.frmmaterialsearch.submit();
}

function cmdListLast(){
	document.frmmaterialsearch.command.value="<%=Command.LAST%>";
	document.frmmaterialsearch.action="matstockcardsearch.jsp";
	document.frmmaterialsearch.submit();
}

function cmdSearch(){
	document.frmmaterialsearch.command.value="<%=Command.LIST%>";
	document.frmmaterialsearch.action="matstockcardsearch.jsp";
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
</head>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<script language="JavaScript">
	window.focus();
</script>
<table width="100%" border="0" cellspacing="3" cellpadding="2" height="100%" bgcolor="#FCFDEC" >
  <tr><%@include file="../../../styletemplate/template_header_empty.jsp" %>
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
              <!--input type="hidden" name="txt_materialgroup" value="<%//=materialgroup%>"-->
              <table width="100%" border="0" cellspacing="1" cellpadding="1">
                <tr>
                  <td width="17%"><%=textMaterialHeader[SESS_LANGUAGE][0]%></td>
                  <td width="83%"> :
                    <select  name="txt_materialgroup" class="formElemen">
                   <option value="-1">Semua Category</option>
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
                                <option value="<%=mGroup.getOID()%>" <%=mGroup.getOID()==objMaterial.getCategoryId()?checked:""%> ><%=parent%><%=mGroup.getName()%></option>
                            <%
                        }
                    } else {
                        vectGroupVal.add("Tidak Ada Category");
                        vectGroupKey.add("-1");
                    }
                  %>
                  </select>
                    </td>
                </tr>
                <tr>
                  <td width="17%"><%=textMaterialHeader[SESS_LANGUAGE][1]%></td>
				  <td width="83%">:
                    <input type="text" name="mat_code" size="30" value="<%=materialcode%>" class="formElemen">
                  </td>
                </tr>
                <tr>
                  <td width="17%"><%=textMaterialHeader[SESS_LANGUAGE][5]%></td>
				  <td width="83%">:
                    <input type="text" name="mat_barcode" size="30" value="<%=materialbarcode%>" class="formElemen">
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
                    <input type="button" name="Button" value="<%=textMaterialHeader[SESS_LANGUAGE][4]%>" onClick="javascript:cmdSearch()" class="formElemen">
                  </td>
                </tr>
                <tr>
                  <td colspan="2"><%=drawListMaterial(SESS_LANGUAGE,vect,start,typeOfBusinessDetail)%></td>
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

<%@ page language = "java" %>
<%@ page import="
                 com.dimata.common.entity.contact.PstContactList,
                 com.dimata.common.entity.contact.ContactList,
                 com.dimata.gui.jsp.ControlList,
                 com.dimata.qdep.form.FRMHandler,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.qdep.form.FRMMessage,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.util.Command,
                 com.dimata.posbo.entity.admin.PstAppUser,
                 com.dimata.posbo.entity.masterdata.*,
                 com.dimata.posbo.session.masterdata.SessMaterial,
                 com.dimata.posbo.form.masterdata.CtrlMaterial,
                 com.dimata.posbo.entity.search.SrcMaterial,
                 com.dimata.posbo.form.search.FrmSrcMaterial,
                 com.dimata.gui.jsp.ControlCombo,
                 com.dimata.posbo.form.masterdata.FrmMaterial"%>

<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_CATALOG); %>
<%@ include file = "../../main/checkuser.jsp" %>



<%!
//public static final int SESS_LANGUAGE = com.dimata.util.lang.I_Language.LANGUAGE_FOREIGN;
public static final String textListTitleHeader[][] =
{
	{"Daftar Barang","Barang","Tidak ada data ..","Masuk ke Exception"},
	{"Goods List","Goods","No data available ..","Go Into Exception"}
};
%>
<%
 if(userGroupNewStatus == PstAppUser.GROUP_SUPERVISOR)
{
	privAdd=false;
	privUpdate=false;
	privDelete=false;
}
%>

<!-- Jsp Block -->
<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

/* this constant used to list text of listHeader */
/*public static final String textListMaterialHeader[][] =
{
	{"No","Sku","Nama","Kategori","Sub Kategori","Tipe Supplier","Supplier","Hrg Beli","Hrg Jual"},
	{"No","Code","Name","Category","Sub Kategori","Supplier Type","Supplier","Cost","Price"}
};*/

    public static final String textListMaterialHeader[][] =
    {
        {"No","Kode","Barcode","Nama Barang","Jenis","Tipe","Hrg Beli","Hrg Jual","Kategori","Merk","Jarak Code"},
        {"No","Code","Barcode","Name","Jenis","Type","Cost","Price","Category","Merk","Range Code"}
    };

public String drawList(int language,Vector objectClass,int start)
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
		ctrlist.addHeader(textListMaterialHeader[language][1],"7%");
        ctrlist.addHeader(textListMaterialHeader[language][3],"20%");

		ctrlist.setLinkRow(1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();
		if(start<0)
		{
			start = 0;
		}
		Vector rowx = new Vector();
		for(int i=0; i<objectClass.size(); i++){
			Vector temp = (Vector)objectClass.get(i);
			Material material = (Material)temp.get(0);
			Category category = (Category)temp.get(1);
			Merk merk = (Merk)temp.get(3);
			rowx = new Vector();
			rowx.add("<a href=\"javascript:cmdEdit('"+material.getOID()+"')\">"+material.getSku()+"</a>");
            String goodName = "";
            if(material.getMaterialSwitchType()==Material.WITH_USE_SWITCH_MERGE_AUTOMATIC){
                material.setProses(Material.IS_PROCESS_INSERT_UPDATE);
                goodName = material.getName();
                try{
                  goodName = goodName.substring(goodName.lastIndexOf(Material.getSeparate())+1,goodName.length());
                }catch(Exception e){}
            }else{
                goodName = material.getName();
            }
            rowx.add(category.getName()+"&nbsp&nbsp&nbsp"+merk.getName()+"&nbsp&nbsp&nbsp"+goodName);
			lstData.add(rowx);
		}
		result = ctrlist.draw();
	}
	else
	{
		result = "<div class=\"msginfo\">&nbsp;&nbsp;"+textListTitleHeader[language][2]+"</div>";
	}
	return result;
}

    public String drawCodeRangeList(int language,Vector objectClass){
        String result = "";
        if(objectClass!=null && objectClass.size()>0){
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("100%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("listgentitle");
            ctrlist.addHeader(textListMaterialHeader[language][10],"20%");
            //ctrlist.addHeader(textListMaterialHeader[language][1],"10%");

            ctrlist.setLinkRow(-1);
            ctrlist.setLinkSufix("");
            Vector lstData = ctrlist.getData();
            Vector lstLinkData = ctrlist.getLinkData();
            ctrlist.setLinkPrefix("javascript:cmdEditSearch('");
            ctrlist.setLinkSufix("')");
            ctrlist.reset();
            Vector rowx = new Vector();
            for(int i=0; i<objectClass.size(); i++){
                CodeRange codeRange = (CodeRange)objectClass.get(i);
                rowx = new Vector();
                rowx.add("<a href=\"javascript:cmdEditSearch('"+codeRange.getOID()+"')\">"+codeRange.getFromRangeCode()+"</a>");
                lstData.add(rowx);
            }
            result = ctrlist.draw();
        }
        return result;
    }
%>

<%
/**
* get approval status for create document
*/
int systemName = I_DocType.SYSTEM_MATERIAL;
boolean privManageData = true;
%>

<%
/**
* get title for purchasing(pr) document
*/
String dfTitle = textListTitleHeader[SESS_LANGUAGE][1];
String dfItemTitle = dfTitle + " Item";

/**
* get request data from current form
*/
long oidMaterial = FRMQueryString.requestLong(request, "hidden_material_id");

/**
* initialitation some variable
*/
int iErrCode = FRMMessage.ERR_NONE;
String msgStr = "";
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
String codeShip = FRMQueryString.requestString(request, "txt_ship");
String code = FRMQueryString.requestString(request, "txt_code");
String codeCounter = FRMQueryString.requestString(request, "txt_counter");
String name = FRMQueryString.requestString(request, "txt_name");
long oidRange = FRMQueryString.requestLong(request, "hidden_range_code_id");


long categoryOid = FRMQueryString.requestLong(request, "category_prd");
long merkOid = FRMQueryString.requestLong(request, "merk_prd");

int recordToGet = 15;
int vectSize = 0;
String whereClause = "";

/**
* instantiate some object used in this page
*/
ControlLine ctrLine = new ControlLine();
CtrlMaterial ctrlMatDispatch = new CtrlMaterial(request);
SrcMaterial srcMaterial = new SrcMaterial();
SessMaterial sessMaterial = new SessMaterial();
FrmSrcMaterial frmSrcMaterial = new FrmSrcMaterial(request, srcMaterial);

/**
* handle current search data session
*/
if(iCommand==Command.BACK || iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST){
	 try{
		//srcMaterial = (SrcMaterial)session.getValue(SessMaterial.SESS_SRC_MATERIAL);
		if (srcMaterial == null){
			srcMaterial = new SrcMaterial();
		}
	 }catch(Exception e){
		out.println(textListTitleHeader[SESS_LANGUAGE][3]);
		srcMaterial = new SrcMaterial();
	 }
}else{
	 //frmSrcMaterial.requestEntityObject(srcMaterial);
    try{
	 //session.removeValue(SessMaterial.SESS_SRC_MATERIAL);
    }catch(Exception e){}
}
    srcMaterial = new SrcMaterial();
    srcMaterial.setJenisCode(1);
    srcMaterial.setCategoryId(categoryOid);

    srcMaterial.setMatcode(code);
    srcMaterial.setCodeShip(codeShip);
    srcMaterial.setCodeCounter(codeCounter);

    srcMaterial.setMatname(name);
    srcMaterial.setDescription("");
    srcMaterial.setSupplierId(-1);
    srcMaterial.setSubCategoryId(-1);
    srcMaterial.setSortby(0);
    srcMaterial.setTypeItem(0);
    srcMaterial.setMerkId(merkOid);
    srcMaterial.setMerkId(merkOid);

    if(oidRange!=0)
        srcMaterial.setOidCodeRange(oidRange);
    else
        srcMaterial.setOidCodeRange(0);

    //session.putValue(SessMaterial.SESS_SRC_MATERIAL, srcMaterial);
    /**
* get vectSize, start and data to be display in this page
*/
vectSize = sessMaterial.getCountSearch(srcMaterial);
//vectSize = sessMaterial.getCountSearchWithMultiSupp(srcMaterial);
if(iCommand==Command.FIRST || iCommand==Command.NEXT ||
        iCommand==Command.PREV || iCommand==Command.LAST ||
        iCommand==Command.LIST){
	start = ctrlMatDispatch.actionList(iCommand,start,vectSize,recordToGet);
}
System.out.println("iCommand: "+iCommand+" start: "+start+" vectSize: "+vectSize+" recordToGet: "+recordToGet);
Vector records = sessMaterial.searchMaterial(srcMaterial,start,recordToGet);
System.out.println("record : "+records.size());
//Vector records = sessMaterial.searchMaterialWithMultiSupp(srcMaterial,start,recordToGet);

    String vendorName = "";
    if(srcMaterial.getSupplierId()>0){
        try{
            ContactList contact = new ContactList();
            contact = PstContactList.fetchExc(srcMaterial.getSupplierId());
            vendorName = ", Supplier : "+contact.getCompName();
        }catch(Exception e){}
    }
    Vector recordsCodeRange = PstCodeRange.list(0,0,"",PstCodeRange.fieldNames[PstCodeRange.FLD_FROM_RANGE_CODE]);
%>
<!-- End of Jsp Block -->

<html>
<head>
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
<!--
function cmdAdd(){
    parent.frames[0].cmdAdd();
    parent.frames[1].cmdedit(0);
}

function cmdEditSearch(oid){
    //document.frm_material.hidden_type.value = "1";
	document.frm_material.hidden_range_code_id.value=oid;
    //alert(oid);
	//document.frm_material.approval_command.value="<%=Command.FIRST%>";
	document.frm_material.command.value="<%=Command.LIST%>";
	document.frm_material.action="simple_material_list.jsp";
	document.frm_material.submit();
}

function cmdEdit(oid)
{
    parent.frames[0].cmdEdit(oid);
    parent.frames[1].cmdedit(oid);
    //parent.frames[1].cmdedit(oid);
}

function cmdListFirst()
{
	document.frm_material.command.value="<%=Command.FIRST%>";
	document.frm_material.action="simple_material_list.jsp";
	document.frm_material.submit();
}

function cmdListPrev()
{
	document.frm_material.command.value="<%=Command.PREV%>";
	document.frm_material.action="simple_material_list.jsp";
	document.frm_material.submit();
}

function cmdListNext()
{
	document.frm_material.command.value="<%=Command.NEXT%>";
	document.frm_material.action="simple_material_list.jsp";
	document.frm_material.submit();
}

function cmdListLast()
{
	document.frm_material.command.value="<%=Command.LAST%>";
	document.frm_material.action="simple_material_list.jsp";
	document.frm_material.submit();
}

function cmdSearch(){
    document.frm_material.start.value=0;
    document.frm_material.hidden_range_code_id.value=0;
	document.frm_material.command.value="<%=Command.LIST%>";
	document.frm_material.action="simple_material_list.jsp";
	document.frm_material.submit();
}

	function cmdHome(){
        parent.cmdhomepage();
		//var act = "<%=approot%>" + "/homepage_admin.jsp";
		//document.frm_material.command.value="<%=Command.NONE%>";
		//document.frm_material.target="_main";
		//document.frm_material.action=act;
		//document.frm_material.submit();
	}

//------------------------- START JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------

//------------------------- END JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}
//-->
</script>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="../../styles/main.css" type="text/css">
<link rel="stylesheet" href="../../styles/tab.css" type="text/css">

</head>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('<%=approot%>/images/BtnSearchOn.jpg','<%=approot%>/images/BtnNewOn.jpg')">
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
  <tr>
    <td width="88%" valign="top" align="left">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td>            <form name="frm_material" method="post" action="">
              <input type="hidden" name="command" value="">
              <input type="hidden" name="add_type" value="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="hidden_material_id" value="<%=oidMaterial%>">
              <input type="hidden" name="hidden_range_code_id" value="<%=oidRange%>">
              <input type="hidden" name="approval_command">
			  <table width="100%" cellspacing="0" cellpadding="3">
			    <!--DWLayoutTable-->
			  <tr align="left" valign="top">
			    <td height="22" valign="middle"><strong>Product  List | <a href="javascript:cmdHome()">Back to homepage</a></strong></td>
			    </tr>
			  <tr align="left" valign="top">
			    <td width="975" height="24"><table width="100%"  border="0" cellspacing="1" cellpadding="1">
                  <tr>
                    <td width="9%"><%=textListMaterialHeader[SESS_LANGUAGE][1]%></td>
                    <td width="91%"><span class="formElemen">:
                        <input name="txt_ship" size="5" type="text" value="<%=codeShip%>" class="formElemen">
                        <input name="txt_code" size="5" type="text" value="<%=code%>" class="formElemen">
                        <input name="txt_counter" size="5" type="text" value="<%=codeCounter%>" class="formElemen">
                    </span></td>
                  </tr>
                  <tr>
                    <td><%=textListMaterialHeader[SESS_LANGUAGE][3]%></td>
                    <td><span class="formElemen">:
                        <input name="txt_name" value="<%=name%>" type="text" class="formElemen">
                    </span></td>
                  </tr>
                  <tr>
                    <td><%=textListMaterialHeader[SESS_LANGUAGE][8]%></td>
                    <td><span class="formElemen">:
                        <%
                        Vector materGroup = PstCategory.list(0,0,PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID]+"!=0",PstCategory.fieldNames[PstCategory.FLD_CODE]);
                        Vector vectGroupVal = new Vector(1,1);
                        Vector vectGroupKey = new Vector(1,1);
                        vectGroupVal.add(" ");
                        vectGroupKey.add("-1");
                        if(materGroup!=null && materGroup.size()>0){
                            for(int i=0; i<materGroup.size(); i++){
                                Category mGroup = (Category)materGroup.get(i);
                                vectGroupVal.add(mGroup.getName());
                                vectGroupKey.add(""+mGroup.getOID());
                            }
                        }
                        out.println(ControlCombo.draw("category_prd","formElemen", null, ""+srcMaterial.getCategoryId(), vectGroupKey, vectGroupVal, null));
                      %>
                    </span></td>
                  </tr>
                  <tr>
                    <td><%=textListMaterialHeader[SESS_LANGUAGE][9]%></td>
                    <td><span class="formElemen">:
                        <%
                      Vector materMerk = PstMerk.list(0,0,PstMerk.fieldNames[PstMerk.FLD_MERK_ID]+"!=0",PstMerk.fieldNames[PstMerk.FLD_NAME]);
                      Vector vectMerkVal = new Vector(1,1);
                      Vector vectMerkKey = new Vector(1,1);
                      vectMerkVal.add(" ");
                      vectMerkKey.add("-1");
                      if(materMerk!=null && materMerk.size()>0){
                          for(int i=0; i<materMerk.size(); i++){
                              Merk merk = (Merk)materMerk.get(i);
                              vectMerkVal.add(merk.getName());
                              vectMerkKey.add(""+merk.getOID());
                          }
                      }
                      out.println(ControlCombo.draw("merk_prd","formElemen", null, ""+srcMaterial.getMerkId(), vectMerkKey, vectMerkVal, null));
                  %>
                    </span></td>
                  </tr>
                  <tr>
                    <td>&nbsp;</td>
                    <td><%=drawCodeRangeList(SESS_LANGUAGE,recordsCodeRange)%></td>
                  </tr>
                </table></td>
			    </tr>
			  <tr align="left" valign="top">
			    <td height="30" valign="middle"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td nowrap width="2%"><a href="javascript:cmdSearch()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnSearchOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24" alt="Search Product"></a></td>
                    <td class="command" nowrap width="43%">&nbsp;<a href="javascript:cmdSearch()">Search</a></td>
                    <% if(privAdd){%>
                    <td nowrap width="2%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="Add New Product"></a></td>
                    <td class="command" nowrap width="53%">&nbsp;<a href="javascript:cmdAdd()">Add New</a></td>
                    <%}%>
                  </tr>
                </table></td>
			    </tr>
			  <tr align="left" valign="top">
				<td height="24" valign="middle"><%=drawList(SESS_LANGUAGE,records,start)%></td>
			  </tr>
			  <tr align="left" valign="top">
				<td height="24" align="left" class="command">
				  <span class="command">
				  <%
					ctrLine.setLocationImg(approot+"/images");
					ctrLine.initDefault();
					out.println(ctrLine.drawImageListLimit(iCommand,vectSize,start,recordToGet));
				  %>
				  </span>
				</td>
			  </tr>
			  <tr align="left" valign="top">
				<td height="24" valign="top">&nbsp;				  </td>
			    </tr>
			  <tr align="left" valign="top">
				<td height="20" valign="top"><!--DWLayoutEmptyCell-->&nbsp;</td>
			  </tr>
			</table>
            </form></td>
        </tr>
      </table>
    </td>
  </tr>
</table>
</body>

</html>

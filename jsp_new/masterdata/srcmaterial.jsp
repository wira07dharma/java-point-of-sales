 
<%@page contentType="text/html"%>
<!-- package java -->
<%@ page import = "java.util.*,
                   com.dimata.posbo.form.search.FrmSrcMaterial,
                   com.dimata.posbo.entity.search.SrcMaterial,
                   com.dimata.posbo.session.masterdata.SessMaterial" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %> 
<%@ page import = "com.dimata.qdep.form.*" %>
<%@ page import = "com.dimata.posbo.form.masterdata.*" %>
<%@ page import="com.dimata.posbo.entity.masterdata.*"%>

<%@ include file = "../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_MEMBER); %>
<%@ include file = "../main/checkuser.jsp" %>
<%!
/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] =
    {
        {"No","Sku","Barcode","Nama","Kategori","Sub Kategori","Hrg Beli","Hrg Jual","Pot. %"},
        {"No","Code","Barcode","Name","Category","Sub Kategori","Cost","Price","Disc. %"}
    };

public String drawList(int language,Vector objectClass,int start, long oidMemberReg)
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
		ctrlist.addHeader(textListMaterialHeader[language][0],"3%");
		ctrlist.addHeader(textListMaterialHeader[language][1],"10%");
		ctrlist.addHeader(textListMaterialHeader[language][3],"55%");
                ctrlist.addHeader(textListMaterialHeader[language][8],"10%");
	
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
		for(int i=0; i<objectClass.size(); i++) 
		{
			Vector temp = (Vector)objectClass.get(i);
			Material material = (Material)temp.get(0);
			
			rowx = new Vector(); 	 		 
			
			start = start + 1;
			
			rowx.add(""+start+"");
			rowx.add("<a href=\"javascript:cmdEdit('"+material.getOID()+"','"+material.getName()+"')\">"+material.getSku()+"</a>");
                        rowx.add(material.getName());

                        long checkOID = PstPersonalDiscount.checkPersonalDiscount(material.getOID(),oidMemberReg);
                        PersonalDiscount personalDiscount = new PersonalDiscount();
                        try{
                            personalDiscount = PstPersonalDiscount.fetchExc(checkOID);
                        }catch(Exception e){}
                        rowx.add("<input type=\"text\" name=\"txt_persen_"+material.getOID()+"\" value=\""+FRMHandler.userFormatStringDecimal(personalDiscount.getPersDiscPct())+"\" size=\"10%\">");
			lstData.add(rowx);
		}
		result = ctrlist.draw();
	}
	else
	{
		result = "<div class=\"msginfo\">&nbsp;&nbsp;Ttttt"/*+textListTitleHeader[language][2]*/+"</div>";		
	}
	return result;
}
%>
<!-- JSP Block -->
<%
int start = FRMQueryString.requestInt(request, "start");
int iCommand = FRMQueryString.requestCommand(request);
long oidMaterial = FRMQueryString.requestLong(request, "hidden_material_id");
long oidMemberReg = FRMQueryString.requestLong(request, "hidden_contact_id");
int prevCommand = FRMQueryString.requestInt(request, "command_prev");
String matName = FRMQueryString.requestString(request, ""+FrmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_MATNAME]+"");
int recordToGet = 10;
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
if(iCommand==Command.SAVE || iCommand==Command.BACK || iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST){
	try {
		srcMaterial = (SrcMaterial)session.getValue("CARI_MATERIAL");
		if (srcMaterial == null){
			srcMaterial = new SrcMaterial();
		} 
	}
	catch(Exception e) {
		srcMaterial = new SrcMaterial();
	}
} else {
	frmSrcMaterial.requestEntityObject(srcMaterial);
	srcMaterial.setSupplierId(-1);
	srcMaterial.setSubCategoryId(-1);
	if(matName!=null&&matName.length()>0){
		srcMaterial.setMatname(matName);
	}
	session.putValue("CARI_MATERIAL", srcMaterial);
}

if(srcMaterial.getSupplierId() == 0) srcMaterial.setSupplierId(-1);
if(srcMaterial.getCategoryId() == 0) srcMaterial.setCategoryId(-1);
if(srcMaterial.getMerkId() == 0) srcMaterial.setMerkId(-1);

/**
* get vectSize, start and data to be display in this page
*/
int vectSize = sessMaterial.getCountSearch(srcMaterial);
//vectSize = sessMaterial.getCountSearchWithMultiSupp(srcMaterial);
if(iCommand==Command.FIRST || iCommand==Command.NEXT || iCommand==Command.PREV || iCommand==Command.LAST || iCommand==Command.LIST) {
	start = ctrlMatDispatch.actionList(iCommand,start,vectSize,recordToGet);
}	
Vector records = sessMaterial.searchMaterial(srcMaterial,start,recordToGet);

// proses penyimpanan data potongan persentase barang
    if(iCommand==Command.SAVE){
        if(records.size()>0){
            for(int k=0;k<records.size();k++){
                Vector temp = (Vector)records.get(k);
                Material material = (Material)temp.get(0);
                double discpersen = FRMQueryString.requestDouble(request, "txt_persen_"+material.getOID());
                //System.out.println("nilai persentase adata : "+discpersen);
                if(discpersen > 0){
                    PersonalDiscount personalDiscount = new PersonalDiscount();
                    personalDiscount.setMaterialId(material.getOID());
                    personalDiscount.setPersDiscVal(0);
                    personalDiscount.setPersDiscPct(discpersen);
                    personalDiscount.setContactId(oidMemberReg);
                    try{
                        long checkOID = PstPersonalDiscount.checkPersonalDiscount(personalDiscount.getMaterialId(),oidMemberReg);
                        if(checkOID!=0){
                            personalDiscount.setOID(checkOID);
                            PstPersonalDiscount.updateExc(personalDiscount);
                            //System.out.println("**************** >>> update personal discount success");
                        }else{
                            PstPersonalDiscount.insertExc(personalDiscount);
                            //System.out.println("**************** >>> insert personal discount success");
                        }
                    }catch(Exception e){}
                }
            }
        }
    }

    //vectSize = records.size();
//Vector records = sessMaterial.searchMaterialWithMultiSupp(srcMaterial,start,recordToGet);

   /* String vendorName = "";
    if(srcMaterial.getSupplierId()>0){
        try{
            ContactList contact = new ContactList();
            contact = PstContactList.fetchExc(srcMaterial.getSupplierId());
            vendorName = ", Supplier : "+contact.getCompName();
        }catch(Exception e){}
    }
	*/
/*
String contactTitle = textListTitleheader[SESS_LANGUAGE][0];
contactTitle = SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar "+contactTitle : contactTitle+" List";
String whereClause = PstSubCategory.fieldNames[PstSubCategory.FLD_CATEGORY_ID] + " = " + oidCategory;

int vectSize = PstSubCategory.getCount(whereClause);
CtrlSubCategory ctrlSCat = new CtrlSubCategory(request);

if((iCommand == Command.FIRST || iCommand == Command.PREV )||(iCommand == Command.NEXT || iCommand == Command.LAST)){
	start = ctrlSCat.actionList(iCommand, start, vectSize, recordToGet);
} 

subCategoryName = "";
Vector vect = PstSubCategory.list(start,recordToGet,whereClause,PstSubCategory.fieldNames[PstSubCategory.FLD_NAME]); 
*/
%>
<!-- End of JSP Block -->
<html><!-- #BeginTemplate "/Templates/maindosearch.dwt" -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
function cmdEdit(oid,name){
	self.opener.document.forms.frmmemberregistrationhistory.<%=FrmPersonalDiscount.fieldNames[FrmPersonalDiscount.FRM_MATERIAL_ID] %>.value = oid;
	self.opener.document.forms.frmmemberregistrationhistory.txt_nama.value = name;
	self.opener.document.forms.frmmemberregistrationhistory.<%=FrmPersonalDiscount.fieldNames[FrmPersonalDiscount.FRM_PERSONAL_DISC_PCT] %>.focus();
	self.close();
}

function cmdClose(){
	self.opener.document.forms.frmmemberregistrationhistory.command.value = "<%=Command.BACK%>";
    self.opener.document.forms.frmmemberregistrationhistory.hidden_contact_id.value = "<%=oidMemberReg%>";
    self.opener.document.forms.frmmemberregistrationhistory.submit();
    self.close();
}

function cmdSearch()
{	
	document.frmscatsearch.command.value="<%=Command.LIST%>";
	document.frmscatsearch.action="srcmaterial.jsp";
	document.frmscatsearch.submit();
}

function cmdSave()
{
	document.frmscatsearch.command.value="<%=Command.SAVE%>";
    document.frmscatsearch.command_prev.value="<%=iCommand%>";
    document.frmscatsearch.action="srcmaterial.jsp";
	document.frmscatsearch.submit();
}

function cmdListFirst(){
	document.frmscatsearch.command.value="<%=Command.FIRST%>";
	document.frmscatsearch.action="srcmaterial.jsp";
	document.frmscatsearch.submit();
}

function cmdListPrev(){
	document.frmscatsearch.command.value="<%=Command.PREV%>";
	document.frmscatsearch.action="srcmaterial.jsp";
	document.frmscatsearch.submit();
}

function cmdListNext(){
	document.frmscatsearch.command.value="<%=Command.NEXT%>";
	document.frmscatsearch.action="srcmaterial.jsp";
	document.frmscatsearch.submit();
}

function cmdListLast(){
	document.frmscatsearch.command.value="<%=Command.LAST%>";
	document.frmscatsearch.action="srcmaterial.jsp";
	document.frmscatsearch.submit();
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
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<!-- #BeginEditable "styles" --> 
<link rel="stylesheet" href="../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" --> 
<link rel="stylesheet" href="../styles/tab.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "headerscript" --> 
<!-- #EndEditable --> 
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<!-- #BeginEditable "editfocus" -->
<script language="JavaScript">
	window.focus();
</script>
<!-- #EndEditable -->    
<table width="100%" border="0" cellspacing="3" cellpadding="2" height="100%" bgcolor="#FCFDEC" >
  <tr> 
    <td width="88%" valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0"> 
        <tr> 
          <td height="20"><font color="#FF8080" face="Century Gothic"><big><strong><!-- #BeginEditable "contenttitle" --><!-- #EndEditable --></strong></big></font></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
			  <form name="frmscatsearch" method="post" action="">
			  <input type="hidden" name="start" value="<%=start%>">
			  <input type="hidden" name="command" value="<%=iCommand%>">
			  <input type="hidden" name="hidden_material_id" value="<%=oidMaterial%>">
			  <input type="hidden" name="<%=FrmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_SUPPLIERID]%>"  value="-1">
			  <input type="hidden" name="<%=FrmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_SUBCATEGORYID]%>"  value="-1">
			  <input type="hidden" name="<%=FrmSrcMaterial.fieldNames[frmSrcMaterial.FRM_FIELD_SORTBY]%>"  value="0">
			  <input type="hidden" name="hidden_contact_id"  value="<%=oidMemberReg%>">
              <input type="hidden" name="command_prev"  value="<%=iCommand%>">
              <table width="100%" border="0" cellspacing="1" cellpadding="1">
                <tr>
                  <td colspan="2">
				  	<table width="100%" border="0" cellspacing="1" cellpadding="1">
                      <tr align="left">
                        <td height="21" width="9%"> Kode
                          <%//=textListTitleHeader[SESS_LANGUAGE][1]%></td>
                        <td height="21" width="1%" valign="top">:
                        <td height="21" width="90%" valign="top"> <input tabindex="1" type="text" name="<%=frmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_MATCODE] %>"  value="<%= srcMaterial.getMatcode() %>" class="formElemen" size="20">
                      <tr align="left">
                        <td height="21" width="9%"> Nama
                          <%//=textListTitleHeader[SESS_LANGUAGE][2]%></td>
                        <td height="21" width="1%" valign="top">:
                        <td height="21" width="90%" valign="top"> <input tabindex="2" type="text" name="<%=frmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_MATNAME] %>"  value="<%= srcMaterial.getMatname() %>" class="formElemen" size="40">
                          <!--tr align="left">
                  <td height="21" width="9%"> <%//=textListTitleHeader[SESS_LANGUAGE][3]%></td>
                  <td height="21" width="1%" valign="top">:
                  <td height="21" width="90%" valign="top">
                    <%
                    /*
						Vector obj_supplier = new Vector(1,1);
						Vector val_supplier = new Vector(1,1);
						Vector key_supplier = new Vector(1,1);
						val_supplier.add("-1");
						key_supplier.add("All Supplier");*/
						//val_supplier.add("0");
						//key_supplier.add("No Supplier");
						//Vector vt_supp = PstContactList.list(0,0,"",PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE]);

						/*Vector vt_supp = PstContactList.list(0,0,"",PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);
						if(vt_supp!=null && vt_supp.size()>0){
							int maxSupp = vt_supp.size();
							for(int d=0; d<maxSupp; d++){
								ContactList cnt = (ContactList)vt_supp.get(d);
								String cntName = cnt.getCompName();
								val_supplier.add(String.valueOf(cnt.getOID()));
								key_supplier.add(cntName);
							}
						}
						String select_supplier = ""+srcMaterial.getSupplierId(); */
					  %>
                    <%//=ControlCombo.draw(frmSrcMaterial.fieldNames[frmSrcMaterial.FRM_FIELD_SUPPLIERID],"formElemen",null,select_supplier,val_supplier,key_supplier," tabindex=\"3\" onkeydown=\"javascript:fnTrapKD()\"")%>
                    -->
                 <tr align="left">
                  <td height="21" width="9%"> Kategori</td>
                  <td height="21" width="1%" valign="top">:
                  <td height="21" width="90%" valign="top">
                    <%
					Vector materGroup = PstCategory.list(0,0,"",PstCategory.fieldNames[PstCategory.FLD_NAME]);
					Vector vectGroupVal = new Vector(1,1);
					Vector vectGroupKey = new Vector(1,1);
					vectGroupVal.add(" ");
					vectGroupKey.add("-1");
					//vectGroupVal.add("No Category");
					//vectGroupKey.add("0");
					if(materGroup!=null && materGroup.size()>0){
						int maxGrp = materGroup.size();
						for(int i=0; i<maxGrp; i++){
							Category mGroup = (Category)materGroup.get(i);
							vectGroupVal.add(mGroup.getName());
							vectGroupKey.add(""+mGroup.getOID());
						}
					}else{
						vectGroupVal.add(" ");
						vectGroupKey.add("-1");
					}
					out.println(ControlCombo.draw(frmSrcMaterial.fieldNames[frmSrcMaterial.FRM_FIELD_CATEGORYID],"formElemen", null, ""+srcMaterial.getCategoryId(), vectGroupKey, vectGroupVal, " tabindex=\"4\" onkeydown=\"javascript:fnTrapKD()\""));
				%>
                                <%--  
                    <tr align="left">
                        <td height="21" valign="top">Sub Kategori</td>
                        <td height="21" valign="top">:</td>
                        <td height="21" valign="top">                    
                            <%
                                    String orderSubBy = PstSubCategory.fieldNames[PstSubCategory.FLD_NAME];
                                    Vector listMaterialSubcategory = PstSubCategory.list(0,0,"",orderSubBy);
                                    Vector vectSubCatVal = new Vector(1,1);
                                    Vector vectSubCatKey = new Vector(1,1);
                                    if(listMaterialSubcategory!=null && listMaterialSubcategory.size()>0) {
                                        for(int i=0; i<listMaterialSubcategory.size(); i++) {
                                            SubCategory SubCategory = (SubCategory)listMaterialSubcategory.get(i);
                                            vectSubCatVal.add(SubCategory.getName());
                                            vectSubCatKey.add(""+SubCategory.getOID());
                                        }
                                    }
                                    out.println(ControlCombo.draw(FrmMaterial.fieldNames[FrmMaterial.FRM_FIELD_SUB_CATEGORY_ID],"formElemen", null, ""+srcMaterial.getSubCategoryId(), vectSubCatKey, vectSubCatVal, null));
                                    %> 
</td>
                      </tr>          
                      --%>
                      <tr align="left">
                        <td height="21" valign="top">Merk</td>
                        <td height="21" valign="top">:</td>
                        <td height="21" valign="top">                    <%
					//Vector materGroup = PstCategory.list(0,0,"",PstCategory.fieldNames[PstCategory.FLD_CODE]);
					materGroup = PstMerk.list(0,0,PstMerk.fieldNames[PstMerk.FLD_MERK_ID]+"!=0",PstMerk.fieldNames[PstMerk.FLD_NAME]);
					vectGroupVal = new Vector(1,1);
					vectGroupKey = new Vector(1,1);
					vectGroupVal.add(" ");
					vectGroupKey.add("-1");
					if(materGroup!=null && materGroup.size()>0)
					{
						int maxGrp = materGroup.size();
						for(int i=0; i<maxGrp; i++){
							Merk mGroup = (Merk)materGroup.get(i);
							vectGroupVal.add(mGroup.getName());
							vectGroupKey.add(""+mGroup.getOID());
						}
					}
					else
					{
						vectGroupVal.add("Tidak Ada Merk");
						vectGroupKey.add("0");
					}
					out.println(ControlCombo.draw(frmSrcMaterial.fieldNames[FrmSrcMaterial.FRM_FIELD_MERK_ID],"formElemen", null, ""+srcMaterial.getMerkId(), vectGroupKey, vectGroupVal, " tabindex=\"5\" onkeydown=\"javascript:fnTrapKD()\""));
				%>
</td>
                      </tr>
                      <tr align="left">
                        <td height="21" valign="top" width="9%">&nbsp;</td>
                        <td height="21" width="1%" valign="top">&nbsp;</td>
                        <td height="21" width="90%" valign="top"> <table width="34%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                              <td nowrap width="9%"><a href="javascript:cmdSearch()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnSearchOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24" alt="Cari Barang"></a></td>
                              <td class="command" nowrap width="91%"><a href="javascript:cmdSearch()">Search
                                <%//=textListTitleHeader[SESS_LANGUAGE][7]%>
                                </a></td>
                            </tr>
                          </table></td>
                      </tr>
                    </table>
				  </td>
                </tr>
				<%if(records!=null&&records.size()>0){%>
                <tr> 
                  <td colspan="2"><%=drawList(SESS_LANGUAGE,records,start,oidMemberReg)%><%//=drawList(SESS_LANGUAGE,vect,start)%></td>
                </tr>
				<%}%>
                <tr> 
                  <td> <span class="command"> 
                    <% 
				  ControlLine ctrlLine= new ControlLine();	
                  ctrlLine.setLocationImg(approot+"/images");
				  ctrlLine.initDefault();
                  out.println(ctrlLine.drawImageListLimit(iCommand,vectSize,start,recordToGet));				  
				  %>
                    </span> </td>
                  <td align="right"><a href="javascript:cmdClose()">Close and Reload</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;<a href="javascript:cmdSave()">Save Discount</a></td>
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
      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>

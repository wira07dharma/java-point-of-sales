<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@ page import="com.dimata.qdep.form.FRMQueryString,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.posbo.entity.search.SrcMinimumStock,
                 com.dimata.posbo.form.search.FrmSrcMinimumStock,
                 com.dimata.gui.jsp.ControlCombo,
                 com.dimata.posbo.session.warehouse.SessMinimumStock,
                 com.dimata.util.Command,
                 com.dimata.common.entity.location.PstLocation,
                 com.dimata.common.entity.location.Location,
                 com.dimata.posbo.entity.masterdata.PstCategory,
                 com.dimata.posbo.entity.masterdata.Category"%>
<%@ page import="com.dimata.gui.jsp.ControlCheckBox"%>
<%@ page import="com.dimata.posbo.entity.masterdata.PstMerk"%>
<%@ page import="com.dimata.posbo.entity.masterdata.Merk"%>
<%@ page import="com.dimata.common.entity.contact.PstContactList"%>
<%@ page import="com.dimata.common.entity.contact.ContactList"%>
<%@ page import="com.dimata.posbo.session.masterdata.SessMemberReg"%>
<%@ page import="com.dimata.posbo.entity.search.SrcMemberReg"%>
<%@ page language = "java" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_PURCHASING, AppObjInfo.G2_PURCHASING, AppObjInfo.OBJ_MINIMUM_STOCK); %>
<%@ include file = "../../../main/checkuser.jsp" %>


<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

/* this constant used to list text of listHeader */
public static final String textListHeader[][] =
{
	{"Lokasi","Supplier","Kategori","Periode","Sku","Sub Kategori","Barang","Merk"},
	{"Location","Supplier","Category","Periode","Code","Sub Category","Catalog","Merk"}
};

public String getJspTitle(int index, int language, String prefiks, boolean addBody){
	String result = "";
	if(addBody){
		if(language==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT){
			result = textListHeader[language][index] + " " + prefiks;
		}else{
			result = prefiks + " " + textListHeader[language][index];
		}
	}else{
		result = textListHeader[language][index];
	}
	return result;
}

%>


<!-- Jsp Block -->
<%
/**
* get data from 'hidden form'
*/
int iCommand = FRMQueryString.requestCommand(request);
int typeRequest =Integer.parseInt((request.getParameter("typeRequest")==null) ? "0" : request.getParameter("typeRequest"));

ControlLine ctrLine = new ControlLine();
String txtKode = "";
String txtNama = "";

SrcMinimumStock srcMinimumStock = new SrcMinimumStock();
FrmSrcMinimumStock frmSrcMinimumStock = new FrmSrcMinimumStock();
try{
	srcMinimumStock = (SrcMinimumStock)session.getValue(SessMinimumStock.SESSION_MINIMUM_NAME);
}catch(Exception e){
	srcMinimumStock = new SrcMinimumStock();
}

if(srcMinimumStock==null){
	srcMinimumStock = new SrcMinimumStock();
}

try{
	session.removeValue(SessMinimumStock.SESSION_MINIMUM_NAME);
}catch(Exception e){}

merkName = PstSystemProperty.getValueByName("NAME_OF_MERK");

%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
function cmdSearch(){
	document.frmsrcstockcard.command.value="<%=Command.LIST%>";
        document.frmsrcstockcard.typeRequest.value="<%=typeRequest%>";
	document.frmsrcstockcard.action="reportstockmin_list.jsp";
	document.frmsrcstockcard.submit();
}

function cmdSelectSubCategory()
{
	window.open("subcategorydosearch.jsp?command=<%=Command.FIRST%>&txt_subcategory="+document.frmsrcreportstock.txt_subcategory.value+
			"&oidCategory="+document.frmsrcreportstock.<%=FrmSrcMinimumStock.fieldNames[FrmSrcMinimumStock.FRM_FIELD_CATEGORY_ID]%>.value+
			"&caller=1",
			"material", "height=600,width=600,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes");
}

function cekbrg(){
    window.open("matminstocksearch.jsp","srhmatcard", "height=500,width=700,status=yes,toolbar=no,menubar=yes,location=no,scrollbars=yes");
}

</script>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<!-- #BeginEditable "styles" -->
<%if(menuUsed == MENU_ICON){%>
    <link href="../../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
<%}%>
<link rel="stylesheet" href="../../../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" -->
<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "headerscript" -->
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
<!-- #EndEditable --> 
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
  <%if(menuUsed == MENU_PER_TRANS){%>
  <tr>
    <td height="25" ID="TOPTITLE"> <!-- #BeginEditable "header" -->
      <%@ include file = "../../../main/header.jsp" %>
      <!-- #EndEditable --></td>
  </tr>
  <tr>
    <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
      <%@ include file = "../../../main/mnmain.jsp" %>
      <!-- #EndEditable --> </td>
  </tr>
  <%}else{%>
   <tr bgcolor="#FFFFFF">
    <td height="10" ID="MAINMENU">
      <%@include file="../../../styletemplate/template_header.jsp" %>
    </td>
  </tr>
  <%}%>
  <tr> 
    <td valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">  
        <tr> 
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->
            Laporan Minimum Stok <!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" -->
            <form name="frmsrcstockcard" method="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="<%=FrmSrcMinimumStock.fieldNames[FrmSrcMinimumStock.FRM_FIELD_MATERIAL_ID]%>" value="0">
              <input type="hidden" name="typeRequest" value="<%=typeRequest%>">
              <table width="100%" border="0">
                <tr>
                  <td colspan="2">
                    <hr size="1">
                  </td>
                </tr>
                <tr>
                  <td colspan="2">
                    <table width="100%" border="0" cellspacing="1" cellpadding="1">
                      <tr>
                        <td height="21" width="9%"><%=getJspTitle(0,SESS_LANGUAGE,"",true)%></td>
                        <td height="21" width="1%">:</td>
                        <td height="21" width="90%">&nbsp;
                          <%
                                Vector val_locationid = new Vector(1,1);
                                Vector key_locationid = new Vector(1,1);

                                //Vector vt_loc = PstLocation.list(0,0,"",PstLocation.fieldNames[PstLocation.FLD_NAME]);
                                //add opie-eyek
                                //algoritma : di check di sistem usernya dimana saja user tsb bisa melakukan create document
                                String whereClause = " ("+PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE +
                                                       " OR "+PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE +")";

                                whereClause += " AND "+PstDataCustom.whereLocReportView(userId, "user_create_document_location");

                                Vector vt_loc = PstLocation.listLocationCreateDocument(0, 0, whereClause, "");
                                for(int d=0;d<vt_loc.size();d++){
                                        Location loc = (Location)vt_loc.get(d);
                                        val_locationid.add(""+loc.getOID()+"");
                                        key_locationid.add(loc.getName());
                                }
                              ControlCheckBox controlCheckBox = new ControlCheckBox();
                              controlCheckBox.setWidth(5);

                          %>
                          <%//=ControlCombo.draw(frmSrcMinimumStock.fieldNames[frmSrcMinimumStock.FRM_FIELD_LOCATION_ID], null, ""+srcMinimumStock.getLocationId(), val_locationid, key_locationid, "", "formElemen")%>
                          <%=controlCheckBox.draw(frmSrcMinimumStock.fieldNames[frmSrcMinimumStock.FRM_FIELD_LOCATION_ID],val_locationid,key_locationid,new Vector())%>
                        </td>
                      </tr>
                      <tr>
                        <td height="21" width="9%"><%=getJspTitle(2,SESS_LANGUAGE,"",true)%></td>
                        <td height="21" width="1%">:</td>
                        <td height="21" width="90%">&nbsp;
                            <%--
						<%
							Vector materGroup = PstCategory.list(0,0,"",PstCategory.fieldNames[PstCategory.FLD_NAME]);
							Vector vectGroupVal = new Vector(1,1);
							Vector vectGroupKey = new Vector(1,1);
                            vectGroupKey.add("0");
                            if(SESS_LANGUAGE==0)
                                vectGroupVal.add("Semua Kategori");
                            else
                                vectGroupVal.add("All Category");

							if(materGroup!=null && materGroup.size()>0){
								for(int i=0; i<materGroup.size(); i++){
									Category mGroup = (Category)materGroup.get(i);
									vectGroupVal.add(mGroup.getName());
									vectGroupKey.add(""+mGroup.getOID());
								}
							}
							out.println(ControlCombo.draw(frmSrcMinimumStock.fieldNames[frmSrcMinimumStock.FRM_FIELD_CATEGORY_ID],"formElemen", null, ""+srcMinimumStock.getCategoryId(), vectGroupKey, vectGroupVal, null));
						%>
                                                --%>
                                                
                                                <select  name="<%=frmSrcMinimumStock.fieldNames[frmSrcMinimumStock.FRM_FIELD_CATEGORY_ID]%>" class="formElemen">
                                                <option value="0">Semua Category</option>
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
                                                             <option value="<%=mGroup.getOID()%>" <%=mGroup.getOID()==srcMinimumStock.getCategoryId()?checked:""%> ><%=parent%><%=mGroup.getName()%></option>
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
                        <td height="21" width="9%"><%=merkName%> <%//=getJspTitle(7,SESS_LANGUAGE,"",true)%></td>
                        <td height="21" width="1%">:</td>
                        <td height="21" width="90%">&nbsp;
						<%
							Vector materMerk = PstMerk.list(0,0,"",PstMerk.fieldNames[PstMerk.FLD_NAME]);
							Vector vectMerkVal = new Vector(1,1);
							Vector vectMerkKey = new Vector(1,1);
                            vectMerkKey.add("0");
                            if(SESS_LANGUAGE==0)
                                vectMerkVal.add("Semua ");
                            else
                                vectMerkVal.add("All ");

                                if(materMerk!=null && materMerk.size()>0){
                                        for(int i=0; i<materMerk.size(); i++){
                                                Merk mGroup = (Merk)materMerk.get(i);
                                                vectMerkVal.add(mGroup.getName());
                                                vectMerkKey.add(""+mGroup.getOID());
                                        }
                                }
                                out.println(ControlCombo.draw(frmSrcMinimumStock.fieldNames[FrmSrcMinimumStock.FRM_FIELD_MERK_ID],"formElemen", null, ""+srcMinimumStock.getCategoryId(), vectMerkKey, vectMerkVal, null));
                        %>
                        </td>
                      </tr>
                      <%if(typeRequest==0){%>
                          <tr>
                            <td height="21" width="9%"><%=getJspTitle(1,SESS_LANGUAGE,"",true)%></td>
                            <td height="21" width="1%">:</td>
                            <td height="21" width="90%">&nbsp;
                                                    <%
                                SrcMemberReg srcMemberReg = new SrcMemberReg();
                                Vector vectContact = SessMemberReg.searchSupplier(srcMemberReg,0,0);
                                Vector vectSuppVal = new Vector(1,1);
                                                            Vector vectSuppKey = new Vector(1,1);
                                vectSuppKey.add("0");
                                if(SESS_LANGUAGE==0)
                                    vectSuppVal.add("All ...");
                                else
                                    vectSuppVal.add("All ...");

                                    if(vectContact!=null && vectContact.size()>0){
                                            for(int i=0; i<vectContact.size(); i++){
                                                    ContactList contactList = (ContactList)vectContact.get(i);
                                                    vectSuppVal.add(contactList.getCompName());
                                                    vectSuppKey.add(""+contactList.getOID());
                                            }
                                    }
                                    out.println(ControlCombo.draw(frmSrcMinimumStock.fieldNames[FrmSrcMinimumStock.FRM_FIELD_SUPPLIER_ID ],"formElemen", null, ""+srcMinimumStock.getOidSupplier(), vectSuppKey, vectSuppVal, null));
                            %>
                          </td>
                          </tr>
                      <%}%>
                      <tr>
                        <td height="21" rowspan="2" valign="top" width="9%" align="left"><%=getJspTitle(6,SESS_LANGUAGE,"",true)%></td>
                        <td height="21" rowspan="2" valign="top" width="1%" align="left">:</td>
                      </tr>
                      <tr align="left">
                        <td height="21" width="90%" valign="top">&nbsp;&nbsp;<input name="txtkode" type="text"  readonly value="<%=txtKode%>" class="formElemen" id="txtkode" size="10"> / <input name="txtnama" type="text" class="formElemen" readonly id="txtnama" value="<%=txtNama%>" size="40">
  <a href="javascript:cekbrg()">CHK</a> * </td>
                      </tr>
                      <tr>
                        <td height="21" valign="top" align="left"><%=getJspTitle(3,SESS_LANGUAGE,"",true)%></td>
                        <td height="21" valign="top" align="left">:</td>
                        <td height="21" valign="top" align="left">&nbsp;
                          <%
							Vector materPeriode = PstPeriode.list(0,0,PstPeriode.fieldNames[PstPeriode.FLD_STATUS] + " = " + PstPeriode.FLD_STATUS_RUNNING,"");
							Vector vectmatPerVal = new Vector(1,1);
							Vector vectmatPerKey = new Vector(1,1);
							if(materPeriode!=null && materPeriode.size()>0){
								for(int i=0; i<materPeriode.size(); i++){
									Periode mper = (Periode)materPeriode.get(i);
									vectmatPerVal.add(mper.getPeriodeName());
									vectmatPerKey.add(""+mper.getOID());
								}
							}
							out.println(ControlCombo.draw(frmSrcMinimumStock.fieldNames[FrmSrcMinimumStock.FRM_FIELD_PERIOD_ID],"formElemen", null, ""+srcMinimumStock.getPeriodId(), vectmatPerKey, vectmatPerVal, null));
						%></td>
                      </tr>
                      <tr>
                        <td height="21" valign="top" width="9%" align="left">&nbsp;</td>
                        <td height="21" valign="top" width="1%" align="left">&nbsp;</td>
                        <td height="21" width="90%" valign="top" align="left">&nbsp;</td>
                      </tr>
                      <tr>
                        <td height="21" valign="top" width="9%" align="left">&nbsp;</td>
                        <td height="21" valign="top" width="1%" align="left">&nbsp;</td>
                        <td height="21" width="90%" valign="top" align="left">
                          <table width="40%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                              <td width="93%" class="btn-primary btn-lg"><a class="btn-primary btn-lg" href="javascript:cmdSearch()"><i class="fa fa-search"> &nbsp;<%=ctrLine.getCommand(SESS_LANGUAGE,"Stock Report",ctrLine.CMD_SEARCH,true)%></i></a></td>
                            </tr>
                          </table>
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>
              </table>
            </form>
            <SCRIPT language=JavaScript>
<!--
//-------------- script control line -------------------
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
//-->
</SCRIPT>
            <!-- #EndEditable --></td> 
        </tr> 
      </table>
    </td>
  </tr>
  <tr> 
    <td colspan="2" height="20"> <!-- #BeginEditable "footer" -->
    <%if(menuUsed == MENU_ICON){%>
            <%@include file="../../../styletemplate/footer.jsp" %>
        <%}else{%>
            <%@ include file = "../../../main/footer.jsp" %>
        <%}%>
      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>

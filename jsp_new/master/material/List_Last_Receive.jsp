<%-- 
    Document   : List_Last_Receive
    Created on : Aug 12, 2013, 4:03:31 PM
    Author     : dimata005
--%>
<%@page import="com.dimata.qdep.form.FRMHandler"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatReceiveItem"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatReceiveItem"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatReceive"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.qdep.entity.I_DocType"%>
<%@page import="com.dimata.qdep.entity.I_Approval"%>
<%@page import="com.dimata.qdep.entity.I_PstDocType"%>
<%@page import="com.dimata.common.entity.payment.PstCurrencyType"%>
<%@page import="com.dimata.common.entity.payment.CurrencyType"%>
<%@page import="com.dimata.util.Formater"%>
<%@page import="java.util.Date"%>
<%@page import="com.dimata.common.entity.contact.ContactList"%>
<%@page import="com.dimata.posbo.entity.purchasing.PurchaseOrder"%>
<%@page import="com.dimata.qdep.entity.I_DocStatus"%>
<%@page import="com.dimata.gui.jsp.ControlList"%>
<%@page import="java.util.Vector"%>
<%@page import="com.dimata.posbo.entity.admin.AppObjInfo"%>
<%@ page import = "com.dimata.common.entity.contact.*" %>
<%@ page import = "com.dimata.common.form.contact.*" %>

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_PURCHASING, AppObjInfo.G2_PURCHASING, AppObjInfo.OBJ_PURCHASE_ORDER); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<!-- Jsp Block -->
<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

public static final String textMainHeader[][] =
{
	{"Gudang","Order Barang"},
	{"Warehouse","Purchase Order"}
};

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] =
{
	{"No","Kode","Tanggal","Supplier","Status","Keterangan","Mata Uang"},
	{"No","Code","Date","Supplier","Status","Remark","Currency"}
};

public String drawList (int language,Vector objectClass,int start)
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
		ctrlist.addHeader("No","3%");
		ctrlist.addHeader("No Document","14%");
		ctrlist.addHeader("Date","7%");
                ctrlist.addHeader("Qty","7%");
                ctrlist.addHeader("Harga Beli","7%");
                ctrlist.addHeader("Contact Supplier","7%");

                ctrlist.setLinkRow(1);
                Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();
                int no=0;
                
                for(int i=0; i<objectClass.size(); i++){
                         no=no+1;
                         MatReceiveItem matreceiveitem = (MatReceiveItem)objectClass.get(i);//new MatReceiveItem();
                         Vector rowx = new Vector();
                         rowx.add(""+no);
                         rowx.add(""+matreceiveitem.getRecCode());
                         rowx.add(""+matreceiveitem.getReceiveDate());
                         rowx.add(""+matreceiveitem.getQty());
                         rowx.add("Rp. "+FRMHandler.userFormatStringDecimal(matreceiveitem.getCost()));
                         if(matreceiveitem.getSupplierId()!=0){
                                ContactList contactList = new ContactList();
                            try {
                                 contactList = PstContactList.fetchExc(matreceiveitem.getSupplierId());
                            } catch (Exception e) {
                            }
                             rowx.add(""+contactList.getCompName()+" / " + contactList.getCompName()+" / "+contactList.getPersonName()+" / "+contactList.getBussAddress()+" / "+contactList.getTelpNr());
                         }else{
                            rowx.add("");
                         }
                         lstData.add(rowx);
			 lstLinkData.add(String.valueOf(""+matreceiveitem.getReceiveMaterialId()));
                }
		result = ctrlist.draw();
	}
	else
	{
		result = "<div class=\"msginfo\">&nbsp;&nbsp;Tidak ada data order pembelian ...</div>";
	}
	return result;
}
%>

<%
long oidMaterial = FRMQueryString.requestLong(request, "oidMaterial");
String orderLastReceiveBy = PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE]+" DESC ";
String whereLastReceiveBy = "("+PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS]+"=5 OR "+PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS]+"=7)";           
Vector record = PstMatReceiveItem.listLastReceive(oidMaterial,0,5,""+whereLastReceiveBy,orderLastReceiveBy);
int prochainMenuBootstrap = Integer.parseInt(PstSystemProperty.getValueByName("PROCHAIN_MENU_BOOTSTRAP"));
%>
<!-- End of Jsp Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
function cmdAdd(){
	document.frm_ordermaterial.start.value=0;
	document.frm_ordermaterial.approval_command.value="<%=Command.SAVE%>";
	document.frm_ordermaterial.command.value="<%=Command.ADD%>";
	document.frm_ordermaterial.add_type.value="<%=ADD_TYPE_LIST%>";
	document.frm_ordermaterial.action="pomaterial_edit.jsp";
	if(compareDateForAdd()==true)
		document.frm_ordermaterial.submit();
}

function cmdEdit(oid){
	document.frm_ordermaterial.hidden_material_order_id.value=oid;
	document.frm_ordermaterial.start.value=0;
	document.frm_ordermaterial.approval_command.value="<%=Command.APPROVE%>";
	document.frm_ordermaterial.command.value="<%=Command.EDIT%>";
        <%if (prochainMenuBootstrap==1){%>
            document.frm_ordermaterial.action="../../warehouse/material/receive/receive_wh_supp_material_edit.jsp?hidden_receive_id="+oid+"&start=0&approval_command=44&command=3";
        <%}else{%>
            document.frm_ordermaterial.action="../../warehouse/material/receive/receive_wh_supp_po_material_edit.jsp?hidden_receive_id="+oid+"&start=0&approval_command=44&command=3";
        <%}%>    
	document.frm_ordermaterial.submit();
}

function cmdListFirst(){
	document.frm_ordermaterial.command.value="<%=Command.FIRST%>";
	document.frm_ordermaterial.action="pomaterial_list.jsp";
	document.frm_ordermaterial.submit();
}

function cmdListPrev(){
	document.frm_ordermaterial.command.value="<%=Command.PREV%>";
	document.frm_ordermaterial.action="pomaterial_list.jsp";
	document.frm_ordermaterial.submit();
}

function cmdListNext(){
	document.frm_ordermaterial.command.value="<%=Command.NEXT%>";
	document.frm_ordermaterial.action="pomaterial_list.jsp";
	document.frm_ordermaterial.submit();
}

function cmdListLast(){
	document.frm_ordermaterial.command.value="<%=Command.LAST%>";
	document.frm_ordermaterial.action="pomaterial_list.jsp";
	document.frm_ordermaterial.submit();
}

function cmdBack(){
	document.frm_ordermaterial.command.value="<%=Command.BACK%>";
	document.frm_ordermaterial.action="srcpomaterial.jsp";
	document.frm_ordermaterial.submit();
}

//------------------------- START JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------
function MM_swapImgRestore() { //v3.0
	var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
	var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
	var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
	if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_swapImage() { //v3.0
	var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
	if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}
//------------------------- END JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------
</script>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<!-- #BeginEditable "styles" -->
<link rel="stylesheet" href="../../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" -->
<link rel="stylesheet" href="../../styles/tab.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "headerscript" -->
<SCRIPT language=JavaScript>
<!--
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

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}
//-->
</SCRIPT>
<!-- #EndEditable -->
</head>

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('<%=approot%>/images/BtnNewOn.jpg','<%=approot%>/images/BtnBackOn.jpg')">
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
  <%@include file="../../styletemplate/template_header_empty.jsp" %>
  <tr>
    <td valign="top" align="left">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="20" class="mainheader">History Receiving</td>
        </tr>
        <tr>
          <td><!-- #BeginEditable "content" -->
            <form name="frm_ordermaterial" method="post" action="">
              <input type="hidden" name="command" value="">
              <input type="hidden" name="add_type" value="">
              <input type="hidden" name="start" value="">
              <input type="hidden" name="hidden_material_order_id" value="">
              <input type="hidden" name="approval_command">
			  <table width="100%" cellspacing="0" cellpadding="3">
			  <tr align="left" valign="top">
				<td height="22" valign="middle" colspan="3"><%=drawList(0,record,0)%></td>
			  </tr>
			  <tr align="left" valign="top">
				<td height="18" valign="top" colspan="3">
				    <table width="44%" border="0" cellspacing="0" cellpadding="0">
                    </table>
				</td>
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
       <%if(menuUsed == MENU_ICON){%>
            <%@include file="../../styletemplate/footer.jsp" %>
           
        <%}else{%>
            <%@ include file = "../../main/footer.jsp" %>
        <%}%>
      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>


<%-- 
    Document   : po_distribution_location
    Created on : Nov 22, 2013, 5:52:23 PM
    Author     : dimata005
--%>

<%@page import="com.dimata.gui.jsp.ControlList"%>
<%@page import="com.dimata.gui.jsp.ControlLine"%>
<%@page import="com.dimata.posbo.entity.purchasing.PstPurchaseOrder"%>
<%@page import="com.dimata.posbo.entity.purchasing.PurchaseOrder"%>
<%@page import="com.dimata.gui.jsp.ControlCombo"%>
<%@page import="com.dimata.posbo.entity.purchasing.PstDistributionPurchaseOrder"%>
<%@page import="com.dimata.posbo.entity.purchasing.DistributionPurchaseOrder"%>
<%@page import="com.dimata.posbo.form.purchasing.FrmDistributionPurchaseOrder"%>
<%@page import="com.dimata.posbo.form.purchasing.CtrlDistributionPurchaseOrder"%>
<%@page import="com.dimata.common.entity.location.Location"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.qdep.form.FRMMessage"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.posbo.entity.admin.AppObjInfo"%>
<%@page import="com.dimata.common.entity.location.PstLocation"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_PURCHASING, AppObjInfo.G2_PURCHASING, AppObjInfo.OBJ_PURCHASE_ORDER); %>
<%@ include file = "../../../main/checkuser.jsp" %>
<%
privAdd=true;
%>
<%!
public static int MAX_LIST_QTY_DISC = 8;
/* this constant used to list text of listHeader */
public static final String textListHeader[][] =
{
	{"No","LOKASI","PERSENTASE DISTRIBUSI (%)","DISKON","TIPE DISKON","Editor Barang"},
	{"No","LOCATION","PERSENTASE DISTRIBUSI (%)","DISCOUNT","DISCOUNT TYPE","Goods Editor"}
};

public static final String textListGlobalHeader[][] =
{
	{"DAFTAR DISTRIBUTION LOCATION","LOKASI","MATA UANG","TIPE MEMBER","KODE PO", "Lokasi Distribusi", "Berlaku juga untuk lokasi","Berlaku Juga Untuk Type Member"},
	{"DISCOUNT QTY LIST","LOCATION","CURRENCY","MEMBER TYPE","MATERIAL NAME", "Distribusi Location", "Also for location","Also for Type Member"}
};

public Vector drawListVector(int language,int iCommand,FrmDistributionPurchaseOrder frmObject,DistributionPurchaseOrder objEntity, Vector objectClass,int start, Vector objecLocation, long oidPurchaseOrder)
{
        String result = "";
        Vector listAll = new Vector(1, 1);

	ControlList ctrlist = new ControlList();
	ctrlist.setAreaWidth("70%");
	ctrlist.setListStyle("listgen");
	ctrlist.setTitleStyle("listgentitle");
	ctrlist.setCellStyle("listgensell");
	ctrlist.setHeaderStyle("listgentitle");
        
        ctrlist.dataFormat(textListHeader[language][0],"3%","left","left");
        ctrlist.dataFormat(textListHeader[language][1],"20%","left","left");
        ctrlist.dataFormat(textListHeader[language][2],"15%","left","left");

	ctrlist.setLinkRow(0);
	ctrlist.setLinkPrefix("javascript:cmdEdit('");
	ctrlist.setLinkSufix("')");
	Vector lstData = ctrlist.getData();
	Vector lstLinkData = ctrlist.getLinkData();
	Vector rowx = new Vector(1,1);
	ctrlist.reset();
	int index = -1;
	if(start<0)
	start = 0;

    //get Tipe Diskon
    //Vector val_base = new Vector(1,1); //hidden values that will be deliver on request (oids)
    //Vector key_base = new Vector(1,1); //texts that displayed on combo box

    for(int i = 0; i < objecLocation.size(); i++)
	{
                Location location =(Location)objecLocation.get(i);
              
                DistributionPurchaseOrder distributionPurchaseOrder = new DistributionPurchaseOrder();
                try{
                    distributionPurchaseOrder = PstDistributionPurchaseOrder.getDistWithLocation(location.getOID(), oidPurchaseOrder);
                }catch(Exception ex){
                    distributionPurchaseOrder = new DistributionPurchaseOrder();
                }
                //  distributionPurchaseOrder = (DistributionPurchaseOrder)objectClass.get(i);
                rowx = new Vector();

                start = start + 1;
                rowx.add("" + start);
                //rowx.add("<div align=\"center\">"+location.getName()+"</div>");
                double qty =0.0;
                if(distributionPurchaseOrder.getQty()!=0){
                    qty=distributionPurchaseOrder.getQty();
                }else{
                    qty=location.getPersentaseDistributionPurchaseOrder();
                }
                rowx.add("<div align=\"left\"><input name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_PURCHASE_DISTRIBUTION_ORDER_ID] +"_"+i+"_oid\" type=\"hidden\" value=\""+distributionPurchaseOrder.getOID()+"\"><input name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_LOCATION_ID] +"_"+i+"\" type=\"hidden\" value=\""+location.getOID()+"\">"+location.getName()+"</div>");
                rowx.add("<div align=\"center\"><input type=\"text\" size=\"17\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY]+"_"+i+"\" value=\""+qty+"\" class=\"formElemen\" style=\"text-align:right\">  &nbsp;</div>");
                lstData.add(rowx);
            }

	result = ctrlist.draw();
        listAll.add(result);
       return listAll;
}
%>
<%

int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int prevCommand = FRMQueryString.requestInt(request, "prev_command");
long oidPurchaseOrder = FRMQueryString.requestLong(request, "hidden_oidPurchaseOrder");
long oidDistributionPurchaseOrder = FRMQueryString.requestLong(request, "hidden_oidDistributionPurchaseOrder");
String merkTitle = textListHeader[SESS_LANGUAGE][5];
String saveTitle = textListGlobalHeader[SESS_LANGUAGE][5];

/*variable declaration*/
int recordToGet = 0;
String msgString = "";
int iErrCode = FRMMessage.NONE;
String whereClause = "";

CtrlDistributionPurchaseOrder ctrlDistributionPurchaseOrder = new CtrlDistributionPurchaseOrder(request);
ControlLine ctrLine = new ControlLine();
Vector listDistributionPurchaseOrder = new Vector(1,1);
Vector listLocation = new Vector(1,1);

/*switch statement */
//int cmd, long oidPurchaseOrder, long oidDistributionPurchaseOrder,String nameUser, long userID
//iErrCode = ctrlDistributionPurchaseOrder.action(iCommand, oidPurchaseOrder, oidDistributionPurchaseOrder,userName,userId,request);
//iErrCode = ctrlDiscountQtyMapping.action(iCommand, list);
/* end switch*/
FrmDistributionPurchaseOrder frmDistributionPurchaseOrder = ctrlDistributionPurchaseOrder.getForm();

/*count list All Unit*/
int vectSize = 10;//PstDiscountQtyMapping.getCount(whereClause);

DistributionPurchaseOrder distributionPurchaseOrder = ctrlDistributionPurchaseOrder.getDistributionPurchaseOrder();
//msgString =  ctrlDistributionPurchaseOrder.getMessage();
//Vector listDiscQtyMapping = ctrlDistributionPurchaseOrder.getDistributionPurchaseOrder();

/* get record to display */
listDistributionPurchaseOrder = PstDistributionPurchaseOrder.list(start,recordToGet, whereClause , "");
listLocation = PstLocation.listAll();

/*get PO*/
PurchaseOrder purchaseOrder = new PurchaseOrder();
purchaseOrder = PstPurchaseOrder.fetchExc(oidPurchaseOrder);

/*handle condition if size of record to display = 0 and start > 0 	after delete*/
/*if (listDistributionPurchaseOrder.size() < 1 && start > 0)
{
	 if (vectSize - recordToGet > recordToGet)
			start = start - recordToGet;   //go to Command.PREV
	 else
	 {
		 start = 0 ;
		 iCommand = Command.FIRST;
		 prevCommand = Command.FIRST; //go to Command.FIRST
	 }
	 //listDiscQty = PstUnit.list(start,recordToGet, whereClause , "");
}*/

Vector list = drawListVector(SESS_LANGUAGE,iCommand,frmDistributionPurchaseOrder, distributionPurchaseOrder, listDistributionPurchaseOrder,start,listLocation,oidPurchaseOrder);
String str = "";
try{
    str = (String)list.get(0);
}catch(Exception e) {
}

if (iCommand == Command.SAVE){
    ctrlDistributionPurchaseOrder.requestBarcode(listLocation.size(),oidPurchaseOrder,request);
}

%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">

function cmdSave()
{
	document.frmmaterial.command.value="<%=Command.SAVE%>";
	document.frmmaterial.prev_command.value="<%=prevCommand%>";
	document.frmmaterial.action="po_distribution_location.jsp";
	document.frmmaterial.submit();
        //self.close();
}

function cmdEdit(oidUnit)
{
	document.frmdiscqty.hidden_merk_id.value=oidUnit;
	document.frmdiscqty.command.value="<%=Command.EDIT%>";
	document.frmdiscqty.prev_command.value="<%=prevCommand%>";
	document.frmdiscqty.action="po_distribution_location.jsp";
	document.frmdiscqty.submit();
}

function cmdBack()
{
        //document.frmdiscqty.hidden_material_id.value = oidMaterial;
	//document.frmdiscqty.command.value="<%=Command.BACK%>";
	//document.frmdiscqty.action="material_main.jsp";
	//document.frmdiscqty.submit();
        self.close();
}

function cmdListFirst()
{
	document.frmdiscqty.command.value="<%=Command.FIRST%>";
	document.frmdiscqty.prev_command.value="<%=Command.FIRST%>";
	document.frmdiscqty.action="po_distribution_location.jsp";
	document.frmdiscqty.submit();
}

function cmdListPrev()
{
	document.frmdiscqty.command.value="<%=Command.PREV%>";
	document.frmdiscqty.prev_command.value="<%=Command.PREV%>";
	document.frmdiscqty.action="po_distribution_location.jsp";
	document.frmdiscqty.submit();
}

function cmdListNext()
{
	document.frmdiscqty.command.value="<%=Command.NEXT%>";
	document.frmdiscqty.prev_command.value="<%=Command.NEXT%>";
	document.frmdiscqty.action="po_distribution_location.jsp";
	document.frmdiscqty.submit();
}

function cmdListLast()
{
	document.frmdiscqty.command.value="<%=Command.LAST%>";
	document.frmdiscqty.prev_command.value="<%=Command.LAST%>";
	document.frmdiscqty.action="po_distribution_location.jsp";
	document.frmdiscqty.submit();
}

function setChecked(val) {
}

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

</script>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<!-- #BeginEditable "styles" -->
<link rel="stylesheet" href="../../../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" -->
<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "headerscript" -->
<SCRIPT language=JavaScript>
</SCRIPT>
<!-- #EndEditable -->
</head>

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
  <tr>
    <td width="88%" valign="top" align="left">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="20" class="mainheader">
              <%@include file="../../../styletemplate/template_header_empty.jsp" %>
          </td>
        </tr>
        <tr>
          <td><!-- #BeginEditable "content" -->
            <form name="frmmaterial" method ="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="vectSize" value="<%=vectSize%>">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="hidden_oidPurchaseOrder" value="<%=oidPurchaseOrder %>">
              <input type="hidden" name="hidden_oidDistributionPurchaseOrder" value="<%=oidDistributionPurchaseOrder %>">
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr align="left" valign="top">
                  <td height="8"  colspan="3">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr align="left" valign="top">
                        <td height="14" valign="middle" colspan="4" align="center">
                          <h4><strong><%=textListGlobalHeader[SESS_LANGUAGE][0]%></strong></h4>
                          <hr size="1">                        </td>
                      </tr>
                      <tr align="left" valign="top">
                        <td width="21%" height="14" valign="middle" class="command"><b><%=textListGlobalHeader[SESS_LANGUAGE][4]%></b></td>
                        <td width="1%" height="14" valign="middle" class="command">:</td>
                        <td width="48%" height="14" valign="middle" class="command"><b><%=purchaseOrder.getPoCode()%></b></td>
                        <td width="30%" height="14" valign="middle" class="command">&nbsp;</td>
                      </tr>
                      <%
			try{
		      %>
                      <tr align="left" valign="top">
                        <td height="22" valign="middle" colspan="4"> <%=str%> </td>
                      </tr>
                      <%
                      }catch(Exception exc){
                      }%>
                      <tr align="left" valign="top">
                        <td height="8" align="left" colspan="4" class="command">
                          <span class="command">
                          <%
				int cmd = 0;
			  %>
                          <% ctrLine.setLocationImg(approot+"/images");
			     ctrLine.initDefault();
								 %>
                          </span> </td>
                      </tr>
                    </table>
                  </td>
                </tr>
                <tr align="left" valign="top" >
                  <td colspan="3" class="command">
                    <%
                    ctrLine.setLocationImg(approot+"/images");

                    // set image alternative caption
                    ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,saveTitle,ctrLine.CMD_SAVE,true));
                    ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,merkTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,merkTitle,ctrLine.CMD_BACK,true)+" List");

                    ctrLine.initDefault();
                    ctrLine.setTableWidth("80%");

                    ctrLine.setCommandStyle("command");
                    ctrLine.setColCommStyle("command");

                    // set command caption
                    ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE,saveTitle,ctrLine.CMD_SAVE,true));
                    ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,merkTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,merkTitle,ctrLine.CMD_BACK,true)+" List");

                    if (privAdd == false){
                            ctrLine.setAddCaption("");
                    }
                    %>
                    <%if(iCommand==Command.ADD || iCommand==Command.EDIT || iCommand==Command.ASK || ((iCommand==Command.SAVE || iCommand==Command.DELETE)&&iErrCode!=FRMMessage.NONE)){%>
                    <%= ctrLine.drawImage(iCommand, iErrCode, msgString)%><%}%></td>
                </tr>
              </table>
            </form>
              <% if(iCommand==Command.SAVE) {
                %>
               <script language="Javascript">
                      self.opener.document.forms.frm_purchaseorder.hidden_material_order_id.value="<%=oidPurchaseOrder%>";
                      self.opener.document.forms.frm_purchaseorder.command.value="<%=Command.EDIT%>";
                      self.opener.document.forms.frm_purchaseorder.submit();
                      self.close();
               </script>
            <% } %>
            <!-- #EndEditable --></td>
        </tr>
      </table>
    </td>
  </tr>
  <!--<tr>
    <td colspan="2" height="20"> <!-- #BeginEditable "footer" -->
      <%//@ include file = "../../main/footer.jsp" %>
      <!-- #EndEditable --> <!--</td>-->
 <!--</tr>-->
</table>
</body>
<!-- #EndTemplate --></html>

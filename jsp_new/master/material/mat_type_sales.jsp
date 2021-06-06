<%-- 
    Document   : mat_type_sales
    Created on : Feb 19, 2015, 5:29:45 PM
    Author     : dimata005
--%>

<%@page import="com.dimata.posbo.entity.masterdata.SalesType"%>
<%@ page language = "java" %>
<%@ page import = "java.util.*,
                   com.dimata.posbo.entity.masterdata.SalesType,
                   com.dimata.posbo.entity.masterdata.PstSalesType,
                   com.dimata.posbo.form.masterdata.CtrlSalesType,
                   com.dimata.posbo.form.masterdata.FrmSalesType,
                   com.dimata.gui.jsp.ControlLine,
                   com.dimata.gui.jsp.ControlList,
                   com.dimata.posbo.jsp.JspInfo" %>
<%@ page import = "com.dimata.util.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_SALES_TYPE); %>
<%@ include file = "../../main/checkuser.jsp" %>
<%
boolean privEditPrice = true;
%>

<!-- Jsp Block -->
<%!
/* this constant used to list text of listHeader */
public static final String textListHeader[][] =
{
	{"No.","Nama","Type", "Tipe Sales"},
	{"No.","Name","type", "Sales Type"}
};

/* this method used to list material department */
public String drawList(int language,Vector objectClass,long costingId, int start)
{
	ControlList ctrlist = new ControlList();
	ctrlist.setAreaWidth("80%");
	ctrlist.setListStyle("listgen");
	ctrlist.setTitleStyle("listgentitle");
	ctrlist.setCellStyle("listgensell");
	ctrlist.setHeaderStyle("listgentitle");
	ctrlist.dataFormat(textListHeader[language][0],"5%","center","left");
	ctrlist.dataFormat(textListHeader[language][1],"30%","center","left");
        ctrlist.dataFormat(textListHeader[language][2],"10%","center","left");
    

	ctrlist.setLinkRow(1);
	ctrlist.setLinkSufix("");
	Vector lstData = ctrlist.getData();
	Vector lstLinkData = ctrlist.getLinkData();
	ctrlist.setLinkPrefix("javascript:cmdEdit('");
	ctrlist.setLinkSufix("')");
	ctrlist.reset();
	

	for(int i=0; i<objectClass.size(); i++)
	{
		SalesType costing= (SalesType)objectClass.get(i);
		Vector rowx = new Vector();
		
		rowx.add(""+(i+start+1));
		rowx.add(costing.getName());
		rowx.add(""+PstSalesType.nameTypeSales[costing.getTypeSales()]);


		lstData.add(rowx);
		lstLinkData.add(String.valueOf(costing.getOID()));
	}
	return ctrlist.draw();
}
%>

<%
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int prevCommand = FRMQueryString.requestInt(request, "prev_command");
long oidTypeSales = FRMQueryString.requestLong(request, "hidden_typle_sales_id");
//String merkTitle = com.dimata.posbo.jsp.JspInfo.txtMaterialInfo[SESS_LANGUAGE][com.dimata.posbo.jsp.JspInfo.MATERIAL_SHIFT];

/*variable declaration*/
int recordToGet = 15;
String msgString = "";
int iErrCode = FRMMessage.NONE;
String whereClause = "";
String orderClause = "";

//costing Title
String costingTitle = textListHeader[SESS_LANGUAGE][3];//i_pstDocType.getDocTitle(docType);
String costingItemTitle = costingTitle + " Item";

CtrlSalesType ctrlSalesType = new CtrlSalesType(request);
ControlLine ctrLine = new ControlLine();
Vector listCosting = new Vector(1,1);

/*switch statement */
iErrCode = ctrlSalesType.action(iCommand , oidTypeSales, request);
/* end switch*/
FrmSalesType frmSalesType = ctrlSalesType.getForm();

/*count list All SalesType*/
int vectSize = PstSalesType.getCount(whereClause);

/*switch list SalesType*/
if((iCommand == Command.FIRST || iCommand == Command.PREV )||
  (iCommand == Command.NEXT || iCommand == Command.LAST)){
		start = ctrlSalesType.actionList(iCommand, start, vectSize, recordToGet);
  }
/* end switch list*/

SalesType costing = ctrlSalesType.getCosting();
msgString =  ctrlSalesType.getMessage();

/* get record to display */
listCosting = PstSalesType.list(start,recordToGet, whereClause , orderClause);

/*handle condition if size of record to display = 0 and start > 0 	after delete*/
if (listCosting.size() < 1 && start > 0)
{
	 if (vectSize - recordToGet > recordToGet)
			start = start - recordToGet;   //go to Command.PREV
	 else
	 {
		 start = 0 ;
		 iCommand = Command.FIRST;
		 prevCommand = Command.FIRST; //go to Command.FIRST
	 }
	 listCosting = PstSalesType.list(start,recordToGet, whereClause , orderClause);

}
%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
function cmdAdd()
{
	document.frmtypesales.hidden_typle_sales_id.value="0";
	document.frmtypesales.command.value="<%=Command.ADD%>";
	document.frmtypesales.prev_command.value="<%=prevCommand%>";
	document.frmtypesales.action="mat_type_sales.jsp";
	document.frmtypesales.submit();
}

function cmdAsk(oidTypeSales)
{
	document.frmtypesales.hidden_typle_sales_id.value=oidTypeSales;
	document.frmtypesales.command.value="<%=Command.ASK%>";
	document.frmtypesales.prev_command.value="<%=prevCommand%>";
	document.frmtypesales.action="mat_type_sales.jsp";
	document.frmtypesales.submit();
}

function cmdConfirmDelete(oidTypeSales)
{
	document.frmtypesales.hidden_typle_sales_id.value=oidTypeSales;
	document.frmtypesales.command.value="<%=Command.DELETE%>";
	document.frmtypesales.prev_command.value="<%=prevCommand%>";
	document.frmtypesales.action="mat_type_sales.jsp";
	document.frmtypesales.submit();
}
function cmdSave()
{
	document.frmtypesales.command.value="<%=Command.SAVE%>";
	document.frmtypesales.prev_command.value="<%=prevCommand%>";
	document.frmtypesales.action="mat_type_sales.jsp";
	document.frmtypesales.submit();
}

function cmdEdit(oidTypeSales)
{
	document.frmtypesales.hidden_typle_sales_id.value=oidTypeSales;
	document.frmtypesales.command.value="<%=Command.EDIT%>";
	document.frmtypesales.prev_command.value="<%=prevCommand%>";
	document.frmtypesales.action="mat_type_sales.jsp";
	document.frmtypesales.submit();
}

function cmdCancel(oidTypeSales)
{
	document.frmtypesales.hidden_typle_sales_id.value=oidTypeSales;
	document.frmtypesales.command.value="<%=Command.EDIT%>";
	document.frmtypesales.prev_command.value="<%=prevCommand%>";
	document.frmtypesales.action="mat_type_sales.jsp";
	document.frmtypesales.submit();
}

function cmdBack()
{
	document.frmtypesales.command.value="<%=Command.BACK%>";
	document.frmtypesales.action="mat_type_sales.jsp";
	document.frmtypesales.submit();
}

function cmdListFirst()
{
	document.frmtypesales.command.value="<%=Command.FIRST%>";
	document.frmtypesales.prev_command.value="<%=Command.FIRST%>";
	document.frmtypesales.action="mat_type_sales.jsp";
	document.frmtypesales.submit();
}

function cmdListPrev()
{
	document.frmtypesales.command.value="<%=Command.PREV%>";
	document.frmtypesales.prev_command.value="<%=Command.PREV%>";
	document.frmtypesales.action="mat_type_sales.jsp";
	document.frmtypesales.submit();
}

function cmdListNext()
{
	document.frmtypesales.command.value="<%=Command.NEXT%>";
	document.frmtypesales.prev_command.value="<%=Command.NEXT%>";
	document.frmtypesales.action="mat_type_sales.jsp";
	document.frmtypesales.submit();
}

function cmdListLast()
{
	document.frmtypesales.command.value="<%=Command.LAST%>";
	document.frmtypesales.prev_command.value="<%=Command.LAST%>";
	document.frmtypesales.action="mat_type_sales.jsp";
	document.frmtypesales.submit();
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
<%if(menuUsed == MENU_ICON){%>
    <link href="../../stylesheets/general_home_style.css" type="text/css"
rel="stylesheet" />
<%}%>
<link rel="stylesheet" href="../../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" -->
<link rel="stylesheet" href="../../styles/tab.css" type="text/css">
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
      <%@ include file = "../../main/header.jsp" %>
      <!-- #EndEditable --></td>
  </tr>
  <tr>
    <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
      <%@ include file = "../../main/mnmain.jsp" %>
      <!-- #EndEditable --> </td>
  </tr>
  <%}else{%>
   <tr bgcolor="#FFFFFF">
    <td height="10" ID="MAINMENU">
      <%@include file="../../styletemplate/template_header.jsp" %>
    </td>
  </tr>
  <%}%>
  <tr> 
    <td valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">  
        <tr> 
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->
            Masterdata &gt; <%=costingTitle%><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" -->
            <form name="frmtypesales" method ="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="vectSize" value="<%=vectSize%>">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="hidden_typle_sales_id" value="<%=oidTypeSales%>">
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr align="left" valign="top">
                  <td height="8"  colspan="3">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr align="left" valign="top">
                        <td height="8" valign="middle" colspan="3">
                          <hr size="1">
                        </td>
                      </tr>
                      <tr align="left" valign="top">
                        <td height="14" valign="middle" colspan="3" class="comment">&nbsp;<u><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar "+costingTitle : costingTitle+" List"%></u></td>
                      </tr>
                      <%
					  if (listCosting.size()>0)
					  {
					  %>
                      <tr align="left" valign="top">
                        <td height="22" valign="middle" colspan="3"> <%=drawList(SESS_LANGUAGE,listCosting,oidTypeSales,start)%> </td>
                      </tr>
                      <%
					  }
					  %>
                      <tr align="left" valign="top">
                        <td height="8" align="left" colspan="3" class="command">
                          <span class="command">
                          <%
								   int cmd = 0;
									   if ((iCommand == Command.FIRST || iCommand == Command.PREV )||
										(iCommand == Command.NEXT || iCommand == Command.LAST))
											cmd =iCommand;
								   else{
									  if(iCommand == Command.NONE || prevCommand == Command.NONE)
										cmd = Command.FIRST;
									  else
									  	cmd =prevCommand;
								   }
							    %>
                          <% ctrLine.setLocationImg(approot+"/images");
							   	ctrLine.initDefault();
								 %>
                          <%=ctrLine.drawImageListLimit(cmd,vectSize,start,recordToGet)%> </span> </td>
                      </tr>
                      <tr align="left" valign="top">
                        <td height="22" valign="middle" colspan="3">
						<%if(iCommand!=Command.ADD && iCommand!=Command.EDIT && iCommand!=Command.ASK && iErrCode==FRMMessage.NONE){%>
                          <table width="17%" border="0" cellspacing="2" cellpadding="3">
                            <tr>
                              <%if(privAdd){%>
                              <!--td width="18%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,costingTitle,ctrLine.CMD_ADD,true)%>"></a></td-->
                              <br><td nowrap width="82%"><a class="btn btn-lg btn-primary" href="javascript:cmdAdd()" class="command"><i class="fa fa-plus-circle"></i> <%=ctrLine.getCommand(SESS_LANGUAGE,costingTitle,ctrLine.CMD_ADD,true)%></a></td>
                              <%}%>
                            </tr>
                          </table>
                          <%}%>
						</td>
                      </tr>
                    </table>
                  </td>
                </tr>
                <tr align="left" valign="top">
                  <td height="8" valign="middle" colspan="3">
                    <%
					  if((iCommand == Command.ADD)
					     || (iCommand == Command.EDIT)
						 || (iCommand == Command.ASK)
						 || ((iCommand==Command.SAVE || iCommand==Command.DELETE) && (iErrCode>0))
						)
					  {
					%>
                    <table width="100%" border="0" cellspacing="2" cellpadding="0">
                      <tr align="left">
                        <td colspan="10" class="comment" height="30"><u><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Editor "+costingTitle : costingTitle+" Editor"%></u></td>
                      <tr align="left">
                        <td width="9%">&nbsp;<%=textListHeader[SESS_LANGUAGE][1]%></td>
                        <td width="1%">:
                        <td colspan="8" width="90%">
                          <input type="text" name="<%=frmSalesType.fieldNames[FrmSalesType.FRM_FIELD_TYPE_SALES_NAME] %>"  value="<%= costing.getName() %>" class="formElemen" size="30">
                          * <%=frmSalesType.getErrorMsg(frmSalesType.FRM_FIELD_TYPE_SALES_NAME)%>
                      
                      <tr align="left">
                        <td width="9%" valign="top">&nbsp;<%=textListHeader[SESS_LANGUAGE][2]%></td>
                        <td width="1%" valign="top">:
                        <td colspan="8" width="90%">
                            <input type="radio" name="<%=frmSalesType.fieldNames[FrmSalesType.FRM_FIELD_TYPE_USE_SALES] %>" value="<%=PstSalesType.TYPE_DINE_IN%>" <%=costing.getTypeSales()==PstSalesType.TYPE_DINE_IN?"checked":""%> >Dine In<br>
                         <input type="radio" name="<%=frmSalesType.fieldNames[FrmSalesType.FRM_FIELD_TYPE_USE_SALES] %>" value="<%=PstSalesType.TYPE_TAKE_AWAY%>" <%=costing.getTypeSales()==PstSalesType.TYPE_TAKE_AWAY?"checked":""%> >Take Away<br>
                         <input type="radio" name="<%=frmSalesType.fieldNames[FrmSalesType.FRM_FIELD_TYPE_USE_SALES] %>" value="<%=PstSalesType.TYPE_DELIVERY%>"<%=costing.getTypeSales()==PstSalesType.TYPE_DELIVERY?"checked":""%> >Delivery<br>
                         <input type="radio" name="<%=frmSalesType.fieldNames[FrmSalesType.FRM_FIELD_TYPE_USE_SALES] %>" value="<%=PstSalesType.TYPE_CATHERING%>" <%=costing.getTypeSales()==PstSalesType.TYPE_CATHERING?"checked":""%>>Event<br>
                         <input type="radio" name="<%=frmSalesType.fieldNames[FrmSalesType.FRM_FIELD_TYPE_USE_SALES] %>" value="<%=PstSalesType.TYPE_EVENT%>" <%=costing.getTypeSales()==PstSalesType.TYPE_EVENT?"checked":""%>>Cathering<br>
                      <tr align="left" >
                        <td colspan="10" class="command" valign="top">                      
                      </tr>
                      <tr align="left" >
                        <td colspan="10" class="command" valign="top">                      
                      </tr>
                      <tr align="left" >
                        <td colspan="10" class="command" valign="top">
                          <%
							ctrLine.setLocationImg(approot+"/images");

							// set image alternative caption
							ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,costingTitle,ctrLine.CMD_SAVE,true));
							ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,costingTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,costingTitle,ctrLine.CMD_BACK,true)+" List");
							ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,costingTitle,ctrLine.CMD_ASK,true));
							ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,costingTitle,ctrLine.CMD_CANCEL,false));

							ctrLine.initDefault();
							ctrLine.setTableWidth("80%");
							String scomDel = "javascript:cmdAsk('"+oidTypeSales+"')";
							String sconDelCom = "javascript:cmdConfirmDelete('"+oidTypeSales+"')";
							String scancel = "javascript:cmdEdit('"+oidTypeSales+"')";
							ctrLine.setCommandStyle("command");
							ctrLine.setColCommStyle("command");

							// set command caption
							ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE,costingTitle,ctrLine.CMD_SAVE,true));
							ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,costingTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,costingTitle,ctrLine.CMD_BACK,true)+" List");
							ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE,costingTitle,ctrLine.CMD_ASK,true));
							ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE,costingTitle,ctrLine.CMD_DELETE,true));
							ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE,costingTitle,ctrLine.CMD_CANCEL,false));


							if (privDelete){
								ctrLine.setConfirmDelCommand(sconDelCom);
								ctrLine.setDeleteCommand(scomDel);
								ctrLine.setEditCommand(scancel);
							}else{
								ctrLine.setConfirmDelCaption("");
								ctrLine.setDeleteCaption("");
								ctrLine.setEditCaption("");
							}

							if(iCommand == Command.EDIT && privUpdate == false){
								ctrLine.setSaveCaption("");
							}

							if (privAdd == false){
								ctrLine.setAddCaption("");
							}
							ctrLine.setAddCaption("");
						  %>
                          <%
						  if( (iCommand==Command.ADD)
						      || (iCommand==Command.EDIT)
							  || (iCommand==Command.ASK)
						      || ((iCommand==Command.SAVE || iCommand==Command.DELETE) && iErrCode>0)
							)
						  {
						  out.println(ctrLine.drawImage(iCommand, iErrCode, msgString));
						  }
						  %>
                      </tr>
                    </table>
					<script language="javascript">
						document.frmcategory.<%=frmSalesType.fieldNames[FrmSalesType.FRM_FIELD_TYPE_SALES_NAME]%>.focus();
					</script>
                    <%}%>
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

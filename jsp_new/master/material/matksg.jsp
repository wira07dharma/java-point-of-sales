<%@page import="com.dimata.posbo.db.DBException"%>
<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package material -->
<%@ page import = "com.dimata.posbo.entity.masterdata.*" %>
<%@ page import = "com.dimata.posbo.form.masterdata.*" %>

<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_KSG); %>
<%@ include file = "../../main/checkuser.jsp" %>



<!-- Jsp Block -->
<%!
/* this constant used to list text of listHeader */
public static final String textListHeader[][] = 
{
	{"No","Nama","Ksg","Daftar","Kode","Lokasi"},
	{"No","Name","Ksg","List of","Code","Location"}
};

/* this method used to list material ksg */
public String drawList(int language,int iCommand,FrmKsg frmObject,Ksg objEntity,Vector objectClass,long ksgId,int start)
{
	ControlList ctrlist = new ControlList();
	ctrlist.setAreaWidth("50%");
	ctrlist.setListStyle("listgen");
	ctrlist.setTitleStyle("listgentitle");
	ctrlist.setCellStyle("listgensell");
	ctrlist.setHeaderStyle("listgentitle");
	ctrlist.addHeader(textListHeader[language][0],"5%");
	ctrlist.addHeader(textListHeader[language][4],"10%");
        ctrlist.addHeader(textListHeader[language][1],"35%");
        ctrlist.addHeader(textListHeader[language][5],"35%");

	ctrlist.setLinkRow(1);
	ctrlist.setLinkPrefix("javascript:cmdEdit('");
	ctrlist.setLinkSufix("')");	
	Vector lstData = ctrlist.getData();
	Vector lstLinkData = ctrlist.getLinkData();
	Vector rowx = new Vector(1,1);
	ctrlist.reset();
	int index = -1;
	if(start<0)
		start = 0;
        
        Vector val_locationid = new Vector(1, 1);
        Vector key_locationid = new Vector(1, 1);
        Vector listLocation = PstLocation.list(0, 0, "", "");
        for (int l = 0; l < listLocation.size(); l++) {
            Location loc = (Location) listLocation.get(l);
            val_locationid.add("" + loc.getOID() + "");
            key_locationid.add(loc.getCode() + " - " + loc.getName());
        }
	
	for(int i = 0; i < objectClass.size(); i++) 
	{
		Ksg matKsg = (Ksg)objectClass.get(i);
		rowx = new Vector();
		if(ksgId == matKsg.getOID())
			 index = i; 
                Location l = new Location();
                try {
                    l = PstLocation.fetchExc(matKsg.getLocationId());
                } catch (DBException dbe) {
                    
                }
		start = start + 1;	
		if(index == i && (iCommand == Command.EDIT || iCommand == Command.ASK))
		{				
			rowx.add(""+start);
			rowx.add("<input type=\"text\" size=\"30\" name=\""+frmObject.fieldNames[FrmKsg.FRM_FIELD_CODE] +"\" value=\""+matKsg.getCode()+"\" class=\"formElemen\"> * ");
                        rowx.add("<input type=\"text\" size=\"30\" name=\""+frmObject.fieldNames[FrmKsg.FRM_FIELD_NAME] +"\" value=\""+matKsg.getName()+"\" class=\"formElemen\"> * ");
                        rowx.add(ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_LOCATION_ID], null, ""+matKsg.getLocationId(), val_locationid, key_locationid, "tabindex=\"1\"", "formElemen"));
		}
		else
		{
			rowx.add("" + start);
                        rowx.add("<a href=\"javascript:cmdEdit('"+String.valueOf(matKsg.getOID())+"')\"><div align=\"left\">"+matKsg.getCode()+"</div></a>");
			rowx.add("<div align=\"left\">"+matKsg.getName()+"</div>");
                        rowx.add("<div align=\"left\">"+l.getCode() + " - " + l.getName()+"</div>");
		} 
		lstData.add(rowx);
	}
	
	rowx = new Vector();
	if(iCommand == Command.ADD || (iCommand == Command.SAVE && frmObject.errorSize() > 0))
	{ 
			rowx.add(""+(start+1));
			rowx.add("<input type=\"text\" size=\"30\" name=\""+frmObject.fieldNames[FrmKsg.FRM_FIELD_CODE] +"\" value=\""+objEntity.getCode()+"\" class=\"formElemen\"> * ");
                        rowx.add("<input type=\"text\" size=\"30\" name=\""+frmObject.fieldNames[FrmKsg.FRM_FIELD_NAME] +"\" value=\""+objEntity.getName()+"\" class=\"formElemen\"> * ");
                        rowx.add(ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_LOCATION_ID], null, null, val_locationid, key_locationid, "tabindex=\"1\"", "formElemen"));
			lstData.add(rowx);
	}
	
	return ctrlist.draw();
}
%>
<%
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int prevCommand = FRMQueryString.requestInt(request, "prev_command");
long oidKsg = FRMQueryString.requestLong(request, "hidden_ksg_id");
String ksgTitle = "KSG";//com.dimata.posbo.jsp.JspInfo.txtMaterialInfo[SESS_LANGUAGE][com.dimata.posbo.jsp.JspInfo.MATERIAL_MERK];

/*variable declaration*/
int recordToGet = 20;
String msgString = "";
int iErrCode = FRMMessage.NONE;
String whereClause = "";
String orderClause = "";

CtrlKsg ctrlKsg = new CtrlKsg(request);
ControlLine ctrLine = new ControlLine();
Vector listKsg = new Vector(1,1);

/*switch statement */
iErrCode = ctrlKsg.action(iCommand , oidKsg);
/* end switch*/
FrmKsg frmKsg = ctrlKsg.getForm();

/*count list All Ksg*/
int vectSize = PstKsg.getCount(whereClause);

/*switch list Ksg*/
if((iCommand == Command.FIRST || iCommand == Command.PREV )||
  (iCommand == Command.NEXT || iCommand == Command.LAST))
  {
		start = ctrlKsg.actionList(iCommand, start, vectSize, recordToGet);
  } 
/* end switch list*/

Ksg matKsg = ctrlKsg.getKsg();
msgString =  ctrlKsg.getMessage();

/* get record to display */
    orderClause = PstKsg.fieldNames[PstKsg.FLD_CODE];
listKsg = PstKsg.list(start,recordToGet, whereClause , orderClause);

/*handle condition if size of record to display = 0 and start > 0 	after delete*/
if (listKsg.size() < 1 && start > 0)
{
	 if (vectSize - recordToGet > recordToGet)
			start = start - recordToGet;   //go to Command.PREV
	 else
	 {
		 start = 0 ;
		 iCommand = Command.FIRST;
		 prevCommand = Command.FIRST; //go to Command.FIRST
	 }
	 listKsg = PstKsg.list(start,recordToGet, whereClause , orderClause);
}
    
    String ksgName = "KSG"; //PstSystemProperty.getValueByName("NAME_OF_MERK");
    ksgTitle = ksgName; //textListHeader[SESS_LANGUAGE][2];
%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">


function cmdAdd()
{
	document.frmmatksg.hidden_ksg_id.value="0";
	document.frmmatksg.command.value="<%=Command.ADD%>";
	document.frmmatksg.prev_command.value="<%=prevCommand%>";
	document.frmmatksg.action="matksg.jsp";
	document.frmmatksg.submit();
}

function cmdAsk(oidKsg)
{
	document.frmmatksg.hidden_ksg_id.value=oidKsg;
	document.frmmatksg.command.value="<%=Command.ASK%>";
	document.frmmatksg.prev_command.value="<%=prevCommand%>";
	document.frmmatksg.action="matksg.jsp";
	document.frmmatksg.submit();
}

function cmdConfirmDelete(oidKsg)
{
	document.frmmatksg.hidden_ksg_id.value=oidKsg;
	document.frmmatksg.command.value="<%=Command.DELETE%>";
	document.frmmatksg.prev_command.value="<%=prevCommand%>";
	document.frmmatksg.action="matksg.jsp";
	document.frmmatksg.submit();
}

function cmdSave()
{
	document.frmmatksg.command.value="<%=Command.SAVE%>";
	document.frmmatksg.prev_command.value="<%=prevCommand%>";
	document.frmmatksg.action="matksg.jsp";
	document.frmmatksg.submit();
}

function cmdEdit(oidKsg)
{
	document.frmmatksg.hidden_ksg_id.value=oidKsg;
	document.frmmatksg.command.value="<%=Command.EDIT%>";
	document.frmmatksg.prev_command.value="<%=prevCommand%>";
	document.frmmatksg.action="matksg.jsp";
	document.frmmatksg.submit();
}

function cmdCancel(oidKsg)
{
	document.frmmatksg.hidden_ksg_id.value=oidKsg;
	document.frmmatksg.command.value="<%=Command.EDIT%>";
	document.frmmatksg.prev_command.value="<%=prevCommand%>";
	document.frmmatksg.action="matksg.jsp";
	document.frmmatksg.submit();
}

function cmdBack()
{
	document.frmmatksg.command.value="<%=Command.BACK%>";
	document.frmmatksg.action="matksg.jsp";
	document.frmmatksg.submit();
}

function cmdListFirst()
{
	document.frmmatksg.command.value="<%=Command.FIRST%>";
	document.frmmatksg.prev_command.value="<%=Command.FIRST%>";
	document.frmmatksg.action="matksg.jsp";
	document.frmmatksg.submit();
}

function cmdListPrev()
{
	document.frmmatksg.command.value="<%=Command.PREV%>";
	document.frmmatksg.prev_command.value="<%=Command.PREV%>";
	document.frmmatksg.action="matksg.jsp";
	document.frmmatksg.submit();
}

function cmdListNext()
{
	document.frmmatksg.command.value="<%=Command.NEXT%>";
	document.frmmatksg.prev_command.value="<%=Command.NEXT%>";   
	document.frmmatksg.action="matksg.jsp";
	document.frmmatksg.submit();
}

function cmdListLast()
{
	document.frmmatksg.command.value="<%=Command.LAST%>";
	document.frmmatksg.prev_command.value="<%=Command.LAST%>";
	document.frmmatksg.action="matksg.jsp";
	document.frmmatksg.submit();
}

//-------------- script form image -------------------

function cmdDelPict(oidKsg)
{
	document.frmimage.hidden_ksg_id.value=oidKsg;
	document.frmimage.command.value="<%=Command.POST%>";
	document.frmimage.action="matksg.jsp";
	document.frmimage.submit();
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
            Masterdata &gt; <%=ksgName%><%///=textListHeader[SESS_LANGUAGE][2]%><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frmmatksg" method ="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="vectSize" value="<%=vectSize%>">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="hidden_ksg_id" value="<%=oidKsg%>">
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
                        <td height="14" valign="middle" colspan="3" class="comment">&nbsp;<u><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar "+ksgName : textListHeader[SESS_LANGUAGE][3]%></u></td>
						</tr>
                      <%
							try{
							%>
                      <tr align="left" valign="top"> 
                        <td height="22" valign="middle" colspan="3"> <%=drawList(SESS_LANGUAGE,iCommand,frmKsg, matKsg,listKsg,oidKsg,start)%> </td>
                      </tr>
                      <% 
						  }catch(Exception exc){ 
						  }%>
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
                              <!--td width="18%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,ksgTitle,ctrLine.CMD_ADD,true)%>"></a></td-->
                              <td nowrap width="82%"><a href="javascript:cmdAdd()" class="btn-lg btn-primary"><i class="fa fa-plus-circle"></i> <%=ctrLine.getCommand(SESS_LANGUAGE,ksgTitle,ctrLine.CMD_ADD,true)%></a></td>
		                      <%}%>							  
                            </tr>
                          </table>
	                      <%}%>					  					  						  
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>
                <tr align="left" valign="top" > 
                  <td colspan="3" class="command"> 
                    <%
									ctrLine.setLocationImg(approot+"/images");
									
									// set image alternative caption
									ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,ksgTitle,ctrLine.CMD_SAVE,true));
									ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,ksgTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,ksgTitle,ctrLine.CMD_BACK,true)+" List");							
									ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,ksgTitle,ctrLine.CMD_ASK,true));							
									ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,ksgTitle,ctrLine.CMD_CANCEL,false));														
									
									ctrLine.initDefault();
									ctrLine.setTableWidth("80%");
									String scomDel = "javascript:cmdAsk('"+oidKsg+"')";
									String sconDelCom = "javascript:cmdConfirmDelete('"+oidKsg+"')";
									String scancel = "javascript:cmdEdit('"+oidKsg+"')";
									ctrLine.setCommandStyle("command");
									ctrLine.setColCommStyle("command");
									
									// set command caption
									ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE,ksgTitle,ctrLine.CMD_SAVE,true));
									ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,ksgTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,ksgTitle,ctrLine.CMD_BACK,true)+" List");							
									ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE,ksgTitle,ctrLine.CMD_ASK,true));							
									ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE,ksgTitle,ctrLine.CMD_DELETE,true));														
									ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE,ksgTitle,ctrLine.CMD_CANCEL,false));									

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
									%>
                    <%if(iCommand==Command.ADD || iCommand==Command.EDIT || iCommand==Command.ASK || ((iCommand==Command.SAVE || iCommand==Command.DELETE)&&iErrCode!=FRMMessage.NONE)){%>
                    <%= ctrLine.drawImage(iCommand, iErrCode, msgString)%><%}%></td>
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

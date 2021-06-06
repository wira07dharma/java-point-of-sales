<%@ page language = "java" %>
<%@ page import = "java.util.*,
                   com.dimata.posbo.form.admin.service.FrmBackUpService" %>
<%@ page import = "com.dimata.util.*" %>
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<%@ page import = "com.dimata.posbo.entity.masterdata.*" %>
<%@ page import = "com.dimata.posbo.form.masterdata.*" %>

<%@ include file = "../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_SHIFT); %>
<%@ include file = "../main/checkuser.jsp" %>



<!-- Jsp Block -->
<%!
/* this constant used to list text of listHeader */
public static final String textListHeader[][] = 
{
	{"No","Nama","Dari Jam","Sampai Jam","Keterangan"},
	{"No","Name","From Time","To Time","Description"}
};

/* this method used to list material unit */
public String drawList(int language,int iCommand,FrmShift frmObject,Shift objEntity,Vector objectClass,long unitId,int start)
{
	ControlList ctrlist = new ControlList();
	ctrlist.setAreaWidth("60%");
	ctrlist.setListStyle("listgen");
	ctrlist.setTitleStyle("listgentitle");
	ctrlist.setCellStyle("listgensell");
	ctrlist.setHeaderStyle("listgentitle");
	ctrlist.addHeader(textListHeader[language][0],"5%");
	ctrlist.addHeader(textListHeader[language][1],"20%");
        ctrlist.addHeader(textListHeader[language][2],"20%");
        ctrlist.addHeader(textListHeader[language][3],"20%");
        ctrlist.addHeader(textListHeader[language][4],"45%");

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
	
	for(int i = 0; i < objectClass.size(); i++){
		Shift matShift = (Shift)objectClass.get(i);
		rowx = new Vector();
		if(unitId == matShift.getOID())
			 index = i; 
			
		start = start + 1;	
		if(index == i && (iCommand == Command.EDIT || iCommand == Command.ASK)){
			rowx.add(""+start);
			rowx.add("<input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[FrmShift.FRM_FIELD_NAME] +"\" value=\""+matShift.getName()+"\" class=\"formElemen\">");
            Date dt=null;
            Date endDt = null;
            try{
                dt = matShift.getStartTime();
                endDt = matShift.getEndTime();
                if(dt==null){
                    dt = new Date();
                }
                if(endDt==null){
                    endDt = new Date();
                }
            }catch(Exception e){
                dt = new Date();
            }
            rowx.add(ControlDate.drawTime(frmObject.fieldNames[FrmShift.FRM_FIELD_START_TIME], dt, "formElemen",24,1,0));
            rowx.add(ControlDate.drawTime(frmObject.fieldNames[FrmShift.FRM_FIELD_END_TIME], endDt, "formElemen",24,1,0));
			rowx.add("<input type=\"text\" size=\"20\" name=\""+frmObject.fieldNames[FrmShift.FRM_FIELD_REMARK] +"\" value=\""+matShift.getRemark()+"\" class=\"formElemen\">");
		}else{
			rowx.add("" + start);
			rowx.add("<a href=\"javascript:cmdEdit('"+String.valueOf(matShift.getOID())+"')\"><div align=\"left\">"+matShift.getName()+"</div></a>");
            rowx.add(Formater.formatDate(matShift.getStartTime(),"HH:mm"));
            rowx.add(Formater.formatDate(matShift.getEndTime(),"HH:mm"));
			rowx.add(matShift.getRemark());
		} 
		lstData.add(rowx);
	}
	
	rowx = new Vector();
	if(iCommand == Command.ADD || (iCommand == Command.SAVE && frmObject.errorSize() > 0)){
            Date dt= new Date();
			rowx.add(""+(start+1));
			rowx.add("<input type=\"text\" size=\"10\" name=\""+frmObject.fieldNames[FrmShift.FRM_FIELD_NAME] +"\" value=\""+objEntity.getName()+"\" class=\"formElemen\">");
            rowx.add(ControlDate.drawTime(frmObject.fieldNames[FrmShift.FRM_FIELD_START_TIME], dt, "formElemen"));
            rowx.add(ControlDate.drawTime(frmObject.fieldNames[FrmShift.FRM_FIELD_END_TIME], dt, "formElemen"));
			rowx.add("<input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[FrmShift.FRM_FIELD_REMARK] +"\" value=\""+objEntity.getRemark()+"\" class=\"formElemen\">");
			lstData.add(rowx);
	}
	
	return ctrlist.draw();
}
%>
<%
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int prevCommand = FRMQueryString.requestInt(request, "prev_command");
long oidShift = FRMQueryString.requestLong(request, "hidden_merk_id");
String merkTitle = com.dimata.posbo.jsp.JspInfo.txtMaterialInfo[SESS_LANGUAGE][com.dimata.posbo.jsp.JspInfo.MATERIAL_SHIFT];

/*variable declaration*/
int recordToGet = 10;
String msgString = "";
int iErrCode = FRMMessage.NONE;
String whereClause = "";
String orderClause = "";

CtrlShift ctrlShift = new CtrlShift(request);
ControlLine ctrLine = new ControlLine();
Vector listShift = new Vector(1,1);

/*switch statement */
iErrCode = ctrlShift.action(iCommand , oidShift, request);
/* end switch*/
FrmShift frmShift = ctrlShift.getForm();

/*count list All Shift*/
int vectSize = PstShift.getCount(whereClause);

/*switch list Shift*/
if((iCommand == Command.FIRST || iCommand == Command.PREV )||
  (iCommand == Command.NEXT || iCommand == Command.LAST)){
		start = ctrlShift.actionList(iCommand, start, vectSize, recordToGet);
  } 
/* end switch list*/

Shift matShift = ctrlShift.getShift();
msgString =  ctrlShift.getMessage();

/* get record to display */
listShift = PstShift.list(start,recordToGet, whereClause , orderClause);

/*handle condition if size of record to display = 0 and start > 0 	after delete*/
if (listShift.size() < 1 && start > 0)
{
	 if (vectSize - recordToGet > recordToGet)
			start = start - recordToGet;   //go to Command.PREV
	 else
	 {
		 start = 0 ;
		 iCommand = Command.FIRST;
		 prevCommand = Command.FIRST; //go to Command.FIRST
	 }
	 listShift = PstShift.list(start,recordToGet, whereClause , orderClause);
}
%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">

function cmdAdd()
{
	document.frmmatshift.hidden_merk_id.value="0";
	document.frmmatshift.command.value="<%=Command.ADD%>";
	document.frmmatshift.prev_command.value="<%=prevCommand%>";
	document.frmmatshift.action="shift.jsp";
	document.frmmatshift.submit();
}

function cmdAsk(oidShift)
{
	document.frmmatshift.hidden_merk_id.value=oidShift;
	document.frmmatshift.command.value="<%=Command.ASK%>";
	document.frmmatshift.prev_command.value="<%=prevCommand%>";
	document.frmmatshift.action="shift.jsp";
	document.frmmatshift.submit();
}

function cmdConfirmDelete(oidShift)
{
	document.frmmatshift.hidden_merk_id.value=oidShift;
	document.frmmatshift.command.value="<%=Command.DELETE%>";
	document.frmmatshift.prev_command.value="<%=prevCommand%>";
	document.frmmatshift.action="shift.jsp";
	document.frmmatshift.submit();
}

function cmdSave()
{
	document.frmmatshift.command.value="<%=Command.SAVE%>";
	document.frmmatshift.prev_command.value="<%=prevCommand%>";
	document.frmmatshift.action="shift.jsp";
	document.frmmatshift.submit();
}

function cmdEdit(oidShift)
{
	document.frmmatshift.hidden_merk_id.value=oidShift;
	document.frmmatshift.command.value="<%=Command.EDIT%>";
	document.frmmatshift.prev_command.value="<%=prevCommand%>";
	document.frmmatshift.action="shift.jsp";
	document.frmmatshift.submit();
}

function cmdCancel(oidShift)
{
	document.frmmatshift.hidden_merk_id.value=oidShift;
	document.frmmatshift.command.value="<%=Command.EDIT%>";
	document.frmmatshift.prev_command.value="<%=prevCommand%>";
	document.frmmatshift.action="shift.jsp";
	document.frmmatshift.submit();
}

function cmdBack()
{
	document.frmmatshift.command.value="<%=Command.BACK%>";
	document.frmmatshift.action="shift.jsp";
	document.frmmatshift.submit();
}

function cmdListFirst()
{
	document.frmmatshift.command.value="<%=Command.FIRST%>";
	document.frmmatshift.prev_command.value="<%=Command.FIRST%>";
	document.frmmatshift.action="shift.jsp";
	document.frmmatshift.submit();
}

function cmdListPrev()
{
	document.frmmatshift.command.value="<%=Command.PREV%>";
	document.frmmatshift.prev_command.value="<%=Command.PREV%>";
	document.frmmatshift.action="shift.jsp";
	document.frmmatshift.submit();
}

function cmdListNext()
{
	document.frmmatshift.command.value="<%=Command.NEXT%>";
	document.frmmatshift.prev_command.value="<%=Command.NEXT%>";
	document.frmmatshift.action="shift.jsp";
	document.frmmatshift.submit();
}

function cmdListLast()
{
	document.frmmatshift.command.value="<%=Command.LAST%>";
	document.frmmatshift.prev_command.value="<%=Command.LAST%>";
	document.frmmatshift.action="shift.jsp";
	document.frmmatshift.submit();
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
    <link href="../stylesheets/general_home_style.css" type="text/css"
rel="stylesheet" />
<%}%>
<link rel="stylesheet" href="../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" --> 
<link rel="stylesheet" href="../styles/tab.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "headerscript" --> 
<!-- #EndEditable --> 
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
  <%if(menuUsed == MENU_PER_TRANS){%>
  <tr>
    <td height="25" ID="TOPTITLE"> <!-- #BeginEditable "header" -->
      <%@ include file = "../main/header.jsp" %>
      <!-- #EndEditable --></td>
  </tr>
  <tr>
    <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
      <%@ include file = "../main/mnmain.jsp" %>
      <!-- #EndEditable --> </td>
  </tr>
  <%}else{%>
   <tr bgcolor="#FFFFFF">
    <td height="10" ID="MAINMENU">
      <%@include file="../styletemplate/template_header.jsp" %>
    </td>
  </tr>
  <%}%>
  <tr> 
    <td valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">  
        <tr> 
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" --> 
            Masterdata &gt; <%=merkTitle%><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frmmatshift" method ="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="vectSize" value="<%=vectSize%>">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="hidden_merk_id" value="<%=oidShift%>">
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
                        <td height="14" valign="middle" colspan="3" class="comment">&nbsp;<u><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar "+merkTitle : merkTitle+" List"%></u></td>
						</tr>
                      <%
							try{
							%>
                      <tr align="left" valign="top"> 
                        <td height="22" valign="middle" colspan="3"> <%=drawList(SESS_LANGUAGE,iCommand,frmShift, matShift,listShift,oidShift,start)%> </td>
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
                              <!--td width="18%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,merkTitle,ctrLine.CMD_ADD,true)%>"></a></td-->
                                      <br><td nowrap width="82%"><a class="btn btn-lg btn-primary" href="javascript:cmdAdd()" class="command"><i class="fa fa-plus-circle"></i> <%=ctrLine.getCommand(SESS_LANGUAGE,merkTitle,ctrLine.CMD_ADD,true)%></a></td>
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
					ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,merkTitle,ctrLine.CMD_SAVE,true));
					ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,merkTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,merkTitle,ctrLine.CMD_BACK,true)+" List");							
					ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,merkTitle,ctrLine.CMD_ASK,true));							
					ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,merkTitle,ctrLine.CMD_CANCEL,false));														
					
					ctrLine.initDefault();
					ctrLine.setTableWidth("80%");
					String scomDel = "javascript:cmdAsk('"+oidShift+"')";
					String sconDelCom = "javascript:cmdConfirmDelete('"+oidShift+"')";
					String scancel = "javascript:cmdEdit('"+oidShift+"')";
					ctrLine.setCommandStyle("command");
					ctrLine.setColCommStyle("command");
					
					// set command caption
					ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE,merkTitle,ctrLine.CMD_SAVE,true));
					ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,merkTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,merkTitle,ctrLine.CMD_BACK,true)+" List");							
					ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE,merkTitle,ctrLine.CMD_ASK,true));							
					ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE,merkTitle,ctrLine.CMD_DELETE,true));														
					ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE,merkTitle,ctrLine.CMD_CANCEL,false));									

					if (privDelete){
						ctrLine.setConfirmDelCommand(sconDelCom);
						ctrLine.setDeleteCommand(scomDel);
						ctrLine.setEditCommand(scancel);
					}else{ 
						ctrLine.setConfirmDelCaption("");
						ctrLine.setDeleteCaption("");
						ctrLine.setEditCaption("");
					}

					if(iCommand == Command.EDIT  && privUpdate == false){
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
			<%
			if(iCommand==Command.ADD || iCommand==Command.EDIT)
			{
			%>
			<script language="javascript">
				document.frmmatshift.<%=FrmShift.fieldNames[FrmShift.FRM_FIELD_NAME]%>.focus();
			</script>
			<%
			}
			%>						
            <!-- #EndEditable --></td> 
        </tr> 
      </table>
    </td>
  </tr>
  <tr> 
    <td colspan="2" height="20"> <!-- #BeginEditable "footer" --> 
       <%if(menuUsed == MENU_ICON){%>
            <%@include file="../styletemplate/footer.jsp" %>

        <%}else{%>
            <%@ include file = "../main/footer.jsp" %>
        <%}%>
      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>

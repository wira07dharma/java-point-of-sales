<% 
/* 
 * Page Name  		:  coderange.jsp
 * Created on 		:  [date] [time] AM/PM 
 * 
 * @author  		:  [authorName] 
 * @version  		:  [version] 
 */

/*******************************************************************
 * Page Description 	: [project description ... ]
 * Imput Parameters 	: [input parameter ...] 
 * Output 			: [output ...] 
 *******************************************************************/
%>
<%@ page language = "java" %>
<%@ page import = "java.util.*"%>
<%@ page import = "com.dimata.util.*" %>
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<%@ page import = "com.dimata.posbo.entity.masterdata.*" %>
<%@ page import = "com.dimata.posbo.form.masterdata.*" %>
<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_GROUP_CODE); %>
<%@ include file = "../../main/checkuser.jsp" %>

<!-- Jsp Block -->
<%!
	
	public static final String textListTitle[][] =
    {
        {"Kode Grup"},
        {"Code Range"}
    };
	
	public static final String textListHeader[][] =
    {
        {"Dari Kode","Sampai Kode"},
        {"From Code","To Code"}
    };
	
	public String drawList(int iCommand,com.dimata.posbo.form.masterdata.FrmCodeRange frmObject,
                           CodeRange objEntity, Vector objectClass,
                           long discountTypeId, int languange)

	{
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
		ctrlist.setAreaWidth("50%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		ctrlist.addHeader(textListHeader[languange][0],"30%");
		ctrlist.addHeader(textListHeader[languange][1],"30%");

		ctrlist.setLinkRow(0);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		Vector rowx = new Vector(1,1);
		ctrlist.reset();
		int index = -1;
		for (int i = 0; i < objectClass.size(); i++) {
			 CodeRange codeRange = (CodeRange)objectClass.get(i);
			 rowx = new Vector();
			 if(discountTypeId == codeRange.getOID())
				 index = i; 

			 if(index == i && (iCommand == Command.EDIT || iCommand == Command.ASK)){
				rowx.add("<input type=\"text\" name=\""+frmObject.fieldNames[com.dimata.posbo.form.masterdata.FrmCodeRange.FRM_FIELD_FROM_CODE] +"\" value=\""+codeRange.getFromRangeCode()+"\" class=\"formElemen\">");
				rowx.add("<input type=\"text\" name=\""+frmObject.fieldNames[com.dimata.posbo.form.masterdata.FrmCodeRange.FRM_FIELD_TO_CODE] +"\" value=\""+codeRange.getToRangeCode()+"\" class=\"formElemen\">");
			}else{
				rowx.add("<a href=\"javascript:cmdEdit('"+String.valueOf(codeRange.getOID())+"')\">"+codeRange.getFromRangeCode()+"</a>");
				rowx.add(codeRange.getToRangeCode());
			}
			lstData.add(rowx);
		}

		 rowx = new Vector();

		if(iCommand == Command.ADD || (iCommand == Command.SAVE && frmObject.errorSize() > 0)){ 
            rowx.add("<input type=\"text\" name=\""+frmObject.fieldNames[com.dimata.posbo.form.masterdata.FrmCodeRange.FRM_FIELD_FROM_CODE] +"\" value=\""+objEntity.getFromRangeCode()+"\" class=\"formElemen\">");
            rowx.add("<input type=\"text\" name=\""+frmObject.fieldNames[com.dimata.posbo.form.masterdata.FrmCodeRange.FRM_FIELD_TO_CODE] +"\" value=\""+objEntity.getToRangeCode()+"\" class=\"formElemen\">");
			lstData.add(rowx);
		}
		
		return ctrlist.draw();
	}

%>
<%
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int prevCommand = FRMQueryString.requestInt(request, "prev_command");
long oidCodeRange = FRMQueryString.requestLong(request, "hidden_code_range_id");

/*variable declaration*/
int recordToGet = 10;
String msgString = "";
int iErrCode = FRMMessage.NONE;
String whereClause = "";
String orderClause = PstCodeRange.fieldNames[PstCodeRange.FLD_FROM_RANGE_CODE];
boolean privManageData = true; 

CtrlCodeRange ctrlCodeRange = new CtrlCodeRange(request);
ControlLine ctrLine = new ControlLine();
Vector listCodeRange = new Vector(1,1);

/*switch statement */
iErrCode = ctrlCodeRange.action(iCommand , oidCodeRange);
/* end switch*/
com.dimata.posbo.form.masterdata.FrmCodeRange frmCodeRange = ctrlCodeRange.getForm();

/*count list All CodeRange*/
int vectSize = PstCodeRange.getCount(whereClause);

/*switch list CodeRange*/
if((iCommand == Command.FIRST || iCommand == Command.PREV )||
  (iCommand == Command.NEXT || iCommand == Command.LAST)){
		start = ctrlCodeRange.actionList(iCommand, start, vectSize, recordToGet);
 } 
/* end switch list*/

CodeRange codeRange = ctrlCodeRange.getCodeRange();
msgString =  ctrlCodeRange.getMessage();

/* get record to display */
listCodeRange = PstCodeRange.list(start,recordToGet, whereClause , orderClause);

/*handle condition if size of record to display = 0 and start > 0 	after delete*/
if (listCodeRange.size() < 1 && start > 0){
	 if (vectSize - recordToGet > recordToGet)
			start = start - recordToGet;   //go to Command.PREV
	 else{
		 start = 0 ;
		 iCommand = Command.FIRST;
		 prevCommand = Command.FIRST; //go to Command.FIRST
	 }
	 listCodeRange = PstCodeRange.list(start,recordToGet, whereClause , orderClause);
}
%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">

function cmdAdd(){
	document.frmcoderange.hidden_code_range_id.value="0";
	document.frmcoderange.command.value="<%=Command.ADD%>";
	document.frmcoderange.prev_command.value="<%=prevCommand%>";
	document.frmcoderange.action="coderange.jsp";
	document.frmcoderange.submit();
}

function cmdAsk(oidCodeRange){
	document.frmcoderange.hidden_code_range_id.value=oidCodeRange;
	document.frmcoderange.command.value="<%=Command.ASK%>";
	document.frmcoderange.prev_command.value="<%=prevCommand%>";
	document.frmcoderange.action="coderange.jsp";
	document.frmcoderange.submit();
}

function cmdConfirmDelete(oidCodeRange){
	document.frmcoderange.hidden_code_range_id.value=oidCodeRange;
	document.frmcoderange.command.value="<%=Command.DELETE%>";
	document.frmcoderange.prev_command.value="<%=prevCommand%>";
	document.frmcoderange.action="coderange.jsp";
	document.frmcoderange.submit();
}

function cmdSave(){
	document.frmcoderange.command.value="<%=Command.SAVE%>";
	document.frmcoderange.prev_command.value="<%=prevCommand%>";
	document.frmcoderange.action="coderange.jsp";
	document.frmcoderange.submit();
}

function cmdEdit(oidCodeRange){
	document.frmcoderange.hidden_code_range_id.value=oidCodeRange;
	document.frmcoderange.command.value="<%=Command.EDIT%>";
	document.frmcoderange.prev_command.value="<%=prevCommand%>";
	document.frmcoderange.action="coderange.jsp";
	document.frmcoderange.submit();
}

function cmdCancel(oidCodeRange){
	document.frmcoderange.hidden_code_range_id.value=oidCodeRange;
	document.frmcoderange.command.value="<%=Command.EDIT%>";
	document.frmcoderange.prev_command.value="<%=prevCommand%>";
	document.frmcoderange.action="coderange.jsp";
	document.frmcoderange.submit();
}

function cmdBack(){
	document.frmcoderange.command.value="<%=Command.BACK%>";
	document.frmcoderange.action="coderange.jsp";
	document.frmcoderange.submit();
}

function cmdListFirst(){
	document.frmcoderange.command.value="<%=Command.FIRST%>";
	document.frmcoderange.prev_command.value="<%=Command.FIRST%>";
	document.frmcoderange.action="coderange.jsp";
	document.frmcoderange.submit();
}

function cmdListPrev(){
	document.frmcoderange.command.value="<%=Command.PREV%>";
	document.frmcoderange.prev_command.value="<%=Command.PREV%>";
	document.frmcoderange.action="coderange.jsp";
	document.frmcoderange.submit();
}

function cmdListNext(){
	document.frmcoderange.command.value="<%=Command.NEXT%>";
	document.frmcoderange.prev_command.value="<%=Command.NEXT%>";
	document.frmcoderange.action="coderange.jsp";
	document.frmcoderange.submit();
}

function cmdListLast(){
	document.frmcoderange.command.value="<%=Command.LAST%>";
	document.frmcoderange.prev_command.value="<%=Command.LAST%>";
	document.frmcoderange.action="coderange.jsp";
	document.frmcoderange.submit();
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
<!-- #EndEditable --> 
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
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
  <tr> 
    <td valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">  
        <tr> 
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" --> 
            Master Data &gt; <%=textListTitle[SESS_LANGUAGE][0]%><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frmcoderange" method ="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="vectSize" value="<%=vectSize%>">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="hidden_code_range_id" value="<%=oidCodeRange%>">
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr align="left" valign="top"> 
                  <td height="8"  colspan="3"> 
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr align="left" valign="top"> 
                        <td height="8" valign="middle" colspan="3"> 
                          <hr>
                        </td>
                      </tr>
                      <tr align="left" valign="top"> 
                        <td height="14" valign="middle" colspan="3" class="comment"><%=textListTitle[SESS_LANGUAGE][0]%></td>
                      </tr>
                      <%
							try{
							%>
                      <tr align="left" valign="top"> 
                        <td height="22" valign="middle" colspan="3"> <%= drawList(iCommand,frmCodeRange, codeRange,listCodeRange,oidCodeRange,SESS_LANGUAGE)%> </td>
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
                          <table width="20%" border="0" cellspacing="0" cellpadding="0">
                            <tr> 
                              <%if(privAdd && privManageData){%>
                              <%if(iCommand==Command.NONE||iCommand==Command.FIRST||iCommand==Command.PREV||iCommand==Command.NEXT||iCommand==Command.LAST||iCommand==Command.BACK|| (iCommand==Command.DELETE && iErrCode==0)){%>
                              <td width="19%" nowrap><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_ADD,true)%>"></a></td>
                              <td width="81%" nowrap class="command"><a href="javascript:cmdAdd()"><%=ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_ADD,true)%></a></td>
                              <%}}%>
                            </tr>
                          </table>
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="8" valign="middle" width="17%">&nbsp;</td>
                  <td height="8" colspan="2" width="83%">&nbsp;</td>
                </tr>
                <tr align="left" valign="top" > 
                  <td colspan="3" class="command"> 
                    <%
					ctrLine.setLocationImg(approot+"/images");
					ctrLine.initDefault(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0]);
					ctrLine.setTableWidth("80%");
					ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_SAVE,true));
					ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_BACK,true)+" List");
					ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_ASK,true));
					ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_CANCEL,false));
					
					String scomDel = "javascript:cmdAsk('"+oidCodeRange+"')";
					String sconDelCom = "javascript:cmdConfirmDelete('"+oidCodeRange+"')";
					String scancel = "javascript:cmdEdit('"+oidCodeRange+"')";
					//ctrLine.setBackCaption("Back to List");
					//ctrLine.setCommandStyle("buttonlink");
					
					ctrLine.setCommandStyle("command");
					ctrLine.setColCommStyle("command");
					
					// set command caption
					ctrLine.setAddCaption(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_ADD,true));
					ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_SAVE,true));
					ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_BACK,true)+" List");
					ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_ASK,true));
					ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_DELETE,true));
					ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_CANCEL,false));

					if (privDelete){
						ctrLine.setConfirmDelCommand(sconDelCom);
						ctrLine.setDeleteCommand(scomDel);
						ctrLine.setEditCommand(scancel);
					}else{ 
						ctrLine.setConfirmDelCaption("");
						ctrLine.setDeleteCaption("");
						ctrLine.setEditCaption("");
					}

					if(privAdd == false  && privUpdate == false){
						ctrLine.setSaveCaption("");
					}

					if (privAdd == false){
						ctrLine.setAddCaption("");
					}
					if(iCommand==Command.DELETE){
						ctrLine.setAddCaption("");
						//ctrLine.setBackCaption("");
					}
					%>
                    <%= ctrLine.drawImage(iCommand, iErrCode, msgString)%> </td>
                </tr>
              </table>
            </form>
			<%
			if(iCommand==Command.ADD || iCommand==Command.EDIT)
			{
			%>
			<script language="javascript">
				document.frmcoderange.<%=com.dimata.posbo.form.masterdata.FrmCodeRange.fieldNames[com.dimata.posbo.form.masterdata.FrmCodeRange.FRM_FIELD_FROM_CODE]%>.focus();
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
      <%@ include file = "../../main/footer.jsp" %>
      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>

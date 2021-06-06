<% 
/* 
 * Page Name  		:  discounttype.jsp
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
<!-- package java -->
<%@ page import = "java.util.*,
                   com.dimata.common.entity.payment.DiscountType,
                   com.dimata.common.form.payment.FrmDiscountType,
                   com.dimata.common.form.payment.CtrlDiscountType,
                   com.dimata.common.entity.payment.PstDiscountType" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package posbo -->
<%@ page import = "com.dimata.posbo.entity.masterdata.*" %>
<%@ page import = "com.dimata.posbo.form.masterdata.*" %>
<%//@ page import = "com.dimata.posbo.entity.admin.*" %>

<%@ include file = "../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_DISCOUNT_TYPE); %>
<%@ include file = "../main/checkuser.jsp" %>

<!-- Jsp Block -->
<%!
	
	public static final String textListTitle[][] =
    {
        {"Tipe Potongan","Harus diisi"},
        {"Discount Type","required"}
    };
	
	public static final String textListHeader[][] =
    {
        {"Kode","Nama","Urutan"},
        {"Code","Name","Index"}
    };
	
	public String drawList(int iCommand,FrmDiscountType frmObject, DiscountType objEntity, Vector objectClass,  long discountTypeId, int languange)

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
        ctrlist.addHeader(textListHeader[languange][2],"20%");

		ctrlist.setLinkRow(0);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		Vector rowx = new Vector(1,1);
		ctrlist.reset();
		int index = -1;

        /**
         * untuk index
         */
        Vector index_value = new Vector(1,1);
		Vector index_key = new Vector(1,1);
        //for(int i=1;i<10;i++){
         for(int i=1;i<21;i++){
            index_key.add(""+i);
            index_value.add(String.valueOf(i));
        }
		for (int i = 0; i < objectClass.size(); i++) {
			 DiscountType discountType = (DiscountType)objectClass.get(i);
			 rowx = new Vector();
			 if(discountTypeId == discountType.getOID())
				 index = i; 

			 if(index == i && (iCommand == Command.EDIT || iCommand == Command.ASK)){
				rowx.add("<input type=\"text\" name=\""+frmObject.fieldNames[FrmDiscountType.FRM_FIELD_CODE] +"\" value=\""+discountType.getCode()+"\" class=\"formElemen\">");
				rowx.add("<input type=\"text\" name=\""+frmObject.fieldNames[FrmDiscountType.FRM_FIELD_NAME] +"\" value=\""+discountType.getName()+"\" class=\"formElemen\">");
                rowx.add("<div align=\"center\">"+ControlCombo.draw(frmObject.fieldNames[FrmDiscountType.FRM_FIELD_INDEX], null, ""+discountType.getPindex(), index_key, index_value,"","formElemen")+"</div>");
			}else{
				rowx.add("<a href=\"javascript:cmdEdit('"+String.valueOf(discountType.getOID())+"')\">"+discountType.getCode()+"</a>");
				rowx.add(discountType.getName());
                 rowx.add("<div align=\"center\">"+discountType.getPindex()+"</div>");
			}

			lstData.add(rowx);
		}

		 rowx = new Vector();

		if(iCommand == Command.ADD || (iCommand == Command.SAVE && frmObject.errorSize() > 0)){ 
				rowx.add("<input type=\"text\" name=\""+frmObject.fieldNames[FrmDiscountType.FRM_FIELD_CODE] +"\" value=\""+objEntity.getCode()+"\" class=\"formElemen\">");
				rowx.add("<input type=\"text\" name=\""+frmObject.fieldNames[FrmDiscountType.FRM_FIELD_NAME] +"\" value=\""+objEntity.getName()+"\" class=\"formElemen\">");
                rowx.add("<div align=\"center\">"+ControlCombo.draw(frmObject.fieldNames[FrmDiscountType.FRM_FIELD_INDEX], null, ""+0, index_key, index_value,"","formElemen")+"</div>");
				lstData.add(rowx);
		}

		return ctrlist.draw();
	}

%>
<%
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int prevCommand = FRMQueryString.requestInt(request, "prev_command");
long oidDiscountType = FRMQueryString.requestLong(request, "hidden_discount_type_id");

/*variable declaration*/
int recordToGet = 10;
String msgString = "";
int iErrCode = FRMMessage.NONE;
String whereClause = "";
String orderClause = PstDiscountType.fieldNames[PstDiscountType.FLD_INDEX];
boolean privManageData = true; 

CtrlDiscountType ctrlDiscountType = new CtrlDiscountType(request);
ControlLine ctrLine = new ControlLine();
Vector listDiscountType = new Vector(1,1);

/*switch statement */
iErrCode = ctrlDiscountType.action(iCommand , oidDiscountType);
/* end switch*/
FrmDiscountType frmDiscountType = ctrlDiscountType.getForm();

/*count list All DiscountType*/
int vectSize = PstDiscountType.getCount(whereClause);

/*switch list DiscountType*/
if((iCommand == Command.FIRST || iCommand == Command.PREV )||
  (iCommand == Command.NEXT || iCommand == Command.LAST)){
		start = ctrlDiscountType.actionList(iCommand, start, vectSize, recordToGet);
 } 
/* end switch list*/

DiscountType discountType = ctrlDiscountType.getDiscountType();
msgString =  ctrlDiscountType.getMessage();

/* get record to display */
listDiscountType = PstDiscountType.list(start,recordToGet, whereClause , orderClause);

/*handle condition if size of record to display = 0 and start > 0 	after delete*/
if (listDiscountType.size() < 1 && start > 0)
{
	 if (vectSize - recordToGet > recordToGet)
			start = start - recordToGet;   //go to Command.PREV
	 else{
		 start = 0 ;
		 iCommand = Command.FIRST;
		 prevCommand = Command.FIRST; //go to Command.FIRST
	 }
	 listDiscountType = PstDiscountType.list(start,recordToGet, whereClause , orderClause);
}
%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">


function cmdAdd(){
	document.frmdiscounttype.hidden_discount_type_id.value="0";
	document.frmdiscounttype.command.value="<%=Command.ADD%>";
	document.frmdiscounttype.prev_command.value="<%=prevCommand%>";
	document.frmdiscounttype.action="discounttype.jsp";
	document.frmdiscounttype.submit();
}

function cmdAsk(oidDiscountType){
	document.frmdiscounttype.hidden_discount_type_id.value=oidDiscountType;
	document.frmdiscounttype.command.value="<%=Command.ASK%>";
	document.frmdiscounttype.prev_command.value="<%=prevCommand%>";
	document.frmdiscounttype.action="discounttype.jsp";
	document.frmdiscounttype.submit();
}

function cmdConfirmDelete(oidDiscountType){
	document.frmdiscounttype.hidden_discount_type_id.value=oidDiscountType;
	document.frmdiscounttype.command.value="<%=Command.DELETE%>";
	document.frmdiscounttype.prev_command.value="<%=prevCommand%>";
	document.frmdiscounttype.action="discounttype.jsp";
	document.frmdiscounttype.submit();
}

function cmdSave(){
	document.frmdiscounttype.command.value="<%=Command.SAVE%>";
	document.frmdiscounttype.prev_command.value="<%=prevCommand%>";
	document.frmdiscounttype.action="discounttype.jsp";
	document.frmdiscounttype.submit();
}

function cmdEdit(oidDiscountType){
	document.frmdiscounttype.hidden_discount_type_id.value=oidDiscountType;
	document.frmdiscounttype.command.value="<%=Command.EDIT%>";
	document.frmdiscounttype.prev_command.value="<%=prevCommand%>";
	document.frmdiscounttype.action="discounttype.jsp";
	document.frmdiscounttype.submit();
}

function cmdCancel(oidDiscountType){
	document.frmdiscounttype.hidden_discount_type_id.value=oidDiscountType;
	document.frmdiscounttype.command.value="<%=Command.EDIT%>";
	document.frmdiscounttype.prev_command.value="<%=prevCommand%>";
	document.frmdiscounttype.action="discounttype.jsp";
	document.frmdiscounttype.submit();
}

function cmdBack(){
	document.frmdiscounttype.command.value="<%=Command.BACK%>";
	document.frmdiscounttype.action="discounttype.jsp";
	document.frmdiscounttype.submit();
}

function cmdListFirst(){
	document.frmdiscounttype.command.value="<%=Command.FIRST%>";
	document.frmdiscounttype.prev_command.value="<%=Command.FIRST%>";
	document.frmdiscounttype.action="discounttype.jsp";
	document.frmdiscounttype.submit();
}

function cmdListPrev(){
	document.frmdiscounttype.command.value="<%=Command.PREV%>";
	document.frmdiscounttype.prev_command.value="<%=Command.PREV%>";
	document.frmdiscounttype.action="discounttype.jsp";
	document.frmdiscounttype.submit();
}

function cmdListNext(){
	document.frmdiscounttype.command.value="<%=Command.NEXT%>";
	document.frmdiscounttype.prev_command.value="<%=Command.NEXT%>";
	document.frmdiscounttype.action="discounttype.jsp";
	document.frmdiscounttype.submit();
}

function cmdListLast(){
	document.frmdiscounttype.command.value="<%=Command.LAST%>";
	document.frmdiscounttype.prev_command.value="<%=Command.LAST%>";
	document.frmdiscounttype.action="discounttype.jsp";
	document.frmdiscounttype.submit();
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
            Master Data &gt; <%=textListTitle[SESS_LANGUAGE][0]%><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frmdiscounttype" method ="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="vectSize" value="<%=vectSize%>">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="hidden_discount_type_id" value="<%=oidDiscountType%>">
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
                        <td height="22" valign="middle" colspan="3"> <%= drawList(iCommand,frmDiscountType, discountType,listDiscountType,oidDiscountType,SESS_LANGUAGE)%> </td>
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
                            <br>
                          <table width="20%" border="0" cellspacing="0" cellpadding="0">
                            <tr> 
                              <%if(privAdd && privManageData){%>
                              <%if(iCommand==Command.NONE||iCommand==Command.FIRST||iCommand==Command.PREV||iCommand==Command.NEXT||iCommand==Command.LAST||iCommand==Command.BACK|| (iCommand==Command.DELETE && iErrCode==0)){%>
                              <!--td width="19%" nowrap><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_ADD,true)%>"></a></td-->
                              <td width="81%" nowrap class="command"><a class="btn btn-lg btn-primary" href="javascript:cmdAdd()"><i class="fa fa-plus-circle"></i> <%=ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_ADD,true)%></a></td>
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
					
					String scomDel = "javascript:cmdAsk('"+oidDiscountType+"')";
					String sconDelCom = "javascript:cmdConfirmDelete('"+oidDiscountType+"')";
					String scancel = "javascript:cmdEdit('"+oidDiscountType+"')";
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

					if(iCommand == Command.EDIT && privUpdate == false){
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
				document.frmdiscounttype.<%=FrmDiscountType.fieldNames[FrmDiscountType.FRM_FIELD_CODE]%>.focus();
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

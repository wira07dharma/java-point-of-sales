<% 

/* 

 * Page Name  		:  paymentsystem.jsp

 * Created on 		:  [date] [time] AM/PM 

 * 

 * @author  		: karya 

 * @version  		: 01 

 */



/*******************************************************************

 * Page Description 	: [project description ... ] 

 * Imput Parameters 	: [input parameter ...] 

 * Output 			: [output ...] 

 *******************************************************************/

%>

<%@ page language = "java" %>

<!-- package java -->

<%@ page import = "java.util.*" %>

<!-- package dimata -->

<%@ page import = "com.dimata.util.*" %>

<!-- package qdep -->

<%@ page import = "com.dimata.gui.jsp.*" %>

<%@ page import = "com.dimata.qdep.form.*" %>

<!--package prochain20 -->

<%@ page import = "com.dimata.common.entity.payment.*" %>

<%@ page import = "com.dimata.common.form.payment.*" %>

<%@ include file = "../../main/javainit.jsp" %>

<% int  appObjCode =  0;//AppObjInfo.composeObjCode(AppObjInfo.G1_MASTER_D, AppObjInfo.G2_MASTER_D_PAYMENT, AppObjInfo.OBJ_MASTER_D_PAYMENT_SYSTEM); %>

<%@ include file = "../../main/checkuser.jsp" %>

<%

/* Check privilege except VIEW, view is already checked on checkuser.jsp as basic access*/

privAdd= true;//userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_ADD));

privUpdate=true;//userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_UPDATE));

privDelete=true;//userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_DELETE));

%>

<!-- Jsp Block -->

<%!

/* this constant used to list text of listHeader */

public static final String textListHeader[][] = {

	{"Tipe Pembayaran","Keterangan"},

	{"Payment Type","Description"}

};



public String drawList(int language,int iCommand,FrmPaymentSystem frmObject,PaymentSystem objEntity,Vector objectClass,long paymentSystemId){

	ControlList ctrlist = new ControlList();

	ctrlist.setAreaWidth("60%");

	ctrlist.setListStyle("listgen");

	ctrlist.setTitleStyle("listgentitle");

	ctrlist.setCellStyle("listgensell");

	ctrlist.setHeaderStyle("listgentitle");

	ctrlist.addHeader(textListHeader[language][0],"20%");

	ctrlist.addHeader(textListHeader[language][1],"40%");



	ctrlist.setLinkRow(0);

	ctrlist.setLinkSufix("");

	Vector lstData = ctrlist.getData();

	Vector lstLinkData = ctrlist.getLinkData();

	Vector rowx = new Vector(1,1);

	ctrlist.reset();

	int index = -1;



	for (int i = 0; i < objectClass.size(); i++) {

		PaymentSystem paymentSystem = (PaymentSystem)objectClass.get(i);

		rowx = new Vector();

		if(paymentSystemId == paymentSystem.getOID())

			 index = i; 



		if(index == i && (iCommand == Command.EDIT || iCommand == Command.ASK)){				

			rowx.add("<input type=\"text\" size=\"20\" name=\""+frmObject.fieldNames[FrmPaymentSystem.FRM_FIELD_PAYMENT_SYSTEM] +"\" value=\""+paymentSystem.getPaymentSystem()+"\" class=\"elemenForm\">");

			rowx.add("<input type=\"text\" size=\"50\" name=\""+frmObject.fieldNames[FrmPaymentSystem.FRM_FIELD_DESCRIPTION] +"\"  value=\""+paymentSystem.getDescription()+"\" class=\"elemenForm\">");

		}else{

			rowx.add("<a href=\"javascript:cmdEdit('"+String.valueOf(paymentSystem.getOID())+"')\">"+paymentSystem.getPaymentSystem()+"</a>");

			rowx.add(paymentSystem.getDescription());

		} 

		lstData.add(rowx);

	}



    rowx = new Vector();

	if(iCommand == Command.ADD || (iCommand == Command.SAVE && frmObject.errorSize() > 0)){ 

		rowx.add("<input type=\"text\" name=\""+frmObject.fieldNames[FrmPaymentSystem.FRM_FIELD_PAYMENT_SYSTEM] +"\" value=\""+objEntity.getPaymentSystem()+"\" class=\"elemenForm\">");

		rowx.add("<input type=\"text\" name=\""+frmObject.fieldNames[FrmPaymentSystem.FRM_FIELD_DESCRIPTION] +"\"  value=\""+objEntity.getDescription()+"\" class=\"elemenForm\">");

	}



	lstData.add(rowx);

	return ctrlist.draw();

}

%>

<%

int iCommand = FRMQueryString.requestCommand(request);

int start = FRMQueryString.requestInt(request, "start");

int prevCommand = FRMQueryString.requestInt(request, "prev_command");

long oidPaymentSystem = FRMQueryString.requestLong(request, "hidden_payment_system_id");

String paymentTitle = com.dimata.common.jsp.JspInfo.txtMaterialInfo[SESS_LANGUAGE][com.dimata.common.jsp.JspInfo.PAYMENT];



/*variable declaration*/

int recordToGet = 10;

String msgString = "";

int iErrCode = FRMMessage.NONE;

String whereClause = "";

String orderClause = "";



CtrlPaymentSystem ctrlPaymentSystem = new CtrlPaymentSystem(request);

ControlLine ctrLine = new ControlLine();

Vector listPaymentSystem = new Vector(1,1);



/*switch statement */

iErrCode = ctrlPaymentSystem.action(iCommand , oidPaymentSystem);

/* end switch*/

FrmPaymentSystem frmPaymentSystem = ctrlPaymentSystem.getForm();



/*count list All PaymentSystem*/

int vectSize = PstPaymentSystem.getCount(whereClause);



/*switch list PaymentSystem*/

if((iCommand == Command.FIRST || iCommand == Command.PREV )||

  (iCommand == Command.NEXT || iCommand == Command.LAST)){

		start = ctrlPaymentSystem.actionList(iCommand, start, vectSize, recordToGet);

 } 

/* end switch list*/



PaymentSystem paymentSystem = ctrlPaymentSystem.getPaymentSystem();

msgString =  ctrlPaymentSystem.getMessage();



/* get record to display */

listPaymentSystem = PstPaymentSystem.list(start,recordToGet, whereClause , orderClause);



/*handle condition if size of record to display = 0 and start > 0 	after delete*/

if (listPaymentSystem.size() < 1 && start > 0)

{

	 if (vectSize - recordToGet > recordToGet)

			start = start - recordToGet;   //go to Command.PREV

	 else{

		 start = 0 ;

		 iCommand = Command.FIRST;

		 prevCommand = Command.FIRST; //go to Command.FIRST

	 }

	 listPaymentSystem = PstPaymentSystem.list(start,recordToGet, whereClause , orderClause);

}

%>

<html><!-- #BeginTemplate "/Templates/main.dwt" -->

<head>

<!-- #BeginEditable "doctitle" --> 

<title><%=paymentTitle%></title>

<script language="JavaScript">





function cmdAdd(){

	document.frmpaymentsystem.hidden_payment_system_id.value="0";

	document.frmpaymentsystem.command.value="<%=Command.ADD%>";

	document.frmpaymentsystem.prev_command.value="<%=prevCommand%>";

	document.frmpaymentsystem.action="paymentsystem.jsp";

	document.frmpaymentsystem.submit();

}



function cmdAsk(oidPaymentSystem){

	document.frmpaymentsystem.hidden_payment_system_id.value=oidPaymentSystem;

	document.frmpaymentsystem.command.value="<%=Command.ASK%>";

	document.frmpaymentsystem.prev_command.value="<%=prevCommand%>";

	document.frmpaymentsystem.action="paymentsystem.jsp";

	document.frmpaymentsystem.submit();

}



function cmdConfirmDelete(oidPaymentSystem){

	document.frmpaymentsystem.hidden_payment_system_id.value=oidPaymentSystem;

	document.frmpaymentsystem.command.value="<%=Command.DELETE%>";

	document.frmpaymentsystem.prev_command.value="<%=prevCommand%>";

	document.frmpaymentsystem.action="paymentsystem.jsp";

	document.frmpaymentsystem.submit();

}



function cmdSave(){

	document.frmpaymentsystem.command.value="<%=Command.SAVE%>";

	document.frmpaymentsystem.prev_command.value="<%=prevCommand%>";

	document.frmpaymentsystem.action="paymentsystem.jsp";

	document.frmpaymentsystem.submit();

}



function cmdEdit(oidPaymentSystem){

	document.frmpaymentsystem.hidden_payment_system_id.value=oidPaymentSystem;

	document.frmpaymentsystem.command.value="<%=Command.EDIT%>";

	document.frmpaymentsystem.prev_command.value="<%=prevCommand%>";

	document.frmpaymentsystem.action="paymentsystem.jsp";

	document.frmpaymentsystem.submit();

}



function cmdCancel(oidPaymentSystem){

	document.frmpaymentsystem.hidden_payment_system_id.value=oidPaymentSystem;

	document.frmpaymentsystem.command.value="<%=Command.EDIT%>";

	document.frmpaymentsystem.prev_command.value="<%=prevCommand%>";

	document.frmpaymentsystem.action="paymentsystem.jsp";

	document.frmpaymentsystem.submit();

}



function cmdBack(){

	document.frmpaymentsystem.command.value="<%=Command.BACK%>";

	document.frmpaymentsystem.action="paymentsystem.jsp";

	document.frmpaymentsystem.submit();

}



function cmdListFirst(){

	document.frmpaymentsystem.command.value="<%=Command.FIRST%>";

	document.frmpaymentsystem.prev_command.value="<%=Command.FIRST%>";

	document.frmpaymentsystem.action="paymentsystem.jsp";

	document.frmpaymentsystem.submit();

}



function cmdListPrev(){

	document.frmpaymentsystem.command.value="<%=Command.PREV%>";

	document.frmpaymentsystem.prev_command.value="<%=Command.PREV%>";

	document.frmpaymentsystem.action="paymentsystem.jsp";

	document.frmpaymentsystem.submit();

}



function cmdListNext(){

	document.frmpaymentsystem.command.value="<%=Command.NEXT%>";

	document.frmpaymentsystem.prev_command.value="<%=Command.NEXT%>";

	document.frmpaymentsystem.action="paymentsystem.jsp";

	document.frmpaymentsystem.submit();

}



function cmdListLast(){

	document.frmpaymentsystem.command.value="<%=Command.LAST%>";

	document.frmpaymentsystem.prev_command.value="<%=Command.LAST%>";

	document.frmpaymentsystem.action="paymentsystem.jsp";

	document.frmpaymentsystem.submit();

}



//-------------- script form image -------------------



function cmdDelPict(oidPaymentSystem){

	document.frmimage.hidden_payment_system_id.value=oidPaymentSystem;

	document.frmimage.command.value="<%=Command.POST%>";

	document.frmimage.action="paymentsystem.jsp";

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



<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('<%=approot%>/images/BtnNewOn.jpg')">

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

    <td width="88%" valign="top" align="left"> 

      <table width="100%" border="0" cellspacing="0" cellpadding="0">  

        <tr> 

          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" --> 

            Masterdata &gt; <%=paymentTitle%><!-- #EndEditable --></td>

        </tr>

        <tr> 

          <td><!-- #BeginEditable "content" --> 

            <form name="frmpaymentsystem" method ="post" action="">

              <input type="hidden" name="command" value="<%=iCommand%>">

              <input type="hidden" name="vectSize" value="<%=vectSize%>">

              <input type="hidden" name="start" value="<%=start%>">

              <input type="hidden" name="prev_command" value="<%=prevCommand%>">

              <input type="hidden" name="hidden_payment_system_id" value="<%=oidPaymentSystem%>">

              <table width="100%" border="0" cellspacing="0" cellpadding="0">

                <tr align="left" valign="top"> 

                  <td height="8"  colspan="3"> 

                    <table width="100%" border="0" cellspacing="0" cellpadding="0">

                      <tr align="left" valign="top"> 

                        <td height="8" valign="middle" colspan="3" class="listtitle"> 

                          <hr size="1">

                        </td>

                      </tr>

                      <tr align="left" valign="top"> 

                        <td height="14" valign="middle" colspan="3" class="comment">&nbsp;<u><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar "+paymentTitle : paymentTitle+" List"%></u></td>

                      </tr>

                      <%

							try{

							%>

                      <tr align="left" valign="top"> 

                        <td height="22" valign="middle" colspan="3"> <%= drawList(SESS_LANGUAGE,iCommand,frmPaymentSystem, paymentSystem,listPaymentSystem,oidPaymentSystem)%> </td>

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

                          <%if((iCommand==Command.SAVE)&&(frmPaymentSystem.errorSize()==0)||(iCommand==Command.BACK)||(iCommand==Command.LIST)||(iCommand==Command.DELETE) || (iCommand==Command.NONE)|| (iCommand==Command.FIRST)|| (iCommand==Command.NEXT)|| (iCommand==Command.PREV)|| (iCommand==Command.LAST)){%>

                          <table cellpadding="0" cellspacing="0" border="0">

						  <%if(privAdd){%>

                            <tr> 

                              <td width="2"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>

                              <td width="24"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image261','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image261" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,paymentTitle,ctrLine.CMD_ADD,true)%>"></a></td>

                              <td width="4"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>

                              <td height="22" valign="middle" colspan="3" width="967"><a href="javascript:cmdAdd()" class="command"><%=ctrLine.getCommand(SESS_LANGUAGE,paymentTitle,ctrLine.CMD_ADD,true)%></a></td>

                            </tr>

							<%}%>

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

									ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,paymentTitle,ctrLine.CMD_SAVE,true));

									ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,paymentTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,paymentTitle,ctrLine.CMD_BACK,true)+" List");							

									ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,paymentTitle,ctrLine.CMD_ASK,true));							

									ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,paymentTitle,ctrLine.CMD_CANCEL,false));														

									

									ctrLine.initDefault();

									ctrLine.setTableWidth("80%");

									String scomDel = "javascript:cmdAsk('"+oidPaymentSystem+"')";

									String sconDelCom = "javascript:cmdConfirmDelete('"+oidPaymentSystem+"')";

									String scancel = "javascript:cmdEdit('"+oidPaymentSystem+"')";

									ctrLine.setCommandStyle("command");

									ctrLine.setColCommStyle("command");

									

									// set command caption 

									ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE,paymentTitle,ctrLine.CMD_SAVE,true));

									ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,paymentTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,paymentTitle,ctrLine.CMD_BACK,true)+" List");							

									ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE,paymentTitle,ctrLine.CMD_ASK,true));							

									ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE,paymentTitle,ctrLine.CMD_DELETE,true));														

									ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE,paymentTitle,ctrLine.CMD_CANCEL,false));																											



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

									if((iCommand ==Command.ADD)||(iCommand==Command.SAVE)&&(frmPaymentSystem.errorSize()>0)||(iCommand==Command.EDIT)||(iCommand==Command.ASK)){%>

                    <%= ctrLine.drawImage(iCommand, iErrCode, msgString)%> 

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

      <%@ include file = "../../main/footer.jsp" %>

      <!-- #EndEditable --> </td>

  </tr>

</table>

</body>

<!-- #EndTemplate --></html>


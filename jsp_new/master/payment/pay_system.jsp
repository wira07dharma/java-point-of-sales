<%@page import="com.dimata.posbo.entity.masterdata.Costing"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstCosting"%>
<% 

/* 

 * Page Name  		:  pay_system.jsp

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

<%//@ page import = "com.dimata.prochain20.entity.admin.*" %>

<%@ include file = "../../main/javainit.jsp" %>

<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_PAYMENT_TYPE); %>

<%@ include file = "../../main/checkuser.jsp" %>

<%

/* Check privilege except VIEW, view is already checked on checkuser.jsp as basic access*/

//boolean privAdd=true;//userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_ADD));

//boolean privUpdate=true;//userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_UPDATE));

//boolean privDelete=true;//userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_DELETE));

%>

<!-- Jsp Block -->

<%!



	public String drawList(int iCommand,FrmPaymentSystem frmObject, PaymentSystem objEntity, Vector objectClass,  long paymentSystemId)



	{

		ControlList ctrlist = new ControlList();

		ctrlist.setAreaWidth("55%");

		ctrlist.setListStyle("listgen");

		ctrlist.setTitleStyle("tableheader");

		ctrlist.setCellStyle("cellStyle");

		ctrlist.setHeaderStyle("tableheader");

		ctrlist.addHeader("Payment System","20%");

		ctrlist.addHeader("Description","40%");



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

					

				rowx.add("<input type=\"text\" name=\""+frmObject.fieldNames[FrmPaymentSystem.FRM_FIELD_PAYMENT_SYSTEM] +"\" value=\""+paymentSystem.getPaymentSystem()+"\" class=\"formElemen\">");

				rowx.add("<textarea name=\""+frmObject.fieldNames[FrmPaymentSystem.FRM_FIELD_DESCRIPTION] +"\"  class=\"formElemen\">"+paymentSystem.getDescription()+"</textarea>");

			}else{

				rowx.add("<a href=\"javascript:cmdEdit('"+String.valueOf(paymentSystem.getOID())+"')\">"+paymentSystem.getPaymentSystem()+"</a>");

				rowx.add(paymentSystem.getDescription());

			} 



			lstData.add(rowx);

		}



		 rowx = new Vector();



		if(iCommand == Command.ADD || (iCommand == Command.SAVE && frmObject.errorSize() > 0)){ 

				rowx.add("<input type=\"text\" name=\""+frmObject.fieldNames[FrmPaymentSystem.FRM_FIELD_PAYMENT_SYSTEM] +"\" value=\""+objEntity.getPaymentSystem()+"\" class=\"formElemen\">");

				rowx.add("<textarea name=\""+frmObject.fieldNames[FrmPaymentSystem.FRM_FIELD_DESCRIPTION] +"\"  class=\"formElemen\">"+objEntity.getDescription()+"</textarea>");



		}

		



		lstData.add(rowx);



		return ctrlist.draw();

	}

	

	public String drawList(Vector objectClass,  long paymentSystemId)



	{

		ControlList ctrlist = new ControlList();

		ctrlist.setAreaWidth("55%");

		//ctrlist.setListStyle("listgen");

		//ctrlist.setTitleStyle("tableheader");

		//ctrlist.setCellStyle("cellStyle");

		//ctrlist.setHeaderStyle("tableheader");
                ctrlist.setListStyle("listgen");
                ctrlist.setTitleStyle("listgentitle");
                ctrlist.setCellStyle("listgensell");
                ctrlist.setHeaderStyle("listgentitle");


		ctrlist.addHeader("Payment System","20%");

		ctrlist.addHeader("Description","40%");



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

				 	

			rowx.add("<a href=\"javascript:cmdEdit('"+String.valueOf(paymentSystem.getOID())+"')\">"+paymentSystem.getPaymentSystem()+"</a>");

			rowx.add(paymentSystem.getDescription());			 



			lstData.add(rowx);

		}

		//return ctrlist.draw();

		return ctrlist.draw(index);		

	}



%>

<%

int iCommand = FRMQueryString.requestCommand(request);

int start = FRMQueryString.requestInt(request, "start");

int prevCommand = FRMQueryString.requestInt(request, "prev_command");

long oidPaymentSystem = FRMQueryString.requestLong(request, "hidden_payment_system_id");

//System.out.println("oidPaymentSystem>>>>>>>>> : "+oidPaymentSystem);

/*variable declaration*/

int recordToGet = 10;

String msgString = "";

int iErrCode = FRMMessage.NONE;

String whereClause = "";

String orderClause = PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_PAYMENT_SYSTEM];



CtrlPaymentSystem ctrlPaymentSystem = new CtrlPaymentSystem(request);

ControlLine ctrLine = new ControlLine();

Vector listPaymentSystem = new Vector(1,1);



/*switch statement */

iErrCode = ctrlPaymentSystem.action(iCommand , oidPaymentSystem);



/* end switch*/

FrmPaymentSystem frmPaymentSystem = ctrlPaymentSystem.getForm();

PaymentSystem paymentSystem = ctrlPaymentSystem.getPaymentSystem();

oidPaymentSystem = paymentSystem.getOID();

/*count list All PaymentSystem*/

int vectSize = PstPaymentSystem.getCount(whereClause);





if((iCommand == Command.SAVE) && (iErrCode == FRMMessage.NONE)&& (oidPaymentSystem== 0))

	start = PstPaymentSystem.findLimitStart(paymentSystem.getOID(),recordToGet, whereClause, orderClause);

	

/*switch list PaymentSystem*/

if((iCommand == Command.FIRST || iCommand == Command.PREV )||

  (iCommand == Command.NEXT || iCommand == Command.LAST)){

		start = ctrlPaymentSystem.actionList(iCommand, start, vectSize, recordToGet);

 } 

/* end switch list*/

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

<html>

<!-- #BeginTemplate "/Templates/main_s_3.dwt" --> 

<head>

<!-- #BeginEditable "doctitle" --> 

<title>Payment System</title>

<script language="JavaScript">

function checkInfo(){

	var valDueInfo = document.frpayment.<%=FrmPaymentSystem.fieldNames[FrmPaymentSystem.FRM_FIELD_DUE_DATE_INFO]%>.value;

	if(document.frpayment.<%=FrmPaymentSystem.fieldNames[FrmPaymentSystem.FRM_FIELD_DUE_DATE_INFO]%>.checked){

		document.all.days.style.display="";

		document.all.cleared.style.display="";

		document.all.fix.style.display="";

		document.frpayment.<%=FrmPaymentSystem.fieldNames[FrmPaymentSystem.FRM_FIELD_DAYS] %>.focus();		

	}else{

		document.all.days.style.display="none";

		document.all.cleared.style.display="none";

		document.all.fix.style.display="none";

	}

	var valBankInfo = document.frpayment.<%=FrmPaymentSystem.fieldNames[FrmPaymentSystem.FRM_FIELD_BANK_INFO_IN]%>.value;

	 

	if(document.frpayment.<%=FrmPaymentSystem.fieldNames[FrmPaymentSystem.FRM_FIELD_BANK_INFO_IN]%>.checked){

		document.all.info.style.display="";

		document.frpayment.<%=FrmPaymentSystem.fieldNames[FrmPaymentSystem.FRM_FIELD_BANK_NAME] %>.focus();		

	}else{

		document.all.info.style.display="none";

	}

}

function checkCost(){
        var radios = document.getElementsByName('<%=frmPaymentSystem.fieldNames[FrmPaymentSystem.FRM_FIELD_INFO_STATUS]%>');
        for (var i = 0, length = radios.length; i < length; i++) {
            if (radios[i].checked) {
                if(radios[i].value=="2"){
                    document.all.customercost.style.display="";
                    document.all.bankcost.style.display="";
                    document.all.costing.style.display="none";
                }else{
                    document.all.customercost.style.display="none";
                    document.all.bankcost.style.display="none";
                    document.all.costing.style.display="none";
                }
            }
        }
}

function checkTypePayment(vType){
    var typePayment = vType.value;
    //alert("hello : "+typePayment);
    if(typePayment=="6"){
        document.all.costing.style.display="";
    }else{
        document.all.costing.style.display="none";
    }
}

function checkTypePaymentEdit(vType){
    var typePayment = vType;
    //alert("hello : "+typePayment);
    if(typePayment=="6" || typePayment == "5"){
        document.all.costing.style.display="";
    }else{
        document.all.costing.style.display="none";
    }
}

function cmdAdd(){

	document.frpayment.hidden_payment_system_id.value="0";

	document.frpayment.command.value="<%=Command.ADD%>";

	document.frpayment.prev_command.value="<%=prevCommand%>";

	document.frpayment.action="pay_system.jsp";

	document.frpayment.submit();

}





function cmdAsk(oidPaymentSystem){

	document.frpayment.hidden_payment_system_id.value=oidPaymentSystem;

	document.frpayment.command.value="<%=Command.ASK%>";

	document.frpayment.prev_command.value="<%=prevCommand%>";

	document.frpayment.action="pay_system.jsp";

	document.frpayment.submit();

}



function cmdConfirmDelete(oidPaymentSystem){

	document.frpayment.hidden_payment_system_id.value=oidPaymentSystem;

	document.frpayment.command.value="<%=Command.DELETE%>";

	document.frpayment.prev_command.value="<%=prevCommand%>";

	document.frpayment.action="pay_system.jsp";

	document.frpayment.submit();

}



function cmdSave(){

	document.frpayment.command.value="<%=Command.SAVE%>";

	document.frpayment.prev_command.value="<%=prevCommand%>";

	document.frpayment.action="pay_system.jsp";

	document.frpayment.submit();

}



function cmdEdit(oidPaymentSystem){

	document.frpayment.hidden_payment_system_id.value=oidPaymentSystem;

	document.frpayment.command.value="<%=Command.EDIT%>";

	document.frpayment.prev_command.value="<%=prevCommand%>";

	document.frpayment.action="pay_system.jsp";

	document.frpayment.submit();

}



function cmdCancel(oidPaymentSystem){

	document.frpayment.hidden_payment_system_id.value=oidPaymentSystem;

	document.frpayment.command.value="<%=Command.EDIT%>";

	document.frpayment.prev_command.value="<%=prevCommand%>";

	document.frpayment.action="pay_system.jsp";

	document.frpayment.submit();

}



function cmdBack(){

	document.frpayment.command.value="<%=Command.BACK%>";

	document.frpayment.action="pay_system.jsp";

	document.frpayment.submit();

}



function cmdListFirst(){

	document.frpayment.command.value="<%=Command.FIRST%>";

	document.frpayment.prev_command.value="<%=Command.FIRST%>";

	document.frpayment.action="pay_system.jsp";

	document.frpayment.submit();

}



function cmdListPrev(){

	document.frpayment.command.value="<%=Command.PREV%>";

	document.frpayment.prev_command.value="<%=Command.PREV%>";

	document.frpayment.action="pay_system.jsp";

	document.frpayment.submit();

}



function cmdListNext(){

	document.frpayment.command.value="<%=Command.NEXT%>";

	document.frpayment.prev_command.value="<%=Command.NEXT%>";

	document.frpayment.action="pay_system.jsp";

	document.frpayment.submit();

}



function cmdListLast(){

	document.frpayment.command.value="<%=Command.LAST%>";

	document.frpayment.prev_command.value="<%=Command.LAST%>";

	document.frpayment.action="pay_system.jsp";

	document.frpayment.submit();

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
<!-- #EndEditable --> 
</head>

<body bgcolor="#FFFFFF" text="#000000" onLoad="MM_preloadImages('<%=approot%>/image/reserv_f2.jpg','<%=approot%>/image/cashiering_f2.jpg','<%=approot%>/image/datamng_f2.jpg','<%=approot%>/image/ledger_f2.jpg','<%=approot%>/image/logout_f2.jpg','<%=approot%>/images/BtnNewOn.jpg')" leftmargin="3" topmargin="3">

<table width="100%" border="0" cellpadding="0" cellspacing="0" height="100%" bgcolor="#FCFDEC">

  <!--DWLayoutTable-->
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

    <td colspan="2" width="100%" align="center" valign="top"> 

      <table width="98%" border="0" cellpadding="1" cellspacing="0">

        <!--DWLayoutTable-->

        <tr> 

          <td align="center" valign="top" class="frmcontents"> 

            <table width="100%" border="0" cellpadding="0" cellspacing="0">

              <!--DWLayoutTable-->

              <tr> 

                <td  height="178" align="center" valign="middle" class="contents"> 

                  <table width="100%" border="0" cellspacing="2" cellpadding="2">

                    <tr> 

                      <td> <!-- #BeginEditable "contents" --> 

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

                        <form name="frpayment" method ="post" action="">

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

                                    <td height="8" valign="middle" colspan="3">&nbsp; 

                                    </td>

                                  </tr>

                                  <% if((listPaymentSystem == null || listPaymentSystem.size()== 0)&& iCommand == Command.NONE){%>

                                  <tr align="left" valign="top"> 

                                    <td height="14" valign="middle" colspan="3">&nbsp;<span class="comment"> 

                                      No Payment System available</span></td>

                                  </tr>

                                  <% }else{

						try{

						%>

                                  <tr align="left" valign="top"> 

                                    <td height="14" valign="middle" colspan="3">&nbsp;<span class="listtitle">Payment 

                                      System List </span></td>

                                  </tr>

                                  <tr align="left" valign="top"> 

                                    <td height="22" valign="middle" colspan="3"> 
                                        
                                      <%= drawList(listPaymentSystem,oidPaymentSystem)%></td>

                                  </tr>

                                  <% 

						  }catch(Exception exc){ 

						  }

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

									  { 

									  	if((iCommand == Command.SAVE) && (iErrCode == FRMMessage.NONE) && (oidPaymentSystem == 0))

									  		cmd = PstPaymentSystem.findLimitCommand(start,recordToGet,vectSize);

									  	else

									  		cmd = prevCommand;

									  } 

								   } 

							    %>

                                      <% ctrLine.setLocationImg(approot+"/images");

							   	ctrLine.initDefault();

								 %>

                                      <%=ctrLine.drawImageListLimit(cmd,vectSize,start,recordToGet)%> </span> </td>

                                  </tr>

                                  <tr align="left" valign="top"> 

                                    <td height="22" valign="middle" colspan="3">&nbsp;</td>

                                  </tr>

                                  <%if((iCommand==Command.SAVE)&&(frmPaymentSystem.errorSize()==0)||(iCommand==Command.BACK)||(iCommand==Command.LIST)||(iCommand==Command.DELETE) || (iCommand==Command.NONE)|| (iCommand==Command.FIRST)|| (iCommand==Command.NEXT)|| (iCommand==Command.PREV)|| (iCommand==Command.LAST)){%>

                                      <%if(privAdd){%>

                                  <tr align="left" valign="top"> 

                                    <td height="22" valign="middle" colspan="3"> 

                                      <table cellpadding="0" cellspacing="0" border="0">

                                        <tr> 

                                          <td width="2"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>

                                          <!--td width="24"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image261','','<%=approot%>images/BtnNew.jpg',1)"><img name="Image261" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="Add new"></a></td-->

                                          <td width="4"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>

                                          <td height="22" valign="middle" colspan="3" width="967"><a class="btn btn-lg btn-primary" href="javascript:cmdAdd()" class="command"><i class="fa fa-plus-circle"></i> Add 

                                            New Payment</a></td>

                                        </tr>

                                      </table>

                                    </td>

                                  </tr>

                                      <%}%>

                                <%}%>

                                </table>

                              </td>

                            </tr>

                            <tr align="left" valign="top" > 

                              <td colspan="3" class="command">&nbsp;</td>

                            </tr>

                            <%if(((iCommand==Command.SAVE)&&(frmPaymentSystem.errorSize()<0))||(iCommand==Command.ADD)||(iCommand==Command.EDIT)||(iCommand==Command.ASK)){%>

                            <tr align="left" valign="top" > 

                              <td colspan="3" class="command"> 

                                <table width="100%" border="0" cellspacing="2" cellpadding="0">

                                  <tr> 

                                    <td colspan="6"><span class="listtitle">Payment 

                                      System </span></td>

                                  </tr>

                                  <tr> <a name="edit"></a> 

                                    <td width="16%" >Payment System Name</td>

                                    <td width="2%">:</td>

                                    <td colspan="4"> 

                                      <input type="text" name="<%=frmPaymentSystem.fieldNames[FrmPaymentSystem.FRM_FIELD_PAYMENT_SYSTEM]%>" value="<%=paymentSystem.getPaymentSystem()%>" class="formElemen">

                                    </td>

                                  </tr>

                                  <tr> 

                                    <td width="16%" valign="top">Description</td>

                                    <td width="2%" valign="top">:</td>

                                    <td colspan="4"> 

                                      <textarea name="<%=frmPaymentSystem.fieldNames[FrmPaymentSystem.FRM_FIELD_DESCRIPTION]%>" class="formElemen"><%=paymentSystem.getDescription()%></textarea>

                                    </td>

                                  </tr>
                                 

                                  <%

					  	paymentSystem = PstPaymentSystem.getInfoPayment(paymentSystem);							

					  %>

                                  <tr> 

                                    <td width="16%" rowspan="3" valign="top">Info 

                                      Statue</td>

                                    <td width="2%" rowspan="3" valign="top">:</td>

                                    <td colspan="4"> 

                                      <input type="radio" name="<%=frmPaymentSystem.fieldNames[FrmPaymentSystem.FRM_FIELD_INFO_STATUS]%>" value="0" <%if(paymentSystem.getInfoStatus()==0){%>checked<%}%> onClick="javascript:checkCost()" >

                                      OTHERS&nbsp;&nbsp; 
                                         
                                      <!-- adding payment type -->
                                      <!-- by mirahu 09032012 -->
                                      
                                         <%
						Vector val_paymenttype = new Vector(1,1);
						Vector key_paymenttype = new Vector(1,1);
                                                
                                                val_paymenttype.add("0");
						key_paymenttype.add("-- Pilih --");
                                                
						val_paymenttype.add("1");
						key_paymenttype.add("Cash");

						val_paymenttype.add("2");
						key_paymenttype.add("Debit Card");
						
						val_paymenttype.add("3");
						key_paymenttype.add("Return");

                                                val_paymenttype.add("4");
						key_paymenttype.add("Voucher");
                                                
                                                val_paymenttype.add("5");
						key_paymenttype.add("Voucher Complimentary");
                                                
                                                val_paymenttype.add("6");
						key_paymenttype.add("FOC");
                                                
                                                val_paymenttype.add("7");
						key_paymenttype.add("Cash Employee");
                                                
                                                String select_paymenttype = "0";
						%>
                                        <%=ControlCombo.draw(FrmPaymentSystem.fieldNames[FrmPaymentSystem.FRM_FIELD_PAYMENT_TYPE],"formElemen", null, ""+paymentSystem.getPaymentType(), val_paymenttype, key_paymenttype, "onChange=\"javascript:checkTypePayment(this)\"")%>

                                      <input type="radio" name="<%=frmPaymentSystem.fieldNames[FrmPaymentSystem.FRM_FIELD_INFO_STATUS]%>" value="1" <%if(paymentSystem.getInfoStatus()==1){%>checked<%}%> onClick="javascript:checkCost()"  >

                                      BANK INFO OUT&nbsp;&nbsp; 

                                      <input type="radio" name="<%=frmPaymentSystem.fieldNames[FrmPaymentSystem.FRM_FIELD_INFO_STATUS]%>" value="2" <%if(paymentSystem.getInfoStatus()==2){%>checked<%}%>  onClick="javascript:checkCost()"  >

                                      CARD INFO &nbsp;&nbsp; 

                                      <input type="radio" name="<%=frmPaymentSystem.fieldNames[FrmPaymentSystem.FRM_FIELD_INFO_STATUS]%>" value="3" <%if(paymentSystem.getInfoStatus()==3){%>checked<%}%> onClick="javascript:checkCost()" >

                                      CHEQUE / BG INFO </td>
                                  </tr>

                                  <tr> 

                                    <td width="12%" nowrap> 

                                      <input type="checkbox" name="<%=frmPaymentSystem.fieldNames[FrmPaymentSystem.FRM_FIELD_DUE_DATE_INFO]%>" value="true" <%if(paymentSystem.isDueDateInfo()==true){%>checked<%}%> onClick="javascript:checkInfo()">

                                      DUE DATE INFO </td>

                                    <td nowrap width="9%"> 

                                      <div id="days">&nbsp; 

                                        <input type="text" name="<%=frmPaymentSystem.fieldNames[FrmPaymentSystem.FRM_FIELD_DAYS]%>" value="<%=paymentSystem.getDays()%>" class="formElemen" size="3" maxlength="3">

                                        days </div>

                                    </td>

                                    <td nowrap width="12%"> 

                                      <div id="cleared">&nbsp; CLEARING TO 

                                        <%

								 String wClause = ""+PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_BANK_INFO_IN]+" = TRUE";

								  Vector vctPayment = PstPaymentSystem.list(0,0,wClause,"");

								  Vector payKey = new Vector (1,1);

								  Vector payValue = new Vector (1,1);

								  for (int i=0; i<vctPayment.size(); i++){

								  	PaymentSystem paySystem = (PaymentSystem)vctPayment.get(i);

									payKey.add(paySystem.getBankName());

									payValue.add(""+paySystem.getOID());

								  }								  

								  %>

                                        <%=ControlCombo.draw(frmPaymentSystem.fieldNames[FrmPaymentSystem.FRM_FIELD_CLEARED_REF_ID], "formElemen", null, ""+paymentSystem.getClearedRefId(), payValue,payKey)%></div>

                                    </td>

                                    <td nowrap width="49%"> 

                                      <div id="fix">&nbsp; 

                                        <input type="checkbox" name="<%=frmPaymentSystem.fieldNames[FrmPaymentSystem.FRM_FIELD_FIXED]%>" value="true" <%if(paymentSystem.isFixed()==true){%>checked<%}%> >

                                        FIXED</div>

                                    </td>

                                  </tr>

                                  <tr> 

                                    <td colspan="4"> 

                                      <input type="checkbox" name="<%=frmPaymentSystem.fieldNames[FrmPaymentSystem.FRM_FIELD_BANK_INFO_IN]%>" value="true" <%if(paymentSystem.isBankInfoIn()==true){%>checked<%}%> onClick="javascript:checkInfo()">

                                      BANK INFO IN 

                                      <table width="100%" border="0" cellspacing="0" cellpadding="0" id="info">

                                        <tr> 

                                          <td width="15%">Bank Name</td>

                                          <td width="2%">:</td>

                                          <td width="83%">&nbsp; 

                                            <input type="text" name="<%=frmPaymentSystem.fieldNames[FrmPaymentSystem.FRM_FIELD_BANK_NAME]%>" value="<%=paymentSystem.getBankName()%>" class="formElemen">

                                          </td>

                                        </tr>

                                        <tr> 

                                          <td width="15%">Bank Address</td>

                                          <td width="2%">:</td>

                                          <td width="83%">&nbsp; 

                                            <input type="text" name="<%=frmPaymentSystem.fieldNames[FrmPaymentSystem.FRM_FIELD_BANK_ADDRESS]%>" value="<%=paymentSystem.getBankAddress()%>" class="formElemen">

                                          </td>

                                        </tr>

                                        <tr> 

                                          <td width="15%">Swift Code</td>

                                          <td width="2%">:</td>

                                          <td width="83%">&nbsp; 

                                            <input type="text" name="<%=frmPaymentSystem.fieldNames[FrmPaymentSystem.FRM_FIELD_SWIFT_CODE]%>" value="<%=paymentSystem.getSwiftCode()%>" class="formElemen">

                                          </td>

                                        </tr>

                                        <tr> 

                                          <td width="15%">Account Name</td>

                                          <td width="2%">:</td>

                                          <td width="83%">&nbsp; 

                                            <input type="text" name="<%=frmPaymentSystem.fieldNames[FrmPaymentSystem.FRM_FIELD_ACCOUNT_NAME]%>" value="<%=paymentSystem.getAccountName()%>" class="formElemen">

                                          </td>

                                        </tr>

                                        <tr> 

                                          <td width="15%">Account Number</td>

                                          <td width="2%">:</td>

                                          <td width="83%">&nbsp; 

                                            <input type="text" name="<%=frmPaymentSystem.fieldNames[FrmPaymentSystem.FRM_FIELD_ACCOUNT_NUMBER]%>" value="<%=paymentSystem.getAccountNumber()%>" class="formElemen">

                                          </td>

                                        </tr>

                                      </table>

                                    </td>

                                  </tr>
                                  <tr>

                                    <td width="16%" valign="top">&nbsp; </td>

                                    <td width="2%" valign="top">&nbsp; </td>

                                    <td colspan="4">&nbsp; </td>

                                  </tr>
                                  <tr  id="customercost">

                                    <td width="16%" >Charge To Customer ( % )</td>

                                    <td width="2%">:</td>

                                    <td colspan="4">
                                      
                                        <input type="text" name="<%=frmPaymentSystem.fieldNames[FrmPaymentSystem.FRM_FIELD_CHARGE_TO_CUSTOMER_PERCENT]%>" value="<%=paymentSystem.getChargeToCustomerPercent()%>" class="formElemen">
                                        
                                    </td>

                                  </tr>
                                        
                                  <tr id="bankcost">

                                    <td width="16%" valign="top">Bank Cost ( % )</td>

                                    <td width="2%" valign="top">:</td>

                                    <td colspan="4">
                                        
                                        <input type="text" name="<%=frmPaymentSystem.fieldNames[FrmPaymentSystem.FRM_FIELD_BANK_COST_PERCENT]%>" value="<%=paymentSystem.getBankCostPercent()%>" class="formElemen">

                                    </td>

                                  </tr>
                                  
                                  <tr  id="costing">

                                    <td width="16%" >Costing to</td>

                                    <td width="2%">:</td>

                                    <td colspan="4">
                                      
                                        <%
                                            Vector costingValue=new Vector();
                                            Vector costingKey=new Vector();
                                            Vector listCosting = PstCosting.listAll();
                                            costingKey.add("");
                                            costingValue.add("0");
                                            for(int i=0; i<listCosting.size(); i++){
                                                Costing costing= (Costing)listCosting.get(i);
                                                costingKey.add(""+costing.getName());
                                                costingValue.add(""+costing.getOID());
                                            }
                                        %>
                                        <%=ControlCombo.draw(frmPaymentSystem.fieldNames[FrmPaymentSystem.FRM_FIELD_COSTING_TO], "formElemen", null, ""+paymentSystem.getCostingTo(), costingValue, costingKey)%></div>
                                    </td>
                                  </tr>
                                  <tr>
                                    <td width="16%" valign="">Enable For Return </td>

                                    <td width="2%" valign="">:</td>

                                    <td colspan="4"><input type="checkbox" <%= (paymentSystem.getEnableForReturn() == 1 ? "checked":"") %> name="<%= frmPaymentSystem.fieldNames[FrmPaymentSystem.FRM_FIELD_ENABLE_FOR_RETURN] %>" value="1"></td>

                                  </tr>

                                </table>

                              </td>

                            </tr>

                            <%}%>

                            <tr align="left" valign="top" > 

                              <td colspan="3" class="command"> 

                                <%
                                ctrLine.setLocationImg(approot+"/images");

                                //ctrLine.setLocationImg(approot+"/images/ctr_line");

                                ctrLine.initDefault();

                                ctrLine.setTableWidth("80%");

                                String scomDel = "javascript:cmdAsk('"+oidPaymentSystem+"')";

                                String sconDelCom = "javascript:cmdConfirmDelete('"+oidPaymentSystem+"')";

                                String scancel = "javascript:cmdEdit('"+oidPaymentSystem+"')";

                                ctrLine.setBackCaption("Back to Payment List");

                                ctrLine.setSaveCaption("Save Payment");

                                ctrLine.setDeleteCaption("Delete Payment");

                                ctrLine.setConfirmDelCaption("Yes Delete Payment");

                                ctrLine.setAddCaption("Add Payment ");									

                                ctrLine.setCommandStyle("command");

                                ctrLine.setColCommStyle("command");



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

                                if((iCommand ==Command.ADD)||(iCommand==Command.SAVE)&&(frmPaymentSystem.errorSize()>0)||(iCommand==Command.EDIT)||(iCommand==Command.ASK)){%>

                                <%= ctrLine.drawImage(iCommand, iErrCode, msgString)%> 

                                <%}%>

                              </td>

                            </tr>

                          </table>

                        </form>

                        

                        <!-- #EndEditable --> </td>

                    </tr>

                  </table>

                </td>

              </tr>

            </table>

          </td>

        </tr>

      </table>

    </td>

  </tr>

  <tr>

    <td height="15"  align="center" valign="top" collspan="2">

	<!-- #BeginEditable "inc_ft" --> 

       <%if(menuUsed == MENU_ICON){%>
            <%@include file="../../styletemplate/footer.jsp" %>

        <%}else{%>
            <%@ include file = "../../main/footer.jsp" %>
        <%}%>

      <!-- #EndEditable --></td>

  </tr>

</table>

</body>

<!-- #EndTemplate --> 

<script language="JavaScript">

<%if(iCommand==Command.ADD || iCommand == Command.EDIT||(iCommand==Command.ASK)){%>	
        var vtyx = document.frpayment.<%=FrmPaymentSystem.fieldNames[FrmPaymentSystem.FRM_FIELD_PAYMENT_TYPE] %>.value;
        //alert("xxx"+vtyx);
	checkInfo();	
        checkCost();
        checkTypePaymentEdit(vtyx);
	document.frpayment.<%=FrmPaymentSystem.fieldNames[FrmPaymentSystem.FRM_FIELD_PAYMENT_SYSTEM] %>.focus();

	window.location ="#edit";

<%}%>

</script>

</html>




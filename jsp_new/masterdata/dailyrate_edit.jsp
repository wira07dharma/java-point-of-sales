<% 
/* 
 * Page Name  		:  dailyrate_edit.jsp
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
                   com.dimata.common.form.payment.CtrlDailyRate,
                   com.dimata.common.form.payment.FrmDailyRate,
                   com.dimata.common.entity.payment.DailyRate,
                   com.dimata.common.entity.payment.PstCurrencyType,
                   com.dimata.common.entity.payment.CurrencyType" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<%@ include file = "../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_DAILY_RATE); %>
<%@ include file = "../main/checkuser.jsp" %>

<!-- Jsp Block -->
<%!
	public static final String textListTitle[][] =
    {
        {"Nilai Tukar Harian","Harus diisi"},
        {"Daily Rate","entry required"}
    };
	
	public static final String textListHeader[][] =
    {
        {"Tipe Mata Uang","Jual","Tanggal"},
        {"Currency Type","Selling Rate","Roster Date"}
    };
%>
<%

	CtrlDailyRate ctrlDailyRate = new CtrlDailyRate(request);
	long oidDailyRate = FRMQueryString.requestLong(request, "hidden_daily_rate_id");

	int iErrCode = FRMMessage.ERR_NONE;
	String errMsg = "";
	String whereClause = "";
	String orderClause = "";
	int iCommand = FRMQueryString.requestCommand(request);
	int start = FRMQueryString.requestInt(request,"start");

	//out.println("iCommand : "+iCommand);
	ControlLine ctrLine = new ControlLine();

	iErrCode = ctrlDailyRate.action(iCommand , oidDailyRate);

	errMsg = ctrlDailyRate.getMessage();
	FrmDailyRate frmDailyRate = ctrlDailyRate.getForm();
	DailyRate dailyRate = ctrlDailyRate.getDailyRate();
	oidDailyRate = dailyRate.getOID();

	if(((iCommand==Command.SAVE)||(iCommand==Command.DELETE))&&(frmDailyRate.errorSize()<1)){
	%>
<jsp:forward page="dailyrate_list.jsp"> 
<jsp:param name="start" value="<%=start%>" />
<jsp:param name="hidden_daily_rate_id" value="<%=dailyRate.getOID()%>" />
</jsp:forward>
<%
	}

%>
<!-- End of Jsp Block -->
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">

	function cmdCancel(){
		document.frm_dailyrate.command.value="<%=Command.ADD%>";
		document.frm_dailyrate.action="dailyrate_edit.jsp";
		document.frm_dailyrate.submit();
	} 
	function cmdCancel(){
		document.frm_dailyrate.command.value="<%=Command.CANCEL%>";
		document.frm_dailyrate.action="dailyrate_edit.jsp";
		document.frm_dailyrate.submit();
	} 

	function cmdEdit(oid){ 
		document.frm_dailyrate.command.value="<%=Command.EDIT%>";
		document.frm_dailyrate.action="dailyrate_edit.jsp";
		document.frm_dailyrate.submit(); 
	} 

	function cmdSave(){
		document.frm_dailyrate.command.value="<%=Command.SAVE%>"; 
		document.frm_dailyrate.action="dailyrate_edit.jsp";
		document.frm_dailyrate.submit();
	}

	function cmdAsk(oid){
		document.frm_dailyrate.command.value="<%=Command.ASK%>"; 
		document.frm_dailyrate.action="dailyrate_edit.jsp";
		document.frm_dailyrate.submit();
	} 

	function cmdConfirmDelete(oid){
		document.frm_dailyrate.command.value="<%=Command.DELETE%>";
		document.frm_dailyrate.action="dailyrate_edit.jsp"; 
		document.frm_dailyrate.submit();
	}  

	function cmdBack(){
		document.frm_dailyrate.command.value="<%=Command.FIRST%>"; 
		document.frm_dailyrate.action="dailyrate_list.jsp";
		document.frm_dailyrate.submit();
	}

//-------------- script form image -------------------

	function cmdDelPic(oid){
		document.frm_dailyrate.command.value="<%=Command.POST%>"; 
		document.frm_dailyrate.action="dailyrate_edit.jsp";
		document.frm_dailyrate.submit();
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
    <link href="../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
<%}%>
<link rel="stylesheet" href="../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" --> 
<link rel="stylesheet" href="../styles/tab.css" type="text/css">
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
            Masterdata > <%=textListTitle[SESS_LANGUAGE][0]%> <!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frm_dailyrate" method="post" action="">
              <input type="hidden" name="command" value="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="hidden_daily_rate_id" value="<%=oidDailyRate%>">
              <table width="100%" cellspacing="1" cellpadding="1" border="0" >
                <tr> 
                  <td colspan="4"> 
                    <hr>
                  </td>
                </tr>
                <tr> 
                  <td width="1%">&nbsp;</td>
                  <td width="12%">&nbsp;</td>
                  <td width="1%">&nbsp;</td>
                  <td width="86%"><i>*) <%=textListTitle[SESS_LANGUAGE][1]%></i></td>
                </tr>
                <tr align="left"> 
                  <td width="1%"  valign="top"  >&nbsp;</td>
                  <td width="12%"  valign="top"  ><%=textListHeader[SESS_LANGUAGE][0]%></td>
                  <td  width="1%"  valign="top">:</td>
                  <td  width="86%"  valign="top"> 
                    <% 
					Vector obj_currencytypeid = new Vector(1,1); //vector of object to be listed 
					Vector val_currencytypeid = new Vector(1,1); //hidden values that will be deliver on request (oids) 
					Vector key_currencytypeid = new Vector(1,1); //texts that displayed on combo box
					val_currencytypeid.add("");
					key_currencytypeid.add("---Pilih---");
					String orderBy = PstCurrencyType.fieldNames[PstCurrencyType.FLD_TAB_INDEX];
					Vector listCurrencyType = PstCurrencyType.list(0,0,PstCurrencyType.fieldNames[PstCurrencyType.FLD_INCLUDE_IN_PROCESS]+"="+PstCurrencyType.INCLUDE,orderBy);
					if(listCurrencyType!=null&&listCurrencyType.size()>0){
						for(int i=0;i<listCurrencyType.size();i++){
							CurrencyType currencyType =(CurrencyType)listCurrencyType.get(i);
							val_currencytypeid.add(""+currencyType.getOID());
							key_currencytypeid.add(currencyType.getCode());
						}
					}
					String select_currencytypeid = ""+dailyRate.getCurrencyTypeId(); //selected on combo box
					%>
                    <%=ControlCombo.draw(FrmDailyRate.fieldNames[FrmDailyRate.FRM_FIELD_CURRENCY_TYPE_ID], null, select_currencytypeid, val_currencytypeid, key_currencytypeid, "", "formElemen")%> * <%=frmDailyRate.getErrorMsg(FrmDailyRate.FRM_FIELD_CURRENCY_TYPE_ID)%></td>
                </tr>
                <tr align="left"> 
                  <td width="1%"  valign="top"  >&nbsp;</td>
                  <td width="12%"  valign="top"  ><%=textListHeader[SESS_LANGUAGE][1]%></td>
                  <td  width="1%"  valign="top">:</td>
                  <td  width="86%"  valign="top"> 
                    <input type="text" name="<%=FrmDailyRate.fieldNames[FrmDailyRate.FRM_FIELD_SELLING_RATE]%>" value="<%=FRMHandler.userFormatStringDecimal(dailyRate.getSellingRate())%>" class="formElemen" style="text-align:right">
                    * <%=frmDailyRate.getErrorMsg(FrmDailyRate.FRM_FIELD_SELLING_RATE)%></td>
                </tr>
                <tr align="left"> 
                  <td width="1%"  valign="top"  >&nbsp;</td>
                  <td width="12%"  valign="top"  ><%=textListHeader[SESS_LANGUAGE][2]%></td>
                  <td  width="1%"  valign="top"></td>
                  <td  width="86%"  valign="top"> 
                      <%//=ControlDate.drawDateWithStyle(FrmDailyRate.fieldNames[FrmDailyRate.FRM_FIELD_ROSTER_DATE], (dailyRate.getRosterDate()==null) ? new Date() : dailyRate.getRosterDate(), 1, -5, "formElemen", "")%>
                      <%=ControlDate.drawDateWithStyle(FrmDailyRate.fieldNames[FrmDailyRate.FRM_FIELD_ROSTER_DATE], (dailyRate.getRosterDate()==null) ? new Date() : dailyRate.getRosterDate(), 1, -5, "formElemen", "")%>&nbsp;&nbsp;
                      <%=ControlDate.drawTimeSec(FrmDailyRate.fieldNames[FrmDailyRate.FRM_FIELD_ROSTER_DATE], (dailyRate.getRosterDate()==null) ? new Date(): dailyRate.getRosterDate(),"formElemen")%>
                      <%//=ControlDate.drawTimeSec(FrmDailyRate.fieldNames[FrmDailyRate.FRM_FIELD_ROSTER_TIME], (dailyRate.getRosterDate()==null) ? new Date(): dailyRate.getRosterDate(),"formElemen")%>
                  </td>
                </tr>
                <tr align="left"> 
                  <td width="1%"  valign="top"  >&nbsp;</td>
                  <td width="12%"  valign="top"  >&nbsp;</td>
                  <td  width="1%"  valign="top">&nbsp;</td>
                  <td  width="86%"  valign="top">&nbsp;</td>
                </tr>
                <tr> 
                  <td colspan="4">&nbsp;</td>
                </tr>
                <tr align="left"> 
                  <td colspan="4"> 
                    <%
							ctrLine.setLocationImg(approot+"/images");
							ctrLine.initDefault(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0]);
							ctrLine.setTableWidth("100%");
							ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_SAVE,true));
							ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_BACK,true)+" List");
							ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_ASK,true));
							ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_CANCEL,false));
									
							String scomDel = "javascript:cmdAsk('"+oidDailyRate+"')";
							String sconDelCom = "javascript:cmdConfirmDelete('"+oidDailyRate+"')";
							String scancel = "javascript:cmdEdit('"+oidDailyRate+"')";
							ctrLine.setBackCaption("Back to List");
								ctrLine.setDeleteCaption("Delete");
								ctrLine.setSaveCaption("Save");
								ctrLine.setAddCaption("");
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
							%>
                    <%= ctrLine.drawImage(iCommand, iErrCode, errMsg)%> </td>
                </tr>
              </table>
            </form>
            <%//@ include file = "../main/menumain.jsp" %>
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

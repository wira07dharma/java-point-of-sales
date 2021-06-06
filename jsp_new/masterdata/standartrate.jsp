<% 
/* 
 * Page Name  		:  standartrate.jsp
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
                   com.dimata.common.entity.payment.StandartRate,
                   com.dimata.common.entity.payment.PstStandartRate,
                   com.dimata.common.form.payment.CtrlStandartRate,
                   com.dimata.common.form.payment.FrmStandartRate,
                   com.dimata.common.entity.payment.PstCurrencyType,
                   com.dimata.common.entity.payment.CurrencyType" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<%@ include file = "../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_STANDARD_RATE); %>
<%@ include file = "../main/checkuser.jsp" %>

<!-- Jsp Block -->
<%!

	public static final String textListTitle[][] =
    {
        {"Nilai Tukar Standar","Harus diisi"},
        {"Standart Rate","required"}
    };
	
	public static final String textListHeader[][] =
    {
        {"Tipe Mata Uang","Harga Jual","Tanggal Mulai","Tanggal Akhir","Status"/*,"Dipakai mapping"*/},
        {"Currency Type","Selling Rate","Start Date","End Date","Status"/*,"Include in mapping"*/}
    };
	
	public String drawList(Vector objectClass ,  long standartRateId, int languange)

	{
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("75%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		ctrlist.addHeader(textListHeader[languange][0],"20%");
		ctrlist.addHeader(textListHeader[languange][1],"20%");
		ctrlist.addHeader(textListHeader[languange][2],"20%");
		ctrlist.addHeader(textListHeader[languange][3],"20%");
		ctrlist.addHeader(textListHeader[languange][4],"20%");
		

		ctrlist.setLinkRow(0);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		//ctrlist.setLinkPrefix("javascript:cmdEdit('");
		//ctrlist.setLinkSufix("')");
		ctrlist.reset();
		int index = -1;

		for (int i = 0; i < objectClass.size(); i++) {
			StandartRate standartRate = (StandartRate)objectClass.get(i);
			 Vector rowx = new Vector();
			 if(standartRateId == standartRate.getOID())
				 index = i;

			//rowx.add(String.valueOf(standartRate.getCurrencyTypeId()));
			CurrencyType crType = new CurrencyType();
			String codeCurrency = "";
			try{
				crType = PstCurrencyType.fetchExc(standartRate.getCurrencyTypeId());
				codeCurrency = crType.getCode();
			}catch(Exception e){
			}
			
			if(standartRate.getStatus()==PstStandartRate.ACTIVE){
				rowx.add("<a href=\"javascript:cmdEdit('"+standartRate.getOID()+"')\">"+codeCurrency+"</a>");
			}else{
				rowx.add(codeCurrency);
			}
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(standartRate.getSellingRate())+"</div>");


			String str_dt_StartDate = ""; 
			try{
				Date dt_StartDate = standartRate.getStartDate();
				if(dt_StartDate==null){
					dt_StartDate = new Date();
				}

				str_dt_StartDate = Formater.formatDate(dt_StartDate, "dd-MM-yyyy");
			}catch(Exception e){ str_dt_StartDate = ""; }

			rowx.add(str_dt_StartDate);

			String str_dt_EndDate = ""; 
			try{
				Date dt_EndDate = standartRate.getEndDate();
				if(dt_EndDate==null){
					dt_EndDate = new Date();
				}

				str_dt_EndDate = Formater.formatDate(dt_EndDate, "dd-MM-yyyy");
			}catch(Exception e){ str_dt_EndDate = ""; }

			rowx.add(str_dt_EndDate);
			
			
			rowx.add(PstStandartRate.statusName[languange][standartRate.getStatus()]);
			
                        //rowx.add(PstStandartRate.includeName[languange][standartRate.getIncludeInProcess()]);
			//rowx.add(String.valueOf(standartRate.getStatus()));

			lstData.add(rowx);
			//lstLinkData.add(String.valueOf(standartRate.getOID()));
		}

		//return ctrlist.drawList(index);
		return ctrlist.draw();
	}

%>
<%
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int prevCommand = FRMQueryString.requestInt(request, "prev_command");
long oidStandartRate = FRMQueryString.requestLong(request, "hidden_standart_rate_id");

/*variable declaration*/
boolean privManageData = true; 
int recordToGet = 10;
String msgString = "";
int iErrCode = FRMMessage.NONE;
String whereClause = "";
String orderClause = "STA."+PstStandartRate.fieldNames[PstStandartRate.FLD_STATUS]+" DESC" +
					 ", CURR."+PstCurrencyType.fieldNames[PstCurrencyType.FLD_TAB_INDEX] +
					 ", STA."+PstStandartRate.fieldNames[PstStandartRate.FLD_START_DATE]+ " DESC" + 
					 ", STA."+PstStandartRate.fieldNames[PstStandartRate.FLD_END_DATE]+" DESC";  

CtrlStandartRate ctrlStandartRate = new CtrlStandartRate(request);
ControlLine ctrLine = new ControlLine();
Vector listStandartRate = new Vector(1,1);

/*switch statement */
iErrCode = ctrlStandartRate.action(iCommand , oidStandartRate);
/* end switch*/
FrmStandartRate frmStandartRate = ctrlStandartRate.getForm();

/*count list All StandartRate*/
int vectSize = PstStandartRate.getCount(whereClause);

StandartRate standartRate = ctrlStandartRate.getStandartRate();
msgString =  ctrlStandartRate.getMessage();

/*switch list StandartRate*/
if((iCommand == Command.SAVE) && (iErrCode == FRMMessage.NONE))
	start = PstStandartRate.findLimitStart(standartRate.getOID(),recordToGet, whereClause);

if((iCommand == Command.FIRST || iCommand == Command.PREV )||
  (iCommand == Command.NEXT || iCommand == Command.LAST)){
		start = ctrlStandartRate.actionList(iCommand, start, vectSize, recordToGet);
 } 
/* end switch list*/

/* get record to display */
listStandartRate = PstStandartRate.listStandartRate(start,recordToGet, whereClause , orderClause);

/*handle condition if size of record to display = 0 and start > 0 	after delete*/
if (listStandartRate.size() < 1 && start > 0)
{
	 if (vectSize - recordToGet > recordToGet)
			start = start - recordToGet;   //go to Command.PREV
	 else{
		 start = 0 ;
		 iCommand = Command.FIRST;
		 prevCommand = Command.FIRST; //go to Command.FIRST
	 }
	 listStandartRate = PstStandartRate.listStandartRate(start,recordToGet, whereClause , orderClause);
}
%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
function cmdAdd(){
	document.frmstandartrate.hidden_standart_rate_id.value="0";
	document.frmstandartrate.command.value="<%=Command.ADD%>";
	document.frmstandartrate.prev_command.value="<%=prevCommand%>";
	document.frmstandartrate.action="standartrate.jsp";
	document.frmstandartrate.submit();
}

function cmdAsk(oidStandartRate){
	document.frmstandartrate.hidden_standart_rate_id.value=oidStandartRate;
	document.frmstandartrate.command.value="<%=Command.ASK%>";
	document.frmstandartrate.prev_command.value="<%=prevCommand%>";
	document.frmstandartrate.action="standartrate.jsp";
	document.frmstandartrate.submit();
}

function cmdConfirmDelete(oidStandartRate){
	document.frmstandartrate.hidden_standart_rate_id.value=oidStandartRate;
	document.frmstandartrate.command.value="<%=Command.DELETE%>";
	document.frmstandartrate.prev_command.value="<%=prevCommand%>";
	document.frmstandartrate.action="standartrate.jsp";
	document.frmstandartrate.submit();
}
function cmdSave(){
	document.frmstandartrate.command.value="<%=Command.SAVE%>";
	document.frmstandartrate.prev_command.value="<%=prevCommand%>";
	document.frmstandartrate.action="standartrate.jsp";
	document.frmstandartrate.submit();
	}

function cmdEdit(oidStandartRate){
	document.frmstandartrate.hidden_standart_rate_id.value=oidStandartRate;
	document.frmstandartrate.command.value="<%=Command.EDIT%>";
	document.frmstandartrate.prev_command.value="<%=prevCommand%>";
	document.frmstandartrate.action="standartrate.jsp";
	document.frmstandartrate.submit();
	}

function cmdCancel(oidStandartRate){
	document.frmstandartrate.hidden_standart_rate_id.value=oidStandartRate;
	document.frmstandartrate.command.value="<%=Command.EDIT%>";
	document.frmstandartrate.prev_command.value="<%=prevCommand%>";
	document.frmstandartrate.action="standartrate.jsp";
	document.frmstandartrate.submit();
}

function cmdBack(){
	document.frmstandartrate.command.value="<%=Command.BACK%>";
	document.frmstandartrate.action="standartrate.jsp";
	document.frmstandartrate.submit();
	}

function cmdListFirst(){
	document.frmstandartrate.command.value="<%=Command.FIRST%>";
	document.frmstandartrate.prev_command.value="<%=Command.FIRST%>";
	document.frmstandartrate.action="standartrate.jsp";
	document.frmstandartrate.submit();
}

function cmdListPrev(){
	document.frmstandartrate.command.value="<%=Command.PREV%>";
	document.frmstandartrate.prev_command.value="<%=Command.PREV%>";
	document.frmstandartrate.action="standartrate.jsp";
	document.frmstandartrate.submit();
	}

function cmdListNext(){
	document.frmstandartrate.command.value="<%=Command.NEXT%>";
	document.frmstandartrate.prev_command.value="<%=Command.NEXT%>";
	document.frmstandartrate.action="standartrate.jsp";
	document.frmstandartrate.submit();
}

function cmdListLast(){
	document.frmstandartrate.command.value="<%=Command.LAST%>";
	document.frmstandartrate.prev_command.value="<%=Command.LAST%>";
	document.frmstandartrate.action="standartrate.jsp";
	document.frmstandartrate.submit();
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
            Master 
            Data &gt; <%=textListTitle[SESS_LANGUAGE][0]%> <!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frmstandartrate" method ="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="vectSize" value="<%=vectSize%>">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="hidden_standart_rate_id" value="<%=oidStandartRate%>">
			  <input type="hidden" name="<%=FrmStandartRate.fieldNames[FrmStandartRate.FRM_FIELD_STATUS] %>" value="1">
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
								if (listStandartRate.size()>0){
							%>
                      <tr align="left" valign="top"> 
                        <td height="22" valign="middle" colspan="3"> <%= drawList(listStandartRate,oidStandartRate,SESS_LANGUAGE)%> </td>
                      </tr>
                      <%  } 
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
                        <td height="22" valign="middle" colspan="3"><table width="20%" border="0" cellspacing="0" cellpadding="0">
                            <tr> 
                              <%if(privAdd && privManageData){%>
                              <%if(iCommand==Command.NONE||iCommand==Command.FIRST||iCommand==Command.PREV||iCommand==Command.NEXT||iCommand==Command.LAST||iCommand==Command.BACK||iCommand==Command.SAVE||iCommand==Command.DELETE){%>
                              <!--td width="19%" nowrap><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_ADD,true)%>"></a></td-->
                              <br><td width="81%" nowrap class="command"><a class="btn btn-lg btn-primary" href="javascript:cmdAdd()"><i class="fa fa-plus-circle"></i> <%=ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_ADD,true)%></a></td>
                              <%}}%>
                            </tr>
                          </table></td>
                      </tr>
                    </table>
                  </td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="8" valign="middle" colspan="3"> 
                    <%if((iCommand ==Command.ADD)||(iCommand==Command.SAVE)&&(frmStandartRate.errorSize()>0)||(iCommand==Command.EDIT)||(iCommand==Command.ASK)){%>
                    <table width="100%" border="0" cellspacing="2" cellpadding="0">
                      <tr align="left" valign="top"> 
                        <td height="21" valign="middle" width="1%">&nbsp;</td>
                        <td height="21" valign="middle" width="13%">&nbsp;</td>
                        <td height="21" valign="middle" width="1%">&nbsp;</td>
                        <td height="21" colspan="2" width="85%" class="comment">*)= 
                          <%=textListTitle[SESS_LANGUAGE][1]%> </td>
                      </tr>
                      <tr align="left" valign="top"> 
                        <td height="21" valign="top" width="1%">&nbsp;</td>
                        <td height="21" valign="top" width="13%"><%=textListHeader[SESS_LANGUAGE][0]%></td>
                        <td height="21" valign="top" width="1%">:</td>
                        <td height="21" colspan="2" width="85%"> 
                          <% Vector currencytypeid_value = new Vector(1,1);
						Vector currencytypeid_key = new Vector(1,1);
					 	String sel_currencytypeid = ""+standartRate.getCurrencyTypeId();
					   currencytypeid_key.add("");
					   currencytypeid_value.add("---Pilih---");
					   String orderBy = PstCurrencyType.fieldNames[PstCurrencyType.FLD_TAB_INDEX];
					   Vector listCurrencyType = PstCurrencyType.list(0,0,PstCurrencyType.fieldNames[PstCurrencyType.FLD_INCLUDE_IN_PROCESS]+"="+PstCurrencyType.INCLUDE,orderBy);
								if(listCurrencyType!=null&&listCurrencyType.size()>0){
									for(int i=0;i<listCurrencyType.size();i++){
										CurrencyType currencyType =(CurrencyType)listCurrencyType.get(i);
										currencytypeid_value.add(currencyType.getCode());
										currencytypeid_key.add(""+currencyType.getOID());
									}
								}
					   %>
                          <%= ControlCombo.draw(frmStandartRate.fieldNames[FrmStandartRate.FRM_FIELD_CURRENCY_TYPE_ID],null, sel_currencytypeid, currencytypeid_key, currencytypeid_value, "", "formElemen") %> 
                      <tr align="left" valign="top"> 
                        <td height="21" valign="top" width="1%">&nbsp;</td>
                        <td height="21" valign="top" width="13%"><%=textListHeader[SESS_LANGUAGE][1]%></td>
                        <td height="21" valign="top" width="1%">:</td>
                        <td height="21" colspan="2" width="85%"> 
                          <input type="text" name="<%=frmStandartRate.fieldNames[FrmStandartRate.FRM_FIELD_SELLING_RATE] %>"  value="<%=FRMHandler.userFormatStringDecimal(standartRate.getSellingRate())%>" class="formElemen" style="text-align:right">
                          * <%= frmStandartRate.getErrorMsg(FrmStandartRate.FRM_FIELD_SELLING_RATE) %> 
                      <tr align="left" valign="top"> 
                        <td height="21" valign="top" width="1%">&nbsp;</td>
                        <td height="21" valign="top" width="13%"><%=textListHeader[SESS_LANGUAGE][2]%></td>
                        <td height="21" valign="top" width="1%">:</td>
                        <td height="21" colspan="2" width="85%"> <%=	ControlDate.drawDateWithStyle(frmStandartRate.fieldNames[FrmStandartRate.FRM_FIELD_START_DATE], standartRate.getStartDate()==null?new Date():standartRate.getStartDate(), 4,-5, "formElemen", "") %> 
                      <tr align="left" valign="top"> 
                        <td height="21" valign="top" width="1%">&nbsp;</td>
                        <td height="21" valign="top" width="13%"><%=textListHeader[SESS_LANGUAGE][3]%></td>
                        <td height="21" valign="top" width="1%">:</td>
                        <td height="21" colspan="2" width="85%"> <%=	ControlDate.drawDateWithStyle(frmStandartRate.fieldNames[FrmStandartRate.FRM_FIELD_END_DATE], standartRate.getEndDate()==null?new Date():standartRate.getEndDate(), 4,-5, "formElemen", "") %> 
                      <!--tr align="left" valign="top"> 
                        <td height="21" valign="top" width="1%">&nbsp;</td>
                        <td height="21" valign="top" width="13%"><%=textListHeader[SESS_LANGUAGE][4]%></td>
                        <td height="21" valign="top" width="1%">&nbsp;</td>
                        <td height="21" colspan="2" width="85%"> 
                          <input type="checkbox" name="<%=frmStandartRate.fieldNames[FrmStandartRate.FRM_FIELD_STATUS] %>" value="1<%//=standartRate.getStatus()%>" <%if(standartRate.getStatus()==1){%>checked<%}%><%if(iCommand==Command.ADD){%>checked<%}%>>
                          <%=PstStandartRate.statusName[SESS_LANGUAGE][PstStandartRate.ACTIVE]%> -->
                          <!--tr align="left" valign="top"> 
                        <td height="21" valign="top" width="17%"><%//=textListHeader[SESS_LANGUAGE][5]%></td>
                        <td height="21" colspan="2" width="83%"> 
                          <input type="checkbox" name="<%//=frmStandartRate.fieldNames[FrmStandartRate.FRM_FIELD_INCLUDE_IN_PROCESS] %>" value="1" <%//if(standartRate.getIncludeInProcess() ==PstStandartRate.INCLUDE){%>checked<%//}%><%//if(iCommand==Command.ADD){%>checked<%//}%>>
                          <%//=PstStandartRate.includeName[SESS_LANGUAGE][PstStandartRate.INCLUDE]%> -->
                      <tr align="left" valign="top"> 
                        <td height="8" valign="middle" width="1%">&nbsp;</td>
                        <td height="8" valign="middle" width="13%">&nbsp;</td>
                        <td height="8" valign="middle" width="1%">&nbsp;</td>
                        <td height="8" colspan="2" width="85%">&nbsp; </td>
                      </tr>
                      <tr align="left" valign="top" > 
                        <td colspan="5" class="command"> 
                          <%
									ctrLine.setLocationImg(approot+"/images");
									ctrLine.initDefault(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0]);
									ctrLine.setTableWidth("80%");
									ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_SAVE,true));
									ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_BACK,true)+" List");
									ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_ASK,true));
									ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_CANCEL,false));
									
									String scomDel = "javascript:cmdAsk('"+oidStandartRate+"')";
									String sconDelCom = "javascript:cmdConfirmDelete('"+oidStandartRate+"')";
									String scancel = "javascript:cmdEdit('"+oidStandartRate+"')";
									//ctrLine.setBackCaption("Back to List");
									//ctrLine.setCommandStyle("command");
										//ctrLine.setDeleteCaption("Delete");
										//ctrLine.setSaveCaption("Save");
										//ctrLine.setAddCaption("");
										
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
									%>
                          <%= ctrLine.drawImage(iCommand, iErrCode, msgString)%> </td>
                      </tr>
                    </table>
                    <%}%>
                  </td>
                </tr>
              </table>
            </form>
			<%
			if(iCommand==Command.ADD || iCommand==Command.EDIT)
			{
			%>
			<script language="javascript">
				document.frmstandartrate.<%=FrmStandartRate.fieldNames[FrmStandartRate.FRM_FIELD_CURRENCY_TYPE_ID]%>.focus();
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

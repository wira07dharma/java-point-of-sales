<% 
/* 
 * Page Name  		:  dailyrate_list.jsp
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
                   com.dimata.common.entity.payment.DailyRate,
                   com.dimata.common.form.payment.CtrlDailyRate,
                   com.dimata.common.entity.payment.PstDailyRate,
                   com.dimata.common.entity.payment.CurrencyType,
                   com.dimata.common.entity.payment.PstCurrencyType" %>
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
        {"Nilai Tukar Harian"},
        {"Daily Rate"}
    };
	
	public static final String textListHeader[][] =
    {
        {"Tipe Mata Uang","Rate","Tanggal Catat"},
        {"Currency Type","Selling Rate","Roster Date"}
    };
	
	public String drawList(Vector objectClass , int languange){
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("50%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		ctrlist.addHeader(textListHeader[languange][0],"5%");
		ctrlist.addHeader(textListHeader[languange][1],"10%");
		ctrlist.addHeader(textListHeader[languange][2],"10%");

		//ctrlist.setLinkRow(0);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		//ctrlist.setLinkPrefix("javascript:cmdEdit('");
		//ctrlist.setLinkSufix("')");
		ctrlist.reset();

		for (int i = 0; i < objectClass.size(); i++) {
			DailyRate dailyRate = (DailyRate)objectClass.get(i);
			Vector rowx = new Vector();
			
			CurrencyType crType = new CurrencyType();
			String codeCurrency = "";
			try{
				crType = PstCurrencyType.fetchExc(dailyRate.getCurrencyTypeId());
				codeCurrency = crType.getCode();
			}catch(Exception e){
			}

			rowx.add(codeCurrency);

			//rowx.add("<div align=\"right\">"+Formater.formatNumber(dailyRate.getSellingRate(),"#,###")+"</div>");
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dailyRate.getSellingRate())+"</div>");

			String str_dt_RosterDate = ""; 
			try{
				Date dt_RosterDate = dailyRate.getRosterDate();				
				if(dt_RosterDate==null){
					dt_RosterDate = new Date();
				}

				str_dt_RosterDate = Formater.formatDate(dt_RosterDate, "dd MMM yyyy HH:mm:ss");
			}catch(Exception e){ str_dt_RosterDate = ""; }

			rowx.add("&nbsp;&nbsp;&nbsp;&nbsp;"+str_dt_RosterDate);

			lstData.add(rowx);
			//lstLinkData.add(String.valueOf(dailyRate.getOID()));
		}

		return ctrlist.draw();
	}

%>
<%

	ControlLine ctrLine = new ControlLine();
	CtrlDailyRate ctrlDailyRate = new CtrlDailyRate(request);
	long oidDailyRate = FRMQueryString.requestLong(request, "hidden_daily_rate_id");

	int iErrCode = FRMMessage.ERR_NONE;
	String msgStr = "";
	int iCommand = FRMQueryString.requestCommand(request);
	int start = FRMQueryString.requestInt(request, "start");
	int recordToGet = 5;
	int vectSize = 0;
	String whereClause = "";
	boolean privManageData = true; 

	//out.println("iCommand : "+iCommand);
	//out.println("<br>start : "+start);
	//out.println("<br>recordToGet : "+recordToGet);

	vectSize = PstDailyRate.getCount(whereClause);

	if((iCommand!=Command.FIRST)&&(iCommand!=Command.NEXT)&&(iCommand!=Command.PREV)&&(iCommand!=Command.LIST)&&(iCommand!=Command.NONE)){
		start = PstDailyRate.findLimitStart(  oidDailyRate,recordToGet, whereClause);
	}

	//out.println("vectSize : "+vectSize);

	ctrlDailyRate.action(iCommand , oidDailyRate);

	if((iCommand==Command.FIRST)||(iCommand==Command.NEXT)||(iCommand==Command.PREV)||(iCommand==Command.LAST)||(iCommand==Command.LIST))

		start = ctrlDailyRate.actionList(iCommand, start, vectSize, recordToGet);

	//Vector records = PstDailyRate.listAll();
	//Vector records = PstDailyRate.list(start,recordToGet,"",PstDailyRate.fieldNames[PstDailyRate.FLD_ROSTER_DATE]+" DESC");
	String orderBy = "DY."+PstDailyRate.fieldNames[PstDailyRate.FLD_ROSTER_DATE];
					 //", CURR."+PstCurrencyType.fieldNames[PstCurrencyType.FLD_TAB_INDEX];
	Vector records = PstDailyRate.listDailyRate(start, recordToGet, "", orderBy);
        Vector recordCurrent = PstDailyRate.getCurrentDailyRate();
	

%>
<!-- End of Jsp Block -->
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">

	function cmdAdd(){
		document.frm_dailyrate.command.value="<%=Command.ADD%>";
		document.frm_dailyrate.action="dailyrate_edit.jsp";
		document.frm_dailyrate.submit();
	}

	function cmdEdit(oid){
		document.frm_dailyrate.command.value="<%=Command.EDIT%>";
		document.frm_dailyrate.hidden_daily_rate_id.value=oid;
		document.frm_dailyrate.action="dailyrate_edit.jsp";
		document.frm_dailyrate.submit();
	}

	function cmdListFirst(){
		document.frm_dailyrate.command.value="<%=Command.FIRST%>";
		document.frm_dailyrate.action="dailyrate_list.jsp";
		document.frm_dailyrate.submit();
	}

	function cmdListPrev(){
		document.frm_dailyrate.command.value="<%=Command.PREV%>";
		document.frm_dailyrate.action="dailyrate_list.jsp";
		document.frm_dailyrate.submit();
	}

	function cmdListNext(){
		document.frm_dailyrate.command.value="<%=Command.NEXT%>";
		document.frm_dailyrate.action="dailyrate_list.jsp";
		document.frm_dailyrate.submit();
	}

	function cmdListLast(){
		document.frm_dailyrate.command.value="<%=Command.LAST%>";
		document.frm_dailyrate.action="dailyrate_list.jsp";
		document.frm_dailyrate.submit();
	}

	function cmdBack(){
		document.frm_dailyrate.command.value="<%=Command.BACK%>";
		document.frm_dailyrate.action="srcdailyrate.jsp";
		document.frm_dailyrate.submit();
	}
//------------------------- START JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------
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
//------------------------- END JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------

</script>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<!-- #BeginEditable "styles" -->
<%if(menuUsed == MENU_ICON){%>
    <link href="../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
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
            Master Data > <%=textListTitle[SESS_LANGUAGE][0]%><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frm_dailyrate" method="post" action="">
              <input type="hidden" name="command" value="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="hidden_daily_rate_id" value="<%=oidDailyRate%>">
              <table border="0" width="100%">
                <tr>
                  <td height="8"> 
                    <hr>
                  </td>
                </tr>
                <tr> 
                  <td height="8" width="100%" class="comment"><%=textListTitle[SESS_LANGUAGE][0]%></td>
                </tr>
              </table>
                <span><br>
                &nbsp;<b>Running Rate <i>(Today)</i></b></span>
              <%if((records!=null)&&(records.size()>0)){%>
              <%=drawList(recordCurrent,SESS_LANGUAGE)%>
              <%}
              else{
	      %>
            <span class="comment"><br>
            &nbsp;Data tidak ada.</span>
              <%}%>
                <span><br>
                &nbsp;<b>History</b></span>
              <%if((records!=null)&&(records.size()>0)){%>
              <%=drawList(records,SESS_LANGUAGE)%> 
              <%}
					else{
					%>
              <span class="comment"><br>
              &nbsp;Data tidak ada.</span> 
              <%}%>
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr> 
                  <td> 
                    <table width="100%" cellspacing="0" cellpadding="3">
                      <tr> 
                        <td> 
                          <% ctrLine.setLocationImg(approot+"/images");
												ctrLine.initDefault();
											%>
                          <%=ctrLine.drawImageListLimit(iCommand,vectSize,start,recordToGet)%></td>
                      </tr>
                    </table>
                  </td>
                </tr>
                <tr> 
                  <td width="46%">&nbsp;</td>
                </tr>
                <tr> 
                  <td width="46%" nowrap align="left" class="command">
					<table width="20%" border="0" cellspacing="0" cellpadding="0">
                            <tr> 
                              <%if(privAdd && privManageData){%>                              
                              
                        <!--td width="13%" nowrap><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_ADD,true)%>"></a></td-->
                              
                        <td width="87%" nowrap class="command"><a class="btn btn-lg btn-primary" href="javascript:cmdAdd()"><i class="fa fa-plus-circle"></i> <%=ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_ADD,true)%></a></td>
                              <%}%>
                            </tr>
                          </table>
                  </td>
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
            <%@include file="../../styletemplate/footer.jsp" %>

        <%}else{%>
            <%@ include file = "../../main/footer.jsp" %>
        <%}%>
      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>

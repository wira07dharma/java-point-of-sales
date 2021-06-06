<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "com.dimata.util.*,
                   com.dimata.common.jsp.JspInfo" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package common -->
<%@ page import = "com.dimata.common.entity.periode.*" %>
<%@ page import = "com.dimata.common.form.periode.*" %>

<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_STOCK_PERIOD); %>
<%@ include file = "../../main/checkuser.jsp" %>

<!-- Jsp Block -->
<%!
/* this constant used to list text of listHeader */
public static final String textListHeader[][] =
{
	{"Nama Periode","Tipe","Tanggal Mulai","Tanggal Akhir","Status","Batas Input"},
	{"Period Name","Type","Start Date","End Date","Status","Last Entry"}
};

public String drawList(int language,Vector objectClass,long stockMaterialPeriodeId)
{
	ControlList ctrlist = new ControlList();
	ctrlist.setAreaWidth("70%");
	ctrlist.setListStyle("listgen");
	ctrlist.setTitleStyle("listgentitle");
	ctrlist.setCellStyle("listgensell");
	ctrlist.setHeaderStyle("listgentitle");
	ctrlist.addHeader(textListHeader[language][0],"10%");
	ctrlist.addHeader(textListHeader[language][2],"15%");
	ctrlist.addHeader(textListHeader[language][3],"15%");
	ctrlist.addHeader(textListHeader[language][5],"10%");
    ctrlist.addHeader(textListHeader[language][4],"10%");

	ctrlist.setLinkRow(0);
	ctrlist.setLinkSufix("");
	Vector lstData = ctrlist.getData();
	Vector lstLinkData = ctrlist.getLinkData();
	ctrlist.setLinkPrefix("javascript:cmdEdit('");
	ctrlist.setLinkSufix("')");
	ctrlist.reset();
	int index = -1;

	for(int i=0; i<objectClass.size(); i++)
	{
		Periode stockMaterialPeriode = (Periode)objectClass.get(i);
		Vector rowx = new Vector();
		if(stockMaterialPeriodeId == stockMaterialPeriode.getOID())
			 index = i;

		String str_dt_StartDate = "";
		try
		{
			Date dt_StartDate = stockMaterialPeriode.getStartDate();
			if(dt_StartDate==null)
			{
				dt_StartDate = new Date();
			}
			str_dt_StartDate = Formater.formatDate(dt_StartDate, "dd-MM-yyyy");
		}
		catch(Exception e){ str_dt_StartDate = ""; }

		String str_dt_EndDate = "";
		try
		{
			Date dt_EndDate = stockMaterialPeriode.getEndDate();
			if(dt_EndDate==null)
			{
				dt_EndDate = new Date();
			}
			str_dt_EndDate = Formater.formatDate(dt_EndDate, "dd-MM-yyyy");
		}
		catch(Exception e)
		{
			str_dt_EndDate = "";
		}

        String str_last_entry = "";
        try
        {
            Date dt_lastEntry = stockMaterialPeriode.getLastEntry();
            if(dt_lastEntry==null)
            {
                dt_lastEntry = new Date();
            }
            str_last_entry = Formater.formatDate(dt_lastEntry, "dd-MM-yyyy");
        }
        catch(Exception e)
        {
            str_last_entry = "";
        }

		rowx.add(String.valueOf(stockMaterialPeriode.getPeriodeName()));
		rowx.add("<div align=\"center\">"+str_dt_StartDate+"</div>");
		rowx.add("<div align=\"center\">"+str_dt_EndDate+"</div>");
        rowx.add("<div align=\"center\">"+str_last_entry+"</div>");
		rowx.add("<div align=\"center\">"+PstPeriode.statusPeriode[stockMaterialPeriode.getStatus()]+"</div>");

		lstData.add(rowx);
		lstLinkData.add(String.valueOf(stockMaterialPeriode.getOID()));
	}
	return ctrlist.draw();
}
%>
<%
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int prevCommand = FRMQueryString.requestInt(request, "prev_command");
long oidMaterialPeriode = FRMQueryString.requestLong(request, "hidden_mat_stock_periode_id");
String periodTitle = "Periode"; //JspInfo.txtMaterialInfo[SESS_LANGUAGE][JspInfo.MATERIAL_PERIOD];
Date dt = FRMQueryString.requestDate(request, ""+FrmPeriode.fieldNames[FrmPeriode.FRM_FIELD_LAST_ENTRY]+"");

/* variable declaration */
int recordToGet = 10;
String msgString = "";
int iErrCode = FRMMessage.NONE;
String whereClause = "";
String orderClause = PstPeriode.fieldNames[PstPeriode.FLD_PERIODE_NAME];

CtrlPeriode ctrlMaterialPeriode = new CtrlPeriode(request);
ControlLine ctrLine = new ControlLine();
Vector listMaterialPeriode = new Vector(1,1);

    if(iCommand==Command.SAVE){
        try{
            Periode matPeriod = PstPeriode.fetchExc(oidMaterialPeriode);
            if(dt.before(matPeriod.getEndDate())){
                iCommand=Command.EDIT;
                msgString = "<b>Tanggal terakhir entry harus lebih besar dari tanggal akhir periode...</b>"; // ctrlMaterialPeriode.getMessage();
            }
        }catch(Exception e){}
    }

iErrCode = ctrlMaterialPeriode.action(iCommand,oidMaterialPeriode);
FrmPeriode frmMaterialPeriode = ctrlMaterialPeriode.getForm();

int vectSize = PstPeriode.getCount(whereClause);
Periode stockMaterialPeriode = ctrlMaterialPeriode.getPeriode();


if((iCommand == Command.SAVE) && (iErrCode == FRMMessage.NONE))
	start = PstPeriode.findLimitStart(stockMaterialPeriode.getOID(),recordToGet, whereClause, "");

if((iCommand == Command.FIRST || iCommand == Command.PREV )||
  (iCommand == Command.NEXT || iCommand == Command.LAST)){
		start = ctrlMaterialPeriode.actionList(iCommand,start,vectSize,recordToGet);
}

listMaterialPeriode = PstPeriode.list(start,recordToGet,whereClause,orderClause);
if (listMaterialPeriode.size()<1 && start>0)
{
	 if (vectSize - recordToGet > recordToGet)
			start = start - recordToGet;
	 else{
		 start = 0 ;
		 iCommand = Command.FIRST;
		 prevCommand = Command.FIRST;
	 }
	 listMaterialPeriode = PstPeriode.list(start,recordToGet,whereClause,orderClause);
}

String attrForm = "";
if(iCommand==Command.EDIT){
	attrForm = "disabled=\"true\"";
}
%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title><%=periodTitle%></title>
<script language="JavaScript">
function cmdSave(){
	document.frmmatstockperiode.command.value="<%=Command.SAVE%>";
	document.frmmatstockperiode.prev_command.value="<%=prevCommand%>";
	document.frmmatstockperiode.action="period.jsp";
	document.frmmatstockperiode.submit();
	}

function cmdEdit(oidMaterialPeriode){
	document.frmmatstockperiode.hidden_mat_stock_periode_id.value=oidMaterialPeriode;
	document.frmmatstockperiode.command.value="<%=Command.EDIT%>";
	document.frmmatstockperiode.prev_command.value="<%=prevCommand%>";
	document.frmmatstockperiode.action="period.jsp";
	document.frmmatstockperiode.submit();
	}

function cmdBack(){
	document.frmmatstockperiode.command.value="<%=Command.BACK%>";
	document.frmmatstockperiode.action="period.jsp";
	document.frmmatstockperiode.submit();
	}

function cmdListFirst(){
	document.frmmatstockperiode.command.value="<%=Command.FIRST%>";
	document.frmmatstockperiode.prev_command.value="<%=Command.FIRST%>";
	document.frmmatstockperiode.action="period.jsp";
	document.frmmatstockperiode.submit();
}

function cmdListPrev(){
	document.frmmatstockperiode.command.value="<%=Command.PREV%>";
	document.frmmatstockperiode.prev_command.value="<%=Command.PREV%>";
	document.frmmatstockperiode.action="period.jsp";
	document.frmmatstockperiode.submit();
	}

function cmdListNext(){
	document.frmmatstockperiode.command.value="<%=Command.NEXT%>";
	document.frmmatstockperiode.prev_command.value="<%=Command.NEXT%>";
	document.frmmatstockperiode.action="period.jsp";
	document.frmmatstockperiode.submit();
}

function cmdListLast(){
	document.frmmatstockperiode.command.value="<%=Command.LAST%>";
	document.frmmatstockperiode.prev_command.value="<%=Command.LAST%>";
	document.frmmatstockperiode.action="period.jsp";
	document.frmmatstockperiode.submit();
}

function backMenu(){
	document.frmmatstockperiode.action="<%=approot%>/management/main_material.jsp";
	document.frmmatstockperiode.submit();
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
<link rel="stylesheet" href="../../styles/main.css" type="text/css">
<link rel="stylesheet" href="../../styles/tab.css" type="text/css">
<%if(menuUsed == MENU_ICON){%>
    <link href="../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
<%}%>
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
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->&nbsp;Masterdata
            &gt; <%=periodTitle%><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" -->
            <form name="frmmatstockperiode" method ="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="vectSize" value="<%=vectSize%>">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="hidden_mat_stock_periode_id" value="<%=oidMaterialPeriode%>">
              <%if(iCommand==Command.EDIT)
			  {
			  %>
              <input type="hidden" name="<%=FrmPeriode.fieldNames[FrmPeriode.FRM_FIELD_PERIODE_NAME]%>" value="<%=stockMaterialPeriode.getPeriodeName()%>">
              <input type="hidden" name="<%=FrmPeriode.fieldNames[FrmPeriode.FRM_FIELD_PERIODE_TYPE]%>" value="<%=stockMaterialPeriode.getPeriodeType()%>">
              <input type="hidden" name="<%=FrmPeriode.fieldNames[FrmPeriode.FRM_FIELD_STATUS]%>" value="<%=stockMaterialPeriode.getStatus()%>">
              <input type="hidden" name="<%=FrmPeriode.fieldNames[FrmPeriode.FRM_FIELD_START_DATE]%>_dy" value="<%=stockMaterialPeriode.getStartDate().getDate()%>">
              <input type="hidden" name="<%=FrmPeriode.fieldNames[FrmPeriode.FRM_FIELD_START_DATE]%>_mn" value="<%=stockMaterialPeriode.getStartDate().getMonth()+1%>">
              <input type="hidden" name="<%=FrmPeriode.fieldNames[FrmPeriode.FRM_FIELD_START_DATE]%>_yr" value="<%=stockMaterialPeriode.getStartDate().getYear()+1900%>">
              <input type="hidden" name="<%=FrmPeriode.fieldNames[FrmPeriode.FRM_FIELD_END_DATE]%>_dy" value="<%=stockMaterialPeriode.getEndDate().getDate()%>">
              <input type="hidden" name="<%=FrmPeriode.fieldNames[FrmPeriode.FRM_FIELD_END_DATE]%>_mn" value="<%=stockMaterialPeriode.getEndDate().getMonth()+1%>">
              <input type="hidden" name="<%=FrmPeriode.fieldNames[FrmPeriode.FRM_FIELD_END_DATE]%>_yr" value="<%=stockMaterialPeriode.getEndDate().getYear()+1900%>">
              <%
			  }
			  %>
              <table width="100%" border="0">
                <tr>
                  <td>
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr align="left" valign="top">
                        <td height="14" valign="middle" colspan="3" class="title">
                          <hr size="1">
                        </td>
                      </tr>
                      <tr align="left" valign="top">
                        <td height="14" valign="middle" colspan="3" class="comment">&nbsp;<u><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar "+periodTitle : periodTitle+" List"%></u></td>
                      </tr>
                      <%
						try{
							if (listMaterialPeriode.size()>0){
						%>
                      <tr align="left" valign="top">
                        <td height="22" valign="middle" colspan="3"> <%=drawList(SESS_LANGUAGE,listMaterialPeriode,oidMaterialPeriode)%> </td>
                      </tr>
                      <%  }
					  }catch(Exception exc){ System.out.println("exc"+exc.toString()); }
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
                          <% ctrLine.setLocationImg(approot+"/images/ctr_line");
							ctrLine.initDefault();
							 %>
                          <%=ctrLine.drawImageListLimit(cmd,vectSize,start,recordToGet)%> </span> </td>
                      </tr>
                      <%if(iCommand!=Command.ADD && iCommand!=Command.EDIT ){%>
                      <tr align="left" valign="top">
                        <td valign="top" colspan="3">
                          <%
						  privAdd = false;
						  if(privAdd && listMaterialPeriode.size()==0){%>
                          <table width="17%" border="0" cellspacing="2" cellpadding="3">
                            <tr>
                              <td width="18%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image500','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image5001" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,periodTitle,ctrLine.CMD_ADD,true)%>"></a></td>
                              <td nowrap width="82%"><a href="javascript:cmdAdd()" class="command"><%=ctrLine.getCommand(SESS_LANGUAGE,periodTitle,ctrLine.CMD_ADD,true)%></a></td>
                            </tr>
                          </table>
                          <%}%>
                        </td>
                      </tr>
                      <%}%>
                    </table>
                  </td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                </tr>
                <tr>
                  <td>
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr align="left" valign="top">
                        <td height="8"  colspan="3" valign="top"> </td>
                      </tr>
                      <%if(iCommand==Command.EDIT  || iCommand==Command.ADD || (iCommand==Command.SAVE && iErrCode!=0)){%>
                      <tr align="left" valign="top">
                        <td height="8" valign="top" colspan="3">
                          <table width="100%" border="0" cellspacing="1" cellpadding="1">
                            <tr align="left">
                              <td colspan="9" class="comment" height="30" valign="top"><u><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Editor "+periodTitle : periodTitle+" Editor"%></u></td>
                            </tr>
                            <tr align="left">
                              <td height="21" width="10%">&nbsp;<%=textListHeader[SESS_LANGUAGE][0]%></td>
                              <td height="21" colspan="2" width="90%">
                                <input type="text" name="<%=frmMaterialPeriode.fieldNames[FrmPeriode.FRM_FIELD_PERIODE_NAME]%>_0" value="<%=stockMaterialPeriode.getPeriodeName()%>" class="formElemen" size="50" disabled="true">
                            <tr align="left">
                              <td height="21" width="10%">&nbsp;<%=textListHeader[SESS_LANGUAGE][2]%></td>
                              <td height="21" colspan="2" width="90%">
                                <input type="text" name="<%=frmMaterialPeriode.fieldNames[FrmPeriode.FRM_FIELD_START_DATE]%>_0" value="<%=Formater.formatDate(stockMaterialPeriode.getStartDate(),"MMMM dd, yyyy")%>" class="formElemen" <%=attrForm%> disabled="true">
                            <tr align="left">
                              <td height="21" width="10%">&nbsp;<%=textListHeader[SESS_LANGUAGE][3]%></td>
                              <td height="21" colspan="2" width="90%">
                                <input type="text" name="<%=frmMaterialPeriode.fieldNames[FrmPeriode.FRM_FIELD_END_DATE]%>_0" value="<%=Formater.formatDate(stockMaterialPeriode.getEndDate(),"MMMM dd, yyyy")%>" class="formElemen" <%=attrForm%> disabled="true">
                            <tr align="left">
                              <td height="21" width="10%">&nbsp;<%=textListHeader[SESS_LANGUAGE][5]%></td>
                              <td height="21" colspan="2" width="90%">
                                <%=ControlDate.drawDate(frmMaterialPeriode.fieldNames[FrmPeriode.FRM_FIELD_LAST_ENTRY], stockMaterialPeriode.getLastEntry(),"formElemen",1,-1)%>
                            <tr align="left" >
                              <td colspan="3" class="command" valign="top"><%=msgString%>
                            </tr>
                          </table>
                        </td>
                      </tr>
                      <tr align="left" valign="top" >
                        <td colspan="3" class="command" valign="top">
                          <table width="17%" border="0" cellspacing="2" cellpadding="3">
                            <tr>
                              <!--td width="18%"><a href="javascript:cmdSave()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image300','','<%=approot%>/images/BtnSaveOn.jpg',1)"><img name="Image301" border="0" src="<%=approot%>/images/BtnSave.jpg" width="24" height="24" alt="Save Periode"></a></td-->
                              <td nowrap ><a href="javascript:cmdSave()" class="btn btn-lg btn-primary"><i class="fa fa-check"></i> Save Periode</a></td>
                              <!--td width="18%"><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image300','','<%=approot%>/images/BtnBackOn.jpg',1)"><img name="Image301" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,periodTitle,ctrLine.CMD_BACK,true)%>"></a></td-->
                              <td nowrap ><a href="javascript:cmdBack()" class="btn btn-lg btn-primary"><i class="fa fa-arrow-left"></i> <%=ctrLine.getCommand(SESS_LANGUAGE,periodTitle,ctrLine.CMD_BACK,true)%></a></td>
                            </tr>
                          </table>
                        </td>
                      </tr>
                      <%}%>
                    </table>
                  </td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr align="left" valign="top">
                  <td height="8"  colspan="3">&nbsp; </td>
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
    </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>


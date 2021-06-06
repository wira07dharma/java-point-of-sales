<% 
/* 
 * Page Name  		:  back_up.jsp
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
<%@ page import = "java.util.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package harisma -->
<%@ page import = "com.dimata.service.entity.*" %>
<%@ page import = "com.dimata.service.form.*" %>
<%@ page import = "com.dimata.service.session.*" %>

<%@ include file = "../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_ADMIN, AppObjInfo.G2_ADMIN_SYSTEM, AppObjInfo.OBJ_ADMIN_SYSTEM_BACK_UP); %>
<%@ include file = "../main/checkuser.jsp" %>

<!-- Jsp Block -->
<%
int iCommandBackup = FRMQueryString.requestInt(request,"command_backup");
long oidServiceBackup = FRMQueryString.requestLong(request, "hidden_service_backup_id");       

Date toDay = new Date();

// simpan data dalam array
int serviceType = PstServiceConfiguration.SERVICE_TYPE_BACKUPDB;
int startTimeHour = FRMQueryString.requestInt(request,FrmServiceConfiguration.fieldNames[FrmServiceConfiguration.FRM_FIELD_START_TIME]+"_hr");
int startTimeMinutes = FRMQueryString.requestInt(request,FrmServiceConfiguration.fieldNames[FrmServiceConfiguration.FRM_FIELD_START_TIME]+"_mi");
int timeInterval = FRMQueryString.requestInt(request,FrmServiceConfiguration.fieldNames[FrmServiceConfiguration.FRM_FIELD_PERIODE]);

Vector vectResult = new Vector(1,1);
CtrlServiceConfiguration ctrlServiceConfiguration = new CtrlServiceConfiguration(request);	
ServiceConfiguration serviceConfBackup = ctrlServiceConfiguration.action(iCommandBackup, oidServiceBackup, serviceType, startTimeHour, startTimeMinutes, timeInterval);
oidServiceBackup = serviceConfBackup.getOID();
%>
<!-- End of Jsp Block -->
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
<!--
function cmdStopBackup()
{
  document.frm_servicecenter.command_backup.value="<%= Command.STOP %>"; 
  document.frm_servicecenter.start.value="0";
  document.frm_servicecenter.maxLog.value="0";  
  document.frm_servicecenter.submit();  
}

function cmdStartBackup()
{
  document.frm_servicecenter.start.value="0";
  document.frm_servicecenter.maxLog.value="0";
  document.frm_servicecenter.command_backup.value="<%= Command.START %>";   
  document.frm_servicecenter.action="service_center.jsp";  
  document.frm_servicecenter.submit();
}

function cmdUpdateBackup(){
  document.frm_servicecenter.command_backup.value="<%= Command.SAVE %>";     
  document.frm_servicecenter.start.value="0";
  document.frm_servicecenter.maxLog.value="0";
  document.frm_servicecenter.submit();
} 

// -------------- script control line -------------------
function MM_swapImgRestore() 
{ //v3.0
	var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_findObj(n, d) 
{ //v4.0
	var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
	d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
	if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
	for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
	if(!x && document.getElementById) x=document.getElementById(n); return x;
}

function MM_swapImage() 
{ //v3.0 
	var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
	if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}//-->
</script>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<!-- #BeginEditable "styles" --> 
<link rel="stylesheet" href="../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" --> 
<link rel="stylesheet" href="../styles/tab.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "headerscript" --> <!-- #EndEditable --> 
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
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
  <tr> 
    <td valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">  
        <tr> 
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->Service 
            &gt; Backup Database<!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frm_servicecenter" method="post" action="">
              <input type="hidden" name="command" value="">
              <input type="hidden" name="command_backup" value="<%=iCommandBackup%>">
              <input type="hidden" name="hidden_service_backup_id" value="<%=oidServiceBackup%>">
              <input type="hidden" name="log_command" value="0">
              <input type="hidden" name="start" value="<%//=start%>">
              <input type="hidden" name="maxLog" value="<%//=maxLog%>">    
              <table width="100%" border="0" cellpadding="2" cellspacing="2">
			  <tr align="left" valign="top"> 
				<td height="8" valign="middle" colspan="3"> 
				  <hr size="1">
				</td>
			  </tr>
			  
                <tr> 
                  <td width="48%" valign="top"> 
                    <table width="100%" border="0" cellspacing="1" cellpadding="1" align="center">
                      <tr> 
                        <td align="left" colspan="2">This process will backup 
                          database based on service configuration below.</td>
                      </tr>
                      <tr> 
                        <td align="left" width="5%">&nbsp;</td>
                        <td align="left" width="95%">- Click <font color="#0000FF">start</font> 
                          button to start backup database service process.</td>
                        <%
						  Date dtBackup=null;
						  try
						  {
							dtBackup = serviceConfBackup.getStartTime();  
							if(dtBackup==null)
							{
								dtBackup = new Date();
							}
						  }
						  catch(Exception e)
						  {
							dtBackup = new Date();
						  }
						  %>
                      </tr>
                      <tr> 
                        <td align="left" width="5%">&nbsp;</td>
                        <td align="left" width="95%">- Click <font color="#0000FF">stop</font> 
                          button to stop backup database service process.</td>
                      </tr>
                      <tr> 
                        <td align="left" width="5%">&nbsp;</td>
                        <td align="left" width="95%">&nbsp;</td>
                      </tr>
                      <tr> 
                        <td align="left" colspan="2"><b>Configuration</b> :</td>
                      </tr>
                      <tr> 
                        <td align="left" width="5%">&nbsp;</td>
                        <td align="left" width="95%">Start Time&nbsp;: <%=ControlDate.drawTime(FrmServiceConfiguration.fieldNames[FrmServiceConfiguration.FRM_FIELD_START_TIME], dtBackup, "formElemen")%></td>
                      </tr>
                      <tr> 
                        <td align="left" width="5%">&nbsp;</td>
                        <td align="left" width="95%">Interval&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:   
                          <input type="text" name="<%=FrmServiceConfiguration.fieldNames[FrmServiceConfiguration.FRM_FIELD_PERIODE]%>" value="<%=serviceConfBackup.getPeriode()%>" class="formElemen" size="10">
                          <i>(minutes)</i></td>
                      </tr>
                      <%
						String stCommandScript = "d:\\tomcat\\webapps\\posver02\\backup.bat";    																											
						BackupDatabaseProcess objBackupDatabaseProcess = new BackupDatabaseProcess(stCommandScript);
						switch (iCommandBackup)
						{
							case  Command.START :
								try
								{
									objBackupDatabaseProcess.startService();  //menghidupkan service presence analyser
								}
								catch(Exception e)
								{
									System.out.println("exc when objBackupDatabaseProcess.startService() : " + e.toString());
								}
								break;
						
							case  Command.STOP :
								try
								{
									objBackupDatabaseProcess.stopService();  //mematikan service
								}
								catch(Exception e)
								{
									System.out.println("exc when objBackupDatabaseProcess.stopService : " + e.toString());
								}
								break;
						}						  

						boolean serviceBackupDatabaseRunning = objBackupDatabaseProcess.getStatus();  													
						String stopStsBackup="";
						String startStsBackup="";
						if(serviceBackupDatabaseRunning)
						{ 					
							startStsBackup="disabled=\"true\"";
							stopStsBackup="";
						} 
						else
						{
							startStsBackup="";
							stopStsBackup="disabled=\"true\"";
						}
						%>
                      <tr> 
                        <td align="left" width="5%">&nbsp;</td>
                        <td align="left" width="95%"> 
                          <input type="button" name="btnSaveBackup" value="   Save   " onClick="javascript:cmdUpdateBackup()" class="formElemen" <%=startStsBackup%>>
                        </td>
                      </tr>
                      <tr> 
                        <td align="left" width="5%">&nbsp;</td>
                        <td align="left" width="95%">&nbsp;</td>
                      </tr>
                      <tr> 
                        <td align="left" colspan="2"><b>Status</b> : </td>
                      </tr>
                      <tr> 
                        <td align="left" width="5%">&nbsp;</td>
                        <td align="left" width="95%">Backup database service status 
                          is&nbsp;&nbsp; 
                          <%							
							if(serviceBackupDatabaseRunning)
							{
							%>
                          <font color="#009900">Running...</font> 
                          <%
							}
							else
							{
							%>
                          <font color="#FF0000">Stopped</font> 
                          <%
							}
							%>
                        </td>
                      </tr>
                      <tr> 
                        <td width="5%">&nbsp;</td>
                        <td width="95%"> 
                          <%//if(hasExecutePriv){%>
                          <input type="button" name="Button4" value="  Start  " onClick="javascript:cmdStartBackup()" class="formElemen" <%=startStsBackup%>>
                          <input type="button" name="Submit24" value="  Stop  " onClick="javascript:cmdStopBackup()" class="formElemen" <%=stopStsBackup%>>
                          <%//}%>
                        </td>
                      </tr>
                    </table>
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
      <%@ include file = "../main/footer.jsp" %>
      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>

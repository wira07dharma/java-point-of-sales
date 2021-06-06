
<%@page import="com.dimata.posbo.entity.warehouse.BillImageMapping"%>
<%@page import="com.dimata.posbo.form.warehouse.FrmBillImageMapping"%>
<%@page import="com.dimata.posbo.form.warehouse.CtrlBillImageMapping"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstBillImageMapping"%>
<% 
/* 
 * Page Name  		:  careerpath.jsp
 * Created on 		:  [date] [time] AM/PM 
 * 
 * @author  		: lkarunia 
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

<%@ page import="com.dimata.util.blob.*" %>
<%@include file="../../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK_REPORT, AppObjInfo.OBJ_STOCK_POTITION);%>
<%@ include file = "../../../main/checkuser.jsp" %>
<%
/* Check privilege except VIEW, view is already checked on checkuser.jsp as basic access*/
//boolean privAdd=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_ADD));
//boolean privUpdate=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_UPDATE));
//boolean privDelete=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_DELETE));
%>
<%!
    public static String strText[][] = {
	{
	"Judul","Deskripsi","Sematkan Berkas",
        "Catatan : Pastikan nama berkas tidak mengandung karakter seperti ' , \" , /, `, $, @, ! ",
	"Simpan Dokumen",
        "File Tipe : Hanya file bertipe jpg, and jpeg saja"
	},	
	
	{
	"Title","Description","Attach File",
        "NOTE : File name may not consist special character like ' , \" , /, `, $, @, !, ",
	"Save Document",
        "File Types : File types only jpg, and jpeg "
	}
    };
%>
<%
int iCommand = FRMQueryString.requestCommand(request);
long oidDokumen = FRMQueryString.requestLong(request, "dokumen_id");
long oidbm = FRMQueryString.requestLong(request, "bill_main_id");
int prevCommand = FRMQueryString.requestInt(request, "prev_command");

System.out.println("oidDokumen.........."+oidDokumen);


/*variable declaration*/
int recordToGet = 10;
String msgString = "";
int iErrCode = FRMMessage.NONE;
String whereClause = PstBillImageMapping.fieldNames[PstBillImageMapping.FLD_IMAGE_ID]+ " = "+oidDokumen;
String orderClause = "";

CtrlBillImageMapping ctrlBillImageMapping = new CtrlBillImageMapping(request);
ControlLine ctrLine = new ControlLine();
Vector listEmpRelevantDoc = new Vector(1,1);
Vector listSection = new Vector(1,1);

/*switch statement */
iErrCode = ctrlBillImageMapping.action(iCommand , oidDokumen);
/* end switch*/
FrmBillImageMapping frmBillImageMapping = ctrlBillImageMapping.getForm();

BillImageMapping billImageMapping = ctrlBillImageMapping.getBillImageMapping();
msgString =  ctrlBillImageMapping.getMessage();

/*switch list CareerPath*/

//listSection = PstSection.list(0,500,PstSection.fieldNames[PstSection.FLD_DEPARTMENT_ID]+ " = "+oidDepartment,"SECTION");

Vector vectPict = new Vector(1,1);
 vectPict.add(""+oidDokumen);

session.putValue("SELECTED_FILE", vectPict);
System.out.println("vectPict.........."+vectPict);
%>
<html><!-- #BeginTemplate "/Templates/maintab.dwt" -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Relevant Document </title>
<script language="JavaScript">
function cmdUpload(){
	document.frm_upload_pict.command.value="<%=Command.SAVE%>";
	document.frm_upload_pict.prev_command.value="<%=prevCommand%>";
	document.frm_upload_pict.action="dokumen_upload.jsp";
	document.frm_upload_pict.submit();
	}

</script>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<!-- #BeginEditable "styles" --> 
<link rel="stylesheet" href="../../style/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #EndEditable -->
<!-- #BeginEditable "headerscript" --> 
<SCRIPT language=JavaScript>
<!--
function hideObjectForEmployee(){    
} 
	 
function hideObjectForLockers(){ 
}
	
function hideObjectForCanteen(){
}
	
function hideObjectForClinic(){
}

function hideObjectForMasterdata(){
}

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
//-->
</SCRIPT>
<!-- #EndEditable -->
</head>  

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('<%=approot%>/images/BtnNewOn.jpg')">
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#F9FCFF">
  
  <tr> 
    <td width="88%" valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="3" cellpadding="2">
        <tr> 
          <td width="100%"> 
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr> 
                <td> 
                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr> 
                      <td valign="top"> 
                        <table width="100%" border="0" cellspacing="1" cellpadding="1">
                          <tr> 
                            <td valign="top"> <!-- #BeginEditable "content" --> 
                              <form name="frm_upload_pict" method ="post" enctype="multipart/form-data" action="">
                                <input type="hidden" name="command" value="">
								<input type="hidden" name="dokumen_id" value="<%=oidDokumen%>">
								<input type="hidden" name="pinjaman_id" value="<%=oidbm %>">
								<input type="hidden" name="prev_command" value="<%=prevCommand%>">
								
                                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                  <% if(oidbm != 0){%>
                                  <tr> 
                                    <td> 
									<br>
                                      
                                    </td>
                                  </tr>
                                  <%}%>
                                  <tr> 
                                    <td class="tablecolor"> 
                                      <table width="100%" border="0" cellspacing="1" cellpadding="1" class="tablecolor">
                                        <tr> 
                                          <td valign="top"> 
                                            <table width="100%" border="0" cellspacing="0" cellpadding="0" class="tabbg">
                                              <tr align="left" valign="top"> 
                                                <td height="8"  colspan="3"> 
                                                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                    <tr align="left" valign="top"> 
                                                      <td height="14" valign="middle" colspan="3" class="listedittitle">&nbsp;</td>
                                                    </tr>
                                                    <% if(oidDokumen != 0){
                                                            BillImageMapping objBillImageMapping = new BillImageMapping();
                                                            try{
                                                                 objBillImageMapping = PstBillImageMapping.fetchExc(oidDokumen);
                                                            }catch(Exception exc){
                                                                 objBillImageMapping = new BillImageMapping();
                                                            }
                                                      %>
                                                    <tr align="left" valign="top"> 
                                                      <td height="14" valign="middle" colspan="3" class="listedittitle"> 
                                                        <table width="100%" border="0" cellspacing="2" cellpadding="1">
                                                          <tr> 
                                                            <td width="17%"><strong><%=strText[SESS_LANGUAGE][0]%> </strong>
                                                               </td>
                                                            <td width="2%">:</td>
                                                            <td width="81%"><strong><%=objBillImageMapping.getDocumentName()%></strong></td>
                                                          </tr>
                                                          <tr> 
                                                            <td width="17%"><strong><%=strText[SESS_LANGUAGE][1]%></strong></td>
                                                            <td width="2%">:</td>
                                                            <td width="81%"><strong><%=objBillImageMapping.getKeterangan()%></strong></td>
                                                          </tr>
                                                        </table>
                                                      </td>
                                                    </tr>
                                                    <tr align="left" valign="top"> 
                                                      <td height="14" valign="middle" colspan="3" class="listedittitle">&nbsp;</td>
                                                    </tr>
                                                    <%}%>
													<tr><td>
													<table width="100%" border="0">
													 <tr align="left" valign="top"> 
                                                      <td height="22" valign="middle" colspan="3" class="listtitle"> 
                                                        &nbsp;<%=strText[SESS_LANGUAGE][2]%> &nbsp&nbsp <%=strText[SESS_LANGUAGE][3]%>
                                                      </td>
                                                    </tr>
													  <tr>
														
														<td><input type="file" name="FRM_FIELD_NAMA_FILE" size="60" height="100" accept="image/jpg, image/jpeg"></td>
													  </tr>
													</table>
													<table cellpadding="0" cellspacing="0" border="0">
                                                          <tr> 
                                                           <td height="25" valign="middle" colspan="3" width="951"><a href="javascript:cmdUpload()" class="command"><%=strText[SESS_LANGUAGE][4]%></a> 
                                                            </td>
                                                            
                                                          </tr>
                                                          <tr> 
                                                           <td><p><%=strText[SESS_LANGUAGE][5]%></p> 
                                                            </td>
                                                            
                                                          </tr>
                                                        </table>
													</td></tr>
                                                  </table>
                                                </td>
                                              </tr>
                                              <tr> 
                                                <td>&nbsp; </td>
                                              </tr>
                                            </table>
                                          </td>
                                        </tr>
                                      </table>
                                    </td>
                                  </tr>
                                  <tr> 
                                    <td>&nbsp; </td>
                                  </tr>
                                </table>
                              </form>
                              <!-- #EndEditable --> </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                    <tr> 
                      <td >&nbsp;</td>
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
    <td colspan="2" height="20" > <!-- #BeginEditable "footer" --> 
      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #BeginEditable "script" --> 
<script language="JavaScript">
	var oBody = document.body;
	var oSuccess = oBody.attachEvent('onkeydown',fnTrapKD);
</script>
<!-- #EndEditable --> 
<!-- #EndTemplate --></html>

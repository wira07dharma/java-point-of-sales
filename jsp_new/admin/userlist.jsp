<%
/*
 * userlist.jsp
 *
 * Created on April 04, 2002, 11:30 AM
 *
 * @author  ktanjana
 * @version 
 */
%>
<%@ page language="java" %>
<%@ page import = "java.util.*,
                   com.dimata.gui.jsp.ControlList,
                   com.dimata.gui.jsp.ControlLine,
                   com.dimata.qdep.form.FRMQueryString,
                   com.dimata.util.Command
                   ,
                   com.dimata.posbo.entity.admin.PstAppUser,
                   com.dimata.posbo.form.admin.CtrlAppUser,
                   com.dimata.posbo.entity.admin.AppUser" %>
				   
<%@ include file = "../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_ADMIN , AppObjInfo.G2_ADMIN_USER, AppObjInfo.OBJ_ADMIN_USER_USER); %>
<%@ include file = "../main/checkuser.jsp" %>
<%
   /* privView = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_VIEW));
    privAdd = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_ADD));
    privUpdate = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_UPDATE));
    privDelete = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_DELETE));
    if(!privView){
        response.sendRedirect(approot + "/homepage.jsp");
    * setelah nikah kehidupanku aman2 aja sampai saat ini, namun kdng2 kesel itu kan selalu ada seperti perbedaan pendapat.
 *  * pernah kejadian
    }*/
%>

<!-- JSP Block -->
<%!
public static final String textListTitleHeader[][] =
{
	{"Sistem","Daftar Pemakai","ID Pemakai","Nama Lengkap","Status","Tambah Pemakai Baru"},
	{"System","User LIst","User ID","Full Name","Status","Add New User"}
};

public String drawListAppUser(int language,Vector objectClass)
{
	String temp = ""; 
	String regdatestr = "";
	
	ControlList ctrlist = new ControlList();
	ctrlist.setAreaWidth("90%");
	ctrlist.setListStyle("listgen");
	ctrlist.setTitleStyle("listgentitle");
	ctrlist.setCellStyle("listgensell");
	ctrlist.setHeaderStyle("listgentitle");
	ctrlist.addHeader(textListTitleHeader[language][2],"20%");
	ctrlist.addHeader(textListTitleHeader[language][3],"70%");
	ctrlist.addHeader(textListTitleHeader[language][4],"10%");

	ctrlist.setLinkRow(0);
        ctrlist.setLinkSufix("");

	Vector lstData = ctrlist.getData();

	Vector lstLinkData 	= ctrlist.getLinkData();

	ctrlist.setLinkPrefix("javascript:cmdEdit('");
	ctrlist.setLinkSufix("')");
	ctrlist.reset();

	for (int i = 0; i < objectClass.size(); i++) {
		 AppUser appUser = (AppUser)objectClass.get(i);

		 Vector rowx = new Vector();
		 
		 rowx.add(String.valueOf(appUser.getLoginId()));
		 rowx.add(String.valueOf(appUser.getFullName()));
		 rowx.add(String.valueOf(AppUser.getStatusTxt(appUser.getUserStatus())));		 
		 		 
		 lstData.add(rowx);
		 lstLinkData.add(String.valueOf(appUser.getOID()));
	}						

	return ctrlist.draw();
}

%>
<%

/* VARIABLE DECLARATION */
int recordToGet = 10;

String order = " " + PstAppUser.fieldNames[PstAppUser.FLD_LOGIN_ID];

Vector listAppUser = new Vector(1,1);
ControlLine ctrLine = new ControlLine();

/* GET REQUEST FROM HIDDEN TEXT */
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start"); 
long appUserOID = FRMQueryString.requestLong(request,"user_oid");
int listCommand = FRMQueryString.requestInt(request, "list_command");
if(listCommand==Command.NONE)
 listCommand = Command.LIST;

CtrlAppUser ctrlAppUser = new CtrlAppUser(request);
 
int vectSize = PstAppUser.getCount(""); 
start = ctrlAppUser.actionList(listCommand, start,vectSize,recordToGet);

listAppUser = PstAppUser.listPartObj(start,recordToGet, "" , order);

%>
<!-- End of JSP Block -->
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
<% if (privAdd){%>
function addNew(){
	document.frmAppUser.user_oid.value="0";
	document.frmAppUser.list_command.value="<%=listCommand%>";
	document.frmAppUser.command.value="<%=Command.ADD%>";
	document.frmAppUser.action="useredit.jsp";
	document.frmAppUser.submit();
}
<%}%>
 
function cmdEdit(oid){
	document.frmAppUser.user_oid.value=oid;
	document.frmAppUser.list_command.value="<%=listCommand%>";
	document.frmAppUser.command.value="<%=Command.EDIT%>";
	document.frmAppUser.action="useredit.jsp";
	document.frmAppUser.submit();
}

function cmdListFirst(){
	document.frmAppUser.command.value="<%=Command.FIRST%>";
	document.frmAppUser.list_command.value="<%=Command.FIRST%>";
	document.frmAppUser.action="userlist.jsp";
	document.frmAppUser.submit();
}

function cmdListPrev(){
	document.frmAppUser.command.value="<%=Command.PREV%>";
	document.frmAppUser.list_command.value="<%=Command.PREV%>";
	document.frmAppUser.action="userlist.jsp";
	document.frmAppUser.submit();
}

function cmdListNext(){
	document.frmAppUser.command.value="<%=Command.NEXT%>";
	document.frmAppUser.list_command.value="<%=Command.NEXT%>";
	document.frmAppUser.action="userlist.jsp";
	document.frmAppUser.submit();
}

function cmdListLast(){
	document.frmAppUser.command.value="<%=Command.LAST%>";
	document.frmAppUser.list_command.value="<%=Command.LAST%>";
	document.frmAppUser.action="userlist.jsp";
	document.frmAppUser.submit();
}

</script>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<!-- #BeginEditable "styles" --> 
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
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" --><%=textListTitleHeader[SESS_LANGUAGE][0]%>
            > <%=textListTitleHeader[SESS_LANGUAGE][1]%><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frmAppUser" method="post" action="">
              <input type="hidden" name="command" value="">
              <input type="hidden" name="user_oid" value="<%=appUserOID%>">
              <input type="hidden" name="vectSize" value="<%=vectSize%>">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="list_command" value="<%=listCommand%>">
              <table width="100%" cellspacing="0" cellpadding="0">
                <tr> 
                  <td colspan="2" class="listtitle">
                    <hr size="1">
                  </td>
                </tr>
              </table>
              <% if ((listAppUser!=null)&&(listAppUser.size()>0)){ %>
              <%=drawListAppUser(SESS_LANGUAGE, listAppUser)%> 
              <%}%>
              <table width="100%">
                <tr> 
                  <td colspan="2"> 
				  <span class="command"> 
				  <% 
				   int cmd = 0;
				   if ((iCommand == Command.FIRST || iCommand == Command.PREV )|| 
					(iCommand == Command.NEXT || iCommand == Command.LAST))
						cmd =iCommand; 
				   else{
					  if(iCommand == Command.NONE)
						cmd = Command.FIRST;
				   } 
				   ctrLine.setLocationImg(approot+"/images");
				   ctrLine.initDefault();						   
				  %>
				  <%=ctrLine.drawImageListLimit(cmd,vectSize,start,recordToGet)%> 
				  </span>				  
				  </td>
                </tr>
                <% if (privAdd){%>
                <tr valign="middle"> 
                  <td colspan="2" class="command"> 
                    <table width="15%" border="0" cellspacing="2" cellpadding="2">
                      <tr> 
                        <td width="20%"><a href="javascript:addNew()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10011','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image10011" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="Tambah Baru User"></a></td>
                        <td nowrap width="80%"><a href="javascript:addNew()" class="command">
                          <%=textListTitleHeader[SESS_LANGUAGE][5]%></a></td>
                      </tr>
                    </table>
                  </td>
                </tr>
                <%}%>
                <tr> 
                  <td width="13%">&nbsp;</td>
                  <td width="87%">&nbsp;</td>
                </tr>
              </table>
            </form>
<script language="JavaScript">
<!--
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
</script>
            <link rel="stylesheet" href="../css/default.css" type="text/css">
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

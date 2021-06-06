 
<%
/*
 * grouplist.jsp
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
                   com.dimata.util.Command,
                   com.dimata.posbo.entity.admin.AppGroup,
                   com.dimata.posbo.entity.admin.PstAppGroup,
                   com.dimata.posbo.form.admin.CtrlAppGroup" %>

<%@ include file = "../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_ADMIN , AppObjInfo.G2_ADMIN_USER, AppObjInfo.OBJ_ADMIN_USER_GROUP); %>
<%@ include file = "../main/checkuser.jsp" %>


<%!
public static final String listTextGlobal[][] = {
	{"Managemen Pemakai","Kelompok","Daftar","Edit","Tidak Ada Kelompok"},
	{"User Management","Group","List","Edit","No Group Available"}
};

public static final String listTextTableHeader[][] = {
	{"No.","Nama","Deskripsi","Tanggal Pembuatan"},
	{"No.","Name","Description","Creation Date"}
};

public String drawListAppGroup(int language, int start, Vector objectClass) {
	String temp = "";
	String regdatestr = "";
	
	ControlList ctrlist = new ControlList();
	ctrlist.setAreaWidth("100%");
	ctrlist.setListStyle("listgen");
	ctrlist.setTitleStyle("listgentitle");
	ctrlist.setCellStyle("listgensell");
	ctrlist.setHeaderStyle("listgentitle");
	ctrlist.setCellSpacing("1");
	
	ctrlist.addHeader(listTextTableHeader[language][0],"5%");
	ctrlist.addHeader(listTextTableHeader[language][1],"20%");
	ctrlist.addHeader(listTextTableHeader[language][2],"60%");
	ctrlist.addHeader(listTextTableHeader[language][3],"15%");		

	ctrlist.setLinkRow(1);
	ctrlist.setLinkSufix("");
	Vector lstData = ctrlist.getData();
	Vector lstLinkData 	= ctrlist.getLinkData();
	
	ctrlist.setLinkPrefix("javascript:cmdEdit('");
	ctrlist.setLinkSufix("')");
	ctrlist.reset();
								
	for (int i = 0; i < objectClass.size(); i++) {
		 AppGroup appGroup = (AppGroup)objectClass.get(i);

		 Vector rowx = new Vector();
		 
		 rowx.add("<div align=\"center\">"+(start+i+1)+"</div>");
		 rowx.add(String.valueOf(appGroup.getGroupName()));		 
		 rowx.add(String.valueOf(appGroup.getDescription()));
		 try{
			 Date regdate = appGroup.getRegDate();
			 regdatestr = Formater.formatDate(regdate, "dd MMMM yyyy");
		 }catch(Exception e){
			 regdatestr = "";
		 }
		 
		 rowx.add(regdatestr);
		 		 
		 lstData.add(rowx);
		 lstLinkData.add(String.valueOf(appGroup.getOID()));
	}						

	return ctrlist.draw();
}

%>
<%

/* VARIABLE DECLARATION */
int recordToGet = 10;

String order = " " + PstAppGroup.fieldNames[PstAppGroup.FLD_GROUP_NAME];

Vector listAppGroup = new Vector(1,1);
ControlLine ctrLine = new ControlLine();

/* GET REQUEST FROM HIDDEN TEXT */
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start"); 
long appGroupOID = FRMQueryString.requestLong(request,"group_oid");
int listCommand = FRMQueryString.requestInt(request, "list_command");
if(listCommand==Command.NONE)
 listCommand = Command.LIST;

CtrlAppGroup ctrlAppGroup = new CtrlAppGroup(request);
 
int vectSize = PstAppGroup.getCount(""); 

start = ctrlAppGroup.actionList(listCommand, start,vectSize,recordToGet);

listAppGroup = PstAppGroup.list(start,recordToGet, "" , order);

/* TO HANDLE CONDITION AFTER DELETE LAST, IF START LIMIT IS BIGGER THAN VECT SIZE, GET LIST FIRST */
if(((listAppGroup==null)||(listAppGroup.size()<1))){		
	start=0;
	listAppGroup = PstAppGroup.list(start,recordToGet, "" , order);
}
%>
<!-- End of JSP Block -->
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
<% if (privAdd){%>
function addNew(){
	document.frmAppGroup.group_oid.value="0";
	document.frmAppGroup.list_command.value="<%=listCommand%>";
	document.frmAppGroup.command.value="<%=Command.ADD%>";
	document.frmAppGroup.action="groupedit.jsp";
	document.frmAppGroup.submit();
}
 <%}%>
function cmdEdit(oid){
	document.frmAppGroup.group_oid.value=oid;
	document.frmAppGroup.list_command.value="<%=listCommand%>";
	document.frmAppGroup.command.value="<%=Command.EDIT%>";
	document.frmAppGroup.action="groupedit.jsp";
	document.frmAppGroup.submit();
}

function first(){
	document.frmAppGroup.command.value="<%=Command.FIRST%>";
	document.frmAppGroup.list_command.value="<%=Command.FIRST%>";
	document.frmAppGroup.action="grouplist.jsp";
	document.frmAppGroup.submit();
}
function prev(){
	document.frmAppGroup.command.value="<%=Command.PREV%>";
	document.frmAppGroup.list_command.value="<%=Command.PREV%>";
	document.frmAppGroup.action="grouplist.jsp";
	document.frmAppGroup.submit();
}

function next(){
	document.frmAppGroup.command.value="<%=Command.NEXT%>";
	document.frmAppGroup.list_command.value="<%=Command.NEXT%>";
	document.frmAppGroup.action="grouplist.jsp";
	document.frmAppGroup.submit();
}
function last(){
	document.frmAppGroup.command.value="<%=Command.LAST%>";
	document.frmAppGroup.list_command.value="<%=Command.LAST%>";
	document.frmAppGroup.action="grouplist.jsp";
	document.frmAppGroup.submit();
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
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->
		  <%=listTextGlobal[SESS_LANGUAGE][0]%> &gt; <%=listTextGlobal[SESS_LANGUAGE][1]%> &gt; <%=listTextGlobal[SESS_LANGUAGE][2]%><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frmAppGroup" method="post" action="">
              <input type="hidden" name="command" value="">
              <input type="hidden" name="group_oid" value="<%=appGroupOID%>">
              <input type="hidden" name="vectSize" value="<%=vectSize%>">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="list_command" value="<%=listCommand%>">
              <table width="100%" cellspacing="0" cellpadding="0">
                <% if ((listAppGroup!=null)&&(listAppGroup.size()>0)){ %>
                <tr> 
                  <td colspan="2" class="bigtitleflash">&nbsp; </td>
                </tr>
                <tr> 
                  <td colspan="2"><%=drawListAppGroup(SESS_LANGUAGE, start, listAppGroup)%></td>
                </tr>
                <%}else{%>
                <tr> 
                  <td colspan="2" height="20">&nbsp;</td>
                </tr>
                <tr> 
                  <td colspan="2"> 
                    <div class="comment">&nbsp;&nbsp;&nbsp;&nbsp;<%=listTextGlobal[SESS_LANGUAGE][4]%></div>
                  </td>
                </tr>
                <%}%>
              </table>
              <table width="100%">
                <tr> 
                  <td colspan="2"> <span class="command"> <%=ctrLine.drawMeListLimit(listCommand,vectSize,start,recordToGet,"first","prev","next","last","left")%> </span> </td>
                </tr>
                <% if (privAdd){%>
                <tr> 
                  <td colspan="2" class="command"> 
                    <table width="15%" border="0" cellspacing="2" cellpadding="3">
                      <tr> 
                        <td width="20%"><a href="javascript:addNew()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10011','','<%=approot%>/images/BtnNewOn.jpg',1)">
							<img name="Image10011" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,listTextGlobal[SESS_LANGUAGE][1],ctrLine.CMD_ADD,true)%>"></a>
						</td>
                        <td nowrap width="80%"><a href="javascript:addNew()" class="command"><%=ctrLine.getCommand(SESS_LANGUAGE,listTextGlobal[SESS_LANGUAGE][1],ctrLine.CMD_ADD,true)%></a></td>
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

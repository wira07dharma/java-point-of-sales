 
<%@ page language="java" %>
<%@ page import = "java.util.*,
                   com.dimata.gui.jsp.ControlList,
                   com.dimata.gui.jsp.ControlLine,
                   com.dimata.qdep.form.FRMQueryString,
                   com.dimata.util.Command,
                   com.dimata.gui.jsp.ControlDate,
                   com.dimata.posbo.entity.admin.AppPriv,
                   com.dimata.posbo.entity.admin.PstAppPriv,
                   com.dimata.posbo.form.admin.FrmAppPriv,
                   com.dimata.posbo.form.admin.CtrlAppPriv" %>
<%@ include file = "../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_ADMIN , AppObjInfo.G2_ADMIN_USER, AppObjInfo.OBJ_ADMIN_USER_PRIVILEGE); %>
<%@ include file = "../main/checkuser.jsp" %>


<%!
public static final String listTextGlobal[][] = {
	{"Managemen Pemakai","Hak Akses","Daftar","Edit","Nama Hak Akses","Deskripsi","Hak Akses Modul",
	 "Daftar Hak Akses Modul","Tidak Ada Hak Akses","Tambah","Ubah"},
	{"User Management","Privilege","List","Edit","Privilege Name","Description","Module Access",
	 "Module Access List","No Privilege Available","Add","Edit"}
};

public static final String listTextTableHeader[][] = {
	{"No.","Nama","Deskripsi","Tanggal Pembuatan"},
	{"No.","Name","Description","Creation Date"}
};

public String drawListAppPriv(int language, int start, Vector objectClass, long privId) {
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
	int index = -1;							
	for (int i = 0; i < objectClass.size(); i++) {
		 AppPriv appPriv = (AppPriv)objectClass.get(i);

		 Vector rowx = new Vector();
		 
		 if(privId == appPriv.getOID())
		 	index = i;
			
		 rowx.add("<div align=\"center\">"+String.valueOf(i+1)+"</div>");
		 rowx.add(String.valueOf(appPriv.getPrivName()));		 
		 rowx.add(String.valueOf(appPriv.getDescr()));
		 try{
			 Date regdate = appPriv.getRegDate();
			 regdatestr = Formater.formatDate(regdate, "dd MMMM yyyy");
		 }catch(Exception e){
			 regdatestr = "";
		 }
		 
		 rowx.add(regdatestr);
		 		 
		 lstData.add(rowx);
		 lstLinkData.add(String.valueOf(appPriv.getOID()));
	}						

	return ctrlist.draw(index);
}

%>
<%

/* VARIABLE DECLARATION */
int recordToGet = 10;
String order = " " + PstAppPriv.fieldNames[PstAppPriv.FLD_PRIV_NAME];

Vector listAppPriv = new Vector(1,1);
ControlLine ctrLine = new ControlLine();

/* GET REQUEST FROM HIDDEN TEXT */
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start"); 
long appPrivOID = FRMQueryString.requestLong(request,"appriv_oid");
int prevCommand = FRMQueryString.requestInt(request, "prev_command");
int prevSaveCommand = FRMQueryString.requestInt(request, "prev_save_command");

CtrlAppPriv ctrlAppPriv = new CtrlAppPriv(request);
FrmAppPriv frmAppPriv = ctrlAppPriv.getForm();
 
int vectSize = PstAppPriv.getCount("");

int excCode = ctrlAppPriv.action(iCommand,appPrivOID,start,vectSize,recordToGet);
vectSize = PstAppPriv.getCount(""); 
String msgString = ctrlAppPriv.getMessage(); 
AppPriv appPriv = ctrlAppPriv.getAppPriv();

if ((iCommand == Command.FIRST || iCommand == Command.PREV )||
	(iCommand == Command.NEXT || iCommand == Command.LAST))
		start = ctrlAppPriv.getStart();

if((iCommand == Command.SAVE)&&(frmAppPriv.errorSize()<1))
	start = PstAppPriv.findLimitStart(appPriv.getOID(),recordToGet,"", order);
		
order= PstAppPriv.fieldNames[PstAppPriv.FLD_PRIV_NAME] ;		
listAppPriv = PstAppPriv.list(start,recordToGet, "" , order);

/* TO HANDLE CONDITION AFTER DELETE LAST, IF START LIMIT IS BIGGER THAN VECT SIZE, GET LIST FIRST */
if(((listAppPriv==null)||(listAppPriv.size()<1))){		
	start=0;
	listAppPriv = PstAppPriv.list(start,recordToGet, "" , order);
}



%>
<!-- End of JSP Block -->
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
function addNew(){
	document.frmAppPriv.appriv_oid.value="0";
	document.frmAppPriv.prev_command.value="<%=prevCommand%>";
	document.frmAppPriv.command.value="<%=Command.ADD%>";
	document.frmAppPriv.action="privilegelist.jsp";
	document.frmAppPriv.submit();
}

function cmdEdit(oid){
	document.frmAppPriv.appriv_oid.value=oid;
	document.frmAppPriv.prev_command.value="<%=prevCommand%>";
	document.frmAppPriv.command.value="<%=Command.EDIT%>";
	document.frmAppPriv.action="privilegelist.jsp";
	document.frmAppPriv.submit();
}


function cmdSave(){
	document.frmAppPriv.command.value="<%=Command.SAVE%>";
	document.frmAppPriv.prev_command.value="<%=prevCommand%>";
	document.frmAppPriv.action="privilegelist.jsp";
	document.frmAppPriv.submit();
}

function cmdEditObj(oid){
	document.frmAppPriv.appriv_oid.value=oid;
	document.frmAppPriv.prev_command.value="<%=prevCommand%>";
	document.frmAppPriv.command.value="<%=Command.LIST%>";
	document.frmAppPriv.action="privilegeedit.jsp";
	document.frmAppPriv.submit();
}

function cmdAsk(oid){
	document.frmAppPriv.appriv_oid.value=oid;
	document.frmAppPriv.prev_command.value="<%=prevCommand%>";
	document.frmAppPriv.command.value="<%=Command.ASK%>";
	document.frmAppPriv.action="privilegelist.jsp";
	document.frmAppPriv.submit();
}
function cmdDelete(oid){
	document.frmAppPriv.appriv_oid.value=oid;
	document.frmAppPriv.prev_command.value="<%=prevCommand%>";
	document.frmAppPriv.command.value="<%=Command.DELETE%>";
	document.frmAppPriv.action="privilegelist.jsp";
	document.frmAppPriv.submit();
}

function cmdCancel(){
	document.frmAppPriv.prev_command.value="<%=prevCommand%>";
	document.frmAppPriv.command.value="<%=Command.EDIT%>";
	document.frmAppPriv.action="privilegelist.jsp";
	document.frmAppPriv.submit();
}


function cmdListFirst(){
	document.frmAppPriv.command.value="<%=Command.FIRST%>";
	document.frmAppPriv.prev_command.value="<%=Command.FIRST%>";
	document.frmAppPriv.action="privilegelist.jsp";
	document.frmAppPriv.submit();
}
function cmdListPrev(){
	document.frmAppPriv.command.value="<%=Command.PREV%>";
	document.frmAppPriv.prev_command.value="<%=Command.PREV%>";
	document.frmAppPriv.action="privilegelist.jsp";
	document.frmAppPriv.submit();
}

function cmdListNext(){
	document.frmAppPriv.command.value="<%=Command.NEXT%>";
	document.frmAppPriv.prev_command.value="<%=Command.NEXT%>";
	document.frmAppPriv.action="privilegelist.jsp";
	document.frmAppPriv.submit();
}
function cmdListLast(){
	document.frmAppPriv.command.value="<%=Command.LAST%>";
	document.frmAppPriv.prev_command.value="<%=Command.LAST%>";
	document.frmAppPriv.action="privilegelist.jsp";
	document.frmAppPriv.submit();
}

function cmdBack(){
	document.frmAppPriv.command.value="<%=Command.BACK%>";
	document.frmAppPriv.action="privilegelist.jsp";
	document.frmAppPriv.submit();
}

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

function showObjectForMenu(){
}

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
            <link rel="stylesheet" href="../css/default.css" type="text/css">
            <form name="frmAppPriv" method="post" action="">
              <input type="hidden" name="command" value="">
              <input type="hidden" name="appriv_oid" value="<%=appPrivOID%>">
              <input type="hidden" name="vectSize" value="<%=vectSize%>">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="prev_save_command" value="<%=prevSaveCommand%>">
              <table width="100%" cellspacing="0" cellpadding="0">
                <tr> 
                  <td colspan="3" class="bigtitleflash">&nbsp; </td>
                </tr>
              </table>
              <% if ((listAppPriv!=null)&&(listAppPriv.size()>0)){ %>
              <%=drawListAppPriv(SESS_LANGUAGE, start, listAppPriv, appPrivOID)%> 
              <%}%>
              <table width="100%">
                <tr> 
                  <td colspan="3"> <span class="command"> 
					<% 
					int cmd = 0;					  
					if ((iCommand == Command.FIRST || iCommand == Command.PREV )|| (iCommand == Command.NEXT || iCommand == Command.LAST))
						cmd =iCommand;								   
					else {
						if(iCommand == Command.NONE || prevCommand == Command.NONE)						  
							cmd=Command.FIRST;
						else {
							if((prevSaveCommand==Command.ADD)&&(iCommand==Command.SAVE)&&(frmAppPriv.errorSize()<1)) {
								cmd = Command.LAST;
							}						
							else {
								if((iCommand == Command.SAVE) && (frmAppPriv.errorSize()<1))
									cmd = PstAppPriv.findLimitCommand(start,recordToGet,vectSize); 
								else
									cmd = prevCommand;
							}
					
						}
					}
					ctrLine.setLocationImg(approot+"/images");
					ctrLine.initDefault();						   					   
					%>
                    <%=ctrLine.drawImageListLimit(cmd,vectSize,start,recordToGet)%> </span> </td>
                </tr>
                <%if(privAdd  && (iCommand!=Command.ADD)&&(iCommand!=Command.ASK)&&(iCommand!=Command.EDIT)&&(frmAppPriv.errorSize()<1)){%>
                <tr> 
                  <td colspan="3" class="command"> 
                    <table width="15%" border="0" cellspacing="2" cellpadding="3">
                      <tr> 
                        <td width="20%"><a href="javascript:addNew()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100111','','<%=approot%>/images/BtnNewOn.jpg',1)">
							<img name="Image100111" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,listTextGlobal[SESS_LANGUAGE][1],ctrLine.CMD_ADD,true)%>"></a>
						</td>
                        <td nowrap width="80%"><a href="javascript:addNew()" class="command"><%=ctrLine.getCommand(SESS_LANGUAGE,listTextGlobal[SESS_LANGUAGE][1],ctrLine.CMD_ADD,true)%></a></td>
                      </tr>
                    </table>
                  </td>
                </tr>
                <%}%>
                <tr>
                  <td colspan="3">&nbsp;</td>
                </tr>
                <%if(((iCommand==Command.SAVE)&&(frmAppPriv.errorSize()>0))||(iCommand==Command.ADD)||(iCommand==Command.EDIT)||(iCommand==Command.ASK)){%>
                <tr> 
                  <td colspan="3" class="listtitle">
				   <%= appPrivOID == 0 ? listTextGlobal[SESS_LANGUAGE][9]+" "+listTextGlobal[SESS_LANGUAGE][1] : listTextGlobal[SESS_LANGUAGE][10]+" "+listTextGlobal[SESS_LANGUAGE][1]%>
				  </td>
                </tr>
                <tr> 
                  <td colspan="3">&nbsp;</td>
                </tr>
                <tr> 
                  <td width="15%" valign="top"><strong><%=listTextTableHeader[SESS_LANGUAGE][1]%></strong></td>
				  <td width="2%" valign="top"><strong>:</strong></td>
                  <td width="83%">
                    <input type="text" name="<%=frmAppPriv.fieldNames[frmAppPriv.FRM_PRIV_NAME] %>" value="<%=appPriv.getPrivName()%>" class="formElemen" size="30">
                    * &nbsp;<%= frmAppPriv.getErrorMsg(frmAppPriv.FRM_PRIV_NAME) %>
				  </td>
                </tr>
                <tr> 
                  <td width="15%" valign="top"><strong><%=listTextTableHeader[SESS_LANGUAGE][2]%></strong></td>
				  <td width="2%" valign="top"><strong>:</strong></td>
                  <td width="83%">
                    <textarea name="<%=frmAppPriv.fieldNames[frmAppPriv.FRM_DESCRIPTION] %>" cols="45" rows="3" class="formElemen"><%=appPriv.getDescr()%></textarea>
                  </td>
                </tr>
                <tr> 
                  <td width="15%" valign="top" height="14"><strong><%=listTextTableHeader[SESS_LANGUAGE][3]%></strong></td>
				  <td width="2%" valign="top"><strong>:</strong></td>
                  <td width="83%"><%=ControlDate.drawDate(frmAppPriv.fieldNames[FrmAppPriv.FRM_REG_DATE], appPriv.getRegDate(), 0, -30)%> </td>
                </tr>
                <tr> 
                  <td colspan="3" class="command">&nbsp;</td>
                </tr>
                <tr> 
                  <td colspan="3" class="command" height="22"> 
                    <%
						String cmdTitle = listTextGlobal[SESS_LANGUAGE][1];
						
						ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,cmdTitle,ctrLine.CMD_SAVE,true));
						ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,cmdTitle,ctrLine.CMD_ASK,true));
						ctrLine.setBackImageAlt(ctrLine.getCommand(SESS_LANGUAGE,cmdTitle,ctrLine.CMD_BACK,true));							
						ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,cmdTitle,ctrLine.CMD_CANCEL,false));
						
						ctrLine.setLocationImg(approot+"/images");
						ctrLine.initDefault();
						ctrLine.setTableWidth("80%");
						ctrLine.setCommandStyle("command");
						ctrLine.setColCommStyle("command");
						String scomDel = "javascript:cmdAsk('"+appPrivOID+"')";
						String sconDelCom = "javascript:cmdDelete('"+appPrivOID+"')";
						String scancel = "javascript:cmdCancel('"+appPrivOID+"')";
						
						ctrLine.setBackCaption(ctrLine.getCommand(SESS_LANGUAGE,cmdTitle,ctrLine.CMD_BACK,true));
						ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE,cmdTitle,ctrLine.CMD_SAVE,true));
						ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE,cmdTitle,ctrLine.CMD_ASK,true));
						ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE,cmdTitle,ctrLine.CMD_DELETE,true));
						ctrLine.setAddCaption(ctrLine.getCommand(SESS_LANGUAGE,cmdTitle,ctrLine.CMD_ADD,true));
						ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE,cmdTitle,ctrLine.CMD_CANCEL,false));

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
                    <%= ctrLine.drawImage(iCommand, excCode, msgString)%>
				  </td>
                </tr>
                <% if((privAdd && privUpdate) && (iCommand != Command.ASK || iCommand == Command.DELETE) && (appPrivOID != 0)){%>
                <tr> 
                  <td colspan="3" class="command"> 
                    <table width="15%" border="0" cellspacing="2" cellpadding="2">
                      <tr> 
                        <td width="20%"><a href="javascript:cmdEditObj('<%=appPrivOID%>')" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10011','','<%=approot%>/images/BtnEditOn.jpg',1)">
							<img name="Image10011" border="0" src="<%=approot%>/images/BtnEdit.jpg" width="24" height="24" alt="<%=listTextGlobal[SESS_LANGUAGE][10]+" "+listTextGlobal[SESS_LANGUAGE][6]%>"></a>
						</td>
                        <td nowrap width="80%"><a href="javascript:cmdEditObj('<%=appPrivOID%>')" class="command"><%=listTextGlobal[SESS_LANGUAGE][10]+" "+listTextGlobal[SESS_LANGUAGE][6]%></a></td>
                      </tr>
                    </table>
                  </td>
                </tr>
                <%}%>
                <%}%>
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

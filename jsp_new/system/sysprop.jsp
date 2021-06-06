<%@ page language="java" %>

<%@ page import="java.util.*,
                 com.dimata.common.form.system.CtrlSystemProperty,
                 com.dimata.common.form.system.FrmSystemProperty,
                 com.dimata.common.entity.system.SystemProperty,
                 com.dimata.common.entity.system.PstSystemProperty,
                 com.dimata.common.session.system.SessSystemProperty" %>
<%@ page import="com.dimata.util.*" %>
<%@ page import="com.dimata.gui.jsp.*" %>
<%@ page import="com.dimata.qdep.form.*" %>
<%//@ page import="com.dimata.system.entity.*" %>
<%//@ page import="com.dimata.system.form.*" %>
<%//@ page import="com.dimata.system.session.*" %>

<%@ include file = "../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_ADMIN, AppObjInfo.G2_ADMIN_SYSTEM, AppObjInfo.OBJ_ADMIN_SYSTEM_APP_SET); %>
<%@ include file = "../main/checkuser.jsp" %>



<%!
public static final String textListTitleHeader[][]=
{
	{"Sistem","Seting Aplikasi","Nama","Nilai","Keterangan","Update Nilai","Batal","Load Nilai Terbaru","Keterangan :", 
	 "Gunakan karakter \"\\\\\" jika anda ingin menginput karakter \"\\\" dalam bagian nilai.",
	 "Klik \"Load Nilai Terbaru\" untuk mengaktifkan nilai variabel terbaru.","Untuk melihat daftar setting yang harus di install klik <a href=\"javascript:cmdhelp()\"> Daftar Setting</a>"},
	{"System","Application Setting","Name","Value","Description","Update Value","Canceled","Load the Newest Value","Note :",
	 "Use character \"\\\\\" if you want to input character \"\\\" into part of value.",
	  "Click \"Load the Newest Value\" to active the Newest Variable Value.","Untuk melihat daftar setting yang harus di install klik <a href=\"javascript:cmdhelp()\"> Daftar Setting</a>"}
};
%>

<% 
int iCommand = FRMQueryString.requestCommand(request);
long lOid = FRMQueryString.requestLong(request, "oid");
CtrlSystemProperty ctrlSystemProperty = new CtrlSystemProperty(request);
ctrlSystemProperty.action(iCommand, lOid, request);

SystemProperty sysProp = ctrlSystemProperty.getSystemProperty();
FrmSystemProperty frmSystemProperty = ctrlSystemProperty.getForm();
%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language=javascript>
function cmdList(){
  document.frmData.command.value="<%= Command.LIST %>";          
  document.frmData.action = "sysprop.jsp";
  document.frmData.submit();
}

function cmdhelp(){
	window.open("settingproperty.jsp","","");
}

function cmdEdit(oid){
  <%if(privUpdate){%>
  document.frmData.command.value="<%= Command.EDIT %>";                    
  document.frmData.oid.value = oid;
  document.frmData.action = "syspropnew.jsp";
  document.frmData.submit();
  <%}%>
}	

function cmdAssign(oid){
  <%if(privUpdate){%>	
  document.frmData.command.value="<%= Command.ASSIGN %>";       
  document.frmData.oid.value= oid;          
  document.frmData.action = "sysprop.jsp";
  document.frmData.submit();
  <%}%>
}

<%if((privAdd)&&(privDelete)){%>
function cmdLoad(){
  document.frmData.command.value="<%= Command.LOAD %>";          
  document.frmData.action = "sysprop.jsp";
  document.frmData.submit();
}

function cmdNew(){
  document.frmData.command.value="<%= Command.ADD %>";          
  document.frmData.action = "syspropnew.jsp";
  document.frmData.submit();
}

function cmdUpdate(oid){
  document.frmData.command.value="<%= Command.UPDATE %>";          
  document.frmData.oid.value = oid;
  document.frmData.action = "sysprop.jsp";
  document.frmData.submit();
}
<%}%>
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
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->&nbsp;<%=textListTitleHeader[SESS_LANGUAGE][0]%>
            &gt; <%=textListTitleHeader[SESS_LANGUAGE][1]%><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frmData" method="post" action="">
              <input type="hidden" name="command" value="0">
              <input type="hidden" name="oid" value="<%= lOid %>">
              <table width="100%" border="0" cellspacing="2" cellpadding="0">
                <tr> 
                  <td>&nbsp; 
                  </td>
                </tr>
                <tr> 
                  <td>
					Tipe : 
					  <%			  
					String cbxName = FrmSystemProperty.fieldNames[FrmSystemProperty.FRM_GROUP];			
					String groupName = FRMQueryString.requestString(request, cbxName);
	
					if(groupName==null || groupName.equals("")){
					   groupName = "";
					} 
	
					Vector grs = Validator.toVector(SessSystemProperty.groups, SessSystemProperty.subGroups, "", "", true);
					Vector val = Validator.toVector(SessSystemProperty.groups, SessSystemProperty.subGroups, "", "", false);
					String attr = "onChange=\"javacript:cmdList()\"";
					out.println("&nbsp;&nbsp;&nbsp;"+ControlCombo.draw(cbxName, null, groupName, val, grs, attr));			
				    %>

                    <%
					ControlList ctrlList = new ControlList();
					ctrlList.setListStyle("listgen");
					ctrlList.setTitleStyle("listgentitle");
					ctrlList.setCellStyle("listgensell");
					ctrlList.setHeaderStyle("listgentitle");
				
					ctrlList.addHeader(textListTitleHeader[SESS_LANGUAGE][2],"20%");
					ctrlList.addHeader(textListTitleHeader[SESS_LANGUAGE][3],"25%");
					ctrlList.addHeader(textListTitleHeader[SESS_LANGUAGE][4],"55%");
				
					ctrlList.setLinkRow(0);
					ctrlList.setLinkSufix("");
				  %>
                  </td>
                </tr>
                <tr> 
                  <td> 
                    <%
						String editValPre = "<input type=\"text\" name=\"" + FrmSystemProperty.fieldNames[FrmSystemProperty.FRM_VALUE] +"\" size=\"20\" value=\"";
						String editValSup = "\"> * "+ frmSystemProperty.getErrorMsg(FrmSystemProperty.FRM_NAME);
		
						Vector lstData 		= ctrlList.getData();
						Vector lstLinkData 	= ctrlList.getLinkData();
		
						Vector vctData = new Vector();
						try {
							vctData = PstSystemProperty.listByGroup(groupName);
		
							ctrlList.setLinkPrefix("javascript:cmdAssign('");					
							ctrlList.setLinkSufix("')");
							ctrlList.reset();
		
							if (vctData != null) {  
								for (int i = 0; i < vctData.size(); i++) {
									 Vector rowx = new Vector();
		
									 SystemProperty sysProp2 = (SystemProperty)vctData.get(i);
		
									 rowx.add(sysProp2.getName());
		
									 if(iCommand == Command.ASSIGN && lOid == sysProp2.getOID()) {
										rowx.add(editValPre + sysProp2.getValue() + editValSup);
									 }else{
										rowx.add(sysProp2.getValue());								
									 }
		
									 rowx.add(sysProp2.getNote());
		
									 lstData.add(rowx); 
									 lstLinkData.add(String.valueOf(sysProp2.getOID()));
								}
							}
						} catch(Exception e) {
							System.out.println("Exc : " + e.toString());
						} 
						out.println(ctrlList.draw());
						%>
                  </td>
                </tr>
                <tr>
                  <td align="right"> 
                    <div align="right"><%= "<i>" + ctrlSystemProperty.getMessage() + "</i>" %> </div>
                  </td>
                </tr>
                <tr> 
                  <td align="right"> 
                    <%if((privAdd)&&(privUpdate)&&(privDelete)){%>
                    <div align="right"> 
                      <% 
							if(iCommand == Command.ASSIGN) {
							out.println("<a href=\"javascript:cmdUpdate('"+ lOid +"')\">"+textListTitleHeader[SESS_LANGUAGE][5]+"</a> | <a href='javascript:cmdList()'>"+textListTitleHeader[SESS_LANGUAGE][6]+"</a>");
							}else{
							out.println("<a href=\"javascript:cmdNew()\">New Data</a> | <a href=\"javascript:cmdLoad()\">"+textListTitleHeader[SESS_LANGUAGE][7]+"</a>&nbsp;");
                                                        }
						 %>
                      <%}%>
                    </div>
                  </td>
                </tr>
                <tr> 
                  <td align="left">&nbsp;<%=textListTitleHeader[SESS_LANGUAGE][8]%> <br>
                    &nbsp;- <%=textListTitleHeader[SESS_LANGUAGE][9]%><br>
                    &nbsp;- <%=textListTitleHeader[SESS_LANGUAGE][10]%><br>
                    &nbsp;- <%=textListTitleHeader[SESS_LANGUAGE][11]%></td>
                </tr>
                <tr>
                  <td align="left">&nbsp;</td>
                </tr>
              </table>
            </form>
            <%//@ include file = "../main/useraccount.jsp" %>
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
<!-- endif of "has access" condition -->

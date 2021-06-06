<%-- 
    Document   : master_type.jsp
    Created on : Mar 7, 2014, 2:23:58 AM
    Author     : Fitora
--%>

<%@page import="com.dimata.gui.jsp.ControlCombo"%>
<%@page import="com.dimata.hanoman.form.masterdata.FrmContact"%>
<%@page import="com.dimata.hanoman.form.masterdata.FrmContact"%>
<%@page import="com.dimata.hanoman.entity.masterdata.MasterGroup"%>
<%@page import="com.dimata.common.entity.location.Negara"%>
<%@page import="com.dimata.hanoman.entity.masterdata.PstMasterGroup"%>
<%@page import="com.dimata.hanoman.form.masterdata.CtrlMasterType"%>

<%@page import="com.dimata.gui.jsp.ControlLine"%>
<%@page import="com.dimata.qdep.form.FRMMessage"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.gui.jsp.ControlList"%>
<%@page import="com.dimata.hanoman.form.masterdata.FrmMasterType"%>
<%@page import="com.dimata.hanoman.form.masterdata.FrmMasterGroup"%>
<%@page import="com.dimata.hanoman.entity.masterdata.MasterType"%>
<%@page import="com.dimata.hanoman.entity.masterdata.PstMasterType"%>
<%@page import="com.dimata.util.Command"%>
<%@ include file = "../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_MASTER_TYPE); %>
<%@ include file = "../main/checkuser.jsp" %>

<%

/* Check privilege except VIEW, view is already checked on checkuser.jsp as basic access*/

//boolean privAdd=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_ADD)); 

//boolean privView=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_VIEW));

//boolean privUpdate=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_UPDATE));

//boolean privDelete=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_DELETE));

%>

<!-- Jsp Block -->

<%!
    
    public static final String textListTitleHeader[][] =
    {
        {"Tambah Master Group","Kembali Ke Menu","Tipe Grup","Daftar Master Group","Kode Master","Nama Master","Keterangan"},
        {"Add New","Back To Menu","Type Group","Master Group Item List","Master Code","Master Name","Description"}
    };

    public static final String textListTitleTable[][] =
    {
        {"Tambah Master Group","Kembali Ke Menu","Tipe Grup","Daftar Master Group"},
        {"Add New","Back To Menu","Type Group","Master Group Item List"}
    };



	public String drawList(int iCommand,FrmMasterType frmObject, MasterType objEntity, Vector objectClass,  long masterTypeId, long oidMaster,int language)



	{

		ControlList ctrlist = new ControlList();

		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");	

		ctrlist.addHeader(""+textListTitleHeader[language][4]+"","20%");

		ctrlist.addHeader(""+textListTitleHeader[language][5]+"","30%");

		ctrlist.addHeader(""+textListTitleHeader[language][6]+"","50%");



		ctrlist.setLinkRow(0);

		ctrlist.setLinkSufix("");

		Vector lstData = ctrlist.getData();

		Vector lstLinkData = ctrlist.getLinkData();

		Vector rowx = new Vector(1,1);

		ctrlist.reset();

		int index = -1;

                String strWhere = "";
                strWhere = PstMasterType.fieldNames[PstMasterType.FLD_TYPE_GROUP] +" = "  + oidMaster;
                Vector master = PstMasterGroup.list(0,0,strWhere,"");
                
               
                

		for (int i = 0; i < objectClass.size(); i++) {

			 MasterType masterType = (MasterType)objectClass.get(i);

			 rowx = new Vector();

			 if(masterTypeId == masterType.getOID())

				 index = i; 



			 if(index == i && (iCommand == Command.EDIT || iCommand == Command.ASK)){

					

				rowx.add("<input type=\"text\" name=\""+frmObject.fieldNames[FrmMasterType.FRM_FIELD_MASTER_CODE] +"\" value=\""+masterType.getMasterCode()+"\" class=\"formElemen\" size=\"10\">");

				rowx.add("<input type=\"text\" name=\""+frmObject.fieldNames[FrmMasterType.FRM_FIELD_MASTER_NAME] +"\" value=\""+masterType.getMasterName()+"\" class=\"formElemen\">");

				rowx.add("<input type=\"text\" name=\""+frmObject.fieldNames[FrmMasterType.FRM_FIELD_DESCRIPTION] +"\" value=\""+masterType.getDescription()+"\" class=\"formElemen\" size=\"35\">");

				//rowx.add("<textarea name=\""+frmObject.fieldNames[FrmMasterType.FRM_FIELD_DESCRIPTION] +"\"  class=\"formElemen\">"+masterType.getDescription()+"</textarea>");

			}else{

				rowx.add("<a href=\"javascript:cmdEdit('"+String.valueOf(masterType.getOID())+"')\">"+masterType.getMasterCode()+"</a>");

				rowx.add(masterType.getMasterName());
                                

				rowx.add(masterType.getDescription());

			} 



			lstData.add(rowx);

		}



		 rowx = new Vector();



		if(iCommand == Command.ADD || (iCommand == Command.SAVE && frmObject.errorSize() > 0)){ 

				rowx.add("<input type=\"text\" name=\""+frmObject.fieldNames[FrmMasterType.FRM_FIELD_MASTER_CODE] +"\" value=\""+objEntity.getMasterCode()+"\" class=\"formElemen\" size=\"10\">");

				rowx.add("<input type=\"text\" name=\""+frmObject.fieldNames[FrmMasterType.FRM_FIELD_MASTER_NAME] +"\" value=\""+objEntity.getMasterName()+"\" class=\"formElemen\">");

				rowx.add("<input type=\"text\" name=\""+frmObject.fieldNames[FrmMasterType.FRM_FIELD_DESCRIPTION] +"\" value=\""+objEntity.getDescription()+"\" class=\"formElemen\" size=\"35\">");

				//rowx.add("<textarea name=\""+frmObject.fieldNames[FrmMasterType.FRM_FIELD_DESCRIPTION] +"\"  class=\"formElemen\">"+objEntity.getDescription()+"</textarea>");



		}



		lstData.add(rowx);



		return ctrlist.draw();

	}



%>

<%

int iCommand = FRMQueryString.requestCommand(request);

int start = FRMQueryString.requestInt(request, "start");

int prevCommand = FRMQueryString.requestInt(request, "prev_command");

long oidMasterType = FRMQueryString.requestLong(request, "hidden_master_type_id");

int group = FRMQueryString.requestInt(request, FrmMasterGroup.fieldNames[FrmMasterGroup.FRM_FIELD_TYPE_GROUP]);

int oidMaster = FRMQueryString.requestInt(request, FrmMasterGroup.fieldNames[FrmMasterGroup.FRM_FIELD_TYPE_GROUP]);

/*variable declaration*/

int recordToGet = 20;



String msgString = "";

int iErrCode = FRMMessage.NONE;

String whereClause = PstMasterType.fieldNames[PstMasterType.FLD_TYPE_GROUP]+"="+group;

String orderClause = "";



CtrlMasterType ctrlMasterType = new CtrlMasterType(request);

ControlLine ctrLine = new ControlLine();

Vector listMasterType = new Vector(1,1);



/*switch statement */

iErrCode = ctrlMasterType.action(iCommand , oidMasterType, group);

/* end switch*/

FrmMasterType frmMasterType = ctrlMasterType.getForm();

//FrmMasterGroup frmMasterGroup = ctrlMasterGroup.getForm();

/*count list All MasterType*/

//Vector listAllMasterType = PstMasterType.listAll();

Vector listAllMasterType = PstMasterType.list(0,0, whereClause, null);

int vectSize = listAllMasterType.size();





/*switch list MasterType*/

if((iCommand == Command.FIRST || iCommand == Command.PREV )||

  (iCommand == Command.NEXT || iCommand == Command.LAST)){

		start = ctrlMasterType.actionList(iCommand, start, vectSize, recordToGet);

 } 

/* end switch list*/



MasterType masterType = ctrlMasterType.getMasterType();

msgString =  ctrlMasterType.getMessage();


 String strWhere = "";
                strWhere = PstMasterType.fieldNames[PstMasterType.FLD_TYPE_GROUP] +" = "  + group;
                Vector master = PstMasterGroup.list(0,0,strWhere,"");
/* get record to display */

listMasterType = PstMasterType.list(start,recordToGet, strWhere , orderClause);



/*handle condition if size of record to display = 0 and start > 0 	after delete*/

if (listMasterType.size() < 1 && start > 0)

{

	 if (vectSize - recordToGet > recordToGet)

			start = start - recordToGet;   //go to Command.PREV

	 else{

		 start = 0 ;

		 iCommand = Command.FIRST;

		 prevCommand = Command.FIRST; //go to Command.FIRST

	 }

	 listMasterType = PstMasterType.list(start,recordToGet, whereClause , orderClause);

}

%>

<html><!-- #BeginTemplate "/Templates/main_s_3.dwt" -->

<head>

<!-- #BeginEditable "doctitle" --> 

<title>master type</title>

<script language="JavaScript">

function showdetail(oidreg){
    window.open("<%=approot%>/servlet/com.dimata.posbo.report.masterdata.PersonalDiscountPrintPDF?hidden_member_id="+oidreg+"&approot=<%=approot%>&sess_language=<%=SESS_LANGUAGE%>");
}


function cmdAdd(){

	document.frmmastertype.hidden_master_type_id.value="0";

	document.frmmastertype.command.value="<%=Command.ADD%>";

	document.frmmastertype.prev_command.value="<%=prevCommand%>";

	document.frmmastertype.action="master_type.jsp";

	document.frmmastertype.submit();

}



function cmdAsk(oidMasterType){

	document.frmmastertype.hidden_master_type_id.value=oidMasterType;

	document.frmmastertype.command.value="<%=Command.ASK%>";

	document.frmmastertype.prev_command.value="<%=prevCommand%>";

	document.frmmastertype.action="master_type.jsp";

	document.frmmastertype.submit();

}



function cmdConfirmDelete(oidMasterType){

	document.frmmastertype.hidden_master_type_id.value=oidMasterType;

	document.frmmastertype.command.value="<%=Command.DELETE%>";

	document.frmmastertype.prev_command.value="<%=prevCommand%>";

	document.frmmastertype.action="master_type.jsp";

	document.frmmastertype.submit();

}



function cmdSave(){

	document.frmmastertype.command.value="<%=Command.SAVE%>";

	document.frmmastertype.prev_command.value="<%=prevCommand%>";

	document.frmmastertype.action="master_type.jsp";

	document.frmmastertype.submit();

}



function cmdEdit(oidMasterType){

	document.frmmastertype.hidden_master_type_id.value=oidMasterType;

	document.frmmastertype.command.value="<%=Command.EDIT%>";

	document.frmmastertype.prev_command.value="<%=prevCommand%>";

	document.frmmastertype.action="master_type.jsp";

	document.frmmastertype.submit();

}



function cmdCancel(oidMasterType){

	document.frmmastertype.hidden_master_type_id.value=oidMasterType;

	document.frmmastertype.command.value="<%=Command.EDIT%>";

	document.frmmastertype.prev_command.value="<%=prevCommand%>";

	document.frmmastertype.action="master_type.jsp";

	document.frmmastertype.submit();

}



function cmdBack(){

	document.frmmastertype.command.value="<%=Command.BACK%>";

	document.frmmastertype.action="master_type.jsp";

	document.frmmastertype.submit();

}



function cmdListFirst(){

	document.frmmastertype.command.value="<%=Command.FIRST%>";

	document.frmmastertype.prev_command.value="<%=Command.FIRST%>";

	document.frmmastertype.action="master_type.jsp";

	document.frmmastertype.submit();

}



function cmdListPrev(){

	document.frmmastertype.command.value="<%=Command.PREV%>";

	document.frmmastertype.prev_command.value="<%=Command.PREV%>";

	document.frmmastertype.action="master_type.jsp";

	document.frmmastertype.submit();

}



function cmdListNext(){

	document.frmmastertype.command.value="<%=Command.NEXT%>";

	document.frmmastertype.prev_command.value="<%=Command.NEXT%>";

	document.frmmastertype.action="master_type.jsp";

	document.frmmastertype.submit();

}



function cmdListLast(){

	document.frmmastertype.command.value="<%=Command.LAST%>";

	document.frmmastertype.prev_command.value="<%=Command.LAST%>";

	document.frmmastertype.action="master_type.jsp";

	document.frmmastertype.submit();

}



function cmdChange(){

	document.frmmastertype.command.value="<%=Command.LIST%>";

	document.frmmastertype.action="master_type.jsp";

	document.frmmastertype.submit();

}



function backMenu(){

	document.frmmastertype.action="<%=approot%>/menuapp/menu_page.jsp?menu=masterdata";

	document.frmmastertype.submit();

}



//-------------- script form image -------------------



function cmdDelPict(oidMasterType){

	document.frmimage.hidden_master_type_id.value=oidMasterType;

	document.frmimage.command.value="<%=Command.POST%>";

	document.frmimage.action="master_type.jsp";

	document.frmimage.submit();

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

<body bgcolor="#FFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
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
           
        </tr>
        <tr>
          <td><!-- #BeginEditable "content" -->
              
               <form name="frmmastertype" method ="post" action="">

              <input type="hidden" name="command" value="<%=iCommand%>">

              <input type="hidden" name="vectSize" value="<%=vectSize%>">

              <input type="hidden" name="start" value="<%=start%>">

              <input type="hidden" name="prev_command" value="<%=prevCommand%>">

              <input type="hidden" name="hidden_master_type_id" value="<%=oidMasterType%>">
              
              <table width="100%" border="0" cellspacing="0" cellpadding="1">

                <tr>

                  <td width="9%">&nbsp;</td>

                

                    <table width="100%" border="0" cellspacing="0" cellpadding="0">

                      <tr align="left" valign="top"> 

                        <td height="8"  colspan="3"> 

                          <table width="100%" border="0" cellspacing="0" cellpadding="0">

                            <tr align="left" valign="top"> 

                              <td height="8" valign="middle" colspan="3">&nbsp;</td>

                            </tr>

                            <tr align="left" valign="top"> 

                              <td height="8" valign="middle" colspan="3">&nbsp;<b class="listtitle"><%= textListTitleHeader[SESS_LANGUAGE][2] %></b>&nbsp;&nbsp; 

                               

                                                                                                                  <%    Vector neg_value = new Vector(1, 1);
                                                                                                                        Vector neg_key = new Vector(1, 1);
                                                                                                                        int limitStarted = 1;
                                                                                                                        neg_value.add("-1");
                                                                                                                        neg_key.add("select ...");
                                                                                                                        //String strWhere = "";
                                                                                                                        //strWhere = PstMasterType.fieldNames[PstMasterType.FLD_TYPE_GROUP] +" = "  + oidMaster;
                                                                                                                       Vector listNeg = PstMasterGroup.list(0, 0, "", "");
                                                                                                                        for (int i = 0; i < listNeg.size(); i++) {
                                                                                                                            MasterGroup mg = (MasterGroup) listNeg.get(i);
                                                                                                                            neg_key.add(mg.getNamaGroup()); 
                                                                                                                            neg_value.add(""+mg.getTypeGroup());  
                                                                                                                        }
                                                                                                                        
                                                                                                                      
                                                                                                                           
                                                                                                                        %>
                                                                                                                        <%= ControlCombo.draw(FrmMasterGroup.fieldNames[FrmMasterGroup.FRM_FIELD_TYPE_GROUP], "formElemen", null, "" + group, neg_value, neg_key, "OnChange=\"javascript:cmdChange()\"" )%> 

                             

                                &nbsp;&nbsp;<a href="javascript:cmdChange()"></a> 

                              </td>

                            </tr>
                            
                            
                            
                           

                            <tr align="left" valign="top"> 

                              <td height="27" valign="middle" colspan="3">&nbsp; </td>

                            </tr>

                            <tr align="left" valign="top"> 

                              <td height="14" valign="middle" colspan="3" class="comment"><b>&nbsp; <%= textListTitleHeader[SESS_LANGUAGE][3] %></b></td>

                            </tr>

                            <%

							try{

							%>

                            <tr align="left" valign="top"> 

                              <td height="22" valign="middle" colspan="3"> <%= drawList(iCommand,frmMasterType, masterType,listMasterType,oidMasterType,oidMaster,SESS_LANGUAGE)%> </td>

                            </tr>

                            <% 

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

							<%if(iCommand!=Command.ADD && iCommand!=Command.EDIT && iCommand!=Command.ASK && 

							  iErrCode==FRMMessage.NONE){%>

                            <tr align="left" valign="top"> 

                              <td height="22" valign="middle" colspan="3">

							   

                                            <table width="51%" border="0" cellspacing="2" cellpadding="3">
                                                <br>
                                              <tr>

								   <% if(privAdd){%>

                                                <!--td width="8%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="Add New Item Group"></a></td-->

                                                <td nowrap width="21%"><a class="btn btn-lg btn-primary" href="javascript:cmdAdd()" class="command"><i class="fa fa-plus-circle"></i> <%= textListTitleHeader[SESS_LANGUAGE][0]%></a></td>

									  <%}%>

                                                <!--td width="8%"><a href="javascript:backMenu()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="Back To Master Data Management Menu"></a></td-->

                                                <td nowrap width="63%"><a class="btn btn-lg btn-primary" href="javascript:backMenu()" class="command"><i class="fa fa-arrow-circle-left"></i> <%= textListTitleHeader[SESS_LANGUAGE][1]%></a></td>

                                  </tr>

                                         </table>

								

                              </td>

                            </tr>

							<%}%>

                            

                          </table>

                        </td>

                </tr>

                <tr align="left" valign="top"> 

                  <td height="8" valign="middle" width="17%">&nbsp;</td>

                  <td height="8" colspan="2" width="83%">&nbsp; </td>

                </tr>

                <tr align="left" valign="top" > 

                  <td colspan="3" class="command"> 

                    <%

									ctrLine.setLocationImg(approot+"/images");

									ctrLine.initDefault();

									ctrLine.setTableWidth("80%");

									String scomDel = "javascript:cmdAsk('"+oidMasterType+"')";

									String sconDelCom = "javascript:cmdConfirmDelete('"+oidMasterType+"')";

									String scancel = "javascript:cmdEdit('"+oidMasterType+"')";

									ctrLine.setBackCaption("Back to List");

									ctrLine.setCommandStyle("command");
                                                                        
                                                                        ctrLine.setColCommStyle("command");

									ctrLine.setSaveCaption("Save Data");

									ctrLine.setDeleteCaption("Delete Data");



									if (privDelete){

										ctrLine.setConfirmDelCommand(sconDelCom);

										ctrLine.setDeleteCommand(scomDel);

										ctrLine.setEditCommand(scancel);

									}else{ 

										ctrLine.setConfirmDelCaption("");

										ctrLine.setDeleteCaption("");

										ctrLine.setEditCaption("");

									}



									if(iCommand == Command.EDIT  && privUpdate == false){

										ctrLine.setSaveCaption("");

									}



									if (privAdd == false){

										ctrLine.setAddCaption("");

									}

									%>

                     </td>

                </tr>

              </table>

			  <%if((!(iCommand==Command.SAVE && iErrCode==FRMMessage.NONE)) && (!(iCommand==Command.DELETE && iErrCode==FRMMessage.NONE))){%>

			  <%= ctrLine.drawImage(iCommand, iErrCode, msgString)%>

			  <%}%>

			  

			

                  <td width="9%">&nbsp;</td>

  </tr>

</table>

			  

              

            </form>

            <!-- #EndEditable --> </td>

				  </tr>

				</table>

                  

                </td>

              </tr>

            </table></td>

        </tr>

      </table></td>

  

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


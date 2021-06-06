<%-- 
    Document   : master_group_mapping
    Created on : Nov 20, 2019, 1:50:33 PM
    Author     : Regen
--%>
<%@page import="com.dimata.posbo.form.masterdata.CtrlMasterGroupMapping"%>
<%@page import="com.dimata.posbo.form.masterdata.FrmMasterGroupMapping"%>
<%@page import="com.dimata.gui.jsp.ControlCombo"%>
<%@page import="com.dimata.posbo.entity.masterdata.MasterGroupMapping"%>
<%@page import="com.dimata.hanoman.entity.masterdata.MasterGroup"%>
<%@page import="com.dimata.hanoman.entity.masterdata.PstMasterGroup"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMasterGroupMapping"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.gui.jsp.ControlLine"%>
<%@page import="com.dimata.gui.jsp.ControlList"%>
<%@page import="com.dimata.hanoman.form.masterdata.FrmMasterGroup"%>
<%@page import="com.dimata.qdep.form.FRMMessage"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.posbo.entity.admin.AppObjInfo"%>
<%@ include file ="../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK_REPORT, AppObjInfo.OBJ_STOCK_POTITION); %>
<%@ include file ="../main/checkuser.jsp" %>
<%!  
public static final String textListGlobal[][] = {
{"Master Data", "Group Mapping", "Add Group Mapping", "Edit Group Mapping", "Delete Group Mapping", "Save Group Mapping"},
{"Master Data", "Group Mapping", "Add Group Mapping", "Edit Group Mapping", "Delete Group Mapping", "Save Group Mapping"}
};
public static final String textListHeader[][] = {};
public static final String textHeaderTable[][] = {
{"No", "Group Name", "Modul Group"},
{"No", "Group Name", "Modul Group"}
};
/* this method used to list material unit */
public String drawList(int language,int iCommand,FrmMasterGroupMapping frmObject,MasterGroupMapping objEntity,Vector objectClass,long unitId,int start)
{
	ControlList ctrlist = new ControlList();
	ctrlist.setAreaWidth("100%");
	ctrlist.setListStyle("listgen");
	ctrlist.setTitleStyle("listgentitle");
	ctrlist.setCellStyle("listgensell");
	ctrlist.setHeaderStyle("listgentitle");
	ctrlist.addHeader(textHeaderTable[language][0],"4%");
	ctrlist.addHeader(textHeaderTable[language][1],"6%");
	ctrlist.addHeader(textHeaderTable[language][2],"20%");	 

  ctrlist.setLinkRow(1);
	ctrlist.setLinkPrefix("javascript:cmdEdit('");
	ctrlist.setLinkSufix("')");	
	Vector lstData = ctrlist.getData();
	Vector lstLinkData = ctrlist.getLinkData();
	Vector rowx = new Vector(1,1);
	ctrlist.reset();
	int index = -1;
	if(start<0)
	start = 0;
  
  Vector val_group=new Vector();
  Vector key_group=new Vector();
  Vector listMasterGroup = PstMasterGroup.list(0,0,"","");
  for(int i=0;i<listMasterGroup.size();i++){
        MasterGroup masterGroup = (MasterGroup)listMasterGroup.get(i);
        val_group.add(""+masterGroup.getOID()+"");
        key_group.add(masterGroup.getNamaGroup());
    }

	for(int i = 0; i < objectClass.size(); i++) 
	{
            MasterGroupMapping matMasterGroupMapping = (MasterGroupMapping)objectClass.get(i); 
            MasterGroup mas = new MasterGroup();
            try {
                mas = PstMasterGroup.fetchExc(matMasterGroupMapping.getGroupId());
              } catch (Exception e) {
              }
            rowx = new Vector();
            index = i; 

            start = start + 1;


            if(index == i && (iCommand == Command.EDIT || iCommand == Command.ASK  || (iCommand == Command.SAVE && frmObject.errorSize() > 0)))
            {				
                rowx.add(""+start);
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmMasterGroupMapping.FRM_FIELD_GROUP_ID], null, ""+matMasterGroupMapping.getGroupId(),val_group,key_group,"","formElemen"));
                String option = "";
                option = "<select name=\""+frmObject.fieldNames[FrmMasterGroupMapping.FRM_FIELD_MODUL]+"\">";
                for(int id = 0; id < PstMasterGroupMapping.modul.length; id++){
                       String txt = PstMasterGroupMapping.modul[id];
                option += "<option value=\""+id+"\">"+txt+"</option>";
                }
                option += "</select>";
                rowx.add(option);
//                rowx.add("<input type=\"text\" size=\"10\" name=\""+frmObject.fieldNames[FrmMasterGroupMapping.FRM_FIELD_MODUL] +"\" value=\""+PstMasterGroupMapping.modul[matMasterGroupMapping.getModul()]+"\" class=\"formElemen\">");		
            }else{
                rowx.add(""+start);
                rowx.add("<a href=\"javascript:cmdEdit('"+String.valueOf(mas.getOID())+"')\">"+mas.getNamaGroup()+"</a>");
                rowx.add(""+PstMasterGroupMapping.modul[matMasterGroupMapping.getModul()]);			
            }

            lstData.add(rowx);
	}
	
	rowx = new Vector();
       
        
	if(iCommand == Command.ADD || (iCommand == Command.SAVE && frmObject.errorSize() > 0))
	{ 
            rowx.add(""+(start+1));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmMasterGroupMapping.FRM_FIELD_GROUP_ID], null, "", val_group,key_group,"","formElemen"));
                String option = "";
                option = "<select name=\""+frmObject.fieldNames[FrmMasterGroupMapping.FRM_FIELD_MODUL]+"\">";
                for(int id = 0; id < PstMasterGroupMapping.modul.length; id++){
                       String txt = PstMasterGroupMapping.modul[id];
                option += "<option value=\""+id+"\">"+txt+"</option>";
                }
                option += "</select>";
                rowx.add(option);
            lstData.add(rowx);
        }
        
        return ctrlist.draw();
}
%>
<%
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int prevCommand = FRMQueryString.requestInt(request, "prev_command");
long oidSales = FRMQueryString.requestLong(request, "hidden_master_group_mapping_id");
String master_group_mappingTitle = textListGlobal[SESS_LANGUAGE][1]; //com.dimata.posbo.jsp.JspInfo.txtMaterialInfo[SESS_LANGUAGE][com.dimata.posbo.jsp.JspInfo.MATERIAL_SALES];

/*variable declaration*/
int recordToGet = 20;
String msgString = "";
int iErrCode = FRMMessage.NONE;
String whereClause = "";
String orderClause = "";

CtrlMasterGroupMapping ctrlMasterGroupMapping = new CtrlMasterGroupMapping(request);
ControlLine ctrLine = new ControlLine();
Vector listSales = new Vector(1,1);

/*switch statement */
iErrCode = ctrlMasterGroupMapping.action(iCommand , oidSales);
/* end switch*/
FrmMasterGroupMapping frmMasterGroupMapping = ctrlMasterGroupMapping.getForm();

/*count list All Sales*/
int vectSize = PstMasterGroupMapping.getCount(whereClause);

/*switch list Sales*/
if((iCommand == Command.FIRST || iCommand == Command.PREV )||
  (iCommand == Command.NEXT || iCommand == Command.LAST))
  {
		start = ctrlMasterGroupMapping.actionList(iCommand, start, vectSize, recordToGet);
  } 
/* end switch list*/

MasterGroupMapping masterGroupMapping = ctrlMasterGroupMapping.getMasterGroupMapping();
msgString =  ctrlMasterGroupMapping.getMessage();

/* get record to display */
listSales = PstMasterGroupMapping.list(start,recordToGet, whereClause , orderClause);

/*handle condition if size of record to display = 0 and start > 0 	after delete*/
if (listSales.size() < 1 && start > 0)
{
	 if (vectSize - recordToGet > recordToGet)
			start = start - recordToGet;   //go to Command.PREV
	 else
	 {
		 start = 0 ;
		 iCommand = Command.FIRST;
		 prevCommand = Command.FIRST; //go to Command.FIRST
	 }
	 listSales = PstMasterGroupMapping.list(start,recordToGet, whereClause , orderClause);
}
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <%@include file="../styles/plugin_component.jsp" %>
    <title>Dimata - ProChain POS</title>
    <script language="JavaScript">

function cmdAdd()
{
	document.frm_matmaster_group_mapping.hidden_master_group_mapping_id.value="0";
	document.frm_matmaster_group_mapping.command.value="<%=Command.ADD%>";
	document.frm_matmaster_group_mapping.prev_command.value="<%=prevCommand%>";
	document.frm_matmaster_group_mapping.action="master_group_mapping.jsp";
	document.frm_matmaster_group_mapping.submit();
}

function cmdAsk(oidSales)
{
	document.frm_matmaster_group_mapping.hidden_master_group_mapping_id.value=oidSales;
	document.frm_matmaster_group_mapping.command.value="<%=Command.ASK%>";
	document.frm_matmaster_group_mapping.prev_command.value="<%=prevCommand%>";
	document.frm_matmaster_group_mapping.action="master_group_mapping.jsp";
	document.frm_matmaster_group_mapping.submit();
}

function cmdConfirmDelete(oidSales)
{
	document.frm_matmaster_group_mapping.hidden_master_group_mapping_id.value=oidSales;
	document.frm_matmaster_group_mapping.command.value="<%=Command.DELETE%>";
	document.frm_matmaster_group_mapping.prev_command.value="<%=prevCommand%>";
	document.frm_matmaster_group_mapping.action="master_group_mapping.jsp";
	document.frm_matmaster_group_mapping.submit();
}

function cmdSave()
{
	document.frm_matmaster_group_mapping.command.value="<%=Command.SAVE%>";
	document.frm_matmaster_group_mapping.prev_command.value="<%=prevCommand%>";
	document.frm_matmaster_group_mapping.action="master_group_mapping.jsp";
	document.frm_matmaster_group_mapping.submit();
}

function cmdEdit(oidSales)
{
	document.frm_matmaster_group_mapping.hidden_master_group_mapping_id.value=oidSales;
	document.frm_matmaster_group_mapping.command.value="<%=Command.EDIT%>";
	document.frm_matmaster_group_mapping.prev_command.value="<%=prevCommand%>";
	document.frm_matmaster_group_mapping.action="master_group_mapping.jsp";
	document.frm_matmaster_group_mapping.submit();
}

function cmdCancel(oidSales)
{
	document.frm_matmaster_group_mapping.hidden_master_group_mapping_id.value=oidSales;
	document.frm_matmaster_group_mapping.command.value="<%=Command.EDIT%>";
	document.frm_matmaster_group_mapping.prev_command.value="<%=prevCommand%>";
	document.frm_matmaster_group_mapping.action="master_group_mapping.jsp";
	document.frm_matmaster_group_mapping.submit();
}

function cmdBack()
{
	document.frm_matmaster_group_mapping.command.value="<%=Command.BACK%>";
	document.frm_matmaster_group_mapping.action="master_group_mapping.jsp";
	document.frm_matmaster_group_mapping.submit();
}

function cmdListFirst()
{
	document.frm_matmaster_group_mapping.command.value="<%=Command.FIRST%>";
	document.frm_matmaster_group_mapping.prev_command.value="<%=Command.FIRST%>";
	document.frm_matmaster_group_mapping.action="master_group_mapping.jsp";
	document.frm_matmaster_group_mapping.submit();
}

function cmdListPrev()
{
	document.frm_matmaster_group_mapping.command.value="<%=Command.PREV%>";
	document.frm_matmaster_group_mapping.prev_command.value="<%=Command.PREV%>";
	document.frm_matmaster_group_mapping.action="master_group_mapping.jsp";
	document.frm_matmaster_group_mapping.submit();
}

function cmdListNext()
{
	document.frm_matmaster_group_mapping.command.value="<%=Command.NEXT%>";
	document.frm_matmaster_group_mapping.prev_command.value="<%=Command.NEXT%>";
	document.frm_matmaster_group_mapping.action="master_group_mapping.jsp";
	document.frm_matmaster_group_mapping.submit();
}

function cmdListLast()
{
	document.frm_matmaster_group_mapping.command.value="<%=Command.LAST%>";
	document.frm_matmaster_group_mapping.prev_command.value="<%=Command.LAST%>";
	document.frm_matmaster_group_mapping.action="master_group_mapping.jsp";
	document.frm_matmaster_group_mapping.submit();
}

//-------------- script form image -------------------

function cmdDelPict(oidSales)
{
	document.frmimage.hidden_master_group_mapping_id.value=oidSales;
	document.frmimage.command.value="<%=Command.POST%>";
	document.frmimage.action="master_group_mapping.jsp";
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

<script>
$(document).ready(function(){
  $("#myInput").on("keyup", function() {
    var value = $(this).val().toLowerCase();
    $(".listgen tbody tr").filter(function() {
      $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
    });
  });
});
</script>
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
<style>
    .listgentitle {
        font-size: 12px !important;
        font-style: normal;
        font-weight: bold;
        color: #FFFFFF;
        background-color: #3e85c3 !important;
        text-align: center;
        height: 25px !important;
        border: 1px solid !important;
      }
    table.listgen {
    width: 95%;
    margin: auto 1%;
    text-align: center;
}
      a.btn.btn-primary.store {
    margin: 25px 0px 0px 30px;
}
.listgensell {
    color: #000000;
    background-color: #ffffff !important;
    border: 1px solid #3e85c3;
}
    .line {
        margin-left: 15px;
        border-bottom: 3px solid #cccccc;
    }
    input#myInput {
    margin: 1% 1% 0% 1%;
    width: 15%;
}
</style>
  </head>
  <body>
    <section class="content-header">
      <h1><%=textListGlobal[SESS_LANGUAGE][0]%><small> <%=textListGlobal[SESS_LANGUAGE][1]%></small> </h1>
      <ol class="breadcrumb">
        <li><%=textListGlobal[SESS_LANGUAGE][1]%></li>
      </ol>
    </section>
     <p class="line"></p>
      <section><!-- #BeginEditable "content" --> 
            <form name="frm_matmaster_group_mapping" method ="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="vectSize" value="<%=vectSize%>">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="hidden_master_group_mapping_id" value="<%=oidSales%>">
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr align="left" valign="top"> 
                  <td height="8"  colspan="3"> 
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <%
							try{
							%>
       
                     <input id="myInput" class="form-control" type="text" placeholder="Search..">
                      <tr align="left" valign="top"> 
                        <td height="22" valign="middle" colspan="3"> <%=drawList(SESS_LANGUAGE,iCommand,frmMasterGroupMapping, masterGroupMapping,listSales,oidSales,start)%> </td>
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
                      <tr align="left" valign="top"> 
                        <td height="22" valign="middle" colspan="3"> 
	                      <%if(iCommand!=Command.ADD && iCommand!=Command.EDIT && iCommand!=Command.ASK && iErrCode==FRMMessage.NONE){%>					  						
                          <table width="17%" border="0" cellspacing="2" cellpadding="3"><br>
                            <tr>  							
                              <td nowrap width="82%"><a class="btn btn-lg btn-primary" href="javascript:cmdAdd()" class="command"><i class="fa fa-plus-circle"></i> <%=textListGlobal[SESS_LANGUAGE][2]%></a></td>			  
                            </tr>
                          </table>
	                      <%}%>					  		
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>
                <tr align="left" valign="top" > 
                  <td colspan="3" class="command"> 
                    <%
									ctrLine.setLocationImg(approot+"/images");
									
									// set image alternative caption
									ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,master_group_mappingTitle,ctrLine.CMD_SAVE,true));
									ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,master_group_mappingTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,master_group_mappingTitle,ctrLine.CMD_BACK,true)+" List");							
									ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,master_group_mappingTitle,ctrLine.CMD_ASK,true));							
									ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,master_group_mappingTitle,ctrLine.CMD_CANCEL,false));														
									
									ctrLine.initDefault();
									ctrLine.setTableWidth("80%");
									String scomDel = "javascript:cmdAsk('"+oidSales+"')";
									String sconDelCom = "javascript:cmdConfirmDelete('"+oidSales+"')";
									String scancel = "javascript:cmdEdit('"+oidSales+"')";
									ctrLine.setCommandStyle("command");
									ctrLine.setColCommStyle("command");
									
									// set command caption
									ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE,master_group_mappingTitle,ctrLine.CMD_SAVE,true));
									ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,master_group_mappingTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,master_group_mappingTitle,ctrLine.CMD_BACK,true)+" List");							
									ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE,master_group_mappingTitle,ctrLine.CMD_ASK,true));							
									ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE,master_group_mappingTitle,ctrLine.CMD_DELETE,true));														
									ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE,master_group_mappingTitle,ctrLine.CMD_CANCEL,false));									

									if (privDelete){
										ctrLine.setConfirmDelCommand(sconDelCom);
										ctrLine.setDeleteCommand(scomDel);
										ctrLine.setEditCommand(scancel);
									}else{ 
										ctrLine.setConfirmDelCommand(sconDelCom);
										ctrLine.setDeleteCommand(scomDel);
										ctrLine.setEditCommand(scancel);
									}

									if(iCommand == Command.EDIT){
										ctrLine.setSaveCaption("");
									}

										ctrLine.setAddCaption("");

									%>
         <%if(iCommand==Command.ADD || iCommand==Command.EDIT || iCommand==Command.ASK || ((iCommand==Command.SAVE || iCommand==Command.DELETE)&&iErrCode!=FRMMessage.NONE)){%>
                    <%= ctrLine.drawImage(iCommand, iErrCode, msgString)%><%}%></td>
                </tr>
              </table>
            </form>
          
    </section>
  </body>
</html>

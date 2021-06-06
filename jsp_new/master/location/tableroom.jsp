<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package prochain -->
<%@ page import = "com.dimata.common.entity.contact.*" %>
<%@ page import = "com.dimata.common.entity.location.*" %>
<%@ page import = "com.dimata.common.form.location.*" %>
<%@ page import = "com.dimata.posbo.form.masterdata.*" %>
<%@ page import = "com.dimata.posbo.entity.masterdata.*" %>
                 

<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_LOCATION); %>
<%@ include file = "../../main/checkuser.jsp" %>

<!-- Jsp Block -->
<%!
public static String formatNumberList = "#,###";
public static String formatNumberEdit = "##.###";

/* this constant used to list text of listHeader */
public static final String textListHeader[][] = 
{
	{"No","Lokasi","Ruangan","No. Meja","Dimensi","Panjang","Lebar","cm","Kapasitas","X","Y","Grid","Pax","Semua Location"},
	{"No","Location","Room","Table Number", "Dimention ","Length","Weight","cm","Capacity","X","Y","Grid","Pax","All Location"}	
};
public static final String textListTitleHeader[][] =
{
	{"Meja"},
	{"Tables"}
};  
public String drawList(int language, Vector objectClass , long tableId, String approot, int start,int recordToGet)
{
	ControlList ctrlist = new ControlList();
	ctrlist.setAreaWidth("70%");
	ctrlist.setListStyle("tabbg");
	ctrlist.setListStyle("listgen");
	ctrlist.setTitleStyle("listgentitle");
	ctrlist.setCellStyle("listgensell");
	ctrlist.setHeaderStyle("listgentitle");
	ctrlist.addHeader(textListHeader[language][0],"3%");
	ctrlist.addHeader(textListHeader[language][1],"20%");
	ctrlist.addHeader(textListHeader[language][2],"17%");
	ctrlist.addHeader(textListHeader[language][3],"5%");
        ctrlist.addHeader(textListHeader[language][4]+"("+textListHeader[language][5]+") ("+textListHeader[language][7]+")","5%");
	ctrlist.addHeader(textListHeader[language][4]+"("+textListHeader[language][6]+") ("+textListHeader[language][7]+")","5%");
	ctrlist.addHeader(textListHeader[language][8]+" ("+textListHeader[language][12]+")","5%");
        ctrlist.addHeader(textListHeader[language][11]+" ("+textListHeader[language][9]+")","5%");
	ctrlist.addHeader(textListHeader[language][11]+" ("+textListHeader[language][10]+")","5%");
	
      
	

	ctrlist.setLinkSufix("");
	Vector lstData = ctrlist.getData();
	Vector lstLinkData = ctrlist.getLinkData();
	ctrlist.reset();
	
	int index = -1;
	if(start<0)
		start = 0;
	
	for (int i = 0; i < objectClass.size(); i++)
	{
		TableRoom tableRoom = (TableRoom)objectClass.get(i);			
		
		Vector rowx = new Vector();
		if(tableId == tableRoom.getOID())
			 index = i;
			 
		start = start + 1; 			
		rowx.add(""+start);
                Location location = new Location();
                try {
                    location = PstLocation.fetchExc(tableRoom.getLocationId());
                }
                catch(Exception e) {
			System.out.println("Location not found ...");
			}
                
		rowx.add("<a href=\"javascript:cmdEdit('"+tableRoom.getOID()+"')\">"+location.getName()+"</a>");
                
                Room room = new Room();
                try {
                    room = PstRoom.fetchExc(tableRoom.getRoomId());
                }
                catch(Exception e) {
			System.out.println("Room not found ...");
			}
                rowx.add(cekNull(room.getName()));
		rowx.add(""+tableRoom.getTableNumber());
                rowx.add(""+tableRoom.getDimentionL());
                rowx.add(""+tableRoom.getDimentionW());
                rowx.add(""+tableRoom.getCapacity());
                rowx.add(""+tableRoom.getGridX());
                rowx.add(""+tableRoom.getGridY());
                
		
		lstData.add(rowx);
		lstLinkData.add(String.valueOf(tableRoom.getOID()));
	}
	return ctrlist.draw();
}

public String cekNull(String val)
{
	if(val==null)
		val="";
	return val;
}
%>

<%
/* get data from request form */
int iCommand = FRMQueryString.requestCommand(request);
int cmdMinimum = FRMQueryString.requestInt(request, "command_minimum");
int startTable = FRMQueryString.requestInt(request, "start_table");
int startMaterial = FRMQueryString.requestInt(request, "start_material");
int prevCommand = FRMQueryString.requestInt(request, "prev_command");
long oidTable = FRMQueryString.requestLong(request, "hidden_table_id");
long oidMinimum = FRMQueryString.requestLong(request,"hidden_mat_minimum_id");
String tableTitle = textListTitleHeader[SESS_LANGUAGE][0]; //com.dimata.common.jsp.JspInfo.txtMaterialInfo[SESS_LANGUAGE][com.dimata.common.jsp.JspInfo.LOCATION];
//String minimumTitle = com.dimata.material.jsp.JspInfo.txtMaterialInfo[SESS_LANGUAGE][com.dimata.material.jsp.JspInfo.MATERIAL_MINIMUM];

/* variable declaration */
int recordToGet = 10;
String msgString = "";
int iErrCode = FRMMessage.NONE;
String whereClause = "";
String orderClause = PstTableRoom.fieldNames[PstTableRoom.FLD_LOCATION_ID]+","+PstTableRoom.fieldNames[PstTableRoom.FLD_TABLE_NUMBER];

/* ControlLine */
ControlLine ctrLine = new ControlLine();

/* Control LOcation */
CtrlTableRoom ctrlTableRoom = new CtrlTableRoom(request);
FrmTableRoom frmTableRoom = ctrlTableRoom.getForm();
iErrCode = ctrlTableRoom.action(iCommand,oidTable, request);
TableRoom tableRoom = ctrlTableRoom.getTableRoom();
msgString =  ctrlTableRoom.getMessage();

/* get start value for list location */
if(iCommand==Command.SAVE && iErrCode==FRMMessage.NONE){
	startTable = PstTableRoom.findLimitStart(tableRoom.getOID(), recordToGet, whereClause);
}	

int vectSize = PstTableRoom.getCount(whereClause);
if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand == Command.LAST){
	startTable = ctrlTableRoom.actionList(iCommand, startTable, vectSize, recordToGet);
} 

/* get record to display */
Vector listTable = new Vector(1,1);
listTable = PstTableRoom.list(startTable,recordToGet,whereClause,orderClause);
if(listTable.size()<1 && startTable>0){
	if(vectSize - recordToGet>recordToGet)
	 	startTable = startTable - recordToGet;   
  	else
	{
	 	startTable = 0 ;
	 	iCommand = Command.FIRST;
	 	prevCommand = Command.FIRST; 
  	}
	listTable = PstTableRoom.list(startTable,recordToGet,whereClause,orderClause);
}

%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
<%if((iCommand ==Command.ADD)||(iCommand==Command.SAVE)&&(frmTableRoom.errorSize()>0)||(iCommand==Command.EDIT)||(iCommand==Command.ASK)){%>
	//window.location="#go";
<%}%>

/*------------- start location function ---------------*/
function cmdAdd()
{
	document.frmtable.hidden_table_id.value="0";
	document.frmtable.command.value="<%=Command.ADD%>";
	document.frmtable.prev_command.value="<%=prevCommand%>";
	document.frmtable.action="tableroom.jsp";
	document.frmtable.submit();
}

function cmdAsk(oidTable)
{
	document.frmtable.hidden_table_id.value=oidTable;
	document.frmtable.command.value="<%=Command.ASK%>";
	document.frmtable.prev_command.value="<%=prevCommand%>";
	document.frmtable.action="tableroom.jsp";
	document.frmtable.submit();
}

function cmdConfirmDelete(oidTable)
{
	document.frmtable.hidden_table_id.value=oidTable;
	document.frmtable.command.value="<%=Command.DELETE%>";
	document.frmtable.prev_command.value="<%=prevCommand%>";
	document.frmtable.action="tableroom.jsp";
	document.frmtable.submit();
}

function cmdSave()
{
	document.frmtable.command.value="<%=Command.SAVE%>";
	document.frmtable.prev_command.value="<%=prevCommand%>";
	document.frmtable.action="tableroom.jsp";
	document.frmtable.submit();
}

function cmdEdit(oidTable)
{
	document.frmtable.hidden_table_id.value=oidTable;
	document.frmtable.command.value="<%=Command.EDIT%>";
	document.frmtable.prev_command.value="<%=prevCommand%>";
	document.frmtable.action="tableroom.jsp";
	document.frmtable.submit();
}

function cmdCancel(oidTable)
{
	document.frmtable.hidden_table_id.value=oidTable;
	document.frmtable.command.value="<%=Command.EDIT%>";
	document.frmtable.prev_command.value="<%=prevCommand%>";
	document.frmtable.action="tableroom.jsp";
	document.frmtable.submit();
}

function cmdBack()
{
	document.frmtable.command.value="<%=Command.BACK%>";
	document.frmtable.action="tableroom.jsp";
	document.frmtable.submit();
}

function cmdListFirst()
{
	document.frmtable.command.value="<%=Command.FIRST%>";
	document.frmtable.prev_command.value="<%=Command.FIRST%>";
	document.frmtable.action="tableroom.jsp";
	document.frmtable.submit();
}

function cmdListPrev()
{
	document.frmtable.command.value="<%=Command.PREV%>";
	document.frmtable.prev_command.value="<%=Command.PREV%>";
	document.frmtable.action="tableroom.jsp";
	document.frmtable.submit();
}

function cmdListNext()
{
	document.frmtable.command.value="<%=Command.NEXT%>";
	document.frmtable.prev_command.value="<%=Command.NEXT%>";
	document.frmtable.action="tableroom.jsp";
	document.frmtable.submit();
}

function cmdListLast()
{
	document.frmtable.command.value="<%=Command.LAST%>";
	document.frmtable.prev_command.value="<%=Command.LAST%>";
	document.frmtable.action="tableroom.jsp";
	document.frmtable.submit();
}

//change kasir based on Location
function cmdChangeLocation(){ 
   document.frmtable.submit();
}


/*------------- end location function ---------------*/



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
    <link href="../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
<%}%>
<link rel="stylesheet" href="../../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" --> 
<link rel="stylesheet" href="../../styles/tab.css" type="text/css">
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
            Masterdata &gt; <%=tableTitle%><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frmtable" method ="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="vectSize" value="<%=vectSize%>">
              <input type="hidden" name="start_table" value="<%=startTable%>">
              <input type="hidden" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="hidden_table_id" value="<%=oidTable%>">			  
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr align="left" valign="top"> 
					<td height="8" valign="middle" colspan="3"> 
					  <hr size="1">
					</td>
				</tr>                							  
                <tr align="left" valign="top"> 
                  <td height="8"  colspan="3"> 
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr align="left" valign="top"> 
                        <td height="14" valign="middle" colspan="3" class="comment">&nbsp;<u><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar "+tableTitle : tableTitle+" List"%></u></td>                      
					  </tr>
                      <tr align="left" valign="top"> 
                        <td height="22" valign="middle" colspan="3"> <%= drawList(SESS_LANGUAGE,listTable,oidTable,approot,startTable,recordToGet)%> </td>
                      </tr>
                      <tr align="left" valign="top"> 
                        <td height="8" align="left" colspan="3" class="command"> 
                          <span class="command"> 
                          <% 
						  int cmd = 0;
						  if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST){
							  cmd =iCommand; 
						  }else{
							  if(iCommand==Command.NONE || prevCommand==Command.NONE)
								cmd = Command.FIRST;
							  else 
								cmd =prevCommand; 
						  } 						  
                          ctrLine.setLocationImg(approot+"/images");
						  ctrLine.initDefault();							
                          out.println(ctrLine.drawImageListLimit(cmd,vectSize,startTable,recordToGet));
						  %> 
						  </span>
						</td>
                      </tr>
					  <%
					     if(iCommand==Command.LIST || iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand == Command.LAST
						   || iCommand==Command.NONE
						   || iCommand==Command.BACK 
						   || (iCommand==Command.SAVE&&iErrCode==0) || (iCommand==Command.DELETE && iErrCode==0)){
					  %>					  
                    <tr>
                          <td >&nbsp;<br></td>
                      </tr>  
                    <tr align="left" valign="top"> 
                        <td> 
                          <%if(((iCommand!=Command.ADD)&&(iCommand!=Command.EDIT)&&(iCommand!=Command.ASK))||(iCommand==Command.SAVE&&iErrCode==0) || (iCommand==Command.DELETE && iErrCode==0)){%>
                          <table cellpadding="0" cellspacing="0" border="0">
                            <tr> 
                              <td width="4"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                              <!--td width="24"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image261','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image261" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,tableTitle,ctrLine.CMD_ADD,true)%>"></a></td>
                              <td width="6"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td-->
                              <td height="22" valign="middle" colspan="3"><a href="javascript:cmdAdd()" class="btn btn-lg btn-primary"><i class="fa fa-plus-circle"></i> <%=ctrLine.getCommand(SESS_LANGUAGE,tableTitle,ctrLine.CMD_ADD,true)%></a></td>
                              
                            </tr>
                          </table>
                          <%}%>
                        </td>
                      </tr>
					  <%}%>					  
                    </table>
                  </td>
                </tr>
				
                <tr align="left" valign="top"> 
                  <td height="8" valign="middle" colspan="3"> 
				  
                    <%
					  if((iCommand ==Command.ADD)||(iCommand==Command.EDIT)||(iCommand==Command.ASK)||((iCommand==Command.SAVE) && iErrCode>0) || (iCommand==Command.DELETE && iErrCode>0))
					  {
					%>
                    <table width="100%" border="0" cellspacing="2" cellpadding="2">
                      <tr> 
                        <td colspan="2" class="comment" height="30"><u><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Editor "+tableTitle : tableTitle+" Editor"%></u></td>
                      </tr>
                      <tr> 
                        <td height="100%" width="100%" colspan="2"> 
                          <table border="0" cellspacing="1" cellpadding="1" width="100%">
                            
                             <tr align="left">
                            <td width="12%" valign="top">&nbsp;<%=textListHeader[SESS_LANGUAGE][1]%></td>
                            <td width="1%" valign="top">:
                            <td width="87%" valign="top">
                          <%
                                                        //add opie 21-06-2012
                                                        long selectedLocationId =0;

                                                        if((iCommand==Command.ADD)){
                                                            selectedLocationId = FRMQueryString.requestLong(request, frmTableRoom.fieldNames[FrmTableRoom.FRM_FIELD_LOCATION_ID]);
                                                        }
							Vector val_locationid = new Vector(1,1);
							Vector key_locationid = new Vector(1,1);
							//Vector vt_loc = PstLocation.list(0,0,"", PstLocation.fieldNames[PstLocation.FLD_CODE]);
                                                        
							Vector vt_loc = PstLocation.list(0,0,"", PstLocation.fieldNames[PstLocation.FLD_CODE]);

							//val_locationid.add("0");
							//key_locationid.add(textListHeader[SESS_LANGUAGE][13]);
							for(int d=0;d<vt_loc.size();d++){
								Location loc = (Location)vt_loc.get(d);
								val_locationid.add(""+loc.getOID()+"");
								key_locationid.add(loc.getName());
							}
                                                        
                                                        String select_loc = "0";
                                                        if (selectedLocationId!=0){
                                                            select_loc= ""+selectedLocationId;
                                                        }
                                                        else{
                                                            select_loc=""+ tableRoom.getLocationId();
                                                        } 
						%>
                          <%//=ControlCombo.draw(frmTableRoom.fieldNames[FrmTableRoom.FRM_FIELD_LOCATION_ID],"formElemen", null, ""+tableRoom.getLocationId(), val_locationid, key_locationid, null)%>
                            <%=ControlCombo.draw(frmTableRoom.fieldNames[FrmTableRoom.FRM_FIELD_LOCATION_ID],"formElemen",null,""+select_loc, val_locationid, key_locationid, "onChange=\"javascript:cmdChangeLocation()\"")%></td>
                             
                             </tr>
                             <tr align="left">
                            <td width="12%" valign="top">&nbsp;<%=textListHeader[SESS_LANGUAGE][2]%></td>
                            <td width="1%" valign="top">:
                            <td width="87%" valign="top">
                          <%
                          
                                                         String where = "";
                                                        if (tableRoom.getLocationId()!=0) {
                                                            where = PstRoom.fieldNames[PstRoom.FLD_LOCATION_ID]+"='" +select_loc+"'";
                                                        }
                                                         
							Vector val_roomid = new Vector(1,1);
							Vector key_roomid = new Vector(1,1);
							Vector vt_room = PstRoom.list(0,0,where, PstRoom.fieldNames[PstRoom.FLD_CODE]);
							
							for(int d=0;d<vt_room.size();d++){
								Room room = (Room)vt_room.get(d);
								val_roomid.add(""+room.getOID()+"");
								key_roomid.add(room.getName());
							}
							
                                                       
						%>
                          <%=ControlCombo.draw(frmTableRoom.fieldNames[FrmTableRoom.FRM_FIELD_ROOM_ID],"formElemen", null, ""+tableRoom.getRoomId(), val_roomid, key_roomid, null)%></td>
                             </tr>
                            <tr align="left"> 
                              <td width="12%"><%=textListHeader[SESS_LANGUAGE][3]%></td>
                              <td width="1%" valign="top">:</td>
                              <td width="87%" valign="top">
                                <input type="text" name="<%=frmTableRoom.fieldNames[FrmTableRoom.FRM_FIELD_TABLE_NUMBER] %>"  value="<%=tableRoom.getTableNumber() %>" class="formElemen" size="5">
                                * <%= frmTableRoom.getErrorMsg(FrmTableRoom.FRM_FIELD_TABLE_NUMBER) %> </td>
                            </tr>
                            <tr align="left"> 
                              <td width="12%"><%=textListHeader[SESS_LANGUAGE][4]%></td>
                              <td width="1%" valign="top">:</td>
                              <td width="87%" valign="top"> 
                                <%=textListHeader[SESS_LANGUAGE][5]%><input type="text" name="<%=frmTableRoom.fieldNames[FrmTableRoom.FRM_FIELD_DIMENTION_L] %>"  value="<%=tableRoom.getDimentionL() %>" class="formElemen" size="10">
                                <%= frmTableRoom.getErrorMsg(FrmTableRoom.FRM_FIELD_DIMENTION_L) %>&nbsp;<%=textListHeader[SESS_LANGUAGE][7]%></td>
                            </tr>
                            
                             <tr align="left"> 
                              <td width="12%">&nbsp;</td>
                              <td width="1%" valign="top"></td>
                              <td width="87%" valign="top"> 
                                <%=textListHeader[SESS_LANGUAGE][6]%><input type="text" name="<%=frmTableRoom.fieldNames[FrmTableRoom.FRM_FIELD_DIMENTION_W] %>"  value="<%=tableRoom.getDimentionW() %>" class="formElemen" size="10">
                                <%= frmTableRoom.getErrorMsg(FrmTableRoom.FRM_FIELD_DIMENTION_W) %>&nbsp;<%=textListHeader[SESS_LANGUAGE][7]%></td>
                            </tr>
                            
                             <tr align="left"> 
                              <td width="12%"><%=textListHeader[SESS_LANGUAGE][8]%></td>
                              <td width="1%" valign="top">:</td>
                              <td width="87%" valign="top"> 
                                <input type="text" name="<%=frmTableRoom.fieldNames[FrmTableRoom.FRM_FIELD_CAPACITY] %>"  value="<%=tableRoom.getCapacity() %>" class="formElemen" size="5">
                                * <%= frmTableRoom.getErrorMsg(FrmTableRoom.FRM_FIELD_CAPACITY) %>&nbsp;<%=textListHeader[SESS_LANGUAGE][12]%></td>
                            </tr>
                            
                            <tr align="left"> 
                              <td width="12%"><%=textListHeader[SESS_LANGUAGE][11]%></td>
                              <td width="1%" valign="top">:</td>
                              <td width="87%" valign="top"> 
                                <%=textListHeader[SESS_LANGUAGE][9]%> :
                                <input type="text" name="<%=frmTableRoom.fieldNames[FrmTableRoom.FRM_FIELD_GRID_X] %>"  value="<%=tableRoom.getGridX() %>" class="formElemen" size="10">
                                   <%= frmTableRoom.getErrorMsg(FrmTableRoom.FRM_FIELD_GRID_X) %>&nbsp;
                                <%=textListHeader[SESS_LANGUAGE][10]%> :
                                <input type="text" name="<%=frmTableRoom.fieldNames[FrmTableRoom.FRM_FIELD_GRID_Y] %>"  value="<%=tableRoom.getGridY() %>" class="formElemen" size="10">
                                 <%= frmTableRoom.getErrorMsg(FrmTableRoom.FRM_FIELD_GRID_Y) %></td>
                            </tr> 
                          </table>
                        </td>
                      </tr>
                      <tr>
                          <td colspan="2" class="command">&nbsp;<br></td>
                      </tr>
                      <tr align="left" valign="top" > 
                        <td colspan="2" class="command"> 
                          <%
							ctrLine.setLocationImg(approot+"/images");
							
							// set image alternative caption
							ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,tableTitle,ctrLine.CMD_SAVE,true));
							ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,tableTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,tableTitle,ctrLine.CMD_BACK,true)+" List");						
							ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,tableTitle,ctrLine.CMD_ASK,true));							
							ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE, tableTitle,ctrLine.CMD_CANCEL,false));	
                                                      												

							ctrLine.initDefault();
							ctrLine.setTableWidth("100%");
							String scomDel = "javascript:cmdAsk('"+oidTable+"')";
							String sconDelCom = "javascript:cmdConfirmDelete('"+oidTable+"')";
							String scancel = "javascript:cmdEdit('"+oidTable+"')";
							ctrLine.setCommandStyle("command");
							ctrLine.setColCommStyle("command");
							
							// set command caption
							ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE,tableTitle,ctrLine.CMD_SAVE,true));
							ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,tableTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,tableTitle,ctrLine.CMD_BACK,true)+" List");							
							ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE,tableTitle,ctrLine.CMD_ASK,true));							
							ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE,tableTitle,ctrLine.CMD_DELETE,true));														
							ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE,tableTitle,ctrLine.CMD_CANCEL,false));							
							
							
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
                          <%= ctrLine.drawImage(iCommand, iErrCode, msgString)%></td>
                      </tr>
                    </table>
                    <%}%>
					
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


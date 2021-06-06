<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package material -->
<%@ page import = "com.dimata.posbo.entity.masterdata.*" %>
<%@ page import = "com.dimata.posbo.form.masterdata.*" %>

<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_MERK); %>
<%@ include file = "../../main/checkuser.jsp" %>



<!-- Jsp Block -->
<%!
/* this constant used to list text of listHeader */
public static final String textListHeader[][] = 
{
	{"No","Nama","Merk","Daftar","Kode","Status","Lokasi Produksi"},
	{"No","Name","Merk","List of","Code","Status","Production Location"}
};

/* this method used to list material merk */
public String drawList(int language,int iCommand,FrmMerk frmObject,Merk objEntity,Vector objectClass,long merkId,int start, String typeOfBusiness, String merkName)
{
        int mappingProduksi = Integer.parseInt(PstSystemProperty.getValueByName("MAPPING_PRINT_PRODUKSI"));
	ControlList ctrlist = new ControlList();
	ctrlist.setAreaWidth("100%");
	ctrlist.setListStyle("listgen");
	ctrlist.setTitleStyle("listgentitle");
	ctrlist.setCellStyle("listgensell");
	ctrlist.setHeaderStyle("listgentitle");
	ctrlist.addHeader(textListHeader[language][0],"5%");
	ctrlist.addHeader(textListHeader[language][4],"10%");
        ctrlist.addHeader(textListHeader[language][1],"35%");
        //adding status ditampilkan atau tidak by mirahu 20120511
        /*if(typeOfBusiness.equals("2")&& merkName=="Sub Category"){
            ctrlist.addHeader(textListHeader[language][5],"10%");
        }*/
        if(typeOfBusiness.equals("2")){
            if(mappingProduksi==0){
                ctrlist.dataFormat(textListHeader[language][6],"10%","center","left");
             }
        }
        

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
        
        //adding combo box 
        Vector val_merkstatus = new Vector(1,1);
	Vector key_merkstatus = new Vector(1,1);

	val_merkstatus.add(""+PstMerk.USE_NO_SHOW);
	key_merkstatus.add("Tidak ditampilkan di outlet");
						
	val_merkstatus.add(""+PstMerk.USE_SHOW);
	key_merkstatus.add("Ditampilkan dioutlet");

	
	for(int i = 0; i < objectClass.size(); i++) 
	{
		Merk matMerk = (Merk)objectClass.get(i);
		rowx = new Vector();
		if(merkId == matMerk.getOID())
			 index = i; 
			
		start = start + 1;	
		if(index == i && (iCommand == Command.EDIT || iCommand == Command.ASK))
		{				
			rowx.add("<div align=\"center\">"+start+"</div>");
			rowx.add("<input type=\"text\" size=\"30\" name=\""+frmObject.fieldNames[FrmMerk.FRM_FIELD_CODE] +"\" value=\""+matMerk.getCode()+"\" class=\"formElemen\"> * ");
                        rowx.add("<input type=\"text\" size=\"30\" name=\""+frmObject.fieldNames[FrmMerk.FRM_FIELD_NAME] +"\" value=\""+matMerk.getName()+"\" class=\"formElemen\"> * ");
                        
                        if(typeOfBusiness.equals("2")){
                            if(mappingProduksi==0){
                                rowx.add("<a href=\"javascript:addCategory('"+matMerk.getOID()+"')\" >mapping produksi</a>");
                            }
                        }
                        
		}
		else
		{
			rowx.add("<div align=\"center\">" + start+"</div>");
                        rowx.add("<a href=\"javascript:cmdEdit('"+String.valueOf(matMerk.getOID())+"')\"><div align=\"left\">"+matMerk.getCode()+"</div></a>");
			rowx.add("<div align=\"left\">"+matMerk.getName()+"</div>");
                        //adding status ditampilkan atau tidak by mirahu 20120511
                        /*if(typeOfBusiness.equals("2")&& merkName=="Sub Category"){
                            if(matMerk.getStatus() == PstMerk.USE_SHOW){
                                rowx.add("Ditampilkan di outlet");

                            } else {
                                rowx.add("Tidak ditampilkan di outlet");
                            }
                        }*/
                        if(typeOfBusiness.equals("2")){
                            if(mappingProduksi==0){
                                rowx.add("<a href=\"javascript:addCategory('"+matMerk.getOID()+"')\" >mapping produksi</a>");
                            }
                        }
                        
                        
		} 
		lstData.add(rowx);
	}
	
	rowx = new Vector();
	if(iCommand == Command.ADD || (iCommand == Command.SAVE && frmObject.errorSize() > 0))
	{ 
			rowx.add(""+(start+1));
			rowx.add("<input type=\"text\" size=\"30\" name=\""+frmObject.fieldNames[FrmMerk.FRM_FIELD_CODE] +"\" value=\""+objEntity.getCode()+"\" class=\"formElemen\"> * ");
                        rowx.add("<input type=\"text\" size=\"30\" name=\""+frmObject.fieldNames[FrmMerk.FRM_FIELD_NAME] +"\" value=\""+objEntity.getName()+"\" class=\"formElemen\"> * ");
                        //adding status ditampilkan atau tidak by mirahu 20120511
                        if(typeOfBusiness.equals("2") && merkName=="Sub Category"){
                             if(mappingProduksi==0){
                                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmMerk.FRM_FIELD_STATUS],"formElemen", null, ""+objEntity.getStatus() , val_merkstatus, key_merkstatus, null));
                            }
                            
			}
                        lstData.add(rowx);

	}
	
	return ctrlist.draw();
}
%>
<%
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int prevCommand = FRMQueryString.requestInt(request, "prev_command");
long oidMerk = FRMQueryString.requestLong(request, "hidden_merk_id");
String merkTitle = com.dimata.posbo.jsp.JspInfo.txtMaterialInfo[SESS_LANGUAGE][com.dimata.posbo.jsp.JspInfo.MATERIAL_MERK];
int source = FRMQueryString.requestInt(request, "source");

/*variable declaration*/
int recordToGet = 0;
String msgString = "";
int iErrCode = FRMMessage.NONE;
String whereClause = "";
String orderClause = "";

CtrlMerk ctrlMerk = new CtrlMerk(request);
ControlLine ctrLine = new ControlLine();
Vector listMerk = new Vector(1,1);

/*switch statement */
iErrCode = ctrlMerk.action(iCommand , oidMerk);
/* end switch*/
FrmMerk frmMerk = ctrlMerk.getForm();

/*count list All Merk*/
int vectSize = PstMerk.getCount(whereClause);

/*switch list Merk*/
if((iCommand == Command.FIRST || iCommand == Command.PREV )||
  (iCommand == Command.NEXT || iCommand == Command.LAST))
  {
		start = ctrlMerk.actionList(iCommand, start, vectSize, recordToGet);
  } 
/* end switch list*/

Merk matMerk = ctrlMerk.getMerk();
msgString =  ctrlMerk.getMessage();

/* get record to display */
    orderClause = PstMerk.fieldNames[PstMerk.FLD_NAME];
listMerk = PstMerk.list(start,recordToGet, whereClause , orderClause);

/*handle condition if size of record to display = 0 and start > 0 	after delete*/
if (listMerk.size() < 1 && start > 0)
{
	 if (vectSize - recordToGet > recordToGet)
			start = start - recordToGet;   //go to Command.PREV
	 else
	 {
		 start = 0 ;
		 iCommand = Command.FIRST;
		 prevCommand = Command.FIRST; //go to Command.FIRST
	 }
	 listMerk = PstMerk.list(start,recordToGet, whereClause , orderClause);
}
    
    merkName = PstSystemProperty.getValueByName("NAME_OF_MERK");
    merkTitle = merkName; //textListHeader[SESS_LANGUAGE][2];
%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">

function addCategory(oidmapping){
    //alert("aaa");
    var strvalue  = "mapping_kitchen_produksi.jsp?command=<%=Command.ADD%>&oidmapping="+oidmapping+"&source=1";
    winSrcMaterial = window.open(strvalue,"PopupWindow", "height=600,width=800,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes");
    if (window.focus) { winSrcMaterial.focus();}
}

function cmdAdd()
{
	document.frmmatmerk.hidden_merk_id.value="0";
	document.frmmatmerk.command.value="<%=Command.ADD%>";
	document.frmmatmerk.prev_command.value="<%=prevCommand%>";
	document.frmmatmerk.action="matmerk.jsp";
	document.frmmatmerk.submit();
}

function cmdAsk(oidMerk)
{
	document.frmmatmerk.hidden_merk_id.value=oidMerk;
	document.frmmatmerk.command.value="<%=Command.ASK%>";
	document.frmmatmerk.prev_command.value="<%=prevCommand%>";
	document.frmmatmerk.action="matmerk.jsp";
	document.frmmatmerk.submit();
}

function cmdConfirmDelete(oidMerk)
{
	document.frmmatmerk.hidden_merk_id.value=oidMerk;
	document.frmmatmerk.command.value="<%=Command.DELETE%>";
	document.frmmatmerk.prev_command.value="<%=prevCommand%>";
	document.frmmatmerk.action="matmerk.jsp";
	document.frmmatmerk.submit();
}

function cmdSave()
{
	document.frmmatmerk.command.value="<%=Command.SAVE%>";
	document.frmmatmerk.prev_command.value="<%=prevCommand%>";
	document.frmmatmerk.action="matmerk.jsp";
	document.frmmatmerk.submit();
}

function cmdEdit(oidMerk)
{
	document.frmmatmerk.hidden_merk_id.value=oidMerk;
	document.frmmatmerk.command.value="<%=Command.EDIT%>";
	document.frmmatmerk.prev_command.value="<%=prevCommand%>";
	document.frmmatmerk.action="matmerk.jsp";
	document.frmmatmerk.submit();
}

function cmdCancel(oidMerk)
{
	document.frmmatmerk.hidden_merk_id.value=oidMerk;
	document.frmmatmerk.command.value="<%=Command.EDIT%>";
	document.frmmatmerk.prev_command.value="<%=prevCommand%>";
	document.frmmatmerk.action="matmerk.jsp";
	document.frmmatmerk.submit();
}

function cmdBack()
{
	document.frmmatmerk.command.value="<%=Command.BACK%>";
	document.frmmatmerk.action="matmerk.jsp";
	document.frmmatmerk.submit();
}

function cmdListFirst()
{
	document.frmmatmerk.command.value="<%=Command.FIRST%>";
	document.frmmatmerk.prev_command.value="<%=Command.FIRST%>";
	document.frmmatmerk.action="matmerk.jsp";
	document.frmmatmerk.submit();
}

function cmdListPrev()
{
	document.frmmatmerk.command.value="<%=Command.PREV%>";
	document.frmmatmerk.prev_command.value="<%=Command.PREV%>";
	document.frmmatmerk.action="matmerk.jsp";
	document.frmmatmerk.submit();
}

function cmdListNext()
{
	document.frmmatmerk.command.value="<%=Command.NEXT%>";
	document.frmmatmerk.prev_command.value="<%=Command.NEXT%>";
	document.frmmatmerk.action="matmerk.jsp";
	document.frmmatmerk.submit();
}

function cmdListLast()
{
	document.frmmatmerk.command.value="<%=Command.LAST%>";
	document.frmmatmerk.prev_command.value="<%=Command.LAST%>";
	document.frmmatmerk.action="matmerk.jsp";
	document.frmmatmerk.submit();
}

//-------------- script form image -------------------

function cmdDelPict(oidMerk)
{
	document.frmimage.hidden_merk_id.value=oidMerk;
	document.frmimage.command.value="<%=Command.POST%>";
	document.frmimage.action="matmerk.jsp";
	document.frmimage.submit();
}

function cmdClose(){
     self.opener.document.forms.frmmaterial.submit();
     self.close();
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
<style type="text/css">
.listheader { COLOR: #FFFFFF; background-color:<%=tableHeader%>; FONT-SIZE: 10px; FONT-WEIGHT: bold; TEXT-ALIGN: center}
.listgensell {  color: #000000; background-color:<%=tableCell%>}
.listgensell {  color: #000000; background-color:<%=tableCell%>}
.tabcontent {  background-color: <%=tableCell%>}
.table_cell {  background-color: <%=tableCell%>}
.listgentitle {  font-size: 11px; font-style: normal; font-weight: bold; color: #FFFFFF; background-color: <%=tableHeader%>; text-align: center}
</style>
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
    <%if(source==0){%>
         <tr bgcolor="#FFFFFF">
            <td height="10" ID="MAINMENU">
              <%@include file="../../styletemplate/template_header.jsp" %>
            </td>
          </tr>
    <%}%>
  <%}%>
  <tr>
    <td valign="top" align="left">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">  
        <tr> 
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->
            Masterdata &gt; <%=merkName%><%///=textListHeader[SESS_LANGUAGE][2]%><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frmmatmerk" method ="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="vectSize" value="<%=vectSize%>">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="hidden_merk_id" value="<%=oidMerk%>">
              <input type="hidden" name="source" value="<%=source%>">
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr align="left" valign="top"> 
                  <td height="8"  colspan="3"> 
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr align="left" valign="top"> 
                        <td height="8" valign="middle" colspan="3"> 
                          <hr size="1">
                        </td>
                      </tr>
                      <tr align="left" valign="top"> 
                        <td height="14" valign="middle" colspan="3" class="comment">&nbsp;<u><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar "+merkName : textListHeader[SESS_LANGUAGE][3]%></u></td>
                      </tr>
                      <%
                        try{
                      %>
                      <tr align="left" valign="top"> 
                        <td height="22" valign="middle" colspan="3"> <%=drawList(SESS_LANGUAGE,iCommand,frmMerk, matMerk,listMerk,oidMerk,start,typeOfBusiness,merkTitle)%> </td>
                      </tr>
                      <% 
                        }catch(Exception exc){ 
                      
                        }
                      %>
                      <tr align="left" valign="top"> 
                        <td height="8" align="left" colspan="3" class="command"> 
                          <span class="command"> 
                          <% 
                            int cmd = 0;
                            if ((iCommand == Command.FIRST || iCommand == Command.PREV)
                                    || (iCommand == Command.NEXT || iCommand == Command.LAST)) {
                                cmd = iCommand;
                            } else {
                                if (iCommand == Command.NONE || prevCommand == Command.NONE) {
                                    cmd = Command.FIRST;
                                } else {
                                    cmd = prevCommand;
                                }
                            }
                          %>
                          <% 
                            ctrLine.setLocationImg(approot+"/images");
                            ctrLine.initDefault();
			  %>
                          <%//=ctrLine.drawImageListLimit(cmd,vectSize,start,recordToGet)%> </span> </td>
                      </tr>
                      <tr align="left" valign="top"> 
                        <td height="22" valign="middle" colspan="3">                             
	                    <%
                            if(iCommand!=Command.ADD && iCommand!=Command.EDIT && iCommand!=Command.ASK && iErrCode==FRMMessage.NONE){%>
                          <table width="17%" border="0" cellspacing="2" cellpadding="3">
                            <tr> 
                              <%if(privAdd){%>					  							
                                  <!--td width="18%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,merkTitle,ctrLine.CMD_ADD,true)%>"></a></td-->
                                  <td nowrap ><a href="javascript:cmdAdd()" class="btn btn-lg btn-primary"><i class="fa fa-plus-circle"></i> <%=ctrLine.getCommand(SESS_LANGUAGE,merkTitle,ctrLine.CMD_ADD,true)%></a></td>
                              <%}%>							  
                            </tr>
                             <tr> 
                               <%if(source==1){%>					  							
                                  <td width="18%"><a href="javascript:cmdClose()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,merkTitle,ctrLine.CMD_BACK_SEARCH,true)%>"></a></td>
                                  <td nowrap width="82%"><a href="javascript:cmdClose()" class="command"><%=ctrLine.getCommand(SESS_LANGUAGE,merkTitle,ctrLine.CMD_BACK_SEARCH,true)%></a></td>
                              <%}%>							  
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
                    ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,merkTitle,ctrLine.CMD_SAVE,true));
                    ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,merkTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,merkTitle,ctrLine.CMD_BACK,true)+" List");							
                    ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,merkTitle,ctrLine.CMD_ASK,true));							
                    ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,merkTitle,ctrLine.CMD_CANCEL,false));														

                    ctrLine.initDefault();
                    ctrLine.setTableWidth("80%");
                    String scomDel = "javascript:cmdAsk('"+oidMerk+"')";
                    String sconDelCom = "javascript:cmdConfirmDelete('"+oidMerk+"')";
                    String scancel = "javascript:cmdEdit('"+oidMerk+"')";
                    ctrLine.setCommandStyle("command");
                    ctrLine.setColCommStyle("command");

                    // set command caption
                    ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE,merkTitle,ctrLine.CMD_SAVE,true));
                    ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,merkTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,merkTitle,ctrLine.CMD_BACK,true)+" List");							
                    ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE,merkTitle,ctrLine.CMD_ASK,true));							
                    ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE,merkTitle,ctrLine.CMD_DELETE,true));														
                    ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE,merkTitle,ctrLine.CMD_CANCEL,false));									

                    if (privDelete){
                            ctrLine.setConfirmDelCommand(sconDelCom);
                            ctrLine.setDeleteCommand(scomDel);
                            ctrLine.setEditCommand(scancel);
                    }else{ 
                            ctrLine.setConfirmDelCaption("");
                            ctrLine.setDeleteCaption("");
                            ctrLine.setEditCaption("");
                    }

                    if(iCommand == Command.EDIT && privUpdate == false){
                            ctrLine.setSaveCaption("");
                    }

                    if (privAdd == false){
                            ctrLine.setAddCaption("");
                    }
                    %>
                    <%if(iCommand==Command.ADD || iCommand==Command.EDIT || iCommand==Command.ASK || ((iCommand==Command.SAVE || iCommand==Command.DELETE)&&iErrCode!=FRMMessage.NONE)){%>
                    <%= ctrLine.drawImage(iCommand, iErrCode, msgString)%><%}%></td>
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

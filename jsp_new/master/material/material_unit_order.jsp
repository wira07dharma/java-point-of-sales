<%-- 
    Document   : material_unit_buy
    Created on : Mar 12, 2014, 3:01:20 PM
    Author     : dimata005
--%>

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
	{"No","Nama","Merk","Daftar","Kode","Status"},
	{"No","Name","Merk","List of","Code","Status"}
};

/* this method used to list material merk */
public String drawList(int language,int iCommand,FrmMaterialUnitOrder frmObject,MaterialUnitOrder objEntity,Vector objectClass,long materialId,int start, String merkTitle,long oidMaterialUnitOrder)
{
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
        ctrlist.addHeader(textListHeader[language][5],"10%");
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
        String whereBaseUnit = "";//PstUnit.fieldNames[PstUnit.FLD_BASE_UNIT_ID] + " = 0";
        Vector listBaseUnit = PstUnit.list(0,0,whereBaseUnit,"");
        Vector vectUnitVal = new Vector(1,1);
        Vector vectUnitKey = new Vector(1,1);
        
        for(int i=0; i<listBaseUnit.size(); i++){
            Unit mateUnit = (Unit)listBaseUnit.get(i);
            vectUnitKey.add(mateUnit.getCode());
            vectUnitVal.add(""+mateUnit.getOID());
        }
	for(int i = 0; i < objectClass.size(); i++) 
	{
		MaterialUnitOrder materialUnitOrder = (MaterialUnitOrder)objectClass.get(i);
		rowx = new Vector();
		if(oidMaterialUnitOrder == materialUnitOrder.getOID())
			 index = i; 
			
		start = start + 1;	
		if(index == i && (iCommand == Command.EDIT || iCommand == Command.ASK))
		{				
			rowx.add("<div align=\"center\">"+start+"</div>");
			rowx.add("<input type=\"hidden\" size=\"30\" name=\""+frmObject.fieldNames[FrmMaterialUnitOrder.FRM_FIELD_MATERIAL_ID] +"\" value=\""+materialUnitOrder.getMaterialID()+"\" class=\"formElemen\"><input type=\"text\" size=\"30\" name=\"nam\" value=\""+merkTitle+"\" class=\"formElemen\"> * ");
                        rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmMaterialUnitOrder.FRM_UNIT_ID],"formElemen", null, ""+materialUnitOrder.getMaterialID() , vectUnitVal, vectUnitKey, null));
                        rowx.add("<input type=\"text\" size=\"30\" name=\""+frmObject.fieldNames[FrmMaterialUnitOrder.FRM_FIELD_MINIMUM_QTY_ORDER] +"\" value=\""+materialUnitOrder.getMinimumQtyOrder()+"\" class=\"formElemen\"> * ");
		}
		else
		{
			rowx.add("<div align=\"center\">" + start+"</div>");
                        rowx.add("<a href=\"javascript:cmdEdit('"+String.valueOf(materialUnitOrder.getOID())+"')\"><div align=\"left\">"+merkTitle+"</div></a>");
			rowx.add("<div align=\"center\">" + materialUnitOrder.getUnitKode()+"</div>");//rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmMaterialUnitOrder.FRM_UNIT_ID],"formElemen", null, ""+materialUnitOrder.getMaterialID() , vectUnitVal, vectUnitKey, null));
                        rowx.add("<div align=\"left\">"+materialUnitOrder.getMinimumQtyOrder()+"</div>");
                        
		} 
		lstData.add(rowx);
	}
	
	rowx = new Vector();
	if(iCommand == Command.ADD || (iCommand == Command.SAVE && frmObject.errorSize() > 0))
	{ 
            rowx.add(""+(start+1));
            rowx.add("<input type=\"hidden\" size=\"30\" name=\""+frmObject.fieldNames[FrmMaterialUnitOrder.FRM_FIELD_MATERIAL_ID] +"\" value=\""+materialId+"\" class=\"formElemen\">"+merkTitle+"");
            rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmMaterialUnitOrder.FRM_UNIT_ID],"formElemen", null, ""+objEntity.getUnitID() , vectUnitVal, vectUnitKey, null));
            rowx.add("<input type=\"text\" size=\"30\" name=\""+frmObject.fieldNames[FrmMaterialUnitOrder.FRM_FIELD_MINIMUM_QTY_ORDER] +"\" value=\""+objEntity.getMinimumQtyOrder()+"\" class=\"formElemen\"> * ");

            lstData.add(rowx);
	}
	
	return ctrlist.draw();
}
%>
<%
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int prevCommand = FRMQueryString.requestInt(request, "prev_command");
long oidMaterial = FRMQueryString.requestLong(request, "oidMaterial");
long oidMaterialUnitOrder = FRMQueryString.requestLong(request, "oidMaterialUnitOrder");
String merkTitle = com.dimata.posbo.jsp.JspInfo.txtMaterialInfo[SESS_LANGUAGE][com.dimata.posbo.jsp.JspInfo.MATERIAL_MERK];

/*variable declaration*/
int recordToGet = 20;
String msgString = "";
int iErrCode = FRMMessage.NONE;
String whereClause = "PMU."+PstMaterialUnitOrder.fieldNames[PstMaterialUnitOrder.FLD_MATERIAL_ID]+"='"+oidMaterial+"'";
String whereClausex = ""+PstMaterialUnitOrder.fieldNames[PstMaterialUnitOrder.FLD_MATERIAL_ID]+"='"+oidMaterial+"'";

String orderClause = "";

CtrlMaterialUnitOrder ctrlMaterialUnitOrder = new CtrlMaterialUnitOrder(request);
ControlLine ctrLine = new ControlLine();
Vector listMaterialUnitOrder = new Vector(1,1);

/*switch statement */
iErrCode = ctrlMaterialUnitOrder.action(iCommand , oidMaterialUnitOrder,userId,userName);
/* end switch*/
FrmMaterialUnitOrder frmMaterialUnitOrder = ctrlMaterialUnitOrder.getForm();

/*count list All Merk*/
int vectSize = PstMaterialUnitOrder.getCount(whereClausex);

/*switch list Merk*/
if((iCommand == Command.FIRST || iCommand == Command.PREV )||
  (iCommand == Command.NEXT || iCommand == Command.LAST))
  {
		start = ctrlMaterialUnitOrder.actionList(iCommand, start, vectSize, recordToGet);
  } 
/* end switch list*/

MaterialUnitOrder materialUnitOrder = ctrlMaterialUnitOrder.getMerk();
msgString =  ctrlMaterialUnitOrder.getMessage();

/* get record to display */
 //   orderClause = PstMerk.fieldNames[PstMerk.FLD_NAME];
listMaterialUnitOrder = PstMaterialUnitOrder.listJoin(start,recordToGet, whereClause , orderClause);

/*handle condition if size of record to display = 0 and start > 0 	after delete*/
if (listMaterialUnitOrder.size() < 1 && start > 0)
{
	 if (vectSize - recordToGet > recordToGet)
			start = start - recordToGet;   //go to Command.PREV
	 else
	 {
		 start = 0 ;
		 iCommand = Command.FIRST;
		 prevCommand = Command.FIRST; //go to Command.FIRST
	 }
	 listMaterialUnitOrder = PstMaterialUnitOrder.list(start,recordToGet, whereClause , orderClause);
}
Material material = new Material();
try{
material = PstMaterial.fetchExc(oidMaterial);
}catch(Exception ex){

}
    //merkName = PstSystemProperty.getValueByName("NAME_OF_MERK");
    merkTitle = material.getName(); //textListHeader[SESS_LANGUAGE][2];
%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">


function cmdAdd()
{
	document.frmmatmerk.oidMaterialUnitOrder.value="0";
	document.frmmatmerk.command.value="<%=Command.ADD%>";
	document.frmmatmerk.prev_command.value="<%=prevCommand%>";
	document.frmmatmerk.action="material_unit_order.jsp";
	document.frmmatmerk.submit();
}

function cmdAsk(oidMerk)
{
	document.frmmatmerk.oidMaterialUnitOrder.value=oidMerk;
	document.frmmatmerk.command.value="<%=Command.ASK%>";
	document.frmmatmerk.prev_command.value="<%=prevCommand%>";
	document.frmmatmerk.action="material_unit_order.jsp";
	document.frmmatmerk.submit();
}

function cmdConfirmDelete(oidMerk)
{
	document.frmmatmerk.oidMaterialUnitOrder.value=oidMerk;
	document.frmmatmerk.command.value="<%=Command.DELETE%>";
	document.frmmatmerk.prev_command.value="<%=prevCommand%>";
	document.frmmatmerk.action="material_unit_order.jsp";
	document.frmmatmerk.submit();
}

function cmdSave()
{
	document.frmmatmerk.command.value="<%=Command.SAVE%>";
	document.frmmatmerk.prev_command.value="<%=prevCommand%>";
	document.frmmatmerk.action="material_unit_order.jsp";
	document.frmmatmerk.submit();
}

function cmdClose(){
     self.opener.document.forms.frmmaterial.submit();
     self.close();
}

function cmdEdit(oidMerk)
{
	document.frmmatmerk.oidMaterialUnitOrder.value=oidMerk;
	document.frmmatmerk.command.value="<%=Command.EDIT%>";
	document.frmmatmerk.prev_command.value="<%=prevCommand%>";
	document.frmmatmerk.action="material_unit_order.jsp";
	document.frmmatmerk.submit();
}

function cmdCancel(oidMerk)
{
	document.frmmatmerk.oidMaterialUnitOrder.value=oidMerk;
	document.frmmatmerk.command.value="<%=Command.EDIT%>";
	document.frmmatmerk.prev_command.value="<%=prevCommand%>";
	document.frmmatmerk.action="material_unit_order.jsp";
	document.frmmatmerk.submit();
}


function cmdViewHistory(oid) {
    var strvalue ="../../main/historypo.jsp?command=<%=Command.FIRST%>"+
                     "&oidDocHistory="+oid;
    window.open(strvalue,"material", "height=600,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
}


function cmdBack()
{
	document.frmmatmerk.command.value="<%=Command.BACK%>";
	document.frmmatmerk.action="material_unit_order.jsp";
	document.frmmatmerk.submit();
}

function cmdListFirst()
{
	document.frmmatmerk.command.value="<%=Command.FIRST%>";
	document.frmmatmerk.prev_command.value="<%=Command.FIRST%>";
	document.frmmatmerk.action="material_unit_order.jsp";
	document.frmmatmerk.submit();
}

function cmdListPrev()
{
	document.frmmatmerk.command.value="<%=Command.PREV%>";
	document.frmmatmerk.prev_command.value="<%=Command.PREV%>";
	document.frmmatmerk.action="material_unit_order.jsp";
	document.frmmatmerk.submit();
}

function cmdListNext()
{
	document.frmmatmerk.command.value="<%=Command.NEXT%>";
	document.frmmatmerk.prev_command.value="<%=Command.NEXT%>";
	document.frmmatmerk.action="material_unit_order.jsp";
	document.frmmatmerk.submit();
}

function cmdListLast()
{
	document.frmmatmerk.command.value="<%=Command.LAST%>";
	document.frmmatmerk.prev_command.value="<%=Command.LAST%>";
	document.frmmatmerk.action="material_unit_order.jsp";
	document.frmmatmerk.submit();
}

//-------------- script form image -------------------

function cmdDelPict(oidMerk)
{
	document.frmimage.oidMaterialUnitOrder.value=oidMerk;
	document.frmimage.command.value="<%=Command.POST%>";
	document.frmimage.action="material_unit_order.jsp";
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
<%@include file="../../styletemplate/template_header_empty.jsp" %>
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
              <input type="hidden" name="oidMaterial" value="<%=oidMaterial%>">
              <input type="hidden" name="hidden_material_id" value="<%=oidMaterial%>">
              <input type="hidden" name="oidMaterialUnitOrder" value="<%=oidMaterialUnitOrder%>">
              
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
                        <td height="22" valign="middle" colspan="3"> <%=drawList(SESS_LANGUAGE,iCommand,frmMaterialUnitOrder, materialUnitOrder,listMaterialUnitOrder,oidMaterial,start,merkTitle,oidMaterialUnitOrder)%> </td>
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
                          <table width="17%" border="0" cellspacing="2" cellpadding="3">
                            <tr> 
		                      <%if(privAdd){%>					  							
                                            <td width="18%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,"",ctrLine.CMD_ADD,true)%>"></a></td>
                                            <td nowrap width="82%"><a href="javascript:cmdAdd()" class="command"><%=ctrLine.getCommand(SESS_LANGUAGE,"",ctrLine.CMD_ADD,true)%></a></td>
		                      <%}%>
                                       <td width="18%"><a href="javascript:cmdClose()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,"",ctrLine.CMD_ADD,true)%>"></a></td>
                                            <td nowrap width="82%"><a href="javascript:cmdClose()" class="command"><%=ctrLine.getCommand(SESS_LANGUAGE,"Masterdata",ctrLine.CMD_BACK,true)%></a></td>
                                       
                                       <td width="5%"><a href="javascript:cmdViewHistory('<%=oidMaterialUnitOrder%>')" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="View History"></a></td>
                                              <td nowrap width="45%"><a href="javascript:cmdViewHistory('<%=oidMaterialUnitOrder%>')">View History</a></td>
                                              
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
									String scomDel = "javascript:cmdAsk('"+oidMaterialUnitOrder+"')";
									String sconDelCom = "javascript:cmdConfirmDelete('"+oidMaterialUnitOrder+"')";
									String scancel = "javascript:cmdEdit('"+oidMaterialUnitOrder+"')";
									ctrLine.setCommandStyle("command");
									ctrLine.setColCommStyle("command");
									
									// set command caption
									ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE,"",ctrLine.CMD_SAVE,true));
									ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,"",ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,"",ctrLine.CMD_BACK,true)+" List");							
									ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE,"",ctrLine.CMD_ASK,true));							
									ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE,"",ctrLine.CMD_DELETE,true));														
									ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE,"",ctrLine.CMD_CANCEL,false));									

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


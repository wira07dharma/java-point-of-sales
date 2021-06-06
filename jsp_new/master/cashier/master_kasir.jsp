<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package material -->
<%@ page import = "com.dimata.pos.entity.masterCashier.*" %>
<%@ page import = "com.dimata.pos.form.masterCashier.*" %>
<%@ page import = "com.dimata.common.entity.location.*" %>


<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_CASHIER); %>
<%@ include file = "../../main/checkuser.jsp" %>
<!-- Jsp Block -->
<%!
/* this constant used to list text of listHeader */
public static final String textListHeader[][] = 
{
	{"No","Lokasi","Nomor Kasir","Pajak","Service","Tipe Harga","Mode Database","Mode Stok"},
	{"No","Location","Cashier Number","Tax","Service","Price Type", "Database Mode","Stock Mode"}	
};
public static final String textListTitleHeader[][] = 
{
	{"Kasir","Master Kasir"},
	{"Cashier","Master Cashier"}	
};
/* this method used to list material master cashier */
public String drawList(int language, int iCommand, FrmCashMaster frmObject, CashMaster objEntity, Vector objectClass, long cashMasterId, int start)
{
    ControlList ctrlist = new ControlList();

    try{

	ctrlist.setAreaWidth("100%");
	ctrlist.setListStyle("listgen");
	ctrlist.setTitleStyle("listgentitle");
	ctrlist.setCellStyle("listgensell");
	ctrlist.setHeaderStyle("listgentitle");
	ctrlist.addHeader(textListHeader[language][0],"1%");
	ctrlist.addHeader(textListHeader[language][1],"15%");
	ctrlist.addHeader(textListHeader[language][2],"15%");
        //update opie-eyek 20131216 di master kasir ga perlu penginputan pajak dan tax service
	//ctrlist.addHeader(textListHeader[language][3],"20%");
	//ctrlist.addHeader(textListHeader[language][4],"20%");
        //added by dewok 2018-06-22
        ctrlist.addHeader(textListHeader[language][6],"15%");
        ctrlist.addHeader(textListHeader[language][7],"15%");
	
	ctrlist.setLinkRow(0);
	ctrlist.setLinkPrefix("javascript:cmdEdit('");
	ctrlist.setLinkSufix("')");	
	Vector lstData = ctrlist.getData();
	Vector lstLinkData = ctrlist.getLinkData();
	Vector rowx = new Vector(1,1);
	ctrlist.reset();
	int index = -1;
	if(start<0)	start = 0;
	
	// create vector of location
	Vector vectLocation = PstLocation.list(0,0,"","");
	Vector vectLocKey = new Vector(1,1);	
	Vector vectLocVal = new Vector(1,1);
	if(vectLocation!=null && vectLocation.size()>0){
		int maxLoc = vectLocation.size();
		for(int i=0; i<maxLoc; i++){
			Location location = (Location) vectLocation.get(i);
			vectLocKey.add(String.valueOf(location.getOID()));
			vectLocVal.add(String.valueOf(location.getName()));			
		}
	}
        
	// create vector of price type
	Vector vectPriceTypeKey = new Vector(1,1);	
	Vector vectPriceTypeVal = new Vector(1,1);	
	vectPriceTypeKey.add("01");
	vectPriceTypeVal.add("01");
	vectPriceTypeKey.add("02");
	vectPriceTypeVal.add("02");
	vectPriceTypeKey.add("03");
	vectPriceTypeVal.add("03");

        //added by dewok 2018-06-22
        //combobox value for database mode
        Vector keyDbMode = new Vector(1,1);	
	Vector valDbMode = new Vector(1,1);
        for (int i = 0; i < PstCashMaster.DATABASE_MODE_TITLE.length; i++) {
            keyDbMode.add(""+PstCashMaster.DATABASE_MODE_TITLE[i]);
            valDbMode.add(""+i);
        }
        //combobox value for stock mode
        Vector keyStockMode = new Vector(1,1);	
	Vector valStockMode = new Vector(1,1);
        for (int i = 0; i < PstCashMaster.STOCK_MODE_TITLE.length; i++) {
            keyStockMode.add(""+PstCashMaster.STOCK_MODE_TITLE[i]);
            valStockMode.add(""+i);
        }
	
	for(int i = 0; i < objectClass.size(); i++) 
	{
		CashMaster cashMaster = (CashMaster)objectClass.get(i);
		rowx = new Vector();
		if(cashMasterId == cashMaster.getOID())
			 index = i; 

		String locationName = "";
		try{
		    PstLocation pstLocation = new PstLocation();
			Location location = pstLocation.fetchExc(cashMaster.getLocationId());
			locationName = location.getName();
		}catch(Exception e){
			System.out.println("err when fetch location : "+e.toString());
		}	

		start = start + 1;

        if(index == i && (iCommand == Command.EDIT || iCommand == Command.ASK)){
			rowx.add("<div align=\"center\">"+start+"</div>");
			rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmCashMaster.FRM_FIELD_LOCATION_ID],null,String.valueOf(cashMaster.getLocationId()),vectLocKey,vectLocVal,"","formElemen"));						
			rowx.add("<div align=\"right\"><input type=\"text\" size=\"12\" name=\""+frmObject.fieldNames[FrmCashMaster.FRM_FIELD_CASHIER_NUMBER] +"\" value=\""+cashMaster.getCashierNumber()+"\" style=\"text-align:right\" class=\"formElemen\"></div>");
			//update opie-eyek 20131216 di master kasir ga perlu penginputan pajak dan tax service
                        //rowx.add("<div align=\"right\"><input type=\"text\" size=\"17\" name=\""+frmObject.fieldNames[FrmCashMaster.FRM_FIELD_TAX] +"\" value=\""+cashMaster.getCashTax()+"\" style=\"text-align:right\" class=\"formElemen\"></div>");
			//rowx.add("<div align=\"right\"><input type=\"text\" size=\"17\" name=\""+frmObject.fieldNames[FrmCashMaster.FRM_FIELD_SERVICE] +"\" value=\""+cashMaster.getCashService()+"\" style=\"text-align:right\" class=\"formElemen\"></div>");
                        //rowx.add("<div align=\"right\">"+ControlCombo.draw(frmObject.fieldNames[FrmCashMaster.FRM_FIELD_PRICE_TYPE],null,String.valueOf(cashMaster.getPriceType()),vectPriceTypeKey,vectPriceTypeVal,"","formElemen")+"</div>");
                        //added by dewok 2018-06-22
                        rowx.add("<div align=\"right\">"+ControlCombo.draw(frmObject.fieldNames[FrmCashMaster.FRM_FIELD_CASHIER_DATABASE_MODE],null,String.valueOf(cashMaster.getCashierDatabaseMode()),valDbMode,keyDbMode,"","formElemen")+"</div>");
                        rowx.add("<div align=\"right\">"+ControlCombo.draw(frmObject.fieldNames[FrmCashMaster.FRM_FIELD_CASHIER_STOCK_MODE],null,String.valueOf(cashMaster.getCashierStockMode()),valStockMode,keyStockMode,"","formElemen")+"</div>");
		}else{
			rowx.add("<div align=\"center\">"+start+"</div>");
			rowx.add("<a href=\"javascript:cmdEdit('"+String.valueOf(cashMaster.getOID())+"')\"><div align=\"left\">"+locationName+"</div></a>");
			rowx.add("<div align=\"center\">"+String.valueOf(cashMaster.getCashierNumber())+"</div>");
                       // System.out.println("asdasdas : 01");
                        //update opie-eyek 20131216 di master kasir ga perlu penginputan pajak dan tax service
                        //rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(cashMaster.getCashTax())+"</div>");
			//rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(cashMaster.getCashService())+"</div>");
                        //rowx.add("<div align=\"right\">"+String.valueOf(cashMaster.getPriceType())+"</div>");
                        //added by dewok 2018-06-22
                        rowx.add("<div align=\"\">"+PstCashMaster.DATABASE_MODE_TITLE[cashMaster.getCashierDatabaseMode()]+"</div>");
                        rowx.add("<div align=\"\">"+PstCashMaster.STOCK_MODE_TITLE[cashMaster.getCashierStockMode()]+"</div>");
		} 
		lstData.add(rowx);

    }
	
	rowx = new Vector();
	if(iCommand == Command.ADD || (iCommand == Command.SAVE && frmObject.errorSize() > 0))
	{ 
			rowx.add("<div align=\"center\">"+(start+1)+"</div>");			
			rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmCashMaster.FRM_FIELD_LOCATION_ID],null,String.valueOf(objEntity.getLocationId())+ " - " + objEntity.getCashierNumber(),vectLocKey,vectLocVal,"","formElemen"));
			rowx.add("<div align=\"right\"><input type=\"text\" size=\"12\" name=\""+frmObject.fieldNames[FrmCashMaster.FRM_FIELD_CASHIER_NUMBER] +"\" value=\""+objEntity.getCashierNumber()+"\" style=\"text-align:right\" class=\"formElemen\"></div>");
			//update opie-eyek 20131216 di master kasir ga perlu penginputan pajak dan tax service
                        //rowx.add("<div align=\"right\"><input type=\"text\" size=\"17\" name=\""+frmObject.fieldNames[FrmCashMaster.FRM_FIELD_TAX] +"\" value=\""+objEntity.getCashTax()+"\" style=\"text-align:right\" class=\"formElemen\"></div>");
			//rowx.add("<div align=\"right\"><input type=\"text\" size=\"17\" name=\""+frmObject.fieldNames[FrmCashMaster.FRM_FIELD_SERVICE] +"\" value=\""+objEntity.getCashService()+"\" style=\"text-align:right\" class=\"formElemen\"></div>");
			//rowx.add("<div align=\"right\">"+ControlCombo.draw(frmObject.fieldNames[FrmCashMaster.FRM_FIELD_PRICE_TYPE],null,String.valueOf(objEntity.getPriceType()),vectPriceTypeKey,vectPriceTypeVal,"","formElemen")+"</div>");	
                        //added by dewok 2018-06-22
                        rowx.add("<div align=\"right\">"+ControlCombo.draw(frmObject.fieldNames[FrmCashMaster.FRM_FIELD_CASHIER_DATABASE_MODE],null,null,valDbMode,keyDbMode,"","formElemen")+"</div>");
                        rowx.add("<div align=\"right\">"+ControlCombo.draw(frmObject.fieldNames[FrmCashMaster.FRM_FIELD_CASHIER_STOCK_MODE],null,null,valStockMode,keyStockMode,"","formElemen")+"</div>");
			lstData.add(rowx);
	}
	
    }catch(Exception e){
        System.out.println("err"+e.toString());
    }
	return ctrlist.draw();
}
%>
<%
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int prevCommand = FRMQueryString.requestInt(request, "prev_command");
long oidCashMaster = FRMQueryString.requestLong(request, "hidden_cashier_id");
String cashierTitle = textListTitleHeader[SESS_LANGUAGE][1];

// variable declaration
int recordToGet = 10;
String msgString = "";
int iErrCode = FRMMessage.NONE;
String whereClause = "";
String orderClause = "";

CtrlCashMaster ctrlCashMaster = new CtrlCashMaster(request);
ControlLine ctrLine = new ControlLine();
Vector listCashMaster = new Vector(1,1);

iErrCode = ctrlCashMaster.action(iCommand, oidCashMaster);
FrmCashMaster frmCashMaster = ctrlCashMaster.getForm();


int vectSize = PstCashMaster.getCount(whereClause);
if((iCommand == Command.FIRST || iCommand == Command.PREV )||
  (iCommand == Command.NEXT || iCommand == Command.LAST))
  {
		start = ctrlCashMaster.actionList(iCommand, start, vectSize, recordToGet);
  } 

CashMaster cashMaster = ctrlCashMaster.getCashMaster();
msgString = ctrlCashMaster.getMessage();

listCashMaster = PstCashMaster.list(start, recordToGet, whereClause, orderClause);

if (listCashMaster.size() < 1 && start > 0)
{
	 if (vectSize - recordToGet > recordToGet)
			start = start - recordToGet;   
	 else
	 {
		 start = 0 ;
		 iCommand = Command.FIRST;
		 prevCommand = Command.FIRST; 
	 }
	 listCashMaster = PstCashMaster.list(start, recordToGet, whereClause, orderClause);
}
%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
function cmdAdd()
{
	document.frmmatkasir.hidden_cashier_id.value="0";
	document.frmmatkasir.command.value="<%=Command.ADD%>";
	document.frmmatkasir.prev_command.value="<%=prevCommand%>";
	document.frmmatkasir.action="master_kasir.jsp";
	document.frmmatkasir.submit();
}

function cmdAsk(oidCashMaster)
{
	document.frmmatkasir.hidden_cashier_id.value=oidCashMaster;
	document.frmmatkasir.command.value="<%=Command.ASK%>";
	document.frmmatkasir.prev_command.value="<%=prevCommand%>";
	document.frmmatkasir.action="master_kasir.jsp";
	document.frmmatkasir.submit();
}

function cmdConfirmDelete(oidCashMaster)
{
	document.frmmatkasir.hidden_cashier_id.value=oidCashMaster;
	document.frmmatkasir.command.value="<%=Command.DELETE%>";
	document.frmmatkasir.prev_command.value="<%=prevCommand%>";
	document.frmmatkasir.action="master_kasir.jsp";
	document.frmmatkasir.submit();
}

function cmdSave()
{
	document.frmmatkasir.command.value="<%=Command.SAVE%>";
	document.frmmatkasir.prev_command.value="<%=prevCommand%>";
	document.frmmatkasir.action="master_kasir.jsp";
	document.frmmatkasir.submit();
}

function cmdEdit(oidCashMaster)
{
	document.frmmatkasir.hidden_cashier_id.value=oidCashMaster;
	document.frmmatkasir.command.value="<%=Command.EDIT%>";
	document.frmmatkasir.prev_command.value="<%=prevCommand%>";
	document.frmmatkasir.action="master_kasir.jsp";
	document.frmmatkasir.submit();
}

function cmdCancel(oidCashMaster)
{
	document.frmmatkasir.hidden_cashier_id.value=oidCashMaster;
	document.frmmatkasir.command.value="<%=Command.EDIT%>";
	document.frmmatkasir.prev_command.value="<%=prevCommand%>";
	document.frmmatkasir.action="master_kasir.jsp";
	document.frmmatkasir.submit();
}

function cmdBack()
{
	document.frmmatkasir.command.value="<%=Command.BACK%>";
	document.frmmatkasir.action="master_kasir.jsp";
	document.frmmatkasir.submit();
}

function cmdListFirst()
{
	document.frmmatkasir.command.value="<%=Command.FIRST%>";
	document.frmmatkasir.prev_command.value="<%=Command.FIRST%>";
	document.frmmatkasir.action="master_kasir.jsp";
	document.frmmatkasir.submit();
}

function cmdListPrev()
{
	document.frmmatkasir.command.value="<%=Command.PREV%>";
	document.frmmatkasir.prev_command.value="<%=Command.PREV%>";
	document.frmmatkasir.action="master_kasir.jsp";
	document.frmmatkasir.submit();
}

function cmdListNext()
{
	document.frmmatkasir.command.value="<%=Command.NEXT%>";
	document.frmmatkasir.prev_command.value="<%=Command.NEXT%>";
	document.frmmatkasir.action="master_kasir.jsp";
	document.frmmatkasir.submit();
}

function cmdListLast()
{
	document.frmmatkasir.command.value="<%=Command.LAST%>";
	document.frmmatkasir.prev_command.value="<%=Command.LAST%>";
	document.frmmatkasir.action="master_kasir.jsp";
	document.frmmatkasir.submit();
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
    <link href="../../stylesheets/general_home_style.css" type="text/css"
rel="stylesheet" />
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
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->Masterdata > <%=textListTitleHeader[SESS_LANGUAGE][1]%><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" -->
            <form name="frmmatkasir" method ="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="vectSize" value="<%=vectSize%>">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="hidden_cashier_id" value="<%=oidCashMaster%>">
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
                        <td height="14" valign="middle" colspan="3" class="comment">&nbsp;<u><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar "+cashierTitle : cashierTitle+" List"%></u></td>
						</tr>
                      <tr align="left" valign="top"> 
                        <td height="22" valign="middle" colspan="3"> <%=drawList(SESS_LANGUAGE, iCommand, frmCashMaster, cashMaster, listCashMaster, oidCashMaster, start)%> </td>
                      </tr>
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
						   
						   ctrLine.setLocationImg(approot+"/images");
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
                              <!--td width="18%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,cashierTitle,ctrLine.CMD_ADD,true)%>"></a></td-->
                            <br><td nowrap width="82%"><a class="btn btn-lg btn-primary" href="javascript:cmdAdd()" class="command"><i class="fa fa-plus-circle"></i> <%=ctrLine.getCommand(SESS_LANGUAGE,cashierTitle,ctrLine.CMD_ADD,true)%></a></td>
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
					ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,cashierTitle,ctrLine.CMD_SAVE,true));
					ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,cashierTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,cashierTitle,ctrLine.CMD_BACK,true)+" List");							
					ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,cashierTitle,ctrLine.CMD_ASK,true));							
					ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,cashierTitle,ctrLine.CMD_CANCEL,false));														
					
					ctrLine.initDefault();
					ctrLine.setTableWidth("80%");
					String scomDel = "javascript:cmdAsk('"+oidCashMaster+"')";
					String sconDelCom = "javascript:cmdConfirmDelete('"+oidCashMaster+"')";
					String scancel = "javascript:cmdEdit('"+oidCashMaster+"')";
					ctrLine.setCommandStyle("command");
					ctrLine.setColCommStyle("command");
					
					// set command caption
					ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE,cashierTitle,ctrLine.CMD_SAVE,true));
					ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,cashierTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,cashierTitle,ctrLine.CMD_BACK,true)+" List");							
					ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE,cashierTitle,ctrLine.CMD_ASK,true));							
					ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE,cashierTitle,ctrLine.CMD_DELETE,true));														
					ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE,cashierTitle,ctrLine.CMD_CANCEL,false));									

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
			<%
			if(iCommand==Command.ADD || iCommand==Command.EDIT)
			{
			%>
			<script language="javascript">
				document.frmmatkasir.<%=FrmCashMaster.fieldNames[FrmCashMaster.FRM_FIELD_LOCATION_ID]%>.focus();
			</script>
			<%
			}
			%>
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


<%@page import="com.dimata.harisma.entity.employee.Employee"%>
<%@page import="com.dimata.harisma.entity.employee.PstEmployee"%>
<%@page import="com.dimata.common.entity.payment.StandartRate"%>
<%@page import="com.dimata.common.entity.payment.PstStandartRate"%>
<%@page import="com.dimata.common.entity.location.Location"%>
<%@page import="com.dimata.common.entity.location.PstLocation"%>
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

<%@ include file = "../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_SALES); %>
<%@ include file = "../main/checkuser.jsp" %>


<!-- Jsp Block -->
<%!
/* this constant used to list text of listHeader */
public static final String textListHeader[][] = 
{
	{"No","Kode","Nama","Keterangan","Daftar Sales","Sales","Comm.%","ID Pemakai","Password","Assign Lokasi Gudang","Standart Mata Uang","Pegawai"},
	{"No","Code","Name","Remark","Sales List","Sales","Comm.%","User Id","Password","Assign Location Warehouse","Default Currency","Employee"}
};

/* this method used to list material unit */
public String drawList(int language,int iCommand,FrmSales frmObject,Sales objEntity,Vector objectClass,long unitId,int start)
{
	ControlList ctrlist = new ControlList();
	ctrlist.setAreaWidth("100%");
	ctrlist.setListStyle("listgen");
	ctrlist.setTitleStyle("listgentitle");
	ctrlist.setCellStyle("listgensell");
	ctrlist.setHeaderStyle("listgentitle");
	ctrlist.addHeader(textListHeader[language][0],"4%");
	ctrlist.addHeader(textListHeader[language][1],"6%");
	ctrlist.addHeader(textListHeader[language][2],"20%");	
        ctrlist.addHeader(textListHeader[language][6],"10%");
        ctrlist.addHeader(textListHeader[language][7],"10%");
        ctrlist.addHeader(textListHeader[language][8],"10%"); 
        ctrlist.addHeader(textListHeader[language][11],"10%");
        ctrlist.addHeader(textListHeader[language][9],"20%");
        ctrlist.addHeader("Status","5%");
        ctrlist.addHeader(textListHeader[language][10],"20%");
        ctrlist.addHeader(textListHeader[language][3],"20%");        

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
        Vector val_base=new Vector();
        Vector key_base=new Vector();

        Vector val_curr=new Vector();
        Vector key_curr=new Vector();
        
        Vector val_emp=new Vector();
        Vector key_emp=new Vector();
        
        Vector val_status=new Vector();
        Vector key_status=new Vector();
        
        String whereLoc ="";// PstLocation.fieldNames[PstLocation.FLD_TYPE]+"='"+PstLocation.TYPE_LOCATION_WAREHOUSE+"'";
        Vector listLocation = PstLocation.list(0,0,whereLoc,"");
        Vector listCurrency =PstStandartRate.getActiveStandardRateSales();
        Vector listEmployee =PstEmployee.list(0, 0, "", "" + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + " ASC ");
        
	      val_base.add("0");
        key_base.add("-");

        val_curr.add("0");
        key_curr.add("-");
        
        val_emp.add("0");
        key_emp.add("-");
        
        String statusTitle[] = {"Aktif","Tidak Aktif"};
                
        val_status.add("0");
        key_status.add("Aktif");
        val_status.add("1");
        key_status.add("Tidak Aktif");

        for(int i=0;i<listLocation.size();i++){
            Location xLocation = (Location)listLocation.get(i);
            val_base.add(""+xLocation.getOID()+"");
            key_base.add(xLocation.getName());
        }

        for(int i=0;i<listCurrency.size();i++){
            StandartRate standartRate = (StandartRate)listCurrency.get(i);
            val_curr.add(""+standartRate.getCurrencyTypeId()+"");
            key_curr.add(standartRate.getCurrencyName());
        }
        
        for(int i=0;i<listEmployee.size();i++){
            Employee employee = (Employee)listEmployee.get(i);
            val_emp.add(""+employee.getOID()+"");
            key_emp.add(employee.getFullName());
        }
        
	for(int i = 0; i < objectClass.size(); i++) 
	{
            Sales matSales = (Sales)objectClass.get(i);
            rowx = new Vector();
            if(unitId == matSales.getOID())
            index = i; 

            start = start + 1;

            String locationName = "-";
            for(int k=0;k<listLocation.size();k++){
            Location xLocation = (Location)listLocation.get(k);
            if(xLocation.getOID()==matSales.getLocationId()){
                locationName = xLocation.getName();
                break;
                }
            }

            String defaultCurrency = "-";
            for(int k=0;k<listCurrency.size();k++){
            StandartRate standartRate = (StandartRate)listCurrency.get(k);
            if(standartRate.getCurrencyTypeId()==matSales.getDefaultCurrencyId()){
                defaultCurrency = standartRate.getCurrencyName();
                break;
                }
            }

            String employeeName = "-";
            for(int k=0;k<listEmployee.size();k++){
            Employee employee = (Employee)listEmployee.get(k);
            if(employee.getOID()==matSales.getEmployeeId()){
                employeeName = employee.getFullName();
                break;
                }
            }

            if(index == i && (iCommand == Command.EDIT || iCommand == Command.ASK  || (iCommand == Command.SAVE && frmObject.errorSize() > 0)))
            {				
                rowx.add(""+start);
                rowx.add("<input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[FrmSales.FRM_FIELD_CODE] +"\" value=\""+matSales.getCode()+"\" class=\"formElemen\">");
                rowx.add("<input type=\"text\" size=\"10\" name=\""+frmObject.fieldNames[FrmSales.FRM_FIELD_NAME] +"\" value=\""+matSales.getName()+"\" class=\"formElemen\">");			
                rowx.add("<input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[FrmSales.FRM_FIELD_COMMISION] +"\" value=\""+matSales.getCommision()+"\" class=\"formElemen\">");
                //adding login id & password by mirahu 20120514
                rowx.add("<input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[FrmSales.FRM_FIELD_LOGIN_ID] +"\" value=\""+matSales.getLoginId()+"\" class=\"formElemen\">");
                rowx.add("<input type=\"password\" size=\"5\" name=\""+frmObject.fieldNames[FrmSales.FRM_FIELD_PASSWORD] +"\" value=\""+matSales.getPassword()+"\" class=\"formElemen\">");
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmSales.FRM_FIELD_EMPLOYEE_ID], null, ""+matSales.getEmployeeId(),val_emp,key_emp,"","formElemen select2"));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmSales.FRM_FIELD_LOCATION_ID], null, ""+matSales.getLocationId(),val_base,key_base,"","formElemen"));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmSales.FRM_FIELD_STATUS], null, ""+matSales.getStatus(),val_status,key_status,"","formElemen"));
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmSales.FRM_FIELD_CURRENCY_ID], null, ""+matSales.getDefaultCurrencyId(),val_curr,key_curr,"","formElemen"));
                rowx.add("<input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[FrmSales.FRM_FIELD_REMARK] +"\" value=\""+matSales.getRemark()+"\" class=\"formElemen\">");
            }else{
                rowx.add(""+start);
                rowx.add("<a href=\"javascript:cmdEdit('"+String.valueOf(matSales.getOID())+"')\">"+matSales.getCode()+"</a>");
                rowx.add(matSales.getName());			
                rowx.add(String.valueOf(matSales.getCommision()));
                //adding login id & password by mirahu 20120514
                rowx.add(matSales.getLoginId());
                rowx.add("*****");
                rowx.add(employeeName);
                rowx.add(locationName);
                rowx.add(statusTitle[matSales.getStatus()]);
                rowx.add(defaultCurrency);
                rowx.add(matSales.getRemark());
            }

            lstData.add(rowx);
	}
	
	rowx = new Vector();
       
        
	if(index==-1 && (iCommand == Command.ADD || (iCommand == Command.SAVE && frmObject.errorSize() > 0)))
	{ 
            //added by dewok 2018-04-25 for litama
            //-----kode otomatis
            String kode = "";
            String format = "10000";
            int count = 1 + PstSales.getCount("");
            try {            
                kode = format.substring(0, format.length() - String.valueOf(count).length()) + count;
            } catch (Exception e) {
                kode = "" + count;
            }
            //----end of kode otomatis
            rowx.add(""+(start+1));
            rowx.add("<input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[FrmSales.FRM_FIELD_CODE] +"\" value=\""+kode+"\" class=\"formElemen\">");
            rowx.add("<input type=\"text\" size=\"10\" name=\""+frmObject.fieldNames[FrmSales.FRM_FIELD_NAME] +"\" value=\""+"\" class=\"formElemen\">");			
            rowx.add("<input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[FrmSales.FRM_FIELD_COMMISION] +"\" value=\""+"\" class=\"formElemen\">");
            //adding login id & password by mirahu 20120514
            rowx.add("<input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[FrmSales.FRM_FIELD_LOGIN_ID] +"\" value=\""+"\" class=\"formElemen\">");
            rowx.add("<input type=\"password\" size=\"5\" name=\""+frmObject.fieldNames[FrmSales.FRM_FIELD_PASSWORD] +"\" value=\""+"\" class=\"formElemen\">");
            rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmSales.FRM_FIELD_EMPLOYEE_ID], null, "",val_emp,key_emp,"","formElemen select2"));
            rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmSales.FRM_FIELD_LOCATION_ID], null, "",val_base,key_base,"","formElemen"));
            rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmSales.FRM_FIELD_STATUS], null, "",val_status,key_status,"","formElemen"));
            rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmSales.FRM_FIELD_CURRENCY_ID], null, "",val_curr,key_curr,"","formElemen"));
            rowx.add("<input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[FrmSales.FRM_FIELD_REMARK] +"\" value=\""+"\" class=\"formElemen\">");

            lstData.add(rowx);
        }
        
        return ctrlist.draw();
}
%>
<%
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int prevCommand = FRMQueryString.requestInt(request, "prev_command");
long oidSales = FRMQueryString.requestLong(request, "hidden_sales_id");
String salesTitle = textListHeader[SESS_LANGUAGE][5]; //com.dimata.posbo.jsp.JspInfo.txtMaterialInfo[SESS_LANGUAGE][com.dimata.posbo.jsp.JspInfo.MATERIAL_SALES];

/*variable declaration*/
int recordToGet = 20;
String msgString = "";
int iErrCode = FRMMessage.NONE;
String whereClause = "";
String orderClause = "" + PstSales.fieldNames[PstSales.FLD_CODE];

CtrlSales ctrlSales = new CtrlSales(request);
ControlLine ctrLine = new ControlLine();
Vector listSales = new Vector(1,1);

/*switch statement */
iErrCode = ctrlSales.action(iCommand , oidSales);
/* end switch*/
FrmSales frmSales = ctrlSales.getForm();

/*count list All Sales*/
int vectSize = PstSales.getCount(whereClause);

/*switch list Sales*/
if((iCommand == Command.FIRST || iCommand == Command.PREV )||
  (iCommand == Command.NEXT || iCommand == Command.LAST))
  {
		start = ctrlSales.actionList(iCommand, start, vectSize, recordToGet);
  } 
/* end switch list*/

Sales matSales = ctrlSales.getSales();
msgString =  ctrlSales.getMessage();

/* get record to display */
listSales = PstSales.list(start,recordToGet, whereClause , orderClause);

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
	 listSales = PstSales.list(start,recordToGet, whereClause , orderClause);
}
%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<%@include file="../styles/plugin_component.jsp" %>
<script language="JavaScript">

function cmdAdd()
{
	document.frmmatsales.hidden_sales_id.value="0";
	document.frmmatsales.command.value="<%=Command.ADD%>";
	document.frmmatsales.prev_command.value="<%=prevCommand%>";
	document.frmmatsales.action="sales.jsp";
	document.frmmatsales.submit();
}

function cmdAsk(oidSales)
{
	document.frmmatsales.hidden_sales_id.value=oidSales;
	document.frmmatsales.command.value="<%=Command.ASK%>";
	document.frmmatsales.prev_command.value="<%=prevCommand%>";
	document.frmmatsales.action="sales.jsp";
	document.frmmatsales.submit();
}

function cmdConfirmDelete(oidSales)
{
	document.frmmatsales.hidden_sales_id.value=oidSales;
	document.frmmatsales.command.value="<%=Command.DELETE%>";
	document.frmmatsales.prev_command.value="<%=prevCommand%>";
	document.frmmatsales.action="sales.jsp";
	document.frmmatsales.submit();
}

function cmdSave()
{
	document.frmmatsales.command.value="<%=Command.SAVE%>";
	document.frmmatsales.prev_command.value="<%=prevCommand%>";
	document.frmmatsales.action="sales.jsp";
	document.frmmatsales.submit();
}

function cmdEdit(oidSales)
{
	document.frmmatsales.hidden_sales_id.value=oidSales;
	document.frmmatsales.command.value="<%=Command.EDIT%>";
	document.frmmatsales.prev_command.value="<%=prevCommand%>";
	document.frmmatsales.action="sales.jsp";
	document.frmmatsales.submit();
}

function cmdCancel(oidSales)
{
	document.frmmatsales.hidden_sales_id.value=oidSales;
	document.frmmatsales.command.value="<%=Command.EDIT%>";
	document.frmmatsales.prev_command.value="<%=prevCommand%>";
	document.frmmatsales.action="sales.jsp";
	document.frmmatsales.submit();
}

function cmdBack()
{
	document.frmmatsales.command.value="<%=Command.BACK%>";
	document.frmmatsales.action="sales.jsp";
	document.frmmatsales.submit();
}

function cmdListFirst()
{
	document.frmmatsales.command.value="<%=Command.FIRST%>";
	document.frmmatsales.prev_command.value="<%=Command.FIRST%>";
	document.frmmatsales.action="sales.jsp";
	document.frmmatsales.submit();
}

function cmdListPrev()
{
	document.frmmatsales.command.value="<%=Command.PREV%>";
	document.frmmatsales.prev_command.value="<%=Command.PREV%>";
	document.frmmatsales.action="sales.jsp";
	document.frmmatsales.submit();
}

function cmdListNext()
{
	document.frmmatsales.command.value="<%=Command.NEXT%>";
	document.frmmatsales.prev_command.value="<%=Command.NEXT%>";
	document.frmmatsales.action="sales.jsp";
	document.frmmatsales.submit();
}

function cmdListLast()
{
	document.frmmatsales.command.value="<%=Command.LAST%>";
	document.frmmatsales.prev_command.value="<%=Command.LAST%>";
	document.frmmatsales.action="sales.jsp";
	document.frmmatsales.submit();
}

//-------------- script form image -------------------

function cmdDelPict(oidSales)
{
	document.frmimage.hidden_sales_id.value=oidSales;
	document.frmimage.command.value="<%=Command.POST%>";
	document.frmimage.action="sales.jsp";
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
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<!-- #BeginEditable "styles" -->

<%if(menuUsed == MENU_ICON){%>
    <link href="../stylesheets/general_home_style.css" type="text/css"
rel="stylesheet" />
<%}%>
<link rel="stylesheet" href="../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" --> 
<link rel="stylesheet" href="../styles/tab.css" type="text/css">
<link rel="stylesheet" href="../styles/select2/css/select2.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "headerscript" --> 
<script type="text/javascript" src="../styles/select2/js/select2.full.min.js"></script>
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
<script>
    $(document).ready(function () {
        $('.select2').select2();
    });
</script>
<!-- #EndEditable --> 
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100" bgcolor="#FCFDEC" >
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
    <section class="content-header">
      <h1>Masterdata <small> <%=textListHeader[SESS_LANGUAGE][4]%></small> </h1>
      <ol class="breadcrumb">
        <li><%=textListHeader[SESS_LANGUAGE][4]%></li>
      </ol>
    </section>
   <p class="line"></p>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frmmatsales" method ="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="vectSize" value="<%=vectSize%>">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="hidden_sales_id" value="<%=oidSales%>">
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr align="left" valign="top"> 
                  <td height="8"  colspan="3"> 
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <%
							try{
							%>
       
                     <input id="myInput" class="form-control" type="text" placeholder="Search..">
                      <tr align="left" valign="top"> 
                        <td height="22" valign="middle" colspan="3"> <%=drawList(SESS_LANGUAGE,iCommand,frmSales, matSales,listSales,oidSales,start)%> </td>
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
		                      <%if(privAdd){%>					  							
                              <!--td width="18%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,salesTitle,ctrLine.CMD_ADD,true)%>"></a></td-->
                              <td nowrap width="82%"><a class="btn btn-lg btn-primary" href="javascript:cmdAdd()" class="command"><i class="fa fa-plus-circle"></i> <%=ctrLine.getCommand(SESS_LANGUAGE,textListHeader[SESS_LANGUAGE][5],ctrLine.CMD_ADD,true)%></a></td>
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
									ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,salesTitle,ctrLine.CMD_SAVE,true));
									ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,salesTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,salesTitle,ctrLine.CMD_BACK,true)+" List");							
									ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,salesTitle,ctrLine.CMD_ASK,true));							
									ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,salesTitle,ctrLine.CMD_CANCEL,false));														
									
									ctrLine.initDefault();
									ctrLine.setTableWidth("80%");
									String scomDel = "javascript:cmdAsk('"+oidSales+"')";
									String sconDelCom = "javascript:cmdConfirmDelete('"+oidSales+"')";
									String scancel = "javascript:cmdEdit('"+oidSales+"')";
									ctrLine.setCommandStyle("command");
									ctrLine.setColCommStyle("command");
									
									// set command caption
									ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE,salesTitle,ctrLine.CMD_SAVE,true));
									ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,salesTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,salesTitle,ctrLine.CMD_BACK,true)+" List");							
									ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE,salesTitle,ctrLine.CMD_ASK,true));							
									ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE,salesTitle,ctrLine.CMD_DELETE,true));														
									ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE,salesTitle,ctrLine.CMD_CANCEL,false));									

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
            <%@include file="../styletemplate/footer.jsp" %>

        <%}else{%>
            <%@ include file = "../main/footer.jsp" %>
        <%}%>
      <!-- #EndEditable --> </td>
  </tr>
</table>
      <script>
 $(document).ready(function () {
		$('.listgen').DataTable({
			"ordering": false
		});
 });
      </script>
</body>
<!-- #EndTemplate --></html>

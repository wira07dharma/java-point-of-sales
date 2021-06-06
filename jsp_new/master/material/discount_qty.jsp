<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package material -->
<%@ page import = "com.dimata.posbo.entity.masterdata.DiscountQtyMapping" %>
<%@ page import = "com.dimata.posbo.entity.masterdata.PstDiscountQtyMapping" %>
<%@ page import = "com.dimata.posbo.entity.masterdata.Material" %>
<%@ page import = "com.dimata.posbo.entity.masterdata.PstMaterial" %>
<%@ page import = "com.dimata.posbo.form.masterdata.CtrlDiscountQtyMapping" %>
<%@ page import = "com.dimata.posbo.form.masterdata.FrmDiscountQtyMapping" %>
<%@ page import = "com.dimata.common.entity.location.Location" %>
<%@ page import = "com.dimata.common.entity.location.PstLocation" %>
<%@ page import = "com.dimata.common.entity.payment.CurrencyType" %>
<%@ page import = "com.dimata.common.entity.payment.PstCurrencyType" %>
<%@ page import = "com.dimata.common.entity.payment.DiscountType" %>
<%@ page import = "com.dimata.common.entity.payment.PstDiscountType" %>
<%@ page import = "com.dimata.common.form.location.*" %>
<%@ page import = "com.dimata.common.form.payment.*" %>

<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_CATALOG); %>
<%@ include file = "../../main/checkuser.jsp" %>



<!-- Jsp Block -->
<%!
public static int MAX_LIST_QTY_DISC = 8;
/* this constant used to list text of listHeader */
public static final String textListHeader[][] =
{
	{"No","START QTY","TO QTY","DISKON","TIPE DISKON","Editor Barang"},
	{"No","START QTY","TO QTY","DISCOUNT","DISCOUNT TYPE","Goods Editor"}
};

public static final String textListGlobalHeader[][] =
{
	{"DAFTAR QTY DISKON","LOKASI","MATA UANG","TIPE MEMBER","NAMA BARANG", "Qty Diskon", "Berlaku juga untuk lokasi","Berlaku Juga Untuk Type Member"},
	{"DISCOUNT QTY LIST","LOCATION","CURRENCY","MEMBER TYPE","MATERIAL NAME", "Qty Discount", "Also for location","Also for Type Member"}
};



//Vector
//Vector listAll = new Vector(1, 1);
public Vector drawListVector(int language,int iCommand,FrmDiscountQtyMapping frmObject,DiscountQtyMapping objEntity,Vector objectClass,int start)
{
        String result = "";
        Vector listAll = new Vector(1, 1);

	ControlList ctrlist = new ControlList();
	ctrlist.setAreaWidth("70%");
	ctrlist.setListStyle("listgen");
	ctrlist.setTitleStyle("listgentitle");
	ctrlist.setCellStyle("listgensell");
	ctrlist.setHeaderStyle("listgentitle");
	/*ctrlist.addHeader(textListHeader[language][0],"3%");
	ctrlist.addHeader(textListHeader[language][1],"15%");
	ctrlist.addHeader(textListHeader[language][2],"30%");
        ctrlist.addHeader(textListHeader[language][3],"15%");
	ctrlist.addHeader(textListHeader[language][4],"17%");*/

        ctrlist.dataFormat(textListHeader[language][0],"3%","left","left");
        ctrlist.dataFormat(textListHeader[language][1],"20%","left","left");
        ctrlist.dataFormat(textListHeader[language][2],"15%","left","left");
        ctrlist.dataFormat(textListHeader[language][3],"15%","left","left");
        ctrlist.dataFormat(textListHeader[language][4],"17%","left","left");

	ctrlist.setLinkRow(0);
	ctrlist.setLinkPrefix("javascript:cmdEdit('");
	ctrlist.setLinkSufix("')");
	Vector lstData = ctrlist.getData();
	Vector lstLinkData = ctrlist.getLinkData();
	Vector rowx = new Vector(1,1);
	ctrlist.reset();
	int index = -1;
	if(start<0)
		start = 0;

    //get Tipe Diskon
    Vector obj_base = new Vector(1,1); //vector of object to be listed
    Vector val_base = new Vector(1,1); //hidden values that will be deliver on request (oids)
    Vector key_base = new Vector(1,1); //texts that displayed on combo box

    val_base.add("-1");
    key_base.add("");
    val_base.add("0");
    key_base.add("" + PstDiscountQtyMapping.typeDiscount[PstDiscountQtyMapping.PCT]);
    val_base.add("1");
    key_base.add("" + PstDiscountQtyMapping.typeDiscount[PstDiscountQtyMapping.ITEM_UNIT]);
    val_base.add("2");
    key_base.add("" + PstDiscountQtyMapping.typeDiscount[PstDiscountQtyMapping.ITEM_BUYING]);

    for(int i = 0; i < MAX_LIST_QTY_DISC; i++)
	{
                  DiscountQtyMapping discQty = null;
                  if(i<objectClass.size()){
                       discQty = (DiscountQtyMapping)objectClass.get(i);
                      } else {
                            discQty= new DiscountQtyMapping();
                  }
		 
                        rowx = new Vector();

                        start = start + 1;
			rowx.add("" + start);
			rowx.add("<div align=\"center\"><input type=\"text\" size=\"10\" name=\""+frmObject.fieldNames[FrmDiscountQtyMapping.FRM_FIELD_START_QTY]+i +"\" value=\""+FRMHandler.userFormatStringDecimal(discQty.getStartQty())+"\" class=\"formElemen\" style=\"text-align:right\"></div>");
			rowx.add("<div align=\"center\"><input type=\"text\" size=\"10\" name=\""+frmObject.fieldNames[FrmDiscountQtyMapping.FRM_FIELD_TO_QTY]+i +"\" value=\""+FRMHandler.userFormatStringDecimal(discQty.getToQty())+"\" class=\"formElemen\" style=\"text-align:right\"></div>");
                        rowx.add("<div align=\"center\"><input type=\"text\" size=\"17\" name=\""+frmObject.fieldNames[FrmDiscountQtyMapping.FRM_FIELD_DISCOUNT_VALUE]+i +"\" value=\""+FRMHandler.userFormatStringDecimal(discQty.getDiscountValue())+"\" class=\"formElemen\" style=\"text-align:right\">  &nbsp;</div>");
                        rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmDiscountQtyMapping.FRM_FIELD_DISCOUNT_TYPE]+i, null, ""+discQty.getDiscountType(),val_base,key_base,"","formElemen"));
                        //rowx.add("<div align=\"right\"><input type=\"text\" size=\"10\" name=\""+frmObject.fieldNames[FrmDiscountQtyMapping.FRM_FIELD_DISCOUNT_VALUE] +"\" value=\"\" class=\"formElemen\" style=\"text-align:right\">  &nbsp;</div>");
              lstData.add(rowx);
            }
         
	result = ctrlist.draw();
        listAll.add(result);
       return listAll;
}


%>
<%

int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int prevCommand = FRMQueryString.requestInt(request, "prev_command");
long oidCurrType = FRMQueryString.requestLong(request, "hidden_currency_type_id");
long oidDiscType = FRMQueryString.requestLong(request, "hidden_discount_type_id");
long oidLocation = FRMQueryString.requestLong(request, "hidden_location_id");
long oidMaterial = FRMQueryString.requestLong(request, "hidden_material_id");
String merkTitle = textListHeader[SESS_LANGUAGE][5]; //com.dimata.posbo.jsp.JspInfo.txtMaterialInfo[SESS_LANGUAGE][com.dimata.posbo.jsp.JspInfo.MATERIAL_UNIT];
String saveTitle = textListGlobalHeader[SESS_LANGUAGE][5];

/*variable declaration*/
int recordToGet = 8;
String msgString = "";
int iErrCode = FRMMessage.NONE;
int iErrCode2 = FRMMessage.NONE;
String whereClause = PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_DISCOUNT_TYPE_ID]+
                      " = "+oidDiscType+
                      " AND "+PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_CURRENCY_TYPE_ID]+
                      " = "+oidCurrType+
                      " AND "+PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_LOCATION_ID]+
                      " = "+oidLocation+
                      " AND "+PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_MATERIAL_ID]+
                      " = "+oidMaterial;
//String ordDiscQty = PstDiscountQtyMapping.fieldNames[PstDiscountQtyMapping.FLD_LOCATION_ID];
//String whereClause = "";
//String orderClause = "CODE";

CtrlDiscountQtyMapping ctrlDiscountQtyMapping = new CtrlDiscountQtyMapping(request);
ControlLine ctrLine = new ControlLine();
Vector listDiscQty = new Vector(1,1);

/*switch statement */
//ctrlDiscountQtyMapping.action(cmd, listDiscQty, oidDiscountType, oidCurrencyType, oidLocation, oidMaterial, request, size, userID, nameUser);
iErrCode = ctrlDiscountQtyMapping.action(iCommand, listDiscQty, oidDiscType, oidCurrType, oidLocation, oidMaterial, request, MAX_LIST_QTY_DISC,userId,userName);
//iErrCode = ctrlDiscountQtyMapping.action(iCommand, list);
/* end switch*/
FrmDiscountQtyMapping frmDiscQtyMapping = ctrlDiscountQtyMapping.getForm();



/*count list All Unit*/
int vectSize = PstDiscountQtyMapping.getCount(whereClause);

/*switch list Unit*/
//if((iCommand == Command.FIRST || iCommand == Command.PREV )||
  //(iCommand == Command.NEXT || iCommand == Command.LAST))
  //{
		//start = ctrlUnit.actionList(iCommand, start, vectSize, recordToGet);
  //}
/* end switch list*/

DiscountQtyMapping discQtyMapping = ctrlDiscountQtyMapping.getDiscountQtyMapping();
msgString =  ctrlDiscountQtyMapping.getMessage();
Vector listDiscQtyMapping = ctrlDiscountQtyMapping.getDiscQtyMapping();

/* get record to display */

 

listDiscQty = PstDiscountQtyMapping.list(start,recordToGet, whereClause , "");

/*handle condition if size of record to display = 0 and start > 0 	after delete*/
if (listDiscQty.size() < 1 && start > 0)
{
	 if (vectSize - recordToGet > recordToGet)
			start = start - recordToGet;   //go to Command.PREV
	 else
	 {
		 start = 0 ;
		 iCommand = Command.FIRST;
		 prevCommand = Command.FIRST; //go to Command.FIRST
	 }
	 //listDiscQty = PstUnit.list(start,recordToGet, whereClause , "");
}

Location location = new Location();
    try	{
        location = PstLocation.fetchExc(oidLocation);
    } catch(Exception e) {
 }

CurrencyType currType = new CurrencyType();
    try	{
        currType = PstCurrencyType.fetchExc(oidCurrType);
    } catch(Exception e) {
 }

DiscountType discType = new DiscountType();
    try	{
        discType = PstDiscountType.fetchExc(oidDiscType);
    } catch(Exception e) {
 }

Material material = new Material();
    try	{
        material = PstMaterial.fetchExc(oidMaterial);
    } catch(Exception e) {
 }

Vector list = drawListVector(SESS_LANGUAGE,iCommand,frmDiscQtyMapping, discQtyMapping, listDiscQty,start);
String str = "";


try{
    str = (String)list.get(0);
}catch(Exception e) {
}

%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">

function cmdSave()
{
	document.frmmaterial.command.value="<%=Command.SAVE%>";
	document.frmmaterial.prev_command.value="<%=prevCommand%>";
	document.frmmaterial.action="discount_qty.jsp";
	document.frmmaterial.submit();
        //self.close();
}

function cmdEdit(oidUnit)
{
	document.frmdiscqty.hidden_merk_id.value=oidUnit;
	document.frmdiscqty.command.value="<%=Command.EDIT%>";
	document.frmdiscqty.prev_command.value="<%=prevCommand%>";
	document.frmdiscqty.action="discount_qty.jsp";
	document.frmdiscqty.submit();
}

function cmdBack()
{
        //document.frmdiscqty.hidden_material_id.value = oidMaterial;
	//document.frmdiscqty.command.value="<%=Command.BACK%>";
	//document.frmdiscqty.action="material_main.jsp";
	//document.frmdiscqty.submit();
        self.close();
}

function cmdListFirst()
{
	document.frmdiscqty.command.value="<%=Command.FIRST%>";
	document.frmdiscqty.prev_command.value="<%=Command.FIRST%>";
	document.frmdiscqty.action="discount_qty.jsp";
	document.frmdiscqty.submit();
}

function cmdListPrev()
{
	document.frmdiscqty.command.value="<%=Command.PREV%>";
	document.frmdiscqty.prev_command.value="<%=Command.PREV%>";
	document.frmdiscqty.action="discount_qty.jsp";
	document.frmdiscqty.submit();
}

function cmdListNext()
{
	document.frmdiscqty.command.value="<%=Command.NEXT%>";
	document.frmdiscqty.prev_command.value="<%=Command.NEXT%>";
	document.frmdiscqty.action="discount_qty.jsp";
	document.frmdiscqty.submit();
}

function cmdListLast()
{
	document.frmdiscqty.command.value="<%=Command.LAST%>";
	document.frmdiscqty.prev_command.value="<%=Command.LAST%>";
	document.frmdiscqty.action="discount_qty.jsp";
	document.frmdiscqty.submit();
}

function setChecked(val) {
	dml=document.frmmaterial;
	len = dml.elements.length;

	var i=0;
	for( i=0 ; i<len ; i++) {
            <%
               String where2 = PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+
                             " != "+oidLocation;
               Vector vt_loc2 = PstLocation.list(0,0,where2,PstLocation.fieldNames[PstLocation.FLD_NAME]);
               for(int i=0; i<vt_loc2.size(); i++) {
                   Location location2 = (Location)vt_loc2.get(i);
            %>
                   if (dml.elements[i].name == '<%=frmDiscQtyMapping.fieldNames[frmDiscQtyMapping.FRM_FIELD_LOCATION_ID]%>') {
			if(val==1)
                            dml.elements[i].checked = <%=i+1%>;
			else
                            dml.elements[i].checked = val;
		   }
            <%}%>
	}
}


function setCheckedTypeMember(val) {
	dml=document.frmmaterial;
	len = dml.elements.length;

	var i=0;
	for( i=0 ; i<len ; i++) {
            <%
               String where3 = PstDiscountType.fieldNames[PstDiscountType.FLD_DISCOUNT_TYPE_ID]+ " !='"+discType.getOID() +"'";
               Vector listDscTypex = PstDiscountType.list(0,0,where3,"");
               for(int i=0; i<listDscTypex.size(); i++) {
                    DiscountType dscType = (DiscountType)listDscTypex.get(i);
            %>
                   if (dml.elements[i].name == '<%=frmDiscQtyMapping.fieldNames[frmDiscQtyMapping.FRM_FIELD_DISCOUNT_TYPE_ID]%>') {
			if(val==1)
                            dml.elements[i].checked = <%=i+1%>;
			else
                            dml.elements[i].checked = val;
		   }
            <%}%>
	}
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
<link rel="stylesheet" href="../../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" -->
<link rel="stylesheet" href="../../styles/tab.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "headerscript" -->
<SCRIPT language=JavaScript>
</SCRIPT>
<!-- #EndEditable --> 
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
  <tr> 
    <td width="88%" valign="top" align="left">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">  
        <tr> 
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->
             <!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" -->
            <form name="frmmaterial" method ="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="vectSize" value="<%=vectSize%>">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="hidden_currency_type_id" value="<%=oidCurrType%>">
              <input type="hidden" name="hidden_discount_type_id" value="<%=oidDiscType%>">
              <input type="hidden" name="hidden_location_id" value="<%=oidLocation%>">
              <input type="hidden" name="hidden_material_id" value="<%=oidMaterial%>">
              <input type="hidden" name="<%=FrmDiscountQtyMapping.fieldNames[FrmDiscountQtyMapping.FRM_FIELD_CURRENCY_TYPE_ID]%>" value="<%=oidCurrType%>">
              <input type="hidden" name="<%=FrmDiscountQtyMapping.fieldNames[FrmDiscountQtyMapping.FRM_FIELD_DISCOUNT_TYPE_ID]%>" value="<%=oidDiscType%>">
              <input type="hidden" name="<%=FrmDiscountQtyMapping.fieldNames[FrmDiscountQtyMapping.FRM_FIELD_LOCATION_ID]%>" value="<%=oidLocation%>">
              <input type="hidden" name="<%=FrmDiscountQtyMapping.fieldNames[FrmDiscountQtyMapping.FRM_FIELD_MATERIAL_ID]%>" value="<%=oidMaterial%>">
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr align="left" valign="top">
                  <td height="8"  colspan="3">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr align="left" valign="top">
                        <td height="14" valign="middle" colspan="4" align="center">
                          <h4><strong><%=textListGlobalHeader[SESS_LANGUAGE][0]%></strong></h4>
                          <hr size="1">                        </td>
                      </tr>
                      <tr align="left" valign="top">
                        <td width="21%" height="14" valign="middle" class="command"><b><%=textListGlobalHeader[SESS_LANGUAGE][1]%></b></td>
                        <td width="1%" height="14" valign="middle" class="command">:</td>
                        <td width="48%" height="14" valign="middle" class="command"><b><%=location.getName().toUpperCase()%></b></td>
                        <td width="30%" height="14" valign="middle" class="command">&nbsp;</td>
                      </tr>
                      <tr align="left" valign="top">
                        <td width="21%" height="14" valign="middle" class="command"><b><%=textListGlobalHeader[SESS_LANGUAGE][2]%></b></td>
                        <td width="1%" height="14" valign="middle" class="command">:</td>
                        <td width="48%" height="14" valign="middle" class="command"><b><%=currType.getCode().toUpperCase()%></b></td>
                        <td width="30%" height="14" valign="middle" class="command">&nbsp;</td>
                        
                      </tr>
                      <tr align="left" valign="top">
                        <td width="21%" height="14" valign="middle" class="command"><b><%=textListGlobalHeader[SESS_LANGUAGE][3]%></b></td>
                        <td width="1%" height="14" valign="middle" class="command">:</td>
                        <td width="48%" height="14" valign="middle" class="command"><b><%=discType.getCode().toUpperCase()%></b></td>
                        <td width="30%" height="14" valign="middle" class="command">&nbsp;</td>
                        
                      </tr>
                      <tr align="left" valign="top">
                        <td width="21%" height="14" valign="middle" class="command"><b><%=textListGlobalHeader[SESS_LANGUAGE][4]%></b></td>
                        <td width="1%" height="14" valign="middle" class="command">:</td>
                        <td width="48%" height="14" valign="middle" class="command"><b><%=material.getName().toUpperCase()%></b></td>
                        <td width="30%" height="14" valign="middle" class="command">&nbsp;</td>
                      </tr>
                      <%
			try{
		      %>
                      <tr align="left" valign="top">
                        <td height="22" valign="middle" colspan="4"> <%=str%> </td>
                      </tr>
                      <%
						  }catch(Exception exc){
						  }%>
                      <tr>
                        <td width="21%" height="14" valign="middle" class="command"><b><%=textListGlobalHeader[SESS_LANGUAGE][6]%></b></td>
                        <td width="1%" height="14" valign="middle" class="command">:</td>
                        <td width="48%" height="14" valign="left" class="command">
			<%
                               //update opie-eyek 20130809
                               String where = PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+ " != "+oidLocation +" AND "+PstLocation.fieldNames[PstLocation.FLD_TYPE]+"='"+PstLocation.TYPE_LOCATION_STORE+"'" ;
                                Vector val_locationid = new Vector(1,1);
                                Vector key_locationid = new Vector(1,1);
                                Vector vt_loc = PstLocation.list(0,0,where,PstLocation.fieldNames[PstLocation.FLD_NAME]);

                                for(int d=0;d<vt_loc.size();d++){
                                Location loc = (Location)vt_loc.get(d);
					val_locationid.add(""+loc.getOID()+"");
					key_locationid.add(loc.getName());
                                        //key_locationid.add(loc.getName());
				}
                              ControlCheckBox controlCheckBox = new ControlCheckBox();
                              controlCheckBox.setWidth(5);

                          %>
                          <%=controlCheckBox.draw(frmDiscQtyMapping.fieldNames[frmDiscQtyMapping.FRM_FIELD_LOCATION_ID],val_locationid,key_locationid,new Vector())%>
                        </td>
                        <td width="30%" height="14" valign="middle" class="command">&nbsp;</td>
                      </tr>

                       <tr>
                        <td colspan="2" width="21%" height="14" valign="middle" class="command"><b><a href="javascript:setChecked(1)" class="menuLink">select all</a> | <a href="javascript:setChecked(null)" class="menuLink">release all</a></b></td>
                        <td width="1%" height="14" valign="middle" class="command">&nbsp</td>
                        <td width="48%" height="14" valign="left" class="command">&nbsp</td>
                       </tr>

                       <%--update opie-eyek 20130809 berlaku untuk type member--%>
                       <tr align="left" valign="top">
                        <td width="21%" height="14" valign="middle" class="command">&nbsp;</td>
                        <td width="1%" height="14" valign="middle" class="command">&nbsp;</td>
                        <td width="48%" height="14" valign="middle" class="command">&nbsp;</td>
                        <td width="30%" height="14" valign="middle" class="command">&nbsp;</td>
                      </tr>
                       <tr>
                        <td width="21%" height="14" valign="middle" class="command"><b><%=textListGlobalHeader[SESS_LANGUAGE][7]%></b></td>
                        <td width="1%" height="14" valign="middle" class="command">:</td>
                        <td width="48%" height="14" valign="left" class="command">
			<%
                               //update opie-eyek 20130809
                               where = PstDiscountType.fieldNames[PstDiscountType.FLD_DISCOUNT_TYPE_ID]+ " !='"+discType.getOID() +"'";
                               Vector listDscType = PstDiscountType.list(0,0,where,"");
                               Vector val_dictypeid = new Vector(1,1);
                               Vector key_dictypeid = new Vector(1,1);

                               for(int i=0;i<listDscType.size();i++) {
                                       DiscountType dscType = (DiscountType)listDscType.get(i);
					val_dictypeid.add(""+dscType.getOID()+"");
					key_dictypeid.add(dscType.getCode());
				}
                              controlCheckBox = new ControlCheckBox();
                              controlCheckBox.setWidth(5);

                          %>
                          <%=controlCheckBox.draw(frmDiscQtyMapping.fieldNames[frmDiscQtyMapping.FRM_FIELD_DISCOUNT_TYPE_ID],val_dictypeid,key_dictypeid,new Vector())%>
                        </td>
                        <td width="30%" height="14" valign="middle" class="command">&nbsp;</td>
                      </tr>
                      <tr>
                        <td colspan="2" width="21%" height="14" valign="middle" class="command"><b><a href="javascript:setCheckedTypeMember(1)" class="menuLink">select all</a> | <a href="javascript:setCheckedTypeMember(null)" class="menuLink">release all</a></b></td>
                        <td width="1%" height="14" valign="middle" class="command">&nbsp</td>
                        <td width="48%" height="14" valign="left" class="command">&nbsp</td>
                      </tr>
                      <tr align="left" valign="top">
                        <td height="8" align="left" colspan="4" class="command">
                          <span class="command">
                          <%
								   int cmd = 0;
									   //if ((iCommand == Command.FIRST || iCommand == Command.PREV )||
										//(iCommand == Command.NEXT || iCommand == Command.LAST))
											//cmd =iCommand;
								  // else{
									  //if(iCommand == Command.NONE || prevCommand == Command.NONE)
										//cmd = Command.FIRST;
									  //else
									  	//cmd =prevCommand;
								  // }
							    %>
                          <% ctrLine.setLocationImg(approot+"/images");
			     ctrLine.initDefault();
								 %>
                          </span> </td>
                      </tr>
                    </table>
                  </td>
                </tr>
                <tr align="left" valign="top" >
                  <td colspan="3" class="command">
                    <%
									ctrLine.setLocationImg(approot+"/images");

									// set image alternative caption
									ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,saveTitle,ctrLine.CMD_SAVE,true));
									ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,merkTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,merkTitle,ctrLine.CMD_BACK,true)+" List");
									
									ctrLine.initDefault();
									ctrLine.setTableWidth("80%");
									
									ctrLine.setCommandStyle("command");
									ctrLine.setColCommStyle("command");

									// set command caption
									ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE,saveTitle,ctrLine.CMD_SAVE,true));
									ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,merkTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,merkTitle,ctrLine.CMD_BACK,true)+" List");
									

								      // if(privAdd == false  && privUpdate == false){
										//ctrLine.setSaveCaption("");
									//}

									if (privAdd == false){
										ctrLine.setAddCaption("");
									}
									%>
                    <%if(iCommand==Command.ADD || iCommand==Command.EDIT || iCommand==Command.ASK || ((iCommand==Command.SAVE || iCommand==Command.DELETE)&&iErrCode!=FRMMessage.NONE)){%>
                    <%= ctrLine.drawImage(iCommand, iErrCode, msgString)%><%}%></td>
                </tr>
              </table>
            </form>
              <% if(iCommand==Command.SAVE && iErrCode == CtrlDiscountQtyMapping.RSLT_OK ) {
                %>
               <script language="Javascript">
                      self.opener.document.forms.frmmaterial.submit();
                      //self.document.frmmaterial.close();
                      //self.close.document.forms.frmmaterial;
                      //self.opener.frmmaterial.submit();
                      self.close();
               </script>
            <% } %>
            <!-- #EndEditable --></td> 
        </tr> 
      </table>
    </td>
  </tr>
  <!--<tr>
    <td colspan="2" height="20"> <!-- #BeginEditable "footer" -->
      <%//@ include file = "../../main/footer.jsp" %>
      <!-- #EndEditable --> <!--</td>-->
 <!--</tr>-->
</table>
</body>
<!-- #EndTemplate --></html>

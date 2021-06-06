<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package material -->
<%@ page import = "com.dimata.posbo.entity.arap.PaymentTerms" %>
<%@ page import = "com.dimata.posbo.entity.arap.PstPaymentTerms" %>
<%@ page import = "com.dimata.posbo.entity.masterdata.Material" %>
<%@ page import = "com.dimata.posbo.entity.masterdata.PstMaterial" %>
<%@ page import = "com.dimata.posbo.form.arap.CtrlPaymentTerms" %>
<%@ page import = "com.dimata.posbo.form.arap.FrmPaymentTerms" %>
<%@ page import = "com.dimata.common.entity.payment.PaymentSystem"%>
<%@ page import = "com.dimata.common.entity.payment.PstPaymentSystem"%>
<%@ page import = "com.dimata.common.form.payment.FrmPaymentSystem"%>
<%@ page import = "com.dimata.common.entity.location.Location" %>
<%@ page import = "com.dimata.common.entity.location.PstLocation" %>
<%@ page import = "com.dimata.common.entity.payment.CurrencyType" %>
<%@ page import = "com.dimata.common.entity.payment.PstCurrencyType" %>
<%@ page import = "com.dimata.common.form.location.*" %>
<%@ page import = "com.dimata.common.form.payment.*" %>
<%@ page import = "com.dimata.posbo.entity.warehouse.*" %>
<%@ page import = "com.dimata.posbo.form.warehouse.*" %>
<%@ page import = "com.dimata.common.entity.contact.PstContactClass" %>
<%@ page import = "com.dimata.common.entity.contact.PstContactList" %>
<%@ page import = "com.dimata.common.entity.contact.ContactList" %>

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE); %>
<%@ include file = "../../../main/checkuser.jsp" %>



<!-- Jsp Block -->
<%!
public static int MAX_LIST_QTY_DISC = 8;
/* this constant used to list text of listHeader */
public static final String textListHeader[][] =
{
	{"N0","TANGGAL","SISTEM PEMBAYARAN","MATA UANG","RATE","JUMLAH","CATATAN","Daftar ","Jadwal Pembayaran","penerimaan"},
	{"N0","DATE","PAYMENT SYSTEM","CURRENCY","RATE","AMOUNT","NOTE", "List of ","Terms Payment","receive"}
};

/* this constant used to list text of listHeader */
public static final String textListOrderHeader[][] = {
    {"Nomor","Lokasi","Tanggal","Supplier","Status","Keterangan","Nota Supplier","Ppn (%)","Waktu","Mata Uang","Sub Total","Grand Total"},
    {"Number","Location","Date","Supplier","Status","Remark","Supplier Invoice","VAT","Time","Currency","Sub Total","Grand Total"}
};

//Vector
//Vector listAll = new Vector(1, 1);
//Method used to list PaymentTerms
public Vector drawListVectorPaymentTerms(int language,int iCommand,FrmPaymentTerms frmObject,PaymentTerms objEntity,Vector objectClass,int start)
{
        String result = "";
        Vector listAll = new Vector(1, 1);

	ControlList ctrlist = new ControlList();
	ctrlist.setAreaWidth("100%");
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
        ctrlist.dataFormat(textListHeader[language][1],"30%","left","left");
        ctrlist.dataFormat(textListHeader[language][2],"15%","left","left");
        ctrlist.dataFormat(textListHeader[language][3],"15%","left","left");
        ctrlist.dataFormat(textListHeader[language][4],"17%","left","left");
        ctrlist.dataFormat(textListHeader[language][5],"15%","left","left");
        ctrlist.dataFormat(textListHeader[language][6],"17%","left","left");

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


    // get Payment System
    Vector obj_base = new Vector(1,1); //vector of object to be listed
    Vector val_base = new Vector(1,1); //hidden values that will be deliver on request (oids)
    Vector key_base = new Vector(1,1); //texts that displayed on combo box
    Vector vectBase = PstPaymentSystem.list(0,0,"","");

    val_base.add("0");
    key_base.add("- Pilih Sistem Pembayaran -");
    for(int i=0;i<vectBase.size();i++){
        PaymentSystem paymentSystem = (PaymentSystem)vectBase.get(i);
        val_base.add(""+paymentSystem.getOID()+"");
        key_base.add(paymentSystem.getPaymentSystem());
    }

     // get CurrencyType
    Vector obj_currency = new Vector(1,1); //vector of object to be listed
    Vector val_currency = new Vector(1,1); //hidden values that will be deliver on request (oids)
    Vector key_currency = new Vector(1,1); //texts that displayed on combo box
    Vector vectCurrency = PstCurrencyType.list(0,0,"","");

    val_currency.add("0");
    key_currency.add("- Pilih Mata Uang -");
    for(int i=0;i<vectCurrency.size();i++){
        CurrencyType currencyType = (CurrencyType)vectCurrency.get(i);
        val_currency.add(""+currencyType.getOID()+"");
        key_currency.add(currencyType.getCode());
    }



    for(int i = 0; i < MAX_LIST_QTY_DISC; i++)
	{
                  PaymentTerms paymentTerms = null;
                  if(i<objectClass.size()){
                       paymentTerms = (PaymentTerms)objectClass.get(i);
                      } else {
                            paymentTerms= new PaymentTerms();
                  }

                        rowx = new Vector();

                        start = start + 1;
			rowx.add("" + start);
                        Date dt=null;
                         try{
                            dt = paymentTerms.getDueDate();
                            if(dt==null){
                                dt = new Date();
                            }

                        }catch(Exception e){
                            dt = new Date();
                        }

			rowx.add(ControlDate.drawDate(frmObject.fieldNames[FrmPaymentTerms.FRM_DUE_DATE], dt, "formElemen"));
			rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmPaymentTerms.FRM_PAYMENT_SYSTEM_ID]+i, null, ""+paymentTerms.getPaymentSystemId(),val_base,key_base,"","formElemen"));
                        rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmPaymentTerms.FRM_CURRENCY_TYPE_ID]+i, null, ""+paymentTerms.getCurrencyTypeId(),val_currency,key_currency,"","formElemen"));
                        rowx.add("<div align=\"center\"><input type=\"text\" size=\"17\" name=\""+frmObject.fieldNames[FrmPaymentTerms.FRM_RATE]+i +"\" value=\""+FRMHandler.userFormatStringDecimal(paymentTerms.getRate())+"\" class=\"formElemen\" style=\"text-align:right\">  &nbsp;</div>");
                        rowx.add("<div align=\"center\"><input type=\"text\" size=\"17\" name=\""+frmObject.fieldNames[FrmPaymentTerms.FRM_AMOUNT]+i +"\" value=\""+FRMHandler.userFormatStringDecimal(paymentTerms.getAmount())+"\" class=\"formElemen\" style=\"text-align:right\">  &nbsp;</div>");
                        rowx.add("<input type=\"text\" size=\"20\" name=\""+frmObject.fieldNames[FrmPaymentTerms.FRM_NOTE] +"\" value=\""+paymentTerms.getNote()+"\" class=\"formElemen\">");
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
long oidReceiveMaterial = FRMQueryString.requestLong(request, "hidden_receive_id");
long oidPaymentTerms = FRMQueryString.requestLong(request, "hidden_payment_terms_id");

String backTitle = textListHeader[SESS_LANGUAGE][9];
String saveTitle = textListHeader[SESS_LANGUAGE][8];

/*variable declaration*/
int recordToGet = 8;
String msgString = "";
int iErrCode = FRMMessage.NONE;
int iErrCode2 = FRMMessage.NONE;
String whereClause = " TERMS."+PstPaymentTerms.fieldNames[PstPaymentTerms.FLD_RECEIVE_MATERIAL_ID]+
                     " = "+oidReceiveMaterial;
String ordPaymentTerms = PstPaymentTerms.fieldNames[PstPaymentTerms.FLD_DUE_DATE];
//String whereClause = "";
//String orderClause = "CODE";

CtrlPaymentTerms ctrlPaymentTerms = new CtrlPaymentTerms(request);
ControlLine ctrLine = new ControlLine();
Vector listPaymentTerms = new Vector(1,1);



/*switch statement */
iErrCode = ctrlPaymentTerms.action(iCommand, oidPaymentTerms, oidReceiveMaterial, request, MAX_LIST_QTY_DISC);
//iErrCode = ctrlDiscountQtyMapping.action(iCommand, list);
/* end switch*/
FrmPaymentTerms frmPaymentTerms = ctrlPaymentTerms.getForm();



/*count list All Unit*/
int vectSize = PstPaymentTerms.getCount(whereClause);

/*switch list Unit*/
//if((iCommand == Command.FIRST || iCommand == Command.PREV )||
  //(iCommand == Command.NEXT || iCommand == Command.LAST))
  //{
		//start = ctrlUnit.actionList(iCommand, start, vectSize, recordToGet);
  //}
/* end switch list*/

PaymentTerms paymentTerms = ctrlPaymentTerms.getPaymentTerms();
msgString =  ctrlPaymentTerms.getMessage();


/* get record to display */

 

listPaymentTerms = PstPaymentTerms.listPaymentTerms(start, recordToGet,whereClause, ordPaymentTerms);

/*handle condition if size of record to display = 0 and start > 0 	after delete*/
if (listPaymentTerms.size() < 1 && start > 0)
{
	if (vectSize - recordToGet > recordToGet)
			start = start - recordToGet;   //go to Command.PREV
    else
	{
		 start = 0 ;
		 iCommand = Command.FIRST;
		 prevCommand = Command.FIRST; //go to Command.FIRST
	}
	listPaymentTerms = PstPaymentTerms.listPaymentTerms(start, recordToGet,whereClause, ordPaymentTerms);
}


// }

MatReceive rec = new MatReceive();
    try	{
        rec = PstMatReceive.fetchExc(oidReceiveMaterial);
    } catch(Exception e) {
 }

Vector list = drawListVectorPaymentTerms(SESS_LANGUAGE,iCommand,frmPaymentTerms, paymentTerms, listPaymentTerms,start);
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
	document.frm_recmaterial.command.value="<%=Command.SAVE%>";
	document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_recmaterial.action="payment_terms.jsp";
	document.frm_recmaterial.submit();
        //self.close();
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
	document.frm_recmaterial.command.value="<%=Command.FIRST%>";
	document.frm_recmaterial.prev_command.value="<%=Command.FIRST%>";
	document.frm_recmaterial.action="payment_terms.jsp";
	document.frm_recmaterial.submit();
}

function cmdListPrev()
{
	document.frm_recmaterial.command.value="<%=Command.PREV%>";
	document.frm_recmaterial.prev_command.value="<%=Command.PREV%>";
	document.frm_recmaterial.action="payment_terms.jsp";
	document.frm_recmaterial.submit();
}

function cmdListNext()
{
	document.frm_recmaterial.command.value="<%=Command.NEXT%>";
	document.frm_recmaterial.prev_command.value="<%=Command.NEXT%>";
	document.frm_recmaterial.action="payment_terms.jsp";
	document.frm_recmaterial.submit();
}

function cmdListLast()
{
	document.frm_recmaterial.command.value="<%=Command.LAST%>";
	document.frm_recmaterial.prev_command.value="<%=Command.LAST%>";
	document.frm_recmaterial.action="payment_terms.jsp";
	document.frm_recmaterial.submit();
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
<link rel="stylesheet" href="../../../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" -->
<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
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
            <form name="frm_recmaterial" method ="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="vectSize" value="<%=vectSize%>">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="hidden_receive_id" value="<%=oidReceiveMaterial%>">
              <input type="hidden" name="hidden_payment_terms_id" value="<%=oidPaymentTerms%>">
              <input type="hidden" name="<%=FrmPaymentTerms.fieldNames[FrmPaymentTerms.FRM_RECEIVE_MATERIAL_ID]%>" value="<%=oidReceiveMaterial%>">
              <input type="hidden" name="<%=FrmPaymentTerms.fieldNames[FrmPaymentTerms.FRM_PAYMENT_TERMS_ID]%>" value="<%=oidPaymentTerms%>">
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr align="left" valign="top">
                  <td height="8"  colspan="3">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr align="left" valign="top">
                        <td height="14" valign="middle" colspan="4" align="center">
                          <h4><strong><%=textListHeader[SESS_LANGUAGE][7]%><%=textListHeader[SESS_LANGUAGE][8]%></strong></h4>
                          <hr size="1">                        </td>

                      </tr>
                      <tr>
                        <td width="8%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][0]%></td>
                        <td width="27%" align="left">
                          : <b><%=rec.getRecCode()%></b>
                        </td>
                        <td width="11%" valign="bottom"><%=textListOrderHeader[SESS_LANGUAGE][3]%></td>
                        <td width="27%" valign="bottom">  :
                          <%
                          ContactList contactList = new ContactList();
                          try	{
                                    contactList = PstContactList.fetchExc(rec.getSupplierId());
                          } catch(Exception e) {
                         }
                          %>
                          <%=contactList.getContactCode()%>
                        </td>
                     </tr>
                     <tr>
                        <td width="8%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][2]%></td>
                        <td width="27%" align="left"> : <%=ControlDate.drawDateWithStyle(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_DATE], rec.getReceiveDate(), 1, -5, "formElemen", "disabled=\"true\"")%></td>
                        <td><%=textListOrderHeader[SESS_LANGUAGE][6]%></td>
                        <td>:
                          <input type="text"  class="formElemen" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_INVOICE_SUPPLIER]%>" value="<%=rec.getInvoiceSupplier()%>"  size="20" style="text-align:right" readOnly>
                        </td>
                        <td width="9%" align="right"></td>
                      </tr>
                      <tr>
                        <td width="8%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][1]%></td>
                        <td width="27%" align="left">:
                          <%
                            Vector obj_locationid = new Vector(1,1);
                            Vector val_locationid = new Vector(1,1);
                            Vector key_locationid = new Vector(1,1);
                            String whereClauseLoc = PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE;
                            whereClause += " OR " + PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE;
                            Vector vt_loc = PstLocation.list(0, 0, whereClauseLoc, "");
                            for(int d=0;d<vt_loc.size();d++){
                                Location loc = (Location)vt_loc.get(d);
                                val_locationid.add(""+loc.getOID()+"");
                                key_locationid.add(loc.getName());
                            }
                            String select_locationid = ""+rec.getLocationId(); //selected on combo box
			  %>
                          <%=ControlCombo.draw(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_LOCATION_ID], null, select_locationid, val_locationid, key_locationid, "disabled=\"true\"", "formElemen")%></td>
                        <td valign="bottom"><%=textListOrderHeader[SESS_LANGUAGE][8]%></td>
                      
                      <%
			try{
		      %>
                      <tr align="left" valign="top">
                        <td height="22" valign="middle" colspan="4"> <%=str%> </td>
                      </tr>
                      <%
						 }catch(Exception exc){
						  }%>
                      
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
									ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,backTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,backTitle,ctrLine.CMD_BACK,true)+" List");
									
									ctrLine.initDefault();
									ctrLine.setTableWidth("80%");
									
									ctrLine.setCommandStyle("command");
									ctrLine.setColCommStyle("command");

									// set command caption
									ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE,saveTitle,ctrLine.CMD_SAVE,true));
									ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,backTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,backTitle,ctrLine.CMD_BACK,true)+" List");
									

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
              <% if(iCommand==Command.SAVE && iErrCode == CtrlPaymentTerms.RSLT_OK ) {
                %>
               <script language="Javascript">
                      self.opener.document.forms.frm_recmaterial.submit();
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

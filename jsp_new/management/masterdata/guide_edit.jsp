<% 

/* 

 * Page Name  		:  guide_edit.jsp

 * Created on 		:  [date] [time] AM/PM 

 * 

 * @author  		:  [authorName] 

 * @version  		:  [version] 

 */



/*******************************************************************

 * Page Description 	: [project description ... ] 

 * Imput Parameters 	: [input parameter ...] 

 * Output 			: [output ...] 

 *******************************************************************/

%>

<%@ page language = "java" %>

<!-- package java -->

<%@ page import = "java.util.*" %>

<!-- package dimata -->

<%@ page import = "com.dimata.util.*" %>

<!-- package qdep -->

<%@ page import = "com.dimata.gui.jsp.*" %>

<%@ page import = "com.dimata.qdep.form.*" %>

<!--package hanoman -->

<%@ page import = "com.dimata.hanoman.entity.masterdata.*" %>

<%@ page import = "com.dimata.harisma.entity.masterdata.*" %>

<%@ page import = "com.dimata.hanoman.form.masterdata.*" %>

<%@ page import = "com.dimata.harisma.form.masterdata.*" %>

<%@ page import = "com.dimata.hanoman.entity.admin.*" %>

<%@ include file = "../../main/javainit.jsp" %>

<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_DATA_MANAGEMENT, AppObjInfo.G2_DATA_MANAG_MASTER_D, AppObjInfo.OBJ_D_MANAG_MASTER_CONTACT_GUIDE); %>

<%@ include file = "../../main/checkuser.jsp" %>

<%

/* Check privilege except VIEW, view is already checked on checkuser.jsp as basic access*/

boolean privAdd=true;//userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_ADD));

boolean privView=true;//userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_VIEW));

boolean privUpdate=true;//userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_UPDATE));

boolean privDelete=true;//userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_DELETE));

%>

<!-- Jsp Block -->

<%



	CtrlContact ctrlContact = new CtrlContact(request);

	long oidContact = FRMQueryString.requestLong(request, "hidden_contact_id");

        int start = FRMQueryString.requestInt(request, "start");

	int iErrCode = FRMMessage.ERR_NONE;

	String errMsg = "";

	String whereClause = "";

	String orderClause = "";

	int iCommand = FRMQueryString.requestCommand(request);



	//out.println("iCommand : "+iCommand);

	ControlLine ctrLine = new ControlLine();



	iErrCode = ctrlContact.action(iCommand , oidContact, PstContactClass.CONTACT_TYPE_GUIDE);	



	errMsg = ctrlContact.getMessage();

	FrmContact frmContact = ctrlContact.getForm();

	Contact contact = ctrlContact.getContact();

	oidContact = contact.getOID();

	ContactClass contactClass = PstContact.getContactClassType(oidContact);

	int contactClassType = contactClass.getClassType();

	out.println(frmContact.getErrors());	

%>

<!-- End of Jsp Block -->

<html><!-- #BeginTemplate "/Templates/main_s_3.dwt" -->

<head>

<!-- #BeginEditable "doctitle" --> 

<title>contact edit</title>

<script language="JavaScript">

<!--

<%if(!privView &&  !privAdd && !privUpdate && !privDelete){%>

	window.location="<%=approot%>/nopriv.jsp";

<%}%>
    
<%if(iCommand==Command.SAVE || iCommand==Command.ASK){%>

		window.location="#go";

	<%}%>



	function cmdCancel(){

		document.frm_contact.command.value="<%=Command.EDIT%>";

		document.frm_contact.action="guide_edit.jsp";

		document.frm_contact.submit();

	} 



	function cmdEdit(oid){ 

		document.frm_contact.command.value="<%=Command.EDIT%>";

		document.frm_contact.action="guide_edit.jsp";

		document.frm_contact.submit(); 

	} 



	function cmdSave(){

		document.frm_contact.command.value="<%=Command.SAVE%>"; 

		document.frm_contact.action="guide_edit.jsp";

		document.frm_contact.submit();

	}



	function cmdAsk(oid){

		document.frm_contact.command.value="<%=Command.ASK%>"; 

		document.frm_contact.hidden_contact_id.value=oid; 		

		document.frm_contact.action="guide_edit.jsp";

		document.frm_contact.submit();

	} 



	function cmdDelete(oid){

		document.frm_contact.command.value="<%=Command.DELETE%>";

		document.frm_contact.hidden_contact_id.value=oid; 		

		document.frm_contact.action="guide_edit.jsp"; 

		document.frm_contact.submit();

	}  



	function cmdBackToList(){

		document.frm_contact.command.value="<%=Command.FIRST%>";

		document.frm_contact.action="guide_list.jsp";

		document.frm_contact.submit();

	}

	

	function cmdContract(){

		document.frm_contact.command.value="<%=Command.LIST%>";

		document.frm_contact.hidden_contact_id.value="<%=oidContact%>"; 		 

		document.frm_contact.action="contract.jsp";

		document.frm_contact.submit();

	}







//-------------- script control line -------------------

//-->

</script>

<!-- #EndEditable -->

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

<!-- #BeginEditable "inc_hdscr" --> 

<%@ include file="../../main/hdscript.jsp"%>

<%if(menuUsed == MENU_ICON){%>
    <link href="../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
<%}%>

<!-- #EndEditable -->

<!-- #BeginEditable "clientscript" --> 

<script language="JavaScript">

<!--



//-->

</script>

<link rel="stylesheet" href="<%=approot%>/css/default.css" type="text/css">

</head>

<body bgcolor="#FFFFFF" text="#000000" onLoad="MM_preloadImages('<%=approot%>/image/reserv_f2.jpg','<%=approot%>/image/cashiering_f2.jpg','<%=approot%>/image/datamng_f2.jpg','<%=approot%>/image/ledger_f2.jpg','<%=approot%>/image/logout_f2.jpg')" leftmargin="3" topmargin="3">

<table width="100%" border="0" cellpadding="0" cellspacing="0" height="100%">

  <!--DWLayoutTable-->


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

    <td width="100%" align="center" valign="top">

	<table width="98%" border="0" cellpadding="1" cellspacing="0">

        <!--DWLayoutTable-->

        <tr> 

          <td align="center" valign="top" class="frmcontents"><table width="100%" border="0" cellpadding="0" cellspacing="0">

              <!--DWLayoutTable-->

              <tr> 

                <td  height="178" align="center" valign="middle" class="contents">

                    <table width="100%" border="0" cellspacing="2" cellpadding="2">

                      <tr>
                            <td>

                            <form name="frm_contact" method="post" action="">

                              <input type="hidden" name="start" value="<%=start%>">

                              <input type="hidden" name="command" value="">

                              <input type="hidden" name="hidden_contact_id" value="<%=oidContact%>">

                              <input type="hidden" name="contact_class_type" value="<%=contactClassType%>">

                              <input type="hidden" name="<%=FrmContact.fieldNames[FrmContact.FRM_FIELD_CONTACT_TYPE]%>" value="<%=PstContact.CONTACT_TYPE_GUIDE%>">

                              <table width="100%" border="0" cellspacing="0" cellpadding="1">

                                <tr>

                                  <td width="15%">&nbsp;</td>

                                  <td width="70%">

                                <table width="100%" cellspacing="1" cellpadding="1" >

                                  <tr>

                                    <td colspan="2" class="title">&nbsp;</td>

                                  </tr>
                                  <tr>

                                    <td colspan="2" class="title">GUIDE EDITOR</td>

                                  </tr>
                                  
                                  <tr>

                                    <td colspan="2" class="txtheading1">*) entry

                                      required</td>

                                  </tr>

                                  <tr>

                                    <td width="29%">&nbsp;</td>

                                    <td width="67%">&nbsp;</td>

                                  </tr>

                                  <tr align="left">

                                    <%

					  Date dt = null;

					  try{

						  dt = contact.getRegdate();

						  if(dt==null){

						  	dt=com.dimata.util.DateCalc.getDate();

						  }

					  }catch(Exception e){

					  	  dt = com.dimata.util.DateCalc.getDate();

					  }

						  

					  %>

                                    <td width="29%"  >Registration Date</td>

                                    <td  width="67%"  valign="top"> <%=ControlDate.drawDateWithStyle(FrmContact.fieldNames[FrmContact.FRM_FIELD_REGDATE], dt, 1, -5, "formElemen", "")%><%=frmContact.getErrorMsg(FrmContact.FRM_FIELD_REGDATE)%></td>

                                  </tr>

                                  <tr align="left"> 

                                    <td width="29%"  >&nbsp;</td>

                                    <td  width="67%"  valign="top">&nbsp;</td>

                                  </tr>

                                  <tr align="left"> 

                                    <td width="29%"  > Code</td>

                                    <td  width="67%"  valign="top"> 

                                      <input type="text" name="<%=FrmContact.fieldNames[FrmContact.FRM_FIELD_CONTACT_CODE]%>" value="<%=contact.getContactCode()%>" class="formElemen" size="8" maxlength="3">

                                      * <%=frmContact.getErrorMsg(FrmContact.FRM_FIELD_CONTACT_CODE)%></td>

                                  </tr>

                                  <tr align="left"> 

                                    <td width="29%"  >Guide Name</td>

                                    <td  width="67%"  valign="top"> 

                                      <input type="text" name="<%=FrmContact.fieldNames[FrmContact.FRM_FIELD_PERSON_NAME]%>" value="<%=contact.getPersonName()%>" class="formElemen" size="35">

                                      * <%=frmContact.getErrorMsg(FrmContact.FRM_FIELD_PERSON_NAME)%></td>

                                  </tr>

                                  <tr align="left"> 

                                    <td width="29%"  >Email</td>

                                    <td  width="67%"  valign="top"> 

                                      <input type="text" name="<%=FrmContact.fieldNames[FrmContact.FRM_FIELD_EMAIL]%>" value="<%=contact.getEmail()%>" class="formElemen" size="45">

                                      <%=frmContact.getErrorMsg(FrmContact.FRM_FIELD_EMAIL)%></td>

                                  </tr>

                                  <tr align="left"> 

                                    <td width="29%"  >&nbsp;</td>

                                    <td  width="67%"  valign="top">&nbsp;</td>

                                  </tr>

                                  <tr align="left"> 

                                    <td colspan="2"  class="tableheader" bgcolor="#000099" height="15" ><font color="#FFFFFF"><b>Bussiness 

                                      Address</b></font></td>

                                  </tr>

                                  <tr align="left"> 

                                    <td width="29%"  >&nbsp;</td>

                                    <td  width="67%"  valign="top">&nbsp;</td>

                                  </tr>

                                  <tr align="left"> 

                                    <td width="29%"  > Address</td>

                                    <td  width="67%"  valign="top"> 

                                      <input type="text" name="<%=FrmContact.fieldNames[FrmContact.FRM_FIELD_BUSS_ADDRESS]%>" value="<%=contact.getBussAddress()%>" class="formElemen" size="45">

                                      * <%=frmContact.getErrorMsg(FrmContact.FRM_FIELD_BUSS_ADDRESS)%></td>

                                  </tr>

                                  <tr align="left"> 

                                    <td width="29%"  >Town</td>

                                    <td  width="67%"  valign="top"> 

                                      <input type="text" name="<%=FrmContact.fieldNames[FrmContact.FRM_FIELD_TOWN]%>" value="<%=contact.getTown()%>" class="formElemen" size="25">

                                      <%=frmContact.getErrorMsg(FrmContact.FRM_FIELD_TOWN)%></td>

                                  </tr>

                                  <tr align="left"> 

                                    <td width="29%"  >Province<a name="go"></a></td>

                                    <td  width="67%"  valign="top"> 

                                      <input type="text" name="<%=FrmContact.fieldNames[FrmContact.FRM_FIELD_PROVINCE]%>" value="<%=contact.getProvince()%>" class="formElemen" size="25">

                                      <%=frmContact.getErrorMsg(FrmContact.FRM_FIELD_PROVINCE)%></td>

                                  </tr>

                                  <tr align="left"> 

                                    <td width="29%"  >Country</td>

                                    <td  width="67%"  valign="top"> 

                                      <%
                                            String order = PstCountry.fieldNames[PstCountry.FLD_COUNTRY_NAME];

                                            Vector countries = PstCountry.list(0,0, "", order);

                                            Vector val_addresscountry = new Vector(1,1); //hidden values that will be deliver on request (oids)

                                            Vector key_addresscountry = new Vector(1,1); //texts that displayed on combo box



                                            val_addresscountry.add("-");

                                            key_addresscountry.add("select ...");



                                            if(countries!=null && countries.size()>0){

                                                    for(int i=0; i<countries.size(); i++){

                                                            Country country = (Country)countries.get(i);

                                                            val_addresscountry.add(country.getCountryName());

                                                            key_addresscountry.add(country.getCountryName());

                                                    }

                                            }
                                            String select_addresscountry = ""+contact.getCountry(); //selected on combo box

                                            %>

                                      <%=ControlCombo.draw(FrmContact.fieldNames[FrmContact.FRM_FIELD_COUNTRY], null, select_addresscountry, val_addresscountry, key_addresscountry, "", "formElemen")%> <%=frmContact.getErrorMsg(FrmContact.FRM_FIELD_COUNTRY)%></td>

                                  </tr>

                                  <tr align="left"> 

                                    <td width="29%"  >Telephone </td>

                                    <td  width="67%"  valign="top"> 

                                      <input type="text" name="<%=FrmContact.fieldNames[FrmContact.FRM_FIELD_TELP_NR]%>" value="<%=contact.getTelpNr()%>" class="formElemen" size="20">

                                      * <%=frmContact.getErrorMsg(FrmContact.FRM_FIELD_TELP_NR)%></td>

                                  </tr>

                                  <tr align="left"> 

                                    <td width="29%"  >Telephone Mobile</td>

                                    <td  width="67%"  valign="top"> 

                                      <input type="text" name="<%=FrmContact.fieldNames[FrmContact.FRM_FIELD_TELP_MOBILE]%>" value="<%=contact.getTelpMobile()%>" class="formElemen" size="20">

                                      <%=frmContact.getErrorMsg(FrmContact.FRM_FIELD_TELP_MOBILE)%></td>

                                  </tr>

                                  <tr align="left"> 

                                    <td width="29%"  >Fax</td>

                                    <td  width="67%"  valign="top"> 

                                      <input type="text" name="<%=FrmContact.fieldNames[FrmContact.FRM_FIELD_FAX]%>" value="<%=contact.getFax()%>" class="formElemen" size="20">

                                      <%=frmContact.getErrorMsg(FrmContact.FRM_FIELD_FAX)%></td>

                                  </tr>

                                  <tr align="left"> 

                                    <td width="29%"  >&nbsp;</td>

                                    <td  width="67%"  valign="top">&nbsp;</td>

                                  </tr>

                                  <tr align="left"> 

                                    <td colspan="2"  class="tableheader" bgcolor="#000099" height="15" ><font color="#FFFFFF"><b>Home 

                                      Address</b></font></td>

                                  </tr>

                                  <tr align="left"> 

                                    <td width="29%"  >&nbsp;</td>

                                    <td  width="67%"  valign="top">&nbsp;</td>

                                  </tr>

                                  <tr align="left"> 

                                    <td width="29%"  > Address</td>

                                    <td  width="67%"  valign="top"> 

                                      <input type="text" name="<%=FrmContact.fieldNames[FrmContact.FRM_FIELD_HOME_ADDR]%>" value="<%=contact.getHomeAddr()%>" class="formElemen" size="45">

                                      <%=frmContact.getErrorMsg(FrmContact.FRM_FIELD_HOME_ADDR)%></td>

                                  </tr>

                                  <tr align="left"> 

                                    <td width="29%"  > Town</td>

                                    <td  width="67%"  valign="top"> 

                                      <input type="text" name="<%=FrmContact.fieldNames[FrmContact.FRM_FIELD_HOME_TOWN]%>" value="<%=contact.getHomeTown()%>" class="formElemen" size="25">

                                      <%=frmContact.getErrorMsg(FrmContact.FRM_FIELD_HOME_TOWN)%></td>

                                  </tr>

                                  <tr align="left"> 

                                    <td width="29%"  > Province</td>

                                    <td  width="67%"  valign="top"> 

                                      <input type="text" name="<%=FrmContact.fieldNames[FrmContact.FRM_FIELD_HOME_PROVINCE]%>" value="<%=contact.getHomeProvince()%>" class="formElemen" size="25">

                                      <%=frmContact.getErrorMsg(FrmContact.FRM_FIELD_HOME_PROVINCE)%></td>

                                  </tr>

                                  <tr align="left"> 

                                    <td width="29%"  > Country</td>

                                    <td  width="67%"  valign="top"> <%=ControlCombo.draw(FrmContact.fieldNames[FrmContact.FRM_FIELD_HOME_COUNTRY], null, contact.getHomeCountry(), val_addresscountry, key_addresscountry, "", "formElemen")%> <%=frmContact.getErrorMsg(FrmContact.FRM_FIELD_COUNTRY)%> <%=frmContact.getErrorMsg(FrmContact.FRM_FIELD_HOME_COUNTRY)%></td>

                                  </tr>

                                  <tr align="left"> 

                                    <td width="29%"  > Telephone</td>

                                    <td  width="67%"  valign="top"> 

                                      <input type="text" name="<%=FrmContact.fieldNames[FrmContact.FRM_FIELD_HOME_TELP]%>" value="<%=contact.getHomeTelp()%>" class="formElemen" size="20">

                                      <%=frmContact.getErrorMsg(FrmContact.FRM_FIELD_HOME_TELP)%></td>

                                  </tr>

                                  <tr align="left"> 

                                    <td width="29%"  > Fax</td>

                                    <td  width="67%"  valign="top"> 

                                      <input type="text" name="<%=FrmContact.fieldNames[FrmContact.FRM_FIELD_HOME_FAX]%>" value="<%=contact.getHomeFax()%>" class="formElemen" size="20">

                                      <%=frmContact.getErrorMsg(FrmContact.FRM_FIELD_HOME_FAX)%></td>

                                  </tr>

                                  <tr align="left"> 

                                    <td width="29%"  valign="top"  >&nbsp;</td>

                                    <td  width="67%"  valign="top">&nbsp;</td>

                                  </tr>

                                  <tr align="left"> 

                                    <td width="29%"  valign="top"  >&nbsp;</td>

                                    <td  width="67%"  valign="top">&nbsp;</td>

                                  </tr>

                                  <tr align="left"> 

                                    <td width="29%"  >Bank Account</td>

                                    <td  width="67%"  valign="top"> 

                                      <input type="text" name="<%=FrmContact.fieldNames[FrmContact.FRM_FIELD_BANK_ACC]%>" value="<%=contact.getBankAcc()%>" class="formElemen" size="20">

                                      <%=frmContact.getErrorMsg(FrmContact.FRM_FIELD_BANK_ACC)%></td>

                                  </tr>

                                  <tr align="left"> 

                                    <td width="29%"  >Bank Account 2</td>

                                    <td  width="67%"  valign="top"> 

                                      <input type="text" name="<%=FrmContact.fieldNames[FrmContact.FRM_FIELD_BANK_ACC2]%>" value="<%=contact.getBankAcc2()%>" class="formElemen" size="20">

                                      <%=frmContact.getErrorMsg(FrmContact.FRM_FIELD_BANK_ACC2)%></td>

                                  </tr>

                                  <tr align="left"> 

                                    <td width="29%"  >&nbsp;</td>

                                    <td  width="67%"  valign="top">&nbsp; </td>

                                  </tr>

                                  <tr align="left"> 

                                    <td width="29%"  valign="top"  >Notes</td>

                                    <td  width="67%"  valign="top"> 

                                      <textarea name="<%=FrmContact.fieldNames[FrmContact.FRM_FIELD_NOTES]%>" cols="45" class="formElemen"><%=contact.getNotes()%></textarea>

                                    </td>

                                  </tr>

                                  <tr align="left"> 

                                    <td width="29%"  valign="top"  >&nbsp;</td>

                                    <td  width="67%"  valign="top">&nbsp;</td>

                                  </tr>

                                  <%if(iCommand==Command.ASK){%>

                                  <tr align="left"> 

                                    <td colspan="2"  valign="top" class="msgquestion" >Are 

                                      you sure to delete ....</td>

                                  </tr>

                                  <%}%>

                                  <%
					  if(iCommand==Command.SAVE && iErrCode!=FRMMessage.NONE){%>

                                  <tr align="left"> 

                                    <td colspan="2"  valign="top" class="msgquestion" ><%=errMsg%></td>

                                  </tr>

                                  <%}

					  if(iCommand==Command.SAVE && iErrCode==FRMMessage.NONE)

					  {%>

                                  <tr align="left"> 

                                    <td colspan="2"  valign="top" class="msginfo" >Data 

                                      have been saved ...</td>

                                  </tr>

                                  <%}%>

                                  <tr align="left"> 

                                    <td colspan="2"  valign="top"  > 

                                      <hr>

                                    </td>

                                  </tr>

                                  <tr align="left"> 

                                    <td width="29%"  valign="top"  >&nbsp;</td>

                                    <td  width="67%"  valign="top">&nbsp;</td>

                                  </tr>

                                  <%if(iCommand!=Command.ASK){%>

                                  <tr> 

                                    <td colspan="2"> 

                                      <div align="center"> 

                                        <%		  

                                          if(iCommand==Command.SAVE && iErrCode==FRMMessage.NONE){%>

                                                <a href="guide_edit.jsp?command=<%=Command.ADD%>">Add New</a> |

                                          <%}%>

                                          <%if(privUpdate){%>

                                        <a href="javascript:cmdSave()">Save Data</a> 

                                        | 

                                        <%if((oidContact!=0)&&(privDelete)){%>

                                        <a href="javascript:cmdAsk('<%=oidContact%>')">Delete 

                                        Data</a> | 

                                        <%}%>

                                        <%if((contactClassType==PstContactClass.CONTACT_TYPE_TRAVEL_AGENT || contactClassType==PstContactClass.CONTACT_TYPE_COMPANY) && (oidContact!=0)){%>

                                        <a href="javascript:cmdContract()">Room 

                                        Contract</a> | 

                                        <%}%><%}%>

                                        <a href="javascript:cmdBackToList()">Back 

                                        To List</a></div>

                                    </td>

                                  </tr>

                                  <%}%>

                                  <%if((iCommand==Command.ASK)&&(privDelete)){%>

                                  {%> 

                                  <tr> 

                                    <td colspan="2"> 

                                      <div align="center"><a href="javascript:cmdCancel()">Cancel 

                                        Delete</a> | <a href="javascript:cmdDelete('<%=oidContact%>')">Yes 

                                        Delete</a></div>

                                    </td>

                                  </tr>

                                  <%}%>

                                  <tr> 

                                    <td colspan="2">&nbsp;</td>

                                  </tr>

                                  <tr> 

                                    <td colspan="2">&nbsp;</td>

                                  </tr>

                                </table>

                  </td>

                  <td width="15%">&nbsp;</td>

                </tr>

                </table>
            </form>

            <!-- #EndEditable -->
                    </td>

                  </tr>

                </table>
                                  
                </td>

              </tr>

            </table></td>

        </tr>

      </table></td>

    <td width="1">&nbsp;</td>

  </tr>

  <tr>

    <td height="15"  align="center" valign="top" collspan="2">

	<!-- #BeginEditable "inc_ft" -->
        <%if(menuUsed == MENU_ICON){%>
            <%@include file="../../styletemplate/footer.jsp" %>

        <%}else{%>
            <%@ include file = "../../main/footer.jsp" %>
        <%}%>

      <!-- #EndEditable --></td>

  </tr>

</table>

</body>

<!-- #EndTemplate --></html>


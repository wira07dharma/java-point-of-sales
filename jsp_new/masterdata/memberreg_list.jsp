
<%@page import="com.dimata.qdep.form.FRMHandler"%>
<%@page import="com.dimata.common.entity.payment.CurrencyType"%>
<%@page import="com.dimata.common.entity.payment.PstCurrencyType"%>
<%@ page import="com.dimata.posbo.entity.masterdata.MemberReg,
                 com.dimata.posbo.entity.masterdata.PstMemberReg,
                 com.dimata.posbo.entity.search.SrcMemberReg,
                 com.dimata.posbo.session.masterdata.SessMemberReg,
                 com.dimata.posbo.form.masterdata.CtrlMemberReg,
                 com.dimata.posbo.form.masterdata.FrmMemberReg,
                 com.dimata.gui.jsp.ControlList,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.qdep.form.FRMMessage,
                 com.dimata.util.Command,
                 com.dimata.gui.jsp.ControlLine"%>
<%
/*
 * Page Name  		:  memberreg_list.jsp
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
<%@ include file = "../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_MEMBER); %>
<%@ include file = "../main/checkuser.jsp" %>

<!-- Jsp Block -->
<%!

	public static final String textListTitle[][] =
    {
        {"Daftar Member","Harus diisi", "Cetak "},
        {"Registration Member","required", "Print "}
    };

	public static final String textListHeader[][] =
    {
        {"Tipe Member","Status","Kode","Nama","Alamat","No Telp","No HP","Tempat/Tgl Lahir","Jenis Kelamin", //8
         "Agama","No ID","Nama Studio","Detil Pot.","Credit Limit","Consigment Limit","Mata Uang","Nama Perusahaan"},  //15

        {"Member Type","Status","Code","Name","Address","Telp No", "HP No","Place/Birthdate","Sex",
         "Religion","ID Number","Studio Name","Detail Disc.","Credit Limit","Consigment Limit","Currency","Company Name"}
    };

	public Vector drawList(Vector objectClass ,  long contactId, int languange, int start) {
		Vector result = new Vector(1,1);
		String listAll = "";
		Vector tableHeader = new Vector(1,1);

		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		ctrlist.addHeader("No","3%");
		ctrlist.addHeader(textListHeader[languange][2],"8%");
		ctrlist.addHeader(textListHeader[languange][3],"15%");
                
                ctrlist.addHeader(textListHeader[languange][16],"15%");
                
		ctrlist.addHeader(textListHeader[languange][4],"20%");
		ctrlist.addHeader(textListHeader[languange][5],"5%");
		ctrlist.addHeader(textListHeader[languange][6],"10%");
		ctrlist.addHeader("Member ID","10%");
                ctrlist.addHeader(textListHeader[languange][13],"10%");
                ctrlist.addHeader(textListHeader[languange][14],"10%");
                ctrlist.addHeader(textListHeader[languange][12],"9%");

		// untuk table header di PDF
		tableHeader.add("No");
		tableHeader.add(textListHeader[languange][3]);
		tableHeader.add(textListHeader[languange][5]);
		tableHeader.add(textListHeader[languange][6]);		
		tableHeader.add(textListHeader[languange][4]);

                ctrlist.setLinkRow(1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();
		int index = -1;

		for (int i = 0; i < objectClass.size(); i++) {
			Vector temp = (Vector)objectClass.get(i);
			MemberReg memberReg = (MemberReg)temp.get(0);

			 Vector rowx = new Vector();
			 if(contactId == memberReg.getOID())
				 index = i;

			rowx.add(""+(i+1+start));
			String code = memberReg.getMemberBarcode();
			if(code.length()==0)
			{
				code = memberReg.getContactCode();
			}
			rowx.add(code);

			String name = "";
			if(memberReg.getPersonName()!=null&&memberReg.getPersonName().length()>0)
			{
				name = memberReg.getPersonName();
			}
			else
			{
				name = memberReg.getCompName();
			}
			rowx.add(name);
                        rowx.add(memberReg.getCompName());
			rowx.add(memberReg.getHomeAddr());
			rowx.add(memberReg.getHomeTelp());
			rowx.add(memberReg.getTelpMobile());
                        rowx.add(memberReg.getMemberIdCardNumber());

                        Vector list = PstCurrencyType.list(0,0,PstCurrencyType.fieldNames[PstCurrencyType.FLD_TAB_INDEX]+"=1","");
                        CurrencyType currSell = new CurrencyType();
                        if(list!=null && list.size()>0){
                        currSell = (CurrencyType)list.get(0);
                        }
                       // rowx.add("<div align=\"center\">"+currSell.getCode()+"</div>");
                        rowx.add("<div align=\"right\">"+currSell.getCode()+ " " +FRMHandler.userFormatStringDecimal(memberReg.getMemberCreditLimit())+"</div>"); // tanda "" + didepan value, menyatakan dia string
                       // rowx.add("<div align=\"center\">"+currSell.getCode()+"</div>");
                        rowx.add("<div align=\"right\">"+currSell.getCode()+ " " +FRMHandler.userFormatStringDecimal(memberReg.getMemberConsigmentLimit())+"</div>"); // tanda "" + didepan value, menyatakan dia string
			rowx.add("<div align=\"center\"><a href=\"javascript:showdetail('"+memberReg.getOID()+"')\">[Detail]</a></div>");

			lstData.add(rowx);
			lstLinkData.add(String.valueOf(memberReg.getOID()));
		}

		//return ctrlist.drawList(index);
		//return ctrlist.draw(index);
		listAll = ctrlist.draw(index);
		result.add(listAll);
		result.add(tableHeader);
		return result;
	}

%>
<%
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int prevCommand = FRMQueryString.requestInt(request, "prev_command");
long oidMemberReg = FRMQueryString.requestLong(request, "hidden_contact_id");

/*variable declaration*/
boolean privManageData = true;
int recordToGet = 100;
String msgString = "";
int iErrCode = FRMMessage.NONE;
String whereClause = "";
String orderClause = ""+PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_BARCODE];

/* begin result */
SrcMemberReg srcMemberReg = new SrcMemberReg();

if(iCommand==Command.LIST||iCommand == Command.FIRST || iCommand == Command.PREV ||
  iCommand == Command.NEXT || iCommand == Command.LAST || iCommand == Command.BACK){
	try{
		srcMemberReg = (SrcMemberReg)session.getValue(SessMemberReg.SESS_SRC_MEMBERREG);
	}catch(Exception e){
		srcMemberReg = new SrcMemberReg();
	}

	if(session.getValue(SessMemberReg.SESS_SRC_MEMBERREG)==null){
		srcMemberReg = new SrcMemberReg();
	}

	session.putValue(SessMemberReg.SESS_SRC_MEMBERREG,srcMemberReg);
}

CtrlMemberReg ctrlMemberReg = new CtrlMemberReg(request);
ControlLine ctrLine = new ControlLine();
Vector listMemberReg = new Vector(1,1);

/*switch statement */
iErrCode = ctrlMemberReg.action(iCommand , oidMemberReg);
/* end switch*/
FrmMemberReg frmMemberReg = ctrlMemberReg.getForm();

/*count list All MemberReg*/
//int vectSize = PstMemberReg.getCount(whereClause);
int vectSize = SessMemberReg.getCountSearch(srcMemberReg);

MemberReg memberReg = ctrlMemberReg.getMemberReg();
msgString =  ctrlMemberReg.getMessage();

/*switch list MemberReg*/
if((iCommand == Command.SAVE) && (iErrCode == FRMMessage.NONE))
	start = PstMemberReg.findLimitStart(memberReg.getOID(),recordToGet, whereClause);

if((iCommand == Command.FIRST || iCommand == Command.PREV )||
  (iCommand == Command.NEXT || iCommand == Command.LAST)){
		start = ctrlMemberReg.actionList(iCommand, start, vectSize, recordToGet);
 }
/* end switch list*/

/* get record to display */
//listMemberReg = PstMemberReg.list(start,recordToGet, whereClause , orderClause);
listMemberReg = SessMemberReg.searchMemberReg(srcMemberReg,start,recordToGet);

/*handle condition if size of record to display = 0 and start > 0 after delete*/
if (listMemberReg.size() < 1 && start > 0)
{
	 if (vectSize - recordToGet > recordToGet)
			start = start - recordToGet;   //go to Command.PREV
	 else{
		 start = 0 ;
		 iCommand = Command.FIRST;
		 prevCommand = Command.FIRST; //go to Command.FIRST
	 }
	 //listMemberReg = PstMemberReg.list(start,recordToGet, whereClause , orderClause);
	 listMemberReg = SessMemberReg.searchMemberReg(srcMemberReg,start,recordToGet);
}

Vector list = drawList(listMemberReg,oidMemberReg,SESS_LANGUAGE,start);
Vector listHeaderPdf = new Vector(1,1);
Vector tableHeaderPdf = new Vector(1,1);
Vector contentPdf = new Vector(1,1);
String str = "";

try {
	str = (String)list.get(0);
	tableHeaderPdf = (Vector)list.get(1);
}
catch(Exception e) {
	System.out.println("\n"+e.toString());
}

// untuk proses ke PDF
listHeaderPdf.add(0, textListTitle[SESS_LANGUAGE][0]);
listHeaderPdf.add(1, companyAddress.get(0)); // name
listHeaderPdf.add(2, companyAddress.get(1)); // address
listHeaderPdf.add(3, companyAddress.get(2)); // telp/fax

contentPdf.add(listHeaderPdf);
contentPdf.add(srcMemberReg);
contentPdf.add(tableHeaderPdf);

session.removeAttribute("SESS_CONTACT_LIST_PDF");
session.putValue("SESS_CONTACT_LIST_PDF", contentPdf);

%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">

function showdetail(oidreg){
    window.open("<%=approot%>/servlet/com.dimata.posbo.report.masterdata.PersonalDiscountPrintPDF?hidden_member_id="+oidreg+"&approot=<%=approot%>&sess_language=<%=SESS_LANGUAGE%>");
}

function cmdAdd(){
	document.frmmemberreg.hidden_contact_id.value="0";
	document.frmmemberreg.command.value="<%=Command.ADD%>";
	document.frmmemberreg.prev_command.value="<%=prevCommand%>";
	document.frmmemberreg.action="memberreg_edit.jsp";
	document.frmmemberreg.submit();
}

function cmdAsk(oidMemberReg){
	document.frmmemberreg.hidden_contact_id.value=oidMemberReg;
	document.frmmemberreg.command.value="<%=Command.ASK%>";
	document.frmmemberreg.prev_command.value="<%=prevCommand%>";
	document.frmmemberreg.action="memberreg_list.jsp";
	document.frmmemberreg.submit();
}

function cmdConfirmDelete(oidMemberReg){
	document.frmmemberreg.hidden_contact_id.value=oidMemberReg;
	document.frmmemberreg.command.value="<%=Command.DELETE%>";
	document.frmmemberreg.prev_command.value="<%=prevCommand%>";
	document.frmmemberreg.action="memberreg_list.jsp";
	document.frmmemberreg.submit();
}
function cmdSave(){
	document.frmmemberreg.command.value="<%=Command.SAVE%>";
	document.frmmemberreg.prev_command.value="<%=prevCommand%>";
	document.frmmemberreg.action="memberreg_list.jsp";
	document.frmmemberreg.submit();
	}

function cmdEdit(oidMemberReg){
	document.frmmemberreg.hidden_contact_id.value=oidMemberReg;
	document.frmmemberreg.command.value="<%=Command.EDIT%>";
	document.frmmemberreg.prev_command.value="<%=prevCommand%>";
	document.frmmemberreg.action="memberreg_edit.jsp";
	document.frmmemberreg.submit();
	}

function cmdCancel(oidMemberReg){
	document.frmmemberreg.hidden_contact_id.value=oidMemberReg;
	document.frmmemberreg.command.value="<%=Command.EDIT%>";
	document.frmmemberreg.prev_command.value="<%=prevCommand%>";
	document.frmmemberreg.action="memberreg_list.jsp";
	document.frmmemberreg.submit();
}

function cmdBack(){
	document.frmmemberreg.command.value="<%=Command.BACK%>";
	document.frmmemberreg.action="srcmemberreg.jsp";
	document.frmmemberreg.submit();
	}

function cmdListFirst(){
	document.frmmemberreg.command.value="<%=Command.FIRST%>";
	document.frmmemberreg.prev_command.value="<%=Command.FIRST%>";
	document.frmmemberreg.action="memberreg_list.jsp";
	document.frmmemberreg.submit();
}

function cmdListPrev(){
	document.frmmemberreg.command.value="<%=Command.PREV%>";
	document.frmmemberreg.prev_command.value="<%=Command.PREV%>";
	document.frmmemberreg.action="memberreg_list.jsp";
	document.frmmemberreg.submit();
	}

function cmdListNext(){
	document.frmmemberreg.command.value="<%=Command.NEXT%>";
	document.frmmemberreg.prev_command.value="<%=Command.NEXT%>";
	document.frmmemberreg.action="memberreg_list.jsp";
	document.frmmemberreg.submit();
}

function cmdListLast(){
	document.frmmemberreg.command.value="<%=Command.LAST%>";
	document.frmmemberreg.prev_command.value="<%=Command.LAST%>";
	document.frmmemberreg.action="memberreg_list.jsp";
	document.frmmemberreg.submit();
}

function printForm() {
    window.open("<%=approot%>/servlet/com.dimata.posbo.report.masterdata.MemberListPdf", "", "scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
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

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
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
            Membership &gt; <%=textListTitle[SESS_LANGUAGE][0]%> <!-- #EndEditable --></td>
        </tr>
        <tr>
          <td><!-- #BeginEditable "content" -->
            <form name="frmmemberreg" method ="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="vectSize" value="<%=vectSize%>">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="hidden_contact_id" value="<%=oidMemberReg%>">
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr align="left" valign="top">
                  <td height="8"  colspan="3">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr align="left" valign="top">
                        <td height="8" valign="middle" colspan="3">
                          <hr>
                        </td>
                      </tr>
                      <tr align="left" valign="top">
                        <td height="14" valign="middle" colspan="3" class="comment"><%=textListTitle[SESS_LANGUAGE][0]%></td>
                      </tr>
                      <%
                          try {
                              if (listMemberReg.size() > 0) {
                      %>
                      <tr align="left" valign="top">
                        <td height="22" valign="middle" colspan="3"><%=str%><%//= drawList(listMemberReg,oidMemberReg,SESS_LANGUAGE,start)%> </td>
                      </tr>
                      <%    
                              }
                          } catch (Exception exc) {
                          }
                      %>
                      <tr align="left" valign="top">
                        <td height="8" align="left" colspan="3" class="command">
                          <span class="command">

                             </span>
                         </td>
                    </tr>
                      <tr align="left" valign="top">
                        <td height="8" align="left" colspan="3" class="command">
                          <span class="command">
                          <%
                          //update opie 23-06-2012
								   int cmd = 0;
									   if ((iCommand == Command.FIRST || iCommand == Command.PREV )||
										(iCommand == Command.NEXT || iCommand == Command.LAST)){
											cmd =iCommand;
								   }else{
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
                          <table width="" border="0" cellspacing="0" cellpadding="0">
                              <tr><td><br></td></tr>
                            <tr>
                              <%if(privAdd && privManageData){%>
                              <%if(iCommand==Command.NONE||iCommand==Command.FIRST||iCommand==Command.PREV||iCommand==Command.NEXT||iCommand==Command.LAST||iCommand==Command.LIST||iCommand==Command.BACK||iCommand==Command.SAVE||iCommand==Command.DELETE){%>
                              <!--td width="3%" nowrap><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_ADD,true)%>"></a></td-->
                              <td width="" nowrap class="command"><a class="btn btn-primary" href="javascript:cmdAdd()"><i class="fa fa-plus-circle"></i> <%=ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_ADD,true)%></a></td>
                              <%}}%>
                              <!--td width="7%" nowrap><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnBackOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_BACK_SEARCH,true)%>"></a></td-->
                              <td width="" nowrap class="command"><a class="btn btn-primary" href="javascript:cmdBack()"><i class="fa fa-arrow-left"></i> <%=ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_BACK_SEARCH,true)%></a></td>
							  <!--td width="4%" nowrap valign="top"><a href="javascript:printForm()"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0"></a></td-->
                              <td width="" nowrap>&nbsp; <a class="btn btn-primary" href="javascript:printForm()" class="command" ><i class="fa fa-print"></i> <%=textListTitle[SESS_LANGUAGE][2]+" "+textListTitle[SESS_LANGUAGE][0]%></a></td>
                            </tr>
                          </table>
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>
              </table>
            </form>
            <%//@ include file = "../main/menumain.jsp" %>
            <!-- #EndEditable --></td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td colspan="2" height="20"> <!-- #BeginEditable "footer" -->
       <%
        if(menuUsed == MENU_ICON){
       %>
            <%@include file="../styletemplate/footer.jsp" %>
        <%
        }else{
        %>
            <%@ include file = "../main/footer.jsp" %>
        <%}%>
      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>

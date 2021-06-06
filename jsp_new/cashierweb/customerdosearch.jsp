<%-- 
    Document   : customerdosearch
    Created on : 19 Jun 13, 14:00:47
    Author     : Wiweka
--%>
<%@page import="com.dimata.common.entity.payment.CurrencyType"%>
<%@page import="com.dimata.common.entity.payment.PstCurrencyType"%>
<%@ page import="com.dimata.posbo.entity.masterdata.MemberReg,
                 com.dimata.posbo.entity.masterdata.PstMemberReg,
                 com.dimata.posbo.entity.search.SrcMemberReg,
                 com.dimata.posbo.session.masterdata.SessMemberReg,
                 com.dimata.posbo.form.masterdata.CtrlMemberReg,
                 com.dimata.posbo.form.masterdata.FrmMemberReg,
                 com.dimata.gui.jsp.ControlList,
                 com.dimata.qdep.form.FRMHandler,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.qdep.form.FRMMessage,
                 com.dimata.util.Command,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.pos.form.billing.*,
         com.dimata.pos.entity.billing.*,
         com.dimata.posbo.entity.masterdata.*,
         com.dimata.posbo.form.masterdata.*"%>
<%@ page language = "java" %>
<!-- package java -->
<%@ include file = "../main/javainit.jsp" %>
<!--% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_MEMBER); %>
<!--%@ include file = "../main/checkuser.jsp" %>

<!-- Jsp Block -->
<%!

	public static final String textListTitle1[][] =
    {
        {"Daftar Member","Harus diisi", "Cetak "},
        {"Registration Member","required", "Print "}
    };

	public static final String textListHeader1[][] =
    {
        {"Tipe Member","Status","Kode","Nama","Alamat","No Telp","No HP","Tempat/Tgl Lahir","Jenis Kelamin", //8
         "Agama","No ID","Nama Studio","Detil Pot.","Credit Limit","Consigment Limit","Mata Uang"},  //15

        {"Member Type","Status","Code","Name","Address","Telp No", "HP No","Place/Birthdate","Sex",
         "Religion","ID Number","Studio Name","Detail Disc.","Credit Limit","Consigment Limit","Currency"}
    };
%>
<%!
	public Vector drawList(Vector objectClass ,  long contactId, int languange, int startMember) {
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
		ctrlist.addHeader(textListHeader1[languange][2],"8%");
		ctrlist.addHeader(textListHeader1[languange][3],"15%");
		ctrlist.addHeader(textListHeader1[languange][4],"20%");
		ctrlist.addHeader(textListHeader1[languange][5],"5%");
		ctrlist.addHeader(textListHeader1[languange][6],"10%");
                //ctrlist.addHeader(textListHeader1[languange][15],"5%");
                ctrlist.addHeader(textListHeader1[languange][13],"10%");
                ctrlist.addHeader(textListHeader1[languange][14],"10%");


                ctrlist.setLinkRow(2);
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

			rowx.add(""+(i+1+startMember));
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

			rowx.add(memberReg.getHomeAddr());
			rowx.add(memberReg.getHomeTelp());
			rowx.add(memberReg.getTelpMobile());


                        Vector list = PstCurrencyType.list(0,0,PstCurrencyType.fieldNames[PstCurrencyType.FLD_TAB_INDEX]+"=1","");
                        CurrencyType currSell = new CurrencyType();
                        if(list!=null && list.size()>0){
                        currSell = (CurrencyType)list.get(0);
                        }
                       // rowx.add("<div align=\"center\">"+currSell.getCode()+"</div>");
                        rowx.add("<div align=\"right\">"+memberReg.getMemberCreditLimit()+"</div>"); // tanda "" + didepan value, menyatakan dia string
                       // rowx.add("<div align=\"center\">"+currSell.getCode()+"</div>");
                        rowx.add("<div align=\"right\">" +memberReg.getMemberConsigmentLimit()+"</div>"); // tanda "" + didepan value, menyatakan dia string
			

			lstData.add(rowx);
			//lstLinkData.add(String.valueOf(memberReg.getOID()));
                        lstLinkData.add(memberReg.getOID() + "','" + memberReg.getCompName() + "','" + name + "','" + memberReg.getHomeAddr()
                            + "','" + FRMHandler.userFormatStringDecimal(memberReg.getMemberCreditLimit()));
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
int iCommandMember = FRMQueryString.requestCommand(request);
int startMember = FRMQueryString.requestInt(request, "startMember");
int prevCommandMember = FRMQueryString.requestInt(request, "prev_command");
long oidMemberReg = FRMQueryString.requestLong(request, "hidden_contact_id");
String compName = FRMQueryString.requestString(request, "compName");
String personName = FRMQueryString.requestString(request, "personName");
double creditLimit = FRMQueryString.requestDouble(request, "creditLimit");
/*variable declaration*/
//boolean privManageData = true;
int recordCustomerToGet = 20;
String msgStringMember = "";
int iErrCodeMember = FRMMessage.NONE;
String whereClauseCustomer = "";
//String orderClause = ""+PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_BARCODE];

/* begin result */
SrcMemberReg srcMemberReg = new SrcMemberReg();

if(iCommandMember==Command.LIST||iCommandMember == Command.FIRST || iCommandMember == Command.PREV ||
  iCommandMember == Command.NEXT || iCommandMember == Command.LAST || iCommandMember == Command.BACK){
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
ControlLine ctrLineCust = new ControlLine();
Vector listMemberReg = new Vector(1,1);

/*switch statement */
iErrCodeMember = ctrlMemberReg.action(iCommandMember , oidMemberReg);
/* end switch*/
FrmMemberReg frmMemberReg = ctrlMemberReg.getForm();

/*count list All MemberReg*/
//int vectSizeMember = PstMemberReg.getCount(whereClauseCustomer);
int vectSizeMember = SessMemberReg.getCountSearch(srcMemberReg);

MemberReg memberReg = ctrlMemberReg.getMemberReg();
msgStringMember =  ctrlMemberReg.getMessage();

/*switch list MemberReg*/
if((iCommandMember == Command.SAVE) && (iErrCodeMember == FRMMessage.NONE))
	startMember = PstMemberReg.findLimitStart(memberReg.getOID(),recordCustomerToGet, whereClauseCustomer);

if((iCommandMember == Command.FIRST || iCommandMember == Command.PREV )||
  (iCommandMember == Command.NEXT || iCommandMember == Command.LAST)){
		startMember = ctrlMemberReg.actionList(iCommandMember, startMember, vectSizeMember, recordCustomerToGet);
 }
/* end switch list*/

/* get record to display */
//listMemberReg = PstMemberReg.list(startMember,recordCustomerToGet, whereClauseCustomer , orderClause);
listMemberReg = SessMemberReg.searchMemberReg(srcMemberReg,startMember,recordCustomerToGet);

/*handle condition if size of record to display = 0 and startMember > 0 	after delete*/
if (listMemberReg.size() < 1 && startMember > 0)
{
	 if (vectSizeMember - recordCustomerToGet > recordCustomerToGet)
			startMember = startMember - recordCustomerToGet;   //go to Command.PREV
	 else{
		 startMember = 0 ;
		 iCommandMember = Command.FIRST;
		 prevCommandMember = Command.FIRST; //go to Command.FIRST
	 }
	 //listMemberReg = PstMemberReg.list(startMember,recordCustomerToGet, whereClauseCustomer , orderClause);
	 listMemberReg = SessMemberReg.searchMemberReg(srcMemberReg,startMember,recordCustomerToGet);
}

%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">


function cmdAdd(){
	document.frmmember.hidden_contact_id.value="0";
	document.frmmember.command.value="<%=Command.ADD%>";
	document.frmmember.prev_command.value="<%=prevCommandMember%>";
	document.frmmember.action="memberreg_edit.jsp";
	document.frmmember.submit();
}

function cmdAsk(oidMemberReg){
	document.frmmember.hidden_contact_id.value=oidMemberReg;
	document.frmmember.command.value="<%=Command.ASK%>";
	document.frmmember.prev_command.value="<%=prevCommandMember%>";
	document.frmmember.action="customerdosearch.jsp";
	document.frmmember.submit();
}

function cmdConfirmDelete(oidMemberReg){
	document.frmmember.hidden_contact_id.value=oidMemberReg;
	document.frmmember.command.value="<%=Command.DELETE%>";
	document.frmmember.prev_command.value="<%=prevCommandMember%>";
	document.frmmember.action="customerdosearch.jsp";
	document.frmmember.submit();
}
function cmdSave(){
	document.frmmember.command.value="<%=Command.SAVE%>";
	document.frmmember.prev_command.value="<%=prevCommandMember%>";
	document.frmmember.action="customerdosearch.jsp";
	document.frmmember.submit();
	}

function cmdEdit(oidMemberReg, compName, personName, homeAddr, creditLimit ){
	self.opener.document.forms.frmcashier.idMember.value = oidMemberReg;
        self.opener.document.forms.frmcashier.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_GUEST_NAME]%>.value = compName;
        self.opener.document.forms.frmcashier.personName.value = personName;
        self.opener.document.forms.frmcashier.homeAddr.value = homeAddr;
        self.opener.document.forms.frmcashier.creditLimit.value = creditLimit;
        self.close();
	}

function cmdCancel(oidMemberReg){
	document.frmmember.hidden_contact_id.value=oidMemberReg;
	document.frmmember.command.value="<%=Command.EDIT%>";
	document.frmmember.prev_command.value="<%=prevCommandMember%>";
	document.frmmember.action="customerdosearch.jsp";
	document.frmmember.submit();
}

function cmdBack(){
	document.frmmember.command.value="<%=Command.BACK%>";
	document.frmmember.action="srcmemberreg.jsp";
	document.frmmember.submit();
	}

function cmdListFirst(){
	document.frmmember.command.value="<%=Command.FIRST%>";
	document.frmmember.prev_command.value="<%=Command.FIRST%>";
	document.frmmember.action="customerdosearch.jsp";
	document.frmmember.submit();
}

function cmdListPrev(){
	document.frmmember.command.value="<%=Command.PREV%>";
	document.frmmember.prev_command.value="<%=Command.PREV%>";
	document.frmmember.action="customerdosearch.jsp";
	document.frmmember.submit();
}

function cmdListNext(){
	document.frmmember.command.value="<%=Command.NEXT%>";
	document.frmmember.prev_command.value="<%=Command.NEXT%>";
	document.frmmember.action="customerdosearch.jsp";
	document.frmmember.submit();
}

function cmdListLast(){
	document.frmmember.command.value="<%=Command.LAST%>";
	document.frmmember.prev_command.value="<%=Command.LAST%>";
	document.frmmember.action="customerdosearch.jsp";
	document.frmmember.submit();
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
  
  <tr>
    <td valign="top" align="left">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        
        <tr>
          <td><!-- #BeginEditable "content" -->
            <form name="frmmember" method ="post" action="">
              <input type="hidden" name="command" value="<%=iCommandMember%>">
              <input type="hidden" name="vectSizeMember" value="<%=vectSizeMember%>">
              <input type="hidden" name="startMember" value="<%=startMember%>">
              <input type="hidden" name="prev_command" value="<%=prevCommandMember%>">
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
                        <td height="14" valign="middle" colspan="3" class="comment"><%=textListTitle1[SESS_LANGUAGE][0]%></td>
                      </tr>
                      <%
							try{
								if (listMemberReg.size()>0){
							%>
                      <tr align="left" valign="top">
                        <td height="22" valign="middle" colspan="3">
                            <%=drawList(listMemberReg,oidMemberReg,SESS_LANGUAGE,startMember)%>
                        </td>
                      </tr>
                      <%  }
						  }catch(Exception exc){
						  }%>
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
									   if ((iCommandMember == Command.FIRST || iCommandMember == Command.PREV )||
										(iCommandMember == Command.NEXT || iCommandMember == Command.LAST)){
											cmd =iCommandMember;
								   }else{
									  if(iCommandMember == Command.NONE || prevCommandMember == Command.NONE)
										cmd = Command.FIRST;
									  else
									  	cmd =prevCommandMember;
								   }
							    %>
                          <% ctrLineCust.setLocationImg(approot+"/images");
							   	ctrLineCust.initDefault();
								 %>
                          <%=ctrLineCust.drawImageListLimit(cmd,vectSizeMember,startMember,recordCustomerToGet)%> </span> </td>
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
  
</table>
</body>
<!-- #EndTemplate --></html>

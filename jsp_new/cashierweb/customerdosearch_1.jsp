<%-- 
    Document   : customerdosearch
    Created on : 19 Jun 13, 14:00:47
    Author     : Wiweka
--%>
<%@page import="com.dimata.common.entity.payment.CurrencyType"%>
<%@page import="com.dimata.common.entity.payment.PstCurrencyType"%>
<%@ page import="java.util.*,com.dimata.posbo.entity.masterdata.MemberReg,
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
<%@ page language = "java" %>
<%@ include file = "../../../main/javainit.jsp" %>
<!-- package java -->

<%!    /* this constant used to list text of listHeader */
    public static final String textMaterialHeader[][] = {
        {"Customer", "Sku", "Nama Barang", "Semua Barang"},
        {"Category", "Code", "Material Name", "All Goods"}
    };

    /* this constant used to list text of listHeader */
    public static final String textListMaterialHeader[][] = {
        {"NO", "Customer", "Alamat", "UNIT"},
        {"NO", "CODE", "NAME PRODUCT", "UNIT"}
    };

    public String drawListCustomer(long contactOid, int language, Vector objectClass, int start) {
        String result = "";
        if (objectClass != null && objectClass.size() > 0) {
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("100%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("listgentitle");

            ctrlist.addHeader(textListMaterialHeader[language][0], "3%");
            ctrlist.addHeader(textListMaterialHeader[language][1], "10%");
            ctrlist.addHeader(textListMaterialHeader[language][2], "40%");
            ctrlist.addHeader(textListMaterialHeader[language][3], "10%");

            ctrlist.setLinkRow(1);
            ctrlist.setLinkSufix("");
            Vector lstData = ctrlist.getData();
            Vector lstLinkData = ctrlist.getLinkData();
            ctrlist.setLinkPrefix("javascript:cmdEdit('");
            ctrlist.setLinkSufix("')");
            ctrlist.reset();
            int index = -1;

            if (start < 0) {
                start = 0;
            }

            for (int i = 0; i < objectClass.size(); i++) {
                Vector vt = (Vector) objectClass.get(i);
                MemberReg memberReg = (MemberReg) vt.get(0);
                start = start + 1;

                Vector rowx = new Vector();

                rowx.add("" + start);
                String code = memberReg.getMemberBarcode();
                if (code.length() == 0) {
                    code = memberReg.getContactCode();
                }
                rowx.add(code);

                String name = "";
                if (memberReg.getPersonName() != null && memberReg.getPersonName().length() > 0) {
                    name = memberReg.getPersonName();
                } else {
                    name = memberReg.getCompName();
                }
                rowx.add(name);

                rowx.add(memberReg.getHomeAddr());
                rowx.add(memberReg.getHomeTelp());
                rowx.add(memberReg.getTelpMobile());
                
                lstData.add(rowx);

                   // lstLinkData.add(memberReg.getOID() + "','" + memberReg.getCompName() + "','" + name + "','" + memberReg.getHomeAddr()
                     //       + "','" + FRMHandler.userFormatStringDecimal(memberReg.getMemberCreditLimit()));
               
            }
            return ctrlist.draw();
        } else {
            result = "<div class=\"msginfo\">&nbsp;&nbsp;Tidak ada data ...</div>";
        }
        return result;
    }
%>

<!-- JSP Block -->
<%
            long contactId = FRMQueryString.requestLong(request, "contactId");
            String customerName = FRMQueryString.requestString(request, "customerName");
            String customerAddress = FRMQueryString.requestString(request, "customerAddress");
            String compName = FRMQueryString.requestString(request, "compName");
            double creditLimit = FRMQueryString.requestDouble(request, "creditLimit");
            int start = FRMQueryString.requestInt(request, "start");
            int iCommand = FRMQueryString.requestCommand(request);
            int recordToGet = 20;

int recordToGet = 15;
String msgString = "";
int iErrCode = FRMMessage.NONE;
String whereClause = PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+" = '0' AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+" = '0' AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+" = '1'";
String orderClause = PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE];

CtrlBillMain ctrlBillMain = new CtrlBillMain(request);
ControlLine ctrLine = new ControlLine();
Vector listMatCategory = new Vector(1,1);

iErrCode = ctrlBillMain.action(iCommand,oidBillMain);
FrmBillMain frmBillMain = ctrlBillMain.getForm();

// count list All MatDepartment
int vectSize = PstBillMain.getCount(whereClause);

BillMain billMain = ctrlBillMain.getBillMain();
msgString =  ctrlBillMain.getMessage();

if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST)
{
        start = ctrlBillMain.actionList(iCommand, start, vectSize, recordToGet);
}

// get record to display
orderClause = PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE];
listMatCategory = PstBillMain.list(start,recordToGet,whereClause,orderClause);

// handle condition if size of record to display=0 and start>0 after delete
if(listMatCategory.size()<1 && start>0)
{
  if (vectSize - recordToGet > recordToGet)
  {
         start = start - recordToGet;
  }
  else
  {
         start = 0 ;
         iCommand = Command.FIRST;
         prevCommand = Command.FIRST;
  }
  listMatCategory = PstBillMain.list(start,recordToGet, whereClause , orderClause);
  }
%>
<!-- End of JSP Block -->
<html>
    <head>
        <title>Dimata - ProChain POS</title>
        <script language="JavaScript">
    
            function cmdEdit(contactId,compName,personName,compAddr,creditLimit ){
                self.opener.document.forms.frmcashier.<%=FrmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_CONTACT_ID]%>.value = contactId;
                self.opener.document.forms.frmcashier.<%=FrmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_COMP_NAME]%>.value = compName;
                self.opener.document.forms.frmcashier.<%=FrmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_PERSON_NAME]%>.value = personName;
                self.opener.document.forms.frmcashier.<%=FrmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_HOME_ADDR]%>.value = compAddr;
                self.opener.document.forms.frmcashier.<%=FrmMemberReg.fieldNames[FrmMemberReg.FRM_FIELD_MEMBER_CREDIT_LIMIT]%>.value = creditLimit;
                self.close();
            }

            function cmdListFirst(){
                document.frmcustomersearch.command.value="<%=Command.FIRST%>";
                document.frmcustomersearch.action="customerdosearch.jsp";
                document.frmcustomersearch.submit();
            }

            function cmdListPrev(){
                document.frmcustomersearch.command.value="<%=Command.PREV%>";
                document.frmcustomersearch.action="customerdosearch.jsp";
                document.frmcustomersearch.submit();
            }

            function cmdListNext(){
                document.frmcustomersearch.command.value="<%=Command.NEXT%>";
                document.frmcustomersearch.action="customerdosearch.jsp";
                document.frmcustomersearch.submit();
            }

            function cmdListLast(){
                document.frmcustomersearch.command.value="<%=Command.LAST%>";
                document.frmcustomersearch.action="customerdosearch.jsp";
                document.frmcustomersearch.submit();
            }

            function cmdSearch(){
                document.frmcustomersearch.start.value="0";
                document.frmcustomersearch.command.value="<%=Command.LIST%>";
                document.frmcustomersearch.action="customerdosearch.jsp";
                document.frmcustomersearch.submit();
            }

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
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <link rel="stylesheet" href="../../../styles/main.css" type="text/css">
        <link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
    </head>
    <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
        <script language="JavaScript">
                window.focus();
        </script>
        <table width="100%" border="0" cellspacing="3" cellpadding="2" height="100%" bgcolor="#FCFDEC" >
            <tr>
                <td width="88%" valign="top" align="left">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                            <td height="20" class="mainheader" colspan="2"> </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <hr size="1">
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <form name="frmcustomersearch" method="post" action="">
                                    <input type="hidden" name="start" value="<%=start%>">
                                    <input type="hidden" name="command" value="<%=iCommand%>">
                                    <input type="hidden" name="contactId" value="<%=contactId%>">
                                    <input type="hidden" name="customerName" value="<%=customerName%>">
                                    <input type="hidden" name="custopmerAddress" value="<%=customerAddress%>">
                                    <input type="hidden" name="source_link2" value="customerdosearch_1.jsp">
                                    <table width="100%" border="0" cellspacing="1" cellpadding="1">

                                        <tr>
                                            <td width="13%"><%=textMaterialHeader[SESS_LANGUAGE][1]%></td>
                                            <td width="87%"> :
                                                <input type="text" name="mat_code" size="15" value="<%=customerName%>" class="formElemen">
                                            </td>
                                        </tr>
                                        <tr>
                                            <td width="13%">&nbsp;</td>
                                            <td width="87%">
                                                <input type="button" name="Button" value="Search" onClick="javascript:cmdSearch()" class="formElemen">
                                                <input type="button" name="Button2" value="Add New Item" onClick="javascript:addNewItem()" class="formElemen">
                                            </td>
                                        </tr>
                                        <tr>
                                            <td colspan="2"><%=drawListMaterial(contactId, SESS_LANGUAGE, vect, start)%></td>
                                        </tr>
                                        <tr>
                                            <td colspan="2"><span class="command">
                                                    <%
                                                                ControlLine ctrlLine = new ControlLine();
                                                    %>
                                                    <%
                                                                ctrlLine.setLocationImg(approot + "/images");
                                                                ctrlLine.initDefault();
                                                    %>
                                                    <%=ctrlLine.drawImageListLimit(iCommand, vect, start, recordToGet)%> </span></td>
                                        </tr>
                                    </table>
                                </form>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </body>
</html>

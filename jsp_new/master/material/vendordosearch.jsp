<%@page contentType="text/html"%>
<%@ page import="com.dimata.gui.jsp.ControlList,
         com.dimata.common.entity.contact.ContactList,
         com.dimata.common.entity.contact.PstContactList,
         com.dimata.qdep.form.FRMQueryString,
         com.dimata.common.entity.contact.PstContactClass,
         com.dimata.common.session.contact.SessContactList,
         com.dimata.common.form.contact.CtrlContactList,
         com.dimata.util.Command,
         com.dimata.posbo.form.masterdata.FrmMatVendorPrice,
         com.dimata.gui.jsp.ControlLine"%>
<%@ page import = "com.dimata.posbo.session.masterdata.SessMemberReg" %>
<%@ page import = "com.dimata.posbo.entity.search.SrcMemberReg" %>

<%@ include file = "../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_CATALOG);%>
<%@ include file = "../../main/checkuser.jsp" %>

<%!
    /* this constant used to list text of listHeader */
    public static final String textListContact[][] = {
        {"No", "Kode", "Nama", "Alamat", "Telepon"},
        {"No", "Code", "Name", "Address", "Phone"}
    };
    public static final String textListTitleHeader[][] = {
        {"Daftar Supplier"},
        {"Supplier List"}
    };
//adding parameter pencarian by Mirahu 09/05/2012
    public static final String textListOrderHeader[][] = {
        {"Kode", "Nama Perusahaan", "Nama Kontak", "Urut Berdasarkan", "Pencarian Supplier", "Tambah Supplier"},
        {"Code", "Company Name", "Contact Person", "Sort By", "Supplier Search", "Add Supplier"}
    };
    public String sortBy[][] = {
        {"Nama supplier", "Kode Supplier"},
        {"Supplier Name", "Supplier Code"}
    };

    public String drawList(int language, Vector objectClass, int start) {
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");

        ctrlist.addHeader(textListContact[language][0], "3%");
        ctrlist.addHeader(textListContact[language][1], "12%");
        ctrlist.addHeader(textListContact[language][2], "30%");
        ctrlist.addHeader(textListContact[language][3], "45%");
        ctrlist.addHeader(textListContact[language][4], "10%");

        ctrlist.setLinkRow(2);
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
            //Vector vt = (Vector)objectClass.get(i);		
            //ContactList contactList = (ContactList)vt.get(0);
            ContactList contactList = (ContactList) objectClass.get(i);

            String contactName = "";
            String strAddr = "";
            String strTelp = "";
            if (contactList.getContactType() == PstContactList.EXT_COMPANY || contactList.getContactType() == PstContactList.OWN_COMPANY) {
                contactName = contactList.getCompName();
                if (!contactList.getBussAddress().equals("null")) {
                    strAddr = contactList.getBussAddress();
                }

                if (!contactList.getTelpNr().equals("null")) {
                    strTelp = contactList.getTelpNr();
                }
            }

            if (contactList.getContactType() == PstContactList.EXT_PERSONEL) {
                contactName = contactList.getHomeAddr() + " " + contactList.getHomeAddr();
                if (!contactList.getHomeAddr().equals("null")) {
                    strAddr = contactList.getHomeAddr();
                }

                if (!contactList.getHomeTelp().equals("null")) {
                    strTelp = contactList.getHomeTelp();
                }
            }

            Vector rowx = new Vector();
            start = start + 1;
            rowx.add("" + start + "");
            rowx.add(contactList.getContactCode());
            rowx.add(contactName);
            rowx.add(strAddr);
            rowx.add(strTelp);

            lstData.add(rowx);
            lstLinkData.add(String.valueOf(contactList.getOID()) + "','" + contactName);
        }
        return ctrlist.draw();
    }
%>
<!-- JSP Block -->
<%
    String vendorName = FRMQueryString.requestString(request, "txt_vendorname");
    int frmId = FRMQueryString.requestInt(request, "frm_id");
    int start = FRMQueryString.requestInt(request, "start");
    int iCommand = FRMQueryString.requestCommand(request);
    int recordToGet = 20;
    String contactTitle = com.dimata.common.jsp.JspInfo.txtMaterialInfo[SESS_LANGUAGE][com.dimata.common.jsp.JspInfo.CONTACT_LIST];
    contactTitle = textListTitleHeader[SESS_LANGUAGE][0]; //SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar "+contactTitle : contactTitle+" List";
    //adding parameter searching vendor by mirahu 20120509 
    String supplierCode = FRMQueryString.requestString(request, "supp_code");
    String companyName = FRMQueryString.requestString(request, "company_name");
    String contactPerson = FRMQueryString.requestString(request, "contact_name");
    
    SrcMemberReg objSrcMemberReg = new SrcMemberReg();
    objSrcMemberReg.setCodeSupplier(supplierCode);
    //objSrcMemberReg.setCompanyName(companyName);
    objSrcMemberReg.setCompanyName(vendorName);
    objSrcMemberReg.setContactPerson(contactPerson);
            
    Vector vt = new Vector(1, 1);
    vt.add("" + PstContactClass.CONTACT_TYPE_SUPPLIER);

    vendorName = "";
    //int vectSize = SessContactList.getCountListVendor(vendorName, vt);
      //int vectSize = SessMemberReg.getCountSearch(objSrcMemberReg);
      int vectSize = SessMemberReg.countSearchSupplier(objSrcMemberReg);
    CtrlContactList ctrlContactList = new CtrlContactList(request);

    if ((iCommand == Command.FIRST || iCommand == Command.PREV) || (iCommand == Command.NEXT || iCommand == Command.LAST)) {
        start = ctrlContactList.actionList(iCommand, start, vectSize, recordToGet);
    }

//Vector vect = SessContactList.getListVendor(vendorName,vt,start,recordToGet); 
    //Vector vect = SessMemberReg.searchSupplier(new SrcMemberReg(), start, recordToGet);
   Vector vect = SessMemberReg.searchSupplier(objSrcMemberReg, start, recordToGet);
%>
<!-- End of JSP Block -->
<html><!-- #BeginTemplate "/Templates/maindosearch.dwt" -->
    <head>
        <!-- #BeginEditable "doctitle" --> 
        <title>Dimata - ProChain POS</title>
        <script language="JavaScript">
            function cmdEdit(oid,name){
                self.opener.document.forms.frmmaterial.<%=FrmMatVendorPrice.fieldNames[FrmMatVendorPrice.FRM_FIELD_VENDOR_ID]%>.value = oid;
                self.opener.document.forms.frmmaterial.txt_vendorname.value = name;
                self.close();
            }

            function cmdListFirst(){
                document.frmvendorsearch.command.value="<%=Command.FIRST%>";
                document.frmvendorsearch.action="vendordosearch.jsp";
                document.frmvendorsearch.submit();
            }

            function cmdListPrev(){
                document.frmvendorsearch.command.value="<%=Command.PREV%>";
                document.frmvendorsearch.action="vendordosearch.jsp";
                document.frmvendorsearch.submit();
            }

            function cmdListNext(){
                document.frmvendorsearch.command.value="<%=Command.NEXT%>";
                document.frmvendorsearch.action="vendordosearch.jsp";
                document.frmvendorsearch.submit();
            }

            function cmdListLast(){
                document.frmvendorsearch.command.value="<%=Command.LAST%>";
                document.frmvendorsearch.action="vendordosearch.jsp";
                document.frmvendorsearch.submit();
            }
            
            function cmdSearch(){
                document.frmvendorsearch.command.value="<%=Command.LIST%>";
                document.frmvendorsearch.start.value = '0';
                document.frmvendorsearch.action="vendordosearch.jsp";
                document.frmvendorsearch.submit();
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
        <!-- #EndEditable -->
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
        <!-- #BeginEditable "styles" --> 
        <link rel="stylesheet" href="../../styles/main.css" type="text/css">
        <!-- #EndEditable -->
        <!-- #BeginEditable "stylestab" --> 
        <link rel="stylesheet" href="../../styles/tab.css" type="text/css">
        <!-- #EndEditable -->
        <!-- #BeginEditable "headerscript" --> 
        <!-- #EndEditable --> 
    </head> 

    <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
        <!-- #BeginEditable "editfocus" -->
        <script language="JavaScript">
                window.focus();
        </script>
        <!-- #EndEditable -->    
        <%@include file="../../styletemplate/template_header_empty.jsp" %>
        <table width="100%" border="0" cellspacing="3" cellpadding="2" height="100%" bgcolor="#FCFDEC" >
            <tr> 
                <td width="88%" valign="top" align="left"> 
                    <table width="100%" border="0" cellspacing="0" cellpadding="0"> 
                        <tr> 
                            <td height="20"><font color="#FF8080" face="Century Gothic"><big><strong><!-- #BeginEditable "contenttitle" --><!-- #EndEditable --></strong></big></font></td>
            </tr>
            <td><!-- #BeginEditable "content" --> 
                <form name="frmvendorsearch" method="post" action="">
                    <input type="hidden" name="start" value="<%=start%>">
                    <input type="hidden" name="command" value="<%=iCommand%>">
                    <input type="hidden" name="frm_id" value="<%=frmId%>">
                    <input type="hidden" name="txt_vendorname" value="<%=vendorName%>">		  
                    <table width="100%" border="0" cellspacing="1" cellpadding="1">
                        <tr>
                            <td colspan="4" class="listtitle" width="14%">&nbsp;<%=contactTitle%></td>
                        </tr>
                        <!-- adding parameter search -->
                        <tr align="left" valign="top"> 
                            <td height="21" valign="top" width="14%"><%=textListOrderHeader[SESS_LANGUAGE][0]%></td>
                            <td height="21" width="1%">:</td>
                            <td height="21" width="83%"> 
                                <input type="text" name="supp_code"  value="<%=supplierCode%>" class="formElemen">
                            </td>
                        </tr>
                        <tr align="left" valign="top"> 
                            <td height="21" valign="top" width="14%"><%=textListOrderHeader[SESS_LANGUAGE][1]%></td>
                            <td height="21" width="1%">:</td>
                            <td height="21" width="83%"> 
                                <input type="text" name="company_name"  value="<%= companyName%>" class="formElemen" size="30">
                            </td>
                        </tr>
                        <tr align="left" valign="top"> 
                            <td height="21" valign="top" width="14%"><%=textListOrderHeader[SESS_LANGUAGE][2]%></td>
                            <td height="21" width="1%">:</td>
                            <td height="21" width="83%"> 
                                <input type="text" name="contact_name"  value="<%= contactPerson%>" class="formElemen" size="30">
                            </td>
                        </tr>
                        <tr align="left" valign="top"> 
                            <td width="21%">&nbsp;</td>
                            <td height="21" width="1%">&nbsp;</td>
                            <td height="21" width="83%"> <input type="button" name="Button" value="Search" onClick="javascript:cmdSearch()" class="formElemen"></td>
                        </tr>
                        <!-- end of parameter search vendor -->
                        <tr> 
                        <tr> 
                            <td colspan="4"><%=drawList(SESS_LANGUAGE, vect, start)%></td>
                        </tr>
                        <tr> 
                            <td colspan="4">
                                <span class="command"> 
                                    <%
                                        ControlLine ctrlLine = new ControlLine();
                                        ctrlLine.setLocationImg(approot + "/images");
                                        ctrlLine.initDefault();
                                        out.println(ctrlLine.drawImageListLimit(iCommand, vectSize, start, recordToGet));
                                    %> 
                                </span>
                            </td>
                            <td>&nbsp;</td>
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
        <!-- #EndEditable --> </td>
</tr>
</table>
</body>
<!-- #EndTemplate --></html>

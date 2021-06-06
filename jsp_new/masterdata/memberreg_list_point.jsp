<%@ page import="com.dimata.posbo.entity.masterdata.MemberReg,
         com.dimata.posbo.entity.masterdata.PstMemberReg,
         com.dimata.posbo.entity.search.SrcMemberReg,
         com.dimata.posbo.session.masterdata.SessMemberReg,
         com.dimata.posbo.form.masterdata.CtrlMemberReg,
         com.dimata.posbo.form.masterdata.FrmMemberReg,
         com.dimata.posbo.entity.masterdata.MemberPoin,
         com.dimata.posbo.entity.masterdata.PstMemberPoin,
         com.dimata.gui.jsp.ControlList,
         com.dimata.util.Command,
         com.dimata.qdep.form.FRMQueryString,
         com.dimata.gui.jsp.ControlLine"%>
<%
    /*
     * Page Name  		:  memberreg_list.jsp
     * Created on 		:  [date] [time] AM/PM
     *
     * @author  		:  [authorName]
     * @version  		:  [version]
     */

    /**
     * *****************************************************************
     * Page Description : [project description ... ] Imput Parameters : [input
     * parameter ...] Output : [output ...]
 ******************************************************************
     */
%>

<%@ page language = "java" %>
<%@ include file = "../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_MEMBER_POINT);%>
<%@ include file = "../main/checkuser.jsp" %>

<!-- Jsp Block -->
<%!    
    public static final String textListTitle[][] = {
        {"Daftar Member", "Harus diisi"},
        {"Registration Member", "required"}
    };

    public static final String textListHeader[][] = {
        {"Tipe Member", "Status", "Barcode", "Nama", "Alamat", "No Telp", "No HP", "Tempat/Tgl Lahir", "Jenis Kelamin", "Agama", "No ID", "Nama Studio"},
        {"Member Type", "Status", "Barcode", "Name", "Address", "Telp No", "HP No", "Place/Birthdate", "Sex", "Religion", "ID Number", "Studio Name"}
    };

    public String drawList(Vector objectClass, long contactId, int languange, int start, int iCommand, long oidReg) {
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.addHeader("No", "1%");
        ctrlist.addHeader(textListHeader[languange][2], "10%");
        ctrlist.addHeader(textListHeader[languange][3], "20%");
        ctrlist.addHeader(textListHeader[languange][4], "25%");
        ctrlist.addHeader(textListHeader[languange][5], "5%");
        ctrlist.addHeader(textListHeader[languange][6], "5%");
        ctrlist.addHeader("Point", "5%");
        //edited by dewok 20180328
        if (iCommand == Command.EDIT) {
            ctrlist.addHeader("Tukar Poin", "5%");
        } else {
            ctrlist.addHeader("Aksi", "5%");
        }
        
        ctrlist.setLinkRow(1);
        ctrlist.setLinkSufix("");
        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        ctrlist.setLinkPrefix("javascript:cmdEdit('");
        ctrlist.setLinkSufix("')");
        ctrlist.reset();
        int index = -1;

        for (int i = 0; i < objectClass.size(); i++) {
            Vector temp = (Vector) objectClass.get(i);
            MemberReg memberReg = (MemberReg) temp.get(0);

            Vector rowx = new Vector();
            if (contactId == memberReg.getOID()) {
                index = i;
            }
            rowx.add("" + (i + 1 + start));
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

            MemberPoin memberPoin = PstMemberPoin.getTotalPoint(memberReg.getOID());
            int point = memberPoin.getCredit() - memberPoin.getDebet();
            rowx.add("<div align=\"center\">" + point + "</div>");
            
            if (iCommand == Command.EDIT) {
                if (oidReg == memberReg.getOID()) {
                    rowx.add("<div align=\"center\"><input size=1 type=\"text\" name=\"txt_point\"><a href=\"javascript:cmdSave()\">Simpan</a></div>");
                } else {
                    //edited by dewok 20180328
                    //rowx.add("<div align=\"center\">" + point + "</div>");
                    rowx.add("<div align=\"center\">"
                            + "<a href='javascript:cmdEdit(\""+memberReg.getOID()+"\")'><i class='fa fa-pencil'></i></a>"
                            + "&nbsp;&nbsp;&nbsp;"
                            + "<a href='javascript:cmdHistoryPoint(\""+memberReg.getOID()+"\")'><i class='fa fa-file-text-o'></i></a>"
                            + "</div>");
                }
            } else {
                //edited by dewok 20180328
                //rowx.add("<div align=\"center\">" + point + "</div>");                
                rowx.add("<div align=\"center\">"
                        + "<a href='javascript:cmdEdit(\""+memberReg.getOID()+"\")'><i class='fa fa-pencil'></i></a>"
                        + "&nbsp;&nbsp;&nbsp;"
                        + "<a href='javascript:cmdHistoryPoint(\""+memberReg.getOID()+"\")'><i class='fa fa-file-text-o'></i></a>"
                        + "</div>");
            }
            lstData.add(rowx);
            lstLinkData.add(String.valueOf(memberReg.getOID()));
        }
        //return ctrlist.drawList(index);
        return ctrlist.draw(index);
    }

%>

<%    
    int iCommand = FRMQueryString.requestCommand(request);
    int start = FRMQueryString.requestInt(request, "start");
    int prevCommand = FRMQueryString.requestInt(request, "prev_command");
    long oidMemberReg = FRMQueryString.requestLong(request, "hidden_contact_id");

    /*variable declaration*/
    boolean privManageData = true;
    int recordToGet = 20;
    SrcMemberReg srcMemberReg = new SrcMemberReg();
    if (iCommand == Command.LIST || iCommand == Command.EDIT || iCommand == Command.UPDATE || iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST || iCommand == Command.BACK) {
        try {
            srcMemberReg = (SrcMemberReg) session.getValue(SessMemberReg.SESS_SRC_MEMBERREG);
        } catch (Exception e) {
            srcMemberReg = new SrcMemberReg();
        }

        if (session.getValue(SessMemberReg.SESS_SRC_MEMBERREG) == null) {
            srcMemberReg = new SrcMemberReg();
        }
        session.putValue(SessMemberReg.SESS_SRC_MEMBERREG, srcMemberReg);
    }

    CtrlMemberReg ctrlMemberReg = new CtrlMemberReg(request);
    ControlLine ctrLine = new ControlLine();
    Vector listMemberReg = new Vector(1, 1);

    int iErrCode = ctrlMemberReg.action(iCommand, oidMemberReg, userId, userName);
    int vectSize = SessMemberReg.getCountSearch(srcMemberReg);
    String msgString = ctrlMemberReg.getMessage();

    if ((iCommand == Command.FIRST || iCommand == Command.PREV) || (iCommand == Command.NEXT || iCommand == Command.LAST)) {
        start = ctrlMemberReg.actionList(iCommand, start, vectSize, recordToGet);
    }

    listMemberReg = SessMemberReg.searchMemberReg(srcMemberReg, start, recordToGet);
    if (listMemberReg.size() < 1 && start > 0) {
        if (vectSize - recordToGet > recordToGet) {
            start = start - recordToGet;   //go to Command.PREV
        } else {
            start = 0;
            iCommand = Command.FIRST;
            prevCommand = Command.FIRST; //go to Command.FIRST
        }
        listMemberReg = SessMemberReg.searchMemberReg(srcMemberReg, start, recordToGet);
    }

    if (iCommand == Command.UPDATE) {
        oidMemberReg = 0;
    }
%>

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
    <head>
        <!-- #BeginEditable "doctitle" -->
        <title>Dimata - ProChain POS</title>
        <script language="JavaScript">
            function cmdSave() {
                document.frmmemberreg.command.value = "<%=Command.UPDATE%>";
                document.frmmemberreg.prev_command.value = "<%=prevCommand%>";
                document.frmmemberreg.action = "memberreg_list_point.jsp";
                document.frmmemberreg.submit();
            }

            function cmdEdit(oidMemberReg) {
                document.frmmemberreg.hidden_contact_id.value = oidMemberReg;
                document.frmmemberreg.command.value = "<%=Command.EDIT%>";
                document.frmmemberreg.prev_command.value = "<%=prevCommand%>";
                document.frmmemberreg.action = "memberreg_list_point.jsp";
                document.frmmemberreg.submit();
            }

            function cmdCancel(oidMemberReg) {
                document.frmmemberreg.hidden_contact_id.value = oidMemberReg;
                document.frmmemberreg.command.value = "<%=Command.EDIT%>";
                document.frmmemberreg.prev_command.value = "<%=prevCommand%>";
                document.frmmemberreg.action = "memberreg_list_point.jsp";
                document.frmmemberreg.submit();
            }

            function cmdBack() {
                document.frmmemberreg.command.value = "<%=Command.BACK%>";
                document.frmmemberreg.action = "srcmemberreg_point.jsp";
                document.frmmemberreg.submit();
            }

            function cmdListFirst() {
                document.frmmemberreg.command.value = "<%=Command.FIRST%>";
                document.frmmemberreg.prev_command.value = "<%=Command.FIRST%>";
                document.frmmemberreg.action = "memberreg_list_point.jsp";
                document.frmmemberreg.submit();
            }

            function cmdListPrev() {
                document.frmmemberreg.command.value = "<%=Command.PREV%>";
                document.frmmemberreg.prev_command.value = "<%=Command.PREV%>";
                document.frmmemberreg.action = "memberreg_list_point.jsp";
                document.frmmemberreg.submit();
            }

            function cmdListNext() {
                document.frmmemberreg.command.value = "<%=Command.NEXT%>";
                document.frmmemberreg.prev_command.value = "<%=Command.NEXT%>";
                document.frmmemberreg.action = "memberreg_list_point.jsp";
                document.frmmemberreg.submit();
            }

            function cmdListLast() {
                document.frmmemberreg.command.value = "<%=Command.LAST%>";
                document.frmmemberreg.prev_command.value = "<%=Command.LAST%>";
                document.frmmemberreg.action = "memberreg_list_point.jsp";
                document.frmmemberreg.submit();
            }
            
            function cmdPrint() {
                window.open("print_out_member_point.jsp?command=<%=Command.LIST%>", "print_out_membership");
            }
            
            function cmdHistoryPoint(oidMemberReg) {
                window.open("history_member_point.jsp?command=<%=Command.LIST%>&oidDocHistory="+oidMemberReg, "print_out_membership","scrollbars=yes,height=500,width=700,left=300,status=no,toolbar=no,menubar=yes,location=no");
            }
            
            function cmdCancelPoint() {
                document.frmmemberreg.command.value = "<%=Command.LIST%>";
                document.frmmemberreg.action = "memberreg_list_point.jsp";
                document.frmmemberreg.submit();
            }

            //-------------- script control line -------------------
            function MM_swapImgRestore() { //v3.0
                var i, x, a = document.MM_sr;
                for (i = 0; a && i < a.length && (x = a[i]) && x.oSrc; i++)
                    x.src = x.oSrc;
            }

            function MM_preloadImages() { //v3.0
                var d = document;
                if (d.images) {
                    if (!d.MM_p)
                        d.MM_p = new Array();
                    var i, j = d.MM_p.length, a = MM_preloadImages.arguments;
                    for (i = 0; i < a.length; i++)
                        if (a[i].indexOf("#") != 0) {
                            d.MM_p[j] = new Image;
                            d.MM_p[j++].src = a[i];
                        }
                }
            }

            function MM_findObj(n, d) { //v4.0
                var p, i, x;
                if (!d)
                    d = document;
                if ((p = n.indexOf("?")) > 0 && parent.frames.length) {
                    d = parent.frames[n.substring(p + 1)].document;
                    n = n.substring(0, p);
                }
                if (!(x = d[n]) && d.all)
                    x = d.all[n];
                for (i = 0; !x && i < d.forms.length; i++)
                    x = d.forms[i][n];
                for (i = 0; !x && d.layers && i < d.layers.length; i++)
                    x = MM_findObj(n, d.layers[i].document);
                if (!x && document.getElementById)
                    x = document.getElementById(n);
                return x;
            }

            function MM_swapImage() { //v3.0
                var i, j = 0, x, a = MM_swapImage.arguments;
                document.MM_sr = new Array;
                for (i = 0; i < (a.length - 2); i += 3)
                    if ((x = MM_findObj(a[i])) != null) {
                        document.MM_sr[j++] = x;
                        if (!x.oSrc)
                            x.oSrc = x.src;
                        x.src = a[i + 2];
                    }
            }

        </script>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
        <% if (menuUsed == MENU_ICON) {%>
        <link href="../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
        <%}%>
        <link rel="stylesheet" href="../styles/main.css" type="text/css">
        <link rel="stylesheet" href="../styles/tab.css" type="text/css">
        <SCRIPT language=JavaScript>
        </SCRIPT>
    </head> 

    <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
            <%if (menuUsed == MENU_PER_TRANS) {%>
            <tr>
                <td height="25" ID="TOPTITLE">
                    <%@ include file = "../main/header.jsp" %>
                </td>
            </tr>
            <tr>
                <td height="20" ID="MAINMENU"> 
                    <%@ include file = "../main/mnmain.jsp" %>
                </td>
            </tr>
            <%} else {%>
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
                            <td height="20" class="mainheader">
                                Membership &gt; Daftar member untuk ubah point.
                            </td>
                        </tr>
                        <tr> 
                            <td>
                                <form name="frmmemberreg" method ="post" action="">
                                    <input type="hidden" name="command" value="<%=iCommand%>">
                                    <input type="hidden" name="vectSize" value="<%=vectSize%>">
                                    <input type="hidden" name="start" value="<%=start%>">
                                    <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                                    <input type="hidden" name="hidden_contact_id" value="<%=oidMemberReg%>">

                                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                        <tr align="left" valign="top">
                                            <td height="8"  colspan="">
                                                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                    <tr align="left" valign="top">
                                                        <td height="22" valign="middle" colspan="">
                                                        <%
                                                            try {
                                                                if (listMemberReg.size() > 0) {
                                                        %>

                                                        <%= drawList(listMemberReg, oidMemberReg, SESS_LANGUAGE, start, iCommand, oidMemberReg)%>                                                             

                                                        <%
                                                                } else {
                                                                    out.print("Data membership tidak ditemukan...");
                                                                }
                                                            } catch (Exception exc) {
                                                            }
                                                        %>
                                                        </td>
                                                    </tr>
                                                    <tr align="left" valign="top">
                                                        <td height="8" align="left" colspan="" class="command">
                                                            <span class="command">
                                                                <%
                                                                    int cmd = 0;
                                                                    if ((iCommand == Command.FIRST || iCommand == Command.PREV) || (iCommand == Command.NEXT || iCommand == Command.LAST)) {
                                                                        cmd = iCommand;
                                                                    } else {
                                                                        if (iCommand == Command.NONE || prevCommand == Command.NONE) {
                                                                            cmd = Command.FIRST;
                                                                        } else {
                                                                            cmd = prevCommand;
                                                                        }
                                                                    }
                                                                %>

                                                                <%
                                                                    ctrLine.setLocationImg(approot + "/images");
                                                                    ctrLine.initDefault();
                                                                %>
                                                                
                                                                <%=ctrLine.drawImageListLimit(cmd, vectSize, start, recordToGet)%> 
                                                            </span>
                                                        </td>
                                                    </tr>
                                                    <tr align="left" valign="top">
                                                        <td height="22" valign="middle" colspan="">
                                                            
                                                        </td>
                                                    </tr>
                                                </table>
                                                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                    <tr>
                                                        <%if (privAdd && privManageData) {%>
                                                        <%if (iCommand == Command.NONE || iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST || iCommand == Command.LIST || iCommand == Command.BACK || iCommand == Command.SAVE || iCommand == Command.DELETE) {%>
                                                        <%}%>
                                                        <%}%>
                                                        <!--td width="5%" nowrap><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnBackOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE, textListTitle[SESS_LANGUAGE][0], ctrLine.CMD_BACK_SEARCH, true)%>"></a></td-->
                                                        <td width="" nowrap class="command"><a class="btn btn-primary" href="javascript:cmdBack()"><i class="fa fa-arrow-left"></i> <%=ctrLine.getCommand(SESS_LANGUAGE, textListTitle[SESS_LANGUAGE][0], ctrLine.CMD_BACK_SEARCH, true)%></a></td>
                                                        <%if (iCommand == Command.EDIT) {%>
                                                        <td width="" nowrap class="command" style=""><a title="Tukar poin" class="btn btn-primary" href="javascript:cmdCancelPoint()"><i class="fa fa-remove"></i> Batal</a></td>
                                                        <%}%>
                                                        <td width="" nowrap class="command" style="text-align: right"><a title="History penukaran poin" class="btn btn-primary" href="javascript:cmdPrint()"><i class="fa fa-print"></i> Cetak</a></td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                    </table>
                                </form>
                                <%//@ include file = "../main/menumain.jsp" %>
                                <!-- #EndEditable -->
                            </td> 
                        </tr> 
                    </table>
                </td>
            </tr>
            <tr> 
                <td colspan="" height="20"> <!-- #BeginEditable "footer" -->
                    <% 
                        if (menuUsed == MENU_ICON) {
                    %>

                    <%@include file="../styletemplate/footer.jsp" %>

                    <%} else {%>

                    <%@ include file = "../main/footer.jsp" %>

                    <%}%>
                    <!-- #EndEditable --> 
                </td>
            </tr>
        </table>
    </body>
    <!-- #EndTemplate -->
</html>

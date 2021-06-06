<%-- 
    Document   : print_out_member_point
    Created on : Mar 22, 2018, 3:03:02 PM
    Author     : Dimata 007
--%>

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

<%@page import="com.dimata.posbo.entity.masterdata.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER, AppObjInfo.OBJ_TRANSFER);%>
<%@ include file = "../main/checkuser.jsp" %>

<%//
    Vector<Company> company = PstCompany.list(0, 0, "", "");
    String compName = "";
    if (!company.isEmpty()) {
        compName = company.get(0).getCompanyName().toUpperCase();
    }
%>

<%//
    int iCommand = FRMQueryString.requestCommand(request);
    int start = FRMQueryString.requestInt(request, "start");
    int prevCommand = FRMQueryString.requestInt(request, "prev_command");
    long oidMemberReg = FRMQueryString.requestLong(request, "hidden_contact_id");

    /*variable declaration*/
    boolean privManageData = true;
    int recordToGet = 0;
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

    int iErrCode = ctrlMemberReg.action(iCommand, oidMemberReg);
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

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="../styles/bootstrap/css/bootstrap.min.css"/>
        <style>
            table {font-size: 14px}            

            .tabel_header {width: 100%}
            .tabel_header td {padding-bottom: 10px}

            .tabel_data {border-color: black !important}
            
            .tabel_data th {
                //text-align: center;
                border-color: black !important;                
                border-bottom-width: thin !important;
                padding: 4px 8px !important;
            }
            .tabel_data td {
                border-color: black !important;
                padding: 4px 8px !important
            }
        </style>
    </head>
    <body>
        <div style="margin: 10px">
            
            <br>
            <div>
                <img style="width: 100px" src="../images/litama.jpeg">
                <span style="font-size: 24px; font-family: TimesNewRomans"><b><%=compName%></b> <small><i>Gallery</i></small></span>
            </div>
            
            <div>
                <h4><b>Membership Poin</b></h4>
            </div>
            
            <table class="tabel_header">
                <tr>
                    <td style="width: 5%">Tanggal dicetak : <%=Formater.formatDate(new Date(), "dd/MM/yyyy")%></td>
                </tr>
            </table>
            
            <hr style="border-width: medium; border-color: black; margin-top: 5px">
            
            <table class="table table-bordered tabel_data">
                <tr>
                    <th style="width: 1%">No.</th>
                    <th>Barcode</th>
                    <th>Nama</th>
                    <th>Alamat</th>
                    <th>No. Telpon</th>
                    <th>No. HP</th>
                    <th>Poin</th>
                </tr>
                
                <%
                    for (int i=0; i<listMemberReg.size(); i++) {
                        Vector temp = (Vector) listMemberReg.get(i);
                        MemberReg memberReg = (MemberReg) temp.get(0);
                        
                        MemberPoin memberPoin = PstMemberPoin.getTotalPoint(memberReg.getOID());
                        int point = memberPoin.getCredit() - memberPoin.getDebet();
                %>
                
                <tr>
                    <td><%=(i+1)%></td>
                    <td><%=memberReg.getMemberBarcode()%></td>
                    <td><%=memberReg.getPersonName()%></td>
                    <td><%=memberReg.getHomeAddr()%></td>
                    <td><%=memberReg.getHomeTelp()%></td>
                    <td><%=memberReg.getTelpMobile()%></td>
                    <td class="text-center"><%=point%></td>
                </tr>
                
                <%
                    }
                %>
                
            </table>
            
        </div>
    </body>
</html>

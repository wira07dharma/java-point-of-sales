<%-- 
    Document   : print_out_supplier
    Created on : Apr 26, 2018, 2:32:10 PM
    Author     : Dimata 007
--%>

<%@page import="com.dimata.common.entity.contact.ContactList"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.posbo.session.masterdata.*"%>
<%@page import="com.dimata.posbo.entity.search.*"%>
<%@page import="com.dimata.posbo.entity.masterdata.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_SUPPLIER); %>
<%@ include file = "../../main/checkuser.jsp" %>

<%//
    int iCommand = FRMQueryString.requestCommand(request);

    SrcMemberReg srcMemberReg = new SrcMemberReg();
    if (iCommand == Command.LIST || iCommand == Command.FIRST || iCommand == Command.PREV
            || iCommand == Command.NEXT || iCommand == Command.LAST || iCommand == Command.BACK) {
        try {
            srcMemberReg = (SrcMemberReg) session.getValue("SESS_SUPPLIER");
        } catch (Exception e) {
            srcMemberReg = new SrcMemberReg();
        }

        if (session.getValue("SESS_SUPPLIER") == null) {
            srcMemberReg = new SrcMemberReg();
        }
        session.putValue("SESS_SUPPLIER", srcMemberReg);
    }
    Vector listSupplier = SessMemberReg.searchSupplier(srcMemberReg, 0, 0);
%>

<%//
    Vector<Company> company = PstCompany.list(0, 0, "", "");
    String compName = "";
    if (!company.isEmpty()) {
        compName = company.get(0).getCompanyName().toUpperCase();
    }
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="../../styles/bootstrap/css/bootstrap.min.css"/>
        <style>
            table {font-size: 14px}            

            .tabel_header {width: 100%}
            .tabel_header td {padding-bottom: 10px}

            .tabel_data {
                border-color: black !important;
                font-size: 12px
            }   
            
            .tabel_data th {
                text-align: center;
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
              <img style="width: 100px" src="../../images/company.jpg">
                <span style="font-size: 24px; font-family: TimesNewRomans"><b><%=compName%></b> <small><i>Gallery</i></small></span>
            </div>
            
            <div>
                <h4><b>Daftar Supplier</b></h4>
            </div>
            
            <div class="">
                Tanggal dicetak : <%=Formater.formatDate(new Date(), "dd/MM/yyyy") %>
            </div>
            
            <hr style="border-width: medium; border-color: black; margin-top: 5px">
            
            <table class="table table-bordered tabel_data">
                <tr>
                    <th>No.</th>
                    <th>Kode Supplier</th>
                    <th>Nama Perusahaan</th>
                    <th>Kontak Person</th>
                    <th>Telepon</th>
                    <th>Fax</th>
                    <th>Alamat Bisnis</th>                    
                </tr>
                
                <%//
                    for (int i = 0; i < listSupplier.size(); i++) {
                        ContactList member = (ContactList) listSupplier.get(i);
                %>
                
                <tr>
                    <td><%=(i+1)%></td>
                    <td><%=member.getContactCode() %></td>
                    <td><%=member.getCompName() %></td>
                    <td><%=member.getPersonName() %></td>
                    <td><%=member.getTelpNr() %></td>
                    <td><%=member.getFax() %></td>
                    <td><%=member.getBussAddress() %></td>
                </tr>
                
                <%
                    }
                %>
                
            </table>
                   
                <!--//START TEMPLATE TANDA TANGAN//-->                
                <% int signType = 1; %>
                <%@ include file = "../../templates/template_sign.jsp" %>
                <!--//END TEMPLATE TANDA TANGAN//-->
                
        </div>
    </body>
</html>

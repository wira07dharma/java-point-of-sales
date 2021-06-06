<%-- 
    Document   : master_kontak
    Created on : Nov 23, 2019, 12:04:55 PM
    Author     : Regen
--%>

<%@page import="com.dimata.common.entity.contact.PstContactClass"%>
<%@page import="com.dimata.common.entity.contact.ContactClass"%>
<%@page import="com.dimata.common.form.contact.FrmContactClass"%>
<%@page import="com.dimata.common.form.contact.CtrlContactClass"%>
<%@page import="com.dimata.common.form.contact.CtrlContactList"%>
<%@page import="com.dimata.qdep.form.FRMMessage"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_PRICE_TYPE);%>
<%@ include file = "../main/checkuser.jsp" %>

<%!    public static final String textListTitle[][]
            = {
                {"Contact Class", "Harus diisi", "List Contact", "Contact"},
                {"Contact Class", "required", "List Contact", "Contact"}
    };
%>
<%
    int iCommand = FRMQueryString.requestCommand(request);
    int cmdMinimum = FRMQueryString.requestInt(request, "command_minimum");
    int startContact = FRMQueryString.requestInt(request, "start_contact");
    int startMaterial = FRMQueryString.requestInt(request, "start_material");
    int prevCommand = FRMQueryString.requestInt(request, "prev_command");
    long oidContact = FRMQueryString.requestLong(request, "hidden_contact_id");
    long oidMinimum = FRMQueryString.requestLong(request, "hidden_mat_minimum_id");

    int iErrCode = FRMMessage.NONE;
    int recordToGet = 10;
    int vectSize = 0;
    String msgString = "";
    String whereClause = "";
    String order = PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE] +" = ASC";
    CtrlContactClass ctrlContactClass = new CtrlContactClass(request);
    iErrCode = ctrlContactClass.action(iCommand, oidContact);
    FrmContactClass frmContactClass = ctrlContactClass.getForm();
    
    vectSize = PstContactClass.getCount(whereClause);
		startContact = ctrlContactClass.actionList(iCommand, startContact, vectSize, recordToGet);
    
    ContactClass contactClass = ctrlContactClass.getContactClass();
    msgString =  ctrlContactClass.getMessage();
    Vector listContactClass = PstContactClass.list(0, 0, whereClause, "");
%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <%@include file="../styles/plugin_component.jsp" %>
    <title>Dimata - ProChain POS</title>
    <script>
                  function cmdAdd()
            {
                document.frmContact.hidden_contact_id.value = "0";
                document.frmContact.command.value = "<%=Command.ADD%>";
                document.frmContact.prev_command.value = "<%=prevCommand%>";
                document.frmContact.action = "master_kontak.jsp";
                document.frmContact.submit();
            }

            function cmdAsk(oidContact)
            {
                document.frmContact.hidden_contact_id.value = oidContact;
                document.frmContact.command.value = "<%=Command.ASK%>";
                document.frmContact.prev_command.value = "<%=prevCommand%>";
                document.frmContact.action = "master_kontak.jsp";
                document.frmContact.submit();
            }

            function cmdDelete(oidContact)
            {
                if(confirm("Are you sure want to delete this?")){
                document.frmContact.hidden_contact_id.value = oidContact;
                document.frmContact.command.value = "<%=Command.DELETE%>";
                document.frmContact.prev_command.value = "<%=prevCommand%>";
                document.frmContact.action = "master_kontak.jsp";
                document.frmContact.submit();
              }
            }

            function cmdSave()
            {
                document.frmContact.command.value = "<%=Command.SAVE%>";
                document.frmContact.prev_command.value = "<%=prevCommand%>";
                document.frmContact.action = "master_kontak.jsp";
                document.frmContact.submit();
            }

            function cmdEdit(oidContact)
            {
                document.frmContact.hidden_contact_id.value = oidContact;
                document.frmContact.command.value = "<%=Command.EDIT%>";
                document.frmContact.prev_command.value = "<%=prevCommand%>";
                document.frmContact.action = "master_kontak.jsp";
                document.frmContact.submit();
            }

            function cmdCancel(oidContact)
            {
                document.frmContact.hidden_contact_id.value = oidContact;
                document.frmContact.command.value = "<%=Command.EDIT%>";
                document.frmContact.prev_command.value = "<%=prevCommand%>";
                document.frmContact.action = "master_kontak.jsp";
                document.frmContact.submit();
            }

            function cmdBack()
            {
                document.frmContact.command.value = "<%=Command.BACK%>";
                document.frmContact.action = "master_kontak.jsp";
                document.frmContact.submit();
            }
    </script>
    <style>
      body{
        background-color: #eee
      }
      .form-group {
          margin: 0px 0px 40px 0px;
      }
      .pad-top-5{
        padding-top: 5px;
      }
      .hd-pos {
          background-color: #579aff;
          color: #fff;
      }
      .sorting_disabled {
          text-align: center;
      }
      tbody > tr > td {
          text-align: center;
          border: 1px solid #579aff !important;
          background-color: #fff !important;
      }
      table.dataTable thead > tr > th {
          padding-right: 0;
      }
    </style>
  </head>
  <body>
    <section class="content-header">
      <h1>Master Data<small> <%=textListTitle[SESS_LANGUAGE][0]%></small> </h1>
      <ol class="breadcrumb">
        <li><%=textListTitle[SESS_LANGUAGE][0]%></li>
      </ol>
    </section>
    <p class="line"></p>
    <section class="content">
      <form name="frmContact" method ="post" action="">
      <input type="hidden" name="command" value="<%=iCommand%>">
      <input type="hidden" name="vectSize" value="<%=vectSize%>">
      <input type="hidden" name="start_contact" value="<%=startContact%>">
      <input type="hidden" name="prev_command" value="<%=prevCommand%>">
      <input type="hidden" name="hidden_contact_id" value="<%=oidContact%>">
      <input type="hidden" name="hidden_mat_minimum_id" value="<%=oidMinimum%>">	
          <%
              if ((iCommand == Command.ADD) || (iCommand == Command.EDIT) || (iCommand == Command.ASK) || ((iCommand == Command.SAVE) && iErrCode > 0) || (iCommand == Command.DELETE && iErrCode > 0)) {
          %>
      <div id="data-contact" class="box box-primary">
        <div class="box-header with-border">
          <label class="box-title pull-left"><%=textListTitle[SESS_LANGUAGE][3]%></label>
        </div>
        <div class="box-body">
          <div class="row">
            <div class="col-md-12">
              <div class="col-md-6">
                <div class="form-group">
                  <label class="col-sm-4 pad-top-5" for="FRM_FIELD_CLASS_NAME">Contact Name</label>
                  <div class="col-sm-8">
                    <input type="text" id="FRM_FIELD_CLASS_NAME" name="<%=frmContactClass.fieldNames[frmContactClass.FRM_FIELD_CLASS_NAME]%>" class="form-control input-sm" value="<%=contactClass.getClassName() %>">
                  </div>
                </div>
                  <div class="form-group">
                    <label class="col-sm-4 pad-top-5">Contact Type</label>
                    <div class="col-sm-8">
                      <select class="form-control input-sm" name="<%=frmContactClass.fieldNames[frmContactClass.FRM_FIELD_CLASS_TYPE] %>">
                        <%
                        for(int ii = 1; ii < 50; ii++ ){
                        %>
                        <option <%=(ii == contactClass.getClassType() ? "selected" : "")%> value="<%=String.valueOf(ii)%>"><%=ii %></option>
                        <%}%>
                      </select>
                    </div>
                  </div>
              </div>
              <div class="col-md-6">
                <div class="form-group">
                  <label class="col-sm-4 pad-top-5" for="FRM_FIELD_CLASS_DESCRIPTION">Description</label>
                  <div class="col-sm-8">
                    <textarea id="FRM_FIELD_CLASS_DESCRIPTION" name="<%=frmContactClass.fieldNames[frmContactClass.FRM_FIELD_CLASS_DESCRIPTION] %>" class="form-control input-sm"><%=contactClass.getClassDescription() %></textarea>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <p></p>
          <div class="box-footer">
            <a class="btn btn-success btn-sm" href="javascript:cmdSave()"><i class="fa fa-save"> </i> Save Contact</a>
            <a class="btn btn-primary btn-sm" href="javascript:cmdBack()"><i class="fa fa-arrow-left"> </i> Back Contact</a>
          </div>
        </div>
      </div>
        <%}%>
      <div id="data-contact" class="box box-primary">
        <div class="box-header with-border">
          <label class="box-title pull-left"><%=textListTitle[SESS_LANGUAGE][2]%></label>
          <a class="btn btn-primary btn-sm pull-right" href="javascript:cmdAdd()">Add Contact</a>
        </div>
        <div class="box-body">
          <div class="table-responsive">
            <table id="listContact" class="table table-striped table-bordered" style="width:100%">
              <thead class="headerList">
                <tr class="hd-pos">
                  <th>No</th>
                  <th>Contact Type</th>
                  <th>Contact Name</th>
                  <th>Description</th>
                  <th>Action</th>
                </tr>
              </thead>
                <%
                 for(int i = 0; i < listContactClass.size(); i++){
                   ContactClass con = (ContactClass) listContactClass.get(i);
                   oidContact = con.getOID();
                %>
                <tr class="bd-pos">
                  <td><%=i + 1 %></td>
                  <td><%=con.getClassType()  %></td>
                  <td><%=con.getClassName() %></td>
                  <td><%=con.getClassDescription() %></td>
                  <td>
                    <a class="btn btn-success btn-sm" href="javascript:cmdEdit('<%=oidContact %>')" data-oid="<%=oidContact %>"><i class="fa fa-edit"></i></a>
                  <a class="btn btn-danger btn-sm" href="javascript:cmdDelete('<%=oidContact %>')" data-oid="<%=oidContact %>"><i class="fa fa-trash"></i></a>
                  </td>
                </tr>
                <%}%>
            </table>
          </div>
          <div class="box-footer">
          </div>
        </div>
      </div>
      </form>
    </section>
      <script>
          $('#listContact').DataTable({
          "paging":   false,
          "ordering": false,
          "info":     false
          });
      </script>
  </body>
</html>
  
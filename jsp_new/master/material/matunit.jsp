<%-- 
    Document   : matunit
    Created on : Nov 23, 2019, 12:04:55 PM
    Author     : Regen
--%>


<%@page import="com.dimata.posbo.form.masterdata.FrmUnit"%>
<%@page import="com.dimata.posbo.entity.masterdata.Unit"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstUnit"%>
<%@page import="com.dimata.posbo.form.masterdata.CtrlUnit"%>
<%@page import="com.dimata.qdep.form.FRMMessage"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_PRICE_TYPE);%>
<%@ include file = "../../main/checkuser.jsp" %>

<%!    public static final String textListTitle[][]
            = {
                {"Satuan", "Harus diisi", "List Satuan", "Satuan"},
                {"Base Unit", "required", "List Base Unit", "Base Unit"}
    };
%>
<%
    int iCommand = FRMQueryString.requestCommand(request);
    int cmdMinimum = FRMQueryString.requestInt(request, "command_minimum");
    int startSatuan = FRMQueryString.requestInt(request, "start_satuan");
    int startMaterial = FRMQueryString.requestInt(request, "start_material");
    int prevCommand = FRMQueryString.requestInt(request, "prev_command");
    long oidSatuan = FRMQueryString.requestLong(request, "hidden_satuan_id");
    long oidMinimum = FRMQueryString.requestLong(request, "hidden_mat_minimum_id");

    int iErrCode = FRMMessage.NONE;
    int recordToGet = 10;
    int vectSize = 0;
    String msgString = "";
    String whereClause = "";
    String order = PstUnit.fieldNames[PstUnit.FLD_CODE] +" = ASC";
    CtrlUnit ctrlUnit = new CtrlUnit(request);
    iErrCode = ctrlUnit.action(iCommand, oidSatuan);
    FrmUnit frmUnit = ctrlUnit.getForm();
    
    vectSize = PstUnit.getCount(whereClause);
		startSatuan = ctrlUnit.actionList(iCommand, startSatuan, vectSize, recordToGet);
    
    Unit unit = ctrlUnit.getUnit();
    msgString =  ctrlUnit.getMessage();
    Vector listUnitSatuan = PstUnit.list(0, 0, whereClause, "");
    Vector vectBase = PstUnit.list(0,0,"","CODE");
%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <%@include file="../../styles/plugin_component.jsp" %>
    <title>Dimata - ProChain POS</title>
    <script>
                  function cmdAdd()
            {
                document.frmContact.hidden_satuan_id.value = "0";
                document.frmContact.command.value = "<%=Command.ADD%>";
                document.frmContact.prev_command.value = "<%=prevCommand%>";
                document.frmContact.action = "matunit.jsp";
                document.frmContact.submit();
            }

            function cmdAsk(oidSatuan)
            {
                document.frmContact.hidden_satuan_id.value = oidSatuan;
                document.frmContact.command.value = "<%=Command.ASK%>";
                document.frmContact.prev_command.value = "<%=prevCommand%>";
                document.frmContact.action = "matunit.jsp";
                document.frmContact.submit();
            }

            function cmdDelete(oidSatuan)
            {
                if(confirm("Are you sure want to delete this?")){
                document.frmContact.hidden_satuan_id.value = oidSatuan;
                document.frmContact.command.value = "<%=Command.DELETE%>";
                document.frmContact.prev_command.value = "<%=prevCommand%>";
                document.frmContact.action = "matunit.jsp";
                document.frmContact.submit();
              }
            }

            function cmdSave()
            {
                document.frmContact.command.value = "<%=Command.SAVE%>";
                document.frmContact.prev_command.value = "<%=prevCommand%>";
                document.frmContact.action = "matunit.jsp";
                document.frmContact.submit();
            }

            function cmdEdit(oidSatuan)
            {
                document.frmContact.hidden_satuan_id.value = oidSatuan;
                document.frmContact.command.value = "<%=Command.EDIT%>";
                document.frmContact.prev_command.value = "<%=prevCommand%>";
                document.frmContact.action = "matunit.jsp";
                document.frmContact.submit();
            }

            function cmdCancel(oidSatuan)
            {
                document.frmContact.hidden_satuan_id.value = oidSatuan;
                document.frmContact.command.value = "<%=Command.EDIT%>";
                document.frmContact.prev_command.value = "<%=prevCommand%>";
                document.frmContact.action = "matunit.jsp";
                document.frmContact.submit();
            }

            function cmdBack()
            {
                document.frmContact.command.value = "<%=Command.BACK%>";
                document.frmContact.action = "matunit.jsp";
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
      <input type="hidden" name="start_satuan" value="<%=startSatuan%>">
      <input type="hidden" name="prev_command" value="<%=prevCommand%>">
      <input type="hidden" name="hidden_satuan_id" value="<%=oidSatuan%>">
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
                  <label class="col-sm-4 pad-top-5" for="FRM_FIELD_CODE">Code</label>
                  <div class="col-sm-8">
                    <input type="text" id="FRM_FIELD_CODE" name="<%=frmUnit.fieldNames[frmUnit.FRM_FIELD_CODE]%>" class="form-control input-sm" value="<%=unit.getCode()%>">
                  </div>
                </div>
                <div class="form-group">
                  <label class="col-sm-4 pad-top-5" for="FRM_FIELD_NAME">Name</label>
                  <div class="col-sm-8">
                    <input type="text" id="FRM_FIELD_NAME" name="<%=frmUnit.fieldNames[frmUnit.FRM_FIELD_NAME]%>" class="form-control input-sm" value="<%=unit.getName()%>">
                  </div>
                </div>
              </div>
              <div class="col-md-6">
                  <div class="form-group">
                    <label class="col-sm-4 pad-top-5">Base Unit</label>
                    <div class="col-sm-8">
                      <select class="form-control input-sm" name="<%=frmUnit.fieldNames[frmUnit.FRM_FIELD_BASE_UNIT_ID] %>">
                        <option value="0">-</option>
                        <%
                        for(int ii = 0; ii < vectBase.size(); ii++ ){
                        Unit ut = (Unit) vectBase.get(ii);
                        %>
                        <option <%=(ut.getOID() == unit.getBaseUnitId()? "selected" : "")%> value="<%=ut.getOID() %>"><%=ut.getCode() %></option>
                        <%}%>
                      </select>
                    </div>
                  </div>
                <div class="form-group">
                  <label class="col-sm-4 pad-top-5" for="FRM_FIELD_QTY_PER_BASE_UNIT">Qty Base Unit</label>
                  <div class="col-sm-8">
                    <input type="text" id="FRM_FIELD_QTY_PER_BASE_UNIT" name="<%=frmUnit.fieldNames[frmUnit.FRM_FIELD_QTY_PER_BASE_UNIT]%>" class="form-control input-sm" value="<%=unit.getQtyPerBaseUnit()%>">
                  </div>
                </div>
              </div>
            </div>
          </div>
          <p></p>
          <div class="box-footer">
            <a class="btn btn-success btn-sm" href="javascript:cmdSave()"><i class="fa fa-save"> </i> Save Base Unit</a>
            <a class="btn btn-primary btn-sm" href="javascript:cmdBack()"><i class="fa fa-arrow-left"> </i> Back Base Unit</a>
          </div>
        </div>
      </div>
        <%}%>
      <div id="data-contact" class="box box-primary">
        <div class="box-header with-border">
          <label class="box-title pull-left"><%=textListTitle[SESS_LANGUAGE][2]%></label>
          <a class="btn btn-primary btn-sm pull-right" href="javascript:cmdAdd()">Add Base Unit</a>
        </div>
        <div class="box-body">
          <div class="table-responsive">
            <table id="listSatuan" class="table table-striped table-bordered" style="width:100%">
              <thead class="headerList">
                <tr class="hd-pos">
                  <th>No</th>
                  <th>Code</th>
                  <th>Name</th>
                  <th>Base Unit</th>
                  <th>Qty Base Unit</th>
                  <th>Action</th>
                </tr>
              </thead>
                <%
                 for(int i = 0; i < listUnitSatuan.size(); i++){
                   Unit un = (Unit) listUnitSatuan.get(i);
                   oidSatuan = un.getOID();
                %>
                <tr class="bd-pos">
                  <td><%=i + 1 %></td>
                  <td><%=un.getCode()%></td>
                  <td><%=un.getName()%></td>
                  <%
                   String unitName = "-";
                   for(int k = 0; k < vectBase.size(); k++){
                   Unit xUnit = (Unit) vectBase.get(k);
                   if(xUnit.getOID()==un.getBaseUnitId()){
                   unitName = xUnit.getCode();
                   break;
                   }
                    }
                  %>
                  <td><%=unitName %></td>
                  <td><%=un.getQtyPerBaseUnit() %></td>
                  <td>
                    <a class="btn btn-success btn-sm" href="javascript:cmdEdit('<%=oidSatuan %>')" data-oid="<%=oidSatuan %>"><i class="fa fa-edit"></i></a>
                  <a class="btn btn-danger btn-sm" href="javascript:cmdDelete('<%=oidSatuan %>')" data-oid="<%=oidSatuan %>"><i class="fa fa-trash"></i></a>
                  </td>
                </tr>
                <%}%>
            </table>
          </div>
          <div class="box-footer">
            <%
            if(useForRaditya.equals("0")){
            %>
            <div class="col-md-12">
              <div class="col-md-6">
                <label>Note</label>
                <p>UnitID = Kg -> Satuan Dasar -> gr = 1000</p>
                <p>UnitID = Gr -> Satuan Dasar -> Kg = 0.001</p>
              </div>
              <div class="col-md-6"></div>
            </div>
            <%}%> 
          </div>
        </div>
      </div>
      </form>
    </section>
      <script>
          $('#listSatuan').DataTable({
          "paging":   false,
          "ordering": false,
          "info":     false
          });
      </script>
  </body>
</html>
  
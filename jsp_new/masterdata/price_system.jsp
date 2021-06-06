<%-- 
    Document   : price_system
    Created on : Nov 7, 2019, 2:59:58 PM
    Author     : Regen
--%>

<%@page import="com.dimata.qdep.form.FRMMessage"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.posbo.form.masterdata.CtrlPriceSystem"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.posbo.entity.masterdata.PriceSystem"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstPriceSystem"%>
<%@page import="com.dimata.posbo.form.masterdata.FrmPriceSystem"%>
<%@page import="com.dimata.posbo.entity.admin.AppObjInfo"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file ="../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK_REPORT, AppObjInfo.OBJ_STOCK_POTITION); %>
<%@ include file ="../main/checkuser.jsp" %>

<%!  
public static final String textListGlobal[][] =
    {
        {"Penentuan Harga","Penjualan", "Tipe Harga", "List Penentuan"},
        {"Formula Price","Sell", "Price Type", "List Formula"}
    };

  public static final String textListTitle[][] =
    {
        {"Tipe harga","Harus diisi"},
        {"Price Type","required"}
    };
	
	public static final String textListHeader[][] =
    {
        {"No","Nama","Urutan", "Keterangan"},
        {"No","Name","Index", "Note"}
    };
%>
<%
  
int iErrCode = FRMMessage.NONE;
int prevCommand = FRMQueryString.requestInt(request, "prev_command");
int iCommand = FRMQueryString.requestCommand(request);
CtrlPriceSystem ctrlPriceSystem = new CtrlPriceSystem(request);
FrmPriceSystem  frmPriceSystem = new FrmPriceSystem();
PriceSystem priceSystem = new PriceSystem();
long oidPriceSystem = FRMQueryString.requestLong(request, "FRM_FIELD_PRICE_ID");

iErrCode = ctrlPriceSystem.action(iCommand , oidPriceSystem);
%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <%@include file="../styles/plugin_component.jsp" %>
    <title>Dimata - ProChain POS</title>
    <style>
      body{
        background-color: #eee;
      }
      input.form-control {
          margin: 0px 10px 10px 0px;
      }
      th {
          text-align: center;
          font-size: 12px;
      }
      thead.headerList {
          background-color: #3c8dbc;
          color: #fff;
          text-align: center;
      }
      td {
          font-size: 14px;
          text-align: center;
          border: 1px solid #428bca !important;
      }
    </style>
    <script language="JavaScript">

   function cmdSave() {
        document.FRM_NAME_PRICE_TYPE.command.value = "<%=Command.SAVE %>";
        document.FRM_NAME_PRICE_TYPE.prev_command.value = "<%=prevCommand %>";
        document.FRM_NAME_PRICE_TYPE.action = "price_system.jsp";
        document.FRM_NAME_PRICE_TYPE.submit();
      } 
   function cmdGo() {
        document.FRM_NAME_PRICE_TYPE.command.value = "<%=Command.BACK %>";
        document.FRM_NAME_PRICE_TYPE.action = "price_type.jsp";
        document.FRM_NAME_PRICE_TYPE.submit();
      } 
 
    </script>
  </head>
  <body>
    <section class="content-header">
      <h1><%=textListGlobal[SESS_LANGUAGE][0]%><small> <%=textListGlobal[SESS_LANGUAGE][1]%> </small> </h1>
      <ol class="breadcrumb">
        <li><%=textListGlobal[SESS_LANGUAGE][2]%></li>
      </ol>
    </section>
      <section  class="content">
      <div class="box box-primary">
        <div class="box-header with-border">
          <label><%=textListGlobal[SESS_LANGUAGE][0] %></label>
        </div>
        <form name="FRM_NAME_PRICE_TYPE" method ="post" action="">
      <input type="hidden" name="command" value="<%=iCommand %>">
      <input type="hidden" name="prev_command" value="<%=prevCommand%>">
      <input type="hidden" name="FRM_FIELD_PRICE_ID" value="<%=oidPriceSystem %>">
        <div class="box-body">
            <div class="col-md-12">
              <div class="col-md-6">
                  <div class="form-group">
                    <label class="col-sm-4"><%=textListHeader[SESS_LANGUAGE][1]%></label>
                    <div class="col-sm-8">
                      <input class="form-control" name="<%=frmPriceSystem.fieldNames[frmPriceSystem.FRM_FIELD_NAME] %>" value="<%=priceSystem.getName() %>">
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-4"><%=textListHeader[SESS_LANGUAGE][2]%></label>
                    <div class="col-sm-8">
                      <input class="form-control" name="<%=frmPriceSystem.fieldNames[frmPriceSystem.FRM_FIELD_INDEX] %>" value="<%=priceSystem.getIndex() %>">
                    </div>
                  </div>
              </div>
              <div class="col-md-6">
                  <div class="form-group">
                    <label class="col-sm-4"><%=textListHeader[SESS_LANGUAGE][3]%></label>
                    <div class="col-sm-8">
                      <textarea class="form-control" name="<%=frmPriceSystem.fieldNames[frmPriceSystem.FRM_FIELD_KETERANGAN] %>" ><%=priceSystem.getKeterangan() %></textarea>
                    </div>
                  </div>
              </div>
            </div>
        </div>
          </form>
        <div class="box-footer">
          <a class="btn btn-primary" href="javascript:cmdSave()">Save Formula</a>
        </div>
      </div>
      </section>
      <section  class="content">
      <div class="box box-primary">
        <div class="box-header with-border">
          <label><%=textListGlobal[SESS_LANGUAGE][3] %></label>
        </div>
        <div class="box-body">
          <div class="table-responsive">
            <table id="listStock" class="table table-striped table-bordered" style="width:100%">
              <thead class="headerList">
                <tr>
                  <th><%=textListHeader[SESS_LANGUAGE][0] %></th>
                  <th><%=textListHeader[SESS_LANGUAGE][1] %></th>
                  <th><%=textListHeader[SESS_LANGUAGE][2] %></th>
                  <th><%=textListHeader[SESS_LANGUAGE][3] %></th>
                </tr>
              </thead>
              <%
              Vector price = PstPriceSystem.listAll();
              for(int i = 0; i < price.size(); i++){
              PriceSystem ps = (PriceSystem)price.get(i);
              %>
              <tbody>
                <tr>
                  <td><%=i + 1 %></td>
                  <td><%=ps.getName() %></td>
                  <td><%=ps.getIndex() %></td>
                  <td><%=ps.getKeterangan() %></td>
                </tr>
              </tbody>
              <%}%>
            </table>
          </div>
        </div>
        <div class="box-footer">
          <a class="btn btn-primary" href="javascript:cmdGo()">Add Type Price</a>
        </div>
      </div>
      </section>
  </body>
</html>

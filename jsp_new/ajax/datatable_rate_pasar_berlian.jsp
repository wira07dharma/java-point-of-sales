<%@page import="com.dimata.posbo.entity.masterdata.RatePasarBerlian"%>
<%@page import="java.util.Vector"%>
<%@page import="com.dimata.util.Formater"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstRatePasarBerlian"%>
<%@page import="java.sql.SQLException"%>
<%@page import="com.dimata.qdep.db.DBException"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="org.json.JSONObject"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.common.entity.logger.PstLogSysHistory"%>
<%@page import="com.dimata.common.session.datatables.SessDataTables"%>
<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%
  
  String params         = request.getParameter("params");
  SessDataTables table  = new SessDataTables() {
    
    //Kolom untuk view.
    @Override 
    protected HashMap<String, Integer> columnIndex() {
      HashMap<String, Integer> m = new HashMap<String, Integer>();
      m.put("CODE", 0);
      m.put("NAME", 1);
      m.put("RATE", 2);
      m.put("UPDATEDATE", 3);
      m.put("DESCRIPTION", 4);
      m.put("ACTION", 5);
      return m;
    };
    
  };
  
  SessDataTables.Column updateDate = new SessDataTables.Column() {{
      this.setColName(PstRatePasarBerlian.fieldNames[PstRatePasarBerlian.FLD_UPDATEDATE]);
      this.setCustom(true);
    }
    @Override 
    protected String customAction(ResultSet rs, HashMap<Integer, String> data) throws DBException, SQLException {
      return Formater.formatDate(rs.getDate(this.getColName()), "dd-MM-yyyy");
    }
  };
  
  //Nama kolom untuk pencarian database.
  /* 00 */ table.addColumn(PstRatePasarBerlian.fieldNames[PstRatePasarBerlian.FLD_CODE]);
  /* 03 */ table.addColumn(PstRatePasarBerlian.fieldNames[PstRatePasarBerlian.FLD_NAME]);
  /* 01 */ table.addColumn(PstRatePasarBerlian.fieldNames[PstRatePasarBerlian.FLD_RATE]);
  /* 01 */ table.addColumnCustom(updateDate);
  /* 01 */ table.addColumn(PstRatePasarBerlian.fieldNames[PstRatePasarBerlian.FLD_DESCRIPTION]);
  /* 01 */ table.addColumn("status");
  table.setRequest(request);
  table.setResponse(response);
  table.setSql(PstRatePasarBerlian.getQueryAll());
  String json = table.generateJSON();
%><%=json%>
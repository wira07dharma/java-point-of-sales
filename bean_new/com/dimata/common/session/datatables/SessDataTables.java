/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.common.session.datatables;

import com.dimata.qdep.db.DBException;
import com.dimata.qdep.db.DBHandler;
import com.dimata.qdep.db.DBResultSet;
import com.dimata.qdep.db.I_DBType;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.util.Formater;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Regen
 */
public class SessDataTables {

  private ArrayList<Column> cols = new ArrayList<Column>();
  private HashMap<String, Integer> map = new HashMap<String, Integer>();
  private HttpServletRequest request    = null;
  private HttpServletResponse response  = null;
  private boolean generateNumber        = true;
  
  private String jsonResult   = "";
  private int start           = 0;
  private int amount          = 0;
  private int colOrder        = 0;
  private long totalRow       = 0;
  private String whereClause  = "";
  private String searchTerm   = "";
  private String dir          = "";
  private String additionalSQLColumn = "";
  
  private String query        = "";
  private String sql          = "";
  
  private Vector data = new Vector();
  
  public void addColumnCustom(Column c) { this.cols.add(c); }
  public void addColumnAction(String colName) { this.addColumn(colName, true); }
  public void addColumn(String colName, boolean action) { this.addColumn(colName, I_DBType.TYPE_STRING, "@", action); }
  public void addColumn(String colName) { this.addColumn(colName, I_DBType.TYPE_STRING); }
  public void addColumn(String colName, int dbtype) { this.addColumn(colName, dbtype, "@", false); }
  public void addColumn(String colName, String pattern) { this.addColumn(colName, I_DBType.TYPE_STRING, pattern, false); }
  public void addColumn(String colName, int dbtype, String pattern, boolean action) {
    Column c = new Column();
    c.setColName(colName);
    c.setPattern(pattern);
    c.setType(dbtype);
    c.setAction(action);
    this.cols.add(c);
  }

  public SessDataTables() {
    super();
    this.map.putAll(this.columnIndex());
  }
  
  
  protected HashMap<Integer, Column> columnMaping() {
    return null;
  }
  
  protected HashMap<String, Integer> columnIndex() {
    return null;
  }
  
  protected String action(String rsVal, HashMap<Integer, String> data) {
    return rsVal;
  }
  
  private void getRequestValues() {
    this.searchTerm = FRMQueryString.requestString(this.request, "sSearch");
    int sStart      = FRMQueryString.requestInt(this.request, "iDisplayStart");
    int sAmount     = FRMQueryString.requestInt(this.request, "iDisplayLength");
    int sCol        = FRMQueryString.requestInt(this.request, "iSortCol_0")-1;
    String sdir     = FRMQueryString.requestString(this.request, "sSortDir_0");
    this.amount     = (sAmount < 10) ? 10 : sAmount;
    this.start      = (sStart < 0) ? 0 : sStart;
    this.colOrder   = (sCol < 0) ? 0 : sCol;
    this.dir        = (sdir.equals("asc")) ? " ASC " : " DESC ";
    if(cols != null && this.cols.size() > 0) {
      this.whereClause = " ( FALSE ";
      for(Column col: cols) { this.whereClause += " OR " + this.getAlias(col.getColName()) + " LIKE '%" + searchTerm + "%' "; }
      this.whereClause += " ) ";
    }
  }
  
  private String generateQueryWithLimit() {
    this.query = this.generateQueryAll();
    System.out.println("Datatable Query : "+this.query);
    if(!this.query.equals("")) {
      this.query += " LIMIT " + this.start + ", " + this.amount;
    }
    return this.query;
  }
  
  private String generateQueryAll() {
    this.query = "";
    for(Column c: this.cols) {
      if(!this.query.equals("")) {this.query+=",";} else { this.query = "SELECT * FROM ( SELECT "; }
      this.query+=c.getColName();
    }
    this.query += (!getAdditionalSQLColumn().equals("") ? " , "+getAdditionalSQLColumn() : "");
    this.query += " FROM ( ";
    this.query += this.sql;
    this.query += " ) AS current ) AS `mytable` ";
    this.query += ((this.whereClause.equals("")) ? "" : " WHERE " + this.whereClause + " ");
    Column x = this.cols.get(colOrder);
    this.query += " ORDER BY " + this.getAlias(x.getColName()) + this.dir;
    
    return this.query;
  }
  
  private String getAlias(String name) {
    String n = name.toLowerCase(); 
    for(int i = n.length()-1; i>=0; i--) {
      if(n.charAt(i) == ' ' && (i-3) >= 0) {
        if(n.charAt(i-1) == 's' && n.charAt(i-2) == 'a' && n.charAt(i-3) == ' ') {
          return n.substring(i, n.length());
        }
      }
    }
    
    return n;
  }
  
  private String generateQueryCountAll() {
    String basicQuery = this.generateQueryAll();
    this.query  = " SELECT COUNT(0) AS `COUNT` FROM ("+basicQuery+") AS `table` ";
    return this.query;
  }
  
  private void execQuery() {
    DBResultSet dbrs = null;
    JSONObject o  = new JSONObject();
    JSONArray a   = new JSONArray();
    int row = 0;
    try {
      dbrs = DBHandler.execQueryResult(this.generateQueryCountAll());
      ResultSet rscount = dbrs.getResultSet();
      this.totalRow = 0;
      while (rscount.next()) {
        this.totalRow = rscount.getLong(1);
      }
      
      dbrs = DBHandler.execQueryResult(this.generateQueryWithLimit());
      ResultSet rs = dbrs.getResultSet();
      int number = this.start;
      while (rs.next()) {
        row++;
        HashMap<Integer, String> d = new HashMap<Integer, String>();
        JSONArray arr = new JSONArray();
        
        int col = 0;
        for(Column c: this.cols) { d.put(col, rs.getString(col+1)); col++; }
        this.data.add(d);
        
        col = 1;
        if(this.generateNumber) {
          number++;
          arr.put(number);
        }
        for(Column c: this.cols) {
          String p = "";
          if(c.isCustom()) {
            p = c.customAction(rs, d);
            arr.put(p);
          } else if(c.isAction()) {
            p = this.action(rs.getString(col), d);
            arr.put(p);
          } else if(!c.pattern.equals("")) {
            switch(c.getType()) {
              case I_DBType.TYPE_DATE :
                p = c.getPattern().replace("@", Formater.formatDate(Formater.formatDate(rs.getString(col), "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss"));
              default:
                p = (rs.getString(col) == null) ? "" : c.getPattern().replace("@", rs.getString(col));
            }
            arr.put(p);
          }
          col++;
        }
        a.put(arr);
      }
      rs.close();
    } catch (DBException e) {
      System.out.println(e);
    } catch (SQLException e) {
      System.out.println(e);
    } finally {
      DBResultSet.close(dbrs);
      try {
        o.put("iTotalRecords", this.totalRow);
        o.put("iTotalDisplayRecords", this.totalRow);
        o.put("aaData", a);
      } catch (JSONException ex) {
        Logger.getLogger(SessDataTables.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    
    this.jsonResult = o.toString();
    
  }
  
  /**
   * 
   * Public methods.
   */
  
  public String generateJSON(HttpServletRequest request, HttpServletResponse response, String query) {
    this.request  = request;
    this.response = response;
    this.sql = query;
    return this.generateJSON();
  }
  
  public String generateJSON() {
    this.getRequestValues();
    this.execQuery();
    
    return this.jsonResult;
  }

  /**
   * @return the colOrder
   */
  public int getColOrder() {
    return colOrder;
  }

  /**
   * @param colOrder the colOrder to set
   */
  public void setColOrder(int colOrder) {
    this.colOrder = colOrder;
  }

  /**
   * @return the dir
   */
  public String getDir() {
    return dir;
  }

  /**
   * @param dir the dir to set
   */
  public void setDir(String dir) {
    this.dir = dir;
  }

  /**
   * @return the start
   */
  public int getStart() {
    return start;
  }

  /**
   * @param start the start to set
   */
  public void setStart(int start) {
    this.start = start;
  }

  /**
   * @return the amount
   */
  public int getAmount() {
    return amount;
  }

  /**
   * @param amount the amount to set
   */
  public void setAmount(int amount) {
    this.amount = amount;
  }

  /**
   * @return the request
   */
  public HttpServletRequest getRequest() {
    return request;
  }

  /**
   * @param request the request to set
   */
  public void setRequest(HttpServletRequest request) {
    this.request = request;
  }

  /**
   * @return the response
   */
  public HttpServletResponse getResponse() {
    return response;
  }

  /**
   * @param response the response to set
   */
  public void setResponse(HttpServletResponse response) {
    this.response = response;
  }

  /**
   * @return the searchTerm
   */
  public String getSearchTerm() {
    return searchTerm;
  }

  /**
   * @param searchTerm the searchTerm to set
   */
  public void setSearchTerm(String searchTerm) {
    this.searchTerm = searchTerm;
  }

  /**
   * @return the jsonResult
   */
  public String getJsonResult() {
    return jsonResult;
  }

  /**
   * @param jsonResult the jsonResult to set
   */
  public void setJsonResult(String jsonResult) {
    this.jsonResult = jsonResult;
  }

  /**
   * @return the totalRow
   */
  public long getTotalRow() {
    return totalRow;
  }

  /**
   * @param totalRow the totalRow to set
   */
  public void setTotalRow(long totalRow) {
    this.totalRow = totalRow;
  }

  /**
   * @return the generateNumber
   */
  public boolean isGenerateNumber() {
    return generateNumber;
  }

  /**
   * @param generateNumber the generateNumber to set
   */
  public void setGenerateNumber(boolean generateNumber) {
    this.generateNumber = generateNumber;
  }
  /**
   * @return the columIndex
   */
  public int getColumIndex(String key) {
    return this.map.get(key);
  }

  /**
   * @return the sql
   */
  public String getSql() {
    return sql;
  }

  /**
   * @param sql the sql to set
   */
  public void setSql(String sql) {
    this.sql = sql;
  }

  /**
   * @return the additionalSQLColumn
   */
  public String getAdditionalSQLColumn() {
    return additionalSQLColumn;
  }

  /**
   * @param additionalSQLColumn the additionalSQLColumn to set
   */
  public void setAdditionalSQLColumn(String additionalSQLColumn) {
    this.additionalSQLColumn = additionalSQLColumn;
  }

  public static class Column {
    protected String customAction(ResultSet rs, HashMap<Integer, String> data) throws DBException, SQLException {
      return "";
    }
    
    private String colName = "";
    private String pattern = "";
    private int type = 0;
    private boolean action = false;
    private boolean custom = false;

    /**
     * @return the colName
     */
    public String getColName() {
      return colName;
    }

    /**
     * @param colName the colName to set
     */
    public void setColName(String colName) {
      this.colName = colName;
    }

    /**
     * @return the pattern
     */
    public String getPattern() {
      return pattern;
    }

    /**
     * @param pattern the pattern to set
     */
    public void setPattern(String pattern) {
      this.pattern = pattern;
    }

    /**
     * @return the type
     */
    public int getType() {
      return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(int type) {
      this.type = type;
    }

    /**
     * @return the action
     */
    public boolean isAction() {
      return action;
    }

    /**
     * @param action the action to set
     */
    public void setAction(boolean action) {
      this.action = action;
    }

    /**
     * @return the custom
     */
    public boolean isCustom() {
      return custom;
    }

    /**
     * @param custom the custom to set
     */
    public void setCustom(boolean custom) {
      this.custom = custom;
    }
  }
  
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.common.session.customreport;

import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.Vector;

/**
 *
 * @author Dimata 007
 */
public class SessCustomReport {
     public static Vector listCustome(String query, String whereClause, String order) {
        Vector lists = new Vector();
        Vector listsValue = new Vector();
        DBResultSet dbrs = null;
        Vector<String> columnNames = new Vector<String>();
        
        try {
            String sql = query;
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
             if (rs != null) {
                 
                    ResultSetMetaData columns = rs.getMetaData();
                    int i = 0;
                    
                    while (i < columns.getColumnCount()) {
                      i++;
                      columnNames.add(""+columns.getColumnName(i));
                    }
                    lists.add(columnNames);
                    while (rs.next()) {
                        Vector<String> columnValue = new Vector<String>();
                        for (i = 1; i <= columnNames.size(); i++) {
                           int type = columns.getColumnType(i); 
                           int k = i-1;
                           switch (type) {
                                  case Types.INTEGER:
                                      columnValue.add(""+rs.getInt(columnNames.get(k)));
                                  break;

                                  case  Types.VARCHAR:
                                      columnValue.add(""+rs.getString(columnNames.get(k)));
                                  break;
                                      
                                  case  Types.LONGNVARCHAR:
                                      columnValue.add(""+rs.getString(columnNames.get(k)));
                                  break;    
                                      
                                  case  Types.LONGVARCHAR:
                                        columnValue.add(""+rs.getString(columnNames.get(k)));
                                  break; 
                                     
                                  case Types.DATE:
                                      columnValue.add(""+rs.getDate(columnNames.get(k)));
                                  break;
                                      
                                  case Types.TIMESTAMP:
                                      columnValue.add(""+rs.getDate(columnNames.get(k)));
                                  break;
                                  
                                  case Types.TIME:
                                      columnValue.add(""+rs.getDate(columnNames.get(k)));
                                  break;        

                                  case Types.BIGINT:
                                      columnValue.add(""+rs.getLong(columnNames.get(k)));
                                  break;  
                                      
                                  case Types.DOUBLE:
                                      columnValue.add(""+rs.getDouble(columnNames.get(k)));
                                  break;
                           }
                        }
                        listsValue.add(columnValue);
                    }
                    lists.add(listsValue);
                  }
             
            rs.close();
            return lists;
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }
}

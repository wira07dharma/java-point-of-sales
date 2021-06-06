/*
 * SessActivity.java
 *
 * Created on February 22, 2007, 2:32 PM
 */

package com.dimata.aiso.session.masterdata;

import java.sql.*;
import java.util.*;

import com.dimata.aiso.db.*;

import com.dimata.aiso.entity.masterdata.*;
import com.dimata.aiso.entity.periode.*;
import com.dimata.aiso.entity.search.SrcActivity;
import com.dimata.aiso.form.masterdata.FrmActivity;
import com.dimata.aiso.form.search.FrmSrcActivity;
import com.dimata.aiso.session.periode.*;

/**
 *
 * @author  dwi
 */
public class SessActivity {
    
  public static final String SESS_ACTIVITY = "SESS_ACTIVITY";
  
  public static Vector listActivityAccount(int start, int recordToGet, String strWhere, String strOrder){
    DBResultSet dbrs = null;
    Vector result = new Vector(1,1);
    try{
        String sql = "";
            sql = "SELECT ACT.*"+
                ", ACC."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA]+
                ", ACC."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH]+
                ", ACC."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]+
                " FROM "+PstActivity.TBL_AISO_ACTIVITY+" AS ACT"+
                " INNER JOIN "+PstActivityAccountLink.TBL_AISO_ACTIVITY_ACCOUNT+" AS ACL"+
                " ON ACT."+PstActivity.fieldNames[PstActivity.FLD_ACTIVITY_ID]+" = "+
                " ACL."+PstActivityAccountLink.fieldNames[PstActivityAccountLink.FLD_ACTIVITY_ID]+
                " INNER JOIN "+PstPerkiraan.TBL_PERKIRAAN+" AS ACC"+
                " ON ACL."+PstActivityAccountLink.fieldNames[PstActivityAccountLink.FLD_ACCOUNT_ID]+
                " = ACC."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]+
                " WHERE ACL."+ PstActivityAccountLink.fieldNames[PstActivityAccountLink.FLD_ACCOUNT_ID]+" > 0";
            
            if(strWhere != null && strWhere.length() > 0)
                sql = sql +" AND "+strWhere;
            else
                sql = sql + "";
            
            if(strOrder != null && strOrder.length() > 0)
                sql = sql +" ORDER BY "+strOrder;
            else
                sql = sql + "";

            switch(DBHandler.DBSVR_TYPE){
                case DBHandler.DBSVR_MYSQL:
                    if(start == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + "LIMIT" +start+","+start;               
                    break;

                case DBHandler.DBSVR_POSTGRESQL:
                    if(start == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + recordToGet+ " OFFSET " +start;                
                    break;

                case DBHandler.DBSVR_MSSQL:
                    break;

                case DBHandler.DBSVR_ORACLE:
                    break;

                case DBHandler.DBSVR_SYBASE:
                    break; 
                    
              }
            
               System.out.println("sql list account activity ====> "+sql);
               
               dbrs = DBHandler.execQueryResult(sql); 
               ResultSet rs = dbrs.getResultSet();
               
               while(rs.next()){
                   Vector vTemp = new Vector();
                    Activity objActivity = new Activity();
                    objActivity.setOID(rs.getLong(PstActivity.fieldNames[PstActivity.FLD_ACTIVITY_ID]));                    
                    objActivity.setActLevel(rs.getInt(PstActivity.fieldNames[PstActivity.FLD_ACT_LEVEL]));
                    objActivity.setAssumpAndRisk(rs.getString(PstActivity.fieldNames[PstActivity.FLD_ASSUMP_AND_RISK]));
                    objActivity.setCode(rs.getString(PstActivity.fieldNames[PstActivity.FLD_CODE]));
                    objActivity.setCostImpl(rs.getString(PstActivity.fieldNames[PstActivity.FLD_COST_IMPL]));
                    objActivity.setDescription(rs.getString(PstActivity.fieldNames[PstActivity.FLD_DESCRIPTION]));
                    objActivity.setIdParent(rs.getLong(PstActivity.fieldNames[PstActivity.FLD_PARENT_ID]));
                    objActivity.setOutPutandDelv(rs.getString(PstActivity.fieldNames[PstActivity.FLD_OUTPUT_AND_DELV]));
                    objActivity.setPerfmIndict(rs.getString(PstActivity.fieldNames[PstActivity.FLD_PERFM_INDICT]));
                    objActivity.setPosted(rs.getInt(PstActivity.fieldNames[PstActivity.FLD_POSTED]));
                    objActivity.setType(rs.getInt(PstActivity.fieldNames[PstActivity.FLD_ACT_TYPE]));
                    vTemp.add(objActivity);
                    
                    Perkiraan objPerkiraan = new Perkiraan();
                    objPerkiraan.setOID(rs.getLong(PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]));
                    objPerkiraan.setNama(rs.getString(PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA]));
                    objPerkiraan.setAccountNameEnglish(rs.getString(PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH]));
                    vTemp.add(objPerkiraan);
                    
                    result.add(vTemp);
               }
           
    }catch(Exception e){
        System.out.println("Exception pada Sess Activity List Activity Account "+e.toString());
    }finally{
        DBResultSet.close(dbrs);
    }
    return result;
  }
  
  public static String getIDActivity(){
      String sql = "";
        try{
            sql = "SELECT ACT."+PstActivity.fieldNames[PstActivity.FLD_ACTIVITY_ID]+
                " FROM "+PstActivity.TBL_AISO_ACTIVITY+" AS ACT"+
                " INNER JOIN "+PstActivityAccountLink.TBL_AISO_ACTIVITY_ACCOUNT+" AS ACL"+
                " ON ACT."+PstActivity.fieldNames[PstActivity.FLD_ACTIVITY_ID]+" = "+
                " ACL."+PstActivityAccountLink.fieldNames[PstActivityAccountLink.FLD_ACTIVITY_ID]+
                " INNER JOIN "+PstPerkiraan.TBL_PERKIRAAN+" AS ACC"+
                " ON ACL."+PstActivityAccountLink.fieldNames[PstActivityAccountLink.FLD_ACCOUNT_ID]+
                " = ACC."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]; 
            //System.out.println("SQL getIDActivity ::::: "+sql);
        }catch(Exception e){}
        return sql;
  }
  
   public static Vector listAllActivity(SrcActivity srcActivity, int start, int recordToGet){
    DBResultSet dbrs = null;
    Vector result = new Vector(1,1);
    try{
        String sql = "";
            sql = "SELECT * FROM "+PstActivity.TBL_AISO_ACTIVITY;                
                
            String codeParameter = "";            
            if(srcActivity.getCode() != null && srcActivity.getCode().length() > 0)
                codeParameter = PstActivity.fieldNames[PstActivity.FLD_CODE]+" LIKE '%"+srcActivity.getCode()+"%'";
            
            String descriptionParameter = "";
            if(srcActivity.getDescription() != null && srcActivity.getDescription().length() > 0)
                descriptionParameter = PstActivity.fieldNames[PstActivity.FLD_DESCRIPTION]+" LIKE '%"+srcActivity.getDescription()+"%'";
            
            String levelParameter = "";
            if(srcActivity.getActLevel() > 0)
                levelParameter = PstActivity.fieldNames[PstActivity.FLD_ACT_LEVEL]+" = "+(srcActivity.getActLevel() - 1);
            
            String postedParameter = "";
            if(srcActivity.getPosted() > 0)
                postedParameter = PstActivity.fieldNames[PstActivity.FLD_POSTED]+" = "+(srcActivity.getPosted() - 1);
            
            String typeParameter = "";
            if(srcActivity.getActType() > 0)
                typeParameter = PstActivity.fieldNames[PstActivity.FLD_ACT_TYPE]+" = "+(srcActivity.getActType() - 1);
            
            String orderByParameter = "";
            switch(srcActivity.getOrderBy()){
                case FrmSrcActivity.SHORTBY_CODE:
                    orderByParameter = " ORDER BY "+FrmSrcActivity.shortFieldKey[FrmSrcActivity.SHORTBY_CODE];
                    break;
                    
                case FrmSrcActivity.SHORTBY_LEVEL:
                    orderByParameter = " ORDER BY "+FrmSrcActivity.shortFieldKey[FrmSrcActivity.SHORTBY_LEVEL];
                    break;
                    
                case FrmSrcActivity.SHORTBY_POSTED:
                    orderByParameter = " ORDER BY "+FrmSrcActivity.shortFieldKey[FrmSrcActivity.SHORTBY_POSTED];
                    break;
                    
                case FrmSrcActivity.SHORTBY_TYPE:
                    orderByParameter = " ORDER BY "+FrmSrcActivity.shortFieldKey[FrmSrcActivity.SHORTBY_TYPE];
            }
            
            
            String allParameter = "";
            if(codeParameter != null && codeParameter.length() > 0)
                allParameter = codeParameter;
           
            if(descriptionParameter != null && descriptionParameter.length() > 0)
                if(allParameter != null && allParameter.length() > 0)
                    allParameter = allParameter + " AND "+descriptionParameter;
                else
                    allParameter = descriptionParameter;
            
            if(levelParameter != null && levelParameter.length() > 0)
                if(allParameter != null && allParameter.length() > 0)
                    allParameter = allParameter + " AND "+levelParameter;
                else
                    allParameter = levelParameter;
            
            if(postedParameter != null && postedParameter.length() > 0)
                if(allParameter != null && allParameter.length() > 0)
                    allParameter = allParameter +" AND "+postedParameter;
                else
                    allParameter = postedParameter;
            
            if(typeParameter != null && typeParameter.length() > 0)
                if(allParameter != null && allParameter.length() > 0)
                    allParameter = allParameter +" AND "+typeParameter;
                else
                    allParameter = typeParameter;            
            
            
            if(allParameter != null && allParameter.length() > 0)
                sql = sql +" WHERE "+allParameter+orderByParameter;
            else
                sql = sql+" "+orderByParameter;
                
            switch(DBHandler.DBSVR_TYPE){
                case DBHandler.DBSVR_MYSQL:
                    if(start == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + "LIMIT" +start+","+start;               
                    break;

                case DBHandler.DBSVR_POSTGRESQL:
                    if(start == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + recordToGet+ " OFFSET " +start;                
                    break;

                case DBHandler.DBSVR_MSSQL:
                    break;

                case DBHandler.DBSVR_ORACLE:
                    break;

                case DBHandler.DBSVR_SYBASE:
                    break; 
                    
              }
            
               System.out.println("sql list All Activity ====> "+sql);
               
               dbrs = DBHandler.execQueryResult(sql); 
               ResultSet rs = dbrs.getResultSet();
               
               while(rs.next()){                   
                    Activity objActivity = new Activity();
                    objActivity.setOID(rs.getLong(PstActivity.fieldNames[PstActivity.FLD_ACTIVITY_ID]));                    
                    objActivity.setActLevel(rs.getInt(PstActivity.fieldNames[PstActivity.FLD_ACT_LEVEL]));
                    objActivity.setAssumpAndRisk(rs.getString(PstActivity.fieldNames[PstActivity.FLD_ASSUMP_AND_RISK]));
                    objActivity.setCode(rs.getString(PstActivity.fieldNames[PstActivity.FLD_CODE]));
                    objActivity.setCostImpl(rs.getString(PstActivity.fieldNames[PstActivity.FLD_COST_IMPL]));
                    objActivity.setDescription(rs.getString(PstActivity.fieldNames[PstActivity.FLD_DESCRIPTION]));
                    objActivity.setIdParent(rs.getLong(PstActivity.fieldNames[PstActivity.FLD_PARENT_ID]));
                    objActivity.setOutPutandDelv(rs.getString(PstActivity.fieldNames[PstActivity.FLD_OUTPUT_AND_DELV]));
                    objActivity.setPerfmIndict(rs.getString(PstActivity.fieldNames[PstActivity.FLD_PERFM_INDICT]));
                    objActivity.setPosted(rs.getInt(PstActivity.fieldNames[PstActivity.FLD_POSTED]));
                    objActivity.setType(rs.getInt(PstActivity.fieldNames[PstActivity.FLD_ACT_TYPE]));                   
                    
                    result.add(objActivity);
               }
           
    }catch(Exception e){
        System.out.println("Exception pada Sess Activity List All Activity "+e.toString());
    }finally{
        DBResultSet.close(dbrs);
    }
    return result;
  }
   
    public static Vector listActivity(SrcActivity srcActivity, int start, int recordToGet){
    DBResultSet dbrs = null;
    Vector result = new Vector(1,1);
    try{
        String sql = "";
            sql = "SELECT ACT.*"+
                ", ACC."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA]+
                ", ACC."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH]+
                ", ACC."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]+
                " FROM "+PstActivity.TBL_AISO_ACTIVITY+" AS ACT"+
                " INNER JOIN "+PstActivityAccountLink.TBL_AISO_ACTIVITY_ACCOUNT+" AS ACL"+
                " ON ACT."+PstActivity.fieldNames[PstActivity.FLD_ACTIVITY_ID]+" = "+
                " ACL."+PstActivityAccountLink.fieldNames[PstActivityAccountLink.FLD_ACTIVITY_ID]+
                " INNER JOIN "+PstPerkiraan.TBL_PERKIRAAN+" AS ACC"+
                " ON ACL."+PstActivityAccountLink.fieldNames[PstActivityAccountLink.FLD_ACCOUNT_ID]+
                " = ACC."+PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN];               
                
            String codeParameter = "";            
            if(srcActivity.getCode() != null && srcActivity.getCode().length() > 0)
                codeParameter = " ACT."+PstActivity.fieldNames[PstActivity.FLD_CODE]+" = '"+srcActivity.getCode()+"'";
            
            String descriptionParameter = "";
            if(srcActivity.getDescription() != null && srcActivity.getDescription().length() > 0)
                descriptionParameter = " ACT."+PstActivity.fieldNames[PstActivity.FLD_DESCRIPTION]+" LIKE '%"+srcActivity.getDescription()+"%'";
          
          
            String orderByParameter = " ORDER BY "+PstActivity.fieldNames[PstActivity.FLD_CODE];
           
            
            
            String allParameter = "";
            if(codeParameter != null && codeParameter.length() > 0)
                allParameter = codeParameter;
           
            if(descriptionParameter != null && descriptionParameter.length() > 0)
                if(allParameter != null && allParameter.length() > 0)
                    allParameter = allParameter + " AND "+descriptionParameter;
                else
                    allParameter = descriptionParameter;           
                
            if(allParameter != null && allParameter.length() > 0)
                sql = sql +" WHERE "+allParameter+orderByParameter;
            else
                sql = sql+" "+orderByParameter;
                
            switch(DBHandler.DBSVR_TYPE){
                case DBHandler.DBSVR_MYSQL:
                    if(start == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + "LIMIT" +start+","+start;               
                    break;

                case DBHandler.DBSVR_POSTGRESQL:
                    if(start == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + recordToGet+ " OFFSET " +start;                
                    break;

                case DBHandler.DBSVR_MSSQL:
                    break;

                case DBHandler.DBSVR_ORACLE:
                    break;

                case DBHandler.DBSVR_SYBASE:
                    break; 
                    
              }
            
               System.out.println("sql list Activity ====> "+sql);
               
               dbrs = DBHandler.execQueryResult(sql); 
               ResultSet rs = dbrs.getResultSet();
               
               while(rs.next()){
                   Vector vTemp = new Vector();
                    Activity objActivity = new Activity();
                    objActivity.setOID(rs.getLong(PstActivity.fieldNames[PstActivity.FLD_ACTIVITY_ID]));                    
                    objActivity.setActLevel(rs.getInt(PstActivity.fieldNames[PstActivity.FLD_ACT_LEVEL]));
                    objActivity.setAssumpAndRisk(rs.getString(PstActivity.fieldNames[PstActivity.FLD_ASSUMP_AND_RISK]));
                    objActivity.setCode(rs.getString(PstActivity.fieldNames[PstActivity.FLD_CODE]));
                    objActivity.setCostImpl(rs.getString(PstActivity.fieldNames[PstActivity.FLD_COST_IMPL]));
                    objActivity.setDescription(rs.getString(PstActivity.fieldNames[PstActivity.FLD_DESCRIPTION]));
                    objActivity.setIdParent(rs.getLong(PstActivity.fieldNames[PstActivity.FLD_PARENT_ID]));
                    objActivity.setOutPutandDelv(rs.getString(PstActivity.fieldNames[PstActivity.FLD_OUTPUT_AND_DELV]));
                    objActivity.setPerfmIndict(rs.getString(PstActivity.fieldNames[PstActivity.FLD_PERFM_INDICT]));
                    objActivity.setPosted(rs.getInt(PstActivity.fieldNames[PstActivity.FLD_POSTED]));
                    objActivity.setType(rs.getInt(PstActivity.fieldNames[PstActivity.FLD_ACT_TYPE]));
                    vTemp.add(objActivity);
                    
                    Perkiraan objPerkiraan = new Perkiraan();
                    objPerkiraan.setOID(rs.getLong(PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]));
                    objPerkiraan.setNama(rs.getString(PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA]));
                    objPerkiraan.setAccountNameEnglish(rs.getString(PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH]));
                    vTemp.add(objPerkiraan);
                    
                    result.add(vTemp);
               }
           
    }catch(Exception e){
        System.out.println("Exception pada Sess Activity List Activity "+e.toString());
    }finally{
        DBResultSet.close(dbrs);
    }
    return result;
  }
   
   public static void main(String[] arg){
        SessActivity sessActivity = new SessActivity();
        SrcActivity srcActivity = new SrcActivity();
        Vector result = new Vector();
        result = sessActivity.listActivity(srcActivity,0,30);
        System.out.println("result.size() ====> "+result.size());
   }
}

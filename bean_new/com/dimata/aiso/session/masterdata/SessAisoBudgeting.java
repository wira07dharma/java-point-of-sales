/*
 * SessAisoBudgeting.java
 * @author  rusdianta
 * Created on March 15, 2005, 10:04 AM
 */

package com.dimata.aiso.session.masterdata;

import java.sql.*;
import java.util.*;

import com.dimata.aiso.db.*;

import com.dimata.aiso.entity.masterdata.*;
import com.dimata.aiso.entity.periode.*;
import com.dimata.aiso.session.periode.*;

public class SessAisoBudgeting 
{         
    
    /**
     * @param lAccOid
     * @return
     */    
    public static AisoBudgeting getCurrentAisoBudgeting(long lAccOid) 
    {
        AisoBudgeting objBudgeting = new AisoBudgeting();
        try 
        {
            long lCurrPerdOid = PstPeriode.getCurrPeriodId();
            objBudgeting = PstAisoBudgeting.getAisoBudgeting(lAccOid, lCurrPerdOid);            
        }
        catch (Exception error) 
        {
            System.out.println(".:: SessAisoBudgeting >> getCurrentAisoBudgeting() : " + error.toString());
        }
        return objBudgeting;
    }
    
    /**
     * rusdianta
     *  
     * @param lAccOid >> account number
     * @return Vector of object AisoBudgeting
     */    
    public static Vector getBudgetingHistory(long lAccOid) 
    {
        Vector results = new Vector();
        DBResultSet dbrs = null;
        try 
        {
            String sql = "SELECT BUD." + PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_BUDGETING_OID]
                       + ", BUD." + PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_PERIODE_ID]
                       + ", BUD." + PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_BUDGET]                       
                       + " FROM " + PstAisoBudgeting.TBL_AISO_BUDGETING
                       + " INNER JOIN " + PstPeriode.TBL_PERIODE
                       + " AS PERD ON BUD." + PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_PERIODE_ID]
                       + " = PERD." + PstPeriode.fieldNames[PstPeriode.FLD_IDPERIODE]
                       + " WHERE " + PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_ID_PERKIRAAN]
                       + " = " + lAccOid
                       + " ORDER BY " + PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_PERIODE_ID];
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();            
            while (rs.next()) 
            {
                AisoBudgeting objBudgeting = new AisoBudgeting();
                objBudgeting.setOID(rs.getLong(PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_BUDGETING_OID]));
                objBudgeting.setBudgetingOid(rs.getLong(PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_BUDGETING_OID]));
                objBudgeting.setIdPerkiraan(rs.getLong(PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_ID_PERKIRAAN]));
                objBudgeting.setPeriodeId(rs.getLong(PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_PERIODE_ID]));
                objBudgeting.setBudget(rs.getDouble(PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_BUDGET]));                
                results.add(objBudgeting);
            }                                    
        }
        catch (Exception error) 
        {
            System.out.println(".:: " + new SessAisoBudgeting().getClass().getName() + ".getBudgetingHistory() : " + error.toString());
        }
        finally 
        {
            DBResultSet.close(dbrs);
        }
        return results;
    }
    
    
    /**
     * @param lAccOid
     * @param iStart
     * @param iRecToGet
     * @return
     */    
    public static Vector getBudgetingHistory(long lAccOid, int iStart, int iRecToGet)
    {
        Vector results = new Vector();
        DBResultSet dbrs = null;
        try 
        {
            String sql = "SELECT PERD." + PstPeriode.fieldNames[PstPeriode.FLD_NAMA]
                       + ", BUD." + PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_BUDGET]                       
                       + " FROM " + PstAisoBudgeting.TBL_AISO_BUDGETING
                       + " AS BUD INNER JOIN " + PstPeriode.TBL_PERIODE
                       + " AS PERD ON BUD." + PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_PERIODE_ID]
                       + " = PERD." + PstPeriode.fieldNames[PstPeriode.FLD_IDPERIODE]
                       + " WHERE BUD." + PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_ID_PERKIRAAN]
                       + " = " + lAccOid
                       + " ORDER BY BUD." + PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_PERIODE_ID];
        
            switch (DBHandler.DBSVR_TYPE) 
            {
                case DBHandler.DBSVR_MYSQL :
                    if(iStart == 0 && iRecToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + iStart + ","+ iRecToGet ;
                    break;
                    
                case DBHandler.DBSVR_POSTGRESQL :
                    if(iStart == 0 && iRecToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " +iRecToGet + " OFFSET "+ iStart ;
                    
                    break;
                    
                case DBHandler.DBSVR_SYBASE :
                    break;
                    
                case DBHandler.DBSVR_ORACLE :
                    break;
                    
                case DBHandler.DBSVR_MSSQL :
                    break;
                    
                default:
                    break;
            }
            
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();            
            while (rs.next()) 
            {
                Budgeting objBudgeting = new Budgeting();   
                objBudgeting.setPeriodName(rs.getString(PstPeriode.fieldNames[PstPeriode.FLD_NAMA]));
                objBudgeting.setBudget(rs.getDouble(PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_BUDGET]));                
                results.add(objBudgeting);
            }                        
        }
        catch (Exception error) 
        {
            System.out.println(".:: " + new SessAisoBudgeting().getClass().getName() + ".getBudgetingHistory() : " + error.toString());
        }
        finally 
        {
            DBResultSet.close(dbrs);
        }
        return results;
    }
    
    
    /**
     * @param lAccOid
     * @return
     */    
    public static int getBudgetingHistoryCount(long lAccOid) 
    {
        int iCount = 0;
        DBResultSet dbrs = null;
        try 
        {
            String sql = "SELECT COUNT(BUD." + PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_BUDGETING_OID]
                       + ") AS BUDGET_COUNT FROM " + PstAisoBudgeting.TBL_AISO_BUDGETING
                       + " AS BUD INNER JOIN " + PstPeriode.TBL_PERIODE
                       + " AS PERD ON BUD." + PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_PERIODE_ID]
                       + " = PERD." + PstPeriode.fieldNames[PstPeriode.FLD_IDPERIODE]
                       + " WHERE BUD." + PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_ID_PERKIRAAN]
                       + " = " + lAccOid;
        
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();            
            if (rs.next()) 
            {
                iCount = rs.getInt("BUDGET_COUNT");
            }                                   
        }
        catch (Exception error) 
        {
            System.out.println(".:: " + new SessAisoBudgeting().getClass().getName() + ".getBudgetingHistoryCount() : " + error.toString());
        }
        finally 
        {
            DBResultSet.close(dbrs);
        }
        return iCount;
    }
}

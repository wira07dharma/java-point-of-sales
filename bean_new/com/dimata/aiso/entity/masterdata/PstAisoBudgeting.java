/*
 * PstAisoBudgeting.java
 * @author  rusdianta
 * Created on February 28, 2005, 11:28 AM
 */

package com.dimata.aiso.entity.masterdata;

import java.util.*;
import java.sql.*;

import com.dimata.aiso.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.lang.I_Language;

public class PstAisoBudgeting extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language{
   
    public static final String TBL_AISO_BUDGETING = "aiso_budgeting";
    
    public static final int FLD_BUDGETING_OID = 0;
    public static final int FLD_ID_PERKIRAAN = 1;
    public static final int FLD_PERIODE_ID = 2;
    public static final int FLD_BUDGET = 3;    
    
    public static String fieldNames[] = {
        "BUDGETING_OID",
        "ID_PERKIRAAN",
        "PERIODE_ID",        
        "BUDGET"
    };
    
    public static int fieldTypes[] = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,        
        TYPE_FLOAT
    };
    
    /** Creates a new instance of PstAisoBudgeting */
    public PstAisoBudgeting() {
    }
    
    public PstAisoBudgeting(int i) throws DBException {
        super(new PstAisoBudgeting());        
    }
    
    public PstAisoBudgeting(String soid) throws DBException {
        super(new PstAisoBudgeting(0));
        if (!locate(soid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public PstAisoBudgeting(long loid) throws DBException {
        super(new PstAisoBudgeting(0));
        String soid = "0";
        try {
            soid = String.valueOf(loid);
        } catch (Exception error) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        
        if (!locate(soid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public String[] getFieldNames() {
        return fieldNames;
    }
    
    public int getFieldSize() {
        return fieldNames.length;
    }
    
    public int[] getFieldTypes() {
        return fieldTypes;
    }
    
    public String getPersistentName() {
        return new PstAisoBudgeting().getClass().getName();
    }
    
    public String getTableName() {
        return TBL_AISO_BUDGETING;
    }
    
    public long deleteExc(long loid) throws DBException {
        try {
            PstAisoBudgeting pab = new PstAisoBudgeting(loid);
            pab.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception error) {
            throw new DBException(new PstAisoBudgeting(0), DBException.UNKNOWN);
        }
        return loid;
    }
    
    public static AisoBudgeting fetchExc(long loid) throws DBException {
        try {
            AisoBudgeting aisoBudg = new AisoBudgeting();
            PstAisoBudgeting pab = new PstAisoBudgeting(loid);
            aisoBudg.setOID(loid);
            aisoBudg.setIdPerkiraan(pab.getlong(FLD_ID_PERKIRAAN));
            aisoBudg.setPeriodeId(pab.getlong(FLD_PERIODE_ID));
            aisoBudg.setBudget(pab.getdouble(FLD_BUDGET));            
            return aisoBudg;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception error) {
            throw new DBException(new PstAisoBudgeting(0), DBException.UNKNOWN);
        }
    }
    
    
    public static long insertExc(AisoBudgeting aisoBudg) throws DBException {
        try {
            PstAisoBudgeting pab = new PstAisoBudgeting(0);
            pab.setLong(FLD_ID_PERKIRAAN, aisoBudg.getIdPerkiraan());
            pab.setLong(FLD_PERIODE_ID, aisoBudg.getPeriodeId());
            pab.setDouble(FLD_BUDGET, aisoBudg.getBudget());            
            pab.insert();
            aisoBudg.setOID(pab.getlong(FLD_BUDGETING_OID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception error) {
            throw new DBException(new PstAisoBudgeting(0), DBException.UNKNOWN);
        }
        return aisoBudg.getOID();
    }
    
    public static long updateExc(AisoBudgeting aisoBudg) throws DBException {
        try {
            if (aisoBudg.getOID() != 0) {
                PstAisoBudgeting pab = new PstAisoBudgeting(aisoBudg.getOID());
                pab.setLong(FLD_ID_PERKIRAAN, aisoBudg.getIdPerkiraan());
                pab.setLong(FLD_PERIODE_ID, aisoBudg.getPeriodeId());
                pab.setDouble(FLD_BUDGET, aisoBudg.getBudget());                
                pab.update();
                return aisoBudg.getOID();                
            }            
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstAisoBudgeting(0), DBException.UNKNOWN);
        }
        return 0;
    }
    
    public long deleteExc(Entity ent) throws Exception {
        if (ent == null)
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        return deleteExc(ent.getOID()); 
    }
    
    public long fetchExc(Entity ent) throws Exception {
        AisoBudgeting aisoBudg = PstAisoBudgeting.fetchExc(ent.getOID());
        ent = (Entity) aisoBudg;
        return aisoBudg.getOID();
    }
    
    public long insertExc(Entity ent) throws Exception {
        return PstAisoBudgeting.insertExc((AisoBudgeting) ent);
    }
    
    public long updateExc(Entity ent) throws Exception {
        return updateExc((AisoBudgeting) ent);
    }    
    
    public static Vector list(int iStart,
                              int iRecToGet,
                              String strWhere,
                              String strOrder)
    {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT BUD." + PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_BUDGETING_OID]
                       + ", BUD." + PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_ID_PERKIRAAN]
                       + ", BUD." + PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_PERIODE_ID]
                       + ", BUD." + PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_BUDGET]                       
                       + " FROM " + PstAisoBudgeting.TBL_AISO_BUDGETING
                       + " AS BUD";
            if (strWhere.length() > 0)
                sql += " WHERE " + strWhere;
            if (strOrder.length() > 0)
                sql += " ORDER BY " + strOrder;
            
            switch (DBHandler.DBSVR_TYPE) {
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
            
            while (rs.next()) {
                AisoBudgeting objBudgeting = new AisoBudgeting();
                objBudgeting.setOID(rs.getLong(PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_BUDGETING_OID]));
                objBudgeting.setBudgetingOid(rs.getLong(PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_BUDGETING_OID]));
                objBudgeting.setIdPerkiraan(rs.getLong(PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_ID_PERKIRAAN]));
                objBudgeting.setPeriodeId(rs.getLong(PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_PERIODE_ID]));
                objBudgeting.setBudget(rs.getDouble(PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_BUDGET]));                
                lists.add(objBudgeting);                
            }
            
        } catch (Exception error) {
            System.out.println(".:: " + new PstAisoBudgeting() + ".list() : " + error.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return lists;
    }
    
    
    public static AisoBudgeting getAisoBudgeting(long lAccOid, long lPeriodOid)
    {
        AisoBudgeting objBudgeting = new AisoBudgeting();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT BUD." + PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_BUDGETING_OID]
                       + ", BUD." + PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_BUDGET]
                       + " FROM " + PstAisoBudgeting.TBL_AISO_BUDGETING
                       + " AS BUD WHERE BUD." + PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_ID_PERKIRAAN]
                       + " = " + lAccOid
                       + " AND BUD." + PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_PERIODE_ID]
                       + " = " + lPeriodOid;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            if (rs.next()) {
                objBudgeting.setBudgetingOid(rs.getLong(PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_BUDGETING_OID]));
                objBudgeting.setIdPerkiraan(lAccOid);
                objBudgeting.setPeriodeId(lPeriodOid);
                objBudgeting.setBudget(rs.getDouble(PstAisoBudgeting.fieldNames[PstAisoBudgeting.FLD_BUDGET]));
            }
            
            
        } catch (Exception error) {
            System.out.println(".:: " + new PstAisoBudgeting().getClass().getName() + ".getAisoBudgeting() : " + error.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return objBudgeting;
    }
    
}

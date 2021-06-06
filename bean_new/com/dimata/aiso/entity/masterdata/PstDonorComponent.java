/*
 * PstDonorComponent.java
 *
 * Created on December 31, 2007, 3:45 PM
 */

package com.dimata.aiso.entity.masterdata;

/* import package aiso*/
import com.dimata.aiso.db.*;

/* import package qdep*/
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_PersintentExc;

/* import package java util*/
import java.util.Vector;

/* import package java sql*/
import java.sql.ResultSet;

/**
 *
 * @author  dwi
 */
public class PstDonorComponent extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc {
    
    public static final String TBL_AISO_DONOR_COMPONENT = "aiso_donor_component";
    public static final int FLD_DONOR_COMPONENT_ID = 0;
    public static final int FLD_CODE = 1;
    public static final int FLD_NAME = 2;
    public static final int FLD_DESCRIPTION = 3;
    public static final int FLD_CONTACT_ID = 4;
    
    public static String [] fieldNames = {
        "DONOR_COMPONENT_ID",
        "CODE",
        "NAME",
        "DESCRIPTION",
        "CONTACT_ID"
    };
    
    public static int [] fieldTypes = {
        TYPE_PK + TYPE_ID + TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_FK + TYPE_LONG
    };
    
    /** Creates a new instance of PstDonorComponent */
    public PstDonorComponent() {
    }
    
    public PstDonorComponent(int i) throws DBException {
        super(new PstDonorComponent());
    }
    
    public PstDonorComponent(String sOid) throws DBException {
        super(new PstDonorComponent(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    public PstDonorComponent(long lOid) throws DBException {
        super(new PstDonorComponent(0));
        String sOid = "0";
        try {
            sOid = String.valueOf(lOid);
        } catch (Exception e) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }

        if (!locate(sOid))
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
         return new PstDonorComponent().getClass().getName();
    }
    
    public String getTableName() {
        return TBL_AISO_DONOR_COMPONENT;
    }
    
    public long deleteExc(Entity ent) throws Exception {
         if (ent == null) {
            throw  new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }
    
    public long fetchExc(Entity ent) throws Exception {
        DonorComponent ojbDonorComp = PstDonorComponent.fetchExc(ent.getOID());
        ent = (Entity) ojbDonorComp;
        return ojbDonorComp.getOID();
    }
    
    public long insertExc(Entity ent) throws Exception {
        return insertExc((DonorComponent) ent);
    }
    
    public long updateExc(Entity ent) throws Exception {
        return updateExc((DonorComponent) ent);
    }
    
     public static DonorComponent fetchExc(long Oid) throws DBException {
        try {
            DonorComponent ojbDonorComp = new DonorComponent();
            PstDonorComponent pstDonorComp = new PstDonorComponent(Oid);
            ojbDonorComp.setOID(Oid);
            ojbDonorComp.setCode(pstDonorComp.getString(FLD_CODE));
            ojbDonorComp.setName(pstDonorComp.getString(FLD_NAME));
            ojbDonorComp.setDescription(pstDonorComp.getString(FLD_DESCRIPTION));
            ojbDonorComp.setContactId(pstDonorComp.getlong(FLD_CONTACT_ID));
            
            System.out.println("ojbDonorComp.getContactId() ===== > "+ojbDonorComp.getContactId());
            
            return ojbDonorComp;
            
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDonorComponent(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(DonorComponent ojbDonorComp) throws DBException {
        try {
            PstDonorComponent pstDonorComp = new PstDonorComponent(0);

            pstDonorComp.setString(FLD_CODE, ojbDonorComp.getCode());
            pstDonorComp.setString(FLD_NAME, ojbDonorComp.getName());
            pstDonorComp.setString(FLD_DESCRIPTION, ojbDonorComp.getDescription());
            pstDonorComp.setLong(FLD_CONTACT_ID, ojbDonorComp.getContactId());
            
            pstDonorComp.insert();
            ojbDonorComp.setOID(pstDonorComp.getlong(FLD_DONOR_COMPONENT_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDonorComponent(0), DBException.UNKNOWN);
        }
        return ojbDonorComp.getOID();
    }

    public static long updateExc(DonorComponent ojbDonorComp) throws DBException {
        try {
            if (ojbDonorComp != null && ojbDonorComp.getOID() != 0) {
                PstDonorComponent pstDonorComp = new PstDonorComponent(ojbDonorComp.getOID());

                pstDonorComp.setString(FLD_CODE, ojbDonorComp.getCode());
                pstDonorComp.setString(FLD_NAME, ojbDonorComp.getName());
                pstDonorComp.setString(FLD_DESCRIPTION, ojbDonorComp.getDescription());
                pstDonorComp.setLong(FLD_CONTACT_ID, ojbDonorComp.getContactId());

                pstDonorComp.update();
                return ojbDonorComp.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDonorComponent(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long Oid) throws DBException {
        try {
            PstDonorComponent pstDonorComp = new PstDonorComponent(Oid);
            pstDonorComp.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDonorComponent(0), DBException.UNKNOWN);
        }
        return Oid;
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_AISO_DONOR_COMPONENT + " ";
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;

            if (order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;

            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
                    break;

                case DBHandler.DBSVR_POSTGRESQL:
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + limitStart;

                    break;

                case DBHandler.DBSVR_SYBASE:
                    break;

                case DBHandler.DBSVR_ORACLE:
                    break;

                case DBHandler.DBSVR_MSSQL:
                    break;

                default:
                    break;
            }
            System.out.println("PstDonorComponent.list() :::: "+sql);    
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                DonorComponent ojbDonorComp = new DonorComponent();
                resultToObject(rs, ojbDonorComp);
                lists.add(ojbDonorComp);
            }
        } catch (Exception error) {
            System.out.println(".:: " + new DonorComponent().getClass().getName() + ".list() : " + error.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return lists;
    }

    private static void resultToObject(ResultSet rs, DonorComponent ojbDonorComp) {
        try {

            ojbDonorComp.setOID(rs.getLong(PstDonorComponent.fieldNames[PstDonorComponent.FLD_DONOR_COMPONENT_ID]));
            ojbDonorComp.setCode(rs.getString(PstDonorComponent.fieldNames[PstDonorComponent.FLD_CODE]));
            ojbDonorComp.setName(rs.getString(PstDonorComponent.fieldNames[PstDonorComponent.FLD_NAME]));
            ojbDonorComp.setDescription(rs.getString(PstDonorComponent.fieldNames[PstDonorComponent.FLD_DESCRIPTION]));
            ojbDonorComp.setContactId(rs.getLong(PstDonorComponent.fieldNames[PstDonorComponent.FLD_CONTACT_ID]));

        } catch (Exception e) {
            System.out.println("resultToObject() " + e.toString());
        }
    }

    public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + PstDonorComponent.fieldNames[PstDonorComponent.FLD_DONOR_COMPONENT_ID] + ") " +
                    " FROM " + TBL_AISO_DONOR_COMPONENT;
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;

            System.out.println(" SQL GET COUNT DONOR LIST ===> "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            int count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            return count;
        } catch (Exception e) {
            System.out.println(e);
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }
    
    public static Vector getListAccount(Vector vectCode, Vector vectName, Vector vectDonorName, int start, int recordToGet, String sortBy) {
        Vector list = getLisAccount(vectCode, vectName, vectDonorName, start, recordToGet, sortBy);
        return list;
    }

    public static Vector getLisAccount(Vector vectCode, Vector vectName, Vector vectDonorName, int start, int recordToGet, String sortBy) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            String sql = "SELECT DISTINCT " + fieldNames[FLD_DONOR_COMPONENT_ID]
                    + ", " + fieldNames[FLD_CODE]
                    + ", " + fieldNames[FLD_NAME]                    
                    + ", " + fieldNames[FLD_DESCRIPTION]
                    + ", " + fieldNames[FLD_CONTACT_ID]
                    + " FROM " + TBL_AISO_DONOR_COMPONENT;           

            String strCode = "";

            if (vectCode != null && vectCode.size() > 0) {
                for (int a = 0; a < vectCode.size(); a++) {
                    if (strCode.length() < 1) {
                        strCode = fieldNames[FLD_CODE] + " LIKE '%" + vectCode.get(a) + "%'";
                    } else {
                        strCode = strCode + " OR " + fieldNames[FLD_CODE] + " LIKE '%" + vectCode.get(a) + "%'";
                    }
                    strCode = "(" + strCode + ")";
                }
            }

            String strName = "";

            if (vectName != null && vectName.size() > 0) {
                for (int a = 0; a < vectName.size(); a++) {
                    if (strName.length() < 1) {
                        strName = fieldNames[FLD_NAME] + " LIKE '%" + vectName.get(a) + "%'";
                    } else {
                        strName = strName + " OR " + fieldNames[FLD_NAME] + " LIKE '%" + vectName.get(a) + "%'";
                    }
                    strName = "(" + strName + ")";
                }
            }
            
            String strDonorName = "";

            
            if (vectDonorName != null && vectDonorName.size() > 0) {
                for (int a = 0; a < vectDonorName.size(); a++) {                    
                    if (strDonorName.length() < 1) {                       
                        strDonorName = fieldNames[FLD_CONTACT_ID] + " LIKE '%" + vectDonorName.get(a) + "%'";                        
                    } else {
                        strDonorName = strDonorName + " OR " + fieldNames[FLD_CONTACT_ID] + " LIKE '%" + vectDonorName.get(a) + "%'";
                    }
                    strDonorName = "(" + strDonorName + ")";
                }
            }
            

            String allCondition = "";           

            if (strCode != "" && strCode.length() > 0) {                
                    allCondition = strCode;                
            }

            if (strName != "" && strName.length() > 0) {
                if ((allCondition.length() > 0)) {
                    allCondition = allCondition + " AND " + strName;
                } else {
                    allCondition = strName;
                }
            }            
            
            if (strDonorName != "" && strDonorName.length() > 0) {
                if ((allCondition.length() > 0)) {
                    allCondition = allCondition + " AND " + strDonorName;
                } else {
                    allCondition = strDonorName;
                }
            }

            if (allCondition != "" && allCondition.length() > 0) {
                sql = sql + " WHERE " + allCondition;
            }

            if (sortBy != "" && sortBy.length() > 0) {
                sql = sql + " ORDER BY " + sortBy;
            }


            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (start == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + start + "," + recordToGet;
                    break;

                case DBHandler.DBSVR_POSTGRESQL:
                    if (start == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + start;

                    break;

                case DBHandler.DBSVR_SYBASE:
                    break;

                case DBHandler.DBSVR_ORACLE:
                    break;

                case DBHandler.DBSVR_MSSQL:
                    break;

                default:
                    break;
            }
            
            System.out.println("sql getListAccount =====> "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                DonorComponent objDonorComponent = new DonorComponent();
                objDonorComponent.setOID(rs.getLong(fieldNames[FLD_DONOR_COMPONENT_ID]));
                objDonorComponent.setCode(rs.getString(fieldNames[FLD_CODE]));
                objDonorComponent.setName(rs.getString(fieldNames[FLD_NAME]));
                objDonorComponent.setDescription(rs.getString(fieldNames[FLD_DESCRIPTION]));
                objDonorComponent.setContactId(rs.getLong(fieldNames[FLD_CONTACT_ID]));                
                result.add(objDonorComponent);
            }
        } catch (Exception e) {
            System.out.println("PstPerkiraan.getListAccount() err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    public static boolean cekDonorComp(String whClause){
        try{
            Vector vList = (Vector)list(0,0,whClause,"");
            if(vList != null && vList.size() > 0){
                return true;
            }
        }catch(Exception e){}
        return false;
    }
}

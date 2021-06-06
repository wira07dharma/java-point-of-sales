// Generated by Together

package com.dimata.aiso.entity.masterdata;

// import java

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date; 

// import qdep
import com.dimata.aiso.db.*;
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_PersintentExc;

public class PstAccountLink extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc {
    public static final String TBL_ACCOUNT_LINK = "aiso_account_link";
    public static final int FLD_ACCOUNT_LINK_ID = 0;
    public static final int FLD_LINKTYPE = 1;
    public static final int FLD_FIRST_ID = 2;
    public static final int FLD_SECOND_ID = 3;
    public static final int FLD_DEPARTMENT_ID = 4;

    public static String[] fieldNames = {
        "ACCOUNT_LINK_ID",
        "LINK_TYPE",
        "FIRST_ID",
        "SECOND_ID",
        "DEPARTMENT_ID"
    };

    public static int[] fieldTypes = {
        TYPE_PK + TYPE_ID + TYPE_LONG,
        TYPE_INT,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG
    };


    public PstAccountLink() {
    }

    public PstAccountLink(int i) throws DBException {
        super(new PstAccountLink());
    }

    public PstAccountLink(String sOid) throws DBException {
        super(new PstAccountLink(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstAccountLink(long lOid) throws DBException {
        super(new PstAccountLink(0));
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

    public int getFieldSize() {
        return fieldNames.length;
    }

    public String getTableName() {
        return TBL_ACCOUNT_LINK;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstAccountLink().getClass().getName();
    }

    public long fetchExc(Entity ent) throws DBException {
        AccountLink acclink = fetchExc(ent.getOID());
        ent = (Entity) acclink;
        return acclink.getOID();
    }

    public long insertExc(Entity ent) throws DBException {
        return insertExc((AccountLink) ent);
    }

    public long updateExc(Entity ent) throws DBException {
        return updateExc((AccountLink) ent);
    }

    public long deleteExc(Entity ent) throws DBException {
        if (ent == null) {
            throw  new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static AccountLink fetchExc(long Oid) throws DBException {
        try {
            AccountLink acclink = new AccountLink();
            PstAccountLink pstAccountLink = new PstAccountLink(Oid);
            acclink.setOID(Oid);

            acclink.setLinkType(pstAccountLink.getInt(FLD_LINKTYPE));
            acclink.setFirstId(pstAccountLink.getlong(FLD_FIRST_ID));
            acclink.setFirstId(pstAccountLink.getlong(FLD_SECOND_ID));
            acclink.setDepartmentOid(pstAccountLink.getlong(FLD_DEPARTMENT_ID));

            return acclink;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstAccountLink(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(AccountLink acclink) throws DBException {
        try {
            PstAccountLink pstAccountLink = new PstAccountLink(0);

            pstAccountLink.setInt(FLD_LINKTYPE, acclink.getLinkType());
            pstAccountLink.setLong(FLD_FIRST_ID, acclink.getFirstId());
            pstAccountLink.setLong(FLD_SECOND_ID, acclink.getSecondId());
            pstAccountLink.setLong(FLD_DEPARTMENT_ID, acclink.getDepartmentOid());

            pstAccountLink.insert();
            acclink.setOID(pstAccountLink.getlong(FLD_ACCOUNT_LINK_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstAccountLink(0), DBException.UNKNOWN);
        }
        return acclink.getOID();
    }

    public static long updateExc(AccountLink acclink) throws DBException {
        try {
            if (acclink != null && acclink.getOID() != 0) {
                PstAccountLink pstAccountLink = new PstAccountLink(acclink.getOID());

                pstAccountLink.setInt(FLD_LINKTYPE, acclink.getLinkType());
                pstAccountLink.setLong(FLD_FIRST_ID, acclink.getFirstId());
                pstAccountLink.setLong(FLD_SECOND_ID, acclink.getSecondId());
                pstAccountLink.setLong(FLD_DEPARTMENT_ID, acclink.getDepartmentOid());

                pstAccountLink.update();
                return acclink.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstAccountLink(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long Oid) throws DBException {
        try {
            PstAccountLink pstAccountLink = new PstAccountLink(Oid);
            pstAccountLink.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstAccountLink(0), DBException.UNKNOWN);
        }
        return Oid;
    }


    public static Vector listAll() {
        return list(0, 500, "", "");
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_ACCOUNT_LINK + " ";
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

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                AccountLink acclink = new AccountLink();
                resultToObject(rs, acclink);
                lists.add(acclink);
            }
        } catch (Exception error) {
            System.out.println(".:: " + new PstAccountLink().getClass().getName() + ".list() : " + error.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return lists;
    }

    private static void resultToObject(ResultSet rs, AccountLink acclink) {
        try {
            acclink.setOID(rs.getLong(PstAccountLink.fieldNames[PstAccountLink.FLD_ACCOUNT_LINK_ID]));
            acclink.setFirstId(rs.getLong(PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID]));
            acclink.setSecondId(rs.getLong(PstAccountLink.fieldNames[PstAccountLink.FLD_SECOND_ID]));
            acclink.setLinkType(rs.getInt(PstAccountLink.fieldNames[PstAccountLink.FLD_LINKTYPE]));
            acclink.setDepartmentOid(rs.getLong(PstAccountLink.fieldNames[PstAccountLink.FLD_DEPARTMENT_ID]));
        } catch (Exception e) {
            System.out.println("resultToObject() " + e.toString());
        }
    }

    public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID] + ") " +
                    " FROM " + TBL_ACCOUNT_LINK;
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;

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

    public static long getLinkAccountId(int accType) {
        DBResultSet dbrs = null;
        long result = 0;
        try {
            String sql = "SELECT " + PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID] +
                    " FROM " + PstAccountLink.TBL_ACCOUNT_LINK +
                    " WHERE " + PstAccountLink.fieldNames[PstAccountLink.FLD_LINKTYPE] +
                    " = " + accType;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                result = rs.getLong(PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID]);
                break;
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("---> PstAccountLink.getLinkAccountId() err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }


    public static String getLinkAccountStr(int accType) {
        DBResultSet dbrs = null;
        String result = "";
        try {
            String sql = "SELECT " + PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID] +
                    " FROM " + PstAccountLink.TBL_ACCOUNT_LINK +
                    " WHERE " + PstAccountLink.fieldNames[PstAccountLink.FLD_LINKTYPE] +
                    " = " + accType;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                result = result + rs.getLong(PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID]) + ",";
            }

            if (result.length() > 0) {
                result = result.substring(0, result.length() - 1);
            }
        } catch (Exception e) {
            System.out.println("---> PstAccountLink.getLinkAccountStr() err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    public boolean isValidAccLink(int linkType, long accFirst, long accSecond) {
        if (accFirst == accSecond) {
            return false;
        } else {
            String whereClause = PstAccountLink.fieldNames[PstAccountLink.FLD_LINKTYPE] + " = " + linkType;
            String order = PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID];
            Vector vectAccLink = PstAccountLink.list(0, 0, whereClause, order);
            if (vectAccLink != null && vectAccLink.size() > 0) {
                for (int i = 0; i < vectAccLink.size(); i++) {
                    AccountLink accLink = (AccountLink) vectAccLink.get(i);
                    if ((accLink.getLinkType() == linkType) && (accLink.getFirstId() == accFirst) && (accLink.getSecondId() == accSecond)) {
                        return false;
                    }
                }
            }
            return true;
        }
    }


    public boolean isValidAccLink(long lDepartmentId, int linkType, long accFirst, long accSecond) {
        if (accFirst == accSecond) {
            return false;
        } else {
            String whereClause = PstAccountLink.fieldNames[PstAccountLink.FLD_DEPARTMENT_ID] + " = " + lDepartmentId +
                    " AND " + PstAccountLink.fieldNames[PstAccountLink.FLD_LINKTYPE] + " = " + linkType;
            String order = PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID];
            Vector vectAccLink = PstAccountLink.list(0, 0, whereClause, order);
            if (vectAccLink != null && vectAccLink.size() > 0) {
                for (int i = 0; i < vectAccLink.size(); i++) {
                    AccountLink accLink = (AccountLink) vectAccLink.get(i);
                    if ((accLink.getDepartmentOid() == lDepartmentId)
                            && (accLink.getLinkType() == linkType)
                            && (accLink.getFirstId() == accFirst)
                            && (accLink.getSecondId() == accSecond)) {
                        return false;
                    }
                }
            }
            return true;
        }
    }


    /**
     * @param iAccountLinkType
     * @return
     */
    public static Vector getVectObjAccountLink(long lDepartmentOid, int iAccountLinkType) {
        DBResultSet dbrs = null;
        Vector vectResult = new Vector(1, 1);
        try {
            String sql = "SELECT " + PstAccountLink.fieldNames[PstAccountLink.FLD_ACCOUNT_LINK_ID] +
                    ", " + PstAccountLink.fieldNames[PstAccountLink.FLD_LINKTYPE] +
                    ", " + PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID] +
                    ", " + PstAccountLink.fieldNames[PstAccountLink.FLD_SECOND_ID] +
                    ", " + PstAccountLink.fieldNames[PstAccountLink.FLD_DEPARTMENT_ID] +
                    " FROM " + PstAccountLink.TBL_ACCOUNT_LINK +
                    " WHERE "+PstAccountLink.fieldNames[PstAccountLink.FLD_LINKTYPE] +
                    " = " + iAccountLinkType;
                    
                    if(lDepartmentOid != 0){
                        String where = " AND "+PstAccountLink.fieldNames[PstAccountLink.FLD_DEPARTMENT_ID] +
                                        " = " + lDepartmentOid;
                        sql = sql + where;
                    }
            System.out.println("sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                AccountLink objAccountLink = new AccountLink();
                objAccountLink.setOID(rs.getLong(PstAccountLink.fieldNames[PstAccountLink.FLD_ACCOUNT_LINK_ID]));
                objAccountLink.setLinkType(rs.getInt(PstAccountLink.fieldNames[PstAccountLink.FLD_LINKTYPE]));
                objAccountLink.setFirstId(rs.getLong(PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID]));
                objAccountLink.setSecondId(rs.getLong(PstAccountLink.fieldNames[PstAccountLink.FLD_SECOND_ID]));
                objAccountLink.setDepartmentOid(rs.getLong(PstAccountLink.fieldNames[PstAccountLink.FLD_DEPARTMENT_ID]));

                vectResult.add(objAccountLink);
            }
        } catch (Exception e) {
            System.out.println("---> PstAccountLink.getVectObjAccountLink() err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return vectResult;
    }


    public static Vector getVectObjListAccountLink(long lDepartmentOid, int iAccountLinkType){
        return getVectObjListAccountLink(lDepartmentOid, iAccountLinkType, 0);
    }
    
    /** gadnyana
     * untuk menampilkan data perkiraan tapi yang ada di link account
     * sesuai dengan department / type group
     * @param lDepartmentOid
     * @param iAccountLinkType
     * @return
     */
    public static Vector getVectObjListAccountLink(long lDepartmentOid, int iAccountLinkType, long lAccountId) {
        DBResultSet dbrs = null;
        Vector vectResult = new Vector(1, 1);
        try {
            String sql = "SELECT PRK." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    ", PRK." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN] +
                    ", PRK." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA] +
                    ", PRK." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH] +
                    ", PRK." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ID_PARENT] +
                    ", PRK." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP] +
                    ", PRK." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT] +
                    " FROM " + PstAccountLink.TBL_ACCOUNT_LINK + " AS LK " +
                    " INNER JOIN " + PstPerkiraan.TBL_PERKIRAAN + " AS PRK " +
                    " ON LK." + PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID] + "=" +
                    " PRK." + PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN] +
                    " WHERE LK." + PstAccountLink.fieldNames[PstAccountLink.FLD_LINKTYPE] + 
                    " = " + iAccountLinkType;
            if (lDepartmentOid != 0) {
                sql = sql + " AND LK." + PstAccountLink.fieldNames[PstAccountLink.FLD_DEPARTMENT_ID] +
                        " = " + lDepartmentOid;
            }
            
            if(lAccountId != 0){
                sql = sql + " AND LK." + PstAccountLink.fieldNames[PstAccountLink.FLD_FIRST_ID] +
                        " != " + lAccountId;
            }
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Perkiraan perkiraan = new Perkiraan();
                perkiraan.setOID(rs.getLong(PstPerkiraan.fieldNames[PstPerkiraan.FLD_IDPERKIRAAN]));
                perkiraan.setIdParent(rs.getLong(PstPerkiraan.fieldNames[PstPerkiraan.FLD_ID_PARENT]));
                perkiraan.setAccountGroup(rs.getInt(PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_GROUP]));
                perkiraan.setTandaDebetKredit(rs.getInt(PstPerkiraan.fieldNames[PstPerkiraan.FLD_TANDA_DEBET_KREDIT]));
                perkiraan.setNoPerkiraan(rs.getString(PstPerkiraan.fieldNames[PstPerkiraan.FLD_NOPERKIRAAN]));
                perkiraan.setNama(rs.getString(PstPerkiraan.fieldNames[PstPerkiraan.FLD_NAMA]));
                perkiraan.setAccountNameEnglish(rs.getString(PstPerkiraan.fieldNames[PstPerkiraan.FLD_ACCOUNT_NAME_ENGLISH]));
                vectResult.add(perkiraan);
            }
        } catch (Exception e) {
            System.out.println("---> PstAccountLink.getVectObjAccountLink() err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return vectResult;
    }


    /**
     * @param iAccountLinkType
     * @return
     */
    public static AccountLink getObjAccountLink(long lDepartmentOid, int iAccountLinkType) {
        AccountLink objResult = new AccountLink();

        Vector vAccountLink = PstAccountLink.getVectObjAccountLink(lDepartmentOid, iAccountLinkType);
        if (vAccountLink != null && vAccountLink.size() > 0) {
            int iAccountLinkAmount = vAccountLink.size();
            for (int i = 0; i < iAccountLinkAmount; i++) {
                objResult = (AccountLink) vAccountLink.get(0);
            }
        }

        return objResult;
    }
}

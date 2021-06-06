/* Created on 	:  [date] [time] AM/PM
 *
 * @author	 :
 * @version	 :
 */

/*******************************************************************
 * Class Description 	: [project description ... ]
 * Imput Parameters 	: [input parameter ...]
 * Output 		: [output ...]
 *******************************************************************/

package com.dimata.posbo.entity.masterdata;

/* package java */

import com.dimata.common.entity.custom.PstDataCustom;
import java.io.*
        ;
import java.sql.*
        ;
import java.util.*
        ;
import java.util.Date;

/* package qdep */
import com.dimata.util.lang.I_Language;

import com.dimata.qdep.entity.*;
import com.dimata.posbo.db.*;

/* package posbo */
//import com.dimata.posbo.db.DBHandler;
//import com.dimata.posbo.db.DBException;
//import com.dimata.posbo.db.DBLogger;
import com.dimata.posbo.entity.masterdata.*;
import org.json.JSONObject;


public class PstMemberGroup extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    //public static final  String TBL_MEMBER_GROUP = "MEMBER_GROUP";
    public static final String TBL_MEMBER_GROUP = "member_group";

    public static final int FLD_MEMBER_GROUP_ID = 0;
    public static final int FLD_DISCOUNT_TYPE_ID = 1;
    public static final int FLD_PRICE_TYPE_ID = 2;
    public static final int FLD_CODE = 3;
    public static final int FLD_NAME = 4;
    public static final int FLD_DESCRIPTION = 5;
    public static final int FLD_GROUP_TYPE = 6;
    public static final int FLD_TYPE_POINT = 7;
    public static final int FLD_POINT_IN_CALCULATE = 8;
    public static final int FLD_VIEW_CUSTOMER_TYPE = 9;

    public static final String[] fieldNames = {
        "MEMBER_GROUP_ID",
        "DISCOUNT_TYPE_ID",
        "PRICE_TYPE_ID",
        "CODE",
        "NAME",
        "DESCRIPTION",
        "GROUP_TYPE",
        "TYPE_POINT",
        "POINT_IN_CALCULATE",
        "VIEW_CUSTOMER_TYPE"
    };

    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_INT,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_INT
    };

    public static final int UMUM = 1;
    public static final int AGEN = 2;
    public static final int KHUSUS = 3;
    
    public static final int POINT_IN_CATEGORY = 0;
    public static final int POINT_ON_TOTAL_SALES=1;
    
    public static final int VIEW_ON_CATALOG=0;
    public static final int VIEW_ON_MEMBER=1;
    public static final int VIEW_ON_MEMBER_AND_CATALOG=2;

    public static final String typeNames[] = {
        "Retail", "Wholesale", "VIP"
    };
    
    public static final String typePointNames[]={
        "Point in Category","Poin on Total Sales"
    };
    
     public static final String viewMemberType[]={
        "View On Catalog","View On Member","View On Catalog And Member"
    };

    public PstMemberGroup() {
    }

    public PstMemberGroup(int i) throws DBException {
        super(new PstMemberGroup());
    }

    public PstMemberGroup(String sOid) throws DBException {
        super(new PstMemberGroup(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstMemberGroup(long lOid) throws DBException {
        super(new PstMemberGroup(0));
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
        return TBL_MEMBER_GROUP;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstMemberGroup().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        MemberGroup membergroup = fetchExc(ent.getOID());
        ent = (Entity) membergroup;
        return membergroup.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((MemberGroup) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((MemberGroup) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static MemberGroup fetchExc(long oid) throws DBException {
        try {
            MemberGroup membergroup = new MemberGroup();
            PstMemberGroup pstMemberGroup = new PstMemberGroup(oid);
            membergroup.setOID(oid);

            membergroup.setDiscountTypeId(pstMemberGroup.getlong(FLD_DISCOUNT_TYPE_ID));
            membergroup.setPriceTypeId(pstMemberGroup.getlong(FLD_PRICE_TYPE_ID));
            membergroup.setCode(pstMemberGroup.getString(FLD_CODE));
            membergroup.setName(pstMemberGroup.getString(FLD_NAME));
            membergroup.setDescription(pstMemberGroup.getString(FLD_DESCRIPTION));
            membergroup.setGroupType(pstMemberGroup.getInt(FLD_GROUP_TYPE));
            membergroup.setTypePoint(pstMemberGroup.getInt(FLD_TYPE_POINT));
            membergroup.setPointInCalculate(pstMemberGroup.getdouble(FLD_POINT_IN_CALCULATE));
            membergroup.setViewCustomerType(pstMemberGroup.getInt(FLD_VIEW_CUSTOMER_TYPE));

            return membergroup;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMemberGroup(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(MemberGroup membergroup) throws DBException {
        try {
            PstMemberGroup pstMemberGroup = new PstMemberGroup(0);

            pstMemberGroup.setLong(FLD_DISCOUNT_TYPE_ID, membergroup.getDiscountTypeId());
            pstMemberGroup.setLong(FLD_PRICE_TYPE_ID, membergroup.getPriceTypeId());
            pstMemberGroup.setString(FLD_CODE, membergroup.getCode());
            pstMemberGroup.setString(FLD_NAME, membergroup.getName());
            pstMemberGroup.setString(FLD_DESCRIPTION, membergroup.getDescription());
            pstMemberGroup.setInt(FLD_GROUP_TYPE, membergroup.getGroupType());
            pstMemberGroup.setInt(FLD_TYPE_POINT, membergroup.getTypePoint());
            pstMemberGroup.setDouble(FLD_POINT_IN_CALCULATE,membergroup.getPointInCalculate());
            pstMemberGroup.setInt(FLD_VIEW_CUSTOMER_TYPE,membergroup.getViewCustomerType());
            
            pstMemberGroup.insert();
            long oidDataSync=PstDataSyncSql.insertExc(pstMemberGroup.getInsertSQL());
            PstDataSyncStatus.insertExc(oidDataSync);
            membergroup.setOID(pstMemberGroup.getlong(FLD_MEMBER_GROUP_ID));
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstMemberGroup.getInsertSQL());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMemberGroup(0), DBException.UNKNOWN);
        }
        return membergroup.getOID();
    }

    public static long updateExc(MemberGroup membergroup) throws DBException {
        try {
            if (membergroup.getOID() != 0) {
                PstMemberGroup pstMemberGroup = new PstMemberGroup(membergroup.getOID());

                pstMemberGroup.setLong(FLD_DISCOUNT_TYPE_ID, membergroup.getDiscountTypeId());
                pstMemberGroup.setLong(FLD_PRICE_TYPE_ID, membergroup.getPriceTypeId());
                pstMemberGroup.setString(FLD_CODE, membergroup.getCode());
                pstMemberGroup.setString(FLD_NAME, membergroup.getName());
                pstMemberGroup.setString(FLD_DESCRIPTION, membergroup.getDescription());
                pstMemberGroup.setInt(FLD_GROUP_TYPE, membergroup.getGroupType());
                pstMemberGroup.setInt(FLD_TYPE_POINT, membergroup.getTypePoint());
                pstMemberGroup.setDouble(FLD_POINT_IN_CALCULATE,membergroup.getPointInCalculate());
                pstMemberGroup.setInt(FLD_VIEW_CUSTOMER_TYPE, membergroup.getViewCustomerType());

                pstMemberGroup.update();

                long oidDataSync=PstDataSyncSql.insertExc(pstMemberGroup.getUpdateSQL());
                PstDataSyncStatus.insertExc(oidDataSync);
				//kebutuhan untuk service transfer katalog
				PstDataCustom.insertDataForSyncAllLocation(pstMemberGroup.getUpdateSQL());
                return membergroup.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMemberGroup(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstMemberGroup pstMemberGroup = new PstMemberGroup(oid);
            pstMemberGroup.delete();
            long oidDataSync = PstDataSyncSql.insertExc(pstMemberGroup.getDeleteSQL());
                PstDataSyncStatus.insertExc(oidDataSync);
				//kebutuhan untuk service transfer katalog
				PstDataCustom.insertDataForSyncAllLocation(pstMemberGroup.getDeleteSQL());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstMemberGroup(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_MEMBER_GROUP;
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
                    if (limitStart == 0 && recordToGet == 0)
                        sql = sql + "";
                    else
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                MemberGroup membergroup = new MemberGroup();
                resultToObject(rs, membergroup);
                lists.add(membergroup);
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

    public static void resultToObject(ResultSet rs, MemberGroup membergroup) {
        try {
            membergroup.setOID(rs.getLong(PstMemberGroup.fieldNames[PstMemberGroup.FLD_MEMBER_GROUP_ID]));
            membergroup.setDiscountTypeId(rs.getLong(PstMemberGroup.fieldNames[PstMemberGroup.FLD_DISCOUNT_TYPE_ID]));
            membergroup.setPriceTypeId(rs.getLong(PstMemberGroup.fieldNames[PstMemberGroup.FLD_PRICE_TYPE_ID]));
            membergroup.setCode(rs.getString(PstMemberGroup.fieldNames[PstMemberGroup.FLD_CODE]));
            membergroup.setName(rs.getString(PstMemberGroup.fieldNames[PstMemberGroup.FLD_NAME]));
            membergroup.setDescription(rs.getString(PstMemberGroup.fieldNames[PstMemberGroup.FLD_DESCRIPTION]));
            membergroup.setGroupType(rs.getInt(PstMemberGroup.fieldNames[PstMemberGroup.FLD_GROUP_TYPE]));
            membergroup.setTypePoint(rs.getInt(fieldNames[FLD_TYPE_POINT]));
            membergroup.setPointInCalculate(rs.getDouble(fieldNames[FLD_POINT_IN_CALCULATE]));
            membergroup.setViewCustomerType(rs.getInt(fieldNames[FLD_VIEW_CUSTOMER_TYPE]));

        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long memberGroupId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_MEMBER_GROUP + " WHERE " +
                    PstMemberGroup.fieldNames[PstMemberGroup.FLD_MEMBER_GROUP_ID] + " = " + memberGroupId;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                result = true;
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            return result;
        }
    }

    public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + PstMemberGroup.fieldNames[PstMemberGroup.FLD_MEMBER_GROUP_ID] + ") FROM " + TBL_MEMBER_GROUP;
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
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }

    /** gadnyana
     * get untuk semua member group
     * @return hashtable
     */
    public static Hashtable getMemberGroup() {
        Hashtable has = new Hashtable();
        DBResultSet dbrs = null;
        try {
            Vector list = list(0, 0, "", "");
            if (list != null && list.size() > 0) {
                for (int k = 0; k < list.size(); k++) {
                    MemberGroup memberGroup = (MemberGroup) list.get(k);
                    has.put(memberGroup.getName().toUpperCase(), memberGroup);
                }
            }
        } catch (Exception e) {
            return new Hashtable();
        }
        return has;
    }


    /* This method used to find current data */
    public static int findLimitStart(long oid, int recordToGet, String whereClause) {
        String order = "";
        int size = getCount(whereClause);
        int start = 0;
        boolean found = false;
        for (int i = 0; (i < size) && !found; i = i + recordToGet) {
            Vector list = list(i, recordToGet, whereClause, order);
            start = i;
            if (list.size() > 0) {
                for (int ls = 0; ls < list.size(); ls++) {
                    MemberGroup membergroup = (MemberGroup) list.get(ls);
                    if (oid == membergroup.getOID())
                        found = true;
                }
            }
        }
        if ((start >= size) && (size > 0))
            start = start - recordToGet;

        return start;
    }


    public static String getCodeMemberWithMemberID(long memberID) {
        DBResultSet dbrs = null;
        String memberCode = "";
        try {
            String sql = "SELECT " + PstMemberGroup.fieldNames[PstMemberGroup.FLD_CODE] + " FROM " + TBL_MEMBER_GROUP;
            sql = sql + " WHERE " + PstMemberGroup.fieldNames[PstMemberGroup.FLD_MEMBER_GROUP_ID]+" ='"+memberID+"'";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                memberCode = rs.getString(1);
            }

            rs.close();
            return memberCode;
        } catch (Exception e) {
            return memberCode;
        } finally {
            DBResultSet.close(dbrs);
        }
    }
    public static JSONObject fetchJSON(long oid){
            JSONObject object = new JSONObject();
            try {
                MemberGroup memberGroup = PstMemberGroup.fetchExc(oid);
                object.put(PstMemberGroup.fieldNames[PstMemberGroup.FLD_MEMBER_GROUP_ID], memberGroup.getOID());
                object.put(PstMemberGroup.fieldNames[PstMemberGroup.FLD_CODE], memberGroup.getCode());
                object.put(PstMemberGroup.fieldNames[PstMemberGroup.FLD_DESCRIPTION], memberGroup.getDescription());
                object.put(PstMemberGroup.fieldNames[PstMemberGroup.FLD_DISCOUNT_TYPE_ID], memberGroup.getDiscountTypeId());
                object.put(PstMemberGroup.fieldNames[PstMemberGroup.FLD_GROUP_TYPE], memberGroup.getGroupType());
                object.put(PstMemberGroup.fieldNames[PstMemberGroup.FLD_NAME], memberGroup.getName());
                object.put(PstMemberGroup.fieldNames[PstMemberGroup.FLD_POINT_IN_CALCULATE], memberGroup.getPointInCalculate());
                object.put(PstMemberGroup.fieldNames[PstMemberGroup.FLD_PRICE_TYPE_ID], memberGroup.getPriceTypeId());
                object.put(PstMemberGroup.fieldNames[PstMemberGroup.FLD_TYPE_POINT], memberGroup.getTypePoint());
                object.put(PstMemberGroup.fieldNames[PstMemberGroup.FLD_VIEW_CUSTOMER_TYPE], memberGroup.getViewCustomerType());
            }catch(Exception exc){}

            return object;
        }
}

/* Created on 	:  [date] [time] AM/PM
 *
 * @author	 :
 * @version	 :
 */
/**
 * *****************************************************************
 * Class Description 	: [project description ... ]
 * Imput Parameters 	: [input parameter ...]
 * Output 		: [output ...]
 ******************************************************************
 */
package com.dimata.posbo.entity.masterdata;

/* package java */
import java.io.*;
import java.sql.*;
import java.util.*;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.util.*;
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.*;

//integrasi HANOMAN
import com.dimata.ObjLink.BOPos.CategoryLink;
import com.dimata.interfaces.BOPos.I_Category;
import org.json.JSONObject;

public class PstCatCommission extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    //public static final String TBL_CAT_COMMISSION = "POS_CATEGORY";
    public static final String TBL_CAT_COMMISSION = "pos_cat_commission";

    public static final int FLD_CAT_COMMISSION_ID = 0;
    public static final int FLD_CATEGORY_ID = 1;
    public static final int FLD_PERCENTAGE_COMMISSION = 2;
    public static final int FLD_START_DATE = 3;
    public static final int FLD_END_DATE = 4;
    public static final int FLD_DESCRIPTION = 5;
    public static final int FLD_STATUS = 6;

    public static final String[] fieldNames
            = {
                "POS_CATEG_COMMISSION_ID",
                "CATEGORY_ID",
                "PERCENTAGE_COMMISSION",
                "START_DATE",
                "END_DATE",
                "DESCRIPTION",
                "STATUS",
            };

    public static final int[] fieldTypes
            = {
                TYPE_LONG + TYPE_PK + TYPE_ID,
                TYPE_LONG,
                TYPE_FLOAT,
                TYPE_DATE,
                TYPE_DATE,
                TYPE_STRING,
                TYPE_INT
            };

    public PstCatCommission() {
    }

    public PstCatCommission(int i) throws DBException {
        super(new PstCatCommission());
    }

    public PstCatCommission(String sOid) throws DBException {
        super(new PstCatCommission(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstCatCommission(long lOid) throws DBException {
        super(new PstCatCommission(0));
        String sOid = "0";
        try {
            sOid = String.valueOf(lOid);
        } catch (Exception e) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public int getFieldSize() {
        return fieldNames.length;
    }

    public String getTableName() {
        return TBL_CAT_COMMISSION;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstCatCommission().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        CaCommission catcom = fetchExc(ent.getOID());
        ent = (Entity) catcom;
        return catcom.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((CaCommission) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((CaCommission) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static CaCommission fetchExc(long oid) throws DBException {
        try {
            CaCommission catcom = new CaCommission();
            PstCatCommission pstCatCom = new PstCatCommission(oid);
            catcom.setOID(oid);

            catcom.setPercentage(pstCatCom.getfloat(FLD_PERCENTAGE_COMMISSION));
            catcom.setStartDate(pstCatCom.getDate(FLD_START_DATE));
            catcom.setEndDate(pstCatCom.getDate(FLD_END_DATE));
            catcom.setDedcription(pstCatCom.getString(FLD_DESCRIPTION));
            catcom.setStatus(pstCatCom.getInt(FLD_STATUS));

            return catcom;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCatCommission(0), DBException.UNKNOWN);
        }
    }
    
    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
      return list(limitStart, recordToGet, whereClause, order, null, false);
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order, String joinCategory, boolean strictDate) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String grouping = "";
            String orderBy = "";
            String select = (joinCategory != null && !joinCategory.equals("")) ? " "+TBL_CAT_COMMISSION+".*, pc.CATEGORY_ID AS CAT_ID, pc.NAME, pc.CODE, pc.POINT_PRICE, pc.CATEGORY, pc.TYPE_CATEGORY, pc.DESCRIPTION, pc.LOCATION_ID, pc.CAT_PARENT_ID, pc.STATUS AS STS  " : " * ";
            String sql = "SELECT"+select+"FROM (SELECT * FROM " + TBL_CAT_COMMISSION + (joinCategory != null && (!joinCategory.equals("")) ? " ORDER BY "+PstCatCommission.fieldNames[PstCatCommission.FLD_START_DATE]+" DESC " :"") + ") AS "+TBL_CAT_COMMISSION+" ";
            if (joinCategory != null && !joinCategory.equals(""))  {
              sql += " "+joinCategory+" JOIN "+PstCategory.TBL_CATEGORY+" pc ON pc."+PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID]+" = "+" "+TBL_CAT_COMMISSION+"."+PstCatCommission.fieldNames[PstCatCommission.FLD_CATEGORY_ID];
              grouping = " GROUP BY pc.CATEGORY_ID ";
              if(strictDate) {
                sql += " AND NOW() >= "+TBL_CAT_COMMISSION+".START_DATE AND NOW() <= "+TBL_CAT_COMMISSION+".END_DATE AND "+TBL_CAT_COMMISSION+"."+fieldNames[FLD_STATUS]+"="+CaCommission.STATUS.VALUE[CaCommission.STATUS.MAP[CaCommission.STS_FINAL]];
              }
            }
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            sql+=grouping;
            if (order != null && order.length() > 0) {
                sql = sql + (orderBy.equals("") ? " ORDER BY " : orderBy+" , ") + order;
            }

            switch (DBHandler.DBSVR_TYPE) {
                case DBHandler.DBSVR_MYSQL:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + limitStart + "," + recordToGet;
                    }

                    break;

                case DBHandler.DBSVR_POSTGRESQL:
                    if (limitStart == 0 && recordToGet == 0) {
                        sql = sql + "";
                    } else {
                        sql = sql + " LIMIT " + recordToGet + " OFFSET " + limitStart;
                    }

                    break;

                case DBHandler.DBSVR_SYBASE:
                    break;

                case DBHandler.DBSVR_ORACLE:
                    break;

                case DBHandler.DBSVR_MSSQL:
                    break;

                default:
                    ;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                CaCommission catcom = new CaCommission();
                resultToObject(rs, catcom, (joinCategory != null && !joinCategory.equals("")));
                lists.add(catcom);
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

    public static void resultToObject(ResultSet rs, CaCommission catcom, boolean withCategory) {
        try {
            catcom.setOID(rs.getLong(fieldNames[FLD_CAT_COMMISSION_ID]));
            catcom.setPercentage(rs.getFloat(fieldNames[FLD_PERCENTAGE_COMMISSION]));
            catcom.setStartDate(rs.getDate(fieldNames[FLD_START_DATE]));
            catcom.setEndDate(rs.getDate(fieldNames[FLD_END_DATE]));
            catcom.setDedcription(rs.getString(fieldNames[FLD_DESCRIPTION]));
            catcom.setComStatus(rs.getInt(fieldNames[FLD_STATUS]));
            catcom.setCatId(rs.getLong(fieldNames[FLD_CATEGORY_ID]));
            if (withCategory) {
              catcom.setCode(rs.getString(PstCategory.fieldNames[PstCategory.FLD_CODE]));
              catcom.setName(rs.getString(PstCategory.fieldNames[PstCategory.FLD_NAME]));
              catcom.setPointPrice(rs.getDouble(PstCategory.fieldNames[PstCategory.FLD_POINT_PRICE]));
              catcom.setTypeCategory(rs.getInt(PstCategory.fieldNames[PstCategory.FLD_TYPE_CATEGORY]));
              catcom.setDescription(rs.getString(PstCategory.fieldNames[PstCategory.FLD_DESCRIPTION]));
              catcom.setLocationId(rs.getLong(PstCategory.fieldNames[PstCategory.FLD_LOCATION_ID]));
              catcom.setCatParentId(rs.getLong(PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID]));
              catcom.setCategoryId(rs.getLong("CAT_ID"));
              catcom.setStatus(rs.getInt("STS"));
            }
        } catch (Exception e) {
            System.out.println("error : " + e.toString());
        }
    }

    public static boolean checkOID(long catcomId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_CAT_COMMISSION + " WHERE "
                    + PstCatCommission.fieldNames[PstCatCommission.FLD_CAT_COMMISSION_ID] + " = " + catcomId;

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
        }
        return result;
    }

    public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + PstCatCommission.fieldNames[PstCatCommission.FLD_CAT_COMMISSION_ID] + ") FROM " + TBL_CAT_COMMISSION;
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

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


    /* This method used to find current data */
    public static int findLimitStart(long oid, int recordToGet, String whereClause, String orderClause) {
        int size = getCount(whereClause);
        int start = 0;
        boolean found = false;
        for (int i = 0; (i < size) && !found; i = i + recordToGet) {
            Vector list = list(i, recordToGet, whereClause, orderClause);
            start = i;
            if (list.size() > 0) {
                for (int ls = 0; ls < list.size(); ls++) {
                    CaCommission catcom = (CaCommission) list.get(ls);
                    if (oid == catcom.getOID()) {
                        found = true;
                    }
                }
            }
        }
        if ((start >= size) && (size > 0)) {
            start = start - recordToGet;
        }

        return start;
    }

    /* This method used to find command where current data */
    public static int findLimitCommand(int start, int recordToGet, int vectSize) {
        int cmd = Command.LIST;
        int mdl = vectSize % recordToGet;
        vectSize = vectSize + (recordToGet - mdl);
        if (start == 0) {
            cmd = Command.FIRST;
        } else if (start == (vectSize - recordToGet)) {
            cmd = Command.LAST;
        } else {
            start = start + recordToGet;
            if (start <= (vectSize - recordToGet)) {
                cmd = Command.NEXT;
                //System.out.println("next.......................");
            } else {
                start = start - recordToGet;
                if (start > 0) {
                    cmd = Command.PREV;
                    //System.out.println("prev.......................");
                }
            }
        }

        return cmd;
    }
    
    public static long insertExc(CaCommission catCommission) throws DBException {
        try {
            PstCatCommission pstCatCommission = new PstCatCommission(0);
            
            pstCatCommission.setFloat(FLD_PERCENTAGE_COMMISSION, catCommission.getPercentage());
            pstCatCommission.setDate(FLD_START_DATE, catCommission.getStartDate());
            pstCatCommission.setDate(FLD_END_DATE, catCommission.getEndDate());
            pstCatCommission.setString(FLD_DESCRIPTION, catCommission.getDedcription());
            pstCatCommission.setInt(FLD_STATUS, catCommission.getComStatus());
            pstCatCommission.setLong(FLD_CATEGORY_ID, catCommission.getCatId());
            pstCatCommission.insert();
            
            catCommission.setOID(pstCatCommission.getlong(FLD_CAT_COMMISSION_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstRoom(0), DBException.UNKNOWN);
        }
        return catCommission.getOID();
    }

    public static long updateExc(CaCommission catCommission) throws DBException {
        try {
            if (catCommission.getOID() != 0) {
                PstCatCommission pstCatCommission = new PstCatCommission(catCommission.getOID());
                
                pstCatCommission.setFloat(FLD_PERCENTAGE_COMMISSION, catCommission.getPercentage());
                pstCatCommission.setDate(FLD_START_DATE, catCommission.getStartDate());
                pstCatCommission.setDate(FLD_END_DATE, catCommission.getEndDate());
                pstCatCommission.setString(FLD_DESCRIPTION, catCommission.getDedcription());
                pstCatCommission.setInt(FLD_STATUS, catCommission.getComStatus());
                pstCatCommission.setLong(FLD_CATEGORY_ID, catCommission.getCatId());
               
                pstCatCommission.update();
                
                return catCommission.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstRoom(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstRoom pstRoom = new PstRoom(oid);
            pstRoom.delete();
            
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstRoom(0), DBException.UNKNOWN);
        }
        return oid;
    }
    
        
    public static JSONObject fetchJSON(long oid){
            JSONObject object = new JSONObject();
            try {
                CaCommission caCommission = PstCatCommission.fetchExc(oid);
                object.put(PstCatCommission.fieldNames[PstCatCommission.FLD_CAT_COMMISSION_ID], caCommission.getOID());
                object.put(PstCatCommission.fieldNames[PstCatCommission.FLD_CATEGORY_ID], caCommission.getCatId());
                object.put(PstCatCommission.fieldNames[PstCatCommission.FLD_DESCRIPTION], caCommission.getDedcription());
                object.put(PstCatCommission.fieldNames[PstCatCommission.FLD_END_DATE], caCommission.getEndDate());
                object.put(PstCatCommission.fieldNames[PstCatCommission.FLD_PERCENTAGE_COMMISSION], caCommission.getPercentage());
                object.put(PstCatCommission.fieldNames[PstCatCommission.FLD_START_DATE], caCommission.getStartDate());
                object.put(PstCatCommission.fieldNames[PstCatCommission.FLD_STATUS], caCommission.getStatus());

            }catch(Exception exc){}

            return object;
        }

}

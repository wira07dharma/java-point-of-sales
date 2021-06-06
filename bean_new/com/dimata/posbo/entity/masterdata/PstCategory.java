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
import java.util.Date;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.util.*;
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.*;

/* package garment */
import com.dimata.posbo.entity.masterdata.*;

//integrasi HANOMAN
import com.dimata.ObjLink.BOPos.CategoryLink;
import com.dimata.common.entity.custom.PstDataCustom;
import com.dimata.interfaces.BOPos.I_Category;
import org.json.JSONObject;

public class PstCategory extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language, I_Category {

    //public static final String TBL_CATEGORY = "POS_CATEGORY";
    public static final String TBL_CATEGORY = "pos_category";

    public static final int FLD_CATEGORY_ID = 0;
    public static final int FLD_CODE = 1;
    public static final int FLD_NAME = 2;
    public static final int FLD_POINT_PRICE = 3;
    public static final int FLD_DESCRIPTION = 4;
    //adding production id by Mirahu 20120511
    public static final int FLD_LOCATION_ID = 5;
    //adding opie-eyek 20130725
    public static final int FLD_CAT_PARENT_ID = 6;
    public static final int FLD_STATUS = 7;
    public static final int FLD_CATEGORY = 8;
    public static final int FLD_TYPE_CATEGORY = 9;
    public static final int FLD_KENAIKAN_HARGA = 10;

    private static Hashtable unitParentID = null;

    public static final String[] fieldNames
            = {
                "CATEGORY_ID",
                "CODE",
                "NAME",
                "POINT_PRICE",
                "DESCRIPTION",
                //adding production id by Mirahu 20120511
                "LOCATION_ID",
                //adding opie-eyek 20130725
                "CAT_PARENT_ID",
                "STATUS",
                "CATEGORY",
                "TYPE_CATEGORY",
                "KENAIKAN_HARGA"

            };

    public static final int[] fieldTypes
            = {
                TYPE_LONG + TYPE_PK + TYPE_ID,
                TYPE_STRING,
                TYPE_STRING,
                TYPE_FLOAT,
                TYPE_STRING,
                //adding production id by Mirahu 20120511
                TYPE_LONG,
                TYPE_LONG,
                TYPE_INT,
                TYPE_INT,
                TYPE_INT,
                TYPE_INT
            };

    public PstCategory() {
    }

    public static final int FOOD = 0;
    public static final int BEVERAGE = 1;
    public static final int OTHER = 2;

    public static final String category[] = {
        "Food", "Beverage", "Other"
    };

    public PstCategory(int i) throws DBException {
        super(new PstCategory());
    }

    public PstCategory(String sOid) throws DBException {
        super(new PstCategory(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstCategory(long lOid) throws DBException {
        super(new PstCategory(0));
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
        return TBL_CATEGORY;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstCategory().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        Category category = fetchExc(ent.getOID());
        ent = (Entity) category;
        return category.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((Category) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((Category) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static Category fetchExc(long oid) throws DBException {
        try {
            Category category = new Category();
            PstCategory pstCategory = new PstCategory(oid);
            category.setOID(oid);

            category.setCode(pstCategory.getString(FLD_CODE));
            category.setName(pstCategory.getString(FLD_NAME));
            category.setPointPrice(pstCategory.getdouble(FLD_POINT_PRICE));
            category.setDescription(pstCategory.getString(FLD_DESCRIPTION));
            //adding production id by Mirahu 20120511
            category.setLocationId(pstCategory.getlong(FLD_LOCATION_ID));
            category.setCatParentId(pstCategory.getlong(FLD_CAT_PARENT_ID));
            category.setCategoryId(pstCategory.getInt(FLD_CATEGORY));

            category.setStatus(pstCategory.getInt(FLD_STATUS));
            category.setTypeCategory(pstCategory.getInt(FLD_TYPE_CATEGORY));
            category.setKenaikanHarga(pstCategory.getInt(FLD_KENAIKAN_HARGA));

            return category;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCategory(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(Category category) throws DBException {
        try {
            PstCategory pstCategory = new PstCategory(0);

            pstCategory.setString(FLD_CODE, category.getCode());
            pstCategory.setString(FLD_NAME, category.getName());
            pstCategory.setDouble(FLD_POINT_PRICE, category.getPointPrice());
            pstCategory.setString(FLD_DESCRIPTION, category.getDescription());
            //adding production id by Mirahu 20120511
            pstCategory.setLong(FLD_LOCATION_ID, category.getLocationId());
            pstCategory.setLong(FLD_CAT_PARENT_ID, category.getCatParentId());
            pstCategory.setInt(FLD_STATUS, category.getStatus());
            pstCategory.setLong(FLD_CATEGORY, category.getCategoryId());
            pstCategory.setInt(FLD_TYPE_CATEGORY, category.getTypeCategory());
            pstCategory.setInt(FLD_KENAIKAN_HARGA, category.getKenaikanHarga());
            pstCategory.insert();
            long oidDataSync = PstDataSyncSql.insertExc(pstCategory.getInsertSQL());
            PstDataSyncStatus.insertExc(oidDataSync);

            category.setOID(pstCategory.getlong(FLD_CATEGORY_ID));
			
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstCategory.getInsertSQL());
			
			
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCategory(0), DBException.UNKNOWN);
        }
        return category.getOID();
    }

    public static long updateExc(Category category) throws DBException {
        try {
            if (category.getOID() != 0) {
                PstCategory pstCategory = new PstCategory(category.getOID());

                pstCategory.setString(FLD_CODE, category.getCode());
                pstCategory.setString(FLD_NAME, category.getName());
                pstCategory.setDouble(FLD_POINT_PRICE, category.getPointPrice());
                pstCategory.setString(FLD_DESCRIPTION, category.getDescription());
                //adding production id by Mirahu 20120511
                pstCategory.setLong(FLD_LOCATION_ID, category.getLocationId());
                pstCategory.setLong(FLD_CAT_PARENT_ID, category.getCatParentId());

                pstCategory.setInt(FLD_STATUS, category.getStatus());
                pstCategory.setLong(FLD_CATEGORY, category.getCategoryId());
                pstCategory.setInt(FLD_TYPE_CATEGORY, category.getTypeCategory());
                pstCategory.setInt(FLD_KENAIKAN_HARGA, category.getKenaikanHarga());
                pstCategory.update();

                long oidDataSync = PstDataSyncSql.insertExc(pstCategory.getUpdateSQL());
                PstDataSyncStatus.insertExc(oidDataSync);
				
				//kebutuhan untuk service transfer katalog
				PstDataCustom.insertDataForSyncAllLocation(pstCategory.getUpdateSQL());

                return category.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCategory(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstCategory pstCategory = new PstCategory(oid);
            pstCategory.delete();
            long oidDataSync = PstDataSyncSql.insertExc(pstCategory.getDeleteSQL());
            PstDataSyncStatus.insertExc(oidDataSync);
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstCategory.getDeleteSQL());
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCategory(0), DBException.UNKNOWN);
        }
        return oid;
    }
    
    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
      return list(limitStart, recordToGet, whereClause, order, null);
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order, String join) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_CATEGORY;
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (join != null && join != "") {
              sql += " JOIN " + join;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
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
                Category category = new Category();
                resultToObject(rs, category);
                lists.add(category);
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

    /**
     * list daftar category hastable
     *
     * @return
     */
    public static Hashtable getListCategoryHastable() {
        Hashtable lists = new Hashtable();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_CATEGORY;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Category category = new Category();
                resultToObject(rs, category);
                lists.put(category.getCode().toUpperCase(), category);
            }
            rs.close();
            return lists;
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Hashtable();
    }

    public static void resultToObject(ResultSet rs, Category category) {
        try {
            category.setOID(rs.getLong(PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID]));
            category.setCode(rs.getString(PstCategory.fieldNames[PstCategory.FLD_CODE]));
            category.setName(rs.getString(PstCategory.fieldNames[PstCategory.FLD_NAME]));
            category.setPointPrice(rs.getDouble(PstCategory.fieldNames[PstCategory.FLD_POINT_PRICE]));
            category.setDescription(rs.getString(PstCategory.fieldNames[PstCategory.FLD_DESCRIPTION]));
            //adding production id by Mirahu 20120511
            category.setLocationId(rs.getLong(PstCategory.fieldNames[PstCategory.FLD_LOCATION_ID]));
            category.setCatParentId(rs.getLong(PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID]));
            category.setStatus(rs.getInt(PstCategory.fieldNames[PstCategory.FLD_STATUS]));
            category.setCategoryId(rs.getInt(PstCategory.fieldNames[PstCategory.FLD_CATEGORY]));

            category.setTypeCategory(rs.getInt(PstCategory.fieldNames[PstCategory.FLD_TYPE_CATEGORY]));
            category.setKenaikanHarga(rs.getInt(PstCategory.fieldNames[PstCategory.FLD_KENAIKAN_HARGA]));

        } catch (Exception e) {
            System.out.println("error : " + e.toString());
        }
    }

    public static boolean checkOID(long categoryId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_CATEGORY + " WHERE "
                    + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + " = " + categoryId;

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

    public static boolean checkCategory(String code, int type) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "";

            if (type == 1) {//kode
                sql = "SELECT * FROM " + TBL_CATEGORY + " WHERE "
                        + PstCategory.fieldNames[PstCategory.FLD_CODE] + " = '" + code + "'";
            } else {//nama
                sql = "SELECT * FROM " + TBL_CATEGORY + " WHERE "
                        + PstCategory.fieldNames[PstCategory.FLD_NAME] + " = '" + code + "'";
            }

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
    
    public static int checkCategoryCount(String code, int type) {
        DBResultSet dbrs = null;
        int result = 0;
        try {
            String sql = "";

            if (type == 1) {//kode
                sql = "SELECT * FROM " + TBL_CATEGORY + " WHERE "
                        + PstCategory.fieldNames[PstCategory.FLD_CODE] + " = '" + code + "'";
            } else {//nama
                sql = "SELECT * FROM " + TBL_CATEGORY + " WHERE "
                        + PstCategory.fieldNames[PstCategory.FLD_NAME] + " = '" + code + "'";
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                result = result + 1;
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
            String sql = "SELECT COUNT(" + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + ") FROM " + TBL_CATEGORY;
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
                    Category category = (Category) list.get(ls);
                    if (oid == category.getOID()) {
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

    /**
     * this method uses in list in jsp
     */
    /*public static Vector getNestedCategory(long idDepartment, Vector result){
        try {
            String whereClause = PstCategory.fieldNames[PstCategory.FLD_DEPARTMENT_ID]+"="+idDepartment;
            Vector departments = PstCategory.list(0,0,whereClause,"");
            if((departments==null) || (departments.size()<1)) {
                return new Vector(1,1);
            }else{
                for(int pd=0; pd<departments.size(); pd++){
                    Category matDepartment = (Category)departments.get(pd);
                    idDepartment = matDepartment.getOID();
                    long parent = matDepartment.getParentId();
                    int indent = ifExist(result,parent);
                    matDepartment.setCode(indent +"/"+ matDepartment.getCode());
                    departments.setElementAt(matDepartment,0);
                    result.add(departments);
                    getNestedCategory(idDepartment,result);
                }
            }
            return result;
        }catch (Exception exc){
            return null;
        }
    }*/
    private static int ifExist(Vector result, long parent) {
        int indent = 0;
        for (int i = 0; i < result.size(); i++) {
            Vector temp = (Vector) result.get(i);
            Category category = (Category) temp.get(0);
            long oid = category.getOID();
            if (parent == oid) {
                String locCode = category.getCode();
                int idn = locCode.indexOf("/");
                int existIdn = 0;
                if (idn > 0) {
                    existIdn = Integer.parseInt(locCode.substring(0, idn));
                }
                indent = existIdn + 1;
            }
        }
        return indent;
    }

    //INTEGRASI HANOMAN
    //----------------------
    public long insertCategory(CategoryLink catLink) {
        Category ct = new Category();
        ct.setName(catLink.getName());
        ct.setCode(catLink.getName());

        try {
            long oid = PstCategory.insertExc(ct);
            return synchronizeOID(oid, catLink.getCategoryId());
        } catch (Exception e) {
        }

        return 0;
    }

    public long updateCategory(CategoryLink catLink) {
        Category ct = new Category();

        try {
            ct = PstCategory.fetchExc(catLink.getCategoryId());
            ct.setName(catLink.getName());

            return PstCategory.updateExc(ct);
        } catch (Exception e) {
        }

        return 0;
    }

    public long deleteCategory(CategoryLink catLink) {
        String where = PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + "=" + catLink.getCategoryId();
        Vector vct = PstMaterial.list(0, 0, where, null);
        if (vct != null && vct.size() > 0) {
            return 0;
        } else {
            try {
                PstCategory.deleteExc(catLink.getCategoryId());
            } catch (Exception e) {
            }
        }
        return catLink.getCategoryId();
    }

    public long synchronizeOID(long oldOID, long newOID) {
        String sql = "UPDATE " + TBL_CATEGORY
                + " SET " + fieldNames[FLD_CATEGORY_ID] + "=" + newOID
                + " WHERE " + fieldNames[FLD_CATEGORY_ID] + "=" + oldOID;
        try {
            DBHandler.execUpdate(sql);
        } catch (Exception e) {

        }

        return newOID;
    }

    public static Vector<Category> structureList(Vector<Category> list) {
        if (list == null || list.size() < 1) {
            return new Vector();
        }
        Vector<Category> resultTotal = new Vector();
        int idx = 0;
        for (int i = 0; i <= list.size(); i++) {
            Category cat = (Category) list.get(idx);
            if (0 == cat.getCatParentId()) {
                resultTotal.add(cat);
                list.remove(0);
                idx = 0;
                i = 0;
                //i=i>0?i-1:0;
            } else {
                boolean child = false;
                for (int k = 0; k < resultTotal.size(); k++) {
                    Category catParent = (Category) resultTotal.get(k);
                    if (catParent.getOID() == cat.getCatParentId()) {
                        resultTotal.insertElementAt(cat, k + 1);
                        list.remove(idx);
                        child = true;
                    }
                }
                if (child) {
                    idx = 0;
                    i = 0;
                } else {
                    idx = idx + 1;
                    i = 0;
                }
            }
        }
        return resultTotal;
    }

    public static Vector<Category> structureList(Vector<Category> list, long oid) {
        if (list == null || list.size() < 1) {
            return new Vector();
        }
        Vector<Category> resultTotal = new Vector();
        int idx = 0;
        try {
            for (int i = 0; i <= list.size(); i++) {
                Category cat = (Category) list.get(idx);
                if (oid == cat.getOID()) {
                    resultTotal.add(cat);
                    list.remove(idx);
                    idx = 0;
                    i = 0;
                    //i=i>0?i-1:0;
                } else {
                    boolean child = false;
                    for (int k = 0; k < resultTotal.size(); k++) {
                        Category catParent = (Category) resultTotal.get(k);
                        try {
                            if (catParent.getOID() == cat.getCatParentId()) {
                                resultTotal.insertElementAt(cat, k + 1);
                                list.remove(idx);
                                child = true;
                            }
                        } catch (Exception ex) {
                            System.out.print("xxxx");
                        }

                    }
                    if (child) {
                        idx = 0;
                        i = 0;
                    } else {
                        idx = idx + 1;
                        i = 0;
                    }
                }
            }
        } catch (Exception ex) {
            System.out.print("xxxx");
        }

        return resultTotal;
    }

    public static Vector<Category> structureListNew(Vector<Category> list) {
        if (list == null || list.size() < 1) {
            return new Vector();
        }
        Vector<Category> resultTotal = new Vector();

        for (int i = 0; i <= list.size(); i++) {
            Category cat = (Category) list.get(0);
            if (0 == cat.getCatParentId()) {
                resultTotal.add(cat);
                list.remove(0);
                i = 0;
                //i=i>0?i-1:0;
            } else {
                for (int k = 0; k < resultTotal.size(); k++) {
                    Category catParent = (Category) resultTotal.get(k);
                    if (catParent.getOID() == cat.getCatParentId()) {
                        Vector xxx = new Vector();
                        xxx.add(cat);
                        catParent.setResultChildParent(xxx);
                        resultTotal.remove(k);
                        resultTotal.insertElementAt(catParent, k);
                        list.remove(0);
                    }
                }
                i = 0;
            }
        }
        return resultTotal;
    }

    public static String drawstructureList(Vector<Category> list, String name, String attTag) {
        String htmlSelect = "";
        if (list == null || list.size() < 1) {
            return htmlSelect;
        }
        Vector<Category> resultTotal = new Vector();
        htmlSelect = "<select id=\"" + name + "\" " + " name=\"" + name + "\" " + attTag + " class=\"" + attTag + "\">\n";
        for (int i = 0; i <= list.size(); i++) {
            Category cat = (Category) list.get(0);
            if (0 == cat.getCatParentId()) {
                resultTotal.add(cat);
                list.remove(0);
                i = 0;
            } else {
                for (int k = 0; k < resultTotal.size(); k++) {
                    Category catParent = (Category) resultTotal.get(k);
                    if (catParent.getOID() == cat.getCatParentId()) {
                        resultTotal.insertElementAt(cat, k + 1);
                        list.remove(0);
                    }
                }
                i = 0;
            }
        }
        htmlSelect = htmlSelect + "</select>\n";
        return htmlSelect;
    }

    public static Vector<Category> structureListRec(Vector<Category> list, Vector<Category> resultTotal, int idx, Category parentCategory) {
        if (list == null || list.size() < 1) {
            return resultTotal;
        }
        if (resultTotal == null) {
            resultTotal = new Vector();
        }
        for (int i = idx; i < list.size(); i++) {
            Category cat = (Category) list.get(i);
            if (cat.getCatParentId() == 0 && parentCategory.getOID() == 0) {
                resultTotal.add(cat);
                structureListRec(list, resultTotal, 0, cat);
            } else if (parentCategory != null) {
                if (cat.getCatParentId() != 0 && cat.getCatParentId() == parentCategory.getOID()) {
                    resultTotal.add(cat);
                    structureListRec(list, resultTotal, 0, cat);
                } else {
                    structureListRec(list, resultTotal, i + 1, parentCategory);
                }
            }

        }
        return resultTotal;
    }

    //update opie-eyek untuk mendapatkan hastable
    //membuat hastable mencari code unit berdasarkan unitID
    public static long getIdFromParentId(long idCategory, long idParent) {
        long categoryId = 0;
        try {
            if (unitParentID == null) {
                loadAllCategoryByHash();
            }
            categoryId = (Long) unitParentID.get(idCategory + "_" + idParent);

        } catch (Exception e) {
            System.out.println("Exc getQtyPerBaseUnitByHash : " + e.toString());
        } finally {
            return categoryId;
        }
    }

    public static void loadAllCategoryByHash() {

        try {
            if (unitParentID == null) {
                unitParentID = new Hashtable();
            }
            Vector listCategory = list(0, 0, "", "");
            if (listCategory != null) {
                for (int i = 0; i < listCategory.size(); i++) {
                    Category category = (Category) listCategory.get(i);
                    unitParentID.put("" + category.getOID() + "_" + category.getCatParentId(), new Long(category.getOID()));
                }
            }
        } catch (Exception e) {
            System.out.println("Exc : " + e.toString());
        } finally {
            return;
        }
    }
    
    public static Category fetchByName(String name) {
        com.dimata.posbo.db.DBResultSet dbrs = null;
        Category cat = new Category();
        try {
            String sql = "SELECT * FROM " + TBL_CATEGORY +
                    " WHERE " + fieldNames[FLD_NAME] +
                    " = '" + name + "'";
            dbrs = com.dimata.posbo.db.DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                resultToObject(rs, cat);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            com.dimata.posbo.db.DBResultSet.close(dbrs);
        }
        return cat;
    }

    //--- end
    
            
    public static JSONObject fetchJSON(long oid){
            JSONObject object = new JSONObject();
            try {
                Category category = PstCategory.fetchExc(oid);
                object.put(PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID], category.getOID());
                object.put(PstCategory.fieldNames[PstCategory.FLD_CATEGORY], category.getCategoryId());
                object.put(PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID], category.getCatParentId());
                object.put(PstCategory.fieldNames[PstCategory.FLD_CODE], category.getCode());
                object.put(PstCategory.fieldNames[PstCategory.FLD_DESCRIPTION], category.getDescription());
                object.put(PstCategory.fieldNames[PstCategory.FLD_LOCATION_ID], category.getLocationId());
                object.put(PstCategory.fieldNames[PstCategory.FLD_NAME], category.getName());
                object.put(PstCategory.fieldNames[PstCategory.FLD_POINT_PRICE], category.getPointPrice());
                object.put(PstCategory.fieldNames[PstCategory.FLD_STATUS], category.getStatus());
                object.put(PstCategory.fieldNames[PstCategory.FLD_TYPE_CATEGORY], category.getTypeCategory());  
            }catch(Exception exc){}

            return object;
        }
    
}

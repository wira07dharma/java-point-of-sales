/**
 *
 * @author Witar
 */
package com.dimata.posbo.entity.masterdata;

import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.util.Command;
import com.dimata.util.lang.I_Language;
import java.sql.ResultSet;
import java.util.Vector;
import org.json.JSONObject;

public class PstNotaSetting
        extends DBHandler
        implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_NOTA_SETTING = "pos_nota_setting";
    public static final int FLD_POS_NOTA_SETTING_ID = 0;
    public static final int FLD_LOCATION_ID = 1;
    public static final int FLD_FOOTER_TEXT = 2;

    public static String[] fieldNames = {
        "POS_NOTA_SETTING_ID",
        "LOCATION_ID",
        "FOOTER_TEXT"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_STRING
    };

    public PstNotaSetting() {
    }

    public PstNotaSetting(int i) throws DBException {

        super(new PstNotaSetting());//merupakan induk constarktor dari DBHandler lalu membuat baru PstEmployee

    }

    public PstNotaSetting(String sOid) throws DBException {

        super(new PstNotaSetting(0));//merupakan induk construktor dari DBHandler lalu membuat new PstEmployee lalu memberi nilai defaoult 0

        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }

    }

    public PstNotaSetting(long lOid) throws DBException {

        super(new PstNotaSetting(0));//merupakan induk construktor dari DBHandler, 0 fungsinya sebagai default PSTGEJALA

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
        return TBL_NOTA_SETTING;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstNotaSetting().getClass().getName();
    }

    public static NotaSetting fetchExc(long oid) throws DBException {

        try {
            NotaSetting notaSetting = new NotaSetting();
            PstNotaSetting pstNotaSetting = new PstNotaSetting(oid);
            notaSetting.setOID(oid);
            notaSetting.setLocationId(pstNotaSetting.getlong(FLD_LOCATION_ID));
            notaSetting.setFooterText(pstNotaSetting.getString(FLD_FOOTER_TEXT));
            return notaSetting;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstNotaSetting(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity ent) throws Exception {
        NotaSetting notaSetting = fetchExc(ent.getOID());
        ent = (Entity) notaSetting;
        return notaSetting.getOID();
    }

    public static synchronized long updateExc(NotaSetting notaSetting) throws DBException {
        try {
            if (notaSetting.getOID() != 0) {
                PstNotaSetting pstNotaSetting = new PstNotaSetting(notaSetting.getOID());
                pstNotaSetting.setLong(FLD_LOCATION_ID, notaSetting.getLocationId());
                pstNotaSetting.setString(FLD_FOOTER_TEXT, notaSetting.getFooterText());
                pstNotaSetting.update();
                return notaSetting.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstNotaSetting(0), DBException.UNKNOWN);
        }
        return 0;

    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((NotaSetting) ent);
    }

    public static synchronized long deleteExc(long oid) throws DBException {

        try {
            PstNotaSetting pstNotaSetting = new PstNotaSetting(oid);
            pstNotaSetting.delete();

        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstNotaSetting(0), DBException.UNKNOWN);
        }
        return oid;

    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static synchronized long insertExc(NotaSetting notaSetting)
            throws DBException {
        try {

            PstNotaSetting pstNotaSetting = new PstNotaSetting(0);
            pstNotaSetting.setLong(FLD_LOCATION_ID, notaSetting.getLocationId());
            pstNotaSetting.setString(FLD_FOOTER_TEXT, notaSetting.getFooterText());
            pstNotaSetting.insert();

            notaSetting.setOID(pstNotaSetting.getlong(FLD_POS_NOTA_SETTING_ID));

        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstNotaSetting(0), DBException.UNKNOWN);
        }
        return notaSetting.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((NotaSetting) ent);
    }

    public static void resultToObject(ResultSet rs, NotaSetting notaSetting) {

        try {
            notaSetting.setOID(rs.getLong(fieldNames[FLD_POS_NOTA_SETTING_ID]));
            notaSetting.setLocationId(rs.getLong(fieldNames[FLD_LOCATION_ID]));
            notaSetting.setFooterText(rs.getString(fieldNames[FLD_FOOTER_TEXT]));
        } catch (Exception e) {
        }

    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {

        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_NOTA_SETTING;

            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }

            if (limitStart == 0 && recordToGet == 0) {
                sql = sql + "";
            } else {
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }

            dbrs = DBHandler.execQueryResult(sql);

            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                NotaSetting notaSetting = new NotaSetting();
                resultToObject(rs, notaSetting);
                lists.add(notaSetting);
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

    public static Vector listAll() {
        return list(0, 500, "", "");
    }

    public static boolean checkOID(long mSId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_NOTA_SETTING + " WHERE "
                    + fieldNames[FLD_POS_NOTA_SETTING_ID] + " = " + mSId;
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
            String sql = "SELECT COUNT(" + fieldNames[FLD_LOCATION_ID]
                    + ") FROM " + TBL_NOTA_SETTING;
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            dbrs = DBHandler.execQueryResult(sql);//execute query sql
            ResultSet rs = dbrs.getResultSet();
            int count = 0;
            while (rs.next()) {
                count = rs.getInt(1);//ambil isi ResultSet yg 1 atau PstEmployee.fieldNames[PstEmployee.FLD_JENIS_ITEM_ID] 
            }
            rs.close();
            return count;
        } catch (Exception e) {
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }

    public static int findLimitStart(long oid, int recordToGet, String whereClause, String orderClause) {
        int size = getCount(whereClause);
        int start = 0;
        boolean found = false;
        for (int i = 0; (i < size) && !found; i = i + recordToGet) {
            Vector list = list(i, recordToGet, whereClause, orderClause);
            start = i;
            if (list.size() > 0) {
                for (int ls = 0; ls < list.size(); ls++) {
                    NotaSetting notaSetting = (NotaSetting) list.get(ls);
                    if (oid == notaSetting.getOID()) {
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
                System.out.println("next.......................");
            } else {
                start = start - recordToGet;
                if (start > 0) {
                    cmd = Command.PREV;
                    System.out.println("prev.......................");
                }
            }
        }
        return cmd;
    }
    public static JSONObject fetchJSON(long oid){
            JSONObject object = new JSONObject();
            try {
                NotaSetting notaSetting = PstNotaSetting.fetchExc(oid);
                object.put(PstNotaSetting.fieldNames[PstNotaSetting.FLD_POS_NOTA_SETTING_ID], notaSetting.getOID());
                object.put(PstNotaSetting.fieldNames[PstNotaSetting.FLD_FOOTER_TEXT], notaSetting.getFooterText());
                object.put(PstNotaSetting.fieldNames[PstNotaSetting.FLD_LOCATION_ID], notaSetting.getLocationId());
            }catch(Exception exc){}

            return object;
        }
}

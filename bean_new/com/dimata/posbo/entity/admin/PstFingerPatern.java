package com.dimata.posbo.entity.admin;

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

public class PstFingerPatern 
extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language   {
    
    public static final String TBL_FINGER_PATERN = "finger_pattern";
    
    public static final int FLD_FINGER_PATERN_ID    = 0;
    public static final int FLD_EMPLOYEE_ID         = 1;
    public static final int FLD_FINGER_TYPE         = 2;
    public static final int FLD_FINGER_PATERN       = 3;
    
    public static  final String[] fieldNames ={
        "FINGER_PATTERN_ID",
        "EMPLOYEE_ID",
        "FINGER_TYPE",
        "FINGER_PATTERN"
    };
    
    public static int[] fieldTypes ={
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_INT,
        TYPE_STRING
    };
    
    public PstFingerPatern() {}

    public PstFingerPatern(int i) throws DBException {
        super(new PstFingerPatern());
    }

    public PstFingerPatern(String sOid) throws DBException {
        super(new PstFingerPatern(0));

        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstFingerPatern(long lOid) throws DBException {
        super(new PstFingerPatern(0));//merupakan induk construktor dari DBHandler, 0 fungsinya sebagai default PSTGEJALA

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
    
    @Override
    public int getFieldSize() {
        return fieldNames.length;
    }

    @Override
    public String getTableName() {
        return TBL_FINGER_PATERN;
    }

    @Override
    public String[] getFieldNames() {
        return fieldNames;
    }

    @Override
    public int[] getFieldTypes() {
        return fieldTypes;
    }

    @Override
    public String getPersistentName() {
        return new PstFingerPatern().getClass().getName();
    }
    
    public static FingerPatern fetchExc(long oid) throws DBException {

        try {
            FingerPatern fingerPatern = new FingerPatern();
            PstFingerPatern pstFingerPatern = new PstFingerPatern(oid);
            fingerPatern.setOID(oid);
            fingerPatern.setEmployeeId(pstFingerPatern.getlong(FLD_EMPLOYEE_ID));
            fingerPatern.setFingerType(pstFingerPatern.getInt(FLD_FINGER_TYPE));
            fingerPatern.setFingerPatern(pstFingerPatern.getString(FLD_FINGER_PATERN));
           
            return fingerPatern;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstFingerPatern(0), DBException.UNKNOWN);
        }
    }

    @Override
    public long fetchExc(Entity ent) throws Exception {
        FingerPatern fingerPatern = fetchExc(ent.getOID());
        ent = (Entity)fingerPatern;
        return fingerPatern.getOID();
    }
    
    public static synchronized long updateExc(FingerPatern fingerPatern) throws DBException {
        try {
            if (fingerPatern.getOID() != 0) {
                PstFingerPatern pstFingerPatern = new PstFingerPatern(fingerPatern.getOID());
                pstFingerPatern.setLong(FLD_EMPLOYEE_ID, fingerPatern.getEmployeeId());
                pstFingerPatern.setInt(FLD_FINGER_TYPE, fingerPatern.getFingerType());
                pstFingerPatern.setString(FLD_FINGER_PATERN, fingerPatern.getFingerPatern());
                pstFingerPatern.update();

                return fingerPatern.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstFingerPatern(0), DBException.UNKNOWN);
        }
        return 0;
    }

    @Override
    public long updateExc(Entity ent) throws Exception {
        return updateExc((FingerPatern)ent);
    }
    
    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstFingerPatern pstFingerPatern = new PstFingerPatern(oid);
            pstFingerPatern.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstFingerPatern(0), DBException.UNKNOWN);
        }
        return oid;
    }

    @Override
    public long deleteExc(Entity ent) throws Exception {
        if(ent==null){
            throw  new DBException(this,DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }
    
    public static synchronized long insertExc(FingerPatern fingerPatern) 
        throws DBException {
        try {

            PstFingerPatern pstFingerPatern = new PstFingerPatern(0);

            pstFingerPatern.setLong(FLD_EMPLOYEE_ID, fingerPatern.getEmployeeId());
            pstFingerPatern.setInt(FLD_FINGER_TYPE, fingerPatern.getFingerType());
            pstFingerPatern.setString(FLD_FINGER_PATERN, fingerPatern.getFingerPatern());
            pstFingerPatern.insert();

            fingerPatern.setOID(pstFingerPatern.getlong(FLD_EMPLOYEE_ID));

        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstFingerPatern(0), DBException.UNKNOWN);
        }
        return fingerPatern.getOID();
    }

    @Override
    public long insertExc(Entity ent) throws Exception {
        return insertExc((FingerPatern)ent);
    }
    
    public static void resultToObject(ResultSet rs, FingerPatern fingerPatern) {
        try {
            fingerPatern.setOID(rs.getLong(PstFingerPatern.fieldNames[PstFingerPatern.FLD_FINGER_PATERN_ID]));
            fingerPatern.setEmployeeId(rs.getLong(PstFingerPatern.fieldNames[PstFingerPatern.FLD_EMPLOYEE_ID]));
            fingerPatern.setFingerType(rs.getInt(PstFingerPatern.fieldNames[PstFingerPatern.FLD_FINGER_TYPE]));
            fingerPatern.setFingerPatern(rs.getString(PstFingerPatern.fieldNames[PstFingerPatern.FLD_FINGER_PATERN]));
        } catch (Exception e) {
        }
    }
    
    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;

        try {
            String sql = "SELECT * FROM " + TBL_FINGER_PATERN;

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
                FingerPatern fingerPatern = new FingerPatern();
                resultToObject(rs, fingerPatern);
                lists.add(fingerPatern);
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
    
    public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + PstFingerPatern.fieldNames[PstFingerPatern.FLD_FINGER_PATERN_ID] 
                    + ") FROM " + TBL_FINGER_PATERN;
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
                    FingerPatern fingerPatern = (FingerPatern) list.get(ls);
                    if (oid == fingerPatern.getOID()) {
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
        } else {
            if (start == (vectSize - recordToGet)) {
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
        }
        return cmd;
    }
 

   public static JSONObject fetchJSON(long oid){
      JSONObject object = new JSONObject();
      try {
         FingerPatern fingerPatern = PstFingerPatern.fetchExc(oid);
         object.put(PstFingerPatern.fieldNames[PstFingerPatern.FLD_FINGER_PATERN_ID], fingerPatern.getOID());
         object.put(PstFingerPatern.fieldNames[PstFingerPatern.FLD_EMPLOYEE_ID], fingerPatern.getEmployeeId());
         object.put(PstFingerPatern.fieldNames[PstFingerPatern.FLD_FINGER_TYPE], fingerPatern.getFingerType());
         object.put(PstFingerPatern.fieldNames[PstFingerPatern.FLD_EMPLOYEE_ID], fingerPatern.getFingerPatern());
      }catch(Exception exc){}
      return object;
   }  
}

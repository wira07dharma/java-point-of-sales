
package com.dimata.common.entity.finger;

/**
 *
 * @author Witar
 */

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

public class PstDeviceFinger 
extends DBHandler 
implements I_DBInterface,I_DBType,I_PersintentExc,I_Language{

    public static final String TBL_DEVICE_FINGER = "device_finger";
    public static final int FLD_DEVICE_ID = 0;
    public static final int FLD_DEVICE_NAME = 1;
    public static final int FLD_SN = 2;
    public static final int FLD_VC = 3;
    public static final int FLD_AC = 4;
    public static final int FLD_VKEY = 5;
    public static final int FLD_MAC_ADDRESS = 6;
    
    public static String [] fieldNames={
        "DEVICE_ID",
        "DEVICE_NAME",
        "SN",
        "VC",
        "AC",
        "VKEY",
        "MAC_ADDRESS"
    };
    
    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING
    };
    
    public PstDeviceFinger() {
    }

    public PstDeviceFinger(int i) throws DBException {
        super(new PstDeviceFinger());
    }

    public PstDeviceFinger(String sOid) throws DBException {
        super(new PstDeviceFinger(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstDeviceFinger(long lOid) throws DBException {
        super(new PstDeviceFinger(0));
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
        return TBL_DEVICE_FINGER;
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
        return new PstDeviceFinger().getClass().getName();
    }
    
    public static DeviceFinger fetchExc(long oid) throws DBException {

        try {
            DeviceFinger deviceFinger = new DeviceFinger();
            PstDeviceFinger pstDeviceFinger = new PstDeviceFinger(oid);
            deviceFinger.setOID(oid);
            deviceFinger.setDeviceName(pstDeviceFinger.getString(FLD_DEVICE_NAME));
            deviceFinger.setSn(pstDeviceFinger.getString(FLD_SN));
            deviceFinger.setVc(pstDeviceFinger.getString(FLD_VC));
            deviceFinger.setAc(pstDeviceFinger.getString(FLD_AC));
            deviceFinger.setvKey(pstDeviceFinger.getString(FLD_VKEY));
            deviceFinger.setMacAddress(pstDeviceFinger.getString(FLD_MAC_ADDRESS));
       
            return deviceFinger;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDeviceFinger(0), DBException.UNKNOWN);
        }
    }
    
    @Override
    public long fetchExc(Entity ent) throws Exception {
        DeviceFinger deviceFinger = fetchExc(ent.getOID());
        ent = (Entity)deviceFinger;
        return ent.getOID();
    }
    
    public static synchronized long updateExc(DeviceFinger deviceFinger) throws DBException {
        try {
            if (deviceFinger.getOID() != 0) {
                PstDeviceFinger pstDeviceFinger = new PstDeviceFinger(deviceFinger.getOID());
                pstDeviceFinger.setString(FLD_DEVICE_NAME, deviceFinger.getDeviceName());//getNamaEmployee = value();
                pstDeviceFinger.setString(FLD_SN, deviceFinger.getSn());
                pstDeviceFinger.setString(FLD_VC, deviceFinger.getVc());
                pstDeviceFinger.setString(FLD_AC, deviceFinger.getAc());
                pstDeviceFinger.setString(FLD_VKEY, deviceFinger.getvKey());
                pstDeviceFinger.setString(FLD_MAC_ADDRESS, deviceFinger.getMacAddress());//getNamaEmployee = value();
                pstDeviceFinger.update();

                return deviceFinger.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDeviceFinger(0), DBException.UNKNOWN);
        }
        return 0;
    }

    @Override
    public long updateExc(Entity ent) throws Exception {
        return updateExc((DeviceFinger)ent);
    }
    
    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstDeviceFinger pstDeviceFinger = new PstDeviceFinger(oid);
            pstDeviceFinger.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDeviceFinger(0), DBException.UNKNOWN);
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
    
    public static synchronized long insertExc(DeviceFinger deviceFinger) 
    throws DBException {
    try {

        PstDeviceFinger pstDeviceFinger = new PstDeviceFinger(0);
        pstDeviceFinger.setString(FLD_DEVICE_NAME, deviceFinger.getDeviceName());//getNamaEmployee = value();
        pstDeviceFinger.setString(FLD_SN, deviceFinger.getSn());
        pstDeviceFinger.setString(FLD_VC, deviceFinger.getVc());
        pstDeviceFinger.setString(FLD_AC, deviceFinger.getAc());
        pstDeviceFinger.setString(FLD_VKEY, deviceFinger.getvKey());//getNamaEmployee = value();
        pstDeviceFinger.setString(FLD_MAC_ADDRESS, deviceFinger.getMacAddress());
     
        pstDeviceFinger.insert();
        
        deviceFinger.setOID(pstDeviceFinger.getlong(FLD_DEVICE_ID));

        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDeviceFinger(0), DBException.UNKNOWN);
        }
        return deviceFinger.getOID();
    }

    @Override
    public long insertExc(Entity ent) throws Exception {
        return insertExc((DeviceFinger)ent);
    }
    
    public static void resultToObject(ResultSet rs, DeviceFinger deviceFinger) { 
        try {
            deviceFinger.setOID(rs.getLong(fieldNames[FLD_DEVICE_ID]));
            deviceFinger.setDeviceName(rs.getString(fieldNames[FLD_DEVICE_NAME]));
            deviceFinger.setSn(rs.getString(fieldNames[FLD_SN]));
            deviceFinger.setVc(rs.getString(fieldNames[FLD_VC]));
            deviceFinger.setAc(rs.getString(fieldNames[FLD_AC]));
            deviceFinger.setvKey(rs.getString(fieldNames[FLD_VKEY]));
            deviceFinger.setMacAddress(rs.getString(fieldNames[FLD_MAC_ADDRESS]));
            
        } catch (Exception e) {
        }

    }
    
    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = " SELECT * FROM " + TBL_DEVICE_FINGER;

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
                DeviceFinger deviceFinger = new DeviceFinger();
                resultToObject(rs, deviceFinger);
                lists.add(deviceFinger);
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
            String sql = "SELECT * FROM " + TBL_DEVICE_FINGER + " WHERE "
                    + fieldNames[FLD_DEVICE_ID] + " = " + mSId;
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
            String sql = "SELECT COUNT(" + fieldNames[FLD_DEVICE_ID] 
                    + ") FROM " + TBL_DEVICE_FINGER;
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            dbrs = DBHandler.execQueryResult(sql);//execute query sql
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
    
    public static int findLimitStart(long oid, int recordToGet, String whereClause, String orderClause) {
        int size = getCount(whereClause);
        int start = 0;
        boolean found = false;
        for (int i = 0; (i < size) && !found; i = i + recordToGet) {
            Vector list = list(i, recordToGet, whereClause, orderClause);
            start = i;
            if (list.size() > 0) {
                for (int ls = 0; ls < list.size(); ls++) {
                    DeviceFinger deviceFinger = (DeviceFinger) list.get(ls);
                    if (oid == deviceFinger.getOID()) {
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
    
}

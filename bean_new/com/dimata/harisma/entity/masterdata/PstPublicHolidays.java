/**
 * User: wardana
 * Date: Apr 8, 2004
 * Time: 8:48:43 AM
 * Version: 1.0
 */
package com.dimata.harisma.entity.masterdata;

import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;



import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.qdep.entity.Entity;
import com.dimata.util.lang.I_Language;
import com.dimata.util.Command;
import com.dimata.util.Formater;

import java.util.Vector;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PstPublicHolidays extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_PUBLIC_HOLIDAYS = "HR_PUBLIC_HOLIDAYS";

    public static final int FLD_PUBLIC_HOLIDAY_ID = 0;
    public static final int FLD_HOLIDAY_DATE = 1;
    public static final int FLD_HOLIDAY_DESC = 2;
    public static final int FLD_HOLIDAY_STATUS = 3;

    public static final String[] fieldNames = {
        "PUBLIC_HOLIDAY_ID",
        "HOLIDAY_DATE",
        "HOLIDAY_DESC",
        "HOLIDAY_STATUS"
    };

    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_DATE,
        TYPE_STRING,
        TYPE_INT
    };

    public static int STS_BOTH = 1;
    public static int STS_HINDU = 2;
    public static int STS_NON_HINDU = 3;

    public static String HINDU_STR = "Hindu";

    public static String[] stHolidaySts = {
        "None", "Both", "Hindu", "Non Hindu"
    };

    public PstPublicHolidays() {
    }

    public PstPublicHolidays(int i) throws DBException {
        super(new PstPublicHolidays());
    }

    public PstPublicHolidays(String sOid) throws DBException {
        super(new PstPublicHolidays(0));
        if (!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }

    public PstPublicHolidays(long lOid) throws DBException {
        super(new PstPublicHolidays(0));
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
        return TBL_PUBLIC_HOLIDAYS;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstPublicHolidays().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        PublicHolidays objPublicHolidays = fetchExc(ent.getOID());
        return objPublicHolidays.getOID();
    }

    public static PublicHolidays fetchExc(long oid) throws DBException {
        try {
            PublicHolidays objPublicHolidays = new PublicHolidays();
            PstPublicHolidays objPstPublicHolidays = new PstPublicHolidays(oid);
            objPublicHolidays.setOID(oid);

            objPublicHolidays.setDtHolidayDate(objPstPublicHolidays.getDate(FLD_HOLIDAY_DATE));
            objPublicHolidays.setStDesc(objPstPublicHolidays.getString(FLD_HOLIDAY_DESC));
            objPublicHolidays.setiHolidaySts(objPstPublicHolidays.getInt(FLD_HOLIDAY_STATUS));

            return objPublicHolidays;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPublicHolidays(0), DBException.UNKNOWN);
        }
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((PublicHolidays) ent);
    }

    public static long updateExc(PublicHolidays objPublicHolidays) throws DBException {
        try {
            if (objPublicHolidays.getOID() != 0) {
                PstPublicHolidays objPstPublicHolidays = new PstPublicHolidays(objPublicHolidays.getOID());

                objPstPublicHolidays.setDate(FLD_HOLIDAY_DATE, objPublicHolidays.getDtHolidayDate());
                objPstPublicHolidays.setString(FLD_HOLIDAY_DESC, objPublicHolidays.getStDesc());
                objPstPublicHolidays.setInt(FLD_HOLIDAY_STATUS, objPublicHolidays.getiHolidaySts());

                objPstPublicHolidays.update();
                return objPublicHolidays.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPublicHolidays(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long deleteExc(Entity ent) throws Exception {
        return deleteExc(ent.getOID());
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstPublicHolidays objPstPublicHolidays = new PstPublicHolidays(oid);
            objPstPublicHolidays.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPublicHolidays(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((PublicHolidays) ent);
    }

    public static long insertExc(PublicHolidays objPublicHolidays) throws DBException {
        try {
            PstPublicHolidays objPstPublicHolidays = new PstPublicHolidays(0);

            objPstPublicHolidays.setDate(FLD_HOLIDAY_DATE, objPublicHolidays.getDtHolidayDate());
            objPstPublicHolidays.setString(FLD_HOLIDAY_DESC, objPublicHolidays.getStDesc());
            objPstPublicHolidays.setInt(FLD_HOLIDAY_STATUS, objPublicHolidays.getiHolidaySts());

            objPstPublicHolidays.insert();
            objPublicHolidays.setOID(objPstPublicHolidays.getlong(FLD_PUBLIC_HOLIDAY_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPublicHolidays(0), DBException.UNKNOWN);
        }
        return objPublicHolidays.getOID();
    }

    public static Vector listAll() {
        return list(0, 500, "", "");
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_PUBLIC_HOLIDAYS;
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            if (order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;
            if (limitStart == 0 && recordToGet == 0)
                sql = sql + "";
            else
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                PublicHolidays objPublicHolidays = new PublicHolidays();
                resultToObject(rs, objPublicHolidays);
                lists.add(objPublicHolidays);
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

    private static void resultToObject(ResultSet rs, PublicHolidays objPublicHolidays) {
        try {
            objPublicHolidays.setOID(rs.getLong(PstPublicHolidays.fieldNames[PstPublicHolidays.FLD_PUBLIC_HOLIDAY_ID]));
            objPublicHolidays.setDtHolidayDate(rs.getDate(PstPublicHolidays.fieldNames[PstPublicHolidays.FLD_HOLIDAY_DATE]));
            objPublicHolidays.setStDesc(rs.getString(PstPublicHolidays.fieldNames[PstPublicHolidays.FLD_HOLIDAY_DESC]));
            objPublicHolidays.setiHolidaySts(rs.getInt(PstPublicHolidays.fieldNames[PstPublicHolidays.FLD_HOLIDAY_STATUS]));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean checkOID(long lHolidayId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_PUBLIC_HOLIDAYS + " WHERE " +
						PstPublicHolidays.fieldNames[PstPublicHolidays.FLD_PUBLIC_HOLIDAY_ID] + " = " + lHolidayId;

			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();

			while(rs.next()) { result = true; }
			rs.close();
		}catch(Exception e){
			System.out.println("err : "+e.toString());
		}finally{
			DBResultSet.close(dbrs);
			return result;
		}
	}

    public static int getCount(String whereClause){
		DBResultSet dbrs = null;
		try {
			String sql = "SELECT COUNT("+ PstPublicHolidays.fieldNames[PstPublicHolidays.FLD_PUBLIC_HOLIDAY_ID] + ") FROM " + TBL_PUBLIC_HOLIDAYS;
			if(whereClause != null && whereClause.length() > 0)
				sql = sql + " WHERE " + whereClause;

			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();

			int count = 0;
			while(rs.next()) { count = rs.getInt(1); }

			rs.close();
			return count;
		}catch(Exception e) {
			return 0;
		}finally {
			DBResultSet.close(dbrs);
		}
	}

    public static int findLimitStart( long oid, int recordToGet, String whereClause, String orderClause){
		int size = getCount(whereClause);
		int start = 0;
		boolean found =false;
		for(int i=0; (i < size) && !found ; i=i+recordToGet){
			 Vector list =  list(i,recordToGet, whereClause, orderClause);
			 start = i;
			 if(list.size()>0){
			  for(int ls=0;ls<list.size();ls++){
			  	   PublicHolidays objPublicHolidays = (PublicHolidays)list.get(ls);
				   if(oid == objPublicHolidays.getOID())
					  found=true;
			  }
		  }
		}
		if((start >= size) && (size > 0))
		    start = start - recordToGet;

		return start;
	}

    public static int findLimitCommand(int start, int recordToGet, int vectSize){
		 int cmd = Command.LIST;
		 int mdl = vectSize % recordToGet;
		 vectSize = vectSize + (recordToGet - mdl);
		 if(start == 0)
			 cmd =  Command.FIRST;
		 else{
			 if(start == (vectSize-recordToGet))
				 cmd = Command.LAST;
			 else{
				 start = start + recordToGet;
				 if(start <= (vectSize - recordToGet)){
					 cmd = Command.NEXT;
					 System.out.println("next.......................");
				 }else{
					 start = start - recordToGet;
					 if(start > 0){
						 cmd = Command.PREV;
						 System.out.println("prev.......................");
					 }
				 }
			 }
		 }

		 return cmd;
	}

    public static Vector getHolidayByDate(Date dtHolidayDate){
        Vector vList = new Vector();
        PublicHolidays objPublicHolidays = new PublicHolidays();
        DBResultSet dbrs;
        String stSQL = " SELECT * FROM "+ TBL_PUBLIC_HOLIDAYS +
                       " WHERE "+ PstPublicHolidays.fieldNames[PstPublicHolidays.FLD_HOLIDAY_DATE] +
                       " = '" + Formater.formatDate(dtHolidayDate, "yyyy-MM-dd")+"'";
        System.out.println("stSQL : "+stSQL);
        try{
            dbrs = DBHandler.execQueryResult(stSQL);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                objPublicHolidays = new PublicHolidays();
                resultToObject(rs, objPublicHolidays);
                vList.add(objPublicHolidays);
            }
        }
        catch(DBException dbe){
            dbe.printStackTrace();
        }
        catch(SQLException sqle){
            sqle.printStackTrace();
        }
        return vList;
    }


}

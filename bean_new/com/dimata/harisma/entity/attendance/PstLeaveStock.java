
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

package com.dimata.harisma.entity.attendance; 

/* package java */ 
import java.sql.*
;import java.util.*
;
/* package qdep */
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;



import com.dimata.qdep.entity.*;

/* package harisma */
//import com.dimata.harisma.db.DBHandler;
//import com.dimata.harisma.db.DBException;
//import com.dimata.harisma.db.DBLogger;
import com.dimata.util.lang.I_Language;

public class PstLeaveStock extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_LEAVE_STOCK = "HR_LEAVE_STOCK";

	public static final  int FLD_LEAVE_STOCK_ID = 0;
	public static final  int FLD_LEAVE_PERIOD_ID = 1;
	public static final  int FLD_EMPLOYEE_ID = 2;
	public static final  int FLD_AL_AMOUNT = 3;
	public static final  int FLD_LL_AMOUNT = 4;
	public static final  int FLD_DP_AMOUNT = 5;
	public static final  int FLD_ADD_AL = 6;
	public static final  int FLD_ADD_LL = 7;
	public static final  int FLD_ADD_DP = 8;

	public static final  String[] fieldNames = {
		"LEAVE_STOCK_ID",
		"LEAVE_PERIOD_ID",
		"EMPLOYEE_ID",
		"AL_AMOUNT",
		"LL_AMOUNT",
		"DP_AMOUNT",
		"ADD_AL",
		"ADD_LL",
		"ADD_DP"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_LONG,
		TYPE_LONG,
		TYPE_INT,
		TYPE_INT,
		TYPE_INT,
		TYPE_INT,
		TYPE_INT,
		TYPE_INT
	 }; 

	public PstLeaveStock(){
	}

	public PstLeaveStock(int i) throws DBException { 
		super(new PstLeaveStock()); 
	}

	public PstLeaveStock(String sOid) throws DBException { 
		super(new PstLeaveStock(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstLeaveStock(long lOid) throws DBException { 
		super(new PstLeaveStock(0)); 
		String sOid = "0"; 
		try { 
			sOid = String.valueOf(lOid); 
		}catch(Exception e) { 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	} 

	public int getFieldSize(){ 
		return fieldNames.length; 
	}

	public String getTableName(){ 
		return TBL_HR_LEAVE_STOCK;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstLeaveStock().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		LeaveStock leavestock = fetchExc(ent.getOID()); 
		ent = (Entity)leavestock; 
		return leavestock.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((LeaveStock) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((LeaveStock) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static LeaveStock fetchExc(long oid) throws DBException{ 
		try{ 
			LeaveStock leavestock = new LeaveStock();
			PstLeaveStock pstLeaveStock = new PstLeaveStock(oid); 
			leavestock.setOID(oid);

			leavestock.setLeavePeriodId(pstLeaveStock.getlong(FLD_LEAVE_PERIOD_ID));
			leavestock.setEmployeeId(pstLeaveStock.getlong(FLD_EMPLOYEE_ID));
			leavestock.setAlAmount(pstLeaveStock.getInt(FLD_AL_AMOUNT));
			leavestock.setLlAmount(pstLeaveStock.getInt(FLD_LL_AMOUNT));
			leavestock.setDpAmount(pstLeaveStock.getInt(FLD_DP_AMOUNT));
			leavestock.setAddAl(pstLeaveStock.getInt(FLD_ADD_AL));
			leavestock.setAddLl(pstLeaveStock.getInt(FLD_ADD_LL));
			leavestock.setAddDp(pstLeaveStock.getInt(FLD_ADD_DP));


			return leavestock; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstLeaveStock(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(LeaveStock leavestock) throws DBException{ 
		try{ 
			PstLeaveStock pstLeaveStock = new PstLeaveStock(0);

			pstLeaveStock.setLong(FLD_LEAVE_PERIOD_ID, leavestock.getLeavePeriodId());
			pstLeaveStock.setLong(FLD_EMPLOYEE_ID, leavestock.getEmployeeId());
			pstLeaveStock.setInt(FLD_AL_AMOUNT, leavestock.getAlAmount());
			pstLeaveStock.setInt(FLD_LL_AMOUNT, leavestock.getLlAmount());
			pstLeaveStock.setInt(FLD_DP_AMOUNT, leavestock.getDpAmount());
			pstLeaveStock.setInt(FLD_ADD_AL, leavestock.getAddAl());
			pstLeaveStock.setInt(FLD_ADD_LL, leavestock.getAddLl());
			pstLeaveStock.setInt(FLD_ADD_DP, leavestock.getAddDp());


			pstLeaveStock.insert(); 
			leavestock.setOID(pstLeaveStock.getlong(FLD_LEAVE_STOCK_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstLeaveStock(0),DBException.UNKNOWN); 
		}
		return leavestock.getOID();
	}

	public static long updateExc(LeaveStock leavestock) throws DBException{ 
		try{ 
			if(leavestock.getOID() != 0){ 
				PstLeaveStock pstLeaveStock = new PstLeaveStock(leavestock.getOID());

				pstLeaveStock.setLong(FLD_LEAVE_PERIOD_ID, leavestock.getLeavePeriodId());
				pstLeaveStock.setLong(FLD_EMPLOYEE_ID, leavestock.getEmployeeId());
				pstLeaveStock.setInt(FLD_AL_AMOUNT, leavestock.getAlAmount());
				pstLeaveStock.setInt(FLD_LL_AMOUNT, leavestock.getLlAmount());
				pstLeaveStock.setInt(FLD_DP_AMOUNT, leavestock.getDpAmount());
				pstLeaveStock.setInt(FLD_ADD_AL, leavestock.getAddAl());
				pstLeaveStock.setInt(FLD_ADD_LL, leavestock.getAddLl());
				pstLeaveStock.setInt(FLD_ADD_DP, leavestock.getAddDp());

				pstLeaveStock.update();
				return leavestock.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstLeaveStock(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstLeaveStock pstLeaveStock = new PstLeaveStock(oid);
			pstLeaveStock.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstLeaveStock(0),DBException.UNKNOWN); 
		}
		return oid;
	}

	public static Vector listAll(){ 
		return list(0, 500, "",""); 
	}

	public static Vector list(int limitStart,int recordToGet, String whereClause, String order){
		Vector lists = new Vector(); 
		DBResultSet dbrs = null;
		try {
			String sql = "SELECT * FROM " + TBL_HR_LEAVE_STOCK; 
			if(whereClause != null && whereClause.length() > 0)
				sql = sql + " WHERE " + whereClause;
			if(order != null && order.length() > 0)
				sql = sql + " ORDER BY " + order;
			if(limitStart == 0 && recordToGet == 0)
				sql = sql + "";
			else
				sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;
			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();
			while(rs.next()) {
				LeaveStock leavestock = new LeaveStock();
				resultToObject(rs, leavestock);
				lists.add(leavestock);
			}
			rs.close();
			return lists;

		}catch(Exception e) {
			System.out.println(e);
		}finally {
			DBResultSet.close(dbrs);
		}
			return new Vector();
	}

	public static void resultToObject(ResultSet rs, LeaveStock leavestock){
		try{
			leavestock.setOID(rs.getLong(PstLeaveStock.fieldNames[PstLeaveStock.FLD_LEAVE_STOCK_ID]));
			leavestock.setLeavePeriodId(rs.getLong(PstLeaveStock.fieldNames[PstLeaveStock.FLD_LEAVE_PERIOD_ID]));
			leavestock.setEmployeeId(rs.getLong(PstLeaveStock.fieldNames[PstLeaveStock.FLD_EMPLOYEE_ID]));
			leavestock.setAlAmount(rs.getInt(PstLeaveStock.fieldNames[PstLeaveStock.FLD_AL_AMOUNT]));
			leavestock.setLlAmount(rs.getInt(PstLeaveStock.fieldNames[PstLeaveStock.FLD_LL_AMOUNT]));
			leavestock.setDpAmount(rs.getInt(PstLeaveStock.fieldNames[PstLeaveStock.FLD_DP_AMOUNT]));
			leavestock.setAddAl(rs.getInt(PstLeaveStock.fieldNames[PstLeaveStock.FLD_ADD_AL]));
			leavestock.setAddLl(rs.getInt(PstLeaveStock.fieldNames[PstLeaveStock.FLD_ADD_LL]));
			leavestock.setAddDp(rs.getInt(PstLeaveStock.fieldNames[PstLeaveStock.FLD_ADD_DP]));


		}catch(Exception e){ }
	}

	public static boolean checkOID(long leaveStockId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_LEAVE_STOCK + " WHERE " + 
						PstLeaveStock.fieldNames[PstLeaveStock.FLD_LEAVE_STOCK_ID] + " = " + leaveStockId;

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
			String sql = "SELECT COUNT("+ PstLeaveStock.fieldNames[PstLeaveStock.FLD_LEAVE_STOCK_ID] + ") FROM " + TBL_HR_LEAVE_STOCK;
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


	/* This method used to find current data */
	public static int findLimitStart( long oid, int recordToGet, String whereClause){
		String order = "";
		int size = getCount(whereClause);
		int start = 0;
		boolean found =false;
		for(int i=0; (i < size) && !found ; i=i+recordToGet){
			 Vector list =  list(i,recordToGet, whereClause, order); 
			 start = i;
			 if(list.size()>0){
			  for(int ls=0;ls<list.size();ls++){ 
			  	   LeaveStock leavestock = (LeaveStock)list.get(ls);
				   if(oid == leavestock.getOID())
					  found=true;
			  }
		  }
		}
		if((start >= size) && (size > 0))
		    start = start - recordToGet;

		return start;
	}

    public static LeaveStock getLeaveStock(long lLeavePeriodId, long lEmployeeId){
        DBResultSet dbrs;
        LeaveStock objLeaveStock = new LeaveStock();
        String stSQL = "SELECT * FROM "+TBL_HR_LEAVE_STOCK +
                       " WHERE "+PstLeaveStock.fieldNames[PstLeaveStock.FLD_LEAVE_PERIOD_ID] + " = " + lLeavePeriodId +
                       " AND "+PstLeaveStock.fieldNames[PstLeaveStock.FLD_EMPLOYEE_ID] + " = " + lEmployeeId;

        try{
            dbrs = DBHandler.execQueryResult(stSQL);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                resultToObject(rs, objLeaveStock);
            }
        }
        catch(DBException dbe){
            dbe.printStackTrace();
        }
        catch(SQLException sqle){
            sqle.printStackTrace();
        }

        return objLeaveStock;
    }
}

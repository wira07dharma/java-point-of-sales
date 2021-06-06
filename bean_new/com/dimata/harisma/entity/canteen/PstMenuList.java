
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

package com.dimata.harisma.entity.canteen; 

/* package java */ 
import java.io.*
;
import java.sql.*
;import java.util.*
;import java.util.Date;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.util.*;
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
import com.dimata.harisma.entity.canteen.*; 

public class PstMenuList extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_MENU_LIST = "HR_MENU_LIST";

	public static final  int FLD_MENU_LIST_ID = 0;
	public static final  int FLD_MENU_ITEM_ID = 1;
	public static final  int FLD_MENU_DATE = 2;
	public static final  int FLD_MENU_TIME = 3;

	public static final  String[] fieldNames = {
		"MENU_LIST_ID",
		"MENU_ITEM_ID",
		"MENU_DATE",
		"MENU_TIME"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_LONG,
		TYPE_DATE,
		TYPE_INT
	 }; 

	public PstMenuList(){
	}

	public PstMenuList(int i) throws DBException { 
		super(new PstMenuList()); 
	}

	public PstMenuList(String sOid) throws DBException { 
		super(new PstMenuList(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstMenuList(long lOid) throws DBException { 
		super(new PstMenuList(0)); 
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
		return TBL_HR_MENU_LIST;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstMenuList().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		MenuList menulist = fetchExc(ent.getOID()); 
		ent = (Entity)menulist; 
		return menulist.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((MenuList) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((MenuList) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static MenuList fetchExc(long oid) throws DBException{ 
		try{ 
			MenuList menulist = new MenuList();
			PstMenuList pstMenuList = new PstMenuList(oid); 
			menulist.setOID(oid);

			menulist.setMenuItemId(pstMenuList.getlong(FLD_MENU_ITEM_ID));
			menulist.setMenuDate(pstMenuList.getDate(FLD_MENU_DATE));
			menulist.setMenuTime(pstMenuList.getInt(FLD_MENU_TIME));

			return menulist; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstMenuList(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(MenuList menulist) throws DBException{ 
		try{ 
			PstMenuList pstMenuList = new PstMenuList(0);

			pstMenuList.setLong(FLD_MENU_ITEM_ID, menulist.getMenuItemId());
			pstMenuList.setDate(FLD_MENU_DATE, menulist.getMenuDate());
			pstMenuList.setInt(FLD_MENU_TIME, menulist.getMenuTime());

			pstMenuList.insert(); 
			menulist.setOID(pstMenuList.getlong(FLD_MENU_LIST_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstMenuList(0),DBException.UNKNOWN); 
		}
		return menulist.getOID();
	}

	public static long updateExc(MenuList menulist) throws DBException{ 
		try{ 
			if(menulist.getOID() != 0){ 
				PstMenuList pstMenuList = new PstMenuList(menulist.getOID());

				pstMenuList.setLong(FLD_MENU_ITEM_ID, menulist.getMenuItemId());
				pstMenuList.setDate(FLD_MENU_DATE, menulist.getMenuDate());
				pstMenuList.setInt(FLD_MENU_TIME, menulist.getMenuTime());

				pstMenuList.update(); 
				return menulist.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstMenuList(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstMenuList pstMenuList = new PstMenuList(oid);
			pstMenuList.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstMenuList(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_MENU_LIST; 
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
				MenuList menulist = new MenuList();
				resultToObject(rs, menulist);
				lists.add(menulist);
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

	public static void resultToObject(ResultSet rs, MenuList menulist){
		try{
			menulist.setOID(rs.getLong(PstMenuList.fieldNames[PstMenuList.FLD_MENU_LIST_ID]));
			menulist.setMenuItemId(rs.getLong(PstMenuList.fieldNames[PstMenuList.FLD_MENU_ITEM_ID]));
			menulist.setMenuDate(rs.getDate(PstMenuList.fieldNames[PstMenuList.FLD_MENU_DATE]));
			menulist.setMenuTime(rs.getInt(PstMenuList.fieldNames[PstMenuList.FLD_MENU_TIME]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long menuListId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_MENU_LIST + " WHERE " + 
						PstMenuList.fieldNames[PstMenuList.FLD_MENU_LIST_ID] + " = " + menuListId;

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
			String sql = "SELECT COUNT("+ PstMenuList.fieldNames[PstMenuList.FLD_MENU_LIST_ID] + ") FROM " + TBL_HR_MENU_LIST;
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
	public static int findLimitStart( long oid, int recordToGet, String whereClause, String orderClause){
		int size = getCount(whereClause);
		int start = 0;
		boolean found =false;
		for(int i=0; (i < size) && !found ; i=i+recordToGet){
			 Vector list =  list(i,recordToGet, whereClause, orderClause); 
			 start = i;
			 if(list.size()>0){
			  for(int ls=0;ls<list.size();ls++){ 
			  	   MenuList menulist = (MenuList)list.get(ls);
				   if(oid == menulist.getOID())
					  found=true;
			  }
		  }
		}
		if((start >= size) && (size > 0))
		    start = start - recordToGet;

		return start;
	}
	/* This method used to find command where current data */
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
}

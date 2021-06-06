
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

public class PstMenuItem extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_MENU_ITEM = "HR_MENU_ITEM";

	public static final  int FLD_MENU_ITEM_ID = 0;
	public static final  int FLD_ITEM_NAME = 1;

	public static final  String[] fieldNames = {
		"MENU_ITEM_ID",
		"ITEM_NAME"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_STRING
	 }; 

	public PstMenuItem(){
	}

	public PstMenuItem(int i) throws DBException { 
		super(new PstMenuItem()); 
	}

	public PstMenuItem(String sOid) throws DBException { 
		super(new PstMenuItem(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstMenuItem(long lOid) throws DBException { 
		super(new PstMenuItem(0)); 
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
		return TBL_HR_MENU_ITEM;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstMenuItem().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		MenuItem menuitem = fetchExc(ent.getOID()); 
		ent = (Entity)menuitem; 
		return menuitem.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((MenuItem) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((MenuItem) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static MenuItem fetchExc(long oid) throws DBException{ 
		try{ 
			MenuItem menuitem = new MenuItem();
			PstMenuItem pstMenuItem = new PstMenuItem(oid); 
			menuitem.setOID(oid);

			menuitem.setItemName(pstMenuItem.getString(FLD_ITEM_NAME));

			return menuitem; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstMenuItem(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(MenuItem menuitem) throws DBException{ 
		try{ 
			PstMenuItem pstMenuItem = new PstMenuItem(0);

			pstMenuItem.setString(FLD_ITEM_NAME, menuitem.getItemName());

			pstMenuItem.insert(); 
			menuitem.setOID(pstMenuItem.getlong(FLD_MENU_ITEM_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstMenuItem(0),DBException.UNKNOWN); 
		}
		return menuitem.getOID();
	}

	public static long updateExc(MenuItem menuitem) throws DBException{ 
		try{ 
			if(menuitem.getOID() != 0){ 
				PstMenuItem pstMenuItem = new PstMenuItem(menuitem.getOID());

				pstMenuItem.setString(FLD_ITEM_NAME, menuitem.getItemName());

				pstMenuItem.update(); 
				return menuitem.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstMenuItem(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstMenuItem pstMenuItem = new PstMenuItem(oid);
			pstMenuItem.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstMenuItem(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_MENU_ITEM; 
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
				MenuItem menuitem = new MenuItem();
				resultToObject(rs, menuitem);
				lists.add(menuitem);
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

	public static void resultToObject(ResultSet rs, MenuItem menuitem){
		try{
			menuitem.setOID(rs.getLong(PstMenuItem.fieldNames[PstMenuItem.FLD_MENU_ITEM_ID]));
			menuitem.setItemName(rs.getString(PstMenuItem.fieldNames[PstMenuItem.FLD_ITEM_NAME]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long menuItemId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_MENU_ITEM + " WHERE " + 
						PstMenuItem.fieldNames[PstMenuItem.FLD_MENU_ITEM_ID] + " = " + menuItemId;

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
			String sql = "SELECT COUNT("+ PstMenuItem.fieldNames[PstMenuItem.FLD_MENU_ITEM_ID] + ") FROM " + TBL_HR_MENU_ITEM;
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
			  	   MenuItem menuitem = (MenuItem)list.get(ls);
				   if(oid == menuitem.getOID())
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

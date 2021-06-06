/*
 * PstRecipe2.java
 *
 * Created on May 18, 2004, 2:03 PM
 */

package com.dimata.pos.entity.billing;

import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;

/* package java */ 

/* package qdep */
import java.sql.ResultSet;
import java.util.Vector;

//import com.dimata.qdep.db.*;

/* package taiga */
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.util.lang.I_Language;

public class PstRecipe extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_CASH_RECIPE = "CASH_RECIPE";

	public static final  int FLD_CASH_RECIPE_ID = 0;
	public static final  int FLD_CASH_BILL_MAIN_ID = 1;
	public static final  int FLD_RECIPE_NUMBER = 2;
	public static final  int FLD_DOCTOR_NAME = 3;
	public static final  int FLD_PATIENT_NAME = 4;
	public static final  int FLD_RECIPE_SERVICE = 5;

	public static final  String[] fieldNames = {
		"CASH_RECIPE_ID",
		"CASH_BILL_MAIN_ID",
		"RECIPE_NUMBER",
		"DOCTOR_NAME",
		"PATIENT_NAME",
		"RECIPE_SERVICE"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_LONG,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_FLOAT
	 }; 

	public PstRecipe(){
	}

	public PstRecipe(int i) throws DBException { 
		super(new PstRecipe()); 
	}

	public PstRecipe(String sOid) throws DBException { 
		super(new PstRecipe(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstRecipe(long lOid) throws DBException { 
		super(new PstRecipe(0)); 
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
		return TBL_CASH_RECIPE;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstRecipe().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		Recipe recipe = fetchExc(ent.getOID()); 
		ent = (Entity)recipe; 
		return recipe.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((Recipe) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((Recipe) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static Recipe fetchExc(long oid) throws DBException{ 
		try{ 
			Recipe recipe = new Recipe();
			PstRecipe pstRecipe = new PstRecipe(oid); 
			recipe.setOID(oid);

			recipe.setCashBillMainId(pstRecipe.getlong(FLD_CASH_BILL_MAIN_ID));
			recipe.setRecipeNumber(pstRecipe.getString(FLD_RECIPE_NUMBER));
			recipe.setDoctorName(pstRecipe.getString(FLD_DOCTOR_NAME));
			recipe.setPatientName(pstRecipe.getString(FLD_PATIENT_NAME));
			recipe.setRecipeService(pstRecipe.getdouble(FLD_RECIPE_SERVICE));

			return recipe; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstRecipe(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(Recipe recipe) throws DBException{ 
		try{ 
			PstRecipe pstRecipe = new PstRecipe(0);

			pstRecipe.setLong(FLD_CASH_BILL_MAIN_ID, recipe.getCashBillMainId());
			pstRecipe.setString(FLD_RECIPE_NUMBER, recipe.getRecipeNumber());
			pstRecipe.setString(FLD_DOCTOR_NAME, recipe.getDoctorName());
			pstRecipe.setString(FLD_PATIENT_NAME, recipe.getPatientName());
			pstRecipe.setDouble(FLD_RECIPE_SERVICE, recipe.getRecipeService());

			pstRecipe.insert(); 
			recipe.setOID(pstRecipe.getlong(FLD_CASH_RECIPE_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstRecipe(0),DBException.UNKNOWN); 
		}
		return recipe.getOID();
	}

        public static long insertExcByOid(Recipe recipe) throws DBException{
		try{
			PstRecipe pstRecipe = new PstRecipe(0);
			pstRecipe.setLong(FLD_CASH_BILL_MAIN_ID, recipe.getCashBillMainId());
			pstRecipe.setString(FLD_RECIPE_NUMBER, recipe.getRecipeNumber());
			pstRecipe.setString(FLD_DOCTOR_NAME, recipe.getDoctorName());
			pstRecipe.setString(FLD_PATIENT_NAME, recipe.getPatientName());
			pstRecipe.setDouble(FLD_RECIPE_SERVICE, recipe.getRecipeService());

			pstRecipe.insertByOid(recipe.getOID());
                        //recipe.setOID(pstRecipe.getlong(FLD_CASH_RECIPE_ID));
		}catch(DBException dbe){
			throw dbe;
		}catch(Exception e){
			throw new DBException(new PstRecipe(0),DBException.UNKNOWN);
		}
		return recipe.getOID();
	}


	public static long updateExc(Recipe recipe) throws DBException{ 
		try{ 
			if(recipe.getOID() != 0){ 
				PstRecipe pstRecipe = new PstRecipe(recipe.getOID());

				pstRecipe.setLong(FLD_CASH_BILL_MAIN_ID, recipe.getCashBillMainId());
				pstRecipe.setString(FLD_RECIPE_NUMBER, recipe.getRecipeNumber());
				pstRecipe.setString(FLD_DOCTOR_NAME, recipe.getDoctorName());
				pstRecipe.setString(FLD_PATIENT_NAME, recipe.getPatientName());
				pstRecipe.setDouble(FLD_RECIPE_SERVICE, recipe.getRecipeService());

				pstRecipe.update(); 
				return recipe.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstRecipe(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstRecipe pstRecipe = new PstRecipe(oid);
			pstRecipe.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstRecipe(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_CASH_RECIPE; 
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
				Recipe recipe = new Recipe();
				resultToObject(rs, recipe);
				lists.add(recipe);
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

	public static void resultToObject(ResultSet rs, Recipe recipe){
		try{
			recipe.setOID(rs.getLong(PstRecipe.fieldNames[PstRecipe.FLD_CASH_RECIPE_ID]));
			recipe.setCashBillMainId(rs.getLong(PstRecipe.fieldNames[PstRecipe.FLD_CASH_BILL_MAIN_ID]));
			recipe.setRecipeNumber(rs.getString(PstRecipe.fieldNames[PstRecipe.FLD_RECIPE_NUMBER]));
			recipe.setDoctorName(rs.getString(PstRecipe.fieldNames[PstRecipe.FLD_DOCTOR_NAME]));
			recipe.setPatientName(rs.getString(PstRecipe.fieldNames[PstRecipe.FLD_PATIENT_NAME]));
			recipe.setRecipeService(rs.getDouble(PstRecipe.fieldNames[PstRecipe.FLD_RECIPE_SERVICE]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long cashRecipeId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_CASH_RECIPE + " WHERE " + 
						PstRecipe.fieldNames[PstRecipe.FLD_CASH_RECIPE_ID] + " = " + cashRecipeId;

			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();

			while(rs.next()) { result = true; }
			rs.close();
		}catch(Exception e){
			System.out.println("err : "+e.toString());
		}finally{
			DBResultSet.close(dbrs);
			
		}
                return result;
	}

	public static int getCount(String whereClause){
		DBResultSet dbrs = null;
		try {
			String sql = "SELECT COUNT("+ PstRecipe.fieldNames[PstRecipe.FLD_CASH_RECIPE_ID] + ") FROM " + TBL_CASH_RECIPE;
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
			  	   Recipe recipe = (Recipe)list.get(ls);
				   if(oid == recipe.getOID())
					  found=true;
			  }
		  }
		}
		if((start >= size) && (size > 0))
		    start = start - recordToGet;

		return start;
	}
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 *
 * @author Wiweka
 */

/* package java */
package com.dimata.common.entity.location;

import java.sql.*;
import java.util.*;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;
import com.dimata.qdep.entity.*;

public class PstNegara extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

	public static final  String TBL_BKD_NEGARA = "hr_negara";

	public static final  int FLD_ID_NEGARA = 0;
	public static final  int FLD_BENUA = 1;
	public static final  int FLD_NM_NEGARA = 2;

    public static final  String[] fieldNames = {
		"ID_NEGARA",
		"BENUA",
		"NAMA_NGR"
	 };

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_STRING,
		TYPE_STRING
	 };

	public PstNegara(){
	}

	public PstNegara(int i) throws DBException {
		super(new PstNegara());
	}

	public PstNegara(String sOid) throws DBException {
		super(new PstNegara(0));
		if(!locate(sOid))
			throw new DBException(this,DBException.RECORD_NOT_FOUND);
		else
			return;
	}

	public PstNegara(long lOid) throws DBException {
		super(new PstNegara(0));
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
		return TBL_BKD_NEGARA;
	}

	public String[] getFieldNames(){
		return fieldNames;
	}

	public int[] getFieldTypes(){
		return fieldTypes;
	}

	public String getPersistentName(){
		return new PstNegara().getClass().getName();
	}

	public long fetchExc(Entity ent) throws Exception{
		Negara negara = fetchExc(ent.getOID());
		ent = (Entity)negara;
		return negara.getOID();
	}

	public long insertExc(Entity ent) throws Exception{
		return insertExc((Negara) ent);
	}

	public long updateExc(Entity ent) throws Exception{
		return updateExc((Negara) ent);
	}

	public long deleteExc(Entity ent) throws Exception{
		if(ent==null){
			throw new DBException(this,DBException.RECORD_NOT_FOUND);
		}
		return deleteExc(ent.getOID());
	}

	public static Negara fetchExc(long oid) throws DBException{
		try{
			Negara negara = new Negara();
			PstNegara pstNegara = new PstNegara(oid);
			negara.setOID(oid);

			negara.setBenua(pstNegara.getString(FLD_BENUA));
			negara.setNmNegara(pstNegara.getString(FLD_NM_NEGARA));

			return negara;
		}catch(DBException dbe){
			throw dbe;
		}catch(Exception e){
			throw new DBException(new PstNegara(0),DBException.UNKNOWN);
		}
	}

	public static long insertExc(Negara negara) throws DBException{
		try{
			PstNegara pstNegara = new PstNegara(0);

			pstNegara.setString(FLD_BENUA, negara.getBenua());
			pstNegara.setString(FLD_NM_NEGARA, negara.getNmNegara());

			pstNegara.insert();
			negara.setOID(pstNegara.getlong(FLD_ID_NEGARA));
		}catch(DBException dbe){
			throw dbe;
		}catch(Exception e){
			throw new DBException(new PstNegara(0),DBException.UNKNOWN);
		}
		return negara.getOID();
	}

	public static long updateExc(Negara negara) throws DBException{
		try{
			if(negara.getOID() != 0){
				PstNegara pstNegara = new PstNegara(negara.getOID());

				pstNegara.setString(FLD_BENUA, negara.getBenua());
				pstNegara.setString(FLD_NM_NEGARA, negara.getNmNegara());

				pstNegara.update();
				return negara.getOID();

			}
		}catch(DBException dbe){
			throw dbe;
		}catch(Exception e){
			throw new DBException(new PstNegara(0),DBException.UNKNOWN);
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{
		try{
			PstNegara pstNegara = new PstNegara(oid);
			pstNegara.delete();
		}catch(DBException dbe){
			throw dbe;
		}catch(Exception e){
			throw new DBException(new PstNegara(0),DBException.UNKNOWN);
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
			String sql = "SELECT * FROM " + TBL_BKD_NEGARA;
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
				Negara negara = new Negara();
				resultToObject(rs, negara);
				lists.add(negara);
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

	private static void resultToObject(ResultSet rs, Negara negara){
		try{
			negara.setOID(rs.getLong(PstNegara.fieldNames[PstNegara.FLD_ID_NEGARA]));
			negara.setBenua(rs.getString(PstNegara.fieldNames[PstNegara.FLD_BENUA]));
			negara.setNmNegara(rs.getString(PstNegara.fieldNames[PstNegara.FLD_NM_NEGARA]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long idNegara){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_BKD_NEGARA + " WHERE " +
						PstNegara.fieldNames[PstNegara.FLD_ID_NEGARA] + " = " + idNegara;

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
			String sql = "SELECT COUNT("+ PstNegara.fieldNames[PstNegara.FLD_ID_NEGARA] + ") FROM " + TBL_BKD_NEGARA;
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


	/* This method used to find current datainduk */
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
			  	   Negara negara = (Negara)list.get(ls);
				   if(oid == negara.getOID())
					  found=true;
			  }
		  }
		}
		if((start >= size) && (size > 0))
		    start = start - recordToGet;

		return start;
	}


}



package com.dimata.aiso.entity.masterdata.region;


import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.*;
import com.dimata.util.lang.I_Language;
import java.sql.*;
import java.util.*;


public class PstCity extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {
/**
 * keteranan: fungsi ini adalah inisialisasi database dengan tabel bernama tb_anggota
 */
public static final  String TBL_TB_CITY = "aiso_city";

        public static final int FLD_CITY_ID = 0;
	public static final int FLD_CITY_NAME = 1;
        
        //update tanggal 7 Maret 2013 oleh Hadi Putra
        public static final int FLD_PROVINCE_ID = 2;
        

	public static final  String[] fieldNames = {
                "CITY_ID",
		"CITY_NAME",
                
                //UPDATE
                "PROVINCE_ID"
	 };

/**
 * menginisialisasikan tipe data untuk database.. 
 */
	public static final  int[] fieldTypes = {
                TYPE_LONG + TYPE_ID + TYPE_PK,	
                TYPE_STRING,
                
                //UPDATE
                TYPE_LONG
   	 };

	public PstCity(){
	}
        
	public PstCity(int i) throws DBException {
		super(new PstCity());
	}

	public PstCity(String sOid) throws DBException {
		super(new PstCity(0));
		if(!locate(sOid))
			throw new DBException(this,DBException.RECORD_NOT_FOUND);
		else
			return;
	}

	public PstCity(long lOid) throws DBException {
		super(new PstCity(0));
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
		return TBL_TB_CITY;
	}

	public String[] getFieldNames(){
		return fieldNames;
	}

	public int[] getFieldTypes(){
		return fieldTypes;
	}

	public String getPersistentName(){
		return new PstCity().getClass().getName();
	}

	public long fetchExc(Entity ent) throws Exception{
		City city = fetchExc(ent.getOID());
		ent = (Entity)city;
		return city.getOID();
	}

	public long insertExc(Entity ent) throws Exception{
		return insertExc((City) ent);
	}

	public long updateExc(Entity ent) throws Exception{
		return updateExc((City) ent);
	}

	public long deleteExc(Entity ent) throws Exception{
		if(ent==null){
			throw new DBException(this,DBException.RECORD_NOT_FOUND);
		}
		return deleteExc(ent.getOID()); 
	}

	public static City fetchExc(long oid) throws DBException{
		try{
			City city = new City();
			PstCity pstCity = new PstCity(oid);
			city.setOID(oid);
			city.setCityName(pstCity.getString(FLD_CITY_NAME));
                        
                        //update
                        city.setIdProvince(pstCity.getlong(FLD_PROVINCE_ID));
			return city;
                        
		}catch(DBException dbe){
			throw dbe;
		}catch(Exception e){
			throw new DBException(new PstCity(0),DBException.UNKNOWN);
		}
	}

	
        public static long insertExc(City city) throws DBException{
		try{

			PstCity pstCity = new PstCity(0);
			pstCity.setString(FLD_CITY_NAME, city.getCityName());
                        
                        //UPDATE
                        pstCity.setLong(FLD_PROVINCE_ID, city.getIdProvince());
                        
			pstCity.insert();
			city.setOID(pstCity.getlong(FLD_CITY_ID));
		}catch(DBException dbe){
			throw dbe;
		}catch(Exception e){
			throw new DBException(new PstCity(0),DBException.UNKNOWN);
		}

		return city.getOID();

	}
        
        
        
        public static long updateExc(City city) throws DBException{
		try{
			if(city.getOID() != 0){
				PstCity pstCity = new PstCity(city.getOID());
				pstCity.setString(FLD_CITY_NAME, city.getCityName());
                                
                                 //UPDATE
                                pstCity.setLong(FLD_PROVINCE_ID, city.getIdProvince());
				pstCity.update();
				return city.getOID();
			}

		}catch(DBException dbe){
			throw dbe;
		}catch(Exception e){
			throw new DBException(new PstCity(0),DBException.UNKNOWN);
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{
		try{
			PstCity pstCity = new PstCity(oid);
			pstCity.delete();
		}catch(DBException dbe){
			throw dbe;
		}catch(Exception e){
			throw new DBException(new PstCity(0),DBException.UNKNOWN);
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
			String sql = "SELECT * FROM " + TBL_TB_CITY;
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
				City city = new City();
				resultToObject(rs, city);
				lists.add(city);
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

	public static void resultToObject(ResultSet rs, City city){
		try{
                        city.setOID(rs.getLong(PstCity.fieldNames[PstCity.FLD_CITY_ID]));
                        city.setCityName(rs.getString(PstCity.fieldNames[PstCity.FLD_CITY_NAME])); 
                        
                        //update
                        city.setIdProvince(rs.getLong(PstCity.fieldNames[PstCity.FLD_PROVINCE_ID]));
                        
		}catch(Exception e){ 
                    System.out.println("Exception"+e);
                }
	}
	
        public static boolean checkOID(long osId){

		DBResultSet dbrs = null;

		boolean result = false;

		try{

			String sql = "SELECT * FROM " + TBL_TB_CITY + " WHERE " +

						PstCity.fieldNames[PstCity.FLD_CITY_ID] + " = " + osId;



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
			String sql = "SELECT COUNT("+ PstCity.fieldNames[PstCity.FLD_CITY_NAME] + ") FROM " + TBL_TB_CITY;
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
			  	  City city = (City)list.get(ls);
				   if(oid == city.getOID()) {
                                  found=true;
                              }
			  }
		  }
		}
		if((start >= size) && (size > 0)) {
                start = start - recordToGet;
            }
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


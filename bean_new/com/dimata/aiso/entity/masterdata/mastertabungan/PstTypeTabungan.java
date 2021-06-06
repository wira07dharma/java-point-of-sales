
package com.dimata.aiso.entity.masterdata.mastertabungan;

/**
 * dede nuharta
 */

import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.*;
import com.dimata.util.lang.I_Language;
import java.sql.*;
import java.util.*;


public class PstTypeTabungan extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {
/**
 * keteranan: fungsi ini adalah inisialisasi database dengan tabel bernama tb_anggota
 */
public static final  String TBL_TYPE_TABUNGAN = "aiso_jenis_transaksi";

        public static final  int FLD_ID_TIPE_TABUNGAN = 0;
	public static final  int FLD_TIPE_TABUNGAN = 1;
        public static final int FLD_AFLIASI_ID=2;
        

	public static final  String[] fieldNames = {

                "JENIS_TRANSAKSI_ID",
		"JENIS_TRANSAKSI",
                "AFLIASI_ID"
	 };

/**
 * menginisialisasikan tipe data untuk database.. 
 */
	public static final  int[] fieldTypes = {
                TYPE_LONG + TYPE_ID + TYPE_PK,	
                TYPE_STRING,
                TYPE_LONG + TYPE_FK
   	 };

	public PstTypeTabungan(){
	}
/**
 * fungsi yang otomatis akan dipanggil setiap kali melakukan instasiasi terhadap suatu kelas
 * @param i
 * @throws DBException 
 */
	public PstTypeTabungan(int i) throws DBException {
		super(new PstTypeTabungan());
	}

	public PstTypeTabungan(String sOid) throws DBException {
		super(new PstTypeTabungan(0));
		if(!locate(sOid))
			throw new DBException(this,DBException.RECORD_NOT_FOUND);
		else
			return;
	}

	public PstTypeTabungan(long lOid) throws DBException {
		super(new PstTypeTabungan(0));
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
		return TBL_TYPE_TABUNGAN;
	}

	public String[] getFieldNames(){
		return fieldNames;
	}

	public int[] getFieldTypes(){
		return fieldTypes;
	}

	public String getPersistentName(){
		return new PstTypeTabungan().getClass().getName();
	}

	public long fetchExc(Entity ent) throws Exception{
		TypeTabungan typeTabungan = fetchExc(ent.getOID());
		ent = (Entity)typeTabungan;
		return typeTabungan.getOID();
	}

	public long insertExc(Entity ent) throws Exception{
		return insertExc((TypeTabungan) ent);
	}

	public long updateExc(Entity ent) throws Exception{
		return updateExc((TypeTabungan) ent);
	}

	public long deleteExc(Entity ent) throws Exception{
		if(ent==null){
			throw new DBException(this,DBException.RECORD_NOT_FOUND);
		}
		return deleteExc(ent.getOID()); 
	}

	public static TypeTabungan fetchExc(long oid) throws DBException{
		try{
			TypeTabungan typeTabungan = new TypeTabungan();
			PstTypeTabungan pstTypeTabungan = new PstTypeTabungan(oid);
			typeTabungan.setOID(oid);
			typeTabungan.setTypeTabungan(pstTypeTabungan.getString(FLD_TIPE_TABUNGAN));
                        typeTabungan.setAfliasiId(pstTypeTabungan.getLong(FLD_AFLIASI_ID));
			return typeTabungan;
                        
		}catch(DBException dbe){
			throw dbe;
		}catch(Exception e){
			throw new DBException(new PstTypeTabungan(0),DBException.UNKNOWN);
		}
	}

	
        public static long insertExc(TypeTabungan typeTabungan) throws DBException{

		try{

			PstTypeTabungan pstTypeTabungan = new PstTypeTabungan(0);


			pstTypeTabungan.setString(FLD_TIPE_TABUNGAN, typeTabungan.getTypeTabungan());
                        pstTypeTabungan.setLong(FLD_AFLIASI_ID, typeTabungan.getAfliasiId());
			pstTypeTabungan.insert();

			typeTabungan.setOID(pstTypeTabungan.getlong(FLD_ID_TIPE_TABUNGAN));

		}catch(DBException dbe){

			throw dbe;

		}catch(Exception e){

			throw new DBException(new PstTypeTabungan(0),DBException.UNKNOWN);

		}

		return typeTabungan.getOID();

	}
        
        
        
        public static long updateExc(TypeTabungan typeTabungan) throws DBException{
		try{
			if(typeTabungan.getOID() != 0){
				PstTypeTabungan pstTypeTabungan = new PstTypeTabungan(typeTabungan.getOID());
				pstTypeTabungan.setString(FLD_TIPE_TABUNGAN, typeTabungan.getTypeTabungan());
                                 pstTypeTabungan.setLong(FLD_AFLIASI_ID, typeTabungan.getAfliasiId());
				pstTypeTabungan.update();
				return typeTabungan.getOID();
			}

		}catch(DBException dbe){
			throw dbe;
		}catch(Exception e){
			throw new DBException(new PstTypeTabungan(0),DBException.UNKNOWN);
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{
		try{
                    /**
                     * Mendefinisikan objek sekalligus menginisialisasi nilai objek.
                     */
			PstTypeTabungan pstTypeTabungan = new PstTypeTabungan(oid);
			pstTypeTabungan.delete();
		}catch(DBException dbe){
			throw dbe;
		}catch(Exception e){
			throw new DBException(new PstTypeTabungan(0),DBException.UNKNOWN);
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
			String sql = "SELECT * FROM " + TBL_TYPE_TABUNGAN;
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
				TypeTabungan typeTabungan = new TypeTabungan();
				resultToObject(rs, typeTabungan);
				lists.add(typeTabungan);
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

	public static void resultToObject(ResultSet rs, TypeTabungan typeTabungan){
		try{
			
                        typeTabungan.setOID(rs.getLong(PstTypeTabungan.fieldNames[PstTypeTabungan.FLD_ID_TIPE_TABUNGAN]));
                    
                        typeTabungan.setTypeTabungan(rs.getString(PstTypeTabungan.fieldNames[PstTypeTabungan.FLD_TIPE_TABUNGAN]));
                        
		}catch(Exception e){ 
                    System.out.println("Exception"+e);
                }
	}
	
        public static boolean checkOID(long osId){

		DBResultSet dbrs = null;

		boolean result = false;

		try{

			String sql = "SELECT * FROM " + TBL_TYPE_TABUNGAN + " WHERE " +

						PstTypeTabungan.fieldNames[PstTypeTabungan.FLD_ID_TIPE_TABUNGAN] + " = " + osId;



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
			String sql = "SELECT COUNT("+ PstTypeTabungan.fieldNames[PstTypeTabungan.FLD_TIPE_TABUNGAN] + ") FROM " + TBL_TYPE_TABUNGAN;
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
			  	  TypeTabungan typeTabungan = (TypeTabungan)list.get(ls);
				   if(oid == typeTabungan.getOID()) {
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


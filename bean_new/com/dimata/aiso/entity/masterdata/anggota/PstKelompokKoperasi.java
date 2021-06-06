
package com.dimata.aiso.entity.masterdata.anggota;


import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.*;
import com.dimata.util.lang.I_Language;
import com.mysql.jdbc.ResultSet;
import java.util.*;


public class PstKelompokKoperasi extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {
/**
 * keteranan: fungsi ini adalah inisialisasi database dengan tabel bernama tb_anggota
 */
public static final  String TBL_AISO_KELOMPOK_KOPERASI = "aiso_kelompok_koperasi";

        public static final  int FLD_KELOMPOK_ID = 0;
	public static final  int FLD_NAMA_KELOMPOK = 1;
        public static final  int FLD_DESC_KELOMPOK = 2;
       
        

	public static final  String[] fieldNames = {

                "KELOMPOK_ID",
		"NAMA_KELOMPOK",
		"DESC_KELOMPOK"             
	 };

/**
 * menginisialisasikan tipe data untuk database.. 
 */
	public static final  int[] fieldTypes = {
                TYPE_LONG + TYPE_ID + TYPE_PK,	
                TYPE_STRING,
                TYPE_STRING,
   	 };

	public PstKelompokKoperasi(){
	}
/**
 * fungsi yang otomatis akan dipanggil setiap kali melakukan instasiasi terhadap suatu kelas
 * @param i
 * @throws DBException 
 */
	public PstKelompokKoperasi(int i) throws DBException {
		super(new PstKelompokKoperasi());
	}

	public PstKelompokKoperasi(String sOid) throws DBException {
		super(new PstKelompokKoperasi(0));
		if(!locate(sOid))
			throw new DBException(this,DBException.RECORD_NOT_FOUND);
		else
			return;
	}

	public PstKelompokKoperasi(long lOid) throws DBException {
		super(new PstKelompokKoperasi(0));
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
		return TBL_AISO_KELOMPOK_KOPERASI;
	}

	public String[] getFieldNames(){
		return fieldNames;
	}

	public int[] getFieldTypes(){
		return fieldTypes;
	}

	public String getPersistentName(){
		return new PstKelompokKoperasi().getClass().getName();
	}

	public long fetchExc(Entity ent) throws Exception{
		KelompokKoperasi kelompokKoperasi = fetchExc(ent.getOID());
		ent = (Entity)kelompokKoperasi;
		return kelompokKoperasi.getOID();
	}

	public long insertExc(Entity ent) throws Exception{
		return insertExc((KelompokKoperasi) ent);
	}

	public long updateExc(Entity ent) throws Exception{
		return updateExc((KelompokKoperasi) ent);
	}

	public long deleteExc(Entity ent) throws Exception{
		if(ent==null){
			throw new DBException(this,DBException.RECORD_NOT_FOUND);
		}
		return deleteExc(ent.getOID()); 
	}

	public static KelompokKoperasi fetchExc(long oid) throws DBException{
		try{
			KelompokKoperasi kelompokKoperasi = new KelompokKoperasi();
			PstKelompokKoperasi pstKelompokKoperasi = new PstKelompokKoperasi(oid);
			kelompokKoperasi.setOID(oid);
			kelompokKoperasi.setNamaKelompok(pstKelompokKoperasi.getString(FLD_NAMA_KELOMPOK));
                        kelompokKoperasi.setDeskripsi(pstKelompokKoperasi.getString(FLD_DESC_KELOMPOK));
			return kelompokKoperasi;
                        
		}catch(DBException dbe){
			throw dbe;
		}catch(Exception e){
			throw new DBException(new PstKelompokKoperasi(0),DBException.UNKNOWN);
		}
	}

	
        public static long insertExc(KelompokKoperasi kelompokKoperasi) throws DBException{

		try{
			PstKelompokKoperasi pstKelompokKoperasi = new PstKelompokKoperasi(0);
                        
			pstKelompokKoperasi.setString(FLD_NAMA_KELOMPOK, kelompokKoperasi.getNamaKelompok());
			pstKelompokKoperasi.setString(FLD_DESC_KELOMPOK, kelompokKoperasi.getDeskripsi());
                        
			pstKelompokKoperasi.insert();
			kelompokKoperasi.setOID(pstKelompokKoperasi.getlong(FLD_KELOMPOK_ID));

		}catch(DBException dbe){

			throw dbe;

		}catch(Exception e){

			throw new DBException(new PstKelompokKoperasi(0),DBException.UNKNOWN);

		}

		return kelompokKoperasi.getOID();

	}
        
        
        
        public static long updateExc(KelompokKoperasi kelompokKoperasi) throws DBException{
		try{
			if(kelompokKoperasi.getOID() != 0){
				PstKelompokKoperasi pstKelompokKoperasi = new PstKelompokKoperasi(kelompokKoperasi.getOID());
				pstKelompokKoperasi.setString(FLD_NAMA_KELOMPOK, kelompokKoperasi.getNamaKelompok());
                                pstKelompokKoperasi.setString(FLD_DESC_KELOMPOK, kelompokKoperasi.getDeskripsi());
				pstKelompokKoperasi.update();
				return kelompokKoperasi.getOID();
			}

		}catch(DBException dbe){
			throw dbe;
		}catch(Exception e){
			throw new DBException(new PstKelompokKoperasi(0),DBException.UNKNOWN);
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{
		try{
                    /**
                     * Mendefinisikan objek sekalligus menginisialisasi nilai objek.
                     */
			PstKelompokKoperasi pstKelompokKoperasi = new PstKelompokKoperasi(oid);
			pstKelompokKoperasi.delete();
		}catch(DBException dbe){
			throw dbe;
		}catch(Exception e){
			throw new DBException(new PstKelompokKoperasi(0),DBException.UNKNOWN);
		}
		return oid;
	}

	public static Vector listAll(){
		return list(0, 500, "","");
	}

	public static Vector list(int limitStart, int recordToGet, String whereClause, String order){
		Vector lists = new Vector();
		DBResultSet dbrs = null;
		try {
			String sql = "SELECT * FROM " + TBL_AISO_KELOMPOK_KOPERASI+"";
                        
			if(whereClause != null && whereClause.length() > 0)
				sql = sql + " WHERE " + whereClause;
			if(order != null && order.length() > 0)
				sql = sql + " ORDER BY " + order;
			if(limitStart == 0 && recordToGet == 0)
				sql = sql + "";
			else
				sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;
                        
			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = (ResultSet) dbrs.getResultSet();
			while(rs.next()) {
				KelompokKoperasi kelompokKoperasi = new KelompokKoperasi();
				resultToObject(rs, kelompokKoperasi);
				lists.add(kelompokKoperasi);
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

	public static void resultToObject(ResultSet rs, KelompokKoperasi kelompokKoperasi){
		try{
			
                        kelompokKoperasi.setOID(rs.getLong(PstKelompokKoperasi.fieldNames[PstKelompokKoperasi.FLD_KELOMPOK_ID]));
                    
                        kelompokKoperasi.setNamaKelompok(rs.getString(PstKelompokKoperasi.fieldNames[PstKelompokKoperasi.FLD_NAMA_KELOMPOK]));                
                        
                        kelompokKoperasi.setDeskripsi(rs.getString(PstKelompokKoperasi.fieldNames[PstKelompokKoperasi.FLD_DESC_KELOMPOK]));
                        
		}catch(Exception e){ 
                    System.out.println("Exception"+e);
                }
	}
	
        public static boolean checkOID(long osId){

		DBResultSet dbrs = null;

		boolean result = false;

		try{

			String sql = "SELECT * FROM " + TBL_AISO_KELOMPOK_KOPERASI + " WHERE " +

						PstKelompokKoperasi.fieldNames[PstKelompokKoperasi.FLD_KELOMPOK_ID] + " = " + osId;



			dbrs = DBHandler.execQueryResult(sql);

			ResultSet rs = (ResultSet) dbrs.getResultSet();



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
			String sql = "SELECT COUNT("+ PstKelompokKoperasi.fieldNames[PstKelompokKoperasi.FLD_NAMA_KELOMPOK] + ") FROM " + TBL_AISO_KELOMPOK_KOPERASI;
			if(whereClause != null && whereClause.length() > 0)
				sql = sql + " WHERE " + whereClause;
                        
			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = (ResultSet) dbrs.getResultSet();
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
			  	  KelompokKoperasi kelompokKoperasi = (KelompokKoperasi)list.get(ls);
				   if(oid == kelompokKoperasi.getOID()) {
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


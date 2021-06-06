
package com.dimata.aiso.entity.masterdata.mastertabungan;


import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.*;
import com.dimata.util.lang.I_Language;
import java.sql.*;
import java.util.*;


public class PstAfiliasi extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {
/**
 * keteranan: fungsi ini adalah inisialisasi database dengan tabel bernama tb_anggota
 */
public static final  String TBL_AISO_AFILIASI = "aiso_afiliasi";

        public static final  int FLD_AFILIASI_ID = 0;
        public static final  int FLD_FEE_KOPERASI = 1;
	public static final  int FLD_NAME_AFILIASI = 2;
        public static final  int FLD_ALAMAT_AFILIASI = 3;
       
        

	public static final  String[] fieldNames = {

                "AFILIASI_ID",
		"FEE_KOPERASI",
                "NAME_AFILIASI",
                "ALAMAT_AFILIASI"
	 };

/**
 * menginisialisasikan tipe data untuk database.. 
 */
	public static final  int[] fieldTypes = {
                TYPE_LONG + TYPE_ID + TYPE_PK,	
                TYPE_FLOAT,
                TYPE_STRING,
                TYPE_STRING,
   	 };

	public PstAfiliasi(){
	}
/**
 * fungsi yang otomatis akan dipanggil setiap kali melakukan instasiasi terhadap suatu kelas
 * @param i
 * @throws DBException 
 */
	public PstAfiliasi(int i) throws DBException {
		super(new PstAfiliasi());
	}

	public PstAfiliasi(String sOid) throws DBException {
		super(new PstAfiliasi(0));
		if(!locate(sOid))
			throw new DBException(this,DBException.RECORD_NOT_FOUND);
		else
			return;
	}

	public PstAfiliasi(long lOid) throws DBException {
		super(new PstAfiliasi(0));
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
		return TBL_AISO_AFILIASI;
	}

	public String[] getFieldNames(){
		return fieldNames;
	}

	public int[] getFieldTypes(){
		return fieldTypes;
	}

	public String getPersistentName(){
		return new PstAfiliasi().getClass().getName();
	}

	public long fetchExc(Entity ent) throws Exception{
		Afiliasi afiliasi = fetchExc(ent.getOID());
		ent = (Entity)afiliasi;
		return afiliasi.getOID();
	}

	public long insertExc(Entity ent) throws Exception{
		return insertExc((Afiliasi) ent);
	}

	public long updateExc(Entity ent) throws Exception{
		return updateExc((Afiliasi) ent);
	}

	public long deleteExc(Entity ent) throws Exception{
		if(ent==null){
			throw new DBException(this,DBException.RECORD_NOT_FOUND);
		}
		return deleteExc(ent.getOID()); 
	}

	public static Afiliasi fetchExc(long oid) throws DBException{
		try{
			Afiliasi afiliasi = new Afiliasi();
			PstAfiliasi pstAfiliasi = new PstAfiliasi(oid);
			afiliasi.setOID(oid);
                        
                        afiliasi.setFeeKoperasi(pstAfiliasi.getdouble(FLD_FEE_KOPERASI));
			afiliasi.setNamaAfiliasi(pstAfiliasi.getString(FLD_NAME_AFILIASI));
                        afiliasi.setAlamatAfiliasi(pstAfiliasi.getString(FLD_ALAMAT_AFILIASI));
			return afiliasi;
                        
		}catch(DBException dbe){
			throw dbe;
		}catch(Exception e){
			throw new DBException(new PstAfiliasi(0),DBException.UNKNOWN);
		}
	}

	
        public static long insertExc(Afiliasi afiliasi) throws DBException{

		try{

			PstAfiliasi pstAfiliasi = new PstAfiliasi(0);

                        pstAfiliasi.setFloat(FLD_FEE_KOPERASI, afiliasi.getFeeKoperasi());

			pstAfiliasi.setString(FLD_NAME_AFILIASI, afiliasi.getNamaAfiliasi());
                        
                        pstAfiliasi.setString(FLD_ALAMAT_AFILIASI, afiliasi.getAlamatAfiliasi());
                        
			pstAfiliasi.insert();

			afiliasi.setOID(pstAfiliasi.getlong(FLD_AFILIASI_ID));

		}catch(DBException dbe){

			throw dbe;

		}catch(Exception e){

			throw new DBException(new PstAfiliasi(0),DBException.UNKNOWN);

		}

		return afiliasi.getOID();

	}
        
        
        
        public static long updateExc(Afiliasi afiliasi) throws DBException{
		try{
			if(afiliasi.getOID() != 0){
				PstAfiliasi pstAfiliasi = new PstAfiliasi(afiliasi.getOID());
                                
				pstAfiliasi.setFloat(FLD_FEE_KOPERASI, afiliasi.getFeeKoperasi());
                                pstAfiliasi.setString(FLD_NAME_AFILIASI, afiliasi.getNamaAfiliasi());
                                pstAfiliasi.setString(FLD_ALAMAT_AFILIASI, afiliasi.getAlamatAfiliasi());
				pstAfiliasi.update();
				return afiliasi.getOID();
			}

		}catch(DBException dbe){
			throw dbe;
		}catch(Exception e){
			throw new DBException(new PstAfiliasi(0),DBException.UNKNOWN);
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{
		try{
                    /**
                     * Mendefinisikan objek sekalligus menginisialisasi nilai objek.
                     */
			PstAfiliasi pstAfiliasi = new PstAfiliasi(oid);
			pstAfiliasi.delete();
		}catch(DBException dbe){
			throw dbe;
		}catch(Exception e){
			throw new DBException(new PstAfiliasi(0),DBException.UNKNOWN);
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
			String sql = "SELECT * FROM " + TBL_AISO_AFILIASI;
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
				Afiliasi afiliasi = new Afiliasi();
				resultToObject(rs, afiliasi);
				lists.add(afiliasi);
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

	public static void resultToObject(ResultSet rs, Afiliasi afiliasi){
		try{
			
                        afiliasi.setOID(rs.getLong(PstAfiliasi.fieldNames[PstAfiliasi.FLD_AFILIASI_ID]));
                        
                        afiliasi.setFeeKoperasi(rs.getFloat(PstAfiliasi.fieldNames[PstAfiliasi.FLD_FEE_KOPERASI]));
                         
                        afiliasi.setNamaAfiliasi(rs.getString(PstAfiliasi.fieldNames[PstAfiliasi.FLD_NAME_AFILIASI]));
                    
                        afiliasi.setAlamatAfiliasi(rs.getString(PstAfiliasi.fieldNames[PstAfiliasi.FLD_ALAMAT_AFILIASI])); 
                        
		}catch(Exception e){ 
                    System.out.println("Exception"+e);
                }
	}
	
        public static boolean checkOID(long osId){

		DBResultSet dbrs = null;

		boolean result = false;

		try{

			String sql = "SELECT * FROM " + TBL_AISO_AFILIASI + " WHERE " +

			PstAfiliasi.fieldNames[PstAfiliasi.FLD_FEE_KOPERASI] + " = " + osId;

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
			String sql = "SELECT COUNT("+ PstAfiliasi.fieldNames[PstAfiliasi.FLD_FEE_KOPERASI] + ") FROM " + TBL_AISO_AFILIASI;
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
			  	  Afiliasi afiliasi = (Afiliasi)list.get(ls);
				   if(oid == afiliasi.getOID()) {
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


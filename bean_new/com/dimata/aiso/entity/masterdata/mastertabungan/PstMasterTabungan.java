
package com.dimata.aiso.entity.masterdata.mastertabungan;


import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.*;
import com.dimata.util.lang.I_Language;
import java.sql.*;
import java.util.*;


public class PstMasterTabungan extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {
/**
 * keteranan: fungsi ini adalah inisialisasi database dengan tabel bernama tb_anggota
 */
public static final  String TBL_AISO_MASTER_TABUNGAN = "aiso_master_tabungan";

        public static final  int FLD_ID_TABUNGAN = 0;
	public static final  int FLD_NAMA_TABUNGAN = 1;
       
        

	public static final  String[] fieldNames = {

                "ID_TABUNGAN",
		"NAMA_TABUNGAN"           
	 };

/**
 * menginisialisasikan tipe data untuk database.. 
 */
	public static final  int[] fieldTypes = {
                TYPE_LONG + TYPE_ID + TYPE_PK,	
                TYPE_STRING,
   	 };

	public PstMasterTabungan(){
	}
/**
 * fungsi yang otomatis akan dipanggil setiap kali melakukan instasiasi terhadap suatu kelas
 * @param i
 * @throws DBException 
 */
	public PstMasterTabungan(int i) throws DBException {
		super(new PstMasterTabungan());
	}

	public PstMasterTabungan(String sOid) throws DBException {
		super(new PstMasterTabungan(0));
		if(!locate(sOid))
			throw new DBException(this,DBException.RECORD_NOT_FOUND);
		else
			return;
	}

	public PstMasterTabungan(long lOid) throws DBException {
		super(new PstMasterTabungan(0));
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
		return TBL_AISO_MASTER_TABUNGAN;
	}

	public String[] getFieldNames(){
		return fieldNames;
	}

	public int[] getFieldTypes(){
		return fieldTypes;
	}

	public String getPersistentName(){
		return new PstMasterTabungan().getClass().getName();
	}

	public long fetchExc(Entity ent) throws Exception{
		MasterTabungan masterTabungan = fetchExc(ent.getOID());
		ent = (Entity)masterTabungan;
		return masterTabungan.getOID();
	}

	public long insertExc(Entity ent) throws Exception{
		return insertExc((MasterTabungan) ent);
	}

	public long updateExc(Entity ent) throws Exception{
		return updateExc((MasterTabungan) ent);
	}

	public long deleteExc(Entity ent) throws Exception{
		if(ent==null){
			throw new DBException(this,DBException.RECORD_NOT_FOUND);
		}
		return deleteExc(ent.getOID()); 
	}

	public static MasterTabungan fetchExc(long oid) throws DBException{
		try{
			MasterTabungan masterTabungan = new MasterTabungan();
			PstMasterTabungan pstMasterTabungan = new PstMasterTabungan(oid);
			masterTabungan.setOID(oid);
			masterTabungan.setSavingName(pstMasterTabungan.getString(FLD_NAMA_TABUNGAN));
			return masterTabungan;
                        
		}catch(DBException dbe){
			throw dbe;
		}catch(Exception e){
			throw new DBException(new PstMasterTabungan(0),DBException.UNKNOWN);
		}
	}

	
        public static long insertExc(MasterTabungan masterTabungan) throws DBException{

		try{

			PstMasterTabungan pstMasterTabungan = new PstMasterTabungan(0);


			pstMasterTabungan.setString(FLD_NAMA_TABUNGAN, masterTabungan.getSavingName());
                        
			pstMasterTabungan.insert();

			masterTabungan.setOID(pstMasterTabungan.getlong(FLD_ID_TABUNGAN));

		}catch(DBException dbe){

			throw dbe;

		}catch(Exception e){

			throw new DBException(new PstMasterTabungan(0),DBException.UNKNOWN);

		}

		return masterTabungan.getOID();

	}
        
        
        
        public static long updateExc(MasterTabungan masterTabungan) throws DBException{
		try{
			if(masterTabungan.getOID() != 0){
				PstMasterTabungan pstMasterTabungan = new PstMasterTabungan(masterTabungan.getOID());
				pstMasterTabungan.setString(FLD_NAMA_TABUNGAN, masterTabungan.getSavingName());
				pstMasterTabungan.update();
				return masterTabungan.getOID();
			}

		}catch(DBException dbe){
			throw dbe;
		}catch(Exception e){
			throw new DBException(new PstMasterTabungan(0),DBException.UNKNOWN);
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{
		try{
                    /**
                     * Mendefinisikan objek sekalligus menginisialisasi nilai objek.
                     */
			PstMasterTabungan pstMasterTabungan = new PstMasterTabungan(oid);
			pstMasterTabungan.delete();
		}catch(DBException dbe){
			throw dbe;
		}catch(Exception e){
			throw new DBException(new PstMasterTabungan(0),DBException.UNKNOWN);
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
			String sql = "SELECT * FROM " + TBL_AISO_MASTER_TABUNGAN;
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
				MasterTabungan masterTabungan = new MasterTabungan();
				resultToObject(rs, masterTabungan);
				lists.add(masterTabungan);
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

	public static void resultToObject(ResultSet rs, MasterTabungan masterTabungan){
		try{
			
                        masterTabungan.setOID(rs.getLong(PstMasterTabungan.fieldNames[PstMasterTabungan.FLD_ID_TABUNGAN]));
                    
                        masterTabungan.setSavingName(rs.getString(PstMasterTabungan.fieldNames[PstMasterTabungan.FLD_NAMA_TABUNGAN])); 
                        
		}catch(Exception e){ 
                    System.out.println("Exception"+e);
                }
	}
	
        public static boolean checkOID(long osId){

		DBResultSet dbrs = null;

		boolean result = false;

		try{

			String sql = "SELECT * FROM " + TBL_AISO_MASTER_TABUNGAN + " WHERE " +

						PstMasterTabungan.fieldNames[PstMasterTabungan.FLD_ID_TABUNGAN] + " = " + osId;



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
			String sql = "SELECT COUNT("+ PstMasterTabungan.fieldNames[PstMasterTabungan.FLD_NAMA_TABUNGAN] + ") FROM " + TBL_AISO_MASTER_TABUNGAN;
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
			  	  MasterTabungan masterTabungan = (MasterTabungan)list.get(ls);
				   if(oid == masterTabungan.getOID()) {
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


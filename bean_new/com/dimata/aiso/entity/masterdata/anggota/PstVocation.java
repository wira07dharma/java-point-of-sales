
package com.dimata.aiso.entity.masterdata.anggota;


import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.*;
import com.dimata.util.lang.I_Language;
import com.mysql.jdbc.ResultSet;
import java.util.*;


public class PstVocation extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {
/**
 * keteranan: fungsi ini adalah inisialisasi database dengan tabel bernama tb_anggota
 */
public static final  String TBL_TB_VOCATION = "aiso_vocation";

        public static final  int FLD_VOCATION_ID = 0;
	public static final  int FLD_VOCATION_NAME = 1;
        public static final  int FLD_DESC_VOCATION = 2;
       
        

	public static final  String[] fieldNames = {

                "VOCATION_ID",
		"VOCATION_NAME",
		"DESC_VOCATION"             
	 };

/**
 * menginisialisasikan tipe data untuk database.. 
 */
	public static final  int[] fieldTypes = {
                TYPE_LONG + TYPE_ID + TYPE_PK,	
                TYPE_STRING,
                TYPE_STRING,
   	 };

	public PstVocation(){
	}
/**
 * fungsi yang otomatis akan dipanggil setiap kali melakukan instasiasi terhadap suatu kelas
 * @param i
 * @throws DBException 
 */
	public PstVocation(int i) throws DBException {
		super(new PstVocation());
	}

	public PstVocation(String sOid) throws DBException {
		super(new PstVocation(0));
		if(!locate(sOid))
			throw new DBException(this,DBException.RECORD_NOT_FOUND);
		else
			return;
	}

	public PstVocation(long lOid) throws DBException {
		super(new PstVocation(0));
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
		return TBL_TB_VOCATION;
	}

	public String[] getFieldNames(){
		return fieldNames;
	}

	public int[] getFieldTypes(){
		return fieldTypes;
	}

	public String getPersistentName(){
		return new PstVocation().getClass().getName();
	}

	public long fetchExc(Entity ent) throws Exception{
		Vocation vocation = fetchExc(ent.getOID());
		ent = (Entity)vocation;
		return vocation.getOID();
	}

	public long insertExc(Entity ent) throws Exception{
		return insertExc((Vocation) ent);
	}

	public long updateExc(Entity ent) throws Exception{
		return updateExc((Vocation) ent);
	}

	public long deleteExc(Entity ent) throws Exception{
		if(ent==null){
			throw new DBException(this,DBException.RECORD_NOT_FOUND);
		}
		return deleteExc(ent.getOID()); 
	}

	public static Vocation fetchExc(long oid) throws DBException{
		try{
			Vocation vocation = new Vocation();
			PstVocation pstVocation = new PstVocation(oid);
			vocation.setOID(oid);
			vocation.setVocationName(pstVocation.getString(FLD_VOCATION_NAME));
                        vocation.setDescription(pstVocation.getString(FLD_DESC_VOCATION));
			return vocation;
                        
		}catch(DBException dbe){
			throw dbe;
		}catch(Exception e){
			throw new DBException(new PstVocation(0),DBException.UNKNOWN);
		}
	}

	
        public static long insertExc(Vocation vocation) throws DBException{

		try{

			PstVocation pstVocation = new PstVocation(0);


			pstVocation.setString(FLD_VOCATION_NAME, vocation.getVocationName());

			pstVocation.setString(FLD_DESC_VOCATION, vocation.getDescription());
                        
			pstVocation.insert();

			vocation.setOID(pstVocation.getlong(FLD_VOCATION_ID));

		}catch(DBException dbe){

			throw dbe;

		}catch(Exception e){

			throw new DBException(new PstVocation(0),DBException.UNKNOWN);

		}

		return vocation.getOID();

	}
        
        
        
        public static long updateExc(Vocation vocation) throws DBException{
		try{
			if(vocation.getOID() != 0){
				PstVocation pstVocation = new PstVocation(vocation.getOID());
				pstVocation.setString(FLD_VOCATION_NAME, vocation.getVocationName());
                                pstVocation.setString(FLD_DESC_VOCATION, vocation.getDescription());
				pstVocation.update();
				return vocation.getOID();
			}

		}catch(DBException dbe){
			throw dbe;
		}catch(Exception e){
			throw new DBException(new PstVocation(0),DBException.UNKNOWN);
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{
		try{
                    /**
                     * Mendefinisikan objek sekalligus menginisialisasi nilai objek.
                     */
			PstVocation pstVocation = new PstVocation(oid);
			pstVocation.delete();
		}catch(DBException dbe){
			throw dbe;
		}catch(Exception e){
			throw new DBException(new PstVocation(0),DBException.UNKNOWN);
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
			String sql = "SELECT * FROM " + TBL_TB_VOCATION+"";
                        
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
				Vocation vocation = new Vocation();
				resultToObject(rs, vocation);
				lists.add(vocation);
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

	public static void resultToObject(ResultSet rs, Vocation vocation){
		try{
			
                        vocation.setOID(rs.getLong(PstVocation.fieldNames[PstVocation.FLD_VOCATION_ID]));
                    
                        vocation.setVocationName(rs.getString(PstVocation.fieldNames[PstVocation.FLD_VOCATION_NAME]));                
                        
                        vocation.setDescription(rs.getString(PstVocation.fieldNames[PstVocation.FLD_DESC_VOCATION]));
                        
		}catch(Exception e){ 
                    System.out.println("Exception"+e);
                }
	}
	
        public static boolean checkOID(long osId){

		DBResultSet dbrs = null;

		boolean result = false;

		try{

			String sql = "SELECT * FROM " + TBL_TB_VOCATION + " WHERE " +

						PstVocation.fieldNames[PstVocation.FLD_VOCATION_ID] + " = " + osId;



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
			String sql = "SELECT COUNT("+ PstVocation.fieldNames[PstVocation.FLD_VOCATION_NAME] + ") FROM " + TBL_TB_VOCATION;
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
        
        public static String getNamaPekerjaan(String whereClause){
		DBResultSet dbrs = null;
                String count = "";
		try {
			String sql = "SELECT "+ PstVocation.fieldNames[PstVocation.FLD_VOCATION_NAME] + " FROM " + TBL_TB_VOCATION;
			if(whereClause != null && whereClause.length() > 0)
				sql = sql + " WHERE " + whereClause;
                        
			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = (ResultSet) dbrs.getResultSet();
			
			while(rs.next()) { count = rs.getString(1); }
			rs.close();
			return count;
		}catch(Exception e) {
			return count;
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
			  	  Vocation vocation = (Vocation)list.get(ls);
				   if(oid == vocation.getOID()) {
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


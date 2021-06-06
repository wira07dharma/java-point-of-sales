
package com.dimata.aiso.entity.masterdata.transaksi;


import java.sql.*;
import java.util.*;
import com.dimata.util.lang.I_Language;
import com.dimata.util.*;
import com.dimata.qdep.entity.*;
import com.dimata.qdep.db.*;

/**
 *
 * @author dede nuharta
 */

public class PstDataTransaksi extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {
/**
 * keteranan: fungsi ini adalah inisialisasi database dengan tabel bernama tb_anggota
 */
public static final  String TBL_AISO_PROSES_TRANSAKSI = "aiso_proses_transaksi";

        public static final  int FLD_ID_TRANSAKSI = 0;
        public static final  int FLD_ID_ANGGOTA = 1;
        public static final  int FLD_CODE_TRANSAKSI = 2;
        public static final  int FLD_TANGGAL_TRANSAKSI = 3;
        public static final  int FLD_JENIS_TRANSAKSI = 4;
        public static final  int FLD_BUNGA = 5;
        public static final  int FLD_POTONGAN = 6;
        public static final  int FLD_JUMLAH_TRANSAKSI = 7;
        public static final  int FLD_TOTAL_SALDO = 8;
        
       
        

	public static final  String[] fieldNames = {

                "ID_TRANSAKSI",
		"ID_ANGGOTA",
                "CODE_TRANSAKSI",
                "TANGGAL_TRANSAKSI",
                "JENIS_TRANSAKSI",
                "BUNGA",
                "POTONGAN",
                "JUMLAH_TRANSAKSI",
                "TOTAL_SALDO"
                
	 };

/**
 * menginisialisasikan tipe data untuk database.. 
 */
	public static final  int[] fieldTypes = {
                TYPE_LONG + TYPE_ID + TYPE_PK,
                TYPE_LONG,
                TYPE_STRING,
                TYPE_DATE,
                TYPE_LONG,
                TYPE_FLOAT,
                TYPE_FLOAT,
                TYPE_FLOAT,
                TYPE_FLOAT,
   	 };

        public static final int DRAFT = 0;
    public static final int ACTIVE = 1;
    public static final int CLOSE = 2;
    
    public static final String[] statusDataTransaksiKey = {"Draft","Aktif","Tutup"};
    
    public static final int[] statusDataTransaksiValues = {0,1,2};
    
    public static Vector getStatusDataTransaksiKey(){
    	Vector result = new Vector(1,1);
        for(int i=0;i<statusDataTransaksiKey.length;i++){
        	result.add(statusDataTransaksiKey[i]);
        }
        return result;
    }
    
    public static Vector getStatusDataTransaksiValue(){
        Vector value = new Vector(1, 1);
        for(int i=0; i<statusDataTransaksiValues.length; i++){
            value.add(Integer.toString(i));
        }
        return value;
    }
    
    public int index;
    
    public int getIndex(){
        return this.index;
    }
    
    public void setIndex(int index){
        this.index = index;
    }
    
    public int getFieldSize() {
        return fieldNames.length;
    }

    public String getTableName() {
        return TBL_AISO_PROSES_TRANSAKSI;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }
    
    public String getPersistentName() {
        return new PstDataTransaksi().getClass().getName();
    }
        
        
	public PstDataTransaksi(){
	}
/**
 * fungsi yang otomatis akan dipanggil setiap kali melakukan instasiasi terhadap suatu kelas
 * @param i
 * @throws DBException 
 */
	public PstDataTransaksi(int i) throws DBException {
		super(new PstDataTransaksi());
	}

	public PstDataTransaksi(String sOid) throws DBException {
		super(new PstDataTransaksi(0));
		if(!locate(sOid))
			throw new DBException(this,DBException.RECORD_NOT_FOUND);
		else
			return;
	}

	public PstDataTransaksi(long lOid) throws DBException {
		super(new PstDataTransaksi(0));
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



	public long fetchExc(Entity ent) throws Exception{
		DataTransaksi dataTransaksi = fetchExc(ent.getOID());
		ent = (Entity)dataTransaksi;
		return dataTransaksi.getOID();
	}

	public long insertExc(Entity ent) throws Exception{
		return insertExc((DataTransaksi) ent);
	}

	public long updateExc(Entity ent) throws Exception{
		return updateExc((DataTransaksi) ent);
	}

	public long deleteExc(Entity ent) throws Exception{
		if(ent==null){
			throw new DBException(this,DBException.RECORD_NOT_FOUND);
		}
		return deleteExc(ent.getOID()); 
	}

	public static DataTransaksi fetchExc(long oid) throws DBException{
                        DataTransaksi dataTransaksi = new DataTransaksi();
		try{
			
			PstDataTransaksi pstDataTransaksi = new PstDataTransaksi(oid);
			dataTransaksi.setOID(oid);
                        dataTransaksi.setIdAnggota(pstDataTransaksi.getlong(FLD_ID_ANGGOTA));
                        dataTransaksi.setCodeTransaksi(pstDataTransaksi.getString(FLD_CODE_TRANSAKSI));
			dataTransaksi.setTanggal(pstDataTransaksi.getDate(FLD_TANGGAL_TRANSAKSI));
                        dataTransaksi.setJenisTransaksi(pstDataTransaksi.getlong(FLD_JENIS_TRANSAKSI));
                        dataTransaksi.setBunga(pstDataTransaksi.getdouble(FLD_BUNGA));
                        dataTransaksi.setPotongan(pstDataTransaksi.getdouble(FLD_POTONGAN));
                        dataTransaksi.setJumlahTransaksi(pstDataTransaksi.getdouble(FLD_JUMLAH_TRANSAKSI));
                        dataTransaksi.setSaldo(pstDataTransaksi.getdouble(FLD_TOTAL_SALDO));
			return dataTransaksi;
                        
		}catch(DBException dbe){
			throw dbe;
		}catch(Exception e){
			throw new DBException(new PstDataTransaksi(0),DBException.UNKNOWN);
		}
	}

	
        public static long insertExc(DataTransaksi dataTransaksi) throws DBException{

		try{

			PstDataTransaksi pstDataTransaksi = new PstDataTransaksi(0);
                        
                        pstDataTransaksi.setLong(FLD_ID_ANGGOTA, dataTransaksi.getIdAnggota());
                        
                        pstDataTransaksi.setString(FLD_CODE_TRANSAKSI, dataTransaksi.getCodeTransaksi());
                        
                        pstDataTransaksi.setDate(FLD_TANGGAL_TRANSAKSI, dataTransaksi.getTanggal());
                         
                        pstDataTransaksi.setLong(FLD_JENIS_TRANSAKSI, dataTransaksi.getJenisTransaksi()); 

                        pstDataTransaksi.setFloat(FLD_BUNGA, dataTransaksi.getBunga());

			pstDataTransaksi.setFloat(FLD_POTONGAN, dataTransaksi.getPotongan());
                        
                        pstDataTransaksi.setFloat(FLD_JUMLAH_TRANSAKSI, dataTransaksi.getJumlahTransaksi());
                        
                        pstDataTransaksi.setFloat(FLD_TOTAL_SALDO, dataTransaksi.getSaldo());
                        
			pstDataTransaksi.insert();

			dataTransaksi.setOID(pstDataTransaksi.getlong(FLD_ID_TRANSAKSI));

		}catch(DBException dbe){

			throw dbe;

		}catch(Exception e){

			throw new DBException(new PstDataTransaksi(0),DBException.UNKNOWN);

		}

		return dataTransaksi.getOID();

	}
        
        
        
        public static long updateExc(DataTransaksi dataTransaksi) throws DBException{
		try{
			if(dataTransaksi.getOID() != 0){
				PstDataTransaksi pstDataTransaksi = new PstDataTransaksi(dataTransaksi.getOID());
                                
                                pstDataTransaksi.setLong(FLD_ID_ANGGOTA, dataTransaksi.getIdAnggota());
                                pstDataTransaksi.setString(FLD_CODE_TRANSAKSI, dataTransaksi.getCodeTransaksi());
                                pstDataTransaksi.setDate(FLD_TANGGAL_TRANSAKSI, dataTransaksi.getTanggal());
                                pstDataTransaksi.setLong(FLD_JENIS_TRANSAKSI, dataTransaksi.getJenisTransaksi());
				pstDataTransaksi.setFloat(FLD_BUNGA, dataTransaksi.getBunga());
                                pstDataTransaksi.setFloat(FLD_POTONGAN, dataTransaksi.getPotongan());
                                pstDataTransaksi.setFloat(FLD_JUMLAH_TRANSAKSI, dataTransaksi.getJumlahTransaksi());
                                pstDataTransaksi.setFloat(FLD_TOTAL_SALDO, dataTransaksi.getSaldo());
				pstDataTransaksi.update();
				return dataTransaksi.getOID();
			}

		}catch(DBException dbe){
			throw dbe;
		}catch(Exception e){
			throw new DBException(new PstDataTransaksi(0),DBException.UNKNOWN);
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{
		try{
                    /**
                     * Mendefinisikan objek sekalligus menginisialisasi nilai objek.
                     */
			PstDataTransaksi pstDataTransaksi = new PstDataTransaksi(oid);
			pstDataTransaksi.delete();
		}catch(DBException dbe){
			throw dbe;
		}catch(Exception e){
			throw new DBException(new PstDataTransaksi(0),DBException.UNKNOWN);
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
			String sql = "SELECT * FROM " + TBL_AISO_PROSES_TRANSAKSI;
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
				DataTransaksi dataTransaksi = new DataTransaksi();
				resultToObject(rs, dataTransaksi);
				lists.add(dataTransaksi);
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

        
            //kode otomatis

    public static int getAutoCode() {
        DBResultSet dbrs = null;
        int code = 0;
        String sCode = "";
        ResultSet rs=null;
        try {
            String sql = "SELECT MAX(" + PstDataTransaksi.fieldNames[PstDataTransaksi.FLD_CODE_TRANSAKSI] + ") AS " + PstDataTransaksi.fieldNames[PstDataTransaksi.FLD_CODE_TRANSAKSI] 
                    + " FROM " + PstDataTransaksi.TBL_AISO_PROSES_TRANSAKSI;

            dbrs = DBHandler.execQueryResult(sql);
             rs = (ResultSet) dbrs.getResultSet();
            while (rs.next()) {
                sCode = rs.getString(PstDataTransaksi.fieldNames[PstDataTransaksi.FLD_CODE_TRANSAKSI]);
            }
            code= Integer.parseInt(sCode.substring(1));
        } catch (Exception e) {
        }
        finally{
            if(rs!=null){
                try{
                rs.close();
                }
                catch(Exception e){
                }
            }
        }
        return ++code;
    }

    public static synchronized String getCodeAutoGenerate() {
        //String prex = "G";
        String code = "";
        int nextCounter = getAutoCode();
        String counter = "";
        if (nextCounter < 10) {
            counter = "00" + nextCounter;
        } else {
            if (nextCounter < 100) {
                counter = "0" + nextCounter;
            } //else {
            // counter = "" + nextCounter;
            //}
           
        }
        code = counter;

        return code;
    }
    ///end kode otomatis

        /**
        * keterangan : untuk list total saldo
        * @return 
        */ 
        public static Vector listSaldo(int limitStart,int recordToGet, String whereClause, String order){
		Vector lists = new Vector();
		DBResultSet dbrs = null;
		try {
			String sql = " SELECT SUM("+PstDataTransaksi.fieldNames[PstDataTransaksi.FLD_TOTAL_SALDO]+") as TOTSALDO,"+ PstDataTransaksi.fieldNames[PstDataTransaksi.FLD_ID_ANGGOTA] + " FROM "+ PstDataTransaksi.TBL_AISO_PROSES_TRANSAKSI ;
                                
                        if(whereClause != null && whereClause.length() > 0)
				sql = sql + " WHERE " + whereClause;
                        sql = sql + " GROUP BY " + PstDataTransaksi.fieldNames[PstDataTransaksi.FLD_ID_ANGGOTA] ;
			if(order != null && order.length() > 0)
				sql = sql + " ORDER BY " + order;
			if(limitStart == 0 && recordToGet == 0)
				sql = sql + "";
			else
				sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;
                                
                             //  + " ORDER BY " + PstDataTransaksi.fieldNames[PstDataTransaksi.FLD_ID_ANGGOTA];
			
			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();
			while(rs.next()) {
				DataTransaksi dataTransaksi = new DataTransaksi();
				dataTransaksi.setSaldo(rs.getDouble("TOTSALDO"));
                                dataTransaksi.setIdAnggota(rs.getLong(PstDataTransaksi.fieldNames[PstDataTransaksi.FLD_ID_ANGGOTA]));
				lists.add(dataTransaksi);
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

	public static void resultToObject(ResultSet rs, DataTransaksi dataTransaksi){
		try{
			
                        dataTransaksi.setOID(rs.getLong(PstDataTransaksi.fieldNames[PstDataTransaksi.FLD_ID_TRANSAKSI]));
                        
                        dataTransaksi.setIdAnggota(rs.getLong(PstDataTransaksi.fieldNames[PstDataTransaksi.FLD_ID_ANGGOTA]));
                        
                        dataTransaksi.setCodeTransaksi(rs.getString(PstDataTransaksi.fieldNames[PstDataTransaksi.FLD_CODE_TRANSAKSI]));
                         
                        dataTransaksi.setTanggal(rs.getDate(PstDataTransaksi.fieldNames[PstDataTransaksi.FLD_TANGGAL_TRANSAKSI]));
                        
                        dataTransaksi.setJenisTransaksi(rs.getLong(PstDataTransaksi.fieldNames[PstDataTransaksi.FLD_JENIS_TRANSAKSI]));
                    
                        dataTransaksi.setBunga(rs.getFloat(PstDataTransaksi.fieldNames[PstDataTransaksi.FLD_BUNGA]));
                        
                        dataTransaksi.setPotongan(rs.getFloat(PstDataTransaksi.fieldNames[PstDataTransaksi.FLD_POTONGAN]));
                        
                         dataTransaksi.setJumlahTransaksi(rs.getFloat(PstDataTransaksi.fieldNames[PstDataTransaksi.FLD_JUMLAH_TRANSAKSI]));
                        
                        dataTransaksi.setSaldo(rs.getFloat(PstDataTransaksi.fieldNames[PstDataTransaksi.FLD_TOTAL_SALDO]));
                        
		}catch(Exception e){ 
                    System.out.println("Exception"+e);
                }
	}
	
        public static boolean checkOID(long osId){

		DBResultSet dbrs = null;

		boolean result = false;

		try{

			String sql = "SELECT * FROM " + TBL_AISO_PROSES_TRANSAKSI + " WHERE " +

			PstDataTransaksi.fieldNames[PstDataTransaksi.FLD_JENIS_TRANSAKSI] + " = " + osId;

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
			String sql = "SELECT COUNT("+ PstDataTransaksi.fieldNames[PstDataTransaksi.FLD_JENIS_TRANSAKSI] + ") FROM " + TBL_AISO_PROSES_TRANSAKSI;
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

        public static double getTotalTabungan(long idAnggota){
            double total = 0.0;
            DBResultSet dbrs = null;
		try {
                if(idAnggota!=0){
			String sql = "SELECT SUM("+PstDataTransaksi.fieldNames[PstDataTransaksi.FLD_TOTAL_SALDO]+") FROM "+ PstDataTransaksi.TBL_AISO_PROSES_TRANSAKSI
                                
                                +" where "+PstDataTransaksi.fieldNames[PstDataTransaksi.FLD_ID_ANGGOTA]+"="+idAnggota;
			
			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();
			while(rs.next()) { total = rs.getInt(1); }
			rs.close();
                   }
			return total;
               
		}catch(Exception e) {
			return 0;
		}finally {
			DBResultSet.close(dbrs);
		}
        }
}


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.common.entity.location;

import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.util.Command;
import com.dimata.util.lang.I_Language;
import com.mysql.jdbc.ResultSet;
import java.util.Hashtable;
import java.util.Vector;

/**
 *
 * @author Acer
 */
public class PstKabupaten extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {
    
    
    public static final  String TBL_HR_KABUPATEN = "hr_kabupaten";
        public static final String TBL_HR_PROPINSI = "hr_propinsi";
        public static final String TBL_HR_NEGARA = "hr_negara";

	public static final  int FLD_ID_KABUPATEN = 0;
	public static final  int FLD_KD_KABUPATEN = 1;
	public static final  int FLD_NM_KABUPATEN = 2;
        //public static final  int FLD_ID_NEGARA = 3;
        public static final  int FLD_ID_PROPINSI = 3;
        

    public static final  String[] fieldNames = {
		"ID_KAB",
		"KD_KAB",
		"NAMA_KAB",
                //"ID_NEG",
                "ID_PROP"
                
	 };

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_STRING,
		TYPE_STRING,
                //TYPE_LONG,
                TYPE_LONG
                
	 };

	public PstKabupaten(){
	}

	public PstKabupaten(int i) throws DBException {
		super(new PstKabupaten());
	}

	public PstKabupaten(String sOid) throws DBException {
		super(new PstKabupaten(0));
		if(!locate(sOid))
			throw new DBException(this,DBException.RECORD_NOT_FOUND);
		else
			return;
	}

	public PstKabupaten(long lOid) throws DBException {
		super(new PstKabupaten(0));
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
		return TBL_HR_KABUPATEN;
	}

	public String[] getFieldNames(){
		return fieldNames;
	}

	public int[] getFieldTypes(){
		return fieldTypes;
	}

	public String getPersistentName(){
		return new PstKabupaten().getClass().getName();
	}

	public long fetchExc(Entity ent) throws Exception{
		Kabupaten kabupaten = fetchExc(ent.getOID());
		ent = (Entity)kabupaten;
		return kabupaten.getOID();
	}

	public long insertExc(Entity ent) throws Exception{
		return insertExc((Kabupaten) ent);
	}

	public long updateExc(Entity ent) throws Exception{
		return updateExc((Kabupaten) ent);
	}

	public long deleteExc(Entity ent) throws Exception{
		if(ent==null){
			throw new DBException(this,DBException.RECORD_NOT_FOUND);
		}
		return deleteExc(ent.getOID());
	}

	public static Kabupaten fetchExc(long oid) throws DBException{
		try{
			Kabupaten kabupaten = new Kabupaten();
			PstKabupaten pstKabupaten = new PstKabupaten(oid);
			kabupaten.setOID(oid);

			kabupaten.setKdKabupaten(pstKabupaten.getString(FLD_KD_KABUPATEN));
			kabupaten.setNmKabupaten(pstKabupaten.getString(FLD_NM_KABUPATEN));
                        //kabupaten.setIdNegara(pstKabupaten.getlong(FLD_ID_NEGARA));
                        kabupaten.setIdPropinsi(pstKabupaten.getlong(FLD_ID_PROPINSI));
                        Provinsi prov = PstProvinsi.fetchExc(kabupaten.getIdPropinsi());
                        kabupaten.setIdNegara(prov.getIdNegara());



			return kabupaten;
		}catch(DBException dbe){
			throw dbe;
		}catch(Exception e){
			throw new DBException(new PstKabupaten(0),DBException.UNKNOWN);
		}
	}

	public static long insertExc(Kabupaten kabupaten) throws DBException{
		try{
			PstKabupaten pstKabupaten = new PstKabupaten(0);

			pstKabupaten.setString(FLD_KD_KABUPATEN, kabupaten.getKdKabupaten());
			pstKabupaten.setString(FLD_NM_KABUPATEN, kabupaten.getNmKabupaten());
                        //pstKabupaten.setLong(FLD_ID_NEGARA, kabupaten.getIdNegara());
                        pstKabupaten.setLong(FLD_ID_PROPINSI, kabupaten.getIdPropinsi());
                        

			pstKabupaten.insert();
			kabupaten.setOID(pstKabupaten.getlong(FLD_ID_KABUPATEN));
		}catch(DBException dbe){
			throw dbe;
		}catch(Exception e){
			throw new DBException(new PstKabupaten(0),DBException.UNKNOWN);
		}
		return kabupaten.getOID();
	}

	public static long updateExc(Kabupaten kabupaten) throws DBException{
		try{
			if(kabupaten.getOID() != 0){
				PstKabupaten pstKabupaten = new PstKabupaten(kabupaten.getOID());

				pstKabupaten.setString(FLD_KD_KABUPATEN, kabupaten.getKdKabupaten());
				pstKabupaten.setString(FLD_NM_KABUPATEN, kabupaten.getNmKabupaten());
                                //pstKabupaten.setLong(FLD_ID_NEGARA, kabupaten.getIdNegara());
                                pstKabupaten.setLong(FLD_ID_PROPINSI, kabupaten.getIdPropinsi());
                                

				pstKabupaten.update();
				return kabupaten.getOID();

			}
		}catch(DBException dbe){
			throw dbe;
		}catch(Exception e){
			throw new DBException(new PstKabupaten(0),DBException.UNKNOWN);
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{
		try{
			PstKabupaten pstKabupaten = new PstKabupaten(oid);
			pstKabupaten.delete();
		}catch(DBException dbe){
			throw dbe;
		}catch(Exception e){
			throw new DBException(new PstKabupaten(0),DBException.UNKNOWN);
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
			String sql = "SELECT * FROM " + TBL_HR_KABUPATEN;
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
				Kabupaten kabupaten = new Kabupaten();
				resultToObject(rs, kabupaten);
				lists.add(kabupaten);
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


        public static Vector listJoin(int limitStart,int recordToGet, String whereClause, String order){
		Vector lists = new Vector();
		DBResultSet dbrs = null;
		try {
                        /*
                         * SELECT kab.* FROM hr_kabupaten AS kab INNER JOIN hr_propinsi AS prov ON kab.ID_PROP=prov.ID_PROP
WHERE prov.NAMA_PROP='Seoul';
                         */
			String sql = " SELECT kab.* FROM " + TBL_HR_KABUPATEN +" AS kab "+
                                     " INNER JOIN "+PstProvinsi.TBL_HR_PROPINSI+" AS prov"+
                                     " ON kab."+PstKabupaten.fieldNames[PstKabupaten.FLD_ID_PROPINSI]+"=prov."+fieldNames[FLD_ID_PROPINSI];
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
				Kabupaten kabupaten = new Kabupaten();
				resultToObject(rs, kabupaten);
				lists.add(kabupaten);
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

	public static void resultToObject(ResultSet rs, Kabupaten kabupaten){
		try{
			kabupaten.setOID(rs.getLong(PstKabupaten.fieldNames[PstKabupaten.FLD_ID_KABUPATEN]));
			kabupaten.setKdKabupaten(rs.getString(PstKabupaten.fieldNames[PstKabupaten.FLD_KD_KABUPATEN]));
			kabupaten.setNmKabupaten(rs.getString(PstKabupaten.fieldNames[PstKabupaten.FLD_NM_KABUPATEN]));
                        //kabupaten.setIdNegara(rs.getLong(PstKabupaten.fieldNames[PstKabupaten.FLD_ID_NEGARA]));
                        kabupaten.setIdPropinsi(rs.getLong(PstKabupaten.fieldNames[PstKabupaten.FLD_ID_PROPINSI]));
                        

		}catch(Exception e){ }
	}

	public static boolean checkOID(long idKabupaten){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_KABUPATEN + " WHERE " +
						PstKabupaten.fieldNames[PstKabupaten.FLD_ID_KABUPATEN] + " = " + idKabupaten;

			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = (ResultSet) dbrs.getResultSet();

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
			String sql = "SELECT COUNT("+ PstKabupaten.fieldNames[PstKabupaten.FLD_ID_KABUPATEN] + ") FROM " + TBL_HR_KABUPATEN;
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


	/* This method used to find current datainduk */
	public static int findLimitStart( long oid, int recordToGet, String whereClause, String order){
		//String order = "";
		int size = getCount(whereClause);
		int start = 0;
		boolean found =false;
		for(int i=0; (i < size) && !found ; i=i+recordToGet){
			 Vector list =  list(i,recordToGet, whereClause, order);
			 start = i;
			 if(list.size()>0){
			  for(int ls=0;ls<list.size();ls++){
			  	   Kabupaten kabupaten = (Kabupaten)list.get(ls);
				   if(oid == kabupaten.getOID())
					  found=true;
			  }
		  }
		}
		if((start >= size) && (size > 0))
		    start = start - recordToGet;

		return start;
	}

    public static Hashtable getListKabPeg() {
        Hashtable lists = new Hashtable();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_HR_KABUPATEN;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = (ResultSet) dbrs.getResultSet();
            while (rs.next()) {
                Kabupaten pnsKab = new Kabupaten();
                resultToObject(rs, pnsKab);
                lists.put(pnsKab.getNmKabupaten().toUpperCase(),pnsKab);
            }
            rs.close();
            return lists;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Hashtable();
    }

    public static int findLimitCommand(int start, int recordToGet, int vectSize) {
        int cmd = Command.LIST;
        int mdl = vectSize % recordToGet;
        vectSize = vectSize + mdl;
        if (start == 0) {
            cmd = Command.FIRST;
        } else {
            if (start == (vectSize - recordToGet)) {
                cmd = Command.LAST;
            } else {
                start = start + recordToGet;
                if (start <= (vectSize - recordToGet)) {
                    cmd = Command.NEXT;
                    System.out.println("next.......................");
                } else {
                    start = start - recordToGet;
                    if (start > 0) {
                        cmd = Command.PREV;
                        System.out.println("prev.......................");
                    }
                }
            }
        }

        return cmd;
    }
    public static Vector listJoinKab(int limitStart,int recordToGet, String whereClause, String order){
		Vector lists = new Vector();
		DBResultSet dbrs = null;
		try {
			String sql = "SELECT * FROM " + TBL_HR_KABUPATEN  + " k "+
                                     " INNER JOIN " + TBL_HR_PROPINSI  + " p "+
                                     " ON k."+PstKabupaten.fieldNames[PstKabupaten.FLD_ID_PROPINSI]+
                                     " = p." +PstProvinsi.fieldNames[PstProvinsi.FLD_ID_PROVINSI]+
                                     " INNER JOIN " + TBL_HR_NEGARA+ " n " +
                                     " ON p."+PstProvinsi.fieldNames[PstProvinsi.FLD_ID_NEGARA]+
                                     " = n." +PstNegara.fieldNames[PstNegara.FLD_ID_NEGARA];

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
				Kabupaten kabupaten = new Kabupaten();
				resultToObject(rs, kabupaten);
				lists.add(kabupaten);
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
    
}

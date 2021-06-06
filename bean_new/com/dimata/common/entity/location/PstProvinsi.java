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
import com.dimata.util.lang.I_Language;
import com.mysql.jdbc.ResultSet;
import java.util.Hashtable;
import java.util.Vector;

/**
 *
 * @author Acer
 */
public class PstProvinsi extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {
    
    public static final  String TBL_HR_PROPINSI = "hr_propinsi";
        public static final  String TBL_HR_NEGARA = "hr_negara";

	public static final  int FLD_ID_PROVINSI = 0;
	public static final  int FLD_KD_PROVINSI = 1;
	public static final  int FLD_NM_PROVINSI = 2;
        public static final  int FLD_ID_NEGARA = 3;

    public static final  String[] fieldNames = {
		"ID_PROP",
		"KD_PROP",
		"NAMA_PROP",
                "ID_NEGARA"
	 };

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_STRING,
		TYPE_STRING,
                TYPE_LONG
	 };

	public PstProvinsi(){
	}

	public PstProvinsi(int i) throws DBException {
		super(new PstProvinsi());
	}

	public PstProvinsi(String sOid) throws DBException {
		super(new PstProvinsi(0));
		if(!locate(sOid))
			throw new DBException(this,DBException.RECORD_NOT_FOUND);
		else
			return;
	}

	public PstProvinsi(long lOid) throws DBException {
		super(new PstProvinsi(0));
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
		return TBL_HR_PROPINSI;
	}

	public String[] getFieldNames(){
		return fieldNames;
	}

	public int[] getFieldTypes(){
		return fieldTypes;
	}

	public String getPersistentName(){
		return new PstProvinsi().getClass().getName();
	}

	public long fetchExc(Entity ent) throws Exception{
		Provinsi provinsi = fetchExc(ent.getOID());
		ent = (Entity)provinsi;
		return provinsi.getOID();
	}

	public long insertExc(Entity ent) throws Exception{
		return insertExc((Provinsi) ent);
	}

	public long updateExc(Entity ent) throws Exception{
		return updateExc((Provinsi) ent);
	}

	public long deleteExc(Entity ent) throws Exception{
		if(ent==null){
			throw new DBException(this,DBException.RECORD_NOT_FOUND);
		}
		return deleteExc(ent.getOID());
	}

	public static Provinsi fetchExc(long oid) throws DBException{
		try{
			Provinsi provinsi = new Provinsi();
			PstProvinsi pstPropinsi = new PstProvinsi(oid);
			provinsi.setOID(oid);

			provinsi.setKdProvinsi(pstPropinsi.getString(FLD_KD_PROVINSI));
			provinsi.setNmProvinsi(pstPropinsi.getString(FLD_NM_PROVINSI));
                        provinsi.setIdNegara(pstPropinsi.getlong(FLD_ID_NEGARA));

			return provinsi;
		}catch(DBException dbe){
			throw dbe;
		}catch(Exception e){
			throw new DBException(new PstProvinsi(0),DBException.UNKNOWN);
		}
	}

	public static long insertExc(Provinsi provinsi) throws DBException{
		try{
			PstProvinsi pstPropinsi = new PstProvinsi(0);

			pstPropinsi.setString(FLD_KD_PROVINSI, provinsi.getKdProvinsi());
			pstPropinsi.setString(FLD_NM_PROVINSI, provinsi.getNmProvinsi());
                        pstPropinsi.setLong(FLD_ID_NEGARA, provinsi.getIdNegara());

			pstPropinsi.insert();
			provinsi.setOID(pstPropinsi.getlong(FLD_ID_PROVINSI));
		}catch(DBException dbe){
			throw dbe;
		}catch(Exception e){
			throw new DBException(new PstProvinsi(0),DBException.UNKNOWN);
		}
		return provinsi.getOID();
	}

	public static long updateExc(Provinsi provinsi) throws DBException{
		try{
			if(provinsi.getOID() != 0){
				PstProvinsi pstPropinsi = new PstProvinsi(provinsi.getOID());

				pstPropinsi.setString(FLD_KD_PROVINSI, provinsi.getKdProvinsi());
				pstPropinsi.setString(FLD_NM_PROVINSI, provinsi.getNmProvinsi());
                                pstPropinsi.setLong(FLD_ID_NEGARA, provinsi.getIdNegara());

				pstPropinsi.update();
				return provinsi.getOID();

			}
		}catch(DBException dbe){
			throw dbe;
		}catch(Exception e){
			throw new DBException(new PstProvinsi(0),DBException.UNKNOWN);
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{
		try{
			PstProvinsi pstPropinsi = new PstProvinsi(oid);
			pstPropinsi.delete();
		}catch(DBException dbe){
			throw dbe;
		}catch(Exception e){
			throw new DBException(new PstProvinsi(0),DBException.UNKNOWN);
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
			String sql = "SELECT * FROM " + TBL_HR_PROPINSI;
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
				Provinsi provinsi = new Provinsi();
				resultToObject(rs, provinsi);
				lists.add(provinsi);
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
                        /*SELECT prov.* FROM hr_propinsi AS prov INNER JOIN hr_negara AS neg ON prov.ID_NEGARA=neg.ID_NEGARA
WHERE neg.NAMA_NGR='Republic of Korea';*/
			String sql = " SELECT prov.* FROM " + TBL_HR_PROPINSI + " AS prov " +
                                     " INNER JOIN "+PstNegara.TBL_BKD_NEGARA+" AS neg "+
                                     " ON prov."+PstProvinsi.fieldNames[PstProvinsi.FLD_ID_NEGARA]+"= neg."+fieldNames[FLD_ID_NEGARA];
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
				Provinsi provinsi = new Provinsi();
				resultToObject(rs, provinsi);
				lists.add(provinsi);
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

	private static void resultToObject(ResultSet rs, Provinsi provinsi){
		try{
			provinsi.setOID(rs.getLong(PstProvinsi.fieldNames[PstProvinsi.FLD_ID_PROVINSI]));
			provinsi.setKdProvinsi(rs.getString(PstProvinsi.fieldNames[PstProvinsi.FLD_KD_PROVINSI]));
			provinsi.setNmProvinsi(rs.getString(PstProvinsi.fieldNames[PstProvinsi.FLD_NM_PROVINSI]));
                        provinsi.setIdNegara(rs.getLong(PstProvinsi.fieldNames[PstProvinsi.FLD_ID_NEGARA]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long idPropinsi){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_PROPINSI + " WHERE " +
						PstProvinsi.fieldNames[PstProvinsi.FLD_ID_PROVINSI] + " = " + idPropinsi;

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
			String sql = ""
                                + " SELECT COUNT(hrp."+ PstProvinsi.fieldNames[PstProvinsi.FLD_ID_PROVINSI] + ") "
                                + " FROM " + TBL_HR_PROPINSI+" as hrp"
                                + " INNER JOIN " +TBL_HR_NEGARA+" as hrn"
                                + " ON hrp."+PstProvinsi.fieldNames[PstProvinsi.FLD_ID_NEGARA]+"= hrn."+PstNegara.fieldNames[PstNegara.FLD_ID_NEGARA]+"";
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
			  	   Provinsi provinsi = (Provinsi)list.get(ls);
				   if(oid == provinsi.getOID())
					  found=true;
			  }
		  }
		}
		if((start >= size) && (size > 0))
		    start = start - recordToGet;

		return start;
	}

      public static Hashtable getListPropPeg() {
        Hashtable lists = new Hashtable();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_HR_PROPINSI;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = (ResultSet) dbrs.getResultSet();
            while (rs.next()) {
                Provinsi pnsPropinsi = new Provinsi();
                resultToObject(rs, pnsPropinsi);
                lists.put(pnsPropinsi.getNmProvinsi().toUpperCase(),pnsPropinsi);
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


    public static Vector listJoinPro(int limitStart,int recordToGet, String whereClause, String order){
		Vector lists = new Vector();
		DBResultSet dbrs = null;
		try {
			String sql = "SELECT * FROM " + TBL_HR_PROPINSI  + " p "+
                                     " INNER JOIN " + TBL_HR_NEGARA + " n "+
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
				Provinsi provinsi = new Provinsi();
				resultToObject(rs, provinsi);
				lists.add(provinsi);
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

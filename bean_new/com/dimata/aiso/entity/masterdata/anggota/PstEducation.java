/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.aiso.entity.masterdata.anggota;

import com.dimata.qdep.db.DBException;
import com.dimata.qdep.db.DBHandler;
import com.dimata.qdep.db.DBResultSet;
import com.dimata.qdep.db.I_DBInterface;
import com.dimata.qdep.db.I_DBType;
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.util.lang.I_Language;
import java.sql.ResultSet;
import java.util.Vector;

/**
 *
 * @author HaddyPuutraa (PKL)
 * Create Kamis, 21 Pebruari 2013
 */
public class PstEducation extends DBHandler implements I_Language, I_DBType, I_DBInterface, I_PersintentExc{
    public static final String TBL_NAME = "aiso_education";
    
    public static final int FLD_EDUCATION_ID = 0;
    public static final int FLD_EDUCATION = 1;
    public static final int FLD_EDUCATION_DESC = 2;
    public static final int FLD_EDUCATION_CODE = 3;
    
    public static String[] fieldNames = {
        "EDUCATION_ID",
        "EDUCATION",
        "EDUCATION_DESC",
        "EDUCATION_CODE"
    };
    
    public static int[] fieldTypes = {
        TYPE_ID + TYPE_LONG + TYPE_PK,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING
    };
    
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
        return TBL_NAME;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstEducation().getClass().getName();
    }

    public PstEducation() {
    }
    
    public PstEducation(int i) throws DBException{
        super(new PstEducation());
    }
    
    public PstEducation(String sOid) throws DBException { 
	super(new PstEducation(0)); 
	if(!locate(sOid)){ 
            throw new DBException(this,DBException.RECORD_NOT_FOUND); 
        }else{ 
            return;
        }
    }

    public PstEducation(long lOid) throws DBException { 
	super(new PstEducation(0)); 
	String sOid = "0"; 
	try { 
            sOid = String.valueOf(lOid); 
	}catch(Exception e) { 
            throw new DBException(this,DBException.RECORD_NOT_FOUND); 
	} 
            
        if(!locate(sOid)){ 
            throw new DBException(this,DBException.RECORD_NOT_FOUND); 
        }else{ 
            return;        
        }
    }
    
    public static Education fetchExc(long oid) throws DBException{ 
        try{ 
            Education education = new Education();
            PstEducation pstEducation = new PstEducation(oid); 
            education.setOID(oid);

            education.setEducation(pstEducation.getString(FLD_EDUCATION));
            education.setEducationDesc(pstEducation.getString(FLD_EDUCATION_DESC));
            education.setEducationCode(pstEducation.getString(FLD_EDUCATION_CODE));
            return education; 
	}catch(DBException dbe){ 
            throw dbe; 
	}catch(Exception e){ 
            throw new DBException(new PstEducation(0),DBException.UNKNOWN); 
	} 
    }
    
    public long fetchExc(Entity ent) throws Exception{ 
        Education education = fetchExc(ent.getOID()); 
	ent = (Entity)education; 
	return education.getOID(); 
    }
    
    public static long insertExc(Education education) throws DBException{ 
	try{ 
            PstEducation pstEducation = new PstEducation(0);

            pstEducation.setString(FLD_EDUCATION, education.getEducation());
            pstEducation.setString(FLD_EDUCATION_DESC, education.getEducationDesc());
            pstEducation.setString(FLD_EDUCATION_CODE, education.getEducationCode());
            
            pstEducation.insert(); 
            education.setOID(pstEducation.getlong(FLD_EDUCATION_ID));
	}catch(DBException dbe){ 
            throw dbe; 
	}catch(Exception e){ 
            throw new DBException(new PstEducation(0),DBException.UNKNOWN); 
	}
	return education.getOID();
    }
    
    public long insertExc(Entity ent) throws Exception{ 
	return insertExc((Education) ent); 
    }
    
    public static long updateExc(Education education) throws DBException{ 
        try{ 
            if(education.getOID() != 0){ 
                PstEducation pstEducation = new PstEducation(education.getOID());

                pstEducation.setString(FLD_EDUCATION, education.getEducation());
                pstEducation.setString(FLD_EDUCATION_DESC, education.getEducationDesc());
                pstEducation.setString(FLD_EDUCATION_CODE, education.getEducationCode());
            
                pstEducation.update(); 
                return education.getOID();

            }
	}catch(DBException dbe){ 
            throw dbe; 
	}catch(Exception e){ 
            throw new DBException(new PstEducation(0),DBException.UNKNOWN); 
	}
	return 0;
    }
    
    public long updateExc(Entity ent) throws Exception{ 
        return updateExc((Education) ent); 
    }
    
    public static long deleteExc(long oid) throws DBException{ 
        try{ 
            PstEducation pstEducation = new PstEducation(oid);
            pstEducation.delete();
	}catch(DBException dbe){ 
            throw dbe; 
	}catch(Exception e){ 
            throw new DBException(new PstEducation(0),DBException.UNKNOWN); 
	}
	return oid;
    }
    
    public long deleteExc(Entity ent) throws Exception{ 
        if(ent==null){ 
            throw new DBException(this,DBException.RECORD_NOT_FOUND); 
	} 
	return deleteExc(ent.getOID()); 
    }
    
    public static int getCount(String whereClause){
        DBResultSet dbrs = null;
	try {
            String sql = "SELECT COUNT("+ PstEducation.fieldNames[PstEducation.FLD_EDUCATION_ID] + ") FROM " + TBL_NAME;
            if(whereClause != null && whereClause.length() > 0){
                sql = sql + " WHERE " + whereClause;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            int count = 0;
            while(rs.next()) { 
                count = rs.getInt(1); 
            }

            rs.close();
            return count;
	}catch(Exception e) {
            return 0;
	}finally {
            DBResultSet.close(dbrs);
	}
    }
    
    public static Vector listAll(){
        return list(0, 0, "","");
    }
    
    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM "+ TBL_NAME;
            
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            if (limitStart == 0 && recordToGet == 0) {
                sql = sql + "";
            } else {
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }
            dbrs = DBHandler.execQueryResult(sql);
            com.mysql.jdbc.ResultSet rs = (com.mysql.jdbc.ResultSet) dbrs.getResultSet();
            while (rs.next()) {
                Education education = new Education();
                resultToObject(rs, education);
                lists.add(education);
            }
            return lists;
        } catch (Exception e) {          
            System.out.println(e);
        }finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }
    
    private static void resultToObject(ResultSet rs, Education education){
        try{
            education.setOID(rs.getLong(PstEducation.fieldNames[PstEducation.FLD_EDUCATION_ID]));
            education.setEducation(rs.getString(PstEducation.fieldNames[PstEducation.FLD_EDUCATION]));
            education.setEducationDesc(rs.getString(PstEducation.fieldNames[PstEducation.FLD_EDUCATION_DESC]));
            education.setEducationCode(rs.getString(PstEducation.fieldNames[PstEducation.FLD_EDUCATION_CODE]));
            
	}catch(Exception e){ }
    }
}


/* Created on 	:  [date] [time] AM/PM 
 * 
 * @author  	: lkarunia
 * @version  	: 01
 */

/*******************************************************************
 * Class Description 	: [project description ... ] 
 * Imput Parameters 	: [input parameter ...] 
 * Output 		: [output ...] 
 *******************************************************************/

package com.dimata.harisma.entity.employee;

/* package java */ 
import java.io.*
;
import java.sql.*
;import java.util.*
;import java.util.Date;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.util.*;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;



import com.dimata.qdep.entity.*;

/* package  harisma */
//import com.dimata.harisma.db.DBHandler;
//import com.dimata.harisma.db.DBException;
//import com.dimata.harisma.db.DBLogger;
import com.dimata.harisma.entity.employee.*; 

public class PstFamilyMember extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_FAMILY_MEMBER = "HR_FAMILY_MEMBER";

	public static final  int FLD_FAMILY_MEMBER_ID = 0;
	public static final  int FLD_EMPLOYEE_ID = 1;
	public static final  int FLD_FULL_NAME = 2;
	public static final  int FLD_RELATIONSHIP = 3;
	public static final  int FLD_BIRTH_DATE = 4;
	public static final  int FLD_JOB = 5;
	public static final  int FLD_ADDRESS = 6;
    public static final  int FLD_GUARANTEED = 7;
	public static final  int FLD_IGNORE_BIRTH = 8;

	public static final  String[] fieldNames = {
		"FAMILY_MEMBER_ID",
		"EMPLOYEE_ID",
		"FULL_NAME",
		"RELATIONSHIP",
		"BIRTH_DATE",
		"JOB",
		"ADDRESS",
        "GUARANTEED",
        "IGNORE_BIRTH"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_LONG,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_DATE,
		TYPE_STRING,
		TYPE_STRING,
        TYPE_BOOL,
        TYPE_BOOL
        };


    public static final int REL_WIFE			= 0;
    public static final int REL_HUSBAND			= 1;
    public static final int REL_CHILDREN		= 2;

    public static String[] relationValue = {"Wife","Husband","Children"};

    public static Vector getRelation(){
    	Vector result = new Vector(1,1);
        for(int i=0;i<relationValue.length;i++){
        	result.add(relationValue[i]);
        }
        return result;
    }

	public PstFamilyMember(){
	}

	public PstFamilyMember(int i) throws DBException { 
		super(new PstFamilyMember()); 
	}

	public PstFamilyMember(String sOid) throws DBException { 
		super(new PstFamilyMember(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstFamilyMember(long lOid) throws DBException { 
		super(new PstFamilyMember(0)); 
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
		return TBL_HR_FAMILY_MEMBER;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstFamilyMember().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		FamilyMember familymember = fetchExc(ent.getOID()); 
		ent = (Entity)familymember; 
		return familymember.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((FamilyMember) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((FamilyMember) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static FamilyMember fetchExc(long oid) throws DBException{ 
		try{ 
			FamilyMember familymember = new FamilyMember();
			PstFamilyMember pstFamilyMember = new PstFamilyMember(oid); 
			familymember.setOID(oid);

			familymember.setEmployeeId(pstFamilyMember.getlong(FLD_EMPLOYEE_ID));
			familymember.setFullName(pstFamilyMember.getString(FLD_FULL_NAME));
			familymember.setRelationship(pstFamilyMember.getString(FLD_RELATIONSHIP));
			familymember.setBirthDate(pstFamilyMember.getDate(FLD_BIRTH_DATE));
			familymember.setJob(pstFamilyMember.getString(FLD_JOB));
			familymember.setAddress(pstFamilyMember.getString(FLD_ADDRESS));
            familymember.setGuaranteed(pstFamilyMember.getboolean(FLD_GUARANTEED));
            familymember.setIgnoreBirth(pstFamilyMember.getboolean(FLD_IGNORE_BIRTH));

			return familymember; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstFamilyMember(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(FamilyMember familymember) throws DBException{ 
		try{ 
			PstFamilyMember pstFamilyMember = new PstFamilyMember(0);

			pstFamilyMember.setLong(FLD_EMPLOYEE_ID, familymember.getEmployeeId());
			pstFamilyMember.setString(FLD_FULL_NAME, familymember.getFullName());
			pstFamilyMember.setString(FLD_RELATIONSHIP, familymember.getRelationship());
			pstFamilyMember.setDate(FLD_BIRTH_DATE, familymember.getBirthDate());
			pstFamilyMember.setString(FLD_JOB, familymember.getJob());
			pstFamilyMember.setString(FLD_ADDRESS, familymember.getAddress());
            pstFamilyMember.setboolean(FLD_GUARANTEED, familymember.getGuaranteed());
            pstFamilyMember.setboolean(FLD_IGNORE_BIRTH, familymember.getIgnoreBirth());

			pstFamilyMember.insert(); 
			familymember.setOID(pstFamilyMember.getlong(FLD_FAMILY_MEMBER_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstFamilyMember(0),DBException.UNKNOWN); 
		}
		return familymember.getOID();
	}

	public static long updateExc(FamilyMember familymember) throws DBException{ 
		try{ 
			if(familymember.getOID() != 0){ 
				PstFamilyMember pstFamilyMember = new PstFamilyMember(familymember.getOID());

				pstFamilyMember.setLong(FLD_EMPLOYEE_ID, familymember.getEmployeeId());
				pstFamilyMember.setString(FLD_FULL_NAME, familymember.getFullName());
				pstFamilyMember.setString(FLD_RELATIONSHIP, familymember.getRelationship());
				pstFamilyMember.setDate(FLD_BIRTH_DATE, familymember.getBirthDate());
				pstFamilyMember.setString(FLD_JOB, familymember.getJob());
				pstFamilyMember.setString(FLD_ADDRESS, familymember.getAddress());
                pstFamilyMember.setboolean(FLD_GUARANTEED, familymember.getGuaranteed());
                pstFamilyMember.setboolean(FLD_IGNORE_BIRTH, familymember.getIgnoreBirth());

				pstFamilyMember.update(); 
				return familymember.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstFamilyMember(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstFamilyMember pstFamilyMember = new PstFamilyMember(oid);
			pstFamilyMember.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstFamilyMember(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_HR_FAMILY_MEMBER; 
			if(whereClause != null && whereClause.length() > 0)
				sql = sql + " WHERE " + whereClause;
			if(order != null && order.length() > 0)
				sql = sql + " ORDER BY " + order;
			if(limitStart == 0 && recordToGet == 0)
				sql = sql + "";
			else
				sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;
                        System.out.println(sql);
			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();
			while(rs.next()) {
				FamilyMember familymember = new FamilyMember();
				resultToObject(rs, familymember);
				lists.add(familymember);
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

	private static void resultToObject(ResultSet rs, FamilyMember familymember){
		try{
			familymember.setOID(rs.getLong(PstFamilyMember.fieldNames[PstFamilyMember.FLD_FAMILY_MEMBER_ID]));
			familymember.setEmployeeId(rs.getLong(PstFamilyMember.fieldNames[PstFamilyMember.FLD_EMPLOYEE_ID]));
			familymember.setFullName(rs.getString(PstFamilyMember.fieldNames[PstFamilyMember.FLD_FULL_NAME]));
			familymember.setRelationship(rs.getString(PstFamilyMember.fieldNames[PstFamilyMember.FLD_RELATIONSHIP]));
			familymember.setBirthDate(rs.getDate(PstFamilyMember.fieldNames[PstFamilyMember.FLD_BIRTH_DATE]));
			familymember.setJob(rs.getString(PstFamilyMember.fieldNames[PstFamilyMember.FLD_JOB]));
			familymember.setAddress(rs.getString(PstFamilyMember.fieldNames[PstFamilyMember.FLD_ADDRESS]));
            familymember.setGuaranteed(rs.getBoolean(PstFamilyMember.fieldNames[PstFamilyMember.FLD_GUARANTEED]));
            familymember.setIgnoreBirth(rs.getBoolean(PstFamilyMember.fieldNames[PstFamilyMember.FLD_IGNORE_BIRTH]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long familyMemberId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_FAMILY_MEMBER + " WHERE " + 
						PstFamilyMember.fieldNames[PstFamilyMember.FLD_FAMILY_MEMBER_ID] + " = " + familyMemberId;

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
			String sql = "SELECT COUNT("+ PstFamilyMember.fieldNames[PstFamilyMember.FLD_FAMILY_MEMBER_ID] + ") FROM " + TBL_HR_FAMILY_MEMBER;
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
			  	   FamilyMember familymember = (FamilyMember)list.get(ls);
				   if(oid == familymember.getOID())
					  found=true;
			  }
		  }
		}
		if((start >= size) && (size > 0))
		    start = start - recordToGet;

		return start;
	}


     public static int findLimitCommand(int start, int recordToGet, int vectSize)
    {
        int cmd = Command.LIST;
        int mdl = vectSize % recordToGet;
        vectSize = vectSize + mdl;
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


    public static long deleteByEmployee(long emplOID)
    {
    	try{
            String sql = " DELETE FROM "+PstFamilyMember.TBL_HR_FAMILY_MEMBER+
                		 " WHERE "+PstFamilyMember.fieldNames[PstFamilyMember.FLD_EMPLOYEE_ID] +
                         " = "+emplOID;

            int status = DBHandler.execUpdate(sql);
    	}catch(Exception exc){
        	System.out.println("error delete fam member by employee "+exc.toString());
    	}

    	return emplOID;
    }




}

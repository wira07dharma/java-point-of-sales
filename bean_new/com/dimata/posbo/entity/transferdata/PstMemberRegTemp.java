
/* Created on 	:  [date] [time] AM/PM 
 * 
 * @author	 :
 * @version	 :
 */

/*******************************************************************
 * Class Description 	: [project description ... ] 
 * Imput Parameters 	: [input parameter ...] 
 * Output 		: [output ...] 
 *******************************************************************/

package com.dimata.posbo.entity.transferdata; 

/* package java */ 
import java.io.*
;
import java.sql.*
;import java.util.*
;import java.util.Date;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.*;
import com.dimata.qdep.entity.*;

/* package posbo */
//import com.dimata.posbo.db.DBHandler;
//import com.dimata.posbo.db.DBException;
//import com.dimata.posbo.db.DBLogger;
import com.dimata.posbo.entity.masterdata.*; 
import com.dimata.common.entity.contact.*; 
import com.dimata.util.*; 
import org.json.JSONObject;

public class PstMemberRegTemp extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	//public static final  String TBL_CONTACT_LIST = "CONTACT_LIST_TEMP";
        public static final  String TBL_CONTACT_LIST = "contact_list_temp";

	public static final  int FLD_CONTACT_ID = 0;
	public static final  int FLD_CONTACT_CODE = 1;
	public static final  int FLD_REGDATE = 2;
	public static final  int FLD_EMPLOYEE_ID = 3;
	public static final  int FLD_COMP_NAME = 4;
	public static final  int FLD_PERSON_NAME = 5;
	public static final  int FLD_PERSON_LASTNAME = 6;
	public static final  int FLD_BUSS_ADDRESS = 7;
	public static final  int FLD_TOWN = 8;
	public static final  int FLD_PROVINCE = 9;
	public static final  int FLD_COUNTRY = 10;
	public static final  int FLD_TELP_NR = 11;
	public static final  int FLD_TELP_MOBILE = 12;
	public static final  int FLD_FAX = 13;
	public static final  int FLD_HOME_ADDR = 14;
	public static final  int FLD_HOME_TOWN = 15;
	public static final  int FLD_HOME_PROVINCE = 16;
	public static final  int FLD_HOME_COUNTRY = 17;
	public static final  int FLD_HOME_TELP = 18;
	public static final  int FLD_HOME_FAX = 19;
	public static final  int FLD_NOTES = 20;
	public static final  int FLD_DIRECTIONS = 21;
	public static final  int FLD_BANK_ACC = 22;
	public static final  int FLD_BANK_ACC2 = 23;
	public static final  int FLD_CONTACT_TYPE = 24;
	public static final  int FLD_EMAIL = 25;
	public static final  int FLD_PARENT_ID = 26;
	public static final  int FLD_MEMBER_GROUP_ID = 27;
	public static final  int FLD_MEMBER_BARCODE = 28;
	public static final  int FLD_MEMBER_ID_CARD_NUMBER = 29;
	public static final  int FLD_MEMBER_SEX = 30;
	public static final  int FLD_MEMBER_BIRTH_DATE = 31;
	public static final  int FLD_MEMBER_COUNTER = 32;
	public static final  int FLD_MEMBER_RELIGION_ID = 33;
	public static final  int FLD_MEMBER_STATUS = 34;
	public static final  int FLD_MEMBER_LAST_UPDATE = 35;
        public static final  int FLD_PROCESS_STATUS = 36;

	public static final  String[] fieldNames = {
		"CONTACT_ID",
		"CONTACT_CODE",
		"REGDATE",
		"EMPLOYEE_ID",
		"COMP_NAME",
		"PERSON_NAME",
		"PERSON_LASTNAME",
		"BUSS_ADDRESS",
		"TOWN",
		"PROVINCE",
		"COUNTRY",
		"TELP_NR",
		"TELP_MOBILE",
		"FAX",
		"HOME_ADDR",
		"HOME_TOWN",
		"HOME_PROVINCE",
		"HOME_COUNTRY",
		"HOME_TELP",
		"HOME_FAX",
		"NOTES",
		"DIRECTIONS",
		"BANK_ACC",
		"BANK_ACC2",
		"CONTACT_TYPE",
		"EMAIL",
		"PARENT_ID",
		"MEMBER_GROUP_ID",
		"MEMBER_BARCODE",
		"MEMBER_ID_CARD_NUMBER",
		"MEMBER_SEX",
		"MEMBER_BIRTH_DATE",
		"MEMBER_COUNTER",
		"MEMBER_RELIGION_ID",
		"MEMBER_STATUS",
		"MEMBER_LAST_UPDATE",
                "PROCESS_STATUS"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_STRING,
		TYPE_DATE,
		TYPE_LONG,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_INT,
		TYPE_STRING,
		TYPE_LONG,
		TYPE_LONG,
		TYPE_STRING,
		TYPE_STRING,
		TYPE_INT,
		TYPE_DATE,
		TYPE_INT,
		TYPE_LONG,
		TYPE_INT,
		TYPE_DATE,
                TYPE_INT
	 }; 

         
        public static final int  VALID = 1;
        public static final int  NOT_VALID = 0;
        
        public static String statusNames[][]={
            {"Tidak Berlaku","Berlaku"},
            {"Not Valid","Valid"}
        };
        
        public static final int MALE = 0;
        public static final int FEMALE = 1;
        
        public static String sexNames[][]={
            {"Pria", "Wanita"},
            {"Male","Female"}
        };
        
        public static final int INSERT = 0;
        public static final int UPDATE = 1;
        public static final int DELETE = 2;
        
	public PstMemberRegTemp(){
	}

	public PstMemberRegTemp(int i) throws DBException { 
		super(new PstMemberRegTemp()); 
	}

	public PstMemberRegTemp(String sOid) throws DBException { 
		super(new PstMemberRegTemp(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstMemberRegTemp(long lOid) throws DBException { 
		super(new PstMemberRegTemp(0)); 
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
		return TBL_CONTACT_LIST;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstMemberRegTemp().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		MemberReg memberreg = fetchExc(ent.getOID()); 
		ent = (Entity)memberreg; 
		return memberreg.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((MemberReg) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((MemberReg) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static MemberReg fetchExc(long oid) throws DBException{ 
		try{ 
			MemberReg memberreg = new MemberReg();
			PstMemberRegTemp pstMemberReg = new PstMemberRegTemp(oid); 
			memberreg.setOID(oid);

			memberreg.setContactCode(pstMemberReg.getString(FLD_CONTACT_CODE));
			memberreg.setRegdate(pstMemberReg.getDate(FLD_REGDATE));
			memberreg.setEmployeeId(pstMemberReg.getlong(FLD_EMPLOYEE_ID));
			memberreg.setCompName(pstMemberReg.getString(FLD_COMP_NAME));
			memberreg.setPersonName(pstMemberReg.getString(FLD_PERSON_NAME));
			memberreg.setPersonLastname(pstMemberReg.getString(FLD_PERSON_LASTNAME));
			memberreg.setBussAddress(pstMemberReg.getString(FLD_BUSS_ADDRESS));
			memberreg.setTown(pstMemberReg.getString(FLD_TOWN));
			memberreg.setProvince(pstMemberReg.getString(FLD_PROVINCE));
			memberreg.setCountry(pstMemberReg.getString(FLD_COUNTRY));
			memberreg.setTelpNr(pstMemberReg.getString(FLD_TELP_NR));
			memberreg.setTelpMobile(pstMemberReg.getString(FLD_TELP_MOBILE));
			memberreg.setFax(pstMemberReg.getString(FLD_FAX));
			memberreg.setHomeAddr(pstMemberReg.getString(FLD_HOME_ADDR));
			memberreg.setHomeTown(pstMemberReg.getString(FLD_HOME_TOWN));
			memberreg.setHomeProvince(pstMemberReg.getString(FLD_HOME_PROVINCE));
			memberreg.setHomeCountry(pstMemberReg.getString(FLD_HOME_COUNTRY));
			memberreg.setHomeTelp(pstMemberReg.getString(FLD_HOME_TELP));
			memberreg.setHomeFax(pstMemberReg.getString(FLD_HOME_FAX));
			memberreg.setNotes(pstMemberReg.getString(FLD_NOTES));
			memberreg.setDirections(pstMemberReg.getString(FLD_DIRECTIONS));
			memberreg.setBankAcc(pstMemberReg.getString(FLD_BANK_ACC));
			memberreg.setBankAcc2(pstMemberReg.getString(FLD_BANK_ACC2));
			memberreg.setContactType(pstMemberReg.getInt(FLD_CONTACT_TYPE));
			memberreg.setEmail(pstMemberReg.getString(FLD_EMAIL));
			memberreg.setParentId(pstMemberReg.getlong(FLD_PARENT_ID));
			memberreg.setMemberGroupId(pstMemberReg.getlong(FLD_MEMBER_GROUP_ID));
			memberreg.setMemberBarcode(pstMemberReg.getString(FLD_MEMBER_BARCODE));
			memberreg.setMemberIdCardNumber(pstMemberReg.getString(FLD_MEMBER_ID_CARD_NUMBER));
			memberreg.setMemberSex(pstMemberReg.getInt(FLD_MEMBER_SEX));
			memberreg.setMemberBirthDate(pstMemberReg.getDate(FLD_MEMBER_BIRTH_DATE));
			memberreg.setMemberCounter(pstMemberReg.getInt(FLD_MEMBER_COUNTER));
			memberreg.setMemberReligionId(pstMemberReg.getlong(FLD_MEMBER_RELIGION_ID));
			memberreg.setMemberStatus(pstMemberReg.getInt(FLD_MEMBER_STATUS));
			memberreg.setMemberLastUpdate(pstMemberReg.getDate(FLD_MEMBER_LAST_UPDATE));
                        memberreg.setProcessStatus(pstMemberReg.getInt(FLD_PROCESS_STATUS));
                        
			return memberreg; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstMemberRegTemp(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(MemberReg memberreg) throws DBException{ 
		try{ 
			PstMemberRegTemp pstMemberReg = new PstMemberRegTemp(0);

			pstMemberReg.setString(FLD_CONTACT_CODE, memberreg.getContactCode());
			pstMemberReg.setDate(FLD_REGDATE, memberreg.getRegdate());
			pstMemberReg.setLong(FLD_EMPLOYEE_ID, memberreg.getEmployeeId());
			pstMemberReg.setString(FLD_COMP_NAME, memberreg.getCompName());
			pstMemberReg.setString(FLD_PERSON_NAME, memberreg.getPersonName());
			pstMemberReg.setString(FLD_PERSON_LASTNAME, memberreg.getPersonLastname());
			pstMemberReg.setString(FLD_BUSS_ADDRESS, memberreg.getBussAddress());
			pstMemberReg.setString(FLD_TOWN, memberreg.getTown());
			pstMemberReg.setString(FLD_PROVINCE, memberreg.getProvince());
			pstMemberReg.setString(FLD_COUNTRY, memberreg.getCountry());
			pstMemberReg.setString(FLD_TELP_NR, memberreg.getTelpNr());
			pstMemberReg.setString(FLD_TELP_MOBILE, memberreg.getTelpMobile());
			pstMemberReg.setString(FLD_FAX, memberreg.getFax());
			pstMemberReg.setString(FLD_HOME_ADDR, memberreg.getHomeAddr());
			pstMemberReg.setString(FLD_HOME_TOWN, memberreg.getHomeTown());
			pstMemberReg.setString(FLD_HOME_PROVINCE, memberreg.getHomeProvince());
			pstMemberReg.setString(FLD_HOME_COUNTRY, memberreg.getHomeCountry());
			pstMemberReg.setString(FLD_HOME_TELP, memberreg.getHomeTelp());
			pstMemberReg.setString(FLD_HOME_FAX, memberreg.getHomeFax());
			pstMemberReg.setString(FLD_NOTES, memberreg.getNotes());
			pstMemberReg.setString(FLD_DIRECTIONS, memberreg.getDirections());
			pstMemberReg.setString(FLD_BANK_ACC, memberreg.getBankAcc());
			pstMemberReg.setString(FLD_BANK_ACC2, memberreg.getBankAcc2());
			pstMemberReg.setInt(FLD_CONTACT_TYPE, memberreg.getContactType());
			pstMemberReg.setString(FLD_EMAIL, memberreg.getEmail());
			pstMemberReg.setLong(FLD_PARENT_ID, memberreg.getParentId());
			pstMemberReg.setLong(FLD_MEMBER_GROUP_ID, memberreg.getMemberGroupId());
			pstMemberReg.setString(FLD_MEMBER_BARCODE, memberreg.getMemberBarcode());
			pstMemberReg.setString(FLD_MEMBER_ID_CARD_NUMBER, memberreg.getMemberIdCardNumber());
			pstMemberReg.setInt(FLD_MEMBER_SEX, memberreg.getMemberSex());
			pstMemberReg.setDate(FLD_MEMBER_BIRTH_DATE, memberreg.getMemberBirthDate());
			pstMemberReg.setInt(FLD_MEMBER_COUNTER, memberreg.getMemberCounter());
			pstMemberReg.setLong(FLD_MEMBER_RELIGION_ID, memberreg.getMemberReligionId());
			pstMemberReg.setInt(FLD_MEMBER_STATUS, memberreg.getMemberStatus());
			pstMemberReg.setDate(FLD_MEMBER_LAST_UPDATE, memberreg.getMemberLastUpdate());
                        pstMemberReg.setInt(FLD_PROCESS_STATUS, memberreg.getProcessStatus());

			pstMemberReg.insert(); 
			memberreg.setOID(pstMemberReg.getlong(FLD_CONTACT_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstMemberRegTemp(0),DBException.UNKNOWN); 
		}
		return memberreg.getOID();
	}

	public static long updateExc(MemberReg memberreg) throws DBException{ 
		try{ 
			if(memberreg.getOID() != 0){ 
				PstMemberRegTemp pstMemberReg = new PstMemberRegTemp(memberreg.getOID());

				pstMemberReg.setString(FLD_CONTACT_CODE, memberreg.getContactCode());
				pstMemberReg.setDate(FLD_REGDATE, memberreg.getRegdate());
				pstMemberReg.setLong(FLD_EMPLOYEE_ID, memberreg.getEmployeeId());
				pstMemberReg.setString(FLD_COMP_NAME, memberreg.getCompName());
				pstMemberReg.setString(FLD_PERSON_NAME, memberreg.getPersonName());
				pstMemberReg.setString(FLD_PERSON_LASTNAME, memberreg.getPersonLastname());
				pstMemberReg.setString(FLD_BUSS_ADDRESS, memberreg.getBussAddress());
				pstMemberReg.setString(FLD_TOWN, memberreg.getTown());
				pstMemberReg.setString(FLD_PROVINCE, memberreg.getProvince());
				pstMemberReg.setString(FLD_COUNTRY, memberreg.getCountry());
				pstMemberReg.setString(FLD_TELP_NR, memberreg.getTelpNr());
				pstMemberReg.setString(FLD_TELP_MOBILE, memberreg.getTelpMobile());
				pstMemberReg.setString(FLD_FAX, memberreg.getFax());
				pstMemberReg.setString(FLD_HOME_ADDR, memberreg.getHomeAddr());
				pstMemberReg.setString(FLD_HOME_TOWN, memberreg.getHomeTown());
				pstMemberReg.setString(FLD_HOME_PROVINCE, memberreg.getHomeProvince());
				pstMemberReg.setString(FLD_HOME_COUNTRY, memberreg.getHomeCountry());
				pstMemberReg.setString(FLD_HOME_TELP, memberreg.getHomeTelp());
				pstMemberReg.setString(FLD_HOME_FAX, memberreg.getHomeFax());
				pstMemberReg.setString(FLD_NOTES, memberreg.getNotes());
				pstMemberReg.setString(FLD_DIRECTIONS, memberreg.getDirections());
				pstMemberReg.setString(FLD_BANK_ACC, memberreg.getBankAcc());
				pstMemberReg.setString(FLD_BANK_ACC2, memberreg.getBankAcc2());
				pstMemberReg.setInt(FLD_CONTACT_TYPE, memberreg.getContactType());
				pstMemberReg.setString(FLD_EMAIL, memberreg.getEmail());
				pstMemberReg.setLong(FLD_PARENT_ID, memberreg.getParentId());
				pstMemberReg.setLong(FLD_MEMBER_GROUP_ID, memberreg.getMemberGroupId());
				pstMemberReg.setString(FLD_MEMBER_BARCODE, memberreg.getMemberBarcode());
				pstMemberReg.setString(FLD_MEMBER_ID_CARD_NUMBER, memberreg.getMemberIdCardNumber());
				pstMemberReg.setInt(FLD_MEMBER_SEX, memberreg.getMemberSex());
				pstMemberReg.setDate(FLD_MEMBER_BIRTH_DATE, memberreg.getMemberBirthDate());
				pstMemberReg.setInt(FLD_MEMBER_COUNTER, memberreg.getMemberCounter());
				pstMemberReg.setLong(FLD_MEMBER_RELIGION_ID, memberreg.getMemberReligionId());
				pstMemberReg.setInt(FLD_MEMBER_STATUS, memberreg.getMemberStatus());
				pstMemberReg.setDate(FLD_MEMBER_LAST_UPDATE, memberreg.getMemberLastUpdate());
                                pstMemberReg.setInt(FLD_PROCESS_STATUS, memberreg.getProcessStatus());

				pstMemberReg.update(); 
				return memberreg.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstMemberRegTemp(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstMemberRegTemp pstMemberReg = new PstMemberRegTemp(oid);
			pstMemberReg.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstMemberRegTemp(0),DBException.UNKNOWN); 
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
			ContactClass cnt = getContactClass();
                        String where = " WHERE CLS."+PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID]+
                                       " = "+cnt.getOID()+" AND "+PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_PROCESS_STATUS]+
                                       " != "+PstMemberRegTemp.DELETE; 
                        
                        String sql = "SELECT CNT.* FROM " + TBL_CONTACT_LIST+" AS CNT "+
                                     " INNER JOIN "+PstContactClassAssign.TBL_CNT_CLS_ASSIGN+" AS ASG "+
                                     " ON CNT."+PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_CONTACT_ID]+
                                     " = ASG."+PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CONTACT_ID]+
                                     " INNER JOIN "+PstContactClass.TBL_CONTACT_CLASS+" AS CLS "+
                                     " ON ASG."+PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CNT_CLS_ID]+
                                     " = CLS."+PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID]; 
			sql = sql + where ;
                        if(whereClause != null && whereClause.length() > 0)
				//sql = sql + " WHERE " + where + whereClause;
                                sql = sql + " AND "+whereClause;
			if(order != null && order.length() > 0)
				sql = sql + " ORDER BY " + order;
			if(limitStart == 0 && recordToGet == 0)
				sql = sql + "";
			else
				sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;
                        
			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();
			while(rs.next()) {
				MemberReg memberreg = new MemberReg();
				resultToObject(rs, memberreg);
				lists.add(memberreg);
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

	private static void resultToObject(ResultSet rs, MemberReg memberreg){
		try{
			memberreg.setOID(rs.getLong(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_CONTACT_ID]));
			memberreg.setContactCode(rs.getString(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_CONTACT_CODE]));
			memberreg.setRegdate(rs.getDate(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_REGDATE]));
			memberreg.setEmployeeId(rs.getLong(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_EMPLOYEE_ID]));
			memberreg.setCompName(rs.getString(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_COMP_NAME]));
			memberreg.setPersonName(rs.getString(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_PERSON_NAME]));
			memberreg.setPersonLastname(rs.getString(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_PERSON_LASTNAME]));
			memberreg.setBussAddress(rs.getString(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_BUSS_ADDRESS]));
			memberreg.setTown(rs.getString(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_TOWN]));
			memberreg.setProvince(rs.getString(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_PROVINCE]));
			memberreg.setCountry(rs.getString(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_COUNTRY]));
			memberreg.setTelpNr(rs.getString(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_TELP_NR]));
			memberreg.setTelpMobile(rs.getString(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_TELP_MOBILE]));
			memberreg.setFax(rs.getString(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_FAX]));
			memberreg.setHomeAddr(rs.getString(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_HOME_ADDR]));
			memberreg.setHomeTown(rs.getString(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_HOME_TOWN]));
			memberreg.setHomeProvince(rs.getString(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_HOME_PROVINCE]));
			memberreg.setHomeCountry(rs.getString(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_HOME_COUNTRY]));
			memberreg.setHomeTelp(rs.getString(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_HOME_TELP]));
			memberreg.setHomeFax(rs.getString(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_HOME_FAX]));
			memberreg.setNotes(rs.getString(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_NOTES]));
			memberreg.setDirections(rs.getString(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_DIRECTIONS]));
			memberreg.setBankAcc(rs.getString(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_BANK_ACC]));
			memberreg.setBankAcc2(rs.getString(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_BANK_ACC2]));
			memberreg.setContactType(rs.getInt(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_CONTACT_TYPE]));
			memberreg.setEmail(rs.getString(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_EMAIL]));
			memberreg.setParentId(rs.getLong(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_PARENT_ID]));
			memberreg.setMemberGroupId(rs.getLong(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_MEMBER_GROUP_ID]));
			memberreg.setMemberBarcode(rs.getString(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_MEMBER_BARCODE]));
			memberreg.setMemberIdCardNumber(rs.getString(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_MEMBER_ID_CARD_NUMBER]));
			memberreg.setMemberSex(rs.getInt(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_MEMBER_SEX]));
			memberreg.setMemberBirthDate(rs.getDate(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_MEMBER_BIRTH_DATE]));
			memberreg.setMemberCounter(rs.getInt(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_MEMBER_COUNTER]));
			memberreg.setMemberReligionId(rs.getLong(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_MEMBER_RELIGION_ID]));
			memberreg.setMemberStatus(rs.getInt(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_MEMBER_STATUS]));
			memberreg.setMemberLastUpdate(rs.getDate(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_MEMBER_LAST_UPDATE]));
                        memberreg.setProcessStatus(rs.getInt(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_PROCESS_STATUS]));

		}catch(Exception e){ }
	}

	public static boolean checkOID(long contactId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_CONTACT_LIST + " WHERE " + 
						PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_CONTACT_ID] + " = " + contactId;

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
			String sql = "SELECT COUNT(CNT."+ PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_CONTACT_ID] + ") FROM " + TBL_CONTACT_LIST+" AS CNT "+
                                    " INNER JOIN "+PstContactClassAssign.TBL_CNT_CLS_ASSIGN+" AS ASG "+
                                     " ON CNT."+PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_CONTACT_ID]+
                                     " = ASG."+PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CONTACT_ID]+
                                     " INNER JOIN "+PstContactClass.TBL_CONTACT_CLASS+" AS CLS "+
                                     " ON ASG."+PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CNT_CLS_ID]+
                                     " = CLS."+PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID];
			
                        ContactClass cnt = getContactClass();
                        String where = " WHERE CLS."+PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID]+
                                       " = "+cnt.getOID()+" AND "+PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_PROCESS_STATUS]+
                                       " != "+PstMemberRegTemp.DELETE; 
                        sql = sql + where;
                        if(whereClause != null && whereClause.length() > 0)
				//sql = sql + " WHERE " + whereClause;
                                sql = sql + " AND " + whereClause;

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
			  	   MemberReg memberreg = (MemberReg)list.get(ls);
				   if(oid == memberreg.getOID())
					  found=true;
			  }
		  }
		}
		if((start >= size) && (size > 0))
		    start = start - recordToGet;

		return start;
	}
        /* get contact class */
        public static ContactClass getContactClass(){
            String where = PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE]+
                            " = "+PstContactClass.CONTACT_TYPE_MEMBER;
            Vector rslt = PstContactClass.list(0,0,where,"");
            ContactClass cClass = new ContactClass();
            if(rslt!=null&&rslt.size()>0){
                cClass = (ContactClass)rslt.get(0);
            }
            return cClass;            
        }
        
        /* get value max counter member */        
        public static int getMaxCounter(MemberReg memberReg){
            int rslt = 0;
            DBResultSet dbrs = null;
            String strDtNow = Formater.formatDate(memberReg.getRegdate(),"yyyy-MM-dd");            
            try{
                String sql = " SELECT MAX("+PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_MEMBER_COUNTER]+
                             ") FROM "+PstMemberRegTemp.TBL_CONTACT_LIST+
                             " WHERE "+PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_REGDATE]+
                             " = '"+strDtNow+"'";
                dbrs = DBHandler.execQueryResult(sql);
                //System.out.println("sql >>>>>>>>>>>>>>. : "+sql);
                
                ResultSet rs = dbrs.getResultSet();
                while(rs.next()){
                    rslt = rs.getInt(1);
                }
                
                rs.close();             
            }catch(Exception e){
                e.printStackTrace();
                System.out.println("err di get max counter: "+e.toString());
            }finally{
                DBResultSet.close(dbrs);
                return rslt;
            }           
        }
        
        /* setCounter */
        public static int setCounter(MemberReg memberReg){
            int maxCounter = getMaxCounter(memberReg);
            return (maxCounter+1);
        }
        
        /* generate barcode 
         * length = 10
         * first 6 digit = ddMMyy
         * next 1 digit = group type form group member
         * last digit = number of counter
         */
        public static String genBarcode(MemberReg memberReg){
            String barcode = "";
            int maxCounter = getMaxCounter(memberReg);
            //System.out.println("maxCounter>>>> : "+maxCounter);
            String counter = "";
            /* check maxcounter */
            if(maxCounter<10){
                counter = "00"+(maxCounter+1);
            }else if(maxCounter<100){
                counter = "0"+(maxCounter+1);
            }else{
                counter = ""+(maxCounter+1);
            }
            String genDate = Formater.formatDate(memberReg.getRegdate(),"ddMMyy");
            String grpType = "0";
            MemberGroup grpMember = new MemberGroup();
            if(memberReg.getMemberGroupId()!=0){
                try{
                    grpMember = PstMemberGroup.fetchExc(memberReg.getMemberGroupId());
                    grpType = ""+grpMember.getGroupType();
                }catch(Exception e){
                    e.printStackTrace();
                    System.out.println("err di fetch member group : "+e.toString());
                }
            }
            /* combine 3 string */
            barcode = genDate + grpType +  counter;
            return barcode;
        }     
    public static JSONObject fetchJSON(long oid){
      JSONObject object = new JSONObject();
      try {
         MemberReg memberReg = PstMemberRegTemp.fetchExc(oid);
         object.put(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_CONTACT_ID], memberReg.getOID());
         object.put(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_CONTACT_CODE], memberReg.getContactCode());
         object.put(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_REGDATE], memberReg.getRegdate());
         object.put(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_EMPLOYEE_ID], memberReg.getEmployeeId());
         object.put(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_COMP_NAME], memberReg.getCompName());
         object.put(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_PERSON_NAME], memberReg.getPersonName());
         object.put(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_PERSON_LASTNAME], memberReg.getPersonLastname());
         object.put(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_BUSS_ADDRESS], memberReg.getBussAddress());
         object.put(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_TOWN], memberReg.getTown());
         object.put(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_PROVINCE], memberReg.getProvince());
         object.put(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_COUNTRY], memberReg.getCountry());
         object.put(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_TELP_NR], memberReg.getTelpNr());
         object.put(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_TELP_MOBILE], memberReg.getTelpMobile());
         object.put(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_FAX], memberReg.getFax());
         object.put(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_HOME_ADDR], memberReg.getHomeAddr());
         object.put(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_HOME_TOWN], memberReg.getHomeTown());
         object.put(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_HOME_PROVINCE], memberReg.getHomeProvince());
         object.put(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_HOME_COUNTRY], memberReg.getHomeCountry());
         object.put(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_HOME_TELP], memberReg.getHomeTelp());
         object.put(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_HOME_FAX], memberReg.getHomeFax());
         object.put(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_NOTES], memberReg.getNotes());
         object.put(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_DIRECTIONS], memberReg.getDirections());
         object.put(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_BANK_ACC], memberReg.getBankAcc());
         object.put(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_BANK_ACC2], memberReg.getBankAcc2());
         object.put(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_CONTACT_TYPE], memberReg.getContactType());
         object.put(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_EMAIL], memberReg.getEmail());
         object.put(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_PARENT_ID], memberReg.getParentId());
         object.put(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_MEMBER_GROUP_ID], memberReg.getMemberGroupId());
         object.put(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_MEMBER_BARCODE], memberReg.getMemberBarcode());
         object.put(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_MEMBER_ID_CARD_NUMBER], memberReg.getMemberIdCardNumber());
         object.put(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_MEMBER_SEX], memberReg.getMemberSex());
         object.put(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_MEMBER_BIRTH_DATE], memberReg.getMemberBirthDate());
         object.put(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_MEMBER_COUNTER], memberReg.getMemberCounter());
         object.put(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_MEMBER_RELIGION_ID], memberReg.getMemberReligionId());
         object.put(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_MEMBER_STATUS], memberReg.getMemberStatus());
         object.put(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_MEMBER_LAST_UPDATE], memberReg.getMemberLastUpdate());
         object.put(PstMemberRegTemp.fieldNames[PstMemberRegTemp.FLD_PROCESS_STATUS], memberReg.getProcessStatus());
      }catch(Exception exc){}
      return object;
   }       
}

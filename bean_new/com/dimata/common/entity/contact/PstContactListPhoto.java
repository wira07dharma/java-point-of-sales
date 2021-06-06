
/* Created on 	:  [date] [time] AM/PM 
 * 
 * @author  	: karya
 * @version  	: 01
 */

/*******************************************************************
 * Class Description 	: [project description ... ] 
 * Imput Parameters 	: [input parameter ...] 
 * Output 		: [output ...] 
 *******************************************************************/

package com.dimata.common.entity.contact; 

/* package java */ 
import java.sql.*;
import java.util.*;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;
import com.dimata.qdep.entity.*;

import com.dimata.common.entity.contact.*;

public class PstContactListPhoto extends DBHandler implements I_DBInterface,
    I_DBType, I_PersintentExc, I_Language, I_PersistentExcSynch  {
	
    public static final  String TBL_P2_CONTACT_LIST_PHOTO = "contact_list_photo";

	public static final  int FLD_CONTACT_ID = 0;
	public static final  int FLD_CONTACT_PHOTO = 1;

	public static final  String[] fieldNames = {
		"CONTACT_ID",
		"CONTACT_PHOTO"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_BLOB
	 }; 

	public PstContactListPhoto(){
	}

	public PstContactListPhoto(int i) throws DBException { 
		super(new PstContactListPhoto()); 
	}

	public PstContactListPhoto(String sOid) throws DBException { 
		super(new PstContactListPhoto(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstContactListPhoto(long lOid) throws DBException { 
		super(new PstContactListPhoto(0)); 
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
		return TBL_P2_CONTACT_LIST_PHOTO;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstContactListPhoto().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		ContactListPhoto contactlistphoto = fetchExc(ent.getOID()); 
		ent = (Entity)contactlistphoto; 
		return contactlistphoto.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((ContactListPhoto) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((ContactListPhoto) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static ContactListPhoto fetchExc(long oid) throws DBException{ 
		try{ 
			ContactListPhoto contactlistphoto = new ContactListPhoto();
			PstContactListPhoto pstContactListPhoto = new PstContactListPhoto(oid); 
			contactlistphoto.setOID(oid);


			return contactlistphoto; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstContactListPhoto(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(ContactListPhoto contactlistphoto) throws DBException{ 
		try{ 
			PstContactListPhoto pstContactListPhoto = new PstContactListPhoto(0);

			pstContactListPhoto.setString(FLD_CONTACT_PHOTO, contactlistphoto.getContactPhoto());

			pstContactListPhoto.insert(); 
			contactlistphoto.setOID(pstContactListPhoto.getlong(FLD_CONTACT_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstContactListPhoto(0),DBException.UNKNOWN); 
		}
		return contactlistphoto.getOID();
	}

    /***  function for data synchronization ***/
    public long insertExcSynch(Entity ent) throws Exception{
        return insertExcSynch((ContactListPhoto) ent);
    }

    public static long insertExcSynch(ContactListPhoto contactlistphoto) throws DBException{
        long newOID = 0;
        long originalOID = contactlistphoto.getOID();
        try{
            newOID= insertExc(contactlistphoto);
            if(newOID != 0){
                updateSynchOID(newOID, originalOID);
                return originalOID;
            }            
        }catch(DBException dbe){
            throw dbe;
        }catch(Exception e){
            throw new DBException(new PstContactListPhoto(0),DBException.UNKNOWN);
        }
        return 0;
    }
    
    public static long updateSynchOID(long newOID, long originalOID) throws DBException {
        DBResultSet dbrs = null;
        try {
            String sql = "UPDATE " + TBL_P2_CONTACT_LIST_PHOTO + " SET " +
            PstContactListPhoto.fieldNames[PstContactListPhoto.FLD_CONTACT_ID] + " = " + originalOID +
            " WHERE " + PstContactListPhoto.fieldNames[PstContactListPhoto.FLD_CONTACT_ID] +
            " = " + newOID;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            rs.close();

            return originalOID;
        }catch(Exception e) {
            return 0;
        }finally {
            DBResultSet.close(dbrs);
        }
    }

    /*** -------------------------- ***/


	public static long updateExc(ContactListPhoto contactlistphoto) throws DBException{ 
		try{ 
			if(contactlistphoto.getOID() != 0){ 
				PstContactListPhoto pstContactListPhoto = new PstContactListPhoto(contactlistphoto.getOID());

				pstContactListPhoto.setString(FLD_CONTACT_PHOTO,contactlistphoto.getContactPhoto());

				pstContactListPhoto.update(); 
				return contactlistphoto.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstContactListPhoto(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstContactListPhoto pstContactListPhoto = new PstContactListPhoto(oid);
			pstContactListPhoto.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstContactListPhoto(0),DBException.UNKNOWN); 
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
			String sql = "SELECT * FROM " + TBL_P2_CONTACT_LIST_PHOTO; 
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
				ContactListPhoto contactlistphoto = new ContactListPhoto();
				resultToObject(rs, contactlistphoto);
				lists.add(contactlistphoto);
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

	private static void resultToObject(ResultSet rs, ContactListPhoto contactlistphoto){
		try{
			contactlistphoto.setOID(rs.getLong(PstContactListPhoto.fieldNames[PstContactListPhoto.FLD_CONTACT_ID]));
			contactlistphoto.setContactPhoto(""); // picture is blob, so don't get it, let it blank
		}catch(Exception e){ }
	}

	public static boolean checkOID(long contactId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_P2_CONTACT_LIST_PHOTO + " WHERE " + 
						PstContactListPhoto.fieldNames[PstContactListPhoto.FLD_CONTACT_ID] + " = " + contactId;

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
			String sql = "SELECT COUNT("+ PstContactListPhoto.fieldNames[PstContactListPhoto.FLD_CONTACT_ID] + ") FROM " + TBL_P2_CONTACT_LIST_PHOTO;
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
			  	   ContactListPhoto contactlistphoto = (ContactListPhoto)list.get(ls);
				   if(oid == contactlistphoto.getOID())
					  found=true;
			  }
		  }
		}
		if((start >= size) && (size > 0))
		    start = start - recordToGet;

		return start;
	}
}

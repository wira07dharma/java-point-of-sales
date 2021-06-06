/*
 * PstContactClassAssign.java
 *
 * Created on March 30, 2002, 10:00 AM
 */

/**
 *
 * @author  edarmasusila
 * @version 
 */


package com.dimata.common.entity.contact;
//general
import com.dimata.common.entity.custom.PstDataCustom;
import java.sql.*;
import java.util.*;

//qdep
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.db.I_DBInterface;
import com.dimata.posbo.db.I_DBType;
import com.dimata.qdep.entity.*;
import com.dimata.posbo.entity.masterdata.*;

public class PstContactClassAssign extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_PersistentExcSynch   {

    public static final String TBL_CNT_CLS_ASSIGN = "contact_class_assign";
	public static final int FLD_CNT_CLS_ID					= 0;
    public static final int FLD_CONTACT_ID					= 1;

    public static String[] fieldNames = {
		"CONTACT_CLASS_ID", "CONTACT_ID"
    } ;

    public static int[] fieldTypes = {
        	TYPE_PK + TYPE_FK + TYPE_LONG,
            TYPE_PK + TYPE_FK + TYPE_LONG
    };

   /**
     *	Contractor
     */
    public PstContactClassAssign()    
    {
    }
    
    
    
      public PstContactClassAssign(int i) throws DBException {
        super(new PstContactClassAssign());
    }
    
    
      
      public PstContactClassAssign(String sOid) throws DBException 
    {
        super(new PstContactClassAssign(0));
        if(!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    
      
      public PstContactClassAssign(long lOid, long lOid1) throws DBException 
    {
        super(new PstContactClassAssign(0));
        
        if(!locate(lOid, lOid1))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
        
    }
    
    
    /**
     *	Implemanting I_Entity interface methods
     */
    public int getFieldSize() {
        return fieldNames.length;
    }
    
    public String getTableName() {
        return TBL_CNT_CLS_ASSIGN;
    }
    
    public String[] getFieldNames() {
        return fieldNames;
    }
    
    public int[] getFieldTypes() {
        return fieldTypes;
    }
    
    public String getPersistentName() {                
        return new PstContactClassAssign().getClass().getName();
    }
    
    
    /**
     *	Implemanting I_DBInterface interface methods
     */
    public long fetchExc(Entity ent) throws Exception {
        ContactClassAssign cntClsAss = PstContactClassAssign.fetchExc(ent.getOID(0),ent.getOID(1));
        ent = (Entity)cntClsAss;
        return cntClsAss.getContactId();
    }
    

    public long insertExc(Entity ent) throws Exception {
        return PstContactClassAssign.insertExc((ContactClassAssign) ent);
    }
    
    public long updateExc(Entity ent) throws Exception {
        return updateExc((ContactClassAssign) ent);
    }
    
    public long deleteExc(Entity ent) throws Exception {
        return deleteExc((ContactClassAssign) ent);
    }

    public static ContactClassAssign fetchExc(long cntClassOID, long cntOID) throws Exception
    {
        ContactClassAssign cntClsAss = new ContactClassAssign();
        try {
            PstContactClassAssign pstCntClassAss = new PstContactClassAssign(cntOID, cntClassOID);
            cntClsAss.setContactId(cntOID);
            cntClsAss.setContactClassId(cntClassOID);
        }
        catch(DBException e) {
            System.out.println(e);
        }
        return cntClsAss;
    }
    
    
    public static long insertExc(ContactClassAssign cntClsAss) throws Exception
    {
        try{
            PstContactClassAssign pstCntClassAss = new PstContactClassAssign(0);
            
            System.out.println("POID : " + cntClsAss.getContactId());
            System.out.println("POID : " + cntClsAss.getContactClassId());
            
            pstCntClassAss.setLong(FLD_CNT_CLS_ID, cntClsAss.getContactClassId());
            pstCntClassAss.setLong(FLD_CONTACT_ID, cntClsAss.getContactId());
            
            pstCntClassAss.insert();
            //adding insert to data sync sql by Mirahu 20120711
            long oidDataSync = PstDataSyncSql.insertExc(pstCntClassAss.getInsertSQL());
            PstDataSyncStatus.insertExc(oidDataSync);
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstCntClassAss.getInsertSQL());
            return cntClsAss.getContactId();
        }
        catch(DBException e) {
            System.out.println(e);
        }
        return 0;  
    }
    

    public static long deleteExc(long oid, long oid1) throws Exception
    {
       try {
            PstContactClassAssign pstCntClassAss = new PstContactClassAssign(oid, oid1);
            pstCntClassAss.delete();
            //adding insert to data sync sql by Mirahu 20120711
            long oidDataSync = PstDataSyncSql.insertExc(pstCntClassAss.getDeleteSQL());
            PstDataSyncStatus.insertExc(oidDataSync);
			//kebutuhan untuk service transfer katalog
			PstDataCustom.insertDataForSyncAllLocation(pstCntClassAss.getDeleteSQL());
            return oid;
            
       }catch(Exception e) {
            System.out.println(e);            
        }
        return 0;
    }
    

    public static long updateExc(ContactClassAssign cntClsAss) throws Exception
    {
        if(cntClsAss.getContactId() != 0 && cntClsAss.getContactClassId() != 0)
        {
            try {
                PstContactClassAssign pstCnlClass = new PstContactClassAssign(cntClsAss.getContactClassId(), cntClsAss.getContactId());

                pstCnlClass.update();
                //adding insert to data sync sql by Mirahu 20120711
                long oidDataSync = PstDataSyncSql.insertExc(pstCnlClass.getUpdateSQL());
                PstDataSyncStatus.insertExc(oidDataSync);
				//kebutuhan untuk service transfer katalog
				PstDataCustom.insertDataForSyncAllLocation(pstCnlClass.getUpdateSQL());
                return cntClsAss.getContactId();
            }catch(Exception e) {
                System.out.println(e);
            }            
        }
        return 0;
    }
   
    
    public static Vector listAll()
    {
        return list(0, 0, null,null);
    }
    
    
    public static Vector list(int limitStart, int recordToGet, String whereClause, String order)
    {
		Vector lists = new Vector();
		DBResultSet dbrs = null;
		try {
			String sql = "SELECT * FROM " + TBL_CNT_CLS_ASSIGN;
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
				ContactClassAssign contactclassassign = new ContactClassAssign();
				resultToObject(rs, contactclassassign);
				lists.add(contactclassassign);
			}
			rs.close();

    	}catch(Exception e){
			System.out.println();
    	}finally{
        	DBResultSet.close(dbrs);
    	}
        return lists;
    }

	private static void resultToObject(ResultSet rs, ContactClassAssign contactClassAssign){
		try{
			contactClassAssign.setContactClassId(rs.getLong(PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CNT_CLS_ID]));
			contactClassAssign.setContactId(rs.getLong(PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CONTACT_ID]));

		}catch(Exception e){ }
	}

    public static long deleteClassAssign(long oid){        
    	try{
        	String sql = "DELETE FROM "+TBL_CNT_CLS_ASSIGN+
                " WHERE "+fieldNames[FLD_CONTACT_ID]+" = "+oid;

        	int status = DBHandler.execUpdate(sql);   
                
                //adding insert to data sync sql by Mirahu 20120711
                long oidDataSync = PstDataSyncSql.insertExc(sql);
                PstDataSyncStatus.insertExc(oidDataSync);

    	}catch(Exception e){
        	System.out.println("Err : "+e.toString());
            oid = 0;
        }
        return oid;
    }

    public static boolean checkOID(long contactClassId, long contactId)
    {
        DBResultSet dbrs = null;
        boolean result = false;
        try
        {
            String sql = "SELECT * FROM " + TBL_CNT_CLS_ASSIGN + 
            " WHERE " +   PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CNT_CLS_ID] + 
            " = " + contactClassId + 
            " AND " + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CONTACT_ID] + 
            " = " + contactId;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while(rs.next())
            {
                result = true;
            }
            rs.close();
        }
        catch(Exception e)
        {
            System.out.println("err : "+e.toString());
        }
        finally
        {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    public static void DeduplicateContact(String strDel){
        DBResultSet dbrs = null;
    	try{
            String sql = "DELETE FROM " + TBL_CNT_CLS_ASSIGN +
                         " WHERE " + fieldNames[FLD_CONTACT_ID] +
                         " IN (" + strDel + ")";
            dbrs = DBHandler.execQueryResult(sql);                        
    	}catch(Exception e){
            System.out.println("Err : "+e.toString());            
    	}finally{
            DBResultSet.close(dbrs);            
    	}
    }   
    
    /***  function for data synchronization ***/
    public long insertExcSynch(Entity ent) throws Exception{
        return insertExcSynch((ContactClassAssign) ent);
    }

    public static long insertExcSynch(ContactClassAssign cntClsAss) throws DBException{
        try{
            insertExc(cntClsAss);
            return cntClsAss.getOID(0);
        }catch(Exception e){
            throw new DBException(new PstContactClassAssign(0),DBException.UNKNOWN);
        }
    }
    /***************************************/

}

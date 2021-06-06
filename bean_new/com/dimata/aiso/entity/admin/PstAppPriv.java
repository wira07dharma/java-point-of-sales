/*
 * PstAppPriv.java
 *
 * Created on April 3, 2002, 9:29 AM
 */

package com.dimata.aiso.entity.admin;

/**
 *
 * @author  ktanjana
 * @version
 */

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;
import com.dimata.aiso.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.*;

public class PstAppPriv extends DBHandler implements I_DBInterface, I_DBType, I_Persintent  {
    
    public static final String TBL_APP_PRIVILEGE = "aiso_app_privilege";
    public static final int FLD_PRIV_ID			= 0;
    public static final int FLD_PRIV_NAME		= 1;
    public static final int FLD_REG_DATE 		= 2;
    public static final int FLD_DESCRIPTION 		= 3;
    
    
    public static  final String[] fieldNames = {
        "PRIV_ID", "PRIV_NAME", "REG_DATE", "DESCRIPTION"
    } ;
    
    public static int[] fieldTypes = {
        TYPE_PK + TYPE_LONG + TYPE_ID, TYPE_STRING,  TYPE_DATE, TYPE_STRING
    };
    
    /** Creates new PstAppPriv */
    public PstAppPriv() {
    }
    
    public PstAppPriv(int i)
    throws DBException {
        super(new PstAppPriv());
    }
    
    
    public PstAppPriv(String sOid)
    throws DBException {
        super(new PstAppPriv(0));
        if(!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    
    public PstAppPriv(long lOid)
    throws DBException {
        super(new PstAppPriv(0));
        String sOid = "0";
        try {
            sOid = String.valueOf(lOid);
        }catch(Exception e) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        
        if(!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
        
    }
    
    public int getFieldSize() {
        return fieldNames.length;
    }
    
    public String getTableName() {
        return TBL_APP_PRIVILEGE;
    }
    
    public String getPersistentName() {
        return new PstAppPriv().getClass().getName();
    }
    
    
    public int[] getFieldTypes() {
        return fieldTypes;
    }
    
    public String[] getFieldNames() {
        return fieldNames;
    }
    
    
    public long delete(Entity ent) {
        return delete((AppPriv) ent);
    }
    
    public long insert(Entity ent) {
        return PstAppPriv.insert((AppPriv) ent);
    }
    
    
    public long update(Entity ent) {
        return update((AppPriv) ent);
    }
    
    public long fetch(Entity ent) {
        AppPriv entObj = PstAppPriv.fetch(ent.getOID());
        ent = (Entity)entObj;
        return ent.getOID();
    }
    
    
    public static AppPriv fetch(long oid) {
        AppPriv entObj = new AppPriv();
        try {
            PstAppPriv pstObj = new PstAppPriv(oid);
            entObj.setOID(oid);
            entObj.setPrivName(pstObj.getString(FLD_PRIV_NAME));
            entObj.setDescr(pstObj.getString(FLD_DESCRIPTION));
            entObj.setRegDate(pstObj.getDate(FLD_REG_DATE));
        }
        catch(DBException e) {
            System.out.println(e);
        }
        return entObj;
    }
    
    
    public static long insert(AppPriv entObj) {
        try{
            PstAppPriv pstObj = new PstAppPriv(0);
            pstObj.setString(FLD_PRIV_NAME, entObj.getPrivName());
            pstObj.setDate(FLD_REG_DATE, entObj.getRegDate());
            pstObj.setString(FLD_DESCRIPTION, entObj.getDescr());
            
            pstObj.insert();
            entObj.setOID(pstObj.getlong(FLD_PRIV_ID));
            return entObj.getOID();
        }
        catch(DBException e) {
            System.out.println(e);
        }
        return 0;
    }
    
    
    public static long update(AppPriv entObj) {
        if( (entObj!=null) && (entObj.getOID() != 0)) {
            try {
                PstAppPriv pstObj = new PstAppPriv(entObj.getOID());
                pstObj.setString(FLD_PRIV_NAME, entObj.getPrivName());
                pstObj.setDate(FLD_REG_DATE, entObj.getRegDate());
                pstObj.setString(FLD_DESCRIPTION, entObj.getDescr());
                
                pstObj.update();
                return entObj.getOID();
            }catch(Exception e) {
                System.out.println(e);
            }
        }
        return 0;
    }
    
    
    public static long delete(long oid) {
        try {
            PstAppPriv pstObj = new PstAppPriv(oid);
            pstObj.delete();
            return oid;
        }catch(Exception e) {
            System.out.println(e);
        }
        return 0;
    }
    
    public static int getCount(String whereClause){
        DBResultSet dbrs=null;
        try{
            int count = 0;
            String sql = " SELECT COUNT("+fieldNames[FLD_PRIV_ID] +") AS NRCOUNT" + 
                         " FROM " + TBL_APP_PRIVILEGE;


            if(whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;

//            System.out.println(sql);
            dbrs=DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while(rs.next()) {
                count = rs.getInt(1);
            }             
            return count;
        }
        catch (Exception exc){
            System.out.println("getCount "+ exc);
            return 0;
        }
       finally{
            DBResultSet.close(dbrs);
       }
        
    }
    
    public static Vector list(int limitStart , int recordToGet, String whereClause, String order)
    {
        Vector lists = new Vector();        
        DBResultSet dbrs=null;
        try {
            String sql = "SELECT "+fieldNames[FLD_PRIV_ID] +
                         ", " + fieldNames[FLD_PRIV_NAME] +
                         ", " + fieldNames[FLD_REG_DATE] +
                         ", " + fieldNames[FLD_DESCRIPTION] +
                         " FROM " + TBL_APP_PRIVILEGE;


            if(whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;

            if(order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;

            
            switch (DBHandler.DBSVR_TYPE)
            {
                case DBHandler.DBSVR_MYSQL :
                        if(limitStart == 0 && recordToGet == 0)
                                sql = sql + "";
                        else
                                sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;
                        break;

                case DBHandler.DBSVR_POSTGRESQL :
                        if(limitStart == 0 && recordToGet == 0)
                                sql = sql + "";
                        else
                                sql = sql + " LIMIT " +recordToGet + " OFFSET "+ limitStart ;

                        break;

                case DBHandler.DBSVR_SYBASE :
                    	break;

                case DBHandler.DBSVR_ORACLE :
                    	break;

                case DBHandler.DBSVR_MSSQL :
                    	break;

                default:
                        break;
            }                        

//            System.out.println(sql);
            dbrs=DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while(rs.next()) {
                AppPriv appPriv = new AppPriv();
                resultToObject(rs, appPriv);
                lists.add(appPriv);
            }
            return lists;

       }catch(Exception e) {
            System.out.println(e);            
       }
        finally{
            DBResultSet.close(dbrs);
        }        
       
       return new Vector();
    }

    
    private static void resultToObject(ResultSet rs, AppPriv appPriv) {
        try {
            appPriv.setOID(rs.getLong(fieldNames[FLD_PRIV_ID]));
            appPriv.setPrivName(rs.getString(fieldNames[FLD_PRIV_NAME]));
            
            String str = rs.getString(fieldNames[FLD_REG_DATE]);
            Date dt = com.dimata.util.Formater.formatDate(str, "yyyy-MM-dd HH:MM:SS");
            appPriv.setRegDate(dt);                        
            
            appPriv.setDescr(rs.getString(fieldNames[FLD_DESCRIPTION]));
        }catch(Exception e){
            System.out.println("resultToObject() " + e.toString());
        }
    }


    public static int findLimitStart( long oid, int recordToGet, String whereClause, String OrderBy){
		String order = "";
		int size = getCount(whereClause);
		int start = 0;
		boolean found =false;
			 Vector list =  list(0,0, whereClause, OrderBy);//listMaterialDispatch(srcMaterial,i,recordToGet);
			 start = 0;
			 if(list.size()>0){
			  for(int ls=0; ls<list.size(); ls++){
			  	   AppPriv appPriv = (AppPriv)list.get(ls);
				   if(oid == appPriv.getOID()) {
                    	start = ls;
					  found=true;
                      break;
                   }
			  }
		  }

//             System.out.println("-------------");
//             System.out.println("start : "+start);
//             System.out.println("size : "+size);
	//	if((start >= size) && (size > 0))
	//	    start = start - recordToGet;

        if(start<=recordToGet){
            start = 0;//first
        }
        else{
            int div = size / recordToGet;
//            System.out.println(".......div : "+div);
            int mod = size % recordToGet;
//            System.out.println(".......mod : "+mod);
            if(div>0){
	            if(div==1 && mod>0){
	                start = recordToGet;  //next & last
	            }
	            else{
                    int under = 0;
                    int up   =0;
	                for(int i=1; i<=div; i++){
	                     under = recordToGet * i;
                         up = recordToGet * (i+1);
                         if((start>=under) && (start <=up)){
                            start = under;            //next
                         }
	                }
	            }
            }
        }
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
//                    System.out.println("next.......................");
             	}else{
                    start = start - recordToGet;
		             if(start > 0){
                         cmd = Command.PREV;
//                         System.out.println("prev.......................");
		             }
                }
            }
        }

        return cmd;
    }
    
    
    
}

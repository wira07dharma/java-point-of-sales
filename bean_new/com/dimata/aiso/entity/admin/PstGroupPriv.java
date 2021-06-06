/*
 * PstGroupPriv.java
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

public class PstGroupPriv extends DBHandler implements I_DBInterface, I_DBType, I_Persintent  {
    
    public static final String TBL_GROUP_PRIV = "aiso_group_priv";
    public static final int FLD_GROUP_ID		= 0;
    public static final int FLD_PRIV_ID                 = 1;    
    
    public static  final String[] fieldNames = {
        "GROUP_ID", "PRIV_ID"
    } ;
    
    public static int[] fieldTypes = {
        TYPE_PK + TYPE_FK + TYPE_LONG,
        TYPE_PK + TYPE_FK + TYPE_LONG
    };
    
    
    /** Creates new PstGroupPriv */
    public PstGroupPriv() {
    }
        
    public PstGroupPriv(int i) throws DBException {
        super(new PstGroupPriv());
    }
    
    
    public PstGroupPriv(String sOid) throws DBException 
    {
        super(new PstGroupPriv(0));
        if(!locate(sOid))
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        else
            return;
    }
    
    
    public PstGroupPriv(long groupOID, long privOID) throws DBException 
    {
        super(new PstGroupPriv(0));
        
        if(!locate(groupOID, privOID))
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
        return TBL_GROUP_PRIV;
    }
    
    public String[] getFieldNames() {
        return fieldNames;
    }
    
    public int[] getFieldTypes() {
        return fieldTypes;
    }
    
    public String getPersistentName() {                
        return new PstGroupPriv().getClass().getName();
    }
    
    
    /**
     *	Implementing I_DBInterface interface methods
     */
    public long fetch(Entity ent) {        
        GroupPriv entObj = PstGroupPriv.fetch(ent.getOID(0),ent.getOID(1));
        ent = (Entity)entObj;
        return entObj.getOID();         
    }
    

    public long insert(Entity ent) {
        return PstGroupPriv.insert((GroupPriv) ent);
    }
    
    public long update(Entity ent) {
        return update((GroupPriv) ent);
    }
    
    public long delete(Entity ent) {
        return delete((GroupPriv) ent);
    }
        
    
    
    public static GroupPriv fetch(long groupID, long privID)
    {
        GroupPriv entObj = new GroupPriv();
        try {
            PstGroupPriv pstObj = new PstGroupPriv(groupID, privID);
            entObj.setGroupID(groupID);
            entObj.setPrivID(privID);
        }
        catch(DBException e) {
            System.out.println(e);
        }
        return entObj;
    }
    
    
    public static long insert(GroupPriv entObj)
    {
        try{
            PstGroupPriv pstObj = new PstGroupPriv(0);
            
//            System.out.println("GroupID : " + entObj.getGroupID());
//            System.out.println("POID : " + entObj.getPrivID());
            
            pstObj.setLong(FLD_GROUP_ID, entObj.getGroupID());            
            pstObj.setLong(FLD_PRIV_ID, entObj.getPrivID());
            
            pstObj.insert();            
            return entObj.getGroupID();
        }
        catch(DBException e) {
            System.out.println(e);
        }
        return 0;  
    }
    

    public static long deleteByGroup(long oid)
    {
       PstGroupPriv pstObj = new PstGroupPriv();
       DBResultSet dbrs = null;
       try {
            String sql = "DELETE FROM " + pstObj.getTableName() +
                         " WHERE " + PstGroupPriv.fieldNames[PstGroupPriv.FLD_GROUP_ID] +
                         " = " + oid;
                         
            int status = DBHandler.execUpdate(sql);
            return oid;            
       }catch(Exception e) {
            System.out.println(e);            
        }
        finally{
            DBResultSet.close(dbrs);
        }
        
        return 0;
    }

    public static long deleteByPriv(long oid)
    {
       PstGroupPriv pstObj = new PstGroupPriv();
       DBResultSet dbrs= null;
       try {
            String sql = "DELETE FROM " + pstObj.getTableName() +
                         " WHERE " + PstGroupPriv.fieldNames[PstGroupPriv.FLD_PRIV_ID] +
                         " = " + oid;
//            System.out.println(sql);             
            int status = DBHandler.execUpdate(sql);
            return oid;            
       }catch(Exception e) {
            System.out.println(" PstGroupPriv.deleteByPriv "+e);            
        }
        finally{
            DBResultSet.close(dbrs);
        }
        
        return 0;
    }
    

    public static long update(GroupPriv entObj)
    {
        if(entObj != null && entObj.getGroupID() != 0 )
        {
            try {
                PstGroupPriv pstObj = new PstGroupPriv(entObj.getGroupID(), entObj.getPrivID());
                
                pstObj.update();
                return entObj.getGroupID();
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
       return new Vector();
    }


/*    public static int findLimitStart( long oid, int recordToGet, String whereClause, String OrderBy){
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

             System.out.println("-------------");
             System.out.println("start : "+start);
             System.out.println("size : "+size);
	//	if((start >= size) && (size > 0))
	//	    start = start - recordToGet;

        if(start<=recordToGet){
            start = 0;//first
        }
        else{
            int div = size / recordToGet;
            System.out.println(".......div : "+div);
            int mod = size % recordToGet;
            System.out.println(".......mod : "+mod);
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
        */
    
}

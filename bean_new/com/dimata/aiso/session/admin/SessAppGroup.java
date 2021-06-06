/*
 * SessAppGroup.java
 *
 * Created on April 6, 2002, 7:04 AM
 */

package com.dimata.aiso.session.admin;

import java.util.*;
import java.io.*;
import java.sql.*;                      

import com.dimata.util.*;
import com.dimata.aiso.db.*;

import com.dimata.aiso.form.admin.*;
import com.dimata.aiso.entity.admin.*;

/**
 *
 * @author  ktanjana
 * @version 
 */
public class SessAppGroup {

    /** Creates new SessAppGroup */
    public SessAppGroup() {
    }

     //-------------------Relation AppGroup and AppPriv--------------//

    public static Vector getGroupPriv(long groupID)
    {
        PstGroupPriv  pstGroupPriv = new PstGroupPriv();
    	PstAppPriv  pstAppPriv = new PstAppPriv();
        Vector lists = new Vector();        
        DBResultSet dbrs=null;
        try {        
            
            String sql = "SELECT AGP."+PstGroupPriv.fieldNames[PstGroupPriv.FLD_GROUP_ID]+
                		 ", AGP."+PstGroupPriv.fieldNames[PstGroupPriv.FLD_PRIV_ID]+
                         ", AP."+PstAppPriv.fieldNames[PstAppPriv.FLD_PRIV_NAME]+
                         ", AP."+PstAppPriv.fieldNames[PstAppPriv.FLD_DESCRIPTION]+
                		 " FROM "+pstGroupPriv.getTableName()+ " AS AGP ," +
                         pstAppPriv.getTableName() + " AS AP "+
                         "WHERE AGP."+PstGroupPriv.fieldNames[PstGroupPriv.FLD_GROUP_ID]+" = '"+ 
                          groupID +"'"+
                         " AND AGP."+PstGroupPriv.fieldNames[PstGroupPriv.FLD_PRIV_ID]+" = "+
                         "AP."+PstAppPriv.fieldNames[PstAppPriv.FLD_PRIV_ID];

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
            appPriv.setOID(rs.getLong(PstAppPriv.fieldNames[PstAppPriv.FLD_PRIV_ID]));
            appPriv.setPrivName(rs.getString(PstAppPriv.fieldNames[PstAppPriv.FLD_PRIV_NAME]));
            appPriv.setDescr(rs.getString(PstAppPriv.fieldNames[PstAppPriv.FLD_DESCRIPTION]));
            
        }catch(Exception e){
            System.out.println("resultToObject() " + e.toString());
        }
    }    
    
    
    // PATERN ## INSERT WITH Vector of  GroupPriv
    /**
     * return false if error
     **/
    public static boolean setGroupPriv(long groupOID, Vector vector) {
        
        // do delete
        if( PstGroupPriv.deleteByGroup(groupOID)==0)
            return false;

        if(vector == null || vector.size() == 0) 
            return true;
        
        // than insert
        for(int i = 0; i < vector.size(); i++) {
            GroupPriv gp = (GroupPriv)vector.get(i);
            if(PstGroupPriv.insert(gp) ==0)
                return false;
        }         
        return true;
    }
        
}

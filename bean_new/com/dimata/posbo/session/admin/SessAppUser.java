/*
 * SessAppUser.java
 *
 * Created on April 6, 2002, 7:04 AM
 */

package com.dimata.posbo.session.admin;

import com.dimata.common.entity.custom.DataCustom;
import com.dimata.common.entity.custom.PstDataCustom;
import com.dimata.posbo.entity.admin.*;
import com.dimata.posbo.db.*;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.pos.entity.balance.PstCashCashier;

import java.util.*;
import java.sql.*;

/**
 *
 * @author  ktanjana
 * @version
 */
public class SessAppUser {

    /** Creates new SessAppUser */
    public SessAppUser() {
    }

    //-------------------Relation AppUser and AppGroup--------------//

    public static Vector getUserGroup(long userID) {
        //System.out.println("---> in : getUserGroup(long userID)");
        PstUserGroup pstUserGroup = new PstUserGroup();
        PstAppGroup pstAppGroup = new PstAppGroup();
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {

            String sql = "SELECT AUG." + PstUserGroup.fieldNames[PstUserGroup.FLD_USER_ID] +
                    ", AUG." + PstUserGroup.fieldNames[PstUserGroup.FLD_GROUP_ID] +
                    ", AG." + PstAppGroup.fieldNames[PstAppGroup.FLD_GROUP_NAME] +
                    ", AG." + PstAppGroup.fieldNames[PstAppGroup.FLD_DESCRIPTION] +
                    " FROM " + pstUserGroup.getTableName() + " AS AUG ," +
                    pstAppGroup.getTableName() + " AS AG " +
                    "WHERE AUG." + PstUserGroup.fieldNames[PstUserGroup.FLD_USER_ID] + " = '" +
                    userID + "'" +
                    " AND AUG." + PstUserGroup.fieldNames[PstUserGroup.FLD_GROUP_ID] + " = " +
                    "AG." + PstAppGroup.fieldNames[PstAppGroup.FLD_GROUP_ID];

            System.out.println(sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                AppGroup appGroup = new AppGroup();
                resultToObject(rs, appGroup);
                lists.add(appGroup);
            }
            return lists;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }

        return new Vector();
    }


    private static void resultToObject(ResultSet rs, AppGroup appGroup) {
        try {
            appGroup.setOID(rs.getLong(PstAppGroup.fieldNames[PstAppGroup.FLD_GROUP_ID]));
            appGroup.setGroupName(rs.getString(PstAppGroup.fieldNames[PstAppGroup.FLD_GROUP_NAME]));
            appGroup.setDescription(rs.getString(PstAppGroup.fieldNames[PstAppGroup.FLD_DESCRIPTION]));

        } catch (Exception e) {
            System.out.println("resultToObject() " + e.toString());
        }
    }


    // PATERN ## INSERT WITH Vector of  UserGroup
    /**
     * return false if error
     **/
    public static boolean setUserGroup(long userOID, Vector vector) {
      
        if(vector.size() > 0){
        // do delete
        if (PstUserGroup.deleteByUser(userOID) == 0)
            return false;
        }
        if (vector == null || vector.size() == 0)
            return true;

        // than insert
        for (int i = 0; i < vector.size(); i++) {
            UserGroup ug = (UserGroup) vector.get(i);
            if (PstUserGroup.insert(ug) == 0)
                return false;
        }
        return true;
    }

       /**
     *
     * @param userOID
     * @param vector update opie-eyek 20130819
     * @return
     */
    public static boolean setUserAssignLocation(long userOID, Vector vector, int type) {

        // do delete
        String where ="";
        if(type==0){
            where = ""+PstDataCustom.fieldNames[PstDataCustom.FLD_OWNER_ID] + "=" + userOID+" AND "+PstDataCustom.fieldNames[PstDataCustom.FLD_DATA_NAME] +"='user_location_map'";
        }else if(type==1){
             where = ""+PstDataCustom.fieldNames[PstDataCustom.FLD_OWNER_ID] + "=" + userOID+" AND "+PstDataCustom.fieldNames[PstDataCustom.FLD_DATA_NAME] +"='user_location_transfer'";
        }else if(type==2){
            where = ""+PstDataCustom.fieldNames[PstDataCustom.FLD_OWNER_ID] + "=" + userOID+" AND "+PstDataCustom.fieldNames[PstDataCustom.FLD_DATA_NAME] +"='user_view_sale_stock_report_location'";
        }else if(type==3){
             where = ""+PstDataCustom.fieldNames[PstDataCustom.FLD_OWNER_ID] + "=" + userOID+" AND "+PstDataCustom.fieldNames[PstDataCustom.FLD_DATA_NAME] +"='user_create_document_location'";
        }else if(type==4){
             where = ""+PstDataCustom.fieldNames[PstDataCustom.FLD_OWNER_ID] + "=" + userOID+" AND "+PstDataCustom.fieldNames[PstDataCustom.FLD_DATA_NAME] +"='user_credit_view'";
        }else if(type==5){
            where = ""+PstDataCustom.fieldNames[PstDataCustom.FLD_OWNER_ID] + "=" + userOID+" AND "+PstDataCustom.fieldNames[PstDataCustom.FLD_DATA_NAME] +"='day_assign'";
        }else if(type==6){
            where = ""+PstDataCustom.fieldNames[PstDataCustom.FLD_OWNER_ID] + "=" + userOID+" AND "+PstDataCustom.fieldNames[PstDataCustom.FLD_DATA_NAME] +"='sales_assign'";
        }else if(type==7){
            where = ""+PstDataCustom.fieldNames[PstDataCustom.FLD_OWNER_ID] + "=" + userOID+" AND "+PstDataCustom.fieldNames[PstDataCustom.FLD_DATA_NAME] +"='delivery_assign'";
        }else if(type==8){
            where = ""+PstDataCustom.fieldNames[PstDataCustom.FLD_OWNER_ID] + "=" + userOID+" AND "+PstDataCustom.fieldNames[PstDataCustom.FLD_DATA_NAME] +"='user_assign_single_location'";
        }
       // int angka = PstDataCustom.deleteCustomDataExc(where);
        if(PstDataCustom.deleteCustomDataExc(where)!=0)
            return false;
        if (vector == null || vector.size() == 0)
            return true;

        // than insert
        for (int i = 0; i < vector.size(); i++) {
            DataCustom dtCustom = new DataCustom();
            dtCustom.setOwnerId(userOID);
            dtCustom.setDataValue((String)vector.get(i));
            if(type==0){
                dtCustom.setDataName("user_location_map");
            }else if(type==1){
                dtCustom.setDataName("user_location_transfer");
            }else if(type==2){
                dtCustom.setDataName("user_view_sale_stock_report_location");
           }else if(type==3){
               dtCustom.setDataName("user_create_document_location");
           }else if(type==4){
               dtCustom.setDataName("user_credit_view");
           }else if(type==5){
                dtCustom.setDataName("day_assign");
            }else if(type==6){
                dtCustom.setDataName("sales_assign");
            }else if(type==7){
                dtCustom.setDataName("delivery_assign");
            }else if(type==8){
                dtCustom.setDataName("user_assign_single_location");
            }
            try {
                if (PstDataCustom.insertExc(dtCustom) == 0)
                    return false;
            } catch (Exception e) {

            }
        }
        return true;
    }

    /**
     *
     * @param discountTypeId
     * @return
     */
    public static boolean readyDataToDelete(long userId) {
        boolean status = true;
        try {
            // pengecekan di bill main
            String where = PstBillMain.fieldNames[PstBillMain.FLD_APPUSER_ID] + "=" + userId;
            Vector vlist = PstBillMain.list(0, 0, where, "");
            if (vlist != null && vlist.size() > 0) {
                status = false;
            } else {
                // pengecekan di cash cashier
                where = PstCashCashier.fieldNames[PstCashCashier.FLD_APPUSER_ID] + "=" + userId;
                vlist = PstCashCashier.list(0, 0, where, "");
                if (vlist != null && vlist.size() > 0) {
                    status = false;
                }
            }
        } catch (Exception e) {
            System.out.println("SessAppUser - readyDataToDelete : " + e.toString());
        }
        return status;
    }
}

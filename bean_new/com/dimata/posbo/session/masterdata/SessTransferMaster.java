package com.dimata.posbo.session.masterdata;

import java.util.*;
import java.util.Date;
import java.sql.*;
import java.text.SimpleDateFormat;

import com.dimata.posbo.db.*;
import com.dimata.common.entity.contact.*;
import com.dimata.common.entity.periode.PstPeriode;
import com.dimata.posbo.entity.admin.*;
import com.dimata.posbo.entity.masterdata.*;

// pos package
import com.dimata.pos.entity.billing.*;

public class SessTransferMaster {

    public static final String TBL_TRANSFER_APP_USER = "TRANSFER_APP_USER";
    public static final String TBL_TRANSFER_CATEGORY = "TRANSFER_CATEGORY";
    public static final String TBL_TRANSFER_CONTACT_CLASS = "TRANSFER_CONTACT_CLASS";
    public static final String TBL_TRANSFER_CONTACT_CLASS_ASSIGN = "TRANSFER_CONTACT_CLASS_ASSIGN";
    public static final String TBL_TRANSFER_CONTACT_LIST = "TRANSFER_CONTACT_LIST";
    public static final String TBL_TRANSFER_CURRENCY = "TRANSFER_CURRENCY";
    public static final String TBL_TRANSFER_MATERIAL = "TRANSFER_MATERIAL";
    public static final String TBL_TRANSFER_MATERIAL_COMPOSIT = "TRANSFER_MATERIAL_COMPOSIT";
    public static final String TBL_TRANSFER_MINMAXSTOCK = "TRANSFER_MINMAXSTOCK";
    public static final String TBL_TRANSFER_PERIODE = "TRANSFER_PERIODE";
    public static final String TBL_TRANSFER_SALES_PERSON = "TRANSFER_SALES_PERSON";
    public static final String TBL_TRANSFER_SHIFT = "TRANSFER_SHIFT";
    public static final String TBL_TRANSFER_SUB_CATEGORY = "TRANSFER_SUB_CATEGORY";
    public static final String TBL_TRANSFER_UNIT = "TRANSFER_UNIT";

    //Transfer Data Master Masuk
    public static boolean TransferMasterIn() {
        boolean result = false;
        Connection koneksi = null;
        try {
            koneksi = openTransferIn();
        } catch (Exception exc) {
            System.out.println("Error : " + exc);
        }

        result = ProcessAppUserIn(koneksi);
        if (result == true) {
            result = ProcessUnitIn(koneksi);
        } else {
            System.out.println("Transfer AppUser In fail!!!");
        }

        if (result == true) {
            result = ProcessShiftIn(koneksi);
        } else {
            System.out.println("Transfer Unit In fail!!!");
        }


        if (result == true) {
            result = ProcessPeriodeIn(koneksi);
        } else {
            System.out.println("Transfer Shift In fail!!!");
        }

        if (result == true) {
            result = ProcessCurrencyIn(koneksi);
        } else {
            System.out.println("Transfer Periode In fail!!!");
        }
        if (result == true) {
            result = ProcessCategoryIn(koneksi);
        } else {
            System.out.println("Transfer Currency In fail!!!");
        }
        if (result == true) {
            result = ProcessSubCategoryIn(koneksi);
        } else {
            System.out.println("Transfer Category In fail!!!");
        }

        if (result == true) {
            result = ProcessSalesPersonIn(koneksi);
        } else {
            System.out.println("Transfer Sub Category In fail!!!");
        }

        if (result == true) {
            result = ProcessMaterialIn(koneksi);
        } else {
            System.out.println("Transfer SalesPerson In fail!!!");
        }

        if (result == true) {
            result = ProcessMaterialCompositIn(koneksi);
        } else {
            System.out.println("Transfer Material In fail!!!");
        }


        if (result == true) {
            result = ProcessMinMaxStockIn(koneksi);
        } else {
            System.out.println("Transfer MaterialComposit In fail!!!");
        }


        result = true;
        if (result == true) {
            result = ProcessContactClassIn(koneksi);
        } else {
            System.out.println("Transfer MinMaxStock In fail!!!");
        }

        if (result == true) {
            result = ProcessContactListIn(koneksi);
        } else {
            System.out.println("Transfer ContactClass In fail!!!");
        }

        if (result == true) {
            result = ProcessContactClassAssignIn(koneksi);
        } else {
            System.out.println("Transfer ContactList In fail!!!");
        }

        if (result == false)
            System.out.println("Transfer ContactClassAssign In fail!!!");

        closeTransferIn(koneksi);
        return result;
    }

    /**
     * Transfer Data Master Keluar
     * Dilakukan di Back Office Warehouse
     */
    public static boolean TransferMasterOut() {
        boolean result = false;
        Connection koneksi = null;
        try {
            koneksi = openTransferOut();
        } catch (Exception exc) {
            System.out.println("Error : " + exc);
        }

        // transfer masterdata AppUser
        result = ProcessAppUserOut(koneksi);

        if (result == true) {
            // transfer masterdata Unit
            result = ProcessUnitOut(koneksi);
        } else {
            System.out.println("Transfer AppUser Out fail!!!");
        }


        if (result == true) {
            // transfer masterdata Shift
            result = ProcessShiftOut(koneksi);
        } else {
            System.out.println("Transfer Unit Out fail!!!");
        }

        if (result == true) {
            // transfer masterdata Periode
            result = ProcessPeriodeOut(koneksi);
        } else {
            System.out.println("Transfer Shift Out fail!!!");
        }

        if (result == true) {
            // transfer masterdata Currency
            result = ProcessCurrencyOut(koneksi);
        } else {
            System.out.println("Transfer Periode Out fail!!!");
        }

        if (result == true) {
            // transfer masterdata Category
            result = ProcessCategoryOut(koneksi);
        } else {
            System.out.println("Transfer Currency Out fail!!!");
        }

        if (result == true) {
            // transfer masterdata Sub Category
            result = ProcessSubCategoryOut(koneksi);
        } else {
            System.out.println("Transfer Category Out fail!!!");
        }

        if (result == true) {
            // transfer masterdata SalesPerson
            result = ProcessSalesPersonOut(koneksi);
        } else {
            System.out.println("Transfer Sub Category Out fail!!!");
        }

        if (result == true) {
            // transfer masterdata Material
            result = ProcessMaterialOut(koneksi);
        } else {
            System.out.println("Transfer SalesPerson Out fail!!!");
        }


        if (result == true) {
            // transfer masterdata Material Composit
            result = ProcessMaterialCompositOut(koneksi);
        } else {
            System.out.println("Transfer Material Out fail!!!");
        }

        if (result == true) {
            // transfer masterdata Maxmin Stock
            result = ProcessMinMaxStockOut(koneksi);
        } else {
            System.out.println("Transfer MaterialComposit Out fail!!!");
        }

        if (result == true) {
            // transfer masterdata Contact Class
            result = ProcessContactClassOut(koneksi);
        } else {
            System.out.println("Transfer MinMaxStock Out fail!!!");
        }


        if (result == true) {
            // transfer masterdata Contact List
            result = ProcessContactListOut(koneksi);
        } else {
            System.out.println("Transfer ContactClass Out fail!!!");
        }

        if (result == true) {
            // transfer masterdata Contact Class Assign
            result = ProcessContactClassAssignOut(koneksi);
        } else {
            System.out.println("Transfer ContactList Out fail!!!");
        }

        if (result == true) {
            System.out.println("Transfer Masterdata success !!!");
        } else {
            System.out.println("Transfer ContactClassAssign Out fail!!!");
        }

        closeTransferOut(koneksi);
        return result;
    }


    /**
     * Transfer AppUser Keluar
     */
    private static boolean ProcessAppUserOut(Connection koneksi) {
        boolean hasil = false;
        DBResultSet dbrs = null;
        try {
            // Delete contents of AppUser Out
            Statement stmt = koneksi.createStatement();

            // Delete AppUser Out
            String sql = "DELETE FROM " + TBL_TRANSFER_APP_USER;
            int res = stmt.executeUpdate(sql);

            // Fetch AppUser
            sql = "SELECT " + PstAppUser.fieldNames[PstAppUser.FLD_USER_ID] +
                    ", " + PstAppUser.fieldNames[PstAppUser.FLD_USER_GROUP_NEW] +
                    ", " + PstAppUser.fieldNames[PstAppUser.FLD_LOGIN_ID] +
                    ", " + PstAppUser.fieldNames[PstAppUser.FLD_PASSWORD] +
                    ", " + PstAppUser.fieldNames[PstAppUser.FLD_FULL_NAME] +
                    ", " + PstAppUser.fieldNames[PstAppUser.FLD_EMAIL] +
                    ", " + PstAppUser.fieldNames[PstAppUser.FLD_DESCRIPTION] +
                    ", " + PstAppUser.fieldNames[PstAppUser.FLD_REG_DATE] +
                    ", " + PstAppUser.fieldNames[PstAppUser.FLD_UPDATE_DATE] +
                    ", " + PstAppUser.fieldNames[PstAppUser.FLD_USER_STATUS] +
                    ", " + PstAppUser.fieldNames[PstAppUser.FLD_LAST_LOGIN_DATE] +
                    ", " + PstAppUser.fieldNames[PstAppUser.FLD_LAST_LOGIN_IP] +
                    ", " + PstAppUser.fieldNames[PstAppUser.FLD_EMPLOYEE_ID] +
                    " FROM " + PstAppUser.TBL_APP_USER +
                    " ORDER BY " + PstAppUser.fieldNames[PstAppUser.FLD_USER_ID];

            //System.out.println("SQL AppUser : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            long oidREC = 0;
            boolean isInsert = true;

            long userId = 0;
            int userGroup = 0;
            String loginId = "";
            String passWd = "";
            String fullName = "";
            String email = "";
            String description = "";
            String strRegDate = "";
            Date updateDate = new Date();
            int userStatus = 0;
            Date lastLoginDate = new Date();
            String lastLoginIp = "";
            long employeeId = 0;

            while (rs.next()) {
                String sql_x = "";
                SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                // Insert item
                userId = rs.getLong(1);
                userGroup = rs.getInt(2);
                loginId = rs.getString(3) != null ? rs.getString(3) : "";
                passWd = rs.getString(4) != null ? rs.getString(4) : "";
                fullName = rs.getString(5) != null ? rs.getString(5) : "";
                email = rs.getString(6) != null ? rs.getString(6) : "";
                description = rs.getString(7) != null ? rs.getString(7) : "";
                strRegDate = rs.getString(8) != null ? rs.getString(8) : "";
                updateDate = rs.getDate(9) != null ? rs.getDate(9) : new Date();
                userStatus = rs.getInt(10);
                lastLoginDate = rs.getDate(11) != null ? rs.getDate(11) : new Date();
                lastLoginIp = rs.getString(12) != null ? rs.getString(12) : "";
                employeeId = rs.getLong(13);

                sql_x = "INSERT INTO  " + TBL_TRANSFER_APP_USER +
                        "(USER_ID" +
                        ", USER_GROUP" +
                        ", LOGIN_ID" +
                        ", PASSWORD" +
                        ", FULL_NAME" +
                        ", EMAIL" +
                        ", DESCRIPTION" +
                        ", REG_DATE" +
                        ", UPDATE_DATE" +
                        ", USER_STATUS" +
                        ", LAST_LOGIN_DATE" +
                        ", LAST_LOGIN_IP" +
                        ", EMPLOYEE_ID)" +
                        " VALUES (" + userId +
                        "," + userGroup +
                        ",'" + loginId + "'" +
                        ",'" + passWd + "'" +
                        ",'" + fullName + "'" +
                        ",'" + email + "'" +
                        ",'" + description + "'" +
                        ",'" + strRegDate + "'" +
                        ",'" + simpledateformat.format(updateDate) + "'" +
                        "," + userStatus +
                        ",'" + simpledateformat.format(lastLoginDate) + "'" +
                        ",'" + lastLoginIp + "'" +
                        "," + employeeId +
                        ")";
                //System.out.println("SQL INSERT APPUSER : "+sql_x);
                res = stmt.executeUpdate(sql_x);
            }
            hasil = true;
        } catch (Exception exc) {
            System.out.println("Error transfer app user out : " + exc);
        }
        return hasil;
    }

    //Transfer Unit Keluar
    private static boolean ProcessUnitOut(Connection koneksi) {
        boolean hasil = false;
        DBResultSet dbrs = null;
        try {
            //Delete contents of Unit Out
            Statement stmt = koneksi.createStatement();

            //Delete Unit Out
            String sql = "DELETE FROM " + TBL_TRANSFER_UNIT;
            int res = stmt.executeUpdate(sql);

            //Fetch unit
            sql = " SELECT " + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                    ", " + PstUnit.fieldNames[PstUnit.FLD_CODE] +
                    ", " + PstUnit.fieldNames[PstUnit.FLD_NAME] +
                    ", " + PstUnit.fieldNames[PstUnit.FLD_QTY_PER_BASE_UNIT] +
                    " FROM " + PstUnit.TBL_P2_UNIT +
                    " ORDER BY " + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID];

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            long oidREC = 0;
            boolean isInsert = true;
            while (rs.next()) {
                String sql_x = "";
                //SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //Insert item
                sql_x = "INSERT INTO " + TBL_TRANSFER_UNIT +
                        " (UNIT_ID" +
                        ", CODE" +
                        ", NAME" +
                        ", QTY_PER_BASE_UNIT" +
                        ") VALUES " +
                        "(" + rs.getLong(1) +
                        ",'" + (rs.getString(2) != null ? rs.getString(2) : "") +
                        "','" + (rs.getString(3) != null ? rs.getString(3) : "") +
                        "'," + rs.getDouble(4) +
                        ")";
                //System.out.println("SQL INSERT UNIT : "+sql_x);
                res = stmt.executeUpdate(sql_x);
            }
            hasil = true;
        } catch (Exception exc) {
            System.out.println("Error transfer unit out : " + exc);
        }
        return hasil;
    }

    //Transfer Shift Keluar
    private static boolean ProcessShiftOut(Connection koneksi) {
        boolean hasil = false;
        DBResultSet dbrs = null;
        try {
            //Delete contents of Shift Out
            Statement stmt = koneksi.createStatement();

            //Delete Shift Out
            String sql = "DELETE FROM " + TBL_TRANSFER_SHIFT;
            int res = stmt.executeUpdate(sql);

            //Fetch Shift
            sql = " SELECT " + PstShift.fieldNames[PstShift.FLD_SHIFT_ID] +
                    ", " + PstShift.fieldNames[PstShift.FLD_NAME] +
                    ", " + PstShift.fieldNames[PstShift.FLD_REMARK] +
                    " FROM " + PstShift.TBL_SHIFT +
                    " ORDER BY " + PstShift.fieldNames[PstShift.FLD_SHIFT_ID];

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            long oidREC = 0;
            boolean isInsert = true;
            while (rs.next()) {
                String sql_x = "";
                //SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //Insert item
                sql_x = "INSERT INTO " + TBL_TRANSFER_SHIFT +
                        " (SHIFT_ID" +
                        ", NAME" +
                        ", REMARK" +
                        ") VALUES " +
                        "(" + rs.getLong(1) +
                        ",'" + (rs.getString(2) != null ? rs.getString(2) : "") +
                        "','" + (rs.getString(3) != null ? rs.getString(3) : "") +
                        "')";
                //System.out.println("SQL SHIFT : "+sql_x);
                res = stmt.executeUpdate(sql_x);
            }
            hasil = true;
        } catch (Exception exc) {
            System.out.println("Error transfer shift out : " + exc);
        }
        return hasil;
    }

    //Transfer Periode Keluar
    private static boolean ProcessPeriodeOut(Connection koneksi) {
        boolean hasil = false;
        DBResultSet dbrs = null;
        try {
            //Delete contents of Periode Out
            Statement stmt = koneksi.createStatement();

            //Delete Periode Out
            String sql = "DELETE FROM " + TBL_TRANSFER_PERIODE;
            int res = stmt.executeUpdate(sql);

            //Fetch Periode
            sql = " SELECT " + PstPeriode.fieldNames[PstPeriode.FLD_STOCK_PERIODE_ID] +
                    ", " + PstPeriode.fieldNames[PstPeriode.FLD_PERIODE_NAME] +
                    ", " + PstPeriode.fieldNames[PstPeriode.FLD_PERIODE_TYPE] +
                    ", " + PstPeriode.fieldNames[PstPeriode.FLD_STATUS] +
                    ", " + PstPeriode.fieldNames[PstPeriode.FLD_START_DATE] +
                    ", " + PstPeriode.fieldNames[PstPeriode.FLD_END_DATE] +
                    " FROM " + PstPeriode.TBL_STOCK_PERIODE +
                    " ORDER BY " + PstPeriode.fieldNames[PstPeriode.FLD_STOCK_PERIODE_ID];

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            long oidREC = 0;
            boolean isInsert = true;
            while (rs.next()) {
                String sql_x = "";
                SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //Insert item
                sql_x = "INSERT INTO " + TBL_TRANSFER_PERIODE +
                        " (PERIODE_ID" +
                        ", NAME" +
                        ", PERIODE_TYPE" +
                        ", STATUS" +
                        ", START_DATE" +
                        ", END_DATE" +
                        ") VALUES " +
                        "(" + rs.getLong(1) +
                        ",'" + (rs.getString(2) != null ? rs.getString(2) : "") +
                        "'," + rs.getInt(3) +
                        "," + rs.getInt(4) +
                        ",'" + simpledateformat.format((rs.getDate(5) != null ? rs.getDate(5) : new Date())) +
                        "','" + simpledateformat.format((rs.getDate(6) != null ? rs.getDate(6) : new Date())) +
                        "')";
                //System.out.println("SQL PERIODE OUT : "+sql_x);
                res = stmt.executeUpdate(sql_x);
            }
            hasil = true;
        } catch (Exception exc) {
            System.out.println("Error transfer periode out : " + exc);
        }
        return hasil;
    }

    //Transfer Currency Keluar
    private static boolean ProcessCurrencyOut(Connection koneksi) {
        boolean hasil = false;
        DBResultSet dbrs = null;
        try {
            //Delete contents of Currency Out
            Statement stmt = koneksi.createStatement();

            //Delete Currency Out
            String sql = "DELETE FROM " + TBL_TRANSFER_CURRENCY;
            int res = stmt.executeUpdate(sql);

            //Fetch Currency
            sql = " SELECT " + PstMatCurrency.fieldNames[PstMatCurrency.FLD_CURRENCY_ID] +
                    ", " + PstMatCurrency.fieldNames[PstMatCurrency.FLD_CODE] +
                    ", " + PstMatCurrency.fieldNames[PstMatCurrency.FLD_NAME] +
                    ", " + PstMatCurrency.fieldNames[PstMatCurrency.FLD_SELLING_RATE] +
                    ", " + PstMatCurrency.fieldNames[PstMatCurrency.FLD_BUYING_RATE] +
                    " FROM " + PstMatCurrency.TBL_CURRENCY +
                    " ORDER BY " + PstMatCurrency.fieldNames[PstMatCurrency.FLD_CURRENCY_ID];

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            long oidREC = 0;
            boolean isInsert = true;
            while (rs.next()) {
                String sql_x = "";
                //SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //Insert item
                sql_x = "INSERT INTO " + TBL_TRANSFER_CURRENCY +
                        " (CURRENCY_ID" +
                        ", CODE" +
                        ", NAME" +
                        ", SELLING_RATE" +
                        ", BUYING_RATE" +
                        ") VALUES " +
                        "(" + rs.getLong(1) +
                        ",'" + (rs.getString(2) != null ? rs.getString(2) : "") +
                        "','" + (rs.getString(3) != null ? rs.getString(3) : "") +
                        "'," + rs.getDouble(4) +
                        "," + rs.getDouble(5) +
                        ")";
                System.out.println("CURRENCY SQL : " + sql);
                res = stmt.executeUpdate(sql_x);
            }
            hasil = true;
        } catch (Exception exc) {
            System.out.println("Error transfer currency out : " + exc);
        }
        return hasil;
    }

    //Transfer Category Keluar
    private static boolean ProcessCategoryOut(Connection koneksi) {
        boolean hasil = false;
        DBResultSet dbrs = null;
        try {
            //Delete contents of Category Out
            Statement stmt = koneksi.createStatement();

            //Delete Category Out
            String sql = "DELETE FROM " + TBL_TRANSFER_CATEGORY;
            int res = stmt.executeUpdate(sql);

            //Fetch Category
            sql = " SELECT " + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
                    ", " + PstCategory.fieldNames[PstCategory.FLD_NAME] +
                    ", " + PstCategory.fieldNames[PstCategory.FLD_CODE] +
                    " FROM " + PstCategory.TBL_CATEGORY +
                    " ORDER BY " + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID];

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            long oidREC = 0;
            boolean isInsert = true;
            while (rs.next()) {
                String sql_x = "";
                //SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //Insert item
                sql_x = "INSERT INTO " + TBL_TRANSFER_CATEGORY +
                        " (CATEGORY_ID" +
                        ", NAME" +
                        ", CODE" +
                        ") VALUES " +
                        "(" + rs.getLong(1) +
                        ",'" + (rs.getString(2) != null ? rs.getString(2) : "") +
                        "','" + (rs.getString(3) != null ? rs.getString(3) : "") +
                        "')";
                //System.out.println("CATEGORY SQL : " +sql_x);
                res = stmt.executeUpdate(sql_x);
            }
            hasil = true;
        } catch (Exception exc) {
            System.out.println("Error transfer category out : " + exc);
        }
        return hasil;
    }

    //Transfer SubCategory Keluar
    private static boolean ProcessSubCategoryOut(Connection koneksi) {
        boolean hasil = false;
        DBResultSet dbrs = null;
        try {
            //Delete contents of SubCategory Out
            Statement stmt = koneksi.createStatement();

            //Delete SubCategory Out
            String sql = "DELETE FROM " + TBL_TRANSFER_SUB_CATEGORY;
            int res = stmt.executeUpdate(sql);

            //Fetch SubCategory
            sql = " SELECT " + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] +
                    ", " + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] +
                    ", " + PstSubCategory.fieldNames[PstSubCategory.FLD_CATEGORY_ID] +
                    ", " + PstSubCategory.fieldNames[PstSubCategory.FLD_CODE] +
                    " FROM " + PstSubCategory.TBL_SUB_CATEGORY +
                    " ORDER BY " + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID];

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            long oidREC = 0;
            boolean isInsert = true;
            while (rs.next()) {
                String sql_x = "";
                //SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //Insert item
                sql_x = "INSERT INTO " + TBL_TRANSFER_SUB_CATEGORY +
                        " (SUB_CATEGORY_ID" +
                        ", NAME" +
                        ", CATEGORY_ID" +
                        ", CODE" +
                        ") VALUES " +
                        "(" + rs.getLong(1) +
                        ",'" + (rs.getString(2) != null ? rs.getString(2) : "") +
                        "'," + rs.getLong(3) +
                        ",'" + (rs.getString(4) != null ? rs.getString(4) : "") +
                        "')";
                //System.out.println("SQL SUB CATEGORY : "+sql_x);
                res = stmt.executeUpdate(sql_x);
            }
            hasil = true;
        } catch (Exception exc) {
            System.out.println("Error transfer sub category out : " + exc);
        }
        return hasil;
    }

    //Transfer SalesPerson Keluar
    private static boolean ProcessSalesPersonOut(Connection koneksi) {
        boolean hasil = false;
        DBResultSet dbrs = null;
        try {
            //Delete contents of SalesPerson Out
            Statement stmt = koneksi.createStatement();

            //Delete SalesPerson Out
            String sql = "DELETE FROM " + TBL_TRANSFER_SALES_PERSON;
            int res = stmt.executeUpdate(sql);

            //Fetch SalesPerson
            sql = " SELECT " + PstSales.fieldNames[PstSales.FLD_SALES_ID] +
                    ", " + PstSales.fieldNames[PstSales.FLD_CODE] +
                    ", " + PstSales.fieldNames[PstSales.FLD_NAME] +
                    ", " + PstSales.fieldNames[PstSales.FLD_REMARK] +
                    " FROM " + PstSales.TBL_SALES +
                    " ORDER BY " + PstSales.fieldNames[PstSales.FLD_SALES_ID];

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            long oidREC = 0;
            boolean isInsert = true;
            while (rs.next()) {
                String sql_x = "";
                //SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //Insert item
                sql_x = "INSERT INTO " + TBL_TRANSFER_SALES_PERSON +
                        " (SALES_ID" +
                        ", SALES_CODE" +
                        ", SALES_NAME" +
                        ", REMARK" +
                        ") VALUES " +
                        "(" + rs.getLong(1) +
                        ",'" + (rs.getString(2) != null ? rs.getString(2) : "") +
                        "','" + (rs.getString(3) != null ? rs.getString(3) : "") +
                        "','" + (rs.getString(4) != null ? rs.getString(4) : "") +
                        "')";
                //System.out.println("SALES PERSON OUT SQL "+sql_x);
                res = stmt.executeUpdate(sql_x);
            }
            hasil = true;
        } catch (Exception exc) {
            System.out.println("Error transfer sales person out : " + exc);
        }
        return hasil;
    }

    //Transfer Material Keluar
    private static boolean ProcessMaterialOut(Connection koneksi) {
        boolean hasil = false;
        DBResultSet dbrs = null;
        try {
            // Delete contents of Material Out
            Statement stmt = koneksi.createStatement();

            // Delete Material Out
            String sql = "DELETE FROM " + TBL_TRANSFER_MATERIAL;
            int res = stmt.executeUpdate(sql);

            // Fetch Material
            sql = " SELECT " + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                    ", " + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                    ", " + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] +
                    ", " + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
                    ", " + PstMaterial.fieldNames[PstMaterial.FLD_MERK_ID] +
                    ", " + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                    ", " + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                    ", " + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                    ", " + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] +
                    ", " + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE_CURRENCY_ID] +
                    ", " + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] +
                    ", " + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] +
                    ", " + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST_CURRENCY_ID] +
                    ", " + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_SUPPLIER_TYPE] +
                    ", " + PstMaterial.fieldNames[PstMaterial.FLD_PRICE_TYPE_01] +
                    ", " + PstMaterial.fieldNames[PstMaterial.FLD_PRICE_TYPE_02] +
                    ", " + PstMaterial.fieldNames[PstMaterial.FLD_PRICE_TYPE_03] +
                    ", " + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] +
                    " FROM " + PstMaterial.TBL_MATERIAL +
                    " ORDER BY " + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID];

            //System.out.println("Material Out SQL : "+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            long oidREC = 0;
            boolean isInsert = true;
            while (rs.next()) {
                String sql_x = "";
                //SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //Insert item
                sql_x = "INSERT INTO " + TBL_TRANSFER_MATERIAL +
                        " (MATERIAL_ID" +
                        ", SKU" +
                        ", BARCODE" +
                        ", NAME" +
                        ", MERK_ID" +
                        ", CATEGORY_ID" +
                        ", SUB_CATEGORY_ID" +
                        ", DEFAULT_SELL_UNIT_ID" +
                        ", DEFAULT_PRICE" +
                        ", DEFAULT_PRICE_CURRENCY_ID" +
                        ", SUPPLIER_ID" +
                        ", DEFAULT_COST" +
                        ", DEFAULT_COST_CURRENCY_ID" +
                        ", DEFAULT_SUPPLIER_TYPE" +
                        ", PRICE_TYPE_01" +
                        ", PRICE_TYPE_02" +
                        ", PRICE_TYPE_03" +
                        ", MATERIAL_TYPE" +
                        ") VALUES " +
                        "(" + rs.getLong(1) +
                        ",'" + (rs.getString(2) != null ? rs.getString(2) : "") +
                        "',' " + (rs.getString(3) != null ? rs.getString(3) : "") +
                        "','" + fixingString((rs.getString(4) != null ? rs.getString(4) : "")) +
                        "'," + rs.getLong(5) +
                        "," + rs.getLong(6) +
                        "," + rs.getLong(7) +
                        "," + rs.getLong(8) +
                        "," + rs.getDouble(9) +
                        "," + rs.getLong(10) +
                        "," + rs.getLong(11) +
                        "," + rs.getDouble(12) +
                        "," + rs.getLong(13) +
                        "," + rs.getInt(14) +
                        "," + rs.getDouble(15) +
                        "," + rs.getDouble(16) +
                        "," + rs.getDouble(17) +
                        "," + rs.getInt(18) +
                        ")";

                //System.out.println("Insert Material to ODBC sql : " + sql_x);
                res = stmt.executeUpdate(sql_x);
            }
            hasil = true;
        } catch (Exception exc) {
            System.out.println("Error transfer material out : " + exc);
        }
        return hasil;
    }

    //Transfer MaterialComposit Keluar
    private static boolean ProcessMaterialCompositOut(Connection koneksi) {
        boolean hasil = false;
        DBResultSet dbrs = null;
        try {
            //Delete contents of MaterialComposit Out
            Statement stmt = koneksi.createStatement();

            //Delete MaterialComposit Out
            String sql = "DELETE FROM " + TBL_TRANSFER_MATERIAL_COMPOSIT;
            int res = stmt.executeUpdate(sql);

            //Fetch MaterialComposit
            sql = " SELECT " + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_COMPOSIT_ID] +
                    ", " + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_ID] +
                    ", " + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_UNIT_ID] +
                    ", " + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_COMPOSER_ID] +
                    ", " + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_QTY] +
                    " FROM " + PstMaterialComposit.TBL_MATERIAL_COMPOSIT +
                    " ORDER BY " + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_COMPOSIT_ID];

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            long oidREC = 0;
            boolean isInsert = true;
            while (rs.next()) {
                String sql_x = "";
                //SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //Insert item
                sql_x = "INSERT INTO " + TBL_TRANSFER_MATERIAL_COMPOSIT +
                        " (MATERIAL_COMPOSIT_ID" +
                        ", MATERIAL_ID" +
                        ", UNIT_ID" +
                        ", MATERIAL_COMPOSER_ID" +
                        ", QTY" +
                        ") VALUES " +
                        "(" + rs.getLong(1) +
                        "," + rs.getLong(2) +
                        "," + rs.getLong(3) +
                        "," + rs.getLong(4) +
                        "," + rs.getDouble(5) +
                        ")";
                //System.out.println("MATERIAL COMPOSIT SQL : "+sql);
                res = stmt.executeUpdate(sql_x);
            }
            hasil = true;
        } catch (Exception exc) {
            System.out.println("Error transfer material composit out : " + exc);
        }
        return hasil;
    }

    //Transfer MainMaxStock Keluar
    private static boolean ProcessMinMaxStockOut(Connection koneksi) {
        boolean hasil = false;
        DBResultSet dbrs = null;
        try {
            //Delete contents of MinMaxStock Out
            Statement stmt = koneksi.createStatement();

            //Delete MinMaxStock Out
            String sql = "DELETE FROM " + TBL_TRANSFER_MINMAXSTOCK;
            int res = stmt.executeUpdate(sql);

            //Fetch MinMaxStock
            sql = " SELECT " + PstMinMaxStock.fieldNames[PstMinMaxStock.FLD_MINMAXSTOCK_ID] +
                    ", " + PstMinMaxStock.fieldNames[PstMinMaxStock.FLD_MATERIAL_ID] +
                    ", " + PstMinMaxStock.fieldNames[PstMinMaxStock.FLD_LOCATION_ID] +
                    ", " + PstMinMaxStock.fieldNames[PstMinMaxStock.FLD_QTY_MIN] +
                    ", " + PstMinMaxStock.fieldNames[PstMinMaxStock.FLD_QTY_MAX] +
                    " FROM " + PstMinMaxStock.TBL_MINMAXSTOCK +
                    " ORDER BY " + PstMinMaxStock.fieldNames[PstMinMaxStock.FLD_MINMAXSTOCK_ID];

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            long oidREC = 0;
            boolean isInsert = true;
            while (rs.next()) {
                String sql_x = "";
                //SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //Insert item
                sql_x = "INSERT INTO " + TBL_TRANSFER_MINMAXSTOCK +
                        " (MINMAXSTOCK_ID" +
                        ", MATERIAL_ID" +
                        ", LOCATION_ID" +
                        ", QTY_MIN" +
                        ", QTY_MAX" +
                        ") VALUES " +
                        "(" + rs.getLong(1) +
                        "," + rs.getLong(2) +
                        "," + rs.getLong(3) +
                        "," + rs.getDouble(4) +
                        "," + rs.getDouble(5) +
                        ")";
                //System.out.println("MINMAXSTOCK SQL : "+sql_x);
                res = stmt.executeUpdate(sql_x);
            }
            hasil = true;
        } catch (Exception exc) {
            System.out.println("Error transfer minmaxstock out : " + exc);
        }
        return hasil;
    }

    //Transfer Contact Class Out
    private static boolean ProcessContactClassOut(Connection koneksi) {
        boolean hasil = false;
        DBResultSet dbrs = null;
        try {
            //Delete contents of ContactClass Out
            Statement stmt = koneksi.createStatement();

            //Delete ContactClass Out
            String sql = "DELETE FROM " + TBL_TRANSFER_CONTACT_CLASS;
            int res = stmt.executeUpdate(sql);

            //Fetch ContactClass
            sql = " SELECT " + PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID] +
                    ", " + PstContactClass.fieldNames[PstContactClass.FLD_CLASS_NAME] +
                    ", " + PstContactClass.fieldNames[PstContactClass.FLD_CLASS_DESCRIPTION] +
                    ", " + PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE] +
                    " FROM " + PstContactClass.TBL_CONTACT_CLASS +
                    " ORDER BY " + PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID];

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            long oidREC = 0;
            boolean isInsert = true;
            while (rs.next()) {
                String sql_x = "";
                //SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //Insert item
                sql_x = "INSERT INTO " + TBL_TRANSFER_CONTACT_CLASS +
                        " (CONTACT_CLASS_ID" +
                        ", CLASS_NAME" +
                        ", CLASS_DESCRIPTION" +
                        ", CLASS_TYPE" +
                        ") VALUES " +
                        "(" + rs.getLong(1) +
                        ",'" + (rs.getString(2) != null ? rs.getString(2) : "") +
                        "','" + (rs.getString(3) != null ? rs.getString(3) : "") +
                        "'," + rs.getInt(4) +
                        ")";
                //System.out.println("CONTACT CLASS SQl : "+sql_x);
                res = stmt.executeUpdate(sql_x);
            }
            hasil = true;
        } catch (Exception exc) {
            System.out.println("Error transfer contact class out : " + exc);
        }
        return hasil;
    }

    //Transfer Contact List Out
    private static boolean ProcessContactListOut(Connection koneksi) {
        boolean hasil = false;
        DBResultSet dbrs = null;
        try {
            //Delete contents of ContactList Out
            Statement stmt = koneksi.createStatement();

            //Delete ContactList Out
            String sql = "DELETE FROM " + TBL_TRANSFER_CONTACT_LIST;
            int res = stmt.executeUpdate(sql);

            //Fetch ContactList
            sql = " SELECT " + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] +
                    ", " + PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE] +
                    ", " + PstContactList.fieldNames[PstContactList.FLD_REGDATE] +
                    ", " + PstContactList.fieldNames[PstContactList.FLD_EMPLOYEE_ID] +
                    ", " + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    ", " + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] +
                    ", " + PstContactList.fieldNames[PstContactList.FLD_PERSON_LASTNAME] +
                    ", " + PstContactList.fieldNames[PstContactList.FLD_BUSS_ADDRESS] +
                    ", " + PstContactList.fieldNames[PstContactList.FLD_TOWN] +
                    ", " + PstContactList.fieldNames[PstContactList.FLD_PROVINCE] +
                    ", " + PstContactList.fieldNames[PstContactList.FLD_COUNTRY] +
                    ", " + PstContactList.fieldNames[PstContactList.FLD_TELP_NR] +
                    ", " + PstContactList.fieldNames[PstContactList.FLD_TELP_MOBILE] +
                    ", " + PstContactList.fieldNames[PstContactList.FLD_FAX] +
                    ", " + PstContactList.fieldNames[PstContactList.FLD_HOME_ADDR] +
                    ", " + PstContactList.fieldNames[PstContactList.FLD_HOME_TOWN] +
                    ", " + PstContactList.fieldNames[PstContactList.FLD_HOME_PROVINCE] +
                    ", " + PstContactList.fieldNames[PstContactList.FLD_COUNTRY] +
                    ", " + PstContactList.fieldNames[PstContactList.FLD_HOME_TELP] +
                    ", " + PstContactList.fieldNames[PstContactList.FLD_FAX] +
                    ", " + PstContactList.fieldNames[PstContactList.FLD_NOTES] +
                    ", " + PstContactList.fieldNames[PstContactList.FLD_DIRECTIONS] +
                    ", " + PstContactList.fieldNames[PstContactList.FLD_BANK_ACC] +
                    ", " + PstContactList.fieldNames[PstContactList.FLD_BANK_ACC2] +
                    ", " + PstContactList.fieldNames[PstContactList.FLD_CONTACT_TYPE] +
                    ", " + PstContactList.fieldNames[PstContactList.FLD_EMAIL] +
                    ", " + PstContactList.fieldNames[PstContactList.FLD_PARENT_ID] +
                    " FROM " + PstContactList.TBL_CONTACT_LIST +
                    " ORDER BY " + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID];

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            long oidREC = 0;
            boolean isInsert = true;
            while (rs.next()) {
                String sql_x = "";
                //SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //Insert item
                sql_x = "INSERT INTO " + TBL_TRANSFER_CONTACT_LIST +
                        " (CONTACT_ID" +
                        ", CONTACT_CODE" +
                        ", REGDATE" +
                        ", EMPLOYEE_ID" +
                        ", COMP_NAME" +
                        ", PERSON_NAME" +
                        ", PERSON_LASTNAME" +
                        ", BUSS_ADDRESS" +
                        ", TOWN" +
                        ", PROVINCE" +
                        ", COUNTRY" +
                        ", TELP_NR" +
                        ", TELP_MOBILE" +
                        ", FAX" +
                        ", HOME_ADDR" +
                        ", HOME_TOWN" +
                        ", HOME_PROVINCE" +
                        ", HOME_COUNTRY" +
                        ", HOME_TELP" +
                        ", HOME_FAX" +
                        ", NOTES" +
                        ", DIRECTIONS" +
                        ", BANK_ACC" +
                        ", BANK_ACC2" +
                        ", CONTACT_TYPE" +
                        ", EMAIL" +
                        ", PARENT_ID" +
                        ") VALUES " +
                        "(" + rs.getLong(1) +
                        ",'" + (rs.getString(2) != null ? rs.getString(2) : "") +
                        "','" + (rs.getString(3) != null ? rs.getString(3) : "") +
                        "'," + rs.getLong(4) +
                        ",'" + (rs.getString(5) != null ? rs.getString(5) : "") +
                        "','" + (rs.getString(6) != null ? rs.getString(6) : "") +
                        "','" + (rs.getString(7) != null ? rs.getString(7) : "") +
                        "','" + (rs.getString(8) != null ? rs.getString(8) : "") +
                        "','" + (rs.getString(9) != null ? rs.getString(9) : "") +
                        "','" + (rs.getString(10) != null ? rs.getString(10) : "") +
                        "','" + (rs.getString(11) != null ? rs.getString(11) : "") +
                        "','" + (rs.getString(12) != null ? rs.getString(12) : "") +
                        "','" + (rs.getString(13) != null ? rs.getString(13) : "") +
                        "','" + (rs.getString(14) != null ? rs.getString(14) : "") +
                        "','" + (rs.getString(15) != null ? rs.getString(15) : "") +
                        "','" + (rs.getString(16) != null ? rs.getString(16) : "") +
                        "','" + (rs.getString(17) != null ? rs.getString(17) : "") +
                        "','" + (rs.getString(18) != null ? rs.getString(18) : "") +
                        "','" + (rs.getString(19) != null ? rs.getString(19) : "") +
                        "','" + (rs.getString(20) != null ? rs.getString(20) : "") +
                        "','" + (rs.getString(21) != null ? rs.getString(21) : "") +
                        "','" + (rs.getString(22) != null ? rs.getString(22) : "") +
                        "','" + (rs.getString(23) != null ? rs.getString(23) : "") +
                        "','" + (rs.getString(24) != null ? rs.getString(24) : "") +
                        "'," + rs.getInt(25) +
                        ",'" + (rs.getString(26) != null ? rs.getString(26) : "") +
                        "'," + rs.getLong(27) +
                        ")";
                //System.out.println("SQL CONTACT LIST : "+sql_x);
                res = stmt.executeUpdate(sql_x);
            }
            hasil = true;
        } catch (Exception exc) {
            System.out.println("Error transfer contact list out : " + exc);
        }
        return hasil;
    }

    //Transfer Contact Class Assign Out
    private static boolean ProcessContactClassAssignOut(Connection koneksi) {
        boolean hasil = false;
        DBResultSet dbrs = null;
        try {
            //Delete contents of ContactClassAssign Out
            Statement stmt = koneksi.createStatement();

            //Delete ContactClassAssign Out
            String sql = "DELETE FROM " + TBL_TRANSFER_CONTACT_CLASS_ASSIGN;
            int res = stmt.executeUpdate(sql);

            //Fetch ContactClassAssign
            sql = " SELECT " + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CNT_CLS_ID] +
                    ", " + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CONTACT_ID] +
                    " FROM " + PstContactClassAssign.TBL_CNT_CLS_ASSIGN +
                    " ORDER BY " + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CONTACT_ID];

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            long oidREC = 0;
            boolean isInsert = true;
            while (rs.next()) {
                String sql_x = "";
                //SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //Insert item
                sql_x = "INSERT INTO " + TBL_TRANSFER_CONTACT_CLASS_ASSIGN +
                        " (CONTACT_CLASS_ID" +
                        ", CONTACT_ID" +
                        ") VALUES " +
                        "(" + rs.getLong(1) +
                        "," + rs.getLong(2) +
                        ")";
                //System.out.println("CONTACT LIST SQL : "+sql_x);
                res = stmt.executeUpdate(sql_x);
            }
            hasil = true;
        } catch (Exception exc) {
            System.out.println("Error transfer contact class assign out : " + exc);
        }
        return hasil;
    }

    //*********************************************************************//
    //*********************************************************************//
    //Transfer AppUser In
    private static boolean ProcessAppUserIn(Connection koneksi) {
        boolean hasil = false;
        try {
            int res = 0;
            //Fetch unit
            String sql = " SELECT USER_ID" +
                    ", USER_GROUP" +
                    ", LOGIN_ID" +
                    ", PASSWORD" +
                    ", FULL_NAME" +
                    ", EMAIL" +
                    ", DESCRIPTION" +
                    ", REG_DATE" +
                    ", UPDATE_DATE" +
                    ", USER_STATUS" +
                    ", LAST_LOGIN_DATE" +
                    ", LAST_LOGIN_IP" +
                    ", EMPLOYEE_ID" +
                    " FROM " + TBL_TRANSFER_APP_USER +
                    " ORDER BY USER_ID";

            //System.out.println("APPUSER IN SQL : "+sql);
            Statement stmt = koneksi.createStatement();
            ResultSet rst = stmt.executeQuery(sql);
            while (rst.next()) {
                String sql_x = "";
                long oidUser = Long.parseLong(rst.getString(1));
                //Cek OID, jika belum ada insert, jika tidak update
                boolean exist = PstAppUser.checkOID(oidUser);
                //boolean exist = true;
                if (exist == false) {
                    sql_x = "INSERT INTO " + PstAppUser.TBL_APP_USER +
                            " (" + PstAppUser.fieldNames[PstAppUser.FLD_USER_ID] +
                            ", " + PstAppUser.fieldNames[PstAppUser.FLD_USER_GROUP_NEW] +
                            ", " + PstAppUser.fieldNames[PstAppUser.FLD_LOGIN_ID] +
                            ", " + PstAppUser.fieldNames[PstAppUser.FLD_PASSWORD] +
                            ", " + PstAppUser.fieldNames[PstAppUser.FLD_FULL_NAME] +
                            ", " + PstAppUser.fieldNames[PstAppUser.FLD_EMAIL] +
                            ", " + PstAppUser.fieldNames[PstAppUser.FLD_DESCRIPTION] +
                            ", " + PstAppUser.fieldNames[PstAppUser.FLD_REG_DATE] +
                            ", " + PstAppUser.fieldNames[PstAppUser.FLD_UPDATE_DATE] +
                            ", " + PstAppUser.fieldNames[PstAppUser.FLD_USER_STATUS] +
                            ", " + PstAppUser.fieldNames[PstAppUser.FLD_LAST_LOGIN_DATE] +
                            ", " + PstAppUser.fieldNames[PstAppUser.FLD_LAST_LOGIN_IP] +
                            ", " + PstAppUser.fieldNames[PstAppUser.FLD_EMPLOYEE_ID] +
                            ") VALUES " +
                            "(" + oidUser +
                            "," + rst.getInt(2) +
                            ",'" + rst.getString(3) +
                            "','" + rst.getString(4) +
                            "','" + rst.getString(5) +
                            "','" + rst.getString(6) +
                            "','" + rst.getString(7) +
                            "','" + rst.getString(8) +
                            "','" + rst.getString(9) +
                            "'," + rst.getInt(10) +
                            ",'" + rst.getString(11) +
                            "','" + rst.getString(12) +
                            "'," + rst.getString(13) +
                            ")";
                } else {
                    sql_x = "UPDATE " + PstAppUser.TBL_APP_USER +
                            " SET " + PstAppUser.fieldNames[PstAppUser.FLD_USER_GROUP_NEW] + " = " + rst.getInt(2) +
                            ", " + PstAppUser.fieldNames[PstAppUser.FLD_LOGIN_ID] + " = '" + rst.getString(3) +
                            "', " + PstAppUser.fieldNames[PstAppUser.FLD_PASSWORD] + " = '" + rst.getString(4) +
                            "', " + PstAppUser.fieldNames[PstAppUser.FLD_FULL_NAME] + " = '" + rst.getString(5) +
                            "', " + PstAppUser.fieldNames[PstAppUser.FLD_EMAIL] + " = '" + rst.getString(6) +
                            "', " + PstAppUser.fieldNames[PstAppUser.FLD_DESCRIPTION] + " = '" + rst.getString(7) +
                            "', " + PstAppUser.fieldNames[PstAppUser.FLD_REG_DATE] + " = '" + rst.getString(8) +
                            "', " + PstAppUser.fieldNames[PstAppUser.FLD_UPDATE_DATE] + " = '" + rst.getString(9) +
                            "', " + PstAppUser.fieldNames[PstAppUser.FLD_USER_STATUS] + " = " + rst.getInt(10) +
                            ", " + PstAppUser.fieldNames[PstAppUser.FLD_LAST_LOGIN_DATE] + " = '" + rst.getString(11) +
                            "', " + PstAppUser.fieldNames[PstAppUser.FLD_LAST_LOGIN_IP] + " = '" + rst.getString(12) +
                            "', " + PstAppUser.fieldNames[PstAppUser.FLD_EMPLOYEE_ID] + " = " + rst.getString(13) +
                            " WHERE " + PstAppUser.fieldNames[PstAppUser.FLD_USER_ID] + " = " + oidUser;
                }
                System.out.println("MANAGE APPUSER IN SQL : " + sql_x);
                res = DBHandler.execUpdate(sql_x);
            }
            hasil = true;
        } catch (Exception exc) {
            System.out.println("Error transfer app user in : " + exc);
        }
        return hasil;
    }

    //Transfer Unit In
    private static boolean ProcessUnitIn(Connection koneksi) {
        boolean hasil = false;
        try {
            int res = 0;
            //Fetch unit
            String sql = " SELECT UNIT_ID" +
                    ", CODE" +
                    ", NAME" +
                    ", QTY_PER_BASE_UNIT" +
                    " FROM " + TBL_TRANSFER_UNIT +
                    " ORDER BY UNIT_ID";

            //System.out.println("UNIT IN SQL : "+sql);
            Statement stmt = koneksi.createStatement();
            ResultSet rst = stmt.executeQuery(sql);
            while (rst.next()) {
                String sql_x = "";
                long oidUnit = Long.parseLong(rst.getString(1));
                //Cek OID, jika belum ada insert, jika tidak update
                boolean exist = PstUnit.checkOID(oidUnit);
                if (exist == false) {
                    sql_x = "INSERT INTO " + PstUnit.TBL_P2_UNIT +
                            " (" + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] +
                            ", " + PstUnit.fieldNames[PstUnit.FLD_CODE] +
                            ", " + PstUnit.fieldNames[PstUnit.FLD_NAME] +
                            ", " + PstUnit.fieldNames[PstUnit.FLD_QTY_PER_BASE_UNIT] +
                            ") VALUES " +
                            "(" + oidUnit +
                            ",'" + rst.getString(2) +
                            "','" + rst.getString(3) +
                            "'," + rst.getDouble(4) +
                            ")";
                } else {
                    sql_x = "UPDATE " + PstUnit.TBL_P2_UNIT +
                            " SET " + PstUnit.fieldNames[PstUnit.FLD_CODE] + " = '" + rst.getString(2) +
                            "', " + PstUnit.fieldNames[PstUnit.FLD_NAME] + " = '" + rst.getString(3) +
                            "', " + PstUnit.fieldNames[PstUnit.FLD_QTY_PER_BASE_UNIT] + " = " + rst.getDouble(4) +
                            " WHERE " + PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] + " = " + oidUnit;
                }
                //System.out.println("MANAGE UNIT IN SQL : "+sql_x);
                res = DBHandler.execUpdate(sql_x);
            }
            hasil = true;
        } catch (Exception exc) {
            System.out.println("Error transfer unit in : " + exc);
        }
        return hasil;
    }

    //Transfer Shift In
    private static boolean ProcessShiftIn(Connection koneksi) {
        boolean hasil = false;
        try {
            int res = 0;
            //Fetch shift
            String sql = " SELECT SHIFT_ID" +
                    ", NAME" +
                    ", REMARK" +
                    " FROM " + TBL_TRANSFER_SHIFT +
                    " ORDER BY SHIFT_ID";

            //System.out.println("SHIFT IN SQL : "+sql);
            Statement stmt = koneksi.createStatement();
            ResultSet rst = stmt.executeQuery(sql);
            while (rst.next()) {
                String sql_x = "";
                long oidShift = Long.parseLong(rst.getString(1));
                //Cek OID, jika belum ada insert, jika tidak update
                boolean exist = PstShift.checkOID(oidShift);
                if (exist == false) {
                    sql_x = "INSERT INTO " + PstShift.TBL_SHIFT +
                            " (" + PstShift.fieldNames[PstShift.FLD_SHIFT_ID] +
                            ", " + PstShift.fieldNames[PstShift.FLD_NAME] +
                            ", " + PstShift.fieldNames[PstShift.FLD_REMARK] +
                            ") VALUES " +
                            "(" + oidShift +
                            ",'" + rst.getString(2) +
                            "','" + rst.getString(3) +
                            "')";
                } else {
                    sql_x = "UPDATE " + PstShift.TBL_SHIFT +
                            " SET " + PstShift.fieldNames[PstShift.FLD_NAME] + " = '" + rst.getString(2) +
                            "', " + PstShift.fieldNames[PstShift.FLD_REMARK] + " = '" + rst.getString(3) +
                            "' WHERE " + PstShift.fieldNames[PstShift.FLD_SHIFT_ID] + " = " + oidShift;
                }
                //System.out.println("MANAGE SHIFT IN SQL : "+sql_x);
                res = DBHandler.execUpdate(sql_x);
            }
            hasil = true;
        } catch (Exception exc) {
            System.out.println("Error transfer shift in : " + exc);
        }
        return hasil;
    }

    //Transfer Periode In
    private static boolean ProcessPeriodeIn(Connection koneksi) {
        boolean hasil = false;
        try {
            int res = 0;
            //Fetch periode
            String sql = " SELECT PERIODE_ID" +
                    ", NAME" +
                    ", PERIODE_TYPE" +
                    ", STATUS" +
                    ", START_DATE" +
                    ", END_DATE" +
                    " FROM " + TBL_TRANSFER_PERIODE +
                    " ORDER BY PERIODE_ID";

            //System.out.println("PERIOD IN SQL : "+sql);
            Statement stmt = koneksi.createStatement();
            ResultSet rst = stmt.executeQuery(sql);
            while (rst.next()) {
                String sql_x = "";
                long oidPeriode = Long.parseLong(rst.getString(1));
                //Cek OID, jika belum ada insert, jika tidak update
                boolean exist = PstPeriode.checkOID(oidPeriode);
                if (exist == false) {
                    sql_x = "INSERT INTO " + PstPeriode.TBL_STOCK_PERIODE +
                            " (" + PstPeriode.fieldNames[PstPeriode.FLD_STOCK_PERIODE_ID] +
                            ", " + PstPeriode.fieldNames[PstPeriode.FLD_PERIODE_NAME] +
                            ", " + PstPeriode.fieldNames[PstPeriode.FLD_PERIODE_TYPE] +
                            ", " + PstPeriode.fieldNames[PstPeriode.FLD_STATUS] +
                            ", " + PstPeriode.fieldNames[PstPeriode.FLD_START_DATE] +
                            ", " + PstPeriode.fieldNames[PstPeriode.FLD_END_DATE] +
                            ") VALUES " +
                            "(" + oidPeriode +
                            ",'" + rst.getString(2) +
                            "'," + rst.getInt(3) +
                            "," + rst.getInt(4) +
                            ",'" + rst.getString(5) +
                            "','" + rst.getString(6) +
                            "')";
                } else {
                    sql_x = "UPDATE " + PstPeriode.TBL_STOCK_PERIODE +
                            " SET " + PstPeriode.fieldNames[PstPeriode.FLD_PERIODE_NAME] + " = '" + rst.getString(2) +
                            "', " + PstPeriode.fieldNames[PstPeriode.FLD_PERIODE_TYPE] + " = " + rst.getInt(3) +
                            ", " + PstPeriode.fieldNames[PstPeriode.FLD_STATUS] + " = " + rst.getInt(4) +
                            ", " + PstPeriode.fieldNames[PstPeriode.FLD_START_DATE] + " = '" + rst.getString(5) +
                            "', " + PstPeriode.fieldNames[PstPeriode.FLD_END_DATE] + " = '" + rst.getString(6) +
                            "' WHERE " + PstPeriode.fieldNames[PstPeriode.FLD_STOCK_PERIODE_ID] + " = " + oidPeriode;
                }
                //System.out.println("INSERT PERIOD IN SQL : "+sql_x);
                res = DBHandler.execUpdate(sql_x);
            }
            hasil = true;
        } catch (Exception exc) {
            System.out.println("Error transfer periode in : " + exc);
        }
        return hasil;
    }

    //Transfer Currency In
    private static boolean ProcessCurrencyIn(Connection koneksi) {
        boolean hasil = false;
        try {
            int res = 0;
            //Fetch currency
            String sql = " SELECT CURRENCY_ID" +
                    ", CODE" +
                    ", NAME" +
                    ", SELLING_RATE" +
                    ", BUYING_RATE" +
                    " FROM " + TBL_TRANSFER_CURRENCY +
                    " ORDER BY CURRENCY_ID";

            //System.out.println("CURRENCY IN SQL : "+sql);
            Statement stmt = koneksi.createStatement();
            ResultSet rst = stmt.executeQuery(sql);
            while (rst.next()) {
                String sql_x = "";
                long oidCurrency = Long.parseLong(rst.getString(1));
                //Cek OID, jika belum ada insert, jika tidak update
                boolean exist = PstMatCurrency.checkOID(oidCurrency);
                if (exist == false) {
                    sql_x = "INSERT INTO " + PstMatCurrency.TBL_CURRENCY +
                            " (" + PstMatCurrency.fieldNames[PstMatCurrency.FLD_CURRENCY_ID] +
                            ", " + PstMatCurrency.fieldNames[PstMatCurrency.FLD_CODE] +
                            ", " + PstMatCurrency.fieldNames[PstMatCurrency.FLD_NAME] +
                            ", " + PstMatCurrency.fieldNames[PstMatCurrency.FLD_SELLING_RATE] +
                            ", " + PstMatCurrency.fieldNames[PstMatCurrency.FLD_BUYING_RATE] +
                            ") VALUES " +
                            "(" + oidCurrency +
                            ",'" + rst.getString(2) +
                            "','" + rst.getString(3) +
                            "'," + rst.getDouble(4) +
                            "," + rst.getDouble(5) +
                            ")";
                } else {
                    sql_x = "UPDATE " + PstMatCurrency.TBL_CURRENCY +
                            " SET " + PstMatCurrency.fieldNames[PstMatCurrency.FLD_CODE] + " = '" + rst.getString(2) +
                            "', " + PstMatCurrency.fieldNames[PstMatCurrency.FLD_NAME] + " = '" + rst.getString(3) +
                            "', " + PstMatCurrency.fieldNames[PstMatCurrency.FLD_SELLING_RATE] + " = " + rst.getDouble(4) +
                            ", " + PstMatCurrency.fieldNames[PstMatCurrency.FLD_BUYING_RATE] + " = " + rst.getDouble(5) +
                            " WHERE " + PstMatCurrency.fieldNames[PstMatCurrency.FLD_CURRENCY_ID] + " = " + oidCurrency;
                }
                //System.out.println("INSERT CURRENCY IN SQL : "+sql_x);
                res = DBHandler.execUpdate(sql_x);
            }
            hasil = true;
        } catch (Exception exc) {
            System.out.println("Error transfer currency in : " + exc);
        }
        return hasil;
    }

    //Transfer Category In
    private static boolean ProcessCategoryIn(Connection koneksi) {
        boolean hasil = false;
        try {
            int res = 0;
            //Fetch category
            String sql = " SELECT CATEGORY_ID" +
                    ", CODE" +
                    ", NAME" +
                    " FROM " + TBL_TRANSFER_CATEGORY +
                    " ORDER BY CATEGORY_ID";

            //System.out.println("CATEGORY IN SQL : "+sql);
            Statement stmt = koneksi.createStatement();
            ResultSet rst = stmt.executeQuery(sql);
            while (rst.next()) {
                String sql_x = "";
                long oidCategory = Long.parseLong(rst.getString(1));
                //Cek OID, jika belum ada insert, jika tidak update
                boolean exist = PstCategory.checkOID(oidCategory);
                if (exist == false) {
                    sql_x = "INSERT INTO " + PstCategory.TBL_CATEGORY +
                            " (" + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
                            ", " + PstCategory.fieldNames[PstCategory.FLD_CODE] +
                            ", " + PstCategory.fieldNames[PstCategory.FLD_NAME] +
                            ") VALUES " +
                            "(" + oidCategory +
                            ",'" + rst.getString(2) +
                            "','" + rst.getString(3) +
                            "')";
                } else {
                    sql_x = "UPDATE " + PstCategory.TBL_CATEGORY +
                            " SET " + PstCategory.fieldNames[PstCategory.FLD_CODE] + " = '" + rst.getString(2) +
                            "', " + PstCategory.fieldNames[PstCategory.FLD_NAME] + " = '" + rst.getString(3) +
                            "' WHERE " + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + " = " + oidCategory;
                }
                //System.out.println("MANAGE CATEGORY IN SQL : "+sql_x);
                res = DBHandler.execUpdate(sql_x);
            }
            hasil = true;
        } catch (Exception exc) {
            System.out.println("Error transfer category in : " + exc);
        }
        return hasil;
    }

    //Transfer SubCategory In
    private static boolean ProcessSubCategoryIn(Connection koneksi) {
        boolean hasil = false;
        try {
            int res = 0;
            //Fetch sub category
            String sql = " SELECT SUB_CATEGORY_ID" +
                    ", CODE" +
                    ", CATEGORY_ID" +
                    ", NAME" +
                    " FROM " + TBL_TRANSFER_SUB_CATEGORY +
                    " ORDER BY SUB_CATEGORY_ID";

            //System.out.println("SUB CATEGORY IN SQL : "+sql);
            Statement stmt = koneksi.createStatement();
            ResultSet rst = stmt.executeQuery(sql);
            while (rst.next()) {
                String sql_x = "";
                long oidSubCategory = Long.parseLong(rst.getString(1));
                //Cek OID, jika belum ada insert, jika tidak update
                boolean exist = PstSubCategory.checkOID(oidSubCategory);
                if (exist == false) {
                    sql_x = "INSERT INTO " + PstSubCategory.TBL_SUB_CATEGORY +
                            " (" + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] +
                            ", " + PstSubCategory.fieldNames[PstSubCategory.FLD_CODE] +
                            ", " + PstSubCategory.fieldNames[PstSubCategory.FLD_CATEGORY_ID] +
                            ", " + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] +
                            ") VALUES " +
                            "(" + oidSubCategory +
                            ",'" + rst.getString(2) +
                            "'," + rst.getString(3) +
                            ",'" + rst.getString(4) +
                            "')";
                } else {
                    sql_x = "UPDATE " + PstSubCategory.TBL_SUB_CATEGORY +
                            " SET " + PstSubCategory.fieldNames[PstSubCategory.FLD_CODE] + " = '" + rst.getString(2) +
                            "', " + PstSubCategory.fieldNames[PstSubCategory.FLD_CATEGORY_ID] + " = " + rst.getString(3) +
                            ", " + PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] + " = '" + rst.getString(4) +
                            "' WHERE " + PstSubCategory.fieldNames[PstSubCategory.FLD_SUB_CATEGORY_ID] + " = " + oidSubCategory;
                }
                //System.out.println("SUB CATEGORY IN SQL : "+sql_x);
                res = DBHandler.execUpdate(sql_x);
            }
            hasil = true;
        } catch (Exception exc) {
            System.out.println("Error transfer sub category in : " + exc);
        }
        return hasil;
    }

    //Transfer SalesPerson In
    private static boolean ProcessSalesPersonIn(Connection koneksi) {
        boolean hasil = false;
        try {
            int res = 0;
            //Fetch sales person
            String sql = " SELECT SALES_ID" +
                    ", SALES_CODE" +
                    ", SALES_NAME" +
                    ", REMARK" +
                    " FROM " + TBL_TRANSFER_SALES_PERSON +
                    " ORDER BY SALES_ID";

            //System.out.println("SALES PERSON SQL : "+sql);
            Statement stmt = koneksi.createStatement();
            ResultSet rst = stmt.executeQuery(sql);
            while (rst.next()) {
                String sql_x = "";
                long oidSales = Long.parseLong(rst.getString(1));
                //Cek OID, jika belum ada insert, jika tidak update
                boolean exist = PstSales.checkOID(oidSales);
                if (exist == false) {
                    sql_x = "INSERT INTO " + PstSales.TBL_SALES +
                            " (" + PstSales.fieldNames[PstSales.FLD_SALES_ID] +
                            ", " + PstSales.fieldNames[PstSales.FLD_CODE] +
                            ", " + PstSales.fieldNames[PstSales.FLD_NAME] +
                            ", " + PstSales.fieldNames[PstSales.FLD_REMARK] +
                            ") VALUES " +
                            "(" + oidSales +
                            ",'" + rst.getString(2) +
                            "','" + rst.getString(3) +
                            "','" + rst.getString(4) +
                            "')";
                } else {
                    sql_x = "UPDATE " + PstSales.TBL_SALES +
                            " SET " + PstSales.fieldNames[PstSales.FLD_CODE] + " = '" + rst.getString(2) +
                            "', " + PstSales.fieldNames[PstSales.FLD_NAME] + " = '" + rst.getString(3) +
                            "', " + PstSales.fieldNames[PstSales.FLD_REMARK] + " = '" + rst.getString(4) +
                            "' WHERE " + PstSales.fieldNames[PstSales.FLD_SALES_ID] + " = " + oidSales;
                }
                //System.out.println("MANAGE SALES PERSON SQL : "+sql_x);
                res = DBHandler.execUpdate(sql_x);
            }
            hasil = true;
        } catch (Exception exc) {
            System.out.println("Error transfer sales person in : " + exc);
        }
        return hasil;
    }

    //Transfer Material In
    private static boolean ProcessMaterialIn(Connection koneksi) {
        boolean hasil = false;
        try {
            int res = 0;
            //Fetch material
            String sql = " SELECT MATERIAL_ID" +
                    ", SKU" +
                    ", BARCODE" +
                    ", NAME" +
                    ", MERK_ID" +
                    ", CATEGORY_ID" +
                    ", SUB_CATEGORY_ID" +
                    ", DEFAULT_SELL_UNIT_ID" +
                    ", DEFAULT_PRICE" +
                    ", DEFAULT_PRICE_CURRENCY_ID" +
                    ", SUPPLIER_ID" +
                    ", DEFAULT_COST" +
                    ", DEFAULT_COST_CURRENCY_ID" +
                    ", DEFAULT_SUPPLIER_TYPE" +
                    ", PRICE_TYPE_01" +
                    ", PRICE_TYPE_02" +
                    ", PRICE_TYPE_03" +
                    ", MATERIAL_TYPE" +
                    " FROM " + TBL_TRANSFER_MATERIAL +
                    " ORDER BY MATERIAL_ID";

            Statement stmt = koneksi.createStatement();
            ResultSet rst = stmt.executeQuery(sql);
            while (rst.next()) {
                String sql_x = "";
                long oidMaterial = Long.parseLong(rst.getString(1));
                //Cek OID, jika belum ada insert, jika tidak update
                boolean exist = PstMaterial.checkOID(oidMaterial);
                if (exist == false) {
                    sql_x = "INSERT INTO " + PstMaterial.TBL_MATERIAL +
                            " (" + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                            ", " + PstMaterial.fieldNames[PstMaterial.FLD_SKU] +
                            ", " + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] +
                            ", " + PstMaterial.fieldNames[PstMaterial.FLD_NAME] +
                            ", " + PstMaterial.fieldNames[PstMaterial.FLD_MERK_ID] +
                            ", " + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                            ", " + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] +
                            ", " + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] +
                            ", " + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] +
                            ", " + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE_CURRENCY_ID] +
                            ", " + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] +
                            ", " + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] +
                            ", " + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST_CURRENCY_ID] +
                            ", " + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_SUPPLIER_TYPE] +
                            ", " + PstMaterial.fieldNames[PstMaterial.FLD_PRICE_TYPE_01] +
                            ", " + PstMaterial.fieldNames[PstMaterial.FLD_PRICE_TYPE_02] +
                            ", " + PstMaterial.fieldNames[PstMaterial.FLD_PRICE_TYPE_03] +
                            ", " + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] +
                            ") VALUES " +
                            "(" + oidMaterial +
                            ",'" + rst.getString(2) +
                            "','" + rst.getString(3) +
                            "','" + fixingString(rst.getString(4)) +
                            "'," + rst.getString(5) +
                            "," + rst.getString(6) +
                            "," + rst.getString(7) +
                            "," + rst.getString(8) +
                            "," + rst.getDouble(9) +
                            "," + rst.getString(10) +
                            "," + rst.getString(11) +
                            "," + rst.getDouble(12) +
                            "," + rst.getString(13) +
                            "," + rst.getInt(14) +
                            "," + rst.getDouble(15) +
                            "," + rst.getDouble(16) +
                            "," + rst.getDouble(17) +
                            "," + rst.getInt(18) +
                            ")";
                } else {
                    sql_x = "UPDATE " + PstMaterial.TBL_MATERIAL +
                            " SET " + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " = '" + rst.getString(2) +
                            "', " + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " = '" + rst.getString(3) +
                            "', " + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " = '" + fixingString(rst.getString(4)) +
                            "', " + PstMaterial.fieldNames[PstMaterial.FLD_MERK_ID] + " = " + rst.getString(5) +
                            ", " + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + rst.getString(6) +
                            ", " + PstMaterial.fieldNames[PstMaterial.FLD_SUB_CATEGORY_ID] + " = " + rst.getString(7) +
                            ", " + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_STOCK_UNIT_ID] + " = " + rst.getString(8) +
                            ", " + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE] + " = " + rst.getDouble(9) +
                            ", " + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_PRICE_CURRENCY_ID] + " = " + rst.getString(10) +
                            ", " + PstMaterial.fieldNames[PstMaterial.FLD_SUPPLIER_ID] + " = " + rst.getString(11) +
                            ", " + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] + " = " + rst.getString(12) +
                            ", " + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST_CURRENCY_ID] + " = " + rst.getString(13) +
                            ", " + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_SUPPLIER_TYPE] + " = " + rst.getString(14) +
                            ", " + PstMaterial.fieldNames[PstMaterial.FLD_PRICE_TYPE_01] + " = " + rst.getDouble(15) +
                            ", " + PstMaterial.fieldNames[PstMaterial.FLD_PRICE_TYPE_02] + " = " + rst.getDouble(16) +
                            ", " + PstMaterial.fieldNames[PstMaterial.FLD_PRICE_TYPE_03] + " = " + rst.getDouble(17) +
                            ", " + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] + " = " + rst.getInt(18) +
                            " WHERE " + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + " = " + oidMaterial;
                }
                //System.out.println("sql_x : "+sql_x);
                res = DBHandler.execUpdate(sql_x);
            }
            hasil = true;
        } catch (Exception exc) {
            System.out.println("Error transfer material in : " + exc);
        }
        return hasil;
    }

    //Transfer MaterialComposit
    private static boolean ProcessMaterialCompositIn(Connection koneksi) {
        boolean hasil = false;
        try {
            int res = 0;
            //Fetch material composit
            String sql = " SELECT MATERIAL_COMPOSIT_ID" +
                    ", MATERIAL_ID" +
                    ", UNIT_ID" +
                    ", MATERIAL_COMPOSER_ID" +
                    ", QTY" +
                    " FROM " + TBL_TRANSFER_MATERIAL_COMPOSIT +
                    " ORDER BY MATERIAL_COMPOSIT_ID";

            //System.out.println("MATCOMPOSIT IN SQL : "+sql);
            Statement stmt = koneksi.createStatement();
            ResultSet rst = stmt.executeQuery(sql);
            while (rst.next()) {
                String sql_x = "";
                long oidComposit = Long.parseLong(rst.getString(1));
                //Cek OID, jika belum ada insert, jika tidak update
                boolean exist = PstMaterialComposit.checkOID(oidComposit);
                if (exist == false) {
                    sql_x = "INSERT INTO " + PstMaterialComposit.TBL_MATERIAL_COMPOSIT +
                            " (" + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_COMPOSIT_ID] +
                            ", " + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_ID] +
                            ", " + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_UNIT_ID] +
                            ", " + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_COMPOSER_ID] +
                            ", " + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_QTY] +
                            ") VALUES " +
                            "(" + oidComposit +
                            "," + rst.getString(2) +
                            "," + rst.getString(3) +
                            "," + rst.getString(4) +
                            "," + rst.getDouble(5) +
                            ")";
                } else {
                    sql_x = "UPDATE " + PstMaterialComposit.TBL_MATERIAL_COMPOSIT +
                            " SET " + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_ID] + " = " + rst.getString(2) +
                            ", " + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_UNIT_ID] + " = " + rst.getString(3) +
                            ", " + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_COMPOSER_ID] + " = " + rst.getString(4) +
                            ", " + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_QTY] + "  = " + rst.getDouble(5) +
                            " WHERE " + PstMaterialComposit.fieldNames[PstMaterialComposit.FLD_MATERIAL_COMPOSIT_ID] + " = " + oidComposit;
                }
                //System.out.println("INSERT MATCOMPOSIT IN SQL : "+sql_x);
                res = DBHandler.execUpdate(sql_x);
            }
            hasil = true;
        } catch (Exception exc) {
            System.out.println("Error transfer material composit in : " + exc);
        }
        return hasil;
    }

    //Transfer MinMaxStock
    private static boolean ProcessMinMaxStockIn(Connection koneksi) {
        boolean hasil = false;
        try {
            int res = 0;
            //Fetch minmaxstock
            String sql = " SELECT MINMAXSTOCK_ID" +
                    ", MATERIAL_ID" +
                    ", LOCATION_ID" +
                    ", QTY_MIN" +
                    ", QTY_MAX" +
                    " FROM " + TBL_TRANSFER_MINMAXSTOCK +
                    " ORDER BY MINMAXSTOCK_ID";

            //System.out.println("MINMATSTOCK IN SQL : "+sql);
            Statement stmt = koneksi.createStatement();
            ResultSet rst = stmt.executeQuery(sql);
            while (rst.next()) {
                String sql_x = "";
                long oidMinMax = Long.parseLong(rst.getString(1));
                //Cek OID, jika belum ada insert, jika tidak update
                boolean exist = PstMinMaxStock.checkOID(oidMinMax);
                if (exist == false) {
                    sql_x = "INSERT INTO " + PstMinMaxStock.TBL_MINMAXSTOCK +
                            " (" + PstMinMaxStock.fieldNames[PstMinMaxStock.FLD_MINMAXSTOCK_ID] +
                            ", " + PstMinMaxStock.fieldNames[PstMinMaxStock.FLD_MATERIAL_ID] +
                            ", " + PstMinMaxStock.fieldNames[PstMinMaxStock.FLD_LOCATION_ID] +
                            ", " + PstMinMaxStock.fieldNames[PstMinMaxStock.FLD_QTY_MIN] +
                            ", " + PstMinMaxStock.fieldNames[PstMinMaxStock.FLD_QTY_MAX] +
                            ") VALUES " +
                            "(" + oidMinMax +
                            "," + rst.getString(2) +
                            "," + rst.getString(3) +
                            "," + rst.getDouble(4) +
                            "," + rst.getDouble(5) +
                            ")";
                } else {
                    sql_x = "UPDATE " + PstMinMaxStock.TBL_MINMAXSTOCK +
                            " SET " + PstMinMaxStock.fieldNames[PstMinMaxStock.FLD_MATERIAL_ID] + " = " + rst.getString(2) +
                            ", " + PstMinMaxStock.fieldNames[PstMinMaxStock.FLD_LOCATION_ID] + " = " + rst.getString(3) +
                            ", " + PstMinMaxStock.fieldNames[PstMinMaxStock.FLD_QTY_MIN] + " = " + rst.getDouble(4) +
                            ", " + PstMinMaxStock.fieldNames[PstMinMaxStock.FLD_QTY_MAX] + " = " + rst.getDouble(5) +
                            " WHERE " + PstMinMaxStock.fieldNames[PstMinMaxStock.FLD_MINMAXSTOCK_ID] + " = " + oidMinMax;
                }
                //System.out.println("INSERT MINMATSTOCK IN SQL : "+sql_x);
                res = DBHandler.execUpdate(sql_x);
            }
            hasil = true;
        } catch (Exception exc) {
            System.out.println("Error transfer minmaxstock in : " + exc);
        }
        return hasil;
    }

    //Transfer ContactClass
    private static boolean ProcessContactClassIn(Connection koneksi) {
        boolean hasil = false;
        try {
            int res = 0;
            //Fetch contact class
            String sql = " SELECT CONTACT_CLASS_ID" +
                    ", CLASS_NAME" +
                    ", CLASS_DESCRIPTION" +
                    ", CLASS_TYPE" +
                    " FROM " + TBL_TRANSFER_CONTACT_CLASS +
                    " ORDER BY CONTACT_CLASS_ID";

            //System.out.println("CONTACT CLASS SQL : "+sql);
            Statement stmt = koneksi.createStatement();
            ResultSet rst = stmt.executeQuery(sql);
            while (rst.next()) {
                String sql_x = "";
                long oidContactClass = Long.parseLong(rst.getString(1));
                //Cek OID, jika belum ada insert, jika tidak update
                boolean exist = PstContactClass.checkOID(oidContactClass);
                if (exist == false) {
                    sql_x = "INSERT INTO " + PstContactClass.TBL_CONTACT_CLASS +
                            " (" + PstContactClass.fieldNames[PstContactClass.FLD_CONTACT_CLASS_ID] +
                            ", " + PstContactClass.fieldNames[PstContactClass.FLD_CLASS_NAME] +
                            ", " + PstContactClass.fieldNames[PstContactClass.FLD_CLASS_DESCRIPTION] +
                            ", " + PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE] +
                            ") VALUES " +
                            "(" + oidContactClass +
                            ",'" + rst.getString(2) +
                            "','" + rst.getString(3) +
                            "'," + rst.getInt(4) +
                            ")";
                } else {
                    sql_x = "UPDATE " + PstContactClass.TBL_CONTACT_CLASS +
                            " SET " + PstContactClass.fieldNames[PstContactClass.FLD_CLASS_NAME] + " = '" + rst.getString(2) +
                            "', " + PstContactClass.fieldNames[PstContactClass.FLD_CLASS_DESCRIPTION] + " = '" + rst.getString(3) +
                            "', " + PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE] + " = " + rst.getInt(4) +
                            " WHERE CONTACT_CLASS_ID = " + oidContactClass;
                }
                //System.out.println("MANAGE CONTACT CLASS SQL : "+sql_x);
                res = DBHandler.execUpdate(sql_x);
            }
            hasil = true;
        } catch (Exception exc) {
            System.out.println("Error transfer contact class in : " + exc);
        }
        return hasil;
    }

    //Transfer ContactList
    private static boolean ProcessContactListIn(Connection koneksi) {
        boolean hasil = false;
        try {
            int res = 0;
            //Fetch contact list
            String sql = " SELECT CONTACT_ID" +
                    ", CONTACT_CODE" +
                    ", REGDATE" +
                    ", EMPLOYEE_ID" +
                    ", COMP_NAME" +
                    ", PERSON_NAME" +
                    ", PERSON_LASTNAME" +
                    ", BUSS_ADDRESS" +
                    ", TOWN" +
                    ", PROVINCE" +
                    ", COUNTRY" +
                    ", TELP_NR" +
                    ", TELP_MOBILE" +
                    ", FAX" +
                    ", HOME_ADDR" +
                    ", HOME_TOWN" +
                    ", HOME_PROVINCE" +
                    ", HOME_COUNTRY" +
                    ", HOME_TELP" +
                    ", HOME_FAX" +
                    ", NOTES" +
                    ", DIRECTIONS" +
                    ", BANK_ACC" +
                    ", BANK_ACC2" +
                    ", CONTACT_TYPE" +
                    ", EMAIL" +
                    ", PARENT_ID" +
                    " FROM " + TBL_TRANSFER_CONTACT_LIST +
                    " ORDER BY CONTACT_ID";

            //System.out.println("CONTACT LIST SQL : "+sql);
            Statement stmt = koneksi.createStatement();
            ResultSet rst = stmt.executeQuery(sql);
            while (rst.next()) {
                String sql_x = "";
                long oidContactList = Long.parseLong(rst.getString(1));
                //Cek OID, jika belum ada insert, jika tidak update
                boolean exist = PstContactList.checkOID(oidContactList);
                if (exist == false) {
                    sql_x = "INSERT INTO " + PstContactList.TBL_CONTACT_LIST +
                            " (" + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] +
                            ", " + PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE] +
                            ", " + PstContactList.fieldNames[PstContactList.FLD_REGDATE] +
                            ", " + PstContactList.fieldNames[PstContactList.FLD_EMPLOYEE_ID] +
                            ", " + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                            ", " + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] +
                            ", " + PstContactList.fieldNames[PstContactList.FLD_PERSON_LASTNAME] +
                            ", " + PstContactList.fieldNames[PstContactList.FLD_BUSS_ADDRESS] +
                            ", " + PstContactList.fieldNames[PstContactList.FLD_TOWN] +
                            ", " + PstContactList.fieldNames[PstContactList.FLD_PROVINCE] +
                            ", " + PstContactList.fieldNames[PstContactList.FLD_COUNTRY] +
                            ", " + PstContactList.fieldNames[PstContactList.FLD_TELP_NR] +
                            ", " + PstContactList.fieldNames[PstContactList.FLD_TELP_MOBILE] +
                            ", " + PstContactList.fieldNames[PstContactList.FLD_FAX] +
                            ", " + PstContactList.fieldNames[PstContactList.FLD_HOME_ADDR] +
                            ", " + PstContactList.fieldNames[PstContactList.FLD_HOME_TOWN] +
                            ", " + PstContactList.fieldNames[PstContactList.FLD_HOME_PROVINCE] +
                            ", " + PstContactList.fieldNames[PstContactList.FLD_HOME_COUNTRY] +
                            ", " + PstContactList.fieldNames[PstContactList.FLD_HOME_TELP] +
                            ", " + PstContactList.fieldNames[PstContactList.FLD_HOME_FAX] +
                            ", " + PstContactList.fieldNames[PstContactList.FLD_NOTES] +
                            ", " + PstContactList.fieldNames[PstContactList.FLD_DIRECTIONS] +
                            ", " + PstContactList.fieldNames[PstContactList.FLD_BANK_ACC] +
                            ", " + PstContactList.fieldNames[PstContactList.FLD_BANK_ACC2] +
                            ", " + PstContactList.fieldNames[PstContactList.FLD_CONTACT_TYPE] +
                            ", " + PstContactList.fieldNames[PstContactList.FLD_EMAIL] +
                            ", " + PstContactList.fieldNames[PstContactList.FLD_PARENT_ID] +
                            ") VALUES " +
                            "(" + oidContactList +
                            ",'" + rst.getString(2) +
                            "','" + rst.getString(3) +
                            "'," + rst.getString(4) +
                            ",'" + rst.getString(5) +
                            "','" + rst.getString(6) +
                            "','" + rst.getString(7) +
                            "','" + rst.getString(8) +
                            "','" + rst.getString(9) +
                            "','" + rst.getString(10) +
                            "','" + rst.getString(11) +
                            "','" + rst.getString(12) +
                            "','" + rst.getString(13) +
                            "','" + rst.getString(14) +
                            "','" + rst.getString(15) +
                            "','" + rst.getString(16) +
                            "','" + rst.getString(17) +
                            "','" + rst.getString(18) +
                            "','" + rst.getString(19) +
                            "','" + rst.getString(20) +
                            "','" + rst.getString(21) +
                            "','" + rst.getString(22) +
                            "','" + rst.getString(23) +
                            "','" + rst.getString(24) +
                            "'," + rst.getInt(25) +
                            ",'" + rst.getString(26) +
                            "'," + rst.getString(27) +
                            ")";
                } else {
                    sql_x = "UPDATE " + PstContactList.TBL_CONTACT_LIST +
                            " SET " + PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE] + " = '" + rst.getString(2) +
                            "', " + PstContactList.fieldNames[PstContactList.FLD_REGDATE] + " = '" + rst.getString(3) +
                            "', " + PstContactList.fieldNames[PstContactList.FLD_EMPLOYEE_ID] + " = " + rst.getString(4) +
                            ", " + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " = '" + rst.getString(5) +
                            "', " + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " = '" + rst.getString(6) +
                            "', " + PstContactList.fieldNames[PstContactList.FLD_PERSON_LASTNAME] + " = '" + rst.getString(7) +
                            "', " + PstContactList.fieldNames[PstContactList.FLD_BUSS_ADDRESS] + " = '" + rst.getString(8) +
                            "', " + PstContactList.fieldNames[PstContactList.FLD_TOWN] + " = '" + rst.getString(9) +
                            "', " + PstContactList.fieldNames[PstContactList.FLD_PROVINCE] + " = '" + rst.getString(10) +
                            "', " + PstContactList.fieldNames[PstContactList.FLD_COUNTRY] + " = '" + rst.getString(11) +
                            "', " + PstContactList.fieldNames[PstContactList.FLD_TELP_NR] + " = '" + rst.getString(12) +
                            "', " + PstContactList.fieldNames[PstContactList.FLD_TELP_MOBILE] + " = '" + rst.getString(13) +
                            "', " + PstContactList.fieldNames[PstContactList.FLD_FAX] + " = '" + rst.getString(14) +
                            "', " + PstContactList.fieldNames[PstContactList.FLD_HOME_ADDR] + " = '" + rst.getString(15) +
                            "', " + PstContactList.fieldNames[PstContactList.FLD_HOME_TOWN] + " = '" + rst.getString(16) +
                            "', " + PstContactList.fieldNames[PstContactList.FLD_HOME_PROVINCE] + " = '" + rst.getString(17) +
                            "', " + PstContactList.fieldNames[PstContactList.FLD_HOME_COUNTRY] + " = '" + rst.getString(18) +
                            "', " + PstContactList.fieldNames[PstContactList.FLD_HOME_TELP] + " = '" + rst.getString(19) +
                            "', " + PstContactList.fieldNames[PstContactList.FLD_HOME_FAX] + " = '" + rst.getString(20) +
                            "', " + PstContactList.fieldNames[PstContactList.FLD_NOTES] + " = '" + rst.getString(21) +
                            "', " + PstContactList.fieldNames[PstContactList.FLD_DIRECTIONS] + " = '" + rst.getString(22) +
                            "', " + PstContactList.fieldNames[PstContactList.FLD_BANK_ACC] + " = '" + rst.getString(23) +
                            "', " + PstContactList.fieldNames[PstContactList.FLD_BANK_ACC2] + " = '" + rst.getString(24) +
                            "', " + PstContactList.fieldNames[PstContactList.FLD_CONTACT_TYPE] + " = " + rst.getInt(25) +
                            ", " + PstContactList.fieldNames[PstContactList.FLD_EMAIL] + " = '" + rst.getString(26) +
                            "', " + PstContactList.fieldNames[PstContactList.FLD_PARENT_ID] + " = " + rst.getString(27) +
                            " WHERE " + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + " = " + oidContactList;
                }
                //System.out.println("MANAGE CONTACT LIST SQL : "+sql_x);
                res = DBHandler.execUpdate(sql_x);
            }
            hasil = true;
        } catch (Exception exc) {
            System.out.println("Error transfer contact list in : " + exc);
        }
        return hasil;
    }

    //Transfer ContactClassAssign
    private static boolean ProcessContactClassAssignIn(Connection koneksi) {
        boolean hasil = false;
        try {
            int res = 0;
            //Fetch contact class assign
            String sql = " SELECT CONTACT_CLASS_ID" +
                    ", CONTACT_ID" +
                    " FROM " + TBL_TRANSFER_CONTACT_CLASS_ASSIGN +
                    " ORDER BY CONTACT_ID";

            //System.out.println("CONTACT CLASS ASSIGN SQL : "+sql);
            Statement stmt = koneksi.createStatement();
            ResultSet rst = stmt.executeQuery(sql);
            while (rst.next()) {
                String sql_x = "";
                long oidContactClass = Long.parseLong(rst.getString(1));
                long oidContact = Long.parseLong(rst.getString(2));
                //Cek OID, jika belum ada insert, jika tidak update
                boolean exist = PstContactClassAssign.checkOID(oidContactClass, oidContact);
                if (exist == false) {
                    //Special treatment because Many To Many
                    sql_x = "INSERT INTO " + PstContactClassAssign.TBL_CNT_CLS_ASSIGN +
                            " (" + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CNT_CLS_ID] +
                            ", " + PstContactClassAssign.fieldNames[PstContactClassAssign.FLD_CONTACT_ID] +
                            ") VALUES " +
                            "(" + oidContactClass +
                            "," + oidContact +
                            ")";
                    //System.out.println("MANAGE CONTACT CLASS ASSIGN SQL : "+sql_x);
                    res = DBHandler.execUpdate(sql_x);
                }
            }
            hasil = true;
        } catch (Exception exc) {
            System.out.println("Error transfer contact class assign in : " + exc);
        }
        return hasil;
    }

    public static Connection openTransferOut() {
        Connection koneksi = null;
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            koneksi = DriverManager.getConnection("jdbc:odbc:MasterOUT");
        } catch (Exception exc) {
            System.out.println("Error open connection : " + exc);
        }
        return koneksi;
    }

    //Tutup local connection untuk transfer data transaksi keluar
    public static void closeTransferOut(Connection koneksi) {
        try {
            koneksi.close();
        } catch (Exception exc) {
            System.out.println("Error close connection : " + exc);
        }
    }

    //Open local connection untuk transfer data transaksi masuk
    public static Connection openTransferIn() {
        Connection koneksi = null;
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            koneksi = DriverManager.getConnection("jdbc:odbc:MasterIN");
        } catch (Exception exc) {
            System.out.println("Error open connection : " + exc);
        }
        return koneksi;
    }

    //Tutup local connection untuk transfer data transaksi masuk
    public static void closeTransferIn(Connection koneksi) {
        try {
            koneksi.close();
        } catch (Exception exc) {
            System.out.println("Error close connection : " + exc);
        }
    }


    public static String fixingString(String inputString) {
        String result = "";
        char petikSatu = '\'';
        for (int i = 0; i < inputString.length(); i++) {
            result = result + inputString.charAt(i);
            if (inputString.charAt(i) == petikSatu) {
                result = result + "'";
            }
        }
        return result;
    }

    /**
     * testing method
     */
    public static void main(String args[]) {
        //boolean result = TransferMasterOut();
        boolean result = TransferMasterIn();

        /*
        Connection koneksi = null;
        try{
            koneksi = openTransferOut();
        }catch(Exception exc){
            System.out.println("Error : " + exc);
        }

        boolean result = ProcessMaterialOut(koneksi);
        System.out.println("result : "+result);

        closeTransferIn(koneksi);
        */

        //System.out.println(fixingString("MEN'S HEALTH"));
    }
}

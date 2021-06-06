/*
 * CashSaleController.java
 *
 * Created on December 8, 2004, 3:57 PM
 */

package com.dimata.pos.cashier;

import com.dimata.ObjLink.BOCashier.CustomerLink;
import com.dimata.common.entity.logger.DocLogger;
import com.dimata.common.entity.logger.PstDocLogger;
import com.dimata.common.entity.payment.*;
import com.dimata.common.entity.location.*;
import com.dimata.interfaces.BOCashier.I_Guest;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.pos.entity.billing.*;
import com.dimata.pos.ftpapi.CashierFtpConstant;
import com.dimata.posbo.entity.admin.AppUser;
import com.dimata.posbo.entity.admin.PstAppUser;
import com.dimata.posbo.entity.admin.PstMappingUserGroup;
import com.dimata.posbo.entity.masterdata.*;
import com.dimata.posbo.entity.warehouse.MaterialStockCode;
import com.dimata.posbo.entity.warehouse.PstMaterialStockCode;

import java.awt.Component;
import java.awt.Point;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.JOptionPane;


/**
 *
 * @author  wpradnyana
 * @edited wpulantara
 */
public class CashSaleController extends DBHandler {

    private static final int PRE_AND_SUF = 0;
    private static final int PRE_ONLY = 1;
    private static final int SUF_ONLY = 2;
    private static int searchMethode = CashierMainApp.getDSJ_CashierXML().getConfig(0).searchMethode;
    private static String prefix = (searchMethode == SUF_ONLY ? "" : "%");
    private static String sufix = (searchMethode == PRE_ONLY ? "" : "%");
    private static double cardCost = 0;

    private BillMain billMain = null;

    public static String getFullNameOfMaterial(long lOid) {
        String fullName = "";
        Material nameMaterial = new Material();
        Category nameCategory = new Category();
        Merk nameMerk = new Merk();
        SubCategory nameSubCategory = new SubCategory();
        try {
            nameMaterial = PstMaterial.fetchExc(lOid);

        } catch (Exception dbe) {
        }
        if (nameMaterial.getOID() > 0) {
            try {
                nameCategory = PstCategory.fetchExc(nameMaterial.getCategoryId());
            } catch (Exception dbe) {
            }
            try {
                nameMerk = PstMerk.fetchExc(nameMaterial.getMerkId());
            } catch (Exception dbe) {
            }

            fullName = nameCategory.getName().toLowerCase() + " " + nameMerk.getName().toLowerCase() + " " + nameMaterial.getName().toLowerCase() + " ";
        }
        fullName = nameCategory.getName() + " " + nameMerk.getName() + " " + nameMaterial.getName() + " ";
        //System.out.println(fullName);
        return fullName;
    }

    public static Hashtable getHashSellingRatePerCurrency() {
        Hashtable hashRate = new Hashtable();
        Hashtable temp = CashierMainApp.getHashCurrencyId();
        Enumeration e = temp.elements();
        while (e.hasMoreElements()) {
            CurrencyType type = (CurrencyType) e.nextElement();
            StandartRate rate = CashSaleController.getLatestRate(String.valueOf(type.getOID()));
            if (rate == null) {
                rate = new StandartRate();
            }

            hashRate.put(new Long(type.getOID()), rate);
        }
        return hashRate;
    }

    public static BillMain getSaleInstance() {

        BillMain billMain = new BillMain();
        return billMain;
    }

    public static void removeSaleInstance() {

    }

    /** Creates a new instance of CashSaleController */
    public CashSaleController() {
        searchMethode = CashierMainApp.getDSJ_CashierXML().getConfig(0).searchMethode;
        //System.out.println("SEARCH METHODE IN CONTROLLER IS "+searchMethode);
        switch (searchMethode) {
            case PRE_AND_SUF:
                prefix = "%";
                sufix = "%";
                break;
            case PRE_ONLY:
                prefix = "%";
                sufix = "";
                break;
            case SUF_ONLY:
                prefix = "";
                sufix = "%";
                break;
        }

    }

    public Vector getCurrency() {

        Vector result = new Vector();

        return result;
    }

    public Vector getMaster(long cashMasterId) {

        Vector result = new Vector();

        return result;
    }

    public Vector getEmployee(String loginName, String password, int tipe) {


        Vector result = new Vector();

        return result;
    }

    public Vector getObjSales() {


        Vector result = new Vector();

        return result;
    }

    public Vector getObjShift() {


        Vector result = new Vector();

        return result;
    }

    public Vector getObjUnit() {

        Vector result = new Vector();

        return result;
    }

    public Vector getProduct(String code) {

        Vector result = new Vector();

        return result;
    }

    public Vector searchingByCode(String key) {


        Vector result = new Vector();

        return result;
    }

    public Vector searchingByName(String key) {


        Vector result = new Vector();

        return result;
    }

    public static Vector getOpenBill(int start, int recordToGet, String openBillCode, String memberName) {


        Vector result = new Vector(1, 1);
        result.add("");


        return result;
    }

    public static Hashtable getMemberPersonalDiscount(MemberReg member) {
        Hashtable hashPersDisc = new Hashtable();
        String whereClause = PstPersonalDiscount.fieldNames[PstPersonalDiscount.FLD_CONTACT_ID] + "=" + member.getOID() + "";
        Vector vctPersDisc = PstPersonalDiscount.list(0, 1000, whereClause, "");
        int size = vctPersDisc.size();
        for (int i = 0; i < size; i++) {
            PersonalDiscount persDisc = (PersonalDiscount) vctPersDisc.get(i);
            hashPersDisc.put(new Long(persDisc.getMaterialId()), persDisc);
        }
        return hashPersDisc;
    }

    public static Vector getMember(int start, int recordToGet, String memberCode, String memberName) {

        //PstMemberReg pstMemberReg = new PstMemberReg();
        String sqlMemberSearch = "";
        String nameSql = "";
        String codeSql = "";
        if (memberName.length() > 0) {
            nameSql = "( " + PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME] + " = '" + memberName + "' OR " +
                    PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_LASTNAME] + " ='" + memberName + "' )";
        }
        if (memberCode.length() > 0) {
            codeSql = PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_BARCODE] + " ='" + memberCode + "' ";
        }
        if (nameSql.length() > 0) {
            sqlMemberSearch = nameSql;
        }
        if (codeSql.length() > 0) {
            if (nameSql.length() > 0) {
                sqlMemberSearch = sqlMemberSearch + " AND " + codeSql;
            } else {
                sqlMemberSearch = codeSql;
            }
        }
        Vector result = new Vector();
        System.out.println(sqlMemberSearch);
        result = PstMemberReg.list(start, recordToGet, sqlMemberSearch, PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME]);

        return result;
    }

    public static Vector getMemberWithValidExp(int start, int recordToGet, String memberCode, String memberName) {

        String sql = "";
        String sqlMemberSearch = "";
        String nameSql = "";
        String codeSql = "";
        String nonMemberCode = CashierMainApp.getDSJ_CashierXML().getConfig(0).nonMemberCode;
        sql = "SELECT " +
                " MEMBER." + PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_ID] +
                " , MEMBER." + PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME] +
                " , MEMBER." + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_BARCODE] +
                " , REGIST." + PstMemberRegistrationHistory.fieldNames[PstMemberRegistrationHistory.FLD_MEMBER_REGISTRATION_HISTORY_ID] +
                " , REGIST." + PstMemberRegistrationHistory.fieldNames[PstMemberRegistrationHistory.FLD_VALID_EXPIRED_DATE] +
                " FROM " + PstMemberReg.TBL_CONTACT_LIST + " as MEMBER ," + PstMemberRegistrationHistory.TBL_MEMBER_REGISTRATION_HISTORY + " as REGIST " +
                " WHERE " + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_BARCODE] + " <> " + nonMemberCode +
                " AND " + PstMemberReg.fieldNames[PstMemberReg.FLD_PROCESS_STATUS] + " <> " + PstMemberReg.DELETE +
                " AND ";


        if (memberName.length() > 0) {
            nameSql = PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME] + " like '" + prefix + memberName + sufix + "' OR " +
                    PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_LASTNAME] + " like '" + prefix + memberName + sufix + "' ";
        }
        if (memberCode.length() > 0) {
            codeSql = PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_BARCODE] + " like '" + prefix + memberCode + sufix + "' ";
        }
        if (nameSql.length() > 0) {
            sqlMemberSearch = nameSql;
        }
        if (codeSql.length() > 0) {
            if (nameSql.length() > 0) {
                sqlMemberSearch = sqlMemberSearch + " OR " + codeSql;
            } else {
                sqlMemberSearch = codeSql;
            }
        }

        if (sqlMemberSearch != "") {
            sql = sql + " ( " + sqlMemberSearch + " ) AND ";
        }
        sql = sql + " MEMBER." + PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_ID] + "= REGIST." + PstMemberRegistrationHistory.fieldNames[PstMemberRegistrationHistory.FLD_MEMBER_ID];

        String orderClause = " GROUP BY MEMBER." + PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_ID] + " ORDER BY REGIST." + PstMemberRegistrationHistory.fieldNames[PstMemberRegistrationHistory.FLD_VALID_EXPIRED_DATE] + " DESC ";
        sql = sql + orderClause;

        System.out.println(sql);
        //result = PstMemberReg.list(start,recordToGet,sqlMemberSearch,PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME]);
        Vector result = new Vector(1, 1);
        if (recordToGet != 0) {
            sql = sql + " LIMIT " + start + "," + recordToGet;
        }
        DBResultSet dbrs = null;
        try {
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector temp = new Vector(1, 1);
                MemberReg member = new MemberReg();
                member.setOID(rs.getLong(PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_ID]));
                member.setPersonName(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME]));
                member.setMemberBarcode(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_BARCODE]));
                temp.add(member);
                MemberRegistrationHistory regist = new MemberRegistrationHistory();
                regist.setOID(rs.getLong(PstMemberRegistrationHistory.fieldNames[PstMemberRegistrationHistory.FLD_MEMBER_REGISTRATION_HISTORY_ID]));
                regist.setValidExpiredDate(rs.getDate(PstMemberRegistrationHistory.fieldNames[PstMemberRegistrationHistory.FLD_VALID_EXPIRED_DATE]));
                temp.add(regist);
                result.add(temp);

            }

        } catch (Exception e) {

            System.out.println("err di search member : " + e.toString());
            e.printStackTrace();
        } finally {
            DBResultSet.close(dbrs);
        }


        return result;

    }

    public static int getCountMemberWithValidExp(int start, int recordToGet, String memberCode, String memberName) {


        String sql = "";
        String sqlMemberSearch = "";
        String nameSql = "";
        String codeSql = "";
        String nonMemberCode = CashierMainApp.getDSJ_CashierXML().getConfig(0).nonMemberCode;
        sql = "SELECT " +
                " COUNT(MEMBER." + PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_ID] + ")" +
                " FROM " + PstMemberReg.TBL_CONTACT_LIST + " as MEMBER ," + PstMemberRegistrationHistory.TBL_MEMBER_REGISTRATION_HISTORY + " as REGIST " +
                " WHERE " + PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_BARCODE] + " <> " + nonMemberCode +
                " AND " + PstMemberReg.fieldNames[PstMemberReg.FLD_PROCESS_STATUS] + " <> " + PstMemberReg.DELETE +
                " AND ";

        int countResult = 0;
        if (memberName.length() > 0) {
            nameSql = PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME] + " like '" + prefix + memberName + sufix + "' OR " +
                    PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_LASTNAME] + " like '" + prefix + memberName + sufix + "' ";
        }
        if (memberCode.length() > 0) {
            codeSql = PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_BARCODE] + " like '" + prefix + memberCode + sufix + "' ";
        }
        if (nameSql.length() > 0) {
            sqlMemberSearch = nameSql;
        }
        if (codeSql.length() > 0) {
            if (nameSql.length() > 0) {
                sqlMemberSearch = sqlMemberSearch + " OR " + codeSql;
            } else {
                sqlMemberSearch = codeSql;
            }
        }

        if (sqlMemberSearch != "") {
            sql = sql + " ( " + sqlMemberSearch + " ) AND ";
        }
        sql = sql + " MEMBER." + PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_ID] + "= REGIST." + PstMemberRegistrationHistory.fieldNames[PstMemberRegistrationHistory.FLD_MEMBER_ID];

        String orderClause = " GROUP BY MEMBER." + PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_ID] + " ORDER BY REGIST." + PstMemberRegistrationHistory.fieldNames[PstMemberRegistrationHistory.FLD_VALID_EXPIRED_DATE] + " DESC ";
        sql = sql + orderClause;

        System.out.println(sql);
        //result = PstMemberReg.list(start,recordToGet,sqlMemberSearch,PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME]);
        Vector result = new Vector(1, 1);
        if (recordToGet != 0) {
            sql = sql + " LIMIT " + start + "," + recordToGet;
        }
        DBResultSet dbrs = null;
        try {
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector temp = new Vector(1, 1);
                //int resCount = rs.getInt (1);

                countResult = countResult + 1;


            }
        } catch (Exception e) {

            System.out.println("err di search member : " + e.toString());
            e.printStackTrace();
        } finally {
            DBResultSet.close(dbrs);
        }

        return countResult;

    }

    public static Vector getReturnedItemList(BillMain billMain) {
        Vector returnedItemList = new Vector();
        Hashtable itemDetail = new Hashtable();
        Hashtable itemDetailCode = new Hashtable();
        returnedItemList.add(itemDetail);
        returnedItemList.add(itemDetailCode);
        return returnedItemList;
    }


    public static Vector getPendingOrder(int start, int recordToGet, String pendingOrderCode, String boxOrderCode) {


        Vector result = new Vector(1, 1);
        String sqlPendOrdSearch = "";
        String poCode = "";
        String boxNum = "";

        if (pendingOrderCode.length() > 0) {
            poCode = "cash_pending_order." + PstPendingOrder.fieldNames[PstPendingOrder.FLD_PO_NUMBER] + " ='" + pendingOrderCode + "'";

        }
        if (boxOrderCode.length() > 0) {
            boxNum = "cash_pending_order." + PstPendingOrder.fieldNames[PstPendingOrder.FLD_ORDER_NUMBER] + " ='" + boxOrderCode + "'";
        }
        if (poCode.length() > 0) {
            sqlPendOrdSearch = poCode;
        }
        if (boxNum.length() > 0) {
            if (poCode.length() > 0) {
                sqlPendOrdSearch = sqlPendOrdSearch + " OR " + boxNum;
            } else {
                sqlPendOrdSearch = boxNum;
            }
        }

        String sql = "SELECT " +
                "cash_pending_order_id " +
                "FROM " +
                "cash_pending_order " +
                "where " +
                "not exists " +
                "(" +
                "select * " +
                "from " +
                "cash_bill_main " +
                "where " +
                "cash_pending_order.cash_pending_order_id=cash_bill_main.cash_pending_order_id )";
        if (sqlPendOrdSearch.length() > 0) {
            sql = sql + " AND " + sqlPendOrdSearch;
        }
        if (recordToGet != 0) {
            sql = sql + " LIMIT " + start + "," + recordToGet;
        }
        DBResultSet dbrs = null;
        Vector tempResult = new Vector();
        try {
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                long pendOId = rs.getLong(1);
                PendingOrder temp = PstPendingOrder.fetchExc(pendOId);
                tempResult.add(temp);
            }
        } catch (Exception e) {

        } finally {
            DBResultSet.close(dbrs);
        }
        result = tempResult;


        return result;
    }

    /**
     * the new one by wpulantara
     * single access to db more efficient and handle searching by custName
     * updated @ 4th May 2005
     * single table access
     */
    public static Vector getPendingOrder(int start, int recordToGet, String pendingOrderCode, String boxOrderCode, String custName) {

        Vector result = new Vector(1, 1);
        String sqlPendOrdSearch = "";
        String poCode = "";
        String boxNum = "";
        String cName = "";

        if (pendingOrderCode.length() > 0) {
            poCode = "PO." + PstPendingOrder.fieldNames[PstPendingOrder.FLD_PO_NUMBER] + " ='" + pendingOrderCode + "'";
            sqlPendOrdSearch = poCode;
        }

        if (boxOrderCode.length() > 0) {
            boxNum = "PO." + PstPendingOrder.fieldNames[PstPendingOrder.FLD_ORDER_NUMBER] + " ='" + boxOrderCode + "'";
            if (sqlPendOrdSearch.length() > 0) {
                sqlPendOrdSearch = sqlPendOrdSearch + " OR " + boxNum;
            } else {
                sqlPendOrdSearch = boxNum;
            }
        }

        if (custName.length() > 0) {
            cName = "PO." + PstPendingOrder.fieldNames[PstPendingOrder.FLD_NAME] + " LIKE '" + prefix + custName + sufix + "'";
            if (sqlPendOrdSearch.length() > 0) {
                sqlPendOrdSearch = sqlPendOrdSearch + " OR " + cName;
            } else {
                sqlPendOrdSearch = cName;
            }
        }

        if (sqlPendOrdSearch.length() > 0)
            sqlPendOrdSearch = "(" + sqlPendOrdSearch + ")";

        String sql = " SELECT PO.* " +
                " FROM " + PstPendingOrder.TBL_CASH_PENDING_ORDER + " AS PO " +
                " WHERE " + sqlPendOrdSearch + " ";
        if (sqlPendOrdSearch.length() > 0) {
            sql = sql + " AND PO." + PstPendingOrder.fieldNames[PstPendingOrder.FLD_PENDING_ORDER_STATUS] +
                    " = " + PstPendingOrder.STATUS_OPEN + " ";
        } else {
            sql = sql + " PO." + PstPendingOrder.fieldNames[PstPendingOrder.FLD_PENDING_ORDER_STATUS] +
                    " = " + PstPendingOrder.STATUS_OPEN + " ";
        }
        if (recordToGet != 0) {
            sql = sql + " LIMIT " + start + "," + recordToGet;
        }
        System.out.println("sql PO : " + sql);
        DBResultSet dbrs = null;
        Vector tempResult = new Vector();
        try {
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                PendingOrder temp = new PendingOrder();
                PstPendingOrder.resultToObject(rs, temp);
                tempResult.add(temp);
            }
        } catch (Exception e) {

        } finally {
            DBResultSet.close(dbrs);
        }
        result = tempResult;


        return result;
    }

    public static Vector getLastInvoice(int start, int recordToGet, String invoiceCode, String memberCode, String memberName) {


        Vector result = null;
        result.add("");
        return result;
    }

    public static Vector getCreditSales(int start, int recordToGet, String creditCode, String memberCode, String memberName) {

        Vector result = new Vector(1, 1);
        result.add("");
        return result;
    }

    public static Vector getCreditSalePayment(int start, int recordToGet, String creditCode, String memberCode) {
        Vector result = new Vector(1, 1);
        result.add("");
        return result;
    }

    /** query optimized by wpulantara */
    public static Vector getItem(int start, int recordToGet, String itemName, String itemCode) {

        Vector result = new Vector();
        switch (CashierMainApp.getIntegrationWith()) {
            case CashierMainApp.INTEGRATE_WITH_POS:
                {
                    String sql = "SELECT " +
                            " " + PstMaterial.TBL_MATERIAL + ".* " +
                            " " +
                            "FROM " + PstMaterial.TBL_MATERIAL + " " +
                            " where " +
                            " " + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + "!=2 ";
                    String sqlProductSearch = "";
                    String sqlName = "";
                    String sqlCode = "";
                    if (itemName.length() > 0) {
                        StringTokenizer st = new StringTokenizer(itemName);
                        while (st.hasMoreTokens()) {
                            String temp = st.nextToken();
                            sqlName = sqlName + " " +
                                    " " + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " like '%" + temp + "%' " +
                                    " AND";
                        }
                        sqlName = sqlName.substring(0, sqlName.length() - 3);
                    }
                    String sqlSearchCode = "";
                    if (itemCode.length() > 0)
                        sqlCode = " (" + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + "  LIKE '" + prefix + itemCode + sufix + "' OR " +
                                PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '" + prefix + itemCode + sufix + "') ";

                    if (sqlName.length() > 0) {
                        sqlSearchCode = sqlName;
                    }
                    if (sqlCode.length() > 0) {
                        if (sqlName.length() > 0) {
                            sqlSearchCode = sqlSearchCode + " AND " + sqlCode;
                        } else {
                            sqlSearchCode = sqlCode;
                        }
                    }

                    if (sqlSearchCode.length() > 0) {
                        sql = sql + " AND " + sqlSearchCode;
                    }
                    if (recordToGet != 0) {
                        sql = sql + " LIMIT " + start + "," + recordToGet;
                    }
                    DBResultSet dbrs = null;
                    System.out.println(sql);

                    try {
                        dbrs = DBHandler.execQueryResult(sql);
                        ResultSet rs = dbrs.getResultSet();
                        while (rs.next()) {
                            Material mat = new Material();
                            PstMaterial.resultToObject(rs, mat);
                            result.add(mat);
                        }

                    } catch (Exception e) {

                    } finally {
                        DBResultSet.close(dbrs);
                    }
                }
                break;
            case CashierMainApp.INTEGRATE_WITH_HANOMAN:
                {

                    String sql = "SELECT " +
                            " " + PstMaterial.TBL_MATERIAL + ".* " +
                            " " +
                            "FROM " + PstMaterial.TBL_MATERIAL + " " +
                            " inner join " + PstMatMappLocation.TBL_POS_MAT_LOCATION + " " +
                            " on " +
                            " " + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + " = " + PstMatMappLocation.TBL_POS_MAT_LOCATION + "." + PstMatMappLocation.fieldNames[PstMatMappLocation.FLD_MATERIAL_ID] + "" +
                            " where " +
                            " " + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + "!=2 " +
                            " and " +
                            " " + PstMatMappLocation.TBL_POS_MAT_LOCATION + "." + PstMatMappLocation.fieldNames[PstMatMappLocation.FLD_LOCATION_ID] + " = " + CashierMainApp.getDSJ_CashierXML().getConfig(0).locationId + " ";

                    String sqlProductSearch = "";
                    String sqlName = "";
                    String sqlCode = "";
                    if (itemName.length() > 0) {
                        StringTokenizer st = new StringTokenizer(itemName);
                        while (st.hasMoreTokens()) {
                            String temp = st.nextToken();
                            sqlName = sqlName + " " +
                                    " " + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " like '%" + temp + "%' " +
                                    " AND";
                        }
                        sqlName = sqlName.substring(0, sqlName.length() - 3);
                    }
                    String sqlSearchCode = "";
                    if (itemCode.length() > 0)
                        sqlCode = " (" + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + "  LIKE '" + prefix + itemCode + sufix + "' OR " +
                                PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '" + prefix + itemCode + sufix + "') ";

                    if (sqlName.length() > 0) {
                        sqlSearchCode = sqlName;
                    }
                    if (sqlCode.length() > 0) {
                        if (sqlName.length() > 0) {
                            sqlSearchCode = sqlSearchCode + " OR " + sqlCode;
                        }
                        sqlSearchCode = sqlCode;
                    }

                    if (sqlSearchCode.length() > 0) {
                        sql = sql + " AND " + sqlSearchCode;
                    }
                    if (recordToGet != 0) {
                        sql = sql + " LIMIT " + start + "," + recordToGet;
                    }
                    DBResultSet dbrs = null;
                    System.out.println(sql);

                    try {
                        dbrs = DBHandler.execQueryResult(sql);
                        ResultSet rs = dbrs.getResultSet();
                        while (rs.next()) {
                            Material mat = new Material();
                            PstMaterial.resultToObject(rs, mat);
                            result.add(mat);
                        }

                    } catch (Exception e) {

                    } finally {
                        DBResultSet.close(dbrs);
                    }
                }
                break;
            default :

                break;
        }

        return result;
    }

    /** get item with price, req current currency used */
    public static Vector getItem(int start, int recordToGet, long oidRange , String itemName, String itemCode, long currId, long priceTypeId) {

        Vector result = new Vector();
        switch (CashierMainApp.getIntegrationWith()) {
            case CashierMainApp.INTEGRATE_WITH_POS:
                {
                    String sql = "SELECT DISTINCT" +
                            " " + PstMaterial.TBL_MATERIAL + ".* " +
                            ", " +
                            " " + PstPriceTypeMapping.TBL_POS_PRICE_TYPE_MAPPING + "." + PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_PRICE] +
                            " " +
                            " FROM " + PstMaterial.TBL_MATERIAL + " " +
                            " INNER JOIN " + PstPriceTypeMapping.TBL_POS_PRICE_TYPE_MAPPING + " " +
                            " ON " + PstPriceTypeMapping.TBL_POS_PRICE_TYPE_MAPPING + "." + PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_MATERIAL_ID] +
                            " = " + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                            " where " +
                            " " + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + "!=2 " +
                            " AND " + PstPriceTypeMapping.TBL_POS_PRICE_TYPE_MAPPING + "." + PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_STANDART_RATE_ID] + " = " + currId +
                            " AND " + PstPriceTypeMapping.TBL_POS_PRICE_TYPE_MAPPING + "." + PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_PRICE_TYPE_ID] + " = " + priceTypeId;
                    String sqlProductSearch = "";
                    String sqlName = "";
                    String sqlCode = "";

                    if(oidRange!=0){
                        try{
                            CodeRange codeRange = PstCodeRange.fetchExc(oidRange);
                            sql = sql + " AND " + "" + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " between '" + codeRange.getFromRangeCode() + "' AND '"+codeRange.getToRangeCode()+"'";
                        }catch(Exception e){}
                    }

                    if (itemName.length() > 0) {
                        StringTokenizer st = new StringTokenizer(itemName);
                        while (st.hasMoreTokens()) {
                            String temp = st.nextToken();
                            sqlName = sqlName + " " +
                                    " " + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " like '%" + temp + "%' " +
                                    " AND";
                        }
                        sqlName = sqlName.substring(0, sqlName.length() - 3);
                    }
                    String sqlSearchCode = "";
                    if (itemCode.length() > 0) {
                        if (sqlName.length() > 0) {
                            sqlCode = " (" + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + "  = '" + itemCode + "' OR " +
                                    PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " = '" + itemCode + "') ";
                        } else {
                            sqlCode = " (" + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + "  LIKE '" + prefix + itemCode + sufix + "' OR " +
                                    PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '" + prefix + itemCode + sufix + "') ";
                        }
                    }

                    if (sqlName.length() > 0) {
                        sqlSearchCode = sqlName;
                    }
                    if (sqlCode.length() > 0) {
                        if (sqlName.length() > 0) {
                            sqlSearchCode = sqlSearchCode + " AND " + sqlCode;
                        } else {
                            sqlSearchCode = sqlCode;
                        }
                    }

                    if (sqlSearchCode.length() > 0) {
                        sql = sql + " AND " + sqlSearchCode;
                    }


                    if (recordToGet != 0) {
                        sql = sql + " LIMIT " + start + "," + recordToGet;
                    }
                    DBResultSet dbrs = null;
                    System.out.println(sql);

                    try {
                        dbrs = DBHandler.execQueryResult(sql);
                        ResultSet rs = dbrs.getResultSet();
                        while (rs.next()) {
                            Material mat = new Material();
                            PstMaterial.resultToObject(rs, mat);
                            mat.setDefaultPrice(rs.getDouble(PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_PRICE]));
                            result.add(mat);
                        }

                    } catch (Exception e) {

                    } finally {
                        DBResultSet.close(dbrs);
                    }
                }
                break;
            case CashierMainApp.INTEGRATE_WITH_MANUFACTURE:
                {
                    String sql = "SELECT DISTINCT" +
                            " " + PstMaterial.TBL_MATERIAL + ".* " +
                            ", " +
                            " " + PstPriceTypeMapping.TBL_POS_PRICE_TYPE_MAPPING + "." + PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_PRICE] +
                            " " +
                            " FROM " + PstMaterial.TBL_MATERIAL + " " +
                            " INNER JOIN " + PstPriceTypeMapping.TBL_POS_PRICE_TYPE_MAPPING + " " +
                            " ON " + PstPriceTypeMapping.TBL_POS_PRICE_TYPE_MAPPING + "." + PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_MATERIAL_ID] +
                            " = " + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                            " where " +
                            " " + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + "!=2 " +
                            " AND " + PstPriceTypeMapping.TBL_POS_PRICE_TYPE_MAPPING + "." + PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_STANDART_RATE_ID] + " = " + currId +
                            " AND " + PstPriceTypeMapping.TBL_POS_PRICE_TYPE_MAPPING + "." + PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_PRICE_TYPE_ID] + " = " + priceTypeId;
                    String sqlProductSearch = "";
                    String sqlName = "";
                    String sqlCode = "";
                    if (itemName.length() > 0) {
                        StringTokenizer st = new StringTokenizer(itemName);
                        while (st.hasMoreTokens()) {
                            String temp = st.nextToken();
                            sqlName = sqlName + " " +
                                    " " + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " like '%" + temp + "%' " +
                                    " AND";
                        }
                        sqlName = sqlName.substring(0, sqlName.length() - 3);
                    }
                    String sqlSearchCode = "";
                    if (itemCode.length() > 0) {
                        if (sqlName.length() > 0) {
                            sqlCode = " (" + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + "  = '" + itemCode + "' OR " +
                                    PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " = '" + itemCode + "') ";
                        } else {
                            sqlCode = " (" + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + "  LIKE '" + prefix + itemCode + sufix + "' OR " +
                                    PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '" + prefix + itemCode + sufix + "') ";
                        }
                    }

                    if (sqlName.length() > 0) {
                        sqlSearchCode = sqlName;
                    }
                    if (sqlCode.length() > 0) {
                        if (sqlName.length() > 0) {
                            sqlSearchCode = sqlSearchCode + " AND " + sqlCode;
                        } else {
                            sqlSearchCode = sqlCode;
                        }
                    }

                    if (sqlSearchCode.length() > 0) {
                        sql = sql + " AND " + sqlSearchCode;
                    }
                    if (recordToGet != 0) {
                        sql = sql + " LIMIT " + start + "," + recordToGet;
                    }
                    DBResultSet dbrs = null;
                    System.out.println(sql);

                    try {
                        dbrs = DBHandler.execQueryResult(sql);
                        ResultSet rs = dbrs.getResultSet();
                        while (rs.next()) {
                            Material mat = new Material();
                            PstMaterial.resultToObject(rs, mat);
                            mat.setDefaultPrice(rs.getDouble(PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_PRICE]));
                            result.add(mat);
                        }

                    } catch (Exception e) {

                    } finally {
                        DBResultSet.close(dbrs);
                    }
                }
                break;
            case CashierMainApp.INTEGRATE_WITH_HANOMAN:
                {

                    String sql = "SELECT DISTINCT" +
                            " " + PstMaterial.TBL_MATERIAL + ".* " +
                            ", " +
                            " " + PstPriceTypeMapping.TBL_POS_PRICE_TYPE_MAPPING + "." + PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_PRICE] +
                            " " +
                            " FROM " + PstMaterial.TBL_MATERIAL + " " +
                            " INNER JOIN " + PstPriceTypeMapping.TBL_POS_PRICE_TYPE_MAPPING + " " +
                            " ON " + PstPriceTypeMapping.TBL_POS_PRICE_TYPE_MAPPING + "." + PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_MATERIAL_ID] +
                            " = " + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                            " inner join " + PstMatMappLocation.TBL_POS_MAT_LOCATION + " " +
                            " on " +
                            " " + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + " = " + PstMatMappLocation.TBL_POS_MAT_LOCATION + "." + PstMatMappLocation.fieldNames[PstMatMappLocation.FLD_MATERIAL_ID] + "" +
                            " where " +
                            " " + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + "!=2 " +
                            " AND " + PstPriceTypeMapping.TBL_POS_PRICE_TYPE_MAPPING + "." + PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_STANDART_RATE_ID] + " = " + currId +
                            " AND " + PstPriceTypeMapping.TBL_POS_PRICE_TYPE_MAPPING + "." + PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_PRICE_TYPE_ID] + " = " + priceTypeId +
                            " and " +
                            " " + PstMatMappLocation.TBL_POS_MAT_LOCATION + "." + PstMatMappLocation.fieldNames[PstMatMappLocation.FLD_LOCATION_ID] + " = " + CashierMainApp.getDSJ_CashierXML().getConfig(0).locationId + " ";

                    String sqlProductSearch = "";
                    String sqlName = "";
                    String sqlCode = "";
                    if (itemName.length() > 0) {
                        StringTokenizer st = new StringTokenizer(itemName);
                        while (st.hasMoreTokens()) {
                            String temp = st.nextToken();
                            sqlName = sqlName + " " +
                                    " " + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " like '%" + temp + "%' " +
                                    " AND";
                        }
                        sqlName = sqlName.substring(0, sqlName.length() - 3);
                    }
                    String sqlSearchCode = "";
                    if (itemCode.length() > 0)
                        if (sqlName.length() > 0) {
                            sqlCode = " (" + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + "  = '" + itemCode + "' OR " +
                                    PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " = '" + itemCode + "') ";
                        } else {
                            sqlCode = " (" + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + "  LIKE '" + prefix + itemCode + sufix + "' OR " +
                                    PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '" + prefix + itemCode + sufix + "') ";
                        }

                    if (sqlName.length() > 0) {
                        sqlSearchCode = sqlName;
                    }
                    if (sqlCode.length() > 0) {
                        if (sqlName.length() > 0) {
                            sqlSearchCode = sqlSearchCode + " OR " + sqlCode;
                        }
                        sqlSearchCode = sqlCode;
                    }

                    if (sqlSearchCode.length() > 0) {
                        sql = sql + " AND " + sqlSearchCode;
                    }
                    if (recordToGet != 0) {
                        sql = sql + " LIMIT " + start + "," + recordToGet;
                    }
                    DBResultSet dbrs = null;
                    System.out.println(sql);

                    try {
                        dbrs = DBHandler.execQueryResult(sql);
                        ResultSet rs = dbrs.getResultSet();
                        while (rs.next()) {
                            Material mat = new Material();
                            PstMaterial.resultToObject(rs, mat);
                            mat.setDefaultPrice(rs.getDouble(PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_PRICE]));
                            result.add(mat);
                        }

                    } catch (Exception e) {

                    } finally {
                        DBResultSet.close(dbrs);
                    }
                }
                break;
            default :

                break;
        }

        return result;
    }

    private static Point centerPoint;

    public static Point getCenterScreenPoint(Component component) {
        int width = component.getWidth() / 2;
        int height = component.getHeight() / 2;

        //if(centerPoint==null){
        centerPoint = new Point();
        int y = Toolkit.getDefaultToolkit().getScreenSize().height / 2;
        int x = Toolkit.getDefaultToolkit().getScreenSize().width / 2;
        x = x - width;
        if (x < 0) {
            x = 0;
        }
        y = y - height;
        if (y < 0) {
            y = 0;
        }
        centerPoint.setLocation(x, y);
        //}
        return centerPoint;
    }

    public static Vector showMemberSearch(Object parent, String keywordName, String keywordCode) {

        Vector result = new Vector(1, 1);
        if (memberSearchDialog == null) {
            memberSearchDialog = new MemberSearchDialog(CashierMainApp.getMainFrame(), true);
            memberSearchDialog.setModal(true);
            memberSearchDialog.setLocation(getCenterScreenPoint(memberSearchDialog));
        }
        memberSearchDialog.initAllFields();
        memberSearchDialog.setKeyword(keywordName, keywordCode);
        if (keywordName.length() + keywordCode.length() > 0)
            memberSearchDialog.cmdSearch();
        memberSearchDialog.show();

        return result;
    }

    public static Vector showItemSearch(Object parent, String keywordName, String keywordCode) {

        Vector result = new Vector(1, 1);
        if (productSearchDialog == null) {
            productSearchDialog = new ProductSearchDialog(CashierMainApp.getMainFrame(), true);
            productSearchDialog.setModal(true);
            productSearchDialog.setLocation(CashSaleController.getCenterScreenPoint(productSearchDialog));
        }

        productSearchDialog.initAllFields();
        productSearchDialog.setKeyword(keywordName, keywordCode);
        if (keywordName.length() + keywordCode.length() > 0)
            productSearchDialog.cmdSearch(0);
        productSearchDialog.show();

        return result;
    }

    public static Vector showItemSearch(Object parent, String keywordName, String keywordCode, long currId, long priceTypeId) {

        Vector result = new Vector(1, 1);
        if (productSearchDialog == null) {
            productSearchDialog = new ProductSearchDialog(CashierMainApp.getMainFrame(), true);
            productSearchDialog.setModal(true);
            productSearchDialog.setLocation(CashSaleController.getCenterScreenPoint(productSearchDialog));
        }

        productSearchDialog.initAllFields();
        productSearchDialog.setKeyword(keywordName, keywordCode, currId, priceTypeId);
        if (keywordName.length() + keywordCode.length() > 0)
            productSearchDialog.cmdSearch(0);
        productSearchDialog.show();

        return result;
    }

    private static GuestSearchDialog guestSearchDialog;

    public static Vector showGuestSearch(Object parent, String keyword) {

        Vector result = new Vector(1, 1);
        if (guestSearchDialog == null) {
            guestSearchDialog = new GuestSearchDialog(CashierMainApp.getMainFrame(), true);
            guestSearchDialog.setModal(true);
            guestSearchDialog.setLocation(CashSaleController.getCenterScreenPoint(guestSearchDialog));
        }

        guestSearchDialog.show();

        return result;
    }

    public static Vector showPendingOrderSearch(Object parent, String keyword) {


        Vector result = new Vector(1, 1);
        if (pendingOrderSearchDialog == null) {
            pendingOrderSearchDialog = new PendingOrderSearchDialog(null, true);
            pendingOrderSearchDialog.setModal(true);
            pendingOrderSearchDialog.setLocation(CashSaleController.getCenterScreenPoint(pendingOrderSearchDialog));
        }
        pendingOrderSearchDialog.initAllFields();
        pendingOrderSearchDialog.show();

        return result;
    }

    public static Vector showCreditSalesSearch(Object parent, String keyword, String name) {


        if (creditSaleSearchDialog == null) {
            creditSaleSearchDialog = new CreditSalesSearchDialog(null, true);
            creditSaleSearchDialog.setModal(true);
            creditSaleSearchDialog.setLocation(CashSaleController.getCenterScreenPoint(creditSaleSearchDialog));
        }
        creditSaleSearchDialog.initAll();
        creditSaleSearchDialog.setKeyword(keyword, name);
        if ((keyword != null && keyword.length() > 0) || (name != null && name.length() > 0))
            creditSaleSearchDialog.cmdSearch();
        creditSaleSearchDialog.show();
        Vector result = new Vector(1, 1);

        return result;
    }

    public static Vector showInvoiceItemSearch(Object parent, String keyword) {

        Vector result = new Vector(1, 1);
        if (invoiceItemSearchDialog == null) {
            invoiceItemSearchDialog = new InvoiceItemSearchDialog(null, true);
            invoiceItemSearchDialog.setModal(true);
            invoiceItemSearchDialog.setLocation(CashSaleController.getCenterScreenPoint(invoiceItemSearchDialog));
        }
        invoiceItemSearchDialog.setKeyword(keyword);
        invoiceItemSearchDialog.cmdSearch();
        invoiceItemSearchDialog.show();


        return result;
    }

    public static Vector showInvoiceSearch(Object parent, String keyword) {

        Vector result = new Vector(1, 1);
        if (invoiceSearchDialog == null) {
            invoiceSearchDialog = new InvoiceSearchDialog(null, true);
            invoiceSearchDialog.setModal(true);
            invoiceSearchDialog.setLocation(CashSaleController.getCenterScreenPoint(invoiceSearchDialog));
        }
        invoiceSearchDialog.initAll();
        invoiceSearchDialog.setKeyword(keyword);
        if (keyword != null && keyword.length() > 0)
            invoiceSearchDialog.cmdSearch();
        invoiceSearchDialog.show();

        return result;
    }

    public static Vector showOpenBillSearch(Object parent, String keyword) {

        Vector result = new Vector(1, 1);
        if (openBillSearchDialog == null) {
            openBillSearchDialog = new OpenBillSearchDialog(null, true);
            openBillSearchDialog.setModal(true);
            openBillSearchDialog.setLocation(CashSaleController.getCenterScreenPoint(openBillSearchDialog));
        }
        openBillSearchDialog.initAllFields();
        openBillSearchDialog.show();

        return result;
    }

    public static Vector showPaymentDialog(Object parent, Hashtable payments, Hashtable paymentsInfo, double netTrans, StandartRate rateUsed) {


        Vector result = new Vector(1, 1);
        if (paymentDialog == null) {
            paymentDialog = new PaymentDialog(null, true);
            paymentDialog.setModal(true);

            paymentDialog.setLocation(CashSaleController.getCenterScreenPoint(paymentDialog));
        }

        paymentDialog.setPayments(null);
        paymentDialog.setPaymentsInfo(null);
        paymentDialog.setTotCardCost(0);
        paymentDialog.setRateUsed(rateUsed);
        paymentDialog.setNetTrans(netTrans);
        paymentDialog.setPayments(payments);
        paymentDialog.setPaymentsInfo(paymentsInfo);
        paymentDialog.synchronizeTableAndModel();
        paymentDialog.show();
        return result;
    }

    public static Vector showCreditPaymentDialog(Object parent, Hashtable creditPayments, Hashtable creditPaymentsInfo) {


        Vector result = new Vector(1, 1);
        if (creditPaymentDialog == null) {
            creditPaymentDialog = new CreditPaymentDialog(CashierMainApp.getMainFrame(), true);
            creditPaymentDialog.setModal(true);
            creditPaymentDialog.setLocation(CashSaleController.getCenterScreenPoint(creditPaymentDialog));

        }
        creditPaymentDialog.cmdNewTrans();
        creditPaymentDialog.show();
        return result;
    }

    public static void showOtherCostDialog(Hashtable hashOtherCost, double totOtherCost) {
        if (otherCostDialog == null) {
            otherCostDialog = new OtherCostDialog(CashierMainApp.getMainFrame(), true);
            otherCostDialog.setLocation(CashSaleController.getCenterScreenPoint(otherCostDialog));
        }
        otherCostDialog.setHashOtherCost(hashOtherCost);
        otherCostDialog.setVectOtherCost(new Vector(hashOtherCost.keySet()));
        otherCostDialog.setTotOtherCost(totOtherCost);
        otherCostDialog.synchronizeTableAndModel();
        otherCostDialog.cmdReset();
        otherCostDialog.show();
    }

    public static void showContactInputDialog() {
        if (contactInputDialog == null) {
            contactInputDialog = new ContactInputDialog(CashierMainApp.getMainFrame(), true);
            contactInputDialog.setLocation(CashSaleController.getCenterScreenPoint(contactInputDialog));
        }
        contactInputDialog.setCandidateMemberReg(new MemberReg());
        contactInputDialog.loadToFields();
        contactInputDialog.show();
    }

    private static Vector memberChoosen;
    private static Vector productChoosen;
    private static Vector invoiceChoosen;
    private static Vector openBillChoosen;
    private static Vector pendingOrderChoosen;
    private static Hashtable paymentSet;
    private static Vector giftChoosen;
    private static Vector creditSaleChoosen;
    private static Hashtable paymentInfoSet;
    private static Hashtable creditPaymentSet;
    private static Hashtable creditPaymentInfo;
    private static Vector materialChoosen;

    private static Vector billDetailChoosen;
    private static Vector billDetailCodeChoosen;
    private static Vector billMainChoosen;

    private static Hashtable otherCostChoosen;
    private static double totOtherCost;
    private static boolean supervisorApprove;


    public static void setMemberChoosen(Vector member) {


        memberChoosen = member;

    }

    public static Vector getMemberChoosen() {

        if (memberChoosen == null) {
            memberChoosen = new Vector();
        }

        return memberChoosen;
    }

    /**
     * Getter for property productChoosen.
     * @return Value of property productChoosen.
     */
    public static Vector getProductChoosen() {

        if (productChoosen == null) {
            productChoosen = new Vector();
        }

        return productChoosen;
    }

    /**
     * Setter for property productChoosen.
     * @param productChoosen New value of property productChoosen.
     */
    public static void setProductChoosen(Vector aproductChoosen) {

        productChoosen = aproductChoosen;

    }

    /**
     * Getter for property invoiceChoosen.
     * @return Value of property invoiceChoosen.
     */
    public static Vector getInvoiceChoosen() {

        if (invoiceChoosen == null) {
            invoiceChoosen = new Vector();
        }

        return invoiceChoosen;
    }

    /**
     * Setter for property invoiceChoosen.
     * @param invoiceChoosen New value of property invoiceChoosen.
     */
    public static void setInvoiceChoosen(Vector ainvoiceChoosen) {

        invoiceChoosen = ainvoiceChoosen;

    }

    /**
     * Getter for property openBillChoosen.
     * @return Value of property openBillChoosen.
     */
    public static Vector getOpenBillChoosen() {

        if (openBillChoosen == null) {
            openBillChoosen = new Vector();
        }

        return openBillChoosen;
    }

    /**
     * Setter for property openBillChoosen.
     * @param openBillChoosen New value of property openBillChoosen.
     */
    public static void setOpenBillChoosen(Vector aopenBillChoosen) {

        openBillChoosen = aopenBillChoosen;

    }

    /**
     * Getter for property pendingOrderChoosen.
     * @return Value of property pendingOrderChoosen.
     */
    public static Vector getPendingOrderChoosen() {
        if (pendingOrderChoosen == null) {
            pendingOrderChoosen = new Vector();
        }

        return pendingOrderChoosen;
    }

    /**
     * Setter for property pendingOrderChoosen.
     * @param pendingOrderChoosen New value of property pendingOrderChoosen.
     */
    public static void setPendingOrderChoosen(Vector apendingOrderChoosen) {


        pendingOrderChoosen = apendingOrderChoosen;

    }

    /**
     * Getter for property paymentSet.
     * @return Value of property paymentSet.
     */
    public static Hashtable getPaymentSet() {

        if (paymentSet == null) {
            paymentSet = new Hashtable();
        }

        return paymentSet;
    }

    /**
     * Setter for property paymentSet.
     * @param paymentSet New value of property paymentSet.
     */
    public static void setPaymentSet(Hashtable apaymentSet) {

        paymentSet = apaymentSet;

    }

    /**
     * Getter for property giftChoosen.
     * @return Value of property giftChoosen.
     */
    public static Vector getCreditSaleChoosen() {

        if (creditSaleChoosen == null) {
            creditSaleChoosen = new Vector();
        }

        return creditSaleChoosen;
    }

    /**
     * Setter for property giftChoosen.
     * @param giftChoosen New value of property giftChoosen.
     */
    public static void setCreditSaleChoosen(Vector acreditSaleChoosen) {

        creditSaleChoosen = acreditSaleChoosen;

    }

    /**
     * Getter for property giftChoosen.
     * @return Value of property giftChoosen.
     */
    public static Vector getGiftChoosen() {


        if (giftChoosen == null) {
            giftChoosen = new Vector();
        }


        return giftChoosen;
    }

    /**
     * Setter for property giftChoosen.
     * @param giftChoosen New value of property giftChoosen.
     */
    public static void setGiftChoosen(Vector agiftChoosen) {

        giftChoosen = agiftChoosen;

    }

    //private MemberSearch memberSearch = null;
    private static MemberSearchDialog memberSearchDialog;
    //private ProductSearch productSearch = null;
    private static ProductSearchDialog productSearchDialog;
    //private OpenBillSearch openBillSearch = null;
    private static OpenBillSearchDialog openBillSearchDialog;

    //private CreditSalesSearch creditSaleSearch = null;
    private static CreditSalesSearchDialog creditSaleSearchDialog;
    //private PendingOrderSearch pendingOrderSearch = null;
    private static PendingOrderSearchDialog pendingOrderSearchDialog;

    private static InvoiceSearchDialog invoiceSearchDialog;
    //private PaymentFrame paymentFrame = null;
    private static PaymentDialog paymentDialog;

    private static InvoiceItemSearchDialog invoiceItemSearchDialog;

    private static CreditPaymentDialog creditPaymentDialog;

    private static OtherCostDialog otherCostDialog;

    private static ContactInputDialog contactInputDialog;

    private static SupervisorLoginDialog supervisorLoginDialog;

    public static Hashtable getAnyPersonalDisc(long memberOid) {


        Hashtable hashPersonalDisc = null;
        Vector vctPersonalDisc = new Vector();
        String whereClause = PstPersonalDiscount.fieldNames[PstPersonalDiscount.FLD_CONTACT_ID] + " = " + memberOid;
        vctPersonalDisc = PstPersonalDiscount.list(0, 500, whereClause, PstPersonalDiscount.fieldNames[PstPersonalDiscount.FLD_MATERIAL_ID]);
        int size = vctPersonalDisc.size();
        if (size > 0) {
            hashPersonalDisc = new Hashtable();
            for (int i = 0; i < size; i++) {
                PersonalDiscount temp = (PersonalDiscount) vctPersonalDisc.get(i);
                hashPersonalDisc.put(new Long(temp.getMaterialId()), temp);

            }
        }


        return hashPersonalDisc;
    }

    public static PersonalDiscount getPersonalDisount(MemberReg member, Material material) {
        PersonalDiscount persDisc = null;
        String whereClause = PstPersonalDiscount.fieldNames[PstPersonalDiscount.FLD_CONTACT_ID] + "=" + member.getOID() + " AND " + PstPersonalDiscount.fieldNames[PstPersonalDiscount.FLD_MATERIAL_ID] + "=" + material.getOID();
        Vector vctPersDisc = PstPersonalDiscount.list(0, 0, whereClause, "");
        if (vctPersDisc.size() == 1) {
            persDisc = (PersonalDiscount) vctPersDisc.get(0);
        }
        return persDisc;
    }

    public static Billdetail createBillDetailFrom(Material material, MemberReg member, Hashtable personalDisc, StandartRate standartRate, CurrencyType curType) {


        Billdetail newBilldetail = new Billdetail();
        String itemName = "";
        String itemCode = "";
        int discType = 0;
        double discValue = 0;
        double discPct = 0;
        double saleUnit = 0;
        double salePrice = 0;
        String srcDiscMapping = "";
        //srcDiscMapping = "SELECT * FROM POS_DISCOUNT_MAPPING WHERE DISCOUNT_TYPE_ID IN (SELECT * FROM POS_DISCOUNT_TYPE WHERE )";
        Vector vctDiscMapping = new Vector();
        DiscountMapping discMap = CashSaleController.getDiscByMember(material, member, curType);
        if (discMap != null) {
            discValue = discMap.getDiscountValue();
            discPct = discMap.getDiscountPct();
            //discValue = 0;
            //discPct = 0;
        }
        itemName = material.getName();
        //itemName = material.getFullName ();
        PriceTypeMapping priceMap = CashSaleController.getPriceByMember(material, member, standartRate);

        PersonalDiscount persDisc = CashSaleController.getPersonalDisount(member, material); //(PersonalDiscount) personalDisc.get (new Long(material.getOID ()));

        if (persDisc != null) {
            discValue = persDisc.getPersDiscVal();
            discPct = persDisc.getPersDiscPct();
            //JOptionPane.showMessageDialog (null,"You got personal discount about "+discPct+"% and \n "+curType.getCode ()+" "+com.dimata.util.Formater.formatNumber (discValue,CashierMainApp.getDSJ_CashierXML ().getConfig (0).forcurrency)+" "," Personal Discount",JOptionPane.INFORMATION_MESSAGE);

        }

        newBilldetail.setSku(material.getSku());
        //newBilldetail.setItemPrice (priceMap.getPrice ()*standartRate.getSellingRate ());
        newBilldetail.setItemPrice(priceMap.getPrice() * standartRate.getSellingRate());
        //System.out.println("Price "+newBilldetail.getItemPrice() );
        //newBilldetail.setDisc (discValue*standartRate.getSellingRate ());
        newBilldetail.setDisc(discValue);
        newBilldetail.setDiscPct(discPct);
        newBilldetail.setDiscType(PstBillDetail.TYPE_DISC_PCT);
        newBilldetail.setMaterialId(material.getOID());
        newBilldetail.setItemName(itemName);
        newBilldetail.setUnitId(material.getDefaultStockUnitId());
        //newBilldetail.setCost (material.getDefaultCost ());
        newBilldetail.setCost(material.getAveragePrice() * standartRate.getSellingRate());
        newBilldetail.setMaterialType(material.getMaterialType());

        return newBilldetail;
    }

    public static DailyRate getLatestDailyRate(long currencyId) {

        DailyRate dailyRate = null;
        String whereLatestRate = PstDailyRate.fieldNames[PstDailyRate.FLD_CURRENCY_TYPE_ID] + "='" + currencyId + "'";
        String orderLatestRate = PstDailyRate.fieldNames[PstDailyRate.FLD_ROSTER_DATE] + " DESC ";
        Vector vctDailyRate = PstDailyRate.list(0, 0, whereLatestRate, orderLatestRate);
        int size = vctDailyRate.size();
        if (size > 0) {
            dailyRate = (DailyRate) vctDailyRate.get(0);
        } else {
            JOptionPane.showMessageDialog(null, "Plase set daily rate", "Set Latest Rate", JOptionPane.WARNING_MESSAGE);

        }
        return dailyRate;
    }

    public static StandartRate getLatestRate(String currencyId) {


        StandartRate standartRate = null;
        String standWhereLatestRate = PstStandartRate.fieldNames[PstStandartRate.FLD_CURRENCY_TYPE_ID] + "='" + currencyId + "'";
        String standOrderLatestRate = PstStandartRate.fieldNames[PstStandartRate.FLD_END_DATE] + " DESC ";
        Vector vctStandartRate = PstStandartRate.list(0, 0, standWhereLatestRate, standOrderLatestRate);
        int sizeSt = vctStandartRate.size();
        if (sizeSt > 0) {
            standartRate = (StandartRate) vctStandartRate.get(0);
        }

        DailyRate dailyRate = null;
        String whereLatestRate = PstDailyRate.fieldNames[PstDailyRate.FLD_CURRENCY_TYPE_ID] + "='" + currencyId + "'";
        String orderLatestRate = PstDailyRate.fieldNames[PstDailyRate.FLD_ROSTER_DATE] + " DESC ";
        Vector vctDailyRate = PstDailyRate.list(0, 0, whereLatestRate, orderLatestRate);
        int size = vctDailyRate.size();
        //System.out.println(whereLatestRate+" , "+orderLatestRate+" , "+size);
        CurrencyType type = (CurrencyType) CashierMainApp.getHashCurrencyId().get(new Long(currencyId));
        if (size > 0) {
            dailyRate = (DailyRate) vctDailyRate.get(0);

            standartRate.setSellingRate(dailyRate.getSellingRate());
            standartRate.setEndDate(dailyRate.getRosterDate());
            standartRate.setCurrencyTypeId(dailyRate.getCurrencyTypeId());
        } else {
            JOptionPane.showMessageDialog(null, "Plase set latest rate for " + type.getCode(), "Set Latest Rate", JOptionPane.WARNING_MESSAGE);

        }


        return standartRate;
    }

    public static StandartRate getStandartRate(String currencyId) {

        StandartRate standartRate = null;
        String standWhereLatestRate = PstStandartRate.fieldNames[PstStandartRate.FLD_CURRENCY_TYPE_ID] + "='" + currencyId + "'" +
                " AND " + PstStandartRate.fieldNames[PstStandartRate.FLD_STATUS] + "=" + PstStandartRate.ACTIVE;
        String standOrderLatestRate = PstStandartRate.fieldNames[PstStandartRate.FLD_END_DATE] + " DESC ";
        Vector vctStandartRate = PstStandartRate.list(0, 0, standWhereLatestRate, standOrderLatestRate);
        int sizeSt = vctStandartRate.size();
        CurrencyType type = (CurrencyType) CashierMainApp.getHashCurrencyId().get(new Long(currencyId));
        if (sizeSt > 0) {
            standartRate = (StandartRate) vctStandartRate.get(0);
        } else {
            JOptionPane.showMessageDialog(null, "Plase set latest rate for currency " + type.getCode(), "Set Latest Rate", JOptionPane.WARNING_MESSAGE);

        }

        return standartRate;
    }

    public static PriceTypeMapping getPriceByMember(Material material, MemberReg member, StandartRate standartRate) {

        PriceTypeMapping priceMap = null;
        MemberGroup memberGroup = new MemberGroup();
        try {
            memberGroup = PstMemberGroup.fetchExc(member.getMemberGroupId());
        } catch (Exception dbe) {
            dbe.printStackTrace();
        }

        StandartRate latestRate = standartRate;

        String whereClause = PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_MATERIAL_ID] + "=" + material.getOID() + " AND " +
                " " + PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_PRICE_TYPE_ID] + "=" + memberGroup.getPriceTypeId() + " ";
        if (latestRate != null) {
            whereClause = whereClause + " AND " + PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_STANDART_RATE_ID] + "=" + latestRate.getOID() + " ";
        }
        Vector vctPriceMap = PstPriceTypeMapping.list(0, 0, whereClause, "");
        System.out.println(whereClause);
        int size = vctPriceMap.size();
        if (size > 0) {
            try {
                priceMap = (PriceTypeMapping) vctPriceMap.get(0);
            } catch (RuntimeException e) {

                e.printStackTrace();
            }
        } else {
            //JOptionPane.showMessageDialog (null,"Plase set member prices","Set member prices",JOptionPane.WARNING_MESSAGE);
            priceMap = new PriceTypeMapping();
            MemberReg temp = new MemberReg();

            temp = CashSaleController.getCustomerNonMember();


            priceMap.setPrice(0);
        }

        return priceMap;
    }

    public static CurrencyType getCurrencyType(String currencyType) {

        CurrencyType temp = null;
        try {
            temp = PstCurrencyType.fetchExc(Long.parseLong(currencyType));
        } catch (Exception dbe) {

        }

        return temp;
    }

    public static MemberGroup getGroupOfMember(long lOID) {

        MemberGroup memberGroup = null;

        //memberGroup = new MemberGroup();
        String whereClause = PstMemberGroup.fieldNames[PstMemberGroup.FLD_MEMBER_GROUP_ID] + " = " + lOID + "";
        int count = PstMemberGroup.getCount(whereClause);
        if (count == 0) {
            lOID = Long.parseLong(CashierMainApp.getDSJ_CashierXML().getConfig(0).nonMemberId);
        }
        try {
            memberGroup = PstMemberGroup.fetchExc(lOID);
        } catch (Exception dbe) {

            dbe.printStackTrace();
        }


        return memberGroup;
    }

    public static DiscountMapping getDiscByMember(Material material, MemberReg member, CurrencyType currType) {


        DiscountMapping discMap = null;

        MemberGroup memberGroup = new MemberGroup();
        try {
            memberGroup = PstMemberGroup.fetchExc(member.getMemberGroupId());
        } catch (Exception dbe) {

            dbe.printStackTrace();
        }
        String whereClause = PstDiscountMapping.fieldNames[PstDiscountMapping.FLD_MATERIAL_ID] + "=" + material.getOID() + " AND " +
                " " + PstDiscountMapping.fieldNames[PstDiscountMapping.FLD_DISCOUNT_TYPE_ID] + "=" + memberGroup.getDiscountTypeId() + " " +
                "   AND " + PstDiscountMapping.fieldNames[PstDiscountMapping.FLD_CURRENCY_TYPE_ID] + "=" + currType.getOID() + " ";
        //whereClause = whereClause + includeCurr_id;
        //System.out.println(whereClause);
        Vector vctDiscMap = PstDiscountMapping.list(0, 0, whereClause, "");
        System.out.println(whereClause);
        int size = vctDiscMap.size();
        if (size == 1) {
            discMap = (DiscountMapping) vctDiscMap.get(0);
            //System.out.println(discMap.getOID());
        } else {
            //JOptionPane.showMessageDialog (null,"Plase set member discount","Set member discount",JOptionPane.WARNING_MESSAGE);
            discMap = new DiscountMapping();
            //MemberReg temp = PstMemberReg.fetchExc (Long.parseLong (CashierMainApp.getDSJ_CashierXML ().getConfig (0).nonMemberId));
            //discMap = CashSaleController.getDiscByMember (material,temp,currType);
            discMap.setDiscountPct(0);
            discMap.setDiscountValue(0);
            discMap.setDiscount(0);
        }

        return discMap;
    }


    /**
     * Getter for property paymentInfoSet.
     * @return Value of property paymentInfoSet.
     */
    public static Hashtable getPaymentInfoSet() {
        return paymentInfoSet;
    }

    /**
     * Setter for property paymentInfoSet.
     * @param paymentInfoSet New value of property paymentInfoSet.
     */
    public static void setPaymentInfoSet(Hashtable aPaymentInfoSet) {
        paymentInfoSet = aPaymentInfoSet;
    }

    /**
     * Getter for property creditPaymentSet.
     * @return Value of property creditPaymentSet.
     */
    public static Hashtable getCreditPaymentSet() {
        return creditPaymentSet;
    }

    /**
     * Setter for property creditPaymentSet.
     * @param creditPaymentSet New value of property creditPaymentSet.
     */
    public static void setCreditPaymentSet(Hashtable acreditPaymentSet) {
        creditPaymentSet = acreditPaymentSet;
    }

    /**
     * Getter for property creditPaymentInfo.
     * @return Value of property creditPaymentInfo.
     */
    public static Hashtable getCreditPaymentInfo() {
        return creditPaymentInfo;
    }

    /**
     * Setter for property creditPaymentInfo.
     * @param creditPaymentInfo New value of property creditPaymentInfo.
     */
    public static void setCreditPaymentInfo(Hashtable acreditPaymentInfo) {
        creditPaymentInfo = acreditPaymentInfo;
    }


    /**
     * added by wpulantara for row count          long currId, long priceTypeId
     * query optimized
     */
    public static int getItemCount(String itemName, String itemCode) {

        int count = 0;
        switch (CashierMainApp.getIntegrationWith()) {
            case CashierMainApp.INTEGRATE_WITH_POS:
                {
                    String sql = "SELECT " +
                            " count(DISTINCT " + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + ") " +

                            " " +
                            "FROM " + PstMaterial.TBL_MATERIAL + " " +
                            " where " +
                            " " + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + "!=2 ";
                    String sqlProductSearch = "";
                    String sqlName = "";
                    String sqlCode = "";
                    if (itemName.length() > 0) {
                        StringTokenizer st = new StringTokenizer(itemName);
                        while (st.hasMoreTokens()) {
                            String temp = st.nextToken();
                            sqlName = sqlName + " " +
                                    " " + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " like '%" + temp + "%' " +
                                    " AND";
                        }
                        sqlName = sqlName.substring(0, sqlName.length() - 3);
                    }
                    String sqlSearchCode = "";
                    if (itemCode.length() > 0) {
                        if (sqlName.length() > 0) {
                            sqlCode = " (" + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + "  = '" + itemCode + "' OR " +
                                    PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " = '" + itemCode + "') ";
                        } else {
                            sqlCode = " (" + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + "  LIKE '" + prefix + itemCode + sufix + "' OR " +
                                    PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '" + prefix + itemCode + sufix + "') ";
                        }
                    }
                    if (sqlName.length() > 0) {
                        sqlSearchCode = sqlName;
                    }
                    if (sqlCode.length() > 0) {
                        if (sqlName.length() > 0) {
                            sqlSearchCode = sqlSearchCode + " AND " + sqlCode;
                        } else {
                            sqlSearchCode = sqlCode;
                        }
                    }
                    if (sqlSearchCode.length() > 0) {
                        sql = sql + " AND " + sqlSearchCode;
                    }

                    DBResultSet dbrs = null;
                    System.out.println(sql);

                    try {
                        dbrs = DBHandler.execQueryResult(sql);
                        ResultSet rs = dbrs.getResultSet();
                        while (rs.next()) {
                            count = rs.getInt(1);
                        }

                    } catch (Exception e) {

                    } finally {
                        DBResultSet.close(dbrs);
                    }
                }
                break;
            case CashierMainApp.INTEGRATE_WITH_MANUFACTURE:
                {
                    String sql = "SELECT " +
                            " count(DISTINCT " + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + ") " +

                            " " +
                            "FROM " + PstMaterial.TBL_MATERIAL + " " +
                            " where " +
                            " " + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + "!=2 ";
                    String sqlProductSearch = "";
                    String sqlName = "";
                    String sqlCode = "";
                    if (itemName.length() > 0) {
                        StringTokenizer st = new StringTokenizer(itemName);
                        while (st.hasMoreTokens()) {
                            String temp = st.nextToken();
                            sqlName = sqlName + " " +
                                    " " + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " like '%" + temp + "%' " +
                                    " AND";
                        }
                        sqlName = sqlName.substring(0, sqlName.length() - 3);
                    }
                    String sqlSearchCode = "";
                    if (itemCode.length() > 0) {
                        if (sqlName.length() > 0) {
                            sqlCode = " (" + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + "  = '" + itemCode + "' OR " +
                                    PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " = '" + itemCode + "') ";
                        } else {
                            sqlCode = " (" + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + "  LIKE '" + prefix + itemCode + sufix + "' OR " +
                                    PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '" + prefix + itemCode + sufix + "') ";
                        }
                    }
                    if (sqlName.length() > 0) {
                        sqlSearchCode = sqlName;
                    }
                    if (sqlCode.length() > 0) {
                        if (sqlName.length() > 0) {
                            sqlSearchCode = sqlSearchCode + " AND " + sqlCode;
                        } else {
                            sqlSearchCode = sqlCode;
                        }
                    }
                    if (sqlSearchCode.length() > 0) {
                        sql = sql + " AND " + sqlSearchCode;
                    }

                    DBResultSet dbrs = null;
                    System.out.println(sql);

                    try {
                        dbrs = DBHandler.execQueryResult(sql);
                        ResultSet rs = dbrs.getResultSet();
                        while (rs.next()) {
                            count = rs.getInt(1);
                        }

                    } catch (Exception e) {

                    } finally {
                        DBResultSet.close(dbrs);
                    }
                }
                break;
            case CashierMainApp.INTEGRATE_WITH_HANOMAN:
                //    case CashierMainApp.INTEGRATE_WITH_POS:
                {
                    String sql = "SELECT " +
                            " count(DISTINCT " + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + ") " +
                            " " +
                            "FROM " + PstMaterial.TBL_MATERIAL + " " +
                            " inner join " + PstMatMappLocation.TBL_POS_MAT_LOCATION + " " +
                            " on " +
                            " " + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + " = " + PstMatMappLocation.TBL_POS_MAT_LOCATION + "." + PstMatMappLocation.fieldNames[PstMatMappLocation.FLD_MATERIAL_ID] + "" +
                            " where " +
                            " " + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + "!=2 " +
                            " and " +
                            " " + PstMatMappLocation.TBL_POS_MAT_LOCATION + "." + PstMatMappLocation.fieldNames[PstMatMappLocation.FLD_LOCATION_ID] + " = " + CashierMainApp.getDSJ_CashierXML().getConfig(0).locationId + " ";

                    String sqlProductSearch = "";
                    String sqlName = "";
                    String sqlCode = "";
                    if (itemName.length() > 0) {
                        StringTokenizer st = new StringTokenizer(itemName);
                        while (st.hasMoreTokens()) {
                            String temp = st.nextToken();
                            sqlName = sqlName + " " +
                                    " " + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " like '%" + temp + "%' " +
                                    " AND";
                        }
                        sqlName = sqlName.substring(0, sqlName.length() - 3);
                    }
                    String sqlSearchCode = "";
                    if (itemCode.length() > 0)
                        if (sqlName.length() > 0) {
                            sqlCode = " (" + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + "  = '" + itemCode + "' OR " +
                                    PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " = '" + itemCode + "') ";
                        } else {
                            sqlCode = " (" + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + "  LIKE '" + prefix + itemCode + sufix + "' OR " +
                                    PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '" + prefix + itemCode + sufix + "') ";
                        }

                    if (sqlName.length() > 0) {
                        sqlSearchCode = sqlName;
                    }
                    if (sqlCode.length() > 0) {
                        if (sqlName.length() > 0) {
                            sqlSearchCode = sqlSearchCode + " OR " + sqlCode;
                        }
                        sqlSearchCode = sqlCode;
                    }

                    if (sqlSearchCode.length() > 0) {
                        sql = sql + " AND " + sqlSearchCode;
                    }

                    DBResultSet dbrs = null;
                    System.out.println(sql);

                    try {
                        dbrs = DBHandler.execQueryResult(sql);
                        ResultSet rs = dbrs.getResultSet();
                        while (rs.next()) {
                            count = rs.getInt(1);

                        }

                    } catch (Exception e) {

                    } finally {
                        DBResultSet.close(dbrs);
                    }
                }
                break;
            default :

                break;
        }

        return count;

    }


    public static int getItemCount(long oidRange, String itemName, String itemCode, long currId, long priceTypeId) {

        int count = 0;
        Vector result = new Vector();
        switch (CashierMainApp.getIntegrationWith()) {
            case CashierMainApp.INTEGRATE_WITH_POS:
                {
                    String sql = "SELECT COUNT(DISTINCT " +
                            " " + PstMaterial.TBL_MATERIAL+"."+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +") AS CNT " +
                            " FROM " + PstMaterial.TBL_MATERIAL + " " +
                            " INNER JOIN " + PstPriceTypeMapping.TBL_POS_PRICE_TYPE_MAPPING + " " +
                            " ON " + PstPriceTypeMapping.TBL_POS_PRICE_TYPE_MAPPING + "." + PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_MATERIAL_ID] +
                            " = " + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                            " where " +
                            " " + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + "!=2 " +
                            " AND " + PstPriceTypeMapping.TBL_POS_PRICE_TYPE_MAPPING + "." + PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_STANDART_RATE_ID] + " = " + currId +
                            " AND " + PstPriceTypeMapping.TBL_POS_PRICE_TYPE_MAPPING + "." + PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_PRICE_TYPE_ID] + " = " + priceTypeId;
                    String sqlProductSearch = "";
                    String sqlName = "";
                    String sqlCode = "";

                    if(oidRange!=0){
                        try{
                            CodeRange codeRange = PstCodeRange.fetchExc(oidRange);
                            sql = sql + " AND " + "" + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " between '" + codeRange.getFromRangeCode() + "' AND '"+codeRange.getToRangeCode()+"'";
                        }catch(Exception e){}
                    }

                    if (itemName.length() > 0) {
                        StringTokenizer st = new StringTokenizer(itemName);
                        while (st.hasMoreTokens()) {
                            String temp = st.nextToken();
                            sqlName = sqlName + " " +
                                    " " + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " like '%" + temp + "%' " +
                                    " AND";
                        }
                        sqlName = sqlName.substring(0, sqlName.length() - 3);
                    }
                    String sqlSearchCode = "";
                    if (itemCode.length() > 0) {
                        if (sqlName.length() > 0) {
                            sqlCode = " (" + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + "  = '" + itemCode + "' OR " +
                                    PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " = '" + itemCode + "') ";
                        } else {
                            sqlCode = " (" + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + "  LIKE '" + prefix + itemCode + sufix + "' OR " +
                                    PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '" + prefix + itemCode + sufix + "') ";
                        }
                    }

                    if (sqlName.length() > 0) {
                        sqlSearchCode = sqlName;
                    }
                    if (sqlCode.length() > 0) {
                        if (sqlName.length() > 0) {
                            sqlSearchCode = sqlSearchCode + " AND " + sqlCode;
                        } else {
                            sqlSearchCode = sqlCode;
                        }
                    }

                    if (sqlSearchCode.length() > 0) {
                        sql = sql + " AND " + sqlSearchCode;
                    }
                    DBResultSet dbrs = null;
                    System.out.println(sql);

                    try {
                        dbrs = DBHandler.execQueryResult(sql);
                        ResultSet rs = dbrs.getResultSet();
                        while (rs.next()) {
                            count = rs.getInt("CNT");
                        }

                    } catch (Exception e) {

                    } finally {
                        DBResultSet.close(dbrs);
                    }
                }
                break;
            case CashierMainApp.INTEGRATE_WITH_MANUFACTURE:
                {
                    String sql = "SELECT DISTINCT" +
                            " " + PstMaterial.TBL_MATERIAL + ".* " +
                            ", " +
                            " " + PstPriceTypeMapping.TBL_POS_PRICE_TYPE_MAPPING + "." + PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_PRICE] +
                            " " +
                            " FROM " + PstMaterial.TBL_MATERIAL + " " +
                            " INNER JOIN " + PstPriceTypeMapping.TBL_POS_PRICE_TYPE_MAPPING + " " +
                            " ON " + PstPriceTypeMapping.TBL_POS_PRICE_TYPE_MAPPING + "." + PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_MATERIAL_ID] +
                            " = " + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                            " where " +
                            " " + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + "!=2 " +
                            " AND " + PstPriceTypeMapping.TBL_POS_PRICE_TYPE_MAPPING + "." + PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_STANDART_RATE_ID] + " = " + currId +
                            " AND " + PstPriceTypeMapping.TBL_POS_PRICE_TYPE_MAPPING + "." + PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_PRICE_TYPE_ID] + " = " + priceTypeId;
                    String sqlProductSearch = "";
                    String sqlName = "";
                    String sqlCode = "";
                    if (itemName.length() > 0) {
                        StringTokenizer st = new StringTokenizer(itemName);
                        while (st.hasMoreTokens()) {
                            String temp = st.nextToken();
                            sqlName = sqlName + " " +
                                    " " + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " like '%" + temp + "%' " +
                                    " AND";
                        }
                        sqlName = sqlName.substring(0, sqlName.length() - 3);
                    }
                    String sqlSearchCode = "";
                    if (itemCode.length() > 0) {
                        if (sqlName.length() > 0) {
                            sqlCode = " (" + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + "  = '" + itemCode + "' OR " +
                                    PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " = '" + itemCode + "') ";
                        } else {
                            sqlCode = " (" + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + "  LIKE '" + prefix + itemCode + sufix + "' OR " +
                                    PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '" + prefix + itemCode + sufix + "') ";
                        }
                    }

                    if (sqlName.length() > 0) {
                        sqlSearchCode = sqlName;
                    }
                    if (sqlCode.length() > 0) {
                        if (sqlName.length() > 0) {
                            sqlSearchCode = sqlSearchCode + " AND " + sqlCode;
                        } else {
                            sqlSearchCode = sqlCode;
                        }
                    }

                    if (sqlSearchCode.length() > 0) {
                        sql = sql + " AND " + sqlSearchCode;
                    }
                    DBResultSet dbrs = null;
                    System.out.println(sql);

                    try {
                        dbrs = DBHandler.execQueryResult(sql);
                        ResultSet rs = dbrs.getResultSet();
                        while (rs.next()) {
                            Material mat = new Material();
                            PstMaterial.resultToObject(rs, mat);
                            mat.setDefaultPrice(rs.getDouble(PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_PRICE]));
                            result.add(mat);
                        }

                    } catch (Exception e) {

                    } finally {
                        DBResultSet.close(dbrs);
                    }
                }
                break;
            case CashierMainApp.INTEGRATE_WITH_HANOMAN:
                {

                    String sql = "SELECT DISTINCT" +
                            " " + PstMaterial.TBL_MATERIAL + ".* " +
                            ", " +
                            " " + PstPriceTypeMapping.TBL_POS_PRICE_TYPE_MAPPING + "." + PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_PRICE] +
                            " " +
                            " FROM " + PstMaterial.TBL_MATERIAL + " " +
                            " INNER JOIN " + PstPriceTypeMapping.TBL_POS_PRICE_TYPE_MAPPING + " " +
                            " ON " + PstPriceTypeMapping.TBL_POS_PRICE_TYPE_MAPPING + "." + PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_MATERIAL_ID] +
                            " = " + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                            " inner join " + PstMatMappLocation.TBL_POS_MAT_LOCATION + " " +
                            " on " +
                            " " + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + " = " + PstMatMappLocation.TBL_POS_MAT_LOCATION + "." + PstMatMappLocation.fieldNames[PstMatMappLocation.FLD_MATERIAL_ID] + "" +
                            " where " +
                            " " + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + "!=2 " +
                            " AND " + PstPriceTypeMapping.TBL_POS_PRICE_TYPE_MAPPING + "." + PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_STANDART_RATE_ID] + " = " + currId +
                            " AND " + PstPriceTypeMapping.TBL_POS_PRICE_TYPE_MAPPING + "." + PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_PRICE_TYPE_ID] + " = " + priceTypeId +
                            " and " +
                            " " + PstMatMappLocation.TBL_POS_MAT_LOCATION + "." + PstMatMappLocation.fieldNames[PstMatMappLocation.FLD_LOCATION_ID] + " = " + CashierMainApp.getDSJ_CashierXML().getConfig(0).locationId + " ";

                    String sqlProductSearch = "";
                    String sqlName = "";
                    String sqlCode = "";
                    if (itemName.length() > 0) {
                        StringTokenizer st = new StringTokenizer(itemName);
                        while (st.hasMoreTokens()) {
                            String temp = st.nextToken();
                            sqlName = sqlName + " " +
                                    " " + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " like '%" + temp + "%' " +
                                    " AND";
                        }
                        sqlName = sqlName.substring(0, sqlName.length() - 3);
                    }
                    String sqlSearchCode = "";
                    if (itemCode.length() > 0)
                        if (sqlName.length() > 0) {
                            sqlCode = " (" + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + "  = '" + itemCode + "' OR " +
                                    PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " = '" + itemCode + "') ";
                        } else {
                            sqlCode = " (" + PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + "  LIKE '" + prefix + itemCode + sufix + "' OR " +
                                    PstMaterial.TBL_MATERIAL + "." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '" + prefix + itemCode + sufix + "') ";
                        }

                    if (sqlName.length() > 0) {
                        sqlSearchCode = sqlName;
                    }
                    if (sqlCode.length() > 0) {
                        if (sqlName.length() > 0) {
                            sqlSearchCode = sqlSearchCode + " OR " + sqlCode;
                        }
                        sqlSearchCode = sqlCode;
                    }

                    if (sqlSearchCode.length() > 0) {
                        sql = sql + " AND " + sqlSearchCode;
                    }
                    DBResultSet dbrs = null;
                    System.out.println(sql);

                    try {
                        dbrs = DBHandler.execQueryResult(sql);
                        ResultSet rs = dbrs.getResultSet();
                        while (rs.next()) {
                            Material mat = new Material();
                            PstMaterial.resultToObject(rs, mat);
                            mat.setDefaultPrice(rs.getDouble(PstPriceTypeMapping.fieldNames[PstPriceTypeMapping.FLD_PRICE]));
                            result.add(mat);
                        }

                    } catch (Exception e) {

                    } finally {
                        DBResultSet.close(dbrs);
                    }
                }
                break;
            default :

                break;
        }

        return count;

    }

    public static int getPendingOrderCount(String pendingOrderNum, String boxCode) {


        String sqlProductSearch = "";
        String sqlName = "";
        String sqlCode = "";

        int result = 0;
        try {
            result = CashSaleController.getPendingOrder(0, 0, pendingOrderNum, boxCode).size();
        } catch (RuntimeException e) {

            e.printStackTrace();
        }

        return result;
    }

    /**
     * new one for handle custName based search
     * more effisien with COUNT(*) query to database
     * instead counting the size of result vector
     */
    public static int getPendingOrderCount(String pendingOrderNum, String boxCode, String custName) {

        int result = 0;
        String sqlPendOrdSearch = "";
        String poCode = "";
        String boxNum = "";
        String cName = "";

        if (pendingOrderNum.length() > 0) {
            poCode = "PO." + PstPendingOrder.fieldNames[PstPendingOrder.FLD_PO_NUMBER] + " ='" + pendingOrderNum + "'";
            sqlPendOrdSearch = poCode;
        }

        if (boxCode.length() > 0) {
            boxNum = "PO." + PstPendingOrder.fieldNames[PstPendingOrder.FLD_ORDER_NUMBER] + " ='" + boxCode + "'";
            if (sqlPendOrdSearch.length() > 0) {
                sqlPendOrdSearch = sqlPendOrdSearch + " OR " + boxNum;
            } else {
                sqlPendOrdSearch = boxNum;
            }
        }

        if (custName.length() > 0) {
            cName = "PO." + PstPendingOrder.fieldNames[PstPendingOrder.FLD_NAME] + " LIKE '" + prefix + custName + sufix + "'";
            if (sqlPendOrdSearch.length() > 0) {
                sqlPendOrdSearch = sqlPendOrdSearch + " OR " + cName;
            } else {
                sqlPendOrdSearch = cName;
            }
        }

        if (sqlPendOrdSearch.length() > 0)
            sqlPendOrdSearch = "(" + sqlPendOrdSearch + ")";

        String sql = " SELECT COUNT(PO." + PstPendingOrder.fieldNames[PstPendingOrder.FLD_CASH_PENDING_ORDER_ID] + ") " +
                " FROM " + PstPendingOrder.TBL_CASH_PENDING_ORDER + " AS PO " +
                " WHERE " + sqlPendOrdSearch + "";
        if (sqlPendOrdSearch.length() > 0) {
            sql = sql + " AND PO." + PstPendingOrder.fieldNames[PstPendingOrder.FLD_PENDING_ORDER_STATUS] +
                    " = " + PstPendingOrder.STATUS_OPEN + " ";
        } else {
            sql = sql + " PO." + PstPendingOrder.fieldNames[PstPendingOrder.FLD_PENDING_ORDER_STATUS] +
                    " = " + PstPendingOrder.STATUS_OPEN + " ";
        }

        System.out.println("sql count PO : " + sql);
        DBResultSet dbrs = null;
        Vector tempResult = new Vector();
        try {
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            if (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (Exception e) {

        } finally {
            DBResultSet.close(dbrs);
        }

        return result;
    }

    /**
     * added by wpulantara for counting the rows
     */
    public static int getMemberCount(String memberCode, String memberName) {

        String sqlMemberSearch = "";
        String nameSql = "";
        String codeSql = "";
        if (memberName.length() > 0) {
            nameSql = PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME] + " like '" + prefix + memberName + sufix + "' OR " +
                    PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_LASTNAME] + " like '" + prefix + memberName + sufix + "' ";
        }
        if (memberCode.length() > 0) {
            codeSql = PstMemberReg.fieldNames[PstMemberReg.FLD_MEMBER_BARCODE] + " like '" + prefix + memberCode + sufix + "' ";
        }
        if (nameSql.length() > 0) {
            sqlMemberSearch = nameSql;
        }
        if (codeSql.length() > 0) {
            if (nameSql.length() > 0) {
                sqlMemberSearch = sqlMemberSearch + " OR " + codeSql;
            } else {
                sqlMemberSearch = codeSql;
            }
        }

        System.out.println(sqlMemberSearch);
        int result = 0;
        try {
            result = PstMemberReg.getCount(sqlMemberSearch);
        } catch (RuntimeException e) {

            e.printStackTrace();
        }


        return result;
    }

    public static Sales getSalesByCode(String salesCode) {

        Sales salesPerson = null;
        String whereClause = PstSales.fieldNames[PstSales.FLD_CODE] + "='" + salesCode + "'";
        try {
            Vector vctSalesPerson = PstSales.list(0, 0, whereClause, "");
            if (vctSalesPerson.size() == 1) {
                salesPerson = (Sales) vctSalesPerson.get(0);
            }
        } catch (Exception e) {

            e.printStackTrace();
        }

        return salesPerson;

    }

    public static AppUser getSalesById(long userId) {

      AppUser salesPerson = new AppUser();
      try {
        salesPerson = PstAppUser.fetch(userId);
        String whereClause = PstMappingUserGroup.fieldNames[PstMappingUserGroup.FLD_USER_ID] + " = " + salesPerson.getOID() + " AND " + PstMappingUserGroup.fieldNames[PstMappingUserGroup.FLD_GROUP_USER_ID] + " = 6";
        Vector sales = PstMappingUserGroup.list(0, 0, whereClause, "");
        if (sales.size() == 1) {
            salesPerson = (AppUser) sales.get(0);
        }
      } catch (Exception e) {
      }
      return salesPerson;
    }

    static MemberReg nonCustomerMember = null;
    static MemberReg customerMember = null;

    public static MemberReg getCustomerMember() {

        if (customerMember == null) {
            String memberCode = CashierMainApp.getDSJ_CashierXML().getConfig(0).memberCode;
            String whereClause = PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_CODE] + "=" + memberCode + "";
            //System.out.println("Member "+memberCode);
            Vector vctMemberReg = PstMemberReg.list(0, 0, whereClause, "");
            if (vctMemberReg != null && vctMemberReg.size() >= 1) {
                MemberReg memberReg = (MemberReg) vctMemberReg.get(0);
                customerMember = memberReg;
            }
        }
        return customerMember;
    }

    public static MemberReg getCustomerNonMember() {

        if (nonCustomerMember == null) {

            //jika konfigurasi menggunakan id
            //nonCustomerMember = PstMemberReg.fetchExc (Long.parseLong (CashierMainApp.getDSJ_CashierXML ().getConfig (0).nonMemberId));
            //jika konfigurasi menggunakan kode
            String nonMemberCode = CashierMainApp.getDSJ_CashierXML().getConfig(0).nonMemberCode;
            String whereClause = PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_CODE] + "='" + nonMemberCode + "'";
            //System.out.println("NonMember "+nonMemberCode);
            Vector vctNonMemberReg = PstMemberReg.list(0, 0, whereClause, "");
            if (vctNonMemberReg != null && vctNonMemberReg.size() >= 1) {
                MemberReg nonMemberReg = (MemberReg) vctNonMemberReg.get(0);
                nonCustomerMember = nonMemberReg;
            }

        }
        return nonCustomerMember;
    }

    /**
     * Getter for property materialChoosen.
     * @return Value of property materialChoosen.
     */
    public static Vector getMaterialChoosen() {
        return materialChoosen;
    }

    /**
     * Setter for property materialChoosen.
     * @param materialChoosen New value of property materialChoosen.
     */
    public static void setMaterialChoosen(Vector argMaterialChoosen) {
        materialChoosen = argMaterialChoosen;
    }

    /**
     * Getter for property billDetailChoosen.
     * @return Value of property billDetailChoosen.
     */
    public static Vector getBillDetailChoosen() {
        return billDetailChoosen;
    }

    /**
     * Setter for property billDetailChoosen.
     * @param billDetailChoosen New value of property billDetailChoosen.
     */
    public static void setBillDetailChoosen(Vector argBillDetailChoosen) {
        billDetailChoosen = argBillDetailChoosen;
    }

    /**
     * Getter for property billDetailCodeChoosen.
     * @return Value of property billDetailCodeChoosen.
     */
    public static Vector getBillDetailCodeChoosen() {
        return billDetailCodeChoosen;
    }

    /**
     * Setter for property billDetailCodeChoosen.
     * @param billDetailCodeChoosen New value of property billDetailCodeChoosen.
     */
    public static void setBillDetailCodeChoosen(Vector argBillDetailCodeChoosen) {
        billDetailCodeChoosen = argBillDetailCodeChoosen;
    }

    /**
     * Getter for property billMainChoosen.
     * @return Value of property billMainChoosen.
     */
    public static Vector getBillMainChoosen() {
        return billMainChoosen;
    }

    /**
     * Setter for property billMainChoosen.
     * @param billMainChoosen New value of property billMainChoosen.
     */
    public static void setBillMainChoosen(Vector argBillMainChoosen) {
        billMainChoosen = argBillMainChoosen;
    }

    //public static PaymentHistoryDialog paymentHistoryDialog;
    public static void showCreditPaymentHistoryDialog(Object parent, String keyword) {

        //if(paymentHistoryDialog==null){
        PaymentHistoryDialog paymentHistoryDialog = new PaymentHistoryDialog(CashierMainApp.getMainFrame(), true);
        paymentHistoryDialog.setModal(true);
        paymentHistoryDialog.setLocation(CashSaleController.getCenterScreenPoint(paymentHistoryDialog));
        //}
        paymentHistoryDialog.setKeyword(keyword);
        paymentHistoryDialog.show();

    }

    /**
     * Getter for property salesChoosen.
     * @return Value of property salesChoosen.
     */
    public static Sales getSalesChoosen() {
        return salesChoosen;
    }

    /**
     * Setter for property salesChoosen.
     * @param salesChoosen New value of property salesChoosen.
     */
    public static void setSalesChoosen(Sales sales) {
        salesChoosen = sales;
    }

    //public static void transferFromCreditSales

    /**
     *  added by wpulantara
     *  for search sales
     */
    private static Sales salesChoosen = null;

    public static Vector getSales(int start, int recordToGet, String name, String code) {


        Vector result = new Vector();
        try {
            String whereClause = "";
            String nameClause = "";
            String codeClause = "";
            if (name != null && name.length() > 0) {
                nameClause = PstSales.fieldNames[PstSales.FLD_NAME] + " LIKE '" + prefix + name + sufix + "' ";
            }
            if (code != null && code.length() > 0) {
                codeClause = PstSales.fieldNames[PstSales.FLD_CODE] + " LIKE '" + prefix + code + sufix + "' ";
            }

            if (nameClause.length() > 0) {
                if (codeClause.length() > 0) {
                    whereClause = nameClause + " AND " + codeClause;
                } else {
                    whereClause = nameClause;
                }
            } else {
                whereClause = codeClause;
            }

            result = PstSales.list(start, recordToGet, whereClause, PstSales.fieldNames[PstSales.FLD_NAME]);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return result;
    }
    
    public static Vector getUserSales(int start, int recordToGet) {

        AppUser ap = new AppUser();
        Vector result = new Vector();
        try {
            String whereClause = "";
            whereClause = PstMappingUserGroup.fieldNames[PstMappingUserGroup.FLD_GROUP_USER_ID] + " = 6";
            result = PstMappingUserGroup.list(start, recordToGet, whereClause, "");
        } catch (Exception e) {

            e.printStackTrace();
        }
        return result;
    }
    
    public static int getUserSalesCount() {

        AppUser ap = new AppUser();
        int result = 0;
        try {
            String whereClause = "";
            whereClause = PstMappingUserGroup.fieldNames[PstMappingUserGroup.FLD_GROUP_USER_ID] + " = 6";
            result = PstMappingUserGroup.getCount(whereClause);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return result;
    }

    public static int getSalesCount(String name, String code) {

        int result = 0;
        try {
            String whereClause = "";
            String nameClause = "";
            String codeClause = "";
            if (name != null && name.length() > 0) {
                nameClause = PstSales.fieldNames[PstSales.FLD_NAME] + " LIKE '" + prefix + name + sufix + "' ";
            }
            if (code != null && code.length() > 0) {
                codeClause = PstSales.fieldNames[PstSales.FLD_CODE] + " LIKE '" + prefix + code + sufix + "' ";
            }

            if (nameClause.length() > 0) {
                if (codeClause.length() > 0) {
                    whereClause = nameClause + " AND " + codeClause;
                } else {
                    whereClause = nameClause;
                }
            } else {
                whereClause = codeClause;
            }

            result = PstSales.getCount(whereClause);
        } catch (Exception e) {


            e.printStackTrace();
        }


        return result;
    }

    private static SalesSearchDialog salesSearchDialog;

    public static void showSalesSearch(Object parent) {

        if (salesSearchDialog == null) {
            salesSearchDialog = new SalesSearchDialog(CashierMainApp.getMainFrame(), true);
            salesSearchDialog.setModal(true);
            salesSearchDialog.setLocation(getCenterScreenPoint(salesSearchDialog));
        }

        salesSearchDialog.show();

    }

    public static void showSalesSearch(Object parent, String name, String code) {

        if (salesSearchDialog == null) {
            salesSearchDialog = new SalesSearchDialog(CashierMainApp.getMainFrame(), true);
            salesSearchDialog.setLocation(getCenterScreenPoint(salesSearchDialog));
        }

        salesSearchDialog.refreshSearch(name, code);
        salesSearchDialog.show();
    }

    /**
     * Getter for property salesSearchDialog.
     * @return Value of property salesSearchDialog.
     */
    public static com.dimata.pos.cashier.SalesSearchDialog getSalesSearchDialog() {
        return salesSearchDialog;
    }

    /**
     * Setter for property salesSearchDialog.
     * @param salesSearchDialog New value of property salesSearchDialog.
     */
    public static void setSalesSearchDialog(com.dimata.pos.cashier.SalesSearchDialog sales) {
        salesSearchDialog = sales;
    }

    /**
     * added by wpulantara
     * for listing available serial number
     */
    public static Vector getSerialCode(int start, int recordToGet, long materialId, long locationId) {

        Vector result = new Vector();
        try {
            String whereClause = PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_MATERIAL_ID] + "=" + materialId +
                    " AND " + PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_STOCK_STATUS] + "=" + PstMaterialStockCode.FLD_STOCK_STATUS_GOOD;
            if (locationId > 0) {
                whereClause = whereClause + " AND " + PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_LOCATION_ID] + "=" + locationId;
            }
            result = PstMaterialStockCode.list(start, recordToGet, whereClause, PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_MATERIAL_ID]);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /** return currently selected MaterialStockCode object on SerialListDialog */
    public static int getSerialCodeCount(long materialId, long locationId) {

        int result = 0;
        try {
            String whereClause = PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_MATERIAL_ID] + "=" + materialId +
                    " AND " + PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_STOCK_STATUS] + "=" + PstMaterialStockCode.FLD_STOCK_STATUS_GOOD;
            if (locationId > 0) {
                whereClause = whereClause + " AND " + PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_LOCATION_ID] + "=" + locationId;
            }
            System.out.println("whereClause=" + whereClause);
            result = PstMaterialStockCode.getCount(whereClause);
        } catch (Exception e) {

            e.printStackTrace();
        }

        return result;
    }

    /**
     * Getter for property stockCodeChoosen.
     * @return Value of property stockCodeChoosen.
     */
    public static MaterialStockCode getStockCodeChoosen() {
        return stockCodeChoosen;
    }

    /**
     * Setter for property stockCodeChoosen.
     * @param stockCodeChoosen New value of property stockCodeChoosen.
     */
    public static void setStockCodeChoosen(MaterialStockCode stockCode) {
        stockCodeChoosen = stockCode;
    }

    /** Holds the current choosed material */
    private static MaterialStockCode stockCodeChoosen = null;
    /** Holds dialog form serialCodeList */
    private static SerialCodeListDialog serialCodeListDialog = null;
    /** Holds dialog form productImage */
    private static ProductImageDialog productImageDialog = null;
    /** Holds hashtable of location key = location code */
    private static Hashtable hashLocationByCode = null;
    /** Holds hashtable of location key = location code */
    private static Hashtable hashLocationById = null;
    /** Holds hashtable of location vales = location name */
    private static Hashtable hashLocationName = null;

    /** methode for showing the serialCodeListDialog */
    public static void showSerialCodeListDialog(Object parent, long matId, long locId) {
        if (serialCodeListDialog == null) {
            serialCodeListDialog = new SerialCodeListDialog(null, true, matId, locId);
            serialCodeListDialog.setLocation(getCenterScreenPoint(serialCodeListDialog));
        } else {
            serialCodeListDialog.setMaterialId(matId);
            serialCodeListDialog.setLocationId(locId);
            serialCodeListDialog.cmdSearch(0);
        }

        serialCodeListDialog.show();
    }

    /** methode for showing the productImageDialog */
    public static void showProductImageDialog(Object parent, String path) {
        if (productImageDialog == null) {
            productImageDialog = new ProductImageDialog(null, true);
        }
        productImageDialog.setImagePath(path);
        productImageDialog.setLocation(getCenterScreenPoint(productImageDialog));
        productImageDialog.show();
        productImageDialog.cmdSetFocus();
    }

    /** methode for showing the supervisorLoginDialog */
    public static void showSupervisorLoginDialog() {
        if (supervisorLoginDialog == null) {
            supervisorLoginDialog = new SupervisorLoginDialog(null, true);
        }
        supervisorLoginDialog.setLocation(getCenterScreenPoint(supervisorLoginDialog));
        supervisorLoginDialog.show();
    }

    /** methode for get the total qty already returned for current item
     *  for return invoice
     *  @param long billMainId, MaterialId, serialCode
     *  @author wpulantara
     *  all parameter is property of main invoice
     *  set serialCode = "" if there isn't any
     */
    public static double getQtyAlreadyReturned(long billMainId, long materialId, String serialCode) {
        double result = 0;

        String sql = "SELECT SUM(D." + PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY] + ") " +
                " FROM " + PstBillMain.TBL_CASH_BILL_MAIN + " AS M " +
                " INNER JOIN " + PstBillDetail.TBL_CASH_BILL_DETAIL + " AS D" +
                " ON M." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
                " = D." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] +
                " LEFT JOIN " + PstBillDetailCode.TBL_CASH_BILL_DETAIL_CODE + " AS C " +
                " ON D." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_DETAIL_ID] +
                " = C." + PstBillDetailCode.fieldNames[PstBillDetailCode.FLD_SALE_ITEM_ID] +
                " WHERE M." + PstBillMain.fieldNames[PstBillMain.FLD_PARENT_ID] +
                " = " + billMainId +
                " AND D." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID] +
                " = " + materialId;

        if (serialCode.length() > 0) {
            sql = sql + " AND C." + PstBillDetailCode.fieldNames[PstBillDetailCode.FLD_STOCK_CODE] +
                    " = '" + serialCode + "'";
        }

        DBResultSet dbrs = null;
        try {
            System.out.println(sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            if (rs.next()) {
                result = rs.getDouble(1);
            }
        } catch (Exception e) {

            System.out.println("err in getQtyAlreadyReturned : " + e.toString());
            e.printStackTrace();
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    /** methode for get the total amount already returned for a category
     *  for return invoice
     *  @param long billMainId, categoryId
     *  @author wpulantara
     *  all parameter is property of main invoice
     */
    public static double getAmountCategoryAlreadyReturned(long billMainId, long categoryId) {
        double result = 0;

        String sql = "SELECT SUM(D." + PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE] + ") " +
                " FROM " + PstBillMain.TBL_CASH_BILL_MAIN + " AS M " +
                " INNER JOIN " + PstBillDetail.TBL_CASH_BILL_DETAIL + " AS D" +
                " ON M." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
                " = D." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] +
                " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS C " +
                " ON D." + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_ID] +
                " = C." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                " WHERE M." + PstBillMain.fieldNames[PstBillMain.FLD_PARENT_ID] +
                " = " + billMainId +
                " AND C." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                " = " + categoryId;

        DBResultSet dbrs = null;
        try {
            System.out.println(sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            if (rs.next()) {
                result = rs.getDouble(1);
            }
        } catch (Exception e) {

            System.out.println("err in getAmountCategoryAlreadyReturned : " + e.toString());
            e.printStackTrace();
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    /** methode for get the total amount already returned for a invoice
     *  for credit payment
     *  @param long billMainId
     *  @author wpulantara
     *  all parameter is property of main invoice
     */
    public static double getAmountAlreadyReturned(long billMainId) {
        double result = 0;

        String sql = "SELECT SUM(D." + PstBillDetail.fieldNames[PstBillDetail.FLD_TOTAL_PRICE] + ") " +
                " , M." + PstBillMain.fieldNames[PstBillMain.FLD_DISCOUNT] +
                " , M." + PstBillMain.fieldNames[PstBillMain.FLD_TAX_VALUE] +
                " , M." + PstBillMain.fieldNames[PstBillMain.FLD_SERVICE_VALUE] +
                " FROM " + PstBillMain.TBL_CASH_BILL_MAIN + " AS M " +
                " INNER JOIN " + PstBillDetail.TBL_CASH_BILL_DETAIL + " AS D" +
                " ON M." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] +
                " = D." + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] +
                " WHERE M." + PstBillMain.fieldNames[PstBillMain.FLD_PARENT_ID] +
                " = " + billMainId +
                " GROUP BY " +
                " M." + PstBillMain.fieldNames[PstBillMain.FLD_DISCOUNT] +
                " , M." + PstBillMain.fieldNames[PstBillMain.FLD_TAX_VALUE] +
                " , M." + PstBillMain.fieldNames[PstBillMain.FLD_SERVICE_VALUE];

        DBResultSet dbrs = null;
        try {
            System.out.println(sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                result = result + rs.getDouble(1); // all item
                result = result - rs.getDouble(2); // disc
                result = result + rs.getDouble(3); // tax
                result = result + rs.getDouble(4); // service
            }
        } catch (Exception e) {

            System.out.println("err in getAmountAlreadyReturned : " + e.toString());
            e.printStackTrace();
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    /**
     * Getter for property guestChoosen.
     * @return Value of property guestChoosen.
     */
    public static Vector getGuestChoosen() {
        if (guestChoosen == null) {
            guestChoosen = new Vector();
        }
        return guestChoosen;
    }

    /**
     * Setter for property guestChoosen.
     * @param guestChoosen New value of property guestChoosen.
     */
    public static void setGuestChoosen(Vector argGuestChoosen) {
        guestChoosen = argGuestChoosen;
    }

    public static Vector guestChoosen;

    public static Vector getGuestList(String guestName, String roomNumber, String resNumber, int start, int recordToGet) {
        Vector result = new Vector();
        I_Guest guestHelper = null;
        try {
            guestHelper = (I_Guest) Class.forName(I_Guest.strCustomerLinkHanoman).newInstance();
            CustomerLink customer = new CustomerLink();
            customer.setCustomerName(guestName);
            customer.setRoomNumber(roomNumber);
            customer.setResNumber(resNumber);
            result = guestHelper.listCustomers(customer, start, recordToGet);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;

    }

    public static int getCountGuest(String guestName, String roomNumber, String resNumber, int start, int recordToGet) {
        Vector result = new Vector();
        int count = 0;
        I_Guest guestHelper = null;
        try {
            guestHelper = (I_Guest) Class.forName(I_Guest.strCustomerLinkHanoman).newInstance();
            CustomerLink customer = new CustomerLink();
            customer.setCustomerName(guestName);
            customer.setRoomNumber(roomNumber);
            customer.setResNumber(resNumber);
            count = guestHelper.countCustomers(customer);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;

    }

    /**
     * Getter for property cardCost.
     * @return Value of property cardCost.
     */
    public static double getCardCost() {
        return cardCost;
    }

    /**
     * Setter for property cardCost.
     * @param cardCost New value of property cardCost.
     */
    public static void setCardCost(double card) {
        cardCost = card;
    }

    /**
     * Getter for property otherCostChoosen.
     * @return Value of property otherCostChoosen.
     */
    public static Hashtable getOtherCostChoosen() {
        return otherCostChoosen;
    }

    /**
     * Setter for property otherCostChoosen.
     * @param otherCostChoosen New value of property otherCostChoosen.
     */
    public static void setOtherCostChoosen(Hashtable other) {
        otherCostChoosen = other;
    }

    /**
     * Getter for property totOtherCost.
     * @return Value of property totOtherCost.
     */
    public static double getTotOtherCost() {
        return totOtherCost;
    }

    /**
     * Setter for property totOtherCost.
     * @param totOtherCost New value of property totOtherCost.
     */
    public static void setTotOtherCost(double tot) {
        totOtherCost = tot;
    }

    public static Vector getFtpLogs() {
        Vector result = new Vector();
        try {
            String where = PstDocLogger.fieldNames[PstDocLogger.FLD_DOC_NUMBER] +
                    " LIKE  '" + CashierFtpConstant.FTP_PARAM + "%'";
            String order = PstDocLogger.fieldNames[PstDocLogger.FLD_DOC_DATE] + " DESC";
            Vector list = PstDocLogger.list(0, 5, where, order);
            for (int i = 0; i < list.size(); i++) {
                DocLogger log = (DocLogger) list.get(i);
                result.add(log.getDescription());
            }
        } catch (Exception e) {
            System.out.println("err on getFtpLogs() = " + e.toString());
        }

        return result;
    }

    /**
     * Getter for property supervisorApprove.
     * @return Value of property supervisorApprove.
     */
    public static boolean isSupervisorApprove() {
        return supervisorApprove;
    }

    /**
     * Setter for property supervisorApprove.
     * @param supervisorApprove New value of property supervisorApprove.
     */
    public static void setSupervisorApprove(boolean supervisor) {
        supervisorApprove = supervisor;
    }

    public static void getHashLocation() {
        try {
            String where = PstLocation.fieldNames[PstLocation.FLD_TYPE] + "=" + PstLocation.TYPE_LOCATION_STORE;
            Vector list = PstLocation.list(0, 0, where, PstLocation.fieldNames[PstLocation.FLD_CODE]);
            hashLocationByCode = new Hashtable();
            hashLocationById = new Hashtable();
            hashLocationName = new Hashtable();
            for (int i = 0; i < list.size(); i++) {
                Location loc = (Location) list.get(i);
                hashLocationByCode.put(loc.getCode() + "", loc.getOID() + "");
                hashLocationById.put(loc.getOID() + "", loc.getCode() + "");
                hashLocationName.put(loc.getCode() + "", loc.getName().length() > 12 ? loc.getName().substring(0, 12) : loc.getName() + "");
            }
        } catch (Exception e) {
            System.out.println("err on getHashLocation() = " + e.toString());
        }
    }

    /**
     * Getter for property hashLocationByCode.
     * @return Value of property hashLocationByCode.
     */
    public static java.util.Hashtable getHashLocationByCode() {
        if (hashLocationByCode == null) {
            getHashLocation();
        }
        return hashLocationByCode;
    }

    /**
     * Setter for property hashLocationByCode.
     * @param hashLocationByCode New value of property hashLocationByCode.
     */
    public static void setHashLocationByCode(java.util.Hashtable hash) {
        hashLocationByCode = hash;
    }

    /**
     * Getter for property hashLocationById.
     * @return Value of property hashLocationById.
     */
    public static java.util.Hashtable getHashLocationById() {
        if (hashLocationById == null) {
            getHashLocation();
        }
        return hashLocationById;
    }

    /**
     * Setter for property hashLocationById.
     * @param hashLocationById New value of property hashLocationById.
     */
    public static void setHashLocationById(java.util.Hashtable hash) {
        hashLocationById = hash;
    }

    /**
     * Getter for property hashLocationName.
     * @return Value of property hashLocationName.
     */
    public static java.util.Hashtable getHashLocationName() {
        if (hashLocationName == null) {
            getHashLocation();
        }
        return hashLocationName;
    }

    /**
     * Setter for property hashLocationName.
     * @param hashLocationName New value of property hashLocationName.
     */
    public static void setHashLocationName(java.util.Hashtable hash) {
        hashLocationName = hash;
    }

    public static Category getCategoryByMaterialId(long materialId) {
        Category result = new Category();
        String sql = " SELECT C.* " +
                " FROM " + PstCategory.TBL_CATEGORY + " AS C " +
                " INNER JOIN " + PstMaterial.TBL_MATERIAL + " AS M " +
                " ON M." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                " = C." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] +
                " WHERE M." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] +
                " = " + materialId;

        DBResultSet dbrs = null;
        try {
            System.out.println(sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            if (rs.next()) {
                PstCategory.resultToObject(rs, result);
            }
        } catch (Exception e) {

            System.out.println("err in getAmountAlreadyReturned : " + e.toString());
            e.printStackTrace();
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

}

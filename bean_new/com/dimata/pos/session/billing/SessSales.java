/*
 * SessInvoice.java
 *
 * Created on October 7, 2003, 1:03 PM
 */

package com.dimata.pos.session.billing;

import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;

import java.util.*;
import java.sql.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.*;
import com.dimata.pos.entity.billing.*;
import com.dimata.posbo.entity.masterdata.*;
import com.dimata.pos.entity.search.*;
import com.dimata.common.entity.payment.*;
import com.dimata.posbo.entity.admin.*;

public class  SessSales {
      public static final String className = I_DocType.DOCTYPE_CLASSNAME;

    public static final String SESS_SRC_ORDERMATERIAL = "SESSION_SRC_ORDERMATERIAL";
    public static final String SESS_SRC_ORDERMARKET = "SESSION_SRC_ORDERMARKET";
    public static final String SESS_SRC_ORDERASSET = "SESSION_SRC_ORDERASSET";

     public static Vector searchSalesOrder(SrcInvoice srcInvoice, int docType, int start, int recordToGet) {
        Vector vectOrderCode = LogicParser.textSentence(srcInvoice.getPrmnumber());
        for (int i = 0; i < vectOrderCode.size(); i++) {
            String name = (String) vectOrderCode.get(i);
            if ((name.equals(LogicParser.SIGN)) || (name.equals(LogicParser.ENGLISH[0])))
                vectOrderCode.remove(i);
        }

        return getSalesOrderList(className, docType, vectOrderCode, srcInvoice, start, recordToGet, 0);
    }


    public static Vector getSalesOrderList(String className, int docType, Vector vectNumber,  SrcInvoice srcInvoice, int start, int recordToGet, long oidVendor) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        try {
            I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(className).newInstance();
            /**
             * Ari wiweka
             * 20130724
             * Menambah DocType, transType, transStatus
             */
  
            String sql = "SELECT cm." +PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " ,cm." +PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]
                    + " , cm." +PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]
                    + ", cm." +PstBillMain.fieldNames[PstBillMain.FLD_CUSTOMER_ID]
                    + ", cl." +PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME]
                    + " , cl." +PstMemberReg.fieldNames[PstMemberReg.FLD_HOME_ADDR]
                    + ", cl." +PstMemberReg.fieldNames[PstMemberReg.FLD_HOME_TELP]
                    + ", cl." +PstMemberReg.fieldNames[PstMemberReg.FLD_EMAIL]
                    + " , cl." +PstMemberReg.fieldNames[PstMemberReg.FLD_COMP_NAME]
                    + ", cl." +PstMemberReg.fieldNames[PstMemberReg.FLD_BUSS_ADDRESS]
                    + ", cl." +PstMemberReg.fieldNames[PstMemberReg.FLD_TELP_MOBILE]
                    + " , cm." +PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID]
                    + ", ct." +PstCurrencyType.fieldNames[PstCurrencyType.FLD_NAME]
                    + ", cm." +PstBillMain.fieldNames[PstBillMain.FLD_AMOUNT]
                    + " , cm." +PstBillMain.fieldNames[PstBillMain.FLD_PAID_AMOUNT]
                    + ", cm." +PstBillMain.fieldNames[PstBillMain.FLD_APPUSER_ID]
                    + ", cm." +PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]
                    + ", cm." +PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]
                    + ", cm." +PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]
                    + ", au." +PstAppUser.fieldNames[PstAppUser.FLD_FULL_NAME]
                    + ", cm." +PstBillMain.fieldNames[PstBillMain.FLD_SALES_CODE]
                    + ", cm." +PstBillMain.fieldNames[PstBillMain.FLD_TYPE_SALES_ORDER]
                    + ", cm." +PstBillMain.fieldNames[PstBillMain.FLD_STATUS_INVOICING]
                    + " FROM " +PstBillMain.TBL_CASH_BILL_MAIN+ "  cm LEFT JOIN " +PstMemberReg.TBL_CONTACT_LIST+ "  cl ON cm." +PstBillMain.fieldNames[PstBillMain.FLD_CUSTOMER_ID]+ " = cl." +PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_ID]
                    + " LEFT JOIN " +PstCurrencyType.TBL_POS_CURRENCY_TYPE+ "  ct ON cm." +PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID]+ " = ct." +PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID]
                    + " LEFT JOIN " +PstAppUser.TBL_APP_USER+ "  au ON cm." +PstBillMain.fieldNames[PstBillMain.FLD_APPUSER_ID]+ " = au."+PstAppUser.fieldNames[PstAppUser.FLD_USER_ID]
                    + " LEFT JOIN " +PstSales.TBL_SALES+ " sa ON sa." +PstSales.fieldNames[PstSales.FLD_CODE]+ " = cm."+PstBillMain.fieldNames[PstBillMain.FLD_SALES_CODE];

            //Ari wiweka 20130714
            String strDate = "";
            if (srcInvoice.getStatusDate() != 0) {
                String startDate = Formater.formatDate(srcInvoice.getInvoiceDate(), "yyyy-MM-dd " );
                String endDate = Formater.formatDate(srcInvoice.getInvoiceDateTo(), "yyyy-MM-dd");
                strDate = " cm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " BETWEEN '" + startDate + " 00:00:00 ' AND '" + endDate + " 23:59:59'";
            }

            //Ari wiweka 20130714
            String invNumber = "";
            if (srcInvoice.getInvoiceNumber() != "" && srcInvoice.getInvoiceNumber().length() > 0) {
                invNumber = " cm." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] + " = '"+srcInvoice.getInvoiceNumber()+"'";
            }

            String cusName = "";
            if (srcInvoice.getCustomerName() != "" && srcInvoice.getCustomerName().length() > 0) {

                cusName = " cl." + PstMemberReg.fieldNames[PstMemberReg.FLD_COMP_NAME] + " LIKE '%"+srcInvoice.getCustomerName()+"%'";
            }

            String personName = "";
            if (srcInvoice.getMemberName() != "" && srcInvoice.getMemberName().length() > 0) {

                personName = " cl." + PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME] + " LIKE '%"+srcInvoice.getMemberName()+"%'";
            }

            String salesName = "";
            if (srcInvoice.getSalesPerson() != "" && srcInvoice.getSalesPerson().length() > 0) {

                salesName = " sa." + PstSales.fieldNames[PstSales.FLD_NAME] + " LIKE '%"+srcInvoice.getSalesPerson()+"%'";
            }
            
            String salesCode = "";
            if (srcInvoice.getSalesName()!= "" && srcInvoice.getSalesName().length() > 0) {

                salesCode = " sa." + PstAppUser.fieldNames[PstAppUser.FLD_FULL_NAME] + " LIKE '%"+srcInvoice.getSalesName()+"%'";
            }
//            String salesCode = "";
//            if (srcInvoice.getSalesCode()!= "" && srcInvoice.getSalesCode().length() > 0) {
//
//                salesCode = " sa." + PstSales.fieldNames[PstSales.FLD_CODE] + " LIKE '%"+srcInvoice.getSalesCode()+"%'";
//            }

            String doctype = "";
            if (srcInvoice.getDocType() != 0) {

                doctype = " cm." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " LIKE '%"+srcInvoice.getDocType()+"%'";
            }

            String transtype = "";
            if (srcInvoice.getTransType() != 0) {

                transtype = " cm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + " LIKE '%"+srcInvoice.getTransType()+"%'";
            }

            String transstatus = "";
            if (srcInvoice.getTransStatus() != 0) {
                transstatus = " cm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " LIKE '%"+srcInvoice.getTransStatus()+"%' AND cm."+ PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] +"='0'";
            }else{
                transstatus = " cm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " !=2 AND cm."+ PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] +"='0'";
            }

            String whereClause = "";

            if (strDate.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDate;
                } else {
                    whereClause = whereClause + strDate;
                }
            }

            if (invNumber.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + invNumber;
                } else {
                    whereClause = whereClause + invNumber;
                }
            }

            if (cusName.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + cusName;
                } else {
                    whereClause = whereClause + cusName;
                }
            }

            if (personName.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + personName;
                } else {
                    whereClause = whereClause + personName;
                }
            }

            if (salesName.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + salesName;
                } else {
                    whereClause = whereClause + salesName;
                }
            }
            
            if (salesCode.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + salesCode;
                } else {
                    whereClause = whereClause + salesCode;
                }
            }
            
            if (doctype.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + doctype;
                } else {
                    whereClause = whereClause + doctype;
                }
            }
            if (transtype.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + transtype;
                } else {
                    whereClause = whereClause + transtype;
                }
            }
            if (transstatus.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + transstatus;
                } else {
                    whereClause = whereClause + transstatus;
                }
            }



           /* if (strStatus.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strStatus;
                } else {
                    whereClause = whereClause + strStatus;
                }
            }*/

            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }


            //sql += ", cm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]; /** defaultnya, list diurut berdasarkan DATE */
            sql +=  " LIMIT " + start + "," + recordToGet;

            System.out.println("sql getSalesOrderList: " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                BillMain billMain = new BillMain();
                MemberReg memberReg = new MemberReg();
                CurrencyType currencyType = new CurrencyType();
                AppUser appUser = new AppUser();
                Vector vt = new Vector(1, 1);

                billMain.setOID(rs.getLong(PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]));
                billMain.setInvoiceNumber(rs.getString(PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]));
                billMain.setBillDate(rs.getDate(PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]));
                billMain.setCustomerId(rs.getLong(PstBillMain.fieldNames[PstBillMain.FLD_CUSTOMER_ID]));
                billMain.setCurrencyId(rs.getLong(PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID]));
                billMain.setAmount(rs.getDouble(PstBillMain.fieldNames[PstBillMain.FLD_AMOUNT]));
                billMain.setPaidAmount(rs.getDouble(PstBillMain.fieldNames[PstBillMain.FLD_PAID_AMOUNT]));
                billMain.setAppUserId(rs.getLong(PstBillMain.fieldNames[PstBillMain.FLD_APPUSER_ID]));
                billMain.setDiscType(rs.getInt(PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]));
                billMain.setTransctionType(rs.getInt(PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]));
                billMain.setTransactionStatus(rs.getInt(PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]));
                billMain.setSalesCode(rs.getString(PstBillMain.fieldNames[PstBillMain.FLD_SALES_CODE]));
                billMain.setTypeSalesOrder(rs.getInt(PstBillMain.fieldNames[PstBillMain.FLD_TYPE_SALES_ORDER]));
                billMain.setStatusInv(rs.getInt(PstBillMain.fieldNames[PstBillMain.FLD_STATUS_INVOICING]));
                //PstBillMain.fieldNames[PstBillMain.FLD_SALES_CODE]
                vt.add(billMain);

                memberReg.setPersonName(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME]));
                memberReg.setHomeAddr(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_HOME_ADDR]));
                memberReg.setHomeTelp(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_HOME_TELP]));
                memberReg.setEmail(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_EMAIL]));
                memberReg.setCompName(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_COMP_NAME]));
                memberReg.setBussAddress(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_BUSS_ADDRESS]));
                memberReg.setTelpMobile(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_TELP_MOBILE]));
                vt.add(memberReg);

                currencyType.setName(rs.getString(PstCurrencyType.fieldNames[PstCurrencyType.FLD_NAME]));
                vt.add(currencyType);

                appUser.setFullName(rs.getString(PstAppUser.fieldNames[PstAppUser.FLD_FULL_NAME]));
                vt.add(appUser);
                result.add(vt);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println("Err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    /**
     * ari Wiweka
     * 20130724
     * count sales order search
     * @param srcInvoice
     * @param docType
     * @return
     */
    public static int getCount(SrcInvoice srcInvoice, int docType) {
        int count = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT cm." +PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " ,cm." +PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]
                    + " , cm." +PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]
                    + ", cm." +PstBillMain.fieldNames[PstBillMain.FLD_CUSTOMER_ID]
                    + ", cl." +PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME]
                    + " , cl." +PstMemberReg.fieldNames[PstMemberReg.FLD_HOME_ADDR]
                    + ", cl." +PstMemberReg.fieldNames[PstMemberReg.FLD_HOME_TELP]
                    + ", cl." +PstMemberReg.fieldNames[PstMemberReg.FLD_EMAIL]
                    + " , cl." +PstMemberReg.fieldNames[PstMemberReg.FLD_COMP_NAME]
                    + ", cl." +PstMemberReg.fieldNames[PstMemberReg.FLD_BUSS_ADDRESS]
                    + ", cl." +PstMemberReg.fieldNames[PstMemberReg.FLD_TELP_MOBILE]
                    + " , cm." +PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID]
                    + ", ct." +PstCurrencyType.fieldNames[PstCurrencyType.FLD_NAME]
                    + ", cm." +PstBillMain.fieldNames[PstBillMain.FLD_AMOUNT]
                    + " , cm." +PstBillMain.fieldNames[PstBillMain.FLD_PAID_AMOUNT]
                    + ", cm." +PstBillMain.fieldNames[PstBillMain.FLD_APPUSER_ID]
                    + ", cm." +PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]
                    + ", cm." +PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]
                    + ", cm." +PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]
                    + ", au." +PstAppUser.fieldNames[PstAppUser.FLD_FULL_NAME]
                    + " FROM " +PstBillMain.TBL_CASH_BILL_MAIN+ "  cm LEFT JOIN " +PstMemberReg.TBL_CONTACT_LIST+ "  cl ON cm." +PstBillMain.fieldNames[PstBillMain.FLD_CUSTOMER_ID]+ " = cl." +PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_ID]
                    + " LEFT JOIN " +PstCurrencyType.TBL_POS_CURRENCY_TYPE+ "  ct ON cm." +PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID]+ " = ct." +PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID]
                    + " LEFT JOIN " +PstAppUser.TBL_APP_USER+ "  au ON cm." +PstBillMain.fieldNames[PstBillMain.FLD_APPUSER_ID]+ " = au."+PstAppUser.fieldNames[PstAppUser.FLD_USER_ID]
                    + " LEFT JOIN " +PstSales.TBL_SALES+ " sa ON sa." +PstSales.fieldNames[PstSales.FLD_CODE]+ " = cm."+PstBillMain.fieldNames[PstBillMain.FLD_SALES_CODE];



            //Ari wiweka 20130714
            String strDate = "";
            if (srcInvoice.getStatusDate() != 0) {
                String startDate = Formater.formatDate(srcInvoice.getInvoiceDate(), "yyyy-MM-dd " );
                String endDate = Formater.formatDate(srcInvoice.getInvoiceDateTo(), "yyyy-MM-dd");
                strDate = " cm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " BETWEEN '" + startDate + " 00:00:00 ' AND '" + endDate + " 23:59:59'";
            }

            //Ari wiweka 20130714
            String invNumber = "";
            if (srcInvoice.getInvoiceNumber() != "" && srcInvoice.getInvoiceNumber().length() > 0) {
                invNumber = " cm." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] + " = '"+srcInvoice.getInvoiceNumber()+"'";
            }

            String cusName = "";
            if (srcInvoice.getCustomerName() != "" && srcInvoice.getCustomerName().length() > 0) {

                cusName = " cl." + PstMemberReg.fieldNames[PstMemberReg.FLD_COMP_NAME] + " LIKE '%"+srcInvoice.getCustomerName()+"%'";
            }

            String personName = "";
            if (srcInvoice.getMemberName() != "" && srcInvoice.getMemberName().length() > 0) {

                personName = " cl." + PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME] + " LIKE '%"+srcInvoice.getMemberName()+"%'";
            }

            String salesName = "";
            if (srcInvoice.getSalesPerson() != "" && srcInvoice.getSalesPerson().length() > 0) {

                salesName = " au." + PstAppUser.fieldNames[PstAppUser.FLD_FULL_NAME] + " LIKE '%"+srcInvoice.getSalesPerson()+"%'";
            }
            
            
            String salesCode = "";
            if (srcInvoice.getSalesName()!= "" && srcInvoice.getSalesName().length() > 0) {
                salesCode = " sa." + PstAppUser.fieldNames[PstAppUser.FLD_FULL_NAME] + " LIKE '%"+srcInvoice.getSalesName()+"%'";
            }
//            String salesCode = "";
//            if (srcInvoice.getSalesCode()!= "" && srcInvoice.getSalesCode().length() > 0) {
//                salesCode = " sa." + PstSales.fieldNames[PstSales.FLD_CODE] + " LIKE '%"+srcInvoice.getSalesCode()+"%'";
//            }
            
            String doctype = "";
            //if (srcInvoice.getDocType() != 0) {

                doctype = " cm." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " = 0 ";
            //}

            String transtype = "";
            //if (srcInvoice.getTransType() != 0) {

                transtype = " cm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + " = 0 ";
            //}

            String transstatus = "";
            /*
             * update opie-eyek 20130916
             */
           if (srcInvoice.getTransStatus() != 0) {
                transstatus = " cm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " LIKE '%"+srcInvoice.getTransStatus()+"%'";
            }else{
                transstatus = " cm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " = 1 ";
            }

           
           String sDraff = "";
           if(srcInvoice.getStatusDraff() != 0)
               sDraff = " cm." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS_INVOICING] + "=0 ";
           
           String sOnProcess = "";
           if(srcInvoice.getStatusOnProcess()!= 0)
               sOnProcess = " cm." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS_INVOICING] + "=1 ";
           
           String sDone = "";
           if(srcInvoice.getStatusDone()!= 0)
               sDone = " cm." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS_INVOICING] + "=2 ";
           
           String statusInvoice = "";
           if(sDraff.length() > 0){
               statusInvoice = statusInvoice + sDraff;
               if (sOnProcess.length() > 0){
                   statusInvoice = statusInvoice + " OR " + sOnProcess;
               }
               if (sDone.length() > 0){
                   statusInvoice = statusInvoice + " OR " + sDone;
               }
           } else {
               if (sOnProcess.length() > 0){
                   statusInvoice = statusInvoice + sOnProcess;
               }
               if (sDone.length() > 0){
                   statusInvoice = statusInvoice + " OR " + sDone;
               }
           }
           

            String whereClause = "";

            if (strDate.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDate;
                } else {
                    whereClause = whereClause + strDate;
                }
            }

            if (invNumber.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + invNumber;
                } else {
                    whereClause = whereClause + invNumber;
                }
            }

            if (cusName.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + cusName;
                } else {
                    whereClause = whereClause + cusName;
                }
            }

            if (personName.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + personName;
                } else {
                    whereClause = whereClause + personName;
                }
            }

            if (salesName.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + salesName;
                } else {
                    whereClause = whereClause + salesName;
                }
            }
            
            if (salesCode.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + salesCode;
                } else {
                    whereClause = whereClause + salesCode;
                }
            }
            
            if (doctype.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + doctype;
                } else {
                    whereClause = whereClause + doctype;
                }
            }
            if (transtype.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + transtype;
                } else {
                    whereClause = whereClause + transtype;
                }
            }
            if (transstatus.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + transstatus;
                } else {
                    whereClause = whereClause + transstatus;
                }
            }

            if (statusInvoice.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + statusInvoice;
                } else {
                    whereClause = whereClause + statusInvoice;
                }
            }


            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            sql = "SELECT COUNT(*) FROM ("+sql+") AS tmp";


            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;
    }

    /////mchen
    public static Vector getList(SrcInvoice srcInvoice, int docType) {
        Vector result = new Vector(1, 1);
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT cm." +PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                    + " ,cm." +PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]
                    + " , cm." +PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]
                    + ", cm." +PstBillMain.fieldNames[PstBillMain.FLD_CUSTOMER_ID]
                    + ", cl." +PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME]
                    + " , cl." +PstMemberReg.fieldNames[PstMemberReg.FLD_HOME_ADDR]
                    + ", cl." +PstMemberReg.fieldNames[PstMemberReg.FLD_HOME_TELP]
                    + ", cl." +PstMemberReg.fieldNames[PstMemberReg.FLD_EMAIL]
                    + " , cl." +PstMemberReg.fieldNames[PstMemberReg.FLD_COMP_NAME]
                    + ", cl." +PstMemberReg.fieldNames[PstMemberReg.FLD_BUSS_ADDRESS]
                    + ", cl." +PstMemberReg.fieldNames[PstMemberReg.FLD_TELP_MOBILE]
                    + " , cm." +PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID]
                    + ", ct." +PstCurrencyType.fieldNames[PstCurrencyType.FLD_NAME]
                    + ", cm." +PstBillMain.fieldNames[PstBillMain.FLD_AMOUNT]
                    + " , cm." +PstBillMain.fieldNames[PstBillMain.FLD_PAID_AMOUNT]
                    + ", cm." +PstBillMain.fieldNames[PstBillMain.FLD_APPUSER_ID]
                    + ", cm." +PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]
                    + ", cm." +PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]
                    + ", cm." +PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]
                    + ", au." +PstAppUser.fieldNames[PstAppUser.FLD_FULL_NAME]
                    + ", sa." +PstSales.fieldNames[PstSales.FLD_NAME] //sa.SALES_NAME
                    + " FROM " +PstBillMain.TBL_CASH_BILL_MAIN+ "  cm LEFT JOIN " +PstMemberReg.TBL_CONTACT_LIST+ "  cl ON cm." +PstBillMain.fieldNames[PstBillMain.FLD_CUSTOMER_ID]+ " = cl." +PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_ID]
                    + " LEFT JOIN " +PstCurrencyType.TBL_POS_CURRENCY_TYPE+ "  ct ON cm." +PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID]+ " = ct." +PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID]
                    + " LEFT JOIN " +PstAppUser.TBL_APP_USER+ "  au ON cm." +PstBillMain.fieldNames[PstBillMain.FLD_APPUSER_ID]+ " = au."+PstAppUser.fieldNames[PstAppUser.FLD_USER_ID]
                    + " LEFT JOIN " +PstSales.TBL_SALES+ " sa ON sa." +PstSales.fieldNames[PstSales.FLD_CODE]+ " = cm."+PstBillMain.fieldNames[PstBillMain.FLD_SALES_CODE];



            //Ari wiweka 20130714
            String strDate = "";
            if (srcInvoice.getStatusDate() != 0) {
                String startDate = Formater.formatDate(srcInvoice.getInvoiceDate(), "yyyy-MM-dd " );
                String endDate = Formater.formatDate(srcInvoice.getInvoiceDateTo(), "yyyy-MM-dd");
                strDate = " cm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " BETWEEN '" + startDate + " 00:00:00 ' AND '" + endDate + " 23:59:59'";
            }

            //Ari wiweka 20130714
            String invNumber = "";
            if (srcInvoice.getInvoiceNumber() != "" && srcInvoice.getInvoiceNumber().length() > 0) {
                invNumber = " cm." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] + " = '"+srcInvoice.getInvoiceNumber()+"'";
            }

            String cusName = "";
            if (srcInvoice.getCustomerName() != "" && srcInvoice.getCustomerName().length() > 0) {

                cusName = " cl." + PstMemberReg.fieldNames[PstMemberReg.FLD_COMP_NAME] + " LIKE '%"+srcInvoice.getCustomerName()+"%'";
            }

            String personName = "";
            if (srcInvoice.getMemberName() != "" && srcInvoice.getMemberName().length() > 0) {

                personName = " cl." + PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME] + " LIKE '%"+srcInvoice.getMemberName()+"%'";
            }

            String salesName = "";
            if (srcInvoice.getSalesPerson() != "" && srcInvoice.getSalesPerson().length() > 0) {

                salesName = " sa." + PstSales.fieldNames[PstSales.FLD_NAME] + " LIKE '%"+srcInvoice.getSalesPerson()+"%'";
            }
            
            
            String salesCode = "";
            if (srcInvoice.getSalesName()!= "" && srcInvoice.getSalesName().length() > 0) {
                salesCode = " sa." + PstAppUser.fieldNames[PstAppUser.FLD_FULL_NAME] + " LIKE '%"+srcInvoice.getSalesName()+"%'";
            }
//            String salesCode = "";
//            if (srcInvoice.getSalesCode()!= "" && srcInvoice.getSalesCode().length() > 0) {
//                salesCode = " sa." + PstSales.fieldNames[PstSales.FLD_CODE] + " LIKE '%"+srcInvoice.getSalesCode()+"%'";
//            }
            
            String doctype = "";
            //if (srcInvoice.getDocType() != 0) {

                doctype = " cm." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " = 0 ";
            //}

            String transtype = "";
            //if (srcInvoice.getTransType() != 0) {

                transtype = " cm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + " = 0 ";
            //}

            String transstatus = "";
            /*
             * update opie-eyek 20130916
             */
           if (srcInvoice.getTransStatus() != 0) {
                transstatus = " cm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " LIKE '%"+srcInvoice.getTransStatus()+"%'";
            }else{
                transstatus = " cm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " = 1 ";
            }

           String sDraff = "";
           if(srcInvoice.getStatusDraff() != 0)
               sDraff = " cm." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS_INVOICING] + "=0 ";
           
           String sOnProcess = "";
           if(srcInvoice.getStatusOnProcess()!= 0)
               sOnProcess = " cm." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS_INVOICING] + "=1 ";
           
           String sDone = "";
           if(srcInvoice.getStatusDone()!= 0)
               sDone = " cm." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS_INVOICING] + "=2 ";
           
           String statusInvoice = "";
           if(sDraff.length() > 0){
               statusInvoice = statusInvoice + sDraff;
               if (sOnProcess.length() > 0){
                   statusInvoice = statusInvoice + " OR " + sOnProcess;
               }
               if (sDone.length() > 0){
                   statusInvoice = statusInvoice + " OR " + sDone;
               }
           } else {
               if (sOnProcess.length() > 0){
                   statusInvoice = statusInvoice + sOnProcess;
               }
               if (sDone.length() > 0){
                   statusInvoice = statusInvoice + " OR " + sDone;
               }
           }

            String whereClause = "";

            if (strDate.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDate;
                } else {
                    whereClause = whereClause + strDate;
                }
            }

            if (invNumber.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + invNumber;
                } else {
                    whereClause = whereClause + invNumber;
                }
            }

            if (cusName.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + cusName;
                } else {
                    whereClause = whereClause + cusName;
                }
            }

            if (personName.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + personName;
                } else {
                    whereClause = whereClause + personName;
                }
            }

            if (salesName.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + salesName;
                } else {
                    whereClause = whereClause + salesName;
                }
            }
            
            if (salesCode.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + salesCode;
                } else {
                    whereClause = whereClause + salesCode;
                }
            }
            
            if (doctype.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + doctype;
                } else {
                    whereClause = whereClause + doctype;
                }
            }
            if (transtype.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + transtype;
                } else {
                    whereClause = whereClause + transtype;
                }
            }
            if (transstatus.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + transstatus;
                } else {
                    whereClause = whereClause + transstatus;
                }
            }

            if (statusInvoice.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + statusInvoice;
                } else {
                    whereClause = whereClause + statusInvoice;
                }
            }

           
            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            //sql = "SELECT COUNT(*) FROM ("+sql+") AS tmp";


            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                BillMain billMain = new BillMain();
                MemberReg memberReg = new MemberReg();
                CurrencyType currencyType = new CurrencyType();
                AppUser appUser = new AppUser();
                Sales saler = new Sales();
                Vector vt = new Vector(1, 1);

                billMain.setOID(rs.getLong(PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]));
                billMain.setInvoiceNumber(rs.getString(PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]));
                billMain.setBillDate(rs.getDate(PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]));
                billMain.setCustomerId(rs.getLong(PstBillMain.fieldNames[PstBillMain.FLD_CUSTOMER_ID]));
                billMain.setCurrencyId(rs.getLong(PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID]));
                billMain.setAmount(rs.getDouble(PstBillMain.fieldNames[PstBillMain.FLD_AMOUNT]));
                billMain.setPaidAmount(rs.getDouble(PstBillMain.fieldNames[PstBillMain.FLD_PAID_AMOUNT]));
                billMain.setAppUserId(rs.getLong(PstBillMain.fieldNames[PstBillMain.FLD_APPUSER_ID]));
                billMain.setDiscType(rs.getInt(PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]));
                billMain.setTransctionType(rs.getInt(PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]));
                billMain.setTransactionStatus(rs.getInt(PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]));
                //billMain.setSalesCode(rs.getString(PstBillMain.fieldNames[PstBillMain.FLD_SALES_CODE]));
                //billMain.setTypeSalesOrder(rs.getInt(PstBillMain.fieldNames[PstBillMain.FLD_TYPE_SALES_ORDER]));
                //billMain.setStatusInv(rs.getInt(PstBillMain.fieldNames[PstBillMain.FLD_STATUS_INVOICING]));
                //PstBillMain.fieldNames[PstBillMain.FLD_SALES_CODE]
                vt.add(billMain);

                memberReg.setPersonName(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_PERSON_NAME]));
                memberReg.setHomeAddr(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_HOME_ADDR]));
                memberReg.setHomeTelp(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_HOME_TELP]));
                memberReg.setEmail(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_EMAIL]));
                memberReg.setCompName(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_COMP_NAME]));
                memberReg.setBussAddress(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_BUSS_ADDRESS]));
                memberReg.setTelpMobile(rs.getString(PstMemberReg.fieldNames[PstMemberReg.FLD_TELP_MOBILE]));
                vt.add(memberReg);

                currencyType.setName(rs.getString(PstCurrencyType.fieldNames[PstCurrencyType.FLD_NAME]));
                vt.add(currencyType);

                appUser.setFullName(rs.getString(PstAppUser.fieldNames[PstAppUser.FLD_FULL_NAME]));
                vt.add(appUser);
                saler.setName(rs.getString(PstSales.fieldNames[PstSales.FLD_NAME]));
                vt.add(saler);
                result.add(vt);
            }
         
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    public static int getCountSalesOrder(SrcInvoice srcInvoice, int docType) {
        if (srcInvoice != null)
            System.out.println("Not null!!!");
        else
            System.out.println("Null");
        Vector vectOrderCode = LogicParser.textSentence(srcInvoice.getPrmnumber());
        for (int i = 0; i < vectOrderCode.size(); i++) {
            String name = (String) vectOrderCode.get(i);
            //System.out.println("name : " + name);
            if ((name.equals(LogicParser.SIGN)) || (name.equals(LogicParser.ENGLISH[0])))
                vectOrderCode.remove(i);
        }

        return getCountSalesOrderList(className, docType, vectOrderCode,  srcInvoice, 0);
    }

    public static int getCountSalesOrderList(String className, int docType, Vector vectNumber, SrcInvoice srcInvoice, long oidVendor) {
        DBResultSet dbrs = null;
        int count = 0;
        try {

            I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(className).newInstance();

            String sql = "SELECT " +
                    " COUNT(" +PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + ") AS CNT " +
                    " FROM " + PstBillMain.TBL_CASH_BILL_MAIN  ;



            String strDate = "";
            if (srcInvoice.getStatusDate() != 0) {
                String startDate = Formater.formatDate(srcInvoice.getInvoiceDate(), "yyyy-MM-dd");
                String endDate = Formater.formatDate(srcInvoice.getInvoiceDateTo(), "yyyy-MM-dd");
                strDate = "cm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
            }

           /* String strStatus = "";
            if (srcInvoice.getPrmstatus() != null && srcInvoice.getPrmstatus().size() > 0) {
                for (int a = 0; a < srcInvoice.getPrmstatus().size(); a++) {
                    if (strStatus.length() != 0) {
                        strStatus = strStatus + " OR " + "(MAT." + PstBillMain.fieldNames[PstBillMain.FLD_PO_STATUS] + " =" + srcInvoice.getPrmstatus().get(a) + ")";
                    } else {
                        strStatus = "(MAT." + PstBillMain.fieldNames[PstBillMain.FLD_PO_STATUS] + " =" + srcInvoice.getPrmstatus().get(a) + ")";
                    }
                }
                strStatus = "(" + strStatus + ")";
            }*/



            String whereClause = "";


            if (strDate.length() > 0) {
                if (whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDate;
                } else {
                    whereClause = whereClause + strDate;
                }
            }


            if (whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                count = rs.getInt("CNT");
            }
            rs.close();

        } catch (Exception e) {
            System.out.println("Err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return count;
    }



   private static String TBL_SALES="sales_person";
    /** Creates new SessInvoice */
    public SessSales() {
    }

  public Vector getSales(){
  Vector lists = new Vector();
	DBResultSet dbrs = null;
        try{
            //String sql = "select cash_master_id,location,cashier_name  from CASH_MASTER  where cash_master_id="+cashMasterId;
            String sql=" SELECT "+PstSales.fieldNames[PstSales.FLD_SALES_ID]+","
                                 +PstSales.fieldNames[PstSales.FLD_CODE]+","
                                 +PstSales.fieldNames[PstSales.FLD_NAME]+""+
                       " FROM "+TBL_SALES;
                      // " WHERE "+PstSales.fieldNames[PstSales.FLD_CODE]+"='"+salesCode+"'";


            dbrs = DBHandler.execQueryResult(sql);
	    ResultSet rs = dbrs.getResultSet();
            lists = new Vector();
            while(rs.next()) {
               Vector sales=new Vector();
               sales.add(""+rs.getLong(1));
               sales.add(""+rs.getString(2));
               sales.add(""+rs.getString(3));
               lists.add(sales);
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


}

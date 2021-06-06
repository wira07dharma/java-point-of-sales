package com.dimata.aiso.session.arap;

import com.dimata.aiso.db.DBResultSet;
//import com.dimata.aiso.db.DBHandler;
import com.dimata.aiso.db.DBHandler;
import com.dimata.aiso.entity.arap.PstArApMain;
import com.dimata.aiso.entity.arap.PstArApPayment;
import com.dimata.aiso.entity.arap.PstArApItem;
import com.dimata.aiso.entity.search.SrcArApReport;
import com.dimata.aiso.entity.aktiva.*;
import com.dimata.aiso.entity.report.*;
import com.dimata.common.entity.contact.PstContactList;
import com.dimata.common.entity.currency.PstCurrencyType;
import com.dimata.util.Formater;
import com.dimata.interfaces.trantype.I_TransactionType;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Date;
import java.util.Collection;
import java.sql.ResultSet;

/**
 * User: pulantara Date: Oct 21, 2005 Time: 2:33:41 PM Description:
 */
public class SessArApDetailReport {

  // nama http session
  public static final String SESS_SEARCH_ARAP_DETAIL_REPORT = "SESS_SEARCH_ARAP_DETAIL_REPORT";
  public static final String SESS_SEARCH_ARAP_DETAIL = "SESS_SEARCH_ARAP_DETAIL";

  // report type
  public static final int AR_INCREASE = 0;
  public static final int AP_INCREASE = 1;
  public static final int AR_PAYMENT = 2;
  public static final int AP_PAYMENT = 3;
  public static final int AR_DETAIL = 4;
  public static final int AP_DETAIL = 5;
  public static final int AR_TODAY_DUE_DATE = 6;
  public static final int AP_TODAY_DUE_DATE = 7;
  public static final int AR_TOMORROW_DUE_DATE = 8;
  public static final int AP_TOMORROW_DUE_DATE = 9;
  public static final int AR_AGING = 10;
  public static final int AP_AGING = 11;

  public static final int REPORT_VS_PAYMENT_MAPPING = 4;

  public static final int AR_PAYMENT_ENTRY = 0;
  public static final int AP_PAYMENT_ENTRY = 1;

  public static final String[][] stReportType = {
    {
      "Daftar Penambahan Piutang",
      "Daftar Penambahan Hutang",
      "Daftar Pembayaran Piutang",
      "Daftar Pembayaran Hutang",
      "Daftar Piutang Detail",
      "Daftar Hutang Detail",
      "Daftar Piutang Jatuh Tempo Sekarang",
      "Daftar Hutang Jatuh Tempo Sekarang",
      "Daftar Piutang Jatuh Tempo Besok",
      "Daftar Hutang Jatuh Tempo Besok",
      "Daftar Umur Piutang",
      "Daftar Umur Hutang"
    },
    {
      "Receivable Increase Report",
      "Payable Increase Report",
      "Receivable Payment Report",
      "Payable Payment Report",
      "Receivable Detail Report",
      "Payable Detail Report",
      "Today Due Date Receivable Report",
      "Today Due Date Payable Report",
      "Tomorrow Due Date Receivable Report",
      "Tomorrow Due Date Payable Report",
      "Receivable Aging Report",
      "Payable Aging Report"
    }
  };

  public static final String[][] stEntryPayment = {
    {
      "Entry Pembayaran Piutang",
      "Entry Pembayaran Hutang"
    },
    {
      "Receivable Payment Entry",
      "Payable Payment Entry"
    }

  };

  public static final int TYPE_ARAP_MAIN = 0;
  public static final int TYPE_SELLING_AKTIVA = 1;
  public static final int TYPE_RECEIVE_AKTIVA = 2;
  public static final int TYPE_ORDER_AKTIVA = 3;

  public static Vector listDpOnOrderAktiva(SrcArApReport srcArApReport) {
    DBResultSet dbrs = null;
    Vector list = new Vector();
    try {
      String sql = "SELECT "
              + " A." + PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_DOWN_PAYMENT]
              + ", A." + PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_NOMOR_ORDER]
              + ", A." + PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_TANGGAL_ORDER]
              + ", R." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_RECEIVE_AKTIVA_ID]
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]
              + " FROM " + PstOrderAktiva.TBL_ORDER_AKTIVA + " AS A "
              + " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " AS CL "
              + " ON CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + " = A." + PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_SUPPLIER_ID]
              + " LEFT JOIN " + PstReceiveAktiva.TBL_RECEIVE_AKTIVA + " AS R "
              + " ON A." + PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_ORDER_AKTIVA_ID]
              + " = R." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_ORDER_AKTIVA_ID];

      String where = " A." + PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_DOWN_PAYMENT] + " > 0 ";

      if (srcArApReport.getContactName().length() > 0) {
        if (where.length() > 0) {
          where = where + " AND (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'"
                  + " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
        } else {
          where = " (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'"
                  + " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
        }
      }

      if (where.length() > 0) {
        where = where + " AND A." + PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_TANGGAL_ORDER] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
      } else {
        where = " A." + PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_TANGGAL_ORDER] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
      }

      if (srcArApReport.getNotaNo().length() > 0) {
        if (where.length() > 0) {
          where = where + " AND A." + PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_NOMOR_ORDER] + " LIKE '%" + srcArApReport.getNotaNo() + "%'";
        } else {
          where = " A." + PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_NOMOR_ORDER] + " LIKE '%" + srcArApReport.getNotaNo() + "%'";
        }
      }

      if (where.length() > 0) {
        sql = sql + " WHERE " + where;
      }

      sql = sql + " ORDER BY CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]
              + ", A." + PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_TANGGAL_ORDER];

      System.out.println(sql);

      dbrs = DBHandler.execQueryResult(sql);
      ResultSet rs = dbrs.getResultSet();
      ArApDetail arap = new ArApDetail();
      Vector listItem = new Vector();
      double totNom = 0;
      double totPay = 0;
      while (rs.next()) {
        long contactId = rs.getLong(PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]);
        if (contactId != arap.getContactId()) {
          if (arap.getContactId() > 0) {
            arap.setDetail(listItem);
            arap.setTotalNominal(totNom);
            arap.setTotalPay(totPay);
            list.add(arap);
          }
          arap = new ArApDetail();
          listItem = new Vector();
          totNom = 0;
          totPay = 0;
          arap.setContactId(contactId);
          String compName = rs.getString(PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);
          String personName = rs.getString(PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]);
          String name = "";
          if (compName != null && compName.length() > 0) {
            name = compName;
          }
          if (personName != null && personName.length() > 0) {
            if (name.length() > 0) {
              name = name + " / " + personName;
            } else {
              name = personName;
            }
          }
          arap.setContactName(name);
        }
        ArApDetailItem item = new ArApDetailItem();
        item.setItemType(TYPE_ORDER_AKTIVA);
        item.setVoucherNo(rs.getString(PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_NOMOR_ORDER]));
        item.setVoucherDate(rs.getDate(PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_TANGGAL_ORDER]));
        item.setDueDate(rs.getDate(PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_TANGGAL_ORDER]));
        item.setNominal(rs.getDouble(PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_DOWN_PAYMENT]));
        item.setDescription("ORDER AKTIVA DP");
        if (rs.getLong(PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_RECEIVE_AKTIVA_ID]) > 0) {
          item.setPayed(item.getNominal());
        }
        totNom = totNom + item.getNominal();
        totPay = totPay + item.getPayed();
        listItem.add(item);
      }
      if (arap.getContactId() > 0) {
        arap.setDetail(listItem);
        arap.setTotalNominal(totNom);
        arap.setTotalPay(totPay);
        list.add(arap);
      }

    } catch (Exception e) {
      System.out.println("err.SessArApReport - listDpOnOrderAktiva " + e.toString());
    } finally {
      DBResultSet.close(dbrs);
    }
    return list;
  }

  public static Vector listDpOnRecieveAktiva(SrcArApReport srcArApReport) {
    DBResultSet dbrs = null;
    Vector list = new Vector();
    try {
      String sql = "SELECT "
              + " A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_DOWN_PAYMENT]
              + ", A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_TANGGAL_RECEIVE]
              + ", A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_NOMOR_RECEIVE]
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]
              + " FROM " + PstReceiveAktiva.TBL_RECEIVE_AKTIVA + " AS A "
              + " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " AS CL "
              + " ON CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + " = A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_SUPPLIER_ID];

      String where = " A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_DOWN_PAYMENT] + " > 0 ";

      if (srcArApReport.getContactName().length() > 0) {
        if (where.length() > 0) {
          where = where + " AND (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'"
                  + " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
        } else {
          where = " (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'"
                  + " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
        }
      }

      if (where.length() > 0) {
        where = where + " AND A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_TANGGAL_RECEIVE] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
      } else {
        where = " A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_TANGGAL_RECEIVE] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
      }

      if (srcArApReport.getNotaNo().length() > 0) {
        if (where.length() > 0) {
          where = where + " AND A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_NOMOR_RECEIVE] + " LIKE '%" + srcArApReport.getNotaNo() + "%'";
        } else {
          where = " A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_NOMOR_RECEIVE] + " LIKE '%" + srcArApReport.getNotaNo() + "%'";
        }
      }

      if (where.length() > 0) {
        sql = sql + " WHERE " + where;
      }

      sql = sql + " ORDER BY CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]
              + ", A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_TANGGAL_RECEIVE];

      System.out.println(sql);

      dbrs = DBHandler.execQueryResult(sql);
      ResultSet rs = dbrs.getResultSet();
      ArApDetail arap = new ArApDetail();
      Vector listItem = new Vector();
      double totPay = 0;
      while (rs.next()) {
        long contactId = rs.getLong(PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]);
        if (contactId != arap.getContactId()) {
          if (arap.getContactId() > 0) {
            arap.setPayment(listItem);
            arap.setTotalPay(totPay);
            list.add(arap);
          }
          arap = new ArApDetail();
          listItem = new Vector();
          totPay = 0;
          arap.setContactId(contactId);
          String compName = rs.getString(PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);
          String personName = rs.getString(PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]);
          String name = "";
          if (compName != null && compName.length() > 0) {
            name = compName;
          }
          if (personName != null && personName.length() > 0) {
            if (name.length() > 0) {
              name = name + " / " + personName;
            } else {
              name = personName;
            }
          }
          arap.setContactName(name);
        }
        ArApDetailPayment item = new ArApDetailPayment();
        item.setPaymentNo(rs.getString(PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_NOMOR_RECEIVE]));
        item.setPaymentDate(rs.getDate(PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_TANGGAL_RECEIVE]));
        item.setNominal(rs.getDouble(PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_DOWN_PAYMENT]));

        totPay = totPay + item.getNominal();

        listItem.add(item);
      }
      if (arap.getContactId() > 0) {
        arap.setPayment(listItem);
        arap.setTotalPay(totPay);
        list.add(arap);
      }

    } catch (Exception e) {
      System.out.println("err.SessArApReport - listDpOnRecieveAktiva " + e.toString());
    } finally {
      DBResultSet.close(dbrs);
    }
    return list;
  }

  public static Vector listApOnReceiveAktiva(SrcArApReport srcArApReport) {
    DBResultSet dbrs = null;
    Vector list = new Vector();
    try {
      String sql = "SELECT "
              + " SUM(I." + PstReceiveAktivaItem.fieldNames[PstReceiveAktivaItem.FLD_TOTAL_PRICE] + ")/"
              + " COUNT(DISTINCT B." + PstArApItem.fieldNames[PstArApItem.FLD_ARAP_ITEM_ID] + ") AS TOTAL "
              + ", SUM(B." + PstArApItem.fieldNames[PstArApItem.FLD_LEFT_TO_PAY] + ")/"
              + " COUNT(DISTINCT I." + PstReceiveAktivaItem.fieldNames[PstReceiveAktivaItem.FLD_RECEIVE_AKTIVA_ITEM_ID] + ") AS LEFT_TO_PAY "
              + ", MIN(B." + PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE] + ") AS MIN_DATE "
              + ", A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_TANGGAL_RECEIVE]
              + ", A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_NOMOR_RECEIVE]
              + ", A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_RECEIVE_AKTIVA_ID]
              + ", A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_DOWN_PAYMENT]
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]
              + " FROM " + PstReceiveAktiva.TBL_RECEIVE_AKTIVA + " AS A "
              + " INNER JOIN " + PstReceiveAktivaItem.TBL_RECEIVE_AKTIVA_ITEM + " AS I "
              + " ON A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_RECEIVE_AKTIVA_ID]
              + " = I." + PstReceiveAktivaItem.fieldNames[PstReceiveAktivaItem.FLD_RECEIVE_AKTIVA_ID]
              + " INNER JOIN " + PstArApItem.TBL_ARAP_ITEM + " AS B "
              + " ON A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_RECEIVE_AKTIVA_ID]
              + " = B." + PstArApItem.fieldNames[PstArApItem.FLD_RECEIVE_AKTIVA_ID]
              + " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " AS CL "
              + " ON CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + " = A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_SUPPLIER_ID];

      String where = " A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_TYPE_PEMBAYARAN] + "=" + I_TransactionType.TIPE_TRANSACTION_KREDIT
              + " AND B." + PstArApItem.fieldNames[PstArApItem.FLD_LEFT_TO_PAY] + " > 0 ";

      if (srcArApReport.getContactName().length() > 0) {
        if (where.length() > 0) {
          where = where + " AND (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'"
                  + " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
        } else {
          where = " (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'"
                  + " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
        }
      }

      if (srcArApReport.getReportType() >= SessArApDetailReport.AR_TODAY_DUE_DATE) {
        if (where.length() > 0) {
          where = where + " AND B." + PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
        } else {
          where = " B." + PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
        }
      } else {
        if (where.length() > 0) {
          where = where + " AND A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_TANGGAL_RECEIVE] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
        } else {
          where = " A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_TANGGAL_RECEIVE] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
        }
      }

      if (srcArApReport.getNotaNo().length() > 0) {
        if (where.length() > 0) {
          where = where + " AND A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_NOMOR_RECEIVE] + " LIKE '%" + srcArApReport.getNotaNo() + "%'";
        } else {
          where = " A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_NOMOR_RECEIVE] + " LIKE '%" + srcArApReport.getNotaNo() + "%'";
        }
      }

      if (where.length() > 0) {
        sql = sql + " WHERE " + where;
      }

      sql = sql + " GROUP BY "
              + " A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_TANGGAL_RECEIVE]
              + ", A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_NOMOR_RECEIVE]
              + ", A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_RECEIVE_AKTIVA_ID]
              + ", A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_DOWN_PAYMENT]
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID];

      sql = sql + " ORDER BY CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]
              + ", A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_TANGGAL_RECEIVE];

      System.out.println(sql);

      dbrs = DBHandler.execQueryResult(sql);
      ResultSet rs = dbrs.getResultSet();
      ArApDetail arap = new ArApDetail();
      Vector listItem = new Vector();
      double totNom = 0;
      double totPay = 0;
      while (rs.next()) {
        long contactId = rs.getLong(PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]);
        if (contactId != arap.getContactId()) {
          if (arap.getContactId() > 0) {
            arap.setDetail(listItem);
            arap.setTotalNominal(totNom);
            arap.setTotalPay(totPay);
            list.add(arap);
          }
          arap = new ArApDetail();
          listItem = new Vector();
          totNom = 0;
          totPay = 0;
          arap.setContactId(contactId);
          String compName = rs.getString(PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);
          String personName = rs.getString(PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]);
          String name = "";
          if (compName != null && compName.length() > 0) {
            name = compName;
          }
          if (personName != null && personName.length() > 0) {
            if (name.length() > 0) {
              name = name + " / " + personName;
            } else {
              name = personName;
            }
          }
          arap.setContactName(name);
        }
        ArApDetailItem item = new ArApDetailItem();
        item.setItemType(TYPE_RECEIVE_AKTIVA);
        item.setMainId(rs.getLong(PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_RECEIVE_AKTIVA_ID]));
        item.setVoucherNo(rs.getString(PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_NOMOR_RECEIVE]));
        item.setVoucherDate(rs.getDate(PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_TANGGAL_RECEIVE]));
        item.setDueDate(rs.getDate("MIN_DATE"));
        item.setNominal(rs.getDouble("TOTAL") - rs.getDouble(PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_DOWN_PAYMENT]));
        item.setDescription("FROM RECEIVE AKTIVA");
        item.setPayed(item.getNominal() - rs.getDouble("LEFT_TO_PAY"));
        totNom = totNom + item.getNominal();
        totPay = totPay + item.getPayed();
        listItem.add(item);
      }
      if (arap.getContactId() > 0) {
        arap.setDetail(listItem);
        arap.setTotalNominal(totNom);
        arap.setTotalPay(totPay);
        list.add(arap);
      }

    } catch (Exception e) {
      System.out.println("err.SessArApReport - listApOnRecieveAktiva " + e.toString());
    } finally {
      DBResultSet.close(dbrs);
    }
    return list;
  }

  public static Vector listArOnSellingAktiva(SrcArApReport srcArApReport) {
    DBResultSet dbrs = null;
    Vector list = new Vector();
    try {
      String sql = "SELECT "
              + " SUM(I." + PstSellingAktivaItem.fieldNames[PstSellingAktivaItem.FLD_TOTAL_PRICE] + ")/"
              + " COUNT(DISTINCT B." + PstArApItem.fieldNames[PstArApItem.FLD_ARAP_ITEM_ID] + ") AS TOTAL "
              + ", SUM(B." + PstArApItem.fieldNames[PstArApItem.FLD_LEFT_TO_PAY] + ")/"
              + " COUNT(DISTINCT I." + PstSellingAktivaItem.fieldNames[PstSellingAktivaItem.FLD_SELLING_AKTIVA_ITEM_ID] + ") AS LEFT_TO_PAY "
              + ", MIN(B." + PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE] + ") AS MIN_DATE "
              + ", A." + PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_TANGGAL_SELLING]
              + ", A." + PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_SELLING_AKTIVA_ID]
              + ", A." + PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_NOMOR_SELLING]
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]
              + " FROM " + PstSellingAktiva.TBL_SELLING_AKTIVA + " AS A "
              + " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " AS CL "
              + " ON CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + " = A." + PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_KONSUMEN_ID]
              + " INNER JOIN " + PstArApItem.TBL_ARAP_ITEM + " AS B "
              + " ON A." + PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_SELLING_AKTIVA_ID]
              + " = B." + PstArApItem.fieldNames[PstArApItem.FLD_SELLING_AKTIVA_ID]
              + " INNER JOIN " + PstSellingAktivaItem.TBL_SELLING_AKTIVA_ITEM + " AS I "
              + " ON A." + PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_SELLING_AKTIVA_ID]
              + " = I." + PstSellingAktivaItem.fieldNames[PstSellingAktivaItem.FLD_SELLING_AKTIVA_ID];

      String where = " A." + PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_TYPE_PEMBAYARAN] + " = " + I_TransactionType.TIPE_TRANSACTION_KREDIT;

      if (srcArApReport.getContactName().length() > 0) {
        if (where.length() > 0) {
          where = where + " AND (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'"
                  + " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
        } else {
          where = " (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'"
                  + " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
        }
      }

      if (srcArApReport.getReportType() >= SessArApDetailReport.AR_TODAY_DUE_DATE) {
        if (where.length() > 0) {
          where = where + " AND B." + PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
        } else {
          where = " B." + PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
        }
      } else {
        if (where.length() > 0) {
          where = where + " AND A." + PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_TANGGAL_SELLING] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
        } else {
          where = " A." + PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_TANGGAL_SELLING] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
        }
      }
      if (srcArApReport.getNotaNo().length() > 0) {
        if (where.length() > 0) {
          where = where + " AND A." + PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_NOMOR_SELLING] + " LIKE '%" + srcArApReport.getNotaNo() + "%'";
        } else {
          where = " A." + PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_NOMOR_SELLING] + " LIKE '%" + srcArApReport.getNotaNo() + "%'";
        }
      }

      if (where.length() > 0) {
        sql = sql + " WHERE " + where;
      }

      sql = sql + " GROUP BY "
              + " A." + PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_TANGGAL_SELLING]
              + ", A." + PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_SELLING_AKTIVA_ID]
              + ", A." + PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_NOMOR_SELLING]
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID];

      sql = sql + " ORDER BY CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]
              + ", A." + PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_TANGGAL_SELLING];

      System.out.println(sql);

      dbrs = DBHandler.execQueryResult(sql);
      ResultSet rs = dbrs.getResultSet();
      ArApDetail arap = new ArApDetail();
      Vector listItem = new Vector();
      double totNom = 0;
      double totPay = 0;
      while (rs.next()) {
        long contactId = rs.getLong(PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]);
        if (contactId != arap.getContactId()) {
          if (arap.getContactId() > 0) {
            arap.setDetail(listItem);
            arap.setTotalNominal(totNom);
            arap.setTotalPay(totPay);
            list.add(arap);
          }
          arap = new ArApDetail();
          listItem = new Vector();
          totNom = 0;
          totPay = 0;
          arap.setContactId(contactId);
          String compName = rs.getString(PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);
          String personName = rs.getString(PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]);
          String name = "";
          if (compName != null && compName.length() > 0) {
            name = compName;
          }
          if (personName != null && personName.length() > 0) {
            if (name.length() > 0) {
              name = name + " / " + personName;
            } else {
              name = personName;
            }
          }
          arap.setContactName(name);
        }
        ArApDetailItem item = new ArApDetailItem();
        item.setItemType(TYPE_SELLING_AKTIVA);
        item.setMainId(rs.getLong(PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_SELLING_AKTIVA_ID]));
        item.setVoucherNo(rs.getString(PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_NOMOR_SELLING]));
        item.setVoucherDate(rs.getDate(PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_TANGGAL_SELLING]));
        item.setDueDate(rs.getDate("MIN_DATE"));
        item.setNominal(rs.getDouble("TOTAL"));
        item.setDescription("FROM SELLING AKTIVA");
        item.setPayed(item.getNominal() - rs.getDouble("LEFT_TO_PAY"));
        totNom = totNom + item.getNominal();
        totPay = totPay + item.getPayed();
        listItem.add(item);
      }
      if (arap.getContactId() > 0) {
        arap.setDetail(listItem);
        arap.setTotalNominal(totNom);
        arap.setTotalPay(totPay);
        list.add(arap);
      }

    } catch (Exception e) {
      System.out.println("err.SessArApReport - listArOnSellingAktiva " + e.toString());
    } finally {
      DBResultSet.close(dbrs);
    }
    System.out.println("LIST SIZE = " + list.size());
    return list;
  }

  public static Vector listArOnArApMain(SrcArApReport srcArApReport) {
    DBResultSet dbrs = null;
    Vector list = new Vector();
    try {
      String sql = "SELECT "
              + " A." + PstArApMain.fieldNames[PstArApMain.FLD_AMOUNT]
              + ", A." + PstArApMain.fieldNames[PstArApMain.FLD_ARAP_MAIN_ID]
              + ", A." + PstArApMain.fieldNames[PstArApMain.FLD_NOTA_DATE]
              + ", A." + PstArApMain.fieldNames[PstArApMain.FLD_VOUCHER_NO]
              + ", A." + PstArApMain.fieldNames[PstArApMain.FLD_DESCRIPTION]
              + ", A." + PstArApMain.fieldNames[PstArApMain.FLD_NOTA_NO]
              + //doc status by mirahu 20120308
              ", A." + PstArApMain.fieldNames[PstArApMain.FLD_ARAP_DOC_STATUS]
              + ", C." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE]
              + ", SUM(B." + PstArApItem.fieldNames[PstArApItem.FLD_LEFT_TO_PAY] + ") AS LEFT_TO_PAY "
              + ", MIN(B." + PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE] + ") AS MIN_DATE "
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]
              + " FROM " + PstArApMain.TBL_ARAP_MAIN + " AS A "
              + " INNER JOIN " + PstArApItem.TBL_ARAP_ITEM + " AS B "
              + " ON A." + PstArApMain.fieldNames[PstArApMain.FLD_ARAP_MAIN_ID]
              + " = B." + PstArApItem.fieldNames[PstArApItem.FLD_ARAP_MAIN_ID]
              + " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " AS CL "
              + " ON CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + " = A." + PstArApMain.fieldNames[PstArApMain.FLD_CONTACT_ID]
              + " INNER JOIN " + PstCurrencyType.TBL_CURRENCY_TYPE + " AS C "
              + " ON A." + PstArApMain.fieldNames[PstArApMain.FLD_ID_CURRENCY] + " = C." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID];

      String where = " A." + PstArApMain.fieldNames[PstArApMain.FLD_ARAP_TYPE] + " = " + PstArApMain.TYPE_AR;

      if (srcArApReport.getContactName().length() > 0) {
        if (where.length() > 0) {
          where = where + " AND (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'"
                  + " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
        } else {
          where = " (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'"
                  + " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
        }
      }

      if (srcArApReport.getReportType() >= SessArApDetailReport.AR_TODAY_DUE_DATE) {
        if (where.length() > 0) {
          where = where + " AND B." + PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
        } else {
          where = " B." + PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
        }
      } else {
        if (where.length() > 0) {
          where = where + " AND A." + PstArApMain.fieldNames[PstArApMain.FLD_NOTA_DATE] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
        } else {
          where = " A." + PstArApMain.fieldNames[PstArApMain.FLD_NOTA_DATE] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
        }
      }

      if (srcArApReport.getNotaNo().length() > 0) {
        if (where.length() > 0) {
          where = where + " AND A." + PstArApMain.fieldNames[PstArApMain.FLD_VOUCHER_NO] + " LIKE '%" + srcArApReport.getNotaNo() + "%'";
        } else {
          where = " A." + PstArApMain.fieldNames[PstArApMain.FLD_VOUCHER_NO] + " LIKE '%" + srcArApReport.getNotaNo() + "%'";
        }
      }

      if (srcArApReport.getViewType() == 0) {
        if (where.length() > 0) {
          where += " AND B." + PstArApItem.fieldNames[PstArApItem.FLD_LEFT_TO_PAY] + " > 0 ";
        } else {
          where += PstArApItem.fieldNames[PstArApItem.FLD_LEFT_TO_PAY] + " > 0 ";
        }
      }

      if (where.length() > 0) {
        sql = sql + " WHERE " + where;
      }

      sql = sql + " GROUP BY "
              + " A." + PstArApMain.fieldNames[PstArApMain.FLD_AMOUNT]
              + ", A." + PstArApMain.fieldNames[PstArApMain.FLD_ARAP_MAIN_ID]
              + ", A." + PstArApMain.fieldNames[PstArApMain.FLD_NOTA_DATE]
              + ", A." + PstArApMain.fieldNames[PstArApMain.FLD_VOUCHER_NO]
              + ", A." + PstArApMain.fieldNames[PstArApMain.FLD_DESCRIPTION]
              + ", A." + PstArApMain.fieldNames[PstArApMain.FLD_NOTA_NO]
              + ", C." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE]
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID];

      sql = sql + " ORDER BY CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]
              + ", A." + PstArApMain.fieldNames[PstArApMain.FLD_NOTA_DATE];

            //System.out.println("SQL SessArApDetailReport.listARonArMain :::::: "+sql);
      dbrs = DBHandler.execQueryResult(sql);
      ResultSet rs = dbrs.getResultSet();
      ArApDetail arap = new ArApDetail();
      Vector listItem = new Vector();
      double totNom = 0;
      double totPay = 0;
      while (rs.next()) {
        long contactId = rs.getLong(PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]);
        if (contactId != arap.getContactId()) {
          if (arap.getContactId() > 0) {
            arap.setDetail(listItem);
            arap.setTotalNominal(totNom);
            arap.setTotalPay(totPay);
            list.add(arap);
          }
          arap = new ArApDetail();
          listItem = new Vector();
          totNom = 0;
          totPay = 0;
          arap.setContactId(contactId);
          String compName = rs.getString(PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);
          String personName = rs.getString(PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]);
          String name = "";
          if (compName != null && compName.length() > 0) {
            name = compName;
          }
          if (personName != null && personName.length() > 0) {
            if (name.length() > 0) {
              name = name + " / " + personName;
            } else {
              name = personName;
            }
          }
          arap.setContactName(name);
        }
        ArApDetailItem item = new ArApDetailItem();
        item.setItemType(TYPE_ARAP_MAIN);
        item.setMainId(rs.getLong(PstArApMain.fieldNames[PstArApMain.FLD_ARAP_MAIN_ID]));
        item.setVoucherNo(rs.getString(PstArApMain.fieldNames[PstArApMain.FLD_VOUCHER_NO]));
        item.setVoucherDate(rs.getDate(PstArApMain.fieldNames[PstArApMain.FLD_NOTA_DATE]));
        item.setDueDate(rs.getDate("MIN_DATE"));
        item.setNominal(rs.getDouble(PstArApMain.fieldNames[PstArApMain.FLD_AMOUNT]));
        item.setDescription(rs.getString(PstArApMain.fieldNames[PstArApMain.FLD_DESCRIPTION]));
        item.setPayed(item.getNominal() - rs.getDouble("LEFT_TO_PAY"));
        item.setNotaNo(rs.getString(PstArApMain.fieldNames[PstArApMain.FLD_NOTA_NO]));
        item.setCurrCode(rs.getString(PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE]));
        totNom = totNom + item.getNominal();
        totPay = totPay + item.getPayed();
        listItem.add(item);
      }
      if (arap.getContactId() > 0) {
        arap.setDetail(listItem);
        arap.setTotalNominal(totNom);
        arap.setTotalPay(totPay);
        list.add(arap);
      }

    } catch (Exception e) {
      System.out.println("err.SessArApReport - listArOnArApMain " + e.toString());
    } finally {
      DBResultSet.close(dbrs);
    }
    System.out.println("LIST SIZE = " + list.size());
    return list;
  }

  public static Vector listApOnArApMain(SrcArApReport srcArApReport) {
    DBResultSet dbrs = null;
    Vector list = new Vector();
    try {
      String sql = "SELECT "
              + " A." + PstArApMain.fieldNames[PstArApMain.FLD_AMOUNT]
              + ", A." + PstArApMain.fieldNames[PstArApMain.FLD_ARAP_MAIN_ID]
              + ", A." + PstArApMain.fieldNames[PstArApMain.FLD_NOTA_DATE]
              + ", A." + PstArApMain.fieldNames[PstArApMain.FLD_VOUCHER_NO]
              + ", A." + PstArApMain.fieldNames[PstArApMain.FLD_DESCRIPTION]
              + ", A." + PstArApMain.fieldNames[PstArApMain.FLD_NOTA_NO]
              + ", C." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE]
              + ", SUM(B." + PstArApItem.fieldNames[PstArApItem.FLD_LEFT_TO_PAY] + ") AS LEFT_TO_PAY "
              + ", MIN(B." + PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE] + ") AS MIN_DATE "
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]
              + " FROM " + PstArApMain.TBL_ARAP_MAIN + " AS A "
              + " INNER JOIN " + PstArApItem.TBL_ARAP_ITEM + " AS B "
              + " ON A." + PstArApMain.fieldNames[PstArApMain.FLD_ARAP_MAIN_ID]
              + " = B." + PstArApItem.fieldNames[PstArApItem.FLD_ARAP_MAIN_ID]
              + " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " AS CL "
              + " ON CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + " = A." + PstArApMain.fieldNames[PstArApMain.FLD_CONTACT_ID]
              + " INNER JOIN " + PstCurrencyType.TBL_CURRENCY_TYPE + " AS C "
              + " ON A." + PstArApMain.fieldNames[PstArApMain.FLD_ID_CURRENCY] + " = C." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID];

      String where = " A." + PstArApMain.fieldNames[PstArApMain.FLD_ARAP_TYPE] + " = " + PstArApMain.TYPE_AP;

      if (srcArApReport.getContactName().length() > 0) {
        if (where.length() > 0) {
          where = where + " AND (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'"
                  + " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
        } else {
          where = " (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'"
                  + " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
        }
      }

      if (srcArApReport.getReportType() >= SessArApDetailReport.AR_TODAY_DUE_DATE) {
        if (where.length() > 0) {
          where = where + " AND B." + PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
        } else {
          where = " B." + PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
        }
      } else {
        if (where.length() > 0) {
          where = where + " AND A." + PstArApMain.fieldNames[PstArApMain.FLD_NOTA_DATE] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
        } else {
          where = " A." + PstArApMain.fieldNames[PstArApMain.FLD_NOTA_DATE] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
        }
      }

      if (srcArApReport.getNotaNo().length() > 0) {
        if (where.length() > 0) {
          where = where + " AND A." + PstArApMain.fieldNames[PstArApMain.FLD_VOUCHER_NO] + " LIKE '%" + srcArApReport.getNotaNo() + "%'";
        } else {
          where = " A." + PstArApMain.fieldNames[PstArApMain.FLD_VOUCHER_NO] + " LIKE '%" + srcArApReport.getNotaNo() + "%'";
        }
      }

      if (srcArApReport.getViewType() == 0) {
        if (where.length() > 0) {
          where += " AND B." + PstArApItem.fieldNames[PstArApItem.FLD_LEFT_TO_PAY] + " > 0 ";
        } else {
          where += PstArApItem.fieldNames[PstArApItem.FLD_LEFT_TO_PAY] + " > 0 ";
        }
      }

      if (where.length() > 0) {
        sql = sql + " WHERE " + where;
      }

      sql = sql + " GROUP BY "
              + " A." + PstArApMain.fieldNames[PstArApMain.FLD_AMOUNT]
              + ", A." + PstArApMain.fieldNames[PstArApMain.FLD_ARAP_MAIN_ID]
              + ", A." + PstArApMain.fieldNames[PstArApMain.FLD_NOTA_DATE]
              + ", A." + PstArApMain.fieldNames[PstArApMain.FLD_VOUCHER_NO]
              + ", A." + PstArApMain.fieldNames[PstArApMain.FLD_DESCRIPTION]
              + ", A." + PstArApMain.fieldNames[PstArApMain.FLD_NOTA_NO]
              + ", C." + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE]
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID];

      sql = sql + " ORDER BY CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]
              + ", A." + PstArApMain.fieldNames[PstArApMain.FLD_NOTA_DATE];

      System.out.println(sql);

      dbrs = DBHandler.execQueryResult(sql);
      ResultSet rs = dbrs.getResultSet();
      ArApDetail arap = new ArApDetail();
      Vector listItem = new Vector();
      double totNom = 0;
      double totPay = 0;
      while (rs.next()) {
        long contactId = rs.getLong(PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]);
        if (contactId != arap.getContactId()) {
          if (arap.getContactId() > 0) {
            arap.setDetail(listItem);
            arap.setTotalNominal(totNom);
            arap.setTotalPay(totPay);
            list.add(arap);
          }
          arap = new ArApDetail();
          listItem = new Vector();
          totNom = 0;
          totPay = 0;
          arap.setContactId(contactId);
          String compName = rs.getString(PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);
          String personName = rs.getString(PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]);
          String name = "";
          if (compName != null && compName.length() > 0) {
            name = compName;
          }
          if (personName != null && personName.length() > 0) {
            if (name.length() > 0) {
              name = name + " / " + personName;
            } else {
              name = personName;
            }
          }
          arap.setContactName(name);
        }
        ArApDetailItem item = new ArApDetailItem();
        item.setItemType(TYPE_ARAP_MAIN);
        item.setMainId(rs.getLong(PstArApMain.fieldNames[PstArApMain.FLD_ARAP_MAIN_ID]));
        item.setVoucherNo(rs.getString(PstArApMain.fieldNames[PstArApMain.FLD_VOUCHER_NO]));
        item.setVoucherDate(rs.getDate(PstArApMain.fieldNames[PstArApMain.FLD_NOTA_DATE]));
        item.setDueDate(rs.getDate("MIN_DATE"));
        item.setNominal(rs.getDouble(PstArApMain.fieldNames[PstArApMain.FLD_AMOUNT]));
        item.setDescription(rs.getString(PstArApMain.fieldNames[PstArApMain.FLD_DESCRIPTION]));
        item.setPayed(item.getNominal() - rs.getDouble("LEFT_TO_PAY"));
        item.setNotaNo(rs.getString(PstArApMain.fieldNames[PstArApMain.FLD_NOTA_NO]));
        item.setCurrCode(rs.getString(PstCurrencyType.fieldNames[PstCurrencyType.FLD_CODE]));
        totNom = totNom + item.getNominal();
        totPay = totPay + item.getPayed();
        listItem.add(item);
      }
      if (arap.getContactId() > 0) {
        arap.setDetail(listItem);
        arap.setTotalNominal(totNom);
        arap.setTotalPay(totPay);
        list.add(arap);
      }

    } catch (Exception e) {
      System.out.println("err.SessArApReport - listApOnArApMain " + e.toString());
    } finally {
      DBResultSet.close(dbrs);
    }
    return list;
  }

  public static Vector listArPayment(SrcArApReport srcArApReport) {
    DBResultSet dbrs = null;
    Vector list = new Vector();
    try {
      String sql = "SELECT "
              + " P." + PstArApPayment.fieldNames[PstArApPayment.FLD_AMOUNT]
              + ", P." + PstArApPayment.fieldNames[PstArApPayment.FLD_PAYMENT_NO]
              + ", P." + PstArApPayment.fieldNames[PstArApPayment.FLD_PAYMENT_DATE]
              + ", A." + PstArApMain.fieldNames[PstArApMain.FLD_NOTA_NO]
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]
              + " FROM " + PstArApPayment.TBL_ARAP_PAYMENT + " AS P "
              + " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " AS CL "
              + " ON CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + " = P." + PstArApPayment.fieldNames[PstArApPayment.FLD_CONTACT_ID]
              + " INNER JOIN " + PstArApMain.TBL_ARAP_MAIN + " AS A "
              + " ON P." + PstArApPayment.fieldNames[PstArApPayment.FLD_ARAP_MAIN_ID] + " = A." + PstArApMain.fieldNames[PstArApMain.FLD_ARAP_MAIN_ID];
      String where = "  P." + PstArApPayment.fieldNames[PstArApPayment.FLD_ARAP_TYPE] + " = " + PstArApMain.TYPE_AR;

      if (srcArApReport.getContactName().length() > 0) {
        if (where.length() > 0) {
          where = where + " AND (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'"
                  + " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
        } else {
          where = " (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'"
                  + " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
        }
      }

      if (where.length() > 0) {
        where = where + " AND P." + PstArApPayment.fieldNames[PstArApPayment.FLD_PAYMENT_DATE] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
      } else {
        where = " P." + PstArApPayment.fieldNames[PstArApPayment.FLD_PAYMENT_DATE] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
      }

      if (srcArApReport.getNotaNo().length() > 0) {
        if (where.length() > 0) {
          where = where + " AND P." + PstArApPayment.fieldNames[PstArApPayment.FLD_PAYMENT_NO] + " LIKE '%" + srcArApReport.getNotaNo() + "%'";
        } else {
          where = " P." + PstArApPayment.fieldNames[PstArApPayment.FLD_PAYMENT_NO] + " LIKE '%" + srcArApReport.getNotaNo() + "%'";
        }
      }

      if (where.length() > 0) {
        sql = sql + " WHERE " + where;
      }

      sql = sql + " ORDER BY CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]
              + ", A." + PstArApMain.fieldNames[PstArApMain.FLD_NOTA_NO]
              + ", P." + PstArApPayment.fieldNames[PstArApPayment.FLD_PAYMENT_NO];

      System.out.println(sql);

      dbrs = DBHandler.execQueryResult(sql);
      ResultSet rs = dbrs.getResultSet();
      ArApDetail arap = new ArApDetail();
      Vector listItem = new Vector();
      double totPay = 0;
      while (rs.next()) {
        long contactId = rs.getLong(PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]);
        if (contactId != arap.getContactId()) {
          if (arap.getContactId() > 0) {
            arap.setPayment(listItem);
            arap.setTotalPay(totPay);
            list.add(arap);
          }
          arap = new ArApDetail();
          listItem = new Vector();
          totPay = 0;
          arap.setContactId(contactId);
          String compName = rs.getString(PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);
          String personName = rs.getString(PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]);
          String name = "";
          if (compName != null && compName.length() > 0) {
            name = compName;
          }
          if (personName != null && personName.length() > 0) {
            if (name.length() > 0) {
              name = name + " / " + personName;
            } else {
              name = personName;
            }
          }
          arap.setContactName(name);
        }
        ArApDetailPayment item = new ArApDetailPayment();
        item.setPaymentNo(rs.getString(PstArApPayment.fieldNames[PstArApPayment.FLD_PAYMENT_NO]));
        item.setNotaNo(rs.getString(PstArApMain.fieldNames[PstArApMain.FLD_NOTA_NO]));
        item.setPaymentDate(rs.getDate(PstArApPayment.fieldNames[PstArApPayment.FLD_PAYMENT_DATE]));
        item.setNominal(rs.getDouble(PstArApPayment.fieldNames[PstArApPayment.FLD_AMOUNT]));

        totPay = totPay + item.getNominal();

        listItem.add(item);
      }
      if (arap.getContactId() > 0) {
        arap.setPayment(listItem);
        arap.setTotalPay(totPay);
        list.add(arap);
      }

    } catch (Exception e) {
      System.out.println("err.SessArApReport - listArPayment " + e.toString());
    } finally {
      DBResultSet.close(dbrs);
    }
    return list;
  }

  public static Vector listApPayment(SrcArApReport srcArApReport) {
    DBResultSet dbrs = null;
    Vector list = new Vector();
    try {
      String sql = "SELECT "
              + " P." + PstArApPayment.fieldNames[PstArApPayment.FLD_AMOUNT]
              + ", P." + PstArApPayment.fieldNames[PstArApPayment.FLD_PAYMENT_NO]
              + ", P." + PstArApPayment.fieldNames[PstArApPayment.FLD_PAYMENT_DATE]
              + ", A." + PstArApMain.fieldNames[PstArApMain.FLD_NOTA_NO]
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]
              + " FROM " + PstArApPayment.TBL_ARAP_PAYMENT + " AS P "
              + " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " AS CL "
              + " ON CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + " = P." + PstArApPayment.fieldNames[PstArApPayment.FLD_CONTACT_ID]
              + " INNER JOIN " + PstArApMain.TBL_ARAP_MAIN + " AS A "
              + " ON P." + PstArApPayment.fieldNames[PstArApPayment.FLD_ARAP_MAIN_ID] + " = A." + PstArApMain.fieldNames[PstArApMain.FLD_ARAP_MAIN_ID];
      String where = "  P." + PstArApPayment.fieldNames[PstArApPayment.FLD_ARAP_TYPE] + " = " + PstArApMain.TYPE_AP;

      if (srcArApReport.getContactName().length() > 0) {
        if (where.length() > 0) {
          where = where + " AND (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'"
                  + " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
        } else {
          where = " (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'"
                  + " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
        }
      }

      if (where.length() > 0) {
        where = where + " AND P." + PstArApPayment.fieldNames[PstArApPayment.FLD_PAYMENT_DATE] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
      } else {
        where = " P." + PstArApPayment.fieldNames[PstArApPayment.FLD_PAYMENT_DATE] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
      }

      if (srcArApReport.getNotaNo().length() > 0) {
        if (where.length() > 0) {
          where = where + " AND P." + PstArApPayment.fieldNames[PstArApPayment.FLD_PAYMENT_NO] + " LIKE '%" + srcArApReport.getNotaNo() + "%'";
        } else {
          where = " P." + PstArApPayment.fieldNames[PstArApPayment.FLD_PAYMENT_NO] + " LIKE '%" + srcArApReport.getNotaNo() + "%'";
        }
      }

      if (where.length() > 0) {
        sql = sql + " WHERE " + where;
      }

      sql = sql + " ORDER BY CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]
              + ", A." + PstArApMain.fieldNames[PstArApMain.FLD_NOTA_NO]
              + ", P." + PstArApPayment.fieldNames[PstArApPayment.FLD_PAYMENT_NO];

      System.out.println(sql);

      dbrs = DBHandler.execQueryResult(sql);
      ResultSet rs = dbrs.getResultSet();
      ArApDetail arap = new ArApDetail();
      Vector listItem = new Vector();
      double totPay = 0;
      while (rs.next()) {
        long contactId = rs.getLong(PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]);
        if (contactId != arap.getContactId()) {
          if (arap.getContactId() > 0) {
            arap.setPayment(listItem);
            arap.setTotalPay(totPay);
            list.add(arap);
          }
          arap = new ArApDetail();
          listItem = new Vector();
          totPay = 0;
          arap.setContactId(contactId);
          String compName = rs.getString(PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);
          String personName = rs.getString(PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]);
          String name = "";
          if (compName != null && compName.length() > 0) {
            name = compName;
          }
          if (personName != null && personName.length() > 0) {
            if (name.length() > 0) {
              name = name + " / " + personName;
            } else {
              name = personName;
            }
          }
          arap.setContactName(name);
        }
        ArApDetailPayment item = new ArApDetailPayment();
        item.setPaymentNo(rs.getString(PstArApPayment.fieldNames[PstArApPayment.FLD_PAYMENT_NO]));
        item.setNotaNo(rs.getString(PstArApMain.fieldNames[PstArApMain.FLD_NOTA_NO]));
        item.setPaymentDate(rs.getDate(PstArApPayment.fieldNames[PstArApPayment.FLD_PAYMENT_DATE]));
        item.setNominal(rs.getDouble(PstArApPayment.fieldNames[PstArApPayment.FLD_AMOUNT]));

        totPay = totPay + item.getNominal();

        listItem.add(item);
      }
      if (arap.getContactId() > 0) {
        arap.setPayment(listItem);
        arap.setTotalPay(totPay);
        list.add(arap);
      }

    } catch (Exception e) {
      System.out.println("err.SessArApReport - listApPayment " + e.toString());
    } finally {
      DBResultSet.close(dbrs);
    }
    return list;
  }

    // todo AR/AP Aging
  public static Vector listDpAgingOrderAktiva(SrcArApReport srcArApReport) {
    DBResultSet dbrs = null;
    Vector list = new Vector();
    try {
      String sql = "SELECT "
              + " A." + PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_DOWN_PAYMENT]
              + ", A." + PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_TANGGAL_ORDER]
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]
              + " FROM " + PstOrderAktiva.TBL_ORDER_AKTIVA + " AS A "
              + " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " AS CL "
              + " ON CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + " = A." + PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_SUPPLIER_ID]
              + " LEFT JOIN " + PstReceiveAktiva.TBL_RECEIVE_AKTIVA + " AS R "
              + " ON A." + PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_ORDER_AKTIVA_ID]
              + " = R." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_ORDER_AKTIVA_ID];

      String where = " A." + PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_DOWN_PAYMENT] + " > 0 "
              + " AND R." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_RECEIVE_AKTIVA_ID] + " = 0";

      if (srcArApReport.getContactName().length() > 0) {
        if (where.length() > 0) {
          where = where + " AND (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'"
                  + " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
        } else {
          where = " (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'"
                  + " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
        }
      }

      if (where.length() > 0) {
        sql = sql + " WHERE " + where;
      }

      sql = sql + " ORDER BY CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]
              + ", A." + PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_TANGGAL_ORDER];

      System.out.println(sql);

      dbrs = DBHandler.execQueryResult(sql);
      ResultSet rs = dbrs.getResultSet();
      ArApAging arap = new ArApAging();
      ArApAging otherArAp = new ArApAging();
      while (rs.next()) {
        long contactId = rs.getLong(PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]);
        if (contactId != arap.getContactId()) {
          if (arap.getContactId() > 0) {
            list.add(arap);
          }
          arap = new ArApAging();
          arap.setContactId(contactId);
          String compName = rs.getString(PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);
          String personName = rs.getString(PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]);
          String name = "";
          if (compName != null && compName.length() > 0) {
            name = compName;
          }
          if (personName != null && personName.length() > 0) {
            if (name.length() > 0) {
              name = name + " / " + personName;
            } else {
              name = personName;
            }
          }
          arap.setContactName(name);

        }
        otherArAp = new ArApAging();
        Date dueDate = rs.getDate(PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_TANGGAL_ORDER]);
        double value = rs.getDouble(PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_DOWN_PAYMENT]);
        if (dueDate.before(srcArApReport.getThirdPeriodeOverDue())) {
          otherArAp.setOverPeriodeOverDueValue(value);
        } else if (dueDate.compareTo(srcArApReport.getThirdPeriodeOverDue()) >= 0 && dueDate.before(srcArApReport.getSecondPeriodeOverDue())) {
          otherArAp.setThirdPeriodeOverDueValue(value);
        } else if (dueDate.compareTo(srcArApReport.getSecondPeriodeOverDue()) >= 0 && dueDate.before(srcArApReport.getFirstPeriodeOverDue())) {
          otherArAp.setSecondPeriodeOverDueValue(value);
        } else if (dueDate.compareTo(srcArApReport.getFirstPeriodeOverDue()) >= 0 && dueDate.before(srcArApReport.getFromDate())) {
          otherArAp.setFirstPeriodeOverDueValue(value);
        } else if (dueDate.equals(srcArApReport.getFromDate())) {
          otherArAp.setTodayDueDateValue(value);
        } else if (dueDate.equals(srcArApReport.getTommorowDueDate())) {
          otherArAp.setTomorrowDueDateValue(value);
        } else if (dueDate.after(srcArApReport.getTommorowDueDate()) && dueDate.compareTo(srcArApReport.getFirstPeriodeDueDate()) <= 0) {
          otherArAp.setFirstPeriodeDueDateValue(value);
        } else if (dueDate.after(srcArApReport.getFirstPeriodeDueDate()) && dueDate.compareTo(srcArApReport.getSecondPeriodeDueDate()) <= 0) {
          otherArAp.setSecondPeriodeDueDateValue(value);
        } else if (dueDate.after(srcArApReport.getSecondPeriodeDueDate()) && dueDate.compareTo(srcArApReport.getThirdPeriodeDueDate()) <= 0) {
          otherArAp.setThirdPeriodeDueDateValue(value);
        } else if (dueDate.after(srcArApReport.getThirdPeriodeDueDate())) {
          otherArAp.setOverPeriodeDueDateValue(value);
        }
        arap.mergeByContactId(otherArAp);

      }
      if (arap.getContactId() > 0) {
        list.add(arap);
      }
    } catch (Exception e) {
      System.out.println("err.SessArApReport - listDpAgingOrderAktiva " + e.toString());
    } finally {
      DBResultSet.close(dbrs);
    }
    return list;
  }

  public static Vector listArAgingMain(SrcArApReport srcArApReport) {
    DBResultSet dbrs = null;
    Vector list = new Vector();
    try {
      String sql = "SELECT "
              + " B." + PstArApItem.fieldNames[PstArApItem.FLD_LEFT_TO_PAY]
              + ", B." + PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE]
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]
              + " FROM " + PstArApMain.TBL_ARAP_MAIN + " AS A "
              + " INNER JOIN " + PstArApItem.TBL_ARAP_ITEM + " AS B "
              + " ON A." + PstArApMain.fieldNames[PstArApMain.FLD_ARAP_MAIN_ID]
              + " = B." + PstArApItem.fieldNames[PstArApItem.FLD_ARAP_MAIN_ID]
              + " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " AS CL "
              + " ON CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + " = A." + PstArApMain.fieldNames[PstArApMain.FLD_CONTACT_ID];

      String where = " A." + PstArApMain.fieldNames[PstArApMain.FLD_ARAP_TYPE] + " = " + PstArApMain.TYPE_AR
              + "";//" AND B." + PstArApItem.fieldNames[PstArApItem.FLD_LEFT_TO_PAY] + " > 0 ";

      if (srcArApReport.getContactName().length() > 0) {
        if (where.length() > 0) {
          where = where + " AND (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'"
                  + " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
        } else {
          where = " (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'"
                  + " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
        }
      }

      if (where.length() > 0) {
        sql = sql + " WHERE " + where;
      }

      sql = sql + " ORDER BY CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]
              + ", B." + PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE];

      System.out.println(sql);

      dbrs = DBHandler.execQueryResult(sql);
      ResultSet rs = dbrs.getResultSet();
      ArApAging arap = new ArApAging();
      ArApAging otherArAp = new ArApAging();
      while (rs.next()) {
        long contactId = rs.getLong(PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]);
        if (contactId != arap.getContactId()) {
          if (arap.getContactId() > 0) {
            list.add(arap);
          }
          arap = new ArApAging();
          arap.setContactId(contactId);
          String compName = rs.getString(PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);
          String personName = rs.getString(PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]);
          String name = "";
          if (compName != null && compName.length() > 0) {
            name = compName;
          }
          if (personName != null && personName.length() > 0) {
            if (name.length() > 0) {
              name = name + " / " + personName;
            } else {
              name = personName;
            }
          }
          arap.setContactName(name);
        }
        otherArAp = new ArApAging();
        Date dueDate = rs.getDate(PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE]);
        double value = rs.getDouble(PstArApItem.fieldNames[PstArApItem.FLD_LEFT_TO_PAY]);
        if (dueDate.before(srcArApReport.getThirdPeriodeOverDue())) {
          otherArAp.setOverPeriodeOverDueValue(value);
        } else if (dueDate.compareTo(srcArApReport.getThirdPeriodeOverDue()) >= 0 && dueDate.before(srcArApReport.getSecondPeriodeOverDue())) {
          otherArAp.setThirdPeriodeOverDueValue(value);
        } else if (dueDate.compareTo(srcArApReport.getSecondPeriodeOverDue()) >= 0 && dueDate.before(srcArApReport.getFirstPeriodeOverDue())) {
          otherArAp.setSecondPeriodeOverDueValue(value);
        } else if (dueDate.compareTo(srcArApReport.getFirstPeriodeOverDue()) >= 0 && dueDate.before(srcArApReport.getFromDate())) {
          otherArAp.setFirstPeriodeOverDueValue(value);
        } else if (dueDate.equals(srcArApReport.getFromDate())) {
          otherArAp.setTodayDueDateValue(value);
        } else if (dueDate.equals(srcArApReport.getTommorowDueDate())) {
          otherArAp.setTomorrowDueDateValue(value);
        } else if (dueDate.after(srcArApReport.getTommorowDueDate()) && dueDate.compareTo(srcArApReport.getFirstPeriodeDueDate()) <= 0) {
          otherArAp.setFirstPeriodeDueDateValue(value);
        } else if (dueDate.after(srcArApReport.getFirstPeriodeDueDate()) && dueDate.compareTo(srcArApReport.getSecondPeriodeDueDate()) <= 0) {
          otherArAp.setSecondPeriodeDueDateValue(value);
        } else if (dueDate.after(srcArApReport.getSecondPeriodeDueDate()) && dueDate.compareTo(srcArApReport.getThirdPeriodeDueDate()) <= 0) {
          otherArAp.setThirdPeriodeDueDateValue(value);
        } else if (dueDate.after(srcArApReport.getThirdPeriodeDueDate())) {
          otherArAp.setOverPeriodeDueDateValue(value);
        }
        arap.mergeByContactId(otherArAp);

      }
      if (arap.getContactId() > 0) {
        list.add(arap);
      }

    } catch (Exception e) {
      System.out.println("err.SessArApReport - listArAgingMain " + e.toString());
    } finally {
      DBResultSet.close(dbrs);
    }

    return list;
  }

  public static Vector listArAgingSelling(SrcArApReport srcArApReport) {
    DBResultSet dbrs = null;
    Vector list = new Vector();
    try {
      String sql = "SELECT "
              + " B." + PstArApItem.fieldNames[PstArApItem.FLD_LEFT_TO_PAY]
              + ", B." + PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE]
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]
              + " FROM " + PstSellingAktiva.TBL_SELLING_AKTIVA + " AS A "
              + " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " AS CL "
              + " ON CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + " = A." + PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_KONSUMEN_ID]
              + " INNER JOIN " + PstArApItem.TBL_ARAP_ITEM + " AS B "
              + " ON A." + PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_SELLING_AKTIVA_ID]
              + " = B." + PstArApItem.fieldNames[PstArApItem.FLD_SELLING_AKTIVA_ID];

      String where = " A." + PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_TYPE_PEMBAYARAN] + " = " + I_TransactionType.TIPE_TRANSACTION_KREDIT
              + "";//" AND B." + PstArApItem.fieldNames[PstArApItem.FLD_LEFT_TO_PAY] + " > 0 ";

      if (srcArApReport.getContactName().length() > 0) {
        if (where.length() > 0) {
          where = where + " AND (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'"
                  + " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
        } else {
          where = " (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'"
                  + " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
        }
      }

      if (where.length() > 0) {
        sql = sql + " WHERE " + where;
      }

      sql = sql + " ORDER BY CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]
              + ", B." + PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE];

      System.out.println(sql);

      dbrs = DBHandler.execQueryResult(sql);
      ResultSet rs = dbrs.getResultSet();
      ArApAging arap = new ArApAging();
      ArApAging otherArAp = new ArApAging();
      while (rs.next()) {
        long contactId = rs.getLong(PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]);
        if (contactId != arap.getContactId()) {
          if (arap.getContactId() > 0) {
            list.add(arap);
          }
          arap = new ArApAging();
          arap.setContactId(contactId);
          String compName = rs.getString(PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);
          String personName = rs.getString(PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]);
          String name = "";
          if (compName != null && compName.length() > 0) {
            name = compName;
          }
          if (personName != null && personName.length() > 0) {
            if (name.length() > 0) {
              name = name + " / " + personName;
            } else {
              name = personName;
            }
          }
          arap.setContactName(name);
        }

        otherArAp = new ArApAging();
        Date dueDate = rs.getDate(PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE]);
        double value = rs.getDouble(PstArApItem.fieldNames[PstArApItem.FLD_LEFT_TO_PAY]);
        if (dueDate.before(srcArApReport.getThirdPeriodeOverDue())) {
          otherArAp.setOverPeriodeOverDueValue(value);
        } else if (dueDate.compareTo(srcArApReport.getThirdPeriodeOverDue()) >= 0 && dueDate.before(srcArApReport.getSecondPeriodeOverDue())) {
          otherArAp.setThirdPeriodeOverDueValue(value);
        } else if (dueDate.compareTo(srcArApReport.getSecondPeriodeOverDue()) >= 0 && dueDate.before(srcArApReport.getFirstPeriodeOverDue())) {
          otherArAp.setSecondPeriodeOverDueValue(value);
        } else if (dueDate.compareTo(srcArApReport.getFirstPeriodeOverDue()) >= 0 && dueDate.before(srcArApReport.getFromDate())) {
          otherArAp.setFirstPeriodeOverDueValue(value);
        } else if (dueDate.equals(srcArApReport.getFromDate())) {
          otherArAp.setTodayDueDateValue(value);
        } else if (dueDate.equals(srcArApReport.getTommorowDueDate())) {
          otherArAp.setTomorrowDueDateValue(value);
        } else if (dueDate.after(srcArApReport.getTommorowDueDate()) && dueDate.compareTo(srcArApReport.getFirstPeriodeDueDate()) <= 0) {
          otherArAp.setFirstPeriodeDueDateValue(value);
        } else if (dueDate.after(srcArApReport.getFirstPeriodeDueDate()) && dueDate.compareTo(srcArApReport.getSecondPeriodeDueDate()) <= 0) {
          otherArAp.setSecondPeriodeDueDateValue(value);
        } else if (dueDate.after(srcArApReport.getSecondPeriodeDueDate()) && dueDate.compareTo(srcArApReport.getThirdPeriodeDueDate()) <= 0) {
          otherArAp.setThirdPeriodeDueDateValue(value);
        } else if (dueDate.after(srcArApReport.getThirdPeriodeDueDate())) {
          otherArAp.setOverPeriodeDueDateValue(value);
        }
        arap.mergeByContactId(otherArAp);
      }
      if (arap.getContactId() > 0) {
        list.add(arap);
      }

    } catch (Exception e) {
      System.out.println("err.SessArApReport - listArOnSellingAktiva " + e.toString());
    } finally {
      DBResultSet.close(dbrs);
    }

    return list;
  }

  public static Vector listApAgingMain(SrcArApReport srcArApReport) {
    DBResultSet dbrs = null;
    Vector list = new Vector();
    try {
      String sql = "SELECT "
              + " B." + PstArApItem.fieldNames[PstArApItem.FLD_LEFT_TO_PAY]
              + ", B." + PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE]
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]
              + " FROM " + PstArApMain.TBL_ARAP_MAIN + " AS A "
              + " INNER JOIN " + PstArApItem.TBL_ARAP_ITEM + " AS B "
              + " ON A." + PstArApMain.fieldNames[PstArApMain.FLD_ARAP_MAIN_ID]
              + " = B." + PstArApItem.fieldNames[PstArApItem.FLD_ARAP_MAIN_ID]
              + " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " AS CL "
              + " ON CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + " = A." + PstArApMain.fieldNames[PstArApMain.FLD_CONTACT_ID];

      String where = " A." + PstArApMain.fieldNames[PstArApMain.FLD_ARAP_TYPE] + " = " + PstArApMain.TYPE_AP
              + "";//"";//" AND B." + PstArApItem.fieldNames[PstArApItem.FLD_LEFT_TO_PAY] + " > 0 ";

      if (srcArApReport.getContactName().length() > 0) {
        if (where.length() > 0) {
          where = where + " AND (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'"
                  + " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
        } else {
          where = " (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'"
                  + " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
        }
      }

      if (where.length() > 0) {
        where = where + " AND B." + PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
      } else {
        where = " B." + PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
      }

      if (where.length() > 0) {
        sql = sql + " WHERE " + where;
      }

      sql = sql + " ORDER BY CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]
              + ", B." + PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE];

      System.out.println(sql);

      dbrs = DBHandler.execQueryResult(sql);
      ResultSet rs = dbrs.getResultSet();
      ArApAging arap = new ArApAging();
      ArApAging otherArAp = new ArApAging();
      while (rs.next()) {
        long contactId = rs.getLong(PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]);
        if (contactId != arap.getContactId()) {
          if (arap.getContactId() > 0) {
            list.add(arap);
          }
          arap = new ArApAging();
          arap.setContactId(contactId);
          String compName = rs.getString(PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);
          String personName = rs.getString(PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]);
          String name = "";
          if (compName != null && compName.length() > 0) {
            name = compName;
          }
          if (personName != null && personName.length() > 0) {
            if (name.length() > 0) {
              name = name + " / " + personName;
            } else {
              name = personName;
            }
          }
          arap.setContactName(name);
        }

        otherArAp = new ArApAging();
        Date dueDate = rs.getDate(PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE]);
        double value = rs.getDouble(PstArApItem.fieldNames[PstArApItem.FLD_LEFT_TO_PAY]);
        if (dueDate.before(srcArApReport.getThirdPeriodeOverDue())) {
          otherArAp.setOverPeriodeOverDueValue(value);
        } else if (dueDate.compareTo(srcArApReport.getThirdPeriodeOverDue()) >= 0 && dueDate.before(srcArApReport.getSecondPeriodeOverDue())) {
          otherArAp.setThirdPeriodeOverDueValue(value);
        } else if (dueDate.compareTo(srcArApReport.getSecondPeriodeOverDue()) >= 0 && dueDate.before(srcArApReport.getFirstPeriodeOverDue())) {
          otherArAp.setSecondPeriodeOverDueValue(value);
        } else if (dueDate.compareTo(srcArApReport.getFirstPeriodeOverDue()) >= 0 && dueDate.before(srcArApReport.getFromDate())) {
          otherArAp.setFirstPeriodeOverDueValue(value);
        } else if (dueDate.equals(srcArApReport.getFromDate())) {
          otherArAp.setTodayDueDateValue(value);
        } else if (dueDate.equals(srcArApReport.getTommorowDueDate())) {
          otherArAp.setTomorrowDueDateValue(value);
        } else if (dueDate.after(srcArApReport.getTommorowDueDate()) && dueDate.compareTo(srcArApReport.getFirstPeriodeDueDate()) <= 0) {
          otherArAp.setFirstPeriodeDueDateValue(value);
        } else if (dueDate.after(srcArApReport.getFirstPeriodeDueDate()) && dueDate.compareTo(srcArApReport.getSecondPeriodeDueDate()) <= 0) {
          otherArAp.setSecondPeriodeDueDateValue(value);
        } else if (dueDate.after(srcArApReport.getSecondPeriodeDueDate()) && dueDate.compareTo(srcArApReport.getThirdPeriodeDueDate()) <= 0) {
          otherArAp.setThirdPeriodeDueDateValue(value);
        } else if (dueDate.after(srcArApReport.getThirdPeriodeDueDate())) {
          otherArAp.setOverPeriodeDueDateValue(value);
        }
        arap.mergeByContactId(otherArAp);
      }
      if (arap.getContactId() > 0) {
        list.add(arap);
      }

    } catch (Exception e) {
      System.out.println("err.SessArApReport - listApAgingMain " + e.toString());
    } finally {
      DBResultSet.close(dbrs);
    }

    return list;
  }

  public static Vector listApAgingReceive(SrcArApReport srcArApReport) {
    DBResultSet dbrs = null;
    Vector list = new Vector();
    try {
      String sql = "SELECT "
              + " B." + PstArApItem.fieldNames[PstArApItem.FLD_LEFT_TO_PAY]
              + ", B." + PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE]
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]
              + ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]
              + " FROM " + PstReceiveAktiva.TBL_RECEIVE_AKTIVA + " AS A "
              + " INNER JOIN " + PstArApItem.TBL_ARAP_ITEM + " AS B "
              + " ON A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_RECEIVE_AKTIVA_ID]
              + " = B." + PstArApItem.fieldNames[PstArApItem.FLD_RECEIVE_AKTIVA_ID]
              + " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " AS CL "
              + " ON CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + " = A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_SUPPLIER_ID];

      String where = " A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_TYPE_PEMBAYARAN] + "=" + I_TransactionType.TIPE_TRANSACTION_KREDIT
              + "";//"";//" AND B." + PstArApItem.fieldNames[PstArApItem.FLD_LEFT_TO_PAY] + " > 0 ";

      if (srcArApReport.getContactName().length() > 0) {
        if (where.length() > 0) {
          where = where + " AND (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'"
                  + " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
        } else {
          where = " (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'"
                  + " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
        }
      }

      if (where.length() > 0) {
        where = where + " AND B." + PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
      } else {
        where = " B." + PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
      }

      if (where.length() > 0) {
        sql = sql + " WHERE " + where;
      }

      sql = sql + " ORDER BY CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]
              + ", B." + PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE];

      System.out.println(sql);

      dbrs = DBHandler.execQueryResult(sql);
      ResultSet rs = dbrs.getResultSet();
      ArApAging arap = new ArApAging();
      ArApAging otherArAp = new ArApAging();
      while (rs.next()) {
        long contactId = rs.getLong(PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]);
        if (contactId != arap.getContactId()) {
          if (arap.getContactId() > 0) {
            list.add(arap);
          }
          arap = new ArApAging();
          arap.setContactId(contactId);
          String compName = rs.getString(PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);
          String personName = rs.getString(PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]);
          String name = "";
          if (compName != null && compName.length() > 0) {
            name = compName;
          }
          if (personName != null && personName.length() > 0) {
            if (name.length() > 0) {
              name = name + " / " + personName;
            } else {
              name = personName;
            }
          }
          arap.setContactName(name);
        }
        otherArAp = new ArApAging();
        Date dueDate = rs.getDate(PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE]);
        double value = rs.getDouble(PstArApItem.fieldNames[PstArApItem.FLD_LEFT_TO_PAY]);
        if (dueDate.before(srcArApReport.getThirdPeriodeOverDue())) {
          otherArAp.setOverPeriodeOverDueValue(value);
        } else if (dueDate.compareTo(srcArApReport.getThirdPeriodeOverDue()) >= 0 && dueDate.before(srcArApReport.getSecondPeriodeOverDue())) {
          otherArAp.setThirdPeriodeOverDueValue(value);
        } else if (dueDate.compareTo(srcArApReport.getSecondPeriodeOverDue()) >= 0 && dueDate.before(srcArApReport.getFirstPeriodeOverDue())) {
          otherArAp.setSecondPeriodeOverDueValue(value);
        } else if (dueDate.compareTo(srcArApReport.getFirstPeriodeOverDue()) >= 0 && dueDate.before(srcArApReport.getFromDate())) {
          otherArAp.setFirstPeriodeOverDueValue(value);
        } else if (dueDate.equals(srcArApReport.getFromDate())) {
          otherArAp.setTodayDueDateValue(value);
        } else if (dueDate.equals(srcArApReport.getTommorowDueDate())) {
          otherArAp.setTomorrowDueDateValue(value);
        } else if (dueDate.after(srcArApReport.getTommorowDueDate()) && dueDate.compareTo(srcArApReport.getFirstPeriodeDueDate()) <= 0) {
          otherArAp.setFirstPeriodeDueDateValue(value);
        } else if (dueDate.after(srcArApReport.getFirstPeriodeDueDate()) && dueDate.compareTo(srcArApReport.getSecondPeriodeDueDate()) <= 0) {
          otherArAp.setSecondPeriodeDueDateValue(value);
        } else if (dueDate.after(srcArApReport.getSecondPeriodeDueDate()) && dueDate.compareTo(srcArApReport.getThirdPeriodeDueDate()) <= 0) {
          otherArAp.setThirdPeriodeDueDateValue(value);
        } else if (dueDate.after(srcArApReport.getThirdPeriodeDueDate())) {
          otherArAp.setOverPeriodeDueDateValue(value);
        }
        arap.mergeByContactId(otherArAp);
      }
      if (arap.getContactId() > 0) {
        list.add(arap);
      }

    } catch (Exception e) {
      System.out.println("err.SessArApReport - listApAgingReceive " + e.toString());
    } finally {
      DBResultSet.close(dbrs);
    }
    return list;
  }

  //todo
  public static Vector listArReport(SrcArApReport srcArApReport) {
    Hashtable hash = new Hashtable();
    updateHashFromVector(hash, SessArApDetailReport.listDpOnOrderAktiva(srcArApReport));
    updateHashFromVector(hash, SessArApDetailReport.listArOnArApMain(srcArApReport));
    updateHashFromVector(hash, SessArApDetailReport.listArOnSellingAktiva(srcArApReport));
    return new Vector(hash.values());
  }

  public static Vector listApReport(SrcArApReport srcArApReport) {
    Hashtable hash = new Hashtable();
    updateHashFromVector(hash, SessArApDetailReport.listApOnArApMain(srcArApReport));
    updateHashFromVector(hash, SessArApDetailReport.listApOnReceiveAktiva(srcArApReport));
    return new Vector(hash.values());
  }

  public static Vector listArPaymentReport(SrcArApReport srcArApReport) {
    Hashtable hash = new Hashtable();
    updateHashFromVector(hash, SessArApDetailReport.listDpOnRecieveAktiva(srcArApReport));
    updateHashFromVector(hash, SessArApDetailReport.listArPayment(srcArApReport));
    return new Vector(hash.values());
  }

  public static Vector listApPaymentReport(SrcArApReport srcArApReport) {
    Hashtable hash = new Hashtable();
    updateHashFromVector(hash, SessArApDetailReport.listApPayment(srcArApReport));
    return new Vector(hash.values());
  }

  public static Vector listArAging(SrcArApReport srcArApReport) {
    Hashtable hash = new Hashtable();
    updateHashFromVectorByMerge(hash, SessArApDetailReport.listDpAgingOrderAktiva(srcArApReport));
    updateHashFromVectorByMerge(hash, SessArApDetailReport.listArAgingMain(srcArApReport));
    updateHashFromVectorByMerge(hash, SessArApDetailReport.listArAgingSelling(srcArApReport));
    return new Vector(hash.values());
  }

  public static Vector listApAging(SrcArApReport srcArApReport) {
    Hashtable hash = new Hashtable();
    updateHashFromVectorByMerge(hash, SessArApDetailReport.listApAgingMain(srcArApReport));
    updateHashFromVectorByMerge(hash, SessArApDetailReport.listApAgingReceive(srcArApReport));
    return new Vector(hash.values());
  }

  private static void updateHashFromVector(Hashtable hash, Vector vect) {
    if (hash != null && vect != null) {
      int size = vect.size();
      ArApDetail objVect = new ArApDetail();
      ArApDetail objHash = new ArApDetail();
      for (int i = 0; i < size; i++) {
        objVect = (ArApDetail) vect.get(i);
        if (hash.containsKey("" + objVect.getContactId())) {
          objHash = (ArApDetail) hash.get("" + objVect.getContactId());
          objHash.getDetail().addAll((Collection) objVect.getDetail());
          objHash.getPayment().addAll((Collection) objVect.getPayment());
          objHash.setTotalNominal(objHash.getTotalNominal() + objVect.getTotalNominal());
          objHash.setTotalPay(objHash.getTotalPay() + objVect.getTotalPay());
        } else {
          hash.put("" + objVect.getContactId(), objVect);
        }
      }
    }
  }

  private static void updateHashFromVectorByMerge(Hashtable hash, Vector vect) {
    if (hash != null && vect != null) {
      int size = vect.size();
      ArApAging objVect = new ArApAging();
      ArApAging objHash = new ArApAging();
      for (int i = 0; i < size; i++) {
        objVect = (ArApAging) vect.get(i);
        if (hash.containsKey("" + objVect.getContactId())) {
          objHash = (ArApAging) hash.get("" + objVect.getContactId());
          objHash.mergeByContactId(objVect);
        } else {
          hash.put("" + objVect.getContactId(), objVect);
        }
      }
    }
  }

}

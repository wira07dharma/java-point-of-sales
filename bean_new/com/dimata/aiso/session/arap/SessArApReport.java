package com.dimata.aiso.session.arap;

import com.dimata.aiso.db.DBResultSet;
import com.dimata.aiso.db.DBHandler;
import com.dimata.aiso.entity.arap.PstArApMain;
import com.dimata.aiso.entity.arap.PstArApPayment;
import com.dimata.aiso.entity.arap.PstArApItem;
import com.dimata.aiso.entity.search.SrcArApReport;
import com.dimata.aiso.entity.aktiva.*;
import com.dimata.aiso.entity.report.ArApPerContact;
import com.dimata.aiso.entity.report.ArApPerDueDate;
import com.dimata.common.entity.contact.PstContactList;
import com.dimata.util.Formater;
import com.dimata.interfaces.trantype.I_TransactionType;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Date;
import java.sql.ResultSet;

/**
 * User: pulantara
 * Date: Oct 21, 2005
 * Time: 2:33:41 PM
 * Description:
 */
public class SessArApReport {
    // nama http session
    public static final String SESS_SEARCH_ARAP_REPORT = "SESS_SEARCH_ARAP_REPORT";
    public static final String SESS_SEARCH_ARAP = "SESS_SEARCH_ARAP";

    // report type
    public static final int AR_REPORT_PER_DEBITOR = 0;
    public static final int AP_REPORT_PER_CREDITOR = 1;
    public static final int ARAP_REPORT_PER_DUE_DATE = 2;

    public static final String[][] stReportType = {
        {
            "Daftar Piutang per Debitur",
            "Daftar Hutang per Kreditur",
            "Hutang dan Piutang per Jatuh Tempo"
        },
        {
            "Receivable per Debitor Report",
            "Payable per Creditor Report",
            "Receivable and Payable per Due Date"
        }
    };

    public static Vector listDpOnOrderAktiva(SrcArApReport srcArApReport) {
        DBResultSet dbrs = null;
        Vector list = new Vector();
        try {
            String sql = "SELECT " +
                    " SUM(A." + PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_DOWN_PAYMENT] + ") TOTAL" +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] +
                    " FROM " + PstOrderAktiva.TBL_ORDER_AKTIVA + " AS A " +
                    " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " AS CL " +
                    " ON CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + " = A." + PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_SUPPLIER_ID];

            String where = " A." + PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_DOWN_PAYMENT] + " > 0 ";

            if (srcArApReport.getContactName().length() > 0) {
                if (where.length() > 0) {
                    where = where + " AND (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'" +
                            " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
                } else {
                    where = " (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'" +
                            " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
                }
            }

            if (where.length() > 0) {
                where = where + " AND A." + PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_TANGGAL_ORDER] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
            } else {
                where = " A." + PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_TANGGAL_ORDER] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
            }

            if (where.length() > 0)
                sql = sql + " WHERE " + where;

            sql = sql + " GROUP BY " +
                    " CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID];

            sql = sql + " ORDER BY CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME];

            System.out.println(sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                ArApPerContact arap = new ArApPerContact();
                arap.setContactId(rs.getLong(PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]));
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
                arap.setIncrement(rs.getDouble("TOTAL"));

                list.add(arap);
            }

        } catch (Exception e) {
            System.out.println("err.SessArApReport - listDpOnOrderAktiva " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }

    public static Vector listPrevDpOnOrderAktiva(SrcArApReport srcArApReport) {
        DBResultSet dbrs = null;
        Vector list = new Vector();
        try {
            String sql = "SELECT " +
                    " SUM(A." + PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_DOWN_PAYMENT] + ") TOTAL" +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] +
                    " FROM " + PstOrderAktiva.TBL_ORDER_AKTIVA + " AS A " +
                    " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " AS CL " +
                    " ON CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + " = A." + PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_SUPPLIER_ID];

            String where = " A." + PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_DOWN_PAYMENT] + " > 0 ";

            if (srcArApReport.getContactName().length() > 0) {
                if (where.length() > 0) {
                    where = where + " AND (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'" +
                            " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
                } else {
                    where = " (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'" +
                            " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
                }
            }

            if (where.length() > 0) {
                where = where + " AND A." + PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_TANGGAL_ORDER] + " < '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "'";
            } else {
                where = " A." + PstOrderAktiva.fieldNames[PstOrderAktiva.FLD_TANGGAL_ORDER] + " < '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "'";
            }

            if (where.length() > 0)
                sql = sql + " WHERE " + where;

            sql = sql + " GROUP BY " +
                    " CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID];

            sql = sql + " ORDER BY CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME];

            System.out.println(sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                ArApPerContact arap = new ArApPerContact();
                arap.setContactId(rs.getLong(PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]));
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
                arap.setPrevBalance(rs.getDouble("TOTAL"));

                list.add(arap);
            }

        } catch (Exception e) {
            System.out.println("err.SessArApReport - listPrevDpOnOrderAktiva " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }

    public static Vector listDpOnRecieveAktiva(SrcArApReport srcArApReport) {
        DBResultSet dbrs = null;
        Vector list = new Vector();
        try {
            String sql = "SELECT " +
                    " SUM(A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_DOWN_PAYMENT] + ") TOTAL" +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] +
                    " FROM " + PstReceiveAktiva.TBL_RECEIVE_AKTIVA + " AS A " +
                    " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " AS CL " +
                    " ON CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + " = A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_SUPPLIER_ID];

            String where = " A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_DOWN_PAYMENT] + " > 0 ";

            if (srcArApReport.getContactName().length() > 0) {
                if (where.length() > 0) {
                    where = where + " AND (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'" +
                            " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
                } else {
                    where = " (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'" +
                            " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
                }
            }

            if (where.length() > 0) {
                where = where + " AND A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_TANGGAL_RECEIVE] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
            } else {
                where = " A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_TANGGAL_RECEIVE] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
            }

            if (where.length() > 0)
                sql = sql + " WHERE " + where;

            sql = sql + " GROUP BY " +
                    " CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID];

            sql = sql + " ORDER BY CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME];

            System.out.println(sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                ArApPerContact arap = new ArApPerContact();
                arap.setContactId(rs.getLong(PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]));
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
                arap.setDecrement(rs.getDouble("TOTAL"));

                list.add(arap);
            }

        } catch (Exception e) {
            System.out.println("err.SessArApReport - listDpOnRecieveAktiva " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }

    public static Vector listPrevDpOnRecieveAktiva(SrcArApReport srcArApReport) {
        DBResultSet dbrs = null;
        Vector list = new Vector();
        try {
            String sql = "SELECT " +
                    " SUM(A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_DOWN_PAYMENT] + ") TOTAL" +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] +
                    " FROM " + PstReceiveAktiva.TBL_RECEIVE_AKTIVA + " AS A " +
                    " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " AS CL " +
                    " ON CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + " = A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_SUPPLIER_ID];

            String where = " A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_DOWN_PAYMENT] + " > 0 ";

            if (srcArApReport.getContactName().length() > 0) {
                if (where.length() > 0) {
                    where = where + " AND (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'" +
                            " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
                } else {
                    where = " (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'" +
                            " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
                }
            }

            if (where.length() > 0) {
                where = where + " AND A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_TANGGAL_RECEIVE] + " < '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "'";
            } else {
                where = " A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_TANGGAL_RECEIVE] + " < '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "'";
            }

            if (where.length() > 0)
                sql = sql + " WHERE " + where;

            sql = sql + " GROUP BY " +
                    " CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID];

            sql = sql + " ORDER BY CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME];

            System.out.println(sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                ArApPerContact arap = new ArApPerContact();
                arap.setContactId(rs.getLong(PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]));
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
                arap.setPrevBalance(-(rs.getDouble("TOTAL")));

                list.add(arap);
            }

        } catch (Exception e) {
            System.out.println("err.SessArApReport - listPrevDpOnRecieveAktiva " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }

    public static Vector listApOnReceiveAktiva(SrcArApReport srcArApReport) {
        DBResultSet dbrs = null;
        Vector list = new Vector();
        try {
            String sql = "SELECT " +
                    " SUM(I." + PstReceiveAktivaItem.fieldNames[PstReceiveAktivaItem.FLD_TOTAL_PRICE] + ") AS TOTAL " +
                    ", SUM(A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_DOWN_PAYMENT] + ") AS TOTAL_DP " +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] +
                    " FROM " + PstReceiveAktiva.TBL_RECEIVE_AKTIVA + " AS A " +
                    " INNER JOIN " + PstReceiveAktivaItem.TBL_RECEIVE_AKTIVA_ITEM + " AS I " +
                    " ON A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_RECEIVE_AKTIVA_ID] +
                    " = I." + PstReceiveAktivaItem.fieldNames[PstReceiveAktivaItem.FLD_RECEIVE_AKTIVA_ID] +
                    " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " AS CL " +
                    " ON CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + " = A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_SUPPLIER_ID];

            String where = " A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_TYPE_PEMBAYARAN] + "=" + I_TransactionType.TIPE_TRANSACTION_KREDIT;

            if (srcArApReport.getContactName().length() > 0) {
                if (where.length() > 0) {
                    where = where + " AND (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'" +
                            " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
                } else {
                    where = " (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'" +
                            " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
                }
            }

            if (where.length() > 0) {
                where = where + " AND A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_TANGGAL_RECEIVE] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
            } else {
                where = " A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_TANGGAL_RECEIVE] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
            }

            if (where.length() > 0)
                sql = sql + " WHERE " + where;

            sql = sql + " GROUP BY " +
                    " CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID];

            sql = sql + " ORDER BY CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME];

            System.out.println(sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                ArApPerContact arap = new ArApPerContact();
                arap.setContactId(rs.getLong(PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]));
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
                arap.setIncrement(rs.getDouble("TOTAL") - rs.getDouble("TOTAL_DP"));

                list.add(arap);
            }

        } catch (Exception e) {
            System.out.println("err.SessArApReport - listApOnRecieveAktiva " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }

    public static Vector listPrevApOnReceiveAktiva(SrcArApReport srcArApReport) {
        DBResultSet dbrs = null;
        Vector list = new Vector();
        try {
            String sql = "SELECT " +
                    " SUM(I." + PstReceiveAktivaItem.fieldNames[PstReceiveAktivaItem.FLD_TOTAL_PRICE] + ") AS TOTAL " +
                    ", SUM(A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_DOWN_PAYMENT] + ") AS TOTAL_DP " +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] +
                    " FROM " + PstReceiveAktiva.TBL_RECEIVE_AKTIVA + " AS A " +
                    " INNER JOIN " + PstReceiveAktivaItem.TBL_RECEIVE_AKTIVA_ITEM + " AS I " +
                    " ON A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_RECEIVE_AKTIVA_ID] +
                    " = I." + PstReceiveAktivaItem.fieldNames[PstReceiveAktivaItem.FLD_RECEIVE_AKTIVA_ID] +
                    " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " AS CL " +
                    " ON CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + " = A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_SUPPLIER_ID];

            String where = " A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_TYPE_PEMBAYARAN] + "=" + I_TransactionType.TIPE_TRANSACTION_KREDIT;

            if (srcArApReport.getContactName().length() > 0) {
                if (where.length() > 0) {
                    where = where + " AND (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'" +
                            " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
                } else {
                    where = " (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'" +
                            " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
                }
            }

            if (where.length() > 0) {
                where = where + " AND A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_TANGGAL_RECEIVE] + " < '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "'";
            } else {
                where = " A." + PstReceiveAktiva.fieldNames[PstReceiveAktiva.FLD_TANGGAL_RECEIVE] + " < '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "'";
            }

            if (where.length() > 0)
                sql = sql + " WHERE " + where;

            sql = sql + " GROUP BY " +
                    " CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID];

            sql = sql + " ORDER BY CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME];

            System.out.println(sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                ArApPerContact arap = new ArApPerContact();
                arap.setContactId(rs.getLong(PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]));
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
                arap.setPrevBalance(rs.getDouble("TOTAL") - rs.getDouble("TOTAL_DP"));

                list.add(arap);
            }

        } catch (Exception e) {
            System.out.println("err.SessArApReport - listPrevApOnRecieveAktiva " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }

    public static Vector listArOnSellingAktiva(SrcArApReport srcArApReport) {
        DBResultSet dbrs = null;
        Vector list = new Vector();
        try {
            String sql = "SELECT " +
                    " SUM(I." + PstSellingAktivaItem.fieldNames[PstSellingAktivaItem.FLD_TOTAL_PRICE] + ") AS TOTAL" +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] +
                    " FROM " + PstSellingAktiva.TBL_SELLING_AKTIVA + " AS A " +
                    " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " AS CL " +
                    " ON CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + " = A." + PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_KONSUMEN_ID] +
                    " INNER JOIN " + PstSellingAktivaItem.TBL_SELLING_AKTIVA_ITEM + " AS I " +
                    " ON A." + PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_SELLING_AKTIVA_ID] +
                    " = I." + PstSellingAktivaItem.fieldNames[PstSellingAktivaItem.FLD_SELLING_AKTIVA_ID];

            String where = " A." + PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_TYPE_PEMBAYARAN] + " = " + I_TransactionType.TIPE_TRANSACTION_KREDIT;

            if (srcArApReport.getContactName().length() > 0) {
                if (where.length() > 0) {
                    where = where + " AND (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'" +
                            " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
                } else {
                    where = " (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'" +
                            " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
                }
            }

            if (where.length() > 0) {
                where = where + " AND A." + PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_TANGGAL_SELLING] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
            } else {
                where = " A." + PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_TANGGAL_SELLING] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
            }

            if (where.length() > 0)
                sql = sql + " WHERE " + where;

            sql = sql + " GROUP BY " +
                    " CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID];

            sql = sql + " ORDER BY CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME];

            System.out.println(sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                ArApPerContact arap = new ArApPerContact();
                arap.setContactId(rs.getLong(PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]));
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
                arap.setIncrement(rs.getDouble("TOTAL"));

                list.add(arap);
            }

        } catch (Exception e) {
            System.out.println("err.SessArApReport - listArOnSellingAktiva " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }

    public static Vector listPrevArOnSellingAktiva(SrcArApReport srcArApReport) {
        DBResultSet dbrs = null;
        Vector list = new Vector();
        try {
            String sql = "SELECT " +
                    " SUM(I." + PstSellingAktivaItem.fieldNames[PstSellingAktivaItem.FLD_TOTAL_PRICE] + ") AS TOTAL" +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] +
                    " FROM " + PstSellingAktiva.TBL_SELLING_AKTIVA + " AS A " +
                    " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " AS CL " +
                    " ON CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + " = A." + PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_KONSUMEN_ID] +
                    " INNER JOIN " + PstSellingAktivaItem.TBL_SELLING_AKTIVA_ITEM + " AS I " +
                    " ON A." + PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_SELLING_AKTIVA_ID] +
                    " = I." + PstSellingAktivaItem.fieldNames[PstSellingAktivaItem.FLD_SELLING_AKTIVA_ID];

            String where = " A." + PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_TYPE_PEMBAYARAN] + " = " + I_TransactionType.TIPE_TRANSACTION_KREDIT;

            if (srcArApReport.getContactName().length() > 0) {
                if (where.length() > 0) {
                    where = where + " AND (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'" +
                            " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
                } else {
                    where = " (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'" +
                            " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
                }
            }

            if (where.length() > 0) {
                where = where + " AND A." + PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_TANGGAL_SELLING] + " < '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "'";
            } else {
                where = " A." + PstSellingAktiva.fieldNames[PstSellingAktiva.FLD_TANGGAL_SELLING] + " < '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "'";
            }

            if (where.length() > 0)
                sql = sql + " WHERE " + where;

            sql = sql + " GROUP BY " +
                    " CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID];

            sql = sql + " ORDER BY CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME];

            System.out.println(sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                ArApPerContact arap = new ArApPerContact();
                arap.setContactId(rs.getLong(PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]));
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
                arap.setPrevBalance(rs.getDouble("TOTAL"));

                list.add(arap);
            }

        } catch (Exception e) {
            System.out.println("err.SessArApReport - listPrevArOnSellingAktiva " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }

    public static Vector listArOnArApMain(SrcArApReport srcArApReport) {
        DBResultSet dbrs = null;
        Vector list = new Vector();
        try {
            String sql = "SELECT " +
                    " SUM( A." + PstArApMain.fieldNames[PstArApMain.FLD_AMOUNT] + ") AS TOTAL " +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] +
                    " FROM " + PstArApMain.TBL_ARAP_MAIN + " AS A " +
                    " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " AS CL " +
                    " ON CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + " = A." + PstArApMain.fieldNames[PstArApMain.FLD_CONTACT_ID];

            String where = " A." + PstArApMain.fieldNames[PstArApMain.FLD_ARAP_TYPE] + " = " + PstArApMain.TYPE_AR;

            if (srcArApReport.getContactName().length() > 0) {
                if (where.length() > 0) {
                    where = where + " AND (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'" +
                            " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
                } else {
                    where = " (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'" +
                            " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
                }
            }

            if (where.length() > 0) {
                where = where + " AND A." + PstArApMain.fieldNames[PstArApMain.FLD_NOTA_DATE] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
            } else {
                where = " A." + PstArApMain.fieldNames[PstArApMain.FLD_NOTA_DATE] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
            }

            if (where.length() > 0)
                sql = sql + " WHERE " + where;

            sql = sql + " GROUP BY " +
                    " CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID];

            sql = sql + " ORDER BY CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME];

            System.out.println(sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                ArApPerContact arap = new ArApPerContact();
                arap.setContactId(rs.getLong(PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]));
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
                arap.setIncrement(rs.getDouble("TOTAL"));

                list.add(arap);
            }

        } catch (Exception e) {
            System.out.println("err.SessArApReport - listArOnArApMain " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }

    public static Vector listPrevArOnArApMain(SrcArApReport srcArApReport) {
        DBResultSet dbrs = null;
        Vector list = new Vector();
        try {
            String sql = "SELECT " +
                    " SUM( A." + PstArApMain.fieldNames[PstArApMain.FLD_AMOUNT] + ") AS TOTAL " +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] +
                    " FROM " + PstArApMain.TBL_ARAP_MAIN + " AS A " +
                    " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " AS CL " +
                    " ON CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + " = A." + PstArApMain.fieldNames[PstArApMain.FLD_CONTACT_ID];

            String where = " A." + PstArApMain.fieldNames[PstArApMain.FLD_ARAP_TYPE] + " = " + PstArApMain.TYPE_AR;

            if (srcArApReport.getContactName().length() > 0) {
                if (where.length() > 0) {
                    where = where + " AND (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'" +
                            " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
                } else {
                    where = " (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'" +
                            " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
                }
            }

            if (where.length() > 0) {
                where = where + " AND A." + PstArApMain.fieldNames[PstArApMain.FLD_NOTA_DATE] + " < '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "'";
            } else {
                where = " A." + PstArApMain.fieldNames[PstArApMain.FLD_NOTA_DATE] + "< '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "'";
            }

            if (where.length() > 0)
                sql = sql + " WHERE " + where;

            sql = sql + " GROUP BY " +
                    " CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID];

            sql = sql + " ORDER BY CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME];

            System.out.println(sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                ArApPerContact arap = new ArApPerContact();
                arap.setContactId(rs.getLong(PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]));
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
                arap.setPrevBalance(rs.getDouble("TOTAL"));

                list.add(arap);
            }

        } catch (Exception e) {
            System.out.println("err.SessArApReport - listPrevArOnArApMain " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }

    public static Vector listApOnArApMain(SrcArApReport srcArApReport) {
        DBResultSet dbrs = null;
        Vector list = new Vector();
        try {
            String sql = "SELECT " +
                    " SUM( A." + PstArApMain.fieldNames[PstArApMain.FLD_AMOUNT] + ") AS TOTAL " +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] +
                    " FROM " + PstArApMain.TBL_ARAP_MAIN + " AS A " +
                    " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " AS CL " +
                    " ON CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + " = A." + PstArApMain.fieldNames[PstArApMain.FLD_CONTACT_ID];

            String where = "  A." + PstArApMain.fieldNames[PstArApMain.FLD_ARAP_TYPE] + " = " + PstArApMain.TYPE_AP;

            if (srcArApReport.getContactName().length() > 0) {
                if (where.length() > 0) {
                    where = where + " AND (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'" +
                            " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
                } else {
                    where = " (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'" +
                            " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
                }
            }

            if (where.length() > 0) {
                where = where + " AND A." + PstArApMain.fieldNames[PstArApMain.FLD_NOTA_DATE] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
            } else {
                where = " A." + PstArApMain.fieldNames[PstArApMain.FLD_NOTA_DATE] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
            }

            if (where.length() > 0)
                sql = sql + " WHERE " + where;

            sql = sql + " GROUP BY " +
                    " CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID];

            sql = sql + " ORDER BY CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME];

            System.out.println(sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                ArApPerContact arap = new ArApPerContact();

                arap.setContactId(rs.getLong(PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]));
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
                arap.setIncrement(rs.getDouble("TOTAL"));


                list.add(arap);
            }

        } catch (Exception e) {
            System.out.println("err.SessArApReport - listApOnArApMain " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }

    public static Vector listPrevApOnArApMain(SrcArApReport srcArApReport) {
        DBResultSet dbrs = null;
        Vector list = new Vector();
        try {
            String sql = "SELECT " +
                    " SUM( A." + PstArApMain.fieldNames[PstArApMain.FLD_AMOUNT] + ") AS TOTAL " +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] +
                    " FROM " + PstArApMain.TBL_ARAP_MAIN + " AS A " +
                    " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " AS CL " +
                    " ON CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + " = A." + PstArApMain.fieldNames[PstArApMain.FLD_CONTACT_ID];

            String where = "  A." + PstArApMain.fieldNames[PstArApMain.FLD_ARAP_TYPE] + " = " + PstArApMain.TYPE_AP;

            if (srcArApReport.getContactName().length() > 0) {
                if (where.length() > 0) {
                    where = where + " AND (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'" +
                            " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
                } else {
                    where = " (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'" +
                            " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
                }
            }

            if (where.length() > 0) {
                where = where + " AND A." + PstArApMain.fieldNames[PstArApMain.FLD_NOTA_DATE] + " < '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "'";
            } else {
                where = " A." + PstArApMain.fieldNames[PstArApMain.FLD_NOTA_DATE] + " < '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "'";
            }

            if (where.length() > 0)
                sql = sql + " WHERE " + where;

            sql = sql + " GROUP BY " +
                    " CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID];

            sql = sql + " ORDER BY CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME];

            System.out.println(sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                ArApPerContact arap = new ArApPerContact();

                arap.setContactId(rs.getLong(PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]));
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
                arap.setPrevBalance(rs.getDouble("TOTAL"));


                list.add(arap);
            }

        } catch (Exception e) {
            System.out.println("err.SessArApReport - listPrevApOnArApMain " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }


    public static Vector listArPaymentOnArApMain(SrcArApReport srcArApReport) {
        DBResultSet dbrs = null;
        Vector list = new Vector();
        try {
            String sql = "SELECT " +
                    " SUM( P." + PstArApPayment.fieldNames[PstArApPayment.FLD_AMOUNT] + ") AS TOTAL " +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] +
                    " FROM " + PstArApPayment.TBL_ARAP_PAYMENT + " AS P " +
                    " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " AS CL " +
                    " ON CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + " = P." + PstArApPayment.fieldNames[PstArApPayment.FLD_CONTACT_ID];
            String where = "  P." + PstArApPayment.fieldNames[PstArApPayment.FLD_ARAP_TYPE] + " = " + PstArApMain.TYPE_AR;

            if (srcArApReport.getContactName().length() > 0) {
                if (where.length() > 0) {
                    where = where + " AND (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'" +
                            " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
                } else {
                    where = " (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'" +
                            " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
                }
            }

            if (where.length() > 0) {
                where = where + " AND P." + PstArApPayment.fieldNames[PstArApPayment.FLD_PAYMENT_DATE] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
            } else {
                where = " P." + PstArApPayment.fieldNames[PstArApPayment.FLD_PAYMENT_DATE] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
            }

            if (where.length() > 0)
                sql = sql + " WHERE " + where;

            sql = sql + " GROUP BY " +
                    " CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID];


            sql = sql + " ORDER BY CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME];

            System.out.println(sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                ArApPerContact arap = new ArApPerContact();
                arap.setContactId(rs.getLong(PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]));
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
                arap.setDecrement(rs.getDouble("TOTAL"));


                list.add(arap);
            }

        } catch (Exception e) {
            System.out.println("err.SessArApReport - listArPayment " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }

    public static Vector listPrevArPaymentOnArApMain(SrcArApReport srcArApReport) {
        DBResultSet dbrs = null;
        Vector list = new Vector();
        try {
            String sql = "SELECT " +
                    " SUM( P." + PstArApPayment.fieldNames[PstArApPayment.FLD_AMOUNT] + ") AS TOTAL " +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] +
                    " FROM " + PstArApPayment.TBL_ARAP_PAYMENT + " AS P " +
                    " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " AS CL " +
                    " ON CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + " = P." + PstArApPayment.fieldNames[PstArApPayment.FLD_CONTACT_ID];
            String where = "  P." + PstArApPayment.fieldNames[PstArApPayment.FLD_ARAP_TYPE] + " = " + PstArApMain.TYPE_AR;

            if (srcArApReport.getContactName().length() > 0) {
                if (where.length() > 0) {
                    where = where + " AND (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'" +
                            " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
                } else {
                    where = " (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'" +
                            " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
                }
            }

            if (where.length() > 0) {
                where = where + " AND P." + PstArApPayment.fieldNames[PstArApPayment.FLD_PAYMENT_DATE] + " < '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "'";
            } else {
                where = " P." + PstArApPayment.fieldNames[PstArApPayment.FLD_PAYMENT_DATE] + " < '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "'";
            }

            if (where.length() > 0)
                sql = sql + " WHERE " + where;

            sql = sql + " GROUP BY " +
                    " CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID];


            sql = sql + " ORDER BY CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME];

            System.out.println(sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                ArApPerContact arap = new ArApPerContact();
                arap.setContactId(rs.getLong(PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]));
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
                arap.setPrevBalance(-(rs.getDouble("TOTAL")));


                list.add(arap);
            }

        } catch (Exception e) {
            System.out.println("err.SessArApReport - listPrevArPaymentOnArApMain " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }


    public static Vector listApPaymentOnArApMain(SrcArApReport srcArApReport) {
        DBResultSet dbrs = null;
        Vector list = new Vector();
        try {
            String sql = "SELECT " +
                    " SUM( P." + PstArApPayment.fieldNames[PstArApPayment.FLD_AMOUNT] + ") AS TOTAL " +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] +
                    " FROM " + PstArApPayment.TBL_ARAP_PAYMENT + " AS P " +
                    " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " AS CL " +
                    " ON CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + " = P." + PstArApPayment.fieldNames[PstArApPayment.FLD_CONTACT_ID];
            String where = "  P." + PstArApPayment.fieldNames[PstArApPayment.FLD_ARAP_TYPE] + " = " + PstArApMain.TYPE_AP;

            if (srcArApReport.getContactName().length() > 0) {
                if (where.length() > 0) {
                    where = where + " AND (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'" +
                            " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
                } else {
                    where = " (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'" +
                            " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
                }
            }

            if (where.length() > 0) {
                where = where + " AND P." + PstArApPayment.fieldNames[PstArApPayment.FLD_PAYMENT_DATE] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
            } else {
                where = " P." + PstArApPayment.fieldNames[PstArApPayment.FLD_PAYMENT_DATE] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
            }

            if (where.length() > 0)
                sql = sql + " WHERE " + where;

            sql = sql + " GROUP BY " +
                    " CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID];


            sql = sql + " ORDER BY CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME];

            System.out.println(sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                ArApPerContact arap = new ArApPerContact();
                arap.setContactId(rs.getLong(PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]));
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
                arap.setDecrement(rs.getDouble("TOTAL"));


                list.add(arap);
            }

        } catch (Exception e) {
            System.out.println("err.SessArApReport - listArPayment " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }

    public static Vector listPrevApPaymentOnArApMain(SrcArApReport srcArApReport) {
        DBResultSet dbrs = null;
        Vector list = new Vector();
        try {
            String sql = "SELECT " +
                    " SUM( P." + PstArApPayment.fieldNames[PstArApPayment.FLD_AMOUNT] + ") AS TOTAL " +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] +
                    " FROM " + PstArApPayment.TBL_ARAP_PAYMENT + " AS P " +
                    " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " AS CL " +
                    " ON CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + " = P." + PstArApPayment.fieldNames[PstArApPayment.FLD_CONTACT_ID];
            String where = "  P." + PstArApPayment.fieldNames[PstArApPayment.FLD_ARAP_TYPE] + " = " + PstArApMain.TYPE_AP;

            if (srcArApReport.getContactName().length() > 0) {
                if (where.length() > 0) {
                    where = where + " AND (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'" +
                            " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
                } else {
                    where = " (CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%'" +
                            " OR CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] + " LIKE '%" + srcArApReport.getContactName() + "%') ";
                }
            }

            if (where.length() > 0) {
                where = where + " AND P." + PstArApPayment.fieldNames[PstArApPayment.FLD_PAYMENT_DATE] + " < '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "'";
            } else {
                where = " P." + PstArApPayment.fieldNames[PstArApPayment.FLD_PAYMENT_DATE] + " < '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "'";
            }

            if (where.length() > 0)
                sql = sql + " WHERE " + where;

            sql = sql + " GROUP BY " +
                    " CL." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME] +
                    ", CL." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID];


            sql = sql + " ORDER BY CL." + PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME];

            System.out.println(sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                ArApPerContact arap = new ArApPerContact();
                arap.setContactId(rs.getLong(PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]));
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
                arap.setPrevBalance(-(rs.getDouble("TOTAL")));


                list.add(arap);
            }

        } catch (Exception e) {
            System.out.println("err.SessArApReport - listPrevApPaymentOnArApMain " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }


    public static Vector listArReport(SrcArApReport srcArApReport) {
        Hashtable hash = new Hashtable();
        updateHashFromVector(hash, SessArApReport.listDpOnOrderAktiva(srcArApReport));
        updateHashFromVector(hash, SessArApReport.listPrevDpOnOrderAktiva(srcArApReport));
        updateHashFromVector(hash, SessArApReport.listDpOnRecieveAktiva(srcArApReport));
        updateHashFromVector(hash, SessArApReport.listPrevDpOnRecieveAktiva(srcArApReport));
        updateHashFromVector(hash, SessArApReport.listArOnArApMain(srcArApReport));
        updateHashFromVector(hash, SessArApReport.listPrevArOnArApMain(srcArApReport));
        updateHashFromVector(hash, SessArApReport.listArOnSellingAktiva(srcArApReport));
        updateHashFromVector(hash, SessArApReport.listPrevArOnSellingAktiva(srcArApReport));
        updateHashFromVector(hash, SessArApReport.listArPaymentOnArApMain(srcArApReport));
        updateHashFromVector(hash, SessArApReport.listPrevArPaymentOnArApMain(srcArApReport));
        return new Vector(hash.values());
    }

    public static Vector listApReport(SrcArApReport srcArApReport) {
        Hashtable hash = new Hashtable();
        updateHashFromVector(hash, SessArApReport.listApOnArApMain(srcArApReport));
        updateHashFromVector(hash, SessArApReport.listPrevApOnArApMain(srcArApReport));
        updateHashFromVector(hash, SessArApReport.listApOnReceiveAktiva(srcArApReport));
        updateHashFromVector(hash, SessArApReport.listPrevApOnReceiveAktiva(srcArApReport));
        updateHashFromVector(hash, SessArApReport.listApPaymentOnArApMain(srcArApReport));
        updateHashFromVector(hash, SessArApReport.listPrevApPaymentOnArApMain(srcArApReport));
        return new Vector(hash.values());
    }

    private static void updateHashFromVector(Hashtable hash, Vector vect) {
        if (hash != null && vect != null) {
            int size = vect.size();
            ArApPerContact objVect = new ArApPerContact();
            ArApPerContact objHash = new ArApPerContact();
            for (int i = 0; i < size; i++) {
                objVect = (ArApPerContact) vect.get(i);
                if (hash.containsKey("" + objVect.getContactId())) {
                    objHash = (ArApPerContact) hash.get("" + objVect.getContactId());
                    objHash.setDecrement(objHash.getDecrement() + objVect.getDecrement());
                    objHash.setIncrement(objHash.getIncrement() + objVect.getIncrement());
                    objHash.setPrevBalance(objHash.getPrevBalance() + objVect.getPrevBalance());
                } else {
                    hash.put("" + objVect.getContactId(), objVect);
                }
            }
        }
    }

    public static Vector listArPerDueDateMain(SrcArApReport srcArApReport) {
        DBResultSet dbrs = null;
        Vector list = new Vector();
        try {
            String sql = " SELECT I." + PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE] +
                    " , SUM(I." + PstArApItem.fieldNames[PstArApItem.FLD_LEFT_TO_PAY] + ") AS TOTAL" +
                    " FROM " + PstArApMain.TBL_ARAP_MAIN + " AS A " +
                    " INNER JOIN " + PstArApItem.TBL_ARAP_ITEM + " AS I " +
                    " ON A." + PstArApMain.fieldNames[PstArApMain.FLD_ARAP_MAIN_ID] +
                    " = I." + PstArApItem.fieldNames[PstArApItem.FLD_ARAP_MAIN_ID];

            String where = " A." + PstArApMain.fieldNames[PstArApMain.FLD_ARAP_TYPE] + " = " + PstArApMain.TYPE_AR +
                    " AND I." + PstArApItem.fieldNames[PstArApItem.FLD_LEFT_TO_PAY] + " > 0 ";

            if (where.length() > 0) {
                where = where + " AND I." + PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
            } else {
                where = " I." + PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
            }

            if (where.length() > 0)
                sql = sql + " WHERE " + where;

            sql = sql + " GROUP BY " +
                    " I." + PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE];

            sql = sql + " ORDER BY I." + PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE];
            ;

            System.out.println(sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                ArApPerDueDate arap = new ArApPerDueDate();
                arap.setDueDate(rs.getDate(PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE]));
                arap.setReceivable(rs.getDouble("TOTAL"));

                list.add(arap);
            }

        } catch (Exception e) {
            System.out.println("err.SessArApReport - listArAging " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }

    public static Vector listArPerDueDateSelling(SrcArApReport srcArApReport) {
        DBResultSet dbrs = null;
        Vector list = new Vector();
        try {
            String sql = " SELECT I." + PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE] +
                    " , SUM(I." + PstArApItem.fieldNames[PstArApItem.FLD_LEFT_TO_PAY] + ") AS TOTAL" +
                    " FROM " + PstArApItem.TBL_ARAP_ITEM + " AS I ";

            String where = " I." + PstArApItem.fieldNames[PstArApItem.FLD_SELLING_AKTIVA_ID] + " > 0 "+
                    " AND I." + PstArApItem.fieldNames[PstArApItem.FLD_LEFT_TO_PAY] + " > 0 ";

            if (where.length() > 0) {
                where = where + " AND I." + PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
            } else {
                where = " I." + PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
            }

            if (where.length() > 0)
                sql = sql + " WHERE " + where;

            sql = sql + " GROUP BY " +
                    " I." + PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE];

            sql = sql + " ORDER BY I." + PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE];
            ;

            System.out.println(sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                ArApPerDueDate arap = new ArApPerDueDate();
                arap.setDueDate(rs.getDate(PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE]));
                arap.setReceivable(rs.getDouble("TOTAL"));

                list.add(arap);
            }

        } catch (Exception e) {
            System.out.println("err.SessArApReport - listArPerDueDateSelling " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }

    public static Vector listApPerDueDateMain(SrcArApReport srcArApReport) {
        DBResultSet dbrs = null;
        Vector list = new Vector();
        try {
            String sql = " SELECT I." + PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE] +
                    " , SUM(I." + PstArApItem.fieldNames[PstArApItem.FLD_LEFT_TO_PAY] + ") AS TOTAL" +
                    " FROM " + PstArApMain.TBL_ARAP_MAIN + " AS A " +
                    " INNER JOIN " + PstArApItem.TBL_ARAP_ITEM + " AS I " +
                    " ON A." + PstArApMain.fieldNames[PstArApMain.FLD_ARAP_MAIN_ID] +
                    " = I." + PstArApItem.fieldNames[PstArApItem.FLD_ARAP_MAIN_ID];

            String where = " A." + PstArApMain.fieldNames[PstArApMain.FLD_ARAP_TYPE] + " = " + PstArApMain.TYPE_AP+
                    " AND I." + PstArApItem.fieldNames[PstArApItem.FLD_LEFT_TO_PAY] + " > 0 ";

            if (where.length() > 0) {
                where = where + " AND I." + PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
            } else {
                where = " I." + PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
            }

            if (where.length() > 0)
                sql = sql + " WHERE " + where;

            sql = sql + " GROUP BY " +
                    " I." + PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE];

            sql = sql + " ORDER BY I." + PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE];
            ;

            System.out.println(sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                ArApPerDueDate arap = new ArApPerDueDate();
                arap.setDueDate(rs.getDate(PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE]));
                arap.setPayable(rs.getDouble("TOTAL"));

                list.add(arap);
            }

        } catch (Exception e) {
            System.out.println("err.SessArApReport - listArOnArApMain " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }

    public static Vector listApPerDueDateReceive(SrcArApReport srcArApReport) {
        DBResultSet dbrs = null;
        Vector list = new Vector();
        try {
            String sql = " SELECT I." + PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE] +
                    " , SUM(I." + PstArApItem.fieldNames[PstArApItem.FLD_LEFT_TO_PAY] + ") AS TOTAL" +
                    " FROM " + PstArApItem.TBL_ARAP_ITEM + " AS I ";

            String where = " I." + PstArApItem.fieldNames[PstArApItem.FLD_RECEIVE_AKTIVA_ID] + " > 0 "+
                    " AND I." + PstArApItem.fieldNames[PstArApItem.FLD_LEFT_TO_PAY] + " > 0 ";

            if (where.length() > 0) {
                where = where + " AND I." + PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
            } else {
                where = " I." + PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE] + " BETWEEN '" + Formater.formatDate(srcArApReport.getFromDate(), "yyyy-MM-dd") + "' AND '" + Formater.formatDate(srcArApReport.getUntilDate(), "yyyy-MM-dd") + "'";
            }

            if (where.length() > 0)
                sql = sql + " WHERE " + where;

            sql = sql + " GROUP BY " +
                    " I." + PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE];

            sql = sql + " ORDER BY I." + PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE];
            ;

            System.out.println(sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                ArApPerDueDate arap = new ArApPerDueDate();
                arap.setDueDate(rs.getDate(PstArApItem.fieldNames[PstArApItem.FLD_DUE_DATE]));
                arap.setPayable(rs.getDouble("TOTAL"));

                list.add(arap);
            }

        } catch (Exception e) {
            System.out.println("err.SessArApReport - listArPerDueDateSelling " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return list;
    }

    public static Vector listArApPerDueDate(SrcArApReport srcArApReport) {
        Vector vect = new Vector();

        vect = shortJoinVectorArApPerDueDate(vect, SessArApReport.listArPerDueDateMain(srcArApReport));
        vect = shortJoinVectorArApPerDueDate(vect, SessArApReport.listApPerDueDateMain(srcArApReport));
        vect = shortJoinVectorArApPerDueDate(vect, SessArApReport.listArPerDueDateSelling(srcArApReport));
        vect = shortJoinVectorArApPerDueDate(vect, SessArApReport.listApPerDueDateReceive(srcArApReport));

        return vect;
    }

    private static Vector shortJoinVectorArApPerDueDate(Vector v1, Vector v2) {
        Vector result = new Vector();
        if (v1 != null && v2 != null) {
            ArApPerDueDate obj1 = new ArApPerDueDate();
            ArApPerDueDate obj2 = new ArApPerDueDate();
            while (!v1.isEmpty()) {
                obj1 = (ArApPerDueDate) v1.get(0);
                if (!v2.isEmpty()) {
                    obj2 = (ArApPerDueDate) v2.get(0);
                    int rs = obj2.getDueDate().compareTo(obj1.getDueDate());
                    if (rs < 0) {
                        result.add(obj2);
                        v2.removeElementAt(0);
                    } else if (rs > 0) {
                        result.add(obj1);
                        v1.removeElementAt(0);
                    } else {
                        obj1.setPayable(obj1.getPayable() + obj2.getPayable());
                        obj1.setReceivable(obj1.getReceivable() + obj2.getReceivable());
                        result.add(obj1);
                        v1.removeElementAt(0);
                        v2.removeElementAt(0);
                    }
                } else {
                    result.add(obj1);
                    v1.removeElementAt(0);
                }
            }
            while (!v2.isEmpty()) {
                obj2 = (ArApPerDueDate) v2.remove(0);
                result.add(obj2);
            }
        }
        return result;
    }

}

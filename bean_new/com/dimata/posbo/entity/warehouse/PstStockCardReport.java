/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Mar 3, 2005
 * Time: 11:45:46 AM
 * To change this template use Options | File Templates.
 */
package com.dimata.posbo.entity.warehouse;

import com.dimata.posbo.db.DBException;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;

import java.util.Vector;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

public class PstStockCardReport {
    public static final String TBL_POS_STOCK_CARD_REPORT = "pos_stock_card_report";

    public static final int FLD_TANGGAL = 0; 
    public static final int FLD_DOC_TYPE = 1; 
    public static final int FLD_KETERANGAN = 2; 
    public static final int FLD_DOC_CODE = 3;
    public static final int FLD_QTY = 4;
    public static final int FLD_TRANSACTION_TYPE = 5;
    public static final int FLD_BERAT = 6;
    public static final int FLD_LOCATION_ID = 7;

    public static final String[] fieldNames = {
        "TANGGAL",
        "DOC_TYPE", 
        "KETERANGAN",
        "DOC_NOMOR",
        "QTY",
        "TRANSACTION_TYPE",
        "BERAT",
        "LOCATION_ID"
    };

    /**
     *
     * @param stockCardReport
     * @return
     * @throws DBException
     */
    public static void insertExc(StockCardReport stockCardReport) throws DBException {
        try {
            SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String s = "DATE_FORMAT('" + simpledateformat.format(stockCardReport.getDate()) + "', '%Y-%m-%d %T')";
            String sql = "INSERT INTO "+TBL_POS_STOCK_CARD_REPORT+
                    "("+fieldNames[FLD_TANGGAL]+
                    ","+fieldNames[FLD_DOC_TYPE]+
                    ","+fieldNames[FLD_DOC_CODE]+
                    ","+fieldNames[FLD_QTY]+
                    ","+fieldNames[FLD_KETERANGAN]+
                    ","+fieldNames[FLD_TRANSACTION_TYPE]+
                    //-- added by dewok-2017 for nilai berat awal
                    ","+fieldNames[FLD_BERAT]+
                    ","+fieldNames[FLD_LOCATION_ID]+")"+
                    //--
                    "VALUES("+s+
                    ","+stockCardReport.getDocType()+
                    ",'"+stockCardReport.getDocCode()+"'"+
                    ","+stockCardReport.getQty()+
                    ",'"+stockCardReport.getKeterangan()+"'"+
                    ","+stockCardReport.getTransaction_type()+
                    //-- added by dewok-2017 for nilai berat awal
                    ","+stockCardReport.getBerat()+
                    ","+stockCardReport.getLocationId()+
                    //--
                    ")";

            DBHandler.execUpdate(sql);
            //System.out.println("use table : "+TBL_POS_STOCK_CARD_REPORT);
        } catch (Exception exc) {
            System.out.println("insertExc : "+exc.toString());
        }
    }

    public static void deleteExc() throws DBException {
        try {
            String sql = "DELETE FROM "+TBL_POS_STOCK_CARD_REPORT;
            DBHandler.execUpdate(sql);
        } catch (Exception exc) {
            System.out.println("deleteExc : "+exc.toString());
        }
    }

    /**
     *
     * @param limitStart
     * @param recordToGet
     * @param whereClause
     * @param order
     * @return
     */
    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_POS_STOCK_CARD_REPORT;
            if (whereClause != null && whereClause.length() > 0)
                sql = sql + " WHERE " + whereClause;
            if (order != null && order.length() > 0)
                sql = sql + " ORDER BY " + order;
            if (limitStart == 0 && recordToGet == 0)
                sql = sql + "";
            else
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                StockCardReport stockCardReport = new StockCardReport();
                resultToObject(rs, stockCardReport);
                lists.add(stockCardReport);
            }
            rs.close();
            return lists;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }


    private static void resultToObject(ResultSet rs, StockCardReport stockCardReport) {
        try {
            stockCardReport.setDate(DBHandler.convertDate(rs.getDate(fieldNames[FLD_TANGGAL]),rs.getTime(fieldNames[FLD_TANGGAL])));
            stockCardReport.setDocCode(rs.getString(fieldNames[FLD_DOC_CODE]));
            stockCardReport.setDocType(rs.getInt(fieldNames[FLD_DOC_TYPE]));
            stockCardReport.setKeterangan(rs.getString(fieldNames[FLD_KETERANGAN]));
            stockCardReport.setQty(rs.getDouble(fieldNames[FLD_QTY]));
            stockCardReport.setTransaction_type(rs.getInt(fieldNames[FLD_TRANSACTION_TYPE]));
            stockCardReport.setBerat(rs.getDouble(fieldNames[FLD_BERAT]));
            stockCardReport.setLocationId(rs.getLong(fieldNames[FLD_LOCATION_ID]));
        } catch (Exception e) {
            System.out.println("error resultToObject > : "+e.toString());
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.report.tabungan;

import com.dimata.qdep.db.DBException;
import com.dimata.qdep.db.DBHandler;
import com.dimata.qdep.db.DBResultSet;
import com.dimata.sedana.entity.tabungan.JenisSimpanan;
import com.dimata.sedana.entity.tabungan.PstJenisSimpanan;
import com.dimata.util.Formater;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Regen
 */
public class PstTabunganHarian {

    public static Vector<TabunganHarian> getTabunganHarianPerTgl(Date start, Date end, String[] idSimpanan, int sort) {
        String[] sortCols = {"no", "PERSON_NAME"};
        Vector<TabunganHarian> th = new Vector<TabunganHarian>();
        try {
            for (String id : idSimpanan) {
                JenisSimpanan js = PstJenisSimpanan.fetchExc(Long.valueOf(id));
                TabunganHarian h = new TabunganHarian();
                h.setNamaSimpanan(js.getNamaSimpanan());
                String query = "SELECT cl.`CONTACT_ID`, cl.`PERSON_NAME`, saldo.`total` AS `saldo_total`, saldo.no, bunga.`total` AS `bunga_total`,saldo.ID_SIMPANAN "
                        + "FROM `contact_list` cl "
                        + "LEFT JOIN ( "
                        + "SELECT adt.`ID_ANGGOTA`, act.`ASSIGN_TABUNGAN_ID`, act.`NO_TABUNGAN` AS NO, adt.`ID_JENIS_SIMPANAN`, SUM(dt.`KREDIT`-dt.`DEBET`) AS `total`,adt.ID_SIMPANAN "
                        + "FROM sedana_transaksi st "
                        + "JOIN `sedana_detail_transaksi` dt USING (TRANSAKSI_ID) "
                        + "JOIN `sedana_jenis_transaksi` jt USING (`JENIS_TRANSAKSI_ID`) "
                        + "JOIN `aiso_data_tabungan` adt USING (`ID_SIMPANAN`) "
                        + "JOIN `sedana_assign_contact_tabungan` act USING (`ASSIGN_TABUNGAN_ID`) "
                        + "JOIN `aiso_jenis_simpanan` js ON (adt.`ID_JENIS_SIMPANAN` = js.`ID_JENIS_SIMPANAN`) "
                        + "WHERE"
                        + " js.`ID_JENIS_SIMPANAN` = " + id + " ";
                
                if (start != null && end != null) {
                    query += ""
                            + "AND st.`TANGGAL_TRANSAKSI` >= '" + Formater.formatDate(start, "yyyy-MM-dd") + " 00:00:00' "
                            + "AND st.`TANGGAL_TRANSAKSI` <= '" + Formater.formatDate(end, "yyyy-MM-dd") + " 23:59:59' ";
                }

                query += ""
                        + "GROUP BY act.`ASSIGN_TABUNGAN_ID`, adt.`ID_ANGGOTA` ) AS saldo ON (cl.`CONTACT_ID` = saldo.`ID_ANGGOTA`) "
                        + "LEFT JOIN ( "
                        + "SELECT adt.`ID_ANGGOTA`, adt.`ID_JENIS_SIMPANAN`, SUM(dt.`KREDIT`-dt.`DEBET`) AS `total` "
                        + "FROM sedana_transaksi st JOIN `sedana_detail_transaksi` dt USING (TRANSAKSI_ID) "
                        + "JOIN `sedana_jenis_transaksi` jt USING (`JENIS_TRANSAKSI_ID`) "
                        + "JOIN `aiso_data_tabungan` adt USING (`ID_SIMPANAN`) "
                        + "JOIN `aiso_jenis_simpanan` js ON (adt.`ID_JENIS_SIMPANAN` = js.`ID_JENIS_SIMPANAN`) "
                        + "WHERE jt.`TYPE_PROSEDUR` = 0 "
                        + "AND jt.`PROSEDURE_UNTUK` = 0 "
                        + "AND jt.`TYPE_TRANSAKSI` = 2 ";
                if (start != null && end != null) {
                    query += ""
                            + "AND st.`TANGGAL_TRANSAKSI` >= '" + Formater.formatDate(start, "yyyy-MM-dd") + " 00:00:00' "
                            + "AND st.`TANGGAL_TRANSAKSI` <= '" + Formater.formatDate(end, "yyyy-MM-dd") + " 23:59:59' ";
                }
                query += ""
                        + "AND js.`ID_JENIS_SIMPANAN` = " + id + " "
                        + "GROUP BY adt.`ID_ANGGOTA` ) AS bunga ON (cl.`CONTACT_ID` = bunga.`ID_ANGGOTA`) "
                        + "INNER JOIN `contact_class_assign` AS cca "
                        + "ON cca.`CONTACT_ID` = cl.`CONTACT_ID` "
                        + "INNER JOIN `contact_class` AS cc "
                        + "ON cc.`CONTACT_CLASS_ID` = cca.`CONTACT_CLASS_ID` "
                        + "WHERE ( "
                        + "cc.`CLASS_TYPE` = '7' "
                        + "OR cc.`CLASS_TYPE` = '19' "
                        + ")"
                        + "AND NO IS NOT NULL "
                        + "ORDER BY `"+sortCols[sort]+"` ASC ";

                DBResultSet dbrs = null;
                dbrs = DBHandler.execQueryResult(query);
                ResultSet rs = dbrs.getResultSet();
                while (rs.next()) {
                    long selisih = rs.getLong("bunga_total");
                    while((selisih-1000000)>0){
                      selisih-=1000000;
                    }
                    while((selisih-100000)>0){
                      selisih-=100000;
                    }
                    while((selisih-10000)>0){
                      selisih-=10000;
                    }
                    while((selisih-1000)>0){
                      selisih-=1000;
                    }
                    while((selisih-100)>0){
                      selisih-=100;
                    }
                    h.setDataTabungan(
                            rs.getLong("CONTACT_ID"),
                            rs.getString("no"),
                            rs.getString("PERSON_NAME"),
                            rs.getDouble("saldo_total"),
                            rs.getDouble("bunga_total"),
                            rs.getLong("bunga_total")-selisih,
                            "",
                            //ADDED BY DEWOK 20180825 TO GET ID SIMPANAN FOR LAST SALDO
                            rs.getLong("ID_SIMPANAN")
                    );
                }

                th.add(h);
            }
        } catch (DBException ex) {
            Logger.getLogger(PstTabunganHarian.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(PstTabunganHarian.class.getName()).log(Level.SEVERE, null, ex);
        }
        return th;
    }
}

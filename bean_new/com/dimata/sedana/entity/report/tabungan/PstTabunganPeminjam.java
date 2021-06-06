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
public class PstTabunganPeminjam {
  public Vector getTabunganPeminjam(String id, Date start, Date end) {
    try {
          JenisSimpanan js = PstJenisSimpanan.fetchExc(Long.valueOf(id));
          TabunganHarian h = new TabunganHarian();
          h.setNamaSimpanan(js.getNamaSimpanan());
          String query =  "SELECT cl.`CONTACT_ID`, cl.`PERSON_NAME`, saldo.`total` AS `saldo_total`, bunga.`total` AS `bunga_total` " +
                          "FROM `contact_list` cl " +
                          "LEFT JOIN ( " +
                          "SELECT st.`ID_ANGGOTA`, adt.`ID_JENIS_SIMPANAN`, SUM(dt.`KREDIT`-dt.`DEBET`) AS `total` " +
                          "FROM sedana_transaksi st " +
                          "JOIN `sedana_detail_transaksi` dt USING (TRANSAKSI_ID) " +
                          "JOIN `sedana_jenis_transaksi` jt USING (`JENIS_TRANSAKSI_ID`) " +
                          "JOIN `aiso_data_tabungan` adt USING (`ID_SIMPANAN`) " +
                          "JOIN `aiso_jenis_simpanan` js ON (adt.`ID_JENIS_SIMPANAN` = js.`ID_JENIS_SIMPANAN`) " +
                          "WHERE jt.`TYPE_PROSEDUR` = 1 " +
                          "AND jt.`PROSEDURE_UNTUK` = 0 " +
                          "AND jt.`TYPE_TRANSAKSI` = 0 " +
                          "AND st.`TANGGAL_TRANSAKSI` >= '"+Formater.formatDate(start, "yyyy-MM-dd")+" 00:00:00' " +
                          "AND st.`TANGGAL_TRANSAKSI` <= '"+Formater.formatDate(end, "yyyy-MM-dd")+" 23:59:59' " +
                          "AND js.`ID_JENIS_SIMPANAN` = "+id+" " +
                          "GROUP BY st.`ID_ANGGOTA` ) AS saldo ON (cl.`CONTACT_ID` = saldo.`ID_ANGGOTA`) " +
                          "LEFT JOIN ( " +
                          "SELECT st.`ID_ANGGOTA`, adt.`ID_JENIS_SIMPANAN`, SUM(dt.`KREDIT`-dt.`DEBET`) AS `total` " +
                          "FROM sedana_transaksi st JOIN `sedana_detail_transaksi` dt USING (TRANSAKSI_ID) " +
                          "JOIN `sedana_jenis_transaksi` jt USING (`JENIS_TRANSAKSI_ID`) " +
                          "JOIN `aiso_data_tabungan` adt USING (`ID_SIMPANAN`) " +
                          "JOIN `aiso_jenis_simpanan` js ON (adt.`ID_JENIS_SIMPANAN` = js.`ID_JENIS_SIMPANAN`) " +
                          "WHERE jt.`TYPE_PROSEDUR` = 0 " +
                          "AND jt.`PROSEDURE_UNTUK` = 0 " +
                          "AND jt.`TYPE_TRANSAKSI` = 1 " +
                          "AND st.`TANGGAL_TRANSAKSI` >= '"+Formater.formatDate(start, "yyyy-MM-dd")+" 00:00:00' " +
                          "AND st.`TANGGAL_TRANSAKSI` <= '"+Formater.formatDate(end, "yyyy-MM-dd")+" 23:59:59' " +
                          "AND js.`ID_JENIS_SIMPANAN` = "+id+" " +
                          "GROUP BY st.`ID_ANGGOTA` ) AS bunga ON (cl.`CONTACT_ID` = bunga.`ID_ANGGOTA`) " +
                          "ORDER BY cl.`PERSON_NAME` ASC "
                          ;
          
          DBResultSet dbrs = null;
          dbrs = DBHandler.execQueryResult(query);
          ResultSet rs = dbrs.getResultSet();
          while (rs.next()) {
            h.setDataTabungan(
                    rs.getLong("CONTACT_ID"),
                    "",
                    rs.getString("PERSON_NAME"),
                    rs.getDouble("saldo_total"),
                    rs.getDouble("bunga_total"),
                    rs.getLong("bunga_total"),
                    "",
                    //ADDED BY DEWOK 20180825 TO GET ID SIMPANAN FOR LAST SALDO
                    0
            );
          }
          
          //th.add(h);
    } catch (DBException ex) {
      Logger.getLogger(PstTabunganHarian.class.getName()).log(Level.SEVERE, null, ex);
    } catch (SQLException ex) {
      Logger.getLogger(PstTabunganHarian.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    return new Vector();
    
  }
}

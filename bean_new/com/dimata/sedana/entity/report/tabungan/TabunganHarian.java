/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.sedana.entity.report.tabungan;

import com.dimata.qdep.entity.Entity;
import java.util.Vector;

/**
 *
 * @author Regen
 */
public class TabunganHarian extends Entity {
  
  private Vector<DataTabungan> dataTabungan = new Vector<DataTabungan>();
  private String namaSimpanan = "";

  /**
   * @return the dataTabungan
   */
  public DataTabungan getDataTabungan(int i) {
    return dataTabungan.get(i);
  }
  
  public Vector<DataTabungan> getDataTabunganVector() {
    return dataTabungan;
  }

  /**
   * @param dataTabungan the dataTabungan to set
   */
  public void setDataTabungan(long oid, String noTabungan, String nama, double saldo, double bunga, long pembulatan, String ket, long idSimpanan) {
    DataTabungan d = new DataTabungan();
    d.setOID(oid);
    d.setNama(nama);
    d.setSaldo(saldo);
    d.setBunga(bunga);
    d.setPembulatan(pembulatan);
    d.setKet(ket);
    d.setNoTabungan(noTabungan);
    //ADDED BY DEWOK 20180825 TO GET ID SIMPANAN FOR LAST SALDO
    d.setIdSimpanan(idSimpanan);
    this.dataTabungan.add(d);
  }

  /**
   * @return the namaSimpanan
   */
  public String getNamaSimpanan() {
    return namaSimpanan;
  }

  /**
   * @param namaSimpanan the namaSimpanan to set
   */
  public void setNamaSimpanan(String namaSimpanan) {
    this.namaSimpanan = namaSimpanan;
  }
  
  public class DataTabungan extends Entity {
    private String noTabungan = "";
    private String nama = "";
    private double saldo = 0;
    private double bunga = 0;
    private long pembulatan = 0;
    private String ket = "";
    //ADDED BY DEWOK 20180825 TO GET ID SIMPANAN FOR LAST SALDO
    private long idSimpanan = 0;

    /**
     * @return the nama
     */
    public String getNama() {
      return nama;
    }

    /**
     * @param nama the nama to set
     */
    public void setNama(String nama) {
      this.nama = nama;
    }

    /**
     * @return the saldo
     */
    public double getSaldo() {
      return saldo;
    }

    /**
     * @param saldo the saldo to set
     */
    public void setSaldo(double saldo) {
      this.saldo = saldo;
    }

    /**
     * @return the bunga
     */
    public double getBunga() {
      return bunga;
    }

    /**
     * @param bunga the bunga to set
     */
    public void setBunga(double bunga) {
      this.bunga = bunga;
    }

    /**
     * @return the pembulatan
     */
    public long getPembulatan() {
      return pembulatan;
    }

    /**
     * @param pembulatan the pembulatan to set
     */
    public void setPembulatan(long pembulatan) {
      this.pembulatan = pembulatan;
    }

    /**
     * @return the ket
     */
    public String getKet() {
      return ket;
    }

    /**
     * @param ket the ket to set
     */
    public void setKet(String ket) {
      this.ket = ket;
    }

    /**
     * @return the noTabungan
     */
    public String getNoTabungan() {
      return noTabungan;
    }

    /**
     * @param noTabungan the noTabungan to set
     */
    public void setNoTabungan(String noTabungan) {
      this.noTabungan = noTabungan;
    }

    public long getIdSimpanan() {
        return idSimpanan;
    }

    public void setIdSimpanan(long idSimpanan) {
        this.idSimpanan = idSimpanan;
    }

  }
}

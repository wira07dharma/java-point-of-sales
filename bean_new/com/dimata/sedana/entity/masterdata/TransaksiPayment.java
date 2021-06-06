package com.dimata.sedana.entity.masterdata;

/**
 *
 * @author Regen
 */
import com.dimata.qdep.entity.Entity;

public class TransaksiPayment extends Entity {

  private long paymentSystemId = 0;
  private long transaksiId = 0;
  private double jumlah = 0;

  public long getPaymentSystemId() {
    return paymentSystemId;
  }

  public void setPaymentSystemId(long paymentSystemId) {
    this.paymentSystemId = paymentSystemId;
  }

  public long getTransaksiId() {
    return transaksiId;
  }

  public void setTransaksiId(long transaksiId) {
    this.transaksiId = transaksiId;
  }

  public double getJumlah() {
    return jumlah;
  }

  public void setJumlah(double jumlah) {
    this.jumlah = jumlah;
  }

}

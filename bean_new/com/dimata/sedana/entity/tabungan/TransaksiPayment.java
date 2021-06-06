/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.tabungan;

/**
 *
 * @author Regen
 */
import com.dimata.qdep.entity.Entity;
import java.util.Date;

public class TransaksiPayment extends Entity {

  private long paymentSystemId = 0;
  private long transaksiId = 0;
  private double jumlah = 0;
  //
  private String cardName = "";
  private String cardNo = "";
  private String bankName = "";
  private Date validateDate = null;

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

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Date getValidateDate() {
        return validateDate;
    }

    public void setValidateDate(Date validateDate) {
        this.validateDate = validateDate;
    }

}

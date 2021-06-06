/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.sedana.entity.kredit;

/**
 *
 * @author Regen
 */
import com.dimata.qdep.entity.Entity;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

public class AngsuranPayment extends Entity {

    //nilai konstanta
    public static final int STATUS_DOC_CLOSE = 5;

    public static final String[] STATUS_DOC_TITLE = {"", "", "", "", "", "Closed"};

    public static final int TIPE_PAYMENT_SAVING = -1;
    public static final int TIPE_PAYMENT_CREDIT_CARD = 0;
    public static final int TIPE_PAYMENT_CASH = 1;
    public static final int TIPE_PAYMENT_DEBIT_CARD = 2;
    public static final int TIPE_PAYMENT_FOC = 6;
    public static final int TIPE_PAYMENT_BIAYA_RUGI = 20;
    public static final int TIPE_PAYMENT_BIAYA_RUGI_POKOK = 21;
    public static final int TIPE_PAYMENT_BIAYA_RUGI_BUNGA = 22;

    private static final String[] TIPE_PAYMENT_TITLE = {
        "Tabungan",
        "Credit Card",
        "Cash",
        "Debit Card",
        "FOC",
        "Biaya Kerugian",
        "Biaya Kerugian Pokok",
        "Biaya Kerugian Bunga"
    };

    public static String getTipePaymentTitle(int tipe) {
        switch (tipe) {
            case TIPE_PAYMENT_SAVING:
                return TIPE_PAYMENT_TITLE[0];
            case TIPE_PAYMENT_CREDIT_CARD:
                return TIPE_PAYMENT_TITLE[1];
            case TIPE_PAYMENT_CASH:
                return TIPE_PAYMENT_TITLE[2];
            case TIPE_PAYMENT_DEBIT_CARD:
                return TIPE_PAYMENT_TITLE[3];
            case TIPE_PAYMENT_FOC:
                return TIPE_PAYMENT_TITLE[4];
            case TIPE_PAYMENT_BIAYA_RUGI:
                return TIPE_PAYMENT_TITLE[5];
            case TIPE_PAYMENT_BIAYA_RUGI_POKOK:
                return TIPE_PAYMENT_TITLE[6];
            case TIPE_PAYMENT_BIAYA_RUGI_BUNGA:
                return TIPE_PAYMENT_TITLE[7];
            default:
                return "Not defined";
        }
    }
    
    public static final TreeMap<Integer, String> TIPE_PAYMENT = new TreeMap<Integer, String>(){{
      put(TIPE_PAYMENT_SAVING, TIPE_PAYMENT_TITLE[0]);
      put(TIPE_PAYMENT_CREDIT_CARD, TIPE_PAYMENT_TITLE[1]);
      put(TIPE_PAYMENT_CASH, TIPE_PAYMENT_TITLE[2]);
      put(TIPE_PAYMENT_DEBIT_CARD, TIPE_PAYMENT_TITLE[3]);
      put(TIPE_PAYMENT_FOC, TIPE_PAYMENT_TITLE[4]);
      put(TIPE_PAYMENT_BIAYA_RUGI, TIPE_PAYMENT_TITLE[5]);
      put(TIPE_PAYMENT_BIAYA_RUGI_POKOK, TIPE_PAYMENT_TITLE[6]);
      put(TIPE_PAYMENT_BIAYA_RUGI_BUNGA, TIPE_PAYMENT_TITLE[7]);
    }};

    private long paymentSystemId = 0;
    private double jumlah = 0;
    private String cardName = "";
    private String cardNumber = "";
    private String bankName = "";
    private Date validateDate = null;
    private int status = 0;
    private long transaksiId = 0;
    private long idSimpanan = 0;

    public long getIdSimpanan() {
        return idSimpanan;
    }

    public void setIdSimpanan(long idSimpanan) {
        this.idSimpanan = idSimpanan;
    }

    public long getTransaksiId() {
        return transaksiId;
    }

    public void setTransaksiId(long transaksiId) {
        this.transaksiId = transaksiId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
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

    public long getPaymentSystemId() {
        return paymentSystemId;
    }

    public void setPaymentSystemId(long paymentSystemId) {
        this.paymentSystemId = paymentSystemId;
    }

    public double getJumlah() {
        return jumlah;
    }

    public void setJumlah(double jumlah) {
        this.jumlah = jumlah;
    }

}

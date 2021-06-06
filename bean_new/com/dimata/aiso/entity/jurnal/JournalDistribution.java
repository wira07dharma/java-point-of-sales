/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.entity.jurnal;

import com.dimata.qdep.entity.Entity;
import java.util.Vector;
import java.util.Date;

/**
 *
 * @author dwi
 */
public class JournalDistribution extends Entity{

    private long bussCenterId = 0;
    private long journalDetailId = 0;
    private String jDetailNote="";
    private long idPerkiraan = 0;
    private long periodeId = 0;
    private int index = 0;
    private long journalIndex = 0;
    private int dataStatus = 0;
    private double debitAmount = 0.0;
    private double creditAmount = 0.0;
    private String note = "";
    private long currencyId = 0;
    private double transRate = 0.0;
    private double standardRate = 0.0;
    private long arapMainId = 0;
    private long arapPaymentId = 0;
    private String coaCode="";
    private String coaName ="";
    private String coaNameEnglish ="";
    private Date transDate = null;
    private long mainJournalId = 0L;
    private String mainJournalNumber="";
    private String mainJournalNote = null;    
    private String detailJournalNote = null;

    public long getBussCenterId() {
        return bussCenterId;
    }

    public void setBussCenterId(long bussCenterId) {
        this.bussCenterId = bussCenterId;
    }

    public double getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(double creditAmount) {
        this.creditAmount = creditAmount;
    }

    public long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(long currencyId) {
        this.currencyId = currencyId;
    }

    public double getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(double debitAmount) {
        this.debitAmount = debitAmount;
    }

    public long getIdPerkiraan() {
        return idPerkiraan;
    }

    public void setIdPerkiraan(long idPerkiraan) {
        this.idPerkiraan = idPerkiraan;
    }

    public long getJournalDetailId() {
        return journalDetailId;
    }

    public void setJournalDetailId(long journalDetailId) {
        this.journalDetailId = journalDetailId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getPeriodeId() {
        return periodeId;
    }

    public void setPeriodeId(long periodeId) {
        this.periodeId = periodeId;
    }

    public double getStandardRate() {
        return standardRate;
    }

    public void setStandardRate(double standardRate) {
        this.standardRate = standardRate;
    }

    public double getTransRate() {
        return transRate;
    }

    public void setTransRate(double transRate) {
        this.transRate = transRate;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(int dataStatus) {
        this.dataStatus = dataStatus;
    }

    public long getJournalIndex() {
        return journalIndex;
    }

    public void setJournalIndex(long journalIndex) {
        this.journalIndex = journalIndex;
    }

    public long getArapMainId() {
        return arapMainId;
    }

    public void setArapMainId(long arapMainId) {
        this.arapMainId = arapMainId;
    }

    public long getArapPaymentId() {
        return arapPaymentId;
    }

    public void setArapPaymentId(long arapPaymentId) {
        this.arapPaymentId = arapPaymentId;
    }

    public String getCoaCode() {
        return coaCode;
    }

    public void setCoaCode(String coaCode) {
        this.coaCode = coaCode;
    }

    public String getCoaName() {
        return coaName;
    }

    public void setCoaName(String coaName) {
        this.coaName = coaName;
    }

    public String getCoaNameEnglish() {
        return coaNameEnglish;
    }

    public void setCoaNameEnglish(String coaNameEnglish) {
        this.coaNameEnglish = coaNameEnglish;
    }

    public String getMainJournalNote() {
        return mainJournalNote;
    }

    public void setMainJournalNote(String mainJournalNote) {
        this.mainJournalNote = mainJournalNote;
    }

    public long getMainJournalId() {
        return mainJournalId;
    }

    public void setMainJournalId(long mainJournalId) {
        this.mainJournalId = mainJournalId;
    }

    public Date getTransDate() {
        return transDate;
    }

    public void setTransDate(Date transDate) {
        this.transDate = transDate;
    }

    public String getMainJournalNumber() {
        return mainJournalNumber;
    }

    public void setMainJournalNumber(String mainJournalNumber) {
        this.mainJournalNumber = mainJournalNumber;
    }

    public String getJDetailNote() {
        return jDetailNote;
    }

    public void setJDetailNote(String jDetailNote) {
        this.jDetailNote = jDetailNote;
    }
    
    
}

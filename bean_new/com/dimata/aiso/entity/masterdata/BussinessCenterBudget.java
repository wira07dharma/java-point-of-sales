/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.aiso.entity.masterdata;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author dwi
 */
public class BussinessCenterBudget extends Entity{
   private Perkiraan perkiraan = new Perkiraan();
   private long idPerkiraan = 0;
   private long periodeId = 0;
   private long bussCenterId = 0;
   private double budget = 0;
   private double debitForeignStdRate = 0;
   private double debitForeignTransRate = 0;
   private double debitLocalStdRate = 0;
   private double debitLocalTransRate = 0;
   private double creditForeignStdRate = 0;
   private double creditForeignTransRate = 0;
   private double creditLocalStdRate = 0;
   private double creditLocalTransRate = 0;

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public double getCreditForeignStdRate() {
        return creditForeignStdRate;
    }

    public void setCreditForeignStdRate(double creditForeignStdRate) {
        this.creditForeignStdRate = creditForeignStdRate;
    }

    public double getCreditForeignTransRate() {
        return creditForeignTransRate;
    }

    public void setCreditForeignTransRate(double creditForeignTransRate) {
        this.creditForeignTransRate = creditForeignTransRate;
    }

    public double getCreditLocalStdRate() {
        return creditLocalStdRate;
    }

    public void setCreditLocalStdRate(double creditLocalStdRate) {
        this.creditLocalStdRate = creditLocalStdRate;
    }

    public double getCreditLocalTransRate() {
        return creditLocalTransRate;
    }

    public void setCreditLocalTransRate(double creditLocalTransRate) {
        this.creditLocalTransRate = creditLocalTransRate;
    }

    public double getDebitForeignStdRate() {
        return debitForeignStdRate;
    }

    public void setDebitForeignStdRate(double debitForeignStdRate) {
        this.debitForeignStdRate = debitForeignStdRate;
    }

    public double getDebitForeignTransRate() {
        return debitForeignTransRate;
    }

    public void setDebitForeignTransRate(double debitForeignTransRate) {
        this.debitForeignTransRate = debitForeignTransRate;
    }

    public double getDebitLocalStdRate() {
        return debitLocalStdRate;
    }

    public void setDebitLocalStdRate(double debitLocalStdRate) {
        this.debitLocalStdRate = debitLocalStdRate;
    }

    public double getDebitLocalTransRate() {
        return debitLocalTransRate;
    }

    public void setDebitLocalTransRate(double debitLocalTransRate) {
        this.debitLocalTransRate = debitLocalTransRate;
    }

    public long getIdPerkiraan() {
        return idPerkiraan;
    }

    public void setIdPerkiraan(long idPerkiraan) {
        this.idPerkiraan = idPerkiraan;
    }

    public long getPeriodeId() {
        return periodeId;
    }

    public void setPeriodeId(long periodeId) {
        this.periodeId = periodeId;
    }

    public long getBussCenterId() {
        return bussCenterId;
    }

    public void setBussCenterId(long bussCenterId) {
        this.bussCenterId = bussCenterId;
    }

    public Perkiraan getPerkiraan() {
        return perkiraan;
    }

    public void setPerkiraan(Perkiraan perkiraan) {
        this.perkiraan = perkiraan;
    }
      
    
}

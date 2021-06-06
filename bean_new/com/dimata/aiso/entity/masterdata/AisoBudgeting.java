/*
 * AisoBudgeting.java
 * @author  rusdianta
 * Created on February 28, 2005, 11:19 AM
 */

package com.dimata.aiso.entity.masterdata;

import com.dimata.qdep.entity.Entity;

public class AisoBudgeting extends Entity { 
    
    private long budgetingOid;
    private long idPerkiraan;
    private long periodeId;
    private double budget;    
    
    /** Creates a new instance of AisoBudgeting */
    public AisoBudgeting() {
        budgetingOid = 0;
        idPerkiraan = 0;
        periodeId = 0;
        budget = 0;        
    }
    
    public long getBudgetingOid() {
        return budgetingOid;
    }
    
    public void setBudgetingOid(long budgetingOid) {
        this.budgetingOid = budgetingOid;
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
    
    public double getBudget() {
        return budget;
    }
    
    public void setBudget(double budget) {
        this.budget = budget;
    }
}

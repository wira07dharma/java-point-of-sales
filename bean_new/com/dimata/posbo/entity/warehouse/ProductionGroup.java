/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.warehouse;

import com.dimata.qdep.entity.Entity;
import java.util.Date;

/**
 *
 * @author Dimata 007
 */
public class ProductionGroup extends Entity {

    private long productionId = 0;
    private String batchNumber = "";
    private long chainPeriodId = 0;
    private Date dateStart = null;
    private Date dateEnd = null;
    private long productionGroupParentId = 0;
    private int index = 0;

    public long getProductionId() {
        return productionId;
    }

    public void setProductionId(long productionId) {
        this.productionId = productionId;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public long getChainPeriodId() {
        return chainPeriodId;
    }

    public void setChainPeriodId(long chainPeriodId) {
        this.chainPeriodId = chainPeriodId;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public long getProductionGroupParentId() {
        return productionGroupParentId;
    }

    public void setProductionGroupParentId(long productionGroupParentId) {
        this.productionGroupParentId = productionGroupParentId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

}

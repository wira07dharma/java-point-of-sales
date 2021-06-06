/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.masterdata;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author Dimata 007
 */
public class CalcCogsCost extends Entity {

    private long calcCogsMainId = 0;
    private long materialId = 0;
    private long unitId = 0;
    private double qtyItem = 0;
    private double stockLeft = 0;
    private double subTotalCost = 0;
    private double stokAwal = 0;

    public long getCalcCogsMainId() {
        return calcCogsMainId;
    }

    public void setCalcCogsMainId(long calcCogsMainId) {
        this.calcCogsMainId = calcCogsMainId;
    }

    public long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(long materialId) {
        this.materialId = materialId;
    }

    public long getUnitId() {
        return unitId;
    }

    public void setUnitId(long unitId) {
        this.unitId = unitId;
    }

    public double getQtyItem() {
        return qtyItem;
    }

    public void setQtyItem(double qtyItem) {
        this.qtyItem = qtyItem;
    }

    public double getStockLeft() {
        return stockLeft;
    }

    public void setStockLeft(double stockLeft) {
        this.stockLeft = stockLeft;
    }

    public double getSubTotalCost() {
        return subTotalCost;
    }

    public void setSubTotalCost(double subTotalCost) {
        this.subTotalCost = subTotalCost;
    }

    public double getStokAwal() {
        return stokAwal;
    }

    public void setStokAwal(double stokAwal) {
        this.stokAwal = stokAwal;
    }

}

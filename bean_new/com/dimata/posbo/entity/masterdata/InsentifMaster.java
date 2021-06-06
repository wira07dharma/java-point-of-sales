/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.masterdata;

import com.dimata.qdep.entity.Entity;
import java.util.Date;

/**
 *
 * @author Dimata 007
 */
public class InsentifMaster extends Entity {

    private String title = "";
    private Date periodeStart = null;
    private Date periodeEnd = null;
    private int periodeForever = 0;
    private int materialMain = 0;
    private int includeSalesProfit = 0;
    private int includeCostOfSales = 0;
    private double divisionPoint = 0;
    private double faktorNominalInsentif = 0;
    private int dependOnPosition = 0;
    private int status = 0;
    private long categoryId = 0;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getPeriodeStart() {
        return periodeStart;
    }

    public void setPeriodeStart(Date periodeStart) {
        this.periodeStart = periodeStart;
    }

    public Date getPeriodeEnd() {
        return periodeEnd;
    }

    public void setPeriodeEnd(Date periodeEnd) {
        this.periodeEnd = periodeEnd;
    }

    public int getPeriodeForever() {
        return periodeForever;
    }

    public void setPeriodeForever(int periodeForever) {
        this.periodeForever = periodeForever;
    }

    public int getMaterialMain() {
        return materialMain;
    }

    public void setMaterialMain(int materialMain) {
        this.materialMain = materialMain;
    }

    public int getIncludeSalesProfit() {
        return includeSalesProfit;
    }

    public void setIncludeSalesProfit(int includeSalesProfit) {
        this.includeSalesProfit = includeSalesProfit;
    }

    public int getIncludeCostOfSales() {
        return includeCostOfSales;
    }

    public void setIncludeCostOfSales(int includeCostOfSales) {
        this.includeCostOfSales = includeCostOfSales;
    }

    public double getDivisionPoint() {
        return divisionPoint;
    }

    public void setDivisionPoint(double divisionPoint) {
        this.divisionPoint = divisionPoint;
    }

    public double getFaktorNominalInsentif() {
        return faktorNominalInsentif;
    }

    public void setFaktorNominalInsentif(double faktorNominalInsentif) {
        this.faktorNominalInsentif = faktorNominalInsentif;
    }

    public int getDependOnPosition() {
        return dependOnPosition;
    }

    public void setDependOnPosition(int dependOnPosition) {
        this.dependOnPosition = dependOnPosition;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

}

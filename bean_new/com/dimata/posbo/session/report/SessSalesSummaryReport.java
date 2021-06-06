/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.session.report;

import com.dimata.common.entity.payment.PaymentSystem;
import java.util.Vector;

/**
 *
 * @author dimata005
 */
public class SessSalesSummaryReport {
    private double pax=0;
    private double food=0.0;
    private double beverage=0.0;
    private double other=0.0;
    private double netSales=0.0;
    private double tax=0.0;
    private double service=0.0;
    private double discount=0.0;
    private double totalSales=0.0;
    private double kreditSales=0.0;
    private double complimentSales=0.0;
    private Vector<PaymentSystem> vPaymentSystem;
    //private Vector<PaymentSystem> vPaymentSystem=null;

    /**
     * @return the pax
     */
    public double getPax() {
        return pax;
    }

    /**
     * @param pax the pax to set
     */
    public void setPax(double pax) {
        this.pax = pax;
    }

    /**
     * @return the food
     */
    public double getFood() {
        return food;
    }

    /**
     * @param food the food to set
     */
    public void setFood(double food) {
        this.food = food;
    }

    /**
     * @return the beverage
     */
    public double getBeverage() {
        return beverage;
    }

    /**
     * @param beverage the beverage to set
     */
    public void setBeverage(double beverage) {
        this.beverage = beverage;
    }

    /**
     * @return the other
     */
    public double getOther() {
        return other;
    }

    /**
     * @param other the other to set
     */
    public void setOther(double other) {
        this.other = other;
    }

    /**
     * @return the netSales
     */
    public double getNetSales() {
        return netSales;
    }

    /**
     * @param netSales the netSales to set
     */
    public void setNetSales(double netSales) {
        this.netSales = netSales;
    }

    /**
     * @return the tax
     */
    public double getTax() {
        return tax;
    }

    /**
     * @param tax the tax to set
     */
    public void setTax(double tax) {
        this.tax = tax;
    }

    /**
     * @return the service
     */
    public double getService() {
        return service;
    }

    /**
     * @param service the service to set
     */
    public void setService(double service) {
        this.service = service;
    }

    /**
     * @return the discount
     */
    public double getDiscount() {
        return discount;
    }

    /**
     * @param discount the discount to set
     */
    public void setDiscount(double discount) {
        this.discount = discount;
    }

    /**
     * @return the totalSales
     */
    public double getTotalSales() {
        return totalSales;
    }

    /**
     * @param totalSales the totalSales to set
     */
    public void setTotalSales(double totalSales) {
        this.totalSales = totalSales;
    }

    /**
     * @return the kreditSales
     */
    public double getKreditSales() {
        return kreditSales;
    }

    /**
     * @param kreditSales the kreditSales to set
     */
    public void setKreditSales(double kreditSales) {
        this.kreditSales = kreditSales;
    }

    /**
     * @return the complimentSales
     */
    public double getComplimentSales() {
        return complimentSales;
    }

    /**
     * @param complimentSales the complimentSales to set
     */
    public void setComplimentSales(double complimentSales) {
        this.complimentSales = complimentSales;
    }

    /**
     * @return the vPaymentSystem
     */
    public Vector<PaymentSystem> getvPaymentSystem() {
        return vPaymentSystem;
    }

    /**
     * @param vPaymentSystem the vPaymentSystem to set
     */
    public void setvPaymentSystem(Vector<PaymentSystem> vPaymentSystem) {
        this.vPaymentSystem = vPaymentSystem;
    }
    
}

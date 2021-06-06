/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.masterdata;

/**
 *
 * @author dimata005
 */
public class HistoryPosisiStok {

    private long materialId = 0;
    private String sku = "";
    private String barcode = "";
    private String namaItem = "";
    private double hpp = 0;
    private String unit = "";
    private double saldoAwal = 0;

    /*in*/
    private double receiveSupplier = 0;
    private double receiveWarehouse = 0;
    private double returnSales = 0;
    private double totalIn = 0;

    /*out*/
    private double transfer = 0;
    private double returnSupplier = 0;
    private double sales = 0;
    private double cost = 0;
    private double totalOut = 0;

    /*total*/
    private double subTotal = 0;

    /*opname*/
    private double opname = 0;
    private double selisihOpnamePlus = 0;
    private double selisihOpnameMinus = 0;
    private double balance = 0;

    //ADDED BY DEWOK 20190927
    private double productionOut = 0;
    private double productionIn = 0;

    /**
     * @return the materialId
     */
    public long getMaterialId() {
        return materialId;
    }

    /**
     * @param materialId the materialId to set
     */
    public void setMaterialId(long materialId) {
        this.materialId = materialId;
    }

    /**
     * @return the sku
     */
    public String getSku() {
        return sku;
    }

    /**
     * @param sku the sku to set
     */
    public void setSku(String sku) {
        this.sku = sku;
    }

    /**
     * @return the barcode
     */
    public String getBarcode() {
        return barcode;
    }

    /**
     * @param barcode the barcode to set
     */
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    /**
     * @return the namaItem
     */
    public String getNamaItem() {
        return namaItem;
    }

    /**
     * @param namaItem the namaItem to set
     */
    public void setNamaItem(String namaItem) {
        this.namaItem = namaItem;
    }

    /**
     * @return the hpp
     */
    public double getHpp() {
        return hpp;
    }

    /**
     * @param hpp the hpp to set
     */
    public void setHpp(double hpp) {
        this.hpp = hpp;
    }

    /**
     * @return the unit
     */
    public String getUnit() {
        return unit;
    }

    /**
     * @param unit the unit to set
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * @return the saldoAwal
     */
    public double getSaldoAwal() {
        return saldoAwal;
    }

    /**
     * @param saldoAwal the saldoAwal to set
     */
    public void setSaldoAwal(double saldoAwal) {
        this.saldoAwal = saldoAwal;
    }

    /**
     * @return the receiveSupplier
     */
    public double getReceiveSupplier() {
        return receiveSupplier;
    }

    /**
     * @param receiveSupplier the receiveSupplier to set
     */
    public void setReceiveSupplier(double receiveSupplier) {
        this.receiveSupplier = receiveSupplier;
    }

    /**
     * @return the receiveWarehouse
     */
    public double getReceiveWarehouse() {
        return receiveWarehouse;
    }

    /**
     * @param receiveWarehouse the receiveWarehouse to set
     */
    public void setReceiveWarehouse(double receiveWarehouse) {
        this.receiveWarehouse = receiveWarehouse;
    }

    /**
     * @return the returnSales
     */
    public double getReturnSales() {
        return returnSales;
    }

    /**
     * @param returnSales the returnSales to set
     */
    public void setReturnSales(double returnSales) {
        this.returnSales = returnSales;
    }

    /**
     * @return the totalIn
     */
    public double getTotalIn() {
        return totalIn;
    }

    public double getTotalInCalculation() {
//         private double receiveSupplier=0;
//    private double receiveWarehouse=0;
//    private double returnSales=0;
        totalIn = this.getReceiveSupplier() + this.receiveWarehouse + this.getReturnSales() + this.getReturnSales();
        return totalIn;
    }

    /**
     * @param totalIn the totalIn to set
     */
    public void setTotalIn(double totalIn) {
        this.totalIn = totalIn;
    }

    /**
     * @return the transfer
     */
    public double getTransfer() {
        return transfer;
    }

    /**
     * @param transfer the transfer to set
     */
    public void setTransfer(double transfer) {
        this.transfer = transfer;
    }

    /**
     * @return the returnSupplier
     */
    public double getReturnSupplier() {
        return returnSupplier;
    }

    /**
     * @param returnSupplier the returnSupplier to set
     */
    public void setReturnSupplier(double returnSupplier) {
        this.returnSupplier = returnSupplier;
    }

    /**
     * @return the sales
     */
    public double getSales() {
        return sales;
    }

    /**
     * @param sales the sales to set
     */
    public void setSales(double sales) {
        this.sales = sales;
    }

    /**
     * @return the cost
     */
    public double getCost() {
        return cost;
    }

    /**
     * @param cost the cost to set
     */
    public void setCost(double cost) {
        this.cost = cost;
    }

    /**
     * @return the totalOut
     */
    public double getTotalOut() {
        return totalOut;
    }

    public double getTotalOutCalculation() {
        totalOut = this.getTransfer() + this.getReturnSupplier() + this.getSales() + this.getCost();
        return totalOut;
    }

    /**
     * @param totalOut the totalOut to set
     */
    public void setTotalOut(double totalOut) {
        this.totalOut = totalOut;
    }

    /**
     * @return the subTotal
     */
    public double getSubTotal() {
        return subTotal;
    }

    /**
     * @param subTotal the subTotal to set
     */
    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    /**
     * @return the opname
     */
    public double getOpname() {
        return opname;
    }

    /**
     * @param opname the opname to set
     */
    public void setOpname(double opname) {
        this.opname = opname;
    }

    /**
     * @return the selisihOpnamePlus
     */
    public double getSelisihOpnamePlus() {
        return selisihOpnamePlus;
    }

    /**
     * @param selisihOpnamePlus the selisihOpnamePlus to set
     */
    public void setSelisihOpnamePlus(double selisihOpnamePlus) {
        this.selisihOpnamePlus = selisihOpnamePlus;
    }

    /**
     * @return the selisihOpnameMinus
     */
    public double getSelisihOpnameMinus() {
        return selisihOpnameMinus;
    }

    /**
     * @param selisihOpnameMinus the selisihOpnameMinus to set
     */
    public void setSelisihOpnameMinus(double selisihOpnameMinus) {
        this.selisihOpnameMinus = selisihOpnameMinus;
    }

    /**
     * @return the balance
     */
    public double getBalance() {
        //balance=
        return balance;
    }

    /**
     * @param balance the balance to set
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getProductionOut() {
        return productionOut;
    }

    public void setProductionOut(double productionOut) {
        this.productionOut = productionOut;
    }

    public double getProductionIn() {
        return productionIn;
    }

    public void setProductionIn(double productionIn) {
        this.productionIn = productionIn;
    }

}

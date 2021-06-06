package com.dimata.posbo.entity.warehouse;

/* package java */
import com.dimata.common.entity.logger.I_LogHistory;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.PstUnit;
import com.dimata.posbo.entity.masterdata.Unit;

/* package qdep */
import com.dimata.qdep.entity.*;
import org.json.JSONObject;

public class MatStockOpnameItem extends Entity implements I_LogHistory {

    private long stockOpnameId;
    private long materialId;
    private long unitId;
    private double qtyOpname = 0;
    private double qtySold = 0;
    private double qtySystem = 0;
    private double cost = 0.00;
    private double price = 0.00;
    private int stockOpnameCounter = 0;
    private long kadarId = 0;
    private long kadarOpnameId = 0;
    private double berat = 0;
    private double beratOpname = 0;
    private String remark = "";
    private double beratSelisih = 0;
    private double qtyItem = 0;
    private double qtySelisih = 0;

    public long getStockOpnameId() {
        return stockOpnameId;
    }

    public void setStockOpnameId(long matStockOpnameId) {
        this.stockOpnameId = matStockOpnameId;
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

    public double getQtyOpname() {
        return qtyOpname;
    }

    public void setQtyOpname(double qtyOpname) {
        this.qtyOpname = qtyOpname;
    }

    public double getQtySold() {
        return qtySold;
    }

    public void setQtySold(double qtySold) {
        this.qtySold = qtySold;
    }

    public double getQtySystem() {
        return qtySystem;
    }

    public void setQtySystem(double qtySystem) {
        this.qtySystem = qtySystem;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return the stockOpnameCounter
     */
    public int getStockOpnameCounter() {
        return stockOpnameCounter;
    }

    /**
     * @param stockOpnameCounter the stockOpnameCounter to set
     */
    public void setStockOpnameCounter(int stockOpnameCounter) {
        this.stockOpnameCounter = stockOpnameCounter;
    }

    public long getKadarId() {
        return kadarId;
    }

    public void setKadarId(long kadarId) {
        this.kadarId = kadarId;
    }

    public long getKadarOpnameId() {
        return kadarOpnameId;
    }

    public void setKadarOpnameId(long kadarOpnameId) {
        this.kadarOpnameId = kadarOpnameId;
    }

    public double getBerat() {
        return berat;
    }

    public void setBerat(double berat) {
        this.berat = berat;
    }

    public double getBeratOpname() {
        return beratOpname;
    }

    public void setBeratOpname(double beratOpname) {
        this.beratOpname = beratOpname;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public double getBeratSelisih() {
        return beratSelisih;
    }

    public void setBeratSelisih(double beratSelisih) {
        this.beratSelisih = beratSelisih;
    }

    public double getQtyItem() {
        return qtyItem;
    }

    public void setQtyItem(double qtyItem) {
        this.qtyItem = qtyItem;
    }

    public double getQtySelisih() {
        return qtySelisih;
    }

    public void setQtySelisih(double qtySelisih) {
        this.qtySelisih = qtySelisih;
    }

    //dyas 20131128
    @Override
    public String getLogDetail(Entity prevDocItem) {
        MatStockOpnameItem prevMatStockOpnameItem = (MatStockOpnameItem) prevDocItem;
        Material material = null;
        Unit unit = null;
        try {
            if (this != null && getMaterialId() != 0) {
                material = PstMaterial.fetchExc(getMaterialId());
            }
            if (this != null && getUnitId() != 0) {
                unit = PstUnit.fetchExc(getUnitId());
            }
        } catch (Exception e) {
        }
        return "SKU : " + material.getSku()
                + (prevMatStockOpnameItem == null || prevMatStockOpnameItem.getOID() == 0 || prevMatStockOpnameItem.getMaterialId() == 0 || prevMatStockOpnameItem.getMaterialId() != this.getMaterialId()
                        ? (" Nama Barang : " + material.getName()) : "")
                + (prevMatStockOpnameItem == null || prevMatStockOpnameItem.getOID() == 0 || prevMatStockOpnameItem.getUnitId() == 0 || prevMatStockOpnameItem.getUnitId() != this.getUnitId()
                        ? (" Unit : " + unit.getName()) : "")
                + (prevMatStockOpnameItem == null || prevMatStockOpnameItem.getOID() == 0 || prevMatStockOpnameItem.getQtyOpname() != this.getQtyOpname()
                        ? (" Qty Opname : " + this.getQtyOpname()) : "")
                + (prevMatStockOpnameItem == null || prevMatStockOpnameItem.getOID() == 0 || prevMatStockOpnameItem.getQtySold() != this.getQtySold()
                        ? (" Qty Sold : " + this.getQtySold()) : "")
                + (prevMatStockOpnameItem == null || prevMatStockOpnameItem.getOID() == 0 || prevMatStockOpnameItem.getCost() != this.getCost()
                        ? (" Cost : " + this.getCost()) : "")
                + (prevMatStockOpnameItem == null || prevMatStockOpnameItem.getOID() == 0 || prevMatStockOpnameItem.getPrice() != this.getPrice()
                        ? (" Price : " + this.getPrice()) : "");
    }
   public static JSONObject fetchJSON(long oid){
      JSONObject object = new JSONObject();
      try {
         MatStockOpnameItem matStockOpnameItem = PstMatStockOpnameItem.fetchExc(oid);
         object.put(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ITEM_ID], matStockOpnameItem.getOID());
         object.put(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID], matStockOpnameItem.getStockOpnameId());
         object.put(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_MATERIAL_ID], matStockOpnameItem.getMaterialId());
         object.put(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_UNIT_ID], matStockOpnameItem.getUnitId());
         object.put(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_OPNAME], matStockOpnameItem.getQtyOpname());
         object.put(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_SOLD], matStockOpnameItem.getQtySold());
         object.put(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_SYSTEM], matStockOpnameItem.getQtySystem());
         object.put(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_COST], matStockOpnameItem.getCost());
         object.put(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_PRICE], matStockOpnameItem.getPrice());
         object.put(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_COUNTER], matStockOpnameItem.getStockOpnameCounter());
         object.put(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_KADAR_ID], matStockOpnameItem.getKadarId());
         object.put(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_KADAR_OPNAME_ID], matStockOpnameItem.getKadarOpnameId());
         object.put(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_BERAT], matStockOpnameItem.getBerat());
         object.put(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_BERAT_OPNAME], matStockOpnameItem.getBeratOpname());
         object.put(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_REMARK], matStockOpnameItem.getRemark());
         object.put(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_BERAT_SELISIH], matStockOpnameItem.getBeratSelisih());
         object.put(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_ITEM], matStockOpnameItem.getQtyItem());
         object.put(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_QTY_SELISIH], matStockOpnameItem.getQtySelisih());
      }catch(Exception exc){}
      return object;
   }
}

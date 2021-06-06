package com.dimata.posbo.entity.warehouse;

/* package java */

import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.PstUnit;
import com.dimata.posbo.entity.masterdata.Unit;

/* package qdep */
import com.dimata.qdep.entity.*;

public class MatCostingItem extends Entity {

    private long costingMaterialId = 0;
    private long materialId = 0;
    private long unitId = 0;
    private double hpp = 0;
    private double qty = 0;
    private double residueQty = 0;
    private double balanceQty = 0;
    private double qtyComposite=0;
    private long parentId=0;
    private double hppComposite=0;
    private double totalHppComposite=0;
    private String spesialNote ="";
    private double weight=0;
    private double cost=0;
    private double totalHpp=0;
    
    public double getHpp() {
        return hpp;
    }

    public void setHpp(double hpp) {
        this.hpp = hpp;
    }

    public long getDispatchMaterialId() {
        return getCostingMaterialId();
    }

    public void setCostingMaterialId(long costingMaterialId) {
        this.costingMaterialId = costingMaterialId;
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

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    /** Getter for property residueQty.
     * @return Value of property residueQty.
     *
     */
    public double getResidueQty() {
        return this.residueQty;
    }

    /** Setter for property residueQty.
     * @param residueQty New value of property residueQty.
     *
     */
    public void setResidueQty(double residueQty) {
        this.residueQty = residueQty;
    }

    /**
     * @return the costingMaterialId
     */
    public long getCostingMaterialId() {
        return costingMaterialId;
    }

    /**
     * @return the balanceQty
     */
    public double getBalanceQty() {
        return balanceQty;
    }

    /**
     * @param balanceQty the balanceQty to set
     */
    public void setBalanceQty(double balanceQty) {
        this.balanceQty = balanceQty;
    }

    // by dyas - 20131126
    // tambah mehtods getLogDetail
    public String getLogDetail(Entity prevDocItem) {
        MatCostingItem prevMatCostingItem = (MatCostingItem)prevDocItem;
        Material material = null;
        Unit unit = null;
        try
        {
            if(this!=null && getMaterialId()!=0 )
            {
                material = PstMaterial.fetchExc(getMaterialId());
}
            if(this!=null && getUnitId()!=0 )
            {
                unit = PstUnit.fetchExc(getUnitId());
            }
        }
        catch(Exception e)
        {
        }
        return  "SKU : " + material.getSku()+

                (prevMatCostingItem == null ||  prevMatCostingItem.getOID()==0 || prevMatCostingItem.getMaterialId() == 0 ||  prevMatCostingItem.getMaterialId() != this.getMaterialId() ?
                (" Nama Barang : " + material.getName()) : "")+

                (prevMatCostingItem == null ||  prevMatCostingItem.getOID()==0 || prevMatCostingItem.getUnitId()== 0 ||  prevMatCostingItem.getUnitId() != this.getUnitId() ?
                (" Unit : " + unit.getName()) : "")+

                (prevMatCostingItem == null ||  prevMatCostingItem.getOID()==0 || prevMatCostingItem.getHpp()!= this.getHpp() ?
                ( " Hpp : " + this.getHpp() ) : "" )+

                (prevMatCostingItem == null ||  prevMatCostingItem.getOID()==0 || prevMatCostingItem.getQty()!= this.getQty() ?
                ( " Qty : " + this.getQty() ) : "" )
                ;
}

    /**
     * @return the qtyComposite
     */
    public double getQtyComposite() {
        return qtyComposite;
    }

    /**
     * @param qtyComposite the qtyComposite to set
     */
    public void setQtyComposite(double qtyComposite) {
        this.qtyComposite = qtyComposite;
    }

    /**
     * @return the parentId
     */
    public long getParentId() {
        return parentId;
    }

    /**
     * @param parentId the parentId to set
     */
    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    /**
     * @return the hppComposite
     */
    public double getHppComposite() {
        return hppComposite;
    }

    /**
     * @param hppComposite the hppComposite to set
     */
    public void setHppComposite(double hppComposite) {
        this.hppComposite = hppComposite;
    }

    /**
     * @return the totalHppComposite
     */
    public double getTotalHppComposite() {
        return totalHppComposite;
    }

    /**
     * @param totalHppComposite the totalHppComposite to set
     */
    public void setTotalHppComposite(double totalHppComposite) {
        this.totalHppComposite = totalHppComposite;
    }

    /**
     * @return the spesialNote
     */
    public String getSpesialNote() {
        return spesialNote;
    }

    /**
     * @param spesialNote the spesialNote to set
     */
    public void setSpesialNote(String spesialNote) {
        this.spesialNote = spesialNote;
    }
    
    /**
     * @return the weight
     */
    public double getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(double weight) {
        this.weight = weight;
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
     * @return the totalHpp
     */
    public double getTotalHpp() {
        return totalHpp;
    }

    /**
     * @param totalHpp the totalHpp to set
     */
    public void setTotalHpp(double totalHpp) {
        this.totalHpp = totalHpp;
    }

}

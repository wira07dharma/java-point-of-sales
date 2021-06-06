package com.dimata.posbo.entity.warehouse;

/* package java */
import com.dimata.common.entity.logger.I_LogHistory;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.PstUnit;
import com.dimata.posbo.entity.masterdata.Unit;

/* package qdep */
import com.dimata.qdep.entity.*;

public class MatReturnItem extends Entity implements I_LogHistory{
    
    private long returnMaterialId;
    private long materialId = 0;
    private long unitId = 0;
    private double cost = 0.00;
    private long currencyId = 0;
    private double qty = 0.00;
    private double total = 0.00;
    private double residueQty = 0.00;
    private double berat = 0.00;
    private double ongkos = 0.00;
    private double hargaJual = 0;
    
    public long getReturnMaterialId() {
        return returnMaterialId;
    }
    
    public void setReturnMaterialId(long returnMaterialId) {
        this.returnMaterialId = returnMaterialId;
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
    
    public double getCost() {
        return cost;
    }
    
    public void setCost(double cost) {
        this.cost = cost;
    }
    
    public long getCurrencyId() {
        return currencyId;
    }
    
    public void setCurrencyId(long currencyId) {
        this.currencyId = currencyId;
    }
    
    public double getQty() {
        return qty;
    }
    
    public void setQty(double qty) {
        this.qty = qty;
    }
    
    public double getTotal() {
        return total;
    }
    
    public void setTotal(double total) {
        this.total = total;
    }
    
    public double getResidueQty() {
        return this.residueQty;
    }
    
    public void setResidueQty(double residueQty) {
        this.residueQty = residueQty;
    }

    public double getBerat() {
        return berat;
    }

    public void setBerat(double berat) {
        this.berat = berat;
    }

    public double getOngkos() {
        return ongkos;
    }

    public void setOngkos(double ongkos) {
        this.ongkos = ongkos;
    }

    public double getHargaJual() {
        return hargaJual;
    }

    public void setHargaJual(double hargaJual) {
        this.hargaJual = hargaJual;
    }
    
    // by dyas - 20131125
    // tambah mehtods getLogDetail
    public String getLogDetail(Entity prevDocItem) {
        MatReturnItem prevMatReturnItem = (MatReturnItem)prevDocItem;
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

                (prevMatReturnItem == null ||  prevMatReturnItem.getOID()==0 || prevMatReturnItem.getMaterialId() == 0 ||  prevMatReturnItem.getMaterialId() != this.getMaterialId() ?
                (" Nama Barang : " + material.getName()) : "")+

                (prevMatReturnItem == null ||  prevMatReturnItem.getOID()==0 || prevMatReturnItem.getUnitId()== 0 ||  prevMatReturnItem.getUnitId() != this.getUnitId() ?
                (" Unit : " + unit.getName()) : "")+

                (prevMatReturnItem == null ||  prevMatReturnItem.getOID()==0 || prevMatReturnItem.getCost()!= this.getCost() ?
                ( " Cost : " + this.getCost() ) : "" )+

                (prevMatReturnItem == null ||  prevMatReturnItem.getOID()==0 || prevMatReturnItem.getQty()!= this.getQty() ?
                ( " Qty : " + this.getQty() ) : "" )+

                (prevMatReturnItem == null ||  prevMatReturnItem.getOID()==0 || prevMatReturnItem.getTotal()!= this.getTotal() ?
                ( " Total : " + this.getTotal() ) : "" )
                ;
}

}

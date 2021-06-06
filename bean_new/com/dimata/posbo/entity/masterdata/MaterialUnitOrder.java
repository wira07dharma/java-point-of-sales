/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.masterdata;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author dimata005
 */
public class MaterialUnitOrder extends Entity {
    private long materialID;
    private long unitID;
    private double minimumQtyOrder;
    private String unitKode;
    /**
     * @return the materialID
     */
    public long getMaterialID() {
        return materialID;
    }

    /**
     * @param materialID the materialID to set
     */
    public void setMaterialID(long materialID) {
        this.materialID = materialID;
    }

    /**
     * @return the unitID
     */
    public long getUnitID() {
        return unitID;
    }

    /**
     * @param unitID the unitID to set
     */
    public void setUnitID(long unitID) {
        this.unitID = unitID;
    }

    /**
     * @return the minimumQtyOrder
     */
    public double getMinimumQtyOrder() {
        return minimumQtyOrder;
    }

    /**
     * @param minimumQtyOrder the minimumQtyOrder to set
     */
    public void setMinimumQtyOrder(double minimumQtyOrder) {
        this.minimumQtyOrder = minimumQtyOrder;
    }

    /**
     * @return the unitKode
     */
    public String getUnitKode() {
        return unitKode;
    }

    /**
     * @param unitKode the unitKode to set
     */
    public void setUnitKode(String unitKode) {
        this.unitKode = unitKode;
    }
    
    
    
    // update by fitra 01-05-2014
    
         public String getLogDetail(Entity prevDoc) {
        String includePpnWord = "";
        Material material = new Material();
        
        Unit unit = new Unit();
       
       
        MaterialUnitOrder prevMaterialUnitOrder = (MaterialUnitOrder)prevDoc;
        
        
        
        
        try {
            if (this.getMaterialID() != 0){
                
                material = PstMaterial.fetchExc(getMaterialID());
                
                
                
}
        }catch(Exception exc){
            
            
        }
        
        
        
        
         try {
            if (this.getUnitID() != 0){
                
                unit = PstUnit.fetchExc(getUnitID());
                
                
                
            }
        }catch(Exception exc){
            
            
        }
        
     
        return
        
       (prevMaterialUnitOrder == null ||  prevMaterialUnitOrder.getOID()==0 ||  prevMaterialUnitOrder.getMaterialID()!=this.getMaterialID()?
       ("Kode: "+ material.getName() +" ;" ) : "" ) +
                
       
                
                
       
                
                
                
      
         
        (prevMaterialUnitOrder == null ||  prevMaterialUnitOrder.getOID()==0 ||  prevMaterialUnitOrder.getUnitID()!=this.getUnitID() ?
       ("Nama: "+ unit.getCode() +" ;" ) : "" ) +
                
                
//      (prevMaterialUnitOrder == null ||  prevMaterialUnitOrder.getOID()==0 || currency!=null?
//       ("Mata Uang  "+currency+":"+ " ;" ) : "" ) +
         
                
        (prevMaterialUnitOrder == null ||  prevMaterialUnitOrder.getOID()==0 ||  prevMaterialUnitOrder.getMinimumQtyOrder()!=this.getMinimumQtyOrder() ?
       ("Standar rate: "+ this.getMinimumQtyOrder() +" ;" ) : "" ); 
    }
    
    
    
    
}

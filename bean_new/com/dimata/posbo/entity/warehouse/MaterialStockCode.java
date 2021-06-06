/**
 * Created by IntelliJ IDEA.
 * User: gadnyana
 * Date: Dec 9, 2004
 * Time: 11:22:01 AM
 * To change this template use Options | File Templates.
 */
package com.dimata.posbo.entity.warehouse;

import com.dimata.qdep.entity.Entity;
import java.util.Date;
import org.json.JSONObject;
/**
 * update opie-eyek 20130906 untuk stockValue nilai stock fifo
 * @author dimata005
 */
public class MaterialStockCode extends Entity {
    private long materialId = 0;
    private long locationId = 0;
    private int stockStatus = 0;
    private String stockCode = "";
    private double stockValue;
    private Date dateStock = new Date();
     
    public void setMaterialId(long materialId) {
        this.materialId = materialId;
    }

    public void setStockStatus(int stockStatus) {
        this.stockStatus = stockStatus;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    public long getMaterialId() {
        return materialId;
    }

    public int getStockStatus() {
        return stockStatus;
    }

    public String getStockCode() {
        return stockCode;
    }

    public long getLocationId() {
        return locationId;
    }

    /**
     * @return the stockValue
     */
    public double getStockValue() {
        return stockValue;
    }

    /**
     * @param stockValue the stockValue to set
     */
    public void setStockValue(double stockValue) {
        this.stockValue = stockValue;
    }

    /**
     * @return the dateStock
     */
    public Date getDateStock() {
        return dateStock;
    }

    /**
     * @param dateStock the dateStock to set
     */
    public void setDateStock(Date dateStock) {
        this.dateStock = dateStock;
    }
    
   public static JSONObject fetchJSON(long oid){
      JSONObject object = new JSONObject();
      try {
         MaterialStockCode materialStockCode = PstMaterialStockCode.fetchExc(oid);
         object.put(PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_MATERIAL_STOCK_CODE_ID], materialStockCode.getOID());
         object.put(PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_MATERIAL_ID], materialStockCode.getMaterialId());
         object.put(PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_LOCATION_ID], materialStockCode.getLocationId());
         object.put(PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_STOCK_CODE], materialStockCode.getStockStatus());
         object.put(PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_STOCK_STATUS], materialStockCode.getStockCode());
         object.put(PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_STOCK_VALUE], materialStockCode.getStockValue());
         object.put(PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_STOCK_DATE], materialStockCode.getDateStock());
      }catch(Exception exc){}
      return object;
   }
}

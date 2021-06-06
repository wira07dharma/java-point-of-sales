/* Created on 	:  [date] [time] AM/PM
 *
 * @author	 :
 * @version	 :
 */

/*******************************************************************
 * Class Description 	: [project description ... ]
 * Imput Parameters 	: [input parameter ...]
 * Output 		: [output ...]
 *******************************************************************/

package com.dimata.posbo.entity.masterdata;

/* package java */

import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

/* package material */
import com.dimata.posbo.session.masterdata.*;
import java.util.Vector;

public class Category extends Entity {

    private String code = "";
    private String name = "";
    private double pointPrice = 1;
    //private double pointPrice = 0;
    private String description = "";
    //adding production location for modul restaurant
    // by mirahu 20120511
    private long locationId = 0;
    private long CatParentId=0;
    private int status=0;
    
    private long categoryId=0;
    private int typeCategory=0;
    private int kenaikanHarga=0;
    
    private Vector resultChildParent= new Vector();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        if (code == null) {
            code = "";
        }
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null) {
            name = "";
        }
        this.name = name;
    }

    /**
     * Getter for property pointPrice.
     * @return Value of property pointPrice.
     */
    public double getPointPrice() {
       // if (pointPrice == 0) {
           // pointPrice = 1;
      //  }
       if (pointPrice == 0) {
           pointPrice = 0;
      }
        return pointPrice;
    }

    /**
     * Setter for property pointPrice.
     * @param pointPrice New value of property pointPrice.
     */
    public void setPointPrice(double pointPrice) {
        this.pointPrice = pointPrice;
    }

    /**
     * @return the locationId
     */
    public long getLocationId() {
        return locationId;
    }

    /**
     * @param locationId the locationId to set
     */
    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    /**
     * @return the CatParentId
     */
    public long getCatParentId() {
        return CatParentId;
    }

    /**
     * @param CatParentId the CatParentId to set
     */
    public void setCatParentId(long CatParentId) {
        this.CatParentId = CatParentId;
    }

    /**
     * @return the resultChildParent
     */
    public Vector getResultChildParent() {
        return resultChildParent;
    }

    /**
     * @param resultChildParent the resultChildParent to set
     */
    public void setResultChildParent(Vector resultChildParent) {
        this.resultChildParent = resultChildParent;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return the categoryId
     */
    public long getCategoryId() {
        return categoryId;
    }

    /**
     * @param categoryId the categoryId to set
     */
    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * @return the typeCategory
     */
    public int getTypeCategory() {
        return typeCategory;
    }

    /**
     * @param typeCategory the typeCategory to set
     */
    public void setTypeCategory(int typeCategory) {
        this.typeCategory = typeCategory;
    }
      /**
   * @return the kenaikanHarga
   */
  public int getKenaikanHarga() {
    return kenaikanHarga;
  }

  /**
   * @param kenaikanHarga the kenaikanHarga to set
   */
  public void setKenaikanHarga(int kenaikanHarga) {
    this.kenaikanHarga = kenaikanHarga;
  }

}

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

/* package qdep */
import com.dimata.qdep.entity.*;

/* package material */
import com.dimata.posbo.session.masterdata.*;
import java.util.Date;
import java.util.Vector;

public class CaCommission extends Category {
    
    public static final int STS_FINAL  = 2;
    public static final int STS_DRAFT  = 0;
    public static final int STS_CLOSED = 5;
    public static final class STATUS {
      public static final int MAP[] = {0, 0, 1, 0, 0, 2};
      public static final String NAME[] = {"Draft", "Final", "Closed"};
      public static final int VALUE[] = {0, 2, 5};
      public static final int size = 3;
    };
    
    private float percentage = 0;
    private long catId = 0;
    private Date startDate = null;
    private Date endDate = null;
    private String dedcription = "";
    private int status = STS_DRAFT;

    /**
     * @return the percentage
     */
    public float getPercentage() {
      return percentage;
    }

    /**
     * @param percentage the percentage to set
     */
    public void setPercentage(float percentage) {
      this.percentage = percentage;
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {
      return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
      this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
      return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
      this.endDate = endDate;
    }

    /**
     * @return the dedcription
     */
    public String getDedcription() {
      return dedcription;
    }

    /**
     * @param dedcription the dedcription to set
     */
    public void setDedcription(String dedcription) {
      this.dedcription = dedcription;
    }

    /**
     * @return the status
     */
    public int getComStatus() {
      return status;
    }

    /**
     * @param status the status to set
     */
    public void setComStatus(int status) {
      this.status = status;
    }

    /**
     * @return the categoryId
     */
    public long getCatId() {
      return catId;
    }

    /**
     * @param categoryId the categoryId to set
     */
    public void setCatId(long categoryId) {
      this.catId = categoryId;
    }
}

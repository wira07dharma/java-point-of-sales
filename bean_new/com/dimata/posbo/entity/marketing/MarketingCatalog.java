/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.marketing;

import com.dimata.qdep.entity.Entity;
import java.util.Date;

/**T
 *
 * @author Sunima
 */
public class MarketingCatalog extends Entity {
    
    
    
    private String marketing_katalog_title ="";
    private Date marketing_katalog_start_date;
    private Date marketing_katalog_end_date;
    private int marketing_katalog_status = 0;

    /**
     * @return the marketing_katalog_title
     */
    public String getMarketing_katalog_title() {
        return marketing_katalog_title;
    }

    /**
     * @param marketing_katalog_title the marketing_katalog_title to set
     */
    public void setMarketing_katalog_title(String marketing_katalog_title) {
        this.marketing_katalog_title = marketing_katalog_title;
    }

    /**
     * @return the marketing_katalog_start_date
     */
    public Date getMarketing_katalog_start_date() {
        return marketing_katalog_start_date;
    }

    /**
     * @param marketing_katalog_start_date the marketing_katalog_start_date to set
     */
    public void setMarketing_katalog_start_date(Date marketing_katalog_start_date) {
        this.marketing_katalog_start_date = marketing_katalog_start_date;
    }

    /**
     * @return the marketing_katalog_end_date
     */
    public Date getMarketing_katalog_end_date() {
        return marketing_katalog_end_date;
    }

    /**
     * @param marketing_katalog_end_date the marketing_katalog_end_date to set
     */
    public void setMarketing_katalog_end_date(Date marketing_katalog_end_date) {
        this.marketing_katalog_end_date = marketing_katalog_end_date;
    }

    /**
     * @return the marketing_katalog_status
     */
    public int getMarketing_katalog_status() {
        return marketing_katalog_status;
    }

    /**
     * @param marketing_katalog_status the marketing_katalog_status to set
     */
    public void setMarketing_katalog_status(int marketing_katalog_status) {
        this.marketing_katalog_status = marketing_katalog_status;
    }
   
    
}

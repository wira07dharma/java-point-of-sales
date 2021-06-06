/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.marketing;

import com.dimata.qdep.entity.Entity;
import java.util.Date;

/**
 *
 * @author Sunima
 */
public class MarketingManagement extends Entity{
    
        
    
    private String marketing_description = "";
    private Date start_date;
    private Date end_date;
    private Date create_date;
    private Date checked_date;
    private Date edited_date;
    private String marketing_title = "";
    private int marketing_status = 0;
    private String marketing_note = "";
    

    /**
     * @return the marketing_description
     */
    public String getMarketing_description() {
        return marketing_description;
    }

    /**
     * @param marketing_description the marketing_description to set
     */
    public void setMarketing_description(String marketing_description) {
        this.marketing_description = marketing_description;
    }

    /**
     * @return the start_date
     */
    public Date getStart_date() {
        return start_date;
    }

    /**
     * @param start_date the start_date to set
     */
    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    /**
     * @return the end_date
     */
    public Date getEnd_date() {
        return end_date;
    }

    /**
     * @param end_date the end_date to set
     */
    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    /**
     * @return the create_date
     */
    public Date getCreate_date() {
        return create_date;
    }

    /**
     * @param create_date the create_date to set
     */
    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    /**
     * @return the checked_date
     */
    public Date getChecked_date() {
        return checked_date;
    }

    /**
     * @param checked_date the checked_date to set
     */
    public void setChecked_date(Date checked_date) {
        this.checked_date = checked_date;
    }

    /**
     * @return the edited_date
     */
    public Date getEdited_date() {
        return edited_date;
    }

    /**
     * @param edited_date the edited_date to set
     */
    public void setEdited_date(Date edited_date) {
        this.edited_date = edited_date;
    }

    /**
     * @return the marketing_title
     */
    public String getMarketing_title() {
        return marketing_title;
    }

    /**
     * @param marketing_title the marketing_title to set
     */
    public void setMarketing_title(String marketing_title) {
        this.marketing_title = marketing_title;
    }

    /**
     * @return the marketing_status
     */
    public int getMarketing_status() {
        return marketing_status;
    }

    /**
     * @param marketing_status the marketing_status to set
     */
    public void setMarketing_status(int marketing_status) {
        this.marketing_status = marketing_status;
    }

    /**
     * @return the marketing_note
     */
    public String getMarketing_note() {
        return marketing_note;
    }

    /**
     * @param marketing_note the marketing_note to set
     */
    public void setMarketing_note(String marketing_note) {
        this.marketing_note = marketing_note;
    }

    
}

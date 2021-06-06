/*
 * SrcLapKegiatanUsaha.java
 *
 * Created on December 19, 2007, 2:06 PM
 */

package com.dimata.aiso.entity.search;

import java.util.Date;

/**
 *
 * @author  dwi
 */
public class SrcLapKegiatanUsaha {
    
    private Date dateFrom;
    private Date dateTo;
    
    public Date getDateFrom(){
        return dateFrom;
    }
    
    public void setDateFrom(Date dateFrom){
        this.dateFrom = dateFrom;
    }
    
    public Date getDateTo(){
        return dateTo;
    }
    
    public void setDateTo(Date dateTo){
        this.dateTo = dateTo;
    }
}


/* Created on 	:  [date] [time] AM/PM
 *
 * @author  	: karya
 * @version  	: 01
 */
/*******************************************************************
 * Class Description 	: [project description ... ]
 * Imput Parameters 	: [input parameter ...]
 * Output 		: [output ...]
 *******************************************************************/
package com.dimata.posbo.entity.masterdata;

/* package java */
import java.io.*;
import java.util.*;

/* package qdep */
import com.dimata.qdep.entity.*;

public class TransferToServer extends Entity implements Serializable {

    private Date dateFrom = new Date();
    private Date dateTo = new Date();



    public String getPstClassName() {
        return "com.dimata.common.entity.masterdata.TransferToOutlet";
    }

    /**
     * @return the dateFrom
     */
    public Date getDateFrom() {
        return dateFrom;
    }

    /**
     * @param dateFrom the dateFrom to set
     */
    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    /**
     * @return the dateTo
     */
    public Date getDateTo() {
        return dateTo;
    }

    /**
     * @param dateTo the dateTo to set
     */
    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }
   
   
}
